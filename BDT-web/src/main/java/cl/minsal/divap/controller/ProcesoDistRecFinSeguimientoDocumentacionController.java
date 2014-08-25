package cl.minsal.divap.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import cl.minsal.divap.pojo.EnvioServiciosPojo;
import cl.minsal.divap.pojo.EstablecimientoPojo;
import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named ( "procesoDistRecFinSeguimientoDocumentacionController" ) 
@ViewScoped 
public class ProcesoDistRecFinSeguimientoDocumentacionController extends AbstractTaskMBean implements Serializable {
	private static final long serialVersionUID = 8979055329731411696L;
	
	private List<EnvioServiciosPojo> listadoEnvioServicios;
	private List<EstablecimientoPojo> listadoEstablecimientos;
	private String actividadSeguimientoTitle;

	@PostConstruct public void init() {
		if(sessionExpired()){
			return;
		}
	}
	
	public String getActividadSeguimientoTitle() {
		return actividadSeguimientoTitle;
	}

	public void setActividadSeguimientoTitle(String actividadSeguimientoTitle) {
		this.actividadSeguimientoTitle = actividadSeguimientoTitle;
	}

	public List<EstablecimientoPojo> getListadoEstablecimientos() {
		return listadoEstablecimientos;
	}

	public void setListadoEstablecimientos(
			List<EstablecimientoPojo> listadoEstablecimientos) {
		this.listadoEstablecimientos = listadoEstablecimientos;
	}

	public List<EnvioServiciosPojo> getListadoEnvioServicios() {
		return listadoEnvioServicios;
	}

	public void setListadoEnvioServicios(
			List<EnvioServiciosPojo> listadoEnvioServicios) {
		this.listadoEnvioServicios = listadoEnvioServicios;
	}
	
	public Long getTotalMunicipal(){
		Long suma = 0L;
		for(EstablecimientoPojo e : listadoEstablecimientos){
			suma+=e.gettS24();
		}
		return suma;
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
