package cl.minsal.divap.mantenedores.bean;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import minsal.divap.dao.MantenedoresDAO;
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.enums.TipoComponenteEnum;
import minsal.divap.service.ComponenteService;
import minsal.divap.service.MantenedoresService;
import minsal.divap.service.ReportesServices;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.FechaRemesaVO;
import minsal.divap.vo.MantenedorCuotasVO;
import minsal.divap.vo.MantenedorProgramaVO;
import minsal.divap.vo.SubtituloVO;
import minsal.divap.vo.TipoComponenteVO;

import org.primefaces.event.CellEditEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.DualListModel;

import sun.security.action.GetLongAction;
import cl.minsal.divap.mantenedores.bean.util.JsfUtil;
import cl.minsal.divap.mantenedores.enums.PersistAction;
import cl.minsal.divap.mantenedores.facade.ProgramaFacade;
import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.Cuota;
import cl.minsal.divap.model.Programa;
import cl.minsal.divap.model.Usuario;
import cl.minsal.util.PrimeFacesUtil;

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
	private Date fecha_cuota;
	private String fecha_convertida;
	private Integer mes;
	private Boolean firstCuota;
	private Boolean programasAnoSiguiente;
	private Integer anoEnCurso;
	private DualListModel<String> componentes;
	private DualListModel<String> fechasDiaRemesas;
	private Integer tipoPrograma;
	private List<TipoComponenteVO> tipoComponentes;
	private Boolean cuotaCienPorciento;
	private Boolean ingresoCamposCuotasObligatorio;
	private Integer anoFinal;
	private String continuarWizard;

	private Boolean reiniciarDistribucionAps;
	private Boolean reiniciarFlujoCaja;
	private Boolean reiniciarConvenio;
	private Boolean reiniciarReliquidacion;
	private Boolean reiniciarOT;
	private Boolean reiniciarModificacionAps;
	private String errorMessage;
	private Boolean permitirGuardarPrograma;
	private Integer totalPorcentajeCuotas;

	@EJB
	private ProgramaFacade ejbFacade;

	@EJB
	private MantenedoresService mantenedoresService;
	@EJB
	private UsuarioDAO usuarioDAO;
	@EJB
	private MantenedoresDAO mantenedoresDAO;
	@EJB
	private ReportesServices reportesServices;
	@EJB
	private ComponenteService componenteService;

	private UsuarioController usuarioController;


	public String deleteAction(MantenedorCuotasVO mantenedorCuotasVO) {
		System.out.println("llega al deleteAction");
		this.cuotas.remove(mantenedorCuotasVO);
		Integer porcentajeTotal = 0;
		for(MantenedorCuotasVO cuotas : this.cuotas){
			porcentajeTotal += cuotas.getPorcentaje_cuota();
		}
		if(porcentajeTotal == 100){
			permitirGuardarPrograma = true;
		}else{
			permitirGuardarPrograma = false;
		}
		return null;
	}

	private void clearField() {
		setPorcentaje_cuota(null);
		setFecha_cuota(null);
	}

	public void onRowEdit(RowEditEvent event) {
		System.out.println("entra al row edit");
		MantenedorCuotasVO cuotaVO = (MantenedorCuotasVO) event.getObject();
		System.out.println("cuotaVO --> " + cuotaVO);
		
		Integer porcentajeAcumuladoCuotas = 0;
		
		for(int i = 0; i < cuotas.size(); i ++){
			if(cuotas.get(i).getNroCuota() == cuotaVO.getNroCuota()){
				cuotas.remove(i);
				cuotas.add(i, cuotaVO);
			}
		}
		for(MantenedorCuotasVO cuotasVO : this.cuotas){
			porcentajeAcumuladoCuotas = porcentajeAcumuladoCuotas + cuotasVO.getPorcentaje_cuota();
		}
		
		
		if(porcentajeAcumuladoCuotas == 100){
			permitirGuardarPrograma = true;
		}else{
			permitirGuardarPrograma = false;
		}
		
		
		FacesMessage msg = new FacesMessage("Cuota Editada", null);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Edicion Cancelada", null);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	@PostConstruct
	@Override
	public void init() {
		cuotaCienPorciento = false;
		permitirGuardarPrograma = false;
		ingresoCamposCuotasObligatorio = true;
		continuarWizard = "true";
		super.setFacade(ejbFacade);
		anoEnCurso = reportesServices.getAnoCurso();
		this.firstCuota = true;
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		programasAnoSiguiente = Boolean.parseBoolean(request
				.getParameter("anoSiguiente"));
		if (programasAnoSiguiente) {
			anoFinal = getAnoEnCurso() + 1;
		} else {
			anoFinal = getAnoEnCurso();
		}
		FacesContext context = FacesContext.getCurrentInstance();
		usuarioController = context.getApplication().evaluateExpressionGet(
				context, "#{usuarioController}", UsuarioController.class);
		this.cuotas = new ArrayList<MantenedorCuotasVO>();

		reiniciarDistribucionAps = false;
		reiniciarFlujoCaja = false;
		reiniciarConvenio = false;
		reiniciarReliquidacion = false;
		reiniciarOT = false;
		reiniciarModificacionAps = false;
	}

	public void prepareCreatePrograma(ActionEvent event) {
		System.out.println("prepareCreateUsuario");
		System.out.println("PREPARECREATE seleccionado --> " + seleccionado);
		seleccionado = new MantenedorProgramaVO();
		this.firstCuota = true;
		this.tipoPrograma = TipoComponenteEnum.PXQ.getId();
		this.fechasDiaRemesas = null;
		this.cuotas = new ArrayList<MantenedorCuotasVO>();

		List<String> componentesSource = componenteService.getNombreComponenteByIdTipoComponente(tipoPrograma);
		
		
		List<String> componentesTarget = new ArrayList<String>();

		componentes = new DualListModel<String>(componentesSource,
				componentesTarget);

		super.prepareCreate(event);
	}

	public void prepareEditarPrograma(ActionEvent event) {

		List<String> diasRemesasSource = seleccionado
				.getDiaPagoRemesasFaltantes();
		List<String> diasRemesasTarget = seleccionado.getDiaPagoRemesas();
		fechasDiaRemesas = new DualListModel<String>(diasRemesasSource,
				diasRemesasTarget);

		this.tipoPrograma = seleccionado.getIdTipoPrograma();

		List<String> componentesSource = new ArrayList<String>();
		List<String> componentesTarget = new ArrayList<String>();

		componentesSource = seleccionado.getComponentesFaltantes();
		componentesTarget = seleccionado.getComponentes();

		componentes = new DualListModel<String>(componentesSource,
				componentesTarget);

		this.cuotas = seleccionado.getListaCuotas();
		if(cuotas == null || cuotas.size() == 0){
			this.firstCuota = true;
		}else{
			this.firstCuota = false;
		}
	}

	public void saveNew(ActionEvent event) {
			ingresoCamposCuotasObligatorio = false;
			seleccionado.setListaCuotas(getCuotas());
			seleccionado.setCuotas(getCuotas().size());
			seleccionado.setComponentes(componentes.getTarget());
			seleccionado.setDiaPagoRemesas(fechasDiaRemesas.getTarget());
			super.saveNew(event);
			usuarios = null;
			programas = null;
	}

	public void edit(ActionEvent event) {
		Boolean guardar = false;
		String ejecutarJavaScrip = null;

		ingresoCamposCuotasObligatorio = false;
		seleccionado.setListaCuotas(getCuotas());
		seleccionado.setCuotas(getCuotas().size());
		seleccionado.setComponentes(componentes.getTarget());
		seleccionado.setDiaPagoRemesas(fechasDiaRemesas.getTarget());
		System.out.println("entra al edit");
		HashMap<String, Boolean> hashMapReiniciarProcesos = new HashMap<String, Boolean>();

		hashMapReiniciarProcesos.put("reiniciarEstimacionFlujoCaja",
				this.reiniciarFlujoCaja);
		hashMapReiniciarProcesos.put("reiniciarConvenio",
				this.reiniciarConvenio);
		hashMapReiniciarProcesos.put("reiniciarReliquidacion",
				this.reiniciarReliquidacion);
		hashMapReiniciarProcesos.put("reiniciarOT", this.reiniciarOT);
		hashMapReiniciarProcesos.put("reiniciarDistribucionAps",
				this.reiniciarDistribucionAps);
		hashMapReiniciarProcesos.put("reiniciarModificacionAps",
				this.reiniciarModificacionAps);
		seleccionado.setReiniciarProcesos(hashMapReiniciarProcesos);

		super.edit(event);
//		usuarios = null;
//		if (programasAnoSiguiente) {
//			programas = mantenedoresService
//					.getAllMantenedorProgramaVO(anoEnCurso + 1);
//			if (programas == null || programas.size() == 0) {
//
//			}
//		} else {
//			programas = mantenedoresService
//					.getAllMantenedorProgramaVO(anoEnCurso);
//		}

	}

	public void delete(ActionEvent event) {
		System.out.println("entra al delete");
		super.delete(event);
		seleccionado = null;
		usuarios = null;
		programas = null;
	}

	@Override
	protected void persist(PersistAction persistAction, String successMessage) {
		System.out.println("this.seleccionado ---> " + this.seleccionado);
		if (this.seleccionado != null) {
			this.setEmbeddableKeys();
			try {
				if (persistAction == PersistAction.UPDATE) {
					this.ejbFacade.edit(this.seleccionado);
					
				} else if (persistAction == PersistAction.CREATE) {
					this.ejbFacade.create(this.seleccionado, anoFinal);
				} else if (persistAction == PersistAction.DELETE) {
					System.out.println("borrando con nuestro delete");
					this.ejbFacade.remove(this.seleccionado);
				}
				JsfUtil.addSuccessMessage(successMessage);
			} catch (EJBException ex) {
				Throwable cause = JsfUtil.getRootCause(ex.getCause());
				if (cause != null) {
					if (cause instanceof ConstraintViolationException) {
						ConstraintViolationException excp = (ConstraintViolationException) cause;
						for (ConstraintViolation s : excp
								.getConstraintViolations()) {
							JsfUtil.addErrorMessage(s.getMessage());
						}
					} else {
						String msg = cause.getLocalizedMessage();
						if (msg.length() > 0) {
							JsfUtil.addErrorMessage(msg);
						} else {
							JsfUtil.addErrorMessage(
									ex,
									ResourceBundle.getBundle("/Bundle")
											.getString(
													"PersistenceErrorOccured"));
						}
					}
				}
			} catch (Exception ex) {
				Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
						null, ex);
				JsfUtil.addErrorMessage(
						ex,
						ResourceBundle.getBundle("bundle/Bundle").getString(
								"PersistenceErrorOccured"));
			}
		}
	}

	public void listenerTipoProgramaCrear() {
		List<String> componentesSource = new ArrayList<String>();
		List<String> componentesTarget = new ArrayList<String>();

		// en la creacion del nuevo Programa, si selecciona un tipo de programa
		// historico, en la siguiente pantalla con los componentes, hay que
		// validar que por lo menos
		// 1 de los componentes sea de tipo historico

		TipoComponenteVO tipoComponenteSeleccionado = mantenedoresService
				.getTipoComponenteById(tipoPrograma);
		System.out
				.println("\n\n\n\n\n\n\n\n\n\n\n\n tipoComponenteSeleccionado --> "
						+ tipoComponenteSeleccionado
						+ "\n\n\n\n\n\n\n\n\n\n\n\n\n\n");

		if (tipoComponenteSeleccionado.getNombre().equalsIgnoreCase("ley")) {
			// ley
			componentesSource = componenteService
					.getNombreComponenteByIdTipoComponente(minsal.divap.enums.TipoComponenteEnum.LEY
							.getId());
		} else if (tipoComponenteSeleccionado.getNombre().equalsIgnoreCase(
				"PxQ")) {
			componentesSource = componenteService
					.getNombreComponenteByIdTipoComponente(minsal.divap.enums.TipoComponenteEnum.PXQ
							.getId());
		} else {
			componentesSource = componenteService
					.getNombreComponenteByNotIdTipoComponente(minsal.divap.enums.TipoComponenteEnum.LEY
							.getId());
		}

		componentes = new DualListModel<String>(componentesSource,
				componentesTarget);

	}

	public void listenerTipoProgramaEditar() {
		continuarWizard = "true";
		if (tipoPrograma.equals(TipoComponenteEnum.PXQ.getId())) {
			if (seleccionado.getIdTipoPrograma().equals(
					TipoComponenteEnum.LEY.getId())) {
				// Se quiere cambiar de ley a pxq
				// lanzar mensaje de excepcion
				continuarWizard = "false";
			} else if (seleccionado.getIdTipoPrograma().equals(
					TipoComponenteEnum.HISTORICO.getId())) {

				// se cambia de historico a pxq
				List<String> componentesPxQ = componenteService
						.getNombreComponenteByIdTipoComponente(minsal.divap.enums.TipoComponenteEnum.PXQ
								.getId());
				List<String> componentesSource = mantenedoresService
						.getComponentesFaltantesEntreDosListas(componentesPxQ,
								seleccionado.getComponentes());
				List<String> componentesTarget = seleccionado.getComponentes();
				componentes = new DualListModel<String>(componentesSource,
						componentesTarget);

			} else {
				return;
			}
		} else if (tipoPrograma.equals(TipoComponenteEnum.LEY.getId())) {
			if (seleccionado.getIdTipoPrograma().equals(
					TipoComponenteEnum.PXQ.getId())) {
				PrimeFacesUtil.ejecutarJavaScript("alertaTipoComponente();");
				tipoPrograma = TipoComponenteEnum.PXQ.getId();
				continuarWizard = "false";
			}
			if (seleccionado.getIdTipoPrograma().equals(
					TipoComponenteEnum.HISTORICO.getId())) {
				PrimeFacesUtil.ejecutarJavaScript("alertaTipoComponente();");
				tipoPrograma = TipoComponenteEnum.HISTORICO.getId();
				continuarWizard = "false";
			}

		} else {

			if (seleccionado.getIdTipoPrograma().equals(
					TipoComponenteEnum.PXQ.getId())) {
				// se quiere cambiar desde PXQ a HISTORICO
				List<String> componentesHistoricos = componenteService
						.getNombreComponenteByNotIdTipoComponente(minsal.divap.enums.TipoComponenteEnum.LEY
								.getId());
				List<String> componentesSource = mantenedoresService
						.getComponentesFaltantesEntreDosListas(
								componentesHistoricos,
								seleccionado.getComponentes());
				List<String> componentesTarget = seleccionado.getComponentes();
				componentes = new DualListModel<String>(componentesSource,
						componentesTarget);
			}
		}
	}

	public ProgramaController() {
		// Inform the Abstract parent controller of the concrete
		// Programa?cap_first Entity
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
			FacesContext
					.getCurrentInstance()
					.getExternalContext()
					.getRequestMap()
					.put("MetadataCore_items",
							this.getSelected().getMetadataCores());
		}
		return "/mantenedor/metadataCore/index";
	}

	/**
	 * Sets the "selected" attribute of the Usuario controller in order to
	 * display its data in a dialog. This is reusing existing the existing View
	 * dialog.
	 * 
	 * @param event
	 *            Event object for the widget that triggered an action
	 */
	public void prepareUsuario(ActionEvent event) {
		if (this.getSelected() != null
				&& usuarioController.getSelected() == null) {
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
			FacesContext
					.getCurrentInstance()
					.getExternalContext()
					.getRequestMap()
					.put("ProgramaAno_items",
							this.getSelected().getProgramasAnos());
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
		// if (this.getSelected() != null) {
		// FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Componente_items",
		// this.getSelected().get getComponentes());
		// }
		return "/mantenedor/componente/index";
	}

	public List<MantenedorProgramaVO> getProgramas() {
		if (programas == null) {
			if (programasAnoSiguiente) {
				programas = mantenedoresService
						.getAllMantenedorProgramaVO(anoEnCurso + 1);
				if (programas == null || programas.size() == 0) {

				}
			} else {
				programas = mantenedoresService
						.getAllMantenedorProgramaVO(anoEnCurso);
			}

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
		System.out.println("seleccionado --> " + this.seleccionado);
	}

	public List<Usuario> getUsuarios() {
		if (usuarios == null) {
			usuarios = usuarioDAO.getUserAll();
		}
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
	
	public String onFlowProcessEdit(FlowEvent event) {
		System.out.println("event.getOldStep-->" + event.getOldStep());
		System.out.println("event.getNewStep-->" + event.getNewStep());

		String ejecutarJavaScrip = null;
		Boolean guardar = false;
		errorMessage = "";
		if (event.getOldStep().equalsIgnoreCase("datosPrograma") && event.getNewStep().equalsIgnoreCase("componentesPrograma")) {
			if(seleccionado == null ||  seleccionado.getNombrePrograma() == null || seleccionado.getNombrePrograma().trim().isEmpty()){
				errorMessage = "Debe ingresar el nombre";
				PrimeFacesUtil.ejecutarJavaScript("levantarError();");
				return event.getOldStep();
			}else if(seleccionado == null || seleccionado.getNombreUsuario() == null || seleccionado.getNombreUsuario().trim().isEmpty()){
				errorMessage = "Debe seleccionar el Usuario";
				PrimeFacesUtil.ejecutarJavaScript("levantarError();");
				return event.getOldStep();
			}else if(seleccionado == null || seleccionado.getDescripcion() == null || seleccionado.getDescripcion().trim().isEmpty()){
				errorMessage = "Debe ingresar la descripción del programa";
				PrimeFacesUtil.ejecutarJavaScript("levantarError();");
				return event.getOldStep();
			}else if(this.fechasDiaRemesas == null || this.fechasDiaRemesas.getTarget().isEmpty()){
				errorMessage = "Debe seleccionar por lo menos un día de remesa";
				PrimeFacesUtil.ejecutarJavaScript("levantarError();");
				return event.getOldStep();
			}
		}
		
		
		

		if (event.getOldStep().equalsIgnoreCase("componentesPrograma")
				&& event.getNewStep().equalsIgnoreCase("cuotas")) {
			
			
			if(this.componentes == null || this.componentes.getTarget().isEmpty()){
				errorMessage = "Debe seleccionar los componentes del programa";
				PrimeFacesUtil.ejecutarJavaScript("levantarError();");
				return event.getOldStep();
			}else{
				if (tipoPrograma.equals(TipoComponenteEnum.HISTORICO.getId())) {
					for (String nombreComponente : componentes.getTarget()) {
						Componente componente = componenteService
								.getComponenteByNombre(nombreComponente);
						if (componente.getTipoComponente().getId()
								.equals(tipoPrograma)) {
							guardar = true;
							break;
						}
					}
					if (!guardar) {
						PrimeFacesUtil.ejecutarJavaScript("noGuardarPrograma();");
						return event.getOldStep();
					}
				} else if (tipoPrograma.equals(TipoComponenteEnum.PXQ.getId())) {
					if (seleccionado.getIdTipoPrograma().equals(
							TipoComponenteEnum.HISTORICO.getId())) {
						for (String nombreComponente : componentes.getTarget()) {
							Componente componente = componenteService
									.getComponenteByNombre(nombreComponente);
							if (componente.getTipoComponente().getId()
									.equals(TipoComponenteEnum.HISTORICO.getId())) {
								PrimeFacesUtil
										.ejecutarJavaScript("noGuardarProgramaDesdeHistoricoHastaPxQ();");
								guardar = false;
								return event.getOldStep();
							}
						}

					}
				}
				
			}

		}

		if ("true".equalsIgnoreCase(continuarWizard)) {
			return event.getNewStep();
		}
		return event.getOldStep();
	}
	
	
	public String onFlowProcessCreate(FlowEvent event) {
		System.out.println("event.getOldStep-->" + event.getOldStep());
		System.out.println("event.getNewStep-->" + event.getNewStep());

		String ejecutarJavaScrip = null;
		Boolean guardar = false;
		errorMessage = "";
		if (event.getOldStep().equalsIgnoreCase("datosPrograma") && event.getNewStep().equalsIgnoreCase("componentesPrograma")) {
			if(seleccionado == null ||  seleccionado.getNombrePrograma() == null || seleccionado.getNombrePrograma().trim().isEmpty()){
				errorMessage = "Debe ingresar el nombre";
				PrimeFacesUtil.ejecutarJavaScript("levantarError();");
				return event.getOldStep();
			}else if(seleccionado == null || seleccionado.getNombreUsuario() == null || seleccionado.getNombreUsuario().trim().isEmpty()){
				errorMessage = "Debe seleccionar el Usuario";
				PrimeFacesUtil.ejecutarJavaScript("levantarError();");
				return event.getOldStep();
			}else if(seleccionado == null || seleccionado.getDescripcion() == null || seleccionado.getDescripcion().trim().isEmpty()){
				errorMessage = "Debe ingresar la descripción del programa";
				PrimeFacesUtil.ejecutarJavaScript("levantarError();");
				return event.getOldStep();
			}else if(this.fechasDiaRemesas == null || this.fechasDiaRemesas.getTarget().isEmpty()){
				errorMessage = "Debe seleccionar por lo menos un día de remesa";
				PrimeFacesUtil.ejecutarJavaScript("levantarError();");
				return event.getOldStep();
			}
			
		}

		if (event.getOldStep().equalsIgnoreCase("componentesPrograma") && event.getNewStep().equalsIgnoreCase("cuotas")) {
			if(this.componentes == null || this.componentes.getTarget().isEmpty()){
				errorMessage = "Debe seleccionar los componentes del programa";
				PrimeFacesUtil.ejecutarJavaScript("levantarError();");
				return event.getOldStep();
			}
			

			if (tipoPrograma.equals(TipoComponenteEnum.HISTORICO.getId())) {
				for (String nombreComponente : componentes.getTarget()) {
					Componente componente = componenteService
							.getComponenteByNombre(nombreComponente);
					if (componente.getTipoComponente().getId()
							.equals(tipoPrograma)) {
						guardar = true;
						break;
					}
				}
				if (!guardar) {
					PrimeFacesUtil.ejecutarJavaScript("noGuardarPrograma();");
					return event.getOldStep();
				}
			}

		}
		

		if ("true".equalsIgnoreCase(continuarWizard)) {
			return event.getNewStep();
		}
		return event.getOldStep();
	}
	
	
	public void revisarError(){
		System.out.println("revisarError-->"+errorMessage);
		if(errorMessage != null && !errorMessage.trim().isEmpty()){
			 FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, new String(errorMessage.trim()), null);
		     FacesContext.getCurrentInstance().addMessage(null, msg);
		     errorMessage = "";
		}
	}
	
	public void listenerAgregaCuota(){
		System.out.println("llega al action listener");
		Integer porcentajeAcumuladoCuotas = 0;
		Boolean agregarCuota = true;
		if(this.porcentaje_cuota == null || this.porcentaje_cuota == 0){
			errorMessage = "Debe ingresar el porcentaje de la cuota";
			agregarCuota = false;
			PrimeFacesUtil.ejecutarJavaScript("levantarError();");
			
		}else if(this.fecha_cuota == null){
			if(cuotas == null || cuotas.size() == 0){
				this.firstCuota = true;
			}else{
				this.firstCuota = false;
			}
			if(this.firstCuota){
				agregarCuota = true;
			}else{
				agregarCuota = false;
				errorMessage = "Debe ingresar la fecha de la cuota";
				PrimeFacesUtil.ejecutarJavaScript("levantarError();");
			}
		}else{
			if(cuotas.get(cuotas.size() - 1).getFecha_cuota() == null){
				agregarCuota = true;
			}else{
				if(this.fecha_cuota.after(cuotas.get(cuotas.size() - 1).getFecha_cuota())){
					agregarCuota = true;
				}else{
					agregarCuota = false;
					errorMessage = "La fecha de la nueva cuota debe ser superior a la de la última cuota de la lista";
					PrimeFacesUtil.ejecutarJavaScript("levantarError();");
				}
			}
			
		}
		
		if(agregarCuota){
			for (MantenedorCuotasVO cuotasVO : cuotas) {
				porcentajeAcumuladoCuotas = porcentajeAcumuladoCuotas
						+ cuotasVO.getPorcentaje_cuota();

			}
			porcentajeAcumuladoCuotas += getPorcentaje_cuota();
			
			
			if(porcentajeAcumuladoCuotas == 100){
				permitirGuardarPrograma = true;
			}else{
				permitirGuardarPrograma = false;
			}
			
			if (porcentajeAcumuladoCuotas > 100) {
				cuotaCienPorciento = true;
				errorMessage = "El porcentaje de las cuotas supera el 100%";
				PrimeFacesUtil.ejecutarJavaScript("levantarError();");
			} else {
				//PrimeFacesUtil.ejecutarJavaScript("agregarFila();");
				MantenedorCuotasVO cuota = new MantenedorCuotasVO();
				cuota.setPorcentaje_cuota(porcentaje_cuota);
				if(this.fecha_cuota != null){
					cuota.setFecha_cuota(fecha_cuota);
				}
				cuota.setNroCuota(cuotas.size() + 1);
				cuotas.add(cuota);
				if(cuotas.size() > 0){
					this.firstCuota = false;
				}
				clearField();
			}
		}
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
		System.out.println("firstCuota --> " + firstCuota);
		this.firstCuota = firstCuota;
	}

	public Boolean getProgramasAnoSiguiente() {
		return programasAnoSiguiente;
	}

	public void setProgramasAnoSiguiente(Boolean programasAnoSiguiente) {
		this.programasAnoSiguiente = programasAnoSiguiente;
	}

	public Integer getAnoEnCurso() {
		return anoEnCurso;
	}

	public void setAnoEnCurso(Integer anoEnCurso) {
		this.anoEnCurso = anoEnCurso;
	}

	public DualListModel<String> getComponentes() {
		return componentes;
	}

	public void setComponentes(DualListModel<String> componentes) {
		this.componentes = componentes;
	}

	public DualListModel<String> getFechasDiaRemesas() {
		List<String> diasRemesasSource = new ArrayList<String>();
		List<String> diasRemesasTarget = new ArrayList<String>();

		if (fechasDiaRemesas == null) {
			diasRemesasSource = mantenedoresService.getDiasNumeroFechaRemesas();
			fechasDiaRemesas = new DualListModel<String>(diasRemesasSource,
					diasRemesasTarget);
		}

		return fechasDiaRemesas;
	}

	public void setFechasDiaRemesas(DualListModel<String> fechasDiaRemesas) {
		this.fechasDiaRemesas = fechasDiaRemesas;
	}

	public Integer getTipoPrograma() {
		return tipoPrograma;
	}

	public void setTipoPrograma(Integer tipoPrograma) {
		this.tipoPrograma = tipoPrograma;
	}

	public List<TipoComponenteVO> getTipoComponentes() {
		if (tipoComponentes == null) {
			tipoComponentes = mantenedoresService.getTiposComponente();
		}
		return tipoComponentes;
	}

	public void setTipoComponentes(List<TipoComponenteVO> tipoComponentes) {
		this.tipoComponentes = tipoComponentes;
	}

	public Boolean getCuotaCienPorciento() {
		return cuotaCienPorciento;
	}

	public void setCuotaCienPorciento(Boolean cuotaCienPorciento) {
		this.cuotaCienPorciento = cuotaCienPorciento;
	}

	public Boolean getIngresoCamposCuotasObligatorio() {
		return ingresoCamposCuotasObligatorio;
	}

	public void setIngresoCamposCuotasObligatorio(
			Boolean ingresoCamposCuotasObligatorio) {
		this.ingresoCamposCuotasObligatorio = ingresoCamposCuotasObligatorio;
	}

	public Integer getAnoFinal() {
		return anoFinal;
	}

	public void setAnoFinal(Integer anoFinal) {
		this.anoFinal = anoFinal;
	}

	public String getContinuarWizard() {
		return continuarWizard;
	}

	public void setContinuarWizard(String continuarWizard) {
		this.continuarWizard = continuarWizard;
	}

	public Boolean getReiniciarDistribucionAps() {
		return reiniciarDistribucionAps;
	}

	public void setReiniciarDistribucionAps(Boolean reiniciarDistribucionAps) {
		this.reiniciarDistribucionAps = reiniciarDistribucionAps;
	}

	public Boolean getReiniciarFlujoCaja() {
		return reiniciarFlujoCaja;
	}

	public void setReiniciarFlujoCaja(Boolean reiniciarFlujoCaja) {
		this.reiniciarFlujoCaja = reiniciarFlujoCaja;
	}

	public Boolean getReiniciarConvenio() {
		return reiniciarConvenio;
	}

	public void setReiniciarConvenio(Boolean reiniciarConvenio) {
		this.reiniciarConvenio = reiniciarConvenio;
	}

	public Boolean getReiniciarReliquidacion() {
		return reiniciarReliquidacion;
	}

	public void setReiniciarReliquidacion(Boolean reiniciarReliquidacion) {
		this.reiniciarReliquidacion = reiniciarReliquidacion;
	}

	public Boolean getReiniciarOT() {
		return reiniciarOT;
	}

	public void setReiniciarOT(Boolean reiniciarOT) {
		this.reiniciarOT = reiniciarOT;
	}

	public Boolean getReiniciarModificacionAps() {
		return reiniciarModificacionAps;
	}

	public void setReiniciarModificacionAps(Boolean reiniciarModificacionAps) {
		this.reiniciarModificacionAps = reiniciarModificacionAps;
	}

	public Boolean getPermitirGuardarPrograma() {
		return permitirGuardarPrograma;
	}

	public void setPermitirGuardarPrograma(Boolean permitirGuardarPrograma) {
		this.permitirGuardarPrograma = permitirGuardarPrograma;
	}

	public Integer getTotalPorcentajeCuotas() {
		totalPorcentajeCuotas = 0;
		if(cuotas != null && cuotas.size() > 0){
			for(MantenedorCuotasVO mantenedorCuotasVO : cuotas){
				totalPorcentajeCuotas += ((mantenedorCuotasVO.getPorcentaje_cuota() != null) ? mantenedorCuotasVO.getPorcentaje_cuota() : 0);
			}
		}
		return totalPorcentajeCuotas;
	}

	public void setTotalPorcentajeCuotas(Integer totalPorcentajeCuotas) {
		this.totalPorcentajeCuotas = totalPorcentajeCuotas;
	}


}
