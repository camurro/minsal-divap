package cl.minsal.divap.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import minsal.divap.service.ConveniosService;
import minsal.divap.service.ProgramasService;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ReporteEmailsEnviadosVO;

import org.apache.log4j.Logger;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named ( "procesoGITResumenDocumentosController" ) 
@ViewScoped 
public class ProcesoGITResumenDocumentosController extends AbstractTaskMBean implements Serializable {
	private static final long serialVersionUID = 8979055329731411696L;
	
	@Inject 
	private transient Logger log;
	private Integer idConvenio;
	private ProgramaVO programa;
	private List<ReporteEmailsEnviadosVO> reporteCorreos;
	private String docIdDownload;
	
	@EJB
	private ProgramasService programaService;
	@EJB
	private ConveniosService conveniosService;
	
	@PostConstruct
	public void init() {
		if(sessionExpired()){
			return;
		}
		if (getTaskDataVO() != null && getTaskDataVO().getData() != null) {
			Integer programaSeleccionado = (Integer) getTaskDataVO().getData().get("_programaSeleccionado");
			idConvenio = (Integer) getTaskDataVO().getData().get("_idConvenio");
			programa = programaService.getProgramaAno(programaSeleccionado);
			reporteCorreos = conveniosService.getReporteCorreosByConvenio(idConvenio);
		}
	}
	
	public String downloadArchivo() {
		Integer docDownload = Integer.valueOf(Integer.parseInt(getDocIdDownload()));
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
		return null;
	}
	
	public String actualizar(){
		return null;
	}
	
	public void buscarReporteCorreos(){
		reporteCorreos = conveniosService.getReporteCorreosByConvenio(idConvenio);
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
