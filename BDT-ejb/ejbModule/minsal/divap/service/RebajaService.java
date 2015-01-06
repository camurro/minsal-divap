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

import minsal.divap.dao.AntecedentesComunaDAO;
import minsal.divap.dao.DistribucionInicialPercapitaDAO;
import minsal.divap.dao.ProgramasDAO;
import minsal.divap.dao.RebajaDAO;
import minsal.divap.dao.SeguimientoDAO;
import minsal.divap.dao.ServicioSaludDAO;
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.doc.GeneradorDocumento;
import minsal.divap.doc.GeneradorResolucionAporteEstatal;
import minsal.divap.doc.GeneradorWord;
import minsal.divap.enums.FieldType;
import minsal.divap.enums.TareasSeguimiento;
import minsal.divap.enums.TipoComuna;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.enums.TiposCumplimientos;
import minsal.divap.excel.GeneradorExcel;
import minsal.divap.excel.impl.RebajaCalculadaSheetExcel;
import minsal.divap.excel.impl.RebajaExcelValidator;
import minsal.divap.excel.impl.RebajaSheetExcel;
import minsal.divap.excel.interfaces.ExcelTemplate;
import minsal.divap.exception.ExcelFormatException;
import minsal.divap.model.mappers.CumplimientoRebajasMapper;
import minsal.divap.model.mappers.TipoCumplimientoMapper;
import minsal.divap.util.StringUtil;
import minsal.divap.vo.BodyVO;
import minsal.divap.vo.CellTypeExcelVO;
import minsal.divap.vo.CumplimientoRebajaVO;
import minsal.divap.vo.CumplimientoVO;
import minsal.divap.vo.DocumentSummaryVO;
import minsal.divap.vo.DocumentoVO;
import minsal.divap.vo.PlanillaRebajaCalculadaVO;
import minsal.divap.vo.RebajaVO;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.SeguimientoVO;
import minsal.divap.vo.ServiciosVO;
import minsal.divap.vo.TipoCumplimientoVO;
import minsal.divap.xml.GeneradorXML;
import minsal.divap.xml.email.Email;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.docx4j.openpackaging.exceptions.Docx4JException;

import cl.minsal.divap.model.AnoEnCurso;
import cl.minsal.divap.model.AntecedentesComunaCalculadoRebaja;
import cl.minsal.divap.model.AntecendentesComuna;
import cl.minsal.divap.model.AntecendentesComunaCalculado;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.ComunaCumplimiento;
import cl.minsal.divap.model.ComunaCumplimientoRebaja;
import cl.minsal.divap.model.Cumplimiento;
import cl.minsal.divap.model.DistribucionInicialPercapita;
import cl.minsal.divap.model.DocumentoRebaja;
import cl.minsal.divap.model.Mes;
import cl.minsal.divap.model.Rebaja;
import cl.minsal.divap.model.RebajaCorte;
import cl.minsal.divap.model.ReferenciaDocumento;
import cl.minsal.divap.model.Seguimiento;
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
	private ProgramasDAO programasDAO;
	@EJB
	private SeguimientoDAO seguimientoDAO;
	@EJB
	private AntecedentesComunaDAO antecedentesComunaDAO;
	@EJB
	private DocumentService documentService;
	@EJB
	private ServicioSaludService servicioSaludService;
	@EJB
	private SeguimientoService seguimientoService;
	@EJB
	private EmailService emailService;
	@EJB
	private AlfrescoService alfrescoService;
	@Resource(name="tmpDir")
	private String tmpDir;
	@Resource(name="tmpDirDoc")
	private String tmpDirDoc;
	@Resource(name="folderTemplateRebaja")
	private String folderTemplateRebaja;
	@Resource(name="folderProcesoRebaja")
	private String folderProcesoRebaja;

	private final double EPSILON = 0.0000001;


	public Integer getPlantillaBaseCumplimiento(){
		Integer plantillaId = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLABASECUMPLIMIENTO);
		if(plantillaId == null){
			List<RebajaVO> servicios = getAllServiciosyComunasConId();
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String filename = tmpDir + File.separator + "plantillaBaseCumplimiento.xlsx";
			String contenType = mimemap.getContentType(filename.toLowerCase());
			GeneradorExcel generadorExcel = new GeneradorExcel(filename);
			List<String> headers = new ArrayList<String>();
			headers.add("2,SERVICIOS");
			headers.add("2,COMUNA");
			headers.add("3,CUMPLIMIENTO");
			List<String> subHeaders = new ArrayList<String>();
			subHeaders.add("ID");	
			subHeaders.add("SERVICIO");
			subHeaders.add("ID");	
			subHeaders.add("COMUNA");
			subHeaders.addAll(getAllTipoCumplimiento());
			ExcelTemplate cumplimientoSheetExcel = new RebajaSheetExcel(headers, subHeaders, servicios);
			generadorExcel.addSheet(cumplimientoSheetExcel, "Hoja 1");
			try {
				BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderTemplateRebaja);
				System.out.println("response rebajaSheetExcel --->"+response);
				plantillaId = documentService.createTemplate(TipoDocumentosProcesos.PLANTILLABASECUMPLIMIENTO, response.getNodeRef(), response.getFileName(), contenType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			plantillaId = documentService.getDocumentoIdByPlantillaId(plantillaId);
		}
		return plantillaId;
	}

	public List<RebajaVO> getAllServiciosyComunasConId() {
		List<AntecendentesComuna> antecendentesComunas = this.servicioSaludDAO.getAntecentesComunasRebaja((getAnoCurso() + 1), new TipoComuna[] { TipoComuna.RURAL, TipoComuna.URBANA});
		List<RebajaVO> result = new ArrayList<RebajaVO>();
		if((antecendentesComunas != null) && (antecendentesComunas.size() > 0)){
			for (AntecendentesComuna antecendenteComuna : antecendentesComunas){
				if(antecendenteComuna.getIdComuna() != null){
					RebajaVO rebajaVO = new RebajaVO();
					if(antecendenteComuna.getIdComuna().getServicioSalud() != null){
						rebajaVO.setId_servicio(antecendenteComuna.getIdComuna().getServicioSalud().getId());
						rebajaVO.setServicio(antecendenteComuna.getIdComuna().getServicioSalud().getNombre());
					}
					rebajaVO.setId_comuna(antecendenteComuna.getIdComuna().getId());
					rebajaVO.setComuna(antecendenteComuna.getIdComuna().getNombre());
					result.add(rebajaVO);
				}
			}
		}
		return result;
	}

	private List<PlanillaRebajaCalculadaVO> getAllRebajasPlanillaTotal(Integer idRebaja) {
		List<PlanillaRebajaCalculadaVO> datosPlanilla = new ArrayList<PlanillaRebajaCalculadaVO>();
		List<AntecendentesComuna> antecendentesComunas = this.servicioSaludDAO.getAntecentesComunasRebaja((getAnoCurso() + 1), new TipoComuna[] { TipoComuna.RURAL, TipoComuna.URBANA});
		Rebaja rebaja = rebajaDAO.findRebajaById(idRebaja);
		if((antecendentesComunas != null) && (antecendentesComunas.size() > 0)){
			DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast((getAnoCurso() + 1));
			for (AntecendentesComuna antecendenteComuna : antecendentesComunas){
				System.out.println("servicioSalud.getId()->"+antecendenteComuna.getIdComuna().getServicioSalud().getId()+" comuna.getId()->"+antecendenteComuna.getIdComuna().getId()+" distribucionInicialPercapita.getIdDistribucionInicialPercapita()->"+distribucionInicialPercapita.getIdDistribucionInicialPercapita());
				List<AntecendentesComunaCalculado> antecendentesComunaCalculados = antecedentesComunaDAO.findAntecendentesComunaCalculadoByComunaServicioDistribucionInicialPercapitaVigente(antecendenteComuna.getIdComuna().getServicioSalud().getId(), antecendenteComuna.getIdComuna().getId(), distribucionInicialPercapita.getIdDistribucionInicialPercapita());
				AntecendentesComunaCalculado antecendentesComunaCalculado = ((antecendentesComunaCalculados != null && antecendentesComunaCalculados.size() > 0) ? antecendentesComunaCalculados.get(0): null);
				System.out.println("antecendentesComunaCalculado->" + antecendentesComunaCalculado);
				if(antecendentesComunaCalculado == null){
					System.out.println("antecendentesComunaCalculado es nulo, se continua con el siguiente si existe");
					continue;
				}

				AntecedentesComunaCalculadoRebaja antecedentesComunaCalculadoRebaja = null;
				if(antecendentesComunaCalculado.getAntecedentesComunaCalculadoRebajas() != null && antecendentesComunaCalculado.getAntecedentesComunaCalculadoRebajas().size() > 0){
					for(AntecedentesComunaCalculadoRebaja antecedentesRebaja : antecendentesComunaCalculado.getAntecedentesComunaCalculadoRebajas()){
						if(antecedentesRebaja.getRebaja().getIdRebaja().equals(idRebaja)){
							antecedentesComunaCalculadoRebaja = antecedentesRebaja;
							break;
						}
					}
				}

				PlanillaRebajaCalculadaVO planilla = new PlanillaRebajaCalculadaVO();
				planilla.setId_servicio(antecendenteComuna.getIdComuna().getServicioSalud().getId());
				planilla.setServicio(((antecendenteComuna.getIdComuna().getServicioSalud().getNombre() != null)?antecendenteComuna.getIdComuna().getServicioSalud().getNombre():null));
				planilla.setId_comuna(antecendenteComuna.getIdComuna().getId());
				planilla.setComuna(antecendenteComuna.getIdComuna().getNombre());
				Integer aporteEstatalMensual = ((antecendentesComunaCalculado.getPercapitaMes() == null) ? 0 : antecendentesComunaCalculado.getPercapitaMes().intValue());
				System.out.println("PercapitaMes="+aporteEstatalMensual);
				aporteEstatalMensual += ((antecendentesComunaCalculado.getDesempenoDificil() == null) ? 0 : antecendentesComunaCalculado.getDesempenoDificil());
				System.out.println("DesempenoDificil="+antecendentesComunaCalculado.getDesempenoDificil());
				planilla.setAporteEstatal(aporteEstatalMensual);
				List<CumplimientoRebajaVO> cumplimientoRebajasVO = getCumplimientoByRebajaComuna(idRebaja, antecendenteComuna.getIdComuna().getId());
				Integer montoRebaja = 0;
				if(cumplimientoRebajasVO != null && cumplimientoRebajasVO.size() > 0){
					Integer porcentajeRebajaCalculado = 0;
					Integer porcentajeRebajaFinal = 0;
					for(CumplimientoRebajaVO cumplimientoRebajaVO : cumplimientoRebajasVO){
						porcentajeRebajaFinal += (( cumplimientoRebajaVO.getRebajaFinal() == null) ? 0 : cumplimientoRebajaVO.getRebajaFinal().intValue());
						porcentajeRebajaCalculado += (( cumplimientoRebajaVO.getRebajaCalculada() == null) ? 0 : cumplimientoRebajaVO.getRebajaCalculada().intValue()); 
						TiposCumplimientos tipoCumplimiento = TiposCumplimientos.getById(cumplimientoRebajaVO.getTipoCumplimiento().getId());
						switch (tipoCumplimiento) {
						case ACTIVIDADGENERAL:
							planilla.setCumplimientoRebajasItem1(cumplimientoRebajaVO);
							break;
						case CONTINUIDADATENCIONSALUD:
							planilla.setCumplimientoRebajasItem2(cumplimientoRebajaVO);
							break;
						case ACTIVIDADGARANTIASEXPLICITASSALUD:
							planilla.setCumplimientoRebajasItem3(cumplimientoRebajaVO);
							break;
						default:
							break;
						}
					}
					String mesDesde = ((rebaja != null && rebaja.getRebajaCorte() != null && rebaja.getRebajaCorte().getMesDesde() != null)? rebaja.getRebajaCorte().getMesDesde().getNombre() : "");
					planilla.setMesDesde(mesDesde);
					String mesHasta = ((rebaja != null && rebaja.getRebajaCorte() != null && rebaja.getRebajaCorte().getMesHasta() != null)? rebaja.getRebajaCorte().getMesHasta().getNombre() : "");
					planilla.setMesHasta(mesHasta);
					String mesCorte = ((rebaja != null && rebaja.getRebajaCorte() != null && rebaja.getRebajaCorte().getMesRebaja() != null)? rebaja.getRebajaCorte().getMesRebaja().getNombre() : "");
					planilla.setMesCorte(mesCorte);
					planilla.setTotalRebajaCalculada(porcentajeRebajaCalculado);
					planilla.setTotalRebajaRebajaFinal(porcentajeRebajaFinal);
					System.out.println("aporteEstatalMensual-->"+aporteEstatalMensual);
					System.out.println("porcentajeRebajaFinal-->"+porcentajeRebajaFinal);
					montoRebaja = (int)Math.round(aporteEstatalMensual * (porcentajeRebajaFinal/100.0));
					System.out.println("montoRebaja-->"+montoRebaja);
					planilla.setMontoRebajaMes(montoRebaja);
				}

				if(antecedentesComunaCalculadoRebaja == null){
					antecedentesComunaCalculadoRebaja = new AntecedentesComunaCalculadoRebaja();
					antecedentesComunaCalculadoRebaja.setRebaja(rebaja);
					antecedentesComunaCalculadoRebaja.setAntecedentesComunaCalculado(antecendentesComunaCalculado);
					antecedentesComunaDAO.save(antecedentesComunaCalculadoRebaja);

				}
				antecedentesComunaCalculadoRebaja.setMontoRebaja(montoRebaja);
				if(montoRebaja == 0){
					System.out.println("No posee porcentaje de rebaja");
					continue;
				}
				planilla.setNuevoAporteEstatal(aporteEstatalMensual - montoRebaja);
				System.out.println("NuevoAporteEstatal-->"+planilla.getNuevoAporteEstatal());
				datosPlanilla.add(planilla);
			}
		}
		return datosPlanilla;
	}

	private void calculaMontoRebaja(Integer idRebaja) {
		List<AntecendentesComuna> antecendentesComunas = this.servicioSaludDAO.getAntecentesComunasRebaja((getAnoCurso() + 1), new TipoComuna[] { TipoComuna.RURAL, TipoComuna.URBANA});
		Rebaja rebaja = rebajaDAO.findRebajaById(idRebaja);
		if((antecendentesComunas != null) && (antecendentesComunas.size() > 0)){
			DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast((getAnoCurso() + 1));
			for (AntecendentesComuna antecendenteComuna : antecendentesComunas){
				System.out.println("servicioSalud.getId()->"+antecendenteComuna.getIdComuna().getServicioSalud().getId()+" comuna.getId()->"+antecendenteComuna.getIdComuna().getId()+" distribucionInicialPercapita.getIdDistribucionInicialPercapita()->"+distribucionInicialPercapita.getIdDistribucionInicialPercapita());
				List<AntecendentesComunaCalculado> antecendentesComunaCalculados = antecedentesComunaDAO.findAntecendentesComunaCalculadoByComunaServicioDistribucionInicialPercapitaVigente(antecendenteComuna.getIdComuna().getServicioSalud().getId(), antecendenteComuna.getIdComuna().getId(), distribucionInicialPercapita.getIdDistribucionInicialPercapita());
				AntecendentesComunaCalculado antecendentesComunaCalculado = ((antecendentesComunaCalculados != null && antecendentesComunaCalculados.size() > 0) ? antecendentesComunaCalculados.get(0): null);
				System.out.println("antecendentesComunaCalculado->" + antecendentesComunaCalculado);
				if(antecendentesComunaCalculado == null){
					System.out.println("antecendentesComunaCalculado es nulo se continua con el siguiente si existe");
					continue;
				}

				AntecedentesComunaCalculadoRebaja antecedentesComunaCalculadoRebaja = null;
				if(antecendentesComunaCalculado.getAntecedentesComunaCalculadoRebajas() != null && antecendentesComunaCalculado.getAntecedentesComunaCalculadoRebajas().size() > 0){
					for(AntecedentesComunaCalculadoRebaja antecedentesRebaja : antecendentesComunaCalculado.getAntecedentesComunaCalculadoRebajas()){
						if(antecedentesRebaja.getRebaja().getIdRebaja().equals(idRebaja)){
							antecedentesComunaCalculadoRebaja = antecedentesRebaja;
							break;
						}
					}
				}
				Integer aporteEstatalMensual = ((antecendentesComunaCalculado.getPercapitaMes() == null) ? 0 : antecendentesComunaCalculado.getPercapitaMes().intValue());
				System.out.println("PercapitaMes="+aporteEstatalMensual);
				aporteEstatalMensual += ((antecendentesComunaCalculado.getDesempenoDificil() == null) ? 0 : antecendentesComunaCalculado.getDesempenoDificil());
				System.out.println("DesempenoDificil="+antecendentesComunaCalculado.getDesempenoDificil());
				List<CumplimientoRebajaVO> cumplimientoRebajasVO = getCumplimientoByRebajaComuna(idRebaja, antecendenteComuna.getIdComuna().getId());
				if(cumplimientoRebajasVO != null && cumplimientoRebajasVO.size() > 0){
					Integer porcentajeRebajaCalculado = 0;
					Integer porcentajeRebajaFinal = 0;
					for(CumplimientoRebajaVO cumplimientoRebajaVO : cumplimientoRebajasVO){
						porcentajeRebajaFinal += (( cumplimientoRebajaVO.getRebajaFinal() == null) ? 0 : cumplimientoRebajaVO.getRebajaFinal().intValue());
						porcentajeRebajaCalculado += (( cumplimientoRebajaVO.getRebajaCalculada() == null) ? 0 : cumplimientoRebajaVO.getRebajaCalculada().intValue()); 
					}
					System.out.println("aporteEstatalMensual-->"+aporteEstatalMensual);
					System.out.println("porcentajeRebajaFinal-->"+porcentajeRebajaFinal);
					Integer montoRebaja = (int)Math.round(aporteEstatalMensual * (porcentajeRebajaFinal/100.0));
					System.out.println("montoRebaja-->"+montoRebaja);

					if(antecedentesComunaCalculadoRebaja == null){
						antecedentesComunaCalculadoRebaja = new AntecedentesComunaCalculadoRebaja();
						antecedentesComunaCalculadoRebaja.setRebaja(rebaja);
						antecedentesComunaCalculadoRebaja.setAntecedentesComunaCalculado(antecendentesComunaCalculado);
						antecedentesComunaDAO.save(antecedentesComunaCalculadoRebaja);
					}
					antecedentesComunaCalculadoRebaja.setMontoRebaja(montoRebaja);
				}
			}
		}
	}

	public List<CumplimientoRebajaVO> getCumplimientoByRebajaComuna(
			Integer idRebaja, Integer idComuna) {
		List<CumplimientoRebajaVO> cumplimientoRebajasVO = new ArrayList<CumplimientoRebajaVO>();
		List<ComunaCumplimiento> comunaCumplimientos = this.rebajaDAO.getCumplimientoByRebejaComuna(idRebaja, idComuna);
		if(comunaCumplimientos != null && comunaCumplimientos.size() > 0){
			for(ComunaCumplimiento comunaCumplimiento: comunaCumplimientos){
				cumplimientoRebajasVO.add(new CumplimientoRebajasMapper().getBasic(comunaCumplimiento));
			}
		}
		return cumplimientoRebajasVO;
	}

	public List<String> getAllTipoCumplimiento(){
		List<TipoCumplimiento> tipoCumplimientos = this.rebajaDAO.getAllTipoCumplimiento();
		List<String> result = new ArrayList<String>();
		if(tipoCumplimientos != null){
			for(TipoCumplimiento tipoCumplimiento: tipoCumplimientos){
				result.add(tipoCumplimiento.getDescripcion());
			}
		}
		return result;
	}

	public void procesarCalculoRebaja(Integer idProcesoRebaja, XSSFWorkbook workbook) throws ExcelFormatException {
		List<CellTypeExcelVO> cells = new ArrayList<CellTypeExcelVO>();
		CellTypeExcelVO fieldOne = new CellTypeExcelVO(true, FieldType.INTEGERFIELD);
		cells.add(fieldOne);
		CellTypeExcelVO fieldTwo = new CellTypeExcelVO(true);
		cells.add(fieldTwo);
		CellTypeExcelVO fieldThree = new CellTypeExcelVO(true, FieldType.INTEGERFIELD);
		cells.add(fieldThree);
		CellTypeExcelVO fieldFour = new CellTypeExcelVO(true);
		cells.add(fieldFour);
		CellTypeExcelVO fieldFive = new CellTypeExcelVO(true, FieldType.PERCENTAGEFIELD);
		cells.add(fieldFive);
		CellTypeExcelVO fieldSix = new CellTypeExcelVO(true, FieldType.PERCENTAGEFIELD);
		cells.add(fieldSix);
		CellTypeExcelVO fieldSeven = new CellTypeExcelVO(true, FieldType.PERCENTAGEFIELD);
		cells.add(fieldSeven);

		XSSFSheet worksheet = workbook.getSheetAt(0);
		RebajaExcelValidator cumplimientoExcelValidator = new RebajaExcelValidator(cells.size(), cells, true, 0, 1);
		cumplimientoExcelValidator.validateFormat(worksheet);		
		List<CumplimientoVO> items = cumplimientoExcelValidator.getItems();

		rebajaDAO.deleteComunaCumplimientoByIdRebaja(idProcesoRebaja);

		Rebaja rebaja = rebajaDAO.findRebajaById(idProcesoRebaja);
		if((items != null) && (items.size() > 0)){
			for(CumplimientoVO item : items){
				Comuna comuna = new Comuna();
				comuna.setId(item.getId_comuna());

				Mes mes = new Mes();
				mes.setIdMes(Integer.valueOf(getMesCurso(true)));

				ComunaCumplimiento comunaCumplimientoActividadGeneral = new ComunaCumplimiento();
				comunaCumplimientoActividadGeneral.setIdComuna(comuna);	
				comunaCumplimientoActividadGeneral.setRebaja(rebaja);
				comunaCumplimientoActividadGeneral.setValor(item.getValue_item1());
				comunaCumplimientoActividadGeneral.setIdTipoCumplimiento(new TipoCumplimiento(TiposCumplimientos.ACTIVIDADGENERAL.getId()));
				comunaCumplimientoActividadGeneral.setIdMes(mes);
				rebajaDAO.persistCumplimientoComunas(comunaCumplimientoActividadGeneral);

				//reglaCalculoRebajaPorComuna(comunaCum1);

				ComunaCumplimiento comunaCumplimientoContinuidadAtencionSalud = new ComunaCumplimiento();
				comunaCumplimientoContinuidadAtencionSalud.setIdComuna(comuna);
				comunaCumplimientoContinuidadAtencionSalud.setRebaja(rebaja);
				comunaCumplimientoContinuidadAtencionSalud.setValor(item.getValue_item2());
				comunaCumplimientoContinuidadAtencionSalud.setIdTipoCumplimiento(new TipoCumplimiento(TiposCumplimientos.CONTINUIDADATENCIONSALUD.getId()));
				comunaCumplimientoContinuidadAtencionSalud.setIdMes(mes);
				rebajaDAO.persistCumplimientoComunas(comunaCumplimientoContinuidadAtencionSalud);

				//reglaCalculoRebajaPorComuna(comunaCum2);

				ComunaCumplimiento comunaCumplimientoActividadGarantiaExplicitasSalud = new ComunaCumplimiento();
				comunaCumplimientoActividadGarantiaExplicitasSalud.setIdComuna(comuna);	
				comunaCumplimientoActividadGarantiaExplicitasSalud.setRebaja(rebaja);
				comunaCumplimientoActividadGarantiaExplicitasSalud.setValor(item.getValue_item3());
				comunaCumplimientoActividadGarantiaExplicitasSalud.setIdTipoCumplimiento(new TipoCumplimiento(TiposCumplimientos.ACTIVIDADGARANTIASEXPLICITASSALUD.getId()));
				comunaCumplimientoActividadGarantiaExplicitasSalud.setIdMes(mes);
				rebajaDAO.persistCumplimientoComunas(comunaCumplimientoActividadGarantiaExplicitasSalud);
				//reglaCalculoRebajaPorComuna(comunaCum3);
			}
		}
	}


	public List <PlanillaRebajaCalculadaVO>  getRebajasByComuna(Integer idRebaja , List<Integer> comunas){
		List <PlanillaRebajaCalculadaVO> planillaRebajaCalculadas = new ArrayList<PlanillaRebajaCalculadaVO>();
		for(Integer idComuna : comunas){
			PlanillaRebajaCalculadaVO planillaRebajaCalculadaVO = getRebajaByComuna( idRebaja , idComuna);
			if(planillaRebajaCalculadaVO != null){
				planillaRebajaCalculadas.add(planillaRebajaCalculadaVO);
			}
		}
		return planillaRebajaCalculadas;
	}

	public PlanillaRebajaCalculadaVO getRebajaByComuna(Integer idRebaja , Integer idComuna){
		DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast((getAnoCurso() + 1));
		AntecendentesComunaCalculado antecendentesComunaCalculado = antecedentesComunaDAO.findAntecendentesComunaCalculadoByComunaDistribucionInicialPercapitaVigenteRebaja(idComuna, distribucionInicialPercapita.getIdDistribucionInicialPercapita(), idRebaja);
		System.out.println("antecendentesComunaCalculado->" + antecendentesComunaCalculado);
		if(antecendentesComunaCalculado == null){
			return null;
		}
		PlanillaRebajaCalculadaVO planilla = new PlanillaRebajaCalculadaVO();
		planilla.setId_servicio(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId());
		planilla.setServicio(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getNombre());
		planilla.setId_comuna(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getId());
		planilla.setComuna(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getNombre());
		Integer aporteEstatalMensual =  ((antecendentesComunaCalculado.getPercapitaMes() == null) ? 0 : antecendentesComunaCalculado.getPercapitaMes().intValue());
		aporteEstatalMensual +=  ((antecendentesComunaCalculado.getDesempenoDificil() == null) ? 0 : antecendentesComunaCalculado.getDesempenoDificil().intValue());
		planilla.setAporteEstatal(aporteEstatalMensual);
		List<CumplimientoRebajaVO> cumplimientoRebajasVO = getCumplimientoByRebajaComuna(idRebaja, antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getId());
		if(cumplimientoRebajasVO != null && cumplimientoRebajasVO.size() > 0){
			Integer porcentajeRebajaCalculado = 0;
			Integer porcentajeRebajaFinal = 0;
			for(CumplimientoRebajaVO cumplimientoRebajaVO : cumplimientoRebajasVO){
				porcentajeRebajaFinal += (( cumplimientoRebajaVO.getRebajaFinal() == null) ? 0 : cumplimientoRebajaVO.getRebajaFinal().intValue());
				porcentajeRebajaCalculado += (( cumplimientoRebajaVO.getRebajaCalculada() == null) ? 0 : cumplimientoRebajaVO.getRebajaCalculada().intValue()); 
				TiposCumplimientos tipoCumplimiento = TiposCumplimientos.getById(cumplimientoRebajaVO.getTipoCumplimiento().getId());
				switch (tipoCumplimiento) {
				case ACTIVIDADGENERAL:
					planilla.setCumplimientoRebajasItem1(cumplimientoRebajaVO);
					break;
				case CONTINUIDADATENCIONSALUD:
					planilla.setCumplimientoRebajasItem2(cumplimientoRebajaVO);
					break;
				case ACTIVIDADGARANTIASEXPLICITASSALUD:
					planilla.setCumplimientoRebajasItem3(cumplimientoRebajaVO);
					break;
				default:
					break;
				}
			}
			planilla.setTotalRebajaCalculada(porcentajeRebajaCalculado);
			planilla.setTotalRebajaRebajaFinal(porcentajeRebajaFinal);
			AntecedentesComunaCalculadoRebaja antecedentesComunaCalculadoRebaja = null;
			if(antecendentesComunaCalculado.getAntecedentesComunaCalculadoRebajas() != null && antecendentesComunaCalculado.getAntecedentesComunaCalculadoRebajas().size() > 0){
				for(AntecedentesComunaCalculadoRebaja antecedentesRebaja : antecendentesComunaCalculado.getAntecedentesComunaCalculadoRebajas()){
					if(antecedentesRebaja.getRebaja().getIdRebaja().equals(idRebaja)){
						antecedentesComunaCalculadoRebaja = antecedentesRebaja;
						break;
					}
				}
			}
			planilla.setMontoRebajaMes(antecedentesComunaCalculadoRebaja.getMontoRebaja());
			planilla.setNuevoAporteEstatal(aporteEstatalMensual - antecedentesComunaCalculadoRebaja.getMontoRebaja());
		}
		return planilla;
	}

	public Integer calculaRebajaMes(int idMes, int idProceso){
		List<ComunaCumplimiento> comunaCumplimientos = rebajaDAO.getComunaCumplimientosByRebaja(idProceso);
		if((comunaCumplimientos != null) && (comunaCumplimientos.size() > 0)){
			for(ComunaCumplimiento comunaCumplimiento : comunaCumplimientos){
				reglaCalculoRebajaPorComuna(comunaCumplimiento);
			}
		}
		//calculaMontoRebaja(idProceso);
		return generarExcelRebajaCalculada(idProceso);
	}

	private void reglaCalculoRebajaPorComuna(ComunaCumplimiento comunaCumplimiento) {
		List<Cumplimiento> cumplimientos = rebajaDAO.getAllCumplimientoByTipoCumplimiento(comunaCumplimiento.getIdTipoCumplimiento().getIdTipoCumplimiento());
		if((cumplimientos != null) && (cumplimientos.size() > 0)){
			for(Cumplimiento cumplimiento : cumplimientos){
				if(cumplimiento.getTipoCumplimiento().getIdTipoCumplimiento().equals(comunaCumplimiento.getIdTipoCumplimiento().getIdTipoCumplimiento())){
					if((greaterThan(comunaCumplimiento.getValor(), cumplimiento.getPorcentajeDesde()) || equals(comunaCumplimiento.getValor(), cumplimiento.getPorcentajeDesde()))  
							&& (lessThan(comunaCumplimiento.getValor(), cumplimiento.getPorcentajeHasta()) || equals(comunaCumplimiento.getValor(), cumplimiento.getPorcentajeHasta()))){
						if(comunaCumplimiento.getRebajaCalculada() == null){
							ComunaCumplimientoRebaja rebajaCalculada = new ComunaCumplimientoRebaja();
							rebajaCalculada.setRebaja(cumplimiento.getRebaja());
							rebajaDAO.save(rebajaCalculada);
							comunaCumplimiento.setRebajaCalculada(rebajaCalculada);
						}else{
							comunaCumplimiento.getRebajaCalculada().setRebaja(cumplimiento.getRebaja());
						}
						if(comunaCumplimiento.getRebajaFinal() == null){
							ComunaCumplimientoRebaja rebajaFinal = new ComunaCumplimientoRebaja();
							rebajaFinal.setRebaja(cumplimiento.getRebaja());
							rebajaDAO.save(rebajaFinal);
							comunaCumplimiento.setRebajaFinal(rebajaFinal);
						}else{
							comunaCumplimiento.getRebajaFinal().setRebaja(cumplimiento.getRebaja());
						}
						break;
					}
				}
			}
		}
	}

	public Integer generarExcelRebajaCalculada(int idRebaja) {

		int documentId = 0;
		List<PlanillaRebajaCalculadaVO> planillaRebajasCalculadas = getAllRebajasPlanillaTotal(idRebaja);
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
		ExcelTemplate rebajaCalculadaSheetExcel = new RebajaCalculadaSheetExcel(headers, subHeaders, planillaRebajasCalculadas);
		generadorExcel.addSheet(rebajaCalculadaSheetExcel, "Hoja 1");
		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderProcesoRebaja.replace("{ANO}", String.valueOf((getAnoCurso() + 1))));
			System.out.println("response rebajaCalculadaSheetExcel --->"+response);
			Rebaja rebaja = rebajaDAO.findRebajaById(idRebaja);
			documentId = documentService.crearDocumentoRebajaCalculada(rebaja, TipoDocumentosProcesos.PLANILLARESULTADOSCALCULADOS, response.getNodeRef(), response.getFileName(), contenType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return documentId;
	}

	private Integer getAnoCurso() {
		DateFormat formatNowYear = new SimpleDateFormat("yyyy");
		Date nowDate = new Date();
		return Integer.valueOf(formatNowYear.format(nowDate)); 
	}

	public String getMesCurso(Boolean numero) {
		SimpleDateFormat dateFormat = null;
		String mesCurso = null;
		if(numero){
			dateFormat = new SimpleDateFormat("MM");
			mesCurso = dateFormat.format(new Date());
			mesCurso = ((Integer.parseInt(mesCurso) > 10) ? "10" : mesCurso);
		}else{
			dateFormat = new SimpleDateFormat("MMMM");
			mesCurso = dateFormat.format(new Date());
			mesCurso = (("noviembre".equalsIgnoreCase(mesCurso) || "diciembre".equalsIgnoreCase(mesCurso) ) ? "octubre" : mesCurso);
		}
		return mesCurso;
	}

	public Integer crearIntanciaRebaja(String username){
		System.out.println("username--> " + username);
		Usuario usuario = this.usuarioDAO.getUserByUsername(username);
		String mesCurso = getMesCurso(true);
		RebajaCorte rebajaCorte = rebajaDAO.getCorteByMes(Integer.parseInt(mesCurso));
		AnoEnCurso anoCurso = programasDAO.getAnoEnCursoById((getAnoCurso() + 1));
		return rebajaDAO.crearIntanciaRebaja(usuario, rebajaCorte, anoCurso);
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

	public Integer elaborarResolucionRebaja(Integer idProcesoRebaja) {
		Integer plantillaIdResolucionRebaja = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLARESOLUCIONREBAJA);
		if(plantillaIdResolucionRebaja == null){
			throw new RuntimeException("No se puede crear Resolución Rebaja Aporte Estatal, la plantilla no esta cargada");
		}
		try {
			List<AntecendentesComunaCalculado> antecedentesComunaCalculadoRebaja = antecedentesComunaDAO.getAntecendentesComunaCalculadoVigenteByRebaja(idProcesoRebaja);
			ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryResolucionRebajaVO = documentService.getDocumentByPlantillaId(plantillaIdResolucionRebaja);
			System.out.println("\n\n\n\n\n\n antes de documentoResolucionRebajaVO ---> referenciaDocumentoSummaryResolucionRebajaVO ---> "+referenciaDocumentoSummaryResolucionRebajaVO);
			DocumentoVO documentoResolucionRebajaVO = documentService.getDocument(referenciaDocumentoSummaryResolucionRebajaVO.getId());
			System.out.println("\n\n\n\n\n\n despues de documentoResolucionRebajaVO");
			String templateResolucionRebaja = tmpDirDoc + File.separator + documentoResolucionRebajaVO.getName();
			templateResolucionRebaja = templateResolucionRebaja.replace(" ", "");
			System.out.println("templateResolucionRebaja template-->"+templateResolucionRebaja);
			GeneradorWord generadorWordPlantillaResolucionRebaja = new GeneradorWord(templateResolucionRebaja);
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			generadorWordPlantillaResolucionRebaja.saveContent(documentoResolucionRebajaVO.getContent(), XWPFDocument.class);
			if(antecedentesComunaCalculadoRebaja != null && antecedentesComunaCalculadoRebaja.size() > 0){
				Map<String, Object> parametersResolucionRebaja = new HashMap<String, Object>();
				parametersResolucionRebaja.put("{ano}", String.valueOf((getAnoCurso() + 1)));
				Date hoy = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd 'de' MMMM 'del' yyyy");
				String date = dateFormat.format(hoy);
				parametersResolucionRebaja.put("{fechaFormato}", date);
				for(AntecendentesComunaCalculado antecedentesComunaCalculado: antecedentesComunaCalculadoRebaja){
					AntecedentesComunaCalculadoRebaja antecedenteComunaCalculadoRebaja = null;
					if(antecedentesComunaCalculado.getAntecedentesComunaCalculadoRebajas() != null && antecedentesComunaCalculado.getAntecedentesComunaCalculadoRebajas().size() > 0){
						for(AntecedentesComunaCalculadoRebaja antecedentesRebaja : antecedentesComunaCalculado.getAntecedentesComunaCalculadoRebajas()){
							if(antecedentesRebaja.getRebaja().getIdRebaja().equals(idProcesoRebaja)){
								antecedenteComunaCalculadoRebaja = antecedentesRebaja;
								break;
							}
						}
					}
					String mesHasta = ((antecedenteComunaCalculadoRebaja.getRebaja() != null && antecedenteComunaCalculadoRebaja.getRebaja().getRebajaCorte() != null && antecedenteComunaCalculadoRebaja.getRebaja().getRebajaCorte().getMesHasta() != null)? antecedenteComunaCalculadoRebaja.getRebaja().getRebajaCorte().getMesHasta().getNombre() : "");
					String mesCorte = ((antecedenteComunaCalculadoRebaja.getRebaja() != null && antecedenteComunaCalculadoRebaja.getRebaja().getRebajaCorte() != null && antecedenteComunaCalculadoRebaja.getRebaja().getRebajaCorte().getMesRebaja() != null)? antecedenteComunaCalculadoRebaja.getRebaja().getRebajaCorte().getMesRebaja().getNombre() : "");
					String filenameResolucionRebaja = tmpDirDoc + File.separator + new Date().getTime() + "_" + "ResolucionRebaja" + mesCorte + ".docx";
					System.out.println("filenameResolucionRebaja filename-->"+filenameResolucionRebaja);
					String contentTypeResolucionRebaja = mimemap.getContentType(filenameResolucionRebaja.toLowerCase());
					System.out.println("contentTypeResolucionRebaja->"+contentTypeResolucionRebaja);
					filenameResolucionRebaja = filenameResolucionRebaja.replaceAll(" ", "");
					Integer aporteEstatalMensual =  ((antecedentesComunaCalculado.getPercapitaMes() == null) ? 0 : antecedentesComunaCalculado.getPercapitaMes().intValue());
					aporteEstatalMensual +=  ((antecedentesComunaCalculado.getDesempenoDificil() == null) ? 0 : antecedentesComunaCalculado.getDesempenoDificil().intValue());
					parametersResolucionRebaja.put("{aporteMensual}", StringUtil.formatNumber(aporteEstatalMensual));
					parametersResolucionRebaja.put("{mesCorte}",mesCorte); 
					parametersResolucionRebaja.put("{numeroResolucion}", new Long(300).toString());
					parametersResolucionRebaja.put("{periodoCorte}", "enero a " + mesHasta);
					parametersResolucionRebaja.put("{comuna}", antecedentesComunaCalculado.getAntecedentesComuna().getIdComuna().getNombre());
					Integer rebajaMes = antecedenteComunaCalculadoRebaja.getMontoRebaja();
					parametersResolucionRebaja.put("{rebaja}", StringUtil.formatNumber(rebajaMes));
					Integer nuevoAporteEstatal = aporteEstatalMensual - rebajaMes;
					parametersResolucionRebaja.put("{nuevoAporte}", StringUtil.integerWithFormat(nuevoAporteEstatal));
					parametersResolucionRebaja.put("{mes}", getMesCurso(false));
					GeneradorResolucionAporteEstatal generadorWordResolucionRebaja = new GeneradorResolucionAporteEstatal(filenameResolucionRebaja, templateResolucionRebaja);
					generadorWordResolucionRebaja.replaceValues(parametersResolucionRebaja, XWPFDocument.class);
					BodyVO responseBorradorResolucionRebaja = alfrescoService.uploadDocument(new File(filenameResolucionRebaja), contentTypeResolucionRebaja, folderProcesoRebaja.replace("{ANO}", String.valueOf((getAnoCurso() + 1))));
					System.out.println("response responseBorradorResolucionRebaja --->"+responseBorradorResolucionRebaja);
					plantillaIdResolucionRebaja = documentService.createDocumentRebaja(idProcesoRebaja, antecedentesComunaCalculado.getAntecedentesComuna().getIdComuna().getId(), TipoDocumentosProcesos.RESOLUCIONREBAJA, responseBorradorResolucionRebaja.getNodeRef(), responseBorradorResolucionRebaja.getFileName(), contentTypeResolucionRebaja);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (Docx4JException e) {
			e.printStackTrace();
		}
		return plantillaIdResolucionRebaja;
	}

	private String replaceMatch(String word){
		if(word == null){
			return "";
		}
		String[] arr = word.split(" ");    

		for ( String ss : arr) {

			System.out.println(ss);
		}
		return null;
	}

	public void administrarVersionesFinalesAlfresco(Integer idProceso) {
		System.out.println("Incio RebajaService.administrarVersionesFinalesAlfresco = "+idProceso);
		System.out.println("DistribucionInicialPercapitaService administrarVersionesFinalesAlfresco eliminar todas las versiones que no sean finales");
		List<DocumentoRebaja> documentosResoluciones = rebajaDAO.getByIdRebajaTipoNotFinal(idProceso, TipoDocumentosProcesos.RESOLUCIONREBAJA);
		if(documentosResoluciones != null && documentosResoluciones.size() > 0){
			for(DocumentoRebaja documentoRebaja : documentosResoluciones){
				String key = ((documentoRebaja.getDocumento().getNodeRef() == null) ? documentoRebaja.getDocumento().getPath() : documentoRebaja.getDocumento().getNodeRef().replace("workspace://SpacesStore/", ""));
				System.out.println("key->"+key);
				alfrescoService.delete(key);
				rebajaDAO.deleteDocumentoRebaja(documentoRebaja.getIdDocumentoRebaja());
				rebajaDAO.deleteDocumento(documentoRebaja.getDocumento().getId());
			}
		}
		System.out.println("Fin RebajaService.administrarVersionesFinalesAlfresco = "+idProceso);
	}

	@Asynchronous 
	public void enviarResolucionesServicioSalud(Integer idProcesoRebaja) {
		Rebaja rebaja = rebajaDAO.findRebajaById(idProcesoRebaja);
		Integer idPlantillaCorreo = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLACORREORESOLUCIONSERVICIOSALUDREBAJA);
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
					List<ReferenciaDocumentoSummaryVO> referenciasDocumentoSummaryVO = documentService.getVersionFinalRebajaByType(idProcesoRebaja, servicio.getId_servicio(), TipoDocumentosProcesos.RESOLUCIONREBAJA);
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
						adjunto.setDescripcion("Resolución Rebaja Aporte Estatal");
						adjunto.setName(documentDocumentoResolucionVO.getName());
						adjunto.setUrl((new File(fileNameDocumentoResolucion)).toURI().toURL());
						adjuntos.add(adjunto);
						if(emailPLantilla != null && emailPLantilla.getAsunto() != null && emailPLantilla.getCuerpo() != null){
							emailService.sendMail(to, cc, cco, emailPLantilla.getAsunto(), emailPLantilla.getCuerpo().replaceAll("(\r\n|\n)", "<br />"), adjuntos);
						}else{
							emailService.sendMail(to, cc, cco , "Resolución Rebaja Aporte Estatal", "Estimados: <br /> <p> se adjunta el documento Resolucion Rebaja</p>", adjuntos);
						}
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Integer createSeguimientoRebaja(Integer idProcesoRebaja,
			TareasSeguimiento tareaSeguimiento,
			String subject, String body, String username,
			List<String> para, List<String> conCopia,
			List<String> conCopiaOculta, List<Integer> documentos) {
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
				BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummaryVO.getPath()), contenType, folderProcesoRebaja.replace("{ANO}", String.valueOf((getAnoCurso() + 1))));
				System.out.println("response upload template --->"+response);
				documentService.updateDocumentTemplate(referenciaDocumentoId, response.getNodeRef(), response.getFileName(), contenType);
			}
		}
		Integer idSeguimiento = seguimientoService.createSeguimiento(tareaSeguimiento, subject, body, from, para, conCopia, conCopiaOculta, documentosTmp);
		Seguimiento seguimiento = seguimientoDAO.getSeguimientoById(idSeguimiento);
		return rebajaDAO.createSeguimiento(idProcesoRebaja, seguimiento);
	}

	public Integer cargarPlantillaCorreo(TipoDocumentosProcesos plantilla, File file){
		Integer plantillaId = documentService.getPlantillaByType(plantilla);
		String filename = "plantillaCorreoResolucionRebaja.xml";
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		filename = tmpDir + File.separator + filename;
		String contentType = mimemap.getContentType(filename.toLowerCase());
		if(plantillaId == null){
			try {
				BodyVO response = alfrescoService.uploadDocument(file, contentType, folderTemplateRebaja);
				System.out.println("response upload template --->"+response);
				plantillaId = documentService.createTemplate(plantilla, response.getNodeRef(), response.getFileName(), contentType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			plantillaId = documentService.getDocumentoIdByPlantillaId(plantillaId);
			try {
				BodyVO response = alfrescoService.uploadDocument(file, contentType, folderTemplateRebaja);
				System.out.println("response upload template --->"+response);
				documentService.updateDocumentTemplate(plantillaId, response.getNodeRef(), response.getFileName(), contentType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return plantillaId;
	}

	public List<SeguimientoVO> getBitacora(Integer idProcesoRebaja, TareasSeguimiento tareaSeguimiento) {
		return seguimientoService.getBitacoraRebaja(idProcesoRebaja, tareaSeguimiento);
	}

	public PlanillaRebajaCalculadaVO updateMontosRebajaComuna(Integer idProcesoRebaja, PlanillaRebajaCalculadaVO planillaRebajaCalculadaVO) {
		Double porcentajeRebajaFinal = 0.0;
		Rebaja rebaja = rebajaDAO.findRebajaById(idProcesoRebaja);
		if(planillaRebajaCalculadaVO.getCumplimientoRebajasItem1() != null && planillaRebajaCalculadaVO.getCumplimientoRebajasItem1().getIdCumplimiento() != null){
			ComunaCumplimiento comunaCumplimiento = rebajaDAO.getCumplimientoById(planillaRebajaCalculadaVO.getCumplimientoRebajasItem1().getIdCumplimiento());
			if(comunaCumplimiento.getRebajaFinal() != null){
				comunaCumplimiento.getRebajaFinal().setRebaja(planillaRebajaCalculadaVO.getCumplimientoRebajasItem1().getRebajaFinal());
			}else{
				ComunaCumplimientoRebaja comunaCumplimientoRebaja = new ComunaCumplimientoRebaja();
				comunaCumplimientoRebaja.setRebaja(planillaRebajaCalculadaVO.getCumplimientoRebajasItem1().getRebajaFinal());
				rebajaDAO.save(comunaCumplimientoRebaja);
				comunaCumplimiento.setRebajaFinal(comunaCumplimientoRebaja);
			}
			porcentajeRebajaFinal+=planillaRebajaCalculadaVO.getCumplimientoRebajasItem1().getRebajaFinal();
		}
		if(planillaRebajaCalculadaVO.getCumplimientoRebajasItem2() != null){
			ComunaCumplimiento comunaCumplimiento = rebajaDAO.getCumplimientoById(planillaRebajaCalculadaVO.getCumplimientoRebajasItem2().getIdCumplimiento());
			if(comunaCumplimiento.getRebajaFinal() != null){
				comunaCumplimiento.getRebajaFinal().setRebaja(planillaRebajaCalculadaVO.getCumplimientoRebajasItem2().getRebajaFinal());
			}else{
				ComunaCumplimientoRebaja comunaCumplimientoRebaja = new ComunaCumplimientoRebaja();
				comunaCumplimientoRebaja.setRebaja(planillaRebajaCalculadaVO.getCumplimientoRebajasItem2().getRebajaFinal());
				rebajaDAO.save(comunaCumplimientoRebaja);
				comunaCumplimiento.setRebajaFinal(comunaCumplimientoRebaja);
			}
			porcentajeRebajaFinal+=planillaRebajaCalculadaVO.getCumplimientoRebajasItem2().getRebajaFinal();
		}
		if(planillaRebajaCalculadaVO.getCumplimientoRebajasItem3() != null){
			ComunaCumplimiento comunaCumplimiento = rebajaDAO.getCumplimientoById(planillaRebajaCalculadaVO.getCumplimientoRebajasItem3().getIdCumplimiento());
			if(comunaCumplimiento.getRebajaFinal() != null){
				comunaCumplimiento.getRebajaFinal().setRebaja(planillaRebajaCalculadaVO.getCumplimientoRebajasItem3().getRebajaFinal());
			}else{
				ComunaCumplimientoRebaja comunaCumplimientoRebaja = new ComunaCumplimientoRebaja();
				comunaCumplimientoRebaja.setRebaja(planillaRebajaCalculadaVO.getCumplimientoRebajasItem3().getRebajaFinal());
				rebajaDAO.save(comunaCumplimientoRebaja);
				comunaCumplimiento.setRebajaFinal(comunaCumplimientoRebaja);
			}
			porcentajeRebajaFinal+=planillaRebajaCalculadaVO.getCumplimientoRebajasItem3().getRebajaFinal();
		}
		DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast((getAnoCurso() + 1));
		List<AntecendentesComunaCalculado> antecendentesComunaCalculados = antecedentesComunaDAO.findAntecendentesComunaCalculadoByComunaServicioDistribucionInicialPercapitaVigente(planillaRebajaCalculadaVO.getId_servicio(), planillaRebajaCalculadaVO.getId_comuna(), distribucionInicialPercapita.getIdDistribucionInicialPercapita());
		AntecendentesComunaCalculado antecendentesComunaCalculado = ((antecendentesComunaCalculados != null && antecendentesComunaCalculados.size() > 0) ? antecendentesComunaCalculados.get(0): null);
		Integer aporteEstatalMensual = ((antecendentesComunaCalculado.getPercapitaMes() == null) ? 0 : antecendentesComunaCalculado.getPercapitaMes().intValue());
		System.out.println("PercapitaMes="+aporteEstatalMensual);
		aporteEstatalMensual += ((antecendentesComunaCalculado.getDesempenoDificil() == null) ? 0 : antecendentesComunaCalculado.getDesempenoDificil());
		System.out.println("DesempenoDificil="+antecendentesComunaCalculado.getDesempenoDificil());
		Integer montoRebaja = (int)Math.round(aporteEstatalMensual * (porcentajeRebajaFinal/100.0));
		System.out.println("montoRebaja-->"+montoRebaja);
		AntecedentesComunaCalculadoRebaja antecedentesComunaCalculadoRebaja = null;
		if(antecendentesComunaCalculado.getAntecedentesComunaCalculadoRebajas() != null && antecendentesComunaCalculado.getAntecedentesComunaCalculadoRebajas().size() > 0){
			for(AntecedentesComunaCalculadoRebaja antecedentesRebaja : antecendentesComunaCalculado.getAntecedentesComunaCalculadoRebajas()){
				if(antecedentesRebaja.getRebaja().getIdRebaja().equals(idProcesoRebaja)){
					antecedentesComunaCalculadoRebaja = antecedentesRebaja;
					break;
				}
			}
		}
		if(antecedentesComunaCalculadoRebaja == null){
			antecedentesComunaCalculadoRebaja = new AntecedentesComunaCalculadoRebaja();
			antecedentesComunaCalculadoRebaja.setRebaja(rebaja);
			antecedentesComunaCalculadoRebaja.setAntecedentesComunaCalculado(antecendentesComunaCalculado);
			antecedentesComunaDAO.save(antecedentesComunaCalculadoRebaja);
		}
		antecedentesComunaCalculadoRebaja.setMontoRebaja(montoRebaja);
		return getRebajaByComuna(idProcesoRebaja, planillaRebajaCalculadaVO.getId_comuna());
	}

	public Integer getPlantillaCorreo(
			TipoDocumentosProcesos plantilla) {
		Integer plantillaId = documentService.getPlantillaByType(plantilla);
		return documentService.getDocumentoIdByPlantillaId(plantillaId);
	}

	private boolean equals(double a, double b){
		return equals(a, b, EPSILON);
	}

	private boolean equals(double a, double b, double epsilon){
		return a == b ? true : Math.abs(a - b) < epsilon;
	}

	private boolean greaterThan(double a, double b){
		return greaterThan(a, b, EPSILON);
	}

	private boolean greaterThan(double a, double b, double epsilon){
		return a - b > epsilon;
	}

	private boolean lessThan(double a, double b){
		return lessThan(a, b, EPSILON);
	}

	private boolean lessThan(double a, double b, double epsilon){
		return b - a > epsilon;
	}

	public TipoCumplimientoVO getItemCumplimientoByType(TiposCumplimientos tiposCumplimiento) {
		TipoCumplimiento tipoCumplimiento = rebajaDAO.getCumplimientoByType(tiposCumplimiento);
		return new TipoCumplimientoMapper().getBasic(tipoCumplimiento);
	}

	public void moveToAlfrescoDistribucionInicialPercapita(Integer idProcesoRebaja, Integer idServicio, Integer referenciaDocumentoId, TipoDocumentosProcesos tipoDocumento, Boolean lastVersion) {
		System.out.println("Buscando referenciaDocumentoId="+referenciaDocumentoId);
		System.out.println("Buscando idProcesoRebaja="+idProcesoRebaja);
		System.out.println("Buscando idServicio="+idServicio);
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummary = documentService.getDocumentSummary(referenciaDocumentoId);
		System.out.println("Buscando referenciaDocumentoSummary="+referenciaDocumentoSummary);
		if(referenciaDocumentoSummary != null){
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contenType = mimemap.getContentType(referenciaDocumentoSummary.getPath().toLowerCase());
			BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummary.getPath()), contenType, folderProcesoRebaja.replace("{ANO}", String.valueOf((getAnoCurso() + 1 )) ));
			System.out.println("response upload template --->"+response);
			documentService.updateDocumentTemplate(referenciaDocumentoSummary.getId(), response.getNodeRef(), response.getFileName(), contenType);
			Rebaja rebaja = rebajaDAO.findRebajaById(idProcesoRebaja);
			documentService.createDocumentRebaja(rebaja, idServicio, tipoDocumento, referenciaDocumentoId, lastVersion);
		}
	}

	public String getMesCorte(Integer idProcesoRebaja) {
		Rebaja rebaja = rebajaDAO.findRebajaById(idProcesoRebaja);
		return ((rebaja != null && rebaja.getRebajaCorte() != null && rebaja.getRebajaCorte().getMesRebaja() != null)? rebaja.getRebajaCorte().getMesRebaja().getNombre() : "");
	}

	public int countVersionFinalRebajaResoluciones(Integer idProcesoRebaja, Integer idServicio) {
		List<ReferenciaDocumentoSummaryVO> versionesFinales = documentService.getVersionFinalRebajaByType(idProcesoRebaja, idServicio, TipoDocumentosProcesos.RESOLUCIONREBAJA);
		if(versionesFinales != null && versionesFinales.size() > 0){
			return versionesFinales.size();
		} 
		return 0;
	}

}
