package minsal.divap.excel;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import minsal.divap.excel.impl.CrearPlanillaCumplimientoMunicialProgramaSheetExcel;
import minsal.divap.excel.impl.EstimacionFlujoCajaConsolidadorSheetExcel;
import minsal.divap.excel.impl.EstimacionFlujoCajaSubtituloSheetExcel;
import minsal.divap.excel.impl.PlanillaTrabajoCumplimientoReliquidacionMunicipalSheetExcel;
import minsal.divap.excel.impl.ProgramaAPSMunicipalesSheetExcel;
import minsal.divap.excel.impl.RebajaCalculadaSheetExcel;
import minsal.divap.excel.impl.RebajaSheetExcel;
import minsal.divap.excel.interfaces.ExcelTemplate;
import minsal.divap.vo.CajaMontoSummaryVO;
import minsal.divap.vo.CellExcelVO;
import minsal.divap.vo.CumplimientoApsMunicipalProgramaVO;
import minsal.divap.vo.ResumenConsolidadorVO;
import minsal.divap.vo.SubtituloFlujoCajaVO;
import minsal.divap.vo.ValorizarReliquidacionSummaryVO;

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
        
        for(int fila = 0; fila < 2; fila++){
        	 XSSFRow row = null;
        	 row = sheet.createRow(fila);
        	 for(int columna = 0; columna < (header.get(2).getColSpan() + 4 ); columna++){
        		 XSSFCell cell = row.createCell(columna);
                 cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                 cell.setCellStyle(cellStyleHeader);
        	 }
        }
        
        int maxColSpan = 0;
        int maxRowSpan = 0;
        int posComponente = 4;
        
        
        for(int i=0;i<header.size();i++){
        	
        	if(i == 0){
        		
        		sheet.setDefaultColumnWidth((short) 20); 
        		XSSFRow row = sheet.getRow(0);
        		CellExcelVO cellExcelVO = header.get(i); 
        		XSSFCell cell = row.getCell(0);
            	cell.setCellStyle(cellStyleHeader);
            	cell.setCellValue(cellExcelVO.getName());
            	sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 1));
        	}
        	else if(i == 1){
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
        		XSSFCell cell = row.getCell(posComponente);
        		cell.setCellStyle(cellStyleHeader);
            	cell.setCellValue(cellExcelVO.getName());
            	posComponente++;            	
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
       	 XSSFRow row = null;
       	 row = sheet.createRow(fila);
       	 for(int columna = 0; columna < header.get(2).getColSpan() + 5 ; columna++){
       		 XSSFCell cell = row.createCell(columna);
             cell.setCellType(XSSFCell.CELL_TYPE_STRING);
             cell.setCellStyle(cellStyleHeader);
       	 }
       }
        
        int posComponente = 4;
        int posComp2 = 4;
        for(int i=0;i<header.size();i++){

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
            	//posComponente = posComponente + cellExcelVO.getColSpan() -1 ;
        	}else{
        		//componentes
        		sheet.setDefaultColumnWidth((short) 20); 
        		XSSFRow row = sheet.getRow(1);
        		CellExcelVO cellExcelVO = header.get(i);
        		XSSFCell cell = row.getCell(posComponente);
        		cell.setCellStyle(cellStyleHeader);
            	cell.setCellValue(cellExcelVO.getName());
            	sheet.addMergedRegion(new CellRangeAddress(1, 1, posComponente, posComponente+cellExcelVO.getColSpan()-1));
            	posComponente = posComponente+cellExcelVO.getColSpan();
        	}
        }

        //#######################################
    	//#################### SUBHEADERS #####################
    	XSSFCell cell = null;
    	XSSFRow row = sheet.createRow(2);
        row = sheet.getRow(2);
        XSSFRow row3 = sheet.createRow(3);
        int totalComponentes = header.size() -4;
        
        
        int posConvenio = 4;
        int posPrimeraCuota = 5;
        int posSegundaCuota = 7;
        int posReliquidacion = 9;
        
        int posPorcentajeCuota1 = 5;
        int posMontoCuota1 = 6;
        int posPorcentajeCuota2 = 7;
        int posMontoCuota2 = 8;
        int posPorcentajeCumplimiento = 9;
        int posPorcentajeReliquidacion = 10;
        int posDescuentoSegundaCuota = 11;
        int posMontoFinal2daCuota = 12;
        
        int p = 0;
        for(int subh=0;subh<subHeader.size();subh++){ 
        	
        	
        	if(subh >= 0 && subh < 4){
        		//crea las celdas hasta comuna        		
	        	CellExcelVO cellExcelVO = subHeader.get(subh);
	        	cell = row.createCell(subh);
	        	cell = row.getCell(subh);
	           	cell.setCellStyle(cellStyleHeader);
	           	cell.setCellValue(cellExcelVO.getName()); 
	           	sheet.addMergedRegion(new CellRangeAddress(2, 3, subh, subh));
        	}
        	
        	if(subh >= 4){
        		for(int c = 0; c < totalComponentes; c++){
        			if(subHeader.get(subh).getName().equalsIgnoreCase("Convenio")){
        				if(posConvenio > (header.get(2).getColSpan() + 4)){
        					break;
        				}else{
        					//crea celda convenio
            				CellExcelVO cellExcelVO = subHeader.get(subh);
            	        	cell = row.createCell(posConvenio);
            	        	cell = row.getCell(posConvenio);
            	           	cell.setCellStyle(cellStyleHeader);
            	           	cell.setCellValue(cellExcelVO.getName()); 
            	           	sheet.addMergedRegion(new CellRangeAddress(2, 3, posConvenio, posConvenio));
            	           	posConvenio = posConvenio+9;
        				}
        				
        			}
        			else if(subHeader.get(subh).getName().equalsIgnoreCase("Primera cuota")){
        				if(posPrimeraCuota > (header.get(2).getColSpan() + 4)){
        					break;
        				}else{
        					//crea celda 1ra cuota
            				CellExcelVO cellExcelVO = subHeader.get(subh);
            	        	cell = row.createCell(posPrimeraCuota);
            	        	cell = row.getCell(posPrimeraCuota);
            	           	cell.setCellStyle(cellStyleHeader);
            	           	cell.setCellValue(cellExcelVO.getName()); 
            	           	sheet.addMergedRegion(new CellRangeAddress(2, 2, posPrimeraCuota, (posPrimeraCuota +1)));
            	           	posPrimeraCuota = posPrimeraCuota+9;
        				}
        				
        			}
        			else if(subHeader.get(subh).getName().equalsIgnoreCase("Segunda cuota")){
        				if(posSegundaCuota > (header.get(2).getColSpan() + 4)){
        					break;
        				}else{
        					CellExcelVO cellExcelVO = subHeader.get(subh);
            	        	cell = row.createCell(posSegundaCuota);
            	        	cell = row.getCell(posSegundaCuota);
            	           	cell.setCellStyle(cellStyleHeader);
            	           	cell.setCellValue(cellExcelVO.getName()); 
            	           	sheet.addMergedRegion(new CellRangeAddress(2, 2, posSegundaCuota, posSegundaCuota +1));
            	           	posSegundaCuota = posSegundaCuota+9;
        				}
        				
        			}
        			else if(subHeader.get(subh).getName().equalsIgnoreCase("Reliquidación")){
        				if(posReliquidacion > (header.get(2).getColSpan() + 4)){
        					break;
        				}else{
        					CellExcelVO cellExcelVO = subHeader.get(subh);
            	        	cell = row.createCell(posReliquidacion);
            	        	cell = row.getCell(posReliquidacion);
            	           	cell.setCellStyle(cellStyleHeader);
            	           	cell.setCellValue(cellExcelVO.getName()); 
            	           	sheet.addMergedRegion(new CellRangeAddress(2, 2, posReliquidacion, posReliquidacion +3));
            	           	posReliquidacion = posReliquidacion + 9;
        				}
        				
        	           	
        			}
        			
        			// #########   SUB-HEADERS DE LOS PORCENTAJES ###########
        			
        			

        			else if(subHeader.get(subh).getName().equalsIgnoreCase("%_pc")){
        				
        				System.out.println("debiera ser el porcentaje de la 1ra cuota ---> "+subHeader.get(subh).getName());
        				
        				if(posPorcentajeCuota1 > (header.get(2).getColSpan() + 4)){
        					break;
        				}else{
        					row = sheet.createRow(3);
            				sheet.setDefaultColumnWidth((short) 10); 
            				System.out.println("Porcentaje 1ra cuota en columna: "+posPorcentajeCuota1);
            				CellExcelVO cellExcelVO = subHeader.get(subh);
            				System.out.println("celda porcentaje 1ra cuota --> "+cellExcelVO.getName());
            	        	cell = row.createCell(posPorcentajeCuota1);
            	        	cell = row.getCell(posPorcentajeCuota1);
            	           	cell.setCellStyle(cellStyleHeader);
            	           	cell.setCellValue("%"); 
            	           	posPorcentajeCuota1 = posPorcentajeCuota1 + 9;
        				}
        				
        			}
        			else if(subHeader.get(subh).getName().equalsIgnoreCase("Monto_pc")){

        				if(posMontoCuota1 > (header.get(2).getColSpan() + 4)){
        					break;
        				}else{
        					sheet.setDefaultColumnWidth((short) 20); 
            				CellExcelVO cellExcelVO = subHeader.get(subh);
            	        	cell = row.createCell(posMontoCuota1);
            	        	cell = row.getCell(posMontoCuota1);
            	           	cell.setCellStyle(cellStyleHeader);
            	           	cell.setCellValue("Monto");
            	           	posMontoCuota1 = posMontoCuota1 + 9;
        				}
        			}
//        			else if(subHeader.get(subh).getName().equalsIgnoreCase("%") && subHeader.get(subh +2).getName().equalsIgnoreCase("% Cumplimiento")){
        			else if(subHeader.get(subh).getName().equalsIgnoreCase("%_sc")){
        				//porcentaje 2da cuota
        				
        				System.out.println("debiera ser el porcentaje de la 2da cuota ---> "+subHeader.get(subh).getName());
        				if(posPorcentajeCuota2 > (header.get(2).getColSpan() + 4)){
        					break;
        				}else{
        					sheet.setDefaultColumnWidth((short) 10); 
            				CellExcelVO cellExcelVO = subHeader.get(subh);
            	        	cell = row.createCell(posPorcentajeCuota2);
            	        	cell = row.getCell(posPorcentajeCuota2);
            	           	cell.setCellStyle(cellStyleHeader);
            	           	cell.setCellValue("%");
            	           	posPorcentajeCuota2 = posPorcentajeCuota2 + 9;
        				}
        			}
        			else if(subHeader.get(subh).getName().equalsIgnoreCase("Monto_sc")){
        				//monto segunda cuota
        				if(posMontoCuota2 > (header.get(2).getColSpan() + 4)){
        					break;
        				}else{
        					sheet.setDefaultColumnWidth((short) 20); 
            				CellExcelVO cellExcelVO = subHeader.get(subh);
            	        	cell = row.createCell(posMontoCuota2);
            	        	cell = row.getCell(posMontoCuota2);
            	           	cell.setCellStyle(cellStyleHeader);
            	           	cell.setCellValue("Monto");
            	           	posMontoCuota2 = posMontoCuota2 + 9;
        				}        				
        			}
        			else if(subHeader.get(subh).getName().equalsIgnoreCase("% Cumplimiento")){
        				// % cumplimiento
        				if(posPorcentajeCumplimiento > (header.get(2).getColSpan() + 4)){
        					break;
        				}else{
        					sheet.setDefaultColumnWidth((short) 20); 
            				CellExcelVO cellExcelVO = subHeader.get(subh);
            	        	cell = row.createCell(posPorcentajeCumplimiento);
            	        	cell = row.getCell(posPorcentajeCumplimiento);
            	           	cell.setCellStyle(cellStyleHeader);
            	           	cell.setCellValue(cellExcelVO.getName());
            	           	posPorcentajeCumplimiento = posPorcentajeCumplimiento + 9;
        				}
        			}
        			else if(subHeader.get(subh).getName().equalsIgnoreCase("% Reliquidación")){
        				//% Reliquidación
        				if(posPorcentajeReliquidacion > (header.get(2).getColSpan() + 4)){
        					break;
        				}else{
        					sheet.setDefaultColumnWidth((short) 15); 
            				CellExcelVO cellExcelVO = subHeader.get(subh);
            	        	cell = row.createCell(posPorcentajeReliquidacion);
            	        	cell = row.getCell(posPorcentajeReliquidacion);
            	           	cell.setCellStyle(cellStyleHeader);
            	           	cell.setCellValue(cellExcelVO.getName());
            	           	posPorcentajeReliquidacion = posPorcentajeReliquidacion + 9;
        				}
        			}
        			else if(subHeader.get(subh).getName().equalsIgnoreCase("% Descuento Segunda Cuota")){
        				//% Descuento Segunda Cuota
        				if(posDescuentoSegundaCuota > (header.get(2).getColSpan() + 4)){
        					break;
        				}else{
        					sheet.setDefaultColumnWidth((short) 30); 
            				CellExcelVO cellExcelVO = subHeader.get(subh);
            	        	cell = row.createCell(posDescuentoSegundaCuota);
            	        	cell = row.getCell(posDescuentoSegundaCuota);
            	           	cell.setCellStyle(cellStyleHeader);
            	           	cell.setCellValue(cellExcelVO.getName());
            	           	posDescuentoSegundaCuota = posDescuentoSegundaCuota + 9;
        				}
        			}
        			else if(subHeader.get(subh).getName().equalsIgnoreCase("% Monto Final Segunda Cuota")){
        				//% Monto Final Segunda Cuota
        				if(posMontoFinal2daCuota > (header.get(2).getColSpan() + 4)){
        					break;
        				}else{
        					sheet.setDefaultColumnWidth((short) 30); 
            				CellExcelVO cellExcelVO = subHeader.get(subh);
            	        	cell = row.createCell(posMontoFinal2daCuota);
            	        	cell = row.getCell(posMontoFinal2daCuota);
            	           	cell.setCellStyle(cellStyleHeader);
            	           	cell.setCellValue(cellExcelVO.getName());
            	           	posMontoFinal2daCuota = posMontoFinal2daCuota + 9;
        				}
        			}
        		}
        	}
        	p++;
         }
        
        
        // ############# items
        int filaItem = 4;
        
        
        for(int i=0;i<item.size();i++){
        	int maxColum = item.get(i).getRow().size();
        	XSSFRow rowItems = sheet.getRow(filaItem);
        	XSSFCell cellItems = null;        	
        	
        	for(int celda = 0; celda < maxColum; celda++ ){
        		
        		cellItems = row.createCell(celda);
        		cellItems = rowItems.createCell(celda);
        		cellItems = rowItems.getCell(celda);
        		cellItems.setCellValue(item.get(i).getRow().get(celda).toString());
        	}
        	filaItem++;
        }
    }
}