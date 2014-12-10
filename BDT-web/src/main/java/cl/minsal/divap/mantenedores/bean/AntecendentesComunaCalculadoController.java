package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.AntecendentesComunaCalculadoFacade;
import cl.minsal.divap.model.AntecendentesComunaCalculado;

@Named("antecendentesComunaCalculadoController")
@ViewScoped
public class AntecendentesComunaCalculadoController extends AbstractController<AntecendentesComunaCalculado> {

    @EJB
    private AntecendentesComunaCalculadoFacade ejbFacade;
    private DistribucionInicialPercapitaController distribucionInicialPercapitaController;
    private AntecendentesComunaController antecedentesComunaController;

    /**
     * Initialize the concrete AntecendentesComunaCalculado controller bean. The
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
        distribucionInicialPercapitaController = context.getApplication().evaluateExpressionGet(context, "#{distribucionInicialPercapitaController}", DistribucionInicialPercapitaController.class);
        antecedentesComunaController = context.getApplication().evaluateExpressionGet(context, "#{antecendentesComunaController}", AntecendentesComunaController.class);
    }

    public AntecendentesComunaCalculadoController() {
        // Inform the Abstract parent controller of the concrete AntecendentesComunaCalculado?cap_first Entity
        super(AntecendentesComunaCalculado.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        distribucionInicialPercapitaController.setSelected(null);
        antecedentesComunaController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the DistribucionInicialPercapita
     * controller in order to display its data in a dialog. This is reusing
     * existing the existing View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareDistribucionInicialPercapita(ActionEvent event) {
        if (this.getSelected() != null && distribucionInicialPercapitaController.getSelected() == null) {
            distribucionInicialPercapitaController.setSelected(this.getSelected().getDistribucionInicialPercapita());
        }
    }

    /**
     * Sets the "selected" attribute of the AntecendentesComuna controller in
     * order to display its data in a dialog. This is reusing existing the
     * existing View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareAntecedentesComuna(ActionEvent event) {
        if (this.getSelected() != null && antecedentesComunaController.getSelected() == null) {
            antecedentesComunaController.setSelected(this.getSelected().getAntecedentesComuna());
        }
    }
}
