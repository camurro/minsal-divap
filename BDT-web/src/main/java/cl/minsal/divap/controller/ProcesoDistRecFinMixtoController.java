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
import minsal.divap.vo.ResumenProgramaMixtoVO;
import minsal.divap.vo.ResumenProgramaServiciosVO;
import minsal.divap.vo.ResumenProgramaVO;
import minsal.divap.vo.ServiciosVO;
import minsal.divap.vo.SubtituloVO;

import org.apache.log4j.Logger;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named ( "procesoDistRecFinMixtoController" ) 
@ViewScoped 
public class ProcesoDistRecFinMixtoController extends AbstractTaskMBean implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6442364795293061655L;
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
	
	private List<ProgramaServicioVO> detalleEstablecimientos;
	private List<ProgramaMunicipalVO> detalleComunas;
	
	
	private boolean tiene21;
	private boolean tiene22;
	private boolean tiene29;
	private boolean tiene24;
	
	private Long total21;
	private Long total22;
	private Long total29;
	private Long totalFinal;
	
	private String posicionElemento;
	private String precioCantidad;
	private String subtitulo;
	private Long totalPxQ;
	
	private List<ResumenProgramaServiciosVO> resumenProgramaServicio;
	private List<ResumenProgramaVO> resumenProgramaMunicipal;
	private List<ResumenProgramaMixtoVO> resumenProgramaMixto;
	
	private Integer programaSeleccionado;
	private Long totalResumen21;
	private Long totalResumen22;
	private Long totalResumen24;
	private Long totalResumen29;
	private Long totalResumen;
	
	private ProgramaVO programa;
	private ProgramaVO programaProxAno;
	private Integer ano;
	
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
			this.ano = (Integer) getTaskDataVO().getData().get("_ano");
			System.out.println("this.ano --->" + this.ano);
		}
		programa = programasService.getProgramaByIdProgramaAndAno(programaSeleccionado, (ano - 1));
		programaProxAno = programasService.getProgramaByIdProgramaAndAno(programaSeleccionado, ano);
		listaServicios = utilitariosService.getAllServicios();
		listaComponentes = componenteService.getComponentesByProgramaAno(programaProxAno.getIdProgramaAno());
		armarResumenPrograma();
	}
	
	private void armarResumenPrograma() {
		resumenProgramaMixto =  new ArrayList<ResumenProgramaMixtoVO>();
		if(programaSeleccionado != null){
			resumenServicio();
			resumenMunicipal();
			//Recorremos para armar un único VO de resumen
			totalResumen = 0L;
			for(int i = 0; i< resumenProgramaServicio.size(); i++){
				System.out.println(resumenProgramaServicio.get(i).getIdServicio());
				ResumenProgramaMixtoVO mixto = new ResumenProgramaMixtoVO();
				mixto.setIdServicio(resumenProgramaServicio.get(i).getIdServicio());
				mixto.setNombreServicio(resumenProgramaServicio.get(i).getNombreServicio());
				if(resumenProgramaServicio.get(i).getTotalS21() != null){
					mixto.setTotalS21(resumenProgramaServicio.get(i).getTotalS21().longValue());
				}
				if(resumenProgramaServicio.get(i).getTotalS22() != null){
					mixto.setTotalS22(resumenProgramaServicio.get(i).getTotalS22().longValue());
				}
				if(resumenProgramaServicio.get(i).getTotalS29() != null){
					mixto.setTotalS29(resumenProgramaServicio.get(i).getTotalS29().longValue());
				}
				resumenProgramaMixto.add(mixto);  
			}
			for(int i = 0; i< resumenProgramaMunicipal.size(); i++){
				ResumenProgramaMixtoVO mixtoServicio = new ResumenProgramaMixtoVO();
				mixtoServicio.setIdServicio(resumenProgramaMunicipal.get(i).getIdServicio());
				int posicion = resumenProgramaMixto.indexOf(mixtoServicio);
                mixtoServicio.setIdServicio(resumenProgramaMunicipal.get(i).getIdServicio());
				if(posicion !=-1){
					resumenProgramaMixto.get(posicion).setTotalS24(resumenProgramaMunicipal.get(i).getTotalS24().longValue());
				}else{
					ResumenProgramaMixtoVO mixto = new ResumenProgramaMixtoVO();
					mixto.setIdServicio(resumenProgramaMunicipal.get(i).getIdServicio());
					mixto.setNombreServicio(resumenProgramaMunicipal.get(i).getNombreServicio());
					if(resumenProgramaMunicipal.get(i).getTotalS24() != null){
						mixto.setTotalS24(resumenProgramaMunicipal.get(i).getTotalS24().longValue());
					}
					resumenProgramaMixto.add(mixto);
				}
			}
			for(ResumenProgramaMixtoVO resumenProgramaMixtoVO: resumenProgramaMixto){
				totalResumen += resumenProgramaMixtoVO.getTotalServicio();
			}
			System.out.println(resumenProgramaMixto);
		}
	}

	private void resumenMunicipal() {
		resumenProgramaMunicipal = programasService.getResumenMunicipal(programaProxAno.getIdProgramaAno(), 3);
		totalResumen24 = 0L;
		tiene24 = false;
		List<ComponentesVO> componentesPrograma = programasService.getComponentesByProgramaAnoSubtitulos(programa.getIdProgramaAno(), Subtitulo.SUBTITULO24);
		if(componentesPrograma != null && componentesPrograma.size() > 0){
			tiene24 = true;
		}
		if(resumenProgramaMunicipal.size() > 0){
			for (ResumenProgramaVO resumen : resumenProgramaMunicipal) {
				totalResumen24 += resumen.getTotalS24();
			}
		}
	}

	private void resumenServicio() {
			resumenProgramaServicio = programasService.getResumenServicio(programaProxAno.getIdProgramaAno(), programa.getId());
			totalResumen21 = 0L;
			totalResumen22 = 0L;
			totalResumen29 = 0L;
			for(ResumenProgramaServiciosVO resumen : resumenProgramaServicio){
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
			}
	}

	public String recalcularTotales(){
		System.out.println("recalcularTotales subtitulo=" + subtitulo);
		if(subtitulo != null && !subtitulo.trim().isEmpty()){
			Integer idSubtitulo = Integer.parseInt(subtitulo);
			if(Subtitulo.SUBTITULO21.getId().equals(idSubtitulo)){
				detalleEstablecimientos.get(Integer.valueOf(getPosicionElemento())).getSubtitulo21().setTotal(detalleEstablecimientos.get(Integer.valueOf(getPosicionElemento())).getSubtitulo21().getCantidad()*detalleEstablecimientos.get(Integer.valueOf(getPosicionElemento())).getSubtitulo21().getMonto());;
			}
			if(Subtitulo.SUBTITULO22.getId().equals(idSubtitulo)){
				detalleEstablecimientos.get(Integer.valueOf(getPosicionElemento())).getSubtitulo22().setTotal(detalleEstablecimientos.get(Integer.valueOf(getPosicionElemento())).getSubtitulo22().getCantidad()*detalleEstablecimientos.get(Integer.valueOf(getPosicionElemento())).getSubtitulo22().getMonto());;
			}
			if(Subtitulo.SUBTITULO29.getId().equals(idSubtitulo)){
				detalleEstablecimientos.get(Integer.valueOf(getPosicionElemento())).getSubtitulo29().setTotal(detalleEstablecimientos.get(Integer.valueOf(getPosicionElemento())).getSubtitulo29().getCantidad()*detalleEstablecimientos.get(Integer.valueOf(getPosicionElemento())).getSubtitulo29().getMonto());;
			}
		}else{
			detalleComunas.get(Integer.valueOf(getPosicionElemento())).setTotal( detalleComunas.get(Integer.valueOf(getPosicionElemento())).getCantidad() * detalleComunas.get(Integer.valueOf(getPosicionElemento())).getPrecio() );
			getTotalesPxQ(detalleComunas);
		}
		calculaTotalesTabla();
		return null;
	}
	
	public void seleccionComponente(){
		if(componenteSeleccionado != null && !componenteSeleccionado.trim().isEmpty()){
			setComponenteSeleccionado(componenteSeleccionado);
		}
		detalleComunas = new ArrayList<ProgramaMunicipalVO>();
		detalleEstablecimientos = new ArrayList<ProgramaServicioVO>();
	}
	
	public String buscarResultados(){
		if(componenteSeleccionado != null && !componenteSeleccionado.trim().isEmpty()){
			Integer idServicio = ((servicioSeleccionado != null && !servicioSeleccionado.trim().isEmpty()) ? Integer.parseInt(servicioSeleccionado) : null);
			Integer idComponente = Integer.parseInt(componenteSeleccionado);
			detalleEstablecimientos = programasService.findByServicioComponenteServicios(idComponente, idServicio, programaProxAno.getIdProgramaAno());
			detalleComunas = programasService.findByServicioComponente(idComponente, idServicio, programaProxAno.getIdProgramaAno());
			getTotalesPxQ(detalleComunas);
			calculaTotalesTabla();
		}else{
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe seleccionar el componente antes de realizar la búsqueda", null);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			detalleComunas = new ArrayList<ProgramaMunicipalVO>();
			detalleEstablecimientos = new ArrayList<ProgramaServicioVO>();
		}
		return null;
	}
	
	private Long getTotalesPxQ(List<ProgramaMunicipalVO> detalleComunas){
		totalPxQ = 0L;
		for (int i=0; i<detalleComunas.size(); i++) {
			totalPxQ += detalleComunas.get(i).getTotal();	
		}
		return totalPxQ;
	}
	
	private void calculaTotalesTabla(){
		tiene21 = false;
		tiene22 = false;
		tiene29 = false;
		total21 = 0L;
		total22 = 0L;
		total29 = 0L;
		totalFinal = 0L;
		if(componenteSeleccionado != null && !componenteSeleccionado.trim().isEmpty()){
			Integer idComponente = Integer.parseInt(componenteSeleccionado);
			ComponentesVO componentesVO = componenteService.getComponenteById(idComponente);
			if(componentesVO != null && componentesVO.getSubtitulos() != null && componentesVO.getSubtitulos().size() > 0){
				for(SubtituloVO subtituloVO : componentesVO.getSubtitulos()){
					if(Subtitulo.SUBTITULO21.getId().equals(subtituloVO.getId())){
						tiene21 = true;
					}else if(Subtitulo.SUBTITULO22.getId().equals(subtituloVO.getId())){
						tiene22 = true;
					}else if(Subtitulo.SUBTITULO29.getId().equals(subtituloVO.getId())){
						tiene29 = true;
					}
				}
			}
		}
		if(detalleEstablecimientos != null && detalleEstablecimientos.size()>0){
			for (ProgramaServicioVO detalle : detalleEstablecimientos) {
				if(detalle.getSubtitulo21() != null){
					total21 += detalle.getSubtitulo21().getTotal();
				}
				if(detalle.getSubtitulo22() != null){
					total22 += detalle.getSubtitulo22().getTotal();
				}
				if(detalle.getSubtitulo29() != null){
					total29 += detalle.getSubtitulo29().getTotal();
				}
			}
			totalFinal += total21 + total22 + total29;
		}
	}

	public void guardar(){
		programasService.guardarProgramaReforzamiento(detalleComunas);
		programasService.guardarProgramaReforzamientoServicios(detalleEstablecimientos);
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

	public void refrescar(){}
	
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

	public Long getTotalPxQ() {
		return totalPxQ;
	}

	public void setTotalPxQ(Long totalPxQ) {
		this.totalPxQ = totalPxQ;
	}

	
	public List<ResumenProgramaServiciosVO> getResumenProgramaServicio() {
		return resumenProgramaServicio;
	}

	public void setResumenProgramaServicio(
			List<ResumenProgramaServiciosVO> resumenProgramaServicio) {
		this.resumenProgramaServicio = resumenProgramaServicio;
	}

	public List<ResumenProgramaVO> getResumenProgramaMunicipal() {
		return resumenProgramaMunicipal;
	}

	public void setResumenProgramaMunicipal(
			List<ResumenProgramaVO> resumenProgramaMunicipal) {
		this.resumenProgramaMunicipal = resumenProgramaMunicipal;
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



	public String getSubtitulo() {
		return subtitulo;
	}

	public void setSubtitulo(String subtitulo) {
		this.subtitulo = subtitulo;
	}

	

	public Long getTotalResumen() {
		return totalResumen;
	}

	public void setTotalResumen(Long totalResumen) {
		this.totalResumen = totalResumen;
	}

	public List<ProgramaMunicipalVO> getDetalleComunas() {
		return detalleComunas;
	}

	public void setDetalleComunas(List<ProgramaMunicipalVO> detalleComunas) {
		this.detalleComunas = detalleComunas;
	}



	public List<ResumenProgramaMixtoVO> getResumenProgramaMixto() {
		return resumenProgramaMixto;
	}

	public void setResumenProgramaMixto(
			List<ResumenProgramaMixtoVO> resumenProgramaMixto) {
		this.resumenProgramaMixto = resumenProgramaMixto;
	}

	public boolean isTiene24() {
		return tiene24;
	}

	public void setTiene24(boolean tiene24) {
		this.tiene24 = tiene24;
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

	public Long getTotalResumen24() {
		return totalResumen24;
	}

	public void setTotalResumen24(Long totalResumen24) {
		this.totalResumen24 = totalResumen24;
	}

	public Long getTotalResumen29() {
		return totalResumen29;
	}

	public void setTotalResumen29(Long totalResumen29) {
		this.totalResumen29 = totalResumen29;
	}

}
