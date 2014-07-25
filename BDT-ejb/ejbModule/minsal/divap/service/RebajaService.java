package minsal.divap.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import minsal.divap.dao.DistribucionInicialPercapitaDAO;
import minsal.divap.dao.RebajaDAO;
import minsal.divap.dao.ServicioSaludDAO;
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.enums.FieldType;
import minsal.divap.enums.TemplatesType;
import minsal.divap.excel.GeneradorExcel;
import minsal.divap.excel.impl.AsignacionRecursosPercapitaSheetExcel;
import minsal.divap.excel.impl.RebajaExcelValidator;
import minsal.divap.excel.impl.RebajaSheetExcel;
import minsal.divap.exception.ExcelFormatException;
import minsal.divap.vo.BaseVO;
import minsal.divap.vo.BodyVO;
import minsal.divap.vo.CellTypeExcelVO;
import minsal.divap.vo.CumplimientoVO;
import minsal.divap.vo.RebajaVO;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.Cumplimiento;
import cl.minsal.divap.model.ServicioSalud;
import cl.minsal.divap.model.TipoCumplimiento;
import cl.minsal.divap.model.Usuario;

@Stateless
@LocalBean
public class RebajaService {
	@EJB
	private DistribucionInicialPercapitaDAO distribucionInicialPercapitaDAO;
	@EJB
	private UsuarioDAO usuarioDAO;
	@EJB
	private ServicioSaludDAO servicioSaludDAO;
	@EJB
	private RebajaDAO rebajaDAO;
	@EJB
	private DocumentService documentService;
	@EJB
	private ServicioSaludService servicioSaludService;
	@EJB
	private AlfrescoService alfrescoService;
	@Resource(name="tmpDir")
	private String tmpDir;
	@Resource(name="folderTemplateRebaja")
	private String folderTemplateRebaja;
	

	public Integer getPlantillaBaseCumplimiento(){
		Integer plantillaId = documentService.getPlantillaByType(TemplatesType.BASECUMPLIMIENTO);
		if(plantillaId == null){
			List<RebajaVO> servicios = getAllServiciosyComunasConId();
			
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String filename = tmpDir + File.separator + "plantillaBaseCumplimiento.xlsx";
			String contenType = mimemap.getContentType(filename.toLowerCase());
			GeneradorExcel generadorExcel = new GeneradorExcel(filename);
			List<String> headers = new ArrayList<String>();
			headers.add("2,SERVICIOS");
			headers.add("2,COMUNA");
			headers.add("3,CUMPLIMIENTOS");
			List<String> subHeaders = new ArrayList<String>();
			subHeaders.add("ID");	
			subHeaders.add("SERVICIO");
			subHeaders.add("ID");	
			subHeaders.add("COMUNA");
			subHeaders.addAll(getAllTipoCumplimiento());
			
			RebajaSheetExcel rebajaSheetExcel = new RebajaSheetExcel(headers,subHeaders, servicios);
			generadorExcel.addSheetCumplimiento(rebajaSheetExcel, "Hoja 1");
			try {
				BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderTemplateRebaja);
				System.out.println("response rebajaSheetExcel --->"+response);
				plantillaId = documentService.createTemplate(TemplatesType.BASECUMPLIMIENTO, response.getNodeRef(), response.getFileName(), contenType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			plantillaId = documentService.getDocumentoIdByPlantillaId(plantillaId);
		}
		return plantillaId;
	}

	public List<RebajaVO> getAllServiciosyComunasConId() {
		List<ServicioSalud> serviciosSalud = this.servicioSaludDAO.getServicios();
		List<RebajaVO> result = new ArrayList<RebajaVO>();
		if(serviciosSalud != null){
			for (ServicioSalud servicioSalud : serviciosSalud){
				for (Comuna comuna : servicioSalud.getComunas()){
					RebajaVO rebajaVO = new RebajaVO();
					rebajaVO.setId_servicio(servicioSalud.getId());
					rebajaVO.setServicio(((servicioSalud.getNombre() != null)?servicioSalud.getNombre():null));
					rebajaVO.setId_comuna(comuna.getId());
					rebajaVO.setComuna(((comuna != null)?comuna.getNombre():null));
					result.add(rebajaVO);
				}
			}
		}
		return result;
	}
	
	public List<String> getAllTipoCumplimiento(){
		List<TipoCumplimiento> tipo_cumplimientos = this.rebajaDAO.getAllTipoCumplimiento();
		List<String> result = new ArrayList<String>();
		if(tipo_cumplimientos!=null){
			for(TipoCumplimiento tcum: tipo_cumplimientos){
				result.add(tcum.getDescripcion());
			}
		}
		return result;
	}
	
	public void procesarCalculoRebaja(XSSFWorkbook workbook) throws ExcelFormatException {
		List<CellTypeExcelVO> cells = new ArrayList<CellTypeExcelVO>();
		CellTypeExcelVO fieldOne = new CellTypeExcelVO(true, FieldType.INTEGERFIELD);
		cells.add(fieldOne);
		CellTypeExcelVO fieldTwo = new CellTypeExcelVO(true);
		cells.add(fieldTwo);
		CellTypeExcelVO fieldThree = new CellTypeExcelVO(true, FieldType.INTEGERFIELD);
		cells.add(fieldThree);
		CellTypeExcelVO fieldFour = new CellTypeExcelVO(true);
		cells.add(fieldFour);
		CellTypeExcelVO fieldFive = new CellTypeExcelVO(true, FieldType.INTEGERFIELD);
		cells.add(fieldFive);
		CellTypeExcelVO fieldSix = new CellTypeExcelVO(true, FieldType.INTEGERFIELD);
		cells.add(fieldSix);
		CellTypeExcelVO fieldSeven = new CellTypeExcelVO(true, FieldType.INTEGERFIELD);
		cells.add(fieldSeven);
		
		XSSFSheet worksheet = workbook.getSheetAt(0);
		RebajaExcelValidator cumplimientoExcelValidator = new RebajaExcelValidator(cells.size(), cells, true, 0, 2);
		cumplimientoExcelValidator.validateFormat(worksheet);		
		List<CumplimientoVO> items = cumplimientoExcelValidator.getItems();
		for(CumplimientoVO item : items){
			System.out.println("item->"+item);
		}
	}
}
