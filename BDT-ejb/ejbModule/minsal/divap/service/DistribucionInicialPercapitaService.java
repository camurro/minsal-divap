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
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.doc.GeneradorOficioConsulta;
import minsal.divap.doc.GeneradorResolucionAporteEstatal;
import minsal.divap.doc.GeneradorWord;
import minsal.divap.doc.GeneradorWordBorradorAporteEstatal;
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
import minsal.divap.vo.AsignacionDistribucionPerCapitaVO;
import minsal.divap.vo.BaseVO;
import minsal.divap.vo.BodyVO;
import minsal.divap.vo.CalculoPercapitaVO;
import minsal.divap.vo.CellTypeExcelVO;
import minsal.divap.vo.DesempenoDificilVO;
import minsal.divap.vo.DocumentoVO;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.SeguimientoVO;
import minsal.divap.vo.VariacionPoblacionVO;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import cl.minsal.divap.model.AntecendentesComuna;
import cl.minsal.divap.model.AntecendentesComunaCalculado;
import cl.minsal.divap.model.DistribucionInicialPercapita;
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
		return distribucionInicialPercapitaDAO.crearIntanciaDistribucionInicialPercapita(usuario);
	}

	public Integer cargarPlantilla(TipoDocumentosProcesos plantilla, File file){
		Integer plantillaId = documentService.getPlantillaByType(plantilla);
		String filename = null;
		switch (plantilla) {
		case PLANTILLAASIGNACIONDESEMPENODIFICIL:
			filename = "plantillaDesempenoDificil.xlsx";
			break;
		case PLANTILLAPOBLACIONINSCRITA:
			filename = "plantillaPercapita.xlsx";
			break;	
		case PLANTILLAOFICIOCONSULTA:
			filename = "plantillaOficioConsulta.docx";
			break;	
		default:
			break;
		}
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
			Integer referenciaDocumentoId = documentService.getDocumentoIdByPlantillaId(plantillaId);
			try {
				BodyVO response = alfrescoService.uploadDocument(file, contentType, folderTemplatePercapita);
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
			List<BaseVO> servicios = servicioSaludService.getAllServicios();

			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String filename = tmpDir + File.separator + "plantillaDesempenoDificil.xlsx";
			String contenType = mimemap.getContentType(filename.toLowerCase());
			GeneradorExcel generadorExcel = new GeneradorExcel(filename);
			List<String> headers = new ArrayList<String>();
			headers.add("REGION");
			headers.add("SERVICIO");
			headers.add("COMUNA");
			headers.add("VALOR BÁSICO DE ASIGNACIÓN POR DESEMPEÑO DIFICIL");
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

	public Integer getIdPlantillaRecursosPerCapita(){
		
		Integer plantillaId = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLAPOBLACIONINSCRITA);
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
			AntecendentesComuna antecendentesComuna = antecedentesComunaDAO.findAntecendentesComunaByComunaServicioAno(calculoPercapitaVO.getServicio(), calculoPercapitaVO.getComuna(), getAnoCurso());
			if(antecendentesComuna != null){
				AntecendentesComunaCalculado antecendentesComunaCalculado = antecedentesComunaDAO.findByAntecedentesDistrinbucionInicial(antecendentesComuna.getIdAntecedentesComuna(), idDistribucionInicialPercapita);
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

	private Integer getAnoCurso() {
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
			AntecendentesComuna antecendentesComuna = antecedentesComunaDAO.findAntecendentesComunaByComunaServicioAno(desempenoDificilVO.getServicio(), desempenoDificilVO.getComuna(), getAnoCurso());
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
		List<AntecendentesComunaCalculado>  antecendentesComunaCalculado = antecedentesComunaDAO.findAntecedentesComunaCalculadosByDistribucionInicialPercapita(idDistribucionInicialPercapita);
		if(antecendentesComunaCalculado != null && antecendentesComunaCalculado.size() > 0){
			for(AntecendentesComunaCalculado antecendenteComunaCalculado: antecendentesComunaCalculado){
				if(antecendenteComunaCalculado.getAntecedentesComuna() != null && antecendenteComunaCalculado.getAntecedentesComuna().getTramoPobreza() != null){
					Integer percapitaBasal = antecendenteComunaCalculado.getAntecedentesComuna().getAnoAnoEnCurso().getMontoPercapitalBasal();
					Double pobreza = percapitaBasal * antecendenteComunaCalculado.getAntecedentesComuna().getTramoPobreza().getValor();
					antecendenteComunaCalculado.setPobreza(pobreza);
					Double ruralidad = (antecendenteComunaCalculado.getAntecedentesComuna().getAnoAnoEnCurso().getMontoPercapitalBasal() + pobreza) * 0.2;
					antecendenteComunaCalculado.setRuralidad(ruralidad);
					Double valorReferencialZona = (percapitaBasal + pobreza + ruralidad) * antecendenteComunaCalculado.getAntecedentesComuna().getAsignacionZona().getValor();
					antecendenteComunaCalculado.setValorReferencialZona(valorReferencialZona);
					Double valorPerCapitaComunalMes = percapitaBasal + pobreza + ruralidad +valorReferencialZona;
					antecendenteComunaCalculado.setValorPerCapitaComunalMes(valorPerCapitaComunalMes);
					System.out.println("antecendenteComunaCalculado.getAntecedentesComuna().getIdAntecedentesComuna()="+antecendenteComunaCalculado.getAntecedentesComuna().getIdAntecedentesComuna());
					System.out.println("getAnoAnterior()=" + (getAnoCurso() - 1));
					Double perCapitaCostoFijo = antecedentesComunaDAO.findPerCapitaCostoFijoByServicioComunaAnoAnterior(antecendenteComunaCalculado.getAntecedentesComuna().getIdComuna().getId(), antecendenteComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId(), (getAnoCurso() - 1));
					System.out.println("perCapitaCostoFijo="+perCapitaCostoFijo);
					if(antecendenteComunaCalculado.getAntecedentesComuna().getClasificacion() != null){
						Integer perCapitaAno = null;
						if(TipoComuna.RURAL.getId() == antecendenteComunaCalculado.getAntecedentesComuna().getClasificacion().getIdTipoComuna()){
							perCapitaAno = (int) (((valorPerCapitaComunalMes * antecendenteComunaCalculado.getPoblacion() + antecendenteComunaCalculado.getPoblacionMayor() * antecendenteComunaCalculado.getAntecedentesComuna().getAnoAnoEnCurso().getAsignacionAdultoMayor())*12)/1000);
						} else if(TipoComuna.URBANA.getId() == antecendenteComunaCalculado.getAntecedentesComuna().getClasificacion().getIdTipoComuna()){
							perCapitaAno = (int)  ((valorPerCapitaComunalMes * antecendenteComunaCalculado.getPoblacion() + antecendenteComunaCalculado.getPoblacionMayor() * antecendenteComunaCalculado.getAntecedentesComuna().getAnoAnoEnCurso().getAsignacionAdultoMayor())*12)/1000;
						} else if(TipoComuna.COSTOFIJO.getId() == antecendenteComunaCalculado.getAntecedentesComuna().getClasificacion().getIdTipoComuna()){
							if(antecendenteComunaCalculado.getAntecedentesComuna() != null && antecendenteComunaCalculado.getAntecedentesComuna().getAnoAnoEnCurso() != null){
								perCapitaAno = (int) (perCapitaCostoFijo * antecendenteComunaCalculado.getAntecedentesComuna().getAnoAnoEnCurso().getInflactor());
							}else{
								System.out.println("antecendenteComunaCalculado.getAntecedentesComuna().getIdAntecedentesComuna()-->"+antecendenteComunaCalculado.getAntecedentesComuna().getIdAntecedentesComuna());
								System.out.println("antecendenteComunaCalculado.getAntecedentesComuna().getAnoAnoEnCurso()-->"+antecendenteComunaCalculado.getAntecedentesComuna().getAnoAnoEnCurso());
							}
						}
						if(perCapitaAno != null){
							antecendenteComunaCalculado.setPercapitaAno(perCapitaAno);
							Integer perCapitaMes = (perCapitaAno *1000)/12;
							antecendenteComunaCalculado.setPercapitaMes(perCapitaMes);
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
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderPercapita.replace("{ANO}", getAnoCurso().toString()));
			System.out.println("response AsignacionRecursosPercapitaSheetExcel --->"+response);
			DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findById(idDistribucionInicialPercapita);
			planillaTrabajoId = documentService.createDocumentPercapita(distribucionInicialPercapita, TipoDocumentosProcesos.PLANILLARESULTADOSCALCULADOS, response.getNodeRef(), response.getFileName(), contenType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return planillaTrabajoId;
	}

	public List<AsignacionDistribucionPerCapitaVO> findAntecedentesComunaCalculadosByDistribucionInicialPercapita(Integer idDistribucionInicialPercapita){
		List<AntecendentesComunaCalculado>  antecendentesComunaCalculado = antecedentesComunaDAO.findAntecedentesComunaCalculadosByDistribucionInicialPercapita(idDistribucionInicialPercapita);
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
			antecendentesComunaCalculado = antecedentesComunaDAO.findAntecendentesComunaCalculadoByComunaServicioDistribucionInicialPercapita(servicio, comuna, idDistribucionInicialPercapita);
		} else {
			antecendentesComunaCalculado = antecedentesComunaDAO.findAntecendentesComunaCalculadoByServicioDistribucionInicialPercapita(servicio, idDistribucionInicialPercapita);
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
			List<VariacionPoblacionVO> variaciones = new ArrayList<VariacionPoblacionVO>();
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contenType = mimemap.getContentType(filename.toLowerCase());
			System.out.println("template->"+template);
			System.out.println("filename->"+filename);
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("ano_siguiente", (getAnoCurso() + 1));
			parameters.put("poblacion_beneficiaria", new Integer(100000));
			parameters.put("porcentaje", new Double(0.96));
			parameters.put("ano_curso", getAnoCurso());


			if(template.endsWith(".doc")){
				System.out.println("Genera archivo doc");
				generadorWord.saveContent(documentoVO.getContent(), HWPFDocument.class);
				GeneradorOficioConsulta generadorOficioConsulta = new GeneradorOficioConsulta(filename,template);
				generadorOficioConsulta.replaceValues(null, null, HWPFDocument.class);
				BodyVO response = alfrescoService.uploadDocument(new File(filename), contenType, folderPercapita.replace("{ANO}", getAnoCurso().toString()));
				System.out.println("response AsignacionRecursosPercapitaSheetExcel --->"+response);
				plantillaId = documentService.createDocumentPercapita(idDistribucionInicialPercapita, TipoDocumentosProcesos.OFICIOCONSULTA, response.getNodeRef(), response.getFileName(), contenType);
			}else if(template.endsWith(".docx")){
				System.out.println("Genera archivo docx");
				generadorWord.saveContent(documentoVO.getContent(), XWPFDocument.class);
				GeneradorOficioConsulta generadorOficioConsulta = new GeneradorOficioConsulta(filename, template);
				generadorOficioConsulta.replaceValues(parameters, variaciones, XWPFDocument.class);
				BodyVO response = alfrescoService.uploadDocument(new File(filename), contenType, folderPercapita.replace("{ANO}", getAnoCurso().toString()));
				System.out.println("response AsignacionRecursosPercapita --->"+response);
				plantillaId = documentService.createDocumentPercapita(idDistribucionInicialPercapita, TipoDocumentosProcesos.OFICIOCONSULTA, response.getNodeRef(), response.getFileName(), contenType);
			}
		}catch(IOException e){
			e.printStackTrace();
		} catch (InvalidFormatException e) {
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
				BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummaryVO.getPath()), contenType, folderPercapita.replace("{ANO}", getAnoCurso().toString()));
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
					AntecendentesComunaCalculado antecendentesComunaCalculado = null;
					if(antecendenteComuna.getAntecendentesComunaCalculadoCollection() != null && antecendenteComuna.getAntecendentesComunaCalculadoCollection().size() > 0){
						antecendentesComunaCalculado = antecendenteComuna.getAntecendentesComunaCalculadoCollection().iterator().next();
					}
					if(antecendenteComuna.getClasificacion() != null){
						if(TipoComuna.COSTOFIJO.getId().equals(antecendenteComuna.getClasificacion().getIdTipoComuna())){
							Map<String, Object> parametersAporteEstatalCF = new HashMap<String, Object>();
							parametersAporteEstatalCF.put("{comuna_seleccionada}", antecendenteComuna.getIdComuna().getNombre());
							parametersAporteEstatalCF.put("{aporte_mensual}",((antecendentesComunaCalculado.getPercapitaMes() == null)? new Integer(0) : antecendentesComunaCalculado.getPercapitaMes()));
							parametersAporteEstatalCF.put("{ano_curso}",getAnoCurso());

							generadorWordAporteEstatalCF.saveContent(documentoAporteEstatalCFVO.getContent(), XWPFDocument.class);
							String filenameAporteEstatalCFTmp = filenameAporteEstatalCF.replace(".docx", "-" + antecendenteComuna.getIdComuna().getId() + ".docx");
							GeneradorResolucionAporteEstatal generadorResolucionAporteEstatalCF = new GeneradorResolucionAporteEstatal(filenameAporteEstatalCFTmp, templateAporteEstatalCF);
							generadorResolucionAporteEstatalCF.replaceValues(parametersAporteEstatalCF, XWPFDocument.class);
							BodyVO responseAporteEstatalCF = alfrescoService.uploadDocument(new File(filenameAporteEstatalCFTmp), contenTypeAporteEstatalCF, folderPercapita.replace("{ANO}", getAnoCurso().toString()));
							System.out.println("response responseAporteEstatalCF --->"+responseAporteEstatalCF);
							plantillaIdResolucionAporteEstatalCF = documentService.createDocumentPercapita(idDistribucionInicialPercapita, antecendenteComuna.getIdComuna().getId(), TipoDocumentosProcesos.RESOLUCIONAPORTEESTATALCF, responseAporteEstatalCF.getNodeRef(), responseAporteEstatalCF.getFileName(), contenTypeAporteEstatalCF);
						}else{
							Map<String, Object> parametersAporteEstatalUR = new HashMap<String, Object>();
							parametersAporteEstatalUR.put("{ano_curso}",getAnoCurso());
							parametersAporteEstatalUR.put("{comuna_seleccionada}", antecendenteComuna.getIdComuna().getNombre());
							Integer poblacion = ((antecendentesComunaCalculado.getPoblacion() == null) ? new Integer(0): antecendentesComunaCalculado.getPoblacion()) + ((antecendentesComunaCalculado.getPoblacionMayor() != null)?new Integer(0): antecendentesComunaCalculado.getPoblacionMayor());
							parametersAporteEstatalUR.put("{poblacion_seleccionada}",poblacion);
							parametersAporteEstatalUR.put("{monto_seleccionado}",((antecendentesComunaCalculado.getValorPerCapitaComunalMes() == null)?new Double(0): antecendentesComunaCalculado.getValorPerCapitaComunalMes()));
							parametersAporteEstatalUR.put("{aporte_mensual}",((antecendentesComunaCalculado.getPercapitaMes() == null)? new Integer(0): antecendentesComunaCalculado.getPercapitaMes()));
							generadorWordAporteEstatalUR.saveContent(documentoAporteEstatalURVO.getContent(), XWPFDocument.class);
							String filenameAporteEstatalURTmp = filenameAporteEstatalUR.replace(".docx", "-" + antecendenteComuna.getIdComuna().getId() + ".docx");
							GeneradorResolucionAporteEstatal generadorResolucionAporteEstatalUR = new GeneradorResolucionAporteEstatal(filenameAporteEstatalURTmp, templateAporteEstatalUR);
							generadorResolucionAporteEstatalUR.replaceValues(parametersAporteEstatalUR, XWPFDocument.class);
							BodyVO responseAporteEstatalUR = alfrescoService.uploadDocument(new File(filenameAporteEstatalURTmp), contenTypeAporteEstatalUR, folderPercapita.replace("{ANO}", getAnoCurso().toString()));
							System.out.println("response responseAporteEstatalUR --->"+responseAporteEstatalUR);
							plantillaIdResolucionAporteEstatalUR = documentService.createDocumentPercapita(idDistribucionInicialPercapita, antecendenteComuna.getIdComuna().getId(), TipoDocumentosProcesos.RESOLUCIONAPORTEESTATALCF, responseAporteEstatalUR.getNodeRef(), responseAporteEstatalUR.getFileName(), contenTypeAporteEstatalUR);
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
			GeneradorWordBorradorAporteEstatal generadorWordDecretoBorradorAporteEstatal = new GeneradorWordBorradorAporteEstatal(filenameBorradorAporteEstatal, templateBorradorAporteEstatal);
			generadorWordDecretoBorradorAporteEstatal.replaceValues(parametersBorradorAporteEstatal, null, XWPFDocument.class);
			BodyVO responseBorradorAporteEstatal = alfrescoService.uploadDocument(new File(filenameBorradorAporteEstatal), contenTypeBorradorAporteEstatal, folderPercapita.replace("{ANO}", getAnoCurso().toString()));
			System.out.println("response responseBorradorAporteEstatal --->"+responseBorradorAporteEstatal);
			plantillaIdBorradorDecretoAporteEstatal = documentService.createDocumentPercapita(idDistribucionInicialPercapita, TipoDocumentosProcesos.BORRADORAPORTEESTATAL, responseBorradorAporteEstatal.getNodeRef(), responseBorradorAporteEstatal.getFileName(), contenTypeBorradorAporteEstatal);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		return plantillaIdBorradorDecretoAporteEstatal;
	}

	public void notificarFinElaboracionResolucion(Integer idDistribucionInicialPercapita, String username) {
		try {
			Usuario usuario = usuarioDAO.getUserByUsername(username);
			List<EmailService.Adjunto> adjuntos = new ArrayList<EmailService.Adjunto>();
			/*EmailService.Adjunto adjunto = new EmailService.Adjunto();
			adjunto.setDescripcion("Borrador Decreto Aporte Estatal");
			adjunto.setName(documentDecretoAporteEstatalVO.getName());
			adjunto.setUrl((new File(fileNameDecretoAporteEstatal)).toURI().toURL());
			adjuntos.add(adjunto);*/
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
			BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummary.getPath()), contenType, folderPercapita.replace("{ANO}", getAnoCurso().toString()));
			System.out.println("response upload template --->"+response);
			documentService.updateDocumentTemplate(referenciaDocumentoSummary.getId(), response.getNodeRef(), response.getFileName(), contenType);
			DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findById(idDistribucionInicialPercapita);
			documentService.createDocumentPercapita(distribucionInicialPercapita, tipoDocumento, referenciaDocumentoId, lastVersion);
		}
	}

	public ReferenciaDocumentoSummaryVO getLastDocumentoSummaryByDistribucionInicialPercapitaType(Integer idDistribucionInicialPercapita,TipoDocumentosProcesos tipoDocumento){
		return documentService.getLastDocumentoSummaryByDistribucionInicialPercapitaType(idDistribucionInicialPercapita, tipoDocumento);
	}

}
