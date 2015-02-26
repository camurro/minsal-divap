package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import minsal.divap.vo.ProgramaMunicipalHistoricoVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ResumenProgramaVO;
import minsal.divap.vo.ServiciosVO;

import org.apache.log4j.Logger;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named ( "procesoDistRecFinHistoricoMunicipalController" ) 
@ViewScoped 
public class ProcesoDistRecFinHistoricoMunicipalController extends AbstractTaskMBean implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8097601336202053671L;
	@Inject private transient Logger log;
	@Inject FacesContext facesContext;
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
	private ProgramaVO programa;
	private ProgramaVO programaProxAno;
	
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
	private Long totalResumen24;
	
	private Double inflactorS24;
	private Long totalS24Pasado;
	private Long totalS24Futuro;
	private Integer ano;
	
	
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
			this.ano = (Integer) getTaskDataVO().getData().get("_ano");
			System.out.println("this.ano --->" + this.ano);
		}
		programa = programasService.getProgramaByIdProgramaAndAno(programaSeleccionado, (ano - 1));
		programaProxAno = programasService.getProgramaByIdProgramaAndAno(programaSeleccionado, ano);
		listaServicios = utilitariosService.getAllServicios();
		listaComponentes= componenteService.getComponenteByPrograma(programa.getId());
		inflactorS24 = subtituloService.getInflactor(3);
	}
	
	private void armarResumenPrograma() {
		resumenPrograma = programasService.getResumenMunicipal(programaSeleccionado, 3);
		totalResumen24 =0l;
		for (ResumenProgramaVO resumen : resumenPrograma) {
			totalResumen24 = totalResumen24+resumen.getTotalS24();
		}
	}

	public void refrescar(){}
	
	public String recalcularTotales(){
		totalS24Pasado=0l;
		totalS24Futuro=0l;
		
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

	private Integer getTotales(List<ProgramaMunicipalHistoricoVO> listadoHistoricoMunicipalActual){
		totalS24Pasado=0l;
		totalS24Futuro=0l;
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

	public Long getTotalResumen24() {
		return totalResumen24;
	}

	public void setTotalResumen24(Long totalResumen24) {
		this.totalResumen24 = totalResumen24;
	}

	public Long getTotalS24Pasado() {
		return totalS24Pasado;
	}

	public void setTotalS24Pasado(Long totalS24Pasado) {
		this.totalS24Pasado = totalS24Pasado;
	}

	public Long getTotalS24Futuro() {
		return totalS24Futuro;
	}

	public void setTotalS24Futuro(Long totalS24Futuro) {
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

	public ProgramaVO getPrograma() {
		return programa;
	}

	public void setPrograma(ProgramaVO programa) {
		this.programa = programa;
	}

	public ProgramaVO getProgramaProxAno() {
		return programaProxAno;
	}

	public void setProgramaProxAno(ProgramaVO programaProxAno) {
		this.programaProxAno = programaProxAno;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}
	
}
