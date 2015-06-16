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
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import minsal.divap.dao.AntecedentesComunaDAO;
import minsal.divap.dao.RemesaDAO;
import minsal.divap.dao.ServicioSaludDAO;
import minsal.divap.service.ComunaService;
import minsal.divap.service.MantenedoresService;
import minsal.divap.service.ReportesServices;
import minsal.divap.service.ServicioSaludService;
import minsal.divap.vo.FactorRefAsigZonaVO;
import minsal.divap.vo.FactorTramoPobrezaVO;
import minsal.divap.vo.MantenedorComunaFinalVO;
import minsal.divap.vo.ServiciosVO;
import minsal.divap.vo.TipoComunaVO;
import cl.minsal.divap.mantenedores.bean.util.JsfUtil;
import cl.minsal.divap.mantenedores.enums.PersistAction;
import cl.minsal.divap.mantenedores.facade.ComunaFacade;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.Remesa;

@Named("comunaController")
@ViewScoped
public class ComunaController extends AbstractController<Comuna> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4704651163827939668L;
	@EJB
	private ComunaFacade ejbFacade;
	//private ServicioSaludController servicioSaludController;
	private MantenedorComunaFinalVO comunaSeleccionada;
	private Integer idServicioSeleccionado = 0;
	private List<ServiciosVO> servicios;
	
	private List<MantenedorComunaFinalVO> listadoComunas;
	private List<Remesa> remesas;
	private List<FactorRefAsigZonaVO> listRefAsigZonaVO;
	private List<FactorTramoPobrezaVO> listTramoPobrezaVO;
	private List<TipoComunaVO> tipoComunas;
	private Integer anoEnCurso;
	private Integer anoFinal;
	private Boolean nuevaComunaEsAuxiliar;

	@EJB
	private ComunaService comunaService;
	@EJB
	private ServicioSaludDAO servicioSaludDAO;
	@EJB
	private ServicioSaludService servicioSaludService;
	@EJB
	private RemesaDAO remesaDAO;
	@EJB
	private MantenedoresService mantenedoresService;
	@EJB
	private ReportesServices reportesServices;
	@EJB
	private AntecedentesComunaDAO antecedentesComunaDAO;
	
	private Boolean comunasAnoSiguiente;

	/**
	 * Initialize the concrete Comuna controller bean. The AbstractController
	 * requires the EJB Facade object for most operations.
	 * <p>
	 * In addition, this controller also requires references to controllers for
	 * parent entities in order to display their information from a context
	 * menu.
	 */
	@PostConstruct
	@Override
	public void init() {
		super.setFacade(ejbFacade);
		comunaSeleccionada = new MantenedorComunaFinalVO();
		nuevaComunaEsAuxiliar = false;
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		comunasAnoSiguiente = Boolean.parseBoolean(request.getParameter("anoSiguiente"));
	}

	public ComunaController() {
		// Inform the Abstract parent controller of the concrete Comuna?cap_first Entity
		super(Comuna.class);
	}

	/**
	 * Resets the "selected" attribute of any parent Entity controllers.
	 */
	public void resetParents() {
		this.comunaSeleccionada.setMostrarBotonesEditViewDelete(true);
		//servicioSaludController.setSelected(null);
	}
	
	public void listenerAuxiliar(){
		if(comunaSeleccionada != null){
			comunaSeleccionada.setComunaAuxiliar(nuevaComunaEsAuxiliar);
		}
	}

	/**
	 * Sets the "items" attribute with a collection of Remesa entities that are
	 * retrieved from Comuna?cap_first and returns the navigation outcome.
	 *
	 * @return navigation outcome for Remesa page
	 */
	public String navigateRemesaCollection() {
		if (this.getSelected() != null) {
			System.out.println("this.comunaSeleccionada ---> "+this.comunaSeleccionada);
			if(this.comunaSeleccionada != null){
				this.remesas = remesaDAO.getRemesasByComuna(this.comunaSeleccionada.getIdComuna());
			}

			FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Remesa_items", this.remesas);
			//            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Remesa_items", this.getSelected().getRemesaCollection());
		}
		return "/mantenedor/remesa/index";
	}

	@Override
	protected void persist(PersistAction persistAction, String successMessage) {
		System.out.println("this.comunaSeleccionada ---> "+this.comunaSeleccionada);
		if (this.comunaSeleccionada != null) {
			this.setEmbeddableKeys();
			try {
				this.comunaSeleccionada.setComunaAuxiliar(nuevaComunaEsAuxiliar);
				if (persistAction == PersistAction.UPDATE) {
					this.ejbFacade.edit(this.comunaSeleccionada, anoFinal);
					JsfUtil.addSuccessMessage(successMessage);
				}else if(persistAction == PersistAction.CREATE){
					this.ejbFacade.create(this.comunaSeleccionada, anoFinal);
					JsfUtil.addSuccessMessage(successMessage);
				}else if(persistAction == PersistAction.DELETE){
					System.out.println("borrando con nuestro delete");
					
					if(!this.comunaSeleccionada.getPuedeEliminarse()){
						JsfUtil.addErrorMessage("La comuna no puede ser eliminada ya que está siendo utilizada en los procesos de Per Cápita / Distribución de Recursos Financieros para Programas de Reforzamiento de APS");
					}else{
						this.ejbFacade.remove(this.comunaSeleccionada);
						JsfUtil.addSuccessMessage(successMessage);
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





	/**
	 * Sets the "items" attribute with a collection of AntecendentesComuna
	 * entities that are retrieved from Comuna?cap_first and returns the
	 * navigation outcome.
	 *
	 * @return navigation outcome for AntecendentesComuna page
	 */
	public String navigateAntecendentesComunas() {
		if (this.getSelected() != null) {
			FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("AntecendentesComuna_items", this.getSelected().getAntecendentesComunasComuna());
		}
		return "/mantenedor/antecendentesComuna/index";
	}

	/**
	 * Sets the "selected" attribute of the ServicioSalud controller in order to
	 * display its data in a dialog. This is reusing existing the existing View
	 * dialog.
	 *
	 * @param event Event object for the widget that triggered an action
	 */
	public void prepareServicioSalud(ActionEvent event) {
		/*if (this.getSelected() != null && servicioSaludController.getSelected() == null) {
			servicioSaludController.setSelected(this.getSelected().getServicioSalud());
		}*/
	}

	/**
	 * Sets the "items" attribute with a collection of Establecimiento entities
	 * that are retrieved from Comuna?cap_first and returns the navigation
	 * outcome.
	 *
	 * @return navigation outcome for Establecimiento page
	 */
	public String navigateEstablecimientos() {
		if (this.getSelected() != null) {
			FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Establecimiento_items", this.getSelected().getEstablecimientos());
		}
		return "/mantenedor/establecimiento/index";
	}

	public void createComuna(){
		this.comunaSeleccionada = new MantenedorComunaFinalVO();
	}

	/**
	 * Sets the "items" attribute with a collection of ProgramaMunicipalCore
	 * entities that are retrieved from Comuna?cap_first and returns the
	 * navigation outcome.
	 *
	 * @return navigation outcome for ProgramaMunicipalCore page
	 */
	public String navigateProgramaMunicipalCores() {
		if (this.getSelected() != null) {
			FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ProgramaMunicipalCore_items", this.getSelected().getProgramaMunicipalCores());
		}
		return "/mantenedor/programaMunicipalCore/index";
	}

	/**
	 * Sets the "items" attribute with a collection of ComunaCumplimiento
	 * entities that are retrieved from Comuna?cap_first and returns the
	 * navigation outcome.
	 *
	 * @return navigation outcome for ComunaCumplimiento page
	 */

//	public List<MantenedorComunaVO> cargarMantenedorComunaVO(){
//		List<MantenedorComunaVO> resultado = new ArrayList<MantenedorComunaVO>();
//		for(ServiciosVO servicio : this.servicios){
//			System.out.println("servicio --> "+servicio.getNombre_servicio());
//			for(ComunaSummaryVO comuna : servicio.getComunas()){
//				MantenedorComunaVO mantenedorComuna = new MantenedorComunaVO();
//				mantenedorComuna.setIdComuna(comuna.getId());
//				mantenedorComuna.setNombreComuna(comuna.getNombre());
//				mantenedorComuna.setIdServicio(servicio.getId_servicio());
//				mantenedorComuna.setNombreServicio(servicio.getNombre_servicio());
//				resultado.add(mantenedorComuna);
//			}
//		}
//		return resultado;
//	}


	public String navigateComunaCumplimientoCollection() {
		if (this.getSelected() != null) {
			FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ComunaCumplimiento_items", this.getSelected().getComunaCumplimientoCollection());
		}
		return "/mantenedor/comunaCumplimiento/index";
	}


	//    public MantenedorComunaVO prepareCreate(ActionEvent event) {
	//    	this.comunaSeleccionada = new MantenedorComunaVO();
	//    	
	//    	return this.comunaSeleccionada;
	//    }


	public void saveNew(ActionEvent event) {
		System.out.println("entra al saveNew");
		super.saveNew(event);
		listadoComunas = null;
		servicios = null;
	}

	public void edit(ActionEvent event){
		System.out.println("entra al edit");
		super.edit(event);
		listadoComunas = null;
		servicios = null;
	}
	
	public void delete(ActionEvent event){
		System.out.println("entra al delete");
		super.delete(event);
		comunaSeleccionada = null;
		listadoComunas = null;
		servicios = null;
	}


	public void prepareCreateComuna(ActionEvent event) {
		System.out.println("prepareCreateComuna");
		nuevaComunaEsAuxiliar = false;
		comunaSeleccionada = new MantenedorComunaFinalVO();
		super.prepareCreate(event);
	}
	
	public void prepareEditComuna(ActionEvent event){
		if(comunaSeleccionada != null){
			nuevaComunaEsAuxiliar = comunaSeleccionada.getComunaAuxiliar();
		}
	}
	

	public MantenedorComunaFinalVO getComunaSeleccionada() {
		return comunaSeleccionada;
	}

	public void setComunaSeleccionada(MantenedorComunaFinalVO comunaSeleccionada) {
		System.out.println("comunaSeleccionada ----> "+comunaSeleccionada);
//		comunaSeleccionada = new MantenedorComunaFinalVO();
//		comunaSeleccionada.setMostrarBotonesEditViewDelete(true);
		
		this.comunaSeleccionada = comunaSeleccionada;
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

	public List<Remesa> getRemesas() {
		return remesas;
	}

	public void setRemesas(List<Remesa> remesas) {
		this.remesas = remesas;
	}

	public Integer getIdServicioSeleccionado() {
		return idServicioSeleccionado;
	}

	public void setIdServicioSeleccionado(Integer idServicioSeleccionado) {
		this.idServicioSeleccionado = idServicioSeleccionado;
	}

	public List<MantenedorComunaFinalVO> getListadoComunas() {
		if(listadoComunas == null){
			if(comunasAnoSiguiente){
				anoFinal = getAnoEnCurso() + 1;
				listadoComunas = mantenedoresService.getAntedentesComunaMantenedor(getAnoEnCurso() + 1);
				if(listadoComunas == null || listadoComunas.size() == 0){
					//cargar antecedentesComuna año actual en año siguiente
					mantenedoresService.copyAntedentesComunaAnoActualToAnoSiguiente(getAnoEnCurso() + 1);
					listadoComunas = mantenedoresService.getAntedentesComunaMantenedor(getAnoEnCurso() + 1);
				}
			}else{
				anoFinal = getAnoEnCurso();
				listadoComunas = mantenedoresService.getAntedentesComunaMantenedor(getAnoEnCurso());
			}
		}
		return listadoComunas;
	}

	public void setListadoComunas(List<MantenedorComunaFinalVO> listadoComunas) {
		this.listadoComunas = listadoComunas;
	}
	public Integer getAnoEnCurso() {
		if(anoEnCurso == null){
			anoEnCurso = reportesServices.getAnoCurso();
		}
		return anoEnCurso;
	}

	public List<FactorRefAsigZonaVO> getListRefAsigZonaVO() {
		if(listRefAsigZonaVO == null){
			listRefAsigZonaVO = mantenedoresService.getAllFactorRefAsigZonaVO();
		}
		return listRefAsigZonaVO;
	}

	public void setListRefAsigZonaVO(List<FactorRefAsigZonaVO> listRefAsigZonaVO) {
		this.listRefAsigZonaVO = listRefAsigZonaVO;
	}
	
	public List<FactorTramoPobrezaVO> getListTramoPobrezaVO() {
		if(listTramoPobrezaVO == null){
			listTramoPobrezaVO = mantenedoresService.getAllFactorTramoPobreza();
		}
		return listTramoPobrezaVO;
	}

	public void setListTramoPobrezaVO(List<FactorTramoPobrezaVO> listTramoPobrezaVO) {
		this.listTramoPobrezaVO = listTramoPobrezaVO;
	}

	public List<TipoComunaVO> getTipoComunas() {
		if(tipoComunas == null){
			tipoComunas = mantenedoresService.getAllTipoComunas();
		}
		return tipoComunas;
	}

	public void setTipoComunas(List<TipoComunaVO> tipoComunas) {
		this.tipoComunas = tipoComunas;
	}

	public Boolean getComunasAnoSiguiente() {
		return comunasAnoSiguiente;
	}

	public void setComunasAnoSiguiente(Boolean comunasAnoSiguiente) {
		this.comunasAnoSiguiente = comunasAnoSiguiente;
	}

	public Integer getAnoFinal() {
		return anoFinal;
	}

	public void setAnoFinal(Integer anoFinal) {
		this.anoFinal = anoFinal;
	}

	public Boolean getNuevaComunaEsAuxiliar() {
		return nuevaComunaEsAuxiliar;
	}

	public void setNuevaComunaEsAuxiliar(Boolean nuevaComunaEsAuxiliar) {
		this.nuevaComunaEsAuxiliar = nuevaComunaEsAuxiliar;
	}

	


}
