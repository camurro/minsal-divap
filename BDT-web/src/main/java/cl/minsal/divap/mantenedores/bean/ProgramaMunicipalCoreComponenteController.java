package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.ProgramaMunicipalCoreComponenteFacade;
import cl.minsal.divap.model.ProgramaMunicipalCoreComponente;

@Named("programaMunicipalCoreComponenteController")
@ViewScoped
public class ProgramaMunicipalCoreComponenteController extends AbstractController<ProgramaMunicipalCoreComponente> {

    @EJB
    private ProgramaMunicipalCoreComponenteFacade ejbFacade;
    private TipoSubtituloController subtituloController;
    private ProgramaMunicipalCoreController programaMunicipalCoreController;
    private ComponenteController municipalCoreComponenteController;

    /**
     * Initialize the concrete ProgramaMunicipalCoreComponente controller bean.
     * The AbstractController requires the EJB Facade object for most
     * operations.
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
        subtituloController = context.getApplication().evaluateExpressionGet(context, "#{tipoSubtituloController}", TipoSubtituloController.class);
        programaMunicipalCoreController = context.getApplication().evaluateExpressionGet(context, "#{programaMunicipalCoreController}", ProgramaMunicipalCoreController.class);
        municipalCoreComponenteController = context.getApplication().evaluateExpressionGet(context, "#{componenteController}", ComponenteController.class);
    }

    public ProgramaMunicipalCoreComponenteController() {
        // Inform the Abstract parent controller of the concrete ProgramaMunicipalCoreComponente?cap_first Entity
        super(ProgramaMunicipalCoreComponente.class);
    }

    @Override
    protected void setEmbeddableKeys() {
        this.getSelected().getProgramaMunicipalCoreComponentePK().setProgramaMunicipalCore(this.getSelected().getProgramaMunicipalCore().getIdProgramaMunicipalCore());
        this.getSelected().getProgramaMunicipalCoreComponentePK().setComponente(this.getSelected().getMunicipalCoreComponente().getId());
    }

    @Override
    protected void initializeEmbeddableKey() {
        this.getSelected().setProgramaMunicipalCoreComponentePK(new cl.minsal.divap.model.ProgramaMunicipalCoreComponentePK());
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        subtituloController.setSelected(null);
        programaMunicipalCoreController.setSelected(null);
        municipalCoreComponenteController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the TipoSubtitulo controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareSubtitulo(ActionEvent event) {
        if (this.getSelected() != null && subtituloController.getSelected() == null) {
            subtituloController.setSelected(this.getSelected().getSubtitulo());
        }
    }

    /**
     * Sets the "selected" attribute of the ProgramaMunicipalCore controller in
     * order to display its data in a dialog. This is reusing existing the
     * existing View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareProgramaMunicipalCore(ActionEvent event) {
        if (this.getSelected() != null && programaMunicipalCoreController.getSelected() == null) {
            programaMunicipalCoreController.setSelected(this.getSelected().getProgramaMunicipalCore());
        }
    }

    /**
     * Sets the "selected" attribute of the Componente controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareMunicipalCoreComponente(ActionEvent event) {
        if (this.getSelected() != null && municipalCoreComponenteController.getSelected() == null) {
            municipalCoreComponenteController.setSelected(this.getSelected().getMunicipalCoreComponente());
        }
    }
}
