package minsal.divap.excel.impl;

import java.util.List;
import java.util.ArrayList;

import minsal.divap.excel.interfaces.ExcelTemplate;
import minsal.divap.vo.CellExcelVO;
import minsal.divap.vo.FlujoCajaVO;

public class EstimacionFlujoCajaSubtituloSheetExcel extends ExcelTemplate<FlujoCajaVO> {
	private List<CellExcelVO> headerComplex = null;
	private List<CellExcelVO> subHeadeComplex = null;

	public EstimacionFlujoCajaSubtituloSheetExcel(List<CellExcelVO> header, List<CellExcelVO> subHeader, List<FlujoCajaVO> items) {
		super(null, items);
		headerComplex = header;
		subHeadeComplex = subHeader;
	}
	
	public EstimacionFlujoCajaSubtituloSheetExcel(List<CellExcelVO> header, List<CellExcelVO> subHeader, List<FlujoCajaVO> items, Integer offsetRows, Integer offsetColumns) {
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
		for(FlujoCajaVO flujoCajaVO : getItems()){
			List<Object> row = flujoCajaVO.getRow();
			if(row != null){
				dataList.add(row);
			}						
		}
		
		return dataList;
	}
	

}
