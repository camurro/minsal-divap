package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.TipoComponenteFacade;
import cl.minsal.divap.model.TipoComponente;

@Named("tipoComponenteController")
@ViewScoped
public class TipoComponenteController extends AbstractController<TipoComponente> {

    @EJB
    private TipoComponenteFacade ejbFacade;

    /**
     * Initialize the concrete TipoComponente controller bean. The
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

    public TipoComponenteController() {
        // Inform the Abstract parent controller of the concrete TipoComponente?cap_first Entity
        super(TipoComponente.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    /**
     * Sets the "items" attribute with a collection of Componente entities that
     * are retrieved from TipoComponente?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for Componente page
     */
    public String navigateComponentes() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Componente_items", this.getSelected().getComponentes());
        }
        return "/mantenedor/componente/index";
    }

    /**
     * Sets the "items" attribute with a collection of MetadataCore entities
     * that are retrieved from TipoComponente?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for MetadataCore page
     */
    public String navigateMetadataCoreCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("MetadataCore_items", this.getSelected().getMetadataCoreCollection());
        }
        return "/mantenedor/metadataCore/index";
    }

}
