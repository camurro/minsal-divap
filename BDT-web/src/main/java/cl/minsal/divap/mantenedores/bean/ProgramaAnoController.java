package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.ProgramaAnoFacade;
import cl.minsal.divap.model.ProgramaAno;

@Named("programaAnoController")
@ViewScoped
public class ProgramaAnoController extends AbstractController<ProgramaAno> {

    @EJB
    private ProgramaAnoFacade ejbFacade;
    private ProgramaController programaController;
    private EstadoProgramaController estadoController;
    private EstadoProgramaController estadoreliquidacionController;
    private EstadoProgramaController estadoConvenioController;
    private AnoEnCursoController anoController;
    private EstadoProgramaController estadoFlujoCajaController;

    /**
     * Initialize the concrete ProgramaAno controller bean. The
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
        estadoController = context.getApplication().evaluateExpressionGet(context, "#{estadoProgramaController}", EstadoProgramaController.class);
        estadoreliquidacionController = context.getApplication().evaluateExpressionGet(context, "#{estadoProgramaController}", EstadoProgramaController.class);
        estadoConvenioController = context.getApplication().evaluateExpressionGet(context, "#{estadoProgramaController}", EstadoProgramaController.class);
        anoController = context.getApplication().evaluateExpressionGet(context, "#{anoEnCursoController}", AnoEnCursoController.class);
        estadoFlujoCajaController = context.getApplication().evaluateExpressionGet(context, "#{estadoProgramaController}", EstadoProgramaController.class);
    }

    public ProgramaAnoController() {
        // Inform the Abstract parent controller of the concrete ProgramaAno?cap_first Entity
        super(ProgramaAno.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        programaController.setSelected(null);
        estadoController.setSelected(null);
        estadoreliquidacionController.setSelected(null);
        estadoConvenioController.setSelected(null);
        anoController.setSelected(null);
        estadoFlujoCajaController.setSelected(null);
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
     * Sets the "selected" attribute of the EstadoPrograma controller in order
     * to display its data in a dialog. This is reusing existing the existing
     * View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareEstado(ActionEvent event) {
        if (this.getSelected() != null && estadoController.getSelected() == null) {
            estadoController.setSelected(this.getSelected().getEstado());
        }
    }

    /**
     * Sets the "selected" attribute of the EstadoPrograma controller in order
     * to display its data in a dialog. This is reusing existing the existing
     * View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareEstadoreliquidacion(ActionEvent event) {
        if (this.getSelected() != null && estadoreliquidacionController.getSelected() == null) {
            estadoreliquidacionController.setSelected(this.getSelected().getEstadoreliquidacion());
        }
    }

    /**
     * Sets the "selected" attribute of the EstadoPrograma controller in order
     * to display its data in a dialog. This is reusing existing the existing
     * View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareEstadoConvenio(ActionEvent event) {
        if (this.getSelected() != null && estadoConvenioController.getSelected() == null) {
            estadoConvenioController.setSelected(this.getSelected().getEstadoConvenio());
        }
    }

    /**
     * Sets the "selected" attribute of the AnoEnCurso controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareAno(ActionEvent event) {
        if (this.getSelected() != null && anoController.getSelected() == null) {
            anoController.setSelected(this.getSelected().getAno());
        }
    }

    /**
     * Sets the "selected" attribute of the EstadoPrograma controller in order
     * to display its data in a dialog. This is reusing existing the existing
     * View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareEstadoFlujoCaja(ActionEvent event) {
        if (this.getSelected() != null && estadoFlujoCajaController.getSelected() == null) {
            estadoFlujoCajaController.setSelected(this.getSelected().getEstadoFlujoCaja());
        }
    }

    /**
     * Sets the "items" attribute with a collection of ProgramaMunicipalCore
     * entities that are retrieved from ProgramaAno?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for ProgramaMunicipalCore page
     */
    public String navigateProgramasMunicipalesCore() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ProgramaMunicipalCore_items", this.getSelected().getProgramasMunicipalesCore());
        }
        return "/mantenedor/programaMunicipalCore/index";
    }

    /**
     * Sets the "items" attribute with a collection of ProgramaServicioCore
     * entities that are retrieved from ProgramaAno?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for ProgramaServicioCore page
     */
    public String navigateProgramasServiciosCore() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ProgramaServicioCore_items", this.getSelected().getProgramasServiciosCore());
        }
        return "/mantenedor/programaServicioCore/index";
    }

    /**
     * Sets the "items" attribute with a collection of Cuota entities that are
     * retrieved from ProgramaAno?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Cuota page
     */
    public String navigateCuotas() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Cuota_items", this.getSelected().getCuotas());
        }
        return "/mantenedor/cuota/index";
    }

    /**
     * Sets the "items" attribute with a collection of MarcoPresupuestario
     * entities that are retrieved from ProgramaAno?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for MarcoPresupuestario page
     */
    public String navigateMarcosPresupuestarios() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("MarcoPresupuestario_items", this.getSelected().getMarcosPresupuestarios());
        }
        return "/mantenedor/marcoPresupuestario/index";
    }

    /**
     * Sets the "items" attribute with a collection of ConvenioComuna entities
     * that are retrieved from ProgramaAno?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for ConvenioComuna page
     */
    public String navigateConvenios() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ConvenioComuna_items", this.getSelected().getConvenios());
        }
        return "/mantenedor/convenioComuna/index";
    }

    /**
     * Sets the "items" attribute with a collection of Remesa entities that are
     * retrieved from ProgramaAno?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Remesa page
     */
    public String navigateRemesaCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Remesa_items", this.getSelected().getRemesaCollection());
        }
        return "/mantenedor/remesa/index";
    }

}
