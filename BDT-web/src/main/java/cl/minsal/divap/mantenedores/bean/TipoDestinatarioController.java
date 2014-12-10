package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.TipoDestinatarioFacade;
import cl.minsal.divap.model.TipoDestinatario;

@Named("tipoDestinatarioController")
@ViewScoped
public class TipoDestinatarioController extends AbstractController<TipoDestinatario> {

    @EJB
    private TipoDestinatarioFacade ejbFacade;

    /**
     * Initialize the concrete TipoDestinatario controller bean. The
     * AbstractController requires the EJB Facade object for most operations.
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

    public TipoDestinatarioController() {
        // Inform the Abstract parent controller of the concrete TipoDestinatario?cap_first Entity
        super(TipoDestinatario.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    /**
     * Sets the "items" attribute with a collection of Destinatarios entities
     * that are retrieved from TipoDestinatario?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for Destinatarios page
     */
    public String navigateDestinatariosCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Destinatarios_items", this.getSelected().getDestinatariosCollection());
        }
        return "/mantenedor/destinatarios/index";
    }

}
