package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.ProgramaMunicipalCoreFacade;
import cl.minsal.divap.model.ProgramaMunicipalCore;

@Named("programaMunicipalCoreController")
@ViewScoped
public class ProgramaMunicipalCoreController extends AbstractController<ProgramaMunicipalCore> {

    @EJB
    private ProgramaMunicipalCoreFacade ejbFacade;
    private ProgramaAnoController programaAnoMunicipalController;
    private EstablecimientoController establecimientoController;
    private ComunaController comunaController;

    /**
     * Initialize the concrete ProgramaMunicipalCore controller bean. The
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
        programaAnoMunicipalController = context.getApplication().evaluateExpressionGet(context, "#{programaAnoController}", ProgramaAnoController.class);
        establecimientoController = context.getApplication().evaluateExpressionGet(context, "#{establecimientoController}", EstablecimientoController.class);
        comunaController = context.getApplication().evaluateExpressionGet(context, "#{comunaController}", ComunaController.class);
    }

    public ProgramaMunicipalCoreController() {
        // Inform the Abstract parent controller of the concrete ProgramaMunicipalCore?cap_first Entity
        super(ProgramaMunicipalCore.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        programaAnoMunicipalController.setSelected(null);
        establecimientoController.setSelected(null);
        comunaController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the ProgramaAno controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareProgramaAnoMunicipal(ActionEvent event) {
        if (this.getSelected() != null && programaAnoMunicipalController.getSelected() == null) {
            programaAnoMunicipalController.setSelected(this.getSelected().getProgramaAnoMunicipal());
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
     * Sets the "selected" attribute of the Comuna controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareComuna(ActionEvent event) {
        if (this.getSelected() != null && comunaController.getSelected() == null) {
            comunaController.setSelected(this.getSelected().getComuna());
        }
    }

    /**
     * Sets the "items" attribute with a collection of
     * ProgramaMunicipalCoreComponente entities that are retrieved from
     * ProgramaMunicipalCore?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for ProgramaMunicipalCoreComponente page
     */
    public String navigateProgramaMunicipalCoreComponentes() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ProgramaMunicipalCoreComponente_items", this.getSelected().getProgramaMunicipalCoreComponentes());
        }
        return "/mantenedor/programaMunicipalCoreComponente/index";
    }

}
