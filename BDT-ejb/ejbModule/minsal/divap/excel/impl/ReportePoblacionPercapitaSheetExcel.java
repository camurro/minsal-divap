package minsal.divap.excel.impl;

import java.util.ArrayList;
import java.util.List;

import minsal.divap.excel.interfaces.ExcelTemplate;
import minsal.divap.vo.CellExcelVO;
import minsal.divap.vo.ReportePerCapitaVO;

public class ReportePoblacionPercapitaSheetExcel extends ExcelTemplate<ReportePerCapitaVO>{
	private List<CellExcelVO> headerComplex = null;

	public ReportePoblacionPercapitaSheetExcel(List<CellExcelVO> header, List<ReportePerCapitaVO> items) {
		super(null, items);
		headerComplex = header;
		
	}

	@Override
	public List<List<Object>> getDataList() {
		List<List<Object>> dataList = new ArrayList<List<Object>>();
		for(ReportePerCapitaVO reportePerCapitaVO : getItems()){
			List<Object> row = reportePerCapitaVO.getRow();
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
	
}
