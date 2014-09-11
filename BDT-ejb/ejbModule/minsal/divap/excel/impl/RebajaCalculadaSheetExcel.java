package minsal.divap.excel.impl;

import java.util.ArrayList;
import java.util.List;

import minsal.divap.excel.interfaces.ExcelTemplate;
import minsal.divap.vo.PlanillaRebajaCalculadaVO;

public class RebajaCalculadaSheetExcel extends ExcelTemplate<PlanillaRebajaCalculadaVO>{

	private List<String> subHeaders;
	
	public RebajaCalculadaSheetExcel(List<String> headers, List<String> subHeaders,List<PlanillaRebajaCalculadaVO> datos) {
		super(headers, datos);
		this.subHeaders=subHeaders;
	}
	
	@Override
	public List<List<Object>> getDataList() {
		List<List<Object>> dataList = new ArrayList<List<Object>>();
		for(PlanillaRebajaCalculadaVO planillaRebaja : getItems()){
			List<Object> row = planillaRebaja.getRow();
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
