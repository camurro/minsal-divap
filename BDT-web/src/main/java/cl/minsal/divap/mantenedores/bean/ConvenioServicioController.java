package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.ConvenioServicioFacade;
import cl.minsal.divap.model.ConvenioServicio;

@Named("convenioServicioController")
@ViewScoped
public class ConvenioServicioController extends AbstractController<ConvenioServicio> {

    @EJB
    private ConvenioServicioFacade ejbFacade;
    private ProgramaAnoController idProgramaController;
    private MesController mesController;
    private EstablecimientoController idEstablecimientoController;

    /**
     * Initialize the concrete ConvenioServicio controller bean. The
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
        mesController = context.getApplication().evaluateExpressionGet(context, "#{mesController}", MesController.class);
        idEstablecimientoController = context.getApplication().evaluateExpressionGet(context, "#{establecimientoController}", EstablecimientoController.class);
    }

    public ConvenioServicioController() {
        // Inform the Abstract parent controller of the concrete ConvenioServicio?cap_first Entity
        super(ConvenioServicio.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        idProgramaController.setSelected(null);
        mesController.setSelected(null);
        idEstablecimientoController.setSelected(null);
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
     * Sets the "selected" attribute of the Mes controller in order to display
     * its data in a dialog. This is reusing existing the existing View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareMes(ActionEvent event) {
        if (this.getSelected() != null && mesController.getSelected() == null) {
            mesController.setSelected(this.getSelected().getMes());
        }
    }

    /**
     * Sets the "selected" attribute of the Establecimiento controller in order
     * to display its data in a dialog. This is reusing existing the existing
     * View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareIdEstablecimiento(ActionEvent event) {
        if (this.getSelected() != null && idEstablecimientoController.getSelected() == null) {
            idEstablecimientoController.setSelected(this.getSelected().getIdEstablecimiento());
        }
    }

    /**
     * Sets the "items" attribute with a collection of
     * ConvenioServicioComponente entities that are retrieved from
     * ConvenioServicio?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for ConvenioServicioComponente page
     */
    public String navigateConvenioServicioComponentes() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ConvenioServicioComponente_items", this.getSelected().getConvenioServicioComponentes());
        }
        return "/mantenedor/convenioServicioComponente/index";
    }

}
