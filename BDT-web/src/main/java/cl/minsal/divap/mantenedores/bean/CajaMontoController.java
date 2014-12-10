package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.CajaMontoFacade;
import cl.minsal.divap.model.CajaMonto;

@Named("cajaMontoController")
@ViewScoped
public class CajaMontoController extends AbstractController<CajaMonto> {

    @EJB
    private CajaMontoFacade ejbFacade;
    private MontoMesController montoController;
    private MesController mesController;
    private CajaController cajaController;

    /**
     * Initialize the concrete CajaMonto controller bean. The AbstractController
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
        montoController = context.getApplication().evaluateExpressionGet(context, "#{montoMesController}", MontoMesController.class);
        mesController = context.getApplication().evaluateExpressionGet(context, "#{mesController}", MesController.class);
        cajaController = context.getApplication().evaluateExpressionGet(context, "#{cajaController}", CajaController.class);
    }

    public CajaMontoController() {
        // Inform the Abstract parent controller of the concrete CajaMonto?cap_first Entity
        super(CajaMonto.class);
    }

    @Override
    protected void setEmbeddableKeys() {
        this.getSelected().getCajaMontoPK().setCaja(this.getSelected().getCaja().getId());
        this.getSelected().getCajaMontoPK().setMes(this.getSelected().getMes().getIdMes());
    }

    @Override
    protected void initializeEmbeddableKey() {
        this.getSelected().setCajaMontoPK(new cl.minsal.divap.model.CajaMontoPK());
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        montoController.setSelected(null);
        mesController.setSelected(null);
        cajaController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the MontoMes controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareMonto(ActionEvent event) {
        if (this.getSelected() != null && montoController.getSelected() == null) {
            montoController.setSelected(this.getSelected().getMonto());
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
     * Sets the "selected" attribute of the Caja controller in order to display
     * its data in a dialog. This is reusing existing the existing View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareCaja(ActionEvent event) {
        if (this.getSelected() != null && cajaController.getSelected() == null) {
            cajaController.setSelected(this.getSelected().getCaja());
        }
    }
}
