package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.ReporteEmailsDestinatariosFacade;
import cl.minsal.divap.model.ReporteEmailsDestinatarios;

@Named("reporteEmailsDestinatariosController")
@ViewScoped
public class ReporteEmailsDestinatariosController extends AbstractController<ReporteEmailsDestinatarios> {

    @EJB
    private ReporteEmailsDestinatariosFacade ejbFacade;
    private TipoDestinatarioController tipoDestinatarioController;
    private ReporteEmailsEnviadosController reporteEmailsEnviadoController;
    private PersonaController destinatarioController;

    /**
     * Initialize the concrete ReporteEmailsDestinatarios controller bean. The
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
        tipoDestinatarioController = context.getApplication().evaluateExpressionGet(context, "#{tipoDestinatarioController}", TipoDestinatarioController.class);
        reporteEmailsEnviadoController = context.getApplication().evaluateExpressionGet(context, "#{reporteEmailsEnviadosController}", ReporteEmailsEnviadosController.class);
        destinatarioController = context.getApplication().evaluateExpressionGet(context, "#{personaController}", PersonaController.class);
    }

    public ReporteEmailsDestinatariosController() {
        // Inform the Abstract parent controller of the concrete ReporteEmailsDestinatarios?cap_first Entity
        super(ReporteEmailsDestinatarios.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        tipoDestinatarioController.setSelected(null);
        reporteEmailsEnviadoController.setSelected(null);
        destinatarioController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the TipoDestinatario controller in order
     * to display its data in a dialog. This is reusing existing the existing
     * View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareTipoDestinatario(ActionEvent event) {
        if (this.getSelected() != null && tipoDestinatarioController.getSelected() == null) {
            tipoDestinatarioController.setSelected(this.getSelected().getTipoDestinatario());
        }
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
     * Sets the "selected" attribute of the Persona controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareDestinatario(ActionEvent event) {
        if (this.getSelected() != null && destinatarioController.getSelected() == null) {
            destinatarioController.setSelected(this.getSelected().getDestinatario());
        }
    }
}
