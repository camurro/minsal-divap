package cl.minsal.divap.mantenedores.bean;

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

import minsal.divap.dao.EmailDAO;
import minsal.divap.service.MantenedoresService;
import minsal.divap.vo.EmailVO;
import minsal.divap.vo.MantenedorComponenteVO;
import minsal.divap.vo.PersonaVO;
import cl.minsal.divap.mantenedores.bean.util.JsfUtil;
import cl.minsal.divap.mantenedores.enums.PersistAction;
import cl.minsal.divap.mantenedores.facade.PersonaFacade;
import cl.minsal.divap.model.Email;
import cl.minsal.divap.model.Persona;

@Named("personaController")
@ViewScoped
public class PersonaController extends AbstractController<Persona> {

    @EJB
    private PersonaFacade ejbFacade;
    @EJB
    private MantenedoresService mantenedoresService;
    @EJB
    private EmailDAO emailDAO;
    
    private EmailController emailController;

    private List<PersonaVO> personas;
    private PersonaVO seleccionado;
    private List<Email> emails;
    
    
    
    @PostConstruct
    @Override
    public void init() {
        super.setFacade(ejbFacade);
        FacesContext context = FacesContext.getCurrentInstance();
        emailController = context.getApplication().evaluateExpressionGet(context, "#{emailController}", EmailController.class);
    }
    
    public void saveNew(ActionEvent event) {
		System.out.println("entra al saveNew");
		super.saveNew(event);
		personas = null;
		emails = null;
	}

	public void edit(ActionEvent event){
		System.out.println("entra al edit");
		super.edit(event);
		personas = null;
		emails = null;
	}
	
	public void delete(ActionEvent event){
		System.out.println("entra al delete");
		super.delete(event);
		seleccionado = null;
		personas = null;
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
    

    public PersonaController() {
        // Inform the Abstract parent controller of the concrete Persona?cap_first Entity
        super(Persona.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        emailController.setSelected(null);
    }

    /**
     * Sets the "items" attribute with a collection of ServicioSalud entities
     * that are retrieved from Persona?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for ServicioSalud page
     */
    public String navigateServicioSaludCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ServicioSalud_items", this.getSelected().getServicioSaludCollection());
        }
        return "/mantenedor/servicioSalud/index";
    }

    /**
     * Sets the "items" attribute with a collection of Region entities that are
     * retrieved from Persona?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Region page
     */
    public String navigateRegiones() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Region_items", this.getSelected().getRegiones());
        }
        return "/mantenedor/region/index";
    }
    
    public void prepareEmail(ActionEvent event) {
        if (this.getSelected() != null && emailController.getSelected() == null) {
            emailController.setSelected(this.getSelected().getEmail());
        }
    }
    
    public void prepareCreatePersona(ActionEvent event) {
		System.out.println("prepareCreatePersona");
		seleccionado = new PersonaVO();
		super.prepareCreate(event);
	}

	public List<PersonaVO> getPersonas() {
		if(personas == null){
			personas = mantenedoresService.getAllPersonas();
		}
		return personas;
	}

	public void setPersonas(List<PersonaVO> personas) {
		this.personas = personas;
	}

	public PersonaVO getSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(PersonaVO seleccionado) {
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
    
    
    
}
