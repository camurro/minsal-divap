package cl.minsal.divap.mantenedores.bean;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import minsal.divap.dao.MantenedoresDAO;
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.service.MantenedoresService;
import minsal.divap.vo.MantenedorCuotasVO;
import minsal.divap.vo.MantenedorProgramaVO;

import org.primefaces.event.FlowEvent;
import org.primefaces.event.RowEditEvent;

import cl.minsal.divap.mantenedores.bean.util.JsfUtil;
import cl.minsal.divap.mantenedores.enums.PersistAction;
import cl.minsal.divap.mantenedores.facade.ProgramaFacade;
import cl.minsal.divap.model.Cuota;
import cl.minsal.divap.model.Programa;
import cl.minsal.divap.model.Usuario;

@Named("programaController")
@ViewScoped
public class ProgramaController extends AbstractController<Programa> {

	
	private List<MantenedorProgramaVO> programas;
	private MantenedorProgramaVO seleccionado;
	private List<Usuario> usuarios;
	private List<MantenedorCuotasVO> cuotas;
	private List<Integer> cantidadCuotas;
	private Integer nroCuota;
	private Integer porcentaje_cuota;
	private Integer monto_cuota;
	private Date fecha_cuota;
	private String fecha_convertida;
	private Integer mes;
	private Boolean firstCuota;
	
    @EJB
    private ProgramaFacade ejbFacade;
    
    @EJB
    private MantenedoresService mantenedoresService;
    @EJB
    private UsuarioDAO usuarioDAO;
    @EJB
    private MantenedoresDAO mantenedoresDAO;
    
    private UsuarioController usuarioController;

    
    
    public void addAction(){
    	System.out.println("getFecha_convertida() -->"+getFecha_convertida()+"<---");
    	System.out.println("getFecha_cuota() --> "+getFecha_cuota());
    	Integer month = null;
    	MantenedorCuotasVO cuota = new MantenedorCuotasVO();
    	if(this.cuotas.size() > 0){
    		setFirstCuota(false);
    	}
    	if(!getFecha_convertida().equals("")){
    		System.out.println("hay fecha");
    		month = Integer.parseInt(getFecha_convertida().substring(3, 5));
    		Date date = null;
        	DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        	try {
    			date = format.parse(getFecha_convertida());
    		} catch (ParseException e) {
    			e.printStackTrace();
    		}
        	cuota.setFecha_cuota(date);
        	cuota.setMes(month);
    	}
    	else{
    		System.out.println("no hay fecha");
    		cuota.setFecha_cuota(null);
    		cuota.setMes(null);
    		setFirstCuota(false);
    	}
    	
    	
    	cuota.setNroCuota(this.cuotas.size()+1);
    	cuota.setMonto_cuota(getMonto_cuota());
    	cuota.setPorcentaje_cuota(getPorcentaje_cuota());
    	
    	
    	this.cuotas.add(cuota);
    	clearField();
    	
    }
    
    private void clearField() {
    	setNroCuota(1);
    	setPorcentaje_cuota(0);
    	setMonto_cuota(0);
    	setFecha_cuota(null);
    }
    
    public void onRowEdit(RowEditEvent event) {
    	System.out.println("entra al row edit");
        FacesMessage msg = new FacesMessage("Cuota Editada", ((MantenedorCuotasVO) event.getObject()).getIdCuota().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
     
    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edicion Cancelada", ((MantenedorCuotasVO) event.getObject()).getIdCuota().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    
    @PostConstruct
    @Override
    public void init() {
        super.setFacade(ejbFacade);
        this.firstCuota = true;
        FacesContext context = FacesContext.getCurrentInstance();
        usuarioController = context.getApplication().evaluateExpressionGet(context, "#{usuarioController}", UsuarioController.class);
        this.cuotas = new ArrayList<MantenedorCuotasVO>();
        
    }
    
    public void prepareCreatePrograma(ActionEvent event) {
		System.out.println("prepareCreateUsuario");
		System.out.println("PREPARECREATE seleccionado --> "+seleccionado);
		seleccionado = new MantenedorProgramaVO();
		if(this.cuotas.size() > 0){
			this.firstCuota = false;
		}
		super.prepareCreate(event);
	}
    
    public void prepareEditarPrograma(ActionEvent event) {
    	
    	List<Cuota> cuotasTemp = mantenedoresDAO.getCuotasByProgramaAno(this.seleccionado.getIdProgramaAno());
    	System.out.println("cuotasTemp --> "+cuotasTemp);
		if(cuotasTemp != null){
			for(Cuota cuota : cuotasTemp){
				MantenedorCuotasVO mantenedorCuotasVO = new MantenedorCuotasVO();
				mantenedorCuotasVO.setIdCuota(cuota.getId());
				mantenedorCuotasVO.setNroCuota((int)cuota.getNumeroCuota());
				mantenedorCuotasVO.setFecha_cuota(cuota.getFechaPago());
				mantenedorCuotasVO.setMonto_cuota(cuota.getMonto());
				mantenedorCuotasVO.setPorcentaje_cuota(cuota.getPorcentaje());
				mantenedorCuotasVO.setMes(cuota.getIdMes().getIdMes());
				this.cuotas.add(mantenedorCuotasVO);
			}
		}
		else{
			this.cuotas = new ArrayList<MantenedorCuotasVO>();
		}
		if(this.cuotas.size() > 0){
			this.firstCuota = false;
		}
    	
    	System.out.println("prepare editar Programa");
    }
    
    public void saveNew(ActionEvent event) {
    	seleccionado.setListaCuotas(getCuotas());
    	seleccionado.setCuotas(getCuotas().size());
		System.out.println("entra al saveNew");
		super.saveNew(event);
		usuarios = null;
		programas = null;
	}

	public void edit(ActionEvent event){
		seleccionado.setListaCuotas(getCuotas());
    	seleccionado.setCuotas(getCuotas().size());
		System.out.println("entra al edit");
		super.edit(event);
		usuarios = null;
		programas = null;
	}
	
	public void delete(ActionEvent event){
		System.out.println("entra al delete");
		super.delete(event);
		seleccionado = null;
		usuarios = null;
		programas = null;
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
    
	public String deleteAction(MantenedorCuotasVO mantenedorCuotasVO) {

        this.cuotas.remove(mantenedorCuotasVO);
        return null;
    }
	
    public ProgramaController() {
        // Inform the Abstract parent controller of the concrete Programa?cap_first Entity
        super(Programa.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        usuarioController.setSelected(null);
    }

    /**
     * Sets the "items" attribute with a collection of MetadataCore entities
     * that are retrieved from Programa?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for MetadataCore page
     */
    public String navigateMetadataCores() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("MetadataCore_items", this.getSelected().getMetadataCores());
        }
        return "/mantenedor/metadataCore/index";
    }

    /**
     * Sets the "selected" attribute of the Usuario controller in order to
     * display its data in a dialog. This is reusing existing the existing View
     * dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareUsuario(ActionEvent event) {
        if (this.getSelected() != null && usuarioController.getSelected() == null) {
            usuarioController.setSelected(this.getSelected().getUsuario());
        }
    }

    /**
     * Sets the "items" attribute with a collection of ProgramaAno entities that
     * are retrieved from Programa?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for ProgramaAno page
     */
    public String navigateProgramasAnos() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ProgramaAno_items", this.getSelected().getProgramasAnos());
        }
        return "/mantenedor/programaAno/index";
    }

    /**
     * Sets the "items" attribute with a collection of Componente entities that
     * are retrieved from Programa?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Componente page
     */
    public String navigateComponentes() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Componente_items", this.getSelected().getComponentes());
        }
        return "/mantenedor/componente/index";
    }

	public List<MantenedorProgramaVO> getProgramas() {
		if(programas == null){
			programas = mantenedoresService.getAllMantenedorProgramaVO();
		}
		return programas;
	}

	public void setProgramas(List<MantenedorProgramaVO> programas) {
		this.programas = programas;
	}

	public MantenedorProgramaVO getSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(MantenedorProgramaVO seleccionado) {
		this.seleccionado = seleccionado;
	}

	public List<Usuario> getUsuarios() {
		if(usuarios == null){
			usuarios = usuarioDAO.getUserAll();
		}
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public String onFlowProcess(FlowEvent event) {
        return event.getNewStep();
    }

	public List<MantenedorCuotasVO> getCuotas() {
		return cuotas;
	}

	public void setCuotas(List<MantenedorCuotasVO> cuotas) {
		this.cuotas = cuotas;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}


	public Integer getNroCuota() {
		return nroCuota;
	}


	public void setNroCuota(Integer nroCuota) {
		this.nroCuota = nroCuota;
	}


	public Integer getPorcentaje_cuota() {
		return porcentaje_cuota;
	}


	public void setPorcentaje_cuota(Integer porcentaje_cuota) {
		this.porcentaje_cuota = porcentaje_cuota;
	}


	public Integer getMonto_cuota() {
		return monto_cuota;
	}

	public void setMonto_cuota(Integer monto_cuota) {
		this.monto_cuota = monto_cuota;
	}

	public Date getFecha_cuota() {
		return fecha_cuota;
	}

	public void setFecha_cuota(Date fecha_cuota) {
		this.fecha_cuota = fecha_cuota;
	}

	public List<Integer> getCantidadCuotas() {
		return cantidadCuotas;
	}

	public void setCantidadCuotas(List<Integer> cantidadCuotas) {
		this.cantidadCuotas = cantidadCuotas;
	}

	public String getFecha_convertida() {
		return fecha_convertida;
	}

	public void setFecha_convertida(String fecha_convertida) {
		this.fecha_convertida = fecha_convertida;
	}

	public Boolean getFirstCuota() {
		return firstCuota;
	}

	public void setFirstCuota(Boolean firstCuota) {
		System.out.println("firstCuota --> "+firstCuota);
		this.firstCuota = firstCuota;
	}
	
}