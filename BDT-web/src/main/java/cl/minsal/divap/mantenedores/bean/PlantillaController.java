package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.PlantillaFacade;
import cl.minsal.divap.model.Plantilla;

@Named("plantillaController")
@ViewScoped
public class PlantillaController extends AbstractController<Plantilla> {

    @EJB
    private PlantillaFacade ejbFacade;
    private TipoDocumentoController tipoPlantillaController;
    private ReferenciaDocumentoController documentoController;
    private ProgramaController idProgramaController;

    /**
     * Initialize the concrete Plantilla controller bean. The AbstractController
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
        tipoPlantillaController = context.getApplication().evaluateExpressionGet(context, "#{tipoDocumentoController}", TipoDocumentoController.class);
        documentoController = context.getApplication().evaluateExpressionGet(context, "#{referenciaDocumentoController}", ReferenciaDocumentoController.class);
        idProgramaController = context.getApplication().evaluateExpressionGet(context, "#{programaController}", ProgramaController.class);
    }

    public PlantillaController() {
        // Inform the Abstract parent controller of the concrete Plantilla?cap_first Entity
        super(Plantilla.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        tipoPlantillaController.setSelected(null);
        documentoController.setSelected(null);
        idProgramaController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the TipoDocumento controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareTipoPlantilla(ActionEvent event) {
        if (this.getSelected() != null && tipoPlantillaController.getSelected() == null) {
            tipoPlantillaController.setSelected(this.getSelected().getTipoPlantilla());
        }
    }

    /**
     * Sets the "selected" attribute of the ReferenciaDocumento controller in
     * order to display its data in a dialog. This is reusing existing the
     * existing View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareDocumento(ActionEvent event) {
        if (this.getSelected() != null && documentoController.getSelected() == null) {
            documentoController.setSelected(this.getSelected().getDocumento());
        }
    }

    /**
     * Sets the "selected" attribute of the Programa controller in order to
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
}
