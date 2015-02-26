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

@Named ( "procesoModificacionDistRecFinHistoricoMixtoController" ) 
@ViewScoped 
public class ProcesoModificacionDistRecFinHistoricoMixtoController extends AbstractTaskMBean implements Serializable  {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2627712921793024122L;
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
	private List<ProgramaMunicipalHistoricoVO> listadoHistoricoMunicipalActual;
	
	private String posicionElemento;
	private String precioCantidad;
	private Integer totalPxQ;
	
	private List<ResumenProgramaVO> resumenPrograma;
	
	private Integer programaSeleccionado;
	private Long totalResumen24;
	
	private Double inflactorS21;
	private Double inflactorS22;
	private Double inflactorS24;
	private Double inflactorS29;
	
	private Long totalS21Pasado;
	private Long totalS21Futuro;
	private Long totalS22Pasado;
	private Long totalS22Futuro;
	private Long totalS29Pasado;
	private Long totalS29Futuro;
	private Long totalS24Pasado;
	private Long totalS24Futuro;
	
	private boolean tieneS21;
	private boolean tieneS22;
	private boolean tieneS29;
	
	private Long totalPasado;
	private Long totalFuturo;
	private Long totalPasadoHorizontal;
	private Long totalFuturoHorizontal;
	private String subtitulo;
	private Integer ano;
	private ProgramaVO programaProxAno;
	
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
			ano = (Integer) getTaskDataVO().getData().get("_ano");
		}
		programa = programasService.getProgramaByIdProgramaAndAno(programaSeleccionado, (ano - 1));
		programaProxAno = programasService.getProgramaByIdProgramaAndAno(programaSeleccionado, ano);
		listaServicios = utilitariosService.getAllServicios();
		listaComponentes= componenteService.getComponenteByPrograma(programa.getId());
		inflactorS21 = subtituloService.getInflactor(1);
		inflactorS22 = subtituloService.getInflactor(2);
		inflactorS24 = subtituloService.getInflactor(3);
		inflactorS29 = subtituloService.getInflactor(4);
		//armarResumenPrograma();
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
		if(subtitulo.equals("3")){
			totalS24Pasado=0l;
			totalS24Futuro=0l;
			for(int i=0; i < listadoHistoricoMunicipalActual.size();i++){
				totalS24Pasado += listadoHistoricoMunicipalActual.get(i).getTotalAnoAnterior();
				totalS24Futuro += listadoHistoricoMunicipalActual.get(i).getTotalAnoActual();
			}
		}
		else{
			if(subtitulo!=null && subtitulo.equals("1")){
				setTotalS21Pasado(0l);
				setTotalS21Futuro(0l);
			}
			if(subtitulo!=null && subtitulo.equals("2")){
				setTotalS22Pasado(0l);
				setTotalS22Futuro(0l);
			}
			if(subtitulo!=null && subtitulo.equals("4")){
				setTotalS29Pasado(0l);
				setTotalS29Futuro(0l);
			}
			setTotalPasado(0l);
			setTotalFuturo(0l);
			setTotalPasadoHorizontal(0l);
			setTotalFuturoHorizontal(0l);
			for(int i=0; i < listadoHistoricoServicioActual.size();i++){
				if(subtitulo!=null && subtitulo.equals("1")){
					totalS21Pasado += listadoHistoricoServicioActual.get(i).getSubtitulo21().getTotal();
					totalS21Futuro += listadoHistoricoServicioActual.get(i).getSubtitulo21().getTotalFuturo();
					listadoHistoricoServicioActual.get(i).setTotalAnoActual(listadoHistoricoServicioActual.get(i).getSubtitulo21().getTotalFuturo());
					tieneS21 = true;
				}
				if(subtitulo!=null && subtitulo.equals("2")){
					totalS22Pasado += listadoHistoricoServicioActual.get(i).getSubtitulo22().getTotal();
					totalS22Futuro += listadoHistoricoServicioActual.get(i).getSubtitulo22().getTotalFuturo();
					listadoHistoricoServicioActual.get(i).setTotalAnoActual(listadoHistoricoServicioActual.get(i).getSubtitulo22().getTotalFuturo());
					tieneS22 = true;
				}
				if(subtitulo!=null && subtitulo.equals("4")){
					totalS29Pasado += listadoHistoricoServicioActual.get(i).getSubtitulo29().getTotal();
					totalS29Futuro += listadoHistoricoServicioActual.get(i).getSubtitulo29().getTotalFuturo();
					
					listadoHistoricoServicioActual.get(i).setTotalAnoActual(listadoHistoricoServicioActual.get(i).getSubtitulo29().getTotalFuturo());
					tieneS29 = true;
				}
				totalPasado += listadoHistoricoServicioActual.get(i).getTotalAnoAnterior();
				totalFuturo += listadoHistoricoServicioActual.get(i).getTotalAnoActual();
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
		listadoHistoricoMunicipalActual = programasService.getHistoricoMunicipal(programaSeleccionado, Integer.valueOf(componenteSeleccionado), Integer.valueOf(servicioSeleccionado));
		getTotales(listadoHistoricoServicioActual);
	}

	private Integer getTotales(List<ProgramaServicioHistoricoVO> listadoHistoricoServicioActual){
	
		totalS21Pasado=0l;
		totalS21Futuro=0l;
		totalS22Pasado=0l;
		totalS22Futuro=0l;
		totalS29Pasado=0l;
		totalS29Futuro=0l;
		tieneS21=false;
		tieneS22=false;
		tieneS29=false;
		totalPasado=0l;
		totalFuturo=0l;
		
		totalS24Pasado=0l;
		totalS24Futuro=0l;
		for(ProgramaMunicipalHistoricoVO prog : listadoHistoricoMunicipalActual){
			totalS24Pasado += prog.getTotalAnoAnterior();
			totalS24Futuro += prog.getTotalAnoActual();
		}
		
		for(ProgramaServicioHistoricoVO prog : listadoHistoricoServicioActual){
			if(prog.getSubtitulo21()!=null){
				totalS21Pasado += prog.getSubtitulo21().getTotal();
				totalS21Futuro += prog.getSubtitulo21().getTotalFuturo();
				tieneS21 = true;
			}
			if(prog.getSubtitulo22()!=null){
				totalS22Pasado += prog.getSubtitulo22().getTotal();
				totalS22Futuro += prog.getSubtitulo22().getTotalFuturo();
				tieneS22 = true;
			}
			if(prog.getSubtitulo29()!=null){
				totalS29Pasado += prog.getTotalAnoAnterior();
				totalS29Futuro += prog.getTotalAnoActual();
				tieneS29 = true;
			}
			totalPasado += prog.getTotalAnoAnterior();
			totalFuturo += prog.getTotalAnoActual();
		}
		return null;
	}
	
	public void guardar(){
		programasService.guardarProgramaHistoricoServicio(listadoHistoricoServicioActual);
		programasService.guardarProgramaHistoricoMunicipal(listadoHistoricoMunicipalActual);
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


	public List<ProgramaServicioHistoricoVO> getListadoHistoricoServicioActual() {
		return listadoHistoricoServicioActual;
	}

	public void setListadoHistoricoServicioActual(
			List<ProgramaServicioHistoricoVO> listadoHistoricoServicioActual) {
		this.listadoHistoricoServicioActual = listadoHistoricoServicioActual;
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

	public Long getTotalResumen24() {
		return totalResumen24;
	}

	public void setTotalResumen24(Long totalResumen24) {
		this.totalResumen24 = totalResumen24;
	}

	public Long getTotalS21Pasado() {
		return totalS21Pasado;
	}

	public void setTotalS21Pasado(Long totalS21Pasado) {
		this.totalS21Pasado = totalS21Pasado;
	}

	public Long getTotalS21Futuro() {
		return totalS21Futuro;
	}

	public void setTotalS21Futuro(Long totalS21Futuro) {
		this.totalS21Futuro = totalS21Futuro;
	}

	public Long getTotalS22Pasado() {
		return totalS22Pasado;
	}

	public void setTotalS22Pasado(Long totalS22Pasado) {
		this.totalS22Pasado = totalS22Pasado;
	}

	public Long getTotalS22Futuro() {
		return totalS22Futuro;
	}

	public void setTotalS22Futuro(Long totalS22Futuro) {
		this.totalS22Futuro = totalS22Futuro;
	}

	public Long getTotalS29Pasado() {
		return totalS29Pasado;
	}

	public void setTotalS29Pasado(Long totalS29Pasado) {
		this.totalS29Pasado = totalS29Pasado;
	}

	public Long getTotalS29Futuro() {
		return totalS29Futuro;
	}

	public void setTotalS29Futuro(Long totalS29Futuro) {
		this.totalS29Futuro = totalS29Futuro;
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

	public Long getTotalPasado() {
		return totalPasado;
	}

	public void setTotalPasado(Long totalPasado) {
		this.totalPasado = totalPasado;
	}

	public Long getTotalFuturo() {
		return totalFuturo;
	}

	public void setTotalFuturo(Long totalFuturo) {
		this.totalFuturo = totalFuturo;
	}

	public Long getTotalPasadoHorizontal() {
		return totalPasadoHorizontal;
	}

	public void setTotalPasadoHorizontal(Long totalPasadoHorizontal) {
		this.totalPasadoHorizontal = totalPasadoHorizontal;
	}

	public Long getTotalFuturoHorizontal() {
		return totalFuturoHorizontal;
	}

	public void setTotalFuturoHorizontal(Long totalFuturoHorizontal) {
		this.totalFuturoHorizontal = totalFuturoHorizontal;
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
