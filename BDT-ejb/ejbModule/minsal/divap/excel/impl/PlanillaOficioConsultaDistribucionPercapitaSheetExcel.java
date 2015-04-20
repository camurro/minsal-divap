package minsal.divap.excel.impl;

import java.util.ArrayList;
import java.util.List;

import minsal.divap.excel.interfaces.ExcelTemplate;
import minsal.divap.vo.OficioConsultaDistribucionPerCapitaVO;

public class PlanillaOficioConsultaDistribucionPercapitaSheetExcel extends ExcelTemplate<OficioConsultaDistribucionPerCapitaVO>{

	public PlanillaOficioConsultaDistribucionPercapitaSheetExcel(List<String> headers, List<OficioConsultaDistribucionPerCapitaVO> items) {
		super(headers, items);
	}
	
	public PlanillaOficioConsultaDistribucionPercapitaSheetExcel(List<String> headers, List<OficioConsultaDistribucionPerCapitaVO> items, Integer offsetRows, Integer offsetColumns){
		super(headers, items, offsetRows, offsetColumns);
	}
	
	@Override
	public List<List<Object>> getDataList() {
		List<List<Object>> dataList = new ArrayList<List<Object>>();
		for(OficioConsultaDistribucionPerCapitaVO oficioConsultaDistribucionPerCapitaVO : getItems()){
			List<Object> row = oficioConsultaDistribucionPerCapitaVO.getRow();
			if(row != null){
				dataList.add(row);
			}
		}
		return dataList;
	}

}
