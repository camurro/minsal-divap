package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.ConvenioServicioComponenteFacade;
import cl.minsal.divap.model.ConvenioServicioComponente;

@Named("convenioServicioComponenteController")
@ViewScoped
public class ConvenioServicioComponenteController extends AbstractController<ConvenioServicioComponente> {

    @EJB
    private ConvenioServicioComponenteFacade ejbFacade;
    private TipoSubtituloController subtituloController;
    private ConvenioServicioController convenioServicioController;
    private ComponenteController componenteController;

    /**
     * Initialize the concrete ConvenioServicioComponente controller bean. The
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
        subtituloController = context.getApplication().evaluateExpressionGet(context, "#{tipoSubtituloController}", TipoSubtituloController.class);
        convenioServicioController = context.getApplication().evaluateExpressionGet(context, "#{convenioServicioController}", ConvenioServicioController.class);
        componenteController = context.getApplication().evaluateExpressionGet(context, "#{componenteController}", ComponenteController.class);
    }

    public ConvenioServicioComponenteController() {
        // Inform the Abstract parent controller of the concrete ConvenioServicioComponente?cap_first Entity
        super(ConvenioServicioComponente.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        subtituloController.setSelected(null);
        convenioServicioController.setSelected(null);
        componenteController.setSelected(null);
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
     * Sets the "selected" attribute of the ConvenioServicio controller in order
     * to display its data in a dialog. This is reusing existing the existing
     * View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareConvenioServicio(ActionEvent event) {
        if (this.getSelected() != null && convenioServicioController.getSelected() == null) {
            convenioServicioController.setSelected(this.getSelected().getConvenioServicio());
        }
    }

    /**
     * Sets the "selected" attribute of the Componente controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareComponente(ActionEvent event) {
        if (this.getSelected() != null && componenteController.getSelected() == null) {
            componenteController.setSelected(this.getSelected().getComponente());
        }
    }
}
