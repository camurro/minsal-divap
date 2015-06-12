package cl.minsal.divap.mantenedores.bean;

import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import minsal.divap.service.MantenedoresService;
import minsal.divap.vo.MantenedorComunaVO;
import minsal.divap.vo.MantenedorFactorRefAsigZonaVO;
import cl.minsal.divap.mantenedores.bean.util.JsfUtil;
import cl.minsal.divap.mantenedores.enums.PersistAction;
import cl.minsal.divap.mantenedores.facade.FactorRefAsigZonaFacade;
import cl.minsal.divap.model.FactorRefAsigZona;

@Named("factorRefAsigZonaController")
@ViewScoped
public class FactorRefAsigZonaController extends AbstractController<FactorRefAsigZona> {

	private List<MantenedorFactorRefAsigZonaVO> factorRefAsigZonas;
	private MantenedorFactorRefAsigZonaVO seleccionado;
	
    @EJB
    private FactorRefAsigZonaFacade ejbFacade;
    @EJB
    private MantenedoresService mantenedoresService;
    
    
    @PostConstruct
    @Override
    public void init() {
        super.setFacade(ejbFacade);
        FacesContext context = FacesContext.getCurrentInstance();
    }
    
  
    
    public void prepareCreateFactorRefAsigZona(ActionEvent event) {
		System.out.println("prepareCreateFactorRefAsigZona");
		seleccionado = new MantenedorFactorRefAsigZonaVO();
		super.prepareCreate(event);
	}
    
    public void saveNew(ActionEvent event) {
		System.out.println("entra al saveNew");
		super.saveNew(event);
		factorRefAsigZonas = null;
	}

	public void edit(ActionEvent event){
		System.out.println("entra al edit");
		super.edit(event);
		factorRefAsigZonas = null;
	}
	
	public void delete(ActionEvent event){
		System.out.println("entra al delete");
		super.delete(event);
		seleccionado = null;
		factorRefAsigZonas = null;
	}
	
	    
	@Override
	protected void persist(PersistAction persistAction, String successMessage) {
		System.out.println("this.seleccionado ---> "+this.seleccionado);
		factorRefAsigZonas = mantenedoresService.getMantenedorFactorRefAsigZonaAll();
		if (this.seleccionado != null) {
			this.setEmbeddableKeys();
			try {
				if (persistAction == PersistAction.UPDATE) {
					this.ejbFacade.edit(this.seleccionado);
					JsfUtil.addSuccessMessage(successMessage);
				}else if(persistAction == PersistAction.CREATE){
					
					String puedeIngresarse = mantenedoresService.puedeInsertarseTramoAsigZona(factorRefAsigZonas, seleccionado);

					
					if(seleccionado.getZonaDesde() > seleccionado.getZonaHasta()){
						JsfUtil.addErrorMessage("El tramo que se desea crear no es válido");
					}else if(puedeIngresarse.equalsIgnoreCase("NO")){
						JsfUtil.addErrorMessage("El tramo que se desea crear no es válido");
					}
					else if(puedeIngresarse.equalsIgnoreCase("SI y agregar desde al último tramo")){
						MantenedorFactorRefAsigZonaVO ultimo = factorRefAsigZonas.get(factorRefAsigZonas.size() - 1);
						if(ultimo.getZonaHasta() == null){
							this.ejbFacade.createAndChangeUltimo(this.seleccionado, puedeIngresarse, ultimo);
							JsfUtil.addSuccessMessage("El tramo se ha creado exitósamente");
						}else{
							this.ejbFacade.create(this.seleccionado);
							JsfUtil.addSuccessMessage("El tramo se ha creado exitósamente");
						}
						
					}
					else{
						this.ejbFacade.create(this.seleccionado);
						JsfUtil.addSuccessMessage("El tramo se ha creado exitósamente");
					}
					
				}else if(persistAction == PersistAction.DELETE){
					if(seleccionado.getPuedeEliminarse()){
						System.out.println("borrando con nuestro delete");
						this.ejbFacade.remove(this.seleccionado);
						JsfUtil.addSuccessMessage("El tramo se ha eliminado exitósamente");
					}else{
						JsfUtil.addErrorMessage("El tramo no puede ser eliminado ya que se encuentra en uso");
					}
					
				}
				
			} catch (EJBException ex) {
				Throwable cause = JsfUtil.getRootCause(ex.getCause());
				if (cause != null) {
					if (cause instanceof ConstraintViolationException) {
						ConstraintViolationException excp = (ConstraintViolationException) cause;
						for (ConstraintViolation s : excp.getConstraintViolations()) {
							JsfUtil.addErrorMessage(s.getMessage());
						}
					} else {
						String msg = cause.getLocalizedMessage();
						if (msg.length() > 0) {
							JsfUtil.addErrorMessage(msg);
						} else {
							JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
						}
					}
				}
			} catch (Exception ex) {
				Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
				JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("bundle/Bundle").getString("PersistenceErrorOccured"));
			}
		}
	}
    
    

    public FactorRefAsigZonaController() {
        // Inform the Abstract parent controller of the concrete FactorRefAsigZona?cap_first Entity
        super(FactorRefAsigZona.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    /**
     * Sets the "items" attribute with a collection of AntecendentesComuna
     * entities that are retrieved from FactorRefAsigZona?cap_first and returns
     * the navigation outcome.
     *
     * @return navigation outcome for AntecendentesComuna page
     */
    public String navigateAntecendentesComunaCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("AntecendentesComuna_items", this.getSelected().getAntecendentesComunaCollection());
        }
        return "/mantenedor/antecendentesComuna/index";
    }

	public List<MantenedorFactorRefAsigZonaVO> getFactorRefAsigZonas() {
		if(factorRefAsigZonas == null){
			factorRefAsigZonas = mantenedoresService.getMantenedorFactorRefAsigZonaAll();
		}
		return factorRefAsigZonas;
	}

	public void setFactorRefAsigZonas(
			List<MantenedorFactorRefAsigZonaVO> factorRefAsigZonas) {
		this.factorRefAsigZonas = factorRefAsigZonas;
	}

	public MantenedorFactorRefAsigZonaVO getSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(MantenedorFactorRefAsigZonaVO seleccionado) {
//		System.out.println("this.seleccionado --> "+this.seleccionado.toString());
		this.seleccionado = seleccionado;
	}
    
    

}
