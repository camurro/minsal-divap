package minsal.divap.excel.impl;

import java.util.ArrayList;
import java.util.List;

import minsal.divap.excel.interfaces.ExcelTemplate;
import minsal.divap.vo.BaseVO;

public class ReliquidacionSheetExcel extends ExcelTemplate<BaseVO>{

	public ReliquidacionSheetExcel(List<String> headers, List<BaseVO> items) {
		super(headers, items);
	}
	
	public ReliquidacionSheetExcel(List<String> headers, List<BaseVO> items, Integer offsetRows, Integer offsetColumns){
		super(headers, items, offsetRows, offsetColumns);
	}
	
	@Override
	public List<List<String>> getDataList() {
		List<List<String>> dataList = new ArrayList<List<String>>();
		for(BaseVO baseVO : getItems()){
			List<String> row = baseVO.getRow();
			if(row != null){
				dataList.add(row);
			}
		}
		return dataList;
	}

}
