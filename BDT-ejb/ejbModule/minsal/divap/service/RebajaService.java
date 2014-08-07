package minsal.divap.service;

import java.io.File;
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
import minsal.divap.dao.RebajaDAO;
import minsal.divap.dao.ServicioSaludDAO;
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.enums.FieldType;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.excel.GeneradorExcel;
import minsal.divap.excel.impl.RebajaCalculadaSheetExcel;
import minsal.divap.excel.impl.RebajaExcelValidator;
import minsal.divap.excel.impl.RebajaSheetExcel;
import minsal.divap.exception.ExcelFormatException;
import minsal.divap.model.mappers.RebajaMapper;
import minsal.divap.vo.BodyVO;
import minsal.divap.vo.CellTypeExcelVO;
import minsal.divap.vo.ComunaVO;
import minsal.divap.vo.CumplimientoSummaryVO;
import minsal.divap.vo.CumplimientoVO;
import minsal.divap.vo.DocumentSummaryVO;
import minsal.divap.vo.PlanillaRebajaCalculadaVO;
import minsal.divap.vo.RebajaVO;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.ComunaCumplimiento;
import cl.minsal.divap.model.ComunaRebaja;
import cl.minsal.divap.model.Cumplimiento;
import cl.minsal.divap.model.Mes;
import cl.minsal.divap.model.Rebaja;
import cl.minsal.divap.model.ReferenciaDocumento;
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
	private AntecedentesComunaDAO antecedentesComunaDAO;
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

	@Resource(name="folderProcesoRebaja")
	private String folderProcesoRebaja;


	public Integer getPlantillaBaseCumplimiento(){
		Integer plantillaId = documentService.getPlantillaByType(TipoDocumentosProcesos.BASECUMPLIMIENTO);
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
				plantillaId = documentService.createTemplate(TipoDocumentosProcesos.BASECUMPLIMIENTO, response.getNodeRef(), response.getFileName(), contenType);
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

	public List<PlanillaRebajaCalculadaVO> getAllRebajasPlanillaTotal() {
		List<PlanillaRebajaCalculadaVO> datosPlanilla = new ArrayList<PlanillaRebajaCalculadaVO>();

		List<ServicioSalud> serviciosSalud = this.servicioSaludDAO.getServicios();

		String formato="MM";
		SimpleDateFormat dateFormat = new SimpleDateFormat(formato);

		if(serviciosSalud != null){
			for (ServicioSalud servicioSalud : serviciosSalud){
				for (Comuna comuna : servicioSalud.getComunas()){
					PlanillaRebajaCalculadaVO planilla = new PlanillaRebajaCalculadaVO();
					planilla.setId_servicio(servicioSalud.getId());
					planilla.setServicio(((servicioSalud.getNombre() != null)?servicioSalud.getNombre():null));
					planilla.setId_comuna(comuna.getId());
					planilla.setComuna(((comuna != null)?comuna.getNombre():null));
					List<Integer> comunaId = new ArrayList<Integer>();
					comunaId.add(comuna.getId());
					List<ComunaVO> comunaRebajas = getRebajasByComuna(comunaId,Integer.parseInt(dateFormat.format(new Date())));
					for(ComunaVO comVO : comunaRebajas){
						planilla.setCumplimientoItem1(comVO.getCumplimiento1().getValor());
						planilla.setCumplimientoItem2(comVO.getCumplimiento2().getValor());
						planilla.setCumplimientoItem3(comVO.getCumplimiento3().getValor());
						planilla.setRebajaCalculada1(comVO.getRebajaCalculada1());
						planilla.setRebajaCalculada2(comVO.getRebajaCalculada2());
						planilla.setRebajaCalculada3(comVO.getRebajaCalculada3());
						planilla.setRebajaFinal1(comVO.getRebajaFinal1());
						planilla.setRebajaFinal2(comVO.getRebajaFinal2());
						planilla.setRebajaFinal3(comVO.getRebajaFinal3());
					}

					planilla.setAporteEstatal(0);
					planilla.setMontoRebajaMes(0);
					planilla.setNuevoAporteEstatal(0);
					datosPlanilla.add(planilla);
				}
			}
		}
		return datosPlanilla;
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
		RebajaExcelValidator cumplimientoExcelValidator = new RebajaExcelValidator(cells.size(), cells, true, 0, 1);
		cumplimientoExcelValidator.validateFormat(worksheet);		
		List<CumplimientoVO> items = cumplimientoExcelValidator.getItems();

		String formato="MM";
		SimpleDateFormat dateFormat = new SimpleDateFormat(formato);

		List<ComunaCumplimiento> cumplimientosExistentes = rebajaDAO.getAllCumplimientoPorMes(Integer.parseInt(dateFormat.format(new Date())));
		if(cumplimientosExistentes.size() > 0){
			List<Integer> listaIdCumplimientos = new ArrayList<Integer>();
			for(ComunaCumplimiento cumpleComuna : cumplimientosExistentes){
				listaIdCumplimientos.add(cumpleComuna.getIdComunaCumplimiento());
			}
			rebajaDAO.deleteComunaRebajaByIdCumplimiento(listaIdCumplimientos);
			rebajaDAO.deleteComunaCumplimientoByIdCumplimiento(listaIdCumplimientos);
		}
		for(CumplimientoVO item : items){

			Comuna comuna = new Comuna();
			comuna.setId(item.getId_comuna());

			Mes mes = new Mes();
			mes.setIdMes(Integer.parseInt(dateFormat.format(new Date())));

			ComunaCumplimiento comunaCum1 = new ComunaCumplimiento();
			comunaCum1.setIdComuna(comuna);			
			comunaCum1.setValor(item.getValue_item1());
			comunaCum1.setIdTipoCumplimiento(new TipoCumplimiento(1));
			comunaCum1.setIdMes(mes);
			rebajaDAO.persistCumplimientoComunas(comunaCum1);

			//reglaCalculoRebajaPorComuna(comunaCum1);

			ComunaCumplimiento comunaCum2 = new ComunaCumplimiento();
			comunaCum2.setIdComuna(comuna);			
			comunaCum2.setValor(item.getValue_item2());
			comunaCum2.setIdTipoCumplimiento(new TipoCumplimiento(2));
			comunaCum2.setIdMes(mes);
			rebajaDAO.persistCumplimientoComunas(comunaCum2);

			//reglaCalculoRebajaPorComuna(comunaCum2);

			ComunaCumplimiento comunaCum3 = new ComunaCumplimiento();
			comunaCum3.setIdComuna(comuna);			
			comunaCum3.setValor(item.getValue_item3());
			comunaCum3.setIdTipoCumplimiento(new TipoCumplimiento(3));
			comunaCum3.setIdMes(mes);
			rebajaDAO.persistCumplimientoComunas(comunaCum3);
			//reglaCalculoRebajaPorComuna(comunaCum3);
		}
	}

	public List<ComunaVO> getRebajasByComuna(List<Integer> comunasId, int idMes){
		List<Comuna> comunasRebaja = rebajaDAO.getRebajasByComunas(comunasId,idMes);
		List<ComunaVO> comunasRebajaVO = new ArrayList<ComunaVO>();
		for(Comuna comuna : comunasRebaja){
			ComunaVO comunaVO = new ComunaVO();
			comunaVO.setIdComuna(comuna.getId());
			comunaVO.setDescComuna(comuna.getNombre());

			List<ComunaCumplimiento> comunaCumplimientos = rebajaDAO.getAllCumplimientoPorComuna(comuna.getId());

			for (int i = 0; i < comunaCumplimientos.size(); i++) {
				CumplimientoSummaryVO cumpleSummaryVO = new RebajaMapper().getSummary(comunaCumplimientos.get(i));
				if(i==0){
					comunaVO.setCumplimiento1(cumpleSummaryVO);
				}
				if(i==1){
					comunaVO.setCumplimiento2(cumpleSummaryVO);
				}
				if(i==2){
					comunaVO.setCumplimiento3(cumpleSummaryVO);
				}
			}

			List<ComunaRebaja> comunaRebaja = rebajaDAO.getallRebajaByComuna(comuna.getId());
			comunaVO.setRebajaCalculada1(comunaRebaja.get(0).getRebajaCalculada());
			comunaVO.setRebajaCalculada2(comunaRebaja.get(1).getRebajaCalculada());
			comunaVO.setRebajaCalculada3(comunaRebaja.get(2).getRebajaCalculada());

			Double totalCalculado = comunaRebaja.get(0).getRebajaCalculada()  +comunaRebaja.get(1).getRebajaCalculada()+comunaRebaja.get(2).getRebajaCalculada();
			comunaVO.setTotalCalculada(totalCalculado);

			comunaVO.setRebajaFinal1(comunaRebaja.get(0).getRebajaFinal());
			comunaVO.setRebajaFinal2(comunaRebaja.get(1).getRebajaFinal());
			comunaVO.setRebajaFinal3(comunaRebaja.get(2).getRebajaFinal());

			Double totalFinal = comunaRebaja.get(0).getRebajaFinal()  +comunaRebaja.get(1).getRebajaFinal()+comunaRebaja.get(2).getRebajaFinal();
			comunaVO.setTotalFinal(totalFinal);


			String formato="yyyy";
			SimpleDateFormat dateFormat = new SimpleDateFormat(formato);
			Double aporteEstatal = antecedentesComunaDAO.findPerCapitaCostoFijoByServicioComunaAnoAnterior(comuna.getId(), comuna.getServicioSalud().getId(), Integer.parseInt(dateFormat.format(new Date())));

			comunaVO.setAporteEstatal((aporteEstatal != null?aporteEstatal.intValue():Integer.parseInt("0")));


			Double montoRebaja = comunaVO.getAporteEstatal()*comunaVO.getTotalFinal()/100;
			comunaVO.setMontoRebaja(montoRebaja.intValue());

			Double nuevoAporte = comunaVO.getAporteEstatal() - montoRebaja;
			comunaVO.setNuevoAporteEstatal(nuevoAporte.intValue());

			comunasRebajaVO.add(comunaVO);
		}
		return comunasRebajaVO;
	}

	public Integer calculaRebajaMes(int idMes, int idProceso){
		List<ComunaCumplimiento> comunaCumplimiento = rebajaDAO.getAllCumplimientoPorMes(idMes);
		if(comunaCumplimiento.size()>0){
			List<Integer> listaIdCumplimientos = new ArrayList<Integer>();
			for(ComunaCumplimiento cumpleComuna : comunaCumplimiento){
				listaIdCumplimientos.add(cumpleComuna.getIdComunaCumplimiento());
			}
			rebajaDAO.deleteComunaRebajaByIdCumplimiento(listaIdCumplimientos);
			for(ComunaCumplimiento comunaCumple : comunaCumplimiento){
				reglaCalculoRebajaPorComuna(comunaCumple);
			}
		}
		return generarExcelRebajaCalculada(idProceso);
	}

	private void reglaCalculoRebajaPorComuna(ComunaCumplimiento comunaCumplimiento) {
		List<Cumplimiento> cumplimiento = rebajaDAO.getAllCumplimientoByTipoCumplimiento(comunaCumplimiento.getIdTipoCumplimiento().getIdTipoCumplimiento());
		for(Cumplimiento cumple : cumplimiento){
			System.out.println(cumple.getIdCumplimiento());
			if(comunaCumplimiento.getValor().intValue() >= cumple.getPorcentajeDesde().intValue() &&
					comunaCumplimiento.getValor().intValue() <= cumple.getPorcentajeHasta()){
				ComunaRebaja comunaRebaja = new ComunaRebaja();
				comunaRebaja.setComunaCumplimiento(comunaCumplimiento);
				comunaRebaja.setRebajaCalculada(cumple.getRebaja());
				comunaRebaja.setRebajaFinal(cumple.getRebaja());
				rebajaDAO.persistRebajaComuna(comunaRebaja);
			}
		}
	}

	public Integer generarExcelRebajaCalculada(int idProceso) {

		int documentId = 0;
		List<PlanillaRebajaCalculadaVO> servicios = getAllRebajasPlanillaTotal();
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator + "planillaRebajaCalculada.xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());
		GeneradorExcel generadorExcel = new GeneradorExcel(filename);
		List<String> headers = new ArrayList<String>();
		headers.add("2,SERVICIOS");
		headers.add("2,COMUNA");
		headers.add("1,APORTE ESTATAL");
		headers.add("3,CUMPLIMIENTOS");
		headers.add("3,REBAJA CALCULADA");
		headers.add("3,REBAJA FINAL");
		headers.add("1,MONTO REBAJA MES");
		headers.add("1,NUEVO APORTE MENSUAL");
		List<String> subHeaders = new ArrayList<String>();
		subHeaders.add("ID");	
		subHeaders.add("SERVICIO");
		subHeaders.add("ID");	
		subHeaders.add("COMUNA");
		subHeaders.add("");
		subHeaders.addAll(getAllTipoCumplimiento());
		subHeaders.add("REBAJA1");
		subHeaders.add("REBAJA2");
		subHeaders.add("REBAJA3");
		subHeaders.add("REB. FINAL1");
		subHeaders.add("REB. FINAL2");
		subHeaders.add("REB. FINAL3");
		subHeaders.add("");
		subHeaders.add("");
		RebajaCalculadaSheetExcel rebajaCalculadaSheetExcel = new RebajaCalculadaSheetExcel(headers,subHeaders, servicios);
		generadorExcel.addSheetRebajaCalculada(rebajaCalculadaSheetExcel, "Hoja 1");
		try {
			String formato="yyyy";
			SimpleDateFormat dateFormat = new SimpleDateFormat(formato);
			String[] folder = folderProcesoRebaja.split("/");
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folder[0]+"/"+dateFormat.format(new Date())+"/"+folder[1]);
			System.out.println("response rebajaCalculadaSheetExcel --->"+response);
			Rebaja rebaja = rebajaDAO.findRebajaById(idProceso);
			documentId = documentService.crearDocumentoRebajaCalculada(rebaja,response.getNodeRef(), response.getFileName(), contenType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return documentId;
	}

	public Integer crearIntanciaRebaja(String username){
		System.out.println("username-->"+username);
		Usuario usuario = this.usuarioDAO.getUserByUsername(username);
		return rebajaDAO.crearIntanciaRebaja(usuario);
	}


	public void updateMontosRebajaComuna(ComunaVO comuna){
		rebajaDAO.updateRebajaComuna(comuna.getCumplimiento1().getIdCumplimiento(),comuna.getRebajaFinal1());
		rebajaDAO.updateRebajaComuna(comuna.getCumplimiento2().getIdCumplimiento(),comuna.getRebajaFinal2());
		rebajaDAO.updateRebajaComuna(comuna.getCumplimiento3().getIdCumplimiento(),comuna.getRebajaFinal3());
	}

	public List<DocumentSummaryVO> getReferenciaDocumentosById(
			List<Integer> allDocuments) {
		List<ReferenciaDocumento> referenciaDocumentos = rebajaDAO.getReferenciaDocumentosById(allDocuments);
		List<DocumentSummaryVO> resumenDocumentos = new ArrayList<DocumentSummaryVO>();
		int i=1;
		for(ReferenciaDocumento referencia : referenciaDocumentos){
			DocumentSummaryVO resumen = new DocumentSummaryVO();
			resumen.setIdDocumento(referencia.getId());
			String[] nombreDocumento = referencia.getPath().split("\\.");
			resumen.setDescDocumento(nombreDocumento[0].concat("_v").concat(String.valueOf(i)).concat("."+nombreDocumento[1]));
			resumenDocumentos.add(resumen);
			i++;
		}
		return resumenDocumentos;
	}

}
