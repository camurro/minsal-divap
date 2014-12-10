package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.ConvenioComunaComponenteFacade;
import cl.minsal.divap.model.ConvenioComunaComponente;

@Named("convenioComunaComponenteController")
@ViewScoped
public class ConvenioComunaComponenteController extends AbstractController<ConvenioComunaComponente> {

    @EJB
    private ConvenioComunaComponenteFacade ejbFacade;
    private TipoSubtituloController subtituloController;
    private ConvenioComunaController convenioComunaController;
    private ComponenteController componenteController;

    /**
     * Initialize the concrete ConvenioComunaComponente controller bean. The
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
        convenioComunaController = context.getApplication().evaluateExpressionGet(context, "#{convenioComunaController}", ConvenioComunaController.class);
        componenteController = context.getApplication().evaluateExpressionGet(context, "#{componenteController}", ComponenteController.class);
    }

    public ConvenioComunaComponenteController() {
        // Inform the Abstract parent controller of the concrete ConvenioComunaComponente?cap_first Entity
        super(ConvenioComunaComponente.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        subtituloController.setSelected(null);
        convenioComunaController.setSelected(null);
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
     * Sets the "selected" attribute of the ConvenioComuna controller in order
     * to display its data in a dialog. This is reusing existing the existing
     * View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareConvenioComuna(ActionEvent event) {
        if (this.getSelected() != null && convenioComunaController.getSelected() == null) {
            convenioComunaController.setSelected(this.getSelected().getConvenioComuna());
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
