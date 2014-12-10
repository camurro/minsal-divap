package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.MesFacade;
import cl.minsal.divap.model.Mes;

@Named("mesController")
@ViewScoped
public class MesController extends AbstractController<Mes> {

    @EJB
    private MesFacade ejbFacade;

    /**
     * Initialize the concrete Mes controller bean. The AbstractController
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

    public MesController() {
        // Inform the Abstract parent controller of the concrete Mes?cap_first Entity
        super(Mes.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    /**
     * Sets the "items" attribute with a collection of Remesa entities that are
     * retrieved from Mes?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Remesa page
     */
    public String navigateRemesaCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Remesa_items", this.getSelected().getRemesaCollection());
        }
        return "/mantenedor/remesa/index";
    }

    /**
     * Sets the "items" attribute with a collection of ComunaCumplimiento
     * entities that are retrieved from Mes?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for ComunaCumplimiento page
     */
    public String navigateComunaCumplimientoCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ComunaCumplimiento_items", this.getSelected().getComunaCumplimientoCollection());
        }
        return "/mantenedor/comunaCumplimiento/index";
    }

}
