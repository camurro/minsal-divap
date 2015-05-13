package cl.minsal.divap.controller;

import java.io.IOException;
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

import minsal.divap.enums.TareasSeguimiento;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.service.EstimacionFlujoCajaService;
import minsal.divap.service.ProgramasService;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.SeguimientoVO;

import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import cl.minsal.divap.pojo.ProcesosProgramasPojo;
import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named("procesoEstimacionFlujoCajaConsolidadorHacerSeguimientoController")
@ViewScoped
public class ProcesoEstimacionFlujoCajaConsolidadorHacerSeguimientoController extends
		AbstractTaskMBean implements Serializable {
	private static final long serialVersionUID = 8979055329731411696L;
	@Inject
	private transient Logger log;

	//ID Programa
	private Integer idLineaProgramatica;
	
	@EJB
	private ProgramasService programaService;
	@EJB
	private EstimacionFlujoCajaService estimacionFlujoCajaService;
	
	private List<SeguimientoVO> bitacoraSeguimiento;
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
	
	private Integer ordinarioProgramacionCajaId;
	private ReferenciaDocumentoSummaryVO planillaProgramacionFlujoCaja;
	
	private  boolean rechazarRevalorizar_;
	private  boolean rechazarSubirArchivos_;
	private  boolean aprobar_;
	
	
	private UploadedFile attachedFile;
	private UploadedFile file;
	private Integer idOT;
	private Integer ano;

	//TODO: [ASAAVEDRA] Verificar cuando los programas estan iniciados.
	
	//Obtiene la lista de programas del usuario
	public List<ProcesosProgramasPojo> getListadoProgramasServicio() {
		List<ProcesosProgramasPojo> listadoProgramasServicio = new ArrayList<ProcesosProgramasPojo>();
		List<ProgramaVO> programas = programaService.getProgramasByUser(getLoggedUsername(), null);
		for (ProgramaVO programaVO : programas) {
			ProcesosProgramasPojo p2 = new ProcesosProgramasPojo();
			p2.setPrograma(programaVO.getNombre());
			p2.setDescripcion("descripcion");
			p2.setId(programaVO.getId());
			listadoProgramasServicio.add(p2);
		}
		return listadoProgramasServicio;
	}

	// Continua el proceso con el programa seleccionado.
	public String continuarProceso() {

		//setIdLineaProgramatica(id);
		setTarget("bandejaTareas");
		return super.enviar();
	}
	
	@Override
	public String toString() {
		return "Proceso Estimacion Flujo Caja Seleccionar Linea Controller";
	}

	@PostConstruct
	public void init() {
		log.info("Proceso Estimacion Flujo Caja Seleccionar Linea [Ingreso].");
		if (!getSessionBean().isLogged()) {
			log.warn("No hay usuario almacenado en sesion, se redirecciona a pantalla de login");
			try {
				facesContext.getExternalContext().redirect("login.jsf");
			} catch (IOException e) {
				log.error(
						"Error tratando de redireccionar a login por falta de usuario en sesion.",
						e);
			}
		} else {

		}
		if (getTaskDataVO() != null && getTaskDataVO().getData() != null) {
			this.ano = (Integer) getTaskDataVO().getData().get("_ano");
		}
		
		idLineaProgramatica = 1;
		//CORREO
		planillaProgramacionFlujoCaja = estimacionFlujoCajaService.getLastDocumentSummaryEstimacionFlujoCajaTipoDocumento(TipoDocumentosProcesos.PLANTILLAPROPUESTA);// distribucionInicialPercapitaService.getLastDocumentoSummaryByDistribucionInicialPercapitaType(idDistribucionInicialPercapita, TipoDocumentosProcesos.POBLACIONINSCRITA);
		bitacoraSeguimiento = estimacionFlujoCajaService.getBitacora(TareasSeguimiento.HACERSEGUIMIENTOESTIMACIONFLUJOCAJA);
		tareaSeguimiento = TareasSeguimiento.HACERSEGUIMIENTOESTIMACIONFLUJOCAJA;

	
		//Ultimo documento oficio.
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO = estimacionFlujoCajaService.getLastDocumentSummaryEstimacionFlujoCajaType(idLineaProgramatica,TipoDocumentosProcesos.PLANTILLABORRADORORDINARIOPROGRAMACIONCAJA);
		//ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO = distribucionInicialPercapitaService.getLastDocumentoSummaryByDistribucionInicialPercapitaType(idDistribucionInicialPercapita, TipoDocumentosProcesos.OFICIOCONSULTA);
		if(referenciaDocumentoSummaryVO != null){
			this.ordinarioProgramacionCajaId = referenciaDocumentoSummaryVO.getId();
		}else{
			this.ordinarioProgramacionCajaId = (Integer) getTaskDataVO().getData().get("_oficioConsultaId");
		}
		
		
	}

	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		return parameters;
	}

	@Override
	public String iniciarProceso() {
		return null;
	}
	
	public Integer getIdLineaProgramatica() {
		return idLineaProgramatica;
	}

	public void setIdLineaProgramatica(Integer idLineaProgramatica) {
		this.idLineaProgramatica = idLineaProgramatica;
	}

	public Integer getIdOT() {
		return idOT;
	}

	public void setIdOT(Integer idOT) {
		this.idOT = idOT;
	}

	public void uploadArchivoSeguimiento() {
		if (attachedFile != null) {
			FacesMessage msg = new FacesMessage(
					"Los archivos fueron cargados correctamente.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void upload() {
		if(file != null) {
			FacesMessage message = new FacesMessage("Archivo", file.getFileName() + " subio correctamente.");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	public void handleAttachedFile(FileUploadEvent event) {
		FacesMessage msg = new FacesMessage("Succesful", event.getFile()
				.getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		System.out.println("handleAttachedFile");
	}

	public String sendMail(){
		String target = "bandejaTareas";
		try{
			List<Integer> documentos = null;
			if (attachedFile != null){
				documentos = new ArrayList<Integer>();
				String filename = attachedFile.getFileName();
				byte[] contentAttachedFile = attachedFile.getContents();
				Integer docAttachedFile = persistFile(filename,	contentAttachedFile);
				if (docAttachedFile != null) {
					documentos.add(docAttachedFile);
				}
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

			System.out.println("[Estimacion Flujo Caja]-->sendMail");
			estimacionFlujoCajaService.createSeguimiento(idLineaProgramatica, tareaSeguimiento, subject, body, getSessionBean().getUsername(), para, conCopia, conCopiaOculta, documentos, getAno());
		}catch(Exception e){
			e.printStackTrace();
			target = null;
		}
		return target;
	}


	public void handleFileUpload(FileUploadEvent event) {
		FacesMessage msg = new FacesMessage("Succesful", event.getFile()
				.getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	

	public boolean isVersionFinal() {
		return versionFinal;
	}

	public void setVersionFinal(boolean versionFinal) {
		this.versionFinal = versionFinal;
	}

	public boolean isRechazarRevalorizar_() {
		System.out.println("rechazarRevalorizar_-->"+rechazarRevalorizar_);
		return rechazarRevalorizar_;
	}

	public void setRechazarRevalorizar_(boolean rechazarRevalorizar_) {
		this.rechazarRevalorizar_ = rechazarRevalorizar_;
	}

	public boolean isRechazarSubirArchivos_() {
		System.out.println("rechazarSubirArchivos_-->"+rechazarSubirArchivos_);
		return rechazarSubirArchivos_;
	}

	public void setRechazarSubirArchivos_(boolean rechazarSubirArchivos_) {
		this.rechazarSubirArchivos_ = rechazarSubirArchivos_;
	}

	public boolean isAprobar_() {
		System.out.println("aprobar_-->"+aprobar_);
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
		System.out.println("verRecargarArchivos-->"+verRecargarArchivos);
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
		System.out.println("verRevalorizar-->"+verRevalorizar);
		return verRevalorizar;
	}

	public boolean isVerBusqueda() {
		switch (tareaSeguimiento) {
		case HACERSEGUIMIENTOOFICIO:
			verBusqueda = false;
			break;
		case HACERSEGUIMIENTORESOLUCIONES:
		case HACERSEGUIMIENTODECRETO:
		case HACERSEGUIMIENTOTOMARAZON:
			verBusqueda = true;
			break;	
		default:
			break;
		}
		System.out.println("verBusqueda-->"+verBusqueda);
		return verBusqueda;
	}
	
	public String downloadPlanilla() {
		Integer docDownload = Integer.valueOf(Integer.parseInt(getDocIdDownload()));
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
		return null;
	}

	public void setVerBusqueda(boolean verBusqueda) {
		this.verBusqueda = verBusqueda;
	}

	public void setVerRevalorizar(boolean verRevalorizar) {
		this.verRevalorizar = verRevalorizar;
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

	/*Subir Archivos*/
	public void handleFile(FileUploadEvent event) {
		FacesMessage msg = new FacesMessage("Succesful", event.getFile()
				.getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		System.out.println("handleFile");
	}

	public void clearFormUpload(){
		System.out.println("clear form");
		this.versionFinal = false;
		this.file = null;
	}
	
	public void uploadVersion() {
		if (file != null){
			System.out.println("uploadVersion file is not null");
			String filename = file.getFileName();
			byte[] contentAttachedFile = file.getContents();
			Integer docNewVersion = persistFile(filename,	contentAttachedFile);
			TipoDocumentosProcesos tipoDocumento = TipoDocumentosProcesos.PLANTILLABORRADORORDINARIOPROGRAMACIONCAJA;
			estimacionFlujoCajaService.moveToAlfresco(null, docNewVersion,tipoDocumento,versionFinal, getAno());
			this.setOrdinarioProgramacionCajaId(docNewVersion);
		}else{
			System.out.println("uploadVersion file is null");
			FacesMessage message = new FacesMessage("uploadVersion file is null");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	public Integer getOrdinarioProgramacionCajaId() {
		return ordinarioProgramacionCajaId;
	}

	public void setOrdinarioProgramacionCajaId(
			Integer ordinarioProgramacionCajaId) {
		this.ordinarioProgramacionCajaId = ordinarioProgramacionCajaId;
	}

	public ReferenciaDocumentoSummaryVO getPlanillaProgramacionFlujoCaja() {
		return planillaProgramacionFlujoCaja;
	}

	public void setPlanillaProgramacionFlujoCaja(
			ReferenciaDocumentoSummaryVO planillaProgramacionFlujoCaja) {
		this.planillaProgramacionFlujoCaja = planillaProgramacionFlujoCaja;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}
	

}
