package minsal.divap.vo;

import java.io.Serializable;

public class EmailVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1351480641602239153L;
	
	private String to = "camurro@gmail.com";
	private String subject;
	private String content;
	
	public EmailVO(){
		
	}

	public EmailVO(String subject, String to, String content) {
		super();
		this.subject = subject;
		this.to = to;
		this.content = content;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
