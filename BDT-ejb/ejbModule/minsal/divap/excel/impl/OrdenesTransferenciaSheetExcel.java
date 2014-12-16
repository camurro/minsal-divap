package minsal.divap.excel.impl;

import java.util.ArrayList;
import java.util.List;

import minsal.divap.excel.interfaces.ExcelTemplate;
import minsal.divap.vo.CellExcelVO;
import minsal.divap.vo.PlanillaResumenFonasaVO;

public class OrdenesTransferenciaSheetExcel extends ExcelTemplate<PlanillaResumenFonasaVO>{
	private List<CellExcelVO> headerComplex = null;
	private List<CellExcelVO> subHeadeComplex = null;
	public OrdenesTransferenciaSheetExcel(List<CellExcelVO> header, List<CellExcelVO> subHeader, List<PlanillaResumenFonasaVO> items) {
		super(null, items);
		headerComplex = header;
		subHeadeComplex = subHeader;
	}
	
	public OrdenesTransferenciaSheetExcel(List<String> cabezas, List<CellExcelVO> header, List<PlanillaResumenFonasaVO> items,Integer offsetRows, Integer offsetColumns) {
		super(cabezas, items, offsetRows, offsetColumns);
		headerComplex = header;
	}
	
		
	@Override
	public List<List<Object>> getDataList() {
		List<List<Object>> dataList = new ArrayList<List<Object>>();
		for(PlanillaResumenFonasaVO programaVO : getItems()){
			List<Object> row = programaVO.getRow();
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
