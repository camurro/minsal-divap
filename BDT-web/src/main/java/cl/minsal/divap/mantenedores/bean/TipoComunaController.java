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

import minsal.divap.dao.ComunaDAO;
import minsal.divap.vo.PersonaVO;
import minsal.divap.vo.RegionSummaryVO;
import minsal.divap.vo.ServiciosVO;
import cl.minsal.divap.mantenedores.bean.util.JsfUtil;
import cl.minsal.divap.mantenedores.enums.PersistAction;
import cl.minsal.divap.mantenedores.facade.TipoComunaFacade;
import cl.minsal.divap.model.TipoComuna;

@Named("tipoComunaController")
@ViewScoped
public class TipoComunaController extends AbstractController<TipoComuna> {

    @EJB
    private TipoComunaFacade ejbFacade;

    private List<TipoComuna> tipoComunas;
    private TipoComuna seleccionado;
    
    @EJB
    private ComunaDAO comunaDAO;
    
    @PostConstruct
    @Override
    public void init() {
        super.setFacade(ejbFacade);
        FacesContext context = FacesContext.getCurrentInstance();
    }
    
    public void saveNew(ActionEvent event) {
		System.out.println("entra al saveNew");
		super.saveNew(event);
		tipoComunas = null;
	}

	public void edit(ActionEvent event){
		System.out.println("entra al edit");
		super.edit(event);
		tipoComunas = null;
	}
	
	public void delete(ActionEvent event){
		System.out.println("entra al delete");
		super.delete(event);
		seleccionado = null;
		tipoComunas = null;
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

    public TipoComunaController() {
        // Inform the Abstract parent controller of the concrete TipoComuna?cap_first Entity
        super(TipoComuna.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    public void prepareCreateTipoComuna(ActionEvent event) {
		System.out.println("prepareCreateTipoComuna");
		seleccionado = new TipoComuna();
		super.prepareCreate(event);
	}

    
    public String navigateAntecendentesComunaCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("AntecendentesComuna_items", this.getSelected().getAntecendentesComunaCollection());
        }
        return "/mantenedor/antecendentesComuna/index";
    }

	public List<TipoComuna> getTipoComunas() {
		if(tipoComunas == null){
			tipoComunas = comunaDAO.getAllTipoComuna();
		}
		return tipoComunas;
	}

	public void setTipoComunas(List<TipoComuna> tipoComunas) {
		this.tipoComunas = tipoComunas;
	}

	public TipoComuna getSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(TipoComuna seleccionado) {
		this.seleccionado = seleccionado;
	}
    
    

}
