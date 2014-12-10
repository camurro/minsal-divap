package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.TareaSeguimientoFacade;
import cl.minsal.divap.model.TareaSeguimiento;

@Named("tareaSeguimientoController")
@ViewScoped
public class TareaSeguimientoController extends AbstractController<TareaSeguimiento> {

    @EJB
    private TareaSeguimientoFacade ejbFacade;

    /**
     * Initialize the concrete TareaSeguimiento controller bean. The
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

    public TareaSeguimientoController() {
        // Inform the Abstract parent controller of the concrete TareaSeguimiento?cap_first Entity
        super(TareaSeguimiento.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    /**
     * Sets the "items" attribute with a collection of Seguimiento entities that
     * are retrieved from TareaSeguimiento?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for Seguimiento page
     */
    public String navigateSeguimientoCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Seguimiento_items", this.getSelected().getSeguimientoCollection());
        }
        return "/mantenedor/seguimiento/index";
    }

}
