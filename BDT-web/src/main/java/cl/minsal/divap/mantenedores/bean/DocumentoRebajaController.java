package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.DocumentoRebajaFacade;
import cl.minsal.divap.model.DocumentoRebaja;

@Named("documentoRebajaController")
@ViewScoped
public class DocumentoRebajaController extends AbstractController<DocumentoRebaja> {

    @EJB
    private DocumentoRebajaFacade ejbFacade;
    private TipoDocumentoController tipoDocumentoController;
    private ReferenciaDocumentoController documentoController;
    private RebajaController rebajaController;
    private ComunaController comunaController;

    /**
     * Initialize the concrete DocumentoRebaja controller bean. The
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
        rebajaController = context.getApplication().evaluateExpressionGet(context, "#{rebajaController}", RebajaController.class);
        comunaController = context.getApplication().evaluateExpressionGet(context, "#{comunaController}", ComunaController.class);
    }

    public DocumentoRebajaController() {
        // Inform the Abstract parent controller of the concrete DocumentoRebaja?cap_first Entity
        super(DocumentoRebaja.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        tipoDocumentoController.setSelected(null);
        documentoController.setSelected(null);
        rebajaController.setSelected(null);
        comunaController.setSelected(null);
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
}
