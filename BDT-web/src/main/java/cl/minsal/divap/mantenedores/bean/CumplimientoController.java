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

import minsal.divap.dao.MantenedoresDAO;
import minsal.divap.dao.RebajaDAO;
import minsal.divap.service.MantenedoresService;
import minsal.divap.vo.MantenedorCumplimientoVO;
import minsal.divap.vo.MantenedorFactorTramoPobrezaVO;
import cl.minsal.divap.mantenedores.bean.util.JsfUtil;
import cl.minsal.divap.mantenedores.enums.PersistAction;
import cl.minsal.divap.mantenedores.facade.CumplimientoFacade;
import cl.minsal.divap.model.Cumplimiento;
import cl.minsal.divap.model.TipoCumplimiento;
import cl.minsal.divap.model.Tramo;

@Named("cumplimientoController")
@ViewScoped
public class CumplimientoController extends AbstractController<Cumplimiento> {
	
	
	private List<MantenedorCumplimientoVO> cumplimientos;
	private MantenedorCumplimientoVO seleccionado;
	
	private List<Tramo> tramos;
	private List<TipoCumplimiento> tipoCumplimientos;
	
	@EJB
	private MantenedoresService mantenedoresService;
	@EJB
	private MantenedoresDAO mantenedoresDAO;
	@EJB
	private RebajaDAO rebajaDAO;

    @EJB
    private CumplimientoFacade ejbFacade;
    private TramoController tramoController;
    private TipoCumplimientoController tipoCumplimientoController;

    
    @PostConstruct
    @Override
    public void init() {
        super.setFacade(ejbFacade);
        FacesContext context = FacesContext.getCurrentInstance();
        tramoController = context.getApplication().evaluateExpressionGet(context, "#{tramoController}", TramoController.class);
        tipoCumplimientoController = context.getApplication().evaluateExpressionGet(context, "#{tipoCumplimientoController}", TipoCumplimientoController.class);
    }
    
    public void prepareCreateCumplimiento(ActionEvent event) {
		System.out.println("prepareCreateCumplimiento");
		seleccionado = new MantenedorCumplimientoVO();
		super.prepareCreate(event);
	}
    
    public void saveNew(ActionEvent event) {
		System.out.println("entra al saveNew");
		super.saveNew(event);
		cumplimientos = null;
		tramos = null;
		tipoCumplimientos = null;
	}

	public void edit(ActionEvent event){
		System.out.println("entra al edit");
		super.edit(event);
		cumplimientos = null;
		tramos = null;
		tipoCumplimientos = null;
	}
	
	public void delete(ActionEvent event){
		System.out.println("entra al delete");
		super.delete(event);
		seleccionado = null;
		cumplimientos = null;
		tramos = null;
		tipoCumplimientos = null;
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
    

    public CumplimientoController() {
        // Inform the Abstract parent controller of the concrete Cumplimiento?cap_first Entity
        super(Cumplimiento.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        tramoController.setSelected(null);
        tipoCumplimientoController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the Tramo controller in order to display
     * its data in a dialog. This is reusing existing the existing View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareTramo(ActionEvent event) {
        if (this.getSelected() != null && tramoController.getSelected() == null) {
            tramoController.setSelected(this.getSelected().getTramo());
        }
    }

    /**
     * Sets the "selected" attribute of the TipoCumplimiento controller in order
     * to display its data in a dialog. This is reusing existing the existing
     * View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareTipoCumplimiento(ActionEvent event) {
        if (this.getSelected() != null && tipoCumplimientoController.getSelected() == null) {
            tipoCumplimientoController.setSelected(this.getSelected().getTipoCumplimiento());
        }
    }

	public List<MantenedorCumplimientoVO> getCumplimientos() {
		if(cumplimientos == null){
			cumplimientos = mantenedoresService.getMantenedorCumplimientoVOAll();
		}
		return cumplimientos;
	}

	public void setCumplimientos(List<MantenedorCumplimientoVO> cumplimientos) {
		this.cumplimientos = cumplimientos;
	}

	public MantenedorCumplimientoVO getSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(MantenedorCumplimientoVO seleccionado) {
		this.seleccionado = seleccionado;
	}

	public List<Tramo> getTramos() {
		if(tramos == null){
			tramos = mantenedoresDAO.getTramoAll();
		}
		return tramos;
	}

	public void setTramos(List<Tramo> tramos) {
		this.tramos = tramos;
	}

	public List<TipoCumplimiento> getTipoCumplimientos() {
		if(tipoCumplimientos == null){
			tipoCumplimientos = rebajaDAO.getAllTipoCumplimiento();
		}
		return tipoCumplimientos;
	}

	public void setTipoCumplimientos(List<TipoCumplimiento> tipoCumplimientos) {
		this.tipoCumplimientos = tipoCumplimientos;
	}
    
    
}
