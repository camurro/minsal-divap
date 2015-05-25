package cl.minsal.divap.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import minsal.divap.dao.MesDAO;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.service.ReportesServices;
import minsal.divap.service.ServicioSaludService;
import minsal.divap.util.StringUtil;
import minsal.divap.vo.ReporteGlosaVO;
import minsal.divap.vo.ServiciosVO;
import cl.minsal.divap.model.Mes;
import cl.redhat.bandejaTareas.controller.BaseController;

@Named("reporteGlosa07Controller")
@ViewScoped
public class ReporteGlosa07Controller extends BaseController implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4493915962863710980L;
	private List<ServiciosVO> servicios;
	private ServiciosVO servicioSeleccionado;
	private String valorComboServicio;
	private List<ReporteGlosaVO> reporteGlosaVO;
	@EJB
	private ServicioSaludService servicioSaludService;
	@EJB
	private ReportesServices reportesServices;
	@EJB
	private MesDAO mesDAO;
	
	private Long sumArt49perCapita;
	private Long sumArt56reforzamientoMunicipal;
	private Long sumTotalRemesasEneroMarzo;
	private Integer idPlanilla;
	private String docIdDownload;
	private String mesActual;
	private Integer ano;
	
	@PostConstruct 
	public void init() {
//		this.idPlanilla = reportesServices.getDocumentByTypeAnoActual(TipoDocumentosProcesos.REPORTEGLOSA07, getAno());
//		if(this.idPlanilla == null){
//			this.idPlanilla = reportesServices.generarPlanillaReporteGlosa07(getLoggedUsername(), getAno());
//		}
//		System.out.println("this.idPlanilla ---> "+this.idPlanilla);
		
		Integer mesCurso = Integer.parseInt(reportesServices.getMesCurso(true));
		
		Mes mes = mesDAO.getMesPorID(mesCurso);
		this.mesActual = StringUtil.caracterUnoMayuscula(mes.getNombre());		
		this.reporteGlosaVO = reportesServices.getReporteGlosaPorServicio(null, getLoggedUsername(), getAno());
	}
	
	public String downloadTemplate() {
		this.idPlanilla = reportesServices.generarPlanillaReporteGlosa07(getLoggedUsername(), getAno());
//		Integer docDownload = Integer.valueOf(Integer
//				.parseInt(getDocIdDownload()));
		setDocumento(documentService.getDocument(this.idPlanilla));
		super.downloadDocument();
		return null;
	}
	
	
	public void cargarTablaPorServicio(){
		System.out.println("cargarTablaPorServicio --> getValorComboServicio() --> "+getValorComboServicio());
		Integer iServicio = ((getValorComboServicio() != null && !getValorComboServicio().trim().isEmpty()) ? Integer.parseInt(getValorComboServicio().trim()) : null);
		this.reporteGlosaVO = reportesServices.getReporteGlosaPorServicio(iServicio, getLoggedUsername(), getAno());
	}

	
	
	public List<ServiciosVO> getServicios() {
		if(servicios == null){
			servicios = servicioSaludService.getServiciosOrderId();
		}
		return servicios;
	}
	public void setServicios(List<ServiciosVO> servicios) {
		this.servicios = servicios;
	}
	public ServiciosVO getServicioSeleccionado() {
		return servicioSeleccionado;
	}
	public void setServicioSeleccionado(ServiciosVO servicioSeleccionado) {
		this.servicioSeleccionado = servicioSeleccionado;
	}
	public String getValorComboServicio() {
		return valorComboServicio;
	}
	public void setValorComboServicio(String valorComboServicio) {
		this.valorComboServicio = valorComboServicio;
	}

	public List<ReporteGlosaVO> getReporteGlosaVO() {
		return reporteGlosaVO;
	}

	public void setReporteGlosaVO(List<ReporteGlosaVO> reporteGlosaVO) {
		this.reporteGlosaVO = reporteGlosaVO;
	}

	
	
	public Long getSumArt49perCapita() {
		this.sumArt49perCapita = 0L;
		for(ReporteGlosaVO lista : this.reporteGlosaVO){
			this.sumArt49perCapita += lista.getArt49perCapita();
		}
		return sumArt49perCapita;
	}

	public void setSumArt49perCapita(Long sumArt49perCapita) {
		this.sumArt49perCapita = sumArt49perCapita;
	}

	public Long getSumArt56reforzamientoMunicipal() {
		this.sumArt56reforzamientoMunicipal = 0L;
		for(ReporteGlosaVO lista : this.reporteGlosaVO){
			this.sumArt56reforzamientoMunicipal += lista.getArt56reforzamientoMunicipal();
		}
		return sumArt56reforzamientoMunicipal;
	}

	public void setSumArt56reforzamientoMunicipal(
			Long sumArt56reforzamientoMunicipal) {
		this.sumArt56reforzamientoMunicipal = sumArt56reforzamientoMunicipal;
	}

	public Long getSumTotalRemesasEneroMarzo() {
		this.sumTotalRemesasEneroMarzo = 0L;
		for(ReporteGlosaVO lista : this.reporteGlosaVO){
			this.sumTotalRemesasEneroMarzo += lista.getTotalRemesasEneroMarzo();
		}
		return sumTotalRemesasEneroMarzo;
	}

	public void setSumTotalRemesasEneroMarzo(Long sumTotalRemesasEneroMarzo) {
		this.sumTotalRemesasEneroMarzo = sumTotalRemesasEneroMarzo;
	}

	public Integer getIdPlanilla() {
		return idPlanilla;
	}

	public void setIdPlanilla(Integer idPlanilla) {
		this.idPlanilla = idPlanilla;
	}

	public String getDocIdDownload() {
		return docIdDownload;
	}

	public void setDocIdDownload(String docIdDownload) {
		this.docIdDownload = docIdDownload;
	}

	public String getMesActual() {
		return mesActual;
	}

	public void setMesActual(String mesActual) {
		this.mesActual = mesActual;
	}

	public Integer getAno() {
		if(ano == null){
			ano = reportesServices.getAnoCurso();
		}
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

}
