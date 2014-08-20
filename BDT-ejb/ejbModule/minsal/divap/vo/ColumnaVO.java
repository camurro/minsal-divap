package minsal.divap.vo;

import java.io.Serializable;

public class ColumnaVO implements Serializable {

	
	
		 
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String header;
	    private String property;
	    private String nombreColumna;
	    
	    public ColumnaVO(String header, String property, String nombreColumna) {
	        this.header = header;
	        this.property = property;
	        this.nombreColumna = nombreColumna;
	    }

	    public String getHeader() {
	        return header;
	    }

	    public String getProperty() {
	        return property;
	    }

		public String getNombreColumna() {
			return nombreColumna;
		}

		public void setNombreColumna(String nombreColumna) {
			this.nombreColumna = nombreColumna;
		}
	
}