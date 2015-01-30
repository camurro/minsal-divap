package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import minsal.divap.service.OTService;
import minsal.divap.service.ProgramasService;
import minsal.divap.service.RecursosFinancierosProgramasReforzamientoService;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ReporteEmailsEnviadosVO;

import org.apache.log4j.Logger;

import cl.minsal.divap.pojo.EnvioServiciosPojo;
import cl.minsal.divap.pojo.EstablecimientoPojo;
import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named ( "procesoFlujoCajaEnviarDocumentosController" ) 
@ViewScoped 
public class ProcesoFlujoCajaEnviarDocumentosController extends AbstractTaskMBean implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<EnvioServiciosPojo> listadoEnvioServicios;
	private List<EstablecimientoPojo> listadoEstablecimientos;
	private String actividadSeguimientoTitle;
	@Inject 
	private transient Logger log;
	

	private ProgramaVO programa;
	private Integer idProcesoOT;
	private Integer idProxAno;
	private List<ReporteEmailsEnviadosVO> reporteCorreos;
	private String docIdDownload;
	
	@EJB
	private RecursosFinancierosProgramasReforzamientoService reforzamientoService;
	@EJB
	private ProgramasService programaService;
	@EJB
	private OTService otService;
	
	@PostConstruct
	public void init() {
		if (!getSessionBean().isLogged()) {
			log.warn("No hay usuario almacenado en sesion, se redirecciona a pantalla de login");
			try {
				facesContext.getExternalContext().redirect("login.jsf");
			} catch (IOException e) {
				log.error("Error tratando de redireccionar a login por falta de usuario en sesion.", e);
			}
		}
		if (getTaskDataVO() != null && getTaskDataVO().getData() != null) {
			idProcesoOT = (Integer) getTaskDataVO()
					.getData().get("_idProcesoOT");
		}
		
		reporteCorreos = otService.getReporteCorreosByIdRemesa(idProcesoOT);
	}
	
	public String downloadArchivo() {
		Integer docDownload = Integer.valueOf(Integer.parseInt(getDocIdDownload()));
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
		return null;
	}
	
	public Integer actualizar(){return null;}
	
	public void buscarReporteCorreos(){
		reporteCorreos = otService.getReporteCorreosByIdRemesa(idProcesoOT);
	}
	
	public String getActividadSeguimientoTitle() {
		return actividadSeguimientoTitle;
	}

	public void setActividadSeguimientoTitle(String actividadSeguimientoTitle) {
		this.actividadSeguimientoTitle = actividadSeguimientoTitle;
	}

	public List<EstablecimientoPojo> getListadoEstablecimientos() {
		return listadoEstablecimientos;
	}

	public void setListadoEstablecimientos(
			List<EstablecimientoPojo> listadoEstablecimientos) {
		this.listadoEstablecimientos = listadoEstablecimientos;
	}

	public List<EnvioServiciosPojo> getListadoEnvioServicios() {
		return listadoEnvioServicios;
	}

	public void setListadoEnvioServicios(
			List<EnvioServiciosPojo> listadoEnvioServicios) {
		this.listadoEnvioServicios = listadoEnvioServicios;
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
		return parameters;
	}

	@Override
	public String iniciarProceso() {
		return null;
	}

	public ProgramaVO getPrograma() {
		return programa;
	}

	public void setPrograma(ProgramaVO programa) {
		this.programa = programa;
	}

	

	public Integer getIdProcesoOT() {
		return idProcesoOT;
	}

	public void setIdProcesoOT(Integer idProcesoOT) {
		this.idProcesoOT = idProcesoOT;
	}

	public RecursosFinancierosProgramasReforzamientoService getReforzamientoService() {
		return reforzamientoService;
	}

	public void setReforzamientoService(
			RecursosFinancierosProgramasReforzamientoService reforzamientoService) {
		this.reforzamientoService = reforzamientoService;
	}

	public Integer getIdProxAno() {
		return idProxAno;
	}

	public void setIdProxAno(Integer idProxAno) {
		this.idProxAno = idProxAno;
	}

	public List<ReporteEmailsEnviadosVO> getReporteCorreos() {
		return reporteCorreos;
	}

	public void setReporteCorreos(List<ReporteEmailsEnviadosVO> reporteCorreos) {
		this.reporteCorreos = reporteCorreos;
	}

	public String getDocIdDownload() {
		return docIdDownload;
	}

	public void setDocIdDownload(String docIdDownload) {
		this.docIdDownload = docIdDownload;
	}
	
}
