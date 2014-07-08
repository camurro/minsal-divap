package cl.redhat.bandejaTareas.controller;

import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.log4j.Logger;

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
	
	@Inject private UserUtil userUtil;
	@Inject FacesContext facesContext;
	
	// @EJB(lookup =
	// "java:global/FormOIRS-ear/FormOIRS-ejb-0.1/SeguridadOirsServiceBean!cl.redhat.bandejaTareas.service.SeguridadOirsServiceRemote")
	// private SeguridadOirsServiceRemote seguridadOirsService;
	
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
	
}
