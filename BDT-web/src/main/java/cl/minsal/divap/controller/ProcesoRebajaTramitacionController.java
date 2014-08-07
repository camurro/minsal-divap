package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import minsal.divap.service.RebajaService;
import minsal.divap.service.UtilitariosService;
import minsal.divap.vo.ComunaVO;
import minsal.divap.vo.RegionVO;
import minsal.divap.vo.ServiciosVO;

import org.apache.log4j.Logger;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named("procesoRebajaTramitacionController")
@ViewScoped
public class ProcesoRebajaTramitacionController extends AbstractTaskMBean
	implements Serializable {
	
	private static final long serialVersionUID = -9223198612121852459L;
	@Inject
	private transient Logger log;
	@Inject
	FacesContext facesContext;
	@EJB
	UtilitariosService utilitariosService;
	@EJB
	RebajaService rebajaService;
	
	//Variables página
	private List<RegionVO> listaRegiones;
	private String regionSeleccionada;	
	private List<ServiciosVO> listaServicios;
	private String servicioSeleccionado;
	private List<ComunaVO> listaComunas;
	private List<String> comunasSeleccionadas;
	private List<ComunaVO> rebajaComunas;
	
	private String mesActual;
	private String target;
	
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
		cargarListaRegiones();
		String formato="MMMM";
		SimpleDateFormat dateFormat = new SimpleDateFormat(formato);
		setMesActual(dateFormat.format(new Date()));
	}
	
	public void cargarListaRegiones(){
		listaRegiones = utilitariosService.getAllRegion();
	}
	public void cargaServicios(){
		if(regionSeleccionada!=null && !regionSeleccionada.equals("")){
			listaServicios=utilitariosService.getServiciosByRegion(Integer.parseInt(regionSeleccionada));
		}else{
			listaServicios = new ArrayList<ServiciosVO>();
		}
	}
	public void cargaComunas(){
		if(servicioSeleccionado!=null && !servicioSeleccionado.equals("")){
			listaComunas = utilitariosService.getComunasByServicio(Integer.parseInt(servicioSeleccionado));
		}else{
			listaComunas = new ArrayList<ComunaVO>();
		}
	}
	
	public void buscarRebaja(){
		List<Integer> comunasId = new ArrayList<Integer>();
		for(String comunas : comunasSeleccionadas){
			Integer comunaId = Integer.parseInt(comunas);
			comunasId.add(comunaId);
		}
		String formato="MM";
		SimpleDateFormat dateFormat = new SimpleDateFormat(formato);
		rebajaComunas = rebajaService.getRebajasByComuna(comunasId,Integer.parseInt(dateFormat.format(new Date())));
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

	public List<RegionVO> getListaRegiones() {
		return listaRegiones;
	}

	public void setListaRegiones(List<RegionVO> listaRegiones) {
		this.listaRegiones = listaRegiones;
	}

	public String getRegionSeleccionada() {
		return regionSeleccionada;
	}

	public void setRegionSeleccionada(String regionSeleccionada) {
		this.regionSeleccionada = regionSeleccionada;
	}

	public List<ServiciosVO> getListaServicios() {
		return listaServicios;
	}

	public void setListaServicios(List<ServiciosVO> listaServicios) {
		this.listaServicios = listaServicios;
	}

	public String getServicioSeleccionado() {
		return servicioSeleccionado;
	}

	public void setServicioSeleccionado(String servicioSeleccionado) {
		this.servicioSeleccionado = servicioSeleccionado;
	}

	public List<ComunaVO> getListaComunas() {
		return listaComunas;
	}

	public void setListaComunas(List<ComunaVO> listaComunas) {
		this.listaComunas = listaComunas;
	}

	public List<String> getComunasSeleccionadas() {
		return comunasSeleccionadas;
	}

	public void setComunasSeleccionadas(List<String> comunasSeleccionadas) {
		this.comunasSeleccionadas = comunasSeleccionadas;
	}

	public List<ComunaVO> getRebajaComunas() {
		return rebajaComunas;
	}

	public void setRebajaComunas(List<ComunaVO> rebajaComunas) {
		this.rebajaComunas = rebajaComunas;
	}

	public String getMesActual() {
		return mesActual;
	}

	public void setMesActual(String mesActual) {
		this.mesActual = mesActual;
	}

}
