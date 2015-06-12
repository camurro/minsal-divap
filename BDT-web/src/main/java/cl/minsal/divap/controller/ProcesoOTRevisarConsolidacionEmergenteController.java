package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import minsal.divap.service.OTService;
import minsal.divap.vo.ProgramaVO;

import org.apache.log4j.Logger;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named("procesoOTRevisarConsolidacionEmergenteController")
@ViewScoped
public class ProcesoOTRevisarConsolidacionEmergenteController extends AbstractTaskMBean
implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8731360558390440239L;

	@Inject private transient Logger log;
	
	@EJB
	private OTService otService;
	private List<ProgramaVO> listaProgramas;

	
	
	@PostConstruct
	public void init() throws NumberFormatException, ParseException {
		
		if (!getSessionBean().isLogged()) {
			log.warn("No hay usuario almacenado en sesion, se redirecciona a pantalla de login");
			try {
				facesContext.getExternalContext().redirect("login.jsf");
			} catch (IOException e) {
				log.error("Error tratando de redireccionar a login por falta de usuario en sesion.", e);
			}
		}
		listaProgramas = otService.getProgramas(getLoggedUsername(), otService.getAnoCurso());
	}

	@Override
	protected Map<String, Object> createResultData() {
		return null;
	}

	@Override
	public String iniciarProceso() {
		return null;
	}

	public List<ProgramaVO> getListaProgramas() {
		return listaProgramas;
	}

	public void setListaProgramas(List<ProgramaVO> listaProgramas) {
		this.listaProgramas = listaProgramas;
	}
	
}
