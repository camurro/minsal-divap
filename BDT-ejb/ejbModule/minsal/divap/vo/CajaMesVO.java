package minsal.divap.vo;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CajaMesVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4150553509677562402L;
	private Long monto;
	private String mes;
	
	
	public Long getMonto() {
		return monto;
	}
	public void setMonto(Long monto) {
		this.monto = monto;
	}
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	@Override
	public String toString() {
		return "CajaMesVO [monto=" + monto + ", mes=" + mes + "]";
	}
	
	public List<Object> getRow() {
		List<Object> row = new ArrayList<Object>();
		if(getMes() != null){
			row.add(getMes());
		}
		if(getMonto() != null){
			row.add(getMonto());
		}
		
		return row;
	}
	
	
	
}
