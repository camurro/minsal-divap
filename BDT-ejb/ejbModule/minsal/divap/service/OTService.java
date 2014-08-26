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

import minsal.divap.dao.AntecedentesComunaDAO;
import minsal.divap.dao.DistribucionInicialPercapitaDAO;
import minsal.divap.dao.SeguimientoDAO;
import minsal.divap.dao.TratamientoOrdenDAO;
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.doc.GeneradorOficioConsulta;
import minsal.divap.doc.GeneradorWord;
import minsal.divap.enums.TareasSeguimiento;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.excel.GeneradorExcel;
import minsal.divap.excel.impl.AsignacionRecursosPercapitaSheetExcel;
import minsal.divap.model.mappers.AsignacionDistribucionPercapitaMapper;
import minsal.divap.vo.AsignacionDistribucionPerCapitaVO;
import minsal.divap.vo.BaseVO;
import minsal.divap.vo.BodyVO;
import minsal.divap.vo.DocumentoVO;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.SeguimientoVO;
import minsal.divap.vo.VariacionPoblacionVO;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import cl.minsal.divap.model.AntecendentesComunaCalculado;
import cl.minsal.divap.model.DistribucionInicialPercapita;
import cl.minsal.divap.model.Seguimiento;

@Stateless
@LocalBean
public class OTService {
	@EJB
	private DistribucionInicialPercapitaDAO distribucionInicialPercapitaDAO;
	@EJB
	private UsuarioDAO usuarioDAO;
	@EJB
	private TratamientoOrdenDAO tratamientoOrdenDAO;
	@EJB
	private SeguimientoDAO seguimientoDAO;
	@EJB
	private DocumentService documentService;
	@EJB
	private ServicioSaludService servicioSaludService;
	@EJB
	private AlfrescoService alfrescoService;
	@EJB
	private SeguimientoService seguimientoService;
	@EJB
	private EmailService emailService;
	@Resource(name="tmpDir")
	private String tmpDir;
	@Resource(name="tmpDirDoc")
	private String tmpDirDoc;
	@Resource(name="folderTemplateOT")
	private String folderTemplateTo;
	@Resource(name="folderOT")
	private String folderOT;

	
	/**
	 * METODO EL CUAL BUSCARA LOS DATOS CORRESPONDIENTE A LAS ACTIVIDADES:
	 * Revisar Antecedentes Línea Programática / Revisar Antecedentes Per Cápita / Revisar Antecedentes Leyes
	 */
	public List<AsignacionDistribucionPerCapitaVO> findDatos(Integer idPrograma) {
		
		List<AntecendentesComunaCalculado>  antecendentesComunaCalculado = null;
		antecendentesComunaCalculado = tratamientoOrdenDAO.findAntecendentes(idPrograma);
		
		List<AsignacionDistribucionPerCapitaVO> antecedentesCalculados = new ArrayList<AsignacionDistribucionPerCapitaVO>();
		
		if(antecendentesComunaCalculado != null && antecendentesComunaCalculado.size() > 0){
			
			for(AntecendentesComunaCalculado antecendenteComunaCalculado : antecendentesComunaCalculado){
				
				AsignacionDistribucionPerCapitaVO asignacionDistribucionPerCapitaVO = new AsignacionDistribucionPercapitaMapper().getBasic(antecendenteComunaCalculado);
				
				if(asignacionDistribucionPerCapitaVO != null){
					
					antecedentesCalculados.add(asignacionDistribucionPerCapitaVO);
				}
			}
		}
		return antecedentesCalculados;
	}
	
	public Integer getIdPlantillaRecursosPerCapita(){
		Integer plantillaId =null;// documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLAPOBLACIONINSCRITA);
		if(plantillaId == null){
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
			AsignacionRecursosPercapitaSheetExcel asignacionRecursosPercapitaSheetExcel = new AsignacionRecursosPercapitaSheetExcel(headers, servicios);
			generadorExcel.addSheet( asignacionRecursosPercapitaSheetExcel, "Hoja 1");
			try {
				BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType,folderTemplateTo );//(folderTemplatePercapita);
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
	
	private Integer getAnoCurso() {
		DateFormat formatNowYear = new SimpleDateFormat("yyyy");
		Date nowDate = new Date();
		return Integer.valueOf(formatNowYear.format(nowDate)); 
	}
	public Integer createSeguimientoOT(Integer idOT, TareasSeguimiento tareaSeguimiento, String subject, String body, String username, List<String> para, List<String> conCopia, List<String> conCopiaOculta, List<Integer> documentos){
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
				BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummaryVO.getPath()), contenType, folderOT.replace("{ANO}", getAnoCurso().toString()));
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
