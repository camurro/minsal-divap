package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import minsal.divap.enums.EstadosProgramas;
import minsal.divap.service.ProgramasService;
import minsal.divap.vo.ProgramaVO;

import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import minsal.divap.service.EstimacionFlujoCajaService;
import minsal.divap.service.ProgramasService;
import minsal.divap.service.ServicioSaludService;
import minsal.divap.util.Util;
import minsal.divap.vo.CajaGlobalVO;
import minsal.divap.vo.CajaVO;
import minsal.divap.vo.ColumnaVO;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ComunaVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ServiciosVO;

import org.apache.log4j.Logger;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

import cl.minsal.divap.pojo.ComponentePojo;
import cl.minsal.divap.pojo.MonitoreoPojo;
import cl.minsal.divap.pojo.ProcesosProgramasPojo;
import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named("procesoEstimacionFlujoCajaConsolidadorRevisarConsolidacionController")
@ViewScoped
public class ProcesoEstimacionFlujoCajaConsolidadorRevisarConsolidacionController extends
		AbstractTaskMBean implements Serializable {
	private static final long serialVersionUID = 8979055329731411696L;
	@Inject
	private transient Logger log;
	FacesContext facesContext;


	/*
	 * Transversal
	 */
	
	private Map<String, Integer> programas21 = new HashMap<String, Integer>();
	private Map<String, Integer> servicios21 = new HashMap<String, Integer>();
	private Map<String, Integer> comunas21 = new HashMap<String, Integer>();
	
	private Map<String, Integer> programas22 = new HashMap<String, Integer>();
	private Map<String, Integer> servicios22 = new HashMap<String, Integer>();
	private Map<String, Integer> comunas22 = new HashMap<String, Integer>();
	
	private Map<String, Integer> programas24 = new HashMap<String, Integer>();
	private Map<String, Integer> servicios24 = new HashMap<String, Integer>();
	private Map<String, Integer> comunas24 = new HashMap<String, Integer>();
	
	private Map<String, Integer> programas29 = new HashMap<String, Integer>();
	private Map<String, Integer> servicios29 = new HashMap<String, Integer>();
	private Map<String, Integer> comunas29 = new HashMap<String, Integer>();
	
	private Map<String, Integer> programasPerCapita = new HashMap<String, Integer>();
	private Map<String, Integer> serviciosPerCapita = new HashMap<String, Integer>();
	private Map<String, Integer> comunasPerCapita = new HashMap<String, Integer>();
	
	private int mes;
	private String mesActual;
	private int anoActual;
	List<ColumnaVO> columns;
	List<ColumnaVO> columnsInicial;
	private List<ProgramaVO> programas;
	/*
	 ********************************** FIN VARIABLES
	 */
	
	
	//ID Programa
	private Integer idLineaProgramatica;
	
	public Integer getIdLineaProgramatica() {
		return idLineaProgramatica;
	}

	public void setIdLineaProgramatica(Integer idLineaProgramatica) {
		this.idLineaProgramatica = idLineaProgramatica;
	}
	@EJB
	private ProgramasService programaService;
	
	@EJB
	private ServicioSaludService servicioSaludService;

	/*
	 *SUBTITULO 21 
	 */
	private Integer valorComboProgramaSubtitulo21;
	private Integer valorComboServicioSubtitulo21;
	private Integer valorComboComunaSubtitulo21;
	CajaGlobalVO CajaGlobalVOSubtitulo21Original;
	CajaGlobalVO CajaGlobalVOSubtitulo21;
	List<CajaVO> listadoMonitoreoSubtitulo21;
	

	/*
	 *SUBTITULO 22 
	 */
	private Integer valorComboProgramaSubtitulo22;
	private Integer valorComboServicioSubtitulo22;
	private Integer valorComboComunaSubtitulo22;
	CajaGlobalVO CajaGlobalVOSubtitulo22Original;
	CajaGlobalVO CajaGlobalVOSubtitulo22;
	List<CajaVO> listadoMonitoreoSubtitulo22;
	
	/*
	 *SUBTITULO 24 
	 */
	private Integer valorComboProgramaSubtitulo24;
	private Integer valorComboServicioSubtitulo24;
	private Integer valorComboComunaSubtitulo24;
	CajaGlobalVO CajaGlobalVOSubtitulo24Original;
	CajaGlobalVO CajaGlobalVOSubtitulo24;
	List<CajaVO> listadoMonitoreoSubtitulo24;
	
	
	/*
	 *SUBTITULO 29 
	 */
	private Integer valorComboProgramaSubtitulo29;
	private Integer valorComboServicioSubtitulo29;
	private Integer valorComboComunaSubtitulo29;
	
	CajaGlobalVO CajaGlobalVOSubtitulo29Original;
	CajaGlobalVO CajaGlobalVOSubtitulo29;
	List<CajaVO> listadoMonitoreoSubtitulo29;
	
	
	/*
	 *SUBTITULO PerCapita 
	 */
	private Integer valorComboProgramaSubtituloPerCapita;
	private Integer valorComboServicioSubtituloPerCapita;
	private Integer valorComboComunaSubtituloPerCapita;
	CajaGlobalVO CajaGlobalVOSubtituloPerCapitaOriginal;
	CajaGlobalVO CajaGlobalVOSubtituloPerCapita;
	List<CajaVO> listadoMonitoreoSubtituloPerCapita;
	

	//TODO: [ASAAVEDRA] Verificar cuando los programas estan iniciados.
	
	//Obtiene la lista de programas del usuario
	public List<ProcesosProgramasPojo> getListadoProgramasServicio() {
		List<ProcesosProgramasPojo> listadoProgramasServicio = new ArrayList<ProcesosProgramasPojo>();
		List<ProgramaVO> programas = programaService
				.getProgramasByUser(getLoggedUsername());
		for (ProgramaVO programaVO : programas) {
			ProcesosProgramasPojo p2 = new ProcesosProgramasPojo();
			p2.setPrograma(programaVO.getNombre());
			p2.setDescripcion("descripcion");
			p2.setId(programaVO.getId());
			listadoProgramasServicio.add(p2);
		}
		return listadoProgramasServicio;
	}

	// Continua el proceso con el programa seleccionado.
	public String continuarProceso() {

		//setIdLineaProgramatica(id);
		setTarget("bandejaTareas");
		
		//TODO: [ASAAVEDRA] Guardar el estado del programa como revisado.
		programaService.guardarEstadoFlujoCaja(EstadosProgramas.REVISADO.getId(), idLineaProgramatica);
		return super.enviar();
	}
	
	@Override
	public String toString() {
		return "Proceso Estimacion Flujo Caja Seleccionar Linea Controller";
	}

	@PostConstruct
	public void init() {
		log.info("Proceso Estimacion Flujo Caja Seleccionar Linea [Ingreso].");
		if (!getSessionBean().isLogged()) {
			log.warn("No hay usuario almacenado en sesion, se redirecciona a pantalla de login");
			try {
				facesContext.getExternalContext().redirect("login.jsf");
			} catch (IOException e) {
				log.error(
						"Error tratando de redireccionar a login por falta de usuario en sesion.",
						e);
			}
		} else {

		}

		idLineaProgramatica = 1;
		cargarProgramas21();
		cargarProgramas22();
		cargarProgramas24();
		cargarProgramas29();
		cargarProgramasPerCapita();
		cargarListas();
		cargarProgramas();
		setMes(11);
		setAnoActual(2014);
		crearColumnasDinamicas();
	
	}

	public void cargarComunas21()
	{
		ProgramaVO programaVO = programaService.getProgramaAno(valorComboProgramaSubtitulo21);
		
		for (ServiciosVO serviciosVO : programaVO.getServicios()) {
			if (serviciosVO.getId_servicio() == valorComboServicioSubtitulo21)
			{
				for (ComunaVO comunaVO : serviciosVO.getComuna()) {
					comunas21.put(comunaVO.getNombre(),comunaVO.getIdComuna());
				}
			
			}
		}
		
		Map<String, Integer> mapOrdenado = new TreeMap<String, Integer>(
				comunas21);
		comunas21 = mapOrdenado;
	}
	
	public void cargarComunas22()
	{
		ProgramaVO programaVO = programaService.getProgramaAno(valorComboProgramaSubtitulo22);
		
		for (ServiciosVO serviciosVO : programaVO.getServicios()) {
			if (serviciosVO.getId_servicio() == valorComboServicioSubtitulo22)
			{
				for (ComunaVO comunaVO : serviciosVO.getComuna()) {
					comunas22.put(comunaVO.getNombre(),comunaVO.getIdComuna());
				}
			
			}
		}
		
		Map<String, Integer> mapOrdenado = new TreeMap<String, Integer>(
				comunas22);
		comunas22 = mapOrdenado;
	}
	
	public void cargarComunas24()
	{
		ProgramaVO programaVO = programaService.getProgramaAno(valorComboProgramaSubtitulo24);
		
		for (ServiciosVO serviciosVO : programaVO.getServicios()) {
			if (serviciosVO.getId_servicio() == valorComboServicioSubtitulo24)
			{
				for (ComunaVO comunaVO : serviciosVO.getComuna()) {
					comunas24.put(comunaVO.getNombre(),comunaVO.getIdComuna());
				}
			
			}
		}
		
		Map<String, Integer> mapOrdenado = new TreeMap<String, Integer>(
				comunas24);
		comunas24 = mapOrdenado;
	}
	
	public void cargarComunas29()
	{
		ProgramaVO programaVO = programaService.getProgramaAno(valorComboProgramaSubtitulo29);
		
		for (ServiciosVO serviciosVO : programaVO.getServicios()) {
			if (serviciosVO.getId_servicio() == valorComboServicioSubtitulo29)
			{
				for (ComunaVO comunaVO : serviciosVO.getComuna()) {
					comunas29.put(comunaVO.getNombre(),comunaVO.getIdComuna());
				}
			
			}
		}
		
		Map<String, Integer> mapOrdenado = new TreeMap<String, Integer>(
				comunas29);
		comunas29 = mapOrdenado;
	}
	
	public void cargarComunasPerCapita()
	{
		ProgramaVO programaVO = programaService.getProgramaAno(valorComboProgramaSubtituloPerCapita);
		
		for (ServiciosVO serviciosVO : programaVO.getServicios()) {
			if (serviciosVO.getId_servicio() == valorComboServicioSubtituloPerCapita)
			{
				for (ComunaVO comunaVO : serviciosVO.getComuna()) {
					comunasPerCapita.put(comunaVO.getNombre(),comunaVO.getIdComuna());
				}
			
			}
		}
		
		Map<String, Integer> mapOrdenado = new TreeMap<String, Integer>(
				comunasPerCapita);
		comunasPerCapita = mapOrdenado;
	}
	
	public void cargarServicios21()
	{
		setComunas21(new HashMap<String, Integer>());
		ProgramaVO programaVO = programaService.getProgramaAno(valorComboProgramaSubtitulo21);
		for (ServiciosVO serviciosVO : programaVO.getServicios()) {
			servicios21.put(serviciosVO.getNombre_servicio(), serviciosVO.getId_servicio());
		}
		
		Map<String, Integer> mapOrdenado = new TreeMap<String, Integer>(
				servicios21);
		servicios21 = mapOrdenado;
	}
	
	public void cargarServicios22()
	{
		setComunas22(new HashMap<String, Integer>());
		ProgramaVO programaVO = programaService.getProgramaAno(valorComboProgramaSubtitulo22);
		for (ServiciosVO serviciosVO : programaVO.getServicios()) {
			servicios22.put(serviciosVO.getNombre_servicio(), serviciosVO.getId_servicio());
		}
		
		Map<String, Integer> mapOrdenado = new TreeMap<String, Integer>(
				servicios22);
		servicios22 = mapOrdenado;
	}
	public void cargarServicios24()
	{
		setComunas24(new HashMap<String, Integer>());
		ProgramaVO programaVO = programaService.getProgramaAno(valorComboProgramaSubtitulo24);
		for (ServiciosVO serviciosVO : programaVO.getServicios()) {
			servicios24.put(serviciosVO.getNombre_servicio(), serviciosVO.getId_servicio());
		}
		
		Map<String, Integer> mapOrdenado = new TreeMap<String, Integer>(
				servicios24);
		servicios24 = mapOrdenado;
	}
	public void cargarServicios29()
	{
		setComunas29(new HashMap<String, Integer>());
		ProgramaVO programaVO = programaService.getProgramaAno(valorComboProgramaSubtitulo29);
		for (ServiciosVO serviciosVO : programaVO.getServicios()) {
			servicios29.put(serviciosVO.getNombre_servicio(), serviciosVO.getId_servicio());
		}
		
		Map<String, Integer> mapOrdenado = new TreeMap<String, Integer>(
				servicios29);
		servicios29 = mapOrdenado;
	}
	public void cargarServiciosPerCapita()
	{
		setComunasPerCapita(new HashMap<String, Integer>());
		ProgramaVO programaVO = programaService.getProgramaAno(valorComboProgramaSubtituloPerCapita);
		for (ServiciosVO serviciosVO : programaVO.getServicios()) {
			serviciosPerCapita.put(serviciosVO.getNombre_servicio(), serviciosVO.getId_servicio());
		}
		
		Map<String, Integer> mapOrdenado = new TreeMap<String, Integer>(
				serviciosPerCapita);
		serviciosPerCapita = mapOrdenado;
	}
	
	private void cargarProgramas() {

	
		programas = programaService.getProgramasByUserAno(getLoggedUsername(), Util.obtenerAno(new Date()));
	// TODO Auto-generated method stub
		
	}
	
	private void cargarProgramas21() {

		servicios21 = new HashMap<String, Integer>();
		setComunas21(new HashMap<String, Integer>());
		List<ProgramaVO> programaVO = programaService.getProgramasByUserAno(getLoggedUsername(), Util.obtenerAno(new Date()));
		for (ProgramaVO programaVO2 : programaVO) {
			programas21.put(programaVO2.getNombre(), programaVO2.getIdProgramaAno());
		}
		
		Map<String, Integer> mapOrdenado = new TreeMap<String, Integer>(
				programas21);
		programas21 = mapOrdenado;
	// TODO Auto-generated method stub
		
	}
	private void cargarProgramas22() {

		servicios22 = new HashMap<String, Integer>();
		setComunas22(new HashMap<String, Integer>());
		List<ProgramaVO> programaVO = programaService.getProgramasByUserAno(getLoggedUsername(), Util.obtenerAno(new Date()));
		for (ProgramaVO programaVO2 : programaVO) {
			programas22.put(programaVO2.getNombre(), programaVO2.getIdProgramaAno());
		}
		
		Map<String, Integer> mapOrdenado = new TreeMap<String, Integer>(
				programas22);
		programas22 = mapOrdenado;
	// TODO Auto-generated method stub
		
	}
	private void cargarProgramas24() {

		servicios24 = new HashMap<String, Integer>();
		setComunas24(new HashMap<String, Integer>());
		List<ProgramaVO> programaVO = programaService.getProgramasByUserAno(getLoggedUsername(), Util.obtenerAno(new Date()));
		for (ProgramaVO programaVO2 : programaVO) {
			programas24.put(programaVO2.getNombre(), programaVO2.getIdProgramaAno());
		}
		
		Map<String, Integer> mapOrdenado = new TreeMap<String, Integer>(
				programas24);
		programas24 = mapOrdenado;
	// TODO Auto-generated method stub
		
	}
	private void cargarProgramas29() {

		servicios29 = new HashMap<String, Integer>();
		setComunas29(new HashMap<String, Integer>());
		List<ProgramaVO> programaVO = programaService.getProgramasByUserAno(getLoggedUsername(), Util.obtenerAno(new Date()));
		for (ProgramaVO programaVO2 : programaVO) {
			programas29.put(programaVO2.getNombre(), programaVO2.getIdProgramaAno());
		}
		
		Map<String, Integer> mapOrdenado = new TreeMap<String, Integer>(
				programas29);
		programas29 = mapOrdenado;
	// TODO Auto-generated method stub
		
	}
	private void cargarProgramasPerCapita() {

		serviciosPerCapita = new HashMap<String, Integer>();
		setComunasPerCapita(new HashMap<String, Integer>());
		List<ProgramaVO> programaVO = programaService.getProgramasByUserAno(getLoggedUsername(), Util.obtenerAno(new Date()));
		for (ProgramaVO programaVO2 : programaVO) {
			programasPerCapita.put(programaVO2.getNombre(), programaVO2.getIdProgramaAno());
		}
		
		Map<String, Integer> mapOrdenado = new TreeMap<String, Integer>(
				programasPerCapita);
		programasPerCapita = mapOrdenado;
	// TODO Auto-generated method stub
		
	}

	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("_flujo", 1);
		return parameters;
	}

	@Override
	public String iniciarProceso() {
		return null;
	}

	
	/*
	 ********************************************************************************************* SUBTITULO 21
	 */
	/*
	 * Filtra la lista segun los parametros de entrada, en este caso el servicio.
	 */
	public void filtrarSubtitulo21()
	{
		
		//String nuevo = valor;
		List<CajaVO> lst = CajaGlobalVOSubtitulo21Original.getCaja();
		List<CajaVO> lstAgregar = new ArrayList<CajaVO>();
		for (CajaVO CajaVO : lst) {
			if (CajaVO.getIdServicio()==(valorComboProgramaSubtitulo21))
			{
				lstAgregar.add(CajaVO);
			}
		}
		//listadoMonitoreoSubtitulo21 = lstAgregar;
		
		CajaGlobalVOSubtitulo21.setCaja(lstAgregar);
		
	}
	
	 /*
	  * Modificaci�n de la celda
	  */
	 public void onCellEditSubtitulo21(CellEditEvent event) {
		 
		 UIColumn col= (UIColumn) event.getColumn();
		 DataTable o=(DataTable) event.getSource();
		
		 CajaVO info=(CajaVO)o.getRowData();
		 
		 
	        Object oldValue = event.getOldValue();
	        Object newValue = event.getNewValue();
	         
	        if(newValue != null && !newValue.equals(oldValue)) {
	            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
	            FacesContext.getCurrentInstance().addMessage(null, msg);
	        }
	       
	        //listadoServicios2.add(info);
	        
	        CajaVO monitore_borrar = new CajaVO();
	        

	        	for (CajaVO monitoreo_actual : listadoMonitoreoSubtitulo21) {
	        		
	        		if (info.getId() == monitoreo_actual.getId())
	        		{
	        		    monitore_borrar = monitoreo_actual;
	        		    break;
	        		}
					
				}
	        	
	        	listadoMonitoreoSubtitulo21.remove(monitore_borrar);
	        	//info.setS24Total(info.getS24Enero() + info.getS24Febrero() + info.getS24Marzo() + info.getS24Abril() + info.getS24Mayo() + info.getS24Junio() + info.getS24Julio() + info.getS24Agosto() + info.getS24Septiembre() + info.getS24Octubre() + info.getS24Noviembre()+ info.getS24Diciembre());
	        	listadoMonitoreoSubtitulo21.add(info);
	        	
	        	CajaGlobalVOSubtitulo21.setCaja(listadoMonitoreoSubtitulo21);
				        
	        	//MODIFICAR LA LISTA ORIGINAL
	        	
	        	List<CajaVO> lst_monitore_borrar =new ArrayList<CajaVO>();
	        	List<CajaVO> lst_monitore_agregar =new ArrayList<CajaVO>();
	        	for (CajaVO monitoreo_actual : listadoMonitoreoSubtitulo21) {
	        		for (CajaVO CajaVO : CajaGlobalVOSubtitulo21Original.getCaja()) {
						
					
	        		if (CajaVO.getId() == monitoreo_actual.getId())
	        		{
	        		    lst_monitore_borrar.add(CajaVO);
	        		    lst_monitore_agregar.add(monitoreo_actual);
	        		}
	        		}
					
				}
	        	
	        	for (CajaVO CajaVO : lst_monitore_borrar) {
	        		CajaGlobalVOSubtitulo21Original.getCaja().remove(CajaVO);
				}
	        	
	        	for (CajaVO CajaVO : lst_monitore_agregar) {
	        		CajaGlobalVOSubtitulo21Original.getCaja().add(CajaVO);
				}
	        	
	 
	 }
	 /*
	  * Carga los datos
	  */
	 public void CargarListaSubtitulo21()
		{
			CajaGlobalVOSubtitulo21 = new CajaGlobalVO();
			
			
			CajaVO CajaVO = new CajaVO();
			CajaVO.setAbril(4);
			CajaVO.setAgosto(9);
			CajaVO.setColor("#FFB5B5");
			CajaVO.setComuna("Macul");
			CajaVO.setConvenioMonto(10);
			CajaVO.setConvenioPorcentaje(10);
			CajaVO.setDiciembre(12);
			CajaVO.setEnero(1);
			CajaVO.setEstablecimiento("Servicio1");
			CajaVO.setFebrero(2);
			CajaVO.setId(1);
			CajaVO.setJulio(7);
			CajaVO.setJunio(6);
			CajaVO.setMarcoMonto(100);
			CajaVO.setMarzo(3);
			CajaVO.setMayo(5);
			CajaVO.setNoviembre(11);
			CajaVO.setOctubre(10);
			CajaVO.setRemesaMonto(15);
			CajaVO.setRemesaPorcentaje(10);
			CajaVO.setSeptiembre(9);
			CajaVO.setServicio("Talcahuano");
			CajaVO.setTotal(100);
			CajaVO.setTransferenciaMonto(100);
			CajaVO.setTransferenciaPorcentaje(10);
			listadoMonitoreoSubtitulo21.add(CajaVO);
			//OTRO REGISTO
			CajaVO = new CajaVO();
			CajaVO.setAbril(45);
			CajaVO.setAgosto(95);
			CajaVO.setColor("#FFB5B5");
			CajaVO.setComuna("Macul 2");
			CajaVO.setConvenioMonto(150);
			CajaVO.setConvenioPorcentaje(150);
			CajaVO.setDiciembre(152);
			CajaVO.setEnero(51);
			CajaVO.setEstablecimiento("Servicio 2");
			CajaVO.setFebrero(52);
			CajaVO.setId(15);
			CajaVO.setJulio(57);
			CajaVO.setJunio(56);
			CajaVO.setMarcoMonto(510);
			CajaVO.setMarzo(53);
			CajaVO.setMayo(55);
			CajaVO.setNoviembre(511);
			CajaVO.setOctubre(510);
			CajaVO.setRemesaMonto(515);
			CajaVO.setRemesaPorcentaje(510);
			CajaVO.setSeptiembre(59);
			CajaVO.setServicio("Iquique");
			CajaVO.setTotal(5100);
			CajaVO.setTransferenciaMonto(5100);
			CajaVO.setTransferenciaPorcentaje(510);
			
			listadoMonitoreoSubtitulo21.add(CajaVO);
			CajaGlobalVOSubtitulo21.setCaja(listadoMonitoreoSubtitulo21);
			//EL FILTRADO DEBE SER IGUAL AL ORIGINAL
			CajaGlobalVOSubtitulo21Original = new CajaGlobalVO();
			
		
			
			CajaGlobalVOSubtitulo21Original  = CajaGlobalVOSubtitulo21.clone();
			
		}
	
	 
	 /*
	  ************************************************************************* FIN SUBTITULO 21
	  */
	 
	 /*
		 ********************************************************************************************* SUBTITULO 22
		 */
		/*
		 * Filtra la lista segun los parametros de entrada, en este caso el servicio.
		 */
		public void filtrarSubtitulo22()
		{
			
			//String nuevo = valor;
			List<CajaVO> lst = CajaGlobalVOSubtitulo22Original.getCaja();
			List<CajaVO> lstAgregar = new ArrayList<CajaVO>();
			for (CajaVO CajaVO : lst) {
				if (CajaVO.getIdServicio()==(valorComboProgramaSubtitulo22))
				{
					lstAgregar.add(CajaVO);
				}
			}
			//listadoMonitoreoSubtitulo22 = lstAgregar;
			
			CajaGlobalVOSubtitulo22.setCaja(lstAgregar);
			
		}
		
		 /*
		  * Modificaci�n de la celda
		  */
		 public void onCellEditSubtitulo22(CellEditEvent event) {
			 
			 UIColumn col= (UIColumn) event.getColumn();
			 DataTable o=(DataTable) event.getSource();
			
			 CajaVO info=(CajaVO)o.getRowData();
			 
			 
		        Object oldValue = event.getOldValue();
		        Object newValue = event.getNewValue();
		         
		        if(newValue != null && !newValue.equals(oldValue)) {
		            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
		            FacesContext.getCurrentInstance().addMessage(null, msg);
		        }
		       
		        //listadoServicios2.add(info);
		        
		        CajaVO monitore_borrar = new CajaVO();
		        

		        	for (CajaVO monitoreo_actual : listadoMonitoreoSubtitulo22) {
		        		
		        		if (info.getId() == monitoreo_actual.getId())
		        		{
		        		    monitore_borrar = monitoreo_actual;
		        		    break;
		        		}
						
					}
		        	
		        	listadoMonitoreoSubtitulo22.remove(monitore_borrar);
		        	//info.setS24Total(info.getS24Enero() + info.getS24Febrero() + info.getS24Marzo() + info.getS24Abril() + info.getS24Mayo() + info.getS24Junio() + info.getS24Julio() + info.getS24Agosto() + info.getS24Septiembre() + info.getS24Octubre() + info.getS24Noviembre()+ info.getS24Diciembre());
		        	listadoMonitoreoSubtitulo22.add(info);
		        	
		        	CajaGlobalVOSubtitulo22.setCaja(listadoMonitoreoSubtitulo22);
					        
		        	//MODIFICAR LA LISTA ORIGINAL
		        	
		        	List<CajaVO> lst_monitore_borrar =new ArrayList<CajaVO>();
		        	List<CajaVO> lst_monitore_agregar =new ArrayList<CajaVO>();
		        	for (CajaVO monitoreo_actual : listadoMonitoreoSubtitulo22) {
		        		for (CajaVO CajaVO : CajaGlobalVOSubtitulo22Original.getCaja()) {
							
						
		        		if (CajaVO.getId() == monitoreo_actual.getId())
		        		{
		        		    lst_monitore_borrar.add(CajaVO);
		        		    lst_monitore_agregar.add(monitoreo_actual);
		        		}
		        		}
						
					}
		        	
		        	for (CajaVO CajaVO : lst_monitore_borrar) {
		        		CajaGlobalVOSubtitulo22Original.getCaja().remove(CajaVO);
					}
		        	
		        	for (CajaVO CajaVO : lst_monitore_agregar) {
		        		CajaGlobalVOSubtitulo22Original.getCaja().add(CajaVO);
					}
		        	
		 
		 }
		 /*
		  * Carga los datos
		  */
		 public void CargarListaSubtitulo22()
			{
				CajaGlobalVOSubtitulo22 = new CajaGlobalVO();
				
				
				CajaVO CajaVO = new CajaVO();
				CajaVO.setAbril(4);
				CajaVO.setAgosto(9);
				CajaVO.setColor("#FFB5B5");
				CajaVO.setComuna("Macul");
				CajaVO.setConvenioMonto(10);
				CajaVO.setConvenioPorcentaje(10);
				CajaVO.setDiciembre(12);
				CajaVO.setEnero(1);
				CajaVO.setEstablecimiento("Establecimiento");
				CajaVO.setFebrero(2);
				CajaVO.setId(1);
				CajaVO.setJulio(7);
				CajaVO.setJunio(6);
				CajaVO.setMarcoMonto(100);
				CajaVO.setMarzo(3);
				CajaVO.setMayo(5);
				CajaVO.setNoviembre(11);
				CajaVO.setOctubre(10);
				CajaVO.setRemesaMonto(15);
				CajaVO.setRemesaPorcentaje(10);
				CajaVO.setSeptiembre(9);
				CajaVO.setServicio("Talcahuano");
				CajaVO.setTotal(100);
				CajaVO.setTransferenciaMonto(100);
				CajaVO.setTransferenciaPorcentaje(10);
				listadoMonitoreoSubtitulo22.add(CajaVO);
				//OTRO REGISTO
				CajaVO = new CajaVO();
				CajaVO.setAbril(45);
				CajaVO.setAgosto(95);
				CajaVO.setColor("#FFB5B5");
				CajaVO.setComuna("Macul 2");
				CajaVO.setConvenioMonto(150);
				CajaVO.setConvenioPorcentaje(150);
				CajaVO.setDiciembre(152);
				CajaVO.setEnero(51);
				CajaVO.setEstablecimiento("Establecimiento 2");
				CajaVO.setFebrero(52);
				CajaVO.setId(15);
				CajaVO.setJulio(57);
				CajaVO.setJunio(56);
				CajaVO.setMarcoMonto(510);
				CajaVO.setMarzo(53);
				CajaVO.setMayo(55);
				CajaVO.setNoviembre(511);
				CajaVO.setOctubre(510);
				CajaVO.setRemesaMonto(515);
				CajaVO.setRemesaPorcentaje(510);
				CajaVO.setSeptiembre(59);
				CajaVO.setServicio("Iquique");
				CajaVO.setTotal(5100);
				CajaVO.setTransferenciaMonto(5100);
				CajaVO.setTransferenciaPorcentaje(510);
				
				listadoMonitoreoSubtitulo22.add(CajaVO);
				CajaGlobalVOSubtitulo22.setCaja(listadoMonitoreoSubtitulo22);
				//EL FILTRADO DEBE SER IGUAL AL ORIGINAL
				CajaGlobalVOSubtitulo22Original = new CajaGlobalVO();
				
			
				
				CajaGlobalVOSubtitulo22Original  = CajaGlobalVOSubtitulo22.clone();
				
			}
		
		 
		 /*
		  ************************************************************************* FIN SUBTITULO 22
		  */
		 
		 
		 /*
			 ********************************************************************************************* SUBTITULO 24
			 */
			/*
			 * Filtra la lista segun los parametros de entrada, en este caso el servicio.
			 */
			public void filtrarSubtitulo24()
			{
				
				//String nuevo = valor;
				List<CajaVO> lst = CajaGlobalVOSubtitulo24Original.getCaja();
				List<CajaVO> lstAgregar = new ArrayList<CajaVO>();
				for (CajaVO CajaVO : lst) {
					if (CajaVO.getIdServicio() == valorComboProgramaSubtitulo24)
					{
						lstAgregar.add(CajaVO);
					}
				}
				//listadoMonitoreoSubtitulo24 = lstAgregar;
				
				CajaGlobalVOSubtitulo24.setCaja(lstAgregar);
				
			}
			
			 /*
			  * Modificaci�n de la celda
			  */
			 public void onCellEditSubtitulo24(CellEditEvent event) {
				 
				 UIColumn col= (UIColumn) event.getColumn();
				 DataTable o=(DataTable) event.getSource();
				
				 CajaVO info=(CajaVO)o.getRowData();
				 
				 
			        Object oldValue = event.getOldValue();
			        Object newValue = event.getNewValue();
			         
			        if(newValue != null && !newValue.equals(oldValue)) {
			            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
			            FacesContext.getCurrentInstance().addMessage(null, msg);
			        }
			       
			        //listadoServicios2.add(info);
			        
			        CajaVO monitore_borrar = new CajaVO();
			        

			        	for (CajaVO monitoreo_actual : listadoMonitoreoSubtitulo24) {
			        		
			        		if (info.getId() == monitoreo_actual.getId())
			        		{
			        		    monitore_borrar = monitoreo_actual;
			        		    break;
			        		}
							
						}
			        	
			        	listadoMonitoreoSubtitulo24.remove(monitore_borrar);
			        	//info.setS24Total(info.getS24Enero() + info.getS24Febrero() + info.getS24Marzo() + info.getS24Abril() + info.getS24Mayo() + info.getS24Junio() + info.getS24Julio() + info.getS24Agosto() + info.getS24Septiembre() + info.getS24Octubre() + info.getS24Noviembre()+ info.getS24Diciembre());
			        	listadoMonitoreoSubtitulo24.add(info);
			        	
			        	CajaGlobalVOSubtitulo24.setCaja(listadoMonitoreoSubtitulo24);
						        
			        	//MODIFICAR LA LISTA ORIGINAL
			        	
			        	List<CajaVO> lst_monitore_borrar =new ArrayList<CajaVO>();
			        	List<CajaVO> lst_monitore_agregar =new ArrayList<CajaVO>();
			        	for (CajaVO monitoreo_actual : listadoMonitoreoSubtitulo24) {
			        		for (CajaVO CajaVO : CajaGlobalVOSubtitulo24Original.getCaja()) {
								
							
			        		if (CajaVO.getId() == monitoreo_actual.getId())
			        		{
			        		    lst_monitore_borrar.add(CajaVO);
			        		    lst_monitore_agregar.add(monitoreo_actual);
			        		}
			        		}
							
						}
			        	
			        	for (CajaVO CajaVO : lst_monitore_borrar) {
			        		CajaGlobalVOSubtitulo24Original.getCaja().remove(CajaVO);
						}
			        	
			        	for (CajaVO CajaVO : lst_monitore_agregar) {
			        		CajaGlobalVOSubtitulo24Original.getCaja().add(CajaVO);
						}
			        	
			 
			 }
			 /*
			  * Carga los datos
			  */
			 public void CargarListaSubtitulo24()
				{
					CajaGlobalVOSubtitulo24 = new CajaGlobalVO();
					
					
					CajaVO CajaVO = new CajaVO();
					CajaVO.setAbril(4);
					CajaVO.setAgosto(9);
					CajaVO.setColor("#FFB5B5");
					CajaVO.setComuna("Macul");
					CajaVO.setConvenioMonto(10);
					CajaVO.setConvenioPorcentaje(10);
					CajaVO.setDiciembre(12);
					CajaVO.setEnero(1);
					CajaVO.setEstablecimiento("Establecimiento");
					CajaVO.setFebrero(2);
					CajaVO.setId(1);
					CajaVO.setJulio(7);
					CajaVO.setJunio(6);
					CajaVO.setMarcoMonto(100);
					CajaVO.setMarzo(3);
					CajaVO.setMayo(5);
					CajaVO.setNoviembre(11);
					CajaVO.setOctubre(10);
					CajaVO.setRemesaMonto(15);
					CajaVO.setRemesaPorcentaje(10);
					CajaVO.setSeptiembre(9);
					CajaVO.setServicio("Talcahuano");
					CajaVO.setTotal(100);
					CajaVO.setTransferenciaMonto(100);
					CajaVO.setTransferenciaPorcentaje(10);
					listadoMonitoreoSubtitulo24.add(CajaVO);
					//OTRO REGISTO
					CajaVO = new CajaVO();
					CajaVO.setAbril(45);
					CajaVO.setAgosto(95);
					CajaVO.setColor("#FFB5B5");
					CajaVO.setComuna("Macul 2");
					CajaVO.setConvenioMonto(150);
					CajaVO.setConvenioPorcentaje(150);
					CajaVO.setDiciembre(152);
					CajaVO.setEnero(51);
					CajaVO.setEstablecimiento("Establecimiento 2");
					CajaVO.setFebrero(52);
					CajaVO.setId(15);
					CajaVO.setJulio(57);
					CajaVO.setJunio(56);
					CajaVO.setMarcoMonto(510);
					CajaVO.setMarzo(53);
					CajaVO.setMayo(55);
					CajaVO.setNoviembre(511);
					CajaVO.setOctubre(510);
					CajaVO.setRemesaMonto(515);
					CajaVO.setRemesaPorcentaje(510);
					CajaVO.setSeptiembre(59);
					CajaVO.setServicio("Iquique");
					CajaVO.setTotal(5100);
					CajaVO.setTransferenciaMonto(5100);
					CajaVO.setTransferenciaPorcentaje(510);
					
					listadoMonitoreoSubtitulo24.add(CajaVO);
					CajaGlobalVOSubtitulo24.setCaja(listadoMonitoreoSubtitulo24);
					//EL FILTRADO DEBE SER IGUAL AL ORIGINAL
					CajaGlobalVOSubtitulo24Original = new CajaGlobalVO();
					
				
					
					CajaGlobalVOSubtitulo24Original  = CajaGlobalVOSubtitulo24.clone();
					
				}
			
			 
			 /*
			  ************************************************************************* FIN SUBTITULO 24
			  */
	 
			 /*
				 ********************************************************************************************* SUBTITULO 29
				 */
				/*
				 * Filtra la lista segun los parametros de entrada, en este caso el servicio.
				 */
				public void filtrarSubtitulo29()
				{
					
					//String nuevo = valor;
					List<CajaVO> lst = CajaGlobalVOSubtitulo29Original.getCaja();
					List<CajaVO> lstAgregar = new ArrayList<CajaVO>();
					for (CajaVO CajaVO : lst) {
						if (CajaVO.getIdServicio()==(valorComboProgramaSubtitulo29))
						{
							lstAgregar.add(CajaVO);
						}
					}
					//listadoMonitoreoSubtitulo29 = lstAgregar;
					
					CajaGlobalVOSubtitulo29.setCaja(lstAgregar);
					
				}
				
				 /*
				  * Modificaci�n de la celda
				  */
				 public void onCellEditSubtitulo29(CellEditEvent event) {
					 
					 UIColumn col= (UIColumn) event.getColumn();
					 DataTable o=(DataTable) event.getSource();
					
					 CajaVO info=(CajaVO)o.getRowData();
					 
					 
				        Object oldValue = event.getOldValue();
				        Object newValue = event.getNewValue();
				         
				        if(newValue != null && !newValue.equals(oldValue)) {
				            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
				            FacesContext.getCurrentInstance().addMessage(null, msg);
				        }
				       
				        //listadoServicios2.add(info);
				        
				        CajaVO monitore_borrar = new CajaVO();
				        

				        	for (CajaVO monitoreo_actual : listadoMonitoreoSubtitulo29) {
				        		
				        		if (info.getId() == monitoreo_actual.getId())
				        		{
				        		    monitore_borrar = monitoreo_actual;
				        		    break;
				        		}
								
							}
				        	
				        	listadoMonitoreoSubtitulo29.remove(monitore_borrar);
				        	//info.setS29Total(info.getS29Enero() + info.getS29Febrero() + info.getS29Marzo() + info.getS29Abril() + info.getS29Mayo() + info.getS29Junio() + info.getS29Julio() + info.getS29Agosto() + info.getS29Septiembre() + info.getS29Octubre() + info.getS29Noviembre()+ info.getS29Diciembre());
				        	listadoMonitoreoSubtitulo29.add(info);
				        	
				        	CajaGlobalVOSubtitulo29.setCaja(listadoMonitoreoSubtitulo29);
							        
				        	//MODIFICAR LA LISTA ORIGINAL
				        	
				        	List<CajaVO> lst_monitore_borrar =new ArrayList<CajaVO>();
				        	List<CajaVO> lst_monitore_agregar =new ArrayList<CajaVO>();
				        	for (CajaVO monitoreo_actual : listadoMonitoreoSubtitulo29) {
				        		for (CajaVO CajaVO : CajaGlobalVOSubtitulo29Original.getCaja()) {
									
								
				        		if (CajaVO.getId() == monitoreo_actual.getId())
				        		{
				        		    lst_monitore_borrar.add(CajaVO);
				        		    lst_monitore_agregar.add(monitoreo_actual);
				        		}
				        		}
								
							}
				        	
				        	for (CajaVO CajaVO : lst_monitore_borrar) {
				        		CajaGlobalVOSubtitulo29Original.getCaja().remove(CajaVO);
							}
				        	
				        	for (CajaVO CajaVO : lst_monitore_agregar) {
				        		CajaGlobalVOSubtitulo29Original.getCaja().add(CajaVO);
							}
				        	
				 
				 }
				 /*
				  * Carga los datos
				  */
				 public void CargarListaSubtitulo29()
					{
						CajaGlobalVOSubtitulo29 = new CajaGlobalVO();
						
						
						CajaVO CajaVO = new CajaVO();
						CajaVO.setAbril(4);
						CajaVO.setAgosto(9);
						CajaVO.setColor("#FFB5B5");
						CajaVO.setComuna("Macul");
						CajaVO.setConvenioMonto(10);
						CajaVO.setConvenioPorcentaje(10);
						CajaVO.setDiciembre(12);
						CajaVO.setEnero(1);
						CajaVO.setEstablecimiento("Establecimiento");
						CajaVO.setFebrero(2);
						CajaVO.setId(1);
						CajaVO.setJulio(7);
						CajaVO.setJunio(6);
						CajaVO.setMarcoMonto(100);
						CajaVO.setMarzo(3);
						CajaVO.setMayo(5);
						CajaVO.setNoviembre(11);
						CajaVO.setOctubre(10);
						CajaVO.setRemesaMonto(15);
						CajaVO.setRemesaPorcentaje(10);
						CajaVO.setSeptiembre(9);
						CajaVO.setServicio("Talcahuano");
						CajaVO.setTotal(100);
						CajaVO.setTransferenciaMonto(100);
						CajaVO.setTransferenciaPorcentaje(10);
						listadoMonitoreoSubtitulo29.add(CajaVO);
						//OTRO REGISTO
						CajaVO = new CajaVO();
						CajaVO.setAbril(45);
						CajaVO.setAgosto(95);
						CajaVO.setColor("#FFB5B5");
						CajaVO.setComuna("Macul 2");
						CajaVO.setConvenioMonto(150);
						CajaVO.setConvenioPorcentaje(150);
						CajaVO.setDiciembre(152);
						CajaVO.setEnero(51);
						CajaVO.setEstablecimiento("Establecimiento 2");
						CajaVO.setFebrero(52);
						CajaVO.setId(15);
						CajaVO.setJulio(57);
						CajaVO.setJunio(56);
						CajaVO.setMarcoMonto(510);
						CajaVO.setMarzo(53);
						CajaVO.setMayo(55);
						CajaVO.setNoviembre(511);
						CajaVO.setOctubre(510);
						CajaVO.setRemesaMonto(515);
						CajaVO.setRemesaPorcentaje(510);
						CajaVO.setSeptiembre(59);
						CajaVO.setServicio("Iquique");
						CajaVO.setTotal(5100);
						CajaVO.setTransferenciaMonto(5100);
						CajaVO.setTransferenciaPorcentaje(510);
						
						listadoMonitoreoSubtitulo29.add(CajaVO);
						CajaGlobalVOSubtitulo29.setCaja(listadoMonitoreoSubtitulo29);
						//EL FILTRADO DEBE SER IGUAL AL ORIGINAL
						CajaGlobalVOSubtitulo29Original = new CajaGlobalVO();
						
					
						
						CajaGlobalVOSubtitulo29Original  = CajaGlobalVOSubtitulo29.clone();
						
					}
				
				 
				 /*
				  ************************************************************************* FIN SUBTITULO 29
				  */
				 
				 /*
					 ********************************************************************************************* SUBTITULO PerCapita
					 */
					/*
					 * Filtra la lista segun los parametros de entrada, en este caso el servicio.
					 */
					public void filtrarSubtituloPerCapita()
					{
						
						//String nuevo = valor;
						List<CajaVO> lst = CajaGlobalVOSubtituloPerCapitaOriginal.getCaja();
						List<CajaVO> lstAgregar = new ArrayList<CajaVO>();
						for (CajaVO CajaVO : lst) {
							if (CajaVO.getIdServicio()==(valorComboProgramaSubtituloPerCapita))
							{
								lstAgregar.add(CajaVO);
							}
						}
						//listadoMonitoreoSubtituloPerCapita = lstAgregar;
						
						CajaGlobalVOSubtituloPerCapita.setCaja(lstAgregar);
						
					}
					
					 /*
					  * Modificaci�n de la celda
					  */
					 public void onCellEditSubtituloPerCapita(CellEditEvent event) {
						 
						 UIColumn col= (UIColumn) event.getColumn();
						 DataTable o=(DataTable) event.getSource();
						
						 CajaVO info=(CajaVO)o.getRowData();
						 
						 
					        Object oldValue = event.getOldValue();
					        Object newValue = event.getNewValue();
					         
					        if(newValue != null && !newValue.equals(oldValue)) {
					            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
					            FacesContext.getCurrentInstance().addMessage(null, msg);
					        }
					       
					        //listadoServicios2.add(info);
					        
					        CajaVO monitore_borrar = new CajaVO();
					        

					        	for (CajaVO monitoreo_actual : listadoMonitoreoSubtituloPerCapita) {
					        		
					        		if (info.getId() == monitoreo_actual.getId())
					        		{
					        		    monitore_borrar = monitoreo_actual;
					        		    break;
					        		}
									
								}
					        	
					        	listadoMonitoreoSubtituloPerCapita.remove(monitore_borrar);
					        	//info.setS24Total(info.getS24Enero() + info.getS24Febrero() + info.getS24Marzo() + info.getS24Abril() + info.getS24Mayo() + info.getS24Junio() + info.getS24Julio() + info.getS24Agosto() + info.getS24Septiembre() + info.getS24Octubre() + info.getS24Noviembre()+ info.getS24Diciembre());
					        	listadoMonitoreoSubtituloPerCapita.add(info);
					        	
					        	CajaGlobalVOSubtituloPerCapita.setCaja(listadoMonitoreoSubtituloPerCapita);
								        
					        	//MODIFICAR LA LISTA ORIGINAL
					        	
					        	List<CajaVO> lst_monitore_borrar =new ArrayList<CajaVO>();
					        	List<CajaVO> lst_monitore_agregar =new ArrayList<CajaVO>();
					        	for (CajaVO monitoreo_actual : listadoMonitoreoSubtituloPerCapita) {
					        		for (CajaVO CajaVO : CajaGlobalVOSubtituloPerCapitaOriginal.getCaja()) {
										
									
					        		if (CajaVO.getId() == monitoreo_actual.getId())
					        		{
					        		    lst_monitore_borrar.add(CajaVO);
					        		    lst_monitore_agregar.add(monitoreo_actual);
					        		}
					        		}
									
								}
					        	
					        	for (CajaVO CajaVO : lst_monitore_borrar) {
					        		CajaGlobalVOSubtituloPerCapitaOriginal.getCaja().remove(CajaVO);
								}
					        	
					        	for (CajaVO CajaVO : lst_monitore_agregar) {
					        		CajaGlobalVOSubtituloPerCapitaOriginal.getCaja().add(CajaVO);
								}
					        	
					 
					 }
					 /*
					  * Carga los datos
					  */
					 public void CargarListaSubtituloPerCapita()
						{
							CajaGlobalVOSubtituloPerCapita = new CajaGlobalVO();
							
							
							CajaVO CajaVO = new CajaVO();
							CajaVO.setAbril(4);
							CajaVO.setAgosto(9);
							CajaVO.setColor("#FFB5B5");
							CajaVO.setComuna("Macul");
							CajaVO.setConvenioMonto(10);
							CajaVO.setConvenioPorcentaje(10);
							CajaVO.setDiciembre(12);
							CajaVO.setEnero(1);
							CajaVO.setEstablecimiento("Establecimiento");
							CajaVO.setFebrero(2);
							CajaVO.setId(1);
							CajaVO.setJulio(7);
							CajaVO.setJunio(6);
							CajaVO.setMarcoMonto(100);
							CajaVO.setMarzo(3);
							CajaVO.setMayo(5);
							CajaVO.setNoviembre(11);
							CajaVO.setOctubre(10);
							CajaVO.setRemesaMonto(15);
							CajaVO.setRemesaPorcentaje(10);
							CajaVO.setSeptiembre(9);
							CajaVO.setServicio("Talcahuano");
							CajaVO.setTotal(100);
							CajaVO.setTransferenciaMonto(100);
							CajaVO.setTransferenciaPorcentaje(10);
							listadoMonitoreoSubtituloPerCapita.add(CajaVO);
							//OTRO REGISTO
							CajaVO = new CajaVO();
							CajaVO.setAbril(45);
							CajaVO.setAgosto(95);
							CajaVO.setColor("#FFB5B5");
							CajaVO.setComuna("Macul 2");
							CajaVO.setConvenioMonto(150);
							CajaVO.setConvenioPorcentaje(150);
							CajaVO.setDiciembre(152);
							CajaVO.setEnero(51);
							CajaVO.setEstablecimiento("Establecimiento 2");
							CajaVO.setFebrero(52);
							CajaVO.setId(15);
							CajaVO.setJulio(57);
							CajaVO.setJunio(56);
							CajaVO.setMarcoMonto(510);
							CajaVO.setMarzo(53);
							CajaVO.setMayo(55);
							CajaVO.setNoviembre(511);
							CajaVO.setOctubre(510);
							CajaVO.setRemesaMonto(515);
							CajaVO.setRemesaPorcentaje(510);
							CajaVO.setSeptiembre(59);
							CajaVO.setServicio("Iquique");
							CajaVO.setTotal(5100);
							CajaVO.setTransferenciaMonto(5100);
							CajaVO.setTransferenciaPorcentaje(510);
							
							listadoMonitoreoSubtituloPerCapita.add(CajaVO);
							CajaGlobalVOSubtituloPerCapita.setCaja(listadoMonitoreoSubtituloPerCapita);
							//EL FILTRADO DEBE SER IGUAL AL ORIGINAL
							CajaGlobalVOSubtituloPerCapitaOriginal = new CajaGlobalVO();
							
						
							
							CajaGlobalVOSubtituloPerCapitaOriginal  = CajaGlobalVOSubtituloPerCapita.clone();
							
						}
					
					 
					 /*
					  ************************************************************************* FIN SUBTITULO PerCapita
					  */
	 /*
		 * Transversal
		 */
		/*
		 * Crea las columnas dinamicas dependiendo del mes
		 */
		public void crearColumnasDinamicas()
		{
			columns = new ArrayList<ColumnaVO>();

			ColumnaVO col = new ColumnaVO();
			
			
			for (int i = mes; i < 13; i++) {
				String nombreMes = Util.obtenerNombreMes(i);
				if (i == mes){
					col = new ColumnaVO(nombreMes, "Real",nombreMes.toLowerCase());
					columns.add(col);
				}
				else
				{
					col = new ColumnaVO(nombreMes, "Estimad.",nombreMes.toLowerCase());
					columns.add(col);
				}
			}
			
			columnsInicial = new ArrayList<ColumnaVO>();
			for (int i = 1; i < mes; i++) {
				String nombreMes = Util.obtenerNombreMes(i);
				
					col = new ColumnaVO(nombreMes, "Real",nombreMes.toLowerCase());
					columnsInicial.add(col);
				}
			
		}
		/*
		 * Carga la lista de elementos para las diferentes grillas.
		 */
		public void cargarListas()
		{
		    listadoMonitoreoSubtitulo21 = new ArrayList<CajaVO>();
		    listadoMonitoreoSubtitulo22 = new ArrayList<CajaVO>();
		    listadoMonitoreoSubtitulo24 = new ArrayList<CajaVO>();
		    listadoMonitoreoSubtitulo29 = new ArrayList<CajaVO>();
		    listadoMonitoreoSubtituloPerCapita = new ArrayList<CajaVO>();
		      //Obtener desde la base de datos.
		    CargarListaSubtitulo21();
		    CargarListaSubtitulo22();
		    CargarListaSubtitulo24();
		    CargarListaSubtitulo29();
		    CargarListaSubtituloPerCapita();
		   
		}

		public int getMes() {
			return mes;
		}

		public void setMes(int mes) {
			this.mes = mes;
		}

		public String getMesActual() {
			return mesActual;
		}

		public void setMesActual(String mesActual) {
			this.mesActual = mesActual;
		}

		public int getAnoActual() {
			return anoActual;
		}

		public void setAnoActual(int anoActual) {
			this.anoActual = anoActual;
		}

		public List<ColumnaVO> getColumns() {
			return columns;
		}

		public void setColumns(List<ColumnaVO> columns) {
			this.columns = columns;
		}

		public List<ColumnaVO> getColumnsInicial() {
			return columnsInicial;
		}

		public void setColumnsInicial(List<ColumnaVO> columnsInicial) {
			this.columnsInicial = columnsInicial;
		}

		
		public CajaGlobalVO getCajaGlobalVOSubtitulo21Original() {
			return CajaGlobalVOSubtitulo21Original;
		}

		public void setCajaGlobalVOSubtitulo21Original(
				CajaGlobalVO CajaGlobalVOSubtitulo21Original) {
			this.CajaGlobalVOSubtitulo21Original = CajaGlobalVOSubtitulo21Original;
		}

		public CajaGlobalVO getCajaGlobalVOSubtitulo21() {
			return CajaGlobalVOSubtitulo21;
		}

		public void setCajaGlobalVOSubtitulo21(
				CajaGlobalVO CajaGlobalVOSubtitulo21) {
			this.CajaGlobalVOSubtitulo21 = CajaGlobalVOSubtitulo21;
		}

		public List<CajaVO> getListadoMonitoreoSubtitulo21() {
			return listadoMonitoreoSubtitulo21;
		}

		public void setListadoMonitoreoSubtitulo21(
				List<CajaVO> listadoMonitoreoSubtitulo21) {
			this.listadoMonitoreoSubtitulo21 = listadoMonitoreoSubtitulo21;
		}

		public Integer getValorComboProgramaSubtitulo21() {
			return valorComboProgramaSubtitulo21;
		}

		public void setValorComboProgramaSubtitulo21(
				Integer valorComboProgramaSubtitulo21) {
			this.valorComboProgramaSubtitulo21 = valorComboProgramaSubtitulo21;
		}

		public Integer getValorComboServicioSubtitulo21() {
			return valorComboServicioSubtitulo21;
		}

		public void setValorComboServicioSubtitulo21(
				Integer valorComboServicioSubtitulo21) {
			this.valorComboServicioSubtitulo21 = valorComboServicioSubtitulo21;
		}

		public Integer getValorComboComunaSubtitulo21() {
			return valorComboComunaSubtitulo21;
		}

		public void setValorComboComunaSubtitulo21(Integer valorComboComunaSubtitulo21) {
			this.valorComboComunaSubtitulo21 = valorComboComunaSubtitulo21;
		}

		public Integer getValorComboProgramaSubtitulo22() {
			return valorComboProgramaSubtitulo22;
		}

		public void setValorComboProgramaSubtitulo22(
				Integer valorComboProgramaSubtitulo22) {
			this.valorComboProgramaSubtitulo22 = valorComboProgramaSubtitulo22;
		}

		public Integer getValorComboServicioSubtitulo22() {
			return valorComboServicioSubtitulo22;
		}

		public void setValorComboServicioSubtitulo22(
				Integer valorComboServicioSubtitulo22) {
			this.valorComboServicioSubtitulo22 = valorComboServicioSubtitulo22;
		}

		public Integer getValorComboComunaSubtitulo22() {
			return valorComboComunaSubtitulo22;
		}

		public void setValorComboComunaSubtitulo22(Integer valorComboComunaSubtitulo22) {
			this.valorComboComunaSubtitulo22 = valorComboComunaSubtitulo22;
		}

		public CajaGlobalVO getCajaGlobalVOSubtitulo22Original() {
			return CajaGlobalVOSubtitulo22Original;
		}

		public void setCajaGlobalVOSubtitulo22Original(
				CajaGlobalVO CajaGlobalVOSubtitulo22Original) {
			this.CajaGlobalVOSubtitulo22Original = CajaGlobalVOSubtitulo22Original;
		}

		public CajaGlobalVO getCajaGlobalVOSubtitulo22() {
			return CajaGlobalVOSubtitulo22;
		}

		public void setCajaGlobalVOSubtitulo22(
				CajaGlobalVO CajaGlobalVOSubtitulo22) {
			this.CajaGlobalVOSubtitulo22 = CajaGlobalVOSubtitulo22;
		}

		public List<CajaVO> getListadoMonitoreoSubtitulo22() {
			return listadoMonitoreoSubtitulo22;
		}

		public void setListadoMonitoreoSubtitulo22(
				List<CajaVO> listadoMonitoreoSubtitulo22) {
			this.listadoMonitoreoSubtitulo22 = listadoMonitoreoSubtitulo22;
		}

		
	
	
		public Integer getValorComboProgramaSubtitulo24() {
			return valorComboProgramaSubtitulo24;
		}

		public void setValorComboProgramaSubtitulo24(
				Integer valorComboProgramaSubtitulo24) {
			this.valorComboProgramaSubtitulo24 = valorComboProgramaSubtitulo24;
		}

		public Integer getValorComboServicioSubtitulo24() {
			return valorComboServicioSubtitulo24;
		}

		public void setValorComboServicioSubtitulo24(
				Integer valorComboServicioSubtitulo24) {
			this.valorComboServicioSubtitulo24 = valorComboServicioSubtitulo24;
		}

		public Integer getValorComboComunaSubtitulo24() {
			return valorComboComunaSubtitulo24;
		}

		public void setValorComboComunaSubtitulo24(Integer valorComboComunaSubtitulo24) {
			this.valorComboComunaSubtitulo24 = valorComboComunaSubtitulo24;
		}

		public CajaGlobalVO getCajaGlobalVOSubtitulo24Original() {
			return CajaGlobalVOSubtitulo24Original;
		}

		public void setCajaGlobalVOSubtitulo24Original(
				CajaGlobalVO CajaGlobalVOSubtitulo24Original) {
			this.CajaGlobalVOSubtitulo24Original = CajaGlobalVOSubtitulo24Original;
		}

		public CajaGlobalVO getCajaGlobalVOSubtitulo24() {
			return CajaGlobalVOSubtitulo24;
		}

		public void setCajaGlobalVOSubtitulo24(
				CajaGlobalVO CajaGlobalVOSubtitulo24) {
			this.CajaGlobalVOSubtitulo24 = CajaGlobalVOSubtitulo24;
		}

		public List<CajaVO> getListadoMonitoreoSubtitulo24() {
			return listadoMonitoreoSubtitulo24;
		}

		public void setListadoMonitoreoSubtitulo24(
				List<CajaVO> listadoMonitoreoSubtitulo24) {
			this.listadoMonitoreoSubtitulo24 = listadoMonitoreoSubtitulo24;
		}

		public Integer getValorComboProgramaSubtitulo29() {
			return valorComboProgramaSubtitulo29;
		}

		public void setValorComboProgramaSubtitulo29(
				Integer valorComboProgramaSubtitulo29) {
			this.valorComboProgramaSubtitulo29 = valorComboProgramaSubtitulo29;
		}

		public Integer getValorComboServicioSubtitulo29() {
			return valorComboServicioSubtitulo29;
		}

		public void setValorComboServicioSubtitulo29(
				Integer valorComboServicioSubtitulo29) {
			this.valorComboServicioSubtitulo29 = valorComboServicioSubtitulo29;
		}

		public Integer getValorComboComunaSubtitulo29() {
			return valorComboComunaSubtitulo29;
		}

		public void setValorComboComunaSubtitulo29(Integer valorComboComunaSubtitulo29) {
			this.valorComboComunaSubtitulo29 = valorComboComunaSubtitulo29;
		}

		public CajaGlobalVO getCajaGlobalVOSubtitulo29Original() {
			return CajaGlobalVOSubtitulo29Original;
		}

		public void setCajaGlobalVOSubtitulo29Original(
				CajaGlobalVO CajaGlobalVOSubtitulo29Original) {
			this.CajaGlobalVOSubtitulo29Original = CajaGlobalVOSubtitulo29Original;
		}

		public CajaGlobalVO getCajaGlobalVOSubtitulo29() {
			return CajaGlobalVOSubtitulo29;
		}

		public void setCajaGlobalVOSubtitulo29(
				CajaGlobalVO CajaGlobalVOSubtitulo29) {
			this.CajaGlobalVOSubtitulo29 = CajaGlobalVOSubtitulo29;
		}

		public List<CajaVO> getListadoMonitoreoSubtitulo29() {
			return listadoMonitoreoSubtitulo29;
		}

		public void setListadoMonitoreoSubtitulo29(
				List<CajaVO> listadoMonitoreoSubtitulo29) {
			this.listadoMonitoreoSubtitulo29 = listadoMonitoreoSubtitulo29;
		}

		public Integer getValorComboProgramaSubtituloPerCapita() {
			return valorComboProgramaSubtituloPerCapita;
		}

		public void setValorComboProgramaSubtituloPerCapita(
				Integer valorComboProgramaSubtituloPerCapita) {
			this.valorComboProgramaSubtituloPerCapita = valorComboProgramaSubtituloPerCapita;
		}

		public Integer getValorComboServicioSubtituloPerCapita() {
			return valorComboServicioSubtituloPerCapita;
		}

		public void setValorComboServicioSubtituloPerCapita(
				Integer valorComboServicioSubtituloPerCapita) {
			this.valorComboServicioSubtituloPerCapita = valorComboServicioSubtituloPerCapita;
		}

		public Integer getValorComboComunaSubtituloPerCapita() {
			return valorComboComunaSubtituloPerCapita;
		}

		public void setValorComboComunaSubtituloPerCapita(
				Integer valorComboComunaSubtituloPerCapita) {
			this.valorComboComunaSubtituloPerCapita = valorComboComunaSubtituloPerCapita;
		}

		public CajaGlobalVO getCajaGlobalVOSubtituloPerCapitaOriginal() {
			return CajaGlobalVOSubtituloPerCapitaOriginal;
		}

		public void setCajaGlobalVOSubtituloPerCapitaOriginal(
				CajaGlobalVO CajaGlobalVOSubtituloPerCapitaOriginal) {
			this.CajaGlobalVOSubtituloPerCapitaOriginal = CajaGlobalVOSubtituloPerCapitaOriginal;
		}

		public CajaGlobalVO getCajaGlobalVOSubtituloPerCapita() {
			return CajaGlobalVOSubtituloPerCapita;
		}

		public void setCajaGlobalVOSubtituloPerCapita(
				CajaGlobalVO CajaGlobalVOSubtituloPerCapita) {
			this.CajaGlobalVOSubtituloPerCapita = CajaGlobalVOSubtituloPerCapita;
		}

		public List<CajaVO> getListadoMonitoreoSubtituloPerCapita() {
			return listadoMonitoreoSubtituloPerCapita;
		}

		public void setListadoMonitoreoSubtituloPerCapita(
				List<CajaVO> listadoMonitoreoSubtituloPerCapita) {
			this.listadoMonitoreoSubtituloPerCapita = listadoMonitoreoSubtituloPerCapita;
		}

		
		public Map<String, Integer> getProgramas21() {
			return programas21;
		}

		public void setProgramas21(Map<String, Integer> programas21) {
			this.programas21 = programas21;
		}

		public Map<String, Integer> getServicios21() {
			return servicios21;
		}

		public void setServicios21(Map<String, Integer> servicios21) {
			this.servicios21 = servicios21;
		}

		public Map<String, Integer> getComunas21() {
			return comunas21;
		}

		public void setComunas21(Map<String, Integer> comunas21) {
			this.comunas21 = comunas21;
		}

		public Map<String, Integer> getProgramas22() {
			return programas22;
		}

		public void setProgramas22(Map<String, Integer> programas22) {
			this.programas22 = programas22;
		}

		public Map<String, Integer> getServicios22() {
			return servicios22;
		}

		public void setServicios22(Map<String, Integer> servicios22) {
			this.servicios22 = servicios22;
		}

		public Map<String, Integer> getComunas22() {
			return comunas22;
		}

		public void setComunas22(Map<String, Integer> comunas22) {
			this.comunas22 = comunas22;
		}

		public Map<String, Integer> getProgramas24() {
			return programas24;
		}

		public void setProgramas24(Map<String, Integer> programas24) {
			this.programas24 = programas24;
		}

		public Map<String, Integer> getServicios24() {
			return servicios24;
		}

		public void setServicios24(Map<String, Integer> servicios24) {
			this.servicios24 = servicios24;
		}

		public Map<String, Integer> getComunas24() {
			return comunas24;
		}

		public void setComunas24(Map<String, Integer> comunas24) {
			this.comunas24 = comunas24;
		}

		public Map<String, Integer> getProgramas29() {
			return programas29;
		}

		public void setProgramas29(Map<String, Integer> programas29) {
			this.programas29 = programas29;
		}

		public Map<String, Integer> getServicios29() {
			return servicios29;
		}

		public void setServicios29(Map<String, Integer> servicios29) {
			this.servicios29 = servicios29;
		}

		public Map<String, Integer> getComunas29() {
			return comunas29;
		}

		public void setComunas29(Map<String, Integer> comunas29) {
			this.comunas29 = comunas29;
		}

		public Map<String, Integer> getProgramasPerCapita() {
			return programasPerCapita;
		}

		public void setProgramasPerCapita(Map<String, Integer> programasPerCapita) {
			this.programasPerCapita = programasPerCapita;
		}

		public Map<String, Integer> getServiciosPerCapita() {
			return serviciosPerCapita;
		}

		public void setServiciosPerCapita(Map<String, Integer> serviciosPerCapita) {
			this.serviciosPerCapita = serviciosPerCapita;
		}

		public Map<String, Integer> getComunasPerCapita() {
			return comunasPerCapita;
		}

		public void setComunasPerCapita(Map<String, Integer> comunasPerCapita) {
			this.comunasPerCapita = comunasPerCapita;
		}

		public List<ProgramaVO> getProgramas() {
			return programas;
		}

		public void setProgramas(List<ProgramaVO> programas) {
			this.programas = programas;
		}
	


}
