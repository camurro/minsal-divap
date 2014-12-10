package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.MetadataCoreFacade;
import cl.minsal.divap.model.MetadataCore;

@Named("metadataCoreController")
@ViewScoped
public class MetadataCoreController extends AbstractController<MetadataCore> {

    @EJB
    private MetadataCoreFacade ejbFacade;
    private ProgramaController programaController;
    private TipoComponenteController idTipoProgramaController;

    /**
     * Initialize the concrete MetadataCore controller bean. The
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
        programaController = context.getApplication().evaluateExpressionGet(context, "#{programaController}", ProgramaController.class);
        idTipoProgramaController = context.getApplication().evaluateExpressionGet(context, "#{tipoComponenteController}", TipoComponenteController.class);
    }

    public MetadataCoreController() {
        // Inform the Abstract parent controller of the concrete MetadataCore?cap_first Entity
        super(MetadataCore.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        programaController.setSelected(null);
        idTipoProgramaController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the Programa controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void preparePrograma(ActionEvent event) {
        if (this.getSelected() != null && programaController.getSelected() == null) {
            programaController.setSelected(this.getSelected().getPrograma());
        }
    }

    /**
     * Sets the "selected" attribute of the TipoComponente controller in order
     * to display its data in a dialog. This is reusing existing the existing
     * View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareIdTipoPrograma(ActionEvent event) {
        if (this.getSelected() != null && idTipoProgramaController.getSelected() == null) {
            idTipoProgramaController.setSelected(this.getSelected().getIdTipoPrograma());
        }
    }
}
