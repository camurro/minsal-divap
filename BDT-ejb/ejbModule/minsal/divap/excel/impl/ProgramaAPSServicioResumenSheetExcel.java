package minsal.divap.excel.impl;

import java.util.ArrayList;
import java.util.List;

import minsal.divap.excel.interfaces.ExcelTemplate;
import minsal.divap.vo.CellExcelVO;
import minsal.divap.vo.ProgramaAPSServicioResumenVO;
import minsal.divap.vo.ProgramaAPSServicioVO;
import minsal.divap.vo.ProgramaAPSVO;

public class ProgramaAPSServicioResumenSheetExcel extends ExcelTemplate<ProgramaAPSServicioResumenVO>{
	private List<CellExcelVO> headerComplex = null;
	private List<CellExcelVO> subHeadeComplex = null;
	public ProgramaAPSServicioResumenSheetExcel(List<CellExcelVO> header, List<CellExcelVO> subHeader, List<ProgramaAPSServicioResumenVO> items) {
		super(null, items);
		headerComplex = header;
		subHeadeComplex = subHeader;
	}
	
	public ProgramaAPSServicioResumenSheetExcel(List<CellExcelVO> header, List<CellExcelVO> subHeader, List<ProgramaAPSServicioResumenVO> items, Integer offsetRows, Integer offsetColumns){
		super(null, items, offsetRows, offsetColumns);
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
		// TODO Auto-generated method stub
		return null;
	}

}
