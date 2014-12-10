package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.MarcoPresupuestarioFacade;
import cl.minsal.divap.model.MarcoPresupuestario;

@Named("marcoPresupuestarioController")
@ViewScoped
public class MarcoPresupuestarioController extends AbstractController<MarcoPresupuestario> {

    @EJB
    private MarcoPresupuestarioFacade ejbFacade;
    private ServicioSaludController servicioSaludController;
    private ProgramaAnoController idProgramaAnoController;

    /**
     * Initialize the concrete MarcoPresupuestario controller bean. The
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
        servicioSaludController = context.getApplication().evaluateExpressionGet(context, "#{servicioSaludController}", ServicioSaludController.class);
        idProgramaAnoController = context.getApplication().evaluateExpressionGet(context, "#{programaAnoController}", ProgramaAnoController.class);
    }

    public MarcoPresupuestarioController() {
        // Inform the Abstract parent controller of the concrete MarcoPresupuestario?cap_first Entity
        super(MarcoPresupuestario.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        servicioSaludController.setSelected(null);
        idProgramaAnoController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the ServicioSalud controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareServicioSalud(ActionEvent event) {
        if (this.getSelected() != null && servicioSaludController.getSelected() == null) {
            servicioSaludController.setSelected(this.getSelected().getServicioSalud());
        }
    }

    /**
     * Sets the "selected" attribute of the ProgramaAno controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareIdProgramaAno(ActionEvent event) {
        if (this.getSelected() != null && idProgramaAnoController.getSelected() == null) {
            idProgramaAnoController.setSelected(this.getSelected().getIdProgramaAno());
        }
    }
}
