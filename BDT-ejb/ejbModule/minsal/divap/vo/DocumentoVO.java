package minsal.divap.vo;

import java.io.Serializable;

public class DocumentoVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4729083931345720572L;
	
	private String name;
	private String contentType;
	private byte[] content;

	public DocumentoVO(String name, String contentType, byte[] content)
	{
		this.name = name;
		this.contentType = contentType;
		this.content = content;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContentType() {
		return this.contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public byte[] getContent() {
		return this.content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}
}
