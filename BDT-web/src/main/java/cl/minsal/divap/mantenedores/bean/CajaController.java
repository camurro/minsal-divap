package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.CajaFacade;
import cl.minsal.divap.model.Caja;

@Named("cajaController")
@ViewScoped
public class CajaController extends AbstractController<Caja> {

    @EJB
    private CajaFacade ejbFacade;
    private TipoSubtituloController idSubtituloController;
    private MarcoPresupuestarioController marcoPresupuestarioController;
    private ComponenteController idComponenteController;

    /**
     * Initialize the concrete Caja controller bean. The AbstractController
     * requires the EJB Facade object for most operations.
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
        idSubtituloController = context.getApplication().evaluateExpressionGet(context, "#{tipoSubtituloController}", TipoSubtituloController.class);
        marcoPresupuestarioController = context.getApplication().evaluateExpressionGet(context, "#{marcoPresupuestarioController}", MarcoPresupuestarioController.class);
        idComponenteController = context.getApplication().evaluateExpressionGet(context, "#{componenteController}", ComponenteController.class);
    }

    public CajaController() {
        // Inform the Abstract parent controller of the concrete Caja?cap_first Entity
        super(Caja.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        idSubtituloController.setSelected(null);
        marcoPresupuestarioController.setSelected(null);
        idComponenteController.setSelected(null);
    }

    /**
     * Sets the "items" attribute with a collection of CajaMonto entities that
     * are retrieved from Caja?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for CajaMonto page
     */
    public String navigateCajaMontos() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("CajaMonto_items", this.getSelected().getCajaMontos());
        }
        return "/mantenedor/cajaMonto/index";
    }

    /**
     * Sets the "selected" attribute of the TipoSubtitulo controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareIdSubtitulo(ActionEvent event) {
        if (this.getSelected() != null && idSubtituloController.getSelected() == null) {
            idSubtituloController.setSelected(this.getSelected().getIdSubtitulo());
        }
    }

    /**
     * Sets the "selected" attribute of the MarcoPresupuestario controller in
     * order to display its data in a dialog. This is reusing existing the
     * existing View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareMarcoPresupuestario(ActionEvent event) {
        if (this.getSelected() != null && marcoPresupuestarioController.getSelected() == null) {
            marcoPresupuestarioController.setSelected(this.getSelected().getMarcoPresupuestario());
        }
    }

    /**
     * Sets the "selected" attribute of the Componente controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareIdComponente(ActionEvent event) {
        if (this.getSelected() != null && idComponenteController.getSelected() == null) {
            idComponenteController.setSelected(this.getSelected().getIdComponente());
        }
    }
}
