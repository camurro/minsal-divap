package minsal.divap.excel.interfaces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExcelDinamicoVO implements Serializable {
	 
    /**
    *
     */
    private static final long serialVersionUID = 1L;
    List<Object> listaColumnas;
    public List<Object> getListaColumnas() {
          return listaColumnas;
    }
    public void setListaColumnas(List<Object> listaColumnas) {
          this.listaColumnas = listaColumnas;
    }
   
    public List<Object> getRow() {
          List<Object> row = new ArrayList<Object>();
         
          for (Object object : listaColumnas) {
                 row.add(object);
          }
         
          return row;
    }
}