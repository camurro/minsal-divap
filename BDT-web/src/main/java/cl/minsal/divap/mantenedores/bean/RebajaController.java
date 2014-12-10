package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.RebajaFacade;
import cl.minsal.divap.model.Rebaja;

@Named("rebajaController")
@ViewScoped
public class RebajaController extends AbstractController<Rebaja> {

    @EJB
    private RebajaFacade ejbFacade;
    private UsuarioController usuarioController;
    private RebajaCorteController rebajaCorteController;
    private AnoEnCursoController anoController;

    /**
     * Initialize the concrete Rebaja controller bean. The AbstractController
     * requires the EJB Facade object for most operations.
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
        rebajaCorteController = context.getApplication().evaluateExpressionGet(context, "#{rebajaCorteController}", RebajaCorteController.class);
        anoController = context.getApplication().evaluateExpressionGet(context, "#{anoEnCursoController}", AnoEnCursoController.class);
    }

    public RebajaController() {
        // Inform the Abstract parent controller of the concrete Rebaja?cap_first Entity
        super(Rebaja.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        usuarioController.setSelected(null);
        rebajaCorteController.setSelected(null);
        anoController.setSelected(null);
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
     * Sets the "selected" attribute of the RebajaCorte controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareRebajaCorte(ActionEvent event) {
        if (this.getSelected() != null && rebajaCorteController.getSelected() == null) {
            rebajaCorteController.setSelected(this.getSelected().getRebajaCorte());
        }
    }

    /**
     * Sets the "selected" attribute of the AnoEnCurso controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareAno(ActionEvent event) {
        if (this.getSelected() != null && anoController.getSelected() == null) {
            anoController.setSelected(this.getSelected().getAno());
        }
    }

    /**
     * Sets the "items" attribute with a collection of ReferenciaDocumento
     * entities that are retrieved from Rebaja?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for ReferenciaDocumento page
     */
    public String navigateReferenciaDocumentoCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ReferenciaDocumento_items", this.getSelected().getReferenciaDocumentoCollection());
        }
        return "/mantenedor/referenciaDocumento/index";
    }

    /**
     * Sets the "items" attribute with a collection of ComunaCumplimiento
     * entities that are retrieved from Rebaja?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for ComunaCumplimiento page
     */
    public String navigateComunaCumplimientos() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ComunaCumplimiento_items", this.getSelected().getComunaCumplimientos());
        }
        return "/mantenedor/comunaCumplimiento/index";
    }

    /**
     * Sets the "items" attribute with a collection of RebajaSeguimiento
     * entities that are retrieved from Rebaja?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for RebajaSeguimiento page
     */
    public String navigateRebajaSeguimientos() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("RebajaSeguimiento_items", this.getSelected().getRebajaSeguimientos());
        }
        return "/mantenedor/rebajaSeguimiento/index";
    }

}
