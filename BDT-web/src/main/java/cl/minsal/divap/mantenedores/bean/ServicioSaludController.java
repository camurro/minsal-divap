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
import minsal.divap.service.ServicioSaludService;
import minsal.divap.vo.MantenedorRegionVO;
import minsal.divap.vo.PersonaVO;
import minsal.divap.vo.RegionSummaryVO;
import minsal.divap.vo.ServiciosVO;
import cl.minsal.divap.mantenedores.bean.util.JsfUtil;
import cl.minsal.divap.mantenedores.enums.PersistAction;
import cl.minsal.divap.mantenedores.facade.ServicioSaludFacade;
import cl.minsal.divap.model.ServicioSalud;

@Named("servicioSaludController")
@ViewScoped
public class ServicioSaludController extends AbstractController<ServicioSalud> {

    @EJB
    private ServicioSaludFacade ejbFacade;
    
    @EJB
    private ServicioSaludService servicioSaludService;
    @EJB
    private MantenedoresService mantenedoresService;
    
    private List<ServiciosVO> servicios;
    private ServiciosVO seleccionado;
    private List<PersonaVO> personas;
    private List<MantenedorRegionVO> regiones;
    
    
    private RegionController regionController;
    private PersonaController directorController;
    private PersonaController encargadoFinanzasApsController;
    private PersonaController encargadoApsController;

    /**
     * Initialize the concrete ServicioSalud controller bean. The
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
        regionController = context.getApplication().evaluateExpressionGet(context, "#{regionController}", RegionController.class);
        directorController = context.getApplication().evaluateExpressionGet(context, "#{personaController}", PersonaController.class);
        encargadoFinanzasApsController = context.getApplication().evaluateExpressionGet(context, "#{personaController}", PersonaController.class);
        encargadoApsController = context.getApplication().evaluateExpressionGet(context, "#{personaController}", PersonaController.class);
    }
    
    public void saveNew(ActionEvent event) {
		System.out.println("entra al saveNew");
		super.saveNew(event);
		personas = null;
		regiones = null;
		servicios = null;
	}

	public void edit(ActionEvent event){
		System.out.println("entra al edit");
		super.edit(event);
		personas = null;
		regiones = null;
		servicios = null;
	}
	
	public void delete(ActionEvent event){
		System.out.println("entra al delete");
		super.delete(event);
		seleccionado = null;
		personas = null;
		regiones = null;
		servicios = null;
	}

	@Override
	protected void persist(PersistAction persistAction, String successMessage) {
		System.out.println("this.seleccionado ---> "+this.seleccionado);
		if (this.seleccionado != null) {
			this.setEmbeddableKeys();
			try {
				if (persistAction == PersistAction.UPDATE) {
					this.ejbFacade.edit(this.seleccionado);
				}else if(persistAction == PersistAction.CREATE){
					this.ejbFacade.create(this.seleccionado);
				}else if(persistAction == PersistAction.DELETE){
					System.out.println("borrando con nuestro delete");
					this.ejbFacade.remove(this.seleccionado);
				}
				JsfUtil.addSuccessMessage(successMessage);
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
	
	
    public ServicioSaludController() {
        // Inform the Abstract parent controller of the concrete ServicioSalud?cap_first Entity
        super(ServicioSalud.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        regionController.setSelected(null);
        directorController.setSelected(null);
        encargadoFinanzasApsController.setSelected(null);
        encargadoApsController.setSelected(null);
    }

    /**
     * Sets the "items" attribute with a collection of Comuna entities that are
     * retrieved from ServicioSalud?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for Comuna page
     */
    public String navigateComunas() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Comuna_items", this.getSelected().getComunas());
        }
        return "/mantenedor/comuna/index";
    }

    /**
     * Sets the "items" attribute with a collection of Establecimiento entities
     * that are retrieved from ServicioSalud?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for Establecimiento page
     */
    public String navigateEstablecimientos() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Establecimiento_items", this.getSelected().getEstablecimientos());
        }
        return "/mantenedor/establecimiento/index";
    }

    /**
     * Sets the "items" attribute with a collection of MarcoPresupuestario
     * entities that are retrieved from ServicioSalud?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for MarcoPresupuestario page
     */
    public String navigateMarcoPresupuestarios() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("MarcoPresupuestario_items", this.getSelected().getMarcoPresupuestarios());
        }
        return "/mantenedor/marcoPresupuestario/index";
    }

    /**
     * Sets the "selected" attribute of the Region controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareRegion(ActionEvent event) {
        if (this.getSelected() != null && regionController.getSelected() == null) {
            regionController.setSelected(this.getSelected().getRegion());
        }
    }

    /**
     * Sets the "items" attribute with a collection of Remesa entities that are
     * retrieved from ServicioSalud?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for Remesa page
     */
    public String navigateRemesaCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Remesa_items", this.getSelected().getRemesaCollection());
        }
        return "/mantenedor/remesa/index";
    }

    /**
     * Sets the "selected" attribute of the Persona controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareDirector(ActionEvent event) {
        if (this.getSelected() != null && directorController.getSelected() == null) {
            directorController.setSelected(this.getSelected().getDirector());
        }
    }

    /**
     * Sets the "selected" attribute of the Persona controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareEncargadoFinanzasAps(ActionEvent event) {
        if (this.getSelected() != null && encargadoFinanzasApsController.getSelected() == null) {
            encargadoFinanzasApsController.setSelected(this.getSelected().getEncargadoFinanzasAps());
        }
    }

    /**
     * Sets the "selected" attribute of the Persona controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareEncargadoAps(ActionEvent event) {
        if (this.getSelected() != null && encargadoApsController.getSelected() == null) {
            encargadoApsController.setSelected(this.getSelected().getEncargadoAps());
        }
    }
    
    public void prepareCreateServicio(ActionEvent event) {
		System.out.println("prepareCreateServicio");
		seleccionado = new ServiciosVO();
		RegionSummaryVO regionSummaryVO = new RegionSummaryVO();
		seleccionado.setRegion(regionSummaryVO);
		seleccionado.setDirector(new PersonaVO());
		seleccionado.setEncargadoAps(new PersonaVO());
		seleccionado.setEncargadoFinanzasAps(new PersonaVO());
		super.prepareCreate(event);
	}

	public List<ServiciosVO> getServicios() {
		if(servicios == null){
			servicios = servicioSaludService.getServiciosOrderId();
		}
		return servicios;
	}

	public void setServicios(List<ServiciosVO> servicios) {
		this.servicios = servicios;
	}

	public ServiciosVO getSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(ServiciosVO seleccionado) {
//		System.out.println("this.seleccionado ---> "+this.seleccionado.toString());
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

	public List<MantenedorRegionVO> getRegiones() {
		if(regiones == null){
			regiones = mantenedoresService.getAllMantenedorRegionesVO();
		}
		return regiones;
	}

	public void setRegiones(List<MantenedorRegionVO> regiones) {
		this.regiones = regiones;
	}
    
    
}
