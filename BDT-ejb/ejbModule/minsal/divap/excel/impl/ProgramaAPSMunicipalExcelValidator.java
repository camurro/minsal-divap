package minsal.divap.excel.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import minsal.divap.excel.interfaces.ExcelValidator;
import minsal.divap.exception.ExcelFormatException;
import minsal.divap.vo.CellTypeExcelVO;
import minsal.divap.vo.ProgramaAPSMunicipalVO;
import minsal.divap.vo.ProgramaMunicipalSummaryVO;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class ProgramaAPSMunicipalExcelValidator extends ExcelValidator<ProgramaAPSMunicipalVO>{
	private ProgramaMunicipalSummaryVO programaMunicipalSummaryVO;
	
	
	
	public ProgramaAPSMunicipalExcelValidator(Integer numberField, List<CellTypeExcelVO> cells) {
		super(numberField, cells);
	}

	public ProgramaAPSMunicipalExcelValidator(Integer numberField, List<CellTypeExcelVO> cells, Boolean omitHeader) {
		super(numberField, cells, omitHeader);
	}

	public ProgramaAPSMunicipalExcelValidator(Integer numberField, List<CellTypeExcelVO> cells, Integer offsetColumns, Integer offsetRows) {
		super(numberField, cells);
		super.setOffsetColumns(offsetColumns);
		super.setOffsetRows(offsetRows);
	}

	public ProgramaAPSMunicipalExcelValidator(Integer numberField, List<CellTypeExcelVO> cells, Boolean omitHeader, Integer offsetColumns, Integer offsetRows) {
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
		
		//programaMunicipalSummaryVO = getHeader(sheet, 0, 4);
		
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
	protected ProgramaAPSMunicipalVO convert() {
		ProgramaAPSMunicipalVO programaAPSMunicipalVO = new ProgramaAPSMunicipalVO();
		if(getValues().size() == getCells().size()){
			if(!"".equals(getValues().get(0))){
				Double servicio = Double.parseDouble(getValues().get(0));
				programaAPSMunicipalVO.setIdServicio(servicio.intValue());
			}
			if(!"".equals(getValues().get(1))){
				programaAPSMunicipalVO.setServicio(getValues().get(1));
			}
			if(!"".equals(getValues().get(2))){
				Double comuna = Double.parseDouble(getValues().get(2));
				programaAPSMunicipalVO.setIdComuna(comuna.intValue());
			}
			if(!"".equals(getValues().get(3))){
				programaAPSMunicipalVO.setComuna(getValues().get(3));
			}
			if(!"".equals(getValues().get(4))){
				Double tarifa = Double.parseDouble(getValues().get(4));
				programaAPSMunicipalVO.getProgramaMunicipalSummary().getComponentes().get(0).getSubtitulos().get(0).setTarifa(tarifa.intValue());
			}
			if(!"".equals(getValues().get(5))){
				Double cantidad = Double.parseDouble(getValues().get(5));
				programaAPSMunicipalVO.getProgramaMunicipalSummary().getComponentes().get(0).getSubtitulos().get(0).setCantidad(cantidad.intValue());
			}
		}
		return programaAPSMunicipalVO;
	}

	public void validaArmaItems(XSSFSheet sheet, int totalCeldasPxQ) throws ExcelFormatException {
		if(sheet == null){
			throw new ExcelFormatException("La hoja de cálculo esta nula");
		}
		if(getNumberField() <= 0){
			throw new ExcelFormatException("Se debe especificar la cantidad de columnas a leer desde la hoja de cálculo");
		}
		
		//programaMunicipalSummaryVO = getHeader(sheet, 0, 4);
		
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
				add(armaObjeto(totalCeldasPxQ));
			}
		}
		
	}

	private ProgramaAPSMunicipalVO armaObjeto(int totalCeldasPxQ) {
		ProgramaAPSMunicipalVO programaAPSMunicipalVO = new ProgramaAPSMunicipalVO();
		if(getValues().size() == getCells().size()){
			if(!"".equals(getValues().get(0))){
				Double servicio = Double.parseDouble(getValues().get(0));
				programaAPSMunicipalVO.setIdServicio(servicio.intValue());
			}
			if(!"".equals(getValues().get(1))){
				programaAPSMunicipalVO.setServicio(getValues().get(1));
			}
			if(!"".equals(getValues().get(2))){
				Double comuna = Double.parseDouble(getValues().get(2));
				programaAPSMunicipalVO.setIdComuna(comuna.intValue());
			}
			if(!"".equals(getValues().get(3))){
				programaAPSMunicipalVO.setComuna(getValues().get(3));
			}
			int posicion=4;
			for(int i=0; i< totalCeldasPxQ/2; i++){
				if(!"".equals(getValues().get(posicion))){
					Double tarifa = Double.parseDouble(getValues().get(posicion));
					programaAPSMunicipalVO.getProgramaMunicipalSummary().getComponentes().get(0).getSubtitulos().get(0).setTarifa(tarifa.intValue());
				}
				if(!"".equals(getValues().get(posicion+1))){
					Double cantidad = Double.parseDouble(getValues().get(posicion+1));
					programaAPSMunicipalVO.getProgramaMunicipalSummary().getComponentes().get(0).getSubtitulos().get(0).setCantidad(cantidad.intValue());
				}
				posicion++;
			}
			
			
		}
		return programaAPSMunicipalVO;
	}



}
