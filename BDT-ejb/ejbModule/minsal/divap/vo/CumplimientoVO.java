package minsal.divap.vo;

import java.io.Serializable;

public class CumplimientoVO implements Serializable{


	private static final long serialVersionUID = 1958331277587353226L;

	private Integer id_comuna;
	private String desc_item1;
	private String desc_item2;
	private String desc_item3;
	private Double value_item1;
	private Double value_item2;
	private Double value_item3;
	public Integer getId_comuna() {
		return id_comuna;
	}
	public void setId_comuna(Integer id_comuna) {
		this.id_comuna = id_comuna;
	}
	public String getDesc_item1() {
		return desc_item1;
	}
	public void setDesc_item1(String desc_item1) {
		this.desc_item1 = desc_item1;
	}
	public String getDesc_item2() {
		return desc_item2;
	}
	public void setDesc_item2(String desc_item2) {
		this.desc_item2 = desc_item2;
	}
	public String getDesc_item3() {
		return desc_item3;
	}
	public void setDesc_item3(String desc_item3) {
		this.desc_item3 = desc_item3;
	}
	public Double getValue_item1() {
		return value_item1;
	}
	public void setValue_item1(Double value_item1) {
		this.value_item1 = value_item1;
	}
	public Double getValue_item2() {
		return value_item2;
	}
	public void setValue_item2(Double value_item2) {
		this.value_item2 = value_item2;
	}
	public Double getValue_item3() {
		return value_item3;
	}
	public void setValue_item3(Double value_item3) {
		this.value_item3 = value_item3;
	}
	
	
	
	
}
