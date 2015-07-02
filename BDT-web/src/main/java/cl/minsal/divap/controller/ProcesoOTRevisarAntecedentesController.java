package cl.minsal.divap.controller;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
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

import minsal.divap.enums.Programas;
import minsal.divap.enums.Subtitulo;
import minsal.divap.service.ComponenteService;
import minsal.divap.service.OTService;
import minsal.divap.service.ProgramasService;
import minsal.divap.service.RecursosFinancierosProgramasReforzamientoService;
import minsal.divap.service.ReportesServices;
import minsal.divap.service.UtilitariosService;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.DiaVO;
import minsal.divap.vo.OTPerCapitaVO;
import minsal.divap.vo.OTResumenDependienteServicioVO;
import minsal.divap.vo.OTResumenMunicipalVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.RemesasProgramaVO;
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
	@EJB
	private ReportesServices reporteService;


	private Integer programaSeleccionado;
	private Integer idProgramaProxAno;
	private ProgramaVO programa;
	private Integer idProceso;
	
	private Integer servicioSeleccionado;
	private List<ServiciosVO> listaServicios;

	private Integer componenteSeleccionado;
	private List<ComponentesVO> listaComponentes;

	private List<OTResumenDependienteServicioVO> resultadoServicioSub21;
	private List<OTResumenDependienteServicioVO> resultadoServicioSub22;
	private List<OTResumenDependienteServicioVO> resultadoServicioSub29;
	private List<OTResumenMunicipalVO> resultadoMunicipal;
	private List<OTPerCapitaVO> resultadoPercapita;

	private List<RemesasProgramaVO> remesasPrograma;
	private List<RemesasProgramaVO> remesasPerCapita;

	private boolean subtitulo21;
	private boolean subtitulo22;
	private boolean subtitulo29;
	private boolean subtitulo24;
	private boolean percapita;
	private boolean desemenoDificil;
	private boolean rebajaIAAPS;


	private String filaHidden;
	private String diaHidden;
	private String mesHidden;
	private String montoHidden;
	private String subtituloHidden;
	private Integer anoCurso;


	@PostConstruct
	public void init() throws NumberFormatException, ParseException {

		if (!getSessionBean().isLogged()) {
			log.warn("No hay usuario almacenado en sesion, se redirecciona a pantalla de login");
			try {
				facesContext.getExternalContext().redirect("login.jsf");
			} catch (IOException e) {
				log.error("Error tratando de redireccionar a login por falta de usuario en sesion.", e);
			}
		}
		if (getTaskDataVO() != null && getTaskDataVO().getData() != null) {
			programaSeleccionado = (Integer) getTaskDataVO().getData().get("_programaSeleccionado");
			anoCurso = (Integer) getTaskDataVO().getData().get("_ano");
			idProceso = (Integer) getTaskDataVO().getData().get("_idProceso");
			if(programaSeleccionado > 0){
				programa = programasService.getProgramaByIdProgramaAndAno(programaSeleccionado, (anoCurso - 1));
				idProgramaProxAno = programasService.evaluarAnoSiguiente(programaSeleccionado, anoCurso);
			}else{
				programa = programasService.getProgramaByIdProgramaAndAno(programaSeleccionado, anoCurso);
				idProgramaProxAno = programa.getIdProgramaAno();
			}
		}
		percapita = false;
		desemenoDificil = false;
		if(programa.getId() < 0){
			if(Programas.PERCAPITA.getId().equals(programa.getId())){
				percapita = true;
			}else if(Programas.DESEMPENODIFICIAL.getId().equals(programa.getId())){
				desemenoDificil = true;
			}else if(Programas.REBAJAIAAPS.getId().equals(programa.getId())){
				rebajaIAAPS = true;
			}
			remesasPerCapita = otService.getRemesasPerCapita(programa.getId(), Integer.parseInt(otService.getMesCurso(true)), anoCurso);
		}else{
			remesasPrograma = otService.getRemesasPrograma(programa.getId(), Integer.parseInt(otService.getMesCurso(true)), anoCurso);
		}

		listaServicios = utilitariosService.getAllServicios();
		listaComponentes = componenteService.getComponentesByProgramaAno(programa.getIdProgramaAno());
		if(listaComponentes != null && listaComponentes.size() == 1){
			componenteSeleccionado = listaComponentes.get(0).getId();
		}
	}

	public void buscarResultados(){
		if(programa.getId()< 0){
			Integer idServicio = ((servicioSeleccionado == null || (servicioSeleccionado.intValue() == -1)) ? null : servicioSeleccionado);
			if(Programas.PERCAPITA.getId().equals(programa.getId())){
				resultadoPercapita = otService.getDetallePerCapita(idProceso, idServicio, anoCurso, idProgramaProxAno); 
				System.out.println("Resultados PerCapita: "+resultadoPercapita.size());
			}else if(Programas.DESEMPENODIFICIAL.getId().equals(programa.getId())){
				resultadoPercapita = otService.getDetalleDesempenoDificil(idProceso, idServicio, anoCurso, idProgramaProxAno); 
				System.out.println("Resultados PerCapita: "+resultadoPercapita.size());
			}
			else if(Programas.REBAJAIAAPS.getId().equals(programa.getId())){
				resultadoPercapita = otService.getDetalleRebajaIAAPS(idProceso, idServicio, anoCurso, idProgramaProxAno); 
				System.out.println("Resultados PerCapita: "+resultadoPercapita.size());
			}
		}else{
			if(componenteSeleccionado != null && (componenteSeleccionado.intValue() != -1)){
				System.out.println("Buscar Resultados para Componente: "+componenteSeleccionado+" Servicio: "+servicioSeleccionado);
				Integer idServicio = ((servicioSeleccionado == null || (servicioSeleccionado.intValue() == -1)) ? null : servicioSeleccionado);
				ComponentesVO componenteVO = componenteService.getComponenteVOById(componenteSeleccionado);
				subtitulo21 = false;
				subtitulo22 = false;
				subtitulo29 = false;
				subtitulo24 = false;
				System.out.println("componenteSeleccionado="+componenteSeleccionado+" IdProgramaProxAno="+idProgramaProxAno);
				for(SubtituloVO subs : componenteVO.getSubtitulos()){
					System.out.println(subs.getId());
					if(Subtitulo.SUBTITULO21.getId().equals(subs.getId())){
						subtitulo21 = true;
						resultadoServicioSub21 = otService.getDetalleOTServicio(componenteSeleccionado, idServicio, Subtitulo.SUBTITULO21.getId(), idProgramaProxAno);
					}
					if(Subtitulo.SUBTITULO22.getId().equals(subs.getId())){
						subtitulo22 = true;
						resultadoServicioSub22 = otService.getDetalleOTServicio(componenteSeleccionado, idServicio, Subtitulo.SUBTITULO22.getId(), idProgramaProxAno);
					}
					if(Subtitulo.SUBTITULO29.getId().equals(subs.getId())){
						subtitulo29 = true;
						resultadoServicioSub29 = otService.getDetalleOTServicio(componenteSeleccionado, idServicio, Subtitulo.SUBTITULO29.getId(), idProgramaProxAno);
					}
					if(Subtitulo.SUBTITULO24.getId().equals(subs.getId())){
						subtitulo24 = true;
						resultadoMunicipal = otService.getDetalleOTMunicipal(componenteSeleccionado, idServicio, idProgramaProxAno);
					}
				}
			}else{
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe seleccionar el componente antes de realizar la búsqueda", null);
				FacesContext.getCurrentInstance().addMessage(null, msg);
				resultadoMunicipal = new ArrayList<OTResumenMunicipalVO>();
				resultadoServicioSub21 = new ArrayList<OTResumenDependienteServicioVO>();
				resultadoServicioSub22 = new ArrayList<OTResumenDependienteServicioVO>();
				resultadoServicioSub29 = new ArrayList<OTResumenDependienteServicioVO>();
			}
		}
	}

	public void actualizarPerCapita(Integer row, Integer idComuna){
		System.out.println("actualizarPerCapita row "+row);
		System.out.println("idComuna "+idComuna);
		OTPerCapitaVO registroTabla = resultadoPercapita.get(row);
		OTPerCapitaVO registroActualizado = otService.actualizarComunaPerCapita(idProceso, idComuna, registroTabla, idProgramaProxAno, Subtitulo.SUBTITULO24.getId(), componenteSeleccionado);
		resultadoPercapita.remove(registroActualizado);
		System.out.println(resultadoPercapita.size());
	}
	
	public void actualizarDesempenoDificil(Integer row, Integer idComuna){
		System.out.println("actualizarDesempenoDificil row "+row);
		System.out.println("idComuna "+idComuna);
		OTPerCapitaVO registroTabla = resultadoPercapita.get(row);
		OTPerCapitaVO registroActualizado = otService.actualizarDesempenoDificil(idProceso, idComuna, registroTabla, idProgramaProxAno, Subtitulo.SUBTITULO24.getId(), componenteSeleccionado);
		resultadoPercapita.remove(registroActualizado);
		System.out.println(resultadoPercapita.size());
	}
	

	public void actualizarRebajaIAAPS(Integer row, Integer idComuna){
		System.out.println("actualizarRebajaIAAPS row "+row);
		System.out.println("idComuna "+idComuna);
		OTPerCapitaVO registroTabla = resultadoPercapita.get(row);
		OTPerCapitaVO registroActualizado = otService.actualizarRebajaIAAPS(idProceso, idComuna, registroTabla, idProgramaProxAno, Subtitulo.SUBTITULO24.getId(), componenteSeleccionado);
		resultadoPercapita.remove(registroActualizado);
		System.out.println(resultadoPercapita.size());
	}

	public void actualizarS21(Integer row, String codEstablecimiento){
		System.out.println("actualizando "+codEstablecimiento);
		OTResumenDependienteServicioVO registroTabla = resultadoServicioSub21.get(row);
		OTResumenDependienteServicioVO registroActualizado = otService.aprobarMontoRemesaProfesional(registroTabla, idProgramaProxAno, Subtitulo.SUBTITULO21.getId(), componenteSeleccionado);
		resultadoServicioSub21.remove(registroActualizado);
	}

	public void actualizarS22(Integer row, String codEstablecimiento){
		System.out.println("actualizando "+codEstablecimiento);
		System.out.println("row " + row);
		OTResumenDependienteServicioVO registroTabla = resultadoServicioSub22.get(row);
		OTResumenDependienteServicioVO registroActualizado = otService.aprobarMontoRemesaProfesional(registroTabla, idProgramaProxAno, Subtitulo.SUBTITULO22.getId(), componenteSeleccionado);
		resultadoServicioSub22.remove(registroActualizado);
	}

	public void actualizarS29(Integer row, String codEstablecimiento){
		System.out.println("actualizando " + codEstablecimiento);
		System.out.println("row " + row);
		OTResumenDependienteServicioVO registroTabla = resultadoServicioSub29.get(row);
		OTResumenDependienteServicioVO registroActualizado = otService.aprobarMontoRemesaProfesional(registroTabla, idProgramaProxAno, Subtitulo.SUBTITULO29.getId(), componenteSeleccionado);
		resultadoServicioSub29.remove(registroActualizado);
	}

	public void actualizarS24(Integer row, Integer idComuna){
		System.out.println("actualizando " + idComuna);
		System.out.println("row " + row);
		OTResumenMunicipalVO registroTabla = resultadoMunicipal.get(row);
		OTResumenMunicipalVO registroActualizado = otService.aprobarMontoRemesaProfesional(registroTabla, idProgramaProxAno, Subtitulo.SUBTITULO24.getId(), componenteSeleccionado);
		resultadoMunicipal.remove(registroActualizado);
	}
	
	public void actualizarPercapita(Integer row, Integer idComuna){
		System.out.println("actualizando " + idComuna);
		System.out.println("row " + row);
		OTPerCapitaVO registroTabla = resultadoPercapita.get(row);
		OTPerCapitaVO registroActualizado = otService.aprobarMontoRemesaProfesional(registroTabla, idProgramaProxAno, Subtitulo.SUBTITULO24.getId(), componenteSeleccionado);
		resultadoMunicipal.remove(registroActualizado);
	}

	public Integer actualizar(){return null;}

	public String actualizarCamposHidden(){
		System.out.println("ACTUALIZANDO SUBTITULO "+subtituloHidden);
		OTResumenDependienteServicioVO registroTabla = null;
		OTResumenMunicipalVO registroTablaMunicipal = null;
		boolean servicio=false;
		if(subtituloHidden.equals("21")){
			registroTabla = resultadoServicioSub21.get(Integer.parseInt(filaHidden));
			servicio=true;
		}
		if(subtituloHidden.equals("22")){
			registroTabla = resultadoServicioSub22.get(Integer.parseInt(filaHidden));
			servicio=true;
		}
		if(subtituloHidden.equals("29")){
			registroTabla = resultadoServicioSub29.get(Integer.parseInt(filaHidden));
			servicio=true;
		}
		if(subtituloHidden.equals("24")){
			registroTablaMunicipal = resultadoMunicipal.get(Integer.parseInt(filaHidden));
			servicio=false;
		}

		if(servicio){
			for(RemesasProgramaVO remesa : registroTabla.getRemesas()){
				if(remesa.getIdMes() == Integer.parseInt(mesHidden)){
					for(DiaVO dia : remesa.getDias()){
						if(dia.getDia() == Integer.parseInt(diaHidden)){
							if(montoHidden != null){
								montoHidden = montoHidden.replace(".", "");
								dia.setMonto(Long.parseLong(montoHidden));	
							}
						}
					}
				}
			}
		}else{
			for(RemesasProgramaVO remesa : registroTablaMunicipal.getRemesas()){
				if(remesa.getIdMes() == Integer.parseInt(mesHidden)){
					for(DiaVO dia : remesa.getDias()){
						if(dia.getDia() == Integer.parseInt(diaHidden)){
							if(montoHidden != null){
								montoHidden = montoHidden.replace(".", "");
								dia.setMonto(Long.parseLong(montoHidden));
							}
						}
					}
				}
			}
		}
		return null;
	}

	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		return parameters;
	}

	@Override
	public String iniciarProceso() {
		return null;
	}

	public Integer getProgramaSeleccionado() {
		return programaSeleccionado;
	}

	public void setProgramaSeleccionado(Integer programaSeleccionado) {
		this.programaSeleccionado = programaSeleccionado;
	}

	public Integer getIdProgramaProxAno() {
		return idProgramaProxAno;
	}

	public void setIdProgramaProxAno(Integer idProgramaProxAno) {
		idProgramaProxAno = idProgramaProxAno;
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

	public List<OTPerCapitaVO> getResultadoPercapita() {
		return resultadoPercapita;
	}
	public void setResultadoPercapita(List<OTPerCapitaVO> resultadoPercapita) {
		this.resultadoPercapita = resultadoPercapita;
	}

	public List<RemesasProgramaVO> getRemesasPrograma() {
		return remesasPrograma;
	}

	public void setRemesasPrograma(List<RemesasProgramaVO> remesasPrograma) {
		this.remesasPrograma = remesasPrograma;
	}
	public boolean isPercapita() {
		return percapita;
	}

	public void setPercapita(boolean percapita) {
		this.percapita = percapita;
	}

	public String getFilaHidden() {
		return filaHidden;
	}

	public void setFilaHidden(String filaHidden) {
		this.filaHidden = filaHidden;
	}

	public String getDiaHidden() {
		return diaHidden;
	}

	public void setDiaHidden(String diaHidden) {
		this.diaHidden = diaHidden;
	}

	public String getMesHidden() {
		return mesHidden;
	}

	public void setMesHidden(String mesHidden) {
		this.mesHidden = mesHidden;
	}

	public String getMontoHidden() {
		return montoHidden;
	}

	public void setMontoHidden(String montoHidden) {
		this.montoHidden = montoHidden;
	}

	public List<RemesasProgramaVO> getRemesasPerCapita() {
		return remesasPerCapita;
	}

	public void setRemesasPerCapita(List<RemesasProgramaVO> remesasPerCapita) {
		this.remesasPerCapita = remesasPerCapita;
	}

	public String getSubtituloHidden() {
		return subtituloHidden;
	}

	public void setSubtituloHidden(String subtituloHidden) {
		this.subtituloHidden = subtituloHidden;
	}

	public Integer getAnoCurso() {
		return anoCurso;
	}

	public void setAnoCurso(Integer anoCurso) {
		this.anoCurso = anoCurso;
	}

	public boolean isDesemenoDificil() {
		return desemenoDificil;
	}

	public void setDesemenoDificil(boolean desemenoDificil) {
		this.desemenoDificil = desemenoDificil;
	}

	public boolean isRebajaIAAPS() {
		return rebajaIAAPS;
	}

	public void setRebajaIAAPS(boolean rebajaIAAPS) {
		this.rebajaIAAPS = rebajaIAAPS;
	}
	
}
