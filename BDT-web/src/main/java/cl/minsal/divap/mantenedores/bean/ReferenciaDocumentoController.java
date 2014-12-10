package cl.minsal.divap.mantenedores.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.facade.ReferenciaDocumentoFacade;
import cl.minsal.divap.model.ReferenciaDocumento;

@Named("referenciaDocumentoController")
@ViewScoped
public class ReferenciaDocumentoController extends AbstractController<ReferenciaDocumento> {

    @EJB
    private ReferenciaDocumentoFacade ejbFacade;

    /**
     * Initialize the concrete ReferenciaDocumento controller bean. The
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

    public ReferenciaDocumentoController() {
        // Inform the Abstract parent controller of the concrete ReferenciaDocumento?cap_first Entity
        super(ReferenciaDocumento.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    /**
     * Sets the "items" attribute with a collection of Rebaja entities that are
     * retrieved from ReferenciaDocumento?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for Rebaja page
     */
    public String navigateRebajaCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Rebaja_items", this.getSelected().getRebajaCollection());
        }
        return "/mantenedor/rebaja/index";
    }

    /**
     * Sets the "items" attribute with a collection of
     * DocumentoEstimacionflujocaja entities that are retrieved from
     * ReferenciaDocumento?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for DocumentoEstimacionflujocaja page
     */
    public String navigateDocumentosEstimacionflujocaja() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("DocumentoEstimacionflujocaja_items", this.getSelected().getDocumentosEstimacionflujocaja());
        }
        return "/mantenedor/documentoEstimacionflujocaja/index";
    }

    /**
     * Sets the "items" attribute with a collection of
     * DocumentoDistribucionInicialPercapita entities that are retrieved from
     * ReferenciaDocumento?cap_first and returns the navigation outcome.
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
     * SeguimientoReferenciaDocumento entities that are retrieved from
     * ReferenciaDocumento?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for SeguimientoReferenciaDocumento page
     */
    public String navigateSeguimientoReferenciaDocumentoCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("SeguimientoReferenciaDocumento_items", this.getSelected().getSeguimientoReferenciaDocumentoCollection());
        }
        return "/mantenedor/seguimientoReferenciaDocumento/index";
    }

    /**
     * Sets the "items" attribute with a collection of Plantilla entities that
     * are retrieved from ReferenciaDocumento?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for Plantilla page
     */
    public String navigatePlantillaCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Plantilla_items", this.getSelected().getPlantillaCollection());
        }
        return "/mantenedor/plantilla/index";
    }

    /**
     * Sets the "items" attribute with a collection of DocumentoRebaja entities
     * that are retrieved from ReferenciaDocumento?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for DocumentoRebaja page
     */
    public String navigateDocumentosRebaja() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("DocumentoRebaja_items", this.getSelected().getDocumentosRebaja());
        }
        return "/mantenedor/documentoRebaja/index";
    }

}
