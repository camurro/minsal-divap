package cl.minsal.divap.mantenedores.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.primefaces.model.DualListModel;

import minsal.divap.dao.EmailDAO;
import minsal.divap.service.MantenedoresService;
import minsal.divap.service.ServicioSaludService;
import minsal.divap.vo.MantenedorEstadoProgramaVO;
import minsal.divap.vo.MantenedorUsuarioVO;
import minsal.divap.vo.ServiciosVO;
import cl.minsal.divap.mantenedores.bean.util.JsfUtil;
import cl.minsal.divap.mantenedores.enums.PersistAction;
import cl.minsal.divap.mantenedores.facade.UsuarioFacade;
import cl.minsal.divap.model.Email;
import cl.minsal.divap.model.Usuario;

@Named("usuarioController")
@ViewScoped
public class UsuarioController extends AbstractController<Usuario> {

	private List<MantenedorUsuarioVO> usuarios;
	private MantenedorUsuarioVO seleccionado;
	private List<Email> emails;
	private List<ServiciosVO> servicios;
	private DualListModel<String> roles;
	

	@EJB
    private UsuarioFacade ejbFacade;
    @EJB
    private MantenedoresService mantenedoresService;
    @EJB
    private ServicioSaludService servicioSaludService;
    @EJB
    private EmailDAO emailDAO;
    
    private EmailController emailController;
    private ServicioSaludController servicioController;

    /**
     * Initialize the concrete Usuario controller bean. The AbstractController
     * requires the EJB Facade object for most operations.
     * <p>
     * In addition, this controller also requires references to controllers for
     * parent entities in order to display their information from a context
     * menu.
     */
    @PostConstruct
    @Override
    public void init() {
        super.setFacade(ejbFacade);
        FacesContext context = FacesContext.getCurrentInstance();
        emailController = context.getApplication().evaluateExpressionGet(context, "#{emailController}", EmailController.class);
        servicioController = context.getApplication().evaluateExpressionGet(context, "#{servicioSaludController}", ServicioSaludController.class);
    }
    
    public void prepareCreateUsuario(ActionEvent event) {
		System.out.println("prepareCreateUsuario");
		System.out.println("PREPARECREATE seleccionado --> "+seleccionado);
		seleccionado = new MantenedorUsuarioVO();
		
		List<String> listaSource = mantenedoresService.getNombreRolesAll();
        List<String> listaTarget = new ArrayList<String>();
        this.roles = new DualListModel<String>(listaSource, listaTarget);
		super.prepareCreate(event);
	}
    
    public void prepareEditarUsuario(ActionEvent event) {
    	System.out.println("prepare editar Usuario");
    	this.roles = new DualListModel<String>(this.seleccionado.getNombreRolesFaltantes(), this.seleccionado.getNombreRoles());
    	
    }
    
    public void saveNew(ActionEvent event) {
		System.out.println("entra al saveNew");
		seleccionado.setNombreRoles(roles.getTarget());
		super.saveNew(event);
		usuarios = null;
		servicios = null;
		emails = null;
	}

	public void edit(ActionEvent event){
		seleccionado.setNombreRoles(roles.getTarget());
		System.out.println("entra al edit");
		super.edit(event);
		usuarios = null;
		servicios = null;
		emails = null;
	}
	
	public void delete(ActionEvent event){
		System.out.println("entra al delete");
		seleccionado.setNombreRoles(roles.getTarget());
		super.delete(event);
		seleccionado = null;
		usuarios = null;
		servicios = null;
		emails = null;
	}
    
	@Override
	protected void persist(PersistAction persistAction, String successMessage) {
		System.out.println("this.seleccionado ---> "+this.seleccionado);
		if (this.seleccionado != null) {
			this.setEmbeddableKeys();
			try {
				if (persistAction == PersistAction.UPDATE) {
					this.ejbFacade.edit(this.seleccionado);
				}else if(persistAction == PersistAction.CREATE){
					this.ejbFacade.create(this.seleccionado);
				}else if(persistAction == PersistAction.DELETE){
					System.out.println("borrando con nuestro delete");
					this.ejbFacade.remove(this.seleccionado);
				}
				JsfUtil.addSuccessMessage(successMessage);
			} catch (EJBException ex) {
				Throwable cause = JsfUtil.getRootCause(ex.getCause());
				if (cause != null) {
					if (cause instanceof ConstraintViolationException) {
						ConstraintViolationException excp = (ConstraintViolationException) cause;
						for (ConstraintViolation s : excp.getConstraintViolations()) {
							JsfUtil.addErrorMessage(s.getMessage());
						}
					} else {
						String msg = cause.getLocalizedMessage();
						if (msg.length() > 0) {
							JsfUtil.addErrorMessage(msg);
						} else {
							JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
						}
					}
				}
			} catch (Exception ex) {
				Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
				JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("bundle/Bundle").getString("PersistenceErrorOccured"));
			}
		}
	}

    public UsuarioController() {
        // Inform the Abstract parent controller of the concrete Usuario?cap_first Entity
        super(Usuario.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        emailController.setSelected(null);
        servicioController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the Email controller in order to display
     * its data in a dialog. This is reusing existing the existing View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareEmail(ActionEvent event) {
        if (this.getSelected() != null && emailController.getSelected() == null) {
            emailController.setSelected(this.getSelected().getEmail());
        }
    }

    /**
     * Sets the "items" attribute with a collection of Programa entities that
     * are retrieved from Usuario?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Programa page
     */
    public String navigateProgramas() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Programa_items", this.getSelected().getProgramas());
        }
        return "/mantenedor/programa/index";
    }

    /**
     * Sets the "items" attribute with a collection of Rol entities that are
     * retrieved from Usuario?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Rol page
     */
    public String navigateRols() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Rol_items", this.getSelected().getRols());
        }
        return "/mantenedor/rol/index";
    }

    /**
     * Sets the "selected" attribute of the ServicioSalud controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareServicio(ActionEvent event) {
        if (this.getSelected() != null && servicioController.getSelected() == null) {
            servicioController.setSelected(this.getSelected().getServicio());
        }
    }

    /**
     * Sets the "items" attribute with a collection of
     * DistribucionInicialPercapita entities that are retrieved from
     * Usuario?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for DistribucionInicialPercapita page
     */
    public String navigateDistribucionInicialPercapitaCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("DistribucionInicialPercapita_items", this.getSelected().getDistribucionInicialPercapitaCollection());
        }
        return "/mantenedor/distribucionInicialPercapita/index";
    }

	public List<MantenedorUsuarioVO> getUsuarios() {
		if(usuarios == null){
			usuarios = mantenedoresService.getMantenedorUsuarioAll();
		}
		return usuarios;
	}

	public void setUsuarios(List<MantenedorUsuarioVO> usuarios) {
		this.usuarios = usuarios;
	}

	public MantenedorUsuarioVO getSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(MantenedorUsuarioVO seleccionado) {
		System.out.println("this.seleccionado ----> "+this.seleccionado);
		this.seleccionado = seleccionado;
	}

	public List<Email> getEmails() {
		if(emails == null){
			emails = emailDAO.getAllEmail();
		}
		return emails;
	}

	public void setEmails(List<Email> emails) {
		this.emails = emails;
	}

	public List<ServiciosVO> getServicios() {
		if(servicios == null){
			servicios = servicioSaludService.getServiciosOrderId();
		}
		return servicios;
	}

	public void setServicios(List<ServiciosVO> servicios) {
		this.servicios = servicios;
	}

	public DualListModel<String> getRoles() {
		List<String> listaSource = new ArrayList<String>();
        List<String> listaTarget = new ArrayList<String>();
        if(roles == null){
        	listaSource = mantenedoresService.getNombreRolesAll();
    		roles = new DualListModel<String>(listaSource, listaTarget);
        }
        
		return roles;
	}

	public void setRoles(DualListModel<String> roles) {
		this.roles = roles;
	}
    

}
