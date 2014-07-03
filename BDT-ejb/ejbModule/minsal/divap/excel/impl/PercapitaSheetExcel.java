package minsal.divap.excel.impl;

import java.util.ArrayList;
import java.util.List;

import minsal.divap.excel.interfaces.ExcelTemplate;
import minsal.divap.vo.PercapitaExcelVO;

public class PercapitaSheetExcel extends ExcelTemplate<PercapitaExcelVO>{

	public PercapitaSheetExcel(List<String> headers, List<PercapitaExcelVO> items) {
		super(headers, items);
	}
	
	public PercapitaSheetExcel(List<String> headers, List<PercapitaExcelVO> items, Integer offsetRows, Integer offsetColumns){
		super(headers, items, offsetRows, offsetColumns);
	}
	@Override
	public List<List<String>> getDataList() {
		List<List<String>> dataList = new ArrayList<List<String>>();
		for(PercapitaExcelVO percapitaExcelVO : getItems()){
			List<String> row = percapitaExcelVO.getRow();
			if(row != null){
				dataList.add(row);
			}
		}
		return dataList;
	}

}
