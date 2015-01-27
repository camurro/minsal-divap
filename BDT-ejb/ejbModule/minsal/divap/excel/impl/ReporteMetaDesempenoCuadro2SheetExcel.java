package minsal.divap.excel.impl;

import java.util.ArrayList;
import java.util.List;

import minsal.divap.excel.interfaces.ExcelTemplate;
import minsal.divap.vo.CellExcelVO;
import minsal.divap.vo.ReporteGlosaVO;
import minsal.divap.vo.ReporteRebajaVO;
import minsal.divap.vo.metaDesempeno.ReporteMetaDesempenoOTAcumuladasPrincipal;

public class ReporteMetaDesempenoCuadro2SheetExcel extends ExcelTemplate<ReporteMetaDesempenoOTAcumuladasPrincipal>{
	private List<CellExcelVO> headerComplex = null;
	private List<CellExcelVO> subHeadeComplex = null;

	public ReporteMetaDesempenoCuadro2SheetExcel(List<CellExcelVO> header,List<CellExcelVO> subHeader, List<ReporteMetaDesempenoOTAcumuladasPrincipal> items) {
		super(null, items);
		headerComplex = header;
		subHeadeComplex = subHeader;
		
	}
	
	public ReporteMetaDesempenoCuadro2SheetExcel(List<CellExcelVO> header,List<CellExcelVO> subHeader, List<ReporteMetaDesempenoOTAcumuladasPrincipal> items, Integer offsetRows, Integer offsetColumns){
		super(null, items);
		headerComplex = header;
		subHeadeComplex = subHeader;
	}

	@Override
	public List<List<Object>> getDataList() {
		List<List<Object>> dataList = new ArrayList<List<Object>>();
		for(ReporteMetaDesempenoOTAcumuladasPrincipal reporteMetaDesempenoOTAcumuladasPrincipal : getItems()){
			List<Object> row = reporteMetaDesempenoOTAcumuladasPrincipal.getRow();
			if(row != null){
				dataList.add(row);
			}
		}
		return dataList;
	}

	public List<CellExcelVO> getHeaderComplex() {
		return headerComplex;
	}

	public void setHeaderComplex(List<CellExcelVO> headerComplex) {
		this.headerComplex = headerComplex;
	}
	public List<CellExcelVO> getSubHeadeComplex() {
		return subHeadeComplex;
	}

	public void setSubHeadeComplex(List<CellExcelVO> subHeadeComplex) {
		this.subHeadeComplex = subHeadeComplex;
	}
	
}
