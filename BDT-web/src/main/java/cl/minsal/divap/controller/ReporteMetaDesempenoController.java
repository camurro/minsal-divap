package cl.minsal.divap.controller;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.service.ProgramasService;
import minsal.divap.service.ReportesServices;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ServiciosVO;
import cl.redhat.bandejaTareas.controller.BaseController;


@Named ( "reporteMetaDesempenoController" )
@ViewScoped
public class ReporteMetaDesempenoController extends BaseController implements Serializable{

	private static final long serialVersionUID = -5594583943620049546L;
	
	private List<ServiciosVO> servicios;
	private List<ProgramaVO> programas;
	private Integer anoEnCurso;
	private Integer valorComboPrograma;
	private Integer idPlanillaCuadro1;
	private String docIdDownloadCuadro1;
	private Integer idPlanillaCuadro2;
	private String docIdDownloadCuadro2;
	@EJB
	private ReportesServices reportesServices;
	@EJB
	private ProgramasService programasService;
	
	@PostConstruct public void init() {
		
		this.idPlanillaCuadro2 = reportesServices.getDocumentByTypeAnoActual(TipoDocumentosProcesos.REPORTEMETADESEMPENOCUADRO2, getAnoEnCurso());
		if(this.idPlanillaCuadro2 == null){
			this.idPlanillaCuadro2 = reportesServices.generarPlanillaMetaDesempenoCuadro2(getAnoEnCurso());
		}
		
	}
	
	public String downloadTemplateCuadro2() {
		Integer docDownload = Integer.valueOf(Integer.parseInt(getDocIdDownloadCuadro2()));
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
		return null;
	}
	
	public Integer getAnoCurso() {
		DateFormat formatNowYear = new SimpleDateFormat("yyyy");
		Date nowDate = new Date();
		return Integer.valueOf(formatNowYear.format(nowDate));
	}
	
	public List<ServiciosVO> getServicios() {
		return servicios;
	}
	
	public void setServicios(List<ServiciosVO> servicios) {
		this.servicios = servicios;
	}
	
	public List<ProgramaVO> getProgramas() {
		if(programas == null){
			programas = programasService.getProgramasReporteByAno(getAnoEnCurso());
		}
		return programas;
	}
	
	public void setProgramas(List<ProgramaVO> programas) {
		this.programas = programas;
	}
	
	public Integer getIdPlanillaCuadro1() {
		return idPlanillaCuadro1;
	}
	
	public void setIdPlanillaCuadro1(Integer idPlanillaCuadro1) {
		this.idPlanillaCuadro1 = idPlanillaCuadro1;
	}
	
	public String getDocIdDownloadCuadro1() {
		return docIdDownloadCuadro1;
	}
	
	public void setDocIdDownloadCuadro1(String docIdDownloadCuadro1) {
		this.docIdDownloadCuadro1 = docIdDownloadCuadro1;
	}
	
	public Integer getIdPlanillaCuadro2() {
		return idPlanillaCuadro2;
	}
	
	public void setIdPlanillaCuadro2(Integer idPlanillaCuadro2) {
		this.idPlanillaCuadro2 = idPlanillaCuadro2;
	}
	
	public String getDocIdDownloadCuadro2() {
		return docIdDownloadCuadro2;
	}
	
	public void setDocIdDownloadCuadro2(String docIdDownloadCuadro2) {
		this.docIdDownloadCuadro2 = docIdDownloadCuadro2;
	}
	
	public Integer getAnoEnCurso() {
		if(anoEnCurso == null){
			anoEnCurso = reportesServices.getAnoCurso();
		}
		return anoEnCurso;
	}
	
	public void setAnoEnCurso(Integer anoEnCurso) {
		this.anoEnCurso = anoEnCurso;
	}

	public Integer getValorComboPrograma() {
		return valorComboPrograma;
	}

	public void setValorComboPrograma(Integer valorComboPrograma) {
		this.valorComboPrograma = valorComboPrograma;
	}

}
