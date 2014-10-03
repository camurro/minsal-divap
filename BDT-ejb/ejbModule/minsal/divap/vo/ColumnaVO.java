package minsal.divap.vo;

import java.io.Serializable;

public class ColumnaVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6029640700623154615L;
	
	private String header;
	private String property;
	private String nombreColumna;

	public ColumnaVO() {

	}
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
	
	@Override
	public String toString() {
		return "ColumnaVO [header=" + header + ", property=" + property
				+ ", nombreColumna=" + nombreColumna + "]";
	}

}
