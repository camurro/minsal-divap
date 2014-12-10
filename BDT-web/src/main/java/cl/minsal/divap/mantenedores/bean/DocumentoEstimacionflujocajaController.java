package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.DocumentoEstimacionflujocajaFacade;
import cl.minsal.divap.model.DocumentoEstimacionflujocaja;

@Named("documentoEstimacionflujocajaController")
@ViewScoped
public class DocumentoEstimacionflujocajaController extends AbstractController<DocumentoEstimacionflujocaja> {

    @EJB
    private DocumentoEstimacionflujocajaFacade ejbFacade;
    private TipoDocumentoController idTipoDocumentoController;
    private ReferenciaDocumentoController idDocumentoController;
    private ProgramaAnoController idProgramaAnoController;
    private MesController idMesController;
    private AnoEnCursoController anoController;

    /**
     * Initialize the concrete DocumentoEstimacionflujocaja controller bean. The
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
        idTipoDocumentoController = context.getApplication().evaluateExpressionGet(context, "#{tipoDocumentoController}", TipoDocumentoController.class);
        idDocumentoController = context.getApplication().evaluateExpressionGet(context, "#{referenciaDocumentoController}", ReferenciaDocumentoController.class);
        idProgramaAnoController = context.getApplication().evaluateExpressionGet(context, "#{programaAnoController}", ProgramaAnoController.class);
        idMesController = context.getApplication().evaluateExpressionGet(context, "#{mesController}", MesController.class);
        anoController = context.getApplication().evaluateExpressionGet(context, "#{anoEnCursoController}", AnoEnCursoController.class);
    }

    public DocumentoEstimacionflujocajaController() {
        // Inform the Abstract parent controller of the concrete DocumentoEstimacionflujocaja?cap_first Entity
        super(DocumentoEstimacionflujocaja.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        idTipoDocumentoController.setSelected(null);
        idDocumentoController.setSelected(null);
        idProgramaAnoController.setSelected(null);
        idMesController.setSelected(null);
        anoController.setSelected(null);
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
     * Sets the "selected" attribute of the AnoEnCurso controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareAno(ActionEvent event) {
        if (this.getSelected() != null && anoController.getSelected() == null) {
            anoController.setSelected(this.getSelected().getAno());
        }
    }
}
