package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.TipoDocumentoFacade;
import cl.minsal.divap.model.TipoDocumento;

@Named("tipoDocumentoController")
@ViewScoped
public class TipoDocumentoController extends AbstractController<TipoDocumento> {

    @EJB
    private TipoDocumentoFacade ejbFacade;

    /**
     * Initialize the concrete TipoDocumento controller bean. The
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
    }

    public TipoDocumentoController() {
        // Inform the Abstract parent controller of the concrete TipoDocumento?cap_first Entity
        super(TipoDocumento.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    /**
     * Sets the "items" attribute with a collection of
     * DocumentoDistribucionInicialPercapita entities that are retrieved from
     * TipoDocumento?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for DocumentoDistribucionInicialPercapita page
     */
    public String navigateDocumentoDistribucionInicialPercapitaCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("DocumentoDistribucionInicialPercapita_items", this.getSelected().getDocumentoDistribucionInicialPercapitaCollection());
        }
        return "/mantenedor/documentoDistribucionInicialPercapita/index";
    }

}
