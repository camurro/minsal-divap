package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.DistribucionInicialPercapitaFacade;
import cl.minsal.divap.model.DistribucionInicialPercapita;

@Named("distribucionInicialPercapitaController")
@ViewScoped
public class DistribucionInicialPercapitaController extends AbstractController<DistribucionInicialPercapita> {

    @EJB
    private DistribucionInicialPercapitaFacade ejbFacade;
    private UsuarioController usuarioController;
    private AnoEnCursoController anoController;

    /**
     * Initialize the concrete DistribucionInicialPercapita controller bean. The
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
        usuarioController = context.getApplication().evaluateExpressionGet(context, "#{usuarioController}", UsuarioController.class);
        anoController = context.getApplication().evaluateExpressionGet(context, "#{anoEnCursoController}", AnoEnCursoController.class);
    }

    public DistribucionInicialPercapitaController() {
        // Inform the Abstract parent controller of the concrete DistribucionInicialPercapita?cap_first Entity
        super(DistribucionInicialPercapita.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        usuarioController.setSelected(null);
        anoController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the Usuario controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareUsuario(ActionEvent event) {
        if (this.getSelected() != null && usuarioController.getSelected() == null) {
            usuarioController.setSelected(this.getSelected().getUsuario());
        }
    }

    /**
     * Sets the "items" attribute with a collection of
     * DocumentoDistribucionInicialPercapita entities that are retrieved from
     * DistribucionInicialPercapita?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for DocumentoDistribucionInicialPercapita page
     */
    public String navigateDocumentoDistribucionInicialPercapitaCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("DocumentoDistribucionInicialPercapita_items", this.getSelected().getDocumentoDistribucionInicialPercapitaCollection());
        }
        return "/mantenedor/documentoDistribucionInicialPercapita/index";
    }

    /**
     * Sets the "items" attribute with a collection of
     * AntecendentesComunaCalculado entities that are retrieved from
     * DistribucionInicialPercapita?cap_first and returns the navigation
     * outcome.
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
}
