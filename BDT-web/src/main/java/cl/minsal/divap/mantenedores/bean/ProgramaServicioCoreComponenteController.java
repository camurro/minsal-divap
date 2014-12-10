package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.ProgramaServicioCoreComponenteFacade;
import cl.minsal.divap.model.ProgramaServicioCoreComponente;

@Named("programaServicioCoreComponenteController")
@ViewScoped
public class ProgramaServicioCoreComponenteController extends AbstractController<ProgramaServicioCoreComponente> {

    @EJB
    private ProgramaServicioCoreComponenteFacade ejbFacade;
    private TipoSubtituloController subtituloController;
    private ProgramaServicioCoreController programaServicioCore1Controller;
    private ComponenteController servicioCoreComponenteController;

    /**
     * Initialize the concrete ProgramaServicioCoreComponente controller bean.
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
        programaServicioCore1Controller = context.getApplication().evaluateExpressionGet(context, "#{programaServicioCoreController}", ProgramaServicioCoreController.class);
        servicioCoreComponenteController = context.getApplication().evaluateExpressionGet(context, "#{componenteController}", ComponenteController.class);
    }

    public ProgramaServicioCoreComponenteController() {
        // Inform the Abstract parent controller of the concrete ProgramaServicioCoreComponente?cap_first Entity
        super(ProgramaServicioCoreComponente.class);
    }

    @Override
    protected void setEmbeddableKeys() {
        this.getSelected().getProgramaServicioCoreComponentePK().setProgramaServicioCore(this.getSelected().getProgramaServicioCore1().getIdProgramaServicioCore());
        this.getSelected().getProgramaServicioCoreComponentePK().setComponente(this.getSelected().getServicioCoreComponente().getId());
        this.getSelected().getProgramaServicioCoreComponentePK().setSubtitulo(this.getSelected().getSubtitulo().getIdTipoSubtitulo());
    }

    @Override
    protected void initializeEmbeddableKey() {
        this.getSelected().setProgramaServicioCoreComponentePK(new cl.minsal.divap.model.ProgramaServicioCoreComponentePK());
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        subtituloController.setSelected(null);
        programaServicioCore1Controller.setSelected(null);
        servicioCoreComponenteController.setSelected(null);
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
     * Sets the "selected" attribute of the ProgramaServicioCore controller in
     * order to display its data in a dialog. This is reusing existing the
     * existing View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareProgramaServicioCore1(ActionEvent event) {
        if (this.getSelected() != null && programaServicioCore1Controller.getSelected() == null) {
            programaServicioCore1Controller.setSelected(this.getSelected().getProgramaServicioCore1());
        }
    }

    /**
     * Sets the "selected" attribute of the Componente controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareServicioCoreComponente(ActionEvent event) {
        if (this.getSelected() != null && servicioCoreComponenteController.getSelected() == null) {
            servicioCoreComponenteController.setSelected(this.getSelected().getServicioCoreComponente());
        }
    }
}
