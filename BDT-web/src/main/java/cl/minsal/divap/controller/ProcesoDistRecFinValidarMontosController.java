package cl.minsal.divap.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named ( "procesoDistRecFinValidarMontosController" ) 
@ViewScoped 
public class ProcesoDistRecFinValidarMontosController extends AbstractTaskMBean implements Serializable {
	private static final long serialVersionUID = 8979055329731411696L;

	@PostConstruct public void init() {
		if(sessionExpired()){
			return;
		}
	}

	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("recursosAPSMunicipal_", new Boolean(true));
		System.out.println("recursosAPSMunicipal_="+parameters.get("recursosAPSMunicipal_"));
		return parameters;
	}

	@Override
	public String iniciarProceso() {
		return null;
	}
}
