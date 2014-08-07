package minsal.divap.vo;

import java.io.Serializable;
import java.util.Date;

public class SeguimientoVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4323497217202488275L;
	private Date date;
	private String to;
	private String cc;
	private String cco;
	private String attached;
	private String subject;
	private String body;
	
	public SeguimientoVO(){
		
	}
	
	public SeguimientoVO(Date date, String to, String cc, String cco,
			String attached, String subject, String body) {
		super();
		this.date = date;
		this.to = to;
		this.cc = cc;
		this.cco = cco;
		this.attached = attached;
		this.subject = subject;
		this.body = body;
	}



	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	public String getTo() {
		return to;
	}
	
	public void setTo(String to) {
		this.to = to;
	}
	
	public String getCc() {
		return cc;
	}
	
	public void setCc(String cc) {
		this.cc = cc;
	}
	
	public String getAttached() {
		return attached;
	}
	
	public void setAttached(String attached) {
		this.attached = attached;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getBody() {
		return body;
	}
	
	public void setBody(String body) {
		this.body = body;
	}

	public String getCco() {
		return cco;
	}

	public void setCco(String cco) {
		this.cco = cco;
	}

	@Override
	public String toString() {
		return "SeguimientoVO [date=" + date + ", to=" + to + ", cc=" + cc
				+ ", cco=" + cco + ", attached=" + attached + ", subject="
				+ subject + ", body=" + body + "]";
	}

}
