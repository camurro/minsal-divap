package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import minsal.divap.enums.BusinessProcess;
import minsal.divap.service.ProgramasService;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.TaskDataVO;
import minsal.divap.vo.TaskVO;

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
import cl.redhat.bandejaTareas.util.JSONHelper;
import cl.redhat.bandejaTareas.util.MatchViewTask;

@Named ("procesoReliquidacionController") 
@ViewScoped
public class ProcesoReliquidacionController extends AbstractTaskMBean implements
				Serializable {
	
	private static final long serialVersionUID = 8979055329731411696L;
	@Inject
	private transient Logger log;
	@Inject 
	FacesContext facesContext;
	
	@EJB
	ProgramasService programaService;
	
	private List<ProgramaVO> listadoProgramasUsuario;
	
	private ProgramaVO programaSeleccionado;
	
	
	private List<ProgramasPojo> listadoProgramas;
	
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
	
	private List<EstablecimientoPojo> listadoEstablecimientos;
	
	public List<EstablecimientoPojo> getListadoEstablecimientos() {
		return listadoEstablecimientos;
	}
	
	public void setListadoEstablecimientos( List<EstablecimientoPojo> listadoEstablecimientos ) {
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
		for(EstablecimientoPojo e : listadoEstablecimientos){
			suma+=e.gettS21();
		}
		return suma;
	}

	public void setTotalS21( Long totalS21 ) {
		this.totalS21 = totalS21;
	}

	public Long getTotalS22() {
		Long suma = 0L;
		for(EstablecimientoPojo e : listadoEstablecimientos){
			suma+=e.gettS22();
		}
		return suma;
	}

	public void setTotalS22( Long totalS22 ) {
		this.totalS22 = totalS22;
	}

	public Long getTotalS24() {
		Long suma = 0L;
		for(EstablecimientoPojo e : listadoEstablecimientos){
			suma+=e.gettS24();
		}
		return suma;
	}

	public void setTotalS24( Long totalS24 ) {
		this.totalS24 = totalS24;
	}

	public Long getTotalS29() {
		Long suma = 0L;
		for(EstablecimientoPojo e : listadoEstablecimientos){
			suma+=e.gettS29();
		}
		return suma;
	}

	public void setTotalS29( Long totalS29 ) {
		this.totalS29 = totalS29;
	}

	@PostConstruct
	public void init() {
		log.info("ProcesoReliquidacionController tocado.");
		if (!getSessionBean().isLogged()) {
			log.warn("No hay usuario almacenado en sesion, se redirecciona a pantalla de login");
			try {
				facesContext.getExternalContext().redirect("login.jsf");
			} catch (IOException e) {
				log.error("Error tratando de redireccionar a login por falta de usuario en sesion.", e);
			}
		}else{
			
			cargaProgramasUsuario(getSessionBean().getUsername());
		}
	
		
		
		ProgramasPojo p;
		listadoProgramas = new ArrayList<ProgramasPojo>();
		Random rnd = new Random();
		
		p = new ProgramasPojo();
		p.setCodServicio("00001");
		p.setGloServicio("Talcahuano");
		p.setS21((long) rnd.nextInt(9999999));
		p.setS22((long) rnd.nextInt(9999999));
		p.setS24((long) rnd.nextInt(9999999));
		p.setS29((long) rnd.nextInt(9999999));
		p.setAvance(1f);
		p.setEstado("green");
		
		listadoProgramas.add(p);
		
		p = new ProgramasPojo();
		p.setCodServicio("00002");
		p.setGloServicio("Concepci��n");
		p.setS21((long) rnd.nextInt(9999999));
		p.setS22((long) rnd.nextInt(9999999));
		p.setS24((long) rnd.nextInt(9999999));
		p.setS29((long) rnd.nextInt(9999999));
		p.setAvance(0.4f);
		p.setEstado("yellow");
		
		listadoProgramas.add(p);
		
		p = new ProgramasPojo();
		p.setCodServicio("00003");
		p.setGloServicio("Metropolitano Sur");
		p.setS21(0L);
		p.setS22(0L);
		p.setS24(0L);
		p.setS29(0L);
		p.setAvance(0);
		p.setEstado("red");
		
		listadoProgramas.add(p);
		
		p = new ProgramasPojo();
		p.setCodServicio("00004");
		p.setGloServicio("Bio Bio");
		p.setS21((long) rnd.nextInt(9999999));
		p.setS22((long) rnd.nextInt(9999999));
		p.setS24((long) rnd.nextInt(9999999));
		p.setS29((long) rnd.nextInt(9999999));
		p.setAvance(1f);
		p.setEstado("green");
		
		listadoProgramas.add(p);
		
		p = new ProgramasPojo();
		p.setCodServicio("00005");
		p.setGloServicio("Iquique");
		p.setS21((long) rnd.nextInt(9999999));
		p.setS22((long) rnd.nextInt(9999999));
		p.setS24((long) rnd.nextInt(9999999));
		p.setS29((long) rnd.nextInt(9999999));
		p.setAvance(1f);
		p.setEstado("green");
		
		listadoProgramas.add(p);
		
		listadoEnvioServicios = new ArrayList<EnvioServiciosPojo>();
		EnvioServiciosPojo e;
		
		e = new EnvioServiciosPojo();
		e.setServicioSalud("Metropolitano Oriente");
		e.setEnviado("15 Abril 2014 16:39");
		listadoEnvioServicios.add(e);
		
		e = new EnvioServiciosPojo();
		e.setServicioSalud("Talcahuano");
		e.setEnviado("15 Abril 2014 16:39");
		listadoEnvioServicios.add(e);
		
		e = new EnvioServiciosPojo();
		e.setServicioSalud("Bio Bio");
		e.setEnviado("15 Abril 2014 16:39");
		listadoEnvioServicios.add(e);
		
		e = new EnvioServiciosPojo();
		e.setServicioSalud("Iquique");
		e.setEnviado("15 Abril 2014 16:39");
		listadoEnvioServicios.add(e);
		
		e = new EnvioServiciosPojo();
		e.setServicioSalud("Alto Hospicio");
		e.setEnviado("15 Abril 2014 16:39");
		listadoEnvioServicios.add(e);
		
		// LISTADO ESTABLECIMIENTOS
		listadoEstablecimientos = new ArrayList<EstablecimientoPojo>();
		listadoComunas = new ArrayList<ComunaPojo>();
		EstablecimientoPojo est;
		ComunaPojo com;
		
		est = new EstablecimientoPojo();
		est.setNombreOficial("Centro Comunitario de Salud Familiar Cerro Esmeralda");
		com = new ComunaPojo();
		com.setNombreComuna("Iquique");
		est.setpS21((long) rnd.nextInt(99999));
		est.setpS22((long) rnd.nextInt(99999));
		est.setpS24((long) rnd.nextInt(99999));
		est.setpS29((long) rnd.nextInt(99999));
		est.setqS21((long) rnd.nextInt(999));
		est.setqS22((long) rnd.nextInt(999));
		est.setqS24((long) rnd.nextInt(999));
		est.setqS29((long) rnd.nextInt(999));
		est.setComuna(com);
		
		listadoEstablecimientos.add(est);
		
		est = new EstablecimientoPojo();
		est.setNombreOficial("Centro Comunitario de Salud Familiar El Boro");
		com = new ComunaPojo();
		com.setNombreComuna("Alto Hospicio");
		est.setpS21((long) rnd.nextInt(99999));
		est.setpS22((long) rnd.nextInt(99999));
		est.setpS24((long) rnd.nextInt(99999));
		est.setpS29((long) rnd.nextInt(99999));
		est.setqS21((long) rnd.nextInt(999));
		est.setqS22((long) rnd.nextInt(999));
		est.setqS24((long) rnd.nextInt(999));
		est.setqS29((long) rnd.nextInt(999));
		est.setComuna(com);
		
		listadoEstablecimientos.add(est);
		
		est = new EstablecimientoPojo();
		est.setNombreOficial("Clínica Dental Móvil Simple. Pat. RW6740 (Iquique)");
		com = new ComunaPojo();
		com.setNombreComuna("Pozo Almonte");
		est.setpS21((long) rnd.nextInt(99999));
		est.setpS22((long) rnd.nextInt(99999));
		est.setpS24((long) rnd.nextInt(99999));
		est.setpS29((long) rnd.nextInt(99999));
		est.setqS21((long) rnd.nextInt(999));
		est.setqS22((long) rnd.nextInt(999));
		est.setqS24((long) rnd.nextInt(999));
		est.setqS29((long) rnd.nextInt(999));
		est.setComuna(com);
		
		listadoEstablecimientos.add(est);
		
		est = new EstablecimientoPojo();
		est.setNombreOficial("COSAM Dr. Jorge Seguel Cáceres");
		com = new ComunaPojo();
		com.setNombreComuna("Macul");
		est.setpS21((long) rnd.nextInt(99999));
		est.setpS22((long) rnd.nextInt(99999));
		est.setpS24((long) rnd.nextInt(99999));
		est.setpS29((long) rnd.nextInt(99999));
		est.setqS21((long) rnd.nextInt(999));
		est.setqS22((long) rnd.nextInt(999));
		est.setqS24((long) rnd.nextInt(999));
		est.setqS29((long) rnd.nextInt(999));
		est.setComuna(com);
		
		listadoEstablecimientos.add(est);
		
		est = new EstablecimientoPojo();
		est.setNombreOficial("COSAM Salvador Allende");
		com = new ComunaPojo();
		com.setNombreComuna("Camiña");
		est.setpS21((long) rnd.nextInt(99999));
		est.setpS22((long) rnd.nextInt(99999));
		est.setpS24((long) rnd.nextInt(99999));
		est.setpS29((long) rnd.nextInt(99999));
		est.setqS21((long) rnd.nextInt(999));
		est.setqS22((long) rnd.nextInt(999));
		est.setqS24((long) rnd.nextInt(999));
		est.setqS29((long) rnd.nextInt(999));
		est.setComuna(com);
		
		listadoEstablecimientos.add(est);
		
		com = new ComunaPojo();
		com.setNombreComuna("Iquique");
		
		listadoComunas.add(com);
		
		com = new ComunaPojo();
		com.setNombreComuna("Alto Hospicio");
		
		listadoComunas.add(com);
		
		com = new ComunaPojo();
		com.setNombreComuna("Pozo Almonte");
		
		listadoComunas.add(com);
		
		com = new ComunaPojo();
		com.setNombreComuna("Camiña");
		
		listadoComunas.add(com);
		
		com = new ComunaPojo();
		com.setNombreComuna("Colchane");
		
		listadoComunas.add(com);
		
		listadoValorHistorico = new ArrayList<ValorHistoricoPojo>();
		ValorHistoricoPojo valorHistorico = new ValorHistoricoPojo();
		valorHistorico.setComuna("Iquique");//alto hospicio, pozo almonte, macul, cami���a
		valorHistorico.setServicioSalud("Metropolitano Oriente");
		valorHistorico.setInflactorS21((double) (1.3));
		valorHistorico.setValorHistoricoS21((long) rnd.nextInt(99999));
		valorHistorico.setInflactorS22((double) (1.35));
		valorHistorico.setValorHistoricoS22((long) rnd.nextInt(99999));
		valorHistorico.setInflactorS24((double) (1.4));
		valorHistorico.setValorHistoricoS24((long) rnd.nextInt(99999));
		valorHistorico.setInflactorS29((double) (1.45));
		valorHistorico.setValorHistoricoS29((long) rnd.nextInt(99999));
		listadoValorHistorico.add(valorHistorico);
		
		valorHistorico = new ValorHistoricoPojo();
		valorHistorico.setComuna("Alto Hospicio");//alto hospicio, pozo almonte, macul, cami���a
		valorHistorico.setServicioSalud("Concepcion");
		valorHistorico.setInflactorS21((double) (1.3));
		valorHistorico.setValorHistoricoS21((long) rnd.nextInt(99999));
		valorHistorico.setInflactorS22((double) (1.35));
		valorHistorico.setValorHistoricoS22((long) rnd.nextInt(99999));
		valorHistorico.setInflactorS24((double) (1.4));
		valorHistorico.setValorHistoricoS24((long) rnd.nextInt(99999));
		valorHistorico.setInflactorS29((double) (1.45));
		valorHistorico.setValorHistoricoS29((long) rnd.nextInt(99999));
		listadoValorHistorico.add(valorHistorico);

		valorHistorico = new ValorHistoricoPojo();
		valorHistorico.setComuna("Pozo Almonte");//alto hospicio, pozo almonte, macul, cami���a
		valorHistorico.setServicioSalud("Talcahuano");
		valorHistorico.setInflactorS21((double) (1.3));
		valorHistorico.setValorHistoricoS21((long) rnd.nextInt(99999));
		valorHistorico.setInflactorS22((double) (1.35));
		valorHistorico.setValorHistoricoS22((long) rnd.nextInt(99999));
		valorHistorico.setInflactorS24((double) (1.4));
		valorHistorico.setValorHistoricoS24((long) rnd.nextInt(99999));
		valorHistorico.setInflactorS29((double) (1.45));
		valorHistorico.setValorHistoricoS29((long) rnd.nextInt(99999));
		listadoValorHistorico.add(valorHistorico);

		valorHistorico = new ValorHistoricoPojo();
		valorHistorico.setComuna("Macul");//alto hospicio, pozo almonte, macul, cami���a
		valorHistorico.setServicioSalud("Iquique");
		valorHistorico.setInflactorS21((double) (1.3));
		valorHistorico.setValorHistoricoS21((long) rnd.nextInt(99999));
		valorHistorico.setInflactorS22((double) (1.35));
		valorHistorico.setValorHistoricoS22((long) rnd.nextInt(99999));
		valorHistorico.setInflactorS24((double) (1.4));
		valorHistorico.setValorHistoricoS24((long) rnd.nextInt(99999));
		valorHistorico.setInflactorS29((double) (1.45));
		valorHistorico.setValorHistoricoS29((long) rnd.nextInt(99999));
		listadoValorHistorico.add(valorHistorico);

		valorHistorico = new ValorHistoricoPojo();
		valorHistorico.setComuna("Camiña");//alto hospicio, pozo almonte, macul, cami���a
		valorHistorico.setServicioSalud("Bio Bio");
		valorHistorico.setInflactorS21((double) (1.3));
		valorHistorico.setValorHistoricoS21((long) rnd.nextInt(99999));
		valorHistorico.setInflactorS22((double) (1.35));
		valorHistorico.setValorHistoricoS22((long) rnd.nextInt(99999));
		valorHistorico.setInflactorS24((double) (1.4));
		valorHistorico.setValorHistoricoS24((long) rnd.nextInt(99999));
		valorHistorico.setInflactorS29((double) (1.45));
		valorHistorico.setValorHistoricoS29((long) rnd.nextInt(99999));
		listadoValorHistorico.add(valorHistorico);
	}
	
	private void cargaProgramasUsuario(String username) {
		listadoProgramasUsuario = programaService.getProgramasByUser(username);
				
	}

	public Long getTotalServicio(){
		Long suma = 0L;
		for(EstablecimientoPojo e : listadoEstablecimientos){
			suma+=e.gettS21()+e.gettS22()+e.gettS29();
		}
		return suma;
	}
	
	public Long getTotalMunicipal(){
		Long suma = 0L;
		for(EstablecimientoPojo e : listadoEstablecimientos){
			suma+=e.gettS24();
		}
		return suma;
	}

	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		System.out.println("createResultData usuario-->"+getSessionBean().getUsername());
		parameters.put("usuario", getSessionBean().getUsername());
		ProgramaVO programaSel = getFromSession("programaSeleccionado", ProgramaVO.class);		
		parameters.put("programa_", JSONHelper.toJSON(programaSel));
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
	
	public String comenzar(){
		if(programaSeleccionado != null){
			System.out.println(programaSeleccionado.getNombre());
			setOnSession("programaSeleccionado", programaSeleccionado);
			//super.setTarget(MatchViewTask.matchView(programaSeleccionado.getTipo_programa()));
			return super.enviar();
			
		}
		return null;
	}

	public List<ProgramaVO> getListadoProgramasUsuario() {
		return listadoProgramasUsuario;
	}

	public void setListadoProgramasUsuario(List<ProgramaVO> listadoProgramasUsuario) {
		this.listadoProgramasUsuario = listadoProgramasUsuario;
	}

	public ProgramaVO getProgramaSeleccionado() {
		if(this.programaSeleccionado == null){
			this.programaSeleccionado = new ProgramaVO();
		}
		return programaSeleccionado;
	}

	public void setProgramaSeleccionado(ProgramaVO programaSeleccionado) {
		this.programaSeleccionado = programaSeleccionado;
	}

	
}
