package minsal.divap.excel.impl;

import java.util.ArrayList;
import java.util.List;

import minsal.divap.excel.interfaces.ExcelValidator;
import minsal.divap.exception.ExcelFormatException;
import minsal.divap.vo.CalculoReliquidacionBaseVO;
import minsal.divap.vo.CellTypeExcelVO;
import minsal.divap.vo.ComponenteCumplimientoVO;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class ReliquidacionCalculoExcelValidator extends ExcelValidator<CalculoReliquidacionBaseVO>{
	public ReliquidacionCalculoExcelValidator(Integer numberField, List<CellTypeExcelVO> cells) {
		super(numberField, cells);
	}

	public ReliquidacionCalculoExcelValidator(Integer numberField, List<CellTypeExcelVO> cells, Boolean omitHeader) {
		super(numberField, cells, omitHeader);
	}

	public ReliquidacionCalculoExcelValidator(Integer numberField, List<CellTypeExcelVO> cells, Integer offsetColumns, Integer offsetRows) {
		super(numberField, cells);
		super.setOffsetColumns(offsetColumns);
		super.setOffsetRows(offsetRows);
	}

	public ReliquidacionCalculoExcelValidator(Integer numberField, List<CellTypeExcelVO> cells, Boolean omitHeader, Integer offsetColumns, Integer offsetRows) {
		super(numberField, cells, omitHeader);
		super.setOffsetColumns(offsetColumns);
		super.setOffsetRows(offsetRows);
	}

	@Override
	public void validateFormat(HSSFSheet sheet) throws ExcelFormatException {
		System.out.println("pasa por aca");
		if(sheet == null){
			throw new ExcelFormatException("La hoja de cálculo esta nula");
		}
		if(getNumberField() <= 0){
			throw new ExcelFormatException("Se debe especificar la cantidad de columnas a leer desde la hoja de cálculo");
		}
		int first = getOffsetRows();
		if(getOmitHeader()){
			first +=4;
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
			if(!empty(xssfRow)){
				if(!validateTypes(xssfRow)){
					throw new ExcelFormatException("Los datos de la fila " + first + " no son válidos ");
				}
				add(convert());
			}
		}
	}

	@Override
	protected CalculoReliquidacionBaseVO convert() {
		CalculoReliquidacionBaseVO calculoReliquidacionBaseVO = new CalculoReliquidacionBaseVO();
		List<ComponenteCumplimientoVO> porc_cumplimiento = new ArrayList<ComponenteCumplimientoVO>();
		if(getValues().size() == getCells().size()){
			if(!"".equals(getValues().get(0))){
				Double value =  Double.parseDouble(getValues().get(0));
				Integer id_servicio = value.intValue();
				calculoReliquidacionBaseVO.setId_servicio(id_servicio.intValue());
			}
			if(!"".equals(getValues().get(1))){
				calculoReliquidacionBaseVO.setServicio(getValues().get(1));
			}
			if(!"".equals(getValues().get(2))){
				Double value =  Double.parseDouble(getValues().get(2));
				Integer id_comuna = value.intValue();
				calculoReliquidacionBaseVO.setId_comuna(id_comuna);
			}
			if(!"".equals(getValues().get(3))){
				calculoReliquidacionBaseVO.setComuna(getValues().get(3));
			}
			for(int i=4;i <getCells().size();i++){
				if(!"".equals(getValues().get(i))){
					Double p_cumplimiento = Double.parseDouble(getValues().get(i));
					ComponenteCumplimientoVO componenteCumplimientoVO = new ComponenteCumplimientoVO();
					componenteCumplimientoVO.setPorcentajeCumplimiento(p_cumplimiento);
					porc_cumplimiento.add(componenteCumplimientoVO);
				}
			}
			calculoReliquidacionBaseVO.setComponentesCumplimientoVO(porc_cumplimiento);
			
		}
		return calculoReliquidacionBaseVO;
	}

}
