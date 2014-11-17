package minsal.divap.excel.impl;

import java.util.List;

import minsal.divap.excel.interfaces.ExcelTemplate;
import minsal.divap.vo.CellExcelVO;
import minsal.divap.vo.ProgramaAPSServicioResumenVO;

public class ProgramaAPSServiciosDetallesSheetExcel extends ExcelTemplate<ProgramaAPSServicioResumenVO>{
	private List<CellExcelVO> headerComplex = null;
	private List<CellExcelVO> subHeadeComplex = null;
	public ProgramaAPSServiciosDetallesSheetExcel(List<CellExcelVO> header, List<CellExcelVO> subHeader, List<ProgramaAPSServicioResumenVO> items) {
		super(null, items);
		headerComplex = header;
		subHeadeComplex = subHeader;
	}
	
	public ProgramaAPSServiciosDetallesSheetExcel(List<CellExcelVO> header, List<CellExcelVO> subHeader, List<ProgramaAPSServicioResumenVO> items, Integer offsetRows, Integer offsetColumns){
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
