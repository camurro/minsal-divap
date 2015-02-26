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
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import minsal.divap.enums.BusinessProcess;
import minsal.divap.enums.Subtitulo;
import minsal.divap.service.ComponenteService;
import minsal.divap.service.OTService;
import minsal.divap.service.ProgramasService;
import minsal.divap.service.UtilitariosService;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.DiaVO;
import minsal.divap.vo.OTPerCapitaVO;
import minsal.divap.vo.OTResumenDependienteServicioVO;
import minsal.divap.vo.OTResumenMunicipalVO;
import minsal.divap.vo.ProgramaFonasaVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.RemesasProgramaVO;
import minsal.divap.vo.ResumenFONASAMunicipalVO;
import minsal.divap.vo.ResumenFONASAServicioVO;
import minsal.divap.vo.ResumenProgramaMixtoVO;
import minsal.divap.vo.ServiciosVO;
import minsal.divap.vo.SubtituloVO;
import minsal.divap.vo.TaskDataVO;
import minsal.divap.vo.TaskVO;

import org.apache.log4j.Logger;

import cl.redhat.bandejaTareas.task.AbstractTaskMBean;

@Named("procesoOTRevisarConsolidacionController")
@ViewScoped
public class ProcesoOTRevisarConsolidacionController extends AbstractTaskMBean
implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4187695365228453660L;
	@Inject private transient Logger log;
	
	private Integer programaSeleccionado;
	private Integer programaSeleccionadoResumen;
	private Integer IdProgramaProxAno;
	private ProgramaVO programa;
	private ProgramaVO programaResumen;
	
	@EJB
	private OTService otService;
	@EJB
	private ProgramasService programasService;
	@EJB
	private UtilitariosService utilitariosService;
	@EJB
	private ComponenteService componenteService;
	
	private Integer servicioSeleccionado;
	private List<ServiciosVO> listaServicios;
	private Integer componenteSeleccionado;
	private List<ComponentesVO> listaComponentes;
	private List<ComponentesVO> listaComponentesResumen;
	private List<ProgramaVO> listaProgramas;
	private List<ProgramaVO> listaProgramasResumen;
	private List<RemesasProgramaVO> remesasPrograma;
	private List<RemesasProgramaVO> remesasPerCapita;
	
	private List<OTResumenDependienteServicioVO> resultadoServicioSub21;
	private List<OTResumenDependienteServicioVO> resultadoServicioSub22;
	private List<OTResumenDependienteServicioVO> resultadoServicioSub29;
	private List<OTResumenMunicipalVO> resultadoMunicipal;
	private List<OTPerCapitaVO> resultadoPercapita;
	private List<ResumenProgramaMixtoVO> resumenPrograma;
	
	private List<ProgramaFonasaVO> encabezadoFonasa;
	private List<ResumenFONASAServicioVO> resumenFonasaServicioS21;
	private List<ResumenFONASAServicioVO> resumenFonasaServicioS22;
	private List<ResumenFONASAServicioVO> resumenFonasaServicioS29;
	private List<ResumenFONASAMunicipalVO> resumenFonasaMunicipalS24;
	
	
	private boolean subtitulo21;
	private boolean subtitulo22;
	private boolean subtitulo29;
	private boolean subtitulo24;
	private boolean percapita;
	
	private boolean subtitulo21Resumen;
	private boolean subtitulo22Resumen;
	private boolean subtitulo29Resumen;
	private boolean subtitulo24Resumen;
	private Long totalSub21Resumen;
	private Long totalSub22Resumen;
	private Long totalSub29Resumen;
	private Long totalSub24Resumen;
	private Long totalResumen;
	
	private String filaHidden;
	private String diaHidden;
	private String mesHidden;
	private String montoHidden;
	private String subtituloHidden;
	
	private String mesActual;
	private Integer activeTab = 0;
	private Integer anoCurso;
	
	private boolean botonBloqueado;
	
	
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
		anoCurso = (Integer) getTaskDataVO().getData().get("_ano");
		mesActual = otService.getMesCurso(false);
		
		listaServicios = utilitariosService.getAllServicios();
		listaProgramas = otService.getProgramas(getLoggedUsername(), getAnoCurso()); 
		
		botonBloqueado=false;
		for(ProgramaVO prog: listaProgramas){
			if(prog.getEstadoOT().getId()!=3){
				//botonBloqueado=true;
				botonBloqueado=false;
				break;
			}
		}
		
		listaProgramasResumen = otService.getProgramas(getLoggedUsername(), getAnoCurso());
		listaComponentes= new ArrayList<ComponentesVO>();
		
		encabezadoFonasa = programasService.getProgramasFonasa(true);
		cargarResumenFonasa();
		
		
	}
	
	private void cargarResumenFonasa() {
		resumenFonasaServicioS21 = new ArrayList<ResumenFONASAServicioVO>();
		resumenFonasaServicioS22 = new ArrayList<ResumenFONASAServicioVO>();
		resumenFonasaServicioS29 = new ArrayList<ResumenFONASAServicioVO>();
		resumenFonasaMunicipalS24 = new ArrayList<ResumenFONASAMunicipalVO>();
		
		resumenFonasaServicioS21 = otService.cargarFonasaServicio(Subtitulo.SUBTITULO21.getId());
		resumenFonasaServicioS22 = otService.cargarFonasaServicio(Subtitulo.SUBTITULO22.getId());
		resumenFonasaServicioS29 = otService.cargarFonasaServicio(Subtitulo.SUBTITULO29.getId());
		
		resumenFonasaMunicipalS24 = otService.cargarFonasaMunicipal();
	}

	public void cargaComponentes() throws NumberFormatException, ParseException{
		programa = otService.getProgramaById(programaSeleccionado);
		remesasPrograma = otService.getRemesasPrograma(programa.getId(), Integer.parseInt(otService.getMesCurso(true)));
		remesasPerCapita = otService.getRemesasPerCapita(programa.getId(), Integer.parseInt(otService.getMesCurso(true)));
		System.out.println("programaSeleccionado" + programaSeleccionado);
		listaComponentes= componenteService.getComponenteByPrograma(programa.getId());
	}
	
	public void buscarDetallePrograma(){
		subtitulo21=false;
		subtitulo22=false;
		subtitulo29=false;
		subtitulo24=false;	
		percapita=false;
		if(programa.getId()< 0){
			percapita=true;
			resultadoPercapita = otService.getDetallePerCapita(servicioSeleccionado, otService.getAnoCurso(), programa.getIdProgramaAno()); 
			System.out.println("Resultados PerCapita: "+resultadoPercapita.size());
		}else{
			System.out.println("Buscar Resultados para Componente: "+componenteSeleccionado+" Servicio: "+servicioSeleccionado);
			ComponentesVO componenteVO = componenteService.getComponenteVOById(componenteSeleccionado);

			for(SubtituloVO subs : componenteVO.getSubtitulos()){
				System.out.println(subs.getId());
				if(subs.getId() == Subtitulo.SUBTITULO21.getId()){
					subtitulo21=true;
					resultadoServicioSub21 = otService.getDetalleOTServicioConsolidador(componenteSeleccionado,servicioSeleccionado, 
							Subtitulo.SUBTITULO21.getId(),programa.getIdProgramaAno());
				}
				if(subs.getId() == Subtitulo.SUBTITULO22.getId()){
					subtitulo22=true;
					resultadoServicioSub22 = otService.getDetalleOTServicioConsolidador(componenteSeleccionado,servicioSeleccionado, 
							Subtitulo.SUBTITULO22.getId(),programa.getIdProgramaAno());
				}
				if(subs.getId() == Subtitulo.SUBTITULO29.getId()){
					subtitulo29=true;
					resultadoServicioSub29 = otService.getDetalleOTServicioConsolidador(componenteSeleccionado,servicioSeleccionado, 
							Subtitulo.SUBTITULO29.getId(),programa.getIdProgramaAno());
				}
				if(subs.getId() == Subtitulo.SUBTITULO24.getId()){
					subtitulo24=true;
					resultadoMunicipal = otService.getDetalleOTMunicipalConsolidador(componenteSeleccionado,servicioSeleccionado, programa.getIdProgramaAno());
				}
			}
		}
	}
	
	public void buscarResumenPrograma(){
		programaResumen = otService.getProgramaById(programaSeleccionadoResumen);
		System.out.println("Buscando resumen para programa: "+ programaResumen.getNombre());
		listaComponentesResumen= componenteService.getComponenteByPrograma(programaSeleccionadoResumen);
		
		resumenPrograma = otService.getResumenPrograma(programaResumen);
		
		subtitulo21Resumen=false;
		subtitulo22Resumen=false;
		subtitulo24Resumen=false;
		subtitulo29Resumen=false;
		totalSub21Resumen=0l;
		totalSub22Resumen=0l;
		totalSub29Resumen=0l;
		totalSub24Resumen=0l;
		
		for(ComponentesVO componente:listaComponentesResumen){
			for(SubtituloVO subtitulo : componente.getSubtitulos()){
				if(subtitulo.getId()==Subtitulo.SUBTITULO21.getId()){
					subtitulo21Resumen=true;
				}
				if(subtitulo.getId()==Subtitulo.SUBTITULO22.getId()){
					subtitulo22Resumen=true;
				}
				if(subtitulo.getId()==Subtitulo.SUBTITULO29.getId()){
					subtitulo29Resumen=true;
				}
				if(subtitulo.getId()==Subtitulo.SUBTITULO24.getId()){
					subtitulo24Resumen=true;
				}
			}
		}
		for(ResumenProgramaMixtoVO resultado : resumenPrograma){
			totalSub21Resumen += resultado.getTotalS21();
			totalSub22Resumen += resultado.getTotalS22();
			totalSub29Resumen += resultado.getTotalS29();
			totalSub24Resumen += resultado.getTotalS24();
		}
		totalResumen = totalSub21Resumen+totalSub22Resumen+totalSub29Resumen+totalSub24Resumen;
	}
	
	public Integer actualizar(){return null;}
	
	public String actualizarCamposHidden(){
		System.out.println("ACTUALIZANDO SUBTITULO "+subtituloHidden);
		OTResumenDependienteServicioVO registroTabla=null;
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
							dia.setMonto(Long.parseLong(montoHidden));	
						}
					}
				}
			}
		}else{
			for(RemesasProgramaVO remesa : registroTablaMunicipal.getRemesas()){
				if(remesa.getIdMes() == Integer.parseInt(mesHidden)){
					for(DiaVO dia : remesa.getDias()){
						if(dia.getDia() == Integer.parseInt(diaHidden)){
							dia.setMonto(Long.parseLong(montoHidden));	
						}
					}
				}
			}
		}
		

		return null;
	}
	
	public void actualizarPerCapita(Integer row, Integer idComuna){
		System.out.println(resultadoPercapita.size());
		OTPerCapitaVO registroTabla = resultadoPercapita.get(row);
		OTPerCapitaVO registroActualizado = otService.actualizarComunaPerCapita(idComuna,registroTabla,programa.getIdProgramaAno());
		resultadoPercapita.remove(registroActualizado);
		System.out.println(resultadoPercapita.size());
	}

	public void actualizarS21(Integer row, String codEstablecimiento){
		System.out.println("actualizando "+codEstablecimiento);
		OTResumenDependienteServicioVO registroTabla = resultadoServicioSub21.get(row);
		if(registroTabla.getIdDetalleRemesa()!=null){
			otService.eliminarDetalleRemesa(registroTabla.getIdDetalleRemesa());
		}
		OTResumenDependienteServicioVO registroActualizado = otService.actualizarServicio(registroTabla, programa.getIdProgramaAno(), Subtitulo.SUBTITULO21.getId(),componenteSeleccionado, registroTabla.getIdDetalleRemesa());
		resultadoServicioSub21.remove(registroActualizado);
	}
	
	public void actualizarS22(Integer row, String codEstablecimiento){
		System.out.println("actualizando "+codEstablecimiento);
		OTResumenDependienteServicioVO registroTabla = resultadoServicioSub22.get(row);
		if(registroTabla.getIdDetalleRemesa()!=null){
			otService.eliminarDetalleRemesa(registroTabla.getIdDetalleRemesa());
		}
		OTResumenDependienteServicioVO registroActualizado = otService.actualizarServicio(registroTabla, programa.getIdProgramaAno(), Subtitulo.SUBTITULO22.getId(),componenteSeleccionado,registroTabla.getIdDetalleRemesa());
		resultadoServicioSub22.remove(registroActualizado);
	}
	
	public void actualizarS29(Integer row, String codEstablecimiento){
		System.out.println("actualizando "+codEstablecimiento);
		OTResumenDependienteServicioVO registroTabla = resultadoServicioSub29.get(row);
		if(registroTabla.getIdDetalleRemesa()!=null){
			otService.eliminarDetalleRemesa(registroTabla.getIdDetalleRemesa());
		}
		OTResumenDependienteServicioVO registroActualizado = otService.actualizarServicio(registroTabla, programa.getIdProgramaAno(), Subtitulo.SUBTITULO29.getId(),componenteSeleccionado,registroTabla.getIdDetalleRemesa());
		resultadoServicioSub29.remove(registroActualizado);
	}
	
	public void actualizarS24(Integer row, Integer idComuna){
		OTResumenMunicipalVO registroTabla = resultadoMunicipal.get(row);
		if(registroTabla.getIdDetalleRemesa()!=null){
			otService.eliminarDetalleRemesa(registroTabla.getIdDetalleRemesa());
		}
		OTResumenMunicipalVO registroActualizado = otService.actualizarMunicipal(registroTabla, programa.getIdProgramaAno(), Subtitulo.SUBTITULO24.getId(),componenteSeleccionado,registroTabla.getIdDetalleRemesa());
		resultadoMunicipal.remove(registroActualizado);
	}
	
	@Override
	protected Map<String, Object> createResultData() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		System.out.println("createResultData usuario-->"
				+ getSessionBean().getUsername());
		parameters.put("usuario", getSessionBean().getUsername());
		return parameters;
	}

	@Override
	public String iniciarProceso() {
		String success = "bandejaTareas";
		Long procId = iniciarProceso(BusinessProcess.OTCONSOLIDADOR);
		System.out.println("procId-->" + procId);
		if (procId == null) {
			success = null;
		} else {
			TaskVO task = getUserTasksByProcessId(procId, getSessionBean()
					.getUsername());
			if (task != null) {
				TaskDataVO taskDataVO = getTaskData(task.getId());
				if (taskDataVO != null) {
					System.out.println("taskDataVO recuperada=" + taskDataVO);
					setOnSession("taskDataSeleccionada", taskDataVO);
				}
			}
		}
		return success;
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

	public List<RemesasProgramaVO> getRemesasPrograma() {
		return remesasPrograma;
	}

	public void setRemesasPrograma(List<RemesasProgramaVO> remesasPrograma) {
		this.remesasPrograma = remesasPrograma;
	}

	public List<RemesasProgramaVO> getRemesasPerCapita() {
		return remesasPerCapita;
	}

	public void setRemesasPerCapita(List<RemesasProgramaVO> remesasPerCapita) {
		this.remesasPerCapita = remesasPerCapita;
	}

	public boolean isPercapita() {
		return percapita;
	}

	public void setPercapita(boolean percapita) {
		this.percapita = percapita;
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

	public List<ProgramaVO> getListaProgramas() {
		return listaProgramas;
	}

	public void setListaProgramas(List<ProgramaVO> listaProgramas) {
		this.listaProgramas = listaProgramas;
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

	public List<OTPerCapitaVO> getResultadoPercapita() {
		return resultadoPercapita;
	}

	public void setResultadoPercapita(List<OTPerCapitaVO> resultadoPercapita) {
		this.resultadoPercapita = resultadoPercapita;
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

	public Integer getProgramaSeleccionadoResumen() {
		return programaSeleccionadoResumen;
	}

	public void setProgramaSeleccionadoResumen(Integer programaSeleccionadoResumen) {
		this.programaSeleccionadoResumen = programaSeleccionadoResumen;
	}

	public List<ProgramaVO> getListaProgramasResumen() {
		return listaProgramasResumen;
	}

	public void setListaProgramasResumen(List<ProgramaVO> listaProgramasResumen) {
		this.listaProgramasResumen = listaProgramasResumen;
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

	public String getSubtituloHidden() {
		return subtituloHidden;
	}

	public void setSubtituloHidden(String subtituloHidden) {
		this.subtituloHidden = subtituloHidden;
	}

	public ProgramaVO getProgramaResumen() {
		return programaResumen;
	}

	public void setProgramaResumen(ProgramaVO programaResumen) {
		this.programaResumen = programaResumen;
	}

	public List<ResumenProgramaMixtoVO> getResumenPrograma() {
		return resumenPrograma;
	}

	public void setResumenPrograma(List<ResumenProgramaMixtoVO> resumenPrograma) {
		this.resumenPrograma = resumenPrograma;
	}

	public List<ComponentesVO> getListaComponentesResumen() {
		return listaComponentesResumen;
	}

	public void setListaComponentesResumen(
			List<ComponentesVO> listaComponentesResumen) {
		this.listaComponentesResumen = listaComponentesResumen;
	}

	public boolean isSubtitulo21Resumen() {
		return subtitulo21Resumen;
	}

	public void setSubtitulo21Resumen(boolean subtitulo21Resumen) {
		this.subtitulo21Resumen = subtitulo21Resumen;
	}

	public boolean isSubtitulo22Resumen() {
		return subtitulo22Resumen;
	}

	public void setSubtitulo22Resumen(boolean subtitulo22Resumen) {
		this.subtitulo22Resumen = subtitulo22Resumen;
	}

	public boolean isSubtitulo29Resumen() {
		return subtitulo29Resumen;
	}

	public void setSubtitulo29Resumen(boolean subtitulo29Resumen) {
		this.subtitulo29Resumen = subtitulo29Resumen;
	}

	public boolean isSubtitulo24Resumen() {
		return subtitulo24Resumen;
	}

	public void setSubtitulo24Resumen(boolean subtitulo24Resumen) {
		this.subtitulo24Resumen = subtitulo24Resumen;
	}

	public Long getTotalSub21Resumen() {
		return totalSub21Resumen;
	}

	public void setTotalSub21Resumen(Long totalSub21Resumen) {
		this.totalSub21Resumen = totalSub21Resumen;
	}

	public Long getTotalSub22Resumen() {
		return totalSub22Resumen;
	}

	public void setTotalSub22Resumen(Long totalSub22Resumen) {
		this.totalSub22Resumen = totalSub22Resumen;
	}

	public Long getTotalSub29Resumen() {
		return totalSub29Resumen;
	}

	public void setTotalSub29Resumen(Long totalSub29Resumen) {
		this.totalSub29Resumen = totalSub29Resumen;
	}

	public Long getTotalSub24Resumen() {
		return totalSub24Resumen;
	}

	public void setTotalSub24Resumen(Long totalSub24Resumen) {
		this.totalSub24Resumen = totalSub24Resumen;
	}

	public Long getTotalResumen() {
		return totalResumen;
	}

	public void setTotalResumen(Long totalResumen) {
		this.totalResumen = totalResumen;
	}

	public List<ProgramaFonasaVO> getEncabezadoFonasa() {
		return encabezadoFonasa;
	}

	public void setEncabezadoFonasa(List<ProgramaFonasaVO> encabezadoFonasa) {
		this.encabezadoFonasa = encabezadoFonasa;
	}

	public List<ResumenFONASAServicioVO> getResumenFonasaServicioS21() {
		return resumenFonasaServicioS21;
	}

	public void setResumenFonasaServicioS21(
			List<ResumenFONASAServicioVO> resumenFonasaServicioS21) {
		this.resumenFonasaServicioS21 = resumenFonasaServicioS21;
	}

	public List<ResumenFONASAServicioVO> getResumenFonasaServicioS22() {
		return resumenFonasaServicioS22;
	}

	public void setResumenFonasaServicioS22(
			List<ResumenFONASAServicioVO> resumenFonasaServicioS22) {
		this.resumenFonasaServicioS22 = resumenFonasaServicioS22;
	}

	public List<ResumenFONASAServicioVO> getResumenFonasaServicioS29() {
		return resumenFonasaServicioS29;
	}

	public void setResumenFonasaServicioS29(
			List<ResumenFONASAServicioVO> resumenFonasaServicioS29) {
		this.resumenFonasaServicioS29 = resumenFonasaServicioS29;
	}

	public String getMesActual() {
		return mesActual;
	}

	public void setMesActual(String mesActual) {
		this.mesActual = mesActual;
	}

	public List<ResumenFONASAMunicipalVO> getResumenFonasaMunicipalS24() {
		return resumenFonasaMunicipalS24;
	}

	public void setResumenFonasaMunicipalS24(
			List<ResumenFONASAMunicipalVO> resumenFonasaMunicipalS24) {
		this.resumenFonasaMunicipalS24 = resumenFonasaMunicipalS24;
	}

	public Integer getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(Integer activeTab) {
		this.activeTab = activeTab;
	}

	public boolean isBotonBloqueado() {
		return botonBloqueado;
	}

	public void setBotonBloqueado(boolean botonBloqueado) {
		this.botonBloqueado = botonBloqueado;
	}

	public Integer getAnoCurso() {
		return anoCurso;
	}

	public void setAnoCurso(Integer anoCurso) {
		this.anoCurso = anoCurso;
	}
	
}
