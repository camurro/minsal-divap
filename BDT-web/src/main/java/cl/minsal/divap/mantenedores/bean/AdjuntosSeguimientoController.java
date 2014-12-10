package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.AdjuntosSeguimientoFacade;
import cl.minsal.divap.model.AdjuntosSeguimiento;

@Named("adjuntosSeguimientoController")
@ViewScoped
public class AdjuntosSeguimientoController extends AbstractController<AdjuntosSeguimiento> {

    @EJB
    private AdjuntosSeguimientoFacade ejbFacade;
    private SeguimientoController seguimientoController;
    private ReferenciaDocumentoController documentoController;

    /**
     * Initialize the concrete AdjuntosSeguimiento controller bean. The
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
        seguimientoController = context.getApplication().evaluateExpressionGet(context, "#{seguimientoController}", SeguimientoController.class);
        documentoController = context.getApplication().evaluateExpressionGet(context, "#{referenciaDocumentoController}", ReferenciaDocumentoController.class);
    }

    public AdjuntosSeguimientoController() {
        // Inform the Abstract parent controller of the concrete AdjuntosSeguimiento?cap_first Entity
        super(AdjuntosSeguimiento.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        seguimientoController.setSelected(null);
        documentoController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the Seguimiento controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareSeguimiento(ActionEvent event) {
        if (this.getSelected() != null && seguimientoController.getSelected() == null) {
            seguimientoController.setSelected(this.getSelected().getSeguimiento());
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
}
