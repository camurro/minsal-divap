package minsal.divap.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.xml.bind.JAXBException;

import minsal.divap.dao.AntecedentesComunaDAO;
import minsal.divap.dao.ComponenteDAO;
import minsal.divap.dao.ComunaDAO;
import minsal.divap.dao.ConveniosDAO;
import minsal.divap.dao.DistribucionInicialPercapitaDAO;
import minsal.divap.dao.DocumentDAO;
import minsal.divap.dao.EstablecimientosDAO;
import minsal.divap.dao.InstitucionDAO;
import minsal.divap.dao.MesDAO;
import minsal.divap.dao.ProgramasDAO;
import minsal.divap.dao.RecursosFinancierosProgramasReforzamientoDAO;
import minsal.divap.dao.ReliquidacionDAO;
import minsal.divap.dao.RemesasDAO;
import minsal.divap.dao.SeguimientoDAO;
import minsal.divap.dao.ServicioSaludDAO;
import minsal.divap.dao.TipoSubtituloDAO;
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.dao.UtilitariosDAO;
import minsal.divap.doc.GeneradorResolucionAporteEstatal;
import minsal.divap.doc.GeneradorWord;
import minsal.divap.enums.EstadosConvenios;
import minsal.divap.enums.EstadosProgramas;
import minsal.divap.enums.Instituciones;
import minsal.divap.enums.Subtitulo;
import minsal.divap.enums.TareasSeguimiento;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.enums.TiposDestinatarios;
import minsal.divap.excel.GeneradorExcel;
import minsal.divap.excel.impl.OrdenesTransferenciaSheetExcel;
import minsal.divap.model.mappers.ProgramaMapper;
import minsal.divap.model.mappers.SeguimientoMapper;
import minsal.divap.vo.AdjuntosVO;
import minsal.divap.vo.BodyVO;
import minsal.divap.vo.CellExcelVO;
import minsal.divap.vo.ComunaVO;
import minsal.divap.vo.DiaVO;
import minsal.divap.vo.DocumentoVO;
import minsal.divap.vo.EstablecimientoVO;
import minsal.divap.vo.OTPerCapitaVO;
import minsal.divap.vo.OTResumenDependienteServicioVO;
import minsal.divap.vo.OTResumenMunicipalVO;
import minsal.divap.vo.PagaRemesaVO;
import minsal.divap.vo.PlanillaResumenFonasaVO;
import minsal.divap.vo.ProgramaFonasaVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.RemesasProgramaVO;
import minsal.divap.vo.ReporteEmailsEnviadosVO;
import minsal.divap.vo.ResumenFONASAMunicipalVO;
import minsal.divap.vo.ResumenFONASAServicioVO;
import minsal.divap.vo.ResumenProgramaMixtoVO;
import minsal.divap.vo.SeguimientoVO;
import minsal.divap.xml.GeneradorXML;
import minsal.divap.xml.email.Email;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.docx4j.openpackaging.exceptions.Docx4JException;

import cl.minsal.divap.model.AntecedentesComunaCalculadoRebaja;
import cl.minsal.divap.model.AntecendentesComunaCalculado;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.ConvenioComuna;
import cl.minsal.divap.model.ConvenioComunaComponente;
import cl.minsal.divap.model.ConvenioServicio;
import cl.minsal.divap.model.ConvenioServicioComponente;
import cl.minsal.divap.model.Cuota;
import cl.minsal.divap.model.DetalleRemesas;
import cl.minsal.divap.model.DistribucionInicialPercapita;
import cl.minsal.divap.model.DocumentoRemesas;
import cl.minsal.divap.model.Establecimiento;
import cl.minsal.divap.model.EstadoConvenio;
import cl.minsal.divap.model.EstadoPrograma;
import cl.minsal.divap.model.Festivos;
import cl.minsal.divap.model.Institucion;
import cl.minsal.divap.model.Mes;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.ProgramaFechaRemesa;
import cl.minsal.divap.model.ReferenciaDocumento;
import cl.minsal.divap.model.RemesaConvenios;
import cl.minsal.divap.model.Remesas;
import cl.minsal.divap.model.ReporteEmailsAdjuntos;
import cl.minsal.divap.model.ReporteEmailsDestinatarios;
import cl.minsal.divap.model.ReporteEmailsEnviados;
import cl.minsal.divap.model.ReporteEmailsRemesas;
import cl.minsal.divap.model.Seguimiento;
import cl.minsal.divap.model.ServicioSalud;
import cl.minsal.divap.model.TipoDestinatario;
import cl.minsal.divap.model.Usuario;

@Stateless
@LocalBean
public class OTService {

	@EJB
	private ProgramasDAO programasDAO;
	@EJB
	private EstablecimientosDAO establecimientosDAO;
	@EJB
	private MesDAO mesDAO;
	@EJB
	private InstitucionDAO institucionDAO;
	@EJB
	private RecursosFinancierosProgramasReforzamientoDAO recursosFinancierosProgramasReforzamientoDAO;
	@EJB
	private UtilitariosDAO utilitariosDAO;
	@EJB
	private ServicioSaludDAO servicioSaludDAO;
	@EJB
	private TipoSubtituloDAO subtituloDAO;
	@EJB
	private AntecedentesComunaDAO antecedentesComunaDAO;
	@EJB
	private RemesasDAO remesasDAO;
	@EJB
	private ComunaDAO comunaDAO;
	@EJB
	private ConveniosDAO conveniosDAO;
	@EJB
	private ReliquidacionDAO reliquidacionDAO;
	@EJB
	private DistribucionInicialPercapitaDAO distribucionInicialPercapitaDAO;
	@EJB
	private UsuarioDAO usuarioDAO;
	@EJB
	private SeguimientoDAO seguimientoDAO;
	@EJB
	private DocumentDAO documentoDAO;
	@EJB
	private ComponenteDAO componenteDAO;
	@EJB
	private ReportesServices reporteService;
	@EJB
	private ProgramasService programasService;
	@EJB
	private DocumentService documentService;
	@EJB
	private AlfrescoService alfrescoService;
	@EJB
	private SeguimientoService seguimientoService;
	@EJB
	private EmailService emailService;

	private final long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000;
	private final Integer REMESA_REGULAR = 24;

	@Resource(name="folderOrdenesTransferencia")
	private String folderOrdenesTransferencia;
	@Resource(name="tmpDir")
	private String tmpDir;
	@Resource(name = "tmpDirDoc")
	private String tmpDirDoc;

	public List<ProgramaVO> getProgramas(String username, Integer ano) {
		List<ProgramaVO> programasVO = new ArrayList<ProgramaVO>();
		if(ano == null){
			ano = getAnoCurso();
		}
		List<ProgramaAno> programas = programasDAO.getProgramasByUserAno(username, ano);
		if(programas != null && programas.size() > 0){
			for(ProgramaAno programa : programas){
				programasVO.add(new ProgramaMapper().getBasic(programa));
			}
		}
		return programasVO;
	}

	public Integer getAnoCurso() {
		DateFormat formatNowYear = new SimpleDateFormat("yyyy");
		Date nowDate = new Date();
		return Integer.valueOf(formatNowYear.format(nowDate)); 
	}


	public ProgramaVO getProgramaById(Integer programaSeleccionado) {
		ProgramaAno programaAno = recursosFinancierosProgramasReforzamientoDAO.findById(programaSeleccionado);
		return new ProgramaMapper().getBasic(programaAno);
	}

	public List<OTResumenDependienteServicioVO> getDetalleOTServicio(Integer componenteSeleccionado, Integer servicioSeleccionado, Integer idTipoSubtitulo, Integer idProgramaAno) {

		System.out.println("Buscando Detalle de Convenios/Remesas para Subtitulo servicio");
		System.out.println("Buscando Detalle de Convenios/Remesas para idTipoSubtitulo = "+ idTipoSubtitulo);
		System.out.println("Buscando Detalle de Convenios/Remesas para componenteSeleccionado = "+ componenteSeleccionado);
		System.out.println("Buscando Detalle de Convenios/Remesas para servicioSeleccionado = "+servicioSeleccionado);
		System.out.println("Buscando Detalle de Convenios/Remesas para idProgramaAno = "+idProgramaAno);
		List<OTResumenDependienteServicioVO> listaOTResumenDependienteServicioVO = new ArrayList<OTResumenDependienteServicioVO>();
		ProgramaVO programaVO = programasService.getProgramaAno(idProgramaAno);
		List<Cuota> cuotasPorPrograma = reliquidacionDAO.getCuotasByProgramaAno(idProgramaAno);

		Integer mes = Integer.parseInt(getMesCurso(true));
		
		
		List<ServicioSalud> servicios = null;
		if(servicioSeleccionado == null){
			servicios = servicioSaludDAO.getServiciosOrderId();
		}else{
			servicios = new ArrayList<ServicioSalud>();
			ServicioSalud servicioSalud = servicioSaludDAO.getById(servicioSeleccionado);
			servicios.add(servicioSalud);
		}
		
		for(ServicioSalud servicio : servicios){
			if(servicio.getEstablecimientos() != null && servicio.getEstablecimientos().size() > 0){
				for(Establecimiento establecimiento : servicio.getEstablecimientos()){
					
					List<DetalleRemesas> remesasPagadasProgramaAnoComponenteEstablecimientoSubtitulo = remesasDAO.getRemesasPagadasByProgramaAnoComponenteEstablecimientoSubtitulo(idProgramaAno, componenteSeleccionado, establecimiento.getId(), idTipoSubtitulo);
					List<DetalleRemesas> remesasPendientesProgramaAnoComponenteEstablecimientoSubtitulo = remesasDAO.getRemesasPendientesByProgramaAnoComponenteEstablecimientoSubtitulo(idProgramaAno, componenteSeleccionado, establecimiento.getId(), idTipoSubtitulo);
					
					Boolean primeraRemesaAprobadaProfesional = null;
					boolean remesaPendiente = false;
					boolean remesaPagada = false;
					if((remesasPagadasProgramaAnoComponenteEstablecimientoSubtitulo != null && remesasPagadasProgramaAnoComponenteEstablecimientoSubtitulo.size() > 0) ||
							(remesasPendientesProgramaAnoComponenteEstablecimientoSubtitulo != null && remesasPendientesProgramaAnoComponenteEstablecimientoSubtitulo.size() > 0)){
						primeraRemesaAprobadaProfesional = false;
						if(remesasPagadasProgramaAnoComponenteEstablecimientoSubtitulo != null && remesasPagadasProgramaAnoComponenteEstablecimientoSubtitulo.size() > 0){
							remesaPagada = true;
						}
						if(remesasPendientesProgramaAnoComponenteEstablecimientoSubtitulo != null && remesasPendientesProgramaAnoComponenteEstablecimientoSubtitulo.size() > 0){
							remesaPendiente = true;
						}
					}else{
						primeraRemesaAprobadaProfesional = true;
					}
					Long marcoPresupuestario = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(),	idProgramaAno, componenteSeleccionado, idTipoSubtitulo);
					List<RemesasProgramaVO> remesas = getRemesasPrograma(programaVO.getId(), mes, programaVO.getAno());
					OTResumenDependienteServicioVO oTResumenDependienteServicioVO = new OTResumenDependienteServicioVO();
					oTResumenDependienteServicioVO.setCreacion(false);
					oTResumenDependienteServicioVO.setMarcoPresupuestario(marcoPresupuestario);
					EstablecimientoVO establecimientoVO = new EstablecimientoVO();
					establecimientoVO.setId(establecimiento.getId());
					establecimientoVO.setCodigoEstablecimiento(establecimiento.getCodigo());
					establecimientoVO.setNombre(establecimiento.getNombre());
					oTResumenDependienteServicioVO.setEstablecimiento(establecimientoVO);
					if(primeraRemesaAprobadaProfesional){
						Integer cuotaAPagar = 0;
						for(Cuota cuota : cuotasPorPrograma){
							if((cuota.getIdMes() == null) || (cuota.getIdMes().getIdMes().intValue() <= mes.intValue())){
								cuotaAPagar++;
							}
						}
						//revisa si hay convenios en tramite
						List<ConvenioServicioComponente> conveniosEnTramiteEstablecimiento = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(
								idProgramaAno, componenteSeleccionado, idTipoSubtitulo, establecimiento.getId(), EstadosConvenios.TRAMITE.getId());
						ConvenioServicioComponente convenioEnTramiteEstablecimiento = ((conveniosEnTramiteEstablecimiento == null || conveniosEnTramiteEstablecimiento.size() == 0) ? null : conveniosEnTramiteEstablecimiento.get(conveniosEnTramiteEstablecimiento.size() - 1));
						oTResumenDependienteServicioVO.setCreacion(true);
						if(convenioEnTramiteEstablecimiento != null){//significa que otro componente subtitulo fue aprobado por el profesional y no hay nuevos convenios aprobados
							oTResumenDependienteServicioVO.setIdConvenioServicio(convenioEnTramiteEstablecimiento.getConvenioServicio().getIdConvenioServicio());
							oTResumenDependienteServicioVO.setIdConvenioServicioComponenteSinAprobar(convenioEnTramiteEstablecimiento.getIdConvenioServicioComponente());
							oTResumenDependienteServicioVO.setTransferenciaAcumulada(0L);
							oTResumenDependienteServicioVO.setDiferencia(marcoPresupuestario - 0L);
							oTResumenDependienteServicioVO.setConveniosRecibidos(new Long(convenioEnTramiteEstablecimiento.getMontoIngresado()));
							Long montoRemesa = 0L;
							for(int cuotaActual = 1; cuotaActual <= cuotaAPagar; cuotaActual++){
								Cuota cuota = cuotasPorPrograma.get((cuotaActual - 1));
								Long montoCuota = Math.round(convenioEnTramiteEstablecimiento.getMontoIngresado() * (cuota.getPorcentaje() / 100.0));	
								montoRemesa += montoCuota;
								oTResumenDependienteServicioVO.setCuotaFinal(cuota.getId());
							}
							boolean primerMes = true;
							boolean mesConMontoAsignado = false;
							for(RemesasProgramaVO remesaPrograma : remesas){
								if(primerMes){
									System.out.println("Mes Remesa 1" + remesaPrograma.getMes());
									for(DiaVO dia : remesaPrograma.getDias()){
										if(!dia.isBloqueado() && !mesConMontoAsignado){
											System.out.println("dia.getDia()=" + dia.getDia());
											dia.setMonto(montoRemesa);
											mesConMontoAsignado = true;
										}
									}
									primerMes = false;
								}else{
									System.out.println("Mes Remesa 2" + remesaPrograma.getMes());
									if(!mesConMontoAsignado){
										for(DiaVO dia : remesaPrograma.getDias()){
											if(!dia.isBloqueado() && !mesConMontoAsignado){
												System.out.println("dia.getDia()=" + dia.getDia());
												dia.setMonto(montoRemesa);
												mesConMontoAsignado = true;
											}
										}
									}
								}
							}
						}else{
							//revisa si hay convenios aprobado para componente/subtitulo
							
							boolean existeRemesaMesActual = false;
							List<ConvenioServicioComponente> conveniosAprobadosEstablecimiento = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(
									idProgramaAno, componenteSeleccionado, idTipoSubtitulo, establecimiento.getId(), EstadosConvenios.APROBADO.getId());
							if(conveniosAprobadosEstablecimiento != null && conveniosAprobadosEstablecimiento.size() > 0){
								existeRemesaMesActual = true;
								int sizeConveniosAprobadosEstablecimiento = conveniosAprobadosEstablecimiento.size();
								ConvenioServicioComponente convenioAprobadoEstablecimiento = conveniosAprobadosEstablecimiento.get(sizeConveniosAprobadosEstablecimiento - 1);
								oTResumenDependienteServicioVO.setIdConvenioServicio(convenioAprobadoEstablecimiento.getConvenioServicio().getIdConvenioServicio());
								oTResumenDependienteServicioVO.setIdConvenioServicioComponenteSinAprobar(convenioAprobadoEstablecimiento.getIdConvenioServicioComponente());
								oTResumenDependienteServicioVO.setTransferenciaAcumulada(0L);
								oTResumenDependienteServicioVO.setDiferencia(marcoPresupuestario - 0L);
								oTResumenDependienteServicioVO.setConveniosRecibidos(new Long(convenioAprobadoEstablecimiento.getMontoIngresado()));
								Long montoRemesa = 0L;
								for(int cuotaActual = 1; cuotaActual <= cuotaAPagar; cuotaActual++){
									Cuota cuota = cuotasPorPrograma.get((cuotaActual - 1));
									Long montoCuota = Math.round(convenioAprobadoEstablecimiento.getMontoIngresado() * (cuota.getPorcentaje() / 100.0));	
									montoRemesa += montoCuota;
									oTResumenDependienteServicioVO.setCuotaFinal(cuota.getId());
								}
								boolean primerMes = true;
								boolean mesConMontoAsignado = false;
								for(RemesasProgramaVO remesaPrograma : remesas){
									if(primerMes){
										System.out.println("Mes Remesa 1" + remesaPrograma.getMes());
										for(DiaVO dia : remesaPrograma.getDias()){
											if(!dia.isBloqueado() && !mesConMontoAsignado){
												System.out.println("dia.getDia()=" + dia.getDia());
												dia.setMonto(montoRemesa);
												mesConMontoAsignado = true;
											}
										}
										primerMes = false;
									}else{
										System.out.println("Mes Remesa 2" + remesaPrograma.getMes());
										if(!mesConMontoAsignado){
											for(DiaVO dia : remesaPrograma.getDias()){
												if(!dia.isBloqueado() && !mesConMontoAsignado){
													System.out.println("dia.getDia()=" + dia.getDia());
													dia.setMonto(montoRemesa);
													mesConMontoAsignado = true;
												}
											}
										}
									}
								} 
							}
							if(!existeRemesaMesActual){
								List<ConvenioServicioComponente> conveniosPagadosEstablecimiento = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(
									idProgramaAno, componenteSeleccionado, idTipoSubtitulo, establecimiento.getId(), EstadosConvenios.PAGADO.getId());
								if(conveniosPagadosEstablecimiento != null && conveniosPagadosEstablecimiento.size() > 0){
									existeRemesaMesActual = true;
									int sizeConveniosPagadosEstablecimiento = conveniosPagadosEstablecimiento.size();
									ConvenioServicioComponente convenioPagadoEstablecimiento = conveniosPagadosEstablecimiento.get(sizeConveniosPagadosEstablecimiento - 1);
									oTResumenDependienteServicioVO.setIdConvenioServicio(convenioPagadoEstablecimiento.getConvenioServicio().getIdConvenioServicio());
									oTResumenDependienteServicioVO.setIdConvenioServicioComponenteSinAprobar(convenioPagadoEstablecimiento.getIdConvenioServicioComponente());
									oTResumenDependienteServicioVO.setTransferenciaAcumulada(0L);
									oTResumenDependienteServicioVO.setDiferencia(marcoPresupuestario - 0L);
									Long montoRemesa = 0L;
									for(int cuotaActual = 1; cuotaActual <= cuotaAPagar; cuotaActual++){
										Cuota cuota = cuotasPorPrograma.get((cuotaActual - 1));
										Long montoCuota = Math.round(convenioPagadoEstablecimiento.getMontoIngresado() * (cuota.getPorcentaje() / 100.0));	
										montoRemesa += montoCuota;
										oTResumenDependienteServicioVO.setCuotaFinal(cuota.getId());
									}
									boolean primerMes = true;
									boolean mesConMontoAsignado = false;
									for(RemesasProgramaVO remesaPrograma : remesas){
										if(primerMes){
											System.out.println("Mes Remesa 1" + remesaPrograma.getMes());
											for(DiaVO dia : remesaPrograma.getDias()){
												if(!dia.isBloqueado() && !mesConMontoAsignado){
													System.out.println("dia.getDia()=" + dia.getDia());
													dia.setMonto(montoRemesa);
													mesConMontoAsignado = true;
												}
											}
											primerMes = false;
										}else{
											System.out.println("Mes Remesa 2" + remesaPrograma.getMes());
											if(!mesConMontoAsignado){
												for(DiaVO dia : remesaPrograma.getDias()){
													if(!dia.isBloqueado() && !mesConMontoAsignado){
														System.out.println("dia.getDia()=" + dia.getDia());
														dia.setMonto(montoRemesa);
														mesConMontoAsignado = true;
													}
												}
											}
										}
									} 
								}
							}
							if(!existeRemesaMesActual){
								continue;
							}
						}
					}else{
						if(remesaPendiente && !remesaPagada){
							
							Integer cuotaAPagar = 0;
							for(Cuota cuota : cuotasPorPrograma){
								if((cuota.getIdMes() == null) || (cuota.getIdMes().getIdMes().intValue() <= mes.intValue())){
									cuotaAPagar++;
								}
							}
							//debo recalcular
							List<Integer> idDetalleRemesaEliminar = new ArrayList<Integer>();
							DetalleRemesas detalleRemesaSeleccionado = null;
							for(DetalleRemesas detalleRemesa : remesasPendientesProgramaAnoComponenteEstablecimientoSubtitulo){
								idDetalleRemesaEliminar.add(detalleRemesa.getIdDetalleRemesa());
								if(detalleRemesaSeleccionado == null){
									detalleRemesaSeleccionado = detalleRemesa;
								}
							}
							
							boolean remesaAprobadaParaMes = false;
							for(RemesasProgramaVO remesaPrograma : remesas){
								for(DiaVO dia : remesaPrograma.getDias()){
									if(detalleRemesaSeleccionado.getDia().getDia().equals(dia.getDia()) && 
											detalleRemesaSeleccionado.getMes().getIdMes().equals(remesaPrograma.getIdMes())){
										remesaAprobadaParaMes = true;
										break;
									}
								}
							}
							
							if(remesaAprobadaParaMes){
								continue;
							}
							oTResumenDependienteServicioVO.setIdDetalleRemesaEliminar(idDetalleRemesaEliminar);
							List<ConvenioServicioComponente> conveniosEnTramiteEstablecimiento = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(
									idProgramaAno, componenteSeleccionado, idTipoSubtitulo, establecimiento.getId(), EstadosConvenios.TRAMITE.getId());
							if(conveniosEnTramiteEstablecimiento != null && conveniosEnTramiteEstablecimiento.size() > 0){
								int sizeConvenioSerivicioComponente = conveniosEnTramiteEstablecimiento.size();
								ConvenioServicioComponente convenioEnTramiteEstablecimiento = conveniosEnTramiteEstablecimiento.get(sizeConvenioSerivicioComponente - 1);
								oTResumenDependienteServicioVO.setIdConvenioServicio(convenioEnTramiteEstablecimiento.getConvenioServicio().getIdConvenioServicio());
								oTResumenDependienteServicioVO.setIdConvenioServicioComponenteSinAprobar(convenioEnTramiteEstablecimiento.getIdConvenioServicioComponente());
								oTResumenDependienteServicioVO.setTransferenciaAcumulada(0L);
								oTResumenDependienteServicioVO.setDiferencia(marcoPresupuestario - 0L);
								Long montoRemesa = 0L;
								for(int cuotaActual = 1; cuotaActual <= cuotaAPagar; cuotaActual++){
									Cuota cuota = cuotasPorPrograma.get((cuotaActual - 1));
									Long montoCuota = Math.round(convenioEnTramiteEstablecimiento.getMontoIngresado() * (cuota.getPorcentaje() / 100.0));	
									montoRemesa += montoCuota;
									oTResumenDependienteServicioVO.setCuotaFinal(cuota.getId());
								}
								boolean primerMes = true;
								boolean mesConMontoAsignado = false;
								for(RemesasProgramaVO remesaPrograma : remesas){
									if(primerMes){
										System.out.println("Mes Remesa 1" + remesaPrograma.getMes());
										for(DiaVO dia : remesaPrograma.getDias()){
											if(!dia.isBloqueado() && !mesConMontoAsignado){
												System.out.println("dia.getDia()=" + dia.getDia());
												
												dia.setMonto(montoRemesa);
												mesConMontoAsignado = true;
											}
										}
										primerMes = false;
									}else{
										System.out.println("Mes Remesa 2" + remesaPrograma.getMes());
										if(!mesConMontoAsignado){
											for(DiaVO dia : remesaPrograma.getDias()){
												if(!dia.isBloqueado() && !mesConMontoAsignado){
													System.out.println("dia.getDia()=" + dia.getDia());
													dia.setMonto(montoRemesa);
													mesConMontoAsignado = true;
												}
											}
										}
									}
								}
							} else {
								boolean existeRemesaMesActual = false;
								List<ConvenioServicioComponente> conveniosAprobadosEstablecimiento = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(
										idProgramaAno, componenteSeleccionado, idTipoSubtitulo, establecimiento.getId(), EstadosConvenios.APROBADO.getId());
								if(conveniosAprobadosEstablecimiento != null && conveniosAprobadosEstablecimiento.size() > 0){
									existeRemesaMesActual = true;
									int sizeConveniosAprobadosEstablecimiento = conveniosAprobadosEstablecimiento.size();
									ConvenioServicioComponente convenioAprobadoEstablecimiento = conveniosAprobadosEstablecimiento.get(sizeConveniosAprobadosEstablecimiento - 1);
									oTResumenDependienteServicioVO.setIdConvenioServicio(convenioAprobadoEstablecimiento.getConvenioServicio().getIdConvenioServicio());
									oTResumenDependienteServicioVO.setIdConvenioServicioComponenteSinAprobar(convenioAprobadoEstablecimiento.getIdConvenioServicioComponente());
									oTResumenDependienteServicioVO.setTransferenciaAcumulada(0L);
									oTResumenDependienteServicioVO.setDiferencia(marcoPresupuestario - 0L);
									Long montoRemesa = 0L;
									for(int cuotaActual = 1; cuotaActual <= cuotaAPagar; cuotaActual++){
										Cuota cuota = cuotasPorPrograma.get((cuotaActual - 1));
										Long montoCuota = Math.round(convenioAprobadoEstablecimiento.getMontoIngresado() * (cuota.getPorcentaje() / 100.0));	
										montoRemesa += montoCuota;
										oTResumenDependienteServicioVO.setCuotaFinal(cuota.getId());
									}
									boolean primerMes = true;
									boolean mesConMontoAsignado = false;
									for(RemesasProgramaVO remesaPrograma : remesas){
										if(primerMes){
											System.out.println("Mes Remesa 1" + remesaPrograma.getMes());
											for(DiaVO dia : remesaPrograma.getDias()){
												if(!dia.isBloqueado() && !mesConMontoAsignado){
													System.out.println("dia.getDia()=" + dia.getDia());
													dia.setMonto(montoRemesa);
													mesConMontoAsignado = true;
												}
											}
											primerMes = false;
										}else{
											System.out.println("Mes Remesa 2" + remesaPrograma.getMes());
											if(!mesConMontoAsignado){
												for(DiaVO dia : remesaPrograma.getDias()){
													if(!dia.isBloqueado() && !mesConMontoAsignado){
														System.out.println("dia.getDia()=" + dia.getDia());
														dia.setMonto(montoRemesa);
														mesConMontoAsignado = true;
													}
												}
											}
										}
									} 
								}
								if(!existeRemesaMesActual){
									List<ConvenioServicioComponente> conveniosPagadosEstablecimiento = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(
										idProgramaAno, componenteSeleccionado, idTipoSubtitulo, establecimiento.getId(), EstadosConvenios.PAGADO.getId());
									if(conveniosPagadosEstablecimiento != null && conveniosPagadosEstablecimiento.size() > 0){
										existeRemesaMesActual = true;
										int sizeConveniosPagadosEstablecimiento = conveniosPagadosEstablecimiento.size();
										ConvenioServicioComponente convenioPagadoEstablecimiento = conveniosPagadosEstablecimiento.get(sizeConveniosPagadosEstablecimiento - 1);
										oTResumenDependienteServicioVO.setIdConvenioServicio(convenioPagadoEstablecimiento.getConvenioServicio().getIdConvenioServicio());
										oTResumenDependienteServicioVO.setIdConvenioServicioComponenteSinAprobar(convenioPagadoEstablecimiento.getIdConvenioServicioComponente());
										oTResumenDependienteServicioVO.setTransferenciaAcumulada(0L);
										oTResumenDependienteServicioVO.setDiferencia(marcoPresupuestario - 0L);
										Long montoRemesa = 0L;
										for(int cuotaActual = 1; cuotaActual <= cuotaAPagar; cuotaActual++){
											Cuota cuota = cuotasPorPrograma.get((cuotaActual-1));
											Long montoCuota = Math.round(convenioPagadoEstablecimiento.getMontoIngresado() * (cuota.getPorcentaje() / 100.0));	
											montoRemesa += montoCuota;
											oTResumenDependienteServicioVO.setCuotaFinal(cuota.getId());
										}
										boolean primerMes = true;
										boolean mesConMontoAsignado = false;
										for(RemesasProgramaVO remesaPrograma : remesas){
											if(primerMes){
												System.out.println("Mes Remesa 1" + remesaPrograma.getMes());
												for(DiaVO dia : remesaPrograma.getDias()){
													if(!dia.isBloqueado() && !mesConMontoAsignado){
														System.out.println("dia.getDia()=" + dia.getDia());
														dia.setMonto(montoRemesa);
														mesConMontoAsignado = true;
													}
												}
												primerMes = false;
											}else{
												System.out.println("Mes Remesa 2" + remesaPrograma.getMes());
												if(!mesConMontoAsignado){
													for(DiaVO dia : remesaPrograma.getDias()){
														if(!dia.isBloqueado() && !mesConMontoAsignado){
															System.out.println("dia.getDia()=" + dia.getDia());
															dia.setMonto(montoRemesa);
															mesConMontoAsignado = true;
														}
													}
												}
											}
										} 
									}
								}
								if(!existeRemesaMesActual){
									continue;
								}
							}
						}else if(!remesaPendiente && remesaPagada){
							
							Integer cuotaAPagar = null;
							for(Cuota cuota : cuotasPorPrograma){
								if((cuota.getIdMes() != null) && (cuota.getIdMes().getIdMes().intValue() == mes.intValue())){
									cuotaAPagar = new Integer(cuota.getNumeroCuota());
								}
							}
							if(cuotaAPagar == null){
								continue;
							}
							Long montoTransferido = 0L;
							int cuotaNro = 0;
							for(DetalleRemesas detalleRemesa : remesasPagadasProgramaAnoComponenteEstablecimientoSubtitulo){
								montoTransferido += detalleRemesa.getMontoRemesa();
								cuotaNro = detalleRemesa.getCuota().getNumeroCuota();
							}
							List<ConvenioServicioComponente> conveniosEnTramiteEstablecimiento = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(
									idProgramaAno, componenteSeleccionado, idTipoSubtitulo, establecimiento.getId(), EstadosConvenios.TRAMITE.getId());
							if(conveniosEnTramiteEstablecimiento != null && conveniosEnTramiteEstablecimiento.size() > 0){
								int sizeConvenioSerivicioComponente = conveniosEnTramiteEstablecimiento.size();
								ConvenioServicioComponente convenioEnTramiteEstablecimiento = conveniosEnTramiteEstablecimiento.get(sizeConvenioSerivicioComponente - 1);
								oTResumenDependienteServicioVO.setIdConvenioServicio(convenioEnTramiteEstablecimiento.getConvenioServicio().getIdConvenioServicio());
								oTResumenDependienteServicioVO.setIdConvenioServicioComponenteSinAprobar(convenioEnTramiteEstablecimiento.getIdConvenioServicioComponente());
								oTResumenDependienteServicioVO.setTransferenciaAcumulada(montoTransferido);
								oTResumenDependienteServicioVO.setDiferencia(marcoPresupuestario - montoTransferido);
								
								Long montoRemesaAnteriorCorregido = 0L;
								for(int cuotaActual = 1; cuotaActual <= cuotaNro; cuotaActual++){
									Cuota cuota = cuotasPorPrograma.get((cuotaActual-1));
									Long montoCuota = Math.round(convenioEnTramiteEstablecimiento.getMontoIngresado() * (cuota.getPorcentaje() / 100.0));	
									montoRemesaAnteriorCorregido += montoCuota;
								}
								montoRemesaAnteriorCorregido -= montoTransferido;
								
								Long montoRemesa = 0L;
								for(int cuotaActual = (cuotaNro + 1); cuotaActual <= cuotaAPagar; cuotaActual++){
									Cuota cuota = cuotasPorPrograma.get((cuotaActual-1));
									Long montoCuota = Math.round(convenioEnTramiteEstablecimiento.getMontoIngresado() * (cuota.getPorcentaje() / 100.0));	
									montoRemesa += montoCuota;
									oTResumenDependienteServicioVO.setCuotaFinal(cuota.getId());
								}
								montoRemesa += montoRemesaAnteriorCorregido;
								
								boolean primerMes = true;
								boolean mesConMontoAsignado = false;
								for(RemesasProgramaVO remesaPrograma : remesas){
									if(primerMes){
										System.out.println("Mes Remesa 1" + remesaPrograma.getMes());
										for(DiaVO dia : remesaPrograma.getDias()){
											if(!dia.isBloqueado() && !mesConMontoAsignado){
												System.out.println("dia.getDia()=" + dia.getDia());
												dia.setMonto(montoRemesa);
												mesConMontoAsignado = true;
											}
										}
										primerMes = false;
									}else{
										System.out.println("Mes Remesa 2" + remesaPrograma.getMes());
										if(!mesConMontoAsignado){
											for(DiaVO dia : remesaPrograma.getDias()){
												if(!dia.isBloqueado() && !mesConMontoAsignado){
													System.out.println("dia.getDia()=" + dia.getDia());
													dia.setMonto(montoRemesa);
													mesConMontoAsignado = true;
												}
											}
										}
									}
								}
							} else {
								boolean existeRemesaMesActual = false;
								List<ConvenioServicioComponente> conveniosAprobadosEstablecimiento = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(
										idProgramaAno, componenteSeleccionado, idTipoSubtitulo, establecimiento.getId(), EstadosConvenios.APROBADO.getId());
								if(conveniosAprobadosEstablecimiento != null && conveniosAprobadosEstablecimiento.size() > 0){
									existeRemesaMesActual = true;
									int sizeConveniosAprobadosEstablecimiento = conveniosAprobadosEstablecimiento.size();
									ConvenioServicioComponente convenioAprobadoEstablecimiento = conveniosAprobadosEstablecimiento.get(sizeConveniosAprobadosEstablecimiento - 1);
									oTResumenDependienteServicioVO.setIdConvenioServicio(convenioAprobadoEstablecimiento.getConvenioServicio().getIdConvenioServicio());
									oTResumenDependienteServicioVO.setIdConvenioServicioComponenteSinAprobar(convenioAprobadoEstablecimiento.getIdConvenioServicioComponente());
									oTResumenDependienteServicioVO.setTransferenciaAcumulada(montoTransferido);
									oTResumenDependienteServicioVO.setDiferencia(marcoPresupuestario - montoTransferido);
									
									Long montoRemesaAnteriorCorregido = 0L;
									for(int cuotaActual = 1; cuotaActual <= cuotaNro; cuotaActual++){
										Cuota cuota = cuotasPorPrograma.get((cuotaActual-1));
										Long montoCuota = Math.round(convenioAprobadoEstablecimiento.getMontoIngresado() * (cuota.getPorcentaje() / 100.0));	
										montoRemesaAnteriorCorregido += montoCuota;
									}
									montoRemesaAnteriorCorregido -= montoTransferido;
									
									Long montoRemesa = 0L;
									for(int cuotaActual = (cuotaNro + 1); cuotaActual <= cuotaAPagar; cuotaActual++){
										Cuota cuota = cuotasPorPrograma.get((cuotaActual-1));
										Long montoCuota = Math.round(convenioAprobadoEstablecimiento.getMontoIngresado() * (cuota.getPorcentaje() / 100.0));	
										montoRemesa += montoCuota;
										oTResumenDependienteServicioVO.setCuotaFinal(cuota.getId());
									}
									montoRemesa += montoRemesaAnteriorCorregido;
									
									boolean primerMes = true;
									boolean mesConMontoAsignado = false;
									for(RemesasProgramaVO remesaPrograma : remesas){
										if(primerMes){
											System.out.println("Mes Remesa 1" + remesaPrograma.getMes());
											for(DiaVO dia : remesaPrograma.getDias()){
												if(!dia.isBloqueado() && !mesConMontoAsignado){
													System.out.println("dia.getDia()=" + dia.getDia());
													dia.setMonto(montoRemesa);
													mesConMontoAsignado = true;
												}
											}
											primerMes = false;
										}else{
											System.out.println("Mes Remesa 2" + remesaPrograma.getMes());
											if(!mesConMontoAsignado){
												for(DiaVO dia : remesaPrograma.getDias()){
													if(!dia.isBloqueado() && !mesConMontoAsignado){
														System.out.println("dia.getDia()=" + dia.getDia());
														dia.setMonto(montoRemesa);
														mesConMontoAsignado = true;
													}
												}
											}
										}
									} 
								}
								if(!existeRemesaMesActual){
									List<ConvenioServicioComponente> conveniosPagadosEstablecimiento = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(
										idProgramaAno, componenteSeleccionado, idTipoSubtitulo, establecimiento.getId(), EstadosConvenios.PAGADO.getId());
									if(conveniosPagadosEstablecimiento != null && conveniosPagadosEstablecimiento.size() > 0){
										existeRemesaMesActual = true;
										int sizeConveniosPagadosEstablecimiento = conveniosPagadosEstablecimiento.size();
										ConvenioServicioComponente convenioPagadoEstablecimiento = conveniosPagadosEstablecimiento.get(sizeConveniosPagadosEstablecimiento - 1);
										oTResumenDependienteServicioVO.setIdConvenioServicio(convenioPagadoEstablecimiento.getConvenioServicio().getIdConvenioServicio());
										oTResumenDependienteServicioVO.setIdConvenioServicioComponenteSinAprobar(convenioPagadoEstablecimiento.getIdConvenioServicioComponente());
										oTResumenDependienteServicioVO.setTransferenciaAcumulada(montoTransferido);
										oTResumenDependienteServicioVO.setDiferencia(marcoPresupuestario - montoTransferido);
										Long montoRemesa = 0L;
										for(int cuotaActual = (cuotaNro + 1); cuotaActual <= cuotaAPagar; cuotaActual++){
											Cuota cuota = cuotasPorPrograma.get((cuotaActual - 1));
											Long montoCuota = Math.round(convenioPagadoEstablecimiento.getMontoIngresado() * (cuota.getPorcentaje() / 100.0));	
											montoRemesa += montoCuota;
											oTResumenDependienteServicioVO.setCuotaFinal(cuota.getId());
										}
										boolean primerMes = true;
										boolean mesConMontoAsignado = false;
										for(RemesasProgramaVO remesaPrograma : remesas){
											if(primerMes){
												System.out.println("Mes Remesa 1" + remesaPrograma.getMes());
												for(DiaVO dia : remesaPrograma.getDias()){
													if(!dia.isBloqueado() && !mesConMontoAsignado){
														System.out.println("dia.getDia()=" + dia.getDia());
														dia.setMonto(montoRemesa);
														mesConMontoAsignado = true;
													}
												}
												primerMes = false;
											}else{
												System.out.println("Mes Remesa 2" + remesaPrograma.getMes());
												if(!mesConMontoAsignado){
													for(DiaVO dia : remesaPrograma.getDias()){
														if(!dia.isBloqueado() && !mesConMontoAsignado){
															System.out.println("dia.getDia()=" + dia.getDia());
															dia.setMonto(montoRemesa);
															mesConMontoAsignado = true;
														}
													}
												}
											}
										} 
									}
								}
								if(!existeRemesaMesActual){
									continue;
								}
							}
						}else if(remesaPendiente && remesaPagada){
							Integer cuotaAPagar = null;
							for(Cuota cuota : cuotasPorPrograma){
								if((cuota.getIdMes() != null) && (cuota.getIdMes().getIdMes().intValue() == mes.intValue())){
									cuotaAPagar = new Integer(cuota.getNumeroCuota());
								}
							}
				
							Long montoTransferido = 0L;
							int cuotaNro = 0;
							
							for(DetalleRemesas detalleRemesa : remesasPagadasProgramaAnoComponenteEstablecimientoSubtitulo){
								montoTransferido += detalleRemesa.getMontoRemesa();
								cuotaNro = detalleRemesa.getCuota().getNumeroCuota();
							}
							//debo recalcular
							List<Integer> idDetalleRemesaEliminar = new ArrayList<Integer>();
							int cuotaPendienteNro = 0;
							DetalleRemesas detalleRemesaSeleccionado = null;
							for(DetalleRemesas detalleRemesa : remesasPendientesProgramaAnoComponenteEstablecimientoSubtitulo){
								idDetalleRemesaEliminar.add(detalleRemesa.getIdDetalleRemesa());
								cuotaPendienteNro = detalleRemesa.getCuota().getNumeroCuota();
								if(detalleRemesaSeleccionado == null){
									detalleRemesaSeleccionado = detalleRemesa;
								}
							}
							
							boolean remesaAprobadaParaMes = false;
							for(RemesasProgramaVO remesaPrograma : remesas){
								for(DiaVO dia : remesaPrograma.getDias()){
									if(detalleRemesaSeleccionado.getDia().getDia().equals(dia.getDia()) && 
											detalleRemesaSeleccionado.getMes().getIdMes().equals(remesaPrograma.getIdMes())){
										remesaAprobadaParaMes = true;
										break;
									}
								}
							}
							
							if(remesaAprobadaParaMes){
								continue;
							}
							
							if(cuotaAPagar == null){
								cuotaAPagar = cuotaPendienteNro;
							}
							
							oTResumenDependienteServicioVO.setIdDetalleRemesaEliminar(idDetalleRemesaEliminar);
							List<ConvenioServicioComponente> conveniosEnTramiteEstablecimiento = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(
									idProgramaAno, componenteSeleccionado, idTipoSubtitulo, establecimiento.getId(), EstadosConvenios.TRAMITE.getId());
							if(conveniosEnTramiteEstablecimiento != null && conveniosEnTramiteEstablecimiento.size() > 0){
								int sizeConvenioSerivicioComponente = conveniosEnTramiteEstablecimiento.size();
								ConvenioServicioComponente convenioEnTramiteEstablecimiento = conveniosEnTramiteEstablecimiento.get(sizeConvenioSerivicioComponente - 1);
								oTResumenDependienteServicioVO.setIdConvenioServicio(convenioEnTramiteEstablecimiento.getConvenioServicio().getIdConvenioServicio());
								oTResumenDependienteServicioVO.setIdConvenioServicioComponenteSinAprobar(convenioEnTramiteEstablecimiento.getIdConvenioServicioComponente());
								oTResumenDependienteServicioVO.setTransferenciaAcumulada(montoTransferido);
								oTResumenDependienteServicioVO.setDiferencia(marcoPresupuestario - montoTransferido);
								
								Long montoRemesaAnteriorCorregido = 0L;
								for(int cuotaActual = 1; cuotaActual <= cuotaNro; cuotaActual++){
									Cuota cuota = cuotasPorPrograma.get((cuotaActual-1));
									Long montoCuota = Math.round(convenioEnTramiteEstablecimiento.getMontoIngresado() * (cuota.getPorcentaje() / 100.0));	
									montoRemesaAnteriorCorregido += montoCuota;
								}
								montoRemesaAnteriorCorregido -= montoTransferido;
								
								Long montoRemesa = 0L;
								for(int cuotaActual = (cuotaNro + 1); cuotaActual <= cuotaAPagar; cuotaActual++){
									Cuota cuota = cuotasPorPrograma.get((cuotaActual-1));
									Long montoCuota = Math.round(convenioEnTramiteEstablecimiento.getMontoIngresado() * (cuota.getPorcentaje() / 100.0));	
									montoRemesa += montoCuota;
									oTResumenDependienteServicioVO.setCuotaFinal(cuota.getId());
								}
								montoRemesa += montoRemesaAnteriorCorregido;
								
								boolean primerMes = true;
								boolean mesConMontoAsignado = false;
								for(RemesasProgramaVO remesaPrograma : remesas){
									if(primerMes){
										System.out.println("Mes Remesa 1" + remesaPrograma.getMes());
										for(DiaVO dia : remesaPrograma.getDias()){
											if(!dia.isBloqueado() && !mesConMontoAsignado){
												System.out.println("dia.getDia()=" + dia.getDia());
												dia.setMonto(montoRemesa);
												mesConMontoAsignado = true;
											}
										}
										primerMes = false;
									}else{
										System.out.println("Mes Remesa 2" + remesaPrograma.getMes());
										if(!mesConMontoAsignado){
											for(DiaVO dia : remesaPrograma.getDias()){
												if(!dia.isBloqueado() && !mesConMontoAsignado){
													System.out.println("dia.getDia()=" + dia.getDia());
													dia.setMonto(montoRemesa);
													mesConMontoAsignado = true;
												}
											}
										}
									}
								}
							} else {
								boolean existeRemesaMesActual = false;
								List<ConvenioServicioComponente> conveniosAprobadosEstablecimiento = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(
										idProgramaAno, componenteSeleccionado, idTipoSubtitulo, establecimiento.getId(), EstadosConvenios.APROBADO.getId());
								if(conveniosAprobadosEstablecimiento != null && conveniosAprobadosEstablecimiento.size() > 0){
									existeRemesaMesActual = true;
									int sizeConveniosAprobadosEstablecimiento = conveniosAprobadosEstablecimiento.size();
									ConvenioServicioComponente convenioAprobadoEstablecimiento = conveniosAprobadosEstablecimiento.get(sizeConveniosAprobadosEstablecimiento - 1);
									oTResumenDependienteServicioVO.setIdConvenioServicio(convenioAprobadoEstablecimiento.getConvenioServicio().getIdConvenioServicio());
									oTResumenDependienteServicioVO.setIdConvenioServicioComponenteSinAprobar(convenioAprobadoEstablecimiento.getIdConvenioServicioComponente());
									oTResumenDependienteServicioVO.setTransferenciaAcumulada(montoTransferido);
									oTResumenDependienteServicioVO.setDiferencia(marcoPresupuestario - montoTransferido);
									
									Long montoRemesaAnteriorCorregido = 0L;
									for(int cuotaActual = 1; cuotaActual <= cuotaNro; cuotaActual++){
										Cuota cuota = cuotasPorPrograma.get((cuotaActual-1));
										Long montoCuota = Math.round(convenioAprobadoEstablecimiento.getMontoIngresado() * (cuota.getPorcentaje() / 100.0));	
										montoRemesaAnteriorCorregido += montoCuota;
									}
									montoRemesaAnteriorCorregido -= montoTransferido;
									
									Long montoRemesa = 0L;
									for(int cuotaActual = (cuotaNro + 1); cuotaActual <= cuotaAPagar; cuotaActual++){
										Cuota cuota = cuotasPorPrograma.get((cuotaActual-1));
										Long montoCuota = Math.round(convenioAprobadoEstablecimiento.getMontoIngresado() * (cuota.getPorcentaje() / 100.0));	
										montoRemesa += montoCuota;
										oTResumenDependienteServicioVO.setCuotaFinal(cuota.getId());
									}
									montoRemesa += montoRemesaAnteriorCorregido;
									
									boolean primerMes = true;
									boolean mesConMontoAsignado = false;
									for(RemesasProgramaVO remesaPrograma : remesas){
										if(primerMes){
											System.out.println("Mes Remesa 1" + remesaPrograma.getMes());
											for(DiaVO dia : remesaPrograma.getDias()){
												if(!dia.isBloqueado() && !mesConMontoAsignado){
													System.out.println("dia.getDia()=" + dia.getDia());
													dia.setMonto(montoRemesa);
													mesConMontoAsignado = true;
												}
											}
											primerMes = false;
										}else{
											System.out.println("Mes Remesa 2" + remesaPrograma.getMes());
											if(!mesConMontoAsignado){
												for(DiaVO dia : remesaPrograma.getDias()){
													if(!dia.isBloqueado() && !mesConMontoAsignado){
														System.out.println("dia.getDia()=" + dia.getDia());
														dia.setMonto(montoRemesa);
														mesConMontoAsignado = true;
													}
												}
											}
										}
									} 
								}
								if(!existeRemesaMesActual){
									List<ConvenioServicioComponente> conveniosPagadosEstablecimiento = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(
										idProgramaAno, componenteSeleccionado, idTipoSubtitulo, establecimiento.getId(), EstadosConvenios.PAGADO.getId());
									if(conveniosPagadosEstablecimiento != null && conveniosPagadosEstablecimiento.size() > 0){
										existeRemesaMesActual = true;
										int sizeConveniosPagadosEstablecimiento = conveniosPagadosEstablecimiento.size();
										ConvenioServicioComponente convenioPagadoEstablecimiento = conveniosPagadosEstablecimiento.get(sizeConveniosPagadosEstablecimiento - 1);
										oTResumenDependienteServicioVO.setIdConvenioServicio(convenioPagadoEstablecimiento.getConvenioServicio().getIdConvenioServicio());
										oTResumenDependienteServicioVO.setIdConvenioServicioComponenteSinAprobar(convenioPagadoEstablecimiento.getIdConvenioServicioComponente());
										oTResumenDependienteServicioVO.setTransferenciaAcumulada(montoTransferido);
										oTResumenDependienteServicioVO.setDiferencia(marcoPresupuestario - montoTransferido);
										
										Long montoRemesa = 0L;
										for(int cuotaActual = (cuotaNro + 1); cuotaActual <= cuotaAPagar; cuotaActual++){
											Cuota cuota = cuotasPorPrograma.get((cuotaActual-1));
											Long montoCuota = Math.round(convenioPagadoEstablecimiento.getMontoIngresado() * (cuota.getPorcentaje() / 100.0));	
											montoRemesa += montoCuota;
											oTResumenDependienteServicioVO.setCuotaFinal(cuota.getId());
										}
										
										boolean primerMes = true;
										boolean mesConMontoAsignado = false;
										for(RemesasProgramaVO remesaPrograma : remesas){
											if(primerMes){
												System.out.println("Mes Remesa 1" + remesaPrograma.getMes());
												for(DiaVO dia : remesaPrograma.getDias()){
													if(!dia.isBloqueado() && !mesConMontoAsignado){
														System.out.println("dia.getDia()=" + dia.getDia());
														dia.setMonto(montoRemesa);
														mesConMontoAsignado = true;
													}
												}
												primerMes = false;
											}else{
												System.out.println("Mes Remesa 2" + remesaPrograma.getMes());
												if(!mesConMontoAsignado){
													for(DiaVO dia : remesaPrograma.getDias()){
														if(!dia.isBloqueado() && !mesConMontoAsignado){
															System.out.println("dia.getDia()=" + dia.getDia());
															dia.setMonto(montoRemesa);
															mesConMontoAsignado = true;
														}
													}
												}
											}
										} 
									}
								}
								if(!existeRemesaMesActual){
									continue;
								}
							}
						}
					}
					oTResumenDependienteServicioVO.setRemesas(remesas);
					listaOTResumenDependienteServicioVO.add(oTResumenDependienteServicioVO);
				}
			}
		}
		return listaOTResumenDependienteServicioVO;

	}

	public List<OTResumenDependienteServicioVO> getDetalleOTServicioConsolidador(Integer componenteSeleccionado,
			Integer servicioSeleccionado, Integer idTipoSubtitulo, Integer idProgramaAno) {
		System.out.println("getDetalleOTServicioConsolidador componenteSeleccionado->"+componenteSeleccionado);
		System.out.println("getDetalleOTServicioConsolidador servicioSeleccionado->"+servicioSeleccionado);
		System.out.println("getDetalleOTServicioConsolidador idTipoSubtitulo->"+idTipoSubtitulo);
		System.out.println("getDetalleOTServicioConsolidador idProgramaAno->"+idProgramaAno);
		List<OTResumenDependienteServicioVO> listaOTResumenDependienteServicioVO = new ArrayList<OTResumenDependienteServicioVO>();
		ProgramaVO programaVO = programasService.getProgramaAno(idProgramaAno);
		List<Cuota> cuotasPrograma = reliquidacionDAO.getCuotasByProgramaAno(idProgramaAno);
		
		List<ServicioSalud> servicios = null;
		if(servicioSeleccionado == null){
			servicios = servicioSaludDAO.getServiciosOrderId();
		}else{
			ServicioSalud servicioSalud = servicioSaludDAO.getById(servicioSeleccionado);
			servicios = new ArrayList<ServicioSalud>();
			servicios.add(servicioSalud);
		}
		
		for(ServicioSalud servicioSalud : servicios){
			if(servicioSalud.getEstablecimientos() != null && servicioSalud.getEstablecimientos().size() > 0){
				for(Establecimiento establecimiento : servicioSalud.getEstablecimientos()){
					OTResumenDependienteServicioVO prog = new OTResumenDependienteServicioVO();
					EstablecimientoVO establecimientoVO = new EstablecimientoVO();
					establecimientoVO.setCodigoEstablecimiento(establecimiento.getCodigo());
					establecimientoVO.setId(establecimiento.getId());
					establecimientoVO.setId_servicio_salud(servicioSalud.getId());
					establecimientoVO.setNombre(establecimiento.getNombre());
					prog.setEstablecimiento(establecimientoVO);
					Map<Integer, RemesasProgramaVO> remesasPorMes = new HashMap<Integer, RemesasProgramaVO>();
					List<DetalleRemesas> remesasPendientes = remesasDAO.getRemesasPendientesConsolidadorProgramaAnoComponenteSubtituloEstablecimiento(idProgramaAno, componenteSeleccionado, idTipoSubtitulo, establecimiento.getId());
					System.out.println("establecimiento.getId()=" + establecimiento.getId() + " remesasPendientes.size()=" + ((remesasPendientes == null) ? 0 : remesasPendientes.size()));
					if(remesasPendientes != null && remesasPendientes.size() > 0){
						List<DetalleRemesas> remesasPagadasProgramaAnoComponenteComunaSubtitulo = remesasDAO.getRemesasPagadasByProgramaAnoComponenteEstablecimientoSubtitulo(idProgramaAno, componenteSeleccionado, establecimiento.getId(), idTipoSubtitulo);
						
						Long marcoPresupuestario = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAno, componenteSeleccionado, idTipoSubtitulo);
						prog.setMarcoPresupuestario(marcoPresupuestario);
						Long montoTransferido = 0L;
						if(remesasPagadasProgramaAnoComponenteComunaSubtitulo != null && remesasPagadasProgramaAnoComponenteComunaSubtitulo.size() > 0){
							for(DetalleRemesas detalleRemesa : remesasPagadasProgramaAnoComponenteComunaSubtitulo){
								montoTransferido += detalleRemesa.getMontoRemesa();
							}
						}
						prog.setTransferenciaAcumulada(montoTransferido);
						
						List<ConvenioServicioComponente> conveniosTramiteEstablecimiento = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(
								idProgramaAno, componenteSeleccionado, idTipoSubtitulo, establecimiento.getId(), EstadosConvenios.TRAMITE.getId());
						
						prog.setConveniosRecibidos(0L);
						if(conveniosTramiteEstablecimiento != null && conveniosTramiteEstablecimiento.size() > 0){
							int sizeConveniosTramiteEstablecimiento = conveniosTramiteEstablecimiento.size();
							ConvenioServicioComponente convenioTramiteEstablecimiento = conveniosTramiteEstablecimiento.get(sizeConveniosTramiteEstablecimiento - 1);
							prog.setConveniosRecibidos(new Long(convenioTramiteEstablecimiento.getMontoIngresado()));
						}else{
							List<ConvenioServicioComponente> conveniosPagadosEstablecimiento = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(
									idProgramaAno, componenteSeleccionado, idTipoSubtitulo, establecimiento.getId(), EstadosConvenios.PAGADO.getId());
							if(conveniosPagadosEstablecimiento != null && conveniosPagadosEstablecimiento.size() > 0){
								int sizeConveniosPagadosEstablecimiento = conveniosPagadosEstablecimiento.size();
								ConvenioServicioComponente convenioPagadosEstablecimiento = conveniosPagadosEstablecimiento.get(sizeConveniosPagadosEstablecimiento - 1);
								prog.setConveniosRecibidos(new Long(convenioPagadosEstablecimiento.getMontoIngresado()));
							}
						}
						
						List<Integer> idDetalleRemesaAProbarConsolidador = new ArrayList<Integer>();
						for(DetalleRemesas remesaPendienteMes : remesasPendientes){
							idDetalleRemesaAProbarConsolidador.add(remesaPendienteMes.getIdDetalleRemesa());
							if(remesasPorMes.get(remesaPendienteMes.getMes().getIdMes()) == null){
								RemesasProgramaVO detalleRemesa = new RemesasProgramaVO();
								Mes mes = mesDAO.getMesPorID(remesaPendienteMes.getMes().getIdMes());
								detalleRemesa.setIdMes(mes.getIdMes());
								detalleRemesa.setMes(mes.getNombre());
								List<DiaVO> dias = new ArrayList<DiaVO>();
								DiaVO dia = new DiaVO();
								dia.setBloqueado(remesaPendienteMes.isBloqueado());
								dia.setDia(remesaPendienteMes.getDia().getId());
								dia.setIdDia(remesaPendienteMes.getDia().getId());
								dia.setMonto(new Long(remesaPendienteMes.getMontoRemesa()));
								dias.add(dia);
								detalleRemesa.setDias(dias);
								remesasPorMes.put(remesaPendienteMes.getMes().getIdMes(), detalleRemesa);
							}else{
								DiaVO dia = new DiaVO();
								dia.setBloqueado(remesaPendienteMes.isBloqueado());
								dia.setDia(remesaPendienteMes.getDia().getId());
								dia.setIdDia(remesaPendienteMes.getDia().getId());
								dia.setMonto(new Long(remesaPendienteMes.getMontoRemesa()));
								remesasPorMes.get(remesaPendienteMes.getMes().getIdMes()).getDias().add(dia);
							}
						}
						prog.setIdDetalleRemesaAProbarConsolidador(idDetalleRemesaAProbarConsolidador);
						List<RemesasProgramaVO> remesas = new ArrayList<RemesasProgramaVO>();
						for (Integer key : remesasPorMes.keySet()) {
							RemesasProgramaVO remesasProgramaVO = remesasPorMes.get(key);
							remesas.add(remesasProgramaVO);
						}
						prog.setRemesas(remesas);
						
						listaOTResumenDependienteServicioVO.add(prog);
					}
				}
			}
		}
		return listaOTResumenDependienteServicioVO;

	}

	public List<OTResumenMunicipalVO> getDetalleOTMunicipal(Integer componenteSeleccionado, Integer servicioSeleccionado, Integer idProgramaAno) {
		System.out.println("Buscando Detalle de Convenios/Remesas para Subtitulo 24");
		System.out.println("Buscando Detalle de Convenios/Remesas para componenteSeleccionado = "+ componenteSeleccionado);
		System.out.println("Buscando Detalle de Convenios/Remesas para servicioSeleccionado = " + servicioSeleccionado);
		System.out.println("Buscando Detalle de Convenios/Remesas para idProgramaAno = " + idProgramaAno);

		List<OTResumenMunicipalVO> listaOtResumenMunicipalVO = new ArrayList<OTResumenMunicipalVO>();

		ProgramaVO programaVO = programasService.getProgramaAno(idProgramaAno);
		List<Cuota> cuotasPorPrograma = reliquidacionDAO.getCuotasByProgramaAno(idProgramaAno);

		Integer mes = Integer.parseInt(getMesCurso(true));
		
		List<ServicioSalud> servicios = null;
		if(servicioSeleccionado == null){
			servicios = servicioSaludDAO.getServiciosOrderId();
		}else{
			servicios = new ArrayList<ServicioSalud>();
			ServicioSalud servicioSalud = servicioSaludDAO.getById(servicioSeleccionado);
			servicios.add(servicioSalud);
		}
		
		
		for(ServicioSalud servicio : servicios){
			if(servicio.getComunas() != null && servicio.getComunas().size() > 0){
				for(Comuna comuna : servicio.getComunas()){
					
					List<DetalleRemesas> remesasPagadasProgramaAnoComponenteComunaSubtitulo = remesasDAO.getRemesasPagadasByProgramaAnoComponenteComunaSubtitulo(idProgramaAno, componenteSeleccionado, comuna.getId(), Subtitulo.SUBTITULO24.getId());
					List<DetalleRemesas> remesasPendientesProgramaAnoComponenteComunaSubtitulo = remesasDAO.getRemesasPendientesByProgramaAnoComponenteComunaSubtitulo(idProgramaAno, componenteSeleccionado, comuna.getId(), Subtitulo.SUBTITULO24.getId());
					
					Boolean primeraRemesaAprobadaProfesional = null;
					boolean remesaPendiente = false;
					boolean remesaPagada = false;
					if((remesasPagadasProgramaAnoComponenteComunaSubtitulo != null && remesasPagadasProgramaAnoComponenteComunaSubtitulo.size() > 0) ||
							(remesasPendientesProgramaAnoComponenteComunaSubtitulo != null && remesasPendientesProgramaAnoComponenteComunaSubtitulo.size() > 0)){
						primeraRemesaAprobadaProfesional = false;
						if(remesasPagadasProgramaAnoComponenteComunaSubtitulo != null && remesasPagadasProgramaAnoComponenteComunaSubtitulo.size() > 0){
							remesaPagada = true;
						}
						if(remesasPendientesProgramaAnoComponenteComunaSubtitulo != null && remesasPendientesProgramaAnoComponenteComunaSubtitulo.size() > 0){
							remesaPendiente = true;
						}
					}else{
						primeraRemesaAprobadaProfesional = true;
					}
					Long marcoPresupuestario = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAno, componenteSeleccionado, Subtitulo.SUBTITULO24.getId());
					List<RemesasProgramaVO> remesas = getRemesasPrograma(programaVO.getId(), mes, programaVO.getAno());
					OTResumenMunicipalVO oTResumenMunicipalVO = new OTResumenMunicipalVO();
					oTResumenMunicipalVO.setCreacion(false);
					oTResumenMunicipalVO.setMarcoPresupuestario(marcoPresupuestario);
					ComunaVO comunaVO = new ComunaVO();
					comunaVO.setIdComuna(comuna.getId());
					comunaVO.setNombre(comuna.getNombre());
					oTResumenMunicipalVO.setComuna(comunaVO);
					
					if(primeraRemesaAprobadaProfesional){
						Integer cuotaAPagar = 0;
						for(Cuota cuota : cuotasPorPrograma){
							if((cuota.getIdMes() == null) || (cuota.getIdMes().getIdMes().intValue() <= mes.intValue())){
								cuotaAPagar++;
							}
						}
						//revisa si hay convenios en tramite
						List<ConvenioComunaComponente> conveniosEnTramiteComuna = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloComunaEstadoConvenio(
								idProgramaAno, componenteSeleccionado, Subtitulo.SUBTITULO24.getId(), comuna.getId(), EstadosConvenios.TRAMITE.getId());
						ConvenioComunaComponente convenioEnTramiteComuna = ((conveniosEnTramiteComuna == null || conveniosEnTramiteComuna.size() == 0) ? null : conveniosEnTramiteComuna.get(conveniosEnTramiteComuna.size()-1));
						oTResumenMunicipalVO.setCreacion(true);
						if(convenioEnTramiteComuna != null){//significa que otro componente subtitulo fue aprobado por el profesional y no hay nuevos convenios aprobados
							oTResumenMunicipalVO.setIdConvenioComuna(convenioEnTramiteComuna.getConvenioComuna().getIdConvenioComuna());
							oTResumenMunicipalVO.setIdConvenioComunaComponenteSinAprobar(convenioEnTramiteComuna.getIdConvenioComunaComponente());
							oTResumenMunicipalVO.setTransferenciaAcumulada(0L);
							oTResumenMunicipalVO.setDiferencia(marcoPresupuestario - 0L);
							oTResumenMunicipalVO.setConveniosRecibidos(new Long(convenioEnTramiteComuna.getMontoIngresado()));
							Long montoRemesa = 0L;
							for(int cuotaActual = 1; cuotaActual <= cuotaAPagar; cuotaActual++){
								Cuota cuota = cuotasPorPrograma.get((cuotaActual - 1));
								Long montoCuota = Math.round(convenioEnTramiteComuna.getMontoIngresado() * (cuota.getPorcentaje() / 100.0));	
								montoRemesa += montoCuota;
								oTResumenMunicipalVO.setCuotaFinal(cuota.getId());
							}
							boolean primerMes = true;
							boolean mesConMontoAsignado = false;
							for(RemesasProgramaVO remesaPrograma : remesas){
								if(primerMes){
									System.out.println("Mes Remesa 1" + remesaPrograma.getMes());
									for(DiaVO dia : remesaPrograma.getDias()){
										if(!dia.isBloqueado() && !mesConMontoAsignado){
											System.out.println("dia.getDia()=" + dia.getDia());
											dia.setMonto(montoRemesa);
											mesConMontoAsignado = true;
										}
									}
									primerMes = false;
								}else{
									System.out.println("Mes Remesa 2" + remesaPrograma.getMes());
									if(!mesConMontoAsignado){
										for(DiaVO dia : remesaPrograma.getDias()){
											if(!dia.isBloqueado() && !mesConMontoAsignado){
												System.out.println("dia.getDia()=" + dia.getDia());
												dia.setMonto(montoRemesa);
												mesConMontoAsignado = true;
											}
										}
									}
								}
							}
						}else{
							//revisa si hay convenios aprobado para componente/subtitulo
							
							boolean existeRemesaMesActual = false;
							List<ConvenioComunaComponente> conveniosAprobadosComuna = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloComunaEstadoConvenio(
									idProgramaAno, componenteSeleccionado, Subtitulo.SUBTITULO24.getId(), comuna.getId(), EstadosConvenios.APROBADO.getId());
							if(conveniosAprobadosComuna != null && conveniosAprobadosComuna.size() > 0){
								existeRemesaMesActual = true;
								int sizeConveniosAprobadosComuna = conveniosAprobadosComuna.size();
								ConvenioComunaComponente convenioAprobadoComuna = conveniosAprobadosComuna.get(sizeConveniosAprobadosComuna - 1);
								oTResumenMunicipalVO.setIdConvenioComuna(convenioAprobadoComuna.getConvenioComuna().getIdConvenioComuna());
								oTResumenMunicipalVO.setIdConvenioComunaComponenteSinAprobar(convenioAprobadoComuna.getIdConvenioComunaComponente());
								oTResumenMunicipalVO.setTransferenciaAcumulada(0L);
								oTResumenMunicipalVO.setDiferencia(marcoPresupuestario - 0L);
								oTResumenMunicipalVO.setConveniosRecibidos(new Long(convenioAprobadoComuna.getMontoIngresado()));
								Long montoRemesa = 0L;
								for(int cuotaActual = 1; cuotaActual <= cuotaAPagar; cuotaActual++){
									Cuota cuota = cuotasPorPrograma.get((cuotaActual - 1));
									Long montoCuota = Math.round(convenioAprobadoComuna.getMontoIngresado() * (cuota.getPorcentaje() / 100.0));	
									montoRemesa += montoCuota;
									oTResumenMunicipalVO.setCuotaFinal(cuota.getId());
								}
								boolean primerMes = true;
								boolean mesConMontoAsignado = false;
								for(RemesasProgramaVO remesaPrograma : remesas){
									if(primerMes){
										System.out.println("Mes Remesa 1" + remesaPrograma.getMes());
										for(DiaVO dia : remesaPrograma.getDias()){
											if(!dia.isBloqueado() && !mesConMontoAsignado){
												System.out.println("dia.getDia()=" + dia.getDia());
												dia.setMonto(montoRemesa);
												mesConMontoAsignado = true;
											}
										}
										primerMes = false;
									}else{
										System.out.println("Mes Remesa 2" + remesaPrograma.getMes());
										if(!mesConMontoAsignado){
											for(DiaVO dia : remesaPrograma.getDias()){
												if(!dia.isBloqueado() && !mesConMontoAsignado){
													System.out.println("dia.getDia()=" + dia.getDia());
													dia.setMonto(montoRemesa);
													mesConMontoAsignado = true;
												}
											}
										}
									}
								} 
							}
							if(!existeRemesaMesActual){
								List<ConvenioComunaComponente> conveniosPagadosComuna = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloComunaEstadoConvenio(
									idProgramaAno, componenteSeleccionado, Subtitulo.SUBTITULO24.getId(), comuna.getId(), EstadosConvenios.PAGADO.getId());
								if(conveniosPagadosComuna != null && conveniosPagadosComuna.size() > 0){
									existeRemesaMesActual = true;
									int sizeConveniosPagadosComuna = conveniosPagadosComuna.size();
									ConvenioComunaComponente convenioPagadoComuna = conveniosPagadosComuna.get(sizeConveniosPagadosComuna - 1);
									oTResumenMunicipalVO.setIdConvenioComuna(convenioPagadoComuna.getConvenioComuna().getIdConvenioComuna());
									oTResumenMunicipalVO.setIdConvenioComunaComponenteSinAprobar(convenioPagadoComuna.getIdConvenioComunaComponente());
									oTResumenMunicipalVO.setTransferenciaAcumulada(0L);
									oTResumenMunicipalVO.setDiferencia(marcoPresupuestario - 0L);
									Long montoRemesa = 0L;
									for(int cuotaActual = 1; cuotaActual <= cuotaAPagar; cuotaActual++){
										Cuota cuota = cuotasPorPrograma.get((cuotaActual - 1));
										Long montoCuota = Math.round(convenioPagadoComuna.getMontoIngresado() * (cuota.getPorcentaje() / 100.0));	
										montoRemesa += montoCuota;
										oTResumenMunicipalVO.setCuotaFinal(cuota.getId());
									}
									boolean primerMes = true;
									boolean mesConMontoAsignado = false;
									for(RemesasProgramaVO remesaPrograma : remesas){
										if(primerMes){
											System.out.println("Mes Remesa 1" + remesaPrograma.getMes());
											for(DiaVO dia : remesaPrograma.getDias()){
												if(!dia.isBloqueado() && !mesConMontoAsignado){
													System.out.println("dia.getDia()=" + dia.getDia());
													dia.setMonto(montoRemesa);
													mesConMontoAsignado = true;
												}
											}
											primerMes = false;
										}else{
											System.out.println("Mes Remesa 2" + remesaPrograma.getMes());
											if(!mesConMontoAsignado){
												for(DiaVO dia : remesaPrograma.getDias()){
													if(!dia.isBloqueado() && !mesConMontoAsignado){
														System.out.println("dia.getDia()=" + dia.getDia());
														dia.setMonto(montoRemesa);
														mesConMontoAsignado = true;
													}
												}
											}
										}
									} 
								}
							}
							if(!existeRemesaMesActual){
								continue;
							}
						}
					}else{
						if(remesaPendiente && !remesaPagada){
							Integer cuotaAPagar = 0;
							for(Cuota cuota : cuotasPorPrograma){
								if((cuota.getIdMes() == null) || (cuota.getIdMes().getIdMes().intValue() <= mes.intValue())){
									cuotaAPagar++;
								}
							}
							//debo recalcular
							List<Integer> idDetalleRemesaEliminar = new ArrayList<Integer>();
							DetalleRemesas detalleRemesaSeleccionado = null;
							for(DetalleRemesas detalleRemesa : remesasPendientesProgramaAnoComponenteComunaSubtitulo){
								idDetalleRemesaEliminar.add(detalleRemesa.getIdDetalleRemesa());
								if(detalleRemesaSeleccionado == null){
									detalleRemesaSeleccionado = detalleRemesa;
								}
							}
							oTResumenMunicipalVO.setIdDetalleRemesaEliminar(idDetalleRemesaEliminar);
							boolean remesaAprobadaParaMes = false;
							for(RemesasProgramaVO remesaPrograma : remesas){
								for(DiaVO dia : remesaPrograma.getDias()){
									if(detalleRemesaSeleccionado.getDia().getDia().equals(dia.getDia()) && 
											detalleRemesaSeleccionado.getMes().getIdMes().equals(remesaPrograma.getIdMes())){
										remesaAprobadaParaMes = true;
										break;
									}
								}
							}
							
							if(remesaAprobadaParaMes){
								continue;
							}
							
							List<ConvenioComunaComponente> conveniosEnTramiteComuna = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloComunaEstadoConvenio(
									idProgramaAno, componenteSeleccionado, Subtitulo.SUBTITULO24.getId(), comuna.getId(), EstadosConvenios.TRAMITE.getId());
							if(conveniosEnTramiteComuna != null && conveniosEnTramiteComuna.size() > 0){
								int sizeConvenioComunaComponente = conveniosEnTramiteComuna.size();
								ConvenioComunaComponente convenioEnTramiteComuna = conveniosEnTramiteComuna.get(sizeConvenioComunaComponente - 1);
								oTResumenMunicipalVO.setIdConvenioComuna(convenioEnTramiteComuna.getConvenioComuna().getIdConvenioComuna());
								oTResumenMunicipalVO.setIdConvenioComunaComponenteSinAprobar(convenioEnTramiteComuna.getIdConvenioComunaComponente());
								oTResumenMunicipalVO.setTransferenciaAcumulada(0L);
								oTResumenMunicipalVO.setDiferencia(marcoPresupuestario - 0L);
								Long montoRemesa = 0L;
								for(int cuotaActual = 1; cuotaActual <= cuotaAPagar; cuotaActual++){
									Cuota cuota = cuotasPorPrograma.get((cuotaActual - 1));
									Long montoCuota = Math.round(convenioEnTramiteComuna.getMontoIngresado() * (cuota.getPorcentaje() / 100.0));	
									montoRemesa += montoCuota;
									oTResumenMunicipalVO.setCuotaFinal(cuota.getId());
								}
								boolean primerMes = true;
								boolean mesConMontoAsignado = false;
								for(RemesasProgramaVO remesaPrograma : remesas){
									if(primerMes){
										System.out.println("Mes Remesa 1" + remesaPrograma.getMes());
										for(DiaVO dia : remesaPrograma.getDias()){
											if(!dia.isBloqueado() && !mesConMontoAsignado){
												System.out.println("dia.getDia()=" + dia.getDia());
												dia.setMonto(montoRemesa);
												mesConMontoAsignado = true;
											}
										}
										primerMes = false;
									}else{
										System.out.println("Mes Remesa 2" + remesaPrograma.getMes());
										if(!mesConMontoAsignado){
											for(DiaVO dia : remesaPrograma.getDias()){
												if(!dia.isBloqueado() && !mesConMontoAsignado){
													System.out.println("dia.getDia()=" + dia.getDia());
													dia.setMonto(montoRemesa);
													mesConMontoAsignado = true;
												}
											}
										}
									}
								}
							} else {
								boolean existeRemesaMesActual = false;
								List<ConvenioComunaComponente> conveniosAprobadosComuna = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloComunaEstadoConvenio(
										idProgramaAno, componenteSeleccionado, Subtitulo.SUBTITULO24.getId(), comuna.getId(), EstadosConvenios.APROBADO.getId());
								if(conveniosAprobadosComuna != null && conveniosAprobadosComuna.size() > 0){
									existeRemesaMesActual = true;
									int sizeConveniosAprobadosComuna = conveniosAprobadosComuna.size();
									ConvenioComunaComponente convenioAprobadoComuna = conveniosAprobadosComuna.get(sizeConveniosAprobadosComuna - 1);
									oTResumenMunicipalVO.setIdConvenioComuna(convenioAprobadoComuna.getConvenioComuna().getIdConvenioComuna());
									oTResumenMunicipalVO.setIdConvenioComunaComponenteSinAprobar(convenioAprobadoComuna.getIdConvenioComunaComponente());
									oTResumenMunicipalVO.setTransferenciaAcumulada(0L);
									oTResumenMunicipalVO.setDiferencia(marcoPresupuestario - 0L);
									Long montoRemesa = 0L;
									for(int cuotaActual = 1; cuotaActual <= cuotaAPagar; cuotaActual++){
										Cuota cuota = cuotasPorPrograma.get((cuotaActual - 1));
										Long montoCuota = Math.round(convenioAprobadoComuna.getMontoIngresado() * (cuota.getPorcentaje() / 100.0));	
										montoRemesa += montoCuota;
										oTResumenMunicipalVO.setCuotaFinal(cuota.getId());
									}
									boolean primerMes = true;
									boolean mesConMontoAsignado = false;
									for(RemesasProgramaVO remesaPrograma : remesas){
										if(primerMes){
											System.out.println("Mes Remesa 1" + remesaPrograma.getMes());
											for(DiaVO dia : remesaPrograma.getDias()){
												if(!dia.isBloqueado() && !mesConMontoAsignado){
													System.out.println("dia.getDia()=" + dia.getDia());
													dia.setMonto(montoRemesa);
													mesConMontoAsignado = true;
												}
											}
											primerMes = false;
										}else{
											System.out.println("Mes Remesa 2" + remesaPrograma.getMes());
											if(!mesConMontoAsignado){
												for(DiaVO dia : remesaPrograma.getDias()){
													if(!dia.isBloqueado() && !mesConMontoAsignado){
														System.out.println("dia.getDia()=" + dia.getDia());
														dia.setMonto(montoRemesa);
														mesConMontoAsignado = true;
													}
												}
											}
										}
									} 
								}
								if(!existeRemesaMesActual){
									List<ConvenioComunaComponente> conveniosPagadosComuna = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloComunaEstadoConvenio(
										idProgramaAno, componenteSeleccionado, Subtitulo.SUBTITULO24.getId(), comuna.getId(), EstadosConvenios.PAGADO.getId());
									if(conveniosPagadosComuna != null && conveniosPagadosComuna.size() > 0){
										existeRemesaMesActual = true;
										int sizeConveniosPagadosComuna = conveniosPagadosComuna.size();
										ConvenioComunaComponente convenioPagadoComuna = conveniosPagadosComuna.get(sizeConveniosPagadosComuna - 1);
										oTResumenMunicipalVO.setIdConvenioComuna(convenioPagadoComuna.getConvenioComuna().getIdConvenioComuna());
										oTResumenMunicipalVO.setIdConvenioComunaComponenteSinAprobar(convenioPagadoComuna.getIdConvenioComunaComponente());
										oTResumenMunicipalVO.setTransferenciaAcumulada(0L);
										oTResumenMunicipalVO.setDiferencia(marcoPresupuestario - 0L);
										Long montoRemesa = 0L;
										for(int cuotaActual = 1; cuotaActual <= cuotaAPagar; cuotaActual++){
											Cuota cuota = cuotasPorPrograma.get((cuotaActual-1));
											Long montoCuota = Math.round(convenioPagadoComuna.getMontoIngresado() * (cuota.getPorcentaje() / 100.0));	
											montoRemesa += montoCuota;
											oTResumenMunicipalVO.setCuotaFinal(cuota.getId());
										}
										boolean primerMes = true;
										boolean mesConMontoAsignado = false;
										for(RemesasProgramaVO remesaPrograma : remesas){
											if(primerMes){
												System.out.println("Mes Remesa 1" + remesaPrograma.getMes());
												for(DiaVO dia : remesaPrograma.getDias()){
													if(!dia.isBloqueado() && !mesConMontoAsignado){
														System.out.println("dia.getDia()=" + dia.getDia());
														dia.setMonto(montoRemesa);
														mesConMontoAsignado = true;
													}
												}
												primerMes = false;
											}else{
												System.out.println("Mes Remesa 2" + remesaPrograma.getMes());
												if(!mesConMontoAsignado){
													for(DiaVO dia : remesaPrograma.getDias()){
														if(!dia.isBloqueado() && !mesConMontoAsignado){
															System.out.println("dia.getDia()=" + dia.getDia());
															dia.setMonto(montoRemesa);
															mesConMontoAsignado = true;
														}
													}
												}
											}
										} 
									}
								}
								if(!existeRemesaMesActual){
									continue;
								}
							}
						}else if(!remesaPendiente && remesaPagada){
							
							Integer cuotaAPagar = null;
							for(Cuota cuota : cuotasPorPrograma){
								if((cuota.getIdMes() != null) && (cuota.getIdMes().getIdMes().intValue() == mes.intValue())){
									cuotaAPagar = new Integer(cuota.getNumeroCuota());
								}
							}
							if(cuotaAPagar == null){
								continue;
							}
							Long montoTransferido = 0L;
							int cuotaNro = 0;
							for(DetalleRemesas detalleRemesa : remesasPagadasProgramaAnoComponenteComunaSubtitulo){
								montoTransferido += detalleRemesa.getMontoRemesa();
								cuotaNro = detalleRemesa.getCuota().getNumeroCuota();
							}
							List<ConvenioComunaComponente> conveniosEnTramiteComuna = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloComunaEstadoConvenio(
									idProgramaAno, componenteSeleccionado, Subtitulo.SUBTITULO24.getId(), comuna.getId(), EstadosConvenios.TRAMITE.getId());
							if(conveniosEnTramiteComuna != null && conveniosEnTramiteComuna.size() > 0){
								int sizeConvenioComunaComponente = conveniosEnTramiteComuna.size();
								ConvenioComunaComponente convenioEnTramiteComuna = conveniosEnTramiteComuna.get(sizeConvenioComunaComponente - 1);
								oTResumenMunicipalVO.setIdConvenioComuna(convenioEnTramiteComuna.getConvenioComuna().getIdConvenioComuna() );
								oTResumenMunicipalVO.setIdConvenioComunaComponenteSinAprobar(convenioEnTramiteComuna.getIdConvenioComunaComponente());
								oTResumenMunicipalVO.setTransferenciaAcumulada(montoTransferido);
								oTResumenMunicipalVO.setDiferencia(marcoPresupuestario - montoTransferido);
								
								Long montoRemesaAnteriorCorregido = 0L;
								for(int cuotaActual = 1; cuotaActual <= cuotaNro; cuotaActual++){
									Cuota cuota = cuotasPorPrograma.get((cuotaActual-1));
									Long montoCuota = Math.round(convenioEnTramiteComuna.getMontoIngresado() * (cuota.getPorcentaje() / 100.0));	
									montoRemesaAnteriorCorregido += montoCuota;
								}
								montoRemesaAnteriorCorregido -= montoTransferido;
								
								Long montoRemesa = 0L;
								for(int cuotaActual = (cuotaNro + 1); cuotaActual <= cuotaAPagar; cuotaActual++){
									Cuota cuota = cuotasPorPrograma.get((cuotaActual-1));
									Long montoCuota = Math.round(convenioEnTramiteComuna.getMontoIngresado() * (cuota.getPorcentaje() / 100.0));	
									montoRemesa += montoCuota;
									oTResumenMunicipalVO.setCuotaFinal(cuota.getId());
								}
								montoRemesa += montoRemesaAnteriorCorregido;
								
								boolean primerMes = true;
								boolean mesConMontoAsignado = false;
								for(RemesasProgramaVO remesaPrograma : remesas){
									if(primerMes){
										System.out.println("Mes Remesa 1" + remesaPrograma.getMes());
										for(DiaVO dia : remesaPrograma.getDias()){
											if(!dia.isBloqueado() && !mesConMontoAsignado){
												System.out.println("dia.getDia()=" + dia.getDia());
												dia.setMonto(montoRemesa);
												mesConMontoAsignado = true;
											}
										}
										primerMes = false;
									}else{
										System.out.println("Mes Remesa 2" + remesaPrograma.getMes());
										if(!mesConMontoAsignado){
											for(DiaVO dia : remesaPrograma.getDias()){
												if(!dia.isBloqueado() && !mesConMontoAsignado){
													System.out.println("dia.getDia()=" + dia.getDia());
													dia.setMonto(montoRemesa);
													mesConMontoAsignado = true;
												}
											}
										}
									}
								}
							} else {
								boolean existeRemesaMesActual = false;
								List<ConvenioComunaComponente> conveniosAprobadosComuna = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloComunaEstadoConvenio(
										idProgramaAno, componenteSeleccionado, Subtitulo.SUBTITULO24.getId(), comuna.getId(), EstadosConvenios.APROBADO.getId());
								if(conveniosAprobadosComuna != null && conveniosAprobadosComuna.size() > 0){
									existeRemesaMesActual = true;
									int sizeConveniosAprobadosComuna = conveniosAprobadosComuna.size();
									ConvenioComunaComponente convenioAprobadoComuna = conveniosAprobadosComuna.get(sizeConveniosAprobadosComuna - 1);
									oTResumenMunicipalVO.setIdConvenioComuna(convenioAprobadoComuna.getConvenioComuna().getIdConvenioComuna());
									oTResumenMunicipalVO.setIdConvenioComunaComponenteSinAprobar(convenioAprobadoComuna.getIdConvenioComunaComponente());
									oTResumenMunicipalVO.setTransferenciaAcumulada(montoTransferido);
									oTResumenMunicipalVO.setDiferencia(marcoPresupuestario - montoTransferido);
									
									Long montoRemesaAnteriorCorregido = 0L;
									for(int cuotaActual = 1; cuotaActual <= cuotaNro; cuotaActual++){
										Cuota cuota = cuotasPorPrograma.get((cuotaActual-1));
										Long montoCuota = Math.round(convenioAprobadoComuna.getMontoIngresado() * (cuota.getPorcentaje() / 100.0));	
										montoRemesaAnteriorCorregido += montoCuota;
									}
									montoRemesaAnteriorCorregido -= montoTransferido;
									
									Long montoRemesa = 0L;
									for(int cuotaActual = (cuotaNro + 1); cuotaActual <= cuotaAPagar; cuotaActual++){
										Cuota cuota = cuotasPorPrograma.get((cuotaActual-1));
										Long montoCuota = Math.round(convenioAprobadoComuna.getMontoIngresado() * (cuota.getPorcentaje() / 100.0));	
										montoRemesa += montoCuota;
										oTResumenMunicipalVO.setCuotaFinal(cuota.getId());
									}
									montoRemesa += montoRemesaAnteriorCorregido;
									
									boolean primerMes = true;
									boolean mesConMontoAsignado = false;
									for(RemesasProgramaVO remesaPrograma : remesas){
										if(primerMes){
											System.out.println("Mes Remesa 1" + remesaPrograma.getMes());
											for(DiaVO dia : remesaPrograma.getDias()){
												if(!dia.isBloqueado() && !mesConMontoAsignado){
													System.out.println("dia.getDia()=" + dia.getDia());
													dia.setMonto(montoRemesa);
													mesConMontoAsignado = true;
												}
											}
											primerMes = false;
										}else{
											System.out.println("Mes Remesa 2" + remesaPrograma.getMes());
											if(!mesConMontoAsignado){
												for(DiaVO dia : remesaPrograma.getDias()){
													if(!dia.isBloqueado() && !mesConMontoAsignado){
														System.out.println("dia.getDia()=" + dia.getDia());
														dia.setMonto(montoRemesa);
														mesConMontoAsignado = true;
													}
												}
											}
										}
									} 
								}
								if(!existeRemesaMesActual){
									List<ConvenioComunaComponente> conveniosPagadosComuna = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloComunaEstadoConvenio(
										idProgramaAno, componenteSeleccionado, Subtitulo.SUBTITULO24.getId(), comuna.getId(), EstadosConvenios.PAGADO.getId());
									if(conveniosPagadosComuna != null && conveniosPagadosComuna.size() > 0){
										existeRemesaMesActual = true;
										int sizeConveniosPagadosComuna = conveniosPagadosComuna.size();
										ConvenioComunaComponente convenioPagadoComuna = conveniosPagadosComuna.get(sizeConveniosPagadosComuna - 1);
										oTResumenMunicipalVO.setIdConvenioComuna(convenioPagadoComuna.getConvenioComuna().getIdConvenioComuna());
										oTResumenMunicipalVO.setIdConvenioComunaComponenteSinAprobar(convenioPagadoComuna.getIdConvenioComunaComponente());
										oTResumenMunicipalVO.setTransferenciaAcumulada(montoTransferido);
										oTResumenMunicipalVO.setDiferencia(marcoPresupuestario - montoTransferido);
										Long montoRemesa = 0L;
										for(int cuotaActual = (cuotaNro + 1); cuotaActual <= cuotaAPagar; cuotaActual++){
											Cuota cuota = cuotasPorPrograma.get((cuotaActual - 1));
											Long montoCuota = Math.round(convenioPagadoComuna.getMontoIngresado() * (cuota.getPorcentaje() / 100.0));	
											montoRemesa += montoCuota;
											oTResumenMunicipalVO.setCuotaFinal(cuota.getId());
										}
										boolean primerMes = true;
										boolean mesConMontoAsignado = false;
										for(RemesasProgramaVO remesaPrograma : remesas){
											if(primerMes){
												System.out.println("Mes Remesa 1" + remesaPrograma.getMes());
												for(DiaVO dia : remesaPrograma.getDias()){
													if(!dia.isBloqueado() && !mesConMontoAsignado){
														System.out.println("dia.getDia()=" + dia.getDia());
														dia.setMonto(montoRemesa);
														mesConMontoAsignado = true;
													}
												}
												primerMes = false;
											}else{
												System.out.println("Mes Remesa 2" + remesaPrograma.getMes());
												if(!mesConMontoAsignado){
													for(DiaVO dia : remesaPrograma.getDias()){
														if(!dia.isBloqueado() && !mesConMontoAsignado){
															System.out.println("dia.getDia()=" + dia.getDia());
															dia.setMonto(montoRemesa);
															mesConMontoAsignado = true;
														}
													}
												}
											}
										} 
									}
								}
								if(!existeRemesaMesActual){
									continue;
								}
							}
						}else if(remesaPendiente && remesaPagada){
							Integer cuotaAPagar = null;
							for(Cuota cuota : cuotasPorPrograma){
								if((cuota.getIdMes() != null) && (cuota.getIdMes().getIdMes().intValue() == mes.intValue())){
									cuotaAPagar = new Integer(cuota.getNumeroCuota());
								}
							}
				
							Long montoTransferido = 0L;
							int cuotaNro = 0;
							for(DetalleRemesas detalleRemesa : remesasPagadasProgramaAnoComponenteComunaSubtitulo){
								montoTransferido += detalleRemesa.getMontoRemesa();
								cuotaNro = detalleRemesa.getCuota().getNumeroCuota();
							}
							//debo recalcular
							List<Integer> idDetalleRemesaEliminar = new ArrayList<Integer>();
							int cuotaPendienteNro = 0;
							DetalleRemesas detalleRemesaSeleccionado = null;
							for(DetalleRemesas detalleRemesa : remesasPendientesProgramaAnoComponenteComunaSubtitulo){
								idDetalleRemesaEliminar.add(detalleRemesa.getIdDetalleRemesa());
								cuotaPendienteNro = detalleRemesa.getCuota().getNumeroCuota();
								if(detalleRemesaSeleccionado == null){
									detalleRemesaSeleccionado = detalleRemesa;
								}
							}
							
							boolean remesaAprobadaParaMes = false;
							for(RemesasProgramaVO remesaPrograma : remesas){
								for(DiaVO dia : remesaPrograma.getDias()){
									if(detalleRemesaSeleccionado.getDia().getDia().equals(dia.getDia()) && 
											detalleRemesaSeleccionado.getMes().getIdMes().equals(remesaPrograma.getIdMes())){
										remesaAprobadaParaMes = true;
										break;
									}
								}
							}
							
							if(remesaAprobadaParaMes){
								continue;
							}
							
							if(cuotaAPagar == null){
								cuotaAPagar = cuotaPendienteNro;
							}
							
							oTResumenMunicipalVO.setIdDetalleRemesaEliminar(idDetalleRemesaEliminar);
							List<ConvenioComunaComponente> conveniosEnTramiteComuna = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloComunaEstadoConvenio(
									idProgramaAno, componenteSeleccionado, Subtitulo.SUBTITULO24.getId(), comuna.getId(), EstadosConvenios.TRAMITE.getId());
							if(conveniosEnTramiteComuna != null && conveniosEnTramiteComuna.size() > 0){
								int sizeConvenioComunaComponente = conveniosEnTramiteComuna.size();
								ConvenioComunaComponente convenioEnTramiteComuna = conveniosEnTramiteComuna.get(sizeConvenioComunaComponente - 1);
								oTResumenMunicipalVO.setIdConvenioComuna(convenioEnTramiteComuna.getConvenioComuna().getIdConvenioComuna());
								oTResumenMunicipalVO.setIdConvenioComunaComponenteSinAprobar(convenioEnTramiteComuna.getIdConvenioComunaComponente());
								oTResumenMunicipalVO.setTransferenciaAcumulada(montoTransferido);
								oTResumenMunicipalVO.setDiferencia(marcoPresupuestario - montoTransferido);
								
								Long montoRemesaAnteriorCorregido = 0L;
								for(int cuotaActual = 1; cuotaActual <= cuotaNro; cuotaActual++){
									Cuota cuota = cuotasPorPrograma.get((cuotaActual-1));
									Long montoCuota = Math.round(convenioEnTramiteComuna.getMontoIngresado() * (cuota.getPorcentaje() / 100.0));	
									montoRemesaAnteriorCorregido += montoCuota;
								}
								montoRemesaAnteriorCorregido -= montoTransferido;
								
								Long montoRemesa = 0L;
								for(int cuotaActual = (cuotaNro + 1); cuotaActual <= cuotaAPagar; cuotaActual++){
									Cuota cuota = cuotasPorPrograma.get((cuotaActual-1));
									Long montoCuota = Math.round(convenioEnTramiteComuna.getMontoIngresado() * (cuota.getPorcentaje() / 100.0));	
									montoRemesa += montoCuota;
									oTResumenMunicipalVO.setCuotaFinal(cuota.getId());
								}
								montoRemesa += montoRemesaAnteriorCorregido;
								
								boolean primerMes = true;
								boolean mesConMontoAsignado = false;
								for(RemesasProgramaVO remesaPrograma : remesas){
									if(primerMes){
										System.out.println("Mes Remesa 1" + remesaPrograma.getMes());
										for(DiaVO dia : remesaPrograma.getDias()){
											if(!dia.isBloqueado() && !mesConMontoAsignado){
												System.out.println("dia.getDia()=" + dia.getDia());
												dia.setMonto(montoRemesa);
												mesConMontoAsignado = true;
											}
										}
										primerMes = false;
									}else{
										System.out.println("Mes Remesa 2" + remesaPrograma.getMes());
										if(!mesConMontoAsignado){
											for(DiaVO dia : remesaPrograma.getDias()){
												if(!dia.isBloqueado() && !mesConMontoAsignado){
													System.out.println("dia.getDia()=" + dia.getDia());
													dia.setMonto(montoRemesa);
													mesConMontoAsignado = true;
												}
											}
										}
									}
								}
							} else {
								boolean existeRemesaMesActual = false;
								List<ConvenioComunaComponente> conveniosAprobadosComuna = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloComunaEstadoConvenio(
										idProgramaAno, componenteSeleccionado, Subtitulo.SUBTITULO24.getId(), comuna.getId(), EstadosConvenios.APROBADO.getId());
								if(conveniosAprobadosComuna != null && conveniosAprobadosComuna.size() > 0){
									existeRemesaMesActual = true;
									int sizeConveniosAprobadosComuna = conveniosAprobadosComuna.size();
									ConvenioComunaComponente convenioAprobadoComuna = conveniosAprobadosComuna.get(sizeConveniosAprobadosComuna - 1);
									oTResumenMunicipalVO.setIdConvenioComuna(convenioAprobadoComuna.getConvenioComuna().getIdConvenioComuna());
									oTResumenMunicipalVO.setIdConvenioComunaComponenteSinAprobar(convenioAprobadoComuna.getIdConvenioComunaComponente());
									oTResumenMunicipalVO.setTransferenciaAcumulada(montoTransferido);
									oTResumenMunicipalVO.setDiferencia(marcoPresupuestario - montoTransferido);
									
									Long montoRemesaAnteriorCorregido = 0L;
									for(int cuotaActual = 1; cuotaActual <= cuotaNro; cuotaActual++){
										Cuota cuota = cuotasPorPrograma.get((cuotaActual-1));
										Long montoCuota = Math.round(convenioAprobadoComuna.getMontoIngresado() * (cuota.getPorcentaje() / 100.0));	
										montoRemesaAnteriorCorregido += montoCuota;
									}
									montoRemesaAnteriorCorregido -= montoTransferido;
									
									Long montoRemesa = 0L;
									for(int cuotaActual = (cuotaNro + 1); cuotaActual <= cuotaAPagar; cuotaActual++){
										Cuota cuota = cuotasPorPrograma.get((cuotaActual-1));
										Long montoCuota = Math.round(convenioAprobadoComuna.getMontoIngresado() * (cuota.getPorcentaje() / 100.0));	
										montoRemesa += montoCuota;
										oTResumenMunicipalVO.setCuotaFinal(cuota.getId());
									}
									montoRemesa += montoRemesaAnteriorCorregido;
									
									boolean primerMes = true;
									boolean mesConMontoAsignado = false;
									for(RemesasProgramaVO remesaPrograma : remesas){
										if(primerMes){
											System.out.println("Mes Remesa 1" + remesaPrograma.getMes());
											for(DiaVO dia : remesaPrograma.getDias()){
												if(!dia.isBloqueado() && !mesConMontoAsignado){
													System.out.println("dia.getDia()=" + dia.getDia());
													dia.setMonto(montoRemesa);
													mesConMontoAsignado = true;
												}
											}
											primerMes = false;
										}else{
											System.out.println("Mes Remesa 2" + remesaPrograma.getMes());
											if(!mesConMontoAsignado){
												for(DiaVO dia : remesaPrograma.getDias()){
													if(!dia.isBloqueado() && !mesConMontoAsignado){
														System.out.println("dia.getDia()=" + dia.getDia());
														dia.setMonto(montoRemesa);
														mesConMontoAsignado = true;
													}
												}
											}
										}
									} 
								}
								if(!existeRemesaMesActual){
									List<ConvenioComunaComponente> conveniosPagadosComuna = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloComunaEstadoConvenio(
										idProgramaAno, componenteSeleccionado, Subtitulo.SUBTITULO24.getId(), comuna.getId(), EstadosConvenios.PAGADO.getId());
									if(conveniosPagadosComuna != null && conveniosPagadosComuna.size() > 0){
										existeRemesaMesActual = true;
										int sizeConveniosPagadosComuna = conveniosPagadosComuna.size();
										ConvenioComunaComponente convenioPagadoComuna = conveniosPagadosComuna.get(sizeConveniosPagadosComuna - 1);
										oTResumenMunicipalVO.setIdConvenioComuna(convenioPagadoComuna.getConvenioComuna().getIdConvenioComuna());
										oTResumenMunicipalVO.setIdConvenioComunaComponenteSinAprobar(convenioPagadoComuna.getIdConvenioComunaComponente());
										oTResumenMunicipalVO.setTransferenciaAcumulada(montoTransferido);
										oTResumenMunicipalVO.setDiferencia(marcoPresupuestario - montoTransferido);
										
										Long montoRemesa = 0L;
										for(int cuotaActual = (cuotaNro + 1); cuotaActual <= cuotaAPagar; cuotaActual++){
											Cuota cuota = cuotasPorPrograma.get((cuotaActual-1));
											Long montoCuota = Math.round(convenioPagadoComuna.getMontoIngresado() * (cuota.getPorcentaje() / 100.0));	
											montoRemesa += montoCuota;
											oTResumenMunicipalVO.setCuotaFinal(cuota.getId());
										}
										
										boolean primerMes = true;
										boolean mesConMontoAsignado = false;
										for(RemesasProgramaVO remesaPrograma : remesas){
											if(primerMes){
												System.out.println("Mes Remesa 1" + remesaPrograma.getMes());
												for(DiaVO dia : remesaPrograma.getDias()){
													if(!dia.isBloqueado() && !mesConMontoAsignado){
														System.out.println("dia.getDia()=" + dia.getDia());
														dia.setMonto(montoRemesa);
														mesConMontoAsignado = true;
													}
												}
												primerMes = false;
											}else{
												System.out.println("Mes Remesa 2" + remesaPrograma.getMes());
												if(!mesConMontoAsignado){
													for(DiaVO dia : remesaPrograma.getDias()){
														if(!dia.isBloqueado() && !mesConMontoAsignado){
															System.out.println("dia.getDia()=" + dia.getDia());
															dia.setMonto(montoRemesa);
															mesConMontoAsignado = true;
														}
													}
												}
											}
										} 
									}
								}
								if(!existeRemesaMesActual){
									continue;
								}
							}
						}
					}
					oTResumenMunicipalVO.setRemesas(remesas);
					listaOtResumenMunicipalVO.add(oTResumenMunicipalVO);
				}
			}
		}
		return listaOtResumenMunicipalVO;
	}

	public List<OTResumenMunicipalVO> getDetalleOTMunicipalConsolidador(Integer componenteSeleccionado,
			Integer servicioSeleccionado, Integer idProgramaAno) {
		System.out.println("Buscando Detalle de Convenios/Remesas para Subtitulo 24");
		ProgramaVO programaVO = programasService.getProgramaAno(idProgramaAno);
		List<OTResumenMunicipalVO> listaOtResumenMunicipalVO = new ArrayList<OTResumenMunicipalVO>();
		List<Cuota> cuotasPrograma = reliquidacionDAO.getCuotasByProgramaAno(programaVO.getId());
		
		
		List<ServicioSalud> servicios = null;
		if(servicioSeleccionado == null){
			servicios = servicioSaludDAO.getServiciosOrderId();
		}else{
			ServicioSalud servicioSalud = servicioSaludDAO.getById(servicioSeleccionado);
			servicios = new ArrayList<ServicioSalud>();
			servicios.add(servicioSalud);
		}
		
		for(ServicioSalud servicioSalud : servicios){
			if(servicioSalud.getComunas() != null && servicioSalud.getComunas().size() > 0){
				for(Comuna comuna : servicioSalud.getComunas()){
					OTResumenMunicipalVO prog = new OTResumenMunicipalVO();
					ComunaVO comunaVO = new ComunaVO();
					comunaVO.setIdComuna(comuna.getId());
					comunaVO.setNombre(comuna.getNombre());
					prog.setComuna(comunaVO);
					Map<Integer, RemesasProgramaVO> remesasPorMes = new HashMap<Integer, RemesasProgramaVO>();
					List<DetalleRemesas> remesasPendientes = remesasDAO.getRemesasPendientesConsolidadorProgramaAnoComponenteSubtituloComuna(idProgramaAno, componenteSeleccionado, Subtitulo.SUBTITULO24.getId(), comuna.getId());
					System.out.println("comuna.getId()=" + comuna.getId() + " remesasPendientes.size()=" + ((remesasPendientes == null) ? 0 : remesasPendientes.size()));
					if(remesasPendientes != null && remesasPendientes.size() > 0){
						List<DetalleRemesas> remesasPagadasProgramaAnoComponenteComunaSubtitulo = remesasDAO.getRemesasPagadasByProgramaAnoComponenteComunaSubtitulo(idProgramaAno, componenteSeleccionado, comuna.getId(), Subtitulo.SUBTITULO24.getId());
						
						Long marcoPresupuestario = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAno, componenteSeleccionado, Subtitulo.SUBTITULO24.getId());
						prog.setMarcoPresupuestario(marcoPresupuestario);
						Long montoTransferido = 0L;
						if(remesasPagadasProgramaAnoComponenteComunaSubtitulo != null && remesasPagadasProgramaAnoComponenteComunaSubtitulo.size() > 0){
							for(DetalleRemesas detalleRemesa : remesasPagadasProgramaAnoComponenteComunaSubtitulo){
								montoTransferido += detalleRemesa.getMontoRemesa();
							}
						}
						prog.setTransferenciaAcumulada(montoTransferido);
						
						List<ConvenioComunaComponente> conveniosTramiteComuna = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloComunaEstadoConvenio(
								idProgramaAno, componenteSeleccionado, Subtitulo.SUBTITULO24.getId(), comuna.getId(), EstadosConvenios.TRAMITE.getId());
						
						prog.setConveniosRecibidos(0L);
						if(conveniosTramiteComuna != null && conveniosTramiteComuna.size() > 0){
							int sizeConveniosTramiteComuna = conveniosTramiteComuna.size();
							ConvenioComunaComponente convenioTramiteComuna = conveniosTramiteComuna.get(sizeConveniosTramiteComuna - 1);
							prog.setConveniosRecibidos(new Long(convenioTramiteComuna.getMontoIngresado()));
						}else{
							List<ConvenioComunaComponente> conveniosPagadosComuna = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloComunaEstadoConvenio(
									idProgramaAno, componenteSeleccionado, Subtitulo.SUBTITULO24.getId(), comuna.getId(), EstadosConvenios.PAGADO.getId());
							if(conveniosPagadosComuna != null && conveniosPagadosComuna.size() > 0){
								int sizeConveniosPagadosComuna = conveniosPagadosComuna.size();
								ConvenioComunaComponente convenioPagadosComuna = conveniosPagadosComuna.get(sizeConveniosPagadosComuna - 1);
								prog.setConveniosRecibidos(new Long(convenioPagadosComuna.getMontoIngresado()));
							}
						}
						
						List<Integer> idDetalleRemesaAProbarConsolidador = new ArrayList<Integer>();
						for(DetalleRemesas remesaPendienteMes : remesasPendientes){
							idDetalleRemesaAProbarConsolidador.add(remesaPendienteMes.getIdDetalleRemesa());
							if(remesasPorMes.get(remesaPendienteMes.getMes().getIdMes()) == null){
								RemesasProgramaVO detalleRemesa = new RemesasProgramaVO();
								Mes mes = mesDAO.getMesPorID(remesaPendienteMes.getMes().getIdMes());
								detalleRemesa.setIdMes(mes.getIdMes());
								detalleRemesa.setMes(mes.getNombre());
								List<DiaVO> dias = new ArrayList<DiaVO>();
								DiaVO dia = new DiaVO();
								dia.setBloqueado(remesaPendienteMes.isBloqueado());
								dia.setDia(remesaPendienteMes.getDia().getId());
								dia.setMonto(new Long(remesaPendienteMes.getMontoRemesa()));
								dias.add(dia);
								detalleRemesa.setDias(dias);
								remesasPorMes.put(remesaPendienteMes.getMes().getIdMes(), detalleRemesa);
							}else{
								DiaVO dia = new DiaVO();
								dia.setBloqueado(remesaPendienteMes.isBloqueado());
								dia.setDia(remesaPendienteMes.getDia().getId());
								dia.setMonto(new Long(remesaPendienteMes.getMontoRemesa()));
								remesasPorMes.get(remesaPendienteMes.getMes().getIdMes()).getDias().add(dia);
							}
						}
						prog.setIdDetalleRemesaAProbarConsolidador(idDetalleRemesaAProbarConsolidador);
						List<RemesasProgramaVO> remesas = new ArrayList<RemesasProgramaVO>();
						for (Integer key : remesasPorMes.keySet()) {
							RemesasProgramaVO remesasProgramaVO = remesasPorMes.get(key);
							remesas.add(remesasProgramaVO);
						}
						prog.setRemesas(remesas);
						listaOtResumenMunicipalVO.add(prog);
					}
				}
			}
		}
		return listaOtResumenMunicipalVO;
	}

	public List<RemesasProgramaVO> getRemesasPrograma(Integer idPrograma, Integer idMes, Integer ano){
		List<ProgramaFechaRemesa> fechasRemesas = programasDAO.findRemesasByPrograma(idPrograma);
		List<RemesasProgramaVO> remesasVO = new ArrayList<RemesasProgramaVO>();
		int meses = 1;

		if(idMes < 12){
			meses = 2;
		}
		ano = ((ano == null) ? getAnoCurso() : ano);
		for (int i = 0; i < meses; i++) {
			List<DiaVO> diasVO = new ArrayList<DiaVO>();
			RemesasProgramaVO remesa = new RemesasProgramaVO();
			Mes mes = mesDAO.getMesPorID(idMes+i);
			for(ProgramaFechaRemesa fechaRemesa : fechasRemesas){
				int day = fechaRemesa.getFechaRemesa().getDia().getDia();
				while(isWeekend(mes.getIdMes(), day, ano) || isFeriado(mes.getIdMes(), day, ano)){
					day-=1;
				}
				DiaVO dia = new DiaVO();
				dia.setDia(day);
				dia.setMonto(0L);
				dia.setBloqueado(diabloqueado(day, mes.getIdMes(), ano));
				diasVO.add(dia);
			}
			remesa.setIdMes(mes.getIdMes());
			remesa.setMes(mes.getNombre());
			remesa.setDias(diasVO);
			System.out.println("diasVO.size()-->" + diasVO.size());
			remesa.setCantDias(diasVO.size());
			remesasVO.add(remesa);
		}
		return remesasVO;
	}

	public List<RemesasProgramaVO> getRemesasPerCapita(Integer idPrograma, Integer idMes, Integer ano){
		List<ProgramaFechaRemesa> fechasRemesas = programasDAO.findRemesasByPrograma(idPrograma);
		List<RemesasProgramaVO> remesasVO = new ArrayList<RemesasProgramaVO>();
		int meses = 1;

		if(idMes < 12){
			meses = 2;
		}
		ano = ((ano == null) ? getAnoCurso() : ano);
		for (int i = 0; i < meses; i++) {
			List<DiaVO> diasVO = new ArrayList<DiaVO>();
			RemesasProgramaVO remesa = new RemesasProgramaVO();
			Mes mes = mesDAO.getMesPorID(idMes+i);
			for(ProgramaFechaRemesa fechaRemesa : fechasRemesas){
				int day = fechaRemesa.getFechaRemesa().getDia().getDia();
				while(isWeekend(mes.getIdMes(), day, ano) || isFeriado(mes.getIdMes(), day, ano)){
					day-=1;
				}
				DiaVO dia = new DiaVO();
				dia.setDia(day);
				dia.setMonto(0L);
				dia.setBloqueado(diabloqueado(day, mes.getIdMes(), ano));
				diasVO.add(dia);
			}
			remesa.setIdMes(mes.getIdMes());
			remesa.setMes(mes.getNombre());
			remesa.setDias(diasVO);
			System.out.println("diasVO.size()-->" + diasVO.size());
			remesa.setCantDias(diasVO.size());
			remesasVO.add(remesa);
		}
		System.out.println("getRemesasPerCapita remesasVO.size()" + ((remesasVO != null) ? remesasVO.size() : 0));
		return remesasVO;
	}


	public boolean isWeekend(Integer idMes, Integer dia, Integer anoCurso) {
		Calendar cal = Calendar.getInstance();
		cal.set(anoCurso, (idMes - 1), dia);
		if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY  || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
			System.out.println("Es fin de semana");
			return true;
		}
		System.out.println("No es fin de semana");
		return false;

	}

	public boolean isFeriado(Integer idMes, int dia, Integer anoCurso) {
		Festivos festivo = utilitariosDAO.findFestivoByDiaMesAno(idMes,dia,anoCurso);
		if(festivo != null){
			System.out.println("Es feriado");
			return true;
		}
		System.out.println("No es feriado");
		return false;
	}

	public boolean diabloqueado(int dia, int mes, Integer ano){
		System.out.println("Evaluando fecha: "+dia+"/"+mes+"/"+ano);

		/*Calendar calendarHoy = Calendar.getInstance();
		calendarHoy.add(Calendar.MONTH, -9);
		Date hoy = new Date(calendarHoy.getTimeInMillis()); 

		Calendar calendar = new GregorianCalendar(ano, mes-1, dia); 
		Date fechaRemesa = new Date(calendar.getTimeInMillis());

		long diferencia = ( fechaRemesa.getTime() - hoy.getTime()  )/MILLSECS_PER_DAY; */
		Calendar hoy = new GregorianCalendar();
		hoy.set(Calendar.YEAR, ano); 
		Calendar fechaRemesa = new GregorianCalendar();
		fechaRemesa.set(ano, (mes - 1), dia); 

		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		System.out.println("fechaRemesa = "+format.format(fechaRemesa.getTime()));
		System.out.println("hoy = "+format.format(hoy.getTime()));
		long diferencia = ( fechaRemesa.getTime().getTime() - hoy.getTime().getTime()) / MILLSECS_PER_DAY;

		System.out.println("diferencia numero de dias entre las fechas-->"+diferencia);
		if(diferencia > 0){
			if(diferencia < 3){
				System.out.println("FECHA BLOQUEADA");
				return true;
			}else{
				System.out.println("FECHA OK");
				return false;
			}
		}else{
			System.out.println("FECHA BLOQUEADA");
			return true;
		}
	}

	public List<OTPerCapitaVO> getDetallePerCapita(Integer servicioSeleccionado, Integer anoCurso, Integer idProgramaAno) {

		List<OTPerCapitaVO> listaReportePercapita = new ArrayList<OTPerCapitaVO>();
		ProgramaVO programaVO = programasService.getProgramaAno(idProgramaAno);
		List<Cuota> cuotasPorPrograma = reliquidacionDAO.getCuotasByProgramaAno(idProgramaAno);
		Integer mes = Integer.parseInt(getMesCurso(true));
		Integer componenteSeleccionado = null;
		if(programaVO.getComponentes() != null && programaVO.getComponentes().size() > 0){
			componenteSeleccionado = programaVO.getComponentes().get(0).getId();
		}
		
		List<ServicioSalud> servicios = null;
		if(servicioSeleccionado == null){
			servicios = servicioSaludDAO.getServiciosOrderId();
		}else{
			servicios = new ArrayList<ServicioSalud>();
			ServicioSalud servicioSalud = servicioSaludDAO.getById(servicioSeleccionado);
			servicios.add(servicioSalud);
		}
		
		DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(anoCurso);
		
		for(ServicioSalud servicioSalud : servicios){
			if(servicioSalud.getComunas() != null && servicioSalud.getComunas().size() > 0){
				for(Comuna comuna : servicioSalud.getComunas()){
					List<DetalleRemesas> remesasPagadasProgramaAnoComponenteComunaSubtitulo = remesasDAO.getRemesasPagadasByProgramaAnoComponenteComunaSubtitulo(idProgramaAno, componenteSeleccionado, comuna.getId(), Subtitulo.SUBTITULO24.getId());
					List<DetalleRemesas> remesasPendientesProgramaAnoComponenteComunaSubtitulo = remesasDAO.getRemesasPendientesByProgramaAnoComponenteComunaSubtitulo(idProgramaAno, componenteSeleccionado, comuna.getId(), Subtitulo.SUBTITULO24.getId());
					Boolean primeraRemesaAprobadaProfesional = null;
					boolean remesaPendiente = false;
					boolean remesaPagada = false;
					Long montoRemesaPagada = 0L;
					if((remesasPagadasProgramaAnoComponenteComunaSubtitulo != null && remesasPagadasProgramaAnoComponenteComunaSubtitulo.size() > 0) ||
							(remesasPendientesProgramaAnoComponenteComunaSubtitulo != null && remesasPendientesProgramaAnoComponenteComunaSubtitulo.size() > 0)){
						primeraRemesaAprobadaProfesional = false;
						if(remesasPagadasProgramaAnoComponenteComunaSubtitulo != null && remesasPagadasProgramaAnoComponenteComunaSubtitulo.size() > 0){
							remesaPagada = true;
							for(DetalleRemesas detalleRemesa : remesasPagadasProgramaAnoComponenteComunaSubtitulo){
								montoRemesaPagada += detalleRemesa.getMontoRemesa();
							}
						}
						if(remesasPendientesProgramaAnoComponenteComunaSubtitulo != null && remesasPendientesProgramaAnoComponenteComunaSubtitulo.size() > 0){
							remesaPendiente = true;
						}
					}else{
						primeraRemesaAprobadaProfesional = true;
					}
					
					List<AntecendentesComunaCalculado> antecendentesComunaCalculados = antecedentesComunaDAO.findAntecendentesComunaCalculadoByComunaServicioDistribucionInicialPercapitaVigente(comuna.getServicioSalud().getId(), comuna.getId(), distribucionInicialPercapita.getIdDistribucionInicialPercapita());
					AntecendentesComunaCalculado antecendentesComunaCalculado = ((antecendentesComunaCalculados != null && antecendentesComunaCalculados.size() > 0) ? antecendentesComunaCalculados.get(0): null);
					if(antecendentesComunaCalculado == null){
						continue;
					}
					
					Long marcoPresupuestario = ((antecendentesComunaCalculado.getPercapitaAno() != null) ? antecendentesComunaCalculado.getPercapitaAno() : 0);	
					List<RemesasProgramaVO> remesas = getRemesasPrograma(programaVO.getId(), mes, programaVO.getAno());
					OTPerCapitaVO percapitaVO = new OTPerCapitaVO();
					percapitaVO.setMarcoPresupuestario(marcoPresupuestario);
					percapitaVO.setIdComuna(comuna.getId());
					percapitaVO.setComuna(comuna.getNombre());
					percapitaVO.setTipoComuna(((antecendentesComunaCalculado.getAntecedentesComuna() != null && antecendentesComunaCalculado.getAntecedentesComuna().getClasificacion() != null) ? antecendentesComunaCalculado.getAntecedentesComuna().getClasificacion().getDescripcion() : ""));
					
					if(primeraRemesaAprobadaProfesional){
						Integer cuotaAPagar = 0;
						percapitaVO.setTransferenciaAcumulada(0L);
						percapitaVO.setDiferencia(percapitaVO.getMarcoPresupuestario() - 0L);
						for(Cuota cuota : cuotasPorPrograma){
							if((cuota.getIdMes() == null) || (cuota.getIdMes().getIdMes().intValue() <= mes.intValue())){
								cuotaAPagar++;
							}
						}
						Long montoRemesa = 0L;
						for(int cuotaActual = 1; cuotaActual <= cuotaAPagar; cuotaActual++){
							Cuota cuota = cuotasPorPrograma.get((cuotaActual - 1));
							montoRemesa += antecendentesComunaCalculado.getPercapitaMes();
							percapitaVO.setCuotaFinal(cuota.getId());
						}
						boolean primerMes = true;
						boolean mesConMontoAsignado = false;
						for(RemesasProgramaVO remesaPrograma : remesas){
							if(primerMes){
								System.out.println("Mes Remesa 1" + remesaPrograma.getMes());
								System.out.println("montoRemesa" + montoRemesa);
								for(DiaVO dia : remesaPrograma.getDias()){
									if(!dia.isBloqueado() && !mesConMontoAsignado){
										System.out.println("dia.getDia()=" + dia.getDia());
										dia.setMonto(montoRemesa);
										mesConMontoAsignado = true;
									}
								}
								primerMes = false;
							}else{
								System.out.println("Mes Remesa 2" + remesaPrograma.getMes());
								System.out.println("montoRemesa" + montoRemesa);
								if(!mesConMontoAsignado){
									for(DiaVO dia : remesaPrograma.getDias()){
										if(!dia.isBloqueado() && !mesConMontoAsignado){
											System.out.println("dia.getDia()=" + dia.getDia());
											dia.setMonto(montoRemesa);
											mesConMontoAsignado = true;
										}
									}
								}
							}
						}
					}else{
						Integer cuotaAPagar = 0;
						percapitaVO.setTransferenciaAcumulada(montoRemesaPagada);
						percapitaVO.setDiferencia(percapitaVO.getMarcoPresupuestario() - montoRemesaPagada);
						for(Cuota cuota : cuotasPorPrograma){
							if((cuota.getIdMes() == null) || (cuota.getIdMes().getIdMes().intValue() <= mes.intValue())){
								cuotaAPagar++;
							}
						}
						Long montoRemesa = antecendentesComunaCalculado.getPercapitaMes();
						Long montoRemesaMesAnterior = 0L;
						for(int cuotaActual = 1; cuotaActual <= cuotaAPagar; cuotaActual++){
							Cuota cuota = cuotasPorPrograma.get((cuotaActual - 1));
							if(cuotaActual < cuotaAPagar){
								montoRemesaMesAnterior += antecendentesComunaCalculado.getPercapitaMes();
							}
							percapitaVO.setCuotaFinal(cuota.getId());
						}
						Long diferenciaRemesaReal = montoRemesaPagada - montoRemesaMesAnterior;
						montoRemesa = montoRemesa + diferenciaRemesaReal;
						boolean primerMes = true;
						boolean mesConMontoAsignado = false;
						for(RemesasProgramaVO remesaPrograma : remesas){
							if(primerMes){
								System.out.println("Mes Remesa 1" + remesaPrograma.getMes());
								System.out.println("montoRemesa" + montoRemesa);
								for(DiaVO dia : remesaPrograma.getDias()){
									if(!dia.isBloqueado() && !mesConMontoAsignado){
										System.out.println("dia.getDia()=" + dia.getDia());
										dia.setMonto(montoRemesa);
										mesConMontoAsignado = true;
									}
								}
								primerMes = false;
							}else{
								System.out.println("Mes Remesa 2" + remesaPrograma.getMes());
								System.out.println("montoRemesa" + montoRemesa);
								if(!mesConMontoAsignado){
									for(DiaVO dia : remesaPrograma.getDias()){
										if(!dia.isBloqueado() && !mesConMontoAsignado){
											System.out.println("dia.getDia()=" + dia.getDia());
											dia.setMonto(montoRemesa);
											mesConMontoAsignado = true;
										}
									}
								}
							}
						}
					}
					percapitaVO.setRemesas(remesas);
					listaReportePercapita.add(percapitaVO);
				}
			}
		}
		return listaReportePercapita;
	}
	
	public List<OTPerCapitaVO> getDetalleDesempenoDificil(Integer servicioSeleccionado, Integer anoCurso, Integer idProgramaAno) {

		List<OTPerCapitaVO> listaReportePercapita = new ArrayList<OTPerCapitaVO>();
		ProgramaVO programaVO = programasService.getProgramaAno(idProgramaAno);
		List<Cuota> cuotasPorPrograma = reliquidacionDAO.getCuotasByProgramaAno(idProgramaAno);
		Integer mes = Integer.parseInt(getMesCurso(true));
		Integer componenteSeleccionado = null;
		if(programaVO.getComponentes() != null && programaVO.getComponentes().size() > 0){
			componenteSeleccionado = programaVO.getComponentes().get(0).getId();
		}
		
		List<ServicioSalud> servicios = null;
		if(servicioSeleccionado == null){
			servicios = servicioSaludDAO.getServiciosOrderId();
		}else{
			servicios = new ArrayList<ServicioSalud>();
			ServicioSalud servicioSalud = servicioSaludDAO.getById(servicioSeleccionado);
			servicios.add(servicioSalud);
		}
		
		DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(anoCurso);
		
		for(ServicioSalud servicioSalud : servicios){
			if(servicioSalud.getComunas() != null && servicioSalud.getComunas().size() > 0){
				for(Comuna comuna : servicioSalud.getComunas()){
					List<DetalleRemesas> remesasPagadasProgramaAnoComponenteComunaSubtitulo = remesasDAO.getRemesasPagadasByProgramaAnoComponenteComunaSubtitulo(idProgramaAno, componenteSeleccionado, comuna.getId(), Subtitulo.SUBTITULO24.getId());
					List<DetalleRemesas> remesasPendientesProgramaAnoComponenteComunaSubtitulo = remesasDAO.getRemesasPendientesByProgramaAnoComponenteComunaSubtitulo(idProgramaAno, componenteSeleccionado, comuna.getId(), Subtitulo.SUBTITULO24.getId());
					Boolean primeraRemesaAprobadaProfesional = null;
					boolean remesaPendiente = false;
					boolean remesaPagada = false;
					Long montoRemesaPagada = 0L;
					if((remesasPagadasProgramaAnoComponenteComunaSubtitulo != null && remesasPagadasProgramaAnoComponenteComunaSubtitulo.size() > 0) ||
							(remesasPendientesProgramaAnoComponenteComunaSubtitulo != null && remesasPendientesProgramaAnoComponenteComunaSubtitulo.size() > 0)){
						primeraRemesaAprobadaProfesional = false;
						if(remesasPagadasProgramaAnoComponenteComunaSubtitulo != null && remesasPagadasProgramaAnoComponenteComunaSubtitulo.size() > 0){
							remesaPagada = true;
							for(DetalleRemesas detalleRemesa : remesasPagadasProgramaAnoComponenteComunaSubtitulo){
								montoRemesaPagada += detalleRemesa.getMontoRemesa();
							}
						}
						if(remesasPendientesProgramaAnoComponenteComunaSubtitulo != null && remesasPendientesProgramaAnoComponenteComunaSubtitulo.size() > 0){
							remesaPendiente = true;
						}
					}else{
						primeraRemesaAprobadaProfesional = true;
					}
					
					List<AntecendentesComunaCalculado> antecendentesComunaCalculados = antecedentesComunaDAO.findAntecendentesComunaCalculadoByComunaServicioDistribucionInicialPercapitaVigente(comuna.getServicioSalud().getId(), comuna.getId(), distribucionInicialPercapita.getIdDistribucionInicialPercapita());
					AntecendentesComunaCalculado antecendentesComunaCalculado = ((antecendentesComunaCalculados != null && antecendentesComunaCalculados.size() > 0) ? antecendentesComunaCalculados.get(0): null);
					if(antecendentesComunaCalculado == null){
						continue;
					}
					
					Long marcoPresupuestario = new Long((antecendentesComunaCalculado.getDesempenoDificil() != null) ? (antecendentesComunaCalculado.getDesempenoDificil() * 12) : 0);
					List<RemesasProgramaVO> remesas = getRemesasPrograma(programaVO.getId(), mes, programaVO.getAno());
					OTPerCapitaVO percapitaVO = new OTPerCapitaVO();
					percapitaVO.setMarcoPresupuestario(marcoPresupuestario);
					percapitaVO.setIdComuna(comuna.getId());
					percapitaVO.setComuna(comuna.getNombre());
					percapitaVO.setTipoComuna(((antecendentesComunaCalculado.getAntecedentesComuna() != null && antecendentesComunaCalculado.getAntecedentesComuna().getClasificacion() != null) ? antecendentesComunaCalculado.getAntecedentesComuna().getClasificacion().getDescripcion() : ""));
					
					if(primeraRemesaAprobadaProfesional){
						Integer cuotaAPagar = 0;
						percapitaVO.setTransferenciaAcumulada(0L);
						percapitaVO.setDiferencia(percapitaVO.getMarcoPresupuestario() - 0L);
						for(Cuota cuota : cuotasPorPrograma){
							if((cuota.getIdMes() == null) || (cuota.getIdMes().getIdMes().intValue() <= mes.intValue())){
								cuotaAPagar++;
							}
						}
						Long montoRemesa = 0L;
						for(int cuotaActual = 1; cuotaActual <= cuotaAPagar; cuotaActual++){
							Cuota cuota = cuotasPorPrograma.get((cuotaActual - 1));
							montoRemesa += antecendentesComunaCalculado.getDesempenoDificil();
							percapitaVO.setCuotaFinal(cuota.getId());
						}
						boolean primerMes = true;
						boolean mesConMontoAsignado = false;
						for(RemesasProgramaVO remesaPrograma : remesas){
							if(primerMes){
								System.out.println("Mes Remesa 1" + remesaPrograma.getMes());
								System.out.println("montoRemesa" + montoRemesa);
								for(DiaVO dia : remesaPrograma.getDias()){
									if(!dia.isBloqueado() && !mesConMontoAsignado){
										System.out.println("dia.getDia()=" + dia.getDia());
										dia.setMonto(montoRemesa);
										mesConMontoAsignado = true;
									}
								}
								primerMes = false;
							}else{
								System.out.println("Mes Remesa 2" + remesaPrograma.getMes());
								System.out.println("montoRemesa" + montoRemesa);
								if(!mesConMontoAsignado){
									for(DiaVO dia : remesaPrograma.getDias()){
										if(!dia.isBloqueado() && !mesConMontoAsignado){
											System.out.println("dia.getDia()=" + dia.getDia());
											dia.setMonto(montoRemesa);
											mesConMontoAsignado = true;
										}
									}
								}
							}
						}
					}else{
						Integer cuotaAPagar = 0;
						percapitaVO.setTransferenciaAcumulada(montoRemesaPagada);
						percapitaVO.setDiferencia(percapitaVO.getMarcoPresupuestario() - montoRemesaPagada);
						for(Cuota cuota : cuotasPorPrograma){
							if((cuota.getIdMes() == null) || (cuota.getIdMes().getIdMes().intValue() <= mes.intValue())){
								cuotaAPagar++;
							}
						}
						Long montoRemesa = new Long((antecendentesComunaCalculado.getDesempenoDificil() != null) ? antecendentesComunaCalculado.getDesempenoDificil() : 0);
						Long montoRemesaMesAnterior = 0L;
						for(int cuotaActual = 1; cuotaActual <= cuotaAPagar; cuotaActual++){
							Cuota cuota = cuotasPorPrograma.get((cuotaActual - 1));
							if(cuotaActual < cuotaAPagar){
								montoRemesaMesAnterior += ((antecendentesComunaCalculado.getDesempenoDificil() != null) ? antecendentesComunaCalculado.getDesempenoDificil() : 0);
							}
							percapitaVO.setCuotaFinal(cuota.getId());
						}
						Long diferenciaRemesaReal = montoRemesaPagada - montoRemesaMesAnterior;
						montoRemesa = montoRemesa + diferenciaRemesaReal;
						boolean primerMes = true;
						boolean mesConMontoAsignado = false;
						for(RemesasProgramaVO remesaPrograma : remesas){
							if(primerMes){
								System.out.println("Mes Remesa 1" + remesaPrograma.getMes());
								System.out.println("montoRemesa" + montoRemesa);
								for(DiaVO dia : remesaPrograma.getDias()){
									if(!dia.isBloqueado() && !mesConMontoAsignado){
										System.out.println("dia.getDia()=" + dia.getDia());
										dia.setMonto(montoRemesa);
										mesConMontoAsignado = true;
									}
								}
								primerMes = false;
							}else{
								System.out.println("Mes Remesa 2" + remesaPrograma.getMes());
								System.out.println("montoRemesa" + montoRemesa);
								if(!mesConMontoAsignado){
									for(DiaVO dia : remesaPrograma.getDias()){
										if(!dia.isBloqueado() && !mesConMontoAsignado){
											System.out.println("dia.getDia()=" + dia.getDia());
											dia.setMonto(montoRemesa);
											mesConMontoAsignado = true;
										}
									}
								}
							}
						}
					}
					percapitaVO.setRemesas(remesas);
					listaReportePercapita.add(percapitaVO);
				}
			}
		}
		return listaReportePercapita;
	}
	
	public List<OTPerCapitaVO> getDetalleRebajaIAAPS(Integer servicioSeleccionado, Integer anoCurso, Integer idProgramaAno) {
		List<OTPerCapitaVO> listaReportePercapita = new ArrayList<OTPerCapitaVO>();
		ProgramaVO programaVO = programasService.getProgramaAno(idProgramaAno);
		List<Cuota> cuotasPorPrograma = reliquidacionDAO.getCuotasByProgramaAno(idProgramaAno);
		Integer mes = Integer.parseInt(getMesCurso(true));
		Integer componenteSeleccionado = null;
		if(programaVO.getComponentes() != null && programaVO.getComponentes().size() > 0){
			componenteSeleccionado = programaVO.getComponentes().get(0).getId();
		}
		
		List<ServicioSalud> servicios = null;
		if(servicioSeleccionado == null){
			servicios = servicioSaludDAO.getServiciosOrderId();
		}else{
			servicios = new ArrayList<ServicioSalud>();
			ServicioSalud servicioSalud = servicioSaludDAO.getById(servicioSeleccionado);
			servicios.add(servicioSalud);
		}
		
		DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(anoCurso);
		for(ServicioSalud servicioSalud : servicios){
			if(servicioSalud.getComunas() != null && servicioSalud.getComunas().size() > 0){
				for(Comuna comuna : servicioSalud.getComunas()){
					List<DetalleRemesas> remesasPagadasProgramaAnoComponenteComunaSubtitulo = remesasDAO.getRemesasPagadasByProgramaAnoComponenteComunaSubtitulo(idProgramaAno, componenteSeleccionado, comuna.getId(), Subtitulo.SUBTITULO24.getId());
					List<DetalleRemesas> remesasPendientesProgramaAnoComponenteComunaSubtitulo = remesasDAO.getRemesasPendientesByProgramaAnoComponenteComunaSubtitulo(idProgramaAno, componenteSeleccionado, comuna.getId(), Subtitulo.SUBTITULO24.getId());
					Boolean primeraRemesaAprobadaProfesional = null;
					boolean remesaPendiente = false;
					boolean remesaPagada = false;
					Long montoRemesaPagada = 0L;
					if((remesasPagadasProgramaAnoComponenteComunaSubtitulo != null && remesasPagadasProgramaAnoComponenteComunaSubtitulo.size() > 0) ||
							(remesasPendientesProgramaAnoComponenteComunaSubtitulo != null && remesasPendientesProgramaAnoComponenteComunaSubtitulo.size() > 0)){
						primeraRemesaAprobadaProfesional = false;
						if(remesasPagadasProgramaAnoComponenteComunaSubtitulo != null && remesasPagadasProgramaAnoComponenteComunaSubtitulo.size() > 0){
							remesaPagada = true;
							for(DetalleRemesas detalleRemesa : remesasPagadasProgramaAnoComponenteComunaSubtitulo){
								montoRemesaPagada += detalleRemesa.getMontoRemesa();
							}
						}
						if(remesasPendientesProgramaAnoComponenteComunaSubtitulo != null && remesasPendientesProgramaAnoComponenteComunaSubtitulo.size() > 0){
							remesaPendiente = true;
						}
					}else{
						primeraRemesaAprobadaProfesional = true;
					}
					
					List<AntecendentesComunaCalculado> antecendentesComunaCalculados = antecedentesComunaDAO.findAntecendentesComunaCalculadoByComunaServicioDistribucionInicialPercapitaVigente(comuna.getServicioSalud().getId(), comuna.getId(), distribucionInicialPercapita.getIdDistribucionInicialPercapita());
					AntecendentesComunaCalculado antecendentesComunaCalculado = ((antecendentesComunaCalculados != null && antecendentesComunaCalculados.size() > 0) ? antecendentesComunaCalculados.get(0): null);
					if(antecendentesComunaCalculado == null){
						continue;
					}
					
					AntecedentesComunaCalculadoRebaja antecedentesComunaCalculadoRebajaSeleccionada = null;
					if(antecendentesComunaCalculado.getAntecedentesComunaCalculadoRebajas() == null || antecendentesComunaCalculado.getAntecedentesComunaCalculadoRebajas().size() == 0){
						continue;
					}else{
						for(AntecedentesComunaCalculadoRebaja antecedentesComunaCalculadoRebaja : antecendentesComunaCalculado.getAntecedentesComunaCalculadoRebajas()){
							if(mes.equals(antecedentesComunaCalculadoRebaja.getRebaja().getRebajaCorte().getMesRebaja().getIdMes())){
								antecedentesComunaCalculadoRebajaSeleccionada = antecedentesComunaCalculadoRebaja;
								break;
							}
						}
					}
					
					if(antecedentesComunaCalculadoRebajaSeleccionada == null){
						continue;
					}
					
					List<RemesasProgramaVO> remesas = getRemesasPrograma(programaVO.getId(), mes, programaVO.getAno());
					OTPerCapitaVO percapitaVO = new OTPerCapitaVO();
					percapitaVO.setMarcoPresupuestario(0L);
					percapitaVO.setTransferenciaAcumulada(0L);
					percapitaVO.setIdComuna(comuna.getId());
					percapitaVO.setComuna(comuna.getNombre());
					percapitaVO.setTipoComuna(((antecendentesComunaCalculado.getAntecedentesComuna() != null && antecendentesComunaCalculado.getAntecedentesComuna().getClasificacion() != null) ? antecendentesComunaCalculado.getAntecedentesComuna().getClasificacion().getDescripcion() : ""));
					if(primeraRemesaAprobadaProfesional){
						Integer cuotaAPagar = 0;
						for(Cuota cuota : cuotasPorPrograma){
							if((cuota.getIdMes() == null) || (cuota.getIdMes().getIdMes().intValue() <= mes.intValue())){
								cuotaAPagar++;
							}
						}
						Long montoRemesa = 0L;
						for(int cuotaActual = 1; cuotaActual <= cuotaAPagar; cuotaActual++){
							Cuota cuota = cuotasPorPrograma.get((cuotaActual - 1));
							percapitaVO.setCuotaFinal(cuota.getId());
						}
						montoRemesa += antecedentesComunaCalculadoRebajaSeleccionada.getMontoRebaja();
						
						boolean primerMes = true;
						boolean mesConMontoAsignado = false;
						for(RemesasProgramaVO remesaPrograma : remesas){
							if(primerMes){
								System.out.println("Mes Remesa 1" + remesaPrograma.getMes());
								System.out.println("montoRemesa" + montoRemesa);
								for(DiaVO dia : remesaPrograma.getDias()){
									if(!dia.isBloqueado() && !mesConMontoAsignado){
										System.out.println("dia.getDia()=" + dia.getDia());
										dia.setMonto(montoRemesa);
										mesConMontoAsignado = true;
									}
								}
								primerMes = false;
							}else{
								System.out.println("Mes Remesa 2" + remesaPrograma.getMes());
								System.out.println("montoRemesa" + montoRemesa);
								if(!mesConMontoAsignado){
									for(DiaVO dia : remesaPrograma.getDias()){
										if(!dia.isBloqueado() && !mesConMontoAsignado){
											System.out.println("dia.getDia()=" + dia.getDia());
											dia.setMonto(montoRemesa);
											mesConMontoAsignado = true;
										}
									}
								}
							}
						}
					}else{
						Integer cuotaAPagar = 0;
						for(Cuota cuota : cuotasPorPrograma){
							if((cuota.getIdMes() == null) || (cuota.getIdMes().getIdMes().intValue() <= mes.intValue())){
								cuotaAPagar++;
							}
						}
						Long montoRemesa = new Long(antecedentesComunaCalculadoRebajaSeleccionada.getMontoRebaja());
						for(int cuotaActual = 1; cuotaActual <= cuotaAPagar; cuotaActual++){
							Cuota cuota = cuotasPorPrograma.get((cuotaActual - 1));
							percapitaVO.setCuotaFinal(cuota.getId());
						}
						boolean primerMes = true;
						boolean mesConMontoAsignado = false;
						for(RemesasProgramaVO remesaPrograma : remesas){
							if(primerMes){
								System.out.println("Mes Remesa 1" + remesaPrograma.getMes());
								System.out.println("montoRemesa" + montoRemesa);
								for(DiaVO dia : remesaPrograma.getDias()){
									if(!dia.isBloqueado() && !mesConMontoAsignado){
										System.out.println("dia.getDia()=" + dia.getDia());
										dia.setMonto(montoRemesa);
										mesConMontoAsignado = true;
									}
								}
								primerMes = false;
							}else{
								System.out.println("Mes Remesa 2" + remesaPrograma.getMes());
								System.out.println("montoRemesa" + montoRemesa);
								if(!mesConMontoAsignado){
									for(DiaVO dia : remesaPrograma.getDias()){
										if(!dia.isBloqueado() && !mesConMontoAsignado){
											System.out.println("dia.getDia()=" + dia.getDia());
											dia.setMonto(montoRemesa);
											mesConMontoAsignado = true;
										}
									}
								}
							}
						}
					}
					percapitaVO.setRemesas(remesas);
					listaReportePercapita.add(percapitaVO);
				}
			}
		}
		return listaReportePercapita;
	}

	public OTPerCapitaVO actualizarComunaPerCapita(Integer idComuna, OTPerCapitaVO registroTabla, Integer idProgramaAno, Integer idSubtitulo, Integer componenteSeleccionado) {
		
		List<Cuota> cuotasPrograma = reliquidacionDAO.getCuotasByProgramaAno(idProgramaAno);
		ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idProgramaAno);
		DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(programaAno.getAno().getAno());
		Cuota cuotaAsociada = null;
		for(Cuota cuota : cuotasPrograma){
			if(cuota.getId().equals(registroTabla.getCuotaFinal())){
				cuotaAsociada = cuota;
				break;
			}
		}

		if(registroTabla.getIdDetalleRemesaEliminar() != null && registroTabla.getIdDetalleRemesaEliminar().size() > 0){
			for(Integer idDetalleRemesa : registroTabla.getIdDetalleRemesaEliminar()){
				DetalleRemesas remesa = remesasDAO.findDetalleRemesaById(idDetalleRemesa);
				if(remesa != null){
					remesasDAO.remove(remesa);
				}
			}
		}

		List<PagaRemesaVO> pagaRemesa = new ArrayList<PagaRemesaVO>();
		for(RemesasProgramaVO registro : registroTabla.getRemesas()){
			for(DiaVO dia : registro.getDias()){
				if(!dia.isBloqueado()){
					PagaRemesaVO paga =  new PagaRemesaVO();
					paga.setDia(dia.getDia());
					paga.setIdMes(registro.getIdMes());
					paga.setMonto(dia.getMonto());
					paga.setBloqueado(false);
					pagaRemesa.add(paga);
				}else{
					PagaRemesaVO paga =  new PagaRemesaVO();
					paga.setDia(dia.getDia());
					paga.setIdMes(registro.getIdMes());
					paga.setMonto(0L);
					paga.setBloqueado(true);
					pagaRemesa.add(paga);
				}
			}
		}
		
		Comuna comuna = comunaDAO.getComunaById(idComuna);
		for(PagaRemesaVO pagando : pagaRemesa){
			long currentTime = Calendar.getInstance().getTimeInMillis();
			DetalleRemesas detalleRemesas = new DetalleRemesas();
			detalleRemesas.setComuna(comuna);
			detalleRemesas.setProgramaAno(programasDAO.getProgramaAnoByID(idProgramaAno));
			detalleRemesas.setRemesaPagada(false);
			detalleRemesas.setFecha(new Date(currentTime));
			detalleRemesas.setSubtitulo(subtituloDAO.getTipoSubtituloById(idSubtitulo));
			detalleRemesas.setMes(utilitariosDAO.findMesById(pagando.getIdMes()));
			detalleRemesas.setDia(utilitariosDAO.findDiaById(pagando.getDia()));
			detalleRemesas.setMontoRemesa(pagando.getMonto().intValue());
			detalleRemesas.setCuota(cuotaAsociada);
			detalleRemesas.setRevisarConsolidador(false);
			detalleRemesas.setBloqueado(pagando.isBloqueado());
			detalleRemesas.setComponente(componenteDAO.getComponenteByID(componenteSeleccionado));
			remesasDAO.save(detalleRemesas);
		}
		
		List<AntecendentesComunaCalculado> antecendentesComunaCalculados = antecedentesComunaDAO.findAntecendentesComunaCalculadoByComunaServicioDistribucionInicialPercapitaVigente(comuna.getServicioSalud().getId(), comuna.getId(), distribucionInicialPercapita.getIdDistribucionInicialPercapita());
		AntecendentesComunaCalculado antecendentesComunaCalculado = ((antecendentesComunaCalculados != null && antecendentesComunaCalculados.size() > 0) ? antecendentesComunaCalculados.get(0): null);
		if(antecendentesComunaCalculado != null){
			antecendentesComunaCalculado.setAprobado(new Boolean(true));
		}
		return registroTabla;
	}
	
public OTPerCapitaVO actualizarDesempenoDificil(Integer idComuna, OTPerCapitaVO registroTabla, Integer idProgramaAno, Integer idSubtitulo, Integer componenteSeleccionado) {
		
		List<Cuota> cuotasPrograma = reliquidacionDAO.getCuotasByProgramaAno(idProgramaAno);
		Cuota cuotaAsociada = null;
		for(Cuota cuota : cuotasPrograma){
			if(cuota.getId().equals(registroTabla.getCuotaFinal())){
				cuotaAsociada = cuota;
				break;
			}
		}

		if(registroTabla.getIdDetalleRemesaEliminar() != null && registroTabla.getIdDetalleRemesaEliminar().size() > 0){
			for(Integer idDetalleRemesa : registroTabla.getIdDetalleRemesaEliminar()){
				DetalleRemesas remesa = remesasDAO.findDetalleRemesaById(idDetalleRemesa);
				if(remesa != null){
					remesasDAO.remove(remesa);
				}
			}
		}

		List<PagaRemesaVO> pagaRemesa = new ArrayList<PagaRemesaVO>();
		for(RemesasProgramaVO registro : registroTabla.getRemesas()){
			for(DiaVO dia : registro.getDias()){
				if(!dia.isBloqueado()){
					PagaRemesaVO paga =  new PagaRemesaVO();
					paga.setDia(dia.getDia());
					paga.setIdMes(registro.getIdMes());
					paga.setMonto(dia.getMonto());
					paga.setBloqueado(false);
					pagaRemesa.add(paga);
				}else{
					PagaRemesaVO paga =  new PagaRemesaVO();
					paga.setDia(dia.getDia());
					paga.setIdMes(registro.getIdMes());
					paga.setMonto(0L);
					paga.setBloqueado(true);
					pagaRemesa.add(paga);
				}
			}
		}
		
		Comuna comuna = comunaDAO.getComunaById(idComuna);
		for(PagaRemesaVO pagando : pagaRemesa){
			long currentTime = Calendar.getInstance().getTimeInMillis();
			DetalleRemesas detalleRemesas = new DetalleRemesas();
			detalleRemesas.setComuna(comuna);
			detalleRemesas.setProgramaAno(programasDAO.getProgramaAnoByID(idProgramaAno));
			detalleRemesas.setRemesaPagada(false);
			detalleRemesas.setFecha(new Date(currentTime));
			detalleRemesas.setSubtitulo(subtituloDAO.getTipoSubtituloById(idSubtitulo));
			detalleRemesas.setMes(utilitariosDAO.findMesById(pagando.getIdMes()));
			detalleRemesas.setDia(utilitariosDAO.findDiaById(pagando.getDia()));
			detalleRemesas.setMontoRemesa(pagando.getMonto().intValue());
			detalleRemesas.setCuota(cuotaAsociada);
			detalleRemesas.setRevisarConsolidador(false);
			detalleRemesas.setBloqueado(pagando.isBloqueado());
			detalleRemesas.setComponente(componenteDAO.getComponenteByID(componenteSeleccionado));
			remesasDAO.save(detalleRemesas);
		}
		return registroTabla;
	}

public OTPerCapitaVO actualizarRebajaIAAPS(Integer idComuna, OTPerCapitaVO registroTabla, Integer idProgramaAno, Integer idSubtitulo, Integer componenteSeleccionado) {
	
	List<Cuota> cuotasPrograma = reliquidacionDAO.getCuotasByProgramaAno(idProgramaAno);
	Cuota cuotaAsociada = null;
	for(Cuota cuota : cuotasPrograma){
		if(cuota.getId().equals(registroTabla.getCuotaFinal())){
			cuotaAsociada = cuota;
			break;
		}
	}

	if(registroTabla.getIdDetalleRemesaEliminar() != null && registroTabla.getIdDetalleRemesaEliminar().size() > 0){
		for(Integer idDetalleRemesa : registroTabla.getIdDetalleRemesaEliminar()){
			DetalleRemesas remesa = remesasDAO.findDetalleRemesaById(idDetalleRemesa);
			if(remesa != null){
				remesasDAO.remove(remesa);
			}
		}
	}

	List<PagaRemesaVO> pagaRemesa = new ArrayList<PagaRemesaVO>();
	for(RemesasProgramaVO registro : registroTabla.getRemesas()){
		for(DiaVO dia : registro.getDias()){
			if(!dia.isBloqueado()){
				PagaRemesaVO paga =  new PagaRemesaVO();
				paga.setDia(dia.getDia());
				paga.setIdMes(registro.getIdMes());
				paga.setMonto(dia.getMonto());
				paga.setBloqueado(false);
				pagaRemesa.add(paga);
			}else{
				PagaRemesaVO paga =  new PagaRemesaVO();
				paga.setDia(dia.getDia());
				paga.setIdMes(registro.getIdMes());
				paga.setMonto(0L);
				paga.setBloqueado(true);
				pagaRemesa.add(paga);
			}
		}
	}
	
	Comuna comuna = comunaDAO.getComunaById(idComuna);
	for(PagaRemesaVO pagando : pagaRemesa){
		long currentTime = Calendar.getInstance().getTimeInMillis();
		DetalleRemesas detalleRemesas = new DetalleRemesas();
		detalleRemesas.setComuna(comuna);
		detalleRemesas.setProgramaAno(programasDAO.getProgramaAnoByID(idProgramaAno));
		detalleRemesas.setRemesaPagada(false);
		detalleRemesas.setFecha(new Date(currentTime));
		detalleRemesas.setSubtitulo(subtituloDAO.getTipoSubtituloById(idSubtitulo));
		detalleRemesas.setMes(utilitariosDAO.findMesById(pagando.getIdMes()));
		detalleRemesas.setDia(utilitariosDAO.findDiaById(pagando.getDia()));
		detalleRemesas.setMontoRemesa(pagando.getMonto().intValue());
		detalleRemesas.setCuota(cuotaAsociada);
		detalleRemesas.setRevisarConsolidador(false);
		detalleRemesas.setBloqueado(pagando.isBloqueado());
		detalleRemesas.setComponente(componenteDAO.getComponenteByID(componenteSeleccionado));
		remesasDAO.save(detalleRemesas);
	}
	return registroTabla;
}

	public String getMesCurso(Boolean numero) {
		SimpleDateFormat dateFormat = null;
		String mesCurso = null;
		if(numero){
			dateFormat = new SimpleDateFormat("MM");
			mesCurso = dateFormat.format(new Date());
		}else{
			dateFormat = new SimpleDateFormat("MMMM");
			mesCurso = dateFormat.format(new Date());
		}
		/*String mesCurso;
		if(numero){
			mesCurso="10";
		}else{
			mesCurso="OCTUBRE";
		}*/
		return mesCurso;
	}

	public OTResumenDependienteServicioVO actualizarServicio(OTResumenDependienteServicioVO registroTabla, Integer idProgramaAno, Integer idSubtitulo, Integer componenteSeleccionado, 
			List<Integer> idDetalleRemesas, Boolean revisarConsolidador, Boolean remesaEstaPagada) {

		System.out.println("idProgramaAno->"+idProgramaAno);
		System.out.println("idSubtitulo->"+idSubtitulo);
		System.out.println("componenteSeleccionado->"+componenteSeleccionado);
		System.out.println("idDetalleRemesas->"+idDetalleRemesas);
		System.out.println("revisarConsolidador->"+revisarConsolidador);
		System.out.println("remesaEstaPagada->"+remesaEstaPagada);
		
		//Buscamos las cuotas del programa y su MP
		ProgramaVO programaVO = programasService.getProgramaAno(idProgramaAno);
		List<Cuota> cuotasPrograma = reliquidacionDAO.getCuotasByProgramaAno(idProgramaAno);
		Cuota cuotaSeleccionada = null;
		for(Cuota cuota : cuotasPrograma){
			if(registroTabla.getCuotaFinal().equals(cuota.getId())){
				cuotaSeleccionada = cuota;
				break;
			}
		}
		Long marcoPresupuestario = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(registroTabla.getEstablecimiento().getId(),
				idProgramaAno,componenteSeleccionado,idSubtitulo);
		
		
		List<DetalleRemesas> remesasPagadasEstablecimiento = remesasDAO.getRemesasPagadasByProgramaAnoComponenteEstablecimientoSubtitulo(idProgramaAno, componenteSeleccionado, registroTabla.getEstablecimiento().getId(), idSubtitulo);

		Long acumulador = 0L;
		for(DetalleRemesas remesaPagada : remesasPagadasEstablecimiento){
			acumulador += remesaPagada.getMontoRemesa();
		}

		List<PagaRemesaVO> pagaRemesa = new ArrayList<PagaRemesaVO>();
		for(RemesasProgramaVO registro : registroTabla.getRemesas()){
			for(DiaVO dia : registro.getDias()){
				if(!dia.isBloqueado() && dia.getMonto() != null && dia.getMonto()>0){
					acumulador += dia.getMonto();
					PagaRemesaVO paga =  new PagaRemesaVO();
					paga.setDia(dia.getDia());
					paga.setIdMes(registro.getIdMes());
					paga.setMonto(dia.getMonto());
					pagaRemesa.add(paga);
				}
			}
		}

		for(PagaRemesaVO pagando : pagaRemesa){
			DetalleRemesas detalleRemesas = new DetalleRemesas();
			detalleRemesas.setEstablecimiento(establecimientosDAO.getEstablecimientoByCodigo(registroTabla.getEstablecimiento().getCodigoEstablecimiento()));
			detalleRemesas.setProgramaAno(programasDAO.getProgramaAnoByID(idProgramaAno));
			detalleRemesas.setRemesaPagada(remesaEstaPagada);
			detalleRemesas.setSubtitulo(subtituloDAO.getTipoSubtituloById(idSubtitulo));
			detalleRemesas.setMes(utilitariosDAO.findMesById(pagando.getIdMes()));
			detalleRemesas.setDia(utilitariosDAO.findDiaById(pagando.getDia()));
			detalleRemesas.setMontoRemesa(pagando.getMonto().intValue());
			detalleRemesas.setRevisarConsolidador(revisarConsolidador);
			detalleRemesas.setCuota(cuotaSeleccionada);
			remesasDAO.save(detalleRemesas);
		}
		
		Integer cuotaInicial = registroTabla.getCuotaInicial();
		cuotaInicial = ((cuotaInicial == null) ? 0 : (cuotaInicial-1));
		
		Long montocuotaAPagar = Math.round( marcoPresupuestario * (cuotasPrograma.get(cuotaInicial).getPorcentaje()/100.0));

		//Si ya cubrí el marco presupuestario con la suma de las remesas pagadas + las ingresadas, entonces agrego la segunda cuota
		if(montocuotaAPagar.equals(acumulador)){
			for(Cuota cuota: cuotasPrograma){
				DetalleRemesas detalleRemesasCuota = new DetalleRemesas();
				detalleRemesasCuota.setEstablecimiento(establecimientosDAO.getEstablecimientoByCodigo(registroTabla.getEstablecimiento().getCodigoEstablecimiento()));
				detalleRemesasCuota.setProgramaAno(programasDAO.getProgramaAnoByID(idProgramaAno));
				detalleRemesasCuota.setRemesaPagada(remesaEstaPagada);
				detalleRemesasCuota.setSubtitulo(subtituloDAO.getTipoSubtituloById(idSubtitulo));

				if(cuota.getIdMes() != null || cuota.getNumeroCuota() != 1){
					Long remesa = Math.round(marcoPresupuestario * (cuota.getPorcentaje()/100.0));
					int day = REMESA_REGULAR;
					while(isWeekend(cuota.getIdMes().getIdMes(), day, programaVO.getAno()) || isFeriado(cuota.getIdMes().getIdMes(), day, programaVO.getAno())){
						day -= 1;
					}
					detalleRemesasCuota.setCuota(cuota);
					detalleRemesasCuota.setMes(cuota.getIdMes());
					detalleRemesasCuota.setDia(utilitariosDAO.findDiaById(day));
					detalleRemesasCuota.setMontoRemesa(remesa.intValue());
					remesasDAO.save(detalleRemesasCuota);
				}
			}
		}
		if(idDetalleRemesas == null){
			System.out.println("Cambiando el estado de los convenios servicio");
			ConvenioServicioComponente convenioServicioComponenteActual = conveniosDAO.getConvenioServicioComponenteById(registroTabla.getIdConvenioServicioComponenteSinAprobar());
			convenioServicioComponenteActual.setAprobadoRevision(true);
				
			ConvenioServicio convenioServicio = convenioServicioComponenteActual.getConvenioServicio();
			boolean aprobar = true;
			for(ConvenioServicioComponente convenioServicioComponente : convenioServicio.getConvenioServicioComponentes()){
				if(convenioServicioComponente.getAprobadoRevision() == null || !convenioServicioComponente.getAprobadoRevision()){
					aprobar = false;
					break;
				}
			}
			if(aprobar){
				convenioServicio.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.TRAMITE.getId()));
			}
		}
		return registroTabla;
	}

	public OTResumenMunicipalVO actualizarMunicipal(OTResumenMunicipalVO registroTabla, Integer idProgramaAno, Integer idSubtitulo, Integer componenteSeleccionado, List<Integer> idDetalleRemesas,
			Boolean revisarConsolidador, Boolean remesaEstaPagada) {

		//Buscamos las cuotas del programa y su MP
		ProgramaVO programaVO = programasService.getProgramaAno(idProgramaAno);
		List<Cuota> cuotasPrograma = reliquidacionDAO.getCuotasByProgramaAno(idProgramaAno);
		
		Cuota cuotaAsociada = null;
		for(Cuota cuota : cuotasPrograma){
			if(cuota.getId().equals(registroTabla.getCuotaFinal())){
				cuotaAsociada = cuota;
				break;
			}
		}

		Long marcoPresupuestario = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(registroTabla.getComuna().getIdComuna(), idProgramaAno,
				componenteSeleccionado, idSubtitulo);

		List<DetalleRemesas> remesasPagadasComuna = remesasDAO.getRemesasPagadasByProgramaAnoComponenteComunaSubtitulo(idProgramaAno, componenteSeleccionado, registroTabla.getComuna().getIdComuna(), idSubtitulo);

		Long acumulador = 0L;
		for(DetalleRemesas remesaPagada : remesasPagadasComuna){
			acumulador += remesaPagada.getMontoRemesa();
		}

		List<PagaRemesaVO> pagaRemesa = new ArrayList<PagaRemesaVO>();
		for(RemesasProgramaVO registro : registroTabla.getRemesas()){
			for(DiaVO dia : registro.getDias()){
				if(!dia.isBloqueado() && dia.getMonto()!=null && dia.getMonto()>0){
					acumulador += dia.getMonto();
					PagaRemesaVO paga =  new PagaRemesaVO();
					paga.setDia(dia.getDia());
					paga.setIdMes(registro.getIdMes());
					paga.setMonto(dia.getMonto());
					pagaRemesa.add(paga);
				}
			}
		}

		for(PagaRemesaVO pagando : pagaRemesa){
			DetalleRemesas detalleRemesas = new DetalleRemesas();
			detalleRemesas.setComuna(comunaDAO.getComunaById(registroTabla.getComuna().getIdComuna()));
			detalleRemesas.setProgramaAno(programasDAO.getProgramaAnoByID(idProgramaAno));
			detalleRemesas.setRemesaPagada(true);
			detalleRemesas.setSubtitulo(subtituloDAO.getTipoSubtituloById(idSubtitulo));
			detalleRemesas.setMes(utilitariosDAO.findMesById(pagando.getIdMes()));
			detalleRemesas.setDia(utilitariosDAO.findDiaById(pagando.getDia()));
			detalleRemesas.setMontoRemesa(pagando.getMonto().intValue());
			detalleRemesas.setCuota(cuotaAsociada);
			detalleRemesas.setRevisarConsolidador(revisarConsolidador);
			remesasDAO.save(detalleRemesas);
		}

		Long cuota1 = Math.round(marcoPresupuestario*(cuotasPrograma.get(0).getPorcentaje()/100.0));

		//Si ya cubrí el marco presupuestario con la suma de las remesas pagadas + las ingresadas, entonces agrego la segunda cuota
		if(cuota1.equals(acumulador)){
			for(Cuota cuota: cuotasPrograma){
				DetalleRemesas detalleRemesasCuota = new DetalleRemesas();
				detalleRemesasCuota.setComuna(comunaDAO.getComunaById(registroTabla.getComuna().getIdComuna()));
				detalleRemesasCuota.setProgramaAno(programasDAO.getProgramaAnoByID(idProgramaAno));
				detalleRemesasCuota.setRemesaPagada(true);
				detalleRemesasCuota.setSubtitulo(subtituloDAO.getTipoSubtituloById(idSubtitulo));

				if(cuota.getIdMes() != null || cuota.getNumeroCuota() != 1){
					Long remesa = Math.round(marcoPresupuestario*(cuota.getPorcentaje()/100.0));
					int day = REMESA_REGULAR;
					while(isWeekend(cuota.getIdMes().getIdMes(), day, programaVO.getAno()) || isFeriado(cuota.getIdMes().getIdMes(), day, programaVO.getAno())){
						day-=1;
					}
					detalleRemesasCuota.setCuota(cuota);
					detalleRemesasCuota.setMes(cuota.getIdMes());
					detalleRemesasCuota.setDia(utilitariosDAO.findDiaById(day));
					detalleRemesasCuota.setMontoRemesa(remesa.intValue());
					detalleRemesasCuota.setRemesaPagada(remesaEstaPagada);
					remesasDAO.save(detalleRemesasCuota);
				}
			}
		}
		if(idDetalleRemesas == null){
			System.out.println("Cambiando el estado de los convenios servicio");
			for(Integer idConvenio : registroTabla.getIdConveniosAprobados()){
				System.out.println("Convenio a actualizar-->"+idConvenio);
				ConvenioComuna convenioComuna = conveniosDAO.getConvenioComunaById(idConvenio);
				convenioComuna.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.TRAMITE.getId()));
			}
		}
		return registroTabla;
	}
 
	public void cambiarEstadoPrograma(Integer idPrograma, Integer ano, EstadosProgramas estadoPrograma) {
		System.out.println("idPrograma-->" + idPrograma + " estadoPrograma.getId()-->"+estadoPrograma.getId());
		ProgramaAno programaAno = programasDAO.getProgramaAnoByIdProgramaAndAno(idPrograma, ano);
		programaAno.setEstadoOT(new EstadoPrograma(estadoPrograma.getId()));
		System.out.println("Cambia estado ok");
	}

	public List<ResumenProgramaMixtoVO> getResumenPrograma(
			ProgramaVO programaResumen) {

		List<ResumenProgramaMixtoVO>  resumenPrograma = new ArrayList<ResumenProgramaMixtoVO>();
		List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();

		for(ServicioSalud servicio : servicios){
			System.out.println(servicio.getNombre());
			ResumenProgramaMixtoVO resumen = new ResumenProgramaMixtoVO();
			resumen.setIdServicio(servicio.getId());
			resumen.setNombreServicio(servicio.getNombre());
			Boolean remesaPagada = new Boolean(false);
			List<DetalleRemesas> remesasPorEstablecimientos = remesasDAO.getRemesasServicioAprobadosConsolidadorMesProgramaAnoServicio(
					Integer.parseInt(getMesCurso(true)), programaResumen.getIdProgramaAno(), servicio.getId(), remesaPagada);

			
			List<DetalleRemesas> remesasPorComunas = remesasDAO.getRemesasComunaAprobadosConsolidadorMesProgramaAnoServicio(
					Integer.parseInt(getMesCurso(true)), programaResumen.getIdProgramaAno(), servicio.getId(), remesaPagada);

			Long acumuladorS21 = 0L;
			Long acumuladorS22 = 0L;
			Long acumuladorS29 = 0L;
			Long acumuladorS24 = 0L;
			for(DetalleRemesas remesa : remesasPorEstablecimientos){
				if(remesa.getSubtitulo().getIdTipoSubtitulo() == Subtitulo.SUBTITULO21.getId()){
					acumuladorS21 += remesa.getMontoRemesa();
				}
				if(remesa.getSubtitulo().getIdTipoSubtitulo() == Subtitulo.SUBTITULO22.getId()){
					acumuladorS22 += remesa.getMontoRemesa();
				}
				if(remesa.getSubtitulo().getIdTipoSubtitulo() == Subtitulo.SUBTITULO29.getId()){
					acumuladorS29 += remesa.getMontoRemesa();
				}
				if(remesa.getSubtitulo().getIdTipoSubtitulo() == Subtitulo.SUBTITULO24.getId()){
					acumuladorS24 += remesa.getMontoRemesa();
				}
			}
			for(DetalleRemesas remesa : remesasPorComunas){
				if(remesa.getSubtitulo().getIdTipoSubtitulo() == Subtitulo.SUBTITULO21.getId()){
					acumuladorS21 += remesa.getMontoRemesa();
				}
				if(remesa.getSubtitulo().getIdTipoSubtitulo() == Subtitulo.SUBTITULO22.getId()){
					acumuladorS22 += remesa.getMontoRemesa();
				}
				if(remesa.getSubtitulo().getIdTipoSubtitulo() == Subtitulo.SUBTITULO29.getId()){
					acumuladorS29 += remesa.getMontoRemesa();
				}
				if(remesa.getSubtitulo().getIdTipoSubtitulo() == Subtitulo.SUBTITULO24.getId()){
					acumuladorS24 += remesa.getMontoRemesa();
				}
			}
			resumen.setTotalS21(acumuladorS21);
			resumen.setTotalS22(acumuladorS22);
			resumen.setTotalS29(acumuladorS29);
			resumen.setTotalS24(acumuladorS24);
			resumenPrograma.add(resumen);
		}

		return resumenPrograma;
	}

	public List<ResumenFONASAServicioVO> cargarFonasaServicio(Integer idSubtitulo, Integer anoCurso) {
		List<ResumenFONASAServicioVO> resultado =  new ArrayList<ResumenFONASAServicioVO>();
		List<ServicioSalud> listaServicios = utilitariosDAO.getServicios();
		
		for(ServicioSalud servicio : listaServicios){
			ResumenFONASAServicioVO resumen = new ResumenFONASAServicioVO();
			resumen.setIdServicio(servicio.getId());
			resumen.setNombreServicio(servicio.getNombre());
			Long totalServicio=0l;
			List<ProgramaFonasaVO> programasFonasa = programasService.getProgramasFonasa(true);
			List<ProgramaFonasaVO> otrosProgramas = programasService.getProgramasFonasa(false);
			for(ProgramaFonasaVO fonasa: programasFonasa){
				//Integer progAno = programasDAO.getIdProgramaAnoAnterior(fonasa.getIdPrograma(), anoCurso);
				
				ProgramaVO programa = programasService.getProgramaByIdProgramaAndAno(fonasa.getIdPrograma(), anoCurso);
				
				/*if(progAno == null){
					programasService.evaluarAnoSiguiente(fonasa.getIdPrograma(), anoCurso);
				}*/
				
				List<DetalleRemesas> remesas = remesasDAO.getRemesasMesActualByMesProgramaAnoServicioSubtitulo1(Integer.parseInt(getMesCurso(true)),
						programa.getIdProgramaAno(), servicio.getId(), idSubtitulo, false);
				Long acumulador = 0L;
				for(DetalleRemesas detalle : remesas){
					acumulador += detalle.getMontoRemesa();
				}
				fonasa.setMonto(fonasa.getMonto() + acumulador);
				totalServicio += acumulador;
			}
			Long totalOtros = 0L;
			for(ProgramaFonasaVO otros: otrosProgramas){
				if(otros.getIdPrograma()>0){
					ProgramaVO programa = programasService.getProgramaByIdProgramaAndAno(otros.getIdPrograma(), anoCurso);
					if(programa!=null){
						System.out.println("Integer.parseInt(getMesCurso(true)="+Integer.parseInt(getMesCurso(true)));
						System.out.println("programa.getIdProgramaAno()="+programa.getIdProgramaAno());
						System.out.println("servicio.getId()="+servicio.getId());
						System.out.println("idSubtitulo="+idSubtitulo);
						List<DetalleRemesas> remesas = remesasDAO.getRemesasMesActualByMesProgramaAnoServicioSubtitulo1(Integer.parseInt(getMesCurso(true)),
								programa.getIdProgramaAno(), servicio.getId(), idSubtitulo, false);

						for(DetalleRemesas detalle : remesas){
							totalOtros += detalle.getMontoRemesa();
							totalServicio += detalle.getMontoRemesa();
						}
					}
				}
			}
			resumen.setProgramasFonasa(programasFonasa);
			resumen.setTotalOtrosProgramas(totalOtros);
			resumen.setTotal(totalServicio);
			resultado.add(resumen);
		}
		return resultado;
	}

	public List<ResumenFONASAMunicipalVO> cargarFonasaMunicipal(Integer anoCurso) {
		List<ResumenFONASAMunicipalVO> resultado =  new ArrayList<ResumenFONASAMunicipalVO>();
		List<ServicioSalud> listaServicios = utilitariosDAO.getServicios();
		DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(anoCurso);
		for(ServicioSalud servicio : listaServicios){
			ResumenFONASAMunicipalVO resumen = new ResumenFONASAMunicipalVO();
			resumen.setIdServicio(servicio.getId());
			resumen.setNombreServicio(servicio.getNombre());
			Long totalServicio = 0L;
			List<ProgramaFonasaVO> programasFonasa = programasService.getProgramasFonasa(true);
			List<ProgramaFonasaVO> otrosProgramas = programasService.getProgramasFonasa(false);

			for(ProgramaFonasaVO fonasa: programasFonasa){
				ProgramaVO programa = programasService.getProgramaByIdProgramaAndAno(fonasa.getIdPrograma(), anoCurso);
				List<DetalleRemesas> remesas = remesasDAO.getRemesasMesActualByMesProgramaAnoServicioSubtitulo2(Integer.parseInt(getMesCurso(true)),
						programa.getIdProgramaAno(), servicio.getId(), Subtitulo.SUBTITULO24.getId(), false);
				Long acumulador=0l;
				for(DetalleRemesas detalle : remesas){
					acumulador += detalle.getMontoRemesa();
				}
				fonasa.setMonto(fonasa.getMonto()+acumulador);
				totalServicio += acumulador;
			}

			Long totalOtros = 0L;
			for(ProgramaFonasaVO otros: otrosProgramas){
				//Descartamos percapita id = -1
				if(otros.getIdPrograma()>0){
					ProgramaVO programa = programasService.getProgramaByIdProgramaAndAno(otros.getIdPrograma(), anoCurso);
					if(programa != null){
						List<DetalleRemesas> remesas = remesasDAO.getRemesasMesActualByMesProgramaAnoServicioSubtitulo2(Integer.parseInt(getMesCurso(true)),
								programa.getIdProgramaAno(), servicio.getId(), Subtitulo.SUBTITULO24.getId(), false);

						for(DetalleRemesas detalle : remesas){
							totalOtros += detalle.getMontoRemesa();
							totalServicio += detalle.getMontoRemesa();
						}
					}
				
				}
			}

			resumen.setProgramasFonasa(programasFonasa);
			resumen.setTotalOtrosProgramas(totalOtros);

			
			Object resultPerCapitaBasal = antecedentesComunaDAO.getPerCapitaBasalByIdServicio(servicio.getId(), distribucionInicialPercapita.getIdDistribucionInicialPercapita());
			Long perCapitaBasal = 0L;
			if(resultPerCapitaBasal != null){
				perCapitaBasal = ((Number)(resultPerCapitaBasal)).longValue();
			}
			resumen.setPerCapitaBasal(perCapitaBasal);

			Object resultDesempenoDificil = antecedentesComunaDAO.getDesempenoDificilByIdServicio(servicio.getId(), distribucionInicialPercapita.getIdDistribucionInicialPercapita());
			Long desempenoDificil = 0L;
			if(resultDesempenoDificil != null){
				desempenoDificil = ((Number)(resultDesempenoDificil)).longValue();
			}
			resumen.setDesempenoDificil(desempenoDificil);

			/*Object resultRebaja = antecedentesComunaDAO.getRebajaByIdServicioMesActual();
			Long rebaja =0l;
			if(resultRebaja != null){
				rebaja = ((Number)(resultRebaja)).longValue();
			}*/
			resumen.setRebaja(0l);
			resumen.setDescuentoRetiro(0l);

			Long totalPercapita = resumen.getPerCapitaBasal() + resumen.getDesempenoDificil() - resumen.getRebaja() - resumen.getDescuentoRetiro();
			resumen.setTotalPercapita(totalPercapita);
			resumen.setTotal(totalServicio+totalPercapita);
			resultado.add(resumen);
		}
		return resultado;
	}

	public void administrarVersionesAlfresco(Integer idProcesoOT) {
		System.out.println("administrar Versiones Alfresco=" + idProcesoOT);
		/*List<DocumentoRemesas> documentosRemesas = remesasDAO.getByIdOTTipoNotFinal(idProcesoOT, TipoDocumentosProcesos.RESUMENCONSOLIDADOFONASA);
		if(documentosRemesas != null && documentosRemesas.size() > 0){
			for(DocumentoRemesas documentoRemesa : documentosRemesas){
				String key = ((documentoRemesa.getDocumento().getNodeRef() == null) ? documentoRemesa.getDocumento().getPath() : documentoRemesa.getDocumento().getNodeRef().replace("workspace://SpacesStore/", ""));
				System.out.println("key->"+key);
				alfrescoService.delete(key);
				remesasDAO.deleteDocumentoRemesa(documentoRemesa.getIdDocumentoRemesas());
				remesasDAO.deleteDocumento(documentoRemesa.getDocumento().getId());
			}
		}*/
		
		List<DocumentoRemesas> documentosRemesas = remesasDAO.getByIdOTTipoNotFinal(idProcesoOT, TipoDocumentosProcesos.ORDINARIOOREDENTRANSFERENCIA);
		if(documentosRemesas != null && documentosRemesas.size() > 0){
			for(DocumentoRemesas documentoRemesa : documentosRemesas){
				String key = ((documentoRemesa.getDocumento().getNodeRef() == null) ? documentoRemesa.getDocumento().getPath() : documentoRemesa.getDocumento().getNodeRef().replace("workspace://SpacesStore/", ""));
				System.out.println("key->"+key);
				alfrescoService.delete(key);
				remesasDAO.deleteDocumentoRemesa(documentoRemesa.getIdDocumentoRemesas());
				remesasDAO.deleteDocumento(documentoRemesa.getDocumento().getId());
			}
		}
		System.out.println("Fin administrar Versiones Alfresco");
	}

	public Integer createSeguimientoOT(Integer idProcesoOT, TareasSeguimiento tareaSeguimiento, String subject, String body, String username, List<String> para,
			List<String> conCopia, List<String> conCopiaOculta, List<Integer> documentos, Integer ano) {
		System.out.println("CREAR SEGUIMIENTO OT");
		String from = usuarioDAO.getEmailByUsername(username);
		if(from == null){
			throw new RuntimeException("Usuario no tiene un email valido");
		}

		List<ReferenciaDocumentoSummaryVO> documentosTmp = new ArrayList<ReferenciaDocumentoSummaryVO>();
		if(documentos != null && documentos.size() > 0){
			for(Integer referenciaDocumentoId : documentos){
				MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
				ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO = documentService.getDocumentSummary(referenciaDocumentoId);
				documentosTmp.add(referenciaDocumentoSummaryVO);
				String contenType= mimemap.getContentType(referenciaDocumentoSummaryVO.getPath().toLowerCase());
				BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummaryVO.getPath()), contenType, folderOrdenesTransferencia.replace("{ANO}", ano.toString()));
				System.out.println("response upload template --->"+response);
				documentService.updateDocumentTemplate(referenciaDocumentoId, response.getNodeRef(), response.getFileName(), contenType);
			}
		}
		Integer idSeguimiento = seguimientoService.createSeguimiento(tareaSeguimiento, subject, body, from, para, conCopia, conCopiaOculta, documentosTmp);
		Seguimiento seguimiento = seguimientoDAO.getSeguimientoById(idSeguimiento);
		return remesasDAO.createSeguimiento(idProcesoOT, seguimiento);
	}

	public Integer crearInstanciaOT(String username) {
		System.out.println("username--> " + username);
		Usuario usuario = this.usuarioDAO.getUserByUsername(username);
		String mesCurso = getMesCurso(true);
		Mes mes = mesDAO.getMesPorID(Integer.parseInt(mesCurso));

		return remesasDAO.crearInstanciaOT(usuario,mes,new Date());

	}

	public List<SeguimientoVO> getBitacora(Integer idProcesoOT,
			TareasSeguimiento tareaSeguimiento) {
		List<SeguimientoVO> bitacora = new ArrayList<SeguimientoVO>();
		List<Seguimiento> bitacoraSeguimiento = seguimientoDAO.getBitacoraOT(idProcesoOT, tareaSeguimiento);
		if(bitacoraSeguimiento != null && bitacoraSeguimiento.size() > 0){
			for(Seguimiento seguimiento : bitacoraSeguimiento){
				bitacora.add(new SeguimientoMapper().getBasic(seguimiento));
			}
		}
		return bitacora;
	}

	public Integer generarOficiosTransferencia(TipoDocumentosProcesos plantillaOrdinarioOrdenTransferencia, String idProcesoOT, Long totalFinal, Integer ano) {
		System.out.println("GENERAR OFICIOS DE OT");
		Integer plantillaOrdinarioOT = documentService.getPlantillaByType(plantillaOrdinarioOrdenTransferencia);

		if (plantillaOrdinarioOT == null) {
			throw new RuntimeException(
					"No se puede crear Borrador Decreto Aporte Estatal, la plantilla no esta cargada");
		}

		try {

			ReferenciaDocumentoSummaryVO referenciaDocOT = documentService.getDocumentByPlantillaId(plantillaOrdinarioOT);
			DocumentoVO documentoOT = documentService.getDocument(referenciaDocOT.getId());
			String templateOrdinarioOT = tmpDirDoc + File.separator + documentoOT.getName();
			templateOrdinarioOT = templateOrdinarioOT.replace(" ", "");

			String filenameBorradorOrdinarioOT = tmpDirDoc+ File.separator + new Date().getTime() + "_"
					+ "Resolucion_Ordenes_de_Transferencia.docx";
			System.out.println("filenameBorradorOrdinarioOT filename-->"
					+ filenameBorradorOrdinarioOT);
			System.out.println("templateOrdinarioProgramasAPS template-->"
					+ templateOrdinarioOT);
			GeneradorWord generadorResolucionOT = new GeneradorWord(
					templateOrdinarioOT);
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contentType = mimemap
					.getContentType(templateOrdinarioOT
							.toLowerCase());
			System.out.println("contenTypeBorradorAporteEstatal->"
					+ contentType);

			generadorResolucionOT.saveContent(documentoOT
					.getContent(), XWPFDocument.class);

			Map<String, Object> parametersBorradorResolucionOT = new HashMap<String, Object>();
			parametersBorradorResolucionOT.put("{ano}", ano);
			parametersBorradorResolucionOT.put("{mes}", getMesCurso(false));
			parametersBorradorResolucionOT.put("{total}",String.format("%, d", totalFinal));

			GeneradorResolucionAporteEstatal generadorWordDecretoBorradorAporteEstatal = new GeneradorResolucionAporteEstatal(
					filenameBorradorOrdinarioOT,
					templateOrdinarioOT);
			generadorWordDecretoBorradorAporteEstatal.replaceValues(parametersBorradorResolucionOT, XWPFDocument.class);

			 
			BodyVO response = alfrescoService.uploadDocument(new File(filenameBorradorOrdinarioOT), contentType, folderOrdenesTransferencia.replace("{ANO}", ano.toString()));
			System.out.println("response responseBorradorAporteEstatal --->" + response);

			plantillaOrdinarioOT = documentService.createDocumentRemesas(TipoDocumentosProcesos.ORDINARIOOREDENTRANSFERENCIA, response.getNodeRef(),
							response.getFileName(), contentType, idProcesoOT);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException ex){
			ex.printStackTrace();
		} catch (Docx4JException de){
			de.printStackTrace();
		}

		return plantillaOrdinarioOT;


	}

	public Long generarExcelFonasaOT(TipoDocumentosProcesos tipoDocumentoProceso, String idProcesoOT, Integer ano) {

		Long totalFinal=0l;
		Integer plantillaId = 0;
		List<ProgramaFonasaVO> encabezadoFonasa = programasService.getProgramasFonasa(true);
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();

		List<CellExcelVO> header = new ArrayList<CellExcelVO>();
		header.add(new CellExcelVO("COD.",1,2));	
		header.add(new CellExcelVO("SERVICIO DE SALUD",1,2));
		header.add(new CellExcelVO("SUBTITULO 24",(encabezadoFonasa.size()+7),1));
		header.add(new CellExcelVO("SUBTITULO 21",(encabezadoFonasa.size()+2),1));
		header.add(new CellExcelVO("SUBTITULO 22",(encabezadoFonasa.size()+2),1));
		header.add(new CellExcelVO("SUBTITULO 29",(encabezadoFonasa.size()+2),1));
		header.add(new CellExcelVO("TOTAL ($)",1,2));

		List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
		subHeader.add(new CellExcelVO("Per Capita Basal",1,1));
		subHeader.add(new CellExcelVO("ADDF",1,1));
		subHeader.add(new CellExcelVO("Descto Retiro Leyes 20.157 y 20.589",1,1));
		subHeader.add(new CellExcelVO("Rebaja Incumpl.IAAPS",1,1));
		subHeader.add(new CellExcelVO("Total Per Capita ($)",1,1));
		for(ProgramaFonasaVO encabezado: encabezadoFonasa){
			subHeader.add(new CellExcelVO(encabezado.getNombrePrograma(),1,1));
		}
		subHeader.add(new CellExcelVO("Otros Ref. Municipal ($)",1,1));
		subHeader.add(new CellExcelVO("Total Ref. Municipal ($)",1,1));
		for(ProgramaFonasaVO encabezado: encabezadoFonasa){
			subHeader.add(new CellExcelVO(encabezado.getNombrePrograma(),1,1));
		}
		subHeader.add(new CellExcelVO("Otros Ref. SS 21 ($)",1,1));
		subHeader.add(new CellExcelVO("Total Ref. Servicios Subt. 21 ($)",1,1));
		for(ProgramaFonasaVO encabezado: encabezadoFonasa){
			subHeader.add(new CellExcelVO(encabezado.getNombrePrograma(),1,1));
		}
		subHeader.add(new CellExcelVO("Otros Ref. SS 22 ($)",1,1));
		subHeader.add(new CellExcelVO("Total Ref. Servicios Subt. 22 ($)",1,1));
		for(ProgramaFonasaVO encabezado: encabezadoFonasa){
			subHeader.add(new CellExcelVO(encabezado.getNombrePrograma(),1,1));
		}
		subHeader.add(new CellExcelVO("Otros Ref. SS 29 ($)",1,1));
		subHeader.add(new CellExcelVO("Total Ref. Servicios Subt. 29 ($)",1,1));

		String filename = tmpDir + File.separator;
		filename += "PlantillaResumenOrdenesTransferenciaFormatoFONASA.xlsx";
		GeneradorExcel generadorExcel = new GeneradorExcel(filename);
		String contenType = mimemap.getContentType(filename.toLowerCase());

		List<String> cabezas = new ArrayList<String>();
		for(CellExcelVO head : header){
			String h = head.getName();
			cabezas.add(h);
		}

		List<PlanillaResumenFonasaVO> resumenFonasa = new ArrayList<PlanillaResumenFonasaVO>();
		List<ServicioSalud> listaServicios = utilitariosDAO.getServicios();
		DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(ano);
		for(ServicioSalud servicio : listaServicios){
			PlanillaResumenFonasaVO resumen = new PlanillaResumenFonasaVO();
			resumen.setIdServicio(servicio.getId());
			resumen.setNombreServicio(servicio.getNombre());

			Object resultPerCapitaBasal = antecedentesComunaDAO.getPerCapitaBasalByIdServicio(servicio.getId(), distribucionInicialPercapita.getIdDistribucionInicialPercapita());
			Long perCapitaBasal = 0l;
			if(resultPerCapitaBasal != null){
				perCapitaBasal = ((Number)(resultPerCapitaBasal)).longValue();
			}
			resumen.setPerCapitaBasal(perCapitaBasal);

			Object resultDesempenoDificil = antecedentesComunaDAO.getDesempenoDificilByIdServicio(servicio.getId(), distribucionInicialPercapita.getIdDistribucionInicialPercapita());
			Long desempenoDificil =0l;
			if(resultDesempenoDificil != null){
				desempenoDificil = ((Number)(resultDesempenoDificil)).longValue();
			}
			resumen.setAddf(desempenoDificil);

			resumen.setRebajaIaaps(0l);
			resumen.setDesctoLeyes(0l);

			Long totalPerCapita = resumen.getPerCapitaBasal() + resumen.getAddf() - resumen.getRebajaIaaps() - resumen.getDesctoLeyes();
			resumen.setTotalPerCapita(totalPerCapita);

			resumen.setFonasaS24(cargarFonasa(servicio.getId(), Subtitulo.SUBTITULO24.getId(), ano));
			resumen.setOtrosS24(cargarOtrosProgramas(servicio.getId(), Subtitulo.SUBTITULO24.getId(), ano));
			resumen.setTotalS24(calculaTotal(totalPerCapita,resumen.getFonasaS24(),resumen.getOtrosS24()));


			resumen.setFonasaS21(cargarFonasa(servicio.getId(),Subtitulo.SUBTITULO21.getId(), ano));
			resumen.setOtrosS21(cargarOtrosProgramas(servicio.getId(),Subtitulo.SUBTITULO21.getId(), ano));
			resumen.setTotalS21(calculaTotal(0l,resumen.getFonasaS21(),resumen.getOtrosS21()));

			resumen.setFonasaS22(cargarFonasa(servicio.getId(),Subtitulo.SUBTITULO22.getId(), ano));
			resumen.setOtrosS22(cargarOtrosProgramas(servicio.getId(),Subtitulo.SUBTITULO22.getId(), ano));
			resumen.setTotalS22(calculaTotal(0l,resumen.getFonasaS22(),resumen.getOtrosS22()));

			resumen.setFonasaS29(cargarFonasa(servicio.getId(),Subtitulo.SUBTITULO29.getId(), ano));
			resumen.setOtrosS29(cargarOtrosProgramas(servicio.getId(),Subtitulo.SUBTITULO29.getId(), ano));
			resumen.setTotalS29(calculaTotal(0l,resumen.getFonasaS29(),resumen.getOtrosS29()));


			totalFinal += resumen.getTotalServicio();
			resumenFonasa.add(resumen);

		}
		OrdenesTransferenciaSheetExcel ordenesTransferenciaSheetExcel = new OrdenesTransferenciaSheetExcel(header, subHeader, resumenFonasa);
		generadorExcel.addSheet(ordenesTransferenciaSheetExcel, "Hoja 1");
		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderOrdenesTransferencia.replace("{ANO}", ano.toString()));
			plantillaId = documentService.createDocumentRemesas(tipoDocumentoProceso, response.getNodeRef(), response.getFileName(), contenType, idProcesoOT);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return totalFinal;
	}

	private Long calculaTotal(Long totalPerCapita, List<ProgramaFonasaVO> programasFonasa, Long otrosProgramas) {
		Long acumulador=0l;
		for(ProgramaFonasaVO prog : programasFonasa){
			acumulador += prog.getMonto();
		}
		acumulador += totalPerCapita;
		acumulador += otrosProgramas;
		return acumulador;
	}

	private Long cargarOtrosProgramas(Integer idServicio, Integer idSubtitulo, Integer ano) {
		List<ProgramaFonasaVO> otrosProgramas = programasService.getProgramasFonasa(false);
		Long totalOtros = 0L;
		for(ProgramaFonasaVO otros: otrosProgramas){
			if(otros.getIdPrograma() > 0){
				Integer progAno = programasDAO.getIdProgramaAnoAnterior(otros.getIdPrograma(), ano);
				System.out.println("mes->"+Integer.parseInt(getMesCurso(true))+" progAno-->"+progAno+" idServicio->"+idServicio+" idSubtitulo-->"+idSubtitulo );
				List<DetalleRemesas> remesas = remesasDAO.getRemesasMesActualByMesProgramaAnoServicioSubtitulo2(Integer.parseInt(getMesCurso(true)),
						progAno, idServicio, idSubtitulo, true);

				for(DetalleRemesas detalle : remesas){
					totalOtros += detalle.getMontoRemesa();
				}
			}
		}
		return totalOtros;
	}

	private List<ProgramaFonasaVO> cargarFonasa(Integer idServicio, Integer idSubtitulo, Integer ano) {
		List<ProgramaFonasaVO> programasFonasa = programasService.getProgramasFonasa(true);
		for(ProgramaFonasaVO fonasa: programasFonasa){

			Integer progAno = programasDAO.getIdProgramaAnoAnterior(fonasa.getIdPrograma(), ano);
			List<DetalleRemesas> remesas = remesasDAO.getRemesasMesActualByMesProgramaAnoServicioSubtitulo1(Integer.parseInt(getMesCurso(true)),
					progAno, idServicio, idSubtitulo , true);
			Long acumulador = 0L;
			for(DetalleRemesas detalle : remesas){
				acumulador += detalle.getMontoRemesa();
			}
			fonasa.setMonto(fonasa.getMonto()+acumulador);
		}
		return programasFonasa;
	}

	public Integer getIdDocumentoRemesa(Integer idProcesoOT,
			TipoDocumentosProcesos plantillaordinariooredentransferencia) {
		return remesasDAO.getIdDocumentoRemesa(idProcesoOT, plantillaordinariooredentransferencia);
	}

	public void moveToAlfresco(Integer idProcesoOT, Integer referenciaDocumentoId, TipoDocumentosProcesos tipoDocumento, Integer ano, boolean versionFinal) {
		System.out.println("Buscando referenciaDocumentoId="+referenciaDocumentoId);
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummary = documentService.getDocumentSummary(referenciaDocumentoId);
		System.out.println("Buscando referenciaDocumentoSummary="+referenciaDocumentoSummary);
		if(referenciaDocumentoSummary != null){
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contenType= mimemap.getContentType(referenciaDocumentoSummary.getPath().toLowerCase());
			BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummary.getPath()), contenType, folderOrdenesTransferencia.replace("{ANO}", ano.toString() ));
			System.out.println("response upload template --->"+response);
			documentService.updateDocumentTemplate(referenciaDocumentoSummary.getId(), response.getNodeRef(), response.getFileName(), contenType);
			Remesas remesa = remesasDAO.findById(idProcesoOT);
			documentService.createDocumentRemesas(remesa, tipoDocumento, referenciaDocumentoId, versionFinal);
		}

	}

	public void enviarDocumentosFonasa(String idProcesoOT) {
		try{
			Integer idPlanillaFonasa = getIdDocumentoRemesa(Integer.parseInt(idProcesoOT), TipoDocumentosProcesos.RESUMENCONSOLIDADOFONASA);
			Integer idOrdinarioOT = getIdDocumentoRemesa(Integer.parseInt(idProcesoOT), TipoDocumentosProcesos.ORDINARIOOREDENTRANSFERENCIA);
			Integer idPlantillaCorreo = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLAOTCORREO);

			ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryPlantillaCorreoVO = documentService.getDocumentByPlantillaId(idPlantillaCorreo);
			DocumentoVO documentoPlantillaCorreoVO = documentService.getDocument(referenciaDocumentoSummaryPlantillaCorreoVO.getId());
			String templatePlantillaCorreo = tmpDirDoc + File.separator + documentoPlantillaCorreoVO.getName();
			templatePlantillaCorreo = templatePlantillaCorreo.replace(" ", "");

			System.out.println("templatePlantillaCorreo template-->"+templatePlantillaCorreo);
			GeneradorXML generadorXMLPlantillaResolucionRebaja = new GeneradorXML(templatePlantillaCorreo);
			Email emailPLantilla = null;
			try {
				emailPLantilla = generadorXMLPlantillaResolucionRebaja.createObject(Email.class, documentoPlantillaCorreoVO.getContent());
			} catch (JAXBException e1) {
				e1.printStackTrace();
			}

			ReferenciaDocumento resolucion = documentoDAO.findById(idPlanillaFonasa);
			ReferenciaDocumento planilla =  documentoDAO.findById(idOrdinarioOT);

			List<EmailService.Adjunto> adjuntos = new ArrayList<EmailService.Adjunto>();


			DocumentoVO documentoResolucion = documentService.getDocument(resolucion.getId());
			String fileNameResolucion = tmpDirDoc + File.separator + documentoResolucion.getName();

			DocumentoVO documentoPlanilla = documentService.getDocument(planilla.getId());
			String fileNamePlanilla = tmpDirDoc + File.separator + documentoPlanilla.getName();



			FileOutputStream fos = new FileOutputStream(fileNameResolucion);
			fos.write(documentoResolucion.getContent());
			fos.close();

			FileOutputStream fosPlanilla = new FileOutputStream(fileNamePlanilla);
			fosPlanilla.write(documentoPlanilla.getContent());
			fosPlanilla.close();


			EmailService.Adjunto adjunto = new EmailService.Adjunto();
			adjunto.setDescripcion("Resolucion Orden de Transferencia");
			adjunto.setName(documentoResolucion.getName());
			adjunto.setUrl((new File(fileNameResolucion)).toURI().toURL());


			adjuntos.add(adjunto);


			EmailService.Adjunto adjuntoPlanilla = new EmailService.Adjunto();
			adjuntoPlanilla.setDescripcion("Documentos Adjuntos");
			adjuntoPlanilla.setName(documentoPlanilla.getName());
			adjuntoPlanilla.setUrl((new File(fileNamePlanilla)).toURI().toURL());

			adjuntos.add(adjuntoPlanilla);

			Institucion institucion = institucionDAO.findById(Instituciones.FONASA.getId());
			List<String> to = new ArrayList<String>();
			to.add(institucion.getDirector().getEmail().getValor());

			if(emailPLantilla != null && emailPLantilla.getAsunto() != null && emailPLantilla.getCuerpo() != null){
				emailService.sendMail(to,null,null,emailPLantilla.getAsunto(),emailPLantilla.getCuerpo().replaceAll("(\r\n|\n)", "<br />"), adjuntos);
			}else{
				emailService.sendMail(to,null,null,"Resolución Orden de Transferencia","Se adjunta Resolución de Orden de Transferencia", adjuntos);
			}

			ReporteEmailsEnviados reporteEmailsEnviados = new ReporteEmailsEnviados();

			ReporteEmailsDestinatarios destinatarioPara = new ReporteEmailsDestinatarios();
			destinatarioPara.setDestinatario(institucion.getDirector());
			reporteEmailsEnviados.setFecha(new Date());
			remesasDAO.save(reporteEmailsEnviados);

			destinatarioPara.setReporteEmailsEnviado(reporteEmailsEnviados);
			destinatarioPara.setTipoDestinatario(new TipoDestinatario(TiposDestinatarios.PARA.getId()));
			remesasDAO.save(destinatarioPara);


			ReporteEmailsAdjuntos reporteEmailsAdjuntos = new ReporteEmailsAdjuntos();
			reporteEmailsAdjuntos.setDocumento(resolucion);
			reporteEmailsAdjuntos.setReporteEmailsEnviado(reporteEmailsEnviados);
			remesasDAO.save(reporteEmailsAdjuntos);

			ReporteEmailsAdjuntos reporteEmailsAdjuntos2 = new ReporteEmailsAdjuntos();
			reporteEmailsAdjuntos2.setDocumento(planilla);
			reporteEmailsAdjuntos2.setReporteEmailsEnviado(reporteEmailsEnviados);
			remesasDAO.save(reporteEmailsAdjuntos2);

			ReporteEmailsRemesas reporteEmailsRemesas = new ReporteEmailsRemesas();
			reporteEmailsRemesas.setRemesa(remesasDAO.findById(Integer.parseInt(idProcesoOT)));
			reporteEmailsRemesas.setReporteEmailsEnviados(reporteEmailsEnviados);
			remesasDAO.save(reporteEmailsRemesas);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public void enviarDocumentosServicioSalud(String idProcesoOT) {
		Integer idPlantillaCorreo = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLAOTCORREO);
		if(idPlantillaCorreo == null){
			throw new RuntimeException("No se puede crear plantilla correo notificación Resolución Rebaja Aporte Estatal, la plantilla no esta cargada");
		}
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryPlantillaCorreoVO = documentService.getDocumentByPlantillaId(idPlantillaCorreo);
		DocumentoVO documentoPlantillaCorreoVO = documentService.getDocument(referenciaDocumentoSummaryPlantillaCorreoVO.getId());
		String templatePlantillaCorreo = tmpDirDoc + File.separator + documentoPlantillaCorreoVO.getName();
		templatePlantillaCorreo = templatePlantillaCorreo.replace(" ", "");

		System.out.println("templatePlantillaCorreo template-->"+templatePlantillaCorreo);
		GeneradorXML generadorXMLPlantillaResolucionRebaja = new GeneradorXML(templatePlantillaCorreo);
		Email emailPLantilla = null;
		try {
			emailPLantilla = generadorXMLPlantillaResolucionRebaja.createObject(Email.class, documentoPlantillaCorreoVO.getContent());
		} catch (JAXBException e1) {
			e1.printStackTrace();
		}
		try{
			List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
			if(servicios != null && servicios.size() > 0){
				for(ServicioSalud servicio : servicios){
					List<String> to = new ArrayList<String>();
					if(servicio.getDirector() != null && servicio.getDirector().getEmail() != null){
						to.add(servicio.getDirector().getEmail().getValor());
					}
					List<String> cc = new ArrayList<String>();
					List<String> cco = new ArrayList<String>();
					if((servicio.getEncargadoAps() != null) || (servicio.getEncargadoFinanzasAps() != null)){
						if(servicio.getEncargadoAps() != null &&  servicio.getEncargadoAps().getEmail() != null){
							cc.add(servicio.getEncargadoAps().getEmail().getValor());
						}
						if(servicio.getEncargadoFinanzasAps() != null &&  servicio.getEncargadoFinanzasAps().getEmail() != null){
							cc.add(servicio.getEncargadoFinanzasAps().getEmail().getValor());
						}
					}

					/*ReferenciaDocumentoSummaryVO referenciaDocumentoFinalSummaryVO = null;
					List<ReferenciaDocumentoSummaryVO> referenciasDocumentoSummaryVO = documentService.getVersionFinalByServicioDistribucionInicialPercapitaTypes(idDistribucionInicialPercapita, servicio.getId_servicio(), new TipoDocumentosProcesos[] { TipoDocumentosProcesos.RESOLUCIONAPORTEESTATALUR, TipoDocumentosProcesos.RESOLUCIONAPORTEESTATALCF});
					if(referenciasDocumentoSummaryVO != null && referenciasDocumentoSummaryVO.size() > 0){
						for(ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO : referenciasDocumentoSummaryVO){
							String contentType = new MimetypesFileTypeMap().getContentType(referenciaDocumentoSummaryVO.getPath());
							System.out.println("contentType="+contentType+" archivo enviado por correo");
							if (contentType.equals("application/pdf")) {
								referenciaDocumentoFinalSummaryVO = referenciaDocumentoSummaryVO;
								break;
							}
							if(referenciaDocumentoSummaryVO.getPath().indexOf(".") != -1){
								String extension = referenciaDocumentoSummaryVO.getPath().substring(referenciaDocumentoSummaryVO.getPath().lastIndexOf(".") + 1, referenciaDocumentoSummaryVO.getPath().length());
								if("pdf".equalsIgnoreCase(extension)){
									referenciaDocumentoFinalSummaryVO = referenciaDocumentoSummaryVO;
									break;
								}
							}
							referenciaDocumentoFinalSummaryVO = referenciaDocumentoSummaryVO;
						}
					}

					if(referenciaDocumentoFinalSummaryVO != null){
						List<EmailService.Adjunto> adjuntos = new ArrayList<EmailService.Adjunto>();
						DocumentoVO documentDocumentoResolucionVO = documentService.getDocument(referenciaDocumentoFinalSummaryVO.getId());
						String fileNameDocumentoResolucion = tmpDirDoc + File.separator + documentDocumentoResolucionVO.getName();
						GeneradorDocumento generadorDocumento = new GeneradorDocumento(fileNameDocumentoResolucion);
						generadorDocumento.saveContent(documentDocumentoResolucionVO.getContent());//
						EmailService.Adjunto adjunto = new EmailService.Adjunto();
						adjunto.setDescripcion("Resolucion");
						adjunto.setName(documentDocumentoResolucionVO.getName());
						adjunto.setUrl((new File(fileNameDocumentoResolucion)).toURI().toURL());
						adjuntos.add(adjunto);
						if(emailPLantilla != null && emailPLantilla.getAsunto() != null && emailPLantilla.getCuerpo() != null){
							emailService.sendMail(to, cc, cco, emailPLantilla.getAsunto(), emailPLantilla.getCuerpo().replaceAll("(\r\n|\n)", "<br />"), adjuntos);
						}else{
							emailService.sendMail(to, cc, cco , "Resolucion", "Estimados: <br /> <p> se adjuntan los documentos Resolucion</p>", adjuntos);
						}
					}*/
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public List<ReporteEmailsEnviadosVO> getReporteCorreosByIdRemesa(Integer idProcesoOT) {
		List<ReporteEmailsRemesas> emailsRemesas = remesasDAO.getReporteCorreosByidRemesa(idProcesoOT);
		List<ReporteEmailsEnviadosVO> emailsEnviadosVO = new ArrayList<ReporteEmailsEnviadosVO>();

		if(emailsRemesas != null && emailsRemesas.size() > 0){
			for(ReporteEmailsRemesas reporte : emailsRemesas){
				ReporteEmailsEnviadosVO correo = new ReporteEmailsEnviadosVO();
				correo.setFecha(reporte.getReporteEmailsEnviados().getFecha());
				correo.setFechaFormat(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(reporte.getReporteEmailsEnviados().getFecha()));
				Set<ReporteEmailsAdjuntos> adjuntos = reporte.getReporteEmailsEnviados().getReporteEmailsAdjuntosSet();
				if(adjuntos != null && adjuntos.size() > 0){
					List<AdjuntosVO> adjs = new ArrayList<AdjuntosVO>();
					for(ReporteEmailsAdjuntos adj : adjuntos){
						AdjuntosVO ad = new AdjuntosVO();
						ad.setId(adj.getDocumento().getId());
						ad.setNombre(adj.getDocumento().getPath());
						adjs.add(ad);
					}
					System.out.println("adjs.size()="+adjs.size());
					correo.setAdjuntos(adjs);
				}
				Set<ReporteEmailsDestinatarios> destinatarios = reporte.getReporteEmailsEnviados().getReporteEmailsDestinatariosSet();
				if(destinatarios != null && destinatarios.size() > 0){
					List<String> to = new ArrayList<String>();
					List<String> cc = new ArrayList<String>();
					for(ReporteEmailsDestinatarios destina : destinatarios){
						if( TiposDestinatarios.PARA.getId()  == destina.getTipoDestinatario().getIdTipoDestinatario().intValue()){
							String para = destina.getDestinatario().getEmail().getValor();
							to.add(para);
						}else if( TiposDestinatarios.CC.getId()  == destina.getTipoDestinatario().getIdTipoDestinatario().intValue()){
							String copia = destina.getDestinatario().getEmail().getValor();
							cc.add(copia);
						}
					}
					System.out.println("cc="+cc);
					System.out.println("to="+to);
					correo.setCc(cc);
					correo.setTo(to);
				}
				emailsEnviadosVO.add(correo);
			}
		}
		System.out.println("emailsRemesas.size()="+emailsRemesas.size());
		return emailsEnviadosVO;
	}

	public void reestablecerProgramas(Integer ano, int estadoOT) {
		List<ProgramaAno> progAno = programasDAO.findByAno(ano);
		for(ProgramaAno prog : progAno){
			if(EstadosProgramas.FINALIZADO.getId().equals(prog.getEstadoOT().getIdEstadoPrograma())){
				prog.setEstadoOT(new EstadoPrograma(estadoOT));
			}
		}
	}
	
	public List<ProgramaVO> getProgramas(Integer ano) {
		List<ProgramaVO> programasVO = new ArrayList<ProgramaVO>();
		if(ano == null){
			ano = getAnoCurso();
		}
		List<ProgramaAno> programas = programasDAO.findByAno(ano);
		if(programas != null && programas.size() > 0){
			for(ProgramaAno programa : programas){
				programasVO.add(new ProgramaMapper().getBasic(programa));
			}
		}
		return programasVO;
	}
																										  
	public OTResumenMunicipalVO aprobarMontoRemesaProfesional(OTResumenMunicipalVO registroTabla, Integer idProgramaAno, Integer idSubtitulo, Integer componenteSeleccionado) {
		//Buscamos las cuotas del programa y su MP
		
		List<Cuota> cuotasPrograma = reliquidacionDAO.getCuotasByProgramaAno(idProgramaAno);
		Cuota cuotaAsociada = null;
		for(Cuota cuota : cuotasPrograma){
			if(cuota.getId().equals(registroTabla.getCuotaFinal())){
				cuotaAsociada = cuota;
				break;
			}
		}

		if(registroTabla.getIdDetalleRemesaEliminar() != null && registroTabla.getIdDetalleRemesaEliminar().size() > 0){
			for(Integer idDetalleRemesa : registroTabla.getIdDetalleRemesaEliminar()){
				DetalleRemesas remesa = remesasDAO.findDetalleRemesaById(idDetalleRemesa);
				if(remesa != null){
					remesasDAO.remove(remesa);
				}
			}
		}

		List<PagaRemesaVO> pagaRemesa = new ArrayList<PagaRemesaVO>();
		for(RemesasProgramaVO registro : registroTabla.getRemesas()){
			for(DiaVO dia : registro.getDias()){
				if(!dia.isBloqueado()){
					PagaRemesaVO paga =  new PagaRemesaVO();
					paga.setDia(dia.getDia());
					paga.setIdMes(registro.getIdMes());
					paga.setMonto(dia.getMonto());
					paga.setBloqueado(false);
					pagaRemesa.add(paga);
				}else{
					PagaRemesaVO paga =  new PagaRemesaVO();
					paga.setDia(dia.getDia());
					paga.setIdMes(registro.getIdMes());
					paga.setMonto(0L);
					paga.setBloqueado(true);
					pagaRemesa.add(paga);
				}
			}
		}
		
		for(PagaRemesaVO pagando : pagaRemesa){
			long currentTime = Calendar.getInstance().getTimeInMillis();
			DetalleRemesas detalleRemesas = new DetalleRemesas();
			detalleRemesas.setComuna(comunaDAO.getComunaById(registroTabla.getComuna().getIdComuna()));
			detalleRemesas.setProgramaAno(programasDAO.getProgramaAnoByID(idProgramaAno));
			detalleRemesas.setRemesaPagada(false);
			detalleRemesas.setFecha(new Date(currentTime));
			detalleRemesas.setSubtitulo(subtituloDAO.getTipoSubtituloById(idSubtitulo));
			detalleRemesas.setMes(utilitariosDAO.findMesById(pagando.getIdMes()));
			detalleRemesas.setDia(utilitariosDAO.findDiaById(pagando.getDia()));
			detalleRemesas.setMontoRemesa(pagando.getMonto().intValue());
			detalleRemesas.setCuota(cuotaAsociada);
			detalleRemesas.setRevisarConsolidador(false);
			detalleRemesas.setBloqueado(pagando.isBloqueado());
			detalleRemesas.setComponente(componenteDAO.getComponenteByID(componenteSeleccionado));
			remesasDAO.save(detalleRemesas);
			
			ConvenioComunaComponente convenioComunaComponenteActual = conveniosDAO.getConvenioComunaComponenteById(registroTabla.getIdConvenioComunaComponenteSinAprobar());
			convenioComunaComponenteActual.setAprobadoRevision(true);
				
			ConvenioComuna convenioComuna = convenioComunaComponenteActual.getConvenioComuna();
			RemesaConvenios remesaConvenios = new RemesaConvenios();
			remesaConvenios.setConvenioComuna(convenioComuna);
			remesaConvenios.setRemesa(detalleRemesas);
			remesasDAO.save(remesaConvenios);
			if(EstadosConvenios.APROBADO.getId().equals(convenioComuna.getEstadoConvenio().getIdEstadoConvenio())){
				convenioComuna.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.TRAMITE.getId()));
			}
		}
		return registroTabla;
	}
	
	public OTPerCapitaVO aprobarMontoRemesaProfesional(OTPerCapitaVO registroTabla, Integer idProgramaAno, Integer idSubtitulo, Integer componenteSeleccionado) {
		//Buscamos las cuotas del programa y su MP
		List<Cuota> cuotasPrograma = reliquidacionDAO.getCuotasByProgramaAno(idProgramaAno);
		Cuota cuotaAsociada = null;
		for(Cuota cuota : cuotasPrograma){
			if(cuota.getId().equals(registroTabla.getCuotaFinal())){
				cuotaAsociada = cuota;
				break;
			}
		}

		if(registroTabla.getIdDetalleRemesaEliminar() != null && registroTabla.getIdDetalleRemesaEliminar().size() > 0){
			for(Integer idDetalleRemesa : registroTabla.getIdDetalleRemesaEliminar()){
				DetalleRemesas remesa = remesasDAO.findDetalleRemesaById(idDetalleRemesa);
				if(remesa != null){
					remesasDAO.remove(remesa);
				}
			}
		}

		List<PagaRemesaVO> pagaRemesa = new ArrayList<PagaRemesaVO>();
		for(RemesasProgramaVO registro : registroTabla.getRemesas()){
			for(DiaVO dia : registro.getDias()){
				if(!dia.isBloqueado()){
					PagaRemesaVO paga =  new PagaRemesaVO();
					paga.setDia(dia.getDia());
					paga.setIdMes(registro.getIdMes());
					paga.setMonto(dia.getMonto());
					paga.setBloqueado(false);
					pagaRemesa.add(paga);
				}else{
					PagaRemesaVO paga =  new PagaRemesaVO();
					paga.setDia(dia.getDia());
					paga.setIdMes(registro.getIdMes());
					paga.setMonto(0L);
					paga.setBloqueado(true);
					pagaRemesa.add(paga);
				}
			}
		}
		
		for(PagaRemesaVO pagando : pagaRemesa){
			long currentTime = Calendar.getInstance().getTimeInMillis();
			DetalleRemesas detalleRemesas = new DetalleRemesas();
			detalleRemesas.setComuna(comunaDAO.getComunaById(registroTabla.getIdComuna()));
			detalleRemesas.setProgramaAno(programasDAO.getProgramaAnoByID(idProgramaAno));
			detalleRemesas.setRemesaPagada(false);
			detalleRemesas.setFecha(new Date(currentTime));
			detalleRemesas.setSubtitulo(subtituloDAO.getTipoSubtituloById(idSubtitulo));
			detalleRemesas.setMes(utilitariosDAO.findMesById(pagando.getIdMes()));
			detalleRemesas.setDia(utilitariosDAO.findDiaById(pagando.getDia()));
			detalleRemesas.setMontoRemesa(pagando.getMonto().intValue());
			detalleRemesas.setCuota(cuotaAsociada);
			detalleRemesas.setRevisarConsolidador(false);
			detalleRemesas.setBloqueado(pagando.isBloqueado());
			detalleRemesas.setComponente(componenteDAO.getComponenteByID(componenteSeleccionado));
			remesasDAO.save(detalleRemesas);
		}
		return registroTabla;
	}

	public OTResumenDependienteServicioVO aprobarMontoRemesaProfesional(OTResumenDependienteServicioVO registroTabla, Integer idProgramaAno, Integer idSubtitulo, Integer componenteSeleccionado) {
		System.out.println("idProgramaAno->"+idProgramaAno);
		System.out.println("idSubtitulo->"+idSubtitulo);
		System.out.println("componenteSeleccionado->"+componenteSeleccionado);
		
		//Buscamos las cuotas del programa y su MP
		List<Cuota> cuotasPrograma = reliquidacionDAO.getCuotasByProgramaAno(idProgramaAno);
		Cuota cuotaAsociada = null;
		for(Cuota cuota : cuotasPrograma){
			if(cuota.getId().equals(registroTabla.getCuotaFinal())){
				cuotaAsociada = cuota;
				break;
			}
		}
		
		if(registroTabla.getIdDetalleRemesaEliminar() != null && registroTabla.getIdDetalleRemesaEliminar().size() > 0){
			for(Integer idDetalleRemesa : registroTabla.getIdDetalleRemesaEliminar()){
				DetalleRemesas remesa = remesasDAO.findDetalleRemesaById(idDetalleRemesa);
				if(remesa != null){
					remesasDAO.remove(remesa);
				}
			}
		}
		
		List<PagaRemesaVO> pagaRemesa = new ArrayList<PagaRemesaVO>();
		for(RemesasProgramaVO registro : registroTabla.getRemesas()){
			for(DiaVO dia : registro.getDias()){
				if(!dia.isBloqueado()){
					PagaRemesaVO paga =  new PagaRemesaVO();
					paga.setDia(dia.getDia());
					paga.setIdMes(registro.getIdMes());
					paga.setMonto(dia.getMonto());
					paga.setBloqueado(false);
					pagaRemesa.add(paga);
				}else{
					PagaRemesaVO paga =  new PagaRemesaVO();
					paga.setDia(dia.getDia());
					paga.setIdMes(registro.getIdMes());
					paga.setMonto(0L);
					paga.setBloqueado(true);
					pagaRemesa.add(paga);
				}
			}
		}
		
		for(PagaRemesaVO pagando : pagaRemesa){
			long currentTime = Calendar.getInstance().getTimeInMillis();
			DetalleRemesas detalleRemesas = new DetalleRemesas();
			System.out.println("registroTabla.getEstablecimiento().getId()-->"+registroTabla.getEstablecimiento().getId());
			detalleRemesas.setEstablecimiento(establecimientosDAO.getEstablecimientoById(registroTabla.getEstablecimiento().getId()));
			detalleRemesas.setProgramaAno(programasDAO.getProgramaAnoByID(idProgramaAno));
			detalleRemesas.setRemesaPagada(false);
			detalleRemesas.setFecha(new Date(currentTime));
			detalleRemesas.setSubtitulo(subtituloDAO.getTipoSubtituloById(idSubtitulo));
			detalleRemesas.setMes(utilitariosDAO.findMesById(pagando.getIdMes()));
			detalleRemesas.setDia(utilitariosDAO.findDiaById(pagando.getDia()));
			detalleRemesas.setMontoRemesa(pagando.getMonto().intValue());
			detalleRemesas.setRevisarConsolidador(false);
			detalleRemesas.setBloqueado(pagando.isBloqueado());
			detalleRemesas.setComponente(componenteDAO.getComponenteByID(componenteSeleccionado));
			detalleRemesas.setCuota(cuotaAsociada);
			remesasDAO.save(detalleRemesas);
			System.out.println("Cambiando el estado de los convenios comuna e insertando relacion con remesa convenio");
			
			ConvenioServicioComponente convenioServicioComponenteActual = conveniosDAO.getConvenioServicioComponenteById(registroTabla.getIdConvenioServicioComponenteSinAprobar());
			convenioServicioComponenteActual.setAprobadoRevision(true);
				
			ConvenioServicio convenioServicio = convenioServicioComponenteActual.getConvenioServicio();
			RemesaConvenios remesaConvenios = new RemesaConvenios();
			remesaConvenios.setConvenioServicio(convenioServicio);
			remesaConvenios.setRemesa(detalleRemesas);
			remesasDAO.save(remesaConvenios);
			if(EstadosConvenios.APROBADO.getId().equals(convenioServicio.getEstadoConvenio().getIdEstadoConvenio())){
				convenioServicio.setEstadoConvenio(new EstadoConvenio(EstadosConvenios.TRAMITE.getId()));
			}
		}
		return registroTabla;
	}
	
	public OTResumenDependienteServicioVO aprobarMontoRemesaConsolidador(OTResumenDependienteServicioVO registroTabla) {
		System.out.println("registroTabla-> " + registroTabla);
		for(Integer idDetalleRemesa : registroTabla.getIdDetalleRemesaAProbarConsolidador()){
			System.out.println("idDetalleRemesa->"+idDetalleRemesa);
			DetalleRemesas detalleRemesa = remesasDAO.findDetalleRemesaById(idDetalleRemesa);
			if(detalleRemesa != null){
				if( registroTabla.getRemesas() != null &&  registroTabla.getRemesas().size() > 0){
					for(RemesasProgramaVO remesaProgramaVO : registroTabla.getRemesas()){
						System.out.println("remesaProgramaVO.getIdMes()->"+remesaProgramaVO.getIdMes());
						if(detalleRemesa.getMes().getIdMes().equals(remesaProgramaVO.getIdMes())){
							if(remesaProgramaVO.getDias() != null &&  remesaProgramaVO.getDias().size() > 0){
								for(DiaVO diaVO : remesaProgramaVO.getDias()){
									System.out.println("diaVO.getDia()->"+diaVO.getDia());
									if(detalleRemesa.getDia().getDia().equals(diaVO.getDia())){
										detalleRemesa.setMontoRemesa(diaVO.getMonto().intValue());
										detalleRemesa.setRevisarConsolidador(true);
									}
								}
							}
						}
					}
				}
			}
		}
		return registroTabla;
	}
	
	public OTResumenMunicipalVO aprobarMontoRemesaConsolidador(OTResumenMunicipalVO registroTabla) {
		System.out.println("registroTabla->"+registroTabla);
		for(Integer idDetalleRemesa : registroTabla.getIdDetalleRemesaAProbarConsolidador()){
			System.out.println("idDetalleRemesa->" + idDetalleRemesa);
			DetalleRemesas detalleRemesa = remesasDAO.findDetalleRemesaById(idDetalleRemesa);
			if(detalleRemesa != null){
				if(registroTabla.getRemesas() != null && registroTabla.getRemesas().size() > 0){
					System.out.println("registroTabla.getRemesas().size()->"+ ( (registroTabla.getRemesas() == null) ? "0" : registroTabla.getRemesas().size() ));
					for(RemesasProgramaVO remesaProgramaVO : registroTabla.getRemesas()){
						System.out.println("remesaProgramaVO.getIdMes()-->"+remesaProgramaVO.getIdMes());
						if(detalleRemesa.getMes().getIdMes().equals(remesaProgramaVO.getIdMes())){
							if(remesaProgramaVO.getDias() != null &&  remesaProgramaVO.getDias().size() > 0){
								for(DiaVO diaVO : remesaProgramaVO.getDias()){
									System.out.println("diaVO.getDia()-->"+diaVO.getDia());
									if(detalleRemesa.getDia().getDia().equals(diaVO.getDia())){
										detalleRemesa.setMontoRemesa(diaVO.getMonto().intValue());
										detalleRemesa.setRevisarConsolidador(true);
									}
								}
							}
						}
					}
				}
			}
		}
		return registroTabla;
	}

	public void pagarOrdenesTransferenciayConvenios() {
		List<DetalleRemesas> remesasPorPagarMes = remesasDAO.getRemesasPorPagarMesActual(Boolean.FALSE, Integer.parseInt(getMesCurso(true)));
		if(remesasPorPagarMes != null){
			for(DetalleRemesas detalleRemesas : remesasPorPagarMes){
				System.out.println("pagando: " + detalleRemesas.getMontoRemesa());
				detalleRemesas.setRemesaPagada(true);
				for(RemesaConvenios convenio : detalleRemesas.getRemesaConvenios()){
					if(convenio.getConvenioComuna() != null){
						convenio.getConvenioComuna().setEstadoConvenio(new EstadoConvenio(EstadosConvenios.PAGADO.getId()));
					}
					if(convenio.getConvenioServicio() != null){
						convenio.getConvenioServicio().setEstadoConvenio(new EstadoConvenio(EstadosConvenios.PAGADO.getId()));
					}
				}
			}
		}
	}

	public int countVersionFinalOTByType(Integer idProcesoOT, TipoDocumentosProcesos tipoDocumento) {
		List<ReferenciaDocumentoSummaryVO> versionesFinales = documentService.getVersionFinalOTByType(idProcesoOT, tipoDocumento);
		if(versionesFinales != null && versionesFinales.size() > 0){
			return versionesFinales.size();
		}
		return 0;
	}

	public List<OTPerCapitaVO> getDetallePerCapitaConsolidador(Integer servicioSeleccionado, Integer anoCurso, Integer idProgramaAno) {
		System.out.println("getDetallePerCapitaConsolidador servicioSeleccionado->"+servicioSeleccionado);
		System.out.println("getDetallePerCapitaConsolidador anoCurso->"+anoCurso);
		System.out.println("getDetallePerCapitaConsolidador idProgramaAno->"+idProgramaAno);
		List<OTPerCapitaVO> listaReportePercapita = new ArrayList<OTPerCapitaVO>();
		ProgramaVO programaVO = programasService.getProgramaAno(idProgramaAno);
		Integer componenteSeleccionado = programaVO.getComponentes().get(0).getId();
		List<ServicioSalud> servicios = null;
		if(servicioSeleccionado == null){
			servicios = servicioSaludDAO.getServiciosOrderId();
		}else{
			ServicioSalud servicioSalud = servicioSaludDAO.getById(servicioSeleccionado);
			servicios = new ArrayList<ServicioSalud>();
			servicios.add(servicioSalud);
		}
		DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(anoCurso);
		for(ServicioSalud servicioSalud : servicios){
			if(servicioSalud.getComunas() != null && servicioSalud.getComunas().size() > 0){
				for(Comuna comuna : servicioSalud.getComunas()){
					OTPerCapitaVO prog = new OTPerCapitaVO();
					prog.setIdComuna(comuna.getId());
					prog.setComuna(comuna.getNombre());
					Map<Integer, RemesasProgramaVO> remesasPorMes = new HashMap<Integer, RemesasProgramaVO>();
					List<DetalleRemesas> remesasPendientes = remesasDAO.getRemesasPendientesConsolidadorProgramaAnoComponenteSubtituloComuna(idProgramaAno, componenteSeleccionado, Subtitulo.SUBTITULO24.getId(), comuna.getId());
					System.out.println("comuna.getId()=" + comuna.getId() + " remesasPendientes.size()=" + ((remesasPendientes == null) ? 0 : remesasPendientes.size()));
					if(remesasPendientes != null && remesasPendientes.size() > 0){
						List<DetalleRemesas> remesasPagadasProgramaAnoComponenteComunaSubtitulo = remesasDAO.getRemesasPagadasByProgramaAnoComponenteComunaSubtitulo(idProgramaAno, componenteSeleccionado, comuna.getId(), Subtitulo.SUBTITULO24.getId());
						List<AntecendentesComunaCalculado> antecendentesComunaCalculados = antecedentesComunaDAO.findAntecendentesComunaCalculadoByComunaServicioDistribucionInicialPercapitaVigente(comuna.getServicioSalud().getId(), comuna.getId(), distribucionInicialPercapita.getIdDistribucionInicialPercapita());
						AntecendentesComunaCalculado antecendentesComunaCalculado = ((antecendentesComunaCalculados != null && antecendentesComunaCalculados.size() > 0) ? antecendentesComunaCalculados.get(0): null);
						if(antecendentesComunaCalculado == null){
							continue;
						}
						
						Long marcoPresupuestario = ((antecendentesComunaCalculado.getPercapitaAno() != null) ? antecendentesComunaCalculado.getPercapitaAno() : 0);	
						prog.setMarcoPresupuestario(marcoPresupuestario);
						prog.setTipoComuna(((antecendentesComunaCalculado.getAntecedentesComuna() != null && antecendentesComunaCalculado.getAntecedentesComuna().getClasificacion() != null) ? antecendentesComunaCalculado.getAntecedentesComuna().getClasificacion().getDescripcion() : ""));
						Long montoTransferido = 0L;
						if(remesasPagadasProgramaAnoComponenteComunaSubtitulo != null && remesasPagadasProgramaAnoComponenteComunaSubtitulo.size() > 0){
							for(DetalleRemesas detalleRemesa : remesasPagadasProgramaAnoComponenteComunaSubtitulo){
								montoTransferido += detalleRemesa.getMontoRemesa();
							}
						}
						prog.setTransferenciaAcumulada(montoTransferido);
						
						List<Integer> idDetalleRemesaAProbarConsolidador = new ArrayList<Integer>();
						for(DetalleRemesas remesaPendienteMes : remesasPendientes){
							idDetalleRemesaAProbarConsolidador.add(remesaPendienteMes.getIdDetalleRemesa());
							if(remesasPorMes.get(remesaPendienteMes.getMes().getIdMes()) == null){
								RemesasProgramaVO detalleRemesa = new RemesasProgramaVO();
								Mes mes = mesDAO.getMesPorID(remesaPendienteMes.getMes().getIdMes());
								detalleRemesa.setIdMes(mes.getIdMes());
								detalleRemesa.setMes(mes.getNombre());
								List<DiaVO> dias = new ArrayList<DiaVO>();
								DiaVO dia = new DiaVO();
								dia.setBloqueado(remesaPendienteMes.isBloqueado());
								dia.setDia(remesaPendienteMes.getDia().getId());
								dia.setIdDia(remesaPendienteMes.getDia().getId());
								dia.setMonto(new Long(remesaPendienteMes.getMontoRemesa()));
								dias.add(dia);
								detalleRemesa.setDias(dias);
								remesasPorMes.put(remesaPendienteMes.getMes().getIdMes(), detalleRemesa);
							}else{
								DiaVO dia = new DiaVO();
								dia.setBloqueado(remesaPendienteMes.isBloqueado());
								dia.setDia(remesaPendienteMes.getDia().getId());
								dia.setIdDia(remesaPendienteMes.getDia().getId());
								dia.setMonto(new Long(remesaPendienteMes.getMontoRemesa()));
								remesasPorMes.get(remesaPendienteMes.getMes().getIdMes()).getDias().add(dia);
							}
						}
						prog.setIdDetalleRemesaAProbarConsolidador(idDetalleRemesaAProbarConsolidador);
						List<RemesasProgramaVO> remesas = new ArrayList<RemesasProgramaVO>();
						for (Integer key : remesasPorMes.keySet()) {
							RemesasProgramaVO remesasProgramaVO = remesasPorMes.get(key);
							remesas.add(remesasProgramaVO);
						}
						prog.setRemesas(remesas);
						listaReportePercapita.add(prog);
					}
				}
			}
		}
		return listaReportePercapita;
	}

	public List<OTPerCapitaVO> getDetalleDesempenoDificilConsolidador(Integer servicioSeleccionado, Integer anoCurso, Integer idProgramaAno) {
		System.out.println("getDetallePerCapitaConsolidador servicioSeleccionado->"+servicioSeleccionado);
		System.out.println("getDetallePerCapitaConsolidador anoCurso->"+anoCurso);
		System.out.println("getDetallePerCapitaConsolidador idProgramaAno->"+idProgramaAno);
		List<OTPerCapitaVO> listaReportePercapita = new ArrayList<OTPerCapitaVO>();
		ProgramaVO programaVO = programasService.getProgramaAno(idProgramaAno);
		Integer componenteSeleccionado = programaVO.getComponentes().get(0).getId();
		List<ServicioSalud> servicios = null;
		if(servicioSeleccionado == null){
			servicios = servicioSaludDAO.getServiciosOrderId();
		}else{
			ServicioSalud servicioSalud = servicioSaludDAO.getById(servicioSeleccionado);
			servicios = new ArrayList<ServicioSalud>();
			servicios.add(servicioSalud);
		}
		DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(anoCurso);
		for(ServicioSalud servicioSalud : servicios){
			if(servicioSalud.getComunas() != null && servicioSalud.getComunas().size() > 0){
				for(Comuna comuna : servicioSalud.getComunas()){
					OTPerCapitaVO prog = new OTPerCapitaVO();
					prog.setIdComuna(comuna.getId());
					prog.setComuna(comuna.getNombre());
					Map<Integer, RemesasProgramaVO> remesasPorMes = new HashMap<Integer, RemesasProgramaVO>();
					List<DetalleRemesas> remesasPendientes = remesasDAO.getRemesasPendientesConsolidadorProgramaAnoComponenteSubtituloComuna(idProgramaAno, componenteSeleccionado, Subtitulo.SUBTITULO24.getId(), comuna.getId());
					System.out.println("comuna.getId()=" + comuna.getId() + " remesasPendientes.size()=" + ((remesasPendientes == null) ? 0 : remesasPendientes.size()));
					if(remesasPendientes != null && remesasPendientes.size() > 0){
						List<DetalleRemesas> remesasPagadasProgramaAnoComponenteComunaSubtitulo = remesasDAO.getRemesasPagadasByProgramaAnoComponenteComunaSubtitulo(idProgramaAno, componenteSeleccionado, comuna.getId(), Subtitulo.SUBTITULO24.getId());
						List<AntecendentesComunaCalculado> antecendentesComunaCalculados = antecedentesComunaDAO.findAntecendentesComunaCalculadoByComunaServicioDistribucionInicialPercapitaVigente(comuna.getServicioSalud().getId(), comuna.getId(), distribucionInicialPercapita.getIdDistribucionInicialPercapita());
						AntecendentesComunaCalculado antecendentesComunaCalculado = ((antecendentesComunaCalculados != null && antecendentesComunaCalculados.size() > 0) ? antecendentesComunaCalculados.get(0): null);
						if(antecendentesComunaCalculado == null){
							continue;
						}
						Long marcoPresupuestario = new Long((antecendentesComunaCalculado.getDesempenoDificil() != null) ? (antecendentesComunaCalculado.getDesempenoDificil() * 12) : 0);
						prog.setMarcoPresupuestario(marcoPresupuestario);
						prog.setTipoComuna(((antecendentesComunaCalculado.getAntecedentesComuna() != null && antecendentesComunaCalculado.getAntecedentesComuna().getClasificacion() != null) ? antecendentesComunaCalculado.getAntecedentesComuna().getClasificacion().getDescripcion() : ""));
						Long montoTransferido = 0L;
						if(remesasPagadasProgramaAnoComponenteComunaSubtitulo != null && remesasPagadasProgramaAnoComponenteComunaSubtitulo.size() > 0){
							for(DetalleRemesas detalleRemesa : remesasPagadasProgramaAnoComponenteComunaSubtitulo){
								montoTransferido += detalleRemesa.getMontoRemesa();
							}
						}
						prog.setTransferenciaAcumulada(montoTransferido);
						
						List<Integer> idDetalleRemesaAProbarConsolidador = new ArrayList<Integer>();
						for(DetalleRemesas remesaPendienteMes : remesasPendientes){
							idDetalleRemesaAProbarConsolidador.add(remesaPendienteMes.getIdDetalleRemesa());
							if(remesasPorMes.get(remesaPendienteMes.getMes().getIdMes()) == null){
								RemesasProgramaVO detalleRemesa = new RemesasProgramaVO();
								Mes mes = mesDAO.getMesPorID(remesaPendienteMes.getMes().getIdMes());
								detalleRemesa.setIdMes(mes.getIdMes());
								detalleRemesa.setMes(mes.getNombre());
								List<DiaVO> dias = new ArrayList<DiaVO>();
								DiaVO dia = new DiaVO();
								dia.setBloqueado(remesaPendienteMes.isBloqueado());
								dia.setDia(remesaPendienteMes.getDia().getId());
								dia.setIdDia(remesaPendienteMes.getDia().getId());
								dia.setMonto(new Long(remesaPendienteMes.getMontoRemesa()));
								dias.add(dia);
								detalleRemesa.setDias(dias);
								remesasPorMes.put(remesaPendienteMes.getMes().getIdMes(), detalleRemesa);
							}else{
								DiaVO dia = new DiaVO();
								dia.setBloqueado(remesaPendienteMes.isBloqueado());
								dia.setDia(remesaPendienteMes.getDia().getId());
								dia.setIdDia(remesaPendienteMes.getDia().getId());
								dia.setMonto(new Long(remesaPendienteMes.getMontoRemesa()));
								remesasPorMes.get(remesaPendienteMes.getMes().getIdMes()).getDias().add(dia);
							}
						}
						prog.setIdDetalleRemesaAProbarConsolidador(idDetalleRemesaAProbarConsolidador);
						List<RemesasProgramaVO> remesas = new ArrayList<RemesasProgramaVO>();
						for (Integer key : remesasPorMes.keySet()) {
							RemesasProgramaVO remesasProgramaVO = remesasPorMes.get(key);
							remesas.add(remesasProgramaVO);
						}
						prog.setRemesas(remesas);
						listaReportePercapita.add(prog);
					}
				}
			}
		}
		return listaReportePercapita;
	}
	
	public List<OTPerCapitaVO> getDetalleRebajaConsolidador(Integer servicioSeleccionado, Integer anoCurso, Integer idProgramaAno) {
		System.out.println("getDetallePerCapitaConsolidador servicioSeleccionado->"+servicioSeleccionado);
		System.out.println("getDetallePerCapitaConsolidador anoCurso->"+anoCurso);
		System.out.println("getDetallePerCapitaConsolidador idProgramaAno->"+idProgramaAno);
		List<OTPerCapitaVO> listaReportePercapita = new ArrayList<OTPerCapitaVO>();
		ProgramaVO programaVO = programasService.getProgramaAno(idProgramaAno);
		Integer componenteSeleccionado = programaVO.getComponentes().get(0).getId();
		List<ServicioSalud> servicios = null;
		if(servicioSeleccionado == null){
			servicios = servicioSaludDAO.getServiciosOrderId();
		}else{
			ServicioSalud servicioSalud = servicioSaludDAO.getById(servicioSeleccionado);
			servicios = new ArrayList<ServicioSalud>();
			servicios.add(servicioSalud);
		}
		DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(anoCurso);
		for(ServicioSalud servicioSalud : servicios){
			if(servicioSalud.getComunas() != null && servicioSalud.getComunas().size() > 0){
				for(Comuna comuna : servicioSalud.getComunas()){
					OTPerCapitaVO prog = new OTPerCapitaVO();
					prog.setIdComuna(comuna.getId());
					prog.setComuna(comuna.getNombre());
					Map<Integer, RemesasProgramaVO> remesasPorMes = new HashMap<Integer, RemesasProgramaVO>();
					List<DetalleRemesas> remesasPendientes = remesasDAO.getRemesasPendientesConsolidadorProgramaAnoComponenteSubtituloComuna(idProgramaAno, componenteSeleccionado, Subtitulo.SUBTITULO24.getId(), comuna.getId());
					System.out.println("comuna.getId()=" + comuna.getId() + " remesasPendientes.size()=" + ((remesasPendientes == null) ? 0 : remesasPendientes.size()));
					if(remesasPendientes != null && remesasPendientes.size() > 0){
						List<DetalleRemesas> remesasPagadasProgramaAnoComponenteComunaSubtitulo = remesasDAO.getRemesasPagadasByProgramaAnoComponenteComunaSubtitulo(idProgramaAno, componenteSeleccionado, comuna.getId(), Subtitulo.SUBTITULO24.getId());
						List<AntecendentesComunaCalculado> antecendentesComunaCalculados = antecedentesComunaDAO.findAntecendentesComunaCalculadoByComunaServicioDistribucionInicialPercapitaVigente(comuna.getServicioSalud().getId(), comuna.getId(), distribucionInicialPercapita.getIdDistribucionInicialPercapita());
						AntecendentesComunaCalculado antecendentesComunaCalculado = ((antecendentesComunaCalculados != null && antecendentesComunaCalculados.size() > 0) ? antecendentesComunaCalculados.get(0): null);
						if(antecendentesComunaCalculado == null){
							continue;
						}
						prog.setMarcoPresupuestario( 0L);
						prog.setTipoComuna(((antecendentesComunaCalculado.getAntecedentesComuna() != null && antecendentesComunaCalculado.getAntecedentesComuna().getClasificacion() != null) ? antecendentesComunaCalculado.getAntecedentesComuna().getClasificacion().getDescripcion() : ""));
						Long montoTransferido = 0L;
						if(remesasPagadasProgramaAnoComponenteComunaSubtitulo != null && remesasPagadasProgramaAnoComponenteComunaSubtitulo.size() > 0){
							for(DetalleRemesas detalleRemesa : remesasPagadasProgramaAnoComponenteComunaSubtitulo){
								montoTransferido += detalleRemesa.getMontoRemesa();
							}
						}
						prog.setTransferenciaAcumulada(montoTransferido);
						
						List<Integer> idDetalleRemesaAProbarConsolidador = new ArrayList<Integer>();
						for(DetalleRemesas remesaPendienteMes : remesasPendientes){
							idDetalleRemesaAProbarConsolidador.add(remesaPendienteMes.getIdDetalleRemesa());
							if(remesasPorMes.get(remesaPendienteMes.getMes().getIdMes()) == null){
								RemesasProgramaVO detalleRemesa = new RemesasProgramaVO();
								Mes mes = mesDAO.getMesPorID(remesaPendienteMes.getMes().getIdMes());
								detalleRemesa.setIdMes(mes.getIdMes());
								detalleRemesa.setMes(mes.getNombre());
								List<DiaVO> dias = new ArrayList<DiaVO>();
								DiaVO dia = new DiaVO();
								dia.setBloqueado(remesaPendienteMes.isBloqueado());
								dia.setDia(remesaPendienteMes.getDia().getId());
								dia.setIdDia(remesaPendienteMes.getDia().getId());
								dia.setMonto(new Long(remesaPendienteMes.getMontoRemesa()));
								dias.add(dia);
								detalleRemesa.setDias(dias);
								remesasPorMes.put(remesaPendienteMes.getMes().getIdMes(), detalleRemesa);
							}else{
								DiaVO dia = new DiaVO();
								dia.setBloqueado(remesaPendienteMes.isBloqueado());
								dia.setDia(remesaPendienteMes.getDia().getId());
								dia.setIdDia(remesaPendienteMes.getDia().getId());
								dia.setMonto(new Long(remesaPendienteMes.getMontoRemesa()));
								remesasPorMes.get(remesaPendienteMes.getMes().getIdMes()).getDias().add(dia);
							}
						}
						prog.setIdDetalleRemesaAProbarConsolidador(idDetalleRemesaAProbarConsolidador);
						List<RemesasProgramaVO> remesas = new ArrayList<RemesasProgramaVO>();
						for (Integer key : remesasPorMes.keySet()) {
							RemesasProgramaVO remesasProgramaVO = remesasPorMes.get(key);
							remesas.add(remesasProgramaVO);
						}
						prog.setRemesas(remesas);
						listaReportePercapita.add(prog);
					}
				}
			}
		}
		return listaReportePercapita;
	}

	public OTPerCapitaVO aprobarMontoRemesaPerCapita(OTPerCapitaVO registroTabla) {
		System.out.println("aprobarMontoRemesaPerCapita registroTabla-> " + registroTabla);
		for(Integer idDetalleRemesa : registroTabla.getIdDetalleRemesaAProbarConsolidador()){
			System.out.println("idDetalleRemesa->"+idDetalleRemesa);
			DetalleRemesas detalleRemesa = remesasDAO.findDetalleRemesaById(idDetalleRemesa);
			if(detalleRemesa != null){
				if( registroTabla.getRemesas() != null &&  registroTabla.getRemesas().size() > 0){
					for(RemesasProgramaVO remesaProgramaVO : registroTabla.getRemesas()){
						System.out.println("remesaProgramaVO.getIdMes()->"+remesaProgramaVO.getIdMes());
						if(detalleRemesa.getMes().getIdMes().equals(remesaProgramaVO.getIdMes())){
							if(remesaProgramaVO.getDias() != null &&  remesaProgramaVO.getDias().size() > 0){
								for(DiaVO diaVO : remesaProgramaVO.getDias()){
									System.out.println("diaVO.getDia()->"+diaVO.getDia());
									if(detalleRemesa.getDia().getDia().equals(diaVO.getDia())){
										detalleRemesa.setMontoRemesa(diaVO.getMonto().intValue());
										detalleRemesa.setRevisarConsolidador(true);
									}
								}
							}
						}
					}
				}
			}
		}
		return registroTabla;
	}

	public OTPerCapitaVO aprobarMontoRemesaDesempenoDificil(OTPerCapitaVO registroTabla) {
		System.out.println("aprobarMontoRemesaPerCapita registroTabla-> " + registroTabla);
		for(Integer idDetalleRemesa : registroTabla.getIdDetalleRemesaAProbarConsolidador()){
			System.out.println("idDetalleRemesa->"+idDetalleRemesa);
			DetalleRemesas detalleRemesa = remesasDAO.findDetalleRemesaById(idDetalleRemesa);
			if(detalleRemesa != null){
				if( registroTabla.getRemesas() != null &&  registroTabla.getRemesas().size() > 0){
					for(RemesasProgramaVO remesaProgramaVO : registroTabla.getRemesas()){
						System.out.println("remesaProgramaVO.getIdMes()->"+remesaProgramaVO.getIdMes());
						if(detalleRemesa.getMes().getIdMes().equals(remesaProgramaVO.getIdMes())){
							if(remesaProgramaVO.getDias() != null &&  remesaProgramaVO.getDias().size() > 0){
								for(DiaVO diaVO : remesaProgramaVO.getDias()){
									System.out.println("diaVO.getDia()->"+diaVO.getDia());
									if(detalleRemesa.getDia().getDia().equals(diaVO.getDia())){
										detalleRemesa.setMontoRemesa(diaVO.getMonto().intValue());
										detalleRemesa.setRevisarConsolidador(true);
									}
								}
							}
						}
					}
				}
			}
		}
		return registroTabla;
	}
	
	public OTPerCapitaVO aprobarMontoRemesaRebaja(OTPerCapitaVO registroTabla) {
		System.out.println("aprobarMontoRemesaPerCapita registroTabla-> " + registroTabla);
		for(Integer idDetalleRemesa : registroTabla.getIdDetalleRemesaAProbarConsolidador()){
			System.out.println("idDetalleRemesa->"+idDetalleRemesa);
			DetalleRemesas detalleRemesa = remesasDAO.findDetalleRemesaById(idDetalleRemesa);
			if(detalleRemesa != null){
				if( registroTabla.getRemesas() != null &&  registroTabla.getRemesas().size() > 0){
					for(RemesasProgramaVO remesaProgramaVO : registroTabla.getRemesas()){
						System.out.println("remesaProgramaVO.getIdMes()->"+remesaProgramaVO.getIdMes());
						if(detalleRemesa.getMes().getIdMes().equals(remesaProgramaVO.getIdMes())){
							if(remesaProgramaVO.getDias() != null &&  remesaProgramaVO.getDias().size() > 0){
								for(DiaVO diaVO : remesaProgramaVO.getDias()){
									System.out.println("diaVO.getDia()->"+diaVO.getDia());
									if(detalleRemesa.getDia().getDia().equals(diaVO.getDia())){
										detalleRemesa.setMontoRemesa(diaVO.getMonto().intValue());
										detalleRemesa.setRevisarConsolidador(true);
									}
								}
							}
						}
					}
				}
			}
		}
		return registroTabla;
	}

}