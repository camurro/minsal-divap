package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.TramoFacade;
import cl.minsal.divap.model.Tramo;

@Named("tramoController")
@ViewScoped
public class TramoController extends AbstractController<Tramo> {

    @EJB
    private TramoFacade ejbFacade;

    /**
     * Initialize the concrete Tramo controller bean. The AbstractController
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

    public TramoController() {
        // Inform the Abstract parent controller of the concrete Tramo?cap_first Entity
        super(Tramo.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    /**
     * Sets the "items" attribute with a collection of Cumplimiento entities
     * that are retrieved from Tramo?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for Cumplimiento page
     */
    public String navigateCumplimientoCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Cumplimiento_items", this.getSelected().getCumplimientoCollection());
        }
        return "/mantenedor/cumplimiento/index";
    }

}
