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
import minsal.divap.service.ComponenteService;
import minsal.divap.service.ProgramasService;
import minsal.divap.service.ReliquidacionService;
import minsal.divap.service.ServicioSaludService;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ProgramaComponentesCuotasSummaryVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ServiciosVO;
import minsal.divap.vo.SubtituloVO;
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
	@EJB
	private ComponenteService componenteService;
	
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
	private List<ValorizarReliquidacionPageSummaryVO> reliquidacionServicio21;
	private List<ValorizarReliquidacionPageSummaryVO> reliquidacionServicio22;
	private List<ValorizarReliquidacionPageSummaryVO> reliquidacionServicio29;
	
	private Boolean mostrarMunicipal = false;
	private Boolean mostrarServicio = false;
	private Boolean mostrarSub21 = false;
	private Boolean mostrarSub22 = false;
	private Boolean mostrarSub29 = false;
	private Long totalMarcoFinal21 = 0L;
	private Long totalMarcoFinal22 = 0L;
	private Long totalMarcoFinal29 = 0L;
	private Long totalRebajaUltimaCouta21 = 0L;
	private Long totalRebajaUltimaCouta22 = 0L;
	private Long totalRebajaUltimaCouta29 = 0L;
	private Long totalUltimaCouta21 = 0L;
	private Long totalUltimaCouta22 = 0L;
	private Long totalUltimaCouta29 = 0L;
	private Long totalMontoConvenios21 = 0L;
	private Long totalMontoConvenios22 = 0L;
	private Long totalMontoConvenios29 = 0L;
	private List<Long> totalMontosCuotas21;
	private List<Long> totalMontosCuotas22;
	private List<Long> totalMontosCuotas29;
	
	private Long totalMontoConveniosMunicipal = 0L;
	private List<Long> totalMontosCuotasMunicipal;
	private Long totalRebajaUltimaCuotaMunicipal = 0L;
	private Long totalUltimaCuotaMunicipal = 0L;
	private Long totalMarcoFinalMunicipal = 0L;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		if(sessionExpired()){
			return;
		}
		this.servicioSeleccionado = "0";
		this.componenteSeleccionado = "0";
		mostrarSub21 = false;
		mostrarSub22 = false;
		mostrarSub29 = false;
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
					this.reliquidacionServicio21 = new ArrayList<ValorizarReliquidacionPageSummaryVO>();
					this.reliquidacionServicio22 = new ArrayList<ValorizarReliquidacionPageSummaryVO>();
					this.reliquidacionServicio29 = new ArrayList<ValorizarReliquidacionPageSummaryVO>();
					this.componenteSeleccionado =  componentes.get(0).getId().toString();
					for(SubtituloVO subtituloVO : componentes.get(0).getSubtitulos()){
						if(Subtitulo.SUBTITULO21.getId().equals(subtituloVO.getId())){
							mostrarSub21 = true;
							this.reliquidacionServicio21 = reliquidacionService.getReliquidacionSummaryVO(getPrograma().getIdProgramaAno(), Integer.parseInt(this.componenteSeleccionado), Subtitulo.SUBTITULO21.getId(), null, getIdReliquidacion());
						}else if(Subtitulo.SUBTITULO22.getId().equals(subtituloVO.getId())){
							mostrarSub22 = true;
							this.reliquidacionServicio22 = reliquidacionService.getReliquidacionSummaryVO(getPrograma().getIdProgramaAno(), Integer.parseInt(this.componenteSeleccionado), Subtitulo.SUBTITULO22.getId(), null, getIdReliquidacion());
						}else if(Subtitulo.SUBTITULO29.getId().equals(subtituloVO.getId())){
							mostrarSub29 = true;
							this.reliquidacionServicio29 = reliquidacionService.getReliquidacionSummaryVO(getPrograma().getIdProgramaAno(), Integer.parseInt(this.componenteSeleccionado), Subtitulo.SUBTITULO29.getId(), null, getIdReliquidacion());
						}
					}
					this.programaComponentesCuotas = reliquidacionService.getProgramaComponenteCuotas(getPrograma().getIdProgramaAno(), Integer.parseInt(this.componenteSeleccionado));
					this.reliquidacionMunicipal = reliquidacionService.getReliquidacionMunicipalSummaryVO(getPrograma().getIdProgramaAno(), Integer.parseInt(this.componenteSeleccionado), null, getIdReliquidacion());
				}else{
					mostrarMunicipal = false;
					mostrarServicio = true;
					this.reliquidacionMunicipal = new ArrayList<ValorizarReliquidacionPageSummaryVO>();
					this.reliquidacionServicio21 = new ArrayList<ValorizarReliquidacionPageSummaryVO>();
					this.reliquidacionServicio22 = new ArrayList<ValorizarReliquidacionPageSummaryVO>();
					this.reliquidacionServicio29 = new ArrayList<ValorizarReliquidacionPageSummaryVO>();
					Subtitulo[] subtitulosServicios = {Subtitulo.SUBTITULO21, Subtitulo.SUBTITULO22, Subtitulo.SUBTITULO29};
					componentes = programaService.getComponenteByProgramaSubtitulos(getPrograma().getId(),  subtitulosServicios);
					this.componenteSeleccionado =  componentes.get(0).getId().toString();
					for(SubtituloVO subtituloVO : componentes.get(0).getSubtitulos()){
						if(Subtitulo.SUBTITULO21.getId().equals(subtituloVO.getId())){
							mostrarSub21 = true;
							this.reliquidacionServicio21 = reliquidacionService.getReliquidacionSummaryVO(getPrograma().getIdProgramaAno(), Integer.parseInt(this.componenteSeleccionado), Subtitulo.SUBTITULO21.getId(), null, getIdReliquidacion());
						}else if(Subtitulo.SUBTITULO22.getId().equals(subtituloVO.getId())){
							mostrarSub22 = true;
							this.reliquidacionServicio22 = reliquidacionService.getReliquidacionSummaryVO(getPrograma().getIdProgramaAno(), Integer.parseInt(this.componenteSeleccionado), Subtitulo.SUBTITULO22.getId(), null, getIdReliquidacion());
						}else if(Subtitulo.SUBTITULO29.getId().equals(subtituloVO.getId())){
							mostrarSub29 = true;
							this.reliquidacionServicio29 = reliquidacionService.getReliquidacionSummaryVO(getPrograma().getIdProgramaAno(), Integer.parseInt(this.componenteSeleccionado), Subtitulo.SUBTITULO29.getId(), null, getIdReliquidacion());
						}
					}
					this.programaComponentesCuotas = reliquidacionService.getProgramaComponenteCuotas(getPrograma().getIdProgramaAno(), Integer.parseInt(this.componenteSeleccionado));
				}
			}
			System.out.println("this.docBaseMunicipal --> " + this.docBaseMunicipal);
			System.out.println("this.docBaseServicio --> " + this.docBaseServicio);
			System.out.println("this.idDocReliquidacionMunicipal --> " + this.idDocReliquidacionMunicipal);
			System.out.println("this.idDocReliquidacionServicio --> " + this.idDocReliquidacionServicio);
			System.out.println("this.idReliquidacion --> " + this.idReliquidacion);
			System.out.println("reliquidacionMunicipal.size() --> "  +((this.reliquidacionMunicipal==null)?"0":this.reliquidacionMunicipal.size()));
			System.out.println("reliquidacionServicio21.size() --> " +((this.reliquidacionServicio21==null)?"0":this.reliquidacionServicio21.size()));
			System.out.println("reliquidacionServicio22.size() --> " +((this.reliquidacionServicio22==null)?"0":this.reliquidacionServicio22.size()));
			System.out.println("reliquidacionServicio29.size() --> " +((this.reliquidacionServicio29==null)?"0":this.reliquidacionServicio29.size()));
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
		mostrarSub21 = false;
		mostrarSub22 = false;
		mostrarSub29 = false;
		if(this.mostrarMunicipal && this.mostrarServicio){
			if(getActiveTab().equals(0)){
				this.reliquidacionServicio21 = new ArrayList<ValorizarReliquidacionPageSummaryVO>();
				this.reliquidacionServicio22 = new ArrayList<ValorizarReliquidacionPageSummaryVO>();
				this.reliquidacionServicio29 = new ArrayList<ValorizarReliquidacionPageSummaryVO>();
				Subtitulo[] subtitulos =  {Subtitulo.SUBTITULO24};
				componentes = programaService.getComponenteByProgramaSubtitulos(getPrograma().getId(),  subtitulos);
				this.componenteSeleccionado =  componentes.get(0).getId().toString();
				this.reliquidacionMunicipal = reliquidacionService.getReliquidacionMunicipalSummaryVO(getPrograma().getIdProgramaAno(), Integer.parseInt(this.componenteSeleccionado), null, getIdReliquidacion());
			}else{
				this.reliquidacionServicio21 = new ArrayList<ValorizarReliquidacionPageSummaryVO>();
				this.reliquidacionServicio22 = new ArrayList<ValorizarReliquidacionPageSummaryVO>();
				this.reliquidacionServicio29 = new ArrayList<ValorizarReliquidacionPageSummaryVO>();
				this.reliquidacionMunicipal = new ArrayList<ValorizarReliquidacionPageSummaryVO>();
				Subtitulo[] subtitulos =  {Subtitulo.SUBTITULO21, Subtitulo.SUBTITULO22, Subtitulo.SUBTITULO29};
				componentes = programaService.getComponenteByProgramaSubtitulos(getPrograma().getId(),  subtitulos);
				this.componenteSeleccionado =  componentes.get(0).getId().toString();
				for(SubtituloVO subtituloVO : componentes.get(0).getSubtitulos()){
					if(Subtitulo.SUBTITULO21.getId().equals(subtituloVO.getId())){
						mostrarSub21 = true;
						this.reliquidacionServicio21 = reliquidacionService.getReliquidacionSummaryVO(getPrograma().getIdProgramaAno(), Integer.parseInt(this.componenteSeleccionado), Subtitulo.SUBTITULO21.getId(), null, getIdReliquidacion());
					}else if(Subtitulo.SUBTITULO22.getId().equals(subtituloVO.getId())){
						mostrarSub22 = true;
						this.reliquidacionServicio22 = reliquidacionService.getReliquidacionSummaryVO(getPrograma().getIdProgramaAno(), Integer.parseInt(this.componenteSeleccionado), Subtitulo.SUBTITULO22.getId(), null, getIdReliquidacion());
					}else if(Subtitulo.SUBTITULO29.getId().equals(subtituloVO.getId())){
						mostrarSub29 = true;
						this.reliquidacionServicio29 = reliquidacionService.getReliquidacionSummaryVO(getPrograma().getIdProgramaAno(), Integer.parseInt(this.componenteSeleccionado), Subtitulo.SUBTITULO29.getId(), null, getIdReliquidacion());
					}
				}
			}
		}else{
			this.reliquidacionServicio21 = new ArrayList<ValorizarReliquidacionPageSummaryVO>();
			this.reliquidacionServicio22 = new ArrayList<ValorizarReliquidacionPageSummaryVO>();
			this.reliquidacionServicio29 = new ArrayList<ValorizarReliquidacionPageSummaryVO>();
			this.reliquidacionMunicipal = new ArrayList<ValorizarReliquidacionPageSummaryVO>();
			Subtitulo[] subtitulos =  {Subtitulo.SUBTITULO21, Subtitulo.SUBTITULO22, Subtitulo.SUBTITULO29};
			componentes = programaService.getComponenteByProgramaSubtitulos(getPrograma().getId(),  subtitulos);
			this.componenteSeleccionado =  componentes.get(0).getId().toString();
			for(SubtituloVO subtituloVO : componentes.get(0).getSubtitulos()){
				if(Subtitulo.SUBTITULO21.getId().equals(subtituloVO.getId())){
					mostrarSub21 = true;
					this.reliquidacionServicio21 = reliquidacionService.getReliquidacionSummaryVO(getPrograma().getIdProgramaAno(), Integer.parseInt(this.componenteSeleccionado), Subtitulo.SUBTITULO21.getId(), null, getIdReliquidacion());
				}else if(Subtitulo.SUBTITULO22.getId().equals(subtituloVO.getId())){
					mostrarSub22 = true;
					this.reliquidacionServicio22 = reliquidacionService.getReliquidacionSummaryVO(getPrograma().getIdProgramaAno(), Integer.parseInt(this.componenteSeleccionado), Subtitulo.SUBTITULO22.getId(), null, getIdReliquidacion());
				}else if(Subtitulo.SUBTITULO29.getId().equals(subtituloVO.getId())){
					mostrarSub29 = true;
					this.reliquidacionServicio29 = reliquidacionService.getReliquidacionSummaryVO(getPrograma().getIdProgramaAno(), Integer.parseInt(this.componenteSeleccionado), Subtitulo.SUBTITULO29.getId(), null, getIdReliquidacion());
				}
			}
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
					this.reliquidacionServicio21 = new ArrayList<ValorizarReliquidacionPageSummaryVO>(); 
					this.reliquidacionServicio22 = new ArrayList<ValorizarReliquidacionPageSummaryVO>();
					this.reliquidacionServicio29 = new ArrayList<ValorizarReliquidacionPageSummaryVO>();
					this.reliquidacionMunicipal = reliquidacionService.getReliquidacionMunicipalSummaryVO(getPrograma().getIdProgramaAno(), idComponente, idServicio, getIdReliquidacion()); 
				}else{
					this.reliquidacionMunicipal = new ArrayList<ValorizarReliquidacionPageSummaryVO>(); 
					this.reliquidacionServicio21 = new ArrayList<ValorizarReliquidacionPageSummaryVO>(); 
					this.reliquidacionServicio22 = new ArrayList<ValorizarReliquidacionPageSummaryVO>();
					this.reliquidacionServicio29 = new ArrayList<ValorizarReliquidacionPageSummaryVO>();
					ComponentesVO componente = componenteService.getComponenteById(idComponente);
					for(SubtituloVO subtituloVO : componente.getSubtitulos()){
						if(Subtitulo.SUBTITULO21.getId().equals(subtituloVO.getId())){
							mostrarSub21 = true;
							this.reliquidacionServicio21 = reliquidacionService.getReliquidacionSummaryVO(getPrograma().getIdProgramaAno(), idComponente, Subtitulo.SUBTITULO21.getId(), null, getIdReliquidacion());
						}else if(Subtitulo.SUBTITULO22.getId().equals(subtituloVO.getId())){
							mostrarSub22 = true;
							this.reliquidacionServicio22 = reliquidacionService.getReliquidacionSummaryVO(getPrograma().getIdProgramaAno(), idComponente, Subtitulo.SUBTITULO22.getId(), null, getIdReliquidacion());
						}else if(Subtitulo.SUBTITULO29.getId().equals(subtituloVO.getId())){
							mostrarSub29 = true;
							this.reliquidacionServicio29 = reliquidacionService.getReliquidacionSummaryVO(getPrograma().getIdProgramaAno(), idComponente, Subtitulo.SUBTITULO29.getId(), null, getIdReliquidacion());
						}
					}
				}
			}else{
				this.reliquidacionServicio21 = new ArrayList<ValorizarReliquidacionPageSummaryVO>(); 
				this.reliquidacionServicio22 = new ArrayList<ValorizarReliquidacionPageSummaryVO>();
				this.reliquidacionServicio29 = new ArrayList<ValorizarReliquidacionPageSummaryVO>();
				this.reliquidacionMunicipal = new ArrayList<ValorizarReliquidacionPageSummaryVO>();
				Subtitulo[] subtitulos =  {Subtitulo.SUBTITULO21, Subtitulo.SUBTITULO22, Subtitulo.SUBTITULO29};
				componentes = programaService.getComponenteByProgramaSubtitulos(getPrograma().getId(),  subtitulos);
				ComponentesVO componente = componenteService.getComponenteById(idComponente);
				for(SubtituloVO subtituloVO : componente.getSubtitulos()){
					if(Subtitulo.SUBTITULO21.getId().equals(subtituloVO.getId())){
						mostrarSub21 = true;
						this.reliquidacionServicio21 = reliquidacionService.getReliquidacionSummaryVO(getPrograma().getIdProgramaAno(), idComponente, Subtitulo.SUBTITULO21.getId(), null, getIdReliquidacion());
					}else if(Subtitulo.SUBTITULO22.getId().equals(subtituloVO.getId())){
						mostrarSub22 = true;
						this.reliquidacionServicio22 = reliquidacionService.getReliquidacionSummaryVO(getPrograma().getIdProgramaAno(), idComponente, Subtitulo.SUBTITULO22.getId(), null, getIdReliquidacion());
					}else if(Subtitulo.SUBTITULO29.getId().equals(subtituloVO.getId())){
						mostrarSub29 = true;
						this.reliquidacionServicio29 = reliquidacionService.getReliquidacionSummaryVO(getPrograma().getIdProgramaAno(), idComponente, Subtitulo.SUBTITULO29.getId(), null, getIdReliquidacion());
					}
				}
			}
		}
		System.out.println("reliquidacionMunicipal.size() --> " +((this.reliquidacionMunicipal==null)?"0":this.reliquidacionMunicipal.size()));
		System.out.println("reliquidacionServicio21.size() --> " +((this.reliquidacionServicio21==null)?"0":this.reliquidacionServicio21.size()));
		System.out.println("reliquidacionServicio22.size() --> " +((this.reliquidacionServicio22==null)?"0":this.reliquidacionServicio22.size()));
		System.out.println("reliquidacionServicio29.size() --> " +((this.reliquidacionServicio29==null)?"0":this.reliquidacionServicio29.size()));
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
	
	public void setTotalMarcoFinal21(Long totalMarcoFinal) {
		this.totalMarcoFinal21 = totalMarcoFinal;
	}
	
	public Long getTotalMarcoFinal21() {
		totalMarcoFinal21 = 0L;
		if(reliquidacionServicio21 != null && reliquidacionServicio21.size() > 0){
			for(ValorizarReliquidacionPageSummaryVO valorizarReliquidacionPageSummaryVO : reliquidacionServicio21){
				totalMarcoFinal21 += ((valorizarReliquidacionPageSummaryVO.getMarcoFinal() == null) ? 0L : valorizarReliquidacionPageSummaryVO.getMarcoFinal());
			}
		}
		return totalMarcoFinal21;
	}
	
	public void setTotalMarcoFinal22(Long totalMarcoFinal) {
		this.totalMarcoFinal21 = totalMarcoFinal;
	}
	
	public Long getTotalMarcoFinal22() {
		totalMarcoFinal22 = 0L;
		if(reliquidacionServicio22 != null && reliquidacionServicio22.size() > 0){
			for(ValorizarReliquidacionPageSummaryVO valorizarReliquidacionPageSummaryVO : reliquidacionServicio22){
				totalMarcoFinal22 += ((valorizarReliquidacionPageSummaryVO.getMarcoFinal() == null) ? 0L : valorizarReliquidacionPageSummaryVO.getMarcoFinal());
			}
		}
		return totalMarcoFinal22;
	}

	public void setTotalMarcoFinal29(Long totalMarcoFinal) {
		this.totalMarcoFinal29 = totalMarcoFinal;
	}
	
	public Long getTotalMarcoFinal29() {
		totalMarcoFinal29 = 0L;
		if(reliquidacionServicio29 != null && reliquidacionServicio29.size() > 0){
			for(ValorizarReliquidacionPageSummaryVO valorizarReliquidacionPageSummaryVO : reliquidacionServicio29){
				totalMarcoFinal29 += ((valorizarReliquidacionPageSummaryVO.getMarcoFinal() == null) ? 0L : valorizarReliquidacionPageSummaryVO.getMarcoFinal());
			}
		}
		return totalMarcoFinal29;
	}

	public Long getTotalRebajaUltimaCouta21() {
		totalRebajaUltimaCouta21 = 0L;
		if(reliquidacionServicio21 != null && reliquidacionServicio21.size() > 0){
			for(ValorizarReliquidacionPageSummaryVO valorizarReliquidacionPageSummaryVO : reliquidacionServicio21){
				if(valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO() != null){
					totalRebajaUltimaCouta21 += ((valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getRebajaUltimaCuota() == null) ? 0L :valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getRebajaUltimaCuota());
				}
			}
		}
		return totalRebajaUltimaCouta21;
	}

	public void setTotalRebajaUltimaCouta21(Long totalRebajaUltimaCouta) {
		this.totalRebajaUltimaCouta21 = totalRebajaUltimaCouta;
	}
	
	public Long getTotalRebajaUltimaCouta22() {
		totalRebajaUltimaCouta22 = 0L;
		if(reliquidacionServicio22 != null && reliquidacionServicio22.size() > 0){
			for(ValorizarReliquidacionPageSummaryVO valorizarReliquidacionPageSummaryVO : reliquidacionServicio22){
				if(valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO() != null){
					totalRebajaUltimaCouta22 += ((valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getRebajaUltimaCuota() == null) ? 0L : valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getRebajaUltimaCuota());
				}
			}
		}
		return totalRebajaUltimaCouta22;
	}

	public void setTotalRebajaUltimaCouta22(Long totalRebajaUltimaCouta) {
		this.totalRebajaUltimaCouta22 = totalRebajaUltimaCouta;
	}
	
	public Long getTotalRebajaUltimaCouta29() {
		totalRebajaUltimaCouta29 = 0L;
		if(reliquidacionServicio29 != null && reliquidacionServicio29.size() > 0){
			for(ValorizarReliquidacionPageSummaryVO valorizarReliquidacionPageSummaryVO : reliquidacionServicio29){
				if(valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO() != null){
					totalRebajaUltimaCouta29 += ((valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getRebajaUltimaCuota() == null) ? 0L : valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getRebajaUltimaCuota());
				}
			}
		}
		return totalRebajaUltimaCouta29;
	}

	public void setTotalRebajaUltimaCouta29(Long totalRebajaUltimaCouta) {
		this.totalRebajaUltimaCouta29 = totalRebajaUltimaCouta;
	}

	public Long getTotalUltimaCouta21() {
		totalUltimaCouta21 = 0L;
		if(reliquidacionServicio21 != null && reliquidacionServicio21.size() > 0){
			for(ValorizarReliquidacionPageSummaryVO valorizarReliquidacionPageSummaryVO : reliquidacionServicio21){
				if(valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO() != null){
					totalUltimaCouta21 += ((valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getMontoUltimaCuota() == null) ? 0L : valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getMontoUltimaCuota());
				}
			}
		}
		return totalUltimaCouta21;
	}

	public void setTotalUltimaCouta21(Long totalUltimaCouta) {
		this.totalUltimaCouta21 = totalUltimaCouta;
	}
	
	public Long getTotalUltimaCouta22() {
		totalUltimaCouta22 = 0L;
		if(reliquidacionServicio22 != null && reliquidacionServicio22.size() > 0){
			for(ValorizarReliquidacionPageSummaryVO valorizarReliquidacionPageSummaryVO : reliquidacionServicio22){
				if(valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO() != null){
					totalUltimaCouta22 += ((valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getMontoUltimaCuota() == null) ? 0L : valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getMontoUltimaCuota());
				}
			}
		}
		return totalUltimaCouta22;
	}

	public void setTotalUltimaCouta22(Long totalUltimaCouta) {
		this.totalUltimaCouta22 = totalUltimaCouta;
	}
	
	public Long getTotalUltimaCouta29() {
		totalUltimaCouta29 = 0L;
		if(reliquidacionServicio29 != null && reliquidacionServicio29.size() > 0){
			for(ValorizarReliquidacionPageSummaryVO valorizarReliquidacionPageSummaryVO : reliquidacionServicio29){
				if(valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO() != null){
					totalUltimaCouta29 += ((valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getMontoUltimaCuota() == null) ? 0L : valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getMontoUltimaCuota());
				}
			}
		}
		return totalUltimaCouta29;
	}

	public void setTotalUltimaCouta29(Long totalUltimaCouta) {
		this.totalUltimaCouta29 = totalUltimaCouta;
	}

	public Long getTotalMontoConvenios21() {
		totalMontoConvenios21 = 0L;
		if(reliquidacionServicio21 != null && reliquidacionServicio21.size() > 0){
			for(ValorizarReliquidacionPageSummaryVO valorizarReliquidacionPageSummaryVO : reliquidacionServicio21){
				if(valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO() != null){
					totalMontoConvenios21 += ((valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getMarcoInicial() == null) ? 0L : valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getMarcoInicial());
				}
			}
		}
		return totalMontoConvenios21;
	}

	public void setTotalMontoConvenios21(Long totalMontoConvenios) {
		this.totalMontoConvenios21 = totalMontoConvenios;
	}
	
	public Long getTotalMontoConvenios22() {
		totalMontoConvenios22 = 0L;
		if(reliquidacionServicio22 != null && reliquidacionServicio22.size() > 0){
			for(ValorizarReliquidacionPageSummaryVO valorizarReliquidacionPageSummaryVO : reliquidacionServicio22){
				if(valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO() != null){
					totalMontoConvenios22 += ((valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getMarcoInicial() == null) ?0L : valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getMarcoInicial());
				}
			}
		}
		return totalMontoConvenios22;
	}

	public void setTotalMontoConvenios22(Long totalMontoConvenios) {
		this.totalMontoConvenios22 = totalMontoConvenios;
	}
	
	public Long getTotalMontoConvenios29() {
		totalMontoConvenios29 = 0L;
		if(reliquidacionServicio29 != null && reliquidacionServicio29.size() > 0){
			for(ValorizarReliquidacionPageSummaryVO valorizarReliquidacionPageSummaryVO : reliquidacionServicio29){
				if(valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO() != null){
					totalMontoConvenios29 += ((valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getMarcoInicial() == null) ?0L : valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getMarcoInicial());
				}
			}
		}
		return totalMontoConvenios29;
	}

	public void setTotalMontoConvenios29(Long totalMontoConvenios) {
		this.totalMontoConvenios29 = totalMontoConvenios;
	}

	public List<Long> getTotalMontosCuotas21() {
		totalMontosCuotas21 = new ArrayList<Long>();
		if(getProgramaComponentesCuotas() != null && getProgramaComponentesCuotas().getCuotas()!= null && getProgramaComponentesCuotas().getCuotas().size() > 0){
			for(int i = 0; i < getProgramaComponentesCuotas().getCuotas().size(); i++){
				totalMontosCuotas21.add(0L);
			}
		}
		if(reliquidacionServicio21 != null && reliquidacionServicio21.size() > 0){
			for(ValorizarReliquidacionPageSummaryVO valorizarReliquidacionPageSummaryVO : reliquidacionServicio21){
				if(valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO() != null){
					for(int cont = 0; cont < totalMontosCuotas21.size(); cont++){
						long monto = totalMontosCuotas21.get(cont);
						switch (cont) {
						case 0:
							monto += ((valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getCuotasUno() == null) ? 0L : valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getCuotasUno().getMonto());
							break;
						case 1:
							monto += ((valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getUltimaCuota() == null) ? 0L : valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getUltimaCuota().getMonto());
							break;
						default:
							break;
						}
						totalMontosCuotas21.set(cont, monto);
					}
				}
			}
		}
		return totalMontosCuotas21;
	}

	public void setTotalMontosCuotas21(List<Long> totalMontosCuotas) {
		this.totalMontosCuotas21 = totalMontosCuotas;
	}
	
	public List<Long> getTotalMontosCuotas22() {
		totalMontosCuotas22 = new ArrayList<Long>();
		if(getProgramaComponentesCuotas() != null && getProgramaComponentesCuotas().getCuotas()!= null && getProgramaComponentesCuotas().getCuotas().size() > 0){
			for(int i = 0; i < getProgramaComponentesCuotas().getCuotas().size(); i++){
				totalMontosCuotas22.add(0L);
			}
		}
		if(reliquidacionServicio22 != null && reliquidacionServicio22.size() > 0){
			for(ValorizarReliquidacionPageSummaryVO valorizarReliquidacionPageSummaryVO : reliquidacionServicio22){
				if(valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO() != null){
					for(int cont = 0; cont < totalMontosCuotas22.size(); cont++){
						long monto = totalMontosCuotas22.get(cont);
						switch (cont) {
						case 0:
							monto += ((valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getCuotasUno() == null) ? 0L : valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getCuotasUno().getMonto());
							break;
						case 1:
							monto += ((valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getUltimaCuota() == null) ? 0L : valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getUltimaCuota().getMonto());
							break;
						default:
							break;
						}
						totalMontosCuotas22.set(cont, monto);
					}
				}
			}
		}
		return totalMontosCuotas22;
	}

	public void setTotalMontosCuotas22(List<Long> totalMontosCuotas) {
		this.totalMontosCuotas22 = totalMontosCuotas;
	}
	
	public List<Long> getTotalMontosCuotas29() {
		totalMontosCuotas29 = new ArrayList<Long>();
		if(getProgramaComponentesCuotas() != null && getProgramaComponentesCuotas().getCuotas()!= null && getProgramaComponentesCuotas().getCuotas().size() > 0){
			for(int i = 0; i < getProgramaComponentesCuotas().getCuotas().size(); i++){
				totalMontosCuotas29.add(0L);
			}
		}
		if(reliquidacionServicio29 != null && reliquidacionServicio29.size() > 0){
			for(ValorizarReliquidacionPageSummaryVO valorizarReliquidacionPageSummaryVO : reliquidacionServicio29){
				if(valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO() != null){
					for(int cont = 0; cont < totalMontosCuotas29.size(); cont++){
						long monto = totalMontosCuotas29.get(cont);
						switch (cont) {
						case 0:
							monto += ((valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getCuotasUno() == null) ? 0L : valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getCuotasUno().getMonto());
							break;
						case 1:
							monto += ((valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getUltimaCuota() == null) ? 0L : valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getUltimaCuota().getMonto());
							break;
						default:
							break;
						}
						totalMontosCuotas29.set(cont, monto);
					}
				}
			}
		}
		return totalMontosCuotas29;
	}

	public void setTotalMontosCuotas29(List<Long> totalMontosCuotas) {
		this.totalMontosCuotas29 = totalMontosCuotas;
	}
	
	public Long getTotalMontoConveniosMunicipal() {
		totalMontoConveniosMunicipal = 0L;
		if(reliquidacionMunicipal != null && reliquidacionMunicipal.size() > 0){
			for(ValorizarReliquidacionPageSummaryVO valorizarReliquidacionPageSummaryVO : reliquidacionMunicipal){
				if(valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO() != null){
					totalMontoConveniosMunicipal+= ((valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getMarcoInicial() == null) ? 0L : valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getMarcoInicial());
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
							monto += ((valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getCuotasUno() == null) ? 0L : valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getCuotasUno().getMonto());
							break;
						case 1:
							monto += ((valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getUltimaCuota() == null) ? 0L : valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getUltimaCuota().getMonto());
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
					totalUltimaCuotaMunicipal+= ((valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getMontoUltimaCuota() == null) ? 0L : valorizarReliquidacionPageSummaryVO.getComponenteReliquidacionPageVO().getMontoUltimaCuota());
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
				totalMarcoFinalMunicipal+= ((valorizarReliquidacionPageSummaryVO.getMarcoFinal() == null) ? 0L : valorizarReliquidacionPageSummaryVO.getMarcoFinal());
			}
		}
		return totalMarcoFinalMunicipal;
	}

	public void setTotalMarcoFinalMunicipal(Long totalMarcoFinalMunicipal) {
		this.totalMarcoFinalMunicipal = totalMarcoFinalMunicipal;
	}

	public Boolean getMostrarSub21() {
		return mostrarSub21;
	}

	public void setMostrarSub21(Boolean mostrarSub21) {
		this.mostrarSub21 = mostrarSub21;
	}

	public Boolean getMostrarSub22() {
		return mostrarSub22;
	}

	public void setMostrarSub22(Boolean mostrarSub22) {
		this.mostrarSub22 = mostrarSub22;
	}

	public Boolean getMostrarSub29() {
		return mostrarSub29;
	}

	public void setMostrarSub29(Boolean mostrarSub29) {
		this.mostrarSub29 = mostrarSub29;
	}

	public List<ValorizarReliquidacionPageSummaryVO> getReliquidacionServicio21() {
		return reliquidacionServicio21;
	}

	public void setReliquidacionServicio21(
			List<ValorizarReliquidacionPageSummaryVO> reliquidacionServicio21) {
		this.reliquidacionServicio21 = reliquidacionServicio21;
	}

	public List<ValorizarReliquidacionPageSummaryVO> getReliquidacionServicio22() {
		return reliquidacionServicio22;
	}

	public void setReliquidacionServicio22(
			List<ValorizarReliquidacionPageSummaryVO> reliquidacionServicio22) {
		this.reliquidacionServicio22 = reliquidacionServicio22;
	}

	public List<ValorizarReliquidacionPageSummaryVO> getReliquidacionServicio29() {
		return reliquidacionServicio29;
	}

	public void setReliquidacionServicio29(
			List<ValorizarReliquidacionPageSummaryVO> reliquidacionServicio29) {
		this.reliquidacionServicio29 = reliquidacionServicio29;
	}
	
}
