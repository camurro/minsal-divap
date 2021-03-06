package minsal.divap.service;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.xml.bind.JAXBException;

import minsal.divap.dao.CajaDAO;
import minsal.divap.dao.ComponenteDAO;
import minsal.divap.dao.ComunaDAO;
import minsal.divap.dao.ConveniosDAO;
import minsal.divap.dao.DocumentDAO;
import minsal.divap.dao.EstablecimientosDAO;
import minsal.divap.dao.InstitucionDAO;
import minsal.divap.dao.MesDAO;
import minsal.divap.dao.ProgramasDAO;
import minsal.divap.dao.SeguimientoDAO;
import minsal.divap.dao.ServicioSaludDAO;
import minsal.divap.dao.TipoSubtituloDAO;
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.doc.GeneradorDocumento;
import minsal.divap.doc.GeneradorResolucionAporteEstatal;
import minsal.divap.doc.GeneradorWord;
import minsal.divap.enums.EstadosConvenios;
import minsal.divap.enums.EstadosProgramas;
import minsal.divap.enums.Instituciones;
import minsal.divap.enums.Subtitulo;
import minsal.divap.enums.TareasSeguimiento;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.enums.TiposDestinatarios;
import minsal.divap.excel.GeneradorExcel;
import minsal.divap.excel.impl.CrearPlanillaConveniosProgramaSheetExcel;
import minsal.divap.excel.interfaces.ExcelTemplate;
import minsal.divap.model.mappers.CargaConvenioComunaComponenteMapper;
import minsal.divap.model.mappers.CargaConvenioServicioComponenteMapper;
import minsal.divap.model.mappers.ConvenioComunaComponenteMapper;
import minsal.divap.model.mappers.ConvenioComunaComponenteSubtituloMapper;
import minsal.divap.model.mappers.ConvenioServicioComponenteMapper;
import minsal.divap.model.mappers.ConvenioServicioComponenteSubtituloMapper;
import minsal.divap.model.mappers.ConvenioServicioMapper;
import minsal.divap.model.mappers.ProgramaMapper;
import minsal.divap.vo.AdjuntosVO;
import minsal.divap.vo.BodyVO;
import minsal.divap.vo.CargaConvenioComponenteSubtituloVO;
import minsal.divap.vo.CargaConvenioComunaComponenteVO;
import minsal.divap.vo.CargaConvenioServicioComponenteVO;
import minsal.divap.vo.CellExcelVO;
import minsal.divap.vo.ComponenteSummaryVO;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ComunaSummaryVO;
import minsal.divap.vo.ConvenioComponenteSubtituloVO;
import minsal.divap.vo.ConvenioComunaComponenteVO;
import minsal.divap.vo.ConvenioDocumentoVO;
import minsal.divap.vo.ConvenioServicioComponenteVO;
import minsal.divap.vo.ConveniosVO;
import minsal.divap.vo.DocumentoVO;
import minsal.divap.vo.EstablecimientoSummaryVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.ReporteEmailsEnviadosVO;
import minsal.divap.vo.ResolucionConveniosServicioVO;
import minsal.divap.vo.SeguimientoVO;
import minsal.divap.vo.ServiciosVO;
import minsal.divap.vo.SubtituloSummaryVO;
import minsal.divap.vo.SubtituloVO;
import minsal.divap.xml.GeneradorXML;
import minsal.divap.xml.email.Email;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.docx4j.openpackaging.exceptions.Docx4JException;

import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.ComponenteSubtitulo;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.Convenio;
import cl.minsal.divap.model.ConvenioComuna;
import cl.minsal.divap.model.ConvenioComunaComponente;
import cl.minsal.divap.model.ConvenioServicio;
import cl.minsal.divap.model.ConvenioServicioComponente;
import cl.minsal.divap.model.DocumentoConvenio;
import cl.minsal.divap.model.DocumentoConvenioComuna;
import cl.minsal.divap.model.Establecimiento;
import cl.minsal.divap.model.EstadoConvenio;
import cl.minsal.divap.model.EstadoPrograma;
import cl.minsal.divap.model.Institucion;
import cl.minsal.divap.model.Mes;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.ProgramaComponente;
import cl.minsal.divap.model.ReferenciaDocumento;
import cl.minsal.divap.model.ReporteEmailsAdjuntos;
import cl.minsal.divap.model.ReporteEmailsConvenio;
import cl.minsal.divap.model.ReporteEmailsDestinatarios;
import cl.minsal.divap.model.ReporteEmailsEnviados;
import cl.minsal.divap.model.Seguimiento;
import cl.minsal.divap.model.ServicioSalud;
import cl.minsal.divap.model.TipoDestinatario;
import cl.minsal.divap.model.TipoSubtitulo;
import cl.minsal.divap.model.Usuario;



@Stateless
@LocalBean
public class ConveniosService {

	@EJB
	private CajaDAO cajaDAO;
	@EJB
	private ComponenteDAO componenteDAO;
	@EJB
	private TipoSubtituloDAO tipoSubtituloDAO;
	@EJB
	private EstablecimientosDAO establecimientosDAO;
	@EJB
	private ProgramasDAO programasDAO;
	@EJB
	private ComunaDAO comunaDAO;
	@EJB
	private UsuarioDAO usuarioDAO;
	@EJB
	private DocumentDAO documentDAO;
	@EJB
	private ConveniosDAO conveniosDAO;
	@EJB
	private ServicioSaludDAO servicioSaludDAO;
	@EJB
	private MesDAO mesDAO;
	@EJB
	private InstitucionDAO institucionDAO;
	@EJB
	private SubtituloService subtituloService;
	@EJB
	private SeguimientoDAO seguimientoDAO;
	@EJB
	private ProgramasService programasService;
	@EJB
	private DocumentService documentService;
	@EJB
	private AlfrescoService alfrescoService;
	@EJB
	private ServicioSaludService servicioSaludService;
	@EJB
	private SeguimientoService seguimientoService;
	@EJB
	private EmailService emailService;
	@Resource(name="tmpDir")
	private String tmpDir;
	@Resource(name="tmpDirDoc")
	private String tmpDirDoc;
	@Resource(name="folderTemplateConvenio")
	private String folderTemplateConvenio;
	@Resource(name="folderProcesoConvenio")
	private String folderProcesoConvenio;

	public void moveConvenioComunaToAlfresco(Integer idConvenioComuna, Integer referenciaDocumentoId, TipoDocumentosProcesos tipoDocumento, Integer ano) {
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummary = documentService.getDocumentSummary(referenciaDocumentoId);
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String contenType= mimemap.getContentType(referenciaDocumentoSummary.getPath().toLowerCase());

		BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummary.getPath()), contenType, folderProcesoConvenio.replace("{ANO}", ano.toString()));
		System.out.println("response upload template --->"+response);
		documentService.updateDocumentTemplate(referenciaDocumentoSummary.getId(), response.getNodeRef(), response.getFileName(), contenType);
		ConvenioComuna convenioComuna = conveniosDAO.findConvenioComunaById(idConvenioComuna);
		documentService.createDocumentConvenioComuna(convenioComuna, tipoDocumento, referenciaDocumentoId);
	}

	public void moveConvenioServicioToAlfresco(Integer idConvenioServicio, Integer referenciaDocumentoId, TipoDocumentosProcesos tipoDocumento, Integer ano) {
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummary = documentService.getDocumentSummary(referenciaDocumentoId);
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String contenType= mimemap.getContentType(referenciaDocumentoSummary.getPath().toLowerCase());

		BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummary.getPath()), contenType, folderProcesoConvenio.replace("{ANO}", ano.toString()));
		System.out.println("response upload template --->"+response);
		documentService.updateDocumentTemplate(referenciaDocumentoSummary.getId(), response.getNodeRef(), response.getFileName(), contenType);
		ConvenioServicio convenioServicio = conveniosDAO.findConvenioServicioById(idConvenioServicio);
		documentService.createDocumentConvenioServicio(convenioServicio, tipoDocumento, referenciaDocumentoId);
	}

	public Integer getAnoCurso() {
		DateFormat formatNowYear = new SimpleDateFormat("yyyy");
		Date nowDate = new Date();
		return Integer.valueOf(formatNowYear.format(nowDate)); 
	}

	public void setDocumentoConvenioById(String nodoref,String filename,int id) {
		conveniosDAO.setDocumentoConvenioById(nodoref,filename,id);
	}

	public ConveniosVO getConvenioById(int idConvenioComuna) {
		ConvenioComuna Convenios = this.conveniosDAO.getConvenioComunaById(idConvenioComuna);
		ConveniosVO componentesConvenios = new ConveniosVO();
		componentesConvenios.setComunaNombre(Convenios.getIdComuna().getNombre());
		componentesConvenios.setProgramaNombre(Convenios.getIdPrograma().getPrograma().getNombre());
		componentesConvenios.setIdConverio(Convenios.getIdConvenioComuna());
		componentesConvenios.setNumeroResolucion(Convenios.getNumeroResolucion());
		if(Convenios.getIdPrograma().getProgramaComponentes() != null && Convenios.getIdPrograma().getProgramaComponentes().size() > 0){
			for (ProgramaComponente programaComponente : Convenios.getIdPrograma().getProgramaComponentes()){
				if(programaComponente.getComponente() != null){
					componentesConvenios.setComponente(programaComponente.getComponente().getNombre());
				}
			}
		}
		return componentesConvenios;
	}

	public List<ConvenioDocumentoVO> getDocumentById(int idConvenioComuna) {
		List<DocumentoConvenioComuna> conveniosDocumentos = this.documentDAO.getDocumentPopUpConvenioComuna(idConvenioComuna);
		List<ConvenioDocumentoVO> documentosConvenio =  new ArrayList<ConvenioDocumentoVO>();
		for (DocumentoConvenioComuna convedoc : conveniosDocumentos){
			ConvenioDocumentoVO ConDocVO = new ConvenioDocumentoVO();
			ConDocVO.setNombreDocu(convedoc.getDocumento().getPath());
			ConDocVO.setId(convedoc.getDocumento().getId());
			documentosConvenio.add(ConDocVO);
		}
		return documentosConvenio;
	}

	public List<ProgramaVO> getProgramasByUserAno(String username, Integer ano) {
		if(ano == null){
			ano = getAnoCurso();
		}
		List<ProgramaAno> programas = this.programasDAO.getProgramasByUserAno(username, ano);
		List<ProgramaVO> result = new ArrayList<ProgramaVO>();
		if(programas != null && programas.size() > 0){
			for(ProgramaAno programa : programas){
				result.add(new ProgramaMapper().getBasic(programa));
			}
		}
		return result;
	}

	public void cambiarEstadoPrograma(Integer programaSeleccionado, Integer ano, EstadosProgramas estadoPrograma) {
		System.out.println("programaSeleccionado-->" + programaSeleccionado + " estadoPrograma.getId()-->"+estadoPrograma.getId());
		ProgramaAno programaAno = programasDAO.getProgramaAnoByIdProgramaAndAno(programaSeleccionado, ano);
		programaAno.setEstadoConvenio(new EstadoPrograma(estadoPrograma.getId()));
		System.out.println("Cambia estado ok");
	}

	public void notificarServicioSalud(Integer programaSeleccionado, Integer ano, Integer idConvenio) {
		System.out.println("ConveniosService::notificarServicioSalud programaSeleccionado->"+programaSeleccionado);
		System.out.println("ConveniosService::notificarServicioSalud ano->"+ano);
		System.out.println("ConveniosService::notificarServicioSalud idConvenio->"+idConvenio);
		List<ServicioSalud> serviciosSalud = servicioSaludDAO.getServiciosOrderId();
		ProgramaAno programaAno = programasDAO.getProgramaAnoByIDProgramaAno(programaSeleccionado, ano);
		Convenio convenio =  conveniosDAO.getConvenioById(idConvenio);
		if(serviciosSalud != null && serviciosSalud.size() > 0){
			for(ServicioSalud servicioSalud : serviciosSalud){
				List<String> to = new ArrayList<String>();
				List<String> cc = new ArrayList<String>();
				String asunto = "Existen Reparos en la Información del Convenio";
				StringBuilder contenido = new StringBuilder();
				contenido.append("Estimado:");
				contenido.append("<p>");
				Map<String, Map<Integer, String>> componenteSubtituloResolucionesComuna = new LinkedHashMap<String, Map<Integer,String>>();
				Set<ConvenioComuna> conveniosComuna = convenio.getConveniosComuna();
				if(conveniosComuna != null && conveniosComuna.size() > 0){
					for(ConvenioComuna convenioComuna : conveniosComuna){
						if(!convenioComuna.getEstadoConvenio().getIdEstadoConvenio().equals(EstadosConvenios.RECHAZADO.getId())){
							continue;
						}
						if(servicioSalud.getId().equals(convenioComuna.getIdComuna().getServicioSalud().getId())){
							for(ConvenioComunaComponente convenioComunaComponente : convenioComuna.getConvenioComunaComponentes()){
								String componenteSubtitulo = convenioComunaComponente.getComponente().getNombre() + "@" + convenioComunaComponente.getSubtitulo().getNombreSubtitulo();
								Integer numeroResulucion = convenioComuna.getNumeroResolucion();
								String comuna = convenioComuna.getIdComuna().getNombre();
								if(!componenteSubtituloResolucionesComuna.containsKey(componenteSubtitulo)){
									Map<Integer, String> resolucionComuna = new LinkedHashMap<Integer, String>();
									resolucionComuna.put(numeroResulucion, comuna);
									componenteSubtituloResolucionesComuna.put(componenteSubtitulo, resolucionComuna);
								}else{
									componenteSubtituloResolucionesComuna.get(componenteSubtitulo).put(numeroResulucion, comuna);
								}
							}
						}
					}
				}

				Map<String, Map<Integer, String>> componenteSubtituloResolucionesEstablecimiento = new LinkedHashMap<String, Map<Integer,String>>();
				Set<ConvenioServicio> conveniosServicio = convenio.getConveniosServicio();
				if(conveniosServicio != null && conveniosServicio.size()  > 0){
					for(ConvenioServicio convenioServicio : conveniosServicio){
						if(!convenioServicio.getEstadoConvenio().getIdEstadoConvenio().equals(EstadosConvenios.RECHAZADO.getId())){
							continue;
						}
						if(servicioSalud.getId().equals(convenioServicio.getIdEstablecimiento().getServicioSalud().getId())){
							convenioServicio.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.RECHAZADO.getId()));
							for(ConvenioServicioComponente convenioServicioComponente : convenioServicio.getConvenioServicioComponentes()){
								String componenteSubtitulo = convenioServicioComponente.getComponente().getNombre() + "@" + convenioServicioComponente.getSubtitulo().getNombreSubtitulo();
								Integer numeroResulucion = convenioServicio.getNumeroResolucion();
								String establecimiento = convenioServicio.getIdEstablecimiento().getNombre();
								if(!componenteSubtituloResolucionesEstablecimiento.containsKey(componenteSubtitulo)){
									Map<Integer, String> resolucionEstablecimiento = new LinkedHashMap<Integer, String>();
									resolucionEstablecimiento.put(numeroResulucion, establecimiento);
									componenteSubtituloResolucionesEstablecimiento.put(componenteSubtitulo, resolucionEstablecimiento);
								}else{
									componenteSubtituloResolucionesEstablecimiento.get(componenteSubtitulo).put(numeroResulucion, establecimiento);
								}
							}
						}
					}
				}
				if(componenteSubtituloResolucionesComuna.isEmpty() && componenteSubtituloResolucionesEstablecimiento.isEmpty()){
					//no hay convenios rechazados
					continue;
				}
				contenido.append("Existen reparos en la información del convenio").append("<br/>");
				contenido.append("para la linea programática  ").append(programaAno.getPrograma().getNombre()).append("<br/>");
				if(!componenteSubtituloResolucionesComuna.isEmpty()){
					contenido.append("<table>");
					contenido.append("<thead>");
					contenido.append("<tr>");
					contenido.append("<th>").append("Componente").append("</th>");
					contenido.append("<th>").append("Subtítulo").append("</th>");
					contenido.append("<th>").append("Resolución").append("</th>");
					contenido.append("<th>").append("Comuna").append("</th>");
					contenido.append("</tr>");
					contenido.append("</thead>");
					contenido.append("<tbody>");
					for(Map.Entry<String, Map<Integer, String>> entry : componenteSubtituloResolucionesComuna.entrySet()) {
						String componenteSubtitulo = entry.getKey();
						String [] splitComponenteSubtitulo = componenteSubtitulo.split("@");
						for (Map.Entry<Integer, String> entryResolucionComuna : entry.getValue().entrySet()){
							contenido.append("<tr>");
							contenido.append("<td>").append(splitComponenteSubtitulo[0]).append("</td>");
							contenido.append("<td>").append(splitComponenteSubtitulo[1]).append("</td>");
							contenido.append("<td>").append(entryResolucionComuna.getKey()).append(" </td>");
							contenido.append("<td>").append(entryResolucionComuna.getValue()).append(" </td>");
							contenido.append("</tr>");
						}
					}
					contenido.append("</tbody>");
					contenido.append("<table>").append("<br/>");
				}

				if(!componenteSubtituloResolucionesEstablecimiento.isEmpty()){
					contenido.append("<table>");
					contenido.append("<thead>");
					contenido.append("<tr>");
					contenido.append("<th>").append("Componente").append("</th>");
					contenido.append("<th>").append("Subtítulo").append("</th>");
					contenido.append("<th>").append("Resolución").append("</th>");
					contenido.append("<th>").append("Establecimiento").append("</th>");
					contenido.append("</tr>");
					contenido.append("</thead>");
					contenido.append("<tbody>");
					for(Map.Entry<String, Map<Integer, String>> entry : componenteSubtituloResolucionesEstablecimiento.entrySet()) {
						String componenteSubtitulo = entry.getKey();
						String [] splitComponenteSubtitulo = componenteSubtitulo.split("@");
						for (Map.Entry<Integer, String> entryResolucionEstablecimiento : entry.getValue().entrySet()){
							contenido.append("<tr>");
							contenido.append("<td>").append(splitComponenteSubtitulo[0]).append("</td>");
							contenido.append("<td>").append(splitComponenteSubtitulo[1]).append("</td>");
							contenido.append("<td>").append(entryResolucionEstablecimiento.getKey()).append(" </td>");
							contenido.append("<td>").append(entryResolucionEstablecimiento.getValue()).append(" </td>");
							contenido.append("</tr>");
						}
					}
					contenido.append("</tbody>");
					contenido.append("<table>").append("<br/>");
				}

				contenido.append("por favor comuniquese con el encargado de esta linea programática");
				contenido.append("</p>");
				contenido.append("Saludos Cordiales.");
				if(servicioSalud.getEncargadoFinanzasAps() != null && servicioSalud.getEncargadoFinanzasAps().getEmail() != null){
					to.add(servicioSalud.getEncargadoFinanzasAps().getEmail().getValor());
				}
				if(servicioSalud.getDirector() != null && servicioSalud.getDirector().getEmail() != null){
					cc.add(servicioSalud.getDirector().getEmail().getValor());
				}
				if(servicioSalud.getEncargadoAps()!= null && servicioSalud.getEncargadoAps().getEmail() != null){
					cc.add(servicioSalud.getEncargadoAps().getEmail().getValor());
				}
				emailService.sendMail(to, cc, null, asunto, contenido.toString());
			}
		}
	}

	public Integer generarResolucionDisponibilizarAlfresco(Integer programaSeleccionado, Integer ano, Integer idConvenio) {
		System.out.println("ConveniosService::generarResolucionDisponibilizarAlfresco->"+programaSeleccionado);
		Integer plantillaIdResolucionRetiro = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLARESOLUCIONRETIRO);
		if(plantillaIdResolucionRetiro == null){
			throw new RuntimeException("No se puede crear Resolución Rebaja Aporte Estatal, la plantilla no esta cargada");
		}
		try {
			ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryResolucionRetiroVO = documentService.getDocumentByPlantillaId(plantillaIdResolucionRetiro);
			DocumentoVO documentoResolucionRetiroVO = documentService.getDocument(referenciaDocumentoSummaryResolucionRetiroVO.getId());
			String templateResolucionRetiro = tmpDirDoc + File.separator + documentoResolucionRetiroVO.getName();
			templateResolucionRetiro = templateResolucionRetiro.replace(" ", "");

			System.out.println("templateResolucionRetiro template-->"+templateResolucionRetiro);
			GeneradorWord generadorWordPlantillaResolucionRebaja = new GeneradorWord(templateResolucionRetiro);
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			generadorWordPlantillaResolucionRebaja.saveContent(documentoResolucionRetiroVO.getContent(), XWPFDocument.class);
			Map<String, Object> parametersResolucionRetiro = new HashMap<String, Object>();
			String filenameResolucionRetiro = tmpDirDoc + File.separator + new Date().getTime() + "_" + "ResolucionRetiro.docx";
			String contentTypeResolucionRetiro = mimemap.getContentType(filenameResolucionRetiro.toLowerCase());
			GeneradorResolucionAporteEstatal generadorWordResolucionRetiro = new GeneradorResolucionAporteEstatal(filenameResolucionRetiro, templateResolucionRetiro);
			generadorWordResolucionRetiro.replaceValues(parametersResolucionRetiro, XWPFDocument.class);
			BodyVO responseBorradorResolucionRetiro = alfrescoService.uploadDocument(new File(filenameResolucionRetiro), contentTypeResolucionRetiro, folderProcesoConvenio.replace("{ANO}", ano.toString()));
			System.out.println("response responseBorradorResolucionRebaja --->"+responseBorradorResolucionRetiro);
			plantillaIdResolucionRetiro = documentService.createDocumentConvenio(idConvenio, null, TipoDocumentosProcesos.RESOLUCIONRETIRO, responseBorradorResolucionRetiro.getNodeRef(), responseBorradorResolucionRetiro.getFileName(), contentTypeResolucionRetiro);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Docx4JException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		return plantillaIdResolucionRetiro;
	}

	public void administrarVersionesAlfresco(Integer programaSeleccionado, Integer idConvenio) {
		System.out.println("ConveniosService::administrarVersionesAlfresco->"+programaSeleccionado);	
		List<DocumentoConvenio> documentosConvenio = conveniosDAO.getByIdConvenioTipoNotFinal(idConvenio, TipoDocumentosProcesos.RESOLUCIONRETIRO);
		if(documentosConvenio != null && documentosConvenio.size() > 0){
			for(DocumentoConvenio documentoConvenio : documentosConvenio){
				String key = ((documentoConvenio.getDocumento().getNodeRef() == null) ? documentoConvenio.getDocumento().getPath() : documentoConvenio.getDocumento().getNodeRef().replace("workspace://SpacesStore/", ""));
				System.out.println("key->"+key);
				alfrescoService.delete(key);
				conveniosDAO.deleteDocumentoConvenio(documentoConvenio.getIdDocumentoConvenio());
				conveniosDAO.deleteDocumento(documentoConvenio.getDocumento().getId());
			}
		}
	}

	public List<CargaConvenioComunaComponenteVO> getResolucionConveniosMunicipal(Integer idServicio, Integer idProgramaAno, Integer componenteSeleccionado, Integer comunaSeleccionada) {
		List<CargaConvenioComunaComponenteVO> resolucionesConvenios = new ArrayList<CargaConvenioComunaComponenteVO>();
		ProgramaVO programa = programasService.getProgramaAno(idProgramaAno);
		if(comunaSeleccionada == null){
			List<Comuna> comunas = comunaDAO.getComunasByServicio(idServicio);
			Componente componente = componenteDAO.getComponenteByID(componenteSeleccionado);
			TipoSubtitulo subtitulo = tipoSubtituloDAO.getTipoSubtituloById(Subtitulo.SUBTITULO24.getId());
			for(Comuna comuna : comunas){
				List<ConvenioComunaComponente> conveniosComunaComponenteIngresada = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getIdTipoSubtitulo(), comuna.getId(), EstadosConvenios.INGRESADO.getId());
				List<ConvenioComunaComponente> conveniosComunaComponenteAprobado = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getIdTipoSubtitulo(), comuna.getId(), EstadosConvenios.APROBADO.getId());
				List<ConvenioComunaComponente> conveniosComunaComponenteEnTramite = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getIdTipoSubtitulo(), comuna.getId(), EstadosConvenios.TRAMITE.getId());
				List<ConvenioComunaComponente> conveniosComunaComponentePagada = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getIdTipoSubtitulo(), comuna.getId(), EstadosConvenios.PAGADO.getId());
				boolean nuevo = true;
				Integer montoActual = 0;
				Integer idConvenioComuna = null;
				Integer numeroResoucion = null;

				if(conveniosComunaComponentePagada != null && conveniosComunaComponentePagada.size() > 0){
					nuevo = false;
					int size = conveniosComunaComponentePagada.size();
					ConvenioComunaComponente convenioComunaComponente = conveniosComunaComponentePagada.get(size -1);
					idConvenioComuna = convenioComunaComponente.getConvenioComuna().getIdConvenioComuna();
					montoActual =  convenioComunaComponente.getMontoIngresado();
				}

				if(conveniosComunaComponenteEnTramite != null && conveniosComunaComponenteEnTramite.size() > 0){
					nuevo = false;
					int size = conveniosComunaComponenteEnTramite.size();
					ConvenioComunaComponente convenioComunaComponente = conveniosComunaComponenteEnTramite.get(size -1);
					idConvenioComuna = convenioComunaComponente.getConvenioComuna().getIdConvenioComuna();
					montoActual =  convenioComunaComponente.getMontoIngresado();
				}

				if(conveniosComunaComponenteAprobado != null && conveniosComunaComponenteAprobado.size() > 0){
					nuevo = false;
					int size = conveniosComunaComponenteAprobado.size();
					ConvenioComunaComponente convenioComunaComponente = conveniosComunaComponenteAprobado.get(size -1);
					idConvenioComuna = convenioComunaComponente.getConvenioComuna().getIdConvenioComuna();
					montoActual =  convenioComunaComponente.getMontoIngresado();
				}

				if(conveniosComunaComponenteIngresada != null && conveniosComunaComponenteIngresada.size() > 0){
					nuevo = false;
					int size = conveniosComunaComponenteIngresada.size();
					ConvenioComunaComponente convenioComunaComponente = conveniosComunaComponenteIngresada.get(size -1);
					idConvenioComuna = convenioComunaComponente.getConvenioComuna().getIdConvenioComuna();
					numeroResoucion = convenioComunaComponente.getConvenioComuna().getNumeroResolucion();
					montoActual =  convenioComunaComponente.getMontoIngresado();
				}
				CargaConvenioComunaComponenteVO cargaConvenioComunaComponenteVO = new CargaConvenioComunaComponenteVO();
				cargaConvenioComunaComponenteVO.setIdConvenioComuna(idConvenioComuna);
				cargaConvenioComunaComponenteVO.setNuevo(nuevo);
				ComponenteSummaryVO componenteSummaryVO = new ComponenteSummaryVO();
				componenteSummaryVO.setId(componente.getId());
				componenteSummaryVO.setNombre(componente.getNombre());
				cargaConvenioComunaComponenteVO.setComponente(componenteSummaryVO);
				cargaConvenioComunaComponenteVO.setIdComuna(comuna.getId());
				cargaConvenioComunaComponenteVO.setNombreComuna(comuna.getNombre());
				SubtituloSummaryVO subtituloSummaryVO = new SubtituloSummaryVO();
				subtituloSummaryVO.setIdSubtitulo(subtitulo.getIdTipoSubtitulo());
				subtituloSummaryVO.setNombre(subtitulo.getNombreSubtitulo());
				cargaConvenioComunaComponenteVO.setSubtitulo(subtituloSummaryVO);
				cargaConvenioComunaComponenteVO.setMontoIngresado(montoActual);
				cargaConvenioComunaComponenteVO.setNumeroResoucion(numeroResoucion);
				resolucionesConvenios.add(cargaConvenioComunaComponenteVO);
			}
		}else{
			Comuna comuna = comunaDAO.getComunaById(comunaSeleccionada);
			Componente componente = componenteDAO.getComponenteByID(componenteSeleccionado);
			TipoSubtitulo subtitulo = tipoSubtituloDAO.getTipoSubtituloById(Subtitulo.SUBTITULO24.getId());
			List<ConvenioComunaComponente> conveniosComunaComponenteIngresada = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getIdTipoSubtitulo(), comuna.getId(), EstadosConvenios.INGRESADO.getId());
			List<ConvenioComunaComponente> conveniosComunaComponenteAprobado = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getIdTipoSubtitulo(), comuna.getId(), EstadosConvenios.APROBADO.getId());
			List<ConvenioComunaComponente> conveniosComunaComponenteEnTramite = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getIdTipoSubtitulo(), comuna.getId(), EstadosConvenios.TRAMITE.getId());
			List<ConvenioComunaComponente> conveniosComunaComponentePagada = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getIdTipoSubtitulo(), comuna.getId(), EstadosConvenios.PAGADO.getId());
			boolean nuevo = true;
			Integer montoActual = 0;
			Integer idConvenioComuna = null;
			Integer numeroResoucion = null;

			if(conveniosComunaComponentePagada != null && conveniosComunaComponentePagada.size() > 0){
				nuevo = false;
				int size = conveniosComunaComponentePagada.size();
				ConvenioComunaComponente convenioComunaComponente = conveniosComunaComponentePagada.get(size -1);
				idConvenioComuna = convenioComunaComponente.getConvenioComuna().getIdConvenioComuna();
				montoActual =  convenioComunaComponente.getMontoIngresado();
			}

			if(conveniosComunaComponenteEnTramite != null && conveniosComunaComponenteEnTramite.size() > 0){
				nuevo = false;
				int size = conveniosComunaComponenteEnTramite.size();
				ConvenioComunaComponente convenioComunaComponente = conveniosComunaComponenteEnTramite.get(size -1);
				idConvenioComuna = convenioComunaComponente.getConvenioComuna().getIdConvenioComuna();
				montoActual =  convenioComunaComponente.getMontoIngresado();
			}

			if(conveniosComunaComponenteAprobado != null && conveniosComunaComponenteAprobado.size() > 0){
				nuevo = false;
				int size = conveniosComunaComponenteAprobado.size();
				ConvenioComunaComponente convenioComunaComponente = conveniosComunaComponenteAprobado.get(size -1);
				idConvenioComuna = convenioComunaComponente.getConvenioComuna().getIdConvenioComuna();
				montoActual =  convenioComunaComponente.getMontoIngresado();
			}

			if(conveniosComunaComponenteIngresada != null && conveniosComunaComponenteIngresada.size() > 0){
				nuevo = false;
				int size = conveniosComunaComponenteIngresada.size();
				ConvenioComunaComponente convenioComunaComponente = conveniosComunaComponenteIngresada.get(size -1);
				idConvenioComuna = convenioComunaComponente.getConvenioComuna().getIdConvenioComuna();
				numeroResoucion = convenioComunaComponente.getConvenioComuna().getNumeroResolucion();
				montoActual =  convenioComunaComponente.getMontoIngresado();
			}
			CargaConvenioComunaComponenteVO cargaConvenioComunaComponenteVO = new CargaConvenioComunaComponenteVO();
			cargaConvenioComunaComponenteVO.setNuevo(nuevo);
			cargaConvenioComunaComponenteVO.setIdConvenioComuna(idConvenioComuna);
			ComponenteSummaryVO componenteSummaryVO = new ComponenteSummaryVO();
			componenteSummaryVO.setId(componente.getId());
			componenteSummaryVO.setNombre(componente.getNombre());
			cargaConvenioComunaComponenteVO.setComponente(componenteSummaryVO);
			cargaConvenioComunaComponenteVO.setIdComuna(comuna.getId());
			cargaConvenioComunaComponenteVO.setNombreComuna(comuna.getNombre());
			SubtituloSummaryVO subtituloSummaryVO = new SubtituloSummaryVO();
			subtituloSummaryVO.setIdSubtitulo(subtitulo.getIdTipoSubtitulo());
			subtituloSummaryVO.setNombre(subtitulo.getNombreSubtitulo());
			cargaConvenioComunaComponenteVO.setSubtitulo(subtituloSummaryVO);
			cargaConvenioComunaComponenteVO.setMontoIngresado(montoActual);
			cargaConvenioComunaComponenteVO.setNumeroResoucion(numeroResoucion);
			resolucionesConvenios.add(cargaConvenioComunaComponenteVO);
		}
		return resolucionesConvenios;
	}

	public List<CargaConvenioServicioComponenteVO> getResolucionConveniosServicio(Integer idServicio, Integer idProgramaAno, Integer componenteSeleccionado, Integer establecimientoSeleccionado, Subtitulo subtituloSeleccionado) {
		List<CargaConvenioServicioComponenteVO> resolucionesConvenios = new ArrayList<CargaConvenioServicioComponenteVO>();
		ProgramaVO programa = programasService.getProgramaAno(idProgramaAno);
		if(establecimientoSeleccionado == null){
			List<Establecimiento> establecimientos = establecimientosDAO.getEstablecimientosByServicio(idServicio);
			Componente componente = componenteDAO.getComponenteByID(componenteSeleccionado);
			System.out.println("subtituloSeleccionado.getId()="+subtituloSeleccionado.getId()+" subtituloSeleccionado.getNombre()="+subtituloSeleccionado.getNombre());
			TipoSubtitulo subtitulo = tipoSubtituloDAO.getTipoSubtituloById(subtituloSeleccionado.getId());
			System.out.println("recuperado de la base de datps subtitulo.getId()="+subtitulo.getIdTipoSubtitulo()+" subtituloSeleccionado.getNombre()="+subtitulo.getNombreSubtitulo());
			for(Establecimiento establecimiento : establecimientos){
				List<ConvenioServicioComponente> conveniosServicioComponenteIngresada = conveniosDAO.getConveniosServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(idProgramaAno, componenteSeleccionado, subtitulo.getIdTipoSubtitulo(), establecimiento.getId(), EstadosConvenios.INGRESADO.getId());
				List<ConvenioServicioComponente> conveniosServicioComponenteAprobado = conveniosDAO.getConveniosServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(idProgramaAno, componenteSeleccionado, subtitulo.getIdTipoSubtitulo(), establecimiento.getId(), EstadosConvenios.APROBADO.getId());
				List<ConvenioServicioComponente> conveniosServicioComponenteEnTramite = conveniosDAO.getConveniosServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(idProgramaAno, componenteSeleccionado, subtitulo.getIdTipoSubtitulo(), establecimiento.getId(), EstadosConvenios.TRAMITE.getId());
				List<ConvenioServicioComponente> conveniosServicioComponentePagada = conveniosDAO.getConveniosServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(idProgramaAno, componenteSeleccionado, subtitulo.getIdTipoSubtitulo(), establecimiento.getId(), EstadosConvenios.PAGADO.getId());
				boolean nuevo = true;
				Integer montoActual = 0;
				Integer idConvenioServicio = null;

				if(conveniosServicioComponentePagada != null && conveniosServicioComponentePagada.size() > 0){
					nuevo = false;
					int size = conveniosServicioComponentePagada.size();
					ConvenioServicioComponente convenioServicioComponente = conveniosServicioComponentePagada.get(size -1);
					montoActual =  convenioServicioComponente.getMontoIngresado();
				}

				if(conveniosServicioComponenteEnTramite != null && conveniosServicioComponenteEnTramite.size() > 0){
					nuevo = false;
					int size = conveniosServicioComponenteEnTramite.size();
					ConvenioServicioComponente convenioServicioComponente = conveniosServicioComponenteEnTramite.get(size -1);
					montoActual =  convenioServicioComponente.getMontoIngresado();
				}

				if(conveniosServicioComponenteAprobado != null && conveniosServicioComponenteAprobado.size() > 0){
					nuevo = false;
					int size = conveniosServicioComponenteAprobado.size();
					ConvenioServicioComponente convenioServicioComponente = conveniosServicioComponenteAprobado.get(size -1);
					montoActual =  convenioServicioComponente.getMontoIngresado();
				}

				if(conveniosServicioComponenteIngresada != null && conveniosServicioComponenteIngresada.size() > 0){
					nuevo = false;
					int size = conveniosServicioComponenteIngresada.size();
					ConvenioServicioComponente convenioServicioComponente  = conveniosServicioComponenteIngresada.get(size -1);
					idConvenioServicio = convenioServicioComponente.getConvenioServicio().getIdConvenioServicio();
					montoActual =  convenioServicioComponente.getMontoIngresado();
				}

				CargaConvenioServicioComponenteVO cargaConvenioServicioComponenteVO = new CargaConvenioServicioComponenteVO();
				cargaConvenioServicioComponenteVO.setIdConvenioServicio(idConvenioServicio);
				cargaConvenioServicioComponenteVO.setNuevo(nuevo);
				ComponenteSummaryVO componenteSummaryVO = new ComponenteSummaryVO();
				componenteSummaryVO.setId(componente.getId());
				componenteSummaryVO.setNombre(componente.getNombre());
				cargaConvenioServicioComponenteVO.setComponente(componenteSummaryVO);
				cargaConvenioServicioComponenteVO.setIdEstablecimiento(establecimiento.getId());
				cargaConvenioServicioComponenteVO.setNombreEstablecimiento(establecimiento.getNombre());
				SubtituloSummaryVO subtituloSummaryVO = new SubtituloSummaryVO();
				subtituloSummaryVO.setIdSubtitulo(subtitulo.getIdTipoSubtitulo());
				subtituloSummaryVO.setNombre(subtitulo.getNombreSubtitulo());
				cargaConvenioServicioComponenteVO.setSubtitulo(subtituloSummaryVO);
				cargaConvenioServicioComponenteVO.setMontoIngresado(montoActual);
				resolucionesConvenios.add(cargaConvenioServicioComponenteVO);
			}
		}else{
			Establecimiento establecimiento = establecimientosDAO.getEstablecimientoById(establecimientoSeleccionado);
			Componente componente = componenteDAO.getComponenteByID(componenteSeleccionado);
			System.out.println("subtituloSeleccionado.getId()="+subtituloSeleccionado.getId()+" subtituloSeleccionado.getNombre()="+subtituloSeleccionado.getNombre());
			TipoSubtitulo subtitulo = tipoSubtituloDAO.getTipoSubtituloById(subtituloSeleccionado.getId());
			System.out.println("recuperado de la base de datps subtitulo.getId()="+subtitulo.getIdTipoSubtitulo()+" subtituloSeleccionado.getNombre()="+subtitulo.getNombreSubtitulo());
			List<ConvenioServicioComponente> conveniosServicioComponenteIngresada = conveniosDAO.getConveniosServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getIdTipoSubtitulo(), establecimiento.getId(), EstadosConvenios.INGRESADO.getId());
			List<ConvenioServicioComponente> conveniosServicioComponenteAprobado = conveniosDAO.getConveniosServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getIdTipoSubtitulo(), establecimiento.getId(), EstadosConvenios.APROBADO.getId());
			List<ConvenioServicioComponente> conveniosServicioComponenteEnTramite = conveniosDAO.getConveniosServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getIdTipoSubtitulo(), establecimiento.getId(), EstadosConvenios.TRAMITE.getId());
			List<ConvenioServicioComponente> conveniosServicioComponentePagada = conveniosDAO.getConveniosServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getIdTipoSubtitulo(), establecimiento.getId(), EstadosConvenios.PAGADO.getId());
			boolean nuevo = true;
			Integer montoActual = 0;
			Integer idConvenioServicio = null;
			Integer numeroResolucion = null;

			if(conveniosServicioComponentePagada != null && conveniosServicioComponentePagada.size() > 0){
				nuevo = false;
				int size = conveniosServicioComponentePagada.size();
				ConvenioServicioComponente convenioServicioComponente = conveniosServicioComponentePagada.get(size -1);
				idConvenioServicio = convenioServicioComponente.getConvenioServicio().getIdConvenioServicio();
				montoActual =  convenioServicioComponente.getMontoIngresado();
			}

			if(conveniosServicioComponenteEnTramite != null && conveniosServicioComponenteEnTramite.size() > 0){
				nuevo = false;
				int size = conveniosServicioComponenteEnTramite.size();
				ConvenioServicioComponente convenioServicioComponente = conveniosServicioComponenteEnTramite.get(size -1);
				idConvenioServicio = convenioServicioComponente.getConvenioServicio().getIdConvenioServicio();
				montoActual =  convenioServicioComponente.getMontoIngresado();
			}

			if(conveniosServicioComponenteAprobado != null && conveniosServicioComponenteAprobado.size() > 0){
				nuevo = false;
				int size = conveniosServicioComponenteAprobado.size();
				ConvenioServicioComponente convenioServicioComponente = conveniosServicioComponenteAprobado.get(size -1);
				idConvenioServicio = convenioServicioComponente.getConvenioServicio().getIdConvenioServicio();
				montoActual =  convenioServicioComponente.getMontoIngresado();
			}

			if(conveniosServicioComponenteIngresada != null && conveniosServicioComponenteIngresada.size() > 0){
				nuevo = false;
				int size = conveniosServicioComponenteIngresada.size();
				ConvenioServicioComponente convenioServicioComponente  = conveniosServicioComponenteIngresada.get(size -1);
				idConvenioServicio = convenioServicioComponente.getConvenioServicio().getIdConvenioServicio();
				montoActual =  convenioServicioComponente.getMontoIngresado();
				numeroResolucion = convenioServicioComponente.getConvenioServicio().getNumeroResolucion();
			}

			CargaConvenioServicioComponenteVO cargaConvenioServicioComponenteVO = new CargaConvenioServicioComponenteVO();
			cargaConvenioServicioComponenteVO.setNuevo(nuevo);
			cargaConvenioServicioComponenteVO.setIdConvenioServicio(idConvenioServicio);
			ComponenteSummaryVO componenteSummaryVO = new ComponenteSummaryVO();
			componenteSummaryVO.setId(componente.getId());
			componenteSummaryVO.setNombre(componente.getNombre());
			cargaConvenioServicioComponenteVO.setComponente(componenteSummaryVO);
			cargaConvenioServicioComponenteVO.setIdEstablecimiento(establecimiento.getId());
			cargaConvenioServicioComponenteVO.setNombreEstablecimiento(establecimiento.getNombre());
			SubtituloSummaryVO subtituloSummaryVO = new SubtituloSummaryVO();
			subtituloSummaryVO.setIdSubtitulo(subtitulo.getIdTipoSubtitulo());
			subtituloSummaryVO.setNombre(subtitulo.getNombreSubtitulo());
			cargaConvenioServicioComponenteVO.setSubtitulo(subtituloSummaryVO);
			cargaConvenioServicioComponenteVO.setMontoIngresado(montoActual);
			cargaConvenioServicioComponenteVO.setNumeroResoucion(numeroResolucion);
			resolucionesConvenios.add(cargaConvenioServicioComponenteVO);
		}
		return resolucionesConvenios;
	}

	public List<ResolucionConveniosServicioVO> getResolucionConveniosServicio(Integer idServicio, Integer idProgramaAno, Integer componenteSeleccionado, Integer establecimientoSeleccionado) {
		List<ResolucionConveniosServicioVO> resolucionesConvenios = new ArrayList<ResolucionConveniosServicioVO>();
		List<ConvenioServicio> convenios = null;
		if(componenteSeleccionado == null && establecimientoSeleccionado == null){
			List<Establecimiento> establecimientos = establecimientosDAO.getEstablecimientosByServicio(idServicio);
			List<Integer> idEstablecimientos = new ArrayList<Integer>();
			if(establecimientos != null && establecimientos.size() > 0){
				for(Establecimiento establecimiento : establecimientos){
					idEstablecimientos.add(establecimiento.getId());
				}
			}
			convenios = this.conveniosDAO.getByProgramaAnoEstablecimientos(idProgramaAno, idEstablecimientos);
		}else{
			if(componenteSeleccionado != null && establecimientoSeleccionado != null){
				List<Integer> idComponentes = new ArrayList<Integer>();
				idComponentes.add(componenteSeleccionado);
				List<Integer> idEstablecimientos = new ArrayList<Integer>();
				idEstablecimientos.add(establecimientoSeleccionado);
				convenios = this.conveniosDAO.getByProgramaAnoComponentesEstablecimientos(idProgramaAno, idComponentes, idEstablecimientos);
			} else {
				if(componenteSeleccionado == null){
					List<Integer> idEstablecimientos = new ArrayList<Integer>();
					idEstablecimientos.add(establecimientoSeleccionado);
					convenios = this.conveniosDAO.getByProgramaAnoEstablecimientos(idProgramaAno, idEstablecimientos);
				}else{
					List<Integer> idComponentes = new ArrayList<Integer>();
					idComponentes.add(componenteSeleccionado);
					convenios = this.conveniosDAO.getConvenioServicioByProgramaAnoComponentes(idProgramaAno, idComponentes);
				}
			}
		}
		if(convenios != null && convenios.size() > 0){
			for(ConvenioServicio convenioServicio : convenios){
				resolucionesConvenios.add(new ConvenioServicioMapper().getBasic(convenioServicio));
			}
		}
		return resolucionesConvenios;
	}

	private ConvenioComuna createConvenio(Integer idProgramaAno, Integer idComponente, Integer idSubtitulo, Integer idComuna){
		ConvenioComuna convenioComuna = new ConvenioComuna();
		Componente componente = componenteDAO.getComponenteByID(idComponente);
		Comuna comuna = comunaDAO.getComunaById(idComuna);
		convenioComuna.setIdComuna(comuna);
		ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idProgramaAno);
		convenioComuna.setIdPrograma(programaAno);
		TipoSubtitulo tipoSubtitulo = tipoSubtituloDAO.getTipoSubtituloById(idSubtitulo);
		ConvenioComunaComponente convenioComunaComponente = new ConvenioComunaComponente();
		convenioComunaComponente.setComponente(componente);
		convenioComunaComponente.setSubtitulo(tipoSubtitulo);
		return convenioComuna;
	}

	private ConvenioServicio createConvenioServicio(Integer idProgramaAno, Integer idComponente, Integer idSubtitulo, Integer idEstablecimiento) {
		ConvenioServicio convenioServicio = new ConvenioServicio();
		Componente componente = componenteDAO.getComponenteByID(idComponente);
		Establecimiento establecimiento = establecimientosDAO.getEstablecimientoById(idEstablecimiento);
		convenioServicio.setIdEstablecimiento(establecimiento);
		ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idProgramaAno);
		convenioServicio.setIdPrograma(programaAno);
		TipoSubtitulo tipoSubtitulo = tipoSubtituloDAO.getTipoSubtituloById(idSubtitulo);
		ConvenioServicioComponente convenioServicioComponente = new ConvenioServicioComponente();
		convenioServicioComponente.setComponente(componente);
		convenioServicioComponente.setSubtitulo(tipoSubtitulo);
		return convenioServicio;
	}


	public void crearConveniosMunicipal(Integer idServicio, Integer idProgramaAno) {
		Map<Integer, List<Integer>> componentesBySubtitulo = new HashMap<Integer, List<Integer>>();
		ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idProgramaAno);
		List<Comuna> comunas = comunaDAO.getComunasByServicio(idServicio);
		List<Integer> idComunas = new ArrayList<Integer>();
		if(comunas != null && comunas.size() > 0){
			for(Comuna comuna : comunas){
				idComunas.add(comuna.getId());
			}
		}
		if(programaAno.getProgramaComponentes() != null &&  programaAno.getProgramaComponentes().size() > 0){
			for(ProgramaComponente programaComponente : programaAno.getProgramaComponentes()){
				for(ComponenteSubtitulo componenteSubtitulo : programaComponente.getComponente().getComponenteSubtitulosComponente()){
					if(Subtitulo.SUBTITULO24.getId().equals(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo())){
						if(componentesBySubtitulo.get(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo()) == null){
							List<Integer> idComponentes = new ArrayList<Integer>();
							idComponentes.add(programaComponente.getComponente().getId());
							componentesBySubtitulo.put(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo(), idComponentes);
						}else{
							componentesBySubtitulo.get(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo()).add(programaComponente.getComponente().getId());
						}
					}
				}
			}
		}
		for (Map.Entry<Integer, List<Integer>> entry : componentesBySubtitulo.entrySet()) {
			Integer idSubtitulo = entry.getKey();
			List<Integer> idComponentes = entry.getValue();
			for(Integer idComponente : idComponentes){
				for(Integer idComuna : idComunas){
					ConvenioComuna convenioComuna = componenteDAO.getConvenioComunaByProgramaAnoComponenteSubtituloComuna(idProgramaAno, idComponente, idSubtitulo, idComuna);
					if(convenioComuna == null){
						convenioComuna = createConvenio(idProgramaAno, idComponente, idSubtitulo, idComuna);
						conveniosDAO.save(convenioComuna);
					}
				}
			}
		}

	}

	public void crearConveniosServicios(Integer idServicio, Integer idProgramaAno) {
		Map<Integer, List<Integer>> componentesBySubtitulo = new HashMap<Integer, List<Integer>>();
		ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idProgramaAno);
		List<Establecimiento> establecimientos = establecimientosDAO.getEstablecimientosByServicio(idServicio);
		List<Integer> idEstablecimientos = new ArrayList<Integer>();
		if(establecimientos != null && establecimientos.size() > 0){
			for(Establecimiento establecimiento : establecimientos){
				idEstablecimientos.add(establecimiento.getId());
			}
		}
		if(programaAno.getProgramaComponentes() != null &&  programaAno.getProgramaComponentes().size() > 0){
			for(ProgramaComponente programaComponente : programaAno.getProgramaComponentes()){
				for(ComponenteSubtitulo componenteSubtitulo : programaComponente.getComponente().getComponenteSubtitulosComponente()){
					if(!Subtitulo.SUBTITULO24.getId().equals(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo())){
						if(componentesBySubtitulo.get(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo()) == null){
							List<Integer> idComponentes = new ArrayList<Integer>();
							idComponentes.add(programaComponente.getComponente().getId());
							componentesBySubtitulo.put(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo(), idComponentes);
						}else{
							componentesBySubtitulo.get(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo()).add(programaComponente.getComponente().getId());
						}
					}
				}
			}
		}
		for (Map.Entry<Integer, List<Integer>> entry : componentesBySubtitulo.entrySet()) {
			Integer idSubtitulo = entry.getKey();
			List<Integer> idComponentes = entry.getValue();
			for(Integer idComponente : idComponentes){
				for(Integer idEstablecimiento : idEstablecimientos){
					ConvenioServicio convenioServicio = componenteDAO.getConvenioServicioByProgramaAnoComponenteSubtituloEstablecimiento(idProgramaAno, idComponente, idSubtitulo, idEstablecimiento);
					if(convenioServicio == null){
						convenioServicio = createConvenioServicio(idProgramaAno, idComponente, idSubtitulo, idEstablecimiento);
						conveniosDAO.save(convenioServicio);
					}
				}
			}
		}
	}

	public Integer crearInstanciaConvenio(String username) {
		System.out.println("usuario-->"+username);
		Usuario usuario = this.usuarioDAO.getUserByUsername(username);
		Mes mesEnCurso = mesDAO.getMesPorID(Integer.parseInt(getMesCurso(true)));
		return conveniosDAO.crearInstanciaConvenio(usuario, mesEnCurso);
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


	public void convenioPorPrograma(Integer idProgramaAno, Integer idConvenio) {
		ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idProgramaAno);
		Convenio convenio =  conveniosDAO.getConvenioById(idConvenio);
		if(convenio != null){
			convenio.setIdProgramaAno(programaAno);
			List<ConvenioServicio> conveniosServicio = conveniosDAO.getConveniosServicioPendientes(idProgramaAno, EstadosConvenios.INGRESADO.getId());
			if(conveniosServicio != null && conveniosServicio.size() > 0){
				for(ConvenioServicio convenioServicio : conveniosServicio){
					convenioServicio.setConvenio(convenio);
				}
			}
			List<ConvenioComuna> conveniosComuna = conveniosDAO.getConveniosComunaPendientes(idProgramaAno, EstadosConvenios.INGRESADO.getId());
			if(conveniosComuna != null && conveniosComuna.size() > 0){
				for(ConvenioComuna convenioComuna : conveniosComuna){
					convenioComuna.setConvenio(convenio);
				}
			}
		}
	}

	public List<ConvenioComunaComponenteVO> getConveniosComunaComponenteByProgramaAnoComponenteSubtituloServicioEstadoConvenio(Integer idProgramaAno, Integer idComponente, Integer idSubtitulo, Integer idServicio, EstadosConvenios estadoConvenio){
		List<ConvenioComunaComponenteVO> conveniosComunaComponenteVO = new ArrayList<ConvenioComunaComponenteVO>();
		List<ServicioSalud> servicios = null;
		if(idServicio == null){
			servicios = servicioSaludDAO.getServiciosOrderId();
		}else{
			servicios = new ArrayList<ServicioSalud>();
			ServicioSalud servicioSalud = servicioSaludDAO.getById(idServicio);
			servicios.add(servicioSalud);
		}
		
		for(ServicioSalud servicioSalud : servicios){
			System.out.println("idProgramaAno="+ idProgramaAno +" idComponente="+ idComponente +" idSubtitulo="+ idSubtitulo +" servicioSalud.getId()=" + servicioSalud.getId() + "  estadoConvenio.getId()=" + estadoConvenio.getId());
			List<ConvenioComunaComponente> results = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloServicioEstadoConvenio(idProgramaAno, idComponente, idSubtitulo, servicioSalud.getId(), estadoConvenio.getId());
			if(results != null && results.size() > 0){
				for(ConvenioComunaComponente convenioComunaComponente : results){
					if(convenioComunaComponente.getAprobado() == null){
						ConvenioComunaComponenteVO convenioComunaComponenteVO = new ConvenioComunaComponenteMapper().getBasic(convenioComunaComponente);
						System.out.println("convenioComunaComponente.getConvenioComuna().getIdComuna().getId()="+ convenioComunaComponente.getConvenioComuna().getIdComuna().getId() +" idProgramaAno="+ idProgramaAno +" idComponente="+ idComponente +" idSubtitulo=" + idSubtitulo);
						Long marcoPresupuestario = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(convenioComunaComponente.getConvenioComuna().getIdComuna().getId(), idProgramaAno, idComponente, idSubtitulo);
						System.out.println("marcoPresupuestario="+marcoPresupuestario);
						convenioComunaComponenteVO.setMarcoPresupuestario(marcoPresupuestario);
						Long montoPendiente = marcoPresupuestario - ((convenioComunaComponenteVO.getMonto() == null) ? 0 : convenioComunaComponenteVO.getMonto());
						convenioComunaComponenteVO.setMontoPendiente(montoPendiente.intValue());
						conveniosComunaComponenteVO.add(convenioComunaComponenteVO);
					}
				}
			}
		}
		return conveniosComunaComponenteVO;
	}

	public List<ConvenioComunaComponenteVO> getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(Integer idProgramaAno, Integer idComponente, Integer idSubtitulo, Integer idComuna, EstadosConvenios estadoConvenio){
		List<ConvenioComunaComponenteVO> conveniosComunaComponenteVO = new ArrayList<ConvenioComunaComponenteVO>();
		List<ConvenioComunaComponente> results = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, idComponente, idSubtitulo, idComuna, estadoConvenio.getId());
		if(results != null && results.size() > 0){
			for(ConvenioComunaComponente convenioComunaComponente : results){
				if(convenioComunaComponente.getAprobado() == null){
					ConvenioComunaComponenteVO convenioComunaComponenteVO = new ConvenioComunaComponenteMapper().getBasic(convenioComunaComponente);
					Long marcoPresupuestario = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(idComuna, idProgramaAno, idComponente, idSubtitulo);
					convenioComunaComponenteVO.setMarcoPresupuestario(marcoPresupuestario);
					Long montoPendiente = marcoPresupuestario - ((convenioComunaComponenteVO.getMonto() == null) ? 0 : convenioComunaComponenteVO.getMonto());
					convenioComunaComponenteVO.setMontoPendiente(montoPendiente.intValue());
					conveniosComunaComponenteVO.add(convenioComunaComponenteVO);
				}
			}
		}
		return conveniosComunaComponenteVO;
	}

	public List<ConvenioServicioComponenteVO> getConveniosServicioComponenteByProgramaAnoComponenteSubtituloServicioEstadoConvenio(Integer idProgramaAno, Integer idComponente, Integer idSubtitulo, Integer idServicio, EstadosConvenios estadoConvenio){
		List<ConvenioServicioComponenteVO> conveniosServicioComponenteVO = new ArrayList<ConvenioServicioComponenteVO>();
		List<ServicioSalud> servicios = null;
		if(idServicio == null){
			servicios = servicioSaludDAO.getServiciosOrderId();
		}else{
			servicios = new ArrayList<ServicioSalud>();
			ServicioSalud servicioSalud = servicioSaludDAO.getById(idServicio);
			servicios.add(servicioSalud);
		}
		for(ServicioSalud servicioSalud : servicios){
			System.out.println("idProgramaAno="+ idProgramaAno +" idComponente="+ idComponente +" idSubtitulo="+ idSubtitulo +" servicioSalud.getId()=" + servicioSalud.getId() + "  estadoConvenio.getId()=" + estadoConvenio.getId());
			List<ConvenioServicioComponente> results = conveniosDAO.getConveniosServicioComponenteByProgramaAnoComponenteSubtituloServicioEstadoConvenio(idProgramaAno, idComponente, idSubtitulo, servicioSalud.getId(), estadoConvenio.getId());
			if(results != null && results.size() > 0){
				for(ConvenioServicioComponente convenioServicioComponente : results){
					if(convenioServicioComponente.getAprobado() == null){
						ConvenioServicioComponenteVO convenioServicioComponenteVO =new ConvenioServicioComponenteMapper().getBasic(convenioServicioComponente);
						System.out.println("convenioServicioComponente.getConvenioServicio().getIdEstablecimiento().getId()="+ convenioServicioComponente.getConvenioServicio().getIdEstablecimiento().getId() +" idProgramaAno="+ idProgramaAno +" idComponente="+ idComponente +" idSubtitulo=" + idSubtitulo);
						Long marcoPresupuestario = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(convenioServicioComponente.getConvenioServicio().getIdEstablecimiento().getId(), idProgramaAno, idComponente, idSubtitulo);
						System.out.println("marcoPresupuestario="+marcoPresupuestario);
						convenioServicioComponenteVO.setMarcoPresupuestario(marcoPresupuestario);
						Long montoPendiente = marcoPresupuestario - ((convenioServicioComponenteVO.getMonto() == null) ? 0 : convenioServicioComponenteVO.getMonto());
						convenioServicioComponenteVO.setMontoPendiente(montoPendiente.intValue());
						conveniosServicioComponenteVO.add(convenioServicioComponenteVO);
					}
				}
			}
		}
		return conveniosServicioComponenteVO;
	}

	public List<ConvenioServicioComponenteVO> getConveniosServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(Integer idProgramaAno, Integer idComponente, Integer idSubtitulo, Integer idEstablecimiento, EstadosConvenios estadoConvenio){
		List<ConvenioServicioComponenteVO> conveniosServicioComponenteVO = new ArrayList<ConvenioServicioComponenteVO>();
		List<ConvenioServicioComponente> resutls = conveniosDAO.getConveniosServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(idProgramaAno, idComponente, idSubtitulo, idEstablecimiento, estadoConvenio.getId());
		if(resutls != null && resutls.size() > 0){
			for(ConvenioServicioComponente convenioServicioComponente : resutls){
				if(convenioServicioComponente.getAprobado() == null){
					ConvenioServicioComponenteVO convenioServicioComponenteVO = new ConvenioServicioComponenteMapper().getBasic(convenioServicioComponente);
					Long marcoPresupuestario = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(idEstablecimiento, idProgramaAno, idComponente, idSubtitulo);
					convenioServicioComponenteVO.setMarcoPresupuestario(marcoPresupuestario);
					Long montoPendiente = marcoPresupuestario - ((convenioServicioComponenteVO.getMonto() == null) ? 0 : convenioServicioComponenteVO.getMonto());
					convenioServicioComponenteVO.setMontoPendiente(montoPendiente.intValue());
					conveniosServicioComponenteVO.add(convenioServicioComponenteVO);
				}
			}
		}
		return conveniosServicioComponenteVO;
	}
	
	public ConvenioServicioComponenteVO getConvenioServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(Integer idProgramaAno, Integer idComponente, Integer idSubtitulo, Integer idEstablecimiento, EstadosConvenios estadoConvenio){
		ConvenioServicioComponenteVO convenioServicioComponenteVO = null;
		System.out.println("idProgramaAno="+ idProgramaAno +" idComponente="+ idComponente +" idSubtitulo="+ idSubtitulo +" idEstablecimiento=" + idEstablecimiento + "  estadoConvenio.getId()=" + estadoConvenio.getId());
		List<ConvenioServicioComponente> results = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(idProgramaAno, idComponente, idSubtitulo, idEstablecimiento, estadoConvenio.getId());
		if(results != null && results.size() > 0){
			int size = results.size();
			ConvenioServicioComponente convenioServicioComponente = results.get((size -1));
			convenioServicioComponenteVO = new ConvenioServicioComponenteMapper().getBasic(convenioServicioComponente);
			System.out.println("idEstablecimiento="+ idEstablecimiento +" idProgramaAno="+ idProgramaAno +" idComponente="+ idComponente +" idSubtitulo=" + idSubtitulo);
			Long marcoPresupuestario = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(idEstablecimiento, idProgramaAno, idComponente, idSubtitulo);
			System.out.println("marcoPresupuestario="+marcoPresupuestario);
			convenioServicioComponenteVO.setMarcoPresupuestario(marcoPresupuestario);
			Long montoPendiente = marcoPresupuestario - ((convenioServicioComponenteVO.getMonto() == null) ? 0 : convenioServicioComponenteVO.getMonto());
			convenioServicioComponenteVO.setMontoPendiente(montoPendiente.intValue());
		}
		return convenioServicioComponenteVO;
	}

	public ConvenioComunaComponenteVO getConvenioComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(Integer idProgramaAno, Integer idComponente, Integer idSubtitulo, Integer idComuna, EstadosConvenios estadoConvenio){
		ConvenioComunaComponenteVO convenioComunaComponenteVO = null;
		System.out.println("idProgramaAno="+ idProgramaAno +" idComponente="+ idComponente +" idSubtitulo="+ idSubtitulo +" idComuna=" + idComuna + "  estadoConvenio.getId()=" + estadoConvenio.getId());
		List<ConvenioComunaComponente> results = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, idComponente, idSubtitulo, idComuna, estadoConvenio.getId());
		if(results != null && results.size() > 0){
			int size = results.size();
			ConvenioComunaComponente convenioComunaComponente = results.get((size -1));
			convenioComunaComponenteVO = new ConvenioComunaComponenteMapper().getBasic(convenioComunaComponente);
			System.out.println("idComuna="+ idComuna +" idProgramaAno="+ idProgramaAno +" idComponente="+ idComponente +" idSubtitulo=" + idSubtitulo);
			Long marcoPresupuestario = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(idComuna, idProgramaAno, idComponente, idSubtitulo);
			System.out.println("marcoPresupuestario="+marcoPresupuestario);
			convenioComunaComponenteVO.setMarcoPresupuestario(marcoPresupuestario);
			Long montoPendiente = marcoPresupuestario - ((convenioComunaComponenteVO.getMonto() == null) ? 0 : convenioComunaComponenteVO.getMonto());
			convenioComunaComponenteVO.setMontoPendiente(montoPendiente.intValue());
		}
		return convenioComunaComponenteVO;
	}
	
	public boolean getConveniosPendientes(Integer idProgramaAno) {
		int count = conveniosDAO.getCountConveniosServicioPendientes(idProgramaAno, EstadosConvenios.INGRESADO.getId());
		if(count != 0){
			return true;
		}
		count = conveniosDAO.getCountConveniosComunaPendientes(idProgramaAno, EstadosConvenios.INGRESADO.getId());
		if(count != 0){
			return true;
		}
		return false;
	}

	public ConvenioServicioComponenteVO aprobarConvenioServicioComponente(Integer idConvenio, Integer idConvenioServicioComponente) {
		ConvenioServicioComponente convenioServicioComponenteSeleccionado = conveniosDAO.getConveniosServicioComponenteById(idConvenioServicioComponente);
		if(convenioServicioComponenteSeleccionado != null){
			convenioServicioComponenteSeleccionado.setAprobado(true);
			ConvenioServicio convenioServicio = convenioServicioComponenteSeleccionado.getConvenioServicio();
			Convenio convenio = conveniosDAO.findById(idConvenio);
			System.out.println("idConvenio="+idConvenio);
			convenioServicio.setConvenio(convenio);
			boolean aprobar = true;
			for(ConvenioServicioComponente convenioServicioComponente : convenioServicio.getConvenioServicioComponentes() ){
				if(convenioServicioComponente.getAprobado() == null || !convenioServicioComponente.getAprobado()){
					aprobar = false;
					break;
				}
			}
			if(aprobar){
				convenioServicio.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.APROBADO.getId()));
			}
		}
		return new ConvenioServicioComponenteMapper().getBasic(convenioServicioComponenteSeleccionado);
	}

	public ConvenioServicioComponenteVO rechazarConvenioServicioComponente(Integer idConvenio, Integer idConvenioServicioComponente) {
		ConvenioServicioComponente convenioServicioComponenteSeleccionado = conveniosDAO.getConveniosServicioComponenteById(idConvenioServicioComponente);
		if(convenioServicioComponenteSeleccionado != null){
			convenioServicioComponenteSeleccionado.setAprobado(false);
			ConvenioServicio convenioServicio = convenioServicioComponenteSeleccionado.getConvenioServicio();
			System.out.println("idConvenio="+idConvenio);
			Convenio convenio = conveniosDAO.findById(idConvenio);
			convenioServicio.setConvenio(convenio);
			convenioServicio.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.RECHAZADO.getId()));
		}
		return new ConvenioServicioComponenteMapper().getBasic(convenioServicioComponenteSeleccionado);
	}

	public ConvenioComunaComponenteVO aprobarConvenioComunaComponente(Integer idConvenio, Integer idConvenioServicioComponente) {
		ConvenioComunaComponente convenioComunaComponenteSeleccionado = conveniosDAO.getConveniosComunaComponenteById(idConvenioServicioComponente);
		if(convenioComunaComponenteSeleccionado != null){
			convenioComunaComponenteSeleccionado.setAprobado(true);
			ConvenioComuna convenioComuna = convenioComunaComponenteSeleccionado.getConvenioComuna();
			System.out.println("idConvenio="+idConvenio);
			Convenio convenio = conveniosDAO.findById(idConvenio);
			convenioComuna.setConvenio(convenio);
			boolean aprobar = true;
			for(ConvenioComunaComponente convenioComunaComponente : convenioComuna.getConvenioComunaComponentes()){
				if(convenioComunaComponente.getAprobado() == null || !convenioComunaComponente.getAprobado()){
					aprobar = false;
					break;
				}
			}
			if(aprobar){
				convenioComuna.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.APROBADO.getId()));
			}
		}
		return new ConvenioComunaComponenteMapper().getBasic(convenioComunaComponenteSeleccionado);
	}

	public ConvenioComunaComponenteVO rechazarConvenioComunaComponente(Integer idConvenio, Integer idConvenioConvenioComponente) {
		ConvenioComunaComponente convenioComunaComponenteSeleccionado = conveniosDAO.getConveniosComunaComponenteById(idConvenioConvenioComponente);
		if(convenioComunaComponenteSeleccionado != null){
			convenioComunaComponenteSeleccionado.setAprobado(false);
			ConvenioComuna convenioComuna = convenioComunaComponenteSeleccionado.getConvenioComuna();
			System.out.println("idConvenio="+idConvenio);
			Convenio convenio = conveniosDAO.findById(idConvenio);
			System.out.println("convenio="+convenio);
			convenioComuna.setConvenio(convenio);
			convenioComuna.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.RECHAZADO.getId()));
		}
		return new ConvenioComunaComponenteMapper().getBasic(convenioComunaComponenteSeleccionado);
	}

	public List<SeguimientoVO> getBitacora(Integer idConvenio, TareasSeguimiento tareaSeguimiento) {
		return seguimientoService.getBitacoraConvenio(idConvenio, tareaSeguimiento);
	}

	public Integer getPlantillaCorreo(TipoDocumentosProcesos plantilla) {
		Integer plantillaId = documentService.getPlantillaByType(plantilla);
		return documentService.getDocumentoIdByPlantillaId(plantillaId);
	}

	public Integer cargarPlantillaCorreo(TipoDocumentosProcesos plantilla, File file) {
		Integer plantillaId = documentService.getPlantillaByType(plantilla);
		String filename = "plantillaCorreoResolucionRetiro.xml";
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		filename = tmpDir + File.separator + filename;
		String contentType = mimemap.getContentType(filename.toLowerCase());
		if(plantillaId == null){
			try {
				BodyVO response = alfrescoService.uploadDocument(file, contentType, folderTemplateConvenio);
				System.out.println("response upload template --->"+response);
				plantillaId = documentService.createTemplate(plantilla, response.getNodeRef(), response.getFileName(), contentType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			plantillaId = documentService.getDocumentoIdByPlantillaId(plantillaId);
			try {
				BodyVO response = alfrescoService.uploadDocument(file, contentType, folderTemplateConvenio);
				System.out.println("response upload template --->"+response);
				documentService.updateDocumentTemplate(plantillaId, response.getNodeRef(), response.getFileName(), contentType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return plantillaId;
	}

	public Integer createSeguimientoConvenio(Integer idConvenio, TareasSeguimiento tareaSeguimiento, String subject, String body, String username, List<String> para,
			List<String> conCopia, List<String> conCopiaOculta, List<Integer> documentos, Integer ano) {
		String from = usuarioDAO.getEmailByUsername(username);
		if(from == null){
			throw new RuntimeException("Usuario no tiene un email válido");
		}
		List<ReferenciaDocumentoSummaryVO> documentosTmp = new ArrayList<ReferenciaDocumentoSummaryVO>();
		if(documentos != null && documentos.size() > 0){
			for(Integer referenciaDocumentoId : documentos){
				MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
				ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO = documentService.getDocumentSummary(referenciaDocumentoId);
				documentosTmp.add(referenciaDocumentoSummaryVO);
				String contenType= mimemap.getContentType(referenciaDocumentoSummaryVO.getPath().toLowerCase());
				BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummaryVO.getPath()), contenType, folderProcesoConvenio.replace("{ANO}", ano.toString()));
				System.out.println("response upload template --->"+response);
				documentService.updateDocumentTemplate(referenciaDocumentoId, response.getNodeRef(), response.getFileName(), contenType);
			}
		}
		Integer idSeguimiento = seguimientoService.createSeguimiento(tareaSeguimiento, subject, body, from, para, conCopia, conCopiaOculta, documentosTmp);
		Seguimiento seguimiento = seguimientoDAO.getSeguimientoById(idSeguimiento);
		return conveniosDAO.createSeguimiento(idConvenio, seguimiento);
	}

	public void moveToAlfresco(Integer idConvenio, Integer referenciaDocumentoId, TipoDocumentosProcesos tipoDocumento, Boolean lastVersion, Integer ano) {
		System.out.println("Buscando referenciaDocumentoId="+referenciaDocumentoId);
		System.out.println("Buscando idConvenio="+idConvenio);
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummary = documentService.getDocumentSummary(referenciaDocumentoId);
		System.out.println("Buscando referenciaDocumentoSummary="+referenciaDocumentoSummary);
		if(referenciaDocumentoSummary != null){
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contenType = mimemap.getContentType(referenciaDocumentoSummary.getPath().toLowerCase());
			BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummary.getPath()), contenType, folderProcesoConvenio.replace("{ANO}", ano.toString()));
			System.out.println("response upload template --->"+response);
			documentService.updateDocumentTemplate(referenciaDocumentoSummary.getId(), response.getNodeRef(), response.getFileName(), contenType);
			Convenio convenio = conveniosDAO.findById(idConvenio);
			documentService.createDocumentConvenio(convenio, tipoDocumento, referenciaDocumentoId, lastVersion);
		}
	}

	@Asynchronous
	public void notificarPorCorreo(Integer programaSeleccionado, Integer idConvenio) {
		Integer idPlantillaCorreo = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLACORREORESOLUCIONRETIRO);
		if(idPlantillaCorreo == null){
			throw new RuntimeException("No se puede crear plantilla correo notificación Resolución l, la plantilla no esta cargada");
		}
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryPlantillaCorreoVO = documentService.getDocumentByPlantillaId(idPlantillaCorreo);
		DocumentoVO documentoPlantillaCorreoVO = documentService.getDocument(referenciaDocumentoSummaryPlantillaCorreoVO.getId());
		String templatePlantillaCorreo = tmpDirDoc + File.separator + documentoPlantillaCorreoVO.getName();
		templatePlantillaCorreo = templatePlantillaCorreo.replace(" ", "");
		System.out.println("templatePlantillaCorreo template-->"+templatePlantillaCorreo);
		ProgramaAno programaAno = programasDAO.getProgramaAnoByID(programaSeleccionado);
		GeneradorXML generadorXMLPlantillaResolucionRebaja = new GeneradorXML(templatePlantillaCorreo);
		Email emailPLantilla = null;
		try {
			emailPLantilla = generadorXMLPlantillaResolucionRebaja.createObject(Email.class, documentoPlantillaCorreoVO.getContent());
		} catch (JAXBException e1) {
			e1.printStackTrace();
		}
		ReferenciaDocumentoSummaryVO referenciaDocumentoFinalSummaryVO = null;
		List<ReferenciaDocumentoSummaryVO> referenciasDocumentoSummaryVO = documentService.getVersionFinalConvenioByType(idConvenio, TipoDocumentosProcesos.RESOLUCIONRETIRO);
		if(referenciasDocumentoSummaryVO != null && referenciasDocumentoSummaryVO.size() > 0){
			for(ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO : referenciasDocumentoSummaryVO){
				String contentType = new MimetypesFileTypeMap().getContentType(referenciaDocumentoSummaryVO.getPath());
				System.out.println("contentType="+contentType+" archivo enviado por correo");
				if (contentType.equals("application/pdf")) {
					referenciaDocumentoFinalSummaryVO = referenciaDocumentoSummaryVO;
					break;
				}
				if(referenciaDocumentoSummaryVO.getPath().indexOf(".") != -1){
					String extension = referenciaDocumentoSummaryVO.getPath().substring(referenciaDocumentoSummaryVO.getPath().lastIndexOf(".") + 1, referenciaDocumentoSummaryVO.getPath().length());
					if("pdf".equalsIgnoreCase(extension)){
						referenciaDocumentoFinalSummaryVO = referenciaDocumentoSummaryVO;
						break;
					}
				}
				referenciaDocumentoFinalSummaryVO = referenciaDocumentoSummaryVO;
			}
		}
		try{
			if(referenciaDocumentoFinalSummaryVO != null){
				Institucion fonasa = institucionDAO.findById(Instituciones.FONASA.getId());
				if(fonasa != null && fonasa.getDirector() != null && fonasa.getDirector().getEmail() != null){
					List<EmailService.Adjunto> adjuntos = new ArrayList<EmailService.Adjunto>();
					DocumentoVO documentDocumentoRetiroVO = documentService.getDocument(referenciaDocumentoFinalSummaryVO.getId());
					String fileNameDocumentoConvenio = tmpDirDoc + File.separator + documentDocumentoRetiroVO.getName();
					GeneradorDocumento generadorDocumento = new GeneradorDocumento(fileNameDocumentoConvenio);
					generadorDocumento.saveContent(documentDocumentoRetiroVO.getContent());
					EmailService.Adjunto adjunto = new EmailService.Adjunto();
					adjunto.setDescripcion("Resolución Rebaja Aporte Estatal");
					adjunto.setName(documentDocumentoRetiroVO.getName());
					adjunto.setUrl((new File(fileNameDocumentoConvenio)).toURI().toURL());
					adjuntos.add(adjunto);
					List<String> to = new ArrayList<String>();
					ReporteEmailsDestinatarios destinatarioPara = new ReporteEmailsDestinatarios();
					to.add(fonasa.getDirector().getEmail().getValor());
					destinatarioPara.setDestinatario(fonasa.getDirector());
					List<String> cc = new ArrayList<String>();
					if(emailPLantilla != null && emailPLantilla.getAsunto() != null && emailPLantilla.getCuerpo() != null){
						emailService.sendMail(to, cc, null, emailPLantilla.getAsunto(), emailPLantilla.getCuerpo().replaceAll("(\r\n|\n)", "<br />"), adjuntos);
					}else{
						emailService.sendMail(to, cc, null, "Resolución Retiro", "Estimado " + fonasa.getDirector().getNombre() + " " + fonasa.getDirector().getApellidoPaterno() + " " + ((fonasa.getDirector().getApellidoMaterno() != null) ? fonasa.getDirector().getApellidoMaterno() : "") + ": <br /> <p> l</p>", adjuntos);
					}
					ReporteEmailsEnviados reporteEmailsEnviados = new ReporteEmailsEnviados();
					ReporteEmailsAdjuntos reporteEmailsAdjuntos = new ReporteEmailsAdjuntos();
					reporteEmailsEnviados.setFecha(new Date());
					reporteEmailsEnviados.setIdProgramaAno(programaAno);
					conveniosDAO.save(reporteEmailsEnviados);
					destinatarioPara.setReporteEmailsEnviado(reporteEmailsEnviados);
					destinatarioPara.setTipoDestinatario(new TipoDestinatario(TiposDestinatarios.PARA.getId()));
					conveniosDAO.save(destinatarioPara);
					ReferenciaDocumento referenciaDocumento = documentDAO.findById(referenciaDocumentoFinalSummaryVO.getId());
					reporteEmailsAdjuntos.setDocumento(referenciaDocumento);
					reporteEmailsAdjuntos.setReporteEmailsEnviado(reporteEmailsEnviados);
					conveniosDAO.save(reporteEmailsAdjuntos);
					ReporteEmailsConvenio reporteEmailsConvenio = new ReporteEmailsConvenio();
					Convenio convenio = conveniosDAO.findById(idConvenio);
					reporteEmailsConvenio.setConvenio(convenio);
					reporteEmailsConvenio.setReporteEmailsEnviados(reporteEmailsEnviados);
					conveniosDAO.save(reporteEmailsConvenio);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<ReporteEmailsEnviadosVO> getReporteCorreosByConvenio(Integer idConvenio) {
		List<ReporteEmailsEnviadosVO> reporteCorreos = new ArrayList<ReporteEmailsEnviadosVO>();
		List<ReporteEmailsConvenio> reportes = conveniosDAO.getReporteCorreosByConvenio(idConvenio);
		System.out.println("getReporteCorreosByConvenio reportes.size()=" + ((reportes == null) ? 0 : reportes.size()) );
		if(reportes != null && reportes.size() > 0){
			for(ReporteEmailsConvenio reporte : reportes){
				ReporteEmailsEnviadosVO correo = new ReporteEmailsEnviadosVO();
				if(reporte.getReporteEmailsEnviados().getIdServicio() != null){
					correo.setIdServicio(reporte.getReporteEmailsEnviados().getIdServicio().getId());
					correo.setNombreServicio(reporte.getReporteEmailsEnviados().getIdServicio().getNombre());
				}	
				correo.setFecha(reporte.getReporteEmailsEnviados().getFecha());
				correo.setFechaFormat(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(reporte.getReporteEmailsEnviados().getFecha()));
				Set<ReporteEmailsAdjuntos> adjuntos = reporte.getReporteEmailsEnviados().getReporteEmailsAdjuntosSet();
				if(adjuntos != null && adjuntos.size() > 0){
					List<AdjuntosVO> adjs = new ArrayList<AdjuntosVO>();
					for(ReporteEmailsAdjuntos adj : adjuntos){
						AdjuntosVO ad = new AdjuntosVO();
						ad.setId(adj.getDocumento().getId());
						ad.setNombre(adj.getDocumento().getPath());
						adjs.add(ad);
					}
					correo.setAdjuntos(adjs);
				}
				Set<ReporteEmailsDestinatarios> destinatarios = reporte.getReporteEmailsEnviados().getReporteEmailsDestinatariosSet();
				if(destinatarios != null && destinatarios.size() > 0){
					List<String> to = new ArrayList<String>();
					List<String> cc = new ArrayList<String>();
					for(ReporteEmailsDestinatarios destina : destinatarios){
						if( TiposDestinatarios.PARA.getId()  == destina.getTipoDestinatario().getIdTipoDestinatario().intValue()){
							String para = destina.getDestinatario().getEmail().getValor();
							to.add(para);
						}else if( TiposDestinatarios.CC.getId()  == destina.getTipoDestinatario().getIdTipoDestinatario().intValue()){
							String copia = destina.getDestinatario().getEmail().getValor();
							cc.add(copia);
						}
					}
					correo.setCc(cc);
					correo.setTo(to);
				}
				reporteCorreos.add(correo);
			}
		}
		return reporteCorreos;
	}

	public void moveConvenioToAlfresco(Integer referenciaDocumentoId, Integer ano) {
		System.out.println("Buscando referenciaDocumentoId="+referenciaDocumentoId);
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummary = documentService.getDocumentSummary(referenciaDocumentoId);
		System.out.println("Buscando referenciaDocumentoSummary="+referenciaDocumentoSummary);
		if(referenciaDocumentoSummary != null){
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contenType= mimemap.getContentType(referenciaDocumentoSummary.getPath().toLowerCase());
			BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummary.getPath()), contenType, folderProcesoConvenio.replace("{ANO}", ano.toString()));
			System.out.println("response upload template --->"+response);
			documentService.updateDocumentTemplate(referenciaDocumentoSummary.getId(), response.getNodeRef(), response.getFileName(), contenType);
		}
	}

	public CargaConvenioServicioComponenteVO guardarConvenioServicioComponente(Integer idProgramaAno, CargaConvenioServicioComponenteVO cargaConvenioServicioComponenteVO, Integer docConvenio) {
		CargaConvenioServicioComponenteVO cargaConvenioServicioComponenteVONuevo = null;
		long milliseconds = Calendar.getInstance().getTimeInMillis();
		
		if(cargaConvenioServicioComponenteVO.getNuevo()){
			ConvenioServicio convenioServicio = null;
			if(cargaConvenioServicioComponenteVO.getIdConvenioServicio() == null){
				List<ConvenioServicio> conveniosServicioEstablecimientoIngresado = conveniosDAO.getConvenioServicioByProgramaAnoEstablecimientoEstado(idProgramaAno, cargaConvenioServicioComponenteVO.getIdEstablecimiento(), EstadosConvenios.INGRESADO);
				if(conveniosServicioEstablecimientoIngresado != null && conveniosServicioEstablecimientoIngresado.size() > 0){
					int size = conveniosServicioEstablecimientoIngresado.size();
					convenioServicio = conveniosServicioEstablecimientoIngresado.get(size - 1);
				}
				if(convenioServicio == null){
					ProgramaAno programa = programasDAO.getProgramaAnoByID(idProgramaAno);
					Mes mes = mesDAO.getMesPorID(Integer.parseInt(getMesCurso(true)));
					Establecimiento establecimiento = establecimientosDAO.getEstablecimientoById(cargaConvenioServicioComponenteVO.getIdEstablecimiento());
					convenioServicio = new ConvenioServicio();
					convenioServicio.setFecha(new Date(milliseconds));
					convenioServicio.setFechaResolucion(cargaConvenioServicioComponenteVO.getFecha());
					convenioServicio.setNumeroResolucion(cargaConvenioServicioComponenteVO.getNumeroResoucion());
					convenioServicio.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.INGRESADO.getId()));
					convenioServicio.setIdEstablecimiento(establecimiento);
					convenioServicio.setIdPrograma(programa);
					convenioServicio.setMes(mes);
					ReferenciaDocumento documentoConvenio = documentDAO.findById(docConvenio);
					convenioServicio.setDocumentoConvenio(documentoConvenio);
					conveniosDAO.save(convenioServicio);
				}
			}else{
				convenioServicio = conveniosDAO.getConvenioServicioById(cargaConvenioServicioComponenteVO.getIdConvenioServicio());
			}
			ConvenioServicioComponente convenioServicioComponente = new ConvenioServicioComponente();
			convenioServicioComponente.setFecha(new Date(milliseconds));
			Componente componente = componenteDAO.getComponenteByID(cargaConvenioServicioComponenteVO.getComponente().getId());
			convenioServicioComponente.setComponente(componente);
			convenioServicioComponente.setConvenioServicio(convenioServicio);
			convenioServicioComponente.setMontoIngresado(cargaConvenioServicioComponenteVO.getMontoIngresado());
			convenioServicioComponente.setMonto(cargaConvenioServicioComponenteVO.getMontoIngresado());
			TipoSubtitulo subtitulo = tipoSubtituloDAO.getTipoSubtituloById(cargaConvenioServicioComponenteVO.getSubtitulo().getIdSubtitulo());
			convenioServicioComponente.setSubtitulo(subtitulo);
			conveniosDAO.save(convenioServicioComponente);
			cargaConvenioServicioComponenteVONuevo = new CargaConvenioServicioComponenteMapper().getBasic(convenioServicioComponente);
			cargaConvenioServicioComponenteVONuevo.setNuevo(false);
		}else{
			List<ConvenioServicioComponente> conveniosServicioComponenteIngresada = conveniosDAO.getConveniosServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(idProgramaAno, cargaConvenioServicioComponenteVO.getComponente().getId(), cargaConvenioServicioComponenteVO.getSubtitulo().getIdSubtitulo(), cargaConvenioServicioComponenteVO.getIdEstablecimiento(), EstadosConvenios.INGRESADO.getId());
			List<ConvenioServicioComponente> conveniosServicioComponenteAprobado = conveniosDAO.getConveniosServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(idProgramaAno, cargaConvenioServicioComponenteVO.getComponente().getId(), cargaConvenioServicioComponenteVO.getSubtitulo().getIdSubtitulo(), cargaConvenioServicioComponenteVO.getIdEstablecimiento(), EstadosConvenios.APROBADO.getId());
			List<ConvenioServicioComponente> conveniosServicioComponenteEnTramite = conveniosDAO.getConveniosServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(idProgramaAno, cargaConvenioServicioComponenteVO.getComponente().getId(), cargaConvenioServicioComponenteVO.getSubtitulo().getIdSubtitulo(), cargaConvenioServicioComponenteVO.getIdEstablecimiento(), EstadosConvenios.TRAMITE.getId());
			List<ConvenioServicioComponente> conveniosServicioComponentePagada = conveniosDAO.getConveniosServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(idProgramaAno, cargaConvenioServicioComponenteVO.getComponente().getId(), cargaConvenioServicioComponenteVO.getSubtitulo().getIdSubtitulo(), cargaConvenioServicioComponenteVO.getIdEstablecimiento(), EstadosConvenios.PAGADO.getId());
			Integer montoActual = 0;
			if(conveniosServicioComponentePagada != null && conveniosServicioComponentePagada.size() > 0){
				int size = conveniosServicioComponentePagada.size();
				ConvenioServicioComponente convenioServicioComponente = conveniosServicioComponentePagada.get(size -1);
				montoActual =  convenioServicioComponente.getMontoIngresado();
			}
			
			if(conveniosServicioComponenteEnTramite != null && conveniosServicioComponenteEnTramite.size() > 0){
				int size = conveniosServicioComponenteEnTramite.size();
				ConvenioServicioComponente convenioServicioComponente = conveniosServicioComponenteEnTramite.get(size -1);
				convenioServicioComponente.getConvenioServicio().setEstadoConvenio(new EstadoConvenio(EstadosConvenios.REEMPLAZADO.getId()));
			}
			
			if(conveniosServicioComponenteAprobado != null && conveniosServicioComponenteAprobado.size() > 0){
				int size = conveniosServicioComponenteAprobado.size();
				ConvenioServicioComponente convenioServicioComponente = conveniosServicioComponenteAprobado.get(size -1);
				convenioServicioComponente.getConvenioServicio().setEstadoConvenio(new EstadoConvenio(EstadosConvenios.REEMPLAZADO.getId()));
			}

			boolean existe = false;
			if(conveniosServicioComponenteIngresada != null && conveniosServicioComponenteIngresada.size() > 0){
				existe = true;
				int size = conveniosServicioComponenteIngresada.size();
				ConvenioServicioComponente convenioServicioComponenteActual = conveniosServicioComponenteIngresada.get(size -1);
				if(convenioServicioComponenteActual.getConvenioServicio().getNumeroResolucion().equals(cargaConvenioServicioComponenteVO.getNumeroResoucion())){
					convenioServicioComponenteActual.setFecha(new Date(milliseconds));
					convenioServicioComponenteActual.setMontoIngresado(cargaConvenioServicioComponenteVO.getMontoIngresado());
					convenioServicioComponenteActual.setMonto(cargaConvenioServicioComponenteVO.getMontoIngresado() - montoActual);
					cargaConvenioServicioComponenteVONuevo = new CargaConvenioServicioComponenteMapper().getBasic(convenioServicioComponenteActual);
					cargaConvenioServicioComponenteVONuevo.setNuevo(false);
				}else{
					convenioServicioComponenteActual.getConvenioServicio().setEstadoConvenio(new EstadoConvenio(EstadosConvenios.REEMPLAZADO.getId()));
					ProgramaAno programa = programasDAO.getProgramaAnoByID(idProgramaAno);
					Mes mes = mesDAO.getMesPorID(Integer.parseInt(getMesCurso(true)));
					Establecimiento establecimiento = establecimientosDAO.getEstablecimientoById(cargaConvenioServicioComponenteVO.getIdEstablecimiento());
					ConvenioServicio convenioServicio = new ConvenioServicio();
					convenioServicio.setFecha(new Date(milliseconds));
					convenioServicio.setFechaResolucion(cargaConvenioServicioComponenteVO.getFecha());
					convenioServicio.setNumeroResolucion(cargaConvenioServicioComponenteVO.getNumeroResoucion());
					convenioServicio.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.INGRESADO.getId()));
					convenioServicio.setIdEstablecimiento(establecimiento);
					convenioServicio.setIdPrograma(programa);
					convenioServicio.setMes(mes);
					ReferenciaDocumento documentoConvenio = documentDAO.findById(docConvenio);
					convenioServicio.setDocumentoConvenio(documentoConvenio);
					conveniosDAO.save(convenioServicio);
					ConvenioServicioComponente convenioServicioComponente = new ConvenioServicioComponente();
					convenioServicioComponente.setFecha(new Date(milliseconds));
					Componente componente = componenteDAO.getComponenteByID(cargaConvenioServicioComponenteVO.getComponente().getId());
					convenioServicioComponente.setComponente(componente);
					convenioServicioComponente.setConvenioServicio(convenioServicio);
					convenioServicioComponente.setMontoIngresado(cargaConvenioServicioComponenteVO.getMontoIngresado());
					convenioServicioComponente.setMonto(cargaConvenioServicioComponenteVO.getMontoIngresado() - montoActual);
					TipoSubtitulo subtitulo = tipoSubtituloDAO.getTipoSubtituloById(cargaConvenioServicioComponenteVO.getSubtitulo().getIdSubtitulo());
					convenioServicioComponente.setSubtitulo(subtitulo);
					conveniosDAO.save(convenioServicioComponente);
					cargaConvenioServicioComponenteVONuevo = new CargaConvenioServicioComponenteMapper().getBasic(convenioServicioComponente);
					cargaConvenioServicioComponenteVONuevo.setNuevo(false);
				}
			}
			if(!existe){
				ProgramaAno programa = programasDAO.getProgramaAnoByID(idProgramaAno);
				Mes mes = mesDAO.getMesPorID(Integer.parseInt(getMesCurso(true)));
				Establecimiento establecimiento = establecimientosDAO.getEstablecimientoById(cargaConvenioServicioComponenteVO.getIdEstablecimiento());
				ConvenioServicio convenioServicio = new ConvenioServicio();
				convenioServicio.setFecha(new Date(milliseconds));
				convenioServicio.setFechaResolucion(cargaConvenioServicioComponenteVO.getFecha());
				convenioServicio.setNumeroResolucion(cargaConvenioServicioComponenteVO.getNumeroResoucion());
				convenioServicio.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.INGRESADO.getId()));
				convenioServicio.setIdEstablecimiento(establecimiento);
				convenioServicio.setIdPrograma(programa);
				convenioServicio.setMes(mes);
				ReferenciaDocumento documentoConvenio = documentDAO.findById(docConvenio);
				convenioServicio.setDocumentoConvenio(documentoConvenio);
				conveniosDAO.save(convenioServicio);
				ConvenioServicioComponente convenioServicioComponente = new ConvenioServicioComponente();
				convenioServicioComponente.setFecha(new Date(milliseconds));
				Componente componente = componenteDAO.getComponenteByID(cargaConvenioServicioComponenteVO.getComponente().getId());
				convenioServicioComponente.setComponente(componente);
				convenioServicioComponente.setConvenioServicio(convenioServicio);
				convenioServicioComponente.setMontoIngresado(cargaConvenioServicioComponenteVO.getMontoIngresado());
				convenioServicioComponente.setMonto(cargaConvenioServicioComponenteVO.getMontoIngresado() - montoActual);
				TipoSubtitulo subtitulo = tipoSubtituloDAO.getTipoSubtituloById(cargaConvenioServicioComponenteVO.getSubtitulo().getIdSubtitulo());
				convenioServicioComponente.setSubtitulo(subtitulo);
				conveniosDAO.save(convenioServicioComponente);
				cargaConvenioServicioComponenteVONuevo = new CargaConvenioServicioComponenteMapper().getBasic(convenioServicioComponente);
				cargaConvenioServicioComponenteVONuevo.setNuevo(false);
			}
		}
		return cargaConvenioServicioComponenteVONuevo;
	}

	public CargaConvenioComunaComponenteVO guardarConvenioComunaComponente(Integer idProgramaAno, CargaConvenioComunaComponenteVO cargaConvenioComunaComponenteVO, Integer docConvenio) {
		CargaConvenioComunaComponenteVO cargaConvenioComunaComponenteVONuevo = null;
		long milliseconds = Calendar.getInstance().getTimeInMillis();
		if(cargaConvenioComunaComponenteVO.getNuevo()){
			ConvenioComuna convenioComuna = null;
			if(cargaConvenioComunaComponenteVO.getIdConvenioComuna() == null){
				ProgramaAno programa = programasDAO.getProgramaAnoByID(idProgramaAno);
				Mes mes = mesDAO.getMesPorID(Integer.parseInt(getMesCurso(true)));
				Comuna comuna = comunaDAO.getComunaById(cargaConvenioComunaComponenteVO.getIdComuna());
				convenioComuna = new ConvenioComuna();
				convenioComuna.setFecha(new Date(milliseconds));
				convenioComuna.setFechaResolucion(cargaConvenioComunaComponenteVO.getFecha());
				convenioComuna.setNumeroResolucion(cargaConvenioComunaComponenteVO.getNumeroResoucion());
				convenioComuna.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.INGRESADO.getId()));
				convenioComuna.setIdComuna(comuna);
				convenioComuna.setIdPrograma(programa);
				convenioComuna.setMes(mes);
				ReferenciaDocumento documentoConvenio = documentDAO.findById(docConvenio);
				convenioComuna.setDocumentoConvenio(documentoConvenio);
				conveniosDAO.save(convenioComuna);
			}else{
				convenioComuna = conveniosDAO.getConvenioComunaById(cargaConvenioComunaComponenteVO.getIdConvenioComuna());
			}
			ConvenioComunaComponente convenioComunaComponente = new ConvenioComunaComponente();
			convenioComunaComponente.setFecha(new Date(milliseconds));
			Componente componente = componenteDAO.getComponenteByID(cargaConvenioComunaComponenteVO.getComponente().getId());
			convenioComunaComponente.setComponente(componente);
			convenioComunaComponente.setConvenioComuna(convenioComuna);
			convenioComunaComponente.setMontoIngresado(cargaConvenioComunaComponenteVO.getMontoIngresado());
			convenioComunaComponente.setMonto(cargaConvenioComunaComponenteVO.getMontoIngresado());
			TipoSubtitulo subtitulo = tipoSubtituloDAO.getTipoSubtituloById(cargaConvenioComunaComponenteVO.getSubtitulo().getIdSubtitulo());
			convenioComunaComponente.setSubtitulo(subtitulo);
			conveniosDAO.save(convenioComunaComponente);
			cargaConvenioComunaComponenteVONuevo = new CargaConvenioComunaComponenteMapper().getBasic(convenioComunaComponente);
			cargaConvenioComunaComponenteVONuevo.setNuevo(false);
		}else{
			System.out.println("*********cargaConvenioComunaComponenteVO-->" + cargaConvenioComunaComponenteVO);
			List<ConvenioComunaComponente> conveniosComunaComponenteIngresada = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, cargaConvenioComunaComponenteVO.getComponente().getId(), cargaConvenioComunaComponenteVO.getSubtitulo().getIdSubtitulo(), cargaConvenioComunaComponenteVO.getIdComuna(), EstadosConvenios.INGRESADO.getId());
			List<ConvenioComunaComponente> conveniosComunaComponenteAprobado = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, cargaConvenioComunaComponenteVO.getComponente().getId(), cargaConvenioComunaComponenteVO.getSubtitulo().getIdSubtitulo(), cargaConvenioComunaComponenteVO.getIdComuna(), EstadosConvenios.APROBADO.getId());
			List<ConvenioComunaComponente> conveniosComunaComponenteEnTramite = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, cargaConvenioComunaComponenteVO.getComponente().getId(), cargaConvenioComunaComponenteVO.getSubtitulo().getIdSubtitulo(), cargaConvenioComunaComponenteVO.getIdComuna(), EstadosConvenios.TRAMITE.getId());
			List<ConvenioComunaComponente> conveniosComunaComponentePagada = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, cargaConvenioComunaComponenteVO.getComponente().getId(), cargaConvenioComunaComponenteVO.getSubtitulo().getIdSubtitulo(), cargaConvenioComunaComponenteVO.getIdComuna(), EstadosConvenios.PAGADO.getId());
			Integer montoActual = 0;
			if(conveniosComunaComponentePagada != null && conveniosComunaComponentePagada.size() > 0){
				int size = conveniosComunaComponentePagada.size();
				ConvenioComunaComponente convenioComunaComponente = conveniosComunaComponentePagada.get(size -1);
				montoActual =  convenioComunaComponente.getMontoIngresado();
			}
 
			if(conveniosComunaComponenteEnTramite != null && conveniosComunaComponenteEnTramite.size() > 0){
				int size = conveniosComunaComponenteEnTramite.size();
				ConvenioComunaComponente convenioComunaComponente = conveniosComunaComponenteEnTramite.get(size -1);
				convenioComunaComponente.getConvenioComuna().setEstadoConvenio(new EstadoConvenio(EstadosConvenios.REEMPLAZADO.getId()));
			}
			
			if(conveniosComunaComponenteAprobado != null && conveniosComunaComponenteAprobado.size() > 0){
				int size = conveniosComunaComponenteAprobado.size();
				ConvenioComunaComponente convenioComunaComponente = conveniosComunaComponenteAprobado.get(size -1);
				convenioComunaComponente.getConvenioComuna().setEstadoConvenio(new EstadoConvenio(EstadosConvenios.REEMPLAZADO.getId()));
			}
			
			boolean existe = false;
			if(conveniosComunaComponenteIngresada != null && conveniosComunaComponenteIngresada.size() > 0){
				existe = true;
				int size = conveniosComunaComponenteIngresada.size();
				ConvenioComunaComponente convenioComunaComponenteActual = conveniosComunaComponenteIngresada.get(size -1);
				if(convenioComunaComponenteActual.getConvenioComuna().getNumeroResolucion().equals(cargaConvenioComunaComponenteVO.getNumeroResoucion())){
					convenioComunaComponenteActual.setFecha(new Date(milliseconds));
					convenioComunaComponenteActual.setMontoIngresado(cargaConvenioComunaComponenteVO.getMontoIngresado());
					convenioComunaComponenteActual.setMonto(cargaConvenioComunaComponenteVO.getMontoIngresado() - montoActual);
					cargaConvenioComunaComponenteVONuevo = new CargaConvenioComunaComponenteMapper().getBasic(convenioComunaComponenteActual);
					cargaConvenioComunaComponenteVONuevo.setNuevo(false);
				}else{
					convenioComunaComponenteActual.getConvenioComuna().setEstadoConvenio(new EstadoConvenio(EstadosConvenios.REEMPLAZADO.getId()));
					ProgramaAno programa = programasDAO.getProgramaAnoByID(idProgramaAno);
					Mes mes = mesDAO.getMesPorID(Integer.parseInt(getMesCurso(true)));
					Comuna comuna = comunaDAO.getComunaById(cargaConvenioComunaComponenteVO.getIdComuna());
					ConvenioComuna convenioComuna = new ConvenioComuna();
					convenioComuna.setFecha(new Date(milliseconds));
					convenioComuna.setFechaResolucion(cargaConvenioComunaComponenteVO.getFecha());
					convenioComuna.setNumeroResolucion(cargaConvenioComunaComponenteVO.getNumeroResoucion());
					convenioComuna.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.INGRESADO.getId()));
					convenioComuna.setIdComuna(comuna);
					convenioComuna.setIdPrograma(programa);
					convenioComuna.setMes(mes);
					ReferenciaDocumento documentoConvenio = documentDAO.findById(docConvenio);
					convenioComuna.setDocumentoConvenio(documentoConvenio);
					conveniosDAO.save(convenioComuna);
					ConvenioComunaComponente convenioComunaComponente = new ConvenioComunaComponente();
					convenioComunaComponente.setFecha(new Date(milliseconds));
					Componente componente = componenteDAO.getComponenteByID(cargaConvenioComunaComponenteVO.getComponente().getId());
					convenioComunaComponente.setComponente(componente);
					convenioComunaComponente.setConvenioComuna(convenioComuna);
					convenioComunaComponente.setMontoIngresado(cargaConvenioComunaComponenteVO.getMontoIngresado());
					convenioComunaComponente.setMonto(cargaConvenioComunaComponenteVO.getMontoIngresado() - montoActual);
					TipoSubtitulo subtitulo = tipoSubtituloDAO.getTipoSubtituloById(cargaConvenioComunaComponenteVO.getSubtitulo().getIdSubtitulo());
					convenioComunaComponente.setSubtitulo(subtitulo);
					conveniosDAO.save(convenioComunaComponente);
					cargaConvenioComunaComponenteVONuevo = new CargaConvenioComunaComponenteMapper().getBasic(convenioComunaComponente);
					cargaConvenioComunaComponenteVONuevo.setNuevo(false);
				}
			}
			if(!existe){
				ProgramaAno programa = programasDAO.getProgramaAnoByID(idProgramaAno);
				Mes mes = mesDAO.getMesPorID(Integer.parseInt(getMesCurso(true)));
				Comuna comuna = comunaDAO.getComunaById(cargaConvenioComunaComponenteVO.getIdComuna());
				ConvenioComuna convenioComuna = new ConvenioComuna();
				convenioComuna.setFecha(new Date(milliseconds));
				convenioComuna.setFechaResolucion(cargaConvenioComunaComponenteVO.getFecha());
				convenioComuna.setNumeroResolucion(cargaConvenioComunaComponenteVO.getNumeroResoucion());
				convenioComuna.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.INGRESADO.getId()));
				convenioComuna.setIdComuna(comuna);
				convenioComuna.setIdPrograma(programa);
				convenioComuna.setMes(mes);
				ReferenciaDocumento documentoConvenio = documentDAO.findById(docConvenio);
				convenioComuna.setDocumentoConvenio(documentoConvenio);
				conveniosDAO.save(convenioComuna);
				ConvenioComunaComponente convenioComunaComponente = new ConvenioComunaComponente();
				convenioComunaComponente.setFecha(new Date(milliseconds));
				Componente componente = componenteDAO.getComponenteByID(cargaConvenioComunaComponenteVO.getComponente().getId());
				convenioComunaComponente.setComponente(componente);
				convenioComunaComponente.setConvenioComuna(convenioComuna);
				convenioComunaComponente.setMontoIngresado(cargaConvenioComunaComponenteVO.getMontoIngresado());
				convenioComunaComponente.setMonto(cargaConvenioComunaComponenteVO.getMontoIngresado() - montoActual);
				TipoSubtitulo subtitulo = tipoSubtituloDAO.getTipoSubtituloById(cargaConvenioComunaComponenteVO.getSubtitulo().getIdSubtitulo());
				convenioComunaComponente.setSubtitulo(subtitulo);
				conveniosDAO.save(convenioComunaComponente);
				cargaConvenioComunaComponenteVONuevo = new CargaConvenioComunaComponenteMapper().getBasic(convenioComunaComponente);
				cargaConvenioComunaComponenteVONuevo.setNuevo(false);
			}
		}
		return cargaConvenioComunaComponenteVONuevo;
	}

	public CargaConvenioComunaComponenteVO guardarLeyRetiro(Integer idProgramaAno, CargaConvenioComunaComponenteVO cargaConvenioComunaComponenteVO, Integer docConvenio) {
		CargaConvenioComunaComponenteVO cargaConvenioComunaComponenteVONuevo = null;
		long milliseconds = Calendar.getInstance().getTimeInMillis();
		if(cargaConvenioComunaComponenteVO.getNuevo()){
			ConvenioComuna convenioComuna = null;
			if(cargaConvenioComunaComponenteVO.getIdConvenioComuna() == null){
				ProgramaAno programa = programasDAO.getProgramaAnoByID(idProgramaAno);
				Mes mes = mesDAO.getMesPorID(Integer.parseInt(getMesCurso(true)));
				Comuna comuna = comunaDAO.getComunaById(cargaConvenioComunaComponenteVO.getIdComuna());
				convenioComuna = new ConvenioComuna();
				convenioComuna.setFecha(new Date(milliseconds));
				convenioComuna.setFechaResolucion(cargaConvenioComunaComponenteVO.getFecha());
				convenioComuna.setNumeroResolucion(cargaConvenioComunaComponenteVO.getNumeroResoucion());
				convenioComuna.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.INGRESADO.getId()));
				convenioComuna.setIdComuna(comuna);
				convenioComuna.setIdPrograma(programa);
				convenioComuna.setMes(mes);
				ReferenciaDocumento documentoConvenio = documentDAO.findById(docConvenio);
				convenioComuna.setDocumentoConvenio(documentoConvenio);
				conveniosDAO.save(convenioComuna);
			}else{
				convenioComuna = conveniosDAO.getConvenioComunaById(cargaConvenioComunaComponenteVO.getIdConvenioComuna());
			}
			ConvenioComunaComponente convenioComunaComponente = new ConvenioComunaComponente();
			convenioComunaComponente.setFecha(new Date(milliseconds));
			Componente componente = componenteDAO.getComponenteByID(cargaConvenioComunaComponenteVO.getComponente().getId());
			convenioComunaComponente.setComponente(componente);
			convenioComunaComponente.setConvenioComuna(convenioComuna);
			convenioComunaComponente.setMontoIngresado(cargaConvenioComunaComponenteVO.getMontoIngresado());
			convenioComunaComponente.setMonto(cargaConvenioComunaComponenteVO.getMontoIngresado());
			TipoSubtitulo subtitulo = tipoSubtituloDAO.getTipoSubtituloById(cargaConvenioComunaComponenteVO.getSubtitulo().getIdSubtitulo());
			convenioComunaComponente.setSubtitulo(subtitulo);
			conveniosDAO.save(convenioComunaComponente);
			cargaConvenioComunaComponenteVONuevo = new CargaConvenioComunaComponenteMapper().getBasic(convenioComunaComponente);
			cargaConvenioComunaComponenteVONuevo.setNuevo(false);
		}else{
			System.out.println("*********cargaConvenioComunaComponenteVO-->" + cargaConvenioComunaComponenteVO);
			List<ConvenioComunaComponente> conveniosComunaComponenteIngresada = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, cargaConvenioComunaComponenteVO.getComponente().getId(), cargaConvenioComunaComponenteVO.getSubtitulo().getIdSubtitulo(), cargaConvenioComunaComponenteVO.getIdComuna(), EstadosConvenios.INGRESADO.getId());
			List<ConvenioComunaComponente> conveniosComunaComponenteAprobado = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, cargaConvenioComunaComponenteVO.getComponente().getId(), cargaConvenioComunaComponenteVO.getSubtitulo().getIdSubtitulo(), cargaConvenioComunaComponenteVO.getIdComuna(), EstadosConvenios.APROBADO.getId());
			List<ConvenioComunaComponente> conveniosComunaComponenteEnTramite = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, cargaConvenioComunaComponenteVO.getComponente().getId(), cargaConvenioComunaComponenteVO.getSubtitulo().getIdSubtitulo(), cargaConvenioComunaComponenteVO.getIdComuna(), EstadosConvenios.TRAMITE.getId());
			List<ConvenioComunaComponente> conveniosComunaComponentePagada = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, cargaConvenioComunaComponenteVO.getComponente().getId(), cargaConvenioComunaComponenteVO.getSubtitulo().getIdSubtitulo(), cargaConvenioComunaComponenteVO.getIdComuna(), EstadosConvenios.PAGADO.getId());
			Integer montoActual = 0;
			if(conveniosComunaComponentePagada != null && conveniosComunaComponentePagada.size() > 0){
				int size = conveniosComunaComponentePagada.size();
				ConvenioComunaComponente convenioComunaComponente = conveniosComunaComponentePagada.get(size -1);
				montoActual =  convenioComunaComponente.getMontoIngresado();
			}
 
			if(conveniosComunaComponenteEnTramite != null && conveniosComunaComponenteEnTramite.size() > 0){
				int size = conveniosComunaComponenteEnTramite.size();
				ConvenioComunaComponente convenioComunaComponente = conveniosComunaComponenteEnTramite.get(size -1);
				convenioComunaComponente.getConvenioComuna().setEstadoConvenio(new EstadoConvenio(EstadosConvenios.REEMPLAZADO.getId()));
			}
			
			if(conveniosComunaComponenteAprobado != null && conveniosComunaComponenteAprobado.size() > 0){
				int size = conveniosComunaComponenteAprobado.size();
				ConvenioComunaComponente convenioComunaComponente = conveniosComunaComponenteAprobado.get(size -1);
				convenioComunaComponente.getConvenioComuna().setEstadoConvenio(new EstadoConvenio(EstadosConvenios.REEMPLAZADO.getId()));
			}
			
			boolean existe = false;
			if(conveniosComunaComponenteIngresada != null && conveniosComunaComponenteIngresada.size() > 0){
				existe = true;
				int size = conveniosComunaComponenteIngresada.size();
				ConvenioComunaComponente convenioComunaComponenteActual = conveniosComunaComponenteIngresada.get(size -1);
				if(convenioComunaComponenteActual.getConvenioComuna().getNumeroResolucion().equals(cargaConvenioComunaComponenteVO.getNumeroResoucion())){
					convenioComunaComponenteActual.setFecha(new Date(milliseconds));
					convenioComunaComponenteActual.setMontoIngresado(cargaConvenioComunaComponenteVO.getMontoIngresado());
					convenioComunaComponenteActual.setMonto(cargaConvenioComunaComponenteVO.getMontoIngresado() - montoActual);
					cargaConvenioComunaComponenteVONuevo = new CargaConvenioComunaComponenteMapper().getBasic(convenioComunaComponenteActual);
					cargaConvenioComunaComponenteVONuevo.setNuevo(false);
				}else{
					convenioComunaComponenteActual.getConvenioComuna().setEstadoConvenio(new EstadoConvenio(EstadosConvenios.REEMPLAZADO.getId()));
					ProgramaAno programa = programasDAO.getProgramaAnoByID(idProgramaAno);
					Mes mes = mesDAO.getMesPorID(Integer.parseInt(getMesCurso(true)));
					Comuna comuna = comunaDAO.getComunaById(cargaConvenioComunaComponenteVO.getIdComuna());
					ConvenioComuna convenioComuna = new ConvenioComuna();
					convenioComuna.setFecha(new Date(milliseconds));
					convenioComuna.setFechaResolucion(cargaConvenioComunaComponenteVO.getFecha());
					convenioComuna.setNumeroResolucion(cargaConvenioComunaComponenteVO.getNumeroResoucion());
					convenioComuna.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.INGRESADO.getId()));
					convenioComuna.setIdComuna(comuna);
					convenioComuna.setIdPrograma(programa);
					convenioComuna.setMes(mes);
					ReferenciaDocumento documentoConvenio = documentDAO.findById(docConvenio);
					convenioComuna.setDocumentoConvenio(documentoConvenio);
					conveniosDAO.save(convenioComuna);
					ConvenioComunaComponente convenioComunaComponente = new ConvenioComunaComponente();
					convenioComunaComponente.setFecha(new Date(milliseconds));
					Componente componente = componenteDAO.getComponenteByID(cargaConvenioComunaComponenteVO.getComponente().getId());
					convenioComunaComponente.setComponente(componente);
					convenioComunaComponente.setConvenioComuna(convenioComuna);
					convenioComunaComponente.setMontoIngresado(cargaConvenioComunaComponenteVO.getMontoIngresado());
					convenioComunaComponente.setMonto(cargaConvenioComunaComponenteVO.getMontoIngresado() - montoActual);
					TipoSubtitulo subtitulo = tipoSubtituloDAO.getTipoSubtituloById(cargaConvenioComunaComponenteVO.getSubtitulo().getIdSubtitulo());
					convenioComunaComponente.setSubtitulo(subtitulo);
					conveniosDAO.save(convenioComunaComponente);
					cargaConvenioComunaComponenteVONuevo = new CargaConvenioComunaComponenteMapper().getBasic(convenioComunaComponente);
					cargaConvenioComunaComponenteVONuevo.setNuevo(false);
				}
			}
			if(!existe){
				ProgramaAno programa = programasDAO.getProgramaAnoByID(idProgramaAno);
				Mes mes = mesDAO.getMesPorID(Integer.parseInt(getMesCurso(true)));
				Comuna comuna = comunaDAO.getComunaById(cargaConvenioComunaComponenteVO.getIdComuna());
				ConvenioComuna convenioComuna = new ConvenioComuna();
				convenioComuna.setFecha(new Date(milliseconds));
				convenioComuna.setFechaResolucion(cargaConvenioComunaComponenteVO.getFecha());
				convenioComuna.setNumeroResolucion(cargaConvenioComunaComponenteVO.getNumeroResoucion());
				convenioComuna.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.INGRESADO.getId()));
				convenioComuna.setIdComuna(comuna);
				convenioComuna.setIdPrograma(programa);
				convenioComuna.setMes(mes);
				ReferenciaDocumento documentoConvenio = documentDAO.findById(docConvenio);
				convenioComuna.setDocumentoConvenio(documentoConvenio);
				conveniosDAO.save(convenioComuna);
				ConvenioComunaComponente convenioComunaComponente = new ConvenioComunaComponente();
				convenioComunaComponente.setFecha(new Date(milliseconds));
				Componente componente = componenteDAO.getComponenteByID(cargaConvenioComunaComponenteVO.getComponente().getId());
				convenioComunaComponente.setComponente(componente);
				convenioComunaComponente.setConvenioComuna(convenioComuna);
				convenioComunaComponente.setMontoIngresado(cargaConvenioComunaComponenteVO.getMontoIngresado());
				convenioComunaComponente.setMonto(cargaConvenioComunaComponenteVO.getMontoIngresado() - montoActual);
				TipoSubtitulo subtitulo = tipoSubtituloDAO.getTipoSubtituloById(cargaConvenioComunaComponenteVO.getSubtitulo().getIdSubtitulo());
				convenioComunaComponente.setSubtitulo(subtitulo);
				conveniosDAO.save(convenioComunaComponente);
				cargaConvenioComunaComponenteVONuevo = new CargaConvenioComunaComponenteMapper().getBasic(convenioComunaComponente);
				cargaConvenioComunaComponenteVONuevo.setNuevo(false);
			}
		}
		return cargaConvenioComunaComponenteVONuevo;
	}

	public ReferenciaDocumentoSummaryVO getLastDocumentSummaryConvenioByType(Integer idConvenio, TipoDocumentosProcesos tipoDocumento) {
		return documentService.getLastDocumentoSummaryConvenioByTypeType(idConvenio, tipoDocumento);
	}

	public int countVersionFinalConvenioByType(Integer idConvenio, TipoDocumentosProcesos tipoDocumento) {
		List<ReferenciaDocumentoSummaryVO> versionesFinales = documentService.getVersionFinalConvenioByType(idConvenio, tipoDocumento);
		if(versionesFinales != null && versionesFinales.size() > 0){
			return versionesFinales.size();
		}
		return 0;
	}

	public Integer planillaMunicipalServicio(Integer programaSeleccionado, Integer idConvenio) {
		Integer planillaTrabajoId = null;
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		ProgramaVO programa = programasService.getProgramaAno(programaSeleccionado);
		String filename = tmpDir + File.separator + "planillaConvenioMunicipal " + programa.getNombre().replace(":", "") + ".xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());
		Subtitulo[] subtitulosServicio = {Subtitulo.SUBTITULO24};
		List<ComponentesVO> componentes = programasService.getComponentesByProgramaAnoSubtitulos(programa.getIdProgramaAno(), subtitulosServicio);
		List<Integer> idComponentes = new ArrayList<Integer>();
		GeneradorExcel generadorExcel = new GeneradorExcel(filename);
		List<CellExcelVO> header = new ArrayList<CellExcelVO>();
		List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
		header.add(new CellExcelVO(programa.getNombre(), (componentes.size() * 5) + 4, 1));
		header.add(new CellExcelVO("SERVICIOS DE SALUD", 2, 2));
		header.add(new CellExcelVO("COMUNAS", 2, 2));

		subHeader.add(new CellExcelVO("ID", 1, 1));
		subHeader.add(new CellExcelVO("Servicio de Salud", 1, 1));
		subHeader.add(new CellExcelVO("ID", 1, 1));
		subHeader.add(new CellExcelVO("Comuna", 1, 1));
		for(int i=0; i< componentes.size() ; i++){
			header.add(new CellExcelVO(componentes.get(i).getNombre().toUpperCase(), 5, 1));
			idComponentes.add(componentes.get(i).getId());
			header.add(new CellExcelVO("Subtítulo 24", 5, 1)); 
			subHeader.add(new CellExcelVO("Marco Presupuestario", 1, 1));
			subHeader.add(new CellExcelVO("Resolución", 1, 1));
			subHeader.add(new CellExcelVO("Fecha", 1, 1));
			subHeader.add(new CellExcelVO("Monto Convenio", 1, 1));
			subHeader.add(new CellExcelVO("Convenios Pendientes", 1, 1));
		}

		List<ServiciosVO> servicios = servicioSaludService.getServiciosOrderId();
		List<List<String>> items = new ArrayList<List<String>>();
		for(int i=0;i<servicios.size();i++){
			List<ComunaSummaryVO> comunas = servicios.get(i).getComunas();
			for(int j=0;j<comunas.size();j++){
				List<String> fila = new ArrayList<String>();
				fila.add(servicios.get(i).getIdServicio().toString());
				fila.add(servicios.get(i).getNombreServicio());
				fila.add(comunas.get(j).getId().toString());
				fila.add(comunas.get(j).getNombre());
				for(int contComponentes = 0; contComponentes< componentes.size() ; contComponentes++){
					for(SubtituloVO subtituloVO : componentes.get(contComponentes).getSubtitulos()){
						if(subtituloVO.getId().equals(Subtitulo.SUBTITULO24.getId())){
							List<ConvenioComunaComponenteVO> conveniosComunaComponente = getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(programaSeleccionado, componentes.get(contComponentes).getId(), subtituloVO.getId(), comunas.get(j).getId(), EstadosConvenios.INGRESADO);
							if(conveniosComunaComponente != null && conveniosComunaComponente.size() > 0){
								int size = conveniosComunaComponente.size();
								ConvenioComunaComponenteVO convenioComunaComponenteVO = conveniosComunaComponente.get(size-1);
								fila.add(convenioComunaComponenteVO.getMarcoPresupuestario().toString());
								fila.add(convenioComunaComponenteVO.getResolucion().toString());
								SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
								if(convenioComunaComponenteVO.getFecha() != null){
									fila.add(sdf.format(convenioComunaComponenteVO.getFecha()));
								}else{
									fila.add("");
								}
								fila.add(convenioComunaComponenteVO.getMonto().toString());
								fila.add(convenioComunaComponenteVO.getMontoPendiente().toString());
							}else{
								fila.add("0");
								fila.add("");
								fila.add("");
								fila.add("0");
								fila.add("0");
							}
						}
					}
				}
				items.add(fila);
			}
		}
		ExcelTemplate crearPlanillaConveniosProgramaSheetExcel = new CrearPlanillaConveniosProgramaSheetExcel(header, subHeader, items);
		generadorExcel.addSheet(crearPlanillaConveniosProgramaSheetExcel, "Planilla Convenio Municipal");
		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderProcesoConvenio.replace("{ANO}", programa.getAno().toString()));
			System.out.println("response crearPlanillaCumplimientoMunicialProgramaSheetExcel --->" + response);
			planillaTrabajoId = documentService.createDocumentConvenio(idConvenio, null, TipoDocumentosProcesos.PLANILLACONVENIOMUNICIPAL, response.getNodeRef(), response.getFileName(), contenType);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return planillaTrabajoId;
	}

	public Integer planillaEstablecimientoServicio(	Integer programaSeleccionado, Integer idConvenio) {
		Integer planillaTrabajoId = null;
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		ProgramaVO programa = programasService.getProgramaAno(programaSeleccionado);
		String filename = tmpDir + File.separator + "planillaConvenioServicio " + programa.getNombre().replace(":", "") + ".xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());
		Subtitulo[] subtitulosServicio = {Subtitulo.SUBTITULO21, Subtitulo.SUBTITULO22, Subtitulo.SUBTITULO29};
		List<ComponentesVO> componentes = programasService.getComponentesByProgramaAnoSubtitulos(programa.getIdProgramaAno(), subtitulosServicio);
		int totalSubtitulos = 0;
		for(ComponentesVO componentesVO : componentes){
			for(SubtituloVO subtituloVO : componentesVO.getSubtitulos()){
				if(subtituloVO.getId().equals(Subtitulo.SUBTITULO21.getId()) || subtituloVO.getId().equals(Subtitulo.SUBTITULO22.getId()) || subtituloVO.getId().equals(Subtitulo.SUBTITULO29.getId())){
					totalSubtitulos++;
				}
			}
		}

		GeneradorExcel generadorExcel = new GeneradorExcel(filename);
		List<CellExcelVO> header = new ArrayList<CellExcelVO>();
		List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
		header.add(new CellExcelVO(programa.getNombre(), (totalSubtitulos * 5) + 4, 1));
		header.add(new CellExcelVO("SERVICIOS DE SALUD", 2, 2));
		header.add(new CellExcelVO("ESTABLECIMIENTOS", 2, 2));

		subHeader.add(new CellExcelVO("ID", 1, 1));
		subHeader.add(new CellExcelVO("Servicio de Salud", 1, 1));
		subHeader.add(new CellExcelVO("ID", 1, 1));
		subHeader.add(new CellExcelVO("Establecimiento", 1, 1));
		for(int i=0; i< componentes.size() ; i++){
			header.add(new CellExcelVO(componentes.get(i).getNombre().toUpperCase(), 5, 1));
			for(SubtituloVO subtituloVO : componentes.get(i).getSubtitulos()){
				if(subtituloVO.getId().equals(Subtitulo.SUBTITULO21.getId()) || subtituloVO.getId().equals(Subtitulo.SUBTITULO22.getId()) || subtituloVO.getId().equals(Subtitulo.SUBTITULO29.getId())){
					header.add(new CellExcelVO(subtituloVO.getNombre(), 5, 1)); 
					subHeader.add(new CellExcelVO("Marco Presupuestario", 1, 1));
					subHeader.add(new CellExcelVO("Resolución", 1, 1));
					subHeader.add(new CellExcelVO("Fecha", 1, 1));
					subHeader.add(new CellExcelVO("Monto Convenio", 1, 1));
					subHeader.add(new CellExcelVO("Convenios Pendientes", 1, 1));
				}
			}
		}
		List<ServiciosVO> servicios = servicioSaludService.getServiciosOrderId();
		List<List<String>> items = new ArrayList<List<String>>();
		for(int i=0;i<servicios.size();i++){
			List<EstablecimientoSummaryVO> establecimientos = servicios.get(i).getEstableclimientos();
			for(int j=0;j<establecimientos.size();j++){
				List<String> fila = new ArrayList<String>();
				fila.add(servicios.get(i).getIdServicio() +"");
				fila.add(servicios.get(i).getNombreServicio());
				fila.add(establecimientos.get(j).getId().toString());
				fila.add(establecimientos.get(j).getNombre());
				for(int contComponentes = 0; contComponentes< componentes.size() ; contComponentes++){
					for(SubtituloVO subtituloVO : componentes.get(contComponentes).getSubtitulos()){
						if(subtituloVO.getId().equals(Subtitulo.SUBTITULO21.getId()) || subtituloVO.getId().equals(Subtitulo.SUBTITULO22.getId()) || subtituloVO.getId().equals(Subtitulo.SUBTITULO29.getId())){
							List<ConvenioServicioComponenteVO> conveniosServicioComponente = getConveniosServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(programaSeleccionado, componentes.get(contComponentes).getId(), subtituloVO.getId(), establecimientos.get(j).getId(), EstadosConvenios.INGRESADO);
							if(conveniosServicioComponente != null && conveniosServicioComponente.size() > 0){
								int size = conveniosServicioComponente.size();
								ConvenioServicioComponenteVO convenioServicioComponenteVO = conveniosServicioComponente.get(size-1);
								fila.add(convenioServicioComponenteVO.getMarcoPresupuestario().toString());
								fila.add(convenioServicioComponenteVO.getResolucion().toString());
								SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
								if(convenioServicioComponenteVO.getFecha() != null){
									fila.add(sdf.format(convenioServicioComponenteVO.getFecha()));
								}else{
									fila.add("");
								}
								fila.add(convenioServicioComponenteVO.getMonto().toString());
								fila.add(convenioServicioComponenteVO.getMontoPendiente().toString());
							}else{
								fila.add("0");
								fila.add("");
								fila.add("");
								fila.add("0");
								fila.add("0");
							}
						}
					}
				}
				items.add(fila);
			}
		}
		ExcelTemplate crearPlanillaConveniosProgramaSheetExcel = new CrearPlanillaConveniosProgramaSheetExcel(header, subHeader, items);
		generadorExcel.addSheet(crearPlanillaConveniosProgramaSheetExcel, "Planilla Convenio Municipal");
		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderProcesoConvenio.replace("{ANO}", programa.getAno().toString()));
			System.out.println("response crearPlanillaCumplimientoMunicialProgramaSheetExcel --->" + response);
			planillaTrabajoId = documentService.createDocumentConvenio(idConvenio, null, TipoDocumentosProcesos.PLANILLACONVENIOMUNICIPAL, response.getNodeRef(), response.getFileName(), contenType);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return planillaTrabajoId;
	}

	public List<ConvenioServicioComponenteVO> getConvenioServicioComponenteConvenioProgramaAnoEstado(Integer idConvenio, Integer idProgramaAno, EstadosConvenios estadoConvenio) {
		List<ConvenioServicioComponente>  conveniosServicio = conveniosDAO.getConvenioServicioComponenteByIdConvenioIdProgramaAnoEstado(idConvenio, idProgramaAno, estadoConvenio);
		List<ConvenioServicioComponenteVO> conveniosServicioComponenteVO = new ArrayList<ConvenioServicioComponenteVO>();
		for(ConvenioServicioComponente convenioServicioComponente : conveniosServicio){
			ConvenioServicioComponenteVO convenioServicioComponenteVO = new ConvenioServicioComponenteMapper().getBasic(convenioServicioComponente);
			conveniosServicioComponenteVO.add(convenioServicioComponenteVO);
		}
		return conveniosServicioComponenteVO;
	}

	public List<ConvenioComunaComponenteVO> getConvenioComunaComponenteConvenioProgramaAnoEstado(Integer idConvenio, Integer idProgramaAno, EstadosConvenios estadoConvenio) {
		List<ConvenioComunaComponente>  conveniosComuna = conveniosDAO.getConvenioComunaComponenteByIdConvenioIdProgramaAnoEstado(idConvenio, idProgramaAno, estadoConvenio);
		List<ConvenioComunaComponenteVO> conveniosComunaComponenteVO = new ArrayList<ConvenioComunaComponenteVO>();
		for(ConvenioComunaComponente convenioComunaComponente : conveniosComuna){
			ConvenioComunaComponenteVO convenioComunaComponenteVO = new ConvenioComunaComponenteMapper().getBasic(convenioComunaComponente);
			conveniosComunaComponenteVO.add(convenioComunaComponenteVO);
		}
		return conveniosComunaComponenteVO;
	}

	public void guardar(CargaConvenioComponenteSubtituloVO cargaConvenioComponenteSubtituloVO) {
		long currentDateMilliseconds = Calendar.getInstance().getTimeInMillis();
		if(cargaConvenioComponenteSubtituloVO.getDependenciaMuncipal()){
			List<ConvenioComuna> conveniosComunaIngresada = conveniosDAO.getConvenioComunaByProgramaAnoComunaEstado(cargaConvenioComponenteSubtituloVO.getPrograma().getIdProgramaAno() , cargaConvenioComponenteSubtituloVO.getItem(), EstadosConvenios.INGRESADO);
			List<ConvenioComuna> conveniosComunaAprobado = conveniosDAO.getConvenioComunaByProgramaAnoComunaEstado(cargaConvenioComponenteSubtituloVO.getPrograma().getIdProgramaAno(), cargaConvenioComponenteSubtituloVO.getItem(), EstadosConvenios.APROBADO);
			List<ConvenioComuna> conveniosComunaEnTramite = conveniosDAO.getConvenioComunaByProgramaAnoComunaEstado(cargaConvenioComponenteSubtituloVO.getPrograma().getIdProgramaAno(), cargaConvenioComponenteSubtituloVO.getItem(), EstadosConvenios.TRAMITE);
			List<ConvenioComuna> conveniosComunaPagada = conveniosDAO.getConvenioComunaByProgramaAnoComunaEstado(cargaConvenioComponenteSubtituloVO.getPrograma().getIdProgramaAno(), cargaConvenioComponenteSubtituloVO.getItem(), EstadosConvenios.PAGADO);
			
			if(conveniosComunaPagada != null && conveniosComunaPagada.size() > 0){
				int size = conveniosComunaPagada.size();
				ConvenioComuna convenioComuna = conveniosComunaPagada.get(size -1);
			}
			
			if(conveniosComunaEnTramite != null && conveniosComunaEnTramite.size() > 0){
				int size = conveniosComunaEnTramite.size();
				ConvenioComuna convenioComuna = conveniosComunaEnTramite.get(size -1);
				convenioComuna.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.REEMPLAZADO.getId()));
			}
			
			if(conveniosComunaAprobado != null && conveniosComunaAprobado.size() > 0){
				int size = conveniosComunaAprobado.size();
				ConvenioComuna convenioComuna = conveniosComunaAprobado.get(size -1);
				convenioComuna.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.REEMPLAZADO.getId()));
			}
			
			if(conveniosComunaIngresada != null && conveniosComunaIngresada.size() > 0){
				int size = conveniosComunaIngresada.size();
				ConvenioComuna convenioComuna = conveniosComunaIngresada.get(size -1);
				convenioComuna.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.REEMPLAZADO.getId()));
			}
			ConvenioComuna convenioComuna = new ConvenioComuna();
			convenioComuna.setFechaResolucion(cargaConvenioComponenteSubtituloVO.getFechaResolucion());
			ProgramaAno programaAno = programasDAO.getProgramaAnoByID(cargaConvenioComponenteSubtituloVO.getPrograma().getIdProgramaAno());
			Comuna comuna = comunaDAO.getComunaById(cargaConvenioComponenteSubtituloVO.getItem());
			convenioComuna.setIdPrograma(programaAno);
			convenioComuna.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.INGRESADO.getId()));
			convenioComuna.setFecha(new Date(currentDateMilliseconds));
			ReferenciaDocumento documento = documentDAO.findById(cargaConvenioComponenteSubtituloVO.getDocumento());
			convenioComuna.setDocumentoConvenio(documento);
			convenioComuna.setIdComuna(comuna);
			convenioComuna.setNumeroResolucion(cargaConvenioComponenteSubtituloVO.getNumeroResolucion());
			conveniosDAO.save(convenioComuna);
			for(ConvenioComponenteSubtituloVO convenioComponenteSubtituloVO : cargaConvenioComponenteSubtituloVO.getConvenioComponentesSubtitulosVO()){
				ConvenioComunaComponente convenioComunaComponente = new ConvenioComunaComponente();
				convenioComunaComponente.setConvenioComuna(convenioComuna);
				Componente componente = componenteDAO.getComponenteByID(convenioComponenteSubtituloVO.getComponente().getId());
				convenioComunaComponente.setComponente(componente);
				TipoSubtitulo subtitulo = tipoSubtituloDAO.getTipoSubtituloById(convenioComponenteSubtituloVO.getSubtitulo().getId());
				convenioComunaComponente.setSubtitulo(subtitulo);
				convenioComunaComponente.setMonto(convenioComponenteSubtituloVO.getMonto());
				convenioComunaComponente.setMontoIngresado(convenioComponenteSubtituloVO.getMonto());
				convenioComunaComponente.setFecha(new Date(currentDateMilliseconds));
				conveniosDAO.save(convenioComunaComponente);
			}
		}else{
			List<ConvenioServicio> conveniosServicioIngresada = conveniosDAO.getConvenioServicioByProgramaAnoEstablecimientoEstado(cargaConvenioComponenteSubtituloVO.getPrograma().getIdProgramaAno() , cargaConvenioComponenteSubtituloVO.getItem(), EstadosConvenios.INGRESADO);
			List<ConvenioServicio> conveniosServicioAprobado = conveniosDAO.getConvenioServicioByProgramaAnoEstablecimientoEstado(cargaConvenioComponenteSubtituloVO.getPrograma().getIdProgramaAno(), cargaConvenioComponenteSubtituloVO.getItem(), EstadosConvenios.APROBADO);
			List<ConvenioServicio> conveniosServicioEnTramite = conveniosDAO.getConvenioServicioByProgramaAnoEstablecimientoEstado(cargaConvenioComponenteSubtituloVO.getPrograma().getIdProgramaAno(), cargaConvenioComponenteSubtituloVO.getItem(), EstadosConvenios.TRAMITE);
			List<ConvenioServicio> conveniosServicioPagada = conveniosDAO.getConvenioServicioByProgramaAnoEstablecimientoEstado(cargaConvenioComponenteSubtituloVO.getPrograma().getIdProgramaAno(), cargaConvenioComponenteSubtituloVO.getItem(), EstadosConvenios.PAGADO);
			
			if(conveniosServicioPagada != null && conveniosServicioPagada.size() > 0){
				int size = conveniosServicioPagada.size();
				ConvenioServicio convenioServicio = conveniosServicioPagada.get(size -1);
			}
			
			if(conveniosServicioEnTramite != null && conveniosServicioEnTramite.size() > 0){
				int size = conveniosServicioEnTramite.size();
				ConvenioServicio convenioServicio = conveniosServicioEnTramite.get(size -1);
				convenioServicio.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.REEMPLAZADO.getId()));
			}
			
			if(conveniosServicioAprobado != null && conveniosServicioAprobado.size() > 0){
				int size = conveniosServicioAprobado.size();
				ConvenioServicio convenioServicio = conveniosServicioAprobado.get(size -1);
				convenioServicio.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.REEMPLAZADO.getId()));
			}
			
			if(conveniosServicioIngresada != null && conveniosServicioIngresada.size() > 0){
				int size = conveniosServicioIngresada.size();
				ConvenioServicio convenioServicio = conveniosServicioIngresada.get(size -1);
				convenioServicio.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.REEMPLAZADO.getId()));
			}
			
			ConvenioServicio convenioServicio = new ConvenioServicio();
			convenioServicio.setFechaResolucion(cargaConvenioComponenteSubtituloVO.getFechaResolucion());
			ProgramaAno programaAno = programasDAO.getProgramaAnoByID(cargaConvenioComponenteSubtituloVO.getPrograma().getIdProgramaAno());
			Establecimiento establecimiento = establecimientosDAO.getEstablecimientoById(cargaConvenioComponenteSubtituloVO.getItem());
			convenioServicio.setIdPrograma(programaAno);
			convenioServicio.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.INGRESADO.getId()));
			convenioServicio.setFecha(new Date(currentDateMilliseconds));
			ReferenciaDocumento documento = documentDAO.findById(cargaConvenioComponenteSubtituloVO.getDocumento());
			convenioServicio.setDocumentoConvenio(documento);
			convenioServicio.setIdEstablecimiento(establecimiento);
			convenioServicio.setNumeroResolucion(cargaConvenioComponenteSubtituloVO.getNumeroResolucion());
			conveniosDAO.save(convenioServicio);
			for(ConvenioComponenteSubtituloVO convenioComponenteSubtituloVO : cargaConvenioComponenteSubtituloVO.getConvenioComponentesSubtitulosVO()){
				ConvenioServicioComponente convenioServicioComponente = new ConvenioServicioComponente();
				convenioServicioComponente.setConvenioServicio(convenioServicio);
				Componente componente = componenteDAO.getComponenteByID(convenioComponenteSubtituloVO.getComponente().getId());
				convenioServicioComponente.setComponente(componente);
				TipoSubtitulo subtitulo = tipoSubtituloDAO.getTipoSubtituloById(convenioComponenteSubtituloVO.getSubtitulo().getId());
				convenioServicioComponente.setSubtitulo(subtitulo);
				convenioServicioComponente.setMonto(convenioComponenteSubtituloVO.getMonto());
				convenioServicioComponente.setMontoIngresado(convenioComponenteSubtituloVO.getMonto());
				convenioServicioComponente.setFecha(new Date(currentDateMilliseconds));
				conveniosDAO.save(convenioServicioComponente);
			}
		}
	}
	
	public void guardarConvenioModificatorio(CargaConvenioComponenteSubtituloVO cargaConvenioComponenteSubtituloVO) {
		long currentDateMilliseconds = Calendar.getInstance().getTimeInMillis();
		if(cargaConvenioComponenteSubtituloVO.getDependenciaMuncipal()){
			List<ConvenioComuna> conveniosComunaIngresada = conveniosDAO.getConvenioComunaByProgramaAnoComunaEstado(cargaConvenioComponenteSubtituloVO.getPrograma().getIdProgramaAno() , cargaConvenioComponenteSubtituloVO.getItem(), EstadosConvenios.INGRESADO);
			List<ConvenioComuna> conveniosComunaAprobado = conveniosDAO.getConvenioComunaByProgramaAnoComunaEstado(cargaConvenioComponenteSubtituloVO.getPrograma().getIdProgramaAno(), cargaConvenioComponenteSubtituloVO.getItem(), EstadosConvenios.APROBADO);
			List<ConvenioComuna> conveniosComunaEnTramite = conveniosDAO.getConvenioComunaByProgramaAnoComunaEstado(cargaConvenioComponenteSubtituloVO.getPrograma().getIdProgramaAno(), cargaConvenioComponenteSubtituloVO.getItem(), EstadosConvenios.TRAMITE);
			List<ConvenioComuna> conveniosComunaPagada = conveniosDAO.getConvenioComunaByProgramaAnoComunaEstado(cargaConvenioComponenteSubtituloVO.getPrograma().getIdProgramaAno(), cargaConvenioComponenteSubtituloVO.getItem(), EstadosConvenios.PAGADO);
			
			if(conveniosComunaPagada != null && conveniosComunaPagada.size() > 0){
				int size = conveniosComunaPagada.size();
				ConvenioComuna convenioComuna = conveniosComunaPagada.get(size -1);
			}
			
			if(conveniosComunaEnTramite != null && conveniosComunaEnTramite.size() > 0){
				int size = conveniosComunaEnTramite.size();
				ConvenioComuna convenioComuna = conveniosComunaEnTramite.get(size -1);
				convenioComuna.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.REEMPLAZADO.getId()));
			}
			
			if(conveniosComunaAprobado != null && conveniosComunaAprobado.size() > 0){
				int size = conveniosComunaAprobado.size();
				ConvenioComuna convenioComuna = conveniosComunaAprobado.get(size -1);
				convenioComuna.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.REEMPLAZADO.getId()));
			}
			
			if(conveniosComunaIngresada != null && conveniosComunaIngresada.size() > 0){
				int size = conveniosComunaIngresada.size();
				ConvenioComuna convenioComuna = conveniosComunaIngresada.get(size -1);
				convenioComuna.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.REEMPLAZADO.getId()));
			}
			ConvenioComuna convenioComuna = new ConvenioComuna();
			convenioComuna.setFechaResolucion(cargaConvenioComponenteSubtituloVO.getFechaResolucion());
			ProgramaAno programaAno = programasDAO.getProgramaAnoByID(cargaConvenioComponenteSubtituloVO.getPrograma().getIdProgramaAno());
			Comuna comuna = comunaDAO.getComunaById(cargaConvenioComponenteSubtituloVO.getItem());
			convenioComuna.setIdPrograma(programaAno);
			convenioComuna.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.INGRESADO.getId()));
			convenioComuna.setFecha(new Date(currentDateMilliseconds));
			ReferenciaDocumento documento = documentDAO.findById(cargaConvenioComponenteSubtituloVO.getDocumento());
			convenioComuna.setDocumentoConvenio(documento);
			convenioComuna.setIdComuna(comuna);
			convenioComuna.setNumeroResolucion(cargaConvenioComponenteSubtituloVO.getNumeroResolucion());
			ConvenioComuna convenioComunaInicial = conveniosDAO.getConvenioComunaById(cargaConvenioComponenteSubtituloVO.getConvenioInicial());
			if(convenioComunaInicial.getNumeroResolucionInicial() == null){
				convenioComuna.setNumeroResolucionInicial(convenioComunaInicial);
			}else{
				convenioComuna.setNumeroResolucionInicial(convenioComunaInicial.getNumeroResolucionInicial());
			}
			conveniosDAO.save(convenioComuna);
			for(ConvenioComponenteSubtituloVO convenioComponenteSubtituloVO : cargaConvenioComponenteSubtituloVO.getConvenioComponentesSubtitulosVO()){
				ConvenioComunaComponente convenioComunaComponente = new ConvenioComunaComponente();
				convenioComunaComponente.setConvenioComuna(convenioComuna);
				Componente componente = componenteDAO.getComponenteByID(convenioComponenteSubtituloVO.getComponente().getId());
				convenioComunaComponente.setComponente(componente);
				TipoSubtitulo subtitulo = tipoSubtituloDAO.getTipoSubtituloById(convenioComponenteSubtituloVO.getSubtitulo().getId());
				convenioComunaComponente.setSubtitulo(subtitulo);
				convenioComunaComponente.setMonto(convenioComponenteSubtituloVO.getMonto());
				convenioComunaComponente.setMontoIngresado(convenioComponenteSubtituloVO.getMonto());
				convenioComunaComponente.setFecha(new Date(currentDateMilliseconds));
				conveniosDAO.save(convenioComunaComponente);
			}
		}else{
			List<ConvenioServicio> conveniosServicioIngresada = conveniosDAO.getConvenioServicioByProgramaAnoEstablecimientoEstado(cargaConvenioComponenteSubtituloVO.getPrograma().getIdProgramaAno() , cargaConvenioComponenteSubtituloVO.getItem(), EstadosConvenios.INGRESADO);
			List<ConvenioServicio> conveniosServicioAprobado = conveniosDAO.getConvenioServicioByProgramaAnoEstablecimientoEstado(cargaConvenioComponenteSubtituloVO.getPrograma().getIdProgramaAno(), cargaConvenioComponenteSubtituloVO.getItem(), EstadosConvenios.APROBADO);
			List<ConvenioServicio> conveniosServicioEnTramite = conveniosDAO.getConvenioServicioByProgramaAnoEstablecimientoEstado(cargaConvenioComponenteSubtituloVO.getPrograma().getIdProgramaAno(), cargaConvenioComponenteSubtituloVO.getItem(), EstadosConvenios.TRAMITE);
			List<ConvenioServicio> conveniosServicioPagada = conveniosDAO.getConvenioServicioByProgramaAnoEstablecimientoEstado(cargaConvenioComponenteSubtituloVO.getPrograma().getIdProgramaAno(), cargaConvenioComponenteSubtituloVO.getItem(), EstadosConvenios.PAGADO);
			
			if(conveniosServicioPagada != null && conveniosServicioPagada.size() > 0){
				int size = conveniosServicioPagada.size();
				ConvenioServicio convenioServicio = conveniosServicioPagada.get(size -1);
			}
			
			if(conveniosServicioEnTramite != null && conveniosServicioEnTramite.size() > 0){
				int size = conveniosServicioEnTramite.size();
				ConvenioServicio convenioServicio = conveniosServicioEnTramite.get(size -1);
				convenioServicio.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.REEMPLAZADO.getId()));
			}
			
			if(conveniosServicioAprobado != null && conveniosServicioAprobado.size() > 0){
				int size = conveniosServicioAprobado.size();
				ConvenioServicio convenioServicio = conveniosServicioAprobado.get(size -1);
				convenioServicio.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.REEMPLAZADO.getId()));
			}
			
			if(conveniosServicioIngresada != null && conveniosServicioIngresada.size() > 0){
				int size = conveniosServicioIngresada.size();
				ConvenioServicio convenioServicio = conveniosServicioIngresada.get(size -1);
				convenioServicio.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.REEMPLAZADO.getId()));
			}
			
			ConvenioServicio convenioServicio = new ConvenioServicio();
			convenioServicio.setFechaResolucion(cargaConvenioComponenteSubtituloVO.getFechaResolucion());
			ProgramaAno programaAno = programasDAO.getProgramaAnoByID(cargaConvenioComponenteSubtituloVO.getPrograma().getIdProgramaAno());
			Establecimiento establecimiento = establecimientosDAO.getEstablecimientoById(cargaConvenioComponenteSubtituloVO.getItem());
			convenioServicio.setIdPrograma(programaAno);
			convenioServicio.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.INGRESADO.getId()));
			convenioServicio.setFecha(new Date(currentDateMilliseconds));
			ReferenciaDocumento documento = documentDAO.findById(cargaConvenioComponenteSubtituloVO.getDocumento());
			convenioServicio.setDocumentoConvenio(documento);
			convenioServicio.setIdEstablecimiento(establecimiento);
			convenioServicio.setNumeroResolucion(cargaConvenioComponenteSubtituloVO.getNumeroResolucion());
			ConvenioServicio convenioServicioInicial = conveniosDAO.getConvenioServicioById(cargaConvenioComponenteSubtituloVO.getConvenioInicial());
			if(convenioServicioInicial.getNumeroResolucionInicial() == null){
				convenioServicio.setNumeroResolucionInicial(convenioServicioInicial);
			}else{
				convenioServicio.setNumeroResolucionInicial(convenioServicioInicial.getNumeroResolucionInicial());
			}
			conveniosDAO.save(convenioServicio);
			for(ConvenioComponenteSubtituloVO convenioComponenteSubtituloVO : cargaConvenioComponenteSubtituloVO.getConvenioComponentesSubtitulosVO()){
				ConvenioServicioComponente convenioServicioComponente = new ConvenioServicioComponente();
				convenioServicioComponente.setConvenioServicio(convenioServicio);
				Componente componente = componenteDAO.getComponenteByID(convenioComponenteSubtituloVO.getComponente().getId());
				convenioServicioComponente.setComponente(componente);
				TipoSubtitulo subtitulo = tipoSubtituloDAO.getTipoSubtituloById(convenioComponenteSubtituloVO.getSubtitulo().getId());
				convenioServicioComponente.setSubtitulo(subtitulo);
				convenioServicioComponente.setMonto(convenioComponenteSubtituloVO.getMonto());
				convenioServicioComponente.setMontoIngresado(convenioComponenteSubtituloVO.getMonto());
				convenioServicioComponente.setFecha(new Date(currentDateMilliseconds));
				conveniosDAO.save(convenioServicioComponente);
			}
		}
	}
	
	public CargaConvenioComponenteSubtituloVO cargaConvenioComponenteSubtitulo(Integer numeroResolucion){
		ConvenioComuna convenioComuna = conveniosDAO.getConvenioComunaByNumeroResolucion(numeroResolucion);
		CargaConvenioComponenteSubtituloVO cargaConvenioComponenteSubtituloVO = null;
		if(convenioComuna != null){
			cargaConvenioComponenteSubtituloVO = new ConvenioComunaComponenteSubtituloMapper().getBasic(convenioComuna);
		}
		if(cargaConvenioComponenteSubtituloVO == null){
			ConvenioServicio convenioServicio = conveniosDAO.getConvenioServicioByNumeroResolucion(numeroResolucion);
			if(convenioServicio != null){
				cargaConvenioComponenteSubtituloVO = new ConvenioServicioComponenteSubtituloMapper().getBasic(convenioServicio);
			}
		}
		return cargaConvenioComponenteSubtituloVO;
	}

}
