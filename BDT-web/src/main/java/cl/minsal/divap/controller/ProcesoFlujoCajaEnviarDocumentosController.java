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

import minsal.divap.service.EstimacionFlujoCajaService;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ReporteEmailsEnviadosVO;

import org.apache.log4j.Logger;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named ( "procesoFlujoCajaEnviarDocumentosController" ) 
@ViewScoped 
public class ProcesoFlujoCajaEnviarDocumentosController extends AbstractTaskMBean implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject 
	private transient Logger log;
	

	private ProgramaVO programa;
	private Integer idProceso;
	//private Integer idProxAno;
	private List<ReporteEmailsEnviadosVO> reporteCorreos;
	private String docIdDownload;
	@EJB
	private EstimacionFlujoCajaService estimacionFlujoCajaService;
	
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
			idProceso = (Integer) getTaskDataVO()
					.getData().get("_idProceso");
		}
		
		reporteCorreos = estimacionFlujoCajaService.getReporteCorreosByFlujoCajaConsolidador(idProceso);
	}
	
	public String downloadArchivo() {
		Integer docDownload = Integer.valueOf(Integer.parseInt(getDocIdDownload()));
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
		return null;
	}
	
	
	public void buscarReporteCorreos(){
		reporteCorreos = estimacionFlujoCajaService.getReporteCorreosByFlujoCajaConsolidador(idProceso);
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

	public Integer getIdProceso() {
		return idProceso;
	}

	public void setIdProceso(Integer idProceso) {
		this.idProceso = idProceso;
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
