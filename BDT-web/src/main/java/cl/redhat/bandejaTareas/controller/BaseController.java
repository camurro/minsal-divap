package cl.redhat.bandejaTareas.controller;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import minsal.divap.service.DocumentService;
import minsal.divap.vo.DocumentoVO;

import org.apache.log4j.Logger;
import org.primefaces.model.UploadedFile;



//import cl.redhat.bandejaTareas.model.OirsRol;
//import cl.redhat.bandejaTareas.model.OirsUsuario;
// import cl.redhat.bandejaTareas.exceptions.BusinessException;
// import cl.redhat.bandejaTareas.model.OirsRol;
// import cl.redhat.bandejaTareas.model.OirsUsuario;
// import cl.redhat.bandejaTareas.model.RolUsuario;
// import cl.redhat.bandejaTareas.service.SeguridadOirsServiceRemote;
import cl.redhat.bandejaTareas.util.UserUtil;

public abstract class BaseController {

	private static Logger log = Logger.getLogger(BaseController.class);

	@Inject 
	private UserUtil userUtil;
	@Inject 
	protected FacesContext facesContext;
	private DocumentoVO documento;
	@EJB
	protected DocumentService documentService;

	public BaseController(){
	}

	protected boolean sessionExpired(){
		boolean expired = false;
		if (!getSessionBean().isLogged()) {
			log.warn("No hay usuario almacenado en sesion, se redirecciona a pantalla de login");
			expired = true;
			try {
				facesContext.getExternalContext().redirect("login.jsf");
			} catch (IOException e) {
				log.error(
						"Error tratando de redireccionar a login por falta de usuario en sesion.",
						e);
			}
		}
		return expired;
	}

	public String getLoggedUsername() {
		if (userUtil != null) return userUtil.getUsername();
		else {
			log.warn("No user logged, returning empty username");
			return "";
		}
	}

	public UserUtil getSessionBean() {
		if (userUtil == null) userUtil = new UserUtil();
		return userUtil;
	}

	public FacesContext getFacesContex() {
		return facesContext;
	}


	/*public boolean verificarRol( OirsUsuario usu, String rol ) {
		OirsRol temp = new OirsRol();
		for (OirsRol o : usu.getOirsRoles()) {
			temp = (OirsRol) o;
			if (temp.getRoleDescripcionRol().equals(rol)) return true;
		}
		return false;
	}*/

	public boolean verificarPermiso( String tarea ) {

		if (userUtil != null && userUtil.isLogged()) {
			//			OirsUsuario usu = null;
			try {
				//				usu = seguridadOirsService.getUsuarioByName(userUtil.getUsername());
			} catch (Exception e) { //BusinessException
				e.printStackTrace();
			}
			//			if (usu != null) {
			if (tarea.equals("MIS TAREAS")) {
				//					if (!verificarRol(usu, RolUsuario.admin.name())) // cualquiera que no sea admin
				return true;
				//					else return false;
			}

			if (tarea.equals("TAREAS DISPONIBLES")) {
				//					if (!verificarRol(usu, RolUsuario.admin.name())// cualquiera que no sea admin
				//									&& !verificarRol(usu, RolUsuario.grOirsEspecialistas.name())) // cualquiera que no sea especialista
				return true;
				//					else return false;
			}

			if (tarea.equals("REPORTES")) {
				//					if (!verificarRol(usu, RolUsuario.admin.name()) && !verificarRol(usu, RolUsuario.grOirsEspecialistas.name())) // solamente
				// admin
				return true;
				//					else return false;
			}

			if (tarea.equals("INGRESAR SOLICITUD")) {
				//					if (!verificarRol(usu, RolUsuario.grOirsEspecialistas.name())) // cualquiera que no sea especialistas
				return true;
				//					else return false;
			}

			if (tarea.equals("MANTENEDORES")) {
				//					if (verificarRol(usu, RolUsuario.admin.name())) // solamente admin
				return true;
				//					else return false;
			}
		}
		return true;
		//		}
		//		return false;
	}

	@SuppressWarnings("unchecked")
	public <T> T getFromSession(String name, Class<T> clazz){
		HttpSession sc = (HttpSession)
				FacesContext.getCurrentInstance().getExternalContext()
				.getSession(false);
		return (T) sc.getAttribute(name);
	}

	public void setOnSession(String name, Object object) {
		HttpSession sc = (HttpSession)
				FacesContext.getCurrentInstance().getExternalContext()
				.getSession(false);
		sc.setAttribute(name, object);
	}


	public void downloadDocument(){
		byte[] content = this.documento.getContent();
		String contentType = this.documento.getContentType();
		HttpServletResponse response = (HttpServletResponse)
				FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.setContentType(contentType);
		response.setCharacterEncoding("UTF-8");
		response.addHeader("Content-Disposition", "attachment; filename=" + 
				this.documento.getName());
		try {
			response.getOutputStream().write(content);
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (IOException e) {
			log.error(e);
		}
		FacesContext.getCurrentInstance().responseComplete();
	}

	public Integer persistFile(UploadedFile file) {
		return documentService.uploadTemporalFile(file.getFileName(), file.getContents());
	}

	public Integer persistFile(String filename, byte[] contents) {
		return documentService.uploadTemporalFile(filename, contents);
	}

	public File createTemporalFile(String filename, byte[] contents) {
		return documentService.createTemporalFile(filename, contents);
	}

	public DocumentoVO getDocumento()
	{
		return this.documento;
	}

	public void setDocumento(DocumentoVO documento) {
		this.documento = documento;
	}

	public Integer compareDatesUsingCalendar(Date firstDate, Date lastDate) {
		Integer compare = null;
		Calendar firstCal = Calendar.getInstance();
		Calendar lastCal = Calendar.getInstance();

		if( (firstDate == null) && (lastDate == null) ){
			return 0;
		}

		if( (firstDate != null) && (lastDate == null) ){
			return 1;
		}

		if( (firstDate == null) && (lastDate != null) ){
			return -1;
		}

		firstCal.setTime(firstDate);
		firstCal.set(Calendar.HOUR_OF_DAY, 0);
		firstCal.set(Calendar.MINUTE, 0);
		firstCal.set(Calendar.SECOND, 0);
		firstCal.set(Calendar.MILLISECOND, 0);
		lastCal.setTime(lastDate);


		//how to check if two dates are equals in java using Calendar
		if (firstCal.equals(lastCal)) {
			compare = 0;
		}

		//how to check if one date comes before another using Calendar
		else if (firstCal.before(lastCal)) {
			compare = -1;
		}

		//how to check if one date comes after another using Calendar
		else if (firstCal.after(lastCal)) {
			compare = 1;
		}
		return compare;
	}

	@SuppressWarnings("unchecked")
	protected <T> T getBean(String name, Class<T> type){
		return 	(T) FacesContext.getCurrentInstance()
				.getApplication()
				.getExpressionFactory()
				.createValueExpression(
						FacesContext.getCurrentInstance().getELContext(), 
						"#{" + name + "}", type)
						.getValue(FacesContext.getCurrentInstance().getELContext());
	}
	
	public String getRequestParameter(String requestParameter) {
        return (String) FacesContext.getCurrentInstance().getExternalContext()
            .getRequestParameterMap().get(requestParameter);
    }

}
