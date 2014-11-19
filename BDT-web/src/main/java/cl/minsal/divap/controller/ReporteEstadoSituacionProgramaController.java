package cl.minsal.divap.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import minsal.divap.enums.Subtitulo;
import minsal.divap.service.EstimacionFlujoCajaService;
import minsal.divap.service.ProgramasService;
import minsal.divap.service.ServicioSaludService;
import minsal.divap.vo.ComponenteSummaryVO;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ServiciosVO;
import minsal.divap.vo.SubtituloVO;
import cl.redhat.bandejaTareas.controller.BaseController;


@Named("reporteEstadoSituacionProgramaController")
@ViewScoped
public class ReporteEstadoSituacionProgramaController extends BaseController implements Serializable {


	private static final long serialVersionUID = -611536140782856412L;

	private Integer valorComboPrograma;
	private Integer valorComboServicio;
	private Integer valorComboComponente;
	private ProgramaVO programaSeleccionado;
	private List<ProgramaVO> programas;
	private List<ServiciosVO> servicios;
	private List<ComponenteSummaryVO> componentes;
	private Integer anoActual;
	private boolean mostrarSubtitulo21;
	private boolean mostrarSubtitulo22;
	private boolean mostrarSubtitulo24;
	private boolean mostrarSubtitulo29;

	@EJB
	private ProgramasService programaService;
	@EJB
	private EstimacionFlujoCajaService estimacionFlujoCajaService;
	@EJB
	private ServicioSaludService servicioSaludService;


	@PostConstruct
	public void init(){
		componentes = new ArrayList<ComponenteSummaryVO>();
	}

	public void cargarComponentes() {
		if(valorComboPrograma != null){
			if(valorComboPrograma.intValue() != 0){
				programaSeleccionado = programaService.getProgramaAno(valorComboPrograma);
				mostrarSubtitulo21 = false;
				mostrarSubtitulo22 = false;
				mostrarSubtitulo24 = false;
				mostrarSubtitulo29 = false;
				for(ComponentesVO componentesVO : programaSeleccionado.getComponentes()){
					List<SubtituloVO> subtitulos = componentesVO.getSubtitulos();
					for(SubtituloVO subtituloVO : componentesVO.getSubtitulos()){
						if(subtituloVO.getId() == 1){
							mostrarSubtitulo21 = true;
						}
						else if(subtituloVO.getId() == 2){
							mostrarSubtitulo22 = true;
						}
						else if(subtituloVO.getId() == 3){
							mostrarSubtitulo24 = true;
						}
						else if(subtituloVO.getId() == 4){
							mostrarSubtitulo29 = true;
						}
					}
				}
			}else{
				componentes = new ArrayList<ComponenteSummaryVO>();
				//consulta cambia
			}
		}
	}

	public void cargarTabla(){
		//cargar datos tabla con el programaano id + componente id

	}
	
	public void cargarDatosServicio(){
		
	}

	public List<ProgramaVO> getProgramas() {
		if(programas == null){
			programas = programaService.getProgramasByUserAno(getLoggedUsername(), getAnoActual());
		}
		return programas;
	}
	public void setProgramas(List<ProgramaVO> programas) {
		this.programas = programas;
	}
	public List<ServiciosVO> getServicios() {
		if(servicios == null){
			servicios = servicioSaludService.getServiciosOrderId();
		}
		return servicios;
	}
	public void setServicios(List<ServiciosVO> servicios) {
		this.servicios = servicios;
	}
	public List<ComponenteSummaryVO> getComponentes(Integer idProgramaAno) {
		return componentes;
	}
	public void setComponentes(List<ComponenteSummaryVO> componentes) {
		this.componentes = componentes;
	}

	public Integer getValorComboPrograma() {
		return valorComboPrograma;
	}
	public void setValorComboPrograma(Integer valorComboPrograma) {
		this.valorComboPrograma = valorComboPrograma;
	}

	public Integer getValorComboServicio() {
		return valorComboServicio;
	}
	public void setValorComboServicio(Integer valorComboServicio) {
		this.valorComboServicio = valorComboServicio;
	}
	public Integer getValorComboComponente() {
		return valorComboComponente;
	}
	public void setValorComboComponente(Integer valorComboComponente) {
		this.valorComboComponente = valorComboComponente;
	}
	public Integer getAnoActual() {
		if(anoActual == null){
			anoActual = estimacionFlujoCajaService.getAnoCurso();
		}
		return anoActual;
	}
	public void setAnoActual(Integer anoActual) {
		this.anoActual = anoActual;
	}



}
