package minsal.divap.service;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import minsal.divap.dao.AntecedentesComunaDAO;
import minsal.divap.dao.DistribucionInicialPercapitaDAO;
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.enums.FieldType;
import minsal.divap.enums.TemplatesType;
import minsal.divap.enums.TipoComuna;
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

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import cl.minsal.divap.model.AntecendentesComuna;
import cl.minsal.divap.model.AntecendentesComunaCalculado;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.DistribucionInicialPercapita;
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
	private DocumentService documentService;
	@EJB
	private ServicioSaludService servicioSaludService;
	@EJB
	private AlfrescoService alfrescoService;
	@Resource(name="tmpDir")
	private String tmpDir;
	@Resource(name="folderTemplatePercapita")
	private String folderTemplatePercapita;
	@Resource(name="folderPercapita")
	private String folderPercapita;


	public Integer crearIntanciaDistribucionInicialPercapita(String username){
		System.out.println("username-->"+username);
		Usuario usuario = this.usuarioDAO.getUserByUsername(username);
		return distribucionInicialPercapitaDAO.crearIntanciaDistribucionInicialPercapita(usuario);
	}


	public Integer getIdPlantillaPoblacionInscrita(){
		Integer plantillaId = documentService.getPlantillaByType(TemplatesType.DESEMPENODIFICIL);
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
				plantillaId = documentService.createTemplate(TemplatesType.DESEMPENODIFICIL, response.getNodeRef(), response.getFileName(), contenType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			plantillaId = documentService.getDocumentoIdByPlantillaId(plantillaId);
		}
		return plantillaId;
	}

	public Integer getIdPlantillaRecursosPerCapita(){
		Integer plantillaId = documentService.getPlantillaByType(TemplatesType.RECURSOSPERCAPITA);
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
				plantillaId = documentService.createTemplate(TemplatesType.RECURSOSPERCAPITA, response.getNodeRef(), response.getFileName(), contenType);
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
		Integer anoCurso = getAnoCurso(); 
		for(CalculoPercapitaVO calculoPercapitaVO : items){
			/*Comuna comuna = antecedentesComunaDAO.findByComunaServicio(calculoPercapitaVO.getServicio(), calculoPercapitaVO.getComuna(), anoCurso);
			if(comuna != null && comuna.getAntecendentesComunas() != null && comuna.getAntecendentesComunas().size() == 1){
				AntecendentesComuna antecendentesComuna = comuna.getAntecendentesComunas().iterator().next();
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
			}*/
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

	private Integer getAnoAnterior() {
		return getAnoCurso() - 1;
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
			//System.out.println("desempenoDificilVO.getServicio()="+desempenoDificilVO.getServicio()+" desempenoDificilVO.getComuna()="+desempenoDificilVO.getComuna()+" anoCurso="+getAnoCurso());

			AntecendentesComuna antecendentesComuna = antecedentesComunaDAO.findAntecendentesComunaByComunaServicioAno(desempenoDificilVO.getServicio(), desempenoDificilVO.getComuna(), getAnoCurso());
			//Comuna comuna = antecedentesComunaDAO.findByComunaServicio(desempenoDificilVO.getServicio(), desempenoDificilVO.getComuna(), getAnoCurso());
			//System.out.println("Comuna--->"+comuna.getId()	+" comuna.getAntecendentesComunas().size()-->"+ ((comuna.getAntecendentesComunas() != null) ? comuna.getAntecendentesComunas().size() : 0));

			//for(AntecendentesComuna antecendentesComunaTmp : comuna.getAntecendentesComunas() ){
			//	System.out.println("antecendentesComunaTmp.getIdAntecedentesComuna()"+antecendentesComunaTmp.getIdAntecedentesComuna());
			//}

			/*if(comuna != null && comuna.getAntecendentesComunas() != null && comuna.getAntecendentesComunas().size() == 1){
				AntecendentesComuna antecendentesComuna = comuna.getAntecendentesComunas().iterator().next();
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
			}*/
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
					System.out.println("getAnoAnterior()="+getAnoAnterior());
					Double perCapitaCostoFijo = antecedentesComunaDAO.findPerCapitaCostoFijoByServicioComunaAnoAnterior(antecendenteComunaCalculado.getAntecedentesComuna().getIdComuna().getId(), antecendenteComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId(), getAnoAnterior());
					System.out.println("perCapitaCostoFijo="+perCapitaCostoFijo);
					if(antecendenteComunaCalculado.getAntecedentesComuna().getClasificacion() != null){
						Double perCapitaAno = null;
						if(TipoComuna.RURAL.getId() == antecendenteComunaCalculado.getAntecedentesComuna().getClasificacion().getIdTipoComuna()){
							perCapitaAno = ((valorPerCapitaComunalMes * antecendenteComunaCalculado.getPoblacion() + antecendenteComunaCalculado.getPoblacionMayor() * antecendenteComunaCalculado.getAntecedentesComuna().getAnoAnoEnCurso().getAsignacionAdultoMayor())*12)/1000;
						} else if(TipoComuna.URBANA.getId() == antecendenteComunaCalculado.getAntecedentesComuna().getClasificacion().getIdTipoComuna()){
							perCapitaAno =  ((valorPerCapitaComunalMes * antecendenteComunaCalculado.getPoblacion() + antecendenteComunaCalculado.getPoblacionMayor() * antecendenteComunaCalculado.getAntecedentesComuna().getAnoAnoEnCurso().getAsignacionAdultoMayor())*12)/1000;
						} else if(TipoComuna.COSTOFIJO.getId() == antecendenteComunaCalculado.getAntecedentesComuna().getClasificacion().getIdTipoComuna()){
							if(antecendenteComunaCalculado.getAntecedentesComuna() != null && antecendenteComunaCalculado.getAntecedentesComuna().getAnoAnoEnCurso() != null){
								perCapitaAno = perCapitaCostoFijo * antecendenteComunaCalculado.getAntecedentesComuna().getAnoAnoEnCurso().getInflactor();
							}else{
								System.out.println("antecendenteComunaCalculado.getAntecedentesComuna().getIdAntecedentesComuna()-->"+antecendenteComunaCalculado.getAntecedentesComuna().getIdAntecedentesComuna());
								System.out.println("antecendenteComunaCalculado.getAntecedentesComuna().getAnoAnoEnCurso()-->"+antecendenteComunaCalculado.getAntecedentesComuna().getAnoAnoEnCurso());
							}
						}
						if(perCapitaAno != null){
							antecendenteComunaCalculado.setPercapitaAno(perCapitaAno);
							Double perCapitaMes = (perCapitaAno *1000)/12;
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
			planillaTrabajoId = documentService.createDocumentPercapita(distribucionInicialPercapita, response.getNodeRef(), response.getFileName(), contenType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return planillaTrabajoId;
	}

	public void procesarCalculoPercapita(HSSFSheet workbook) {
		throw new NotImplementedException();
	}

	public void procesarValorBasicoDesempeno(HSSFSheet workbook) {
		throw new NotImplementedException();
	}




}
