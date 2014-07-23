package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import minsal.divap.enums.BusinessProcess;
import minsal.divap.vo.TaskDataVO;
import minsal.divap.vo.TaskVO;

import org.apache.log4j.Logger;
import org.primefaces.model.UploadedFile;

import cl.minsal.divap.pojo.RebajaPojo;
import cl.redhat.bandejaTareas.controller.BaseController;
import cl.redhat.bandejaTareas.controller.divapProcesoRebajaCargarCumplimientoController;
import cl.redhat.bandejaTareas.task.AbstractTaskMBean;
import cl.redhat.bandejaTareas.util.BandejaProperties;

@Named("procesoRebajaController")
@ViewScoped
public class ProcesoRebajaController extends AbstractTaskMBean
	implements Serializable {
	
	private static final long serialVersionUID = 8979055329731411696L;
	@Inject
	private transient Logger log;
	@Inject
	FacesContext facesContext;
	
	
	private UploadedFile cumplimientoFile;
	private String target;
	
	//Variables de salida proceso
	private boolean error_;
	private String correoUsuario_;
	private String correoSubject_;
	private String correoBody_;
	
	@PostConstruct
	public void init() {
		log.info("ProcesosRebajaController tocado.");
		if (!getSessionBean().isLogged()) {
			log.warn("No hay usuario almacenado en sesion, se redirecciona a pantalla de login");
			try {
				facesContext.getExternalContext().redirect("login.jsf");
			} catch (IOException e) {
				log.error(
						"Error tratando de redireccionar a login por falta de usuario en sesion.",
						e);
			}
		}
	}
	
	public String getTarget() {
		return target;
	}

	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		System.out.println("createResultData usuario-->"+getSessionBean().getUsername());
		parameters.put("usuario", getSessionBean().getUsername());
		parameters.put("error_", this.error_);
		return parameters;
	}

	@Override
	public String iniciarProceso() {
		String success = "divapProcesoRebajaCargarInformacionCumplimiento";
		Long procId = iniciarProceso(BusinessProcess.REBAJA);
		System.out.println("procId-->"+procId);
		if(procId == null){
			 success = null;
		}else{
			TaskVO task = getUserTasksByProcessId(procId, getSessionBean().getUsername());
			if(task != null){
				TaskDataVO taskDataVO = getTaskData(task.getId());
				if(taskDataVO != null){
					System.out.println("taskDataVO recuperada="+taskDataVO);
					setOnSession("taskDataSeleccionada", taskDataVO);
				}
			}
		}
		return success;
	}
	
	@Override
	public String enviar(){
		setError_(false);
		setCorreoUsuario_("robson.watt@gmail.com");
		setCorreoSubject_("[PROCESO]Problemas validación Archivo");
		setCorreoBody_("PRUEBA");
		return super.enviar();
	}
	
	public void uploadArchivoCumplimiento(){
		//TODO VALIDAR ARCHIVO
		if (cumplimientoFile != null) {
			FacesMessage msg = new FacesMessage(
					"El archivo de cumplimiento ha sido cargado correctamente.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		
	}
		
	public UploadedFile getCumplimientoFile() {
		return cumplimientoFile;
	}

	public void setCumplimientoFile(UploadedFile cumplimientoFile) {
		this.cumplimientoFile = cumplimientoFile;
	}

	public boolean isError_() {
		return error_;
	}

	public void setError_(boolean error_) {
		this.error_ = error_;
	}

	public String getCorreoUsuario_() {
		return correoUsuario_;
	}

	public void setCorreoUsuario_(String correoUsuario_) {
		this.correoUsuario_ = correoUsuario_;
	}

	public String getCorreoSubject_() {
		return correoSubject_;
	}

	public void setCorreoSubject_(String correoSubject_) {
		this.correoSubject_ = correoSubject_;
	}

	public String getCorreoBody_() {
		return correoBody_;
	}

	public void setCorreoBody_(String correoBody_) {
		this.correoBody_ = correoBody_;
	}
	

}
