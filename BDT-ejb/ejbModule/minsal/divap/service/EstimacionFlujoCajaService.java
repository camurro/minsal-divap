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
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import minsal.divap.dao.CajaDAO;
import minsal.divap.dao.EstimacionFlujoCajaDAO;
import minsal.divap.dao.ProgramasDAO;
import minsal.divap.dao.SeguimientoDAO;
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.doc.GeneradorWord;
import minsal.divap.doc.GeneradorWordBorradorAporteEstatal;
import minsal.divap.enums.Subtitulo;
import minsal.divap.enums.TareasSeguimiento;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.excel.GeneradorExcel;
import minsal.divap.excel.impl.EstimacionFlujoCajaSheetExcel;
import minsal.divap.excel.impl.EstimacionFlujoCajaSubtituloSheetExcel;
import minsal.divap.model.mappers.FlujoCajaMapper;
import minsal.divap.util.Util;
import minsal.divap.vo.AsignacionDistribucionPerCapitaVO;
import minsal.divap.vo.BaseVO;
import minsal.divap.vo.BodyVO;
import minsal.divap.vo.CajaMesVO;
import minsal.divap.vo.CellExcelVO;
import minsal.divap.vo.DocumentoVO;
import minsal.divap.vo.EmailVO;
import minsal.divap.vo.FlujoCajaVO;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.SeguimientoVO;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import cl.minsal.divap.model.AnoEnCurso;
import cl.minsal.divap.model.Caja;
import cl.minsal.divap.model.DocumentoEstimacionflujocaja;
import cl.minsal.divap.model.EstadoPrograma;
import cl.minsal.divap.model.ProgramaAno;
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
	private CajaDAO cajaDAO;

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

			ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idLineaProgramatica);
			TipoDocumento tipoDocumento = new TipoDocumento(TipoDocumentosProcesos.PLANTILLABORRADORORDINARIOPROGRAMACIONCAJA.getId());
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
	// valores del a�o pasado.
	public Integer calcularPropuesta(Integer idProgramaAno) {

		//Obtnemos los datos del programa ano anterior.
		System.out.println("calcularPropuesta idProgramaAno-->"+idProgramaAno);
//		ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idProgramaAno);
//		System.out.println("programaAno----->"+programaAno);
//		System.out.println("programaAno-->"+programaAno.getIdProgramaAno() +" nombre->" + programaAno.getPrograma().getNombre());

		//ProgramaAno programaAnoAnterior = programasDAO.getProgramaAnoByIDProgramaAno(programaAno.getPrograma().getId(), programaAno.getAno().getAno() -1);

		/*
		
		List<Caja> lst = cajaDAO.getByIDProgramaAno(programaAnoAnterior.getIdProgramaAno());
		if (lst.size()==0)
		{
			System.out.println("No se han encontrado datos para el a�o anterior.");
		}
		else
		{
			//Obtenemos el programa actual.

			for (Caja caja : lst) {
				// TODO: [ASAAVEDRA] Debo cambiar el programa ano.
				caja.setId(caja.getMonto());
				// caja.setIdPrograma(programaAnoSiguiente);
			}
			// Generamos la copia de los datos para el nuevo a�o
			//Generamos la copia de los datos para el nuevo a�o
			cajaDAO.save(lst);
		}
		*/
		return idProgramaAno;
	}

	public Integer getIdPlantillaProgramacion() {
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
			plantillaId = documentService
					.getDocumentoIdByPlantillaId(plantillaId);
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
		List<CajaMesVO> cajaMeses = new ArrayList<CajaMesVO>();
		List<FlujoCajaVO> flujoCajaSub21 = new ArrayList<FlujoCajaVO>();
		List<FlujoCajaVO> flujoCajaSub22 = new ArrayList<FlujoCajaVO>();
		
		List<CajaMesVO> cajaMesesResumen = new ArrayList<CajaMesVO>();
		List<FlujoCajaVO> flujoCajaResumen = new ArrayList<FlujoCajaVO>();
		
		List<FlujoCajaVO> flujoCajaSub24 = getFlujoCajaServicios(idLineaProgramatica, Subtitulo.SUBTITULO24);
		List<FlujoCajaVO> flujoCajaSub29 = getFlujoCajaServicios(idLineaProgramatica, Subtitulo.SUBTITULO29);
		for(int i=0;i<12;i++){
			
			CajaMesVO cajaMes = new CajaMesVO();
			
			
			//Para los totales
			
			
			long montoMes = 100000000;
			
			if(i == 0){
				cajaMes.setMes("enero");
				cajaMes.setMonto((long)montoMes - 5000000);
				cajaMeses.add(cajaMes);
			}
			else if(i == 1){
				cajaMes.setMes("febrero");
				cajaMes.setMonto((long)montoMes + 30000000);
				cajaMeses.add(cajaMes);
			}else if(i == 2){
				cajaMes.setMes("marzo");
				cajaMes.setMonto((long)montoMes);
				cajaMeses.add(cajaMes);
			}else if(i == 3){
				cajaMes.setMes("abril");
				cajaMes.setMonto((long)montoMes + 4000000);
				cajaMeses.add(cajaMes);
			}else if(i == 4){
				cajaMes.setMes("mayo");
				cajaMes.setMonto((long)montoMes + 12000000);
				cajaMeses.add(cajaMes);
			}else if(i == 5){
				cajaMes.setMes("junio");
				cajaMes.setMonto((long)montoMes);
				cajaMeses.add(cajaMes);
			}else if(i == 6){
				cajaMes.setMes("julio");
				cajaMes.setMonto((long)montoMes - 2000000);
				cajaMeses.add(cajaMes);
			}else if(i == 7){
				cajaMes.setMes("agosto");
				cajaMes.setMonto((long)montoMes + 3500000);
				cajaMeses.add(cajaMes);
			}else if(i == 8){
				cajaMes.setMes("septiembre");
				cajaMes.setMonto((long)montoMes - 3500000);
				cajaMeses.add(cajaMes);
			}else if(i == 9){
				cajaMes.setMes("octubre");
				cajaMes.setMonto((long)montoMes - 2000000);
				cajaMeses.add(cajaMes);
			}else if(i == 10){
				cajaMes.setMes("noviembre");
				cajaMes.setMonto((long)montoMes - 25000000);
				cajaMeses.add(cajaMes);
			}else if(i == 11){
				cajaMes.setMes("diciembre");
				cajaMes.setMonto((long)montoMes + 1000000 );
				cajaMeses.add(cajaMes);
			}
		}
		
		for (int i=0; i<5; i++){
			FlujoCajaVO flujo1 = new FlujoCajaVO();
			if(i==1){
				flujo1.setServicio("Santiago");
				flujo1.setSubtitulo(cajaMeses);
				flujoCajaSub21.add(flujo1);
				flujoCajaSub22.add(flujo1);
				
				//al final setear montoSubtitulo
			}
			if(i==2){
				flujo1.setServicio("Curico");
				flujo1.setSubtitulo(cajaMeses);
				flujoCajaSub21.add(flujo1);
				flujoCajaSub22.add(flujo1);
				//al final setear montoSubtitulo
			}
			if(i==3){
				flujo1.setServicio("Talca");
				flujo1.setSubtitulo(cajaMeses);
				flujoCajaSub21.add(flujo1);
				flujoCajaSub22.add(flujo1);
				//al final setear montoSubtitulo
			}
			if(i==4){
				flujo1.setServicio("Concepcion");
				flujo1.setSubtitulo(cajaMeses);
				flujoCajaSub21.add(flujo1);
				flujoCajaSub22.add(flujo1);
				//al final setear montoSubtitulo
			}
			if(i==5){
				flujo1.setServicio("Punta Arenas");
				flujo1.setSubtitulo(cajaMeses);
				flujoCajaSub21.add(flujo1);
				flujoCajaSub22.add(flujo1);
				//al final setear montoSubtitulo
			}
			
		}
		
		
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator
				+ "planillaPropuestaEstimacionFlujoCaja.xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());
		GeneradorExcel generadorExcel = new GeneradorExcel(filename);
		List<CellExcelVO> header = new ArrayList<CellExcelVO>();
		List<FlujoCajaVO> items = null;	
		List<FlujoCajaVO> totalItems = null;
		
		
		header.add(new CellExcelVO("SERVICIOS DE SALUD", 1, 2));
		header.add(new CellExcelVO("CHILE CRECE CONTIGO", 12, 1));
		//header.add(new CellExcelVO("TOTAL CHILE CRECE CONTIGO SUBT. XX AÑO 2014", 1, 2));
		
		List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
		
		subHeader.add(new CellExcelVO("ENERO REAL", 1, 1));
		subHeader.add(new CellExcelVO("FEBRERO REAL", 1, 1));
		subHeader.add(new CellExcelVO("MARZO REAL", 1, 1));
		subHeader.add(new CellExcelVO("ABRIL", 1, 1));
		subHeader.add(new CellExcelVO("MAYO", 1, 1));
		subHeader.add(new CellExcelVO("JUNIO", 1, 1));
		subHeader.add(new CellExcelVO("JULIO", 1, 1));
		subHeader.add(new CellExcelVO("AGOSTO", 1, 1));
		subHeader.add(new CellExcelVO("SEPTIEMBRE", 1, 1));
		subHeader.add(new CellExcelVO("OCTUBRE", 1, 1));
		subHeader.add(new CellExcelVO("NOVIEMBRE", 1, 1));
		subHeader.add(new CellExcelVO("DICIEMBRE", 1, 1));
		
		List<CellExcelVO> headerTotales = new ArrayList<CellExcelVO>();
		headerTotales.add(new CellExcelVO("SERVICIOS DE SALUD", 1, 2));
		headerTotales.add(new CellExcelVO("ENERO REAL", 1, 1));
		headerTotales.add(new CellExcelVO("FEBRERO REAL", 1, 1));
		headerTotales.add(new CellExcelVO("MARZO REAL", 1, 1));
		headerTotales.add(new CellExcelVO("ABRIL", 1, 1));
		headerTotales.add(new CellExcelVO("MAYO", 1, 1));
		headerTotales.add(new CellExcelVO("JUNIO", 1, 1));
		headerTotales.add(new CellExcelVO("JULIO", 1, 1));
		headerTotales.add(new CellExcelVO("AGOSTO", 1, 1));
		headerTotales.add(new CellExcelVO("SEPTIEMBRE", 1, 1));
		headerTotales.add(new CellExcelVO("OCTUBRE", 1, 1));
		headerTotales.add(new CellExcelVO("NOVIEMBRE", 1, 1));
		headerTotales.add(new CellExcelVO("DICIEMBRE", 1, 1));
		headerTotales.add(new CellExcelVO("TOTAL MENSUAL APS SUBT. 24, 21, 22 Y 29 AÑO 2014 ($)", 1, 2));
		
		List<CellExcelVO> subHeaderTotales = new ArrayList<CellExcelVO>();
		for(int i=0;i<12;i++){
			subHeaderTotales.add(new CellExcelVO("TOTAL MENSUAL", 1, 1));
		}
		
		
		long totalenero=0;
		long totalfebrero=0;
		long totalmarzo=0;
		long totalabril=0;
		long totalmayo=0;
		long totaljunio=0;
		long totaljulio=0;
		long totalagosto=0;
		long totalseptiembre=0;
		long totaloctubre=0;
		long totalnoviembre=0;
		long totaldiciembre=0;

			
		
		EstimacionFlujoCajaSubtituloSheetExcel estimacionFlujoCajaSubtituloSheetExcel = new EstimacionFlujoCajaSubtituloSheetExcel(header, subHeader, items);
		
		EstimacionFlujoCajaSubtituloSheetExcel estimacionFlujoCajaResumenSheetExcel = new EstimacionFlujoCajaSubtituloSheetExcel(headerTotales, subHeaderTotales, totalItems);
				
		
		CajaMesVO cajaMesResumen = new CajaMesVO();
		
		if(flujoCajaSub21 != null && !flujoCajaSub21.isEmpty()){
			header.add(new CellExcelVO("TOTAL CHILE CRECE \nCONTIGO SUBT. 21 AÑO 2014", 1, 2));
			estimacionFlujoCajaSubtituloSheetExcel.setItems(flujoCajaSub21);
			totalItems = estimacionFlujoCajaSubtituloSheetExcel.getItems();
			for(int i=0;i<totalItems.size();i++){				
				totalenero=totalItems.get(i).getSubtitulo().get(0).getMonto();
				totalfebrero+=totalItems.get(i).getSubtitulo().get(1).getMonto();
				totalmarzo+=totalItems.get(i).getSubtitulo().get(2).getMonto();
				totalabril+=totalItems.get(i).getSubtitulo().get(3).getMonto();
				totalmayo+=totalItems.get(i).getSubtitulo().get(4).getMonto();
				totaljunio+=totalItems.get(i).getSubtitulo().get(5).getMonto();
				totaljulio+=totalItems.get(i).getSubtitulo().get(6).getMonto();
				totalagosto+=totalItems.get(i).getSubtitulo().get(7).getMonto();
				totalseptiembre+=totalItems.get(i).getSubtitulo().get(8).getMonto();
				totaloctubre+=totalItems.get(i).getSubtitulo().get(9).getMonto();
				totalnoviembre+=totalItems.get(i).getSubtitulo().get(10).getMonto();
				totaldiciembre+=totalItems.get(i).getSubtitulo().get(11).getMonto();
			}
			
			
			generadorExcel.addSheet(estimacionFlujoCajaSubtituloSheetExcel, "21");
		}
		if(flujoCajaSub22 != null && !flujoCajaSub22.isEmpty()){
			header.add(new CellExcelVO("TOTAL CHILE CRECE CONTIGO SUBT. 22 AÑO 2014", 1, 2));
			estimacionFlujoCajaSubtituloSheetExcel.setItems(flujoCajaSub22);
			totalItems = estimacionFlujoCajaSubtituloSheetExcel.getItems();
			for(int i=0;i<totalItems.size();i++){				
				totalenero=totalItems.get(i).getSubtitulo().get(0).getMonto();
				totalfebrero+=totalItems.get(i).getSubtitulo().get(1).getMonto();
				totalmarzo+=totalItems.get(i).getSubtitulo().get(2).getMonto();
				totalabril+=totalItems.get(i).getSubtitulo().get(3).getMonto();
				totalmayo+=totalItems.get(i).getSubtitulo().get(4).getMonto();
				totaljunio+=totalItems.get(i).getSubtitulo().get(5).getMonto();
				totaljulio+=totalItems.get(i).getSubtitulo().get(6).getMonto();
				totalagosto+=totalItems.get(i).getSubtitulo().get(7).getMonto();
				totalseptiembre+=totalItems.get(i).getSubtitulo().get(8).getMonto();
				totaloctubre+=totalItems.get(i).getSubtitulo().get(9).getMonto();
				totalnoviembre+=totalItems.get(i).getSubtitulo().get(10).getMonto();
				totaldiciembre+=totalItems.get(i).getSubtitulo().get(11).getMonto();
			}
			
			generadorExcel.addSheet(estimacionFlujoCajaSubtituloSheetExcel, "22");
		}
		if(flujoCajaSub24 != null && !flujoCajaSub24.isEmpty()){
			header.add(new CellExcelVO("TOTAL CHILE CRECE CONTIGO SUBT. 24 AÑO 2014", 1, 2));
			estimacionFlujoCajaSubtituloSheetExcel.setItems(flujoCajaSub24);
			generadorExcel.addSheet(estimacionFlujoCajaSubtituloSheetExcel, "Ref. Mun.");
		}
		if(flujoCajaSub29 != null && !flujoCajaSub29.isEmpty()){
			header.add(new CellExcelVO("TOTAL CHILE CRECE CONTIGO SUBT. 29 AÑO 2014", 1, 2));
			estimacionFlujoCajaSubtituloSheetExcel.setItems(flujoCajaSub29);
			generadorExcel.addSheet(estimacionFlujoCajaSubtituloSheetExcel, "29");
		}
		
		for(int i=0; i<12;i++){
			if(i == 0){
				cajaMesResumen.setMes("enero");
				cajaMesResumen.setMonto((long)totalenero);
				cajaMesesResumen.add(cajaMesResumen);
			}
			else if(i == 1){
				cajaMesResumen.setMes("febrero");
				cajaMesResumen.setMonto((long)totalfebrero);
				cajaMesesResumen.add(cajaMesResumen);
			}else if(i == 2){
				cajaMesResumen.setMes("marzo");
				cajaMesResumen.setMonto((long)totalmarzo);
				cajaMesesResumen.add(cajaMesResumen);
			}else if(i == 3){
				cajaMesResumen.setMes("abril");
				cajaMesResumen.setMonto((long)totalabril);
				cajaMesesResumen.add(cajaMesResumen);
			}else if(i == 4){
				cajaMesResumen.setMes("mayo");
				cajaMesResumen.setMonto((long)totalmayo);
				cajaMesesResumen.add(cajaMesResumen);
			}else if(i == 5){
				cajaMesResumen.setMes("junio");
				cajaMesResumen.setMonto((long)totaljunio);
				cajaMesesResumen.add(cajaMesResumen);
			}else if(i == 6){
				cajaMesResumen.setMes("julio");
				cajaMesResumen.setMonto((long)totaljulio);
				cajaMesesResumen.add(cajaMesResumen);
			}else if(i == 7){
				cajaMesResumen.setMes("agosto");
				cajaMesResumen.setMonto((long)totalagosto);
				cajaMesesResumen.add(cajaMesResumen);
			}else if(i == 8){
				cajaMesResumen.setMes("septiembre");
				cajaMesResumen.setMonto((long)totalseptiembre);
				cajaMesesResumen.add(cajaMesResumen);
			}else if(i == 9){
				cajaMesResumen.setMes("octubre");
				cajaMesResumen.setMonto((long)totaloctubre);
				cajaMesesResumen.add(cajaMesResumen);
			}else if(i == 10){
				cajaMesResumen.setMes("noviembre");
				cajaMesResumen.setMonto((long)totalnoviembre);
				cajaMesesResumen.add(cajaMesResumen);
			}else if(i == 11){
				cajaMesResumen.setMes("diciembre");
				cajaMesResumen.setMonto((long)totaldiciembre);
				cajaMesesResumen.add(cajaMesResumen);
			}
		}
		for (int i=0; i<5; i++){
			FlujoCajaVO flujoResumen = new FlujoCajaVO();
			if(i==1){
				flujoResumen.setServicio("Santiago");
				flujoResumen.setSubtitulo(cajaMesesResumen);
				flujoCajaResumen.add(flujoResumen);
				
				//al final setear montoSubtitulo
			}
			if(i==2){
				flujoResumen.setServicio("Curico");
				flujoResumen.setSubtitulo(cajaMesesResumen);
				flujoCajaResumen.add(flujoResumen);
				//al final setear montoSubtitulo
			}
			if(i==3){
				flujoResumen.setServicio("Talca");
				flujoResumen.setSubtitulo(cajaMesesResumen);
				flujoCajaResumen.add(flujoResumen);
				//al final setear montoSubtitulo
			}
			if(i==4){
				flujoResumen.setServicio("Concepcion");
				flujoResumen.setSubtitulo(cajaMesesResumen);
				flujoCajaResumen.add(flujoResumen);
				//al final setear montoSubtitulo
			}
			if(i==5){
				flujoResumen.setServicio("Punta Arenas");
				flujoResumen.setSubtitulo(cajaMesesResumen);
				flujoCajaResumen.add(flujoResumen);
				//al final setear montoSubtitulo
			}
			
		}
		
		
		
		//generadorExcel.addSheet(estimacionFlujoCajaResumenSheetExcel, "Resumen");
		
		
		//############################################################################################33
		
		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel
					.saveExcel(), contenType, folderEstimacionFlujoCaja
					.replace("{ANO}", getAnoCurso().toString()));
			System.out
			.println("response AsignacionRecursosPercapitaSheetExcel --->"
					+ response);
			ProgramaAno programaAno = programasDAO
					.getProgramaAnoByID(idLineaProgramatica);
			
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

	public Integer notificarUsuarioConsolidador(Integer idLineaProgramatica) {
		EmailVO emailVO = new EmailVO();

		emailVO.setContent("ola");
		emailVO.setSubject("mensaje prueba");
		emailVO.setTo("asaavedra@nectia.com");
		emailService.sendMail(emailVO);
		return 1;
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

	public Integer enviarOrdinarioFONASA(Integer idLineaProgramatica) {
		// TODO [ASAAVEDRA] Enviar a usuario consolidador tambi�n.
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

}
