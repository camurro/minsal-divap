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
import minsal.divap.excel.impl.ProgramaAPSServicioSheetExcel;
import minsal.divap.excel.impl.RebajaCalculadaSheetExcel;
import minsal.divap.excel.impl.RebajaSheetExcel;
import minsal.divap.excel.interfaces.ExcelTemplate;
import minsal.divap.vo.CajaMontoSummaryVO;
import minsal.divap.vo.CellExcelVO;
import minsal.divap.vo.CumplimientoApsMunicipalProgramaVO;
import minsal.divap.vo.ProgramaAPSServicioVO;
import minsal.divap.vo.ProgramaAPSVO;
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

            if(excelSheet instanceof ProgramaAPSServicioSheetExcel){
                addSheet((ProgramaAPSServicioSheetExcel)excelSheet, sheetName);
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
                cell_idcomuna.setCellValue(items.get(i).getIdComuna());

                XSSFCell cell_comuna = row.createCell(3);
                cell_comuna = row.getCell(3);
                cell_comuna.setCellValue(items.get(i).getComuna());

                currentRow++;
            }
        }
    }

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