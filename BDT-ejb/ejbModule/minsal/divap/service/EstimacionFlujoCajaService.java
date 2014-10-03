package minsal.divap.service;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import minsal.divap.dao.AntecedentesComunaDAO;
import minsal.divap.dao.CajaDAO;
import minsal.divap.dao.DistribucionInicialPercapitaDAO;
import minsal.divap.dao.EstimacionFlujoCajaDAO;
import minsal.divap.dao.MesDAO;
import minsal.divap.dao.ProgramasDAO;
import minsal.divap.dao.SeguimientoDAO;
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.doc.GeneradorWord;
import minsal.divap.doc.GeneradorWordBorradorAporteEstatal;
import minsal.divap.enums.BusinessProcess;
import minsal.divap.enums.EstadosProgramas;
import minsal.divap.enums.Subtitulo;
import minsal.divap.enums.TareasSeguimiento;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.excel.GeneradorExcel;
import minsal.divap.excel.impl.EstimacionFlujoCajaSheetExcel;
import minsal.divap.model.mappers.ConvenioRemesaSubtituloFlujoCajaMapper;
import minsal.divap.model.mappers.SubtituloFlujoCajaMapper;
import minsal.divap.util.Util;
import minsal.divap.vo.AsignacionDistribucionPerCapitaVO;
import minsal.divap.vo.BaseVO;
import minsal.divap.vo.BodyVO;
import minsal.divap.vo.DocumentoVO;
import minsal.divap.vo.EmailVO;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.SeguimientoVO;
import minsal.divap.vo.SubtituloFlujoCajaVO;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import cl.minsal.divap.model.AnoEnCurso;
import cl.minsal.divap.model.Caja;
import cl.minsal.divap.model.CajaMonto;
import cl.minsal.divap.model.CajaMontoPK;
import cl.minsal.divap.model.DistribucionInicialPercapita;
import cl.minsal.divap.model.DocumentoEstimacionflujocaja;
import cl.minsal.divap.model.EstadoPrograma;
import cl.minsal.divap.model.MarcoPresupuestario;
import cl.minsal.divap.model.Mes;
import cl.minsal.divap.model.MontoMes;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.ProgramaMunicipalCoreComponente;
import cl.minsal.divap.model.ProgramaServicioCoreComponente;
import cl.minsal.divap.model.Seguimiento;
import cl.minsal.divap.model.TipoDocumento;

@Stateless
@LocalBean
public class EstimacionFlujoCajaService {
	@EJB
	private EstimacionFlujoCajaDAO estimacionFlujoCajaDAO;
	@EJB
	private AlfrescoService alfrescoService;
	@EJB
	private DocumentService documentService;
	@EJB
	private ServicioSaludService servicioSaludService;
	@EJB
	private ProcessService processService; 

	@Resource(name = "tmpDir")
	private String tmpDir;

	@Resource(name = "tmpDirDoc")
	private String tmpDirDoc;

	@Resource(name = "folderEstimacionFlujoCaja")
	private String folderEstimacionFlujoCaja;

	@Resource(name = "folderTemplateEstimacionFlujoCaja")
	private String folderTemplateEstimacionFlujoCaja;

	@EJB
	private UsuarioDAO usuarioDAO;

	@EJB
	private SeguimientoService seguimientoService;

	@EJB
	private SeguimientoDAO seguimientoDAO;

	@EJB
	private AntecedentesComunaDAO antecedentesComunaDAO;

	@EJB
	private DistribucionInicialPercapitaDAO distribucionInicialPercapitaDAO;

	@EJB
	private CajaDAO cajaDAO;

	@EJB
	private MesDAO mesDAO;

	@EJB
	private ProgramasDAO programasDAO;

	@EJB
	private EmailService emailService;

	// Generar documento
	public Integer elaborarOrdinarioProgramacionCaja(Integer idLineaProgramatica) {

		// return
		// distribucionInicialPercapitaService.valorizarDisponibilizarPlanillaTrabajo(idDistribucionInicialPercapita);

		Integer plantillaBorradorOrdinarioProgramacionCaja = documentService
				.getPlantillaByType(TipoDocumentosProcesos.PLANTILLABORRADORORDINARIOPROGRAMACIONCAJA);
		if (plantillaBorradorOrdinarioProgramacionCaja == null) {
			throw new RuntimeException(
					"No se puede crear Borrador Decreto Aporte Estatal, la plantilla no esta cargada");
		}
		try {
			ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryBorradorAporteEstatalVO = documentService
					.getDocumentByPlantillaId(plantillaBorradorOrdinarioProgramacionCaja);
			DocumentoVO documentoBorradorOrdinarioProgramacionCajaVO = documentService
					.getDocument(referenciaDocumentoSummaryBorradorAporteEstatalVO
							.getId());
			String templateOrdinarioProgramacionCaja = tmpDirDoc
					+ File.separator
					+ documentoBorradorOrdinarioProgramacionCajaVO.getName();
			templateOrdinarioProgramacionCaja = templateOrdinarioProgramacionCaja
					.replace(" ", "");
			String filenameBorradorOrdinarioProgramacionCaja = tmpDirDoc
					+ File.separator + new Date().getTime() + "_"
					+ "OrdinarioProgramacionCaja.docx";
			filenameBorradorOrdinarioProgramacionCaja = filenameBorradorOrdinarioProgramacionCaja
					.replaceAll(" ", "");
			System.out.println("filenameBorradorAporteEstatal filename-->"
					+ filenameBorradorOrdinarioProgramacionCaja);
			System.out.println("templateBorradorAporteEstatal template-->"
					+ templateOrdinarioProgramacionCaja);
			GeneradorWord generadorWordPlantillaBorradorOrdinarioProgramacionCaja = new GeneradorWord(
					filenameBorradorOrdinarioProgramacionCaja);
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contentType = mimemap
					.getContentType(filenameBorradorOrdinarioProgramacionCaja
							.toLowerCase());
			System.out.println("contenTypeBorradorAporteEstatal->"
					+ contentType);
			// generadorWordPlantillaBorradorDecretoAporteEstatal.saveContent(documentoBorradorAporteEstatalVO.getContent(),
			// XWPFDocument.class);
			generadorWordPlantillaBorradorOrdinarioProgramacionCaja
			.saveContent(documentoBorradorOrdinarioProgramacionCajaVO
					.getContent(), XWPFDocument.class);

			Map<String, Object> parametersBorradorAporteEstatal = new HashMap<String, Object>();
			parametersBorradorAporteEstatal.put("{ano}",
					Util.obtenerAno(new Date()));
			parametersBorradorAporteEstatal.put("{mes}",
					Util.obtenerNombreMes(Util.obtenerMes(new Date())));
			GeneradorWordBorradorAporteEstatal generadorWordDecretoBorradorAporteEstatal = new GeneradorWordBorradorAporteEstatal(
					filenameBorradorOrdinarioProgramacionCaja,
					templateOrdinarioProgramacionCaja);
			generadorWordDecretoBorradorAporteEstatal.replaceValues(
					parametersBorradorAporteEstatal, null, XWPFDocument.class);
			BodyVO response = alfrescoService.uploadDocument(new File(
					filenameBorradorOrdinarioProgramacionCaja), contentType,
					folderEstimacionFlujoCaja.replace("{ANO}", getAnoCurso()
							.toString()));
			System.out.println("response responseBorradorAporteEstatal --->"
					+ response);
			// TODO: Crear documento para estimacion flujo de caja.
			// plantillaIdBorradorDecretoAporteEstatal =
			// documentService.createDocumentPercapita(idDistribucionInicialPercapita,
			// TipoDocumentosProcesos.BORRADORAPORTEESTATAL,
			// responseBorradorAporteEstatal.getNodeRef(),
			// responseBorradorAporteEstatal.getFileName(),
			// contenTypeBorradorAporteEstatal);

			ProgramaAno programaAno = programasDAO
					.getProgramaAnoByID(idLineaProgramatica);
			TipoDocumento tipoDocumento = new TipoDocumento(
					TipoDocumentosProcesos.PLANTILLABORRADORORDINARIOPROGRAMACIONCAJA
					.getId());
			plantillaBorradorOrdinarioProgramacionCaja = documentService
					.createDocumentPropuestaEstimacionFlujoCaja(programaAno,
							tipoDocumento, response.getNodeRef(),
							response.getFileName(), contentType);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		return plantillaBorradorOrdinarioProgramacionCaja;
	}

	public List<AsignacionDistribucionPerCapitaVO> buscarDatosPlanillaPropuestaEstimacion(
			Integer idPrograma) {
		// List<AntecendentesComunaCalculado> antecendentesComunaCalculado =
		// antecedentesComunaDAO.findAntecedentesComunaCalculadosByDistribucionInicialPercapita(idDistribucionInicialPercapita);
		// List<AsignacionDistribucionPerCapitaVO> antecedentesCalculados = new
		// ArrayList<AsignacionDistribucionPerCapitaVO>();
		// if(antecendentesComunaCalculado != null &&
		// antecendentesComunaCalculado.size() > 0){
		// for(AntecendentesComunaCalculado antecendenteComunaCalculado :
		// antecendentesComunaCalculado){
		// AsignacionDistribucionPerCapitaVO asignacionDistribucionPerCapitaVO =
		// new
		// AsignacionDistribucionPercapitaMapper().getBasic(antecendenteComunaCalculado);
		// if(asignacionDistribucionPerCapitaVO != null){
		// antecedentesCalculados.add(asignacionDistribucionPerCapitaVO);
		// }
		// }
		// }
		// return antecedentesCalculados;
		return null;
	}

	// Para hacer el calculo de la propuesta, se debe hacer una copia de los
	// valores del año pasado.
	public void calcularPropuesta(Integer idProgramaAno, Boolean iniciarFlujoCaja) {
		if(iniciarFlujoCaja){
			ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idProgramaAno);
			ProgramaAno programaAnoAnterior = programasDAO.getProgramaAnoByIDProgramaAno(programaAno.getPrograma().getId(), (getAnoCurso() - 1));
			if(programaAnoAnterior != null){
				for(MarcoPresupuestario marcoPresupuestarioAnoAnterior : programaAnoAnterior.getMarcosPresupuestarios()){
					MarcoPresupuestario marcoPresupuestario = new MarcoPresupuestario();
					marcoPresupuestario.setIdProgramaAno(programaAno);
					marcoPresupuestario.setMarcoInicial((int)(marcoPresupuestarioAnoAnterior.getMarcoModificado() * programaAno.getAno().getInflactorMarcoPresupuestario()));
					marcoPresupuestario.setServicioSalud(marcoPresupuestarioAnoAnterior.getServicioSalud());
					cajaDAO.save(marcoPresupuestario);
					List<Caja> flujosCaja = cajaDAO.getByIdProgramaAnoIdServicio(programaAnoAnterior.getIdProgramaAno(), marcoPresupuestarioAnoAnterior.getServicioSalud().getId());
					if(flujosCaja != null && flujosCaja.size() > 0){
						//Obtenemos el programa año actual.
						for (Caja cajaAnoAnterior : flujosCaja) {
							Caja cajaActual = new Caja();
							cajaActual.setMarcoPresupuestario(marcoPresupuestario);
							cajaActual.setIdComponente(cajaAnoAnterior.getIdComponente());
							cajaActual.setIdSubtitulo(cajaAnoAnterior.getIdSubtitulo());
							int montoActual = (int)(cajaAnoAnterior.getMonto() * programaAno.getAno().getInflactorMarcoPresupuestario());
							cajaActual.setMonto(montoActual);
							cajaDAO.save(cajaActual);
							if(cajaAnoAnterior.getCajaMontos() != null && cajaAnoAnterior.getCajaMontos().size() > 0){
								for(CajaMonto cajaMontoAnterior : cajaAnoAnterior.getCajaMontos()){
									CajaMonto cajaMontoActual = new CajaMonto();
									Mes mes = mesDAO.getMesPorID(cajaMontoAnterior.getMes().getIdMes());
									CajaMontoPK cajaMontoPK = new CajaMontoPK(cajaActual.getId(), cajaMontoAnterior.getMes().getIdMes());
									cajaMontoActual.setCajaMontoPK(cajaMontoPK);
									cajaMontoActual.setCaja(cajaActual);
									cajaMontoActual.setMes(mes);
									if(cajaMontoAnterior.getMonto() != null){
										MontoMes montoMesActual = new MontoMes();
										int porcentaje = cajaMontoAnterior.getMonto().getPorcentajeMonto();
										int monto = (int)(montoActual * (porcentaje/100.0));
										montoMesActual.setPorcentajeMonto(porcentaje);
										montoMesActual.setMonto(monto);
										montoMesActual.setFechaMonto(new Date());
										cajaDAO.save(montoMesActual);
										cajaMontoActual.setMonto(montoMesActual);
									}
									cajaDAO.save(cajaMontoActual);
								}
							}
						}
					}
				}
				programaAno.setEstadoFlujoCaja(new EstadoPrograma(EstadosProgramas.ENREVISION.getId()));
			}
		}else{
			//Obtenemos los datos del programa ano anterior.
			DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast();
			List<Object[]> percapitaServicios = antecedentesComunaDAO.groupPercapitaServicioByDistribucionInicialPercapita(distribucionInicialPercapita.getIdDistribucionInicialPercapita());
			if(percapitaServicios != null && percapitaServicios.size() > 0){
				for(Object[] percapitaServicio : percapitaServicios){
					Integer idServicio = ((Number)(percapitaServicio[0])).intValue();
					Long percapitaAno = ((Number)(percapitaServicio[1])).longValue();
					MarcoPresupuestario marcoPresupuestario = cajaDAO.getMarcoPresupuestarioByProgramaAnoServicio(idProgramaAno, idServicio);
					if(percapitaAno != null){
						marcoPresupuestario.setMarcoModificado(percapitaAno.intValue());
					}else{
						marcoPresupuestario.setMarcoModificado(0);
					}
					List<Caja> cajas = cajaDAO.getByIdProgramaAnoIdServicio(idProgramaAno, idServicio);
					if(cajas != null && cajas.size() > 0){
						//Obtenemos el programa actual.
						for (Caja cajaActual : cajas) {
							Integer idComponente = cajaActual.getIdComponente().getId();
							Integer idSubtitulo = cajaActual.getIdSubtitulo().getIdTipoSubtitulo();
							Integer totalComponenteSubtitulo = 0;
							List<ProgramaMunicipalCoreComponente> programaMunicipalesCore = programasDAO.getProgramaMunicipales(idProgramaAno, idComponente, idSubtitulo);
							if(programaMunicipalesCore != null && programaMunicipalesCore.size() > 0){
								for(ProgramaMunicipalCoreComponente programaMunicipalCoreComponente : programaMunicipalesCore){
									totalComponenteSubtitulo += programaMunicipalCoreComponente.getMonto();
								}
							}
							List<ProgramaServicioCoreComponente> programaServiciosCore = programasDAO.getProgramaServicios(idProgramaAno, idComponente, idSubtitulo);
							if(programaServiciosCore != null && programaServiciosCore.size() > 0){
								for(ProgramaServicioCoreComponente programaServicioCoreComponente : programaServiciosCore){
									totalComponenteSubtitulo += programaServicioCoreComponente.getMonto();
								}
							}
							cajaActual.setMonto(totalComponenteSubtitulo);
							marcoPresupuestario.setMarcoModificado(marcoPresupuestario.getMarcoModificado() + totalComponenteSubtitulo);
						}
						
						for (Caja cajaActual : cajas) {
							if(cajaActual.getCajaMontos() != null && cajaActual.getCajaMontos().size() > 0){
								for(CajaMonto cajaMonto : cajaActual.getCajaMontos()){
									int porcentaje = cajaMonto.getMonto().getPorcentajeMonto();
									int monto = (int)(cajaActual.getMonto() * (porcentaje/100.0));
									cajaMonto.getMonto().setMonto(monto);
								}
							}
						}
					}
				}
			}
		}
	}

	public Integer getIdPlantillaProgramacion() {
		// TODO Auto-generated method stub
		Integer plantillaId = null;// documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLAPROGRAMACION);
		if (plantillaId == null) {
			// TODO: Implementar la creacion del excel.
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
			EstimacionFlujoCajaSheetExcel estimacionFlujoCajaSheetExcel = new EstimacionFlujoCajaSheetExcel(
					headers, servicios);
			generadorExcel.addSheet(estimacionFlujoCajaSheetExcel, "Hoja 1");
			try {
				BodyVO response = alfrescoService.uploadDocument(
						generadorExcel.saveExcel(), contenType,
						folderEstimacionFlujoCaja);
				System.out
				.println("response AsignacionRecursosPercapitaSheetExcel --->"
						+ response);
				plantillaId = documentService.createTemplate(
						TipoDocumentosProcesos.PLANTILLAPOBLACIONINSCRITA,
						response.getNodeRef(), response.getFileName(),
						contenType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else { 
			plantillaId = documentService.getDocumentoIdByPlantillaId(plantillaId);
		}
		return plantillaId;
	}

	public Integer getIdPlantillaPropuesta() {
		// TODO Auto-generated method stub
		Integer plantillaId = null;// documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLAPROPUESTA);
		if (plantillaId == null) {
			// TODO: Implementar la creacion del excel.
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
			EstimacionFlujoCajaSheetExcel estimacionFlujoCajaSheetExcel = new EstimacionFlujoCajaSheetExcel(
					headers, servicios);
			generadorExcel.addSheet(estimacionFlujoCajaSheetExcel, "Hoja 1");
			try {
				BodyVO response = alfrescoService.uploadDocument(
						generadorExcel.saveExcel(), contenType,
						folderEstimacionFlujoCaja);
				System.out
				.println("response AsignacionRecursosPercapitaSheetExcel --->"
						+ response);
				plantillaId = documentService.createTemplate(
						TipoDocumentosProcesos.PLANTILLAPOBLACIONINSCRITA,
						response.getNodeRef(), response.getFileName(),
						contenType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			plantillaId = documentService
					.getDocumentoIdByPlantillaId(plantillaId);
		}
		return plantillaId;
	}

	// CORREO
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
		}else{
			dateFormat = new SimpleDateFormat("MMMM");
			mesCurso = dateFormat.format(new Date());
		}
		return mesCurso;
	}

	public Integer createSeguimiento(Integer idLineaProgramatica,
			TareasSeguimiento tareaSeguimiento, String subject, String body,
			String username, List<String> para, List<String> conCopia,
			List<String> conCopiaOculta, List<Integer> documentos) {
		String from = usuarioDAO.getEmailByUsername(username);
		if (from == null) {
			throw new RuntimeException("Usuario no tiene un email valido");
		}
		List<ReferenciaDocumentoSummaryVO> documentosTmp = new ArrayList<ReferenciaDocumentoSummaryVO>();
		if (documentos != null && documentos.size() > 0) {
			for (Integer referenciaDocumentoId : documentos) {
				MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
				ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO = documentService
						.getDocumentSummary(referenciaDocumentoId);
				documentosTmp.add(referenciaDocumentoSummaryVO);
				String contenType = mimemap
						.getContentType(referenciaDocumentoSummaryVO.getPath()
								.toLowerCase());
				BodyVO response = alfrescoService.uploadDocument(new File(
						referenciaDocumentoSummaryVO.getPath()), contenType,
						folderEstimacionFlujoCaja.replace("{ANO}",
								getAnoCurso().toString()));
				System.out.println("response upload template --->" + response);
				documentService.updateDocumentTemplate(referenciaDocumentoId,
						response.getNodeRef(), response.getFileName(),
						contenType);
			}
		}
		Integer idSeguimiento = seguimientoService.createSeguimiento(
				tareaSeguimiento, subject, body, from, para, conCopia,
				conCopiaOculta, documentosTmp);
		Seguimiento seguimiento = seguimientoDAO
				.getSeguimientoById(idSeguimiento);

		estimacionFlujoCajaDAO.createSeguimiento(idLineaProgramatica,seguimiento);
		return 1;// distribucionInicialPercapitaDAO.createSeguimiento(idDistribucionInicialPercapita,
		// seguimiento);
	}

	public List<SeguimientoVO> getBitacora(
			Integer idPorgramaAno,
			TareasSeguimiento tareaSeguimiento) {
		return seguimientoService.getBitacoraEstimacionFlujoCaja(idPorgramaAno,	tareaSeguimiento);
	}

	public Integer generarPlanillaPropuesta(Integer idLineaProgramatica) {
		Integer planillaTrabajoId = null;
		// List<AsignacionDistribucionPerCapitaVO> antecedentesCalculados =
		// findAntecedentesComunaCalculadosByDistribucionInicialPercapita(idDistribucionInicialPercapita);
		List<BaseVO> servicios = servicioSaludService.getAllServicios();
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator
				+ "planillaPropuestaEstimacionFlujoCaja.xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());
		GeneradorExcel generadorExcel = new GeneradorExcel(filename);
		List<String> headers = new ArrayList<String>();
		headers.add("REGION");
		headers.add("SERVICIO");
		headers.add("COMUNA");
		headers.add("CLASIFICACION " + getAnoCurso());
		headers.add("Ref. Asig_Zon " + getAnoCurso());
		headers.add("Tramo Pobreza");
		headers.add("Per Capita Basal " + getAnoCurso());
		headers.add("Pobreza " + getAnoCurso());
		headers.add("Ruralidad " + getAnoCurso());
		headers.add("Valor Ref. Asig_Zon " + getAnoCurso());
		headers.add("Valor Per Capita " + getAnoCurso() + "($/mes "
				+ getAnoCurso() + ")");
		headers.add("POBLACION AÑO" + getAnoCurso());
		headers.add("POBLACION MAYOR DE 65 AÑOS" + getAnoCurso());
		headers.add("PER CAPITA AÑO " + getAnoCurso() + "(m$ " + getAnoCurso()
				+ ")");
		EstimacionFlujoCajaSheetExcel asignacionDistribucionPercapitaSheetExcel = new EstimacionFlujoCajaSheetExcel(
				headers, servicios);
		generadorExcel.addSheet(asignacionDistribucionPercapitaSheetExcel,
				"Hoja 1");
		/*try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel
					.saveExcel(), contenType, folderEstimacionFlujoCaja
					.replace("{ANO}", getAnoCurso().toString()));
			System.out.println("response AsignacionRecursosPercapitaSheetExcel --->" + response);
			ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idLineaProgramatica);
			TipoDocumento tipoDocumento = new TipoDocumento(TipoDocumentosProcesos.PLANTILLAPROPUESTA.getId());
			planillaTrabajoId = documentService.createDocumentPropuestaEstimacionFlujoCaja(programaAno,
							tipoDocumento, response.getNodeRef(),
							response.getFileName(), contenType);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		planillaTrabajoId = -1;
		return planillaTrabajoId;
	}

	public Integer eliminarPlanillaPropuesta(Integer idLineaProgramatica) {
		// TODO Auto-generated method stub
		// Obtener todos los documentos.
		ProgramaAno programaAno = programasDAO
				.getProgramaAnoByID(idLineaProgramatica);
		TipoDocumento tipoDocumento = new TipoDocumento(
				TipoDocumentosProcesos.PLANTILLAPROPUESTA.getId());
		List<DocumentoEstimacionflujocaja> lstDocEstimacionFlujoCaja = documentService
				.getDocumentEstimacionFlujoCajaByIDProgramaAnoTipoDocumento(
						programaAno, tipoDocumento);
		for (DocumentoEstimacionflujocaja documentoEstimacionflujocaja : lstDocEstimacionFlujoCaja) {
			String key = ((documentoEstimacionflujocaja.getIdDocumento()
					.getNodeRef() == null) ? documentoEstimacionflujocaja
							.getIdDocumento().getPath() : documentoEstimacionflujocaja
							.getIdDocumento().getNodeRef()
							.replace("workspace://SpacesStore/", ""));

			alfrescoService.delete(key);
		}

		return 0;
	}
	
	@Asynchronous
	public void notificarUsuarioConsolidador(Integer idProgramaAno, String usuario) {
		//Iniciar el segundo proceso.
		ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idProgramaAno);
		programaAno.setEstadoFlujoCaja(new EstadoPrograma(EstadosProgramas.REVISADO.getId()));
		String mailTo = usuarioDAO.getEmailByUsername(usuario);
		EmailVO emailVO = new EmailVO();
		emailVO.setContent("Revisar Consolidación de Estimación de Flujos de Caja <p> para el programaAno " + programaAno.getPrograma().getNombre() +  " que se encuentra disponible en su Bandeja de Tareas.</p>");
		emailVO.setSubject("Revisar Consolidación de Estimación de Flujos de Caja");
		emailVO.setTo(mailTo);
		emailService.sendMail(emailVO);
		System.out.println("Fin enviar mail consolidador");
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("usuario", usuario);		
		try {
			processService.startProcess(BusinessProcess.ESTIMACIONFLUJOCAJACONSOLIDADOR, parameters);
			System.out.println("Fin Iniciar el proceso consolidador");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Fin notificarUsuarioConsolidador");
	}

	public Integer eliminarOrdinarioFonasa(Integer idLineaProgramatica) {
		//Eliminar todos menos el ultimo.
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummary =  getLastDocumentSummaryEstimacionFlujoCajaType(idLineaProgramatica, TipoDocumentosProcesos.PLANTILLABORRADORORDINARIOPROGRAMACIONCAJA);

		ProgramaAno programaAno = programasDAO
				.getProgramaAnoByID(idLineaProgramatica);

		TipoDocumento tipoDocumento = new TipoDocumento(
				TipoDocumentosProcesos.PLANTILLABORRADORORDINARIOPROGRAMACIONCAJA
				.getId());
		List<DocumentoEstimacionflujocaja> lstDocEstimacionFlujoCaja = documentService
				.getDocumentEstimacionFlujoCajaByIDProgramaAnoTipoDocumento(
						programaAno, tipoDocumento);

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



	public void moveToAlfresco(Integer idLineaProgramatica,
			Integer referenciaDocumentoId, TipoDocumentosProcesos tipoDocumento,
			boolean versionFinal) {
		System.out.println("Buscando referenciaDocumentoId="
				+ referenciaDocumentoId);
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummary = documentService
				.getDocumentSummary(referenciaDocumentoId);

		System.out.println("Buscando referenciaDocumentoSummary="
				+ referenciaDocumentoSummary);
		if (referenciaDocumentoSummary != null) {
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contenType = mimemap
					.getContentType(referenciaDocumentoSummary.getPath()
							.toLowerCase());
			BodyVO response = alfrescoService.uploadDocument(new File(
					referenciaDocumentoSummary.getPath()), contenType,
					folderEstimacionFlujoCaja.replace("{ANO}", getAnoCurso().toString()));
			System.out.println("response upload template --->" + response);
			documentService.updateDocumentTemplate(
					referenciaDocumentoSummary.getId(), response.getNodeRef(),
					response.getFileName(), contenType);
			//			DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO
			//					.findById(idDistribucionInicialPercapita);
			ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idLineaProgramatica);
			documentService.createDocumentOrdinarioProgramacionEstimacionFlujoCaja(programaAno,tipoDocumento,referenciaDocumentoId,versionFinal);
			//documentService.createDocumentPercapita(
			//		distribucionInicialPercapita, tipoDocumento,
			//		referenciaDocumentoId, lastVersion);

		}

	}

	public ReferenciaDocumentoSummaryVO getLastDocumentSummaryEstimacionFlujoCajaType(
			Integer idProgramaAno,
			TipoDocumentosProcesos tipoDocumento) {
		// TODO Auto-generated method stub
		return documentService.getLastDocumentoSummaryByEstimacionFlujoCajaType(idProgramaAno, tipoDocumento);

	}

	//Obtiene el id del programa para el a�o siguiente para hacer la estimacion del flujo de caja.
	public Integer obtenerIdProgramaAno(Integer id) {
		// TODO Auto-generated method stub
		ProgramaAno programaAnoActual = programasDAO.getProgramaAnoByID(id);

		// Obtener el siguiente programa ano.
		ProgramaAno programaAnoSiguiente = programasDAO
				.getProgramaAnoByIDProgramaAno(programaAnoActual.getPrograma()
						.getId(), programaAnoActual.getAno().getAno()+1);

		Integer idProgramaAnoNuevo = 0;
		// CREAMOS EL PROGRAMA PARA EL A�O SIGUIENTE SI NO EXISTE.
		if (programaAnoSiguiente == null) {


			AnoEnCurso anoCurso = programasDAO.getAnoEnCursoById(programaAnoActual.getAno().getAno() + 1);
			if (anoCurso==null){
				AnoEnCurso anoC = new AnoEnCurso();
				anoC.setAno(programaAnoActual.getAno().getAno() + 1);
				anoC.setAsignacionAdultoMayor(0);
				anoC.setInflactor(0.0);
				anoC.setMontoPercapitalBasal(0);

				anoCurso = programasDAO.saveAnoCurso(anoC);
			}

			EstadoPrograma estadoProgramaCurso = new EstadoPrograma(EstadosProgramas.ENCURSO.getId());
			EstadoPrograma estadoProgramaIniciar = new EstadoPrograma(EstadosProgramas.SININICIAR.getId());
			programaAnoActual.setAno(anoCurso);
			programaAnoActual.setEstado(estadoProgramaIniciar);
			programaAnoActual.setEstadoFlujoCaja(estadoProgramaCurso);
			programaAnoActual.setIdProgramaAno(null);

			idProgramaAnoNuevo = programasDAO.saveProgramaAno(programaAnoActual,true);
			// estadoPrograma );
		}
		else
		{
			idProgramaAnoNuevo = programaAnoSiguiente.getIdProgramaAno();
		}

		return idProgramaAnoNuevo;
	}

	public Integer enviarOrdinarioFONASA(Integer idLineaProgramatica) {
		//System.out.println("enviarDecretoAporteEstatal--> "+borradorAporteEstatalId+" username="+username);

		ReferenciaDocumentoSummaryVO referenciaDocumentoSummary = getLastDocumentSummaryEstimacionFlujoCajaType(idLineaProgramatica, TipoDocumentosProcesos.PLANTILLABORRADORORDINARIOPROGRAMACIONCAJA);
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
		//adjunto.set

	}

	public List<SubtituloFlujoCajaVO> getMonitoreoByProgramaAnoComponenteSubtitulo(Integer IdProgramaAno, Integer idComponente, Subtitulo subtitulo) {
		List<SubtituloFlujoCajaVO> subtitulosFlujosCaja = new ArrayList<SubtituloFlujoCajaVO>(); 
		List<Caja> flujosCaja = cajaDAO.getMonitoreoByProgramaAnoComponenteSubtitulo(IdProgramaAno, idComponente, subtitulo);
		if(flujosCaja != null && flujosCaja.size() > 0){
			for(Caja caja : flujosCaja){
				subtitulosFlujosCaja.add(new SubtituloFlujoCajaMapper().getBasic(caja, Integer.parseInt(getMesCurso(true))));
			}
		}
		return subtitulosFlujosCaja;
	}

	public List<SubtituloFlujoCajaVO> getConvenioRemesaByProgramaAnoComponenteSubtitulo(Integer idProgramaAno, Integer idComponente, Subtitulo subtitulo) {
		List<SubtituloFlujoCajaVO> subtitulosFlujosCaja = new ArrayList<SubtituloFlujoCajaVO>(); 
		List<Caja> flujosCaja = cajaDAO.getConvenioRemesaByProgramaAnoComponenteSubtitulo(idProgramaAno, idComponente, subtitulo);
		if(flujosCaja != null && flujosCaja.size() > 0){
			for(Caja caja : flujosCaja){
				subtitulosFlujosCaja.add(new ConvenioRemesaSubtituloFlujoCajaMapper().getBasic(caja, Integer.parseInt(getMesCurso(true))));
			}
		}
		return subtitulosFlujosCaja;
	}

}
