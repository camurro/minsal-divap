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
import minsal.divap.vo.MantenedorAnoVO;
import minsal.divap.vo.MantenedorEstadoProgramaVO;
import cl.minsal.divap.mantenedores.bean.util.JsfUtil;
import cl.minsal.divap.mantenedores.enums.PersistAction;
import cl.minsal.divap.mantenedores.facade.AnoEnCursoFacade;
import cl.minsal.divap.model.AnoEnCurso;

@Named("anoEnCursoController")
@ViewScoped
public class AnoEnCursoController extends AbstractController<AnoEnCurso> {

    @EJB
    private AnoEnCursoFacade ejbFacade;
    @EJB
    private MantenedoresService mantenedoresService;

    private List<MantenedorAnoVO> anos;
    private MantenedorAnoVO seleccionado;

    
    
    @PostConstruct
    @Override
    public void init() {
        super.setFacade(ejbFacade);
        FacesContext context = FacesContext.getCurrentInstance();
    }
    
    public void prepareCreateAnoEnCurso(ActionEvent event) {
		System.out.println("prepareCreateAnoEnCurso");
		seleccionado = new MantenedorAnoVO();
		super.prepareCreate(event);
	}
    
    public void saveNew(ActionEvent event) {
		System.out.println("entra al saveNew");
		super.saveNew(event);
		anos = null;
	}

	public void edit(ActionEvent event){
		System.out.println("entra al edit");
		super.edit(event);
		anos = null;
	}
	
	public void delete(ActionEvent event){
		System.out.println("entra al delete");
		super.delete(event);
		seleccionado = null;
		anos = null;
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

    public AnoEnCursoController() {
        // Inform the Abstract parent controller of the concrete AnoEnCurso?cap_first Entity
        super(AnoEnCurso.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    /**
     * Sets the "items" attribute with a collection of AntecendentesComuna
     * entities that are retrieved from AnoEnCurso?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for AntecendentesComuna page
     */
    public String navigateAntecendentesComunas() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("AntecendentesComuna_items", this.getSelected().getAntecendentesComunas());
        }
        return "/mantenedor/antecendentesComuna/index";
    }

	public List<MantenedorAnoVO> getAnos() {
		if(anos == null){
			anos = mantenedoresService.getMantenedorAnoAll();
		}
		return anos;
	}

	public void setAnos(List<MantenedorAnoVO> anos) {
		this.anos = anos;
	}

	public MantenedorAnoVO getSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(MantenedorAnoVO seleccionado) {
		this.seleccionado = seleccionado;
	}

    
}
