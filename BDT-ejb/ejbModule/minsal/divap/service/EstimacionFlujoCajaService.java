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
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.NamedQuery;

import minsal.divap.dao.AntecedentesComunaDAO;
import minsal.divap.dao.CajaDAO;
import minsal.divap.dao.ConveniosDAO;
import minsal.divap.dao.DocumentDAO;
import minsal.divap.dao.DistribucionInicialPercapitaDAO;
import minsal.divap.dao.EstimacionFlujoCajaDAO;
import minsal.divap.dao.MesDAO;
import minsal.divap.dao.ProgramasDAO;
import minsal.divap.dao.RemesaDAO;
import minsal.divap.dao.SeguimientoDAO;
import minsal.divap.dao.ServicioSaludDAO;
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.doc.GeneradorWord;
import minsal.divap.doc.GeneradorWordBorradorAporteEstatal;
import minsal.divap.enums.BusinessProcess;
import minsal.divap.enums.EstadosProgramas;
import minsal.divap.enums.Subtitulo;
import minsal.divap.enums.TareasSeguimiento;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.excel.GeneradorExcel;
import minsal.divap.excel.impl.EstimacionFlujoCajaConsolidadorSheetExcel;
import minsal.divap.excel.impl.EstimacionFlujoCajaSheetExcel;
import minsal.divap.excel.impl.EstimacionFlujoCajaSubtituloSheetExcel;
import minsal.divap.model.mappers.FlujoCajaMapper;
import minsal.divap.model.mappers.SubtituloFlujoCajaMapper;
import minsal.divap.util.Util;
import minsal.divap.vo.AsignacionDistribucionPerCapitaVO;
import minsal.divap.vo.BaseVO;
import minsal.divap.vo.BodyVO;
import minsal.divap.vo.CajaMesVO;
import minsal.divap.vo.CajaMontoSummaryVO;
import minsal.divap.vo.CellExcelVO;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ConveniosSummaryVO;
import minsal.divap.vo.DocumentoVO;
import minsal.divap.vo.EmailVO;
import minsal.divap.vo.FlujoCajaVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.ResumenConsolidadorVO;
import minsal.divap.vo.SeguimientoVO;
import minsal.divap.vo.ServiciosVO;
import minsal.divap.vo.SubtituloFlujoCajaVO;
import minsal.divap.vo.SubtituloVO;
import minsal.divap.vo.TransferenciaSummaryVO;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import cl.minsal.divap.model.AnoEnCurso;
import cl.minsal.divap.model.AntecendentesComunaCalculado;
import cl.minsal.divap.model.Caja;
import cl.minsal.divap.model.CajaMonto;
import cl.minsal.divap.model.CajaMontoPK;
import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.Convenio;
import cl.minsal.divap.model.DistribucionInicialPercapita;
import cl.minsal.divap.model.DocumentoEstimacionflujocaja;
import cl.minsal.divap.model.EstadoPrograma;
import cl.minsal.divap.model.MarcoPresupuestario;
import cl.minsal.divap.model.Mes;
import cl.minsal.divap.model.MontoMes;
import cl.minsal.divap.model.Programa;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.ProgramaMunicipalCoreComponente;
import cl.minsal.divap.model.ProgramaServicioCoreComponente;
import cl.minsal.divap.model.ReferenciaDocumento;
import cl.minsal.divap.model.Remesa;
import cl.minsal.divap.model.Seguimiento;
import cl.minsal.divap.model.ServicioSalud;
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
	private ProgramasService programasService;
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

	@Resource(name = "folderDocEstimacionFlujoCaja")
	private String folderDocEstimacionFlujoCaja;
	@EJB
	private UsuarioDAO usuarioDAO;
	@EJB
	private SeguimientoService seguimientoService;
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
	private ProgramasDAO programasDAO;
	@EJB
	private EmailService emailService;
	@EJB
	private ConveniosDAO conveniosDAO;
	@EJB
	private RemesaDAO remesasDAO;
	@EJB
	private ServicioSaludDAO servicioSaludDAO;

	// Generar documento
	public Integer elaborarOrdinarioProgramacionCaja() {

		

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
		

			TipoDocumento tipoDocumento = new TipoDocumento(
					TipoDocumentosProcesos.PLANTILLABORRADORORDINARIOPROGRAMACIONCAJA
					.getId());
			plantillaBorradorOrdinarioProgramacionCaja = documentService
					.createDocumentOrdinarioProgramacióndeCaja(tipoDocumento, response.getNodeRef(),
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
									CajaMontoPK cajaMontoPK = new CajaMontoPK(cajaActual.getId(), mes.getIdMes());
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
			DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(getAnoCurso());
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
			EstimacionFlujoCajaSheetExcel estimacionFlujoCajaSheetExcel = new EstimacionFlujoCajaSheetExcel(headers, servicios);
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
		
		Integer referenciaDocId = 0;
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
		return 1;
	}

	public List<SeguimientoVO> getBitacora(TareasSeguimiento tareaSeguimiento) {
		return seguimientoService.getBitacoraEstimacionFlujoCaja(tareaSeguimiento);
	}

	
	public Integer generarPlanillaPropuestaConsolidador(String username) {
		Integer planillaTrabajoId = null;
		//obtengo todos los programas del usuario
		
		//obtengo lista de servicios
		Integer mes = Integer.parseInt(getMesCurso(true));
		List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
		
		List<ResumenConsolidadorVO> resumenConsolidadorSubtitulo21 = new ArrayList<ResumenConsolidadorVO>();
		List<ResumenConsolidadorVO> resumenConsolidadorSubtitulo22 = new ArrayList<ResumenConsolidadorVO>();
		List<ResumenConsolidadorVO> resumenConsolidadorSubtitulo24 = new ArrayList<ResumenConsolidadorVO>();
		List<ResumenConsolidadorVO> resumenConsolidadorSubtitulo29 = new ArrayList<ResumenConsolidadorVO>();
		
		List<ProgramaAno> programasSubtitulo21 = programasDAO.getProgramasBySubtitulo(getAnoCurso(), Subtitulo.SUBTITULO21);
		List<ProgramaAno> programasSubtitulo22 = programasDAO.getProgramasBySubtitulo(getAnoCurso(), Subtitulo.SUBTITULO22);
		List<ProgramaAno> programasSubtitulo24 = programasDAO.getProgramasBySubtitulo(getAnoCurso(), Subtitulo.SUBTITULO24);
		List<ProgramaAno> programasSubtitulo29 = programasDAO.getProgramasBySubtitulo(getAnoCurso(), Subtitulo.SUBTITULO29);
		
		List<CellExcelVO> header = new ArrayList<CellExcelVO>();
		List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
		header.add(new CellExcelVO("COD SS", 1, 2));
		header.add(new CellExcelVO("SERVICIOS DE SALUD", 1, 2));
		
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator + "planillaPropuestaEstimacionFlujoCajaConsolidador.xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());
		
		GeneradorExcel generadorExcel = new GeneradorExcel(filename);
		
		if(servicios != null && servicios.size() > 0){
			for(ServicioSalud servicioSalud : servicios){
				ResumenConsolidadorVO resumenConsolidadorVO = new ResumenConsolidadorVO();
				resumenConsolidadorVO.setCodigoServicio(servicioSalud.getId().toString());
				resumenConsolidadorVO.setServicio(servicioSalud.getNombre());
				EstimacionFlujoCajaConsolidadorSheetExcel estimacionFlujoCajaConsolidadorSheetExcel = null;
				if(programasSubtitulo21 != null && programasSubtitulo21.size() > 0){
					List<Long> montos = new ArrayList<Long>();
					header.add(new CellExcelVO("SUBTÍTULO 21", programasSubtitulo21.size(), 1));			
					for(ProgramaAno programaAno : programasSubtitulo21) {
						subHeader.add(new CellExcelVO(programaAno.getPrograma().getNombre(), 1, 1));
					}
					subHeader.add(new CellExcelVO("TOTAL REF. SERVICIOS SUBT. 21", 1, 1));
					estimacionFlujoCajaConsolidadorSheetExcel = new EstimacionFlujoCajaConsolidadorSheetExcel(header, subHeader, null);
					for(ProgramaAno programaAno : programasSubtitulo21) {
						List<Caja> cajas = cajaDAO.getByProgramaAnoServicioSubtitulo(programaAno.getIdProgramaAno(), servicioSalud.getId(), Subtitulo.SUBTITULO21);
						if(cajas != null && cajas.size() > 0){
							for(Caja caja : cajas) {
								if(caja.getCajaMontos() != null && caja.getCajaMontos().size() > 0){
									Long totalMes = 0L;
									for(CajaMonto cajaMonto : caja.getCajaMontos()){
										if(mes.equals(cajaMonto.getMes().getIdMes())){
											totalMes+=cajaMonto.getMonto().getMonto();
										}
									}
									montos.add(totalMes);
								}
							}
						}
					}
					resumenConsolidadorVO.setMontos(montos);
					resumenConsolidadorSubtitulo21.add(resumenConsolidadorVO);
				}
				estimacionFlujoCajaConsolidadorSheetExcel.setItems(resumenConsolidadorSubtitulo21);
				generadorExcel.addSheet(estimacionFlujoCajaConsolidadorSheetExcel, getMesCurso(false));
			}
			for(int i=0;i<subHeader.size();i++){
				System.out.println("subHeader.get(i) ---> "+subHeader.get(i));
			}
		}
		
 
	
		
		
		//estimacionFlujoCajaSubtituloSheetExcel.setItems(flujoCajaSub22);
		
		
	
		//String mes = getMesCurso(false);
		
		
		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel
					.saveExcel(), contenType, folderDocEstimacionFlujoCaja
					.replace("{ANO}", getAnoCurso().toString()));
			System.out
			.println("response planillaPropuestaEstimacionFlujoCajaConsolidador --->"
					+ response);


			TipoDocumento tipoDocumento = new TipoDocumento(
					TipoDocumentosProcesos.PLANTILLAPROPUESTA.getId());
			planillaTrabajoId = documentService
					.createDocumentPropuestaConsolidador(tipoDocumento, response.getNodeRef(),
							response.getFileName(), contenType, getAnoCurso(), Integer.parseInt(getMesCurso(true)));
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("planillaTrabajoId --> "+planillaTrabajoId);
		
		return planillaTrabajoId;
		
	}
	
	
	public Integer generarPlanillaPropuesta(Integer idProgramaAno) {
		Integer planillaTrabajoId = null;
		ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idProgramaAno);
		List<Integer> idComponentes = new ArrayList<Integer>();
		for(Componente componente : programaAno.getPrograma().getComponentes()){
			idComponentes.add(componente.getId());
		}
		DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(getAnoCurso());
		System.out.println("distribucionInicialPercapita.getIdDistribucionInicialPercapita()-->"+distribucionInicialPercapita.getIdDistribucionInicialPercapita());
		List<SubtituloFlujoCajaVO> flujoCajaPercapita = new ArrayList<SubtituloFlujoCajaVO>();
		List<Object[]> percapitaServicios = antecedentesComunaDAO.groupPercapitaServicioByDistribucionInicialPercapita(distribucionInicialPercapita.getIdDistribucionInicialPercapita());
		System.out.println("percapitaServicios-->"+percapitaServicios);
		if(percapitaServicios != null && percapitaServicios.size() > 0){
			for(Object[] percapitaServicio : percapitaServicios){
				Integer idServicio = ((Number)(percapitaServicio[0])).intValue();
				Long percapitaAno = ((Number)(percapitaServicio[1])).longValue();
				flujoCajaPercapita.add(getPercapitaSubtituloFlujoCajaVO(idServicio, percapitaAno));
			}
		}
		
		List<SubtituloFlujoCajaVO> flujoCajaSub21 = getMonitoreoByProgramaAnoComponenteSubtitulo(programaAno.getIdProgramaAno(), idComponentes, Subtitulo.SUBTITULO21, true);
		List<SubtituloFlujoCajaVO> flujoCajaSub22 = getMonitoreoByProgramaAnoComponenteSubtitulo(programaAno.getIdProgramaAno(), idComponentes, Subtitulo.SUBTITULO22, true);
		List<SubtituloFlujoCajaVO> flujoCajaSub24 = getMonitoreoByProgramaAnoComponenteSubtitulo(programaAno.getIdProgramaAno(), idComponentes, Subtitulo.SUBTITULO24, true);
		List<SubtituloFlujoCajaVO> flujoCajaSub29 = getMonitoreoByProgramaAnoComponenteSubtitulo(programaAno.getIdProgramaAno(), idComponentes, Subtitulo.SUBTITULO29, true);

		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator + "planillaPropuestaEstimacionFlujoCaja.xlsx";
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
		if(flujoCajaPercapita != null && !flujoCajaPercapita.isEmpty()){
			header.add(new CellExcelVO("APORTE ESTATAL AÑO " + anoCurso, 1, 2));
			SubtituloFlujoCajaVO totalSubtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
			totalSubtituloFlujoCajaVO.setServicio("Total");
			Integer totalEnero=0, totalFebrero=0, totalMarzo=0, totalAbril=0, totalMayo=0, totalJunio=0, totalJulio=0;
			Integer totalAgosto=0, totalSeptiembre=0, totalOctubre=0, totalNoviembre=0, totalDiciembre=0, totalSubtituloPercapita=0;
			for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : flujoCajaPercapita){ 
				totalEnero+=subtituloFlujoCajaVO.getCajaMontos().get(0).getMontoMes();
				totalFebrero+=subtituloFlujoCajaVO.getCajaMontos().get(1).getMontoMes();
				totalMarzo+=subtituloFlujoCajaVO.getCajaMontos().get(2).getMontoMes();
				totalAbril+=subtituloFlujoCajaVO.getCajaMontos().get(3).getMontoMes();
				totalMayo+=subtituloFlujoCajaVO.getCajaMontos().get(4).getMontoMes();
				totalJunio+=subtituloFlujoCajaVO.getCajaMontos().get(5).getMontoMes();
				totalJulio+=subtituloFlujoCajaVO.getCajaMontos().get(6).getMontoMes();
				totalAgosto+=subtituloFlujoCajaVO.getCajaMontos().get(7).getMontoMes();
				totalSeptiembre+=subtituloFlujoCajaVO.getCajaMontos().get(8).getMontoMes();
				totalOctubre+=subtituloFlujoCajaVO.getCajaMontos().get(9).getMontoMes();
				totalNoviembre+=subtituloFlujoCajaVO.getCajaMontos().get(10).getMontoMes();
				totalDiciembre+=subtituloFlujoCajaVO.getCajaMontos().get(11).getMontoMes();
				totalSubtituloPercapita+=subtituloFlujoCajaVO.getTotalMontos();
			}
			totalSubtituloFlujoCajaVO.getCajaMontos().get(0).setMontoMes(totalEnero);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(1).setMontoMes(totalFebrero);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(2).setMontoMes(totalMarzo);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(3).setMontoMes(totalAbril);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(4).setMontoMes(totalMayo);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(5).setMontoMes(totalJunio);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(6).setMontoMes(totalJulio);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(7).setMontoMes(totalAgosto);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(8).setMontoMes(totalSeptiembre);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(9).setMontoMes(totalOctubre);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(10).setMontoMes(totalNoviembre);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(11).setMontoMes(totalDiciembre);
			totalSubtituloFlujoCajaVO.setTotalMontos(totalSubtituloPercapita);
			flujoCajaPercapita.add(totalSubtituloFlujoCajaVO);
			EstimacionFlujoCajaSubtituloSheetExcel estimacionFlujoCajaSubtituloSheetExcel = new EstimacionFlujoCajaSubtituloSheetExcel(header, subHeader, null);
			estimacionFlujoCajaSubtituloSheetExcel.setItems(flujoCajaPercapita);
			generadorExcel.addSheet(estimacionFlujoCajaSubtituloSheetExcel, "Per Cápita");
		}

		if(flujoCajaSub21 != null && !flujoCajaSub21.isEmpty()){
			header.add(new CellExcelVO("TOTAL " + programaAno.getPrograma().getNombre() + " SUBT. 21 " + anoCurso, 1, 2));
			SubtituloFlujoCajaVO totalSubtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
			totalSubtituloFlujoCajaVO.setServicio("Total");
			Integer totalEnero=0, totalFebrero=0, totalMarzo=0, totalAbril=0, totalMayo=0, totalJunio=0, totalJulio=0;
			Integer totalAgosto=0, totalSeptiembre=0, totalOctubre=0, totalNoviembre=0, totalDiciembre=0, totalSubtitulo21=0;
			for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : flujoCajaSub21){ 
				totalEnero+=subtituloFlujoCajaVO.getCajaMontos().get(0).getMontoMes();
				totalFebrero+=subtituloFlujoCajaVO.getCajaMontos().get(1).getMontoMes();
				totalMarzo+=subtituloFlujoCajaVO.getCajaMontos().get(2).getMontoMes();
				totalAbril+=subtituloFlujoCajaVO.getCajaMontos().get(3).getMontoMes();
				totalMayo+=subtituloFlujoCajaVO.getCajaMontos().get(4).getMontoMes();
				totalJunio+=subtituloFlujoCajaVO.getCajaMontos().get(5).getMontoMes();
				totalJulio+=subtituloFlujoCajaVO.getCajaMontos().get(6).getMontoMes();
				totalAgosto+=subtituloFlujoCajaVO.getCajaMontos().get(7).getMontoMes();
				totalSeptiembre+=subtituloFlujoCajaVO.getCajaMontos().get(8).getMontoMes();
				totalOctubre+=subtituloFlujoCajaVO.getCajaMontos().get(9).getMontoMes();
				totalNoviembre+=subtituloFlujoCajaVO.getCajaMontos().get(10).getMontoMes();
				totalDiciembre+=subtituloFlujoCajaVO.getCajaMontos().get(11).getMontoMes();
				totalSubtitulo21+=subtituloFlujoCajaVO.getTotalMontos();
			}
			totalSubtituloFlujoCajaVO.getCajaMontos().get(0).setMontoMes(totalEnero);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(1).setMontoMes(totalFebrero);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(2).setMontoMes(totalMarzo);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(3).setMontoMes(totalAbril);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(4).setMontoMes(totalMayo);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(5).setMontoMes(totalJunio);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(6).setMontoMes(totalJulio);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(7).setMontoMes(totalAgosto);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(8).setMontoMes(totalSeptiembre);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(9).setMontoMes(totalOctubre);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(10).setMontoMes(totalNoviembre);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(11).setMontoMes(totalDiciembre);
			totalSubtituloFlujoCajaVO.setTotalMontos(totalSubtitulo21);
			flujoCajaSub21.add(totalSubtituloFlujoCajaVO);
			EstimacionFlujoCajaSubtituloSheetExcel estimacionFlujoCajaSubtituloSheetExcel = new EstimacionFlujoCajaSubtituloSheetExcel(header, subHeader, null);
			estimacionFlujoCajaSubtituloSheetExcel.setItems(flujoCajaSub21);
			generadorExcel.addSheet(estimacionFlujoCajaSubtituloSheetExcel, "21");
		}
		if(flujoCajaSub22 != null && !flujoCajaSub22.isEmpty()){
			header.add(new CellExcelVO("TOTAL " + programaAno.getPrograma().getNombre() + " SUBT. 22 " + anoCurso, 1, 2));
			SubtituloFlujoCajaVO totalSubtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
			totalSubtituloFlujoCajaVO.setServicio("Total");
			Integer totalEnero=0, totalFebrero=0, totalMarzo=0, totalAbril=0, totalMayo=0, totalJunio=0, totalJulio=0;
			Integer totalAgosto=0, totalSeptiembre=0, totalOctubre=0, totalNoviembre=0, totalDiciembre=0, totalSubtitulo22=0;
			for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : flujoCajaSub22){ 
				totalEnero+=subtituloFlujoCajaVO.getCajaMontos().get(0).getMontoMes();
				totalFebrero+=subtituloFlujoCajaVO.getCajaMontos().get(1).getMontoMes();
				totalMarzo+=subtituloFlujoCajaVO.getCajaMontos().get(2).getMontoMes();
				totalAbril+=subtituloFlujoCajaVO.getCajaMontos().get(3).getMontoMes();
				totalMayo+=subtituloFlujoCajaVO.getCajaMontos().get(4).getMontoMes();
				totalJunio+=subtituloFlujoCajaVO.getCajaMontos().get(5).getMontoMes();
				totalJulio+=subtituloFlujoCajaVO.getCajaMontos().get(6).getMontoMes();
				totalAgosto+=subtituloFlujoCajaVO.getCajaMontos().get(7).getMontoMes();
				totalSeptiembre+=subtituloFlujoCajaVO.getCajaMontos().get(8).getMontoMes();
				totalOctubre+=subtituloFlujoCajaVO.getCajaMontos().get(9).getMontoMes();
				totalNoviembre+=subtituloFlujoCajaVO.getCajaMontos().get(10).getMontoMes();
				totalDiciembre+=subtituloFlujoCajaVO.getCajaMontos().get(11).getMontoMes();
				totalSubtitulo22+=subtituloFlujoCajaVO.getTotalMontos();
			}
			totalSubtituloFlujoCajaVO.getCajaMontos().get(0).setMontoMes(totalEnero);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(1).setMontoMes(totalFebrero);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(2).setMontoMes(totalMarzo);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(3).setMontoMes(totalAbril);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(4).setMontoMes(totalMayo);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(5).setMontoMes(totalJunio);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(6).setMontoMes(totalJulio);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(7).setMontoMes(totalAgosto);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(8).setMontoMes(totalSeptiembre);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(9).setMontoMes(totalOctubre);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(10).setMontoMes(totalNoviembre);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(11).setMontoMes(totalDiciembre);
			totalSubtituloFlujoCajaVO.setTotalMontos(totalSubtitulo22);
			flujoCajaSub22.add(totalSubtituloFlujoCajaVO);
			EstimacionFlujoCajaSubtituloSheetExcel estimacionFlujoCajaSubtituloSheetExcel = new EstimacionFlujoCajaSubtituloSheetExcel(header, subHeader, null);
			estimacionFlujoCajaSubtituloSheetExcel.setItems(flujoCajaSub22);
			generadorExcel.addSheet(estimacionFlujoCajaSubtituloSheetExcel, "22");
		}
		if(flujoCajaSub24 != null && !flujoCajaSub24.isEmpty()){
			header.add(new CellExcelVO("TOTAL " + programaAno.getPrograma().getNombre() + " SUBT. 24 " + anoCurso, 1, 2));
			SubtituloFlujoCajaVO totalSubtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
			totalSubtituloFlujoCajaVO.setServicio("Total");
			Integer totalEnero=0, totalFebrero=0, totalMarzo=0, totalAbril=0, totalMayo=0, totalJunio=0, totalJulio=0;
			Integer totalAgosto=0, totalSeptiembre=0, totalOctubre=0, totalNoviembre=0, totalDiciembre=0, totalSubtitulo24=0;
			for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : flujoCajaSub24){ 
				totalEnero+=subtituloFlujoCajaVO.getCajaMontos().get(0).getMontoMes();
				totalFebrero+=subtituloFlujoCajaVO.getCajaMontos().get(1).getMontoMes();
				totalMarzo+=subtituloFlujoCajaVO.getCajaMontos().get(2).getMontoMes();
				totalAbril+=subtituloFlujoCajaVO.getCajaMontos().get(3).getMontoMes();
				totalMayo+=subtituloFlujoCajaVO.getCajaMontos().get(4).getMontoMes();
				totalJunio+=subtituloFlujoCajaVO.getCajaMontos().get(5).getMontoMes();
				totalJulio+=subtituloFlujoCajaVO.getCajaMontos().get(6).getMontoMes();
				totalAgosto+=subtituloFlujoCajaVO.getCajaMontos().get(7).getMontoMes();
				totalSeptiembre+=subtituloFlujoCajaVO.getCajaMontos().get(8).getMontoMes();
				totalOctubre+=subtituloFlujoCajaVO.getCajaMontos().get(9).getMontoMes();
				totalNoviembre+=subtituloFlujoCajaVO.getCajaMontos().get(10).getMontoMes();
				totalDiciembre+=subtituloFlujoCajaVO.getCajaMontos().get(11).getMontoMes();
				totalSubtitulo24+=subtituloFlujoCajaVO.getTotalMontos();
			}
			totalSubtituloFlujoCajaVO.getCajaMontos().get(0).setMontoMes(totalEnero);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(1).setMontoMes(totalFebrero);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(2).setMontoMes(totalMarzo);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(3).setMontoMes(totalAbril);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(4).setMontoMes(totalMayo);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(5).setMontoMes(totalJunio);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(6).setMontoMes(totalJulio);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(7).setMontoMes(totalAgosto);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(8).setMontoMes(totalSeptiembre);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(9).setMontoMes(totalOctubre);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(10).setMontoMes(totalNoviembre);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(11).setMontoMes(totalDiciembre);
			totalSubtituloFlujoCajaVO.setTotalMontos(totalSubtitulo24);
			flujoCajaSub24.add(totalSubtituloFlujoCajaVO);
			EstimacionFlujoCajaSubtituloSheetExcel estimacionFlujoCajaSubtituloSheetExcel = new EstimacionFlujoCajaSubtituloSheetExcel(header, subHeader, null);
			estimacionFlujoCajaSubtituloSheetExcel.setItems(flujoCajaSub24);
			generadorExcel.addSheet(estimacionFlujoCajaSubtituloSheetExcel, "Ref. Mun.");
		}
		if(flujoCajaSub29 != null && !flujoCajaSub29.isEmpty()){
			header.add(new CellExcelVO("TOTAL " + programaAno.getPrograma().getNombre() + " SUBT. 29 " + anoCurso, 1, 2));
			SubtituloFlujoCajaVO totalSubtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
			totalSubtituloFlujoCajaVO.setServicio("Total");
			Integer totalEnero=0, totalFebrero=0, totalMarzo=0, totalAbril=0, totalMayo=0, totalJunio=0, totalJulio=0;
			Integer totalAgosto=0, totalSeptiembre=0, totalOctubre=0, totalNoviembre=0, totalDiciembre=0, totalSubtitulo29=0;
			for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : flujoCajaSub29){ 
				totalEnero+=subtituloFlujoCajaVO.getCajaMontos().get(0).getMontoMes();
				totalFebrero+=subtituloFlujoCajaVO.getCajaMontos().get(1).getMontoMes();
				totalMarzo+=subtituloFlujoCajaVO.getCajaMontos().get(2).getMontoMes();
				totalAbril+=subtituloFlujoCajaVO.getCajaMontos().get(3).getMontoMes();
				totalMayo+=subtituloFlujoCajaVO.getCajaMontos().get(4).getMontoMes();
				totalJunio+=subtituloFlujoCajaVO.getCajaMontos().get(5).getMontoMes();
				totalJulio+=subtituloFlujoCajaVO.getCajaMontos().get(6).getMontoMes();
				totalAgosto+=subtituloFlujoCajaVO.getCajaMontos().get(7).getMontoMes();
				totalSeptiembre+=subtituloFlujoCajaVO.getCajaMontos().get(8).getMontoMes();
				totalOctubre+=subtituloFlujoCajaVO.getCajaMontos().get(9).getMontoMes();
				totalNoviembre+=subtituloFlujoCajaVO.getCajaMontos().get(10).getMontoMes();
				totalDiciembre+=subtituloFlujoCajaVO.getCajaMontos().get(11).getMontoMes();
				totalSubtitulo29+=subtituloFlujoCajaVO.getTotalMontos();
			}
			totalSubtituloFlujoCajaVO.getCajaMontos().get(0).setMontoMes(totalEnero);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(1).setMontoMes(totalFebrero);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(2).setMontoMes(totalMarzo);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(3).setMontoMes(totalAbril);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(4).setMontoMes(totalMayo);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(5).setMontoMes(totalJunio);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(6).setMontoMes(totalJulio);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(7).setMontoMes(totalAgosto);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(8).setMontoMes(totalSeptiembre);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(9).setMontoMes(totalOctubre);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(10).setMontoMes(totalNoviembre);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(11).setMontoMes(totalDiciembre);
			totalSubtituloFlujoCajaVO.setTotalMontos(totalSubtitulo29);
			flujoCajaSub29.add(totalSubtituloFlujoCajaVO);
			EstimacionFlujoCajaSubtituloSheetExcel estimacionFlujoCajaSubtituloSheetExcel = new EstimacionFlujoCajaSubtituloSheetExcel(header, subHeader, null);
			estimacionFlujoCajaSubtituloSheetExcel.setItems(flujoCajaSub29);
			generadorExcel.addSheet(estimacionFlujoCajaSubtituloSheetExcel, "29");
		}


		//generadorExcel.addSheet(estimacionFlujoCajaResumenSheetExcel, "Resumen");


		//############################################################################################33

		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel
					.saveExcel(), contenType, folderDocEstimacionFlujoCaja
					.replace("{ANO}", getAnoCurso().toString()));
			System.out
			.println("response AsignacionRecursosPercapitaSheetExcel --->"
					+ response);


			TipoDocumento tipoDocumento = new TipoDocumento(
					TipoDocumentosProcesos.PLANTILLAPROPUESTA.getId());
			planillaTrabajoId = documentService
					.createDocumentPropuestaEstimacionFlujoCaja(programaAno,
							tipoDocumento, response.getNodeRef(),
							response.getFileName(), contenType);
			// DistribucionInicialPercapita distribucionInicialPercapita =
			// distribucionInicialPercapitaDAO.findById(idDistribucionInicialPercapita);
			// planillaTrabajoId =
			// documentService.createDocumentPercapita(distribucionInicialPercapita,
			// TipoDocumentosProcesos.PLANILLARESULTADOSCALCULADOS,
			// response.getNodeRef(), response.getFileName(), contenType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return planillaTrabajoId;
	}

	private SubtituloFlujoCajaVO getPercapitaSubtituloFlujoCajaVO(Integer idServicio, Long percapitaAno) {
		SubtituloFlujoCajaVO subtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
		ServiciosVO serviciosVO = servicioSaludService.getServicioSaludById(idServicio);
		subtituloFlujoCajaVO.setServicio(serviciosVO.getNombre_servicio());
		int idMes = 1;
		for(CajaMontoSummaryVO cajaMontoSummaryVO : subtituloFlujoCajaVO.getCajaMontos()){
			Mes mes = mesDAO.getMesPorID(idMes++);
			cajaMontoSummaryVO.setIdMes(mes.getIdMes());
			cajaMontoSummaryVO.setNombreMes(mes.getNombre());
			cajaMontoSummaryVO.setMontoMes((int)(percapitaAno/12));
		}
		return subtituloFlujoCajaVO;
	}

	private List<FlujoCajaVO> getFlujoCajaServicios(
			Integer idLineaProgramatica, Subtitulo subtitulo) {
		List<FlujoCajaVO> flujoCajas = new ArrayList<FlujoCajaVO>();
		List<Caja> cajas = estimacionFlujoCajaDAO.getFlujoCajaServicios(idLineaProgramatica, subtitulo);
		if(cajas != null && cajas.size() > 0) {
			for(Caja caja : cajas) {
				flujoCajas.add(new FlujoCajaMapper().getBasic(caja));	
			}
		}
		return flujoCajas;
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
	public void notificarUsuarioConsolidador(Integer idProgramaAno,
			String usuario, Integer idPlanillaMonitoreo) {
		// Iniciar el segundo proceso.
		
		System.out.println("idPlanillaMonitoreo --> "+idPlanillaMonitoreo);
		ProgramaAno programaAno = programasDAO
				.getProgramaAnoByID(idProgramaAno);
		programaAno.setEstadoFlujoCaja(new EstadoPrograma(
				EstadosProgramas.REVISADO.getId()));
		String mailTo = usuarioDAO.getEmailByUsername(usuario);
		EmailVO emailVO = new EmailVO();
		emailVO.setContent("Revisar Consolidación de Estimación de Flujos de Caja <p> para el programaAno "
				+ programaAno.getPrograma().getNombre()
				+ " que se encuentra disponible en su Bandeja de Tareas.</p>");
		emailVO.setSubject("Revisar Consolidación de Estimación de Flujos de Caja");
		emailVO.setTo(mailTo);
		emailService.sendMail(emailVO);
		System.out.println("Fin enviar mail consolidador");
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("usuario", usuario);
		parameters.put("idProgramaAno", idProgramaAno);
		parameters.put("idPlanillaMonitoreo", idPlanillaMonitoreo);		
		try {
			processService
					.startProcess(
							BusinessProcess.ESTIMACIONFLUJOCAJACONSOLIDADOR,
							parameters);
			System.out.println("Fin Iniciar el proceso consolidador");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Fin notificarUsuarioConsolidador");
		programaAno.setEstado(new EstadoPrograma(EstadosProgramas.FINALIZADO.getId()));	
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
	
	public ReferenciaDocumentoSummaryVO getLastDocumentSummaryEstimacionFlujoCajaTipoDocumento(TipoDocumentosProcesos tipoDocumento) {
		// TODO Auto-generated method stub
		return documentService.getLastDocumentoSummaryByEstimacionFlujoCajaTipoDocumento(tipoDocumento);

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

	public List<SubtituloFlujoCajaVO> getMonitoreoByProgramaAnoComponenteSubtituloServicio(Integer idProgramaAno, List<Integer> idComponentes, Subtitulo subtitulo, Boolean iniciarFlujoCaja, Integer idServicio) {
		
		
		List<SubtituloFlujoCajaVO> subtitulosFlujosCaja = new ArrayList<SubtituloFlujoCajaVO>();
		List<Caja> flujosCaja = cajaDAO.getMonitoreoByProgramaAnoComponenteSubtituloServicio(idProgramaAno, idComponentes, subtitulo, idServicio);
		if(flujosCaja != null && flujosCaja.size() > 0){
			for(Caja caja : flujosCaja){
				SubtituloFlujoCajaVO subtituloFlujoCajaVO = new SubtituloFlujoCajaMapper().getBasic(caja);
				int indexOf = subtitulosFlujosCaja.indexOf(subtituloFlujoCajaVO);
				if(indexOf == -1){
					subtitulosFlujosCaja.add(subtituloFlujoCajaVO);
				}else{
					for(int elemento = 0; elemento < subtituloFlujoCajaVO.getCajaMontos().size(); elemento++){
						int nuevoMonto = subtitulosFlujosCaja.get(indexOf).getCajaMontos().get(elemento).getMontoMes();
						nuevoMonto += subtituloFlujoCajaVO.getCajaMontos().get(elemento).getMontoMes();
						subtitulosFlujosCaja.get(indexOf).getCajaMontos().get(elemento).setMontoMes(nuevoMonto);
					}
				}

			}
		}
		if(!iniciarFlujoCaja){
			Integer mesActual = Integer.parseInt(getMesCurso(true));
			for(SubtituloFlujoCajaVO subtituloFlujoCaja : subtitulosFlujosCaja){
				ConveniosSummaryVO convenioRecibido = new ConveniosSummaryVO(0, 0);
				TransferenciaSummaryVO transferenciasAcumulada = new TransferenciaSummaryVO(0, 0);
				Integer marcoPresupuestario = subtituloFlujoCaja.getMarcoPresupuestario();
				List<Convenio> convenios = conveniosDAO.getConveniosSummaryByProgramaAnoComponenteSubtitulo(idProgramaAno, subtituloFlujoCaja.getIdServicio(), idComponentes, subtitulo);
				List<Remesa> remesas = remesasDAO.getRemesasSummaryByProgramaAnoComponenteSubtitulo(idProgramaAno, subtituloFlujoCaja.getIdServicio(), null, subtitulo);
				if(convenios != null && convenios.size() > 0){
					for(Convenio convenio : convenios){
						convenioRecibido.setMonto(convenioRecibido.getMonto() + ((convenio.getMonto() == null)?0:convenio.getMonto()));
					}
				}
				if(!convenioRecibido.getMonto().equals(0)){
					Integer porcentaje = (int)((convenioRecibido.getMonto() * 100.0)/marcoPresupuestario);
					convenioRecibido.setPorcentaje(porcentaje);
				}
				subtituloFlujoCaja.setConvenioRecibido(convenioRecibido);
				Integer montoRemesaMesActual = 0;
				if(remesas != null && remesas.size() > 0){
					for(Remesa remesa : remesas){
						if(remesa.getIdmes().getIdMes() <= mesActual){
							Integer montoRemesaMes = ((remesa.getValordia09() == null) ? 0 : remesa.getValordia09()) + ((remesa.getValordia24() == null) ? 0 : remesa.getValordia24()) +
									((remesa.getValordia28() == null) ? 0 : remesa.getValordia28());
							transferenciasAcumulada.setMonto(transferenciasAcumulada.getMonto() + montoRemesaMes);
							if(remesa.getIdmes().getIdMes() == mesActual){
								montoRemesaMesActual = montoRemesaMes;
							}
						}
					}
				}
				if(!transferenciasAcumulada.getMonto().equals(0)){
					Integer porcentaje = (int)((transferenciasAcumulada.getMonto() * 100.0)/marcoPresupuestario);
					transferenciasAcumulada.setPorcentaje(porcentaje);
				}
				subtituloFlujoCaja.setTransferenciaAcumulada(transferenciasAcumulada);
				for(CajaMontoSummaryVO cajaMontoSummaryVO : subtituloFlujoCaja.getCajaMontos()){
					if(mesActual.equals(cajaMontoSummaryVO.getIdMes()) ){
						cajaMontoSummaryVO.setMontoMes(montoRemesaMesActual);
						break;
					}
				}
			}
		}
		return subtitulosFlujosCaja;

	}
	
	
	public List<SubtituloFlujoCajaVO> getMonitoreoByProgramaAnoComponenteSubtitulo(Integer idProgramaAno, List<Integer> idComponentes, Subtitulo subtitulo, Boolean iniciarFlujoCaja) {
		List<SubtituloFlujoCajaVO> subtitulosFlujosCaja = new ArrayList<SubtituloFlujoCajaVO>(); 
		List<Caja> flujosCaja = cajaDAO.getMonitoreoByProgramaAnoComponenteSubtitulo(idProgramaAno, idComponentes, subtitulo);
		if(flujosCaja != null && flujosCaja.size() > 0){
			for(Caja caja : flujosCaja){
				SubtituloFlujoCajaVO subtituloFlujoCajaVO = new SubtituloFlujoCajaMapper().getBasic(caja);
				int indexOf = subtitulosFlujosCaja.indexOf(subtituloFlujoCajaVO);
				if(indexOf == -1){
					subtitulosFlujosCaja.add(subtituloFlujoCajaVO);
				}else{
					for(int elemento = 0; elemento < subtituloFlujoCajaVO.getCajaMontos().size(); elemento++){
						int nuevoMonto = subtitulosFlujosCaja.get(indexOf).getCajaMontos().get(elemento).getMontoMes();
						nuevoMonto += subtituloFlujoCajaVO.getCajaMontos().get(elemento).getMontoMes();
						subtitulosFlujosCaja.get(indexOf).getCajaMontos().get(elemento).setMontoMes(nuevoMonto);
					}
				}

			}
		}
		if(!iniciarFlujoCaja){
			Integer mesActual = Integer.parseInt(getMesCurso(true));
			for(SubtituloFlujoCajaVO subtituloFlujoCaja : subtitulosFlujosCaja){
				ConveniosSummaryVO convenioRecibido = new ConveniosSummaryVO(0, 0);
				TransferenciaSummaryVO transferenciasAcumulada = new TransferenciaSummaryVO(0, 0);
				Integer marcoPresupuestario = subtituloFlujoCaja.getMarcoPresupuestario();
				List<Convenio> convenios = conveniosDAO.getConveniosSummaryByProgramaAnoComponenteSubtitulo(idProgramaAno, subtituloFlujoCaja.getIdServicio(), idComponentes, subtitulo);
				List<Remesa> remesas = remesasDAO.getRemesasSummaryByProgramaAnoComponenteSubtitulo(idProgramaAno, subtituloFlujoCaja.getIdServicio(), null, subtitulo);
				if(convenios != null && convenios.size() > 0){
					for(Convenio convenio : convenios){
						convenioRecibido.setMonto(convenioRecibido.getMonto() + ((convenio.getMonto() == null)?0:convenio.getMonto()));
					}
				}
				if(!convenioRecibido.getMonto().equals(0)){
					Integer porcentaje = (int)((convenioRecibido.getMonto() * 100.0)/marcoPresupuestario);
					convenioRecibido.setPorcentaje(porcentaje);
				}
				subtituloFlujoCaja.setConvenioRecibido(convenioRecibido);
				Integer montoRemesaMesActual = 0;
				if(remesas != null && remesas.size() > 0){
					for(Remesa remesa : remesas){
						if(remesa.getIdmes().getIdMes() <= mesActual){
							Integer montoRemesaMes = ((remesa.getValordia09() == null) ? 0 : remesa.getValordia09()) + ((remesa.getValordia24() == null) ? 0 : remesa.getValordia24()) +
									((remesa.getValordia28() == null) ? 0 : remesa.getValordia28());
							transferenciasAcumulada.setMonto(transferenciasAcumulada.getMonto() + montoRemesaMes);
							if(remesa.getIdmes().getIdMes() == mesActual){
								montoRemesaMesActual = montoRemesaMes;
							}
						}
					}
				}
				if(!transferenciasAcumulada.getMonto().equals(0)){
					Integer porcentaje = (int)((transferenciasAcumulada.getMonto() * 100.0)/marcoPresupuestario);
					transferenciasAcumulada.setPorcentaje(porcentaje);
				}
				subtituloFlujoCaja.setTransferenciaAcumulada(transferenciasAcumulada);
				for(CajaMontoSummaryVO cajaMontoSummaryVO : subtituloFlujoCaja.getCajaMontos()){
					if(mesActual.equals(cajaMontoSummaryVO.getIdMes()) ){
						cajaMontoSummaryVO.setMontoMes(montoRemesaMesActual);
						break;
					}
				}
			}
		}
		return subtitulosFlujosCaja;
	}

	public List<SubtituloFlujoCajaVO> getConvenioRemesaByProgramaAnoComponenteSubtitulo(Integer idProgramaAno, List<Integer> idComponentes, Subtitulo subtitulo) {
		List<SubtituloFlujoCajaVO> subtitulosFlujosCaja = new ArrayList<SubtituloFlujoCajaVO>(); 
		List<Caja> flujosCaja = cajaDAO.getConvenioRemesaByProgramaAnoComponenteSubtitulo(idProgramaAno, idComponentes, subtitulo);
		if(flujosCaja != null && flujosCaja.size() > 0){
			for(Caja caja : flujosCaja){
				SubtituloFlujoCajaVO subtituloFlujoCajaVO = new SubtituloFlujoCajaMapper().getBasic(caja);
				int indexOf = subtitulosFlujosCaja.indexOf(subtituloFlujoCajaVO);
				if(indexOf == -1){
					subtitulosFlujosCaja.add(subtituloFlujoCajaVO);
				}else{
					for(int elemento = 0; elemento < subtituloFlujoCajaVO.getCajaMontos().size(); elemento++){
						int nuevoMonto = subtitulosFlujosCaja.get(indexOf).getCajaMontos().get(elemento).getMontoMes();
						nuevoMonto += subtituloFlujoCajaVO.getCajaMontos().get(elemento).getMontoMes();
						subtitulosFlujosCaja.get(indexOf).getCajaMontos().get(elemento).setMontoMes(nuevoMonto);
					}
				}
			}
		}
		Integer mesActual = Integer.parseInt(getMesCurso(true));
		for(SubtituloFlujoCajaVO subtituloFlujoCaja : subtitulosFlujosCaja){
			TransferenciaSummaryVO transferenciasAcumulada = new TransferenciaSummaryVO(0, 0);
			List<Remesa> remesas = remesasDAO.getRemesasSummaryByProgramaAnoComponenteSubtitulo(idProgramaAno, subtituloFlujoCaja.getIdServicio(), null, subtitulo);
			if((remesas != null) && (remesas.size() > 0) && (mesActual > 2)){
				int elementosConRemesa = 0;
				for(int elemento = 0; elemento < remesas.size(); elemento++){
					if(remesas.get(elemento).getIdmes().getIdMes() <= (mesActual - 2)){
						Integer montoRemesaMes = ((remesas.get(elemento).getValordia09() == null) ? 0 : remesas.get(elemento).getValordia09()) + ((remesas.get(elemento).getValordia24() == null) ? 0 : remesas.get(elemento).getValordia24()) +
								((remesas.get(elemento).getValordia28() == null) ? 0 : remesas.get(elemento).getValordia28());
						transferenciasAcumulada.setMonto(transferenciasAcumulada.getMonto() + montoRemesaMes);
						subtituloFlujoCaja.getCajaMontos().get(elemento).setMontoMes(montoRemesaMes);
						elementosConRemesa++;
					}
				}
				for(int elemento = elementosConRemesa; elemento < subtituloFlujoCaja.getCajaMontos().size(); elemento++){
					subtituloFlujoCaja.getCajaMontos().get(elemento).setMontoMes(0);
				}
			}else{
				for(CajaMontoSummaryVO cajaMontoSummaryVO : subtituloFlujoCaja.getCajaMontos()){
					cajaMontoSummaryVO.setMontoMes(0);
				}
			}
			subtituloFlujoCaja.setTransferenciaAcumulada(transferenciasAcumulada);
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

	public void actualizarMonitoreoServicioSubtituloFlujoCaja(Integer idProgramaAno, Integer IdServicio, CajaMontoSummaryVO cajaMontoSummary, Subtitulo subtitulo) {
		List<Caja> cajas = cajaDAO.getByProgramaAnoServicioSubtitulo(idProgramaAno, IdServicio, subtitulo);
		System.out.println("idProgramaAno ="+idProgramaAno+" IdServicio="+ IdServicio +" subtitulo="+subtitulo.getNombre());
		if(cajas != null && cajas.size() > 0){
			for(Caja caja : cajas){
				System.out.println("caja.getId()=" + caja.getId() + "caja.getIdComponente().getId()=" + caja.getIdComponente().getId() + "caja.getIdComponente().getNombre()=" + caja.getIdComponente().getNombre());
				float pesoComponente = caja.getIdComponente().getPeso();
				for(CajaMonto cajaMonto : caja.getCajaMontos()){
					if(cajaMontoSummary.getIdMes().equals(cajaMonto.getMes().getIdMes())){
						if(((int)pesoComponente) == 100){
							cajaMonto.getMonto().setMonto(cajaMontoSummary.getMontoMes());
						}else{
							int montoActualizado = (int)((pesoComponente * cajaMontoSummary.getMontoMes())/100.0);
							cajaMonto.getMonto().setMonto(montoActualizado);
						}
					}
				}
			}
		}
	}
	
	
	public List<SubtituloFlujoCajaVO> getPercapitaByProgramaAno(Integer idProgramaAno, Integer idServicio) {
		DistribucionInicialPercapita  distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(getAnoCurso());
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
			 List<CajaMontoSummaryVO> cajaMontos = new ArrayList<CajaMontoSummaryVO>();
			 Integer montoMensual = valor.intValue() / 12;
			 for(int i=0; i<12; i++){
				 CajaMontoSummaryVO caja = new CajaMontoSummaryVO();
				 caja.setIdMes(i+1);
				 caja.setMontoMes(montoMensual);
				 cajaMontos.add(i, caja);
			 }			 
			 
			 subtituloFlujoCajaVO.setCajaMontos(cajaMontos);
			 subtituloFlujoCajaVO.setMarcoPresupuestario(montoMensual*12);
			 resultado.add(subtituloFlujoCajaVO);
			 
		 } 
		
		 
		
		return resultado;
		
	}
public List<SubtituloFlujoCajaVO> getPercapitaByProgramaAno(Integer idProgramaAno) {
		
		DistribucionInicialPercapita  distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(getAnoCurso());
		Integer id_disperc = distribucionInicialPercapita.getIdDistribucionInicialPercapita();
		System.out.println("id_disperc --> "+id_disperc);
		
		
		List<AntecendentesComunaCalculado> antecendentesComunasCalculado = antecedentesComunaDAO.findAntecedentesComunaCalculadosByDistribucionInicialPercapita(distribucionInicialPercapita.getIdDistribucionInicialPercapita());
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
			 List<CajaMontoSummaryVO> cajaMontos = new ArrayList<CajaMontoSummaryVO>();
			 Integer montoMensual = valor.intValue() / 12;
			 for(int i=0; i<12; i++){
				 CajaMontoSummaryVO caja = new CajaMontoSummaryVO();
				 caja.setIdMes(i+1);
				 caja.setMontoMes(montoMensual);
				 cajaMontos.add(i, caja);
			 }			 
			 
			 subtituloFlujoCajaVO.setCajaMontos(cajaMontos);
			 subtituloFlujoCajaVO.setMarcoPresupuestario(montoMensual*12);
			 resultado.add(subtituloFlujoCajaVO);
			 
		 } 
		
		 
		
		return resultado;
	}

public DocumentoEstimacionflujocaja getDocumentByTypProgramaAno(Integer idProgramaAno, TipoDocumentosProcesos tipoDocumentoProceso){
	return documentDAO.getDocumentByTypProgramaAno(idProgramaAno, tipoDocumentoProceso);
	
}



	

}