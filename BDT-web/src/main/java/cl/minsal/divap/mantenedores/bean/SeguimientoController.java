package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.SeguimientoFacade;
import cl.minsal.divap.model.Seguimiento;

@Named("seguimientoController")
@ViewScoped
public class SeguimientoController extends AbstractController<Seguimiento> {

    @EJB
    private SeguimientoFacade ejbFacade;
    private TareaSeguimientoController tareaSeguimientoController;
    private ProgramaController idProgramaController;
    private EmailController mailFromController;

    /**
     * Initialize the concrete Seguimiento controller bean. The
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
        tareaSeguimientoController = context.getApplication().evaluateExpressionGet(context, "#{tareaSeguimientoController}", TareaSeguimientoController.class);
        idProgramaController = context.getApplication().evaluateExpressionGet(context, "#{programaController}", ProgramaController.class);
        mailFromController = context.getApplication().evaluateExpressionGet(context, "#{emailController}", EmailController.class);
    }

    public SeguimientoController() {
        // Inform the Abstract parent controller of the concrete Seguimiento?cap_first Entity
        super(Seguimiento.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        tareaSeguimientoController.setSelected(null);
        idProgramaController.setSelected(null);
        mailFromController.setSelected(null);
    }

    /**
     * Sets the "items" attribute with a collection of
     * SeguimientoReferenciaDocumento entities that are retrieved from
     * Seguimiento?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for SeguimientoReferenciaDocumento page
     */
    public String navigateSeguimientoReferenciaDocumentoCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("SeguimientoReferenciaDocumento_items", this.getSelected().getSeguimientoReferenciaDocumentoCollection());
        }
        return "/mantenedor/seguimientoReferenciaDocumento/index";
    }

    /**
     * Sets the "items" attribute with a collection of
     * DistribucionInicialPercapitaSeguimiento entities that are retrieved from
     * Seguimiento?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for DistribucionInicialPercapitaSeguimiento
     * page
     */
    public String navigateDistribucionInicialPercapitaSeguimientoCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("DistribucionInicialPercapitaSeguimiento_items", this.getSelected().getDistribucionInicialPercapitaSeguimientoCollection());
        }
        return "/mantenedor/distribucionInicialPercapitaSeguimiento/index";
    }

    /**
     * Sets the "items" attribute with a collection of
     * ProgramasReforzamientoSeguimiento entities that are retrieved from
     * Seguimiento?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for ProgramasReforzamientoSeguimiento page
     */
    public String navigateProgramasReforzamientoSeguimientoCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ProgramasReforzamientoSeguimiento_items", this.getSelected().getProgramasReforzamientoSeguimientoCollection());
        }
        return "/mantenedor/programasReforzamientoSeguimiento/index";
    }

    /**
     * Sets the "items" attribute with a collection of OtSeguimiento entities
     * that are retrieved from Seguimiento?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for OtSeguimiento page
     */
    public String navigateOtSeguimientoCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("OtSeguimiento_items", this.getSelected().getOtSeguimientoCollection());
        }
        return "/mantenedor/otSeguimiento/index";
    }

    /**
     * Sets the "items" attribute with a collection of AdjuntosSeguimiento
     * entities that are retrieved from Seguimiento?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for AdjuntosSeguimiento page
     */
    public String navigateAdjuntosSeguimientoCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("AdjuntosSeguimiento_items", this.getSelected().getAdjuntosSeguimientoCollection());
        }
        return "/mantenedor/adjuntosSeguimiento/index";
    }

    /**
     * Sets the "items" attribute with a collection of Destinatarios entities
     * that are retrieved from Seguimiento?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for Destinatarios page
     */
    public String navigateDestinatariosCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Destinatarios_items", this.getSelected().getDestinatariosCollection());
        }
        return "/mantenedor/destinatarios/index";
    }

    /**
     * Sets the "selected" attribute of the TareaSeguimiento controller in order
     * to display its data in a dialog. This is reusing existing the existing
     * View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareTareaSeguimiento(ActionEvent event) {
        if (this.getSelected() != null && tareaSeguimientoController.getSelected() == null) {
            tareaSeguimientoController.setSelected(this.getSelected().getTareaSeguimiento());
        }
    }

    /**
     * Sets the "selected" attribute of the Programa controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareIdPrograma(ActionEvent event) {
        if (this.getSelected() != null && idProgramaController.getSelected() == null) {
            idProgramaController.setSelected(this.getSelected().getIdPrograma());
        }
    }

    /**
     * Sets the "selected" attribute of the Email controller in order to display
     * its data in a dialog. This is reusing existing the existing View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareMailFrom(ActionEvent event) {
        if (this.getSelected() != null && mailFromController.getSelected() == null) {
            mailFromController.setSelected(this.getSelected().getMailFrom());
        }
    }

    /**
     * Sets the "items" attribute with a collection of RebajaSeguimiento
     * entities that are retrieved from Seguimiento?cap_first and returns the
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
