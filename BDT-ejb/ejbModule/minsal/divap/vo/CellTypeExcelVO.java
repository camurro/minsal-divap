package minsal.divap.vo;

import java.io.Serializable;

import minsal.divap.enums.FieldType;


public class CellTypeExcelVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7762994976281819918L;
	
	private Boolean required = true;
	private FieldType type;

	public CellTypeExcelVO(Boolean required) {
		this.required = required;
		this.type = FieldType.STRINGFIELD;
	}
	
	public CellTypeExcelVO(Boolean required, FieldType type) {
		this.required = required;
		this.type = type;
	}
	
	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public FieldType getType() {
		return type;
	}

	public void setType(FieldType type) {
		this.type = type;
	}

}
