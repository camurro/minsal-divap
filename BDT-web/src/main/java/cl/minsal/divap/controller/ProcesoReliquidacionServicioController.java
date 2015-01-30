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

import minsal.divap.enums.Subtitulo;
import minsal.divap.service.ProgramasService;
import minsal.divap.service.ReliquidacionService;
import minsal.divap.service.ServicioSaludService;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ProgramaComponentesCuotasSummaryVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ServiciosVO;
import minsal.divap.vo.ValorizarReliquidacionPageSummaryVO;

import org.primefaces.event.TabChangeEvent;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;
import cl.redhat.bandejaTareas.util.JSONHelper;

@Named("procesoReliquidacionServicioController") 
@ViewScoped
public class ProcesoReliquidacionServicioController extends AbstractTaskMBean implements
Serializable {


	private static final long serialVersionUID = 5164638468861890516L;
	@EJB
	private ProgramasService programaService;
	@EJB
	private ReliquidacionService reliquidacionService;
	@EJB
	private ServicioSaludService serviciosService;
	private ProgramaComponentesCuotasSummaryVO programaComponentesCuotas;
	private List<ServiciosVO> servicios;
	private ProgramaVO programaSeleccionado;
	private List<ComponentesVO> componentes;
	private String docIdDownload;
	private Integer idReliquidacion;
	private String componenteSeleccionado;
	private String servicioSeleccionado;
	private ProgramaVO programa;
	private Integer docBaseMunicipal;
	private Integer docBaseServicio;
	private Integer idDocReliquidacionMunicipal;
	private Integer idDocReliquidacionServicio;
	private Boolean conReparos = false;
	private Integer activeTab = 0;
	private List<ValorizarReliquidacionPageSummaryVO> reliquidacionMunicipal;
	private List<ValorizarReliquidacionPageSummaryVO> reliquidacionServicio;
	private Boolean mostrarMunicipal = false;
	private Boolean mostrarServicio = false;
	private Long totalMarcoFinal=0L;
	private Long totalRebajaUltimaCouta=0L;
	private Long totalUltimaCouta=0L;
	private Long totalMontoConvenios=0L;
	private List<Long> totalMontosCuotas;
	
	private Long totalMontoConveniosMunicipal=0L;
	private List<Long> totalMontosCuotasMunicipal;
	private Long totalRebajaUltimaCuotaMunicipal=0L;
	private Long totalUltimaCuotaMunicipal=0L;
	private Long totalMarcoFinalMunicipal=0L;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		if(sessionExpired()){
			return;
		}
		this.servicioSeleccionado = "0";
		this.componenteSeleccionado = "0";
		if (getTaskDataVO() != null && getTaskDataVO().getData() != null) {
			Integer idProgramaAno = (Integer) getTaskDataVO().getData().get("_idProgramaAno");
			System.out.println("this.idProgramaAno --> " + idProgramaAno);
			this.idReliquidacion = (Integer) getTaskDataVO().getData().get("_idReliquidacion");
			String idDocReliquidacion = (String) getTaskDataVO().getData().get("_idDocReliquidacion");
			String idDocumentosBase = (String) getTaskDataVO().getData().get("_documentos");
			if(idDocumentosBase != null){
				List<Integer> documentos = JSONHelper.fromJSON(idDocumentosBase, List.class);
				if(documentos != null){
					if(documentos.size() == 2){
						docBaseMunicipal = documentos.get(0);
						docBaseServicio = documentos.get(1);
					}else{
						docBaseServicio = documentos.get(0);
					}
				}
			}
			if(idDocReliquidacion != null){
				String [] splitDocReliquidacion = idDocReliquidacion.split("\\#");
				if(splitDocReliquidacion != null){
					if(splitDocReliquidacion.length == 2){
						this.idDocReliquidacionMunicipal = Integer.parseInt(splitDocReliquidacion[0]);
						this.idDocReliquidacionServicio = Integer.parseInt(splitDocReliquidacion[1]);
					}else{
						this.idDocReliquidacionServicio = Integer.parseInt(splitDocReliquidacion[0]);
					}
				}
			}
			setPrograma(programaService.getProgramaAno(idProgramaAno));
			if(getPrograma() != null){
				if(getPrograma().getDependenciaMunicipal() && getPrograma().getDependenciaServicio()){
					mostrarMunicipal = true;
					mostrarServicio = true;
					Subtitulo[] subtitulosMunicipal = { Subtitulo.SUBTITULO24};
					componentes = programaService.getComponenteByProgramaSubtitulos(getPrograma().getId(),  subtitulosMunicipal);
					this.componenteSeleccionado =  componentes.get(0).getId().toString();
					this.programaComponentesCuotas = reliquidacionService.getProgramaComponenteCuotas(getPrograma().getIdProgramaAno(), Integer.parseInt(this.componenteSeleccionado));
					//Integer idProgramaAno, Integer idComponente, Integer idSubtitulo, Integer idServicio, Integer idReliquidacion
					//this.reliquidacionServicio = reliquidacionService.getReliquidacionSummaryVO(getPrograma().getIdProgramaAno(), Integer.parseInt(this.componenteSeleccionado), null, getIdReliquidacion());
					this.reliquidacionMunicipal = reliquidacionService.getReliquidacionMunicipalSummaryVO(getPrograma().getIdProgramaAno(), Integer.parseInt(this.componenteSeleccionado), null, getIdReliquidacion());
				}else{
					mostrarMunicipal = false;
					mostrarServicio = true;
					Subtitulo[] subtitulosServicios = {Subtitulo.SUBTITULO21, Subtitulo.SUBTITULO22, Subtitulo.SUBTITULO29};
					componentes = programaService.getComponenteByProgramaSubtitulos(getPrograma().getId(),  subtitulosServicios);
					this.componenteSeleccionado =  componentes.get(0).getId().toString();
					this.programaComponentesCuotas = reliquidacionService.getProgramaComponenteCuotas(getPrograma().getIdProgramaAno(), Integer.parseInt(this.componenteSeleccionado));
					//this.reliquidacionServicio = reliquidacionService.getReliquidacionSummaryVO(getPrograma().getIdProgramaAno(), Integer.parseInt(this.componenteSeleccionado), null, getIdReliquidacion());
				}
			}
			System.out.println("this.docBaseMunicipal --> " + this.docBaseMunicipal);
			System.out.println("this.docBaseServicio --> " + this.docBaseServicio);
			System.out.println("this.idDocReliquidacionMunicipal --> " + this.idDocReliquidacionMunicipal);
			System.out.println("this.idDocReliquidacionServicio --> " + this.idDocReliquidacionServicio);
			System.out.println("this.idReliquidacion --> " + this.idReliquidacion);
			System.out.println("reliquidacionMunicipal.size() --> " +((this.reliquidacionMunicipal==null)?"0":this.reliquidacionMunicipal.size()));
			System.out.println("reliquidacionServicio.size() --> " +((this.reliquidacionServicio==null)?"0":this.reliquidacionServicio.size()));
		}
	}

	public Long getTotalServicio(){
		Long suma = 0L;
		return suma;
	}

	public Long getTotalMunicipal(){
		Long suma = 0L;
		return suma;
	}

	public void onTabChange(TabChangeEvent event) {
		System.out.println("Tab Changed, Active Tab: " + event.getTab().getTitle());
		System.out.println("event.getTab().getId(): " + event.getTab().getId());
		if(this.mostrarMunicipal && this.mostrarServicio){
			if(getActiveTab().equals(0)){
				Subtitulo[] subtitulos =  {Subtitulo.SUBTITULO24};
				componentes = programaService.getComponenteByProgramaSubtitulos(getPrograma().getId(),  subtitulos);
				this.componenteSeleccionado =  componentes.get(0).getId().toString();
				this.reliquidacionMunicipal = reliquidacionService.getReliquidacionMunicipalSummaryVO(getPrograma().getIdProgramaAno(), Integer.parseInt(this.componenteSeleccionado), null, getIdReliquidacion());
			}else{
				Subtitulo[] subtitulos =  {Subtitulo.SUBTITULO21, Subtitulo.SUBTITULO22, Subtitulo.SUBTITULO29};
				componentes = programaService.getComponenteByProgramaSubtitulos(getPrograma().getId(),  subtitulos);
				this.componenteSeleccionado =  componentes.get(0).getId().toString();
				//this.reliquidacionServicio = reliquidacionService.getReliquidacionSummaryVO(getPrograma().getIdProgramaAno(), Integer.parseInt(this.componenteSeleccionado), null, getIdReliquidacion());
			}
		}else{
			Subtitulo[] subtitulos =  {Subtitulo.SUBTITULO21, Subtitulo.SUBTITULO22, Subtitulo.SUBTITULO29};
			componentes = programaService.getComponenteByProgramaSubtitulos(getPrograma().getId(),  subtitulos);
			this.componenteSeleccionado =  componentes.get(0).getId().toString();
			//this.reliquidacionServicio = reliquidacionService.getReliquidacionSummaryVO(getPrograma().getIdProgramaAno(), Integer.parseInt(this.componenteSeleccionado), null, getIdReliquidacion());
		}
	}

	public Integer getActiveTab() {
		System.out.println("getActiveTab "+activeTab);
		return activeTab;
	}

	public void setActiveTab(Integer activeTab) {
		System.out.println("setActiveTab "+activeTab);
		this.activeTab = activeTab;
	}

	public void buscar() {
		System.out.println("buscar--> .componenteSeleccionado=" + componenteSeleccionado + " servicioSeleccionado=" + servicioSeleccionado);
		if(componenteSeleccionado == null || componenteSeleccionado.trim().isEmpty() || componenteSeleccionado.trim().equals("0")){
			FacesMessage msg = new FacesMessage("Debe seleccionar el componente antes de realizar la búsqueda");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}else{
			Integer idComponente = Integer.parseInt(componenteSeleccionado);
			Integer idServicio = ((servicioSeleccionado == null || servicioSeleccionado.trim().isEmpty() || servicioSeleccionado.trim().equals("0"))?null:Integer.parseInt(servicioSeleccionado));
			programaComponentesCuotas = reliquidacionService.getProgramaComponenteCuotas(getPrograma().getIdProgramaAno(), idComponente);
			if(this.mostrarMunicipal && this.mostrarServicio){
				if(getActiveTab().equals(0)){
					this.reliquidacionServicio = new ArrayList<ValorizarReliquidacionPageSummaryVO>(); 
					this.reliquidacionMunicipal = reliquidacionService.getReliquidacionMunicipalSummaryVO(getPrograma().getIdProgramaAno(), idComponente, idServicio, getIdReliquidacion()); 
				}else{
					this.reliquidacionMunicipal = new ArrayList<ValorizarReliquidacionPageSummaryVO>(); 
					//this.reliquidacionServicio = reliquidacionService.getReliquidacionSummaryVO(getPrograma().getIdProgramaAno(), idComponente, idServicio, getIdReliquidacion());
				}
			}else{
				this.reliquidacionMunicipal = new ArrayList<ValorizarReliquidacionPageSummaryVO>(); 
				//this.reliquidacionServicio = reliquidacionService.getReliquidacionSummaryVO(getPrograma().getIdProgramaAno(), idComponente, idServicio, getIdReliquidacion()); 
			}
		}
		System.out.println("reliquidacionMunicipal.size() --> " +((this.reliquidacionMunicipal==null)?"0":this.reliquidacionMunicipal.size()));
		System.out.println("reliquidacionServicio.size() --> " +((this.reliquidacionServicio==null)?"0":this.reliquidacionServicio.size()));
		System.out.println("fin buscar-->");
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
		return null;
	}

	public void buscarReliquidacion(){

	}

	public ProgramaVO getProgramaSeleccionado() {
		return programaSeleccionado;
	}

	public void setProgramaSeleccionado(ProgramaVO programaSeleccionado) {
		this.programaSeleccionado = programaSeleccionado;
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

	public Integer getIdDocReliquidacionMunicipal() {
		return idDocReliquidacionMunicipal;
	}

	public void setIdDocReliquidacionMunicipal(Integer idDocReliquidacionMunicipal) {
		this.idDocReliquidacionMunicipal = idDocReliquidacionMunicipal;
	}

	public Integer getIdDocReliquidacionServicio() {
		return idDocReliquidacionServicio;
	}

	public void setIdDocReliquidacionServicio(Integer idDocReliquidacionServicio) {
		this.idDocReliquidacionServicio = idDocReliquidacionServicio;
	}

	public String getDocIdDownload() {
		return docIdDownload;
	}

	public void setDocIdDownload(String docIdDownload) {
		this.docIdDownload = docIdDownload;
	}

	public String downloadTemplate() {
		Integer docDownload =  Integer.parseInt(getDocIdDownload());
		setDocumento(documentService.getDocument(docDownload));
		super.downloadDocument();
		return null;
	}

	public List<ValorizarReliquidacionPageSummaryVO> getReliquidacionMunicipal() {
		if(reliquidacionMunicipal == null){
			reliquidacionMunicipal = new ArrayList<ValorizarReliquidacionPageSummaryVO>();
		}
		return reliquidacionMunicipal;
	}

	public void setReliquidacionMunicipal(
			List<ValorizarReliquidacionPageSummaryVO> reliquidacionMunicipal) {
		this.reliquidacionMunicipal = reliquidacionMunicipal;
	}

	public List<ValorizarReliquidacionPageSummaryVO> getReliquidacionServicio() {
		if(reliquidacionServicio == null){
			//reliquidacionServicio = reliquidacionService.getReliquidacionSummaryVO(getPrograma().getIdProgramaAno(), Integer.parseInt(this.componenteSeleccionado), null, getIdReliquidacion());
		}
		return reliquidacionServicio;
	}


	public void setReliquidacionServicio(
			List<ValorizarReliquidacionPageSummaryVO> reliquidacionServicio) {
		this.reliquidacionServicio = reliquidacionServicio;
	}

	public String continuar(){
		conReparos = false;
		return super.enviar();
	}



	public String continuarConReparos(){
		conReparos = true;
		return super.enviar();
	}

	public Integer getDocBaseMunicipal() {
		return docBaseMunicipal;
	}

	public void setDocBaseMunicipal(Integer docBaseMunicipal) {
		this.docBaseMunicipal = docBaseMunicipal;
	}

	public Integer getDocBaseServicio() {
		return docBaseServicio;
	}

	public void setDocBaseServicio(Integer docBaseServicio) {
		this.docBaseServicio = docBaseServicio;
	}

	public Boolean getMostrarMunicipal() {
		return mostrarMunicipal;
	}

	public void setMostrarMunicipal(Boolean mostrarMunicipal) {
		this.mostrarMunicipal = mostrarMunicipal;
	}

	public Boolean getMostrarServicio() {
		return mostrarServicio;
	}

	public void setMostrarServicio(Boolean mostrarServicio) {
		this.mostrarServicio = mostrarServicio;
	}

	public ProgramaComponentesCuotasSummaryVO getProgramaComponentesCuotas() {
		return programaComponentesCuotas;
	}

	public void setProgramaComponentesCuotas(
			ProgramaComponentesCuotasSummaryVO programaComponentesCuotas) {
		this.programaComponentesCuotas = programaComponentesCuotas;
	}

	public List<ComponentesVO> getComponentes() {
		return componentes;
	}

	public void setComponentes(List<ComponentesVO> componentes) {
		this.componentes = componentes;
	}

	public Long getTotalMarcoFinal() {
		totalMarcoFinal = 0L;
		if(reliquidacionServicio != null && reliquidacionServicio.size() > 0){
			for(ValorizarReliquidacionPageSummaryVO valorizarReliquidacionPageSummaryVO : reliquidacionServicio){
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
		if(reliquidacionServicio != null && reliquidacionServicio.size() > 0){
			for(ValorizarReliquidacionPageSummaryVO valorizarReliquidacionPageSummaryVO : reliquidacionServicio){
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
		if(reliquidacionServicio != null && reliquidacionServicio.size() > 0){
			for(ValorizarReliquidacionPageSummaryVO valorizarReliquidacionPageSummaryVO : reliquidacionServicio){
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
		if(reliquidacionServicio != null && reliquidacionServicio.size() > 0){
			for(ValorizarReliquidacionPageSummaryVO valorizarReliquidacionPageSummaryVO : reliquidacionServicio){
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
		if(reliquidacionServicio != null && reliquidacionServicio.size() > 0){
			for(ValorizarReliquidacionPageSummaryVO valorizarReliquidacionPageSummaryVO : reliquidacionServicio){
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

	public Long getTotalMontoConveniosMunicipal() {
		totalMontoConveniosMunicipal = 0L;
		if(reliquidacionMunicipal != null && reliquidacionMunicipal.size() > 0){
			for(ValorizarReliquidacionPageSummaryVO valorizarReliquidacionPageSummaryVO : reliquidacionMunicipal){
				if(valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO() != null){
					totalMontoConveniosMunicipal+= ((valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getMarcoInicial() == null)?0L:valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getMarcoInicial());
				}
			}
		}
		return totalMontoConveniosMunicipal;
	}

	public void setTotalMontoConveniosMunicipal(Long totalMontoConveniosMunicipal) {
		this.totalMontoConveniosMunicipal = totalMontoConveniosMunicipal;
	}
	
	public List<Long> getTotalMontosCuotasMunicipal() {
		totalMontosCuotasMunicipal = new ArrayList<Long>();
		if(getProgramaComponentesCuotas() != null && getProgramaComponentesCuotas().getCuotas()!= null && getProgramaComponentesCuotas().getCuotas().size() > 0){
			for(int i = 0; i<getProgramaComponentesCuotas().getCuotas().size();i++){
				totalMontosCuotasMunicipal.add(0L);
			}
		}
		if(reliquidacionMunicipal != null && reliquidacionMunicipal.size() > 0){
			for(ValorizarReliquidacionPageSummaryVO valorizarReliquidacionPageSummaryVO : reliquidacionMunicipal){
				if(valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO() != null){
					for(int cont = 0; cont < totalMontosCuotasMunicipal.size(); cont++){
						long monto = totalMontosCuotasMunicipal.get(cont);
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
						totalMontosCuotasMunicipal.set(cont, monto);
					}
				}
			}
		}
		return totalMontosCuotasMunicipal;
	}

	public void setTotalMontosCuotasMunicipal(List<Long> totalMontosCuotasMunicipal) {
		this.totalMontosCuotasMunicipal = totalMontosCuotasMunicipal;
	}

	public Long getTotalRebajaUltimaCuotaMunicipal() {
		totalRebajaUltimaCuotaMunicipal = 0L;
		if(reliquidacionMunicipal != null && reliquidacionMunicipal.size() > 0){
			for(ValorizarReliquidacionPageSummaryVO valorizarReliquidacionPageSummaryVO : reliquidacionMunicipal){
				if(valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO() != null){
					totalRebajaUltimaCuotaMunicipal+= ((valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getRebajaUltimaCuota() == null)?0L:valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getRebajaUltimaCuota());
				}
			}
		}
		return totalRebajaUltimaCuotaMunicipal;
	}

	public void setTotalRebajaUltimaCuotaMunicipal(
			Long totalRebajaUltimaCuotaMunicipal) {
		this.totalRebajaUltimaCuotaMunicipal = totalRebajaUltimaCuotaMunicipal;
	}

	public Long getTotalUltimaCuotaMunicipal() {
		totalUltimaCuotaMunicipal =0L;
		if(reliquidacionMunicipal != null && reliquidacionMunicipal.size() > 0){
			for(ValorizarReliquidacionPageSummaryVO valorizarReliquidacionPageSummaryVO : reliquidacionMunicipal){
				if(valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO() != null){
					totalUltimaCuotaMunicipal+= ((valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getMontoUltimaCuota() == null)?0L:valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getMontoUltimaCuota());
				}
			}
		}
		return totalUltimaCuotaMunicipal;
	}

	public void setTotalUltimaCuotaMunicipal(Long totalUltimaCuotaMunicipal) {
		this.totalUltimaCuotaMunicipal = totalUltimaCuotaMunicipal;
	}

	public Long getTotalMarcoFinalMunicipal() {
		totalMarcoFinalMunicipal = 0L;
		if(reliquidacionMunicipal != null && reliquidacionMunicipal.size() > 0){
			for(ValorizarReliquidacionPageSummaryVO valorizarReliquidacionPageSummaryVO : reliquidacionMunicipal){
				totalMarcoFinalMunicipal+= ((valorizarReliquidacionPageSummaryVO.getMarcoFinal() == null)?0L:valorizarReliquidacionPageSummaryVO.getMarcoFinal());
			}
		}
		return totalMarcoFinalMunicipal;
	}

	public void setTotalMarcoFinalMunicipal(Long totalMarcoFinalMunicipal) {
		this.totalMarcoFinalMunicipal = totalMarcoFinalMunicipal;
	}
	
}
