package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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

import minsal.divap.enums.BusinessProcess;
import minsal.divap.service.OTService;
import minsal.divap.vo.AsignacionDistribucionPerCapitaVO;
import minsal.divap.vo.ColumnaVO;
import minsal.divap.vo.OTRevisarAntecedentesGlobalVO;
import minsal.divap.vo.OTRevisarAntecedentesVO;
import minsal.divap.vo.TaskDataVO;
import minsal.divap.vo.TaskVO;

import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

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
	private OTService tratamientoOrdenService;
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

	@EJB
	private OTService  otService;
	private Integer docAsignacionRecursosPercapita;
	private String docIdDownload;
	public String getDocIdDownload() {
		return docIdDownload;
	}
	
	public String downloadTemplate() {
		Integer docDownload = Integer.valueOf(Integer
				.parseInt(getDocIdDownload()));
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
		return null;
	}


	public void setDocIdDownload(String docIdDownload) {
		System.out.println("docIdDownload-->" + docIdDownload);
		this.docIdDownload = docIdDownload;
	}
	
	public Integer getDocAsignacionRecursosPercapita() {
		return docAsignacionRecursosPercapita;
	}
	
	public void setDocAsignacionRecursosPercapita(
			Integer docAsignacionRecursosPercapita) {
		this.docAsignacionRecursosPercapita = docAsignacionRecursosPercapita;
	}
	
	
	/*
	 ********************************************************************************************* SUBTITULO 21
	 */
	/*
	 * Filtra la lista segun los parametros de entrada, en este caso el servicio.
	 */

	OTRevisarAntecedentesGlobalVO oTRevisarAntecedentesGlobalVOSubtitulo21Original;
	public OTRevisarAntecedentesGlobalVO getOTRevisarAntecedentesGlobalVOSubtitulo21Original() {
		return oTRevisarAntecedentesGlobalVOSubtitulo21Original;
	}

	public void setOTRevisarAntecedentesGlobalVOSubtitulo21Original(
			OTRevisarAntecedentesGlobalVO oTRevisarAntecedentesGlobalVOSubtitulo21Original) {
		this.oTRevisarAntecedentesGlobalVOSubtitulo21Original = oTRevisarAntecedentesGlobalVOSubtitulo21Original;
	}
	
	private String valorComboProgramaFiltro;
	public String getValorComboProgramaFiltro() {
		return valorComboProgramaFiltro;
	}

	public void setValorComboProgramaFiltro(
			String valorComboProgramaFiltro) {
		this.valorComboProgramaFiltro = valorComboProgramaFiltro;
	}
	
	
	public void filtrarGrillasTabDetallePrograma()
	{
		filtrarSubtitulo21();
		filtrarSubtitulo22();
		filtrarSubtitulo29();
		filtrarMunicipal();
	}
	
	public void filtrarSubtitulo21()
	{
		List<OTRevisarAntecedentesVO> lst = oTRevisarAntecedentesGlobalVOSubtitulo21Original.getListadoServicios();
		List<OTRevisarAntecedentesVO> lstAgregar = new ArrayList<OTRevisarAntecedentesVO>();
		for (OTRevisarAntecedentesVO oTRevisarAntecedentesVO : lst) {
			if (oTRevisarAntecedentesVO.getServicio().contains(valorComboProgramaFiltro))
			{
				lstAgregar.add(oTRevisarAntecedentesVO);
			}
		}
		oTRevisarAntecedentesGlobalVOSubtitulo21.setListadoServicios(lstAgregar);
		
	}
	
	/*
	 ********************************************************************************************* SUBTITULO 22
	 */
	/*
	 * Filtra la lista segun los parametros de entrada, en este caso el servicio.
	 */

	OTRevisarAntecedentesGlobalVO oTRevisarAntecedentesGlobalVOSubtitulo22Original;
	public OTRevisarAntecedentesGlobalVO getOTRevisarAntecedentesGlobalVOSubtitulo22Original() {
		return oTRevisarAntecedentesGlobalVOSubtitulo22Original;
	}

	public void setOTRevisarAntecedentesGlobalVOSubtitulo22Original(
			OTRevisarAntecedentesGlobalVO oTRevisarAntecedentesGlobalVOSubtitulo22Original) {
		this.oTRevisarAntecedentesGlobalVOSubtitulo22Original = oTRevisarAntecedentesGlobalVOSubtitulo22Original;
	}

	public void filtrarSubtitulo22()
	{
		List<OTRevisarAntecedentesVO> lst = oTRevisarAntecedentesGlobalVOSubtitulo22Original.getListadoServicios();
		List<OTRevisarAntecedentesVO> lstAgregar = new ArrayList<OTRevisarAntecedentesVO>();
		for (OTRevisarAntecedentesVO estimacionFlujoMonitoreoPojo : lst) {
			if (estimacionFlujoMonitoreoPojo.getServicio().contains(valorComboProgramaFiltro))
			{
				lstAgregar.add(estimacionFlujoMonitoreoPojo);
			}
		}
		oTRevisarAntecedentesGlobalVOSubtitulo22.setListadoServicios(lstAgregar);
	}
	
	
	/*
	 ********************************************************************************************* SUBTITULO 29
	 */
	/*
	 * Filtra la lista segun los parametros de entrada, en este caso el servicio.
	 */

	OTRevisarAntecedentesGlobalVO oTRevisarAntecedentesGlobalVOSubtitulo29Original;
	public OTRevisarAntecedentesGlobalVO getOTRevisarAntecedentesGlobalVOSubtitulo29Original() {
		return oTRevisarAntecedentesGlobalVOSubtitulo29Original;
	}

	public void setOTRevisarAntecedentesGlobalVOSubtitulo29Original(
			OTRevisarAntecedentesGlobalVO oTRevisarAntecedentesGlobalVOSubtitulo29Original) {
		this.oTRevisarAntecedentesGlobalVOSubtitulo29Original = oTRevisarAntecedentesGlobalVOSubtitulo29Original;
	}
	
	public void filtrarSubtitulo29()
	{
		List<OTRevisarAntecedentesVO> lst = oTRevisarAntecedentesGlobalVOSubtitulo29Original.getListadoServicios();
		List<OTRevisarAntecedentesVO> lstAgregar = new ArrayList<OTRevisarAntecedentesVO>();
		for (OTRevisarAntecedentesVO estimacionFlujoMonitoreoPojo : lst) {
			if (estimacionFlujoMonitoreoPojo.getServicio().contains(valorComboProgramaFiltro))
			{
				lstAgregar.add(estimacionFlujoMonitoreoPojo);
			}
		}
		oTRevisarAntecedentesGlobalVOSubtitulo29.setListadoServicios(lstAgregar);
	}
	
	/*
	 ********************************************************************************************* MUNICIPAL
	 */
	/*
	 * Filtra la lista segun los parametros de entrada, en este caso el servicio.
	 */

	OTRevisarAntecedentesGlobalVO oTRevisarAntecedentesGlobalVOMunicipalOriginal;
	public OTRevisarAntecedentesGlobalVO getOTRevisarAntecedentesGlobalVOMunicipalOriginal() {
		return oTRevisarAntecedentesGlobalVOMunicipalOriginal;
	}

	public void setOTRevisarAntecedentesGlobalVOMunicipalOriginal(
			OTRevisarAntecedentesGlobalVO oTRevisarAntecedentesGlobalVOMunicipalOriginal) {
		this.oTRevisarAntecedentesGlobalVOMunicipalOriginal = oTRevisarAntecedentesGlobalVOMunicipalOriginal;
	}
	
	public void filtrarMunicipal()
	{
		List<OTRevisarAntecedentesVO> lst = oTRevisarAntecedentesGlobalVOMunicipalOriginal.getListadoServicios();
		List<OTRevisarAntecedentesVO> lstAgregar = new ArrayList<OTRevisarAntecedentesVO>();
		for (OTRevisarAntecedentesVO estimacionFlujoMonitoreoPojo : lst) {
			if (estimacionFlujoMonitoreoPojo.getServicio().contains(valorComboProgramaFiltro))
			{
				lstAgregar.add(estimacionFlujoMonitoreoPojo);
			}
		}
		oTRevisarAntecedentesGlobalVOMunicipal.setListadoServicios(lstAgregar);
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
		
		this.docAsignacionRecursosPercapita = otService.getIdPlantillaRecursosPerCapita();
		
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
	        	
	        	//MODIFICAR LA LISTA ORIGINAL
	        	
	        	List<OTRevisarAntecedentesVO> lst_monitore_borrar =new ArrayList<OTRevisarAntecedentesVO>();
	        	List<OTRevisarAntecedentesVO> lst_monitore_agregar =new ArrayList<OTRevisarAntecedentesVO>();
	        	
	        	for (OTRevisarAntecedentesVO monitoreo_actual : listadoServiciosSubtitulo21) {
	        		
	        		for (OTRevisarAntecedentesVO oTRevisarAntecedentesVO : oTRevisarAntecedentesGlobalVOSubtitulo21Original.getListadoServicios()) {
						
					
	        		if (oTRevisarAntecedentesVO.getId() == monitoreo_actual.getId())
	        		{
	        		    lst_monitore_borrar.add(oTRevisarAntecedentesVO);
	        		    lst_monitore_agregar.add(monitoreo_actual);
	        		}
	        		}
					
				}
	        	
	        	for (OTRevisarAntecedentesVO oTRevisarAntecedentesVO : lst_monitore_borrar) {
	        		oTRevisarAntecedentesGlobalVOSubtitulo21Original.getListadoServicios().remove(oTRevisarAntecedentesVO);
				}
	        	
	        	for (OTRevisarAntecedentesVO oTRevisarAntecedentesVO : lst_monitore_agregar) {
	        		oTRevisarAntecedentesGlobalVOSubtitulo21Original.getListadoServicios().add(oTRevisarAntecedentesVO);
				}	
	}
	
	
	//EDICION SUBTITULO 22
		public void onCellEditSubtitulo22(CellEditEvent event) {
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
		        	
		        	//MODIFICAR LA LISTA ORIGINAL
		        	
		        	List<OTRevisarAntecedentesVO> lst_monitore_borrar =new ArrayList<OTRevisarAntecedentesVO>();
		        	List<OTRevisarAntecedentesVO> lst_monitore_agregar =new ArrayList<OTRevisarAntecedentesVO>();
		        	
		        	for (OTRevisarAntecedentesVO monitoreo_actual : listadoServiciosSubtitulo22) {
		        		
		        		for (OTRevisarAntecedentesVO oTRevisarAntecedentesVO : oTRevisarAntecedentesGlobalVOSubtitulo22Original.getListadoServicios()) {
							
						
		        		if (oTRevisarAntecedentesVO.getId() == monitoreo_actual.getId())
		        		{
		        		    lst_monitore_borrar.add(oTRevisarAntecedentesVO);
		        		    lst_monitore_agregar.add(monitoreo_actual);
		        		}
		        		}
						
					}
		        	
		        	for (OTRevisarAntecedentesVO oTRevisarAntecedentesVO : lst_monitore_borrar) {
		        		oTRevisarAntecedentesGlobalVOSubtitulo22Original.getListadoServicios().remove(oTRevisarAntecedentesVO);
					}
		        	
		        	for (OTRevisarAntecedentesVO oTRevisarAntecedentesVO : lst_monitore_agregar) {
		        		oTRevisarAntecedentesGlobalVOSubtitulo22Original.getListadoServicios().add(oTRevisarAntecedentesVO);
					}
		}
		
		//EDICION SUBTITULO 29
		public void onCellEditSubtitulo29(CellEditEvent event) {
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
		        	
		        	//MODIFICAR LA LISTA ORIGINAL
		        	
		        	List<OTRevisarAntecedentesVO> lst_monitore_borrar =new ArrayList<OTRevisarAntecedentesVO>();
		        	List<OTRevisarAntecedentesVO> lst_monitore_agregar =new ArrayList<OTRevisarAntecedentesVO>();
		        	
		        	for (OTRevisarAntecedentesVO monitoreo_actual : listadoServiciosSubtitulo29) {
		        		
		        		for (OTRevisarAntecedentesVO oTRevisarAntecedentesVO : oTRevisarAntecedentesGlobalVOSubtitulo29Original.getListadoServicios()) {
							
						
		        		if (oTRevisarAntecedentesVO.getId() == monitoreo_actual.getId())
		        		{
		        		    lst_monitore_borrar.add(oTRevisarAntecedentesVO);
		        		    lst_monitore_agregar.add(monitoreo_actual);
		        		}
		        		}
						
					}
		        	
		        	for (OTRevisarAntecedentesVO oTRevisarAntecedentesVO : lst_monitore_borrar) {
		        		oTRevisarAntecedentesGlobalVOSubtitulo29Original.getListadoServicios().remove(oTRevisarAntecedentesVO);
					}
		        	
		        	for (OTRevisarAntecedentesVO oTRevisarAntecedentesVO : lst_monitore_agregar) {
		        		oTRevisarAntecedentesGlobalVOSubtitulo29Original.getListadoServicios().add(oTRevisarAntecedentesVO);
					}
		}
	
	//EDICION MUNICIPAL
	public void onCellEditMunicipal(CellEditEvent event) {
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
	        	
	        	//MODIFICAR LA LISTA ORIGINAL
	        	
	        	List<OTRevisarAntecedentesVO> lst_monitore_borrar =new ArrayList<OTRevisarAntecedentesVO>();
	        	List<OTRevisarAntecedentesVO> lst_monitore_agregar =new ArrayList<OTRevisarAntecedentesVO>();
	        	
	        	for (OTRevisarAntecedentesVO monitoreo_actual : listadoServiciosMunicipal) {
	        		
	        		for (OTRevisarAntecedentesVO oTRevisarAntecedentesVO : oTRevisarAntecedentesGlobalVOMunicipalOriginal.getListadoServicios()) {
						
					
	        		if (oTRevisarAntecedentesVO.getId() == monitoreo_actual.getId())
	        		{
	        		    lst_monitore_borrar.add(oTRevisarAntecedentesVO);
	        		    lst_monitore_agregar.add(monitoreo_actual);
	        		}
	        		}
					
				}
	        	
	        	for (OTRevisarAntecedentesVO oTRevisarAntecedentesVO : lst_monitore_borrar) {
	        		oTRevisarAntecedentesGlobalVOMunicipalOriginal.getListadoServicios().remove(oTRevisarAntecedentesVO);
				}
	        	
	        	for (OTRevisarAntecedentesVO oTRevisarAntecedentesVO : lst_monitore_agregar) {
	        		oTRevisarAntecedentesGlobalVOMunicipalOriginal.getListadoServicios().add(oTRevisarAntecedentesVO);
				}
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
		
		CargarListaSubTitulo21(c);
		CargarListaSubTitulo22(c);
		CargarListaSubTitulo29(c);
		CargarListaMunicipal(c);
	}
	
	private void CargarListaSubTitulo21(ComponentePojo c)
	{
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
				
		//EL FILTRADO DEBE SER IGUAL AL ORIGINAL
		oTRevisarAntecedentesGlobalVOSubtitulo21Original = new OTRevisarAntecedentesGlobalVO();
		oTRevisarAntecedentesGlobalVOSubtitulo21Original  = oTRevisarAntecedentesGlobalVOSubtitulo21.clone();
	}
	
	private void CargarListaSubTitulo22(ComponentePojo c)
	{
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
		
		//EL FILTRADO DEBE SER IGUAL AL ORIGINAL
		oTRevisarAntecedentesGlobalVOSubtitulo22Original = new OTRevisarAntecedentesGlobalVO();
		oTRevisarAntecedentesGlobalVOSubtitulo22Original  = oTRevisarAntecedentesGlobalVOSubtitulo22.clone();
	}
	
	private void CargarListaSubTitulo29(ComponentePojo c)
	{
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
		//EL FILTRADO DEBE SER IGUAL AL ORIGINAL
		oTRevisarAntecedentesGlobalVOSubtitulo29Original = new OTRevisarAntecedentesGlobalVO();
		oTRevisarAntecedentesGlobalVOSubtitulo29Original  = oTRevisarAntecedentesGlobalVOSubtitulo29.clone();
	}
	
	private void CargarListaMunicipal(ComponentePojo c)
	{
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
		
		//EL FILTRADO DEBE SER IGUAL AL ORIGINAL
		oTRevisarAntecedentesGlobalVOMunicipalOriginal = new OTRevisarAntecedentesGlobalVO();
		oTRevisarAntecedentesGlobalVOMunicipalOriginal  = oTRevisarAntecedentesGlobalVOMunicipal.clone();
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
