package cl.minsal.divap.controller;

import java.io.File;
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
import minsal.divap.service.AlfrescoService;
import minsal.divap.service.ProgramasService;
import minsal.divap.service.RecursosFinancierosProgramasReforzamientoService;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.SeguimientoVO;

import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import cl.minsal.divap.pojo.montosDistribucionPojo;
import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named ( "procesoDistRecFinSeguimientoController" ) 
@ViewScoped 
public class ProcesoDistRecFinSeguimientoController extends AbstractTaskMBean implements Serializable {
	private static final long serialVersionUID = 8979055329731411696L;
	private List<String> comunasSeleccionadas;
	private List<String> establecimientosSeleccionadas;
	private List<String> comunas;
	private String actividadSeguimientoTitle;
	private UploadedFile adjunto;
	private List<montosDistribucionPojo> planillaMontosDistribucion;
	
	private TareasSeguimiento tareaSeguimiento;
	private UploadedFile resolucion;
	private UploadedFile file;
	private boolean versionFinal;
	
	@Inject private transient Logger log;
	private Integer programaSeleccionado;
	
	private boolean servicio;
	private boolean municipal;
	private boolean mixto;
	
	@EJB
	private RecursosFinancierosProgramasReforzamientoService reforzamientoService;
	@EJB
	private ProgramasService programasService;
	@EJB
	private AlfrescoService alfrescoService;
	
	private String docIdDownloadResolucion;
	private String docIdDownloadExcelResolucion;
	private Integer resolucionPrograma;
	private Integer resumenMunicipalPais;
	private Integer resumenServicioPais;
	private Integer excelResolucion;
	private Integer ordinarioPrograma;
	private Integer excelOrdinario;
	
	private Integer plantillaResolucionCorreo;
	private Integer plantillaOrdinarioCorreo;
	
	private List<SeguimientoVO> bitacoraSeguimiento;
	private String to;
	private String cc;
	private String cco;
	private String subject;
	private String body;
	private UploadedFile attachedFile;
	private ProgramaVO programaEvaluacion;
	private Integer ano;
	private Integer idProceso;

	@PostConstruct 
	public void init() {
		if (!getSessionBean().isLogged()) {
			log.warn("No hay usuario almacenado en sesion, se redirecciona a pantalla de login");
			try {
				facesContext.getExternalContext().redirect("login.jsf");
			} catch (IOException e) {
				log.error("Error tratando de redireccionar a login por falta de usuario en sesion.", e);
			}
		}
		Boolean tipoProgramaPxQ = false;
		if (getTaskDataVO() != null && getTaskDataVO().getData() != null) {
			programaSeleccionado = (Integer) getTaskDataVO().getData().get("_programaSeleccionado");
			tipoProgramaPxQ = (Boolean) getTaskDataVO().getData().get("_tipoProgramaPxQ");
			this.ano = (Integer) getTaskDataVO().getData().get("_ano");
			this.idProceso = (Integer) getTaskDataVO().getData().get("_idProceso");
			System.out.println("this.ano --->" + this.ano);
			System.out.println("this.idProceso --->" + this.idProceso);
		}
		programaEvaluacion = programasService.getProgramaByIdProgramaAndAno(programaSeleccionado, ano);
		servicio = false;
		municipal = false;
		mixto = false;
		if(programaEvaluacion.getDependenciaMunicipal()){
			excelResolucion = reforzamientoService.getLastDocumentRecursosFinancierosByType(idProceso, programaEvaluacion.getIdProgramaAno(), TipoDocumentosProcesos.PLANTILLARESOLUCIONPROGRAMASAPS);
			resolucionPrograma = reforzamientoService.getLastDocumentRecursosFinancierosByType(idProceso, programaEvaluacion.getIdProgramaAno(), TipoDocumentosProcesos.RESOLUCIONPROGRAMASAPS);
			plantillaResolucionCorreo = documentService.getIdDocumentoFromPlantilla(TipoDocumentosProcesos.PLANTILLARESOLUCIONCORREO);
			municipal = true;
		}
		if(programaEvaluacion.getDependenciaServicio() && !programaEvaluacion.getDependenciaMunicipal()){
			excelOrdinario = reforzamientoService.getLastDocumentRecursosFinancierosByType(idProceso, programaEvaluacion.getIdProgramaAno(), TipoDocumentosProcesos.PLANTILLARESOLUCIONPROGRAMASAPS);
			ordinarioPrograma = reforzamientoService.getLastDocumentRecursosFinancierosByType(idProceso, programaEvaluacion.getIdProgramaAno(), TipoDocumentosProcesos.ORDINARIOPROGRAMASAPS);
			plantillaOrdinarioCorreo = documentService.getIdDocumentoFromPlantilla(TipoDocumentosProcesos.PLANTILLAORDINARIOCORREO);
			servicio = true;
		}
		
		if(tipoProgramaPxQ){
			//Servicio PxQ
			if(programaEvaluacion.getDependenciaServicio() && !programaEvaluacion.getDependenciaMunicipal()){
				resumenServicioPais = reforzamientoService.getLastDocumentRecursosFinancierosByType(idProceso, programaEvaluacion.getIdProgramaAno(), TipoDocumentosProcesos.PLANTILLASERVICIOAPSRESUMENSERVICIO);
				if(resumenServicioPais == null){
					resumenServicioPais = reforzamientoService.getIdPlantillaProgramasPais(idProceso, programaEvaluacion.getId(), this.ano, TipoDocumentosProcesos.PLANTILLASERVICIOAPSRESUMENSERVICIO);
				}
			}
			//Municipal PxQ
			if(programaEvaluacion.getDependenciaMunicipal() && !programaEvaluacion.getDependenciaServicio()){
				resumenMunicipalPais = reforzamientoService.getLastDocumentRecursosFinancierosByType(idProceso, programaEvaluacion.getIdProgramaAno(), TipoDocumentosProcesos.PLANTILLAMUNICIPALAPSRESUMENSERVICIO);
				if(resumenMunicipalPais == null){
					resumenMunicipalPais = reforzamientoService.getIdPlantillaProgramasPais(idProceso, programaEvaluacion.getId(), this.ano, TipoDocumentosProcesos.PLANTILLAMUNICIPALAPSRESUMENSERVICIO);
				}
			}
			//Mixto PxQ
			if(programaEvaluacion.getDependenciaMunicipal() && programaEvaluacion.getDependenciaServicio()){
				resumenMunicipalPais = reforzamientoService.getLastDocumentRecursosFinancierosByType(idProceso, programaEvaluacion.getIdProgramaAno(), TipoDocumentosProcesos.PLANTILLAMUNICIPALAPSRESUMENSERVICIO);
				if(resumenMunicipalPais == null){
					resumenMunicipalPais = reforzamientoService.getIdPlantillaProgramasPais(idProceso, programaEvaluacion.getId(), this.ano, TipoDocumentosProcesos.PLANTILLAMUNICIPALAPSRESUMENSERVICIO);
				}
				resumenServicioPais = reforzamientoService.getLastDocumentRecursosFinancierosByType(idProceso, programaEvaluacion.getIdProgramaAno(), TipoDocumentosProcesos.PLANTILLASERVICIOAPSRESUMENSERVICIO);
				if(resumenServicioPais == null){
					resumenServicioPais = reforzamientoService.getIdPlantillaProgramasPais(idProceso, programaEvaluacion.getId(), this.ano, TipoDocumentosProcesos.PLANTILLASERVICIOAPSRESUMENSERVICIO);
				}
				mixto=true;
			}
		}else{
			//Servicio H
			if(programaEvaluacion.getDependenciaServicio() && !programaEvaluacion.getDependenciaMunicipal()){
				resumenServicioPais = reforzamientoService.getLastDocumentRecursosFinancierosByType(idProceso, programaEvaluacion.getIdProgramaAno(), TipoDocumentosProcesos.PLANTILLASERVICIOAPSRESUMENHISTORICO);
				if(resumenServicioPais == null){
					resumenServicioPais = reforzamientoService.getIdPlantillaProgramasPais(idProceso, programaEvaluacion.getId(), this.ano, TipoDocumentosProcesos.PLANTILLASERVICIOAPSRESUMENHISTORICO);
				}
			}
			//Municipal H
			if(programaEvaluacion.getDependenciaMunicipal() && !programaEvaluacion.getDependenciaServicio()){
				resumenMunicipalPais = reforzamientoService.getLastDocumentRecursosFinancierosByType(idProceso, programaEvaluacion.getIdProgramaAno(), TipoDocumentosProcesos.PLANTILLAMUNICIPALAPSRESUMENHISTORICO);
				if(resumenMunicipalPais == null){
					resumenMunicipalPais = reforzamientoService.getIdPlantillaProgramasPais(idProceso, programaEvaluacion.getId(), this.ano, TipoDocumentosProcesos.PLANTILLAMUNICIPALAPSRESUMENHISTORICO);
				}
			}
			//Mixto H
			if(programaEvaluacion.getDependenciaMunicipal() && programaEvaluacion.getDependenciaServicio()){
				resumenMunicipalPais = reforzamientoService.getLastDocumentRecursosFinancierosByType(idProceso, programaEvaluacion.getIdProgramaAno(), TipoDocumentosProcesos.PLANTILLAMUNICIPALAPSRESUMENHISTORICO);
				if(resumenMunicipalPais == null){
					resumenMunicipalPais = reforzamientoService.getIdPlantillaProgramasPais(idProceso, programaEvaluacion.getId(), this.ano, TipoDocumentosProcesos.PLANTILLAMUNICIPALAPSRESUMENHISTORICO);
				}
				resumenServicioPais = reforzamientoService.getLastDocumentRecursosFinancierosByType(idProceso, programaEvaluacion.getIdProgramaAno(), TipoDocumentosProcesos.PLANTILLASERVICIOAPSRESUMENHISTORICO);
				if(resumenServicioPais == null){
					resumenServicioPais = reforzamientoService.getIdPlantillaProgramasPais(idProceso, programaEvaluacion.getId(), this.ano, TipoDocumentosProcesos.PLANTILLASERVICIOAPSRESUMENHISTORICO);
				}
				mixto=true;
			}
		}
		bitacoraSeguimiento = reforzamientoService.getBitacora(idProceso, programaEvaluacion.getIdProgramaAno(), TareasSeguimiento.HACERSEGUIMIENTOPROGRAMASREFORZAMIENTORESOLUCION);
	}
	
	public void handleFile(FileUploadEvent event) {
		FacesMessage msg = new FacesMessage("Succesful", event.getFile()
				.getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		System.out.println("handleAttachedFile");
	}
	
	public String downloadResolucion() {
		System.out.println(getDocIdDownloadResolucion());
		Integer docDownload = Integer.valueOf(Integer.parseInt(getDocIdDownloadResolucion()));
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
		return null;
	}
	
	
	public String downloadExcelResolucion() {
		Integer docDownload = Integer.valueOf(Integer.parseInt(getDocIdDownloadExcelResolucion()));
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
		return null;
	}
	
	public void uploadVersion() {
		if (file != null){
			System.out.println("uploadVersion file is not null");
			String filename = file.getFileName();
			byte[] contentAttachedFile = file.getContents();
			Integer docNewVersion = persistFile(filename, contentAttachedFile);
			reforzamientoService.moveToAlfresco(idProceso, programaEvaluacion.getIdProgramaAno(), docNewVersion, TipoDocumentosProcesos.RESOLUCIONPROGRAMASAPS, this.ano, versionFinal);
			this.resolucionPrograma = docNewVersion;
		}else{
			System.out.println("uploadVersion file is null");
			FacesMessage message = new FacesMessage("Debe seleccionar resolución de recursos a subir");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	public void uploadVersion2() {
		if (file != null){
			System.out.println("uploadVersion file is not null");
			String filename = file.getFileName();
			byte[] contentAttachedFile = file.getContents();
			Integer docNewVersion = persistFile(filename,	contentAttachedFile);
			reforzamientoService.moveToAlfresco(idProceso, programaEvaluacion.getIdProgramaAno(), docNewVersion, TipoDocumentosProcesos.ORDINARIOPROGRAMASAPS, this.ano, versionFinal);
			this.ordinarioPrograma = docNewVersion;
		}else{
			System.out.println("uploadVersion file is null");
			FacesMessage message = new FacesMessage("Debe seleccionar el ordinario a subir");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	public void uploadVersionResolucionCorreo() {
		if (file != null){
			try {
				System.out.println("uploadVersion file is not null");
				String filename = file.getFileName();
				filename = filename.replaceAll(" ", "");
				byte[] contentPlantillaFile = file.getContents();
				File file = createTemporalFile(filename, contentPlantillaFile);
				plantillaResolucionCorreo = reforzamientoService.cargarPlantilla(TipoDocumentosProcesos.PLANTILLARESOLUCIONCORREO, file);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("uploadVersion file is null");
			FacesMessage message = new FacesMessage("uploadVersion file is null");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	public void uploadVersionOrdinarioCorreo() {
		if (file != null){
			try {
				System.out.println("uploadVersion file is not null");
				String filename = file.getFileName();
				filename = filename.replaceAll(" ", "");
				byte[] contentPlantillaFile = file.getContents();
				File file = createTemporalFile(filename, contentPlantillaFile);
				plantillaResolucionCorreo = reforzamientoService.cargarPlantilla(TipoDocumentosProcesos.PLANTILLARESOLUCIONCORREO, file);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("uploadVersion file is null");
			FacesMessage message = new FacesMessage("uploadVersion file is null");
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
		String target = "divapProcesoDistRecFinProgSeguimientoDocumentacion";
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
			reforzamientoService.createSeguimientoProgramaReforzamiento(idProceso, programaEvaluacion.getIdProgramaAno(), TareasSeguimiento.HACERSEGUIMIENTOPROGRAMASREFORZAMIENTORESOLUCION, subject,
					body, getSessionBean().getUsername(), para, conCopia, conCopiaOculta, documentos, this.ano);
			System.out.println("ProcesoDistRecFinSeguimientoController Fin -->sendMail");
		}catch(Exception e){
			e.printStackTrace();
			target = null;
		}
		return target;
	}
	
	@Override
	public String enviar() {
		int numDocFinales = 0;
		if(programaEvaluacion.getDependenciaMunicipal()){
			numDocFinales = reforzamientoService.countVersionFinalRecursosFinancierosByType(idProceso, programaEvaluacion.getIdProgramaAno(), TipoDocumentosProcesos.RESOLUCIONPROGRAMASAPS);
			System.out.println("numDocFinales="+numDocFinales);
			if(numDocFinales == 0){
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No existe versión final para la resolución de distribución de recursos", null);
				FacesContext.getCurrentInstance().addMessage(null, msg);
				return null;
			}
		}
		if(programaEvaluacion.getDependenciaServicio() && !programaEvaluacion.getDependenciaMunicipal()){
			numDocFinales = reforzamientoService.countVersionFinalRecursosFinancierosByType(idProceso, programaEvaluacion.getIdProgramaAno(), TipoDocumentosProcesos.ORDINARIOPROGRAMASAPS);
			System.out.println("numDocFinales="+numDocFinales);
			if(numDocFinales == 0){
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No existe versión final para el ordinario de distribución de recursos", null);
				FacesContext.getCurrentInstance().addMessage(null, msg);
				return null;
			}
		}
		return super.enviar();
	}
	
	public List<montosDistribucionPojo> getPlanillaMontosDistribucion() {
		return planillaMontosDistribucion;
	}

	public void setPlanillaMontosDistribucion(
			List<montosDistribucionPojo> planillaMontosDistribucion) {
		this.planillaMontosDistribucion = planillaMontosDistribucion;
	}

	public List<String> getComunasSeleccionadas() {
		return comunasSeleccionadas;
	}

	public void setComunasSeleccionadas(List<String> comunasSeleccionadas) {
		this.comunasSeleccionadas = comunasSeleccionadas;
	}
	
	public List<String> getEstablecimientosSeleccionadas() {
		return establecimientosSeleccionadas;
	}

	public void setEstablecimientosSeleccionadas(
			List<String> establecimientosSeleccionadas) {
		this.establecimientosSeleccionadas = establecimientosSeleccionadas;
	}

	public List<String> getComunas() {
		return comunas;
	}

	public void setComunas(List<String> comunas) {
		this.comunas = comunas;
	}

	public String getActividadSeguimientoTitle() {
		return actividadSeguimientoTitle;
	}

	public void setActividadSeguimientoTitle(String actividadSeguimientoTitle) {
		this.actividadSeguimientoTitle = actividadSeguimientoTitle;
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

	public String getDocIdDownloadResolucion() {
		return docIdDownloadResolucion;
	}

	public void setDocIdDownloadResolucion(String docIdDownloadResolucion) {
		this.docIdDownloadResolucion = docIdDownloadResolucion;
	}

	

	public Integer getResolucionPrograma() {
		return resolucionPrograma;
	}

	public void setResolucionPrograma(Integer resolucionPrograma) {
		this.resolucionPrograma = resolucionPrograma;
	}

	public Integer getProgramaSeleccionado() {
		return programaSeleccionado;
	}

	public void setProgramaSeleccionado(Integer programaSeleccionado) {
		this.programaSeleccionado = programaSeleccionado;
	}

	public UploadedFile getAdjunto() {
		return adjunto;
	}

	public void setAdjunto(UploadedFile adjunto) {
		this.adjunto = adjunto;
	}

	public Integer getExcelResolucion() {
		return excelResolucion;
	}

	public void setExcelResolucion(Integer excelResolucion) {
		this.excelResolucion = excelResolucion;
	}

	public String getDocIdDownloadExcelResolucion() {
		return docIdDownloadExcelResolucion;
	}

	public void setDocIdDownloadExcelResolucion(String docIdDownloadExcelResolucion) {
		this.docIdDownloadExcelResolucion = docIdDownloadExcelResolucion;
	}

	public UploadedFile getResolucion() {
		return resolucion;
	}

	public void setResolucion(UploadedFile resolucion) {
		this.resolucion = resolucion;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public boolean isVersionFinal() {
		return versionFinal;
	}

	public void setVersionFinal(boolean versionFinal) {
		this.versionFinal = versionFinal;
	}

	public List<SeguimientoVO> getBitacoraSeguimiento() {
		return bitacoraSeguimiento;
	}

	public void setBitacoraSeguimiento(List<SeguimientoVO> bitacoraSeguimiento) {
		this.bitacoraSeguimiento = bitacoraSeguimiento;
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

	public TareasSeguimiento getTareaSeguimiento() {
		return tareaSeguimiento;
	}

	public void setTareaSeguimiento(TareasSeguimiento tareaSeguimiento) {
		this.tareaSeguimiento = tareaSeguimiento;
	}

	public Integer getExcelOrdinario() {
		return excelOrdinario;
	}

	public void setExcelOrdinario(Integer excelOrdinario) {
		this.excelOrdinario = excelOrdinario;
	}

	public Integer getOrdinarioPrograma() {
		return ordinarioPrograma;
	}

	public void setOrdinarioPrograma(Integer ordinarioPrograma) {
		this.ordinarioPrograma = ordinarioPrograma;
	}

	public boolean isServicio() {
		return servicio;
	}

	public void setServicio(boolean servicio) {
		this.servicio = servicio;
	}

	public boolean isMunicipal() {
		return municipal;
	}

	public void setMunicipal(boolean municipal) {
		this.municipal = municipal;
	}

	public Integer getResumenMunicipalPais() {
		return resumenMunicipalPais;
	}

	public void setResumenMunicipalPais(Integer resumenMunicipalPais) {
		this.resumenMunicipalPais = resumenMunicipalPais;
	}

	public Integer getResumenServicioPais() {
		return resumenServicioPais;
	}

	public void setResumenServicioPais(Integer resumenServicioPais) {
		this.resumenServicioPais = resumenServicioPais;
	}

	public boolean isMixto() {
		return mixto;
	}

	public void setMixto(boolean mixto) {
		this.mixto = mixto;
	}

	public Integer getPlantillaResolucionCorreo() {
		return plantillaResolucionCorreo;
	}

	public void setPlantillaResolucionCorreo(Integer plantillaResolucionCorreo) {
		this.plantillaResolucionCorreo = plantillaResolucionCorreo;
	}

	public Integer getPlantillaOrdinarioCorreo() {
		return plantillaOrdinarioCorreo;
	}

	public void setPlantillaOrdinarioCorreo(Integer plantillaOrdinarioCorreo) {
		this.plantillaOrdinarioCorreo = plantillaOrdinarioCorreo;
	}

	public ProgramaVO getProgramaEvaluacion() {
		return programaEvaluacion;
	}

	public void setProgramaEvaluacion(ProgramaVO programaEvaluacion) {
		this.programaEvaluacion = programaEvaluacion;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}
	
	public void resetLastVersion(){
		System.out.println("resetLastVersion lastVersion = false");
		versionFinal = false;
	}
	
}
