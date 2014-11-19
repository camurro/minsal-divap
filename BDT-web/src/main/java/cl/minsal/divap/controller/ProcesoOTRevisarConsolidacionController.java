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
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import minsal.divap.enums.EstadosProgramas;
import minsal.divap.enums.Subtitulo;
import minsal.divap.service.OTService;
import minsal.divap.service.ProgramasService;
import minsal.divap.util.Util;
import minsal.divap.vo.ColumnaVO;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.OTResumenDependienteServicioVO;
import minsal.divap.vo.OTResumenConsolidadoFonasaVO;
import minsal.divap.vo.OTRevisarAntecedentesGlobalVO;
import minsal.divap.vo.OTRevisarAntecedentesVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.SubtituloVO;

import org.apache.log4j.Logger;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named("procesoOTRevisarConsolidacionController")
@ViewScoped
public class ProcesoOTRevisarConsolidacionController extends AbstractTaskMBean
implements Serializable {
	private static final long serialVersionUID = 8979055329731411696L;
	@Inject private transient Logger log;
	@EJB
	private OTService tratamientoOrdenService;
	@EJB
	private ProgramasService programasService;	
	@EJB
	private OTService  otService;
	private Integer idArchivoResumenConsolidadoFonasa;
	private String docIdDownload;
	private boolean existeProgramasPendientes;
	private Integer idMesActual;
	private Integer idMesSiguiente;
	private Integer idProgramaAno=1;
	private Integer idProgramaAnoResumenTotal=1;
	private boolean mostrarSubtitulo21;
	private boolean mostrarSubtitulo22;
	private boolean mostrarSubtitulo29;
	private boolean mostrarSubtitulo24;
	private boolean mostrarSubtitulo21ResumenTotal;
	private boolean mostrarSubtitulo22ResumenTotal;
	private boolean mostrarSubtitulo29ResumenTotal;
	private boolean mostrarSubtitulo24ResumenTotal;
	OTRevisarAntecedentesGlobalVO oTRevisarAntecedentesGlobalVOSubtitulo21Original;
	OTRevisarAntecedentesGlobalVO oTRevisarAntecedentesGlobalVOSubtitulo22Original;
	OTRevisarAntecedentesGlobalVO oTRevisarAntecedentesGlobalVOSubtitulo29Original;
	OTRevisarAntecedentesGlobalVO oTRevisarAntecedentesGlobalVOMunicipalOriginal;
	private Integer valorComboProgramaFiltro;
	private Integer valorComboProgramaFiltroResumenTotal;
	private Integer valorComboProgramaRechazo;
	private HashMap<String,Integer> listaProgramaCombo = new HashMap <String,Integer>();  
	private String observacionReparo;
	private Integer idOrdenTransferencia=1;
	
	public Integer getIdOrdenTransferencia() {
		return idOrdenTransferencia;
	}

	public void setIdOrdenTransferencia(Integer idOrdenTransferencia) {
		this.idOrdenTransferencia = idOrdenTransferencia;
	}


	/**
	 * SUBTITULO 21
	 * Objeto Global que contiene las listas y el total general
	 */
	OTRevisarAntecedentesGlobalVO oTRevisarAntecedentesGlobalVOSubtitulo21;
	
	/**
	 * SUBTITULO 22
	 * Objeto Global que contiene las listas y el total general
	 */
	OTRevisarAntecedentesGlobalVO oTRevisarAntecedentesGlobalVOSubtitulo22;
	/**
	 * SUBTITULO 29
	 * Objeto Global que contiene las listas y el total general
	 */
	OTRevisarAntecedentesGlobalVO oTRevisarAntecedentesGlobalVOSubtitulo29;
	/**
	 * MUNICIPAL
	 * Objeto Global que contiene las listas y el total general del tab Ref. Municipal
	 */
	OTRevisarAntecedentesGlobalVO oTRevisarAntecedentesGlobalVOMunicipal;
	/**
	 * Lista de columnas dinamicas
	 */
	List<ColumnaVO> columns;
	Integer diaActual = minsal.divap.util.Util.obtenerDia(new Date());
	/**
	 * Lista correspodiente al subtitulo 21
	 */
	List<OTRevisarAntecedentesVO> listadoServiciosSubtitulo21;
	
	/**
	 * Lista correspodiente al subtitulo 22
	 */
	List<OTRevisarAntecedentesVO> listadoServiciosSubtitulo22;
	/**
	 * Lista correspodiente al subtitulo 29
	 */
	List<OTRevisarAntecedentesVO> listadoServiciosSubtitulo29;
	/**
	 * Lista correspodiente al municipal
	 */
	List<OTRevisarAntecedentesVO> listadoServiciosMunicipal;
	List<ProgramaVO> listadoProgramaVO;

	public String getDocIdDownload() {
		return docIdDownload;
	}
	
	public boolean isExisteProgramasPendientes() {
		
		boolean existeProgramasPendientes = false;
		
		for (ProgramaVO programaVO : listadoProgramaVO) {
			if(programaVO.getEstado().getId() == EstadosProgramas.ENCURSO.getId() || 
			   programaVO.getEstado().getId() == EstadosProgramas.SININICIAR.getId())
			{
				existeProgramasPendientes = true;
			}
		}
		return existeProgramasPendientes;
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
	
	public Integer getIdArchivoResumenConsolidadoFonasa() {
		return idArchivoResumenConsolidadoFonasa;
	}
	
	public void setIdArchivoResumenConsolidadoFonasa(
			Integer idArchivoResumenConsolidadoFonasa) {
		this.idArchivoResumenConsolidadoFonasa = idArchivoResumenConsolidadoFonasa;
	}
	
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
	
  	public Integer getIdProgramaAno() {
  		return idProgramaAno;
  	}

  	public void setIdProgramaAno(Integer idProgramaAno) {
  		this.idProgramaAno = idProgramaAno;
  	}
	
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
	
	public boolean isMostrarSubtitulo21ResumenTotal() {
		return mostrarSubtitulo21ResumenTotal;
	}

	public void setMostrarSubtitulo21ResumenTotal(
			boolean mostrarSubtitulo21ResumenTotal) {
		this.mostrarSubtitulo21ResumenTotal = mostrarSubtitulo21ResumenTotal;
	}

	public boolean isMostrarSubtitulo22ResumenTotal() {
		return mostrarSubtitulo22ResumenTotal;
	}

	public void setMostrarSubtitulo22ResumenTotal(
			boolean mostrarSubtitulo22ResumenTotal) {
		this.mostrarSubtitulo22ResumenTotal = mostrarSubtitulo22ResumenTotal;
	}

	public boolean isMostrarSubtitulo29ResumenTotal() {
		return mostrarSubtitulo29ResumenTotal;
	}

	public void setMostrarSubtitulo29ResumenTotal(
			boolean mostrarSubtitulo29ResumenTotal) {
		this.mostrarSubtitulo29ResumenTotal = mostrarSubtitulo29ResumenTotal;
	}

	public boolean isMostrarSubtitulo24ResumenTotal() {
		return mostrarSubtitulo24ResumenTotal;
	}

	public void setMostrarSubtitulo24ResumenTotal(
			boolean mostrarSubtitulo24ResumenTotal) {
		this.mostrarSubtitulo24ResumenTotal = mostrarSubtitulo24ResumenTotal;
	}

	public OTRevisarAntecedentesGlobalVO getOTRevisarAntecedentesGlobalVOSubtitulo21Original() {
		return oTRevisarAntecedentesGlobalVOSubtitulo21Original;
	}

	public void setOTRevisarAntecedentesGlobalVOSubtitulo21Original(
			OTRevisarAntecedentesGlobalVO oTRevisarAntecedentesGlobalVOSubtitulo21Original) {
		this.oTRevisarAntecedentesGlobalVOSubtitulo21Original = oTRevisarAntecedentesGlobalVOSubtitulo21Original;
	}
	
	public Integer getValorComboProgramaFiltro() {
		return valorComboProgramaFiltro;
	}

	public void setValorComboProgramaFiltro(
			Integer valorComboProgramaFiltro) {
		this.valorComboProgramaFiltro = valorComboProgramaFiltro;
	}
	
	public Integer getValorComboProgramaFiltroResumenTotal() {
		return valorComboProgramaFiltroResumenTotal;
	}

	public void setValorComboProgramaFiltroResumenTotal(
			Integer valorComboProgramaFiltroResumenTotal) {
		this.valorComboProgramaFiltroResumenTotal = valorComboProgramaFiltroResumenTotal;
	}
	
	public Integer getValorComboProgramaRechazo() {
		return valorComboProgramaRechazo;
	}

	public void setValorComboProgramaRechazo(Integer valorComboProgramaRechazo) {
		this.valorComboProgramaRechazo = valorComboProgramaRechazo;
	}

	public void setListaProgramaCombo(HashMap<String,Integer> listaProgramaCombo) {
		this.listaProgramaCombo = listaProgramaCombo;
	}

	public  Map<String,Integer> getListaProgramaCombo() {
      
		for (ProgramaVO programaVO : listadoProgramaVO) {
			
			if(valorComboProgramaFiltro==null)
				valorComboProgramaFiltro = programaVO.getId();
			
			if(valorComboProgramaFiltroResumenTotal==null)
				valorComboProgramaFiltroResumenTotal = programaVO.getId();
			
			listaProgramaCombo.put(programaVO.getNombre(),programaVO.getId());
		}
		return listaProgramaCombo;
    }

	
	public String getObservacionReparo() {
		return observacionReparo;
	}

	public void setObservacionReparo(String observacionReparo) {
		this.observacionReparo = observacionReparo;
	}

	public OTRevisarAntecedentesGlobalVO getOTRevisarAntecedentesGlobalVOSubtitulo22Original() {
		return oTRevisarAntecedentesGlobalVOSubtitulo22Original;
	}

	public void setOTRevisarAntecedentesGlobalVOSubtitulo22Original(
			OTRevisarAntecedentesGlobalVO oTRevisarAntecedentesGlobalVOSubtitulo22Original) {
		this.oTRevisarAntecedentesGlobalVOSubtitulo22Original = oTRevisarAntecedentesGlobalVOSubtitulo22Original;
	}

	public OTRevisarAntecedentesGlobalVO getOTRevisarAntecedentesGlobalVOSubtitulo29Original() {
		return oTRevisarAntecedentesGlobalVOSubtitulo29Original;
	}

	public void setOTRevisarAntecedentesGlobalVOSubtitulo29Original(
			OTRevisarAntecedentesGlobalVO oTRevisarAntecedentesGlobalVOSubtitulo29Original) {
		this.oTRevisarAntecedentesGlobalVOSubtitulo29Original = oTRevisarAntecedentesGlobalVOSubtitulo29Original;
	}

	public OTRevisarAntecedentesGlobalVO getOTRevisarAntecedentesGlobalVOMunicipalOriginal() {
		return oTRevisarAntecedentesGlobalVOMunicipalOriginal;
	}

	public void setOTRevisarAntecedentesGlobalVOMunicipalOriginal(
			OTRevisarAntecedentesGlobalVO oTRevisarAntecedentesGlobalVOMunicipalOriginal) {
		this.oTRevisarAntecedentesGlobalVOMunicipalOriginal = oTRevisarAntecedentesGlobalVOMunicipalOriginal;
	}

	public OTRevisarAntecedentesGlobalVO getoTRevisarAntecedentesGlobalVOSubtitulo21() {
		return oTRevisarAntecedentesGlobalVOSubtitulo21;
	}

	public void setoTRevisarAntecedentesGlobalVOSubtitulo21(
			OTRevisarAntecedentesGlobalVO oTRevisarAntecedentesGlobalVOSubtitulo21) {
		this.oTRevisarAntecedentesGlobalVOSubtitulo21 = oTRevisarAntecedentesGlobalVOSubtitulo21;
	}

	public OTRevisarAntecedentesGlobalVO getoTRevisarAntecedentesGlobalVOSubtitulo22() {
		return oTRevisarAntecedentesGlobalVOSubtitulo22;
	}

	public void setoTRevisarAntecedentesGlobalVOSubtitulo22(
			OTRevisarAntecedentesGlobalVO oTRevisarAntecedentesGlobalVOSubtitulo22) {
		this.oTRevisarAntecedentesGlobalVOSubtitulo22 = oTRevisarAntecedentesGlobalVOSubtitulo22;
	}
	
	public OTRevisarAntecedentesGlobalVO getoTRevisarAntecedentesGlobalVOSubtitulo29() {
		return oTRevisarAntecedentesGlobalVOSubtitulo29;
	}

	public void setoTRevisarAntecedentesGlobalVOSubtitulo29(
			OTRevisarAntecedentesGlobalVO oTRevisarAntecedentesGlobalVOSubtitulo29) {
		this.oTRevisarAntecedentesGlobalVOSubtitulo29 = oTRevisarAntecedentesGlobalVOSubtitulo29;
	}

	public OTRevisarAntecedentesGlobalVO getoTRevisarAntecedentesGlobalVOMunicipal() {
		return oTRevisarAntecedentesGlobalVOMunicipal;
	}

	public void setoTRevisarAntecedentesGlobalVOMunicipal(
			OTRevisarAntecedentesGlobalVO oTRevisarAntecedentesGlobalVOMunicipal) {
		this.oTRevisarAntecedentesGlobalVOMunicipal = oTRevisarAntecedentesGlobalVOMunicipal;
	}

	public Integer getDiaActual() {
		return diaActual;
	}

	public List<ColumnaVO> getColumns() {
		return columns;
	}

	public void setColumns(List<ColumnaVO> columns) {
		this.columns = columns;
	}
	
	public List<OTRevisarAntecedentesVO> getListadoServiciosSubtitulo21() {
		return listadoServiciosSubtitulo21;
	}
	
	public void setListadoServiciosSubtitulo21(List<OTRevisarAntecedentesVO> listadoServiciosSubtitulo21 ) {
		this.listadoServiciosSubtitulo21 = listadoServiciosSubtitulo21;
	}

	public List<OTRevisarAntecedentesVO> getListadoServiciosSubtitulo22() {
		return listadoServiciosSubtitulo22;
	}
	
	public void setListadoServiciosSubtitulo22(List<OTRevisarAntecedentesVO> listadoServiciosSubtitulo22) {
		this.listadoServiciosSubtitulo22 = listadoServiciosSubtitulo22;
	}

	public List<OTRevisarAntecedentesVO> getListadoServiciosSubtitulo29() {
		return listadoServiciosSubtitulo29;
	}
	
	public void setListadoServiciosSubtitulo29(List<OTRevisarAntecedentesVO> listadoServiciosSubtitulo29) {
		this.listadoServiciosSubtitulo29 = listadoServiciosSubtitulo21;
	}

	public List<OTRevisarAntecedentesVO> getListadoServiciosMunicipal() {
		return listadoServiciosMunicipal;
	}
	
	public void setListadoServiciosMunicipal(List<OTRevisarAntecedentesVO> listadoServiciosMunicipal) {
		this.listadoServiciosMunicipal = listadoServiciosMunicipal;
	}
	
	public List<ProgramaVO> getListadoProgramaVO() {
		return listadoProgramaVO;
	}
	
	public void setProgramaVO( List<ProgramaVO> listadoProgramaVO ) {
		this.listadoProgramaVO = listadoProgramaVO;
	}
	
	//TODO INIT() INICIO CONTROLLER
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
	    idOrdenTransferencia = otService.crearObjetoOrdenTransferencia(getSessionBean().getUsername());

		cargarListadoProgramaVOPorUsuario();
		getListaProgramaCombo();
		configurarVisibilidadPaneles();
		configurarVisibilidadPanelesResumenTotal();
		generarObjetosGlobal();
		cargarServicios();
		cargarListaResumenConsolidadoFonasa();
		cargarListaResumenTotal();
		generarColumnasDinamicas();
		
		crearArchivoResumenConsolidadorFonasa();
		
	}
	
	private void cargarListadoProgramaVOPorUsuario()
	{
		listadoProgramaVO = programasService.getProgramasByUser("cmurillo");
	}
	
	private void generarObjetosGlobal() {
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
	
	//METODO GRILLAS
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
	
	private void configurarVisibilidadPanelesResumenTotal() {
        // TODO [ASAAVEDRA] Completar la visibilidad de los paneles segun los componentes/subtitulos asociados ala programa.
        ProgramaVO programa = programasService.getProgramaAnoPorID(idProgramaAnoResumenTotal);
        List<ComponentesVO> s = programa.getComponentes();
       
        List<SubtituloVO> lst = new ArrayList<SubtituloVO>();
                     
        for (ComponentesVO componentesVO : s) {
               lst.addAll(componentesVO.getSubtitulos());
        }

        mostrarSubtitulo21ResumenTotal = false;
        mostrarSubtitulo22ResumenTotal = false;
        mostrarSubtitulo24ResumenTotal = false;
        mostrarSubtitulo29ResumenTotal = false;
       
        for (SubtituloVO subtituloVO : lst) {
               if (subtituloVO.getId()== Subtitulo.SUBTITULO21.getId())
                      mostrarSubtitulo21ResumenTotal = true;
               if (subtituloVO.getId()== Subtitulo.SUBTITULO22.getId())
                      mostrarSubtitulo22ResumenTotal = true;
               if (subtituloVO.getId()== Subtitulo.SUBTITULO24.getId())
                      mostrarSubtitulo24ResumenTotal = true;
               if (subtituloVO.getId()== Subtitulo.SUBTITULO29.getId())
                      mostrarSubtitulo29ResumenTotal = true;
        }
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
		
		//EL FILTRADO DEBE SER IGUAL AL ORIGINAL
		oTRevisarAntecedentesGlobalVOSubtitulo21Original = new OTRevisarAntecedentesGlobalVO();
		oTRevisarAntecedentesGlobalVOSubtitulo21Original  = oTRevisarAntecedentesGlobalVOSubtitulo21.clone();
	}
	
	private void CargarListaSubTitulo22()
	{
		//SUBTITULO 22
		listadoServiciosSubtitulo22 = new ArrayList<OTRevisarAntecedentesVO>();
		listadoServiciosSubtitulo22.addAll(otService.obtenerListaSubtituloVOPorPrograma(idProgramaAno,Subtitulo.SUBTITULO22.getId()));
		oTRevisarAntecedentesGlobalVOSubtitulo22.setListadoServicios(listadoServiciosSubtitulo22);
		
		//EL FILTRADO DEBE SER IGUAL AL ORIGINAL
		oTRevisarAntecedentesGlobalVOSubtitulo22Original = new OTRevisarAntecedentesGlobalVO();
		oTRevisarAntecedentesGlobalVOSubtitulo22Original  = oTRevisarAntecedentesGlobalVOSubtitulo22.clone();
	}
	
	private void CargarListaSubTitulo29()
	{
		//SUBTITULO 29
		listadoServiciosSubtitulo29 = new ArrayList<OTRevisarAntecedentesVO>();
		listadoServiciosSubtitulo29.addAll(otService.obtenerListaSubtituloVOPorPrograma(idProgramaAno,Subtitulo.SUBTITULO29.getId()));
		oTRevisarAntecedentesGlobalVOSubtitulo29.setListadoServicios(listadoServiciosSubtitulo29);
		
		//EL FILTRADO DEBE SER IGUAL AL ORIGINAL
		oTRevisarAntecedentesGlobalVOSubtitulo29Original = new OTRevisarAntecedentesGlobalVO();
		oTRevisarAntecedentesGlobalVOSubtitulo29Original  = oTRevisarAntecedentesGlobalVOSubtitulo29.clone();
	}
	
	private void CargarListaMunicipal()
	{
		//MUNICIPAL
		listadoServiciosMunicipal = new ArrayList<OTRevisarAntecedentesVO>();
		listadoServiciosMunicipal.addAll(otService.obtenerListaSubtituloVOPorPrograma(idProgramaAno,Subtitulo.SUBTITULO24.getId()));
		oTRevisarAntecedentesGlobalVOMunicipal.setListadoServicios(listadoServiciosMunicipal);
		
		//EL FILTRADO DEBE SER IGUAL AL ORIGINAL
		oTRevisarAntecedentesGlobalVOMunicipalOriginal = new OTRevisarAntecedentesGlobalVO();
		oTRevisarAntecedentesGlobalVOMunicipalOriginal  = oTRevisarAntecedentesGlobalVOMunicipal.clone();
	}
	
	//**EVENTO FILTRO COMBO
	public void filtrarGrillasTabDetallePrograma()
		{	
			idProgramaAno = valorComboProgramaFiltro;
			configurarVisibilidadPaneles();
			cargarServicios();
			
			if(mostrarSubtitulo21)
				filtrarSubtitulo21();
			if(mostrarSubtitulo22)
				filtrarSubtitulo22();
			if(mostrarSubtitulo29)
				filtrarSubtitulo29();
			if(mostrarSubtitulo24)
				filtrarMunicipal();
		}
	
	//**EVENTO FILTRO COMBO
		public void filtrarGrillasTabDetalleProgramaResumenTotal()
		{	
			idProgramaAnoResumenTotal = valorComboProgramaFiltroResumenTotal;
			configurarVisibilidadPanelesResumenTotal();
			cargarListaResumenTotal();
		}
		
	//**EVENTO FILTRO COMBO
		
	/*
	 ********************************************************************************************* SUBTITULO 21
	 */
	/*
	 * Filtra la lista segun los parametros de entrada, en este caso el servicio.
	 */

	public void filtrarSubtitulo21()
		{
			List<OTRevisarAntecedentesVO> lst = oTRevisarAntecedentesGlobalVOSubtitulo21Original.getListadoServicios();
			List<OTRevisarAntecedentesVO> lstAgregar = new ArrayList<OTRevisarAntecedentesVO>();
			for (OTRevisarAntecedentesVO oTRevisarAntecedentesVO : lst) {
				if (oTRevisarAntecedentesVO.getProgramaAno().getIdProgramaAno().intValue() == valorComboProgramaFiltro)
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

	public void filtrarSubtitulo22()
		{
			List<OTRevisarAntecedentesVO> lst = oTRevisarAntecedentesGlobalVOSubtitulo22Original.getListadoServicios();
			List<OTRevisarAntecedentesVO> lstAgregar = new ArrayList<OTRevisarAntecedentesVO>();
			for (OTRevisarAntecedentesVO oTRevisarAntecedentesVO : lst) {
				if (oTRevisarAntecedentesVO.getProgramaAno().getIdProgramaAno().intValue() == valorComboProgramaFiltro)
				{
					lstAgregar.add(oTRevisarAntecedentesVO);
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

	public void filtrarSubtitulo29()
		{
			List<OTRevisarAntecedentesVO> lst = oTRevisarAntecedentesGlobalVOSubtitulo29Original.getListadoServicios();
			List<OTRevisarAntecedentesVO> lstAgregar = new ArrayList<OTRevisarAntecedentesVO>();
			for (OTRevisarAntecedentesVO oTRevisarAntecedentesVO : lst) {
				if (oTRevisarAntecedentesVO.getProgramaAno().getIdProgramaAno().intValue() == valorComboProgramaFiltro)
				{
					lstAgregar.add(oTRevisarAntecedentesVO);
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
		
	public void filtrarMunicipal()
		{
			List<OTRevisarAntecedentesVO> lst = oTRevisarAntecedentesGlobalVOMunicipalOriginal.getListadoServicios();
			List<OTRevisarAntecedentesVO> lstAgregar = new ArrayList<OTRevisarAntecedentesVO>();
			for (OTRevisarAntecedentesVO oTRevisarAntecedentesVO : lst) {
				if (oTRevisarAntecedentesVO.getProgramaAno().getIdProgramaAno().intValue() == valorComboProgramaFiltro)
				{
					lstAgregar.add(oTRevisarAntecedentesVO);
				}
			}
			oTRevisarAntecedentesGlobalVOMunicipal.setListadoServicios(lstAgregar);
		}
	
	
	/**
	 * Lista de columnas dinamicas
	 */
	List<ColumnaVO> columnsProgramas;
	
	public List<ColumnaVO> getColumnsProgramas() {
		return columnsProgramas;
	}
	
	private void generarColumnasDinamicasResumenConsolidado() {
		
		List<ProgramaVO> listaProgramaVO = programasService.getProgramasByUserAno("cmurillo", Util.obtenerAno(new Date()));
		columnsProgramas = new ArrayList<ColumnaVO>();
		
		for (ProgramaVO programaVO : listaProgramaVO) {
			if(programaVO.getRevisaFonasa())
			{
				ColumnaVO  col = new ColumnaVO(programaVO.getNombre(),"Valor", programaVO.getNombre());
				columnsProgramas.add(col);
			}
		}
	}
	
	
	private List<OTResumenDependienteServicioVO>listaResumenProgramasVOSub21;
	
	
	public List<OTResumenDependienteServicioVO> getListaResumenProgramasVOSub21() {
		return listaResumenProgramasVOSub21;
	}

	public void setListaResumenProgramasVOSub21(
			List<OTResumenDependienteServicioVO> listaResumenProgramasVOSub21) {
		this.listaResumenProgramasVOSub21 = listaResumenProgramasVOSub21;
	}
	
	private List<OTResumenDependienteServicioVO>listaResumenProgramasVOSub22;
	
	
	public List<OTResumenDependienteServicioVO> getListaResumenProgramasVOSub22() {
		return listaResumenProgramasVOSub22;
	}

	public void setListaResumenProgramasVOSub22(
			List<OTResumenDependienteServicioVO> listaResumenProgramasVOSub22) {
		this.listaResumenProgramasVOSub22 = listaResumenProgramasVOSub22;
	}
	
	private List<OTResumenDependienteServicioVO>listaResumenProgramasVOSub29;
	
	
	public List<OTResumenDependienteServicioVO> getListaResumenProgramasVOSub29() {
		return listaResumenProgramasVOSub29;
	}

	public void setListaResumenProgramasVOSub29(
			List<OTResumenDependienteServicioVO> listaResumenProgramasVOSub29) {
		this.listaResumenProgramasVOSub29 = listaResumenProgramasVOSub29;
	}
	
	private List<OTResumenDependienteServicioVO>listaResumenProgramasVOSub24;
	
	
	public List<OTResumenDependienteServicioVO> getListaResumenProgramasVOSub24() {
		return listaResumenProgramasVOSub24;
	}

	public void setListaResumenProgramasVOSub24(
			List<OTResumenDependienteServicioVO> listaResumenProgramasVOSub24) {
		this.listaResumenProgramasVOSub24 = listaResumenProgramasVOSub24;
	}

	//CREACION EXCEL
	List<List<OTResumenConsolidadoFonasaVO>> listaResumenesConsolidadoVO = new ArrayList<List<OTResumenConsolidadoFonasaVO>>();
	List<List<OTResumenDependienteServicioVO>> listaResumenesProgramasVO = new ArrayList<List<OTResumenDependienteServicioVO>>();
	public void crearArchivoResumenConsolidadorFonasa()
	{
		this.idArchivoResumenConsolidadoFonasa = otService.getIdPlantillaOTFonasa(idOrdenTransferencia,columnsProgramas,listaResumenesProgramasVO,listaResumenesConsolidadoVO);
	}

	//cargar grilla resumen consolidado fonasa
	private void cargarListaResumenConsolidadoFonasa()
	{

		//SUBTITULO 21
		List<OTResumenConsolidadoFonasaVO> listadoResumenConsolidadoSub21 = 
				otService.obtenerListaResumenConsolidadoFonasaVOPorUsuario("cmurillo",Subtitulo.SUBTITULO21.getId());
		oTRevisarAntecedentesGlobalVOSubtitulo21.setListadoResumenConsolidado(listadoResumenConsolidadoSub21);
		listaResumenesConsolidadoVO.add(listadoResumenConsolidadoSub21);
		
		listaResumenProgramasVOSub21 = new ArrayList<OTResumenDependienteServicioVO>();
		for (OTResumenConsolidadoFonasaVO obje : oTRevisarAntecedentesGlobalVOSubtitulo21.getListadoResumenConsolidado()) {
			listaResumenProgramasVOSub21.addAll(obje.getListaOTResumenConsolidadoFonasaProgramasVO());
		}
		listaResumenesProgramasVO.add(listaResumenProgramasVOSub21);

		//SUBTITULO 22
		List<OTResumenConsolidadoFonasaVO> listadoResumenConsolidadoSub22 =
				  otService.obtenerListaResumenConsolidadoFonasaVOPorUsuario("cmurillo",Subtitulo.SUBTITULO22.getId());
		oTRevisarAntecedentesGlobalVOSubtitulo22.setListadoResumenConsolidado(listadoResumenConsolidadoSub22);
		listaResumenesConsolidadoVO.add(listadoResumenConsolidadoSub22);
		
		listaResumenProgramasVOSub22 = new ArrayList<OTResumenDependienteServicioVO>();
		for (OTResumenConsolidadoFonasaVO obje : oTRevisarAntecedentesGlobalVOSubtitulo22.getListadoResumenConsolidado()) {
			listaResumenProgramasVOSub22.addAll(obje.getListaOTResumenConsolidadoFonasaProgramasVO());
		}
		listaResumenesProgramasVO.add(listaResumenProgramasVOSub22);
		
		//SUBTITULO 29
		List<OTResumenConsolidadoFonasaVO> listadoResumenConsolidadoSub29 =
				otService.obtenerListaResumenConsolidadoFonasaVOPorUsuario("cmurillo",Subtitulo.SUBTITULO29.getId());
		oTRevisarAntecedentesGlobalVOSubtitulo29.setListadoResumenConsolidado(listadoResumenConsolidadoSub29);
		listaResumenesConsolidadoVO.add(listadoResumenConsolidadoSub29);
		
		listaResumenProgramasVOSub29 = new ArrayList<OTResumenDependienteServicioVO>();
		for (OTResumenConsolidadoFonasaVO obje : oTRevisarAntecedentesGlobalVOSubtitulo29.getListadoResumenConsolidado()) {
			listaResumenProgramasVOSub29.addAll(obje.getListaOTResumenConsolidadoFonasaProgramasVO());
		}
		listaResumenesProgramasVO.add(listaResumenProgramasVOSub29);
		
		//SUBTITULO 24
		List<OTResumenConsolidadoFonasaVO> listadoResumenConsolidadoSub24 = otService.obtenerListaResumenConsolidadoFonasaVOPorUsuario("cmurillo",Subtitulo.SUBTITULO24.getId());
		oTRevisarAntecedentesGlobalVOMunicipal.setListadoResumenConsolidado(listadoResumenConsolidadoSub24);
		listaResumenesConsolidadoVO.add(listadoResumenConsolidadoSub24);
		
		listaResumenProgramasVOSub24 = new ArrayList<OTResumenDependienteServicioVO>();
		for (OTResumenConsolidadoFonasaVO obje : oTRevisarAntecedentesGlobalVOMunicipal.getListadoResumenConsolidado()) {
			listaResumenProgramasVOSub24.addAll(obje.getListaOTResumenConsolidadoFonasaProgramasVO());
		}
		listaResumenesProgramasVO.add(listaResumenProgramasVOSub24);

		generarColumnasDinamicasResumenConsolidado();
	}
	

	//cargar grilla resumen consolidado fonasa
	private void cargarListaResumenTotal()
	{
		oTRevisarAntecedentesGlobalVOSubtitulo21.setListadoResumenTotal(
				   otService.obtenerListaResumenTotalPorIDProgramaAno(idProgramaAnoResumenTotal));
	}
	
	
	/**
	 * Metodos implementacion BPM 
	 */

	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("solicitaReparo_", "no");
		parameters.put("idProcesoTramitacionOrden_",idOrdenTransferencia);
		return parameters;
	}
	
	// Continua el proceso con el programa seleccionado.
		public void continuarProceso() {
			super.enviar();
		}
		
	// Reparo programa
	public void reparoPrograma() {
		 otService.repararPrograma(valorComboProgramaRechazo,idMesActual,idMesSiguiente,getSessionBean().getUsername(),observacionReparo);
		//super.enviar();
	}	

	@Override
	public String iniciarProceso() {
		return null;
	}
	

}
