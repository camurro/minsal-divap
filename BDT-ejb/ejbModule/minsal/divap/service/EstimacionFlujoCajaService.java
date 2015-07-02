package minsal.divap.service;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.xml.bind.JAXBException;

import minsal.divap.dao.AnoDAO;
import minsal.divap.dao.AntecedentesComunaDAO;
import minsal.divap.dao.CajaDAO;
import minsal.divap.dao.ComponenteDAO;
import minsal.divap.dao.ComunaDAO;
import minsal.divap.dao.ConveniosDAO;
import minsal.divap.dao.DistribucionInicialPercapitaDAO;
import minsal.divap.dao.DocumentDAO;
import minsal.divap.dao.EstablecimientosDAO;
import minsal.divap.dao.EstimacionFlujoCajaDAO;
import minsal.divap.dao.InstitucionDAO;
import minsal.divap.dao.MesDAO;
import minsal.divap.dao.ProgramasDAO;
import minsal.divap.dao.RemesasDAO;
import minsal.divap.dao.SeguimientoDAO;
import minsal.divap.dao.ServicioSaludDAO;
import minsal.divap.dao.TipoSubtituloDAO;
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.doc.GeneradorDocumento;
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
import minsal.divap.excel.impl.EstimacionFlujoCajaConsolidadorSheetExcel;
import minsal.divap.excel.impl.EstimacionFlujoCajaMonitoreoConsolidadorSheetExcel;
import minsal.divap.excel.impl.EstimacionFlujoCajaSheetExcel;
import minsal.divap.excel.impl.EstimacionFlujoCajaSubtituloSheetExcel;
import minsal.divap.excel.interfaces.ExcelTemplate;
import minsal.divap.model.mappers.ReferenciaDocumentoMapper;
import minsal.divap.model.mappers.ServicioMapper;
import minsal.divap.util.Util;
import minsal.divap.vo.AdjuntosVO;
import minsal.divap.vo.BaseVO;
import minsal.divap.vo.BodyVO;
import minsal.divap.vo.CajaMontoSummaryVO;
import minsal.divap.vo.CellExcelVO;
import minsal.divap.vo.ConvenioComunaComponenteVO;
import minsal.divap.vo.ConvenioServicioComponenteVO;
import minsal.divap.vo.ConveniosSummaryVO;
import minsal.divap.vo.DocumentoVO;
import minsal.divap.vo.EmailVO;
import minsal.divap.vo.ProgramaFonasaVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.ReporteEmailsEnviadosVO;
import minsal.divap.vo.ResumenConsolidadorVO;
import minsal.divap.vo.ResumenFONASAMunicipalVO;
import minsal.divap.vo.ResumenFONASAServicioVO;
import minsal.divap.vo.SeguimientoVO;
import minsal.divap.vo.ServiciosVO;
import minsal.divap.vo.SubtituloFlujoCajaVO;
import minsal.divap.vo.TransferenciaSummaryVO;
import minsal.divap.xml.GeneradorXML;
import minsal.divap.xml.email.Email;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.jboss.logging.Logger;

import cl.minsal.divap.model.AnoEnCurso;
import cl.minsal.divap.model.AntecedentesComunaCalculadoRebaja;
import cl.minsal.divap.model.AntecendentesComunaCalculado;
import cl.minsal.divap.model.Caja;
import cl.minsal.divap.model.CajaMonto;
import cl.minsal.divap.model.CajaMontoPK;
import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.ComponenteSubtitulo;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.Cuota;
import cl.minsal.divap.model.DetalleRemesas;
import cl.minsal.divap.model.DistribucionInicialPercapita;
import cl.minsal.divap.model.DocumentoEstimacionFlujoCajaConsolidador;
import cl.minsal.divap.model.DocumentoEstimacionflujocaja;
import cl.minsal.divap.model.Establecimiento;
import cl.minsal.divap.model.EstadoPrograma;
import cl.minsal.divap.model.FlujoCajaConsolidador;
import cl.minsal.divap.model.Institucion;
import cl.minsal.divap.model.Mes;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.ProgramaComponente;
import cl.minsal.divap.model.ProgramaSubtituloComponentePeso;
import cl.minsal.divap.model.ReferenciaDocumento;
import cl.minsal.divap.model.ReporteEmailsAdjuntos;
import cl.minsal.divap.model.ReporteEmailsDestinatarios;
import cl.minsal.divap.model.ReporteEmailsEnviados;
import cl.minsal.divap.model.ReporteEmailsFlujoCajaConsolidador;
import cl.minsal.divap.model.Seguimiento;
import cl.minsal.divap.model.ServicioSalud;
import cl.minsal.divap.model.TipoDestinatario;
import cl.minsal.divap.model.TipoDocumento;
import cl.minsal.divap.model.TipoSubtitulo;
import cl.minsal.divap.model.Usuario;

@Stateless
@LocalBean
public class EstimacionFlujoCajaService {
	@EJB
	private EstimacionFlujoCajaDAO estimacionFlujoCajaDAO;
	@EJB
	private DocumentDAO fileDAO;
	@EJB
	private ComponenteDAO componenteDAO;
	@EJB
	private TipoSubtituloDAO subtituloDAO;
	@EJB
	private EstablecimientosDAO establecimientosDAO;
	@EJB
	private ComunaDAO comunaDAO;
	@EJB
	private InstitucionDAO institucionDAO;
	@EJB
	private AlfrescoService alfrescoService;
	@EJB
	private DocumentService documentService;
	@EJB
	private ServicioSaludService servicioSaludService;
	@EJB
	private ProgramasService programasService;
	@EJB
	private ProcessService processService; 

	@Resource(name = "tmpDir")
	private String tmpDir;

	@Resource(name = "tmpDirDoc")
	private String tmpDirDoc;

	@Resource(name = "folderTemplateEstimacionFlujoCaja")
	private String folderTemplateEstimacionFlujoCaja;

	@Resource(name = "folderEstimacionFlujoCaja")
	private String folderEstimacionFlujoCaja;

	@EJB
	private UsuarioDAO usuarioDAO;
	@EJB
	private SeguimientoService seguimientoService;
	@EJB
	private ConveniosService conveniosService;
	@EJB
	private SeguimientoDAO seguimientoDAO;
	@EJB
	private DocumentDAO documentDAO;
	@EJB
	private AntecedentesComunaDAO antecedentesComunaDAO;
	@EJB
	private DistribucionInicialPercapitaDAO distribucionInicialPercapitaDAO;
	@EJB
	private CajaDAO cajaDAO;
	@EJB
	private MesDAO mesDAO;
	@EJB
	private AnoDAO anoDAO;
	@EJB
	private ProgramasDAO programasDAO;
	@EJB
	private EmailService emailService;
	@EJB
	private ConveniosDAO conveniosDAO;
	@EJB
	private RemesasDAO remesasDAO;
	@EJB
	private ServicioSaludDAO servicioSaludDAO;
	private static final Logger LOGGER = Logger.getLogger(EstimacionFlujoCajaService.class);

	// Generar documento
	public Integer elaborarOrdinarioProgramacionCaja(Integer ano) {
		Integer plantillaBorradorOrdinarioProgramacionCaja = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLAOFICIOPROGRAMACIONCAJA);
		if (plantillaBorradorOrdinarioProgramacionCaja == null) {
			throw new RuntimeException("No se puede crear Borrador Decreto Aporte Estatal, la plantilla no esta cargada");
		}
		try {
			ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryBorradorAporteEstatalVO = documentService.getDocumentByPlantillaId(plantillaBorradorOrdinarioProgramacionCaja);
			DocumentoVO documentoBorradorOrdinarioProgramacionCajaVO = documentService.getDocument(referenciaDocumentoSummaryBorradorAporteEstatalVO.getId());
			String templateOrdinarioProgramacionCaja = tmpDirDoc + File.separator + documentoBorradorOrdinarioProgramacionCajaVO.getName();
			templateOrdinarioProgramacionCaja = templateOrdinarioProgramacionCaja.replace(" ", "");
			String filenameBorradorOrdinarioProgramacionCaja = tmpDirDoc + File.separator + new Date().getTime() + "_" + "OrdinarioProgramacionCaja.docx";
			filenameBorradorOrdinarioProgramacionCaja = filenameBorradorOrdinarioProgramacionCaja.replaceAll(" ", "");
			System.out.println("filenameBorradorAporteEstatal filename-->"	+ filenameBorradorOrdinarioProgramacionCaja);
			System.out.println("templateBorradorAporteEstatal template-->"  + templateOrdinarioProgramacionCaja);
			GeneradorWord generadorWordPlantillaBorradorOrdinarioProgramacionCaja = new GeneradorWord(templateOrdinarioProgramacionCaja);
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contentType = mimemap.getContentType(templateOrdinarioProgramacionCaja.toLowerCase());
			System.out.println("contenTypeBorradorAporteEstatal->" + contentType);

			generadorWordPlantillaBorradorOrdinarioProgramacionCaja.saveContent(documentoBorradorOrdinarioProgramacionCajaVO.getContent(), XWPFDocument.class);

			Map<String, Object> parametersBorradorAporteEstatal = new HashMap<String, Object>();
			parametersBorradorAporteEstatal.put("{ano}",Util.obtenerAno(new Date()));
			parametersBorradorAporteEstatal.put("{mes}",Util.obtenerNombreMes(Util.obtenerMes(new Date())));
			GeneradorResolucionAporteEstatal generadorWordDecretoBorradorAporteEstatal = new GeneradorResolucionAporteEstatal(filenameBorradorOrdinarioProgramacionCaja, templateOrdinarioProgramacionCaja);
			generadorWordDecretoBorradorAporteEstatal.replaceValues(parametersBorradorAporteEstatal, XWPFDocument.class);
			BodyVO response = alfrescoService.uploadDocument(new File(filenameBorradorOrdinarioProgramacionCaja), contentType, folderEstimacionFlujoCaja.replace("{ANO}", ano.toString()));
			System.out.println("response responseBorradorAporteEstatal --->" + response);
			TipoDocumento tipoDocumento = new TipoDocumento(TipoDocumentosProcesos.PLANTILLAOFICIOPROGRAMACIONCAJA.getId());
			plantillaBorradorOrdinarioProgramacionCaja = documentService.createDocumentOrdinarioProgramacióndeCaja(tipoDocumento, response.getNodeRef(), response.getFileName(), contentType);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (Docx4JException e) {
			e.printStackTrace();
		}
		return plantillaBorradorOrdinarioProgramacionCaja;
	}

	// Para hacer el calculo de la propuesta, se debe hacer una copia de los
	// valores del año pasado.
	public void calcularPropuesta(Integer idPrograma, Integer ano, Boolean iniciarFlujoCaja) {
		if(iniciarFlujoCaja){
			long millisecons = System.currentTimeMillis();
			ProgramaAno programaAno = programasDAO.getProgramaAnoByIdProgramaAndAno(idPrograma, ano);
			programaAno.setEstadoFlujoCaja(new EstadoPrograma(EstadosProgramas.CALCULARPROPUESTA.getId()));
			Map<Integer, List<Integer>> componentesPorSubtitulo = new HashMap<Integer, List<Integer>>();
			if(programaAno.getProgramaComponentes() != null &&  programaAno.getProgramaComponentes().size() > 0){
				for(ProgramaComponente programaComponente : programaAno.getProgramaComponentes()){
					for(ComponenteSubtitulo componenteSubtitulo : programaComponente.getComponente().getComponenteSubtitulosComponente()){
						if(componentesPorSubtitulo.get(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo()) == null){
							List<Integer> componentes = new ArrayList<Integer>();
							componentes.add(programaComponente.getComponente().getId());
							componentesPorSubtitulo.put(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo(), componentes);
						}else{
							componentesPorSubtitulo.get(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo()).add(programaComponente.getComponente().getId());
						}
					}
				}
				
			}
			
			
			List<Caja> cajasPogramaAno = cajaDAO.getCajasByProgramaAno(programaAno.getIdProgramaAno());
			if(cajasPogramaAno != null && cajasPogramaAno.size() > 0){
				List<Integer> idCajas = new ArrayList<Integer>();
				for(Caja caja : cajasPogramaAno){
					idCajas.add(caja.getId());
					cajaDAO.eliminarCajaMontosById(caja.getId());
				}
				if(idCajas.size() > 0){
					cajaDAO.eliminarCajasById(idCajas);
				}
			}
			ProgramaAno programaAnoBase = programasDAO.getProgramaAnoByIDProgramaAno(idPrograma, (ano-1));
			List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
			List<Cuota> cuotasPrograma = estimacionFlujoCajaDAO.getCuotasByProgramaAno(programaAno.getIdProgramaAno());
			List<Cuota> cuotasProgramaBase = estimacionFlujoCajaDAO.getCuotasByProgramaAno(programaAnoBase.getIdProgramaAno());
			for(ServicioSalud servicioSalud : servicios){
				for (Map.Entry<Integer, List<Integer>> entry : componentesPorSubtitulo.entrySet()) { 
					if(Subtitulo.SUBTITULO24.getId().equals(entry.getKey())){
						if(servicioSalud.getComunas() != null && servicioSalud.getComunas().size() > 0){
							List<Integer> componentes = entry.getValue();
							Long totalPorServicio = 0L;
							Map<Integer, Long> totalesPorComponente = new HashMap<Integer, Long>();
							for(Integer componente : componentes){
								Long totalServicioPorComponente = 0L;
								for(Comuna comuna : servicioSalud.getComunas()){
									Long marcoPresupuestarioComunaBase = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), programaAnoBase.getIdProgramaAno(), componente, entry.getKey());
									Long marcoPresupuestarioComunaEvaluacion = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), programaAno.getIdProgramaAno(), componente, entry.getKey());
									totalPorServicio += marcoPresupuestarioComunaEvaluacion;
									totalServicioPorComponente += marcoPresupuestarioComunaEvaluacion;
									if(marcoPresupuestarioComunaBase != 0L){
										Caja cajaBase = cajaDAO.getByServicioComunaProgramaAnoComponenteSubtitulo(servicioSalud.getId(), comuna.getId(), programaAnoBase.getIdProgramaAno(), componente, Subtitulo.getById(entry.getKey()));
										if(cajaBase != null){
											System.out.println("Se distribuye de la misma forma que el año Base");
											Caja caja = new Caja();
											caja.setCajaInicial(true);
											Componente componenteDTO = componenteDAO.getComponenteByID(componente);
											caja.setIdComponente(componenteDTO);
											caja.setIdSubtitulo(new TipoSubtitulo(entry.getKey()));
											caja.setPrograma(programaAno);
											caja.setServicio(servicioSalud);
											caja.setComuna(comuna);
											cajaDAO.save(caja);
											List<CajaMonto> cajaMontosBase = cajaBase.getCajaMontos();
											int mesCajaMonto = 1;
											int montoCalculado = 0;
											for(CajaMonto cajaMontoBase : cajaMontosBase){
												boolean mesConMonto = false;
												CajaMonto cajaMonto = new CajaMonto();
												CajaMontoPK cajaMontoPK = new CajaMontoPK(caja.getId(), cajaMontoBase.getMes().getIdMes());
												cajaMonto.setCajaMontoPK(cajaMontoPK);
												cajaMonto.setMes(cajaMontoBase.getMes());
												cajaMonto.setCaja(caja);
												if(cajaMontoBase.getMonto() == 0){
													cajaMonto.setMonto(0);
												}else{
													int porcentaje = (int)((cajaMontoBase.getMonto() * 100.0) / marcoPresupuestarioComunaBase);
													int monto = (int)(marcoPresupuestarioComunaEvaluacion * (porcentaje/100.0));
													montoCalculado += monto;
													cajaMonto.setMonto(monto);
													mesConMonto = true;
												}
												cajaDAO.save(cajaMonto);
												if(mesConMonto){
													mesCajaMonto = cajaMonto.getMes().getIdMes();
												}
											}
											if(marcoPresupuestarioComunaEvaluacion != montoCalculado){
												Long diferenciaMonto = (marcoPresupuestarioComunaEvaluacion - montoCalculado);
												CajaMonto cajaMonto = cajaDAO.getCajaMontoByCajaMes(caja.getId(), mesCajaMonto);
												if(cajaMonto != null){
													cajaMonto.setMonto(cajaMonto.getMonto() + diferenciaMonto.intValue());
												}
											}
										}
									}else{
										if(cuotasPrograma != null && cuotasPrograma.size() > 0){
											Long marcoAcumulado = 0L; 
											Caja caja = new Caja();
											caja.setCajaInicial(true);
											Componente componenteDTO = componenteDAO.getComponenteByID(componente);
											caja.setIdComponente(componenteDTO);
											caja.setIdSubtitulo(new TipoSubtitulo(entry.getKey()));
											caja.setPrograma(programaAno);
											caja.setServicio(servicioSalud);
											caja.setComuna(comuna);
											cajaDAO.save(caja);
											List<Integer> mesesConCuotas = new ArrayList<Integer>();
											for(int contCuotas = 0; contCuotas <  cuotasPrograma.size(); contCuotas++){
												CajaMonto cajaMonto = new CajaMonto();
												if(contCuotas == 0){
													CajaMontoPK cajaMontoPK = new CajaMontoPK(caja.getId(), 3);
													cajaMonto.setCajaMontoPK(cajaMontoPK);
													cajaMonto.setMes(new Mes(3));
													cajaMonto.setCaja(caja);
													int monto = (int)(marcoPresupuestarioComunaEvaluacion * (cuotasPrograma.get(contCuotas).getPorcentaje()/100.0));
													marcoAcumulado += monto;
													cajaMonto.setMonto(monto);
													mesesConCuotas.add(3);
												}else if(contCuotas == (cuotasPrograma.size() - 1)){
													mesesConCuotas.add(cuotasPrograma.get(contCuotas).getIdMes().getIdMes());
													CajaMontoPK cajaMontoPK = new CajaMontoPK(caja.getId(), cuotasPrograma.get(contCuotas).getIdMes().getIdMes());
													cajaMonto.setCajaMontoPK(cajaMontoPK);
													cajaMonto.setMes(cuotasPrograma.get(contCuotas).getIdMes());
													cajaMonto.setCaja(caja);
													int monto = (int)(marcoPresupuestarioComunaEvaluacion - marcoAcumulado);
													cajaMonto.setMonto(monto);
												}else{
													mesesConCuotas.add(cuotasPrograma.get(contCuotas).getIdMes().getIdMes());
													CajaMontoPK cajaMontoPK = new CajaMontoPK(caja.getId(), cuotasPrograma.get(contCuotas).getIdMes().getIdMes());
													cajaMonto.setCajaMontoPK(cajaMontoPK);
													cajaMonto.setMes(cuotasPrograma.get(contCuotas).getIdMes());
													cajaMonto.setCaja(caja);
													int monto = (int)(marcoPresupuestarioComunaEvaluacion * (cuotasPrograma.get(contCuotas).getPorcentaje()/100.0));
													marcoAcumulado += monto;
													cajaMonto.setMonto(monto);
												}
												cajaDAO.save(cajaMonto);
											}
											for(Integer mes = 1; mes <= 12; mes++){
												if(mesesConCuotas.contains(mes)){
													continue;
												}
												CajaMonto cajaMonto = new CajaMonto();
												CajaMontoPK cajaMontoPK = new CajaMontoPK(caja.getId(), mes);
												cajaMonto.setCajaMontoPK(cajaMontoPK);
												cajaMonto.setMes(new Mes(mes));
												cajaMonto.setCaja(caja);
												cajaMonto.setMonto(0);
												cajaDAO.save(cajaMonto);
											}
										}else{
											System.out.println("Advertencia no hay cuotas para el programa y se calculara la propuesta con las cuotas del año anterior");
											if(cuotasProgramaBase != null && cuotasProgramaBase.size() > 0){
												Long marcoAcumulado = 0L; 
												Caja caja = new Caja();
												caja.setCajaInicial(true);
												Componente componenteDTO = componenteDAO.getComponenteByID(componente);
												caja.setIdComponente(componenteDTO);
												caja.setIdSubtitulo(new TipoSubtitulo(entry.getKey()));
												caja.setPrograma(programaAno);
												caja.setServicio(servicioSalud);
												caja.setComuna(comuna);
												cajaDAO.save(caja);
												List<Integer> mesesConCuotas = new ArrayList<Integer>();
												for(int contCuotas = 0; contCuotas <  cuotasProgramaBase.size(); contCuotas++){
													CajaMonto cajaMonto = new CajaMonto();
													if(contCuotas == 0){
														CajaMontoPK cajaMontoPK = new CajaMontoPK(caja.getId(), 3);
														cajaMonto.setCajaMontoPK(cajaMontoPK);
														cajaMonto.setMes(new Mes(3));
														cajaMonto.setCaja(caja);
														int monto = (int)(marcoPresupuestarioComunaEvaluacion * (cuotasProgramaBase.get(contCuotas).getPorcentaje()/100.0));
														marcoAcumulado += monto;
														cajaMonto.setMonto(monto);
														mesesConCuotas.add(3);
													}else if(contCuotas == (cuotasProgramaBase.size() - 1)){
														mesesConCuotas.add(cuotasProgramaBase.get(contCuotas).getIdMes().getIdMes());
														CajaMontoPK cajaMontoPK = new CajaMontoPK(caja.getId(), cuotasProgramaBase.get(contCuotas).getIdMes().getIdMes());
														cajaMonto.setCajaMontoPK(cajaMontoPK);
														cajaMonto.setMes(cuotasProgramaBase.get(contCuotas).getIdMes());
														cajaMonto.setCaja(caja);
														int monto = (int)(marcoPresupuestarioComunaEvaluacion - marcoAcumulado);
														cajaMonto.setMonto(monto);
													}else{
														mesesConCuotas.add(cuotasProgramaBase.get(contCuotas).getIdMes().getIdMes());
														CajaMontoPK cajaMontoPK = new CajaMontoPK(caja.getId(), cuotasProgramaBase.get(contCuotas).getIdMes().getIdMes());
														cajaMonto.setCajaMontoPK(cajaMontoPK);
														cajaMonto.setMes(cuotasProgramaBase.get(contCuotas).getIdMes());
														cajaMonto.setCaja(caja);
														int monto = (int)(marcoPresupuestarioComunaEvaluacion * (cuotasProgramaBase.get(contCuotas).getPorcentaje()/100.0));
														marcoAcumulado += monto;
														cajaMonto.setMonto(monto);
													}
													cajaDAO.save(cajaMonto);
												}
												for(Integer mes = 1; mes <= 12; mes++){
													if(mesesConCuotas.contains(mes)){
														continue;
													}
													CajaMonto cajaMonto = new CajaMonto();
													CajaMontoPK cajaMontoPK = new CajaMontoPK(caja.getId(), mes);
													cajaMonto.setCajaMontoPK(cajaMontoPK);
													cajaMonto.setMes(new Mes(mes));
													cajaMonto.setCaja(caja);
													cajaMonto.setMonto(0);
													cajaDAO.save(cajaMonto);
												}
											}
										}
									}
								}
								totalesPorComponente.put(componente, totalServicioPorComponente);
							}
							int sizeComponentes = componentes.size();
							int pesoAcumulado = 0;
							for(int contadorComponentes = 0; contadorComponentes < sizeComponentes; contadorComponentes++){
								Long totalServicioPorComponente = totalesPorComponente.get(componentes.get(contadorComponentes));
								int pesoCalculado = 0;
								if(totalServicioPorComponente != 0L){
									if(contadorComponentes == (sizeComponentes - 1)){
										pesoCalculado = (new Double(100.0 - pesoAcumulado)).intValue();
									}else{
										pesoCalculado = (new Double((totalServicioPorComponente * 100.0) / totalServicioPorComponente)).intValue();
										pesoAcumulado += pesoCalculado;
									}
								}
								ProgramaSubtituloComponentePeso programaSubtituloComponentePeso = programasDAO.getProgramaSubtituloComponentePesoByProgramaComponenteSubtitulo(programaAno.getIdProgramaAno(), componentes.get(contadorComponentes), Subtitulo.SUBTITULO24.getId());
								if(programaSubtituloComponentePeso == null){
									programaSubtituloComponentePeso = new ProgramaSubtituloComponentePeso();
									Componente componenteDTO = componenteDAO.getComponenteByID(componentes.get(contadorComponentes));
									TipoSubtitulo tipoSubtitulo = new TipoSubtitulo(Subtitulo.SUBTITULO24.getId());
									programaSubtituloComponentePeso.setComponente(componenteDTO);
									programaSubtituloComponentePeso.setPrograma(programaAno);
									programaSubtituloComponentePeso.setSubtitulo(tipoSubtitulo);
									programaSubtituloComponentePeso.setPeso(pesoCalculado);
									programasDAO.save(programaSubtituloComponentePeso);
								}else{
									programaSubtituloComponentePeso.setPeso(pesoCalculado);
								}
							}
						}
					}else{
						if(servicioSalud.getEstablecimientos() != null && servicioSalud.getEstablecimientos().size() > 0){
							List<Integer> componentes = entry.getValue();
							Long totalPorServicio = 0L;
							Map<Integer, Long> totalesPorComponente = new HashMap<Integer, Long>();
							for(Integer componente : componentes){
								Long totalServicioPorComponente = 0L;
								for(Establecimiento establecimiento : servicioSalud.getEstablecimientos()){
									Long marcoPresupuestarioEstablecimientoBase = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), programaAnoBase.getIdProgramaAno(), componente, entry.getKey());
									Long marcoPresupuestarioEstablecimientoEvaluacion = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), programaAno.getIdProgramaAno(), componente, entry.getKey());
									totalPorServicio += marcoPresupuestarioEstablecimientoEvaluacion;
									totalServicioPorComponente += marcoPresupuestarioEstablecimientoEvaluacion;
									if(marcoPresupuestarioEstablecimientoBase != 0L){
										Caja cajaBase = cajaDAO.getByServicioEstablecimientoProgramaAnoComponenteSubtitulo(servicioSalud.getId(), establecimiento.getId(), programaAnoBase.getIdProgramaAno(), componente, Subtitulo.getById(entry.getKey()));
										if(cajaBase != null){
											System.out.println("Se distribuye de la misma forma que el año Base");
											Caja caja = new Caja();
											caja.setCajaInicial(true);
											Componente componenteDTO = componenteDAO.getComponenteByID(componente);
											caja.setIdComponente(componenteDTO);
											caja.setIdSubtitulo(new TipoSubtitulo(entry.getKey()));
											caja.setPrograma(programaAno);
											caja.setServicio(servicioSalud);
											caja.setEstablecimiento(establecimiento);
											cajaDAO.save(caja);
											List<CajaMonto> cajaMontosBase = cajaBase.getCajaMontos();
											int mesCajaMonto = 1;
											int montoCalculado = 0;
											for(CajaMonto cajaMontoBase : cajaMontosBase){
												boolean mesConMonto = false;
												CajaMonto cajaMonto = new CajaMonto();
												CajaMontoPK cajaMontoPK = new CajaMontoPK(caja.getId(), cajaMontoBase.getMes().getIdMes());
												cajaMonto.setCajaMontoPK(cajaMontoPK);
												cajaMonto.setMes(cajaMontoBase.getMes());
												cajaMonto.setCaja(caja);
												if(cajaMontoBase.getMonto() == 0){
													cajaMonto.setMonto(0);
												}else{
													int porcentaje = (int)((cajaMontoBase.getMonto() * 100.0) / marcoPresupuestarioEstablecimientoBase);
													int monto = (int)(marcoPresupuestarioEstablecimientoEvaluacion * (porcentaje/100.0));
													montoCalculado += monto;
													cajaMonto.setMonto(monto);
													mesConMonto = true;
												}
												cajaDAO.save(cajaMonto);
												if(mesConMonto){
													mesCajaMonto = cajaMonto.getMes().getIdMes();
												}
											}
											if(marcoPresupuestarioEstablecimientoEvaluacion != montoCalculado){
												Long diferenciaMonto = (marcoPresupuestarioEstablecimientoEvaluacion - montoCalculado);
												CajaMonto cajaMonto = cajaDAO.getCajaMontoByCajaMes(caja.getId(), mesCajaMonto);
												if(cajaMonto != null){
													cajaMonto.setMonto(cajaMonto.getMonto() + diferenciaMonto.intValue());
												}
											}
										}
									}else{
										if(cuotasPrograma != null && cuotasPrograma.size() > 0){
											Long marcoAcumulado = 0L; 
											Caja caja = new Caja();
											caja.setCajaInicial(true);
											Componente componenteDTO = componenteDAO.getComponenteByID(componente);
											caja.setIdComponente(componenteDTO);
											caja.setIdSubtitulo(new TipoSubtitulo(entry.getKey()));
											caja.setPrograma(programaAno);
											caja.setServicio(servicioSalud);
											caja.setEstablecimiento(establecimiento);
											cajaDAO.save(caja);
											List<Integer> mesesConCuotas = new ArrayList<Integer>();
											for(int contCuotas = 0; contCuotas <  cuotasPrograma.size(); contCuotas++){
												CajaMonto cajaMonto = new CajaMonto();
												if(contCuotas == 0){
													CajaMontoPK cajaMontoPK = new CajaMontoPK(caja.getId(), 3);
													cajaMonto.setCajaMontoPK(cajaMontoPK);
													cajaMonto.setMes(new Mes(3));
													cajaMonto.setCaja(caja);
													int monto = (int)(marcoPresupuestarioEstablecimientoEvaluacion * (cuotasPrograma.get(contCuotas).getPorcentaje()/100.0));
													marcoAcumulado += monto;
													cajaMonto.setMonto(monto);
													mesesConCuotas.add(3);
												}else if(contCuotas == (cuotasPrograma.size() - 1)){
													mesesConCuotas.add(cuotasPrograma.get(contCuotas).getIdMes().getIdMes());
													CajaMontoPK cajaMontoPK = new CajaMontoPK(caja.getId(), cuotasPrograma.get(contCuotas).getIdMes().getIdMes());
													cajaMonto.setCajaMontoPK(cajaMontoPK);
													cajaMonto.setMes(cuotasPrograma.get(contCuotas).getIdMes());
													cajaMonto.setCaja(caja);
													int monto = (int)(marcoPresupuestarioEstablecimientoEvaluacion - marcoAcumulado);
													cajaMonto.setMonto(monto);
												}else{
													mesesConCuotas.add(cuotasPrograma.get(contCuotas).getIdMes().getIdMes());
													CajaMontoPK cajaMontoPK = new CajaMontoPK(caja.getId(), cuotasPrograma.get(contCuotas).getIdMes().getIdMes());
													cajaMonto.setCajaMontoPK(cajaMontoPK);
													cajaMonto.setMes(cuotasPrograma.get(contCuotas).getIdMes());
													cajaMonto.setCaja(caja);
													int monto = (int)(marcoPresupuestarioEstablecimientoEvaluacion * (cuotasPrograma.get(contCuotas).getPorcentaje()/100.0));
													marcoAcumulado += monto;
													cajaMonto.setMonto(monto);
												}
												cajaDAO.save(cajaMonto);
											}
											for(Integer mes = 1; mes <= 12; mes++){
												if(mesesConCuotas.contains(mes)){
													continue;
												}
												CajaMonto cajaMonto = new CajaMonto();
												CajaMontoPK cajaMontoPK = new CajaMontoPK(caja.getId(), mes);
												cajaMonto.setCajaMontoPK(cajaMontoPK);
												cajaMonto.setMes(new Mes(mes));
												cajaMonto.setCaja(caja);
												cajaMonto.setMonto(0);
												cajaDAO.save(cajaMonto);
											}
										}else{
											System.out.println("Advertencia no hay cuotas para el programa y se calculara la propuesta con las cuotas del año anterior");
											if(cuotasProgramaBase != null && cuotasProgramaBase.size() > 0){
												Long marcoAcumulado = 0L; 
												Caja caja = new Caja();
												caja.setCajaInicial(true);
												Componente componenteDTO = componenteDAO.getComponenteByID(componente);
												caja.setIdComponente(componenteDTO);
												caja.setIdSubtitulo(new TipoSubtitulo(entry.getKey()));
												caja.setPrograma(programaAno);
												caja.setServicio(servicioSalud);
												caja.setEstablecimiento(establecimiento);
												cajaDAO.save(caja);
												List<Integer> mesesConCuotas = new ArrayList<Integer>();
												for(int contCuotas = 0; contCuotas <  cuotasProgramaBase.size(); contCuotas++){
													CajaMonto cajaMonto = new CajaMonto();
													if(contCuotas == 0){
														CajaMontoPK cajaMontoPK = new CajaMontoPK(caja.getId(), 3);
														cajaMonto.setCajaMontoPK(cajaMontoPK);
														cajaMonto.setMes(new Mes(3));
														cajaMonto.setCaja(caja);
														int monto = (int)(marcoPresupuestarioEstablecimientoEvaluacion * (cuotasProgramaBase.get(contCuotas).getPorcentaje()/100.0));
														marcoAcumulado += monto;
														cajaMonto.setMonto(monto);
														mesesConCuotas.add(3);
													}else if(contCuotas == (cuotasProgramaBase.size() - 1)){
														mesesConCuotas.add(cuotasProgramaBase.get(contCuotas).getIdMes().getIdMes());
														CajaMontoPK cajaMontoPK = new CajaMontoPK(caja.getId(), cuotasProgramaBase.get(contCuotas).getIdMes().getIdMes());
														cajaMonto.setCajaMontoPK(cajaMontoPK);
														cajaMonto.setMes(cuotasProgramaBase.get(contCuotas).getIdMes());
														cajaMonto.setCaja(caja);
														int monto = (int)(marcoPresupuestarioEstablecimientoEvaluacion - marcoAcumulado);
														cajaMonto.setMonto(monto);
													}else{
														mesesConCuotas.add(cuotasProgramaBase.get(contCuotas).getIdMes().getIdMes());
														CajaMontoPK cajaMontoPK = new CajaMontoPK(caja.getId(), cuotasProgramaBase.get(contCuotas).getIdMes().getIdMes());
														cajaMonto.setCajaMontoPK(cajaMontoPK);
														cajaMonto.setMes(cuotasProgramaBase.get(contCuotas).getIdMes());
														cajaMonto.setCaja(caja);
														int monto = (int)(marcoPresupuestarioEstablecimientoEvaluacion * (cuotasProgramaBase.get(contCuotas).getPorcentaje()/100.0));
														marcoAcumulado += monto;
														cajaMonto.setMonto(monto);
													}
													cajaDAO.save(cajaMonto);
												}
												for(Integer mes = 1; mes <= 12; mes++){
													if(mesesConCuotas.contains(mes)){
														continue;
													}
													CajaMonto cajaMonto = new CajaMonto();
													CajaMontoPK cajaMontoPK = new CajaMontoPK(caja.getId(), mes);
													cajaMonto.setCajaMontoPK(cajaMontoPK);
													cajaMonto.setMes(new Mes(mes));
													cajaMonto.setCaja(caja);
													cajaMonto.setMonto(0);
													cajaDAO.save(cajaMonto);
												}
											}
										}
									}
								}
								totalesPorComponente.put(componente, totalServicioPorComponente);
							}
							int sizeComponentes = componentes.size();
							int pesoAcumulado = 0;
							for(int contadorComponentes = 0; contadorComponentes < sizeComponentes; contadorComponentes++){
								Long totalServicioPorComponente = totalesPorComponente.get(componentes.get(contadorComponentes));
								int pesoCalculado = 0;
								if(totalServicioPorComponente != 0L){
									if(contadorComponentes == (sizeComponentes - 1)){
										pesoCalculado = (new Double(100.0 - pesoAcumulado)).intValue();
									}else{
										pesoCalculado = (new Double((totalServicioPorComponente * 100.0) / totalServicioPorComponente)).intValue();
										pesoAcumulado += pesoCalculado;
									}
								}
								ProgramaSubtituloComponentePeso programaSubtituloComponentePeso = programasDAO.getProgramaSubtituloComponentePesoByProgramaComponenteSubtitulo(programaAno.getIdProgramaAno(), componentes.get(contadorComponentes), Subtitulo.SUBTITULO24.getId());
								if(programaSubtituloComponentePeso == null){
									programaSubtituloComponentePeso = new ProgramaSubtituloComponentePeso();
									Componente componenteDTO = componenteDAO.getComponenteByID(componentes.get(contadorComponentes));
									TipoSubtitulo tipoSubtitulo = new TipoSubtitulo(Subtitulo.SUBTITULO24.getId());
									programaSubtituloComponentePeso.setComponente(componenteDTO);
									programaSubtituloComponentePeso.setPrograma(programaAno);
									programaSubtituloComponentePeso.setSubtitulo(tipoSubtitulo);
									programaSubtituloComponentePeso.setPeso(pesoCalculado);
									programasDAO.save(programaSubtituloComponentePeso);
								}else{
									programaSubtituloComponentePeso.setPeso(pesoCalculado);
								}
							}
						}
					}
				}
			}
			System.out.println("Fin subtitulo 21/22/24/29");
			System.out.println("Tardo " + ((System.currentTimeMillis()-millisecons)/1000) + " segundos");
			programaAno.setEstadoFlujoCaja(new EstadoPrograma(EstadosProgramas.CALCULARPROPUESTA.getId()));
		}else{
			//MODIFICACION NO HAY CALCULO DE PROPUESTA
		}
	}

	public Integer getIdPlantillaPropuesta() {
		Integer plantillaId = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLAPROPUESTA);
		if (plantillaId == null) {
			List<BaseVO> servicios = servicioSaludService.getAllServicios();
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String filename = tmpDir + File.separator
					+ "plantillaPercapita.xlsx";
			String contenType = mimemap.getContentType(filename.toLowerCase());
			GeneradorExcel generadorExcel = new GeneradorExcel(filename);
			List<String> headers = new ArrayList<String>();
			headers.add("REGION");
			headers.add("SERVICIO");
			headers.add("COMUNA");
			headers.add("POBLACION");
			headers.add("POBLACION MAYOR DE 65 AÑOS");
			EstimacionFlujoCajaSheetExcel estimacionFlujoCajaSheetExcel = new EstimacionFlujoCajaSheetExcel(headers, servicios);
			generadorExcel.addSheet(estimacionFlujoCajaSheetExcel, "Hoja 1");
			try {
				BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderEstimacionFlujoCaja);
				System.out.println("response AsignacionRecursosPercapitaSheetExcel --->"+ response);
				plantillaId = documentService.createTemplate(TipoDocumentosProcesos.PLANTILLAPROPUESTA, response.getNodeRef(), response.getFileName(), contenType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			plantillaId = documentService.getDocumentoIdByPlantillaId(plantillaId);
		}
		return plantillaId;
	}

	public Integer getAnoCurso() {
		DateFormat formatNowYear = new SimpleDateFormat("yyyy");
		Date nowDate = new Date();
		return Integer.valueOf(formatNowYear.format(nowDate));
	}

	public String getMesCurso(Boolean numero) {
		SimpleDateFormat dateFormat = null;
		String mesCurso = null;
		if(numero){
			dateFormat = new SimpleDateFormat("MM");
			mesCurso = dateFormat.format(new Date());
			mesCurso = "4";
		}else{
			dateFormat = new SimpleDateFormat("MMMM");
			mesCurso = dateFormat.format(new Date());
		}
		return mesCurso;
	}

	public Integer createSeguimiento(Integer idLineaProgramatica, TareasSeguimiento tareaSeguimiento, String subject, String body,
			String username, List<String> para, List<String> conCopia, List<String> conCopiaOculta, List<Integer> documentos, Integer ano) {
		String from = usuarioDAO.getEmailByUsername(username);
		if (from == null) {
			throw new RuntimeException("Usuario no tiene un email valido");
		}
		List<ReferenciaDocumentoSummaryVO> documentosTmp = new ArrayList<ReferenciaDocumentoSummaryVO>();
		if (documentos != null && documentos.size() > 0) {
			for (Integer referenciaDocumentoId : documentos) {
				MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
				ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO = documentService.getDocumentSummary(referenciaDocumentoId);
				documentosTmp.add(referenciaDocumentoSummaryVO);
				String contenType = mimemap.getContentType(referenciaDocumentoSummaryVO.getPath().toLowerCase());
				BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummaryVO.getPath()), contenType, folderEstimacionFlujoCaja.replace("{ANO}", ano.toString()));
				System.out.println("response upload template --->" + response);
				documentService.updateDocumentTemplate(referenciaDocumentoId, response.getNodeRef(), response.getFileName(), contenType);
			}
		}

		Integer idSeguimiento = seguimientoService.createSeguimiento(tareaSeguimiento, subject, body, from, para, conCopia, conCopiaOculta, documentosTmp);
		Seguimiento seguimiento = seguimientoDAO.getSeguimientoById(idSeguimiento);
		estimacionFlujoCajaDAO.createSeguimiento(idLineaProgramatica,seguimiento);
		return 1;
	}

	public List<SeguimientoVO> getBitacora(TareasSeguimiento tareaSeguimiento) {
		return seguimientoService.getBitacoraEstimacionFlujoCaja(tareaSeguimiento);
	}


	public Integer generarPlanillaPropuestaConsolidador(Integer idProceso, Integer ano) {
		System.out.println("generarPlanillaPropuestaConsolidador idProceso=" + idProceso + " ano="+ano);
		Integer planillaTrabajoId = null;
		//obtengo todos los programas del usuario

		//obtengo lista de servicios
		Integer mes = Integer.parseInt(getMesCurso(true));
		List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();

		List<ResumenConsolidadorVO> resumenConsolidadorSubtitulo21 = new ArrayList<ResumenConsolidadorVO>();
		List<ResumenConsolidadorVO> resumenConsolidadorSubtitulo22 = new ArrayList<ResumenConsolidadorVO>();
		List<ResumenConsolidadorVO> resumenConsolidadorSubtitulo24 = new ArrayList<ResumenConsolidadorVO>();
		List<ResumenConsolidadorVO> resumenConsolidadorSubtitulo29 = new ArrayList<ResumenConsolidadorVO>();

		List<ProgramaAno> programasSubtitulo21 = programasDAO.getProgramasBySubtitulo(ano, Subtitulo.SUBTITULO21);
		List<ProgramaAno> programasSubtitulo22 = programasDAO.getProgramasBySubtitulo(ano, Subtitulo.SUBTITULO22);
		List<ProgramaAno> programasSubtitulo24 = programasDAO.getProgramasBySubtitulo(ano, Subtitulo.SUBTITULO24);
		List<ProgramaAno> programasSubtitulo29 = programasDAO.getProgramasBySubtitulo(ano, Subtitulo.SUBTITULO29);

		List<CellExcelVO> header = new ArrayList<CellExcelVO>();
		List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
		header.add(new CellExcelVO("COD SS", 1, 2));
		header.add(new CellExcelVO("SERVICIOS DE SALUD", 1, 2));

		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator + "planillaPropuestaEstimacionFlujoCajaConsolidador" + getMesCurso(false) + ".xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());

		GeneradorExcel generadorExcel = new GeneradorExcel(filename);

		if(servicios != null && servicios.size() > 0){
			for(ServicioSalud servicioSalud : servicios){
				if(programasSubtitulo21 != null && programasSubtitulo21.size() > 0){
					ResumenConsolidadorVO resumenConsolidadorVO = new ResumenConsolidadorVO();
					resumenConsolidadorVO.setCodigoServicio(servicioSalud.getId().toString());
					resumenConsolidadorVO.setServicio(servicioSalud.getNombre());

					List<Long> montos = new ArrayList<Long>();
					header.add(new CellExcelVO("SUBTÍTULO 21", programasSubtitulo21.size() + 1, 1));			
					for(ProgramaAno programaAno : programasSubtitulo21) {
						subHeader.add(new CellExcelVO(programaAno.getPrograma().getNombre(), 1, 1));
					}
					subHeader.add(new CellExcelVO("TOTAL REF. SERVICIOS SUBT. 21", 1, 1));
					for(ProgramaAno programaAno : programasSubtitulo21) {
						List<Caja> cajas = cajaDAO.getByProgramaAnoServicioSubtitulo(programaAno.getIdProgramaAno(), servicioSalud.getId(), Subtitulo.SUBTITULO21);
						if(cajas != null && cajas.size() > 0){
							for(Caja caja : cajas) {
								if(caja.getCajaMontos() != null && caja.getCajaMontos().size() > 0){
									Long totalMes = 0L;
									for(CajaMonto cajaMonto : caja.getCajaMontos()){
										if(mes.equals(cajaMonto.getMes().getIdMes())){
											totalMes+=cajaMonto.getMonto();
										}
									}
									montos.add(totalMes);
								}
							}
						}else{
							montos.add(0L);
						}
					}
					resumenConsolidadorVO.setMontos(montos);
					resumenConsolidadorSubtitulo21.add(resumenConsolidadorVO);
				}
				System.out.println("Luego de programas para subtitulo 21 servicio-->"+servicioSalud.getNombre());
				if(programasSubtitulo22 != null && programasSubtitulo22.size() > 0){
					ResumenConsolidadorVO resumenConsolidadorVO = new ResumenConsolidadorVO();
					resumenConsolidadorVO.setCodigoServicio(servicioSalud.getId().toString());
					resumenConsolidadorVO.setServicio(servicioSalud.getNombre());

					List<Long> montos = new ArrayList<Long>();
					header.add(new CellExcelVO("SUBTÍTULO 22", programasSubtitulo22.size() + 1, 1));			
					for(ProgramaAno programaAno : programasSubtitulo22) {
						subHeader.add(new CellExcelVO(programaAno.getPrograma().getNombre(), 1, 1));
					}
					subHeader.add(new CellExcelVO("TOTAL REF. SERVICIOS SUBT. 22", 1, 1));
					for(ProgramaAno programaAno : programasSubtitulo22) {
						List<Caja> cajas = cajaDAO.getByProgramaAnoServicioSubtitulo(programaAno.getIdProgramaAno(), servicioSalud.getId(), Subtitulo.SUBTITULO22);
						if(cajas != null && cajas.size() > 0){
							for(Caja caja : cajas) {
								if(caja.getCajaMontos() != null && caja.getCajaMontos().size() > 0){
									Long totalMes = 0L;
									for(CajaMonto cajaMonto : caja.getCajaMontos()){
										if(mes.equals(cajaMonto.getMes().getIdMes())){
											totalMes+=cajaMonto.getMonto();
										}
									}
									montos.add(totalMes);
								}
							}
						}else{
							montos.add(0L);
						}
					}
					resumenConsolidadorVO.setMontos(montos);
					resumenConsolidadorSubtitulo22.add(resumenConsolidadorVO);
				}
				System.out.println("Luego de programas para subtitulo 22 servicio-->"+servicioSalud.getNombre());
				if(programasSubtitulo24 != null && programasSubtitulo24.size() > 0){
					ResumenConsolidadorVO resumenConsolidadorVO = new ResumenConsolidadorVO();
					resumenConsolidadorVO.setCodigoServicio(servicioSalud.getId().toString());
					resumenConsolidadorVO.setServicio(servicioSalud.getNombre());

					List<Long> montos = new ArrayList<Long>();
					header.add(new CellExcelVO("SUBTÍTULO 24", programasSubtitulo24.size() + 1, 1));			
					for(ProgramaAno programaAno : programasSubtitulo24) {
						subHeader.add(new CellExcelVO(programaAno.getPrograma().getNombre(), 1, 1));
					}
					subHeader.add(new CellExcelVO("TOTAL REF. SERVICIOS SUBT. 24", 1, 1));
					for(ProgramaAno programaAno : programasSubtitulo24) {
						List<Caja> cajas = cajaDAO.getByProgramaAnoServicioSubtitulo(programaAno.getIdProgramaAno(), servicioSalud.getId(), Subtitulo.SUBTITULO24);
						if(cajas != null && cajas.size() > 0){
							for(Caja caja : cajas) {
								if(caja.getCajaMontos() != null && caja.getCajaMontos().size() > 0){
									Long totalMes = 0L;
									for(CajaMonto cajaMonto : caja.getCajaMontos()){
										if(mes.equals(cajaMonto.getMes().getIdMes())){
											totalMes+=cajaMonto.getMonto();
										}
									}
									montos.add(totalMes);
								}
							}
						}else{
							montos.add(0L);
						}
					}
					resumenConsolidadorVO.setMontos(montos);
					resumenConsolidadorSubtitulo24.add(resumenConsolidadorVO);
				}
				System.out.println("Luego de programas para subtitulo 24 servicio-->"+servicioSalud.getNombre());
				if(programasSubtitulo29 != null && programasSubtitulo29.size() > 0){
					ResumenConsolidadorVO resumenConsolidadorVO = new ResumenConsolidadorVO();
					resumenConsolidadorVO.setCodigoServicio(servicioSalud.getId().toString());
					resumenConsolidadorVO.setServicio(servicioSalud.getNombre());

					List<Long> montos = new ArrayList<Long>();
					header.add(new CellExcelVO("SUBTÍTULO 29", programasSubtitulo29.size() + 1, 1));			
					for(ProgramaAno programaAno : programasSubtitulo29) {
						subHeader.add(new CellExcelVO(programaAno.getPrograma().getNombre(), 1, 1));
					}
					subHeader.add(new CellExcelVO("TOTAL REF. SERVICIOS SUBT. 29", 1, 1));
					for(ProgramaAno programaAno : programasSubtitulo29) {
						List<Caja> cajas = cajaDAO.getByProgramaAnoServicioSubtitulo(programaAno.getIdProgramaAno(), servicioSalud.getId(), Subtitulo.SUBTITULO29);
						if(cajas != null && cajas.size() > 0){
							for(Caja caja : cajas) {
								if(caja.getCajaMontos() != null && caja.getCajaMontos().size() > 0){
									Long totalMes = 0L;
									for(CajaMonto cajaMonto : caja.getCajaMontos()){
										if(mes.equals(cajaMonto.getMes().getIdMes())){
											totalMes+=cajaMonto.getMonto();
										}
									}
									montos.add(totalMes);
								}
							}
						}else{
							montos.add(0L);
						}
					}
					resumenConsolidadorVO.setMontos(montos);
					resumenConsolidadorSubtitulo29.add(resumenConsolidadorVO);
				}
				System.out.println("Luego de programas para subtitulo 29 servicio-->"+servicioSalud.getNombre());
			}
			if(resumenConsolidadorSubtitulo21 != null && resumenConsolidadorSubtitulo21.size() > 0){
				EstimacionFlujoCajaConsolidadorSheetExcel estimacionFlujoCajaConsolidadorSheetExcel = new EstimacionFlujoCajaConsolidadorSheetExcel(header, subHeader, null);
				estimacionFlujoCajaConsolidadorSheetExcel.setItems(resumenConsolidadorSubtitulo21);
				generadorExcel.addSheet(estimacionFlujoCajaConsolidadorSheetExcel, "Subtítulo 21 " + getMesCurso(false));
				System.out.println("Hoja subtitulo 21 ok");
			}
			if(resumenConsolidadorSubtitulo22 != null && resumenConsolidadorSubtitulo22.size() > 0){
				EstimacionFlujoCajaConsolidadorSheetExcel estimacionFlujoCajaConsolidadorSheetExcel = new EstimacionFlujoCajaConsolidadorSheetExcel(header, subHeader, null);
				estimacionFlujoCajaConsolidadorSheetExcel.setItems(resumenConsolidadorSubtitulo22);
				generadorExcel.addSheet(estimacionFlujoCajaConsolidadorSheetExcel, "Subtítulo 22 " + getMesCurso(false));
				System.out.println("Hoja subtitulo 22 ok");
			}
			if(resumenConsolidadorSubtitulo24 != null && resumenConsolidadorSubtitulo24.size() > 0){
				EstimacionFlujoCajaConsolidadorSheetExcel estimacionFlujoCajaConsolidadorSheetExcel = new EstimacionFlujoCajaConsolidadorSheetExcel(header, subHeader, null);
				estimacionFlujoCajaConsolidadorSheetExcel.setItems(resumenConsolidadorSubtitulo24);
				generadorExcel.addSheet(estimacionFlujoCajaConsolidadorSheetExcel, "Subtítulo 24 " + getMesCurso(false));
				System.out.println("Hoja subtitulo 24 ok");
			}
			if(resumenConsolidadorSubtitulo29 != null && resumenConsolidadorSubtitulo29.size() > 0){
				EstimacionFlujoCajaConsolidadorSheetExcel estimacionFlujoCajaConsolidadorSheetExcel = new EstimacionFlujoCajaConsolidadorSheetExcel(header, subHeader, null);
				estimacionFlujoCajaConsolidadorSheetExcel.setItems(resumenConsolidadorSubtitulo29);
				generadorExcel.addSheet(estimacionFlujoCajaConsolidadorSheetExcel, "Subtítulo 29 " + getMesCurso(false));
				System.out.println("Hoja subtitulo 29 ok");
			}
		}

		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderEstimacionFlujoCaja.replace("{ANO}", ano.toString()));
			System.out.println("response planillaPropuestaEstimacionFlujoCajaConsolidador --->"	+ response);
			TipoDocumento tipoDocumento = new TipoDocumento(TipoDocumentosProcesos.PLANTILLAPROPUESTA.getId());
			FlujoCajaConsolidador flujoCajaConsolidador = estimacionFlujoCajaDAO.findFlujoCajaConsolidadorById(idProceso);
			planillaTrabajoId = documentService.createDocumentMonitoreoConsolidador(flujoCajaConsolidador, tipoDocumento, response.getNodeRef(), response.getFileName(), contenType, ano, Integer.parseInt(getMesCurso(true)));

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("planillaTrabajoId --> "+planillaTrabajoId);
		return planillaTrabajoId;
	}

	public void generarPlanillaPropuesta(Integer idPrograma, Integer ano) {
		long milliseconds = System.currentTimeMillis();
		System.out.println("Inicio generarPlanillaPropuesta idPrograma="+idPrograma);
		System.out.println("Inicio generarPlanillaPropuesta ano="+ano);
		ProgramaAno programaAno = programasDAO.getProgramaAnoByIdProgramaAndAno(idPrograma, ano);
		List<CajaMonto> cajaMontosReparos = cajaDAO.getCajaMontoReparosByProgramaAno(programaAno.getIdProgramaAno());
		if(cajaMontosReparos != null && cajaMontosReparos.size() > 0){
			for(CajaMonto cajaMonto : cajaMontosReparos){
				System.out.println("Quitando el reparo a cajaMonto");
				cajaMonto.setReparos(false);
			}
		}

		Map<Integer, List<Integer>> componentesPorSubtitulo = new HashMap<Integer, List<Integer>>();
		if(programaAno.getProgramaComponentes() != null &&  programaAno.getProgramaComponentes().size() > 0){
			for(ProgramaComponente programaComponente : programaAno.getProgramaComponentes()){
				for(ComponenteSubtitulo componenteSubtitulo : programaComponente.getComponente().getComponenteSubtitulosComponente()){
					if(componentesPorSubtitulo.get(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo()) == null){
						List<Integer> componentes = new ArrayList<Integer>();
						componentes.add(programaComponente.getComponente().getId());
						componentesPorSubtitulo.put(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo(), componentes);
					}else{
						componentesPorSubtitulo.get(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo()).add(programaComponente.getComponente().getId());
					}
				}
			}
		}
		List<SubtituloFlujoCajaVO> flujoCajaSub21 = null;
		if(componentesPorSubtitulo.get(Subtitulo.SUBTITULO21.getId()) != null){
			System.out.println("buscando sub21");
			flujoCajaSub21 = getMonitoreoServicioByProgramaAnoEstablecimientoComponenteSubtitulo(programaAno.getIdProgramaAno(), null, null, componentesPorSubtitulo.get(Subtitulo.SUBTITULO21.getId()), Subtitulo.SUBTITULO21, true);
		}
		List<SubtituloFlujoCajaVO> flujoCajaSub22 = null;
		if(componentesPorSubtitulo.get(Subtitulo.SUBTITULO22.getId()) != null){
			System.out.println("buscando sub22");
			flujoCajaSub22 = getMonitoreoServicioByProgramaAnoEstablecimientoComponenteSubtitulo(programaAno.getIdProgramaAno(), null, null, componentesPorSubtitulo.get(Subtitulo.SUBTITULO22.getId()), Subtitulo.SUBTITULO22, true);
		}
		List<SubtituloFlujoCajaVO> flujoCajaSub24 = null;
		if(componentesPorSubtitulo.get(Subtitulo.SUBTITULO24.getId()) != null){
			System.out.println("buscando sub24");
			flujoCajaSub24 = getMonitoreoComunaByProgramaAnoComponenteSubtitulo(programaAno.getIdProgramaAno(), null, null, componentesPorSubtitulo.get(Subtitulo.SUBTITULO24.getId()), Subtitulo.SUBTITULO24, true);
		}
		List<SubtituloFlujoCajaVO> flujoCajaSub29 = null;
		if(componentesPorSubtitulo.get(Subtitulo.SUBTITULO29.getId()) != null){
			System.out.println("buscando sub29");
			flujoCajaSub29 = getMonitoreoServicioByProgramaAnoEstablecimientoComponenteSubtitulo(programaAno.getIdProgramaAno(), null, null, componentesPorSubtitulo.get(Subtitulo.SUBTITULO29.getId()), Subtitulo.SUBTITULO29, true);
		}

		DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(ano);
		List<AntecendentesComunaCalculado> antecedentesComunasCalculado = antecedentesComunaDAO.findAntecedentesComunaCalculadosByDistribucionInicialPercapitaVigente(distribucionInicialPercapita.getIdDistribucionInicialPercapita());
		System.out.println("Comenzando con percapita");
		List<SubtituloFlujoCajaVO> flujoCajaPercapita = new ArrayList<SubtituloFlujoCajaVO>();
		List<SubtituloFlujoCajaVO> desempenosDificil = new ArrayList<SubtituloFlujoCajaVO>();
		if(antecedentesComunasCalculado != null && antecedentesComunasCalculado.size() > 0){
			for(AntecendentesComunaCalculado antecedentesComunaCalculado : antecedentesComunasCalculado){
				flujoCajaPercapita.add(getPercapitaFlujoCajaVO(antecedentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getNombre(), antecedentesComunaCalculado.getPercapitaMes()));
				desempenosDificil.add(getDesempenoDificialFlujoCajaVO(antecedentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getNombre(), antecedentesComunaCalculado.getDesempenoDificil()));
			}
		}
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator + "planillaPropuestaEstimacionFlujoCaja" + getMesCurso(false) + ".xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());
		GeneradorExcel generadorExcel = new GeneradorExcel(filename);
		List<CellExcelVO> header = new ArrayList<CellExcelVO>();

		String anoCurso =  "AÑO " + programaAno.getAno().getAno();
		header.add(new CellExcelVO("SERVICIOS DE SALUD", 1, 2));
		header.add(new CellExcelVO(programaAno.getPrograma().getNombre() , 12, 1));

		List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
		Integer mesCurso = Integer.parseInt(getMesCurso(true));
		List<Mes> meses = mesDAO.getAll();
		for(Mes mes : meses){
			if(mes.getIdMes() < mesCurso){
				subHeader.add(new CellExcelVO(mes.getNombre() + " REAL", 1, 1));
			}else{
				subHeader.add(new CellExcelVO(mes.getNombre(), 1, 1));
			}
		}
		System.out.println("flujoCajaPercapita--->"+flujoCajaPercapita);
		boolean removeLast = false;
		System.out.println("Inicio pestaña percapita");
		if(flujoCajaPercapita != null && !flujoCajaPercapita.isEmpty()){
			header.set(1, new CellExcelVO("REMESA PER CÁPITA" , 12, 1));
			EstimacionFlujoCajaSubtituloSheetExcel estimacionFlujoCajaSubtituloSheetExcel = new EstimacionFlujoCajaSubtituloSheetExcel(header, subHeader, null);
			removeLast = true;
			header.add(new CellExcelVO("TOTAL PER CÁPITA " + anoCurso, 1, 2));
			SubtituloFlujoCajaVO totalSubtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
			totalSubtituloFlujoCajaVO.setServicio("Total");
			Long totalEnero=0L, totalFebrero=0L, totalMarzo=0L, totalAbril=0L, totalMayo=0L, totalJunio=0L, totalJulio=0L;
			Long totalAgosto=0L, totalSeptiembre=0L, totalOctubre=0L, totalNoviembre=0L, totalDiciembre=0L, totalSubtituloPercapita=0L;
			for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : flujoCajaPercapita){ 
				totalEnero+=subtituloFlujoCajaVO.getCajaMontoEnero().getMontoMes();
				totalFebrero+=subtituloFlujoCajaVO.getCajaMontoFebrero().getMontoMes();
				totalMarzo+=subtituloFlujoCajaVO.getCajaMontoMarzo().getMontoMes();
				totalAbril+=subtituloFlujoCajaVO.getCajaMontoAbril().getMontoMes();
				totalMayo+=subtituloFlujoCajaVO.getCajaMontoMayo().getMontoMes();
				totalJunio+=subtituloFlujoCajaVO.getCajaMontoJunio().getMontoMes();
				totalJulio+=subtituloFlujoCajaVO.getCajaMontoJulio().getMontoMes();
				totalAgosto+=subtituloFlujoCajaVO.getCajaMontoAgosto().getMontoMes();
				totalSeptiembre+=subtituloFlujoCajaVO.getCajaMontoSeptiembre().getMontoMes();
				totalOctubre+=subtituloFlujoCajaVO.getCajaMontoOctubre().getMontoMes();
				totalNoviembre+=subtituloFlujoCajaVO.getCajaMontoNoviembre().getMontoMes();
				totalDiciembre+=subtituloFlujoCajaVO.getCajaMontoDiciembre().getMontoMes();
				totalSubtituloPercapita+=subtituloFlujoCajaVO.getTotalMontos();
			}
			totalSubtituloFlujoCajaVO.getCajaMontoEnero().setMontoMes(totalEnero);
			totalSubtituloFlujoCajaVO.getCajaMontoFebrero().setMontoMes(totalFebrero);
			totalSubtituloFlujoCajaVO.getCajaMontoMarzo().setMontoMes(totalMarzo);
			totalSubtituloFlujoCajaVO.getCajaMontoAbril().setMontoMes(totalAbril);
			totalSubtituloFlujoCajaVO.getCajaMontoMayo().setMontoMes(totalMayo);
			totalSubtituloFlujoCajaVO.getCajaMontoJunio().setMontoMes(totalJunio);
			totalSubtituloFlujoCajaVO.getCajaMontoJulio().setMontoMes(totalJulio);
			totalSubtituloFlujoCajaVO.getCajaMontoAgosto().setMontoMes(totalAgosto);
			totalSubtituloFlujoCajaVO.getCajaMontoSeptiembre().setMontoMes(totalSeptiembre);
			totalSubtituloFlujoCajaVO.getCajaMontoOctubre().setMontoMes(totalOctubre);
			totalSubtituloFlujoCajaVO.getCajaMontoNoviembre().setMontoMes(totalNoviembre);
			totalSubtituloFlujoCajaVO.getCajaMontoDiciembre().setMontoMes(totalDiciembre);
			totalSubtituloFlujoCajaVO.setTotalMontos(totalSubtituloPercapita);
			flujoCajaPercapita.add(totalSubtituloFlujoCajaVO);
			estimacionFlujoCajaSubtituloSheetExcel.setItems(flujoCajaPercapita);
			generadorExcel.addSheet(estimacionFlujoCajaSubtituloSheetExcel, "Per Cápita");

			if(desempenosDificil != null && desempenosDificil.size() >0){
				header.set(1, new CellExcelVO("ASIGNACIÓN DE DESEMPEÑO DIFICIL" , 12, 1));
				if(removeLast){
					header.remove(header.size()-1);
					removeLast = false;
				}
				removeLast = true;
				header.add(new CellExcelVO("TOTAL ASIGNACIÓN DE DESEMPEÑO DIFICIL SUBT. 24 " + anoCurso, 1, 2));
				SubtituloFlujoCajaVO totalSubtituloDesempenoFlujoCajaVO = new SubtituloFlujoCajaVO();
				totalSubtituloDesempenoFlujoCajaVO.setServicio("Total");
				Long totalEneroDesempeno=0L, totalFebreroDesempeno=0L, totalMarzoDesempeno=0L, totalAbrilDesempeno=0L, totalMayoDesempeno=0L, totalJunioDesempeno=0L, totalJulioDesempeno=0L;
				Long totalAgostoDesempeno=0L, totalSeptiembreDesempeno=0L, totalOctubreDesempeno=0L, totalNoviembreDesempeno=0L, totalDiciembreDesempeno=0L, totalSubtituloDesempeno=0L;
				for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : desempenosDificil){ 
					totalEneroDesempeno+=subtituloFlujoCajaVO.getCajaMontoEnero().getMontoMes();
					totalFebreroDesempeno+=subtituloFlujoCajaVO.getCajaMontoFebrero().getMontoMes();
					totalMarzoDesempeno+=subtituloFlujoCajaVO.getCajaMontoMarzo().getMontoMes();
					totalAbrilDesempeno+=subtituloFlujoCajaVO.getCajaMontoAbril().getMontoMes();
					totalMayoDesempeno+=subtituloFlujoCajaVO.getCajaMontoMayo().getMontoMes();
					totalJunioDesempeno+=subtituloFlujoCajaVO.getCajaMontoJunio().getMontoMes();
					totalJulioDesempeno+=subtituloFlujoCajaVO.getCajaMontoJulio().getMontoMes();
					totalAgostoDesempeno+=subtituloFlujoCajaVO.getCajaMontoAgosto().getMontoMes();
					totalSeptiembreDesempeno+=subtituloFlujoCajaVO.getCajaMontoSeptiembre().getMontoMes();
					totalOctubreDesempeno+=subtituloFlujoCajaVO.getCajaMontoOctubre().getMontoMes();
					totalNoviembreDesempeno+=subtituloFlujoCajaVO.getCajaMontoNoviembre().getMontoMes();
					totalDiciembreDesempeno+=subtituloFlujoCajaVO.getCajaMontoDiciembre().getMontoMes();
					totalSubtituloDesempeno+=subtituloFlujoCajaVO.getTotalMontos();
				}
				totalSubtituloDesempenoFlujoCajaVO.getCajaMontoEnero().setMontoMes(totalEneroDesempeno);
				totalSubtituloDesempenoFlujoCajaVO.getCajaMontoFebrero().setMontoMes(totalFebreroDesempeno);
				totalSubtituloDesempenoFlujoCajaVO.getCajaMontoMarzo().setMontoMes(totalMarzoDesempeno);
				totalSubtituloDesempenoFlujoCajaVO.getCajaMontoAbril().setMontoMes(totalAbrilDesempeno);
				totalSubtituloDesempenoFlujoCajaVO.getCajaMontoMayo().setMontoMes(totalMayoDesempeno);
				totalSubtituloDesempenoFlujoCajaVO.getCajaMontoJunio().setMontoMes(totalJunioDesempeno);
				totalSubtituloDesempenoFlujoCajaVO.getCajaMontoJulio().setMontoMes(totalJulioDesempeno);
				totalSubtituloDesempenoFlujoCajaVO.getCajaMontoAgosto().setMontoMes(totalAgostoDesempeno);
				totalSubtituloDesempenoFlujoCajaVO.getCajaMontoSeptiembre().setMontoMes(totalSeptiembreDesempeno);
				totalSubtituloDesempenoFlujoCajaVO.getCajaMontoOctubre().setMontoMes(totalOctubreDesempeno);
				totalSubtituloDesempenoFlujoCajaVO.getCajaMontoNoviembre().setMontoMes(totalNoviembreDesempeno);
				totalSubtituloDesempenoFlujoCajaVO.getCajaMontoDiciembre().setMontoMes(totalDiciembreDesempeno);
				totalSubtituloDesempenoFlujoCajaVO.setTotalMontos(totalSubtituloDesempeno);
				desempenosDificil.add(totalSubtituloDesempenoFlujoCajaVO);
				estimacionFlujoCajaSubtituloSheetExcel.setItems(desempenosDificil);
				generadorExcel.addSheet(estimacionFlujoCajaSubtituloSheetExcel, "Per Cápita");
			}

		}
		System.out.println("Fin pestaña percapita");
		if(removeLast){
			header.remove(header.size()-1);
			removeLast = false;
		}
		System.out.println("Inicio pestaña sub24");
		if(flujoCajaSub24 != null && !flujoCajaSub24.isEmpty()){
			header.set(1,new CellExcelVO(programaAno.getPrograma().getNombre() , 12, 1));
			header.add(new CellExcelVO("TOTAL " + programaAno.getPrograma().getNombre() + " SUBT. 24 " + anoCurso, 1, 2));
			removeLast = true;
			SubtituloFlujoCajaVO totalSubtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
			totalSubtituloFlujoCajaVO.setServicio("Total");
			Long totalEnero=0L, totalFebrero=0L, totalMarzo=0L, totalAbril=0L, totalMayo=0L, totalJunio=0L, totalJulio=0L;
			Long totalAgosto=0L, totalSeptiembre=0L, totalOctubre=0L, totalNoviembre=0L, totalDiciembre=0L, totalSubtitulo24=0L;
			for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : flujoCajaSub24){ 
				totalEnero+=subtituloFlujoCajaVO.getCajaMontoEnero().getMontoMes();
				totalFebrero+=subtituloFlujoCajaVO.getCajaMontoFebrero().getMontoMes();
				totalMarzo+=subtituloFlujoCajaVO.getCajaMontoMarzo().getMontoMes();
				totalAbril+=subtituloFlujoCajaVO.getCajaMontoAbril().getMontoMes();
				totalMayo+=subtituloFlujoCajaVO.getCajaMontoMayo().getMontoMes();
				totalJunio+=subtituloFlujoCajaVO.getCajaMontoJunio().getMontoMes();
				totalJulio+=subtituloFlujoCajaVO.getCajaMontoJulio().getMontoMes();
				totalAgosto+=subtituloFlujoCajaVO.getCajaMontoAgosto().getMontoMes();
				totalSeptiembre+=subtituloFlujoCajaVO.getCajaMontoSeptiembre().getMontoMes();
				totalOctubre+=subtituloFlujoCajaVO.getCajaMontoOctubre().getMontoMes();
				totalNoviembre+=subtituloFlujoCajaVO.getCajaMontoNoviembre().getMontoMes();
				totalDiciembre+=subtituloFlujoCajaVO.getCajaMontoDiciembre().getMontoMes();
				totalSubtitulo24+=subtituloFlujoCajaVO.getTotalMontos();
			}
			totalSubtituloFlujoCajaVO.getCajaMontoEnero().setMontoMes(totalEnero);
			totalSubtituloFlujoCajaVO.getCajaMontoFebrero().setMontoMes(totalFebrero);
			totalSubtituloFlujoCajaVO.getCajaMontoMarzo().setMontoMes(totalMarzo);
			totalSubtituloFlujoCajaVO.getCajaMontoAbril().setMontoMes(totalAbril);
			totalSubtituloFlujoCajaVO.getCajaMontoMayo().setMontoMes(totalMayo);
			totalSubtituloFlujoCajaVO.getCajaMontoJunio().setMontoMes(totalJunio);
			totalSubtituloFlujoCajaVO.getCajaMontoJulio().setMontoMes(totalJulio);
			totalSubtituloFlujoCajaVO.getCajaMontoAgosto().setMontoMes(totalAgosto);
			totalSubtituloFlujoCajaVO.getCajaMontoSeptiembre().setMontoMes(totalSeptiembre);
			totalSubtituloFlujoCajaVO.getCajaMontoOctubre().setMontoMes(totalOctubre);
			totalSubtituloFlujoCajaVO.getCajaMontoNoviembre().setMontoMes(totalNoviembre);
			totalSubtituloFlujoCajaVO.getCajaMontoDiciembre().setMontoMes(totalDiciembre);
			totalSubtituloFlujoCajaVO.setTotalMontos(totalSubtitulo24);
			flujoCajaSub24.add(totalSubtituloFlujoCajaVO);
			EstimacionFlujoCajaSubtituloSheetExcel estimacionFlujoCajaSubtituloSheetExcel = new EstimacionFlujoCajaSubtituloSheetExcel(header, subHeader, null);
			estimacionFlujoCajaSubtituloSheetExcel.setItems(flujoCajaSub24);
			generadorExcel.addSheet(estimacionFlujoCajaSubtituloSheetExcel, "Ref. Mun.");
		}
		System.out.println("Fin pestaña sub24");
		if(removeLast){
			header.remove(header.size()-1);
			removeLast = false;
		}
		System.out.println("Inicio pestaña sub21");
		if(flujoCajaSub21 != null && !flujoCajaSub21.isEmpty()){
			header.set(1,new CellExcelVO(programaAno.getPrograma().getNombre() , 12, 1));
			header.add(new CellExcelVO("TOTAL " + programaAno.getPrograma().getNombre() + " SUBT. 21 " + anoCurso, 1, 2));
			removeLast = true;
			SubtituloFlujoCajaVO totalSubtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
			totalSubtituloFlujoCajaVO.setServicio("Total");
			Long totalEnero=0L, totalFebrero=0L, totalMarzo=0L, totalAbril=0L, totalMayo=0L, totalJunio=0L, totalJulio=0L;
			Long totalAgosto=0L, totalSeptiembre=0L, totalOctubre=0L, totalNoviembre=0L, totalDiciembre=0L, totalSubtitulo21=0L;
			for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : flujoCajaSub21){ 
				totalEnero+=subtituloFlujoCajaVO.getCajaMontoEnero().getMontoMes();
				totalFebrero+=subtituloFlujoCajaVO.getCajaMontoFebrero().getMontoMes();
				totalMarzo+=subtituloFlujoCajaVO.getCajaMontoMarzo().getMontoMes();
				totalAbril+=subtituloFlujoCajaVO.getCajaMontoAbril().getMontoMes();
				totalMayo+=subtituloFlujoCajaVO.getCajaMontoMayo().getMontoMes();
				totalJunio+=subtituloFlujoCajaVO.getCajaMontoJunio().getMontoMes();
				totalJulio+=subtituloFlujoCajaVO.getCajaMontoJulio().getMontoMes();
				totalAgosto+=subtituloFlujoCajaVO.getCajaMontoAgosto().getMontoMes();
				totalSeptiembre+=subtituloFlujoCajaVO.getCajaMontoSeptiembre().getMontoMes();
				totalOctubre+=subtituloFlujoCajaVO.getCajaMontoOctubre().getMontoMes();
				totalNoviembre+=subtituloFlujoCajaVO.getCajaMontoNoviembre().getMontoMes();
				totalDiciembre+=subtituloFlujoCajaVO.getCajaMontoDiciembre().getMontoMes();
				totalSubtitulo21+=subtituloFlujoCajaVO.getTotalMontos();
			}
			totalSubtituloFlujoCajaVO.getCajaMontoEnero().setMontoMes(totalEnero);
			totalSubtituloFlujoCajaVO.getCajaMontoFebrero().setMontoMes(totalFebrero);
			totalSubtituloFlujoCajaVO.getCajaMontoMarzo().setMontoMes(totalMarzo);
			totalSubtituloFlujoCajaVO.getCajaMontoAbril().setMontoMes(totalAbril);
			totalSubtituloFlujoCajaVO.getCajaMontoMayo().setMontoMes(totalMayo);
			totalSubtituloFlujoCajaVO.getCajaMontoJunio().setMontoMes(totalJunio);
			totalSubtituloFlujoCajaVO.getCajaMontoJulio().setMontoMes(totalJulio);
			totalSubtituloFlujoCajaVO.getCajaMontoAgosto().setMontoMes(totalAgosto);
			totalSubtituloFlujoCajaVO.getCajaMontoSeptiembre().setMontoMes(totalSeptiembre);
			totalSubtituloFlujoCajaVO.getCajaMontoOctubre().setMontoMes(totalOctubre);
			totalSubtituloFlujoCajaVO.getCajaMontoNoviembre().setMontoMes(totalNoviembre);
			totalSubtituloFlujoCajaVO.getCajaMontoDiciembre().setMontoMes(totalDiciembre);
			totalSubtituloFlujoCajaVO.setTotalMontos(totalSubtitulo21);
			flujoCajaSub21.add(totalSubtituloFlujoCajaVO);
			EstimacionFlujoCajaSubtituloSheetExcel estimacionFlujoCajaSubtituloSheetExcel = new EstimacionFlujoCajaSubtituloSheetExcel(header, subHeader, null);
			estimacionFlujoCajaSubtituloSheetExcel.setItems(flujoCajaSub21);
			generadorExcel.addSheet(estimacionFlujoCajaSubtituloSheetExcel, "21");
		}
		System.out.println("Fin pestaña sub21");
		if(removeLast){
			header.remove(header.size()-1);
			removeLast = false;
		}
		System.out.println("Inicio pestaña sub22");
		if(flujoCajaSub22 != null && !flujoCajaSub22.isEmpty()){
			header.set(1,new CellExcelVO(programaAno.getPrograma().getNombre() , 12, 1));
			header.add(new CellExcelVO("TOTAL " + programaAno.getPrograma().getNombre() + " SUBT. 22 " + anoCurso, 1, 2));
			removeLast = true;
			SubtituloFlujoCajaVO totalSubtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
			totalSubtituloFlujoCajaVO.setServicio("Total");
			Long totalEnero=0L, totalFebrero=0L, totalMarzo=0L, totalAbril=0L, totalMayo=0L, totalJunio=0L, totalJulio=0L;
			Long totalAgosto=0L, totalSeptiembre=0L, totalOctubre=0L, totalNoviembre=0L, totalDiciembre=0L, totalSubtitulo22=0L;
			for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : flujoCajaSub22){ 
				totalEnero+=subtituloFlujoCajaVO.getCajaMontoEnero().getMontoMes();
				totalFebrero+=subtituloFlujoCajaVO.getCajaMontoFebrero().getMontoMes();
				totalMarzo+=subtituloFlujoCajaVO.getCajaMontoMarzo().getMontoMes();
				totalAbril+=subtituloFlujoCajaVO.getCajaMontoAbril().getMontoMes();
				totalMayo+=subtituloFlujoCajaVO.getCajaMontoMayo().getMontoMes();
				totalJunio+=subtituloFlujoCajaVO.getCajaMontoJunio().getMontoMes();
				totalJulio+=subtituloFlujoCajaVO.getCajaMontoJulio().getMontoMes();
				totalAgosto+=subtituloFlujoCajaVO.getCajaMontoAgosto().getMontoMes();
				totalSeptiembre+=subtituloFlujoCajaVO.getCajaMontoSeptiembre().getMontoMes();
				totalOctubre+=subtituloFlujoCajaVO.getCajaMontoOctubre().getMontoMes();
				totalNoviembre+=subtituloFlujoCajaVO.getCajaMontoNoviembre().getMontoMes();
				totalDiciembre+=subtituloFlujoCajaVO.getCajaMontoDiciembre().getMontoMes();
				totalSubtitulo22+=subtituloFlujoCajaVO.getTotalMontos();
			}
			totalSubtituloFlujoCajaVO.getCajaMontoEnero().setMontoMes(totalEnero);
			totalSubtituloFlujoCajaVO.getCajaMontoFebrero().setMontoMes(totalFebrero);
			totalSubtituloFlujoCajaVO.getCajaMontoMarzo().setMontoMes(totalMarzo);
			totalSubtituloFlujoCajaVO.getCajaMontoAbril().setMontoMes(totalAbril);
			totalSubtituloFlujoCajaVO.getCajaMontoMayo().setMontoMes(totalMayo);
			totalSubtituloFlujoCajaVO.getCajaMontoJunio().setMontoMes(totalJunio);
			totalSubtituloFlujoCajaVO.getCajaMontoJulio().setMontoMes(totalJulio);
			totalSubtituloFlujoCajaVO.getCajaMontoAgosto().setMontoMes(totalAgosto);
			totalSubtituloFlujoCajaVO.getCajaMontoSeptiembre().setMontoMes(totalSeptiembre);
			totalSubtituloFlujoCajaVO.getCajaMontoOctubre().setMontoMes(totalOctubre);
			totalSubtituloFlujoCajaVO.getCajaMontoNoviembre().setMontoMes(totalNoviembre);
			totalSubtituloFlujoCajaVO.getCajaMontoDiciembre().setMontoMes(totalDiciembre);
			totalSubtituloFlujoCajaVO.setTotalMontos(totalSubtitulo22);
			flujoCajaSub22.add(totalSubtituloFlujoCajaVO);
			EstimacionFlujoCajaSubtituloSheetExcel estimacionFlujoCajaSubtituloSheetExcel = new EstimacionFlujoCajaSubtituloSheetExcel(header, subHeader, null);
			estimacionFlujoCajaSubtituloSheetExcel.setItems(flujoCajaSub22);
			generadorExcel.addSheet(estimacionFlujoCajaSubtituloSheetExcel, "22");
		}
		System.out.println("Fin pestaña sub22");
		if(removeLast){
			header.remove(header.size()-1);
			removeLast = false;
		}
		System.out.println("Inicio pestaña sub29");
		if(flujoCajaSub29 != null && !flujoCajaSub29.isEmpty()){
			header.set(1,new CellExcelVO(programaAno.getPrograma().getNombre() , 12, 1));
			header.add(new CellExcelVO("TOTAL " + programaAno.getPrograma().getNombre() + " SUBT. 29 " + anoCurso, 1, 2));
			removeLast = true;
			SubtituloFlujoCajaVO totalSubtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
			totalSubtituloFlujoCajaVO.setServicio("Total");
			Long totalEnero=0L, totalFebrero=0L, totalMarzo=0L, totalAbril=0L, totalMayo=0L, totalJunio=0L, totalJulio=0L;
			Long totalAgosto=0L, totalSeptiembre=0L, totalOctubre=0L, totalNoviembre=0L, totalDiciembre=0L, totalSubtitulo29=0L;
			for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : flujoCajaSub29){ 
				totalEnero+=subtituloFlujoCajaVO.getCajaMontoEnero().getMontoMes();
				totalFebrero+=subtituloFlujoCajaVO.getCajaMontoFebrero().getMontoMes();
				totalMarzo+=subtituloFlujoCajaVO.getCajaMontoMarzo().getMontoMes();
				totalAbril+=subtituloFlujoCajaVO.getCajaMontoAbril().getMontoMes();
				totalMayo+=subtituloFlujoCajaVO.getCajaMontoMayo().getMontoMes();
				totalJunio+=subtituloFlujoCajaVO.getCajaMontoJunio().getMontoMes();
				totalJulio+=subtituloFlujoCajaVO.getCajaMontoJulio().getMontoMes();
				totalAgosto+=subtituloFlujoCajaVO.getCajaMontoAgosto().getMontoMes();
				totalSeptiembre+=subtituloFlujoCajaVO.getCajaMontoSeptiembre().getMontoMes();
				totalOctubre+=subtituloFlujoCajaVO.getCajaMontoOctubre().getMontoMes();
				totalNoviembre+=subtituloFlujoCajaVO.getCajaMontoNoviembre().getMontoMes();
				totalDiciembre+=subtituloFlujoCajaVO.getCajaMontoDiciembre().getMontoMes();
				totalSubtitulo29+=subtituloFlujoCajaVO.getTotalMontos();
			}
			totalSubtituloFlujoCajaVO.getCajaMontoEnero().setMontoMes(totalEnero);
			totalSubtituloFlujoCajaVO.getCajaMontoFebrero().setMontoMes(totalFebrero);
			totalSubtituloFlujoCajaVO.getCajaMontoMarzo().setMontoMes(totalMarzo);
			totalSubtituloFlujoCajaVO.getCajaMontoAbril().setMontoMes(totalAbril);
			totalSubtituloFlujoCajaVO.getCajaMontoMayo().setMontoMes(totalMayo);
			totalSubtituloFlujoCajaVO.getCajaMontoJunio().setMontoMes(totalJunio);
			totalSubtituloFlujoCajaVO.getCajaMontoJulio().setMontoMes(totalJulio);
			totalSubtituloFlujoCajaVO.getCajaMontoAgosto().setMontoMes(totalAgosto);
			totalSubtituloFlujoCajaVO.getCajaMontoSeptiembre().setMontoMes(totalSeptiembre);
			totalSubtituloFlujoCajaVO.getCajaMontoOctubre().setMontoMes(totalOctubre);
			totalSubtituloFlujoCajaVO.getCajaMontoNoviembre().setMontoMes(totalNoviembre);
			totalSubtituloFlujoCajaVO.getCajaMontoDiciembre().setMontoMes(totalDiciembre);
			totalSubtituloFlujoCajaVO.setTotalMontos(totalSubtitulo29);
			flujoCajaSub29.add(totalSubtituloFlujoCajaVO);
			EstimacionFlujoCajaSubtituloSheetExcel estimacionFlujoCajaSubtituloSheetExcel = new EstimacionFlujoCajaSubtituloSheetExcel(header, subHeader, null);
			estimacionFlujoCajaSubtituloSheetExcel.setItems(flujoCajaSub29);
			generadorExcel.addSheet(estimacionFlujoCajaSubtituloSheetExcel, "29");
		}
		System.out.println("fin pestaña sub29");
		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderEstimacionFlujoCaja.replace("{ANO}", ano.toString()));
			System.out.println("response AsignacionRecursosPercapitaSheetExcel --->" + response);
			TipoDocumento tipoDocumento = new TipoDocumento(TipoDocumentosProcesos.PLANTILLAPROPUESTA.getId());
			documentService.createDocumentPropuestaEstimacionFlujoCaja(programaAno, tipoDocumento, response.getNodeRef(), response.getFileName(), contenType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Fin generarPlanillaPropuesta demoro " + ((System.currentTimeMillis() - milliseconds) / 1000) +" segundos");
	}

	private SubtituloFlujoCajaVO getPercapitaFlujoCajaVO(String nombreServicio, Long percapitaMes) {
		SubtituloFlujoCajaVO subtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
		subtituloFlujoCajaVO.setServicio(nombreServicio);
		for(int mes = 1; mes <= 12; mes++){
			Mes mesDTO = mesDAO.getMesPorID(mes);
			CajaMontoSummaryVO cajaMontoSummaryVO = new CajaMontoSummaryVO();
			cajaMontoSummaryVO.setIdMes(mesDTO.getIdMes());
			cajaMontoSummaryVO.setNombreMes(mesDTO.getNombre());
			cajaMontoSummaryVO.setMontoMes(percapitaMes);
			switch (mes) {
			case 1:
				subtituloFlujoCajaVO.setCajaMontoEnero(cajaMontoSummaryVO);
				break;
			case 2:
				subtituloFlujoCajaVO.setCajaMontoFebrero(cajaMontoSummaryVO);
				break;
			case 3:
				subtituloFlujoCajaVO.setCajaMontoMarzo(cajaMontoSummaryVO);
				break;
			case 4:
				subtituloFlujoCajaVO.setCajaMontoAbril(cajaMontoSummaryVO);
				break;
			case 5:
				subtituloFlujoCajaVO.setCajaMontoMayo(cajaMontoSummaryVO);
				break;
			case 6:
				subtituloFlujoCajaVO.setCajaMontoJunio(cajaMontoSummaryVO);
				break;
			case 7:
				subtituloFlujoCajaVO.setCajaMontoJulio(cajaMontoSummaryVO);
				break;
			case 8:
				subtituloFlujoCajaVO.setCajaMontoAgosto(cajaMontoSummaryVO);
				break;
			case 9:
				subtituloFlujoCajaVO.setCajaMontoSeptiembre(cajaMontoSummaryVO);
				break;
			case 10:
				subtituloFlujoCajaVO.setCajaMontoOctubre(cajaMontoSummaryVO);
				break;
			case 11:
				subtituloFlujoCajaVO.setCajaMontoNoviembre(cajaMontoSummaryVO);
				break;
			case 12:
				subtituloFlujoCajaVO.setCajaMontoDiciembre(cajaMontoSummaryVO);
				break;
			default:
				break;
			}
		}
		return subtituloFlujoCajaVO;
	}

	private SubtituloFlujoCajaVO getDesempenoDificialFlujoCajaVO(String nombreServicio, Integer desempeno) {
		SubtituloFlujoCajaVO subtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
		subtituloFlujoCajaVO.setServicio(nombreServicio);
		for(int mes = 1; mes <= 12; mes++){
			Mes mesDTO = mesDAO.getMesPorID(mes);
			CajaMontoSummaryVO cajaMontoSummaryVO = new CajaMontoSummaryVO();
			cajaMontoSummaryVO.setIdMes(mesDTO.getIdMes());
			cajaMontoSummaryVO.setNombreMes(mesDTO.getNombre());
			if(desempeno != null){
				cajaMontoSummaryVO.setMontoMes(desempeno.longValue());
			}else{
				cajaMontoSummaryVO.setMontoMes(0L);
			}
			switch (mes) {
			case 1:
				subtituloFlujoCajaVO.setCajaMontoEnero(cajaMontoSummaryVO);
				break;
			case 2:
				subtituloFlujoCajaVO.setCajaMontoFebrero(cajaMontoSummaryVO);
				break;
			case 3:
				subtituloFlujoCajaVO.setCajaMontoMarzo(cajaMontoSummaryVO);
				break;
			case 4:
				subtituloFlujoCajaVO.setCajaMontoAbril(cajaMontoSummaryVO);
				break;
			case 5:
				subtituloFlujoCajaVO.setCajaMontoMayo(cajaMontoSummaryVO);
				break;
			case 6:
				subtituloFlujoCajaVO.setCajaMontoJunio(cajaMontoSummaryVO);
				break;
			case 7:
				subtituloFlujoCajaVO.setCajaMontoJulio(cajaMontoSummaryVO);
				break;
			case 8:
				subtituloFlujoCajaVO.setCajaMontoAgosto(cajaMontoSummaryVO);
				break;
			case 9:
				subtituloFlujoCajaVO.setCajaMontoSeptiembre(cajaMontoSummaryVO);
				break;
			case 10:
				subtituloFlujoCajaVO.setCajaMontoOctubre(cajaMontoSummaryVO);
				break;
			case 11:
				subtituloFlujoCajaVO.setCajaMontoNoviembre(cajaMontoSummaryVO);
				break;
			case 12:
				subtituloFlujoCajaVO.setCajaMontoDiciembre(cajaMontoSummaryVO);
				break;
			default:
				break;
			}
		}
		return subtituloFlujoCajaVO;
	}


	public void eliminarPlanillaPropuesta(Integer idPrograma, Integer ano) {
		System.out.println("eliminarPlanillaPropuesta idPrograma-->"+idPrograma);
		System.out.println("eliminarPlanillaPropuesta ano-->"+ano);
		ProgramaAno programaAno = programasDAO.getProgramaAnoByIdProgramaAndAno(idPrograma, ano);
		List<DocumentoEstimacionflujocaja> documentosEstimacionFlujoCaja = estimacionFlujoCajaDAO.getDocumentByIDProgramaAnoTipoDocumento(programaAno.getIdProgramaAno(), TipoDocumentosProcesos.PLANTILLAPROPUESTA);
		if(documentosEstimacionFlujoCaja != null && documentosEstimacionFlujoCaja.size() > 0){
			int ultimoDoc = 0;
			for(DocumentoEstimacionflujocaja documentoEstimacionflujocaja : documentosEstimacionFlujoCaja){
				if(ultimoDoc == 0){
					String key = ((documentoEstimacionflujocaja.getIdDocumento().getNodeRef() == null) ? documentoEstimacionflujocaja.getIdDocumento().getPath() : documentoEstimacionflujocaja.getIdDocumento().getNodeRef().replace("workspace://SpacesStore/", ""));
					System.out.println("key->"+key);
					alfrescoService.delete(key);
					ultimoDoc++;
				}
				List<Integer> idDocumentosEstimacion = new ArrayList<Integer>();
				idDocumentosEstimacion.add(documentoEstimacionflujocaja.getId());
				estimacionFlujoCajaDAO.deleteDocumentoEstimacionflujoCaja(idDocumentosEstimacion);
				estimacionFlujoCajaDAO.deleteDocumento(documentoEstimacionflujocaja.getIdDocumento().getId());
			}
		}
	}

	@Asynchronous
	public void notificarUsuarioConsolidador(Integer idPrograma, Integer ano, String usuario) {
		// Iniciar el segundo proceso.
		ProgramaAno programaAno = programasDAO.getProgramaAnoByIdProgramaAndAno(idPrograma, ano);
		programaAno.setEstadoFlujoCaja(new EstadoPrograma(EstadosProgramas.REVISADO.getId()));
		String mailTo = usuarioDAO.getEmailByUsername(usuario);
		EmailVO emailVO = new EmailVO();
		emailVO.setContent("Revisar Consolidación de Estimación de Flujos de Caja <p> para el programa "
				+ programaAno.getPrograma().getNombre()
				+ " que se encuentra disponible en su Bandeja de Tareas.</p>");
		emailVO.setSubject("Revisar Consolidación de Estimación de Flujos de Caja");
		emailVO.setTo(mailTo);
		emailService.sendMail(emailVO);
		System.out.println("Fin enviar mail consolidador");
		programaAno.setEstado(new EstadoPrograma(EstadosProgramas.FINALIZADO.getId()));	
		System.out.println("Fin notificarUsuarioConsolidador");
	}


	public Integer eliminarOrdinarioFonasa(Integer idPrograma, Integer ano) {
		//Eliminar todos menos el ultimo.
		ProgramaAno programaAno = programasDAO.getProgramaAnoByIdProgramaAndAno(idPrograma, ano);
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummary =  getLastDocumentSummaryEstimacionFlujoCajaType(programaAno.getIdProgramaAno(), TipoDocumentosProcesos.PLANTILLABORRADORORDINARIOPROGRAMACIONCAJA);

		TipoDocumento tipoDocumento = new TipoDocumento(TipoDocumentosProcesos.PLANTILLABORRADORORDINARIOPROGRAMACIONCAJA.getId());
		List<DocumentoEstimacionflujocaja> lstDocEstimacionFlujoCaja = documentService.getDocumentEstimacionFlujoCajaByIDProgramaAnoTipoDocumento(programaAno, tipoDocumento);
		String keyUltimo = ((referenciaDocumentoSummary.getNodeRef() == null) ? referenciaDocumentoSummary.getPath() : referenciaDocumentoSummary.getNodeRef().replace("workspace://SpacesStore/", ""));
		for (DocumentoEstimacionflujocaja documentoEstimacionflujocaja : lstDocEstimacionFlujoCaja) {
			String key = ((documentoEstimacionflujocaja.getIdDocumento().getNodeRef() == null) ? documentoEstimacionflujocaja.getIdDocumento().getPath() : documentoEstimacionflujocaja.getIdDocumento().getNodeRef()
					.replace("workspace://SpacesStore/", ""));
			if (key.equals(keyUltimo)){
				alfrescoService.delete(key);
			}
		}
		return 0;
	}

	public void moveToAlfresco(Integer idPrograma,	Integer referenciaDocumentoId, TipoDocumentosProcesos tipoDocumento, boolean versionFinal, Integer ano) {
		System.out.println("Buscando referenciaDocumentoId=" + referenciaDocumentoId);
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummary = documentService.getDocumentSummary(referenciaDocumentoId);
		System.out.println("Buscando referenciaDocumentoSummary=" + referenciaDocumentoSummary);
		if (referenciaDocumentoSummary != null) {
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contenType = mimemap.getContentType(referenciaDocumentoSummary.getPath().toLowerCase());
			BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummary.getPath()), contenType, folderEstimacionFlujoCaja.replace("{ANO}", ano.toString()));
			System.out.println("response upload template --->" + response);
			documentService.updateDocumentTemplate(referenciaDocumentoSummary.getId(), response.getNodeRef(), response.getFileName(), contenType);
			ProgramaAno programaAno = programasDAO.getProgramaAnoByIdProgramaAndAno(idPrograma, ano);
			documentService.createDocumentOrdinarioProgramacionEstimacionFlujoCaja(programaAno,tipoDocumento,referenciaDocumentoId,versionFinal);
		}

	}

	public ReferenciaDocumentoSummaryVO getLastDocumentSummaryEstimacionFlujoCajaType(Integer idProgramaAno, TipoDocumentosProcesos tipoDocumento) {
		return documentService.getLastDocumentoSummaryByEstimacionFlujoCajaType(idProgramaAno, tipoDocumento);

	}

	public ReferenciaDocumentoSummaryVO getLastDocumentSummaryEstimacionFlujoCajaTipoDocumento(TipoDocumentosProcesos tipoDocumento) {
		return documentService.getLastDocumentoSummaryByEstimacionFlujoCajaTipoDocumento(tipoDocumento);
	}

	//Obtiene el id del programa para el a�o siguiente para hacer la estimacion del flujo de caja.
	public Integer obtenerIdProgramaAno(Integer idPrograma, Integer ano) {
		// Obtener el siguiente programa ano.
		ProgramaAno programaAnoEvaluacion = programasDAO.getProgramaAnoByIDProgramaAno(idPrograma, ano);
		// CREAMOS EL PROGRAMA PARA EL A�O SIGUIENTE SI NO EXISTE.
		if (programaAnoEvaluacion == null) {
			AnoEnCurso anoEvaluacion = programasDAO.getAnoEnCursoById(ano);
			if (anoEvaluacion == null){
				AnoEnCurso anoBase = programasDAO.getAnoEnCursoById((ano-1));
				anoEvaluacion = new AnoEnCurso();
				anoEvaluacion.setAno(ano);
				anoEvaluacion.setAsignacionAdultoMayor(anoBase.getAsignacionAdultoMayor());
				anoEvaluacion.setInflactor(anoBase.getInflactor());
				anoEvaluacion.setMontoPercapitalBasal(anoBase.getMontoPercapitalBasal());
				anoEvaluacion.setInflactorMarcoPresupuestario(anoBase.getInflactorMarcoPresupuestario());
				programasDAO.saveAnoCurso(anoEvaluacion);
			}

			programaAnoEvaluacion = new ProgramaAno();
			programaAnoEvaluacion.setAno(anoEvaluacion);
			programaAnoEvaluacion.setEstado(new EstadoPrograma(EstadosProgramas.SININICIAR.getId()));
			programaAnoEvaluacion.setEstadoFlujoCaja(new EstadoPrograma(EstadosProgramas.SININICIAR.getId()));
			programaAnoEvaluacion.setEstadoConvenio(new EstadoPrograma(EstadosProgramas.SININICIAR.getId()));
			programaAnoEvaluacion.setEstadoModificacionAPS(new EstadoPrograma(EstadosProgramas.SININICIAR.getId()));
			programaAnoEvaluacion.setEstadoOT(new EstadoPrograma(EstadosProgramas.SININICIAR.getId()));
			programaAnoEvaluacion.setEstadoreliquidacion(new EstadoPrograma(EstadosProgramas.SININICIAR.getId()));
			programasDAO.saveProgramaAno(programaAnoEvaluacion);
		}
		return programaAnoEvaluacion.getIdProgramaAno();
	}

	public Integer enviarOrdinarioFONASA(Integer idPrograma, Integer ano) {
		System.out.println("enviarOrdinarioFONASA idPrograma--> "+idPrograma);
		System.out.println("enviarOrdinarioFONASA ano--> "+ano);
		ProgramaAno programaAno = programasDAO.getProgramaAnoByIdProgramaAndAno(idPrograma, ano);
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummary = getLastDocumentSummaryEstimacionFlujoCajaType(programaAno.getIdProgramaAno(), TipoDocumentosProcesos.PLANTILLABORRADORORDINARIOPROGRAMACIONCAJA);
		DocumentoVO documentDecretoAporteEstatalVO = documentService.getDocument(referenciaDocumentoSummary.getId());

		String fileNameDecretoAporteEstatal = tmpDirDoc + File.separator + documentDecretoAporteEstatalVO.getName();

		GeneradorWord generadorWordDecretoAporteEstatal = new GeneradorWord(fileNameDecretoAporteEstatal);
		try {
			generadorWordDecretoAporteEstatal.saveContent(documentDecretoAporteEstatalVO.getContent(), XWPFDocument.class);
			List<EmailService.Adjunto> adjuntos = new ArrayList<EmailService.Adjunto>();
			EmailService.Adjunto adjunto = new EmailService.Adjunto();
			adjunto.setDescripcion("Borrador Decreto Aporte Estatal");
			adjunto.setName(documentDecretoAporteEstatalVO.getName());
			adjunto.setUrl((new File(fileNameDecretoAporteEstatal)).toURI().toURL());
			adjuntos.add(adjunto);
			emailService.sendMail("asaavedra@nectia.com", "Borrador Decreto Aporte Estatal", "Estimado " + "usuario prueba" + ": <br /> <p> Se Adjunta Borrador Decreto Aporte Estatal</p>", adjuntos);
			System.out.println("enviarDecretoAporteEstatal se ejecuto correctamente");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 1;
	}

	public List<SubtituloFlujoCajaVO> getMonitoreoServicioByProgramaAnoEstablecimientoComponenteSubtitulo(Integer idProgramaAno, Integer idServicio, Integer idEstablecimiento, List<Integer> idComponentes, Subtitulo subtitulo, Boolean iniciarFlujoCaja) {
		LOGGER.info("Inicio getMonitoreoServicioByProgramaAnoComponenteSubtitulo");
		List<SubtituloFlujoCajaVO> subtitulosFlujosCaja = new ArrayList<SubtituloFlujoCajaVO>();

		List<Establecimiento> establecimientos = new ArrayList<Establecimiento>();
		if(idEstablecimiento != null){
			Establecimiento establecimiento = servicioSaludDAO.getEstablecimientoById(idEstablecimiento);
			establecimientos.add(establecimiento);
		}else{
			if(idServicio != null){
				ServicioSalud servicioSalud = servicioSaludDAO.getById(idServicio);
				if(servicioSalud.getEstablecimientos() != null && servicioSalud.getEstablecimientos().size() > 0){
					for(Establecimiento establecimiento :servicioSalud.getEstablecimientos()){
						establecimientos.add(establecimiento);
					}
				}
			}else{
				List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
				for(ServicioSalud servicioSalud : servicios){
					if(servicioSalud.getEstablecimientos() != null && servicioSalud.getEstablecimientos().size() > 0){
						for(Establecimiento establecimiento :servicioSalud.getEstablecimientos()){
							establecimientos.add(establecimiento);
						}
					}
				}
			}
		}

		if(establecimientos.size() > 0){
			for(Establecimiento establecimiento : establecimientos){
				SubtituloFlujoCajaVO subtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
				subtituloFlujoCajaVO.setMarcoPresupuestario(0L);
				subtituloFlujoCajaVO.setIdServicio(establecimiento.getServicioSalud().getId());
				subtituloFlujoCajaVO.setServicio(establecimiento.getServicioSalud().getNombre());
				subtituloFlujoCajaVO.setIdEstablecimiento(establecimiento.getId());
				subtituloFlujoCajaVO.setEstablecimiento(establecimiento.getNombre());
				for(Integer idComponente : idComponentes){
					ProgramaSubtituloComponentePeso programaSubtituloComponentePeso = programasDAO.getProgramaSubtituloComponentePesoByProgramaComponenteSubtitulo(idProgramaAno, idComponente, subtitulo.getId());
					if(programaSubtituloComponentePeso != null){
						subtituloFlujoCajaVO.setPesoComponentes(subtituloFlujoCajaVO.getPesoComponentes() + programaSubtituloComponentePeso.getPeso());
					}
				}

				subtituloFlujoCajaVO.setPesoComponentes(subtituloFlujoCajaVO.getPesoComponentes()/100.0); 
				Long marcoPresupuestarioServicio = 0L;
				for(Integer idComponente : idComponentes){
					marcoPresupuestarioServicio += programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAno, idComponente, subtitulo.getId());
				}
				subtituloFlujoCajaVO.setMarcoPresupuestario(marcoPresupuestarioServicio);

				for(int mes = 1; mes <= 12; mes++){
					Mes mesDTO = mesDAO.getMesPorID(mes);
					CajaMontoSummaryVO cajaMontoMes = new CajaMontoSummaryVO(null, mes, mesDTO.getNombre());
					Long montoComponentes = 0L;
					for(Integer idComponente : idComponentes){
						CajaMonto cajaMonto = cajaDAO.getCajaMontoByServicioEstablecimientoProgramaComponenteSubtituloMes(establecimiento.getServicioSalud().getId(), establecimiento.getId(),  idProgramaAno, idComponente, subtitulo.getId(), mes);
						montoComponentes += ((cajaMonto != null) ? cajaMonto.getMonto() : 0L);
					}
					cajaMontoMes.setMontoMes(montoComponentes);
					cajaMontoMes.setMontoMesOriginal(montoComponentes);
					switch (mes) {
					case 1:
						subtituloFlujoCajaVO.setCajaMontoEnero(cajaMontoMes);
						break;
					case 2:
						subtituloFlujoCajaVO.setCajaMontoFebrero(cajaMontoMes);
						break;
					case 3:
						subtituloFlujoCajaVO.setCajaMontoMarzo(cajaMontoMes);
						break;
					case 4:
						subtituloFlujoCajaVO.setCajaMontoAbril(cajaMontoMes);
						break;
					case 5:
						subtituloFlujoCajaVO.setCajaMontoMayo(cajaMontoMes);
						break;
					case 6:
						subtituloFlujoCajaVO.setCajaMontoJunio(cajaMontoMes);
						break;
					case 7:
						subtituloFlujoCajaVO.setCajaMontoJulio(cajaMontoMes);
						break;
					case 8:
						subtituloFlujoCajaVO.setCajaMontoAgosto(cajaMontoMes);
						break;
					case 9:
						subtituloFlujoCajaVO.setCajaMontoSeptiembre(cajaMontoMes);
						break;
					case 10:
						subtituloFlujoCajaVO.setCajaMontoOctubre(cajaMontoMes);
						break;
					case 11:
						subtituloFlujoCajaVO.setCajaMontoNoviembre(cajaMontoMes);
						break;
					case 12:
						subtituloFlujoCajaVO.setCajaMontoDiciembre(cajaMontoMes);
						break;
					default:
						break;
					}
				}
				subtitulosFlujosCaja.add(subtituloFlujoCajaVO);
			}
		}

		if(!iniciarFlujoCaja){
			Integer mesActual = Integer.parseInt(getMesCurso(true));
			for(SubtituloFlujoCajaVO subtituloFlujoCaja : subtitulosFlujosCaja){
				ConveniosSummaryVO convenioRecibido = new ConveniosSummaryVO(0.0, 0);
				TransferenciaSummaryVO transferenciasAcumulada = new TransferenciaSummaryVO(0.0, 0L);
				Long totalConveniosPorServicio = 0L;
				for(Integer idComponente : idComponentes){
					ConvenioServicioComponenteVO convenioServicioComponente = conveniosService.getConvenioServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(idProgramaAno, idComponente, subtitulo.getId(), subtituloFlujoCaja.getIdEstablecimiento(), EstadosConvenios.INGRESADO);
					if(convenioServicioComponente == null){
						convenioServicioComponente = conveniosService.getConvenioServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(idProgramaAno, idComponente, subtitulo.getId(), subtituloFlujoCaja.getIdEstablecimiento(), EstadosConvenios.APROBADO);
					}
					if(convenioServicioComponente == null){
						convenioServicioComponente = conveniosService.getConvenioServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(idProgramaAno, idComponente, subtitulo.getId(), subtituloFlujoCaja.getIdEstablecimiento(), EstadosConvenios.TRAMITE);
					}
					if(convenioServicioComponente == null){
						convenioServicioComponente = conveniosService.getConvenioServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(idProgramaAno, idComponente, subtitulo.getId(), subtituloFlujoCaja.getIdEstablecimiento(), EstadosConvenios.PAGADO);
					}
					if(convenioServicioComponente != null){
						totalConveniosPorServicio += ((convenioServicioComponente.getMonto() != null) ? convenioServicioComponente.getMonto() : 0L);
					}
				}
				convenioRecibido.setMonto(totalConveniosPorServicio.intValue());

				if(subtituloFlujoCaja .getMarcoPresupuestario() != 0){
					Double porcentaje = ((totalConveniosPorServicio * 100.0) / subtituloFlujoCaja .getMarcoPresupuestario());
					porcentaje = porcentaje / 100.0;
					convenioRecibido.setPorcentaje(porcentaje);
				}
				subtituloFlujoCaja.setConvenioRecibido(convenioRecibido);

				Long totalRemesasPorServicio = 0L;
				System.out.println("marcoPresupuestario-->"+subtituloFlujoCaja .getMarcoPresupuestario());
				if(subtituloFlujoCaja .getMarcoPresupuestario() != 0){
					for(Integer idComponente : idComponentes){
						System.out.println("buscando las remesas para este servicio-");
						List<DetalleRemesas> detalleRemesas = remesasDAO.getRemesasPagadasEstablecimientosProgramaAnoEstablecimientoComponenteSubtituloMesHasta(idProgramaAno, subtituloFlujoCaja.getIdEstablecimiento(), idComponente, subtitulo.getId(), mesActual);
						if(detalleRemesas != null && detalleRemesas.size() > 0){
							for(DetalleRemesas detalleRemesa : detalleRemesas){
								totalRemesasPorServicio += detalleRemesa.getMontoRemesa();
							}
						}
					}
				}

				transferenciasAcumulada.setMonto(totalRemesasPorServicio);
				if(subtituloFlujoCaja .getMarcoPresupuestario() != 0){
					Double porcentajeRemesas = ((totalRemesasPorServicio * 100.0) / subtituloFlujoCaja .getMarcoPresupuestario());
					porcentajeRemesas = porcentajeRemesas / 100.0;
					transferenciasAcumulada.setPorcentaje(porcentajeRemesas);
				}
				subtituloFlujoCaja.setTransferenciaAcumulada(transferenciasAcumulada);
			}
		}
		LOGGER.info("Fin getMonitoreoServicioByProgramaAnoComponenteSubtitulo");
		return subtitulosFlujosCaja;
	}

	public List<SubtituloFlujoCajaVO> getMonitoreoComunaByProgramaAnoComponenteSubtitulo(Integer idProgramaAno, Integer idServicio, Integer idComuna, List<Integer> idComponentes, Subtitulo subtitulo, Boolean iniciarFlujoCaja) {
		LOGGER.info("Inicio getMonitoreoComunaByProgramaAnoComponenteSubtitulo");
		List<SubtituloFlujoCajaVO> subtitulosFlujosCaja = new ArrayList<SubtituloFlujoCajaVO>();

		List<Comuna> comunas = new ArrayList<Comuna>();
		if(idComuna != null){
			Comuna comuna = servicioSaludDAO.getComunaById(idComuna);
			comunas.add(comuna);
		}else{
			if(idServicio != null){
				ServicioSalud servicioSalud = servicioSaludDAO.getById(idServicio);
				if(servicioSalud.getComunas() != null && servicioSalud.getComunas().size() > 0){
					for(Comuna comuna :servicioSalud.getComunas()){
						comunas.add(comuna);
					}
				}
			}else{
				List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
				for(ServicioSalud servicioSalud : servicios){
					if(servicioSalud.getComunas() != null && servicioSalud.getComunas().size() > 0){
						for(Comuna comuna  :servicioSalud.getComunas()){
							comunas.add(comuna);
						}
					}
				}
			}
		}

		if(comunas.size() > 0){
			for(Comuna comuna : comunas){
				SubtituloFlujoCajaVO subtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
				subtituloFlujoCajaVO.setIdServicio(comuna.getServicioSalud().getId());
				subtituloFlujoCajaVO.setServicio(comuna.getServicioSalud().getNombre());
				subtituloFlujoCajaVO.setIdComuna(comuna.getId());
				subtituloFlujoCajaVO.setComuna(comuna.getNombre());
				subtituloFlujoCajaVO.setMarcoPresupuestario(0L);

				for(Integer idComponente : idComponentes){
					ProgramaSubtituloComponentePeso programaSubtituloComponentePeso = programasDAO.getProgramaSubtituloComponentePesoByProgramaComponenteSubtitulo(idProgramaAno, idComponente, subtitulo.getId());
					if(programaSubtituloComponentePeso != null){
						subtituloFlujoCajaVO.setPesoComponentes(subtituloFlujoCajaVO.getPesoComponentes() + programaSubtituloComponentePeso.getPeso());
					}
				}

				subtituloFlujoCajaVO.setPesoComponentes(subtituloFlujoCajaVO.getPesoComponentes()/100.0);
				Long marcoPresupuestarioServicio = 0L;
				for(Integer idComponente : idComponentes){
					marcoPresupuestarioServicio += programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAno, idComponente, subtitulo.getId());
				}
				subtituloFlujoCajaVO.setMarcoPresupuestario(marcoPresupuestarioServicio);

				for(int mes = 1; mes <= 12; mes++){
					Mes mesDTO = mesDAO.getMesPorID(mes);
					CajaMontoSummaryVO cajaMontoMes = new CajaMontoSummaryVO(null, mes, mesDTO.getNombre());
					Long montoComponentes = 0L;

					for(Integer idComponente : idComponentes){
						CajaMonto cajaMonto = cajaDAO.getCajaMontoByServicioComunaProgramaComponenteSubtituloMes(comuna.getServicioSalud().getId(), comuna.getId(), idProgramaAno, idComponente, subtitulo.getId(), mes);
						montoComponentes += ((cajaMonto != null) ? cajaMonto.getMonto() : 0L);
					}
					cajaMontoMes.setMontoMes(montoComponentes);
					cajaMontoMes.setMontoMesOriginal(montoComponentes);
					switch (mes) {
					case 1:
						subtituloFlujoCajaVO.setCajaMontoEnero(cajaMontoMes);
						break;
					case 2:
						subtituloFlujoCajaVO.setCajaMontoFebrero(cajaMontoMes);
						break;
					case 3:
						subtituloFlujoCajaVO.setCajaMontoMarzo(cajaMontoMes);
						break;
					case 4:
						subtituloFlujoCajaVO.setCajaMontoAbril(cajaMontoMes);
						break;
					case 5:
						subtituloFlujoCajaVO.setCajaMontoMayo(cajaMontoMes);
						break;
					case 6:
						subtituloFlujoCajaVO.setCajaMontoJunio(cajaMontoMes);
						break;
					case 7:
						subtituloFlujoCajaVO.setCajaMontoJulio(cajaMontoMes);
						break;
					case 8:
						subtituloFlujoCajaVO.setCajaMontoAgosto(cajaMontoMes);
						break;
					case 9:
						subtituloFlujoCajaVO.setCajaMontoSeptiembre(cajaMontoMes);
						break;
					case 10:
						subtituloFlujoCajaVO.setCajaMontoOctubre(cajaMontoMes);
						break;
					case 11:
						subtituloFlujoCajaVO.setCajaMontoNoviembre(cajaMontoMes);
						break;
					case 12:
						subtituloFlujoCajaVO.setCajaMontoDiciembre(cajaMontoMes);
						break;
					default:
						break;
					}
				}
				subtitulosFlujosCaja.add(subtituloFlujoCajaVO);
			}
		}
		if(!iniciarFlujoCaja){
			Integer mesActual = Integer.parseInt(getMesCurso(true));
			for(SubtituloFlujoCajaVO subtituloFlujoCaja : subtitulosFlujosCaja){
				ConveniosSummaryVO convenioRecibido = new ConveniosSummaryVO(0.0, 0);
				TransferenciaSummaryVO transferenciasAcumulada = new TransferenciaSummaryVO(0.0, 0L);
				Long totalConveniosPorServicio = 0L;
				for(Integer idComponente : idComponentes){
					ConvenioComunaComponenteVO convenioComunaComponente = conveniosService.getConvenioComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, idComponente, subtitulo.getId(), subtituloFlujoCaja.getIdComuna(), EstadosConvenios.INGRESADO);
					if(convenioComunaComponente == null){
						convenioComunaComponente = conveniosService.getConvenioComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, idComponente, subtitulo.getId(), subtituloFlujoCaja.getIdComuna(), EstadosConvenios.APROBADO);
					}
					if(convenioComunaComponente == null){
						convenioComunaComponente = conveniosService.getConvenioComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, idComponente, subtitulo.getId(), subtituloFlujoCaja.getIdComuna(), EstadosConvenios.TRAMITE);
					}
					if(convenioComunaComponente == null){
						convenioComunaComponente = conveniosService.getConvenioComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, idComponente, subtitulo.getId(), subtituloFlujoCaja.getIdComuna(), EstadosConvenios.PAGADO);
					}
					if(convenioComunaComponente != null){
						totalConveniosPorServicio += ((convenioComunaComponente.getMonto() != null) ? convenioComunaComponente.getMonto() : 0L);
					}
				}
				convenioRecibido.setMonto(totalConveniosPorServicio.intValue());
				if(subtituloFlujoCaja .getMarcoPresupuestario() != 0){
					Double porcentaje = ((totalConveniosPorServicio * 100.0) / subtituloFlujoCaja .getMarcoPresupuestario());
					porcentaje = porcentaje / 100.0;
					convenioRecibido.setPorcentaje(porcentaje);
				}
				subtituloFlujoCaja.setConvenioRecibido(convenioRecibido);

				Long totalRemesasPorServicio = 0L;
				System.out.println("marcoPresupuestario-->"+subtituloFlujoCaja .getMarcoPresupuestario());
				if(subtituloFlujoCaja .getMarcoPresupuestario() != 0){
					for(Integer idComponente : idComponentes){
						System.out.println("buscando las remesas para este servicio-");
						List<DetalleRemesas> detalleRemesas = remesasDAO.getRemesasPagadasComunasProgramaAnoServicioComponenteSubtituloMesHasta(idProgramaAno, subtituloFlujoCaja.getIdServicio(), idComponente, subtitulo.getId(), mesActual);
						if(detalleRemesas != null && detalleRemesas.size() > 0){
							for(DetalleRemesas detalleRemesa : detalleRemesas){
								totalRemesasPorServicio += detalleRemesa.getMontoRemesa();
							}
						}
					}
				}
				transferenciasAcumulada.setMonto(totalRemesasPorServicio);
				if(subtituloFlujoCaja .getMarcoPresupuestario() != 0){
					Double porcentajeRemesas = ((totalRemesasPorServicio * 100.0) / subtituloFlujoCaja .getMarcoPresupuestario());
					porcentajeRemesas = porcentajeRemesas / 100.0;
					transferenciasAcumulada.setPorcentaje(porcentajeRemesas);
				}
				subtituloFlujoCaja.setTransferenciaAcumulada(transferenciasAcumulada);
			}
		}
		System.out.println("getMonitoreoComunaByProgramaAnoComponenteSubtitulo-->subtitulosFlujosCaja.size()=" +subtitulosFlujosCaja.size());
		LOGGER.info("getMonitoreoComunaByProgramaAnoComponenteSubtitulo-->subtitulosFlujosCaja.size()=" +subtitulosFlujosCaja.size());
		LOGGER.info("Fin getMonitoreoComunaByProgramaAnoComponenteSubtitulo");
		return subtitulosFlujosCaja;
	}

	public List<SubtituloFlujoCajaVO> getConvenioRemesaByProgramaAnoComponenteSubtitulo(Integer idProgramaAno, List<Integer> idComponentes, Subtitulo subtitulo) {
		List<SubtituloFlujoCajaVO> subtitulosFlujosCaja = new ArrayList<SubtituloFlujoCajaVO>(); 
		List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
		Integer mesActual = Integer.parseInt(getMesCurso(true));
		for(ServicioSalud servicioSalud : servicios){
			SubtituloFlujoCajaVO subtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
			subtituloFlujoCajaVO.setMarcoPresupuestario(0L);
			subtituloFlujoCajaVO.setIdServicio(servicioSalud.getId());
			subtituloFlujoCajaVO.setServicio(servicioSalud.getNombre());

			Long marcoPresupuestario = 0L;

			if(Subtitulo.SUBTITULO24.getId().equals(subtitulo.getId())){
				for(Comuna comuna : servicioSalud.getComunas()){
					for(Integer idComponente : idComponentes){
						marcoPresupuestario += programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAno, idComponente, subtitulo.getId());
					}
				}
			}else{
				for(Establecimiento establecimiento : servicioSalud.getEstablecimientos()){
					for(Integer idComponente : idComponentes){
						marcoPresupuestario += programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAno, idComponente, subtitulo.getId());
					}
				}
			}
			subtituloFlujoCajaVO.setMarcoPresupuestario(marcoPresupuestario);
			Long totalRemesasPorServicio = 0L;

			for(int mes = 1; mes <= 12; mes++){
				Mes mesDTO = mesDAO.getMesPorID(mes);
				CajaMontoSummaryVO cajaMontoMes = new CajaMontoSummaryVO(null, mes, mesDTO.getNombre());
				Long montoComponentes = 0L;
				if(mes < mesActual){
					if(Subtitulo.SUBTITULO24.getId().equals(subtitulo.getId())){
						List<DetalleRemesas> detalleRemesas = remesasDAO.getRemesasPagadasComunasProgramaAnoServicioSubtituloMesActual(idProgramaAno, subtituloFlujoCajaVO.getIdServicio(), subtitulo.getId(), mes);
						if(detalleRemesas != null && detalleRemesas.size() > 0){
							for(DetalleRemesas detalleRemesa : detalleRemesas){
								montoComponentes += detalleRemesa.getMontoRemesa();
							}
						}
					}else{
						List<DetalleRemesas> detalleRemesas = remesasDAO.getRemesasPagadasEstablecimientosProgramaAnoServicioSubtituloMesActual(idProgramaAno, subtituloFlujoCajaVO.getIdServicio(), subtitulo.getId(), mes);
						if(detalleRemesas != null && detalleRemesas.size() > 0){
							for(DetalleRemesas detalleRemesa : detalleRemesas){
								montoComponentes += detalleRemesa.getMontoRemesa();
							}
						}
					}
				}
				totalRemesasPorServicio += montoComponentes;
				cajaMontoMes.setMontoMes(montoComponentes);
				cajaMontoMes.setMontoMesOriginal(montoComponentes);
				switch (mes) {
				case 1:
					subtituloFlujoCajaVO.setCajaMontoEnero(cajaMontoMes);
					break;
				case 2:
					subtituloFlujoCajaVO.setCajaMontoFebrero(cajaMontoMes);
					break;
				case 3:
					subtituloFlujoCajaVO.setCajaMontoMarzo(cajaMontoMes);
					break;
				case 4:
					subtituloFlujoCajaVO.setCajaMontoAbril(cajaMontoMes);
					break;
				case 5:
					subtituloFlujoCajaVO.setCajaMontoMayo(cajaMontoMes);
					break;
				case 6:
					subtituloFlujoCajaVO.setCajaMontoJunio(cajaMontoMes);
					break;
				case 7:
					subtituloFlujoCajaVO.setCajaMontoJulio(cajaMontoMes);
					break;
				case 8:
					subtituloFlujoCajaVO.setCajaMontoAgosto(cajaMontoMes);
					break;
				case 9:
					subtituloFlujoCajaVO.setCajaMontoSeptiembre(cajaMontoMes);
					break;
				case 10:
					subtituloFlujoCajaVO.setCajaMontoOctubre(cajaMontoMes);
					break;
				case 11:
					subtituloFlujoCajaVO.setCajaMontoNoviembre(cajaMontoMes);
					break;
				case 12:
					subtituloFlujoCajaVO.setCajaMontoDiciembre(cajaMontoMes);
					break;
				default:
					break;
				}
			}
			TransferenciaSummaryVO transferenciasAcumulada = new TransferenciaSummaryVO(0.0, 0L);
			transferenciasAcumulada.setMonto(totalRemesasPorServicio);
			if(marcoPresupuestario != 0){
				Double porcentajeRemesas = ((totalRemesasPorServicio * 100.0) / marcoPresupuestario);
				porcentajeRemesas = porcentajeRemesas / 100.0;
				transferenciasAcumulada.setPorcentaje(porcentajeRemesas);
			}
			subtituloFlujoCajaVO.setTransferenciaAcumulada(transferenciasAcumulada);
			subtitulosFlujosCaja.add(subtituloFlujoCajaVO);
		}
		return subtitulosFlujosCaja;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Integer obtenerIdProgramaAnoSiguiente(Integer idProgramaAno, Integer anoSiguiente) {
		ProgramaAno programaAnoActual = programasDAO.getProgramaAnoByID(idProgramaAno);
		ProgramaAno programaAnoSiguiente = programasDAO.getProgramaAnoSiguiente(programaAnoActual.getPrograma().getId(), anoSiguiente);
		if(programaAnoSiguiente == null){
			//crear 
			programaAnoSiguiente = new ProgramaAno();
			AnoEnCurso anoSiguienteDTO = programasDAO.getAnoEnCursoById(anoSiguiente);
			if(anoSiguienteDTO == null) {
				AnoEnCurso anoActualDTO = programasDAO.getAnoEnCursoById(anoSiguiente-1);
				anoSiguienteDTO = new AnoEnCurso();
				anoSiguienteDTO.setAno(anoSiguiente);
				anoSiguienteDTO.setMontoPercapitalBasal(anoActualDTO.getMontoPercapitalBasal());
				anoSiguienteDTO.setAsignacionAdultoMayor(anoActualDTO.getAsignacionAdultoMayor());
				anoSiguienteDTO.setInflactor(anoActualDTO.getInflactor());
				anoSiguienteDTO.setInflactorMarcoPresupuestario(anoActualDTO.getInflactorMarcoPresupuestario());
				programasDAO.saveAnoCurso(anoSiguienteDTO);
			}
			programaAnoSiguiente.setAno(anoSiguienteDTO);
			programaAnoSiguiente.setEstado(new EstadoPrograma(1));
			programaAnoSiguiente.setEstadoFlujoCaja(new EstadoPrograma(1));
			programaAnoSiguiente.setPrograma(programaAnoActual.getPrograma());
			programasDAO.save(programaAnoSiguiente);
		} 
		return programaAnoSiguiente.getIdProgramaAno();
	}

	public void actualizarMonitoreoServicioEstablecimientoSubtituloFlujoCaja(Integer idProgramaAno, Integer idServicio, Integer idEstablecimiento, CajaMontoSummaryVO cajaMontoSummary, Subtitulo subtitulo, Boolean iniciarFlujoCaja) {
		ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idProgramaAno);
		List<Componente> componentes = componenteDAO.getComponentesByIdProgramaAnoIdSubtitulos(idProgramaAno, subtitulo);
		int componentesSize = componentes.size();
		int iteracionPorComponentes = 1;
		int montoAcumulado = 0;
		if(programaAno.getProgramaComponentes() != null &&  programaAno.getProgramaComponentes().size() > 0){
			for(ProgramaComponente programaComponente : programaAno.getProgramaComponentes()){
				CajaMonto cajaMonto = cajaDAO.getCajaMontoByServicioEstablecimientoProgramaComponenteSubtituloMes(idServicio, idEstablecimiento, idProgramaAno, programaComponente.getComponente().getId(), subtitulo.getId(), cajaMontoSummary.getIdMes());
				ProgramaSubtituloComponentePeso programaSubtituloComponentePeso = programasDAO.getProgramaSubtituloComponentePesoByProgramaComponenteSubtitulo(idProgramaAno, programaComponente.getComponente().getId(), subtitulo.getId());
				if(programaSubtituloComponentePeso != null){
					if(iteracionPorComponentes !=  componentesSize){
						int pesoComponente =  (int)programaSubtituloComponentePeso.getPeso();
						System.out.println("actualizando el caja monto="+pesoComponente);
						if((pesoComponente) == 100){
							cajaMonto.setMonto(cajaMontoSummary.getMontoMes().intValue());
							montoAcumulado += cajaMontoSummary.getMontoMes(); 
							cajaMonto.setReparos(true);
						}else{
							System.out.println("actualizando el caja monto");
							int montoActualizado = (int)((pesoComponente * cajaMontoSummary.getMontoMes())/100.0);
							cajaMonto.setMonto(montoActualizado);
							montoAcumulado += montoActualizado;
							cajaMonto.setReparos(true);
						}
					}else{
						cajaMonto.setMonto((int)(cajaMontoSummary.getMontoMes() - montoAcumulado));
						cajaMonto.setReparos(true);
					}
				}
				iteracionPorComponentes++;
			}
		}
	}

	public void actualizarMonitoreoServicioComunaSubtituloFlujoCaja(Integer idProgramaAno, Integer idServicio, Integer idComuna, CajaMontoSummaryVO cajaMontoSummary, Subtitulo subtitulo, Boolean iniciarFlujoCaja) {
		ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idProgramaAno);
		List<Componente> componentes = componenteDAO.getComponentesByIdProgramaAnoIdSubtitulos(idProgramaAno, subtitulo);
		int componentesSize = componentes.size();
		int iteracionPorComponentes = 1;
		int montoAcumulado = 0;
		if(programaAno.getProgramaComponentes() != null &&  programaAno.getProgramaComponentes().size() > 0){
			for(ProgramaComponente programaComponente : programaAno.getProgramaComponentes()){
				CajaMonto cajaMonto = cajaDAO.getCajaMontoByServicioComunaProgramaComponenteSubtituloMes(idServicio, idComuna, idProgramaAno, programaComponente.getComponente().getId(), subtitulo.getId(), cajaMontoSummary.getIdMes());
				ProgramaSubtituloComponentePeso programaSubtituloComponentePeso = programasDAO.getProgramaSubtituloComponentePesoByProgramaComponenteSubtitulo(idProgramaAno, programaComponente.getComponente().getId(), subtitulo.getId());
				if(programaSubtituloComponentePeso != null){
					if(iteracionPorComponentes !=  componentesSize){
						int pesoComponente =  (int)programaSubtituloComponentePeso.getPeso();
						System.out.println("actualizando el caja monto="+pesoComponente);
						if((pesoComponente) == 100){
							cajaMonto.setMonto(cajaMontoSummary.getMontoMes().intValue());
							montoAcumulado += cajaMontoSummary.getMontoMes(); 
							cajaMonto.setReparos(true);
						}else{
							System.out.println("actualizando el caja monto");
							int montoActualizado = (int)((pesoComponente * cajaMontoSummary.getMontoMes())/100.0);
							cajaMonto.setMonto(montoActualizado);
							montoAcumulado += montoActualizado;
							cajaMonto.setReparos(true);
						}
					}else{
						cajaMonto.setMonto((int)(cajaMontoSummary.getMontoMes() - montoAcumulado));
						cajaMonto.setReparos(true);
					}
				}
				iteracionPorComponentes++;
			}
		}
	}

	public List<SubtituloFlujoCajaVO> getPercapitaByProgramaAno(Integer idPrograma, Integer ano, Integer idServicio) {
		DistribucionInicialPercapita  distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(ano);
		List<AntecendentesComunaCalculado> antecendentesComunasCalculado = antecedentesComunaDAO.findAntecedentesComunaCalculadosByDistribucionInicialPercapitaServicio(distribucionInicialPercapita.getIdDistribucionInicialPercapita(), idServicio);
		List<SubtituloFlujoCajaVO> resultado = new ArrayList<SubtituloFlujoCajaVO>();
		Map<Integer, Long> percapitaServicio = new HashMap<Integer, Long>();

		System.out.println("antecendentesComunasCalculado.size() --> "+antecendentesComunasCalculado.size());
		if(antecendentesComunasCalculado != null && antecendentesComunasCalculado.size()>0){
			for(AntecendentesComunaCalculado antecendentesComunaCalculado : antecendentesComunasCalculado){
				if(percapitaServicio.containsKey(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId())){
					Long valor = percapitaServicio.get(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId());
					valor+= antecendentesComunaCalculado.getPercapitaAno();
					percapitaServicio.put(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId(), valor);
				}else{
					percapitaServicio.put(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId(), antecendentesComunaCalculado.getPercapitaAno());
				}
			}
		}


		for (Integer key : percapitaServicio.keySet()) {
			Long valor = percapitaServicio.get(key); //ojo que en key se guarda el 
			SubtituloFlujoCajaVO subtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
			ServicioSalud servicioSalud = servicioSaludService.getServicioSaludPorID(key);
			if(servicioSalud != null) {
				subtituloFlujoCajaVO.setServicio(servicioSalud.getNombre());
				subtituloFlujoCajaVO.setIdServicio(servicioSalud.getId());
			}
			Long montoMensual = valor / 12;
			for(int mes=1; mes<=12 ; mes++){
				CajaMontoSummaryVO cajaMonto = new CajaMontoSummaryVO();
				cajaMonto.setIdMes(mes);
				cajaMonto.setMontoMes(montoMensual);
				switch (mes) {
				case 1:
					subtituloFlujoCajaVO.setCajaMontoEnero(cajaMonto);
					break;
				case 2:
					subtituloFlujoCajaVO.setCajaMontoFebrero(cajaMonto);
					break;
				case 3:
					subtituloFlujoCajaVO.setCajaMontoMarzo(cajaMonto);
					break;
				case 4:
					subtituloFlujoCajaVO.setCajaMontoAbril(cajaMonto);
					break;
				case 5:
					subtituloFlujoCajaVO.setCajaMontoMayo(cajaMonto);
					break;
				case 6:
					subtituloFlujoCajaVO.setCajaMontoJunio(cajaMonto);
					break;
				case 7:
					subtituloFlujoCajaVO.setCajaMontoJulio(cajaMonto);
					break;
				case 8:
					subtituloFlujoCajaVO.setCajaMontoAgosto(cajaMonto);
					break;
				case 9:
					subtituloFlujoCajaVO.setCajaMontoSeptiembre(cajaMonto);
					break;
				case 10:
					subtituloFlujoCajaVO.setCajaMontoOctubre(cajaMonto);
					break;
				case 11:
					subtituloFlujoCajaVO.setCajaMontoNoviembre(cajaMonto);
					break;
				case 12:
					subtituloFlujoCajaVO.setCajaMontoDiciembre(cajaMonto);
					break;
				default:
					break;
				}
			}		
			subtituloFlujoCajaVO.setMarcoPresupuestario(valor);
			resultado.add(subtituloFlujoCajaVO);
		} 
		return resultado;

	}
	public List<SubtituloFlujoCajaVO> getPercapitaByProgramaAno(Integer idPrograma, Integer ano) {
		DistribucionInicialPercapita  distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(ano);
		System.out.println("distribucionInicialPercapita.getIdDistribucionInicialPercapita() --> "+distribucionInicialPercapita.getIdDistribucionInicialPercapita());
		List<AntecendentesComunaCalculado> antecendentesComunasCalculado = antecedentesComunaDAO.findAntecedentesComunaCalculadosByDistribucionInicialPercapitaVigente(distribucionInicialPercapita.getIdDistribucionInicialPercapita());
		List<SubtituloFlujoCajaVO> resultado = new ArrayList<SubtituloFlujoCajaVO>();
		Map<Integer, Long> percapitaServicio = new HashMap<Integer, Long>();

		System.out.println("antecendentesComunasCalculado.size() --> "+antecendentesComunasCalculado.size());
		if(antecendentesComunasCalculado != null && antecendentesComunasCalculado.size()>0){
			for(AntecendentesComunaCalculado antecendentesComunaCalculado : antecendentesComunasCalculado){
				if(percapitaServicio.containsKey(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId())){
					Long valor = ((percapitaServicio.get(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId()) == null) ? 0 : percapitaServicio.get(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId()));
					valor+= ((antecendentesComunaCalculado.getPercapitaAno() == null) ? 0 : antecendentesComunaCalculado.getPercapitaAno());
					percapitaServicio.put(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId(), valor);
				}else{
					percapitaServicio.put(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId(), antecendentesComunaCalculado.getPercapitaAno());
				}
			}
		}


		for (Integer key : percapitaServicio.keySet()) {
			Long valor = percapitaServicio.get(key); //ojo que en key se guarda el 
			SubtituloFlujoCajaVO subtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
			ServicioSalud servicioSalud = servicioSaludService.getServicioSaludPorID(key);
			if(servicioSalud != null) {
				subtituloFlujoCajaVO.setServicio(servicioSalud.getNombre());
				subtituloFlujoCajaVO.setIdServicio(servicioSalud.getId());
			}
			List<CajaMontoSummaryVO> cajaMontos = new ArrayList<CajaMontoSummaryVO>();
			Long montoMensual = valor / 12;

			for(int mes=1; mes<=12 ; mes++){
				CajaMontoSummaryVO cajaMonto = new CajaMontoSummaryVO();
				cajaMonto.setIdMes(mes);
				cajaMonto.setMontoMes(montoMensual);
				switch (mes) {
				case 1:
					subtituloFlujoCajaVO.setCajaMontoEnero(cajaMonto);
					break;
				case 2:
					subtituloFlujoCajaVO.setCajaMontoFebrero(cajaMonto);
					break;
				case 3:
					subtituloFlujoCajaVO.setCajaMontoMarzo(cajaMonto);
					break;
				case 4:
					subtituloFlujoCajaVO.setCajaMontoAbril(cajaMonto);
					break;
				case 5:
					subtituloFlujoCajaVO.setCajaMontoMayo(cajaMonto);
					break;
				case 6:
					subtituloFlujoCajaVO.setCajaMontoJunio(cajaMonto);
					break;
				case 7:
					subtituloFlujoCajaVO.setCajaMontoJulio(cajaMonto);
					break;
				case 8:
					subtituloFlujoCajaVO.setCajaMontoAgosto(cajaMonto);
					break;
				case 9:
					subtituloFlujoCajaVO.setCajaMontoSeptiembre(cajaMonto);
					break;
				case 10:
					subtituloFlujoCajaVO.setCajaMontoOctubre(cajaMonto);
					break;
				case 11:
					subtituloFlujoCajaVO.setCajaMontoNoviembre(cajaMonto);
					break;
				case 12:
					subtituloFlujoCajaVO.setCajaMontoDiciembre(cajaMonto);
					break;
				default:
					break;
				}
			}		
			subtituloFlujoCajaVO.setMarcoPresupuestario(valor);
			resultado.add(subtituloFlujoCajaVO);

		} 

		return resultado;
	}

	public DocumentoEstimacionflujocaja getDocumentByTypProgramaAno(Integer idProgramaAno, TipoDocumentosProcesos tipoDocumentoProceso){
		return documentDAO.getDocumentByTypProgramaAno(idProgramaAno, tipoDocumentoProceso);

	}

	public List<ServiciosVO> getServicioByProgramaAnoSubtitulo(Integer valorComboPrograma, Subtitulo subtitulo) {
		List<ServiciosVO> servicios = new ArrayList<ServiciosVO>();
		List<Caja> cajas = cajaDAO.getCajaByProgramaAnoSubtitulo(valorComboPrograma, subtitulo);
		if(cajas != null && cajas.size() > 0){
			for(Caja caja : cajas){
				if(caja.getServicio() != null){
					ServiciosVO serviciosVO = new ServicioMapper().getBasic(caja.getServicio());
					servicios.add(serviciosVO);
				}
			}
		}
		return servicios;
	}

	public List<SubtituloFlujoCajaVO> getPercapitaByAno(Integer ano) {
		DistribucionInicialPercapita  distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(ano);
		System.out.println("distribucionInicialPercapita.getIdDistribucionInicialPercapita() --> " + distribucionInicialPercapita.getIdDistribucionInicialPercapita());
		List<AntecendentesComunaCalculado> antecendentesComunasCalculado = antecedentesComunaDAO.findAntecedentesComunaCalculadosByDistribucionInicialPercapitaVigente(distribucionInicialPercapita.getIdDistribucionInicialPercapita());

		List<SubtituloFlujoCajaVO> resultado = new ArrayList<SubtituloFlujoCajaVO>();
		Map<Integer, Long> percapitaServicio = new HashMap<Integer, Long>();
		System.out.println("antecendentesComunasCalculado.size() --> "+antecendentesComunasCalculado.size());
		if(antecendentesComunasCalculado != null && antecendentesComunasCalculado.size()>0){
			for(AntecendentesComunaCalculado antecendentesComunaCalculado : antecendentesComunasCalculado){
				if(percapitaServicio.containsKey(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId())){
					Long valor = ((percapitaServicio.get(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId()) == null) ? 0 : percapitaServicio.get(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId()));
					valor+= ((antecendentesComunaCalculado.getPercapitaMes() == null) ? 0 : antecendentesComunaCalculado.getPercapitaMes());
					percapitaServicio.put(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId(), valor);
				}else{
					percapitaServicio.put(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId(), antecendentesComunaCalculado.getPercapitaMes());
				}
			}
		}
		for (Integer key : percapitaServicio.keySet()) {
			Long montoMensual = percapitaServicio.get(key);
			SubtituloFlujoCajaVO subtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
			ServicioSalud servicioSalud = servicioSaludService.getServicioSaludPorID(key);
			if(servicioSalud != null) {
				subtituloFlujoCajaVO.setServicio(servicioSalud.getNombre());
				subtituloFlujoCajaVO.setIdServicio(servicioSalud.getId());
			}

			for(int mes=1; mes<=12 ; mes++){
				CajaMontoSummaryVO cajaMonto = new CajaMontoSummaryVO();
				cajaMonto.setIdMes(mes);
				cajaMonto.setMontoMes(montoMensual);
				switch (mes) {
				case 1:
					subtituloFlujoCajaVO.setCajaMontoEnero(cajaMonto);
					break;
				case 2:
					subtituloFlujoCajaVO.setCajaMontoFebrero(cajaMonto);
					break;
				case 3:
					subtituloFlujoCajaVO.setCajaMontoMarzo(cajaMonto);
					break;
				case 4:
					subtituloFlujoCajaVO.setCajaMontoAbril(cajaMonto);
					break;
				case 5:
					subtituloFlujoCajaVO.setCajaMontoMayo(cajaMonto);
					break;
				case 6:
					subtituloFlujoCajaVO.setCajaMontoJunio(cajaMonto);
					break;
				case 7:
					subtituloFlujoCajaVO.setCajaMontoJulio(cajaMonto);
					break;
				case 8:
					subtituloFlujoCajaVO.setCajaMontoAgosto(cajaMonto);
					break;
				case 9:
					subtituloFlujoCajaVO.setCajaMontoSeptiembre(cajaMonto);
					break;
				case 10:
					subtituloFlujoCajaVO.setCajaMontoOctubre(cajaMonto);
					break;
				case 11:
					subtituloFlujoCajaVO.setCajaMontoNoviembre(cajaMonto);
					break;
				case 12:
					subtituloFlujoCajaVO.setCajaMontoDiciembre(cajaMonto);
					break;
				default:
					break;
				}
			}		
			subtituloFlujoCajaVO.setMarcoPresupuestario(0L);
			resultado.add(subtituloFlujoCajaVO);
		} 
		return resultado;
	}

	public List<SubtituloFlujoCajaVO> getPercapitaByAnoServicio(Integer idServicio, Integer ano) {
		DistribucionInicialPercapita  distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(ano);
		System.out.println("distribucionInicialPercapita.getIdDistribucionInicialPercapita() --> "+distribucionInicialPercapita.getIdDistribucionInicialPercapita());
		List<AntecendentesComunaCalculado> antecendentesComunasCalculado = antecedentesComunaDAO.findAntecedentesComunaCalculadosByDistribucionInicialPercapitaVigenteServicio(distribucionInicialPercapita.getIdDistribucionInicialPercapita(),  idServicio);

		List<SubtituloFlujoCajaVO> resultado = new ArrayList<SubtituloFlujoCajaVO>();
		Map<Integer, Long> percapitaServicio = new HashMap<Integer, Long>();
		System.out.println("antecendentesComunasCalculado.size() --> "+antecendentesComunasCalculado.size());
		if(antecendentesComunasCalculado != null && antecendentesComunasCalculado.size() > 0){
			for(AntecendentesComunaCalculado antecendentesComunaCalculado : antecendentesComunasCalculado){
				if(percapitaServicio.containsKey(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId())){
					Long valor = ((percapitaServicio.get(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId()) == null) ? 0 : percapitaServicio.get(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId()));
					valor+= ((antecendentesComunaCalculado.getPercapitaMes() == null) ? 0 : antecendentesComunaCalculado.getPercapitaMes());
					percapitaServicio.put(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId(), valor);
				}else{
					percapitaServicio.put(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId(), antecendentesComunaCalculado.getPercapitaMes());
				}
			}
		}else{
			percapitaServicio.put(idServicio, 0L);
		}
		for (Integer key : percapitaServicio.keySet()) {
			Long montoMensual = percapitaServicio.get(key);
			SubtituloFlujoCajaVO subtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
			ServicioSalud servicioSalud = servicioSaludService.getServicioSaludPorID(key);
			if(servicioSalud != null) {
				subtituloFlujoCajaVO.setServicio(servicioSalud.getNombre());
				subtituloFlujoCajaVO.setIdServicio(servicioSalud.getId());
			}
			for(int mes=1; mes<=12 ; mes++){
				CajaMontoSummaryVO cajaMonto = new CajaMontoSummaryVO();
				cajaMonto.setIdMes(mes);
				cajaMonto.setMontoMes(montoMensual);
				switch (mes) {
				case 1:
					subtituloFlujoCajaVO.setCajaMontoEnero(cajaMonto);
					break;
				case 2:
					subtituloFlujoCajaVO.setCajaMontoFebrero(cajaMonto);
					break;
				case 3:
					subtituloFlujoCajaVO.setCajaMontoMarzo(cajaMonto);
					break;
				case 4:
					subtituloFlujoCajaVO.setCajaMontoAbril(cajaMonto);
					break;
				case 5:
					subtituloFlujoCajaVO.setCajaMontoMayo(cajaMonto);
					break;
				case 6:
					subtituloFlujoCajaVO.setCajaMontoJunio(cajaMonto);
					break;
				case 7:
					subtituloFlujoCajaVO.setCajaMontoJulio(cajaMonto);
					break;
				case 8:
					subtituloFlujoCajaVO.setCajaMontoAgosto(cajaMonto);
					break;
				case 9:
					subtituloFlujoCajaVO.setCajaMontoSeptiembre(cajaMonto);
					break;
				case 10:
					subtituloFlujoCajaVO.setCajaMontoOctubre(cajaMonto);
					break;
				case 11:
					subtituloFlujoCajaVO.setCajaMontoNoviembre(cajaMonto);
					break;
				case 12:
					subtituloFlujoCajaVO.setCajaMontoDiciembre(cajaMonto);
					break;
				default:
					break;
				}
			}			 
			subtituloFlujoCajaVO.setMarcoPresupuestario(0L);
			resultado.add(subtituloFlujoCajaVO);

		} 
		return resultado;
	}

	public boolean tieneMarcosConReparos(Integer idPrograma, Integer ano) {
		ProgramaAno programaAno = programasDAO.getProgramaAnoByIdProgramaAndAno(idPrograma, ano);
		List<CajaMonto> cajaMontoReparos = cajaDAO.getCajaMontoReparosByProgramaAno(programaAno.getIdProgramaAno());
		boolean tieneMarcosConReparos = false;
		if(cajaMontoReparos != null && cajaMontoReparos.size() > 0){
			tieneMarcosConReparos = true;
		}
		System.out.println("tieneMarcosConReparos->"+tieneMarcosConReparos);
		return tieneMarcosConReparos;
	}

	public boolean existeDistribucionRecursos(Integer idPrograma, Integer ano) {
		System.out.println("existeDistribucionRecursos Inicio idPrograma-->"+idPrograma);
		System.out.println("existeDistribucionRecursos Inicio ano-->"+ano);
		ProgramaVO programa = programasService.getProgramaByIdProgramaAndAno(idPrograma, ano);
		if(programa == null){
			return false;
		}
		if(!programa.getEstado().getId().equals(EstadosProgramas.FINALIZADO.getId())){
			return false;
		}
		return true;
	}

	public int countAntecendentesComunaCalculadoVigente(Integer ano) {
		return antecedentesComunaDAO.countAntecendentesComunaCalculadoVigente(ano);
	}

	public ReferenciaDocumentoSummaryVO getDocumentEstimacionFlujoCajaByType(Integer idPrograma, Integer ano, TipoDocumentosProcesos tipoDocumento) {
		System.out.println("idPrograma->"+idPrograma);
		System.out.println("ano->"+ano);
		System.out.println("tipoDocumento->"+tipoDocumento.getId());
		ProgramaAno programaAno = programasDAO.getProgramaAnoByIdProgramaAndAno(idPrograma, ano);
		ReferenciaDocumento referencia = fileDAO.getLastDocumentByTypeEstimacionFlujoCaja(programaAno.getIdProgramaAno(), tipoDocumento);
		return new ReferenciaDocumentoMapper().getSummary(referencia);
	}


	public SubtituloFlujoCajaVO getMonitoreoComunaByProgramaAnoServicioSubtituloMes(Integer idProgramaAno, Integer idServicio, Integer idComuna, Subtitulo subtitulo, Integer idMes, Boolean iniciarFlujoCaja) {
		LOGGER.info("Inicio getMonitoreoComunaByProgramaAnoServicioSubtituloMes");
		SubtituloFlujoCajaVO subtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
		ServicioSalud servicioSalud = servicioSaludDAO.getById(idServicio);
		Comuna comuna = comunaDAO.getComunaById(idComuna);
		subtituloFlujoCajaVO.setIdServicio(servicioSalud.getId());
		subtituloFlujoCajaVO.setServicio(servicioSalud.getNombre());
		subtituloFlujoCajaVO.setIdComuna(comuna.getId());
		subtituloFlujoCajaVO.setComuna(comuna.getNombre());
		subtituloFlujoCajaVO.setMarcoPresupuestario(0L);
		ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idProgramaAno);
		List<Componente> componentes = componenteDAO.getComponentesByIdProgramaAnoIdSubtitulos(idProgramaAno, subtitulo);
		Long marcoPorComuna = 0L;
		for(int mes = 1; mes <= 12; mes++){
			Mes mesDTO = mesDAO.getMesPorID(mes);
			CajaMontoSummaryVO cajaMontoMes = new CajaMontoSummaryVO(null, mes, mesDTO.getNombre());
			Long montoComponentes = 0L;
			for(Componente componente : componentes){
				CajaMonto cajaMonto = cajaDAO.getCajaMontoByServicioComunaProgramaComponenteSubtituloMes(idServicio, idComuna, idProgramaAno, componente.getId(), subtitulo.getId(), mes);
				montoComponentes += ((cajaMonto != null) ? cajaMonto.getMonto() : 0L);
			}
			cajaMontoMes.setMontoMes(montoComponentes);
			cajaMontoMes.setMontoMesOriginal(montoComponentes);
			switch (mes) {
			case 1:
				subtituloFlujoCajaVO.setCajaMontoEnero(cajaMontoMes);
				break;
			case 2:
				subtituloFlujoCajaVO.setCajaMontoFebrero(cajaMontoMes);
				break;
			case 3:
				subtituloFlujoCajaVO.setCajaMontoMarzo(cajaMontoMes);
				break;
			case 4:
				subtituloFlujoCajaVO.setCajaMontoAbril(cajaMontoMes);
				break;
			case 5:
				subtituloFlujoCajaVO.setCajaMontoMayo(cajaMontoMes);
				break;
			case 6:
				subtituloFlujoCajaVO.setCajaMontoJunio(cajaMontoMes);
				break;
			case 7:
				subtituloFlujoCajaVO.setCajaMontoJulio(cajaMontoMes);
				break;
			case 8:
				subtituloFlujoCajaVO.setCajaMontoAgosto(cajaMontoMes);
				break;
			case 9:
				subtituloFlujoCajaVO.setCajaMontoSeptiembre(cajaMontoMes);
				break;
			case 10:
				subtituloFlujoCajaVO.setCajaMontoOctubre(cajaMontoMes);
				break;
			case 11:
				subtituloFlujoCajaVO.setCajaMontoNoviembre(cajaMontoMes);
				break;
			case 12:
				subtituloFlujoCajaVO.setCajaMontoDiciembre(cajaMontoMes);
				break;
			default:
				break;
			}
		}
		for(Componente componente : componentes){
			marcoPorComuna += programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAno, componente.getId(), subtitulo.getId());
		}
		subtituloFlujoCajaVO.setMarcoPresupuestario(marcoPorComuna);
		if(!iniciarFlujoCaja){
			Integer mesActual = Integer.parseInt(getMesCurso(true));
			Calendar calendar = Calendar.getInstance();
			Integer diaDelMes = calendar.get(Calendar.DAY_OF_MONTH);
			ConveniosSummaryVO convenioRecibido = new ConveniosSummaryVO(0.0, 0);
			TransferenciaSummaryVO transferenciasAcumulada = new TransferenciaSummaryVO(0.0, 0L);
			Long totalConveniosPorComuna = 0L;
			Long totalRemesasPorComuna = 0L;
			for(Componente componente : componentes){
				ConvenioComunaComponenteVO convenioComunaComponenteVO = conveniosService.getConvenioComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getId(), comuna.getId(), EstadosConvenios.INGRESADO);
				if(convenioComunaComponenteVO == null){
					convenioComunaComponenteVO = conveniosService.getConvenioComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getId(), comuna.getId(), EstadosConvenios.APROBADO);
				}
				if(convenioComunaComponenteVO == null){
					convenioComunaComponenteVO = conveniosService.getConvenioComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getId(), comuna.getId(), EstadosConvenios.TRAMITE);
				}
				if(convenioComunaComponenteVO == null){
					convenioComunaComponenteVO = conveniosService.getConvenioComunaComponenteByProgramaAnoComponenteSubtituloComunaEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getId(), comuna.getId(), EstadosConvenios.PAGADO);
				}
				if(convenioComunaComponenteVO != null){
					totalConveniosPorComuna += ((convenioComunaComponenteVO.getMonto() != null) ? convenioComunaComponenteVO.getMonto() : 0L);
				}
				totalRemesasPorComuna += remesasDAO.getRemesasPagadasComunaProgramaComponenteSubtituloDiaMes(idProgramaAno, componente.getId(), subtitulo.getId(), comuna.getId(), diaDelMes, mesActual);
				System.out.println("totalRemesasPorComuna --> "+ totalRemesasPorComuna);
			}
			convenioRecibido.setMonto(totalConveniosPorComuna.intValue());
			if(marcoPorComuna != 0){
				Double porcentaje = ((totalConveniosPorComuna * 100.0) / marcoPorComuna);
				porcentaje = porcentaje / 100.0;
				convenioRecibido.setPorcentaje(porcentaje);
			}
			subtituloFlujoCajaVO.setConvenioRecibido(convenioRecibido);
			
			transferenciasAcumulada.setMonto(totalRemesasPorComuna);
			if(marcoPorComuna != 0){
				Double porcentajeRemesas = ((totalRemesasPorComuna * 100.0) / marcoPorComuna);
				porcentajeRemesas = porcentajeRemesas / 100.0;
				transferenciasAcumulada.setPorcentaje(porcentajeRemesas);
			}
			subtituloFlujoCajaVO.setTransferenciaAcumulada(transferenciasAcumulada);
		}
		System.out.println("getMonitoreoServicioByProgramaAnoServicioSubtituloMes-->subtituloFlujoCajaVO=" + subtituloFlujoCajaVO);
		LOGGER.info("Fin getMonitoreoServicioByProgramaAnoServicioSubtituloMes");
		return subtituloFlujoCajaVO;
	}


	public SubtituloFlujoCajaVO getMonitoreoServicioByProgramaAnoServicioSubtituloMes(Integer idProgramaAno, Integer idServicio, Integer idEstablecimiento, Subtitulo subtitulo, Integer idMes, Boolean iniciarFlujoCaja) {
		LOGGER.info("Inicio getMonitoreoServicioByProgramaAnoServicioSubtituloMes");
		SubtituloFlujoCajaVO subtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
		ServicioSalud servicioSalud = servicioSaludDAO.getById(idServicio);
		Establecimiento establecimiento = establecimientosDAO.getEstablecimientoById(idEstablecimiento);
		subtituloFlujoCajaVO.setIdServicio(servicioSalud.getId());
		subtituloFlujoCajaVO.setServicio(servicioSalud.getNombre());
		subtituloFlujoCajaVO.setIdEstablecimiento(establecimiento.getId());
		subtituloFlujoCajaVO.setEstablecimiento(establecimiento.getNombre());
		subtituloFlujoCajaVO.setMarcoPresupuestario(0L);
		ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idProgramaAno);
		List<Componente> componentes = componenteDAO.getComponentesByIdProgramaAnoIdSubtitulos(idProgramaAno, subtitulo);
		Long marcoPorEstablecimiento = 0L;
		for(int mes = 1; mes <= 12; mes++){
			Mes mesDTO = mesDAO.getMesPorID(mes);
			CajaMontoSummaryVO cajaMontoMes = new CajaMontoSummaryVO(null, mes, mesDTO.getNombre());
			Long montoComponentes = 0L;
			for(Componente componente : componentes){
				CajaMonto cajaMonto = cajaDAO.getCajaMontoByServicioEstablecimientoProgramaComponenteSubtituloMes(idServicio, idEstablecimiento, idProgramaAno, componente.getId(), subtitulo.getId(), mes);
				montoComponentes += ((cajaMonto != null) ? cajaMonto.getMonto() : 0L);
			}
			cajaMontoMes.setMontoMes(montoComponentes);
			cajaMontoMes.setMontoMesOriginal(montoComponentes);
			switch (mes) {
			case 1:
				subtituloFlujoCajaVO.setCajaMontoEnero(cajaMontoMes);
				break;
			case 2:
				subtituloFlujoCajaVO.setCajaMontoFebrero(cajaMontoMes);
				break;
			case 3:
				subtituloFlujoCajaVO.setCajaMontoMarzo(cajaMontoMes);
				break;
			case 4:
				subtituloFlujoCajaVO.setCajaMontoAbril(cajaMontoMes);
				break;
			case 5:
				subtituloFlujoCajaVO.setCajaMontoMayo(cajaMontoMes);
				break;
			case 6:
				subtituloFlujoCajaVO.setCajaMontoJunio(cajaMontoMes);
				break;
			case 7:
				subtituloFlujoCajaVO.setCajaMontoJulio(cajaMontoMes);
				break;
			case 8:
				subtituloFlujoCajaVO.setCajaMontoAgosto(cajaMontoMes);
				break;
			case 9:
				subtituloFlujoCajaVO.setCajaMontoSeptiembre(cajaMontoMes);
				break;
			case 10:
				subtituloFlujoCajaVO.setCajaMontoOctubre(cajaMontoMes);
				break;
			case 11:
				subtituloFlujoCajaVO.setCajaMontoNoviembre(cajaMontoMes);
				break;
			case 12:
				subtituloFlujoCajaVO.setCajaMontoDiciembre(cajaMontoMes);
				break;
			default:
				break;
			}
		}
		for(Componente componente : componentes){
			marcoPorEstablecimiento += programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAno, componente.getId(), subtitulo.getId());
		}
		subtituloFlujoCajaVO.setMarcoPresupuestario(marcoPorEstablecimiento);

		if(!iniciarFlujoCaja){
			Integer mesActual = Integer.parseInt(getMesCurso(true));
			Calendar calendar = Calendar.getInstance();
			Integer diaDelMes = calendar.get(Calendar.DAY_OF_MONTH);
			ConveniosSummaryVO convenioRecibido = new ConveniosSummaryVO(0.0, 0);
			TransferenciaSummaryVO transferenciasAcumulada = new TransferenciaSummaryVO(0.0, 0L);
			Long totalConveniosPorEstablecimiento = 0L;
			Long totalRemesasPorEstablecimiento = 0L;
			for(Componente componente : componentes){
				ConvenioServicioComponenteVO convenioServicioComponente = conveniosService.getConvenioServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getId(), establecimiento.getId(), EstadosConvenios.INGRESADO);
				if(convenioServicioComponente == null){
					convenioServicioComponente = conveniosService.getConvenioServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getId(), establecimiento.getId(), EstadosConvenios.APROBADO);
				}
				if(convenioServicioComponente == null){
					convenioServicioComponente = conveniosService.getConvenioServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getId(), establecimiento.getId(), EstadosConvenios.TRAMITE);
				}
				if(convenioServicioComponente == null){
					convenioServicioComponente = conveniosService.getConvenioServicioComponenteByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(idProgramaAno, componente.getId(), subtitulo.getId(), establecimiento.getId(), EstadosConvenios.PAGADO);
				}
				if(convenioServicioComponente != null){
					totalConveniosPorEstablecimiento += ((convenioServicioComponente.getMonto() != null) ? convenioServicioComponente.getMonto() : 0L);
				}
				totalRemesasPorEstablecimiento += remesasDAO.getRemesasPagadasEstablecimientoProgramaComponenteSubtituloDiaMes(idProgramaAno, componente.getId(), subtitulo.getId(), establecimiento.getId(), diaDelMes, mesActual);
				System.out.println("totalRemesasPorEstablecimiento --> "+ totalRemesasPorEstablecimiento);
			}
			convenioRecibido.setMonto(totalConveniosPorEstablecimiento.intValue());
			if(marcoPorEstablecimiento != 0){
				Double porcentaje = ((totalConveniosPorEstablecimiento * 100.0) / marcoPorEstablecimiento);
				porcentaje = porcentaje / 100.0;
				convenioRecibido.setPorcentaje(porcentaje);
			}
			subtituloFlujoCajaVO.setConvenioRecibido(convenioRecibido);
			
			transferenciasAcumulada.setMonto(totalRemesasPorEstablecimiento);
			if(marcoPorEstablecimiento != 0){
				Double porcentajeRemesas = ((totalRemesasPorEstablecimiento * 100.0) / marcoPorEstablecimiento);
				porcentajeRemesas = porcentajeRemesas / 100.0;
				transferenciasAcumulada.setPorcentaje(porcentajeRemesas);
			}
			subtituloFlujoCajaVO.setTransferenciaAcumulada(transferenciasAcumulada);
		}
		System.out.println("getMonitoreoServicioByProgramaAnoServicioSubtituloMes-->subtituloFlujoCajaVO=" + subtituloFlujoCajaVO);
		LOGGER.info("Fin getMonitoreoServicioByProgramaAnoServicioSubtituloMes");
		return subtituloFlujoCajaVO;
	}

	public Integer crearIntanciaConsolidador(String username, Integer ano) {
		System.out.println("username-->"+username);
		Usuario usuario = this.usuarioDAO.getUserByUsername(username);
		AnoEnCurso anoEnCurso = anoDAO.getAnoById(ano);
		Mes mes = mesDAO.getMesPorID(Integer.parseInt(getMesCurso(true)));
		return estimacionFlujoCajaDAO.crearIntanciaConsolidador(usuario, mes, anoEnCurso);
	}

	public void elaborarOrdinarioProgramacionPlanilla(Integer idProceso, Integer ano) {
		elaborarOrdinarioProgramacion(idProceso, ano);
		elaborarPlanillaProgramacion(idProceso, ano);
	}

	private void elaborarOrdinarioProgramacion(Integer idProceso, Integer ano){
		Integer plantillaIdOrdinario = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLAORDINARIOPROGRAMACIONCAJA);
		if(plantillaIdOrdinario == null){
			throw new RuntimeException("No se puede crear Ordinario Programación Caja, la plantilla no esta cargada");
		}
		try {
			ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryOrdinarioVO = documentService.getDocumentByPlantillaId(plantillaIdOrdinario);
			System.out.println("\n\n\n\n\n\n antes de documentoOrdinarioVO ---> referenciaDocumentoSummaryOrdinarioVO ---> "+referenciaDocumentoSummaryOrdinarioVO);
			DocumentoVO documentoOrdinarioVO = documentService.getDocument(referenciaDocumentoSummaryOrdinarioVO.getId());
			System.out.println("\n\n\n\n\n\n despues de documentoOrdinarioVO");
			String templateOrdinario = tmpDirDoc + File.separator + documentoOrdinarioVO.getName();
			templateOrdinario = templateOrdinario.replace(" ", "");
			System.out.println("templateOrdinario template-->"+templateOrdinario);
			GeneradorWord generadorWordPlantillaOrdinario = new GeneradorWord(templateOrdinario);
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			generadorWordPlantillaOrdinario.saveContent(documentoOrdinarioVO.getContent(), XWPFDocument.class);
			Map<String, Object> parametersOrdinario = new HashMap<String, Object>();
			parametersOrdinario.put("{ano}", ano);
			parametersOrdinario.put("{mes}", getMesCurso(false));
			String filenameOrdinario = tmpDirDoc + File.separator + new Date().getTime() + "_" + "OrdinarioFlujaCaja" + getMesCurso(false) + ".docx";
			System.out.println("filenameOrdinario filename-->"+filenameOrdinario);
			String contentTypeOrdinario = mimemap.getContentType(filenameOrdinario.toLowerCase());
			System.out.println("contentTypeOrdinario->"+contentTypeOrdinario);
			filenameOrdinario = filenameOrdinario.replaceAll(" ", "");
			GeneradorResolucionAporteEstatal generadorWordOrdinario = new GeneradorResolucionAporteEstatal(filenameOrdinario, templateOrdinario);
			generadorWordOrdinario.replaceValues(parametersOrdinario, XWPFDocument.class);
			BodyVO responseOrdinario = alfrescoService.uploadDocument(new File(filenameOrdinario), contentTypeOrdinario, folderEstimacionFlujoCaja.replace("{ANO}", ano.toString()));
			System.out.println("response responseOrdinario --->"+responseOrdinario);
			FlujoCajaConsolidador flujoCajaConsolidador = estimacionFlujoCajaDAO.findFlujoCajaConsolidadorById(idProceso);
			plantillaIdOrdinario = documentService.createDocumentFlujoCaja(flujoCajaConsolidador, TipoDocumentosProcesos.ORDINARIOPROGRAMACIONCAJA, responseOrdinario.getNodeRef(), responseOrdinario.getFileName(), contentTypeOrdinario);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (Docx4JException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void elaborarPlanillaProgramacion(Integer idProceso, Integer ano) {
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator + "planillaMonitoreoConsolidador" + getMesCurso(false) + ".xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());
		GeneradorExcel generadorExcel = new GeneradorExcel(filename);
		List<ResumenFONASAMunicipalVO> resumenFONASAMunicipal24VO = cargarFonasaMunicipal(ano);
		ExcelTemplate excelSheet = null;
		Integer posicionColumnaInicial = 0;
		List<Long> totales = new ArrayList<Long>();


		if(resumenFONASAMunicipal24VO != null && resumenFONASAMunicipal24VO.size() > 0){
			for(ResumenFONASAMunicipalVO resumenFONASAMunicipalVO : resumenFONASAMunicipal24VO){
				totales.add(resumenFONASAMunicipalVO.getTotal());
			}
			Integer countCols = 0;
			List<CellExcelVO> header24 = new ArrayList<CellExcelVO>();
			List<CellExcelVO> subHeader24 = new ArrayList<CellExcelVO>();
			header24.add(new CellExcelVO("COD SS", 1, 2));
			countCols += 1; 
			header24.add(new CellExcelVO("SERVICIOS DE SALUD", 1, 2));
			countCols += 1;
			ResumenFONASAMunicipalVO resumenFONASAMunicipalVO = resumenFONASAMunicipal24VO.get(0);
			int numeroProgramas = ((resumenFONASAMunicipalVO.getProgramasFonasa() == null ) ? 0 : resumenFONASAMunicipalVO.getProgramasFonasa().size());
			header24.add(new CellExcelVO("SUBTÍTULO 24", (numeroProgramas + 8) , 1));
			countCols += (numeroProgramas + 8);
			subHeader24.add(new CellExcelVO("PER CAPITA BASAL", 1,1));
			subHeader24.add(new CellExcelVO("ADDF", 1,1));
			subHeader24.add(new CellExcelVO("DESCTO. RETIRO LEYES 20.157 Y 20.589", 1,1));
			subHeader24.add(new CellExcelVO("REBAJA INCUMPL. IAAPS", 1,1));
			subHeader24.add(new CellExcelVO("TOTAL PER CÁPITA (M$)", 1,1));
			subHeader24.add(new CellExcelVO("LEYES 19.813, 20.157 y 20.250", 1,1));
			if(resumenFONASAMunicipalVO.getProgramasFonasa() != null && resumenFONASAMunicipalVO.getProgramasFonasa().size() > 0){
				for(ProgramaFonasaVO programaFonasaVO : resumenFONASAMunicipalVO.getProgramasFonasa()){
					subHeader24.add(new CellExcelVO(programaFonasaVO.getNombrePrograma(), 1,1));
				}
			}
			subHeader24.add(new CellExcelVO("OTROS REF. MUN.", 1,1));
			subHeader24.add(new CellExcelVO("TOTAL REF. MUNICIPAL (M$)", 1,1));
			excelSheet = new EstimacionFlujoCajaMonitoreoConsolidadorSheetExcel(header24, subHeader24, resumenFONASAMunicipal24VO);
			((EstimacionFlujoCajaMonitoreoConsolidadorSheetExcel)excelSheet).setPosicionColumnaInicial(posicionColumnaInicial);
			generadorExcel.addSheet(excelSheet, getMesCurso(false));
			posicionColumnaInicial += countCols;
		}
		List<ResumenFONASAServicioVO> resumenFONASAServicio21VO = cargarFonasaServicio(Subtitulo.SUBTITULO21, ano) ;
		if(resumenFONASAServicio21VO != null && resumenFONASAServicio21VO.size() > 0){
			Integer contadorElementos = 0;
			for(ResumenFONASAServicioVO resumenFONASAServicio : resumenFONASAServicio21VO){
				totales.set(contadorElementos, (totales.get(contadorElementos) + resumenFONASAServicio.getTotal()));
			}
			Integer countCols = 0;
			List<CellExcelVO> header21 = new ArrayList<CellExcelVO>();
			List<CellExcelVO> subHeader21 = new ArrayList<CellExcelVO>();
			ResumenFONASAServicioVO resumenFONASAServicioVO = resumenFONASAServicio21VO.get(0);
			int numeroProgramas = ((resumenFONASAServicioVO.getProgramasFonasa() == null ) ? 0 : resumenFONASAServicioVO.getProgramasFonasa().size());
			header21.add(new CellExcelVO("SUBTÍTULO 21", (numeroProgramas + 2), 1));
			countCols += (numeroProgramas + 2); 
			if(resumenFONASAServicioVO.getProgramasFonasa() != null && resumenFONASAServicioVO.getProgramasFonasa().size() > 0){
				for(ProgramaFonasaVO programaFonasaVO : resumenFONASAServicioVO.getProgramasFonasa()){
					subHeader21.add(new CellExcelVO(programaFonasaVO.getNombrePrograma(), 1,1));
				}
			}
			subHeader21.add(new CellExcelVO("OTROS REF. SS 21", 1,1));
			subHeader21.add(new CellExcelVO("TOTAL REF. SERVICIOS SUBT. 21", 1,1));
			excelSheet = new EstimacionFlujoCajaMonitoreoConsolidadorSheetExcel(header21, subHeader21, null);
			((EstimacionFlujoCajaMonitoreoConsolidadorSheetExcel)excelSheet).setItemSub21(resumenFONASAServicio21VO);
			((EstimacionFlujoCajaMonitoreoConsolidadorSheetExcel)excelSheet).setPosicionColumnaInicial(posicionColumnaInicial);
			generadorExcel.addSheet(excelSheet, getMesCurso(false));
			posicionColumnaInicial += countCols;
		}
		List<ResumenFONASAServicioVO> resumenFONASAServicio22VO = cargarFonasaServicio(Subtitulo.SUBTITULO22, ano);
		if(resumenFONASAServicio22VO != null && resumenFONASAServicio22VO.size() > 0){
			Integer contadorElementos = 0;
			for(ResumenFONASAServicioVO resumenFONASAServicio : resumenFONASAServicio22VO){
				totales.set(contadorElementos, (totales.get(contadorElementos) + resumenFONASAServicio.getTotal()));
			}
			Integer countCols = 0;
			List<CellExcelVO> header22 = new ArrayList<CellExcelVO>();
			List<CellExcelVO> subHeader22 = new ArrayList<CellExcelVO>();
			ResumenFONASAServicioVO resumenFONASAServicioVO = resumenFONASAServicio22VO.get(0);
			int numeroProgramas = ((resumenFONASAServicioVO.getProgramasFonasa() == null ) ? 0 : resumenFONASAServicioVO.getProgramasFonasa().size());
			header22.add(new CellExcelVO("SUBTÍTULO 22", (numeroProgramas + 2), 1));
			countCols += (numeroProgramas + 2);
			if(resumenFONASAServicioVO.getProgramasFonasa() != null && resumenFONASAServicioVO.getProgramasFonasa().size() > 0){
				for(ProgramaFonasaVO programaFonasaVO : resumenFONASAServicioVO.getProgramasFonasa()){
					subHeader22.add(new CellExcelVO(programaFonasaVO.getNombrePrograma(), 1,1));
				}
			}
			subHeader22.add(new CellExcelVO("OTROS REF. SS 22", 1,1));
			subHeader22.add(new CellExcelVO("TOTAL REF. SERVICIOS SUBT. 22", 1,1));
			excelSheet = new EstimacionFlujoCajaMonitoreoConsolidadorSheetExcel(header22, subHeader22, null);
			((EstimacionFlujoCajaMonitoreoConsolidadorSheetExcel)excelSheet).setItemSub22(resumenFONASAServicio22VO);
			((EstimacionFlujoCajaMonitoreoConsolidadorSheetExcel)excelSheet).setPosicionColumnaInicial(posicionColumnaInicial);
			generadorExcel.addSheet(excelSheet, getMesCurso(false));
			posicionColumnaInicial += countCols;
		}
		List<ResumenFONASAServicioVO> resumenFONASAServicio29VO = cargarFonasaServicio(Subtitulo.SUBTITULO29, ano);
		if(resumenFONASAServicio29VO != null && resumenFONASAServicio29VO.size() > 0){
			Integer contadorElementos = 0;
			for(ResumenFONASAServicioVO resumenFONASAServicio : resumenFONASAServicio29VO){
				totales.set(contadorElementos, (totales.get(contadorElementos) + resumenFONASAServicio.getTotal()));
			}
			Integer countCols = 0;
			List<CellExcelVO> header29 = new ArrayList<CellExcelVO>();
			List<CellExcelVO> subHeader29 = new ArrayList<CellExcelVO>();
			ResumenFONASAServicioVO resumenFONASAServicioVO = resumenFONASAServicio29VO.get(0);
			int numeroProgramas = ((resumenFONASAServicioVO.getProgramasFonasa() == null ) ? 0 : resumenFONASAServicioVO.getProgramasFonasa().size());
			header29.add(new CellExcelVO("SUBTÍTULO 29", (numeroProgramas + 2), 1));
			countCols += (numeroProgramas + 2);
			if(resumenFONASAServicioVO.getProgramasFonasa() != null && resumenFONASAServicioVO.getProgramasFonasa().size() > 0){
				for(ProgramaFonasaVO programaFonasaVO : resumenFONASAServicioVO.getProgramasFonasa()){
					subHeader29.add(new CellExcelVO(programaFonasaVO.getNombrePrograma(), 1,1));
				}
			}
			subHeader29.add(new CellExcelVO("OTROS REF. SS 29", 1,1));
			subHeader29.add(new CellExcelVO("TOTAL REF. SERVICIOS SUBT. 29", 1,1));
			excelSheet = new EstimacionFlujoCajaMonitoreoConsolidadorSheetExcel(header29, subHeader29, null);
			((EstimacionFlujoCajaMonitoreoConsolidadorSheetExcel)excelSheet).setItemSub29(resumenFONASAServicio29VO);
			((EstimacionFlujoCajaMonitoreoConsolidadorSheetExcel)excelSheet).setPosicionColumnaInicial(posicionColumnaInicial);
			generadorExcel.addSheet(excelSheet, getMesCurso(false));
			posicionColumnaInicial += countCols;
		}

		if(totales.size() > 0){
			excelSheet = new EstimacionFlujoCajaMonitoreoConsolidadorSheetExcel(null, null, null);
			((EstimacionFlujoCajaMonitoreoConsolidadorSheetExcel)excelSheet).setPosicionColumnaInicial(posicionColumnaInicial);
			((EstimacionFlujoCajaMonitoreoConsolidadorSheetExcel)excelSheet).setAno(ano);
			((EstimacionFlujoCajaMonitoreoConsolidadorSheetExcel)excelSheet).setMes(getMesCurso(false));
			((EstimacionFlujoCajaMonitoreoConsolidadorSheetExcel)excelSheet).setTotales(totales);
			generadorExcel.addSheet(excelSheet, getMesCurso(false));
		}

		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderEstimacionFlujoCaja.replace("{ANO}", ano.toString()));
			System.out.println("response planillaProgramacionEstimacionFlujoCajaConsolidador --->"	+ response);
			TipoDocumento tipoDocumento = new TipoDocumento(TipoDocumentosProcesos.PLANILLAPROGRAMACIONCAJA.getId());
			FlujoCajaConsolidador flujoCajaConsolidador = estimacionFlujoCajaDAO.findFlujoCajaConsolidadorById(idProceso);
			documentService.createDocumentMonitoreoConsolidador(flujoCajaConsolidador, tipoDocumento, response.getNodeRef(), response.getFileName(), contenType, ano, Integer.parseInt(getMesCurso(true)));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void administrarVersionesFinalesConsolidador(Integer idProceso, Integer ano) {
		System.out.println("EstimacionFlujoCajaService administrarVersionesFinalesConsolidador eliminar todas las versiones que no sean finales");
		List<DocumentoEstimacionFlujoCajaConsolidador> documentosOrdinario = estimacionFlujoCajaDAO.getByIdEstimacionFlujoCajaConsolidadorTipoNotFinal(idProceso, TipoDocumentosProcesos.ORDINARIOPROGRAMACIONCAJA);
		if(documentosOrdinario != null && documentosOrdinario.size() > 0){
			for(DocumentoEstimacionFlujoCajaConsolidador documentoEstimacionFlujoCajaConsolidador : documentosOrdinario){
				String key = ((documentoEstimacionFlujoCajaConsolidador.getDocumento().getNodeRef() == null) ? documentoEstimacionFlujoCajaConsolidador.getDocumento().getPath() : documentoEstimacionFlujoCajaConsolidador.getDocumento().getNodeRef().replace("workspace://SpacesStore/", ""));
				System.out.println("key->"+key);
				alfrescoService.delete(key);
				estimacionFlujoCajaDAO.deleteDocumentoOrdinario(documentoEstimacionFlujoCajaConsolidador.getIdDocumentoEstimacionFlujoCajaConsolidador());
				estimacionFlujoCajaDAO.deleteDocumento(documentoEstimacionFlujoCajaConsolidador.getDocumento().getId());
			}
		}
	}

	public void enviarOrdinarioFonasaConsolidador(Integer idProceso, Integer ano) {
		Integer idPlantillaCorreo = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLACORREOORDINARIOPLANILLA);
		if(idPlantillaCorreo == null){
			throw new RuntimeException("No se puede crear plantilla correo ORDINARIO PLANILLA, la plantilla no esta cargada");
		}
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryPlantillaCorreoVO = documentService.getDocumentByPlantillaId(idPlantillaCorreo);
		DocumentoVO documentoPlantillaCorreo = documentService.getDocument(referenciaDocumentoSummaryPlantillaCorreoVO.getId());
		String templatePlantillaCorreo = tmpDirDoc + File.separator + documentoPlantillaCorreo.getName();
		templatePlantillaCorreo = templatePlantillaCorreo.replace(" ", "");
		System.out.println("templatePlantillaCorreo template-->"+templatePlantillaCorreo);
		GeneradorXML generadorXMLPlantillaResolucionRebaja = new GeneradorXML(templatePlantillaCorreo);
		Email emailPLantilla = null;
		try {
			emailPLantilla = generadorXMLPlantillaResolucionRebaja.createObject(Email.class, documentoPlantillaCorreo.getContent());
		} catch (JAXBException e1) {
			e1.printStackTrace();
		}
		ReferenciaDocumentoSummaryVO referenciaDocumentoFinalSummaryVO = null;
		List<ReferenciaDocumentoSummaryVO> referenciasDocumentoSummaryVO = documentService.getVersionFinalEstimacionFlujoCajaByType(idProceso, TipoDocumentosProcesos.ORDINARIOPROGRAMACIONCAJA);

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
		try{
			if(referenciaDocumentoFinalSummaryVO != null){
				Institucion fonasa = institucionDAO.findById(Instituciones.FONASA.getId());
				if(fonasa != null && fonasa.getDirector() != null && fonasa.getDirector().getEmail() != null){
					List<EmailService.Adjunto> adjuntos = new ArrayList<EmailService.Adjunto>();
					DocumentoVO documentOrdinario = documentService.getDocument(referenciaDocumentoFinalSummaryVO.getId());
					String fileNameDocumentoOrdinario = tmpDirDoc + File.separator + documentOrdinario.getName();

					GeneradorDocumento generadorDocumento = new GeneradorDocumento(fileNameDocumentoOrdinario);
					generadorDocumento.saveContent(documentOrdinario.getContent());

					EmailService.Adjunto adjunto = new EmailService.Adjunto();
					adjunto.setDescripcion("Ordinario de Programación de Caja");
					adjunto.setName(documentOrdinario.getName());
					adjunto.setUrl((new File(fileNameDocumentoOrdinario)).toURI().toURL());
					adjuntos.add(adjunto);

					ReferenciaDocumentoSummaryVO referenciaPlanilla = getLastDocumentSummaryEstimacionFlujoCajaTipoDocumento(idProceso, TipoDocumentosProcesos.PLANILLAPROGRAMACIONCAJA);

					if(referenciaPlanilla != null){
						DocumentoVO documentPlanilla = documentService.getDocument(referenciaPlanilla.getId());
						String fileNameDocumentoPlanilla = tmpDirDoc + File.separator + documentPlanilla.getName();
						GeneradorDocumento generadorDocumentoPlanilla = new GeneradorDocumento(fileNameDocumentoPlanilla);
						generadorDocumentoPlanilla.saveContent(documentOrdinario.getContent());
						EmailService.Adjunto adjuntoPlanilla = new EmailService.Adjunto();
						adjuntoPlanilla.setDescripcion("Planilla de Programación de Caja");
						adjuntoPlanilla.setName(documentPlanilla.getName());
						adjuntoPlanilla.setUrl((new File(fileNameDocumentoPlanilla)).toURI().toURL());
						adjuntos.add(adjuntoPlanilla);
					}

					List<String> to = new ArrayList<String>();
					ReporteEmailsDestinatarios destinatarioPara = new ReporteEmailsDestinatarios();
					to.add(fonasa.getDirector().getEmail().getValor());
					destinatarioPara.setDestinatario(fonasa.getDirector());
					List<String> cc = new ArrayList<String>();
					if(emailPLantilla != null && emailPLantilla.getAsunto() != null && emailPLantilla.getCuerpo() != null){
						emailService.sendMail(to, cc, null, emailPLantilla.getAsunto(), emailPLantilla.getCuerpo().replaceAll("(\r\n|\n)", "<br />"), adjuntos);
					}else{
						emailService.sendMail(to, cc, null, "Ordinario de Programación de Caja", "Estimado " + fonasa.getDirector().getNombre() + " " + fonasa.getDirector().getApellidoPaterno() + " " + ((fonasa.getDirector().getApellidoMaterno() != null) ? fonasa.getDirector().getApellidoMaterno() : "") + ": <br /> <p> l</p>", adjuntos);
					}
					ReporteEmailsEnviados reporteEmailsEnviados = new ReporteEmailsEnviados();
					ReporteEmailsAdjuntos reporteEmailsAdjuntos = new ReporteEmailsAdjuntos();
					ReporteEmailsAdjuntos reporteEmailsPlanillaAdjunta = new ReporteEmailsAdjuntos();
					reporteEmailsEnviados.setFecha(new Date());
					estimacionFlujoCajaDAO.save(reporteEmailsEnviados);
					destinatarioPara.setReporteEmailsEnviado(reporteEmailsEnviados);
					destinatarioPara.setTipoDestinatario(new TipoDestinatario(TiposDestinatarios.PARA.getId()));
					estimacionFlujoCajaDAO.save(destinatarioPara);

					ReferenciaDocumento referenciaDocumento = documentDAO.findById(referenciaDocumentoFinalSummaryVO.getId());
					reporteEmailsAdjuntos.setDocumento(referenciaDocumento);
					reporteEmailsAdjuntos.setReporteEmailsEnviado(reporteEmailsEnviados);
					estimacionFlujoCajaDAO.save(reporteEmailsAdjuntos);
					if(referenciaPlanilla != null){
						ReferenciaDocumento referenciaPlanillaAdjunta = documentDAO.findById(referenciaPlanilla.getId());
						reporteEmailsPlanillaAdjunta.setDocumento(referenciaPlanillaAdjunta);
						reporteEmailsPlanillaAdjunta.setReporteEmailsEnviado(reporteEmailsEnviados);
						estimacionFlujoCajaDAO.save(reporteEmailsPlanillaAdjunta);
					}

					ReporteEmailsFlujoCajaConsolidador reporteEmailsFlujoCajaConsolidador = new ReporteEmailsFlujoCajaConsolidador();
					FlujoCajaConsolidador flujoCajaConsolidador = estimacionFlujoCajaDAO.findFlujoCajaConsolidadorById(idProceso);
					reporteEmailsFlujoCajaConsolidador.setFlujoCajaConsolidador(flujoCajaConsolidador);
					reporteEmailsFlujoCajaConsolidador.setReporteEmailsEnviados(reporteEmailsEnviados);
					estimacionFlujoCajaDAO.save(reporteEmailsFlujoCajaConsolidador);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<SeguimientoVO> getBitacora(Integer idProceso, TareasSeguimiento tareaSeguimiento) {
		return seguimientoService.getBitacoraFlujoCaja(idProceso, tareaSeguimiento);
	}

	public Integer getPlantillaCorreo(TipoDocumentosProcesos plantilla) {
		Integer plantillaId = documentService.getPlantillaByType(plantilla);
		return documentService.getDocumentoIdByPlantillaId(plantillaId);
	}

	public Integer cargarPlantillaCorreo(TipoDocumentosProcesos plantilla, File file) {
		Integer plantillaId = documentService.getPlantillaByType(plantilla);
		String filename = "plantillaCorreoOrdinarioProgramacionCaja.xml";
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		filename = tmpDir + File.separator + filename;
		String contentType = mimemap.getContentType(filename.toLowerCase());
		if(plantillaId == null){
			try {
				BodyVO response = alfrescoService.uploadDocument(file, contentType, folderTemplateEstimacionFlujoCaja);
				System.out.println("response upload template --->"+response);
				plantillaId = documentService.createTemplate(plantilla, response.getNodeRef(), response.getFileName(), contentType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			plantillaId = documentService.getDocumentoIdByPlantillaId(plantillaId);
			try {
				BodyVO response = alfrescoService.uploadDocument(file, contentType, folderTemplateEstimacionFlujoCaja);
				System.out.println("response upload template --->"+response);
				documentService.updateDocumentTemplate(plantillaId, response.getNodeRef(), response.getFileName(), contentType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return plantillaId;
	}

	public Integer createSeguimientoFlujoCajaConsolidador(Integer idProceso, TareasSeguimiento tareaSeguimiento, String subject, String body, String username, List<String> para,
			List<String> conCopia, List<String> conCopiaOculta, List<Integer> documentos, Integer ano) {
		String from = usuarioDAO.getEmailByUsername(username);
		if(from == null){
			throw new RuntimeException("Usuario no tiene un email válido");
		}
		List<ReferenciaDocumentoSummaryVO> documentosTmp = new ArrayList<ReferenciaDocumentoSummaryVO>();
		if(documentos != null && documentos.size() > 0){
			for(Integer referenciaDocumentoId : documentos){
				MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
				ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO = documentService.getDocumentSummary(referenciaDocumentoId);
				documentosTmp.add(referenciaDocumentoSummaryVO);
				String contenType= mimemap.getContentType(referenciaDocumentoSummaryVO.getPath().toLowerCase());
				BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummaryVO.getPath()), contenType, folderEstimacionFlujoCaja.replace("{ANO}", ano.toString()));
				System.out.println("response upload template --->"+response);
				documentService.updateDocumentTemplate(referenciaDocumentoId, response.getNodeRef(), response.getFileName(), contenType);
			}
		}
		Integer idSeguimiento = seguimientoService.createSeguimiento(tareaSeguimiento, subject, body, from, para, conCopia, conCopiaOculta, documentosTmp);
		Seguimiento seguimiento = seguimientoDAO.getSeguimientoById(idSeguimiento);
		return estimacionFlujoCajaDAO.createSeguimientoFlujoCajaConsolidador(idProceso, seguimiento);
	}

	public void moveToAlfresco(Integer idProceso, Integer referenciaDocumentoId, TipoDocumentosProcesos tipoDocumento, Boolean lastVersion, Integer ano) {
		System.out.println("Buscando referenciaDocumentoId="+referenciaDocumentoId);
		System.out.println("Buscando idProceso="+idProceso);
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummary = documentService.getDocumentSummary(referenciaDocumentoId);
		System.out.println("Buscando referenciaDocumentoSummary="+referenciaDocumentoSummary);
		if(referenciaDocumentoSummary != null){
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contenType = mimemap.getContentType(referenciaDocumentoSummary.getPath().toLowerCase());
			BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummary.getPath()), contenType, folderEstimacionFlujoCaja.replace("{ANO}", ano.toString()));
			System.out.println("response upload template --->"+response);
			documentService.updateDocumentTemplate(referenciaDocumentoSummary.getId(), response.getNodeRef(), response.getFileName(), contenType);
			FlujoCajaConsolidador flujoCajaConsolidador = estimacionFlujoCajaDAO.findFlujoCajaConsolidadorById(idProceso);
			documentService.createDocumentFlujoCaja(flujoCajaConsolidador, tipoDocumento, referenciaDocumentoId, lastVersion);
		}
	}

	public List<ReporteEmailsEnviadosVO> getReporteCorreosByFlujoCajaConsolidador(Integer idProceso) {
		List<ReporteEmailsEnviadosVO> reporteCorreos = new ArrayList<ReporteEmailsEnviadosVO>();
		List<ReporteEmailsFlujoCajaConsolidador> reportes = estimacionFlujoCajaDAO.getReporteCorreosByFlujoCajaConsolidador(idProceso);
		System.out.println("getReporteCorreosByFlujoCajaConsolidador reportes.size()=" + ((reportes == null) ? 0 : reportes.size()) );
		if(reportes != null && reportes.size() > 0){
			for(ReporteEmailsFlujoCajaConsolidador reporte : reportes){
				ReporteEmailsEnviadosVO correo = new ReporteEmailsEnviadosVO();
				if(reporte.getReporteEmailsEnviados().getIdServicio() != null){
					correo.setIdServicio(reporte.getReporteEmailsEnviados().getIdServicio().getId());
					correo.setNombreServicio(reporte.getReporteEmailsEnviados().getIdServicio().getNombre());
				}	
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
					correo.setCc(cc);
					correo.setTo(to);
				}
				reporteCorreos.add(correo);
			}
		}
		return reporteCorreos;
	}

	public void moveToAlfrescoFlujoCaja(Integer idProceso, Integer idServicio, Integer referenciaDocumentoId, TipoDocumentosProcesos tipoDocumento, Boolean lastVersion, Integer ano) {
		System.out.println("Buscando referenciaDocumentoId="+referenciaDocumentoId);
		System.out.println("Buscando idProceso="+idProceso);
		System.out.println("Buscando idServicio="+idServicio);
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummary = documentService.getDocumentSummary(referenciaDocumentoId);
		System.out.println("Buscando referenciaDocumentoSummary="+referenciaDocumentoSummary);
		if(referenciaDocumentoSummary != null){
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contenType = mimemap.getContentType(referenciaDocumentoSummary.getPath().toLowerCase());
			BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummary.getPath()), contenType, folderEstimacionFlujoCaja.replace("{ANO}", ano.toString()));
			System.out.println("response upload template --->"+response);
			documentService.updateDocumentTemplate(referenciaDocumentoSummary.getId(), response.getNodeRef(), response.getFileName(), contenType);
			FlujoCajaConsolidador flujoCajaConsolidador = estimacionFlujoCajaDAO.findFlujoCajaConsolidadorById(idProceso);
			documentService.createDocumentFlujoCaja(flujoCajaConsolidador, idServicio, tipoDocumento, referenciaDocumentoId, lastVersion);
		}

	}

	public int countVersionFinalEstimacionFlujoCajaByType(Integer idProceso, TipoDocumentosProcesos tipoDocumento) {
		List<ReferenciaDocumentoSummaryVO> versionesFinales = documentService.getVersionFinalEstimacionFlujoCajaByType(idProceso, tipoDocumento);
		if(versionesFinales != null && versionesFinales.size() > 0){
			return versionesFinales.size();
		}
		return 0;
	}

	public ReferenciaDocumentoSummaryVO getLastDocumentSummaryEstimacionFlujoCajaTipoDocumento(Integer idProceso, TipoDocumentosProcesos tipoDocumento) {
		return documentService.getLastDocumentoSummaryByEstimacionFlujoCajaTipoDocumento(idProceso, tipoDocumento);
	}

	public List<ResumenFONASAServicioVO> cargarFonasaServicio(Subtitulo subtitulo, Integer ano) {
		List<ResumenFONASAServicioVO> resultado =  new ArrayList<ResumenFONASAServicioVO>();
		List<ServicioSalud> listaServicios = servicioSaludDAO.getServiciosOrderId();
		for(ServicioSalud servicioSalud : listaServicios){
			ResumenFONASAServicioVO resumen = new ResumenFONASAServicioVO();
			resumen.setIdServicio(servicioSalud.getId());
			resumen.setNombreServicio(servicioSalud.getNombre());
			Long totalServicio = 0L;
			List<ProgramaVO> programasFonasa = programasService.getProgramasFonasa(true, ano, subtitulo);
			List<ProgramaFonasaVO> resumenProgramaFonasa = new ArrayList<ProgramaFonasaVO>();
			for(ProgramaVO programaFonasa: programasFonasa){
				ProgramaFonasaVO programaFonasaVO = new ProgramaFonasaVO();
				programaFonasaVO.setIdPrograma(programaFonasa.getId());
				programaFonasaVO.setNombrePrograma(programaFonasa.getNombre());
				List<CajaMonto> cajaMontos = cajaDAO.getByProgramaAnoServicioSubtituloMes(programaFonasa.getIdProgramaAno(), servicioSalud.getId(), subtitulo, Integer.parseInt(getMesCurso(true)));
				Long acumulador = 0L;
				if(cajaMontos != null && cajaMontos.size() > 0){
					for(CajaMonto cajaMonto : cajaMontos){
						acumulador += cajaMonto.getMonto();
					}
				}
				programaFonasaVO.setMonto(acumulador);
				totalServicio += acumulador;
				resumenProgramaFonasa.add(programaFonasaVO);
			}

			Long totalOtros = 0L;
			List<ProgramaVO> otrosProgramas = programasService.getProgramasFonasa(false, ano, subtitulo);
			for(ProgramaVO otroPrograma : otrosProgramas){
				List<CajaMonto> cajaMontos = cajaDAO.getByProgramaAnoServicioSubtituloMes(otroPrograma.getIdProgramaAno(), servicioSalud.getId(), subtitulo, Integer.parseInt(getMesCurso(true)));
				if(cajaMontos != null && cajaMontos.size() > 0){
					for(CajaMonto cajaMonto : cajaMontos){
						totalOtros += cajaMonto.getMonto();
						totalServicio += cajaMonto.getMonto();
					}
				}
			}
			resumen.setProgramasFonasa(resumenProgramaFonasa);
			resumen.setTotalOtrosProgramas(totalOtros);
			resumen.setTotal(totalServicio);
			resultado.add(resumen);
		}
		return resultado;
	}

	public List<ResumenFONASAMunicipalVO> cargarFonasaMunicipal(Integer ano) {
		List<ResumenFONASAMunicipalVO> resultado =  new ArrayList<ResumenFONASAMunicipalVO>();
		List<ServicioSalud> listaServicios = servicioSaludDAO.getServiciosOrderId();

		DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(ano);
		System.out.println("distribucionInicialPercapita.getIdDistribucionInicialPercapita() --> "+distribucionInicialPercapita.getIdDistribucionInicialPercapita());

		for(ServicioSalud servicioSalud : listaServicios){
			ResumenFONASAMunicipalVO resumen = new ResumenFONASAMunicipalVO();
			resumen.setIdServicio(servicioSalud.getId());
			resumen.setNombreServicio(servicioSalud.getNombre());
			Long totalServicio = 0L;
			List<ProgramaVO> programasFonasa = programasService.getProgramasFonasa(true, ano, Subtitulo.SUBTITULO24);
			List<ProgramaFonasaVO> resumenProgramaFonasa = new ArrayList<ProgramaFonasaVO>();
			for(ProgramaVO programaFonasa: programasFonasa){
				ProgramaFonasaVO programaFonasaVO = new ProgramaFonasaVO();
				programaFonasaVO.setIdPrograma(programaFonasa.getId());
				programaFonasaVO.setNombrePrograma(programaFonasa.getNombre());
				List<CajaMonto> cajaMontos = cajaDAO.getByProgramaAnoServicioSubtituloMes(programaFonasa.getIdProgramaAno(), servicioSalud.getId(), Subtitulo.SUBTITULO24, Integer.parseInt(getMesCurso(true)));
				Long acumulador = 0L;
				if(cajaMontos != null && cajaMontos.size() > 0){
					for(CajaMonto cajaMonto : cajaMontos){
						acumulador += cajaMonto.getMonto();
					}
				}
				programaFonasaVO.setMonto(acumulador);
				totalServicio += acumulador;
				resumenProgramaFonasa.add(programaFonasaVO);
			}
			resumen.setProgramasFonasa(resumenProgramaFonasa);
			Long totalOtros = 0L;
			List<ProgramaVO> otrosProgramas = programasService.getProgramasFonasa(false, ano, Subtitulo.SUBTITULO24);
			for(ProgramaVO otroPrograma: otrosProgramas){
				//Descartamos percapita id = -1
				if(otroPrograma.getId()>0){
					List<CajaMonto> cajaMontos = cajaDAO.getByProgramaAnoServicioSubtituloMes(otroPrograma.getIdProgramaAno(), servicioSalud.getId(), Subtitulo.SUBTITULO24, Integer.parseInt(getMesCurso(true)));
					if(cajaMontos != null && cajaMontos.size() > 0){
						for(CajaMonto cajaMonto : cajaMontos){
							totalOtros += cajaMonto.getMonto();
							totalServicio += cajaMonto.getMonto();
						}
					}
				}
			}

			resumen.setTotalOtrosProgramas(totalOtros);


			List<AntecendentesComunaCalculado> antecendentesComunasCalculado = antecedentesComunaDAO.findAntecedentesComunaCalculadosByDistribucionInicialPercapitaVigenteServicio(distribucionInicialPercapita.getIdDistribucionInicialPercapita(), servicioSalud.getId());
			Long perCapitaBasal = 0L;
			Long desempenoDificil = 0L;
			Long rebaja = 0L;
			Long descuentoRetiro = 0L;
			Long leyes = 0L;
			if(antecendentesComunasCalculado != null && antecendentesComunasCalculado.size() > 0){
				for(AntecendentesComunaCalculado antecendentesComunaCalculado : antecendentesComunasCalculado){
					perCapitaBasal += antecendentesComunaCalculado.getPercapitaMes();
					desempenoDificil += antecendentesComunaCalculado.getDesempenoDificil();
					if(antecendentesComunaCalculado.getAntecedentesComunaCalculadoRebajas() != null && antecendentesComunaCalculado.getAntecedentesComunaCalculadoRebajas().size() > 0){
						for(AntecedentesComunaCalculadoRebaja antecedentesComunaCalculadoRebaja : antecendentesComunaCalculado.getAntecedentesComunaCalculadoRebajas()){
							if(antecedentesComunaCalculadoRebaja.getRebaja() != null && antecedentesComunaCalculadoRebaja.getRebaja().getRebajaCorte() != null && antecedentesComunaCalculadoRebaja.getRebaja().getRebajaCorte().getMesRebaja() != null 
									&& antecedentesComunaCalculadoRebaja.getRebaja().getRebajaCorte().getMesRebaja().getIdMes().equals( Integer.parseInt(getMesCurso(true)))){
								rebaja += antecedentesComunaCalculadoRebaja.getMontoRebaja();
							}
						}
					}
				}
			}
			resumen.setPerCapitaBasal(perCapitaBasal);
			resumen.setDesempenoDificil(desempenoDificil);
			resumen.setRebaja(rebaja);
			resumen.setDescuentoRetiro(descuentoRetiro);
			resumen.setLeyes(leyes);

			Long totalPercapita = resumen.getPerCapitaBasal() + resumen.getDesempenoDificil() - resumen.getRebaja() - resumen.getDescuentoRetiro();
			resumen.setTotalPercapita(totalPercapita);
			resumen.setTotal(totalServicio + totalPercapita);
			resultado.add(resumen);
		}
		return resultado;
	}

}
