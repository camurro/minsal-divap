package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.ReliquidacionFacade;
import cl.minsal.divap.model.Reliquidacion;

@Named("reliquidacionController")
@ViewScoped
public class ReliquidacionController extends AbstractController<Reliquidacion> {

    @EJB
    private ReliquidacionFacade ejbFacade;
    private UsuarioController usuarioController;
    private ProgramaAnoController idProgramaAnoController;
    private MesController mesController;

    /**
     * Initialize the concrete Reliquidacion controller bean. The
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
        idProgramaAnoController = context.getApplication().evaluateExpressionGet(context, "#{programaAnoController}", ProgramaAnoController.class);
        mesController = context.getApplication().evaluateExpressionGet(context, "#{mesController}", MesController.class);
    }

    public ReliquidacionController() {
        // Inform the Abstract parent controller of the concrete Reliquidacion?cap_first Entity
        super(Reliquidacion.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        usuarioController.setSelected(null);
        idProgramaAnoController.setSelected(null);
        mesController.setSelected(null);
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
     * Sets the "items" attribute with a collection of ReliquidacionServicio
     * entities that are retrieved from Reliquidacion?cap_first and returns the
     * navigation outcome.
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
     * entities that are retrieved from Reliquidacion?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for ReliquidacionComuna page
     */
    public String navigateReliquidacionComunas() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ReliquidacionComuna_items", this.getSelected().getReliquidacionComunas());
        }
        return "/mantenedor/reliquidacionComuna/index";
    }

    /**
     * Sets the "items" attribute with a collection of DocumentoReliquidacion
     * entities that are retrieved from Reliquidacion?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for DocumentoReliquidacion page
     */
    public String navigateDocumentosReliquidacion() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("DocumentoReliquidacion_items", this.getSelected().getDocumentosReliquidacion());
        }
        return "/mantenedor/documentoReliquidacion/index";
    }

}
