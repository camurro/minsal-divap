package cl.minsal.divap.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.service.RebajaService;
import minsal.divap.service.ReportesServices;
import minsal.divap.vo.ReporteRebajaVO;
import cl.redhat.bandejaTareas.controller.BaseController;


@Named("reportesRebajaController")
@ViewScoped
public class ReportesRebajaController extends BaseController implements Serializable{

	
	private static final long serialVersionUID = 7165151104555335864L;
	private List<ReporteRebajaVO> reporteRebajaVO;
	
	private Long montoCorte1;
	private Long montoCorte2;
	private Long montoCorte3;
	private Long montoCorte4;
	private Long montoRebajaTotal;
	private Integer idPlanillaRebaja;
	private String docIdDownload;
	
	@EJB
	ReportesServices reportesServices;
	
	
	@PostConstruct
	public void init(){
		this.reporteRebajaVO = reportesServices.getReporteRebaja();
		this.idPlanillaRebaja = reportesServices.getDocumentByTypeAnoActual(TipoDocumentosProcesos.REPORTEREBAJA);
		if(this.idPlanillaRebaja == null){
			this.idPlanillaRebaja = reportesServices.generarPlanillaReporteRebaja(getLoggedUsername());
		}
		
	}
	
	public String downloadTemplate() {
		Integer docDownload = Integer.valueOf(Integer
				.parseInt(getDocIdDownload()));
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
		return null;
	}
	
	public List<ReporteRebajaVO> getReporteRebajaVO() {
		return reporteRebajaVO;
	}
	public void setReporteRebajaVO(List<ReporteRebajaVO> reporteRebajaVO) {
		this.reporteRebajaVO = reporteRebajaVO;
	}

	public Long getMontoCorte1() {
		this.montoCorte1 = 0L;
		for(ReporteRebajaVO lista : reporteRebajaVO){
			this.montoCorte1 += lista.getfCorte1Monto();
		}
		return montoCorte1;
	}

	public void setMontoCorte1(Long montoCorte1) {
		this.montoCorte1 = montoCorte1;
	}

	public Long getMontoCorte2() {
		this.montoCorte2 = 0L;
		for(ReporteRebajaVO lista : reporteRebajaVO){
			this.montoCorte2 += lista.getfCorte2Monto();
		}
		return montoCorte2;
	}

	public void setMontoCorte2(Long montoCorte2) {
		this.montoCorte2 = montoCorte2;
	}

	public Long getMontoCorte3() {
		this.montoCorte3 = 0L;
		for(ReporteRebajaVO lista : reporteRebajaVO){
			this.montoCorte3 += lista.getfCorte3Monto();
		}
		return montoCorte3;
	}

	public void setMontoCorte3(Long montoCorte3) {
		this.montoCorte3 = montoCorte3;
	}

	public Long getMontoCorte4() {
		this.montoCorte4 = 0L;
		for(ReporteRebajaVO lista : reporteRebajaVO){
			this.montoCorte4 += lista.getfCorte4Monto();
		}
		return montoCorte4;
	}

	public void setMontoCorte4(Long montoCorte4) {
		this.montoCorte4 = montoCorte4;
	}

	public Long getMontoRebajaTotal() {
		this.montoRebajaTotal = 0L;
		for(ReporteRebajaVO lista : reporteRebajaVO){
			this.montoRebajaTotal += lista.getRebajaTotal();
		}
		return montoRebajaTotal;
	}

	public void setMontoRebajaTotal(Long montoRebajaTotal) {
		this.montoRebajaTotal = montoRebajaTotal;
	}

	public Integer getIdPlanillaRebaja() {
		return idPlanillaRebaja;
	}

	public void setIdPlanillaRebaja(Integer idPlanillaRebaja) {
		this.idPlanillaRebaja = idPlanillaRebaja;
	}

	public String getDocIdDownload() {
		return docIdDownload;
	}

	public void setDocIdDownload(String docIdDownload) {
		this.docIdDownload = docIdDownload;
	}
	

}
