package cl.minsal.divap.mantenedores.bean;

import java.util.ArrayList;
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

import minsal.divap.dao.ServicioSaludDAO;
import minsal.divap.service.ServicioSaludService;
import minsal.divap.vo.EstablecimientoSummaryVO;
import minsal.divap.vo.EstablecimientoVO;
import minsal.divap.vo.MantenedorComunaVO;
import minsal.divap.vo.MantenedorEstablecimientoVO;
import minsal.divap.vo.ServiciosVO;
import cl.minsal.divap.mantenedores.bean.util.JsfUtil;
import cl.minsal.divap.mantenedores.enums.PersistAction;
import cl.minsal.divap.mantenedores.facade.EstablecimientoFacade;
import cl.minsal.divap.model.Establecimiento;
import cl.minsal.divap.model.ServicioSalud;

@Named("establecimientoController")
@ViewScoped
public class EstablecimientoController extends AbstractController<Establecimiento> {
	
	private List<ServicioSalud> servicios;
	private Integer idServicioSeleccionado = 0;
	private List<MantenedorEstablecimientoVO> establecimientos;
	private MantenedorEstablecimientoVO establecimientoSeleccionado;

    @EJB
    private EstablecimientoFacade ejbFacade;
    private ComunaController comunaController;
    private ServicioSaludController servicioSaludController;
    @EJB
	private ServicioSaludService servicioSaludService;
    @EJB ServicioSaludDAO servicioSaludDAO;
    /**
     * Initialize the concrete Establecimiento controller bean. The
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
        comunaController = context.getApplication().evaluateExpressionGet(context, "#{comunaController}", ComunaController.class);
        servicioSaludController = context.getApplication().evaluateExpressionGet(context, "#{servicioSaludController}", ServicioSaludController.class);
    }

    public EstablecimientoController() {
        // Inform the Abstract parent controller of the concrete Establecimiento?cap_first Entity
        super(Establecimiento.class);
    }

    
    public List<MantenedorEstablecimientoVO> cargarMantenedorEstablecimientoVO(){
    	List<MantenedorEstablecimientoVO> resultado = new ArrayList<MantenedorEstablecimientoVO>();
    	for(ServicioSalud servicio : this.servicios){
    		System.out.println("servicio --> "+servicio.getNombre());
    		for(Establecimiento establecimiento : servicio.getEstablecimientos()){
    			MantenedorEstablecimientoVO mantenedorEstablecimientoVO = new MantenedorEstablecimientoVO();
    			mantenedorEstablecimientoVO.setIdEstablecimiento(establecimiento.getId());
    			mantenedorEstablecimientoVO.setNombreEstablecimiento(establecimiento.getNombre());
    			mantenedorEstablecimientoVO.setIdServicio(servicio.getId());
    			mantenedorEstablecimientoVO.setNombreServicio(servicio.getNombre());
    			mantenedorEstablecimientoVO.setCodigo(establecimiento.getCodigo());
    			mantenedorEstablecimientoVO.setTipo(establecimiento.getTipo());
    			resultado.add(mantenedorEstablecimientoVO);
    		}
    	}
    	
    	return resultado;
    }
    
    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        comunaController.setSelected(null);
        servicioSaludController.setSelected(null);
    }

    /**
     * Sets the "items" attribute with a collection of Remesa entities that are
     * retrieved from Establecimiento?cap_first and returns the navigation
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
     * Sets the "selected" attribute of the Comuna controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareComuna(ActionEvent event) {
        if (this.getSelected() != null && comunaController.getSelected() == null) {
            comunaController.setSelected(this.getSelected().getComuna());
        }
    }

    /**
     * Sets the "selected" attribute of the ServicioSalud controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareServicioSalud(ActionEvent event) {
        if (this.getSelected() != null && servicioSaludController.getSelected() == null) {
            servicioSaludController.setSelected(this.getSelected().getServicioSalud());
        }
    }

    public List<ServicioSalud> getServicios() {
		if(servicios == null){
			servicios = servicioSaludDAO.getServiciosOrderId();
		}
		return servicios;
	}

	public void setServicios(List<ServicioSalud> servicios) {
		this.servicios = servicios;
	}

	public Integer getIdServicioSeleccionado() {
		return idServicioSeleccionado;
	}

	public void setIdServicioSeleccionado(Integer idServicioSeleccionado) {
		this.idServicioSeleccionado = idServicioSeleccionado;
	}

	public List<MantenedorEstablecimientoVO> getEstablecimientos() {
		if(establecimientos == null){
			establecimientos = new ArrayList<MantenedorEstablecimientoVO>();
			for(ServicioSalud servicio : getServicios()){
	    		System.out.println("servicio --> "+servicio.getNombre());
	    		for(Establecimiento establecimiento : servicio.getEstablecimientos()){
	    			MantenedorEstablecimientoVO mantenedorEstablecimientoVO = new MantenedorEstablecimientoVO();
	    			mantenedorEstablecimientoVO.setIdEstablecimiento(establecimiento.getId());
	    			mantenedorEstablecimientoVO.setNombreEstablecimiento(establecimiento.getNombre());
	    			mantenedorEstablecimientoVO.setIdServicio(servicio.getId());
	    			mantenedorEstablecimientoVO.setNombreServicio(servicio.getNombre());
	    			mantenedorEstablecimientoVO.setCodigo(establecimiento.getCodigo());
	    			mantenedorEstablecimientoVO.setTipo(establecimiento.getTipo());
	    			establecimientos.add(mantenedorEstablecimientoVO);
	    		}
	    	}
		}
		return establecimientos;
	}
	
	
	@Override
	protected void persist(PersistAction persistAction, String successMessage) {
		System.out.println("this.establecimientoSeleccionado ---> "+this.establecimientoSeleccionado);
		if (this.establecimientoSeleccionado != null) {
			this.setEmbeddableKeys();
			try {
				if (persistAction == PersistAction.UPDATE) {
					this.ejbFacade.edit(this.establecimientoSeleccionado);
				}else if(persistAction == PersistAction.CREATE){
					this.ejbFacade.create(this.establecimientoSeleccionado);
				}else if(persistAction == PersistAction.DELETE){
					System.out.println("borrando con nuestro delete");
					this.ejbFacade.remove(this.establecimientoSeleccionado);
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
	
	
	public void prepareCreateEstablecimiento(ActionEvent event) {
		System.out.println("prepareCreateEstablecimiento");
		establecimientoSeleccionado = new MantenedorEstablecimientoVO();
		super.prepareCreate(event);
	}
	
	public void saveNew(ActionEvent event) {
		System.out.println("entra al saveNew");
		super.saveNew(event);
		establecimientos = null;
		servicios = null;
	}

	public void edit(ActionEvent event){
		System.out.println("entra al edit");
		super.edit(event);
		establecimientos = null;
		servicios = null;
	}
	
	public void delete(ActionEvent event){
		System.out.println("entra al delete");
		super.delete(event);
		establecimientoSeleccionado = null;
		establecimientos = null;
		servicios = null;
	}

	public void setEstablecimientos(
			List<MantenedorEstablecimientoVO> establecimientos) {
		this.establecimientos = establecimientos;
	}

	public MantenedorEstablecimientoVO getEstablecimientoSeleccionado() {
		return establecimientoSeleccionado;
	}

	public void setEstablecimientoSeleccionado(MantenedorEstablecimientoVO establecimientoSeleccionado) {
		System.out.println("establecimientoSeleccionado --> "+establecimientoSeleccionado);
		this.establecimientoSeleccionado = establecimientoSeleccionado;
	}
    
    
}
