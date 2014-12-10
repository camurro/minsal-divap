package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.SeguimientoReferenciaDocumentoFacade;
import cl.minsal.divap.model.SeguimientoReferenciaDocumento;

@Named("seguimientoReferenciaDocumentoController")
@ViewScoped
public class SeguimientoReferenciaDocumentoController extends AbstractController<SeguimientoReferenciaDocumento> {

    @EJB
    private SeguimientoReferenciaDocumentoFacade ejbFacade;
    private SeguimientoController idSeguimientoController;
    private ReferenciaDocumentoController idReferenciaDocumentoController;

    /**
     * Initialize the concrete SeguimientoReferenciaDocumento controller bean.
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
        idSeguimientoController = context.getApplication().evaluateExpressionGet(context, "#{seguimientoController}", SeguimientoController.class);
        idReferenciaDocumentoController = context.getApplication().evaluateExpressionGet(context, "#{referenciaDocumentoController}", ReferenciaDocumentoController.class);
    }

    public SeguimientoReferenciaDocumentoController() {
        // Inform the Abstract parent controller of the concrete SeguimientoReferenciaDocumento?cap_first Entity
        super(SeguimientoReferenciaDocumento.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        idSeguimientoController.setSelected(null);
        idReferenciaDocumentoController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the Seguimiento controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareIdSeguimiento(ActionEvent event) {
        if (this.getSelected() != null && idSeguimientoController.getSelected() == null) {
            idSeguimientoController.setSelected(this.getSelected().getIdSeguimiento());
        }
    }

    /**
     * Sets the "selected" attribute of the ReferenciaDocumento controller in
     * order to display its data in a dialog. This is reusing existing the
     * existing View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareIdReferenciaDocumento(ActionEvent event) {
        if (this.getSelected() != null && idReferenciaDocumentoController.getSelected() == null) {
            idReferenciaDocumentoController.setSelected(this.getSelected().getIdReferenciaDocumento());
        }
    }
}
