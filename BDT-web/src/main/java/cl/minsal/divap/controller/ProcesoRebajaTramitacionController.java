package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import minsal.divap.enums.BusinessProcess;
import minsal.divap.vo.TaskDataVO;
import minsal.divap.vo.TaskVO;

import org.apache.log4j.Logger;
import org.primefaces.model.UploadedFile;

import cl.minsal.divap.pojo.RebajaPojo;
import cl.redhat.bandejaTareas.controller.BaseController;
import cl.redhat.bandejaTareas.controller.divapProcesoRebajaCargarCumplimientoController;
import cl.redhat.bandejaTareas.task.AbstractTaskMBean;
import cl.redhat.bandejaTareas.util.BandejaProperties;

@Named("procesoRebajaTramitacionController")
@ViewScoped
public class ProcesoRebajaTramitacionController extends AbstractTaskMBean
	implements Serializable {
	
	private static final long serialVersionUID = -9223198612121852459L;
	@Inject
	private transient Logger log;
	@Inject
	FacesContext facesContext;
	
	
	//Variables página
	private String target;
	private Map<String, String> comunas;
	private List<String> comunasSeleccionadas;
	List<RebajaPojo> listRebaja;
	
	//Variables de salida proceso
	private boolean aprobar_;
	private boolean rechazarRevalorizar_;
	private boolean rechazarSubirArchivo_;
	
	@PostConstruct
	public void init() {
		log.info("ProcesosRebajaValidarMontosController tocado.");
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
	public void generaComunas() {
		comunas = new HashMap<String, String>();
		comunas.put("PROVIDENCIA", "PROVIDENCIA");
		comunas.put("MACUL", "MACUL");
		comunas.put("LA FLORIDA", "LA FLORIDA");
		comunas.put("LA REINA", "LA REINA");
		comunas.put("RENCA", "RENCA");
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
		rebaja.setTotalRebaja(rebaja.getRebaja1() + rebaja.getRebaja2() + rebaja.getRebaja3());
		
		rebaja.setRebajaExcepcional1(0.04f);
		rebaja.setRebajaExcepcional2(0);
		rebaja.setRebajaExcepcional3(0.05f);
		rebaja.setTotalRebajaExcepcional(rebaja.getRebajaExcepcional1() + rebaja.getRebajaExcepcional2() + rebaja.getRebajaExcepcional3());
		
		listRebaja.add(rebaja);
		
		rebaja = new RebajaPojo();
		
		rebaja.setComuna("LA REINA");
		rebaja.setItemCumplimiento1(0.03f);
		rebaja.setItemCumplimiento1(0.015f);
		rebaja.setItemCumplimiento1(0.01f);
		
		rebaja.setRebaja1(0.013f);
		rebaja.setRebaja2(0);
		rebaja.setRebaja3(0.015f);
		rebaja.setTotalRebaja(rebaja.getRebaja1() + rebaja.getRebaja2() + rebaja.getRebaja3());
		
		listRebaja.add(rebaja);
	}
	public String getTarget() {
		return target;
	}

	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		System.out.println("createResultData usuario-->"+getSessionBean().getUsername());
		parameters.put("usuario", getSessionBean().getUsername());
		parameters.put("aprobar_", this.aprobar_);
		parameters.put("rechazarRevalorizar_", this.rechazarRevalorizar_);
		parameters.put("rechazarSubirArchivo_", this.rechazarSubirArchivo_);
		return parameters;
	}

	@Override
	public String enviar(){
		setAprobar_(true);
		setRechazarRevalorizar_(false);
		setRechazarSubirArchivo_(false);
		return super.enviar();
	}
	
	public String recalcular(){
		setAprobar_(false);
		setRechazarRevalorizar_(true);
		setRechazarSubirArchivo_(false);
		return super.enviar();
	}

	@Override
	public String iniciarProceso() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Map<String, String> getComunas() {
		return comunas;
	}

	public void setComunas(Map<String, String> comunas) {
		this.comunas = comunas;
	}

	public List<String> getComunasSeleccionadas() {
		return comunasSeleccionadas;
	}

	public void setComunasSeleccionadas(List<String> comunasSeleccionadas) {
		this.comunasSeleccionadas = comunasSeleccionadas;
	}
	public List<RebajaPojo> getListRebaja() {
		return listRebaja;
	}
	public void setListRebaja(List<RebajaPojo> listRebaja) {
		this.listRebaja = listRebaja;
	}
	public boolean isAprobar_() {
		return aprobar_;
	}
	public void setAprobar_(boolean aprobar_) {
		this.aprobar_ = aprobar_;
	}
	public boolean isRechazarRevalorizar_() {
		return rechazarRevalorizar_;
	}
	public void setRechazarRevalorizar_(boolean rechazarRevalorizar_) {
		this.rechazarRevalorizar_ = rechazarRevalorizar_;
	}
	public boolean isRechazarSubirArchivo_() {
		return rechazarSubirArchivo_;
	}
	public void setRechazarSubirArchivo_(boolean rechazarSubirArchivo_) {
		this.rechazarSubirArchivo_ = rechazarSubirArchivo_;
	}

}
