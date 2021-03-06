package cl.minsal.divap.controller;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;

import minsal.divap.enums.TareasSeguimiento;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.service.DistribucionInicialPercapitaService;
import minsal.divap.service.DocumentService;
import minsal.divap.service.ServicioSaludService;
import minsal.divap.util.StringUtil;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.ReferenciaDocumentoVO;
import minsal.divap.vo.SeguimientoVO;
import minsal.divap.vo.ServiciosSummaryVO;
import minsal.divap.vo.ServiciosVO;

import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named("procesoAsignacionPerCapitaSeguimientoController")
@ViewScoped
public class ProcesoAsignacionPerCapitaSeguimientoController extends AbstractTaskMBean
implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7833255114459148554L;
	@Inject
	private transient Logger log;
	@EJB
	private DistribucionInicialPercapitaService distribucionInicialPercapitaService;
	@EJB
	private ServicioSaludService serviciosService;
	@EJB
	private DocumentService documentService;
	private List<SeguimientoVO> bitacoraSeguimiento;
	private List<ServiciosVO> servicios;
	private List<ReferenciaDocumentoVO> documentos;
	private List<ServiciosSummaryVO> serviciosResoluciones;
	private String servicioSeleccionado;
	private TareasSeguimiento tareaSeguimiento;
	private String actividadSeguimientoTitle;
	private String to;
	private String cc;
	private String cco;
	private String subject;
	private String body;
	private boolean verRecargarArchivos;
	private boolean verRevalorizar;
	private boolean verBusqueda;

	private boolean versionFinal;
	private String docIdDownload;

	private Integer oficioConsultaId;
	private Integer decretoId;
	private Integer plantillaCorreoId;
	private ReferenciaDocumentoSummaryVO documentoPoblacionInscrita;
	private ReferenciaDocumentoSummaryVO documentoPlanillaOficio;
	private ReferenciaDocumentoSummaryVO documentoTomaRazon;
	private ReferenciaDocumentoSummaryVO documentoPlanillaDecreto;

	private  boolean rechazarRevalorizar_;
	private  boolean rechazarSubirArchivos_;
	private  boolean aprobar_;


	private UploadedFile attachedFile;
	private UploadedFile file;
	private UploadedFile file2;
	private Part fileUpload;
	private Integer idDistribucionInicialPercapita;
	private Integer ano;
	private Boolean lastVersion = false;
	private String hiddenIdServicio;


	public void uploadArchivoSeguimiento() {
		if (attachedFile != null) {
			FacesMessage msg = new FacesMessage(
					"Los archivos fueron cargados correctamente.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void uploadVersion() {
		if (file != null){
			System.out.println("uploadVersion file is not null");
			String filename = file.getFileName();
			filename = StringUtil.removeSpanishAccents(filename);
			byte[] contentAttachedFile = file.getContents();
			Integer docNewVersion = persistFile(filename,	contentAttachedFile);
			switch (tareaSeguimiento) {
			case HACERSEGUIMIENTOOFICIO:
				distribucionInicialPercapitaService.moveToAlfresco(idDistribucionInicialPercapita, docNewVersion, TipoDocumentosProcesos.OFICIOCONSULTA, versionFinal, this.ano);
				this.oficioConsultaId = docNewVersion;
				break;
			case HACERSEGUIMIENTORESOLUCIONES:
				break;	
			case HACERSEGUIMIENTOTOMARAZON:
				distribucionInicialPercapitaService.moveToAlfresco(idDistribucionInicialPercapita, docNewVersion, TipoDocumentosProcesos.TOMARAZONAPORTEESTATAL, versionFinal, this.ano);
				this.documentoTomaRazon.setId(docNewVersion);
				break;
			case HACERSEGUIMIENTODECRETO:
				distribucionInicialPercapitaService.moveToAlfresco(idDistribucionInicialPercapita, docNewVersion, TipoDocumentosProcesos.BORRADORAPORTEESTATAL, versionFinal, this.ano);
				this.decretoId = docNewVersion;
				break;	
			default:
				break;
			}
		}else{
			System.out.println("uploadVersion file is null");
			FacesMessage message = new FacesMessage("uploadVersion file is null");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	public void uploadVersion2() {
		if (file2 != null){
			try {
				System.out.println("uploadVersion2 file2 is not null");
				String filename = file2.getFileName();
				filename = StringUtil.removeSpanishAccents(filename);
				filename = filename.replaceAll(" ", "");
				byte[] contentPlantillaFile = file2.getContents();
				File file = createTemporalFile(filename, contentPlantillaFile);
				plantillaCorreoId = distribucionInicialPercapitaService.cargarPlantillaCorreo(this.tareaSeguimiento, file);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("uploadVersion2 file is null");
			FacesMessage message = new FacesMessage("uploadVersion2 file is null");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	public void uploadVersionFinal() {
		if (file != null){
			try {
				System.out.println("uploadVersionFinal file is not null");
				String filename = file.getFileName();
				filename = StringUtil.removeSpanishAccents(filename);
				filename = filename.replaceAll(" ", "");
				byte[] contentResolucionFile = file.getContents();
				Integer docResolucion = persistFile(filename, contentResolucionFile);
				Integer idServicio = Integer.parseInt(getHiddenIdServicio());
				System.out.println("docResolucion->"+docResolucion);
				System.out.println("idServicio->"+idServicio);
				distribucionInicialPercapitaService.moveToAlfrescoDistribucionInicialPercapita(this.idDistribucionInicialPercapita, idServicio, docResolucion, TipoDocumentosProcesos.RESOLUCIONAPORTEESTATALUR, this.lastVersion, this.ano);
				Boolean versionFinal = (this.lastVersion != null && this.lastVersion) ? true : false;
				if(serviciosResoluciones != null && serviciosResoluciones.size() > 0){
					for(ServiciosSummaryVO  serviciosSummaryVO : serviciosResoluciones){
						if(serviciosSummaryVO.getId_servicio().equals(idServicio)){
							serviciosSummaryVO.setVersionFinal(versionFinal);
						}
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("uploadVersion file is null");
			FacesMessage message = new FacesMessage("uploadVersion file is null");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	public String uploadVersionModal() {
		if (attachedFile != null){
			String filename = attachedFile.getFileName();
			byte[] contentAttachedFile = attachedFile.getContents();
			Integer docAttachedFile = persistFile(filename,	contentAttachedFile);
			System.out.println("docAttachedFile="+docAttachedFile);
		}
		return null;
	}

	public void handleAttachedFile(FileUploadEvent event) {
		FacesMessage msg = new FacesMessage("Succesful", event.getFile()
				.getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		System.out.println("handleAttachedFile");
	}

	public void handleFile(FileUploadEvent event) {
		FacesMessage msg = new FacesMessage("Succesful", event.getFile()
				.getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		System.out.println("handleFile");
	}

	public void handleFile2(FileUploadEvent event) {
		FacesMessage msg = new FacesMessage("Succesful", event.getFile()
				.getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		System.out.println("handleFile");
	}

	public String sendMail(){
		String target = "divapProcesoAsignacionPerCapitaSeguimiento";
		try{
			List<Integer> documentos = null;
			if (attachedFile != null){
				documentos = new ArrayList<Integer>();
				String filename = attachedFile.getFileName();
				System.out.println("attachedFile------>"+filename);
				byte[] contentAttachedFile = attachedFile.getContents();
				Integer docAttachedFile = persistFile(filename,	contentAttachedFile);
				if (docAttachedFile != null) {
					documentos.add(docAttachedFile);
				}
			}
			if (file != null){
				String filename = file.getFileName();
				System.out.println("filename------>"+filename);
			}
			List<String> para = Arrays.asList(this.to.split("\\,"));
			List<String> conCopia = null;
			if((this.cc != null) && !(this.cc.trim().isEmpty())){
				conCopia = Arrays.asList(this.cc.split("\\,")); 
			}
			List<String> conCopiaOculta = null;
			if((this.cco != null) && !(this.cco.trim().isEmpty())){
				conCopiaOculta = Arrays.asList(this.cco.split("\\,")); 
			}
			System.out.println("ProcesoAsignacionPerCapitaSeguimientoController-->sendMail");
			distribucionInicialPercapitaService.createSeguimientoDistribucionInicialPercapita(idDistribucionInicialPercapita, tareaSeguimiento, subject, body, getSessionBean().getUsername(), 
					para, conCopia, conCopiaOculta, documentos, this.ano);
		}catch(Exception e){
			e.printStackTrace();
			target = null;
		}
		return target;
	}

	public void buscar() {
		System.out.println("buscar--> servicioSeleccionado="+servicioSeleccionado);
		TipoDocumentosProcesos[] tiposDocumentos = {TipoDocumentosProcesos.RESOLUCIONAPORTEESTATALUR, TipoDocumentosProcesos.RESOLUCIONAPORTEESTATALCF};
		if(servicioSeleccionado == null || servicioSeleccionado.trim().isEmpty()){
			serviciosResoluciones = documentService.getDocumentByResolucionTypesServicioDistribucionInicialPercapita(idDistribucionInicialPercapita, null, tiposDocumentos);
		}else{
			serviciosResoluciones = documentService.getDocumentByResolucionTypesServicioDistribucionInicialPercapita(idDistribucionInicialPercapita, Integer.parseInt(servicioSeleccionado), tiposDocumentos);
		}
		System.out.println("fin buscar-->");
	}

	public void limpiar() {
		System.out.println("Limpiar-->");
		servicioSeleccionado = "";
		documentos = new ArrayList<ReferenciaDocumentoVO>();
		serviciosResoluciones = new ArrayList<ServiciosSummaryVO>();
		System.out.println("fin limpiar");
	}

	public void resetLastVersion(){
		System.out.println("resetLastVersion lastVersion = false");
		lastVersion = false;
	}

	@Override
	public String toString() {
		return "ProcesoAsignacionPerCapitaController [validarMontosDistribucion=]";
	}

	@PostConstruct
	public void init() {
		log.info("ProcesosPrincipalController Alcanzado.");
		if(sessionExpired()){
			return;
		}
		if (getTaskDataVO() != null && getTaskDataVO().getData() != null) {
			this.idDistribucionInicialPercapita = (Integer) getTaskDataVO()
					.getData().get("_idDistribucionInicialPercapita");
			System.out.println("this.idDistribucionInicialPercapita --->"
					+ this.idDistribucionInicialPercapita);
			this.actividadSeguimientoTitle = getTaskDataVO().getTask().getName();
			System.out.println("this.actividadSeguimientoTitle --->"
					+ this.actividadSeguimientoTitle);
			String _tareaSeguimiento = (String) getTaskDataVO()
					.getData().get("_tareaSeguimiento");
			this.ano = (Integer) getTaskDataVO()
					.getData().get("_ano");
			ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO = distribucionInicialPercapitaService.getLastDocumentoSummaryByDistribucionInicialPercapitaType(idDistribucionInicialPercapita, TipoDocumentosProcesos.OFICIOCONSULTA);
			if(referenciaDocumentoSummaryVO != null){
				this.oficioConsultaId = referenciaDocumentoSummaryVO.getId();
			}else{
				this.oficioConsultaId = (Integer) getTaskDataVO().getData().get("_oficioConsultaId");
			}
			ReferenciaDocumentoSummaryVO referenciaDocumentoBorradorSummaryVO = distribucionInicialPercapitaService.getLastDocumentoSummaryByDistribucionInicialPercapitaType(idDistribucionInicialPercapita, TipoDocumentosProcesos.BORRADORAPORTEESTATAL);
			if(referenciaDocumentoBorradorSummaryVO != null){
				this.decretoId = referenciaDocumentoBorradorSummaryVO.getId();
			}else{
				this.decretoId = (Integer)getTaskDataVO().getData().get("_borradorAporteEstatalId");
			}
			this.tareaSeguimiento = TareasSeguimiento.getById(Integer.valueOf(_tareaSeguimiento));
		}
		documentoPoblacionInscrita = distribucionInicialPercapitaService.getLastDocumentoSummaryByDistribucionInicialPercapitaType(idDistribucionInicialPercapita, TipoDocumentosProcesos.POBLACIONINSCRITA);
		documentoPlanillaOficio = distribucionInicialPercapitaService.getLastDocumentoSummaryByDistribucionInicialPercapitaType(idDistribucionInicialPercapita, TipoDocumentosProcesos.PLANILLAOFICIOCONSULTA);
		documentoTomaRazon = distribucionInicialPercapitaService.getLastDocumentoSummaryByDistribucionInicialPercapitaType(idDistribucionInicialPercapita, TipoDocumentosProcesos.TOMARAZONAPORTEESTATAL);
		if(documentoTomaRazon == null){
			documentoTomaRazon = distribucionInicialPercapitaService.getLastDocumentoSummaryByDistribucionInicialPercapitaType(idDistribucionInicialPercapita, TipoDocumentosProcesos.BORRADORAPORTEESTATAL);
		}
		documentoPlanillaDecreto = distribucionInicialPercapitaService.getLastDocumentoSummaryByDistribucionInicialPercapitaType(idDistribucionInicialPercapita, TipoDocumentosProcesos.PLANILLABORRADORAPORTEESTATAL);
		bitacoraSeguimiento = distribucionInicialPercapitaService.getBitacora(this.idDistribucionInicialPercapita, this.tareaSeguimiento);
		plantillaCorreoId = distribucionInicialPercapitaService.getPlantillaCorreo(this.tareaSeguimiento);
	}

	public void handleFileUpload(FileUploadEvent event) {
		FacesMessage msg = new FacesMessage("Succesful", event.getFile()
				.getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		System.out.println("createResultData usuario-->"+getSessionBean().getUsername());
		parameters.put("usuario", getSessionBean().getUsername());
		parameters.put("rechazarRevalorizar_", new Boolean(isRechazarRevalorizar_()));
		parameters.put("rechazarSubirArchivos_", new Boolean(isRechazarSubirArchivos_()));
		parameters.put("enviarOficioConsultaRegional_", new Boolean(true));
		parameters.put("aprobar_", new Boolean(isAprobar_()));
		return parameters;
	}

	public ReferenciaDocumentoSummaryVO getDocumentoPoblacionInscrita() {
		return documentoPoblacionInscrita;
	}

	public void setDocumentoPoblacionInscrita(
			ReferenciaDocumentoSummaryVO documentoPoblacionInscrita) {
		this.documentoPoblacionInscrita = documentoPoblacionInscrita;
	}

	public boolean isVersionFinal() {
		return versionFinal;
	}

	public void setVersionFinal(boolean versionFinal) {
		this.versionFinal = versionFinal;
	}

	public boolean isRechazarRevalorizar_() {
		return rechazarRevalorizar_;
	}

	public void setRechazarRevalorizar_(boolean rechazarRevalorizar_) {
		this.rechazarRevalorizar_ = rechazarRevalorizar_;
	}

	public boolean isRechazarSubirArchivos_() {
		return rechazarSubirArchivos_;
	}

	public void setRechazarSubirArchivos_(boolean rechazarSubirArchivos_) {
		this.rechazarSubirArchivos_ = rechazarSubirArchivos_;
	}

	public boolean isAprobar_() {
		return aprobar_;
	}

	public void setAprobar_(boolean aprobar_) {
		this.aprobar_ = aprobar_;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getCco() {
		return cco;
	}

	public void setCco(String cco) {
		this.cco = cco;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public UploadedFile getAttachedFile() {
		return attachedFile;
	}

	public void setAttachedFile(UploadedFile attachedFile) {
		this.attachedFile = attachedFile;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public Integer getIdDistribucionInicialPercapita() {
		return idDistribucionInicialPercapita;
	}

	public void setIdDistribucionInicialPercapita(
			Integer idDistribucionInicialPercapita) {
		this.idDistribucionInicialPercapita = idDistribucionInicialPercapita;
	}

	public String getActividadSeguimientoTitle() {
		return actividadSeguimientoTitle;
	}

	public void setActividadSeguimientoTitle(String actividadSeguimientoTitle) {
		this.actividadSeguimientoTitle = actividadSeguimientoTitle;
	}

	public List<SeguimientoVO> getBitacoraSeguimiento() {
		return bitacoraSeguimiento;
	}

	public void setBitacoraSeguimiento(List<SeguimientoVO> bitacoraSeguimiento) {
		this.bitacoraSeguimiento = bitacoraSeguimiento;
	}

	public boolean isVerRecargarArchivos() {
		switch (tareaSeguimiento) {
		case HACERSEGUIMIENTORESOLUCIONES:
		case HACERSEGUIMIENTOOFICIO:
			verRecargarArchivos = false;
			break;
		case HACERSEGUIMIENTODECRETO:
		case HACERSEGUIMIENTOTOMARAZON:
			verRecargarArchivos = true;
			break;
		default:
			break;
		}
		return verRecargarArchivos;
	}

	public void setVerRecargarArchivos(boolean verRecargarArchivos) {
		this.verRecargarArchivos = verRecargarArchivos;
	}

	public boolean isVerRevalorizar() {
		switch (tareaSeguimiento) {
		case HACERSEGUIMIENTORESOLUCIONES:
		case HACERSEGUIMIENTOOFICIO:
			verRevalorizar = false;
			break;
		case HACERSEGUIMIENTODECRETO:
		case HACERSEGUIMIENTOTOMARAZON:
			verRevalorizar = true;
			break;	
		default:
			break;
		}
		return verRevalorizar;
	}

	public boolean isVerBusqueda() {
		switch (tareaSeguimiento) {
		case HACERSEGUIMIENTOOFICIO:
		case HACERSEGUIMIENTODECRETO:
		case HACERSEGUIMIENTOTOMARAZON:
			verBusqueda = false;
			break;
		case HACERSEGUIMIENTORESOLUCIONES:
			verBusqueda = true;
			break;	
		default:
			break;
		}
		return verBusqueda;
	}

	public String downloadPlanilla() {
		Integer docDownload = Integer.valueOf(Integer.parseInt(getDocIdDownload()));
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
		return null;
	}

	public String downloadResolucion() {
		Integer idServicio = Integer.valueOf(Integer.parseInt(getDocIdDownload()));
		ServiciosSummaryVO serviciosSummaryVO = serviciosService.getServicioSaludSummaryById(idServicio);
		TipoDocumentosProcesos[] tiposDocumentos = {TipoDocumentosProcesos.RESOLUCIONAPORTEESTATALUR, TipoDocumentosProcesos.RESOLUCIONAPORTEESTATALCF};
		List<Integer> documentos = documentService.getDocumentosByDistribucionInicialPercapitaServicioTypes(idDistribucionInicialPercapita, idServicio, tiposDocumentos);
		setDocumento(documentService.getDocument(serviciosSummaryVO.getNombre_servicio(), documentos));
		super.downloadDocument();
		return null;
	}

	public void clearFormUpload(){
		System.out.println("clear form");
		this.versionFinal = false;
		this.file = null;
	}

	public void setVerBusqueda(boolean verBusqueda) {
		this.verBusqueda = verBusqueda;
	}

	public void setVerRevalorizar(boolean verRevalorizar) {
		this.verRevalorizar = verRevalorizar;
	}

	public Integer getOficioConsultaId() {
		return oficioConsultaId;
	}

	public void setOficioConsultaId(Integer oficioConsultaId) {
		this.oficioConsultaId = oficioConsultaId;
	}

	public TareasSeguimiento getTareaSeguimiento() {
		return tareaSeguimiento;
	}

	public void setTareaSeguimiento(TareasSeguimiento tareaSeguimiento) {
		this.tareaSeguimiento = tareaSeguimiento;
	}

	public String getDocIdDownload() {
		return docIdDownload;
	}

	public void setDocIdDownload(String docIdDownload) {
		this.docIdDownload = docIdDownload;
	}

	public Part getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(Part fileUpload) {
		this.fileUpload = fileUpload;
	}

	public Integer getDecretoId() {
		return decretoId;
	}

	public void setDecretoId(Integer decretoId) {
		this.decretoId = decretoId;
	}

	public List<ServiciosVO> getServicios() {
		if(servicios == null){
			servicios = serviciosService.getAllServiciosVO();
		}
		return servicios;
	}

	public void setServicios(List<ServiciosVO> servicios) {
		this.servicios = servicios;
	}

	public String getServicioSeleccionado() {
		return servicioSeleccionado;
	}

	public void setServicioSeleccionado(String servicioSeleccionado) {
		this.servicioSeleccionado = servicioSeleccionado;
	}

	public List<ReferenciaDocumentoVO> getDocumentos() {
		return documentos;
	}

	public void setDocumentos(List<ReferenciaDocumentoVO> documentos) {
		this.documentos = documentos;
	}

	public Integer getPlantillaCorreoId() {
		return plantillaCorreoId;
	}

	public void setPlantillaCorreoId(Integer plantillaCorreoId) {
		this.plantillaCorreoId = plantillaCorreoId;
	}

	public UploadedFile getFile2() {
		return file2;
	}

	public void setFile2(UploadedFile file2) {
		this.file2 = file2;
	}

	public List<ServiciosSummaryVO> getServiciosResoluciones() {
		return serviciosResoluciones;
	}

	public void setServiciosResoluciones(
			List<ServiciosSummaryVO> serviciosResoluciones) {
		this.serviciosResoluciones = serviciosResoluciones;
	}

	public Boolean getLastVersion() {
		return lastVersion;
	}

	public void setLastVersion(Boolean lastVersion) {
		this.lastVersion = lastVersion;
	}

	public String getHiddenIdServicio() {
		return hiddenIdServicio;
	}

	public void setHiddenIdServicio(String hiddenIdServicio) {
		this.hiddenIdServicio = hiddenIdServicio;
	}

	@Override
	public String enviar(){
		int numDocFinales = 0;
		String message = null;
		switch (tareaSeguimiento) {
		case HACERSEGUIMIENTOOFICIO:
			numDocFinales = distribucionInicialPercapitaService.countVersionFinalDistribucionInicialPercapitaByType(this.idDistribucionInicialPercapita, TipoDocumentosProcesos.OFICIOCONSULTA);
			message = "No existe versión final para oficio consulta";
			break;
		case HACERSEGUIMIENTORESOLUCIONES:
			TipoDocumentosProcesos[] tiposDocumentos = {TipoDocumentosProcesos.RESOLUCIONAPORTEESTATALUR, TipoDocumentosProcesos.RESOLUCIONAPORTEESTATALCF};
			List<ServiciosSummaryVO> serviciosResoluciones = documentService.getDocumentResolucionByTypesServicioDistribucionInicialPercapita(idDistribucionInicialPercapita, null, tiposDocumentos);
			if(serviciosResoluciones != null && serviciosResoluciones.size() > 0){
				for(ServiciosSummaryVO serviciosSummaryVO : serviciosResoluciones){
					numDocFinales = distribucionInicialPercapitaService.countVersionFinalDistribucionInicialPercapitaResoluciones(this.idDistribucionInicialPercapita, serviciosSummaryVO.getId_servicio());
					if(numDocFinales == 0){
						message = "No existe versión final para documento aporte estatal servicio " + serviciosSummaryVO.getNombre_servicio();
						break;
					}
				}
			}
			break;	
		case HACERSEGUIMIENTOTOMARAZON:
			numDocFinales = distribucionInicialPercapitaService.countVersionFinalDistribucionInicialPercapitaByType(this.idDistribucionInicialPercapita, TipoDocumentosProcesos.TOMARAZONAPORTEESTATAL);
			message = "No existe versión final para documento toma razon de aporte estatal";
			break;	
		case HACERSEGUIMIENTODECRETO:
			numDocFinales = distribucionInicialPercapitaService.countVersionFinalDistribucionInicialPercapitaByType(this.idDistribucionInicialPercapita, TipoDocumentosProcesos.BORRADORAPORTEESTATAL);
			message = "No existe versión final para documento aporte estatal";
			break;	
		default:
			break;
		}
		System.out.println("numDocFinales="+numDocFinales);
		if(numDocFinales == 0){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return null;
		}
		return super.enviar();
	}

	public ReferenciaDocumentoSummaryVO getDocumentoPlanillaOficio() {
		return documentoPlanillaOficio;
	}

	public void setDocumentoPlanillaOficio(
			ReferenciaDocumentoSummaryVO documentoPlanillaOficio) {
		this.documentoPlanillaOficio = documentoPlanillaOficio;
	}

	public ReferenciaDocumentoSummaryVO getDocumentoTomaRazon() {
		return documentoTomaRazon;
	}

	public void setDocumentoTomaRazon(
			ReferenciaDocumentoSummaryVO documentoTomaRazon) {
		this.documentoTomaRazon = documentoTomaRazon;
	}

	public ReferenciaDocumentoSummaryVO getDocumentoPlanillaDecreto() {
		return documentoPlanillaDecreto;
	}

	public void setDocumentoPlanillaDecreto(
			ReferenciaDocumentoSummaryVO documentoPlanillaDecreto) {
		this.documentoPlanillaDecreto = documentoPlanillaDecreto;
	}

	@Override
	public String iniciarProceso() {
		return null;
	}

}
