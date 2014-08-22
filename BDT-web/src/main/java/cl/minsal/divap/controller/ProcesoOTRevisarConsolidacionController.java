package cl.minsal.divap.controller;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

import minsal.divap.doc.GeneradorOficioConsulta;
import minsal.divap.doc.GeneradorWord;
import minsal.divap.enums.BusinessProcess;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.excel.GeneradorExcel;
import minsal.divap.excel.impl.AsignacionRecursosPercapitaSheetExcel;
import minsal.divap.service.AlfrescoService;
import minsal.divap.service.TratamientoOrdenService;
import minsal.divap.vo.AsignacionDistribucionPerCapitaVO;
import minsal.divap.vo.BaseVO;
import minsal.divap.vo.BodyVO;
import minsal.divap.vo.ColumnaVO;
import minsal.divap.vo.DocumentoVO;
import minsal.divap.vo.OTRevisarAntecedentesGlobalVO;
import minsal.divap.vo.OTRevisarAntecedentesVO;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.TaskDataVO;
import minsal.divap.vo.TaskVO;
import minsal.divap.vo.VariacionPoblacionVO;
import cl.minsal.divap.pojo.ComponentePojo;
import cl.minsal.divap.pojo.ProcesosProgramasPojo;
import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named("procesoOTRevisarConsolidacionController")
@ViewScoped
public class ProcesoOTRevisarConsolidacionController extends AbstractTaskMBean
implements Serializable {
	private static final long serialVersionUID = 8979055329731411696L;
	@Inject private transient Logger log;

	@EJB
	private TratamientoOrdenService tratamientoOrdenService;
	private List<AsignacionDistribucionPerCapitaVO> antecendentesComunaCalculado;
	
	private boolean existeProgramasPendientes;
	
	
	public boolean isExisteProgramasPendientes() {
		
		boolean existeProgramasPendientes = false;
		
		for (ProcesosProgramasPojo programasPojo : listadoProgramasServicio) {
			if(programasPojo.getEstado() == "red")
			{
				existeProgramasPendientes = true;
			}
		}
		return existeProgramasPendientes;
	}

	@PostConstruct
	public void init() {
		
		log.info("procesoOTRevisarAntecedentesController tocado.");
		if (!getSessionBean().isLogged()) {
			log.warn("No hay usuario almacenado en sesion, se redirecciona a pantalla de login");
			try {
				facesContext.getExternalContext().redirect("login.jsf");
			} catch (IOException e) {
				log.error("Error tratando de redireccionar a login por falta de usuario en sesion.", e);
			}
		}
		generarObjetosGlobal();
		generaProgramas();
		generaServicios();
		
		generarColumnasDinamicas();
	}
	


	private void generarObjetosGlobal() {
		// TODO Auto-generated method stub
		
		//Objeto global para el tab Subtitulo 21
		if(oTRevisarAntecedentesGlobalVOSubtitulo21 ==null)
		{
			oTRevisarAntecedentesGlobalVOSubtitulo21 = new OTRevisarAntecedentesGlobalVO();
		}
		
		//Objeto global para el tab Subtitulo 22
		if(oTRevisarAntecedentesGlobalVOSubtitulo22 ==null)
		{
			oTRevisarAntecedentesGlobalVOSubtitulo22 = new OTRevisarAntecedentesGlobalVO();
		}
		//Objeto global para el tab Subtitulo 29
		if(oTRevisarAntecedentesGlobalVOSubtitulo29 ==null)
		{
			oTRevisarAntecedentesGlobalVOSubtitulo29 = new OTRevisarAntecedentesGlobalVO();
		}
		
		//Objeto global para el tab Municipal
		if(oTRevisarAntecedentesGlobalVOMunicipal ==null)
		{
			oTRevisarAntecedentesGlobalVOMunicipal = new OTRevisarAntecedentesGlobalVO();
		}
	}

	private void generarColumnasDinamicas() {
		// TODO Auto-generated method stub
		columns = new ArrayList<ColumnaVO>();
		ColumnaVO  col = new ColumnaVO("Abril", "Valor", "abril");
		columns.add(col);
		col = new ColumnaVO("Septiembre", "Valor","septiembre");
		columns.add(col);
//		col = new ColumnaVO("Octubre", "Valor","octubre");
//		columns.add(col);
//		col = new ColumnaVO("Noviembre", "Valor","noviembre");
//		columns.add(col);
//		col = new ColumnaVO("Diciembre", "Valor","diciembre");
//		columns.add(col);
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
		//p2.setUrl("divapProcesoProgMonitoreo");
		p2.setUrl("divapProcesoOTUsuario");
		p2.setEditar(true);
		p2.setProgreso(0.55D);
		p2.setEstado("green");
		p2.setTerminar(true);
		listadoProgramasServicio.add(p2);
		
		p2 = new ProcesosProgramasPojo();
		p2.setPrograma("APOYO A LAS ACCIONES EN EL NIVEL PRIMARIO DE SALUD EN ESTABLECIMIENTOS DEPENDIENTES");
		p2.setDescripcion("Descripción del Programa de Apoyo a las acciones en el nivel primario de Salud (Servicio).");
		//p2.setUrl("divapProcesoProgMonitoreo");
		p2.setUrl("divapProcesoOTUsuario");
		p2.setEditar(false);
		p2.setProgreso(1D);
		p2.setEstado("green");
		p2.setTerminar(false);
		listadoProgramasServicio.add(p2);
		
		p2 = new ProcesosProgramasPojo();
		p2.setPrograma("PILOTO VIDA SANA: ALCOHOL");
		p2.setDescripcion("Descripción del Programa de Vida Sana con Alcohol (Mixto).");
		//p2.setUrl("divapProcesoProgMonitoreo");
		p2.setUrl("divapProcesoOTUsuario");
		p2.setEditar(true);
		p2.setProgreso(0D);
		p2.setEstado("green");
		p2.setTerminar(false);
		listadoProgramasServicio.add(p2);
		
		p2 = new ProcesosProgramasPojo();
		p2.setPrograma("APOYO A LAS ACCIONES EN EL NIVEL PRIMARIO DE SALUD EN ESTABLECIMIENTOS DEPENDIENTES");
		p2.setDescripcion("Descripción del Programa de Apoyo a las Acciones en el nivel Primario (Programa Valores Históricos Municipal).");
		//p2.setUrl("divapProcesoProgMonitoreo");
		p2.setUrl("divapProcesoOTUsuario");
		p2.setEditar(true);
		p2.setProgreso(0.75D);
		p2.setEstado("green");
		p2.setTerminar(false);
		listadoProgramasServicio.add(p2);
		
		p2 = new ProcesosProgramasPojo();
		p2.setPrograma("CAPACITACIÓN Y FORMACIÓN ATENCIÓN PRIMARIA EN LA RED ASISTENCIAL");
		p2.setDescripcion("Descripción del Programa de Apoyo a las Acciones en el nivel Primario (Programa Valores Históricos Servicio Salud).");
		//p2.setUrl("divapProcesoProgMonitoreo");
		p2.setUrl("divapProcesoOTUsuario");
		p2.setEditar(true);
		p2.setProgreso(0.75D);
		p2.setEstado("green");
		p2.setTerminar(false);
		listadoProgramasServicio.add(p2);
	}
		 
	//METODO EDICION GRILLAS
	
	//EDICION SUBTITULO 21
	public void onCellEditSubtitulo21(CellEditEvent event) {
		 
		 UIColumn col= (UIColumn) event.getColumn();
		 DataTable o=(DataTable) event.getSource();
		
		 OTRevisarAntecedentesVO info=(OTRevisarAntecedentesVO)o.getRowData();
		 
		 
	        Object oldValue = event.getOldValue();
	        Object newValue = event.getNewValue();
	         
	        if(newValue != null && !newValue.equals(oldValue)) {
	            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
	            FacesContext.getCurrentInstance().addMessage(null, msg);
	        }
	       
	        OTRevisarAntecedentesVO monitore_borrar = new OTRevisarAntecedentesVO();
	        

	        	for (OTRevisarAntecedentesVO monitoreo_actual : listadoServiciosSubtitulo21) {
	        		
	        		if (info.getId() == monitoreo_actual.getId())
	        		{
	        		    monitore_borrar = monitoreo_actual;
	        		    break;
	        		}
					
				}
	        	
	        	listadoServiciosSubtitulo21.remove(monitore_borrar);
	        	listadoServiciosSubtitulo21.add(info);        	
	        	
	        	oTRevisarAntecedentesGlobalVOSubtitulo21.setListadoServicios(listadoServiciosSubtitulo21);
	}
	
	
	//EDICION SUBTITULO 22
		public void onCellEditSubtitulo22(CellEditEvent event) {
			 
			 UIColumn col= (UIColumn) event.getColumn();
			 DataTable o=(DataTable) event.getSource();
			
			 OTRevisarAntecedentesVO info=(OTRevisarAntecedentesVO)o.getRowData();
			 
			 
		        Object oldValue = event.getOldValue();
		        Object newValue = event.getNewValue();
		         
		        if(newValue != null && !newValue.equals(oldValue)) {
		            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
		            FacesContext.getCurrentInstance().addMessage(null, msg);
		        }
		       
		        OTRevisarAntecedentesVO monitore_borrar = new OTRevisarAntecedentesVO();
		        

		        	for (OTRevisarAntecedentesVO monitoreo_actual : listadoServiciosSubtitulo22) {
		        		
		        		if (info.getId() == monitoreo_actual.getId())
		        		{
		        		    monitore_borrar = monitoreo_actual;
		        		    break;
		        		}
						
					}
		        	
		        	listadoServiciosSubtitulo22.remove(monitore_borrar);
		        	listadoServiciosSubtitulo22.add(info);        	
		        	
		        	oTRevisarAntecedentesGlobalVOSubtitulo22.setListadoServicios(listadoServiciosSubtitulo22);
		}
		
		//EDICION SUBTITULO 29
		public void onCellEditSubtitulo29(CellEditEvent event) {
			 
			 UIColumn col= (UIColumn) event.getColumn();
			 DataTable o=(DataTable) event.getSource();
			
			 OTRevisarAntecedentesVO info=(OTRevisarAntecedentesVO)o.getRowData();
			 
			 
		        Object oldValue = event.getOldValue();
		        Object newValue = event.getNewValue();
		         
		        if(newValue != null && !newValue.equals(oldValue)) {
		            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
		            FacesContext.getCurrentInstance().addMessage(null, msg);
		        }
		       
		        OTRevisarAntecedentesVO monitore_borrar = new OTRevisarAntecedentesVO();
		        

		        	for (OTRevisarAntecedentesVO monitoreo_actual : listadoServiciosSubtitulo29) {
		        		
		        		if (info.getId() == monitoreo_actual.getId())
		        		{
		        		    monitore_borrar = monitoreo_actual;
		        		    break;
		        		}
						
					}
		        	
		        	listadoServiciosSubtitulo29.remove(monitore_borrar);
		        	listadoServiciosSubtitulo29.add(info);        	
		        	
		        	oTRevisarAntecedentesGlobalVOSubtitulo29.setListadoServicios(listadoServiciosSubtitulo29);
		}
	
	//EDICION MUNICIPAL
	public void onCellEditMunicipal(CellEditEvent event) {
		 
		 UIColumn col= (UIColumn) event.getColumn();
		 DataTable o=(DataTable) event.getSource();
		
		 OTRevisarAntecedentesVO info=(OTRevisarAntecedentesVO)o.getRowData();
		 
		 
	        Object oldValue = event.getOldValue();
	        Object newValue = event.getNewValue();
	         
	        if(newValue != null && !newValue.equals(oldValue)) {
	            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
	            FacesContext.getCurrentInstance().addMessage(null, msg);
	        }
	       
	        OTRevisarAntecedentesVO monitore_borrar = new OTRevisarAntecedentesVO();
	        
	        	for (OTRevisarAntecedentesVO monitoreo_actual : listadoServiciosMunicipal) {
	        		
	        		if (info.getId() == monitoreo_actual.getId())
	        		{
	        		    monitore_borrar = monitoreo_actual;
	        		    break;
	        		}
				}
	        	
	        	listadoServiciosMunicipal.remove(monitore_borrar);
	        	listadoServiciosMunicipal.add(info);        	
	        	
	        	oTRevisarAntecedentesGlobalVOMunicipal.setListadoServicios(listadoServiciosMunicipal);
	}

	
	public void buscar() {
//		System.out.println("buscar--> servicioSeleccionado="+servicioSeleccionado+" comunaSeleccionada="+comunaSeleccionada);
//		if((servicioSeleccionado == null || servicioSeleccionado.trim().isEmpty()) && (comunaSeleccionada == null || comunaSeleccionada.trim().isEmpty()) ){
//			FacesMessage msg = new FacesMessage("Debe seleccionar al menos un filtro antes de realizar la búsqueda");
//			FacesContext.getCurrentInstance().addMessage(null, msg);
//		}else{
//			
//			
			this.antecendentesComunaCalculado = tratamientoOrdenService.findDatos(1);
			
			
//		}
//		System.out.println("fin buscar-->");
	}

	
	/**
	 * SUBTITULO 21
	 * Objeto Global que contiene las listas y el total general
	 */
	OTRevisarAntecedentesGlobalVO oTRevisarAntecedentesGlobalVOSubtitulo21;
	
	public OTRevisarAntecedentesGlobalVO getoTRevisarAntecedentesGlobalVOSubtitulo21() {
		return oTRevisarAntecedentesGlobalVOSubtitulo21;
	}

	public void setoTRevisarAntecedentesGlobalVOSubtitulo21(
			OTRevisarAntecedentesGlobalVO oTRevisarAntecedentesGlobalVOSubtitulo21) {
		this.oTRevisarAntecedentesGlobalVOSubtitulo21 = oTRevisarAntecedentesGlobalVOSubtitulo21;
	}
	
	
	/**
	 * SUBTITULO 22
	 * Objeto Global que contiene las listas y el total general
	 */
	OTRevisarAntecedentesGlobalVO oTRevisarAntecedentesGlobalVOSubtitulo22;
	
	public OTRevisarAntecedentesGlobalVO getoTRevisarAntecedentesGlobalVOSubtitulo22() {
		return oTRevisarAntecedentesGlobalVOSubtitulo22;
	}

	public void setoTRevisarAntecedentesGlobalVOSubtitulo22(
			OTRevisarAntecedentesGlobalVO oTRevisarAntecedentesGlobalVOSubtitulo22) {
		this.oTRevisarAntecedentesGlobalVOSubtitulo22 = oTRevisarAntecedentesGlobalVOSubtitulo22;
	}
	
	/**
	 * SUBTITULO 29
	 * Objeto Global que contiene las listas y el total general
	 */
	OTRevisarAntecedentesGlobalVO oTRevisarAntecedentesGlobalVOSubtitulo29;

	public OTRevisarAntecedentesGlobalVO getoTRevisarAntecedentesGlobalVOSubtitulo29() {
		return oTRevisarAntecedentesGlobalVOSubtitulo29;
	}

	public void setoTRevisarAntecedentesGlobalVOSubtitulo29(
			OTRevisarAntecedentesGlobalVO oTRevisarAntecedentesGlobalVOSubtitulo29) {
		this.oTRevisarAntecedentesGlobalVOSubtitulo29 = oTRevisarAntecedentesGlobalVOSubtitulo29;
	}

	/**
	 * MUNICIPAL
	 * Objeto Global que contiene las listas y el total general del tab Ref. Municipal
	 */
	OTRevisarAntecedentesGlobalVO oTRevisarAntecedentesGlobalVOMunicipal;
	
	public OTRevisarAntecedentesGlobalVO getoTRevisarAntecedentesGlobalVOMunicipal() {
		return oTRevisarAntecedentesGlobalVOMunicipal;
	}

	public void setoTRevisarAntecedentesGlobalVOMunicipal(
			OTRevisarAntecedentesGlobalVO oTRevisarAntecedentesGlobalVOMunicipal) {
		this.oTRevisarAntecedentesGlobalVOMunicipal = oTRevisarAntecedentesGlobalVOMunicipal;
	}

	Integer diaActual = minsal.divap.util.Util.obtenerDia(new Date());
	
	public Integer getDiaActual() {
		return diaActual;
	}

	
	/**
	 * Lista de columnas dinamicas
	 */
	List<ColumnaVO> columns;
	
	public List<ColumnaVO> getColumns() {
		return columns;
	}

	public void setColumns(List<ColumnaVO> columns) {
		this.columns = columns;
	}
	
	/**
	 * Lista correspodiente al subtitulo 21
	 */
	List<OTRevisarAntecedentesVO> listadoServiciosSubtitulo21;
	
	public List<OTRevisarAntecedentesVO> getListadoServiciosSubtitulo21() {
		return listadoServiciosSubtitulo21;
	}
	
	public void setListadoServiciosSubtitulo21(List<OTRevisarAntecedentesVO> listadoServiciosSubtitulo21 ) {
		this.listadoServiciosSubtitulo21 = listadoServiciosSubtitulo21;
	}
	
	/**
	 * Lista correspodiente al subtitulo 22
	 */
	List<OTRevisarAntecedentesVO> listadoServiciosSubtitulo22;
	
	public List<OTRevisarAntecedentesVO> getListadoServiciosSubtitulo22() {
		return listadoServiciosSubtitulo22;
	}
	
	public void setListadoServiciosSubtitulo22(List<OTRevisarAntecedentesVO> listadoServiciosSubtitulo22) {
		this.listadoServiciosSubtitulo22 = listadoServiciosSubtitulo22;
	}
	
	/**
	 * Lista correspodiente al subtitulo 29
	 */
	List<OTRevisarAntecedentesVO> listadoServiciosSubtitulo29;
	
	public List<OTRevisarAntecedentesVO> getListadoServiciosSubtitulo29() {
		return listadoServiciosSubtitulo29;
	}
	
	public void setListadoServiciosSubtitulo29(List<OTRevisarAntecedentesVO> listadoServiciosSubtitulo29) {
		this.listadoServiciosSubtitulo29 = listadoServiciosSubtitulo21;
	}
	
	
	/**
	 * Lista correspodiente al municipal
	 */
	List<OTRevisarAntecedentesVO> listadoServiciosMunicipal;
	
	public List<OTRevisarAntecedentesVO> getListadoServiciosMunicipal() {
		return listadoServiciosMunicipal;
	}
	
	public void setListadoServiciosMunicipal(List<OTRevisarAntecedentesVO> listadoServiciosMunicipal) {
		this.listadoServiciosMunicipal = listadoServiciosMunicipal;
	}
	
	public void generaServicios(){

		ComponentePojo c = new ComponentePojo();
		c.setComponenteNombre("Emergencia Dental");
		c.setPesoComponente(0.3f);
		
		//SUBTITULO 21
		OTRevisarAntecedentesVO p;
		listadoServiciosSubtitulo21 = new ArrayList<OTRevisarAntecedentesVO>();
		p = new OTRevisarAntecedentesVO();
		p.setEstablecimiento("Centro Comunitario de Salud Familiar Cerro Esmeralda");
		p.setServicio("Metropolitano Oriente");
		p.setComuna("Macul");
		p.setAbrilRemesa09(222L);
		p.setComponente(c);
		p.setId(1L);
		listadoServiciosSubtitulo21.add(p);
		
		p = new OTRevisarAntecedentesVO();
		p.setEstablecimiento("Centro Comunitario de Salud Familiar El Boro");
		p.setServicio("Iquique");
		p.setComuna("La Reina");
		p.setAbrilRemesa09(333L);
		p.setComponente(c);
		p.setId(2L);
		listadoServiciosSubtitulo21.add(p);
		
		oTRevisarAntecedentesGlobalVOSubtitulo21.setListadoServicios(listadoServiciosSubtitulo21);
		
		//SUBTITULO 22
		OTRevisarAntecedentesVO p22;
		listadoServiciosSubtitulo22 = new ArrayList<OTRevisarAntecedentesVO>();
		p22 = new OTRevisarAntecedentesVO();
		p22.setEstablecimiento("Centro Comunitario de Salud Familiar Cerro Esmeralda");
		p22.setServicio("Metropolitano Oriente");
		p22.setComuna("Macul");
		p22.setAbrilRemesa09(555L);
		p22.setComponente(c);
		p22.setId(1L);
		listadoServiciosSubtitulo22.add(p22);
		
		p22 = new OTRevisarAntecedentesVO();
		p22.setEstablecimiento("Centro Comunitario de Salud Familiar El Boro");
		p22.setServicio("Iquique");
		p22.setComuna("La Reina");
		p22.setAbrilRemesa09(999L);
		p22.setComponente(c);
		p22.setId(2L);
		listadoServiciosSubtitulo22.add(p22);
		
		oTRevisarAntecedentesGlobalVOSubtitulo22.setListadoServicios(listadoServiciosSubtitulo22);
		
		//SUBTITULO 29
		OTRevisarAntecedentesVO p29;
		listadoServiciosSubtitulo29 = new ArrayList<OTRevisarAntecedentesVO>();
		p29 = new OTRevisarAntecedentesVO();
		p29.setEstablecimiento("Centro Comunitario de Salud Familiar Cerro Esmeralda");
		p29.setServicio("Metropolitano Oriente");
		p29.setComuna("Macul");
		p29.setAbrilRemesa09(234L);
		p29.setComponente(c);
		p29.setId(1L);
		listadoServiciosSubtitulo29.add(p29);
		
		p29 = new OTRevisarAntecedentesVO();
		p29.setEstablecimiento("Centro Comunitario de Salud Familiar El Boro");
		p29.setServicio("Iquique");
		p29.setComuna("La Reina");
		p29.setAbrilRemesa09(182L);
		p29.setComponente(c);
		p29.setId(2L);
		listadoServiciosSubtitulo29.add(p29);
		
		oTRevisarAntecedentesGlobalVOSubtitulo29.setListadoServicios(listadoServiciosSubtitulo29);

		//MUNICIPAL
		OTRevisarAntecedentesVO pMunicipal;
		listadoServiciosMunicipal = new ArrayList<OTRevisarAntecedentesVO>();
		pMunicipal = new OTRevisarAntecedentesVO();
		pMunicipal.setEstablecimiento("PAC");
		pMunicipal.setServicio("Metropolitano Oriente");
		pMunicipal.setComuna("Macul");
		pMunicipal.setAbrilRemesa09(1200L);
		pMunicipal.setComponente(c);
		pMunicipal.setId(1L);
		listadoServiciosMunicipal.add(pMunicipal);
		
		pMunicipal = new OTRevisarAntecedentesVO();
		pMunicipal.setEstablecimiento("DEHESA");
		pMunicipal.setServicio("Iquique");
		pMunicipal.setComuna("La Reina");
		pMunicipal.setAbrilRemesa09(2900L);
		pMunicipal.setComponente(c);
		pMunicipal.setId(2L);
		listadoServiciosMunicipal.add(pMunicipal);
		oTRevisarAntecedentesGlobalVOMunicipal.setListadoServicios(listadoServiciosMunicipal);
		
	}
	
	/**
	 * Metodos implementacion BPM 
	 */
	
	
	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("solicitaReparo_", "no");
		parameters.put("idProcesoTramitacionOrden_", 12);
		return parameters;
	}

	@Override
	public String iniciarProceso() {
		String success = "bandejaTareas";
		Long procId = iniciarProceso(BusinessProcess.OTCONSOLIDADOR);
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
	

}
