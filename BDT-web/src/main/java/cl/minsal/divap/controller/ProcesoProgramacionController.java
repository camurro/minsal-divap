package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

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
		
		columns = new ArrayList<ColumnaVO>();
		//ColumnaVO col = new ColumnaVO("Julio","valor");
		//columns.add(col);
		ColumnaVO  col = new ColumnaVO("Agosto", "Valor", "agosto");
		columns.add(col);
		col = new ColumnaVO("Septiembre", "Valor","septiembre");
		columns.add(col);
		col = new ColumnaVO("Octubre", "Valor","octubre");
		columns.add(col);
		col = new ColumnaVO("Noviembre", "Valor","noviembre");
		columns.add(col);
		col = new ColumnaVO("Diciembre", "Valor","diciembre");
		columns.add(col);
		
		
	}
	
	 public void onRowEdit(RowEditEvent event) {
		 ((MonitoreoPojo) event.getObject()).setTotal(100);
	        FacesMessage msg = new FacesMessage("Car Edited", ((MonitoreoPojo) event.getObject()).getServicio());
	        FacesContext.getCurrentInstance().addMessage(null, msg);
	    }
	     
	    public void onRowCancel(RowEditEvent event) {
	        FacesMessage msg = new FacesMessage("Edit Cancelled", ((MonitoreoPojo) event.getObject()).getServicio());
	        FacesContext.getCurrentInstance().addMessage(null, msg);
	    }
	    
	 public void onCellEdit(CellEditEvent event) {
		 
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
	        	
	        	estimacionFlujoMonitoreoGlobalPojo.setEstimacionFlujoMonitoreoPojo(listadoMonitoreoSubtitulo21);
				        
	    }
	 
	public UIComponent findComponent(String id) {

		UIComponent result = null;
		UIComponent root = FacesContext.getCurrentInstance().getViewRoot();
		if (root != null) {
		result = findComponent(root, id);
		}
		return result;

		}

		private UIComponent findComponent(UIComponent root, String id) {

		UIComponent result = null;
		if (root.getId().equals(id))
		return root;

		for (UIComponent child : root.getChildren()) {
		if (child.getId().equals(id)) {
		result = child;
		break;
		}
		result = findComponent(child, id);
		if (result != null)
		break;
		}
		return result;
		}
	
	List<ColumnaVO> columns;
	
	public List<ColumnaVO> getColumns() {
		return columns;
	}

	public void setColumns(List<ColumnaVO> columns) {
		this.columns = columns;
	}
	
	//Listas para llenar las grillas
	List<EstimacionFlujoMonitoreoPojo> listadoMonitoreoSubtitulo21;
	public List<EstimacionFlujoMonitoreoPojo> getListadoMonitoreoSubtitulo21() {
		return listadoMonitoreoSubtitulo21;
	}

	public void setListadoMonitoreoSubtitulo21(
			List<EstimacionFlujoMonitoreoPojo> listadoMonitoreoSubtitulo21) {
		this.listadoMonitoreoSubtitulo21 = listadoMonitoreoSubtitulo21;
	}

	public void cargarListas()
	{
	    listadoMonitoreoSubtitulo21 = new ArrayList<EstimacionFlujoMonitoreoPojo>();
	    //Obtener desde la base de datos.
	    CargarListaSubtitulo21();
	}
	
	EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojo;
	public EstimacionFlujoMonitoreoGlobalPojo getEstimacionFlujoMonitoreoGlobalPojo() {
		return estimacionFlujoMonitoreoGlobalPojo;
	}

	public void setEstimacionFlujoMonitoreoGlobalPojo(
			EstimacionFlujoMonitoreoGlobalPojo estimacionFlujoMonitoreoGlobalPojo) {
		this.estimacionFlujoMonitoreoGlobalPojo = estimacionFlujoMonitoreoGlobalPojo;
	}

	public void CargarListaSubtitulo21()
	{
		estimacionFlujoMonitoreoGlobalPojo = new EstimacionFlujoMonitoreoGlobalPojo();
		
		
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
		estimacionFlujoMonitoreoGlobalPojo.setEstimacionFlujoMonitoreoPojo(listadoMonitoreoSubtitulo21);
	}

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
		p2.setPrograma("VIDA SANA: INTERVENCIÓN EN  FACTORES DE RIESGO DE ENFERMEDADES CRÓNICAS ASOCIADAS A LA MALNUTRICIÓN EN NIÑOS, NIÑAS, ADOLESCENTES, ADULTOS Y MUJERES POSTPARTO");
		p2.setDescripcion("Descripción del Programa de Vida Sana (Municipal)");
		p2.setUrl("divapProcesoProgMonitoreo");
		p2.setEditar(true);
		p2.setProgreso(0.55D);
		p2.setEstado("green");
		p2.setTerminar(true);
		listadoProgramasServicio.add(p2);
		
		p2 = new ProcesosProgramasPojo();
		p2.setPrograma("APOYO A LAS ACCIONES EN EL NIVEL PRIMARIO DE SALUD EN ESTABLECIMIENTOS DEPENDIENTES");
		p2.setDescripcion("Descripción del Programa de Apoyo a las acciones en el nivel primario de Salud (Servicio).");
		p2.setUrl("divapProcesoProgMonitoreo");
		p2.setEditar(false);
		p2.setProgreso(1D);
		p2.setEstado("green");
		p2.setTerminar(false);
		listadoProgramasServicio.add(p2);
		
		p2 = new ProcesosProgramasPojo();
		p2.setPrograma("PILOTO VIDA SANA: ALCOHOL");
		p2.setDescripcion("Descripción del Programa de Vida Sana con Alcohol (Mixto).");
		p2.setUrl("divapProcesoProgMonitoreo");
		p2.setEditar(true);
		p2.setProgreso(0D);
		p2.setEstado("red");
		p2.setTerminar(false);
		listadoProgramasServicio.add(p2);
		
		p2 = new ProcesosProgramasPojo();
		p2.setPrograma("APOYO A LAS ACCIONES EN EL NIVEL PRIMARIO DE SALUD EN ESTABLECIMIENTOS DEPENDIENTES");
		p2.setDescripcion("Descripción del Programa de Apoyo a las Acciones en el nivel Primario (Programa Valores Históricos Municipal).");
		p2.setUrl("divapProcesoProgMonitoreo");
		p2.setEditar(true);
		p2.setProgreso(0.75D);
		p2.setEstado("green");
		p2.setTerminar(false);
		listadoProgramasServicio.add(p2);
		
		p2 = new ProcesosProgramasPojo();
		p2.setPrograma("CAPACITACIÓN Y FORMACIÓN ATENCIÓN PRIMARIA EN LA RED ASISTENCIAL");
		p2.setDescripcion("Descripción del Programa de Apoyo a las Acciones en el nivel Primario (Programa Valores Históricos Servicio Salud).");
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
		p.setEstablecimiento("Clínica Dental Móvil Simple. Pat. RW6740 (Iquique)");
		p.setServicio("Metropolitano Sur");
		p.setComuna("Puente Alto");
		p.setComponente(c);
		p.setId(3L);
		listadoServicios.add(p);
		
		p = new MonitoreoPojo();
		p.setEstablecimiento("COSAM Dr. Jorge Seguel Cáceres");
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
}
