package minsal.divap.excel;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import minsal.divap.excel.impl.EstimacionFlujoCajaSubtituloSheetExcel;
import minsal.divap.excel.impl.ProgramaAPSMunicipalesSheetExcel;
import minsal.divap.excel.impl.RebajaCalculadaSheetExcel;
import minsal.divap.excel.impl.RebajaSheetExcel;
import minsal.divap.excel.interfaces.ExcelTemplate;
import minsal.divap.vo.CajaMesVO;
import minsal.divap.vo.CellExcelVO;
import minsal.divap.vo.FlujoCajaVO;

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

			if(excelSheet instanceof ProgramaAPSMunicipalesSheetExcel){
				addSheet((ProgramaAPSMunicipalesSheetExcel)excelSheet, sheetName);
				return;
			}
			if(excelSheet instanceof EstimacionFlujoCajaSubtituloSheetExcel){
				addSheet((EstimacionFlujoCajaSubtituloSheetExcel)excelSheet, sheetName);
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
		if(header != null && header.size() > 0){
			for(CellExcelVO cellExcelVO : header){
				if(maxColSpan == 0){
					maxRowSpan = cellExcelVO.getRowSpan();
				}
				maxColSpan += cellExcelVO.getColSpan();
			}
			for(; currentRow < maxRowSpan;){
				XSSFRow row = sheet.createRow(currentRow++);
				for(int currentCol= 0; currentCol < maxColSpan;){
					XSSFCell cell = row.createCell(currentCol++);
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
					sheet.addMergedRegion(new CellRangeAddress(rowsReplace, cellExcelVO.getRowSpan(), colsReplace, cellExcelVO.getColSpan()));
				}else{
					XSSFRow row = sheet.getRow(appendRow++) ;
					XSSFCell cell = row.getCell(colsReplace);
					cell.setCellValue(cellExcelVO.getName());
					colsReplace += cellExcelVO.getColSpan();
					sheet.addMergedRegion(new CellRangeAddress(appendRow, cellExcelVO.getRowSpan(), colsReplace, cellExcelVO.getColSpan()));
				}
			}
		}
		/*if(subHeader != null && subHeader.size() > 0){
			int rows = 0;
			int cols = 0;
			for(CellExcelVO cellExcelVO : subHeader){
				if(cols == 0){
					rows = currentRow;
					rows += cellExcelVO.getRowSpan();
				}
				cols += cellExcelVO.getColSpan();
			}
			for(; currentRow < rows;){
				XSSFRow row = sheet.createRow(currentRow++);
				for(int currentCol= 0; currentCol < cols;){
					XSSFCell cell = row.createCell(currentCol++);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					cell.setCellStyle(style);
				}
			}
			for(CellExcelVO cellExcelVO : subHeader){
				XSSFRow row = sheet.getRow(rowsReplace) ;
				XSSFCell cell = row.getCell(colsReplace);
				cell.setCellValue(cellExcelVO.getName());
				colsReplace += cellExcelVO.getColSpan();
				sheet.addMergedRegion(new CellRangeAddress(rowsReplace, cellExcelVO.getRowSpan(), colsReplace, cellExcelVO.getColSpan()));
			}
		}*/
	}
	
	private void addSheet(EstimacionFlujoCajaSubtituloSheetExcel excelSheet, String sheetName){
		XSSFSheet sheet = workbook.createSheet(sheetName);
		List<CellExcelVO> header = excelSheet.getHeaderComplex();
		List<CellExcelVO> subHeader = excelSheet.getSubHeadeComplex();
		List<FlujoCajaVO> items = excelSheet.getItems();
		
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
			for(int i=0; i<items.size(); i++){
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
			cell_tot_servicio.setCellStyle(styleTotales);
			
			cell_tot_servicio = totales.getCell(0);				
			cell_tot_servicio.setCellValue("TOTAL");
			
			
			
			for(int i=1;i<14;i++){
				XSSFCell cell_tot_mes = totales.createCell(i);
				cell_tot_mes.setCellStyle(styleTotales);
				cell_tot_mes = totales.getCell(i);
				if(i==1){
					cell_tot_mes.setCellValue(totalenero);
				}
				else if(i==2){
					cell_tot_mes.setCellValue(totalfebrero);
				}else if(i==3){
					cell_tot_mes.setCellValue(totalmarzo);
				}else if(i==4){
					cell_tot_mes.setCellValue(totalabril);
				}else if(i==5){
					cell_tot_mes.setCellValue(totalmayo);
				}else if(i==6){
					cell_tot_mes.setCellValue(totaljunio);
				}else if(i==7){
					cell_tot_mes.setCellValue(totaljulio);
				}else if(i==8){
					cell_tot_mes.setCellValue(totalagosto);
				}else if(i==9){
					cell_tot_mes.setCellValue(totalseptiembre);
				}else if(i==10){
					cell_tot_mes.setCellValue(totaloctubre);
				}else if(i==11){
					cell_tot_mes.setCellValue(totalnoviembre);
				}else if(i==12){
					cell_tot_mes.setCellValue(totaldiciembre);
				}else if(i==13){
					cell_tot_mes.setCellValue(totaltotal);
				}				
				
			}
			
			
		}		
	}
	
	private void addSheetResumenFlujoCaja(EstimacionFlujoCajaSubtituloSheetExcel excelSheet, String sheetName){
		XSSFSheet sheet = workbook.createSheet(sheetName);
		List<CellExcelVO> header = excelSheet.getHeaderComplex();
		List<CellExcelVO> subHeader = excelSheet.getSubHeadeComplex();
		List<FlujoCajaVO> items = excelSheet.getItems();
		
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
			for(int i=0; i<items.size(); i++){
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
			cell_tot_total.setCellValue(totaltotal);
			
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
