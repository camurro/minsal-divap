package minsal.divap.vo;

import java.io.Serializable;

public class ConvenioDocumentoVO  implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8784037809787513482L;
	private Integer id;				
	private String nombreDocu;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNombreDocu() {
		return nombreDocu;
	}
	public void setNombreDocu(String nombreDocu) {
		this.nombreDocu = nombreDocu;
	}
	
	

}
