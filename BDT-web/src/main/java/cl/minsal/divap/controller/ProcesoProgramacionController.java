package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import minsal.divap.service.DistribucionInicialPercapitaService;
import minsal.divap.service.EstimacionFlujoCajaService;
import minsal.divap.util.Util;
import minsal.divap.vo.ColumnaVO;

import org.apache.log4j.Logger;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

import cl.minsal.divap.pojo.ComponentePojo;
import cl.minsal.divap.pojo.EstimacionFlujoMonitoreoGlobalPojo;
import cl.minsal.divap.pojo.EstimacionFlujoMonitoreoPojo;
import cl.minsal.divap.pojo.MonitoreoPojo;
import cl.minsal.divap.pojo.ProcesosProgramasPojo;
import cl.redhat.bandejaTareas.controller.BaseController;



@Named ( "procesoProgramacionController" ) @ViewScoped public class ProcesoProgramacionController extends BaseController implements
				Serializable {
	private static final long serialVersionUID = 8979055329731411696L;
	@Inject private transient Logger log;
	@Inject FacesContext facesContext;
	/*
	 ********************************** VARIABLES
	 */
	
	/*
	 * SUBTITULO 21
	 */
	
	EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo21;
	List<EstimacionFlujoMonitoreoPojo> listadoMonitoreoSubtitulo21;
	
	//Convenio Remesa
	EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo21ConvenioRemesa;
	List<EstimacionFlujoMonitoreoPojo> listadoMonitoreoSubtitulo21ConvenioRemesa;
	/*
	 *SUBTITULO 22 
	 */
	private String valorComboSubtitulo22;
	EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo22Original;
	EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo22;
	List<EstimacionFlujoMonitoreoPojo> listadoMonitoreoSubtitulo22;
	
	
	//Convenio Remesa
	private String valorComboSubtitulo22Componente;
	private String valorComboSubtitulo22Servicio;
	EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo22OriginalConvenioRemesa;
	EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo22ConvenioRemesa;
	List<EstimacionFlujoMonitoreoPojo> listadoMonitoreoSubtitulo22ConvenioRemesa;
	/*
	 *SUBTITULO 24 
	 */
	private String valorComboSubtitulo24;
	EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo24Original;
	EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo24;
	List<EstimacionFlujoMonitoreoPojo> listadoMonitoreoSubtitulo24;
	
	//Convenio Remesa
		private String valorComboSubtitulo24Componente;
		private String valorComboSubtitulo24Servicio;
		EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo24OriginalConvenioRemesa;
		EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo24ConvenioRemesa;
		List<EstimacionFlujoMonitoreoPojo> listadoMonitoreoSubtitulo24ConvenioRemesa;
		
		
	/*
	 *SUBTITULO 29 
	 */
	private String valorComboSubtitulo29;
	EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo29Original;
	EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo29;
	List<EstimacionFlujoMonitoreoPojo> listadoMonitoreoSubtitulo29;
	
	//Convenio Remesa
			private String valorComboSubtitulo29Componente;
			private String valorComboSubtitulo29Servicio;
			EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo29OriginalConvenioRemesa;
			EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo29ConvenioRemesa;
			List<EstimacionFlujoMonitoreoPojo> listadoMonitoreoSubtitulo29ConvenioRemesa;
			
			/*
			 *SUBTITULO X COMPONENTE 
			 */
			private String valorComboSubtituloComponente;
			EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtituloComponenteOriginal;
			EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtituloComponente;
			List<EstimacionFlujoMonitoreoPojo> listadoMonitoreoSubtituloComponente;
	/*
	 * Transversal
	 */
	
	
	private int mes;
	private String mesActual;
	private int anoActual;
	List<ColumnaVO> columns;
	List<ColumnaVO> columnsInicial;
	@EJB
	private EstimacionFlujoCajaService estimacionFlujoCajaService;
	
	


	private Integer docProgramacion;
	private Integer docPropuesta;
	/*
	 ********************************** FIN VARIABLES
	 */
	
	
	
	@PostConstruct public void init() {
		
		listadoServicios2 = new ArrayList<MonitoreoPojo>();
		log.info("procesoProgramacionController tocado.");
		if (!getSessionBean().isLogged()) {
			log.warn("No hay usuario almacenado en sesion, se redirecciona a pantalla de login");
			try {
				facesContext.getExternalContext().redirect("login.jsf");
			} catch (IOException e) {
				log.error("Error tratando de redireccionar a login por falta de usuario en sesion.", e);
			}
		}
		
		generaProgramas();
		generaServicios();
		cargarListas();
		setMes(8);
		crearColumnasDinamicas();
		//DOCUMENTOS
		this.docProgramacion = estimacionFlujoCajaService.getIdPlantillaProgramacion();
				
		this.docPropuesta = estimacionFlujoCajaService.getIdPlantillaPropuesta();
		
		
		anoActual = 2014;
	}

	/*
	 ********************************************************************************************** SUBTITULO 21
	 */
	/*
	 * Modificacion de la celda
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
	       
	        
	        EstimacionFlujoMonitoreoPojo monitore_borrar = new EstimacionFlujoMonitoreoPojo();
	        

	        	for (EstimacionFlujoMonitoreoPojo monitoreo_actual : listadoMonitoreoSubtitulo21) {
	        		
	        		if (info.getId() == monitoreo_actual.getId())
	        		{
	        		    monitore_borrar = monitoreo_actual;
	        		    break;
	        		}
					
				}
	        	
	        	listadoMonitoreoSubtitulo21.remove(monitore_borrar);
	           	listadoMonitoreoSubtitulo21.add(info);
	        	
	        	estimacionFlujoMonitoreoGlobalPojoSubtitulo21.setEstimacionFlujoMonitoreoPojo(listadoMonitoreoSubtitulo21);
				        
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
		estimacionFlujoMonitoreoPojo.setMarcoMonto(10);
		estimacionFlujoMonitoreoPojo.setMarzo(3);
		estimacionFlujoMonitoreoPojo.setMayo(5);
		estimacionFlujoMonitoreoPojo.setNoviembre(11);
		estimacionFlujoMonitoreoPojo.setOctubre(10);
		estimacionFlujoMonitoreoPojo.setRemesaMonto(15);
		estimacionFlujoMonitoreoPojo.setRemesaPorcentaje(10);
		estimacionFlujoMonitoreoPojo.setSeptiembre(9);
		estimacionFlujoMonitoreoPojo.setServicio("Servicio");
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
		estimacionFlujoMonitoreoPojo.setServicio("Servicio 2");
		estimacionFlujoMonitoreoPojo.setTotal(5100);
		estimacionFlujoMonitoreoPojo.setTransferenciaMonto(5100);
		estimacionFlujoMonitoreoPojo.setTransferenciaPorcentaje(510);
		
		listadoMonitoreoSubtitulo21.add(estimacionFlujoMonitoreoPojo);
		estimacionFlujoMonitoreoGlobalPojoSubtitulo21.setEstimacionFlujoMonitoreoPojo(listadoMonitoreoSubtitulo21);
	}
	
	
	/*
	 ****************************************************************************************** FIN SUBTITULO 21
	 */
	
	
	
	/*
	 ********************************************************************************************** SUBTITULO 21 CONVENIO REMESA
	 */
	/*
	 * Modificacion de la celda
	 */
	public void onCellEditSubtitulo21ConvenioRemesa(CellEditEvent event) {
		 
		 UIColumn col= (UIColumn) event.getColumn();
		 DataTable o=(DataTable) event.getSource();
		
		 EstimacionFlujoMonitoreoPojo info=(EstimacionFlujoMonitoreoPojo)o.getRowData();
		 
		 
	        Object oldValue = event.getOldValue();
	        Object newValue = event.getNewValue();
	         
	        if(newValue != null && !newValue.equals(oldValue)) {
	            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
	            FacesContext.getCurrentInstance().addMessage(null, msg);
	        }
	       
	        
	        EstimacionFlujoMonitoreoPojo monitore_borrar = new EstimacionFlujoMonitoreoPojo();
	        

	        	for (EstimacionFlujoMonitoreoPojo monitoreo_actual : listadoMonitoreoSubtitulo21ConvenioRemesa) {
	        		
	        		if (info.getId() == monitoreo_actual.getId())
	        		{
	        		    monitore_borrar = monitoreo_actual;
	        		    break;
	        		}
					
				}
	        	
	        	listadoMonitoreoSubtitulo21ConvenioRemesa.remove(monitore_borrar);
	           	listadoMonitoreoSubtitulo21ConvenioRemesa.add(info);
	        	
	        	estimacionFlujoMonitoreoGlobalPojoSubtitulo21ConvenioRemesa.setEstimacionFlujoMonitoreoPojo(listadoMonitoreoSubtitulo21ConvenioRemesa);
				        
	    }
	/*
	 * Carga los datos
	 */
	public void CargarListaSubtitulo21ConvenioRemesa()
	{
		estimacionFlujoMonitoreoGlobalPojoSubtitulo21ConvenioRemesa = new EstimacionFlujoMonitoreoGlobalPojo();
		
		
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
		estimacionFlujoMonitoreoPojo.setMarcoMonto(10);
		estimacionFlujoMonitoreoPojo.setMarzo(3);
		estimacionFlujoMonitoreoPojo.setMayo(5);
		estimacionFlujoMonitoreoPojo.setNoviembre(11);
		estimacionFlujoMonitoreoPojo.setOctubre(10);
		estimacionFlujoMonitoreoPojo.setRemesaMonto(15);
		estimacionFlujoMonitoreoPojo.setRemesaPorcentaje(10);
		estimacionFlujoMonitoreoPojo.setSeptiembre(9);
		estimacionFlujoMonitoreoPojo.setServicio("Servicio");
		estimacionFlujoMonitoreoPojo.setTotal(100);
		estimacionFlujoMonitoreoPojo.setTransferenciaMonto(100);
		estimacionFlujoMonitoreoPojo.setTransferenciaPorcentaje(10);
		listadoMonitoreoSubtitulo21ConvenioRemesa.add(estimacionFlujoMonitoreoPojo);
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
		estimacionFlujoMonitoreoPojo.setServicio("Servicio 2");
		estimacionFlujoMonitoreoPojo.setTotal(5100);
		estimacionFlujoMonitoreoPojo.setTransferenciaMonto(5100);
		estimacionFlujoMonitoreoPojo.setTransferenciaPorcentaje(510);
		
		listadoMonitoreoSubtitulo21ConvenioRemesa.add(estimacionFlujoMonitoreoPojo);
		estimacionFlujoMonitoreoGlobalPojoSubtitulo21ConvenioRemesa.setEstimacionFlujoMonitoreoPojo(listadoMonitoreoSubtitulo21ConvenioRemesa);
	}
	
	
	/*
	 ****************************************************************************************** FIN SUBTITULO 21
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
			if (estimacionFlujoMonitoreoPojo.getServicio().contains(valorComboSubtitulo22))
			{
				lstAgregar.add(estimacionFlujoMonitoreoPojo);
			}
		}
		//listadoMonitoreoSubtitulo22 = lstAgregar;
		
		estimacionFlujoMonitoreoGlobalPojoSubtitulo22.setEstimacionFlujoMonitoreoPojo(lstAgregar);
		
	}
	
	 /*
	  * Modificaci髇 de la celda
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
		 ********************************************************************************************* SUBTITULO 22 CONVENIO REMESA
		 */
		/*
		 * Filtra la lista segun los parametros de entrada, en este caso el servicio.
		 */
		public void filtrarSubtitulo22ConvenioRemesa()
		{
			
			//String nuevo = valor;
			List<EstimacionFlujoMonitoreoPojo> lst = estimacionFlujoMonitoreoGlobalPojoSubtitulo22OriginalConvenioRemesa.getEstimacionFlujoMonitoreoPojo();
			List<EstimacionFlujoMonitoreoPojo> lstAgregar = new ArrayList<EstimacionFlujoMonitoreoPojo>();
			for (EstimacionFlujoMonitoreoPojo estimacionFlujoMonitoreoPojo : lst) {
				if (estimacionFlujoMonitoreoPojo.getServicio().contains(valorComboSubtitulo22Componente))
				{
					lstAgregar.add(estimacionFlujoMonitoreoPojo);
				}
			}
			//listadoMonitoreoSubtitulo22 = lstAgregar;
			
			estimacionFlujoMonitoreoGlobalPojoSubtitulo22ConvenioRemesa.setEstimacionFlujoMonitoreoPojo(lstAgregar);
			
		}
		
		 /*
		  * Carga los datos
		  */
		 public void CargarListaSubtitulo22ConvenioRemesa()
			{
				estimacionFlujoMonitoreoGlobalPojoSubtitulo22ConvenioRemesa = new EstimacionFlujoMonitoreoGlobalPojo();
				
				
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
				listadoMonitoreoSubtitulo22ConvenioRemesa.add(estimacionFlujoMonitoreoPojo);
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
				
				listadoMonitoreoSubtitulo22ConvenioRemesa.add(estimacionFlujoMonitoreoPojo);
				estimacionFlujoMonitoreoGlobalPojoSubtitulo22ConvenioRemesa.setEstimacionFlujoMonitoreoPojo(listadoMonitoreoSubtitulo22ConvenioRemesa);
				//EL FILTRADO DEBE SER IGUAL AL ORIGINAL
				estimacionFlujoMonitoreoGlobalPojoSubtitulo22OriginalConvenioRemesa = new EstimacionFlujoMonitoreoGlobalPojo();
				
			
				
				estimacionFlujoMonitoreoGlobalPojoSubtitulo22OriginalConvenioRemesa  = estimacionFlujoMonitoreoGlobalPojoSubtitulo22ConvenioRemesa.clone();
				
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
				if (estimacionFlujoMonitoreoPojo.getServicio().contains(valorComboSubtitulo24))
				{
					lstAgregar.add(estimacionFlujoMonitoreoPojo);
				}
			}
			//listadoMonitoreoSubtitulo24 = lstAgregar;
			
			estimacionFlujoMonitoreoGlobalPojoSubtitulo24.setEstimacionFlujoMonitoreoPojo(lstAgregar);
			
		}
		
		 /*
		  * Modificaci髇 de la celda
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
		  ************************************************************************* FIN SUBTITULO 24 CONVENIO REMESA
		  */
		 
		 /*
			 ********************************************************************************************* SUBTITULO 24 CONVENIO REMESA
			 */
			/*
			 * Filtra la lista segun los parametros de entrada, en este caso el servicio.
			 */
			public void filtrarSubtitulo24ConvenioRemesa()
			{
				
				//String nuevo = valor;
				List<EstimacionFlujoMonitoreoPojo> lst = estimacionFlujoMonitoreoGlobalPojoSubtitulo24OriginalConvenioRemesa.getEstimacionFlujoMonitoreoPojo();
				List<EstimacionFlujoMonitoreoPojo> lstAgregar = new ArrayList<EstimacionFlujoMonitoreoPojo>();
				for (EstimacionFlujoMonitoreoPojo estimacionFlujoMonitoreoPojo : lst) {
					if (estimacionFlujoMonitoreoPojo.getServicio().contains(valorComboSubtitulo24Componente))
					{
						lstAgregar.add(estimacionFlujoMonitoreoPojo);
					}
				}
				//listadoMonitoreoSubtitulo24 = lstAgregar;
				
				estimacionFlujoMonitoreoGlobalPojoSubtitulo24ConvenioRemesa.setEstimacionFlujoMonitoreoPojo(lstAgregar);
				
			}
			
			 /*
			  * Carga los datos
			  */
			 public void CargarListaSubtitulo24ConvenioRemesa()
				{
					estimacionFlujoMonitoreoGlobalPojoSubtitulo24ConvenioRemesa = new EstimacionFlujoMonitoreoGlobalPojo();
					
					
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
					listadoMonitoreoSubtitulo24ConvenioRemesa.add(estimacionFlujoMonitoreoPojo);
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
					
					listadoMonitoreoSubtitulo24ConvenioRemesa.add(estimacionFlujoMonitoreoPojo);
					estimacionFlujoMonitoreoGlobalPojoSubtitulo24ConvenioRemesa.setEstimacionFlujoMonitoreoPojo(listadoMonitoreoSubtitulo24ConvenioRemesa);
					//EL FILTRADO DEBE SER IGUAL AL ORIGINAL
					estimacionFlujoMonitoreoGlobalPojoSubtitulo24OriginalConvenioRemesa = new EstimacionFlujoMonitoreoGlobalPojo();
					
				
					
					estimacionFlujoMonitoreoGlobalPojoSubtitulo24OriginalConvenioRemesa  = estimacionFlujoMonitoreoGlobalPojoSubtitulo24ConvenioRemesa.clone();
					
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
					if (estimacionFlujoMonitoreoPojo.getServicio().contains(valorComboSubtitulo29))
					{
						lstAgregar.add(estimacionFlujoMonitoreoPojo);
					}
				}
				//listadoMonitoreoSubtitulo29 = lstAgregar;
				
				estimacionFlujoMonitoreoGlobalPojoSubtitulo29.setEstimacionFlujoMonitoreoPojo(lstAgregar);
				
			}
			
			 /*
			  * Modificaci髇 de la celda
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
			        	//info.setS24Total(info.getS24Enero() + info.getS24Febrero() + info.getS24Marzo() + info.getS24Abril() + info.getS24Mayo() + info.getS24Junio() + info.getS24Julio() + info.getS24Agosto() + info.getS24Septiembre() + info.getS24Octubre() + info.getS24Noviembre()+ info.getS24Diciembre());
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
				 ********************************************************************************************* SUBTITULO 29 CONVENIO REMESA
				 */
				/*
				 * Filtra la lista segun los parametros de entrada, en este caso el servicio.
				 */
				public void filtrarSubtitulo29ConvenioRemesa()
				{
					
					//String nuevo = valor;
					List<EstimacionFlujoMonitoreoPojo> lst = estimacionFlujoMonitoreoGlobalPojoSubtitulo29OriginalConvenioRemesa.getEstimacionFlujoMonitoreoPojo();
					List<EstimacionFlujoMonitoreoPojo> lstAgregar = new ArrayList<EstimacionFlujoMonitoreoPojo>();
					for (EstimacionFlujoMonitoreoPojo estimacionFlujoMonitoreoPojo : lst) {
						if (estimacionFlujoMonitoreoPojo.getServicio().contains(valorComboSubtitulo29Componente))
						{
							lstAgregar.add(estimacionFlujoMonitoreoPojo);
						}
					}
					//listadoMonitoreoSubtitulo29 = lstAgregar;
					
					estimacionFlujoMonitoreoGlobalPojoSubtitulo29ConvenioRemesa.setEstimacionFlujoMonitoreoPojo(lstAgregar);
					
				}
				
				 /*
				  * Carga los datos
				  */
				 public void CargarListaSubtitulo29ConvenioRemesa()
					{
						estimacionFlujoMonitoreoGlobalPojoSubtitulo29ConvenioRemesa = new EstimacionFlujoMonitoreoGlobalPojo();
						
						
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
						listadoMonitoreoSubtitulo29ConvenioRemesa.add(estimacionFlujoMonitoreoPojo);
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
						
						listadoMonitoreoSubtitulo29ConvenioRemesa.add(estimacionFlujoMonitoreoPojo);
						estimacionFlujoMonitoreoGlobalPojoSubtitulo29ConvenioRemesa.setEstimacionFlujoMonitoreoPojo(listadoMonitoreoSubtitulo29ConvenioRemesa);
						//EL FILTRADO DEBE SER IGUAL AL ORIGINAL
						estimacionFlujoMonitoreoGlobalPojoSubtitulo29OriginalConvenioRemesa = new EstimacionFlujoMonitoreoGlobalPojo();
						
					
						
						estimacionFlujoMonitoreoGlobalPojoSubtitulo29OriginalConvenioRemesa  = estimacionFlujoMonitoreoGlobalPojoSubtitulo29ConvenioRemesa.clone();
						
					}
				
				 
				 /*
				  ************************************************************************* FIN SUBTITULO 29
				  */
	
				 /*
					 ********************************************************************************************* SUBTITULO Componente
					 */
					/*
					 * Filtra la lista segun los parametros de entrada, en este caso el servicio.
					 */
					public void filtrarSubtituloComponente()
					{
						
						//String nuevo = valor;
						List<EstimacionFlujoMonitoreoPojo> lst = estimacionFlujoMonitoreoGlobalPojoSubtituloComponenteOriginal.getEstimacionFlujoMonitoreoPojo();
						List<EstimacionFlujoMonitoreoPojo> lstAgregar = new ArrayList<EstimacionFlujoMonitoreoPojo>();
						for (EstimacionFlujoMonitoreoPojo estimacionFlujoMonitoreoPojo : lst) {
							if (estimacionFlujoMonitoreoPojo.getServicio().contains("Talca"))
							{
								lstAgregar.add(estimacionFlujoMonitoreoPojo);
							}
						}
						//listadoMonitoreoSubtituloComponente = lstAgregar;
						
						estimacionFlujoMonitoreoGlobalPojoSubtituloComponente.setEstimacionFlujoMonitoreoPojo(lstAgregar);
						
					}
					
					 /*
					  * Modificaci髇 de la celda
					  */
					 public void onCellEditSubtituloComponente(CellEditEvent event) {
						 
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
					        

					        	for (EstimacionFlujoMonitoreoPojo monitoreo_actual : listadoMonitoreoSubtituloComponente) {
					        		
					        		if (info.getId() == monitoreo_actual.getId())
					        		{
					        		    monitore_borrar = monitoreo_actual;
					        		    break;
					        		}
									
								}
					        	
					        	listadoMonitoreoSubtituloComponente.remove(monitore_borrar);
					        	//info.setS24Total(info.getS24Enero() + info.getS24Febrero() + info.getS24Marzo() + info.getS24Abril() + info.getS24Mayo() + info.getS24Junio() + info.getS24Julio() + info.getS24Agosto() + info.getS24Septiembre() + info.getS24Octubre() + info.getS24Noviembre()+ info.getS24Diciembre());
					        	listadoMonitoreoSubtituloComponente.add(info);
					        	
					        	estimacionFlujoMonitoreoGlobalPojoSubtituloComponente.setEstimacionFlujoMonitoreoPojo(listadoMonitoreoSubtituloComponente);
								        
					        	//MODIFICAR LA LISTA ORIGINAL
					        	
					        	List<EstimacionFlujoMonitoreoPojo> lst_monitore_borrar =new ArrayList<EstimacionFlujoMonitoreoPojo>();
					        	List<EstimacionFlujoMonitoreoPojo> lst_monitore_agregar =new ArrayList<EstimacionFlujoMonitoreoPojo>();
					        	for (EstimacionFlujoMonitoreoPojo monitoreo_actual : listadoMonitoreoSubtituloComponente) {
					        		for (EstimacionFlujoMonitoreoPojo estimacionFlujoMonitoreoPojo : estimacionFlujoMonitoreoGlobalPojoSubtituloComponenteOriginal.getEstimacionFlujoMonitoreoPojo()) {
										
									
					        		if (estimacionFlujoMonitoreoPojo.getId() == monitoreo_actual.getId())
					        		{
					        		    lst_monitore_borrar.add(estimacionFlujoMonitoreoPojo);
					        		    lst_monitore_agregar.add(monitoreo_actual);
					        		}
					        		}
									
								}
					        	
					        	for (EstimacionFlujoMonitoreoPojo estimacionFlujoMonitoreoPojo : lst_monitore_borrar) {
					        		estimacionFlujoMonitoreoGlobalPojoSubtituloComponenteOriginal.getEstimacionFlujoMonitoreoPojo().remove(estimacionFlujoMonitoreoPojo);
								}
					        	
					        	for (EstimacionFlujoMonitoreoPojo estimacionFlujoMonitoreoPojo : lst_monitore_agregar) {
					        		estimacionFlujoMonitoreoGlobalPojoSubtituloComponenteOriginal.getEstimacionFlujoMonitoreoPojo().add(estimacionFlujoMonitoreoPojo);
								}
					        	
					 
					 }
					 /*
					  * Carga los datos
					  */
					 public void CargarListaSubtituloComponente()
						{
							estimacionFlujoMonitoreoGlobalPojoSubtituloComponente = new EstimacionFlujoMonitoreoGlobalPojo();
							
							
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
							listadoMonitoreoSubtituloComponente.add(estimacionFlujoMonitoreoPojo);
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
							
							listadoMonitoreoSubtituloComponente.add(estimacionFlujoMonitoreoPojo);
							estimacionFlujoMonitoreoGlobalPojoSubtituloComponente.setEstimacionFlujoMonitoreoPojo(listadoMonitoreoSubtituloComponente);
							//EL FILTRADO DEBE SER IGUAL AL ORIGINAL
							estimacionFlujoMonitoreoGlobalPojoSubtituloComponenteOriginal = new EstimacionFlujoMonitoreoGlobalPojo();
							
						
							
							estimacionFlujoMonitoreoGlobalPojoSubtituloComponenteOriginal  = estimacionFlujoMonitoreoGlobalPojoSubtituloComponente.clone();
							
						}
					
					 
					 /*
					  ************************************************************************* FIN SUBTITULO Componente
					  */

	List<ProcesosProgramasPojo> listadoProgramasServicio;
	
	public List<ProcesosProgramasPojo> getListadoProgramasServicio() {
		return listadoProgramasServicio;
	}
	
	public void setListadoProgramasServicio( List<ProcesosProgramasPojo> listadoProgramasServicio ) {
		this.listadoProgramasServicio = listadoProgramasServicio;
	}
	
	public void generaProgramas() {
		listadoProgramasServicio = new ArrayList<ProcesosProgramasPojo>();
		
		ProcesosProgramasPojo p2;
		
		p2 = new ProcesosProgramasPojo();
		p2.setPrograma("VIDA SANA: INTERVENCI脫N EN  FACTORES DE RIESGO DE ENFERMEDADES CR脫NICAS ASOCIADAS A LA MALNUTRICI脫N EN NI脩OS, NI脩AS, ADOLESCENTES, ADULTOS Y MUJERES POSTPARTO");
		p2.setDescripcion("Descripci贸n del Programa de Vida Sana (Municipal)");
		p2.setUrl("divapProcesoProgMonitoreo");
		p2.setEditar(true);
		p2.setProgreso(0.55D);
		p2.setEstado("green");
		p2.setTerminar(true);
		listadoProgramasServicio.add(p2);
		
		p2 = new ProcesosProgramasPojo();
		p2.setPrograma("APOYO A LAS ACCIONES EN EL NIVEL PRIMARIO DE SALUD EN ESTABLECIMIENTOS DEPENDIENTES");
		p2.setDescripcion("Descripci贸n del Programa de Apoyo a las acciones en el nivel primario de Salud (Servicio).");
		p2.setUrl("divapProcesoProgMonitoreo");
		p2.setEditar(false);
		p2.setProgreso(1D);
		p2.setEstado("green");
		p2.setTerminar(false);
		listadoProgramasServicio.add(p2);
		
		p2 = new ProcesosProgramasPojo();
		p2.setPrograma("PILOTO VIDA SANA: ALCOHOL");
		p2.setDescripcion("Descripci贸n del Programa de Vida Sana con Alcohol (Mixto).");
		p2.setUrl("divapProcesoProgMonitoreo");
		p2.setEditar(true);
		p2.setProgreso(0D);
		p2.setEstado("red");
		p2.setTerminar(false);
		listadoProgramasServicio.add(p2);
		
		p2 = new ProcesosProgramasPojo();
		p2.setPrograma("APOYO A LAS ACCIONES EN EL NIVEL PRIMARIO DE SALUD EN ESTABLECIMIENTOS DEPENDIENTES");
		p2.setDescripcion("Descripci贸n del Programa de Apoyo a las Acciones en el nivel Primario (Programa Valores Hist贸ricos Municipal).");
		p2.setUrl("divapProcesoProgMonitoreo");
		p2.setEditar(true);
		p2.setProgreso(0.75D);
		p2.setEstado("green");
		p2.setTerminar(false);
		listadoProgramasServicio.add(p2);
		
		p2 = new ProcesosProgramasPojo();
		p2.setPrograma("CAPACITACI脫N Y FORMACI脫N ATENCI脫N PRIMARIA EN LA RED ASISTENCIAL");
		p2.setDescripcion("Descripci贸n del Programa de Apoyo a las Acciones en el nivel Primario (Programa Valores Hist贸ricos Servicio Salud).");
		p2.setUrl("divapProcesoProgMonitoreo");
		p2.setEditar(true);
		p2.setProgreso(0.75D);
		p2.setEstado("red");
		p2.setTerminar(false);
		listadoProgramasServicio.add(p2);
	}
	
	List<MonitoreoPojo> listadoServicios;
	
	public List<MonitoreoPojo> getListadoServicios() {
		return listadoServicios;
	}
	
	public void setListadoServicios( List<MonitoreoPojo> listadoServicios ) {
		this.listadoServicios = listadoServicios;
	}
	
	
List<MonitoreoPojo> listadoServicios2;
	
	public List<MonitoreoPojo> getListadoServicios2() {
		return listadoServicios2;
	}
	
	public void setListadoServicios2( List<MonitoreoPojo> listadoServicios2 ) {
		this.listadoServicios2 = listadoServicios2;
	}
	
	public void generaServicios(){
		MonitoreoPojo p;
		listadoServicios = new ArrayList<MonitoreoPojo>();
		ComponentePojo c = new ComponentePojo();
		c.setComponenteNombre("Emergencia Dental");
		c.setPesoComponente(0.3f);
		
		p = new MonitoreoPojo();
		p.setEstablecimiento("Centro Comunitario de Salud Familiar Cerro Esmeralda");
		p.setServicio("Metropolitano Oriente");
		p.setComuna("Macul");
		p.setComponente(c);
		p.setId(1L);
		listadoServicios.add(p);
		
		p = new MonitoreoPojo();
		p.setEstablecimiento("Centro Comunitario de Salud Familiar El Boro");
		p.setServicio("Iquique");
		p.setComuna("La Reina");
		p.setComponente(c);
		p.setId(2L);
		listadoServicios.add(p);
		
		p = new MonitoreoPojo();
		p.setEstablecimiento("Cl铆nica Dental M贸vil Simple. Pat. RW6740 (Iquique)");
		p.setServicio("Metropolitano Sur");
		p.setComuna("Puente Alto");
		p.setComponente(c);
		p.setId(3L);
		listadoServicios.add(p);
		
		p = new MonitoreoPojo();
		p.setEstablecimiento("COSAM Dr. Jorge Seguel C谩ceres");
		p.setServicio("Alto Hospicio");
		p.setComuna("Santiago");
		p.setComponente(c);
		p.setId(4L);
		listadoServicios.add(p);
		
		p = new MonitoreoPojo();
		p.setEstablecimiento("COSAM Salvador Allende	");
		p.setServicio("Bio Bio");
		p.setComuna("Providencia");
		p.setComponente(c);
		p.setId(5L);
		listadoServicios.add(p);
	}
	
	/*
	 * GETTER Y SETTER
	 */
	public String getValorComboSubtitulo22() {
		return valorComboSubtitulo22;
	}
	public void setValorComboSubtitulo22(String valorComboSubtitulo22) {
		this.valorComboSubtitulo22 = valorComboSubtitulo22;
	}
	public int getMes() {
		return mes;
	}
	public void setMes(int mes) {
		this.mes = mes;
	}

	public String getMesActual() {
		
		return Util.obtenerNombreMes(getMes());
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
	public String getValorComboSubtitulo24() {
		return valorComboSubtitulo24;
	}


	public void setValorComboSubtitulo24(String valorComboSubtitulo24) {
		this.valorComboSubtitulo24 = valorComboSubtitulo24;
	}


	public EstimacionFlujoMonitoreoGlobalPojo getEstimacionFlujoMonitoreoGlobalPojoSubtitulo24Original() {
		return estimacionFlujoMonitoreoGlobalPojoSubtitulo24Original;
	}


	public void setEstimacionFlujoMonitoreoGlobalPojoSubtitulo24Original(
			EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo24Original) {
		this.estimacionFlujoMonitoreoGlobalPojoSubtitulo24Original = estimacionFlujoMonitoreoGlobalPojoSubtitulo24Original;
	}


	public String getValorComboSubtitulo29() {
		return valorComboSubtitulo29;
	}


	public void setValorComboSubtitulo29(String valorComboSubtitulo29) {
		this.valorComboSubtitulo29 = valorComboSubtitulo29;
	}


	public EstimacionFlujoMonitoreoGlobalPojo getEstimacionFlujoMonitoreoGlobalPojoSubtitulo29Original() {
		return estimacionFlujoMonitoreoGlobalPojoSubtitulo29Original;
	}


	public void setEstimacionFlujoMonitoreoGlobalPojoSubtitulo29Original(
			EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo29Original) {
		this.estimacionFlujoMonitoreoGlobalPojoSubtitulo29Original = estimacionFlujoMonitoreoGlobalPojoSubtitulo29Original;
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
	
	public EstimacionFlujoMonitoreoGlobalPojo getEstimacionFlujoMonitoreoGlobalPojoSubtitulo21ConvenioRemesa() {
		return estimacionFlujoMonitoreoGlobalPojoSubtitulo21ConvenioRemesa;
	}

	public void setEstimacionFlujoMonitoreoGlobalPojoSubtitulo21ConvenioRemesa(
			EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo21ConvenioRemesa) {
		this.estimacionFlujoMonitoreoGlobalPojoSubtitulo21ConvenioRemesa = estimacionFlujoMonitoreoGlobalPojoSubtitulo21ConvenioRemesa;
	}

	public List<EstimacionFlujoMonitoreoPojo> getListadoMonitoreoSubtitulo21ConvenioRemesa() {
		return listadoMonitoreoSubtitulo21ConvenioRemesa;
	}

	public void setListadoMonitoreoSubtitulo21ConvenioRemesa(
			List<EstimacionFlujoMonitoreoPojo> listadoMonitoreoSubtitulo21ConvenioRemesa) {
		this.listadoMonitoreoSubtitulo21ConvenioRemesa = listadoMonitoreoSubtitulo21ConvenioRemesa;
	}

	public List<ColumnaVO> getColumnsInicial() {
		return columnsInicial;
	}

	public void setColumnsInicial(List<ColumnaVO> columnsInicial) {
		this.columnsInicial = columnsInicial;
	}

	public String getValorComboSubtitulo22Componente() {
		return valorComboSubtitulo22Componente;
	}

	public void setValorComboSubtitulo22Componente(
			String valorComboSubtitulo22Componente) {
		this.valorComboSubtitulo22Componente = valorComboSubtitulo22Componente;
	}

	public String getValorComboSubtitulo22Servicio() {
		return valorComboSubtitulo22Servicio;
	}

	public void setValorComboSubtitulo22Servicio(
			String valorComboSubtitulo22Servicio) {
		this.valorComboSubtitulo22Servicio = valorComboSubtitulo22Servicio;
	}

	public EstimacionFlujoMonitoreoGlobalPojo getEstimacionFlujoMonitoreoGlobalPojoSubtitulo22OriginalConvenioRemesa() {
		return estimacionFlujoMonitoreoGlobalPojoSubtitulo22OriginalConvenioRemesa;
	}

	public void setEstimacionFlujoMonitoreoGlobalPojoSubtitulo22OriginalConvenioRemesa(
			EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo22OriginalConvenioRemesa) {
		this.estimacionFlujoMonitoreoGlobalPojoSubtitulo22OriginalConvenioRemesa = estimacionFlujoMonitoreoGlobalPojoSubtitulo22OriginalConvenioRemesa;
	}

	public EstimacionFlujoMonitoreoGlobalPojo getEstimacionFlujoMonitoreoGlobalPojoSubtitulo22ConvenioRemesa() {
		return estimacionFlujoMonitoreoGlobalPojoSubtitulo22ConvenioRemesa;
	}

	public void setEstimacionFlujoMonitoreoGlobalPojoSubtitulo22ConvenioRemesa(
			EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo22ConvenioRemesa) {
		this.estimacionFlujoMonitoreoGlobalPojoSubtitulo22ConvenioRemesa = estimacionFlujoMonitoreoGlobalPojoSubtitulo22ConvenioRemesa;
	}

	public List<EstimacionFlujoMonitoreoPojo> getListadoMonitoreoSubtitulo22ConvenioRemesa() {
		return listadoMonitoreoSubtitulo22ConvenioRemesa;
	}

	public void setListadoMonitoreoSubtitulo22ConvenioRemesa(
			List<EstimacionFlujoMonitoreoPojo> listadoMonitoreoSubtitulo22ConvenioRemesa) {
		this.listadoMonitoreoSubtitulo22ConvenioRemesa = listadoMonitoreoSubtitulo22ConvenioRemesa;
	}

	public String getValorComboSubtitulo24Componente() {
		return valorComboSubtitulo24Componente;
	}

	public void setValorComboSubtitulo24Componente(
			String valorComboSubtitulo24Componente) {
		this.valorComboSubtitulo24Componente = valorComboSubtitulo24Componente;
	}

	public String getValorComboSubtitulo24Servicio() {
		return valorComboSubtitulo24Servicio;
	}

	public void setValorComboSubtitulo24Servicio(
			String valorComboSubtitulo24Servicio) {
		this.valorComboSubtitulo24Servicio = valorComboSubtitulo24Servicio;
	}

	public EstimacionFlujoMonitoreoGlobalPojo getEstimacionFlujoMonitoreoGlobalPojoSubtitulo24OriginalConvenioRemesa() {
		return estimacionFlujoMonitoreoGlobalPojoSubtitulo24OriginalConvenioRemesa;
	}

	public void setEstimacionFlujoMonitoreoGlobalPojoSubtitulo24OriginalConvenioRemesa(
			EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo24OriginalConvenioRemesa) {
		this.estimacionFlujoMonitoreoGlobalPojoSubtitulo24OriginalConvenioRemesa = estimacionFlujoMonitoreoGlobalPojoSubtitulo24OriginalConvenioRemesa;
	}

	public EstimacionFlujoMonitoreoGlobalPojo getEstimacionFlujoMonitoreoGlobalPojoSubtitulo24ConvenioRemesa() {
		return estimacionFlujoMonitoreoGlobalPojoSubtitulo24ConvenioRemesa;
	}

	public void setEstimacionFlujoMonitoreoGlobalPojoSubtitulo24ConvenioRemesa(
			EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo24ConvenioRemesa) {
		this.estimacionFlujoMonitoreoGlobalPojoSubtitulo24ConvenioRemesa = estimacionFlujoMonitoreoGlobalPojoSubtitulo24ConvenioRemesa;
	}

	public List<EstimacionFlujoMonitoreoPojo> getListadoMonitoreoSubtitulo24ConvenioRemesa() {
		return listadoMonitoreoSubtitulo24ConvenioRemesa;
	}

	public void setListadoMonitoreoSubtitulo24ConvenioRemesa(
			List<EstimacionFlujoMonitoreoPojo> listadoMonitoreoSubtitulo24ConvenioRemesa) {
		this.listadoMonitoreoSubtitulo24ConvenioRemesa = listadoMonitoreoSubtitulo24ConvenioRemesa;
	}

	public String getValorComboSubtitulo29Componente() {
		return valorComboSubtitulo29Componente;
	}

	public void setValorComboSubtitulo29Componente(
			String valorComboSubtitulo29Componente) {
		this.valorComboSubtitulo29Componente = valorComboSubtitulo29Componente;
	}

	public String getValorComboSubtitulo29Servicio() {
		return valorComboSubtitulo29Servicio;
	}

	public void setValorComboSubtitulo29Servicio(
			String valorComboSubtitulo29Servicio) {
		this.valorComboSubtitulo29Servicio = valorComboSubtitulo29Servicio;
	}

	public EstimacionFlujoMonitoreoGlobalPojo getEstimacionFlujoMonitoreoGlobalPojoSubtitulo29OriginalConvenioRemesa() {
		return estimacionFlujoMonitoreoGlobalPojoSubtitulo29OriginalConvenioRemesa;
	}

	public void setEstimacionFlujoMonitoreoGlobalPojoSubtitulo29OriginalConvenioRemesa(
			EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo29OriginalConvenioRemesa) {
		this.estimacionFlujoMonitoreoGlobalPojoSubtitulo29OriginalConvenioRemesa = estimacionFlujoMonitoreoGlobalPojoSubtitulo29OriginalConvenioRemesa;
	}

	public EstimacionFlujoMonitoreoGlobalPojo getEstimacionFlujoMonitoreoGlobalPojoSubtitulo29ConvenioRemesa() {
		return estimacionFlujoMonitoreoGlobalPojoSubtitulo29ConvenioRemesa;
	}

	public void setEstimacionFlujoMonitoreoGlobalPojoSubtitulo29ConvenioRemesa(
			EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtitulo29ConvenioRemesa) {
		this.estimacionFlujoMonitoreoGlobalPojoSubtitulo29ConvenioRemesa = estimacionFlujoMonitoreoGlobalPojoSubtitulo29ConvenioRemesa;
	}

	public List<EstimacionFlujoMonitoreoPojo> getListadoMonitoreoSubtitulo29ConvenioRemesa() {
		return listadoMonitoreoSubtitulo29ConvenioRemesa;
	}

	public void setListadoMonitoreoSubtitulo29ConvenioRemesa(
			List<EstimacionFlujoMonitoreoPojo> listadoMonitoreoSubtitulo29ConvenioRemesa) {
		this.listadoMonitoreoSubtitulo29ConvenioRemesa = listadoMonitoreoSubtitulo29ConvenioRemesa;
	}

	public String getValorComboSubtituloComponente() {
		return valorComboSubtituloComponente;
	}

	public void setValorComboSubtituloComponente(
			String valorComboSubtituloComponente) {
		this.valorComboSubtituloComponente = valorComboSubtituloComponente;
	}

	public EstimacionFlujoMonitoreoGlobalPojo getEstimacionFlujoMonitoreoGlobalPojoSubtituloComponenteOriginal() {
		return estimacionFlujoMonitoreoGlobalPojoSubtituloComponenteOriginal;
	}

	public void setEstimacionFlujoMonitoreoGlobalPojoSubtituloComponenteOriginal(
			EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtituloComponenteOriginal) {
		this.estimacionFlujoMonitoreoGlobalPojoSubtituloComponenteOriginal = estimacionFlujoMonitoreoGlobalPojoSubtituloComponenteOriginal;
	}

	public EstimacionFlujoMonitoreoGlobalPojo getEstimacionFlujoMonitoreoGlobalPojoSubtituloComponente() {
		return estimacionFlujoMonitoreoGlobalPojoSubtituloComponente;
	}

	public void setEstimacionFlujoMonitoreoGlobalPojoSubtituloComponente(
			EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojoSubtituloComponente) {
		this.estimacionFlujoMonitoreoGlobalPojoSubtituloComponente = estimacionFlujoMonitoreoGlobalPojoSubtituloComponente;
	}

	public List<EstimacionFlujoMonitoreoPojo> getListadoMonitoreoSubtituloComponente() {
		return listadoMonitoreoSubtituloComponente;
	}

	public void setListadoMonitoreoSubtituloComponente(
			List<EstimacionFlujoMonitoreoPojo> listadoMonitoreoSubtituloComponente) {
		this.listadoMonitoreoSubtituloComponente = listadoMonitoreoSubtituloComponente;
	}
	
	private String docIdDownload;
	public String getDocIdDownload() {
		return docIdDownload;
	}

	public void setDocIdDownload(String docIdDownload) {
		this.docIdDownload = docIdDownload;
	}

	public Integer getDocProgramacion() {
		return docProgramacion;
	}

	public void setDocProgramacion(Integer docProgramacion) {
		this.docProgramacion = docProgramacion;
	}

	public Integer getDocPropuesta() {
		return docPropuesta;
	}

	public void setDocPropuesta(Integer docPropuesta) {
		this.docPropuesta = docPropuesta;
	}

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
	    listadoMonitoreoSubtitulo21ConvenioRemesa = new ArrayList<EstimacionFlujoMonitoreoPojo>();
	    listadoMonitoreoSubtitulo22ConvenioRemesa = new ArrayList<EstimacionFlujoMonitoreoPojo>();
	    listadoMonitoreoSubtitulo24ConvenioRemesa = new ArrayList<EstimacionFlujoMonitoreoPojo>();
	    listadoMonitoreoSubtitulo29ConvenioRemesa = new ArrayList<EstimacionFlujoMonitoreoPojo>();
	    listadoMonitoreoSubtituloComponente = new ArrayList<EstimacionFlujoMonitoreoPojo>();
	    //Obtener desde la base de datos.
	    CargarListaSubtitulo21();
	    CargarListaSubtitulo22();
	    CargarListaSubtitulo24();
	    CargarListaSubtitulo29();
	    CargarListaSubtitulo21ConvenioRemesa();
	    CargarListaSubtitulo22ConvenioRemesa();
	    CargarListaSubtitulo24ConvenioRemesa();
	    CargarListaSubtitulo29ConvenioRemesa();
	    CargarListaSubtituloComponente();
	}
	
	/*Descargar Archivos*/
	public String downloadTemplate() {
		Integer docDownload = Integer.valueOf(Integer
				.parseInt(getDocIdDownload()));
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
		return null;
	}
}
