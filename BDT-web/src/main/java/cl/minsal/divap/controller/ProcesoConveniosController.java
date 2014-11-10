package cl.minsal.divap.controller;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.service.ComponenteService;
import minsal.divap.service.ComunaService;
import minsal.divap.service.ConveniosService;
import minsal.divap.service.EstablecimientosService;
import minsal.divap.service.ProgramasService;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ComunaVO;
import minsal.divap.vo.ConvenioDocumentoVO;
import minsal.divap.vo.ConveniosVO;
import minsal.divap.vo.EstablecimientoVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ResolucionConveniosComunaVO;
import minsal.divap.vo.ResolucionConveniosServicioVO;

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
	private List<ConvenioDocumentoVO> documentos;
	private List<ResolucionConveniosComunaVO> resolucionConveniosMunicipal;
	private List<ResolucionConveniosServicioVO> resolucionConveniosServicio;
	private List<ConveniosVO> resolucionesServicios;
	private List<ConveniosVO> resolucionesMunicipal;
	private ConveniosVO convenio;
	private ProgramaVO programa;
	private String convenioSeleccionado;
	private String programaSeleccionado;
	private String comunaSeleccionada;
	private String componenteSeleccionado;
	private String establecimientoSeleccionado;
	private String documentoSeleccionado;
	private UploadedFile plantillaFile;


	@EJB
	private ProgramasService programasService;
	@EJB
	private ComponenteService componenteService;
	@EJB
	private ComunaService comunaService;
	@EJB
	private EstablecimientosService establecimientosService;
	//@EJB
	//private AlfrescoService alfrescoService;
	@EJB
	private ConveniosService conveniosService;

/*	public void downloadDocAlfresco(String id ){
		String mensaje="";	
		try{
			mensaje = "Archivo Descargado";
			FacesMessage msg = new FacesMessage(mensaje);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			ReferenciaDocumento ref = new ReferenciaDocumento();
			ref = conveniosService.getReferenciaDocumentoByIdConvenio(Integer.parseInt(id));
			System.out.println("felipe :"+ref.getId());
			Integer docDownload = Integer.valueOf(ref.getId());
			setDocumento(documentService.getDocument(docDownload));
			super.downloadDocument();
		}catch(Exception e) {
			mensaje ="EL convenio no  conteine archivo relacionado";
			FacesMessage msg = new FacesMessage(mensaje);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			throw new RuntimeException(e);
		}

	}*/

	public void uploadPopup(){
		String mensaje="";	
		if(documentoSeleccionado.equals("") && plantillaFile != null){
			String filename = plantillaFile.getFileName();
			byte[] contentAttachedFile = plantillaFile.getContents();
			Integer docAttachedFile = persistFile(filename,	contentAttachedFile);

			try{
				mensaje="El Archivo  fue cargado con �xito.";	

				Integer idconvenioNew=	conveniosService.convenioSave(Integer.parseInt(getProgramaSeleccionado()),
						Integer.parseInt(getEstablecimientoSeleccionado()),
						Integer.parseInt(getComunaSeleccionada()),
						Integer.parseInt(convenio.getIdSubtitulo().toString()),
						Integer.parseInt(convenio.getConvenioMonto().toString()),
						Integer.parseInt(getComponenteSeleccionado()),
						Integer.parseInt(convenio.getNumeroResolucion().toString())
						);
				conveniosService.moveToAlfresco(idconvenioNew, docAttachedFile, TipoDocumentosProcesos.CONVENIOS);
				FacesMessage msg = new FacesMessage(mensaje);
				FacesContext.getCurrentInstance().addMessage(null, msg);

			} catch (Exception e) {
				mensaje ="Los Archivos no fueron cargados.";
				FacesMessage msg = new FacesMessage(mensaje);
				FacesContext.getCurrentInstance().addMessage(null, msg);

				throw new RuntimeException(e);

			}
		}else if(!documentoSeleccionado.equals("") && plantillaFile == null){
			try{
				mensaje = "Los datos fueron actualizados correctamente.";

				Integer idconvenioNew=	conveniosService.convenioSave(Integer.parseInt(getProgramaSeleccionado()),
						Integer.parseInt(getEstablecimientoSeleccionado()),
						Integer.parseInt(getComunaSeleccionada()),
						Integer.parseInt(convenio.getIdSubtitulo().toString()),
						Integer.parseInt(convenio.getConvenioMonto().toString()),
						Integer.parseInt(getComponenteSeleccionado()),
						Integer.parseInt(convenio.getNumeroResolucion().toString())
						);
				conveniosService.moveToBd(idconvenioNew,Integer.parseInt(getDocumentoSeleccionado()), TipoDocumentosProcesos.CONVENIOS );
				//actualiza convenio en la bd
				//aPSConveniosService.setConvenioById(convenio.getIdConverio(),convenio.getNumeroResolucion(),convenio.getConvenioMonto());
				FacesMessage msg = new FacesMessage(mensaje);
				FacesContext.getCurrentInstance().addMessage(null, msg);

			} catch (Exception e) {
				mensaje ="Los Archivos no fueron actualizados.";
				FacesMessage msg = new FacesMessage(mensaje);
				FacesContext.getCurrentInstance().addMessage(null, msg);
				throw new RuntimeException(e);
			}
		}
	}
	
	@PostConstruct 
	public void init() {
		System.out.println("ProcesoConveniosController Alcanzado.");
		if(sessionExpired()){
			return;
		}
		setComunaSeleccionada("0");
		setEstablecimientoSeleccionado("0");
		setPrograma(null);
		if(getServicio() != null && getServicio().getId_servicio() != null){
			setComunas(comunaService.getComunasByServicio(getServicio().getId_servicio()));
			setEstablecimientos(establecimientosService.getEstablecimientosByServicio(getServicio().getId_servicio()));
		}
	}

	public void cargaConvenio(String pramConvenio){
		convenio = conveniosService.getConvenioById(Integer.parseInt(pramConvenio));
	}

	/*public List<ConveniosVO> getListResolucionesMuni() {
		Integer muni1 = 1;// representa a munisipal
		Integer muni3 = 3;// representa a mixtos
		if(getComunaSeleccionada() != null && getProgramaSeleccionado() != null && getComponenteSeleccionado() != null  && getEstablecimientoSeleccionado() != null){
			convenios = conveniosService.getConveniosForSubGridMuniServi(Integer.parseInt(programaSeleccionado),Integer.parseInt(componenteSeleccionado),Integer.parseInt(comunaSeleccionada),Integer.parseInt(establecimientoSeleccionado),muni1,muni3);
		}else{
			convenios = new ArrayList<ConveniosVO>();
		}
		return convenios;
	}*/

	/*public List<ConveniosVO> getListResolucionesServi() {
		Integer muni2 = 2;// representa a Servicios
		Integer muni3 = 3;// representa a mixtos
		if(getComunaSeleccionada() != null && getProgramaSeleccionado() != null && getComponenteSeleccionado() != null  && getEstablecimientoSeleccionado() != null){
			convenios = conveniosService.getConveniosForSubGridMuniServi(Integer.parseInt(programaSeleccionado),Integer.parseInt(componenteSeleccionado),Integer.parseInt(comunaSeleccionada),Integer.parseInt(establecimientoSeleccionado),muni2,muni3);
		}else{
			convenios = new ArrayList<ConveniosVO>();
		}
		return convenios;
	}*/

	/*public List<ConveniosVO> getConveniosMuni() {
		Integer muni1 = 1;//1 representa a munisipal
		Integer muni3 = 3;// representa a mixtos
		if(getComunaSeleccionada() != null && getProgramaSeleccionado() != null && getComponenteSeleccionado() != null  && getEstablecimientoSeleccionado() != null){
			convenios = conveniosService.getConveniosForGridMuniServi(Integer.parseInt(programaSeleccionado),Integer.parseInt(componenteSeleccionado),Integer.parseInt(comunaSeleccionada),Integer.parseInt(establecimientoSeleccionado),muni1,muni3);
		}else{
			convenios = new ArrayList<ConveniosVO>();
		}
		return convenios;
	}*/
	
	/*public List<ConveniosVO> getConveniosServi() {
		Integer muni2 = 2;//1 representa a Servicios
		Integer muni3 = 3;// representa a mixtos
		if(getComunaSeleccionada() != null && getProgramaSeleccionado() != null && getComponenteSeleccionado() != null  && getEstablecimientoSeleccionado() != null){
			convenios = conveniosService.getConveniosForGridMuniServi(Integer.parseInt(programaSeleccionado),Integer.parseInt(componenteSeleccionado),Integer.parseInt(comunaSeleccionada),Integer.parseInt(establecimientoSeleccionado),muni2,muni3);
		}else{
			convenios = new ArrayList<ConveniosVO>();
		}
		return convenios;
	}*/
	
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

	public List<ConvenioDocumentoVO> getDocumentos() {
		if(convenio != null && convenio.getIdConverio()!=null){
			documentos = conveniosService.getDocumentById(convenio.getIdConverio());
		}else{
			documentos = new ArrayList<ConvenioDocumentoVO>();
		}
		return documentos;
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

	public List<ResolucionConveniosComunaVO> getResolucionConveniosMunicipal() {
		return resolucionConveniosMunicipal;
	}

	public void setResolucionConveniosMunicipal(
			List<ResolucionConveniosComunaVO> resolucionConveniosMunicipal) {
		this.resolucionConveniosMunicipal = resolucionConveniosMunicipal;
	}

	public List<ResolucionConveniosServicioVO> getResolucionConveniosServicio() {
		return resolucionConveniosServicio;
	}

	public void setResolucionConveniosServicio(
			List<ResolucionConveniosServicioVO> resolucionConveniosServicio) {
		this.resolucionConveniosServicio = resolucionConveniosServicio;
	}

	public List<ProgramaVO> getProgramas() {
		if(programas == null){
			programas = programasService.getProgramasByUser(getLoggedUsername());
		}
		return programas;
	}

	public void cargarComponentesPorPrograma(){
		System.out.println("ProcesoConveniosController::cargarComponentesPorPrograma programaSeleccionado->" + getProgramaSeleccionado());
		if(getProgramaSeleccionado() != null && !"0".equals(getProgramaSeleccionado())){
			programa = programasService.getProgramaAno(Integer.parseInt(getProgramaSeleccionado()));
			System.out.println("programa.getDependenciaMunicipal()->" + programa.getDependenciaMunicipal() + " programa.getDependenciaServicio()->" + programa.getDependenciaServicio());
			componentes = componenteService.getComponenteByPrograma(programa.getId());
		}else{
			componentes = new ArrayList<ComponentesVO>();
			programa = null;
		}
	}

	public void cargarDatos(){
		System.out.println("cargarDatos");
		if(getPrograma() != null){
			if(getPrograma().getDependenciaMunicipal() != null && getPrograma().getDependenciaMunicipal()){
				conveniosService.crearConveniosMunicipal(getServicio().getId_servicio(), getPrograma().getIdProgramaAno());
				Integer componenteSeleccionado = ((getComponenteSeleccionado() == null || getComponenteSeleccionado().equals("0")) ? null : Integer.parseInt(getComponenteSeleccionado()));
				Integer comunaSeleccionada = ((getComunaSeleccionada() == null || getComunaSeleccionada().equals("0")) ? null : Integer.parseInt(getComunaSeleccionada()));
				resolucionConveniosMunicipal = conveniosService.getResolucionConveniosMunicipal(getServicio().getId_servicio(), getPrograma().getIdProgramaAno(), componenteSeleccionado, comunaSeleccionada);
			}
			if(getPrograma().getDependenciaServicio() != null && getPrograma().getDependenciaServicio()){
				conveniosService.crearConveniosServicios(getServicio().getId_servicio(), getPrograma().getIdProgramaAno());
				Integer componenteSeleccionado = ((getComponenteSeleccionado() == null || getComponenteSeleccionado().equals("0")) ? null : Integer.parseInt(getComponenteSeleccionado()));
				Integer establecimientoSeleccionado = ((getEstablecimientoSeleccionado() == null || getEstablecimientoSeleccionado().equals("0")) ? null : Integer.parseInt(getEstablecimientoSeleccionado()));
				resolucionConveniosServicio = conveniosService.getResolucionConveniosServicio(getServicio().getId_servicio(), getPrograma().getIdProgramaAno(), componenteSeleccionado, establecimientoSeleccionado);
			}
		}
	}


	public String getComponenteSeleccionado() {
		return componenteSeleccionado;
	}

	public void setComponenteSeleccionado(String componenteSeleccionado) {
		this.componenteSeleccionado = componenteSeleccionado;
	}

	public String getEstablecimientoSeleccionado() {
		return establecimientoSeleccionado;
	}

	public void setEstablecimientoSeleccionado(String establecimientoSeleccionado) {
		this.establecimientoSeleccionado = establecimientoSeleccionado;
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

	public ConveniosVO getConvenio() {
		return convenio;
	}

	public void setConvenio(ConveniosVO convenio) {
		this.convenio = convenio;
	}

	public UploadedFile getPlantillaFile() {
		return plantillaFile;
	}

	public void setPlantillaFile(UploadedFile plantillaFile) {
		this.plantillaFile = plantillaFile;
	}

	public void setDocumentos(List<ConvenioDocumentoVO> documentos) {
		this.documentos = documentos;
	}

	public String getDocumentoSeleccionado() {
		return documentoSeleccionado;
	}

	public void setDocumentoSeleccionado(String documentoSeleccionado) {
		this.documentoSeleccionado = documentoSeleccionado;
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
	

}
