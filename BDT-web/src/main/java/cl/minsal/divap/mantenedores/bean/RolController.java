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

import minsal.divap.dao.RolDAO;
import minsal.divap.service.MantenedoresService;
import minsal.divap.vo.MantenedorComponenteVO;
import minsal.divap.vo.MantenedorRolVO;
import minsal.divap.vo.RolVO;
import cl.minsal.divap.mantenedores.bean.util.JsfUtil;
import cl.minsal.divap.mantenedores.enums.PersistAction;
import cl.minsal.divap.mantenedores.facade.RolFacade;
import cl.minsal.divap.model.Rol;

@Named("rolController")
@ViewScoped
public class RolController extends AbstractController<Rol> {

    @EJB
    private RolFacade ejbFacade;
    @EJB
    private RolDAO rolDAO;
    @EJB
    private MantenedoresService mantenedoresService;

    private List<RolVO> roles;
    private RolVO seleccionado;
    
    
    
    @PostConstruct
    @Override
    public void init() {
        super.setFacade(ejbFacade);
        FacesContext context = FacesContext.getCurrentInstance();
    }
    

    public RolController() {
        // Inform the Abstract parent controller of the concrete Rol?cap_first Entity
        super(Rol.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    /**
     * Sets the "items" attribute with a collection of Usuario entities that are
     * retrieved from Rol?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Usuario page
     */
    public String navigateUsuarios() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Usuario_items", this.getSelected().getUsuarios());
        }
        return "/mantenedor/usuario/index";
    }

    public void saveNew(ActionEvent event) {
		System.out.println("entra al saveNew");
		super.saveNew(event);
		roles = null;
	}

	public void edit(ActionEvent event){
		System.out.println("entra al edit");
		super.edit(event);
		roles = null;
	}
	
	public void delete(ActionEvent event){
		System.out.println("entra al delete");
		super.delete(event);
		seleccionado = null;
		roles = null;
	}
	
	public void prepareCreateRol(ActionEvent event) {
		System.out.println("prepareCreateRol");
		seleccionado = new RolVO();
		System.out.println("seleccionado --> "+seleccionado);
		super.prepareCreate(event);
	}
	
	@Override
	protected void persist(PersistAction persistAction, String successMessage) {
		System.out.println("this.seleccionado ---> "+this.seleccionado);
		if (this.seleccionado != null) {
			this.setEmbeddableKeys();
			try {
				if (persistAction == PersistAction.UPDATE) {
					this.ejbFacade.edit(this.seleccionado);
					JsfUtil.addSuccessMessage("El rol ha sido editado correctamente");
				}else if(persistAction == PersistAction.CREATE){
					this.ejbFacade.create(this.seleccionado);
					JsfUtil.addSuccessMessage("El rol ha sido creado correctamente");
				}else if(persistAction == PersistAction.DELETE){
					if(this.seleccionado.getPuedeBorrarse()){
						System.out.println("borrando con nuestro delete");
						this.ejbFacade.remove(this.seleccionado);
						JsfUtil.addSuccessMessage("El rol ha sido eliminado correctamente");
					}else{
						JsfUtil.addErrorMessage("El rol no puede eliminarse ya que tiene usuarios asociados");
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
	

	public RolVO getSeleccionado() {
		return seleccionado;
	}
	public void setSeleccionado(RolVO seleccionado) {
		this.seleccionado = seleccionado;
	}
	public List<RolVO> getRoles() {
		if(roles == null){
			roles = mantenedoresService.getAllRolesVO();
		}
		return roles;
	}

	public void setRoles(List<RolVO> roles) {
		this.roles = roles;
	}

    
    
}
