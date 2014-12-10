package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.EstimacionFlujoCajaSeguimientoFacade;
import cl.minsal.divap.model.EstimacionFlujoCajaSeguimiento;

@Named("estimacionFlujoCajaSeguimientoController")
@ViewScoped
public class EstimacionFlujoCajaSeguimientoController extends AbstractController<EstimacionFlujoCajaSeguimiento> {

    @EJB
    private EstimacionFlujoCajaSeguimientoFacade ejbFacade;
    private SeguimientoController seguimientoController;
    private ProgramaAnoController programaAnoController;

    /**
     * Initialize the concrete EstimacionFlujoCajaSeguimiento controller bean.
     * The AbstractController requires the EJB Facade object for most
     * operations.
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
        programaAnoController = context.getApplication().evaluateExpressionGet(context, "#{programaAnoController}", ProgramaAnoController.class);
    }

    public EstimacionFlujoCajaSeguimientoController() {
        // Inform the Abstract parent controller of the concrete EstimacionFlujoCajaSeguimiento?cap_first Entity
        super(EstimacionFlujoCajaSeguimiento.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        seguimientoController.setSelected(null);
        programaAnoController.setSelected(null);
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
     * Sets the "selected" attribute of the ProgramaAno controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareProgramaAno(ActionEvent event) {
        if (this.getSelected() != null && programaAnoController.getSelected() == null) {
            programaAnoController.setSelected(this.getSelected().getProgramaAno());
        }
    }
}
