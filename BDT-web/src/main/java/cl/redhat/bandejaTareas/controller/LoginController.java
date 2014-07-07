package cl.redhat.bandejaTareas.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import minsal.divap.EJBTest;

import org.apache.log4j.Logger;


// import cl.redhat.bandejaTareas.brms.client.BRMSClient;
// import cl.redhat.bandejaTareas.brms.client.BRMSClientImpl;
// import cl.redhat.bandejaTareas.brms.client.artifacts.responses.GETRoleCheckResponse;
// import cl.redhat.bandejaTareas.exceptions.BusinessException;
// import cl.redhat.bandejaTareas.model.OirsRol;
//import cl.redhat.bandejaTareas.model.OirsSeguridad;
//import cl.redhat.bandejaTareas.model.OirsUsuario;
// import cl.redhat.bandejaTareas.model.OirsUsuario;
// import cl.redhat.bandejaTareas.service.SeguridadOirsServiceRemote;
import cl.redhat.bandejaTareas.util.BandejaProperties;

@Named("loginController")
@ViewScoped
public class LoginController extends BaseController implements Serializable {

	private static final long serialVersionUID = -8180634775574337540L;

	private static Logger log = Logger.getLogger(LoginController.class);

	private String usuario = "admin";
	private String contrasena = "admin";

	// private BRMSClient client;
	//
	//private OirsSeguridad oirsSeguridad;

    @Inject
	private EJBTest ejbtest;

	@Inject
	private BandejaProperties bandejaProperties;

	@Inject
	private FacesContext facesContext;

	// @EJB(lookup =
	// "java:global/FormOIRS-ear/FormOIRS-ejb-0.1/SeguridadOirsServiceBean!cl.redhat.bandejaTareas.service.SeguridadOirsServiceRemote")
	// private SeguridadOirsServiceRemote seguridadOirsService;

	@PostConstruct
	public void init() {
		try {
			//setOirsSeguridad(new OirsSeguridad());
			//ejbtest.testMethod();
			// setClient(new BRMSClientImpl(bandejaProperties.getHostBRMS(),
			// bandejaProperties.getPortBRMS()));
		} catch (Exception e) {
			log.warn(e);
		}

	}

	public void logout() {
		try {
			// client.logout();
			// if (getOirsSeguridad().getIdSeguridad() != null)
			// seguridadOirsService.eliminarComprobacion(getOirsSeguridad());
			getSessionBean().logout();
			String contextPath = facesContext.getExternalContext()
					.getRequestContextPath();
			facesContext.getExternalContext().redirect(
					contextPath + "/login.jsf");
		} catch (Exception e1) {
			log.error("Se ha detectado un error al tratar de cerrar la sesi�n",
					e1);
		}
	}

	public String login() {
		try {
			// if (!seguridadOirsService.comprobarUsuario(usuario, contrasena))
			// {
			Boolean inicioSessionDummy = usuario.equals("admin")
					&& contrasena.equals("admin");
			if (!inicioSessionDummy) {
				facesContext.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "",
						"No existe usuario con el usuario y contrase\u00F1a"));

				return "";
			}
			// if(client.isLogged()) client.logout();
			//
			// try {
			// client.login(usuario, contrasena);
			// verificarRol();
			// } catch (Exception e) {
			// facesContext.addMessage(null, new FacesMessage(
			// FacesMessage.SEVERITY_ERROR, "", e.getMessage()));
			// return "";
			// }

			// GETProcessDefinitionsResponse resp =
			// client.getProcessDefinitions();
			//
			// // checkear si viene cl.redhat.bandejaTareas.process.Solicitud
			// boolean processActive = false;
			// for (ProcessDefinition pd : resp.getDefinitions()) {
			// if
			// ("cl.redhat.bandejaTareas.process.Solicitud".equals(pd.getId()))
			// processActive = true;
			// }
			//
			// if (!processActive)
			// throw new BusinessException(
			// "No se encuentra declarado el proceso cl.redhat.bandejaTareas.process.Solicitud en BRMS");

			// String token = seguridadOirsService.generacionToken(usuario);
			// OirsUsuario user =
			// seguridadOirsService.getUsuarioByName(usuario);
			// OirsSeguridad oirsSeguridad =
			// seguridadOirsService.guardarComprobacion(usuario, token);

			//OirsUsuario user = new OirsUsuario();
			//user.setActive(true);

			boolean isAdmin = true;
			boolean isCentr = true;
			boolean isEspec = true;

			//
			// boolean isAdmin = false;
			// boolean isCentr = false;
			// boolean isEspec = false;
			//
			// for (OirsRol rol : user.getOirsRoles()) {
			// if(rol.getRoleIdRol().equals(1L)){
			// isAdmin = true;
			// break;
			// }
			// if(rol.getRoleIdRol().equals(2L)){
			// isCentr = true;
			// break;
			// }
			// if(rol.getRoleIdRol().equals(3L)){
			// isEspec = true;
			// break;
			// }
			// }
			//
			//System.out.println("ID de Usuario: "+user.getUsuaIdUsuario());
			guardarUserUtil(8L, isAdmin,
					isCentr, isEspec);
		} catch (Exception e) {
			facesContext.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "", e.getMessage()));
			return "";
		}

		return "bandejaTareas?faces-redirect=true";
	}

	public void verificarRol() throws Exception {
		// OirsUsuario usu = null;
		// try {
		// usu = seguridadOirsService.getUsuarioByName(usuario);
		// } catch (BusinessException e) {
		// e.printStackTrace();
		// }
		// List<String> listaRol = new ArrayList<String>();
		// OirsRol objectSet = new OirsRol();
		//
		// if (usu != null){
		// if (usu.getOirsRoles().size()>1)
		// throw new
		// Exception("Existe mas de un rol para el usuario en Base de Datos");
		//
		// for (OirsRol o : usu.getOirsRoles()) {
		// objectSet = (OirsRol) o;
		// listaRol.add(objectSet.getRoleDescripcionRol());
		// }
		// }
		// GETRoleCheckResponse checkRol = client.getRoleChecks(listaRol);
		// for (int i = 0; i < checkRol.getRoles().size(); i++) {
		// if (!checkRol.getRoles().get(i).getAssigned())
		// throw new Exception(
		// "Existe inconsisitencia en el rol del usuario de Base de Datos con BRMS");
		// }

	}

	public void guardarUserUtil(Long userId, boolean isAdmin, boolean isCentr, boolean isEspec) {
		getSessionBean().setUserId(userId);
		getSessionBean().setFechaToken(null);
		//System.out.println("Veamos que le está pasando el UserUtil: "+oirsSeguridad.getFechaCreacion());
		getSessionBean().setPassword(contrasena);
		getSessionBean().setToken(null);
		getSessionBean().setUsername(usuario);
		getSessionBean().setRoles(isAdmin, isCentr, isEspec);
	}

	// public BRMSClient getClient() {
	// return client;
	// }
	//
	// public void setClient(BRMSClient client) {
	// this.client = client;
	// }
	//
	// public OirsSeguridad getOirsSeguridad() {
	// return oirsSeguridad;
	// }

	/*public void setOirsSeguridad(OirsSeguridad oirsSeguridad) {
		this.oirsSeguridad = oirsSeguridad;
	}*/

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public FacesContext getFacesContext() {
		return facesContext;
	}

	public void setFacesContext(FacesContext facesContext) {
		this.facesContext = facesContext;
	}

}
