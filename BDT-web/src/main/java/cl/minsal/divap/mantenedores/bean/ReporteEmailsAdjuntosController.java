package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.ReporteEmailsAdjuntosFacade;
import cl.minsal.divap.model.ReporteEmailsAdjuntos;

@Named("reporteEmailsAdjuntosController")
@ViewScoped
public class ReporteEmailsAdjuntosController extends AbstractController<ReporteEmailsAdjuntos> {

    @EJB
    private ReporteEmailsAdjuntosFacade ejbFacade;
    private ReporteEmailsEnviadosController reporteEmailsEnviadoController;
    private ReferenciaDocumentoController documentoController;

    /**
     * Initialize the concrete ReporteEmailsAdjuntos controller bean. The
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
        reporteEmailsEnviadoController = context.getApplication().evaluateExpressionGet(context, "#{reporteEmailsEnviadosController}", ReporteEmailsEnviadosController.class);
        documentoController = context.getApplication().evaluateExpressionGet(context, "#{referenciaDocumentoController}", ReferenciaDocumentoController.class);
    }

    public ReporteEmailsAdjuntosController() {
        // Inform the Abstract parent controller of the concrete ReporteEmailsAdjuntos?cap_first Entity
        super(ReporteEmailsAdjuntos.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        reporteEmailsEnviadoController.setSelected(null);
        documentoController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the ReporteEmailsEnviados controller in
     * order to display its data in a dialog. This is reusing existing the
     * existing View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareReporteEmailsEnviado(ActionEvent event) {
        if (this.getSelected() != null && reporteEmailsEnviadoController.getSelected() == null) {
            reporteEmailsEnviadoController.setSelected(this.getSelected().getReporteEmailsEnviado());
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
