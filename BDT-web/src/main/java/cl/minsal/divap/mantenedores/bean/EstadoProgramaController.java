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
import minsal.divap.vo.MantenedorEstadoProgramaVO;
import cl.minsal.divap.mantenedores.bean.util.JsfUtil;
import cl.minsal.divap.mantenedores.enums.PersistAction;
import cl.minsal.divap.mantenedores.facade.EstadoProgramaFacade;
import cl.minsal.divap.model.EstadoPrograma;

@Named("estadoProgramaController")
@ViewScoped
public class EstadoProgramaController extends AbstractController<EstadoPrograma> {
	
	private List<MantenedorEstadoProgramaVO> estadoProgramas;
	private MantenedorEstadoProgramaVO seleccionado;

    @EJB
    private EstadoProgramaFacade ejbFacade;
    @EJB
    private MantenedoresService mantenedoresService;
    

    /**
     * Initialize the concrete EstadoPrograma controller bean. The
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
    
    public void saveNew(ActionEvent event) {
		System.out.println("entra al saveNew");
		super.saveNew(event);
		estadoProgramas = null;
	}

	public void edit(ActionEvent event){
		System.out.println("entra al edit");
		super.edit(event);
		estadoProgramas = null;
	}
	
	public void delete(ActionEvent event){
		System.out.println("entra al delete");
		super.delete(event);
		seleccionado = null;
		estadoProgramas = null;
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
	
	public void prepareCreateEstadoPrograma(ActionEvent event) {
		System.out.println("prepareCreateEstadoPrograma");
		seleccionado = new MantenedorEstadoProgramaVO();
		super.prepareCreate(event);
	}
    

    public EstadoProgramaController() {
        // Inform the Abstract parent controller of the concrete EstadoPrograma?cap_first Entity
        super(EstadoPrograma.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    /**
     * Sets the "items" attribute with a collection of ProgramaAno entities that
     * are retrieved from EstadoPrograma?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for ProgramaAno page
     */
    public String navigateProgramaAnosReliquidacion() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ProgramaAno_items", this.getSelected().getProgramaAnosReliquidacion());
        }
        return "/mantenedor/programaAno/index";
    }

    /**
     * Sets the "items" attribute with a collection of ProgramaAno entities that
     * are retrieved from EstadoPrograma?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for ProgramaAno page
     */
    public String navigateProgramaAnosConvenio() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ProgramaAno_items", this.getSelected().getProgramaAnosConvenio());
        }
        return "/mantenedor/programaAno/index";
    }

    /**
     * Sets the "items" attribute with a collection of ProgramaAno entities that
     * are retrieved from EstadoPrograma?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for ProgramaAno page
     */
    public String navigateProgramasAnosFlujoCaja() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ProgramaAno_items", this.getSelected().getProgramasAnosFlujoCaja());
        }
        return "/mantenedor/programaAno/index";
    }

    /**
     * Sets the "items" attribute with a collection of ProgramaAno entities that
     * are retrieved from EstadoPrograma?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for ProgramaAno page
     */
    public String navigateProgramasAnos() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ProgramaAno_items", this.getSelected().getProgramasAnos());
        }
        return "/mantenedor/programaAno/index";
    }

	public List<MantenedorEstadoProgramaVO> getEstadoProgramas() {
		if(estadoProgramas == null){
			estadoProgramas = mantenedoresService.getMantenedorEstadoProgramaAll();
		}
		return estadoProgramas;
	}

	public void setEstadoProgramas(List<MantenedorEstadoProgramaVO> estadoProgramas) {
		this.estadoProgramas = estadoProgramas;
	}

	public MantenedorEstadoProgramaVO getSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(MantenedorEstadoProgramaVO seleccionado) {
		this.seleccionado = seleccionado;
	}
    

}
