package minsal.divap.excel.impl;

import java.util.ArrayList;
import java.util.List;

import minsal.divap.excel.interfaces.ExcelTemplate;
import minsal.divap.vo.CellExcelVO;
import minsal.divap.vo.CumplimientoApsMunicipalProgramaVO;

public class CrearPlanillaCumplimientoMunicialProgramaSheetExcel extends ExcelTemplate<CumplimientoApsMunicipalProgramaVO> {
	private List<CellExcelVO> headerComplex = null;
	private List<CellExcelVO> subHeadeComplex = null;

	public CrearPlanillaCumplimientoMunicialProgramaSheetExcel(List<CellExcelVO> header, List<CellExcelVO> subHeader, List<CumplimientoApsMunicipalProgramaVO> items) {
		super(null, items);
		headerComplex = header;
		subHeadeComplex = subHeader;
	}
	
	public CrearPlanillaCumplimientoMunicialProgramaSheetExcel(List<CellExcelVO> header, List<CellExcelVO> subHeader, List<CumplimientoApsMunicipalProgramaVO> items, Integer offsetRows, Integer offsetColumns) {
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
		for(CumplimientoApsMunicipalProgramaVO cumplimientoApsMunicipalProgramaVO : getItems()){
			List<Object> row = cumplimientoApsMunicipalProgramaVO.getRow();
			if(row != null){
				dataList.add(row);
			}						
		}
		return dataList;
	}
	

}
