package minsal.divap.excel.impl;

import java.util.ArrayList;
import java.util.List;

import minsal.divap.excel.interfaces.ExcelTemplate;
import minsal.divap.vo.RebajaVO;

public class RebajaSheetExcel extends ExcelTemplate<RebajaVO>{

	private List<String> subHeaders;
	
	public RebajaSheetExcel(List<String> headers,List<String> subHeaders, List<RebajaVO> items) {
		super(headers, items);
		this.subHeaders=subHeaders;
	}
	
	public RebajaSheetExcel(List<String> headers, List<RebajaVO> items, Integer offsetRows, Integer offsetColumns){
		super(headers, items, offsetRows, offsetColumns);
	}
	
	@Override
	public List<List<String>> getDataList() {
		List<List<String>> dataList = new ArrayList<List<String>>();
		for(RebajaVO rebajaVO : getItems()){
			List<String> row = rebajaVO.getRow();
			if(row != null){
				dataList.add(row);
			}
		}
		return dataList;
	}

	public List<String> getSubHeaders() {
		return subHeaders;
	}

	public void setSubHeaders(List<String> subHeaders) {
		this.subHeaders = subHeaders;
	}

	
}