package minsal.divap.excel.interfaces;

import java.util.List;

public abstract class ExcelTemplate<T>{

	private Integer offsetRows = 0;
	private Integer offsetColumns = 0;
	private List<String> headers = null;

	private List<T> items;

	public abstract List<List<Object>> getDataList();

	public ExcelTemplate(List<String> headers, List<T> items){
		this.headers = headers;
		this.items = items;
	}

	public ExcelTemplate(List<String> headers, List<T> items, Integer offsetRows, Integer offsetColumns){
		this.headers = headers;
		this.items = items;
		this.offsetRows = offsetRows;
		this.offsetColumns = offsetColumns;
	}
	
	public Integer getOffsetRows() {
		return offsetRows;
	}

	public void setOffsetRows(Integer offsetRows) {
		this.offsetRows = offsetRows;
	}

	public Integer getOffsetColumns() {
		return offsetColumns;
	}

	public void setOffsetColumns(Integer offsetColumns) {
		this.offsetColumns = offsetColumns;
	}

	public List<String> getHeaders() {
		return headers;
	}

	public void setHeaders(List<String> headers) {
		this.headers = headers;
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

}
