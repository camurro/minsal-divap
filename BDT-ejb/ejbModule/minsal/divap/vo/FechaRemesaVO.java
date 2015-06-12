package minsal.divap.vo;

import java.io.Serializable;

public class FechaRemesaVO implements Serializable{

	private static final long serialVersionUID = -2580158910040561651L;
	
	private Integer id;
	private Integer dia;
	
	public FechaRemesaVO(){
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDia() {
		return dia;
	}

	public void setDia(Integer dia) {
		this.dia = dia;
	}

	@Override
	public String toString() {
		return dia.toString();
	}
	

}
