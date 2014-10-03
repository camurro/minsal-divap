package minsal.divap.service;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import minsal.divap.dao.ComunaDAO;
import minsal.divap.dao.ProgramasDAO;
import minsal.divap.dao.RecursosFinancierosProgramasReforzamientoDAO;
import minsal.divap.dao.TipoSubtituloDAO;
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.enums.EstadosProgramas;
import minsal.divap.enums.FieldType;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.excel.GeneradorExcel;
import minsal.divap.excel.impl.PercapitaCalculoPercapitaExcelValidator;
import minsal.divap.excel.impl.ProgramaAPSMunicipalExcelValidator;
import minsal.divap.excel.impl.ProgramaAPSMunicipalesSheetExcel;
import minsal.divap.exception.ExcelFormatException;
import minsal.divap.model.mappers.ProgramaMapper;
import minsal.divap.vo.BodyVO;
import minsal.divap.vo.CalculoPercapitaVO;
import minsal.divap.vo.CellExcelVO;
import minsal.divap.vo.CellTypeExcelVO;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ProgramaAPSMunicipalVO;
import minsal.divap.vo.ProgramaCoreVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.SubtituloVO;
import cl.minsal.divap.model.AntecendentesComuna;
import cl.minsal.divap.model.AntecendentesComunaCalculado;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.DistribucionInicialPercapita;
import cl.minsal.divap.model.EstadoPrograma;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.ProgramaMunicipalCore;
import cl.minsal.divap.model.ProgramaMunicipalCoreComponente;
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
	private TipoSubtituloDAO tipoSubtituloDAO;
	@EJB
	private RecursosFinancierosProgramasReforzamientoDAO recursosFinancierosProgramasReforzamientoDAO;
	@EJB
	private AlfrescoService alfrescoService;
	@EJB
	private DocumentService documentService;
	@EJB
	private ProgramasService programaService;
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
		Integer plantillaId = documentService.getPlantillaByType(tipoDocumentoProceso);
		if(plantillaId == null){
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			ProgramaVO programa = new ProgramaMapper().getBasic(programasDAO.getProgramaAnoByID(programaSeleccionado));
			List<CellExcelVO> header = new ArrayList<CellExcelVO>();
			header.add(new CellExcelVO("Servicios de Salud", 2, 2));
			header.add(new CellExcelVO("Comunas", 2, 2));
			List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
			String filename = tmpDir + File.separator;
			switch (tipoDocumentoProceso) {
			case PLANTILLAPROGRAMAAPSMUNICIPALES:
				filename += "Plantilla Programa APS-Municipal.xlsx";
				header.add(new CellExcelVO(programa.getNombre(), 2, 1));
				subHeader.add(new CellExcelVO("ID", 1, 2));
				subHeader.add(new CellExcelVO("Servicio de Salud", 1, 2));
				subHeader.add(new CellExcelVO("ID", 1, 2));
				subHeader.add(new CellExcelVO("Comuna", 1, 2));
				for(ComponentesVO componente : programa.getComponentes()){
					header.add(new CellExcelVO(componente.getNombre(), 2, 1));
					for(SubtituloVO subtitulo : componente.getSubtitulos()){
						subHeader.add(new CellExcelVO(subtitulo.getNombre(), 2, 1));
						subHeader.add(new CellExcelVO("Tarifa", 1, 1));
						subHeader.add(new CellExcelVO("Cantidad", 1, 1));
					}
				}
				break;
			case PLANTILLAPROGRAMAAPSSERVICIO:
				filename += "Plantilla Programa APS-Servicio.xlsx";
				header.add(new CellExcelVO("Establecimientos", 2, 2));
				header.add(new CellExcelVO(programa.getNombre(), 1, (2 * programa.getComponentes().size())));
				subHeader.add(new CellExcelVO("ID", 1, 2));
				subHeader.add(new CellExcelVO("Servicio de Salud", 1, 2));
				subHeader.add(new CellExcelVO("ID", 1, 2));
				subHeader.add(new CellExcelVO("Comuna", 1, 2));
				subHeader.add(new CellExcelVO("ID", 1, 2));
				subHeader.add(new CellExcelVO("Establecimiento", 1, 2));
				for(ComponentesVO componente : programa.getComponentes()){
					header.add(new CellExcelVO(componente.getNombre(), 1, 2));
					for(SubtituloVO subtitulo : componente.getSubtitulos()){
						subHeader.add(new CellExcelVO(subtitulo.getNombre(), 2, 1));
						subHeader.add(new CellExcelVO("Tarifa", 1, 1));
						subHeader.add(new CellExcelVO("Cantidad", 1, 1));
					}
				}
				break;
			default:
				break;
			}

			String contenType = mimemap.getContentType(filename.toLowerCase());
			GeneradorExcel generadorExcel = new GeneradorExcel(filename);
			ProgramaAPSMunicipalesSheetExcel programaAPSMunicipalesSheetExcel = new ProgramaAPSMunicipalesSheetExcel(header, subHeader, null);
			generadorExcel.addSheet(programaAPSMunicipalesSheetExcel, "Hoja 1");
			try {
				BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderTemplateRecursosFinancierosAPS);
				System.out.println("response asignacionDesempenoDificilSheetExcel --->"+response);
				plantillaId = documentService.createTemplate(tipoDocumentoProceso, response.getNodeRef(), response.getFileName(), contenType);
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

	public void procesarPlanillaMunicipal(Integer idProgramaAno, XSSFWorkbook workbook) throws ExcelFormatException {
		List<CellTypeExcelVO> cells = new ArrayList<CellTypeExcelVO>();
		CellTypeExcelVO fieldOne = new CellTypeExcelVO(true, FieldType.INTEGERFIELD);
		cells.add(fieldOne);
		CellTypeExcelVO fieldTwo = new CellTypeExcelVO(true);
		cells.add(fieldTwo);
		CellTypeExcelVO fieldThree = new CellTypeExcelVO(true, FieldType.INTEGERFIELD);
		cells.add(fieldThree);
		CellTypeExcelVO fieldFour = new CellTypeExcelVO(true);
		cells.add(fieldFour);
		CellTypeExcelVO fieldFive = new CellTypeExcelVO(true, FieldType.INTEGERFIELD);
		cells.add(fieldFive);
		CellTypeExcelVO fieldSix = new CellTypeExcelVO(true, FieldType.INTEGERFIELD);
		cells.add(fieldSix);
		XSSFSheet worksheet = workbook.getSheetAt(0);
		ProgramaAPSMunicipalExcelValidator programaAPSMunicipalExcelValidator = new ProgramaAPSMunicipalExcelValidator(cells.size(), cells, true, 0, 4);
		programaAPSMunicipalExcelValidator.validateFormat(worksheet);		
		List<ProgramaAPSMunicipalVO> items = programaAPSMunicipalExcelValidator.getItems();
		List<ProgramaMunicipalCore> programaMunicipalesCore = recursosFinancierosProgramasReforzamientoDAO.getProgramasCoreByProgramaAno(idProgramaAno);
		if(programaMunicipalesCore != null && programaMunicipalesCore.size() > 0){
			List<Integer> idProgramasCore = new ArrayList<Integer>();
			for(ProgramaMunicipalCore programaMunicipalCore : programaMunicipalesCore){
				idProgramasCore.add(programaMunicipalCore.getIdProgramaMunicipalCore());
			}
			recursosFinancierosProgramasReforzamientoDAO.deleteProgramasMunicipalCore(idProgramasCore);
		}
		for(ProgramaAPSMunicipalVO programaAPSMunicipalVO : items){
			ProgramaMunicipalCore programaMunicipalCore = new ProgramaMunicipalCore();
			Comuna comuna = comunaDAO.getComunaById(programaAPSMunicipalVO.getIdComuna());
			programaMunicipalCore.setComuna(comuna);
			ProgramaAno programaAno = recursosFinancierosProgramasReforzamientoDAO.findById(idProgramaAno);
			programaMunicipalCore.setProgramaAnoMunicipal(programaAno);
			recursosFinancierosProgramasReforzamientoDAO.save(programaMunicipalCore);
			ProgramaMunicipalCoreComponente programaMunicipalCoreComponente = new ProgramaMunicipalCoreComponente();
			programaMunicipalCoreComponente.setCantidad(programaAPSMunicipalVO.getCantidad());
			programaMunicipalCoreComponente.setTarifa(programaAPSMunicipalVO.getTarifa());
			programaMunicipalCoreComponente.setProgramaMunicipalCore(programaMunicipalCore);
			TipoSubtitulo tipoSubtitulo = tipoSubtituloDAO.getTipoSubtituloByName(programaAPSMunicipalVO.getSubtitulo());
			programaMunicipalCoreComponente.setSubtitulo(tipoSubtitulo);
			recursosFinancierosProgramasReforzamientoDAO.save(programaMunicipalCoreComponente);
		}
	}

}
