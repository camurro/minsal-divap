package minsal.divap.vo;

import java.io.Serializable;

public class BodyCreateFolderVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2830484597004172509L;
	private String nodeRef;
	private String site;
	private String container;
	
	public String getNodeRef() {
		return nodeRef;
	}
	
	public void setNodeRef(String nodeRef) {
		this.nodeRef = nodeRef;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getContainer() {
		return container;
	}

	public void setContainer(String container) {
		this.container = container;
	}

	@Override
	public String toString() {
		return "BodyCreateFolderVO [nodeRef=" + nodeRef + ", site=" + site
				+ ", container=" + container + "]";
	}
	
}
