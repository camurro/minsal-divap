package minsal.divap.excel.impl;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import minsal.divap.enums.FieldType;
import minsal.divap.excel.interfaces.ExcelValidator;
import minsal.divap.excel.util.ExcelExtensionValidator;
import minsal.divap.exception.ExcelFormatException;
import minsal.divap.vo.CellTypeExcelVO;
import minsal.divap.vo.DesempenoDificilVO;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PercapitaDesempenoDificilExcelValidator extends ExcelValidator<DesempenoDificilVO>{
	public PercapitaDesempenoDificilExcelValidator(Integer numberField, List<CellTypeExcelVO> cells) {
		super(numberField, cells);
	}

	public PercapitaDesempenoDificilExcelValidator(Integer numberField, List<CellTypeExcelVO> cells, Boolean omitHeader) {
		super(numberField, cells, omitHeader);
	}

	public PercapitaDesempenoDificilExcelValidator(Integer numberField, List<CellTypeExcelVO> cells, Integer offsetColumns, Integer offsetRows) {
		super(numberField, cells);
		super.setOffsetColumns(offsetColumns);
		super.setOffsetRows(offsetRows);
	}

	public PercapitaDesempenoDificilExcelValidator(Integer numberField, List<CellTypeExcelVO> cells, Boolean omitHeader, Integer offsetColumns, Integer offsetRows) {
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
			System.out.println("xssfRow-->"+xssfRow);
			if(!empty(xssfRow)){
				if(!validateTypes(xssfRow)){
					throw new ExcelFormatException("Los datos de la fila " + first + " no son válidos ");
				}
				add(convert());
			}
		}
	}

	@Override
	protected DesempenoDificilVO convert() {
		DesempenoDificilVO desempenoDificilVO = new DesempenoDificilVO();
		if(getValues().size() == getCells().size()){
			if(!"".equals(getValues().get(0))){
				Double region = Double.parseDouble(getValues().get(0));
				desempenoDificilVO.setRegion( region.intValue());
			}
			if(!"".equals(getValues().get(1))){
				desempenoDificilVO.setServicio(getValues().get(1));
			}
			if(!"".equals(getValues().get(2))){
				desempenoDificilVO.setComuna(getValues().get(2));
			}
			if(!"".equals(getValues().get(3))){
				Double desempenoDificil = Double.parseDouble(getValues().get(3));
				desempenoDificilVO.setValorDesempenoDificil(desempenoDificil.intValue());
			}
		}
		return desempenoDificilVO;
	}

	public static void main(String[] args) {
		try {
			URL url = new URL("file:///home/cmurillo/MinSal/PERCAPITA.xls");
			List<CellTypeExcelVO> cells = new ArrayList<CellTypeExcelVO>();
			CellTypeExcelVO fieldOne = new CellTypeExcelVO(true, FieldType.INTEGERFIELD);
			cells.add(fieldOne);
			CellTypeExcelVO fieldTwo = new CellTypeExcelVO(true);
			cells.add(fieldTwo);
			CellTypeExcelVO fieldThree = new CellTypeExcelVO(true);
			cells.add(fieldThree);
			ExcelExtensionValidator excelExtensionValidator = new ExcelExtensionValidator();
			if(excelExtensionValidator.validate(url.getFile())){
				if(url.getFile().endsWith("xls")){
					HSSFWorkbook workbook = new HSSFWorkbook(url.openStream());
					HSSFSheet worksheet = workbook.getSheetAt(0);
					PercapitaDesempenoDificilExcelValidator rebajaExcelValidator = new PercapitaDesempenoDificilExcelValidator(cells.size(), cells, true, 1, 1);
					rebajaExcelValidator.validateFormat(worksheet);		
					List<DesempenoDificilVO> items = rebajaExcelValidator.getItems();
					for(DesempenoDificilVO item : items){
						System.out.println("item->"+item);
					}
				}else{
					XSSFWorkbook workbook = new XSSFWorkbook (url.openStream());
					XSSFSheet worksheet = workbook.getSheetAt(0);
					PercapitaDesempenoDificilExcelValidator rebajaExcelValidator = new PercapitaDesempenoDificilExcelValidator(cells.size(), cells, true, 1, 1);
					rebajaExcelValidator.validateFormat(worksheet);		
					List<DesempenoDificilVO> items = rebajaExcelValidator.getItems();
					for(DesempenoDificilVO item : items){
						System.out.println("item->"+item);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
