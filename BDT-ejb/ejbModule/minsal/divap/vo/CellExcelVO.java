package minsal.divap.vo;

import java.io.Serializable;

public class CellExcelVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3606998161454607624L;
	private String name;
	// columnas que ocupará el dato
	private int colSpan;
	//filas que ocupará el dato
	private int rowSpan;
	private int posx;
	private int posy;
	
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

	public int getPosx() {
		return posx;
	}

	public void setPosx(int posx) {
		this.posx = posx;
	}

	public int getPosy() {
		return posy;
	}

	public void setPosy(int posy) {
		this.posy = posy;
	}

	@Override
	public String toString() {
		return "CellExcelVO [name=" + name + ", colSpan=" + colSpan
				+ ", rowSpan=" + rowSpan + ", posx=" + posx + ", posy=" + posy
				+ "]";
	}

}
