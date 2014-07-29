package minsal.divap.excel.impl;

import java.util.List;

import minsal.divap.excel.interfaces.ExcelValidator;
import minsal.divap.exception.ExcelFormatException;
import minsal.divap.vo.CellTypeExcelVO;
import minsal.divap.vo.CumplimientoVO;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class RebajaExcelValidator extends ExcelValidator<CumplimientoVO>{
        
        public RebajaExcelValidator(Integer numberField, List<CellTypeExcelVO> cells) {
                super(numberField, cells);
        }

        public RebajaExcelValidator(Integer numberField, List<CellTypeExcelVO> cells, Boolean omitHeader) {
                super(numberField, cells, omitHeader);
        }

        public RebajaExcelValidator(Integer numberField, List<CellTypeExcelVO> cells, Integer offsetColumns, Integer offsetRows) {
                super(numberField, cells);
                super.setOffsetColumns(offsetColumns);
                super.setOffsetRows(offsetRows);
        }

        public RebajaExcelValidator(Integer numberField, List<CellTypeExcelVO> cells, Boolean omitHeader, Integer offsetColumns, Integer offsetRows) {
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
                        if(!empty(xssfRow)){
                                if(!validateTypes(xssfRow)){
                                        throw new ExcelFormatException("Los datos de la fila " + first + " no son válidos ");
                                }
                                add(convert());
                        }
                }
        }

        @Override
        protected CumplimientoVO convert() {
                CumplimientoVO cumVO = new CumplimientoVO();
                if(getValues().size() == getCells().size()){
                        if(!"".equals(getValues().get(2))){
                                Double id_comuna = Double.parseDouble(getValues().get(2));
                                cumVO.setId_comuna(id_comuna.intValue());
                        }
                        if(!"".equals(getValues().get(4))){
                                Double value_item1 = Double.parseDouble(getValues().get(4));
                                cumVO.setValue_item1(value_item1.intValue());
                        }
                        if(!"".equals(getValues().get(5))){
                                Double value_item2 = Double.parseDouble(getValues().get(5));
                                cumVO.setValue_item2(value_item2.intValue());
                        }
                        if(!"".equals(getValues().get(6))){
                                Double value_item3 = Double.parseDouble(getValues().get(6));
                                cumVO.setValue_item3(value_item3.intValue());
                        }
                
                }
                return cumVO;
        }
        
}
