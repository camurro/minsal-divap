package minsal.divap.excel.interfaces;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import minsal.divap.exception.ExcelFormatException;
import minsal.divap.vo.CellTypeExcelVO;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public abstract class ExcelValidator<T>{

	private Integer numberField = 0;
	private Boolean omitHeader = false;
	private Integer offsetRows = 0;
	private Integer offsetColumns = 0;
	private List<CellTypeExcelVO> cells;
	private List<String> values = null;

	private List<T> items = new ArrayList<T>();

	public abstract void validateFormat(HSSFSheet sheet) throws ExcelFormatException;
	public abstract void validateFormat(XSSFSheet sheet) throws ExcelFormatException;

	protected abstract T convert();

	public ExcelValidator(Integer numberField, List<CellTypeExcelVO> cells){
		this.numberField = numberField;
		this.cells = cells;
	}

	public ExcelValidator(Integer numberField, List<CellTypeExcelVO> cells, Boolean omitHeader){
		this.numberField = numberField;
		this.omitHeader = omitHeader;
		this.cells = cells;
	}

	public boolean validateTypes(HSSFRow hssfRow){
		if(hssfRow == null){
			return false;
		}
		if(getNumberField() <= 0){
			return false;
		}
		if(getCells() == null || getCells().isEmpty()){
			return false;
		}

		int first = getOffsetColumns();
		boolean isValidRow = true;
		values = new ArrayList<String>();
		for(CellTypeExcelVO cellType : getCells()){
			switch (cellType.getType()) {
			case STRINGFIELD:
				try{
					String value = "";
					if(cellType.getRequired()){
						value = hssfRow.getCell(first++).getRichStringCellValue().getString();
					}else{
						if(hssfRow.getCell(first).getCellType() != 3){
							value = hssfRow.getCell(first++).getRichStringCellValue().getString();
						}
					}
					values.add(value);
				}catch (Exception e) {
					isValidRow = false;
				}
				break;
			case INTEGERFIELD:
			case DOUBLEFIELD:
				try{
					String value = "";
					if(cellType.getRequired()){
						value = "" + hssfRow.getCell(first++).getNumericCellValue();
					}else{
						if(hssfRow.getCell(first).getCellType() != 3){
							value = "" + hssfRow.getCell(first++).getNumericCellValue();
						}
					}
					values.add(value);
				}catch (Exception e) {
					isValidRow = false;
				}
				break;
			case BOOLEANFIELD:
				try{
					String value = "";
					if(cellType.getRequired()){
						value = "" + hssfRow.getCell(first++).getBooleanCellValue();
					}else{
						if(hssfRow.getCell(first).getCellType() != 3){
							value = "" + hssfRow.getCell(first++).getBooleanCellValue();
						}
					}	
					values.add(value);
				}catch (Exception e) {
					isValidRow = false;
				}
				break;
			case PERCENTAGEFIELD:
				try{
					String value = "";
					if(cellType.getRequired()){
						if(hssfRow.getCell(first).getCellStyle().getDataFormatString().contains("%")){
							Double valueTmp = hssfRow.getCell(first++).getNumericCellValue() * 100;
							value = "" +valueTmp;
						}else{
							Double valueTmp = hssfRow.getCell(first++).getNumericCellValue();
							value = "" + valueTmp;
						}
					}else{
						if(hssfRow.getCell(first).getCellType() != 3){
							if(hssfRow.getCell(first).getCellStyle().getDataFormatString().contains("%")){
								Double valueTmp = hssfRow.getCell(first++).getNumericCellValue() * 100;
								value = "" +valueTmp;
							}else{
								Double valueTmp = hssfRow.getCell(first++).getNumericCellValue();
								value = "" + valueTmp;
							}
						}
					}	
					values.add(value);
				}catch (Exception e) {
					isValidRow = false;
				}
				break;
			}
			if(!isValidRow){
				break;
			}
		}
		return isValidRow;
	}

	public boolean validateTypes(XSSFRow xssfRow){
		if(xssfRow == null){
			return false;
		}
		if(getNumberField() <= 0){
			return false;
		}
		if(getCells() == null || getCells().isEmpty()){
			return false;
		}

		int first = getOffsetColumns();
		boolean isValidRow = true;
		values = new ArrayList<String>();
		for(CellTypeExcelVO cellType : getCells()){
			switch (cellType.getType()) {
			case STRINGFIELD:
				try{
					String value = "";
					if(cellType.getRequired()){
						//System.out.println("xssfRow.getCell("+first+").getRichStringCellValue().getString()-->" + xssfRow.getCell(first).getRichStringCellValue().getString());
						value = xssfRow.getCell(first++).getRichStringCellValue().getString();
					}else{
						if(xssfRow.getCell(first).getCellType() != 3){
							value = xssfRow.getCell(first++).getRichStringCellValue().getString();
						}
					}
					values.add(value);
				}catch (Exception e) {
					isValidRow = false;
				}
				break;
			case INTEGERFIELD:
			case DOUBLEFIELD:
				try{
					String value = "";
					if(cellType.getRequired()){
						//System.out.println("xssfRow.getCell("+first+").getNumericCellValue().getString()-->" + xssfRow.getCell(first).getNumericCellValue());
						value = "" + xssfRow.getCell(first++).getNumericCellValue();
					}else{
						if(xssfRow.getCell(first).getCellType() != 3){
							value = "" + xssfRow.getCell(first++).getNumericCellValue();
						}
					}
					values.add(value);
				}catch (Exception e) {
					isValidRow = false;
				}
				break;
			case BOOLEANFIELD:
				try{
					String value = "";
					if(cellType.getRequired()){
						value = "" + xssfRow.getCell(first++).getBooleanCellValue();
					}else{
						if(xssfRow.getCell(first).getCellType() != 3){
							value = "" + xssfRow.getCell(first++).getBooleanCellValue();
						}
					}
					values.add(value);
				}catch (Exception e) {
					isValidRow = false;
				}
				break;
			case PERCENTAGEFIELD:
				try{
					String value = "";
					if(cellType.getRequired()){
						if(xssfRow.getCell(first).getCellStyle().getDataFormatString().contains("%")){
							Double valueTmp = xssfRow.getCell(first++).getNumericCellValue() * 100;
							BigDecimal bd = new BigDecimal(valueTmp);
							bd = bd.setScale(2, RoundingMode.HALF_UP);
							value = "" + bd.doubleValue();
						}else{
							Double valueTmp = xssfRow.getCell(first++).getNumericCellValue();
							BigDecimal bd = new BigDecimal(valueTmp);
							bd = bd.setScale(2, RoundingMode.HALF_UP);
							value = "" + bd.doubleValue();
						}
					}else{
						if(xssfRow.getCell(first).getCellType() != 3){
							if(xssfRow.getCell(first).getCellStyle().getDataFormatString().contains("%")){
								Double valueTmp = xssfRow.getCell(first++).getNumericCellValue() * 100;
								value = "" +valueTmp;
							}else{
								Double valueTmp = xssfRow.getCell(first++).getNumericCellValue();
								value = "" + valueTmp;
							}
						}
					}	
					values.add(value);
				}catch (Exception e) {
					isValidRow = false;
					e.printStackTrace();
				}
				break;
			}
			if(!isValidRow){
				break;
			}
		}
		return isValidRow;
	}

	public boolean empty(HSSFRow hssfRow) {
		if(null == hssfRow){
			return true;
		}
		for (Cell cell : hssfRow) {
			if(cell.getCellType() != 3){
				return false;
			}
		}
		return true;
	}

	public boolean empty(XSSFRow hssfRow) {
		if(null == hssfRow){
			return true;
		}
		for (Cell cell : hssfRow) {
			if(cell.getCellType() != 3){
				return false;
			}
		}
		return true;
	}

	public Integer getNumberField() {
		return numberField;
	}

	public void setNumberField(Integer numberField) {
		this.numberField = numberField;
	}

	public Boolean getOmitHeader() {
		return omitHeader;
	}

	public void setOmitHeader(Boolean omitHeader) {
		this.omitHeader = omitHeader;
	}

	public Integer getOffsetRows() {
		return offsetRows;
	}

	public void setOffsetRows(Integer offsetRows) {
		this.offsetRows = offsetRows;
	}

	public Integer getOffsetColumns() {
		return offsetColumns;
	}

	public void setOffsetColumns(Integer offsetColumns) {
		this.offsetColumns = offsetColumns;
	}

	public List<CellTypeExcelVO> getCells() {
		return cells;
	}

	public void setCells(List<CellTypeExcelVO> cells) {
		this.cells = cells;
	}

	public void add(T item) {
		items.add(item);
	}

	public List<T> getItems() {
		return items;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

}
