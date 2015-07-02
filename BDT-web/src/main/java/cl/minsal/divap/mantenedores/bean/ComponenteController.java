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

import org.primefaces.model.DualListModel;

import minsal.divap.dao.ComponenteDAO;
import minsal.divap.dao.ProgramasDAO;
import minsal.divap.dao.TipoSubtituloDAO;
import minsal.divap.service.MantenedoresService;
import minsal.divap.service.ProgramasService;
import minsal.divap.service.SubtituloService;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.MantenedorComponenteVO;
import minsal.divap.vo.MantenedorComunaVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.SubtituloVO;
import cl.minsal.divap.mantenedores.bean.util.JsfUtil;
import cl.minsal.divap.mantenedores.enums.PersistAction;
import cl.minsal.divap.mantenedores.facade.ComponenteFacade;
import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.Programa;
import cl.minsal.divap.model.TipoComponente;
import cl.minsal.divap.model.TipoSubtitulo;

@Named("componenteController")
@ViewScoped
public class ComponenteController extends AbstractController<Componente> {

    @EJB
    private ComponenteFacade ejbFacade;
    private ProgramaController idProgramaController;
    private TipoComponenteController tipoComponenteController;

    private List<MantenedorComponenteVO> componentes;
    private MantenedorComponenteVO seleccionado;
    private List<Programa> programas;
    private List<TipoComponente> tipoComponentes;
//    private DualListModel<SubtituloVO> subtitulos;
    
    private DualListModel<String> subtitulos;
    
    
    private Integer idProgramaSeleccionado;
    private Integer idTipoComponenteSeleccionado;
    
    @EJB
    private ComponenteDAO componenteDAO;
    @EJB
    private ProgramasService programasService;
    @EJB
    private ProgramasDAO programasDAO;
    @EJB
    private SubtituloService subtituloService; 
    @EJB
    private MantenedoresService mantenedoresService;
    
    
    @PostConstruct
    @Override
    public void init() {
        super.setFacade(ejbFacade);
        FacesContext context = FacesContext.getCurrentInstance();
        idProgramaController = context.getApplication().evaluateExpressionGet(context, "#{programaController}", ProgramaController.class);
        tipoComponenteController = context.getApplication().evaluateExpressionGet(context, "#{tipoComponenteController}", TipoComponenteController.class);
        
    }

    public ComponenteController() {
        // Inform the Abstract parent controller of the concrete Componente?cap_first Entity
        super(Componente.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
//        idProgramaController.setSelected(null);
//        tipoComponenteController.setSelected(null);
    }
    
    public void prepareCreateComponente(ActionEvent event) {
		System.out.println("prepareCreateComponente");
		List<String> subtitulosSource = mantenedoresService.getSubtitulosNombres();
        List<String> subtitulosTarget = new ArrayList<String>();
        this.subtitulos = new DualListModel<String>(subtitulosSource, subtitulosTarget);
		seleccionado = new MantenedorComponenteVO();
		super.prepareCreate(event);
	}
    
    public void prepareEditComponente(ActionEvent event) {
    	this.subtitulos = new DualListModel<String>(this.seleccionado.getNombreSubtitulosFaltantes(), this.seleccionado.getNombreSubtitulos());
    }

   
    @Override
	protected void persist(PersistAction persistAction, String successMessage) {
		System.out.println("this.seleccionado ---> "+this.seleccionado);
		if (this.seleccionado != null) {
			this.setEmbeddableKeys();
			try {
				if (persistAction == PersistAction.UPDATE) {
					this.ejbFacade.edit(this.seleccionado);
					JsfUtil.addSuccessMessage("El componente ha sido editado exitósamente");
				}else if(persistAction == PersistAction.CREATE){
					this.ejbFacade.create(this.seleccionado);
					JsfUtil.addSuccessMessage("El componente ha sido creado exitósamente");
				}else if(persistAction == PersistAction.DELETE){
					if(this.seleccionado.getPuedeEliminarse()){
						System.out.println("borrando con nuestro delete");
						this.ejbFacade.remove(this.seleccionado);
						JsfUtil.addSuccessMessage("El componente ha sido eliminado exitósamente");
					}
					else{
						JsfUtil.addErrorMessage("El componente no puede eliminarse ya que se encuentra en uso");
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
    
    
    public void prepareIdPrograma(ActionEvent event) {
//        if (this.getSelected() != null && idProgramaController.getSelected() == null) {
//            idProgramaController.setSelected(this.getSelected().getIdPrograma());
//        }
    }

    /**
     * Sets the "items" attribute with a collection of ComponenteSubtitulo
     * entities that are retrieved from Componente?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for ComponenteSubtitulo page
     */
    public String navigateComponenteSubtitulos() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ComponenteSubtitulo_items", this.getSelected().getComponenteSubtitulosComponente());
        }
        return "/mantenedor/componenteSubtitulo/index";
    }

    /**
     * Sets the "selected" attribute of the TipoComponente controller in order
     * to display its data in a dialog. This is reusing existing the existing
     * View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareTipoComponente(ActionEvent event) {
        if (this.getSelected() != null && tipoComponenteController.getSelected() == null) {
            tipoComponenteController.setSelected(this.getSelected().getTipoComponente());
        }
    }

    /**
     * Sets the "items" attribute with a collection of
     * ProgramaServicioCoreComponente entities that are retrieved from
     * Componente?cap_first and returns the navigation outcome.
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
     * Sets the "items" attribute with a collection of
     * ProgramaMunicipalCoreComponente entities that are retrieved from
     * Componente?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for ProgramaMunicipalCoreComponente page
     */
    public String navigateProgramaMunicipalCoreComponentes() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ProgramaMunicipalCoreComponente_items", this.getSelected().getProgramaMunicipalCoreComponentes());
        }
        return "/mantenedor/programaMunicipalCoreComponente/index";
    }

    /**
     * Sets the "items" attribute with a collection of Cuota entities that are
     * retrieved from Componente?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Cuota page
     */
    public String navigateCuotas() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Cuota_items", this.getSelected().getCuotas());
        }
        return "/mantenedor/cuota/index";
    }

    public void saveNew(ActionEvent event) {
		System.out.println("entra al saveNew");
		seleccionado.setNombreSubtitulos(subtitulos.getTarget());
		super.saveNew(event);
		componentes = null;
		programas = null;
		subtitulos = null;
	}

	public void edit(ActionEvent event){
		System.out.println("entra al edit");
		seleccionado.setNombreSubtitulos(subtitulos.getTarget());
		super.edit(event);
		componentes = null;
		programas = null;
		subtitulos = null;
	}
	
	public void delete(ActionEvent event){
		System.out.println("entra al delete");
		seleccionado.setNombreSubtitulos(mantenedoresService.getNombreSubtitulosByComponente(this.seleccionado.getIdComponente()));
		super.delete(event);
		seleccionado = null;
		componentes = null;
		programas = null;
		subtitulos = null;
	}
    
	public List<MantenedorComponenteVO> getComponentes() {
		if(componentes == null){
			componentes = mantenedoresService.getAllMantenedorComponenteVO();
		}
		return componentes;
	}

	public void setComponentes(List<MantenedorComponenteVO> componentes) {
		this.componentes = componentes;
	}

	public MantenedorComponenteVO getSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(MantenedorComponenteVO seleccionado) {
		System.out.println("this.seleccionado ----> "+this.seleccionado);
		this.seleccionado = seleccionado;
	}

	public Integer getIdProgramaSeleccionado() {
		return idProgramaSeleccionado;
	}

	public void setIdProgramaSeleccionado(Integer idProgramaSeleccionado) {
		this.idProgramaSeleccionado = idProgramaSeleccionado;
	}

	public Integer getIdTipoComponenteSeleccionado() {
		return idTipoComponenteSeleccionado;
	}

	public void setIdTipoComponenteSeleccionado(Integer idTipoComponenteSeleccionado) {
		this.idTipoComponenteSeleccionado = idTipoComponenteSeleccionado;
	}

	public List<Programa> getProgramas() {
		if(programas == null){
			programas = programasDAO.getAllProgramas();
		}
		return programas;
	}

	public void setProgramas(List<Programa> programas) {
		this.programas = programas;
	}

	public List<TipoComponente> getTipoComponentes() {
		if(tipoComponentes == null){
			tipoComponentes = componenteDAO.getAllTipoComponente();
		}
		return tipoComponentes;
	}

	public void setTipoComponentes(List<TipoComponente> tipoComponentes) {
		this.tipoComponentes = tipoComponentes;
	}

	public DualListModel<String> getSubtitulos() {
		List<String> subtitulosSource = new ArrayList<String>();
        List<String> subtitulosTarget = new ArrayList<String>();
        
        if(subtitulos == null){
        	subtitulosSource = mantenedoresService.getSubtitulosNombres();
    		subtitulos = new DualListModel<String>(subtitulosSource, subtitulosTarget);
        }
        
        
		return subtitulos;
	}

	public void setSubtitulos(DualListModel<String> subtitulos) {
		this.subtitulos = subtitulos;
	}

	
	
    
	
}
