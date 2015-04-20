package minsal.divap.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import minsal.divap.dao.DocumentDAO;
import minsal.divap.dao.InstitucionDAO;
import minsal.divap.dao.SeguimientoDAO;
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.doc.GeneradorDocumento;
import minsal.divap.doc.GeneradorOficioConsulta;
import minsal.divap.doc.GeneradorResolucionAporteEstatal;
import minsal.divap.doc.GeneradorWord;
import minsal.divap.doc.GeneradorWordBorradorAporteEstatal;
import minsal.divap.enums.FieldType;
import minsal.divap.enums.Instituciones;
import minsal.divap.enums.TareasSeguimiento;
import minsal.divap.enums.TipoComuna;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.enums.TiposDestinatarios;
import minsal.divap.excel.GeneradorExcel;
import minsal.divap.excel.impl.AsignacionDistribucionPercapitaSheetExcel;
import minsal.divap.excel.impl.AsignacionRecursosPercapitaSheetExcel;
import minsal.divap.excel.impl.PercapitaCalculoPercapitaExcelValidator;
import minsal.divap.excel.impl.PercapitaDesempenoDificilExcelValidator;
import minsal.divap.exception.ExcelFormatException;
import minsal.divap.model.mappers.AsignacionDistribucionPercapitaMapper;
import minsal.divap.model.mappers.ReferenciaDocumentoMapper;
import minsal.divap.util.StringUtil;
import minsal.divap.vo.AsignacionDistribucionPerCapitaVO;
import minsal.divap.vo.BaseVO;
import minsal.divap.vo.BodyVO;
import minsal.divap.vo.CalculoPercapitaVO;
import minsal.divap.vo.CellTypeExcelVO;
import minsal.divap.vo.DesempenoDificilVO;
import minsal.divap.vo.DocumentoVO;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.RegionVO;
import minsal.divap.vo.SeguimientoVO;
import minsal.divap.vo.ServiciosVO;
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
import cl.minsal.divap.model.DocumentoModificacionPercapita;
import cl.minsal.divap.model.Institucion;
import cl.minsal.divap.model.ModificacionDistribucionInicialPercapita;
import cl.minsal.divap.model.ReferenciaDocumento;
import cl.minsal.divap.model.ReporteEmailsAdjuntos;
import cl.minsal.divap.model.ReporteEmailsDestinatarios;
import cl.minsal.divap.model.ReporteEmailsEnviados;
import cl.minsal.divap.model.ReporteEmailsModificacionPercapita;
import cl.minsal.divap.model.Seguimiento;
import cl.minsal.divap.model.TipoDestinatario;
import cl.minsal.divap.model.Usuario;

@Stateless
@LocalBean
public class ModificacionDistribucionInicialPercapitaService {
	@EJB
	private DistribucionInicialPercapitaDAO distribucionInicialPercapitaDAO;
	@EJB
	private UsuarioDAO usuarioDAO;
	@EJB
	private AntecedentesComunaDAO antecedentesComunaDAO;
	@EJB
	private InstitucionDAO institucionDAO;
	@EJB
	private SeguimientoDAO seguimientoDAO;
	@EJB
	private AnoDAO anoDAO;
	@EJB
	private DocumentDAO documentDAO;
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

	public Integer crearIntanciaModificacionDistribucionInicialPercapita(String username, Integer ano){
		System.out.println("username-->"+username);
		Usuario usuario = this.usuarioDAO.getUserByUsername(username);
		AnoEnCurso anoEnCurso = anoDAO.getAnoById(ano);
		return distribucionInicialPercapitaDAO.crearIntanciaModificacionDistribucionInicialPercapita(usuario, anoEnCurso);
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

	public void procesarCalculoPercapita(Integer idModificacionDistribucionInicialPercapita, XSSFWorkbook workbook, Integer ano) throws ExcelFormatException {
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
		
		DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(ano);
		ModificacionDistribucionInicialPercapita modificacionPercapita = distribucionInicialPercapitaDAO.findModificacionDistribucionInicialById(idModificacionDistribucionInicialPercapita);

		PercapitaCalculoPercapitaExcelValidator calculoPercapitaExcelValidator = new PercapitaCalculoPercapitaExcelValidator(cells.size(), cells, true, 0, 0);
		calculoPercapitaExcelValidator.validateFormat(worksheet);		
		List<CalculoPercapitaVO> items = calculoPercapitaExcelValidator.getItems();
		for(CalculoPercapitaVO calculoPercapitaVO : items){
			AntecendentesComuna antecendentesComuna = antecedentesComunaDAO.findAntecendentesComunaByComunaServicioAno(calculoPercapitaVO.getServicio(), calculoPercapitaVO.getComuna(), ano);
			System.out.println("antecendentesComuna-->"+antecendentesComuna);
			if(antecendentesComuna != null){				
				AntecendentesComunaCalculado antecendentesComunaCalculadoVigente = antecedentesComunaDAO.findByAntecedentesDistrinbucionInicialVigente (antecendentesComuna.getIdAntecedentesComuna(), distribucionInicialPercapita.getIdDistribucionInicialPercapita());
				if(antecendentesComunaCalculadoVigente == null){
					System.out.println("No tiene AntecendentesComunaCalculado se debe crear el registro");
					AntecendentesComunaCalculado antecendentesComunaCalculado = new AntecendentesComunaCalculado();
					antecendentesComunaCalculado.setAntecedentesComuna(antecendentesComuna);
					antecendentesComunaCalculado.setPoblacion(calculoPercapitaVO.getPoblacion().intValue());
					antecendentesComunaCalculado.setPoblacionMayor(calculoPercapitaVO.getPoblacionMayor().intValue());
					antecendentesComunaCalculado.setDistribucionInicialPercapita(distribucionInicialPercapita);
					antecendentesComunaCalculado.setModificacionPercapita(modificacionPercapita);
					antecedentesComunaDAO.createAntecendentesComunaCalcuado(antecendentesComunaCalculado);
				} else {
					antecendentesComunaCalculadoVigente.setFechaVigencia(new Date());
					AntecendentesComunaCalculado antecendentesComunaCalculado = new AntecendentesComunaCalculado();
					antecendentesComunaCalculado.setAntecedentesComuna(antecendentesComuna);
					antecendentesComunaCalculado.setPoblacion(calculoPercapitaVO.getPoblacion().intValue());
					antecendentesComunaCalculado.setPoblacionMayor(calculoPercapitaVO.getPoblacionMayor().intValue());
					antecendentesComunaCalculado.setDistribucionInicialPercapita(distribucionInicialPercapita);
					antecendentesComunaCalculado.setModificacionPercapita(modificacionPercapita);
					antecendentesComunaCalculado.setAprobado(antecendentesComunaCalculadoVigente.getAprobado());
					antecendentesComunaCalculado.setDesempenoDificil(antecendentesComunaCalculadoVigente.getDesempenoDificil());
					antecedentesComunaDAO.createAntecendentesComunaCalcuado(antecendentesComunaCalculado);
				}
			}
		}
	}
	
	public void procesarValorBasicoDesempeno(Integer idModificacionDistribucionInicialPercapita, XSSFWorkbook workbook, Integer ano)  throws ExcelFormatException {
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
		
		System.out.println("procesarValorBasicoDesempeno->idModificacionDistribucionInicialPercapita="+idModificacionDistribucionInicialPercapita);
		System.out.println("procesarValorBasicoDesempeno->ano="+ano);
		
		DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(ano);
		ModificacionDistribucionInicialPercapita modificacionPercapita = distribucionInicialPercapitaDAO.findModificacionDistribucionInicialById(idModificacionDistribucionInicialPercapita);
		
		PercapitaDesempenoDificilExcelValidator desempenoDificilExcelValidator = new PercapitaDesempenoDificilExcelValidator(cells.size(), cells, true, 0, 0);
		desempenoDificilExcelValidator.validateFormat(worksheet);		
		List<DesempenoDificilVO> items = desempenoDificilExcelValidator.getItems();
		
		for(DesempenoDificilVO desempenoDificilVO : items){
			AntecendentesComuna antecendentesComuna = antecedentesComunaDAO.findAntecendentesComunaByComunaServicioAno(desempenoDificilVO.getServicio(), desempenoDificilVO.getComuna(), ano);
			if(antecendentesComuna != null){
				AntecendentesComunaCalculado antecendentesComunaCalculadoVigente = antecedentesComunaDAO.findByAntecedentesDistrinbucionInicialVigente (antecendentesComuna.getIdAntecedentesComuna(), distribucionInicialPercapita.getIdDistribucionInicialPercapita());
				if( antecendentesComunaCalculadoVigente == null){
					antecendentesComunaCalculadoVigente = new AntecendentesComunaCalculado();
					antecendentesComunaCalculadoVigente.setAntecedentesComuna(antecendentesComuna);
					antecendentesComunaCalculadoVigente.setDesempenoDificil(desempenoDificilVO.getValorDesempenoDificil().intValue());
					antecendentesComunaCalculadoVigente.setDistribucionInicialPercapita(distribucionInicialPercapita);
					antecendentesComunaCalculadoVigente.setModificacionPercapita(modificacionPercapita);
					antecedentesComunaDAO.createAntecendentesComunaCalcuado(antecendentesComunaCalculadoVigente);
				} else {
					antecendentesComunaCalculadoVigente.setDesempenoDificil(desempenoDificilVO.getValorDesempenoDificil().intValue());
				}
			}
		}
	}

	public Integer getAnoCursodd() {
		DateFormat formatNowYear = new SimpleDateFormat("yyyy");
		Date nowDate = new Date();
		return Integer.valueOf(formatNowYear.format(nowDate)); 
	}

	public Integer valorizarDisponibilizarPlanillaTrabajo(Integer idModificacionDistribucionInicialPercapita, Integer ano) {
		System.out.println("valorizarDisponibilizarPlanillaTrabajo-->"+idModificacionDistribucionInicialPercapita);
		DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(ano);
		List<AntecendentesComunaCalculado>  antecendentesComunaCalculado = antecedentesComunaDAO.findAntecedentesComunaCalculadosByDistribucionInicialPercapitaVigenteModificacion(
				distribucionInicialPercapita.getIdDistribucionInicialPercapita(), idModificacionDistribucionInicialPercapita);
		if(antecendentesComunaCalculado != null && antecendentesComunaCalculado.size() > 0){
			for(AntecendentesComunaCalculado antecendenteComunaCalculado: antecendentesComunaCalculado){
				Integer percapitaBasal = antecendenteComunaCalculado.getAntecedentesComuna().getAnoAnoEnCurso().getMontoPercapitalBasal();
				Integer asignacionAdultoMayor = antecendenteComunaCalculado.getAntecedentesComuna().getAnoAnoEnCurso().getAsignacionAdultoMayor();
				System.out.println("percapitaBasal="+percapitaBasal+ " antecendenteComunaCalculado.getAntecedentesComuna().getTramoPobreza().getValor()=" + ((antecendenteComunaCalculado.getAntecedentesComuna().getTramoPobreza() == null) ? 0 : antecendenteComunaCalculado.getAntecedentesComuna().getTramoPobreza().getValor()));
				Double pobreza = percapitaBasal * ((antecendenteComunaCalculado.getAntecedentesComuna().getTramoPobreza() == null) ? 0 : antecendenteComunaCalculado.getAntecedentesComuna().getTramoPobreza().getValor());
				antecendenteComunaCalculado.setPobreza(pobreza);
				System.out.println("antecendenteComunaCalculado.getAntecedentesComuna().getIdAntecedentesComuna()="+antecendenteComunaCalculado.getAntecedentesComuna().getIdAntecedentesComuna());
				if(antecendenteComunaCalculado.getAntecedentesComuna().getClasificacion() != null){
					if(TipoComuna.RURAL.getId().equals(antecendenteComunaCalculado.getAntecedentesComuna().getClasificacion().getIdTipoComuna())){
						Double ruralidad = (percapitaBasal + pobreza) * 0.2;
						antecendenteComunaCalculado.setRuralidad(ruralidad);
						Double valorReferencialZona = (percapitaBasal + pobreza + ruralidad) * ((antecendenteComunaCalculado.getAntecedentesComuna().getAsignacionZona() == null) ? 0 : antecendenteComunaCalculado.getAntecedentesComuna().getAsignacionZona().getValor());
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
						System.out.println("perCapitaMes= "+perCapitaMes);
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
						Double perCapitaCostoFijo = antecedentesComunaDAO.findPerCapitaCostoFijoByServicioComunaAnoAnterior(antecendenteComunaCalculado.getAntecedentesComuna().getIdComuna().getId(), antecendenteComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId(), ano);
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
		return publicarPlanillaTrabajo(idModificacionDistribucionInicialPercapita, ano);
	}

	private Integer publicarPlanillaTrabajo(Integer idModificacionDistribucionInicialPercapita, Integer ano){
		Integer planillaTrabajoId = null;
		DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(ano);
		List<AsignacionDistribucionPerCapitaVO> antecedentesCalculados = findAntecedentesComunaCalculadosByDistribucionInicialPercapita(distribucionInicialPercapita.getIdDistribucionInicialPercapita(), idModificacionDistribucionInicialPercapita);
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator + "planillaAsignacionDistribucionPercapita.xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());
		GeneradorExcel generadorExcel = new GeneradorExcel(filename);
		List<String> headers = new ArrayList<String>();
		headers.add("REGION");
		headers.add("SERVICIO");
		headers.add("COMUNA");
		headers.add("CLASIFICACION " + ano);
		headers.add("Ref. Asig_Zon " + ano);
		headers.add("Tramo Pobreza");
		headers.add("Per Capita Basal " + ano);
		headers.add("Pobreza " + ano);
		headers.add("Ruralidad " + ano);
		headers.add("Valor Ref. Asig_Zon " + ano);
		headers.add("Valor Per Capita " + ano + "($/mes " + ano + ")");
		headers.add("POBLACION AÑO" + ano);
		headers.add("POBLACION MAYOR DE 65 AÑOS" + ano);
		headers.add("PER CAPITA AÑO " + ano + "($ " + ano + ")");
		headers.add("PER CAPITA MES " + ano + "($ " + ano + ")");
		AsignacionDistribucionPercapitaSheetExcel asignacionDistribucionPercapitaSheetExcel = new AsignacionDistribucionPercapitaSheetExcel(headers, antecedentesCalculados);
		generadorExcel.addSheet( asignacionDistribucionPercapitaSheetExcel, "Hoja 1");
		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderPercapita.replace("{ANO}", ano.toString()));
			System.out.println("response AsignacionRecursosPercapitaSheetExcel --->"+response);
			ModificacionDistribucionInicialPercapita modificacionPercapita = distribucionInicialPercapitaDAO.findModificacionDistribucionInicialById(idModificacionDistribucionInicialPercapita);
			planillaTrabajoId = documentService.createDocumentModificacionPercapita(modificacionPercapita, TipoDocumentosProcesos.PLANILLARESULTADOSCALCULADOS, response.getNodeRef(), response.getFileName(), contenType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return planillaTrabajoId;
	}

	public List<AsignacionDistribucionPerCapitaVO> findAntecedentesComunaCalculadosByDistribucionInicialPercapita(Integer idModificacionDistribucionInicialPercapita, Integer ano){
		DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(ano);
		List<AntecendentesComunaCalculado>  antecendentesComunaCalculado = antecedentesComunaDAO.findAntecedentesComunaCalculadosByDistribucionInicialPercapitaVigente(distribucionInicialPercapita.getIdDistribucionInicialPercapita());
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

	private List<AsignacionDistribucionPerCapitaVO> findAntecedentesComunaCalculadosByDistribucionInicialPercapita(Integer idDistribucionInicialPercapita, Integer idModificacionDistribucionInicialPercapita, Integer ano) {
		DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(ano);
		List<AntecendentesComunaCalculado>  antecendentesComunaCalculado = antecedentesComunaDAO.findAntecedentesComunaCalculadosByDistribucionInicialPercapitaVigenteModificacion(distribucionInicialPercapita.getIdDistribucionInicialPercapita(),
				idModificacionDistribucionInicialPercapita);
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

	public List<AsignacionDistribucionPerCapitaVO> findAntecedentesServicioCalculadosByDistribucionInicialPercapita(Integer idServicio, Integer idModificacionDistribucionInicialPercapita, Integer ano){
		DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(ano);
		List<AntecendentesComunaCalculado>  antecendentesComunaCalculado = antecedentesComunaDAO.findAntecedentesServicioCalculadosByDistribucionInicialPercapitaVigente(idServicio, distribucionInicialPercapita.getIdDistribucionInicialPercapita());
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

	public List<AsignacionDistribucionPerCapitaVO> findAntecedentesComunaCalculadosByDistribucionInicialPercapita(Integer servicio, Integer comuna, Integer idModificacionDistribucionInicialPercapita, Integer ano) {
		List<AntecendentesComunaCalculado>  antecendentesComunaCalculado = null;
		DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(ano);
		if(comuna != null){
			antecendentesComunaCalculado = antecedentesComunaDAO.findAntecendentesComunaCalculadoByComunaServicioDistribucionInicialPercapitaVigente(servicio, comuna, distribucionInicialPercapita.getIdDistribucionInicialPercapita());
		} else {
			antecendentesComunaCalculado = antecedentesComunaDAO.findAntecendentesComunaCalculadoByServicioDistribucionInicialPercapitaVigente(servicio, distribucionInicialPercapita.getIdDistribucionInicialPercapita());
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

	public Integer crearOficioConsulta(Integer idModificacionDistribucionInicialPercapita, Integer ano) {
		System.out.println("DistribucionInicialPercapitaService --- > crearOficioConsulta");
		Integer plantillaId = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLAOFICIOCONSULTA);
		if(plantillaId == null){
			throw new RuntimeException("No se puede crear Oficio Consulta, la plantilla no esta cargada");
		}
		DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(ano);
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
			parameters.put("{ano_siguiente}", (ano + 1));
			parameters.put("{poblacion_beneficiaria}", new Integer(100000));
			parameters.put("{ano_curso}", ano);
			if(template.endsWith(".doc")){
				System.out.println("Genera archivo doc");
				generadorWord.saveContent(documentoVO.getContent(), HWPFDocument.class);
				GeneradorOficioConsulta generadorOficioConsulta = new GeneradorOficioConsulta(filename,template);
				generadorOficioConsulta.replaceValues(null, null, HWPFDocument.class);
				BodyVO response = alfrescoService.uploadDocument(new File(filename), contenType, folderPercapita.replace("{ANO}", ano.toString()));
				System.out.println("response AsignacionRecursosPercapitaSheetExcel --->"+response);
				plantillaId = documentService.createDocumentPercapita(distribucionInicialPercapita.getIdDistribucionInicialPercapita(), TipoDocumentosProcesos.OFICIOCONSULTA, response.getNodeRef(), response.getFileName(), contenType);
			}else if(template.endsWith(".docx")){
				System.out.println("Genera archivo docx");
				generadorWord.saveContent(documentoVO.getContent(), XWPFDocument.class);
				GeneradorResolucionAporteEstatal generadorOficioConsulta = new GeneradorResolucionAporteEstatal(filename, template);
				//generadorOficioConsulta.replaceValues(parameters, variaciones, XWPFDocument.class);
				generadorOficioConsulta.replaceValues(parameters, XWPFDocument.class);
				BodyVO response = alfrescoService.uploadDocument(new File(filename), contenType, folderPercapita.replace("{ANO}", ano.toString()));
				System.out.println("response AsignacionRecursosPercapita --->"+response);
				ModificacionDistribucionInicialPercapita modificacionPercapita = distribucionInicialPercapitaDAO.findModificacionDistribucionInicialById(idModificacionDistribucionInicialPercapita);
				plantillaId = documentService.createDocumentModificacionPercapita(modificacionPercapita, TipoDocumentosProcesos.OFICIOCONSULTA, response.getNodeRef(), response.getFileName(), contenType);
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

	public List<SeguimientoVO> getBitacoraModificacionPercapita(Integer idModificacionDistribucionInicialPercapita,	TareasSeguimiento tareaSeguimiento) {
		return seguimientoService.getBitacoraModificacionPercapita(idModificacionDistribucionInicialPercapita, tareaSeguimiento);
	}

	public void elaborarDocumentoFormal(Integer idModificacionDistribucionInicialPercapita, Integer ano) {
		System.out.println("ModificacionDistribucionInicialPercapitaService  ResolucionAporteEstatal idModificacionDistribucionInicialPercapita-->"+idModificacionDistribucionInicialPercapita);
		Integer plantillaIdResolucionAporteEstatalUR = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLAMODIFICACIONRESOLUCIONAPORTEESTATAL);
		if(plantillaIdResolucionAporteEstatalUR == null){
			throw new RuntimeException("No se puede crear Resolucion Aporte Estatal UR, la plantilla no esta cargada");
		}
		try{

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
			String contenTypeAporteEstatalUR = mimemap.getContentType(filenameAporteEstatalUR.toLowerCase());
			System.out.println("templateAporteEstatalUR->"+templateAporteEstatalUR);
			DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(ano);
			System.out.println("distribucionInicialPercapita="+distribucionInicialPercapita.getIdDistribucionInicialPercapita());
			List<AntecendentesComuna> antecendentesComuna = antecedentesComunaDAO.findAntecendentesComunaByidDistribucionInicialPercapitaModificacionPercapita(
					distribucionInicialPercapita.getIdDistribucionInicialPercapita(), idModificacionDistribucionInicialPercapita);
			System.out.println("antecendentesComuna.size()="+((antecendentesComuna == null)?0:antecendentesComuna.size()));
			if(antecendentesComuna != null && antecendentesComuna.size() > 0){
				for(AntecendentesComuna antecendenteComuna : antecendentesComuna){
					AntecendentesComunaCalculado antecendentesComunaCalculadoVigente = antecedentesComunaDAO.findByAntecedentesDistrinbucionInicialVigenteModificacionPercapita(antecendenteComuna.getIdAntecedentesComuna(), distribucionInicialPercapita.getIdDistribucionInicialPercapita(), idModificacionDistribucionInicialPercapita);
					if(antecendenteComuna.getClasificacion() != null){
						Map<String, Object> parametersAporteEstatalUR = new HashMap<String, Object>();
						parametersAporteEstatalUR.put("{ano_curso}", ano);
						parametersAporteEstatalUR.put("{comuna_seleccionada}", antecendenteComuna.getIdComuna().getNombre());
						Integer poblacion = ((antecendentesComunaCalculadoVigente.getPoblacion() == null) ? 0 : antecendentesComunaCalculadoVigente.getPoblacion()) + ((antecendentesComunaCalculadoVigente.getPoblacionMayor() != null)?new Integer(0): antecendentesComunaCalculadoVigente.getPoblacionMayor());
						parametersAporteEstatalUR.put("{poblacion_seleccionada}", StringUtil.formatNumber(poblacion));
						Double percapitaComunaMes = ((antecendentesComunaCalculadoVigente.getValorPerCapitaComunalMes() == null)? 0.0 : antecendentesComunaCalculadoVigente.getValorPerCapitaComunalMes());
						parametersAporteEstatalUR.put("{monto_seleccionado}", StringUtil.formatNumber(Math.round(percapitaComunaMes)));
						Long percapitaMes = ((antecendentesComunaCalculadoVigente.getPercapitaMes() == null) ? 0 : antecendentesComunaCalculadoVigente.getPercapitaMes());
						parametersAporteEstatalUR.put("{aporte_mensual}", StringUtil.formatNumber(percapitaMes));
						parametersAporteEstatalUR.put("{diferencia_aporte_mensual}", StringUtil.formatNumber(0));

						generadorWordAporteEstatalUR.saveContent(documentoAporteEstatalURVO.getContent(), XWPFDocument.class);
						String filenameAporteEstatalURTmp = filenameAporteEstatalUR.replace(".docx", "-" + antecendenteComuna.getIdComuna().getNombre() + ".docx");
						filenameAporteEstatalURTmp = StringUtil.removeSpanishAccents(filenameAporteEstatalURTmp);
						GeneradorResolucionAporteEstatal generadorResolucionAporteEstatalUR = new GeneradorResolucionAporteEstatal(filenameAporteEstatalURTmp, templateAporteEstatalUR);
						generadorResolucionAporteEstatalUR.replaceValues(parametersAporteEstatalUR, XWPFDocument.class);
						BodyVO responseAporteEstatalUR = alfrescoService.uploadDocument(new File(filenameAporteEstatalURTmp), contenTypeAporteEstatalUR, folderPercapita.replace("{ANO}", ano.toString()));
						System.out.println("response responseAporteEstatalUR --->"+responseAporteEstatalUR);
						ModificacionDistribucionInicialPercapita modificacionPercapita = distribucionInicialPercapitaDAO.findModificacionDistribucionInicialById(idModificacionDistribucionInicialPercapita);
						plantillaIdResolucionAporteEstatalUR = documentService.createDocumentModificacionPercapita(modificacionPercapita, antecendenteComuna.getIdComuna().getId(), TipoDocumentosProcesos.ORDINARIOMODIFICACIONRESOLUCIONAPORTEESTATAL, responseAporteEstatalUR.getNodeRef(), responseAporteEstatalUR.getFileName(), contenTypeAporteEstatalUR);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void enviarDecretoAporteEstatal(Integer borradorAporteEstatalId, String username, Integer ano) {
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

	public Integer elaborarBorradorDecretoAporteEstatal(Integer idModificacionDistribucionInicialPercapita, Integer ano) {
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
			BodyVO responseBorradorAporteEstatal = alfrescoService.uploadDocument(new File(filenameBorradorAporteEstatal), contenTypeBorradorAporteEstatal, folderPercapita.replace("{ANO}", ano.toString()));
			System.out.println("response responseBorradorAporteEstatal --->"+responseBorradorAporteEstatal);
			ModificacionDistribucionInicialPercapita modificacionPercapita = distribucionInicialPercapitaDAO.findModificacionDistribucionInicialById(idModificacionDistribucionInicialPercapita);
			plantillaIdBorradorDecretoAporteEstatal = documentService.createDocumentModificacionPercapita(modificacionPercapita, TipoDocumentosProcesos.BORRADORAPORTEESTATAL, responseBorradorAporteEstatal.getNodeRef(), responseBorradorAporteEstatal.getFileName(), contenTypeBorradorAporteEstatal);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		return plantillaIdBorradorDecretoAporteEstatal;
	}

	public void notificarFinElaboracionResolucion(Integer idModificacionDistribucionInicialPercapita, String username, Integer ano) {
		try {
			Usuario usuario = usuarioDAO.getUserByUsername(username);
			List<EmailService.Adjunto> adjuntos = new ArrayList<EmailService.Adjunto>();
			emailService.sendMail(usuario.getEmail().getValor(), "Documentos Formales(Resoluciones Comunales de Aporte Estatal)", "Estimado " + username + ": <br /> <p> Se completo exitosamente la generación Documentos Formales(Resoluciones Comunales de Aporte Estatal)</p>", adjuntos);
			System.out.println("notificarFinElaboracionResolucion se ejecuto correctamente");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void moveToAlfresco(Integer idModificacionDistribucionInicialPercapita, Integer referenciaDocumentoId, TipoDocumentosProcesos tipoDocumento, Boolean lastVersion, Integer ano) {
		System.out.println("Buscando referenciaDocumentoId="+referenciaDocumentoId);
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummary = documentService.getDocumentSummary(referenciaDocumentoId);
		System.out.println("Buscando referenciaDocumentoSummary="+referenciaDocumentoSummary);
		if(referenciaDocumentoSummary != null){
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contenType= mimemap.getContentType(referenciaDocumentoSummary.getPath().toLowerCase());
			BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummary.getPath()), contenType, folderPercapita.replace("{ANO}", ano.toString()));
			System.out.println("response upload template --->"+response);
			documentService.updateDocumentTemplate(referenciaDocumentoSummary.getId(), response.getNodeRef(), response.getFileName(), contenType);
			ModificacionDistribucionInicialPercapita modificacionPercapita = distribucionInicialPercapitaDAO.findModificacionDistribucionInicialById(idModificacionDistribucionInicialPercapita);
			documentService.createDocumentModificacionPercapita(modificacionPercapita, tipoDocumento, referenciaDocumentoId, lastVersion);
		}
	}

	public ReferenciaDocumentoSummaryVO getLastDocumentoSummaryByModificacionDistribucionInicialPercapitaType(Integer idModificacionDistribucionInicialPercapita, TipoDocumentosProcesos tipoDocumento){
		return documentService.getLastDocumentSummaryModificacionPercapitaByType(idModificacionDistribucionInicialPercapita, tipoDocumento);
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
	public void enviarResolucionesServicioSalud(Integer idModificacionPercapita, Integer ano) {
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
					List<ReferenciaDocumentoSummaryVO> referenciasDocumentoSummaryVO = documentService.getVersionFinalByServicioModificacionPercapitaTypes(idModificacionPercapita, servicio.getId_servicio(), TipoDocumentosProcesos.ORDINARIOMODIFICACIONRESOLUCIONAPORTEESTATAL);
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
						generadorDocumento.saveContent(documentDocumentoResolucionVO.getContent());
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
	public void enviarConsultaRegional(Integer idModificacionDistribucionInicialPercapita, Integer oficioConsultaId, Integer ano) {
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
			ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryOficioConsulta = documentService.getDocumentById(oficioConsultaId);
			if(referenciaDocumentoSummaryOficioConsulta == null){
				DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(ano);
				List<DocumentoDistribucionInicialPercapita> documentosOficioConsulta  = distribucionInicialPercapitaDAO.getByIdDistribucionInicialPercapitaTipo(distribucionInicialPercapita.getIdDistribucionInicialPercapita(), TipoDocumentosProcesos.OFICIOCONSULTA);
				if(documentosOficioConsulta != null && documentosOficioConsulta.size() > 0){
					referenciaDocumentoSummaryOficioConsulta = new ReferenciaDocumentoMapper().getSummary(documentosOficioConsulta.get(0).getIdDocumento());
				}
			}
			DocumentoVO documentDocumentoOficioConsultaVO = documentService.getDocument(referenciaDocumentoSummaryOficioConsulta.getId());
			String fileNameDocumentoOficioConsulta = tmpDirDoc + File.separator + documentDocumentoOficioConsultaVO.getName();
			GeneradorWord generadorWordOficioConsulta = new GeneradorWord(fileNameDocumentoOficioConsulta);
			generadorWordOficioConsulta.saveContent(documentDocumentoOficioConsultaVO.getContent(), XWPFDocument.class);
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

	public void administrarVersionesFinalesAlfresco(Integer idModificacionDistribucionInicialPercapita, Integer ano) {
		System.out.println("DistribucionInicialPercapitaService administrarVersionesFinalesAlfresco eliminar todas las versiones que no sean finales");
		TipoDocumentosProcesos[] tiposDocumentos = {TipoDocumentosProcesos.RESOLUCIONAPORTEESTATALUR, TipoDocumentosProcesos.RESOLUCIONAPORTEESTATALCF};
		List<DocumentoModificacionPercapita> documentosResoluciones = distribucionInicialPercapitaDAO.getByIdModificacionPercapitaTipoNotFinal(idModificacionDistribucionInicialPercapita, tiposDocumentos);
		if(documentosResoluciones != null && documentosResoluciones.size() > 0){
			for(DocumentoModificacionPercapita documentoModificacionPercapita : documentosResoluciones){
				String key = ((documentoModificacionPercapita.getDocumento().getNodeRef() == null) ? documentoModificacionPercapita.getDocumento().getPath() : documentoModificacionPercapita.getDocumento().getNodeRef().replace("workspace://SpacesStore/", ""));
				System.out.println("key->"+key);
				alfrescoService.delete(key);
				distribucionInicialPercapitaDAO.deleteDocumentoModificacionPercapita(documentoModificacionPercapita.getIdDocumentoModificacionPercapita());
				distribucionInicialPercapitaDAO.deleteDocumento(documentoModificacionPercapita.getDocumento().getId());
			}
		}
	}

	public void generarOrdinarioSolicitudAntecedentes(Integer idModificacionDistribucionInicialPercapita, Integer ano) {
		System.out.println("ModificarDistribucion::generarOrdinarioSolicitudAntecedentes->"+idModificacionDistribucionInicialPercapita);
		Integer plantillaIdResolucionRetiro = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLAORDINARIOSOLICITUDANTECEDENTES);
		if(plantillaIdResolucionRetiro == null){
			throw new RuntimeException("No se puede crear Ordinario solicitud antecedentes, la plantilla no esta cargada");
		}
		try {
			ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryOrdinarioSolicitudAntecedentesVO = documentService.getDocumentByPlantillaId(plantillaIdResolucionRetiro);
			DocumentoVO documentoOrdinarioSolicitudAntecedentesVO = documentService.getDocument(referenciaDocumentoSummaryOrdinarioSolicitudAntecedentesVO.getId());
			String templateOrdinarioSolicitudAntecedentes = tmpDirDoc + File.separator + documentoOrdinarioSolicitudAntecedentesVO.getName();
			templateOrdinarioSolicitudAntecedentes = templateOrdinarioSolicitudAntecedentes.replace(" ", "");

			System.out.println("templateOrdinarioSolicitudAntecedentes template-->"+templateOrdinarioSolicitudAntecedentes);
			GeneradorWord generadorWordPlantillaOrdinarioSolicitudAntecedentes = new GeneradorWord(templateOrdinarioSolicitudAntecedentes);
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			generadorWordPlantillaOrdinarioSolicitudAntecedentes.saveContent(documentoOrdinarioSolicitudAntecedentesVO.getContent(), XWPFDocument.class);
			Map<String, Object> parametersOrdinarioSolicitudAntecedentes = new HashMap<String, Object>();
			String filenameOrdinarioSolicitudAntecedentes = tmpDirDoc + File.separator + new Date().getTime() + "_" + "OrdinarioSolicitudAntecedentes.docx";
			String contentTypeOrdinarioSolicitudAntecedentes = mimemap.getContentType(filenameOrdinarioSolicitudAntecedentes.toLowerCase());
			GeneradorResolucionAporteEstatal generadorWordResolucionRetiro = new GeneradorResolucionAporteEstatal(filenameOrdinarioSolicitudAntecedentes, templateOrdinarioSolicitudAntecedentes);
			generadorWordResolucionRetiro.replaceValues(parametersOrdinarioSolicitudAntecedentes, XWPFDocument.class);
			BodyVO responseBorradorOrdinarioSolicitudAntecedentes = alfrescoService.uploadDocument(new File(filenameOrdinarioSolicitudAntecedentes), contentTypeOrdinarioSolicitudAntecedentes, folderPercapita.replace("{ANO}", ano.toString()));
			System.out.println("response responseOrdinarioSolicitudAntecedentes --->"+responseBorradorOrdinarioSolicitudAntecedentes);
			plantillaIdResolucionRetiro = documentService.createDocumentModificacionPercapita(idModificacionDistribucionInicialPercapita, null, TipoDocumentosProcesos.ORDINARIOSOLICITUDANTECEDENTES, responseBorradorOrdinarioSolicitudAntecedentes.getNodeRef(), responseBorradorOrdinarioSolicitudAntecedentes.getFileName(), contentTypeOrdinarioSolicitudAntecedentes);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Docx4JException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
	}

	public void enviarOrdinarioSolicitudAntecedentes(Integer idModificacionDistribucionInicialPercapita, Integer ano) {
		Integer idPlantillaCorreo = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLACORREOORDINARIOSOLICITUDANTECEDENTES);
		if(idPlantillaCorreo == null){
			throw new RuntimeException("No se puede crear plantilla correo ORDINARIO SOLICITUD ANTECEDENTES, la plantilla no esta cargada");
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
		ReferenciaDocumentoSummaryVO referenciaDocumentoFinalSummaryVO = null;
		List<ReferenciaDocumentoSummaryVO> referenciasDocumentoSummaryVO = documentService.getVersionFinalModificacionDistribucionInicialByType(idModificacionDistribucionInicialPercapita, TipoDocumentosProcesos.ORDINARIOSOLICITUDANTECEDENTES);
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
					DocumentoVO documentDocumentoRetiroVO = documentService.getDocument(referenciaDocumentoFinalSummaryVO.getId());
					String fileNameDocumentoConvenio = tmpDirDoc + File.separator + documentDocumentoRetiroVO.getName();
					GeneradorDocumento generadorDocumento = new GeneradorDocumento(fileNameDocumentoConvenio);
					generadorDocumento.saveContent(documentDocumentoRetiroVO.getContent());
					EmailService.Adjunto adjunto = new EmailService.Adjunto();
					adjunto.setDescripcion("Ordinario Solicitud Antecedentes");
					adjunto.setName(documentDocumentoRetiroVO.getName());
					adjunto.setUrl((new File(fileNameDocumentoConvenio)).toURI().toURL());
					adjuntos.add(adjunto);
					List<String> to = new ArrayList<String>();
					ReporteEmailsDestinatarios destinatarioPara = new ReporteEmailsDestinatarios();
					to.add(fonasa.getDirector().getEmail().getValor());
					destinatarioPara.setDestinatario(fonasa.getDirector());
					List<String> cc = new ArrayList<String>();
					if(emailPLantilla != null && emailPLantilla.getAsunto() != null && emailPLantilla.getCuerpo() != null){
						emailService.sendMail(to, cc, null, emailPLantilla.getAsunto(), emailPLantilla.getCuerpo().replaceAll("(\r\n|\n)", "<br />"), adjuntos);
					}else{
						emailService.sendMail(to, cc, null, "Ordinario Solicitud Antecedentes", "Estimado " + fonasa.getDirector().getNombre() + " " + fonasa.getDirector().getApellidoPaterno() + " " + ((fonasa.getDirector().getApellidoMaterno() != null) ? fonasa.getDirector().getApellidoMaterno() : "") + ": <br /> <p> l</p>", adjuntos);
					}
					ReporteEmailsEnviados reporteEmailsEnviados = new ReporteEmailsEnviados();
					ReporteEmailsAdjuntos reporteEmailsAdjuntos = new ReporteEmailsAdjuntos();
					reporteEmailsEnviados.setFecha(new Date());
					distribucionInicialPercapitaDAO.save(reporteEmailsEnviados);
					destinatarioPara.setReporteEmailsEnviado(reporteEmailsEnviados);
					destinatarioPara.setTipoDestinatario(new TipoDestinatario(TiposDestinatarios.PARA.getId()));
					distribucionInicialPercapitaDAO.save(destinatarioPara);
					ReferenciaDocumento referenciaDocumento = documentDAO.findById(referenciaDocumentoFinalSummaryVO.getId());
					reporteEmailsAdjuntos.setDocumento(referenciaDocumento);
					reporteEmailsAdjuntos.setReporteEmailsEnviado(reporteEmailsEnviados);
					distribucionInicialPercapitaDAO.save(reporteEmailsAdjuntos);
					ReporteEmailsModificacionPercapita reporteEmailsModificacionPercapita = new ReporteEmailsModificacionPercapita();
					ModificacionDistribucionInicialPercapita modificacionDistribucionInicialPercapita = distribucionInicialPercapitaDAO.findModificacionDistribucionInicialById(idModificacionDistribucionInicialPercapita);
					reporteEmailsModificacionPercapita.setModificacionDistribucionInicialPercapita(modificacionDistribucionInicialPercapita);
					reporteEmailsModificacionPercapita.setReporteEmailsEnviados(reporteEmailsEnviados);
					distribucionInicialPercapitaDAO.save(reporteEmailsModificacionPercapita);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ReferenciaDocumentoSummaryVO getLastDocumentSummaryModificacionPercapitaByType(Integer idModificacionDistribucionInicialPercapita, TipoDocumentosProcesos tipoDocumento) {
		return documentService.getLastDocumentSummaryModificacionPercapitaByType(idModificacionDistribucionInicialPercapita, tipoDocumento);
	}

	public Integer getPlantillaCorreo(TipoDocumentosProcesos plantilla) {
		Integer plantillaId = documentService.getPlantillaByType(plantilla);
		return documentService.getDocumentoIdByPlantillaId(plantillaId);
	}

	public Integer createSeguimientoModificacionPercapita(Integer idModificacionDistribucionInicialPercapita, TareasSeguimiento tareaSeguimiento,
			String subject, String body, String username, List<String> para,
			List<String> conCopia, List<String> conCopiaOculta,
			List<Integer> documentos, Integer ano) {
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
				BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummaryVO.getPath()), contenType, folderPercapita.replace("{ANO}", ano.toString()));
				System.out.println("response upload template --->"+response);
				documentService.updateDocumentTemplate(referenciaDocumentoId, response.getNodeRef(), response.getFileName(), contenType);
			}
		}
		Integer idSeguimiento = seguimientoService.createSeguimiento(tareaSeguimiento, subject, body, from, para, conCopia, conCopiaOculta, documentosTmp);
		Seguimiento seguimiento = seguimientoDAO.getSeguimientoById(idSeguimiento);
		return distribucionInicialPercapitaDAO.createSeguimientoModificacion(idModificacionDistribucionInicialPercapita, seguimiento);

	}

	public int countVersionFinalModificacionPercapitaByType(Integer idModificacionDistribucionInicialPercapita, TipoDocumentosProcesos tipoDocumento) {
		List<ReferenciaDocumentoSummaryVO> versionesFinales = documentService.getVersionFinalModificacionPercapitaByType(idModificacionDistribucionInicialPercapita, tipoDocumento);
		if(versionesFinales != null && versionesFinales.size() > 0){
			return versionesFinales.size();
		}
		return 0;
	}

	public void moveToAlfrescoModificacion(Integer idModificacionDistribucionInicialPercapita, Integer referenciaDocumentoId, TipoDocumentosProcesos tipoDocumento,
			Boolean lastVersion, Integer ano) {
		System.out.println("Buscando referenciaDocumentoId="+referenciaDocumentoId);
		System.out.println("Buscando idModificacionDistribucionInicialPercapita="+idModificacionDistribucionInicialPercapita);
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummary = documentService.getDocumentSummary(referenciaDocumentoId);
		System.out.println("Buscando referenciaDocumentoSummary="+referenciaDocumentoSummary);
		if(referenciaDocumentoSummary != null){
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contenType = mimemap.getContentType(referenciaDocumentoSummary.getPath().toLowerCase());
			BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummary.getPath()), contenType, folderPercapita.replace("{ANO}", ano.toString()));
			System.out.println("response upload template --->"+response);
			documentService.updateDocumentTemplate(referenciaDocumentoSummary.getId(), response.getNodeRef(), response.getFileName(), contenType);
			ModificacionDistribucionInicialPercapita modificacionDistribucionInicialPercapita = distribucionInicialPercapitaDAO.findModificacionDistribucionInicialById(idModificacionDistribucionInicialPercapita);
			documentService.createDocumentModificacionPercapita(modificacionDistribucionInicialPercapita, tipoDocumento, referenciaDocumentoId, lastVersion);
		}
	}


	public void moveToAlfrescoModificacion(Integer idModificacionDistribucionInicialPercapita, Integer idServicio, Integer referenciaDocumentoId, TipoDocumentosProcesos tipoDocumento,
			Boolean lastVersion, Integer ano) {
		System.out.println("Buscando referenciaDocumentoId="+referenciaDocumentoId);
		System.out.println("Buscando idModificacionDistribucionInicialPercapita="+idModificacionDistribucionInicialPercapita);
		System.out.println("Buscando idServicio="+idServicio);
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummary = documentService.getDocumentSummary(referenciaDocumentoId);
		System.out.println("Buscando referenciaDocumentoSummary="+referenciaDocumentoSummary);
		if(referenciaDocumentoSummary != null){
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contenType = mimemap.getContentType(referenciaDocumentoSummary.getPath().toLowerCase());
			BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummary.getPath()), contenType, folderPercapita.replace("{ANO}", ano.toString()));
			System.out.println("response upload template --->"+response);
			documentService.updateDocumentTemplate(referenciaDocumentoSummary.getId(), response.getNodeRef(), response.getFileName(), contenType);
			ModificacionDistribucionInicialPercapita modificacionDistribucionInicialPercapita = distribucionInicialPercapitaDAO.findModificacionDistribucionInicialById(idModificacionDistribucionInicialPercapita);
			documentService.createDocumentModificacionPercapita(modificacionDistribucionInicialPercapita, idServicio, tipoDocumento, referenciaDocumentoId, lastVersion);
		}
	}

	public Integer countVersionFinalModificacionPercapitaResoluciones(Integer idModificacionPercapita, Integer idServicio) {
		List<ReferenciaDocumentoSummaryVO> versionesFinales = documentService.getVersionFinalModificacionPercapitaByType(idModificacionPercapita, idServicio, TipoDocumentosProcesos.ORDINARIOMODIFICACIONRESOLUCIONAPORTEESTATAL);
		if(versionesFinales != null && versionesFinales.size() > 0){
			return versionesFinales.size();
		} 
		return 0;
	}

	public static void main(String[] args){
		double valor = 6661.46;
		String val = valor+"";
		BigDecimal big = new BigDecimal(val);
		big = big.setScale(2, RoundingMode.HALF_UP);
		System.out.println("Número : "+big);
		System.out.println("Número long : "+Math.round(big.doubleValue()));
	}

}
