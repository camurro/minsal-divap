package minsal.divap.vo;

import java.io.Serializable;

public class TaskStatusVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2746859311486228917L;
	
	private Integer id;
	private String status;
	private String description;
	
	public TaskStatusVO(Integer id, String status, String description) {
		super();
		this.id = id;
		this.status = status;
		this.description = description;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}	
