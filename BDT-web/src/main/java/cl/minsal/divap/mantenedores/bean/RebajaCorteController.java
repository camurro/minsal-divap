package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.RebajaCorteFacade;
import cl.minsal.divap.model.RebajaCorte;

@Named("rebajaCorteController")
@ViewScoped
public class RebajaCorteController extends AbstractController<RebajaCorte> {

    @EJB
    private RebajaCorteFacade ejbFacade;
    private MesController mesRebajaController;
    private MesController mesHastaController;
    private MesController mesDesdeController;
    private MesController mesInicioController;

    /**
     * Initialize the concrete RebajaCorte controller bean. The
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
        mesRebajaController = context.getApplication().evaluateExpressionGet(context, "#{mesController}", MesController.class);
        mesHastaController = context.getApplication().evaluateExpressionGet(context, "#{mesController}", MesController.class);
        mesDesdeController = context.getApplication().evaluateExpressionGet(context, "#{mesController}", MesController.class);
        mesInicioController = context.getApplication().evaluateExpressionGet(context, "#{mesController}", MesController.class);
    }

    public RebajaCorteController() {
        // Inform the Abstract parent controller of the concrete RebajaCorte?cap_first Entity
        super(RebajaCorte.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        mesRebajaController.setSelected(null);
        mesHastaController.setSelected(null);
        mesDesdeController.setSelected(null);
        mesInicioController.setSelected(null);
    }

    /**
     * Sets the "items" attribute with a collection of Rebaja entities that are
     * retrieved from RebajaCorte?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Rebaja page
     */
    public String navigateRebajas() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Rebaja_items", this.getSelected().getRebajas());
        }
        return "/mantenedor/rebaja/index";
    }

    /**
     * Sets the "selected" attribute of the Mes controller in order to display
     * its data in a dialog. This is reusing existing the existing View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareMesRebaja(ActionEvent event) {
        if (this.getSelected() != null && mesRebajaController.getSelected() == null) {
            mesRebajaController.setSelected(this.getSelected().getMesRebaja());
        }
    }

    /**
     * Sets the "selected" attribute of the Mes controller in order to display
     * its data in a dialog. This is reusing existing the existing View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareMesHasta(ActionEvent event) {
        if (this.getSelected() != null && mesHastaController.getSelected() == null) {
            mesHastaController.setSelected(this.getSelected().getMesHasta());
        }
    }

    /**
     * Sets the "selected" attribute of the Mes controller in order to display
     * its data in a dialog. This is reusing existing the existing View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareMesDesde(ActionEvent event) {
        if (this.getSelected() != null && mesDesdeController.getSelected() == null) {
            mesDesdeController.setSelected(this.getSelected().getMesDesde());
        }
    }

    /**
     * Sets the "selected" attribute of the Mes controller in order to display
     * its data in a dialog. This is reusing existing the existing View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareMesInicio(ActionEvent event) {
        if (this.getSelected() != null && mesInicioController.getSelected() == null) {
            mesInicioController.setSelected(this.getSelected().getMesInicio());
        }
    }
}
