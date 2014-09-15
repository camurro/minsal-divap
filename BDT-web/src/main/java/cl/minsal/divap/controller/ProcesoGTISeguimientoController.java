package cl.minsal.divap.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.service.ConveniosService;
import minsal.divap.service.EmailService;
import minsal.divap.service.EmailService.Adjunto;

import org.primefaces.model.UploadedFile;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;



@Named ( "procesoGTISeguimientoController" ) 
@ViewScoped 
public class ProcesoGTISeguimientoController extends AbstractTaskMBean   implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3416230843287598037L;

	
	//
	@EJB 
	private EmailService emailService;
	private String paraSeleccionado;
	private String asuntoSeleccionado;
	private String mensajeSeleccionado;
	private String  cCSeleccionado;
	private String  cCOSeleccionado;
	private UploadedFile uploadedFile;
	private File file;
	
	@EJB
	private ConveniosService aPSConveniosService;
	@Override
	protected Map<String, Object> createResultData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String iniciarProceso() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void enviarCorreo() {
String mensaje = "";

ArrayList<String> to = new ArrayList<String>();
ArrayList<String> cc = new ArrayList<String>();
ArrayList<String> cco = new ArrayList<String>();

		if(paraSeleccionado != "" && asuntoSeleccionado != "" && mensajeSeleccionado != "" ){
			    String filename = uploadedFile.getFileName();
				byte[] contentAttachedFile = uploadedFile.getContents();
				Integer docAttachedFile = persistFile(filename,	contentAttachedFile);
					
			if(cCSeleccionado !=""){
				cc.add(cCSeleccionado);		
			}else{
				cc = null;	
			}
			if(cCOSeleccionado !=""){
			 cco.add(cCOSeleccionado);
			}else{
				cco = null;
			}
			List<Adjunto> adjuntos = new ArrayList<Adjunto>();
			mensaje ="Correo enviado";
			//adjuntos.
		    //uploadedFile.getFileName();
			FacesMessage msg = new FacesMessage(mensaje);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			//(List<String> to, List<String> cc, List<String> cco, String subject, String content, List<Adjunto> adjuntos)
			emailService.sendMail(to,cc, cco,asuntoSeleccionado, mensajeSeleccionado, adjuntos);
		}else{
				
			
				
				
				mensaje ="Los campos Para Asunto y Mensaje no pueden ir en blanco";
				FacesMessage msg = new FacesMessage(mensaje);
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}		
		
		

		
	}

	
	
	
	
	
	
	
	
	
	
	
	
	public String getParaSeleccionado() {
		return paraSeleccionado;
	}

	public void setParaSeleccionado(String paraSeleccionado) {
		this.paraSeleccionado = paraSeleccionado;
	}

	public String getAsuntoSeleccionado() {
		return asuntoSeleccionado;
	}

	public void setAsuntoSeleccionado(String asuntoSeleccionado) {
		this.asuntoSeleccionado = asuntoSeleccionado;
	}

	public String getMensajeSeleccionado() {
		return mensajeSeleccionado;
	}

	public void setMensajeSeleccionado(String mensajeSeleccionado) {
		this.mensajeSeleccionado = mensajeSeleccionado;
	}

	public String getcCSeleccionado() {
		return cCSeleccionado;
	}

	public void setcCSeleccionado(String cCSeleccionado) {
		this.cCSeleccionado = cCSeleccionado;
	}

	public String getcCOSeleccionado() {
		return cCOSeleccionado;
	}

	public void setcCOSeleccionado(String cCOSeleccionado) {
		this.cCOSeleccionado = cCOSeleccionado;
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}
	
	
	
	
	
	
	
	
	
}
