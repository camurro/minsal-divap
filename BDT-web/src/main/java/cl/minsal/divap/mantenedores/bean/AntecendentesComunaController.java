package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.AntecendentesComunaFacade;
import cl.minsal.divap.model.AntecendentesComuna;

@Named("antecendentesComunaController")
@ViewScoped
public class AntecendentesComunaController extends AbstractController<AntecendentesComuna> {

    @EJB
    private AntecendentesComunaFacade ejbFacade;
    private TipoComunaController clasificacionController;
    private FactorRefAsigZonaController asignacionZonaController;
    private FactorTramoPobrezaController tramoPobrezaController;
    private ComunaController idComunaController;
    private AnoEnCursoController anoAnoEnCursoController;

    /**
     * Initialize the concrete AntecendentesComuna controller bean. The
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
        clasificacionController = context.getApplication().evaluateExpressionGet(context, "#{tipoComunaController}", TipoComunaController.class);
        asignacionZonaController = context.getApplication().evaluateExpressionGet(context, "#{factorRefAsigZonaController}", FactorRefAsigZonaController.class);
        tramoPobrezaController = context.getApplication().evaluateExpressionGet(context, "#{factorTramoPobrezaController}", FactorTramoPobrezaController.class);
        idComunaController = context.getApplication().evaluateExpressionGet(context, "#{comunaController}", ComunaController.class);
        anoAnoEnCursoController = context.getApplication().evaluateExpressionGet(context, "#{anoEnCursoController}", AnoEnCursoController.class);
    }

    public AntecendentesComunaController() {
        // Inform the Abstract parent controller of the concrete AntecendentesComuna?cap_first Entity
        super(AntecendentesComuna.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        clasificacionController.setSelected(null);
        asignacionZonaController.setSelected(null);
        tramoPobrezaController.setSelected(null);
        idComunaController.setSelected(null);
        anoAnoEnCursoController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the TipoComuna controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareClasificacion(ActionEvent event) {
        if (this.getSelected() != null && clasificacionController.getSelected() == null) {
            clasificacionController.setSelected(this.getSelected().getClasificacion());
        }
    }

    /**
     * Sets the "selected" attribute of the FactorRefAsigZona controller in
     * order to display its data in a dialog. This is reusing existing the
     * existing View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareAsignacionZona(ActionEvent event) {
        if (this.getSelected() != null && asignacionZonaController.getSelected() == null) {
            asignacionZonaController.setSelected(this.getSelected().getAsignacionZona());
        }
    }

    /**
     * Sets the "selected" attribute of the FactorTramoPobreza controller in
     * order to display its data in a dialog. This is reusing existing the
     * existing View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareTramoPobreza(ActionEvent event) {
        if (this.getSelected() != null && tramoPobrezaController.getSelected() == null) {
            tramoPobrezaController.setSelected(this.getSelected().getTramoPobreza());
        }
    }

    /**
     * Sets the "items" attribute with a collection of
     * AntecendentesComunaCalculado entities that are retrieved from
     * AntecendentesComuna?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for AntecendentesComunaCalculado page
     */
    public String navigateAntecendentesComunaCalculadoCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("AntecendentesComunaCalculado_items", this.getSelected().getAntecendentesComunaCalculadoCollection());
        }
        return "/mantenedor/antecendentesComunaCalculado/index";
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
     * Sets the "selected" attribute of the AnoEnCurso controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareAnoAnoEnCurso(ActionEvent event) {
        if (this.getSelected() != null && anoAnoEnCursoController.getSelected() == null) {
            anoAnoEnCursoController.setSelected(this.getSelected().getAnoAnoEnCurso());
        }
    }
}
