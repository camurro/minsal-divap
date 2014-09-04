package minsal.divap.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import cl.minsal.divap.model.AnoEnCurso;
import cl.minsal.divap.model.AntecendentesComuna;
import cl.minsal.divap.model.AntecendentesComunaCalculado;
import cl.minsal.divap.model.Caja;
import cl.minsal.divap.model.DistribucionInicialPercapita;
import cl.minsal.divap.model.DocumentoEstimacionflujocaja;
import cl.minsal.divap.model.EstadoPrograma;
import cl.minsal.divap.model.MarcoPresupuestario;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.ReferenciaDocumento;
import cl.minsal.divap.model.Seguimiento;
import cl.minsal.divap.model.TipoDocumento;
import cl.minsal.divap.model.Usuario;
import minsal.divap.dao.CajaDAO;
import minsal.divap.dao.DistribucionInicialPercapitaDAO;
import minsal.divap.dao.EstimacionFlujoCajaDAO;
import minsal.divap.dao.ProgramasDAO;
import minsal.divap.dao.SeguimientoDAO;
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.doc.GeneradorWord;
import minsal.divap.doc.GeneradorWordBorradorAporteEstatal;
import minsal.divap.enums.EstadosProgramas;
import minsal.divap.enums.Subtitulo;
import minsal.divap.enums.TareasSeguimiento;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.excel.GeneradorExcel;
import minsal.divap.excel.impl.AsignacionDistribucionPercapitaSheetExcel;
import minsal.divap.excel.impl.AsignacionRecursosPercapitaSheetExcel;
import minsal.divap.excel.impl.EstimacionFlujoCajaSheetExcel;
import minsal.divap.model.mappers.AsignacionDistribucionPercapitaMapper;
import minsal.divap.service.EmailService.Adjunto;
import minsal.divap.util.Util;
import minsal.divap.vo.AsignacionDistribucionPerCapitaVO;
import minsal.divap.vo.BaseVO;
import minsal.divap.vo.BodyVO;
import minsal.divap.vo.DocumentoVO;
import minsal.divap.vo.EmailVO;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.SeguimientoVO;

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
	// valores del a pasado.
	public Integer calcularPropuesta(Integer idProgramaAno) {

		//Obtnemos los datos del programa ano anterior.
		ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idProgramaAno);
	
		ProgramaAno programaAnoAnterior = programasDAO.getProgramaAnoByIDProgramaAno(programaAno.getPrograma().getId(),programaAno.getAno().getAno() -1);
		
		List<Caja> lst = cajaDAO.getByIDProgramaAno(programaAnoAnterior.getIdProgramaAno());
		if (lst.size()==0)
		{
			System.out.println("No se han encontrado datos para el a anterior.");
		}
		else
		{
		//Obtenemos el programa actual.
		
		for (Caja caja : lst) {
			// TODO: [ASAAVEDRA] Debo cambiar el programa ano.
			caja.setAno(caja.getAno() + 1);
			caja.setIdPrograma(programaAno);
			caja.setId(null);
			// caja.setIdPrograma(programaAnoSiguiente);
		}
		// Generamos la copia de los datos para el nuevo a
		cajaDAO.save(lst);
		}
		return idProgramaAno;
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
			headers.add("POBLACION MAYOR DE 65 Aﾃ前S");
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
			headers.add("POBLACION MAYOR DE 65 Aﾃ前S");
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
	private Integer getAnoCurso() {
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
		headers.add("POBLACION Aﾃ前" + getAnoCurso());
		headers.add("POBLACION MAYOR DE 65 Aﾃ前S" + getAnoCurso());
		headers.add("PER CAPITA Aﾃ前 " + getAnoCurso() + "(m$ " + getAnoCurso()
				+ ")");
		EstimacionFlujoCajaSheetExcel asignacionDistribucionPercapitaSheetExcel = new EstimacionFlujoCajaSheetExcel(
				headers, servicios);
		generadorExcel.addSheet(asignacionDistribucionPercapitaSheetExcel,
				"Hoja 1");
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

	//Obtiene el id del programa para el a siguiente para hacer la estimacion del flujo de caja.
	public Integer obtenerIdProgramaAno(Integer id) {
		// TODO Auto-generated method stub
		ProgramaAno programaAnoActual = programasDAO.getProgramaAnoByID(id);
		
		// Obtener el siguiente programa ano.
		ProgramaAno programaAnoSiguiente = programasDAO
				.getProgramaAnoByIDProgramaAno(programaAnoActual.getPrograma()
						.getId(), programaAnoActual.getAno().getAno()+1);
		
		Integer idProgramaAnoNuevo = 0;
		// CREAMOS EL PROGRAMA PARA EL AﾑO SIGUIENTE SI NO EXISTE.
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
		// TODO [ASAAVEDRA] Enviar a usuario consolidador tambi駭.
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

}
