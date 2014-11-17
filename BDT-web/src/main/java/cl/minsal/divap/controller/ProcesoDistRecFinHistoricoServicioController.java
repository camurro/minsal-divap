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
import minsal.divap.vo.ProgramaServicioHistoricoVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ResumenProgramaVO;
import minsal.divap.vo.ServiciosVO;

import org.apache.log4j.Logger;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named ( "procesoDistRecFinHistoricoServicioController" ) 
@ViewScoped 
public class ProcesoDistRecFinHistoricoServicioController extends AbstractTaskMBean implements Serializable  {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7352566157530634701L;
	@Inject private transient Logger log;
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
	
	private List<ProgramaServicioHistoricoVO> listadoHistoricoServicioActual;
	
	private String posicionElemento;
	private String precioCantidad;
	private Integer totalPxQ;
	
	private List<ResumenProgramaVO> resumenPrograma;
	
	private Integer programaSeleccionado;
	private Integer totalResumen24;
	
	private Double inflactorS21;
	private Double inflactorS22;
	private Double inflactorS29;
	
	private Integer totalS21Pasado;
	private Integer totalS21Futuro;
	private Integer totalS22Pasado;
	private Integer totalS22Futuro;
	private Integer totalS29Pasado;
	private Integer totalS29Futuro;
	
	private boolean tieneS21;
	private boolean tieneS22;
	private boolean tieneS29;
	
	private Integer totalPasado;
	private Integer totalFuturo;
	private Integer totalPasadoHorizontal;
	private Integer totalFuturoHorizontal;
	
	private String anoActual;
	private String anoProximo;
	
	private String subtitulo;
	
	
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
		inflactorS21 = subtituloService.getInflactor(1);
		inflactorS22 = subtituloService.getInflactor(2);
		inflactorS29 = subtituloService.getInflactor(4);
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
		setTotalS21Pasado(0);
		setTotalS21Futuro(0);
		setTotalS22Pasado(0);
		setTotalS22Futuro(0);
		setTotalS29Pasado(0);
		setTotalS29Futuro(0);
		setTotalPasado(0);
		setTotalFuturo(0);
		setTotalPasadoHorizontal(0);
		setTotalFuturoHorizontal(0);
		
		
		for(int i=0; i < listadoHistoricoServicioActual.size();i++){
			if(subtitulo!=null && subtitulo.equals("1")){
				totalS21Pasado += listadoHistoricoServicioActual.get(i).getSubtitulo21().getTotal();
				totalS21Futuro += listadoHistoricoServicioActual.get(i).getSubtitulo21().getTotalFuturo();
				totalPasado += listadoHistoricoServicioActual.get(i).getSubtitulo21().getTotal();
				totalFuturo += listadoHistoricoServicioActual.get(i).getSubtitulo21().getTotalFuturo();
				listadoHistoricoServicioActual.get(i).setTotalAnoActual(listadoHistoricoServicioActual.get(i).getSubtitulo21().getTotalFuturo());
				tieneS21 = true;
			}
			if(subtitulo!=null && subtitulo.equals("2")){
				totalS22Pasado += listadoHistoricoServicioActual.get(i).getSubtitulo22().getTotal();
				totalS22Futuro += listadoHistoricoServicioActual.get(i).getSubtitulo22().getTotalFuturo();
				totalPasado += listadoHistoricoServicioActual.get(i).getSubtitulo22().getTotal();
				totalFuturo += listadoHistoricoServicioActual.get(i).getSubtitulo22().getTotalFuturo();
				listadoHistoricoServicioActual.get(i).setTotalAnoActual(listadoHistoricoServicioActual.get(i).getSubtitulo22().getTotalFuturo());
				tieneS22 = true;
			}
			if(subtitulo!=null && subtitulo.equals("4")){
				totalS29Pasado += listadoHistoricoServicioActual.get(i).getSubtitulo29().getTotal();
				totalS29Futuro += listadoHistoricoServicioActual.get(i).getSubtitulo29().getTotalFuturo();
				totalPasado += listadoHistoricoServicioActual.get(i).getSubtitulo29().getTotal();
				totalFuturo += listadoHistoricoServicioActual.get(i).getSubtitulo29().getTotalFuturo();
				listadoHistoricoServicioActual.get(i).setTotalAnoActual(listadoHistoricoServicioActual.get(i).getSubtitulo29().getTotalFuturo());
				tieneS29 = true;
			}
			
			
		}
		return null;
	}
	public void seleccionComponente(){
		if(componenteSeleccionado!=null){
			setComponenteSeleccionado(componenteSeleccionado);
		}
	}
	public void buscarResultados(){
		listadoHistoricoServicioActual = programasService.getHistoricoServicio(programaSeleccionado, Integer.valueOf(componenteSeleccionado), Integer.valueOf(servicioSeleccionado));
		getTotales(listadoHistoricoServicioActual);
	}

	private Integer getTotales(List<ProgramaServicioHistoricoVO> listadoHistoricoServicioActual){
	
		totalS21Pasado=0;
		totalS21Futuro=0;
		totalS22Pasado=0;
		totalS22Futuro=0;
		totalS29Pasado=0;
		totalS29Futuro=0;
		tieneS21=false;
		tieneS22=false;
		tieneS29=false;
		totalPasado=0;
		totalFuturo=0;
		for(ProgramaServicioHistoricoVO prog : listadoHistoricoServicioActual){
			if(prog.getSubtitulo21()!=null){
				totalS21Pasado += prog.getTotalAnoAnterior();
				totalS21Futuro += prog.getTotalAnoActual();
				totalPasado += prog.getTotalAnoAnterior();
				totalFuturo += prog.getTotalAnoActual();
				tieneS21 = true;
			}
			if(prog.getSubtitulo22()!=null){
				totalS22Pasado += prog.getTotalAnoAnterior();
				totalS22Futuro += prog.getTotalAnoActual();
				totalPasado += prog.getTotalAnoAnterior();
				totalFuturo += prog.getTotalAnoActual();
				tieneS22 = true;
			}
			if(prog.getSubtitulo29()!=null){
				totalS29Pasado += prog.getTotalAnoAnterior();
				totalS29Futuro += prog.getTotalAnoActual();
				totalPasado += prog.getTotalAnoAnterior();
				totalFuturo += prog.getTotalAnoActual();
				tieneS29 = true;
			}
		}
		return null;
	}
	
	public void guardar(){
		System.out.println("Guardar los cambios:"+ componenteSeleccionado+" servicio: "+servicioSeleccionado);
		System.out.println(listadoHistoricoServicioActual.get(0).getNombreEstablecimiento());
		programasService.guardarProgramaHistoricoServicio(listadoHistoricoServicioActual);
	}

	
	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		System.out.println("createResultData usuario-->"
				+ getSessionBean().getUsername());
		parameters.put("recursosAPSMunicipal_", false);
		
		
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

	public List<ProgramaServicioHistoricoVO> getListadoHistoricoServicioActual() {
		return listadoHistoricoServicioActual;
	}

	public void setListadoHistoricoServicioActual(
			List<ProgramaServicioHistoricoVO> listadoHistoricoServicioActual) {
		this.listadoHistoricoServicioActual = listadoHistoricoServicioActual;
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

	public Double getInflactorS21() {
		return inflactorS21;
	}

	public void setInflactorS21(Double inflactorS21) {
		this.inflactorS21 = inflactorS21;
	}

	public Double getInflactorS22() {
		return inflactorS22;
	}

	public void setInflactorS22(Double inflactorS22) {
		this.inflactorS22 = inflactorS22;
	}

	public Double getInflactorS29() {
		return inflactorS29;
	}

	public void setInflactorS29(Double inflactorS29) {
		this.inflactorS29 = inflactorS29;
	}

	public Integer getTotalS21Pasado() {
		return totalS21Pasado;
	}

	public void setTotalS21Pasado(Integer totalS21Pasado) {
		this.totalS21Pasado = totalS21Pasado;
	}

	public Integer getTotalS21Futuro() {
		return totalS21Futuro;
	}

	public void setTotalS21Futuro(Integer totalS21Futuro) {
		this.totalS21Futuro = totalS21Futuro;
	}

	public Integer getTotalS22Pasado() {
		return totalS22Pasado;
	}

	public void setTotalS22Pasado(Integer totalS22Pasado) {
		this.totalS22Pasado = totalS22Pasado;
	}

	public Integer getTotalS22Futuro() {
		return totalS22Futuro;
	}

	public void setTotalS22Futuro(Integer totalS22Futuro) {
		this.totalS22Futuro = totalS22Futuro;
	}

	public Integer getTotalS29Pasado() {
		return totalS29Pasado;
	}

	public void setTotalS29Pasado(Integer totalS29Pasado) {
		this.totalS29Pasado = totalS29Pasado;
	}

	public Integer getTotalS29Futuro() {
		return totalS29Futuro;
	}

	public void setTotalS29Futuro(Integer totalS29Futuro) {
		this.totalS29Futuro = totalS29Futuro;
	}

	public boolean isTieneS21() {
		return tieneS21;
	}

	public void setTieneS21(boolean tieneS21) {
		this.tieneS21 = tieneS21;
	}

	public boolean isTieneS22() {
		return tieneS22;
	}

	public void setTieneS22(boolean tieneS22) {
		this.tieneS22 = tieneS22;
	}

	public boolean isTieneS29() {
		return tieneS29;
	}

	public void setTieneS29(boolean tieneS29) {
		this.tieneS29 = tieneS29;
	}

	public String getSubtitulo() {
		return subtitulo;
	}

	public void setSubtitulo(String subtitulo) {
		this.subtitulo = subtitulo;
	}

	public Integer getTotalPasado() {
		return totalPasado;
	}

	public void setTotalPasado(Integer totalPasado) {
		this.totalPasado = totalPasado;
	}

	public Integer getTotalFuturo() {
		return totalFuturo;
	}

	public void setTotalFuturo(Integer totalFuturo) {
		this.totalFuturo = totalFuturo;
	}

	public Integer getTotalPasadoHorizontal() {
		return totalPasadoHorizontal;
	}

	public void setTotalPasadoHorizontal(Integer totalPasadoHorizontal) {
		this.totalPasadoHorizontal = totalPasadoHorizontal;
	}

	public Integer getTotalFuturoHorizontal() {
		return totalFuturoHorizontal;
	}

	public void setTotalFuturoHorizontal(Integer totalFuturoHorizontal) {
		this.totalFuturoHorizontal = totalFuturoHorizontal;
	}

	public ProgramaVO getPrograma() {
		return programa;
	}

	public void setPrograma(ProgramaVO programa) {
		this.programa = programa;
	}


}
