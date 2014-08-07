package minsal.divap.excel;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import minsal.divap.excel.impl.RebajaCalculadaSheetExcel;
import minsal.divap.excel.impl.RebajaSheetExcel;
import minsal.divap.excel.interfaces.ExcelTemplate;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class GeneradorExcel {
	private XSSFWorkbook workbook = null;
	private String fileName;
	private Integer numberSheet;

	public GeneradorExcel(String fileName){
		numberSheet = 0;
		this.fileName = fileName;
	}

	public static <T> T fromContent(byte [] content, Class<T> clazz) throws InvalidFormatException, IOException {
		System.out.println("GeneradorExcel fromContent clazz.getName()-->"+clazz.getSimpleName());
		if("XSSFWorkbook".equals(clazz.getSimpleName())){
			return (T)createXlsx(content);
		}else{
			return (T)createXls(content);
		}
	}
	
	private static HSSFWorkbook createXls(byte [] content) throws IOException{
		InputStream inputStream = new ByteArrayInputStream(content);
		HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
		return workbook;
	}

	private static XSSFWorkbook createXlsx(byte [] content) throws InvalidFormatException, IOException{
		InputStream inputStream = new ByteArrayInputStream(content);
		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
		return workbook;
	}

	public void addSheet(ExcelTemplate excelSheet, String sheetName){
		try{
			if(workbook == null){
				workbook = new XSSFWorkbook();
			}
			XSSFSheet sheet = null;
			sheet = workbook.createSheet(sheetName);
			int currentRow = 0;
			for(; currentRow < excelSheet.getOffsetRows(); ){
				sheet.createRow(currentRow++);
			}

			XSSFRow row = sheet.createRow(currentRow++);
			List<String> headers = excelSheet.getHeaders();
			CellStyle style = workbook.createCellStyle();
			style.setFillBackgroundColor(IndexedColors.BLUE.getIndex());
			style.setFillPattern(CellStyle.ALIGN_FILL);
			Font font = workbook.createFont();
			font.setColor(IndexedColors.WHITE.getIndex());
			style.setFont(font);
			for (int element = 0, skipPosition = 0; element < headers.size(); element++) {
				if(element == 0){
					for (; skipPosition < excelSheet.getOffsetColumns(); skipPosition++) {
						row.createCell(skipPosition);
					}
				}
				XSSFCell cell = row.createCell(skipPosition++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(headers.get(element));
				cell.setCellStyle(style);
			}

			List<List<Object>>  datalist = excelSheet.getDataList();
			for(List<Object> rowData : datalist){
				int skipPosition = 0;
				XSSFRow newRow = sheet.createRow(currentRow++);
				for (; skipPosition < excelSheet.getOffsetColumns(); skipPosition++) {
					newRow.createCell(skipPosition);
				}
				for (int element = 0; element < rowData.size(); element++) {
					XSSFCell cell = newRow.createCell(skipPosition++);
					if(rowData.get(element) instanceof String){
						cell.setCellType(XSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(rowData.get(element).toString());
					}else if(rowData.get(element) instanceof Boolean){
						cell.setCellType(XSSFCell.CELL_TYPE_BOOLEAN);
						boolean value = (Boolean)rowData.get(element);
						cell.setCellValue(value);
					}else if((rowData.get(element) instanceof Integer) || (rowData.get(element) instanceof Long) || (rowData.get(element) instanceof Double) || (rowData.get(element) instanceof Float)){
						cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
						Double value = null;
						if(rowData.get(element) instanceof Integer){
							value = Double.valueOf(((Integer)rowData.get(element)).toString());
						}
						if(rowData.get(element) instanceof Long){
							value = Double.valueOf(((Long)rowData.get(element)).toString());
						}
						if(rowData.get(element) instanceof Double){
							value = (Double)rowData.get(element);
						}
						if(rowData.get(element) instanceof Float){
							value = Double.valueOf(((Float)rowData.get(element)).toString());
						}
						cell.setCellValue(value);
					}
				}
			}
			numberSheet++;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void addSheetCumplimiento(ExcelTemplate excelSheet, String sheetName){
		try{
			if(workbook == null){
				workbook = new XSSFWorkbook();
			}
			XSSFSheet sheet = null;
			sheet = workbook.createSheet(sheetName);
			int currentRow = 0;
			for(; currentRow < excelSheet.getOffsetRows(); ){
				sheet.createRow(currentRow++);
			}

			XSSFRow row = sheet.createRow(currentRow++);
			List<String> headers = excelSheet.getHeaders();
			List<String> subHeaders = ((RebajaSheetExcel)excelSheet).getSubHeaders();
			
			CellStyle style = workbook.createCellStyle();
			style.setFillBackgroundColor(IndexedColors.BLUE.getIndex());
			style.setFillPattern(CellStyle.ALIGN_FILL);
			Font font = workbook.createFont();
			font.setColor(IndexedColors.WHITE.getIndex());
			style.setFont(font);
			
			
			List<Integer> merged = new ArrayList<Integer>();
			List<String> titulos = new ArrayList<String>();
			
			for (int i = 0; i < headers.size(); i++) {
				String[] datos = headers.get(i).split(",");
				merged.add(Integer.parseInt(datos[0]));
				titulos.add(datos[1]);
			}
			
			int pos=0;
			for (int i = 0; i < titulos.size(); i++) {
				int longitud = merged.get(i);
				for (int j = 0; j < longitud; j++) {
					XSSFCell cell = row.createCell(pos++);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					if(j==0){
						cell.setCellValue(titulos.get(i));
					}else{
						cell.setCellValue("");
					}
					cell.setCellStyle(style);
				}
			}
			
			int posicion=0;
			for (int i = 0; i < merged.size(); i++) {	
				if(i==0){
					sheet.addMergedRegion(new CellRangeAddress(0,0,posicion,merged.get(i)-1));
					posicion+=merged.get(i);
				}else{
					sheet.addMergedRegion(new CellRangeAddress(0,0,posicion,posicion+merged.get(i)-1));
					posicion+=merged.get(i);
				}
			}
			
						
			XSSFRow row2 = sheet.createRow(currentRow++);
			
			
			for (int element = 0, skipPosition = 0; element < subHeaders.size(); element++) {
				if(element == 0){
					for (; skipPosition < excelSheet.getOffsetColumns(); skipPosition++) {
						row.createCell(skipPosition);
					}
				}
				XSSFCell cell = row2.createCell(skipPosition++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(subHeaders.get(element));
				cell.setCellStyle(style);
				
			}

			List<List<Object>>  datalist = excelSheet.getDataList();
			for(List<Object> rowData : datalist){
				int skipPosition = 0;
				XSSFRow newRow = sheet.createRow(currentRow++);
				for (; skipPosition < excelSheet.getOffsetColumns(); skipPosition++) {
					newRow.createCell(skipPosition);
				}
				for (int element = 0; element < rowData.size(); element++) {
					XSSFCell cell = newRow.createCell(skipPosition++);
					if(rowData.get(element) instanceof String){
						cell.setCellType(XSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(rowData.get(element).toString());
					}else if(rowData.get(element) instanceof Boolean){
						cell.setCellType(XSSFCell.CELL_TYPE_BOOLEAN);
						boolean value = (Boolean)rowData.get(element);
						cell.setCellValue(value);
					}else if((rowData.get(element) instanceof Integer) || (rowData.get(element) instanceof Long) || (rowData.get(element) instanceof Double) || (rowData.get(element) instanceof Float)){
						cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
						Double value = null;
						if(rowData.get(element) instanceof Integer){
							value = Double.valueOf(((Integer)rowData.get(element)).toString());
						}
						if(rowData.get(element) instanceof Long){
							value = Double.valueOf(((Long)rowData.get(element)).toString());
						}
						if(rowData.get(element) instanceof Double){
							value = (Double)rowData.get(element);
						}
						if(rowData.get(element) instanceof Float){
							value = Double.valueOf(((Float)rowData.get(element)).toString());
						}
						cell.setCellValue(value);
					}
				}
			}
			numberSheet++;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void addSheetRebajaCalculada(ExcelTemplate excelSheet, String sheetName){
		try{
			if(workbook == null){
				workbook = new XSSFWorkbook();
			}
			XSSFSheet sheet = null;
			sheet = workbook.createSheet(sheetName);
			int currentRow = 0;
			for(; currentRow < excelSheet.getOffsetRows(); ){
				sheet.createRow(currentRow++);
			}

			XSSFRow row = sheet.createRow(currentRow++);
			List<String> headers = excelSheet.getHeaders();
			List<String> subHeaders = ((RebajaCalculadaSheetExcel)excelSheet).getSubHeaders();
			
			CellStyle style = workbook.createCellStyle();
			style.setFillBackgroundColor(IndexedColors.BLUE.getIndex());
			style.setFillPattern(CellStyle.ALIGN_FILL);
			Font font = workbook.createFont();
			font.setColor(IndexedColors.WHITE.getIndex());
			style.setFont(font);
			
			
			List<Integer> merged = new ArrayList<Integer>();
			List<String> titulos = new ArrayList<String>();
			
			for (int i = 0; i < headers.size(); i++) {
				String[] datos = headers.get(i).split(",");
				merged.add(Integer.parseInt(datos[0]));
				titulos.add(datos[1]);
			}
			
			int pos=0;
			for (int i = 0; i < titulos.size(); i++) {
				int longitud = merged.get(i);
				for (int j = 0; j < longitud; j++) {
					XSSFCell cell = row.createCell(pos++);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					if(j==0){
						cell.setCellValue(titulos.get(i));
					}else{
						cell.setCellValue("");
					}
					cell.setCellStyle(style);
				}
			}
			
			int posicion=0;
			for (int i = 0; i < merged.size(); i++) {	
				if(i==0){
					sheet.addMergedRegion(new CellRangeAddress(0,0,posicion,merged.get(i)-1));
					posicion+=merged.get(i);
				}else{
					sheet.addMergedRegion(new CellRangeAddress(0,0,posicion,posicion+merged.get(i)-1));
					posicion+=merged.get(i);
				}
			}
			
						
			XSSFRow row2 = sheet.createRow(currentRow++);
			
			
			for (int element = 0, skipPosition = 0; element < subHeaders.size(); element++) {
				if(element == 0){
					for (; skipPosition < excelSheet.getOffsetColumns(); skipPosition++) {
						row.createCell(skipPosition);
					}
				}
				XSSFCell cell = row2.createCell(skipPosition++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(subHeaders.get(element));
				cell.setCellStyle(style);
				
			}

			List<List<Object>>  datalist = excelSheet.getDataList();
			for(List<Object> rowData : datalist){
				int skipPosition = 0;
				XSSFRow newRow = sheet.createRow(currentRow++);
				for (; skipPosition < excelSheet.getOffsetColumns(); skipPosition++) {
					newRow.createCell(skipPosition);
				}
				for (int element = 0; element < rowData.size(); element++) {
					XSSFCell cell = newRow.createCell(skipPosition++);
					if(rowData.get(element) instanceof String){
						cell.setCellType(XSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(rowData.get(element).toString());
					}else if(rowData.get(element) instanceof Boolean){
						cell.setCellType(XSSFCell.CELL_TYPE_BOOLEAN);
						boolean value = (Boolean)rowData.get(element);
						cell.setCellValue(value);
					}else if((rowData.get(element) instanceof Integer) || (rowData.get(element) instanceof Long) || (rowData.get(element) instanceof Double) || (rowData.get(element) instanceof Float)){
						cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
						Double value = null;
						if(rowData.get(element) instanceof Integer){
							value = Double.valueOf(((Integer)rowData.get(element)).toString());
						}
						if(rowData.get(element) instanceof Long){
							value = Double.valueOf(((Long)rowData.get(element)).toString());
						}
						if(rowData.get(element) instanceof Double){
							value = (Double)rowData.get(element);
						}
						if(rowData.get(element) instanceof Float){
							value = Double.valueOf(((Float)rowData.get(element)).toString());
						}
						cell.setCellValue(value);
					}
				}
			}
			numberSheet++;
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public File saveExcel() throws Exception {
		if(workbook == null){
			throw new Exception("La planilla no contiene datos");
		}
		File file = new File(fileName); 
		FileOutputStream os = null; 
		try { 
			if (!file.getParentFile().exists()) {
			    file.getParentFile().mkdir();
			}
			os = new FileOutputStream(file);
			workbook.write(os);
		} catch (IOException e) { 
			e.printStackTrace();
		} catch (Exception e) { 
			e.printStackTrace();
		} finally { 
			try { 
				if (null != os) 
					os.close(); 
			} catch (Exception ex) { 
				ex.printStackTrace();
			} 
		} 
		return file;
	}

	public XSSFWorkbook getWorkbook() {
		return workbook;
	}

	public void setWorkbook(XSSFWorkbook workbook) {
		this.workbook = workbook;
	}

	public Integer getNumberSheet() {
		return numberSheet;
	}

	public void setNumberSheet(Integer numberSheet) {
		this.numberSheet = numberSheet;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
