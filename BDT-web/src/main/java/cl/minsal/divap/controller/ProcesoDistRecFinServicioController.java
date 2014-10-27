package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import minsal.divap.service.UtilitariosService;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ComunaVO;
import minsal.divap.vo.ProgramaAPSMunicipalVO;
import minsal.divap.vo.ProgramaMunicipalVO;
import minsal.divap.vo.ProgramaServicioVO;
import minsal.divap.vo.ResumenProgramaServiciosVO;
import minsal.divap.vo.ResumenProgramaVO;
import minsal.divap.vo.ServiciosVO;
import minsal.divap.vo.SubtituloProgramasVO;
import minsal.divap.vo.SubtituloVO;

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

@Named ( "procesoDistRecFinServicioController" ) 
@ViewScoped 
public class ProcesoDistRecFinServicioController extends AbstractTaskMBean implements Serializable  {
	private static final long serialVersionUID = 8979055329731411696L;
	@Inject private transient Logger log;
	@Inject private BandejaProperties bandejaProperties;
	@Inject FacesContext facesContext;
	
	@EJB
	private UtilitariosService utilitariosService;
	@EJB
	private ComponenteService componenteService;
	@EJB
	private ProgramasService programasService;
	
	private String servicioSeleccionado;
	private List<ServiciosVO> listaServicios;
	
	private String componenteSeleccionado;
	private List<ComponentesVO> listaComponentes;
	
	private List<ProgramaServicioVO> detalleEstablecimientos;
	
	
	private boolean tiene21;
	private boolean tiene22;
	private boolean tiene29;
	
	private Integer total21;
	private Integer total22;
	private Integer total29;
	private Integer totalFinal;
	
	private String posicionElemento;
	private String precioCantidad;
	private String subtitulo;
	private Integer totalPxQ;
	
	private List<ResumenProgramaServiciosVO> resumenPrograma;
	
	private Integer programaSeleccionado;
	private Integer totalResumen21;
	private Integer totalResumen22;
	private Integer totalResumen29;
	private Integer totalResumen;
	
	
	@PostConstruct 
	public void init() {
		// log.info("ProcesosPrincipalController Alcanzado.");
		calculaTotalesTabla();
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
		listaServicios = utilitariosService.getAllServicios();
		listaComponentes= componenteService.getComponenteByPrograma(programaSeleccionado);
		armarResumenPrograma();
	}
	
	private void armarResumenPrograma() {
		if(programaSeleccionado!=null){
			resumenPrograma = programasService.getResumenServicio(programaSeleccionado);
			totalResumen21=0;
			totalResumen22=0;
			totalResumen29=0;
			totalResumen=0;
			for(ResumenProgramaServiciosVO resumen : resumenPrograma){
				if(resumen.getTotalS21()!=null){
					tiene21=true;
					totalResumen21+=resumen.getTotalS21();
				}
				if(resumen.getTotalS22()!=null){
							tiene22=true;	
							totalResumen22+=resumen.getTotalS22();
							}
				if(resumen.getTotalS29()!=null){
					tiene29=true;
					totalResumen29+=resumen.getTotalS29();
				}
				totalResumen+=resumen.getTotalServicio();
			}
		}
		
	}

	public String recalcularTotales(){
		
		if(Integer.parseInt(subtitulo)==1){
			detalleEstablecimientos.get(Integer.valueOf(getPosicionElemento())).getSubtitulo21().setTotal(detalleEstablecimientos.get(Integer.valueOf(getPosicionElemento())).getSubtitulo21().getCantidad()*detalleEstablecimientos.get(Integer.valueOf(getPosicionElemento())).getSubtitulo21().getMonto());;
		}
		if(Integer.parseInt(subtitulo)==2){
			detalleEstablecimientos.get(Integer.valueOf(getPosicionElemento())).getSubtitulo22().setTotal(detalleEstablecimientos.get(Integer.valueOf(getPosicionElemento())).getSubtitulo22().getCantidad()*detalleEstablecimientos.get(Integer.valueOf(getPosicionElemento())).getSubtitulo22().getMonto());;
		}
		if(Integer.parseInt(subtitulo)==4){
			detalleEstablecimientos.get(Integer.valueOf(getPosicionElemento())).getSubtitulo29().setTotal(detalleEstablecimientos.get(Integer.valueOf(getPosicionElemento())).getSubtitulo29().getCantidad()*detalleEstablecimientos.get(Integer.valueOf(getPosicionElemento())).getSubtitulo29().getMonto());;
		}
		calculaTotalesTabla();

		return null;
	}
	public void seleccionComponente(){
		if(componenteSeleccionado!=null){
			setComponenteSeleccionado(componenteSeleccionado);
		}
	}
	public String buscarResultados(){
		detalleEstablecimientos = programasService.findByServicioComponenteServicios(Integer.valueOf(componenteSeleccionado), Integer.valueOf(servicioSeleccionado));
		calculaTotalesTabla();
		return null;

	}
	
	private void calculaTotalesTabla(){
		if(detalleEstablecimientos != null && detalleEstablecimientos.size()>0){
			tiene21=false;
			tiene22=false;
			tiene29=false;
			total21=0;
			total22=0;
			total29=0;
			totalFinal=0;
			for (ProgramaServicioVO detalle : detalleEstablecimientos) {
				if(detalle.getSubtitulo21()!=null){
					tiene21 = true;
					total21+=detalle.getSubtitulo21().getTotal();
				}
				if(detalle.getSubtitulo22()!=null){
					tiene22 = true;
					total22+=detalle.getSubtitulo22().getTotal();
				}
				if(detalle.getSubtitulo29()!=null){
					tiene29 = true;
					total29+=detalle.getSubtitulo29().getTotal();
				}
				
			}
			totalFinal += totalFinal + total21 + total22 + total29;
		}
		
	}

	public void guardar(){
		programasService.guardarProgramaReforzamientoServicios(detalleEstablecimientos);
		armarResumenPrograma();
	}
	
	
	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		System.out.println("createResultData usuario-->"
				+ getSessionBean().getUsername());
		parameters.put("recursosAPSMunicipal_", false);
		
		
		return parameters;
	}

	public void refrescar(){}
	
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

	
	public List<ProgramaServicioVO> getDetalleEstablecimientos() {
		return detalleEstablecimientos;
	}

	public void setDetalleEstablecimientos(
			List<ProgramaServicioVO> detalleEstablecimientos) {
		this.detalleEstablecimientos = detalleEstablecimientos;
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



	public List<ResumenProgramaServiciosVO> getResumenPrograma() {
		return resumenPrograma;
	}

	public void setResumenPrograma(List<ResumenProgramaServiciosVO> resumenPrograma) {
		this.resumenPrograma = resumenPrograma;
	}

	public Integer getProgramaSeleccionado() {
		return programaSeleccionado;
	}

	public void setProgramaSeleccionado(Integer programaSeleccionado) {
		this.programaSeleccionado = programaSeleccionado;
	}

	public boolean isTiene21() {
		return tiene21;
	}

	public void setTiene21(boolean tiene21) {
		this.tiene21 = tiene21;
	}

	public boolean isTiene22() {
		return tiene22;
	}

	public void setTiene22(boolean tiene22) {
		this.tiene22 = tiene22;
	}

	public boolean isTiene29() {
		return tiene29;
	}

	public void setTiene29(boolean tiene29) {
		this.tiene29 = tiene29;
	}

	public Integer getTotal21() {
		return total21;
	}

	public void setTotal21(Integer total21) {
		this.total21 = total21;
	}

	public Integer getTotal22() {
		return total22;
	}

	public void setTotal22(Integer total22) {
		this.total22 = total22;
	}

	public Integer getTotal29() {
		return total29;
	}

	public void setTotal29(Integer total29) {
		this.total29 = total29;
	}

	public Integer getTotalFinal() {
		return totalFinal;
	}

	public void setTotalFinal(Integer totalFinal) {
		this.totalFinal = totalFinal;
	}

	public String getSubtitulo() {
		return subtitulo;
	}

	public void setSubtitulo(String subtitulo) {
		this.subtitulo = subtitulo;
	}

	public Integer getTotalResumen21() {
		return totalResumen21;
	}

	public void setTotalResumen21(Integer totalResumen21) {
		this.totalResumen21 = totalResumen21;
	}

	public Integer getTotalResumen22() {
		return totalResumen22;
	}

	public void setTotalResumen22(Integer totalResumen22) {
		this.totalResumen22 = totalResumen22;
	}

	public Integer getTotalResumen29() {
		return totalResumen29;
	}

	public void setTotalResumen29(Integer totalResumen29) {
		this.totalResumen29 = totalResumen29;
	}

	public Integer getTotalResumen() {
		return totalResumen;
	}

	public void setTotalResumen(Integer totalResumen) {
		this.totalResumen = totalResumen;
	}
	
}
