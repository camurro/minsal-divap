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

import minsal.divap.dao.CajaDAO;
import minsal.divap.dao.EstimacionFlujoCajaDAO;
import minsal.divap.dao.SeguimientoDAO;
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.doc.GeneradorWord;
import minsal.divap.doc.GeneradorWordBorradorAporteEstatal;
import minsal.divap.enums.TareasSeguimiento;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.excel.GeneradorExcel;
import minsal.divap.excel.impl.AsignacionDistribucionPercapitaSheetExcel;
import minsal.divap.excel.impl.EstimacionFlujoCajaSheetExcel;
import minsal.divap.util.Util;
import minsal.divap.vo.AsignacionDistribucionPerCapitaVO;
import minsal.divap.vo.BaseVO;
import minsal.divap.vo.BodyVO;
import minsal.divap.vo.DocumentoVO;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.SeguimientoVO;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import cl.minsal.divap.model.Caja;
import cl.minsal.divap.model.Seguimiento;

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
	
	@Resource(name="tmpDir")
	private String tmpDir;
	
	@Resource(name="tmpDirDoc")
	private String tmpDirDoc;
	
	@Resource(name="folderEstimacionFlujoCaja")
	private String folderEstimacionFlujoCaja;
	
	@Resource(name="folderTemplateEstimacionFlujoCaja")
	private String folderTemplateEstimacionFlujoCaja;
	
	@EJB
	private UsuarioDAO usuarioDAO;
	
	@EJB
	private SeguimientoService seguimientoService;
	
	@EJB
	private SeguimientoDAO seguimientoDAO;
	
	@EJB
	private CajaDAO cajaDAO;
	
	//Generar documento
	public Integer elaborarOrdinarioProgramacionCaja(Integer idDistribucionInicialPercapita) {
		
		
		//return distribucionInicialPercapitaService.valorizarDisponibilizarPlanillaTrabajo(idDistribucionInicialPercapita);
		
		
		Integer plantillaBorradorOrdinarioProgramacionCaja = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLABORRADORORDINARIOPROGRAMACIONCAJA);
		if(plantillaBorradorOrdinarioProgramacionCaja == null){
			throw new RuntimeException("No se puede crear Borrador Decreto Aporte Estatal, la plantilla no esta cargada");
		}
		try {
			ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryBorradorAporteEstatalVO = documentService.getDocumentByPlantillaId(plantillaBorradorOrdinarioProgramacionCaja);
			DocumentoVO documentoBorradorOrdinarioProgramacionCajaVO = documentService.getDocument(referenciaDocumentoSummaryBorradorAporteEstatalVO.getId());
			String templateOrdinarioProgramacionCaja = tmpDirDoc + File.separator + documentoBorradorOrdinarioProgramacionCajaVO.getName();
			templateOrdinarioProgramacionCaja = templateOrdinarioProgramacionCaja.replace(" ", "");
			String filenameBorradorOrdinarioProgramacionCaja = tmpDirDoc + File.separator + new Date().getTime() + "_" + "OrdinarioProgramacionCaja.docx";
			filenameBorradorOrdinarioProgramacionCaja = filenameBorradorOrdinarioProgramacionCaja.replaceAll(" ", "");
			System.out.println("filenameBorradorAporteEstatal filename-->"+filenameBorradorOrdinarioProgramacionCaja);
			System.out.println("templateBorradorAporteEstatal template-->"+templateOrdinarioProgramacionCaja);
			GeneradorWord generadorWordPlantillaBorradorOrdinarioProgramacionCaja = new GeneradorWord(filenameBorradorOrdinarioProgramacionCaja);
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contenTypeBorradorAporteEstatal = mimemap.getContentType(filenameBorradorOrdinarioProgramacionCaja.toLowerCase());
			System.out.println("contenTypeBorradorAporteEstatal->"+contenTypeBorradorAporteEstatal);
			//generadorWordPlantillaBorradorDecretoAporteEstatal.saveContent(documentoBorradorAporteEstatalVO.getContent(), XWPFDocument.class);
			generadorWordPlantillaBorradorOrdinarioProgramacionCaja.saveContent(documentoBorradorOrdinarioProgramacionCajaVO.getContent(), XWPFDocument.class);
			
			
			
			Map<String, Object> parametersBorradorAporteEstatal = new HashMap<String, Object>();
			parametersBorradorAporteEstatal.put("{numero}", "100");
			GeneradorWordBorradorAporteEstatal generadorWordDecretoBorradorAporteEstatal = new GeneradorWordBorradorAporteEstatal(filenameBorradorOrdinarioProgramacionCaja, templateOrdinarioProgramacionCaja);
			generadorWordDecretoBorradorAporteEstatal.replaceValues(parametersBorradorAporteEstatal, null, XWPFDocument.class);
			BodyVO responseBorradorAporteEstatal = alfrescoService.uploadDocument(new File(filenameBorradorOrdinarioProgramacionCaja), contenTypeBorradorAporteEstatal, folderEstimacionFlujoCaja.replace("{ANO}", getAnoCurso().toString()));
			System.out.println("response responseBorradorAporteEstatal --->"+responseBorradorAporteEstatal);
			//TODO: Crear documento para estimacion flujo de caja.
			//plantillaIdBorradorDecretoAporteEstatal = documentService.createDocumentPercapita(idDistribucionInicialPercapita, TipoDocumentosProcesos.BORRADORAPORTEESTATAL, responseBorradorAporteEstatal.getNodeRef(), responseBorradorAporteEstatal.getFileName(), contenTypeBorradorAporteEstatal);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		return plantillaBorradorOrdinarioProgramacionCaja;
	}
	
	public List<AsignacionDistribucionPerCapitaVO> buscarDatosPlanillaPropuestaEstimacion(Integer idPrograma){
//		List<AntecendentesComunaCalculado>  antecendentesComunaCalculado = antecedentesComunaDAO.findAntecedentesComunaCalculadosByDistribucionInicialPercapita(idDistribucionInicialPercapita);
//		List<AsignacionDistribucionPerCapitaVO> antecedentesCalculados = new ArrayList<AsignacionDistribucionPerCapitaVO>();
//		if(antecendentesComunaCalculado != null && antecendentesComunaCalculado.size() > 0){
//			for(AntecendentesComunaCalculado antecendenteComunaCalculado : antecendentesComunaCalculado){
//				AsignacionDistribucionPerCapitaVO asignacionDistribucionPerCapitaVO = new AsignacionDistribucionPercapitaMapper().getBasic(antecendenteComunaCalculado);
//				if(asignacionDistribucionPerCapitaVO != null){
//					antecedentesCalculados.add(asignacionDistribucionPerCapitaVO);
//				}
//			}
//		}
//		return antecedentesCalculados;
		return null;
	}
	
	//GENERAR PLANILLA DE PROPUESTA DE ESTIMACION DE FLUJO DE CAJA DE LINEA PROGRAMATICA
	private Integer generarPlanillaPropuestaEstimacion(Integer idPrograma){
		
		Integer planillaTrabajoId = null;
		List<AsignacionDistribucionPerCapitaVO> antecedentesCalculados = buscarDatosPlanillaPropuestaEstimacion(idPrograma);
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator + "planillaAsignacionDistribucionPercapita.xlsx";
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
		headers.add("Valor Per Capita " + getAnoCurso() + "($/mes " + getAnoCurso() + ")");
		headers.add("POBLACION AÑO" + getAnoCurso());
		headers.add("POBLACION MAYOR DE 65 AÑOS" + getAnoCurso());
		headers.add("PER CAPITA AÑO " + getAnoCurso() + "(m$ " + getAnoCurso() + ")");
		AsignacionDistribucionPercapitaSheetExcel asignacionDistribucionPercapitaSheetExcel = new AsignacionDistribucionPercapitaSheetExcel(headers, antecedentesCalculados);
		generadorExcel.addSheet( asignacionDistribucionPercapitaSheetExcel, "Hoja 1");
		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderEstimacionFlujoCaja.replace("{ANO}", getAnoCurso().toString()));
			System.out.println("response AsignacionRecursosPercapitaSheetExcel --->"+response);
			//TODO: Guardar en la base.
			//DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findById(idDistribucionInicialPercapita);
			//planillaTrabajoId = documentService.createDocumentPercapita(distribucionInicialPercapita, TipoDocumentosProcesos.PLANILLARESULTADOSCALCULADOS, response.getNodeRef(), response.getFileName(), contenType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return planillaTrabajoId;
	}
	
	//Para hacer el calculo de la propuesta, se debe hacer una copia de los valores del a�o pasado.
	public Integer calcularPropuesta(Integer idPrograma){
	
		//Obtenemos los datos de la caja por el ID de programa.
		//Modificamos el a�o y guardamos nuevamente.
		List<Caja> lst = cajaDAO.getByIDProgramaAno(idPrograma, Util.obtenerAno(new Date()));
		for (Caja caja : lst) {
			caja.setAno(caja.getAno() + 1);
			caja.setId(null);
		}
		//Generamos la copia de los datos para el nuevo a�o
		cajaDAO.save(lst);
		
		
		
		
	return 1;
	}

	public Integer getIdPlantillaProgramacion() {
		// TODO Auto-generated method stub
		Integer plantillaId = null;//documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLAPROGRAMACION);
		if(plantillaId == null){
			//TODO: Implementar la creacion del excel.
			List<BaseVO> servicios = servicioSaludService.getAllServicios();

			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String filename = tmpDir + File.separator + "plantillaPercapita.xlsx";
			String contenType = mimemap.getContentType(filename.toLowerCase());
			GeneradorExcel generadorExcel = new GeneradorExcel(filename);
			List<String> headers = new ArrayList<String>();
			headers.add("REGION");
			headers.add("SERVICIO");
			headers.add("COMUNA");
			headers.add("POBLACION");
			headers.add("POBLACION MAYOR DE 65 AÑOS");
			EstimacionFlujoCajaSheetExcel estimacionFlujoCajaSheetExcel = new EstimacionFlujoCajaSheetExcel(headers, servicios);
			generadorExcel.addSheet( estimacionFlujoCajaSheetExcel, "Hoja 1");
			try {
				BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderEstimacionFlujoCaja);
				System.out.println("response AsignacionRecursosPercapitaSheetExcel --->"+response);
				plantillaId = documentService.createTemplate(TipoDocumentosProcesos.PLANTILLAPOBLACIONINSCRITA, response.getNodeRef(), response.getFileName(), contenType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			plantillaId = documentService.getDocumentoIdByPlantillaId(plantillaId);
		}
		return plantillaId;
	}

	public Integer getIdPlantillaPropuesta() {
		// TODO Auto-generated method stub
		Integer plantillaId = null;//documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLAPROPUESTA);
		if(plantillaId == null){
			//TODO: Implementar la creacion del excel.
			List<BaseVO> servicios = servicioSaludService.getAllServicios();

			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String filename = tmpDir + File.separator + "plantillaPercapita.xlsx";
			String contenType = mimemap.getContentType(filename.toLowerCase());
			GeneradorExcel generadorExcel = new GeneradorExcel(filename);
			List<String> headers = new ArrayList<String>();
			headers.add("REGION");
			headers.add("SERVICIO");
			headers.add("COMUNA");
			headers.add("POBLACION");
			headers.add("POBLACION MAYOR DE 65 AÑOS");
			EstimacionFlujoCajaSheetExcel estimacionFlujoCajaSheetExcel = new EstimacionFlujoCajaSheetExcel(headers, servicios);
			generadorExcel.addSheet( estimacionFlujoCajaSheetExcel, "Hoja 1");
			try {
				BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderEstimacionFlujoCaja);
				System.out.println("response AsignacionRecursosPercapitaSheetExcel --->"+response);
				plantillaId = documentService.createTemplate(TipoDocumentosProcesos.PLANTILLAPOBLACIONINSCRITA, response.getNodeRef(), response.getFileName(), contenType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			plantillaId = documentService.getDocumentoIdByPlantillaId(plantillaId);
		}
		return plantillaId;
	}
	//CORREO
		private Integer getAnoCurso() {
			DateFormat formatNowYear = new SimpleDateFormat("yyyy");
			Date nowDate = new Date();
			return Integer.valueOf(formatNowYear.format(nowDate)); 
		}
		public Integer createSeguimiento(Integer idOT, TareasSeguimiento tareaSeguimiento, String subject, String body, String username, List<String> para, List<String> conCopia, List<String> conCopiaOculta, List<Integer> documentos){
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
					BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummaryVO.getPath()), contenType, folderEstimacionFlujoCaja.replace("{ANO}", getAnoCurso().toString()));
					System.out.println("response upload template --->"+response);
					documentService.updateDocumentTemplate(referenciaDocumentoId, response.getNodeRef(), response.getFileName(), contenType);
				}
			}
			Integer idSeguimiento = seguimientoService.createSeguimiento(tareaSeguimiento, subject, body, from, para, conCopia, conCopiaOculta, documentosTmp);
			Seguimiento seguimiento = seguimientoDAO.getSeguimientoById(idSeguimiento);
			return  1;//distribucionInicialPercapitaDAO.createSeguimiento(idDistribucionInicialPercapita, seguimiento);
		}
		
		public List<SeguimientoVO> getBitacora(
				Integer idDistribucionInicialPercapita,
				TareasSeguimiento tareaSeguimiento) {
			return seguimientoService.getBitacora(idDistribucionInicialPercapita, tareaSeguimiento);
		}
	
	

}

