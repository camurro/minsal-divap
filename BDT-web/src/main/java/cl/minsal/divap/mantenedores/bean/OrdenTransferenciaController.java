package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.OrdenTransferenciaFacade;
import cl.minsal.divap.model.OrdenTransferencia;

@Named("ordenTransferenciaController")
@ViewScoped
public class OrdenTransferenciaController extends AbstractController<OrdenTransferencia> {

    @EJB
    private OrdenTransferenciaFacade ejbFacade;
    private UsuarioController usuarioController;

    /**
     * Initialize the concrete OrdenTransferencia controller bean. The
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
        usuarioController = context.getApplication().evaluateExpressionGet(context, "#{usuarioController}", UsuarioController.class);
    }

    public OrdenTransferenciaController() {
        // Inform the Abstract parent controller of the concrete OrdenTransferencia?cap_first Entity
        super(OrdenTransferencia.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        usuarioController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the Usuario controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareUsuario(ActionEvent event) {
        if (this.getSelected() != null && usuarioController.getSelected() == null) {
            usuarioController.setSelected(this.getSelected().getUsuario());
        }
    }

    /**
     * Sets the "items" attribute with a collection of DocumentoOt entities that
     * are retrieved from OrdenTransferencia?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for DocumentoOt page
     */
    public String navigateDocumentoOtCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("DocumentoOt_items", this.getSelected().getDocumentoOtCollection());
        }
        return "/mantenedor/documentoOt/index";
    }

}
