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

import minsal.divap.enums.TiposCumplimientos;
import minsal.divap.service.RebajaService;
import minsal.divap.service.UtilitariosService;
import minsal.divap.vo.ComunaVO;
import minsal.divap.vo.DocumentSummaryVO;
import minsal.divap.vo.PlanillaRebajaCalculadaVO;
import minsal.divap.vo.RegionVO;
import minsal.divap.vo.ServiciosVO;
import minsal.divap.vo.TipoCumplimientoVO;

import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;
import cl.redhat.bandejaTareas.util.JSONHelper;

@Named("procesoRebajaValidarMontosController")
@ViewScoped
public class ProcesoRebajaValidarMontosController extends AbstractTaskMBean
implements Serializable {

	private static final long serialVersionUID = -9223198612121852459L;

	@EJB
	private UtilitariosService utilitariosService;
	@EJB
	private RebajaService rebajaService;

	//Variables página
	private List<RegionVO> listaRegiones;
	private String regionSeleccionada;	
	private List<ServiciosVO> listaServicios;
	private String servicioSeleccionado;
	private List<ComunaVO> listaComunas;
	private List<String> comunasSeleccionadas;
	private List<PlanillaRebajaCalculadaVO> rebajaComunas;
	private PlanillaRebajaCalculadaVO selectedPlanilla;  
	private List<Integer> allDocuments;
	private List<DocumentSummaryVO> resumenDocumentos;
	private Integer fisrtTime = 1;
	private Integer totalIncumplimiento = 0;
	
	private TipoCumplimientoVO cumplimientoItem1;
	private TipoCumplimientoVO cumplimientoItem2;
	private TipoCumplimientoVO cumplimientoItem3;

	private String posicionElemento;
	private String columnaElemento;
	private String valorElemento;


	private Integer docRebaja;
	private String docIdDownload;
	private String rebajaSeleccionada;
	private String mesActual;
	//Variables de entrada tarea
	private Integer idProcesoRebaja;

	//Variables de salida tarea
	private boolean aprobar_;
	private boolean rechazarRevalorizar_;
	private boolean rechazarSubirArchivo_;

	@PostConstruct
	public void init() {
		System.out.println("ProcesosRebajaValidarMontosController tocado.");
		if(sessionExpired()){
			return;
		}
		if(getTaskDataVO().getData().get("_idProcesoRebaja") != null){
			this.idProcesoRebaja = (Integer)getTaskDataVO().getData().get("_idProcesoRebaja");
		}
		buscaDocumentos();
		cargarListaRegiones();
		setMesActual(rebajaService.getMesCurso(false));
	}

	private void buscaDocumentos() {
		this.docRebaja = (Integer) getTaskDataVO().getData().get("_idDoc");
		System.out.println("this.docRebaja-->"+this.docRebaja);
		String todosDocumentos = (String) getTaskDataVO().getData().get("_allDocumentsId");
		if(todosDocumentos != null){
			allDocuments = JSONHelper.fromJSON(todosDocumentos, List.class);
			allDocuments.add(docRebaja);
		}else{
			allDocuments = new ArrayList<Integer>();
			allDocuments.add(docRebaja);
		}
		resumenDocumentos = rebajaService.getReferenciaDocumentosById(allDocuments);
	}

	public void cargarListaRegiones(){
		listaRegiones = utilitariosService.getAllRegion();
	}

	public void cargaServicios(){
		if(regionSeleccionada!=null && !regionSeleccionada.equals("")){
			listaServicios=utilitariosService.getServiciosByRegion(Integer.parseInt(regionSeleccionada));
		}else{
			listaServicios = new ArrayList<ServiciosVO>();
		}
	}

	public void cargaComunas(){
		if(servicioSeleccionado!=null && !servicioSeleccionado.equals("")){
			listaComunas = utilitariosService.getComunasByServicio(Integer.parseInt(servicioSeleccionado));
		}else{
			listaComunas = new ArrayList<ComunaVO>();
		}
		fisrtTime = 1;
		totalIncumplimiento = 0;
	}

	public void buscarRebaja(){
		System.out.println("ProcesoRebajaValidarMontosController:buscarRebaja");
		List<Integer> idComunas = new ArrayList<Integer>();
		for(String comunas : comunasSeleccionadas){
			Integer idComuna = Integer.parseInt(comunas);
			idComunas.add(idComuna);
		}
		rebajaComunas = rebajaService.getRebajasByComuna(this.idProcesoRebaja, idComunas);
		totalIncumplimiento = rebajaComunas.size();
		fisrtTime++;
		System.out.println("ProcesoRebajaValidarMontosController:buscarRebaja fin totalIncumplimiento="+totalIncumplimiento);
	}

	public void limpiar() {
		System.out.println("Limpiar-->");
		this.regionSeleccionada = null;
		this.servicioSeleccionado = null;
		this.comunasSeleccionadas = new ArrayList<String>();
		this.rebajaComunas = new ArrayList<PlanillaRebajaCalculadaVO>();
		this.listaServicios = new ArrayList<ServiciosVO>();
		this.listaComunas = new ArrayList<ComunaVO>();
		fisrtTime = 1;
		totalIncumplimiento = 0;
		System.out.println("fin limpiar");
	}

	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		System.out.println("createResultData usuario-->"+getSessionBean().getUsername());
		parameters.put("usuario", getSessionBean().getUsername());
		parameters.put("aprobar_", this.aprobar_);
		parameters.put("rechazarRevalorizar_", this.rechazarRevalorizar_);
		parameters.put("rechazarSubirArchivo_", this.rechazarSubirArchivo_);
		parameters.put("allDocumentsId_", JSONHelper.toJSON(allDocuments));
		return parameters;
	}

	public String downloadExcelRebaja() {
		Integer docDownload = Integer.valueOf(Integer.parseInt(getDocIdDownload()));
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
		return null;
	}

	public void guardaRebaja(){
		System.out.println("guardaRebaja Inicio");
		if(rebajaSeleccionada != null && !rebajaSeleccionada.trim().isEmpty()){
			Integer posicion = Integer.parseInt(posicionElemento);
			PlanillaRebajaCalculadaVO planillaRebajaCalculadaVO = rebajaComunas.get(posicion);
			planillaRebajaCalculadaVO = rebajaService.updateMontosRebajaComuna(this.idProcesoRebaja, planillaRebajaCalculadaVO);
			planillaRebajaCalculadaVO.setActualizar(false);
			rebajaComunas.set(posicion, planillaRebajaCalculadaVO);
			System.out.println("Actualizacion ok");
		}
		System.out.println("guardaRebaja Fin");
	}


	public void actualizarModelo(){
		System.out.println("actualizarModelo:posicionElemento="+posicionElemento);
		System.out.println("actualizarModelo:columnaElemento="+columnaElemento);
		System.out.println("actualizarModelo:valorElemento="+valorElemento);
		if((posicionElemento != null) && !(posicionElemento.trim().isEmpty()) 
				&& (columnaElemento != null) && !(columnaElemento.trim().isEmpty()) 
				&& (valorElemento != null) && !(valorElemento.trim().isEmpty())){
			Integer posicion = Integer.parseInt(posicionElemento);
			Integer columna = Integer.parseInt(columnaElemento);
			Double valor = Double.parseDouble(valorElemento);
			PlanillaRebajaCalculadaVO planillaRebajaCalculadaVO = rebajaComunas.get(posicion);
			planillaRebajaCalculadaVO.setActualizar(true);
			switch (columna) {
			case 1:
				planillaRebajaCalculadaVO.getCumplimientoRebajasItem1().setRebajaFinal(valor);
				break;
			case 2:
				planillaRebajaCalculadaVO.getCumplimientoRebajasItem2().setRebajaFinal(valor);
				break;
			case 3:
				planillaRebajaCalculadaVO.getCumplimientoRebajasItem3().setRebajaFinal(valor);
				break;
			default:
				break;
			}
			Integer porcentajeRebajaCalculado = 0;
			Integer porcentajeRebajaFinal = 0;
			porcentajeRebajaFinal += (( planillaRebajaCalculadaVO.getCumplimientoRebajasItem1().getRebajaFinal() == null) ? 0 : planillaRebajaCalculadaVO.getCumplimientoRebajasItem1().getRebajaFinal().intValue());
			porcentajeRebajaCalculado += (( planillaRebajaCalculadaVO.getCumplimientoRebajasItem1().getRebajaCalculada() == null) ? 0 : planillaRebajaCalculadaVO.getCumplimientoRebajasItem1().getRebajaCalculada().intValue()); 
			porcentajeRebajaFinal += (( planillaRebajaCalculadaVO.getCumplimientoRebajasItem2().getRebajaFinal() == null) ? 0 : planillaRebajaCalculadaVO.getCumplimientoRebajasItem2().getRebajaFinal().intValue());
			porcentajeRebajaCalculado += (( planillaRebajaCalculadaVO.getCumplimientoRebajasItem2().getRebajaCalculada() == null) ? 0 : planillaRebajaCalculadaVO.getCumplimientoRebajasItem2().getRebajaCalculada().intValue());
			porcentajeRebajaFinal += (( planillaRebajaCalculadaVO.getCumplimientoRebajasItem3().getRebajaFinal() == null) ? 0 : planillaRebajaCalculadaVO.getCumplimientoRebajasItem3().getRebajaFinal().intValue());
			porcentajeRebajaCalculado += (( planillaRebajaCalculadaVO.getCumplimientoRebajasItem3().getRebajaCalculada() == null) ? 0 : planillaRebajaCalculadaVO.getCumplimientoRebajasItem3().getRebajaCalculada().intValue());
			planillaRebajaCalculadaVO.setTotalRebajaCalculada(porcentajeRebajaCalculado);
			planillaRebajaCalculadaVO.setTotalRebajaRebajaFinal(porcentajeRebajaFinal);
			Integer montoRebaja = (int)(planillaRebajaCalculadaVO.getAporteEstatal() * (porcentajeRebajaFinal/100.0));
			planillaRebajaCalculadaVO.setMontoRebajaMes(montoRebaja);
			planillaRebajaCalculadaVO.setNuevoAporteEstatal(planillaRebajaCalculadaVO.getAporteEstatal() - montoRebaja);
			System.out.println("actualizarModelo:Fin");
		}
	}

	public void actualizarDummy(){
		System.out.println("ProcesoRebajaValidarMontosController:actualizarDummy");
	}


	public void onRowSelect(SelectEvent event) {  
		FacesMessage msg = new FacesMessage("PlanillaRebajaCalculadaVO Selected", ((PlanillaRebajaCalculadaVO) event.getObject()).getId_comuna()+"");  
		System.out.println("PlanillaRebajaCalculadaVO Selected"+ ((PlanillaRebajaCalculadaVO) event.getObject()).getId_comuna()+"");
		FacesContext.getCurrentInstance().addMessage(null, msg);  
	}  

	public void onRowUnselect(UnselectEvent event) {
		FacesMessage msg = new FacesMessage("PlanillaRebajaCalculadaVO Unselected", ((PlanillaRebajaCalculadaVO) event.getObject()).getId_comuna()+"");
		System.out.println("PlanillaRebajaCalculadaVO Unselected"+ ((PlanillaRebajaCalculadaVO) event.getObject()).getId_comuna()+"");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}


	public void onCellEdit(CellEditEvent event) {
		try{
			Object oldValue = event.getOldValue();
			Object newValue = event.getNewValue();
			System.out.println("oldValue->" + oldValue + " newValue->" + newValue);
			if(newValue != null && !newValue.equals(oldValue)) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public String enviar(){
		setAprobar_(true);
		setRechazarRevalorizar_(false);
		setRechazarSubirArchivo_(false);
		return super.enviar();
	}

	public String recargarArchivos(){
		setAprobar_(false);
		setRechazarRevalorizar_(false);
		setRechazarSubirArchivo_(true);
		return super.enviar();
	}

	public String recalcular(){
		setAprobar_(false);
		setRechazarRevalorizar_(true);
		setRechazarSubirArchivo_(false);
		return super.enviar();
	}

	@Override
	public String iniciarProceso() {
		return null;
	}

	public List<String> getComunasSeleccionadas() {
		return comunasSeleccionadas;
	}

	public void setComunasSeleccionadas(List<String> comunasSeleccionadas) {
		this.comunasSeleccionadas = comunasSeleccionadas;
	}

	public boolean isAprobar_() {
		return aprobar_;
	}
	public void setAprobar_(boolean aprobar_) {
		this.aprobar_ = aprobar_;
	}
	public boolean isRechazarRevalorizar_() {
		return rechazarRevalorizar_;
	}
	public void setRechazarRevalorizar_(boolean rechazarRevalorizar_) {
		this.rechazarRevalorizar_ = rechazarRevalorizar_;
	}
	public boolean isRechazarSubirArchivo_() {
		return rechazarSubirArchivo_;
	}
	public void setRechazarSubirArchivo_(boolean rechazarSubirArchivo_) {
		this.rechazarSubirArchivo_ = rechazarSubirArchivo_;
	}
	public List<RegionVO> getListaRegiones() {
		return listaRegiones;
	}
	public void setListaRegiones(List<RegionVO> listaRegiones) {
		this.listaRegiones = listaRegiones;
	}

	public List<ServiciosVO> getListaServicios() {
		return listaServicios;
	}

	public void setListaServicios(List<ServiciosVO> listaServicios) {
		this.listaServicios = listaServicios;
	}

	public String getRegionSeleccionada() {
		return regionSeleccionada;
	}

	public void setRegionSeleccionada(String regionSeleccionada) {
		this.regionSeleccionada = regionSeleccionada;
	}

	public String getServicioSeleccionado() {
		return servicioSeleccionado;
	}

	public void setServicioSeleccionado(String servicioSeleccionado) {
		this.servicioSeleccionado = servicioSeleccionado;
	}

	public List<ComunaVO> getListaComunas() {
		return listaComunas;
	}

	public void setListaComunas(List<ComunaVO> listaComunas) {
		this.listaComunas = listaComunas;
	}

	public List<PlanillaRebajaCalculadaVO> getRebajaComunas() {
		return rebajaComunas;
	}

	public void setRebajaComunas(List<PlanillaRebajaCalculadaVO> rebajaComunas) {
		this.rebajaComunas = rebajaComunas;
	}

	public String getDocIdDownload() {
		return docIdDownload;
	}

	public void setDocIdDownload(String docIdDownload) {
		this.docIdDownload = docIdDownload;
	}

	public Integer getDocRebaja() {
		return docRebaja;
	}

	public void setDocRebaja(Integer docRebaja) {
		this.docRebaja = docRebaja;
	}

	public String getRebajaSeleccionada() {
		return rebajaSeleccionada;
	}

	public void setRebajaSeleccionada(String rebajaSeleccionada) {
		this.rebajaSeleccionada = rebajaSeleccionada;
	}

	public String getMesActual() {
		return mesActual;
	}

	public void setMesActual(String mesActual) {
		this.mesActual = mesActual;
	}

	public List<Integer> getAllDocuments() {
		return allDocuments;
	}

	public void setAllDocuments(List<Integer> allDocuments) {
		this.allDocuments = allDocuments;
	}

	public List<DocumentSummaryVO> getResumenDocumentos() {
		return resumenDocumentos;
	}

	public void setResumenDocumentos(List<DocumentSummaryVO> resumenDocumentos) {
		this.resumenDocumentos = resumenDocumentos;
	}

	public Integer getIdProcesoRebaja() {
		return idProcesoRebaja;
	}

	public void setIdProcesoRebaja(Integer idProcesoRebaja) {
		this.idProcesoRebaja = idProcesoRebaja;
	}

	public Integer getFisrtTime() {
		return fisrtTime;
	}

	public void setFisrtTime(Integer fisrtTime) {
		this.fisrtTime = fisrtTime;
	}

	public PlanillaRebajaCalculadaVO getSelectedPlanilla() {
		return selectedPlanilla;
	}

	public void setSelectedPlanilla(PlanillaRebajaCalculadaVO selectedPlanilla) {
		this.selectedPlanilla = selectedPlanilla;
	}

	public String getPosicionElemento() {
		return posicionElemento;
	}

	public void setPosicionElemento(String posicionElemento) {
		this.posicionElemento = posicionElemento;
	}

	public String getColumnaElemento() {
		return columnaElemento;
	}

	public void setColumnaElemento(String columnaElemento) {
		this.columnaElemento = columnaElemento;
	}

	public String getValorElemento() {
		return valorElemento;
	}

	public void setValorElemento(String valorElemento) {
		this.valorElemento = valorElemento;
	}

	public Integer getTotalIncumplimiento() {
		return totalIncumplimiento;
	}

	public void setTotalIncumplimiento(Integer totalIncumplimiento) {
		this.totalIncumplimiento = totalIncumplimiento;
	}

	public TipoCumplimientoVO getCumplimientoItem1() {
		if(cumplimientoItem1 == null){
			cumplimientoItem1 = rebajaService.getItemCumplimientoByType(TiposCumplimientos.ACTIVIDADGENERAL);
		}
		return cumplimientoItem1;
	}

	public void setCumplimientoItem1(TipoCumplimientoVO cumplimientoItem1) {
		this.cumplimientoItem1 = cumplimientoItem1;
	}

	public TipoCumplimientoVO getCumplimientoItem2() {
		if(cumplimientoItem2 == null){
			cumplimientoItem2 = rebajaService.getItemCumplimientoByType(TiposCumplimientos.CONTINUIDADATENCIONSALUD);
		}
		return cumplimientoItem2;
	}

	public void setCumplimientoItem2(TipoCumplimientoVO cumplimientoItem2) {
		this.cumplimientoItem2 = cumplimientoItem2;
	}

	public TipoCumplimientoVO getCumplimientoItem3() {
		if(cumplimientoItem3 == null){
			cumplimientoItem3 = rebajaService.getItemCumplimientoByType(TiposCumplimientos.ACTIVIDADGARANTIASEXPLICITASSALUD);
		}
		return cumplimientoItem3;
	}

	public void setCumplimientoItem3(TipoCumplimientoVO cumplimientoItem3) {
		this.cumplimientoItem3 = cumplimientoItem3;
	}

}
