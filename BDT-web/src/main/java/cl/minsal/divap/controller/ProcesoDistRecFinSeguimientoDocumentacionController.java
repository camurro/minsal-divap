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

import org.apache.log4j.Logger;

import minsal.divap.service.ProgramasService;
import minsal.divap.service.RecursosFinancierosProgramasReforzamientoService;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ReporteEmailsEnviadosVO;
import cl.minsal.divap.pojo.EnvioServiciosPojo;
import cl.minsal.divap.pojo.EstablecimientoPojo;
import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named ( "procesoDistRecFinSeguimientoDocumentacionController" ) 
@ViewScoped 
public class ProcesoDistRecFinSeguimientoDocumentacionController extends AbstractTaskMBean implements Serializable {
	private static final long serialVersionUID = 8979055329731411696L;
	
	private List<EnvioServiciosPojo> listadoEnvioServicios;
	private List<EstablecimientoPojo> listadoEstablecimientos;
	private String actividadSeguimientoTitle;
	@Inject 
	private transient Logger log;
	private ProgramaVO programa;
	private ProgramaVO programaProxAno;
	private Integer programaSeleccionado;
	private Integer instanciaProceso;
	private Integer ano;
	private List<ReporteEmailsEnviadosVO> reporteCorreos;
	private String docIdDownload;
	
	@EJB
	private RecursosFinancierosProgramasReforzamientoService reforzamientoService;
	@EJB
	private ProgramasService programasService;
	
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
			programaSeleccionado = (Integer) getTaskDataVO()
					.getData().get("_programaSeleccionado");
			instanciaProceso = (Integer) getTaskDataVO()
					.getData().get("_idProceso");
			this.ano = (Integer) getTaskDataVO().getData().get("_ano");
			System.out.println("this.ano --->" + this.ano);
		}
		programa = programasService.getProgramaByIdProgramaAndAno(programaSeleccionado, (ano - 1));
		programaProxAno = programasService.getProgramaByIdProgramaAndAno(programaSeleccionado, ano);
		
		reporteCorreos = reforzamientoService.getReporteCorreosByIdInstanciaReforzamiento(instanciaProceso);
	}
	
	public String downloadArchivo() {
		Integer docDownload = Integer.valueOf(Integer.parseInt(getDocIdDownload()));
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
		return null;
	}
	
	public Integer actualizar(){
		return null;
	}
	
	public void buscarReporteCorreos(){
		reporteCorreos = reforzamientoService.getReporteCorreosByIdInstanciaReforzamiento(instanciaProceso);
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

	public Integer getProgramaSeleccionado() {
		return programaSeleccionado;
	}

	public void setProgramaSeleccionado(Integer programaSeleccionado) {
		this.programaSeleccionado = programaSeleccionado;
	}

	public RecursosFinancierosProgramasReforzamientoService getReforzamientoService() {
		return reforzamientoService;
	}

	public void setReforzamientoService(
			RecursosFinancierosProgramasReforzamientoService reforzamientoService) {
		this.reforzamientoService = reforzamientoService;
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

	public Integer getInstanciaProceso() {
		return instanciaProceso;
	}

	public void setInstanciaProceso(Integer instanciaProceso) {
		this.instanciaProceso = instanciaProceso;
	}

	public ProgramaVO getProgramaProxAno() {
		return programaProxAno;
	}

	public void setProgramaProxAno(ProgramaVO programaProxAno) {
		this.programaProxAno = programaProxAno;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}
	
}
