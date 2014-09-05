package minsal.divap.excel.impl;

import java.util.ArrayList;
import java.util.List;

import minsal.divap.excel.interfaces.ExcelDinamicoVO;
import minsal.divap.excel.interfaces.ExcelTemplate;

public class OTResumenOrdenFormatoFonasaSheetExcel extends ExcelTemplate<ExcelDinamicoVO>{

	
	  public OTResumenOrdenFormatoFonasaSheetExcel(List<String> headers, List<ExcelDinamicoVO> items) {
          super(headers, items);
    }
   
    public OTResumenOrdenFormatoFonasaSheetExcel(List<String> headers, List<ExcelDinamicoVO> items, Integer offsetRows, Integer offsetColumns){
          super(headers, items, offsetRows, offsetColumns);
    }
   
    @Override
    public List<List<Object>> getDataList() {
          List<List<Object>> dataList = new ArrayList<List<Object>>();
          for(ExcelDinamicoVO baseVO : getItems()){
                 List<Object> row = baseVO.getRow();
                 if(row != null){
                        dataList.add(row);
                 }
          }
          return dataList;
    }
}
