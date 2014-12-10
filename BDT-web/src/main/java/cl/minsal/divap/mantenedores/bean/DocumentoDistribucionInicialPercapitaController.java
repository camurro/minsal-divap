package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.DocumentoDistribucionInicialPercapitaFacade;
import cl.minsal.divap.model.DocumentoDistribucionInicialPercapita;

@Named("documentoDistribucionInicialPercapitaController")
@ViewScoped
public class DocumentoDistribucionInicialPercapitaController extends AbstractController<DocumentoDistribucionInicialPercapita> {

    @EJB
    private DocumentoDistribucionInicialPercapitaFacade ejbFacade;
    private ReferenciaDocumentoController idDocumentoController;
    private DistribucionInicialPercapitaController idDistribucionInicialPercapitaController;
    private TipoDocumentoController tipoDocumentoController;
    private ComunaController comunaController;

    /**
     * Initialize the concrete DocumentoDistribucionInicialPercapita controller
     * bean. The AbstractController requires the EJB Facade object for most
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
        idDocumentoController = context.getApplication().evaluateExpressionGet(context, "#{referenciaDocumentoController}", ReferenciaDocumentoController.class);
        idDistribucionInicialPercapitaController = context.getApplication().evaluateExpressionGet(context, "#{distribucionInicialPercapitaController}", DistribucionInicialPercapitaController.class);
        tipoDocumentoController = context.getApplication().evaluateExpressionGet(context, "#{tipoDocumentoController}", TipoDocumentoController.class);
        comunaController = context.getApplication().evaluateExpressionGet(context, "#{comunaController}", ComunaController.class);
    }

    public DocumentoDistribucionInicialPercapitaController() {
        // Inform the Abstract parent controller of the concrete DocumentoDistribucionInicialPercapita?cap_first Entity
        super(DocumentoDistribucionInicialPercapita.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        idDocumentoController.setSelected(null);
        idDistribucionInicialPercapitaController.setSelected(null);
        tipoDocumentoController.setSelected(null);
        comunaController.setSelected(null);
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
     * Sets the "selected" attribute of the DistribucionInicialPercapita
     * controller in order to display its data in a dialog. This is reusing
     * existing the existing View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareIdDistribucionInicialPercapita(ActionEvent event) {
        if (this.getSelected() != null && idDistribucionInicialPercapitaController.getSelected() == null) {
            idDistribucionInicialPercapitaController.setSelected(this.getSelected().getIdDistribucionInicialPercapita());
        }
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
