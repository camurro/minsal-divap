package cl.redhat.bandejaTareas.controller;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import minsal.divap.service.UsuarioService;
import minsal.divap.vo.UsuarioVO;

import org.apache.log4j.Logger;

import cl.redhat.bandejaTareas.util.BandejaProperties;

@Named("loginController")
@ViewScoped
public class LoginController extends BaseController implements Serializable {

	private static final long serialVersionUID = -8180634775574337540L;

	private static Logger log = Logger.getLogger(LoginController.class);

	private String usuario;
	private String contrasena;

	@EJB 
	private UsuarioService usuarioService;

	@Inject
	private BandejaProperties bandejaProperties;

	@Inject
	private FacesContext facesContext;

	@PostConstruct
	public void init() {

	}

	public void logout() {
		try {
			getSessionBean().logout();
			ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
			HttpSession session = (HttpSession)context.getSession(true);
			System.out.println("logout antes de invalidate");
			if (session != null) {
				session.invalidate();
			}
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
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest)
					context.getExternalContext().getRequest();
			System.out.println("usuario="+usuario);
			System.out.println("contrasena="+contrasena);
			
			request.login(usuario.trim(), contrasena.trim());
			
			UsuarioVO usuarioVO = this.usuarioService.getUserByUsername(usuario);
			
			getSessionBean().setUsername(usuario);
			getSessionBean().setPassword(contrasena);
			if(usuarioVO.getRoles() != null && usuarioVO.getRoles().size() > 0){
				getSessionBean().setRoles(new ArrayList<String>(usuarioVO.getRoles()));
			}

			boolean isAdmin = true;
			boolean isCentr = true;
			boolean isEspec = true;

			guardarUserUtil(8L, isAdmin,
					isCentr, isEspec);
		} catch (Exception e) {
			e.printStackTrace();
			facesContext.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "", e.getMessage()));
			return null;
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
