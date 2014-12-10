package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.ComunaCumplimientoRebajaFacade;
import cl.minsal.divap.model.ComunaCumplimientoRebaja;

@Named("comunaCumplimientoRebajaController")
@ViewScoped
public class ComunaCumplimientoRebajaController extends AbstractController<ComunaCumplimientoRebaja> {

    @EJB
    private ComunaCumplimientoRebajaFacade ejbFacade;

    /**
     * Initialize the concrete ComunaCumplimientoRebaja controller bean. The
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

    public ComunaCumplimientoRebajaController() {
        // Inform the Abstract parent controller of the concrete ComunaCumplimientoRebaja?cap_first Entity
        super(ComunaCumplimientoRebaja.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    /**
     * Sets the "items" attribute with a collection of ComunaCumplimiento
     * entities that are retrieved from ComunaCumplimientoRebaja?cap_first and
     * returns the navigation outcome.
     *
     * @return navigation outcome for ComunaCumplimiento page
     */
    public String navigateComunaCumplimientosRebajaFinal() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ComunaCumplimiento_items", this.getSelected().getComunaCumplimientosRebajaFinal());
        }
        return "/mantenedor/comunaCumplimiento/index";
    }

    /**
     * Sets the "items" attribute with a collection of ComunaCumplimiento
     * entities that are retrieved from ComunaCumplimientoRebaja?cap_first and
     * returns the navigation outcome.
     *
     * @return navigation outcome for ComunaCumplimiento page
     */
    public String navigateComunaCumplimientosRebajaCalculada() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ComunaCumplimiento_items", this.getSelected().getComunaCumplimientosRebajaCalculada());
        }
        return "/mantenedor/comunaCumplimiento/index";
    }

}
