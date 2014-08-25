package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import minsal.divap.vo.ProgramaVO;

import org.apache.log4j.Logger;
import org.primefaces.model.UploadedFile;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;
import cl.redhat.bandejaTareas.util.JSONHelper;

@Named ("procesoReliquidacionPlanillasController") 
@ViewScoped
public class ProcesoReliquidacionPlanillasController extends AbstractTaskMBean implements
				Serializable {
	

	private static final long serialVersionUID = -4494951061320207248L;
	@Inject
	private transient Logger log;
	@Inject 
	FacesContext facesContext;
	
	private UploadedFile planillaMunicipal;
	private UploadedFile planillaServicio;
	private ProgramaVO programaSeleccionado;
	private boolean errorArchivo;
	private boolean archivosCargados = true;
	
	
	
	@PostConstruct
	public void init() {
		log.info("ProcesoReliquidacionPlanillasController tocado.");
		if (!getSessionBean().isLogged()) {
			log.warn("No hay usuario almacenado en sesion, se redirecciona a pantalla de login");
			try {
				facesContext.getExternalContext().redirect("login.jsf");
			} catch (IOException e) {
				log.error("Error tratando de redireccionar a login por falta de usuario en sesion.", e);
			}
		}
	
	}

	public String cargarArchivos(){
		//TODO VALIDAR ARCHIVO
		if(isErrorArchivo()){
			setArchivosCargados(true);			
		}else{
			setArchivosCargados(false);
		}
		return null;
	}
	
		
	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("usuario", getSessionBean().getUsername());
		parameters.put("sufijoTarea_", getProgramaSeleccionado().getTipoPrograma().getNombre());
		return parameters;
	}


	@Override
	public String iniciarProceso() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public UploadedFile getPlanillaMunicipal() {
		return planillaMunicipal;
	}



	public void setPlanillaMunicipal(UploadedFile planillaMunicipal) {
		this.planillaMunicipal = planillaMunicipal;
	}



	public UploadedFile getPlanillaServicio() {
		return planillaServicio;
	}



	public void setPlanillaServicio(UploadedFile planillaServicio) {
		this.planillaServicio = planillaServicio;
	}



	public ProgramaVO getProgramaSeleccionado() {
		programaSeleccionado = getFromSession("programaSeleccionado", ProgramaVO.class);
		if(programaSeleccionado==null){
			String variablePrograma = (String) getTaskDataVO().getData().get("_programa");				
			programaSeleccionado = (ProgramaVO) JSONHelper.fromJSON(variablePrograma,ProgramaVO.class);
			setOnSession("programaSeleccionado", programaSeleccionado);
		}
		return programaSeleccionado;
	}

	public void setProgramaSeleccionado(ProgramaVO programaSeleccionado) {
		this.programaSeleccionado = programaSeleccionado;
	}

	public boolean isErrorArchivo() {
		return errorArchivo;
	}

	public void setErrorArchivo(boolean errorArchivo) {
		this.errorArchivo = errorArchivo;
	}

	public boolean isArchivosCargados() {
		return archivosCargados;
	}

	public void setArchivosCargados(boolean archivosCargados) {
		this.archivosCargados = archivosCargados;
	}
	
	
		
}
