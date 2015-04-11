package cl.minsal.divap.controller;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import minsal.divap.enums.Subtitulo;
import minsal.divap.enums.TiposPrograma;
import minsal.divap.service.ComponenteService;
import minsal.divap.service.ComunaService;
import minsal.divap.service.ConveniosService;
import minsal.divap.service.EstablecimientosService;
import minsal.divap.service.ProgramasService;
import minsal.divap.service.SubtituloService;
import minsal.divap.vo.CargaConvenioComponenteSubtituloVO;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ComunaVO;
import minsal.divap.vo.ConvenioComponenteSubtituloVO;
import minsal.divap.vo.DependenciaVO;
import minsal.divap.vo.EstablecimientoVO;
import minsal.divap.vo.ItemVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.SubtituloVO;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.UploadedFile;

import cl.redhat.bandejaTareas.controller.BaseController;

@Named ( "procesoConveniosController" ) 
@ViewScoped 
public class ProcesoConveniosController extends BaseController implements Serializable {
	private static final long serialVersionUID = 8979055329731411696L;
	private List<ConvenioComponenteSubtituloVO> orderList = new ArrayList<ConvenioComponenteSubtituloVO>();
	private Integer monto;
	private String docIdDownload;
	private String dependenciaSeleccionado;
	private List<DependenciaVO> dependencias;
	private String establecimientoSeleccionado;
	private List<ItemVO> item;
	private String itemSeleccionado;
	private Integer numeroResolucion;
	private Integer totalElmentos;
	private Integer docConvenio = null;
	
	private List<ProgramaVO> programas;
	private List<ComunaVO> comunas = new ArrayList<ComunaVO>();
	private List<EstablecimientoVO> establecimientos = new ArrayList<EstablecimientoVO>();
	private List<ComponentesVO> componentes;
	private List<SubtituloVO> subtitulos;
	private ProgramaVO programa;
	private String programaSeleccionado;
	private String componenteSeleccionado;
	private UploadedFile plantillaFile;
	private Boolean leyRetiro;
	private Date currentDate;
	private Integer montoAporteEstatal;
	private Integer montoAdicionalComplementario;
	private Integer totalLey;
	private Integer ano;
	private String subtituloSeleccionado;
	@EJB
	private ProgramasService programasService;
	@EJB
	private ComponenteService componenteService;
	@EJB
	private ComunaService comunaService;
	@EJB
	private EstablecimientosService establecimientosService;
	@EJB
	private ConveniosService conveniosService;
	@EJB
	private SubtituloService subtituloService;
	
	@PostConstruct 
	public void init() {
		System.out.println("ProcesoConveniosController Alcanzado.");
		if(sessionExpired()){
			return;
		}
		setLeyRetiro(false);
		setProgramaSeleccionado(null);
		setComponenteSeleccionado(null);
		setPrograma(null);
		setTotalElmentos(0);
		if(getServicio() != null && getServicio().getId_servicio() != null){
			setComunas(comunaService.getComunasByServicio(getServicio().getId_servicio()));
			setEstablecimientos(establecimientosService.getEstablecimientosByServicio(getServicio().getId_servicio()));
		}
	}

	public void setEstablecimientos(List<EstablecimientoVO> establecimientos) {
		this.establecimientos = establecimientos;
	}

	public List<EstablecimientoVO> getEstablecimientos() {
		return establecimientos;
	}

	public void setComunas(List<ComunaVO> comunas) {
		this.comunas = comunas;
	}

	public List<ComunaVO> getComunas() {
		return comunas;
	}

	public List<ComponentesVO> getComponentes() {
		return componentes;
	}

	public void cargaSubtitulo(){
		System.out.println("cargaSubtitulo componenteSeleccionado="+componenteSeleccionado);
		if(componenteSeleccionado != null && !componenteSeleccionado.trim().isEmpty()){
			subtitulos = new ArrayList<SubtituloVO>();
			ComponentesVO componente = componenteService.getComponenteById(Integer.parseInt(componenteSeleccionado));
			if(dependenciaSeleccionado.equals("1")){
				for(SubtituloVO subtituloVO : componente.getSubtitulos()){
					 if(Subtitulo.SUBTITULO24.getId().equals(subtituloVO.getId())){
						 subtitulos.add(subtituloVO);
					 }
				}
			}else{
				for(SubtituloVO subtituloVO : componente.getSubtitulos()){
					 if(Subtitulo.SUBTITULO21.getId().equals(subtituloVO.getId()) || Subtitulo.SUBTITULO22.getId().equals(subtituloVO.getId()) || Subtitulo.SUBTITULO29.getId().equals(subtituloVO.getId())){
						 subtitulos.add(subtituloVO);
					 }
				}
			}
			if(subtitulos.size() == 1){
				subtituloSeleccionado = subtitulos.get(0).getId().toString();
			}
		}else{
			subtituloSeleccionado = "";
			subtitulos = new ArrayList<SubtituloVO>();
		}
	}

	public List<ProgramaVO> getProgramas() {
		if(programas == null){
			programas = programasService.getProgramasByUser(getLoggedUsername(), getAno());
		}
		return programas;
	}

	public void cargarComponentesPorPrograma(){
		System.out.println("ProcesoConveniosController::cargarComponentesPorPrograma programaSeleccionado->" + getProgramaSeleccionado());
		setLeyRetiro(false);
		dependenciaSeleccionado = "";
		item = new ArrayList<ItemVO>();
		componentes = new ArrayList<ComponentesVO>();
		if(getProgramaSeleccionado() != null && !"".equals(getProgramaSeleccionado())){
			subtituloSeleccionado = "";
			subtitulos = new ArrayList<SubtituloVO>();
			programa = programasService.getProgramaAno(Integer.parseInt(getProgramaSeleccionado()));
			System.out.println("programa.getDependenciaMunicipal()->" + programa.getDependenciaMunicipal() + " programa.getDependenciaServicio()->" + programa.getDependenciaServicio());
			dependencias = new ArrayList<DependenciaVO>();
			if(programa.getDependenciaMunicipal()){
				dependencias.add(new DependenciaVO(1, "Municipal"));
			}
			if(programa.getDependenciaServicio()){
				dependencias.add(new DependenciaVO(2, "Servicio"));
			}
			if(dependencias != null && dependencias.size() == 1){
				dependenciaSeleccionado = dependencias.get(0).getId().toString();
				List<ComponentesVO> componentesTmp = componenteService.getComponenteByPrograma(programa.getId());
				if(componentesTmp != null && componentesTmp.size() > 0){
					for(ComponentesVO componentesVO : componentesTmp){
						if(TiposPrograma.ProgramaLey.getId().equals(componentesVO.getTipoComponente().getId())){
							setLeyRetiro(true);
						}
					}
				}
				if(dependenciaSeleccionado.equals("1")){
					if(componentesTmp != null && componentesTmp.size() > 0){
						for(ComponentesVO componentesVO : componentesTmp){
							for(SubtituloVO subtituloVO : componentesVO.getSubtitulos()){
								 if(Subtitulo.SUBTITULO24.getId().equals(subtituloVO.getId())){
									 componentes.add(componentesVO);
									 break;
								 }
							}
						}
					}
					for(ComunaVO comuna: getComunas()){
						item.add(new ItemVO(comuna.getIdComuna(), comuna.getDescComuna()));
					}
				}else{
					if(componentesTmp != null && componentesTmp.size() > 0){
						for(ComponentesVO componentesVO : componentesTmp){
							for(SubtituloVO subtituloVO : componentesVO.getSubtitulos()){
								 if(Subtitulo.SUBTITULO21.getId().equals(subtituloVO.getId()) || Subtitulo.SUBTITULO22.getId().equals(subtituloVO.getId()) || Subtitulo.SUBTITULO29.getId().equals(subtituloVO.getId())){
									 componentes.add(componentesVO);
									 break;
								 }
							}
						}
					}
					for(EstablecimientoVO establecimiento: getEstablecimientos()){
						item.add(new ItemVO(establecimiento.getId(), establecimiento.getNombre()));
					}
				}
			}
		}else{
			programa = null;
			itemSeleccionado = null;
			item = new ArrayList<ItemVO>();
			componenteSeleccionado = "";
			componentes = new ArrayList<ComponentesVO>();
			subtituloSeleccionado = "";
			subtitulos = new ArrayList<SubtituloVO>();
			monto = null;
			numeroResolucion = null;
			totalElmentos = 0;
			plantillaFile = null;
			orderList = new ArrayList<ConvenioComponenteSubtituloVO>();
		}
	}
	
	public void cargarComunaEstablecimiento(){
		System.out.println("cargarComunaEstablecimiento dependenciaSeleccionado="+dependenciaSeleccionado);
		if(dependenciaSeleccionado != null && !dependenciaSeleccionado.equals("0")){
			item = new ArrayList<ItemVO>();
			componentes = new ArrayList<ComponentesVO>();
			componenteSeleccionado = "";
			orderList = new ArrayList<ConvenioComponenteSubtituloVO>();
			itemSeleccionado = null;
			List<ComponentesVO> componentesTmp = componenteService.getComponenteByPrograma(programa.getId());
			if(dependenciaSeleccionado.equals("1")){
				if(componentesTmp != null && componentesTmp.size() > 0){
					for(ComponentesVO componentesVO : componentesTmp){
						for(SubtituloVO subtituloVO : componentesVO.getSubtitulos()){
							 if(Subtitulo.SUBTITULO24.getId().equals(subtituloVO.getId())){
								 componentes.add(componentesVO);
								 break;
							 }
						}
					}
				}
				for(ComunaVO comuna: getComunas()){
					item.add(new ItemVO(comuna.getIdComuna(), comuna.getDescComuna()));
				}
			}else{
				if(componentesTmp != null && componentesTmp.size() > 0){
					for(ComponentesVO componentesVO : componentesTmp){
						for(SubtituloVO subtituloVO : componentesVO.getSubtitulos()){
							 if(Subtitulo.SUBTITULO21.getId().equals(subtituloVO.getId()) || Subtitulo.SUBTITULO22.getId().equals(subtituloVO.getId()) || Subtitulo.SUBTITULO29.getId().equals(subtituloVO.getId())){
								 componentes.add(componentesVO);
								 break;
							 }
						}
					}
				}
				for(EstablecimientoVO establecimiento: getEstablecimientos()){
					item.add(new ItemVO(establecimiento.getId(), establecimiento.getNombre()));
				}
			}
		}else{
			item = new ArrayList<ItemVO>();
			componenteSeleccionado = "";
			componentes = new ArrayList<ComponentesVO>();
			orderList = new ArrayList<ConvenioComponenteSubtituloVO>();
			itemSeleccionado = null;
			subtituloSeleccionado = "";
			subtitulos = new ArrayList<SubtituloVO>();
		}
	}
	
	public void setearComunaEstablecimiento(){
		System.out.println("itemSeleccionado =" + itemSeleccionado);
		componenteSeleccionado = "";
		orderList = new ArrayList<ConvenioComponenteSubtituloVO>();
		subtituloSeleccionado = ""; 
	}
	

	/*public void buscar(){
		System.out.println("cargarDatos");
		if(componenteSeleccionado == null || componenteSeleccionado.trim().isEmpty()){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe seleccionar el componente antes de realizar la búsqueda", null);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}else{
			setLeyRetiro(false);
			Integer idComponente = Integer.parseInt(componenteSeleccionado);
			ComponentesVO componentesVO = componenteService.getComponenteById(idComponente);
			if(componentesVO != null){
				if(TiposPrograma.ProgramaLey.getId().equals(componentesVO.getTipoComponente().getId())){
					setLeyRetiro(true);
				}
			}
			System.out.println("getPrograma().getIdProgramaAno()="+getPrograma().getIdProgramaAno());
			System.out.println("idComponente="+idComponente);
			System.out.println("idServicio=" + getServicio().getId_servicio());
			System.out.println("EstadosConvenios="+EstadosConvenios.INGRESADO.getId());
			for(SubtituloVO subtituloVO : componentesVO.getSubtitulos()){
				if(Subtitulo.SUBTITULO21.getId().equals(subtituloVO.getId())){
					Integer idEstablecimientoSub21 = ((establecimientoSeleccionado21 == null || establecimientoSeleccionado21.trim().isEmpty() || establecimientoSeleccionado21.trim().equals("0")) ? null : Integer.parseInt(establecimientoSeleccionado21));
					System.out.println("Subtitulo.SUBTITULO21.getId()="+Subtitulo.SUBTITULO21.getId());
					System.out.println("idEstablecimientoSub21="+idEstablecimientoSub21);
				}else if(Subtitulo.SUBTITULO22.getId().equals(subtituloVO.getId())){
					Integer idEstablecimientoSub22 = ((establecimientoSeleccionado22 == null || establecimientoSeleccionado22.trim().isEmpty() || establecimientoSeleccionado22.trim().equals("0"))?null:Integer.parseInt(establecimientoSeleccionado22));
					System.out.println("Subtitulo.SUBTITULO22.getId()="+Subtitulo.SUBTITULO22.getId());
					System.out.println("idEstablecimientoSub22="+idEstablecimientoSub22);
				}else if(Subtitulo.SUBTITULO24.getId().equals(subtituloVO.getId())){
					Integer idComuna = ((comunaSeleccionada == null || comunaSeleccionada.trim().isEmpty() || comunaSeleccionada.trim().equals("0")) ? null : Integer.parseInt(comunaSeleccionada));
					System.out.println("Subtitulo.SUBTITULO24.getId()="+Subtitulo.SUBTITULO24.getId());
					System.out.println("idComuna="+idComuna);
				}else if(Subtitulo.SUBTITULO29.getId().equals(subtituloVO.getId())){
					Integer idEstablecimientoSub29 = ((establecimientoSeleccionado29 == null || establecimientoSeleccionado29.trim().isEmpty() || establecimientoSeleccionado29.trim().equals("0"))?null:Integer.parseInt(establecimientoSeleccionado29));
					System.out.println("Subtitulo.SUBTITULO29.getId()="+Subtitulo.SUBTITULO29.getId());
					System.out.println("idEstablecimientoSub22="+idEstablecimientoSub29);
				}
			} 
		}
		System.out.println("fin buscar-->");

	}*/

	/*public String guardarConvenioServicio(){
		System.out.println("guardarConvenioServicio()");
		String mensaje = "El archivo fue cargado correctamente.";
		FacesMessage msg = null;
		if (plantillaFile != null) {
			try {
				String filename = plantillaFile.getFileName();
				byte[] contentConvenioFile = plantillaFile.getContents();
				Integer docConvenio = persistFile(filename, contentConvenioFile);
				conveniosService.moveConvenioToAlfresco(docConvenio, getAno());
				//CargaConvenioServicioComponenteVO cargaConvenioServicioComponenteVO = conveniosService.guardarConvenioServicioComponente(getPrograma().getIdProgramaAno(), convenioServicio, docConvenio);
				switch (subtituloSeleccionado) {
				case SUBTITULO21:
					//getResolucionConveniosServicioSub21().set(rowIndexServicio21, cargaConvenioServicioComponenteVO);
					break;
				case SUBTITULO22:
					//getResolucionConveniosServicioSub22().set(rowIndexServicio22, cargaConvenioServicioComponenteVO);
					break;
				case SUBTITULO29:
					//getResolucionConveniosServicioSub29().set(rowIndexServicio29, cargaConvenioServicioComponenteVO);
					break;
				default:
					break;
				}
				msg = new FacesMessage(FacesMessage.SEVERITY_INFO, mensaje, null);
			} catch (Exception e) {
				msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
				e.printStackTrace();
			}
		} else {
			mensaje = "El archivo no fuero cargado.";
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, null);
		}
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return null;
	}*/

	public String guardarConvenioComuna(){
		System.out.println("guardarConvenioServicio()");
		String mensaje = "El archivo fue cargado correctamente.";
		FacesMessage msg = null;
		if (plantillaFile != null) {
			try {
				String filename = plantillaFile.getFileName();
				byte[] contentConvenioFile = plantillaFile.getContents();
				Integer docConvenio = persistFile(filename, contentConvenioFile);
				conveniosService.moveConvenioToAlfresco(docConvenio, getAno());
				//CargaConvenioComunaComponenteVO cargaConvenioComunaComponenteVO = conveniosService.guardarConvenioComunaComponente(getPrograma().getIdProgramaAno(), convenioComuna, docConvenio);
				//getResolucionConveniosMunicipal().set(this.rowIndexMunicipal, cargaConvenioComunaComponenteVO);
				msg = new FacesMessage(FacesMessage.SEVERITY_INFO, mensaje, null);
			} catch (Exception e) {
				msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
				e.printStackTrace();
			}
		} else {
			mensaje = "El archivo no fue cargado.";
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, null);
		}
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return null;
	}

	public String guardarLeyRetiro(){
		System.out.println("guardarLeyRetiro()");
		String mensaje = "El archivo fue cargado correctamente.";
		FacesMessage msg = null;
		if (plantillaFile != null) {
			try {
				String filename = plantillaFile.getFileName();
				byte[] contentConvenioFile = plantillaFile.getContents();
				Integer docConvenio = persistFile(filename, contentConvenioFile);
				conveniosService.moveConvenioToAlfresco(docConvenio, getAno());
				//CargaConvenioComunaComponenteVO cargaConvenioComunaComponenteVO = conveniosService.guardarLeyRetiro(getPrograma().getIdProgramaAno(), convenioComuna, docConvenio);
				//getResolucionConveniosMunicipal().set(this.rowIndexMunicipal, cargaConvenioComunaComponenteVO);
				msg = new FacesMessage(FacesMessage.SEVERITY_INFO, mensaje, null);
			} catch (Exception e) {
				msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
				e.printStackTrace();
			}
		} else {
			mensaje = "El archivo no fuero cargado.";
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, null);
		}
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return null;
	}

	public String getComponenteSeleccionado() {
		return componenteSeleccionado;
	}

	public void setComponenteSeleccionado(String componenteSeleccionado) {
		this.componenteSeleccionado = componenteSeleccionado;
	}

	public void setComponentes(List<ComponentesVO> componentes) {
		this.componentes = componentes;
	}

	public void setProgramas(List<ProgramaVO> programas) {
		this.programas = programas;
	}

	public String getProgramaSeleccionado() {
		return programaSeleccionado;
	}

	public void setProgramaSeleccionado(String programaSeleccionado) {
		this.programaSeleccionado = programaSeleccionado;
	}

	public UploadedFile getPlantillaFile() {
		return plantillaFile;
	}

	public void handleFileUploadConvenioFile(FileUploadEvent event) {
		FacesMessage msg = new FacesMessage("Succesful", event.getFile()
				.getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		System.out.println("handleFileUploadConvenioFile-->");
	}

	public void setPlantillaFile(UploadedFile plantillaFile) {
		this.plantillaFile = plantillaFile;
	}

	public ProgramaVO getPrograma() {
		return programa;
	}

	public void setPrograma(ProgramaVO programa) {
		this.programa = programa;
	}

	public Boolean getLeyRetiro() {
		return leyRetiro;
	}

	public void setLeyRetiro(Boolean leyRetiro) {
		this.leyRetiro = leyRetiro;
	}

	public Date getCurrentDate() {
		if(currentDate == null){
			currentDate = new Date();
		}
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}

	public Integer getMontoAporteEstatal() {
		if(montoAporteEstatal == null){
			montoAporteEstatal = 0;
		}
		return montoAporteEstatal;
	}

	public void setMontoAporteEstatal(Integer montoAporteEstatal) {
		this.montoAporteEstatal = montoAporteEstatal;
	}

	public Integer getMontoAdicionalComplementario() {
		if(montoAdicionalComplementario == null){
			montoAdicionalComplementario = 0;
		}
		return montoAdicionalComplementario;
	}

	public void setMontoAdicionalComplementario(Integer montoAdicionalComplementario) {
		this.montoAdicionalComplementario = montoAdicionalComplementario;
	}

	public Integer getTotalLey() {
		if(totalLey == null){
			totalLey = 0;
		}
		return totalLey;
	}

	public void setTotalLey(Integer totalLey) {
		this.totalLey = totalLey;
	}

	public Integer getAno() {
		if(ano == null){
			ano = conveniosService.getAnoCurso();
		}
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	/*public String addAction() {
		ComponentesVO componente = componenteService.getComponenteById(Integer.parseInt(componenteSeleccionado));
		SubtituloVO subtitulo = subtituloService.getSubtituloVOById(Integer.parseInt(subtituloSeleccionados));
		OrderBeanVO orderitem = new OrderBeanVO(componente, subtitulo, monto);
		orderList.add(orderitem);
		return null;
	}*/
	
	public void addAction() {
		System.out.println("Inicio addAction");
		ComponentesVO componente = componenteService.getComponenteById(Integer.parseInt(componenteSeleccionado));
		SubtituloVO subtitulo = subtituloService.getSubtituloVOById(Integer.parseInt(subtituloSeleccionado));
		ConvenioComponenteSubtituloVO orderitem = new ConvenioComponenteSubtituloVO(componente, subtitulo, monto);
		int posicion = orderList.indexOf(orderitem);
		if(posicion == -1){
			orderList.add(orderitem);
		}else{
			orderList.set(posicion, orderitem);
		}
		setTotalElmentos(orderList.size());
		componenteSeleccionado = "";
		subtituloSeleccionado = "";
		monto = null;
		System.out.println("Fin addAction");
	}
	
	public String limpiar() {
		System.out.println("Inicio limpiar");
		programaSeleccionado = null;
		dependenciaSeleccionado = null;
		dependencias = new ArrayList<DependenciaVO>();
		numeroResolucion = null;
		itemSeleccionado = null;
		item = new ArrayList<ItemVO>();
		orderList = new ArrayList<ConvenioComponenteSubtituloVO>();
		componenteSeleccionado = null;
		componentes = new ArrayList<ComponentesVO>();
		subtituloSeleccionado = null;
		subtitulos = new ArrayList<SubtituloVO>();
		currentDate = new Date();
		setTotalElmentos(0);
		System.out.println("Fin limpiar");
		return null;
	}
	
	public void cargarArchivo(){
		FacesMessage msg = null;
		boolean errorCargandoArchivo = false;
		if (plantillaFile != null) {
			try {
				String filename = plantillaFile.getFileName();
				byte[] contentConvenioFile = plantillaFile.getContents();
				docConvenio = persistFile(filename, contentConvenioFile);
				conveniosService.moveConvenioToAlfresco(docConvenio, getAno());
				System.out.println("docConvenio="+docConvenio);
			} catch (Exception e) {
				errorCargandoArchivo = true;
				msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
				e.printStackTrace();
			}
		} else {
			errorCargandoArchivo = true;
			String mensaje = "El archivo no fue cargado.";
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, null);
		}
		if(errorCargandoArchivo){
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}else{
			CargaConvenioComponenteSubtituloVO cargaConvenioComponenteSubtituloVO = formToConvenio();
			cargaConvenioComponenteSubtituloVO.setDocumento(docConvenio);
			conveniosService.guardar(cargaConvenioComponenteSubtituloVO);
			limpiar();
			String mensaje = "Convenio guardado correctamente.";
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, mensaje, null);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
	
	public String guardar(){
		FacesMessage msg = null;
		CargaConvenioComponenteSubtituloVO cargaConvenioComponenteSubtituloVO = formToConvenio();
		cargaConvenioComponenteSubtituloVO.setDocumento(docConvenio);
		conveniosService.guardar(cargaConvenioComponenteSubtituloVO);
		limpiar();
		String mensaje = "Convenio guardado correctamente.";
		msg = new FacesMessage(FacesMessage.SEVERITY_INFO, mensaje, null);
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return null;
	}
	
	private CargaConvenioComponenteSubtituloVO formToConvenio() {
		CargaConvenioComponenteSubtituloVO cargaConvenioComponenteSubtituloVO = new CargaConvenioComponenteSubtituloVO();
		cargaConvenioComponenteSubtituloVO.setItem(Integer.parseInt(itemSeleccionado));
		if(dependenciaSeleccionado.equals("1")){
			cargaConvenioComponenteSubtituloVO.setDependenciaMuncipal(true);
		}else{
			cargaConvenioComponenteSubtituloVO.setDependenciaMuncipal(false);			
		}
		cargaConvenioComponenteSubtituloVO.setNumeroResolucion(numeroResolucion);
		cargaConvenioComponenteSubtituloVO.setFechaResolucion(currentDate);
		cargaConvenioComponenteSubtituloVO.setPrograma(getPrograma());
		cargaConvenioComponenteSubtituloVO.setConvenioComponentesSubtitulosVO(orderList);
		return cargaConvenioComponenteSubtituloVO;
	}

	public void onEdit(RowEditEvent event) {  
		FacesMessage msg = new FacesMessage("Registro Modificado",((ConvenioComponenteSubtituloVO) event.getObject()).getSubtitulo().getNombre());  
		FacesContext.getCurrentInstance().addMessage(null, msg);  
	}  

	public void onCancel(RowEditEvent event) {  
		FacesMessage msg = new FacesMessage("Registro Eliminado");   
		FacesContext.getCurrentInstance().addMessage(null, msg); 
		orderList.remove((ConvenioComponenteSubtituloVO) event.getObject());
		setTotalElmentos(orderList.size());
	}

	public List<ConvenioComponenteSubtituloVO> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<ConvenioComponenteSubtituloVO> orderList) {
		this.orderList = orderList;
	}

	public Integer getMonto() {
		return monto;
	}

	public void setMonto(Integer monto) {
		this.monto = monto;
	}

	public List<SubtituloVO> getSubtitulos() {
		return subtitulos;
	}

	public void setSubtitulos(List<SubtituloVO> subtitulos) {
		this.subtitulos = subtitulos;
	}

	public String getSubtituloSeleccionado() {
		return subtituloSeleccionado;
	}

	public void setSubtituloSeleccionado(String subtituloSeleccionado) {
		this.subtituloSeleccionado = subtituloSeleccionado;
	}

	public String getDocIdDownload() {
		return docIdDownload;
	}

	public void setDocIdDownload(String docIdDownload) {
		this.docIdDownload = docIdDownload;
	}
	
	public String downloadTemplate() {
		Integer docDownload = Integer.valueOf(Integer.parseInt(getDocIdDownload()));
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
		return null;
	}

	public String getDependenciaSeleccionado() {
		return dependenciaSeleccionado;
	}

	public void setDependenciaSeleccionado(String dependenciaSeleccionado) {
		this.dependenciaSeleccionado = dependenciaSeleccionado;
	}

	public List<DependenciaVO> getDependencias() {
		return dependencias;
	}

	public void setDependencias(List<DependenciaVO> dependencias) {
		this.dependencias = dependencias;
	}

	public String getEstablecimientoSeleccionado() {
		return establecimientoSeleccionado;
	}

	public void setEstablecimientoSeleccionado(String establecimientoSeleccionado) {
		this.establecimientoSeleccionado = establecimientoSeleccionado;
	}

	public List<ItemVO> getItem() {
		return item;
	}

	public void setItem(List<ItemVO> item) {
		this.item = item;
	}

	public String getItemSeleccionado() {
		return itemSeleccionado;
	}

	public void setItemSeleccionado(String itemSeleccionado) {
		this.itemSeleccionado = itemSeleccionado;
	}

	public Integer getNumeroResolucion() {
		return numeroResolucion;
	}

	public void setNumeroResolucion(Integer numeroResolucion) {
		this.numeroResolucion = numeroResolucion;
	}

	public Integer getTotalElmentos() {
		return totalElmentos;
	}

	public void setTotalElmentos(Integer totalElmentos) {
		this.totalElmentos = totalElmentos;
	}
		
}
