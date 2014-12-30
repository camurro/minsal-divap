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
import javax.xml.bind.JAXBException;

import minsal.divap.dao.AnoDAO;
import minsal.divap.dao.AntecedentesComunaDAO;
import minsal.divap.dao.DistribucionInicialPercapitaDAO;
import minsal.divap.dao.SeguimientoDAO;
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.doc.GeneradorDocumento;
import minsal.divap.doc.GeneradorOficioConsulta;
import minsal.divap.doc.GeneradorResolucionAporteEstatal;
import minsal.divap.doc.GeneradorWord;
import minsal.divap.doc.util.NumberToLetters;
import minsal.divap.enums.FieldType;
import minsal.divap.enums.TareasSeguimiento;
import minsal.divap.enums.TipoComuna;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.excel.GeneradorExcel;
import minsal.divap.excel.impl.AsignacionDistribucionPercapitaSheetExcel;
import minsal.divap.excel.impl.AsignacionRecursosPercapitaSheetExcel;
import minsal.divap.excel.impl.PercapitaCalculoPercapitaExcelValidator;
import minsal.divap.excel.impl.PercapitaDesempenoDificilExcelValidator;
import minsal.divap.exception.ExcelFormatException;
import minsal.divap.model.mappers.AsignacionDistribucionPercapitaMapper;
import minsal.divap.util.StringUtil;
import minsal.divap.vo.AsignacionDistribucionPerCapitaVO;
import minsal.divap.vo.BaseVO;
import minsal.divap.vo.BodyVO;
import minsal.divap.vo.CalculoPercapitaVO;
import minsal.divap.vo.CellTypeExcelVO;
import minsal.divap.vo.ComunaSummaryVO;
import minsal.divap.vo.DesempenoDificilVO;
import minsal.divap.vo.DocumentoVO;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.RegionVO;
import minsal.divap.vo.SeguimientoVO;
import minsal.divap.vo.ServiciosVO;
//import minsal.divap.vo.VariacionPoblacionVO;
import minsal.divap.xml.GeneradorXML;
import minsal.divap.xml.email.Email;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.docx4j.openpackaging.exceptions.Docx4JException;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import cl.minsal.divap.model.AnoEnCurso;
import cl.minsal.divap.model.AntecendentesComuna;
import cl.minsal.divap.model.AntecendentesComunaCalculado;
import cl.minsal.divap.model.DistribucionInicialPercapita;
import cl.minsal.divap.model.DocumentoDistribucionInicialPercapita;
import cl.minsal.divap.model.Seguimiento;
import cl.minsal.divap.model.Usuario;

@Stateless
@LocalBean
public class DistribucionInicialPercapitaService {
	@EJB
	private DistribucionInicialPercapitaDAO distribucionInicialPercapitaDAO;
	@EJB
	private UsuarioDAO usuarioDAO;
	@EJB
	private AntecedentesComunaDAO antecedentesComunaDAO;
	@EJB
	private SeguimientoDAO seguimientoDAO;
	@EJB
	private AnoDAO anoDAO;
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
	@Resource(name="folderTemplatePercapita")
	private String folderTemplatePercapita;
	@Resource(name="folderPercapita")
	private String folderPercapita;


	public Integer crearIntanciaDistribucionInicialPercapita(String username){
		System.out.println("username-->"+username);
		Usuario usuario = this.usuarioDAO.getUserByUsername(username);
		AnoEnCurso anoEnCurso = anoDAO.getAnoById(getAnoCurso() + 1);
		return distribucionInicialPercapitaDAO.crearIntanciaDistribucionInicialPercapita(usuario, anoEnCurso);
	}

	public Integer cargarPlantilla(TipoDocumentosProcesos plantilla, File file){
		Integer plantillaId = documentService.getPlantillaByType(plantilla);
		String filename = null;
		String folderAlfresco = null;
		switch (plantilla) {
		case PLANTILLAASIGNACIONDESEMPENODIFICIL:
			filename = "plantillaDesempenoDificil.xlsx";
			folderAlfresco = folderTemplatePercapita;
			break;
		case PLANTILLAPOBLACIONINSCRITA:
			filename = "plantillaPercapita.xlsx";
			folderAlfresco = folderTemplatePercapita;
			break;	
		case PLANTILLAOFICIOCONSULTA:
			filename = "plantillaOficioConsulta.docx";
			folderAlfresco = folderTemplatePercapita;
			break;	
		case PLANTILLACORREOCONSULTAREGIONALPERCAPITA:
			filename = "plantillaCorreoConsultaRegionalPercapita.xml";
			folderAlfresco = folderTemplatePercapita;
			break;
		case PLANTILLACORREORESOLUCIONESPERCAPITA:
			filename = "plantillaResolucionesPercapita.xml";
			folderAlfresco = folderTemplatePercapita;
			break;
		case PLANTILLAMODIFICACIONRESOLUCIONAPORTEESTATAL:
			filename = "plantillaModificacionResolucionAporteEstatal.docx";
			folderAlfresco = folderTemplatePercapita;
			break;
		case PLANTILLAMODIFICACIONDECRETOAPORTEESTATAL:
			filename = "plantillaModificacionDecretoAporteEstatal.docx";
			folderAlfresco = folderTemplatePercapita;
			break;
		case PLANTILLARESOLUCIONREBAJA:
			filename = "plantillaResolucionRebaja.docx";
			folderAlfresco = "TEMPLATES/REBAJA";
			break;	
		case PLANTILLACORREORESOLUCIONSERVICIOSALUDREBAJA:
			filename = "plantillaCorreoResolucionRebaja.xml";
			folderAlfresco = "TEMPLATES/REBAJA";
			break;	
		case PLANTILLAOFICIOPROGRAMACIONCAJA:
			filename = "plantillaOficioProgramacionCaja.docx";
			folderAlfresco = "TEMPLATES/ESTIMACIONFLUJOCAJA";
			break;
		case RESOLUCIONPROGRAMASAPS:
			filename = "plantillaResolucionProgramasAPS.docx";
			folderAlfresco = "TEMPLATES/RECURSOSFINANCIEROSPROGRAMASREFORZAMIENTOAPS";
			break;
		case ORDINARIOPROGRAMASAPS:
			filename = "plantillaOrdinarioProgramasAPS.docx";
			folderAlfresco = "TEMPLATES/RECURSOSFINANCIEROSPROGRAMASREFORZAMIENTOAPS";
			break;
		case PLANTILLARESOLUCIONCORREO:
			filename = "plantillaResolucionCorreo.xml";
			folderAlfresco = "TEMPLATES/RECURSOSFINANCIEROSPROGRAMASREFORZAMIENTOAPS";
			break;
		case PLANTILLAORDINARIOCORREO:
			filename = "plantillaOrdinarioCorreo.xml";
			folderAlfresco = "TEMPLATES/RECURSOSFINANCIEROSPROGRAMASREFORZAMIENTOAPS";
			break;
		case PLANTILLARESOLUCIONRETIRO:
			filename = "plantillaResolucionRetiro.docx";
			folderAlfresco = "TEMPLATES/CONVENIO";
			break;
		case PLANTILLACORREORESOLUCIONRETIRO:
			filename = "plantillaCorreoResolucionRetiro.xml";
			folderAlfresco = "TEMPLATES/CONVENIO";
			break;
		case PLANTILLAORDINARIOSOLICITUDANTECEDENTES:
			filename = "plantillaOrdinarioSolicitudAntecedentes.docx";
			folderAlfresco = folderTemplatePercapita;
			break;
		case PLANTILLACORREOORDINARIOSOLICITUDANTECEDENTES:
			filename = "plantillaCorreoOrdinarioSolicitudAntecedentes.xml";
			folderAlfresco = folderTemplatePercapita;
		case PLANTILLAORDINARIOOREDENTRANSFERENCIA:
			filename = "TemplateOrdinarioOT.docx";
			folderAlfresco = "TEMPLATES/ORDENESDETRANSFERENCIA";
			break;
		case PLANTILLAOTCORREO:
			filename = "plantillaOTCorreo.xml";
			folderAlfresco = "TEMPLATES/ORDENESDETRANSFERENCIA";
			break;
		case MODIFICACIONRESOLUCIONPROGRAMASAPS:
			filename = "ModificacionResolucionProgramasAPS.docx";
			folderAlfresco = "TEMPLATES/RECURSOSFINANCIEROSPROGRAMASREFORZAMIENTOAPS";
			break;
		case MODIFICACIONORDINARIOPROGRAMASAPS:
			filename = "ModificacionOrdinarioProgramasAPS.docx";
			folderAlfresco = "TEMPLATES/RECURSOSFINANCIEROSPROGRAMASREFORZAMIENTOAPS";
			break;
		default:
			break;
		}
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		filename = tmpDir + File.separator + filename;
		String contentType = mimemap.getContentType(filename.toLowerCase());
		if(plantillaId == null){
			try {
				BodyVO response = alfrescoService.uploadDocument(file, contentType, folderAlfresco);
				System.out.println("response upload template --->"+response);
				plantillaId = documentService.createTemplate(plantilla, response.getNodeRef(), response.getFileName(), contentType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			Integer referenciaDocumentoId = documentService.getDocumentoIdByPlantillaId(plantillaId);
			try {
				BodyVO response = alfrescoService.uploadDocument(file, contentType, folderAlfresco);
				System.out.println("response upload template --->"+response);
				documentService.updateDocumentTemplate(referenciaDocumentoId, response.getNodeRef(), response.getFileName(), contentType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return plantillaId;
	}

	public Integer getIdPlantillaPoblacionInscrita(){
		Integer plantillaId = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLAASIGNACIONDESEMPENODIFICIL);
		if(plantillaId == null){
			List<BaseVO> servicios = servicioSaludService.getAllServiciosComunasCFUrbanaRural();

			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String filename = tmpDir + File.separator + "plantillaDesempenoDificil.xlsx";
			String contenType = mimemap.getContentType(filename.toLowerCase());
			GeneradorExcel generadorExcel = new GeneradorExcel(filename);
			List<String> headers = new ArrayList<String>();
			headers.add("REGION");
			headers.add("SERVICIO");
			headers.add("COMUNA");
			headers.add("VALOR BÁSICO DE ASIGNACIÓN POR DESEMPEÑO DIFICIL/Mensual");
			AsignacionRecursosPercapitaSheetExcel asignacionDesempenoDificilSheetExcel = new AsignacionRecursosPercapitaSheetExcel(headers, servicios);
			generadorExcel.addSheet( asignacionDesempenoDificilSheetExcel, "Hoja 1");
			try {
				BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderTemplatePercapita);
				System.out.println("response asignacionDesempenoDificilSheetExcel --->"+response);
				plantillaId = documentService.createTemplate(TipoDocumentosProcesos.PLANTILLAASIGNACIONDESEMPENODIFICIL, response.getNodeRef(), response.getFileName(), contenType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			plantillaId = documentService.getDocumentoIdByPlantillaId(plantillaId);
		}
		return plantillaId;
	}
	
	
	public DistribucionInicialPercapita ultimaDistribucionPercapita(){
		return distribucionInicialPercapitaDAO.findLast(getAnoCurso() + 1);
	}

	public Integer getIdPlantillaRecursosPerCapita(){

		Integer plantillaId = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLAPOBLACIONINSCRITA);
		if(plantillaId == null){
			List<BaseVO> servicios = servicioSaludService.getAllServiciosComunasCFUrbanaRural();

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
				BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderTemplatePercapita);
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

	public void procesarCalculoPercapita(Integer idDistribucionInicialPercapita, XSSFWorkbook workbook) throws ExcelFormatException {
		List<CellTypeExcelVO> cells = new ArrayList<CellTypeExcelVO>();
		CellTypeExcelVO fieldOne = new CellTypeExcelVO(true, FieldType.INTEGERFIELD);
		cells.add(fieldOne);
		CellTypeExcelVO fieldTwo = new CellTypeExcelVO(true);
		cells.add(fieldTwo);
		CellTypeExcelVO fieldThree = new CellTypeExcelVO(true);
		cells.add(fieldThree);
		CellTypeExcelVO fieldFour = new CellTypeExcelVO(true, FieldType.INTEGERFIELD);
		cells.add(fieldFour);
		CellTypeExcelVO fieldFive = new CellTypeExcelVO(true, FieldType.INTEGERFIELD);
		cells.add(fieldFive);
		XSSFSheet worksheet = workbook.getSheetAt(0);
		
		PercapitaCalculoPercapitaExcelValidator calculoPercapitaExcelValidator = new PercapitaCalculoPercapitaExcelValidator(cells.size(), cells, true, 0, 0);
		calculoPercapitaExcelValidator.validateFormat(worksheet);		
		List<CalculoPercapitaVO> items = calculoPercapitaExcelValidator.getItems();
		for(CalculoPercapitaVO calculoPercapitaVO : items){
			AntecendentesComuna antecendentesComuna = antecedentesComunaDAO.findAntecendentesComunaByComunaServicioAno(calculoPercapitaVO.getServicio(), calculoPercapitaVO.getComuna(), getAnoCurso() + 1);
			if(antecendentesComuna != null){
				AntecendentesComunaCalculado antecendentesComunaCalculado = antecedentesComunaDAO.findByAntecedentesDistrinbucionInicialVigente(antecendentesComuna.getIdAntecedentesComuna(), idDistribucionInicialPercapita);
				if( antecendentesComunaCalculado == null){
					antecendentesComunaCalculado = new AntecendentesComunaCalculado();
					antecendentesComunaCalculado.setAntecedentesComuna(antecendentesComuna);
					antecendentesComunaCalculado.setPoblacion(calculoPercapitaVO.getPoblacion().intValue());
					antecendentesComunaCalculado.setPoblacionMayor(calculoPercapitaVO.getPoblacionMayor().intValue());
					DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findById(idDistribucionInicialPercapita);
					antecendentesComunaCalculado.setDistribucionInicialPercapita(distribucionInicialPercapita);
					antecedentesComunaDAO.createAntecendentesComunaCalcuado(antecendentesComunaCalculado);
				} else {
					antecendentesComunaCalculado.setPoblacion(calculoPercapitaVO.getPoblacion().intValue());
					antecendentesComunaCalculado.setPoblacionMayor(calculoPercapitaVO.getPoblacionMayor().intValue());
				}
			}
		}
	}

	public Integer getAnoCurso() {
		DateFormat formatNowYear = new SimpleDateFormat("yyyy");
		Date nowDate = new Date();
		return Integer.valueOf(formatNowYear.format(nowDate)); 
	}

	public void procesarValorBasicoDesempeno(Integer idDistribucionInicialPercapita, XSSFWorkbook workbook) throws ExcelFormatException {
		List<CellTypeExcelVO> cells = new ArrayList<CellTypeExcelVO>();
		CellTypeExcelVO fieldOne = new CellTypeExcelVO(true, FieldType.INTEGERFIELD);
		cells.add(fieldOne);
		CellTypeExcelVO fieldTwo = new CellTypeExcelVO(true);
		cells.add(fieldTwo);
		CellTypeExcelVO fieldThree = new CellTypeExcelVO(true);
		cells.add(fieldThree);
		CellTypeExcelVO fieldFour = new CellTypeExcelVO(true, FieldType.INTEGERFIELD);
		cells.add(fieldFour);
		XSSFSheet worksheet = workbook.getSheetAt(0);
		PercapitaDesempenoDificilExcelValidator desempenoDificilExcelValidator = new PercapitaDesempenoDificilExcelValidator(cells.size(), cells, true, 0, 0);
		desempenoDificilExcelValidator.validateFormat(worksheet);		
		List<DesempenoDificilVO> items = desempenoDificilExcelValidator.getItems();

		System.out.println("procesarValorBasicoDesempeno->idDistribucionInicialPercapita="+idDistribucionInicialPercapita+" ");
		for(DesempenoDificilVO desempenoDificilVO : items){
			AntecendentesComuna antecendentesComuna = antecedentesComunaDAO.findAntecendentesComunaByComunaServicioAno(desempenoDificilVO.getServicio(), desempenoDificilVO.getComuna(), getAnoCurso() + 1);
			if(antecendentesComuna != null){
				AntecendentesComunaCalculado antecendentesComunaCalculado = antecedentesComunaDAO.findByAntecedentesDistrinbucionInicial(antecendentesComuna.getIdAntecedentesComuna(), idDistribucionInicialPercapita);
				if( antecendentesComunaCalculado == null){
					antecendentesComunaCalculado = new AntecendentesComunaCalculado();
					antecendentesComunaCalculado.setAntecedentesComuna(antecendentesComuna);
					antecendentesComunaCalculado.setDesempenoDificil(desempenoDificilVO.getValorDesempenoDificil().intValue());
					DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findById(idDistribucionInicialPercapita);
					antecendentesComunaCalculado.setDistribucionInicialPercapita(distribucionInicialPercapita);
					antecedentesComunaDAO.createAntecendentesComunaCalcuado(antecendentesComunaCalculado);
				} else {
					antecendentesComunaCalculado.setDesempenoDificil(desempenoDificilVO.getValorDesempenoDificil().intValue());
				}
			}
		}
	}

	public Integer valorizarDisponibilizarPlanillaTrabajo(Integer idDistribucionInicialPercapita) {
		System.out.println("valorizarDisponibilizarPlanillaTrabajo-->"+idDistribucionInicialPercapita);
		List<AntecendentesComunaCalculado>  antecendentesComunaCalculado = antecedentesComunaDAO.findAntecedentesComunaCalculadosByDistribucionInicialPercapitaVigente(idDistribucionInicialPercapita);
		if(antecendentesComunaCalculado != null && antecendentesComunaCalculado.size() > 0){
			for(AntecendentesComunaCalculado antecendenteComunaCalculado: antecendentesComunaCalculado){
				if(antecendenteComunaCalculado.getAntecedentesComuna() != null){
					Integer percapitaBasal = antecendenteComunaCalculado.getAntecedentesComuna().getAnoAnoEnCurso().getMontoPercapitalBasal();
					Integer asignacionAdultoMayor = antecendenteComunaCalculado.getAntecedentesComuna().getAnoAnoEnCurso().getAsignacionAdultoMayor();
					System.out.println("percapitaBasal=" + percapitaBasal + " antecendenteComunaCalculado.getAntecedentesComuna().getTramoPobreza().getValor()=" + ((antecendenteComunaCalculado.getAntecedentesComuna().getTramoPobreza() == null) ? 0 : antecendenteComunaCalculado.getAntecedentesComuna().getTramoPobreza().getValor()));
					Double pobreza = percapitaBasal * ((antecendenteComunaCalculado.getAntecedentesComuna().getTramoPobreza() == null) ? 0 : antecendenteComunaCalculado.getAntecedentesComuna().getTramoPobreza().getValor());
					System.out.println("pobreza-->"+pobreza);
					antecendenteComunaCalculado.setPobreza(pobreza);
					System.out.println("antecendenteComunaCalculado.getAntecedentesComuna().getIdAntecedentesComuna()="+antecendenteComunaCalculado.getAntecedentesComuna().getIdAntecedentesComuna());
					if(antecendenteComunaCalculado.getAntecedentesComuna().getClasificacion() != null){
						if(TipoComuna.RURAL.getId().equals(antecendenteComunaCalculado.getAntecedentesComuna().getClasificacion().getIdTipoComuna())){
							Double ruralidad = (percapitaBasal + pobreza) * 0.2;
							System.out.println("ruralidad-->"+ruralidad);
							antecendenteComunaCalculado.setRuralidad(ruralidad);
							Double valorReferencialZona = (percapitaBasal + pobreza + ruralidad) * ((antecendenteComunaCalculado.getAntecedentesComuna().getAsignacionZona() == null) ? 0 : antecendenteComunaCalculado.getAntecedentesComuna().getAsignacionZona().getValor());
							System.out.println("valorReferencialZona-->"+valorReferencialZona);
							antecendenteComunaCalculado.setValorReferencialZona(valorReferencialZona);
							Double valorPerCapitaComunalMes = (percapitaBasal + pobreza + ruralidad + valorReferencialZona);
							antecendenteComunaCalculado.setValorPerCapitaComunalMes(valorPerCapitaComunalMes);
							System.out.println("valorPerCapitaComunalMes="+valorPerCapitaComunalMes);
							System.out.println("antecendenteComunaCalculado.getPoblacion()="+antecendenteComunaCalculado.getPoblacion());
							System.out.println("antecendenteComunaCalculado.getPoblacionMayor()="+antecendenteComunaCalculado.getPoblacionMayor());
							int poblacion =((antecendenteComunaCalculado.getPoblacion() == null) ? 0 : antecendenteComunaCalculado.getPoblacion());
							int poblacionMayor =(( antecendenteComunaCalculado.getPoblacionMayor() == null) ? 0 : antecendenteComunaCalculado.getPoblacionMayor());
							System.out.println("asignacionAdultoMayor="+asignacionAdultoMayor);
							Integer redondeado = (int)Math.round(valorPerCapitaComunalMes);
							Double calculoFinal = (double)(redondeado * poblacion + poblacionMayor * asignacionAdultoMayor);
							Long perCapitaMes = Math.round(calculoFinal);
							antecendenteComunaCalculado.setPercapitaMes(perCapitaMes);
							Long perCapitaAno = perCapitaMes * 12;
							System.out.println("perCapitaAno= "+perCapitaAno);
							antecendenteComunaCalculado.setPercapitaAno(perCapitaAno);
						} else if(TipoComuna.URBANA.getId().equals(antecendenteComunaCalculado.getAntecedentesComuna().getClasificacion().getIdTipoComuna())){
							Double ruralidad = 0.0;
							antecendenteComunaCalculado.setRuralidad(ruralidad);
							Double valorReferencialZona = (percapitaBasal + pobreza + ruralidad) * ((antecendenteComunaCalculado.getAntecedentesComuna().getAsignacionZona() == null) ? 0 : antecendenteComunaCalculado.getAntecedentesComuna().getAsignacionZona().getValor());
							antecendenteComunaCalculado.setValorReferencialZona(valorReferencialZona);
							Double valorPerCapitaComunalMes = (percapitaBasal + pobreza + 0.0 + valorReferencialZona);
							antecendenteComunaCalculado.setValorPerCapitaComunalMes(valorPerCapitaComunalMes);
							System.out.println("valorPerCapitaComunalMes="+valorPerCapitaComunalMes);
							System.out.println("antecendenteComunaCalculado.getPoblacion()="+antecendenteComunaCalculado.getPoblacion());
							System.out.println("antecendenteComunaCalculado.getPoblacionMayor()="+antecendenteComunaCalculado.getPoblacionMayor());
							System.out.println("asignacionAdultoMayor="+asignacionAdultoMayor);
							int poblacion =(( antecendenteComunaCalculado.getPoblacion() == null) ? 0 : antecendenteComunaCalculado.getPoblacion());
							int poblacionMayor =(( antecendenteComunaCalculado.getPoblacionMayor() == null) ? 0 : antecendenteComunaCalculado.getPoblacionMayor());
							Integer redondeado = (int)Math.round(valorPerCapitaComunalMes);
							Double calculoFinal = (double)(redondeado * poblacion + poblacionMayor * asignacionAdultoMayor);
							Long perCapitaMes = Math.round(calculoFinal);
							System.out.println("perCapitaMes="+perCapitaMes);
							antecendenteComunaCalculado.setPercapitaMes(perCapitaMes);
							Long perCapitaAno = perCapitaMes * 12;
							System.out.println("perCapitaAno="+perCapitaAno);
							antecendenteComunaCalculado.setPercapitaAno(perCapitaAno);
						} else if(TipoComuna.COSTOFIJO.getId().equals(antecendenteComunaCalculado.getAntecedentesComuna().getClasificacion().getIdTipoComuna())){
							Double ruralidad = 0.0;
							antecendenteComunaCalculado.setRuralidad(ruralidad);
							Double valorReferencialZona = (percapitaBasal + pobreza + ruralidad) * ((antecendenteComunaCalculado.getAntecedentesComuna().getAsignacionZona() == null) ? 0 : antecendenteComunaCalculado.getAntecedentesComuna().getAsignacionZona().getValor());
							antecendenteComunaCalculado.setValorReferencialZona(valorReferencialZona);
							Double valorPerCapitaComunalMes = (percapitaBasal + pobreza + 0.0 + valorReferencialZona);
							antecendenteComunaCalculado.setValorPerCapitaComunalMes(valorPerCapitaComunalMes);
							Double perCapitaCostoFijo = antecedentesComunaDAO.findPerCapitaCostoFijoByServicioComunaAnoAnterior(antecendenteComunaCalculado.getAntecedentesComuna().getIdComuna().getId(), antecendenteComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId(), getAnoCurso());
							System.out.println("perCapitaCostoFijo="+perCapitaCostoFijo);
							perCapitaCostoFijo = ((perCapitaCostoFijo == null) ? 0.0 : perCapitaCostoFijo);
							Double calculoFinal = (perCapitaCostoFijo * antecendenteComunaCalculado.getAntecedentesComuna().getAnoAnoEnCurso().getInflactor());
							Long perCapitaMes = Math.round(calculoFinal);
							System.out.println("perCapitaMes="+perCapitaMes);
							antecendenteComunaCalculado.setPercapitaMes(perCapitaMes);
							Long perCapitaAno = perCapitaMes * 12;
							System.out.println("perCapitaAno="+perCapitaAno);
							antecendenteComunaCalculado.setPercapitaAno(perCapitaAno);
						}
					}
				}
			}
		}
		return publicarPlanillaTrabajo(idDistribucionInicialPercapita);
	}

	private Integer publicarPlanillaTrabajo(Integer idDistribucionInicialPercapita){
		Integer planillaTrabajoId = null;
		List<AsignacionDistribucionPerCapitaVO> antecedentesCalculados = findAntecedentesComunaCalculadosByDistribucionInicialPercapita(idDistribucionInicialPercapita);
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator + "planillaAsignacionDistribucionPercapita.xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());
		GeneradorExcel generadorExcel = new GeneradorExcel(filename);
		List<String> headers = new ArrayList<String>();
		Integer idAnoSiguiente = getAnoCurso() + 1;
		headers.add("REGION");
		headers.add("SERVICIO");
		headers.add("COMUNA");
		headers.add("CLASIFICACION " + idAnoSiguiente);
		headers.add("Ref. Asig_Zon " + idAnoSiguiente);
		headers.add("Tramo Pobreza");
		headers.add("Per Capita Basal " + idAnoSiguiente);
		headers.add("Pobreza " + idAnoSiguiente);
		headers.add("Ruralidad " + idAnoSiguiente);
		headers.add("Valor Ref. Asig_Zon " + idAnoSiguiente);
		headers.add("Valor Per Capita " + idAnoSiguiente + "($/mes " + idAnoSiguiente + ")");
		headers.add("POBLACION AÑO" + idAnoSiguiente);
		headers.add("POBLACION MAYOR DE 65 AÑOS" + idAnoSiguiente);
		headers.add("PER CAPITA AÑO " + idAnoSiguiente + "($ " + idAnoSiguiente + ")");
		headers.add("PER CAPITA MES " + idAnoSiguiente + "($ " + idAnoSiguiente + ")");
		AsignacionDistribucionPercapitaSheetExcel asignacionDistribucionPercapitaSheetExcel = new AsignacionDistribucionPercapitaSheetExcel(headers, antecedentesCalculados);
		generadorExcel.addSheet( asignacionDistribucionPercapitaSheetExcel, "Hoja 1");
		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderPercapita.replace("{ANO}", idAnoSiguiente.toString()));
			System.out.println("response AsignacionRecursosPercapitaSheetExcel --->"+response);
			DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findById(idDistribucionInicialPercapita);
			planillaTrabajoId = documentService.createDocumentPercapita(distribucionInicialPercapita, TipoDocumentosProcesos.PLANILLARESULTADOSCALCULADOS, response.getNodeRef(), response.getFileName(), contenType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return planillaTrabajoId;
	}

	public List<AsignacionDistribucionPerCapitaVO> findAntecedentesComunaCalculadosByDistribucionInicialPercapita(Integer idDistribucionInicialPercapita){
		List<AntecendentesComunaCalculado>  antecendentesComunaCalculado = antecedentesComunaDAO.findAntecedentesComunaCalculadosByDistribucionInicialPercapitaVigente(idDistribucionInicialPercapita);
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
	
	public List<AsignacionDistribucionPerCapitaVO> findAntecedentesServicioCalculadosByDistribucionInicialPercapita(Integer idServicio, Integer idDistribucionInicialPercapita){
		List<AntecendentesComunaCalculado>  antecendentesComunaCalculado = antecedentesComunaDAO.findAntecedentesServicioCalculadosByDistribucionInicialPercapitaVigente(idServicio, idDistribucionInicialPercapita);
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

	public void procesarCalculoPercapita(HSSFSheet workbook) {
		throw new NotImplementedException();
	}

	public void procesarValorBasicoDesempeno(HSSFSheet workbook) {
		throw new NotImplementedException();
	}

	public List<AsignacionDistribucionPerCapitaVO> findAntecedentesComunaCalculadosByDistribucionInicialPercapita(
			Integer servicio, Integer comuna, Integer idDistribucionInicialPercapita) {
		List<AntecendentesComunaCalculado>  antecendentesComunaCalculado = null;
		if(comuna != null){
			antecendentesComunaCalculado = antecedentesComunaDAO.findAntecendentesComunaCalculadoByComunaServicioDistribucionInicialPercapitaVigente(servicio, comuna, idDistribucionInicialPercapita);
		} else {
			antecendentesComunaCalculado = antecedentesComunaDAO.findAntecendentesComunaCalculadoByServicioDistribucionInicialPercapitaVigente(servicio, idDistribucionInicialPercapita);
		}
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

	public Integer crearOficioConsulta(Integer idDistribucionInicialPercapita) {
		System.out.println("DistribucionInicialPercapitaService --- > crearOficioConsulta");
		Integer plantillaId = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLAOFICIOCONSULTA);
		if(plantillaId == null){
			throw new RuntimeException("No se puede crear Oficio Consulta, la plantilla no esta cargada");
		}
		try{
			ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO = documentService.getDocumentByPlantillaId(plantillaId);
			DocumentoVO documentoVO = documentService.getDocument(referenciaDocumentoSummaryVO.getId());
			String template = tmpDirDoc + File.separator + documentoVO.getName();
			template = template.replace(" ", "");
			String filename = tmpDirDoc + File.separator + new Date().getTime() + "_" + "OficioConsulta.docx";
			filename = filename.replaceAll(" ", "");
			System.out.println("crearOficioConsulta filename-->"+filename);
			System.out.println("crearOficioConsulta template-->"+template);
			GeneradorWord generadorWord = new GeneradorWord(template);
			//List<VariacionPoblacionVO> variaciones = new ArrayList<VariacionPoblacionVO>();
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contenType = mimemap.getContentType(filename.toLowerCase());
			System.out.println("template->"+template);
			System.out.println("filename->"+filename);
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("{ano_siguiente}", (getAnoCurso() + 1));
			parameters.put("{poblacion_beneficiaria}", new Integer(100000));
			parameters.put("{ano_curso}", getAnoCurso());
			if(template.endsWith(".doc")){
				System.out.println("Genera archivo doc");
				generadorWord.saveContent(documentoVO.getContent(), HWPFDocument.class);
				GeneradorOficioConsulta generadorOficioConsulta = new GeneradorOficioConsulta(filename,template);
				generadorOficioConsulta.replaceValues(null, null, HWPFDocument.class);
				BodyVO response = alfrescoService.uploadDocument(new File(filename), contenType, folderPercapita.replace("{ANO}", String.valueOf((getAnoCurso() + 1 )) ));
				System.out.println("response AsignacionRecursosPercapitaSheetExcel --->"+response);
				plantillaId = documentService.createDocumentPercapita(idDistribucionInicialPercapita, TipoDocumentosProcesos.OFICIOCONSULTA, response.getNodeRef(), response.getFileName(), contenType);
			}else if(template.endsWith(".docx")){
				System.out.println("Genera archivo docx");
				generadorWord.saveContent(documentoVO.getContent(), XWPFDocument.class);
				GeneradorResolucionAporteEstatal generadorOficioConsulta = new GeneradorResolucionAporteEstatal(filename, template);
				//generadorOficioConsulta.replaceValues(parameters, variaciones, XWPFDocument.class);
				generadorOficioConsulta.replaceValues(parameters, XWPFDocument.class);
				BodyVO response = alfrescoService.uploadDocument(new File(filename), contenType, folderPercapita.replace("{ANO}", String.valueOf((getAnoCurso() + 1 ))));
				System.out.println("response AsignacionRecursosPercapita --->"+response);
				plantillaId = documentService.createDocumentPercapita(idDistribucionInicialPercapita, TipoDocumentosProcesos.OFICIOCONSULTA, response.getNodeRef(), response.getFileName(), contenType);
			}
		}catch(IOException e){
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (Docx4JException e) {
			e.printStackTrace();
		}
		return plantillaId;
	}

	public Integer createSeguimientoDistribucionInicialPercapita(Integer idDistribucionInicialPercapita, TareasSeguimiento tareaSeguimiento, String subject, String body, String username, List<String> para, List<String> conCopia, List<String> conCopiaOculta, List<Integer> documentos){
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
				BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummaryVO.getPath()), contenType, folderPercapita.replace("{ANO}", String.valueOf((getAnoCurso() + 1 ))));
				System.out.println("response upload template --->"+response);
				documentService.updateDocumentTemplate(referenciaDocumentoId, response.getNodeRef(), response.getFileName(), contenType);
			}
		}
		Integer idSeguimiento = seguimientoService.createSeguimiento(tareaSeguimiento, subject, body, from, para, conCopia, conCopiaOculta, documentosTmp);
		Seguimiento seguimiento = seguimientoDAO.getSeguimientoById(idSeguimiento);
		return distribucionInicialPercapitaDAO.createSeguimiento(idDistribucionInicialPercapita, seguimiento);
	}

	public List<SeguimientoVO> getBitacora(
			Integer idDistribucionInicialPercapita,
			TareasSeguimiento tareaSeguimiento) {
		return seguimientoService.getBitacora(idDistribucionInicialPercapita, tareaSeguimiento);
	}

	public void elaborarDocumentoFormal(Integer idDistribucionInicialPercapita) {
		System.out.println("DistribucionInicialPercapitaService --- > crearOficioConsulta");
		Integer plantillaIdResolucionAporteEstatalCF = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLARESOLUCIONAPORTEESTATALCF);
		Integer plantillaIdResolucionAporteEstatalUR = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLARESOLUCIONAPORTEESTATALUR);
		if(plantillaIdResolucionAporteEstatalCF == null){
			throw new RuntimeException("No se puede crear Resolucion Aporte Estatal CF, la plantilla no esta cargada");
		}
		if(plantillaIdResolucionAporteEstatalUR == null){
			throw new RuntimeException("No se puede crear Resolucion Aporte Estatal UR, la plantilla no esta cargada");
		}
		try{
			ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryAporteEstatalCFVO = documentService.getDocumentByPlantillaId(plantillaIdResolucionAporteEstatalCF);
			DocumentoVO documentoAporteEstatalCFVO = documentService.getDocument(referenciaDocumentoSummaryAporteEstatalCFVO.getId());
			String templateAporteEstatalCF = tmpDirDoc + File.separator + documentoAporteEstatalCFVO.getName();
			templateAporteEstatalCF = templateAporteEstatalCF.replace(" ", "");
			String filenameAporteEstatalCF = tmpDirDoc + File.separator + new Date().getTime() + "_" + "ResolucionAporteEstatalCF.docx";
			filenameAporteEstatalCF = filenameAporteEstatalCF.replaceAll(" ", "");
			System.out.println("resolucionAporteEstatalCF filename-->"+filenameAporteEstatalCF);
			System.out.println("resolucionAporteEstatalCF template-->"+templateAporteEstatalCF);
			GeneradorWord generadorWordAporteEstatalCF = new GeneradorWord(templateAporteEstatalCF);

			ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryAporteEstatalURVO = documentService.getDocumentByPlantillaId(plantillaIdResolucionAporteEstatalUR);
			DocumentoVO documentoAporteEstatalURVO = documentService.getDocument(referenciaDocumentoSummaryAporteEstatalURVO.getId());
			String templateAporteEstatalUR = tmpDirDoc + File.separator + documentoAporteEstatalURVO.getName();
			templateAporteEstatalUR = templateAporteEstatalUR.replace(" ", "");
			String filenameAporteEstatalUR = tmpDirDoc + File.separator + new Date().getTime() + "_" + "ResolucionAporteEstatalUR.docx";
			filenameAporteEstatalUR = filenameAporteEstatalUR.replaceAll(" ", "");
			System.out.println("resolucionAporteEstatalUR filename-->"+filenameAporteEstatalUR);
			System.out.println("resolucionAporteEstatalUR template-->"+templateAporteEstatalUR);
			GeneradorWord generadorWordAporteEstatalUR = new GeneradorWord(templateAporteEstatalUR);

			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contenTypeAporteEstatalCF = mimemap.getContentType(filenameAporteEstatalCF.toLowerCase());
			System.out.println("templateAporteEstatalCF->"+templateAporteEstatalCF);

			String contenTypeAporteEstatalUR = mimemap.getContentType(filenameAporteEstatalUR.toLowerCase());
			System.out.println("templateAporteEstatalUR->"+templateAporteEstatalUR);

			List<AntecendentesComuna> antecendentesComuna = antecedentesComunaDAO.findAntecendentesComunaByidDistribucionInicialPercapita(idDistribucionInicialPercapita);
			if(antecendentesComuna != null && antecendentesComuna.size() > 0){
				for(AntecendentesComuna antecendenteComuna : antecendentesComuna){
					AntecendentesComunaCalculado antecendentesComunaCalculadoVigente = antecedentesComunaDAO.findByAntecedentesDistrinbucionInicialVigente (antecendenteComuna.getIdAntecedentesComuna(), idDistribucionInicialPercapita);
					if(antecendenteComuna.getClasificacion() != null){
						if(TipoComuna.COSTOFIJO.getId().equals(antecendenteComuna.getClasificacion().getIdTipoComuna())){
							Map<String, Object> parametersAporteEstatalCF = new HashMap<String, Object>();
							parametersAporteEstatalCF.put("{comuna_seleccionada}", antecendenteComuna.getIdComuna().getNombre());
							Long aporteMensual = ((antecendentesComunaCalculadoVigente.getPercapitaMes() == null)? new Integer(0) : antecendentesComunaCalculadoVigente.getPercapitaMes());
							parametersAporteEstatalCF.put("{aporte_mensual}", StringUtil.formatNumber(aporteMensual));
							parametersAporteEstatalCF.put("{ano_curso}", (getAnoCurso() + 1));

							generadorWordAporteEstatalCF.saveContent(documentoAporteEstatalCFVO.getContent(), XWPFDocument.class);
							String filenameAporteEstatalCFTmp = filenameAporteEstatalCF.replace(".docx", "-" +  antecendenteComuna.getIdComuna().getNombre() + ".docx");
							filenameAporteEstatalCFTmp = StringUtil.removeSpanishAccents(filenameAporteEstatalCFTmp);
							GeneradorResolucionAporteEstatal generadorResolucionAporteEstatalCF = new GeneradorResolucionAporteEstatal(filenameAporteEstatalCFTmp, templateAporteEstatalCF);
							generadorResolucionAporteEstatalCF.replaceValues(parametersAporteEstatalCF, XWPFDocument.class);
							BodyVO responseAporteEstatalCF = alfrescoService.uploadDocument(new File(filenameAporteEstatalCFTmp), contenTypeAporteEstatalCF, folderPercapita.replace("{ANO}", String.valueOf((getAnoCurso() + 1 )) ));
							System.out.println("response responseAporteEstatalCF --->"+responseAporteEstatalCF);
							plantillaIdResolucionAporteEstatalCF = documentService.createDocumentPercapita(idDistribucionInicialPercapita, antecendenteComuna.getIdComuna().getId(), TipoDocumentosProcesos.RESOLUCIONAPORTEESTATALCF, responseAporteEstatalCF.getNodeRef(), responseAporteEstatalCF.getFileName(), contenTypeAporteEstatalCF);
						}else{
							Map<String, Object> parametersAporteEstatalUR = new HashMap<String, Object>();
							parametersAporteEstatalUR.put("{ano_curso}", (getAnoCurso() + 1));
							parametersAporteEstatalUR.put("{comuna_seleccionada}", antecendenteComuna.getIdComuna().getNombre());
							Integer poblacion = ((antecendentesComunaCalculadoVigente.getPoblacion() == null) ? new Integer(0): antecendentesComunaCalculadoVigente.getPoblacion()) + ((antecendentesComunaCalculadoVigente.getPoblacionMayor() != null)?new Integer(0): antecendentesComunaCalculadoVigente.getPoblacionMayor());
							parametersAporteEstatalUR.put("{poblacion_seleccionada}", StringUtil.formatNumber(poblacion));
							Double perCapitaComunalMes = ((antecendentesComunaCalculadoVigente.getValorPerCapitaComunalMes() == null)?new Double(0): antecendentesComunaCalculadoVigente.getValorPerCapitaComunalMes());
							parametersAporteEstatalUR.put("{monto_seleccionado}", StringUtil.formatNumber(Math.round(perCapitaComunalMes)));
							Long percapitaMes = ((antecendentesComunaCalculadoVigente.getPercapitaMes() == null)? new Integer(0): antecendentesComunaCalculadoVigente.getPercapitaMes());
							parametersAporteEstatalUR.put("{aporte_mensual}", StringUtil.formatNumber(percapitaMes));
							generadorWordAporteEstatalUR.saveContent(documentoAporteEstatalURVO.getContent(), XWPFDocument.class);
							String filenameAporteEstatalURTmp = filenameAporteEstatalUR.replace(".docx", "-" +  antecendenteComuna.getIdComuna().getNombre() + ".docx");
							filenameAporteEstatalURTmp = StringUtil.removeSpanishAccents(filenameAporteEstatalURTmp);
							GeneradorResolucionAporteEstatal generadorResolucionAporteEstatalUR = new GeneradorResolucionAporteEstatal(filenameAporteEstatalURTmp, templateAporteEstatalUR);
							generadorResolucionAporteEstatalUR.replaceValues(parametersAporteEstatalUR, XWPFDocument.class);
							BodyVO responseAporteEstatalUR = alfrescoService.uploadDocument(new File(filenameAporteEstatalURTmp), contenTypeAporteEstatalUR, folderPercapita.replace("{ANO}", String.valueOf((getAnoCurso() + 1 )) ));
							System.out.println("response responseAporteEstatalUR --->"+responseAporteEstatalUR);
							plantillaIdResolucionAporteEstatalUR = documentService.createDocumentPercapita(idDistribucionInicialPercapita, antecendenteComuna.getIdComuna().getId(), TipoDocumentosProcesos.RESOLUCIONAPORTEESTATALUR, responseAporteEstatalUR.getNodeRef(), responseAporteEstatalUR.getFileName(), contenTypeAporteEstatalUR);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void enviarDecretoAporteEstatal(Integer borradorAporteEstatalId, String username) {
		System.out.println("enviarDecretoAporteEstatal--> "+borradorAporteEstatalId+" username="+username);
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryDecretoAporteEstatal = documentService.getDocumentById(borradorAporteEstatalId);
		DocumentoVO documentDecretoAporteEstatalVO = documentService.getDocument(referenciaDocumentoSummaryDecretoAporteEstatal.getId());
		String fileNameDecretoAporteEstatal = tmpDirDoc + File.separator + documentDecretoAporteEstatalVO.getName();
		GeneradorWord generadorWordDecretoAporteEstatal = new GeneradorWord(fileNameDecretoAporteEstatal);
		try {
			generadorWordDecretoAporteEstatal.saveContent(documentDecretoAporteEstatalVO.getContent(), XWPFDocument.class);
			Usuario usuario = usuarioDAO.getUserByUsername(username);
			List<EmailService.Adjunto> adjuntos = new ArrayList<EmailService.Adjunto>();
			EmailService.Adjunto adjunto = new EmailService.Adjunto();
			adjunto.setDescripcion("Borrador Decreto Aporte Estatal");
			adjunto.setName(documentDecretoAporteEstatalVO.getName());
			adjunto.setUrl((new File(fileNameDecretoAporteEstatal)).toURI().toURL());
			adjuntos.add(adjunto);
			emailService.sendMail(usuario.getEmail().getValor(), "Borrador Decreto Aporte Estatal", "Estimado " + username + ": <br /> <p> Se Adjunta Borrador Decreto Aporte Estatal</p>", adjuntos);
			System.out.println("enviarDecretoAporteEstatal se ejecuto correctamente");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Integer elaborarBorradorDecretoAporteEstatal(Integer idDistribucionInicialPercapita) {
		Integer plantillaIdBorradorDecretoAporteEstatal = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLABORRADORAPORTEESTATAL);
		if(plantillaIdBorradorDecretoAporteEstatal == null){
			throw new RuntimeException("No se puede crear Borrador Decreto Aporte Estatal, la plantilla no esta cargada");
		}
		try {
			ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryBorradorAporteEstatalVO = documentService.getDocumentByPlantillaId(plantillaIdBorradorDecretoAporteEstatal);
			DocumentoVO documentoBorradorAporteEstatalVO = documentService.getDocument(referenciaDocumentoSummaryBorradorAporteEstatalVO.getId());
			String templateBorradorAporteEstatal = tmpDirDoc + File.separator + documentoBorradorAporteEstatalVO.getName();
			templateBorradorAporteEstatal = templateBorradorAporteEstatal.replace(" ", "");
			String filenameBorradorAporteEstatal = tmpDirDoc + File.separator + new Date().getTime() + "_" + "BorradorAporteEstatal.docx";
			filenameBorradorAporteEstatal = filenameBorradorAporteEstatal.replaceAll(" ", "");
			System.out.println("filenameBorradorAporteEstatal filename-->"+filenameBorradorAporteEstatal);
			System.out.println("templateBorradorAporteEstatal template-->"+templateBorradorAporteEstatal);
			GeneradorWord generadorWordPlantillaBorradorDecretoAporteEstatal = new GeneradorWord(templateBorradorAporteEstatal);
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contenTypeBorradorAporteEstatal = mimemap.getContentType(filenameBorradorAporteEstatal.toLowerCase());
			System.out.println("contenTypeBorradorAporteEstatal->"+contenTypeBorradorAporteEstatal);
			generadorWordPlantillaBorradorDecretoAporteEstatal.saveContent(documentoBorradorAporteEstatalVO.getContent(), XWPFDocument.class);
			Map<String, Object> parametersBorradorAporteEstatal = new HashMap<String, Object>();
			AnoEnCurso anoEnCurso = anoDAO.getAnoById((getAnoCurso() + 1));
			Integer percapitaBasal = anoEnCurso.getMontoPercapitalBasal();
			Integer asignacionAdultoMayor = anoEnCurso.getAsignacionAdultoMayor();
			List<AntecendentesComunaCalculado> antecedentesComunaCalculado = antecedentesComunaDAO.findAntecedentesComunaCalculadosByDistribucionInicialPercapitaVigente(idDistribucionInicialPercapita);
			Long aporteEstatal = 0L;
			if(antecedentesComunaCalculado != null && antecedentesComunaCalculado.size() > 0){
				for(AntecendentesComunaCalculado antecendentesComunaCalculado : antecedentesComunaCalculado){
					aporteEstatal+=((antecendentesComunaCalculado.getPercapitaAno() == null) ? 0L : antecendentesComunaCalculado.getPercapitaAno()) + ((antecendentesComunaCalculado.getDesempenoDificil() == null) ? 0 : antecendentesComunaCalculado.getDesempenoDificil());
				}
			}
			parametersBorradorAporteEstatal.put("{ano_curso}", (getAnoCurso() + 1));
			parametersBorradorAporteEstatal.put("{aporte_estatal}", StringUtil.formatNumber(aporteEstatal));
			parametersBorradorAporteEstatal.put("{aporte_estatal_palabra}", NumberToLetters.readNumber(aporteEstatal.toString(), "," ,"pesos"));
			parametersBorradorAporteEstatal.put("{percapita_basal}", StringUtil.formatNumber(percapitaBasal));
			parametersBorradorAporteEstatal.put("{percapita_basal_palabra}",  NumberToLetters.readNumber(percapitaBasal.toString(), "," ,"pesos"));
			parametersBorradorAporteEstatal.put("{asignacion_adulto_mayor}", StringUtil.formatNumber(asignacionAdultoMayor));
			GeneradorResolucionAporteEstatal generadorWordDecretoBorradorAporteEstatal = new GeneradorResolucionAporteEstatal(filenameBorradorAporteEstatal, templateBorradorAporteEstatal);
			generadorWordDecretoBorradorAporteEstatal.replaceValues(parametersBorradorAporteEstatal, XWPFDocument.class);
			BodyVO responseBorradorAporteEstatal = alfrescoService.uploadDocument(new File(filenameBorradorAporteEstatal), contenTypeBorradorAporteEstatal, folderPercapita.replace("{ANO}", String.valueOf((getAnoCurso() + 1 )) ));
			System.out.println("response responseBorradorAporteEstatal --->"+responseBorradorAporteEstatal);
			plantillaIdBorradorDecretoAporteEstatal = documentService.createDocumentPercapita(idDistribucionInicialPercapita, TipoDocumentosProcesos.BORRADORAPORTEESTATAL, responseBorradorAporteEstatal.getNodeRef(), responseBorradorAporteEstatal.getFileName(), contenTypeBorradorAporteEstatal);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (Docx4JException e) {
			e.printStackTrace();
		}
		return plantillaIdBorradorDecretoAporteEstatal;
	}

	public void notificarFinElaboracionResolucion(Integer idDistribucionInicialPercapita, String username) {
		try {
			Usuario usuario = usuarioDAO.getUserByUsername(username);
			List<EmailService.Adjunto> adjuntos = new ArrayList<EmailService.Adjunto>();
			emailService.sendMail(usuario.getEmail().getValor(), "Documentos Formales(Resoluciones Comunales de Aporte Estatal)", "Estimado " + username + ": <br /> <p> Se completo exitosamente la generación Documentos Formales(Resoluciones Comunales de Aporte Estatal)</p>", adjuntos);
			System.out.println("notificarFinElaboracionResolucion se ejecuto correctamente");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void moveToAlfresco(Integer idDistribucionInicialPercapita, Integer referenciaDocumentoId, TipoDocumentosProcesos tipoDocumento, Boolean lastVersion ) {
		System.out.println("Buscando referenciaDocumentoId="+referenciaDocumentoId);
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummary = documentService.getDocumentSummary(referenciaDocumentoId);
		System.out.println("Buscando referenciaDocumentoSummary="+referenciaDocumentoSummary);
		if(referenciaDocumentoSummary != null){
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contenType= mimemap.getContentType(referenciaDocumentoSummary.getPath().toLowerCase());
			BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummary.getPath()), contenType, folderPercapita.replace("{ANO}", String.valueOf((getAnoCurso() + 1 ))));
			System.out.println("response upload template --->"+response);
			documentService.updateDocumentTemplate(referenciaDocumentoSummary.getId(), response.getNodeRef(), response.getFileName(), contenType);
			DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findById(idDistribucionInicialPercapita);
			documentService.createDocumentPercapita(distribucionInicialPercapita, tipoDocumento, referenciaDocumentoId, lastVersion);
		}
	}

	public ReferenciaDocumentoSummaryVO getLastDocumentoSummaryByDistribucionInicialPercapitaType(Integer idDistribucionInicialPercapita,TipoDocumentosProcesos tipoDocumento){
		return documentService.getLastDocumentoSummaryByDistribucionInicialPercapitaType(idDistribucionInicialPercapita, tipoDocumento);
	}

	public Integer cargarPlantillaCorreo(TipoDocumentosProcesos plantilla, File file){
		Integer plantillaId = documentService.getPlantillaByType(plantilla);
		String filename = "plantillaCorreoResolucionRebaja.xml";
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		filename = tmpDir + File.separator + filename;
		String contentType = mimemap.getContentType(filename.toLowerCase());
		if(plantillaId == null){
			try {
				BodyVO response = alfrescoService.uploadDocument(file, contentType, folderTemplatePercapita);
				System.out.println("response upload template --->"+response);
				plantillaId = documentService.createTemplate(plantilla, response.getNodeRef(), response.getFileName(), contentType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			plantillaId = documentService.getDocumentoIdByPlantillaId(plantillaId);
			try {
				BodyVO response = alfrescoService.uploadDocument(file, contentType, folderTemplatePercapita);
				System.out.println("response upload template --->"+response);
				documentService.updateDocumentTemplate(plantillaId, response.getNodeRef(), response.getFileName(), contentType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return plantillaId;
	}

	@Asynchronous
	public void enviarResolucionesServicioSalud(Integer idDistribucionInicialPercapita) {
		//MODIFICAR LA LOGICA DEL ENVIO DE CORREO ES DISTINTA EN ESTE CASO
		Integer idPlantillaCorreo = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLACORREORESOLUCIONESPERCAPITA);
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
			List<ServiciosVO> servicios = servicioSaludService.getAllServiciosVO();
			if(servicios != null && servicios.size() > 0){
				for(ServiciosVO servicio : servicios){
					List<String> to = new ArrayList<String>();
					if(servicio.getDirector() != null && servicio.getDirector().getCorreo() != null){
						to.add(servicio.getDirector().getCorreo());
					}
					List<String> cc = new ArrayList<String>();
					List<String> cco = new ArrayList<String>();
					if((servicio.getEncargadoAps() != null) || (servicio.getEncargadoFinanzasAps() != null)){
						if(servicio.getEncargadoAps() != null &&  servicio.getEncargadoAps().getCorreo() != null){
							cc.add(servicio.getEncargadoAps().getCorreo());
						}
						if(servicio.getEncargadoFinanzasAps() != null &&  servicio.getEncargadoFinanzasAps().getCorreo() != null){
							cc.add(servicio.getEncargadoFinanzasAps().getCorreo());
						}
					}
					
					ReferenciaDocumentoSummaryVO referenciaDocumentoFinalSummaryVO = null;
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
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Asynchronous
	public void enviarConsultaRegional(Integer idDistribucionInicialPercapita, Integer oficioConsultaId) {
		System.out.println("enviarConsultaRegional idDistribucionInicialPercapita="+idDistribucionInicialPercapita + " oficioConsultaId="+oficioConsultaId);
		Integer idPlantillaCorreo = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLACORREOCONSULTAREGIONALPERCAPITA);
		if(idPlantillaCorreo == null){
			throw new RuntimeException("No se puede crear plantilla correo notificación Resolución Rebaja Aporte Estatal, la plantilla no esta cargada");
		}
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryPlantillaCorreoVO = documentService.getDocumentByPlantillaId(idPlantillaCorreo);
		DocumentoVO documentoPlantillaCorreoVO = documentService.getDocument(referenciaDocumentoSummaryPlantillaCorreoVO.getId());
		String templatePlantillaCorreo = tmpDirDoc + File.separator + documentoPlantillaCorreoVO.getName();
		templatePlantillaCorreo = templatePlantillaCorreo.replace(" ", "");

		System.out.println("templatePlantillaCorreo template-->"+templatePlantillaCorreo);
		GeneradorXML generadorXMLPlantillaCorreo = new GeneradorXML(templatePlantillaCorreo);
		Email emailPLantilla = null;
		try {
			emailPLantilla = generadorXMLPlantillaCorreo.createObject(Email.class, documentoPlantillaCorreoVO.getContent());
		} catch (JAXBException jaxBException) {
			jaxBException.printStackTrace();
		}
		try{
			List<EmailService.Adjunto> adjuntos = new ArrayList<EmailService.Adjunto>();
			/*ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryOficioConsulta = documentService.getDocumentById(oficioConsultaId);
			if(referenciaDocumentoSummaryOficioConsulta == null){
				List<DocumentoDistribucionInicialPercapita> documentosOficioConsulta  = distribucionInicialPercapitaDAO.getByIdDistribucionInicialPercapitaTipo(idDistribucionInicialPercapita, TipoDocumentosProcesos.OFICIOCONSULTA);
				if(documentosOficioConsulta != null && documentosOficioConsulta.size() > 0){
					referenciaDocumentoSummaryOficioConsulta = new ReferenciaDocumentoMapper().getSummary(documentosOficioConsulta.get(0).getIdDocumento());
				}
			}*/
			
			ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryOficioConsulta = null;
			List<ReferenciaDocumentoSummaryVO> referenciasDocumentoSummaryVO = documentService.getVersionFinalDistribucionInicialPercapitaByType(idDistribucionInicialPercapita, TipoDocumentosProcesos.OFICIOCONSULTA);
			if(referenciasDocumentoSummaryVO != null && referenciasDocumentoSummaryVO.size() > 0){
				for(ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO : referenciasDocumentoSummaryVO){
					String contentType = new MimetypesFileTypeMap().getContentType(referenciaDocumentoSummaryVO.getPath());
					System.out.println("contentType="+contentType+" archivo enviado por correo");
					if (contentType.equals("application/pdf")) {
						referenciaDocumentoSummaryOficioConsulta = referenciaDocumentoSummaryVO;
						break;
					}
					if(referenciaDocumentoSummaryVO.getPath().indexOf(".") != -1){
						String extension = referenciaDocumentoSummaryVO.getPath().substring(referenciaDocumentoSummaryVO.getPath().lastIndexOf(".") + 1, referenciaDocumentoSummaryVO.getPath().length());
						if("pdf".equalsIgnoreCase(extension)){
							referenciaDocumentoSummaryOficioConsulta = referenciaDocumentoSummaryVO;
							break;
						}
					}
					referenciaDocumentoSummaryOficioConsulta = referenciaDocumentoSummaryVO;
				}
			}
			DocumentoVO documentDocumentoOficioConsultaVO = documentService.getDocument(referenciaDocumentoSummaryOficioConsulta.getId());
			String fileNameDocumentoOficioConsulta = tmpDirDoc + File.separator + documentDocumentoOficioConsultaVO.getName();
			GeneradorDocumento generadorWordOficioConsulta = new GeneradorDocumento(fileNameDocumentoOficioConsulta);
			generadorWordOficioConsulta.saveContent(documentDocumentoOficioConsultaVO.getContent());
			EmailService.Adjunto adjunto = new EmailService.Adjunto();
			adjunto.setDescripcion("Oficio Consulta");
			adjunto.setName(documentDocumentoOficioConsultaVO.getName());
			adjunto.setUrl((new File(fileNameDocumentoOficioConsulta)).toURI().toURL());
			adjuntos.add(adjunto);
			List<RegionVO> regiones = servicioSaludService.getAllRegionesVO();
			if(regiones != null && regiones.size() > 0){
				for(RegionVO region : regiones){
					List<String> to = new ArrayList<String>();
					if(region.getSecretarioRegional() != null && region.getSecretarioRegional().getCorreo() != null){
						to.add(region.getSecretarioRegional().getCorreo());
					}
					List<String> cc = new ArrayList<String>();
					List<String> cco = new ArrayList<String>();
					System.out.println("Enviando a region="+region.getDescRegion());
					if((region.getServicios() != null) || (region.getServicios().size() > 0)){
						for(ServiciosVO serviciosVO : region.getServicios()){
							System.out.println("Servicio="+serviciosVO.getNombre_servicio());
							if(serviciosVO.getEncargadoAps() != null &&  serviciosVO.getEncargadoAps().getCorreo() != null){
								cc.add(serviciosVO.getEncargadoAps().getCorreo());
							}
							if(serviciosVO.getEncargadoFinanzasAps() != null &&  serviciosVO.getEncargadoFinanzasAps().getCorreo() != null){
								cc.add(serviciosVO.getEncargadoFinanzasAps().getCorreo());
							}
						}
					}
					if(emailPLantilla != null && emailPLantilla.getAsunto() != null && emailPLantilla.getCuerpo() != null){
						emailService.sendMail(to, cc, cco, emailPLantilla.getAsunto(), emailPLantilla.getCuerpo().replaceAll("(\r\n|\n)", "<br />"), adjuntos);
					}else{
						emailService.sendMail(to, cc, cco , "Oficio Consulta", "Estimados: <br /> <p> se adjuntan los documentos  Oficio Consulta</p>", adjuntos);
					}
					System.out.println();
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Integer getPlantillaCorreo(TareasSeguimiento tareaSeguimiento) {
		Integer plantillaId = null;
		switch (tareaSeguimiento) {
		case HACERSEGUIMIENTOOFICIO:
			plantillaId = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLACORREOCONSULTAREGIONALPERCAPITA);
			plantillaId = documentService.getDocumentoIdByPlantillaId(plantillaId);
			break;
		case HACERSEGUIMIENTORESOLUCIONES:
			plantillaId = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLACORREORESOLUCIONESPERCAPITA);
			plantillaId = documentService.getDocumentoIdByPlantillaId(plantillaId);
			break;
		case HACERSEGUIMIENTODECRETO:

			break;
		case HACERSEGUIMIENTOTOMARAZON:

			break;
		default:
			break;
		}
		return plantillaId;
	}

	public Integer cargarPlantillaCorreo(TareasSeguimiento tareaSeguimiento, File file) {
		Integer plantillaId = null;
		String filename = null;
		TipoDocumentosProcesos plantilla = null;
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		switch (tareaSeguimiento) {
		case HACERSEGUIMIENTOOFICIO:
			plantilla = TipoDocumentosProcesos.PLANTILLACORREOCONSULTAREGIONALPERCAPITA;
			plantillaId = documentService.getPlantillaByType(plantilla);
			filename = "PlantillaCorreoConsultaRegional.xml";
			break;
		case HACERSEGUIMIENTORESOLUCIONES:
			plantilla = TipoDocumentosProcesos.PLANTILLACORREORESOLUCIONESPERCAPITA;
			plantillaId = documentService.getPlantillaByType(plantilla);
			filename = "PlantillaCorreoConsultaResoluciones.xml";
			break;
		case HACERSEGUIMIENTODECRETO:

			break;
		case HACERSEGUIMIENTOTOMARAZON:

			break;
		default:
			break;
		}
		filename = tmpDir + File.separator + filename;
		String contentType = mimemap.getContentType(filename.toLowerCase());
		if(plantillaId == null){
			try {
				BodyVO response = alfrescoService.uploadDocument(file, contentType, folderTemplatePercapita);
				System.out.println("response upload template --->"+response);
				plantillaId = documentService.createTemplate(plantilla, response.getNodeRef(), response.getFileName(), contentType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			plantillaId = documentService.getDocumentoIdByPlantillaId(plantillaId);
			try {
				BodyVO response = alfrescoService.uploadDocument(file, contentType, folderTemplatePercapita);
				System.out.println("response upload template --->"+response);
				documentService.updateDocumentTemplate(plantillaId, response.getNodeRef(), response.getFileName(), contentType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return plantillaId;
	}

	public void administrarVersionesFinalesAlfresco(Integer idDistribucionInicialPercapita) {
		System.out.println("DistribucionInicialPercapitaService administrarVersionesFinalesAlfresco eliminar todas las versiones que no sean finales");
		TipoDocumentosProcesos[] tiposDocumentos = {TipoDocumentosProcesos.RESOLUCIONAPORTEESTATALUR, TipoDocumentosProcesos.RESOLUCIONAPORTEESTATALCF};
		List<DocumentoDistribucionInicialPercapita> documentosResoluciones = distribucionInicialPercapitaDAO.getByIdDistribucionInicialPercapitaTipoNotFinal(idDistribucionInicialPercapita, tiposDocumentos);
		if(documentosResoluciones != null && documentosResoluciones.size() > 0){
			for(DocumentoDistribucionInicialPercapita documentoDistribucionInicialPercapita : documentosResoluciones){
				String key = ((documentoDistribucionInicialPercapita.getIdDocumento().getNodeRef() == null) ? documentoDistribucionInicialPercapita.getIdDocumento().getPath() : documentoDistribucionInicialPercapita.getIdDocumento().getNodeRef().replace("workspace://SpacesStore/", ""));
				System.out.println("key->"+key);
				alfrescoService.delete(key);
				distribucionInicialPercapitaDAO.deleteDocumentoDistribucionInicialPercapita(documentoDistribucionInicialPercapita.getIdDocumentoDistribucionInicialPercapita());
				distribucionInicialPercapitaDAO.deleteDocumento(documentoDistribucionInicialPercapita.getIdDocumento().getId());
			}
		}
	}

	public void creacionAntecedentesComuna(String usuario, Integer idDistribucionInicialPercapita) {
		List<ServiciosVO> servicios = servicioSaludService.getServiciosOrderId();
		if(servicios != null && servicios.size()>0){
			Integer idAnoSiguiente = getAnoCurso() + 1;
			for(ServiciosVO servicio : servicios){
				if(servicio.getComunas() != null && servicio.getComunas().size() > 0){
					for(ComunaSummaryVO comuna : servicio.getComunas()){
						AntecendentesComuna antecendentesComunaSiguiente = antecedentesComunaDAO.getAntecendentesComunaByComunaAno(comuna.getId(), idAnoSiguiente);
						if(antecendentesComunaSiguiente == null){
							AntecendentesComuna antecendentesComunaActual = antecedentesComunaDAO.getAntecendentesComunaByComunaAno(comuna.getId(), getAnoCurso());
							antecendentesComunaSiguiente = new AntecendentesComuna();
							antecendentesComunaSiguiente.setTramoPobreza(antecendentesComunaActual.getTramoPobreza());
							antecendentesComunaSiguiente.setAsignacionZona(antecendentesComunaActual.getAsignacionZona());
							antecendentesComunaSiguiente.setClasificacion(antecendentesComunaActual.getClasificacion());
							antecendentesComunaSiguiente.setIdComuna(antecendentesComunaActual.getIdComuna());
							AnoEnCurso anoSiguiente = anoDAO.getAnoById(idAnoSiguiente);
							if(anoSiguiente == null){
								AnoEnCurso anoActual = anoDAO.getAnoById(getAnoCurso());
								anoSiguiente = new AnoEnCurso();
								anoSiguiente.setAno(getAnoCurso() + 1);
								anoSiguiente.setMontoPercapitalBasal(anoActual.getMontoPercapitalBasal());
								anoSiguiente.setAsignacionAdultoMayor(anoActual.getAsignacionAdultoMayor());
								anoSiguiente.setInflactor(anoActual.getInflactor());
								anoSiguiente.setInflactorMarcoPresupuestario(anoActual.getInflactorMarcoPresupuestario());
								alfrescoService.createRepoAlfresco(idAnoSiguiente);
								anoSiguiente.setRepoAlfresco(true);
								anoDAO.save(anoSiguiente);
							}
							antecendentesComunaSiguiente.setAnoAnoEnCurso(anoSiguiente);
							antecedentesComunaDAO.save(antecendentesComunaSiguiente);
							if(TipoComuna.COSTOFIJO.getId().equals(antecendentesComunaSiguiente.getClasificacion().getIdTipoComuna())){
								AntecendentesComunaCalculado antecendentesComunaCalculadoSiguiente = new AntecendentesComunaCalculado();
								antecendentesComunaCalculadoSiguiente.setAntecedentesComuna(antecendentesComunaSiguiente);
								DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findById(idDistribucionInicialPercapita);
								antecendentesComunaCalculadoSiguiente.setDistribucionInicialPercapita(distribucionInicialPercapita);
								antecedentesComunaDAO.createAntecendentesComunaCalcuado(antecendentesComunaCalculadoSiguiente);
							}
						}
					}
				}
			}
		}
	}

	public void moveToAlfrescoDistribucionInicialPercapita(Integer idDistribucionInicialPercapita, Integer idServicio, Integer referenciaDocumentoId, TipoDocumentosProcesos tipoDocumento,	Boolean lastVersion) {
		System.out.println("Buscando referenciaDocumentoId="+referenciaDocumentoId);
		System.out.println("Buscando idDistribucionInicialPercapita="+idDistribucionInicialPercapita);
		System.out.println("Buscando idServicio="+idServicio);
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummary = documentService.getDocumentSummary(referenciaDocumentoId);
		System.out.println("Buscando referenciaDocumentoSummary="+referenciaDocumentoSummary);
		if(referenciaDocumentoSummary != null){
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contenType = mimemap.getContentType(referenciaDocumentoSummary.getPath().toLowerCase());
			BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummary.getPath()), contenType, folderPercapita.replace("{ANO}", String.valueOf((getAnoCurso() + 1 )) ));
			System.out.println("response upload template --->"+response);
			documentService.updateDocumentTemplate(referenciaDocumentoSummary.getId(), response.getNodeRef(), response.getFileName(), contenType);
			DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findById(idDistribucionInicialPercapita);
			documentService.createDocumentPercapita(distribucionInicialPercapita, idServicio, tipoDocumento, referenciaDocumentoId, lastVersion);
		}
	}

	public int countVersionFinalDistribucionInicialPercapitaByType(Integer idDistribucionInicialPercapita, TipoDocumentosProcesos tipoDocumento) {
		List<ReferenciaDocumentoSummaryVO> versionesFinales = documentService.getVersionFinalDistribucionInicialPercapitaByType(idDistribucionInicialPercapita, tipoDocumento);
		if(versionesFinales != null && versionesFinales.size() > 0){
			return versionesFinales.size();
		}
		return 0;
	}

	public int countVersionFinalDistribucionInicialPercapitaResoluciones(Integer idDistribucionInicialPercapita, Integer idServicio) {
		List<ReferenciaDocumentoSummaryVO> versionesFinales = documentService.getVersionFinalDistribucionInicialPercapitaByType(idDistribucionInicialPercapita, idServicio, TipoDocumentosProcesos.RESOLUCIONAPORTEESTATALUR);
		if(versionesFinales != null && versionesFinales.size() > 0){
			return versionesFinales.size();
		} 
		return 0;
	}

}
