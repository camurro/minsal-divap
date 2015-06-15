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
import minsal.divap.vo.MantenedorRegionVO;
import minsal.divap.vo.PersonaMantenedorVO;
import minsal.divap.vo.PersonaVO;
import minsal.divap.vo.RegionVO;
import cl.minsal.divap.mantenedores.bean.util.JsfUtil;
import cl.minsal.divap.mantenedores.enums.PersistAction;
import cl.minsal.divap.mantenedores.facade.RegionFacade;
import cl.minsal.divap.model.Region;

@Named("regionController")
@ViewScoped
public class RegionController extends AbstractController<Region> {

    @EJB
    private RegionFacade ejbFacade;
    
    @EJB
    private MantenedoresService mantenedoresService;
    
    private PersonaController secretarioRegionalController;

    private List<MantenedorRegionVO> regiones;
    private MantenedorRegionVO seleccionado;
    
    private List<PersonaVO> personas;
    
    @PostConstruct
    @Override
    public void init() {
        super.setFacade(ejbFacade);
        FacesContext context = FacesContext.getCurrentInstance();
        secretarioRegionalController = context.getApplication().evaluateExpressionGet(context, "#{personaController}", PersonaController.class);
    }
    
    public void saveNew(ActionEvent event) {
		System.out.println("entra al saveNew");
		super.saveNew(event);
		personas = null;
		regiones = null;
	}

	public void edit(ActionEvent event){
		System.out.println("entra al edit");
		super.edit(event);
		personas = null;
		regiones = null;
	}
	
	public void delete(ActionEvent event){
		System.out.println("entra al delete");
		super.delete(event);
		seleccionado = null;
		personas = null;
		regiones = null;
	}
    
	@Override
	protected void persist(PersistAction persistAction, String successMessage) {
		System.out.println("this.seleccionado ---> "+this.seleccionado);
		if (this.seleccionado != null) {
			this.setEmbeddableKeys();
			try {
				if (persistAction == PersistAction.UPDATE) {
					this.ejbFacade.edit(this.seleccionado);
					JsfUtil.addSuccessMessage("La región se ha editado correctamente");
				}else if(persistAction == PersistAction.CREATE){
					this.ejbFacade.create(this.seleccionado);
					JsfUtil.addSuccessMessage("La región se ha creado correctamente");
				}else if(persistAction == PersistAction.DELETE){
					if(this.seleccionado.getPuedeEliminarse()){
						System.out.println("borrando con nuestro delete");
						this.ejbFacade.remove(this.seleccionado);
						JsfUtil.addSuccessMessage("La región se ha eliminado correctamente");
					}else{
						JsfUtil.addErrorMessage("La región posee servicios por lo tanto no puede eliminarse");
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
	
	public void prepareCreateRegion(ActionEvent event) {
		System.out.println("prepareCreateRegion");
		seleccionado = new MantenedorRegionVO();
		PersonaMantenedorVO secretarioRegional = new PersonaMantenedorVO();
		seleccionado.setSecretarioRegional(secretarioRegional);
		super.prepareCreate(event);
	}

    public RegionController() {
        // Inform the Abstract parent controller of the concrete Region?cap_first Entity
        super(Region.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        secretarioRegionalController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the Persona controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareSecretarioRegional(ActionEvent event) {
        if (this.getSelected() != null && secretarioRegionalController.getSelected() == null) {
            secretarioRegionalController.setSelected(this.getSelected().getSecretarioRegional());
        }
    }

    /**
     * Sets the "items" attribute with a collection of ServicioSalud entities
     * that are retrieved from Region?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for ServicioSalud page
     */
    public String navigateServicioSaluds() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ServicioSalud_items", this.getSelected().getServicioSaluds());
        }
        return "/mantenedor/servicioSalud/index";
    }

	public List<MantenedorRegionVO> getRegiones() {
		if(regiones == null){
			regiones = mantenedoresService.getAllMantenedorRegionesVO();
		}
		return regiones;
	}

	public void setRegiones(List<MantenedorRegionVO> regiones) {
		this.regiones = regiones;
	}

	public MantenedorRegionVO getSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(MantenedorRegionVO seleccionado) {
		this.seleccionado = seleccionado;
	}

	public List<PersonaVO> getPersonas() {
		if(personas == null){
			personas = mantenedoresService.getAllPersonas();
		}
		return personas;
	}

	public void setPersonas(List<PersonaVO> personas) {
		this.personas = personas;
	}

    
}
