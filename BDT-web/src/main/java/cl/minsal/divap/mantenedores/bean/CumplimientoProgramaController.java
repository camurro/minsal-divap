package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.CumplimientoProgramaFacade;
import cl.minsal.divap.model.CumplimientoPrograma;

@Named("cumplimientoProgramaController")
@ViewScoped
public class CumplimientoProgramaController extends AbstractController<CumplimientoPrograma> {

    @EJB
    private CumplimientoProgramaFacade ejbFacade;
    private ProgramaController programaController;

    /**
     * Initialize the concrete CumplimientoPrograma controller bean. The
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
    }

    public CumplimientoProgramaController() {
        // Inform the Abstract parent controller of the concrete CumplimientoPrograma?cap_first Entity
        super(CumplimientoPrograma.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        programaController.setSelected(null);
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
     * Sets the "items" attribute with a collection of ReliquidacionServicio
     * entities that are retrieved from CumplimientoPrograma?cap_first and
     * returns the navigation outcome.
     *
     * @return navigation outcome for ReliquidacionServicio page
     */
    public String navigateReliquidacionServicios() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ReliquidacionServicio_items", this.getSelected().getReliquidacionServicios());
        }
        return "/mantenedor/reliquidacionServicio/index";
    }

    /**
     * Sets the "items" attribute with a collection of ReliquidacionComuna
     * entities that are retrieved from CumplimientoPrograma?cap_first and
     * returns the navigation outcome.
     *
     * @return navigation outcome for ReliquidacionComuna page
     */
    public String navigateReliquidacionComunas() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ReliquidacionComuna_items", this.getSelected().getReliquidacionComunas());
        }
        return "/mantenedor/reliquidacionComuna/index";
    }

}
