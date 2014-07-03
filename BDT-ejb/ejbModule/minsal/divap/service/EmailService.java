package minsal.divap.service;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;

import minsal.divap.vo.EmailVO;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

@Stateless
public class EmailService {

	@Resource(name="smtpHost")
	private String smtpHost;

	@Resource(name="smtpPort")
	private Integer smtpPort;

	@Resource(name="smtpSSL")
	private Boolean smtpSSL;

	@Resource(name="smtpUsername")
	private String smtpUsername;

	@Resource(name="smtpPassword")
	private String smtpPassword;

	@Resource(name="mailFrom")
	private String mailFrom;

	public static class Adjunto implements Serializable{
		private static final long serialVersionUID = -1629629251519813940L;
		private String name;
		private String descripcion;
		private URL url;

		public String getName(){
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescripcion() {
			return this.descripcion;
		}

		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
		}

		public URL getUrl() {
			return this.url;
		}

		public void setUrl(URL url) {
			this.url = url;
		}
	}

	public void sendMail(EmailVO email){
		sendMail(email.getTo(), email.getSubject(), email.getContent(), new ArrayList<Adjunto>());
	}

	public void sendMail(String to, String subject, String content, List<Adjunto> adjuntos){
		ArrayList<String> forAll = new ArrayList<String>();
		forAll.add(to);
		sendMail(forAll, subject, content, adjuntos);
	}

	public void sendMail(List<String> to, String subject, String content){
		sendMail(to, subject, content, new ArrayList<Adjunto>());
	}

	public void sendMail(String rut, String subject, String content){
		sendMail(rut, subject, content, 
				new ArrayList<Adjunto>());
	}

	public void sendMail(List<String> to, String subject, String content, List<Adjunto> adjuntos){
		if (adjuntos == null)
			adjuntos = new ArrayList<Adjunto>();
		HtmlEmail email = new HtmlEmail();
		try {
			email.setHostName(this.smtpHost);
			email.setAuthentication(this.smtpUsername, this.smtpPassword);
			email.setSmtpPort(this.smtpPort.intValue());
			email.setFrom(this.mailFrom);
			for (String mailTo : to) {
				email.addTo(mailTo);
			}
			email.setSubject(subject);
			email.setSSL(this.smtpSSL.booleanValue());
			email.setHtmlMsg(content);
			email.setDebug(false);
			for (Adjunto adjunto : adjuntos) {
				EmailAttachment attachment = new EmailAttachment();
				attachment.setDescription(adjunto.getDescripcion());
				attachment.setName(adjunto.getName());
				attachment.setURL(adjunto.getUrl());
				email.attach(attachment);
			}
			email.send();
			System.out.println("email enviado");
		}
		catch (EmailException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
