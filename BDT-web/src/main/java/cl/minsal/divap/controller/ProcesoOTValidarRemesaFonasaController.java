package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import minsal.divap.service.OTService;
import minsal.divap.service.ProgramasService;
import minsal.divap.service.RecursosFinancierosProgramasReforzamientoService;
import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named ( "procesoOTValidarRemesaFonasaController" ) 
@ViewScoped 
public class ProcesoOTValidarRemesaFonasaController extends AbstractTaskMBean implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String docIdDownload;
	private Integer ano;
	private Integer idProcesoOT;
	private Boolean validacionRemesaFonasa;
	
	@EJB
	private RecursosFinancierosProgramasReforzamientoService reforzamientoService;
	@EJB
	private ProgramasService programaService;
	@EJB
	private OTService otService;
	
	@PostConstruct
	public void init() {
		if (!getSessionBean().isLogged()) {
			try {
				facesContext.getExternalContext().redirect("login.jsf");
			} catch (IOException e) {
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

	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		System.out.println("validacionRemesaFonasa->"+validacionRemesaFonasa);
		parameters.put("validacionRemesaFonasa_", validacionRemesaFonasa);
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

	public RecursosFinancierosProgramasReforzamientoService getReforzamientoService() {
		return reforzamientoService;
	}

	public void setReforzamientoService(
			RecursosFinancierosProgramasReforzamientoService reforzamientoService) {
		this.reforzamientoService = reforzamientoService;
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

	public Boolean getValidacionRemesaFonasa() {
		return validacionRemesaFonasa;
	}

	public void setValidacionRemesaFonasa(Boolean validacionRemesaFonasa) {
		this.validacionRemesaFonasa = validacionRemesaFonasa;
	}
	
}
