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

import minsal.divap.enums.BusinessProcess;
import minsal.divap.enums.Subtitulo;
import minsal.divap.service.AlfrescoService;
import minsal.divap.service.ProgramasService;
import minsal.divap.service.ReliquidacionService;
import minsal.divap.service.ServicioSaludService;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ProgramaComponentesCuotasSummaryVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ServiciosVO;
import minsal.divap.vo.TaskDataVO;
import minsal.divap.vo.TaskVO;
import minsal.divap.vo.ValorizarReliquidacionPageSummaryVO;

import org.apache.log4j.Logger;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;
import cl.redhat.bandejaTareas.util.JSONHelper;

@Named("procesoReliquidacionMuniController") 
@ViewScoped
public class ProcesoReliquidacionMuniController extends AbstractTaskMBean implements
				Serializable {
	

	private static final long serialVersionUID = 5164638468861890516L;
	@Inject
	private transient Logger log;
	@Inject 
	FacesContext facesContext;
	@EJB
	private ProgramasService programaService;
	@EJB
	private AlfrescoService alfrescoService;
	@EJB
	private ServicioSaludService serviciosService;
	@EJB
	private ReliquidacionService reliquidacionService;
	private String docIdDownload;
	private Integer idDocReliquidacionMunicipal;
	private Integer docBaseMunicipal;
	private Boolean conReparos = false;
	private List<ComponentesVO> componentes;
	private String componenteSeleccionado;
	private String servicioSeleccionado;
	private ProgramaComponentesCuotasSummaryVO programaComponentesCuotas;
	private Integer idReliquidacion;
	private ProgramaVO programa;
	private List<ValorizarReliquidacionPageSummaryVO> reliquidacionMunicipal;
	private List<ServiciosVO> servicios;
	
	private Long totalMarcoFinal=0L;
	private Long totalRebajaUltimaCouta=0L;
	private Long totalUltimaCouta=0L;
	private Long totalMontoConvenios=0L;
	private List<Long> totalMontosCuotas;
	
	@PostConstruct
	@SuppressWarnings("unchecked")
	public void init() {
		if(sessionExpired()){
			return;
		}
		this.servicioSeleccionado = "0";
		this.componenteSeleccionado = "0";
		if (getTaskDataVO() != null && getTaskDataVO().getData() != null) {
			Integer idProgramaAno = (Integer) getTaskDataVO().getData().get("_idProgramaAno");
			setPrograma(programaService.getProgramaAno(idProgramaAno));
			this.idReliquidacion = (Integer) getTaskDataVO().getData().get("_idReliquidacion");
			String idDocReliquidacion = (String) getTaskDataVO().getData().get("_idDocReliquidacion");
			String idDocumentosBase = (String) getTaskDataVO().getData().get("_documentos");
			if(idDocumentosBase != null){
				List<Integer> documentos = JSONHelper.fromJSON(idDocumentosBase, List.class);
				if(documentos != null){
					docBaseMunicipal = documentos.get(0);
				}
			}
			if(idDocReliquidacion != null){
				String [] splitDocReliquidacion = idDocReliquidacion.split("\\#");
				if(splitDocReliquidacion != null){
					if(splitDocReliquidacion.length == 1){
						this.idDocReliquidacionMunicipal = Integer.parseInt(splitDocReliquidacion[0]);
					}
				}
			}
			System.out.println("this.idProgramaAno --> "+idProgramaAno);
			System.out.println("this.idReliquidacion --> "+this.idReliquidacion);
			
			if(getPrograma() != null){
				componentes = programaService.getComponentesByProgramaAnoSubtitulos(programa.getIdProgramaAno(), Subtitulo.SUBTITULO24);
				this.componenteSeleccionado =  componentes.get(0).getId().toString();
				this.programaComponentesCuotas = reliquidacionService.getProgramaComponenteCuotas(getPrograma().getIdProgramaAno(), Integer.parseInt(this.componenteSeleccionado));
				this.reliquidacionMunicipal = reliquidacionService.getReliquidacionMunicipalSummaryVO(getPrograma().getIdProgramaAno(), Integer.parseInt(this.componenteSeleccionado), null, getIdReliquidacion());
			}
		}
		System.out.println("reliquidacionMunicipal.size() --> " +((this.reliquidacionMunicipal==null)?"0":this.reliquidacionMunicipal.size()));
	}

	public Long getTotalServicio(){
		Long suma = 0L;
		return suma;
	}
	
	public Long getTotalMunicipal(){
		Long suma = 0L;
		return suma;
	}

	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		System.out.println("createResultData usuario-->"+getSessionBean().getUsername());
		parameters.put("usuario", getSessionBean().getUsername());
		parameters.put("aprobar_", this.conReparos);
		return parameters;
	}

	@Override
	public String iniciarProceso() {
		String success = "divapProcesoReliqProgramas";
		Long procId = iniciarProceso(BusinessProcess.RELIQUIDACION);
		System.out.println("procId-->"+procId);
		if(procId == null){
			 success = null;
		}else{
			TaskVO task = getUserTasksByProcessId(procId, getSessionBean().getUsername());
			if(task != null){
				TaskDataVO taskDataVO = getTaskData(task.getId());
				if(taskDataVO != null){
					System.out.println("taskDataVO recuperada="+taskDataVO);
					setOnSession("taskDataSeleccionada", taskDataVO);
				}
			}
		}
		return success;
	}
	
	public void buscarReliquidacion(){
		System.out.println("buscar--> .componenteSeleccionado=" + componenteSeleccionado + " servicioSeleccionado=" + servicioSeleccionado);
		if(componenteSeleccionado == null || componenteSeleccionado.trim().isEmpty() || componenteSeleccionado.trim().equals("0")){
			FacesMessage msg = new FacesMessage("Debe seleccionar el componente antes de realizar la búsqueda");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}else{
			Integer idComponente = Integer.parseInt(componenteSeleccionado);
			Integer idServicio = ((servicioSeleccionado == null || servicioSeleccionado.trim().isEmpty() || servicioSeleccionado.trim().equals("0"))?null:Integer.parseInt(servicioSeleccionado));
			programaComponentesCuotas = reliquidacionService.getProgramaComponenteCuotas(getPrograma().getIdProgramaAno(), idComponente);
			this.reliquidacionMunicipal = reliquidacionService.getReliquidacionMunicipalSummaryVO(getPrograma().getIdProgramaAno(), idComponente, idServicio, getIdReliquidacion()); 
		}
		System.out.println("reliquidacionMunicipal.size() --> " +((this.reliquidacionMunicipal==null)?"0":this.reliquidacionMunicipal.size()));
		System.out.println("fin buscar-->");
	}
	
	public Integer getIdReliquidacion() {
		return idReliquidacion;
	}

	public void setIdReliquidacion(Integer idReliquidacion) {
		this.idReliquidacion = idReliquidacion;
	}

	public ProgramaVO getPrograma() {
		return programa;
	}

	public void setPrograma(ProgramaVO programa) {
		this.programa = programa;
	}
	
	public String getDocIdDownload() {
		return docIdDownload;
	}

	public void setDocIdDownload(String docIdDownload) {
		this.docIdDownload = docIdDownload;
	}

	public Integer getIdDocReliquidacionMunicipal() {
		return idDocReliquidacionMunicipal;
	}

	public void setIdDocReliquidacionMunicipal(Integer idDocReliquidacionMunicipal) {
		this.idDocReliquidacionMunicipal = idDocReliquidacionMunicipal;
	}
	
	public Integer getDocBaseMunicipal() {
		return docBaseMunicipal;
	}

	public void setDocBaseMunicipal(Integer docBaseMunicipal) {
		this.docBaseMunicipal = docBaseMunicipal;
	}
	
	public String continuar(){
		conReparos = false;
		return super.enviar();
	}
	
	public String continuarConReparos(){
		conReparos = true;
		return super.enviar();
	}

	public String downloadTemplate() {
		Integer docDownload =  Integer.parseInt(getDocIdDownload());
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
		return null;
	}

	public List<ComponentesVO> getComponentes() {
		return componentes;
	}

	public void setComponentes(List<ComponentesVO> componentes) {
		this.componentes = componentes;
	}

	public String getComponenteSeleccionado() {
		return componenteSeleccionado;
	}

	public void setComponenteSeleccionado(String componenteSeleccionado) {
		this.componenteSeleccionado = componenteSeleccionado;
	}

	public String getServicioSeleccionado() {
		return servicioSeleccionado;
	}

	public void setServicioSeleccionado(String servicioSeleccionado) {
		this.servicioSeleccionado = servicioSeleccionado;
	}

	public ProgramaComponentesCuotasSummaryVO getProgramaComponentesCuotas() {
		return programaComponentesCuotas;
	}

	public void setProgramaComponentesCuotas(
			ProgramaComponentesCuotasSummaryVO programaComponentesCuotas) {
		this.programaComponentesCuotas = programaComponentesCuotas;
	}

	public List<ValorizarReliquidacionPageSummaryVO> getReliquidacionMunicipal() {
		return reliquidacionMunicipal;
	}

	public void setReliquidacionMunicipal(
			List<ValorizarReliquidacionPageSummaryVO> reliquidacionMunicipal) {
		this.reliquidacionMunicipal = reliquidacionMunicipal;
	}

	public List<ServiciosVO> getServicios() {
		if(servicios == null){
			servicios = serviciosService.getAllServiciosVO();
		}
		return servicios;
	}

	public void setServicios(List<ServiciosVO> servicios) {
		this.servicios = servicios;
	}

	public Long getTotalMarcoFinal() {
		totalMarcoFinal = 0L;
		if(reliquidacionMunicipal != null && reliquidacionMunicipal.size() > 0){
			for(ValorizarReliquidacionPageSummaryVO valorizarReliquidacionPageSummaryVO : reliquidacionMunicipal){
				totalMarcoFinal+= ((valorizarReliquidacionPageSummaryVO.getMarcoFinal() == null)?0L:valorizarReliquidacionPageSummaryVO.getMarcoFinal());
			}
		}
		return totalMarcoFinal;
	}

	public void setTotalMarcoFinal(Long totalMarcoFinal) {
		this.totalMarcoFinal = totalMarcoFinal;
	}

	public Long getTotalRebajaUltimaCouta() {
		totalRebajaUltimaCouta = 0L;
		if(reliquidacionMunicipal != null && reliquidacionMunicipal.size() > 0){
			for(ValorizarReliquidacionPageSummaryVO valorizarReliquidacionPageSummaryVO : reliquidacionMunicipal){
				if(valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO() != null){
					totalRebajaUltimaCouta+= ((valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getRebajaUltimaCuota() == null)?0L:valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getRebajaUltimaCuota());
				}
			}
		}
		return totalRebajaUltimaCouta;
	}

	public void setTotalRebajaUltimaCouta(Long totalRebajaUltimaCouta) {
		this.totalRebajaUltimaCouta = totalRebajaUltimaCouta;
	}

	public Long getTotalUltimaCouta() {
		totalUltimaCouta =0L;
		if(reliquidacionMunicipal != null && reliquidacionMunicipal.size() > 0){
			for(ValorizarReliquidacionPageSummaryVO valorizarReliquidacionPageSummaryVO : reliquidacionMunicipal){
				if(valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO() != null){
					totalUltimaCouta+= ((valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getMontoUltimaCuota() == null)?0L:valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getMontoUltimaCuota());
				}
			}
		}
		return totalUltimaCouta;
	}

	public void setTotalUltimaCouta(Long totalUltimaCouta) {
		this.totalUltimaCouta = totalUltimaCouta;
	}

	public Long getTotalMontoConvenios() {
		totalMontoConvenios = 0L;
		if(reliquidacionMunicipal != null && reliquidacionMunicipal.size() > 0){
			for(ValorizarReliquidacionPageSummaryVO valorizarReliquidacionPageSummaryVO : reliquidacionMunicipal){
				if(valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO() != null){
					totalMontoConvenios+= ((valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getMarcoInicial() == null)?0L:valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getMarcoInicial());
				}
			}
		}
		return totalMontoConvenios;
	}

	public void setTotalMontoConvenios(Long totalMontoConvenios) {
		this.totalMontoConvenios = totalMontoConvenios;
	}

	public List<Long> getTotalMontosCuotas() {
		totalMontosCuotas = new ArrayList<Long>();
		if(getProgramaComponentesCuotas() != null && getProgramaComponentesCuotas().getCuotas()!= null && getProgramaComponentesCuotas().getCuotas().size() > 0){
			for(int i = 0; i<getProgramaComponentesCuotas().getCuotas().size();i++){
				totalMontosCuotas.add(0L);
			}
		}
		if(reliquidacionMunicipal != null && reliquidacionMunicipal.size() > 0){
			for(ValorizarReliquidacionPageSummaryVO valorizarReliquidacionPageSummaryVO : reliquidacionMunicipal){
				if(valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO() != null){
					for(int cont = 0; cont < totalMontosCuotas.size(); cont++){
						long monto = totalMontosCuotas.get(cont);
						switch (cont) {
						case 0:
							monto += ((valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getCuotasUno() == null)?0L:valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getCuotasUno().getMonto());
							break;
						case 1:
							monto += ((valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getUltimaCuota() == null)?0L:valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getUltimaCuota().getMonto());
							break;
						default:
							break;
						}
						totalMontosCuotas.set(cont, monto);
					}
				}
			}
		}
		return totalMontosCuotas;
	}

	public void setTotalMontosCuotas(List<Long> totalMontosCuotas) {
		this.totalMontosCuotas = totalMontosCuotas;
	}
	
}
