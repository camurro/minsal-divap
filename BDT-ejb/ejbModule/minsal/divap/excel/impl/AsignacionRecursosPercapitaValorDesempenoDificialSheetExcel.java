package minsal.divap.excel.impl;

import java.util.ArrayList;
import java.util.List;

import minsal.divap.excel.interfaces.ExcelTemplate;
import minsal.divap.vo.AsignacionDistribucionPerCapitaVO;

public class AsignacionRecursosPercapitaValorDesempenoDificialSheetExcel extends ExcelTemplate<AsignacionDistribucionPerCapitaVO>{

	public AsignacionRecursosPercapitaValorDesempenoDificialSheetExcel(List<String> headers, List<AsignacionDistribucionPerCapitaVO> items) {
		super(headers, items);
	}
	
	public AsignacionRecursosPercapitaValorDesempenoDificialSheetExcel(List<String> headers, List<AsignacionDistribucionPerCapitaVO> items, Integer offsetRows, Integer offsetColumns){
		super(headers, items, offsetRows, offsetColumns);
	}
	
	@Override
	public List<List<Object>> getDataList() {
		List<List<Object>> dataList = new ArrayList<List<Object>>();
		for(AsignacionDistribucionPerCapitaVO baseVO : getItems()){
			List<Object> row = baseVO.getRow();
			if(row != null){
				dataList.add(row);
			}
		}
		return dataList;
	}

}
