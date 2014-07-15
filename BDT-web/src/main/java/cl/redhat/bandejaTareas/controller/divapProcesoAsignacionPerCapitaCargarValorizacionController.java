package cl.redhat.bandejaTareas.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named("divapProcesoAsignacionPerCapitaCargarValorizacionController")
@ViewScoped
public class divapProcesoAsignacionPerCapitaCargarValorizacionController extends AbstractTaskMBean implements
Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5529806559437739875L;

	private boolean errorCarga = false;

	@PostConstruct
	public void init() {

	}
	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("error_", new Boolean(isErrorCarga()));
		return parameters;
	}

	@Override
	public String iniciarProceso() {
		return null;
	}
	
	public boolean isErrorCarga() {
		return errorCarga;
	}
	
	public void setErrorCarga(boolean errorCarga) {
		this.errorCarga = errorCarga;
	}

}
