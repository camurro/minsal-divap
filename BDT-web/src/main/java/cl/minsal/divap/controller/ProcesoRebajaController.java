package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import cl.minsal.divap.pojo.RebajaPojo;
import cl.redhat.bandejaTareas.controller.BaseController;
import cl.redhat.bandejaTareas.util.BandejaProperties;

@Named("procesoRebajaController")
@ViewScoped
public class ProcesoRebajaController extends BaseController implements
		Serializable {
	private static final long serialVersionUID = 8979055329731411696L;
	@Inject
	private transient Logger log;
	@Inject
	private BandejaProperties bandejaProperties;
	@Inject
	FacesContext facesContext;

	// private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

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

		generaComunas();
		generaRebajas();
	}

	private Map<String, String> comunas;
	private List<String> comunasSeleccionadas;

	public List<String> getComunasSeleccionadas() {
		return comunasSeleccionadas;
	}

	public void setComunasSeleccionadas(List<String> comunasSeleccionadas) {
		this.comunasSeleccionadas = comunasSeleccionadas;
	}

	public Map<String, String> getComunas() {
		return comunas;
	}

	public void setComunas(Map<String, String> comunas) {
		this.comunas = comunas;
	}

	public void generaComunas() {
		comunas = new HashMap<String, String>();
		comunas.put("PROVIDENCIA", "PROVIDENCIA");
		comunas.put("MACUL", "MACUL");
		comunas.put("LA FLORIDA", "LA FLORIDA");
		comunas.put("LA REINA", "LA REINA");
		comunas.put("RENCA", "RENCA");
	}

	List<RebajaPojo> listRebaja;

	public List<RebajaPojo> getListRebaja() {
		return listRebaja;
	}

	public void setListRebaja(List<RebajaPojo> listRebaja) {
		this.listRebaja = listRebaja;
	}

	public void generaRebajas() {
		listRebaja = new ArrayList();
		RebajaPojo rebaja = new RebajaPojo();

		rebaja.setComuna("MACUL");
		rebaja.setItemCumplimiento1(0.05f);
		rebaja.setItemCumplimiento1(0.02f);
		rebaja.setItemCumplimiento1(0.017f);

		rebaja.setRebaja1(0.04f);
		rebaja.setRebaja2(0);
		rebaja.setRebaja3(0.05f);
		rebaja.setTotalRebaja(rebaja.getRebaja1() + rebaja.getRebaja2()
				+ rebaja.getRebaja3());

		rebaja.setRebajaExcepcional1(0.04f);
		rebaja.setRebajaExcepcional2(0);
		rebaja.setRebajaExcepcional3(0.05f);
		rebaja.setTotalRebajaExcepcional(rebaja.getRebajaExcepcional1()
				+ rebaja.getRebajaExcepcional2()
				+ rebaja.getRebajaExcepcional3());

		listRebaja.add(rebaja);

		rebaja = new RebajaPojo();

		rebaja.setComuna("LA REINA");
		rebaja.setItemCumplimiento1(0.03f);
		rebaja.setItemCumplimiento1(0.015f);
		rebaja.setItemCumplimiento1(0.01f);

		rebaja.setRebaja1(0.013f);
		rebaja.setRebaja2(0);
		rebaja.setRebaja3(0.015f);
		rebaja.setTotalRebaja(rebaja.getRebaja1() + rebaja.getRebaja2()
				+ rebaja.getRebaja3());

		listRebaja.add(rebaja);
	}

}
