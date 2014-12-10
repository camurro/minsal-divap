package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.DocumentoProgramasReforzamientoFacade;
import cl.minsal.divap.model.DocumentoProgramasReforzamiento;

@Named("documentoProgramasReforzamientoController")
@ViewScoped
public class DocumentoProgramasReforzamientoController extends AbstractController<DocumentoProgramasReforzamiento> {

    @EJB
    private DocumentoProgramasReforzamientoFacade ejbFacade;
    private TipoDocumentoController idTipoDocumentoController;
    private ReferenciaDocumentoController idDocumentoController;
    private ProgramaAnoController idProgramaAnoController;
    private ServicioSaludController idServicioController;

    /**
     * Initialize the concrete DocumentoProgramasReforzamiento controller bean.
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
        idTipoDocumentoController = context.getApplication().evaluateExpressionGet(context, "#{tipoDocumentoController}", TipoDocumentoController.class);
        idDocumentoController = context.getApplication().evaluateExpressionGet(context, "#{referenciaDocumentoController}", ReferenciaDocumentoController.class);
        idProgramaAnoController = context.getApplication().evaluateExpressionGet(context, "#{programaAnoController}", ProgramaAnoController.class);
        idServicioController = context.getApplication().evaluateExpressionGet(context, "#{servicioSaludController}", ServicioSaludController.class);
    }

    public DocumentoProgramasReforzamientoController() {
        // Inform the Abstract parent controller of the concrete DocumentoProgramasReforzamiento?cap_first Entity
        super(DocumentoProgramasReforzamiento.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        idTipoDocumentoController.setSelected(null);
        idDocumentoController.setSelected(null);
        idProgramaAnoController.setSelected(null);
        idServicioController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the TipoDocumento controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareIdTipoDocumento(ActionEvent event) {
        if (this.getSelected() != null && idTipoDocumentoController.getSelected() == null) {
            idTipoDocumentoController.setSelected(this.getSelected().getIdTipoDocumento());
        }
    }

    /**
     * Sets the "selected" attribute of the ReferenciaDocumento controller in
     * order to display its data in a dialog. This is reusing existing the
     * existing View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareIdDocumento(ActionEvent event) {
        if (this.getSelected() != null && idDocumentoController.getSelected() == null) {
            idDocumentoController.setSelected(this.getSelected().getIdDocumento());
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

    /**
     * Sets the "selected" attribute of the ServicioSalud controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareIdServicio(ActionEvent event) {
        if (this.getSelected() != null && idServicioController.getSelected() == null) {
            idServicioController.setSelected(this.getSelected().getIdServicio());
        }
    }
}
