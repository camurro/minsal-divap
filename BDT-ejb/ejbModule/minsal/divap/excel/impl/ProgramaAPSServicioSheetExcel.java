package minsal.divap.excel.impl;

import java.util.ArrayList;
import java.util.List;

import minsal.divap.excel.interfaces.ExcelTemplate;
import minsal.divap.vo.CellExcelVO;
import minsal.divap.vo.ProgramaAPSServicioVO;
import minsal.divap.vo.ProgramaAPSVO;

public class ProgramaAPSServicioSheetExcel extends ExcelTemplate<ProgramaAPSServicioVO>{
	private List<CellExcelVO> headerComplex = null;
	private List<CellExcelVO> subHeadeComplex = null;
	public ProgramaAPSServicioSheetExcel(List<CellExcelVO> header, List<CellExcelVO> subHeader, List<ProgramaAPSServicioVO> items) {
		super(null, items);
		headerComplex = header;
		subHeadeComplex = subHeader;
	}
	
	public ProgramaAPSServicioSheetExcel(List<CellExcelVO> header, List<CellExcelVO> subHeader, List<ProgramaAPSServicioVO> items, Integer offsetRows, Integer offsetColumns){
		super(null, items, offsetRows, offsetColumns);
		headerComplex = header;
		subHeadeComplex = subHeader;
	}
	
	@Override
	public List<List<Object>> getDataList() {
		List<List<Object>> dataList = new ArrayList<List<Object>>();
		for(ProgramaAPSServicioVO programaAPSServicioVO : getItems()){
			List<Object> row = programaAPSServicioVO.getRow();
			if(row != null){
				dataList.add(row);
			}
		}
		return dataList;
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

}
