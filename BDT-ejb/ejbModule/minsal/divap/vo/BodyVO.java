package minsal.divap.vo;

import java.io.Serializable;

public class BodyVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2830484597004172509L;
	private String nodeRef;
	private String fileName;
	private StatusVO status;
	
	public String getNodeRef() {
		return nodeRef;
	}
	
	public void setNodeRef(String nodeRef) {
		this.nodeRef = nodeRef;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public StatusVO getStatus() {
		return status;
	}
	
	public void setStatus(StatusVO status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "BodyVO [nodeRef=" + nodeRef + ", fileName=" + fileName
				+ ", status=" + status + ", getStatus()=" + getStatus() + "]";
	}
	
}
