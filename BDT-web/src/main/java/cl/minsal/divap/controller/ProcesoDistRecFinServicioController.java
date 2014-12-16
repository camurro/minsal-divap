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
import minsal.divap.service.UtilitariosService;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ProgramaServicioVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ResumenProgramaServiciosVO;
import minsal.divap.vo.ServiciosVO;

import org.apache.log4j.Logger;

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
	@EJB
	private RecursosFinancierosProgramasReforzamientoService recursosFinancierosProgramasReforzamientoService;
	
	private String servicioSeleccionado;
	private List<ServiciosVO> listaServicios;
	
	private String componenteSeleccionado;
	private List<ComponentesVO> listaComponentes;
	
	private List<ProgramaServicioVO> detalleEstablecimientos;
	
	
	private boolean tiene21;
	private boolean tiene22;
	private boolean tiene29;
	
	private Long total21;
	private Long total22;
	private Long total29;
	private Long totalFinal;
	
	private String posicionElemento;
	private String precioCantidad;
	private String subtitulo;
	private Long totalPxQ;
	
	private List<ResumenProgramaServiciosVO> resumenPrograma;
	
	private Integer programaSeleccionado;
	private Long totalResumen21;
	private Long totalResumen22;
	private Long totalResumen29;
	private Long totalResumen;
	
	private ProgramaVO programa;
	
	
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
		programa = programasService.getProgramaAno(programaSeleccionado);
		listaServicios = utilitariosService.getAllServicios();
		listaComponentes= componenteService.getComponenteByPrograma(programa.getIdProgramaAno());
		
		armarResumenPrograma();
	}
	
	private void armarResumenPrograma() {
		if(programaSeleccionado!=null){
			Integer anoSiguiente = recursosFinancierosProgramasReforzamientoService.getAnoCurso() + 1;
			int IdProgramaProxAno = programasService.getProgramaAnoSiguiente(programaSeleccionado, anoSiguiente);
			
			
			resumenPrograma = programasService.getResumenServicio(IdProgramaProxAno, programa.getIdProgramaAno());
			totalResumen21=0l;
			totalResumen22=0l;
			totalResumen29=0l;
			totalResumen=0l;
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
			total21=0l;
			total22=0l;
			total29=0l;
			totalFinal=0l;
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

	public List<ResumenProgramaServiciosVO> getResumenPrograma() {
		return resumenPrograma;
	}

	public void setResumenPrograma(List<ResumenProgramaServiciosVO> resumenPrograma) {
		this.resumenPrograma = resumenPrograma;
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

	public String getSubtitulo() {
		return subtitulo;
	}

	public void setSubtitulo(String subtitulo) {
		this.subtitulo = subtitulo;
	}

	public ProgramaVO getPrograma() {
		return programa;
	}

	public void setPrograma(ProgramaVO programa) {
		this.programa = programa;
	}

	public Long getTotal21() {
		return total21;
	}

	public void setTotal21(Long total21) {
		this.total21 = total21;
	}

	public Long getTotal22() {
		return total22;
	}

	public void setTotal22(Long total22) {
		this.total22 = total22;
	}

	public Long getTotal29() {
		return total29;
	}

	public void setTotal29(Long total29) {
		this.total29 = total29;
	}

	public Long getTotalFinal() {
		return totalFinal;
	}

	public void setTotalFinal(Long totalFinal) {
		this.totalFinal = totalFinal;
	}

	public Long getTotalPxQ() {
		return totalPxQ;
	}

	public void setTotalPxQ(Long totalPxQ) {
		this.totalPxQ = totalPxQ;
	}

	public Long getTotalResumen21() {
		return totalResumen21;
	}

	public void setTotalResumen21(Long totalResumen21) {
		this.totalResumen21 = totalResumen21;
	}

	public Long getTotalResumen22() {
		return totalResumen22;
	}

	public void setTotalResumen22(Long totalResumen22) {
		this.totalResumen22 = totalResumen22;
	}

	public Long getTotalResumen29() {
		return totalResumen29;
	}

	public void setTotalResumen29(Long totalResumen29) {
		this.totalResumen29 = totalResumen29;
	}

	public Long getTotalResumen() {
		return totalResumen;
	}

	public void setTotalResumen(Long totalResumen) {
		this.totalResumen = totalResumen;
	}

	public Integer getProgramaSeleccionado() {
		return programaSeleccionado;
	}

	public void setProgramaSeleccionado(Integer programaSeleccionado) {
		this.programaSeleccionado = programaSeleccionado;
	}
	
}
