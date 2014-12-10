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

import minsal.divap.dao.TipoSubtituloDAO;
import minsal.divap.vo.MantenedorComponenteVO;
import minsal.divap.vo.MantenedorTipoSubtituloVO;
import cl.minsal.divap.mantenedores.bean.util.JsfUtil;
import cl.minsal.divap.mantenedores.enums.PersistAction;
import cl.minsal.divap.mantenedores.facade.TipoSubtituloFacade;
import cl.minsal.divap.model.Dependencia;
import cl.minsal.divap.model.TipoSubtitulo;

@Named("tipoSubtituloController")
@ViewScoped
public class TipoSubtituloController extends AbstractController<TipoSubtitulo> {

    @EJB
    private TipoSubtituloFacade ejbFacade;
    private DependenciaController dependenciaController;

    private List<MantenedorTipoSubtituloVO> subtitulos;
    private MantenedorTipoSubtituloVO seleccionado;
    private List<Dependencia> dependencias;
    private List<TipoSubtitulo> tipoSubtitulos;
    
    @EJB
    private TipoSubtituloDAO tipoSubtituloDAO;
    
    
    
    
    
    
    @PostConstruct
    @Override
    public void init() {
        super.setFacade(ejbFacade);
        FacesContext context = FacesContext.getCurrentInstance();
        dependenciaController = context.getApplication().evaluateExpressionGet(context, "#{dependenciaController}", DependenciaController.class);
    }

    public TipoSubtituloController() {
        // Inform the Abstract parent controller of the concrete TipoSubtitulo?cap_first Entity
        super(TipoSubtitulo.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        dependenciaController.setSelected(null);
    }

    /**
     * Sets the "items" attribute with a collection of Remesa entities that are
     * retrieved from TipoSubtitulo?cap_first and returns the navigation
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
     * Sets the "items" attribute with a collection of ComponenteSubtitulo
     * entities that are retrieved from TipoSubtitulo?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for ComponenteSubtitulo page
     */
    public String navigateComponenteSubtitulos() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ComponenteSubtitulo_items", this.getSelected().getComponenteSubtitulos());
        }
        return "/mantenedor/componenteSubtitulo/index";
    }

    /**
     * Sets the "items" attribute with a collection of
     * ProgramaServicioCoreComponente entities that are retrieved from
     * TipoSubtitulo?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for ProgramaServicioCoreComponente page
     */
    public String navigateProgramaServicioCoreComponentes() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ProgramaServicioCoreComponente_items", this.getSelected().getProgramaServicioCoreComponentes());
        }
        return "/mantenedor/programaServicioCoreComponente/index";
    }

    /**
     * Sets the "selected" attribute of the Dependencia controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareDependencia(ActionEvent event) {
        if (this.getSelected() != null && dependenciaController.getSelected() == null) {
            dependenciaController.setSelected(this.getSelected().getDependencia());
        }
    }

    /**
     * Sets the "items" attribute with a collection of
     * ProgramaMunicipalCoreComponente entities that are retrieved from
     * TipoSubtitulo?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for ProgramaMunicipalCoreComponente page
     */
    public String navigateProgramaMunicipalCoreComponentes() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ProgramaMunicipalCoreComponente_items", this.getSelected().getProgramaMunicipalCoreComponentes());
        }
        return "/mantenedor/programaMunicipalCoreComponente/index";
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
    
    public void saveNew(ActionEvent event) {
		System.out.println("entra al saveNew");
		super.saveNew(event);
		subtitulos = null;
		dependencias = null;
		tipoSubtitulos = null;
	}

	public void edit(ActionEvent event){
		System.out.println("entra al edit");
		super.edit(event);
		subtitulos = null;
		dependencias = null;
		tipoSubtitulos = null;
	}
	
	public void delete(ActionEvent event){
		System.out.println("entra al delete");
		super.delete(event);
		seleccionado = null;
		subtitulos = null;
		dependencias = null;
		tipoSubtitulos = null;
	}
    

	public List<MantenedorTipoSubtituloVO> getSubtitulos() {
		if(subtitulos == null){
			subtitulos = new ArrayList<MantenedorTipoSubtituloVO>();
			for(TipoSubtitulo tipoSub : getTipoSubtitulos()){
				MantenedorTipoSubtituloVO mantenedorTipoSubtituloVO = new MantenedorTipoSubtituloVO();
				mantenedorTipoSubtituloVO.setIdSubtitulo(tipoSub.getIdTipoSubtitulo());
				mantenedorTipoSubtituloVO.setNombreSubtitulo(tipoSub.getNombreSubtitulo());
				mantenedorTipoSubtituloVO.setIdDependencia(tipoSub.getDependencia().getIdDependenciaPrograma());
				mantenedorTipoSubtituloVO.setNombreDependencia(tipoSub.getDependencia().getNombre());
				mantenedorTipoSubtituloVO.setInflactor(tipoSub.getInflactor());
				subtitulos.add(mantenedorTipoSubtituloVO);
			}
		}
		return subtitulos;
	}
	
	public void prepareCreateSubtitulo(ActionEvent event) {
		System.out.println("prepareCreateSubtitulo");
		seleccionado = new MantenedorTipoSubtituloVO();
		super.prepareCreate(event);
	}

	public void setSubtitulos(List<MantenedorTipoSubtituloVO> subtitulos) {
		this.subtitulos = subtitulos;
	}

	public MantenedorTipoSubtituloVO getSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(MantenedorTipoSubtituloVO seleccionado) {
		this.seleccionado = seleccionado;
	}

	public List<TipoSubtitulo> getTipoSubtitulos() {
		if(tipoSubtitulos == null){
			tipoSubtitulos = tipoSubtituloDAO.getTipoSubtituloAll();
		}
		return tipoSubtitulos;
	}

	public void setTipoSubtitulos(List<TipoSubtitulo> tipoSubtitulos) {
		this.tipoSubtitulos = tipoSubtitulos;
	}

	public List<Dependencia> getDependencias() {
		if(dependencias == null){
			dependencias = tipoSubtituloDAO.getDependenciaAll();
		}
		return dependencias;
	}

	public void setDependencias(List<Dependencia> dependencias) {
		this.dependencias = dependencias;
	}

    
    
    
}
