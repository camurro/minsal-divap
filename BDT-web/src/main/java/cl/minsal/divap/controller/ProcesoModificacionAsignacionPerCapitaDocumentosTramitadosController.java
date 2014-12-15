package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named("procesoModificacionAsignacionPerCapitaDocumentosTramitadosController")
@ViewScoped
public class ProcesoModificacionAsignacionPerCapitaDocumentosTramitadosController extends AbstractTaskMBean
		implements Serializable {
	private static final long serialVersionUID = 8979055329731411696L;
	@Inject
	private transient Logger log;
	@Inject
	FacesContext facesContext;
	
	private UploadedFile decretoFirma;
	private UploadedFile resolucionArica;
	private UploadedFile resolucionTarapaca;
	private UploadedFile resolucionMetropolitana;
	
	public UploadedFile getDecretoFirma() {
		return decretoFirma;
	}

	public void setDecretoFirma(UploadedFile decretoFirma) {
		this.decretoFirma = decretoFirma;
	}

	public UploadedFile getResolucionArica() {
		return resolucionArica;
	}

	public void setResolucionArica(UploadedFile resolucionArica) {
		this.resolucionArica = resolucionArica;
	}

	public UploadedFile getResolucionTarapaca() {
		return resolucionTarapaca;
	}

	public void setResolucionTarapaca(UploadedFile resolucionTarapaca) {
		this.resolucionTarapaca = resolucionTarapaca;
	}

	public UploadedFile getResolucionMetropolitana() {
		return resolucionMetropolitana;
	}

	public void setResolucionMetropolitana(UploadedFile resolucionMetropolitana) {
		this.resolucionMetropolitana = resolucionMetropolitana;
	}

	public void uploadArchivosValorizacion() {
		if (resolucionArica != null && decretoFirma != null && resolucionTarapaca != null && resolucionMetropolitana != null) {
			FacesMessage msg = new FacesMessage(
					"Los archivos fueron cargados correctamente.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}


	@PostConstruct
	public void init() {
		log.info("ProcesoAsignacionPerCapitaDocumentosTramitadosController Alcanzado.");
		//ejb.testMethod();
		if (!getSessionBean().isLogged()) {
			log.warn("No hay usuario almacenado en sesion, se redirecciona a pantalla de login");
			try {
				facesContext.getExternalContext().redirect("login.jsf");
			} catch (IOException e) {
				log.error(
						"Error tratando de redireccionar a login por falta de usuario en sesion.",
						e);
			}
		}

	}

	public void handleFileUpload(FileUploadEvent event) {
		FacesMessage msg = new FacesMessage("Succesful", event.getFile()
				.getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		System.out.println("createResultData usuario-->"+getSessionBean().getUsername());
		parameters.put("usuario", getSessionBean().getUsername());
		return parameters;
	}

	@Override
	public String iniciarProceso() {
		return null;
	}
	
}
