package minsal.divap.vo;

import java.io.Serializable;

public class ReferenciaDocumentoSummaryVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -433212556646785639L;
	private Integer id;
	private String path;
	private String nodeRef;
	
	
	public ReferenciaDocumentoSummaryVO(Integer id, String path, String nodeRef) {
		super();
		this.id = id;
		this.path = path;
		this.nodeRef = nodeRef;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getNodeRef() {
		return nodeRef;
	}
	public void setNodeRef(String nodeRef) {
		this.nodeRef = nodeRef;
	}
	
}
