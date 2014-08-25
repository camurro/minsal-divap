package minsal.divap.vo;

import java.io.Serializable;

public class CellExcelVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3606998161454607624L;
	String name;
	int colSpan;
	int rowSpan;
	
	public CellExcelVO(String name) {
		this(name, 1, 1) ;
	}
	
	public CellExcelVO(String name, int colSpan, int rowSpan) {
		super();
		this.name = name;
		this.colSpan = colSpan;
		this.rowSpan = rowSpan;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getColSpan() {
		return colSpan;
	}
	public void setColSpan(int colSpan) {
		this.colSpan = colSpan;
	}
	public int getRowSpan() {
		return rowSpan;
	}
	public void setRowSpan(int rowSpan) {
		this.rowSpan = rowSpan;
	}
	
	
}
