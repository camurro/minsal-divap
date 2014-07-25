package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import minsal.divap.enums.BusinessProcess;
import minsal.divap.service.ProgramasService;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.TaskDataVO;
import minsal.divap.vo.TaskVO;

import org.apache.log4j.Logger;
import org.primefaces.model.UploadedFile;

import cl.minsal.divap.model.Programa;
import cl.minsal.divap.pojo.ComunaPojo;
import cl.minsal.divap.pojo.EnvioServiciosPojo;
import cl.minsal.divap.pojo.EstablecimientoPojo;
import cl.minsal.divap.pojo.ProcesosProgramasPojo;
import cl.minsal.divap.pojo.ProgramasPojo;
import cl.minsal.divap.pojo.ValorHistoricoPojo;
import cl.redhat.bandejaTareas.controller.BaseController;
import cl.redhat.bandejaTareas.task.AbstractTaskMBean;
import cl.redhat.bandejaTareas.util.BandejaProperties;
import cl.redhat.bandejaTareas.util.JSONHelper;
import cl.redhat.bandejaTareas.util.MatchViewTask;

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
		parameters.put("sufijoTarea_", getProgramaSeleccionado().getTipo_programa());
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
