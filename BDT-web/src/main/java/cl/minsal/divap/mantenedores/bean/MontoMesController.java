package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.MontoMesFacade;
import cl.minsal.divap.model.MontoMes;

@Named("montoMesController")
@ViewScoped
public class MontoMesController extends AbstractController<MontoMes> {

    @EJB
    private MontoMesFacade ejbFacade;

    /**
     * Initialize the concrete MontoMes controller bean. The AbstractController
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
    }

    public MontoMesController() {
        // Inform the Abstract parent controller of the concrete MontoMes?cap_first Entity
        super(MontoMes.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    /**
     * Sets the "items" attribute with a collection of CajaMonto entities that
     * are retrieved from MontoMes?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for CajaMonto page
     */
    public String navigateCajaMontos() {
//        if (this.getSelected() != null) {
//            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("CajaMonto_items", this.getSelected().getCajaMontos());
//        }
        return "/mantenedor/cajaMonto/index";
    }

}
