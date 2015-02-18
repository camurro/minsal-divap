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

import minsal.divap.enums.EstadosConvenios;
import minsal.divap.enums.Subtitulo;
import minsal.divap.enums.TiposPrograma;
import minsal.divap.service.ComponenteService;
import minsal.divap.service.ComunaService;
import minsal.divap.service.ConveniosService;
import minsal.divap.service.EstablecimientosService;
import minsal.divap.service.ProgramasService;
import minsal.divap.vo.CargaConvenioComunaComponenteVO;
import minsal.divap.vo.CargaConvenioServicioComponenteVO;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ComunaVO;
import minsal.divap.vo.ConveniosVO;
import minsal.divap.vo.EstablecimientoVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.SubtituloVO;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import cl.redhat.bandejaTareas.controller.BaseController;

@Named ( "procesoConveniosController" ) 
@ViewScoped 
public class ProcesoConveniosController extends BaseController implements Serializable {
	private static final long serialVersionUID = 8979055329731411696L;
	private List<ProgramaVO> programas;
	private List<ComunaVO> comunas = new ArrayList<ComunaVO>();
	private List<EstablecimientoVO> establecimientos = new ArrayList<EstablecimientoVO>();
	private List<ComponentesVO> componentes;
	private List<CargaConvenioComunaComponenteVO> resolucionConveniosMunicipal;
	private List<CargaConvenioServicioComponenteVO> resolucionConveniosServicioSub21;
	private List<CargaConvenioServicioComponenteVO> resolucionConveniosServicioSub22;
	private List<CargaConvenioServicioComponenteVO> resolucionConveniosServicioSub29;
	private List<ConveniosVO> resolucionesServicios;
	private List<ConveniosVO> resolucionesMunicipal;
	private CargaConvenioServicioComponenteVO convenioServicio;
	private CargaConvenioComunaComponenteVO convenioComuna;
	private Subtitulo subtituloSeleccionado = null;
	private ProgramaVO programa;
	private Boolean sub21;
	private Boolean sub22;
	private Boolean sub24;
	private Boolean sub29;
	private String convenioSeleccionado;
	private String programaSeleccionado;
	private String comunaSeleccionada;
	private String componenteSeleccionado;
	private String establecimientoSeleccionado21;
	private String establecimientoSeleccionado22;
	private String establecimientoSeleccionado29;
	private UploadedFile plantillaFile;
	private Integer rowIndexMunicipal;
	private Integer rowIndexServicio21;
	private Integer rowIndexServicio22;
	private Integer rowIndexServicio29;
	private List<ConveniosVO> resolucionesServicios21;
	private List<ConveniosVO> resolucionesServicios22;
	private List<ConveniosVO> resolucionesServicios29;
	private Boolean leyRetiro;
	private Date currentDate;
	private Integer montoAporteEstatal;
	private Integer montoAdicionalComplementario;
	private Integer totalLey;
	private Integer ano;
	
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

	@PostConstruct 
	public void init() {
		System.out.println("ProcesoConveniosController Alcanzado.");
		if(sessionExpired()){
			return;
		}
		setSub21(false);
		setSub22(false);
		setSub24(false);
		setSub29(false);
		setLeyRetiro(false);
		setComunaSeleccionada("0");
		setEstablecimientoSeleccionado21("0");
		setEstablecimientoSeleccionado22("0");
		setEstablecimientoSeleccionado29("0");
		setProgramaSeleccionado("0");
		setComponenteSeleccionado("0");
		setPrograma(null);
		if(getServicio() != null && getServicio().getId_servicio() != null){
			setComunas(comunaService.getComunasByServicio(getServicio().getId_servicio()));
			setEstablecimientos(establecimientosService.getEstablecimientosByServicio(getServicio().getId_servicio()));
		}
	}

	public String cargaConvenioSub24(Integer rowIndex){
		System.out.println("cargaConvenioSub24 rowIndex="+rowIndex);
		rowIndexMunicipal = rowIndex;
		convenioComuna = getResolucionConveniosMunicipal().get(rowIndex);
		return null;
	}

	public String cargaConvenioSub21(Integer rowIndex){
		System.out.println("cargaConvenioSub21 rowIndex="+rowIndex);
		rowIndexServicio21 = rowIndex;
		convenioServicio = getResolucionConveniosServicioSub21().get(rowIndex);
		subtituloSeleccionado = Subtitulo.SUBTITULO21;
		return null;
	}

	public String cargaConvenioSub22(Integer rowIndex){
		System.out.println("cargaConvenioSub22 rowIndex="+rowIndex);
		rowIndexServicio22 = rowIndex;
		convenioServicio = getResolucionConveniosServicioSub22().get(rowIndex);
		subtituloSeleccionado = Subtitulo.SUBTITULO22;
		return null;
	}

	public String cargaConvenioSub29(Integer rowIndex){
		System.out.println("cargaConvenioSub29 rowIndex="+rowIndex);
		rowIndexServicio29 = rowIndex;
		convenioServicio = getResolucionConveniosServicioSub29().get(rowIndex);
		subtituloSeleccionado = Subtitulo.SUBTITULO29;
		return null;
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
		if(getProgramaSeleccionado() != null && !"0".equals(getProgramaSeleccionado())){
			ProgramaVO programaVO = programasService.getProgramaAno(Integer.parseInt(getProgramaSeleccionado()));
			componentes = componenteService.getComponenteByPrograma(programaVO.getId());
		}else{
			componentes = new ArrayList<ComponentesVO>();
		}
		return componentes;
	}

	public void resetSubtitulos(){
		setComunaSeleccionada("0");
		setEstablecimientoSeleccionado21("0");
		setEstablecimientoSeleccionado22("0");
		setEstablecimientoSeleccionado29("0");
		resolucionConveniosMunicipal = new ArrayList<CargaConvenioComunaComponenteVO>();
		resolucionConveniosServicioSub21 = new ArrayList<CargaConvenioServicioComponenteVO>();
		resolucionConveniosServicioSub22 = new ArrayList<CargaConvenioServicioComponenteVO>();
		resolucionConveniosServicioSub29 = new ArrayList<CargaConvenioServicioComponenteVO>();
	}

	public void resetResolucionConveniosServicioSub21(){
		resolucionConveniosServicioSub21 = new ArrayList<CargaConvenioServicioComponenteVO>();
	}

	public void resetResolucionConveniosServicioSub22(){
		resolucionConveniosServicioSub22 = new ArrayList<CargaConvenioServicioComponenteVO>();
	}

	public void resetResolucionConveniosMunicipal(){
		resolucionConveniosMunicipal = new ArrayList<CargaConvenioComunaComponenteVO>();
	}

	public void resetResolucionConveniosServicioSub29(){
		resolucionConveniosServicioSub29 = new ArrayList<CargaConvenioServicioComponenteVO>();
	}

	public List<CargaConvenioComunaComponenteVO> getResolucionConveniosMunicipal() {
		return resolucionConveniosMunicipal;
	}

	public void setResolucionConveniosMunicipal(
			List<CargaConvenioComunaComponenteVO> resolucionConveniosMunicipal) {
		this.resolucionConveniosMunicipal = resolucionConveniosMunicipal;
	}

	public List<CargaConvenioServicioComponenteVO> getResolucionConveniosServicioSub21() {
		return resolucionConveniosServicioSub21;
	}

	public void setResolucionConveniosServicioSub21(
			List<CargaConvenioServicioComponenteVO> resolucionConveniosServicioSub21) {
		this.resolucionConveniosServicioSub21 = resolucionConveniosServicioSub21;
	}

	public List<ProgramaVO> getProgramas() {
		if(programas == null){
			programas = programasService.getProgramasByUser(getLoggedUsername(), getAno());
		}
		return programas;
	}

	public void cargarComponentesPorPrograma(){
		System.out.println("ProcesoConveniosController::cargarComponentesPorPrograma programaSeleccionado->" + getProgramaSeleccionado());
		setSub21(false);
		setSub22(false);
		setSub24(false);
		setSub29(false);
		setLeyRetiro(false);
		resetSubtitulos();
		if(getProgramaSeleccionado() != null && !"0".equals(getProgramaSeleccionado())){
			programa = programasService.getProgramaAno(Integer.parseInt(getProgramaSeleccionado()));
			System.out.println("programa.getDependenciaMunicipal()->" + programa.getDependenciaMunicipal() + " programa.getDependenciaServicio()->" + programa.getDependenciaServicio());
			componentes = componenteService.getComponenteByPrograma(programa.getId());
			if(componentes != null && componentes.size() > 0){
				for(ComponentesVO componentesVO : componentes){
					if(TiposPrograma.ProgramaLey.getId().equals(componentesVO.getTipoComponente().getId())){
						setLeyRetiro(true);
					}
					for(SubtituloVO subtituloVO : componentesVO.getSubtitulos()){
						if(Subtitulo.SUBTITULO21.getId().equals(subtituloVO.getId()) && !getSub21()){
							setSub21(true);
						}else if(Subtitulo.SUBTITULO22.getId().equals(subtituloVO.getId()) && !getSub22()){
							setSub22(true);
						}else if(Subtitulo.SUBTITULO24.getId().equals(subtituloVO.getId()) && !getSub24()){
							setSub24(true);
						}else if(Subtitulo.SUBTITULO29.getId().equals(subtituloVO.getId()) && !getSub29()){
							setSub29(true);
						}
					}
				}
			}
		}else{
			componentes = new ArrayList<ComponentesVO>();
			programa = null;
		}
	}

	public void buscar(){
		System.out.println("cargarDatos");
		if(componenteSeleccionado == null || componenteSeleccionado.trim().isEmpty() || componenteSeleccionado.trim().equals("0")){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe seleccionar el componente antes de realizar la búsqueda", null);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}else{
			setSub21(false);
			setSub22(false);
			setSub24(false);
			setSub29(false);
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
				if(Subtitulo.SUBTITULO21.getId().equals(subtituloVO.getId()) && !getSub21()){
					setSub21(true);
					Integer idEstablecimientoSub21 = ((establecimientoSeleccionado21 == null || establecimientoSeleccionado21.trim().isEmpty() || establecimientoSeleccionado21.trim().equals("0")) ? null : Integer.parseInt(establecimientoSeleccionado21));
					System.out.println("Subtitulo.SUBTITULO21.getId()="+Subtitulo.SUBTITULO21.getId());
					System.out.println("idEstablecimientoSub21="+idEstablecimientoSub21);
					resolucionConveniosServicioSub21 = conveniosService.getResolucionConveniosServicio(getServicio().getId_servicio(), getPrograma().getIdProgramaAno(), idComponente, idEstablecimientoSub21, Subtitulo.SUBTITULO21);
				}else if(Subtitulo.SUBTITULO22.getId().equals(subtituloVO.getId()) && !getSub22()){
					setSub22(true);
					Integer idEstablecimientoSub22 = ((establecimientoSeleccionado22 == null || establecimientoSeleccionado22.trim().isEmpty() || establecimientoSeleccionado22.trim().equals("0"))?null:Integer.parseInt(establecimientoSeleccionado22));
					System.out.println("Subtitulo.SUBTITULO22.getId()="+Subtitulo.SUBTITULO22.getId());
					System.out.println("idEstablecimientoSub22="+idEstablecimientoSub22);
					resolucionConveniosServicioSub22 = conveniosService.getResolucionConveniosServicio(getServicio().getId_servicio(), getPrograma().getIdProgramaAno(), idComponente, idEstablecimientoSub22, Subtitulo.SUBTITULO22);
				}else if(Subtitulo.SUBTITULO24.getId().equals(subtituloVO.getId()) && !getSub24()){
					setSub24(true);
					Integer idComuna = ((comunaSeleccionada == null || comunaSeleccionada.trim().isEmpty() || comunaSeleccionada.trim().equals("0")) ? null : Integer.parseInt(comunaSeleccionada));
					System.out.println("Subtitulo.SUBTITULO24.getId()="+Subtitulo.SUBTITULO24.getId());
					System.out.println("idComuna="+idComuna);
					resolucionConveniosMunicipal = conveniosService.getResolucionConveniosMunicipal(getServicio().getId_servicio(), getPrograma().getIdProgramaAno(), idComponente, idComuna);
				}else if(Subtitulo.SUBTITULO29.getId().equals(subtituloVO.getId()) && !getSub29()){
					setSub29(true);
					Integer idEstablecimientoSub29 = ((establecimientoSeleccionado29 == null || establecimientoSeleccionado29.trim().isEmpty() || establecimientoSeleccionado29.trim().equals("0"))?null:Integer.parseInt(establecimientoSeleccionado29));
					System.out.println("Subtitulo.SUBTITULO29.getId()="+Subtitulo.SUBTITULO29.getId());
					System.out.println("idEstablecimientoSub22="+idEstablecimientoSub29);
					resolucionConveniosServicioSub29 = conveniosService.getResolucionConveniosServicio(getServicio().getId_servicio(), getPrograma().getIdProgramaAno(), idComponente, idEstablecimientoSub29, Subtitulo.SUBTITULO29);
				}
			} 
		}
		System.out.println("resolucionConveniosServicioSub21.size() --> " +((this.resolucionConveniosServicioSub29==null)?"0":this.resolucionConveniosServicioSub29.size()));
		System.out.println("resolucionConveniosServicioSub22.size() --> " +((this.resolucionConveniosServicioSub22==null)?"0":this.resolucionConveniosServicioSub22.size()));
		System.out.println("resolucionConveniosMunicipal.size() --> " +((this.resolucionConveniosMunicipal==null)?"0":this.resolucionConveniosMunicipal.size()));
		System.out.println("resolucionConveniosServicioSub29.size() --> " +((this.resolucionConveniosServicioSub29==null)?"0":this.resolucionConveniosServicioSub29.size()));
		System.out.println("fin buscar-->");

	}

	public String guardarConvenioServicio(){
		System.out.println("guardarConvenioServicio()");
		String mensaje = "El archivo fue cargado correctamente.";
		FacesMessage msg = null;
		if (plantillaFile != null) {
			try {
				String filename = plantillaFile.getFileName();
				byte[] contentConvenioFile = plantillaFile.getContents();
				Integer docConvenio = persistFile(filename, contentConvenioFile);
				conveniosService.moveConvenioToAlfresco(docConvenio);
				CargaConvenioServicioComponenteVO cargaConvenioServicioComponenteVO = conveniosService.guardarConvenioServicioComponente(getPrograma().getIdProgramaAno(), convenioServicio, docConvenio);
				switch (subtituloSeleccionado) {
				case SUBTITULO21:
					getResolucionConveniosServicioSub21().set(rowIndexServicio21, cargaConvenioServicioComponenteVO);
					break;
				case SUBTITULO22:
					getResolucionConveniosServicioSub22().set(rowIndexServicio22, cargaConvenioServicioComponenteVO);
					break;
				case SUBTITULO29:
					getResolucionConveniosServicioSub29().set(rowIndexServicio29, cargaConvenioServicioComponenteVO);
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
	}

	public String guardarConvenioComuna(){
		System.out.println("guardarConvenioServicio()");
		String mensaje = "El archivo fue cargado correctamente.";
		FacesMessage msg = null;
		if (plantillaFile != null) {
			try {
				String filename = plantillaFile.getFileName();
				byte[] contentConvenioFile = plantillaFile.getContents();
				Integer docConvenio = persistFile(filename, contentConvenioFile);
				conveniosService.moveConvenioToAlfresco(docConvenio);
				CargaConvenioComunaComponenteVO cargaConvenioComunaComponenteVO = conveniosService.guardarConvenioComunaComponente(getPrograma().getIdProgramaAno(), convenioComuna, docConvenio);
				getResolucionConveniosMunicipal().set(this.rowIndexMunicipal, cargaConvenioComunaComponenteVO);
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
	
	public String guardarLeyRetiro(){
		System.out.println("guardarLeyRetiro()");
		String mensaje = "El archivo fue cargado correctamente.";
		FacesMessage msg = null;
		if (plantillaFile != null) {
			try {
				String filename = plantillaFile.getFileName();
				byte[] contentConvenioFile = plantillaFile.getContents();
				Integer docConvenio = persistFile(filename, contentConvenioFile);
				conveniosService.moveConvenioToAlfresco(docConvenio);
				CargaConvenioComunaComponenteVO cargaConvenioComunaComponenteVO = conveniosService.guardarLeyRetiro(getPrograma().getIdProgramaAno(), convenioComuna, docConvenio);
				getResolucionConveniosMunicipal().set(this.rowIndexMunicipal, cargaConvenioComunaComponenteVO);
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

	public String getEstablecimientoSeleccionado21() {
		return establecimientoSeleccionado21;
	}

	public void setEstablecimientoSeleccionado21(
			String establecimientoSeleccionado21) {
		this.establecimientoSeleccionado21 = establecimientoSeleccionado21;
	}

	public String getComunaSeleccionada() {
		return comunaSeleccionada;
	}

	public void setComunaSeleccionada(String comunaSeleccionada) {
		this.comunaSeleccionada = comunaSeleccionada;
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

	public String getConvenioSeleccionado() {
		return convenioSeleccionado;
	}

	public void setConvenioSeleccionado(String convenioSeleccionado) {
		this.convenioSeleccionado = convenioSeleccionado;
	}

	public CargaConvenioServicioComponenteVO getConvenioServicio() {
		return convenioServicio;
	}

	public void setConvenioServicio(
			CargaConvenioServicioComponenteVO convenioServicio) {
		this.convenioServicio = convenioServicio;
	}

	public CargaConvenioComunaComponenteVO getConvenioComuna() {
		return convenioComuna;
	}

	public void setConvenioComuna(CargaConvenioComunaComponenteVO convenioComuna) {
		this.convenioComuna = convenioComuna;
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

	public List<ConveniosVO> getResolucionesServicios() {
		return resolucionesServicios;
	}

	public void setResolucionesServicios(List<ConveniosVO> resolucionesServicios) {
		this.resolucionesServicios = resolucionesServicios;
	}

	public List<ConveniosVO> getResolucionesMunicipal() {
		return resolucionesMunicipal;
	}

	public void setResolucionesMunicipal(List<ConveniosVO> resolucionesMunicipal) {
		this.resolucionesMunicipal = resolucionesMunicipal;
	}

	public Boolean getSub21() {
		return sub21;
	}

	public void setSub21(Boolean sub21) {
		this.sub21 = sub21;
	}

	public Boolean getSub22() {
		return sub22;
	}

	public void setSub22(Boolean sub22) {
		this.sub22 = sub22;
	}

	public Boolean getSub24() {
		return sub24;
	}

	public void setSub24(Boolean sub24) {
		this.sub24 = sub24;
	}

	public Boolean getSub29() {
		return sub29;
	}

	public void setSub29(Boolean sub29) {
		this.sub29 = sub29;
	}

	public String getEstablecimientoSeleccionado22() {
		return establecimientoSeleccionado22;
	}

	public void setEstablecimientoSeleccionado22(
			String establecimientoSeleccionado22) {
		this.establecimientoSeleccionado22 = establecimientoSeleccionado22;
	}

	public String getEstablecimientoSeleccionado29() {
		return establecimientoSeleccionado29;
	}

	public void setEstablecimientoSeleccionado29(
			String establecimientoSeleccionado29) {
		this.establecimientoSeleccionado29 = establecimientoSeleccionado29;
	}

	public List<CargaConvenioServicioComponenteVO> getResolucionConveniosServicioSub22() {
		return resolucionConveniosServicioSub22;
	}

	public void setResolucionConveniosServicioSub22(
			List<CargaConvenioServicioComponenteVO> resolucionConveniosServicioSub22) {
		this.resolucionConveniosServicioSub22 = resolucionConveniosServicioSub22;
	}

	public List<CargaConvenioServicioComponenteVO> getResolucionConveniosServicioSub29() {
		return resolucionConveniosServicioSub29;
	}

	public void setResolucionConveniosServicioSub29(
			List<CargaConvenioServicioComponenteVO> resolucionConveniosServicioSub29) {
		this.resolucionConveniosServicioSub29 = resolucionConveniosServicioSub29;
	}

	public List<ConveniosVO> getResolucionesServicios21() {
		return resolucionesServicios21;
	}

	public void setResolucionesServicios21(List<ConveniosVO> resolucionesServicios21) {
		this.resolucionesServicios21 = resolucionesServicios21;
	}

	public List<ConveniosVO> getResolucionesServicios22() {
		return resolucionesServicios22;
	}

	public void setResolucionesServicios22(List<ConveniosVO> resolucionesServicios22) {
		this.resolucionesServicios22 = resolucionesServicios22;
	}

	public List<ConveniosVO> getResolucionesServicios29() {
		return resolucionesServicios29;
	}

	public void setResolucionesServicios29(List<ConveniosVO> resolucionesServicios29) {
		this.resolucionesServicios29 = resolucionesServicios29;
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
			ano = 2016;
		}
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

}
