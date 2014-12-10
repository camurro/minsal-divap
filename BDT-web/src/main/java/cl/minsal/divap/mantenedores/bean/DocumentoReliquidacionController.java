package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.DocumentoReliquidacionFacade;
import cl.minsal.divap.model.DocumentoReliquidacion;

@Named("documentoReliquidacionController")
@ViewScoped
public class DocumentoReliquidacionController extends AbstractController<DocumentoReliquidacion> {

    @EJB
    private DocumentoReliquidacionFacade ejbFacade;
    private TipoDocumentoController idTipoDocumentoController;
    private ReliquidacionController idReliquidacionController;
    private ReferenciaDocumentoController idDocumentoController;

    /**
     * Initialize the concrete DocumentoReliquidacion controller bean. The
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
        idReliquidacionController = context.getApplication().evaluateExpressionGet(context, "#{reliquidacionController}", ReliquidacionController.class);
        idDocumentoController = context.getApplication().evaluateExpressionGet(context, "#{referenciaDocumentoController}", ReferenciaDocumentoController.class);
    }

    public DocumentoReliquidacionController() {
        // Inform the Abstract parent controller of the concrete DocumentoReliquidacion?cap_first Entity
        super(DocumentoReliquidacion.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        idTipoDocumentoController.setSelected(null);
        idReliquidacionController.setSelected(null);
        idDocumentoController.setSelected(null);
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
     * Sets the "selected" attribute of the Reliquidacion controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareIdReliquidacion(ActionEvent event) {
        if (this.getSelected() != null && idReliquidacionController.getSelected() == null) {
            idReliquidacionController.setSelected(this.getSelected().getIdReliquidacion());
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
}
