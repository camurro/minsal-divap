package minsal.divap.service;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.transaction.Transaction;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.sun.org.apache.xpath.internal.axes.HasPositionalPredChecker;

import minsal.divap.dao.ComunaDAO;
import minsal.divap.dao.EstablecimientosDAO;
import minsal.divap.dao.ProgramasDAO;
import minsal.divap.dao.RecursosFinancierosProgramasReforzamientoDAO;
import minsal.divap.dao.ServicioSaludDAO;
import minsal.divap.dao.TipoSubtituloDAO;
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.enums.EstadosProgramas;
import minsal.divap.enums.FieldType;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.excel.GeneradorExcel;
import minsal.divap.excel.impl.PercapitaCalculoPercapitaExcelValidator;
import minsal.divap.excel.impl.ProgramaAPSMunicipalExcelValidator;
import minsal.divap.excel.impl.ProgramaAPSMunicipalesSheetExcel;
import minsal.divap.excel.impl.ProgramaAPSServicioSheetExcel;
import minsal.divap.exception.ExcelFormatException;
import minsal.divap.model.mappers.ProgramaMapper;
import minsal.divap.vo.BodyVO;
import minsal.divap.vo.CalculoPercapitaVO;
import minsal.divap.vo.CellExcelVO;
import minsal.divap.vo.CellTypeExcelVO;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ProgramaAPSMunicipalVO;
import minsal.divap.vo.ProgramaAPSServicioVO;
import minsal.divap.vo.ProgramaAPSVO;
import minsal.divap.vo.ProgramaCoreVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ServicioComunaVO;
import minsal.divap.vo.SubtituloVO;
import minsal.divap.vo.TipoComponenteVO;
import cl.minsal.divap.model.AntecendentesComuna;
import cl.minsal.divap.model.AntecendentesComunaCalculado;
import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.DistribucionInicialPercapita;
import cl.minsal.divap.model.Establecimiento;
import cl.minsal.divap.model.EstadoPrograma;
import cl.minsal.divap.model.Programa;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.ProgramaMunicipalCore;
import cl.minsal.divap.model.ProgramaMunicipalCoreComponente;
import cl.minsal.divap.model.ProgramaMunicipalCoreComponentePK;
import cl.minsal.divap.model.ProgramaServicioCore;
import cl.minsal.divap.model.ProgramaServicioCoreComponente;
import cl.minsal.divap.model.ProgramaServicioCoreComponentePK;
import cl.minsal.divap.model.ServicioSalud;
import cl.minsal.divap.model.TipoSubtitulo;
import cl.minsal.divap.model.Usuario;

@Stateless
@LocalBean
public class RecursosFinancierosProgramasReforzamientoService {
	@EJB
	private UsuarioDAO usuarioDAO;
	@EJB
	private ProgramasDAO programasDAO;
	@EJB
	private ComunaDAO comunaDAO;
	@EJB
	private EstablecimientosDAO establecimientoDAO;
	@EJB
	private TipoSubtituloDAO tipoSubtituloDAO;
	@EJB
	private ServicioSaludDAO servicioSaludDAO;
	@EJB
	private RecursosFinancierosProgramasReforzamientoDAO recursosFinancierosProgramasReforzamientoDAO;
	@EJB
	private AlfrescoService alfrescoService;
	@EJB
	private DocumentService documentService;
	@EJB
	private ProgramasService programaService;
	@EJB
	private ComunaService comunaService;
	@EJB
	private EstablecimientosService establecimientoService;
	@EJB
	private ComponenteService componenteService;
	@Resource(name="tmpDir")
	private String tmpDir;
	@Resource(name="folderTemplateRecursosFinancierosAPS")
	private String folderTemplateRecursosFinancierosAPS;
	@Resource(name="folderRecursosFinancierosAPS")
	private String folderRecursosFinancierosAPS;


	public Integer crearIntanciaProgramaAno(String username){
		System.out.println("username-->"+username);
		Usuario usuario = this.usuarioDAO.getUserByUsername(username);
		return recursosFinancierosProgramasReforzamientoDAO.crearIntanciaProgramaAno(usuario);
	}

	public Integer cargarPlantilla(TipoDocumentosProcesos plantilla, File file){
		Integer plantillaId = documentService.getPlantillaByType(plantilla);
		String filename = null;
		switch (plantilla) {
		case PLANTILLAPROGRAMAAPSMUNICIPALES:
			filename = "plantillaProgramasAPSMunicipal.xlsx";
			break;
		case PLANTILLAPROGRAMAAPSMUNICIPALEXCEPCION:
			filename = "plantillaProgramasAPSMunicipalExcepcion.xlsx";
			break;	
		case PLANTILLAPROGRAMAAPSSERVICIO:
			filename = "plantillaProgramasAPSServicio.docx";
			break;	
		default:
			break;
		}
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		filename = tmpDir + File.separator + filename;
		String contentType = mimemap.getContentType(filename.toLowerCase());
		if(plantillaId == null){
			try {
				BodyVO response = alfrescoService.uploadDocument(file, contentType, folderTemplateRecursosFinancierosAPS);
				System.out.println("response upload template --->"+response);
				plantillaId = documentService.createTemplate(plantilla, response.getNodeRef(), response.getFileName(), contentType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			Integer referenciaDocumentoId = documentService.getDocumentoIdByPlantillaId(plantillaId);
			try {
				BodyVO response = alfrescoService.uploadDocument(file, contentType, folderTemplateRecursosFinancierosAPS);
				System.out.println("response upload template --->"+response);
				documentService.updateDocumentTemplate(referenciaDocumentoId, response.getNodeRef(), response.getFileName(), contentType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return plantillaId;
	}

	public Integer getIdPlantillaProgramas(Integer programaSeleccionado, TipoDocumentosProcesos tipoDocumentoProceso){
		Integer plantillaId = documentService.getPlantillaByTypeAndProgram(tipoDocumentoProceso, programaSeleccionado);
	    Programa prog =  programaService.getProgramasByID(programaSeleccionado);
		ProgramaVO programa;
		if(plantillaId == null){
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			programa = new ProgramaMapper().getBasic(programasDAO.getProgramaAnoByID(programaSeleccionado));
				
			List<CellExcelVO> header = new ArrayList<CellExcelVO>();
			header.add(new CellExcelVO("Servicios de Salud", 2, 2));
			
			List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
			String filename = tmpDir + File.separator;
			GeneradorExcel generadorExcel = null;
			String contenType = null;
			switch (tipoDocumentoProceso) {
			case PLANTILLAPROGRAMAAPSMUNICIPALES:
				header.add(new CellExcelVO("Comunas", 2, 2));
				filename += "Plantilla Programa -"+prog.getNombre()+"- Municipal.xlsx";
				generadorExcel = new GeneradorExcel(filename);
				int subtitulos=0;
				for(ComponentesVO componente : programa.getComponentes()){
					for(SubtituloVO subtitulo : componente.getSubtitulos()){
						if(subtitulo.getId()==3){
							subtitulos++;
						}
					}
				header.add(new CellExcelVO(programa.getNombre(), subtitulos*2, 1));
				subHeader.add(new CellExcelVO("ID", 1, 2));
				subHeader.add(new CellExcelVO("Servicio de Salud", 1, 2));
				subHeader.add(new CellExcelVO("ID", 1, 2));
				subHeader.add(new CellExcelVO("Comuna", 1, 2));
				
					header.add(new CellExcelVO(componente.getNombre(), subtitulos*2, 1));
					for(SubtituloVO subtitulo : componente.getSubtitulos()){
						if(subtitulo.getId()==3){
							subHeader.add(new CellExcelVO(subtitulo.getNombre(), 2, 1));
							subHeader.add(new CellExcelVO("Tarifa", 1, 1));
							subHeader.add(new CellExcelVO("Cantidad", 1, 1));
						}
						
					}
				}
				
				contenType = mimemap.getContentType(filename.toLowerCase());
				List<ProgramaAPSVO> servicioComunas = comunaService.getServiciosComunas();
				ProgramaAPSMunicipalesSheetExcel programaAPSMunicipalesSheetExcel = new ProgramaAPSMunicipalesSheetExcel(header, subHeader, servicioComunas);
				generadorExcel.addSheet(programaAPSMunicipalesSheetExcel, "Hoja 1");
				
				break;
			case PLANTILLAPROGRAMAAPSSERVICIO:
				filename += "Plantilla Programa -"+prog.getNombre()+"- Servicio.xlsx";
				generadorExcel = new GeneradorExcel(filename);
				header.add(new CellExcelVO("Establecimientos", 2, 2));
				int subtitulosServicio=0;
				for(ComponentesVO componente : programa.getComponentes()){
					for(SubtituloVO subtitulo : componente.getSubtitulos()){
						if(subtitulo.getId()!=3){
							subtitulosServicio++;
						}
					}
				header.add(new CellExcelVO(programa.getNombre(), (2 * subtitulosServicio), 1));
				subHeader.add(new CellExcelVO("ID", 1, 2));
				subHeader.add(new CellExcelVO("Servicio de Salud", 1, 2));
				//subHeader.add(new CellExcelVO("ID", 1, 2));
				//subHeader.add(new CellExcelVO("Comuna", 1, 2));
				subHeader.add(new CellExcelVO("ID", 1, 2));
				subHeader.add(new CellExcelVO("Establecimiento", 1, 2));
				
					header.add(new CellExcelVO(componente.getNombre(),subtitulosServicio*2, 1));
					for(SubtituloVO subtitulo : componente.getSubtitulos()){
						if(subtitulo.getId()!=3){
							subHeader.add(new CellExcelVO(subtitulo.getNombre(), 2, 1));
							subHeader.add(new CellExcelVO("Tarifa", 1, 1));
							subHeader.add(new CellExcelVO("Cantidad", 1, 1));
						}
					}
				}
				contenType = mimemap.getContentType(filename.toLowerCase());
				List<ProgramaAPSServicioVO> servicioComunaEstablecimientos = establecimientoService.getServiciosComunasEstablecimientos();
				ProgramaAPSServicioSheetExcel programaAPSServicioSheetExcel = new ProgramaAPSServicioSheetExcel(header, subHeader, servicioComunaEstablecimientos);
				generadorExcel.addSheet(programaAPSServicioSheetExcel, "Hoja 1");
				break;
			default:
				break;
			}

			
			try {
				BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderTemplateRecursosFinancierosAPS);
				plantillaId = documentService.createTemplateProgramas(tipoDocumentoProceso, response.getNodeRef(), response.getFileName(), contenType, programa);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			plantillaId = documentService.getDocumentoIdByPlantillaId(plantillaId);
		}
		return plantillaId;
	}

	public Integer getAnoCurso() {
		DateFormat formatNowYear = new SimpleDateFormat("yyyy");
		Date nowDate = new Date();
		return Integer.valueOf(formatNowYear.format(nowDate)); 
	}

	public void cambiarEstadoPrograma(Integer idPrograma, EstadosProgramas estadoPrograma) {
		System.out.println("idPrograma-->" + idPrograma + " estadoPrograma.getId()-->"+estadoPrograma.getId());
		ProgramaAno programaAno = recursosFinancierosProgramasReforzamientoDAO.findById(idPrograma);

		programaAno.setEstado(new EstadoPrograma(estadoPrograma.getId()));
		System.out.println("Cambia estado ok");
	}

	public List<ProgramaVO> getProgramas(String username) {
		List<ProgramaVO> programasVO = new ArrayList<ProgramaVO>();
		List<ProgramaAno> programas = programasDAO.getProgramasByUserAno(username, getAnoCurso());
		if(programas != null && programas.size() > 0){
			for(ProgramaAno programa : programas){
				programasVO.add(new ProgramaMapper().getBasic(programa));
			}
		}
		return programasVO;
	}

	public ProgramaVO getProgramaById(Integer idPrograma) {
		ProgramaAno programaAno = recursosFinancierosProgramasReforzamientoDAO.findById(idPrograma);
		return new ProgramaMapper().getBasic(programaAno);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) 
	public void procesarPlanillaMunicipal(Integer idProgramaAno, XSSFWorkbook workbook, List<ComponentesVO> componentes) throws ExcelFormatException {
		
		List<ProgramaMunicipalCore> programaMunicipalesCore = recursosFinancierosProgramasReforzamientoDAO.getProgramasCoreByProgramaAno(idProgramaAno);
		if(programaMunicipalesCore != null && programaMunicipalesCore.size() > 0){
			List<Integer> idProgramasCore = new ArrayList<Integer>();
			for(ProgramaMunicipalCore programaMunicipalCore : programaMunicipalesCore){
				idProgramasCore.add(programaMunicipalCore.getIdProgramaMunicipalCore());
			}
			recursosFinancierosProgramasReforzamientoDAO.deleteProgramasMunicipalCore(idProgramasCore);
		}
		
		XSSFSheet worksheet = workbook.getSheetAt(0);
		
		int filasCabecera=4;
		persisteDataExcel(worksheet,filasCabecera,componentes,idProgramaAno);
		System.out.println("FIN PROCESAR PLANILLA MUNICIPAL");		
		
	}

	public void persisteDataExcel(XSSFSheet sheet, int filasCabecera, 
			List<ComponentesVO> componentes, Integer idProgramaAno) throws ExcelFormatException {
	try{
		
		if(sheet == null){
			throw new ExcelFormatException("La hoja de cálculo esta nula");
		}
				
		int last = sheet.getPhysicalNumberOfRows();
		int first = 0;
		
		System.out.println("Componentes del programa-->"+componentes.size());
		List<Integer> cantidadSubs = new ArrayList<Integer>();
		int totalSubtitulos = 0;
		for (int i = 0; i < componentes.size(); i++) {
			System.out.println("Componente: "+ componentes.get(i).getNombre()+" tiene: "+ componentes.get(i).getSubtitulos().size());
			cantidadSubs.add(componentes.get(i).getSubtitulos().size());
			totalSubtitulos += componentes.get(i).getSubtitulos().size();
		}
		
		int celdasPxQ = totalSubtitulos * 2;
		
		List<Integer> mergeComponentes = new ArrayList<Integer>();
		
		for (int i = 0; i < componentes.size(); i++) {
			System.out.println(componentes.get(i).getNombre()+" tiene "+cantidadSubs.get(i)+" subtitulos");
			mergeComponentes.add(cantidadSubs.get(i)*2);
		}
		XSSFRow xssfRow;
		int subtitulosMunicipales=0;
		
		List<SubtituloVO> subtitulosMunis = new ArrayList<SubtituloVO>();
		List<ComponentesVO> composMunis = new ArrayList<ComponentesVO>();
		for (int j = 0; j <componentes.size(); j++) {
			SubtituloVO subM = null;
			ComponentesVO comM = null;
			for(int k=0; k < componentes.get(j).getSubtitulos().size();k++){
				subM = new SubtituloVO();
				comM = new ComponentesVO();
				SubtituloVO subMun = componentes.get(j).getSubtitulos().get(k);
				if(subMun.getId()==3){					
					subM.setDependencia(subMun.getDependencia());
					subM.setId(subMun.getId());
					subM.setNombre(subMun.getNombre());
					
					comM.setId(componentes.get(j).getId());
					comM.setNombre(componentes.get(j).getNombre());
					comM.setPeso(componentes.get(j).getPeso());
					comM.setTipoComponente(componentes.get(j).getTipoComponente());
					
					subtitulosMunicipales++;
				}
			}
			if(subM!=null && comM!=null){
				subtitulosMunis.add(subM);
				comM.setSubtitulos(subtitulosMunis);
				composMunis.add(comM);
			}
			
		}
		
		int columna=4;
		//itera componentes
		for (int j = 0; j <composMunis.size(); j++) {
			//itera subtitulos
			for(int k=0; k < subtitulosMunicipales;k++){
			    //itera filas
				for (int i = 4; i < last; i++) {	
					xssfRow = sheet.getRow(i);
					
					ProgramaMunicipalCore programaMunicipalCore = new ProgramaMunicipalCore();
					Comuna comuna = comunaDAO.getComunaById((Double.valueOf(xssfRow.getCell(2).toString())).intValue());
					programaMunicipalCore.setComuna(comuna);
					ProgramaAno programaAno = recursosFinancierosProgramasReforzamientoDAO.findById(idProgramaAno);
					programaMunicipalCore.setProgramaAnoMunicipal(programaAno);
					recursosFinancierosProgramasReforzamientoDAO.save(programaMunicipalCore);
					
					ProgramaMunicipalCoreComponente programaMunicipalCoreComponente = new ProgramaMunicipalCoreComponente();
					programaMunicipalCoreComponente.setProgramaMunicipalCore(programaMunicipalCore);
					
					programaMunicipalCoreComponente.setMonto((Double.valueOf(xssfRow.getCell(columna).toString()).intValue()));
					programaMunicipalCoreComponente.setCantidad((Double.valueOf(xssfRow.getCell(columna+1).toString()).intValue()));
					
					TipoSubtitulo tipoSubtitulo = tipoSubtituloDAO.getTipoSubtituloByName(composMunis.get(j).getSubtitulos().get(k).getNombre());
					programaMunicipalCoreComponente.setSubtitulo(tipoSubtitulo);
					
					int tarifa = (Double.valueOf(xssfRow.getCell(columna).toString())).intValue() * (Double.valueOf(xssfRow.getCell(columna+1).toString())).intValue();
					programaMunicipalCoreComponente.setTarifa(tarifa);
					
					Componente componen = componenteService.getComponenteById(composMunis.get(j).getId());
					programaMunicipalCoreComponente.setMunicipalCoreComponente(componen);
						
					ProgramaMunicipalCoreComponentePK pk = new ProgramaMunicipalCoreComponentePK();
					pk.setComponente(componen.getId());
					pk.setProgramaMunicipalCore(programaMunicipalCore.getIdProgramaMunicipalCore());
					programaMunicipalCoreComponente.setProgramaMunicipalCoreComponentePK(pk);
					recursosFinancierosProgramasReforzamientoDAO.save(programaMunicipalCoreComponente);
					
				}
				columna+=2;
			}
		}
		
		
	}catch(Exception e){
		e.printStackTrace();
	}

	}

	
	public void persisteDataServicioExcel(XSSFSheet sheet, int filasCabecera, 
			List<ComponentesVO> componentes, Integer idProgramaAno) throws ExcelFormatException {
	try{
		
		if(sheet == null){
			throw new ExcelFormatException("La hoja de cálculo esta nula");
		}
				
		int last = sheet.getPhysicalNumberOfRows();
		int first = 0;
		
		System.out.println("Componentes del programa-->"+componentes.size());
		List<Integer> cantidadSubs = new ArrayList<Integer>();
		int totalSubtitulos = 0;
		for (int i = 0; i < componentes.size(); i++) {
			System.out.println("Componente: "+ componentes.get(i).getNombre()+" tiene: "+ componentes.get(i).getSubtitulos().size());
			cantidadSubs.add(componentes.get(i).getSubtitulos().size());
			totalSubtitulos += componentes.get(i).getSubtitulos().size();
		}
		
		int celdasPxQ = totalSubtitulos * 2;
		
		List<Integer> mergeComponentes = new ArrayList<Integer>();
		
		for (int i = 0; i < componentes.size(); i++) {
			System.out.println(componentes.get(i).getNombre()+" tiene "+cantidadSubs.get(i)+" subtitulos");
			mergeComponentes.add(cantidadSubs.get(i)*2);
		}
		XSSFRow xssfRow;
		
		int subtitulosServicios=0;
		
		
		List<ComponentesVO> composServ= new ArrayList<ComponentesVO>();
		for (int j = 0; j <componentes.size(); j++) {
			for(int k=0; k < componentes.get(j).getSubtitulos().size();k++){
				
				SubtituloVO subMun = componentes.get(j).getSubtitulos().get(k);
				if(subMun.getId()!=3){					
					
					ComponentesVO comServ= new ComponentesVO();
					
					comServ.setId(componentes.get(j).getId());
					comServ.setNombre(componentes.get(j).getNombre());
					comServ.setPeso(componentes.get(j).getPeso());
					comServ.setTipoComponente(componentes.get(j).getTipoComponente());
					
					SubtituloVO subServ = new SubtituloVO();
					subServ.setDependencia(subMun.getDependencia());
					subServ.setId(subMun.getId());
					subServ.setNombre(subMun.getNombre());
					
					int existe = composServ.indexOf(comServ);
					if(existe == -1){
						List<SubtituloVO> subtitulosServ = new ArrayList<SubtituloVO>();
						subtitulosServ.add(subServ);
						comServ.setSubtitulos(subtitulosServ);
						composServ.add(comServ);
					}else{
						composServ.get(existe).getSubtitulos().add(subServ);
					}
					
					subtitulosServicios++;
				}
			}
		}
		
		int columna=4;
		//itera componentes
		for (int j = 0; j <composServ.size(); j++) {
			//itera subtitulos
			for(int k=0; k < subtitulosServicios;k++){
			    //itera filas
				for (int i = 4; i < last; i++) {	
					xssfRow = sheet.getRow(i);
					
					ProgramaServicioCore programaServicioCore = new ProgramaServicioCore();
					
					Establecimiento establecimiento = establecimientoDAO.getEstablecimientoByCodigo(xssfRow.getCell(2).toString());
					programaServicioCore.setEstablecimiento(establecimiento);
					
					ProgramaAno programaAno = recursosFinancierosProgramasReforzamientoDAO.findById(idProgramaAno);
					programaServicioCore.setProgramaAnoServicio(programaAno);
					
					ServicioSalud servicioSalud = servicioSaludDAO.getById((Double.valueOf(xssfRow.getCell(0).toString())).intValue());
					programaServicioCore.setServicio(servicioSalud);
					recursosFinancierosProgramasReforzamientoDAO.save(programaServicioCore);
					
					
					ProgramaServicioCoreComponente programaServicioCoreComponente = new ProgramaServicioCoreComponente();
					programaServicioCoreComponente.setProgramaServicioCore1(programaServicioCore);
					
					programaServicioCoreComponente.setMonto((Double.valueOf(xssfRow.getCell(columna).toString()).intValue()));
					programaServicioCoreComponente.setCantidad((Double.valueOf(xssfRow.getCell(columna+1).toString()).intValue()));
					
					TipoSubtitulo tipoSubtitulo = tipoSubtituloDAO.getTipoSubtituloByName(composServ.get(j).getSubtitulos().get(k).getNombre());
					programaServicioCoreComponente.setSubtitulo(tipoSubtitulo);
					
					int tarifa = (Double.valueOf(xssfRow.getCell(columna).toString())).intValue() * (Double.valueOf(xssfRow.getCell(columna+1).toString())).intValue();
					programaServicioCoreComponente.setTarifa(tarifa);
					
					Componente componen = componenteService.getComponenteById(composServ.get(j).getId());
					programaServicioCoreComponente.setServicioCoreComponente(componen);
						
					ProgramaServicioCoreComponentePK pk = new ProgramaServicioCoreComponentePK();
					pk.setComponente(componen.getId());
					pk.setProgramaServicioCore(programaServicioCore.getIdProgramaServicioCore());
					programaServicioCoreComponente.setProgramaServicioCoreComponentePK(pk);
					recursosFinancierosProgramasReforzamientoDAO.save(programaServicioCoreComponente);
					
				}
				columna+=2;
			}
		}
		
		
	}catch(Exception e){
		e.printStackTrace();
	}

	}
	public void moveToAlfresco(Integer idProgramaAno,
			Integer docPlanillaMuncipal,
			TipoDocumentosProcesos programaapsmunicipal, Object object) {
		// TODO Auto-generated method stub
		
	}

	public void procesarPlanillaServicio(Integer idProgramaAno,
			XSSFWorkbook workbook, List<ComponentesVO> componentes) throws ExcelFormatException {
		
		List<ProgramaServicioCore> programaServiciosCore = recursosFinancierosProgramasReforzamientoDAO.getProgramaServicioCoreByProgramaAno(idProgramaAno);
		

		if(programaServiciosCore != null && programaServiciosCore.size() > 0){
			List<Integer> idProgramasCore = new ArrayList<Integer>();
			for(ProgramaServicioCore servicioCore : programaServiciosCore){
				idProgramasCore.add(servicioCore.getIdProgramaServicioCore());
			}
			recursosFinancierosProgramasReforzamientoDAO.deleteProgramasServiciosCore(idProgramasCore);
		}
		
		XSSFSheet worksheet = workbook.getSheetAt(0);
		
		int filasCabecera=4;
		persisteDataServicioExcel(worksheet,filasCabecera,componentes,idProgramaAno);
		System.out.println("FIN PROCESAR PLANILLA SERVICIO");		
		
	}

}
