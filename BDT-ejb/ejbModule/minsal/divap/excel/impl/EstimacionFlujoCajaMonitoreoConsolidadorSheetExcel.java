package minsal.divap.excel.impl;

import java.util.ArrayList;
import java.util.List;

import minsal.divap.excel.interfaces.ExcelTemplate;
import minsal.divap.vo.CellExcelVO;
import minsal.divap.vo.ResumenFONASAMunicipalVO;
import minsal.divap.vo.ResumenFONASAServicioVO;

public class EstimacionFlujoCajaMonitoreoConsolidadorSheetExcel extends ExcelTemplate<ResumenFONASAMunicipalVO>{
	private List<CellExcelVO> headerComplex = null;
	private List<CellExcelVO> subHeadeComplex = null;
	private List<ResumenFONASAServicioVO> itemSub21 = null;
	private List<ResumenFONASAServicioVO> itemSub22 = null;
	private List<ResumenFONASAServicioVO> itemSub29 = null;
	private Integer posicionColumnaInicial;
	private String mes = null;
	private Integer ano = null;
	private List<Long> totales = null;
	
	public EstimacionFlujoCajaMonitoreoConsolidadorSheetExcel(List<CellExcelVO> header, List<CellExcelVO> subHeader, List<ResumenFONASAMunicipalVO> items) {
		super(null, items);
		headerComplex = header;
		subHeadeComplex = subHeader;
	}
	
	public EstimacionFlujoCajaMonitoreoConsolidadorSheetExcel(List<CellExcelVO> header, List<CellExcelVO> subHeader, List<ResumenFONASAMunicipalVO> items, Integer offsetRows, Integer offsetColumns) {
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

	public List<ResumenFONASAServicioVO> getItemSub21() {
		return itemSub21;
	}

	public void setItemSub21(List<ResumenFONASAServicioVO> itemSub21) {
		this.itemSub21 = itemSub21;
	}

	public List<ResumenFONASAServicioVO> getItemSub22() {
		return itemSub22;
	}

	public void setItemSub22(List<ResumenFONASAServicioVO> itemSub22) {
		this.itemSub22 = itemSub22;
	}

	public List<ResumenFONASAServicioVO> getItemSub29() {
		return itemSub29;
	}

	public void setItemSub29(List<ResumenFONASAServicioVO> itemSub29) {
		this.itemSub29 = itemSub29;
	}

	public Integer getPosicionColumnaInicial() {
		return posicionColumnaInicial;
	}

	public void setPosicionColumnaInicial(Integer posicionColumnaInicial) {
		this.posicionColumnaInicial = posicionColumnaInicial;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}
	
	public List<Long> getTotales() {
		return totales;
	}

	public void setTotales(List<Long> totales) {
		this.totales = totales;
	}

	@Override
	public List<List<Object>> getDataList() {
		List<List<Object>> dataList = new ArrayList<List<Object>>();
		for(ResumenFONASAMunicipalVO resumenFONASAMunicipalVO : getItems()){
			List<Object> row = resumenFONASAMunicipalVO.getRow();
			if(row != null){
				dataList.add(row);
			}						
		}
		return dataList;
	}
	 
}
