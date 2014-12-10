package minsal.divap.excel.impl;

import java.util.ArrayList;
import java.util.List;

import minsal.divap.excel.interfaces.ExcelTemplate;
import minsal.divap.vo.CellExcelVO;
import minsal.divap.vo.ReporteGlosaVO;
import minsal.divap.vo.ReporteRebajaVO;

public class ReporteGlosa07SheetExcel extends ExcelTemplate<ReporteGlosaVO>{
	private List<CellExcelVO> headerComplex = null;
	private List<CellExcelVO> subHeadeComplex = null;

	public ReporteGlosa07SheetExcel(List<CellExcelVO> header,List<CellExcelVO> subHeader, List<ReporteGlosaVO> items) {
		super(null, items);
		headerComplex = header;
		subHeadeComplex = subHeader;
		
	}
	
	public ReporteGlosa07SheetExcel(List<CellExcelVO> header,List<CellExcelVO> subHeader, List<ReporteGlosaVO> items, Integer offsetRows, Integer offsetColumns){
		super(null, items);
		headerComplex = header;
		subHeadeComplex = subHeader;
	}

	@Override
	public List<List<Object>> getDataList() {
		List<List<Object>> dataList = new ArrayList<List<Object>>();
		for(ReporteGlosaVO reporteGlosaVO : getItems()){
			List<Object> row = reporteGlosaVO.getRow();
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
