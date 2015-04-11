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
import minsal.divap.vo.ReporteEmailsEnviadosVO;

import org.apache.log4j.Logger;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named ( "procesoOTSeguimientoDocumentacionServiciosController" ) 
@ViewScoped 
public class ProcesoOTSeguimientoDocumentacionServiciosController extends AbstractTaskMBean implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String actividadSeguimientoTitle;
	@Inject 
	private transient Logger log;
	private List<ReporteEmailsEnviadosVO> reporteCorreos;
	private Integer idProcesoOT;
	private Integer ano;
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
			idProcesoOT = (Integer) getTaskDataVO().getData().get("_idProcesoOT");
			ano = (Integer) getTaskDataVO().getData().get("_ano");
		}
	}
	
	public String downloadArchivo() {
		Integer docDownload = Integer.valueOf(Integer.parseInt(getDocIdDownload()));
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
		return null;
	}
	
	public void buscarReporteCorreos(){
		reporteCorreos = otService.getReporteCorreosByIdRemesa(idProcesoOT);
	}
	
	public String getActividadSeguimientoTitle() {
		return actividadSeguimientoTitle;
	}

	public void setActividadSeguimientoTitle(String actividadSeguimientoTitle) {
		this.actividadSeguimientoTitle = actividadSeguimientoTitle;
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

	public Integer getIdProcesoOT() {
		return idProcesoOT;
	}

	public void setIdProcesoOT(Integer idProcesoOT) {
		this.idProcesoOT = idProcesoOT;
	}

	public List<ReporteEmailsEnviadosVO> getReporteCorreos() {
		if(reporteCorreos == null){
			reporteCorreos = otService.getReporteCorreosByIdRemesa(idProcesoOT);
		}
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

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}
	
}
