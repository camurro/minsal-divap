package minsal.divap.excel.impl;

import java.util.ArrayList;
import java.util.List;

import minsal.divap.excel.interfaces.ExcelTemplate;
import minsal.divap.vo.BaseVO;

public class AsignacionRecursosPercapitaSheetExcel extends ExcelTemplate<BaseVO>{

	public AsignacionRecursosPercapitaSheetExcel(List<String> headers, List<BaseVO> items) {
		super(headers, items);
	}
	
	public AsignacionRecursosPercapitaSheetExcel(List<String> headers, List<BaseVO> items, Integer offsetRows, Integer offsetColumns){
		super(headers, items, offsetRows, offsetColumns);
	}
	
	@Override
	public List<List<Object>> getDataList() {
		List<List<Object>> dataList = new ArrayList<List<Object>>();
		for(BaseVO baseVO : getItems()){
			List<Object> row = baseVO.getRow();
			if(row != null){
				dataList.add(row);
			}
		}
		return dataList;
	}

}
