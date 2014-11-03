package minsal.divap.excel.impl;

import java.util.ArrayList;
import java.util.List;

import minsal.divap.excel.interfaces.ExcelTemplate;
import minsal.divap.vo.CellExcelVO;
import minsal.divap.vo.ValorizarReliquidacionSummaryVO;

public class PlanillaTrabajoCumplimientoReliquidacionMunicipalSheetExcel extends ExcelTemplate<ValorizarReliquidacionSummaryVO> {
	private List<CellExcelVO> headerComplex = null;
	private List<CellExcelVO> subHeadeComplex = null;

	public PlanillaTrabajoCumplimientoReliquidacionMunicipalSheetExcel(List<CellExcelVO> header, List<CellExcelVO> subHeader, List<ValorizarReliquidacionSummaryVO> items) {
		super(null, items);
		headerComplex = header;
		subHeadeComplex = subHeader;
	}
	
	public PlanillaTrabajoCumplimientoReliquidacionMunicipalSheetExcel(List<CellExcelVO> header, List<CellExcelVO> subHeader, List<ValorizarReliquidacionSummaryVO> items, Integer offsetRows, Integer offsetColumns) {
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
		for(ValorizarReliquidacionSummaryVO valorizarReliquidacionSummaryVO : getItems()){
			List<Object> row = valorizarReliquidacionSummaryVO.getRow();
			if(row != null){
				dataList.add(row);
			}						
		}
		return dataList;
	}
	

}
