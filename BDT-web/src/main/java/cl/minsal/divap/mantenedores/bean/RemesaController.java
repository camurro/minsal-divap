package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.RemesaFacade;
import cl.minsal.divap.model.Remesa;

@Named("remesaController")
@ViewScoped
public class RemesaController extends AbstractController<Remesa> {

    @EJB
    private RemesaFacade ejbFacade;
    private TipoSubtituloController tipoSubtituloController;
    private ServicioSaludController idserviciosaludController;
    private ProgramaAnoController idprogramaController;
    private MesController idmesController;
    private EstablecimientoController idestablecimientoController;
    private ComunaController idcomunaController;

    /**
     * Initialize the concrete Remesa controller bean. The AbstractController
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
        tipoSubtituloController = context.getApplication().evaluateExpressionGet(context, "#{tipoSubtituloController}", TipoSubtituloController.class);
        idserviciosaludController = context.getApplication().evaluateExpressionGet(context, "#{servicioSaludController}", ServicioSaludController.class);
        idprogramaController = context.getApplication().evaluateExpressionGet(context, "#{programaAnoController}", ProgramaAnoController.class);
        idmesController = context.getApplication().evaluateExpressionGet(context, "#{mesController}", MesController.class);
        idestablecimientoController = context.getApplication().evaluateExpressionGet(context, "#{establecimientoController}", EstablecimientoController.class);
        idcomunaController = context.getApplication().evaluateExpressionGet(context, "#{comunaController}", ComunaController.class);
    }

    public RemesaController() {
        // Inform the Abstract parent controller of the concrete Remesa?cap_first Entity
        super(Remesa.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        tipoSubtituloController.setSelected(null);
        idserviciosaludController.setSelected(null);
        idprogramaController.setSelected(null);
        idmesController.setSelected(null);
        idestablecimientoController.setSelected(null);
        idcomunaController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the TipoSubtitulo controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareTipoSubtitulo(ActionEvent event) {
        if (this.getSelected() != null && tipoSubtituloController.getSelected() == null) {
            tipoSubtituloController.setSelected(this.getSelected().getTipoSubtitulo());
        }
    }

    /**
     * Sets the "selected" attribute of the ServicioSalud controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareIdserviciosalud(ActionEvent event) {
        if (this.getSelected() != null && idserviciosaludController.getSelected() == null) {
            idserviciosaludController.setSelected(this.getSelected().getIdserviciosalud());
        }
    }

    /**
     * Sets the "selected" attribute of the ProgramaAno controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareIdprograma(ActionEvent event) {
        if (this.getSelected() != null && idprogramaController.getSelected() == null) {
            idprogramaController.setSelected(this.getSelected().getIdprograma());
        }
    }

    /**
     * Sets the "selected" attribute of the Mes controller in order to display
     * its data in a dialog. This is reusing existing the existing View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareIdmes(ActionEvent event) {
        if (this.getSelected() != null && idmesController.getSelected() == null) {
            idmesController.setSelected(this.getSelected().getIdmes());
        }
    }

    /**
     * Sets the "selected" attribute of the Establecimiento controller in order
     * to display its data in a dialog. This is reusing existing the existing
     * View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareIdestablecimiento(ActionEvent event) {
        if (this.getSelected() != null && idestablecimientoController.getSelected() == null) {
            idestablecimientoController.setSelected(this.getSelected().getIdestablecimiento());
        }
    }

    /**
     * Sets the "selected" attribute of the Comuna controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareIdcomuna(ActionEvent event) {
        if (this.getSelected() != null && idcomunaController.getSelected() == null) {
            idcomunaController.setSelected(this.getSelected().getIdcomuna());
        }
    }
}
