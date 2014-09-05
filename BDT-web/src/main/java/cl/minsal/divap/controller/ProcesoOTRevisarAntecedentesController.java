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

import minsal.divap.enums.Subtitulo;
import minsal.divap.service.OTService;
import minsal.divap.service.ProgramasService;
import minsal.divap.service.RemesaService;
import minsal.divap.util.Util;
import minsal.divap.vo.ColumnaVO;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.OTRevisarAntecedentesGlobalVO;
import minsal.divap.vo.OTRevisarAntecedentesVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.SubtituloVO;

import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named("procesoOTRevisarAntecedentesController")
@ViewScoped
public class ProcesoOTRevisarAntecedentesController extends AbstractTaskMBean
implements Serializable {
	private static final long serialVersionUID = 8979055329731411696L;
	@Inject private transient Logger log;
	
	@EJB
	private OTService otService;
		
	@EJB
	private ProgramasService programasService;
	
	@EJB
	private RemesaService remesaService;
	
	private Integer idMesActual;
	private Integer idMesSiguiente;

	public Integer getIdMesActual() {
		return idMesActual;
	}

	public void setIdMesActual(Integer idMesActual) {
		this.idMesActual = idMesActual;
	}

	public Integer getIdMesSiguiente() {
		return idMesSiguiente;
	}

	public void setIdMesSiguiente(Integer idMesSiguiente) {
		this.idMesSiguiente = idMesSiguiente;
	}
	
	private boolean mostrarSubtitulo21;
	private boolean mostrarSubtitulo22;
	private boolean mostrarSubtitulo29;
	private boolean mostrarSubtitulo24;
	
	public boolean isMostrarSubtitulo21() {
		return mostrarSubtitulo21;
	}

	public void setMostrarSubtitulo21(boolean mostrarSubtitulo21) {
		this.mostrarSubtitulo21 = mostrarSubtitulo21;
	}

	public boolean isMostrarSubtitulo22() {
		return mostrarSubtitulo22;
	}

	public void setMostrarSubtitulo22(boolean mostrarSubtitulo22) {
		this.mostrarSubtitulo22 = mostrarSubtitulo22;
	}

	public boolean isMostrarSubtitulo29() {
		return mostrarSubtitulo29;
	}

	public void setMostrarSubtitulo29(boolean mostrarSubtitulo29) {
		this.mostrarSubtitulo29 = mostrarSubtitulo29;
	}

	public boolean isMostrarSubtitulo24() {
		return mostrarSubtitulo24;
	}

	public void setMostrarSubtitulo24(boolean mostrarSubtitulo24) {
		this.mostrarSubtitulo24 = mostrarSubtitulo24;
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
		
		this.idProgramaAno = (Integer) getTaskDataVO().getData().get("_idProgramaAno");
		configurarVisibilidadPaneles();
		generarObjetosGlobal();
		cargarServicios();
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
		
		idMesActual = Util.obtenerMes(new Date());
		idMesSiguiente = idMesActual+1;
		String mesActualNombre = Util.obtenerNombreMes(idMesActual);
		String mesSiguienteNombre = Util.obtenerNombreMes(idMesSiguiente);
		
		columns = new ArrayList<ColumnaVO>();
		ColumnaVO  col = new ColumnaVO(mesActualNombre, "Valor", mesActualNombre.toLowerCase());
		columns.add(col);
		col = new ColumnaVO(mesSiguienteNombre, "Valor",mesSiguienteNombre.toLowerCase());
		columns.add(col);
	}
	
	//LISTAS DE DATOS SELECCIONADOS 
	
	//SUBTITULO 21
	 private List<OTRevisarAntecedentesVO> seleccionadosSubtitulo21;
	
	 public List<OTRevisarAntecedentesVO> getSeleccionadosSubtitulo21() {
	        return seleccionadosSubtitulo21;
	    }
	 
	    public void setSeleccionadosSubtitulo21(List<OTRevisarAntecedentesVO> seleccionadosSubtitulo21) {
	        this.seleccionadosSubtitulo21 = seleccionadosSubtitulo21;
	    } 
	
	  //SUBTITULO 22
	 private List<OTRevisarAntecedentesVO> seleccionadosSubtitulo22;
	
	 public List<OTRevisarAntecedentesVO> getSeleccionadosSubtitulo22() {
	        return seleccionadosSubtitulo22;
	    }
	 
	    public void setSeleccionadosSubtitulo22(List<OTRevisarAntecedentesVO> seleccionadosSubtitulo22) {
	        this.seleccionadosSubtitulo22 = seleccionadosSubtitulo22;
	    } 
	 
	  //SUBTITULO 21
	 private List<OTRevisarAntecedentesVO> seleccionadosSubtitulo29;
	
	 public List<OTRevisarAntecedentesVO> getSeleccionadosSubtitulo29() {
	        return seleccionadosSubtitulo29;
	    }
	 
	    public void setSeleccionadosSubtitulo29(List<OTRevisarAntecedentesVO> seleccionadosSubtitulo29) {
	        this.seleccionadosSubtitulo29 = seleccionadosSubtitulo29;
	    } 
		    
	  //SUBTITULO MUNICIPAL
	 private List<OTRevisarAntecedentesVO> seleccionadosMunicipal;
	
	 public List<OTRevisarAntecedentesVO> getSeleccionadosMunicipal() {
	        return seleccionadosMunicipal;
	    }
	 
	    public void setSeleccionadosMunicipal(List<OTRevisarAntecedentesVO> seleccionadosMunicipal) {
	        this.seleccionadosMunicipal = seleccionadosMunicipal;
	    } 
	
	//METODO EDICION GRILLAS
	
	//EDICION SUBTITULO 21
	public void onCellEditSubtitulo21(CellEditEvent event) {
		 
		 DataTable o=(DataTable) event.getSource();
		
		 OTRevisarAntecedentesVO info=(OTRevisarAntecedentesVO)o.getRowData();
		 info.calcularDiferencia(info);
		 
	        Object oldValue = event.getOldValue();
	        Object newValue = event.getNewValue();
	         
	        if(newValue != null && !newValue.equals(oldValue)) {
	            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
	            FacesContext.getCurrentInstance().addMessage(null, msg);
	        }
	       
	        OTRevisarAntecedentesVO otRevisarAntecedentesVO_borrar = new OTRevisarAntecedentesVO();
	        
	        	for (OTRevisarAntecedentesVO otRevisarAntecedentesVO : listadoServiciosSubtitulo21) {
	        		
	        		if (info.getId() == otRevisarAntecedentesVO.getId())
	        		{
	        			otRevisarAntecedentesVO_borrar = otRevisarAntecedentesVO;
	        		    break;
	        		}
				}
	        	
	        	listadoServiciosSubtitulo21.remove(otRevisarAntecedentesVO_borrar);
	        	listadoServiciosSubtitulo21.add(info);        	
	        	
	        	oTRevisarAntecedentesGlobalVOSubtitulo21.setListadoServicios(listadoServiciosSubtitulo21);
	}
	
	
	//EDICION SUBTITULO 22
		public void onCellEditSubtitulo22(CellEditEvent event) {
			 
			 DataTable o=(DataTable) event.getSource();
			
			 OTRevisarAntecedentesVO info=(OTRevisarAntecedentesVO)o.getRowData();
			 info.calcularDiferencia(info);
			 
		        Object oldValue = event.getOldValue();
		        Object newValue = event.getNewValue();
		         
		        if(newValue != null && !newValue.equals(oldValue)) {
		            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
		            FacesContext.getCurrentInstance().addMessage(null, msg);
		        }
		       
		        OTRevisarAntecedentesVO otRevisarAntecedentesVO_borrar = new OTRevisarAntecedentesVO();
		        

		        	for (OTRevisarAntecedentesVO otRevisarAntecedentesVO : listadoServiciosSubtitulo22) {
		        		
		        		if (info.getId() == otRevisarAntecedentesVO.getId())
		        		{
		        			otRevisarAntecedentesVO_borrar = otRevisarAntecedentesVO;
		        		    break;
		        		}
						
					}
		        	
		        	listadoServiciosSubtitulo22.remove(otRevisarAntecedentesVO_borrar);
		        	listadoServiciosSubtitulo22.add(info);        	
		        	
		        	oTRevisarAntecedentesGlobalVOSubtitulo22.setListadoServicios(listadoServiciosSubtitulo22);
		}
		
		//EDICION SUBTITULO 29
		public void onCellEditSubtitulo29(CellEditEvent event) {
			 
			 DataTable o=(DataTable) event.getSource();
			
			 OTRevisarAntecedentesVO info=(OTRevisarAntecedentesVO)o.getRowData();
			 info.calcularDiferencia(info);
			 
		        Object oldValue = event.getOldValue();
		        Object newValue = event.getNewValue();
		         
		        if(newValue != null && !newValue.equals(oldValue)) {
		            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
		            FacesContext.getCurrentInstance().addMessage(null, msg);
		        }
		       
		        OTRevisarAntecedentesVO otRevisarAntecedentesVO_borrar = new OTRevisarAntecedentesVO();
		        

		        	for (OTRevisarAntecedentesVO otRevisarAntecedentesVO : listadoServiciosSubtitulo29) {
		        		
		        		if (info.getId() == otRevisarAntecedentesVO.getId())
		        		{
		        			otRevisarAntecedentesVO_borrar = otRevisarAntecedentesVO;
		        		    break;
		        		}
					}
		        	
		        	listadoServiciosSubtitulo29.remove(otRevisarAntecedentesVO_borrar);
		        	listadoServiciosSubtitulo29.add(info);        	
		        	
		        	oTRevisarAntecedentesGlobalVOSubtitulo29.setListadoServicios(listadoServiciosSubtitulo29);
		}
	
	//EDICION MUNICIPAL
	public void onCellEditMunicipal(CellEditEvent event) {
		 
		 DataTable o=(DataTable) event.getSource();
		
		 OTRevisarAntecedentesVO info=(OTRevisarAntecedentesVO)o.getRowData();
		 info.calcularDiferencia(info);
		 
	        Object oldValue = event.getOldValue();
	        Object newValue = event.getNewValue();
	         
	        if(newValue != null && !newValue.equals(oldValue)) {
	            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
	            FacesContext.getCurrentInstance().addMessage(null, msg);
	        }
	       
	        OTRevisarAntecedentesVO otRevisarAntecedentesVO_borrar = new OTRevisarAntecedentesVO();
	        
	        	for (OTRevisarAntecedentesVO otRevisarAntecedentesVO : listadoServiciosMunicipal) {
	        		
	        		if (info.getId() == otRevisarAntecedentesVO.getId())
	        		{
	        			otRevisarAntecedentesVO_borrar = otRevisarAntecedentesVO;
	        		    break;
	        		}
				}
	        	
	        	listadoServiciosMunicipal.remove(otRevisarAntecedentesVO_borrar);
	        	listadoServiciosMunicipal.add(info);        	
	        	
	        	oTRevisarAntecedentesGlobalVOMunicipal.setListadoServicios(listadoServiciosMunicipal);
	}

	
	 private OTRevisarAntecedentesVO selectedCar;
    public OTRevisarAntecedentesVO getSelectedCar() {
        return selectedCar;
    }
 
    //ID Programa
    
    private ProgramaVO programa;
    
  	public ProgramaVO getPrograma() {
		return programa;
	}

	public void setPrograma(ProgramaVO programa) {
		this.programa = programa;
	}

	private Integer idProgramaAno=1;
  	
  	public Integer getIdProgramaAno() {
  		return idProgramaAno;
  	}

  	public void setIdProgramaAno(Integer idProgramaAno) {
  		this.idProgramaAno = idProgramaAno;
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
	
	public void cargarServicios(){
	
		if(mostrarSubtitulo21)
			CargarListaSubTitulo21();
		if(mostrarSubtitulo22)
			CargarListaSubTitulo22();
		if(mostrarSubtitulo29)
			CargarListaSubTitulo29();
		if(mostrarSubtitulo24)
			CargarListaMunicipal();
	}
	
	private void CargarListaSubTitulo21()
	{
		listadoServiciosSubtitulo21 = new ArrayList<OTRevisarAntecedentesVO>();
		listadoServiciosSubtitulo21.addAll(otService.obtenerListaSubtituloVOPorPrograma(idProgramaAno,Subtitulo.SUBTITULO21.getId()));
		oTRevisarAntecedentesGlobalVOSubtitulo21.setListadoServicios(listadoServiciosSubtitulo21);
	}
	
	private void CargarListaSubTitulo22()
	{
		//SUBTITULO 22
		listadoServiciosSubtitulo22 = new ArrayList<OTRevisarAntecedentesVO>();
		listadoServiciosSubtitulo22.addAll(otService.obtenerListaSubtituloVOPorPrograma(idProgramaAno,Subtitulo.SUBTITULO22.getId()));
		oTRevisarAntecedentesGlobalVOSubtitulo22.setListadoServicios(listadoServiciosSubtitulo22);
	}
	
	private void CargarListaSubTitulo29()
	{
		//SUBTITULO 29
		listadoServiciosSubtitulo29 = new ArrayList<OTRevisarAntecedentesVO>();
		listadoServiciosSubtitulo29.addAll(otService.obtenerListaSubtituloVOPorPrograma(idProgramaAno,Subtitulo.SUBTITULO29.getId()));
		oTRevisarAntecedentesGlobalVOSubtitulo29.setListadoServicios(listadoServiciosSubtitulo29);
	}
	
	private void CargarListaMunicipal()
	{
		//MUNICIPAL
		listadoServiciosMunicipal = new ArrayList<OTRevisarAntecedentesVO>();
		listadoServiciosMunicipal.addAll(otService.obtenerListaSubtituloVOPorPrograma(idProgramaAno,Subtitulo.SUBTITULO24.getId()));
		oTRevisarAntecedentesGlobalVOMunicipal.setListadoServicios(listadoServiciosMunicipal);
	}

	//METODO QUE DEBE GUARDAR LOS DATOS DE LAS LISTAS.
	public void guardarDatos()
	{
		//Guardar datos correspondientes a la lista Subtitulo21
		if(mostrarSubtitulo21)
			if(seleccionadosSubtitulo21 !=null)
				otService.guardarDatosProcesoRevisarOT(seleccionadosSubtitulo21,idProgramaAno,idMesActual,idMesSiguiente,Subtitulo.SUBTITULO21.getId());
		
		//Guardar datos correspondientes a la lista Subtitulo22
		if(mostrarSubtitulo22)
			if(seleccionadosSubtitulo22 !=null)
				otService.guardarDatosProcesoRevisarOT(seleccionadosSubtitulo22,idProgramaAno,idMesActual,idMesSiguiente,Subtitulo.SUBTITULO22.getId());
		
		//Guardar datos correspondientes a la lista Subtitulo29
		if(mostrarSubtitulo29)
			if(seleccionadosSubtitulo29 !=null)
				otService.guardarDatosProcesoRevisarOT(seleccionadosSubtitulo29,idProgramaAno,idMesActual,idMesSiguiente,Subtitulo.SUBTITULO29.getId());
		
		//Guardar datos correspondientes a la lista Municipal
		if(mostrarSubtitulo24)
			if(seleccionadosMunicipal !=null)
				otService.guardarDatosProcesoRevisarOT(seleccionadosMunicipal,idProgramaAno,idMesActual,idMesSiguiente,Subtitulo.SUBTITULO24.getId());
	}
	
	private void configurarVisibilidadPaneles() {
        // TODO [ASAAVEDRA] Completar la visibilidad de los paneles segun los componentes/subtitulos asociados ala programa.
       
        ProgramaVO programa = programasService.getProgramaAnoPorID(idProgramaAno);
        List<ComponentesVO> s = programa.getComponentes();
       
        List<SubtituloVO> lst = new ArrayList<SubtituloVO>();
                     
        for (ComponentesVO componentesVO : s) {
               lst.addAll(componentesVO.getSubtitulos());
        }

        mostrarSubtitulo21 = false;
        mostrarSubtitulo22 = false;
        mostrarSubtitulo24 = false;
        mostrarSubtitulo29 = false;
       
        for (SubtituloVO subtituloVO : lst) {
               if (subtituloVO.getId()== Subtitulo.SUBTITULO21.getId())
                      mostrarSubtitulo21 = true;
               if (subtituloVO.getId()== Subtitulo.SUBTITULO22.getId())
                      mostrarSubtitulo22 = true;
               if (subtituloVO.getId()== Subtitulo.SUBTITULO24.getId())
                      mostrarSubtitulo24 = true;
               if (subtituloVO.getId()== Subtitulo.SUBTITULO29.getId())
                      mostrarSubtitulo29 = true;
        }
	}


	/**
	 * Metodos implementacion BPM 
	 */
	
	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		return parameters;
	}

	@Override
	public String iniciarProceso() {
		
		return null;
	}
	
	// Continua el proceso con el programa seleccionado.
		public void continuarProceso() {

			super.enviar();
		}

}
