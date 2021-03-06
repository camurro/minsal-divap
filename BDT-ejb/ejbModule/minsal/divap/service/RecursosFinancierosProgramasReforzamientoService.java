package minsal.divap.service;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.xml.bind.JAXBException;

import minsal.divap.dao.ComponenteDAO;
import minsal.divap.dao.ComunaDAO;
import minsal.divap.dao.DocumentDAO;
import minsal.divap.dao.EstablecimientosDAO;
import minsal.divap.dao.MesDAO;
import minsal.divap.dao.ProgramasDAO;
import minsal.divap.dao.RecursosFinancierosProgramasReforzamientoDAO;
import minsal.divap.dao.SeguimientoDAO;
import minsal.divap.dao.ServicioSaludDAO;
import minsal.divap.dao.TipoSubtituloDAO;
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.doc.GeneradorDocumento;
import minsal.divap.doc.GeneradorResolucionAporteEstatal;
import minsal.divap.doc.GeneradorWord;
import minsal.divap.enums.EstadosProgramas;
import minsal.divap.enums.Subtitulo;
import minsal.divap.enums.TareasSeguimiento;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.enums.TiposDestinatarios;
import minsal.divap.excel.GeneradorExcel;
import minsal.divap.excel.impl.ProgramaAPSDetallesMunicipalesHistoricosSheetExcel;
import minsal.divap.excel.impl.ProgramaAPSDetallesServiciosHistoricosSheetExcel;
import minsal.divap.excel.impl.ProgramaAPSLeySheetExcel;
import minsal.divap.excel.impl.ProgramaAPSMunicipalesDetallesSheetExcel;
import minsal.divap.excel.impl.ProgramaAPSMunicipalesHistoricosSheetExcel;
import minsal.divap.excel.impl.ProgramaAPSMunicipalesSheetExcel;
import minsal.divap.excel.impl.ProgramaAPSMunicipalesTemplateSheetExcel;
import minsal.divap.excel.impl.ProgramaAPSResolucionSheetExcel;
import minsal.divap.excel.impl.ProgramaAPSServicioResumenSheetExcel;
import minsal.divap.excel.impl.ProgramaAPSServicioSheetExcel;
import minsal.divap.excel.impl.ProgramaAPSServiciosDetallesSheetExcel;
import minsal.divap.excel.impl.ProgramaAPSServiciosHistoricosSheetExcel;
import minsal.divap.exception.ExcelFormatException;
import minsal.divap.model.mappers.ProgramaMapper;
import minsal.divap.model.mappers.SeguimientoMapper;
import minsal.divap.util.StringUtil;
import minsal.divap.vo.AdjuntosVO;
import minsal.divap.vo.BodyVO;
import minsal.divap.vo.CellExcelVO;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.DocumentoVO;
import minsal.divap.vo.ProgramaAPSServicioResumenVO;
import minsal.divap.vo.ProgramaAPSServicioVO;
import minsal.divap.vo.ProgramaAPSVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.ReporteEmailsEnviadosVO;
import minsal.divap.vo.ResumenProgramaMixtoVO;
import minsal.divap.vo.SeguimientoVO;
import minsal.divap.vo.SubtituloVO;
import minsal.divap.xml.GeneradorXML;
import minsal.divap.xml.email.Email;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.docx4j.openpackaging.exceptions.Docx4JException;

import cl.minsal.divap.model.AnoEnCurso;
import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.DocumentoProgramasReforzamiento;
import cl.minsal.divap.model.Establecimiento;
import cl.minsal.divap.model.EstadoPrograma;
import cl.minsal.divap.model.Mes;
import cl.minsal.divap.model.Programa;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.ProgramaMunicipalCore;
import cl.minsal.divap.model.ProgramaMunicipalCoreComponente;
import cl.minsal.divap.model.ProgramaMunicipalCoreComponentePK;
import cl.minsal.divap.model.ProgramaServicioCore;
import cl.minsal.divap.model.ProgramaServicioCoreComponente;
import cl.minsal.divap.model.ProgramaServicioCoreComponentePK;
import cl.minsal.divap.model.ProgramasReforzamiento;
import cl.minsal.divap.model.ReferenciaDocumento;
import cl.minsal.divap.model.ReporteEmailsAdjuntos;
import cl.minsal.divap.model.ReporteEmailsDestinatarios;
import cl.minsal.divap.model.ReporteEmailsEnviados;
import cl.minsal.divap.model.ReporteEmailsProgramasReforzamiento;
import cl.minsal.divap.model.Seguimiento;
import cl.minsal.divap.model.ServicioSalud;
import cl.minsal.divap.model.TipoDestinatario;
import cl.minsal.divap.model.TipoDocumento;
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
	private DocumentDAO documentoDAO;
	@EJB
	private EstablecimientosDAO establecimientoDAO;
	@EJB
	private TipoSubtituloDAO tipoSubtituloDAO;
	@EJB
	private ServicioSaludDAO servicioSaludDAO;
	@EJB
	private SeguimientoDAO seguimientoDAO;
	@EJB
	private MesDAO mesDAO;
	@EJB
	private SeguimientoService seguimientoService;
	@EJB
	private RecursosFinancierosProgramasReforzamientoDAO recursosFinancierosProgramasReforzamientoDAO;
	@EJB
	private RecursosFinancierosProgramasReforzamientoService recursosFinancierosService;
	@EJB
	private AlfrescoService alfrescoService;
	@EJB
	private DocumentService documentService;
	@EJB
	private ProgramasService programaService;
	@EJB
	private EmailService emailService;
	@EJB
	private ComunaService comunaService;
	@EJB
	private EstablecimientosService establecimientoService;
	@EJB
	private ComponenteDAO componenteDAO;
	@Resource(name="tmpDir")
	private String tmpDir;
	@Resource(name = "tmpDirDoc")
	private String tmpDirDoc;
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
		case PLANTILLARESOLUCIONCORREO:
			filename = "plantillaResolucionCorreo.xml";
			break;
		case PLANTILLAORDINARIOCORREO:
			filename = "plantillaOrdinarioCorreo.xml";
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



	public Integer getIdPlantillaProgramasPais(Integer idProceso, Integer programaSeleccionado, Integer ano, TipoDocumentosProcesos tipoDocumentoProceso){
		ProgramaVO programa =  programaService.getProgramaByIdProgramaAndAno(programaSeleccionado, ano);
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();

		List<CellExcelVO> header = new ArrayList<CellExcelVO>();
		header.add(new CellExcelVO("Servicios de Salud", 2, 2));

		List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
		String filename = tmpDir + File.separator;
		GeneradorExcel generadorExcel = null;
		String contenType = null;
		switch (tipoDocumentoProceso) {
		case PLANTILLAMUNICIPALAPSRESUMENSERVICIO:
			filename += "Resumen Programa -"+ programa.getNombre().replace(":", "-")+"- Municipal.xlsx";
			filename = StringUtil.removeSpanishAccents(filename);
			generadorExcel = new GeneradorExcel(filename);
			int subtitulos=0;
			int idComponente =0;

			List<Integer> idComponentes = new ArrayList<Integer>();

			for(ComponentesVO componente : programa.getComponentes()){
				for(SubtituloVO subtitulo : componente.getSubtitulos()){
					if(subtitulo.getId()==3){
						subtitulos++;
						idComponentes.add(componente.getId());
						idComponente=componente.getId();
					}
				}
			}
			header.add(new CellExcelVO("Comunas", 2, 2));
			header.add(new CellExcelVO(programa.getNombre(), subtitulos*3, 1));
			subHeader.add(new CellExcelVO("ID", 1, 2));
			subHeader.add(new CellExcelVO("Servicio de Salud", 1, 2));
			subHeader.add(new CellExcelVO("ID", 1, 2));
			subHeader.add(new CellExcelVO("Comuna", 1, 2));

			for(ComponentesVO componente : programa.getComponentes()){
				if(idComponentes.indexOf(componente.getId()) != -1){
					header.add(new CellExcelVO(componente.getNombre(), 3, 1));
					for(SubtituloVO subtitulo : componente.getSubtitulos()){
						if(subtitulo.getId()==3){
							subHeader.add(new CellExcelVO(subtitulo.getNombre(), 3, 1));
							subHeader.add(new CellExcelVO("Tarifa ($)", 1, 1));
							subHeader.add(new CellExcelVO("Cantidad", 1, 1));
							subHeader.add(new CellExcelVO("Total ($)", 1, 1));
						}
					}
				}

			}

			contenType = mimemap.getContentType(filename.toLowerCase());
			List<ProgramaAPSVO> servicioComunas = programaService.getProgramaMunicipalesResumen(programa.getIdProgramaAno(), idComponentes, 3);

			ProgramaAPSMunicipalesSheetExcel programaAPSMunicipalesSheetExcel = new ProgramaAPSMunicipalesSheetExcel(header, subHeader, servicioComunas);
			generadorExcel.addSheet(programaAPSMunicipalesSheetExcel, "Hoja 1");

			break;
		case PLANTILLASERVICIOAPSRESUMENSERVICIO:
			filename += "Resumen Programa -"+ programa.getNombre().replace(":", "-")+"- Servicio.xlsx";
			filename = StringUtil.removeSpanishAccents(filename);
			generadorExcel = new GeneradorExcel(filename);
			header.add(new CellExcelVO("Establecimientos", 2, 2));
			int subtitulosServicio=0;

			List<Integer> idComponentesServicio = new ArrayList<Integer>();
			HashMap<Integer, Integer> componenteSubs = new HashMap<Integer, Integer>();
			for(ComponentesVO componente : programa.getComponentes()){
				int sub=0;


				for(SubtituloVO subtitulo : componente.getSubtitulos()){
					if(subtitulo.getId()!=3){
						sub++;
						if(componenteSubs.containsKey(componente.getId())){
							componenteSubs.remove(componente.getId());
							componenteSubs.put(componente.getId(), sub);
						}else{
							componenteSubs.put(componente.getId(), sub);
						}
						subtitulosServicio++;
						idComponentesServicio.add(componente.getId());
					}
				}
			}

			//nombre programa
			header.add(new CellExcelVO(programa.getNombre(), (3 * subtitulosServicio), 1));
			subHeader.add(new CellExcelVO("ID", 1, 2));
			subHeader.add(new CellExcelVO("Servicio de Salud", 1, 2));
			subHeader.add(new CellExcelVO("ID", 1, 2));
			subHeader.add(new CellExcelVO("Establecimiento", 1, 2));

			for(ComponentesVO componente : programa.getComponentes()){
				if(idComponentesServicio.indexOf(componente.getId()) != -1){
					header.add(new CellExcelVO(componente.getNombre(),componenteSubs.get(componente.getId())*3, 1));

					for(SubtituloVO subtitulo : componente.getSubtitulos()){
						if(subtitulo.getId()!=3){
							subHeader.add(new CellExcelVO(subtitulo.getNombre(), 3, 1));
							subHeader.add(new CellExcelVO("Tarifa", 1, 1));
							subHeader.add(new CellExcelVO("Cantidad", 1, 1));
							subHeader.add(new CellExcelVO("Total", 1, 1));
						}
					}
				}
			}
			contenType = mimemap.getContentType(filename.toLowerCase());


			List<ProgramaAPSServicioResumenVO> servicioComunaEstablecimientos = programaService.getProgramaServiciosResumen(programa.getIdProgramaAno(), idComponentesServicio);
			ProgramaAPSServicioResumenSheetExcel programaAPSServicioSheetExcel = new ProgramaAPSServicioResumenSheetExcel(header, subHeader, servicioComunaEstablecimientos);
			generadorExcel.addSheet(programaAPSServicioSheetExcel, "Hoja 1");
			break;
		case PLANTILLAMUNICIPALAPSRESUMENHISTORICO:
			filename += "Resumen Programa -" + programa.getNombre().replace(":", "-")+"- Municipal.xlsx";
			filename = StringUtil.removeSpanishAccents(filename);
			generadorExcel = new GeneradorExcel(filename);
			int subtitulosH=0;
			int idComponenteH =0;

			List<Integer> idComponentesH = new ArrayList<Integer>();
			for(ComponentesVO componente : programa.getComponentes()){
				for(SubtituloVO subtitulo : componente.getSubtitulos()){
					if(subtitulo.getId()==3){
						subtitulosH++;
						idComponenteH=componente.getId();
						idComponentesH.add(componente.getId());
					}
				}
			}
			header.add(new CellExcelVO("Comunas", 2, 2));
			header.add(new CellExcelVO(programa.getNombre(), subtitulosH, 1));
			subHeader.add(new CellExcelVO("ID", 1, 2));
			subHeader.add(new CellExcelVO("Servicio de Salud", 1, 2));
			subHeader.add(new CellExcelVO("ID", 1, 2));
			subHeader.add(new CellExcelVO("Comuna", 1, 2));

			for(ComponentesVO componente : programa.getComponentes()){
				header.add(new CellExcelVO(componente.getNombre(), subtitulosH, 1));
				for(SubtituloVO subtitulo : componente.getSubtitulos()){
					if(subtitulo.getId()==3){
						subHeader.add(new CellExcelVO(subtitulo.getNombre(), 1, 1));
						subHeader.add(new CellExcelVO("Total", 1, 1));
					}
				}
			}

			contenType = mimemap.getContentType(filename.toLowerCase());
			List<ProgramaAPSVO> servicioComunasH = programaService.getProgramaMunicipalesResumen(programa.getIdProgramaAno(), idComponentesH, 3);


			ProgramaAPSMunicipalesHistoricosSheetExcel programaAPSMunicipalesHistoricosSheetExcel = new ProgramaAPSMunicipalesHistoricosSheetExcel(header, subHeader, servicioComunasH);
			generadorExcel.addSheet(programaAPSMunicipalesHistoricosSheetExcel, "Hoja 1");

			break;
		case PLANTILLASERVICIOAPSRESUMENHISTORICO:
			filename += "Resumen Programa -" + programa.getNombre().replace(":", "-")+"- Servicio.xlsx";
			filename = StringUtil.removeSpanishAccents(filename);
			generadorExcel = new GeneradorExcel(filename);
			header.add(new CellExcelVO("Establecimientos", 2, 2));
			int subtitulosServicioH=0;
			for(ComponentesVO componente : programa.getComponentes()){
				for(SubtituloVO subtitulo : componente.getSubtitulos()){
					if(subtitulo.getId()!=3){
						subtitulosServicioH++;
					}
				}
				//nombre programa
				header.add(new CellExcelVO(programa.getNombre(), subtitulosServicioH, 1));
				subHeader.add(new CellExcelVO("ID", 1, 2));
				subHeader.add(new CellExcelVO("Servicio de Salud", 1, 2));
				subHeader.add(new CellExcelVO("ID", 1, 2));
				subHeader.add(new CellExcelVO("Establecimiento", 1, 2));

				header.add(new CellExcelVO(componente.getNombre(),subtitulosServicioH, 1));
				for(SubtituloVO subtitulo : componente.getSubtitulos()){
					if(subtitulo.getId()!=3){
						subHeader.add(new CellExcelVO(subtitulo.getNombre(), 1, 1));
						subHeader.add(new CellExcelVO("Total", 1, 1));
					}
				}
			}
			contenType = mimemap.getContentType(filename.toLowerCase());

			List<ProgramaAPSServicioResumenVO> servicioComunaEstablecimientosH = programaService.getProgramaServicios(programa.getIdProgramaAno());
			ProgramaAPSServiciosHistoricosSheetExcel programaAPSServiciosHistoricosSheetExcel = new ProgramaAPSServiciosHistoricosSheetExcel(header, subHeader, servicioComunaEstablecimientosH);
			generadorExcel.addSheet(programaAPSServiciosHistoricosSheetExcel, "Hoja 1");
			break;
		default:
			break;
		}


		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderRecursosFinancierosAPS.replace("{ANO}", ano.toString()));
			ProgramasReforzamiento programasReforzamiento = recursosFinancierosProgramasReforzamientoDAO.findByIdInstancia(idProceso);
			return documentService.createDocumentProgramasReforzamiento(programasReforzamiento, tipoDocumentoProceso, response.getNodeRef(), response.getFileName(), contenType,
							programa.getIdProgramaAno(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Integer getIdPlantillaProgramasPaisModificado(Integer idProceso, Integer programaSeleccionado, TipoDocumentosProcesos tipoDocumentoProceso, Integer ano){
		ProgramaVO programaEvaluacion = programaService.getProgramaByIdProgramaAndAno(programaSeleccionado, ano);
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();

		List<CellExcelVO> header = new ArrayList<CellExcelVO>();
		header.add(new CellExcelVO("Servicios de Salud", 2, 2));

		List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
		String filename = tmpDir + File.separator;
		GeneradorExcel generadorExcel = null;
		String contenType = null;
		switch (tipoDocumentoProceso) {
		case PLANTILLAMUNICIPALAPSRESUMENSERVICIO:
			filename += "Resumen Programa -"+programaEvaluacion.getNombre().replace(":", "-")+"- Municipal.xlsx";
			filename = StringUtil.removeSpanishAccents(filename);
			generadorExcel = new GeneradorExcel(filename);
			int subtitulos=0;
			int idComponente =0;

			List<Integer> idComponentes = new ArrayList<Integer>();

			for(ComponentesVO componente : programaEvaluacion.getComponentes()){
				for(SubtituloVO subtitulo : componente.getSubtitulos()){
					if(subtitulo.getId()==3){
						subtitulos++;
						idComponentes.add(componente.getId());
						idComponente=componente.getId();
					}
				}
			}
			header.add(new CellExcelVO("Comunas", 2, 2));
			header.add(new CellExcelVO(programaEvaluacion.getNombre(), subtitulos*3, 1));
			subHeader.add(new CellExcelVO("ID", 1, 2));
			subHeader.add(new CellExcelVO("Servicio de Salud", 1, 2));
			subHeader.add(new CellExcelVO("ID", 1, 2));
			subHeader.add(new CellExcelVO("Comuna", 1, 2));

			for(ComponentesVO componente : programaEvaluacion.getComponentes()){
				if(idComponentes.indexOf(componente.getId())!= -1){
					header.add(new CellExcelVO(componente.getNombre(), 3, 1));
					for(SubtituloVO subtitulo : componente.getSubtitulos()){
						if(subtitulo.getId()==3){
							subHeader.add(new CellExcelVO(subtitulo.getNombre(), 3, 1));
							subHeader.add(new CellExcelVO("Tarifa ($)", 1, 1));
							subHeader.add(new CellExcelVO("Cantidad", 1, 1));
							subHeader.add(new CellExcelVO("Total ($)", 1, 1));
						}
					}
				}

			}


			contenType = mimemap.getContentType(filename.toLowerCase());
			List<ProgramaAPSVO> servicioComunas = programaService.getProgramaMunicipalesResumen(programaEvaluacion.getIdProgramaAno(), idComponentes, 3);

			ProgramaAPSMunicipalesSheetExcel programaAPSMunicipalesSheetExcel = new ProgramaAPSMunicipalesSheetExcel(header, subHeader, servicioComunas);
			generadorExcel.addSheet(programaAPSMunicipalesSheetExcel, "Hoja 1");

			break;
		case PLANTILLASERVICIOAPSRESUMENSERVICIO:
			filename += "Resumen Programa -"+programaEvaluacion.getNombre().replace(":", "-")+"- Servicio.xlsx";
			filename = StringUtil.removeSpanishAccents(filename);
			generadorExcel = new GeneradorExcel(filename);
			header.add(new CellExcelVO("Establecimientos", 2, 2));
			int subtitulosServicio=0;

			List<Integer> idComponentesServicio = new ArrayList<Integer>();
			HashMap<Integer, Integer> componenteSubs = new HashMap<Integer, Integer>();
			for(ComponentesVO componente : programaEvaluacion.getComponentes()){
				int sub=0;


				for(SubtituloVO subtitulo : componente.getSubtitulos()){
					if(subtitulo.getId()!=3){
						sub++;
						if(componenteSubs.containsKey(componente.getId())){
							componenteSubs.remove(componente.getId());
							componenteSubs.put(componente.getId(), sub);
						}else{
							componenteSubs.put(componente.getId(), sub);
						}
						subtitulosServicio++;
						idComponentesServicio.add(componente.getId());
					}
				}
			}

			//nombre programa
			header.add(new CellExcelVO(programaEvaluacion.getNombre(), (3 * subtitulosServicio), 1));
			subHeader.add(new CellExcelVO("ID", 1, 2));
			subHeader.add(new CellExcelVO("Servicio de Salud", 1, 2));
			subHeader.add(new CellExcelVO("ID", 1, 2));
			subHeader.add(new CellExcelVO("Establecimiento", 1, 2));

			for(ComponentesVO componente : programaEvaluacion.getComponentes()){
				if(idComponentesServicio.indexOf(componente.getId()) != -1){
					header.add(new CellExcelVO(componente.getNombre(),componenteSubs.get(componente.getId())*3, 1));

					for(SubtituloVO subtitulo : componente.getSubtitulos()){
						if(subtitulo.getId()!=3){
							subHeader.add(new CellExcelVO(subtitulo.getNombre(), 3, 1));
							subHeader.add(new CellExcelVO("Tarifa", 1, 1));
							subHeader.add(new CellExcelVO("Cantidad", 1, 1));
							subHeader.add(new CellExcelVO("Total", 1, 1));
						}
					}
				}
			}
			contenType = mimemap.getContentType(filename.toLowerCase());


			List<ProgramaAPSServicioResumenVO> servicioComunaEstablecimientos = programaService.getProgramaServiciosResumen(programaEvaluacion.getIdProgramaAno(), idComponentesServicio);
			ProgramaAPSServicioResumenSheetExcel programaAPSServicioSheetExcel = new ProgramaAPSServicioResumenSheetExcel(header, subHeader, servicioComunaEstablecimientos);
			generadorExcel.addSheet(programaAPSServicioSheetExcel, "Hoja 1");
			break;
		case PLANTILLAMUNICIPALAPSRESUMENHISTORICO:
			filename += "Resumen Programa -"+programaEvaluacion.getNombre().replace(":", "-")+"- Municipal.xlsx";
			filename = StringUtil.removeSpanishAccents(filename);
			generadorExcel = new GeneradorExcel(filename);
			int subtitulosH=0;
			int idComponenteH =0;

			List<Integer> idComponentesH = new ArrayList<Integer>();
			for(ComponentesVO componente : programaEvaluacion.getComponentes()){
				for(SubtituloVO subtitulo : componente.getSubtitulos()){
					if(subtitulo.getId()==3){
						subtitulosH++;
						idComponenteH=componente.getId();
						idComponentesH.add(componente.getId());
					}
				}
			}
			header.add(new CellExcelVO("Comunas", 2, 2));
			header.add(new CellExcelVO(programaEvaluacion.getNombre(), subtitulosH, 1));
			subHeader.add(new CellExcelVO("ID", 1, 2));
			subHeader.add(new CellExcelVO("Servicio de Salud", 1, 2));
			subHeader.add(new CellExcelVO("ID", 1, 2));
			subHeader.add(new CellExcelVO("Comuna", 1, 2));

			for(ComponentesVO componente : programaEvaluacion.getComponentes()){
				header.add(new CellExcelVO(componente.getNombre(), subtitulosH, 1));
				for(SubtituloVO subtitulo : componente.getSubtitulos()){
					if(subtitulo.getId()==3){
						subHeader.add(new CellExcelVO(subtitulo.getNombre(), 1, 1));
						subHeader.add(new CellExcelVO("Total", 1, 1));
					}
				}
			}

			contenType = mimemap.getContentType(filename.toLowerCase());
			List<ProgramaAPSVO> servicioComunasH = programaService.getProgramaMunicipalesResumen(programaEvaluacion.getIdProgramaAno(), idComponentesH, 3);
			ProgramaAPSMunicipalesHistoricosSheetExcel programaAPSMunicipalesHistoricosSheetExcel = new ProgramaAPSMunicipalesHistoricosSheetExcel(header, subHeader, servicioComunasH);
			generadorExcel.addSheet(programaAPSMunicipalesHistoricosSheetExcel, "Hoja 1");

			break;
		case PLANTILLASERVICIOAPSRESUMENHISTORICO:
			filename += "Resumen Programa -"+programaEvaluacion.getNombre().replace(":", "-")+"- Servicio.xlsx";
			filename = StringUtil.removeSpanishAccents(filename);
			generadorExcel = new GeneradorExcel(filename);
			header.add(new CellExcelVO("Establecimientos", 2, 2));
			int subtitulosServicioH=0;
			for(ComponentesVO componente : programaEvaluacion.getComponentes()){
				for(SubtituloVO subtitulo : componente.getSubtitulos()){
					if(subtitulo.getId()!=3){
						subtitulosServicioH++;
					}
				}
				//nombre programa
				header.add(new CellExcelVO(programaEvaluacion.getNombre(), subtitulosServicioH, 1));
				subHeader.add(new CellExcelVO("ID", 1, 2));
				subHeader.add(new CellExcelVO("Servicio de Salud", 1, 2));
				subHeader.add(new CellExcelVO("ID", 1, 2));
				subHeader.add(new CellExcelVO("Establecimiento", 1, 2));

				header.add(new CellExcelVO(componente.getNombre(),subtitulosServicioH, 1));
				for(SubtituloVO subtitulo : componente.getSubtitulos()){
					if(subtitulo.getId()!=3){
						subHeader.add(new CellExcelVO(subtitulo.getNombre(), 1, 1));
						subHeader.add(new CellExcelVO("Total", 1, 1));
					}
				}
			}
			contenType = mimemap.getContentType(filename.toLowerCase());

			List<ProgramaAPSServicioResumenVO> servicioComunaEstablecimientosH = programaService.getProgramaServicios(programaEvaluacion.getIdProgramaAno());
			ProgramaAPSServiciosHistoricosSheetExcel programaAPSServiciosHistoricosSheetExcel = new ProgramaAPSServiciosHistoricosSheetExcel(header, subHeader, servicioComunaEstablecimientosH);
			generadorExcel.addSheet(programaAPSServiciosHistoricosSheetExcel, "Hoja 1");
			break;
		default:
			break;
		}

		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderRecursosFinancierosAPS.replace("{ANO}", ano.toString()));
			ProgramasReforzamiento programasReforzamiento = recursosFinancierosProgramasReforzamientoDAO.findByIdInstancia(idProceso);
			return documentService.createDocumentProgramasReforzamiento(programasReforzamiento, tipoDocumentoProceso, response.getNodeRef(), response.getFileName(), contenType,
					programaEvaluacion.getIdProgramaAno(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Integer getIdPlantillaProgramas(Integer programaSeleccionado, TipoDocumentosProcesos tipoDocumentoProceso, boolean template, Integer ano){
		System.out.println("programaSeleccionado-->"+programaSeleccionado);
		System.out.println("ano-->"+ano);
		Integer plantillaId = documentService.getPlantillaByTypeAndProgram(tipoDocumentoProceso, programaSeleccionado);
		ProgramaVO programa = programaService.getProgramaByIdProgramaAndAno(programaSeleccionado, (ano - 1));
		ProgramaVO programaProxAno = programaService.getProgramaByIdProgramaAndAno(programaSeleccionado, ano);
		if(plantillaId == null){
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			List<CellExcelVO> header = new ArrayList<CellExcelVO>();
			List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
			String filename = tmpDir + File.separator;
			GeneradorExcel generadorExcel = null;
			String contenType = null;
			switch (tipoDocumentoProceso) {
			case PLANTILLAPROGRAMAAPSMUNICIPALES:
				header.add(new CellExcelVO("Servicios de Salud", 2, 2));
				header.add(new CellExcelVO("Comunas", 2, 2));
				String filenameTmp = "Plantilla Programa -" + programa.getNombre();
				filenameTmp = filenameTmp.replace(":", "").replace(".", "").replace("/", "");;
				filenameTmp += "- Municipal.xlsx";
				filenameTmp = StringUtil.removeSpanishAccents(filenameTmp);
				filename += filenameTmp;
				System.out.println("filename->"+filename);
				generadorExcel = new GeneradorExcel(filename);
				int subtitulos=0;
				List<Integer> idComponentes = new ArrayList<Integer>();
				List<ComponentesVO> componentes = programaService.getComponentesByProgramaAnoSubtitulos(programa.getIdProgramaAno(), Subtitulo.SUBTITULO24);
				for(ComponentesVO componente : componentes){
					for(SubtituloVO subtitulo : componente.getSubtitulos()){
						if(Subtitulo.SUBTITULO24.getId().equals(subtitulo.getId())){
							subtitulos++;
							idComponentes.add(componente.getId());
						}
					}
				}


				header.add(new CellExcelVO(programa.getNombre(), subtitulos*2, 1));
				subHeader.add(new CellExcelVO("ID", 1, 2));
				subHeader.add(new CellExcelVO("Servicio de Salud", 1, 2));
				subHeader.add(new CellExcelVO("ID", 1, 2));
				subHeader.add(new CellExcelVO("Comuna", 1, 2));
				for(ComponentesVO componente : componentes){
					if(idComponentes.indexOf(componente.getId()) != -1){
						header.add(new CellExcelVO(componente.getNombre(), 2, 1));
						for(SubtituloVO subtitulo : componente.getSubtitulos()){
							if(Subtitulo.SUBTITULO24.getId().equals(subtitulo.getId())){
								subHeader.add(new CellExcelVO(subtitulo.getNombre(), 2, 1));
								subHeader.add(new CellExcelVO("Tarifa", 1, 1));
								subHeader.add(new CellExcelVO("Cantidad", 1, 1));
							}
						}
					}
				}

				contenType = mimemap.getContentType(filename.toLowerCase());

				if(template){
					List<ProgramaAPSVO> servicioComunas = comunaService.getServiciosComunas();
					ProgramaAPSMunicipalesTemplateSheetExcel programaAPSMunicipalesTemplateSheetExcel = new ProgramaAPSMunicipalesTemplateSheetExcel(header, subHeader, servicioComunas);
					generadorExcel.addSheet(programaAPSMunicipalesTemplateSheetExcel, "Hoja 1");
				}else{
					List<ProgramaAPSVO> servicioComunas = programaService.getProgramaMunicipalesResumen(programaProxAno.getIdProgramaAno(), idComponentes, 3);
					ProgramaAPSMunicipalesSheetExcel programaAPSMunicipalesSheetExcel = new ProgramaAPSMunicipalesSheetExcel(header, subHeader, servicioComunas);
					generadorExcel.addSheet(programaAPSMunicipalesSheetExcel, "Hoja 1");
				}
				break;
			case PLANTILLAPROGRAMAAPSSERVICIO:
				header.add(new CellExcelVO("Servicios de Salud", 2, 2));
				String filenameServTmp = "Plantilla Programa -"+programa.getNombre();
				filenameServTmp = filenameServTmp.replace(":", "").replace(".", "").replace("/", "");
				filenameServTmp +=  "- Servicio.xlsx";
				filenameServTmp = StringUtil.removeSpanishAccents(filenameServTmp);
				filename += filenameServTmp;
				System.out.println("filename->"+filename);
				generadorExcel = new GeneradorExcel(filename);
				header.add(new CellExcelVO("Establecimientos", 2, 2));
				int subtitulosServicio=0;
				HashMap<Integer, Integer> compoSubs = new HashMap<Integer, Integer>();


				Subtitulo[] subtitulosServicios = {Subtitulo.SUBTITULO21, Subtitulo.SUBTITULO22, Subtitulo.SUBTITULO29};
				List<ComponentesVO> componentesServicios = programaService.getComponentesByProgramaAnoSubtitulos(programa.getIdProgramaAno(), subtitulosServicios);
				for(ComponentesVO componente : componentesServicios){
					Integer cantidadSubServ=0;
					for(SubtituloVO subtitulo : componente.getSubtitulos()){
						System.out.println("subtitulo: "+ subtitulo.getNombre());
						if(!Subtitulo.SUBTITULO24.getId().equals(subtitulo.getId())){	
							subtitulosServicio++;
							cantidadSubServ++;
						}
					}
					compoSubs.put(componente.getId(), cantidadSubServ);
				}
				header.add(new CellExcelVO(programa.getNombre(), (2 * subtitulosServicio), 1));
				subHeader.add(new CellExcelVO("ID", 1, 2));
				subHeader.add(new CellExcelVO("Servicio de Salud", 1, 2));
				//subHeader.add(new CellExcelVO("ID", 1, 2));
				//subHeader.add(new CellExcelVO("Comuna", 1, 2));
				subHeader.add(new CellExcelVO("ID", 1, 2));
				subHeader.add(new CellExcelVO("Establecimiento", 1, 2));
				for(ComponentesVO componente : componentesServicios){
					if(compoSubs.get(componente.getId())>0){
						Integer cantidadSubs = compoSubs.get(componente.getId());
						header.add(new CellExcelVO(componente.getNombre(),compoSubs.get(componente.getId())*2, 1));
						for(SubtituloVO subtitulo : componente.getSubtitulos()){
							if(!Subtitulo.SUBTITULO24.getId().equals(subtitulo.getId())){	
								subHeader.add(new CellExcelVO(subtitulo.getNombre(), 2, 1));
								subHeader.add(new CellExcelVO("Tarifa", 1, 1));
								subHeader.add(new CellExcelVO("Cantidad", 1, 1));
							}
						}
					}
				}
				contenType = mimemap.getContentType(filename.toLowerCase());
				List<ProgramaAPSServicioVO> servicioComunaEstablecimientos = establecimientoService.getServiciosComunasEstablecimientos();
				ProgramaAPSServicioSheetExcel programaAPSServicioSheetExcel = new ProgramaAPSServicioSheetExcel(header, subHeader, servicioComunaEstablecimientos);
				generadorExcel.addSheet(programaAPSServicioSheetExcel, "Hoja 1");
				break;
			case PLANTILLALEYAPS:
				header.add(new CellExcelVO("Servicios de Salud", 2, 1));
				header.add(new CellExcelVO("Comunas", 2, 1));
				String filenameLeyTmp = "Plantilla Ley -"+programa.getNombre();
				filenameLeyTmp = filenameLeyTmp.replace(":", "").replace(".", "").replace("/", "");
				filenameLeyTmp += ".xlsx";
				filenameLeyTmp = StringUtil.removeSpanishAccents(filenameLeyTmp);
				filename += filenameLeyTmp;
				System.out.println("filename->"+filename);
				generadorExcel = new GeneradorExcel(filename);
				header.add(new CellExcelVO(programa.getNombre(), 1, 1));
				subHeader.add(new CellExcelVO("ID", 1, 1));
				subHeader.add(new CellExcelVO("Servicio de Salud", 1, 1));
				subHeader.add(new CellExcelVO("ID", 1, 1));
				subHeader.add(new CellExcelVO("Comuna", 1, 1));
				subHeader.add(new CellExcelVO("Monto", 1, 1));

				contenType = mimemap.getContentType(filename.toLowerCase());
				List<ProgramaAPSVO> servicioComunasLey = comunaService.getServiciosComunas();
				ProgramaAPSLeySheetExcel programaAPSMunicipalesSheetExcelLey = new ProgramaAPSLeySheetExcel(header, subHeader, servicioComunasLey);
				generadorExcel.addSheet(programaAPSMunicipalesSheetExcelLey, "Hoja 1");
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

	public Integer getIdPlantillaModificacionProgramas(Integer programaSeleccionado, TipoDocumentosProcesos tipoDocumentoProceso, boolean template, Integer ano){
		Integer plantillaId = documentService.getPlantillaByTypeAndProgram(tipoDocumentoProceso, programaSeleccionado);
		Programa prog =  programaService.getProgramasByID(programaSeleccionado);
		Integer idProgAnoActual = programaService.getIdProgramaAnoAnterior(prog.getId(), ano);
		ProgramaVO programa;
		if(plantillaId == null){
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			programa = new ProgramaMapper().getBasic(programasDAO.getProgramaAnoByID(programaSeleccionado));

			List<CellExcelVO> header = new ArrayList<CellExcelVO>();


			List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
			String filename = tmpDir + File.separator;
			GeneradorExcel generadorExcel = null;
			String contenType = null;
			switch (tipoDocumentoProceso) {
			case PLANTILLAPROGRAMAAPSMUNICIPALES:
				header.add(new CellExcelVO("Servicios de Salud", 2, 2));
				header.add(new CellExcelVO("Comunas", 2, 2));
				filename += "Plantilla Programa -"+prog.getNombre()+"- Municipal.xlsx";
				filename = StringUtil.removeSpanishAccents(filename);
				generadorExcel = new GeneradorExcel(filename);
				int subtitulos=0;
				List<Integer> idComponentes = new ArrayList<Integer>();

				for(ComponentesVO componente : programa.getComponentes()){
					for(SubtituloVO subtitulo : componente.getSubtitulos()){
						if(subtitulo.getId()==3){
							subtitulos++;
							idComponentes.add(componente.getId());
						}
					}
				}


				header.add(new CellExcelVO(programa.getNombre(), subtitulos*2, 1));
				subHeader.add(new CellExcelVO("ID", 1, 2));
				subHeader.add(new CellExcelVO("Servicio de Salud", 1, 2));
				subHeader.add(new CellExcelVO("ID", 1, 2));
				subHeader.add(new CellExcelVO("Comuna", 1, 2));
				for(ComponentesVO componente : programa.getComponentes()){
					if(idComponentes.indexOf(componente.getId()) != -1){
						header.add(new CellExcelVO(componente.getNombre(), 2, 1));
						for(SubtituloVO subtitulo : componente.getSubtitulos()){
							if(subtitulo.getId()==3){
								subHeader.add(new CellExcelVO(subtitulo.getNombre(), 2, 1));
								subHeader.add(new CellExcelVO("Tarifa", 1, 1));
								subHeader.add(new CellExcelVO("Cantidad", 1, 1));
							}

						}
					}

				}

				contenType = mimemap.getContentType(filename.toLowerCase());

				if(template){
					List<ProgramaAPSVO> servicioComunas = comunaService.getServiciosComunas();
					ProgramaAPSMunicipalesTemplateSheetExcel programaAPSMunicipalesTemplateSheetExcel = new ProgramaAPSMunicipalesTemplateSheetExcel(header, subHeader, servicioComunas);
					generadorExcel.addSheet(programaAPSMunicipalesTemplateSheetExcel, "Hoja 1");
				}else{
					List<ProgramaAPSVO> servicioComunas = programaService.getProgramaMunicipalesResumen(idProgAnoActual, idComponentes, 3);
					ProgramaAPSMunicipalesSheetExcel programaAPSMunicipalesSheetExcel = new ProgramaAPSMunicipalesSheetExcel(header, subHeader, servicioComunas);
					generadorExcel.addSheet(programaAPSMunicipalesSheetExcel, "Hoja 1");
				}


				break;
			case PLANTILLAPROGRAMAAPSSERVICIO:
				header.add(new CellExcelVO("Servicios de Salud", 2, 2));
				filename += "Plantilla Programa -"+prog.getNombre()+"- Servicio.xlsx";
				filename = StringUtil.removeSpanishAccents(filename);
				generadorExcel = new GeneradorExcel(filename);
				header.add(new CellExcelVO("Establecimientos", 2, 2));
				int subtitulosServicio=0;
				HashMap<Integer, Integer> compoSubs = new HashMap<Integer, Integer>();



				for(ComponentesVO componente : programa.getComponentes()){
					Integer cantidadSubServ=0;
					for(SubtituloVO subtitulo : componente.getSubtitulos()){
						System.out.println("subtitulo: "+ subtitulo.getNombre());
						if(subtitulo.getId()!=3){
							subtitulosServicio++;
							cantidadSubServ++;
						}
					}
					compoSubs.put(componente.getId(), cantidadSubServ);
				}
				header.add(new CellExcelVO(programa.getNombre(), (2 * subtitulosServicio), 1));
				subHeader.add(new CellExcelVO("ID", 1, 2));
				subHeader.add(new CellExcelVO("Servicio de Salud", 1, 2));
				//subHeader.add(new CellExcelVO("ID", 1, 2));
				//subHeader.add(new CellExcelVO("Comuna", 1, 2));
				subHeader.add(new CellExcelVO("ID", 1, 2));
				subHeader.add(new CellExcelVO("Establecimiento", 1, 2));
				for(ComponentesVO componente : programa.getComponentes()){
					if(compoSubs.get(componente.getId())>0){
						Integer cantidadSubs = compoSubs.get(componente.getId());
						header.add(new CellExcelVO(componente.getNombre(),compoSubs.get(componente.getId())*2, 1));
						for(SubtituloVO subtitulo : componente.getSubtitulos()){
							if(subtitulo.getId()!=3){
								subHeader.add(new CellExcelVO(subtitulo.getNombre(), 2, 1));
								subHeader.add(new CellExcelVO("Tarifa", 1, 1));
								subHeader.add(new CellExcelVO("Cantidad", 1, 1));
							}
						}
					}

				}
				contenType = mimemap.getContentType(filename.toLowerCase());
				List<ProgramaAPSServicioVO> servicioComunaEstablecimientos = establecimientoService.getServiciosComunasEstablecimientos();
				ProgramaAPSServicioSheetExcel programaAPSServicioSheetExcel = new ProgramaAPSServicioSheetExcel(header, subHeader, servicioComunaEstablecimientos);
				generadorExcel.addSheet(programaAPSServicioSheetExcel, "Hoja 1");
				break;
			case PLANTILLALEYAPS:
				header.add(new CellExcelVO("Servicios de Salud", 2, 1));
				header.add(new CellExcelVO("Comunas", 2, 1));
				filename += "Plantilla Ley -"+prog.getNombre()+".xlsx";
				filename = StringUtil.removeSpanishAccents(filename);
				generadorExcel = new GeneradorExcel(filename);
				header.add(new CellExcelVO(prog.getNombre(), 1, 1));
				subHeader.add(new CellExcelVO("ID", 1, 1));
				subHeader.add(new CellExcelVO("Servicio de Salud", 1, 1));
				subHeader.add(new CellExcelVO("ID", 1, 1));
				subHeader.add(new CellExcelVO("Comuna", 1, 1));
				subHeader.add(new CellExcelVO("Monto", 1, 1));

				contenType = mimemap.getContentType(filename.toLowerCase());
				List<ProgramaAPSVO> servicioComunasLey = comunaService.getServiciosComunas();
				ProgramaAPSLeySheetExcel programaAPSMunicipalesSheetExcelLey = new ProgramaAPSLeySheetExcel(header, subHeader, servicioComunasLey);
				generadorExcel.addSheet(programaAPSMunicipalesSheetExcelLey, "Hoja 1");
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

	public void cambiarEstadoPrograma(Integer idPrograma, Integer ano, EstadosProgramas estadoPrograma) {
		System.out.println("idPrograma-->" + idPrograma + " estadoPrograma.getId()-->"+estadoPrograma.getId());
		ProgramaAno programaAno = programasDAO.getProgramaAnoByIdProgramaAndAno(idPrograma, ano);
		programaAno.setEstado(new EstadoPrograma(estadoPrograma.getId()));
		System.out.println("Cambia estado ok");
	}

	public void modificacionCambiarEstadoPrograma(Integer idPrograma, EstadosProgramas estadoPrograma) {
		System.out.println("idPrograma-->" + idPrograma + " estadoPrograma.getId()-->"+estadoPrograma.getId());
		ProgramaAno programaAno = recursosFinancierosProgramasReforzamientoDAO.findById(idPrograma);

		programaAno.setEstadoModificacionAPS(new EstadoPrograma(estadoPrograma.getId()));
		System.out.println("Cambia estado ok");
	}

	public List<ProgramaVO> getProgramas(String username, Integer ano) {
		if(ano == null){
			ano = getAnoCurso();
		}
		List<ProgramaVO> programasVO = new ArrayList<ProgramaVO>();
		List<ProgramaAno> programas = programasDAO.getProgramasByUserAno(username, ano);
		if(programas != null && programas.size() > 0){
			for(ProgramaAno programaAno : programas){
				if(programaAno.getPrograma().getId().intValue() > 0){
					programasVO.add(new ProgramaMapper().getBasic(programaAno));
				}
			}
		}
		return programasVO;
	}

	public List<ProgramaVO> getProgramasSinPerCapita(String username, Integer ano) {
		if(ano == null){
			ano = getAnoCurso();
		}
		List<ProgramaVO> programasVO = new ArrayList<ProgramaVO>();
		List<ProgramaAno> programas = programasDAO.getProgramasByUserAno(username, ano);
		if(programas != null && programas.size() > 0){
			for(ProgramaAno programa : programas){
				if(programa.getIdProgramaAno()>0){
					programasVO.add(new ProgramaMapper().getBasic(programa));
				}

			}
		}
		return programasVO;
	}

	public List<ProgramaVO> getProgramasSinPerCapitaSinHistoricos(String username, Integer ano) {
		List<ProgramaVO> programasVO = new ArrayList<ProgramaVO>();
		List<ProgramaVO> programasSalidaVO = new ArrayList<ProgramaVO>();
		List<ProgramaAno> programas = programasDAO.getProgramasByUserAno(username, ano);
		if(programas != null && programas.size() > 0){
			for(ProgramaAno programa : programas){
				if(programa.getIdProgramaAno()>0){
					programasVO.add(new ProgramaMapper().getBasic(programa));
				}

			}

			for(ProgramaVO programaVO : programasVO){
				for(ComponentesVO componenteVO : programaVO.getComponentes()){
					if(componenteVO.getTipoComponente().getId()==1){
						if(programasSalidaVO.indexOf(programaVO)==-1){
							programasSalidaVO.add(programaVO);
						}

					}

				}
			}
		}
		return programasSalidaVO;
	}


	public ProgramaVO getProgramaById(Integer idPrograma) {
		ProgramaAno programaAno = recursosFinancierosProgramasReforzamientoDAO.findById(idPrograma);
		return new ProgramaMapper().getBasic(programaAno);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) 
	public void procesarPlanillaMunicipal(boolean ley, Integer idProgramaAno, XSSFWorkbook workbook, List<ComponentesVO> componentes,int filasCabecera) throws ExcelFormatException {
		List<ProgramaMunicipalCore> programaMunicipalesCore = recursosFinancierosProgramasReforzamientoDAO.getProgramasCoreByProgramaAno(idProgramaAno);
		if(programaMunicipalesCore != null && programaMunicipalesCore.size() > 0){
			List<Integer> idProgramasCore = new ArrayList<Integer>();
			for(ProgramaMunicipalCore programaMunicipalCore : programaMunicipalesCore){
				idProgramasCore.add(programaMunicipalCore.getIdProgramaMunicipalCore());
			}
			recursosFinancierosProgramasReforzamientoDAO.deleteProgramasMunicipalCore(idProgramasCore);
		}
		XSSFSheet worksheet = workbook.getSheetAt(0);
		if(ley){
			persisteDataLeyesExcel(worksheet, filasCabecera, componentes, idProgramaAno);
		}else{
			persisteDataExcel(worksheet, filasCabecera, componentes, idProgramaAno);
		}
		System.out.println("FIN PROCESAR PLANILLA MUNICIPAL");		
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) 
	public List<Integer> procesarModificacionPlanillaMunicipal(boolean ley, Integer IdProgramaAnoActual, XSSFWorkbook workbook, List<ComponentesVO> componentes,int filasCabecera) throws ExcelFormatException {

		XSSFSheet worksheet = workbook.getSheetAt(0);
		List<Integer> listaServicios = new ArrayList<Integer>();
		if(ley){
			persisteDataLeyesExcel(worksheet,filasCabecera,componentes,IdProgramaAnoActual);
		}else{
			listaServicios = persisteModificacionDataExcel(worksheet,filasCabecera,componentes,IdProgramaAnoActual);
		}

		System.out.println("FIN PROCESAR PLANILLA MUNICIPAL");		
		return listaServicios;
	}

	public void persisteDataExcel(XSSFSheet sheet, int filasCabecera, List<ComponentesVO> componentes, Integer idProgramaAno) throws ExcelFormatException {

			if(sheet == null){
				throw new ExcelFormatException("La hoja de cálculo esta nula");
			}


			System.out.println("Componentes del programa-->"+componentes.size());
			Map<Integer, Integer> cantidadSubtitulos = new HashMap<Integer, Integer>();
			for (ComponentesVO componente : componentes) {
				if(componente.getSubtitulos() != null && componente.getSubtitulos().size() > 0){
					for(SubtituloVO subtituloVO : componente.getSubtitulos()){
						if(Subtitulo.SUBTITULO24.getId().equals(subtituloVO.getId())){
							System.out.println("Componente: " + componente.getNombre( ) + " tiene: " + componente.getSubtitulos().size());
							//cantidadSubs.add(componente.getSubtitulos().size());
							if(cantidadSubtitulos.get(componente.getId()) == null){
								cantidadSubtitulos.put(componente.getId(), new Integer(1));
							}else{
								Integer cantidad = cantidadSubtitulos.get(componente.getId());
								cantidadSubtitulos.put(componente.getId(), (cantidad + 1));
							}
						}
					}
				}
			}

			List<Integer> mergeComponentes = new ArrayList<Integer>();

			for (ComponentesVO componente : componentes) {
				System.out.println(componente.getNombre() + " tiene " + cantidadSubtitulos.get(componente.getId()) + " subtitulos");
				if(cantidadSubtitulos.get(componente.getId()) != null && (cantidadSubtitulos.get(componente.getId()) > 0)){
					mergeComponentes.add(cantidadSubtitulos.get(componente.getId()) * 2);
				}
			}

			XSSFRow xssfRow;

			int columna = 4;
			int last = sheet.getPhysicalNumberOfRows();
			for (ComponentesVO componente : componentes) {
				//itera por subtitulos
				System.out.println("INGRESANDO DATOS DESDE EXCEL PARA COMPONENTE: " + componente.getNombre());
				if(cantidadSubtitulos.containsKey(componente.getId())){
					for(int sub=0; sub < cantidadSubtitulos.get(componente.getId()); sub++){
						for (int i = filasCabecera; i < last; i++) {	
							xssfRow = sheet.getRow(i);
							ProgramaMunicipalCore programaMunicipalCore = new ProgramaMunicipalCore();
							System.out.println("Leyendo registros fila: "+i);
							if(xssfRow != null){
								if(xssfRow.getCell(2) != null){
									Comuna comuna = comunaDAO.getComunaById((Double.valueOf(xssfRow.getCell(2).toString())).intValue());
									programaMunicipalCore.setComuna(comuna);
									
									ProgramaAno programaAno = recursosFinancierosProgramasReforzamientoDAO.findById(idProgramaAno);
									programaMunicipalCore.setProgramaAnoMunicipal(programaAno);
									recursosFinancierosProgramasReforzamientoDAO.save(programaMunicipalCore);

									ProgramaMunicipalCoreComponente programaMunicipalCoreComponente = new ProgramaMunicipalCoreComponente();
									programaMunicipalCoreComponente.setProgramaMunicipalCore(programaMunicipalCore);
									Integer monto = 0;
									if(xssfRow.getCell(columna) != null && !xssfRow.getCell(columna).toString().trim().isEmpty()){
										try{
											monto = Double.valueOf(xssfRow.getCell(columna).toString()).intValue();
										}catch (NumberFormatException numberFormatException) {
											throw new ExcelFormatException("Error en la columna " + (columna + 1) + " de la fila " + (i+1));
										}
									}
									programaMunicipalCoreComponente.setMonto(monto);
									Integer cantidad = 0;
									if(xssfRow.getCell(columna+1) != null && !xssfRow.getCell(columna+1).toString().trim().isEmpty()){
										try{
											cantidad = Double.valueOf(xssfRow.getCell(columna+1).toString()).intValue();
										}catch (NumberFormatException numberFormatException) {
											throw new ExcelFormatException("Error en la columna " + (columna + 2) + " de la fila " + (i+1));
										}
									}
									programaMunicipalCoreComponente.setCantidad(cantidad);

									int tarifa = monto * cantidad;
									programaMunicipalCoreComponente.setTarifa(tarifa);

									TipoSubtitulo tipoSubtitulo = tipoSubtituloDAO.getTipoSubtituloById(3);
									programaMunicipalCoreComponente.setSubtitulo(tipoSubtitulo);

									Componente componen = componenteDAO.getComponenteByID(componente.getId());
									programaMunicipalCoreComponente.setMunicipalCoreComponente(componen);

									System.out.println("comuna: "+comuna.getNombre()+" P:"+programaMunicipalCoreComponente.getTarifa()+" Q:"+programaMunicipalCoreComponente.getMonto());
									ProgramaMunicipalCoreComponentePK pk = new ProgramaMunicipalCoreComponentePK();
									pk.setComponente(componen.getId());
									pk.setProgramaMunicipalCore(programaMunicipalCore.getIdProgramaMunicipalCore());
									programaMunicipalCoreComponente.setProgramaMunicipalCoreComponentePK(pk);
									recursosFinancierosProgramasReforzamientoDAO.save(programaMunicipalCoreComponente);
								}else{
									System.out.println("Se han encontrado registros Nulos en la fila");
								}
							}
						}
						columna+=2;
					}
				}
			}
	}

	public List<Integer> persisteModificacionDataExcel(XSSFSheet sheet, int filasCabecera, 
			List<ComponentesVO> componentes, Integer idProgramaAno) throws ExcelFormatException {
		List<Integer> listaServicios = new  ArrayList<Integer>();
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


			HashMap<Integer, Integer> componentesSubMunis = new HashMap<Integer, Integer>();

			for(ComponentesVO componente : componentes){
				int subs=0;
				for(SubtituloVO subtitulo : componente.getSubtitulos()){

					if(subtitulo.getId() == 3){
						System.out.println("Componente: "+ componente.getNombre());
						subs++;

					}

				}
				componentesSubMunis.put(componente.getId(), subs);
			}
			int columna=4;

			for (int j = 0; j <componentes.size(); j++) {
				//itera subtitulos
				System.out.println("ACTUALIZANDO DATOS DESDE EXCEL PARA COMPONENTE: "+componentes.get(j).getNombre());
				if(componentesSubMunis.containsKey(componentes.get(j).getId())){
					for(int sub=0; sub < componentesSubMunis.get(componentes.get(j).getId());sub++){

						for (int i = filasCabecera; i < last; i++) {	
							xssfRow = sheet.getRow(i);
							System.out.println("Actualizando registros fila: "+i);
							if(xssfRow!=null){
								if(xssfRow.getCell(2)!=null){
									Comuna comuna = comunaDAO.getComunaById((Double.valueOf(xssfRow.getCell(2).toString())).intValue());

									if(listaServicios!=null){
										if(listaServicios.indexOf(comuna.getServicioSalud().getId())==-1){
											listaServicios.add(comuna.getServicioSalud().getId());
										}
									}else{
										listaServicios.add(comuna.getServicioSalud().getId());
									}

									ProgramaMunicipalCore municipalCore = programasDAO.getProgramaMunicipalCoreByComunaProgramaAno(comuna.getId(), idProgramaAno);
									System.out.println("idProgramaAno ----> "+idProgramaAno);
									System.out.println("municipalCore ---> "+municipalCore);

									ProgramaMunicipalCoreComponente coreComponente = programasDAO.getProgramaMunicipalCoreComponente(municipalCore.getIdProgramaMunicipalCore(), componentes.get(j).getId());

									Integer monto = (Double.valueOf(xssfRow.getCell(columna).toString()).intValue());
									Integer cantidad = (Double.valueOf(xssfRow.getCell(columna+1).toString()).intValue());

									Integer tarifa = monto * cantidad;

									if(coreComponente!=null){
										Integer tarifaAnterior = coreComponente.getTarifa();
										coreComponente.setTarifaAnterior(tarifaAnterior);
										coreComponente.setMonto(monto);
										coreComponente.setCantidad(cantidad);
										coreComponente.setTarifa(tarifa);
									}else{
										ProgramaMunicipalCoreComponente programaMunicipalCoreComponente = new ProgramaMunicipalCoreComponente();
										programaMunicipalCoreComponente.setProgramaMunicipalCore(municipalCore);

										programaMunicipalCoreComponente.setMonto(monto);
										programaMunicipalCoreComponente.setCantidad(cantidad);
										programaMunicipalCoreComponente.setTarifa(tarifa);

										TipoSubtitulo tipoSubtitulo = tipoSubtituloDAO.getTipoSubtituloById(3);
										programaMunicipalCoreComponente.setSubtitulo(tipoSubtitulo);

										Componente componen = componenteDAO.getComponenteByID(componentes.get(j).getId());
										programaMunicipalCoreComponente.setMunicipalCoreComponente(componen);

										ProgramaMunicipalCoreComponentePK pk = new ProgramaMunicipalCoreComponentePK();
										pk.setComponente(componen.getId());
										pk.setProgramaMunicipalCore(municipalCore.getIdProgramaMunicipalCore());
										programaMunicipalCoreComponente.setProgramaMunicipalCoreComponentePK(pk);
										recursosFinancierosProgramasReforzamientoDAO.save(programaMunicipalCoreComponente);
									}
								}else{
									System.out.println("Se han encontrado registros Nulos en la fila");
								}
							}

						}
						columna+=2;
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return listaServicios;

	}
	public void persisteDataLeyesExcel(XSSFSheet sheet, int filasCabecera, 
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
					for (int i = filasCabecera; i < last; i++) {	
						xssfRow = sheet.getRow(i);
						ProgramaMunicipalCore programaMunicipalCore = new ProgramaMunicipalCore();
						System.out.println("Leyendo registros fila: "+i);
						Comuna comuna = comunaDAO.getComunaById((Double.valueOf(xssfRow.getCell(2).toString())).intValue());
						programaMunicipalCore.setComuna(comuna);
						ProgramaAno programaAno = recursosFinancierosProgramasReforzamientoDAO.findById(idProgramaAno);
						programaMunicipalCore.setProgramaAnoMunicipal(programaAno);
						recursosFinancierosProgramasReforzamientoDAO.save(programaMunicipalCore);

						ProgramaMunicipalCoreComponente programaMunicipalCoreComponente = new ProgramaMunicipalCoreComponente();
						programaMunicipalCoreComponente.setProgramaMunicipalCore(programaMunicipalCore);

						programaMunicipalCoreComponente.setTarifa((Double.valueOf(xssfRow.getCell(columna).toString()).intValue()));

						TipoSubtitulo tipoSubtitulo = tipoSubtituloDAO.getTipoSubtituloByName(composMunis.get(j).getSubtitulos().get(k).getNombre());
						programaMunicipalCoreComponente.setSubtitulo(tipoSubtitulo);

						Componente componen = componenteDAO.getComponenteByID(composMunis.get(j).getId());
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


	public void persisteDataServicioExcel(XSSFSheet sheet, int filasCabecera, List<ComponentesVO> componentes, Integer idProgramaAno) throws ExcelFormatException {
			if(sheet == null){
				throw new ExcelFormatException("La hoja de cálculo esta nula");
			}

			int last = sheet.getPhysicalNumberOfRows();

			System.out.println("Componentes del programa-->"+componentes.size());
			Map<Integer, Integer> cantidadSubtitulos = new HashMap<Integer, Integer>();
			for (ComponentesVO componente : componentes) {
				if(componente.getSubtitulos() != null && componente.getSubtitulos().size() > 0){
					for(SubtituloVO subtituloVO : componente.getSubtitulos()){
						if(!Subtitulo.SUBTITULO24.getId().equals(subtituloVO.getId())){
							System.out.println("Componente: " + componente.getNombre( ) + " tiene: " + componente.getSubtitulos().size());
							//cantidadSubs.add(componente.getSubtitulos().size());
							if(cantidadSubtitulos.get(componente.getId()) == null){
								cantidadSubtitulos.put(componente.getId(), new Integer(1));
							}else{
								Integer cantidad = cantidadSubtitulos.get(componente.getId());
								cantidadSubtitulos.put(componente.getId(), (cantidad + 1));
							}
						}
					}
				}
			}

			//itera componentes

			for (int row = 4; row < last; row++) {	
				XSSFRow xssfRow = sheet.getRow(row);
				if(xssfRow != null){
					int columnaSub = 4;
					if(xssfRow.getCell(2) != null){
						for (ComponentesVO componente : componentes) {	
							int columnaEstablecimiento = 2;
							int columnaServicio = 0;
							for (SubtituloVO subtitulo : componente.getSubtitulos()) {
								if(!Subtitulo.SUBTITULO24.getId().equals(subtitulo.getId())){			
									System.out.println("columnaSub:"+columnaSub);
									ProgramaServicioCore programaServicioCore = new ProgramaServicioCore();
									System.out.println("Establecimiento codigo: " + xssfRow.getCell(columnaEstablecimiento).toString() + " Fila:" + row);
									Establecimiento establecimiento = establecimientoDAO.getEstablecimientoByCodigo(xssfRow.getCell(columnaEstablecimiento).toString());
									programaServicioCore.setEstablecimiento(establecimiento);

									ProgramaAno programaAno = recursosFinancierosProgramasReforzamientoDAO.findById(idProgramaAno);
									programaServicioCore.setProgramaAnoServicio(programaAno);

									ServicioSalud servicioSalud = servicioSaludDAO.getById((Double.valueOf(xssfRow.getCell(columnaServicio).toString())).intValue());
									programaServicioCore.setServicio(servicioSalud);
									recursosFinancierosProgramasReforzamientoDAO.save(programaServicioCore);


									ProgramaServicioCoreComponente programaServicioCoreComponente = new ProgramaServicioCoreComponente();
									programaServicioCoreComponente.setProgramaServicioCore1(programaServicioCore);
									Integer monto = 0;
									if(xssfRow.getCell(columnaSub) != null && !xssfRow.getCell(columnaSub).toString().trim().isEmpty()){
										try{
											monto = Double.valueOf(xssfRow.getCell(columnaSub).toString()).intValue();
										}catch (NumberFormatException numberFormatException) {
											throw new ExcelFormatException("Error en la columna " + (columnaSub + 1) + " de la fila " + (row+1));
										}	
									}
									programaServicioCoreComponente.setMonto(monto);
									Integer cantidad = 0;
									if(xssfRow.getCell((columnaSub + 1)) != null && !xssfRow.getCell((columnaSub + 1)).toString().trim().isEmpty()){
										try{
											cantidad = Double.valueOf(xssfRow.getCell((columnaSub + 1)).toString()).intValue();
										}catch (NumberFormatException numberFormatException) {
											throw new ExcelFormatException("Error en la columna " + (columnaSub + 2) + " de la fila " + (row+1));
										}	
									}
									programaServicioCoreComponente.setCantidad(cantidad);

									TipoSubtitulo tipoSubtitulo = tipoSubtituloDAO.getTipoSubtituloByName(subtitulo.getNombre());
									System.out.println("ID--->"+tipoSubtitulo.getIdTipoSubtitulo());
									programaServicioCoreComponente.setSubtitulo(tipoSubtitulo);

									int tarifa = monto * cantidad;
									programaServicioCoreComponente.setTarifa(tarifa);

									Componente componenteDTO = componenteDAO.getComponenteByID(componente.getId());
									programaServicioCoreComponente.setServicioCoreComponente(componenteDTO);

									ProgramaServicioCoreComponentePK pk = new ProgramaServicioCoreComponentePK();
									pk.setComponente(componenteDTO.getId());
									pk.setProgramaServicioCore(programaServicioCore.getIdProgramaServicioCore());
									pk.setSubtitulo(tipoSubtitulo.getIdTipoSubtitulo());
									programaServicioCoreComponente.setProgramaServicioCoreComponentePK(pk);
									recursosFinancierosProgramasReforzamientoDAO.save(programaServicioCoreComponente);
									columnaSub += 2;
								}
							}
						}
					}
				}
			}
	}


	public List<Integer> persisteModificacionDataServicioExcel(XSSFSheet sheet, int filasCabecera, 
			List<ComponentesVO> componentes, Integer idProgramaAno) throws ExcelFormatException {
		List<Integer> listaServicios = new ArrayList<Integer>();
		try{

			if(sheet == null){
				throw new ExcelFormatException("La hoja de cálculo esta nula");
			}

			int last = sheet.getPhysicalNumberOfRows();
			int first = 0;

			System.out.println("Componentes del programa-->"+componentes.size());
			List<Integer> cantidadSubs = new ArrayList<Integer>();
			int totalSubtitulos = 0;

			List<Integer> IdComponentes = new ArrayList<Integer>();

			HashMap<Integer, Integer> componenteSubtitulosServicio =  new HashMap<Integer, Integer>();
			for (int i = 0; i < componentes.size(); i++) {
				int subs=0;
				for(SubtituloVO sub : componentes.get(i).getSubtitulos()){
					if(sub.getId()!=3){
						IdComponentes.add(componentes.get(i).getId());
						subs++;
					}
				}
				componenteSubtitulosServicio.put(componentes.get(i).getId(), subs);
			}



			XSSFRow xssfRow;

			int subtitulosServicios=0;


			List<ComponentesVO> composServ= new ArrayList<ComponentesVO>();
			for (int j = 0; j <componentes.size(); j++) {
				System.out.println("COMPONENTE--> "+componentes.get(j).getId());
				if(componenteSubtitulosServicio.containsKey(componentes.get(j).getId()) && componenteSubtitulosServicio.get(componentes.get(j).getId()) > 0){
					for(int k=0; k < componentes.get(j).getSubtitulos().size();k++){

						SubtituloVO subMun = componentes.get(j).getSubtitulos().get(k);
						System.out.println("SUB ID: "+subMun.getId());
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
			}

			//itera componentes
			int columna=4;
			for(int h =0 ; h< componentes.size();h++){

				if(componenteSubtitulosServicio.containsKey(componentes.get(h).getId()) && componenteSubtitulosServicio.get(componentes.get(h).getId())>0){


					for(int k=0; k<componenteSubtitulosServicio.get(componentes.get(h).getId());k++){
						System.out.println("COLUMNA:"+columna);
						for (int i = 4; i < last; i++) {	
							xssfRow = sheet.getRow(i);
							System.out.println("Actualizando fila: "+i);
							if(xssfRow!=null){
								if(xssfRow.getCell(2)!=null){
									Establecimiento establecimiento = establecimientoDAO.getEstablecimientoByCodigo(xssfRow.getCell(2).toString());

									if(listaServicios!=null){
										if(listaServicios.indexOf(establecimiento.getServicioSalud().getId())==-1){
											listaServicios.add(establecimiento.getServicioSalud().getId());
										}
									}else{
										listaServicios.add(establecimiento.getServicioSalud().getId());
									}

									Integer subtitulo = componentes.get(h).getSubtitulos().get(k).getId();
									Integer componente = componentes.get(h).getId();


									ProgramaServicioCoreComponente coreComponente = programasDAO.getServicioCoreComponenteByProgramaAnoIdEstablecimientoComponenteSubtitulo(
											idProgramaAno,establecimiento.getId(),componente,subtitulo);


									Integer monto = (Double.valueOf(xssfRow.getCell(columna).toString()).intValue());
									Integer cantidad = (Double.valueOf(xssfRow.getCell(columna+1).toString()).intValue());

									Integer tarifa = monto * cantidad;

									if(coreComponente!=null){
										Integer montoAnterior = coreComponente.getTarifa();
										coreComponente.setTarifaAnterior(montoAnterior);
										coreComponente.setMonto(monto);
										coreComponente.setCantidad(cantidad);
										coreComponente.setTarifa(tarifa);
									}else{
										ProgramaServicioCore programaServicioCore = new ProgramaServicioCore();
										programaServicioCore.setEstablecimiento(establecimiento);

										ProgramaAno programaAno = recursosFinancierosProgramasReforzamientoDAO.findById(idProgramaAno);
										programaServicioCore.setProgramaAnoServicio(programaAno);

										ServicioSalud servicioSalud = servicioSaludDAO.getById((Double.valueOf(xssfRow.getCell(0).toString())).intValue());
										programaServicioCore.setServicio(servicioSalud);
										recursosFinancierosProgramasReforzamientoDAO.save(programaServicioCore);


										ProgramaServicioCoreComponente programaServicioCoreComponente = new ProgramaServicioCoreComponente();
										programaServicioCoreComponente.setProgramaServicioCore1(programaServicioCore);

										programaServicioCoreComponente.setMonto(monto);
										programaServicioCoreComponente.setCantidad(cantidad);

										TipoSubtitulo tipoSubtitulo = tipoSubtituloDAO.getTipoSubtituloByName(componentes.get(h).getSubtitulos().get(k).getNombre());
										programaServicioCoreComponente.setSubtitulo(tipoSubtitulo);

										programaServicioCoreComponente.setTarifa(tarifa);

										Componente componen = componenteDAO.getComponenteByID(componentes.get(h).getId());
										programaServicioCoreComponente.setServicioCoreComponente(componen);

										ProgramaServicioCoreComponentePK pk = new ProgramaServicioCoreComponentePK();
										pk.setComponente(componen.getId());
										pk.setProgramaServicioCore(programaServicioCore.getIdProgramaServicioCore());
										pk.setSubtitulo(tipoSubtitulo.getIdTipoSubtitulo());
										programaServicioCoreComponente.setProgramaServicioCoreComponentePK(pk);
										recursosFinancierosProgramasReforzamientoDAO.save(programaServicioCoreComponente);
									}
								}else{
									System.out.println("Se han encontrado registros Nulos en la fila");
								}	
							}
						}
						columna+=2;
					}
				}
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return listaServicios;

	}


	public void moveToAlfresco(Integer idProceso, Integer idProgramaAno, Integer referenciaDocumentoId, TipoDocumentosProcesos tipoDocumento, Integer ano, Boolean lastVersion) {
		System.out.println("Buscando referenciaDocumentoId="+referenciaDocumentoId);
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummary = documentService.getDocumentSummary(referenciaDocumentoId);
		System.out.println("Buscando referenciaDocumentoSummary="+referenciaDocumentoSummary);
		if(referenciaDocumentoSummary != null){
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contenType= mimemap.getContentType(referenciaDocumentoSummary.getPath().toLowerCase());
			BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummary.getPath()), contenType, folderRecursosFinancierosAPS.replace("{ANO}", ano.toString()));
			System.out.println("response upload template --->"+response);
			documentService.updateDocumentTemplate(referenciaDocumentoSummary.getId(), response.getNodeRef(), response.getFileName(), contenType);
			ProgramasReforzamiento programasReforzamiento = recursosFinancierosProgramasReforzamientoDAO.findByIdInstancia(idProceso);
			documentService.createDocumentProgramasReforzamiento(programasReforzamiento, tipoDocumento, response.getNodeRef(), response.getFileName(), contenType, idProgramaAno, lastVersion);
		}

	}

	public void procesarPlanillaServicio(Integer idProgramaAno, XSSFWorkbook workbook, List<ComponentesVO> componentes) throws ExcelFormatException {

		List<ProgramaServicioCore> programaServiciosCore = recursosFinancierosProgramasReforzamientoDAO.getProgramaServicioCoreByProgramaAno(idProgramaAno);
		if(programaServiciosCore != null && programaServiciosCore.size() > 0){
			List<Integer> idProgramasCore = new ArrayList<Integer>();
			for(ProgramaServicioCore servicioCore : programaServiciosCore){
				idProgramasCore.add(servicioCore.getIdProgramaServicioCore());
			}
			recursosFinancierosProgramasReforzamientoDAO.deleteProgramasServiciosCore(idProgramasCore);
		}
		XSSFSheet worksheet = workbook.getSheetAt(0);
		int filasCabecera = 4;
		persisteDataServicioExcel(worksheet, filasCabecera, componentes, idProgramaAno);
		System.out.println("FIN PROCESAR PLANILLA SERVICIO");		
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) 
	public List<Integer> procesarModificacionPlanillaServicio(Integer idProgramaAno,
			XSSFWorkbook workbook, List<ComponentesVO> componentes) throws ExcelFormatException {

		XSSFSheet worksheet = workbook.getSheetAt(0);

		int filasCabecera=4;
		System.out.println("FIN PROCESAR PLANILLA SERVICIO");		
		return persisteModificacionDataServicioExcel(worksheet,filasCabecera,componentes,idProgramaAno);


	}

	public void calcularActual(Integer idPrograma, String tipoHistorico, Integer ano) {
		ProgramaAno programaAnoActual = programasDAO.getProgramaAnoSiguiente(idPrograma, (ano-1));
		ProgramaAno programaAnoSiguiente = programasDAO.getProgramaAnoSiguiente(idPrograma, ano);

		if(programaAnoSiguiente == null){
			//crear 
			programaAnoSiguiente = new ProgramaAno();
			AnoEnCurso anoSiguienteDTO = programasDAO.getAnoEnCursoById(ano);
			if(anoSiguienteDTO == null) {
				AnoEnCurso anoActualDTO = programasDAO.getAnoEnCursoById((ano - 1));
				anoSiguienteDTO = new AnoEnCurso();
				anoSiguienteDTO.setAno(ano);
				anoSiguienteDTO.setMontoPercapitalBasal(anoActualDTO.getMontoPercapitalBasal());
				anoSiguienteDTO.setAsignacionAdultoMayor(anoActualDTO.getAsignacionAdultoMayor());
				anoSiguienteDTO.setInflactor(anoActualDTO.getInflactor());
				anoSiguienteDTO.setInflactorMarcoPresupuestario(anoActualDTO.getInflactorMarcoPresupuestario());
				programasDAO.saveAnoCurso(anoSiguienteDTO);
			}
			programaAnoSiguiente.setAno(anoSiguienteDTO);
			programaAnoSiguiente.setEstado(new EstadoPrograma(1));
			programaAnoSiguiente.setEstadoFlujoCaja(new EstadoPrograma(1));
			programaAnoSiguiente.setPrograma(programaAnoActual.getPrograma());
			programaAnoSiguiente.setEstadoConvenio(new EstadoPrograma(1));
			programaAnoSiguiente.setEstadoreliquidacion(new EstadoPrograma(1));
			programasDAO.save(programaAnoSiguiente);
		} 


		if(tipoHistorico.equals("municipal")){
			calculaMunicipalDesdeHistorico(programaAnoActual,programaAnoSiguiente);
		}

		if(tipoHistorico.equals("servicio")){
			calculaServicioDesdeHistorico(programaAnoActual, programaAnoSiguiente);
		}

		if(tipoHistorico.equals("mixto")){
			calculaMunicipalDesdeHistorico(programaAnoActual, programaAnoSiguiente);
			calculaServicioDesdeHistorico(programaAnoActual, programaAnoSiguiente);
		}

	}

	private void calculaMunicipalDesdeHistorico(ProgramaAno programaAnoActual, ProgramaAno programaAnoSiguiente){
		Double inflactorS24 = tipoSubtituloDAO.getInfactorById(3);

		List<ProgramaMunicipalCore> programaMunicipalesCore = recursosFinancierosProgramasReforzamientoDAO.getProgramasCoreByProgramaAno(programaAnoSiguiente.getIdProgramaAno());
		if(programaMunicipalesCore != null && programaMunicipalesCore.size() > 0){
			List<Integer> idProgramasCore = new ArrayList<Integer>();
			for(ProgramaMunicipalCore programaMunicipalCore : programaMunicipalesCore){
				idProgramasCore.add(programaMunicipalCore.getIdProgramaMunicipalCore());
			}
			recursosFinancierosProgramasReforzamientoDAO.deleteProgramasMunicipalCore(idProgramasCore);
		}

		for(ProgramaMunicipalCore progMun : programaAnoActual.getProgramasMunicipalesCore()){

			ProgramaMunicipalCore nuevoCore = new ProgramaMunicipalCore();
			nuevoCore.setComuna(progMun.getComuna());
			nuevoCore.setEstablecimiento(progMun.getEstablecimiento());
			nuevoCore.setProgramaAnoMunicipal(programaAnoSiguiente);
			programasDAO.save(nuevoCore);

			for(ProgramaMunicipalCoreComponente compoMun : progMun.getProgramaMunicipalCoreComponentes()){

				ProgramaMunicipalCoreComponente nuevoComponente = new ProgramaMunicipalCoreComponente();
				nuevoComponente.setMunicipalCoreComponente(compoMun.getMunicipalCoreComponente());
				nuevoComponente.setProgramaMunicipalCore(nuevoCore);

				ProgramaMunicipalCoreComponentePK componentePk = new ProgramaMunicipalCoreComponentePK();
				System.out.println("nuevoCore.getIdProgramaMunicipalCore()-->"+nuevoCore.getIdProgramaMunicipalCore());
				System.out.println("compoMun.getProgramaMunicipalCoreComponentePK().getComponente()-->"+compoMun.getProgramaMunicipalCoreComponentePK().getComponente());
				componentePk.setComponente(compoMun.getProgramaMunicipalCoreComponentePK().getComponente());
				componentePk.setProgramaMunicipalCore(nuevoCore.getIdProgramaMunicipalCore());

				System.out.println(componentePk.toString());
				nuevoComponente.setProgramaMunicipalCoreComponentePK(componentePk);
				nuevoComponente.setSubtitulo(compoMun.getSubtitulo()); 
				Long nuevaTarifa = Math.round(compoMun.getTarifa()*inflactorS24);
				nuevoComponente.setTarifa(nuevaTarifa.intValue());

				programasDAO.save(nuevoComponente);
			}

		}
	}

	private void calculaServicioDesdeHistorico(ProgramaAno programaAnoActual, ProgramaAno programaAnoSiguiente){
		Double inflactorS21 = tipoSubtituloDAO.getInfactorById(1);
		Double inflactorS22 = tipoSubtituloDAO.getInfactorById(2);
		Double inflactorS29 = tipoSubtituloDAO.getInfactorById(4);

		List<ProgramaServicioCore> programaServiciosCore = recursosFinancierosProgramasReforzamientoDAO.getProgramaServicioCoreByProgramaAno(programaAnoSiguiente.getIdProgramaAno());


		if(programaServiciosCore != null && programaServiciosCore.size() > 0){
			List<Integer> idProgramasCore = new ArrayList<Integer>();
			for(ProgramaServicioCore servicioCore : programaServiciosCore){
				idProgramasCore.add(servicioCore.getIdProgramaServicioCore());
			}
			recursosFinancierosProgramasReforzamientoDAO.deleteProgramasServiciosCore(idProgramasCore);
		}

		for(ProgramaServicioCore progServ : programaAnoActual.getProgramasServiciosCore()){

			ProgramaServicioCore nuevoCore = new ProgramaServicioCore();
			nuevoCore.setEstablecimiento(progServ.getEstablecimiento());
			nuevoCore.setProgramaAnoServicio(programaAnoSiguiente);
			nuevoCore.setServicio(progServ.getServicio());
			programasDAO.save(nuevoCore);

			for(ProgramaServicioCoreComponente compoServ : progServ.getProgramaServicioCoreComponentes()){

				ProgramaServicioCoreComponente nuevoComponente = new ProgramaServicioCoreComponente();
				nuevoComponente.setServicioCoreComponente(compoServ.getServicioCoreComponente());
				nuevoComponente.setProgramaServicioCore1(nuevoCore);

				ProgramaServicioCoreComponentePK componentePk =  new ProgramaServicioCoreComponentePK();
				componentePk.setComponente(compoServ.getProgramaServicioCoreComponentePK().getComponente());
				componentePk.setProgramaServicioCore(nuevoCore.getIdProgramaServicioCore());
				componentePk.setSubtitulo(compoServ.getSubtitulo().getIdTipoSubtitulo());


				nuevoComponente.setProgramaServicioCoreComponentePK(componentePk);
				nuevoComponente.setSubtitulo(compoServ.getSubtitulo());

				if(compoServ.getSubtitulo().getIdTipoSubtitulo()==1){
					Long nuevaTarifa = Math.round(compoServ.getTarifa()*inflactorS21);
					nuevoComponente.setTarifa(nuevaTarifa.intValue());
				}
				if(compoServ.getSubtitulo().getIdTipoSubtitulo()==2){
					Long nuevaTarifa = Math.round(compoServ.getTarifa()*inflactorS22);
					nuevoComponente.setTarifa(nuevaTarifa.intValue());
				}
				if(compoServ.getSubtitulo().getIdTipoSubtitulo()==4){
					Long nuevaTarifa = Math.round(compoServ.getTarifa()*inflactorS29);
					nuevoComponente.setTarifa(nuevaTarifa.intValue());
				}

				programasDAO.save(nuevoComponente);
			}

		}

	}

	public List<ResumenProgramaMixtoVO>  getConsolidadoPrograma(Integer idPrograma, Integer ano){
		Long date = Calendar.getInstance().getTimeInMillis();
		System.out.println("******************* inicio metodo *******************");
		System.out.println("idPrograma-->"+idPrograma);
		System.out.println("ano-->"+ano);
		List<ResumenProgramaMixtoVO> resumen = new ArrayList<ResumenProgramaMixtoVO>();
		ProgramaVO programa = programaService.getProgramaByIdProgramaAndAno(idPrograma, ano);
		List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
		Map<Integer, List<Integer>> componentesBySubtitulos = new HashMap<Integer, List<Integer>>();
		for(ComponentesVO componente: programa.getComponentes()){
			for(SubtituloVO subtitulo : componente.getSubtitulos()){
				if(componentesBySubtitulos.get(subtitulo.getId()) == null){
					List<Integer> componentes = new ArrayList<Integer>();
					componentes.add(componente.getId());
					componentesBySubtitulos.put(subtitulo.getId(), componentes);
				}else{
					componentesBySubtitulos.get(subtitulo.getId()).add(componente.getId());
				}
			}
		}
		if(programa.getDependenciaMunicipal() && programa.getDependenciaServicio()){
			for(ServicioSalud servicioSalud : servicios){
				ResumenProgramaMixtoVO resumenProgramaMixtoVO = new ResumenProgramaMixtoVO();
				resumenProgramaMixtoVO.setIdServicio(servicioSalud.getId());
				resumenProgramaMixtoVO.setNombreServicio(servicioSalud.getNombre());
				resumenProgramaMixtoVO.setTotalS24(0L);
				resumenProgramaMixtoVO.setTotalS21(0L);
				resumenProgramaMixtoVO.setTotalS22(0L);
				resumenProgramaMixtoVO.setTotalS29(0L);
				List<ComponentesVO> componentesPrograma = programaService.getComponentesByProgramaAnoSubtitulos(programa.getIdProgramaAno(), Subtitulo.SUBTITULO24);
				if(componentesPrograma != null && componentesPrograma.size() > 0){
					resumenProgramaMixtoVO.setSub24(true);
				}
				componentesPrograma = programaService.getComponentesByProgramaAnoSubtitulos(programa.getIdProgramaAno(), Subtitulo.SUBTITULO21);
				if(componentesPrograma != null && componentesPrograma.size() > 0){
					resumenProgramaMixtoVO.setSub21(true);
				}
				componentesPrograma = programaService.getComponentesByProgramaAnoSubtitulos(programa.getIdProgramaAno(), Subtitulo.SUBTITULO22);
				if(componentesPrograma != null && componentesPrograma.size() > 0){
					resumenProgramaMixtoVO.setSub22(true);
				}
				componentesPrograma = programaService.getComponentesByProgramaAnoSubtitulos(programa.getIdProgramaAno(), Subtitulo.SUBTITULO29);
				if(componentesPrograma != null && componentesPrograma.size() > 0){
					resumenProgramaMixtoVO.setSub29(true);
				}
				for (Map.Entry<Integer, List<Integer>> entry : componentesBySubtitulos.entrySet()) {
					System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()); 
					if(Subtitulo.SUBTITULO24.getId().equals(entry.getKey())){
						List<ProgramaMunicipalCoreComponente> programaMunicipalesCoreComponente = recursosFinancierosProgramasReforzamientoDAO.getProgramasCoreComponenteByServicioProgramaAnoComponentesSubtitulo(servicioSalud.getId(),
								programa.getIdProgramaAno(), entry.getValue(), entry.getKey());
						if(programaMunicipalesCoreComponente != null && programaMunicipalesCoreComponente.size()>0){
							for(ProgramaMunicipalCoreComponente programaMunicipalCoreComponente : programaMunicipalesCoreComponente){
								resumenProgramaMixtoVO.setTotalS24(resumenProgramaMixtoVO.getTotalS24() + programaMunicipalCoreComponente.getTarifa());
							}
						}

					}else{
						System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()); 
						List<ProgramaServicioCoreComponente> programaServiciosCoreComponente = recursosFinancierosProgramasReforzamientoDAO.getProgramasServicioCoreComponenteByServicioProgramaAnoComponentesSubtitulo(servicioSalud.getId(),
								programa.getIdProgramaAno(), entry.getValue(), entry.getKey());
						if(Subtitulo.SUBTITULO21.getId().equals(entry.getKey())){
							for(ProgramaServicioCoreComponente programaServicioCoreComponente : programaServiciosCoreComponente){
								resumenProgramaMixtoVO.setTotalS21(resumenProgramaMixtoVO.getTotalS21() + programaServicioCoreComponente.getTarifa());
							}
						}else if(Subtitulo.SUBTITULO22.getId().equals(entry.getKey())){
							for(ProgramaServicioCoreComponente programaServicioCoreComponente : programaServiciosCoreComponente){
								resumenProgramaMixtoVO.setTotalS22(resumenProgramaMixtoVO.getTotalS22() + programaServicioCoreComponente.getTarifa());
							}
						}else  if(Subtitulo.SUBTITULO29.getId().equals(entry.getKey())){
							for(ProgramaServicioCoreComponente programaServicioCoreComponente : programaServiciosCoreComponente){
								resumenProgramaMixtoVO.setTotalS29(resumenProgramaMixtoVO.getTotalS29() + programaServicioCoreComponente.getTarifa());
							}
						}
					}
				}
				resumen.add(resumenProgramaMixtoVO);
			}

		}else{
			if(programa.getDependenciaMunicipal()){
				for(ServicioSalud servicioSalud : servicios){
					ResumenProgramaMixtoVO resumenProgramaMixtoVO = new ResumenProgramaMixtoVO();
					resumenProgramaMixtoVO.setIdServicio(servicioSalud.getId());
					resumenProgramaMixtoVO.setNombreServicio(servicioSalud.getNombre());
					resumenProgramaMixtoVO.setTotalS24(0L);
					resumenProgramaMixtoVO.setTotalS21(0L);
					resumenProgramaMixtoVO.setTotalS22(0L);
					resumenProgramaMixtoVO.setTotalS29(0L);
					resumenProgramaMixtoVO.setSub24(true);
					List<ProgramaMunicipalCoreComponente> programaMunicipalesCoreComponente = recursosFinancierosProgramasReforzamientoDAO.getProgramasCoreComponenteByServicioProgramaAnoComponentesSubtitulo(servicioSalud.getId(),
							programa.getIdProgramaAno(), componentesBySubtitulos.get(Subtitulo.SUBTITULO24.getId()), Subtitulo.SUBTITULO24.getId());
					if(programaMunicipalesCoreComponente != null && programaMunicipalesCoreComponente.size()>0){
						for(ProgramaMunicipalCoreComponente programaMunicipalCoreComponente : programaMunicipalesCoreComponente){
							resumenProgramaMixtoVO.setTotalS24(resumenProgramaMixtoVO.getTotalS24() + programaMunicipalCoreComponente.getTarifa());
						}
					}
					resumen.add(resumenProgramaMixtoVO);
				}
			}else{
				for(ServicioSalud servicioSalud : servicios){
					ResumenProgramaMixtoVO resumenProgramaMixtoVO = new ResumenProgramaMixtoVO();
					resumenProgramaMixtoVO.setIdServicio(servicioSalud.getId());
					resumenProgramaMixtoVO.setNombreServicio(servicioSalud.getNombre());
					resumenProgramaMixtoVO.setTotalS24(0L);
					resumenProgramaMixtoVO.setTotalS21(0L);
					resumenProgramaMixtoVO.setTotalS22(0L);
					resumenProgramaMixtoVO.setTotalS29(0L);

					List<ComponentesVO> componentesPrograma = programaService.getComponentesByProgramaAnoSubtitulos(programa.getIdProgramaAno(), Subtitulo.SUBTITULO21);
					if(componentesPrograma != null && componentesPrograma.size() > 0){
						resumenProgramaMixtoVO.setSub21(true);
					}
					componentesPrograma = programaService.getComponentesByProgramaAnoSubtitulos(programa.getIdProgramaAno(), Subtitulo.SUBTITULO22);
					if(componentesPrograma != null && componentesPrograma.size() > 0){
						resumenProgramaMixtoVO.setSub22(true);
					}
					componentesPrograma = programaService.getComponentesByProgramaAnoSubtitulos(programa.getIdProgramaAno(), Subtitulo.SUBTITULO29);
					if(componentesPrograma != null && componentesPrograma.size() > 0){
						resumenProgramaMixtoVO.setSub29(true);
					}

					for (Map.Entry<Integer, List<Integer>> entry : componentesBySubtitulos.entrySet()) { 
						System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()); 
						List<ProgramaServicioCoreComponente> programaServiciosCoreComponente = recursosFinancierosProgramasReforzamientoDAO.getProgramasServicioCoreComponenteByServicioProgramaAnoComponentesSubtitulo(servicioSalud.getId(),
								programa.getIdProgramaAno(), entry.getValue(), entry.getKey());
						if(Subtitulo.SUBTITULO21.getId().equals(entry.getKey())){
							for(ProgramaServicioCoreComponente programaServicioCoreComponente : programaServiciosCoreComponente){
								resumenProgramaMixtoVO.setTotalS21(resumenProgramaMixtoVO.getTotalS21() + programaServicioCoreComponente.getTarifa());
							}
						}else if(Subtitulo.SUBTITULO22.getId().equals(entry.getKey())){
							for(ProgramaServicioCoreComponente programaServicioCoreComponente : programaServiciosCoreComponente){
								resumenProgramaMixtoVO.setTotalS22(resumenProgramaMixtoVO.getTotalS22() + programaServicioCoreComponente.getTarifa());
							}
						}else{
							for(ProgramaServicioCoreComponente programaServicioCoreComponente : programaServiciosCoreComponente){
								resumenProgramaMixtoVO.setTotalS29(resumenProgramaMixtoVO.getTotalS29() + programaServicioCoreComponente.getTarifa());
							}
						}
					}
					resumen.add(resumenProgramaMixtoVO);
				}
			}
		}
		System.out.println("******************* final metodo: "+(Calendar.getInstance().getTimeInMillis()-date)/1000 +" segs.");
		return resumen;
	}

	public List<ResumenProgramaMixtoVO>  getConsolidadoProgramaModificado(Integer idPrograma, Integer ano){
		Long date = Calendar.getInstance().getTimeInMillis();
		System.out.println("******************* inicio metodo getConsolidadoProgramaModificado");
		List<ResumenProgramaMixtoVO> resumen = new ArrayList<ResumenProgramaMixtoVO>();
		ProgramaVO programa = getProgramaById(idPrograma);
		Integer idProgramaAno = programasDAO.getIdProgramaAnoAnterior(idPrograma, ano);
		List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
		Map<Integer, List<Integer>> componentesBySubtitulos = new HashMap<Integer, List<Integer>>();
		for(ComponentesVO componente: programa.getComponentes()){
			for(SubtituloVO subtitulo : componente.getSubtitulos()){
				if(componentesBySubtitulos.get(subtitulo.getId()) == null){
					List<Integer> componentes = new ArrayList<Integer>();
					componentes.add(componente.getId());
					componentesBySubtitulos.put(subtitulo.getId(), componentes);
				}else{
					componentesBySubtitulos.get(subtitulo.getId()).add(componente.getId());
				}
			}
		}
		if(programa.getDependenciaMunicipal() && programa.getDependenciaServicio()){
			for(ServicioSalud servicioSalud : servicios){
				ResumenProgramaMixtoVO resumenProgramaMixtoVO = new ResumenProgramaMixtoVO();
				resumenProgramaMixtoVO.setIdServicio(servicioSalud.getId());
				resumenProgramaMixtoVO.setNombreServicio(servicioSalud.getNombre());
				resumenProgramaMixtoVO.setTotalS24(0L);
				resumenProgramaMixtoVO.setTotalS21(0L);
				resumenProgramaMixtoVO.setTotalS22(0L);
				resumenProgramaMixtoVO.setTotalS29(0L);
				for (Map.Entry<Integer, List<Integer>> entry : componentesBySubtitulos.entrySet()) {
					System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()); 
					if(Subtitulo.SUBTITULO24.getId().equals(entry.getKey())){
						List<ProgramaMunicipalCoreComponente> programaMunicipalesCoreComponente = recursosFinancierosProgramasReforzamientoDAO.getProgramasCoreComponenteByServicioProgramaAnoComponentesSubtitulo(servicioSalud.getId(),
								idProgramaAno, entry.getValue(), entry.getKey());
						if(programaMunicipalesCoreComponente != null && programaMunicipalesCoreComponente.size()>0){
							for(ProgramaMunicipalCoreComponente programaMunicipalCoreComponente : programaMunicipalesCoreComponente){
								resumenProgramaMixtoVO.setTotalS24(resumenProgramaMixtoVO.getTotalS24() + programaMunicipalCoreComponente.getTarifa());
							}
						}

					}else{
						System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()); 
						List<ProgramaServicioCoreComponente> programaServiciosCoreComponente = recursosFinancierosProgramasReforzamientoDAO.getProgramasServicioCoreComponenteByServicioProgramaAnoComponentesSubtitulo(servicioSalud.getId(),
								idProgramaAno, entry.getValue(), entry.getKey());
						if(Subtitulo.SUBTITULO21.getId().equals(entry.getKey())){
							for(ProgramaServicioCoreComponente programaServicioCoreComponente : programaServiciosCoreComponente){
								resumenProgramaMixtoVO.setTotalS21(resumenProgramaMixtoVO.getTotalS21() + programaServicioCoreComponente.getTarifa());
							}
						}else if(Subtitulo.SUBTITULO22.getId().equals(entry.getKey())){
							for(ProgramaServicioCoreComponente programaServicioCoreComponente : programaServiciosCoreComponente){
								resumenProgramaMixtoVO.setTotalS22(resumenProgramaMixtoVO.getTotalS22() + programaServicioCoreComponente.getTarifa());
							}
						}else  if(Subtitulo.SUBTITULO29.getId().equals(entry.getKey())){
							for(ProgramaServicioCoreComponente programaServicioCoreComponente : programaServiciosCoreComponente){
								resumenProgramaMixtoVO.setTotalS29(resumenProgramaMixtoVO.getTotalS29() + programaServicioCoreComponente.getTarifa());
							}
						}
					}
				}
				resumen.add(resumenProgramaMixtoVO);
			}

		}else{
			if(programa.getDependenciaMunicipal()){
				for(ServicioSalud servicioSalud : servicios){
					ResumenProgramaMixtoVO resumenProgramaMixtoVO = new ResumenProgramaMixtoVO();
					resumenProgramaMixtoVO.setIdServicio(servicioSalud.getId());
					resumenProgramaMixtoVO.setNombreServicio(servicioSalud.getNombre());
					resumenProgramaMixtoVO.setTotalS24(0L);
					resumenProgramaMixtoVO.setTotalS21(0L);
					resumenProgramaMixtoVO.setTotalS22(0L);
					resumenProgramaMixtoVO.setTotalS29(0L);
					List<ProgramaMunicipalCoreComponente> programaMunicipalesCoreComponente = recursosFinancierosProgramasReforzamientoDAO.getProgramasCoreComponenteByServicioProgramaAnoComponentesSubtitulo(servicioSalud.getId(),
							idProgramaAno, componentesBySubtitulos.get(Subtitulo.SUBTITULO24.getId()), Subtitulo.SUBTITULO24.getId());
					if(programaMunicipalesCoreComponente != null && programaMunicipalesCoreComponente.size()>0){
						for(ProgramaMunicipalCoreComponente programaMunicipalCoreComponente : programaMunicipalesCoreComponente){

							resumenProgramaMixtoVO.setTotalS24(resumenProgramaMixtoVO.getTotalS24() + programaMunicipalCoreComponente.getTarifa());
						}
					}
					resumen.add(resumenProgramaMixtoVO);
				}
			}else{
				for(ServicioSalud servicioSalud : servicios){
					ResumenProgramaMixtoVO resumenProgramaMixtoVO = new ResumenProgramaMixtoVO();
					resumenProgramaMixtoVO.setIdServicio(servicioSalud.getId());
					resumenProgramaMixtoVO.setNombreServicio(servicioSalud.getNombre());
					resumenProgramaMixtoVO.setTotalS24(0L);
					resumenProgramaMixtoVO.setTotalS21(0L);
					resumenProgramaMixtoVO.setTotalS22(0L);
					resumenProgramaMixtoVO.setTotalS29(0L);
					for (Map.Entry<Integer, List<Integer>> entry : componentesBySubtitulos.entrySet()) { 
						System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()); 
						List<ProgramaServicioCoreComponente> programaServiciosCoreComponente = recursosFinancierosProgramasReforzamientoDAO.getProgramasServicioCoreComponenteByServicioProgramaAnoComponentesSubtitulo(servicioSalud.getId(),
								idProgramaAno, entry.getValue(), entry.getKey());
						if(Subtitulo.SUBTITULO21.getId().equals(entry.getKey())){
							for(ProgramaServicioCoreComponente programaServicioCoreComponente : programaServiciosCoreComponente){
								resumenProgramaMixtoVO.setTotalS21(resumenProgramaMixtoVO.getTotalS21() + programaServicioCoreComponente.getTarifa());
							}
						}else if(Subtitulo.SUBTITULO22.getId().equals(entry.getKey())){
							for(ProgramaServicioCoreComponente programaServicioCoreComponente : programaServiciosCoreComponente){
								resumenProgramaMixtoVO.setTotalS22(resumenProgramaMixtoVO.getTotalS22() + programaServicioCoreComponente.getTarifa());
							}
						}else{
							for(ProgramaServicioCoreComponente programaServicioCoreComponente : programaServiciosCoreComponente){
								resumenProgramaMixtoVO.setTotalS29(resumenProgramaMixtoVO.getTotalS29() + programaServicioCoreComponente.getTarifa());
							}
						}
					}
					resumen.add(resumenProgramaMixtoVO);
				}
			}
		}
		System.out.println("******************* final metodo: "+(Calendar.getInstance().getTimeInMillis()-date)/1000 +" segs.");
		return resumen;
	}

	public Integer elaborarResolucionProgramaReforzamiento(Integer idProceso, Integer idPrograma, List<ResumenProgramaMixtoVO> resumen, Integer ano) {
		ProgramaVO programa = programaService.getProgramaByIdProgramaAndAno(idPrograma, ano);
		Integer plantillaBorradorResolucionProgramaReforzamiento = documentService.getPlantillaByType(TipoDocumentosProcesos.RESOLUCIONPROGRAMASAPS);
		if (plantillaBorradorResolucionProgramaReforzamiento == null) {
			throw new RuntimeException("No se puede crear Borrador Decreto Aporte Estatal, la plantilla no esta cargada");
		}

		try {
			Long totalSub21 = 0L;
			Long totalSub22 = 0L;
			Long totalSub24 = 0L;
			Long totalSub29 = 0L;
			boolean sub21 = false;
			boolean sub22 = false;
			boolean sub24 = false;
			boolean sub29 = false; 
			if(resumen.size() > 0){
				for(ResumenProgramaMixtoVO res : resumen){
					if(res.getTotalS21() != null && (res.getTotalS21() > 0)){
						totalSub21 += res.getTotalS21();
						sub21 = true;
					}
					if(res.getTotalS22() != null && (res.getTotalS22() > 0)){
						totalSub22 += res.getTotalS22();
						sub22 = true;
					}
					if(res.getTotalS24() != null && (res.getTotalS24() > 0)){
						totalSub24 += res.getTotalS24();
						sub24 = true;
					}
					if(res.getTotalS29() != null && (res.getTotalS29() > 0)){
						totalSub29 += res.getTotalS29();
						sub29 = true;
					}
				}
			}

			StringBuilder resumenAsignacion = new StringBuilder();
			if(sub24){
				resumenAsignacion.append("\\$").append(StringUtil.formatNumber(totalSub24)).append(" al subtitulo 24");
			}
			if(sub21){
				resumenAsignacion.append(" \\$").append(StringUtil.formatNumber(totalSub21)).append(" al subtitulo 21");
			}
			if(sub22){
				resumenAsignacion.append(" \\$").append(StringUtil.formatNumber(totalSub22)).append(" al subtitulo 22");
			}
			if(sub29){
				resumenAsignacion.append(" \\$").append(StringUtil.formatNumber(totalSub29)).append(" al subtitulo 29");
			}
			System.out.println(resumenAsignacion.toString());
			ReferenciaDocumentoSummaryVO referenciaResolucionProgramasAPSVO = documentService.getDocumentByPlantillaId(plantillaBorradorResolucionProgramaReforzamiento);
			DocumentoVO documentoBorradorResolucionProgramasAPSVO = documentService.getDocument(referenciaResolucionProgramasAPSVO.getId());
			String templateResolucionProgramasAPS = tmpDirDoc + File.separator + documentoBorradorResolucionProgramasAPSVO.getName();
			templateResolucionProgramasAPS = templateResolucionProgramasAPS.replace(" ", "");

			String filenameBorradorResolucionProgramasAPS = tmpDirDoc + File.separator + new Date().getTime() + "_" + "ResolucionPrograma" + programa.getNombre().replace(":", "-") + ".docx";
			filenameBorradorResolucionProgramasAPS = filenameBorradorResolucionProgramasAPS.replaceAll(" ", "");
			filenameBorradorResolucionProgramasAPS = StringUtil.removeSpanishAccents(filenameBorradorResolucionProgramasAPS);
			System.out.println("filenameBorradorResolucionProgramasAPS filename-->"	+ filenameBorradorResolucionProgramasAPS);
			System.out.println("templateResolucionProgramasAPS template-->"	+ templateResolucionProgramasAPS);
			GeneradorWord generadorWordPlantillaBorradorOrdinarioProgramacionCaja = new GeneradorWord(templateResolucionProgramasAPS);
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contentType = mimemap.getContentType(templateResolucionProgramasAPS.toLowerCase());
			System.out.println("contenTypeBorradorAporteEstatal->" + contentType);

			generadorWordPlantillaBorradorOrdinarioProgramacionCaja.saveContent(documentoBorradorResolucionProgramasAPSVO.getContent(), XWPFDocument.class);

			Map<String, Object> parametersBorradorAporteEstatal = new HashMap<String, Object>();
			parametersBorradorAporteEstatal.put("{nombrePrograma}", programa.getNombre().replace(":", "\\:"));
			parametersBorradorAporteEstatal.put("{anoPrograma}", ano);
			parametersBorradorAporteEstatal.put("{resumenAsignacion}", resumenAsignacion.toString());

			GeneradorResolucionAporteEstatal generadorWordDecretoBorradorAporteEstatal = new GeneradorResolucionAporteEstatal(filenameBorradorResolucionProgramasAPS, templateResolucionProgramasAPS);
			generadorWordDecretoBorradorAporteEstatal.setReplaceInTable(true);
			generadorWordDecretoBorradorAporteEstatal.replaceValues(parametersBorradorAporteEstatal, XWPFDocument.class);

			BodyVO response = alfrescoService.uploadDocument(new File(filenameBorradorResolucionProgramasAPS), contentType, folderRecursosFinancierosAPS.replace("{ANO}", ano.toString()));
			System.out.println("response responseBorradorAporteEstatal --->" + response);
			ProgramasReforzamiento programasReforzamiento = recursosFinancierosProgramasReforzamientoDAO.findByIdInstancia(idProceso);
			  
			plantillaBorradorResolucionProgramaReforzamiento = documentService.createDocumentProgramasReforzamiento(programasReforzamiento, TipoDocumentosProcesos.RESOLUCIONPROGRAMASAPS, 
					response.getNodeRef(), response.getFileName(), contentType, programa.getIdProgramaAno(), null);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException ex){
			ex.printStackTrace();
		} catch (Docx4JException de){
			de.printStackTrace();
		}
		return plantillaBorradorResolucionProgramaReforzamiento;
	}

	public Integer elaborarResolucionModificacionProgramaReforzamiento(Integer idProceso, Integer idPrograma, List<ResumenProgramaMixtoVO> resumen, List<Integer> listaServicios, Integer ano) {

		ProgramaVO programaVO = programaService.getProgramaByIdProgramaAndAno(idPrograma, ano);
		Integer plantillaBorradorResolucionProgramaReforzamiento = documentService
				.getPlantillaByType(TipoDocumentosProcesos.MODIFICACIONRESOLUCIONPROGRAMASAPS);

		if (plantillaBorradorResolucionProgramaReforzamiento == null) {
			throw new RuntimeException(
					"No se puede crear Borrador Decreto Aporte Estatal, la plantilla no esta cargada");
		}

		try {

			Long totalActual=0l;
			List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
			Map<Integer, List<Integer>> componentesBySubtitulos = new HashMap<Integer, List<Integer>>();
			for(ComponentesVO componente: programaVO.getComponentes()){
				for(SubtituloVO subtitulo : componente.getSubtitulos()){
					if(componentesBySubtitulos.get(subtitulo.getId()) == null){
						List<Integer> componentes = new ArrayList<Integer>();
						componentes.add(componente.getId());
						componentesBySubtitulos.put(subtitulo.getId(), componentes);
					}else{
						componentesBySubtitulos.get(subtitulo.getId()).add(componente.getId());
					}
				}
			}

			for(ServicioSalud servicio : servicios){
				List<ProgramaMunicipalCoreComponente> programaMunicipalesCoreComponente = 
						recursosFinancierosProgramasReforzamientoDAO.getProgramasCoreComponenteByServicioProgramaAnoComponentesSubtitulo(
								servicio.getId(),idPrograma, componentesBySubtitulos.get(Subtitulo.SUBTITULO24.getId()), Subtitulo.SUBTITULO24.getId());

				for(ProgramaMunicipalCoreComponente componente : programaMunicipalesCoreComponente){
					if(componente.getTarifaAnterior()!=null){
						totalActual+=componente.getTarifaAnterior();
					}else{
						totalActual+=componente.getTarifa();
					}
				}
			}

			Long tS21=0L;
			Long tS22=0L;
			Long tS24=0L;
			Long tS29=0L;
			boolean s21=false;
			boolean s22=false;
			boolean s24=false;
			boolean s29=false; 
			if(resumen.size()>0){
				for(ResumenProgramaMixtoVO res : resumen){
					if(res.getTotalS21()>0){
						tS21 += res.getTotalS21();
						s21=true;
					}
					if(res.getTotalS22()>0){
						tS22 += res.getTotalS22();
						s22=true;
					}
					if(res.getTotalS24()>0){
						tS24 += res.getTotalS24();
						s24=true;
					}
					if(res.getTotalS29()>0){
						tS29 += res.getTotalS29();
						s29=true;
					}
				}
			}

			StringBuilder resumenAsignacion = new StringBuilder();
			if(s21){
				resumenAsignacion.append("\\$").append(String.format("%, d",tS21)).append(" al subtitulo 21");
			}
			if(s22){
				resumenAsignacion.append(" \\$").append(String.format("%, d",tS22)).append(" al subtitulo 22");
			}
			if(s24){
				resumenAsignacion.append(" \\$").append(String.format("%, d",tS24)).append(" al subtitulo 24");
			}
			if(s29){
				resumenAsignacion.append(" \\$").append(String.format("%, d",tS29)).append(" al subtitulo 29");
			}
			System.out.println(resumenAsignacion.toString());
			ReferenciaDocumentoSummaryVO referenciaResolucionProgramasAPSVO = documentService
					.getDocumentByPlantillaId(plantillaBorradorResolucionProgramaReforzamiento);
			DocumentoVO documentoBorradorResolucionProgramasAPSVO = documentService
					.getDocument(referenciaResolucionProgramasAPSVO.getId());
			String templateResolucionProgramasAPS = tmpDirDoc
					+ File.separator
					+ documentoBorradorResolucionProgramasAPSVO.getName();
			templateResolucionProgramasAPS = templateResolucionProgramasAPS.replace(" ", "");

			String filenameBorradorResolucionProgramasAPS = tmpDirDoc
					+ File.separator + new Date().getTime() + "_"
					+ "ResolucionModificatoriaPrograma" + programaVO.getNombre().replace(":", "-")+".docx";
			filenameBorradorResolucionProgramasAPS = filenameBorradorResolucionProgramasAPS.replaceAll(" ", "");
			System.out.println("filenameBorradorResolucionModificatoriaProgramasAPS filename-->"
					+ filenameBorradorResolucionProgramasAPS);
			System.out.println("templateResolucionModificatoriaProgramasAPS template-->"
					+ templateResolucionProgramasAPS);
			GeneradorWord generadorWordPlantillaBorradorOrdinarioProgramacionCaja = new GeneradorWord(
					templateResolucionProgramasAPS);
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contentType = mimemap
					.getContentType(templateResolucionProgramasAPS
							.toLowerCase());
			System.out.println("contenTypeBorradorAporteEstatal->"
					+ contentType);

			generadorWordPlantillaBorradorOrdinarioProgramacionCaja.saveContent(documentoBorradorResolucionProgramasAPSVO
					.getContent(), XWPFDocument.class);


			StringBuilder serviciosAfectados = new StringBuilder();

			for(int i = 0; i < listaServicios.size();i++){
				ServicioSalud servicio = servicioSaludDAO.getById(listaServicios.get(i));
				if(i==0){
					serviciosAfectados.append(servicio.getNombre());
				}else{
					serviciosAfectados.append(", ").append(servicio.getNombre());
				}
			}


			Map<String, Object> parametersBorradorAporteEstatal = new HashMap<String, Object>();
			parametersBorradorAporteEstatal.put("{programa}", programaVO.getNombre().replace(":", "\\:"));
			parametersBorradorAporteEstatal.put("{totalActual}",totalActual);
			parametersBorradorAporteEstatal.put("{ano}", (ano - 1));
			parametersBorradorAporteEstatal.put("{anoSiguiente}", ano);
			parametersBorradorAporteEstatal.put("{serviciosAfectados}",serviciosAfectados.toString());
			parametersBorradorAporteEstatal.put("{totalesModificados}",resumenAsignacion.toString());

			GeneradorResolucionAporteEstatal generadorWordDecretoBorradorAporteEstatal = new GeneradorResolucionAporteEstatal(
					filenameBorradorResolucionProgramasAPS,
					templateResolucionProgramasAPS);
			generadorWordDecretoBorradorAporteEstatal.replaceValues(parametersBorradorAporteEstatal, XWPFDocument.class);

			BodyVO response = alfrescoService.uploadDocument(new File(filenameBorradorResolucionProgramasAPS), contentType, folderRecursosFinancierosAPS.replace("{ANO}", ano.toString()));
			System.out.println("response responseBorradorAporteEstatal --->" + response);
			ProgramasReforzamiento programasReforzamiento = recursosFinancierosProgramasReforzamientoDAO.findByIdInstancia(idProceso);
			//reateDocumentProgramasReforzamiento(ProgramasReforzamiento distribucionRecursos, TipoDocumentosProcesos tipoDocumentoProceso,
			//		String nodeRef, String filename, String contenType, Integer idProgramaAno, Boolean lastVersion) 
			plantillaBorradorResolucionProgramaReforzamiento = documentService.createDocumentProgramasReforzamiento(programasReforzamiento, TipoDocumentosProcesos.MODIFICACIONRESOLUCIONPROGRAMASAPS,
					response.getNodeRef(), response.getFileName(), contentType, programaVO.getIdProgramaAno(), null);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException ex){
			ex.printStackTrace();
		} catch (Docx4JException de){
			de.printStackTrace();
		}
		return plantillaBorradorResolucionProgramaReforzamiento;
	}

	public Integer elaborarOrdinarioProgramaReforzamiento(Integer idProceso, Integer idPrograma, List<ResumenProgramaMixtoVO> resumen, Integer ano) {

		ProgramaVO programa = programaService.getProgramaByIdProgramaAndAno(idPrograma, ano);
		Integer plantillaBorradorOrdinarioProgramaReforzamiento = documentService.getPlantillaByType(TipoDocumentosProcesos.ORDINARIOPROGRAMASAPS);

		if (plantillaBorradorOrdinarioProgramaReforzamiento == null) {
			throw new RuntimeException("No se puede crear Borrador Ordinario APS, la plantilla no esta cargada");
		}

		try {
			ReferenciaDocumentoSummaryVO referenciaOrdinarioProgramasAPSVO = documentService.getDocumentByPlantillaId(plantillaBorradorOrdinarioProgramaReforzamiento);
			DocumentoVO documentoBorradorOrdinarioProgramasAPSVO = documentService.getDocument(referenciaOrdinarioProgramasAPSVO.getId());
			String templateOrdinarioProgramasAPS = tmpDirDoc + File.separator + documentoBorradorOrdinarioProgramasAPSVO.getName();
			templateOrdinarioProgramasAPS = templateOrdinarioProgramasAPS.replace(" ", "");
			String filenameBorradorOrdinarioProgramasAPS =  "OrdinarioPrograma"+programa.getNombre(); 
			filenameBorradorOrdinarioProgramasAPS = filenameBorradorOrdinarioProgramasAPS.replace(":", "-").replace(".", "").replace("/", "");
			filenameBorradorOrdinarioProgramasAPS += ".docx";
			filenameBorradorOrdinarioProgramasAPS = StringUtil.removeSpanishAccents(filenameBorradorOrdinarioProgramasAPS);
			filenameBorradorOrdinarioProgramasAPS = tmpDirDoc + File.separator + new Date().getTime() + "_" + filenameBorradorOrdinarioProgramasAPS;
			System.out.println("filenameBorradorOrdinarioProgramasAPS filename-->" + filenameBorradorOrdinarioProgramasAPS);
			System.out.println("templateOrdinarioProgramasAPS template-->"	+ templateOrdinarioProgramasAPS);
			GeneradorWord generadorWordPlantillaBorradorOrdinarioProgramacionCaja = new GeneradorWord(templateOrdinarioProgramasAPS);
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contentType = mimemap.getContentType(templateOrdinarioProgramasAPS.toLowerCase());
			System.out.println("contenTypeBorradorAporteEstatal->" + contentType);

			generadorWordPlantillaBorradorOrdinarioProgramacionCaja.saveContent(documentoBorradorOrdinarioProgramasAPSVO.getContent(), XWPFDocument.class);

			Map<String, Object> parametersBorradorAporteEstatal = new HashMap<String, Object>();
			parametersBorradorAporteEstatal.put("{nombrePrograma}", programa.getNombre().replace(":", "\\:"));
			parametersBorradorAporteEstatal.put("{anoPrograma}", ano.toString());

			GeneradorResolucionAporteEstatal generadorWordDecretoBorradorAporteEstatal = new GeneradorResolucionAporteEstatal(filenameBorradorOrdinarioProgramasAPS, templateOrdinarioProgramasAPS);
			generadorWordDecretoBorradorAporteEstatal.replaceValues(parametersBorradorAporteEstatal, XWPFDocument.class);

			BodyVO response = alfrescoService.uploadDocument(new File(filenameBorradorOrdinarioProgramasAPS), contentType, folderRecursosFinancierosAPS.replace("{ANO}", ano.toString()));
			System.out.println("response responseBorradorAporteEstatal --->" + response);
			ProgramasReforzamiento programasReforzamiento = recursosFinancierosProgramasReforzamientoDAO.findByIdInstancia(idProceso);
			plantillaBorradorOrdinarioProgramaReforzamiento = documentService.createDocumentProgramasReforzamiento(programasReforzamiento, TipoDocumentosProcesos.ORDINARIOPROGRAMASAPS, response.getNodeRef(),
							response.getFileName(), contentType, programa.getIdProgramaAno(), null);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException ex){
			ex.printStackTrace();
		} catch (Docx4JException de){
			de.printStackTrace();
		}

		return plantillaBorradorOrdinarioProgramaReforzamiento;



	}

	public Integer elaborarOrdinarioModificacionProgramaReforzamiento(Integer idProceso, Integer idPrograma, List<ResumenProgramaMixtoVO> resumen, List<Integer> listaServ, Integer ano) {

		ProgramaVO programa = programaService.getProgramaByIdProgramaAndAno(idPrograma, ano);
		Integer plantillaBorradorOrdinarioProgramaReforzamiento = documentService.getPlantillaByType(TipoDocumentosProcesos.MODIFICACIONORDINARIOPROGRAMASAPS);

		if (plantillaBorradorOrdinarioProgramaReforzamiento == null) {
			throw new RuntimeException("No se puede crear Borrador Decreto Aporte Estatal, la plantilla no esta cargada");
		}

		try {

			Long totalActual=0l;
			Integer idProgramaAno = programasDAO.getIdProgramaAnoAnterior(idPrograma, ano);
			List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
			Map<Integer, List<Integer>> componentesBySubtitulos = new HashMap<Integer, List<Integer>>();
			for(ComponentesVO componente: programa.getComponentes()){
				for(SubtituloVO subtitulo : componente.getSubtitulos()){
					if(componentesBySubtitulos.get(subtitulo.getId()) == null){
						List<Integer> componentes = new ArrayList<Integer>();
						componentes.add(componente.getId());
						componentesBySubtitulos.put(subtitulo.getId(), componentes);
					}else{
						componentesBySubtitulos.get(subtitulo.getId()).add(componente.getId());
					}
				}
			}

			for(ServicioSalud servicio : servicios){
				for(ComponentesVO componente : programa.getComponentes()){
					for(SubtituloVO subtitulo : componente.getSubtitulos()){
						ProgramaServicioCoreComponente coreComponente = programasDAO.
								getProgramaServicioCoreComponenteByEstablecimientoProgramaAnoComponenteSubtitulo(
										servicio.getId(), idProgramaAno, componente.getId(), subtitulo.getId());
						if(coreComponente.getTarifaAnterior()!=null){
							totalActual+=coreComponente.getTarifaAnterior();
						}else{
							totalActual+=coreComponente.getTarifa();
						}
					}
				}
			}

			ReferenciaDocumentoSummaryVO referenciaOrdinarioProgramasAPSVO = documentService
					.getDocumentByPlantillaId(plantillaBorradorOrdinarioProgramaReforzamiento);
			DocumentoVO documentoBorradorOrdinarioProgramasAPSVO = documentService
					.getDocument(referenciaOrdinarioProgramasAPSVO.getId());
			String templateOrdinarioProgramasAPS = tmpDirDoc
					+ File.separator
					+ documentoBorradorOrdinarioProgramasAPSVO.getName();
			templateOrdinarioProgramasAPS = templateOrdinarioProgramasAPS.replace(" ", "");

			String filenameBorradorOrdinarioProgramasAPS = tmpDirDoc
					+ File.separator + new Date().getTime() + "_"
					+ "OrdinarioModificacionPrograma"+programa.getNombre().replace(":", "-")+".docx";
			filenameBorradorOrdinarioProgramasAPS = filenameBorradorOrdinarioProgramasAPS.replaceAll(" ", "");
			System.out.println("filenameBorradorOrdinarioModificacionProgramasAPS filename-->"
					+ filenameBorradorOrdinarioProgramasAPS);
			System.out.println("templateOrdinarioModificacionProgramasAPS template-->"
					+ templateOrdinarioProgramasAPS);
			GeneradorWord generadorWordPlantillaBorradorOrdinarioProgramacionCaja = new GeneradorWord(
					templateOrdinarioProgramasAPS);
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contentType = mimemap
					.getContentType(templateOrdinarioProgramasAPS
							.toLowerCase());
			System.out.println("contenTypeBorradorAporteEstatal->"
					+ contentType);

			generadorWordPlantillaBorradorOrdinarioProgramacionCaja.saveContent(documentoBorradorOrdinarioProgramasAPSVO
					.getContent(), XWPFDocument.class);


			StringBuilder serviciosAfectados = new StringBuilder();

			for(int i = 0; i < listaServ.size();i++){
				ServicioSalud servicio = servicioSaludDAO.getById(listaServ.get(i));
				if(i==0){
					serviciosAfectados.append(servicio.getNombre());
				}else{
					serviciosAfectados.append(", ").append(servicio.getNombre());
				}
			}

			Map<String, Object> parametersBorradorAporteEstatal = new HashMap<String, Object>();
			parametersBorradorAporteEstatal.put("{programa}",programa.getNombre().replace(":", "\\:"));
			parametersBorradorAporteEstatal.put("{ano}", ano);

			GeneradorResolucionAporteEstatal generadorWordDecretoBorradorAporteEstatal = new GeneradorResolucionAporteEstatal(
					filenameBorradorOrdinarioProgramasAPS,
					templateOrdinarioProgramasAPS);
			generadorWordDecretoBorradorAporteEstatal.replaceValues(parametersBorradorAporteEstatal, XWPFDocument.class);

			BodyVO response = alfrescoService.uploadDocument(new File(filenameBorradorOrdinarioProgramasAPS), contentType, folderRecursosFinancierosAPS.replace("{ANO}", ano.toString()));
			System.out.println("response responseBorradorAporteEstatal --->" + response);
			ProgramasReforzamiento programasReforzamiento = recursosFinancierosProgramasReforzamientoDAO.findByIdInstancia(idProceso);
			plantillaBorradorOrdinarioProgramaReforzamiento = documentService.createDocumentProgramasReforzamiento(programasReforzamiento, TipoDocumentosProcesos.MODIFICACIONORDINARIOPROGRAMASAPS,
					response.getNodeRef(), response.getFileName(), contentType, programa.getIdProgramaAno(), null);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException ex){
			ex.printStackTrace();
		} catch (Docx4JException de){
			de.printStackTrace();
		}

		return plantillaBorradorOrdinarioProgramaReforzamiento;



	}

	public void elaborarExcelResolucion(Integer idProceso, Integer idPrograma, List<ResumenProgramaMixtoVO> resumen, TipoDocumentosProcesos tipoDocumentoProceso, Integer ano) {
		ProgramaVO programa = programaService.getProgramaByIdProgramaAndAno(idPrograma, ano);
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		List<CellExcelVO> header = new ArrayList<CellExcelVO>();
		header.add(new CellExcelVO("COD."));	
		header.add(new CellExcelVO("SERVICIO DE SALUD"));
		if(resumen.get(0).isSub24()){
			header.add(new CellExcelVO("SUBTITULO 24 ($)"));
		}
		if(resumen.get(0).isSub21()){
			header.add(new CellExcelVO("SUBTITULO 21 ($)"));
		}
		if(resumen.get(0).isSub22()){
			header.add(new CellExcelVO("SUBTITULO 22 ($)"));
		}
		if(resumen.get(0).isSub29()){
			header.add(new CellExcelVO("SUBTITULO 29 ($)"));
		}
		header.add(new CellExcelVO("TOTAL ($)"));
		String filename = tmpDir + File.separator;
		filename += "Plantilla Resolucion Programa -" + programa.getNombre().replace(":", "-")+".xlsx";
		filename = StringUtil.removeSpanishAccents(filename);
		GeneradorExcel generadorExcel = new GeneradorExcel(filename);
		String contenType = mimemap.getContentType(filename.toLowerCase());

		List<String> cabezas = new ArrayList<String>();
		for(CellExcelVO head : header){
			cabezas.add(head.getName());
		}

		ProgramaAPSResolucionSheetExcel programaAPSResolucionSheetExcel = new ProgramaAPSResolucionSheetExcel(cabezas,header, resumen, resumen.size(), header.size());
		generadorExcel.addSheet(programaAPSResolucionSheetExcel, "Hoja 1");

		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderRecursosFinancierosAPS.replace("{ANO}", ano.toString()));
			ProgramasReforzamiento programasReforzamiento = recursosFinancierosProgramasReforzamientoDAO.findByIdInstancia(idProceso);
			documentService.createDocumentProgramasReforzamiento(programasReforzamiento, tipoDocumentoProceso, response.getNodeRef(), response.getFileName(), contenType,
					programa.getIdProgramaAno(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void elaborarExcelResolucionModificado(Integer idProceso, Integer idPrograma, List<ResumenProgramaMixtoVO> resumen, TipoDocumentosProcesos tipoDocumentoProceso, Integer ano) {
		ProgramaVO programaVO =  programaService.getProgramaByIdProgramaAndAno(idPrograma, ano);
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();

		List<CellExcelVO> header = new ArrayList<CellExcelVO>();
		header.add(new CellExcelVO("COD."));	
		header.add(new CellExcelVO("SERVICIO DE SALUD"));
		boolean sub24 = false;
		boolean sub21 = false;
		boolean sub22 = false;
		boolean sub29 = false;

		for(int i=0; i< resumen.size(); i++){
			if(resumen.get(i).getTotalS24() > 0){
				sub24 = true;
			}
			if(resumen.get(i).getTotalS21() > 0){
				sub21 = true;
			}
			if(resumen.get(i).getTotalS22() > 0){
				sub22 = true;
			}
			if(resumen.get(i).getTotalS29() > 0){
				sub29 = true;
			}
		}

		if(sub24){
			header.add(new CellExcelVO("SUBTITULO 24 ($)"));
		}

		if(sub21){
			header.add(new CellExcelVO("SUBTITULO 21 ($)"));
		}

		if(sub22){
			header.add(new CellExcelVO("SUBTITULO 22 ($)"));
		}

		if(sub29){
			header.add(new CellExcelVO("SUBTITULO 29 ($)"));
		}

		header.add(new CellExcelVO("TOTAL ($)"));

		String filename = tmpDir + File.separator;
		filename += "Plantilla Resolucion Programa -" + programaVO.getNombre().replace(":", "-")+".xlsx";
		filename = StringUtil.removeSpanishAccents(filename);
		GeneradorExcel generadorExcel = new GeneradorExcel(filename);
		String contenType = mimemap.getContentType(filename.toLowerCase());

		List<String> cabezas = new ArrayList<String>();
		for(CellExcelVO head : header){
			String h = head.getName();
			cabezas.add(h);
		}
		ProgramaAPSResolucionSheetExcel programaAPSResolucionSheetExcel = new ProgramaAPSResolucionSheetExcel(cabezas,header, resumen, resumen.size(), header.size());
		generadorExcel.addSheet(programaAPSResolucionSheetExcel, "Hoja 1");

		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderRecursosFinancierosAPS.replace("{ANO}", ano.toString()));
			ProgramasReforzamiento programasReforzamiento = recursosFinancierosProgramasReforzamientoDAO.findByIdInstancia(idProceso);
			documentService.createDocumentProgramasReforzamiento(programasReforzamiento, tipoDocumentoProceso, response.getNodeRef(), response.getFileName(), contenType,
					programaVO.getIdProgramaAno(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void elaborarExcelOrdinario(Integer idProceso, Integer idPrograma, List<ResumenProgramaMixtoVO> resumen, TipoDocumentosProcesos tipoDocumentoProceso, Integer ano) {

		ProgramaVO programa = programaService.getProgramaByIdProgramaAndAno(idPrograma, ano);

		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();

		List<CellExcelVO> header = new ArrayList<CellExcelVO>();
		header.add(new CellExcelVO("COD."));	
		header.add(new CellExcelVO("SERVICIO DE SALUD"));

		if(resumen.get(0).isSub24()){
			header.add(new CellExcelVO("SUBTITULO 24 ($)"));
		}

		if(resumen.get(0).isSub21()){
			header.add(new CellExcelVO("SUBTITULO 21 ($)"));
		}

		if(resumen.get(0).isSub22()){
			header.add(new CellExcelVO("SUBTITULO 22 ($)"));
		}

		if(resumen.get(0).isSub29()){
			header.add(new CellExcelVO("SUBTITULO 29 ($)"));
		}

		header.add(new CellExcelVO("TOTAL ($)"));

		String filename =  "Plantilla Ordinario Programa -" + programa.getNombre(); 
		filename = filename.replace(":", "-").replace(".", "").replace("/", "");
		filename += ".xlsx";
		filename = StringUtil.removeSpanishAccents(filename);
		filename = tmpDir + File.separator + "_" + filename;
		
		GeneradorExcel generadorExcel = new GeneradorExcel(filename);
		String contenType = mimemap.getContentType(filename.toLowerCase());

		List<String> cabezas = new ArrayList<String>();
		for(CellExcelVO head : header){
			String h = head.getName();
			cabezas.add(h);
		}

		ProgramaAPSResolucionSheetExcel programaAPSResolucionSheetExcel = new ProgramaAPSResolucionSheetExcel(cabezas,header, resumen, resumen.size(), header.size());
		generadorExcel.addSheet(programaAPSResolucionSheetExcel, "Hoja 1");

		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderRecursosFinancierosAPS.replace("{ANO}", ano.toString()));
			ProgramasReforzamiento programasReforzamiento = recursosFinancierosProgramasReforzamientoDAO.findByIdInstancia(idProceso);
			documentService.createDocumentProgramasReforzamiento(programasReforzamiento, tipoDocumentoProceso, response.getNodeRef(), response.getFileName(), contenType, 
					programa.getIdProgramaAno(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void elaborarExcelOrdinarioModificado(Integer idProceso, Integer idPrograma, List<ResumenProgramaMixtoVO> resumen, TipoDocumentosProcesos tipoDocumentoProceso, Integer ano) {
		ProgramaVO programaEvaluacion = programaService.getProgramaByIdProgramaAndAno(idPrograma, ano);

		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();

		List<CellExcelVO> header = new ArrayList<CellExcelVO>();
		header.add(new CellExcelVO("COD."));	
		header.add(new CellExcelVO("SERVICIO DE SALUD"));

		boolean sub24 = false;
		boolean sub21 = false;
		boolean sub22 = false;
		boolean sub29 = false;

		for(int i=0; i<resumen.size();i++){
			if(resumen.get(i).getTotalS24()>0){
				sub24=true;
			}
			if(resumen.get(i).getTotalS21()>0){
				sub21=true;
			}
			if(resumen.get(i).getTotalS22()>0){
				sub22=true;
			}
			if(resumen.get(i).getTotalS29()>0){
				sub29=true;
			}
		}

		if(sub24){
			header.add(new CellExcelVO("SUBTITULO 24 ($)"));
		}

		if(sub21){
			header.add(new CellExcelVO("SUBTITULO 21 ($)"));
		}

		if(sub22){
			header.add(new CellExcelVO("SUBTITULO 22 ($)"));
		}

		if(sub29){
			header.add(new CellExcelVO("SUBTITULO 29 ($)"));
		}

		header.add(new CellExcelVO("TOTAL ($)"));

		String filename = tmpDir + File.separator;
		filename += "Plantilla Ordinario Modificacion Programa -"+programaEvaluacion.getNombre().replace(":", "-")+".xlsx";
		filename = StringUtil.removeSpanishAccents(filename);
		GeneradorExcel generadorExcel = new GeneradorExcel(filename);
		String contenType = mimemap.getContentType(filename.toLowerCase());

		List<String> cabezas = new ArrayList<String>();
		for(CellExcelVO head : header){
			String h = head.getName();
			cabezas.add(h);
		}

		ProgramaAPSResolucionSheetExcel programaAPSResolucionSheetExcel = new ProgramaAPSResolucionSheetExcel(cabezas,header, resumen, resumen.size(), header.size());
		generadorExcel.addSheet(programaAPSResolucionSheetExcel, "Hoja 1");

		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderRecursosFinancierosAPS.replace("{ANO}", ano.toString()));
			ProgramasReforzamiento programasReforzamiento = recursosFinancierosProgramasReforzamientoDAO.findByIdInstancia(idProceso);
			documentService.createDocumentProgramasReforzamiento(programasReforzamiento, tipoDocumentoProceso, response.getNodeRef(), response.getFileName(), contenType,
					programaEvaluacion.getIdProgramaAno(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Integer getIdResolucion(Integer programaSeleccionado, TipoDocumentosProcesos tipoDocumentoProceso) {
		System.out.println("BUSCANDO ID PARA: "+ tipoDocumentoProceso.getName());
		ReferenciaDocumentoSummaryVO referencia = documentService.getLastDocumentoSummaryByResolucionAPSType(programaSeleccionado, tipoDocumentoProceso);
		if(referencia != null){
			return referencia.getId();
		}
		return null;
	}

	public Integer createSeguimientoProgramaReforzamiento(Integer idProceso, Integer idProgramaAno, TareasSeguimiento tareaSeguimiento, String subject, String body, String username, List<String> para,
			List<String> conCopia, List<String> conCopiaOculta, List<Integer> documentos, Integer ano) {

		String from = usuarioDAO.getEmailByUsername(username);
		if(from == null){
			throw new RuntimeException("Usuario no tiene un email valido");
		}
		List<ReferenciaDocumentoSummaryVO> documentosTmp = new ArrayList<ReferenciaDocumentoSummaryVO>();
		if(documentos != null && documentos.size() > 0){
			for(Integer referenciaDocumentoId : documentos){
				MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
				ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO = documentService.getDocumentSummary(referenciaDocumentoId);
				documentosTmp.add(referenciaDocumentoSummaryVO);
				String contenType= mimemap.getContentType(referenciaDocumentoSummaryVO.getPath().toLowerCase());
				BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummaryVO.getPath()), contenType, folderRecursosFinancierosAPS.replace("{ANO}", ano.toString()));
				System.out.println("response upload template --->"+response);
				documentService.updateDocumentTemplate(referenciaDocumentoId, response.getNodeRef(), response.getFileName(), contenType);
			}
		}
		Integer idSeguimiento = seguimientoService.createSeguimiento(tareaSeguimiento, subject, body, from, para, conCopia, conCopiaOculta, documentosTmp);
		Seguimiento seguimiento = seguimientoDAO.getSeguimientoById(idSeguimiento);
		ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idProgramaAno);
		ProgramasReforzamiento programasReforzamiento = recursosFinancierosProgramasReforzamientoDAO.findByIdInstancia(idProceso);
		return recursosFinancierosProgramasReforzamientoDAO.createSeguimiento(programasReforzamiento, programaAno, seguimiento);

	}

	public List<SeguimientoVO> getBitacora(Integer idProceso, Integer idPrograma, TareasSeguimiento tareaSeguimiento) {
		List<SeguimientoVO> bitacora = new ArrayList<SeguimientoVO>();
		List<Seguimiento> bitacoraSeguimiento = seguimientoDAO.getBitacoraProgramasReforzamiento(idProceso, idPrograma, tareaSeguimiento);
		if(bitacoraSeguimiento != null && bitacoraSeguimiento.size() > 0){
			for(Seguimiento seguimiento : bitacoraSeguimiento){
				bitacora.add(new SeguimientoMapper().getBasic(seguimiento));
			}
		}
		return bitacora;
	}

	@Asynchronous
	public void recursosFinancierosProgramasReforzamientoService(Integer idPrograma, Boolean tipoProgramaPxQ, Integer idProceso, Integer ano) {
		ProgramaVO programaEvaluacion = programaService.getProgramaByIdProgramaAndAno(idPrograma, ano);
		ReferenciaDocumento referenciaDocumento = null;
		Integer idReferenciaDetalleMunicipal = null;
		Integer idReferenciaDetalleServicio = null;
		List<Integer> idComponentes = new ArrayList<Integer>();
		for(ComponentesVO componente : programaEvaluacion.getComponentes()){
			for(SubtituloVO subtitulo : componente.getSubtitulos()){
				if(subtitulo.getId() != 3){
					if(idComponentes.indexOf(componente.getId()) == -1){
						idComponentes.add(componente.getId());
					}
				}
			}
		}
		String subject = "";
		Integer idPlantillaCorreo = null;
		if(programaEvaluacion.getDependenciaMunicipal()){
			idPlantillaCorreo = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLARESOLUCIONCORREO);
			Integer idReferenciaDocumento = getLastDocumentRecursosFinancierosByType(idProceso, programaEvaluacion.getIdProgramaAno(), TipoDocumentosProcesos.RESOLUCIONPROGRAMASAPS);
			referenciaDocumento = documentoDAO.findById(idReferenciaDocumento);
			subject = "Resolución de Distribución de Recursos para el Programa " + programaEvaluacion.getNombre();
		}
		if(programaEvaluacion.getDependenciaServicio() && !programaEvaluacion.getDependenciaMunicipal()){
			idPlantillaCorreo = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLAORDINARIOCORREO);
			Integer idReferenciaDocumento = getLastDocumentRecursosFinancierosByType(idProceso, programaEvaluacion.getIdProgramaAno(), TipoDocumentosProcesos.ORDINARIOPROGRAMASAPS);
			referenciaDocumento = documentoDAO.findById(idReferenciaDocumento);
			subject = "Ordinario de Distribución de Recursos para el Programa " + programaEvaluacion.getNombre();
		}

		ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryPlantillaCorreoVO = documentService.getDocumentByPlantillaId(idPlantillaCorreo);
		DocumentoVO documentoPlantillaCorreoVO = documentService.getDocument(referenciaDocumentoSummaryPlantillaCorreoVO.getId());
		String templatePlantillaCorreo = tmpDirDoc + File.separator + documentoPlantillaCorreoVO.getName();
		templatePlantillaCorreo = templatePlantillaCorreo.replace(" ", "");

		System.out.println("templatePlantillaCorreo template-->"+templatePlantillaCorreo);
		GeneradorXML generadorXMLPlantillaResolucionRebaja = new GeneradorXML(templatePlantillaCorreo);
		Email emailPLantilla = null;
		try {
			emailPLantilla = generadorXMLPlantillaResolucionRebaja.createObject(Email.class, documentoPlantillaCorreoVO.getContent());
		} catch (JAXBException e1) {
			e1.printStackTrace();
		}

		if(tipoProgramaPxQ){
			if(programaEvaluacion.getDependenciaMunicipal() && !programaEvaluacion.getDependenciaServicio()){
				idReferenciaDetalleMunicipal = getIdPlantillaProgramasDetalles(programaEvaluacion.getId(), TipoDocumentosProcesos.PLANTILLAAPSDETALLEMUNICIPAL, ano);
			}
			if(!programaEvaluacion.getDependenciaMunicipal() && programaEvaluacion.getDependenciaServicio()){
				idReferenciaDetalleServicio = getIdPlantillaProgramasDetalles(programaEvaluacion.getId(), TipoDocumentosProcesos.PLANTILLAAPSDETALLESERVICIO, ano);
			}
			if(programaEvaluacion.getDependenciaMunicipal() && programaEvaluacion.getDependenciaServicio()){
				idReferenciaDetalleMunicipal = getIdPlantillaProgramasDetalles(programaEvaluacion.getId(), TipoDocumentosProcesos.PLANTILLAAPSDETALLEMUNICIPAL, ano);
				idReferenciaDetalleServicio = getIdPlantillaProgramasDetalles(programaEvaluacion.getId(), TipoDocumentosProcesos.PLANTILLAAPSDETALLESERVICIO, ano);
			}
		}else{
			if(programaEvaluacion.getDependenciaMunicipal() && !programaEvaluacion.getDependenciaServicio()){
				idReferenciaDetalleMunicipal = getIdPlantillaProgramasDetalles(programaEvaluacion.getId(), TipoDocumentosProcesos.PLANTILLAAPSDETALLEMUNICIPALHISTORICO, ano);
			}
			if(!programaEvaluacion.getDependenciaMunicipal() && programaEvaluacion.getDependenciaServicio()){
				idReferenciaDetalleServicio  = getIdPlantillaProgramasDetalles(programaEvaluacion.getId(), TipoDocumentosProcesos.PLANTILLAAPSDETALLESERVICIOHISTORICO, ano);
			}
			if(programaEvaluacion.getDependenciaMunicipal() && programaEvaluacion.getDependenciaServicio()){
				idReferenciaDetalleMunicipal = getIdPlantillaProgramasDetalles(programaEvaluacion.getId(), TipoDocumentosProcesos.PLANTILLAAPSDETALLEMUNICIPALHISTORICO, ano);
				idReferenciaDetalleServicio = getIdPlantillaProgramasDetalles(programaEvaluacion.getId(), TipoDocumentosProcesos.PLANTILLAAPSDETALLESERVICIOHISTORICO, ano);
			}
		}

		try{
			ProgramaAno programaAnoEvaluacion = programasDAO.getProgramaAnoByID(programaEvaluacion.getIdProgramaAno());
			ProgramasReforzamiento distribucionRecursos = recursosFinancierosProgramasReforzamientoDAO.findByIdInstancia(idProceso);
			List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
			for (ServicioSalud servicioSalud : servicios) {
				if(servicioSalud != null && servicioSalud.getDirector() != null && servicioSalud.getDirector().getEmail() != null){
					List<EmailService.Adjunto> adjuntos = new ArrayList<EmailService.Adjunto>();

					DocumentoProgramasReforzamiento documentoProgramasReforzamiento = new DocumentoProgramasReforzamiento();
					documentoProgramasReforzamiento.setIdProgramaAno(programaAnoEvaluacion);
					documentoProgramasReforzamiento.setIdTipoDocumento(new TipoDocumento(TipoDocumentosProcesos.ADJUNTOCORREO.getId()));
					documentoProgramasReforzamiento.setIdDocumento(referenciaDocumento);
					documentoProgramasReforzamiento.setIdServicio(servicioSalud);
					documentoProgramasReforzamiento.setDistribucionRecursos(distribucionRecursos);
					recursosFinancierosProgramasReforzamientoDAO.save(documentoProgramasReforzamiento);

					if(idReferenciaDetalleMunicipal != null && tipoProgramaPxQ){
						Integer idDetalleMunicipal = construirExcelDetalleMunicipal(idProceso, idReferenciaDetalleMunicipal, servicioSalud.getId(), programaEvaluacion.getIdProgramaAno(), programaEvaluacion.getComponentes(), servicioSalud.getNombre(), ano);
						DocumentoVO documentDocumentoResolucionVO = documentService.getDocument(idDetalleMunicipal);
						String fileNameDetalleMunicipal = tmpDirDoc + File.separator + documentDocumentoResolucionVO.getName();
						GeneradorDocumento generadorDocumento = new GeneradorDocumento(fileNameDetalleMunicipal);
						generadorDocumento.saveContent(documentDocumentoResolucionVO.getContent());
						EmailService.Adjunto adjunto = new EmailService.Adjunto();
						adjunto.setDescripcion("Detalle Municipal Programa");
						adjunto.setName("Detalle Programa " + programaEvaluacion.getNombre().replace(":", "-") + " servicio "+servicioSalud.getNombre() + " - Municipal.xlsx");
						adjunto.setUrl((new File(fileNameDetalleMunicipal)).toURI().toURL());
						adjuntos.add(adjunto);
					}
					if(idReferenciaDetalleServicio != null && tipoProgramaPxQ){
						Integer idDetalleServicio = construirExcelDetalleServicio(idProceso, idReferenciaDetalleServicio, servicioSalud.getId(), programaEvaluacion.getIdProgramaAno(), servicioSalud.getNombre(), idComponentes, ano);
						DocumentoVO documentDocumentoResolucionVO = documentService.getDocument(idDetalleServicio);
						String fileNameDetalleServicio = tmpDirDoc + File.separator + documentDocumentoResolucionVO.getName();
						GeneradorDocumento generadorDocumento = new GeneradorDocumento(fileNameDetalleServicio);
						generadorDocumento.saveContent(documentDocumentoResolucionVO.getContent());
						EmailService.Adjunto adjunto = new EmailService.Adjunto();
						adjunto.setDescripcion("Detalle Servicio Programa");
						adjunto.setName("Detalle Programa " + programaEvaluacion.getNombre().replace(":", "-").replace("Ñ", "N").replace("ñ", "n").replace("?", "N")+" servicio "+servicioSalud.getNombre()+" - Servicio.xlsx");
						adjunto.setUrl((new File(fileNameDetalleServicio)).toURI().toURL());
						adjuntos.add(adjunto);
					}
					if(idReferenciaDetalleMunicipal != null && !tipoProgramaPxQ){
						Integer idDetalleMunicipal = construirExcelDetalleMunicipalHistorico(idProceso, idReferenciaDetalleMunicipal, servicioSalud.getId(), programaEvaluacion.getIdProgramaAno(), programaEvaluacion.getComponentes(), servicioSalud.getNombre(), ano);
						DocumentoVO documentDocumentoResolucionVO = documentService.getDocument(idDetalleMunicipal);
						String fileNameDetalleMunicipalHistorico = tmpDirDoc + File.separator + documentDocumentoResolucionVO.getName();
						GeneradorDocumento generadorDocumento = new GeneradorDocumento(fileNameDetalleMunicipalHistorico);
						generadorDocumento.saveContent(documentDocumentoResolucionVO.getContent());
						EmailService.Adjunto adjunto = new EmailService.Adjunto();
						adjunto.setDescripcion("Detalle Municipal Programa");
						adjunto.setName("Detalle Programa " + programaEvaluacion.getNombre().replace(":", "-")+" servicio "+servicioSalud.getNombre()+" - Municipal.xlsx");
						adjunto.setUrl((new File(fileNameDetalleMunicipalHistorico)).toURI().toURL());
						adjuntos.add(adjunto);
					}
					if(idReferenciaDetalleServicio != null && !tipoProgramaPxQ){
						Integer idDetalleServicio = construirExcelDetalleServicioHistorico(idProceso, idReferenciaDetalleServicio, servicioSalud.getId(), programaEvaluacion.getIdProgramaAno(), servicioSalud.getNombre(), ano);
						DocumentoVO documentDocumentoResolucionVO = documentService.getDocument(idDetalleServicio);
						String fileNameDetalleServicioHistorico = tmpDirDoc + File.separator + documentDocumentoResolucionVO.getName();
						GeneradorDocumento generadorDocumento = new GeneradorDocumento(fileNameDetalleServicioHistorico);
						generadorDocumento.saveContent(documentDocumentoResolucionVO.getContent());
						EmailService.Adjunto adjunto = new EmailService.Adjunto();
						adjunto.setDescripcion("Detalle Servicio Programa");
						adjunto.setName("Detalle Programa " + programaEvaluacion.getNombre().replace(":", "-")+" servicio "+servicioSalud.getNombre()+" - Servicio.xlsx");
						adjunto.setUrl((new File(fileNameDetalleServicioHistorico)).toURI().toURL());
						adjuntos.add(adjunto);
					}

					DocumentoVO documentDocumentoVO = documentService.getDocument(referenciaDocumento.getId());
					String fileNameDocumentoResolucion = tmpDirDoc + File.separator + documentDocumentoVO.getName();
					GeneradorDocumento generadorDocumento = new GeneradorDocumento(fileNameDocumentoResolucion);
					generadorDocumento.saveContent(documentDocumentoVO.getContent());
					EmailService.Adjunto adjunto = new EmailService.Adjunto();
					adjunto.setDescripcion("Documentos Adjuntos");
					adjunto.setName(documentDocumentoVO.getName());
					adjunto.setUrl((new File(fileNameDocumentoResolucion)).toURI().toURL());
					adjuntos.add(adjunto);

					List<String> to = new ArrayList<String>();
					to.add(servicioSalud.getDirector().getEmail().getValor());

					List<String> cc = new ArrayList<String>();
					cc.add(servicioSalud.getEncargadoFinanzasAps().getEmail().getValor());
					cc.add(servicioSalud.getEncargadoAps().getEmail().getValor());

					if(emailPLantilla != null && emailPLantilla.getAsunto() != null && emailPLantilla.getCuerpo() != null){
						emailService.sendMail(to, cc, null, emailPLantilla.getAsunto().replace("{nombrePrograma}", programaEvaluacion.getNombre()), emailPLantilla.getCuerpo().replaceAll("(\r\n|\n)", "<br />").replace("{nombrePrograma}", programaEvaluacion.getNombre()+" "), adjuntos);
					}else{
						emailService.sendMail(to, cc, null, subject, "Estimado " + servicioSalud.getDirector().getNombre() + " " + servicioSalud.getDirector().getApellidoPaterno() + " " + ((servicioSalud.getDirector().getApellidoMaterno() != null) ? servicioSalud.getDirector().getApellidoMaterno() : "") + ": <br /> <p> l</p>", adjuntos);
					}

					ReporteEmailsEnviados reporteEnviados = new ReporteEmailsEnviados();
					reporteEnviados.setIdProgramaAno(programaAnoEvaluacion);
					reporteEnviados.setIdServicio(servicioSalud);
					reporteEnviados.setFecha(new Date());
					reporteEnviados.setModifica(false);
					recursosFinancierosProgramasReforzamientoDAO.save(reporteEnviados);

					List<DocumentoProgramasReforzamiento> documentosAdjuntos =	recursosFinancierosProgramasReforzamientoDAO.getByIdProcesoIdProgramaAnoIdTipoIdServicio(idProceso, programaEvaluacion.getIdProgramaAno(), TipoDocumentosProcesos.ADJUNTOCORREO, servicioSalud.getId());

					for(DocumentoProgramasReforzamiento documentoProgramasAdjunto : documentosAdjuntos){
						ReporteEmailsAdjuntos adjuntoMail = new ReporteEmailsAdjuntos();
						adjuntoMail.setReporteEmailsEnviado(reporteEnviados);
						adjuntoMail.setDocumento(documentoProgramasAdjunto.getIdDocumento());
						recursosFinancierosProgramasReforzamientoDAO.save(adjuntoMail);
					}

					ReporteEmailsDestinatarios destinatarioDirector = new ReporteEmailsDestinatarios();
					destinatarioDirector.setDestinatario(servicioSalud.getDirector());
					destinatarioDirector.setTipoDestinatario(new TipoDestinatario(TiposDestinatarios.PARA.getId()));
					destinatarioDirector.setReporteEmailsEnviado(reporteEnviados);
					recursosFinancierosProgramasReforzamientoDAO.save(destinatarioDirector);

					ReporteEmailsDestinatarios destinatarionEncargadoFinanzas = new ReporteEmailsDestinatarios();
					destinatarionEncargadoFinanzas.setDestinatario(servicioSalud.getEncargadoFinanzasAps());
					destinatarionEncargadoFinanzas.setTipoDestinatario(new TipoDestinatario(TiposDestinatarios.CC.getId()));						
					destinatarionEncargadoFinanzas.setReporteEmailsEnviado(reporteEnviados);
					recursosFinancierosProgramasReforzamientoDAO.save(destinatarionEncargadoFinanzas);

					ReporteEmailsDestinatarios destinatarionEncargadoAPS = new ReporteEmailsDestinatarios();
					destinatarionEncargadoAPS.setDestinatario(servicioSalud.getEncargadoAps());
					destinatarionEncargadoAPS.setTipoDestinatario(new TipoDestinatario(TiposDestinatarios.CC.getId()));						
					destinatarionEncargadoAPS.setReporteEmailsEnviado(reporteEnviados);
					recursosFinancierosProgramasReforzamientoDAO.save(destinatarionEncargadoAPS);

					ReporteEmailsProgramasReforzamiento reporteEmailsProgramasReforzamiento = new ReporteEmailsProgramasReforzamiento();
					reporteEmailsProgramasReforzamiento.setProgramasReforzamiento(distribucionRecursos);
					reporteEmailsProgramasReforzamiento.setReporteEmailsEnviados(reporteEnviados);
					recursosFinancierosProgramasReforzamientoDAO.save(reporteEmailsProgramasReforzamiento);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Asynchronous
	public void recursosFinancierosProgramasReforzamientoModificacionService(Integer idPrograma, Boolean tipoProgramaPxQ, List<Integer> serviciosSalud, Integer idProcesoModificacion, Integer ano) {

		ProgramaVO programaEvaluacion = programaService.getProgramaByIdProgramaAndAno(idPrograma, ano);
		List<ServicioSalud> servicios = servicioSaludDAO.getServicios();
		ReferenciaDocumento referenciaDocumento = null;
		Integer idReferenciaDetalleMunicipal = null;
		Integer idReferenciaDetalleServicio = null;
		String subject = "";
		List<Integer> idComponentes = new ArrayList<Integer>();
		for(ComponentesVO componente : programaEvaluacion.getComponentes()){
			for(SubtituloVO subtitulo : componente.getSubtitulos()){
				if(subtitulo.getId() != 3){
					if(idComponentes.indexOf(componente.getId()) == -1){
						idComponentes.add(componente.getId());
					}
				}
			}
		}

		Integer idPlantillaCorreo = null;
		if(programaEvaluacion.getDependenciaMunicipal()){
			idPlantillaCorreo = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLARESOLUCIONCORREO);
			Integer idReferenciaDocumento = getLastDocumentRecursosFinancierosByType(idProcesoModificacion, programaEvaluacion.getIdProgramaAno(), TipoDocumentosProcesos.MODIFICACIONRESOLUCIONPROGRAMASAPS);
			referenciaDocumento = documentoDAO.findById(idReferenciaDocumento);
			subject = "Resolución de Distribución de Recursos para el Programa " + programaEvaluacion.getNombre();
		}
		if(programaEvaluacion.getDependenciaServicio() && !programaEvaluacion.getDependenciaMunicipal()){
			idPlantillaCorreo = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLAORDINARIOCORREO);
			referenciaDocumento = documentoDAO.getLastDocumentoSummaryByResolucionAPSType(programaEvaluacion.getIdProgramaAno(), TipoDocumentosProcesos.MODIFICACIONRESOLUCIONPROGRAMASAPS);
			subject = "Ordinario de Distribución de Recursos para el Programa"+programaEvaluacion.getNombre();
		}

		ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryPlantillaCorreoVO = documentService.getDocumentByPlantillaId(idPlantillaCorreo);
		DocumentoVO documentoPlantillaCorreoVO = documentService.getDocument(referenciaDocumentoSummaryPlantillaCorreoVO.getId());
		String templatePlantillaCorreo = tmpDirDoc + File.separator + documentoPlantillaCorreoVO.getName();
		templatePlantillaCorreo = templatePlantillaCorreo.replace(" ", "");

		System.out.println("templatePlantillaCorreo template-->"+templatePlantillaCorreo);
		GeneradorXML generadorXMLPlantillaResolucionRebaja = new GeneradorXML(templatePlantillaCorreo);
		Email emailPLantilla = null;
		try {
			emailPLantilla = generadorXMLPlantillaResolucionRebaja.createObject(Email.class, documentoPlantillaCorreoVO.getContent());
		} catch (JAXBException e1) {
			e1.printStackTrace();
		}

		if(tipoProgramaPxQ){
			if(programaEvaluacion.getDependenciaMunicipal() && !programaEvaluacion.getDependenciaServicio()){
				idReferenciaDetalleMunicipal = getIdPlantillaProgramasDetalles(programaEvaluacion.getId(), TipoDocumentosProcesos.PLANTILLAAPSDETALLEMUNICIPAL, ano);
			}
			if(!programaEvaluacion.getDependenciaMunicipal() && programaEvaluacion.getDependenciaServicio()){
				idReferenciaDetalleServicio = getIdPlantillaProgramasDetalles(programaEvaluacion.getId(), TipoDocumentosProcesos.PLANTILLAAPSDETALLESERVICIO, ano);
			}
			if(programaEvaluacion.getDependenciaMunicipal() && programaEvaluacion.getDependenciaServicio()){
				idReferenciaDetalleMunicipal = getIdPlantillaProgramasDetalles(programaEvaluacion.getId(), TipoDocumentosProcesos.PLANTILLAAPSDETALLEMUNICIPAL, ano);
				idReferenciaDetalleServicio = getIdPlantillaProgramasDetalles(programaEvaluacion.getId(), TipoDocumentosProcesos.PLANTILLAAPSDETALLESERVICIO, ano);
			}
		}else{
			if(programaEvaluacion.getDependenciaMunicipal() && !programaEvaluacion.getDependenciaServicio()){
				idReferenciaDetalleMunicipal = getIdPlantillaProgramasDetalles(programaEvaluacion.getId(), TipoDocumentosProcesos.PLANTILLAAPSDETALLEMUNICIPALHISTORICO, ano);
			}
			if(!programaEvaluacion.getDependenciaMunicipal() && programaEvaluacion.getDependenciaServicio()){
				idReferenciaDetalleServicio  = getIdPlantillaProgramasDetalles(programaEvaluacion.getId(), TipoDocumentosProcesos.PLANTILLAAPSDETALLESERVICIOHISTORICO, ano);
			}
			if(programaEvaluacion.getDependenciaMunicipal() && programaEvaluacion.getDependenciaServicio()){
				idReferenciaDetalleMunicipal = getIdPlantillaProgramasDetalles(programaEvaluacion.getId(), TipoDocumentosProcesos.PLANTILLAAPSDETALLEMUNICIPALHISTORICO, ano);
				idReferenciaDetalleServicio  = getIdPlantillaProgramasDetalles(programaEvaluacion.getId(), TipoDocumentosProcesos.PLANTILLAAPSDETALLESERVICIOHISTORICO, ano);
			}
		}

		for(ServicioSalud servicioSalud : servicios) {
			List<DocumentoProgramasReforzamiento> documentosAdjuntos =	recursosFinancierosProgramasReforzamientoDAO.getByIdProcesoIdProgramaAnoIdTipoIdServicio(idProcesoModificacion, programaEvaluacion.getIdProgramaAno(), TipoDocumentosProcesos.ADJUNTOCORREO, servicioSalud.getId());
			if(documentosAdjuntos.size()>0){
				for(DocumentoProgramasReforzamiento docs : documentosAdjuntos){
					System.out.println("ELIMINANDO DOCUMENTO ID REFERENCIA --> "+docs.getIdDocumento().getId());
					recursosFinancierosProgramasReforzamientoDAO.deleteDocumentoProgramaAPS(docs.getIdDocumento().getId());
				}
			}
		}

		try{
			ProgramaAno programaAno = programasDAO.getProgramaAnoByID(programaEvaluacion.getIdProgramaAno());
			ProgramasReforzamiento distribucionRecursos = recursosFinancierosProgramasReforzamientoDAO.findByIdInstancia(idProcesoModificacion);
			for (ServicioSalud servicioSalud : servicios) {
				if(servicioSalud != null && servicioSalud.getDirector() != null && servicioSalud.getDirector().getEmail() != null && serviciosSalud.contains(servicioSalud.getId())){
					List<EmailService.Adjunto> adjuntos = new ArrayList<EmailService.Adjunto>();
					DocumentoProgramasReforzamiento documentoProgramasReforzamiento = new DocumentoProgramasReforzamiento();
					documentoProgramasReforzamiento.setIdProgramaAno(programaAno);
					documentoProgramasReforzamiento.setIdTipoDocumento(new TipoDocumento(TipoDocumentosProcesos.ADJUNTOCORREO.getId()));
					documentoProgramasReforzamiento.setIdDocumento(referenciaDocumento);
					documentoProgramasReforzamiento.setIdServicio(servicioSalud);
					documentoProgramasReforzamiento.setDistribucionRecursos(distribucionRecursos);
					recursosFinancierosProgramasReforzamientoDAO.save(documentoProgramasReforzamiento);
					if(idReferenciaDetalleMunicipal != null && tipoProgramaPxQ){
						Integer idDetalleMunicipal = construirExcelDetalleMunicipal(idProcesoModificacion, idReferenciaDetalleMunicipal, servicioSalud.getId(), programaEvaluacion.getIdProgramaAno(), programaEvaluacion.getComponentes(), servicioSalud.getNombre(), ano);
						DocumentoVO documentDocumentoResolucionVO = documentService.getDocument(idDetalleMunicipal);
						String fileNameDetalleMunicipal = tmpDirDoc + File.separator + documentDocumentoResolucionVO.getName();
						GeneradorDocumento generadorDocumento = new GeneradorDocumento(fileNameDetalleMunicipal);
						generadorDocumento.saveContent(documentDocumentoResolucionVO.getContent());
						EmailService.Adjunto adjunto = new EmailService.Adjunto();
						adjunto.setDescripcion("Detalle Municipal Programa");
						adjunto.setName("Detalle Programa "+programaEvaluacion.getNombre().replace(":", "-")+" servicio "+servicioSalud.getNombre()+" - Municipal.xlsx");
						adjunto.setUrl((new File(fileNameDetalleMunicipal)).toURI().toURL());
						adjuntos.add(adjunto);
					}
					
					if(idReferenciaDetalleServicio != null && tipoProgramaPxQ){
						Integer idDetalleServicio = construirExcelDetalleServicio(idProcesoModificacion, idReferenciaDetalleServicio, servicioSalud.getId(), programaEvaluacion.getIdProgramaAno(), servicioSalud.getNombre(), idComponentes, ano);
						DocumentoVO documentDocumentoResolucionVO = documentService.getDocument(idDetalleServicio);
						String fileNameDetalleServicio = tmpDirDoc + File.separator + documentDocumentoResolucionVO.getName();
						GeneradorDocumento generadorDocumento = new GeneradorDocumento(fileNameDetalleServicio);
						generadorDocumento.saveContent(documentDocumentoResolucionVO.getContent());
						EmailService.Adjunto adjunto = new EmailService.Adjunto();
						adjunto.setDescripcion("Detalle Servicio Programa");
						adjunto.setName("Detalle Programa " + programaEvaluacion.getNombre().replace(":", "-").replace("Ñ", "N").replace("ñ", "n").replace("?", "N")+" servicio "+servicioSalud.getNombre()+" - Servicio.xlsx");
						adjunto.setUrl((new File(fileNameDetalleServicio)).toURI().toURL());
						adjuntos.add(adjunto);
					}
					
					if(idReferenciaDetalleMunicipal != null && !tipoProgramaPxQ){
						Integer idDetalleMunicipal = construirExcelDetalleMunicipalHistorico(idProcesoModificacion, idReferenciaDetalleMunicipal, servicioSalud.getId(), programaEvaluacion.getIdProgramaAno(), programaEvaluacion.getComponentes(), servicioSalud.getNombre(), ano);
						DocumentoVO documentDocumentoResolucionVO = documentService.getDocument(idDetalleMunicipal);
						String fileNameDetalleMunicipalHistorico = tmpDirDoc + File.separator + documentDocumentoResolucionVO.getName();
						GeneradorDocumento generadorDocumento = new GeneradorDocumento(fileNameDetalleMunicipalHistorico);
						generadorDocumento.saveContent(documentDocumentoResolucionVO.getContent());
						EmailService.Adjunto adjunto = new EmailService.Adjunto();
						adjunto.setDescripcion("Detalle Municipal Programa");
						adjunto.setName("Detalle Programa "+programaEvaluacion.getNombre().replace(":", "-")+" servicio "+servicioSalud.getNombre()+" - Municipal.xlsx");
						adjunto.setUrl((new File(fileNameDetalleMunicipalHistorico)).toURI().toURL());
						adjuntos.add(adjunto);
					}
					
					if(idReferenciaDetalleServicio != null && !tipoProgramaPxQ){
						Integer idDetalleServicio = construirExcelDetalleServicioHistorico(idProcesoModificacion, idReferenciaDetalleServicio, servicioSalud.getId(), programaEvaluacion.getIdProgramaAno(), servicioSalud.getNombre(), ano);
						DocumentoVO documentDocumentoResolucionVO = documentService.getDocument(idDetalleServicio);
						String fileNameDetalleServicioHistorico = tmpDirDoc + File.separator + documentDocumentoResolucionVO.getName();
						GeneradorDocumento generadorDocumento = new GeneradorDocumento(fileNameDetalleServicioHistorico);
						generadorDocumento.saveContent(documentDocumentoResolucionVO.getContent());
						EmailService.Adjunto adjunto = new EmailService.Adjunto();
						adjunto.setDescripcion("Detalle Servicio Programa");
						adjunto.setName("Detalle Programa "+programaEvaluacion.getNombre().replace(":", "-")+" servicio "+servicioSalud.getNombre()+" - Servicio.xlsx");
						adjunto.setUrl((new File(fileNameDetalleServicioHistorico)).toURI().toURL());
						adjuntos.add(adjunto);
					}

					DocumentoVO documentDocumentoVO = documentService.getDocument(referenciaDocumento.getId());
					String fileNameDocumentoResolucion = tmpDirDoc + File.separator + documentDocumentoVO.getName();
					GeneradorDocumento generadorDocumento = new GeneradorDocumento(fileNameDocumentoResolucion);
					generadorDocumento.saveContent(documentDocumentoVO.getContent());
					EmailService.Adjunto adjunto = new EmailService.Adjunto();
					adjunto.setDescripcion("Documentos Adjuntos");
					adjunto.setName(documentDocumentoVO.getName());
					adjunto.setUrl((new File(fileNameDocumentoResolucion)).toURI().toURL());
					adjuntos.add(adjunto);

					List<String> para = new ArrayList<String>();
					para.add(servicioSalud.getDirector().getEmail().getValor());

					List<String> cc = new ArrayList<String>();
					cc.add(servicioSalud.getEncargadoFinanzasAps().getEmail().getValor());
					cc.add(servicioSalud.getEncargadoAps().getEmail().getValor());

					if(emailPLantilla != null && emailPLantilla.getAsunto() != null && emailPLantilla.getCuerpo() != null){
						emailService.sendMail(para, cc, null, emailPLantilla.getAsunto().replace("{nombrePrograma}", programaEvaluacion.getNombre()+" "), emailPLantilla.getCuerpo().replaceAll("(\r\n|\n)", "<br />").replace("{nombrePrograma}", programaEvaluacion.getNombre()+" "), adjuntos);
					}else{
						emailService.sendMail(para, cc, null, subject, "Estimado " + servicioSalud.getDirector().getNombre() + " " + servicioSalud.getDirector().getApellidoPaterno() + " " + ((servicioSalud.getDirector().getApellidoMaterno() != null) ? servicioSalud.getDirector().getApellidoMaterno() : "") + ": <br /> <p> l</p>", adjuntos);
					}

					ReporteEmailsEnviados reporteEnviados = new ReporteEmailsEnviados();
					reporteEnviados.setIdProgramaAno(programaAno);
					reporteEnviados.setIdServicio(servicioSalud);
					reporteEnviados.setFecha(new Date());
					reporteEnviados.setModifica(false);
					recursosFinancierosProgramasReforzamientoDAO.save(reporteEnviados);

					List<DocumentoProgramasReforzamiento> documentosAdjuntos =	recursosFinancierosProgramasReforzamientoDAO.getByIdProcesoIdProgramaAnoIdTipoIdServicio(idProcesoModificacion, programaEvaluacion.getIdProgramaAno(), TipoDocumentosProcesos.ADJUNTOCORREO, servicioSalud.getId());

					for(DocumentoProgramasReforzamiento documentoProgramasAdjunto : documentosAdjuntos){
						ReporteEmailsAdjuntos adjuntoMail = new ReporteEmailsAdjuntos();
						adjuntoMail.setReporteEmailsEnviado(reporteEnviados);
						adjuntoMail.setDocumento(documentoProgramasAdjunto.getIdDocumento());
						recursosFinancierosProgramasReforzamientoDAO.save(adjuntoMail);
					}

					ReporteEmailsDestinatarios destinatarioPara = new ReporteEmailsDestinatarios();
					destinatarioPara.setDestinatario(servicioSalud.getDirector());
					destinatarioPara.setTipoDestinatario(new TipoDestinatario(TiposDestinatarios.PARA.getId()));
					destinatarioPara.setReporteEmailsEnviado(reporteEnviados);
					recursosFinancierosProgramasReforzamientoDAO.save(destinatarioPara);

					ReporteEmailsDestinatarios destinatarioEncargadoFinanzasCC = new ReporteEmailsDestinatarios();
					destinatarioEncargadoFinanzasCC.setDestinatario(servicioSalud.getEncargadoFinanzasAps());
					destinatarioEncargadoFinanzasCC.setTipoDestinatario(new TipoDestinatario(TiposDestinatarios.CC.getId()));						
					destinatarioEncargadoFinanzasCC.setReporteEmailsEnviado(reporteEnviados);
					recursosFinancierosProgramasReforzamientoDAO.save(destinatarioEncargadoFinanzasCC);

					ReporteEmailsDestinatarios destinatarioEncargadoAPSCC = new ReporteEmailsDestinatarios();
					destinatarioEncargadoAPSCC.setDestinatario(servicioSalud.getEncargadoAps());
					destinatarioEncargadoAPSCC.setTipoDestinatario(new TipoDestinatario(2));						
					destinatarioEncargadoAPSCC.setReporteEmailsEnviado(reporteEnviados);
					recursosFinancierosProgramasReforzamientoDAO.save(destinatarioEncargadoAPSCC);

					ReporteEmailsProgramasReforzamiento reporteEmailsProgramasReforzamiento = new ReporteEmailsProgramasReforzamiento();
					reporteEmailsProgramasReforzamiento.setProgramasReforzamiento(recursosFinancierosProgramasReforzamientoDAO.findByIdInstancia(idProcesoModificacion));
					reporteEmailsProgramasReforzamiento.setReporteEmailsEnviados(reporteEnviados);
					recursosFinancierosProgramasReforzamientoDAO.save(reporteEmailsProgramasReforzamiento);
				}

			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Integer construirExcelDetalleMunicipal(Integer idProceso, Integer idReferenciaDocumento, Integer idServicio, Integer idProgramaAno, List<ComponentesVO> componentes, String nombreServicio, Integer ano) {

		DocumentoVO documentoVO = documentService.getDocument(idReferenciaDocumento);
		XSSFWorkbook workBook=null;

		List<Integer> idComponentes = new ArrayList<Integer>();
		for(ComponentesVO componente : componentes){
			for(SubtituloVO subs : componente.getSubtitulos()){
				if(subs.getId()==3){
					idComponentes.add(componente.getId());
				}
			}

		}
		List<ProgramaAPSVO> servicioComunas = programaService.getProgramaMunicipalesResumenDetalle(idProgramaAno, idComponentes,idServicio);
		HashMap<Integer, List<ProgramaAPSVO>> componenteResultados = new HashMap<Integer, List<ProgramaAPSVO>>();

		for(int com=0; com < idComponentes.size();com++){
			List<ProgramaAPSVO> result = new ArrayList<ProgramaAPSVO>();
			for(int i=0; i<servicioComunas.size(); i++){
				if(idComponentes.get(com).intValue()== servicioComunas.get(i).getIdComponente()){
					result.add(servicioComunas.get(i));
				}
			}
			componenteResultados.put(idComponentes.get(com), result);
		}		




		try {
			workBook = GeneradorExcel.createXlsx(documentoVO.getContent());
			XSSFSheet sheet = workBook.getSheetAt(0);
			CellStyle cellStyleLong = workBook.createCellStyle();
			cellStyleLong.setDataFormat(workBook.createDataFormat().getFormat("#,##0"));

			Long totalPrograma = 0l;

			int columna = 0;
			XSSFRow row = null;
			XSSFRow rowt = null;
			int currentRow;
			for (int elemento = 0; elemento < idComponentes.size(); elemento++) {
				currentRow=4;
				if(elemento == 0){
					columna=4;	
					for(int detalle =0; detalle < componenteResultados.get(idComponentes.get(elemento)).size(); detalle++){
						sheet.createRow(currentRow);
						currentRow++;
					}
					currentRow=4;
				}
				if(elemento==1){
					columna=7;
				}
				if(elemento==2){
					columna=10;
				}
				if(elemento==3){
					columna=13;
				}
				if(elemento==4){
					columna=16;
				}
				if(elemento==5){
					columna=19;
				}
				if(elemento==6){
					columna=22;
				}
				if(elemento==7){
					columna=25;
				}
				if(elemento==8){
					columna=28;
				}
				if(elemento==9){
					columna=31;
				}
				if(elemento==10){
					columna=34;
				}
				if(elemento==11){
					columna=37;
				}
				if(elemento==12){
					columna=40;
				}
				if(elemento==13){
					columna=43;
				}
				if(elemento==14){
					columna=46;
				}
				if(elemento==15){
					columna=49;
				}

				for(int detalle=0; detalle < componenteResultados.get(idComponentes.get(elemento)).size();detalle++){

					row = sheet.getRow(currentRow);
					XSSFCell cell_idservicio = row.createCell(0);
					cell_idservicio = row.getCell(0);				
					cell_idservicio.setCellValue(componenteResultados.get(idComponentes.get(elemento)).get(detalle).getIdServicioSalud());				

					XSSFCell cell_servicio = row.createCell(1);
					cell_servicio = row.getCell(1);
					cell_servicio.setCellValue(componenteResultados.get(idComponentes.get(elemento)).get(detalle).getServicioSalud());

					XSSFCell cell_idcomuna = row.createCell(2);
					cell_idcomuna = row.getCell(2);
					cell_idcomuna.setCellValue(componenteResultados.get(idComponentes.get(elemento)).get(detalle).getIdComuna());

					XSSFCell cell_comuna = row.createCell(3);
					cell_comuna = row.getCell(3);
					cell_comuna.setCellValue(componenteResultados.get(idComponentes.get(elemento)).get(detalle).getComuna());

					if(componenteResultados.get(idComponentes.get(elemento)).get(detalle).getTarifa()!= null && componenteResultados.get(idComponentes.get(elemento)).get(detalle).getTarifa() > 0){
						XSSFCell cell_p = row.createCell(columna);
						cell_p = row.getCell(columna);
						cell_p.setCellValue(componenteResultados.get(idComponentes.get(elemento)).get(detalle).getTarifa());
						cell_p.setCellStyle(cellStyleLong);

						XSSFCell cell_q = row.createCell(columna+1);
						cell_q = row.getCell(columna+1);
						cell_q.setCellValue(componenteResultados.get(idComponentes.get(elemento)).get(detalle).getCantidad());

						XSSFCell cell_total = row.createCell(columna+2);
						cell_total = row.getCell(columna+2);
						cell_total.setCellValue(componenteResultados.get(idComponentes.get(elemento)).get(detalle).getTotal());
						cell_total.setCellStyle(cellStyleLong);
						totalPrograma += componenteResultados.get(idComponentes.get(elemento)).get(detalle).getTotal();
					}
					currentRow++;
				}
				if(elemento==0){
					rowt = sheet.createRow(currentRow);
					rowt = sheet.getRow(currentRow);

					XSSFCell cell_tot = rowt.createCell(1);
					cell_tot = rowt.getCell(1);				
					cell_tot.setCellValue("TOTAL ($)");

					XSSFCell cell_totMonto = rowt.createCell(6);
					cell_totMonto = rowt.getCell(6);				
					cell_totMonto.setCellValue(totalPrograma);	
					cell_totMonto.setCellStyle(cellStyleLong);
					totalPrograma=0l;
				}
				if(elemento==1){
					rowt = sheet.getRow(currentRow);

					XSSFCell cell_totMonto = rowt.createCell(9);
					cell_totMonto = rowt.getCell(9);				
					cell_totMonto.setCellValue(totalPrograma);	
					cell_totMonto.setCellStyle(cellStyleLong);
					totalPrograma=0l;
				}
				if(elemento==2){
					rowt = sheet.getRow(currentRow);

					XSSFCell cell_totMonto = rowt.createCell(12);
					cell_totMonto = rowt.getCell(12);				
					cell_totMonto.setCellValue(totalPrograma);	
					cell_totMonto.setCellStyle(cellStyleLong);
					totalPrograma=0l;
				}
				if(elemento==3){
					rowt = sheet.getRow(currentRow);

					XSSFCell cell_totMonto = rowt.createCell(15);
					cell_totMonto = rowt.getCell(15);				
					cell_totMonto.setCellValue(totalPrograma);	
					cell_totMonto.setCellStyle(cellStyleLong);
					totalPrograma=0l;
				}
				if(elemento==4){
					rowt = sheet.getRow(currentRow);

					XSSFCell cell_totMonto = rowt.createCell(18);
					cell_totMonto = rowt.getCell(18);				
					cell_totMonto.setCellValue(totalPrograma);	
					cell_totMonto.setCellStyle(cellStyleLong);
					totalPrograma=0l;
				}
				if(elemento==5){
					rowt = sheet.getRow(currentRow);

					XSSFCell cell_totMonto = rowt.createCell(21);
					cell_totMonto = rowt.getCell(21);				
					cell_totMonto.setCellValue(totalPrograma);	
					cell_totMonto.setCellStyle(cellStyleLong);
					totalPrograma=0l;
				}
				if(elemento==6){
					rowt = sheet.getRow(currentRow);

					XSSFCell cell_totMonto = rowt.createCell(24);
					cell_totMonto = rowt.getCell(24);				
					cell_totMonto.setCellValue(totalPrograma);	
					cell_totMonto.setCellStyle(cellStyleLong);
					totalPrograma=0l;
				}
				if(elemento==7){
					rowt = sheet.getRow(currentRow);

					XSSFCell cell_totMonto = rowt.createCell(27);
					cell_totMonto = rowt.getCell(27);				
					cell_totMonto.setCellValue(totalPrograma);	
					cell_totMonto.setCellStyle(cellStyleLong);
					totalPrograma=0l;
				}
				if(elemento==8){
					rowt = sheet.getRow(currentRow);

					XSSFCell cell_totMonto = rowt.createCell(30);
					cell_totMonto = rowt.getCell(30);				
					cell_totMonto.setCellValue(totalPrograma);	
					cell_totMonto.setCellStyle(cellStyleLong);
					totalPrograma=0l;
				}
				if(elemento==9){
					rowt = sheet.getRow(currentRow);

					XSSFCell cell_totMonto = rowt.createCell(31);
					cell_totMonto = rowt.getCell(31);				
					cell_totMonto.setCellValue(totalPrograma);	
					cell_totMonto.setCellStyle(cellStyleLong);
					totalPrograma=0l;
				}
				if(elemento==10){
					rowt = sheet.getRow(currentRow);

					XSSFCell cell_totMonto = rowt.createCell(34);
					cell_totMonto = rowt.getCell(34);				
					cell_totMonto.setCellValue(totalPrograma);	
					cell_totMonto.setCellStyle(cellStyleLong);
					totalPrograma=0l;
				}
				if(elemento==11){
					rowt = sheet.getRow(currentRow);

					XSSFCell cell_totMonto = rowt.createCell(37);
					cell_totMonto = rowt.getCell(37);				
					cell_totMonto.setCellValue(totalPrograma);	
					cell_totMonto.setCellStyle(cellStyleLong);
					totalPrograma=0l;
				}
				if(elemento==12){
					rowt = sheet.getRow(currentRow);

					XSSFCell cell_totMonto = rowt.createCell(40);
					cell_totMonto = rowt.getCell(40);				
					cell_totMonto.setCellValue(totalPrograma);	
					cell_totMonto.setCellStyle(cellStyleLong);
					totalPrograma=0l;
				}
				if(elemento==13){
					rowt = sheet.getRow(currentRow);

					XSSFCell cell_totMonto = rowt.createCell(43);
					cell_totMonto = rowt.getCell(43);				
					cell_totMonto.setCellValue(totalPrograma);	
					cell_totMonto.setCellStyle(cellStyleLong);
					totalPrograma=0l;
				}
				if(elemento==14){
					rowt = sheet.getRow(currentRow);

					XSSFCell cell_totMonto = rowt.createCell(46);
					cell_totMonto = rowt.getCell(46);				
					cell_totMonto.setCellValue(totalPrograma);	
					cell_totMonto.setCellStyle(cellStyleLong);
					totalPrograma=0l;
				}
				if(elemento==15){
					rowt = sheet.getRow(currentRow);

					XSSFCell cell_totMonto = rowt.createCell(49);
					cell_totMonto = rowt.getCell(49);				
					cell_totMonto.setCellValue(totalPrograma);	
					cell_totMonto.setCellStyle(cellStyleLong);
					totalPrograma=0l;
				}

			}

			String filename="Detalle Excel Servicio "+nombreServicio+" Municipal.xlsx";
			filename = StringUtil.removeSpanishAccents(filename);
			GeneradorExcel generador = new GeneradorExcel(workBook, tmpDir + File.separator +filename);

			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			filename = tmpDir + File.separator + filename;
			String contentType = mimemap.getContentType(filename.toLowerCase());

			BodyVO response = alfrescoService.uploadDocument(generador.saveExcel(), contentType, folderRecursosFinancierosAPS.replace("{ANO}", ano.toString()));
			ProgramasReforzamiento programasReforzamiento = recursosFinancierosProgramasReforzamientoDAO.findByIdInstancia(idProceso);
			return documentService.createDocumentProgramasReforzamiento(programasReforzamiento, TipoDocumentosProcesos.ADJUNTOCORREO, response.getNodeRef(), response.getFileName(), contentType,
					idProgramaAno, idServicio, null);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	private Integer construirExcelDetalleServicio(Integer idProceso, Integer idReferenciaDocumento, Integer idServicio, Integer idProgramaAno, String nombreServicio, 
			List<Integer> idComponentes, Integer ano) {

		try {
			DocumentoVO documentoVO = documentService.getDocument(idReferenciaDocumento);
			XSSFWorkbook workBook = null;

			List<ProgramaAPSServicioResumenVO> servicioEstablecimientos = programaService.getProgramaServiciosResumen(idProgramaAno, idServicio, idComponentes);

			workBook = GeneradorExcel.createXlsx(documentoVO.getContent());
			XSSFSheet sheet = workBook.getSheetAt(0);

			CellStyle cellStyleLong = workBook.createCellStyle();
			cellStyleLong.setDataFormat(workBook.createDataFormat().getFormat("#,##0"));

			int currentRow = 4;
			for (ProgramaAPSServicioResumenVO registro : servicioEstablecimientos) {
				int columna = 4;
				XSSFRow row = sheet.createRow(currentRow);
				row = sheet.getRow(currentRow);
				XSSFCell cell_idservicio = row.createCell(0);
				cell_idservicio = row.getCell(0);				
				cell_idservicio.setCellValue(registro.getIdServicioSalud());				

				XSSFCell cell_servicio = row.createCell(1);
				cell_servicio = row.getCell(1);
				cell_servicio.setCellValue(registro.getServicioSalud());

				XSSFCell cell_idcomuna = row.createCell(2);
				cell_idcomuna = row.getCell(2);
				cell_idcomuna.setCellValue(registro.getCodigoEstablecimiento());

				XSSFCell cell_comuna = row.createCell(3);
				cell_comuna = row.getCell(3);
				cell_comuna.setCellValue(registro.getEstablecimiento());

				for(ComponentesVO componentes : registro.getComponentes()){

					for(SubtituloVO subtitulos : componentes.getSubtitulos()){

						XSSFCell cell_precio = row.createCell(columna);
						cell_precio = row.getCell(columna);
						cell_precio.setCellValue(subtitulos.getTarifa());

						XSSFCell cell_cantidad = row.createCell(columna+1);
						cell_cantidad = row.getCell(columna+1);
						cell_cantidad.setCellValue(subtitulos.getCantidad());

						XSSFCell cell_total = row.createCell(columna+2);
						cell_total = row.getCell(columna+2);
						cell_total.setCellValue(subtitulos.getTotal());
						cell_total.setCellStyle(cellStyleLong);

						columna +=3;
					}
				}
				currentRow++;
			}

			XSSFRow rowt = sheet.createRow(currentRow);
			rowt = sheet.getRow(currentRow);

			XSSFCell cell_total = rowt.createCell(1);
			cell_total = rowt.getCell(1);
			cell_total.setCellValue("TOTALES ($)");

			HashMap<Integer, HashMap<Integer, Long>> totales = new HashMap<Integer, HashMap<Integer,Long>>();

			for (ProgramaAPSServicioResumenVO registro : servicioEstablecimientos) {

				for(ComponentesVO componentes : registro.getComponentes()){

					if(!totales.containsKey(componentes.getId())){

						HashMap<Integer, Long> tot = new HashMap<Integer, Long>();

						for(SubtituloVO subtitulos : componentes.getSubtitulos()){

							if(!tot.containsKey(subtitulos.getId())){
								tot.put(subtitulos.getId(), subtitulos.getTotal().longValue());
							}else{
								Long suma = tot.get(subtitulos.getId());
								suma += subtitulos.getTotal().longValue();
								tot.put(subtitulos.getId(), suma);
							}
						}
						totales.put(componentes.getId(), tot);

					}else{
						HashMap<Integer, Long> tot = totales.get(componentes.getId());

						for(SubtituloVO subtitulos : componentes.getSubtitulos()){

							if(!tot.containsKey(subtitulos.getId())){
								tot.put(subtitulos.getId(), subtitulos.getTotal().longValue());
							}else{
								Long suma = tot.get(subtitulos.getId());
								suma += subtitulos.getTotal().longValue();
								tot.put(subtitulos.getId(), suma);
							}

						}
						totales.put(componentes.getId(), tot);
					}


				}
			}

			System.out.println(totales);
			List<Long> totalesFinales = new ArrayList<Long>();
			List keys = new ArrayList(totales.keySet());
			Collections.sort(keys);

			for(int i=0; i < keys.size();i++){

				HashMap<Integer, Long> valorTotal = totales.get(keys.get(i));
				List claves = new ArrayList(valorTotal.keySet());
				Collections.sort(claves);

				for(int j=0; j<claves.size(); j++){
					totalesFinales.add(valorTotal.get(claves.get(j)));
				}


			}

			int columna =6;
			for(int h=0; h < totalesFinales.size(); h++){
				XSSFCell cell_totalFinal = rowt.createCell(columna);
				cell_totalFinal = rowt.getCell(columna);
				cell_totalFinal.setCellValue(totalesFinales.get(h));
				cell_totalFinal.setCellStyle(cellStyleLong);
				columna +=3;
			}
			System.out.println("************************");

			String filename="Detalle Excel Servicio "+nombreServicio+" Servicio.xlsx";
			filename = StringUtil.removeSpanishAccents(filename);
			GeneradorExcel generador = new GeneradorExcel(workBook, tmpDir + File.separator +filename);

			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			filename = tmpDir + File.separator + filename;
			String contentType = mimemap.getContentType(filename.toLowerCase());
			BodyVO response = alfrescoService.uploadDocument(generador.saveExcel(), contentType, folderRecursosFinancierosAPS.replace("{ANO}", ano.toString()));
			ProgramasReforzamiento programasReforzamiento = recursosFinancierosProgramasReforzamientoDAO.findByIdInstancia(idProceso);
			return documentService.createDocumentProgramasReforzamiento(programasReforzamiento, TipoDocumentosProcesos.ADJUNTOCORREO, response.getNodeRef(), response.getFileName(), contentType,
					idProgramaAno, idServicio, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Integer construirExcelDetalleMunicipalHistorico(Integer idProceso, Integer idReferenciaDocumento, Integer idServicio, Integer idProgramaAno, List<ComponentesVO> componentes, String nombreServicio, Integer ano) {

		DocumentoVO docVO = documentService.getDocument(idReferenciaDocumento);
		XSSFWorkbook workBook=null;

		int id=0;
		int cantidadCompos =0;
		List<Integer> idComponentes = new ArrayList<Integer>();
		for(ComponentesVO componente : componentes){
			if(id != componente.getId()){
				id = componente.getId();
				idComponentes.add(componente.getId());
				cantidadCompos++;
			}
		}

		List<ProgramaAPSVO> servicioComunas = programaService.getProgramaMunicipalesResumenDetalle(idProgramaAno, idComponentes, idServicio);
		try {
			workBook = GeneradorExcel.createXlsx(docVO.getContent());
			XSSFSheet sheet = workBook.getSheetAt(0);
			int currentRow=4;
			Long total=0l;
			CellStyle cellStyleLong = workBook.createCellStyle();
			cellStyleLong.setDataFormat(workBook.createDataFormat().getFormat("#,##0"));
			for(ProgramaAPSVO comunas : servicioComunas){
				XSSFRow row = sheet.createRow(currentRow);

				for(int celda=0; celda < 7;celda++){
					XSSFCell cell = row.createCell(celda);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);

					if(celda==0){
						cell.setCellValue(comunas.getIdServicioSalud());
					}
					if(celda==1){
						cell.setCellValue(comunas.getServicioSalud());
					}
					if(celda==2){
						cell.setCellValue(comunas.getIdComuna());
					}
					if(celda==3){
						cell.setCellValue(comunas.getComuna());
					}
					if(celda==4){
						cell.setCellValue(comunas.getTotal());
						cell.setCellStyle(cellStyleLong);
						total += comunas.getTotal();
					}
				}
				currentRow++;
			}
			if(total>0){
				XSSFRow row = sheet.createRow(currentRow);
				XSSFCell cell = row.createCell(1);
				cell.setCellValue("TOTAL ($)");

				XSSFCell cellt = row.createCell(4);
				cellt.setCellValue(total);
				cellt.setCellStyle(cellStyleLong);
			}

			String filename="Detalle Excel Servicio "+nombreServicio+" Municipal.xlsx";
			filename = StringUtil.removeSpanishAccents(filename);
			GeneradorExcel generador = new GeneradorExcel(workBook, tmpDir + File.separator +filename);

			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			filename = tmpDir + File.separator + filename;
			String contentType = mimemap.getContentType(filename.toLowerCase());
			BodyVO response = alfrescoService.uploadDocument(generador.saveExcel(), contentType, folderRecursosFinancierosAPS.replace("{ANO}", ano.toString()));
			ProgramasReforzamiento programasReforzamiento = recursosFinancierosProgramasReforzamientoDAO.findByIdInstancia(idProceso);
			return documentService.createDocumentProgramasReforzamiento(programasReforzamiento, TipoDocumentosProcesos.ADJUNTOCORREO, response.getNodeRef(), response.getFileName(), contentType,
					idProgramaAno, idServicio, null);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}


	private Integer construirExcelDetalleServicioHistorico(Integer idProceso, Integer idReferenciaDocumento, Integer idServicio, Integer idProgramaAno, String nombreServicio, Integer ano) {

		DocumentoVO documentoVO = documentService.getDocument(idReferenciaDocumento);
		XSSFWorkbook workBook=null;
		List<ProgramaAPSServicioResumenVO> servicioEstablecimientos = programaService.getProgramaServicios(idProgramaAno, idServicio);
		int columnas = 4;
		boolean sub21=false;
		boolean sub22=false;
		boolean sub29=false;
		if(servicioEstablecimientos.size() > 0){
			if(servicioEstablecimientos.get(0).getTotalS21()!=null){
				columnas += 1;
				sub21=true;
			}
			if(servicioEstablecimientos.get(0).getTotalS22()!=null){
				columnas += 1;
				sub22=true;
			}
			if(servicioEstablecimientos.get(0).getTotalS29()!=null){
				columnas += 1;
				sub29=true;
			}
		}
		try {
			workBook = GeneradorExcel.createXlsx(documentoVO.getContent());
			XSSFSheet sheet = workBook.getSheetAt(0);

			int currentRow=4;
			Long totalS21=0l;
			Long totalS22=0l;
			Long totalS29=0l;
			CellStyle cellStyleLong = workBook.createCellStyle();
			cellStyleLong.setDataFormat(workBook.createDataFormat().getFormat("#,##0"));
			for(ProgramaAPSServicioResumenVO establecimiento : servicioEstablecimientos){
				XSSFRow row = sheet.createRow(currentRow);
				for(int celda=0; celda < columnas;celda++){
					XSSFCell cell = row.createCell(celda);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);

					if(celda==0){
						cell.setCellValue(establecimiento.getIdServicioSalud());
					}
					if(celda==1){
						cell.setCellValue(establecimiento.getServicioSalud());
					}
					if(celda==2){
						cell.setCellValue(establecimiento.getCodigoEstablecimiento());
					}
					if(celda==3){
						cell.setCellValue(establecimiento.getEstablecimiento());
					}
					if(sub21){
						if(celda==4){
							cell.setCellValue(establecimiento.getTotalS21());
							cell.setCellStyle(cellStyleLong);
							totalS21 += establecimiento.getTotalS21();
						}
					}
					if(sub21 && sub22){
						if(celda==5){
							cell.setCellValue(establecimiento.getTotalS22());
							cell.setCellStyle(cellStyleLong);
							totalS22 += establecimiento.getTotalS22();
						}
					}
					if(!sub21 && sub22){
						if(celda==4){
							cell.setCellValue(establecimiento.getTotalS22());
							cell.setCellStyle(cellStyleLong);
							totalS22 += establecimiento.getTotalS22();
						}
					}
					if(sub21 && sub22 && sub29){
						if(celda==6){
							cell.setCellValue(establecimiento.getTotalS29());
							cell.setCellStyle(cellStyleLong);
							totalS29 += establecimiento.getTotalS29();
						}
					}
					if((!sub21 || !sub22) && sub29){
						if(celda==5){
							cell.setCellValue(establecimiento.getTotalS29());
							cell.setCellStyle(cellStyleLong);
							totalS29 += establecimiento.getTotalS29();
						}
					}
					if(!sub21 && !sub22 && sub29){
						if(celda==4){
							cell.setCellValue(establecimiento.getTotalS29());
							cell.setCellStyle(cellStyleLong);
							totalS29 += establecimiento.getTotalS29();
						}
					}
				}
				currentRow++;
			}

			XSSFRow row = sheet.createRow(currentRow);
			XSSFCell cell = row.createCell(1);
			cell.setCellValue("TOTAL ($)");

			if(sub21){
				XSSFCell cellt = row.createCell(4);
				cellt.setCellValue(totalS21);
				cell.setCellStyle(cellStyleLong);
			}
			if(sub21 && sub22){
				XSSFCell cellt = row.createCell(5);
				cellt.setCellValue(totalS22);
				cell.setCellStyle(cellStyleLong);
			}
			if(!sub21 && sub22){
				XSSFCell cellt = row.createCell(4);
				cellt.setCellValue(totalS22);
				cell.setCellStyle(cellStyleLong);
			}
			if(sub21 && sub22 && sub29){
				XSSFCell cellt = row.createCell(6);
				cellt.setCellValue(totalS29);
				cell.setCellStyle(cellStyleLong);
			}
			if((!sub21 || !sub22) && sub29){
				XSSFCell cellt = row.createCell(5);
				cellt.setCellValue(totalS29);
				cell.setCellStyle(cellStyleLong);
			}
			if(!sub21 && !sub22 && sub29){
				XSSFCell cellt = row.createCell(4);
				cellt.setCellValue(totalS29);
				cell.setCellStyle(cellStyleLong);
			}

			String filename="Detalle Excel Servicio "+nombreServicio+" Servicio.xlsx";
			filename = StringUtil.removeSpanishAccents(filename);
			GeneradorExcel generador = new GeneradorExcel(workBook, tmpDir + File.separator +filename);

			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			filename = tmpDir + File.separator + filename;
			String contentType = mimemap.getContentType(filename.toLowerCase());
			BodyVO response = alfrescoService.uploadDocument(generador.saveExcel(), contentType, folderRecursosFinancierosAPS.replace("{ANO}", ano.toString()));
			ProgramasReforzamiento programasReforzamiento = recursosFinancierosProgramasReforzamientoDAO.findByIdInstancia(idProceso);
			return documentService.createDocumentProgramasReforzamiento(programasReforzamiento, TipoDocumentosProcesos.ADJUNTOCORREO, response.getNodeRef(), response.getFileName(), contentType, 
					idProgramaAno, idServicio, null);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	private Integer getIdPlantillaProgramasDetalles(Integer programaSeleccionado, TipoDocumentosProcesos tipoDocumentoProceso, Integer ano) {
		ProgramaVO programaEvaluacion = programaService.getProgramaByIdProgramaAndAno(programaSeleccionado, ano);
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();

		List<CellExcelVO> header = new ArrayList<CellExcelVO>();
		header.add(new CellExcelVO("Servicios de Salud", 2, 2));

		List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
		String filename = tmpDir + File.separator;
		GeneradorExcel generadorExcel = null;
		String contenType = null;
		switch (tipoDocumentoProceso) {
		case PLANTILLAAPSDETALLEMUNICIPAL:
			filename += "Detalle Programa -" + programaEvaluacion.getNombre().replace(":", "-")+"- Municipal.xlsx";
			filename = StringUtil.removeSpanishAccents(filename);
			generadorExcel = new GeneradorExcel(filename);
			int subtitulos=0;

			List<Integer> idComponentes =  new ArrayList<Integer>(); 
			for(ComponentesVO componente : programaEvaluacion.getComponentes()){
				for(SubtituloVO subtitulo : componente.getSubtitulos()){
					if(subtitulo.getId()==3){
						idComponentes.add(componente.getId());
						subtitulos++;
					}
				}
			}
			header.add(new CellExcelVO("Comunas", 2, 2));
			header.add(new CellExcelVO(programaEvaluacion.getNombre(), subtitulos*3, 1));
			subHeader.add(new CellExcelVO("ID", 1, 2));
			subHeader.add(new CellExcelVO("Servicio de Salud", 1, 2));
			subHeader.add(new CellExcelVO("ID", 1, 2));
			subHeader.add(new CellExcelVO("Comuna", 1, 2));

			for(ComponentesVO componente : programaEvaluacion.getComponentes()){
				if(idComponentes.indexOf(componente.getId()) != -1){
					header.add(new CellExcelVO(componente.getNombre(), 3, 1));
					for(SubtituloVO subtitulo : componente.getSubtitulos()){
						if(subtitulo.getId()==3){
							subHeader.add(new CellExcelVO(subtitulo.getNombre(), 3, 1));
							subHeader.add(new CellExcelVO("Tarifa", 1, 1));
							subHeader.add(new CellExcelVO("Cantidad", 1, 1));
							subHeader.add(new CellExcelVO("Total", 1, 1));
						}
					}
				}

			}
			contenType = mimemap.getContentType(filename.toLowerCase());


			ProgramaAPSMunicipalesDetallesSheetExcel programaAPSMunicipalesDetallesSheetExcel = new ProgramaAPSMunicipalesDetallesSheetExcel(header, subHeader,null);
			generadorExcel.addSheet(programaAPSMunicipalesDetallesSheetExcel, "Hoja 1");

			break;
		case PLANTILLAAPSDETALLESERVICIO:
			filename += "Detalle Programa -" + programaEvaluacion.getNombre().replace(":", "-")+"- Servicio.xlsx";
			filename = StringUtil.removeSpanishAccents(filename);
			generadorExcel = new GeneradorExcel(filename);
			header.add(new CellExcelVO("Establecimientos", 2, 2));

			List<Integer> idComponentesServicios = new ArrayList<Integer>();
			for(ComponentesVO componente : programaEvaluacion.getComponentes()){
				for(SubtituloVO subtitulo : componente.getSubtitulos()){
					if(subtitulo.getId()!=3){
						idComponentesServicios.add(componente.getId());
					}
				}
			}
			int subtitulosServicio=0;
			HashMap<Integer, Integer> componenteSubs = new HashMap<Integer, Integer>();
			for(ComponentesVO componente : programaEvaluacion.getComponentes()){
				int sub=0;
				for(SubtituloVO subtitulo : componente.getSubtitulos()){
					if(subtitulo.getId()!=3){
						sub++;
						if(componenteSubs.containsKey(componente.getId())){
							componenteSubs.remove(componente.getId());
							componenteSubs.put(componente.getId(), sub);
						}else{
							componenteSubs.put(componente.getId(), sub);
						}
						subtitulosServicio++;
					}
				}
			}

			//nombre programa
			header.add(new CellExcelVO(programaEvaluacion.getNombre(), (3 * subtitulosServicio), 1));
			subHeader.add(new CellExcelVO("ID", 1, 2));
			subHeader.add(new CellExcelVO("Servicio de Salud", 1, 2));
			subHeader.add(new CellExcelVO("ID", 1, 2));
			subHeader.add(new CellExcelVO("Establecimiento", 1, 2));
			for(ComponentesVO componente : programaEvaluacion.getComponentes()){
				if(idComponentesServicios.indexOf(componente.getId()) != -1){
					header.add(new CellExcelVO(componente.getNombre(),componenteSubs.get(componente.getId())*3, 1));
					for(SubtituloVO subtitulo : componente.getSubtitulos()){
						if(subtitulo.getId()!=3){
							subHeader.add(new CellExcelVO(subtitulo.getNombre(), 3, 1));
							subHeader.add(new CellExcelVO("Tarifa", 1, 1));
							subHeader.add(new CellExcelVO("Cantidad", 1, 1));
							subHeader.add(new CellExcelVO("Total", 1, 1));
						}
					}
				}

			}
			contenType = mimemap.getContentType(filename.toLowerCase());

			ProgramaAPSServiciosDetallesSheetExcel programaAPSServiciosDetallesSheetExcel = new ProgramaAPSServiciosDetallesSheetExcel(header, subHeader,null);
			generadorExcel.addSheet(programaAPSServiciosDetallesSheetExcel, "Hoja 1");
			break;
		case PLANTILLAAPSDETALLEMUNICIPALHISTORICO:
			filename += "Detalle Programa -" + programaEvaluacion.getNombre().replace(":", "-")+"- Municipal.xlsx";
			filename = StringUtil.removeSpanishAccents(filename);
			generadorExcel = new GeneradorExcel(filename);
			int subtitulosH=0;

			for(ComponentesVO componente : programaEvaluacion.getComponentes()){
				for(SubtituloVO subtitulo : componente.getSubtitulos()){
					if(subtitulo.getId()==3){
						subtitulosH++;
					}
				}
			}
			header.add(new CellExcelVO("Comunas", 2, 2));
			header.add(new CellExcelVO(programaEvaluacion.getNombre(), subtitulosH, 1));
			subHeader.add(new CellExcelVO("ID", 1, 2));
			subHeader.add(new CellExcelVO("Servicio de Salud", 1, 2));
			subHeader.add(new CellExcelVO("ID", 1, 2));
			subHeader.add(new CellExcelVO("Comuna", 1, 2));

			for(ComponentesVO componente : programaEvaluacion.getComponentes()){
				header.add(new CellExcelVO(componente.getNombre(), subtitulosH, 1));
				for(SubtituloVO subtitulo : componente.getSubtitulos()){
					if(subtitulo.getId()==3){
						subHeader.add(new CellExcelVO(subtitulo.getNombre(), 1, 1));
						subHeader.add(new CellExcelVO("Total", 1, 1));
					}
				}
			}

			contenType = mimemap.getContentType(filename.toLowerCase());

			ProgramaAPSDetallesMunicipalesHistoricosSheetExcel programaAPSDetallesMunicipalesHistoricosSheetExcel = new ProgramaAPSDetallesMunicipalesHistoricosSheetExcel(header, subHeader,null);
			generadorExcel.addSheet(programaAPSDetallesMunicipalesHistoricosSheetExcel, "Hoja 1");
			break;
		case PLANTILLAAPSDETALLESERVICIOHISTORICO:
			filename += "Detalle Programa -" + programaEvaluacion.getNombre().replace(":", "-")+"- Servicio.xlsx";
			filename = StringUtil.removeSpanishAccents(filename);
			generadorExcel = new GeneradorExcel(filename);
			header.add(new CellExcelVO("Establecimientos", 2, 2));
			int subtitulosServicioH=0;
			for(ComponentesVO componente : programaEvaluacion.getComponentes()){
				for(SubtituloVO subtitulo : componente.getSubtitulos()){
					if(subtitulo.getId()!=3){
						subtitulosServicioH++;
					}
				}
				//nombre programa
				header.add(new CellExcelVO(programaEvaluacion.getNombre(), subtitulosServicioH, 1));
				subHeader.add(new CellExcelVO("ID", 1, 2));
				subHeader.add(new CellExcelVO("Servicio de Salud", 1, 2));
				subHeader.add(new CellExcelVO("ID", 1, 2));
				subHeader.add(new CellExcelVO("Establecimiento", 1, 2));

				header.add(new CellExcelVO(componente.getNombre(),subtitulosServicioH, 1));
				for(SubtituloVO subtitulo : componente.getSubtitulos()){
					if(subtitulo.getId()!=3){
						subHeader.add(new CellExcelVO(subtitulo.getNombre(), 1, 1));
						subHeader.add(new CellExcelVO("Total", 1, 1));
					}
				}
			}
			contenType = mimemap.getContentType(filename.toLowerCase());

			ProgramaAPSDetallesServiciosHistoricosSheetExcel programaAPSDetallesServiciosHistoricosSheetExcel = new ProgramaAPSDetallesServiciosHistoricosSheetExcel(header, subHeader,null);
			generadorExcel.addSheet(programaAPSDetallesServiciosHistoricosSheetExcel, "Hoja 1");
			break;
		default:
			break;
		}

		Integer plantillaId = null;
		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderTemplateRecursosFinancierosAPS);
			plantillaId = documentService.createTemplateProgramas(tipoDocumentoProceso, response.getNodeRef(), response.getFileName(), contenType, programaEvaluacion);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return plantillaId;
	}

	public List<ReporteEmailsEnviadosVO> getReporteCorreosByIdInstanciaReforzamiento(Integer idProceso) {
		List<ReporteEmailsEnviadosVO> reporteCorreos = new ArrayList<ReporteEmailsEnviadosVO>();
		List<ReporteEmailsProgramasReforzamiento> reporteCorreosPrograma = recursosFinancierosProgramasReforzamientoDAO.getReporteCorreosProgramasReforzamiento(idProceso);
		for(ReporteEmailsProgramasReforzamiento report : reporteCorreosPrograma){
			ReporteEmailsEnviadosVO resumenEnviados = new ReporteEmailsEnviadosVO();
			Set<ReporteEmailsAdjuntos> adjuntos = report.getReporteEmailsEnviados().getReporteEmailsAdjuntosSet();
			List<AdjuntosVO> adjs = new ArrayList<AdjuntosVO>();
			for(ReporteEmailsAdjuntos adj : adjuntos){
				AdjuntosVO ad = new AdjuntosVO();
				if(adj.getDocumento() != null){
					ad.setId(adj.getDocumento().getId());
					ad.setNombre(adj.getDocumento().getPath());
					adjs.add(ad);
				}
			}
			resumenEnviados.setAdjuntos(adjs);
			Set<ReporteEmailsDestinatarios> destinatarios = report.getReporteEmailsEnviados().getReporteEmailsDestinatariosSet();
			List<String> to = new ArrayList<String>();
			List<String> cc = new ArrayList<String>();
			for(ReporteEmailsDestinatarios destina : destinatarios){
				if(TiposDestinatarios.PARA.getId() == destina.getTipoDestinatario().getIdTipoDestinatario()){
					String para = destina.getDestinatario().getEmail().getValor();
					to.add(para);
				}else if(TiposDestinatarios.CC.getId() == destina.getTipoDestinatario().getIdTipoDestinatario()){
					String copia = destina.getDestinatario().getEmail().getValor();
					cc.add(copia);
				}

			}
			resumenEnviados.setCc(cc);
			resumenEnviados.setTo(to);
			resumenEnviados.setFecha(report.getReporteEmailsEnviados().getFecha());
			resumenEnviados.setFechaFormat(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(report.getReporteEmailsEnviados().getFecha()));
			resumenEnviados.setIdServicio(report.getReporteEmailsEnviados().getIdServicio().getId());
			resumenEnviados.setNombreServicio(report.getReporteEmailsEnviados().getIdServicio().getNombre());
			reporteCorreos.add(resumenEnviados);
		}
		return reporteCorreos;
	}

	public Integer crearInstanciaModificacion(String username) {
		System.out.println("username--> " + username);
		Usuario usuario = this.usuarioDAO.getUserByUsername(username);
		String mesCurso = getMesCurso(true);
		Mes mes = mesDAO.getMesPorID(Integer.parseInt(mesCurso));

		return recursosFinancierosProgramasReforzamientoDAO.crearInstanciaModificacion(usuario,mes,new Date());
	}

	public String getMesCurso(Boolean numero) {
		SimpleDateFormat dateFormat = null;
		String mesCurso = null;
		if(numero){
			dateFormat = new SimpleDateFormat("MM");
			mesCurso = dateFormat.format(new Date());
		}else{
			dateFormat = new SimpleDateFormat("MMMM");
			mesCurso = dateFormat.format(new Date());
		}
		return mesCurso;
	}

	public Integer crearInstanciaDistribucion(String username) {
		System.out.println("username--> " + username);
		Usuario usuario = this.usuarioDAO.getUserByUsername(username);
		String mesCurso = getMesCurso(true);
		Mes mes = mesDAO.getMesPorID(Integer.parseInt(mesCurso));
		return recursosFinancierosProgramasReforzamientoDAO.crearInstanciaModificacion(usuario,mes,new Date());
	}

	public void notificarGeneracionResoluciones(Integer idPrograma, Integer ano, String username) {
		try {
			Usuario usuario = usuarioDAO.getUserByUsername(username);
			ProgramaVO programaVO = programaService.getProgramaByIdProgramaAndAno(idPrograma, ano);
			List<EmailService.Adjunto> adjuntos = new ArrayList<EmailService.Adjunto>();
			emailService.sendMail(usuario.getEmail().getValor(), "Generación automática de documentos(Resoluciones/Ordinarios)", "Estimado " + username + ": <br /> <p> Se completo exitosamente la generación de Resoluciones/Ordinarios para el programa " + programaVO.getNombre() + "</p>", adjuntos);
			System.out.println("notificarGeneracionResoluciones se ejecuto correctamente");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Integer getLastDocumentRecursosFinancierosByType(Integer idProceso, Integer idProgramaAno, TipoDocumentosProcesos tipoDocumento) {
		return documentService.getLastDocumentRecursosFinancierosByType(idProceso, idProgramaAno, tipoDocumento);
	}
	
	public int countVersionFinalRecursosFinancierosByType(Integer idProceso, Integer idProgramaAno, TipoDocumentosProcesos tipoDocumento) {
		List<ReferenciaDocumentoSummaryVO> versionesFinales = documentService.getVersionFinalRecursosFinancierosByType(idProceso, idProgramaAno, tipoDocumento);
		if(versionesFinales != null && versionesFinales.size() > 0){
			return versionesFinales.size();
		}
		return 0;
	}

	public void administrarVersionesFinales(Integer idProgramaAno, Integer ano, Integer idProceso) {
		System.out.println("DistribucionInicialPercapitaService administrarVersionesFinalesAlfresco eliminar todas las versiones que no sean finales");
		TipoDocumentosProcesos[] tiposDocumentos = {TipoDocumentosProcesos.RESOLUCIONPROGRAMASAPS, TipoDocumentosProcesos.ORDINARIOPROGRAMASAPS};
		List<DocumentoProgramasReforzamiento> documentosProgramasReforzamiento = recursosFinancierosProgramasReforzamientoDAO.getDocumentByIdProcesoIdProgramaAnoTipoNotFinal(idProceso, idProgramaAno, tiposDocumentos);
		if(documentosProgramasReforzamiento != null && documentosProgramasReforzamiento.size() > 0){
			for(DocumentoProgramasReforzamiento documentoProgramasReforzamiento : documentosProgramasReforzamiento){
				String key = ((documentoProgramasReforzamiento.getIdDocumento().getNodeRef() == null) ? documentoProgramasReforzamiento.getIdDocumento().getPath() : documentoProgramasReforzamiento.getIdDocumento().getNodeRef().replace("workspace://SpacesStore/", ""));
				System.out.println("key->"+key);
				alfrescoService.delete(key);
				recursosFinancierosProgramasReforzamientoDAO.deleteDocumentoProgramasReforzamiento(documentoProgramasReforzamiento.getId());
				recursosFinancierosProgramasReforzamientoDAO.deleteDocumento(documentoProgramasReforzamiento.getIdDocumento().getId());
			}
		}
		
	}

 

}
