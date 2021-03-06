package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.ReliquidacionComunaFacade;
import cl.minsal.divap.model.ReliquidacionComuna;

@Named("reliquidacionComunaController")
@ViewScoped
public class ReliquidacionComunaController extends AbstractController<ReliquidacionComuna> {

    @EJB
    private ReliquidacionComunaFacade ejbFacade;
    private ServicioSaludController servicioController;
    private ReliquidacionController reliquidacionController;
    private ProgramaAnoController programaController;
    private CumplimientoProgramaController cumplimientoController;
    private ComunaController comunaController;
    private ComponenteController componenteController;

    /**
     * Initialize the concrete ReliquidacionComuna controller bean. The
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
        servicioController = context.getApplication().evaluateExpressionGet(context, "#{servicioSaludController}", ServicioSaludController.class);
        reliquidacionController = context.getApplication().evaluateExpressionGet(context, "#{reliquidacionController}", ReliquidacionController.class);
        programaController = context.getApplication().evaluateExpressionGet(context, "#{programaAnoController}", ProgramaAnoController.class);
        cumplimientoController = context.getApplication().evaluateExpressionGet(context, "#{cumplimientoProgramaController}", CumplimientoProgramaController.class);
        comunaController = context.getApplication().evaluateExpressionGet(context, "#{comunaController}", ComunaController.class);
        componenteController = context.getApplication().evaluateExpressionGet(context, "#{componenteController}", ComponenteController.class);
    }

    public ReliquidacionComunaController() {
        // Inform the Abstract parent controller of the concrete ReliquidacionComuna?cap_first Entity
        super(ReliquidacionComuna.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        servicioController.setSelected(null);
        reliquidacionController.setSelected(null);
        programaController.setSelected(null);
        cumplimientoController.setSelected(null);
        comunaController.setSelected(null);
        componenteController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the ServicioSalud controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareServicio(ActionEvent event) {
        if (this.getSelected() != null && servicioController.getSelected() == null) {
            servicioController.setSelected(this.getSelected().getServicio());
        }
    }

    /**
     * Sets the "selected" attribute of the Reliquidacion controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareReliquidacion(ActionEvent event) {
        if (this.getSelected() != null && reliquidacionController.getSelected() == null) {
            reliquidacionController.setSelected(this.getSelected().getReliquidacion());
        }
    }

    /**
     * Sets the "selected" attribute of the ProgramaAno controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void preparePrograma(ActionEvent event) {
        if (this.getSelected() != null && programaController.getSelected() == null) {
            programaController.setSelected(this.getSelected().getPrograma());
        }
    }

    /**
     * Sets the "selected" attribute of the CumplimientoPrograma controller in
     * order to display its data in a dialog. This is reusing existing the
     * existing View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareCumplimiento(ActionEvent event) {
        if (this.getSelected() != null && cumplimientoController.getSelected() == null) {
//            cumplimientoController.setSelected(this.getSelected().getCumplimiento());
        }
    }

    /**
     * Sets the "selected" attribute of the Comuna controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareComuna(ActionEvent event) {
        if (this.getSelected() != null && comunaController.getSelected() == null) {
            comunaController.setSelected(this.getSelected().getComuna());
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
//            componenteController.setSelected(this.getSelected().get Componente());
        }
    }
}
