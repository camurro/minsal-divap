package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.ReporteEmailsEnviadosFacade;
import cl.minsal.divap.model.ReporteEmailsEnviados;

@Named("reporteEmailsEnviadosController")
@ViewScoped
public class ReporteEmailsEnviadosController extends AbstractController<ReporteEmailsEnviados> {

    @EJB
    private ReporteEmailsEnviadosFacade ejbFacade;
    private ServicioSaludController idServicioController;
    private ProgramaAnoController idProgramaAnoController;

    /**
     * Initialize the concrete ReporteEmailsEnviados controller bean. The
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
        idServicioController = context.getApplication().evaluateExpressionGet(context, "#{servicioSaludController}", ServicioSaludController.class);
        idProgramaAnoController = context.getApplication().evaluateExpressionGet(context, "#{programaAnoController}", ProgramaAnoController.class);
    }

    public ReporteEmailsEnviadosController() {
        // Inform the Abstract parent controller of the concrete ReporteEmailsEnviados?cap_first Entity
        super(ReporteEmailsEnviados.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        idServicioController.setSelected(null);
        idProgramaAnoController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the ServicioSalud controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareIdServicio(ActionEvent event) {
        if (this.getSelected() != null && idServicioController.getSelected() == null) {
            idServicioController.setSelected(this.getSelected().getIdServicio());
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

    /**
     * Sets the "items" attribute with a collection of
     * ReporteEmailsDestinatarios entities that are retrieved from
     * ReporteEmailsEnviados?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for ReporteEmailsDestinatarios page
     */
    public String navigateReporteEmailsDestinatariosSet() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ReporteEmailsDestinatarios_items", this.getSelected().getReporteEmailsDestinatariosSet());
        }
        return "/mantenedor/reporteEmailsDestinatarios/index";
    }

    /**
     * Sets the "items" attribute with a collection of ReporteEmailsAdjuntos
     * entities that are retrieved from ReporteEmailsEnviados?cap_first and
     * returns the navigation outcome.
     *
     * @return navigation outcome for ReporteEmailsAdjuntos page
     */
    public String navigateReporteEmailsAdjuntosSet() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ReporteEmailsAdjuntos_items", this.getSelected().getReporteEmailsAdjuntosSet());
        }
        return "/mantenedor/reporteEmailsAdjuntos/index";
    }

}
