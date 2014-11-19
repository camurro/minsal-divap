package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import minsal.divap.enums.Subtitulo;
import minsal.divap.service.ComponenteService;
import minsal.divap.service.OTService;
import minsal.divap.service.ProgramasService;
import minsal.divap.service.RecursosFinancierosProgramasReforzamientoService;
import minsal.divap.service.UtilitariosService;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.OTResumenDependienteServicioVO;
import minsal.divap.vo.OTResumenMunicipalVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ServiciosVO;
import minsal.divap.vo.SubtituloVO;

import org.apache.log4j.Logger;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named("procesoOTRevisarAntecedentesController")
@ViewScoped
public class ProcesoOTRevisarAntecedentesController extends AbstractTaskMBean
implements Serializable {
	private static final long serialVersionUID = 8979055329731411696L;
	@Inject private transient Logger log;
	
	@EJB
	private OTService otService;
	@EJB
	private ProgramasService programasService;
	@EJB
	private RecursosFinancierosProgramasReforzamientoService recursosFinancierosProgramasReforzamientoService;
	@EJB
	private UtilitariosService utilitariosService;
	@EJB
	private ComponenteService componenteService;

	
	private Integer programaSeleccionado;
	private Integer IdProgramaProxAno;
	private ProgramaVO programa;
	
	private Integer servicioSeleccionado;
	private List<ServiciosVO> listaServicios;
	
	private Integer componenteSeleccionado;
	private List<ComponentesVO> listaComponentes;
	
	private List<OTResumenDependienteServicioVO> resultadoServicioSub21;
	private List<OTResumenDependienteServicioVO> resultadoServicioSub22;
	private List<OTResumenDependienteServicioVO> resultadoServicioSub29;
	private List<OTResumenMunicipalVO> resultadoMunicipal;
	
	private boolean subtitulo21;
	private boolean subtitulo22;
	private boolean subtitulo29;
	private boolean subtitulo24;
	
	
	@PostConstruct
	public void init() {
		
		if (!getSessionBean().isLogged()) {
			log.warn("No hay usuario almacenado en sesion, se redirecciona a pantalla de login");
			try {
				facesContext.getExternalContext().redirect("login.jsf");
			} catch (IOException e) {
				log.error("Error tratando de redireccionar a login por falta de usuario en sesion.", e);
			}
		}
		if (getTaskDataVO() != null && getTaskDataVO().getData() != null) {
			programaSeleccionado = (Integer) getTaskDataVO()
					.getData().get("_programaSeleccionado");
			programa = otService.getProgramaById(programaSeleccionado);
			programa = recursosFinancierosProgramasReforzamientoService.getProgramaById(programaSeleccionado);
			IdProgramaProxAno = programasService.evaluarAnoSiguiente(programaSeleccionado,programa);
		}
		
		listaServicios = utilitariosService.getAllServicios();
		listaComponentes= componenteService.getComponenteByPrograma(programa.getIdProgramaAno());
		
	}


	
	
	public void buscarResultados(){
		System.out.println("Buscar Resultados para Componente: "+componenteSeleccionado+" Servicio: "+servicioSeleccionado);
		ComponentesVO componenteVO = componenteService.getComponenteVOById(componenteSeleccionado);
		subtitulo21=false;
		subtitulo22=false;
		subtitulo29=false;
		subtitulo24=false;
		for(SubtituloVO subs : componenteVO.getSubtitulos()){
			System.out.println(subs.getId());
			if(subs.getId() == Subtitulo.SUBTITULO21.getId()){
				subtitulo21=true;
				resultadoServicioSub21 = otService.getDetalleOTServicio(componenteSeleccionado,servicioSeleccionado, 
						Subtitulo.SUBTITULO21.getId(),programa.getIdProgramaAno());
			}
			if(subs.getId() == Subtitulo.SUBTITULO22.getId()){
				subtitulo22=true;
				resultadoServicioSub22 = otService.getDetalleOTServicio(componenteSeleccionado,servicioSeleccionado, 
						Subtitulo.SUBTITULO22.getId(),programa.getIdProgramaAno());
			}
			if(subs.getId() == Subtitulo.SUBTITULO29.getId()){
				subtitulo29=true;
				resultadoServicioSub29 = otService.getDetalleOTServicio(componenteSeleccionado,servicioSeleccionado, 
						Subtitulo.SUBTITULO29.getId(),programa.getIdProgramaAno());
			}
			if(subs.getId() == Subtitulo.SUBTITULO24.getId()){
				subtitulo24=true;
				resultadoMunicipal = otService.getDetalleOTMunicipal(servicioSeleccionado, programa.getIdProgramaAno());
			}
		}
		
		
	}
	
	@Override
	protected Map<String, Object> createResultData() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String iniciarProceso() {
		// TODO Auto-generated method stub
		return null;
	}


	public Integer getProgramaSeleccionado() {
		return programaSeleccionado;
	}


	public void setProgramaSeleccionado(Integer programaSeleccionado) {
		this.programaSeleccionado = programaSeleccionado;
	}


	public Integer getIdProgramaProxAno() {
		return IdProgramaProxAno;
	}


	public void setIdProgramaProxAno(Integer idProgramaProxAno) {
		IdProgramaProxAno = idProgramaProxAno;
	}


	public ProgramaVO getPrograma() {
		return programa;
	}


	public void setPrograma(ProgramaVO programa) {
		this.programa = programa;
	}


	


	public List<ServiciosVO> getListaServicios() {
		return listaServicios;
	}


	public void setListaServicios(List<ServiciosVO> listaServicios) {
		this.listaServicios = listaServicios;
	}


	public List<ComponentesVO> getListaComponentes() {
		return listaComponentes;
	}


	public void setListaComponentes(List<ComponentesVO> listaComponentes) {
		this.listaComponentes = listaComponentes;
	}


	public boolean isSubtitulo21() {
		return subtitulo21;
	}


	public void setSubtitulo21(boolean subtitulo21) {
		this.subtitulo21 = subtitulo21;
	}


	public boolean isSubtitulo22() {
		return subtitulo22;
	}


	public void setSubtitulo22(boolean subtitulo22) {
		this.subtitulo22 = subtitulo22;
	}


	public boolean isSubtitulo29() {
		return subtitulo29;
	}


	public void setSubtitulo29(boolean subtitulo29) {
		this.subtitulo29 = subtitulo29;
	}


	public boolean isSubtitulo24() {
		return subtitulo24;
	}


	public void setSubtitulo24(boolean subtitulo24) {
		this.subtitulo24 = subtitulo24;
	}

	public List<OTResumenDependienteServicioVO> getResultadoServicioSub21() {
		return resultadoServicioSub21;
	}


	public void setResultadoServicioSub21(
			List<OTResumenDependienteServicioVO> resultadoServicioSub21) {
		this.resultadoServicioSub21 = resultadoServicioSub21;
	}


	public List<OTResumenDependienteServicioVO> getResultadoServicioSub22() {
		return resultadoServicioSub22;
	}


	public void setResultadoServicioSub22(
			List<OTResumenDependienteServicioVO> resultadoServicioSub22) {
		this.resultadoServicioSub22 = resultadoServicioSub22;
	}


	public List<OTResumenDependienteServicioVO> getResultadoServicioSub29() {
		return resultadoServicioSub29;
	}


	public void setResultadoServicioSub29(
			List<OTResumenDependienteServicioVO> resultadoServicioSub29) {
		this.resultadoServicioSub29 = resultadoServicioSub29;
	}


	public List<OTResumenMunicipalVO> getResultadoMunicipal() {
		return resultadoMunicipal;
	}


	public void setResultadoMunicipal(List<OTResumenMunicipalVO> resultadoMunicipal) {
		this.resultadoMunicipal = resultadoMunicipal;
	}




	public Integer getServicioSeleccionado() {
		return servicioSeleccionado;
	}




	public void setServicioSeleccionado(Integer servicioSeleccionado) {
		this.servicioSeleccionado = servicioSeleccionado;
	}




	public Integer getComponenteSeleccionado() {
		return componenteSeleccionado;
	}




	public void setComponenteSeleccionado(Integer componenteSeleccionado) {
		this.componenteSeleccionado = componenteSeleccionado;
	}
	
	

	

}
