package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.EstadoProgramaFlujocajaFacade;
import cl.minsal.divap.model.EstadoProgramaFlujocaja;

@Named("estadoProgramaFlujocajaController")
@ViewScoped
public class EstadoProgramaFlujocajaController extends AbstractController<EstadoProgramaFlujocaja> {

    @EJB
    private EstadoProgramaFlujocajaFacade ejbFacade;

    /**
     * Initialize the concrete EstadoProgramaFlujocaja controller bean. The
     * AbstractController requires the EJB Facade object for most operations.
     */
    @PostConstruct
    @Override
    public void init() {
        super.setFacade(ejbFacade);
    }

    public EstadoProgramaFlujocajaController() {
        // Inform the Abstract parent controller of the concrete EstadoProgramaFlujocaja?cap_first Entity
        super(EstadoProgramaFlujocaja.class);
    }

}
