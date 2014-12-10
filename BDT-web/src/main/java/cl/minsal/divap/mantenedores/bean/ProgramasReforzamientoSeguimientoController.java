package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.ProgramasReforzamientoSeguimientoFacade;
import cl.minsal.divap.model.ProgramasReforzamientoSeguimiento;

@Named("programasReforzamientoSeguimientoController")
@ViewScoped
public class ProgramasReforzamientoSeguimientoController extends AbstractController<ProgramasReforzamientoSeguimiento> {

    @EJB
    private ProgramasReforzamientoSeguimientoFacade ejbFacade;
    private SeguimientoController seguimientoController;
    private ProgramaAnoController idProgramaAnoController;

    /**
     * Initialize the concrete ProgramasReforzamientoSeguimiento controller
     * bean. The AbstractController requires the EJB Facade object for most
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
        idProgramaAnoController = context.getApplication().evaluateExpressionGet(context, "#{programaAnoController}", ProgramaAnoController.class);
    }

    public ProgramasReforzamientoSeguimientoController() {
        // Inform the Abstract parent controller of the concrete ProgramasReforzamientoSeguimiento?cap_first Entity
        super(ProgramasReforzamientoSeguimiento.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        seguimientoController.setSelected(null);
        idProgramaAnoController.setSelected(null);
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
    public void prepareIdProgramaAno(ActionEvent event) {
        if (this.getSelected() != null && idProgramaAnoController.getSelected() == null) {
            idProgramaAnoController.setSelected(this.getSelected().getIdProgramaAno());
        }
    }
}
