package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.DestinatariosFacade;
import cl.minsal.divap.model.Destinatarios;

@Named("destinatariosController")
@ViewScoped
public class DestinatariosController extends AbstractController<Destinatarios> {

    @EJB
    private DestinatariosFacade ejbFacade;
    private TipoDestinatarioController tipoDestinatarioController;
    private EmailController destinatarioController;
    private SeguimientoController seguimientoController;

    /**
     * Initialize the concrete Destinatarios controller bean. The
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
        destinatarioController = context.getApplication().evaluateExpressionGet(context, "#{emailController}", EmailController.class);
        seguimientoController = context.getApplication().evaluateExpressionGet(context, "#{seguimientoController}", SeguimientoController.class);
    }

    public DestinatariosController() {
        // Inform the Abstract parent controller of the concrete Destinatarios?cap_first Entity
        super(Destinatarios.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        tipoDestinatarioController.setSelected(null);
        destinatarioController.setSelected(null);
        seguimientoController.setSelected(null);
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
     * Sets the "selected" attribute of the Email controller in order to display
     * its data in a dialog. This is reusing existing the existing View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareDestinatario(ActionEvent event) {
        if (this.getSelected() != null && destinatarioController.getSelected() == null) {
            destinatarioController.setSelected(this.getSelected().getDestinatario());
        }
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
}
