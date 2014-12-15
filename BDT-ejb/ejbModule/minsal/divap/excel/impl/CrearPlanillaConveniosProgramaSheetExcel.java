package minsal.divap.excel.impl;

import java.util.ArrayList;
import java.util.List;

import minsal.divap.excel.interfaces.ExcelTemplate;
import minsal.divap.vo.CellExcelVO;

public class CrearPlanillaConveniosProgramaSheetExcel extends ExcelTemplate<List<String>> {
	private List<CellExcelVO> headerComplex = null;
	private List<CellExcelVO> subHeadeComplex = null;

	public CrearPlanillaConveniosProgramaSheetExcel(List<CellExcelVO> header, List<CellExcelVO> subHeader, List<List<String>> items) {
		super(null, items);
		headerComplex = header;
		subHeadeComplex = subHeader;
	}
	
	public CrearPlanillaConveniosProgramaSheetExcel(List<CellExcelVO> header, List<CellExcelVO> subHeader, List<List<String>> items, Integer offsetRows, Integer offsetColumns) {
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
		for(List<String> filaCompleta : getItems()){
			List<Object> row = new ArrayList<Object>();
			for(String campo : filaCompleta){
				row.add(campo);
			}
			dataList.add(row);
		}
		return dataList;
	}
	

}
