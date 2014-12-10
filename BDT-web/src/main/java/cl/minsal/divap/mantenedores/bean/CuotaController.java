package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.CuotaFacade;
import cl.minsal.divap.model.Cuota;

@Named("cuotaController")
@ViewScoped
public class CuotaController extends AbstractController<Cuota> {

    @EJB
    private CuotaFacade ejbFacade;
    private ProgramaAnoController idProgramaController;
    private MesController idMesController;
    private ComponenteController componenteController;

    /**
     * Initialize the concrete Cuota controller bean. The AbstractController
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
        idProgramaController = context.getApplication().evaluateExpressionGet(context, "#{programaAnoController}", ProgramaAnoController.class);
        idMesController = context.getApplication().evaluateExpressionGet(context, "#{mesController}", MesController.class);
        componenteController = context.getApplication().evaluateExpressionGet(context, "#{componenteController}", ComponenteController.class);
    }

    public CuotaController() {
        // Inform the Abstract parent controller of the concrete Cuota?cap_first Entity
        super(Cuota.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        idProgramaController.setSelected(null);
        idMesController.setSelected(null);
        componenteController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the ProgramaAno controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareIdPrograma(ActionEvent event) {
        if (this.getSelected() != null && idProgramaController.getSelected() == null) {
            idProgramaController.setSelected(this.getSelected().getIdPrograma());
        }
    }

    /**
     * Sets the "selected" attribute of the Mes controller in order to display
     * its data in a dialog. This is reusing existing the existing View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareIdMes(ActionEvent event) {
        if (this.getSelected() != null && idMesController.getSelected() == null) {
            idMesController.setSelected(this.getSelected().getIdMes());
        }
    }

    /**
     * Sets the "selected" attribute of the Componente controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareComponente(ActionEvent event) {
        if (this.getSelected() != null && componenteController.getSelected() == null) {
            componenteController.setSelected(this.getSelected().getComponente());
        }
    }
}
