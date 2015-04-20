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
import javax.inject.Inject;
import javax.inject.Named;

import minsal.divap.service.ModificacionDistribucionInicialPercapitaService;
import minsal.divap.service.UtilitariosService;
import minsal.divap.vo.AsignacionDistribucionPerCapitaVO;
import minsal.divap.vo.ComunaVO;
import minsal.divap.vo.ServiciosVO;

import org.apache.log4j.Logger;
import org.primefaces.model.UploadedFile;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named("procesoModificacionAsignacionPerCapitaValidarMontosController")
@ViewScoped
public class ProcesoModificacionAsignacionPerCapitaValidarMontosController extends AbstractTaskMBean
implements Serializable {
	private static final long serialVersionUID = 8979055329731411696L;
	@Inject
	private transient Logger log;
	private boolean checkRegion;
	private boolean checkComuna;
	private boolean checkServicio;
	private boolean checkRefAsigZona;
	private boolean checkTramoPobreza;
	private boolean checkPerCapitaBasal;
	private boolean checkPobreza;
	private boolean checkRuralidad;
	private boolean checkValorRefAsigZona;
	private boolean checkValorperCapita;
	private boolean checkPoblacionAno;
	private boolean checkPoblacionMayor65Anos;
	private boolean checkPerCapitaMes;
	private boolean checkPerCapitaAno;
	private boolean checkAsigDesempenoDificil;
	private boolean emptyCheckColumn;
	@EJB
	private UtilitariosService utilitariosService;
	@EJB
	private ModificacionDistribucionInicialPercapitaService modificacionDistribucionInicialPercapitaService;
	private List<AsignacionDistribucionPerCapitaVO> antecendentesComunaCalculado;
	private String docIdDownload;
	private Integer docId;
	private Integer idDistribucionInicialPercapita;
	private UploadedFile calculoPerCapitaFile;
	private UploadedFile valorBasicoDesempenoFile;
	private String servicioSeleccionado;
	private List<ServiciosVO> servicios;
	private String comunaSeleccionada;
	private List<ComunaVO> comunas;

	private  boolean rechazarRevalorizar_;
	private  boolean rechazarSubirArchivos_;
	private  boolean aprobar_;
	private Integer anoProceso;


	public UploadedFile getCalculoPerCapitaFile() {
		return calculoPerCapitaFile;
	}

	public void setCalculoPerCapitaFile(UploadedFile calculoPerCapitaFile) {
		this.calculoPerCapitaFile = calculoPerCapitaFile;
	}

	public UploadedFile getValorBasicoDesempenoFile() {
		return valorBasicoDesempenoFile;
	}

	public void setValorBasicoDesempenoFile(
			UploadedFile valorBasicoDesempenoFile) {
		this.valorBasicoDesempenoFile = valorBasicoDesempenoFile;
	}

	public void uploadArchivosValorizacion() {
		if (calculoPerCapitaFile != null && valorBasicoDesempenoFile != null) {
			FacesMessage msg = new FacesMessage(
					"Los archivos fueron cargados correctamente.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
	
	public void buscar() {
		System.out.println("buscar--> servicioSeleccionado="+servicioSeleccionado+" comunaSeleccionada="+comunaSeleccionada);
		if((servicioSeleccionado == null || servicioSeleccionado.trim().isEmpty()) && (comunaSeleccionada == null || comunaSeleccionada.trim().isEmpty()) ){
			this.antecendentesComunaCalculado = modificacionDistribucionInicialPercapitaService.findAntecedentesComunaCalculadosByDistribucionInicialPercapita(idDistribucionInicialPercapita, this.anoProceso);
		}else{
			Integer idComuna = ((comunaSeleccionada == null || comunaSeleccionada.trim().isEmpty()) ? null : Integer.parseInt(comunaSeleccionada));
			Integer idServicio = ((servicioSeleccionado == null || servicioSeleccionado.trim().isEmpty()) ? null : Integer.parseInt(servicioSeleccionado));
			this.antecendentesComunaCalculado = modificacionDistribucionInicialPercapitaService.findAntecedentesComunaCalculadosByDistribucionInicialPercapita(idServicio,
					idComuna, idDistribucionInicialPercapita, this.anoProceso);
		}
		System.out.println("fin buscar-->");
	}

	public void limpiar() {
		System.out.println("Limpiar-->");
		servicioSeleccionado = "";
		comunaSeleccionada = "";
		antecendentesComunaCalculado = new ArrayList<AsignacionDistribucionPerCapitaVO>();
		comunas = new ArrayList<ComunaVO>();
		limpiarCheck();
		System.out.println("fin limpiar");
	}
	
	private void limpiarCheck(){
		checkRegion = false;
		checkComuna = false;
		checkServicio = false;
		checkRefAsigZona = false;
		checkTramoPobreza = false;
		checkPerCapitaBasal = false;
		checkPobreza = false;
		checkRuralidad = false;
		checkValorRefAsigZona = false;
		checkValorperCapita = false;
		checkPoblacionAno = false;
		checkPoblacionMayor65Anos = false;
		checkPerCapitaMes = false;
		checkPerCapitaAno = false;
		checkAsigDesempenoDificil = false;
	}
	
	private void initCheck(){
		checkRegion = true;
		checkComuna = true;
		checkServicio = true;
		checkRefAsigZona = true;
		checkTramoPobreza = true;
		checkPerCapitaBasal = true;
		checkPobreza = true;
		checkRuralidad = true;
		checkValorRefAsigZona = true;
		checkValorperCapita = true;
		checkPoblacionAno = true;
		checkPoblacionMayor65Anos = true;
		checkPerCapitaMes = true;
		checkPerCapitaAno = true;
		checkAsigDesempenoDificil = true;
	}

	String actividadSeguimientoTitle = "";

	public String getActividadSeguimientoTitle() {
		return actividadSeguimientoTitle;
	}

	public void setActividadSeguimientoTitle(String actividadSeguimientoTitle) {
		this.actividadSeguimientoTitle = actividadSeguimientoTitle;
	}

	@Override
	public String toString() {
		return "ProcesoAsignacionPerCapitaController [validarMontosDistribucion="
				+ idDistribucionInicialPercapita + "]";
	}

	@PostConstruct
	public void init() {
		log.info("ProcesoAsignacionPerCapitaValidarMontosController Alcanzado.");
		if(sessionExpired()){
			return;
		}
		this.docId = (Integer) getTaskDataVO().getData().get("_docId");
		System.out.println("this.docId->"+this.docId);
		this.idDistribucionInicialPercapita = (Integer) getTaskDataVO().getData().get("_idDistribucionInicialPercapita");
		System.out.println("this.idDistribucionInicialPercapita->"+this.idDistribucionInicialPercapita);
		this.anoProceso = (Integer) getTaskDataVO().getData().get("_ano");
		System.out.println("this.idDistribucionInicialPercapita->"+this.idDistribucionInicialPercapita);
		this.antecendentesComunaCalculado = modificacionDistribucionInicialPercapitaService.findAntecedentesComunaCalculadosByDistribucionInicialPercapita(idDistribucionInicialPercapita, this.anoProceso);
		if(this.antecendentesComunaCalculado  != null && this.antecendentesComunaCalculado .size() > 0){
			initCheck();
		}
	}


	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		System.out.println("createResultData usuario-->"+getSessionBean().getUsername());
		parameters.put("usuario", getSessionBean().getUsername());
		parameters.put("rechazarRevalorizar_", new Boolean(isRechazarRevalorizar_()));
		parameters.put("rechazarSubirArchivos_", new Boolean(isRechazarSubirArchivos_()));
		parameters.put("aprobar_", new Boolean(isAprobar_()));
		return parameters;
	}

	public String downloadPlanilla() {
		Integer docDownload = Integer.valueOf(Integer.parseInt(getDocIdDownload()));
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
		return null;
	}


	public boolean isRechazarRevalorizar_() {
		System.out.println("rechazarRevalorizar_-->"+rechazarRevalorizar_);
		return rechazarRevalorizar_;
	}

	public void setRechazarRevalorizar_(boolean rechazarRevalorizar_) {
		this.rechazarRevalorizar_ = rechazarRevalorizar_;
	}

	public boolean isRechazarSubirArchivos_() {
		System.out.println("rechazarSubirArchivos_-->"+rechazarSubirArchivos_);
		return rechazarSubirArchivos_;
	}

	public void setRechazarSubirArchivos_(boolean rechazarSubirArchivos_) {
		this.rechazarSubirArchivos_ = rechazarSubirArchivos_;
	}

	public boolean isAprobar_() {
		System.out.println("aprobar_-->"+aprobar_);
		return aprobar_;
	}

	public void setAprobar_(boolean aprobar_) {
		this.aprobar_ = aprobar_;
	}

	@Override
	public String iniciarProceso() {
		return null;
	}

	public String getDocIdDownload() {
		return docIdDownload;
	}

	public void setDocIdDownload(String docIdDownload) {
		this.docIdDownload = docIdDownload;
	}

	public Integer getDocId() {
		return docId;
	}

	public void setDocId(Integer docId) {
		this.docId = docId;
	}

	public Integer getIdDistribucionInicialPercapita() {
		return idDistribucionInicialPercapita;
	}

	public void setIdDistribucionInicialPercapita(
			Integer idDistribucionInicialPercapita) {
		this.idDistribucionInicialPercapita = idDistribucionInicialPercapita;
	}

	
	public void cargaComunas(){
		if(servicioSeleccionado != null && !servicioSeleccionado.trim().isEmpty()){
			comunas = utilitariosService.getComunasByServicio(Integer.parseInt(servicioSeleccionado));
		}else{
			comunas = new ArrayList<ComunaVO>();
		}
	}

	public List<ServiciosVO> getServicios() {
		if(this.servicios == null){
			this.servicios = utilitariosService.getAllServicios();
		}
		return servicios;
	}

	public void setServicios(List<ServiciosVO> servicios) {
		this.servicios = servicios;
	}

	public String getServicioSeleccionado() {
		return servicioSeleccionado;
	}

	public void setServicioSeleccionado(String servicioSeleccionado) {
		this.servicioSeleccionado = servicioSeleccionado;
	}

	public List<ComunaVO> getComunas() {
		return comunas;
	}

	public void setComunas(List<ComunaVO> comunas) {
		this.comunas = comunas;
	}

	public String getComunaSeleccionada() {
		return comunaSeleccionada;
	}

	public void setComunaSeleccionada(String comunaSeleccionada) {
		this.comunaSeleccionada = comunaSeleccionada;
	}

	public FacesContext getFacesContext() {
		return facesContext;
	}

	public void setFacesContext(FacesContext facesContext) {
		this.facesContext = facesContext;
	}

	public boolean isCheckRegion() {
		return checkRegion;
	}

	public void setCheckRegion(boolean checkRegion) {
		this.checkRegion = checkRegion;
	}

	public boolean isCheckComuna() {
		return checkComuna;
	}

	public void setCheckComuna(boolean checkComuna) {
		this.checkComuna = checkComuna;
	}

	public boolean isCheckServicio() {
		return checkServicio;
	}

	public void setCheckServicio(boolean checkServicio) {
		this.checkServicio = checkServicio;
	}

	public boolean isCheckRefAsigZona() {
		return checkRefAsigZona;
	}

	public void setCheckRefAsigZona(boolean checkRefAsigZona) {
		this.checkRefAsigZona = checkRefAsigZona;
	}

	public boolean isCheckTramoPobreza() {
		return checkTramoPobreza;
	}

	public void setCheckTramoPobreza(boolean checkTramoPobreza) {
		this.checkTramoPobreza = checkTramoPobreza;
	}

	public boolean isCheckPerCapitaBasal() {
		return checkPerCapitaBasal;
	}

	public void setCheckPerCapitaBasal(boolean checkPerCapitaBasal) {
		this.checkPerCapitaBasal = checkPerCapitaBasal;
	}

	public boolean isCheckPobreza() {
		return checkPobreza;
	}

	public void setCheckPobreza(boolean checkPobreza) {
		this.checkPobreza = checkPobreza;
	}

	public boolean isCheckRuralidad() {
		return checkRuralidad;
	}

	public void setCheckRuralidad(boolean checkRuralidad) {
		this.checkRuralidad = checkRuralidad;
	}

	public boolean isCheckValorRefAsigZona() {
		return checkValorRefAsigZona;
	}

	public void setCheckValorRefAsigZona(boolean checkValorRefAsigZona) {
		this.checkValorRefAsigZona = checkValorRefAsigZona;
	}

	public boolean isCheckValorperCapita() {
		return checkValorperCapita;
	}

	public void setCheckValorperCapita(boolean checkValorperCapita) {
		this.checkValorperCapita = checkValorperCapita;
	}

	public boolean isCheckPoblacionAno() {
		return checkPoblacionAno;
	}

	public void setCheckPoblacionAno(boolean checkPoblacionAno) {
		this.checkPoblacionAno = checkPoblacionAno;
	}

	public boolean isCheckPoblacionMayor65Anos() {
		return checkPoblacionMayor65Anos;
	}

	public void setCheckPoblacionMayor65Anos(boolean checkPoblacionMayor65Anos) {
		this.checkPoblacionMayor65Anos = checkPoblacionMayor65Anos;
	}

	public boolean isCheckPerCapitaMes() {
		return checkPerCapitaMes;
	}

	public void setCheckPerCapitaMes(boolean checkPerCapitaMes) {
		this.checkPerCapitaMes = checkPerCapitaMes;
	}

	public boolean isCheckPerCapitaAno() {
		return checkPerCapitaAno;
	}

	public void setCheckPerCapitaAno(boolean checkPerCapitaAno) {
		this.checkPerCapitaAno = checkPerCapitaAno;
	}

	public boolean isCheckAsigDesempenoDificil() {
		return checkAsigDesempenoDificil;
	}

	public void setCheckAsigDesempenoDificil(boolean checkAsigDesempenoDificil) {
		this.checkAsigDesempenoDificil = checkAsigDesempenoDificil;
	}

	public List<AsignacionDistribucionPerCapitaVO> getAntecendentesComunaCalculado() {
		return antecendentesComunaCalculado;
	}

	public void setAntecendentesComunaCalculado(
			List<AsignacionDistribucionPerCapitaVO> antecendentesComunaCalculado) {
		this.antecendentesComunaCalculado = antecendentesComunaCalculado;
	}

	public boolean isEmptyCheckColumn() {
		emptyCheckColumn = (checkRegion || 	checkComuna || checkServicio || checkRefAsigZona || checkTramoPobreza || checkPerCapitaBasal || checkPobreza || checkRuralidad || checkValorRefAsigZona ||  checkValorperCapita
		|| 	checkPoblacionAno || checkPoblacionMayor65Anos || checkPerCapitaMes || checkPerCapitaAno || checkAsigDesempenoDificil);
		return emptyCheckColumn;
	}

	public void setEmptyCheckColumn(boolean emptyCheckColumn) {
		
		this.emptyCheckColumn = emptyCheckColumn;
	}
	
}

