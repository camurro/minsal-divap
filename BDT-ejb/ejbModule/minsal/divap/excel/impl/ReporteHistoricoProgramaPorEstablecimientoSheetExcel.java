package minsal.divap.excel.impl;

import java.util.ArrayList;
import java.util.List;

import minsal.divap.excel.interfaces.ExcelTemplate;
import minsal.divap.vo.CellExcelVO;
import minsal.divap.vo.ReporteHistoricoPorProgramaComunaVO;
import minsal.divap.vo.ReporteHistoricoPorProgramaEstablecimientoForExcelVO;
import minsal.divap.vo.ReporteHistoricoPorProgramaEstablecimientoVO;
import minsal.divap.vo.ReporteMarcoPresupuestarioComunaVO;
import minsal.divap.vo.ReporteMarcoPresupuestarioEstablecimientoVO;

public class ReporteHistoricoProgramaPorEstablecimientoSheetExcel extends ExcelTemplate<ReporteHistoricoPorProgramaEstablecimientoForExcelVO>{
	private List<CellExcelVO> headerComplex = null;
	private List<CellExcelVO> subHeadeComplex = null;

	public ReporteHistoricoProgramaPorEstablecimientoSheetExcel(List<CellExcelVO> header,List<CellExcelVO> subHeader, List<ReporteHistoricoPorProgramaEstablecimientoForExcelVO> items) {
		super(null, items);
		headerComplex = header;
		subHeadeComplex = subHeader;
		
	}
	
	public ReporteHistoricoProgramaPorEstablecimientoSheetExcel(List<CellExcelVO> header,List<CellExcelVO> subHeader, List<ReporteHistoricoPorProgramaEstablecimientoForExcelVO> items, Integer offsetRows, Integer offsetColumns){
		super(null, items);
		headerComplex = header;
		subHeadeComplex = subHeader;
	}

	@Override
	public List<List<Object>> getDataList() {
		List<List<Object>> dataList = new ArrayList<List<Object>>();
		for(ReporteHistoricoPorProgramaEstablecimientoForExcelVO reporteHistoricoPorProgramaEstablecimientoForExcelVO : getItems()){
			List<Object> row = reporteHistoricoPorProgramaEstablecimientoForExcelVO.getRow();
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
