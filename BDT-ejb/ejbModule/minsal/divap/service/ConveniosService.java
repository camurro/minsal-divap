package minsal.divap.service;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
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
import javax.xml.bind.JAXBException;

import minsal.divap.dao.CajaDAO;
import minsal.divap.dao.ComponenteDAO;
import minsal.divap.dao.ComunaDAO;
import minsal.divap.dao.ConveniosDAO;
import minsal.divap.dao.DocumentDAO;
import minsal.divap.dao.EstablecimientosDAO;
import minsal.divap.dao.MesDAO;
import minsal.divap.dao.ProgramasDAO;
import minsal.divap.dao.SeguimientoDAO;
import minsal.divap.dao.ServicioSaludDAO;
import minsal.divap.dao.TipoSubtituloDAO;
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.doc.GeneradorResolucionAporteEstatal;
import minsal.divap.doc.GeneradorWord;
import minsal.divap.enums.EstadosConvenios;
import minsal.divap.enums.EstadosProgramas;
import minsal.divap.enums.Subtitulo;
import minsal.divap.enums.TareasSeguimiento;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.enums.TiposDestinatarios;
import minsal.divap.model.mappers.CargaConvenioComunaComponenteMapper;
import minsal.divap.model.mappers.CargaConvenioServicioComponenteMapper;
import minsal.divap.model.mappers.ConvenioComunaComponenteMapper;
import minsal.divap.model.mappers.ConvenioServicioComponenteMapper;
import minsal.divap.model.mappers.ConvenioServicioMapper;
import minsal.divap.model.mappers.ProgramaMapper;
import minsal.divap.vo.AdjuntosVO;
import minsal.divap.vo.BodyVO;
import minsal.divap.vo.CargaConvenioComunaComponenteVO;
import minsal.divap.vo.CargaConvenioServicioComponenteVO;
import minsal.divap.vo.ComponenteSummaryVO;
import minsal.divap.vo.ConvenioComunaComponenteVO;
import minsal.divap.vo.ConvenioDocumentoVO;
import minsal.divap.vo.ConvenioServicioComponenteVO;
import minsal.divap.vo.ConveniosVO;
import minsal.divap.vo.DocumentoVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.ReporteEmailsEnviadosVO;
import minsal.divap.vo.ResolucionConveniosServicioVO;
import minsal.divap.vo.SeguimientoVO;
import minsal.divap.vo.SubtituloSummaryVO;
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
import cl.minsal.divap.model.Mes;
import cl.minsal.divap.model.ProgramaAno;
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
	private SeguimientoDAO seguimientoDAO;
	@EJB
	private ProgramasService programasService;
	@EJB
	private DocumentService documentService;
	@EJB
	private AlfrescoService alfrescoService;
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

	public void moveConvenioComunaToAlfresco(Integer idConvenioComuna, Integer referenciaDocumentoId, TipoDocumentosProcesos tipoDocumento) {
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummary = documentService.getDocumentSummary(referenciaDocumentoId);
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String contenType= mimemap.getContentType(referenciaDocumentoSummary.getPath().toLowerCase());

		BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummary.getPath()), contenType, folderProcesoConvenio.replace("{ANO}", getAnoCurso().toString()));
		System.out.println("response upload template --->"+response);
		documentService.updateDocumentTemplate(referenciaDocumentoSummary.getId(), response.getNodeRef(), response.getFileName(), contenType);
		ConvenioComuna convenioComuna = conveniosDAO.findConvenioComunaById(idConvenioComuna);
		documentService.createDocumentConvenioComuna(convenioComuna, tipoDocumento, referenciaDocumentoId);
	}

	public void moveConvenioServicioToAlfresco(Integer idConvenioServicio, Integer referenciaDocumentoId, TipoDocumentosProcesos tipoDocumento) {
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummary = documentService.getDocumentSummary(referenciaDocumentoId);
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String contenType= mimemap.getContentType(referenciaDocumentoSummary.getPath().toLowerCase());

		BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummary.getPath()), contenType, folderProcesoConvenio.replace("{ANO}", getAnoCurso().toString()));
		System.out.println("response upload template --->"+response);
		documentService.updateDocumentTemplate(referenciaDocumentoSummary.getId(), response.getNodeRef(), response.getFileName(), contenType);
		ConvenioServicio convenioServicio = conveniosDAO.findConvenioServicioById(idConvenioServicio);
		documentService.createDocumentConvenioServicio(convenioServicio, tipoDocumento, referenciaDocumentoId);
	}

	private Integer getAnoCurso() {
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
		Collection<Componente> con = Convenios.getIdPrograma().getPrograma().getComponentes();
		for (Componente conve : con){
			componentesConvenios.setComponente(conve.getNombre());
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

	public List<ProgramaVO> getProgramasByUserAno(String username) {
		List<ProgramaAno> programas = this.programasDAO.getProgramasByUserAno(username, getAnoCurso());
		List<ProgramaVO> result = new ArrayList<ProgramaVO>();
		if(programas != null && programas.size() > 0){
			for(ProgramaAno programa : programas){
				result.add(new ProgramaMapper().getBasic(programa));
			}
		}
		return result;
	}

	public void cambiarEstadoPrograma(Integer programaSeleccionado, EstadosProgramas estadoPrograma) {
		System.out.println("programaSeleccionado-->" + programaSeleccionado + " estadoPrograma.getId()-->"+estadoPrograma.getId());
		ProgramaAno programaAno = programasDAO.getProgramaAnoByID(programaSeleccionado);
		programaAno.setEstadoConvenio(new EstadoPrograma(estadoPrograma.getId()));
		System.out.println("Cambia estado ok");
	}

	public void notificarServicioSalud(Integer programaSeleccionado, Integer servicioSeleccionado, Integer idConvenio) {
		System.out.println("ConveniosService::notificarServicioSalud programaSeleccionado->"+programaSeleccionado);
		System.out.println("ConveniosService::notificarServicioSalud servicioSeleccionado->"+servicioSeleccionado);
		System.out.println("ConveniosService::notificarServicioSalud idConvenio->"+idConvenio);
		ServicioSalud servicioSalud = servicioSaludDAO.getById(servicioSeleccionado);
		ProgramaAno programaAno = programasDAO.getProgramaAnoByID(programaSeleccionado);
		Convenio convenio =  conveniosDAO.getConvenioById(idConvenio);
		if(servicioSalud != null){
			List<String> to = new ArrayList<String>();
			List<String> cc = new ArrayList<String>();
			String asunto = "Existen Reparos en la Información del Convenio";
			StringBuilder contenido = new StringBuilder();
			contenido.append("Estimado:");
			contenido.append("<p>");
			Integer numeroResulucion = -1;
			Set<ConvenioComuna> conveniosComuna = convenio.getConveniosComuna();
			if(conveniosComuna != null && conveniosComuna.size()  > 0){
				for(ConvenioComuna convenioComuna : conveniosComuna){
					numeroResulucion = convenioComuna.getNumeroResolucion();
					break;
				}
			}
			Set<ConvenioServicio> conveniosServicio = convenio.getConveniosServicio();
			if(conveniosServicio != null && conveniosServicio.size()  > 0){
				for(ConvenioServicio convenioServicio : conveniosServicio){
					numeroResulucion = convenioServicio.getNumeroResolucion();
					break;
				}
			}
			contenido.append("Existen reparos en la información del convenio con número de resolución ").append(numeroResulucion).append("<br/>");
			contenido.append("para la linea programática  ").append(programaAno.getPrograma().getNombre()).append("<br/>");
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

	public void generarResolucionDisponibilizarAlfresco(Integer programaSeleccionado, Integer idConvenio) {
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

			List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
			if(servicios != null && servicios.size() > 0){
				Map<String, Object> parametersResolucionRetiro = new HashMap<String, Object>();
				for(ServicioSalud servicio : servicios){
					String filenameResolucionRetiro = tmpDirDoc + File.separator + new Date().getTime() + "_" + "ResolucionRetiro" + servicio.getNombre() + ".docx";
					String contentTypeResolucionRetiro = mimemap.getContentType(filenameResolucionRetiro.toLowerCase());
					GeneradorResolucionAporteEstatal generadorWordResolucionRetiro = new GeneradorResolucionAporteEstatal(filenameResolucionRetiro, templateResolucionRetiro);
					generadorWordResolucionRetiro.replaceValues(parametersResolucionRetiro, XWPFDocument.class);
					BodyVO responseBorradorResolucionRetiro = alfrescoService.uploadDocument(new File(filenameResolucionRetiro), contentTypeResolucionRetiro, folderProcesoConvenio.replace("{ANO}", getAnoCurso().toString()));
					System.out.println("response responseBorradorResolucionRebaja --->"+responseBorradorResolucionRetiro);
					plantillaIdResolucionRetiro = documentService.createDocumentConvenio(idConvenio, servicio.getId(), TipoDocumentosProcesos.RESOLUCIONRETIRO, responseBorradorResolucionRetiro.getNodeRef(), responseBorradorResolucionRetiro.getFileName(), contentTypeResolucionRetiro);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Docx4JException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
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
		if(componenteSeleccionado == null && comunaSeleccionada == null){
			List<Comuna> comunas = comunaDAO.getComunasByServicio(idServicio);
			List<Componente> componentes = componenteDAO.getComponentesByIdProgramaIdSubtitulos(programa.getId(), Subtitulo.SUBTITULO24);
			TipoSubtitulo subtitulo = tipoSubtituloDAO.getTipoSubtituloById(Subtitulo.SUBTITULO24.getId());
			for(Comuna comuna : comunas){
				for(Componente componente : componentes){
					List<ConvenioComunaComponente> conveniosComunaComponenteIngresada = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getIdTipoSubtitulo(), comuna.getId(), EstadosConvenios.INGRESADO.getId());
					List<ConvenioComunaComponente> conveniosComunaComponenteAprobado = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getIdTipoSubtitulo(), comuna.getId(), EstadosConvenios.APROBADO.getId());
					List<ConvenioComunaComponente> conveniosComunaComponenteEnTramite = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getIdTipoSubtitulo(), comuna.getId(), EstadosConvenios.TRAMITE.getId());
					List<ConvenioComunaComponente> conveniosComunaComponentePagada = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getIdTipoSubtitulo(), comuna.getId(), EstadosConvenios.PAGADO.getId());
					boolean nuevo = true;
					boolean reemplazar = true;
					Integer montoActual = 0;
					Integer montoActualizado = 0;
					Integer idConvenioComuna = 0;
					
					if(conveniosComunaComponentePagada != null && conveniosComunaComponentePagada.size() > 0){
						nuevo = false;
						reemplazar = false;
						int size = conveniosComunaComponentePagada.size();
						ConvenioComunaComponente convenioComunaComponente = conveniosComunaComponentePagada.get(size -1);
						idConvenioComuna = convenioComunaComponente.getConvenioComuna().getIdConvenioComuna();
						montoActual =  convenioComunaComponente.getMontoIngresado();
						montoActualizado = convenioComunaComponente.getMonto();
					}
					
					if(conveniosComunaComponenteEnTramite != null && conveniosComunaComponenteEnTramite.size() > 0){
						nuevo = false;
						reemplazar = false;
						int size = conveniosComunaComponenteEnTramite.size();
						ConvenioComunaComponente convenioComunaComponente = conveniosComunaComponenteEnTramite.get(size -1);
						idConvenioComuna = convenioComunaComponente.getConvenioComuna().getIdConvenioComuna();
						montoActual =  convenioComunaComponente.getMontoIngresado();
						montoActualizado = convenioComunaComponente.getMonto();
					}
					
					if(conveniosComunaComponenteAprobado != null && conveniosComunaComponenteAprobado.size() > 0){
						nuevo = false;
						reemplazar = false;
						int size = conveniosComunaComponenteAprobado.size();
						ConvenioComunaComponente convenioComunaComponente = conveniosComunaComponenteAprobado.get(size -1);
						idConvenioComuna = convenioComunaComponente.getConvenioComuna().getIdConvenioComuna();
						montoActual =  convenioComunaComponente.getMontoIngresado();
						montoActualizado = convenioComunaComponente.getMonto();
					}
					
					if(conveniosComunaComponenteIngresada != null && conveniosComunaComponenteIngresada.size() > 0){
						nuevo = false;
						reemplazar = true;
						int size = conveniosComunaComponenteIngresada.size();
						ConvenioComunaComponente convenioComunaComponente = conveniosComunaComponenteIngresada.get(size -1);
						idConvenioComuna = convenioComunaComponente.getConvenioComuna().getIdConvenioComuna();
						montoActual =  convenioComunaComponente.getMontoIngresado();
						montoActualizado = convenioComunaComponente.getMonto();
					}
					CargaConvenioComunaComponenteVO cargaConvenioComunaComponenteVO = new CargaConvenioComunaComponenteVO();
					cargaConvenioComunaComponenteVO.setIdConvenioComuna(idConvenioComuna);
					cargaConvenioComunaComponenteVO.setNuevo(nuevo);
					cargaConvenioComunaComponenteVO.setReemplazar(reemplazar);
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
					cargaConvenioComunaComponenteVO.setMonto(montoActualizado);
					resolucionesConvenios.add(cargaConvenioComunaComponenteVO);
				}
			}
		}else{
			if(componenteSeleccionado != null && comunaSeleccionada != null){
				Comuna comuna = comunaDAO.getComunaById(comunaSeleccionada);
				Componente componente = componenteDAO.getComponenteByID(componenteSeleccionado);
				TipoSubtitulo subtitulo = tipoSubtituloDAO.getTipoSubtituloById(Subtitulo.SUBTITULO24.getId());
				List<ConvenioComunaComponente> conveniosComunaComponenteIngresada = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getIdTipoSubtitulo(), comuna.getId(), EstadosConvenios.INGRESADO.getId());
				List<ConvenioComunaComponente> conveniosComunaComponenteAprobado = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getIdTipoSubtitulo(), comuna.getId(), EstadosConvenios.APROBADO.getId());
				List<ConvenioComunaComponente> conveniosComunaComponenteEnTramite = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getIdTipoSubtitulo(), comuna.getId(), EstadosConvenios.TRAMITE.getId());
				List<ConvenioComunaComponente> conveniosComunaComponentePagada = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getIdTipoSubtitulo(), comuna.getId(), EstadosConvenios.PAGADO.getId());
				boolean nuevo = true;
				boolean reemplazar = true;
				Integer montoActual = 0;
				Integer montoActualizado = 0;
				Integer idConvenioComuna = 0;
				
				if(conveniosComunaComponentePagada != null && conveniosComunaComponentePagada.size() > 0){
					nuevo = false;
					reemplazar = false;
					int size = conveniosComunaComponentePagada.size();
					ConvenioComunaComponente convenioComunaComponente = conveniosComunaComponentePagada.get(size -1);
					idConvenioComuna = convenioComunaComponente.getConvenioComuna().getIdConvenioComuna();
					montoActual =  convenioComunaComponente.getMontoIngresado();
					montoActualizado = convenioComunaComponente.getMonto();
				}
				
				if(conveniosComunaComponenteEnTramite != null && conveniosComunaComponenteEnTramite.size() > 0){
					nuevo = false;
					reemplazar = false;
					int size = conveniosComunaComponenteEnTramite.size();
					ConvenioComunaComponente convenioComunaComponente = conveniosComunaComponenteEnTramite.get(size -1);
					idConvenioComuna = convenioComunaComponente.getConvenioComuna().getIdConvenioComuna();
					montoActual =  convenioComunaComponente.getMontoIngresado();
					montoActualizado = convenioComunaComponente.getMonto();
				}
				
				if(conveniosComunaComponenteAprobado != null && conveniosComunaComponenteAprobado.size() > 0){
					nuevo = false;
					reemplazar = false;
					int size = conveniosComunaComponenteAprobado.size();
					ConvenioComunaComponente convenioComunaComponente = conveniosComunaComponenteAprobado.get(size -1);
					idConvenioComuna = convenioComunaComponente.getConvenioComuna().getIdConvenioComuna();
					montoActual =  convenioComunaComponente.getMontoIngresado();
					montoActualizado = convenioComunaComponente.getMonto();
				}
				
				if(conveniosComunaComponenteIngresada != null && conveniosComunaComponenteIngresada.size() > 0){
					nuevo = false;
					reemplazar = true;
					int size = conveniosComunaComponenteIngresada.size();
					ConvenioComunaComponente convenioComunaComponente = conveniosComunaComponenteIngresada.get(size -1);
					idConvenioComuna = convenioComunaComponente.getConvenioComuna().getIdConvenioComuna();
					montoActual =  convenioComunaComponente.getMontoIngresado();
					montoActualizado = convenioComunaComponente.getMonto();
				}
				CargaConvenioComunaComponenteVO cargaConvenioComunaComponenteVO = new CargaConvenioComunaComponenteVO();
				cargaConvenioComunaComponenteVO.setNuevo(nuevo);
				cargaConvenioComunaComponenteVO.setReemplazar(reemplazar);
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
				cargaConvenioComunaComponenteVO.setMonto(montoActualizado);
				resolucionesConvenios.add(cargaConvenioComunaComponenteVO);
			} else {
				if(componenteSeleccionado == null){
					Comuna comuna = comunaDAO.getComunaById(comunaSeleccionada);
					List<Componente> componentes = componenteDAO.getComponentesByIdProgramaIdSubtitulos(programa.getId(), Subtitulo.SUBTITULO24);
					TipoSubtitulo subtitulo = tipoSubtituloDAO.getTipoSubtituloById(Subtitulo.SUBTITULO24.getId());
					for(Componente componente : componentes){
						List<ConvenioComunaComponente> conveniosComunaComponenteIngresada = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getIdTipoSubtitulo(), comuna.getId(), EstadosConvenios.INGRESADO.getId());
						List<ConvenioComunaComponente> conveniosComunaComponenteAprobado = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getIdTipoSubtitulo(), comuna.getId(), EstadosConvenios.APROBADO.getId());
						List<ConvenioComunaComponente> conveniosComunaComponenteEnTramite = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getIdTipoSubtitulo(), comuna.getId(), EstadosConvenios.TRAMITE.getId());
						List<ConvenioComunaComponente> conveniosComunaComponentePagada = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getIdTipoSubtitulo(), comuna.getId(), EstadosConvenios.PAGADO.getId());
						boolean nuevo = true;
						boolean reemplazar = true;
						Integer montoActual = 0;
						Integer montoActualizado = 0;
						Integer idConvenioComuna = 0;
						
						if(conveniosComunaComponenteEnTramite != null && conveniosComunaComponenteEnTramite.size() > 0){
							nuevo = false;
							reemplazar = false;
							int size = conveniosComunaComponenteEnTramite.size();
							ConvenioComunaComponente convenioComunaComponente = conveniosComunaComponenteEnTramite.get(size -1);
							montoActual =  convenioComunaComponente.getMontoIngresado();
							montoActualizado = convenioComunaComponente.getMonto();
						}
						
						if(conveniosComunaComponentePagada != null && conveniosComunaComponentePagada.size() > 0){
							nuevo = false;
							reemplazar = false;
							int size = conveniosComunaComponentePagada.size();
							ConvenioComunaComponente convenioComunaComponente = conveniosComunaComponentePagada.get(size -1);
							idConvenioComuna = convenioComunaComponente.getConvenioComuna().getIdConvenioComuna();
							montoActual =  convenioComunaComponente.getMontoIngresado();
							montoActualizado = convenioComunaComponente.getMonto();
						}
						
						if(conveniosComunaComponenteAprobado != null && conveniosComunaComponenteAprobado.size() > 0){
							nuevo = false;
							reemplazar = false;
							int size = conveniosComunaComponenteAprobado.size();
							ConvenioComunaComponente convenioComunaComponente = conveniosComunaComponenteAprobado.get(size -1);
							idConvenioComuna = convenioComunaComponente.getConvenioComuna().getIdConvenioComuna();
							montoActual =  convenioComunaComponente.getMontoIngresado();
							montoActualizado = convenioComunaComponente.getMonto();
						}
						
						if(conveniosComunaComponenteIngresada != null && conveniosComunaComponenteIngresada.size() > 0){
							nuevo = false;
							reemplazar = true;
							int size = conveniosComunaComponentePagada.size();
							ConvenioComunaComponente convenioComunaComponente = conveniosComunaComponentePagada.get(size -1);
							idConvenioComuna = convenioComunaComponente.getConvenioComuna().getIdConvenioComuna();
							montoActual =  convenioComunaComponente.getMontoIngresado();
							montoActualizado = convenioComunaComponente.getMonto();
						}
						CargaConvenioComunaComponenteVO cargaConvenioComunaComponenteVO = new CargaConvenioComunaComponenteVO();
						cargaConvenioComunaComponenteVO.setNuevo(nuevo);
						cargaConvenioComunaComponenteVO.setReemplazar(reemplazar);
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
						cargaConvenioComunaComponenteVO.setMonto(montoActualizado);
						resolucionesConvenios.add(cargaConvenioComunaComponenteVO);
					}
				}else{
					Componente componente = componenteDAO.getComponenteByID(componenteSeleccionado);
					List<Comuna> comunas = comunaDAO.getComunasByServicio(idServicio);
					TipoSubtitulo subtitulo = tipoSubtituloDAO.getTipoSubtituloById(Subtitulo.SUBTITULO24.getId());
					for(Comuna comuna : comunas){
						List<ConvenioComunaComponente> conveniosComunaComponenteIngresada = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getIdTipoSubtitulo(), comuna.getId(), EstadosConvenios.INGRESADO.getId());
						List<ConvenioComunaComponente> conveniosComunaComponenteAprobado = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getIdTipoSubtitulo(), comuna.getId(), EstadosConvenios.APROBADO.getId());
						List<ConvenioComunaComponente> conveniosComunaComponenteEnTramite = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getIdTipoSubtitulo(), comuna.getId(), EstadosConvenios.TRAMITE.getId());
						List<ConvenioComunaComponente> conveniosComunaComponentePagada = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getIdTipoSubtitulo(), comuna.getId(), EstadosConvenios.PAGADO.getId());
						boolean nuevo = true;
						boolean reemplazar = true;
						Integer montoActual = 0;
						Integer montoActualizado = 0;
						Integer idConvenioComuna = 0;
						
						if(conveniosComunaComponentePagada != null && conveniosComunaComponentePagada.size() > 0){
							nuevo = false;
							reemplazar = false;
							int size = conveniosComunaComponentePagada.size();
							ConvenioComunaComponente convenioComunaComponente = conveniosComunaComponentePagada.get(size -1);
							idConvenioComuna = convenioComunaComponente.getConvenioComuna().getIdConvenioComuna();
							montoActual =  convenioComunaComponente.getMontoIngresado();
						}
						
						if(conveniosComunaComponenteEnTramite != null && conveniosComunaComponenteEnTramite.size() > 0){
							nuevo = false;
							reemplazar = false;
							int size = conveniosComunaComponenteEnTramite.size();
							ConvenioComunaComponente convenioComunaComponente = conveniosComunaComponenteEnTramite.get(size -1);
							idConvenioComuna = convenioComunaComponente.getConvenioComuna().getIdConvenioComuna();
							montoActual =  convenioComunaComponente.getMontoIngresado();
							montoActualizado = convenioComunaComponente.getMonto();
						}
						
						if(conveniosComunaComponenteAprobado != null && conveniosComunaComponenteAprobado.size() > 0){
							nuevo = false;
							reemplazar = false;
							int size = conveniosComunaComponenteAprobado.size();
							ConvenioComunaComponente convenioComunaComponente = conveniosComunaComponenteAprobado.get(size -1);
							idConvenioComuna = convenioComunaComponente.getConvenioComuna().getIdConvenioComuna();
							montoActual =  convenioComunaComponente.getMontoIngresado();
							montoActualizado = convenioComunaComponente.getMonto();
						}
						
						if(conveniosComunaComponenteIngresada != null && conveniosComunaComponenteIngresada.size() > 0){
							nuevo = false;
							reemplazar = true;
							int size = conveniosComunaComponenteIngresada.size();
							ConvenioComunaComponente convenioComunaComponente = conveniosComunaComponenteIngresada.get(size -1);
							idConvenioComuna = convenioComunaComponente.getConvenioComuna().getIdConvenioComuna();
							montoActual =  convenioComunaComponente.getMontoIngresado();
							montoActualizado = convenioComunaComponente.getMonto();
						}
						CargaConvenioComunaComponenteVO cargaConvenioComunaComponenteVO = new CargaConvenioComunaComponenteVO();
						cargaConvenioComunaComponenteVO.setNuevo(nuevo);
						cargaConvenioComunaComponenteVO.setReemplazar(reemplazar);
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
						cargaConvenioComunaComponenteVO.setMonto(montoActualizado);
						resolucionesConvenios.add(cargaConvenioComunaComponenteVO);
					}
				}
			}
		}
		return resolucionesConvenios;
	}

	public List<CargaConvenioServicioComponenteVO> getResolucionConveniosServicio(Integer idServicio, Integer idProgramaAno, Integer componenteSeleccionado, Integer establecimientoSeleccionado, Subtitulo subtituloSeleccionado) {
		List<CargaConvenioServicioComponenteVO> resolucionesConvenios = new ArrayList<CargaConvenioServicioComponenteVO>();
		ProgramaVO programa = programasService.getProgramaAno(idProgramaAno);
		if(componenteSeleccionado == null && establecimientoSeleccionado == null){
			List<Establecimiento> establecimientos = establecimientosDAO.getEstablecimientosByServicio(idServicio);
			List<Componente> componentes = componenteDAO.getComponentesByIdProgramaIdSubtitulos(programa.getId(), subtituloSeleccionado);
			TipoSubtitulo subtitulo = tipoSubtituloDAO.getTipoSubtituloById(subtituloSeleccionado.getId());
			for(Establecimiento establecimiento : establecimientos){
				for(Componente componente : componentes){
					List<ConvenioServicioComponente> conveniosServicioComponente = conveniosDAO.getConveniosServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getIdTipoSubtitulo(), establecimiento.getId(), EstadosConvenios.INGRESADO.getId());
					CargaConvenioServicioComponenteVO cargaConvenioServicioComponenteVO = new CargaConvenioServicioComponenteVO();
					if(conveniosServicioComponente != null && conveniosServicioComponente.size() > 0){
						int size = conveniosServicioComponente.size();
						ConvenioServicioComponente convenioServicioComponente = conveniosServicioComponente.get(size -1);
						Integer montoActual = convenioServicioComponente.getMontoIngresado();
						cargaConvenioServicioComponenteVO.setNuevo(false);
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
						cargaConvenioServicioComponenteVO.setMonto(montoActual);
					}else{
						cargaConvenioServicioComponenteVO.setNuevo(true);
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
						cargaConvenioServicioComponenteVO.setMonto(0);
					}
					resolucionesConvenios.add(cargaConvenioServicioComponenteVO);
				}
			}
		}else{
			if(componenteSeleccionado != null && establecimientoSeleccionado != null){
				Establecimiento establecimiento = establecimientosDAO.getEstablecimientoById(establecimientoSeleccionado);
				Componente componente = componenteDAO.getComponenteByID(componenteSeleccionado);
				TipoSubtitulo subtitulo = tipoSubtituloDAO.getTipoSubtituloById(subtituloSeleccionado.getId());
				List<ConvenioServicioComponente> conveniosServicioComponente = conveniosDAO.getConveniosServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getIdTipoSubtitulo(), establecimiento.getId(), EstadosConvenios.INGRESADO.getId());
				CargaConvenioServicioComponenteVO cargaConvenioServicioComponenteVO = new CargaConvenioServicioComponenteVO();
				if(conveniosServicioComponente != null && conveniosServicioComponente.size() > 0){
					int size = conveniosServicioComponente.size();
					ConvenioServicioComponente convenioServicioComponente = conveniosServicioComponente.get(size-1);
					Integer montoActual = convenioServicioComponente.getMontoIngresado();
					cargaConvenioServicioComponenteVO.setNuevo(false);
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
					cargaConvenioServicioComponenteVO.setMonto(montoActual);
				}else{
					cargaConvenioServicioComponenteVO.setNuevo(true);
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
					cargaConvenioServicioComponenteVO.setMonto(0);
				}
				resolucionesConvenios.add(cargaConvenioServicioComponenteVO);
			} else {
				if(componenteSeleccionado == null){
					Establecimiento establecimiento = establecimientosDAO.getEstablecimientoById(establecimientoSeleccionado);
					List<Componente> componentes = componenteDAO.getComponentesByIdProgramaIdSubtitulos(programa.getId(), subtituloSeleccionado);
					TipoSubtitulo subtitulo = tipoSubtituloDAO.getTipoSubtituloById(subtituloSeleccionado.getId());
					for(Componente componente : componentes){
						List<ConvenioServicioComponente> conveniosServicioComponente = conveniosDAO.getConveniosServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getIdTipoSubtitulo(), establecimiento.getId(), EstadosConvenios.INGRESADO.getId());
						CargaConvenioServicioComponenteVO cargaConvenioServicioComponenteVO = new CargaConvenioServicioComponenteVO();
						if(conveniosServicioComponente != null && conveniosServicioComponente.size() > 0){
							int size = conveniosServicioComponente.size();
							ConvenioServicioComponente convenioServicioComponente = conveniosServicioComponente.get(size-1);
							Integer montoActual = convenioServicioComponente.getMontoIngresado();
							cargaConvenioServicioComponenteVO.setNuevo(false);
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
							cargaConvenioServicioComponenteVO.setMonto(montoActual);
						}else{
							cargaConvenioServicioComponenteVO.setNuevo(true);
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
							cargaConvenioServicioComponenteVO.setMonto(0);
						}
						resolucionesConvenios.add(cargaConvenioServicioComponenteVO);
					}
				}else{
					Componente componente = componenteDAO.getComponenteByID(componenteSeleccionado);
					List<Establecimiento> establecimientos = establecimientosDAO.getEstablecimientosByServicio(idServicio);
					TipoSubtitulo subtitulo = tipoSubtituloDAO.getTipoSubtituloById(subtituloSeleccionado.getId());
					for(Establecimiento establecimiento : establecimientos){
						List<ConvenioServicioComponente> conveniosServicioComponente = conveniosDAO.getConveniosServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getIdTipoSubtitulo(), establecimiento.getId(), EstadosConvenios.INGRESADO.getId());
						CargaConvenioServicioComponenteVO cargaConvenioServicioComponenteVO = new CargaConvenioServicioComponenteVO();
						if(conveniosServicioComponente != null && conveniosServicioComponente.size() > 0){
							int size = conveniosServicioComponente.size();
							ConvenioServicioComponente convenioServicioComponente = conveniosServicioComponente.get(size-1);
							Integer montoActual = convenioServicioComponente.getMontoIngresado();
							cargaConvenioServicioComponenteVO.setNuevo(false);
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
							cargaConvenioServicioComponenteVO.setMonto(montoActual);
						}else{
							cargaConvenioServicioComponenteVO.setNuevo(true);
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
							cargaConvenioServicioComponenteVO.setMonto(0);
						}
						resolucionesConvenios.add(cargaConvenioServicioComponenteVO);
					}
				}
			}
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
		for(Componente componente : programaAno.getPrograma().getComponentes()){
			for(ComponenteSubtitulo componenteSubtitulo : componente.getComponenteSubtitulos()){
				if(Subtitulo.SUBTITULO24.getId().equals(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo())){
					if(componentesBySubtitulo.get(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo()) == null){
						List<Integer> idComponentes = new ArrayList<Integer>();
						idComponentes.add(componente.getId());
						componentesBySubtitulo.put(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo(), idComponentes);
					}else{
						componentesBySubtitulo.get(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo()).add(componente.getId());
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
		for(Componente componente : programaAno.getPrograma().getComponentes()){
			for(ComponenteSubtitulo componenteSubtitulo : componente.getComponenteSubtitulos()){
				if(!Subtitulo.SUBTITULO24.getId().equals(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo())){
					if(componentesBySubtitulo.get(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo()) == null){
						List<Integer> idComponentes = new ArrayList<Integer>();
						idComponentes.add(componente.getId());
						componentesBySubtitulo.put(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo(), idComponentes);
					}else{
						componentesBySubtitulo.get(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo()).add(componente.getId());
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

	public List<ConvenioComunaComponenteVO> getConveniosComunaComponenteByProgramaAnoComponenteSubtituloServicioConvenioEstadoConvenio(Integer idProgramaAno, Integer idComponente, Integer idSubtitulo, Integer idServicio, Integer idConvenio, EstadosConvenios estadoConvenio){
		List<ConvenioComunaComponenteVO> conveniosComunaComponenteVO = new ArrayList<ConvenioComunaComponenteVO>();
		List<ConvenioComunaComponente> results = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloServicioConvenioEstadoConvenio(idProgramaAno, idComponente, idSubtitulo, idServicio, idConvenio, estadoConvenio.getId());
		if(results != null && results.size() > 0){
			for(ConvenioComunaComponente convenioComunaComponente : results){
				if(convenioComunaComponente.isAprobado()){
					continue;
				}
				conveniosComunaComponenteVO.add(new ConvenioComunaComponenteMapper().getBasic(convenioComunaComponente));
			}
		}
		return conveniosComunaComponenteVO;
	}

	public List<ConvenioComunaComponenteVO> getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaConvenioEstadoConvenio(Integer idProgramaAno, Integer idComponente, Integer idSubtitulo, Integer idComuna, Integer idConvenio, EstadosConvenios estadoConvenio){
		List<ConvenioComunaComponenteVO> conveniosComunaComponenteVO = new ArrayList<ConvenioComunaComponenteVO>();
		List<ConvenioComunaComponente> results = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaConvenioEstadoConvenio(idProgramaAno, idComponente, idSubtitulo, idComuna, idConvenio,estadoConvenio.getId());
		if(results != null && results.size() > 0){
			for(ConvenioComunaComponente convenioComunaComponente : results){
				if(convenioComunaComponente.isAprobado()){
					continue;
				}
				conveniosComunaComponenteVO.add(new ConvenioComunaComponenteMapper().getBasic(convenioComunaComponente));
			}
		}
		return conveniosComunaComponenteVO;
	}

	public List<ConvenioServicioComponenteVO> getConveniosServicioComponenteByProgramaAnoComponenteSubtituloServicioConvenioEstadoConvenio(Integer idProgramaAno, Integer idComponente, Integer idSubtitulo, Integer idServicio, Integer idConvenio, EstadosConvenios estadoConvenio){
		List<ConvenioServicioComponenteVO> conveniosServicioComponenteVO = new ArrayList<ConvenioServicioComponenteVO>();
		List<ConvenioServicioComponente> results = conveniosDAO.getConveniosServicioComponenteByProgramaAnoComponenteSubtituloServicioConvenioEstadoConvenio(idProgramaAno, idComponente, idSubtitulo, idServicio, idConvenio, estadoConvenio.getId());
		if(results != null && results.size() > 0){
			for(ConvenioServicioComponente convenioServicioComponente : results){
				if(convenioServicioComponente.isAprobado()){
					continue;
				}
				conveniosServicioComponenteVO.add(new ConvenioServicioComponenteMapper().getBasic(convenioServicioComponente));
			}
		}
		return conveniosServicioComponenteVO;
	}

	public List<ConvenioServicioComponenteVO> getConveniosServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoConvenioEstadoConvenio(Integer idProgramaAno, Integer idComponente, Integer idSubtitulo, Integer idEstablecimiento, Integer idConvenio, EstadosConvenios estadoConvenio){
		List<ConvenioServicioComponenteVO> conveniosServicioComponenteVO = new ArrayList<ConvenioServicioComponenteVO>();
		List<ConvenioServicioComponente> resutls = conveniosDAO.getConveniosServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoConvenioEstadoConvenio(idProgramaAno, idComponente, idSubtitulo, idEstablecimiento, idConvenio, estadoConvenio.getId());
		if(resutls != null && resutls.size() > 0){
			for(ConvenioServicioComponente convenioServicioComponente : resutls){
				if(convenioServicioComponente.isAprobado()){
					continue;
				}
				conveniosServicioComponenteVO.add(new ConvenioServicioComponenteMapper().getBasic(convenioServicioComponente));
			}
		}
		return conveniosServicioComponenteVO;
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
			convenioServicioComponenteSeleccionado.setAprobado(Boolean.TRUE);
			ConvenioServicio convenioServicio = convenioServicioComponenteSeleccionado.getConvenioServicio();
			boolean aprobado = true;
			for(ConvenioServicioComponente convenioServicioComponente : convenioServicio.getConvenioServicioComponentes()){
				if(!convenioServicioComponente.isAprobado()){
					aprobado = false;
					break;
				}
			}
			if(aprobado){
				Convenio convenio = conveniosDAO.findById(idConvenio);
				convenioServicio.setConvenio(convenio);
				convenioServicio.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.APROBADO.getId()));
			}
		}
		return new ConvenioServicioComponenteMapper().getBasic(convenioServicioComponenteSeleccionado);
	}

	public ConvenioComunaComponenteVO aprobarConvenioComunaComponente(Integer idConvenio, Integer idConvenioServicioComponente) {
		ConvenioComunaComponente convenioComunaComponenteSeleccionado = conveniosDAO.getConveniosComunaComponenteById(idConvenioServicioComponente);
		if(convenioComunaComponenteSeleccionado != null){
			convenioComunaComponenteSeleccionado.setAprobado(Boolean.TRUE);
			ConvenioComuna convenioComuna = convenioComunaComponenteSeleccionado.getConvenioComuna();
			boolean aprobado = true;
			for(ConvenioComunaComponente convenioComunaComponente : convenioComuna.getConvenioComunaComponentes()){
				if(!convenioComunaComponente.isAprobado()){
					aprobado = false;
					break;
				}
			}
			if(aprobado){
				System.out.println("idConvenio="+idConvenio);
				Convenio convenio = conveniosDAO.findById(idConvenio);
				System.out.println("convenio="+convenio);
				convenioComuna.setConvenio(convenio);
				convenioComuna.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.APROBADO.getId()));
			}
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
			List<String> conCopia, List<String> conCopiaOculta, List<Integer> documentos) {
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
				BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummaryVO.getPath()), contenType, folderProcesoConvenio.replace("{ANO}", getAnoCurso().toString()));
				System.out.println("response upload template --->"+response);
				documentService.updateDocumentTemplate(referenciaDocumentoId, response.getNodeRef(), response.getFileName(), contenType);
			}
		}
		Integer idSeguimiento = seguimientoService.createSeguimiento(tareaSeguimiento, subject, body, from, para, conCopia, conCopiaOculta, documentosTmp);
		Seguimiento seguimiento = seguimientoDAO.getSeguimientoById(idSeguimiento);
		return conveniosDAO.createSeguimiento(idConvenio, seguimiento);
	}

	public void moveToAlfresco(Integer idConvenio, Integer referenciaDocumentoId, TipoDocumentosProcesos tipoDocumento, Boolean lastVersion) {
		System.out.println("Buscando referenciaDocumentoId="+referenciaDocumentoId);
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummary = documentService.getDocumentSummary(referenciaDocumentoId);
		System.out.println("Buscando referenciaDocumentoSummary="+referenciaDocumentoSummary);
		if(referenciaDocumentoSummary != null){
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contenType = mimemap.getContentType(referenciaDocumentoSummary.getPath().toLowerCase());
			BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummary.getPath()), contenType, folderProcesoConvenio.replace("{ANO}", getAnoCurso().toString()));
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
		List<DocumentoConvenio> documentosConvenio = conveniosDAO.getByIdConvenioTipo(idConvenio, TipoDocumentosProcesos.RESOLUCIONRETIRO);
		try{
			if(documentosConvenio != null && documentosConvenio.size() > 0){
				for(DocumentoConvenio documentoConvenio : documentosConvenio){
					if(documentoConvenio.getServicio() != null && documentoConvenio.getServicio().getDirector() != null && documentoConvenio.getServicio().getDirector().getEmail() != null){
						List<EmailService.Adjunto> adjuntos = new ArrayList<EmailService.Adjunto>();
						ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryResolucionRebaja = documentService.getDocumentById(documentoConvenio.getDocumento().getId());
						DocumentoVO documentDocumentoRebajaVO = documentService.getDocument(referenciaDocumentoSummaryResolucionRebaja.getId());
						String fileNameDocumentoRebaja = tmpDirDoc + File.separator + documentDocumentoRebajaVO.getName();
						GeneradorWord generadorWordDocumentoRebaja = new GeneradorWord(fileNameDocumentoRebaja);
						generadorWordDocumentoRebaja.saveContent(documentDocumentoRebajaVO.getContent(), XWPFDocument.class);
						EmailService.Adjunto adjunto = new EmailService.Adjunto();
						adjunto.setDescripcion("Resolución Rebaja Aporte Estatal");
						adjunto.setName(documentDocumentoRebajaVO.getName());
						adjunto.setUrl((new File(fileNameDocumentoRebaja)).toURI().toURL());
						adjuntos.add(adjunto);
						List<String> to = new ArrayList<String>();
						ReporteEmailsDestinatarios destinatarioPara = new ReporteEmailsDestinatarios();
						to.add(documentoConvenio.getServicio().getDirector().getEmail().getValor());
						destinatarioPara.setDestinatario(documentoConvenio.getServicio().getDirector());
						List<String> cc = new ArrayList<String>();
						ReporteEmailsDestinatarios destinatarioConCopiaEncargadoFinanzas = null;
						ReporteEmailsDestinatarios destinatarioConCopiaEncargadoAPS = null;
						if(documentoConvenio.getServicio().getEncargadoFinanzasAps() != null  && documentoConvenio.getServicio().getEncargadoFinanzasAps().getEmail() != null){
							destinatarioConCopiaEncargadoAPS = new ReporteEmailsDestinatarios();
							cc.add(documentoConvenio.getServicio().getEncargadoFinanzasAps().getEmail().getValor());
							destinatarioConCopiaEncargadoAPS.setDestinatario(documentoConvenio.getServicio().getEncargadoFinanzasAps());
						}
						if(documentoConvenio.getServicio().getEncargadoAps() != null  && documentoConvenio.getServicio().getEncargadoAps().getEmail() != null){
							destinatarioConCopiaEncargadoFinanzas = new ReporteEmailsDestinatarios();
							cc.add(documentoConvenio.getServicio().getEncargadoAps().getEmail().getValor());
							destinatarioConCopiaEncargadoFinanzas.setDestinatario(documentoConvenio.getServicio().getEncargadoAps());
						}
						if(emailPLantilla != null && emailPLantilla.getAsunto() != null && emailPLantilla.getCuerpo() != null){
							emailService.sendMail(to, cc, null, emailPLantilla.getAsunto(), emailPLantilla.getCuerpo().replaceAll("(\r\n|\n)", "<br />"), adjuntos);

						}else{
							emailService.sendMail(to, cc, null, "Resolución Retiro", "Estimado " + documentoConvenio.getServicio().getDirector().getNombre() + " " + documentoConvenio.getServicio().getDirector().getApellidoPaterno() + " " + ((documentoConvenio.getServicio().getDirector().getApellidoMaterno() != null) ? documentoConvenio.getServicio().getDirector().getApellidoMaterno() : "") + ": <br /> <p> l</p>", adjuntos);
						}
						ReporteEmailsEnviados reporteEmailsEnviados = new ReporteEmailsEnviados();
						ReporteEmailsAdjuntos reporteEmailsAdjuntos = new ReporteEmailsAdjuntos();
						reporteEmailsEnviados.setFecha(new Date());
						reporteEmailsEnviados.setIdProgramaAno(programaAno);
						reporteEmailsEnviados.setIdServicio(documentoConvenio.getServicio());
						conveniosDAO.save(reporteEmailsEnviados);
						destinatarioPara.setReporteEmailsEnviado(reporteEmailsEnviados);
						destinatarioPara.setTipoDestinatario(new TipoDestinatario(TiposDestinatarios.PARA.getId()));
						conveniosDAO.save(destinatarioPara);
						if(destinatarioConCopiaEncargadoFinanzas != null){
							destinatarioConCopiaEncargadoFinanzas.setReporteEmailsEnviado(reporteEmailsEnviados);
							destinatarioConCopiaEncargadoFinanzas.setTipoDestinatario(new TipoDestinatario(TiposDestinatarios.CC.getId()));
							conveniosDAO.save(destinatarioConCopiaEncargadoFinanzas);
						}
						if(destinatarioConCopiaEncargadoAPS != null){
							destinatarioConCopiaEncargadoAPS.setReporteEmailsEnviado(reporteEmailsEnviados);
							destinatarioConCopiaEncargadoAPS.setTipoDestinatario(new TipoDestinatario(TiposDestinatarios.CC.getId()));
							conveniosDAO.save(destinatarioConCopiaEncargadoAPS);
						}
						reporteEmailsAdjuntos.setDocumento(documentoConvenio.getDocumento());
						reporteEmailsAdjuntos.setReporteEmailsEnviado(reporteEmailsEnviados);
						conveniosDAO.save(reporteEmailsAdjuntos);
						ReporteEmailsConvenio reporteEmailsConvenio = new ReporteEmailsConvenio();
						reporteEmailsConvenio.setConvenio(documentoConvenio.getConvenio());
						reporteEmailsConvenio.setReporteEmailsEnviados(reporteEmailsEnviados);
						conveniosDAO.save(reporteEmailsConvenio);
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<ReporteEmailsEnviadosVO> getReporteCorreosByConvenio(Integer idConvenio) {
		List<ReporteEmailsEnviadosVO> reporteCorreos = new ArrayList<ReporteEmailsEnviadosVO>();
		List<ReporteEmailsConvenio> reportes = conveniosDAO.getReporteCorreosByConvenio(idConvenio);
		for(ReporteEmailsConvenio reporte : reportes){
			ReporteEmailsEnviadosVO correo = new ReporteEmailsEnviadosVO();
			correo.setIdServicio(reporte.getReporteEmailsEnviados().getIdServicio().getId());
			correo.setNombreServicio(reporte.getReporteEmailsEnviados().getIdServicio().getNombre());
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
		return reporteCorreos;
	}

	public void moveConvenioToAlfresco(Integer referenciaDocumentoId) {
		System.out.println("Buscando referenciaDocumentoId="+referenciaDocumentoId);
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummary = documentService.getDocumentSummary(referenciaDocumentoId);
		System.out.println("Buscando referenciaDocumentoSummary="+referenciaDocumentoSummary);
		if(referenciaDocumentoSummary != null){
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contenType= mimemap.getContentType(referenciaDocumentoSummary.getPath().toLowerCase());
			BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummary.getPath()), contenType, folderProcesoConvenio.replace("{ANO}", getAnoCurso().toString()));
			System.out.println("response upload template --->"+response);
			documentService.updateDocumentTemplate(referenciaDocumentoSummary.getId(), response.getNodeRef(), response.getFileName(), contenType);
		}
	}

	public CargaConvenioServicioComponenteVO guardarConvenioServicioComponente(Integer idProgramaAno, CargaConvenioServicioComponenteVO cargaConvenioServicioComponenteVO, Integer docConvenio) {
		CargaConvenioServicioComponenteVO cargaConvenioServicioComponenteVONuevo = null;
		Date fechaActual = new Date();
		if(cargaConvenioServicioComponenteVO.getNuevo()){
			ProgramaAno programa = programasDAO.getProgramaAnoByID(idProgramaAno);
			Mes mes = mesDAO.getMesPorID(Integer.parseInt(getMesCurso(true)));
			Establecimiento establecimiento = establecimientosDAO.getEstablecimientoById(cargaConvenioServicioComponenteVO.getIdEstablecimiento());
			ConvenioServicio convenioServicio = new ConvenioServicio();
			convenioServicio.setFecha(fechaActual);
			convenioServicio.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.INGRESADO.getId()));
			convenioServicio.setNumeroResolucion(cargaConvenioServicioComponenteVO.getNumeroResoucion());
			convenioServicio.setIdEstablecimiento(establecimiento);
			convenioServicio.setIdPrograma(programa);
			convenioServicio.setMes(mes);
			conveniosDAO.save(convenioServicio);
			ConvenioServicioComponente convenioServicioComponente = new ConvenioServicioComponente();
			convenioServicioComponente.setAprobado(false);
			convenioServicioComponente.setFecha(fechaActual);
			Componente componente = componenteDAO.getComponenteByID(cargaConvenioServicioComponenteVO.getComponente().getId());
			convenioServicioComponente.setComponente(componente);
			convenioServicioComponente.setConvenioServicio(convenioServicio);
			convenioServicioComponente.setMontoIngresado(cargaConvenioServicioComponenteVO.getMontoIngresado());
			convenioServicioComponente.setMonto(cargaConvenioServicioComponenteVO.getMontoIngresado());
			TipoSubtitulo subtitulo = tipoSubtituloDAO.getTipoSubtituloById(cargaConvenioServicioComponenteVO.getSubtitulo().getIdSubtitulo());
			convenioServicioComponente.setSubtitulo(subtitulo);
			ReferenciaDocumento documentoConvenio = documentDAO.findById(docConvenio);
			convenioServicioComponente.setDocumentoConvenio(documentoConvenio);
			conveniosDAO.save(convenioServicioComponente);
			cargaConvenioServicioComponenteVONuevo = new CargaConvenioServicioComponenteMapper().getBasic(convenioServicioComponente);
			cargaConvenioServicioComponenteVONuevo.setNuevo(false);
			cargaConvenioServicioComponenteVONuevo.setReemplazar(true);
		}else{
			if(cargaConvenioServicioComponenteVO.getReemplazar()){
				ConvenioServicio convenioServicio = conveniosDAO.getConvenioServicioById(cargaConvenioServicioComponenteVO.getIdConvenioServicio());
				Integer totalActual = 0;
				if(convenioServicio.getConvenioServicioComponentes() != null && convenioServicio.getConvenioServicioComponentes().size() > 0){
					int size = convenioServicio.getConvenioServicioComponentes().size();
					ConvenioServicioComponente convenioServicioComponente = convenioServicio.getConvenioServicioComponentes().get(size-1);
					convenioServicioComponente.setAprobado(true);
					totalActual += convenioServicioComponente.getMontoIngresado();
				}
				ConvenioServicioComponente convenioComunaComponenteActualizar = new ConvenioServicioComponente();
				convenioComunaComponenteActualizar.setAprobado(false);
				convenioComunaComponenteActualizar.setFecha(fechaActual);
				Componente componente = componenteDAO.getComponenteByID(cargaConvenioServicioComponenteVO.getComponente().getId());
				convenioComunaComponenteActualizar.setComponente(componente);
				convenioComunaComponenteActualizar.setConvenioServicio(convenioServicio);
				convenioComunaComponenteActualizar.setMontoIngresado(cargaConvenioServicioComponenteVO.getMontoIngresado());
				convenioComunaComponenteActualizar.setMonto(cargaConvenioServicioComponenteVO.getMontoIngresado() - totalActual);
				TipoSubtitulo subtitulo = tipoSubtituloDAO.getTipoSubtituloById(cargaConvenioServicioComponenteVO.getSubtitulo().getIdSubtitulo());
				convenioComunaComponenteActualizar.setSubtitulo(subtitulo);
				ReferenciaDocumento documentoConvenio = documentDAO.findById(docConvenio);
				convenioComunaComponenteActualizar.setDocumentoConvenio(documentoConvenio);
				conveniosDAO.save(convenioComunaComponenteActualizar);
				cargaConvenioServicioComponenteVONuevo = new CargaConvenioServicioComponenteMapper().getBasic(convenioComunaComponenteActualizar);
				cargaConvenioServicioComponenteVONuevo.setNuevo(false);
				cargaConvenioServicioComponenteVONuevo.setReemplazar(true);
			}else{
				ProgramaAno programa = programasDAO.getProgramaAnoByID(idProgramaAno);
				Mes mes = mesDAO.getMesPorID(Integer.parseInt(getMesCurso(true)));
				Establecimiento establecimiento = establecimientosDAO.getEstablecimientoById(cargaConvenioServicioComponenteVO.getIdEstablecimiento());
				Componente componente = componenteDAO.getComponenteByID(cargaConvenioServicioComponenteVO.getComponente().getId());
				List<ConvenioServicioComponente> conveniosServicioComponentePagada = conveniosDAO.getConveniosServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(idProgramaAno, componente.getId(), cargaConvenioServicioComponenteVO.getSubtitulo().getIdSubtitulo(), establecimiento.getId(), EstadosConvenios.PAGADO.getId());
				List<ConvenioServicioComponente> conveniosServicioComponenteEnTramite = conveniosDAO.getConveniosServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(idProgramaAno, componente.getId(), cargaConvenioServicioComponenteVO.getSubtitulo().getIdSubtitulo(), establecimiento.getId(), EstadosConvenios.TRAMITE.getId());
				Integer montoActual = 0;
				boolean reemplazar = true;
				if(conveniosServicioComponentePagada != null && conveniosServicioComponentePagada.size() > 0){
					int size = conveniosServicioComponentePagada.size();
					ConvenioServicioComponente convenioServicioComponente = conveniosServicioComponentePagada.get(size -1);
					montoActual =  convenioServicioComponente.getMontoIngresado();
					reemplazar = false;
				}
				if(conveniosServicioComponenteEnTramite != null && conveniosServicioComponenteEnTramite.size() > 0){
					int size = conveniosServicioComponenteEnTramite.size();
					ConvenioServicioComponente convenioServicioComponente = conveniosServicioComponenteEnTramite.get(size -1);
					montoActual =  convenioServicioComponente.getMontoIngresado();
					reemplazar = false;
				}
				ConvenioServicio convenioServicio = new ConvenioServicio();
				convenioServicio.setFecha(fechaActual);
				convenioServicio.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.INGRESADO.getId()));
				convenioServicio.setNumeroResolucion(cargaConvenioServicioComponenteVO.getNumeroResoucion());
				convenioServicio.setIdEstablecimiento(establecimiento);
				convenioServicio.setIdPrograma(programa);
				convenioServicio.setMes(mes);
				conveniosDAO.save(convenioServicio);
				ConvenioServicioComponente convenioServicioComponenteActualizar = new ConvenioServicioComponente();
				convenioServicioComponenteActualizar.setAprobado(false);
				convenioServicioComponenteActualizar.setFecha(fechaActual);
				convenioServicioComponenteActualizar.setComponente(componente);
				convenioServicioComponenteActualizar.setConvenioServicio(convenioServicio);
				convenioServicioComponenteActualizar.setMontoIngresado(cargaConvenioServicioComponenteVO.getMontoIngresado());
				convenioServicioComponenteActualizar.setMonto(cargaConvenioServicioComponenteVO.getMontoIngresado() - montoActual);
				TipoSubtitulo subtitulo = tipoSubtituloDAO.getTipoSubtituloById(cargaConvenioServicioComponenteVO.getSubtitulo().getIdSubtitulo());
				convenioServicioComponenteActualizar.setSubtitulo(subtitulo);
				ReferenciaDocumento documentoConvenio = documentDAO.findById(docConvenio);
				convenioServicioComponenteActualizar.setDocumentoConvenio(documentoConvenio);
				conveniosDAO.save(convenioServicioComponenteActualizar);
				cargaConvenioServicioComponenteVONuevo = new CargaConvenioServicioComponenteMapper().getBasic(convenioServicioComponenteActualizar);
				cargaConvenioServicioComponenteVONuevo.setNuevo(false);
				cargaConvenioServicioComponenteVONuevo.setReemplazar(reemplazar);
			}
		}
		return cargaConvenioServicioComponenteVONuevo;
	}
	
	public CargaConvenioComunaComponenteVO guardarConvenioComunaComponente(Integer idProgramaAno, CargaConvenioComunaComponenteVO cargaConvenioComunaComponenteVO, Integer docConvenio) {
		CargaConvenioComunaComponenteVO cargaConvenioComunaComponenteVONuevo = null;
		Date fechaActual = new Date();
		if(cargaConvenioComunaComponenteVO.getNuevo()){
			ProgramaAno programa = programasDAO.getProgramaAnoByID(idProgramaAno);
			Mes mes = mesDAO.getMesPorID(Integer.parseInt(getMesCurso(true)));
			Comuna comuna = comunaDAO.getComunaById(cargaConvenioComunaComponenteVO.getIdComuna());
			ConvenioComuna convenioComuna = new ConvenioComuna();
			convenioComuna.setFecha(fechaActual);
			convenioComuna.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.INGRESADO.getId()));
			convenioComuna.setNumeroResolucion(cargaConvenioComunaComponenteVO.getNumeroResoucion());
			convenioComuna.setIdComuna(comuna);
			convenioComuna.setIdPrograma(programa);
			convenioComuna.setMes(mes);
			conveniosDAO.save(convenioComuna);
			ConvenioComunaComponente convenioComunaComponente = new ConvenioComunaComponente();
			convenioComunaComponente.setAprobado(false);
			convenioComunaComponente.setFecha(fechaActual);
			Componente componente = componenteDAO.getComponenteByID(cargaConvenioComunaComponenteVO.getComponente().getId());
			convenioComunaComponente.setComponente(componente);
			convenioComunaComponente.setConvenioComuna(convenioComuna);
			convenioComunaComponente.setMontoIngresado(cargaConvenioComunaComponenteVO.getMontoIngresado());
			convenioComunaComponente.setMonto(cargaConvenioComunaComponenteVO.getMontoIngresado());
			TipoSubtitulo subtitulo = tipoSubtituloDAO.getTipoSubtituloById(cargaConvenioComunaComponenteVO.getSubtitulo().getIdSubtitulo());
			convenioComunaComponente.setSubtitulo(subtitulo);
			ReferenciaDocumento documentoConvenio = documentDAO.findById(docConvenio);
			convenioComunaComponente.setDocumentoConvenio(documentoConvenio);
			conveniosDAO.save(convenioComunaComponente);
			cargaConvenioComunaComponenteVONuevo = new CargaConvenioComunaComponenteMapper().getBasic(convenioComunaComponente);
			cargaConvenioComunaComponenteVONuevo.setNuevo(false);
			cargaConvenioComunaComponenteVONuevo.setReemplazar(true);
		}else{
			System.out.println("*********cargaConvenioComunaComponenteVO-->"+cargaConvenioComunaComponenteVO);
			if(cargaConvenioComunaComponenteVO.getReemplazar()){
				ConvenioComuna convenioComuna = conveniosDAO.getConvenioComunaById(cargaConvenioComunaComponenteVO.getIdConvenioComuna());
				Integer totalActual = 0;
				if(convenioComuna.getConvenioComunaComponentes() != null && convenioComuna.getConvenioComunaComponentes().size() > 0){
					int size = convenioComuna.getConvenioComunaComponentes().size();
					ConvenioComunaComponente convenioComunaComponente = convenioComuna.getConvenioComunaComponentes().get(size-1);
					convenioComunaComponente.setAprobado(true);
					totalActual += convenioComunaComponente.getMontoIngresado();
				}
				ConvenioComunaComponente convenioComunaComponenteActualizar  = new ConvenioComunaComponente();
				convenioComunaComponenteActualizar.setAprobado(false);
				convenioComunaComponenteActualizar.setFecha(fechaActual);
				Componente componente = componenteDAO.getComponenteByID(cargaConvenioComunaComponenteVO.getComponente().getId());
				convenioComunaComponenteActualizar.setComponente(componente);
				convenioComunaComponenteActualizar.setConvenioComuna(convenioComuna);
				convenioComunaComponenteActualizar.setMontoIngresado(cargaConvenioComunaComponenteVO.getMontoIngresado());
				convenioComunaComponenteActualizar.setMonto(cargaConvenioComunaComponenteVO.getMontoIngresado() - totalActual);
				TipoSubtitulo subtitulo = tipoSubtituloDAO.getTipoSubtituloById(cargaConvenioComunaComponenteVO.getSubtitulo().getIdSubtitulo());
				convenioComunaComponenteActualizar.setSubtitulo(subtitulo);
				ReferenciaDocumento documentoConvenio = documentDAO.findById(docConvenio);
				convenioComunaComponenteActualizar.setDocumentoConvenio(documentoConvenio);
				conveniosDAO.save(convenioComunaComponenteActualizar);
				cargaConvenioComunaComponenteVONuevo = new CargaConvenioComunaComponenteMapper().getBasic(convenioComunaComponenteActualizar);
				cargaConvenioComunaComponenteVONuevo.setNuevo(false);
				cargaConvenioComunaComponenteVONuevo.setReemplazar(true);
			}else{
				ProgramaAno programa = programasDAO.getProgramaAnoByID(idProgramaAno);
				Mes mes = mesDAO.getMesPorID(Integer.parseInt(getMesCurso(true)));
				Comuna comuna = comunaDAO.getComunaById(cargaConvenioComunaComponenteVO.getIdComuna());
				Componente componente = componenteDAO.getComponenteByID(cargaConvenioComunaComponenteVO.getComponente().getId());
				List<ConvenioComunaComponente> conveniosComunaComponentePagada = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, componente.getId(), cargaConvenioComunaComponenteVO.getSubtitulo().getIdSubtitulo(), comuna.getId(), EstadosConvenios.PAGADO.getId());
				List<ConvenioComunaComponente> conveniosComunaComponenteEnTramite = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, componente.getId(), cargaConvenioComunaComponenteVO.getSubtitulo().getIdSubtitulo(), comuna.getId(), EstadosConvenios.TRAMITE.getId());
				Integer montoActual = 0;
				boolean reemplazar = true;
				if(conveniosComunaComponentePagada != null && conveniosComunaComponentePagada.size() > 0){
					int size = conveniosComunaComponentePagada.size();
					ConvenioComunaComponente convenioComunaComponente = conveniosComunaComponentePagada.get(size -1);
					montoActual =  convenioComunaComponente.getMontoIngresado();
					reemplazar = false;
				}
				if(conveniosComunaComponenteEnTramite != null && conveniosComunaComponenteEnTramite.size() > 0){
					int size = conveniosComunaComponenteEnTramite.size();
					ConvenioComunaComponente convenioComunaComponente = conveniosComunaComponenteEnTramite.get(size -1);
					montoActual =  convenioComunaComponente.getMontoIngresado();
					reemplazar = false;
				}
				ConvenioComuna convenioComuna = new ConvenioComuna();
				convenioComuna.setFecha(fechaActual);
				convenioComuna.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.INGRESADO.getId()));
				convenioComuna.setNumeroResolucion(cargaConvenioComunaComponenteVO.getNumeroResoucion());
				convenioComuna.setIdComuna(comuna);
				convenioComuna.setIdPrograma(programa);
				convenioComuna.setMes(mes);
				conveniosDAO.save(convenioComuna);
				ConvenioComunaComponente convenioComunaComponenteActualizar = new ConvenioComunaComponente();
				convenioComunaComponenteActualizar.setAprobado(false);
				convenioComunaComponenteActualizar.setFecha(fechaActual);
				convenioComunaComponenteActualizar.setComponente(componente);
				convenioComunaComponenteActualizar.setConvenioComuna(convenioComuna);
				convenioComunaComponenteActualizar.setMontoIngresado(cargaConvenioComunaComponenteVO.getMontoIngresado());
				convenioComunaComponenteActualizar.setMonto(cargaConvenioComunaComponenteVO.getMontoIngresado() - montoActual);
				TipoSubtitulo subtitulo = tipoSubtituloDAO.getTipoSubtituloById(cargaConvenioComunaComponenteVO.getSubtitulo().getIdSubtitulo());
				convenioComunaComponenteActualizar.setSubtitulo(subtitulo);
				ReferenciaDocumento documentoConvenio = documentDAO.findById(docConvenio);
				convenioComunaComponenteActualizar.setDocumentoConvenio(documentoConvenio);
				conveniosDAO.save(convenioComunaComponenteActualizar);
				cargaConvenioComunaComponenteVONuevo = new CargaConvenioComunaComponenteMapper().getBasic(convenioComunaComponenteActualizar);
				cargaConvenioComunaComponenteVONuevo.setNuevo(false);
				cargaConvenioComunaComponenteVONuevo.setReemplazar(reemplazar);
			}
		}
		return cargaConvenioComunaComponenteVONuevo;
	}
	
}
