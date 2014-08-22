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

import minsal.divap.service.EstimacionFlujoCajaService;
import minsal.divap.service.ProgramasService;
import minsal.divap.util.Util;
import minsal.divap.vo.ColumnaVO;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ProgramaVO;

import org.apache.log4j.Logger;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

import cl.minsal.divap.pojo.ComponentePojo;
import cl.minsal.divap.pojo.EstimacionFlujoMonitoreoGlobalPojo;
import cl.minsal.divap.pojo.EstimacionFlujoMonitoreoPojo;
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

	
	/*
	 * Transversal
	 */
	
	
	private int mes;
	private String mesActual;
	private int anoActual;
	List<ColumnaVO> columns;
	List<ColumnaVO> columnsInicial;
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

	
	/*
	 *SUBTITULO 21 
	 */
	private String valorComboProgramaSubtitulo21;
	private String valorComboServicioSubtitulo21;
	private String valorComboComunaSubtitulo21;
	EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo21Original;
	EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo21;
	List<EstimacionFlujoMonitoreoPojo> listadoMonitoreoSubtitulo21;
	

	/*
	 *SUBTITULO 22 
	 */
	private String valorComboProgramaSubtitulo22;
	private String valorComboServicioSubtitulo22;
	private String valorComboComunaSubtitulo22;
	EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo22Original;
	EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo22;
	List<EstimacionFlujoMonitoreoPojo> listadoMonitoreoSubtitulo22;
	
	/*
	 *SUBTITULO 24 
	 */
	private String valorComboProgramaSubtitulo24;
	private String valorComboServicioSubtitulo24;
	private String valorComboComunaSubtitulo24;
	EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo24Original;
	EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo24;
	List<EstimacionFlujoMonitoreoPojo> listadoMonitoreoSubtitulo24;
	
	
	/*
	 *SUBTITULO 29 
	 */
	private String valorComboProgramaSubtitulo29;
	private String valorComboServicioSubtitulo29;
	private String valorComboComunaSubtitulo29;
	public String getValorComboProgramaSubtitulo24() {
		return valorComboProgramaSubtitulo24;
	}

	public void setValorComboProgramaSubtitulo24(
			String valorComboProgramaSubtitulo24) {
		this.valorComboProgramaSubtitulo24 = valorComboProgramaSubtitulo24;
	}

	public String getValorComboServicioSubtitulo24() {
		return valorComboServicioSubtitulo24;
	}

	public void setValorComboServicioSubtitulo24(
			String valorComboServicioSubtitulo24) {
		this.valorComboServicioSubtitulo24 = valorComboServicioSubtitulo24;
	}

	public String getValorComboComunaSubtitulo24() {
		return valorComboComunaSubtitulo24;
	}

	public void setValorComboComunaSubtitulo24(String valorComboComunaSubtitulo24) {
		this.valorComboComunaSubtitulo24 = valorComboComunaSubtitulo24;
	}

	public EstimacionFlujoMonitoreoGlobalPojo getEstimacionFlujoMonitoreoGlobalPojoSubtitulo24Original() {
		return estimacionFlujoMonitoreoGlobalPojoSubtitulo24Original;
	}

	public void setEstimacionFlujoMonitoreoGlobalPojoSubtitulo24Original(
			EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo24Original) {
		this.estimacionFlujoMonitoreoGlobalPojoSubtitulo24Original = estimacionFlujoMonitoreoGlobalPojoSubtitulo24Original;
	}

	public EstimacionFlujoMonitoreoGlobalPojo getEstimacionFlujoMonitoreoGlobalPojoSubtitulo24() {
		return estimacionFlujoMonitoreoGlobalPojoSubtitulo24;
	}

	public void setEstimacionFlujoMonitoreoGlobalPojoSubtitulo24(
			EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo24) {
		this.estimacionFlujoMonitoreoGlobalPojoSubtitulo24 = estimacionFlujoMonitoreoGlobalPojoSubtitulo24;
	}

	public List<EstimacionFlujoMonitoreoPojo> getListadoMonitoreoSubtitulo24() {
		return listadoMonitoreoSubtitulo24;
	}

	public void setListadoMonitoreoSubtitulo24(
			List<EstimacionFlujoMonitoreoPojo> listadoMonitoreoSubtitulo24) {
		this.listadoMonitoreoSubtitulo24 = listadoMonitoreoSubtitulo24;
	}

	public String getValorComboProgramaSubtitulo29() {
		return valorComboProgramaSubtitulo29;
	}

	public void setValorComboProgramaSubtitulo29(
			String valorComboProgramaSubtitulo29) {
		this.valorComboProgramaSubtitulo29 = valorComboProgramaSubtitulo29;
	}

	public String getValorComboServicioSubtitulo29() {
		return valorComboServicioSubtitulo29;
	}

	public void setValorComboServicioSubtitulo29(
			String valorComboServicioSubtitulo29) {
		this.valorComboServicioSubtitulo29 = valorComboServicioSubtitulo29;
	}

	public String getValorComboComunaSubtitulo29() {
		return valorComboComunaSubtitulo29;
	}

	public void setValorComboComunaSubtitulo29(String valorComboComunaSubtitulo29) {
		this.valorComboComunaSubtitulo29 = valorComboComunaSubtitulo29;
	}

	public EstimacionFlujoMonitoreoGlobalPojo getEstimacionFlujoMonitoreoGlobalPojoSubtitulo29Original() {
		return estimacionFlujoMonitoreoGlobalPojoSubtitulo29Original;
	}

	public void setEstimacionFlujoMonitoreoGlobalPojoSubtitulo29Original(
			EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo29Original) {
		this.estimacionFlujoMonitoreoGlobalPojoSubtitulo29Original = estimacionFlujoMonitoreoGlobalPojoSubtitulo29Original;
	}

	public EstimacionFlujoMonitoreoGlobalPojo getEstimacionFlujoMonitoreoGlobalPojoSubtitulo29() {
		return estimacionFlujoMonitoreoGlobalPojoSubtitulo29;
	}

	public void setEstimacionFlujoMonitoreoGlobalPojoSubtitulo29(
			EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo29) {
		this.estimacionFlujoMonitoreoGlobalPojoSubtitulo29 = estimacionFlujoMonitoreoGlobalPojoSubtitulo29;
	}

	public List<EstimacionFlujoMonitoreoPojo> getListadoMonitoreoSubtitulo29() {
		return listadoMonitoreoSubtitulo29;
	}

	public void setListadoMonitoreoSubtitulo29(
			List<EstimacionFlujoMonitoreoPojo> listadoMonitoreoSubtitulo29) {
		this.listadoMonitoreoSubtitulo29 = listadoMonitoreoSubtitulo29;
	}

	public String getValorComboProgramaSubtituloPerCapita() {
		return valorComboProgramaSubtituloPerCapita;
	}

	public void setValorComboProgramaSubtituloPerCapita(
			String valorComboProgramaSubtituloPerCapita) {
		this.valorComboProgramaSubtituloPerCapita = valorComboProgramaSubtituloPerCapita;
	}

	public String getValorComboServicioSubtituloPerCapita() {
		return valorComboServicioSubtituloPerCapita;
	}

	public void setValorComboServicioSubtituloPerCapita(
			String valorComboServicioSubtituloPerCapita) {
		this.valorComboServicioSubtituloPerCapita = valorComboServicioSubtituloPerCapita;
	}

	public String getValorComboComunaSubtituloPerCapita() {
		return valorComboComunaSubtituloPerCapita;
	}

	public void setValorComboComunaSubtituloPerCapita(
			String valorComboComunaSubtituloPerCapita) {
		this.valorComboComunaSubtituloPerCapita = valorComboComunaSubtituloPerCapita;
	}

	public EstimacionFlujoMonitoreoGlobalPojo getEstimacionFlujoMonitoreoGlobalPojoSubtituloPerCapitaOriginal() {
		return estimacionFlujoMonitoreoGlobalPojoSubtituloPerCapitaOriginal;
	}

	public void setEstimacionFlujoMonitoreoGlobalPojoSubtituloPerCapitaOriginal(
			EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtituloPerCapitaOriginal) {
		this.estimacionFlujoMonitoreoGlobalPojoSubtituloPerCapitaOriginal = estimacionFlujoMonitoreoGlobalPojoSubtituloPerCapitaOriginal;
	}

	public EstimacionFlujoMonitoreoGlobalPojo getEstimacionFlujoMonitoreoGlobalPojoSubtituloPerCapita() {
		return estimacionFlujoMonitoreoGlobalPojoSubtituloPerCapita;
	}

	public void setEstimacionFlujoMonitoreoGlobalPojoSubtituloPerCapita(
			EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtituloPerCapita) {
		this.estimacionFlujoMonitoreoGlobalPojoSubtituloPerCapita = estimacionFlujoMonitoreoGlobalPojoSubtituloPerCapita;
	}

	public List<EstimacionFlujoMonitoreoPojo> getListadoMonitoreoSubtituloPerCapita() {
		return listadoMonitoreoSubtituloPerCapita;
	}

	public void setListadoMonitoreoSubtituloPerCapita(
			List<EstimacionFlujoMonitoreoPojo> listadoMonitoreoSubtituloPerCapita) {
		this.listadoMonitoreoSubtituloPerCapita = listadoMonitoreoSubtituloPerCapita;
	}
	EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo29Original;
	EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo29;
	List<EstimacionFlujoMonitoreoPojo> listadoMonitoreoSubtitulo29;
	
	
	/*
	 *SUBTITULO PerCapita 
	 */
	private String valorComboProgramaSubtituloPerCapita;
	private String valorComboServicioSubtituloPerCapita;
	private String valorComboComunaSubtituloPerCapita;
	EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtituloPerCapitaOriginal;
	EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtituloPerCapita;
	List<EstimacionFlujoMonitoreoPojo> listadoMonitoreoSubtituloPerCapita;
	
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
		
		cargarListas();
		setMes(11);
		setAnoActual(2014);
		crearColumnasDinamicas();
	
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
		List<EstimacionFlujoMonitoreoPojo> lst = estimacionFlujoMonitoreoGlobalPojoSubtitulo21Original.getEstimacionFlujoMonitoreoPojo();
		List<EstimacionFlujoMonitoreoPojo> lstAgregar = new ArrayList<EstimacionFlujoMonitoreoPojo>();
		for (EstimacionFlujoMonitoreoPojo estimacionFlujoMonitoreoPojo : lst) {
			if (estimacionFlujoMonitoreoPojo.getServicio().contains(valorComboProgramaSubtitulo21))
			{
				lstAgregar.add(estimacionFlujoMonitoreoPojo);
			}
		}
		//listadoMonitoreoSubtitulo21 = lstAgregar;
		
		estimacionFlujoMonitoreoGlobalPojoSubtitulo21.setEstimacionFlujoMonitoreoPojo(lstAgregar);
		
	}
	
	 /*
	  * Modificación de la celda
	  */
	 public void onCellEditSubtitulo21(CellEditEvent event) {
		 
		 UIColumn col= (UIColumn) event.getColumn();
		 DataTable o=(DataTable) event.getSource();
		
		 EstimacionFlujoMonitoreoPojo info=(EstimacionFlujoMonitoreoPojo)o.getRowData();
		 
		 
	        Object oldValue = event.getOldValue();
	        Object newValue = event.getNewValue();
	         
	        if(newValue != null && !newValue.equals(oldValue)) {
	            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
	            FacesContext.getCurrentInstance().addMessage(null, msg);
	        }
	       
	        //listadoServicios2.add(info);
	        
	        EstimacionFlujoMonitoreoPojo monitore_borrar = new EstimacionFlujoMonitoreoPojo();
	        

	        	for (EstimacionFlujoMonitoreoPojo monitoreo_actual : listadoMonitoreoSubtitulo21) {
	        		
	        		if (info.getId() == monitoreo_actual.getId())
	        		{
	        		    monitore_borrar = monitoreo_actual;
	        		    break;
	        		}
					
				}
	        	
	        	listadoMonitoreoSubtitulo21.remove(monitore_borrar);
	        	//info.setS24Total(info.getS24Enero() + info.getS24Febrero() + info.getS24Marzo() + info.getS24Abril() + info.getS24Mayo() + info.getS24Junio() + info.getS24Julio() + info.getS24Agosto() + info.getS24Septiembre() + info.getS24Octubre() + info.getS24Noviembre()+ info.getS24Diciembre());
	        	listadoMonitoreoSubtitulo21.add(info);
	        	
	        	estimacionFlujoMonitoreoGlobalPojoSubtitulo21.setEstimacionFlujoMonitoreoPojo(listadoMonitoreoSubtitulo21);
				        
	        	//MODIFICAR LA LISTA ORIGINAL
	        	
	        	List<EstimacionFlujoMonitoreoPojo> lst_monitore_borrar =new ArrayList<EstimacionFlujoMonitoreoPojo>();
	        	List<EstimacionFlujoMonitoreoPojo> lst_monitore_agregar =new ArrayList<EstimacionFlujoMonitoreoPojo>();
	        	for (EstimacionFlujoMonitoreoPojo monitoreo_actual : listadoMonitoreoSubtitulo21) {
	        		for (EstimacionFlujoMonitoreoPojo estimacionFlujoMonitoreoPojo : estimacionFlujoMonitoreoGlobalPojoSubtitulo21Original.getEstimacionFlujoMonitoreoPojo()) {
						
					
	        		if (estimacionFlujoMonitoreoPojo.getId() == monitoreo_actual.getId())
	        		{
	        		    lst_monitore_borrar.add(estimacionFlujoMonitoreoPojo);
	        		    lst_monitore_agregar.add(monitoreo_actual);
	        		}
	        		}
					
				}
	        	
	        	for (EstimacionFlujoMonitoreoPojo estimacionFlujoMonitoreoPojo : lst_monitore_borrar) {
	        		estimacionFlujoMonitoreoGlobalPojoSubtitulo21Original.getEstimacionFlujoMonitoreoPojo().remove(estimacionFlujoMonitoreoPojo);
				}
	        	
	        	for (EstimacionFlujoMonitoreoPojo estimacionFlujoMonitoreoPojo : lst_monitore_agregar) {
	        		estimacionFlujoMonitoreoGlobalPojoSubtitulo21Original.getEstimacionFlujoMonitoreoPojo().add(estimacionFlujoMonitoreoPojo);
				}
	        	
	 
	 }
	 /*
	  * Carga los datos
	  */
	 public void CargarListaSubtitulo21()
		{
			estimacionFlujoMonitoreoGlobalPojoSubtitulo21 = new EstimacionFlujoMonitoreoGlobalPojo();
			
			
			EstimacionFlujoMonitoreoPojo estimacionFlujoMonitoreoPojo = new EstimacionFlujoMonitoreoPojo();
			estimacionFlujoMonitoreoPojo.setAbril(4);
			estimacionFlujoMonitoreoPojo.setAgosto(9);
			estimacionFlujoMonitoreoPojo.setColor("#FFB5B5");
			estimacionFlujoMonitoreoPojo.setComuna("Macul");
			estimacionFlujoMonitoreoPojo.setConvenioMonto(10);
			estimacionFlujoMonitoreoPojo.setConvenioPorcentaje(10);
			estimacionFlujoMonitoreoPojo.setDiciembre(12);
			estimacionFlujoMonitoreoPojo.setEnero(1);
			estimacionFlujoMonitoreoPojo.setEstablecimiento("Establecimiento");
			estimacionFlujoMonitoreoPojo.setFebrero(2);
			estimacionFlujoMonitoreoPojo.setId(1);
			estimacionFlujoMonitoreoPojo.setJulio(7);
			estimacionFlujoMonitoreoPojo.setJunio(6);
			estimacionFlujoMonitoreoPojo.setMarcoMonto(100);
			estimacionFlujoMonitoreoPojo.setMarzo(3);
			estimacionFlujoMonitoreoPojo.setMayo(5);
			estimacionFlujoMonitoreoPojo.setNoviembre(11);
			estimacionFlujoMonitoreoPojo.setOctubre(10);
			estimacionFlujoMonitoreoPojo.setRemesaMonto(15);
			estimacionFlujoMonitoreoPojo.setRemesaPorcentaje(10);
			estimacionFlujoMonitoreoPojo.setSeptiembre(9);
			estimacionFlujoMonitoreoPojo.setServicio("Talcahuano");
			estimacionFlujoMonitoreoPojo.setTotal(100);
			estimacionFlujoMonitoreoPojo.setTransferenciaMonto(100);
			estimacionFlujoMonitoreoPojo.setTransferenciaPorcentaje(10);
			listadoMonitoreoSubtitulo21.add(estimacionFlujoMonitoreoPojo);
			//OTRO REGISTO
			estimacionFlujoMonitoreoPojo = new EstimacionFlujoMonitoreoPojo();
			estimacionFlujoMonitoreoPojo.setAbril(45);
			estimacionFlujoMonitoreoPojo.setAgosto(95);
			estimacionFlujoMonitoreoPojo.setColor("#FFB5B5");
			estimacionFlujoMonitoreoPojo.setComuna("Macul 2");
			estimacionFlujoMonitoreoPojo.setConvenioMonto(150);
			estimacionFlujoMonitoreoPojo.setConvenioPorcentaje(150);
			estimacionFlujoMonitoreoPojo.setDiciembre(152);
			estimacionFlujoMonitoreoPojo.setEnero(51);
			estimacionFlujoMonitoreoPojo.setEstablecimiento("Establecimiento 2");
			estimacionFlujoMonitoreoPojo.setFebrero(52);
			estimacionFlujoMonitoreoPojo.setId(15);
			estimacionFlujoMonitoreoPojo.setJulio(57);
			estimacionFlujoMonitoreoPojo.setJunio(56);
			estimacionFlujoMonitoreoPojo.setMarcoMonto(510);
			estimacionFlujoMonitoreoPojo.setMarzo(53);
			estimacionFlujoMonitoreoPojo.setMayo(55);
			estimacionFlujoMonitoreoPojo.setNoviembre(511);
			estimacionFlujoMonitoreoPojo.setOctubre(510);
			estimacionFlujoMonitoreoPojo.setRemesaMonto(515);
			estimacionFlujoMonitoreoPojo.setRemesaPorcentaje(510);
			estimacionFlujoMonitoreoPojo.setSeptiembre(59);
			estimacionFlujoMonitoreoPojo.setServicio("Iquique");
			estimacionFlujoMonitoreoPojo.setTotal(5100);
			estimacionFlujoMonitoreoPojo.setTransferenciaMonto(5100);
			estimacionFlujoMonitoreoPojo.setTransferenciaPorcentaje(510);
			
			listadoMonitoreoSubtitulo21.add(estimacionFlujoMonitoreoPojo);
			estimacionFlujoMonitoreoGlobalPojoSubtitulo21.setEstimacionFlujoMonitoreoPojo(listadoMonitoreoSubtitulo21);
			//EL FILTRADO DEBE SER IGUAL AL ORIGINAL
			estimacionFlujoMonitoreoGlobalPojoSubtitulo21Original = new EstimacionFlujoMonitoreoGlobalPojo();
			
		
			
			estimacionFlujoMonitoreoGlobalPojoSubtitulo21Original  = estimacionFlujoMonitoreoGlobalPojoSubtitulo21.clone();
			
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
			List<EstimacionFlujoMonitoreoPojo> lst = estimacionFlujoMonitoreoGlobalPojoSubtitulo22Original.getEstimacionFlujoMonitoreoPojo();
			List<EstimacionFlujoMonitoreoPojo> lstAgregar = new ArrayList<EstimacionFlujoMonitoreoPojo>();
			for (EstimacionFlujoMonitoreoPojo estimacionFlujoMonitoreoPojo : lst) {
				if (estimacionFlujoMonitoreoPojo.getServicio().contains(valorComboProgramaSubtitulo22))
				{
					lstAgregar.add(estimacionFlujoMonitoreoPojo);
				}
			}
			//listadoMonitoreoSubtitulo22 = lstAgregar;
			
			estimacionFlujoMonitoreoGlobalPojoSubtitulo22.setEstimacionFlujoMonitoreoPojo(lstAgregar);
			
		}
		
		 /*
		  * Modificación de la celda
		  */
		 public void onCellEditSubtitulo22(CellEditEvent event) {
			 
			 UIColumn col= (UIColumn) event.getColumn();
			 DataTable o=(DataTable) event.getSource();
			
			 EstimacionFlujoMonitoreoPojo info=(EstimacionFlujoMonitoreoPojo)o.getRowData();
			 
			 
		        Object oldValue = event.getOldValue();
		        Object newValue = event.getNewValue();
		         
		        if(newValue != null && !newValue.equals(oldValue)) {
		            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
		            FacesContext.getCurrentInstance().addMessage(null, msg);
		        }
		       
		        //listadoServicios2.add(info);
		        
		        EstimacionFlujoMonitoreoPojo monitore_borrar = new EstimacionFlujoMonitoreoPojo();
		        

		        	for (EstimacionFlujoMonitoreoPojo monitoreo_actual : listadoMonitoreoSubtitulo22) {
		        		
		        		if (info.getId() == monitoreo_actual.getId())
		        		{
		        		    monitore_borrar = monitoreo_actual;
		        		    break;
		        		}
						
					}
		        	
		        	listadoMonitoreoSubtitulo22.remove(monitore_borrar);
		        	//info.setS24Total(info.getS24Enero() + info.getS24Febrero() + info.getS24Marzo() + info.getS24Abril() + info.getS24Mayo() + info.getS24Junio() + info.getS24Julio() + info.getS24Agosto() + info.getS24Septiembre() + info.getS24Octubre() + info.getS24Noviembre()+ info.getS24Diciembre());
		        	listadoMonitoreoSubtitulo22.add(info);
		        	
		        	estimacionFlujoMonitoreoGlobalPojoSubtitulo22.setEstimacionFlujoMonitoreoPojo(listadoMonitoreoSubtitulo22);
					        
		        	//MODIFICAR LA LISTA ORIGINAL
		        	
		        	List<EstimacionFlujoMonitoreoPojo> lst_monitore_borrar =new ArrayList<EstimacionFlujoMonitoreoPojo>();
		        	List<EstimacionFlujoMonitoreoPojo> lst_monitore_agregar =new ArrayList<EstimacionFlujoMonitoreoPojo>();
		        	for (EstimacionFlujoMonitoreoPojo monitoreo_actual : listadoMonitoreoSubtitulo22) {
		        		for (EstimacionFlujoMonitoreoPojo estimacionFlujoMonitoreoPojo : estimacionFlujoMonitoreoGlobalPojoSubtitulo22Original.getEstimacionFlujoMonitoreoPojo()) {
							
						
		        		if (estimacionFlujoMonitoreoPojo.getId() == monitoreo_actual.getId())
		        		{
		        		    lst_monitore_borrar.add(estimacionFlujoMonitoreoPojo);
		        		    lst_monitore_agregar.add(monitoreo_actual);
		        		}
		        		}
						
					}
		        	
		        	for (EstimacionFlujoMonitoreoPojo estimacionFlujoMonitoreoPojo : lst_monitore_borrar) {
		        		estimacionFlujoMonitoreoGlobalPojoSubtitulo22Original.getEstimacionFlujoMonitoreoPojo().remove(estimacionFlujoMonitoreoPojo);
					}
		        	
		        	for (EstimacionFlujoMonitoreoPojo estimacionFlujoMonitoreoPojo : lst_monitore_agregar) {
		        		estimacionFlujoMonitoreoGlobalPojoSubtitulo22Original.getEstimacionFlujoMonitoreoPojo().add(estimacionFlujoMonitoreoPojo);
					}
		        	
		 
		 }
		 /*
		  * Carga los datos
		  */
		 public void CargarListaSubtitulo22()
			{
				estimacionFlujoMonitoreoGlobalPojoSubtitulo22 = new EstimacionFlujoMonitoreoGlobalPojo();
				
				
				EstimacionFlujoMonitoreoPojo estimacionFlujoMonitoreoPojo = new EstimacionFlujoMonitoreoPojo();
				estimacionFlujoMonitoreoPojo.setAbril(4);
				estimacionFlujoMonitoreoPojo.setAgosto(9);
				estimacionFlujoMonitoreoPojo.setColor("#FFB5B5");
				estimacionFlujoMonitoreoPojo.setComuna("Macul");
				estimacionFlujoMonitoreoPojo.setConvenioMonto(10);
				estimacionFlujoMonitoreoPojo.setConvenioPorcentaje(10);
				estimacionFlujoMonitoreoPojo.setDiciembre(12);
				estimacionFlujoMonitoreoPojo.setEnero(1);
				estimacionFlujoMonitoreoPojo.setEstablecimiento("Establecimiento");
				estimacionFlujoMonitoreoPojo.setFebrero(2);
				estimacionFlujoMonitoreoPojo.setId(1);
				estimacionFlujoMonitoreoPojo.setJulio(7);
				estimacionFlujoMonitoreoPojo.setJunio(6);
				estimacionFlujoMonitoreoPojo.setMarcoMonto(100);
				estimacionFlujoMonitoreoPojo.setMarzo(3);
				estimacionFlujoMonitoreoPojo.setMayo(5);
				estimacionFlujoMonitoreoPojo.setNoviembre(11);
				estimacionFlujoMonitoreoPojo.setOctubre(10);
				estimacionFlujoMonitoreoPojo.setRemesaMonto(15);
				estimacionFlujoMonitoreoPojo.setRemesaPorcentaje(10);
				estimacionFlujoMonitoreoPojo.setSeptiembre(9);
				estimacionFlujoMonitoreoPojo.setServicio("Talcahuano");
				estimacionFlujoMonitoreoPojo.setTotal(100);
				estimacionFlujoMonitoreoPojo.setTransferenciaMonto(100);
				estimacionFlujoMonitoreoPojo.setTransferenciaPorcentaje(10);
				listadoMonitoreoSubtitulo22.add(estimacionFlujoMonitoreoPojo);
				//OTRO REGISTO
				estimacionFlujoMonitoreoPojo = new EstimacionFlujoMonitoreoPojo();
				estimacionFlujoMonitoreoPojo.setAbril(45);
				estimacionFlujoMonitoreoPojo.setAgosto(95);
				estimacionFlujoMonitoreoPojo.setColor("#FFB5B5");
				estimacionFlujoMonitoreoPojo.setComuna("Macul 2");
				estimacionFlujoMonitoreoPojo.setConvenioMonto(150);
				estimacionFlujoMonitoreoPojo.setConvenioPorcentaje(150);
				estimacionFlujoMonitoreoPojo.setDiciembre(152);
				estimacionFlujoMonitoreoPojo.setEnero(51);
				estimacionFlujoMonitoreoPojo.setEstablecimiento("Establecimiento 2");
				estimacionFlujoMonitoreoPojo.setFebrero(52);
				estimacionFlujoMonitoreoPojo.setId(15);
				estimacionFlujoMonitoreoPojo.setJulio(57);
				estimacionFlujoMonitoreoPojo.setJunio(56);
				estimacionFlujoMonitoreoPojo.setMarcoMonto(510);
				estimacionFlujoMonitoreoPojo.setMarzo(53);
				estimacionFlujoMonitoreoPojo.setMayo(55);
				estimacionFlujoMonitoreoPojo.setNoviembre(511);
				estimacionFlujoMonitoreoPojo.setOctubre(510);
				estimacionFlujoMonitoreoPojo.setRemesaMonto(515);
				estimacionFlujoMonitoreoPojo.setRemesaPorcentaje(510);
				estimacionFlujoMonitoreoPojo.setSeptiembre(59);
				estimacionFlujoMonitoreoPojo.setServicio("Iquique");
				estimacionFlujoMonitoreoPojo.setTotal(5100);
				estimacionFlujoMonitoreoPojo.setTransferenciaMonto(5100);
				estimacionFlujoMonitoreoPojo.setTransferenciaPorcentaje(510);
				
				listadoMonitoreoSubtitulo22.add(estimacionFlujoMonitoreoPojo);
				estimacionFlujoMonitoreoGlobalPojoSubtitulo22.setEstimacionFlujoMonitoreoPojo(listadoMonitoreoSubtitulo22);
				//EL FILTRADO DEBE SER IGUAL AL ORIGINAL
				estimacionFlujoMonitoreoGlobalPojoSubtitulo22Original = new EstimacionFlujoMonitoreoGlobalPojo();
				
			
				
				estimacionFlujoMonitoreoGlobalPojoSubtitulo22Original  = estimacionFlujoMonitoreoGlobalPojoSubtitulo22.clone();
				
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
				List<EstimacionFlujoMonitoreoPojo> lst = estimacionFlujoMonitoreoGlobalPojoSubtitulo24Original.getEstimacionFlujoMonitoreoPojo();
				List<EstimacionFlujoMonitoreoPojo> lstAgregar = new ArrayList<EstimacionFlujoMonitoreoPojo>();
				for (EstimacionFlujoMonitoreoPojo estimacionFlujoMonitoreoPojo : lst) {
					if (estimacionFlujoMonitoreoPojo.getServicio().contains(valorComboProgramaSubtitulo24))
					{
						lstAgregar.add(estimacionFlujoMonitoreoPojo);
					}
				}
				//listadoMonitoreoSubtitulo24 = lstAgregar;
				
				estimacionFlujoMonitoreoGlobalPojoSubtitulo24.setEstimacionFlujoMonitoreoPojo(lstAgregar);
				
			}
			
			 /*
			  * Modificación de la celda
			  */
			 public void onCellEditSubtitulo24(CellEditEvent event) {
				 
				 UIColumn col= (UIColumn) event.getColumn();
				 DataTable o=(DataTable) event.getSource();
				
				 EstimacionFlujoMonitoreoPojo info=(EstimacionFlujoMonitoreoPojo)o.getRowData();
				 
				 
			        Object oldValue = event.getOldValue();
			        Object newValue = event.getNewValue();
			         
			        if(newValue != null && !newValue.equals(oldValue)) {
			            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
			            FacesContext.getCurrentInstance().addMessage(null, msg);
			        }
			       
			        //listadoServicios2.add(info);
			        
			        EstimacionFlujoMonitoreoPojo monitore_borrar = new EstimacionFlujoMonitoreoPojo();
			        

			        	for (EstimacionFlujoMonitoreoPojo monitoreo_actual : listadoMonitoreoSubtitulo24) {
			        		
			        		if (info.getId() == monitoreo_actual.getId())
			        		{
			        		    monitore_borrar = monitoreo_actual;
			        		    break;
			        		}
							
						}
			        	
			        	listadoMonitoreoSubtitulo24.remove(monitore_borrar);
			        	//info.setS24Total(info.getS24Enero() + info.getS24Febrero() + info.getS24Marzo() + info.getS24Abril() + info.getS24Mayo() + info.getS24Junio() + info.getS24Julio() + info.getS24Agosto() + info.getS24Septiembre() + info.getS24Octubre() + info.getS24Noviembre()+ info.getS24Diciembre());
			        	listadoMonitoreoSubtitulo24.add(info);
			        	
			        	estimacionFlujoMonitoreoGlobalPojoSubtitulo24.setEstimacionFlujoMonitoreoPojo(listadoMonitoreoSubtitulo24);
						        
			        	//MODIFICAR LA LISTA ORIGINAL
			        	
			        	List<EstimacionFlujoMonitoreoPojo> lst_monitore_borrar =new ArrayList<EstimacionFlujoMonitoreoPojo>();
			        	List<EstimacionFlujoMonitoreoPojo> lst_monitore_agregar =new ArrayList<EstimacionFlujoMonitoreoPojo>();
			        	for (EstimacionFlujoMonitoreoPojo monitoreo_actual : listadoMonitoreoSubtitulo24) {
			        		for (EstimacionFlujoMonitoreoPojo estimacionFlujoMonitoreoPojo : estimacionFlujoMonitoreoGlobalPojoSubtitulo24Original.getEstimacionFlujoMonitoreoPojo()) {
								
							
			        		if (estimacionFlujoMonitoreoPojo.getId() == monitoreo_actual.getId())
			        		{
			        		    lst_monitore_borrar.add(estimacionFlujoMonitoreoPojo);
			        		    lst_monitore_agregar.add(monitoreo_actual);
			        		}
			        		}
							
						}
			        	
			        	for (EstimacionFlujoMonitoreoPojo estimacionFlujoMonitoreoPojo : lst_monitore_borrar) {
			        		estimacionFlujoMonitoreoGlobalPojoSubtitulo24Original.getEstimacionFlujoMonitoreoPojo().remove(estimacionFlujoMonitoreoPojo);
						}
			        	
			        	for (EstimacionFlujoMonitoreoPojo estimacionFlujoMonitoreoPojo : lst_monitore_agregar) {
			        		estimacionFlujoMonitoreoGlobalPojoSubtitulo24Original.getEstimacionFlujoMonitoreoPojo().add(estimacionFlujoMonitoreoPojo);
						}
			        	
			 
			 }
			 /*
			  * Carga los datos
			  */
			 public void CargarListaSubtitulo24()
				{
					estimacionFlujoMonitoreoGlobalPojoSubtitulo24 = new EstimacionFlujoMonitoreoGlobalPojo();
					
					
					EstimacionFlujoMonitoreoPojo estimacionFlujoMonitoreoPojo = new EstimacionFlujoMonitoreoPojo();
					estimacionFlujoMonitoreoPojo.setAbril(4);
					estimacionFlujoMonitoreoPojo.setAgosto(9);
					estimacionFlujoMonitoreoPojo.setColor("#FFB5B5");
					estimacionFlujoMonitoreoPojo.setComuna("Macul");
					estimacionFlujoMonitoreoPojo.setConvenioMonto(10);
					estimacionFlujoMonitoreoPojo.setConvenioPorcentaje(10);
					estimacionFlujoMonitoreoPojo.setDiciembre(12);
					estimacionFlujoMonitoreoPojo.setEnero(1);
					estimacionFlujoMonitoreoPojo.setEstablecimiento("Establecimiento");
					estimacionFlujoMonitoreoPojo.setFebrero(2);
					estimacionFlujoMonitoreoPojo.setId(1);
					estimacionFlujoMonitoreoPojo.setJulio(7);
					estimacionFlujoMonitoreoPojo.setJunio(6);
					estimacionFlujoMonitoreoPojo.setMarcoMonto(100);
					estimacionFlujoMonitoreoPojo.setMarzo(3);
					estimacionFlujoMonitoreoPojo.setMayo(5);
					estimacionFlujoMonitoreoPojo.setNoviembre(11);
					estimacionFlujoMonitoreoPojo.setOctubre(10);
					estimacionFlujoMonitoreoPojo.setRemesaMonto(15);
					estimacionFlujoMonitoreoPojo.setRemesaPorcentaje(10);
					estimacionFlujoMonitoreoPojo.setSeptiembre(9);
					estimacionFlujoMonitoreoPojo.setServicio("Talcahuano");
					estimacionFlujoMonitoreoPojo.setTotal(100);
					estimacionFlujoMonitoreoPojo.setTransferenciaMonto(100);
					estimacionFlujoMonitoreoPojo.setTransferenciaPorcentaje(10);
					listadoMonitoreoSubtitulo24.add(estimacionFlujoMonitoreoPojo);
					//OTRO REGISTO
					estimacionFlujoMonitoreoPojo = new EstimacionFlujoMonitoreoPojo();
					estimacionFlujoMonitoreoPojo.setAbril(45);
					estimacionFlujoMonitoreoPojo.setAgosto(95);
					estimacionFlujoMonitoreoPojo.setColor("#FFB5B5");
					estimacionFlujoMonitoreoPojo.setComuna("Macul 2");
					estimacionFlujoMonitoreoPojo.setConvenioMonto(150);
					estimacionFlujoMonitoreoPojo.setConvenioPorcentaje(150);
					estimacionFlujoMonitoreoPojo.setDiciembre(152);
					estimacionFlujoMonitoreoPojo.setEnero(51);
					estimacionFlujoMonitoreoPojo.setEstablecimiento("Establecimiento 2");
					estimacionFlujoMonitoreoPojo.setFebrero(52);
					estimacionFlujoMonitoreoPojo.setId(15);
					estimacionFlujoMonitoreoPojo.setJulio(57);
					estimacionFlujoMonitoreoPojo.setJunio(56);
					estimacionFlujoMonitoreoPojo.setMarcoMonto(510);
					estimacionFlujoMonitoreoPojo.setMarzo(53);
					estimacionFlujoMonitoreoPojo.setMayo(55);
					estimacionFlujoMonitoreoPojo.setNoviembre(511);
					estimacionFlujoMonitoreoPojo.setOctubre(510);
					estimacionFlujoMonitoreoPojo.setRemesaMonto(515);
					estimacionFlujoMonitoreoPojo.setRemesaPorcentaje(510);
					estimacionFlujoMonitoreoPojo.setSeptiembre(59);
					estimacionFlujoMonitoreoPojo.setServicio("Iquique");
					estimacionFlujoMonitoreoPojo.setTotal(5100);
					estimacionFlujoMonitoreoPojo.setTransferenciaMonto(5100);
					estimacionFlujoMonitoreoPojo.setTransferenciaPorcentaje(510);
					
					listadoMonitoreoSubtitulo24.add(estimacionFlujoMonitoreoPojo);
					estimacionFlujoMonitoreoGlobalPojoSubtitulo24.setEstimacionFlujoMonitoreoPojo(listadoMonitoreoSubtitulo24);
					//EL FILTRADO DEBE SER IGUAL AL ORIGINAL
					estimacionFlujoMonitoreoGlobalPojoSubtitulo24Original = new EstimacionFlujoMonitoreoGlobalPojo();
					
				
					
					estimacionFlujoMonitoreoGlobalPojoSubtitulo24Original  = estimacionFlujoMonitoreoGlobalPojoSubtitulo24.clone();
					
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
					List<EstimacionFlujoMonitoreoPojo> lst = estimacionFlujoMonitoreoGlobalPojoSubtitulo29Original.getEstimacionFlujoMonitoreoPojo();
					List<EstimacionFlujoMonitoreoPojo> lstAgregar = new ArrayList<EstimacionFlujoMonitoreoPojo>();
					for (EstimacionFlujoMonitoreoPojo estimacionFlujoMonitoreoPojo : lst) {
						if (estimacionFlujoMonitoreoPojo.getServicio().contains(valorComboProgramaSubtitulo29))
						{
							lstAgregar.add(estimacionFlujoMonitoreoPojo);
						}
					}
					//listadoMonitoreoSubtitulo29 = lstAgregar;
					
					estimacionFlujoMonitoreoGlobalPojoSubtitulo29.setEstimacionFlujoMonitoreoPojo(lstAgregar);
					
				}
				
				 /*
				  * Modificación de la celda
				  */
				 public void onCellEditSubtitulo29(CellEditEvent event) {
					 
					 UIColumn col= (UIColumn) event.getColumn();
					 DataTable o=(DataTable) event.getSource();
					
					 EstimacionFlujoMonitoreoPojo info=(EstimacionFlujoMonitoreoPojo)o.getRowData();
					 
					 
				        Object oldValue = event.getOldValue();
				        Object newValue = event.getNewValue();
				         
				        if(newValue != null && !newValue.equals(oldValue)) {
				            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
				            FacesContext.getCurrentInstance().addMessage(null, msg);
				        }
				       
				        //listadoServicios2.add(info);
				        
				        EstimacionFlujoMonitoreoPojo monitore_borrar = new EstimacionFlujoMonitoreoPojo();
				        

				        	for (EstimacionFlujoMonitoreoPojo monitoreo_actual : listadoMonitoreoSubtitulo29) {
				        		
				        		if (info.getId() == monitoreo_actual.getId())
				        		{
				        		    monitore_borrar = monitoreo_actual;
				        		    break;
				        		}
								
							}
				        	
				        	listadoMonitoreoSubtitulo29.remove(monitore_borrar);
				        	//info.setS29Total(info.getS29Enero() + info.getS29Febrero() + info.getS29Marzo() + info.getS29Abril() + info.getS29Mayo() + info.getS29Junio() + info.getS29Julio() + info.getS29Agosto() + info.getS29Septiembre() + info.getS29Octubre() + info.getS29Noviembre()+ info.getS29Diciembre());
				        	listadoMonitoreoSubtitulo29.add(info);
				        	
				        	estimacionFlujoMonitoreoGlobalPojoSubtitulo29.setEstimacionFlujoMonitoreoPojo(listadoMonitoreoSubtitulo29);
							        
				        	//MODIFICAR LA LISTA ORIGINAL
				        	
				        	List<EstimacionFlujoMonitoreoPojo> lst_monitore_borrar =new ArrayList<EstimacionFlujoMonitoreoPojo>();
				        	List<EstimacionFlujoMonitoreoPojo> lst_monitore_agregar =new ArrayList<EstimacionFlujoMonitoreoPojo>();
				        	for (EstimacionFlujoMonitoreoPojo monitoreo_actual : listadoMonitoreoSubtitulo29) {
				        		for (EstimacionFlujoMonitoreoPojo estimacionFlujoMonitoreoPojo : estimacionFlujoMonitoreoGlobalPojoSubtitulo29Original.getEstimacionFlujoMonitoreoPojo()) {
									
								
				        		if (estimacionFlujoMonitoreoPojo.getId() == monitoreo_actual.getId())
				        		{
				        		    lst_monitore_borrar.add(estimacionFlujoMonitoreoPojo);
				        		    lst_monitore_agregar.add(monitoreo_actual);
				        		}
				        		}
								
							}
				        	
				        	for (EstimacionFlujoMonitoreoPojo estimacionFlujoMonitoreoPojo : lst_monitore_borrar) {
				        		estimacionFlujoMonitoreoGlobalPojoSubtitulo29Original.getEstimacionFlujoMonitoreoPojo().remove(estimacionFlujoMonitoreoPojo);
							}
				        	
				        	for (EstimacionFlujoMonitoreoPojo estimacionFlujoMonitoreoPojo : lst_monitore_agregar) {
				        		estimacionFlujoMonitoreoGlobalPojoSubtitulo29Original.getEstimacionFlujoMonitoreoPojo().add(estimacionFlujoMonitoreoPojo);
							}
				        	
				 
				 }
				 /*
				  * Carga los datos
				  */
				 public void CargarListaSubtitulo29()
					{
						estimacionFlujoMonitoreoGlobalPojoSubtitulo29 = new EstimacionFlujoMonitoreoGlobalPojo();
						
						
						EstimacionFlujoMonitoreoPojo estimacionFlujoMonitoreoPojo = new EstimacionFlujoMonitoreoPojo();
						estimacionFlujoMonitoreoPojo.setAbril(4);
						estimacionFlujoMonitoreoPojo.setAgosto(9);
						estimacionFlujoMonitoreoPojo.setColor("#FFB5B5");
						estimacionFlujoMonitoreoPojo.setComuna("Macul");
						estimacionFlujoMonitoreoPojo.setConvenioMonto(10);
						estimacionFlujoMonitoreoPojo.setConvenioPorcentaje(10);
						estimacionFlujoMonitoreoPojo.setDiciembre(12);
						estimacionFlujoMonitoreoPojo.setEnero(1);
						estimacionFlujoMonitoreoPojo.setEstablecimiento("Establecimiento");
						estimacionFlujoMonitoreoPojo.setFebrero(2);
						estimacionFlujoMonitoreoPojo.setId(1);
						estimacionFlujoMonitoreoPojo.setJulio(7);
						estimacionFlujoMonitoreoPojo.setJunio(6);
						estimacionFlujoMonitoreoPojo.setMarcoMonto(100);
						estimacionFlujoMonitoreoPojo.setMarzo(3);
						estimacionFlujoMonitoreoPojo.setMayo(5);
						estimacionFlujoMonitoreoPojo.setNoviembre(11);
						estimacionFlujoMonitoreoPojo.setOctubre(10);
						estimacionFlujoMonitoreoPojo.setRemesaMonto(15);
						estimacionFlujoMonitoreoPojo.setRemesaPorcentaje(10);
						estimacionFlujoMonitoreoPojo.setSeptiembre(9);
						estimacionFlujoMonitoreoPojo.setServicio("Talcahuano");
						estimacionFlujoMonitoreoPojo.setTotal(100);
						estimacionFlujoMonitoreoPojo.setTransferenciaMonto(100);
						estimacionFlujoMonitoreoPojo.setTransferenciaPorcentaje(10);
						listadoMonitoreoSubtitulo29.add(estimacionFlujoMonitoreoPojo);
						//OTRO REGISTO
						estimacionFlujoMonitoreoPojo = new EstimacionFlujoMonitoreoPojo();
						estimacionFlujoMonitoreoPojo.setAbril(45);
						estimacionFlujoMonitoreoPojo.setAgosto(95);
						estimacionFlujoMonitoreoPojo.setColor("#FFB5B5");
						estimacionFlujoMonitoreoPojo.setComuna("Macul 2");
						estimacionFlujoMonitoreoPojo.setConvenioMonto(150);
						estimacionFlujoMonitoreoPojo.setConvenioPorcentaje(150);
						estimacionFlujoMonitoreoPojo.setDiciembre(152);
						estimacionFlujoMonitoreoPojo.setEnero(51);
						estimacionFlujoMonitoreoPojo.setEstablecimiento("Establecimiento 2");
						estimacionFlujoMonitoreoPojo.setFebrero(52);
						estimacionFlujoMonitoreoPojo.setId(15);
						estimacionFlujoMonitoreoPojo.setJulio(57);
						estimacionFlujoMonitoreoPojo.setJunio(56);
						estimacionFlujoMonitoreoPojo.setMarcoMonto(510);
						estimacionFlujoMonitoreoPojo.setMarzo(53);
						estimacionFlujoMonitoreoPojo.setMayo(55);
						estimacionFlujoMonitoreoPojo.setNoviembre(511);
						estimacionFlujoMonitoreoPojo.setOctubre(510);
						estimacionFlujoMonitoreoPojo.setRemesaMonto(515);
						estimacionFlujoMonitoreoPojo.setRemesaPorcentaje(510);
						estimacionFlujoMonitoreoPojo.setSeptiembre(59);
						estimacionFlujoMonitoreoPojo.setServicio("Iquique");
						estimacionFlujoMonitoreoPojo.setTotal(5100);
						estimacionFlujoMonitoreoPojo.setTransferenciaMonto(5100);
						estimacionFlujoMonitoreoPojo.setTransferenciaPorcentaje(510);
						
						listadoMonitoreoSubtitulo29.add(estimacionFlujoMonitoreoPojo);
						estimacionFlujoMonitoreoGlobalPojoSubtitulo29.setEstimacionFlujoMonitoreoPojo(listadoMonitoreoSubtitulo29);
						//EL FILTRADO DEBE SER IGUAL AL ORIGINAL
						estimacionFlujoMonitoreoGlobalPojoSubtitulo29Original = new EstimacionFlujoMonitoreoGlobalPojo();
						
					
						
						estimacionFlujoMonitoreoGlobalPojoSubtitulo29Original  = estimacionFlujoMonitoreoGlobalPojoSubtitulo29.clone();
						
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
						List<EstimacionFlujoMonitoreoPojo> lst = estimacionFlujoMonitoreoGlobalPojoSubtituloPerCapitaOriginal.getEstimacionFlujoMonitoreoPojo();
						List<EstimacionFlujoMonitoreoPojo> lstAgregar = new ArrayList<EstimacionFlujoMonitoreoPojo>();
						for (EstimacionFlujoMonitoreoPojo estimacionFlujoMonitoreoPojo : lst) {
							if (estimacionFlujoMonitoreoPojo.getServicio().contains(valorComboProgramaSubtituloPerCapita))
							{
								lstAgregar.add(estimacionFlujoMonitoreoPojo);
							}
						}
						//listadoMonitoreoSubtituloPerCapita = lstAgregar;
						
						estimacionFlujoMonitoreoGlobalPojoSubtituloPerCapita.setEstimacionFlujoMonitoreoPojo(lstAgregar);
						
					}
					
					 /*
					  * Modificación de la celda
					  */
					 public void onCellEditSubtituloPerCapita(CellEditEvent event) {
						 
						 UIColumn col= (UIColumn) event.getColumn();
						 DataTable o=(DataTable) event.getSource();
						
						 EstimacionFlujoMonitoreoPojo info=(EstimacionFlujoMonitoreoPojo)o.getRowData();
						 
						 
					        Object oldValue = event.getOldValue();
					        Object newValue = event.getNewValue();
					         
					        if(newValue != null && !newValue.equals(oldValue)) {
					            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
					            FacesContext.getCurrentInstance().addMessage(null, msg);
					        }
					       
					        //listadoServicios2.add(info);
					        
					        EstimacionFlujoMonitoreoPojo monitore_borrar = new EstimacionFlujoMonitoreoPojo();
					        

					        	for (EstimacionFlujoMonitoreoPojo monitoreo_actual : listadoMonitoreoSubtituloPerCapita) {
					        		
					        		if (info.getId() == monitoreo_actual.getId())
					        		{
					        		    monitore_borrar = monitoreo_actual;
					        		    break;
					        		}
									
								}
					        	
					        	listadoMonitoreoSubtituloPerCapita.remove(monitore_borrar);
					        	//info.setS24Total(info.getS24Enero() + info.getS24Febrero() + info.getS24Marzo() + info.getS24Abril() + info.getS24Mayo() + info.getS24Junio() + info.getS24Julio() + info.getS24Agosto() + info.getS24Septiembre() + info.getS24Octubre() + info.getS24Noviembre()+ info.getS24Diciembre());
					        	listadoMonitoreoSubtituloPerCapita.add(info);
					        	
					        	estimacionFlujoMonitoreoGlobalPojoSubtituloPerCapita.setEstimacionFlujoMonitoreoPojo(listadoMonitoreoSubtituloPerCapita);
								        
					        	//MODIFICAR LA LISTA ORIGINAL
					        	
					        	List<EstimacionFlujoMonitoreoPojo> lst_monitore_borrar =new ArrayList<EstimacionFlujoMonitoreoPojo>();
					        	List<EstimacionFlujoMonitoreoPojo> lst_monitore_agregar =new ArrayList<EstimacionFlujoMonitoreoPojo>();
					        	for (EstimacionFlujoMonitoreoPojo monitoreo_actual : listadoMonitoreoSubtituloPerCapita) {
					        		for (EstimacionFlujoMonitoreoPojo estimacionFlujoMonitoreoPojo : estimacionFlujoMonitoreoGlobalPojoSubtituloPerCapitaOriginal.getEstimacionFlujoMonitoreoPojo()) {
										
									
					        		if (estimacionFlujoMonitoreoPojo.getId() == monitoreo_actual.getId())
					        		{
					        		    lst_monitore_borrar.add(estimacionFlujoMonitoreoPojo);
					        		    lst_monitore_agregar.add(monitoreo_actual);
					        		}
					        		}
									
								}
					        	
					        	for (EstimacionFlujoMonitoreoPojo estimacionFlujoMonitoreoPojo : lst_monitore_borrar) {
					        		estimacionFlujoMonitoreoGlobalPojoSubtituloPerCapitaOriginal.getEstimacionFlujoMonitoreoPojo().remove(estimacionFlujoMonitoreoPojo);
								}
					        	
					        	for (EstimacionFlujoMonitoreoPojo estimacionFlujoMonitoreoPojo : lst_monitore_agregar) {
					        		estimacionFlujoMonitoreoGlobalPojoSubtituloPerCapitaOriginal.getEstimacionFlujoMonitoreoPojo().add(estimacionFlujoMonitoreoPojo);
								}
					        	
					 
					 }
					 /*
					  * Carga los datos
					  */
					 public void CargarListaSubtituloPerCapita()
						{
							estimacionFlujoMonitoreoGlobalPojoSubtituloPerCapita = new EstimacionFlujoMonitoreoGlobalPojo();
							
							
							EstimacionFlujoMonitoreoPojo estimacionFlujoMonitoreoPojo = new EstimacionFlujoMonitoreoPojo();
							estimacionFlujoMonitoreoPojo.setAbril(4);
							estimacionFlujoMonitoreoPojo.setAgosto(9);
							estimacionFlujoMonitoreoPojo.setColor("#FFB5B5");
							estimacionFlujoMonitoreoPojo.setComuna("Macul");
							estimacionFlujoMonitoreoPojo.setConvenioMonto(10);
							estimacionFlujoMonitoreoPojo.setConvenioPorcentaje(10);
							estimacionFlujoMonitoreoPojo.setDiciembre(12);
							estimacionFlujoMonitoreoPojo.setEnero(1);
							estimacionFlujoMonitoreoPojo.setEstablecimiento("Establecimiento");
							estimacionFlujoMonitoreoPojo.setFebrero(2);
							estimacionFlujoMonitoreoPojo.setId(1);
							estimacionFlujoMonitoreoPojo.setJulio(7);
							estimacionFlujoMonitoreoPojo.setJunio(6);
							estimacionFlujoMonitoreoPojo.setMarcoMonto(100);
							estimacionFlujoMonitoreoPojo.setMarzo(3);
							estimacionFlujoMonitoreoPojo.setMayo(5);
							estimacionFlujoMonitoreoPojo.setNoviembre(11);
							estimacionFlujoMonitoreoPojo.setOctubre(10);
							estimacionFlujoMonitoreoPojo.setRemesaMonto(15);
							estimacionFlujoMonitoreoPojo.setRemesaPorcentaje(10);
							estimacionFlujoMonitoreoPojo.setSeptiembre(9);
							estimacionFlujoMonitoreoPojo.setServicio("Talcahuano");
							estimacionFlujoMonitoreoPojo.setTotal(100);
							estimacionFlujoMonitoreoPojo.setTransferenciaMonto(100);
							estimacionFlujoMonitoreoPojo.setTransferenciaPorcentaje(10);
							listadoMonitoreoSubtituloPerCapita.add(estimacionFlujoMonitoreoPojo);
							//OTRO REGISTO
							estimacionFlujoMonitoreoPojo = new EstimacionFlujoMonitoreoPojo();
							estimacionFlujoMonitoreoPojo.setAbril(45);
							estimacionFlujoMonitoreoPojo.setAgosto(95);
							estimacionFlujoMonitoreoPojo.setColor("#FFB5B5");
							estimacionFlujoMonitoreoPojo.setComuna("Macul 2");
							estimacionFlujoMonitoreoPojo.setConvenioMonto(150);
							estimacionFlujoMonitoreoPojo.setConvenioPorcentaje(150);
							estimacionFlujoMonitoreoPojo.setDiciembre(152);
							estimacionFlujoMonitoreoPojo.setEnero(51);
							estimacionFlujoMonitoreoPojo.setEstablecimiento("Establecimiento 2");
							estimacionFlujoMonitoreoPojo.setFebrero(52);
							estimacionFlujoMonitoreoPojo.setId(15);
							estimacionFlujoMonitoreoPojo.setJulio(57);
							estimacionFlujoMonitoreoPojo.setJunio(56);
							estimacionFlujoMonitoreoPojo.setMarcoMonto(510);
							estimacionFlujoMonitoreoPojo.setMarzo(53);
							estimacionFlujoMonitoreoPojo.setMayo(55);
							estimacionFlujoMonitoreoPojo.setNoviembre(511);
							estimacionFlujoMonitoreoPojo.setOctubre(510);
							estimacionFlujoMonitoreoPojo.setRemesaMonto(515);
							estimacionFlujoMonitoreoPojo.setRemesaPorcentaje(510);
							estimacionFlujoMonitoreoPojo.setSeptiembre(59);
							estimacionFlujoMonitoreoPojo.setServicio("Iquique");
							estimacionFlujoMonitoreoPojo.setTotal(5100);
							estimacionFlujoMonitoreoPojo.setTransferenciaMonto(5100);
							estimacionFlujoMonitoreoPojo.setTransferenciaPorcentaje(510);
							
							listadoMonitoreoSubtituloPerCapita.add(estimacionFlujoMonitoreoPojo);
							estimacionFlujoMonitoreoGlobalPojoSubtituloPerCapita.setEstimacionFlujoMonitoreoPojo(listadoMonitoreoSubtituloPerCapita);
							//EL FILTRADO DEBE SER IGUAL AL ORIGINAL
							estimacionFlujoMonitoreoGlobalPojoSubtituloPerCapitaOriginal = new EstimacionFlujoMonitoreoGlobalPojo();
							
						
							
							estimacionFlujoMonitoreoGlobalPojoSubtituloPerCapitaOriginal  = estimacionFlujoMonitoreoGlobalPojoSubtituloPerCapita.clone();
							
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
		    listadoMonitoreoSubtitulo21 = new ArrayList<EstimacionFlujoMonitoreoPojo>();
		    listadoMonitoreoSubtitulo22 = new ArrayList<EstimacionFlujoMonitoreoPojo>();
		    listadoMonitoreoSubtitulo24 = new ArrayList<EstimacionFlujoMonitoreoPojo>();
		    listadoMonitoreoSubtitulo29 = new ArrayList<EstimacionFlujoMonitoreoPojo>();
		    listadoMonitoreoSubtituloPerCapita = new ArrayList<EstimacionFlujoMonitoreoPojo>();
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

		
		public EstimacionFlujoMonitoreoGlobalPojo getEstimacionFlujoMonitoreoGlobalPojoSubtitulo21Original() {
			return estimacionFlujoMonitoreoGlobalPojoSubtitulo21Original;
		}

		public void setEstimacionFlujoMonitoreoGlobalPojoSubtitulo21Original(
				EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo21Original) {
			this.estimacionFlujoMonitoreoGlobalPojoSubtitulo21Original = estimacionFlujoMonitoreoGlobalPojoSubtitulo21Original;
		}

		public EstimacionFlujoMonitoreoGlobalPojo getEstimacionFlujoMonitoreoGlobalPojoSubtitulo21() {
			return estimacionFlujoMonitoreoGlobalPojoSubtitulo21;
		}

		public void setEstimacionFlujoMonitoreoGlobalPojoSubtitulo21(
				EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo21) {
			this.estimacionFlujoMonitoreoGlobalPojoSubtitulo21 = estimacionFlujoMonitoreoGlobalPojoSubtitulo21;
		}

		public List<EstimacionFlujoMonitoreoPojo> getListadoMonitoreoSubtitulo21() {
			return listadoMonitoreoSubtitulo21;
		}

		public void setListadoMonitoreoSubtitulo21(
				List<EstimacionFlujoMonitoreoPojo> listadoMonitoreoSubtitulo21) {
			this.listadoMonitoreoSubtitulo21 = listadoMonitoreoSubtitulo21;
		}

		public String getValorComboProgramaSubtitulo21() {
			return valorComboProgramaSubtitulo21;
		}

		public void setValorComboProgramaSubtitulo21(
				String valorComboProgramaSubtitulo21) {
			this.valorComboProgramaSubtitulo21 = valorComboProgramaSubtitulo21;
		}

		public String getValorComboServicioSubtitulo21() {
			return valorComboServicioSubtitulo21;
		}

		public void setValorComboServicioSubtitulo21(
				String valorComboServicioSubtitulo21) {
			this.valorComboServicioSubtitulo21 = valorComboServicioSubtitulo21;
		}

		public String getValorComboComunaSubtitulo21() {
			return valorComboComunaSubtitulo21;
		}

		public void setValorComboComunaSubtitulo21(String valorComboComunaSubtitulo21) {
			this.valorComboComunaSubtitulo21 = valorComboComunaSubtitulo21;
		}

		public String getValorComboProgramaSubtitulo22() {
			return valorComboProgramaSubtitulo22;
		}

		public void setValorComboProgramaSubtitulo22(
				String valorComboProgramaSubtitulo22) {
			this.valorComboProgramaSubtitulo22 = valorComboProgramaSubtitulo22;
		}

		public String getValorComboServicioSubtitulo22() {
			return valorComboServicioSubtitulo22;
		}

		public void setValorComboServicioSubtitulo22(
				String valorComboServicioSubtitulo22) {
			this.valorComboServicioSubtitulo22 = valorComboServicioSubtitulo22;
		}

		public String getValorComboComunaSubtitulo22() {
			return valorComboComunaSubtitulo22;
		}

		public void setValorComboComunaSubtitulo22(String valorComboComunaSubtitulo22) {
			this.valorComboComunaSubtitulo22 = valorComboComunaSubtitulo22;
		}

		public EstimacionFlujoMonitoreoGlobalPojo getEstimacionFlujoMonitoreoGlobalPojoSubtitulo22Original() {
			return estimacionFlujoMonitoreoGlobalPojoSubtitulo22Original;
		}

		public void setEstimacionFlujoMonitoreoGlobalPojoSubtitulo22Original(
				EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo22Original) {
			this.estimacionFlujoMonitoreoGlobalPojoSubtitulo22Original = estimacionFlujoMonitoreoGlobalPojoSubtitulo22Original;
		}

		public EstimacionFlujoMonitoreoGlobalPojo getEstimacionFlujoMonitoreoGlobalPojoSubtitulo22() {
			return estimacionFlujoMonitoreoGlobalPojoSubtitulo22;
		}

		public void setEstimacionFlujoMonitoreoGlobalPojoSubtitulo22(
				EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo22) {
			this.estimacionFlujoMonitoreoGlobalPojoSubtitulo22 = estimacionFlujoMonitoreoGlobalPojoSubtitulo22;
		}

		public List<EstimacionFlujoMonitoreoPojo> getListadoMonitoreoSubtitulo22() {
			return listadoMonitoreoSubtitulo22;
		}

		public void setListadoMonitoreoSubtitulo22(
				List<EstimacionFlujoMonitoreoPojo> listadoMonitoreoSubtitulo22) {
			this.listadoMonitoreoSubtitulo22 = listadoMonitoreoSubtitulo22;
		}
	
	
	
	


}
