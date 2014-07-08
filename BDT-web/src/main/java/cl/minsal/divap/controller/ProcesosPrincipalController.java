package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import cl.redhat.bandejaTareas.controller.BaseController;
import cl.redhat.bandejaTareas.util.BandejaProperties;

@Named ( "procesosPrincipalController" ) @ViewScoped public class ProcesosPrincipalController extends BaseController implements
				Serializable {
	private static final long serialVersionUID = 8979055329731411696L;
	@Inject private transient Logger log;
	@Inject private BandejaProperties bandejaProperties;
	@Inject FacesContext facesContext;
	
	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	@PostConstruct public void init() {
		log.info("ProcesosPrincipalController Alcanzado.");
		if (!getSessionBean().isLogged()) {
			log.warn("No hay usuario almacenado en sesion, se redirecciona a pantalla de login");
			try {
				facesContext.getExternalContext().redirect("login.jsf");
			} catch (IOException e) {
				log.error("Error tratando de redireccionar a login por falta de usuario en sesion.", e);
			}
		}
	}
}
