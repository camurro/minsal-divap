package minsal.divap.vo;

import java.io.Serializable;

public class DocumentSummaryVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9150637771113401871L;
	private Integer idDocumento;
	private String descDocumento;
	public Integer getIdDocumento() {
		return idDocumento;
	}
	public void setIdDocumento(Integer idDocumento) {
		this.idDocumento = idDocumento;
	}
	public String getDescDocumento() {
		return descDocumento;
	}
	public void setDescDocumento(String descDocumento) {
		this.descDocumento = descDocumento;
	}
	
	
}
