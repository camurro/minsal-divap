package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.TipoCumplimientoFacade;
import cl.minsal.divap.model.TipoCumplimiento;

@Named("tipoCumplimientoController")
@ViewScoped
public class TipoCumplimientoController extends AbstractController<TipoCumplimiento> {

    @EJB
    private TipoCumplimientoFacade ejbFacade;

    /**
     * Initialize the concrete TipoCumplimiento controller bean. The
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

    public TipoCumplimientoController() {
        // Inform the Abstract parent controller of the concrete TipoCumplimiento?cap_first Entity
        super(TipoCumplimiento.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    /**
     * Sets the "items" attribute with a collection of ComunaCumplimiento
     * entities that are retrieved from TipoCumplimiento?cap_first and returns
     * the navigation outcome.
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
