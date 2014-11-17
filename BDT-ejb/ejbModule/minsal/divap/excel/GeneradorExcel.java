package minsal.divap.excel;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import minsal.divap.excel.impl.CrearPlanillaCumplimientoMunicialProgramaSheetExcel;
import minsal.divap.excel.impl.EstimacionFlujoCajaConsolidadorSheetExcel;
import minsal.divap.excel.impl.EstimacionFlujoCajaSubtituloSheetExcel;
import minsal.divap.excel.impl.PlanillaTrabajoCumplimientoReliquidacionMunicipalSheetExcel;
import minsal.divap.excel.impl.ProgramaAPSDetallesMunicipalesHistoricosSheetExcel;
import minsal.divap.excel.impl.ProgramaAPSDetallesServiciosHistoricosSheetExcel;
import minsal.divap.excel.impl.ProgramaAPSLeySheetExcel;
import minsal.divap.excel.impl.ProgramaAPSMunicipalesDetallesSheetExcel;
import minsal.divap.excel.impl.ProgramaAPSMunicipalesHistoricosSheetExcel;
import minsal.divap.excel.impl.ProgramaAPSMunicipalesSheetExcel;
import minsal.divap.excel.impl.ProgramaAPSMunicipalesTemplateSheetExcel;
import minsal.divap.excel.impl.ProgramaAPSResolucionSheetExcel;
import minsal.divap.excel.impl.ProgramaAPSServicioResumenSheetExcel;
import minsal.divap.excel.impl.ProgramaAPSServicioSheetExcel;
import minsal.divap.excel.impl.ProgramaAPSServiciosDetallesSheetExcel;
import minsal.divap.excel.impl.ProgramaAPSServiciosHistoricosSheetExcel;
import minsal.divap.excel.impl.RebajaCalculadaSheetExcel;
import minsal.divap.excel.impl.RebajaSheetExcel;
import minsal.divap.excel.interfaces.ExcelTemplate;
import minsal.divap.vo.CajaMontoSummaryVO;
import minsal.divap.vo.CellExcelVO;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ProgramaAPSServicioResumenVO;
import minsal.divap.vo.ProgramaAPSServicioVO;
import minsal.divap.vo.ProgramaAPSVO;
import minsal.divap.vo.CumplimientoApsMunicipalProgramaVO;
import minsal.divap.vo.ResumenConsolidadorVO;
import minsal.divap.vo.ResumenProgramaMixtoVO;
import minsal.divap.vo.SubtituloFlujoCajaVO;
import minsal.divap.vo.ValorizarReliquidacionSummaryVO;
import minsal.divap.vo.SubtituloVO;

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

			if(excelSheet instanceof ProgramaAPSServicioSheetExcel){
=======
    private XSSFWorkbook workbook = null;
    private String fileName;
    private Integer numberSheet;
    

    public GeneradorExcel(String fileName){
        numberSheet = 0;
        this.fileName = fileName;
    }

    public GeneradorExcel(XSSFWorkbook workbook, String fileName){
    	this.workbook=workbook;
    	this.fileName=fileName;
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

    public static XSSFWorkbook createXlsx(byte [] content) throws InvalidFormatException, IOException{
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
            if(excelSheet instanceof ProgramaAPSMunicipalesTemplateSheetExcel){
                addSheet((ProgramaAPSMunicipalesTemplateSheetExcel)excelSheet, sheetName);
                return;
            }
            if(excelSheet instanceof ProgramaAPSMunicipalesHistoricosSheetExcel){
                addSheet((ProgramaAPSMunicipalesHistoricosSheetExcel)excelSheet, sheetName);
                return;
            }
            if(excelSheet instanceof ProgramaAPSDetallesMunicipalesHistoricosSheetExcel){
                addSheet((ProgramaAPSDetallesMunicipalesHistoricosSheetExcel)excelSheet, sheetName);
                return;
            }
            if(excelSheet instanceof ProgramaAPSMunicipalesDetallesSheetExcel){
                addSheet((ProgramaAPSMunicipalesDetallesSheetExcel)excelSheet, sheetName);
                return;
            }
            
            if(excelSheet instanceof ProgramaAPSServiciosDetallesSheetExcel){
                addSheet((ProgramaAPSServiciosDetallesSheetExcel)excelSheet, sheetName);
                return;
            }
            
            if(excelSheet instanceof ProgramaAPSResolucionSheetExcel){
                addSheet((ProgramaAPSResolucionSheetExcel)excelSheet, sheetName);
                return;
            }
            if(excelSheet instanceof ProgramaAPSLeySheetExcel){
                addSheet((ProgramaAPSLeySheetExcel)excelSheet, sheetName);
                return;
            }
            if(excelSheet instanceof ProgramaAPSServicioResumenSheetExcel){
                addSheet((ProgramaAPSServicioResumenSheetExcel)excelSheet, sheetName);
                return;
            }
            if(excelSheet instanceof ProgramaAPSServiciosHistoricosSheetExcel){
                addSheet((ProgramaAPSServiciosHistoricosSheetExcel)excelSheet, sheetName);
                return;
            }
            if(excelSheet instanceof ProgramaAPSDetallesServiciosHistoricosSheetExcel){
                addSheet((ProgramaAPSDetallesServiciosHistoricosSheetExcel)excelSheet, sheetName);
                return;
            }
            
            if(excelSheet instanceof ProgramaAPSServicioSheetExcel){
>>>>>>> programasReforzamiento
				addSheet((ProgramaAPSServicioSheetExcel)excelSheet, sheetName);
				return;
			}

<<<<<<< HEAD
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

			if(excelSheet instanceof CrearPlanillaCumplimientoMunicialProgramaSheetExcel){
				addSheet((CrearPlanillaCumplimientoMunicialProgramaSheetExcel)excelSheet, sheetName);
				return;
			}

			if(excelSheet instanceof PlanillaTrabajoCumplimientoReliquidacionMunicipalSheetExcel){
				addSheet((PlanillaTrabajoCumplimientoReliquidacionMunicipalSheetExcel)excelSheet, sheetName);
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
		List<ProgramaAPSVO> items = excelSheet.getItems();
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
			int contador_cols = 0;
			for(CellExcelVO cellExcelVO : header){
				if(cellExcelVO.getRowSpan() > maxRowSpan){
					maxRowSpan = cellExcelVO.getRowSpan();
				}
				if(cellExcelVO.getRowSpan() == maxRowSpan){
					maxColSpan += cellExcelVO.getColSpan();
				}else{
					addRow++;
					if(cellExcelVO.getColSpan() > contador_cols){
=======
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

    
    private void addSheet(ProgramaAPSResolucionSheetExcel excelSheet, String sheetName){
    	try{
	    	XSSFSheet sheet = null;
	        sheet = workbook.createSheet(sheetName);
	        int currentRow = 0;
	        for(; currentRow < excelSheet.getOffsetRows(); ){
	            sheet.createRow(currentRow++);
	        }
	        currentRow = 0;
	        XSSFRow row = sheet.createRow(currentRow++);
	        List<String> headers = excelSheet.getHeaders();
	        
	        CellStyle style = workbook.createCellStyle();
	        style.setFillBackgroundColor(IndexedColors.BLUE.getIndex());
	        style.setFillPattern(CellStyle.ALIGN_FILL);
	        Font font = workbook.createFont();
	        font.setColor(IndexedColors.WHITE.getIndex());
	        style.setFont(font);

	        
	        //creando el header
	        int pos=0;
	        for (int i = 0; i < headers.size(); i++) {
	            XSSFCell cell = row.createCell(pos++);
	            cell.setCellType(XSSFCell.CELL_TYPE_STRING);
	            cell.setCellValue(headers.get(i));
	            cell.setCellStyle(style);
	        }
	        
	       	List<ResumenProgramaMixtoVO> items = excelSheet.getItems();
	       	CellStyle cellStyleLong = workbook.createCellStyle();
            cellStyleLong.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			Long totalS24 = 0l;
			Long totalS21 = 0l;
			Long totalS22 = 0l;
			Long totalS29 = 0l;
			Long totalServicio = 0l;
			boolean s24=false;
			boolean s21=false;
			boolean s22=false;
			boolean s29=false;
			
			if(items.size()>0){
				if(items.get(0).getTotalS24()!=null && items.get(0).getTotalS24()>0){
					s24=true;
				}
				if(items.get(0).getTotalS21()!=null && items.get(0).getTotalS21()>0){
					s21=true;
				}
				if(items.get(0).getTotalS22()!=null && items.get(0).getTotalS22()>0){
					s22=true;
				}
				if(items.get(0).getTotalS29()!=null && items.get(0).getTotalS29()>0){
					s29=true;
				}
			}
					
			for(int i=0; i<items.size(); i++){
				currentRow =i+1;
				XSSFRow fila = sheet.createRow(currentRow);
				fila = sheet.getRow(currentRow);
				
				XSSFCell cell_idservicio = fila.createCell(0);
				cell_idservicio = fila.getCell(0);				
				cell_idservicio.setCellValue(items.get(i).getIdServicio());
				
				XSSFCell cell_servicio = fila.createCell(1);
				cell_servicio = fila.getCell(1);				
				cell_servicio.setCellValue(items.get(i).getNombreServicio());
				
				if(s24){
					XSSFCell tS24 = fila.createCell(2);
					tS24 = fila.getCell(2);				
					tS24.setCellValue(items.get(i).getTotalS24());
					tS24.setCellStyle(cellStyleLong);
					totalS24 += items.get(i).getTotalS24();
					
					if(!s21 && !s22 && !s29){
						XSSFCell totales = fila.createCell(3);
						totales = fila.getCell(3);				
						totales.setCellValue(items.get(i).getTotalServicio());
						totales.setCellStyle(cellStyleLong);
						totalServicio += items.get(i).getTotalServicio();
					}
				}
				if(!s24 && s21){
					XSSFCell tS21 = fila.createCell(2);
					tS21 = fila.getCell(2);				
					tS21.setCellValue(items.get(i).getTotalS21());
					tS21.setCellStyle(cellStyleLong);
					totalS21 += items.get(i).getTotalS21();
				}
				if(s24 && s21){
					XSSFCell tS21 = fila.createCell(3);
					tS21 = fila.getCell(3);				
					tS21.setCellValue(items.get(i).getTotalS21());
					tS21.setCellStyle(cellStyleLong);
					totalS21 += items.get(i).getTotalS21();
					
				}
				if(!s24 && !s21 && s22){
					XSSFCell tS22 = fila.createCell(2);
					tS22 = fila.getCell(2);				
					tS22.setCellValue(items.get(i).getTotalS22());
					tS22.setCellStyle(cellStyleLong);
					totalS22 += items.get(i).getTotalS22();
					
					if(!s29){
						XSSFCell totales = fila.createCell(3);
						totales = fila.getCell(3);				
						totales.setCellValue(items.get(i).getTotalServicio());
						totales.setCellStyle(cellStyleLong);
						totalServicio += items.get(i).getTotalServicio();
					}else{
						XSSFCell tS29 = fila.createCell(3);
						tS29 = fila.getCell(3);				
						tS29.setCellValue(items.get(i).getTotalS29());
						tS29.setCellStyle(cellStyleLong);
						totalS29 += items.get(i).getTotalS29();
						
						XSSFCell totales = fila.createCell(4);
						totales = fila.getCell(4);				
						totales.setCellValue(items.get(i).getTotalServicio());
						totales.setCellStyle(cellStyleLong);
						totalServicio += items.get(i).getTotalServicio();
					}
				}
				if((s24 || s21) && s22){
					XSSFCell tS22 = fila.createCell(3);
					tS22 = fila.getCell(3);				
					tS22.setCellValue(items.get(i).getTotalS22());
					tS22.setCellStyle(cellStyleLong);
					totalS22 += items.get(i).getTotalS22();
					
					if(!s29){
						XSSFCell totales = fila.createCell(4);
						totales = fila.getCell(4);				
						totales.setCellValue(items.get(i).getTotalServicio());
						totales.setCellStyle(cellStyleLong);
						totalServicio += items.get(i).getTotalServicio();
					}else{
						XSSFCell tS29 = fila.createCell(4);
						tS29 = fila.getCell(4);				
						tS29.setCellValue(items.get(i).getTotalS29());
						tS29.setCellStyle(cellStyleLong);
						totalS29 += items.get(i).getTotalS29();
						
						XSSFCell totales = fila.createCell(5);
						totales = fila.getCell(5);				
						totales.setCellValue(items.get(i).getTotalServicio());
						totales.setCellStyle(cellStyleLong);
						totalServicio += items.get(i).getTotalServicio();
					}
				}
				if(s24 && s21 && s22){
					XSSFCell tS22 = fila.createCell(4);
					tS22 = fila.getCell(4);				
					tS22.setCellValue(items.get(i).getTotalS22());
					tS22.setCellStyle(cellStyleLong);
					totalS22 += items.get(i).getTotalS22();
					
					if(!s29){
						XSSFCell totales = fila.createCell(5);
						totales = fila.getCell(5);				
						totales.setCellValue(items.get(i).getTotalServicio());
						totales.setCellStyle(cellStyleLong);
						totalServicio += items.get(i).getTotalServicio();
					}else{
						XSSFCell tS29 = fila.createCell(5);
						tS29 = fila.getCell(5);				
						tS29.setCellValue(items.get(i).getTotalS29());
						tS29.setCellStyle(cellStyleLong);
						totalS29 += items.get(i).getTotalS29();
						
						XSSFCell totales = fila.createCell(6);
						totales = fila.getCell(6);				
						totales.setCellValue(items.get(i).getTotalServicio());
						totales.setCellStyle(cellStyleLong);
						totalServicio += items.get(i).getTotalServicio();
					}
				}
				
				currentRow++;
			}
	       	
			XSSFRow fila = sheet.createRow(currentRow);
			fila = sheet.getRow(currentRow);
			
			XSSFCell total = fila.createCell(1);
			total = fila.getCell(1);				
			total.setCellValue("TOTALES ($)");
	       	
			
			if(s24){
				XSSFCell tS24 = fila.createCell(2);
				tS24 = fila.getCell(2);				
				tS24.setCellValue(totalS24);
				tS24.setCellStyle(cellStyleLong);
				
				if(!s21 && !s22 && !s29){
					XSSFCell totales = fila.createCell(3);
					totales = fila.getCell(3);				
					totales.setCellValue(totalServicio);
					totales.setCellStyle(cellStyleLong);
				}
			}
			if(!s24 && s21){
				XSSFCell tS21 = fila.createCell(2);
				tS21 = fila.getCell(2);				
				tS21.setCellValue(totalS21);
				tS21.setCellStyle(cellStyleLong);
			}
			if(s24 && s21){
				XSSFCell tS21 = fila.createCell(3);
				tS21 = fila.getCell(3);				
				tS21.setCellValue(totalS21);
				tS21.setCellStyle(cellStyleLong);
				
			}
			if(!s24 && !s21 && s22){
				XSSFCell tS22 = fila.createCell(2);
				tS22 = fila.getCell(2);				
				tS22.setCellValue(totalS22);
				tS22.setCellStyle(cellStyleLong);
				
				if(!s29){
					XSSFCell totales = fila.createCell(3);
					totales = fila.getCell(3);				
					totales.setCellValue(totalServicio);
					totales.setCellStyle(cellStyleLong);
				}else{
					XSSFCell tS29 = fila.createCell(3);
					tS29 = fila.getCell(3);				
					tS29.setCellValue(totalS29);
					tS29.setCellStyle(cellStyleLong);
					
					XSSFCell totales = fila.createCell(4);
					totales = fila.getCell(4);				
					totales.setCellValue(totalServicio);
					totales.setCellStyle(cellStyleLong);
				}
			}
			if((s24 || s21) && s22){
				XSSFCell tS22 = fila.createCell(3);
				tS22 = fila.getCell(3);				
				tS22.setCellValue(totalS22);
				tS22.setCellStyle(cellStyleLong);
				
				if(!s29){
					XSSFCell totales = fila.createCell(4);
					totales = fila.getCell(4);				
					totales.setCellValue(totalServicio);
					totales.setCellStyle(cellStyleLong);
				}else{
					XSSFCell tS29 = fila.createCell(4);
					tS29 = fila.getCell(4);				
					tS29.setCellValue(totalS29);
					tS29.setCellStyle(cellStyleLong);
					
					XSSFCell totales = fila.createCell(5);
					totales = fila.getCell(5);				
					totales.setCellValue(totalServicio);
					totales.setCellStyle(cellStyleLong);
				}
			}
			if(s24 && s21 && s22){
				XSSFCell tS22 = fila.createCell(4);
				tS22 = fila.getCell(4);				
				tS22.setCellValue(totalS22);
				tS22.setCellStyle(cellStyleLong);
				
				if(!s29){
					XSSFCell totales = fila.createCell(5);
					totales = fila.getCell(5);				
					totales.setCellValue(totalServicio);
					totales.setCellStyle(cellStyleLong);
				}else{
					XSSFCell tS29 = fila.createCell(5);
					tS29 = fila.getCell(5);				
					tS29.setCellValue(totalS29);
					tS29.setCellStyle(cellStyleLong);
					
					XSSFCell totales = fila.createCell(6);
					totales = fila.getCell(6);				
					totales.setCellValue(totalServicio);
					totales.setCellStyle(cellStyleLong);
				}
			}
			
		
	        
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

    private void addSheet(ProgramaAPSMunicipalesDetallesSheetExcel excelSheet, String sheetName){
        XSSFSheet sheet = workbook.createSheet(sheetName);
        List<CellExcelVO> header = excelSheet.getHeaderComplex();
        List<CellExcelVO> subHeader = excelSheet.getSubHeadeComplex();
        List<ProgramaAPSVO> items = excelSheet.getItems();
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
       int contador_cols = 0;
            for(CellExcelVO cellExcelVO : header){
                if(cellExcelVO.getRowSpan() > maxRowSpan){
                    maxRowSpan = cellExcelVO.getRowSpan();
                }
                if(cellExcelVO.getRowSpan() == maxRowSpan){
                    maxColSpan += cellExcelVO.getColSpan();
                }else{
                    addRow++;
                    	if(cellExcelVO.getColSpan() > contador_cols){
>>>>>>> programasReforzamiento
						contador_cols = cellExcelVO.getColSpan();
					}
					if(addRow == maxRowSpan){
						maxColSpan += contador_cols;
						contador_cols=0;
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
			int columnas_max = 0;
			int count_colums = 0;
			for(CellExcelVO cellExcelVO : header){
				if(cellExcelVO.getColSpan() > columnas_max){
					columnas_max =  cellExcelVO.getColSpan();
				}
				if(maxRowSpan == cellExcelVO.getRowSpan()){
					appendRow = 0;
					XSSFRow row = sheet.getRow(rowsReplace) ;
					XSSFCell cell = row.getCell(colsReplace);
					cell.setCellValue(cellExcelVO.getName());
					colsReplace += cellExcelVO.getColSpan();
					sheet.addMergedRegion(new CellRangeAddress(rowsReplace, (cellExcelVO.getRowSpan() - 1), colsReplace - cellExcelVO.getColSpan(), colsReplace -1 ));
				}else{
					count_colums += cellExcelVO.getColSpan();
					XSSFRow row = sheet.getRow(appendRow) ;
					XSSFCell cell = row.getCell(colsReplace);
					cell.setCellValue(cellExcelVO.getName());
					System.out.println("CellRangeAddress("+appendRow+","+appendRow+","+colsReplace+","+(colsReplace + cellExcelVO.getColSpan()-1)+")");
					sheet.addMergedRegion(new CellRangeAddress(appendRow, appendRow, colsReplace, (colsReplace + cellExcelVO.getColSpan()-1)));
					if(columnas_max == count_colums){
						appendRow++;
						count_colums=0;
					}else{
						colsReplace += cellExcelVO.getColSpan();
					}
<<<<<<< HEAD
				}
			}
		}
		if(subHeader != null && subHeader.size() > 0){
			int rows = 0;
			int cols = 0;
			addRow = 0;
			maxColSpan = 0;
			int contador_cols = 0;
			for(CellExcelVO cellExcelVO : subHeader){
				if(cellExcelVO.getRowSpan() > maxRowSpan){
					maxRowSpan = cellExcelVO.getRowSpan();
				}
			}
			for(CellExcelVO cellExcelVO : subHeader){
				if(cellExcelVO.getRowSpan() == maxRowSpan){
					maxColSpan += cellExcelVO.getColSpan();
				}else{
					addRow++;
					if(addRow != maxRowSpan){
						maxColSpan += cellExcelVO.getColSpan();
						cols = cellExcelVO.getColSpan();
						contador_cols = 0;
					}else{
						contador_cols += cellExcelVO.getColSpan();
						if(contador_cols == cols){
							addRow = 0;
						}else{
							addRow--;
						}
					}
				}
			}

			currentRow = 2;
			int totalRow = currentRow + maxRowSpan;
			for( ; currentRow < totalRow ; currentRow++){
				XSSFRow row = sheet.createRow(currentRow);
				for(int currentCol= 0; currentCol <= maxColSpan; currentCol++){
					XSSFCell cell = row.createCell(currentCol);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					CellStyle cellStyle = workbook.createCellStyle();
					cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
					cell.setCellStyle(cellStyle);
				}
			}
			currentRow = 2;
			int currentRowTmp = currentRow;
			int currentCol = 0, lengthColTmp = 0;
			int contadorColumnas = 0;
			addRow=0;
			for(CellExcelVO cellExcelVO : subHeader){
				if(maxRowSpan == cellExcelVO.getRowSpan()){
					XSSFRow row = sheet.getRow(currentRow) ;
					XSSFCell cell = row.getCell(currentCol);
					cell.setCellValue(cellExcelVO.getName());
					sheet.addMergedRegion(new CellRangeAddress(currentRow, (cellExcelVO.getRowSpan()==1) ? currentRow : ((currentRow + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
					if(cellExcelVO.getColSpan()==1){
						currentCol +=1;
					}else{
						currentCol += cellExcelVO.getColSpan();
					}
				}else{
					addRow++;
					if(addRow != maxRowSpan){
						XSSFRow row = sheet.getRow(currentRowTmp) ;
						XSSFCell cell = row.getCell(currentCol);
						cell.setCellValue(cellExcelVO.getName());
						sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
						lengthColTmp = cellExcelVO.getColSpan();
						currentRowTmp++;

					}else{
						contadorColumnas += cellExcelVO.getColSpan();
						if(lengthColTmp == contadorColumnas){
							XSSFRow row = sheet.getRow(currentRowTmp) ;
							XSSFCell cell = row.getCell(currentCol++);
							cell.setCellValue(cellExcelVO.getName());
							sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
							contadorColumnas = 0;
							addRow=0;
							currentRowTmp=currentRow;

						}else{
							XSSFRow row = sheet.getRow(currentRowTmp) ;
							XSSFCell cell = row.getCell(currentCol++);
							cell.setCellValue(cellExcelVO.getName());
							sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
							addRow--;
						}
					}
				}
=======
                }
            }
        }
        if(subHeader != null && subHeader.size() > 0){
            int rows = 0;
            int cols = 0;
            addRow = 0;
            maxColSpan = 0;
            	int contador_cols = 0;
			  for(CellExcelVO cellExcelVO : subHeader){
			        if(cellExcelVO.getRowSpan() > maxRowSpan){
			            maxRowSpan = cellExcelVO.getRowSpan();
			        }
			    }
			    for(CellExcelVO cellExcelVO : subHeader){
			        if(cellExcelVO.getRowSpan() == maxRowSpan){
			            maxColSpan += cellExcelVO.getColSpan();
			        }else{
			            addRow++;
			            if(addRow != maxRowSpan){
			                maxColSpan += cellExcelVO.getColSpan();
			                cols = cellExcelVO.getColSpan();
			                contador_cols = 0;
			            }else{
			                contador_cols += cellExcelVO.getColSpan();
			                if(contador_cols == cols){
			                    addRow = 0;
			                }else{
			                    addRow--;
			                }
			            }
			        }
			    }
			
			    currentRow = 2;
			    int totalRow = currentRow + maxRowSpan;
            for( ; currentRow < totalRow ; currentRow++){
                XSSFRow row = sheet.createRow(currentRow);
                for(int currentCol= 0; currentCol < maxColSpan; currentCol++){
                    XSSFCell cell = row.createCell(currentCol);
                    cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                    cell.setCellStyle(style);
                }
            }
           currentRow = 2;
			    int currentRowTmp = currentRow;
			    int currentCol = 0, lengthColTmp = 0;
			    int contadorColumnas = 0;
			    addRow=0;
			    for(CellExcelVO cellExcelVO : subHeader){
			        if(maxRowSpan == cellExcelVO.getRowSpan()){
			            XSSFRow row = sheet.getRow(currentRow) ;
			            XSSFCell cell = row.getCell(currentCol);
			            cell.setCellValue(cellExcelVO.getName());
			            sheet.addMergedRegion(new CellRangeAddress(currentRow, (cellExcelVO.getRowSpan()==1) ? currentRow : ((currentRow + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			            if(cellExcelVO.getColSpan()==1){
			                currentCol +=1;
			            }else{
			                currentCol += cellExcelVO.getColSpan();
			            }
			        }else{
			            addRow++;
			            if(addRow != maxRowSpan){
			                XSSFRow row = sheet.getRow(currentRowTmp) ;
			                XSSFCell cell = row.getCell(currentCol);
			                cell.setCellValue(cellExcelVO.getName());
			                sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			                lengthColTmp = cellExcelVO.getColSpan();
			                currentRowTmp++;
			                
			            }else{
			                contadorColumnas += cellExcelVO.getColSpan();
			                if(lengthColTmp == contadorColumnas){
			                    XSSFRow row = sheet.getRow(currentRowTmp) ;
			                    XSSFCell cell = row.getCell(currentCol++);
			                    cell.setCellValue(cellExcelVO.getName());
			                    sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			                    contadorColumnas = 0;
			                    addRow=0;
			                    currentRowTmp=currentRow;
			                    
			                }else{
			                    XSSFRow row = sheet.getRow(currentRowTmp) ;
			                    XSSFCell cell = row.getCell(currentCol++);
			                    cell.setCellValue(cellExcelVO.getName());
			                    sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			                    addRow--;
			                }
			            }
			        }
>>>>>>> programasReforzamiento
			}
			for(int columnPosition = 0; columnPosition <= maxColSpan; columnPosition++) {
				sheet.autoSizeColumn((short) (columnPosition));
			}
<<<<<<< HEAD

			for(int i=0; i<items.size(); i++){
				currentRow =i+4;
				XSSFRow row = sheet.createRow(currentRow);
				row = sheet.getRow(currentRow);
				XSSFCell cell_idservicio = row.createCell(0);
				cell_idservicio = row.getCell(0);				
				cell_idservicio.setCellValue(items.get(i).getIdServicioSalud());				

				XSSFCell cell_servicio = row.createCell(1);
				cell_servicio = row.getCell(1);
				cell_servicio.setCellValue(items.get(i).getServicioSalud());

				XSSFCell cell_idcomuna = row.createCell(2);
				cell_idcomuna = row.getCell(2);
				cell_idcomuna.setCellValue(items.get(i).getIdComuna());

				XSSFCell cell_comuna = row.createCell(3);
				cell_comuna = row.getCell(3);
				cell_comuna.setCellValue(items.get(i).getComuna());

				currentRow++;
			}
		}
	}

=======
		}
    }
  
 
    private void addSheet(ProgramaAPSMunicipalesSheetExcel excelSheet, String sheetName){
        XSSFSheet sheet = workbook.createSheet(sheetName);
        List<CellExcelVO> header = excelSheet.getHeaderComplex();
        List<CellExcelVO> subHeader = excelSheet.getSubHeadeComplex();
        List<ProgramaAPSVO> items = excelSheet.getItems();
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
       int contador_cols = 0;
            for(CellExcelVO cellExcelVO : header){
                if(cellExcelVO.getRowSpan() > maxRowSpan){
                    maxRowSpan = cellExcelVO.getRowSpan();
                }
                if(cellExcelVO.getRowSpan() == maxRowSpan){
                    maxColSpan += cellExcelVO.getColSpan();
                }else{
                    addRow++;
                    	if(cellExcelVO.getColSpan() > contador_cols){
						contador_cols = cellExcelVO.getColSpan();
					}
					if(addRow == maxRowSpan){
						maxColSpan += contador_cols;
						contador_cols=0;
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
	int columnas_max = 0;
			int count_colums = 0;
            for(CellExcelVO cellExcelVO : header){
	if(cellExcelVO.getColSpan() > columnas_max){
					columnas_max =  cellExcelVO.getColSpan();
				}
                if(maxRowSpan == cellExcelVO.getRowSpan()){
                    appendRow = 0;
                    XSSFRow row = sheet.getRow(rowsReplace) ;
                    XSSFCell cell = row.getCell(colsReplace);
                    cell.setCellValue(cellExcelVO.getName());
                    colsReplace += cellExcelVO.getColSpan();
                    sheet.addMergedRegion(new CellRangeAddress(rowsReplace, (cellExcelVO.getRowSpan() - 1), colsReplace - cellExcelVO.getColSpan(), colsReplace -1 ));
                }else{
count_colums += cellExcelVO.getColSpan();
                    XSSFRow row = sheet.getRow(appendRow) ;
                    XSSFCell cell = row.getCell(colsReplace);
                    cell.setCellValue(cellExcelVO.getName());
                    System.out.println("CellRangeAddress("+appendRow+","+appendRow+","+colsReplace+","+(colsReplace + cellExcelVO.getColSpan()-1)+")");
                    sheet.addMergedRegion(new CellRangeAddress(appendRow, appendRow, colsReplace, (colsReplace + cellExcelVO.getColSpan()-1)));
                   if(columnas_max == count_colums){
						appendRow++;
						count_colums=0;
					}else{
						colsReplace += cellExcelVO.getColSpan();
					}
                }
            }
        }
        if(subHeader != null && subHeader.size() > 0){
            int rows = 0;
            int cols = 0;
            addRow = 0;
            maxColSpan = 0;
            	int contador_cols = 0;
			  for(CellExcelVO cellExcelVO : subHeader){
			        if(cellExcelVO.getRowSpan() > maxRowSpan){
			            maxRowSpan = cellExcelVO.getRowSpan();
			        }
			    }
			    for(CellExcelVO cellExcelVO : subHeader){
			        if(cellExcelVO.getRowSpan() == maxRowSpan){
			            maxColSpan += cellExcelVO.getColSpan();
			        }else{
			            addRow++;
			            if(addRow != maxRowSpan){
			                maxColSpan += cellExcelVO.getColSpan();
			                cols = cellExcelVO.getColSpan();
			                contador_cols = 0;
			            }else{
			                contador_cols += cellExcelVO.getColSpan();
			                if(contador_cols == cols){
			                    addRow = 0;
			                }else{
			                    addRow--;
			                }
			            }
			        }
			    }
			
			    currentRow = 2;
			    int totalRow = currentRow + maxRowSpan;
            for( ; currentRow < totalRow ; currentRow++){
                XSSFRow row = sheet.createRow(currentRow);
                for(int currentCol= 0; currentCol < maxColSpan; currentCol++){
                    XSSFCell cell = row.createCell(currentCol);
                    cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                    cell.setCellStyle(style);
                }
            }
           currentRow = 2;
			    int currentRowTmp = currentRow;
			    int currentCol = 0, lengthColTmp = 0;
			    int contadorColumnas = 0;
			    addRow=0;
			    for(CellExcelVO cellExcelVO : subHeader){
			        if(maxRowSpan == cellExcelVO.getRowSpan()){
			            XSSFRow row = sheet.getRow(currentRow) ;
			            XSSFCell cell = row.getCell(currentCol);
			            cell.setCellValue(cellExcelVO.getName());
			            sheet.addMergedRegion(new CellRangeAddress(currentRow, (cellExcelVO.getRowSpan()==1) ? currentRow : ((currentRow + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			            if(cellExcelVO.getColSpan()==1){
			                currentCol +=1;
			            }else{
			                currentCol += cellExcelVO.getColSpan();
			            }
			        }else{
			            addRow++;
			            if(addRow != maxRowSpan){
			                XSSFRow row = sheet.getRow(currentRowTmp) ;
			                XSSFCell cell = row.getCell(currentCol);
			                cell.setCellValue(cellExcelVO.getName());
			                sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			                lengthColTmp = cellExcelVO.getColSpan();
			                currentRowTmp++;
			                
			            }else{
			                contadorColumnas += cellExcelVO.getColSpan();
			                if(lengthColTmp == contadorColumnas){
			                    XSSFRow row = sheet.getRow(currentRowTmp) ;
			                    XSSFCell cell = row.getCell(currentCol++);
			                    cell.setCellValue(cellExcelVO.getName());
			                    sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			                    contadorColumnas = 0;
			                    addRow=0;
			                    currentRowTmp=currentRow;
			                    
			                }else{
			                    XSSFRow row = sheet.getRow(currentRowTmp) ;
			                    XSSFCell cell = row.getCell(currentCol++);
			                    cell.setCellValue(cellExcelVO.getName());
			                    sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			                    addRow--;
			                }
			            }
			        }
			}
			for(int columnPosition = 0; columnPosition <= maxColSpan; columnPosition++) {
				sheet.autoSizeColumn((short) (columnPosition));
			}
			
			
			int id=0;
			int cantidadCompos =0;
			List<Integer> listaIdComponentes = new ArrayList<Integer>();
			for(ProgramaAPSVO item : items){
				if(id != item.getIdComponente()){
					id = item.getIdComponente();
					listaIdComponentes.add(item.getIdComponente());
					cantidadCompos++;
				}
			}
			
			System.out.println("RESULTADOS CONTIENEN "+cantidadCompos+" COMPONENTES");
			
			HashMap<Integer, List<ProgramaAPSVO>> componenteResultados = new HashMap<Integer, List<ProgramaAPSVO>>();
			
			for(int com=0; com < listaIdComponentes.size();com++){
				List<ProgramaAPSVO> result = new ArrayList<ProgramaAPSVO>();
				for(int i=0; i<items.size(); i++){
					if(listaIdComponentes.get(com).intValue()== items.get(i).getIdComponente()){
						result.add(items.get(i));
					}
				}
				componenteResultados.put(listaIdComponentes.get(com), result);
			}
		
			CellStyle cellStyleLong = workbook.createCellStyle();
            cellStyleLong.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			Long totalPrograma = 0l;
			
			int columna = 0;
			XSSFRow row = null;
			XSSFRow rowt = null;
			for (int lista = 0; lista < listaIdComponentes.size(); lista++) {
				currentRow=4;
				if(lista==0){
					columna=4;	
					
					for(int detalle=0; detalle < componenteResultados.get(listaIdComponentes.get(lista)).size();detalle++){
						sheet.createRow(currentRow);
						currentRow++;
					}
					currentRow=4;
					
				}
				if(lista==1){
					columna=7;
				}
				if(lista==2){
					columna=10;
				}
				if(lista==3){
					columna=13;
				}
				if(lista==4){
					columna=16;
				}
				if(lista==5){
					columna=19;
				}
				if(lista==6){
					columna=22;
				}
				if(lista==7){
					columna=25;
				}
				if(lista==8){
					columna=28;
				}
				if(lista==9){
					columna=31;
				}
				if(lista==10){
					columna=34;
				}
				if(lista==11){
					columna=37;
				}
				if(lista==12){
					columna=40;
				}
				if(lista==13){
					columna=43;
				}
				if(lista==14){
					columna=46;
				}
				if(lista==15){
					columna=49;
				}
				
				for(int detalle=0; detalle < componenteResultados.get(listaIdComponentes.get(lista)).size();detalle++){
					
					row = sheet.getRow(currentRow);
					XSSFCell cell_idservicio = row.createCell(0);
					cell_idservicio = row.getCell(0);				
					cell_idservicio.setCellValue(componenteResultados.get(listaIdComponentes.get(lista)).get(detalle).getIdServicioSalud());				
					
					XSSFCell cell_servicio = row.createCell(1);
					cell_servicio = row.getCell(1);
					cell_servicio.setCellValue(componenteResultados.get(listaIdComponentes.get(lista)).get(detalle).getServicioSalud());
					
					XSSFCell cell_idcomuna = row.createCell(2);
					cell_idcomuna = row.getCell(2);
					cell_idcomuna.setCellValue(componenteResultados.get(listaIdComponentes.get(lista)).get(detalle).getIdComuna());
					
					XSSFCell cell_comuna = row.createCell(3);
					cell_comuna = row.getCell(3);
					cell_comuna.setCellValue(componenteResultados.get(listaIdComponentes.get(lista)).get(detalle).getComuna());
					
					if(componenteResultados.get(listaIdComponentes.get(lista)).get(detalle).getTarifa()!= null && componenteResultados.get(listaIdComponentes.get(lista)).get(detalle).getTarifa() > 0){
						XSSFCell cell_p = row.createCell(columna);
						cell_p = row.getCell(columna);
						cell_p.setCellValue(componenteResultados.get(listaIdComponentes.get(lista)).get(detalle).getTarifa());
						cell_p.setCellStyle(cellStyleLong);
						
						XSSFCell cell_q = row.createCell(columna+1);
						cell_q = row.getCell(columna+1);
						cell_q.setCellValue(componenteResultados.get(listaIdComponentes.get(lista)).get(detalle).getCantidad());
						
						XSSFCell cell_total = row.createCell(columna+2);
						cell_total = row.getCell(columna+2);
						cell_total.setCellValue(componenteResultados.get(listaIdComponentes.get(lista)).get(detalle).getTotal());
						cell_total.setCellStyle(cellStyleLong);
						totalPrograma += componenteResultados.get(listaIdComponentes.get(lista)).get(detalle).getTotal();
					}
					currentRow++;
				}
				if(lista==0){
					rowt = sheet.createRow(currentRow);
					rowt = sheet.getRow(currentRow);
					
					XSSFCell cell_tot = rowt.createCell(1);
					cell_tot = rowt.getCell(1);				
					cell_tot.setCellValue("TOTAL ($)");
					
					XSSFCell cell_totMonto = rowt.createCell(6);
					cell_totMonto = rowt.getCell(6);				
					cell_totMonto.setCellValue(totalPrograma);	
					cell_totMonto.setCellStyle(cellStyleLong);
					totalPrograma=0l;
				}
				if(lista==1){
					rowt = sheet.getRow(currentRow);
					
					XSSFCell cell_totMonto = rowt.createCell(9);
					cell_totMonto = rowt.getCell(9);				
					cell_totMonto.setCellValue(totalPrograma);	
					cell_totMonto.setCellStyle(cellStyleLong);
					totalPrograma=0l;
				}
				if(lista==2){
					rowt = sheet.getRow(currentRow);
					
					XSSFCell cell_totMonto = rowt.createCell(12);
					cell_totMonto = rowt.getCell(12);				
					cell_totMonto.setCellValue(totalPrograma);	
					cell_totMonto.setCellStyle(cellStyleLong);
					totalPrograma=0l;
				}
				if(lista==3){
					rowt = sheet.getRow(currentRow);
					
					XSSFCell cell_totMonto = rowt.createCell(15);
					cell_totMonto = rowt.getCell(15);				
					cell_totMonto.setCellValue(totalPrograma);	
					cell_totMonto.setCellStyle(cellStyleLong);
					totalPrograma=0l;
				}
				if(lista==4){
					rowt = sheet.getRow(currentRow);
					
					XSSFCell cell_totMonto = rowt.createCell(18);
					cell_totMonto = rowt.getCell(18);				
					cell_totMonto.setCellValue(totalPrograma);	
					cell_totMonto.setCellStyle(cellStyleLong);
					totalPrograma=0l;
				}
				if(lista==5){
					rowt = sheet.getRow(currentRow);
					
					XSSFCell cell_totMonto = rowt.createCell(21);
					cell_totMonto = rowt.getCell(21);				
					cell_totMonto.setCellValue(totalPrograma);	
					cell_totMonto.setCellStyle(cellStyleLong);
					totalPrograma=0l;
				}
				if(lista==6){
					rowt = sheet.getRow(currentRow);
					
					XSSFCell cell_totMonto = rowt.createCell(24);
					cell_totMonto = rowt.getCell(24);				
					cell_totMonto.setCellValue(totalPrograma);	
					cell_totMonto.setCellStyle(cellStyleLong);
					totalPrograma=0l;
				}
				if(lista==7){
					rowt = sheet.getRow(currentRow);
					
					XSSFCell cell_totMonto = rowt.createCell(27);
					cell_totMonto = rowt.getCell(27);				
					cell_totMonto.setCellValue(totalPrograma);	
					cell_totMonto.setCellStyle(cellStyleLong);
					totalPrograma=0l;
				}
				if(lista==8){
					rowt = sheet.getRow(currentRow);
					
					XSSFCell cell_totMonto = rowt.createCell(30);
					cell_totMonto = rowt.getCell(30);				
					cell_totMonto.setCellValue(totalPrograma);	
					cell_totMonto.setCellStyle(cellStyleLong);
					totalPrograma=0l;
				}
				if(lista==9){
					rowt = sheet.getRow(currentRow);
					
					XSSFCell cell_totMonto = rowt.createCell(31);
					cell_totMonto = rowt.getCell(31);				
					cell_totMonto.setCellValue(totalPrograma);	
					cell_totMonto.setCellStyle(cellStyleLong);
					totalPrograma=0l;
				}
				if(lista==10){
					rowt = sheet.getRow(currentRow);
					
					XSSFCell cell_totMonto = rowt.createCell(34);
					cell_totMonto = rowt.getCell(34);				
					cell_totMonto.setCellValue(totalPrograma);	
					cell_totMonto.setCellStyle(cellStyleLong);
					totalPrograma=0l;
				}
				if(lista==11){
					rowt = sheet.getRow(currentRow);
					
					XSSFCell cell_totMonto = rowt.createCell(37);
					cell_totMonto = rowt.getCell(37);				
					cell_totMonto.setCellValue(totalPrograma);	
					cell_totMonto.setCellStyle(cellStyleLong);
					totalPrograma=0l;
				}
				if(lista==12){
					rowt = sheet.getRow(currentRow);
					
					XSSFCell cell_totMonto = rowt.createCell(40);
					cell_totMonto = rowt.getCell(40);				
					cell_totMonto.setCellValue(totalPrograma);	
					cell_totMonto.setCellStyle(cellStyleLong);
					totalPrograma=0l;
				}
				if(lista==13){
					rowt = sheet.getRow(currentRow);
					
					XSSFCell cell_totMonto = rowt.createCell(43);
					cell_totMonto = rowt.getCell(43);				
					cell_totMonto.setCellValue(totalPrograma);	
					cell_totMonto.setCellStyle(cellStyleLong);
					totalPrograma=0l;
				}
				if(lista==14){
					rowt = sheet.getRow(currentRow);
					
					XSSFCell cell_totMonto = rowt.createCell(46);
					cell_totMonto = rowt.getCell(46);				
					cell_totMonto.setCellValue(totalPrograma);	
					cell_totMonto.setCellStyle(cellStyleLong);
					totalPrograma=0l;
				}
				if(lista==15){
					rowt = sheet.getRow(currentRow);
					
					XSSFCell cell_totMonto = rowt.createCell(49);
					cell_totMonto = rowt.getCell(49);				
					cell_totMonto.setCellValue(totalPrograma);	
					cell_totMonto.setCellStyle(cellStyleLong);
					totalPrograma=0l;
				}
			
				
			}
			
			if(totalPrograma > 0){
				
			}
			
		}
    }
    
    private void addSheet(ProgramaAPSMunicipalesTemplateSheetExcel excelSheet, String sheetName){
        XSSFSheet sheet = workbook.createSheet(sheetName);
        List<CellExcelVO> header = excelSheet.getHeaderComplex();
        List<CellExcelVO> subHeader = excelSheet.getSubHeadeComplex();
        List<ProgramaAPSVO> items = excelSheet.getItems();
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
       int contador_cols = 0;
            for(CellExcelVO cellExcelVO : header){
                if(cellExcelVO.getRowSpan() > maxRowSpan){
                    maxRowSpan = cellExcelVO.getRowSpan();
                }
                if(cellExcelVO.getRowSpan() == maxRowSpan){
                    maxColSpan += cellExcelVO.getColSpan();
                }else{
                    addRow++;
                    	if(cellExcelVO.getColSpan() > contador_cols){
						contador_cols = cellExcelVO.getColSpan();
					}
					if(addRow == maxRowSpan){
						maxColSpan += contador_cols;
						contador_cols=0;
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
            int columnas_max = 0;
			int count_colums = 0;
            for(CellExcelVO cellExcelVO : header){
            	if(cellExcelVO.getColSpan() > columnas_max){
					columnas_max =  cellExcelVO.getColSpan();
				}
                if(maxRowSpan == cellExcelVO.getRowSpan()){
                    appendRow = 0;
                    XSSFRow row = sheet.getRow(rowsReplace) ;
                    XSSFCell cell = row.getCell(colsReplace);
                    cell.setCellValue(cellExcelVO.getName());
                    colsReplace += cellExcelVO.getColSpan();
                    sheet.addMergedRegion(new CellRangeAddress(rowsReplace, (cellExcelVO.getRowSpan() - 1), colsReplace - cellExcelVO.getColSpan(), colsReplace -1 ));
                }else{
                	count_colums += cellExcelVO.getColSpan();
                    XSSFRow row = sheet.getRow(appendRow) ;
                    XSSFCell cell = row.getCell(colsReplace);
                    cell.setCellValue(cellExcelVO.getName());
                    System.out.println("CellRangeAddress("+appendRow+","+appendRow+","+colsReplace+","+(colsReplace + cellExcelVO.getColSpan()-1)+")");
                    sheet.addMergedRegion(new CellRangeAddress(appendRow, appendRow, colsReplace, (colsReplace + cellExcelVO.getColSpan()-1)));
                   if(columnas_max == count_colums){
						appendRow++;
						count_colums=0;
					}else{
						colsReplace += cellExcelVO.getColSpan();
					}
                }
            }
        }
        if(subHeader != null && subHeader.size() > 0){
            int rows = 0;
            int cols = 0;
            addRow = 0;
            maxColSpan = 0;
            	int contador_cols = 0;
			  for(CellExcelVO cellExcelVO : subHeader){
			        if(cellExcelVO.getRowSpan() > maxRowSpan){
			            maxRowSpan = cellExcelVO.getRowSpan();
			        }
			    }
			    for(CellExcelVO cellExcelVO : subHeader){
			        if(cellExcelVO.getRowSpan() == maxRowSpan){
			            maxColSpan += cellExcelVO.getColSpan();
			        }else{
			            addRow++;
			            if(addRow != maxRowSpan){
			                maxColSpan += cellExcelVO.getColSpan();
			                cols = cellExcelVO.getColSpan();
			                contador_cols = 0;
			            }else{
			                contador_cols += cellExcelVO.getColSpan();
			                if(contador_cols == cols){
			                    addRow = 0;
			                }else{
			                    addRow--;
			                }
			            }
			        }
			    }
			
			    currentRow = 2;
			    int totalRow = currentRow + maxRowSpan;
            for( ; currentRow < totalRow ; currentRow++){
                XSSFRow row = sheet.createRow(currentRow);
                for(int currentCol= 0; currentCol < maxColSpan; currentCol++){
                    XSSFCell cell = row.createCell(currentCol);
                    cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                    cell.setCellStyle(style);
                }
            }
           currentRow = 2;
			    int currentRowTmp = currentRow;
			    int currentCol = 0, lengthColTmp = 0;
			    int contadorColumnas = 0;
			    addRow=0;
			    for(CellExcelVO cellExcelVO : subHeader){
			        if(maxRowSpan == cellExcelVO.getRowSpan()){
			            XSSFRow row = sheet.getRow(currentRow) ;
			            XSSFCell cell = row.getCell(currentCol);
			            cell.setCellValue(cellExcelVO.getName());
			            sheet.addMergedRegion(new CellRangeAddress(currentRow, (cellExcelVO.getRowSpan()==1) ? currentRow : ((currentRow + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			            if(cellExcelVO.getColSpan()==1){
			                currentCol +=1;
			            }else{
			                currentCol += cellExcelVO.getColSpan();
			            }
			        }else{
			            addRow++;
			            if(addRow != maxRowSpan){
			                XSSFRow row = sheet.getRow(currentRowTmp) ;
			                XSSFCell cell = row.getCell(currentCol);
			                cell.setCellValue(cellExcelVO.getName());
			                sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			                lengthColTmp = cellExcelVO.getColSpan();
			                currentRowTmp++;
			                
			            }else{
			                contadorColumnas += cellExcelVO.getColSpan();
			                if(lengthColTmp == contadorColumnas){
			                    XSSFRow row = sheet.getRow(currentRowTmp) ;
			                    XSSFCell cell = row.getCell(currentCol++);
			                    cell.setCellValue(cellExcelVO.getName());
			                    sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			                    contadorColumnas = 0;
			                    addRow=0;
			                    currentRowTmp=currentRow;
			                    
			                }else{
			                    XSSFRow row = sheet.getRow(currentRowTmp) ;
			                    XSSFCell cell = row.getCell(currentCol++);
			                    cell.setCellValue(cellExcelVO.getName());
			                    sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			                    addRow--;
			                }
			            }
			        }
			}
			for(int columnPosition = 0; columnPosition <= maxColSpan; columnPosition++) {
				sheet.autoSizeColumn((short) (columnPosition));
			}
			
			CellStyle cellStyleLong = workbook.createCellStyle();
            cellStyleLong.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			Long totalPrograma = 0l;
			for(int i=0; i<items.size(); i++){
				currentRow =i+4;
				XSSFRow row = sheet.createRow(currentRow);
				row = sheet.getRow(currentRow);
				XSSFCell cell_idservicio = row.createCell(0);
				cell_idservicio = row.getCell(0);				
				cell_idservicio.setCellValue(items.get(i).getIdServicioSalud());				
				
				XSSFCell cell_servicio = row.createCell(1);
				cell_servicio = row.getCell(1);
				cell_servicio.setCellValue(items.get(i).getServicioSalud());
				
				XSSFCell cell_idcomuna = row.createCell(2);
				cell_idcomuna = row.getCell(2);
				cell_idcomuna.setCellValue(items.get(i).getIdComuna());
				
				XSSFCell cell_comuna = row.createCell(3);
				cell_comuna = row.getCell(3);
				cell_comuna.setCellValue(items.get(i).getComuna());
				
				if(items.get(i).getTarifa()!= null && items.get(i).getTarifa() > 0){
					XSSFCell cell_p = row.createCell(4);
					cell_p = row.getCell(4);
					cell_p.setCellValue(items.get(i).getTarifa());
					cell_p.setCellStyle(cellStyleLong);
					
					XSSFCell cell_q = row.createCell(5);
					cell_q = row.getCell(5);
					cell_q.setCellValue(items.get(i).getCantidad());
					
					XSSFCell cell_total = row.createCell(6);
					cell_total = row.getCell(6);
					cell_total.setCellValue(items.get(i).getTotal());
					cell_total.setCellStyle(cellStyleLong);
					totalPrograma += items.get(i).getTotal();
				}
				
				
				currentRow++;
			}
			
			if(totalPrograma > 0){
				XSSFRow row = sheet.createRow(currentRow);
				row = sheet.getRow(currentRow);
				
				XSSFCell cell_tot = row.createCell(1);
				cell_tot = row.getCell(1);				
				cell_tot.setCellValue("TOTAL ($)");
				
				XSSFCell cell_totMonto = row.createCell(6);
				cell_totMonto = row.getCell(6);				
				cell_totMonto.setCellValue(totalPrograma);	
				cell_totMonto.setCellStyle(cellStyleLong);
			}
			
		}
			
    }
    
    private void addSheet(ProgramaAPSMunicipalesHistoricosSheetExcel excelSheet, String sheetName){
        XSSFSheet sheet = workbook.createSheet(sheetName);
        List<CellExcelVO> header = excelSheet.getHeaderComplex();
        List<CellExcelVO> subHeader = excelSheet.getSubHeadeComplex();
        List<ProgramaAPSVO> items = excelSheet.getItems();
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
        	int contador_cols = 0;
            for(CellExcelVO cellExcelVO : header){
                if(cellExcelVO.getRowSpan() > maxRowSpan){
                    maxRowSpan = cellExcelVO.getRowSpan();
                }
                if(cellExcelVO.getRowSpan() == maxRowSpan){
                    maxColSpan += cellExcelVO.getColSpan();
                }else{
                    addRow++;
                    	if(cellExcelVO.getColSpan() > contador_cols){
						contador_cols = cellExcelVO.getColSpan();
					}
					if(addRow == maxRowSpan){
						maxColSpan += contador_cols;
						contador_cols=0;
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
            int columnas_max = 0;
			int count_colums = 0;
            for(CellExcelVO cellExcelVO : header){
	if(cellExcelVO.getColSpan() > columnas_max){
					columnas_max =  cellExcelVO.getColSpan();
				}
                if(maxRowSpan == cellExcelVO.getRowSpan()){
                    appendRow = 0;
                    XSSFRow row = sheet.getRow(rowsReplace) ;
                    XSSFCell cell = row.getCell(colsReplace);
                    cell.setCellValue(cellExcelVO.getName());
                    colsReplace += cellExcelVO.getColSpan();
                    sheet.addMergedRegion(new CellRangeAddress(rowsReplace, (cellExcelVO.getRowSpan() - 1), colsReplace - cellExcelVO.getColSpan(), colsReplace -1 ));
                }else{
                	count_colums += cellExcelVO.getColSpan();
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
            	int contador_cols = 0;
			  for(CellExcelVO cellExcelVO : subHeader){
			        if(cellExcelVO.getRowSpan() > maxRowSpan){
			            maxRowSpan = cellExcelVO.getRowSpan();
			        }
			    }
			    for(CellExcelVO cellExcelVO : subHeader){
			        if(cellExcelVO.getRowSpan() == maxRowSpan){
			            maxColSpan += cellExcelVO.getColSpan();
			        }else{
			            addRow++;
			            if(addRow != maxRowSpan){
			                maxColSpan += cellExcelVO.getColSpan();
			                cols = cellExcelVO.getColSpan();
			                contador_cols = 0;
			            }else{
			                contador_cols += cellExcelVO.getColSpan();
			                if(contador_cols == cols){
			                    addRow = 0;
			                }else{
			                    addRow--;
			                }
			            }
			        }
			    }
			
			    currentRow = 2;
			    int totalRow = currentRow + maxRowSpan;
            for( ; currentRow < totalRow ; currentRow++){
                XSSFRow row = sheet.createRow(currentRow);
                for(int currentCol= 0; currentCol < maxColSpan; currentCol++){
                    XSSFCell cell = row.createCell(currentCol);
                    cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                    cell.setCellStyle(style);
                }
            }
           currentRow = 2;
			    int currentRowTmp = currentRow;
			    int currentCol = 0, lengthColTmp = 0;
			    int contadorColumnas = 0;
			    addRow=0;
			    for(CellExcelVO cellExcelVO : subHeader){
			        if(maxRowSpan == cellExcelVO.getRowSpan()){
			            XSSFRow row = sheet.getRow(currentRow) ;
			            XSSFCell cell = row.getCell(currentCol);
			            cell.setCellValue(cellExcelVO.getName());
			            sheet.addMergedRegion(new CellRangeAddress(currentRow, (cellExcelVO.getRowSpan()==1) ? currentRow : ((currentRow + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			            if(cellExcelVO.getColSpan()==1){
			                currentCol +=1;
			            }else{
			                currentCol += cellExcelVO.getColSpan();
			            }
			        }else{
			            addRow++;
			            if(addRow != maxRowSpan){
			                XSSFRow row = sheet.getRow(currentRowTmp) ;
			                XSSFCell cell = row.getCell(currentCol);
			                cell.setCellValue(cellExcelVO.getName());
			                sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			                lengthColTmp = cellExcelVO.getColSpan();
			                currentRowTmp++;
			                
			            }else{
			                contadorColumnas += cellExcelVO.getColSpan();
			                if(lengthColTmp == contadorColumnas){
			                    XSSFRow row = sheet.getRow(currentRowTmp) ;
			                    XSSFCell cell = row.getCell(currentCol++);
			                    cell.setCellValue(cellExcelVO.getName());
			                    sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			                    contadorColumnas = 0;
			                    addRow=0;
			                    currentRowTmp=currentRow;
			                    
			                }else{
			                    XSSFRow row = sheet.getRow(currentRowTmp) ;
			                    XSSFCell cell = row.getCell(currentCol++);
			                    cell.setCellValue(cellExcelVO.getName());
			                    sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			                    addRow--;
			                }
			            }
			        }
			}
			for(int columnPosition = 0; columnPosition <= maxColSpan; columnPosition++) {
				sheet.autoSizeColumn((short) (columnPosition));
			}
			CellStyle cellStyleLong = workbook.createCellStyle();
            cellStyleLong.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			Long totalPrograma = 0l;
			for(int i=0; i<items.size(); i++){
				currentRow =i+4;
				XSSFRow row = sheet.createRow(currentRow);
				row = sheet.getRow(currentRow);
				XSSFCell cell_idservicio = row.createCell(0);
				cell_idservicio = row.getCell(0);				
				cell_idservicio.setCellValue(items.get(i).getIdServicioSalud());				
				
				XSSFCell cell_servicio = row.createCell(1);
				cell_servicio = row.getCell(1);
				cell_servicio.setCellValue(items.get(i).getServicioSalud());
				
				XSSFCell cell_idcomuna = row.createCell(2);
				cell_idcomuna = row.getCell(2);
				cell_idcomuna.setCellValue(items.get(i).getIdComuna());
				
				XSSFCell cell_comuna = row.createCell(3);
				cell_comuna = row.getCell(3);
				cell_comuna.setCellValue(items.get(i).getComuna());
				
				XSSFCell cell_p = row.createCell(4);
				cell_p = row.getCell(4);
				cell_p.setCellValue(items.get(i).getTotal());
				cell_p.setCellStyle(cellStyleLong);
				totalPrograma += items.get(i).getTotal();
				currentRow++;
			}
			
			if(totalPrograma > 0){
				XSSFRow row = sheet.createRow(currentRow);
				row = sheet.getRow(currentRow);
				
				XSSFCell cell_tot = row.createCell(1);
				cell_tot = row.getCell(1);				
				cell_tot.setCellValue("TOTAL ($)");
				
				XSSFCell cell_totMonto = row.createCell(4);
				cell_totMonto = row.getCell(4);				
				cell_totMonto.setCellValue(totalPrograma);	
				cell_totMonto.setCellStyle(cellStyleLong);
			}
			
		}
    }
    
    private void addSheet(ProgramaAPSDetallesMunicipalesHistoricosSheetExcel excelSheet, String sheetName){
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
        	int contador_cols = 0;
            for(CellExcelVO cellExcelVO : header){
                if(cellExcelVO.getRowSpan() > maxRowSpan){
                    maxRowSpan = cellExcelVO.getRowSpan();
                }
                if(cellExcelVO.getRowSpan() == maxRowSpan){
                    maxColSpan += cellExcelVO.getColSpan();
                }else{
                    addRow++;
                    	if(cellExcelVO.getColSpan() > contador_cols){
						contador_cols = cellExcelVO.getColSpan();
					}
					if(addRow == maxRowSpan){
						maxColSpan += contador_cols;
						contador_cols=0;
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
            int columnas_max = 0;
			int count_colums = 0;
            for(CellExcelVO cellExcelVO : header){
	if(cellExcelVO.getColSpan() > columnas_max){
					columnas_max =  cellExcelVO.getColSpan();
				}
                if(maxRowSpan == cellExcelVO.getRowSpan()){
                    appendRow = 0;
                    XSSFRow row = sheet.getRow(rowsReplace) ;
                    XSSFCell cell = row.getCell(colsReplace);
                    cell.setCellValue(cellExcelVO.getName());
                    colsReplace += cellExcelVO.getColSpan();
                    sheet.addMergedRegion(new CellRangeAddress(rowsReplace, (cellExcelVO.getRowSpan() - 1), colsReplace - cellExcelVO.getColSpan(), colsReplace -1 ));
                }else{
                	count_colums += cellExcelVO.getColSpan();
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
            	int contador_cols = 0;
			  for(CellExcelVO cellExcelVO : subHeader){
			        if(cellExcelVO.getRowSpan() > maxRowSpan){
			            maxRowSpan = cellExcelVO.getRowSpan();
			        }
			    }
			    for(CellExcelVO cellExcelVO : subHeader){
			        if(cellExcelVO.getRowSpan() == maxRowSpan){
			            maxColSpan += cellExcelVO.getColSpan();
			        }else{
			            addRow++;
			            if(addRow != maxRowSpan){
			                maxColSpan += cellExcelVO.getColSpan();
			                cols = cellExcelVO.getColSpan();
			                contador_cols = 0;
			            }else{
			                contador_cols += cellExcelVO.getColSpan();
			                if(contador_cols == cols){
			                    addRow = 0;
			                }else{
			                    addRow--;
			                }
			            }
			        }
			    }
			
			    currentRow = 2;
			    int totalRow = currentRow + maxRowSpan;
            for( ; currentRow < totalRow ; currentRow++){
                XSSFRow row = sheet.createRow(currentRow);
                for(int currentCol= 0; currentCol < maxColSpan; currentCol++){
                    XSSFCell cell = row.createCell(currentCol);
                    cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                    cell.setCellStyle(style);
                }
            }
           currentRow = 2;
			    int currentRowTmp = currentRow;
			    int currentCol = 0, lengthColTmp = 0;
			    int contadorColumnas = 0;
			    addRow=0;
			    for(CellExcelVO cellExcelVO : subHeader){
			        if(maxRowSpan == cellExcelVO.getRowSpan()){
			            XSSFRow row = sheet.getRow(currentRow) ;
			            XSSFCell cell = row.getCell(currentCol);
			            cell.setCellValue(cellExcelVO.getName());
			            sheet.addMergedRegion(new CellRangeAddress(currentRow, (cellExcelVO.getRowSpan()==1) ? currentRow : ((currentRow + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			            if(cellExcelVO.getColSpan()==1){
			                currentCol +=1;
			            }else{
			                currentCol += cellExcelVO.getColSpan();
			            }
			        }else{
			            addRow++;
			            if(addRow != maxRowSpan){
			                XSSFRow row = sheet.getRow(currentRowTmp) ;
			                XSSFCell cell = row.getCell(currentCol);
			                cell.setCellValue(cellExcelVO.getName());
			                sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			                lengthColTmp = cellExcelVO.getColSpan();
			                currentRowTmp++;
			                
			            }else{
			                contadorColumnas += cellExcelVO.getColSpan();
			                if(lengthColTmp == contadorColumnas){
			                    XSSFRow row = sheet.getRow(currentRowTmp) ;
			                    XSSFCell cell = row.getCell(currentCol++);
			                    cell.setCellValue(cellExcelVO.getName());
			                    sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			                    contadorColumnas = 0;
			                    addRow=0;
			                    currentRowTmp=currentRow;
			                    
			                }else{
			                    XSSFRow row = sheet.getRow(currentRowTmp) ;
			                    XSSFCell cell = row.getCell(currentCol++);
			                    cell.setCellValue(cellExcelVO.getName());
			                    sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			                    addRow--;
			                }
			            }
			        }
			}
			for(int columnPosition = 0; columnPosition <= maxColSpan; columnPosition++) {
				sheet.autoSizeColumn((short) (columnPosition));
			}
		}
    }
    
    
    private void addSheet(ProgramaAPSLeySheetExcel excelSheet, String sheetName){
        XSSFSheet sheet = workbook.createSheet(sheetName);
        List<CellExcelVO> header = excelSheet.getHeaderComplex();
        List<CellExcelVO> subHeader = excelSheet.getSubHeadeComplex();
List<ProgramaAPSVO> items = excelSheet.getItems();
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
       int contador_cols = 0;
            for(CellExcelVO cellExcelVO : header){
                if(cellExcelVO.getRowSpan() > maxRowSpan){
                    maxRowSpan = cellExcelVO.getRowSpan();
                }
                if(cellExcelVO.getRowSpan() == maxRowSpan){
                    maxColSpan += cellExcelVO.getColSpan();
                }else{
                    addRow++;
                    	if(cellExcelVO.getColSpan() > contador_cols){
						contador_cols = cellExcelVO.getColSpan();
					}
					if(addRow == maxRowSpan){
						maxColSpan += contador_cols;
						contador_cols=0;
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
            int columnas_max = 0;
			int count_colums = 0;
            for(CellExcelVO cellExcelVO : header){
            	if(cellExcelVO.getColSpan() > columnas_max){
					columnas_max =  cellExcelVO.getColSpan();
				}
                if(maxRowSpan == cellExcelVO.getRowSpan()){
                    appendRow = 0;
                    XSSFRow row = sheet.getRow(rowsReplace) ;
                    XSSFCell cell = row.getCell(colsReplace);
                    cell.setCellValue(cellExcelVO.getName());
                    colsReplace += cellExcelVO.getColSpan();
                    sheet.addMergedRegion(new CellRangeAddress(rowsReplace, (cellExcelVO.getRowSpan() - 1), colsReplace - cellExcelVO.getColSpan(), colsReplace -1 ));
                }else{
                	count_colums += cellExcelVO.getColSpan();
                    XSSFRow row = sheet.getRow(appendRow) ;
                    XSSFCell cell = row.getCell(colsReplace);
                    cell.setCellValue(cellExcelVO.getName());
                    System.out.println("CellRangeAddress("+appendRow+","+appendRow+","+colsReplace+","+(colsReplace + cellExcelVO.getColSpan()-1)+")");
                    sheet.addMergedRegion(new CellRangeAddress(appendRow, appendRow, colsReplace, (colsReplace + cellExcelVO.getColSpan()-1)));
                   if(columnas_max == count_colums){
						appendRow++;
						count_colums=0;
					}else{
						colsReplace += cellExcelVO.getColSpan();
					}
                }
            }
        }
        if(subHeader != null && subHeader.size() > 0){
            int rows = 0;
            int cols = 0;
            addRow = 0;
            maxColSpan = 0;
            	int contador_cols = 0;
			  for(CellExcelVO cellExcelVO : subHeader){
			        if(cellExcelVO.getRowSpan() > maxRowSpan){
			            maxRowSpan = cellExcelVO.getRowSpan();
			        }
			    }
			    for(CellExcelVO cellExcelVO : subHeader){
			        if(cellExcelVO.getRowSpan() == maxRowSpan){
			            maxColSpan += cellExcelVO.getColSpan();
			        }else{
			            addRow++;
			            if(addRow != maxRowSpan){
			                maxColSpan += cellExcelVO.getColSpan();
			                cols = cellExcelVO.getColSpan();
			                contador_cols = 0;
			            }else{
			                contador_cols += cellExcelVO.getColSpan();
			                if(contador_cols == cols){
			                    addRow = 0;
			                }else{
			                    addRow--;
			                }
			            }
			        }
			    }
			
			    currentRow = 1;
			    int totalRow = currentRow + maxRowSpan;
            for( ; currentRow < totalRow ; currentRow++){
                XSSFRow row = sheet.createRow(currentRow);
                for(int currentCol= 0; currentCol < maxColSpan; currentCol++){
                    XSSFCell cell = row.createCell(currentCol);
                    cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                    cell.setCellStyle(style);
                }
            }
           currentRow = 1;
			    int currentRowTmp = currentRow;
			    int currentCol = 0, lengthColTmp = 0;
			    int contadorColumnas = 0;
			    addRow=0;
			    for(CellExcelVO cellExcelVO : subHeader){
			        if(maxRowSpan == cellExcelVO.getRowSpan()){
			            XSSFRow row = sheet.getRow(currentRow) ;
			            XSSFCell cell = row.getCell(currentCol);
			            cell.setCellValue(cellExcelVO.getName());
			            sheet.addMergedRegion(new CellRangeAddress(currentRow, (cellExcelVO.getRowSpan()==1) ? currentRow : ((currentRow + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			            if(cellExcelVO.getColSpan()==1){
			                currentCol +=1;
			            }else{
			                currentCol += cellExcelVO.getColSpan();
			            }
			        }else{
			            addRow++;
			            if(addRow != maxRowSpan){
			                XSSFRow row = sheet.getRow(currentRowTmp) ;
			                XSSFCell cell = row.getCell(currentCol);
			                cell.setCellValue(cellExcelVO.getName());
			                sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			                lengthColTmp = cellExcelVO.getColSpan();
			                currentRowTmp++;
			                
			            }else{
			                contadorColumnas += cellExcelVO.getColSpan();
			                if(lengthColTmp == contadorColumnas){
			                    XSSFRow row = sheet.getRow(currentRowTmp) ;
			                    XSSFCell cell = row.getCell(currentCol++);
			                    cell.setCellValue(cellExcelVO.getName());
			                    sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			                    contadorColumnas = 0;
			                    addRow=0;
			                    currentRowTmp=currentRow;
			                    
			                }else{
			                    XSSFRow row = sheet.getRow(currentRowTmp) ;
			                    XSSFCell cell = row.getCell(currentCol++);
			                    cell.setCellValue(cellExcelVO.getName());
			                    sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			                    addRow--;
			                }
			            }
			        }
			}
			for(int columnPosition = 0; columnPosition <= maxColSpan; columnPosition++) {
				sheet.autoSizeColumn((short) (columnPosition));
			}
			
			for(int i=0; i<items.size(); i++){
				currentRow =i+2;
				XSSFRow row = sheet.createRow(currentRow);
				row = sheet.getRow(currentRow);
				XSSFCell cell_idservicio = row.createCell(0);
				cell_idservicio = row.getCell(0);				
				cell_idservicio.setCellValue(items.get(i).getIdServicioSalud());				
				
				XSSFCell cell_servicio = row.createCell(1);
				cell_servicio = row.getCell(1);
				cell_servicio.setCellValue(items.get(i).getServicioSalud());
				
				XSSFCell cell_idcomuna = row.createCell(2);
				cell_idcomuna = row.getCell(2);
				cell_idcomuna.setCellValue(items.get(i).getIdComuna());
				
				XSSFCell cell_comuna = row.createCell(3);
				cell_comuna = row.getCell(3);
				cell_comuna.setCellValue(items.get(i).getComuna());
				
				currentRow++;
			}
		}
    }

	private void addSheet(ProgramaAPSServicioResumenSheetExcel excelSheet, String sheetName){
	/*	XSSFSheet sheet = workbook.createSheet(sheetName);
		List<CellExcelVO> header = excelSheet.getHeaderComplex();
		List<CellExcelVO> subHeader = excelSheet.getSubHeadeComplex();
		List<ProgramaAPSServicioResumenVO> items = excelSheet.getItems();
		CellStyle style = workbook.createCellStyle();
		style.setFillBackgroundColor(IndexedColors.BLUE.getIndex());
		style.setFillPattern(CellStyle.ALIGN_CENTER);
		Font font = workbook.createFont();
		font.setColor(IndexedColors.WHITE.getIndex());
		style.setFont(font);
		int currentRow = 0;
		int maxRowSpan = 0;
		int maxColSpan = 0;
		int addRow = 0;
		if(header != null && header.size() > 0){
			int contador_cols = 0;
			for(CellExcelVO cellExcelVO : header){
				if(cellExcelVO.getRowSpan() > maxRowSpan){
					maxRowSpan = cellExcelVO.getRowSpan();
				}
				if(cellExcelVO.getRowSpan() == maxRowSpan){
					maxColSpan += cellExcelVO.getColSpan();
				}else{
					addRow++;
					if(cellExcelVO.getColSpan() > contador_cols){
						contador_cols = cellExcelVO.getColSpan();
					}
					if(addRow == maxRowSpan){
						maxColSpan += contador_cols;
						contador_cols=0;
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
			int columnas_max = 0;
			int count_colums = 0;
			for(CellExcelVO cellExcelVO : header){
				if(cellExcelVO.getColSpan() > columnas_max){
					columnas_max =  cellExcelVO.getColSpan();
				}
				if(maxRowSpan == cellExcelVO.getRowSpan()){
					appendRow = 0;
					XSSFRow row = sheet.getRow(rowsReplace) ;
					XSSFCell cell = row.getCell(colsReplace);
					cell.setCellValue(cellExcelVO.getName());
					colsReplace += cellExcelVO.getColSpan();
					sheet.addMergedRegion(new CellRangeAddress(rowsReplace, (cellExcelVO.getRowSpan() - 1), colsReplace - cellExcelVO.getColSpan(), colsReplace -1 ));
				}else{
					count_colums += cellExcelVO.getColSpan();
					XSSFRow row = sheet.getRow(appendRow) ;	
					XSSFCell cell = row.getCell(colsReplace);
					cell.setCellValue(cellExcelVO.getName());
					System.out.println("CellRangeAddress("+appendRow+","+appendRow+","+colsReplace+","+(colsReplace + cellExcelVO.getColSpan()-1)+")");
					sheet.addMergedRegion(new CellRangeAddress(appendRow, appendRow, colsReplace, (colsReplace + cellExcelVO.getColSpan()-1)));
					if(columnas_max == count_colums){
						appendRow++;
						count_colums=0;
					}else{
						colsReplace += cellExcelVO.getColSpan();
					}
				}
			}
		}
		if(subHeader != null && subHeader.size() > 0){
			int rows = 0;
			int cols = 0;
			addRow = 0;
			maxColSpan = 0;
			int contador_cols = 0;
			  for(CellExcelVO cellExcelVO : subHeader){
			        if(cellExcelVO.getRowSpan() > maxRowSpan){
			            maxRowSpan = cellExcelVO.getRowSpan();
			        }
			    }
			    for(CellExcelVO cellExcelVO : subHeader){
			        if(cellExcelVO.getRowSpan() == maxRowSpan){
			            maxColSpan += cellExcelVO.getColSpan();
			        }else{
			            addRow++;
			            if(addRow != maxRowSpan){
			                maxColSpan += cellExcelVO.getColSpan();
			                cols = cellExcelVO.getColSpan();
			                contador_cols = 0;
			            }else{
			                contador_cols += cellExcelVO.getColSpan();
			                if(contador_cols == cols){
			                    addRow = 0;
			                }else{
			                    addRow--;
			                }
			            }
			        }
			    }
			
			    currentRow = 2;
			    int totalRow = currentRow + maxRowSpan;
			    for( ; currentRow < totalRow ; currentRow++){
			        XSSFRow row = sheet.createRow(currentRow);
			        for(int currentCol= 0; currentCol < maxColSpan; currentCol++){
			            XSSFCell cell = row.createCell(currentCol);
			            cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			            cell.setCellStyle(style);
			        }
			    }
			    currentRow = 2;
			    int currentRowTmp = currentRow;
			    int currentCol = 0, lengthColTmp = 0;
			    int contadorColumnas = 0;
			    addRow=0;
			    for(CellExcelVO cellExcelVO : subHeader){
			        if(maxRowSpan == cellExcelVO.getRowSpan()){
			            XSSFRow row = sheet.getRow(currentRow) ;
			            XSSFCell cell = row.getCell(currentCol);
			            cell.setCellValue(cellExcelVO.getName());
			            sheet.addMergedRegion(new CellRangeAddress(currentRow, (cellExcelVO.getRowSpan()==1) ? currentRow : ((currentRow + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			            if(cellExcelVO.getColSpan()==1){
			                currentCol +=1;
			            }else{
			                currentCol += cellExcelVO.getColSpan();
			            }
			        }else{
			            addRow++;
			            if(addRow != maxRowSpan){
			                XSSFRow row = sheet.getRow(currentRowTmp) ;
			                XSSFCell cell = row.getCell(currentCol);
			                cell.setCellValue(cellExcelVO.getName());
			                sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			                lengthColTmp = cellExcelVO.getColSpan();
			                currentRowTmp++;
			                
			            }else{
			                contadorColumnas += cellExcelVO.getColSpan();
			                if(lengthColTmp == contadorColumnas){
			                    XSSFRow row = sheet.getRow(currentRowTmp) ;
			                    XSSFCell cell = row.getCell(currentCol++);
			                    cell.setCellValue(cellExcelVO.getName());
			                    sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			                    contadorColumnas = 0;
			                    addRow=0;
			                    currentRowTmp=currentRow;
			                    
			                }else{
			                    XSSFRow row = sheet.getRow(currentRowTmp) ;
			                    XSSFCell cell = row.getCell(currentCol++);
			                    cell.setCellValue(cellExcelVO.getName());
			                    sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			                    addRow--;
			                }
			            }
			        }
			}
			for(int columnPosition = 0; columnPosition <= maxColSpan; columnPosition++) {
				sheet.autoSizeColumn((short) (columnPosition));
			}*/
		
		XSSFSheet sheet = workbook.createSheet(sheetName);
		List<CellExcelVO> header = excelSheet.getHeaderComplex();
		List<CellExcelVO> subHeader = excelSheet.getSubHeadeComplex();
		List<ProgramaAPSServicioResumenVO> items = excelSheet.getItems();
		CellStyle style = workbook.createCellStyle();
		style.setFillBackgroundColor(IndexedColors.BLUE.getIndex());
		style.setFillPattern(CellStyle.ALIGN_CENTER);
		Font font = workbook.createFont();
		font.setColor(IndexedColors.WHITE.getIndex());
		style.setFont(font);
		int currentRow = 0;
		int maxRowSpan = 0;
		int maxColSpan = 0;
		int addRow = 0;
		if(header != null && header.size() > 0){
			int contador_cols = 0;
			for(CellExcelVO cellExcelVO : header){
				if(cellExcelVO.getRowSpan() > maxRowSpan){
					maxRowSpan = cellExcelVO.getRowSpan();
				}
				if(cellExcelVO.getRowSpan() == maxRowSpan){
					maxColSpan += cellExcelVO.getColSpan();
				}else{
					addRow++;
					if(cellExcelVO.getColSpan() > contador_cols){
						contador_cols = cellExcelVO.getColSpan();
					}
					if(addRow == maxRowSpan){
						maxColSpan += contador_cols;
						contador_cols=0;
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
			int columnas_max = 0;
			int count_colums = 0;
			for(CellExcelVO cellExcelVO : header){
				if(cellExcelVO.getColSpan() > columnas_max){
					columnas_max =  cellExcelVO.getColSpan();
				}
				if(maxRowSpan == cellExcelVO.getRowSpan()){
					appendRow = 0;
					XSSFRow row = sheet.getRow(rowsReplace) ;
					XSSFCell cell = row.getCell(colsReplace);
					cell.setCellValue(cellExcelVO.getName());
					colsReplace += cellExcelVO.getColSpan();
					sheet.addMergedRegion(new CellRangeAddress(rowsReplace, (cellExcelVO.getRowSpan() - 1), colsReplace - cellExcelVO.getColSpan(), colsReplace -1 ));
				}else{
					count_colums += cellExcelVO.getColSpan();
					XSSFRow row = sheet.getRow(appendRow) ;	
					XSSFCell cell = row.getCell(colsReplace);
					cell.setCellValue(cellExcelVO.getName());
					System.out.println("CellRangeAddress("+appendRow+","+appendRow+","+colsReplace+","+(colsReplace + cellExcelVO.getColSpan()-1)+")");
					sheet.addMergedRegion(new CellRangeAddress(appendRow, appendRow, colsReplace, (colsReplace + cellExcelVO.getColSpan()-1)));
					if(columnas_max == count_colums){
						appendRow++;
						count_colums=0;
					}else{
						colsReplace += cellExcelVO.getColSpan();
					}
				}
			}
		}
		if(subHeader != null && subHeader.size() > 0){
			int rows = 0;
			int cols = 0;
			addRow = 0;
			maxColSpan = 0;
			int contador_cols = 0;
			  for(CellExcelVO cellExcelVO : subHeader){
			        if(cellExcelVO.getRowSpan() > maxRowSpan){
			            maxRowSpan = cellExcelVO.getRowSpan();
			        }
			    }
			    for(CellExcelVO cellExcelVO : subHeader){
			        if(cellExcelVO.getRowSpan() == maxRowSpan){
			            maxColSpan += cellExcelVO.getColSpan();
			        }else{
			            addRow++;
			            if(addRow != maxRowSpan){
			                maxColSpan += cellExcelVO.getColSpan();
			                cols = cellExcelVO.getColSpan();
			                contador_cols = 0;
			            }else{
			                contador_cols += cellExcelVO.getColSpan();
			                if(contador_cols == cols){
			                    addRow = 0;
			                }else{
			                    addRow--;
			                }
			            }
			        }
			    }
			
			    currentRow = 2;
			    int totalRow = currentRow + maxRowSpan;
			    for( ; currentRow < totalRow ; currentRow++){
			        XSSFRow row = sheet.createRow(currentRow);
			        for(int currentCol= 0; currentCol < maxColSpan; currentCol++){
			            XSSFCell cell = row.createCell(currentCol);
			            cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			            CellStyle cellStyle = workbook.createCellStyle();
			            cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			            cell.setCellStyle(style);
			        }
			    }
			    currentRow = 2;
			    int currentRowTmp = currentRow;
			    int currentCol = 0, lengthColTmp = 0;
			    int contadorColumnas = 0;
			    addRow=0;
			    for(CellExcelVO cellExcelVO : subHeader){
			        if(maxRowSpan == cellExcelVO.getRowSpan()){
			            XSSFRow row = sheet.getRow(currentRow) ;
			            XSSFCell cell = row.getCell(currentCol);
			            cell.setCellValue(cellExcelVO.getName());
			            sheet.addMergedRegion(new CellRangeAddress(currentRow, (cellExcelVO.getRowSpan()==1) ? currentRow : ((currentRow + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			            if(cellExcelVO.getColSpan()==1){
			                currentCol +=1;
			            }else{
			                currentCol += cellExcelVO.getColSpan();
			            }
			        }else{
			            addRow++;
			            if(addRow != maxRowSpan){
			                XSSFRow row = sheet.getRow(currentRowTmp) ;
			                XSSFCell cell = row.getCell(currentCol);
			                cell.setCellValue(cellExcelVO.getName());
			                sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			                lengthColTmp = cellExcelVO.getColSpan();
			                currentRowTmp++;
			                
			            }else{
			                contadorColumnas += cellExcelVO.getColSpan();
			                if(lengthColTmp == contadorColumnas){
			                    XSSFRow row = sheet.getRow(currentRowTmp) ;
			                    XSSFCell cell = row.getCell(currentCol++);
			                    cell.setCellValue(cellExcelVO.getName());
			                    sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			                    contadorColumnas = 0;
			                    addRow=0;
			                    currentRowTmp=currentRow;
			                    
			                }else{
			                    XSSFRow row = sheet.getRow(currentRowTmp) ;
			                    XSSFCell cell = row.getCell(currentCol++);
			                    cell.setCellValue(cellExcelVO.getName());
			                    sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			                    addRow--;
			                }
			            }
			        }
			}
			for(int columnPosition = 0; columnPosition <= maxColSpan; columnPosition++) {
				sheet.autoSizeColumn((short) (columnPosition));
			}
			
		
			CellStyle cellStyleLong = workbook.createCellStyle();
            cellStyleLong.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			Long totalPrograma = 0l;
			
		
			currentRow = 4;
			for (ProgramaAPSServicioResumenVO registro : items) {
				int columna = 4;
				XSSFRow row = sheet.createRow(currentRow);
				row = sheet.getRow(currentRow);
				XSSFCell cell_idservicio = row.createCell(0);
				cell_idservicio = row.getCell(0);				
				cell_idservicio.setCellValue(registro.getIdServicioSalud());				
				
				XSSFCell cell_servicio = row.createCell(1);
				cell_servicio = row.getCell(1);
				cell_servicio.setCellValue(registro.getServicioSalud());
				
				XSSFCell cell_idcomuna = row.createCell(2);
				cell_idcomuna = row.getCell(2);
				cell_idcomuna.setCellValue(registro.getCodigoEstablecimiento());
				
				XSSFCell cell_comuna = row.createCell(3);
				cell_comuna = row.getCell(3);
				cell_comuna.setCellValue(registro.getEstablecimiento());
				
				for(ComponentesVO componentes : registro.getComponentes()){
					
					for(SubtituloVO subtitulos : componentes.getSubtitulos()){
						
						XSSFCell cell_precio = row.createCell(columna);
						cell_precio = row.getCell(columna);
						cell_precio.setCellValue(subtitulos.getTarifa());
						
						XSSFCell cell_cantidad = row.createCell(columna+1);
						cell_cantidad = row.getCell(columna+1);
						cell_cantidad.setCellValue(subtitulos.getCantidad());
						
						XSSFCell cell_total = row.createCell(columna+2);
						cell_total = row.getCell(columna+2);
						cell_total.setCellValue(subtitulos.getTotal());
						cell_total.setCellStyle(cellStyleLong);
						
						columna +=3;
					}
				}
				currentRow++;
			}
			
			XSSFRow rowt = sheet.createRow(currentRow);
			rowt = sheet.getRow(currentRow);
			
			XSSFCell cell_total = rowt.createCell(1);
			cell_total = rowt.getCell(1);
			cell_total.setCellValue("TOTALES ($)");
			
			HashMap<Integer, HashMap<Integer, Long>> totales = new HashMap<Integer, HashMap<Integer,Long>>();
			
			for (ProgramaAPSServicioResumenVO registro : items) {
				
				for(ComponentesVO componentes : registro.getComponentes()){
					
					if(!totales.containsKey(componentes.getId())){
						
						HashMap<Integer, Long> tot = new HashMap<Integer, Long>();
						
						for(SubtituloVO subtitulos : componentes.getSubtitulos()){
							
							if(!tot.containsKey(subtitulos.getId())){
								tot.put(subtitulos.getId(), subtitulos.getTotal().longValue());
							}else{
								Long suma = tot.get(subtitulos.getId());
								suma += subtitulos.getTotal().longValue();
								tot.put(subtitulos.getId(), suma);
							}
						}
						totales.put(componentes.getId(), tot);
						
					}else{
						HashMap<Integer, Long> tot = totales.get(componentes.getId());
						
						for(SubtituloVO subtitulos : componentes.getSubtitulos()){
							
							if(!tot.containsKey(subtitulos.getId())){
								tot.put(subtitulos.getId(), subtitulos.getTotal().longValue());
							}else{
								Long suma = tot.get(subtitulos.getId());
								suma += subtitulos.getTotal().longValue();
								tot.put(subtitulos.getId(), suma);
							}
							
						}
						totales.put(componentes.getId(), tot);
					}
					
					
				}
			}
			
			System.out.println(totales);
			List<Long> totalesFinales = new ArrayList<Long>();
			List keys = new ArrayList(totales.keySet());
			Collections.sort(keys);
			
			for(int i=0; i < keys.size();i++){
				
				HashMap<Integer, Long> valorTotal = totales.get(keys.get(i));
				List claves = new ArrayList(valorTotal.keySet());
				Collections.sort(claves);
				
				for(int j=0; j<claves.size(); j++){
					totalesFinales.add(valorTotal.get(claves.get(j)));
				}
				
				
			}
			
			int columna =6;
			for(int h=0; h < totalesFinales.size(); h++){
				XSSFCell cell_totalFinal = rowt.createCell(columna);
				cell_totalFinal = rowt.getCell(columna);
				cell_totalFinal.setCellValue(totalesFinales.get(h));
				cell_totalFinal.setCellStyle(cellStyleLong);
				columna +=3;
			}
			System.out.println("************************");
			
		}
	}
	
	private void addSheet(ProgramaAPSServiciosDetallesSheetExcel excelSheet, String sheetName){
		XSSFSheet sheet = workbook.createSheet(sheetName);
		List<CellExcelVO> header = excelSheet.getHeaderComplex();
		List<CellExcelVO> subHeader = excelSheet.getSubHeadeComplex();
		CellStyle style = workbook.createCellStyle();
		style.setFillBackgroundColor(IndexedColors.BLUE.getIndex());
		style.setFillPattern(CellStyle.ALIGN_CENTER);
		Font font = workbook.createFont();
		font.setColor(IndexedColors.WHITE.getIndex());
		style.setFont(font);
		int currentRow = 0;
		int maxRowSpan = 0;
		int maxColSpan = 0;
		int addRow = 0;
		if(header != null && header.size() > 0){
			int contador_cols = 0;
			for(CellExcelVO cellExcelVO : header){
				if(cellExcelVO.getRowSpan() > maxRowSpan){
					maxRowSpan = cellExcelVO.getRowSpan();
				}
				if(cellExcelVO.getRowSpan() == maxRowSpan){
					maxColSpan += cellExcelVO.getColSpan();
				}else{
					addRow++;
					if(cellExcelVO.getColSpan() > contador_cols){
						contador_cols = cellExcelVO.getColSpan();
					}
					if(addRow == maxRowSpan){
						maxColSpan += contador_cols;
						contador_cols=0;
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
			int columnas_max = 0;
			int count_colums = 0;
			for(CellExcelVO cellExcelVO : header){
				if(cellExcelVO.getColSpan() > columnas_max){
					columnas_max =  cellExcelVO.getColSpan();
				}
				if(maxRowSpan == cellExcelVO.getRowSpan()){
					appendRow = 0;
					XSSFRow row = sheet.getRow(rowsReplace) ;
					XSSFCell cell = row.getCell(colsReplace);
					cell.setCellValue(cellExcelVO.getName());
					colsReplace += cellExcelVO.getColSpan();
					sheet.addMergedRegion(new CellRangeAddress(rowsReplace, (cellExcelVO.getRowSpan() - 1), colsReplace - cellExcelVO.getColSpan(), colsReplace -1 ));
				}else{
					count_colums += cellExcelVO.getColSpan();
					XSSFRow row = sheet.getRow(appendRow) ;	
					XSSFCell cell = row.getCell(colsReplace);
					cell.setCellValue(cellExcelVO.getName());
					System.out.println("CellRangeAddress("+appendRow+","+appendRow+","+colsReplace+","+(colsReplace + cellExcelVO.getColSpan()-1)+")");
					sheet.addMergedRegion(new CellRangeAddress(appendRow, appendRow, colsReplace, (colsReplace + cellExcelVO.getColSpan()-1)));
					if(columnas_max == count_colums){
						appendRow++;
						count_colums=0;
					}else{
						colsReplace += cellExcelVO.getColSpan();
					}
				}
			}
		}
		if(subHeader != null && subHeader.size() > 0){
			int rows = 0;
			int cols = 0;
			addRow = 0;
			maxColSpan = 0;
			int contador_cols = 0;
			  for(CellExcelVO cellExcelVO : subHeader){
			        if(cellExcelVO.getRowSpan() > maxRowSpan){
			            maxRowSpan = cellExcelVO.getRowSpan();
			        }
			    }
			    for(CellExcelVO cellExcelVO : subHeader){
			        if(cellExcelVO.getRowSpan() == maxRowSpan){
			            maxColSpan += cellExcelVO.getColSpan();
			        }else{
			            addRow++;
			            if(addRow != maxRowSpan){
			                maxColSpan += cellExcelVO.getColSpan();
			                cols = cellExcelVO.getColSpan();
			                contador_cols = 0;
			            }else{
			                contador_cols += cellExcelVO.getColSpan();
			                if(contador_cols == cols){
			                    addRow = 0;
			                }else{
			                    addRow--;
			                }
			            }
			        }
			    }
			
			    currentRow = 2;
			    int totalRow = currentRow + maxRowSpan;
			    for( ; currentRow < totalRow ; currentRow++){
			        XSSFRow row = sheet.createRow(currentRow);
			        for(int currentCol= 0; currentCol < maxColSpan; currentCol++){
			            XSSFCell cell = row.createCell(currentCol);
			            cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			            cell.setCellStyle(style);
			        }
			    }
			    currentRow = 2;
			    int currentRowTmp = currentRow;
			    int currentCol = 0, lengthColTmp = 0;
			    int contadorColumnas = 0;
			    addRow=0;
			    for(CellExcelVO cellExcelVO : subHeader){
			        if(maxRowSpan == cellExcelVO.getRowSpan()){
			            XSSFRow row = sheet.getRow(currentRow) ;
			            XSSFCell cell = row.getCell(currentCol);
			            cell.setCellValue(cellExcelVO.getName());
			            sheet.addMergedRegion(new CellRangeAddress(currentRow, (cellExcelVO.getRowSpan()==1) ? currentRow : ((currentRow + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			            if(cellExcelVO.getColSpan()==1){
			                currentCol +=1;
			            }else{
			                currentCol += cellExcelVO.getColSpan();
			            }
			        }else{
			            addRow++;
			            if(addRow != maxRowSpan){
			                XSSFRow row = sheet.getRow(currentRowTmp) ;
			                XSSFCell cell = row.getCell(currentCol);
			                cell.setCellValue(cellExcelVO.getName());
			                sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			                lengthColTmp = cellExcelVO.getColSpan();
			                currentRowTmp++;
			                
			            }else{
			                contadorColumnas += cellExcelVO.getColSpan();
			                if(lengthColTmp == contadorColumnas){
			                    XSSFRow row = sheet.getRow(currentRowTmp) ;
			                    XSSFCell cell = row.getCell(currentCol++);
			                    cell.setCellValue(cellExcelVO.getName());
			                    sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			                    contadorColumnas = 0;
			                    addRow=0;
			                    currentRowTmp=currentRow;
			                    
			                }else{
			                    XSSFRow row = sheet.getRow(currentRowTmp) ;
			                    XSSFCell cell = row.getCell(currentCol++);
			                    cell.setCellValue(cellExcelVO.getName());
			                    sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			                    addRow--;
			                }
			            }
			        }
			}
			for(int columnPosition = 0; columnPosition <= maxColSpan; columnPosition++) {
				sheet.autoSizeColumn((short) (columnPosition));
			}
		}
	}
	private void addSheet(ProgramaAPSServiciosHistoricosSheetExcel excelSheet, String sheetName){
		XSSFSheet sheet = workbook.createSheet(sheetName);
		List<CellExcelVO> header = excelSheet.getHeaderComplex();
		List<CellExcelVO> subHeader = excelSheet.getSubHeadeComplex();
		List<ProgramaAPSServicioResumenVO> items = excelSheet.getItems();
		CellStyle style = workbook.createCellStyle();
		style.setFillBackgroundColor(IndexedColors.BLUE.getIndex());
		style.setFillPattern(CellStyle.ALIGN_CENTER);
		Font font = workbook.createFont();
		font.setColor(IndexedColors.WHITE.getIndex());
		style.setFont(font);
		int currentRow = 0;
		int maxRowSpan = 0;
		int maxColSpan = 0;
		int addRow = 0;
		if(header != null && header.size() > 0){
			int contador_cols = 0;
			for(CellExcelVO cellExcelVO : header){
				if(cellExcelVO.getRowSpan() > maxRowSpan){
					maxRowSpan = cellExcelVO.getRowSpan();
				}
				if(cellExcelVO.getRowSpan() == maxRowSpan){
					maxColSpan += cellExcelVO.getColSpan();
				}else{
					addRow++;
					if(cellExcelVO.getColSpan() > contador_cols){
						contador_cols = cellExcelVO.getColSpan();
					}
					if(addRow == maxRowSpan){
						maxColSpan += contador_cols;
						contador_cols=0;
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
			int columnas_max = 0;
			int count_colums = 0;
			for(CellExcelVO cellExcelVO : header){
				if(cellExcelVO.getColSpan() > columnas_max){
					columnas_max =  cellExcelVO.getColSpan();
				}
				if(maxRowSpan == cellExcelVO.getRowSpan()){
					appendRow = 0;
					XSSFRow row = sheet.getRow(rowsReplace) ;
					XSSFCell cell = row.getCell(colsReplace);
					cell.setCellValue(cellExcelVO.getName());
					colsReplace += cellExcelVO.getColSpan();
					sheet.addMergedRegion(new CellRangeAddress(rowsReplace, (cellExcelVO.getRowSpan() - 1), colsReplace - cellExcelVO.getColSpan(), colsReplace -1 ));
				}else{
					count_colums += cellExcelVO.getColSpan();
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
			int contador_cols = 0;
			  for(CellExcelVO cellExcelVO : subHeader){
			        if(cellExcelVO.getRowSpan() > maxRowSpan){
			            maxRowSpan = cellExcelVO.getRowSpan();
			        }
			    }
			    for(CellExcelVO cellExcelVO : subHeader){
			        if(cellExcelVO.getRowSpan() == maxRowSpan){
			            maxColSpan += cellExcelVO.getColSpan();
			        }else{
			            addRow++;
			            if(addRow != maxRowSpan){
			                maxColSpan += cellExcelVO.getColSpan();
			                cols = cellExcelVO.getColSpan();
			                contador_cols = 0;
			            }else{
			                contador_cols += cellExcelVO.getColSpan();
			                if(contador_cols == cols){
			                    addRow = 0;
			                }else{
			                    addRow--;
			                }
			            }
			        }
			    }
			
			    currentRow = 2;
			    int totalRow = currentRow + maxRowSpan;
			    for( ; currentRow < totalRow ; currentRow++){
			        XSSFRow row = sheet.createRow(currentRow);
			        for(int currentCol= 0; currentCol < maxColSpan; currentCol++){
			            XSSFCell cell = row.createCell(currentCol);
			            cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			            cell.setCellStyle(style);
			        }
			    }
			    currentRow = 2;
			    int currentRowTmp = currentRow;
			    int currentCol = 0, lengthColTmp = 0;
			    int contadorColumnas = 0;
			    addRow=0;
			    for(CellExcelVO cellExcelVO : subHeader){
			        if(maxRowSpan == cellExcelVO.getRowSpan()){
			            XSSFRow row = sheet.getRow(currentRow) ;
			            XSSFCell cell = row.getCell(currentCol);
			            cell.setCellValue(cellExcelVO.getName());
			            sheet.addMergedRegion(new CellRangeAddress(currentRow, (cellExcelVO.getRowSpan()==1) ? currentRow : ((currentRow + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			            if(cellExcelVO.getColSpan()==1){
			                currentCol +=1;
			            }else{
			                currentCol += cellExcelVO.getColSpan();
			            }
			        }else{
			            addRow++;
			            if(addRow != maxRowSpan){
			                XSSFRow row = sheet.getRow(currentRowTmp) ;
			                XSSFCell cell = row.getCell(currentCol);
			                cell.setCellValue(cellExcelVO.getName());
			                sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			                lengthColTmp = cellExcelVO.getColSpan();
			                currentRowTmp++;
			                
			            }else{
			                contadorColumnas += cellExcelVO.getColSpan();
			                if(lengthColTmp == contadorColumnas){
			                    XSSFRow row = sheet.getRow(currentRowTmp) ;
			                    XSSFCell cell = row.getCell(currentCol++);
			                    cell.setCellValue(cellExcelVO.getName());
			                    sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			                    contadorColumnas = 0;
			                    addRow=0;
			                    currentRowTmp=currentRow;
			                    
			                }else{
			                    XSSFRow row = sheet.getRow(currentRowTmp) ;
			                    XSSFCell cell = row.getCell(currentCol++);
			                    cell.setCellValue(cellExcelVO.getName());
			                    sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			                    addRow--;
			                }
			            }
			        }
			}
			for(int columnPosition = 0; columnPosition <= maxColSpan; columnPosition++) {
				sheet.autoSizeColumn((short) (columnPosition));
			}
			boolean s21=false;
			boolean s22=false;
			boolean s29=false;
			Long totalS21=0l;
			Long totalS22=0l;
			Long totalS29=0l;
			CellStyle cellStyleLong = workbook.createCellStyle();
            cellStyleLong.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			for(int i=0; i<items.size(); i++){
				currentRow =i+4;
				XSSFRow row = sheet.createRow(currentRow);
				row = sheet.getRow(currentRow);
				XSSFCell cell_idservicio = row.createCell(0);
				cell_idservicio = row.getCell(0);				
				cell_idservicio.setCellValue(items.get(i).getIdServicioSalud());				
				
				XSSFCell cell_servicio = row.createCell(1);
				cell_servicio = row.getCell(1);
				cell_servicio.setCellValue(items.get(i).getServicioSalud());
				
				XSSFCell cell_idcomuna = row.createCell(2);
				cell_idcomuna = row.getCell(2);
				cell_idcomuna.setCellValue(items.get(i).getCodigoEstablecimiento());
				
				XSSFCell cell_comuna = row.createCell(3);
				cell_comuna = row.getCell(3);
				cell_comuna.setCellValue(items.get(i).getEstablecimiento());
				
				if(items.get(i).getTotalS21()!=null){
					XSSFCell cell_ts21 = row.createCell(4);
					cell_ts21 = row.getCell(4);
					cell_ts21.setCellValue(items.get(i).getTotalS21());
					cell_ts21.setCellStyle(cellStyleLong);
					s21=true;
					totalS21 += items.get(i).getTotalS21();
				}
				if(s21 && items.get(i).getTotalS22()!=null){
					XSSFCell cell_ts22 = row.createCell(5);
					cell_ts22 = row.getCell(5);
					cell_ts22.setCellValue(items.get(i).getTotalS22());
					cell_ts22.setCellStyle(cellStyleLong);
					s22=true;
					totalS22 += items.get(i).getTotalS22();
				}
				if(!s21 && items.get(i).getTotalS22()!=null){
					XSSFCell cell_ts22 = row.createCell(4);
					cell_ts22 = row.getCell(4);
					cell_ts22.setCellValue(items.get(i).getTotalS22());
					cell_ts22.setCellStyle(cellStyleLong);
					s22=true;
					totalS22 += items.get(i).getTotalS22();
				}
				if(s21 && s22 && items.get(i).getTotalS29()!=null){
					XSSFCell cell_ts29 = row.createCell(6);
					cell_ts29 = row.getCell(6);
					cell_ts29.setCellValue(items.get(i).getTotalS29());
					cell_ts29.setCellStyle(cellStyleLong);
					s29=true;
					totalS29 += items.get(i).getTotalS29();
				}
				if((!s21 || !s22) && items.get(i).getTotalS29()!=null){
					XSSFCell cell_ts29 = row.createCell(5);
					cell_ts29 = row.getCell(5);
					cell_ts29.setCellValue(items.get(i).getTotalS29());
					cell_ts29.setCellStyle(cellStyleLong);
					s29=true;
					totalS29 += items.get(i).getTotalS29();
				}
				if(!s21 && !s22 && items.get(i).getTotalS29()!=null){
					XSSFCell cell_ts29 = row.createCell(4);
					cell_ts29 = row.getCell(4);
					cell_ts29.setCellValue(items.get(i).getTotalS29());
					cell_ts29.setCellStyle(cellStyleLong);
					s29=true;
					totalS29 += items.get(i).getTotalS29();
				}
				
				currentRow++;
			}
			XSSFRow row = sheet.createRow(currentRow);
			row = sheet.getRow(currentRow);
			
			XSSFCell cell_idservicio = row.createCell(1);
			cell_idservicio = row.getCell(1);				
			cell_idservicio.setCellValue("TOTALES ($)");	
			
			if(s21){
				XSSFCell cell_t = row.createCell(4);
				cell_t = row.getCell(4);
				cell_t.setCellValue(totalS21);
				cell_t.setCellStyle(cellStyleLong);
			}
			if(!s21 && s22){
				XSSFCell cell_t = row.createCell(4);
				cell_t = row.getCell(4);
				cell_t.setCellValue(totalS22);
				cell_t.setCellStyle(cellStyleLong);
			}
			if(s21 && s22){
				XSSFCell cell_t = row.createCell(5);
				cell_t = row.getCell(5);
				cell_t.setCellValue(totalS22);
				cell_t.setCellStyle(cellStyleLong);
			}
			if(s21 && s22 && s29){
				XSSFCell cell_t = row.createCell(6);
				cell_t = row.getCell(6);
				cell_t.setCellValue(totalS29);
				cell_t.setCellStyle(cellStyleLong);
			}
			if((!s21 || !s22) && s29){
				XSSFCell cell_t = row.createCell(5);
				cell_t = row.getCell(5);
				cell_t.setCellValue(totalS29);
				cell_t.setCellStyle(cellStyleLong);
			}
			if(!s21 && !s22 && s29){
				XSSFCell cell_t = row.createCell(4);
				cell_t = row.getCell(4);
				cell_t.setCellValue(totalS29);
				cell_t.setCellStyle(cellStyleLong);
			}
		}
	}

	private void addSheet(ProgramaAPSDetallesServiciosHistoricosSheetExcel excelSheet, String sheetName){
		XSSFSheet sheet = workbook.createSheet(sheetName);
		List<CellExcelVO> header = excelSheet.getHeaderComplex();
		List<CellExcelVO> subHeader = excelSheet.getSubHeadeComplex();
		CellStyle style = workbook.createCellStyle();
		style.setFillBackgroundColor(IndexedColors.BLUE.getIndex());
		style.setFillPattern(CellStyle.ALIGN_CENTER);
		Font font = workbook.createFont();
		font.setColor(IndexedColors.WHITE.getIndex());
		style.setFont(font);
		int currentRow = 0;
		int maxRowSpan = 0;
		int maxColSpan = 0;
		int addRow = 0;
		if(header != null && header.size() > 0){
			int contador_cols = 0;
			for(CellExcelVO cellExcelVO : header){
				if(cellExcelVO.getRowSpan() > maxRowSpan){
					maxRowSpan = cellExcelVO.getRowSpan();
				}
				if(cellExcelVO.getRowSpan() == maxRowSpan){
					maxColSpan += cellExcelVO.getColSpan();
				}else{
					addRow++;
					if(cellExcelVO.getColSpan() > contador_cols){
						contador_cols = cellExcelVO.getColSpan();
					}
					if(addRow == maxRowSpan){
						maxColSpan += contador_cols;
						contador_cols=0;
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
			int columnas_max = 0;
			int count_colums = 0;
			for(CellExcelVO cellExcelVO : header){
				if(cellExcelVO.getColSpan() > columnas_max){
					columnas_max =  cellExcelVO.getColSpan();
				}
				if(maxRowSpan == cellExcelVO.getRowSpan()){
					appendRow = 0;
					XSSFRow row = sheet.getRow(rowsReplace) ;
					XSSFCell cell = row.getCell(colsReplace);
					cell.setCellValue(cellExcelVO.getName());
					colsReplace += cellExcelVO.getColSpan();
					sheet.addMergedRegion(new CellRangeAddress(rowsReplace, (cellExcelVO.getRowSpan() - 1), colsReplace - cellExcelVO.getColSpan(), colsReplace -1 ));
				}else{
					count_colums += cellExcelVO.getColSpan();
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
			int contador_cols = 0;
			  for(CellExcelVO cellExcelVO : subHeader){
			        if(cellExcelVO.getRowSpan() > maxRowSpan){
			            maxRowSpan = cellExcelVO.getRowSpan();
			        }
			    }
			    for(CellExcelVO cellExcelVO : subHeader){
			        if(cellExcelVO.getRowSpan() == maxRowSpan){
			            maxColSpan += cellExcelVO.getColSpan();
			        }else{
			            addRow++;
			            if(addRow != maxRowSpan){
			                maxColSpan += cellExcelVO.getColSpan();
			                cols = cellExcelVO.getColSpan();
			                contador_cols = 0;
			            }else{
			                contador_cols += cellExcelVO.getColSpan();
			                if(contador_cols == cols){
			                    addRow = 0;
			                }else{
			                    addRow--;
			                }
			            }
			        }
			    }
			
			    currentRow = 2;
			    int totalRow = currentRow + maxRowSpan;
			    for( ; currentRow < totalRow ; currentRow++){
			        XSSFRow row = sheet.createRow(currentRow);
			        for(int currentCol= 0; currentCol < maxColSpan; currentCol++){
			            XSSFCell cell = row.createCell(currentCol);
			            cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			            cell.setCellStyle(style);
			        }
			    }
			    currentRow = 2;
			    int currentRowTmp = currentRow;
			    int currentCol = 0, lengthColTmp = 0;
			    int contadorColumnas = 0;
			    addRow=0;
			    for(CellExcelVO cellExcelVO : subHeader){
			        if(maxRowSpan == cellExcelVO.getRowSpan()){
			            XSSFRow row = sheet.getRow(currentRow) ;
			            XSSFCell cell = row.getCell(currentCol);
			            cell.setCellValue(cellExcelVO.getName());
			            sheet.addMergedRegion(new CellRangeAddress(currentRow, (cellExcelVO.getRowSpan()==1) ? currentRow : ((currentRow + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			            if(cellExcelVO.getColSpan()==1){
			                currentCol +=1;
			            }else{
			                currentCol += cellExcelVO.getColSpan();
			            }
			        }else{
			            addRow++;
			            if(addRow != maxRowSpan){
			                XSSFRow row = sheet.getRow(currentRowTmp) ;
			                XSSFCell cell = row.getCell(currentCol);
			                cell.setCellValue(cellExcelVO.getName());
			                sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			                lengthColTmp = cellExcelVO.getColSpan();
			                currentRowTmp++;
			                
			            }else{
			                contadorColumnas += cellExcelVO.getColSpan();
			                if(lengthColTmp == contadorColumnas){
			                    XSSFRow row = sheet.getRow(currentRowTmp) ;
			                    XSSFCell cell = row.getCell(currentCol++);
			                    cell.setCellValue(cellExcelVO.getName());
			                    sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			                    contadorColumnas = 0;
			                    addRow=0;
			                    currentRowTmp=currentRow;
			                    
			                }else{
			                    XSSFRow row = sheet.getRow(currentRowTmp) ;
			                    XSSFCell cell = row.getCell(currentCol++);
			                    cell.setCellValue(cellExcelVO.getName());
			                    sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			                    addRow--;
			                }
			            }
			        }
			}
			for(int columnPosition = 0; columnPosition <= maxColSpan; columnPosition++) {
				sheet.autoSizeColumn((short) (columnPosition));
			}
		}
	}
    
>>>>>>> programasReforzamiento
	private void addSheet(ProgramaAPSServicioSheetExcel excelSheet, String sheetName){
		XSSFSheet sheet = workbook.createSheet(sheetName);
		List<CellExcelVO> header = excelSheet.getHeaderComplex();
		List<CellExcelVO> subHeader = excelSheet.getSubHeadeComplex();
		List<ProgramaAPSServicioVO> items = excelSheet.getItems();
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
			int contador_cols = 0;
			for(CellExcelVO cellExcelVO : header){
				if(cellExcelVO.getRowSpan() > maxRowSpan){
					maxRowSpan = cellExcelVO.getRowSpan();
				}
				if(cellExcelVO.getRowSpan() == maxRowSpan){
					maxColSpan += cellExcelVO.getColSpan();
				}else{
					addRow++;
					if(cellExcelVO.getColSpan() > contador_cols){
						contador_cols = cellExcelVO.getColSpan();
					}
					if(addRow == maxRowSpan){
						maxColSpan += contador_cols;
						contador_cols=0;
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
			int columnas_max = 0;
			int count_colums = 0;
			for(CellExcelVO cellExcelVO : header){
				if(cellExcelVO.getColSpan() > columnas_max){
					columnas_max =  cellExcelVO.getColSpan();
				}
				if(maxRowSpan == cellExcelVO.getRowSpan()){
					appendRow = 0;
					XSSFRow row = sheet.getRow(rowsReplace) ;
					XSSFCell cell = row.getCell(colsReplace);
					cell.setCellValue(cellExcelVO.getName());
					colsReplace += cellExcelVO.getColSpan();
					sheet.addMergedRegion(new CellRangeAddress(rowsReplace, (cellExcelVO.getRowSpan() - 1), colsReplace - cellExcelVO.getColSpan(), colsReplace -1 ));
				}else{
					count_colums += cellExcelVO.getColSpan();
					XSSFRow row = sheet.getRow(appendRow) ;	
					XSSFCell cell = row.getCell(colsReplace);
					cell.setCellValue(cellExcelVO.getName());
					System.out.println("CellRangeAddress("+appendRow+","+appendRow+","+colsReplace+","+(colsReplace + cellExcelVO.getColSpan()-1)+")");
					sheet.addMergedRegion(new CellRangeAddress(appendRow, appendRow, colsReplace, (colsReplace + cellExcelVO.getColSpan()-1)));
					if(columnas_max == count_colums){
						appendRow++;
						count_colums=0;
					}else{
						colsReplace += cellExcelVO.getColSpan();
					}
				}
			}
		}
		if(subHeader != null && subHeader.size() > 0){
			int rows = 0;
			int cols = 0;
			addRow = 0;
			maxColSpan = 0;
			int contador_cols = 0;
<<<<<<< HEAD
			for(CellExcelVO cellExcelVO : subHeader){
				if(cellExcelVO.getRowSpan() > maxRowSpan){
					maxRowSpan = cellExcelVO.getRowSpan();
				}
			}
			for(CellExcelVO cellExcelVO : subHeader){
				if(cellExcelVO.getRowSpan() == maxRowSpan){
					maxColSpan += cellExcelVO.getColSpan();
				}else{
					addRow++;
					if(addRow != maxRowSpan){
						maxColSpan += cellExcelVO.getColSpan();
						cols = cellExcelVO.getColSpan();
						contador_cols = 0;
					}else{
						contador_cols += cellExcelVO.getColSpan();
						if(contador_cols == cols){
							addRow = 0;
						}else{
							addRow--;
						}
					}
				}
			}

			currentRow = 2;
			int totalRow = currentRow + maxRowSpan;
			for( ; currentRow < totalRow ; currentRow++){
				XSSFRow row = sheet.createRow(currentRow);
				for(int currentCol= 0; currentCol <= maxColSpan; currentCol++){
					XSSFCell cell = row.createCell(currentCol);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					CellStyle cellStyle = workbook.createCellStyle();
					cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
					cell.setCellStyle(cellStyle);
				}
			}
			currentRow = 2;
			int currentRowTmp = currentRow;
			int currentCol = 0, lengthColTmp = 0;
			int contadorColumnas = 0;
			addRow=0;
			for(CellExcelVO cellExcelVO : subHeader){
				if(maxRowSpan == cellExcelVO.getRowSpan()){
					XSSFRow row = sheet.getRow(currentRow) ;
					XSSFCell cell = row.getCell(currentCol);
					cell.setCellValue(cellExcelVO.getName());
					sheet.addMergedRegion(new CellRangeAddress(currentRow, (cellExcelVO.getRowSpan()==1) ? currentRow : ((currentRow + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
					if(cellExcelVO.getColSpan()==1){
						currentCol +=1;
					}else{
						currentCol += cellExcelVO.getColSpan();
					}
				}else{
					addRow++;
					if(addRow != maxRowSpan){
						XSSFRow row = sheet.getRow(currentRowTmp) ;
						XSSFCell cell = row.getCell(currentCol);
						cell.setCellValue(cellExcelVO.getName());
						sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
						lengthColTmp = cellExcelVO.getColSpan();
						currentRowTmp++;

					}else{
						contadorColumnas += cellExcelVO.getColSpan();
						if(lengthColTmp == contadorColumnas){
							XSSFRow row = sheet.getRow(currentRowTmp) ;
							XSSFCell cell = row.getCell(currentCol++);
							cell.setCellValue(cellExcelVO.getName());
							sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
							contadorColumnas = 0;
							addRow=0;
							currentRowTmp=currentRow;

						}else{
							XSSFRow row = sheet.getRow(currentRowTmp) ;
							XSSFCell cell = row.getCell(currentCol++);
							cell.setCellValue(cellExcelVO.getName());
							sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
							addRow--;
						}
					}
				}
=======
			  for(CellExcelVO cellExcelVO : subHeader){
			        if(cellExcelVO.getRowSpan() > maxRowSpan){
			            maxRowSpan = cellExcelVO.getRowSpan();
			        }
			    }
			    for(CellExcelVO cellExcelVO : subHeader){
			        if(cellExcelVO.getRowSpan() == maxRowSpan){
			            maxColSpan += cellExcelVO.getColSpan();
			        }else{
			            addRow++;
			            if(addRow != maxRowSpan){
			                maxColSpan += cellExcelVO.getColSpan();
			                cols = cellExcelVO.getColSpan();
			                contador_cols = 0;
			            }else{
			                contador_cols += cellExcelVO.getColSpan();
			                if(contador_cols == cols){
			                    addRow = 0;
			                }else{
			                    addRow--;
			                }
			            }
			        }
			    }
			
			    currentRow = 2;
			    int totalRow = currentRow + maxRowSpan;
			    for( ; currentRow < totalRow ; currentRow++){
			        XSSFRow row = sheet.createRow(currentRow);
			        for(int currentCol= 0; currentCol < maxColSpan; currentCol++){
			            XSSFCell cell = row.createCell(currentCol);
			            cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			            CellStyle cellStyle = workbook.createCellStyle();
			            cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			            cell.setCellStyle(cellStyle);
			        }
			    }
			    currentRow = 2;
			    int currentRowTmp = currentRow;
			    int currentCol = 0, lengthColTmp = 0;
			    int contadorColumnas = 0;
			    addRow=0;
			    for(CellExcelVO cellExcelVO : subHeader){
			        if(maxRowSpan == cellExcelVO.getRowSpan()){
			            XSSFRow row = sheet.getRow(currentRow) ;
			            XSSFCell cell = row.getCell(currentCol);
			            cell.setCellValue(cellExcelVO.getName());
			            sheet.addMergedRegion(new CellRangeAddress(currentRow, (cellExcelVO.getRowSpan()==1) ? currentRow : ((currentRow + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			            if(cellExcelVO.getColSpan()==1){
			                currentCol +=1;
			            }else{
			                currentCol += cellExcelVO.getColSpan();
			            }
			        }else{
			            addRow++;
			            if(addRow != maxRowSpan){
			                XSSFRow row = sheet.getRow(currentRowTmp) ;
			                XSSFCell cell = row.getCell(currentCol);
			                cell.setCellValue(cellExcelVO.getName());
			                sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			                lengthColTmp = cellExcelVO.getColSpan();
			                currentRowTmp++;
			                
			            }else{
			                contadorColumnas += cellExcelVO.getColSpan();
			                if(lengthColTmp == contadorColumnas){
			                    XSSFRow row = sheet.getRow(currentRowTmp) ;
			                    XSSFCell cell = row.getCell(currentCol++);
			                    cell.setCellValue(cellExcelVO.getName());
			                    sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			                    contadorColumnas = 0;
			                    addRow=0;
			                    currentRowTmp=currentRow;
			                    
			                }else{
			                    XSSFRow row = sheet.getRow(currentRowTmp) ;
			                    XSSFCell cell = row.getCell(currentCol++);
			                    cell.setCellValue(cellExcelVO.getName());
			                    sheet.addMergedRegion(new CellRangeAddress(currentRowTmp, (cellExcelVO.getRowSpan()==1) ? currentRowTmp : ((currentRowTmp + cellExcelVO.getRowSpan())-1) , currentCol, (cellExcelVO.getColSpan()==1)?currentCol: ((currentCol + cellExcelVO.getColSpan()) - 1)));
			                    addRow--;
			                }
			            }
			        }
>>>>>>> programasReforzamiento
			}
			for(int columnPosition = 0; columnPosition <= maxColSpan; columnPosition++) {
				sheet.autoSizeColumn((short) (columnPosition));
			}

			for(int i=0; i<items.size(); i++){
				currentRow =i+4;
				XSSFRow row = sheet.createRow(currentRow);
				row = sheet.getRow(currentRow);
				XSSFCell cell_idservicio = row.createCell(0);
				cell_idservicio = row.getCell(0);				
				cell_idservicio.setCellValue(items.get(i).getIdServicioSalud());				

				XSSFCell cell_servicio = row.createCell(1);
				cell_servicio = row.getCell(1);
				cell_servicio.setCellValue(items.get(i).getServicioSalud());

				XSSFCell cell_idcomuna = row.createCell(2);
				cell_idcomuna = row.getCell(2);
				cell_idcomuna.setCellValue(items.get(i).getCodigo());

				XSSFCell cell_comuna = row.createCell(3);
				cell_comuna = row.getCell(3);
				cell_comuna.setCellValue(items.get(i).getEstablecimiento());

				currentRow++;
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
		if(items != null){
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

	private void addSheet(CrearPlanillaCumplimientoMunicialProgramaSheetExcel excelSheet, String sheetName){
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
System.out.println("sheetName="+sheetName);
		List<CellExcelVO> header = excelSheet.getHeaderComplex();
		List<CellExcelVO> subHeader = excelSheet.getSubHeadeComplex();
		List<CumplimientoApsMunicipalProgramaVO> item = excelSheet.getItems();
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

		XSSFFont fontHeader = workbook.createFont();
		fontHeader.setColor(IndexedColors.BLACK.getIndex());
		fontHeader.setBold(true);
		CellStyle cellStyleHeader = workbook.createCellStyle();
		cellStyleHeader.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		cellStyleHeader.setFont(fontHeader);
		
		int maxColumnHeader = 0;
		boolean fisrtTime = true;
		int countCol = 0;
		int maxColumnTmp = 0;
		int maxRowHeader = 2;
		for(CellExcelVO cellExcelVO : header){
			System.out.println("header cellExcelVO="+cellExcelVO);
			if(cellExcelVO.getRowSpan() == maxRowHeader){
				maxColumnHeader += cellExcelVO.getColSpan();
			}else{
				if(fisrtTime){
					maxColumnHeader += cellExcelVO.getColSpan();
					maxColumnTmp += cellExcelVO.getColSpan();
					fisrtTime = false;
				}else{
					countCol += cellExcelVO.getColSpan();
					if(countCol == maxColumnTmp){
						fisrtTime = true;
						maxColumnTmp = 0;
						countCol = 0;
					}
				}
			}
		}
		
		System.out.println("maxRowHeader="+maxRowHeader);
		System.out.println("maxColumnHeader="+maxColumnHeader);
		for(int fila = 0; fila < maxRowHeader; fila++){
			XSSFRow row = sheet.createRow(fila);
			for(int columna = 0; columna < maxColumnHeader; columna++){
				XSSFCell cell = row.createCell(columna);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(cellStyleHeader);
			}
		}
		int posComponente = 4;
		for(int i=0; i<header.size(); i++){
			if(i == 0){
				sheet.setDefaultColumnWidth((short) 20); 
				XSSFRow row = sheet.getRow(0);
				CellExcelVO cellExcelVO = header.get(i); 
				XSSFCell cell = row.getCell(0);
				cell.setCellStyle(cellStyleHeader);
				cell.setCellValue(cellExcelVO.getName());
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 1));
			}else if(i == 1){
				sheet.setDefaultColumnWidth((short) 20); 
				XSSFRow row = sheet.getRow(0);
				CellExcelVO cellExcelVO = header.get(i); 
				XSSFCell cell = row.getCell(2);
				cell.setCellStyle(cellStyleHeader);
				cell.setCellValue(cellExcelVO.getName());
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 3));
			}
			if(i == 2){
				//nombre del programa
				sheet.setDefaultColumnWidth((short) 40); 
				XSSFRow row = sheet.getRow(0);
				CellExcelVO cellExcelVO = header.get(i); 
				XSSFCell cell = row.getCell(4);
				cell.setCellStyle(cellStyleHeader);
				cell.setCellValue(cellExcelVO.getName());
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, (4 + cellExcelVO.getColSpan() -1)));
			}

			if(i > 2){
				//componentes
				sheet.setDefaultColumnWidth((short) 45); 
				XSSFRow row = sheet.getRow(1);
				CellExcelVO cellExcelVO = header.get(i);
				System.out.println("cellExcelVO.getName()="+cellExcelVO.getName());
				System.out.println("cellExcelVO.getColSpan()="+cellExcelVO.getColSpan());
				System.out.println("posComponente="+posComponente);
				if(cellExcelVO.getColSpan() == 1){
					XSSFCell cell = row.getCell(posComponente);
					cell.setCellStyle(cellStyleHeader);
					cell.setCellValue(cellExcelVO.getName());
					posComponente++; 
				}else{
					XSSFCell cell = row.getCell(posComponente);
					cell.setCellStyle(cellStyleHeader);
					cell.setCellValue(cellExcelVO.getName());
					sheet.addMergedRegion(new CellRangeAddress(1, 1, posComponente, (posComponente + cellExcelVO.getColSpan() -1)));
					posComponente = posComponente + cellExcelVO.getColSpan();
				}
			}
		}
		//##### Sub-Headers ########

		XSSFRow row = sheet.createRow(2);
		row = sheet.getRow(2);
		for(int i=0;i<subHeader.size();i++){        	
			CellExcelVO cellExcelVO = subHeader.get(i);
			XSSFCell cell = row.createCell(i);
			cell = row.getCell(i);
			cell.setCellStyle(cellStyleHeader);
			cell.setCellValue(cellExcelVO.getName());        	
		}


		//########### items ##########
		for(int i=0;i<item.size();i++){
			int fila = i+3;
			row = sheet.createRow(fila);
			row = sheet.getRow(fila);
			CumplimientoApsMunicipalProgramaVO cumplimiento = item.get(i);

			XSSFCell cellIdServicio = row.createCell(0);
			//XSSFCell.CELL_TYPE_STRING
			cellIdServicio.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
			cellIdServicio = row.getCell(0);
			cellIdServicio.setCellStyle(cellStyleHeader);
			cellIdServicio.setCellValue(cumplimiento.getIdServicio());

			XSSFCell cellServicio = row.createCell(1);
			cellServicio.setCellType(XSSFCell.CELL_TYPE_STRING);
			cellServicio = row.getCell(1);
			cellServicio.setCellStyle(cellStyleHeader);
			cellServicio.setCellValue(cumplimiento.getServicio());

			XSSFCell cellIdComuna = row.createCell(2);
			cellIdComuna.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
			cellIdComuna = row.getCell(2);
			cellIdComuna.setCellStyle(cellStyleHeader);
			cellIdComuna.setCellValue(cumplimiento.getIdComuna());

			XSSFCell cellComuna = row.createCell(3);
			cellComuna.setCellType(XSSFCell.CELL_TYPE_STRING);
			cellComuna = row.getCell(3);
			cellComuna.setCellStyle(cellStyleHeader);
			cellComuna.setCellValue(cumplimiento.getComuna());


		}

	}



	//###################valorizacion cumplimiento ###########


	private void addSheet(PlanillaTrabajoCumplimientoReliquidacionMunicipalSheetExcel excelSheet, String sheetName){
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
		List<ValorizarReliquidacionSummaryVO> item = excelSheet.getItems();
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

		XSSFFont fontHeader = workbook.createFont();
		fontHeader.setColor(IndexedColors.BLACK.getIndex());
		fontHeader.setBold(true);
		CellStyle cellStyleHeader = workbook.createCellStyle();
		cellStyleHeader.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		cellStyleHeader.setFont(fontHeader);
		cellStyleHeader.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleHeader.setBorderBottom(CellStyle.BORDER_MEDIUM);
		cellStyleHeader.setBorderLeft(CellStyle.BORDER_MEDIUM);
		cellStyleHeader.setBorderRight(CellStyle.BORDER_MEDIUM);
		cellStyleHeader.setBorderTop(CellStyle.BORDER_MEDIUM);

		for(int fila = 0; fila < (4 + item.size()); fila++){
			 XSSFRow row = sheet.createRow(fila);
			for(int columna = 0; columna < header.get(2).getColSpan() + 5 ; columna++){
				XSSFCell cell = row.createCell(columna);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(cellStyleHeader);
			}
		}

		int posComponente = 4;
		for(int i=0; i < header.size() ;i++){
			if(i < 2){
				sheet.setDefaultColumnWidth((short) 25); 
				XSSFRow row = sheet.getRow(0);
				CellExcelVO cellExcelVO = header.get(i); 
				if(i == 0){
					XSSFCell cell = row.getCell(i);
					cell.setCellStyle(cellStyleHeader);
					cell.setCellValue(cellExcelVO.getName());
					sheet.addMergedRegion(new CellRangeAddress(0, 1, i, (i+1)));
				}else{
					XSSFCell cell = row.getCell(i+1);
					cell.setCellStyle(cellStyleHeader);
					cell.setCellValue(cellExcelVO.getName());
					sheet.addMergedRegion(new CellRangeAddress(0, 1, i+1, (i+2)));
				}
			}else if(i == 2){
				//nombre del programa
				sheet.setDefaultColumnWidth((short) 40); 
				XSSFRow row = sheet.getRow(0);
				CellExcelVO cellExcelVO = header.get(i); 
				XSSFCell cell = row.getCell(posComponente);
				cell.setCellStyle(cellStyleHeader);
				cell.setCellValue(cellExcelVO.getName());
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, (4 + cellExcelVO.getColSpan() - 1)));
			}else if(i > 2 && header.get(i).getName().equalsIgnoreCase("Marco Final")){
				//marco total
				sheet.setDefaultColumnWidth((short) 20); 
				XSSFRow row = sheet.getRow(0);
				CellExcelVO cellExcelVO = header.get(i);
				System.out.println("Marco final en columna --> "+(header.get(2).getColSpan() + 4) );
				XSSFCell cell = row.getCell(header.get(2).getColSpan()+4);
				cell.setCellStyle(cellStyleHeader);
				cell.setCellValue(cellExcelVO.getName());
				sheet.addMergedRegion(new CellRangeAddress(0, 3, header.get(2).getColSpan() + 4, header.get(2).getColSpan() + 4));
			}else{
				//componentes
				sheet.setDefaultColumnWidth((short) 20); 
				XSSFRow row = sheet.getRow(1);
				CellExcelVO cellExcelVO = header.get(i);
				System.out.println("cellExcelVO.getName()="+cellExcelVO.getName()+" posComponente="+posComponente);
				XSSFCell cell = row.getCell(posComponente);
				cell.setCellStyle(cellStyleHeader);
				cell.setCellValue(cellExcelVO.getName());
				sheet.addMergedRegion(new CellRangeAddress(1, 1, posComponente, posComponente+cellExcelVO.getColSpan()-1));
				posComponente += cellExcelVO.getColSpan();
			}
		}

		//#######################################
		//#################### SUBHEADERS #####################

		int posCol = 0;
		int posRow = 2;
		int posColTmp = posCol;
		int posRowTmp = posRow;
		int maxColsSpan = 1;
		boolean firstTime = true;
		int countCols = 0;
	 
		XSSFRow rowSubHeader = null;
		for(CellExcelVO cellExcelVO : subHeader){
			System.out.println("cellExcelVO.getRowSpan()="+cellExcelVO.getRowSpan() + " cellExcelVO.getName()="+cellExcelVO.getName());
			if(cellExcelVO.getRowSpan() == 2){
				rowSubHeader = sheet.getRow(posRow);
				XSSFCell cell = rowSubHeader.createCell(posCol);
				cell.setCellStyle(cellStyleHeader);
				cell.setCellValue(cellExcelVO.getName()); 
				sheet.addMergedRegion(new CellRangeAddress(posRow, (posRow + 1), posCol, (posCol + cellExcelVO.getColSpan()-1)));
				posCol+=cellExcelVO.getColSpan();
				posColTmp = posCol;
			}else{
				System.out.println("firstTime="+firstTime+" ");
				rowSubHeader = sheet.getRow(posRowTmp);
				if(firstTime){
					firstTime = false;
					maxColsSpan = cellExcelVO.getColSpan();
					XSSFCell cell = rowSubHeader.createCell(posCol);
					cell.setCellStyle(cellStyleHeader);
					cell.setCellValue(cellExcelVO.getName()); 
					sheet.addMergedRegion(new CellRangeAddress(posRow, posRow , posCol, (posCol + cellExcelVO.getColSpan()-1)));
					posRowTmp++;
				}else{
					countCols += cellExcelVO.getColSpan();
					if(countCols != maxColsSpan){
						XSSFCell cell = rowSubHeader.createCell(posColTmp);
						cell.setCellStyle(cellStyleHeader);
						cell.setCellValue(cellExcelVO.getName()); 
						sheet.addMergedRegion(new CellRangeAddress(posRowTmp, posRowTmp, posColTmp, (posColTmp + cellExcelVO.getColSpan()-1)));
					}else{
						XSSFCell cell = rowSubHeader.createCell(posColTmp);
						cell.setCellStyle(cellStyleHeader);
						cell.setCellValue(cellExcelVO.getName()); 
						sheet.addMergedRegion(new CellRangeAddress(posRowTmp, posRowTmp, posColTmp, (posColTmp + cellExcelVO.getColSpan()-1)));
						firstTime = true;
						countCols = 0;
						posRowTmp = posRow;
						posCol+=maxColsSpan;
					}
					posColTmp +=cellExcelVO.getColSpan();
				}
			}
		}
		// ############# items
		int filaItem = 4;
		for(int i=0;i<item.size();i++){
			int maxColum = item.get(i).getRow().size();
			XSSFRow rowItems = sheet.getRow(filaItem);
			for(int celda = 0; celda < maxColum; celda++ ){
				XSSFCell cellItems = rowItems.createCell(celda);
				cellItems.setCellValue(item.get(i).getRow().get(celda).toString());
			}
			filaItem++;
		}
	}
}