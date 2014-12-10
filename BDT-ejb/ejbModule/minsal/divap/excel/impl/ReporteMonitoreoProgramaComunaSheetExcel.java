package minsal.divap.excel.impl;

import java.util.ArrayList;
import java.util.List;

import minsal.divap.excel.interfaces.ExcelTemplate;
import minsal.divap.vo.CellExcelVO;
import minsal.divap.vo.ReporteMonitoreoProgramaPorComunaVO;
import minsal.divap.vo.ReporteRebajaVO;

public class ReporteMonitoreoProgramaComunaSheetExcel extends ExcelTemplate<ReporteMonitoreoProgramaPorComunaVO>{
	private List<CellExcelVO> headerComplex = null;
	private List<CellExcelVO> subHeadeComplex = null;

	public ReporteMonitoreoProgramaComunaSheetExcel(List<CellExcelVO> header,List<CellExcelVO> subHeader, List<ReporteMonitoreoProgramaPorComunaVO> items) {
		super(null, items);
		headerComplex = header;
		subHeadeComplex = subHeader;
		
	}
	
	public ReporteMonitoreoProgramaComunaSheetExcel(List<CellExcelVO> header,List<CellExcelVO> subHeader, List<ReporteMonitoreoProgramaPorComunaVO> items, Integer offsetRows, Integer offsetColumns){
		super(null, items);
		headerComplex = header;
		subHeadeComplex = subHeader;
	}

	@Override
	public List<List<Object>> getDataList() {
		List<List<Object>> dataList = new ArrayList<List<Object>>();
		for(ReporteMonitoreoProgramaPorComunaVO reporteMonitoreoProgramaPorComunaVO : getItems()){
			List<Object> row = reporteMonitoreoProgramaPorComunaVO.getRow();
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
