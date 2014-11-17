package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import minsal.divap.service.ComponenteService;
import minsal.divap.service.ProgramasService;
import minsal.divap.service.RecursosFinancierosProgramasReforzamientoService;
import minsal.divap.service.SubtituloService;
import minsal.divap.service.UtilitariosService;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ComunaVO;
import minsal.divap.vo.ProgramaAPSMunicipalVO;
import minsal.divap.vo.ProgramaMunicipalHistoricoVO;
import minsal.divap.vo.ProgramaMunicipalVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ResumenProgramaVO;
import minsal.divap.vo.ServiciosVO;

import org.apache.log4j.Logger;

import cl.minsal.divap.pojo.ComunaPojo;
import cl.minsal.divap.pojo.EnvioServiciosPojo;
import cl.minsal.divap.pojo.EstablecimientoPojo;
import cl.minsal.divap.pojo.ProcesosProgramasPojo;
import cl.minsal.divap.pojo.ProgramasPojo;
import cl.minsal.divap.pojo.ValorHistoricoPojo;
import cl.redhat.bandejaTareas.controller.BaseController;
import cl.redhat.bandejaTareas.task.AbstractTaskMBean;
import cl.redhat.bandejaTareas.util.BandejaProperties;

@Named ( "procesoDistRecFinHistoricoMunicipalController" ) 
@ViewScoped 
public class ProcesoDistRecFinHistoricoMunicipalController extends AbstractTaskMBean implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8097601336202053671L;
	@Inject private transient Logger log;
	@Inject private BandejaProperties bandejaProperties;
	@Inject FacesContext facesContext;
	
	private ProgramaVO programa;
	
	@EJB
	private UtilitariosService utilitariosService;
	@EJB
	private ComponenteService componenteService;
	@EJB
	private ProgramasService programasService;
	@EJB
	private SubtituloService subtituloService;
	@EJB
	private RecursosFinancierosProgramasReforzamientoService reforzamientoService;
	
	private String servicioSeleccionado;
	private List<ServiciosVO> listaServicios;
	
	private String componenteSeleccionado;
	private List<ComponentesVO> listaComponentes;
	
	private List<ProgramaMunicipalHistoricoVO> listadoHistoricoMunicipalActual;
	
	private String posicionElemento;
	private String precioCantidad;
	private Integer totalPxQ;
	
	private List<ResumenProgramaVO> resumenPrograma;
	
	private Integer programaSeleccionado;
	private Integer totalResumen24;
	
	private Double inflactorS24;
	private Integer totalS24Pasado;
	private Integer totalS24Futuro;
	
	private String anoActual;
	private String anoProximo;
	
	
	@PostConstruct 
	public void init() {
		// log.info("ProcesosPrincipalController Alcanzado.");
		if (!getSessionBean().isLogged()) {
			log.warn("No hay usuario almacenado en sesion, se redirecciona a pantalla de login");
			try {
				facesContext.getExternalContext().redirect("login.jsf");
			} catch (IOException e) {
				log.error("Error tratando de redireccionar a login por falta de usuario en sesion.", e);
			}
		}
		if (getTaskDataVO() != null && getTaskDataVO().getData() != null) {
			programaSeleccionado = (Integer) getTaskDataVO()
					.getData().get("_programaSeleccionado");
		}
		programa = reforzamientoService.getProgramaById(programaSeleccionado);
		listaServicios = utilitariosService.getAllServicios();
		listaComponentes= componenteService.getComponenteByPrograma(programaSeleccionado);
		inflactorS24 = subtituloService.getInflactor(3);
		anoActual = reforzamientoService.getAnoCurso()+"";
		anoProximo = (reforzamientoService.getAnoCurso()+1)+"";
		//armarResumenPrograma();
	}
	
	private void armarResumenPrograma() {
		resumenPrograma = programasService.getResumenMunicipal(programaSeleccionado, 3);
		totalResumen24 =0;
		for (ResumenProgramaVO resumen : resumenPrograma) {
			totalResumen24 = totalResumen24+resumen.getTotalS24();
		}
	}

	public void refrescar(){}
	
	public String recalcularTotales(){
		totalS24Pasado=0;
		totalS24Futuro=0;
		
		for(int i=0; i < listadoHistoricoMunicipalActual.size();i++){
			totalS24Pasado += listadoHistoricoMunicipalActual.get(i).getTotalAnoAnterior();
			totalS24Futuro += listadoHistoricoMunicipalActual.get(i).getTotalAnoActual();
		}
		return null;
	}
	public void seleccionComponente(){
		if(componenteSeleccionado!=null){
			setComponenteSeleccionado(componenteSeleccionado);
		}
	}
	public void cargaComunas(){

		listadoHistoricoMunicipalActual = programasService.getHistoricoMunicipal(programaSeleccionado, Integer.valueOf(componenteSeleccionado), Integer.valueOf(servicioSeleccionado));
		getTotales(listadoHistoricoMunicipalActual);
	}

	private void calcularAnoActual(
			List<ProgramaMunicipalHistoricoVO> listadoHistoricoMunicipal) {
		for(ProgramaMunicipalHistoricoVO prog : listadoHistoricoMunicipal){
			ProgramaMunicipalHistoricoVO obj = new ProgramaMunicipalHistoricoVO();
		}
		
	}

	private Integer getTotales(List<ProgramaMunicipalHistoricoVO> listadoHistoricoMunicipalActual){
		/*for (int i=0;i<detalleComunas.size();i++) {
					totalPxQ=totalPxQ+detalleComunas.get(i).getTotal();	
		}
		return totalPxQ;*/
		totalS24Pasado=0;
		totalS24Futuro=0;
		for(ProgramaMunicipalHistoricoVO prog : listadoHistoricoMunicipalActual){
			totalS24Pasado += prog.getTotalAnoAnterior();
			totalS24Futuro += prog.getTotalAnoActual();
		}
		return null;
	}
	
	public void guardar(){
		System.out.println("Guardar los cambios:"+ componenteSeleccionado+" servicio: "+servicioSeleccionado);
		System.out.println(listadoHistoricoMunicipalActual.get(0).getNombreComuna());
		programasService.guardarProgramaHistoricoMunicipal(listadoHistoricoMunicipalActual);
		armarResumenPrograma();
	}

	
	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		System.out.println("createResultData usuario-->"
				+ getSessionBean().getUsername());
		parameters.put("recursosAPSMunicipal_", true);
		
		
		return parameters;
	}

	@Override
	public String iniciarProceso() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public ComponenteService getComponenteService() {
		return componenteService;
	}

	public void setComponenteService(ComponenteService componenteService) {
		this.componenteService = componenteService;
	}

	public String getServicioSeleccionado() {
		return servicioSeleccionado;
	}

	public void setServicioSeleccionado(String servicioSeleccionado) {
		this.servicioSeleccionado = servicioSeleccionado;
	}

	public List<ServiciosVO> getListaServicios() {
		return listaServicios;
	}

	public void setListaServicios(List<ServiciosVO> listaServicios) {
		this.listaServicios = listaServicios;
	}

	public String getComponenteSeleccionado() {
		return componenteSeleccionado;
	}

	public void setComponenteSeleccionado(String componenteSeleccionado) {
		this.componenteSeleccionado = componenteSeleccionado;
	}

	public List<ComponentesVO> getListaComponentes() {
		return listaComponentes;
	}

	public void setListaComponentes(List<ComponentesVO> listaComponentes) {
		this.listaComponentes = listaComponentes;
	}


	public String getPosicionElemento() {
		return posicionElemento;
	}

	public void setPosicionElemento(String posicionElemento) {
		this.posicionElemento = posicionElemento;
	}

	public String getPrecioCantidad() {
		return precioCantidad;
	}

	public void setPrecioCantidad(String precioCantidad) {
		this.precioCantidad = precioCantidad;
	}

	public Integer getTotalPxQ() {
		return totalPxQ;
	}

	public void setTotalPxQ(Integer totalPxQ) {
		this.totalPxQ = totalPxQ;
	}

	public List<ResumenProgramaVO> getResumenPrograma() {
		return resumenPrograma;
	}

	public void setResumenPrograma(List<ResumenProgramaVO> resumenPrograma) {
		this.resumenPrograma = resumenPrograma;
	}

	public Integer getProgramaSeleccionado() {
		return programaSeleccionado;
	}

	public void setProgramaSeleccionado(Integer programaSeleccionado) {
		this.programaSeleccionado = programaSeleccionado;
	}

	public Integer getTotalResumen24() {
		return totalResumen24;
	}

	public void setTotalResumen24(Integer totalResumen24) {
		this.totalResumen24 = totalResumen24;
	}

	public Integer getTotalS24Pasado() {
		return totalS24Pasado;
	}

	public void setTotalS24Pasado(Integer totalS24Pasado) {
		this.totalS24Pasado = totalS24Pasado;
	}

	

	public Integer getTotalS24Futuro() {
		return totalS24Futuro;
	}

	public void setTotalS24Futuro(Integer totalS24Futuro) {
		this.totalS24Futuro = totalS24Futuro;
	}

	public Double getInflactorS24() {
		return inflactorS24;
	}

	public void setInflactorS24(Double inflactorS24) {
		this.inflactorS24 = inflactorS24;
	}

	public List<ProgramaMunicipalHistoricoVO> getListadoHistoricoMunicipalActual() {
		return listadoHistoricoMunicipalActual;
	}

	public void setListadoHistoricoMunicipalActual(
			List<ProgramaMunicipalHistoricoVO> listadoHistoricoMunicipalActual) {
		this.listadoHistoricoMunicipalActual = listadoHistoricoMunicipalActual;
	}

	public String getAnoActual() {
		return anoActual;
	}

	public void setAnoActual(String anoActual) {
		this.anoActual = anoActual;
	}

	public String getAnoProximo() {
		return anoProximo;
	}

	public void setAnoProximo(String anoProximo) {
		this.anoProximo = anoProximo;
	}

	public ProgramaVO getPrograma() {
		return programa;
	}

	public void setPrograma(ProgramaVO programa) {
		this.programa = programa;
	}
	
}