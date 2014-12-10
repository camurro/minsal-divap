package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.ProgramaServicioCoreFacade;
import cl.minsal.divap.model.ProgramaServicioCore;

@Named("programaServicioCoreController")
@ViewScoped
public class ProgramaServicioCoreController extends AbstractController<ProgramaServicioCore> {

    @EJB
    private ProgramaServicioCoreFacade ejbFacade;
    private ProgramaAnoController programaAnoServicioController;
    private EstablecimientoController establecimientoController;
    private ServicioSaludController servicioController;

    /**
     * Initialize the concrete ProgramaServicioCore controller bean. The
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
        programaAnoServicioController = context.getApplication().evaluateExpressionGet(context, "#{programaAnoController}", ProgramaAnoController.class);
        establecimientoController = context.getApplication().evaluateExpressionGet(context, "#{establecimientoController}", EstablecimientoController.class);
        servicioController = context.getApplication().evaluateExpressionGet(context, "#{servicioSaludController}", ServicioSaludController.class);
    }

    public ProgramaServicioCoreController() {
        // Inform the Abstract parent controller of the concrete ProgramaServicioCore?cap_first Entity
        super(ProgramaServicioCore.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        programaAnoServicioController.setSelected(null);
        establecimientoController.setSelected(null);
        servicioController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the ProgramaAno controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareProgramaAnoServicio(ActionEvent event) {
        if (this.getSelected() != null && programaAnoServicioController.getSelected() == null) {
            programaAnoServicioController.setSelected(this.getSelected().getProgramaAnoServicio());
        }
    }

    /**
     * Sets the "selected" attribute of the Establecimiento controller in order
     * to display its data in a dialog. This is reusing existing the existing
     * View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareEstablecimiento(ActionEvent event) {
        if (this.getSelected() != null && establecimientoController.getSelected() == null) {
            establecimientoController.setSelected(this.getSelected().getEstablecimiento());
        }
    }

    /**
     * Sets the "selected" attribute of the ServicioSalud controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareServicio(ActionEvent event) {
        if (this.getSelected() != null && servicioController.getSelected() == null) {
            servicioController.setSelected(this.getSelected().getServicio());
        }
    }

    /**
     * Sets the "items" attribute with a collection of
     * ProgramaServicioCoreComponente entities that are retrieved from
     * ProgramaServicioCore?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for ProgramaServicioCoreComponente page
     */
    public String navigateProgramaServicioCoreComponentes() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ProgramaServicioCoreComponente_items", this.getSelected().getProgramaServicioCoreComponentes());
        }
        return "/mantenedor/programaServicioCoreComponente/index";
    }

}
