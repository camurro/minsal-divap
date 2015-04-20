package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import minsal.divap.enums.Subtitulo;
import minsal.divap.service.ComponenteService;
import minsal.divap.service.ProgramasService;
import minsal.divap.service.RecursosFinancierosProgramasReforzamientoService;
import minsal.divap.service.UtilitariosService;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ProgramaMunicipalVO;
import minsal.divap.vo.ProgramaServicioVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ResumenProgramaVO;
import minsal.divap.vo.ServiciosVO;

import org.apache.log4j.Logger;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named ( "procesoDistRecFinController" ) 
@ViewScoped 
public class ProcesoDistRecFinController extends AbstractTaskMBean implements Serializable  {
	private static final long serialVersionUID = 8979055329731411696L;
	@Inject private transient Logger log;
	@Inject FacesContext facesContext;
	
	@EJB
	private UtilitariosService utilitariosService;
	@EJB
	private ComponenteService componenteService;
	@EJB
	private ProgramasService programasService;
	@EJB
	private RecursosFinancierosProgramasReforzamientoService recursosFinancierosProgramasReforzamientoService;
	
	private String servicioSeleccionado;
	private List<ServiciosVO> listaServicios;
	
	private String componenteSeleccionado;
	private List<ComponentesVO> listaComponentes;
	
	private List<ProgramaMunicipalVO> detalleComunas;
	
	private String posicionElemento;
	private String precioCantidad;
	private Long totalPxQ;
	
	private List<ResumenProgramaVO> resumenPrograma;
	
	private Integer programaSeleccionado;
	private Long totalResumen24;
	private ProgramaVO programa;
	private ProgramaVO programaProxAno;
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
			programaSeleccionado = (Integer) getTaskDataVO().getData().get("_programaSeleccionado");
			this.ano = (Integer) getTaskDataVO().getData().get("_ano");
			System.out.println("this.ano --->" + this.ano);
		}
		programa = programasService.getProgramaByIdProgramaAndAno(programaSeleccionado, (ano - 1));
		programaProxAno = programasService.getProgramaByIdProgramaAndAno(programaSeleccionado, ano);
		listaServicios = utilitariosService.getAllServicios();
		listaComponentes = componenteService.getComponenteByPrograma(programa.getId());
		armarResumenPrograma();
	}
	
	private void armarResumenPrograma() {
		resumenPrograma = programasService.getResumenMunicipal(programaProxAno.getIdProgramaAno(), Subtitulo.SUBTITULO24.getId());
		totalResumen24 = 0L;
		for (ResumenProgramaVO resumen : resumenPrograma) {
			totalResumen24 = totalResumen24+resumen.getTotalS24();
		}
	}

	public String recalcularTotales(){
		detalleComunas.get(Integer.valueOf(getPosicionElemento())).setTotal( detalleComunas.get(Integer.valueOf(getPosicionElemento())).getCantidad() * detalleComunas.get(Integer.valueOf(getPosicionElemento())).getPrecio() );
		getTotalesPxQ(detalleComunas);
		return null;
	}
	public void seleccionComponente(){
		if(componenteSeleccionado!=null){
			setComponenteSeleccionado(componenteSeleccionado);
		}
	}
	public void cargaComunas(){
		if(componenteSeleccionado != null && !componenteSeleccionado.trim().isEmpty()){
			if(servicioSeleccionado != null && !servicioSeleccionado.trim().isEmpty()){
				detalleComunas = programasService.findByServicioComponente(Integer.valueOf(componenteSeleccionado), Integer.valueOf(servicioSeleccionado), programaProxAno.getIdProgramaAno());
				getTotalesPxQ(detalleComunas);
			}else{
				detalleComunas = programasService.findByServicioComponente(Integer.valueOf(componenteSeleccionado), null, programaProxAno.getIdProgramaAno());
				getTotalesPxQ(detalleComunas);
			}
		}else{
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe seleccionar el componente antes de realizar la búsqueda", null);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			detalleComunas = new ArrayList<ProgramaMunicipalVO>();
		}	
	}

	private Long getTotalesPxQ(List<ProgramaMunicipalVO> detalleComunas){
		totalPxQ=0l;
		for (int i=0;i<detalleComunas.size();i++) {
			totalPxQ=totalPxQ+detalleComunas.get(i).getTotal();	
		}
		return totalPxQ;
	}
	
	public void guardar(){
		System.out.println("Guardar los cambios:"+ componenteSeleccionado+" servicio: "+servicioSeleccionado);
		System.out.println(detalleComunas.get(0).getNombreComuna());
		programasService.guardarProgramaReforzamiento(detalleComunas);
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

	public List<ProgramaMunicipalVO> getDetalleComunas() {
		return detalleComunas;
	}

	public void setDetalleComunas(List<ProgramaMunicipalVO> detalleComunas) {
		this.detalleComunas = detalleComunas;
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

	public Long getTotalPxQ() {
		return totalPxQ;
	}

	public void setTotalPxQ(Long totalPxQ) {
		this.totalPxQ = totalPxQ;
	}

	public Long getTotalResumen24() {
		return totalResumen24;
	}

	public void setTotalResumen24(Long totalResumen24) {
		this.totalResumen24 = totalResumen24;
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
	
}
