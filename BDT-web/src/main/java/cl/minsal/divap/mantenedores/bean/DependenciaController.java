package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.DependenciaFacade;
import cl.minsal.divap.model.Dependencia;

@Named("dependenciaController")
@ViewScoped
public class DependenciaController extends AbstractController<Dependencia> {

    @EJB
    private DependenciaFacade ejbFacade;

    /**
     * Initialize the concrete Dependencia controller bean. The
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

    public DependenciaController() {
        // Inform the Abstract parent controller of the concrete Dependencia?cap_first Entity
        super(Dependencia.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    /**
     * Sets the "items" attribute with a collection of TipoSubtitulo entities
     * that are retrieved from Dependencia?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for TipoSubtitulo page
     */
    public String navigateTipoSubtitulos() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("TipoSubtitulo_items", this.getSelected().getTipoSubtitulos());
        }
        return "/mantenedor/tipoSubtitulo/index";
    }

}
