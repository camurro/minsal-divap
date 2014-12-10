package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.RebajaSeguimientoFacade;
import cl.minsal.divap.model.RebajaSeguimiento;

@Named("rebajaSeguimientoController")
@ViewScoped
public class RebajaSeguimientoController extends AbstractController<RebajaSeguimiento> {

    @EJB
    private RebajaSeguimientoFacade ejbFacade;
    private SeguimientoController seguimientoController;
    private RebajaController rebajaController;

    /**
     * Initialize the concrete RebajaSeguimiento controller bean. The
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
        seguimientoController = context.getApplication().evaluateExpressionGet(context, "#{seguimientoController}", SeguimientoController.class);
        rebajaController = context.getApplication().evaluateExpressionGet(context, "#{rebajaController}", RebajaController.class);
    }

    public RebajaSeguimientoController() {
        // Inform the Abstract parent controller of the concrete RebajaSeguimiento?cap_first Entity
        super(RebajaSeguimiento.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        seguimientoController.setSelected(null);
        rebajaController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the Seguimiento controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareSeguimiento(ActionEvent event) {
        if (this.getSelected() != null && seguimientoController.getSelected() == null) {
            seguimientoController.setSelected(this.getSelected().getSeguimiento());
        }
    }

    /**
     * Sets the "selected" attribute of the Rebaja controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareRebaja(ActionEvent event) {
        if (this.getSelected() != null && rebajaController.getSelected() == null) {
            rebajaController.setSelected(this.getSelected().getRebaja());
        }
    }
}
