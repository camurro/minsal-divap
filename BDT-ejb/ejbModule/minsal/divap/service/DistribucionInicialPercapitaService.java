package minsal.divap.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import minsal.divap.dao.DistribucionInicialPercapitaDAO;
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.enums.FieldType;
import minsal.divap.enums.TemplatesType;
import minsal.divap.excel.GeneradorExcel;
import minsal.divap.excel.impl.AsignacionRecursosPercapitaSheetExcel;
import minsal.divap.excel.impl.PercapitaDesempenoDificilExcelValidator;
import minsal.divap.exception.ExcelFormatException;
import minsal.divap.vo.BaseVO;
import minsal.divap.vo.BodyVO;
import minsal.divap.vo.CellTypeExcelVO;
import minsal.divap.vo.DesempenoDificilVO;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import cl.minsal.divap.model.Usuario;

@Stateless
@LocalBean
public class DistribucionInicialPercapitaService {
	@EJB
	private DistribucionInicialPercapitaDAO distribucionInicialPercapitaDAO;
	@EJB
	private UsuarioDAO usuarioDAO;
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
			for(BaseVO baseVO : servicios){
				System.out.println("getIdPlantillaRecursosPerCapita baseVO="+baseVO);
			}

			
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


	public void procesarCalculoPercapita(XSSFWorkbook workbook) {

	}


	public void procesarValorBasicoDesempeno(XSSFWorkbook workbook) throws ExcelFormatException {
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
		for(DesempenoDificilVO item : items){
			System.out.println("item->"+item);
		}
	}

	public void procesarCalculoPercapita(HSSFSheet workbook) {
		throw new NotImplementedException();
	}

	public void procesarValorBasicoDesempeno(HSSFSheet workbook) {
		throw new NotImplementedException();
	}

}
