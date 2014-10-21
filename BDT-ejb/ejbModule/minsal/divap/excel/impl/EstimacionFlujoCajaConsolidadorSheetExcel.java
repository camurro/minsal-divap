package minsal.divap.excel.impl;

import java.util.ArrayList;
import java.util.List;

import minsal.divap.excel.interfaces.ExcelTemplate;
import minsal.divap.vo.CellExcelVO;
import minsal.divap.vo.SubtituloFlujoCajaVO;

public class EstimacionFlujoCajaConsolidadorSheetExcel extends ExcelTemplate<SubtituloFlujoCajaVO>{
	private List<CellExcelVO> headerComplex = null;
	private List<CellExcelVO> subHeadeComplex = null;
	
	public EstimacionFlujoCajaConsolidadorSheetExcel(List<CellExcelVO> header, List<CellExcelVO> subHeader, List<SubtituloFlujoCajaVO> items) {
		super(null, items);
		headerComplex = header;
		subHeadeComplex = subHeader;
	}
	
	public EstimacionFlujoCajaConsolidadorSheetExcel(List<CellExcelVO> header, List<CellExcelVO> subHeader, List<SubtituloFlujoCajaVO> items, Integer offsetRows, Integer offsetColumns) {
		super(null, items);
		headerComplex = header;
		subHeadeComplex = subHeader;	
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

	@Override
	public List<List<Object>> getDataList() {
		List<List<Object>> dataList = new ArrayList<List<Object>>();
		for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : getItems()){
			List<Object> row = subtituloFlujoCajaVO.getRow();
			if(row != null){
				dataList.add(row);
			}						
		}
		return dataList;
	}
}
