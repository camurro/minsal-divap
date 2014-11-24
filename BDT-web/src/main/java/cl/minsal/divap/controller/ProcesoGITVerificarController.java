package cl.minsal.divap.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import minsal.divap.enums.EstadosConvenios;
import minsal.divap.enums.Subtitulo;
import minsal.divap.service.ComponenteService;
import minsal.divap.service.ComunaService;
import minsal.divap.service.ConveniosService;
import minsal.divap.service.EstablecimientosService;
import minsal.divap.service.ProgramasService;
import minsal.divap.service.ServicioSaludService;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ComunaVO;
import minsal.divap.vo.ConvenioComunaComponenteVO;
import minsal.divap.vo.ConvenioServicioComponenteVO;
import minsal.divap.vo.ConveniosVO;
import minsal.divap.vo.EstablecimientoVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ServiciosVO;
import minsal.divap.vo.SubtituloVO;
import cl.redhat.bandejaTareas.task.AbstractTaskMBean;


@Named ( "procesoGITVerificarController" ) 
@ViewScoped 
public class ProcesoGITVerificarController extends AbstractTaskMBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9029285397119804637L;
	private ProgramaVO programa;
	private Integer idConvenio;
	private String docIdDownload;
	private List<SubtituloVO>  subtitulos;
	private List<ConveniosVO> convenios;
	private Integer programaSeleccionado;
	private Boolean sub21;
	private Boolean sub22;
	private Boolean sub24;
	private Boolean sub29;
	private Boolean rectificar_;
	private String componenteSeleccionado;
	private String servicioSeleccionado;
	private String comunaSeleccionada24;
	private String establecimientoSeleccionado21;
	private String establecimientoSeleccionado22;
	private String establecimientoSeleccionado29;
	private boolean convenioServicioComponenteSub21Seleccionado;
	private boolean convenioServicioComponenteSub22Seleccionado;
	private boolean convenioServicioComponenteSub29Seleccionado;
	private boolean convenioServicioComponenteSub24Seleccionado;
	private Long totalMarcoSub24;
	private Long totalConveniosSub24;
	private Long totalConveniosPendientesSub24;
	private Long totalMarcoSub21;
	private Long totalConveniosSub21;
	private Long totalConveniosPendientesSub21;
	private Long totalMarcoSub22;
	private Long totalConveniosSub22;
	private Long totalConveniosPendientesSub22;
	private Long totalMarcoSub29;
	private Long totalConveniosSub29;
	private Long totalConveniosPendientesSub29;
	private List<ComunaVO> comunasSub24;
	private List<ServiciosVO> servicios;
	private List<EstablecimientoVO> establecimientos;
	private List<ConvenioComunaComponenteVO> conveniosComunaComponenteSub24;
	private List<ConvenioServicioComponenteVO> conveniosServicioComponenteSub21;
	private List<ConvenioServicioComponenteVO> conveniosServicioComponenteSub22;
	private List<ConvenioServicioComponenteVO> conveniosServicioComponenteSub29;
	private boolean conveniosPendientes = false;
	private boolean busquedaRealizada = false;
	private boolean mostrarBotonReparos = false;
	@EJB
	private ConveniosService conveniosService;
	@EJB
	private ComponenteService componenteService;
	@EJB
	private ProgramasService programaService;
	@EJB
	private ComunaService comunaService;
	@EJB
	private EstablecimientosService establecimientosService;
	@EJB
	private ServicioSaludService serviciosService;

	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new  HashMap<String, Object>();
		parameters.put("rectificar_", isRectificar_() );
		Integer idServicio = ((servicioSeleccionado == null || servicioSeleccionado.trim().isEmpty() || servicioSeleccionado.trim().equals("0"))?null:Integer.parseInt(servicioSeleccionado));
		if(idServicio != null){
			parameters.put("servicioSeleccionado_", idServicio);
		}
		return parameters;
	}

	@Override
	public String iniciarProceso() {
		return null;
	}

	public String  informarReparos(){
		return  enviar();
	}

	@Override
	public String enviar(){
		return super.enviar();
	}

	@PostConstruct 
	public void init() {
		if(sessionExpired()){
			return;
		}
		setSub21(false);
		setSub22(false);
		setSub24(false);
		setSub29(false);
		setBusquedaRealizada(false);
		servicios = serviciosService.getServiciosOrderId();
		servicioSeleccionado = servicios.get(0).getId_servicio() + "";
		if (getTaskDataVO() != null && getTaskDataVO().getData() != null) {
			programaSeleccionado = (Integer) getTaskDataVO().getData().get("_programaSeleccionado");
			idConvenio = (Integer) getTaskDataVO().getData().get("_idConvenio");
			System.out.println("programaSeleccionado --->"+ programaSeleccionado);
			System.out.println("idConvenio --->"+ idConvenio);
			System.out.println("servicioSeleccionado --->"+ servicioSeleccionado);
			setPrograma(programaService.getProgramaAnoPorID(programaSeleccionado));
			componenteSeleccionado = getPrograma().getComponentes().get(0).getId().toString();
			for(SubtituloVO subtituloVO : getPrograma().getComponentes().get(0).getSubtitulos()){
				if(Subtitulo.SUBTITULO21.getId().equals(subtituloVO.getId()) && !getSub21()){
					setSub21(true);
				}else if(Subtitulo.SUBTITULO22.getId().equals(subtituloVO.getId()) && !getSub22()){
					setSub22(true);
				}else if(Subtitulo.SUBTITULO24.getId().equals(subtituloVO.getId()) && !getSub24()){
					setSub24(true);
					comunasSub24 = comunaService.getComunasByServicio(Integer.parseInt(servicioSeleccionado));
				}else if(Subtitulo.SUBTITULO29.getId().equals(subtituloVO.getId()) && !getSub29()){
					setSub29(true);
				}
			}
			if(getSub21() || getSub22() || getSub29()){
				establecimientos = establecimientosService.getEstablecimientosByServicio(Integer.parseInt(servicioSeleccionado));
			}
		}
		System.out.println("comunasSub24.size() --> " +((this.comunasSub24==null)?"0":this.comunasSub24.size()));
		System.out.println("establecimientos.size() --> " +((this.establecimientos==null)?"0":this.establecimientos.size()));
		System.out.println("conveniosServicioComponenteSub21.size() --> " +((this.conveniosServicioComponenteSub21==null)?"0":this.conveniosServicioComponenteSub21.size()));
		System.out.println("conveniosServicioComponenteSub22.size() --> " +((this.conveniosServicioComponenteSub22==null)?"0":this.conveniosServicioComponenteSub22.size()));
		System.out.println("conveniosComunaComponenteSub24.size() --> " +((this.conveniosComunaComponenteSub24==null)?"0":this.conveniosComunaComponenteSub24.size()));
		System.out.println("conveniosServicioComponenteSub29.size() --> " +((this.conveniosServicioComponenteSub29==null)?"0":this.conveniosServicioComponenteSub29.size()));
		System.out.println("fin init-->");
	}

	public List<ComunaVO> getComunasSub24() {
		return comunasSub24;
	}

	public void setComunasSub24(List<ComunaVO> comunasSub24) {
		this.comunasSub24 = comunasSub24;
	}

	public String downloadConvenio() {
		Integer docDownload = Integer.valueOf(Integer.parseInt(getDocIdDownload()));
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
		return null;
	}

	public List<ConveniosVO> getConvenios() {
		return convenios;
	}

	public void setConvenios(List<ConveniosVO> convenios) {
		this.convenios = convenios;
	}

	public ProgramaVO getPrograma() {
		return programa;
	}

	public void setPrograma(ProgramaVO programa) {
		this.programa = programa;
	}

	public Integer getProgramaSeleccionado() {
		return programaSeleccionado;
	}

	public void setProgramaSeleccionado(Integer programaSeleccionado) {
		this.programaSeleccionado = programaSeleccionado;
	}

	public String getComponenteSeleccionado() {
		return componenteSeleccionado;
	}

	public void setComponenteSeleccionado(String componenteSeleccionado) {
		this.componenteSeleccionado = componenteSeleccionado;
	}

	public List<SubtituloVO> getSubtitulos() {
		return subtitulos;
	}

	public void setSubtitulos(List<SubtituloVO> subtitulos) {
		this.subtitulos = subtitulos;
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

	public String getEstablecimientoSeleccionado21() {
		return establecimientoSeleccionado21;
	}

	public void setEstablecimientoSeleccionado21(
			String establecimientoSeleccionado21) {
		this.establecimientoSeleccionado21 = establecimientoSeleccionado21;
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

	public String getComunaSeleccionada24() {
		return comunaSeleccionada24;
	}

	public void setComunaSeleccionada24(String comunaSeleccionada24) {
		this.comunaSeleccionada24 = comunaSeleccionada24;
	}

	public boolean isRectificar_() {
		return rectificar_;
	}

	public void setRectificar_(boolean rectificar_) {
		this.rectificar_ = rectificar_;
	}

	public List<ServiciosVO> getServicios() {
		if(servicios == null){
			servicios = serviciosService.getAllServiciosVO();
		}
		return servicios;
	}

	public String getServicioSeleccionado() {
		return servicioSeleccionado;
	}

	public void setServicioSeleccionado(String servicioSeleccionado) {
		this.servicioSeleccionado = servicioSeleccionado;
	}

	public void setServicios(List<ServiciosVO> servicios) {
		this.servicios = servicios;
	}
	
	public void cargarEstablecimientosComunas(){
		Integer idServicio = ((servicioSeleccionado == null || servicioSeleccionado.trim().isEmpty() || servicioSeleccionado.trim().equals("0"))?null:Integer.parseInt(servicioSeleccionado));
		setBusquedaRealizada(false);
		System.out.println("cargarEstablecimientosComunas idServicio="+idServicio);
		if(idServicio == null){
			establecimientos = new ArrayList<EstablecimientoVO>();
			comunasSub24 = new ArrayList<ComunaVO>();
		}else{
			establecimientos = establecimientosService.getEstablecimientosByServicio(idServicio);
			comunasSub24 = comunaService.getComunasByServicio(idServicio); 
		}
		conveniosServicioComponenteSub21 = new ArrayList<ConvenioServicioComponenteVO>();
		conveniosServicioComponenteSub22  = new ArrayList<ConvenioServicioComponenteVO>() ;
		conveniosComunaComponenteSub24  = new ArrayList<ConvenioComunaComponenteVO>() ;
		conveniosServicioComponenteSub29  = new ArrayList<ConvenioServicioComponenteVO>() ;
		comunaSeleccionada24 = "0";
		establecimientoSeleccionado21 = "0";
		establecimientoSeleccionado22 = "0";
		establecimientoSeleccionado29 = "0";
		totalMarcoSub21 = null;
		totalConveniosSub21 = null;
		totalConveniosPendientesSub21 = null;
		totalMarcoSub22 = null;
		totalConveniosSub22 = null;
		totalConveniosPendientesSub22 = null;
		totalMarcoSub24 = null;
		totalConveniosSub24 = null;
		totalConveniosPendientesSub24 = null;
		totalMarcoSub29 = null;
		totalConveniosSub29 = null;
		totalConveniosPendientesSub29 = null;
	}
	
	public void buscar() {
		System.out.println("buscar--> .componenteSeleccionado=" + componenteSeleccionado + " servicioSeleccionado=" + servicioSeleccionado);
		if(componenteSeleccionado == null || componenteSeleccionado.trim().isEmpty() || componenteSeleccionado.trim().equals("0")){
			FacesMessage msg = new FacesMessage("Debe seleccionar el componente antes de realizar la búsqueda");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			setBusquedaRealizada(false);
		}else{
			setSub21(false);
			setSub22(false);
			setSub24(false);
			setSub29(false);
			setBusquedaRealizada(true);
			Integer idComponente = Integer.parseInt(componenteSeleccionado);
			ComponentesVO componentesVO = componenteService.getComponenteById(idComponente);
			Integer idServicio = ((servicioSeleccionado == null || servicioSeleccionado.trim().isEmpty() || servicioSeleccionado.trim().equals("0"))?null:Integer.parseInt(servicioSeleccionado));
			System.out.println("getPrograma().getIdProgramaAno()="+getPrograma().getIdProgramaAno());
			System.out.println("idComponente="+idComponente);
			System.out.println("idServicio="+idServicio);
			System.out.println("EstadosConvenios="+EstadosConvenios.INGRESADO.getId());
			for(SubtituloVO subtituloVO : componentesVO.getSubtitulos()){
				if(Subtitulo.SUBTITULO21.getId().equals(subtituloVO.getId()) && !getSub21()){
					setSub21(true);
					Integer idEstablecimientoSub21 = ((establecimientoSeleccionado21 == null || establecimientoSeleccionado21.trim().isEmpty() || establecimientoSeleccionado21.trim().equals("0"))?null:Integer.parseInt(establecimientoSeleccionado21));
					System.out.println("Subtitulo.SUBTITULO21.getId()="+Subtitulo.SUBTITULO21.getId());
					System.out.println("idEstablecimientoSub21="+idEstablecimientoSub21);
					if(idEstablecimientoSub21 == null){
						this.conveniosServicioComponenteSub21 = conveniosService.getConveniosServicioComponenteByProgramaAnoComponenteSubtituloServicioConvenioEstadoConvenio(getPrograma().getIdProgramaAno(), idComponente, Subtitulo.SUBTITULO21.getId(), idServicio, idConvenio,EstadosConvenios.INGRESADO);
					}else{
						this.conveniosServicioComponenteSub21 = conveniosService.getConveniosServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoConvenioEstadoConvenio(getPrograma().getIdProgramaAno(), idComponente, Subtitulo.SUBTITULO21.getId(), idEstablecimientoSub21, idConvenio, EstadosConvenios.INGRESADO);
					}
					if(this.conveniosServicioComponenteSub21 != null && this.conveniosServicioComponenteSub21.size() > 0){
						calcularTotalesSub21();
					}
				}else if(Subtitulo.SUBTITULO22.getId().equals(subtituloVO.getId()) && !getSub22()){
					setSub22(true);
					Integer idEstablecimientoSub22 = ((establecimientoSeleccionado22 == null || establecimientoSeleccionado22.trim().isEmpty() || establecimientoSeleccionado22.trim().equals("0"))?null:Integer.parseInt(establecimientoSeleccionado22));
					System.out.println("Subtitulo.SUBTITULO22.getId()="+Subtitulo.SUBTITULO22.getId());
					System.out.println("idEstablecimientoSub22="+idEstablecimientoSub22);
					if(idEstablecimientoSub22 == null){
						this.conveniosServicioComponenteSub22 = conveniosService.getConveniosServicioComponenteByProgramaAnoComponenteSubtituloServicioConvenioEstadoConvenio(getPrograma().getIdProgramaAno(), idComponente, Subtitulo.SUBTITULO22.getId(), idServicio, idConvenio, EstadosConvenios.INGRESADO);
					}else{
						this.conveniosServicioComponenteSub22 = conveniosService.getConveniosServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoConvenioEstadoConvenio(getPrograma().getIdProgramaAno(), idComponente, Subtitulo.SUBTITULO22.getId(), idEstablecimientoSub22, idConvenio, EstadosConvenios.INGRESADO);
					}
					if(this.conveniosServicioComponenteSub22 != null && this.conveniosServicioComponenteSub22.size() > 0){
						calcularTotalesSub22();
					}
				}else if(Subtitulo.SUBTITULO24.getId().equals(subtituloVO.getId()) && !getSub24()){
					setSub24(true);
					Integer idComuna = ((comunaSeleccionada24 == null || comunaSeleccionada24.trim().isEmpty() || comunaSeleccionada24.trim().equals("0"))?null:Integer.parseInt(comunaSeleccionada24));
					System.out.println("Subtitulo.SUBTITULO24.getId()="+Subtitulo.SUBTITULO24.getId());
					System.out.println("idComuna="+idComuna);
					if(idComuna == null){
						this.conveniosComunaComponenteSub24 = conveniosService.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloServicioConvenioEstadoConvenio(getPrograma().getIdProgramaAno(), idComponente, Subtitulo.SUBTITULO24.getId(), idServicio, idConvenio, EstadosConvenios.INGRESADO);
					}else{
						this.conveniosComunaComponenteSub24 = conveniosService.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaConvenioEstadoConvenio(getPrograma().getIdProgramaAno(), idComponente, Subtitulo.SUBTITULO24.getId(), idComuna, idConvenio, EstadosConvenios.INGRESADO);
					}
					if(this.conveniosComunaComponenteSub24 != null && this.conveniosComunaComponenteSub24.size() > 0){
						calcularTotalesSub24();
					}
				}else if(Subtitulo.SUBTITULO29.getId().equals(subtituloVO.getId()) && !getSub29()){
					setSub29(true);
					Integer idEstablecimientoSub29 = ((establecimientoSeleccionado29 == null || establecimientoSeleccionado29.trim().isEmpty() || establecimientoSeleccionado29.trim().equals("0"))?null:Integer.parseInt(establecimientoSeleccionado29));
					System.out.println("Subtitulo.SUBTITULO29.getId()="+Subtitulo.SUBTITULO29.getId());
					System.out.println("idEstablecimientoSub22="+idEstablecimientoSub29);
					if(idEstablecimientoSub29 == null){
						this.conveniosServicioComponenteSub29 = conveniosService.getConveniosServicioComponenteByProgramaAnoComponenteSubtituloServicioConvenioEstadoConvenio(getPrograma().getIdProgramaAno(), idComponente, Subtitulo.SUBTITULO29.getId(), idServicio, idConvenio, EstadosConvenios.INGRESADO);
					}else{
						this.conveniosServicioComponenteSub29 = conveniosService.getConveniosServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoConvenioEstadoConvenio(getPrograma().getIdProgramaAno(), idComponente, Subtitulo.SUBTITULO29.getId(), idEstablecimientoSub29, idConvenio, EstadosConvenios.INGRESADO);
					}
					if(this.conveniosServicioComponenteSub29 != null && this.conveniosServicioComponenteSub29.size() > 0){
						calcularTotalesSub29();
					}
				}
			} 
			if(getSub21() || getSub22() || getSub29()){
				establecimientos = establecimientosService.getEstablecimientosByServicio(idServicio);
			}
		}
		System.out.println("conveniosServicioComponenteSub21.size() --> " +((this.conveniosServicioComponenteSub21==null)?"0":this.conveniosServicioComponenteSub21.size()));
		System.out.println("conveniosServicioComponenteSub22.size() --> " +((this.conveniosServicioComponenteSub22==null)?"0":this.conveniosServicioComponenteSub22.size()));
		System.out.println("conveniosComunaComponenteSub24.size() --> " +((this.conveniosComunaComponenteSub24==null)?"0":this.conveniosComunaComponenteSub24.size()));
		System.out.println("conveniosServicioComponenteSub29.size() --> " +((this.conveniosServicioComponenteSub29==null)?"0":this.conveniosServicioComponenteSub29.size()));
		System.out.println("fin buscar-->");
	}

	private void calcularTotalesSub29() {
		totalMarcoSub29 = 0L;
		totalConveniosSub29 = 0L;
		totalConveniosPendientesSub29 = 0L;
		for(ConvenioServicioComponenteVO convenio : this.conveniosServicioComponenteSub29){
			totalMarcoSub29 += convenio.getMarcoPresupuestario();
			totalConveniosSub29 += convenio.getMonto();
			totalConveniosPendientesSub29 += convenio.getMontoPendiente();
		}
		
	}

	private void calcularTotalesSub24() {
		totalMarcoSub24 = 0L;
		totalConveniosSub24 = 0L;
		totalConveniosPendientesSub24 = 0L;
		for(ConvenioComunaComponenteVO convenio : this.conveniosComunaComponenteSub24){
			totalMarcoSub24 += convenio.getMarcoPresupuestario();
			totalConveniosSub24 += convenio.getMonto();
			totalConveniosPendientesSub24 += convenio.getMontoPendiente();
		}
	}

	private void calcularTotalesSub22() {
		totalMarcoSub22 = 0L;
		totalConveniosSub22 = 0L;
		totalConveniosPendientesSub22 = 0L;
		for(ConvenioServicioComponenteVO convenio : this.conveniosServicioComponenteSub22){
			totalMarcoSub22 += convenio.getMarcoPresupuestario();
			totalConveniosSub22 += convenio.getMonto();
			totalConveniosPendientesSub22 += convenio.getMontoPendiente();
		}
	}

	private void calcularTotalesSub21() {
		totalMarcoSub21 = 0L;
		totalConveniosSub21 = 0L;
		totalConveniosPendientesSub21 = 0L;
		for(ConvenioServicioComponenteVO convenio : this.conveniosServicioComponenteSub21){
			totalMarcoSub21 += convenio.getMarcoPresupuestario();
			totalConveniosSub21 += convenio.getMonto();
			totalConveniosPendientesSub21 += convenio.getMontoPendiente();
		}
		
	}

	public List<EstablecimientoVO> getEstablecimientos() {
		return establecimientos;
	}

	public void setEstablecimientos(List<EstablecimientoVO> establecimientos) {
		this.establecimientos = establecimientos;
	}

	public List<ConvenioComunaComponenteVO> getConveniosComunaComponenteSub24() {
		return conveniosComunaComponenteSub24;
	}

	public void setConveniosComunaComponenteSub24(
			List<ConvenioComunaComponenteVO> conveniosComunaComponenteSub24) {
		this.conveniosComunaComponenteSub24 = conveniosComunaComponenteSub24;
	}

	public List<ConvenioServicioComponenteVO> getConveniosServicioComponenteSub21() {
		return conveniosServicioComponenteSub21;
	}

	public void setConveniosServicioComponenteSub21(
			List<ConvenioServicioComponenteVO> conveniosServicioComponenteSub21) {
		this.conveniosServicioComponenteSub21 = conveniosServicioComponenteSub21;
	}

	public List<ConvenioServicioComponenteVO> getConveniosServicioComponenteSub22() {
		return conveniosServicioComponenteSub22;
	}

	public void setConveniosServicioComponenteSub22(
			List<ConvenioServicioComponenteVO> conveniosServicioComponenteSub22) {
		this.conveniosServicioComponenteSub22 = conveniosServicioComponenteSub22;
	}

	public List<ConvenioServicioComponenteVO> getConveniosServicioComponenteSub29() {
		return conveniosServicioComponenteSub29;
	}

	public void setConveniosServicioComponenteSub29(
			List<ConvenioServicioComponenteVO> conveniosServicioComponenteSub29) {
		this.conveniosServicioComponenteSub29 = conveniosServicioComponenteSub29;
	}

	public String getDocIdDownload() {
		return docIdDownload;
	}

	public void setDocIdDownload(String docIdDownload) {
		this.docIdDownload = docIdDownload;
	}

	public void cargarConveniosServicioComponenteSub21(){
		/*Integer idComponente = ((componenteSeleccionado == null || componenteSeleccionado.equals("0")) ? null : Integer.parseInt(componenteSeleccionado));
		Integer idServicio = ((servicioSeleccionado == null || servicioSeleccionado.equals("0")) ? null : Integer.parseInt(servicioSeleccionado));
		Integer idEstablecimiento = ((establecimientoSeleccionado21 == null || establecimientoSeleccionado21.equals("0")) ? null : Integer.parseInt(establecimientoSeleccionado21));
		if(idEstablecimiento == null){
			this.conveniosServicioComponenteSub21 = conveniosService.getConveniosServicioComponenteByProgramaAnoComponenteSubtituloServicioEstadoConvenio(getPrograma().getIdProgramaAno(), idComponente, Subtitulo.SUBTITULO21.getId(), idServicio, EstadosConvenios.INGRESADO);
		}else{
			this.conveniosServicioComponenteSub21 = conveniosService.getConveniosServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(getPrograma().getIdProgramaAno(), idComponente, Subtitulo.SUBTITULO21.getId(), idEstablecimiento, EstadosConvenios.INGRESADO);
		}*/
		conveniosServicioComponenteSub21 = new ArrayList<ConvenioServicioComponenteVO>();
		setBusquedaRealizada(false);
	}

	public void cargarConveniosServicioComponenteSub22(){
		/*Integer idComponente = ((componenteSeleccionado == null || componenteSeleccionado.equals("0")) ? null : Integer.parseInt(componenteSeleccionado));
		Integer idServicio = ((servicioSeleccionado == null || servicioSeleccionado.equals("0")) ? null : Integer.parseInt(servicioSeleccionado));
		Integer idEstablecimiento = ((establecimientoSeleccionado22 == null || establecimientoSeleccionado22.equals("0")) ? null : Integer.parseInt(establecimientoSeleccionado22));
		
		if(idEstablecimiento == null){
			this.conveniosServicioComponenteSub22 = conveniosService.getConveniosServicioComponenteByProgramaAnoComponenteSubtituloServicioEstadoConvenio(getPrograma().getIdProgramaAno(), idComponente, Subtitulo.SUBTITULO22.getId(), idServicio, EstadosConvenios.INGRESADO);
		}else{
			this.conveniosServicioComponenteSub22 = conveniosService.getConveniosServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(getPrograma().getIdProgramaAno(), idComponente, Subtitulo.SUBTITULO22.getId(), idEstablecimiento, EstadosConvenios.INGRESADO);
		}*/
		this.conveniosServicioComponenteSub22 = new ArrayList<ConvenioServicioComponenteVO>();
		setBusquedaRealizada(false);
	}

	public void cargarConveniosServicioComponenteSub29(){
		/*Integer idComponente = ((componenteSeleccionado == null || componenteSeleccionado.equals("0")) ? null : Integer.parseInt(componenteSeleccionado));
		Integer idServicio = ((servicioSeleccionado == null || servicioSeleccionado.equals("0")) ? null : Integer.parseInt(servicioSeleccionado));
		Integer idEstablecimiento = ((establecimientoSeleccionado29 == null || establecimientoSeleccionado29.equals("0")) ? null : Integer.parseInt(establecimientoSeleccionado29));
		if(idEstablecimiento == null){
			this.conveniosServicioComponenteSub29 = conveniosService.getConveniosServicioComponenteByProgramaAnoComponenteSubtituloServicioEstadoConvenio(getPrograma().getIdProgramaAno(), idComponente, Subtitulo.SUBTITULO29.getId(), idServicio, EstadosConvenios.INGRESADO);
		}else{
			this.conveniosServicioComponenteSub29 = conveniosService.getConveniosServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(getPrograma().getIdProgramaAno(), idComponente, Subtitulo.SUBTITULO29.getId(), idEstablecimiento, EstadosConvenios.INGRESADO);
		}*/
		conveniosServicioComponenteSub29 = new ArrayList<ConvenioServicioComponenteVO>();
		setBusquedaRealizada(false);
	}

	public void cargarConveniosComunaComponenteSub24(){
		/*Integer idComponente = ((componenteSeleccionado == null || componenteSeleccionado.equals("0")) ? null : Integer.parseInt(componenteSeleccionado));
		Integer idServicio = ((servicioSeleccionado == null || servicioSeleccionado.equals("0")) ? null : Integer.parseInt(servicioSeleccionado));
		Integer idComuna = ((comunaSeleccionada24 == null || comunaSeleccionada24.equals("0")) ? null : Integer.parseInt(comunaSeleccionada24));
		if(idComuna == null){
			this.conveniosComunaComponenteSub24 = conveniosService.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloServicioEstadoConvenio(getPrograma().getIdProgramaAno(), idComponente, Subtitulo.SUBTITULO24.getId(), idServicio, EstadosConvenios.INGRESADO);
		}else{
			this.conveniosComunaComponenteSub24 = conveniosService.getConveniosComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(getPrograma().getIdProgramaAno(), idComponente, Subtitulo.SUBTITULO24.getId(), idComuna, EstadosConvenios.INGRESADO);
		}*/
		conveniosComunaComponenteSub24 = new ArrayList<ConvenioComunaComponenteVO>();
		setBusquedaRealizada(false);
	}

	public Long getTotalMarcoSub24() {
		return totalMarcoSub24;
	}

	public void setTotalMarcoSub24(Long totalMarcoSub24) {
		this.totalMarcoSub24 = totalMarcoSub24;
	}

	public Long getTotalConveniosSub24() {
		return totalConveniosSub24;
	}

	public void setTotalConveniosSub24(Long totalConveniosSub24) {
		this.totalConveniosSub24 = totalConveniosSub24;
	}

	public Long getTotalConveniosPendientesSub24() {
		return totalConveniosPendientesSub24;
	}

	public void setTotalConveniosPendientesSub24(Long totalConveniosPendientesSub24) {
		this.totalConveniosPendientesSub24 = totalConveniosPendientesSub24;
	}

	public Long getTotalMarcoSub21() {
		return totalMarcoSub21;
	}

	public void setTotalMarcoSub21(Long totalMarcoSub21) {
		this.totalMarcoSub21 = totalMarcoSub21;
	}

	public Long getTotalConveniosSub21() {
		return totalConveniosSub21;
	}

	public void setTotalConveniosSub21(Long totalConveniosSub21) {
		this.totalConveniosSub21 = totalConveniosSub21;
	}

	public Long getTotalConveniosPendientesSub21() {
		return totalConveniosPendientesSub21;
	}

	public void setTotalConveniosPendientesSub21(Long totalConveniosPendientesSub21) {
		this.totalConveniosPendientesSub21 = totalConveniosPendientesSub21;
	}

	public Long getTotalMarcoSub22() {
		return totalMarcoSub22;
	}

	public void setTotalMarcoSub22(Long totalMarcoSub22) {
		this.totalMarcoSub22 = totalMarcoSub22;
	}

	public Long getTotalConveniosSub22() {
		return totalConveniosSub22;
	}

	public void setTotalConveniosSub22(Long totalConveniosSub22) {
		this.totalConveniosSub22 = totalConveniosSub22;
	}

	public Long getTotalConveniosPendientesSub22() {
		return totalConveniosPendientesSub22;
	}

	public void setTotalConveniosPendientesSub22(Long totalConveniosPendientesSub22) {
		this.totalConveniosPendientesSub22 = totalConveniosPendientesSub22;
	}

	public Long getTotalMarcoSub29() {
		return totalMarcoSub29;
	}

	public void setTotalMarcoSub29(Long totalMarcoSub29) {
		this.totalMarcoSub29 = totalMarcoSub29;
	}

	public Long getTotalConveniosSub29() {
		return totalConveniosSub29;
	}

	public void setTotalConveniosSub29(Long totalConveniosSub29) {
		this.totalConveniosSub29 = totalConveniosSub29;
	}

	public Long getTotalConveniosPendientesSub29() {
		return totalConveniosPendientesSub29;
	}

	public void setTotalConveniosPendientesSub29(Long totalConveniosPendientesSub29) {
		this.totalConveniosPendientesSub29 = totalConveniosPendientesSub29;
	}

	public boolean getConvenioServicioComponenteSub29Seleccionado() {
		return convenioServicioComponenteSub29Seleccionado;
	}

	public void setConvenioServicioComponenteSub29Seleccionado(
			boolean convenioServicioComponenteSub29Seleccionado) {
		this.convenioServicioComponenteSub29Seleccionado = convenioServicioComponenteSub29Seleccionado;
	}

	public boolean isConvenioServicioComponenteSub21Seleccionado() {
		return convenioServicioComponenteSub21Seleccionado;
	}

	public void setConvenioServicioComponenteSub21Seleccionado(
			boolean convenioServicioComponenteSub21Seleccionado) {
		this.convenioServicioComponenteSub21Seleccionado = convenioServicioComponenteSub21Seleccionado;
	}

	public boolean isConvenioServicioComponenteSub22Seleccionado() {
		return convenioServicioComponenteSub22Seleccionado;
	}

	public void setConvenioServicioComponenteSub22Seleccionado(
			boolean convenioServicioComponenteSub22Seleccionado) {
		this.convenioServicioComponenteSub22Seleccionado = convenioServicioComponenteSub22Seleccionado;
	}

	public boolean isConvenioServicioComponenteSub24Seleccionado() {
		return convenioServicioComponenteSub24Seleccionado;
	}

	public void setConvenioServicioComponenteSub24Seleccionado(
			boolean convenioServicioComponenteSub24Seleccionado) {
		this.convenioServicioComponenteSub24Seleccionado = convenioServicioComponenteSub24Seleccionado;
	}

	public void actualizarSub21(Integer row, Integer idConvenioServicioComponente) {
		System.out.println("actualizarSub21(" + row + "," + idConvenioServicioComponente + ")");
		ConvenioServicioComponenteVO conveniosServicioComponente =  conveniosService.aprobarConvenioServicioComponente(idConvenio, idConvenioServicioComponente);
		if(this.conveniosServicioComponenteSub21 != null && (row < this.conveniosServicioComponenteSub21.size()) && conveniosServicioComponente != null){
			this.conveniosServicioComponenteSub21.remove(row);
		}
	}

	public void actualizarSub22(Integer row, Integer idConvenioServicioComponente) {
		System.out.println("actualizarSub22(" + row + "," + idConvenioServicioComponente + ")");
		ConvenioServicioComponenteVO conveniosServicioComponente =  conveniosService.aprobarConvenioServicioComponente(idConvenio, idConvenioServicioComponente);
		if(this.conveniosServicioComponenteSub22 != null && (row < this.conveniosServicioComponenteSub22.size()) && conveniosServicioComponente != null){
			this.conveniosServicioComponenteSub22.remove(row);
		}
	}

	public void actualizarSub29(Integer row, Integer idConvenioServicioComponente) {
		System.out.println("actualizarSub29(" + row + "," + idConvenioServicioComponente + ")");
		ConvenioServicioComponenteVO conveniosServicioComponente =  conveniosService.aprobarConvenioServicioComponente(idConvenio, idConvenioServicioComponente);
		if(this.conveniosServicioComponenteSub29 != null && (row < this.conveniosServicioComponenteSub29.size()) && conveniosServicioComponente != null){
			this.conveniosServicioComponenteSub29.remove(conveniosServicioComponente);
		}
	}

	public void actualizarSub24(Integer row, Integer idConvenioServicioComponente) {
		System.out.println("actualizarSub24(" + row + "," + idConvenioServicioComponente + ")");
		ConvenioComunaComponenteVO conveniosComunaComponente = conveniosService.aprobarConvenioComunaComponente(idConvenio, idConvenioServicioComponente);
		if(this.conveniosComunaComponenteSub24 != null && (row < this.conveniosComunaComponenteSub24.size()) && conveniosComunaComponente != null){
			System.out.println("removiendo el elemento de la posicion="+row);
			this.conveniosComunaComponenteSub24.remove(conveniosComunaComponente);
		}
		System.out.println("conveniosComunaComponenteSub24.size()=" + ((conveniosComunaComponenteSub24==null) ? "0" : conveniosComunaComponenteSub24.size()) );
	}

	public boolean isConveniosPendientes() {
		conveniosPendientes = conveniosService.getConveniosPendientes(getPrograma().getIdProgramaAno());
		System.out.println("Hay Convenios Pendientes isConveniosPendientes conveniosPendientes="+conveniosPendientes);
		return conveniosPendientes;
	}

	public void setConveniosPendientes(boolean conveniosPendientes) {
		this.conveniosPendientes = conveniosPendientes;
	}

	public boolean isBusquedaRealizada() {
		return busquedaRealizada;
	}

	public void setBusquedaRealizada(boolean busquedaRealizada) {
		this.busquedaRealizada = busquedaRealizada;
	}

	public boolean isMostrarBotonReparos() {
		mostrarBotonReparos = false;
		if(isBusquedaRealizada()){
			if((conveniosServicioComponenteSub21 != null && conveniosServicioComponenteSub21.size() > 0) || (conveniosServicioComponenteSub22 != null && conveniosServicioComponenteSub22.size() > 0) 
					&& (conveniosServicioComponenteSub29 != null && conveniosServicioComponenteSub29.size() > 0) || (conveniosComunaComponenteSub24 != null && conveniosComunaComponenteSub24.size() > 0)){
				mostrarBotonReparos = true;
			}
		}
		return mostrarBotonReparos;
	}

	public void setMostrarBotonReparos(boolean mostrarBotonReparos) {
		this.mostrarBotonReparos = mostrarBotonReparos;
	}

}
