package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.ComunaCumplimientoFacade;
import cl.minsal.divap.model.ComunaCumplimiento;

@Named("comunaCumplimientoController")
@ViewScoped
public class ComunaCumplimientoController extends AbstractController<ComunaCumplimiento> {

    @EJB
    private ComunaCumplimientoFacade ejbFacade;
    private TipoCumplimientoController idTipoCumplimientoController;
    private MesController idMesController;
    private RebajaController rebajaController;
    private ComunaCumplimientoRebajaController rebajaFinalController;
    private ComunaCumplimientoRebajaController rebajaCalculadaController;
    private ComunaController idComunaController;

    /**
     * Initialize the concrete ComunaCumplimiento controller bean. The
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
        idTipoCumplimientoController = context.getApplication().evaluateExpressionGet(context, "#{tipoCumplimientoController}", TipoCumplimientoController.class);
        idMesController = context.getApplication().evaluateExpressionGet(context, "#{mesController}", MesController.class);
        rebajaController = context.getApplication().evaluateExpressionGet(context, "#{rebajaController}", RebajaController.class);
        rebajaFinalController = context.getApplication().evaluateExpressionGet(context, "#{comunaCumplimientoRebajaController}", ComunaCumplimientoRebajaController.class);
        rebajaCalculadaController = context.getApplication().evaluateExpressionGet(context, "#{comunaCumplimientoRebajaController}", ComunaCumplimientoRebajaController.class);
        idComunaController = context.getApplication().evaluateExpressionGet(context, "#{comunaController}", ComunaController.class);
    }

    public ComunaCumplimientoController() {
        // Inform the Abstract parent controller of the concrete ComunaCumplimiento?cap_first Entity
        super(ComunaCumplimiento.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        idTipoCumplimientoController.setSelected(null);
        idMesController.setSelected(null);
        rebajaController.setSelected(null);
        rebajaFinalController.setSelected(null);
        rebajaCalculadaController.setSelected(null);
        idComunaController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the TipoCumplimiento controller in order
     * to display its data in a dialog. This is reusing existing the existing
     * View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareIdTipoCumplimiento(ActionEvent event) {
        if (this.getSelected() != null && idTipoCumplimientoController.getSelected() == null) {
            idTipoCumplimientoController.setSelected(this.getSelected().getIdTipoCumplimiento());
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

    /**
     * Sets the "selected" attribute of the ComunaCumplimientoRebaja controller
     * in order to display its data in a dialog. This is reusing existing the
     * existing View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareRebajaFinal(ActionEvent event) {
        if (this.getSelected() != null && rebajaFinalController.getSelected() == null) {
            rebajaFinalController.setSelected(this.getSelected().getRebajaFinal());
        }
    }

    /**
     * Sets the "selected" attribute of the ComunaCumplimientoRebaja controller
     * in order to display its data in a dialog. This is reusing existing the
     * existing View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareRebajaCalculada(ActionEvent event) {
        if (this.getSelected() != null && rebajaCalculadaController.getSelected() == null) {
            rebajaCalculadaController.setSelected(this.getSelected().getRebajaCalculada());
        }
    }

    /**
     * Sets the "selected" attribute of the Comuna controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareIdComuna(ActionEvent event) {
        if (this.getSelected() != null && idComunaController.getSelected() == null) {
            idComunaController.setSelected(this.getSelected().getIdComuna());
        }
    }
}
