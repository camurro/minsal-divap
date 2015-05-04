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
import minsal.divap.dao.ComunaDAO;
import minsal.divap.dao.ConveniosDAO;
import minsal.divap.dao.DistribucionInicialPercapitaDAO;
import minsal.divap.dao.EstablecimientosDAO;
import minsal.divap.dao.MesDAO;
import minsal.divap.dao.ProgramasDAO;
import minsal.divap.dao.RebajaDAO;
import minsal.divap.dao.ReliquidacionDAO;
import minsal.divap.dao.RemesasDAO;
import minsal.divap.dao.ServicioSaludDAO;
import minsal.divap.enums.Subtitulo;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.excel.GeneradorExcel;
import minsal.divap.excel.impl.ReporteEstadoSituacionPorComunaSheetExcel;
import minsal.divap.excel.impl.ReporteEstadoSituacionPorServicioSheetExcel;
import minsal.divap.excel.impl.ReporteGlosa07SheetExcel;
import minsal.divap.excel.impl.ReporteHistoricoProgramaPorComunaSheetExcel;
import minsal.divap.excel.impl.ReporteHistoricoProgramaPorEstablecimientoSheetExcel;
import minsal.divap.excel.impl.ReporteMarcoPresupuestarioComunaSheetExcel;
import minsal.divap.excel.impl.ReporteMarcoPresupuestarioEstablecimientoSheetExcel;
import minsal.divap.excel.impl.ReporteMetaDesempenoCuadro2SheetExcel;
import minsal.divap.excel.impl.ReporteMonitoreoProgramaComunaSheetExcel;
import minsal.divap.excel.impl.ReporteMonitoreoProgramaEstablecimientoSheetExcel;
import minsal.divap.excel.impl.ReportePoblacionPercapitaSheetExcel;
import minsal.divap.excel.impl.ReporteRebajaSheetExcel;
import minsal.divap.util.StringUtil;
import minsal.divap.vo.BodyVO;
import minsal.divap.vo.CellExcelVO;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ComunaSummaryVO;
import minsal.divap.vo.CumplimientoRebajaVO;
import minsal.divap.vo.EstablecimientoSummaryVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.ReporteEstadoSituacionByComunaVO;
import minsal.divap.vo.ReporteEstadoSituacionByServiciosVO;
import minsal.divap.vo.ReporteGlosaVO;
import minsal.divap.vo.ReporteHistoricoPorProgramaComunaForExcelVO;
import minsal.divap.vo.ReporteHistoricoPorProgramaComunaVO;
import minsal.divap.vo.ReporteHistoricoPorProgramaEstablecimientoForExcelVO;
import minsal.divap.vo.ReporteHistoricoPorProgramaEstablecimientoVO;
import minsal.divap.vo.ReporteHistoricoPorProgramaMarcosAnosForExcelVO;
import minsal.divap.vo.ReporteMarcoPresupuestarioComunaVO;
import minsal.divap.vo.ReporteMarcoPresupuestarioEstablecimientoVO;
import minsal.divap.vo.ReporteMonitoreoProgramaPorComunaVO;
import minsal.divap.vo.ReporteMonitoreoProgramaPorEstablecimientoVO;
import minsal.divap.vo.ReportePerCapitaVO;
import minsal.divap.vo.ReporteRebajaVO;
import minsal.divap.vo.ServiciosVO;
import minsal.divap.vo.metaDesempeno.ReporteMetaDesempenoOTAcumuladasPrincipal;
import minsal.divap.vo.metaDesempeno.ReporteMetaDesempenoOTAcumuladasSub21;
import minsal.divap.vo.metaDesempeno.ReporteMetaDesempenoOTAcumuladasSub22;
import minsal.divap.vo.metaDesempeno.ReporteMetaDesempenoOTAcumuladasSub24;
import minsal.divap.vo.metaDesempeno.ReporteMetaDesempenoOTAcumuladasSub29;
import cl.minsal.divap.model.AntecedentesComunaCalculadoRebaja;
import cl.minsal.divap.model.AntecendentesComunaCalculado;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.DistribucionInicialPercapita;
import cl.minsal.divap.model.Establecimiento;
import cl.minsal.divap.model.Mes;
import cl.minsal.divap.model.ProgramaMunicipalCoreComponente;
import cl.minsal.divap.model.RebajaCorte;
import cl.minsal.divap.model.ServicioSalud;
import cl.minsal.divap.model.TipoDocumento;

@Stateless
@LocalBean
public class ReportesServices {
	@EJB
	private DistribucionInicialPercapitaDAO distribucionInicialPercapitaDAO;
	@EJB
	private AntecedentesComunaDAO antecedentesComunaDAO;
	@EJB
	private RebajaDAO rebajaDAO;
	@EJB
	private ComunaService comunaService;
	@EJB
	private ProgramasDAO programasDAO;
	@EJB
	private ProgramasService programasService;
	@EJB
	private ComponenteService componenteService;
	@EJB
	private ConveniosDAO conveniosDAO;
	@EJB
	private ReliquidacionDAO reliquidacionDAO;
	@EJB
	private AlfrescoService alfrescoService;
	@EJB
	private DocumentService documentService;
	@EJB
	private ServicioSaludService servicioSaludService;
	@EJB
	private ServicioSaludDAO servicioSaludDAO;
	@EJB
	private ComunaDAO comunaDAO;
	@EJB
	private MesDAO mesDAO;
	@EJB
	EstablecimientosDAO establecimientosDAO;
	@EJB
	private RebajaService rebajaService;
	@EJB
	private RemesasDAO remesasDAO;

	@Resource(name = "tmpDir")
	private String tmpDir;

	@Resource(name = "folderReportes")
	private String folderReportes;

	public List<ReportePerCapitaVO> getReportePercapitaAll(Integer ano,
			String usuario) {
		List<ReportePerCapitaVO> listaReportePerCapitaVO = new ArrayList<ReportePerCapitaVO>();
		List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
		for (ServicioSalud servicioSalud : servicios) {
			for (Comuna comuna : servicioSalud.getComunas()) {
				ReportePerCapitaVO reportePerCapitaVO = new ReportePerCapitaVO();
				reportePerCapitaVO.setRegion(servicioSalud.getRegion()
						.getNombre());
				reportePerCapitaVO.setServicio(servicioSalud.getNombre());
				reportePerCapitaVO.setComuna(comuna.getNombre());

				DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO
						.findLast((ano));
				List<AntecendentesComunaCalculado> antecendentesComunaCalculados = antecedentesComunaDAO
						.findAntecendentesComunaCalculadoByComunaServicioDistribucionInicialPercapitaVigente(
								servicioSalud.getId(), comuna.getId(),
								distribucionInicialPercapita
								.getIdDistribucionInicialPercapita());
				AntecendentesComunaCalculado antecendentesComunaCalculado = ((antecendentesComunaCalculados != null && antecendentesComunaCalculados
						.size() > 0) ? antecendentesComunaCalculados.get(0)
								: null);
				System.out.println("antecendentesComunaCalculado->"
						+ antecendentesComunaCalculado);

				if (antecendentesComunaCalculado != null) {

					if (antecendentesComunaCalculado.getAntecedentesComuna()
							.getClasificacion() != null) {
						reportePerCapitaVO
						.setClasificacion(antecendentesComunaCalculado
								.getAntecedentesComuna()
								.getClasificacion().getIdTipoComuna()
								.toString());
					}

					if (antecendentesComunaCalculado
							.getValorPerCapitaComunalMes() != null) {
						reportePerCapitaVO
						.setValorPercapita(antecendentesComunaCalculado
								.getValorPerCapitaComunalMes()
								.longValue());
					} else {
						reportePerCapitaVO.setValorPercapita(0L);
					}
					if (antecendentesComunaCalculado.getPoblacion() != null) {
						reportePerCapitaVO
						.setPoblacion(antecendentesComunaCalculado
								.getPoblacion());
					}
					if (antecendentesComunaCalculado.getPoblacionMayor() != null) {
						reportePerCapitaVO
						.setPoblacion65mayor(antecendentesComunaCalculado
								.getPoblacionMayor());
					}

					if (antecendentesComunaCalculado.getPercapitaAno() != null) {
						reportePerCapitaVO
						.setPercapita(antecendentesComunaCalculado
								.getPercapitaAno());
					} else {
						reportePerCapitaVO.setPercapita(0L);
					}
					if (antecendentesComunaCalculado.getDesempenoDificil() != null) {
						reportePerCapitaVO
						.setDesempenoDificil(antecendentesComunaCalculado
								.getDesempenoDificil().longValue());
					} else {
						reportePerCapitaVO.setDesempenoDificil(0L);
					}
					reportePerCapitaVO.setAporteEstatal((reportePerCapitaVO
							.getPercapita() + reportePerCapitaVO
							.getDesempenoDificil()));
					Long rebajaIAAPS = 0L;
					for (AntecedentesComunaCalculadoRebaja antecedentesComunaCalculadoRebaja : antecendentesComunaCalculado
							.getAntecedentesComunaCalculadoRebajas()) {
						rebajaIAAPS += antecedentesComunaCalculadoRebaja
								.getMontoRebaja();
						System.out.println("rebajaIAAPS --> " + rebajaIAAPS);
					}
					reportePerCapitaVO.setRebajaIAAPS(rebajaIAAPS);

				}
				// TODO falta este valor
				Long descuentoIncentivoRetiro = 23423L;
				reportePerCapitaVO
				.setDescuentoIncentivoRetiro(descuentoIncentivoRetiro);
				reportePerCapitaVO.setAporteEstatalFinal(reportePerCapitaVO
						.getAporteEstatal()
						- reportePerCapitaVO.getRebajaIAAPS()
						- reportePerCapitaVO.getDescuentoIncentivoRetiro());

				listaReportePerCapitaVO.add(reportePerCapitaVO);

			}
		}

		return listaReportePerCapitaVO;
	}

	public List<ReportePerCapitaVO> getReportePercapitaServicioComuna(Integer idServicio, Integer ano, Integer idComuna, String usuario) {
		
		List<ReportePerCapitaVO> resultado = new ArrayList<ReportePerCapitaVO>();
		
		List<Integer> comunas = new ArrayList<Integer>();
		if(idComuna != null){
			comunas.add(idComuna);
		}else{
			List<ServicioSalud> servicios = null;
			if(idServicio == null){
				servicios = servicioSaludDAO.getServiciosOrderId();
			}else{
				servicios = new ArrayList<ServicioSalud>();
				ServicioSalud servicioSalud = servicioSaludDAO.getById(idServicio);
				servicios.add(servicioSalud);
			}
			
			for(ServicioSalud servicioSalud : servicios){
				if(servicioSalud.getComunas() != null && servicioSalud.getComunas().size() > 0){
					for(Comuna comuna : servicioSalud.getComunas()){
						if(!comuna.isAuxiliar()){
							comunas.add(comuna.getId());
						}
					}
				}
			}
		}
		DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(ano);
		if(distribucionInicialPercapita != null){
			for(Integer idComunaActual : comunas){
				Comuna comuna = comunaDAO.getComunaById(idComunaActual);
				List<AntecendentesComunaCalculado> antecendentesComunaCalculados = antecedentesComunaDAO.findAntecendentesComunaCalculadoByComunaServicioDistribucionInicialPercapitaVigente(
						comuna.getServicioSalud().getId(), comuna.getId(), distribucionInicialPercapita.getIdDistribucionInicialPercapita());
				
				AntecendentesComunaCalculado antecendentesComunaCalculado = ((antecendentesComunaCalculados != null && antecendentesComunaCalculados.size() > 0) ? antecendentesComunaCalculados.get(0) : null);
				System.out.println("antecendentesComunaCalculado->"	+ antecendentesComunaCalculado);
				if(antecendentesComunaCalculado == null){
					continue;
				}
	
				ReportePerCapitaVO reportePerCapitaVO = new ReportePerCapitaVO();
				reportePerCapitaVO.setRegion(comuna.getServicioSalud().getRegion().getNombre());
				reportePerCapitaVO.setServicio(comuna.getServicioSalud().getNombre());
				reportePerCapitaVO.setComuna(comuna.getNombre());
				
	
				if (antecendentesComunaCalculado.getAntecedentesComuna().getClasificacion() != null) {
					reportePerCapitaVO.setClasificacion(antecendentesComunaCalculado.getAntecedentesComuna().getClasificacion().getDescripcion());
				}
	
				if (antecendentesComunaCalculado.getValorPerCapitaComunalMes() != null) {
					reportePerCapitaVO.setValorPercapita(new Long(antecendentesComunaCalculado.getValorPerCapitaComunalMes().longValue() * 12));
				}else{
					reportePerCapitaVO.setValorPercapita(0L);
				}
				
				if (antecendentesComunaCalculado.getPoblacion() != null) {
					reportePerCapitaVO.setPoblacion(antecendentesComunaCalculado.getPoblacion());
				}else{
					reportePerCapitaVO.setPoblacion(0);
				}
	
				if (antecendentesComunaCalculado.getPoblacionMayor() != null) {
					reportePerCapitaVO.setPoblacion65mayor(antecendentesComunaCalculado.getPoblacionMayor());
				}else{
					reportePerCapitaVO.setPoblacion65mayor(0);
				}
	
				if (antecendentesComunaCalculado.getPercapitaAno() != null) {
					reportePerCapitaVO.setPercapita(antecendentesComunaCalculado.getPercapitaAno());
				}else{
					reportePerCapitaVO.setPercapita(0L);
				}
				
				if (antecendentesComunaCalculado.getDesempenoDificil() != null) {
					reportePerCapitaVO.setDesempenoDificil(new Long(antecendentesComunaCalculado.getDesempenoDificil() * 12));
				}else{
					reportePerCapitaVO.setDesempenoDificil(0L);
				}
	
				reportePerCapitaVO.setAporteEstatal((reportePerCapitaVO.getPercapita() + reportePerCapitaVO.getDesempenoDificil()));
				
				Long rebajaIAAPS = 0L;
				if (antecendentesComunaCalculado.getAntecedentesComunaCalculadoRebajas() != null && antecendentesComunaCalculado.getAntecedentesComunaCalculadoRebajas().size() > 0) {
					for (AntecedentesComunaCalculadoRebaja antecedentesComunaCalculadoRebaja : antecendentesComunaCalculado.getAntecedentesComunaCalculadoRebajas()) {
						rebajaIAAPS += antecedentesComunaCalculadoRebaja.getMontoRebaja();
						System.out.println("rebajaIAAPS --> " + rebajaIAAPS);
					}
				}
				
				reportePerCapitaVO.setRebajaIAAPS(rebajaIAAPS);
	
				// TODO falta este valor
				Long descuentoIncentivoRetiro = 234234L;
	
				reportePerCapitaVO.setDescuentoIncentivoRetiro(descuentoIncentivoRetiro);
				reportePerCapitaVO.setAporteEstatalFinal(reportePerCapitaVO.getAporteEstatal() - reportePerCapitaVO.getRebajaIAAPS() - reportePerCapitaVO.getDescuentoIncentivoRetiro());
				resultado.add(reportePerCapitaVO);
			}
		}
		return resultado;
	}

	public Integer generarPlanillaPoblacionPercapita(String usuario, Integer ano) {
		Integer planillaTrabajoId = null;

		List<ServiciosVO> servicios = servicioSaludService.getServiciosOrderId();
		List<CellExcelVO> header = new ArrayList<CellExcelVO>();
		header.add((new CellExcelVO("REGION", 1, 1)));
		header.add((new CellExcelVO("SERVICIO", 1, 1)));
		header.add((new CellExcelVO("COMUNA", 1, 1)));
		header.add((new CellExcelVO("CLASIFICACIÓN", 1, 1)));
		header.add((new CellExcelVO("VALOR PER CÁPITA", 1, 1)));
		header.add((new CellExcelVO("POBLACIÓN", 1, 1)));
		header.add((new CellExcelVO("POBLACIÓN MAYOR 65 Y MÁS AÑOS", 1, 1)));
		header.add((new CellExcelVO("PER CÁPITA", 1, 1)));
		header.add((new CellExcelVO("DESEMPEÑO DIFICIL", 1, 1)));
		header.add((new CellExcelVO("APORTE ESTATAL", 1, 1)));
		header.add((new CellExcelVO("REBAJA IAAPS", 1, 1)));
		header.add((new CellExcelVO("DESCUENTO POR INCENTIVO AL RETIRO", 1, 1)));
		header.add((new CellExcelVO("APORTE ESTATAL FINAL", 1, 1)));

		List<ReportePerCapitaVO> reportePerCapitaAnoActualMenos3 = this
				.getReportePercapitaAll((ano - 3), usuario);
		List<ReportePerCapitaVO> reportePerCapitaAnoActualMenos2 = this
				.getReportePercapitaAll((ano - 2), usuario);
		List<ReportePerCapitaVO> reportePerCapitaAnoActualMenos1 = this
				.getReportePercapitaAll((ano - 1), usuario);
		List<ReportePerCapitaVO> reportePerCapitaAnoActual = this
				.getReportePercapitaAll(ano, usuario);

		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator
				+ "Planilla Poblacion per Capita.xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());

		GeneradorExcel generadorExcel = new GeneradorExcel(filename);

		ReportePoblacionPercapitaSheetExcel reportePoblacionPercapitaSheetExcelAnoActualMenos3 = new ReportePoblacionPercapitaSheetExcel(
				header, null);
		reportePoblacionPercapitaSheetExcelAnoActualMenos3
		.setItems(reportePerCapitaAnoActualMenos3);

		ReportePoblacionPercapitaSheetExcel reportePoblacionPercapitaSheetExcelAnoActualMenos2 = new ReportePoblacionPercapitaSheetExcel(
				header, null);
		reportePoblacionPercapitaSheetExcelAnoActualMenos2
		.setItems(reportePerCapitaAnoActualMenos2);

		ReportePoblacionPercapitaSheetExcel reportePoblacionPercapitaSheetExcelAnoActualMenos1 = new ReportePoblacionPercapitaSheetExcel(
				header, null);
		reportePoblacionPercapitaSheetExcelAnoActualMenos1
		.setItems(reportePerCapitaAnoActualMenos1);

		ReportePoblacionPercapitaSheetExcel reportePoblacionPercapitaSheetExcelAnoActual = new ReportePoblacionPercapitaSheetExcel(
				header, null);
		reportePoblacionPercapitaSheetExcelAnoActual
		.setItems(reportePerCapitaAnoActual);

		generadorExcel.addSheet(reportePoblacionPercapitaSheetExcelAnoActual, ano.toString());
		generadorExcel.addSheet(reportePoblacionPercapitaSheetExcelAnoActualMenos1, String.valueOf((ano	- 1)));
		generadorExcel.addSheet(reportePoblacionPercapitaSheetExcelAnoActualMenos2, String.valueOf((ano	- 2)));
		generadorExcel.addSheet(reportePoblacionPercapitaSheetExcelAnoActualMenos3, String.valueOf((ano	- 3)));

		System.out.println("folderPercapita --> " + folderReportes);

		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderReportes.replace("{ANO}", ano.toString()));
			TipoDocumento tipoDocumento = new TipoDocumento(TipoDocumentosProcesos.REPORTEPOBLACIONPERCAPITA.getId());
			planillaTrabajoId = documentService.createDocumentReportes(tipoDocumento, response.getNodeRef(), response.getFileName(), contenType, ano, Integer.parseInt(getMesCurso(true)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return planillaTrabajoId;
	}

	public Integer getAnoCurso() {
		DateFormat formatNowYear = new SimpleDateFormat("yyyy");
		Date nowDate = new Date();
		return Integer.valueOf(formatNowYear.format(nowDate));
	}

	public List<ReporteMarcoPresupuestarioComunaVO> getReporteMarcoPorComunaFiltroServicioComuna(Integer idServicio, Integer idComuna, Subtitulo subtitulo, String usuario, Integer ano) {

		List<ReporteMarcoPresupuestarioComunaVO> resultado = new ArrayList<ReporteMarcoPresupuestarioComunaVO>();
		List<ProgramaVO> programasVO = programasService.getProgramasByAnoSubtitulo(ano, subtitulo);
		
		List<Comuna> comunas = null;
		if(idComuna != null && idComuna != 0){
			comunas = new ArrayList<Comuna>();
			Comuna comuna = comunaDAO.getComunaById(idComuna);
			comunas.add(comuna);
		}else{
			if(idServicio != null){
				comunas = comunaDAO.getComunasByServicio(idServicio);
			}else{
				comunas = new ArrayList<Comuna>();
				List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
				for(ServicioSalud servicioSalud : servicios){
					if(servicioSalud.getComunas() != null && servicioSalud.getComunas().size() > 0){
						for(Comuna comunaServicio : servicioSalud.getComunas()){
							comunas.add(comunaServicio);
						}
					}
				}
			}
		}
		
		if(comunas != null && comunas.size() > 0){
			for(Comuna comuna : comunas){
				for (ProgramaVO programa : programasVO) {
					List<ComponentesVO> componentes = programasService.getComponenteByProgramaSubtitulos(programa.getId(), subtitulo);
					for (ComponentesVO componenteVO : componentes) {
						ReporteMarcoPresupuestarioComunaVO reporteMarcoPresupuestarioComunaVO = new ReporteMarcoPresupuestarioComunaVO();
						reporteMarcoPresupuestarioComunaVO.setServicio(comuna.getServicioSalud().getNombre());
						reporteMarcoPresupuestarioComunaVO.setPrograma(programa.getNombre());
						reporteMarcoPresupuestarioComunaVO.setComponente(componenteVO.getNombre());
						reporteMarcoPresupuestarioComunaVO.setComuna(comuna.getNombre());
		
						DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast((ano));
						List<AntecendentesComunaCalculado> antecendentesComunaCalculados = antecedentesComunaDAO.findAntecendentesComunaCalculadoByComunaServicioDistribucionInicialPercapitaVigente(
								comuna.getServicioSalud().getId(), comuna.getId(), distribucionInicialPercapita.getIdDistribucionInicialPercapita());
		
						Long tarifaAnoActual = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(idComuna, programa.getIdProgramaAno(), componenteVO.getId(), subtitulo.getId());
		
						Integer mesActual = Integer.parseInt(getMesCurso(true));
						reporteMarcoPresupuestarioComunaVO.setMarco(tarifaAnoActual);
		
						reporteMarcoPresupuestarioComunaVO.setConvenios(0L);
		
						Long totalRemesasAcumuladasMesActual = remesasDAO.getRemesasPagadasComunaProgramaSubtitulo(programa.getIdProgramaAno(), subtitulo.getId(), comuna.getId(), mesActual);
						if (totalRemesasAcumuladasMesActual > 0L) {
							System.out.println("Marco --> "	+ reporteMarcoPresupuestarioComunaVO.getMarco());
							System.out.println("totalRemesasAcumuladasMesActual -->  " + totalRemesasAcumuladasMesActual);
		
							Double porcentajeRemesasPagadas = (totalRemesasAcumuladasMesActual * 100.0) / reporteMarcoPresupuestarioComunaVO.getMarco();
							porcentajeRemesasPagadas = porcentajeRemesasPagadas / 100.0;
							System.out.println("porcentajeRemesasPagadas --> " + porcentajeRemesasPagadas);
		
							reporteMarcoPresupuestarioComunaVO.setRemesasAcumuladas(totalRemesasAcumuladasMesActual);
							reporteMarcoPresupuestarioComunaVO.setPorcentajeCuotaTransferida(porcentajeRemesasPagadas);
						} else {
							reporteMarcoPresupuestarioComunaVO.setRemesasAcumuladas(0L);
							reporteMarcoPresupuestarioComunaVO.setPorcentajeCuotaTransferida(0.0);
						}
		
						Comuna comunaAuxiliar = comunaDAO.getComunaServicioAuxiliar(idServicio);
						System.out.println("comunaAuxiliar --> " + ((comunaAuxiliar == null) ? null : comunaAuxiliar.getNombre()));
		
						Long tarifaComunaAux = 0L;
						if(comunaAuxiliar != null){
							tarifaComunaAux = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comunaAuxiliar.getId(), programa.getIdProgramaAno(), componenteVO.getId(), subtitulo.getId());
						}
						System.out.println("tarifaComunaAux --> "+tarifaComunaAux);
						if (tarifaComunaAux == 0L) {
							reporteMarcoPresupuestarioComunaVO.setObservacion("");
						} else {
							reporteMarcoPresupuestarioComunaVO.setObservacion("Existen recursos no distribuidos en este programa");
						}
						resultado.add(reporteMarcoPresupuestarioComunaVO);
					}
				}
			}
		}

		return resultado;
	}

	public List<ReporteMarcoPresupuestarioComunaVO> getReporteMarcoPorComunaFiltroServicioComunaPrograma(Integer idProgramaAno, Integer idServicio, Subtitulo subtitulo, Integer idComuna, String usuario) {

		System.out.println("entra al metodo getReporteMarcoPorComunaFiltroServicioComunaPrograma con subtitulo --> " + subtitulo.getNombre());

		List<ReporteMarcoPresupuestarioComunaVO> resultado = new ArrayList<ReporteMarcoPresupuestarioComunaVO>();

		ProgramaVO programa = programasService.getProgramaAno(idProgramaAno);

		ServicioSalud servicio = servicioSaludDAO.getServicioSaludById(idServicio);
		Comuna comuna = comunaService.getComunaById(idComuna);

		List<ComponentesVO> componentes = programasService.getComponenteByProgramaSubtitulos(programa.getId(), subtitulo);
		for (ComponentesVO componenteVO : componentes) {
			ReporteMarcoPresupuestarioComunaVO reporteMarcoPresupuestarioComunaVO = new ReporteMarcoPresupuestarioComunaVO();
			reporteMarcoPresupuestarioComunaVO.setServicio(servicio.getNombre());
			reporteMarcoPresupuestarioComunaVO.setPrograma(programa.getNombre());
			reporteMarcoPresupuestarioComunaVO.setComponente(componenteVO.getNombre());
			reporteMarcoPresupuestarioComunaVO.setComuna(comuna.getNombre());

			Long tarifaAnoActual = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(idComuna, programa.getIdProgramaAno(), componenteVO.getId(), subtitulo.getId());

			Integer mesActual = Integer.parseInt(getMesCurso(true));
			reporteMarcoPresupuestarioComunaVO.setMarco(tarifaAnoActual);

			// Long conveniosTEMP = (long) convenioComunaComponente.getMonto();
			reporteMarcoPresupuestarioComunaVO.setConvenios(0L);

			Long totalRemesasAcumuladasMesActual = remesasDAO.getRemesasPagadasComunaProgramaSubtitulo(idProgramaAno, subtitulo.getId(), comuna.getId(), mesActual);
			if (totalRemesasAcumuladasMesActual > 0L) {
				System.out.println("Marco --> "	+ reporteMarcoPresupuestarioComunaVO.getMarco());
				System.out.println("totalRemesasAcumuladasMesActual --> " + totalRemesasAcumuladasMesActual);

				Double porcentajeRemesasPagadas = (totalRemesasAcumuladasMesActual * 100.0)	/ reporteMarcoPresupuestarioComunaVO.getMarco();
				porcentajeRemesasPagadas = porcentajeRemesasPagadas / 100.0;
				System.out.println("porcentajeRemesasPagadas --> "	+ porcentajeRemesasPagadas);

				reporteMarcoPresupuestarioComunaVO.setRemesasAcumuladas(totalRemesasAcumuladasMesActual);
				reporteMarcoPresupuestarioComunaVO.setPorcentajeCuotaTransferida(porcentajeRemesasPagadas);
			} else {
				reporteMarcoPresupuestarioComunaVO.setRemesasAcumuladas(0L);
				reporteMarcoPresupuestarioComunaVO.setPorcentajeCuotaTransferida(0.0);
			}

			Comuna comunaAuxiliar = comunaDAO.getComunaServicioAuxiliar(idServicio);
			System.out.println("comunaAuxiliar --> " + comunaAuxiliar.getNombre());

			Long tarifaComunaAux = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comunaAuxiliar.getId(), idProgramaAno, componenteVO.getId(), subtitulo.getId());

			System.out.println("tarifaComunaAux --> "+tarifaComunaAux);

			if (tarifaComunaAux == 0L) {
				reporteMarcoPresupuestarioComunaVO.setObservacion("");
			} else {
				reporteMarcoPresupuestarioComunaVO.setObservacion("Existen recursos no distribuidos en este programa");
			}
			resultado.add(reporteMarcoPresupuestarioComunaVO);
		}
		return resultado;
	}

	public List<ReporteMarcoPresupuestarioEstablecimientoVO> getReporteMarcoPorServicioEstablecimiento(Integer idServicio, Integer idEstablecimiento, Subtitulo subtitulo, Integer ano) {

		List<ReporteMarcoPresupuestarioEstablecimientoVO> resultado = new ArrayList<ReporteMarcoPresupuestarioEstablecimientoVO>();
		List <ProgramaVO> programas = programasService.getProgramasByAnoSubtitulo(ano, subtitulo);

		List<Establecimiento> establecimientos = null;
		if(idEstablecimiento != null && idEstablecimiento != 0){
			establecimientos = new ArrayList<Establecimiento>();
			Establecimiento establecimiento = establecimientosDAO.getEstablecimientoById(idEstablecimiento);
			establecimientos.add(establecimiento);
		}else{
			if(idServicio != null){
				establecimientos = establecimientosDAO.getEstablecimientosByServicio(idServicio);
			}else{
				establecimientos = new ArrayList<Establecimiento>();
				List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
				for(ServicioSalud servicioSalud : servicios){
					if(servicioSalud.getEstablecimientos() != null && servicioSalud.getEstablecimientos().size() > 0){
						for(Establecimiento establecimientoServicio : servicioSalud.getEstablecimientos()){
							establecimientos.add(establecimientoServicio);
						}
					}
				}
			}
		}
		if(establecimientos != null && establecimientos.size() > 0){
			for(ProgramaVO programa : programas){
				List<ComponentesVO> componentes = programasService.getComponenteByProgramaSubtitulos(programa.getId(), subtitulo);
				for (ComponentesVO componenteVO : componentes) {
					for(Establecimiento establecimiento : establecimientos){
						ReporteMarcoPresupuestarioEstablecimientoVO reporteMarcoPresupuestarioEstablecimientoVO = new ReporteMarcoPresupuestarioEstablecimientoVO();
						reporteMarcoPresupuestarioEstablecimientoVO.setServicio(establecimiento.getServicioSalud().getNombre());
						reporteMarcoPresupuestarioEstablecimientoVO.setPrograma(programa.getNombre());
						reporteMarcoPresupuestarioEstablecimientoVO.setComponente(componenteVO.getNombre());
						reporteMarcoPresupuestarioEstablecimientoVO.setEstablecimiento(establecimiento.getNombre());

						Long marcoAnoActual = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), programa.getIdProgramaAno(), componenteVO.getId(), subtitulo.getId());
						System.out.println("marcoAnoActual --> " + marcoAnoActual);

						reporteMarcoPresupuestarioEstablecimientoVO.setMarco(marcoAnoActual);
						reporteMarcoPresupuestarioEstablecimientoVO.setConvenios(0L);
						reporteMarcoPresupuestarioEstablecimientoVO.setRemesasAcumuladas(0L);

						Integer mesActual = Integer.parseInt(getMesCurso(true));
						Long totalRemesasAcumuladasMesActual = 0L;

						totalRemesasAcumuladasMesActual = remesasDAO.getRemesasPagadasEstablecimientoProgramaSubtitulo(programa.getIdProgramaAno(), subtitulo.getId(),
								establecimiento.getId(), mesActual);
						System.out.println("totalRemesasAcumuladasMesActual --> "+ totalRemesasAcumuladasMesActual);

						reporteMarcoPresupuestarioEstablecimientoVO.setRemesasAcumuladas(totalRemesasAcumuladasMesActual);

						Double porcentajeRemesa = 0.0;

						if(marcoAnoActual != 0){
							porcentajeRemesa = ((totalRemesasAcumuladasMesActual * 100.0) / marcoAnoActual) / 100;
						}

						reporteMarcoPresupuestarioEstablecimientoVO
						.setPorcentajeCuotaTransferida(porcentajeRemesa);

						Establecimiento auxiliar = establecimientosDAO.getEstablecimientoServicioAuxiliar(idServicio);
						System.out.println("establecimiento auxiliar --> "+ ((auxiliar == null) ? null : auxiliar.getNombre()));

						Long tarifaEstablecimientoAuxiliar = 0L;
						if(auxiliar != null){
							tarifaEstablecimientoAuxiliar = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(auxiliar.getId(), programa.getIdProgramaAno(), componenteVO.getId(), subtitulo.getId());
						}

						if (tarifaEstablecimientoAuxiliar == 0) {
							System.out.println("el establecimiento auxiliar no tiene recursos asignados");
							reporteMarcoPresupuestarioEstablecimientoVO.setObservacion("");
						} else {
							reporteMarcoPresupuestarioEstablecimientoVO.setObservacion("Existen recursos no distribuidos en este programa");
						}

						resultado.add(reporteMarcoPresupuestarioEstablecimientoVO);
					}
				}
			}
		}
		return resultado;
	}

	public List<ReporteMarcoPresupuestarioEstablecimientoVO> getReporteMarcoPorServicioFiltroEstablecimiento(
			Integer idProgramaAno, Integer idServicio,
			Integer idEstablecimiento, Subtitulo subtitulo) {

		System.out
		.println("entra al metodo getReporteMarcoPorServicioFiltroEstablecimiento con subtitulo --> "
				+ subtitulo.getNombre());

		List<ReporteMarcoPresupuestarioEstablecimientoVO> resultado = new ArrayList<ReporteMarcoPresupuestarioEstablecimientoVO>();

		ProgramaVO programa = programasService.getProgramaAno(idProgramaAno);

		ServicioSalud servicio = servicioSaludDAO
				.getServicioSaludById(idServicio);

		Establecimiento establecimiento = establecimientosDAO
				.getEstablecimientoById(idEstablecimiento);

		List<ComponentesVO> componentes = programasService
				.getComponenteByProgramaSubtitulos(programa.getId(), subtitulo);
		for (ComponentesVO componenteVO : componentes) {

			ReporteMarcoPresupuestarioEstablecimientoVO reporteMarcoPresupuestarioEstablecimientoVO = new ReporteMarcoPresupuestarioEstablecimientoVO();

			reporteMarcoPresupuestarioEstablecimientoVO.setServicio(servicio
					.getNombre());
			reporteMarcoPresupuestarioEstablecimientoVO.setPrograma(programa
					.getNombre());
			reporteMarcoPresupuestarioEstablecimientoVO
			.setComponente(componenteVO.getNombre());
			reporteMarcoPresupuestarioEstablecimientoVO
			.setEstablecimiento(establecimiento.getNombre());

			Long marcoAnoActual = programasDAO
					.getMPEstablecimientoProgramaAnoComponenteSubtitulo(
							establecimiento.getId(), idProgramaAno,
							componenteVO.getId(), subtitulo.getId());
			System.out.println("marcoAnoActual --> " + marcoAnoActual);

			reporteMarcoPresupuestarioEstablecimientoVO
			.setMarco(marcoAnoActual);

			reporteMarcoPresupuestarioEstablecimientoVO.setConvenios(0L);

			// TODO cambiar esto por la remesa. Cargar datos en la tabla 1ro
			reporteMarcoPresupuestarioEstablecimientoVO
			.setRemesasAcumuladas(0L);

			Integer mesActual = Integer.parseInt(getMesCurso(true));
			Long totalRemesasAcumuladasMesActual = 0L;

			totalRemesasAcumuladasMesActual = remesasDAO
					.getRemesasPagadasEstablecimientoProgramaSubtitulo(
							idProgramaAno, subtitulo.getId(),
							establecimiento.getId(), mesActual);
			System.out.println("totalRemesasAcumuladasMesActual --> "
					+ totalRemesasAcumuladasMesActual);

			reporteMarcoPresupuestarioEstablecimientoVO
			.setRemesasAcumuladas(totalRemesasAcumuladasMesActual);

			Double porcentajeRemesa = 0.0;

			if(marcoAnoActual != 0){
				porcentajeRemesa = ((totalRemesasAcumuladasMesActual * 100.0) / marcoAnoActual) / 100;
			}

			reporteMarcoPresupuestarioEstablecimientoVO
			.setPorcentajeCuotaTransferida(porcentajeRemesa);

			Establecimiento auxiliar = establecimientosDAO
					.getEstablecimientoServicioAuxiliar(idServicio);
			System.out.println("establecimiento auxiliar --> "
					+ auxiliar.getNombre());

			Long tarifaEstablecimientoAuxiliar = programasDAO
					.getMPEstablecimientoProgramaAnoComponenteSubtitulo(
							auxiliar.getId(), idProgramaAno,
							componenteVO.getId(), subtitulo.getId());

			if (tarifaEstablecimientoAuxiliar == 0) {
				System.out
				.println("el establecimiento auxiliar no tiene recursos asignados");
				reporteMarcoPresupuestarioEstablecimientoVO.setObservacion("");
			} else {
				reporteMarcoPresupuestarioEstablecimientoVO
				.setObservacion("Existen recursos no distribuidos en este programa");
			}

			resultado.add(reporteMarcoPresupuestarioEstablecimientoVO);

		}

		return resultado;
	}

	public String getMesCurso(Boolean numero) {
		SimpleDateFormat dateFormat = null;
		String mesCurso = null;
		if (numero) {
			dateFormat = new SimpleDateFormat("MM");
			mesCurso = dateFormat.format(new Date());
		} else {
			dateFormat = new SimpleDateFormat("MMMM");
			mesCurso = dateFormat.format(new Date());
		}
		return mesCurso;
	}

	public List<ReporteGlosaVO> getReporteGlosa(String usuario, Integer ano) {
		List<ReporteGlosaVO> resultado = new ArrayList<ReporteGlosaVO>();
		List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();

		for (ServicioSalud servicio : servicios) {
			for (Comuna comuna : servicio.getComunas()) {
				ReporteGlosaVO reporteGlosaVO = new ReporteGlosaVO();
				reporteGlosaVO.setRegion(servicio.getRegion().getNombre());
				reporteGlosaVO.setServicio(servicio.getNombre());
				reporteGlosaVO.setComuna(comuna.getNombre());

				DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO
						.findLast((ano));
				List<AntecendentesComunaCalculado> antecendentesComunaCalculados = antecedentesComunaDAO
						.findAntecendentesComunaCalculadoByComunaServicioDistribucionInicialPercapitaVigente(
								servicio.getId(), comuna.getId(),
								distribucionInicialPercapita
								.getIdDistribucionInicialPercapita());
				AntecendentesComunaCalculado antecendentesComunaCalculado = ((antecendentesComunaCalculados != null && antecendentesComunaCalculados
						.size() > 0) ? antecendentesComunaCalculados.get(0)
								: null);
				System.out.println("antecendentesComunaCalculado->"
						+ antecendentesComunaCalculado);

				Long tarifa = 0L;
				Long totalRemesasAcumuladasMesActual = 0L;
				if (antecendentesComunaCalculado != null) {
					reporteGlosaVO
					.setArt49perCapita(((antecendentesComunaCalculado
							.getPercapitaMes() == null) ? 0L
									: antecendentesComunaCalculado
									.getPercapitaMes()));
				}
				List<ProgramaMunicipalCoreComponente> programaMunicipalCoreComponentes = programasDAO
						.getByIdComuna(comuna.getId(), ano);
				if (programaMunicipalCoreComponentes != null
						&& programaMunicipalCoreComponentes.size() > 0) {
					for (ProgramaMunicipalCoreComponente programaMunicipalCoreComponente : programaMunicipalCoreComponentes) {
						if (programaMunicipalCoreComponente == null) {
							continue;
						}
						tarifa += programaMunicipalCoreComponente.getTarifa();
					}
				}
				reporteGlosaVO.setArt56reforzamientoMunicipal(tarifa);
				Integer mesActual = Integer.parseInt(getMesCurso(true));
				for (ProgramaVO programaVO : programasService
						.getProgramasByUserAno(usuario, ano)) {
					totalRemesasAcumuladasMesActual += remesasDAO
							.getRemesasPagadasComunaPrograma(
									programaVO.getIdProgramaAno(),
									comuna.getId(), mesActual);
				}
				reporteGlosaVO
				.setTotalRemesasEneroMarzo(totalRemesasAcumuladasMesActual);
				resultado.add(reporteGlosaVO);
			}
		}
		return resultado;
	}

	public List<ReporteGlosaVO> getReporteGlosaPorServicio(Integer idServicio, String usuario, Integer ano) {
		List<ReporteGlosaVO> resultado = new ArrayList<ReporteGlosaVO>();
		List<ServicioSalud> servicios = null;
		
		if(idServicio == null){
			servicios = servicioSaludDAO.getServiciosOrderId();
		}else{
			servicios = new ArrayList<ServicioSalud>();
			ServicioSalud servicio = servicioSaludDAO.getServicioSaludById(idServicio);
			servicios.add(servicio);
		}
		
		for (ServicioSalud servicioSalud : servicios) {
			if(servicioSalud.getComunas() != null && servicioSalud.getComunas().size() > 0){
				for (Comuna comuna : servicioSalud.getComunas()) {
					ReporteGlosaVO reporteGlosaVO = new ReporteGlosaVO();
					reporteGlosaVO.setRegion(servicioSalud.getRegion().getNombre());
					reporteGlosaVO.setServicio(servicioSalud.getNombre());
					reporteGlosaVO.setComuna(comuna.getNombre());
		
					DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(ano);
					List<AntecendentesComunaCalculado> antecendentesComunaCalculados = antecedentesComunaDAO.findAntecendentesComunaCalculadoByComunaServicioDistribucionInicialPercapitaVigente(servicioSalud.getId(),
							comuna.getId(), distribucionInicialPercapita.getIdDistribucionInicialPercapita());
					AntecendentesComunaCalculado antecendentesComunaCalculado = ((antecendentesComunaCalculados != null && antecendentesComunaCalculados.size() > 0) ? antecendentesComunaCalculados.get(0) : null);
					System.out.println("antecendentesComunaCalculado->" + antecendentesComunaCalculado);
		
					if (antecendentesComunaCalculado != null) {
						reporteGlosaVO.setArt49perCapita(((antecendentesComunaCalculado.getPercapitaMes() == null) ? 0L	: antecendentesComunaCalculado.getPercapitaMes()));
					}
		
					Long tarifa = 0L;
					Long totalRemesasAcumuladasMesActual = 0L;
					List<ProgramaMunicipalCoreComponente> programaMunicipalCoreComponentes = programasDAO.getByIdComuna(comuna.getId(), ano);
					if (programaMunicipalCoreComponentes != null && programaMunicipalCoreComponentes.size() > 0) {
						for (ProgramaMunicipalCoreComponente programaMunicipalCoreComponente : programaMunicipalCoreComponentes) {
							if (programaMunicipalCoreComponente == null) {
								continue;
							}
							tarifa += programaMunicipalCoreComponente.getTarifa();
						}
					}
					reporteGlosaVO.setArt56reforzamientoMunicipal(tarifa);
					Integer mesActual = Integer.parseInt(getMesCurso(true));
					for (ProgramaVO programaVO : programasService.getProgramasByUserAno(usuario, ano)) {
						totalRemesasAcumuladasMesActual += remesasDAO.getRemesasPagadasComunaPrograma(programaVO.getIdProgramaAno(), comuna.getId(), mesActual);
					}
					reporteGlosaVO.setTotalRemesasEneroMarzo(totalRemesasAcumuladasMesActual);
					resultado.add(reporteGlosaVO);
				}
			}
		}
		return resultado;

	}

	public List<ReporteHistoricoPorProgramaEstablecimientoVO> getReporteHistoricoEstablecimientoPorProgramaVOFiltroServicioEstablecimiento(
			Integer idProgramaAno, Integer idServicio, Integer idEstablecimiento, Subtitulo subtitulo, Integer ano) {

		System.out.println("idProgramaAno --> " + idProgramaAno	+ "  subtitulo --> " + subtitulo.getNombre());
		
		List<Establecimiento> estableciminetos = null;
		if(idEstablecimiento != null && idEstablecimiento != 0){
			estableciminetos = new ArrayList<Establecimiento>();
			Establecimiento establecimiento = establecimientosDAO.getEstablecimientoById(idEstablecimiento);
			estableciminetos.add(establecimiento);
		}else{
			if(idServicio != null){
				estableciminetos = establecimientosDAO.getEstablecimientosByServicio(idServicio);
			}else{
				estableciminetos = new ArrayList<Establecimiento>();
				List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
				for(ServicioSalud servicioSalud : servicios){
					if(servicioSalud.getEstablecimientos() != null && servicioSalud.getEstablecimientos().size() > 0){
						for(Establecimiento establecimientoServicio : servicioSalud.getEstablecimientos()){
							estableciminetos.add(establecimientoServicio);
						}
					}
				}
			}
		}

		ProgramaVO programaAnoActual = programasService.getProgramaAno(idProgramaAno); // 2015
		List<ComponentesVO> componentes = programasService.getComponenteByProgramaSubtitulos(programaAnoActual.getId(), subtitulo);

		Integer idProgramaAnoActualMenos1 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(), (ano - 1)); // 2014
		Integer idProgramaAnoActualMenos2 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(), (ano - 2)); // 2013
		Integer idProgramaAnoActualMenos3 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(), (ano - 3)); // 2012
		Integer idProgramaAnoActualMenos4 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(), (ano - 4)); // 2011
		Integer idProgramaAnoActualMenos5 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(), (ano - 5)); // 2010
		Integer idProgramaAnoActualMenos6 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(), (ano - 6)); // 2009
		Integer idProgramaAnoActualMenos7 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(),	(ano - 7)); // 2008
		Integer idProgramaAnoActualMenos8 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(),	(ano - 8)); // 2007

		List<ReporteHistoricoPorProgramaEstablecimientoVO> resultado = new ArrayList<ReporteHistoricoPorProgramaEstablecimientoVO>();


		if(estableciminetos != null && estableciminetos.size() > 0){
			for(Establecimiento establecimiento : estableciminetos){
				
		
				Long marcoAnoActual = 0L;
		
				Long marcoAnoActualMenos1 = 0L;
				Long marcoAnoActualMenos2 = 0L;
				Long marcoAnoActualMenos3 = 0L;
				Long marcoAnoActualMenos4 = 0L;
				Long marcoAnoActualMenos5 = 0L;
				Long marcoAnoActualMenos6 = 0L;
				Long marcoAnoActualMenos7 = 0L;
				Long marcoAnoActualMenos8 = 0L;
		
				ReporteHistoricoPorProgramaEstablecimientoVO reporteHistoricoPorProgramaEstablecimientoVO = new ReporteHistoricoPorProgramaEstablecimientoVO();
		
				reporteHistoricoPorProgramaEstablecimientoVO.setRegion(establecimiento.getServicioSalud().getRegion().getNombre());
				reporteHistoricoPorProgramaEstablecimientoVO.setServicio(establecimiento.getServicioSalud().getNombre());
				reporteHistoricoPorProgramaEstablecimientoVO.setEstablecimiento(establecimiento.getNombre());
				reporteHistoricoPorProgramaEstablecimientoVO.setPrograma(programaAnoActual.getNombre());
		
				for (ComponentesVO componente : componentes) {
		
					marcoAnoActual += programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), programaAnoActual.getIdProgramaAno(),componente.getId(), subtitulo.getId());
					marcoAnoActualMenos1 += programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAnoActualMenos1, componente.getId(), subtitulo.getId());
					marcoAnoActualMenos2 += programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAnoActualMenos2,	componente.getId(), subtitulo.getId());
					marcoAnoActualMenos3 += programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAnoActualMenos3,	componente.getId(), subtitulo.getId());
					marcoAnoActualMenos4 += programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAnoActualMenos4,	componente.getId(), subtitulo.getId());
					marcoAnoActualMenos5 += programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAnoActualMenos5,	componente.getId(), subtitulo.getId());
					marcoAnoActualMenos6 += programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAnoActualMenos6,	componente.getId(), subtitulo.getId());
					marcoAnoActualMenos7 += programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAnoActualMenos7,	componente.getId(), subtitulo.getId());
					marcoAnoActualMenos8 += programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAnoActualMenos8,	componente.getId(), subtitulo.getId());
		
				}
		
				reporteHistoricoPorProgramaEstablecimientoVO.setMarcoAnoActual(marcoAnoActual);
				reporteHistoricoPorProgramaEstablecimientoVO.setMarcoAnoActualMenos1(marcoAnoActualMenos1);
				reporteHistoricoPorProgramaEstablecimientoVO.setMarcoAnoActualMenos2(marcoAnoActualMenos2);
				reporteHistoricoPorProgramaEstablecimientoVO.setMarcoAnoActualMenos3(marcoAnoActualMenos3);
				reporteHistoricoPorProgramaEstablecimientoVO.setMarcoAnoActualMenos4(marcoAnoActualMenos4);
				reporteHistoricoPorProgramaEstablecimientoVO.setMarcoAnoActualMenos5(marcoAnoActualMenos5);
				reporteHistoricoPorProgramaEstablecimientoVO.setMarcoAnoActualMenos6(marcoAnoActualMenos6);
				reporteHistoricoPorProgramaEstablecimientoVO.setMarcoAnoActualMenos7(marcoAnoActualMenos7);
				reporteHistoricoPorProgramaEstablecimientoVO.setMarcoAnoActualMenos8(marcoAnoActualMenos8);
				resultado.add(reporteHistoricoPorProgramaEstablecimientoVO);
			}
		}
		return resultado;
	}


	public Integer generarPlanillaReporteHistoricoEstablecimiento(Integer ano) {
		Integer planillaTrabajoId = null;
		List<CellExcelVO> headerSub21 = new ArrayList<CellExcelVO>();
		List<CellExcelVO> headerSub22 = new ArrayList<CellExcelVO>();
		List<CellExcelVO> headerSub29 = new ArrayList<CellExcelVO>();

		List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();

		headerSub21.add((new CellExcelVO("SERVICIO SALUD", 1, 1)));
		headerSub21.add((new CellExcelVO("ESTABLECIMIENTO", 1, 1)));

		headerSub22.add((new CellExcelVO("SERVICIO SALUD", 1, 1)));
		headerSub22.add((new CellExcelVO("ESTABLECIMIENTO", 1, 1)));

		headerSub29.add((new CellExcelVO("SERVICIO SALUD", 1, 1)));
		headerSub29.add((new CellExcelVO("ESTABLECIMIENTO", 1, 1)));

		List<ProgramaVO> programasSub21 = programasService.getProgramasByAnoSubtitulo(ano, Subtitulo.SUBTITULO21);
		List<ProgramaVO> programasSub22 = programasService.getProgramasByAnoSubtitulo(ano, Subtitulo.SUBTITULO22);
		List<ProgramaVO> programasSub29 = programasService.getProgramasByAnoSubtitulo(ano, Subtitulo.SUBTITULO29);

		for (ProgramaVO programa : programasSub21) {
			headerSub21.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " " + (ano - 9) + " (" + ano + ")", 1, 1)));
			headerSub21.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " " + (ano - 8) + " (" + ano + ")", 1, 1)));
			headerSub21.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " " + (ano - 7) + " (" + ano + ")", 1, 1)));
			headerSub21.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " " + (ano - 6) + " (" + ano + ")", 1, 1)));
			headerSub21.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " " + (ano - 5) + " (" + ano + ")", 1, 1)));
			headerSub21.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " " + (ano - 4) + " (" + ano + ")", 1, 1)));
			headerSub21.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " " + (ano - 3) + " (" + ano + ")", 1, 1)));
			headerSub21.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " " + (ano - 2) + " (" + ano + ")", 1, 1)));
			headerSub21.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " " + (ano - 1) + " (" + ano + ")", 1, 1)));
			headerSub21.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " " + ano, 1, 1)));
		}

		for (ProgramaVO programa : programasSub22) {
			headerSub22.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " " + (ano - 9) + " (" + ano + ")", 1, 1)));
			headerSub22.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " " + (ano - 8) + " (" + ano + ")", 1, 1)));
			headerSub22.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " " + (ano - 7) + " (" + ano + ")", 1, 1)));
			headerSub22.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " " + (ano - 6) + " (" + ano + ")", 1, 1)));
			headerSub22.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " " + (ano - 5) + " (" + ano + ")", 1, 1)));
			headerSub22.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " " + (ano - 4) + " (" + ano + ")", 1, 1)));
			headerSub22.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " " + (ano - 3) + " (" + ano + ")", 1, 1)));
			headerSub22.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " " + (ano - 2) + " (" + ano + ")", 1, 1)));
			headerSub22.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " " + (ano - 1) + " (" + ano + ")", 1, 1)));
			headerSub22.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " " + ano, 1, 1)));
		}

		for (ProgramaVO programa : programasSub29) {
			headerSub29.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " " + (ano - 9) + " (" + ano + ")", 1, 1)));
			headerSub29.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " " + (ano - 8) + " (" + ano + ")", 1, 1)));
			headerSub29.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " " + (ano - 7) + " (" + ano + ")", 1, 1)));
			headerSub29.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " " + (ano - 6) + " (" + ano + ")", 1, 1)));
			headerSub29.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " " + (ano - 5) + " (" + ano + ")", 1, 1)));
			headerSub29.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " " + (ano - 4) + " (" + ano + ")", 1, 1)));
			headerSub29.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " " + (ano - 3) + " (" + ano + ")", 1, 1)));
			headerSub29.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " " + (ano - 2) + " (" + ano + ")", 1, 1)));
			headerSub29.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " " + (ano - 1) + " (" + ano + ")", 1, 1)));
			headerSub29.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " " + ano, 1, 1)));
		}

		List<ReporteHistoricoPorProgramaEstablecimientoForExcelVO> reporteHistoricoPorProgramaEstablecimientoForExcelVOSub21 = generarListadoTotalReporteHistoricoDependenciaEstablecimiento(Subtitulo.SUBTITULO21, ano);
		List<ReporteHistoricoPorProgramaEstablecimientoForExcelVO> reporteHistoricoPorProgramaEstablecimientoForExcelVOSub22 = generarListadoTotalReporteHistoricoDependenciaEstablecimiento(Subtitulo.SUBTITULO22, ano);
		List<ReporteHistoricoPorProgramaEstablecimientoForExcelVO> reporteHistoricoPorProgramaEstablecimientoForExcelVOSub29 = generarListadoTotalReporteHistoricoDependenciaEstablecimiento(Subtitulo.SUBTITULO29, ano);

		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator + "Planilla Reporte Historico Programa - Establecimiento.xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());

		GeneradorExcel generadorExcel = new GeneradorExcel(filename);

		ReporteHistoricoProgramaPorEstablecimientoSheetExcel reporteHistoricoProgramaPorEstablecimientoSheetExcelSub21 = new ReporteHistoricoProgramaPorEstablecimientoSheetExcel(
				headerSub21, subHeader, null);
		reporteHistoricoProgramaPorEstablecimientoSheetExcelSub21.setItems(reporteHistoricoPorProgramaEstablecimientoForExcelVOSub21);

		ReporteHistoricoProgramaPorEstablecimientoSheetExcel reporteHistoricoProgramaPorEstablecimientoSheetExcelSub22 = new ReporteHistoricoProgramaPorEstablecimientoSheetExcel(
				headerSub22, subHeader, null);
		reporteHistoricoProgramaPorEstablecimientoSheetExcelSub22.setItems(reporteHistoricoPorProgramaEstablecimientoForExcelVOSub22);

		ReporteHistoricoProgramaPorEstablecimientoSheetExcel reporteHistoricoProgramaPorEstablecimientoSheetExcelSub29 = new ReporteHistoricoProgramaPorEstablecimientoSheetExcel(
				headerSub29, subHeader, null);
		reporteHistoricoProgramaPorEstablecimientoSheetExcelSub29.setItems(reporteHistoricoPorProgramaEstablecimientoForExcelVOSub29);

		generadorExcel.addSheet(reporteHistoricoProgramaPorEstablecimientoSheetExcelSub21, "Subtitulo 21");
		generadorExcel.addSheet(reporteHistoricoProgramaPorEstablecimientoSheetExcelSub22, "Subtitulo 22");
		generadorExcel.addSheet(reporteHistoricoProgramaPorEstablecimientoSheetExcelSub29, "Subtitulo 29");

		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderReportes.replace("{ANO}", ano.toString()));
			TipoDocumento tipoDocumento = new TipoDocumento(TipoDocumentosProcesos.REPORTEHISTORICOPROGRAMAESTABLECIMIENTO.getId());
			planillaTrabajoId = documentService.createDocumentReportes(tipoDocumento, response.getNodeRef(), response.getFileName(), contenType, ano, Integer.parseInt(getMesCurso(true)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return planillaTrabajoId;
	}

	public List<ReporteHistoricoPorProgramaEstablecimientoForExcelVO> generarListadoTotalReporteHistoricoDependenciaEstablecimiento(Subtitulo subtitulo, Integer ano) {
		List<ReporteHistoricoPorProgramaEstablecimientoForExcelVO> resultado = new ArrayList<ReporteHistoricoPorProgramaEstablecimientoForExcelVO>();
		List<ServiciosVO> servicios = servicioSaludService.getServiciosOrderId();
		for (ServiciosVO servicio : servicios) {
			if(servicio.getEstableclimientos() == null || servicio.getEstableclimientos().size() == 0){
				continue;
			}
			for (EstablecimientoSummaryVO establecimiento : servicio.getEstableclimientos()) {
				ReporteHistoricoPorProgramaEstablecimientoForExcelVO fila = new ReporteHistoricoPorProgramaEstablecimientoForExcelVO();
				fila.setServicio(servicio.getNombre_servicio());
				fila.setEstablecimiento(establecimiento.getNombre());

				List<ReporteHistoricoPorProgramaMarcosAnosForExcelVO> listadoMarcosAno = new ArrayList<ReporteHistoricoPorProgramaMarcosAnosForExcelVO>();
				List<ProgramaVO> programas = programasService.getProgramasByAnoSubtitulo(ano, subtitulo);
				for (ProgramaVO programa : programas) {

					ProgramaVO programaAnoActual = programasService.getProgramaAno(programa.getIdProgramaAno());
					Integer idProgramaAnoActual = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(), ano);
					Integer idProgramaAnoActualMenos1 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(),	(ano - 1));
					Integer idProgramaAnoActualMenos2 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(),	(ano - 2));
					Integer idProgramaAnoActualMenos3 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(),	(ano - 3));
					Integer idProgramaAnoActualMenos4 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(),	(ano - 4));
					Integer idProgramaAnoActualMenos5 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(),	(ano - 5));
					Integer idProgramaAnoActualMenos6 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(),	(ano - 6));
					Integer idProgramaAnoActualMenos7 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(),	(ano - 7));
					Integer idProgramaAnoActualMenos8 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(),	(ano - 8));
					Integer idProgramaAnoActualMenos9 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(),	(ano - 9));

					Long marcoAnoActual = 0L;
					Long marcoAnoActualMenos1 = 0L;
					Long marcoAnoActualMenos2 = 0L;
					Long marcoAnoActualMenos3 = 0L;
					Long marcoAnoActualMenos4 = 0L;
					Long marcoAnoActualMenos5 = 0L;
					Long marcoAnoActualMenos6 = 0L;
					Long marcoAnoActualMenos7 = 0L;
					Long marcoAnoActualMenos8 = 0L;
					Long marcoAnoActualMenos9 = 0L;

					ReporteHistoricoPorProgramaMarcosAnosForExcelVO marcos = new ReporteHistoricoPorProgramaMarcosAnosForExcelVO();

					for (ComponentesVO componente : programa.getComponentes()) {

						marcoAnoActual += programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), programaAnoActual.getIdProgramaAno(),componente.getId(), subtitulo.getId());
						marcoAnoActualMenos1 += programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAnoActualMenos1,	componente.getId(), subtitulo.getId());
						marcoAnoActualMenos2 += programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAnoActualMenos2,	componente.getId(), subtitulo.getId());
						marcoAnoActualMenos3 += programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAnoActualMenos3,	componente.getId(), subtitulo.getId());
						marcoAnoActualMenos4 += programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAnoActualMenos4,	componente.getId(), subtitulo.getId());
						marcoAnoActualMenos5 += programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAnoActualMenos5,	componente.getId(), subtitulo.getId());
						marcoAnoActualMenos6 += programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAnoActualMenos6,	componente.getId(), subtitulo.getId());
						marcoAnoActualMenos7 += programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAnoActualMenos7,	componente.getId(), subtitulo.getId());
						marcoAnoActualMenos8 += programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAnoActualMenos8,	componente.getId(), subtitulo.getId());
						marcoAnoActualMenos9 += programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAnoActualMenos9,	componente.getId(), subtitulo.getId());
					}

					marcos.setMarcoAnoActual(marcoAnoActual);
					marcos.setMarcoAnoActualMenos1(marcoAnoActualMenos1);
					marcos.setMarcoAnoActualMenos2(marcoAnoActualMenos2);
					marcos.setMarcoAnoActualMenos3(marcoAnoActualMenos3);
					marcos.setMarcoAnoActualMenos4(marcoAnoActualMenos4);
					marcos.setMarcoAnoActualMenos5(marcoAnoActualMenos5);
					marcos.setMarcoAnoActualMenos6(marcoAnoActualMenos6);
					marcos.setMarcoAnoActualMenos7(marcoAnoActualMenos7);
					marcos.setMarcoAnoActualMenos8(marcoAnoActualMenos8);
					marcos.setMarcoAnoActualMenos9(marcoAnoActualMenos9);
					listadoMarcosAno.add(marcos);
				}
				fila.setMarcosAnos(listadoMarcosAno);
				resultado.add(fila);
			}
		}
		return resultado;
	}

	public List<ReporteHistoricoPorProgramaComunaForExcelVO> generarListadoTotalReporteHistoricoDependenciaComuna(Subtitulo subtitulo, Integer ano) {
		List<ReporteHistoricoPorProgramaComunaForExcelVO> resultado = new ArrayList<ReporteHistoricoPorProgramaComunaForExcelVO>();

		List<ServiciosVO> servicios = servicioSaludService.getServiciosOrderId();
		for (ServiciosVO servicio : servicios) {
			if(servicio.getComunas() == null || servicio.getComunas().size() == 0){
				continue;
			}
			for (ComunaSummaryVO comuna : servicio.getComunas()) {
				ReporteHistoricoPorProgramaComunaForExcelVO fila = new ReporteHistoricoPorProgramaComunaForExcelVO();
				fila.setServicio(servicio.getNombre_servicio());
				fila.setComuna(comuna.getNombre());
				List<ReporteHistoricoPorProgramaMarcosAnosForExcelVO> listadoMarcosAno = new ArrayList<ReporteHistoricoPorProgramaMarcosAnosForExcelVO>();

				List<ProgramaVO> programas = programasService.getProgramasByAnoSubtitulo(ano, subtitulo);
				System.out.println("\n\n\ncantidad de programas ---> " + programas.size());
				for (ProgramaVO programa : programas) {
					ProgramaVO programaAnoActual = programasService.getProgramaAno(programa.getIdProgramaAno());
					Integer idProgramaAnoActual = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(), ano);
					Integer idProgramaAnoActualMenos1 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(), (ano - 1));
					Integer idProgramaAnoActualMenos2 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(), (ano - 2));
					Integer idProgramaAnoActualMenos3 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(), (ano - 3));
					Integer idProgramaAnoActualMenos4 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(), (ano - 4));
					Integer idProgramaAnoActualMenos5 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(), (ano - 5));
					Integer idProgramaAnoActualMenos6 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(), (ano - 6));
					Integer idProgramaAnoActualMenos7 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(), (ano - 7));
					Integer idProgramaAnoActualMenos8 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(), (ano - 8));
					Integer idProgramaAnoActualMenos9 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(), (ano - 9));

					Long tarifaAnoActual = 0L;
					Long tarifaAnoActualMenos1 = 0L;
					Long tarifaAnoActualMenos2 = 0L;
					Long tarifaAnoActualMenos3 = 0L;
					Long tarifaAnoActualMenos4 = 0L;
					Long tarifaAnoActualMenos5 = 0L;
					Long tarifaAnoActualMenos6 = 0L;
					Long tarifaAnoActualMenos7 = 0L;
					Long tarifaAnoActualMenos8 = 0L;
					Long tarifaAnoActualMenos9 = 0L;

					ReporteHistoricoPorProgramaMarcosAnosForExcelVO marcos = new ReporteHistoricoPorProgramaMarcosAnosForExcelVO();

					for (ComponentesVO componente : programa.getComponentes()) {
						tarifaAnoActual += programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAnoActual, componente.getId(), subtitulo.getId());
						tarifaAnoActualMenos1 += programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAnoActualMenos1, componente.getId(), subtitulo.getId());
						tarifaAnoActualMenos2 += programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAnoActualMenos2, componente.getId(), subtitulo.getId());
						tarifaAnoActualMenos3 += programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAnoActualMenos3, componente.getId(), subtitulo.getId());
						tarifaAnoActualMenos4 += programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAnoActualMenos4, componente.getId(), subtitulo.getId());
						tarifaAnoActualMenos5 += programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAnoActualMenos5, componente.getId(), subtitulo.getId());
						tarifaAnoActualMenos6 += programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAnoActualMenos6, componente.getId(), subtitulo.getId());
						tarifaAnoActualMenos7 += programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAnoActualMenos7, componente.getId(), subtitulo.getId());
						tarifaAnoActualMenos8 += programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAnoActualMenos8, componente.getId(), subtitulo.getId());
						tarifaAnoActualMenos9 += programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAnoActualMenos9, componente.getId(), subtitulo.getId());

					}
					marcos.setMarcoAnoActualMenos9(tarifaAnoActualMenos9);
					marcos.setMarcoAnoActualMenos8(tarifaAnoActualMenos8);
					marcos.setMarcoAnoActualMenos7(tarifaAnoActualMenos7);
					marcos.setMarcoAnoActualMenos6(tarifaAnoActualMenos6);
					marcos.setMarcoAnoActualMenos5(tarifaAnoActualMenos5);
					marcos.setMarcoAnoActualMenos4(tarifaAnoActualMenos4);
					marcos.setMarcoAnoActualMenos3(tarifaAnoActualMenos3);
					marcos.setMarcoAnoActualMenos2(tarifaAnoActualMenos2);
					marcos.setMarcoAnoActualMenos1(tarifaAnoActualMenos1);
					marcos.setMarcoAnoActual(tarifaAnoActual);

					System.out.println("\n\n\nmarcos.getMarcoAnoActual() --> "+ marcos.getMarcoAnoActual());

					listadoMarcosAno.add(marcos);
				}
				fila.setMarcosAnos(listadoMarcosAno);
				resultado.add(fila);
			}
		}
		return resultado;
	}

	public List<ReporteHistoricoPorProgramaComunaVO> getReporteHistoricoPorProgramaVOFiltroServicioComuna(Integer idProgramaAno, Integer idServicio, Integer idComuna, Subtitulo subtitulo, Integer ano) {
		System.out.println("idProgramaAno --> " + idProgramaAno	+ "  subtitulo --> " + subtitulo.getNombre());
		List<ReporteHistoricoPorProgramaComunaVO> resultado = new ArrayList<ReporteHistoricoPorProgramaComunaVO>();

		ProgramaVO programaAnoActual = programasService.getProgramaAno(idProgramaAno); // 2015
		List<ComponentesVO> componentes = programasService.getComponenteByProgramaSubtitulos(programaAnoActual.getId(), subtitulo);
		Integer idProgramaAnoActualMenos1 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(),	(ano - 1)); // 2014
		Integer idProgramaAnoActualMenos2 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(),	(ano - 2)); // 2013
		Integer idProgramaAnoActualMenos3 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(),	(ano - 3)); // 2012
		Integer idProgramaAnoActualMenos4 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(),	(ano - 4)); // 2011
		Integer idProgramaAnoActualMenos5 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(),	(ano - 5)); // 2010
		Integer idProgramaAnoActualMenos6 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(),	(ano - 6)); // 2009
		Integer idProgramaAnoActualMenos7 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(),	(ano - 7)); // 2008
		Integer idProgramaAnoActualMenos8 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(),	(ano - 8)); // 2007
		Integer idProgramaAnoActualMenos9 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(),	(ano - 9)); // 2006

		// TODO cambiar despues este valor
		System.out.println("ano --> " + ano);

		List<Comuna> comunas = null;
		if(idComuna != null && idComuna != 0){
			comunas = new ArrayList<Comuna>();
			Comuna comuna = comunaDAO.getComunaById(idComuna);
			comunas.add(comuna);
		}else{
			if(idServicio != null){
				comunas = comunaDAO.getComunasByServicio(idServicio);
			}else{
				comunas = new ArrayList<Comuna>();
				List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
				for(ServicioSalud servicioSalud : servicios){
					if(servicioSalud.getComunas() != null && servicioSalud.getComunas().size() > 0){
						for(Comuna comunaServicio : servicioSalud.getComunas()){
							comunas.add(comunaServicio);
						}
					}
				}
			}
		}
		
		if(comunas != null && comunas.size() > 0){
			for(Comuna comuna : comunas){
				Long tarifaAnoActual = 0L;
				Long tarifaAnoActualMenos1 = 0L;
				Long tarifaAnoActualMenos2 = 0L;
				Long tarifaAnoActualMenos3 = 0L;
				Long tarifaAnoActualMenos4 = 0L;
				Long tarifaAnoActualMenos5 = 0L;
				Long tarifaAnoActualMenos6 = 0L;
				Long tarifaAnoActualMenos7 = 0L;
				Long tarifaAnoActualMenos8 = 0L;
				Long tarifaAnoActualMenos9 = 0L;
		
				ReporteHistoricoPorProgramaComunaVO reporteHistoricoPorProgramaComunaVO = new ReporteHistoricoPorProgramaComunaVO();
				reporteHistoricoPorProgramaComunaVO.setRegion(comuna.getServicioSalud().getRegion().getNombre());
				reporteHistoricoPorProgramaComunaVO.setServicio(comuna.getServicioSalud().getNombre());
				reporteHistoricoPorProgramaComunaVO.setComuna(comuna.getNombre());
				reporteHistoricoPorProgramaComunaVO.setPrograma(programaAnoActual.getNombre());
		
				for (ComponentesVO componente : componentes) {
					tarifaAnoActual += programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAno, componente.getId(), subtitulo.getId());
					tarifaAnoActualMenos1 += programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAnoActualMenos1, componente.getId(), subtitulo.getId());
					tarifaAnoActualMenos2 += programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAnoActualMenos2, componente.getId(), subtitulo.getId());
					tarifaAnoActualMenos3 += programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAnoActualMenos3, componente.getId(), subtitulo.getId());
					tarifaAnoActualMenos4 += programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAnoActualMenos4, componente.getId(), subtitulo.getId());
					tarifaAnoActualMenos5 += programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAnoActualMenos5, componente.getId(), subtitulo.getId());
					tarifaAnoActualMenos6 += programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAnoActualMenos6, componente.getId(), subtitulo.getId());
					tarifaAnoActualMenos7 += programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAnoActualMenos7, componente.getId(), subtitulo.getId());
					tarifaAnoActualMenos8 += programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAnoActualMenos8, componente.getId(), subtitulo.getId());
					tarifaAnoActualMenos9 += programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAnoActualMenos9, componente.getId(), subtitulo.getId());
				}
				
				reporteHistoricoPorProgramaComunaVO.setMarcoAnoActual(tarifaAnoActual);
				reporteHistoricoPorProgramaComunaVO.setMarcoAnoActualMenos1(tarifaAnoActualMenos1);
				reporteHistoricoPorProgramaComunaVO.setMarcoAnoActualMenos2(tarifaAnoActualMenos2);
				reporteHistoricoPorProgramaComunaVO.setMarcoAnoActualMenos3(tarifaAnoActualMenos3);
				reporteHistoricoPorProgramaComunaVO.setMarcoAnoActualMenos4(tarifaAnoActualMenos4);
				reporteHistoricoPorProgramaComunaVO.setMarcoAnoActualMenos5(tarifaAnoActualMenos5);
				reporteHistoricoPorProgramaComunaVO.setMarcoAnoActualMenos6(tarifaAnoActualMenos6);
				reporteHistoricoPorProgramaComunaVO.setMarcoAnoActualMenos7(tarifaAnoActualMenos7);
				reporteHistoricoPorProgramaComunaVO.setMarcoAnoActualMenos8(tarifaAnoActualMenos8);
				resultado.add(reporteHistoricoPorProgramaComunaVO);
			}
		}
		return resultado;
	}

	public Integer generarPlanillaReporteHistoricoComuna(Integer ano) {
		Integer planillaTrabajoId = null;
		List<CellExcelVO> header = new ArrayList<CellExcelVO>();
		List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();

		header.add((new CellExcelVO("SERVICIO SALUD", 1, 1)));
		header.add((new CellExcelVO("COMUNA", 1, 1)));

		List<ProgramaVO> programasSub24 = programasService.getProgramasByAnoSubtitulo(ano, Subtitulo.SUBTITULO24);
		for (ProgramaVO programa : programasSub24) {
			header.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " "	+ (ano - 9) + " (" + ano + ")", 1, 1)));
			header.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " "	+ (ano - 8) + " (" + ano + ")", 1, 1)));
			header.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " " + (ano - 7) + " (" + ano + ")", 1, 1)));
			header.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " " + (ano - 6) + " (" + ano + ")", 1, 1)));
			header.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " "	+ (ano - 5) + " (" + ano + ")", 1, 1)));
			header.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " "	+ (ano - 4) + " (" + ano + ")", 1, 1)));
			header.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " "	+ (ano - 3) + " (" + ano + ")", 1, 1)));
			header.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " "	+ (ano - 2) + " (" + ano + ")", 1, 1)));
			header.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " "	+ (ano - 1) + " (" + ano + ")", 1, 1)));
			header.add((new CellExcelVO("PROG, " + programa.getNombre().toUpperCase() + " " + ano, 1, 1)));
		}

		System.out.println("\n\n\n\ncantidad de headers --> "+header.size()+"\n\n\n\n");

		List<ReporteHistoricoPorProgramaComunaForExcelVO> reporteHistoricoPorProgramaComunaForExcelVO = generarListadoTotalReporteHistoricoDependenciaComuna(Subtitulo.SUBTITULO24, ano);

		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator + "Planilla Reporte Historico Programa - Comuna.xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());

		GeneradorExcel generadorExcel = new GeneradorExcel(filename);
		ReporteHistoricoProgramaPorComunaSheetExcel reporteHistoricoProgramaPorComunaSheetExcelSub24 = new ReporteHistoricoProgramaPorComunaSheetExcel(header, subHeader, null);
		reporteHistoricoProgramaPorComunaSheetExcelSub24.setItems(reporteHistoricoPorProgramaComunaForExcelVO);

		generadorExcel.addSheet(reporteHistoricoProgramaPorComunaSheetExcelSub24, "Subtitulo 24");

		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderReportes.replace("{ANO}", ano.toString()));
			TipoDocumento tipoDocumento = new TipoDocumento(TipoDocumentosProcesos.REPORTEHISTORICOPROGRAMACOMUNA.getId());
			planillaTrabajoId = documentService.createDocumentReportes(tipoDocumento, response.getNodeRef(), response.getFileName(), contenType, ano, Integer.parseInt(getMesCurso(true)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return planillaTrabajoId;
	}
	
	public Integer getDocumentByTypeAnoActual(TipoDocumentosProcesos tipoDocumentoProceso, Integer ano) {
		System.out.println("tipoDocumentoProceso --> " + tipoDocumentoProceso.getName());
		System.out.println("ano --> " + ano);
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO = documentService.getDocumentByTypeAnoReportes(tipoDocumentoProceso, ano);
		if (referenciaDocumentoSummaryVO == null) {
			return null;
		} else {
			return referenciaDocumentoSummaryVO.getId();
		}
	}

	public List<ReporteRebajaVO> getReporteRebaja(Integer ano) {
		System.out.println("Entro a getReporteRebaja");
		List<ReporteRebajaVO> resultado = new ArrayList<ReporteRebajaVO>();
		List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
		for (ServicioSalud servicioSalud : servicios) {
			if(servicioSalud.getComunas() == null || servicioSalud.getComunas().size() == 0){
				continue;
			}
			for (Comuna comuna : servicioSalud.getComunas()) {
				ReporteRebajaVO reporteRebajaVO = new ReporteRebajaVO();
				reporteRebajaVO.setRegion(servicioSalud.getRegion().getNombre());
				reporteRebajaVO.setServicio(servicioSalud.getNombre());
				reporteRebajaVO.setComuna(comuna.getNombre());
				DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(ano);
				System.out.println("distribucionInicialPercapita --> " + ((distribucionInicialPercapita == null) ? null : distribucionInicialPercapita.getIdDistribucionInicialPercapita()));

				List<AntecendentesComunaCalculado> antecendentesComunaCalculados = antecedentesComunaDAO.findAntecendentesComunaCalculadoByComunaServicioDistribucionInicialPercapitaVigente(servicioSalud.getId(), comuna.getId(),
								distribucionInicialPercapita.getIdDistribucionInicialPercapita());
				AntecendentesComunaCalculado antecendentesComunaCalculado = ((antecendentesComunaCalculados != null && antecendentesComunaCalculados.size() > 0) ? antecendentesComunaCalculados.get(0)	: null);
				Double porcentajeRebajaCorte1 = 0.0;
				Double porcentajeRebajaCorte2 = 0.0;
				Double porcentajeRebajaCorte3 = 0.0;
				Double porcentajeRebajaCorte4 = 0.0;

				Integer montoRebajaCorte1 = 0;
				Integer montoRebajaCorte2 = 0;
				Integer montoRebajaCorte3 = 0;
				Integer montoRebajaCorte4 = 0;
				
				System.out.println("antecendentesComunaCalculado --> " + ((antecendentesComunaCalculado == null) ? null : antecendentesComunaCalculado.getIdAntecendentesComunaCalculado()));
				
				if (antecendentesComunaCalculado != null && !antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().isAuxiliar()) {
					if (antecendentesComunaCalculado.getAntecedentesComunaCalculadoRebajas() != null && antecendentesComunaCalculado.getAntecedentesComunaCalculadoRebajas().size() > 0) {
						for (AntecedentesComunaCalculadoRebaja antecedentesComunaCalculadoRebaja : antecendentesComunaCalculado.getAntecedentesComunaCalculadoRebajas()) {
							if (antecedentesComunaCalculadoRebaja.getRebaja().getRebajaCorte().getRebajaCorteId() == 1) {
								System.out.println("corte1");
								montoRebajaCorte1 = antecedentesComunaCalculadoRebaja.getMontoRebaja();

								List<CumplimientoRebajaVO> cumplimientoRebajasVO = rebajaService.getCumplimientoByRebajaComuna(antecedentesComunaCalculadoRebaja.getRebaja().getIdRebaja(),
												antecedentesComunaCalculadoRebaja.getAntecedentesComunaCalculado().getAntecedentesComuna().getIdComuna().getId());
								if (cumplimientoRebajasVO != null && cumplimientoRebajasVO.size() > 0) {
									Integer porcentajeRebajaFinal1 = 0;
									for (CumplimientoRebajaVO cumplimientoRebajaVO : cumplimientoRebajasVO) {
										System.out.println("cumplimientoRebajaVO.getRebajaFinal() --> "	+ cumplimientoRebajaVO.getRebajaFinal());
										porcentajeRebajaFinal1 += ((cumplimientoRebajaVO.getRebajaFinal() == null) ? 0 : cumplimientoRebajaVO.getRebajaFinal().intValue());
									}
									System.out.println("porcentajeRebajaFinal-->" + porcentajeRebajaFinal1);
									porcentajeRebajaCorte1 = porcentajeRebajaFinal1 / 100.0;
								}
							}else if(antecedentesComunaCalculadoRebaja.getRebaja().getRebajaCorte().getRebajaCorteId() == 2) {
								System.out.println("corte2");
								montoRebajaCorte2 = antecedentesComunaCalculadoRebaja.getMontoRebaja();

								System.out.println("antecedentesComunaCalculadoRebaja.getRebaja().getIdRebaja() --> " + antecedentesComunaCalculadoRebaja.getRebaja().getIdRebaja());
								System.out.println("antecedentesComunaCalculadoRebaja.getAntecedentesComunaCalculado().getAntecedentesComuna().getIdComuna().getId() --> " + antecedentesComunaCalculadoRebaja.getAntecedentesComunaCalculado().getAntecedentesComuna().getIdComuna().getId());

								List<CumplimientoRebajaVO> cumplimientoRebajasVO = rebajaService.getCumplimientoByRebajaComuna(antecedentesComunaCalculadoRebaja.getRebaja().getIdRebaja(),
												antecedentesComunaCalculadoRebaja.getAntecedentesComunaCalculado().getAntecedentesComuna().getIdComuna().getId());
								if (cumplimientoRebajasVO != null && cumplimientoRebajasVO.size() > 0) {
									Integer porcentajeRebajaFinal2 = 0;
									for (CumplimientoRebajaVO cumplimientoRebajaVO : cumplimientoRebajasVO) {
										System.out.println("cumplimientoRebajaVO.getRebajaFinal() --> "	+ cumplimientoRebajaVO.getRebajaFinal());
										porcentajeRebajaFinal2 += ((cumplimientoRebajaVO.getRebajaFinal() == null) ? 0 : cumplimientoRebajaVO.getRebajaFinal().intValue());
										System.out.println("CORTE 2 --> porcentajeRebajaFinal-->" + porcentajeRebajaFinal2);
									}
									porcentajeRebajaCorte2 = porcentajeRebajaFinal2 / 100.0;
								}
							}else if(antecedentesComunaCalculadoRebaja.getRebaja().getRebajaCorte().getRebajaCorteId() == 3) {
								System.out.println("corte3");
								montoRebajaCorte3 = antecedentesComunaCalculadoRebaja.getMontoRebaja();

								List<CumplimientoRebajaVO> cumplimientoRebajasVO = rebajaService.getCumplimientoByRebajaComuna(antecedentesComunaCalculadoRebaja.getRebaja().getIdRebaja(),
												antecedentesComunaCalculadoRebaja.getAntecedentesComunaCalculado().getAntecedentesComuna().getIdComuna().getId());
								if (cumplimientoRebajasVO != null && cumplimientoRebajasVO.size() > 0) {
									Integer porcentajeRebajaFinal3 = 0;
									for (CumplimientoRebajaVO cumplimientoRebajaVO : cumplimientoRebajasVO) {
										System.out.println("cumplimientoRebajaVO.getRebajaFinal() --> " + cumplimientoRebajaVO.getRebajaFinal());
										porcentajeRebajaFinal3 += ((cumplimientoRebajaVO.getRebajaFinal() == null) ? 0 : cumplimientoRebajaVO.getRebajaFinal().intValue());
										System.out.println("CORTE 3 --> porcentajeRebajaFinal-->" + porcentajeRebajaFinal3);
									}
									porcentajeRebajaCorte3 = porcentajeRebajaFinal3 / 100.0;
								}
							}else if(antecedentesComunaCalculadoRebaja.getRebaja().getRebajaCorte().getRebajaCorteId() == 4) {
								System.out.println("corte4");
								montoRebajaCorte4 = antecedentesComunaCalculadoRebaja.getMontoRebaja();

								List<CumplimientoRebajaVO> cumplimientoRebajasVO = rebajaService.getCumplimientoByRebajaComuna(antecedentesComunaCalculadoRebaja.getRebaja().getIdRebaja(),
												antecedentesComunaCalculadoRebaja.getAntecedentesComunaCalculado().getAntecedentesComuna().getIdComuna().getId());
								if (cumplimientoRebajasVO != null && cumplimientoRebajasVO.size() > 0) {
									Integer porcentajeRebajaFinal4 = 0;
									for (CumplimientoRebajaVO cumplimientoRebajaVO : cumplimientoRebajasVO) {
										System.out.println("cumplimientoRebajaVO.getRebajaFinal() --> "	+ cumplimientoRebajaVO.getRebajaFinal());
										porcentajeRebajaFinal4 += ((cumplimientoRebajaVO.getRebajaFinal() == null) ? 0 : cumplimientoRebajaVO.getRebajaFinal().intValue());
									}
									System.out.println("porcentajeRebajaFinal-->" + porcentajeRebajaFinal4);
									porcentajeRebajaCorte4 = porcentajeRebajaFinal4 / 100.0;
								}
							}
						}
					}else{
						continue;
					}
				}else{
					continue;
				}
				reporteRebajaVO.setfCorte1Monto(montoRebajaCorte1.longValue());
				reporteRebajaVO.setfCorte1Porcentaje((porcentajeRebajaCorte1));
				reporteRebajaVO.setfCorte2Monto(montoRebajaCorte2.longValue());
				reporteRebajaVO.setfCorte2Porcentaje((porcentajeRebajaCorte2));
				reporteRebajaVO.setfCorte3Monto(montoRebajaCorte3.longValue());
				reporteRebajaVO.setfCorte3Porcentaje((porcentajeRebajaCorte3));
				reporteRebajaVO.setfCorte4Monto(montoRebajaCorte4.longValue());
				reporteRebajaVO.setfCorte4Porcentaje((porcentajeRebajaCorte4));
				reporteRebajaVO.setRebajaTotal(montoRebajaCorte1.longValue() + montoRebajaCorte2.longValue() + montoRebajaCorte3.longValue() + montoRebajaCorte4.longValue());
				resultado.add(reporteRebajaVO);
			}
		}
		System.out.println("Salio a getReporteRebaja");
		return resultado;
	}

	public Integer generarPlanillaReporteRebaja(String usuario, Integer ano) {
		Integer planillaTrabajoId = null;

		RebajaCorte rebajaCorte1 = rebajaDAO.getCorteById(1);
		RebajaCorte rebajaCorte2 = rebajaDAO.getCorteById(2);
		RebajaCorte rebajaCorte3 = rebajaDAO.getCorteById(3);
		RebajaCorte rebajaCorte4 = rebajaDAO.getCorteById(4);

		Mes fechaMesCorte1 = mesDAO.getMesPorID(rebajaCorte1.getMesRebaja().getIdMes());
		Mes fechaMesCorte2 = mesDAO.getMesPorID(rebajaCorte2.getMesRebaja().getIdMes());
		Mes fechaMesCorte3 = mesDAO.getMesPorID(rebajaCorte3.getMesRebaja().getIdMes());
		Mes fechaMesCorte4 = mesDAO.getMesPorID(rebajaCorte4.getMesRebaja().getIdMes());

		List<CellExcelVO> header = new ArrayList<CellExcelVO>();
		List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
		header.add((new CellExcelVO("REGION", 1, 2)));
		header.add((new CellExcelVO("SERVICIO", 1, 2)));
		header.add((new CellExcelVO("COMUNA", 1, 2)));
		header.add((new CellExcelVO(StringUtil.caracterUnoMayuscula(fechaMesCorte1.getNombre()), 2, 1)));
		header.add((new CellExcelVO(StringUtil.caracterUnoMayuscula(fechaMesCorte2.getNombre()), 2, 1)));
		header.add((new CellExcelVO(StringUtil.caracterUnoMayuscula(fechaMesCorte3.getNombre()), 2, 1)));
		header.add((new CellExcelVO(StringUtil.caracterUnoMayuscula(fechaMesCorte4.getNombre()), 2, 1)));
		header.add((new CellExcelVO("REBAJA APLICADA", 1, 2)));

		subHeader.add((new CellExcelVO("$ MONTO", 1, 1)));
		subHeader.add((new CellExcelVO("%", 1, 1)));
		subHeader.add((new CellExcelVO("$ MONTO", 1, 1)));
		subHeader.add((new CellExcelVO("%", 1, 1)));
		subHeader.add((new CellExcelVO("$ MONTO", 1, 1)));
		subHeader.add((new CellExcelVO("%", 1, 1)));
		subHeader.add((new CellExcelVO("$ MONTO", 1, 1)));
		subHeader.add((new CellExcelVO("%", 1, 1)));

		List<ReporteRebajaVO> reporteRebajaVO = this.getReporteRebaja(ano);

		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator + "Panilla Reporte Rebaja.xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());

		GeneradorExcel generadorExcel = new GeneradorExcel(filename);

		ReporteRebajaSheetExcel reporteRebajaSheetExcel = new ReporteRebajaSheetExcel(header, subHeader, null);
		reporteRebajaSheetExcel.setItems(reporteRebajaVO);

		generadorExcel.addSheet(reporteRebajaSheetExcel, "rebaja");

		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderReportes.replace("{ANO}", ano.toString()));
			TipoDocumento tipoDocumento = new TipoDocumento(TipoDocumentosProcesos.REPORTEREBAJA.getId());
			planillaTrabajoId = documentService.createDocumentReportes(tipoDocumento, response.getNodeRef(), response.getFileName(), contenType, ano, Integer.parseInt(getMesCurso(true)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return planillaTrabajoId;
	}

	public Integer generarPlanillaReporteMonitoreoProgramaPorComuna(Integer ano) {
		Integer planillaTrabajoId = null;
		List<CellExcelVO> header = new ArrayList<CellExcelVO>();
		List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
		header.add((new CellExcelVO("SERVICIO", 1, 2)));
		header.add((new CellExcelVO("PROGRAMA", 1, 2)));
		header.add((new CellExcelVO("COMPONENTE", 1, 2)));
		header.add((new CellExcelVO("COMUNA", 1, 2)));
		header.add((new CellExcelVO("MARCO PRESUPUESTARIO", 1, 2)));

		header.add((new CellExcelVO("REMESA ACUMULADA", 1, 1)));
		header.add((new CellExcelVO("CONVENIO", 1, 1)));

		header.add((new CellExcelVO("CONVENIO PENDIENTE", 1, 2)));

		subHeader.add((new CellExcelVO("MONTO", 1, 1)));
		subHeader.add((new CellExcelVO("%", 1, 1)));
		subHeader.add((new CellExcelVO("MONTO", 1, 1)));
		subHeader.add((new CellExcelVO("%", 1, 1)));

		List<ReporteMonitoreoProgramaPorComunaVO> reporteMonitoreoProgramaPorComunaVO = getReporteMonitoreoPorComunaAll(Subtitulo.SUBTITULO24, ano);

		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator + "Panilla Reporte Monitoreo Programa - Comuna.xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());

		GeneradorExcel generadorExcel = new GeneradorExcel(filename);

		ReporteMonitoreoProgramaComunaSheetExcel reporteMonitoreoProgramaComunaSheetExcel = new ReporteMonitoreoProgramaComunaSheetExcel(
				header, subHeader, null);

		reporteMonitoreoProgramaComunaSheetExcel.setItems(reporteMonitoreoProgramaPorComunaVO);

		generadorExcel.addSheet(reporteMonitoreoProgramaComunaSheetExcel, "Por Comuna");

		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderReportes.replace("{ANO}", ano.toString()));
			TipoDocumento tipoDocumento = new TipoDocumento(TipoDocumentosProcesos.REPORTEMONITOREOPROGRAMACOMUNA.getId());
			planillaTrabajoId = documentService.createDocumentReportes(tipoDocumento, response.getNodeRef(), response.getFileName(), contenType, ano, Integer.parseInt(getMesCurso(true)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return planillaTrabajoId;
	}

	public Integer generarPlanillaReporteMonitoreoProgramaPorServicios(Integer ano) {
		Integer planillaTrabajoId = null;
		List<CellExcelVO> header = new ArrayList<CellExcelVO>();
		List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
		header.add((new CellExcelVO("SERVICIO", 1, 2)));
		header.add((new CellExcelVO("PROGRAMA", 1, 2)));
		header.add((new CellExcelVO("COMPONENTE", 1, 2)));
		header.add((new CellExcelVO("ESTABLECIMIENTO", 1, 2)));
		header.add((new CellExcelVO("MARCO PRESUPUESTARIO", 1, 2)));

		header.add((new CellExcelVO("REMESA ACUMULADA", 1, 1)));
		header.add((new CellExcelVO("CONVENIO", 1, 1)));

		header.add((new CellExcelVO("CONVENIO PENDIENTE", 1, 2)));

		subHeader.add((new CellExcelVO("MONTO", 1, 1)));
		subHeader.add((new CellExcelVO("%", 1, 1)));
		subHeader.add((new CellExcelVO("MONTO", 1, 1)));
		subHeader.add((new CellExcelVO("%", 1, 1)));

		List<ReporteMonitoreoProgramaPorEstablecimientoVO> reporteMonitoreoProgramaPorEstablecimientoVOSub21 = getReporteMonitoreoPorEstablecimientoAll(Subtitulo.SUBTITULO21, ano);
		List<ReporteMonitoreoProgramaPorEstablecimientoVO> reporteMonitoreoProgramaPorEstablecimientoVOSub22 = getReporteMonitoreoPorEstablecimientoAll(Subtitulo.SUBTITULO22, ano);
		List<ReporteMonitoreoProgramaPorEstablecimientoVO> reporteMonitoreoProgramaPorEstablecimientoVOSub29 = getReporteMonitoreoPorEstablecimientoAll(Subtitulo.SUBTITULO29, ano);

		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator + "Panilla Reporte Monitoreo Programa - Servicios.xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());

		GeneradorExcel generadorExcel = new GeneradorExcel(filename);

		ReporteMonitoreoProgramaEstablecimientoSheetExcel reporteMonitoreoProgramaEstablecimientoSheetExcelSub21 = new ReporteMonitoreoProgramaEstablecimientoSheetExcel(
				header, subHeader, null);

		ReporteMonitoreoProgramaEstablecimientoSheetExcel reporteMonitoreoProgramaEstablecimientoSheetExcelSub22 = new ReporteMonitoreoProgramaEstablecimientoSheetExcel(
				header, subHeader, null);

		ReporteMonitoreoProgramaEstablecimientoSheetExcel reporteMonitoreoProgramaEstablecimientoSheetExcelSub29 = new ReporteMonitoreoProgramaEstablecimientoSheetExcel(
				header, subHeader, null);

		reporteMonitoreoProgramaEstablecimientoSheetExcelSub21.setItems(reporteMonitoreoProgramaPorEstablecimientoVOSub21);
		reporteMonitoreoProgramaEstablecimientoSheetExcelSub22.setItems(reporteMonitoreoProgramaPorEstablecimientoVOSub22);
		reporteMonitoreoProgramaEstablecimientoSheetExcelSub29.setItems(reporteMonitoreoProgramaPorEstablecimientoVOSub29);

		generadorExcel.addSheet(reporteMonitoreoProgramaEstablecimientoSheetExcelSub21,	"Subtitulo 21");
		generadorExcel.addSheet(reporteMonitoreoProgramaEstablecimientoSheetExcelSub22,	"Subtitulo 22");
		generadorExcel.addSheet(reporteMonitoreoProgramaEstablecimientoSheetExcelSub29,	"Subtitulo 29");

		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderReportes.replace("{ANO}", ano.toString()));
			TipoDocumento tipoDocumento = new TipoDocumento(TipoDocumentosProcesos.REPORTEMONITOREOPROGRAMASERVICIO.getId());
			planillaTrabajoId = documentService.createDocumentReportes(tipoDocumento, response.getNodeRef(), response.getFileName(), contenType, ano, Integer.parseInt(getMesCurso(true)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return planillaTrabajoId;
	}

	public Integer generarPlanillaReporteGlosa07(String usuario, Integer ano) {
		System.out.println("generando excel");
		Integer planillaTrabajoId = null;
		List<CellExcelVO> header = new ArrayList<CellExcelVO>();
		List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();

		Integer mesCurso = Integer.parseInt(getMesCurso(true));

		Mes mes = mesDAO.getMesPorID(mesCurso);

		header.add((new CellExcelVO("REGION", 1, 2)));
		header.add((new CellExcelVO("SERVICIO DE SALUD", 1, 2)));
		header.add((new CellExcelVO("COMUNA", 1, 2)));
		header.add((new CellExcelVO("ART 49 PER CAPITA", 2, 1)));
		header.add((new CellExcelVO("ART. 56 REFORZAMIENTO " + mes.getNombre().toUpperCase() + " 2014 APS MUNICIPAL", 2, 1)));
		header.add((new CellExcelVO("TOTAL REMESAS ENERO A " + mes.getNombre().toUpperCase() + " 2014 APS MUNICIPAL", 2, 1)));

		List<ReporteGlosaVO> reporteGlosaVO = this.getReporteGlosa(usuario, ano);

		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator + "Panilla Reporte Glosa07.xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());

		GeneradorExcel generadorExcel = new GeneradorExcel(filename);

		ReporteGlosa07SheetExcel reporteGlosa07SheetExcel = new ReporteGlosa07SheetExcel(header, null, null);
		reporteGlosa07SheetExcel.setItems(reporteGlosaVO);

		generadorExcel.addSheet(reporteGlosa07SheetExcel, "glosa07");

		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderReportes.replace("{ANO}", ano.toString()));
			TipoDocumento tipoDocumento = new TipoDocumento(TipoDocumentosProcesos.REPORTEGLOSA07.getId());
			planillaTrabajoId = documentService.createDocumentReportes(tipoDocumento, response.getNodeRef(), response.getFileName(), contenType, ano, Integer.parseInt(getMesCurso(true)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return planillaTrabajoId;
	}

	public Integer generarPlanillaReporteMarcoPresupuestarioComuna(Integer ano) {
		Integer planillaTrabajoId = null;
		List<CellExcelVO> header = new ArrayList<CellExcelVO>();
		List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
		header.add((new CellExcelVO("SERVICIOS", 1, 1)));
		header.add((new CellExcelVO("LINEAS PROGRAMATICAS", 1, 1)));
		header.add((new CellExcelVO("COMPONENTES", 1, 1)));
		header.add((new CellExcelVO("COMUNAS", 1, 1)));
		header.add((new CellExcelVO("MARCO AÑO" + ano, 1, 1)));
		header.add((new CellExcelVO("CONVENIO AÑO" + ano, 1, 1)));
		header.add((new CellExcelVO("REMESAS ACUMULADAS AÑO" + ano,
				1, 1)));
		header.add((new CellExcelVO("% TRANSFERIDO A " + getMesCurso(true) + "-" + ano, 1, 1)));
		header.add((new CellExcelVO("OBSERVACIÓN", 1, 1)));

		List<ReporteMarcoPresupuestarioComunaVO> reporteMarcoPresupuestarioComunaVO = this.getReporteMarcoPorComunaAll(Subtitulo.SUBTITULO24, ano);

		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator + "Planilla Marco Presupuestario por Comuna.xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());

		GeneradorExcel generadorExcel = new GeneradorExcel(filename);

		ReporteMarcoPresupuestarioComunaSheetExcel reporteMarcoPresupuestarioComunaSheetExcel = new ReporteMarcoPresupuestarioComunaSheetExcel(header, subHeader, null);

		reporteMarcoPresupuestarioComunaSheetExcel.setItems(reporteMarcoPresupuestarioComunaVO);

		generadorExcel.addSheet(reporteMarcoPresupuestarioComunaSheetExcel, "Subtitulo 24");

		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderReportes.replace("{ANO}", ano.toString()));
			TipoDocumento tipoDocumento = new TipoDocumento(TipoDocumentosProcesos.REPORTEMARCOPRESPUESTARIOCOMUNA.getId());
			planillaTrabajoId = documentService.createDocumentReportes(tipoDocumento, response.getNodeRef(), response.getFileName(), contenType, ano, Integer.parseInt(getMesCurso(true)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return planillaTrabajoId;
	}

	public Integer generarPlanillaReporteMarcoPresupuestarioServicios(Integer ano) {
		Integer planillaTrabajoId = null;
		List<CellExcelVO> header = new ArrayList<CellExcelVO>();
		List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
		header.add((new CellExcelVO("SERVICIOS", 1, 1)));
		header.add((new CellExcelVO("LINEAS PROGRAMATICAS", 1, 1)));
		header.add((new CellExcelVO("COMPONENTES", 1, 1)));
		header.add((new CellExcelVO("ESTABLECIMIENTOS", 1, 1)));
		header.add((new CellExcelVO("MARCO AÑO" + ano, 1, 1)));
		header.add((new CellExcelVO("CONVENIO AÑO" + ano, 1, 1)));
		header.add((new CellExcelVO("REMESAS ACUMULADAS AÑO" + ano,
				1, 1)));
		header.add((new CellExcelVO("% TRANSFERIDO A " + getMesCurso(true) + "-" + ano, 1, 1)));
		header.add((new CellExcelVO("OBSERVACIÓN", 1, 1)));

		List<ReporteMarcoPresupuestarioEstablecimientoVO> reporteMarcoPresupuestarioEstablecimientoVO21 = this.getReporteMarcoPorEstablecimientoAll(Subtitulo.SUBTITULO21, ano);

		List<ReporteMarcoPresupuestarioEstablecimientoVO> reporteMarcoPresupuestarioEstablecimientoVO22 = this.getReporteMarcoPorEstablecimientoAll(Subtitulo.SUBTITULO22, ano);

		List<ReporteMarcoPresupuestarioEstablecimientoVO> reporteMarcoPresupuestarioEstablecimientoVO29 = this.getReporteMarcoPorEstablecimientoAll(Subtitulo.SUBTITULO29, ano);

		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator + "Planilla Marco Presupuestario por Servicio.xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());

		GeneradorExcel generadorExcel = new GeneradorExcel(filename);

		ReporteMarcoPresupuestarioEstablecimientoSheetExcel reporteMarcoPresupuestarioEstablecimientoSheetExcel21 = new ReporteMarcoPresupuestarioEstablecimientoSheetExcel(
				header, subHeader, null);

		ReporteMarcoPresupuestarioEstablecimientoSheetExcel reporteMarcoPresupuestarioEstablecimientoSheetExcel22 = new ReporteMarcoPresupuestarioEstablecimientoSheetExcel(
				header, subHeader, null);

		ReporteMarcoPresupuestarioEstablecimientoSheetExcel reporteMarcoPresupuestarioEstablecimientoSheetExcel29 = new ReporteMarcoPresupuestarioEstablecimientoSheetExcel(
				header, subHeader, null);

		reporteMarcoPresupuestarioEstablecimientoSheetExcel21.setItems(reporteMarcoPresupuestarioEstablecimientoVO21);
		reporteMarcoPresupuestarioEstablecimientoSheetExcel22.setItems(reporteMarcoPresupuestarioEstablecimientoVO22);
		reporteMarcoPresupuestarioEstablecimientoSheetExcel29.setItems(reporteMarcoPresupuestarioEstablecimientoVO29);

		generadorExcel.addSheet(reporteMarcoPresupuestarioEstablecimientoSheetExcel21, "Subtitulo 21");
		generadorExcel.addSheet(reporteMarcoPresupuestarioEstablecimientoSheetExcel22, "Subtitulo 22");
		generadorExcel.addSheet(reporteMarcoPresupuestarioEstablecimientoSheetExcel29, "Subtitulo 29");

		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderReportes.replace("{ANO}", ano.toString()));
			TipoDocumento tipoDocumento = new TipoDocumento(TipoDocumentosProcesos.REPORTEMARCOPRESPUESTARIOSERVICIO.getId());
			planillaTrabajoId = documentService.createDocumentReportes(tipoDocumento, response.getNodeRef(), response.getFileName(), contenType, ano, Integer.parseInt(getMesCurso(true)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return planillaTrabajoId;
	}

	public List<ReporteMarcoPresupuestarioComunaVO> getReporteMarcoPorComunaAll(Subtitulo subtitulo, Integer ano) {
		List<ReporteMarcoPresupuestarioComunaVO> resultado = new ArrayList<ReporteMarcoPresupuestarioComunaVO>();
		List<ProgramaVO> programasVO = programasService.getProgramasByAnoSubtitulo(ano, subtitulo);

		for (ProgramaVO programa : programasVO) {
			List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
			for (ServicioSalud servicio : servicios) {
				Comuna comunaAuxiliar = comunaDAO.getComunaServicioAuxiliar(servicio.getId());
				System.out.println("comunaAuxiliar --> " +  ((comunaAuxiliar == null) ? null : comunaAuxiliar.getNombre()));
				List<Comuna> comunas = servicio.getComunas();
				List<ComponentesVO> componentes = programasService.getComponenteByProgramaSubtitulos(programa.getId(), subtitulo);
				for (Comuna comuna : comunas) {
					for (ComponentesVO componenteVO : componentes) {
						ReporteMarcoPresupuestarioComunaVO reporteMarcoPresupuestarioComunaVO = new ReporteMarcoPresupuestarioComunaVO();

						reporteMarcoPresupuestarioComunaVO.setServicio(servicio.getNombre());
						reporteMarcoPresupuestarioComunaVO.setPrograma(programa.getNombre());
						reporteMarcoPresupuestarioComunaVO.setComponente(componenteVO.getNombre());
						reporteMarcoPresupuestarioComunaVO.setComuna(comuna.getNombre());

						Long tarifaAnoActual = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), programa.getIdProgramaAno(), componenteVO.getId(), subtitulo.getId());

						Integer mesActual = Integer.parseInt(getMesCurso(true));
						reporteMarcoPresupuestarioComunaVO.setMarco(tarifaAnoActual);

						reporteMarcoPresupuestarioComunaVO.setConvenios(0L);

						Long totalRemesasAcumuladasMesActual = remesasDAO.getRemesasPagadasComunaProgramaSubtitulo(programa.getIdProgramaAno(),	subtitulo.getId(), comuna.getId(), mesActual);
						if (totalRemesasAcumuladasMesActual > 0L) {
							System.out.println("Marco --> "	+ reporteMarcoPresupuestarioComunaVO.getMarco());
							System.out.println("totalRemesasAcumuladasMesActual --> " + totalRemesasAcumuladasMesActual);

							Double porcentajeRemesasPagadas = (totalRemesasAcumuladasMesActual * 100.0)	/ reporteMarcoPresupuestarioComunaVO.getMarco();
							porcentajeRemesasPagadas = porcentajeRemesasPagadas / 100.0;
							System.out.println("porcentajeRemesasPagadas --> " + porcentajeRemesasPagadas);

							reporteMarcoPresupuestarioComunaVO.setRemesasAcumuladas(totalRemesasAcumuladasMesActual);
							reporteMarcoPresupuestarioComunaVO.setPorcentajeCuotaTransferida(porcentajeRemesasPagadas);
						} else {
							reporteMarcoPresupuestarioComunaVO.setRemesasAcumuladas(0L);
							reporteMarcoPresupuestarioComunaVO.setPorcentajeCuotaTransferida(0.0);
						}

						Long tarifaComunaAux = 0L;
						if(comunaAuxiliar != null){
							tarifaComunaAux = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comunaAuxiliar.getId(), programa.getIdProgramaAno(), componenteVO.getId(), subtitulo.getId());
						}

						System.out.println("tarifaComunaAux --> "+tarifaComunaAux);

						if (tarifaComunaAux == 0L) {
							reporteMarcoPresupuestarioComunaVO.setObservacion("");
						} else {
							reporteMarcoPresupuestarioComunaVO
							.setObservacion("Existen recursos no distribuidos en este programa");
						}

						resultado.add(reporteMarcoPresupuestarioComunaVO);
					}
				}
			}
		}
		return resultado;
	}

	public List<ReporteMarcoPresupuestarioEstablecimientoVO> getReporteMarcoPorEstablecimientoAll(Subtitulo subtitulo, Integer ano) {
		List<ReporteMarcoPresupuestarioEstablecimientoVO> resultado = new ArrayList<ReporteMarcoPresupuestarioEstablecimientoVO>();
		List<ProgramaVO> programasVO = programasService.getProgramasByAnoSubtitulo(ano, subtitulo);

		for (ProgramaVO programa : programasVO) {
			List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
			for (ServicioSalud servicio : servicios) {
				Establecimiento auxiliar = establecimientosDAO.getEstablecimientoServicioAuxiliar(servicio.getId());
				System.out.println("establecimiento auxiliar --> "+ ((auxiliar == null) ? null : auxiliar.getNombre()));

				List<Establecimiento> establecimientos = servicio.getEstablecimientos();
				List<ComponentesVO> componentes = programasService.getComponenteByProgramaSubtitulos(programa.getId(), subtitulo);

				for (Establecimiento establecimiento : establecimientos) {
					for (ComponentesVO componenteVO : componentes) {
						ReporteMarcoPresupuestarioEstablecimientoVO reporteMarcoPresupuestarioEstablecimientoVO = new ReporteMarcoPresupuestarioEstablecimientoVO();

						reporteMarcoPresupuestarioEstablecimientoVO.setServicio(servicio.getNombre());
						reporteMarcoPresupuestarioEstablecimientoVO.setPrograma(programa.getNombre());
						reporteMarcoPresupuestarioEstablecimientoVO.setComponente(componenteVO.getNombre());
						reporteMarcoPresupuestarioEstablecimientoVO.setEstablecimiento(establecimiento.getNombre());

						Long marcoAnoActual = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), programa.getIdProgramaAno(), componenteVO.getId(), subtitulo.getId());
						System.out.println("marcoAnoActual --> " + marcoAnoActual);

						reporteMarcoPresupuestarioEstablecimientoVO.setMarco(marcoAnoActual);
						reporteMarcoPresupuestarioEstablecimientoVO.setConvenios(0L);
						reporteMarcoPresupuestarioEstablecimientoVO.setRemesasAcumuladas(0L);

						Integer mesActual = Integer.parseInt(getMesCurso(true));
						Long totalRemesasAcumuladasMesActual = 0L;

						totalRemesasAcumuladasMesActual = remesasDAO.getRemesasPagadasEstablecimientoProgramaSubtitulo(programa.getIdProgramaAno(), subtitulo.getId(),
								establecimiento.getId(), mesActual);
						System.out.println("totalRemesasAcumuladasMesActual --> "+ totalRemesasAcumuladasMesActual);

						reporteMarcoPresupuestarioEstablecimientoVO.setRemesasAcumuladas(totalRemesasAcumuladasMesActual);
						Double porcentajeRemesa = 0.0;

						if(marcoAnoActual != 0){
							porcentajeRemesa = ((totalRemesasAcumuladasMesActual * 100.0) / marcoAnoActual) / 100;
						}

						reporteMarcoPresupuestarioEstablecimientoVO.setPorcentajeCuotaTransferida(porcentajeRemesa);

						Long tarifaEstablecimientoAuxiliar = 0L;
						if(auxiliar != null){
							tarifaEstablecimientoAuxiliar = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(
									auxiliar.getId(), programa.getIdProgramaAno(), componenteVO.getId(), subtitulo.getId());
						}

						if (tarifaEstablecimientoAuxiliar == 0) {
							System.out.println("el establecimiento auxiliar no tiene recursos asignados");
							reporteMarcoPresupuestarioEstablecimientoVO.setObservacion("");
						} else {
							reporteMarcoPresupuestarioEstablecimientoVO.setObservacion("Existen recursos no distribuidos en este programa");
						}
						resultado.add(reporteMarcoPresupuestarioEstablecimientoVO);
					}
				}
			}
		}
		return resultado;
	}

	public List<ReporteMonitoreoProgramaPorComunaVO> getReporteMonitoreoPorComunaAll(Subtitulo subtitulo, Integer ano) {
		List<ReporteMonitoreoProgramaPorComunaVO> resultado = new ArrayList<ReporteMonitoreoProgramaPorComunaVO>();
		List<ProgramaVO> programas = programasService.getProgramasByAnoSubtitulo(ano, subtitulo);
		for (ProgramaVO programa : programas) {
			List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
			List<ComponentesVO> componentes = programasService.getComponenteByProgramaSubtitulos(programa.getId(), subtitulo);
			for (ComponentesVO componente : componentes) {
				for (ServicioSalud servicio : servicios) {
					for (Comuna comuna : servicio.getComunas()) {
						ReporteMonitoreoProgramaPorComunaVO reporteMonitoreoProgramaPorComunaVO = new ReporteMonitoreoProgramaPorComunaVO();
						reporteMonitoreoProgramaPorComunaVO.setServicio(servicio.getNombre());
						reporteMonitoreoProgramaPorComunaVO.setPrograma(programa.getNombre());
						reporteMonitoreoProgramaPorComunaVO.setComponente(componente.getNombre());
						reporteMonitoreoProgramaPorComunaVO.setComuna(comuna.getNombre());

						Long tarifaAnoActual = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), programa.getIdProgramaAno(), componente.getId(), subtitulo.getId());
						Integer mesActual = Integer.parseInt(getMesCurso(true));
						reporteMonitoreoProgramaPorComunaVO.setMarco(tarifaAnoActual);

						Long totalRemesasAcumuladasMesActual = remesasDAO.getRemesasPagadasComunaProgramaSubtitulo(programa.getIdProgramaAno(), subtitulo.getId(), comuna.getId(), mesActual);
						if (totalRemesasAcumuladasMesActual > 0L) {
							System.out.println("Marco --> "	+ reporteMonitoreoProgramaPorComunaVO.getMarco());
							System.out.println("totalRemesasAcumuladasMesActual --> "+ totalRemesasAcumuladasMesActual);

							Long marcoMenosRemesasPagadas = reporteMonitoreoProgramaPorComunaVO.getMarco() - totalRemesasAcumuladasMesActual;
							System.out.println("marcoMenosRemesasPagadas ---> "	+ marcoMenosRemesasPagadas);
							Double porcentajeRemesasPagadas = (totalRemesasAcumuladasMesActual * 100.0)	/ reporteMonitoreoProgramaPorComunaVO.getMarco();
							porcentajeRemesasPagadas = porcentajeRemesasPagadas / 100.0;
							System.out.println("porcentajeRemesasPagadas --> "	+ porcentajeRemesasPagadas);

							reporteMonitoreoProgramaPorComunaVO.setRemesa_monto(totalRemesasAcumuladasMesActual);
							reporteMonitoreoProgramaPorComunaVO.setRemesa_porcentaje(porcentajeRemesasPagadas);
						} else {
							reporteMonitoreoProgramaPorComunaVO.setRemesa_monto(0L);
							reporteMonitoreoProgramaPorComunaVO.setRemesa_porcentaje(0.0);
						}
						reporteMonitoreoProgramaPorComunaVO.setConvenio_monto(0L);
						reporteMonitoreoProgramaPorComunaVO.setConvenio_porcentaje(1.0);
						reporteMonitoreoProgramaPorComunaVO.setConvenio_pendiente((long) 0);
						resultado.add(reporteMonitoreoProgramaPorComunaVO);
					}
				}
			}
		}
		return resultado;
	}

	public List<ReporteMonitoreoProgramaPorComunaVO> getReporteMonitoreoPorComunaFiltroServicioPrograma(Integer idProgramaAno, Integer idComponente, Integer idServicio, Subtitulo subtitulo) {
		System.out.println("getReporteMonitoreoPorComunaFiltroServicioPrograma idProgramaAno="+idProgramaAno);
		System.out.println("getReporteMonitoreoPorComunaFiltroServicioPrograma idComponente="+idComponente);
		System.out.println("getReporteMonitoreoPorComunaFiltroServicioPrograma idServicio="+idServicio);
		List<ReporteMonitoreoProgramaPorComunaVO> resultado = new ArrayList<ReporteMonitoreoProgramaPorComunaVO>();
		ProgramaVO programa = programasService.getProgramaAno(idProgramaAno);
		ComponentesVO componente = componenteService.getComponenteById(idComponente);
		
		List<ServicioSalud> servicios = null;
		
		if(idServicio == null){
			servicios = servicioSaludDAO.getServiciosOrderId();
		}else{
			servicios = new ArrayList<ServicioSalud>();
			ServicioSalud servicioSalud = servicioSaludDAO.getById(idServicio);
			servicios.add(servicioSalud);
		}
			
		for(ServicioSalud servicio : servicios){	
			if(servicio.getComunas() != null && servicio.getComunas().size() > 0){
				for (Comuna comuna : servicio.getComunas()) {
					ReporteMonitoreoProgramaPorComunaVO reporteMonitoreoProgramaPorComunaVO = new ReporteMonitoreoProgramaPorComunaVO();
					reporteMonitoreoProgramaPorComunaVO.setServicio(servicio.getNombre());
					reporteMonitoreoProgramaPorComunaVO.setPrograma(programa.getNombre());
					reporteMonitoreoProgramaPorComunaVO.setComponente(componente.getNombre());
					reporteMonitoreoProgramaPorComunaVO.setComuna(comuna.getNombre());
	
					Long tarifaAnoActual = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAno, componente.getId(), subtitulo.getId());
					Integer mesActual = Integer.parseInt(getMesCurso(true));
					reporteMonitoreoProgramaPorComunaVO.setMarco(tarifaAnoActual);
					reporteMonitoreoProgramaPorComunaVO.setRemesa_monto(0L);
					reporteMonitoreoProgramaPorComunaVO.setRemesa_porcentaje(0.0);
					Long totalRemesasAcumuladasMesActual = 0L;
					reporteMonitoreoProgramaPorComunaVO.setRemesa_monto(totalRemesasAcumuladasMesActual);
					totalRemesasAcumuladasMesActual = remesasDAO.getRemesasPagadasComunaProgramaComponenteSubtitulo(idProgramaAno, componente.getId(), subtitulo.getId(), comuna.getId(), mesActual);
					if (totalRemesasAcumuladasMesActual > 0L && reporteMonitoreoProgramaPorComunaVO.getMarco() > 0) {
						System.out.println("Marco --> " + reporteMonitoreoProgramaPorComunaVO.getMarco());
						System.out.println("totalRemesasAcumuladasMesActual --> " + totalRemesasAcumuladasMesActual);
	
						Long marcoMenosRemesasPagadas = reporteMonitoreoProgramaPorComunaVO.getMarco() - totalRemesasAcumuladasMesActual;
						System.out.println("marcoMenosRemesasPagadas ---> " + marcoMenosRemesasPagadas);
						Double porcentajeRemesasPagadas = (totalRemesasAcumuladasMesActual * 100.0)	/ reporteMonitoreoProgramaPorComunaVO.getMarco();
						porcentajeRemesasPagadas = porcentajeRemesasPagadas / 100.0;
						System.out.println("porcentajeRemesasPagadas --> " + porcentajeRemesasPagadas);
	
						reporteMonitoreoProgramaPorComunaVO.setRemesa_monto(totalRemesasAcumuladasMesActual);
						reporteMonitoreoProgramaPorComunaVO.setRemesa_porcentaje(porcentajeRemesasPagadas);
					}
					reporteMonitoreoProgramaPorComunaVO.setConvenio_monto(0L);
					reporteMonitoreoProgramaPorComunaVO.setConvenio_porcentaje(0.0);
					reporteMonitoreoProgramaPorComunaVO.setConvenio_pendiente(reporteMonitoreoProgramaPorComunaVO.getMarco() - reporteMonitoreoProgramaPorComunaVO.getConvenio_monto());
					resultado.add(reporteMonitoreoProgramaPorComunaVO);
				}
			}
		}
		return resultado;
	}

	public List<ReporteMonitoreoProgramaPorEstablecimientoVO> getReporteMonitoreoPorEstablecimientoAll(Subtitulo subtitulo, Integer ano) {
		List<ReporteMonitoreoProgramaPorEstablecimientoVO> resultado = new ArrayList<ReporteMonitoreoProgramaPorEstablecimientoVO>();
		List<ProgramaVO> programas = programasService.getProgramasByAnoSubtitulo(ano, subtitulo);
		for (ProgramaVO programa : programas) {

			List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
			for (ServicioSalud servicio : servicios) {
				for (ComponentesVO componente : programa.getComponentes()) {
					for (Establecimiento establecimiento : servicio.getEstablecimientos()) {

						ReporteMonitoreoProgramaPorEstablecimientoVO reporteMonitoreoProgramaPorEstablecimientoVO = new ReporteMonitoreoProgramaPorEstablecimientoVO();
						reporteMonitoreoProgramaPorEstablecimientoVO
						.setServicio(servicio.getNombre());
						reporteMonitoreoProgramaPorEstablecimientoVO
						.setEstablecimiento(establecimiento.getNombre());
						reporteMonitoreoProgramaPorEstablecimientoVO
						.setPrograma(programa.getNombre());
						reporteMonitoreoProgramaPorEstablecimientoVO
						.setComponente(componente.getNombre());

						Long marcoAnoActual = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), programa.getIdProgramaAno(), componente.getId(), subtitulo.getId());
						System.out.println("marcoAnoActual --> " + marcoAnoActual);
						reporteMonitoreoProgramaPorEstablecimientoVO.setMarco(marcoAnoActual);

						Integer mesActual = Integer.parseInt(getMesCurso(true));
						Long totalRemesasAcumuladasMesActual = 0L;

						totalRemesasAcumuladasMesActual = remesasDAO.getRemesasPagadasEstablecimientoProgramaSubtitulo(programa.getIdProgramaAno(), subtitulo.getId(), establecimiento.getId(), mesActual);
						System.out.println("totalRemesasAcumuladasMesActual --> " + totalRemesasAcumuladasMesActual);

						reporteMonitoreoProgramaPorEstablecimientoVO.setRemesa_monto(totalRemesasAcumuladasMesActual);

						Double porcentajeRemesa = ((totalRemesasAcumuladasMesActual * 100.0) / marcoAnoActual) / 100;
						System.out.println("porcentajeRemesa --> " + porcentajeRemesa);

						if (marcoAnoActual == 0) {
							reporteMonitoreoProgramaPorEstablecimientoVO.setRemesa_porcentaje(0.0);
						} else {
							reporteMonitoreoProgramaPorEstablecimientoVO.setRemesa_porcentaje(porcentajeRemesa);
						}

						reporteMonitoreoProgramaPorEstablecimientoVO.setConvenio_monto(0L);
						reporteMonitoreoProgramaPorEstablecimientoVO.setConvenio_porcentaje(0.0);
						reporteMonitoreoProgramaPorEstablecimientoVO.setConvenio_pendiente(0L);
						resultado.add(reporteMonitoreoProgramaPorEstablecimientoVO);
					}
				}
			}
		}
		return resultado;
	}

	public List<ReporteMonitoreoProgramaPorEstablecimientoVO> getReporteMonitoreoPorEstablecimientoFiltroPrograma(Integer idProgramaAno, Subtitulo subtitulo) {
		List<ReporteMonitoreoProgramaPorEstablecimientoVO> resultado = new ArrayList<ReporteMonitoreoProgramaPorEstablecimientoVO>();
		ProgramaVO programa = programasService.getProgramaAno(idProgramaAno);
		List<ComponentesVO> componentes = programasService.getComponenteByProgramaSubtitulos(programa.getId(), subtitulo);
		for (ComponentesVO componente : componentes) {
			List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
			for (ServicioSalud servicio : servicios) {
				ReporteMonitoreoProgramaPorEstablecimientoVO reporteMonitoreoProgramaPorEstablecimientoVO = new ReporteMonitoreoProgramaPorEstablecimientoVO();
				reporteMonitoreoProgramaPorEstablecimientoVO.setServicio(servicio.getNombre());
				reporteMonitoreoProgramaPorEstablecimientoVO.setPrograma(programa.getNombre());
				reporteMonitoreoProgramaPorEstablecimientoVO.setComponente(componente.getNombre());

				reporteMonitoreoProgramaPorEstablecimientoVO.setMarco((long) subtitulo.getId() * programa.getIdProgramaAno() * servicio.getId() * 4175 + 950678);
				reporteMonitoreoProgramaPorEstablecimientoVO.setRemesa_monto((long) (9081 + subtitulo.getId() * servicio.getId() * 103));
				reporteMonitoreoProgramaPorEstablecimientoVO.setRemesa_porcentaje(1.0);
				reporteMonitoreoProgramaPorEstablecimientoVO.setConvenio_monto((long) (15081 + subtitulo.getId() * servicio.getId() * 523));
				reporteMonitoreoProgramaPorEstablecimientoVO.setConvenio_porcentaje(1.0);
				reporteMonitoreoProgramaPorEstablecimientoVO.setConvenio_pendiente((long) 0);
				resultado.add(reporteMonitoreoProgramaPorEstablecimientoVO);
			}
		}
		return resultado;
	}

	public List<ReporteMonitoreoProgramaPorEstablecimientoVO> getReporteMonitoreoPorEstablecimientoFiltroServicioPrograma(Integer idProgramaAno, Integer idComponente, Integer idServicio, Subtitulo subtitulo) {
		System.out.println("getReporteMonitoreoPorEstablecimientoFiltroServicioPrograma idProgramaAno="+idProgramaAno);
		System.out.println("getReporteMonitoreoPorEstablecimientoFiltroServicioPrograma idComponente="+idComponente);
		System.out.println("getReporteMonitoreoPorEstablecimientoFiltroServicioPrograma idServicio="+idServicio);
		List<ReporteMonitoreoProgramaPorEstablecimientoVO> resultado = new ArrayList<ReporteMonitoreoProgramaPorEstablecimientoVO>();
		ProgramaVO programa = programasService.getProgramaAno(idProgramaAno);

		ComponentesVO componente = componenteService.getComponenteById(idComponente);
		
		List<ServicioSalud> servicios = null;
		
		if(idServicio == null){
			servicios = servicioSaludDAO.getServiciosOrderId();
		}else{
			servicios = new ArrayList<ServicioSalud>();
			ServicioSalud servicioSalud = servicioSaludDAO.getById(idServicio);
			servicios.add(servicioSalud);
		}

		for(ServicioSalud servicio : servicios){
			if(servicio.getEstablecimientos() != null && servicio.getEstablecimientos().size() > 0){
				for (Establecimiento establecimiento : servicio.getEstablecimientos()) {
					ReporteMonitoreoProgramaPorEstablecimientoVO reporteMonitoreoProgramaPorEstablecimientoVO = new ReporteMonitoreoProgramaPorEstablecimientoVO();
					reporteMonitoreoProgramaPorEstablecimientoVO.setServicio(servicio.getNombre());
					reporteMonitoreoProgramaPorEstablecimientoVO.setEstablecimiento(establecimiento.getNombre());
					reporteMonitoreoProgramaPorEstablecimientoVO.setPrograma(programa.getNombre());
					reporteMonitoreoProgramaPorEstablecimientoVO.setComponente(componente.getNombre());
	
					Long marcoAnoActual = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAno, componente.getId(), subtitulo.getId());
					Integer mesActual = Integer.parseInt(getMesCurso(true));
					System.out.println("marcoAnoActual --> " + marcoAnoActual);
					reporteMonitoreoProgramaPorEstablecimientoVO.setMarco(marcoAnoActual);
					reporteMonitoreoProgramaPorEstablecimientoVO.setRemesa_monto(0L);
					reporteMonitoreoProgramaPorEstablecimientoVO.setRemesa_porcentaje(0.0);
					Long totalRemesasAcumuladasMesActual = remesasDAO.getRemesasPagadasEstablecimientoProgramaComponenteSubtitulo(idProgramaAno, componente.getId(), subtitulo.getId(), establecimiento.getId(), mesActual);
					System.out.println("totalRemesasAcumuladasMesActual --> " + totalRemesasAcumuladasMesActual);
					
					if (totalRemesasAcumuladasMesActual > 0L && reporteMonitoreoProgramaPorEstablecimientoVO.getMarco() > 0) {
						System.out.println("Marco --> " + reporteMonitoreoProgramaPorEstablecimientoVO.getMarco());
						System.out.println("totalRemesasAcumuladasMesActual --> " + totalRemesasAcumuladasMesActual);
	
						Long marcoMenosRemesasPagadas = reporteMonitoreoProgramaPorEstablecimientoVO.getMarco() - totalRemesasAcumuladasMesActual;
						System.out.println("marcoMenosRemesasPagadas ---> " + marcoMenosRemesasPagadas);
						Double porcentajeRemesasPagadas = (totalRemesasAcumuladasMesActual * 100.0)	/ reporteMonitoreoProgramaPorEstablecimientoVO.getMarco();
						porcentajeRemesasPagadas = porcentajeRemesasPagadas / 100.0;
						System.out.println("porcentajeRemesasPagadas --> " + porcentajeRemesasPagadas);
	
						reporteMonitoreoProgramaPorEstablecimientoVO.setRemesa_monto(totalRemesasAcumuladasMesActual);
						reporteMonitoreoProgramaPorEstablecimientoVO.setRemesa_porcentaje(porcentajeRemesasPagadas);
					}
	
					reporteMonitoreoProgramaPorEstablecimientoVO.setRemesa_monto(totalRemesasAcumuladasMesActual);
	
	
					reporteMonitoreoProgramaPorEstablecimientoVO.setConvenio_monto(0L);
					reporteMonitoreoProgramaPorEstablecimientoVO.setConvenio_porcentaje(0.0);
					reporteMonitoreoProgramaPorEstablecimientoVO.setConvenio_pendiente(reporteMonitoreoProgramaPorEstablecimientoVO.getMarco() - reporteMonitoreoProgramaPorEstablecimientoVO.getConvenio_monto());
	
					resultado.add(reporteMonitoreoProgramaPorEstablecimientoVO);
				}
			}
		}
		return resultado;
	}

	public List<ReporteEstadoSituacionByComunaVO> getReporteEstadoSituacionByComunaAll(Subtitulo subtitulo, Integer ano) {
		List<ReporteEstadoSituacionByComunaVO> resultado = new ArrayList<ReporteEstadoSituacionByComunaVO>();
		List<ProgramaVO> programas = programasService.getProgramasByAnoSubtitulo(ano, subtitulo);
		for (ProgramaVO programa : programas) {
			List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
			for (ServicioSalud servicio : servicios) {
				for (Comuna comuna : servicio.getComunas()) {
					List<ComponentesVO> componentes = programasService.getComponenteByProgramaSubtitulos(programa.getId(), subtitulo);
					for (ComponentesVO componenteVO : componentes) {
						ReporteEstadoSituacionByComunaVO reporteEstadoSituacionByComunaVO = new ReporteEstadoSituacionByComunaVO();

						reporteEstadoSituacionByComunaVO.setPrograma(programa.getNombre());
						reporteEstadoSituacionByComunaVO.setServicio(servicio.getNombre());
						reporteEstadoSituacionByComunaVO.setComuna(comuna.getNombre());
						reporteEstadoSituacionByComunaVO.setComponente(componenteVO.getNombre());

						Long tarifaAnteriorAnoActual = 0L;

						tarifaAnteriorAnoActual = programasDAO.getTarifaAnteriorComunaProgramaAnoComponenteSubtitulo(comuna.getId(), programa.getIdProgramaAno(), componenteVO.getId(), subtitulo.getId());
						Long tarifaAnoActual = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), programa.getIdProgramaAno(), componenteVO.getId(), subtitulo.getId());

						if(tarifaAnteriorAnoActual > 0){
							//el marco fue modificado
							reporteEstadoSituacionByComunaVO.setMarco_modificado(tarifaAnoActual);
							reporteEstadoSituacionByComunaVO.setMarco_inicial(tarifaAnteriorAnoActual);
						}else{
							//no tiene modificaciones
							reporteEstadoSituacionByComunaVO.setMarco_inicial(tarifaAnoActual);
							reporteEstadoSituacionByComunaVO.setMarco_modificado(tarifaAnoActual);
						}

						reporteEstadoSituacionByComunaVO.setConvenioRecibido_monto(0L);
						reporteEstadoSituacionByComunaVO.setConvenioRecibido_porcentaje(0.0);
						reporteEstadoSituacionByComunaVO.setConvenioPendiente_monto(0L);
						reporteEstadoSituacionByComunaVO.setConvenioPendiente_porcentaje(0.0);

						Integer mesActual = Integer.parseInt(getMesCurso(true));
						Long totalRemesasAcumuladasMesActual = remesasDAO.getRemesasPagadasComunaProgramaSubtitulo(programa.getIdProgramaAno(), subtitulo.getId(), comuna.getId(), mesActual);
						Long totalRemesasNoPagadasMesActual = remesasDAO.getRemesasNoPagadasComunaProgramaSubtituloMesHasta(programa.getIdProgramaAno(), subtitulo.getId(), comuna.getId(), mesActual);

						Double porcentajeRemesasPagadas = (totalRemesasAcumuladasMesActual * 100.0) / reporteEstadoSituacionByComunaVO.getMarco_inicial();
						Double porcentajeRemesasNoPagadas = (totalRemesasNoPagadasMesActual * 100.0) / reporteEstadoSituacionByComunaVO.getMarco_inicial();

						reporteEstadoSituacionByComunaVO.setRemesaAcumulada_monto(totalRemesasAcumuladasMesActual);
						reporteEstadoSituacionByComunaVO.setRemesaAcumulada_porcentaje(porcentajeRemesasPagadas);
						reporteEstadoSituacionByComunaVO.setRemesaPendiente_monto(totalRemesasNoPagadasMesActual);
						reporteEstadoSituacionByComunaVO.setRemesaPendiente_porcentaje(porcentajeRemesasNoPagadas);
						reporteEstadoSituacionByComunaVO.setReliquidacion_monto(0L);
						reporteEstadoSituacionByComunaVO.setReliquidacion_porcentaje(0.0);
						reporteEstadoSituacionByComunaVO.setIncremento(reporteEstadoSituacionByComunaVO.getMarco_modificado()- reporteEstadoSituacionByComunaVO.getMarco_inicial());

						resultado.add(reporteEstadoSituacionByComunaVO);
					}
				}
			}
		}
		return resultado;
	}

	public List<ReporteEstadoSituacionByComunaVO> getReporteEstadoSituacionByComunaFiltroPrograma(Integer idProgramaAno, Subtitulo subtitulo) {
		List<ReporteEstadoSituacionByComunaVO> resultado = new ArrayList<ReporteEstadoSituacionByComunaVO>();
		ProgramaVO programa = programasService.getProgramaAno(idProgramaAno);
		List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
		for (ServicioSalud servicio : servicios) {
			for (Comuna comuna : servicio.getComunas()) {
				ReporteEstadoSituacionByComunaVO reporteEstadoSituacionByComunaVO = new ReporteEstadoSituacionByComunaVO();
				reporteEstadoSituacionByComunaVO.setPrograma(programa.getNombre());
				reporteEstadoSituacionByComunaVO.setServicio(servicio.getNombre());
				reporteEstadoSituacionByComunaVO.setComuna(comuna.getNombre());
				reporteEstadoSituacionByComunaVO.setMarco_inicial((long) subtitulo.getId() * programa.getIdProgramaAno() * comuna.getId() * 17);
				reporteEstadoSituacionByComunaVO.setMarco_modificado((long) subtitulo.getId() * programa.getIdProgramaAno() * comuna.getId() * 55);
				reporteEstadoSituacionByComunaVO.setConvenioRecibido_monto((long) (15081 + subtitulo.getId() * comuna.getId() * 123));
				reporteEstadoSituacionByComunaVO.setConvenioRecibido_porcentaje(1.0);
				reporteEstadoSituacionByComunaVO.setConvenioPendiente_monto(0L);
				reporteEstadoSituacionByComunaVO.setConvenioPendiente_porcentaje(0.0);
				reporteEstadoSituacionByComunaVO.setRemesaAcumulada_monto((long) (9081 + subtitulo.getId() * comuna.getId() * 1103));
				reporteEstadoSituacionByComunaVO.setRemesaAcumulada_porcentaje(1.0);
				reporteEstadoSituacionByComunaVO.setRemesaPendiente_monto(0L);
				reporteEstadoSituacionByComunaVO.setRemesaPendiente_porcentaje(0.0);
				reporteEstadoSituacionByComunaVO.setReliquidacion_monto((long) programa.getIdProgramaAno() * comuna.getId() * 7);
				reporteEstadoSituacionByComunaVO.setReliquidacion_porcentaje(0.2);
				reporteEstadoSituacionByComunaVO.setIncremento(reporteEstadoSituacionByComunaVO.getMarco_modificado() - reporteEstadoSituacionByComunaVO.getMarco_inicial());
				resultado.add(reporteEstadoSituacionByComunaVO);
			}
		}
		return resultado;
	}

	public List<ReporteEstadoSituacionByComunaVO> getReporteEstadoSituacionByComunaFiltroProgramaServicioComuna(
			Integer idProgramaAno, Integer idServicio, Integer idComuna,
			Subtitulo subtitulo) {
		System.out.println("entra al metodo");

		List<ReporteEstadoSituacionByComunaVO> resultado = new ArrayList<ReporteEstadoSituacionByComunaVO>();
		ProgramaVO programa = programasService.getProgramaAno(idProgramaAno);
		ServicioSalud servicio = servicioSaludDAO.getServicioSaludPorID(idServicio);
		Comuna comuna = comunaDAO.getComunaById(idComuna);
		List<ComponentesVO> componentes = programasService.getComponenteByProgramaSubtitulos(programa.getId(), subtitulo);

		for (ComponentesVO componenteVO : componentes) {
			ReporteEstadoSituacionByComunaVO reporteEstadoSituacionByComunaVO = new ReporteEstadoSituacionByComunaVO();

			reporteEstadoSituacionByComunaVO.setPrograma(programa.getNombre());
			reporteEstadoSituacionByComunaVO.setServicio(servicio.getNombre());
			reporteEstadoSituacionByComunaVO.setComuna(comuna.getNombre());
			reporteEstadoSituacionByComunaVO.setComponente(componenteVO.getNombre());

			Long tarifaAnteriorAnoActual = 0L;

			tarifaAnteriorAnoActual = programasDAO.getTarifaAnteriorComunaProgramaAnoComponenteSubtitulo(comuna.getId(), programa.getIdProgramaAno(), componenteVO.getId(), subtitulo.getId());
			Long tarifaAnoActual = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), programa.getIdProgramaAno(), componenteVO.getId(), subtitulo.getId());

			if(tarifaAnteriorAnoActual > 0){
				//el marco fue modificado
				reporteEstadoSituacionByComunaVO.setMarco_modificado(tarifaAnoActual);
				reporteEstadoSituacionByComunaVO.setMarco_inicial(tarifaAnteriorAnoActual);
			}else{
				//no tiene modificaciones
				reporteEstadoSituacionByComunaVO.setMarco_inicial(tarifaAnoActual);
				reporteEstadoSituacionByComunaVO.setMarco_modificado(tarifaAnoActual);
			}
			reporteEstadoSituacionByComunaVO.setConvenioRecibido_monto(0L);
			reporteEstadoSituacionByComunaVO.setConvenioRecibido_porcentaje(0.0);
			reporteEstadoSituacionByComunaVO.setConvenioPendiente_monto(0L);
			reporteEstadoSituacionByComunaVO.setConvenioPendiente_porcentaje(0.0);

			Integer mesActual = Integer.parseInt(getMesCurso(true));
			Long totalRemesasAcumuladasMesActual = remesasDAO.getRemesasPagadasComunaProgramaSubtitulo(programa.getIdProgramaAno(), subtitulo.getId(), comuna.getId(), mesActual);
			Long totalRemesasNoPagadasMesActual = remesasDAO.getRemesasNoPagadasComunaProgramaSubtituloMesHasta(programa.getIdProgramaAno(), subtitulo.getId(), comuna.getId(), mesActual);

			Double porcentajeRemesasPagadas = (totalRemesasAcumuladasMesActual * 100.0) / reporteEstadoSituacionByComunaVO.getMarco_inicial();
			Double porcentajeRemesasNoPagadas = (totalRemesasNoPagadasMesActual * 100.0) / reporteEstadoSituacionByComunaVO.getMarco_inicial();

			reporteEstadoSituacionByComunaVO.setRemesaAcumulada_monto(totalRemesasAcumuladasMesActual);
			reporteEstadoSituacionByComunaVO.setRemesaAcumulada_porcentaje(porcentajeRemesasPagadas);
			reporteEstadoSituacionByComunaVO.setRemesaPendiente_monto(totalRemesasNoPagadasMesActual);
			reporteEstadoSituacionByComunaVO.setRemesaPendiente_porcentaje(porcentajeRemesasNoPagadas);
			reporteEstadoSituacionByComunaVO.setReliquidacion_monto(0L);
			reporteEstadoSituacionByComunaVO.setReliquidacion_porcentaje(0.0);
			reporteEstadoSituacionByComunaVO.setIncremento(reporteEstadoSituacionByComunaVO.getMarco_modificado()- reporteEstadoSituacionByComunaVO.getMarco_inicial());

			resultado.add(reporteEstadoSituacionByComunaVO);
		}
		return resultado;
	}

	public List<ReporteEstadoSituacionByServiciosVO> getReporteEstadoSituacionByServicioAll(Subtitulo subtitulo, Integer ano) {
		List<ReporteEstadoSituacionByServiciosVO> resultado = new ArrayList<ReporteEstadoSituacionByServiciosVO>();
		List<ProgramaVO> programas = programasService.getProgramasByAnoSubtitulo(ano, subtitulo);

		List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
		for (ProgramaVO programa : programas) {
			List<ComponentesVO> componentes = programasService.getComponenteByProgramaSubtitulos(programa.getId(), subtitulo);

			for (ServicioSalud servicio : servicios) {
				for(Establecimiento establecimiento : servicio.getEstablecimientos()){
					for (ComponentesVO componenteVO : componentes) {
						ReporteEstadoSituacionByServiciosVO reporteEstadoSituacionByServiciosVO = new ReporteEstadoSituacionByServiciosVO();

						reporteEstadoSituacionByServiciosVO.setPrograma(programa.getNombre());
						reporteEstadoSituacionByServiciosVO.setServicio(servicio.getNombre());
						reporteEstadoSituacionByServiciosVO.setEstablecimiento(establecimiento.getNombre());
						reporteEstadoSituacionByServiciosVO.setComponente(componenteVO.getNombre());

						Long marcoAnoActual = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(
								establecimiento.getId(), programa.getIdProgramaAno(), componenteVO.getId(), subtitulo.getId());
						System.out.println("marcoAnoActual --> " + marcoAnoActual);

						reporteEstadoSituacionByServiciosVO.setMarco_inicial(marcoAnoActual);

						// TODO ver bien de donde sacar este valor
						reporteEstadoSituacionByServiciosVO.setMarco_modificado(marcoAnoActual);
						reporteEstadoSituacionByServiciosVO.setConvenioRecibido_monto(0L);
						reporteEstadoSituacionByServiciosVO.setConvenioRecibido_porcentaje(0.0);
						reporteEstadoSituacionByServiciosVO.setConvenioPendiente_monto(0L);
						reporteEstadoSituacionByServiciosVO.setConvenioPendiente_porcentaje(0.0);

						Integer mesActual = Integer.parseInt(getMesCurso(true));
						Long totalRemesasAcumuladasMesActual = 0L;
						Long totalRemesasPendientesMesActual = 0L;

						totalRemesasAcumuladasMesActual = remesasDAO.getRemesasPagadasEstablecimientoProgramaSubtitulo(
								programa.getIdProgramaAno(), subtitulo.getId(), establecimiento.getId(), mesActual);
						System.out.println("totalRemesasAcumuladasMesActual --> "+ totalRemesasAcumuladasMesActual);

						Double porcentajeRemesaAcumulada = ((totalRemesasAcumuladasMesActual * 100.0) / marcoAnoActual) / 100;
						System.out.println("porcentajeRemesaAcumulada --> "+ porcentajeRemesaAcumulada);

						totalRemesasPendientesMesActual = remesasDAO.getRemesasNoPagadasEstablecimientoProgramaSubtitulo(
								programa.getIdProgramaAno(), subtitulo.getId(), establecimiento.getId(), mesActual);

						Double porcentajeRemesaPendiente = ((totalRemesasPendientesMesActual * 100.0) / marcoAnoActual) / 100;
						System.out.println("porcentajeRemesaPendiente --> "
								+ porcentajeRemesaPendiente);

						reporteEstadoSituacionByServiciosVO.setRemesaAcumulada_monto(totalRemesasAcumuladasMesActual);
						reporteEstadoSituacionByServiciosVO.setRemesaAcumulada_porcentaje(porcentajeRemesaAcumulada);
						reporteEstadoSituacionByServiciosVO.setRemesaPendiente_monto(totalRemesasPendientesMesActual);
						reporteEstadoSituacionByServiciosVO.setRemesaPendiente_porcentaje(porcentajeRemesaPendiente);
						reporteEstadoSituacionByServiciosVO.setReliquidacion_monto(0L);
						reporteEstadoSituacionByServiciosVO.setReliquidacion_porcentaje(0.0);
						reporteEstadoSituacionByServiciosVO.setIncremento(reporteEstadoSituacionByServiciosVO.getMarco_modificado()	- reporteEstadoSituacionByServiciosVO.getMarco_inicial());
						resultado.add(reporteEstadoSituacionByServiciosVO);
					}
				}
			}
		}
		return resultado;
	}

	public List<ReporteEstadoSituacionByServiciosVO> getReporteEstadoSituacionByServicioFiltroProgramaServicioEstablecimiento(
			Integer idProgramaAno, Integer idServicio,
			Integer idEstablecimiento, Subtitulo subtitulo) {

		System.out.println("idEstablecimiento --> " + idEstablecimiento);

		List<ReporteEstadoSituacionByServiciosVO> resultado = new ArrayList<ReporteEstadoSituacionByServiciosVO>();
		ProgramaVO programa = programasService.getProgramaAno(idProgramaAno);
		ServicioSalud servicio = servicioSaludDAO.getServicioSaludPorID(idServicio);

		Establecimiento establecimiento = establecimientosDAO.getEstablecimientoById(idEstablecimiento);

		List<ComponentesVO> componentes = programasService.getComponenteByProgramaSubtitulos(programa.getId(), subtitulo);

		for (ComponentesVO componenteVO : componentes) {
			ReporteEstadoSituacionByServiciosVO reporteEstadoSituacionByServiciosVO = new ReporteEstadoSituacionByServiciosVO();

			reporteEstadoSituacionByServiciosVO.setPrograma(programa.getNombre());
			reporteEstadoSituacionByServiciosVO.setServicio(servicio.getNombre());
			reporteEstadoSituacionByServiciosVO.setEstablecimiento(establecimiento.getNombre());
			reporteEstadoSituacionByServiciosVO.setComponente(componenteVO.getNombre());

			Long marcoAnoActual = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAno, componenteVO.getId(), subtitulo.getId());
			System.out.println("marcoAnoActual --> " + marcoAnoActual);

			reporteEstadoSituacionByServiciosVO.setMarco_inicial(marcoAnoActual);

			// TODO ver bien de donde sacar este valor
			reporteEstadoSituacionByServiciosVO.setMarco_modificado(marcoAnoActual);
			reporteEstadoSituacionByServiciosVO.setConvenioRecibido_monto(0L);
			reporteEstadoSituacionByServiciosVO.setConvenioRecibido_porcentaje(0.0);
			reporteEstadoSituacionByServiciosVO.setConvenioPendiente_monto(0L);
			reporteEstadoSituacionByServiciosVO.setConvenioPendiente_porcentaje(0.0);

			Integer mesActual = Integer.parseInt(getMesCurso(true));
			Long totalRemesasAcumuladasMesActual = 0L;
			Long totalRemesasPendientesMesActual = 0L;

			totalRemesasAcumuladasMesActual = remesasDAO.getRemesasPagadasEstablecimientoProgramaSubtitulo(idProgramaAno, subtitulo.getId(), idEstablecimiento, mesActual);
			System.out.println("totalRemesasAcumuladasMesActual --> " + totalRemesasAcumuladasMesActual);

			Double porcentajeRemesaAcumulada = ((totalRemesasAcumuladasMesActual * 100.0) / marcoAnoActual) / 100;
			System.out.println("porcentajeRemesaAcumulada --> "	+ porcentajeRemesaAcumulada);

			totalRemesasPendientesMesActual = remesasDAO.getRemesasNoPagadasEstablecimientoProgramaSubtitulo(idProgramaAno, subtitulo.getId(), idEstablecimiento, mesActual);

			Double porcentajeRemesaPendiente = ((totalRemesasPendientesMesActual * 100.0) / marcoAnoActual) / 100;
			System.out.println("porcentajeRemesaPendiente --> "	+ porcentajeRemesaPendiente);

			reporteEstadoSituacionByServiciosVO.setRemesaAcumulada_monto(totalRemesasAcumuladasMesActual);
			reporteEstadoSituacionByServiciosVO.setRemesaAcumulada_porcentaje(porcentajeRemesaAcumulada);
			reporteEstadoSituacionByServiciosVO.setRemesaPendiente_monto(totalRemesasPendientesMesActual);
			reporteEstadoSituacionByServiciosVO.setRemesaPendiente_porcentaje(porcentajeRemesaPendiente);
			reporteEstadoSituacionByServiciosVO.setReliquidacion_monto(0L);
			reporteEstadoSituacionByServiciosVO.setReliquidacion_porcentaje(0.0);
			reporteEstadoSituacionByServiciosVO.setIncremento(reporteEstadoSituacionByServiciosVO.getMarco_modificado()	- reporteEstadoSituacionByServiciosVO.getMarco_inicial());
			resultado.add(reporteEstadoSituacionByServiciosVO);
		}
		return resultado;
	}

	public Integer generarPlanillaReporteEstadoSituacionPorComuna(Integer ano) {
		Integer planillaTrabajoId = null;
		List<CellExcelVO> header = new ArrayList<CellExcelVO>();
		List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
		header.add((new CellExcelVO("PROGRAMA", 1, 2)));
		header.add((new CellExcelVO("SERVICIO", 1, 2)));
		header.add((new CellExcelVO("COMUNA", 1, 2)));
		header.add((new CellExcelVO("COMPONENTE", 1, 2)));
		header.add((new CellExcelVO("MARCO PRESUPUESTARIO INICIAL", 1, 2)));
		header.add((new CellExcelVO("MARCO PRESUPUESTARIO MODIFICADO", 1, 2)));
		header.add((new CellExcelVO("CONVENIOS RECIBIDOS", 2, 1)));
		header.add((new CellExcelVO("CONVENIOS PENDIENTES", 2, 1)));
		header.add((new CellExcelVO("REMESA ACUMULADA", 2, 1)));
		header.add((new CellExcelVO("REMESA PENDIENTE", 2, 1)));
		header.add((new CellExcelVO("RELIQUIDACION", 2, 1)));
		header.add((new CellExcelVO("INCREMENTO", 1, 2)));

		subHeader.add((new CellExcelVO("MONTO", 1, 1)));
		subHeader.add((new CellExcelVO("%", 1, 1)));
		subHeader.add((new CellExcelVO("MONTO", 1, 1)));
		subHeader.add((new CellExcelVO("%", 1, 1)));
		subHeader.add((new CellExcelVO("MONTO", 1, 1)));
		subHeader.add((new CellExcelVO("%", 1, 1)));
		subHeader.add((new CellExcelVO("MONTO", 1, 1)));
		subHeader.add((new CellExcelVO("%", 1, 1)));
		subHeader.add((new CellExcelVO("MONTO", 1, 1)));
		subHeader.add((new CellExcelVO("%", 1, 1)));

		List<ReporteEstadoSituacionByComunaVO> reporteEstadoSituacionByComunaVO = getReporteEstadoSituacionByComunaAll(Subtitulo.SUBTITULO24, ano);

		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator + "Panilla Reporte Estado Situacion Programa - Comuna.xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());

		GeneradorExcel generadorExcel = new GeneradorExcel(filename);

		ReporteEstadoSituacionPorComunaSheetExcel reporteEstadoSituacionPorComunaSheetExcel = new ReporteEstadoSituacionPorComunaSheetExcel(
				header, subHeader, null);

		reporteEstadoSituacionPorComunaSheetExcel
		.setItems(reporteEstadoSituacionByComunaVO);

		generadorExcel.addSheet(reporteEstadoSituacionPorComunaSheetExcel, "Subtitulo 24");

		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderReportes.replace("{ANO}", ano.toString()));
			TipoDocumento tipoDocumento = new TipoDocumento(TipoDocumentosProcesos.REPORTEESTADOSITUACIONPROGRAMABYCOMUNA.getId());
			planillaTrabajoId = documentService.createDocumentReportes(tipoDocumento, response.getNodeRef(), response.getFileName(), contenType, ano, Integer.parseInt(getMesCurso(true)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return planillaTrabajoId;
	}

	public Integer generarPlanillaReporteEstadoSituacionPorEstablecimiento(Integer ano) {
		Integer planillaTrabajoId = null;
		List<CellExcelVO> header = new ArrayList<CellExcelVO>();
		List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
		header.add((new CellExcelVO("PROGRAMA", 1, 2)));
		header.add((new CellExcelVO("SERVICIO", 1, 2)));
		header.add((new CellExcelVO("ESTABLECIMIENTO", 1, 2)));
		header.add((new CellExcelVO("COMPONENTE", 1, 2)));
		header.add((new CellExcelVO("MARCO PRESUPUESTARIO INICIAL", 1, 2)));
		header.add((new CellExcelVO("MARCO PRESUPUESTARIO MODIFICADO", 1, 2)));
		header.add((new CellExcelVO("CONVENIOS RECIBIDOS", 2, 1)));
		header.add((new CellExcelVO("CONVENIOS PENDIENTES", 2, 1)));
		header.add((new CellExcelVO("REMESA ACUMULADA", 2, 1)));
		header.add((new CellExcelVO("REMESA PENDIENTE", 2, 1)));
		header.add((new CellExcelVO("RELIQUIDACION", 2, 1)));
		header.add((new CellExcelVO("INCREMENTO", 1, 2)));

		subHeader.add((new CellExcelVO("MONTO", 1, 1)));
		subHeader.add((new CellExcelVO("%", 1, 1)));
		subHeader.add((new CellExcelVO("MONTO", 1, 1)));
		subHeader.add((new CellExcelVO("%", 1, 1)));
		subHeader.add((new CellExcelVO("MONTO", 1, 1)));
		subHeader.add((new CellExcelVO("%", 1, 1)));
		subHeader.add((new CellExcelVO("MONTO", 1, 1)));
		subHeader.add((new CellExcelVO("%", 1, 1)));
		subHeader.add((new CellExcelVO("MONTO", 1, 1)));
		subHeader.add((new CellExcelVO("%", 1, 1)));

		List<ReporteEstadoSituacionByServiciosVO> reporteEstadoSituacionByServiciosSub21 = getReporteEstadoSituacionByServicioAll(Subtitulo.SUBTITULO21, ano);
		List<ReporteEstadoSituacionByServiciosVO> reporteEstadoSituacionByServiciosSub22 = getReporteEstadoSituacionByServicioAll(Subtitulo.SUBTITULO22, ano);
		List<ReporteEstadoSituacionByServiciosVO> reporteEstadoSituacionByServiciosSub29 = getReporteEstadoSituacionByServicioAll(Subtitulo.SUBTITULO29, ano);


		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator + "Panilla Reporte Estado Situacion Programa - Servicio.xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());

		GeneradorExcel generadorExcel = new GeneradorExcel(filename);

		ReporteEstadoSituacionPorServicioSheetExcel reporteEstadoSituacionPorServicioSheetExcelSub21 = new ReporteEstadoSituacionPorServicioSheetExcel(header, subHeader, null);
		reporteEstadoSituacionPorServicioSheetExcelSub21.setItems(reporteEstadoSituacionByServiciosSub21);

		ReporteEstadoSituacionPorServicioSheetExcel reporteEstadoSituacionPorServicioSheetExcelSub22 = new ReporteEstadoSituacionPorServicioSheetExcel(header, subHeader, null);
		reporteEstadoSituacionPorServicioSheetExcelSub22.setItems(reporteEstadoSituacionByServiciosSub22);

		ReporteEstadoSituacionPorServicioSheetExcel reporteEstadoSituacionPorServicioSheetExcelSub29 = new ReporteEstadoSituacionPorServicioSheetExcel(header, subHeader, null);
		reporteEstadoSituacionPorServicioSheetExcelSub29.setItems(reporteEstadoSituacionByServiciosSub29);


		generadorExcel.addSheet(reporteEstadoSituacionPorServicioSheetExcelSub21, "Subtitulo 21");
		generadorExcel.addSheet(reporteEstadoSituacionPorServicioSheetExcelSub22, "Subtitulo 22");
		generadorExcel.addSheet(reporteEstadoSituacionPorServicioSheetExcelSub29, "Subtitulo 29");

		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderReportes.replace("{ANO}", ano.toString()));
			TipoDocumento tipoDocumento = new TipoDocumento(TipoDocumentosProcesos.REPORTEESTADOSITUACIONPROGRAMABYSERVICIO.getId());
			planillaTrabajoId = documentService.createDocumentReportes(tipoDocumento, response.getNodeRef(), response.getFileName(), contenType, ano, Integer.parseInt(getMesCurso(true)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return planillaTrabajoId;
	}

	Long marcoProgramasDependenciaMunicipalPorServicio(Integer idProgramaAno, Integer idServicio, Integer idSubtitulo){
		Long marco = 0L;
		ProgramaVO programa = programasService.getProgramaAnoPorID(idProgramaAno);
		List<ComponentesVO> componentes = programa.getComponentes();
		ServiciosVO servicio = servicioSaludService.getServicioSaludById(idServicio);
		for(ComunaSummaryVO comuna : servicio.getComunas()){
			for(ComponentesVO componente : componentes){
				marco += programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAno, componente.getId(), idSubtitulo);
			}
		}
		return marco;
	}


	List<ReporteMetaDesempenoOTAcumuladasPrincipal> generarMetaDesempenoOT(Integer ano) {

		List<ReporteMetaDesempenoOTAcumuladasPrincipal> resultado = new ArrayList<ReporteMetaDesempenoOTAcumuladasPrincipal>();

		List<ServiciosVO> servicios = servicioSaludService.getServiciosOrderId();


		for (ServiciosVO servicio : servicios) {
			ReporteMetaDesempenoOTAcumuladasPrincipal fila = new ReporteMetaDesempenoOTAcumuladasPrincipal();
			fila.setCod_ss(servicio.getId_servicio());
			fila.setServicio(servicio.getNombre_servicio());

			ReporteMetaDesempenoOTAcumuladasSub24 sub24 = new ReporteMetaDesempenoOTAcumuladasSub24();
			ReporteMetaDesempenoOTAcumuladasSub21 sub21 = new ReporteMetaDesempenoOTAcumuladasSub21();
			ReporteMetaDesempenoOTAcumuladasSub22 sub22 = new ReporteMetaDesempenoOTAcumuladasSub22();
			ReporteMetaDesempenoOTAcumuladasSub29 sub29 = new ReporteMetaDesempenoOTAcumuladasSub29();

			Long montoTemp = 24234234L;
			Long percapitaBasal = 0L;
			Long totalPercapita = 0L;
			Long totalAddf = 0L;
			Long totalRebajaIAAPS = 0L;
			DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(ano);
			
			if(servicio.getComunas() == null || servicio.getComunas().size() == 0){
				continue;
			}

			for(ComunaSummaryVO comuna : servicio.getComunas()){
				List<AntecendentesComunaCalculado> antecendentesComunaCalculados = antecedentesComunaDAO
						.findAntecendentesComunaCalculadoByComunaServicioDistribucionInicialPercapitaVigente(
								servicio.getId_servicio(), comuna.getId(), distribucionInicialPercapita.getIdDistribucionInicialPercapita());
				AntecendentesComunaCalculado antecendentesComunaCalculado = ((antecendentesComunaCalculados != null && antecendentesComunaCalculados.size() > 0) ? antecendentesComunaCalculados.get(0) : null);
				
				if(antecendentesComunaCalculado == null){
					continue;
				}

				Long rebajaIAAPS = 0L;
				Long percapitaMes = 0L;
				Integer desempenoDificil = 0;
				if (antecendentesComunaCalculado != null && antecendentesComunaCalculado.getAntecedentesComunaCalculadoRebajas() != null && antecendentesComunaCalculado.getAntecedentesComunaCalculadoRebajas().size() > 0) {
					for (AntecedentesComunaCalculadoRebaja antecedentesComunaCalculadoRebaja : antecendentesComunaCalculado.getAntecedentesComunaCalculadoRebajas()) {
						rebajaIAAPS += antecedentesComunaCalculadoRebaja.getMontoRebaja();
						System.out.println("rebajaIAAPS --> " + rebajaIAAPS);
					}
					percapitaMes = antecendentesComunaCalculado.getPercapitaMes();
					desempenoDificil = antecendentesComunaCalculado.getDesempenoDificil();
				}
				totalRebajaIAAPS += ((rebajaIAAPS == null) ? 0 : rebajaIAAPS);

				percapitaBasal += ((percapitaMes == null) ? 0 : percapitaMes);

				totalAddf += ((desempenoDificil == null) ? 0 : desempenoDificil);

			}

			sub24.setPercapitaBasal(percapitaBasal);
			sub24.setAddf(totalAddf);
			sub24.setDescuentoRetiro(montoTemp);
			sub24.setRebajaIncumplida(totalRebajaIAAPS);

			totalPercapita = (percapitaBasal + totalAddf) - sub24.getDescuentoRetiro() - sub24.getRebajaIncumplida();

			sub24.setTotalPercapita(totalPercapita);
			sub24.setLeyes(montoTemp);
			sub24.setRetiroYbonificacionComplementaria(montoTemp);
			sub24.setChileCreceContigo(montoTemp);
			sub24.setOtrasLeyes(montoTemp);
			sub24.setResolutividad(montoTemp);
			sub24.setUrgencia(montoTemp);
			sub24.setOdontologia(montoTemp);
			sub24.setOtrosRefMunicipal(montoTemp);
			sub24.setTotalRefMunicipal(montoTemp);


			sub21.setChileCreceContigo(montoTemp);
			sub21.setApoyoGestionSalud(montoTemp);
			sub21.setResolutividad(montoTemp);
			sub21.setUrgencia(montoTemp);
			sub21.setOdontologia(montoTemp);
			sub21.setOtrosRefSub21(montoTemp);
			sub21.setTotalRefSub21(montoTemp);


			sub22.setChileCreceContigo(montoTemp);
			sub22.setApoyoGestionSalud(montoTemp);
			sub22.setResolutividad(montoTemp);
			sub22.setUrgencia(montoTemp);
			sub22.setOdontologia(montoTemp);
			sub22.setCompraFarmacos(montoTemp);
			sub22.setMandatosApsProgramas(montoTemp);
			sub22.setOtrosRefSub22(montoTemp);
			sub22.setTotalRefSub22(montoTemp);

			sub29.setOtrosRefSub29(montoTemp);
			sub29.setTotalRefSub29(montoTemp);

			fila.setReporteMetaDesempenoOTAcumuladasSub21(sub21);
			fila.setReporteMetaDesempenoOTAcumuladasSub22(sub22);
			fila.setReporteMetaDesempenoOTAcumuladasSub24(sub24);
			fila.setReporteMetaDesempenoOTAcumuladasSub29(sub29);
			fila.setTotalAPSAcumulado(montoTemp);

			resultado.add(fila);
		}

		return resultado;
	}

	public Integer generarPlanillaMetaDesempenoCuadro2(Integer ano) {
		Integer planillaTrabajoId = null;
		List<ReporteMetaDesempenoOTAcumuladasPrincipal> reporteMetaDesempenoOTAcumuladasPrincipal = generarMetaDesempenoOT(ano);
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator + "Panilla Reporte Meta Desempeno Cuadro2.xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());

		GeneradorExcel generadorExcel = new GeneradorExcel(filename);

		ReporteMetaDesempenoCuadro2SheetExcel reporteMetaDesempenoCuadro2SheetExcel = new ReporteMetaDesempenoCuadro2SheetExcel(null, null, null);
		reporteMetaDesempenoCuadro2SheetExcel.setItems(reporteMetaDesempenoOTAcumuladasPrincipal);

		generadorExcel.addSheet(reporteMetaDesempenoCuadro2SheetExcel, "hoja 1");
		System.out.println("folderReportes ---> "+folderReportes);
		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderReportes.replace("{ANO}", ano.toString()));
			TipoDocumento tipoDocumento = new TipoDocumento(TipoDocumentosProcesos.REPORTEMETADESEMPENOCUADRO2.getId());
			planillaTrabajoId = documentService.createDocumentReportes(tipoDocumento, response.getNodeRef(), response.getFileName(), contenType, ano, Integer.parseInt(getMesCurso(true)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return planillaTrabajoId;
	}

}
