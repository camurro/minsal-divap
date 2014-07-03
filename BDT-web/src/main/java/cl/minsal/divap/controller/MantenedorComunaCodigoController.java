package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import cl.minsal.divap.pojo.ComunaPojo;
import cl.redhat.bandejaTareas.controller.BaseOirsController;
import cl.redhat.bandejaTareas.util.BandejaProperties;

@Named ( "mantenedorComunaCodigoController" ) @ViewScoped public class MantenedorComunaCodigoController extends BaseOirsController
				implements Serializable {
	private static final long serialVersionUID = 8979055329731411696L;
	@Inject private transient Logger log;
	@Inject private BandejaProperties bandejaProperties;
	@Inject FacesContext facesContext;
	
	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private List<ComunaPojo> listadoComunas;
	
	public List<ComunaPojo> getListadoComunas() {
		return listadoComunas;
	}
	
	public void setListadoComunas( List<ComunaPojo> listadoComunas ) {
		this.listadoComunas = listadoComunas;
	}
	
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
		
		listadoComunas = new ArrayList();
		ComunaPojo comuna = new ComunaPojo();
		
		comuna = new ComunaPojo();
		comuna.setId(1);
		comuna.setCodigoDivap("100");
		comuna.setCodigoFonasa("1");
		comuna.setCodigoOtro("13");
		comuna.setNombreComuna("La Reina");
		comuna.setRegion("Metropolitana");
		
		listadoComunas.add(comuna);
		
		comuna = new ComunaPojo();
		comuna.setId(2);
		comuna.setCodigoDivap("102");
		comuna.setCodigoFonasa("12");
		comuna.setCodigoOtro("11");
		comuna.setNombreComuna("Macul");
		comuna.setRegion("Metropolitana");
		
		listadoComunas.add(comuna);
		
		comuna = new ComunaPojo();
		comuna.setId(1);
		comuna.setCodigoDivap("103");
		comuna.setCodigoFonasa("3");
		comuna.setCodigoOtro("15");
		comuna.setNombreComuna("Santiago");
		comuna.setRegion("Metropolitana");
		
		listadoComunas.add(comuna);
		
		comuna = new ComunaPojo();
		comuna.setId(1);
		comuna.setCodigoDivap("105");
		comuna.setCodigoFonasa("4");
		comuna.setCodigoOtro("16");
		comuna.setNombreComuna("Lampa");
		comuna.setRegion("Metropolitana");
		
		listadoComunas.add(comuna);
		
	}
}
