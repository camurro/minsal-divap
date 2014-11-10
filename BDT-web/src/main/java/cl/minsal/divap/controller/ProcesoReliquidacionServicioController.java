package cl.minsal.divap.controller;

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

import minsal.divap.enums.BusinessProcess;
import minsal.divap.service.ProgramasService;
import minsal.divap.service.ServicioSaludService;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.EstablecimientoVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ServiciosVO;
import minsal.divap.vo.TaskDataVO;
import minsal.divap.vo.TaskVO;

import org.apache.log4j.Logger;

import cl.minsal.divap.pojo.ComunaPojo;
import cl.minsal.divap.pojo.EnvioServiciosPojo;
import cl.minsal.divap.pojo.EstablecimientoPojo;
import cl.minsal.divap.pojo.ProgramasPojo;
import cl.minsal.divap.pojo.ValorHistoricoPojo;
import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named("procesoReliquidacionServicioController") 
@ViewScoped
public class ProcesoReliquidacionServicioController extends AbstractTaskMBean implements
				Serializable {
	

	private static final long serialVersionUID = 5164638468861890516L;
	@Inject
	private transient Logger log;
	@Inject 
	FacesContext facesContext;
	@EJB
	private ProgramasService programaService;
	@EJB
	private ServicioSaludService serviciosService;
	private List<ProgramaVO> listadoProgramasUsuario;
	private List<ComponentesVO> listadoComponentes;
	private List<ServiciosVO> servicios;
	private ProgramaVO programaSeleccionado;
	private List<ProgramasPojo> listadoProgramas;
	private List<EstablecimientoVO> listadoEstablecimientos;
	private Integer idReliquidacion;
	private Integer idProgramaAno;
	private String componenteSeleccionado;
	private String servicioSeleccionado;
	private ProgramaVO programa;
	
	public List<ProgramasPojo> getListadoProgramas() {
		return listadoProgramas;
	}
	
	public void setListadoProgramas( List<ProgramasPojo> listadoProgramas ) {
		this.listadoProgramas = listadoProgramas;
	}
	
	
	private List<EnvioServiciosPojo> listadoEnvioServicios;
	
	public List<EnvioServiciosPojo> getListadoEnvioServicios() {
		return listadoEnvioServicios;
	}
	
	public void setListadoEnvioServicios( List<EnvioServiciosPojo> listadoEnvioServicios ) {
		this.listadoEnvioServicios = listadoEnvioServicios;
	}
	
	
	
	public List<EstablecimientoVO> getListadoEstablecimientos() {
		return listadoEstablecimientos;
	}
	
	public void setListadoEstablecimientos( List<EstablecimientoVO> listadoEstablecimientos ) {
		this.listadoEstablecimientos = listadoEstablecimientos;
	}
	
	private List<ComunaPojo> listadoComunas;
	
	public List<ComunaPojo> getListadoComunas() {
		return listadoComunas;
	}
	
	public void setListadoComunas( List<ComunaPojo> listadoComunas ) {
		this.listadoComunas = listadoComunas;
	}
	
	private List<ValorHistoricoPojo> listadoValorHistorico;
	
	public List<ValorHistoricoPojo> getListadoValorHistorico() {
		return listadoValorHistorico;
	}
	
	public void setListadoValorHistorico( List<ValorHistoricoPojo> listadoValorHistorico ) {
		this.listadoValorHistorico = listadoValorHistorico;
	}
	
	private Long totalS21;
	private Long totalS22;
	private Long totalS24;
	private Long totalS29;
	
	
	
	public Long getTotalS21() {
		Long suma = 0L;
		if(listadoEstablecimientos != null && listadoEstablecimientos.size() >0){
			for(EstablecimientoVO e : listadoEstablecimientos){
				//suma+=e.gettS21();
			}
		}
		return suma;
	}

	public void setTotalS21( Long totalS21 ) {
		this.totalS21 = totalS21;
	}

	public Long getTotalS22() {
		Long suma = 0L;
		if(listadoEstablecimientos != null && listadoEstablecimientos.size() >0){
			for(EstablecimientoVO e : listadoEstablecimientos){
				//suma+=e.gettS22();
			}
		}
		return suma;
	}

	public void setTotalS22( Long totalS22 ) {
		this.totalS22 = totalS22;
	}

	public Long getTotalS24() {
		Long suma = 0L;
		if(listadoEstablecimientos != null && listadoEstablecimientos.size() >0){
			for(EstablecimientoVO e : listadoEstablecimientos){
				//suma+=e.gettS24();
			}
		}
		return suma;
	}

	public void setTotalS24( Long totalS24 ) {
		this.totalS24 = totalS24;
	}

	public Long getTotalS29() {
		Long suma = 0L;
		if(listadoEstablecimientos != null && listadoEstablecimientos.size() >0){
			for(EstablecimientoVO e : listadoEstablecimientos){
				//suma+=e.gettS29();
			}
		}
		return suma;
	}

	public void setTotalS29( Long totalS29 ) {
		this.totalS29 = totalS29;
	}

	@PostConstruct
	public void init() {
		if(sessionExpired()){
			return;
		}
		if (getTaskDataVO() != null && getTaskDataVO().getData() != null) {
			this.idProgramaAno = (Integer) getTaskDataVO().getData().get("_idProgramaAno");
			this.idReliquidacion = (Integer) getTaskDataVO().getData().get("_idReliquidacion");
			System.out.println("this.idProgramaAno --> "+this.idProgramaAno);
			System.out.println("this.idReliquidacion --> "+this.idReliquidacion);
			setPrograma(programaService.getProgramaAno(idProgramaAno));
		}
	}
	
	public Long getTotalServicio(){
		Long suma = 0L;
		if(listadoEstablecimientos != null && listadoEstablecimientos.size() >0){
			for(EstablecimientoVO e : listadoEstablecimientos){
				//suma+=e.gettS21()+e.gettS22()+e.gettS29();
			}
		}
		return suma;
	}
	
	public Long getTotalMunicipal(){
		Long suma = 0L;
		if(listadoEstablecimientos != null && listadoEstablecimientos.size() >0){
			for(EstablecimientoVO e : listadoEstablecimientos){
				//suma+=e.gettS24();
			}
		}
		return suma;
	}

	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		System.out.println("createResultData usuario-->"+getSessionBean().getUsername());
		parameters.put("usuario", getSessionBean().getUsername());
		return parameters;
	}

	@Override
	public String iniciarProceso() {
		String success = "divapProcesoReliqProgramas";
		Long procId = iniciarProceso(BusinessProcess.RELIQUIDACION);
		System.out.println("procId-->"+procId);
		if(procId == null){
			 success = null;
		}else{
			TaskVO task = getUserTasksByProcessId(procId, getSessionBean().getUsername());
			if(task != null){
				TaskDataVO taskDataVO = getTaskData(task.getId());
				if(taskDataVO != null){
					System.out.println("taskDataVO recuperada="+taskDataVO);
					setOnSession("taskDataSeleccionada", taskDataVO);
				}
			}
		}
		return success;
	}
	
	public void buscarReliquidacion(){
		
	}
	
	public List<ProgramaVO> getListadoProgramasUsuario() {
		return listadoProgramasUsuario;
	}

	public void setListadoProgramasUsuario(List<ProgramaVO> listadoProgramasUsuario) {
		this.listadoProgramasUsuario = listadoProgramasUsuario;
	}

	public ProgramaVO getProgramaSeleccionado() {
		return programaSeleccionado;
	}

	public void setProgramaSeleccionado(ProgramaVO programaSeleccionado) {
		this.programaSeleccionado = programaSeleccionado;
	}

	public List<ComponentesVO> getListadoComponentes() {
		return listadoComponentes;
	}

	public void setListadoComponentes(List<ComponentesVO> listadoComponentes) {
		this.listadoComponentes = listadoComponentes;
	}

	public List<ServiciosVO> getServicios() {
		if(servicios == null){
			servicios = serviciosService.getAllServiciosVO();
		}
		return servicios;
	}

	public void setServicios(List<ServiciosVO> servicios) {
		this.servicios = servicios;
	}

	public Integer getIdReliquidacion() {
		return idReliquidacion;
	}

	public void setIdReliquidacion(Integer idReliquidacion) {
		this.idReliquidacion = idReliquidacion;
	}

	public Integer getIdProgramaAno() {
		return idProgramaAno;
	}

	public void setIdProgramaAno(Integer idProgramaAno) {
		this.idProgramaAno = idProgramaAno;
	}

	public ProgramaVO getPrograma() {
		return programa;
	}

	public void setPrograma(ProgramaVO programa) {
		this.programa = programa;
	}

	public String getComponenteSeleccionado() {
		return componenteSeleccionado;
	}

	public void setComponenteSeleccionado(String componenteSeleccionado) {
		this.componenteSeleccionado = componenteSeleccionado;
	}

	public String getServicioSeleccionado() {
		return servicioSeleccionado;
	}

	public void setServicioSeleccionado(String servicioSeleccionado) {
		this.servicioSeleccionado = servicioSeleccionado;
	}
	
}
