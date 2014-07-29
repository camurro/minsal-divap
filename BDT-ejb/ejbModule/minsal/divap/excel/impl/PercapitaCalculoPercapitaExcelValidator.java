package minsal.divap.excel.impl;

import java.util.List;

import minsal.divap.excel.interfaces.ExcelValidator;
import minsal.divap.exception.ExcelFormatException;
import minsal.divap.vo.CalculoPercapitaVO;
import minsal.divap.vo.CellTypeExcelVO;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class PercapitaCalculoPercapitaExcelValidator extends ExcelValidator<CalculoPercapitaVO>{
	public PercapitaCalculoPercapitaExcelValidator(Integer numberField, List<CellTypeExcelVO> cells) {
		super(numberField, cells);
	}

	public PercapitaCalculoPercapitaExcelValidator(Integer numberField, List<CellTypeExcelVO> cells, Boolean omitHeader) {
		super(numberField, cells, omitHeader);
	}

	public PercapitaCalculoPercapitaExcelValidator(Integer numberField, List<CellTypeExcelVO> cells, Integer offsetColumns, Integer offsetRows) {
		super(numberField, cells);
		super.setOffsetColumns(offsetColumns);
		super.setOffsetRows(offsetRows);
	}

	public PercapitaCalculoPercapitaExcelValidator(Integer numberField, List<CellTypeExcelVO> cells, Boolean omitHeader, Integer offsetColumns, Integer offsetRows) {
		super(numberField, cells, omitHeader);
		super.setOffsetColumns(offsetColumns);
		super.setOffsetRows(offsetRows);
	}

	@Override
	public void validateFormat(HSSFSheet sheet) throws ExcelFormatException {
		if(sheet == null){
			throw new ExcelFormatException("La hoja de cálculo esta nula");
		}
		if(getNumberField() <= 0){
			throw new ExcelFormatException("Se debe especificar la cantidad de columnas a leer desde la hoja de cálculo");
		}
		int first = getOffsetRows();
		if(getOmitHeader()){
			first +=1;
		}
		int last = sheet.getPhysicalNumberOfRows();
		System.out.println("Ultima fila->"+last);
		for(;first<=last;first++){
			HSSFRow hssfRow = sheet.getRow(first);
			if(!empty(hssfRow)){
				if(!validateTypes(hssfRow)){
					throw new ExcelFormatException("Los datos de la fila " + first + " no son válidos ");
				}
				add(convert());
			}
		}
	}

	@Override
	public void validateFormat(XSSFSheet sheet) throws ExcelFormatException {
		if(sheet == null){
			throw new ExcelFormatException("La hoja de cálculo esta nula");
		}
		if(getNumberField() <= 0){
			throw new ExcelFormatException("Se debe especificar la cantidad de columnas a leer desde la hoja de cálculo");
		}
		int first = getOffsetRows();
		if(getOmitHeader()){
			first +=1;
		}
		int last = sheet.getPhysicalNumberOfRows();
		for(;first<=last;first++){
			XSSFRow xssfRow = sheet.getRow(first);
			//System.out.println("xssfRow-->"+xssfRow);
			if(!empty(xssfRow)){
				if(!validateTypes(xssfRow)){
					throw new ExcelFormatException("Los datos de la fila " + first + " no son válidos ");
				}
				add(convert());
			}
		}
	}

	@Override
	protected CalculoPercapitaVO convert() {
		CalculoPercapitaVO calculoPercapitaVO = new CalculoPercapitaVO();
		if(getValues().size() == getCells().size()){
			if(!"".equals(getValues().get(0))){
				Double region = Double.parseDouble(getValues().get(0));
				calculoPercapitaVO.setRegion(region.intValue());
			}
			if(!"".equals(getValues().get(1))){
				calculoPercapitaVO.setServicio(getValues().get(1));
			}
			if(!"".equals(getValues().get(2))){
				calculoPercapitaVO.setComuna(getValues().get(2));
			}
			if(!"".equals(getValues().get(3))){
				Double poblacion = Double.parseDouble(getValues().get(3));
				calculoPercapitaVO.setPoblacion(poblacion.intValue());
			}
			if(!"".equals(getValues().get(4))){
				Double poblacionMayor = Double.parseDouble(getValues().get(4));
				calculoPercapitaVO.setPoblacionMayor(poblacionMayor.intValue());
			}
		}
		return calculoPercapitaVO;
	}

}
