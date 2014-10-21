package minsal.divap.excel;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import minsal.divap.excel.impl.EstimacionFlujoCajaConsolidadorSheetExcel;
import minsal.divap.excel.impl.EstimacionFlujoCajaSubtituloSheetExcel;
import minsal.divap.excel.impl.ProgramaAPSMunicipalesSheetExcel;
import minsal.divap.excel.impl.RebajaCalculadaSheetExcel;
import minsal.divap.excel.impl.RebajaSheetExcel;
import minsal.divap.excel.interfaces.ExcelTemplate;
import minsal.divap.vo.CajaMesVO;
import minsal.divap.vo.CajaMontoSummaryVO;
import minsal.divap.vo.CellExcelVO;
import minsal.divap.vo.FlujoCajaVO;
import minsal.divap.vo.ResumenConsolidadorVO;
import minsal.divap.vo.SubtituloFlujoCajaVO;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
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

			if(excelSheet instanceof ProgramaAPSMunicipalesSheetExcel){
				addSheet((ProgramaAPSMunicipalesSheetExcel)excelSheet, sheetName);
				return;
			}

			if(excelSheet instanceof RebajaSheetExcel){
				addSheet((RebajaSheetExcel)excelSheet, sheetName);
				return;
			}

			if(excelSheet instanceof RebajaCalculadaSheetExcel){
				addSheet((RebajaCalculadaSheetExcel)excelSheet, sheetName);
				return;
			}

			if(excelSheet instanceof EstimacionFlujoCajaSubtituloSheetExcel){
				addSheet((EstimacionFlujoCajaSubtituloSheetExcel)excelSheet, sheetName);
				return;
			}
			
			if(excelSheet instanceof EstimacionFlujoCajaConsolidadorSheetExcel){
				addSheet((EstimacionFlujoCajaConsolidadorSheetExcel)excelSheet, sheetName);
				return;
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

	private void addSheet(RebajaSheetExcel excelSheet, String sheetName){
		try{
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
				for( ;skipPosition < subHeaders.size(); skipPosition++){
					System.out.println("celda posicion skipPosition="+skipPosition);
					XSSFCell cell = newRow.createCell(skipPosition);
					CellStyle stylePercentage = workbook.createCellStyle();
					stylePercentage.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
					cell.setCellStyle(stylePercentage);
				}
			}
			numberSheet++;
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void addSheet(ProgramaAPSMunicipalesSheetExcel excelSheet, String sheetName){
		XSSFSheet sheet = workbook.createSheet(sheetName);
		List<CellExcelVO> header = excelSheet.getHeaderComplex();
		List<CellExcelVO> subHeader = excelSheet.getSubHeadeComplex();
		CellStyle style = workbook.createCellStyle();
		style.setFillBackgroundColor(IndexedColors.BLUE.getIndex());
		style.setFillPattern(CellStyle.ALIGN_FILL);
		Font font = workbook.createFont();
		font.setColor(IndexedColors.WHITE.getIndex());
		style.setFont(font);
		int currentRow = 0;
		int maxRowSpan = 0;
		int maxColSpan = 0;
		int addRow = 0;
		if(header != null && header.size() > 0){
			for(CellExcelVO cellExcelVO : header){
				if(cellExcelVO.getRowSpan() > maxRowSpan){
					maxRowSpan = cellExcelVO.getRowSpan();
				}
				if(cellExcelVO.getRowSpan() == maxRowSpan){
					maxColSpan += cellExcelVO.getColSpan();
				}else{
					addRow++;
					if(addRow == maxRowSpan){
						maxColSpan += cellExcelVO.getColSpan();
					}
				}
			}
			for( ; currentRow < maxRowSpan ; currentRow++){
				XSSFRow row = sheet.createRow(currentRow);
				for(int currentCol= 0; currentCol < maxColSpan; currentCol++){
					XSSFCell cell = row.createCell(currentCol);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					cell.setCellStyle(style);
				}
			}
			int rowsReplace = 0;
			int colsReplace = 0;
			int appendRow = 0;
			for(CellExcelVO cellExcelVO : header){
				if(maxRowSpan == cellExcelVO.getRowSpan()){
					appendRow = 0;
					XSSFRow row = sheet.getRow(rowsReplace) ;
					XSSFCell cell = row.getCell(colsReplace);
					cell.setCellValue(cellExcelVO.getName());
					colsReplace += cellExcelVO.getColSpan();
					sheet.addMergedRegion(new CellRangeAddress(rowsReplace, (cellExcelVO.getRowSpan() - 1), colsReplace - cellExcelVO.getColSpan(), colsReplace -1 ));
				}else{
					XSSFRow row = sheet.getRow(appendRow) ;
					XSSFCell cell = row.getCell(colsReplace);
					cell.setCellValue(cellExcelVO.getName());
					System.out.println("CellRangeAddress("+appendRow+","+appendRow+","+colsReplace+","+(colsReplace + cellExcelVO.getColSpan()-1)+")");
					sheet.addMergedRegion(new CellRangeAddress(appendRow, appendRow, colsReplace, (colsReplace + cellExcelVO.getColSpan()-1)));
					appendRow++;
				}
			}
		}
		if(subHeader != null && subHeader.size() > 0){
			int rows = 0;
			int cols = 0;
			addRow = 0;
			maxColSpan = 0;
			for(CellExcelVO cellExcelVO : subHeader){
				if(cellExcelVO.getRowSpan() > rows){
					rows = cellExcelVO.getRowSpan();
				}
				if(cellExcelVO.getColSpan() > cols){
					cols = cellExcelVO.getColSpan();
				}
				if(cellExcelVO.getRowSpan() == maxRowSpan){
					maxColSpan += cellExcelVO.getColSpan();
				}else{
					addRow++;
					if(addRow == maxRowSpan){
						maxColSpan += cellExcelVO.getColSpan();
					}
				}
			}
			maxRowSpan = maxRowSpan + rows;
			int rowsReplace = currentRow;
			int appendRow = currentRow;


			System.out.println("maxRowSpan="+maxRowSpan+"maxColSpan="+maxColSpan+" rowsReplace="+rowsReplace+" appendRow="+appendRow);

			for( ; currentRow < maxRowSpan ; currentRow++){
				XSSFRow row = sheet.createRow(currentRow);
				for(int currentCol= 0; currentCol <= maxColSpan; currentCol++){
					XSSFCell cell = row.createCell(currentCol);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					CellStyle cellStyle = workbook.createCellStyle();
					cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
					cell.setCellStyle(cellStyle);
				}
			}
			int colsReplace = 0;
			int appendCol = 0;
			int lastColSpan=0;
			for(CellExcelVO cellExcelVO : subHeader){
				System.out.println("rows="+rows+" cellExcelVO.getRowSpan()="+cellExcelVO.getRowSpan()+" cellExcelVO.getColSpan()="+cellExcelVO.getColSpan()+" cellExcelVO.getName()="+cellExcelVO.getName());
				if(rows == cellExcelVO.getRowSpan()){
					System.out.println("if true");
					appendCol = 0;
					appendRow = rowsReplace;
					XSSFRow row = sheet.getRow(rowsReplace) ;
					XSSFCell cell = row.getCell(colsReplace);
					cell.setCellValue(cellExcelVO.getName());
					colsReplace += cellExcelVO.getColSpan();
					lastColSpan=cellExcelVO.getColSpan();
					sheet.addMergedRegion(new CellRangeAddress(rowsReplace, ((rowsReplace + cellExcelVO.getRowSpan()) - 1), colsReplace - cellExcelVO.getColSpan(), colsReplace -1 ));
				}else{
					System.out.println("if false");
					System.out.println("appendRow="+appendRow);
					System.out.println("colsReplace="+colsReplace);

					if(cols == cellExcelVO.getColSpan()){
						System.out.println("if maxColSpan == cellExcelVO.getColSpan()");
						XSSFRow row = sheet.getRow(appendRow) ;
						XSSFCell cell = row.getCell(colsReplace);
						cell.setCellValue(cellExcelVO.getName());
						System.out.println("CellRangeAddress("+appendRow+","+appendRow+","+colsReplace+","+(colsReplace + cellExcelVO.getColSpan()-1)+")");
						sheet.addMergedRegion(new CellRangeAddress(appendRow, appendRow, colsReplace, (colsReplace + cellExcelVO.getColSpan() -1)));
						if(appendCol == 0){
							appendCol = colsReplace; 
						}
						colsReplace += cellExcelVO.getColSpan();
						appendRow++;
					}else{
						System.out.println("else maxColSpan == cellExcelVO.getColSpan()");
						System.out.println("appendCol ="+appendCol);
						XSSFRow row = sheet.getRow(appendRow) ;
						XSSFCell cell = row.getCell(appendCol);
						cell.setCellValue(cellExcelVO.getName());
						System.out.println("CellRangeAddress("+appendRow+","+appendRow+","+appendCol+","+appendCol+")");
						sheet.addMergedRegion(new CellRangeAddress(appendRow, appendRow, appendCol, appendCol));
						appendCol += cellExcelVO.getColSpan();
					}
				}
			}
			for(int columnPosition = 0; columnPosition <= maxColSpan; columnPosition++) {
				sheet.autoSizeColumn((short) (columnPosition));
			}
		}
	}

	private void addSheet(EstimacionFlujoCajaSubtituloSheetExcel excelSheet, String sheetName){
		XSSFSheet sheet = null;
		Boolean hojaNueva = null;
		int index = workbook.getSheetIndex(sheetName);
		if(index == -1){
			hojaNueva = true;
			sheet = workbook.createSheet(sheetName);
		} else {
			hojaNueva = false;
			sheet = workbook.getSheetAt(index);
		}

		List<CellExcelVO> header = excelSheet.getHeaderComplex();
		List<CellExcelVO> subHeader = excelSheet.getSubHeadeComplex();
		CellStyle style = workbook.createCellStyle();
		CellStyle styleTotales = workbook.createCellStyle();
		styleTotales.setFillPattern(CellStyle.ALIGN_FILL);
		styleTotales.setBorderBottom(CellStyle.BORDER_MEDIUM);
		styleTotales.setBorderLeft(CellStyle.BORDER_MEDIUM);
		styleTotales.setBorderRight(CellStyle.BORDER_MEDIUM);
		styleTotales.setBorderTop(CellStyle.BORDER_MEDIUM);
		styleTotales.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
		Font fontTotales = workbook.createFont();
		fontTotales.setColor(IndexedColors.BLACK.getIndex());

		style.setFillPattern(CellStyle.ALIGN_FILL);
		style.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		style.setBorderBottom(CellStyle.BORDER_MEDIUM);
		style.setBorderLeft(CellStyle.BORDER_MEDIUM);
		style.setBorderRight(CellStyle.BORDER_MEDIUM);
		style.setBorderTop(CellStyle.BORDER_MEDIUM);
		Font font = workbook.createFont();
		font.setColor(IndexedColors.WHITE.getIndex());
		style.setFont(font);
		styleTotales.setFont(fontTotales);

		int currentRow = 0;
		int currentCol = ((hojaNueva)?0:14);
		int maxRowSpan = 0;
		int maxColSpan = 0;

		XSSFFont fontHeader = workbook.createFont();
		fontHeader.setColor(IndexedColors.BLACK.getIndex());
		fontHeader.setBold(true);
		CellStyle cellStyleHeader = workbook.createCellStyle();
		cellStyleHeader.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		cellStyleHeader.setFont(fontHeader);

		if(header != null && header.size() > 0){
			for(CellExcelVO cellExcelVO : header){
				if(cellExcelVO.getRowSpan() > maxRowSpan){
					maxRowSpan = cellExcelVO.getRowSpan();
				}
				maxColSpan += cellExcelVO.getColSpan();
			}
			System.out.println("maxColSpan->"+maxColSpan);
			System.out.println("maxRowSpan->"+maxRowSpan);
			for(int fila = 0; fila < maxRowSpan; fila++){
				XSSFRow row = null;
				if(hojaNueva){
					row = sheet.createRow(fila);
				}else{
					row = sheet.getRow(fila);
				}
				for(int columna = currentCol; columna < (maxColSpan+currentCol); columna++){
					XSSFCell cell = row.createCell(columna);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					cell.setCellStyle(cellStyleHeader);
				}
			}
			boolean first = true;
			for(CellExcelVO cellExcelVO : header){	
				if(!hojaNueva && first){
					first = false;
					continue;
				}
				XSSFRow row = sheet.getRow(currentRow) ;
				XSSFCell cell = row.getCell(currentCol);

				cell.setCellStyle(cellStyleHeader);
				System.out.println("cellExcelVO.getName()="+cellExcelVO.getName());
				cell.setCellValue(cellExcelVO.getName());
				sheet.addMergedRegion(new CellRangeAddress(currentRow, (cellExcelVO.getRowSpan()==1) ? currentRow : (cellExcelVO.getRowSpan()-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: (currentCol + cellExcelVO.getColSpan() - 1)));
				if(cellExcelVO.getColSpan()==1){
					currentCol +=1;
				}else{
					currentCol += cellExcelVO.getColSpan();
				}
			}
			currentRow++;	
		}	
		currentCol = ((hojaNueva)?1:14);
		for(CellExcelVO cellExcelVO : subHeader){
			XSSFRow row = sheet.getRow(currentRow);
			XSSFCell cell = row.getCell(currentCol++);
			cell.setCellValue(cellExcelVO.getName());
		}
		currentRow++;
		currentCol = ((hojaNueva)?0:14);
		int inicialCol = currentCol;
		List<SubtituloFlujoCajaVO> items = excelSheet.getItems();
		System.out.println("items.size()=" + ((items == null)? 0 : items.size()));
		if(items != null){
			int lastRow = items.size();
			int contElementos = 1;
			CellStyle cellStyleLong = workbook.createCellStyle();
			cellStyleLong.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			cellStyleHeader.setDataFormat(workbook.createDataFormat().getFormat("#,##0")); 
			for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : items){
				if(contElementos != lastRow){
					XSSFRow row = null;
					XSSFCell cell = null;
					if(hojaNueva){
						row = sheet.createRow(currentRow++);
						cell = row.createCell(currentCol++);
						cell.setCellValue(subtituloFlujoCajaVO.getServicio());
					}else{
						row = sheet.getRow(currentRow++);
					}
					for(CajaMontoSummaryVO cajaMontoSummaryVO : subtituloFlujoCajaVO.getCajaMontos()){
						cell = row.createCell(currentCol++);
						cell.setCellStyle(cellStyleLong);
						cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
						cell.setCellValue(cajaMontoSummaryVO.getMontoMes());
					}
					cell = row.createCell(currentCol);
					cell.setCellStyle(cellStyleHeader);
					cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(subtituloFlujoCajaVO.getTotalMontos());
					currentCol = ((hojaNueva)?0:14);
				} else {
					XSSFRow row = null;
					XSSFCell cell = null;
					if(hojaNueva){
						row = sheet.createRow(currentRow++);
						cell = row.createCell(currentCol++);
						cell.setCellStyle(cellStyleHeader);
						cell.setCellValue(subtituloFlujoCajaVO.getServicio());
					}else{
						row = sheet.getRow(currentRow++);
					}
					for(CajaMontoSummaryVO cajaMontoSummaryVO : subtituloFlujoCajaVO.getCajaMontos()){
						cell = row.createCell(currentCol++);
						cell.setCellStyle(cellStyleHeader);
						cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
						cell.setCellValue(cajaMontoSummaryVO.getMontoMes());
					}
					cell = row.createCell(currentCol);
					cell.setCellStyle(cellStyleHeader);
					cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(subtituloFlujoCajaVO.getTotalMontos());
					System.out.println("inicialCol-->"+inicialCol+"  (currentCol+inicialCol)-->"+ (currentCol+inicialCol));
					for(int columnPosition = inicialCol; columnPosition <= (currentCol+inicialCol); columnPosition++) {
						sheet.autoSizeColumn((short) (columnPosition));
					}
				}
				contElementos++;
			}
		}
	}

	
	
	
	private void addSheet(EstimacionFlujoCajaConsolidadorSheetExcel excelSheet, String sheetName){
		XSSFSheet sheet = null;
		Boolean hojaNueva = null;
		int index = workbook.getSheetIndex(sheetName);
		if(index == -1){
			hojaNueva = true;
			sheet = workbook.createSheet(sheetName);
		} else {
			hojaNueva = false;
			sheet = workbook.getSheetAt(index);
		}

		List<CellExcelVO> header = excelSheet.getHeaderComplex();
		List<CellExcelVO> subHeader = excelSheet.getSubHeadeComplex();
		CellStyle style = workbook.createCellStyle();
		CellStyle styleTotales = workbook.createCellStyle();
		styleTotales.setFillPattern(CellStyle.ALIGN_FILL);
		styleTotales.setBorderBottom(CellStyle.BORDER_MEDIUM);
		styleTotales.setBorderLeft(CellStyle.BORDER_MEDIUM);
		styleTotales.setBorderRight(CellStyle.BORDER_MEDIUM);
		styleTotales.setBorderTop(CellStyle.BORDER_MEDIUM);
		styleTotales.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
		Font fontTotales = workbook.createFont();
		fontTotales.setColor(IndexedColors.BLACK.getIndex());

		style.setFillPattern(CellStyle.ALIGN_FILL);
		style.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		style.setBorderBottom(CellStyle.BORDER_MEDIUM);
		style.setBorderLeft(CellStyle.BORDER_MEDIUM);
		style.setBorderRight(CellStyle.BORDER_MEDIUM);
		style.setBorderTop(CellStyle.BORDER_MEDIUM);
		Font font = workbook.createFont();
		font.setColor(IndexedColors.WHITE.getIndex());
		style.setFont(font);
		styleTotales.setFont(fontTotales);

		int currentRow = 0;
		int currentCol = ((hojaNueva)?0:14);
		int maxRowSpan = 0;
		int maxColSpan = 0;

		XSSFFont fontHeader = workbook.createFont();
		fontHeader.setColor(IndexedColors.BLACK.getIndex());
		fontHeader.setBold(true);
		CellStyle cellStyleHeader = workbook.createCellStyle();
		cellStyleHeader.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		cellStyleHeader.setFont(fontHeader);

		if(header != null && header.size() > 0){
			for(CellExcelVO cellExcelVO : header){
				if(cellExcelVO.getRowSpan() > maxRowSpan){
					maxRowSpan = cellExcelVO.getRowSpan();
				}
				maxColSpan += cellExcelVO.getColSpan();
			}
			System.out.println("maxColSpan->"+maxColSpan);
			System.out.println("maxRowSpan->"+maxRowSpan);
			for(int fila = 0; fila < maxRowSpan; fila++){
				XSSFRow row = null;
				if(hojaNueva){
					row = sheet.createRow(fila);
				}else{
					row = sheet.getRow(fila);
				}
				for(int columna = currentCol; columna < (maxColSpan+currentCol); columna++){
					XSSFCell cell = row.createCell(columna);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					cell.setCellStyle(cellStyleHeader);
				}
			}
			boolean first = true;
			for(CellExcelVO cellExcelVO : header){	
				if(!hojaNueva && first){
					first = false;
					continue;
				}
				XSSFRow row = sheet.getRow(currentRow) ;
				XSSFCell cell = row.getCell(currentCol);

				cell.setCellStyle(cellStyleHeader);
				cell.setCellValue(cellExcelVO.getName());
				sheet.addMergedRegion(new CellRangeAddress(currentRow, (cellExcelVO.getRowSpan()==1) ? currentRow : (cellExcelVO.getRowSpan()-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: (currentCol + cellExcelVO.getColSpan() - 1)));
				if(cellExcelVO.getColSpan()==1){
					currentCol +=1;
				}else{
					currentCol += cellExcelVO.getColSpan();
				}
			}
			currentRow++;	
		}	
		currentCol = ((hojaNueva)?1:14);
		for(CellExcelVO cellExcelVO : subHeader){
			XSSFRow row = sheet.getRow(currentRow);
			XSSFCell cell = row.getCell(currentCol++);
			cell.setCellValue(cellExcelVO.getName());
			
		}
		
		for(int columnPosition = 0; columnPosition <= maxColSpan; columnPosition++) {
			sheet.autoSizeColumn((short) (columnPosition));
		}
		currentRow=3;
		currentCol = ((hojaNueva)?0:14);
		int inicialCol = currentCol;
		List<ResumenConsolidadorVO> items = excelSheet.getItems();
		System.out.println("items.size()=" + ((items == null)? 0 : items.size()));
		/*if(items != null){
			int lastRow = items.size();
			int contElementos = 1;
			CellStyle cellStyleLong = workbook.createCellStyle();
			cellStyleLong.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			cellStyleHeader.setDataFormat(workbook.createDataFormat().getFormat("#,##0")); 
			for(ResumenConsolidadorVO resumenConsolidadorVO : items){
				if(contElementos != lastRow){
					XSSFRow row = null;
					XSSFCell cell = null;
					if(hojaNueva){
						row = sheet.createRow(currentRow++);
						cell = row.createCell(currentCol++);
						cell.setCellValue(resumenConsolidadorVO.getCodigoServicio());
						row = sheet.createRow(currentRow++);
						cell = row.createCell(currentCol++);
						cell.setCellValue(resumenConsolidadorVO.getServicio());
					}else{
						row = sheet.getRow(currentRow++);
					}
					for(Long monto : resumenConsolidadorVO.getMontos()){
						cell = row.createCell(currentCol++);
						cell.setCellStyle(cellStyleLong);
						cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
						cell.setCellValue(monto);
					}
					cell = row.createCell(currentCol);
					cell.setCellStyle(cellStyleHeader);
					cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(resumenConsolidadorVO.getTotal());
					currentCol = ((hojaNueva)?0:14);
				} else {
					
					
				}
				contElementos++;
			}
		}*/

	}

	
	

	private void addSheetResumenFlujoCaja(EstimacionFlujoCajaSubtituloSheetExcel excelSheet, String sheetName){
		XSSFSheet sheet = workbook.createSheet(sheetName);
		List<CellExcelVO> header = excelSheet.getHeaderComplex();
		List<CellExcelVO> subHeader = excelSheet.getSubHeadeComplex();
		List<SubtituloFlujoCajaVO> items = excelSheet.getItems();

		CellStyle style = workbook.createCellStyle();
		style.setFillPattern(CellStyle.ALIGN_FILL);
		style.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		style.setBorderBottom(CellStyle.BORDER_MEDIUM);
		style.setBorderLeft(CellStyle.BORDER_MEDIUM);
		style.setBorderRight(CellStyle.BORDER_MEDIUM);
		style.setBorderTop(CellStyle.BORDER_MEDIUM);
		Font font = workbook.createFont();
		font.setColor(IndexedColors.WHITE.getIndex());
		style.setFont(font);
		int currentRow = 0;
		int maxRowSpan = 0;
		int maxColSpan = 0;

		if(header != null && header.size() > 0){
			for(CellExcelVO cellExcelVO : header){
				if(maxColSpan == 0){
					maxRowSpan = cellExcelVO.getRowSpan();
				}
				maxColSpan += cellExcelVO.getColSpan();
			}
			for(int i= currentRow; i < maxRowSpan; i++){
				XSSFRow row = sheet.createRow(i);			
				for(int currentCol= 0; currentCol < maxColSpan;){
					XSSFCell cell = row.createCell(currentCol);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					cell.setCellStyle(style);
					currentCol++;
				}
			}
			int rowsReplace = 0; //donde parte la fila que vas a mezclar
			int colsReplace = 0; //donde parte la culumna que vas a mezclar
			int appendRow = 0;
			int contHeader = 0;

			int fila_actual=0;

			for(CellExcelVO cellExcelVO : header){				
				if(header.get(contHeader).getName().equalsIgnoreCase("SERVICIOS DE SALUD")){
					XSSFRow row = sheet.getRow(0) ;
					XSSFCell cell = row.getCell(0);
					cell.setCellValue(cellExcelVO.getName());					
					sheet.addMergedRegion(new CellRangeAddress(0, 1, 0 , 0));
					sheet.autoSizeColumn(0);
				}					

				else if(!header.get(contHeader).getName().startsWith("TOTAL")){
					XSSFRow row = sheet.getRow(0);
					XSSFCell cell = row.getCell(1);
					cell.setCellValue(cellExcelVO.getName());
					sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 12));
				}
				else{
					XSSFRow row = sheet.getRow(0);
					XSSFCell cell = row.getCell(13);
					cell.setCellValue(cellExcelVO.getName());
					sheet.addMergedRegion(new CellRangeAddress(0, 1, 13 , 13));
					sheet.autoSizeColumn(13);
					appendRow++;
				}
				contHeader++;
			}


			int posSubHeader = 0;
			for(CellExcelVO cellExcelVO : subHeader){
				XSSFRow row = sheet.getRow(1);
				XSSFCell cell = row.getCell(posSubHeader+1);
				cell.setCellValue(cellExcelVO.getName());
				posSubHeader++;
			}

			long totalenero=0;
			long totalfebrero=0;
			long totalmarzo=0;
			long totalabril=0;
			long totalmayo=0;
			long totaljunio=0;
			long totaljulio=0;
			long totalagosto=0;
			long totalseptiembre=0;
			long totaloctubre=0;
			long totalnoviembre=0;
			long totaldiciembre=0;

			//Agragando items
			/*for(int i=0; i<items.size(); i++){
				fila_actual =i+2;
				XSSFRow row = sheet.createRow(fila_actual);
				row = sheet.getRow(fila_actual);
				XSSFCell cell_servicio = row.createCell(0);
				cell_servicio = row.getCell(0);				
				cell_servicio.setCellValue(items.get(i).getServicio());				

				List<CajaMesVO> cajames = items.get(i).getSubtitulo();
				int col_actual =1;
				long total_servicio = 0;
				for(int caja=0; caja<cajames.size(); caja++){
					XSSFCell cell = row.createCell(col_actual);
					cell = row.getCell(col_actual);
					cell.setCellValue(cajames.get(caja).getMonto());
					total_servicio+=cajames.get(caja).getMonto();
					col_actual++;
				}
				XSSFCell tot_service = row.createCell(13);
				tot_service = row.getCell(13);
				tot_service.setCellValue(total_servicio);

				fila_actual++;
			}

			//Agregando los totales de cada mes
			XSSFRow totales = sheet.createRow(fila_actual);
			totales = sheet.getRow(fila_actual);
			for(int i=0; i<items.size();i++){
				totalenero+=items.get(i).getSubtitulo().get(0).getMonto();
				totalfebrero+=items.get(i).getSubtitulo().get(1).getMonto();
				totalmarzo+=items.get(i).getSubtitulo().get(2).getMonto();
				totalabril+=items.get(i).getSubtitulo().get(3).getMonto();
				totalmayo+=items.get(i).getSubtitulo().get(4).getMonto();
				totaljunio+=items.get(i).getSubtitulo().get(5).getMonto();
				totaljulio+=items.get(i).getSubtitulo().get(6).getMonto();
				totalagosto+=items.get(i).getSubtitulo().get(7).getMonto();
				totalseptiembre+=items.get(i).getSubtitulo().get(8).getMonto();
				totaloctubre+=items.get(i).getSubtitulo().get(9).getMonto();
				totalnoviembre+=items.get(i).getSubtitulo().get(10).getMonto();
				totaldiciembre+=items.get(i).getSubtitulo().get(11).getMonto();
			}
			long totaltotal=totalenero+totalfebrero+totalmarzo+totalabril+totalmayo+totaljunio+totaljulio+totalagosto+totalseptiembre+totaloctubre+totalnoviembre+totaldiciembre;

			XSSFCell cell_tot_servicio = totales.createCell(0);
			cell_tot_servicio = totales.getCell(0);				
			cell_tot_servicio.setCellValue("TOTAL");

			XSSFCell cell_tot_enero = totales.createCell(1);
			cell_tot_enero = totales.getCell(1);				
			cell_tot_enero.setCellValue(totalenero);

			XSSFCell cell_tot_febrero = totales.createCell(2);
			cell_tot_febrero = totales.getCell(2);				
			cell_tot_febrero.setCellValue(totalfebrero);

			XSSFCell cell_tot_marzo = totales.createCell(3);
			cell_tot_marzo = totales.getCell(3);				
			cell_tot_marzo.setCellValue(totalmarzo);

			XSSFCell cell_tot_abril = totales.createCell(4);
			cell_tot_abril = totales.getCell(4);				
			cell_tot_abril.setCellValue(totalabril);

			XSSFCell cell_tot_mayo = totales.createCell(5);
			cell_tot_mayo = totales.getCell(5);				
			cell_tot_mayo.setCellValue(totalmayo);

			XSSFCell cell_tot_junio = totales.createCell(6);
			cell_tot_junio = totales.getCell(6);				
			cell_tot_junio.setCellValue(totaljunio);

			XSSFCell cell_tot_julio = totales.createCell(7);
			cell_tot_julio = totales.getCell(7);				
			cell_tot_julio.setCellValue(totaljulio);

			XSSFCell cell_tot_agosto = totales.createCell(8);
			cell_tot_agosto = totales.getCell(8);				
			cell_tot_agosto.setCellValue(totalagosto);

			XSSFCell cell_tot_septiembre = totales.createCell(9);
			cell_tot_septiembre = totales.getCell(9);				
			cell_tot_septiembre.setCellValue(totalseptiembre);

			XSSFCell cell_tot_octubre = totales.createCell(10);
			cell_tot_octubre = totales.getCell(10);				
			cell_tot_octubre.setCellValue(totaloctubre);

			XSSFCell cell_tot_noviembre = totales.createCell(11);
			cell_tot_noviembre = totales.getCell(11);				
			cell_tot_noviembre.setCellValue(totalnoviembre);

			XSSFCell cell_tot_diciembre = totales.createCell(12);
			cell_tot_diciembre = totales.getCell(12);				
			cell_tot_diciembre.setCellValue(totaldiciembre);

			XSSFCell cell_tot_total = totales.createCell(13);
			cell_tot_total = totales.getCell(13);				
			cell_tot_total.setCellValue(totaltotal);*/

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

	private void addSheet(RebajaCalculadaSheetExcel excelSheet, String sheetName){
		try{
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
			int totalColumn = 0;
			for(List<Object> rowData : datalist){
				int skipPosition = 0;
				XSSFRow newRow = sheet.createRow(currentRow++);
				for (; skipPosition < excelSheet.getOffsetColumns(); skipPosition++) {
					newRow.createCell(skipPosition);
				}
				for (int element = 0; element < rowData.size(); element++) {
					XSSFCell cell = newRow.createCell(skipPosition++);
					if(rowData.get(element) instanceof String){
						String data = rowData.get(element).toString(); 
						if(data.endsWith("%")){
							CellStyle stylePercentage = workbook.createCellStyle();
							stylePercentage.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
							stylePercentage.setAlignment(XSSFCellStyle.ALIGN_CENTER);
							cell.setCellStyle(stylePercentage);
							cell.setCellValue(data);
						}else{
							cell.setCellType(XSSFCell.CELL_TYPE_STRING);
							cell.setCellValue(data);
						}
					}else if(rowData.get(element) instanceof Boolean){
						cell.setCellType(XSSFCell.CELL_TYPE_BOOLEAN);
						boolean value = (Boolean)rowData.get(element);
						cell.setCellValue(value);
					}else if((rowData.get(element) instanceof Integer) || (rowData.get(element) instanceof Long) || (rowData.get(element) instanceof Double) || (rowData.get(element) instanceof Float)){
						cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
						Double value = null;
						if(rowData.get(element) instanceof Integer){
							value = Double.valueOf(((Integer)rowData.get(element)).toString());
							if(element > 3){
								CellStyle styleInteger = workbook.createCellStyle();
								styleInteger.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
								cell.setCellStyle(styleInteger);
							}
						}
						if(rowData.get(element) instanceof Long){
							CellStyle styleInteger = workbook.createCellStyle();
							styleInteger.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
							cell.setCellStyle(styleInteger);
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
				if(totalColumn == 0){
					totalColumn = skipPosition;
				}
			}
			for(int columnPosition = 0; columnPosition <  totalColumn; columnPosition++) {
				sheet.autoSizeColumn((short) (columnPosition));
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
