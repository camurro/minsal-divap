package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.ConvenioComunaFacade;
import cl.minsal.divap.model.ConvenioComuna;

@Named("convenioComunaController")
@ViewScoped
public class ConvenioComunaController extends AbstractController<ConvenioComuna> {

    @EJB
    private ConvenioComunaFacade ejbFacade;
    private ProgramaAnoController idProgramaController;
    private ComunaController idComunaController;

    /**
     * Initialize the concrete ConvenioComuna controller bean. The
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
        idProgramaController = context.getApplication().evaluateExpressionGet(context, "#{programaAnoController}", ProgramaAnoController.class);
        idComunaController = context.getApplication().evaluateExpressionGet(context, "#{comunaController}", ComunaController.class);
    }

    public ConvenioComunaController() {
        // Inform the Abstract parent controller of the concrete ConvenioComuna?cap_first Entity
        super(ConvenioComuna.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        idProgramaController.setSelected(null);
        idComunaController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the ProgramaAno controller in order to
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
     * Sets the "selected" attribute of the Comuna controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareIdComuna(ActionEvent event) {
        if (this.getSelected() != null && idComunaController.getSelected() == null) {
            idComunaController.setSelected(this.getSelected().getIdComuna());
        }
    }

    /**
     * Sets the "items" attribute with a collection of ConvenioComunaComponente
     * entities that are retrieved from ConvenioComuna?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for ConvenioComunaComponente page
     */
    public String navigateConvenioComunaComponentes() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ConvenioComunaComponente_items", this.getSelected().getConvenioComunaComponentes());
        }
        return "/mantenedor/convenioComunaComponente/index";
    }

    /**
     * Sets the "items" attribute with a collection of DocumentoConvenio
     * entities that are retrieved from ConvenioComuna?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for DocumentoConvenio page
     */
    public String navigateDocumentosConvenio() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("DocumentoConvenio_items", this.getSelected().getDocumentosConvenio());
        }
        return "/mantenedor/documentoConvenio/index";
    }

}
