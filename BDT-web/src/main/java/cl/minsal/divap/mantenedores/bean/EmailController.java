package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.EmailFacade;
import cl.minsal.divap.model.Email;

@Named("emailController")
@ViewScoped
public class EmailController extends AbstractController<Email> {

    @EJB
    private EmailFacade ejbFacade;

    /**
     * Initialize the concrete Email controller bean. The AbstractController
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
    }

    public EmailController() {
        // Inform the Abstract parent controller of the concrete Email?cap_first Entity
        super(Email.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    /**
     * Sets the "items" attribute with a collection of Usuario entities that are
     * retrieved from Email?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Usuario page
     */
    public String navigateUsuarioCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Usuario_items", this.getSelected().getUsuarioCollection());
        }
        return "/mantenedor/usuario/index";
    }

    /**
     * Sets the "items" attribute with a collection of Destinatarios entities
     * that are retrieved from Email?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for Destinatarios page
     */
    public String navigateDestinatariosCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Destinatarios_items", this.getSelected().getDestinatariosCollection());
        }
        return "/mantenedor/destinatarios/index";
    }

    /**
     * Sets the "items" attribute with a collection of Seguimiento entities that
     * are retrieved from Email?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Seguimiento page
     */
    public String navigateSeguimientoCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Seguimiento_items", this.getSelected().getSeguimientoCollection());
        }
        return "/mantenedor/seguimiento/index";
    }

    /**
     * Sets the "items" attribute with a collection of Persona entities that are
     * retrieved from Email?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Persona page
     */
    public String navigatePersonas() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Persona_items", this.getSelected().getPersonas());
        }
        return "/mantenedor/persona/index";
    }

}
