package cl.minsal.divap.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

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

	@PostConstruct public void init() {
		if(sessionExpired()){
			return;
		}
	}
	
	public UploadedFile getAdjunto() {
		return adjunto;
	}

	public void setAdjunto(UploadedFile adjunto) {
		this.adjunto = adjunto;
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
}
