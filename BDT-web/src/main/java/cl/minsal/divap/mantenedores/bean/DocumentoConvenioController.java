package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.DocumentoConvenioFacade;
import cl.minsal.divap.model.DocumentoConvenio;

@Named("documentoConvenioController")
@ViewScoped
public class DocumentoConvenioController extends AbstractController<DocumentoConvenio> {

    @EJB
    private DocumentoConvenioFacade ejbFacade;
    private TipoDocumentoController tipoDocumentoController;
    private ReferenciaDocumentoController documentoController;
    private ConvenioComunaController convenioController;

    /**
     * Initialize the concrete DocumentoConvenio controller bean. The
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
        tipoDocumentoController = context.getApplication().evaluateExpressionGet(context, "#{tipoDocumentoController}", TipoDocumentoController.class);
        documentoController = context.getApplication().evaluateExpressionGet(context, "#{referenciaDocumentoController}", ReferenciaDocumentoController.class);
        convenioController = context.getApplication().evaluateExpressionGet(context, "#{convenioComunaController}", ConvenioComunaController.class);
    }

    public DocumentoConvenioController() {
        // Inform the Abstract parent controller of the concrete DocumentoConvenio?cap_first Entity
        super(DocumentoConvenio.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        tipoDocumentoController.setSelected(null);
        documentoController.setSelected(null);
        convenioController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the TipoDocumento controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareTipoDocumento(ActionEvent event) {
        if (this.getSelected() != null && tipoDocumentoController.getSelected() == null) {
            tipoDocumentoController.setSelected(this.getSelected().getTipoDocumento());
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
     * Sets the "selected" attribute of the ConvenioComuna controller in order
     * to display its data in a dialog. This is reusing existing the existing
     * View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareConvenio(ActionEvent event) {
        if (this.getSelected() != null && convenioController.getSelected() == null) {
            convenioController.setSelected(this.getSelected().getConvenio());
        }
    }
}
