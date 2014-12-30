package minsal.divap.service;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import minsal.divap.dao.AntecedentesComunaDAO;
import minsal.divap.dao.ComunaDAO;
import minsal.divap.dao.ConveniosDAO;
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
import minsal.divap.excel.impl.ReporteGlosa07SheetExcel;
import minsal.divap.excel.impl.ReporteHistoricoProgramaPorComunaSheetExcel;
import minsal.divap.excel.impl.ReporteHistoricoProgramaPorEstablecimientoSheetExcel;
import minsal.divap.excel.impl.ReporteMarcoPresupuestarioComunaSheetExcel;
import minsal.divap.excel.impl.ReporteMarcoPresupuestarioEstablecimientoSheetExcel;
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
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.ReporteEstadoSituacionByComunaVO;
import minsal.divap.vo.ReporteEstadoSituacionByServiciosVO;
import minsal.divap.vo.ReporteGlosaVO;
import minsal.divap.vo.ReporteHistoricoPorProgramaComunaVO;
import minsal.divap.vo.ReporteHistoricoPorProgramaEstablecimientoVO;
import minsal.divap.vo.ReporteMarcoPresupuestarioComunaVO;
import minsal.divap.vo.ReporteMarcoPresupuestarioEstablecimientoVO;
import minsal.divap.vo.ReporteMonitoreoProgramaPorComunaVO;
import minsal.divap.vo.ReporteMonitoreoProgramaPorEstablecimientoVO;
import minsal.divap.vo.ReportePerCapitaVO;
import minsal.divap.vo.ReporteRebajaVO;
import minsal.divap.vo.ServiciosVO;
import cl.minsal.divap.model.AntecedentesComunaCalculadoRebaja;
import cl.minsal.divap.model.AntecendentesComuna;
import cl.minsal.divap.model.AntecendentesComunaCalculado;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.ComunaCumplimiento;
import cl.minsal.divap.model.ConvenioServicioComponente;
import cl.minsal.divap.model.Cuota;
import cl.minsal.divap.model.Establecimiento;
import cl.minsal.divap.model.Mes;
import cl.minsal.divap.model.ProgramaMunicipalCoreComponente;
import cl.minsal.divap.model.ProgramaServicioCoreComponente;
import cl.minsal.divap.model.RebajaCorte;
import cl.minsal.divap.model.ServicioSalud;
import cl.minsal.divap.model.TipoDocumento;

@Stateless
@LocalBean
public class ReportesServices {

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

		List<ServiciosVO> serviciosVO = servicioSaludService
				.getServiciosOrderId();
		for (ServiciosVO servicioVO : serviciosVO) {

			System.out.println("servicioSalud --> "
					+ servicioVO.getNombre_servicio());

			for (ComunaSummaryVO comuna : servicioVO.getComunas()) {
				ReportePerCapitaVO reportePerCapitaVO = new ReportePerCapitaVO();
				reportePerCapitaVO.setRegion(servicioVO.getRegion().getNombre());
				reportePerCapitaVO.setServicio(servicioVO.getNombre_servicio());
				reportePerCapitaVO.setComuna(comuna.getNombre());

				AntecendentesComunaCalculado antecendentesComunaCalculado = antecedentesComunaDAO
						.findByComunaAnoVigencia(comuna.getId(), getAnoCurso());
				
				
				if (antecendentesComunaCalculado == null) {
					continue;
				}

				List<ComunaCumplimiento> comunasCumplimientos = rebajaDAO
						.getCumplimientoPorAnoComuna(ano, comuna.getId());

				if (antecendentesComunaCalculado.getAntecedentesComuna().getClasificacion() != null) {
					reportePerCapitaVO.setClasificacion(antecendentesComunaCalculado.getAntecedentesComuna()
							.getClasificacion().getIdTipoComuna().toString());
				}

				if (antecendentesComunaCalculado.getValorPerCapitaComunalMes() != null) {
					reportePerCapitaVO.setValorPercapita(antecendentesComunaCalculado
							.getValorPerCapitaComunalMes().longValue());
				} else {
					reportePerCapitaVO.setValorPercapita(0L);
				}
				if (antecendentesComunaCalculado.getPoblacion() != null) {
					reportePerCapitaVO.setPoblacion(antecendentesComunaCalculado
							.getPoblacion());
				}
				if (antecendentesComunaCalculado.getPoblacionMayor() != null) {
					reportePerCapitaVO.setPoblacion65mayor(antecendentesComunaCalculado
							.getPoblacionMayor());
				}

				if (antecendentesComunaCalculado.getPercapitaAno() != null) {
					reportePerCapitaVO.setPercapita(antecendentesComunaCalculado.getPercapitaAno());
				}
				if (antecendentesComunaCalculado.getDesempenoDificil() != null) {
					reportePerCapitaVO.setDesempenoDificil(antecendentesComunaCalculado
							.getDesempenoDificil().longValue());
				}

				if (antecendentesComunaCalculado.getPercapitaAno() != null
						&& antecendentesComunaCalculado.getDesempenoDificil() != null) {
					reportePerCapitaVO.setAporteEstatal((antecendentesComunaCalculado
							.getPercapitaAno() + antecendentesComunaCalculado
							.getDesempenoDificil().longValue()));
				}
				
				Set<AntecedentesComunaCalculadoRebaja> setAntecedentesComunaCalculadoRebaja = antecendentesComunaCalculado.getAntecedentesComunaCalculadoRebajas();
				
				Long rebajaIAAPS = 0L;
				List<AntecedentesComunaCalculadoRebaja> antecedentesComunaCalculadoRebajas = new ArrayList<AntecedentesComunaCalculadoRebaja>(setAntecedentesComunaCalculadoRebaja);
				for(AntecedentesComunaCalculadoRebaja antecedentesComunaCalculadoRebaja : antecedentesComunaCalculadoRebajas){
					rebajaIAAPS += antecedentesComunaCalculadoRebaja.getMontoRebaja();
					System.out.println("rebajaIAAPS --> "+rebajaIAAPS);
				}
				
				Long totalRebaja = 0L;

				for (ComunaCumplimiento comunaCumplimiento : comunasCumplimientos) {
					Double porcentajeRebaja = comunaCumplimiento.getRebajaFinal()
							.getRebaja();
					totalRebaja = (long) (totalRebaja + (porcentajeRebaja * reportePerCapitaVO
							.getAporteEstatal()));
				}
				reportePerCapitaVO.setRebajaIAAPS(rebajaIAAPS);

				// TODO falta este valor
				Long descuentoIncentivoRetiro = 234234L;
				
				reportePerCapitaVO.setDescuentoIncentivoRetiro(descuentoIncentivoRetiro);
				reportePerCapitaVO.setAporteEstatalFinal(reportePerCapitaVO.getAporteEstatal()
						- reportePerCapitaVO.getRebajaIAAPS()
						- reportePerCapitaVO.getDescuentoIncentivoRetiro());

				listaReportePerCapitaVO.add(reportePerCapitaVO);

			}
		}

		return listaReportePerCapitaVO;
	}

	public List<ReportePerCapitaVO> getReportePercapitaServicio(
			Integer idServicio, Integer ano) {
		List<ReportePerCapitaVO> listaReportePerCapitaVO = new ArrayList<ReportePerCapitaVO>();

		ServicioSalud servicioSalud = this.servicioSaludDAO
				.getServicioSaludPorID(idServicio);

		System.out.println("servicioSalud --> " + servicioSalud.getNombre());

		for (Comuna comuna : servicioSalud.getComunas()) {
			System.out.println("comuna --> " + comuna.getNombre());
			System.out.println("ano ---> " + ano);
			ReportePerCapitaVO reportePerCapitaVO = new ReportePerCapitaVO();
			reportePerCapitaVO.setRegion(servicioSalud.getRegion().getNombre());
			reportePerCapitaVO.setServicio(servicioSalud.getNombre());
			reportePerCapitaVO.setComuna(comuna.getNombre());

			AntecendentesComunaCalculado antecendentesComunaCalculado = antecedentesComunaDAO
					.findByComunaAno(comuna.getId(), ano);
			// AntecendentesComuna antecendentesComuna = antecedentesComunaDAO
			// .findAntecendentesComunaByComunaServicioAno(
			// servicioSalud.getNombre(), comuna.getNombre(), ano);
			//
			// if (antecendentesComuna == null) {
			// continue;
			// }

			if (antecendentesComunaCalculado == null) {
				continue;
			}

			List<ComunaCumplimiento> comunasCumplimientos = rebajaDAO
					.getCumplimientoPorAnoComuna(ano, comuna.getId());

			if (antecendentesComunaCalculado.getAntecedentesComuna()
					.getClasificacion() != null) {
				reportePerCapitaVO
						.setClasificacion(antecendentesComunaCalculado
								.getAntecedentesComuna().getClasificacion()
								.getIdTipoComuna().toString());
			}

			if (antecendentesComunaCalculado.getValorPerCapitaComunalMes() != null) {
				reportePerCapitaVO
						.setValorPercapita(antecendentesComunaCalculado
								.getValorPerCapitaComunalMes().longValue());
			} else {
				reportePerCapitaVO.setValorPercapita(0L);
			}
			if (antecendentesComunaCalculado.getPoblacion() != null) {
				reportePerCapitaVO.setPoblacion(antecendentesComunaCalculado
						.getPoblacion());
			}
			if (antecendentesComunaCalculado.getPoblacionMayor() != null) {
				reportePerCapitaVO
						.setPoblacion65mayor(antecendentesComunaCalculado
								.getPoblacionMayor());
			}

			if (antecendentesComunaCalculado.getPercapitaAno() != null) {
				reportePerCapitaVO.setPercapita(antecendentesComunaCalculado
						.getPercapitaAno());
			}
			if (antecendentesComunaCalculado.getDesempenoDificil() != null) {
				reportePerCapitaVO
						.setDesempenoDificil(antecendentesComunaCalculado
								.getDesempenoDificil().longValue());
			}

			if (antecendentesComunaCalculado.getPercapitaAno() != null
					&& antecendentesComunaCalculado.getDesempenoDificil() != null) {
				reportePerCapitaVO
						.setAporteEstatal((antecendentesComunaCalculado
								.getPercapitaAno() + antecendentesComunaCalculado
								.getDesempenoDificil().longValue()));
			}

			Long totalRebaja = 0L;

			for (ComunaCumplimiento comunaCumplimiento : comunasCumplimientos) {
				Double porcentajeRebaja = comunaCumplimiento.getRebajaFinal()
						.getRebaja();
				totalRebaja = (long) (totalRebaja + (porcentajeRebaja * reportePerCapitaVO
						.getAporteEstatal()));
			}
			reportePerCapitaVO.setRebajaIAAPS(totalRebaja);

			Long descuentoIncentivoRetiro = 234234L;
			// TODO falta este valor
			reportePerCapitaVO
					.setDescuentoIncentivoRetiro(descuentoIncentivoRetiro);
			reportePerCapitaVO.setAporteEstatalFinal(reportePerCapitaVO
					.getAporteEstatal()
					- reportePerCapitaVO.getRebajaIAAPS()
					- reportePerCapitaVO.getDescuentoIncentivoRetiro());

			listaReportePerCapitaVO.add(reportePerCapitaVO);

		}

		return listaReportePerCapitaVO;
	}

	public ReportePerCapitaVO getReportePercapitaServicioComuna(
			Integer idServicio, Integer ano, Integer idComuna, String usuario) {

		ServicioSalud servicioSalud = this.servicioSaludDAO
				.getServicioSaludPorID(idServicio);

		Comuna comuna = comunaService.getComunaById(idComuna);

		ReportePerCapitaVO reportePerCapitaVO = new ReportePerCapitaVO();
		reportePerCapitaVO.setRegion(servicioSalud.getRegion().getNombre());
		reportePerCapitaVO.setServicio(servicioSalud.getNombre());
		reportePerCapitaVO.setComuna(comuna.getNombre());


		AntecendentesComunaCalculado antecendentesComunaCalculado = antecedentesComunaDAO
				.findByComunaAnoVigencia(comuna.getId(), getAnoCurso());
		
		
		if (antecendentesComunaCalculado == null) {
			antecendentesComunaCalculado = new AntecendentesComunaCalculado();
		}

		List<ComunaCumplimiento> comunasCumplimientos = rebajaDAO
				.getCumplimientoPorAnoComuna(ano, comuna.getId());

		if (antecendentesComunaCalculado.getAntecedentesComuna().getClasificacion() != null) {
			reportePerCapitaVO.setClasificacion(antecendentesComunaCalculado.getAntecedentesComuna()
					.getClasificacion().getIdTipoComuna().toString());
		}

		if (antecendentesComunaCalculado.getValorPerCapitaComunalMes() != null) {
			reportePerCapitaVO.setValorPercapita(antecendentesComunaCalculado
					.getValorPerCapitaComunalMes().longValue());
		} else {
			reportePerCapitaVO.setValorPercapita(0L);
		}
		if (antecendentesComunaCalculado.getPoblacion() != null) {
			reportePerCapitaVO.setPoblacion(antecendentesComunaCalculado
					.getPoblacion());
		}
		if (antecendentesComunaCalculado.getPoblacionMayor() != null) {
			reportePerCapitaVO.setPoblacion65mayor(antecendentesComunaCalculado
					.getPoblacionMayor());
		}

		if (antecendentesComunaCalculado.getPercapitaAno() != null) {
			reportePerCapitaVO.setPercapita(antecendentesComunaCalculado.getPercapitaAno());
		}
		if (antecendentesComunaCalculado.getDesempenoDificil() != null) {
			reportePerCapitaVO.setDesempenoDificil(antecendentesComunaCalculado
					.getDesempenoDificil().longValue());
		}

		if (antecendentesComunaCalculado.getPercapitaAno() != null
				&& antecendentesComunaCalculado.getDesempenoDificil() != null) {
			reportePerCapitaVO.setAporteEstatal((antecendentesComunaCalculado
					.getPercapitaAno() + antecendentesComunaCalculado
					.getDesempenoDificil().longValue()));
		}
		
		Set<AntecedentesComunaCalculadoRebaja> setAntecedentesComunaCalculadoRebaja = antecendentesComunaCalculado.getAntecedentesComunaCalculadoRebajas();
		
		Long rebajaIAAPS = 0L;
		List<AntecedentesComunaCalculadoRebaja> antecedentesComunaCalculadoRebajas = new ArrayList<AntecedentesComunaCalculadoRebaja>(setAntecedentesComunaCalculadoRebaja);
		for(AntecedentesComunaCalculadoRebaja antecedentesComunaCalculadoRebaja : antecedentesComunaCalculadoRebajas){
			rebajaIAAPS += antecedentesComunaCalculadoRebaja.getMontoRebaja();
			System.out.println("rebajaIAAPS --> "+rebajaIAAPS);
		}
		
		Long totalRebaja = 0L;

		for (ComunaCumplimiento comunaCumplimiento : comunasCumplimientos) {
			Double porcentajeRebaja = comunaCumplimiento.getRebajaFinal()
					.getRebaja();
			totalRebaja = (long) (totalRebaja + (porcentajeRebaja * reportePerCapitaVO
					.getAporteEstatal()));
		}
		reportePerCapitaVO.setRebajaIAAPS(rebajaIAAPS);

		// TODO falta este valor
		Long descuentoIncentivoRetiro = 234234L;
		
		reportePerCapitaVO.setDescuentoIncentivoRetiro(descuentoIncentivoRetiro);
		reportePerCapitaVO.setAporteEstatalFinal(reportePerCapitaVO.getAporteEstatal()
				- reportePerCapitaVO.getRebajaIAAPS()
				- reportePerCapitaVO.getDescuentoIncentivoRetiro());

		return reportePerCapitaVO;
	}

	public Integer generarPlanillaPoblacionPercapita(String usuario) {
		Integer planillaTrabajoId = null;
		List<ServiciosVO> servicios = servicioSaludService
				.getServiciosOrderId();
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
		// List<ReportePerCapitaVO> reportePerCapita2011 =
		// this.getReportePercapitaAll(getAnoCurso()-3);
		// List<ReportePerCapitaVO> reportePerCapita2012 =
		// this.getReportePercapitaAll(getAnoCurso()-2);
		// List<ReportePerCapitaVO> reportePerCapita2013 =
		// this.getReportePercapitaAll(getAnoCurso()-1);
		// List<ReportePerCapitaVO> reportePerCapita2014 =
		// this.getReportePercapitaAll(getAnoCurso());

		List<ReportePerCapitaVO> reportePerCapita2011 = this
				.getReportePercapitaAll(2011, usuario);
		List<ReportePerCapitaVO> reportePerCapita2012 = this
				.getReportePercapitaAll(2012, usuario);
		List<ReportePerCapitaVO> reportePerCapita2013 = this
				.getReportePercapitaAll(2013, usuario);
		List<ReportePerCapitaVO> reportePerCapita2014 = this
				.getReportePercapitaAll(2014, usuario);

		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator
				+ "Panilla Poblacion per Capita.xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());

		GeneradorExcel generadorExcel = new GeneradorExcel(filename);

		ReportePoblacionPercapitaSheetExcel reportePoblacionPercapitaSheetExcel2011 = new ReportePoblacionPercapitaSheetExcel(
				header, null);
		reportePoblacionPercapitaSheetExcel2011.setItems(reportePerCapita2011);

		ReportePoblacionPercapitaSheetExcel reportePoblacionPercapitaSheetExcel2012 = new ReportePoblacionPercapitaSheetExcel(
				header, null);
		reportePoblacionPercapitaSheetExcel2012.setItems(reportePerCapita2012);

		ReportePoblacionPercapitaSheetExcel reportePoblacionPercapitaSheetExcel2013 = new ReportePoblacionPercapitaSheetExcel(
				header, null);
		reportePoblacionPercapitaSheetExcel2013.setItems(reportePerCapita2013);

		ReportePoblacionPercapitaSheetExcel reportePoblacionPercapitaSheetExcel2014 = new ReportePoblacionPercapitaSheetExcel(
				header, null);
		reportePoblacionPercapitaSheetExcel2014.setItems(reportePerCapita2014);

		generadorExcel.addSheet(reportePoblacionPercapitaSheetExcel2014,
				getAnoCurso() + "");
		generadorExcel.addSheet(reportePoblacionPercapitaSheetExcel2013,
				getAnoCurso() - 1 + "");
		generadorExcel.addSheet(reportePoblacionPercapitaSheetExcel2012,
				getAnoCurso() - 2 + "");
		generadorExcel.addSheet(reportePoblacionPercapitaSheetExcel2011,
				getAnoCurso() - 3 + "");

		System.out.println("folderPercapita --> " + folderReportes);


		try {
			BodyVO response = alfrescoService.uploadDocument(
					generadorExcel.saveExcel(), contenType,
					folderReportes.replace("{ANO}", getAnoCurso().toString()));
			// System.out.println("response planillaPropuestaEstimacionFlujoCajaConsolidador --->"
			// + response);

			TipoDocumento tipoDocumento = new TipoDocumento(
					TipoDocumentosProcesos.REPORTEPOBLACIONPERCAPITA.getId());
			planillaTrabajoId = documentService.createDocumentReportes(
					tipoDocumento, response.getNodeRef(),
					response.getFileName(), contenType, getAnoCurso(),
					Integer.parseInt(getMesCurso(true)));

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

	public List<ReporteMarcoPresupuestarioComunaVO> getReporteMarcoPorComunaFiltroServicio(
			Integer idServicio, Subtitulo subtitulo, Integer idComuna,
			String usuario) {

		List<ReporteMarcoPresupuestarioComunaVO> resultado = new ArrayList<ReporteMarcoPresupuestarioComunaVO>();
		List<ProgramaVO> programasVO = programasService
				.getProgramasBySubtitulo(subtitulo);

		for (ProgramaVO programa : programasVO) {
			ServicioSalud servicio = servicioSaludDAO
					.getServicioSaludById(idServicio);
			Comuna comuna = comunaService.getComunaById(idComuna);

			List<ComponentesVO> componentes = programasService.getComponenteByProgramaSubtitulos(programa.getId(), subtitulo);
			for (ComponentesVO componenteVO : componentes) {
				// System.out.println("programa --> "+programa+"  --  nombre del componente --> "+componenteVO.getNombre());

				AntecendentesComuna antecendentesComuna = antecedentesComunaDAO
						.findAntecendentesComunaByComunaServicioAno(
								servicio.getNombre(), comuna.getNombre(),
								getAnoCurso());
				// ReporteMarcoPresupuestarioComunaVO
				// reporteMarcoPresupuestarioComunaVO = new
				// ReporteMarcoPresupuestarioComunaVO();
				// reporteMarcoPresupuestarioComunaVO.setServicio(servicio.getNombre());
				// reporteMarcoPresupuestarioComunaVO.setPrograma(programa
				// .getNombre());
				// reporteMarcoPresupuestarioComunaVO.setComponente(componenteVO
				// .getNombre());
				// reporteMarcoPresupuestarioComunaVO.setComuna(comuna.getNombre());
				//
				// AntecendentesComuna antecendentesComuna =
				// antecedentesComunaDAO
				// .findAntecendentesComunaByComunaServicioAno(
				// servicio.getNombre(), comuna.getNombre(), 2014);
				//
				// if (antecendentesComuna == null) {
				// continue;
				// }
				//
				// AntecendentesComunaCalculado antecendentesComunaCalculado =
				// antecedentesComunaDAO
				// .findByIdAntecedentesComuna(antecendentesComuna
				// .getIdAntecedentesComuna());
				// if (antecendentesComunaCalculado == null) {
				// continue;
				// }
				// Long tarifa = 0L;
				// List<ProgramaMunicipalCoreComponente>
				// programaMunicipalCoreComponentes = programasDAO
				// .getByIdComuna(comuna.getId());
				// for (ProgramaMunicipalCoreComponente
				// programaMunicipalCoreComponente :
				// programaMunicipalCoreComponentes) {
				// if (programaMunicipalCoreComponente == null) {
				// continue;
				// }
				// tarifa += programaMunicipalCoreComponente.getTarifa();
				// }
				//
				// ConvenioComuna convenioComuna = conveniosDAO
				// .getConvenioComunaByIdComunaIdProgramaAno(
				// comuna.getId(), programa.getIdProgramaAno());
				// if (convenioComuna == null) {
				// continue;
				// }
				// ConvenioComunaComponente convenioComunaComponente =
				// conveniosDAO
				// .getByIdConvenioComunaIdSubtituloIdComponente(
				// convenioComuna.getIdConvenioComuna(),
				// subtitulo.getId(), componenteVO.getId());
				//
				// if (convenioComunaComponente == null) {
				// continue;
				// }
				//
				// Long marcoTEMP =
				// antecendentesComunaCalculado.getPercapitaAno()
				// + tarifa;
				// reporteMarcoPresupuestarioComunaVO.setMarco(marcoTEMP);
				//
				// Long conveniosTEMP = (long)
				// convenioComunaComponente.getMonto();
				// reporteMarcoPresupuestarioComunaVO.setConvenios(conveniosTEMP);
				// Double porcentajeCuotaTransferida = 0.0;
				//
				// // TODO cambiar este valor despues
				// Long remesasAcumuladasTEMP = (long) (convenioComunaComponente
				// .getMonto() + 3);
				// if (Integer.parseInt(getMesCurso(true)) > 10) {
				// porcentajeCuotaTransferida = 1.0;
				// } else {
				// Cuota cuota = reliquidacionDAO
				// .getCuotaByIdProgramaAnoNroCuota(
				// programa.getIdProgramaAno(), (short) 1);
				// if (cuota.getIdMes().getIdMes() < Integer
				// .parseInt(getMesCurso(true))) {
				// porcentajeCuotaTransferida = (double) cuota
				// .getPorcentaje() / 100;
				// reporteMarcoPresupuestarioComunaVO
				// .setPorcentajeCuotaTransferida(porcentajeCuotaTransferida);
				// } else {
				// reporteMarcoPresupuestarioComunaVO
				// .setPorcentajeCuotaTransferida(0.6);
				// }
				// }
				// // TODO cambiar despues
				// reporteMarcoPresupuestarioComunaVO
				// .setPorcentajeCuotaTransferida(1.0);
				// reporteMarcoPresupuestarioComunaVO
				// .setRemesasAcumuladas(remesasAcumuladasTEMP);
				// reporteMarcoPresupuestarioComunaVO.setObservacion("");
				//
				// System.out.println("reporteMarcoPresupuestarioVO ---> "
				// + reporteMarcoPresupuestarioComunaVO.toString());
				//
				// resultado.add(reporteMarcoPresupuestarioComunaVO);
				ReporteMarcoPresupuestarioComunaVO reporteMarcoPresupuestarioComunaVO = new ReporteMarcoPresupuestarioComunaVO();
				reporteMarcoPresupuestarioComunaVO.setServicio(servicio
						.getNombre());
				reporteMarcoPresupuestarioComunaVO.setPrograma(programa
						.getNombre());
				reporteMarcoPresupuestarioComunaVO.setComponente(componenteVO
						.getNombre());
				reporteMarcoPresupuestarioComunaVO
						.setComuna(comuna.getNombre());

				// AntecendentesComunaCalculado antecendentesComunaCalculado =
				// antecedentesComunaDAO
				// .findByComunaAno(comuna.getId(), ano);
				// if (antecendentesComunaCalculado == null) {
				// continue;
				// }
				//
				// Long tarifa = 0L;
				// List<ProgramaMunicipalCoreComponente>
				// programaMunicipalCoreComponentes = programasDAO
				// .getByIdComuna(comuna.getId());
				// for (ProgramaMunicipalCoreComponente
				// programaMunicipalCoreComponente :
				// programaMunicipalCoreComponentes) {
				// if (programaMunicipalCoreComponente == null) {
				// continue;
				// }
				// tarifa += programaMunicipalCoreComponente.getTarifa();
				// }
				//
				// ConvenioComuna convenioComuna = conveniosDAO
				// .getConvenioComunaByIdComunaIdProgramaAno(
				// comuna.getId(), programa.getIdProgramaAno());
				// if (convenioComuna == null) {
				// continue;
				// }
				// ConvenioComunaComponente convenioComunaComponente =
				// conveniosDAO
				// .getByIdConvenioComunaIdSubtituloIdComponente(
				// convenioComuna.getIdConvenioComuna(),
				// subtitulo.getId(), componenteVO.getId());
				//
				// if (convenioComunaComponente == null) {
				// continue;
				// }

				// Long marcoTEMP =
				// antecendentesComunaCalculado.getPercapitaAno()
				// + tarifa;
				reporteMarcoPresupuestarioComunaVO.setMarco((long) 10
						* programa.getIdProgramaAno() * servicio.getId()
						* comuna.getId());

				// Long conveniosTEMP = (long)
				// convenioComunaComponente.getMonto();
				reporteMarcoPresupuestarioComunaVO.setConvenios((long) 13
						* programa.getIdProgramaAno() * comuna.getId());
				Double porcentajeCuotaTransferida = 0.0;

				// TODO cambiar este valor despues
				// Long remesasAcumuladasTEMP = (long)
				// (convenioComunaComponente.getMonto() + 3);
				if (Integer.parseInt(getMesCurso(true)) > 10) {
					porcentajeCuotaTransferida = 1.0;
				} else {
					Cuota cuota = reliquidacionDAO
							.getCuotaByIdProgramaAnoNroCuota(
									programa.getIdProgramaAno(), (short) 1);
					if (cuota.getIdMes().getIdMes() < Integer
							.parseInt(getMesCurso(true))) {
						porcentajeCuotaTransferida = (double) cuota
								.getPorcentaje() / 100;
						reporteMarcoPresupuestarioComunaVO
								.setPorcentajeCuotaTransferida(porcentajeCuotaTransferida);
					} else {
						reporteMarcoPresupuestarioComunaVO
								.setPorcentajeCuotaTransferida(0.6);
					}
				}
				// TODO cambiar despues
				reporteMarcoPresupuestarioComunaVO
						.setPorcentajeCuotaTransferida(1.0);
				reporteMarcoPresupuestarioComunaVO
						.setRemesasAcumuladas((long) servicio.getId()
								* comuna.getId() * 6);
				reporteMarcoPresupuestarioComunaVO.setObservacion("");

				resultado.add(reporteMarcoPresupuestarioComunaVO);

			}

		}

		return resultado;
	}

	public List<ReporteMarcoPresupuestarioComunaVO> getReporteMarcoPorComunaFiltroServicioComunaPrograma(
			Integer idProgramaAno, Integer idServicio, Subtitulo subtitulo,
			Integer idComuna, String usuario) {

		List<ReporteMarcoPresupuestarioComunaVO> resultado = new ArrayList<ReporteMarcoPresupuestarioComunaVO>();

		ProgramaVO programa = programasService.getProgramaAno(idProgramaAno);

		ServicioSalud servicio = servicioSaludDAO
				.getServicioSaludById(idServicio);
		Comuna comuna = comunaService.getComunaById(idComuna);

		List<ComponentesVO> componentes = programasService.getComponenteByProgramaSubtitulos(programa.getId(), subtitulo);
		for (ComponentesVO componenteVO : componentes) {
			// ReporteMarcoPresupuestarioComunaVO
			// reporteMarcoPresupuestarioComunaVO = new
			// ReporteMarcoPresupuestarioComunaVO();
			// reporteMarcoPresupuestarioComunaVO.setServicio(servicio.getNombre());
			// reporteMarcoPresupuestarioComunaVO.setPrograma(programa
			// .getNombre());
			// reporteMarcoPresupuestarioComunaVO.setComponente(componenteVO
			// .getNombre());
			// reporteMarcoPresupuestarioComunaVO.setComuna(comuna.getNombre());
			//
			// AntecendentesComuna antecendentesComuna = antecedentesComunaDAO
			// .findAntecendentesComunaByComunaServicioAno(
			// servicio.getNombre(), comuna.getNombre(), 2014);
			//
			// if (antecendentesComuna == null) {
			// continue;
			// }
			//
			// AntecendentesComunaCalculado antecendentesComunaCalculado =
			// antecedentesComunaDAO
			// .findByIdAntecedentesComuna(antecendentesComuna
			// .getIdAntecedentesComuna());
			// if (antecendentesComunaCalculado == null) {
			// continue;
			// }
			// Long tarifa = 0L;
			// List<ProgramaMunicipalCoreComponente>
			// programaMunicipalCoreComponentes = programasDAO
			// .getByIdComuna(comuna.getId());
			// for (ProgramaMunicipalCoreComponente
			// programaMunicipalCoreComponente :
			// programaMunicipalCoreComponentes) {
			// if (programaMunicipalCoreComponente == null) {
			// continue;
			// }
			// tarifa += programaMunicipalCoreComponente.getTarifa();
			// }
			//
			// ConvenioComuna convenioComuna = conveniosDAO
			// .getConvenioComunaByIdComunaIdProgramaAno(
			// comuna.getId(), programa.getIdProgramaAno());
			// if (convenioComuna == null) {
			// continue;
			// }
			// ConvenioComunaComponente convenioComunaComponente = conveniosDAO
			// .getByIdConvenioComunaIdSubtituloIdComponente(
			// convenioComuna.getIdConvenioComuna(),
			// subtitulo.getId(), componenteVO.getId());
			//
			// if (convenioComunaComponente == null) {
			// continue;
			// }
			//
			// Long marcoTEMP = antecendentesComunaCalculado.getPercapitaAno()
			// + tarifa;
			// reporteMarcoPresupuestarioComunaVO.setMarco(marcoTEMP);
			//
			// Long conveniosTEMP = (long) convenioComunaComponente.getMonto();
			// reporteMarcoPresupuestarioComunaVO.setConvenios(conveniosTEMP);
			// Double porcentajeCuotaTransferida = 0.0;
			//
			// // TODO cambiar este valor despues
			// Long remesasAcumuladasTEMP = (long) (convenioComunaComponente
			// .getMonto() + 3);
			// if (Integer.parseInt(getMesCurso(true)) > 10) {
			// porcentajeCuotaTransferida = 1.0;
			// } else {
			// Cuota cuota = reliquidacionDAO
			// .getCuotaByIdProgramaAnoNroCuota(
			// programa.getIdProgramaAno(), (short) 1);
			// if (cuota.getIdMes().getIdMes() < Integer
			// .parseInt(getMesCurso(true))) {
			// porcentajeCuotaTransferida = (double) cuota
			// .getPorcentaje() / 100;
			// reporteMarcoPresupuestarioComunaVO
			// .setPorcentajeCuotaTransferida(porcentajeCuotaTransferida);
			// } else {
			// reporteMarcoPresupuestarioComunaVO
			// .setPorcentajeCuotaTransferida(0.6);
			// }
			// }
			// // TODO cambiar despues
			// reporteMarcoPresupuestarioComunaVO
			// .setPorcentajeCuotaTransferida(1.0);
			// reporteMarcoPresupuestarioComunaVO
			// .setRemesasAcumuladas(remesasAcumuladasTEMP);
			// reporteMarcoPresupuestarioComunaVO.setObservacion("");
			//
			// System.out.println("reporteMarcoPresupuestarioVO ---> "
			// + reporteMarcoPresupuestarioComunaVO.toString());
			//
			// resultado.add(reporteMarcoPresupuestarioComunaVO);
			ReporteMarcoPresupuestarioComunaVO reporteMarcoPresupuestarioComunaVO = new ReporteMarcoPresupuestarioComunaVO();
			reporteMarcoPresupuestarioComunaVO
					.setServicio(servicio.getNombre());
			reporteMarcoPresupuestarioComunaVO
					.setPrograma(programa.getNombre());
			reporteMarcoPresupuestarioComunaVO.setComponente(componenteVO
					.getNombre());
			reporteMarcoPresupuestarioComunaVO.setComuna(comuna.getNombre());

			// AntecendentesComunaCalculado antecendentesComunaCalculado =
			// antecedentesComunaDAO
			// .findByComunaAno(comuna.getId(), ano);
			// if (antecendentesComunaCalculado == null) {
			// continue;
			// }
			//
			// Long tarifa = 0L;
			// List<ProgramaMunicipalCoreComponente>
			// programaMunicipalCoreComponentes = programasDAO
			// .getByIdComuna(comuna.getId());
			// for (ProgramaMunicipalCoreComponente
			// programaMunicipalCoreComponente :
			// programaMunicipalCoreComponentes) {
			// if (programaMunicipalCoreComponente == null) {
			// continue;
			// }
			// tarifa += programaMunicipalCoreComponente.getTarifa();
			// }
			//
			// ConvenioComuna convenioComuna = conveniosDAO
			// .getConvenioComunaByIdComunaIdProgramaAno(
			// comuna.getId(), programa.getIdProgramaAno());
			// if (convenioComuna == null) {
			// continue;
			// }
			// ConvenioComunaComponente convenioComunaComponente = conveniosDAO
			// .getByIdConvenioComunaIdSubtituloIdComponente(
			// convenioComuna.getIdConvenioComuna(),
			// subtitulo.getId(), componenteVO.getId());
			//
			// if (convenioComunaComponente == null) {
			// continue;
			// }

			// Long marcoTEMP = antecendentesComunaCalculado.getPercapitaAno()
			// + tarifa;
			reporteMarcoPresupuestarioComunaVO.setMarco((long) 10
					* programa.getIdProgramaAno() * servicio.getId()
					* comuna.getId());

			// Long conveniosTEMP = (long) convenioComunaComponente.getMonto();
			reporteMarcoPresupuestarioComunaVO.setConvenios((long) 13
					* programa.getIdProgramaAno() * comuna.getId());
			Double porcentajeCuotaTransferida = 0.0;

			// TODO cambiar este valor despues
			// Long remesasAcumuladasTEMP = (long)
			// (convenioComunaComponente.getMonto() + 3);
			if (Integer.parseInt(getMesCurso(true)) > 10) {
				porcentajeCuotaTransferida = 1.0;
			} else {
				Cuota cuota = reliquidacionDAO.getCuotaByIdProgramaAnoNroCuota(
						programa.getIdProgramaAno(), (short) 1);
				if (cuota.getIdMes().getIdMes() < Integer
						.parseInt(getMesCurso(true))) {
					porcentajeCuotaTransferida = (double) cuota.getPorcentaje() / 100;
					reporteMarcoPresupuestarioComunaVO
							.setPorcentajeCuotaTransferida(porcentajeCuotaTransferida);
				} else {
					reporteMarcoPresupuestarioComunaVO
							.setPorcentajeCuotaTransferida(0.6);
				}
			}
			// TODO cambiar despues
			reporteMarcoPresupuestarioComunaVO
					.setPorcentajeCuotaTransferida(1.0);
			reporteMarcoPresupuestarioComunaVO
					.setRemesasAcumuladas((long) servicio.getId()
							* comuna.getId() * 6);
			reporteMarcoPresupuestarioComunaVO.setObservacion("");

			resultado.add(reporteMarcoPresupuestarioComunaVO);

		}

		return resultado;
	}

	public List<ReporteMarcoPresupuestarioEstablecimientoVO> getReporteMarcoPorServicio(
			Integer idProgramaAno, Integer idServicio, Subtitulo subtitulo) {

		Integer ano = getAnoCurso();
		List<ReporteMarcoPresupuestarioEstablecimientoVO> resultado = new ArrayList<ReporteMarcoPresupuestarioEstablecimientoVO>();
		ProgramaVO programa = programasService.getProgramaAno(idProgramaAno);

		ServicioSalud servicio = servicioSaludDAO
				.getServicioSaludById(idServicio);
		List<Establecimiento> establecimientos = servicio.getEstablecimientos();

		List<ComponentesVO> componentes = programasService.getComponenteByProgramaSubtitulos(programa.getId(), subtitulo);
		for (ComponentesVO componenteVO : componentes) {
			System.out.println("programa --> " + programa
					+ "  --  nombre del componente --> "
					+ componenteVO.getNombre());

			for (Establecimiento establecimiento : establecimientos) {

				ReporteMarcoPresupuestarioEstablecimientoVO reporteMarcoPresupuestarioEstablecimientoVO = new ReporteMarcoPresupuestarioEstablecimientoVO();

				reporteMarcoPresupuestarioEstablecimientoVO
						.setServicio(servicio.getNombre());
				reporteMarcoPresupuestarioEstablecimientoVO
						.setPrograma(programa.getNombre());
				reporteMarcoPresupuestarioEstablecimientoVO
						.setComponente(componenteVO.getNombre());
				reporteMarcoPresupuestarioEstablecimientoVO
						.setEstablecimiento(establecimiento.getNombre());

				// ProgramaServicioCore programaServicioCore =
				// programasDAO.getProgramaServicioCoreByProgramaAnoEstablecimiento(idProgramaAno,
				// establecimiento.getId());
				// if(programaServicioCore == null){
				// continue;
				// }
				// System.out.println("programaServicioCore --> "+programaServicioCore.getEstablecimiento());
				//
				// ProgramaServicioCoreComponente programaServicioCoreComponente
				// =
				// programasDAO.getProgramaServicioCoreComponenteByProgramaAnoEstablecimientoServicioComponenteSubtitulo(idProgramaAno,
				// establecimiento.getId(), idServicio, componenteVO.getId(),
				// subtitulo.getId());
				// if(programaServicioCoreComponente == null){
				// continue;
				// }

				// TODO cambiar esto por la consulta hecha
				reporteMarcoPresupuestarioEstablecimientoVO
						.setMarco((long) 15000 * establecimiento.getId());

				ConvenioServicioComponente convenioServicioComponente = conveniosDAO
						.getConvenioServicioComponenteByIdSubtituloIdComponente(
								idProgramaAno, componenteVO.getId(),
								subtitulo.getId(), establecimiento.getId());
				if (convenioServicioComponente == null) {
					System.out.println("convenioServicioComponente es null");
					continue;
				}
				reporteMarcoPresupuestarioEstablecimientoVO
						.setConvenios((long) convenioServicioComponente
								.getMonto());

				// TODO cambiar esto por la remesa. Cargar datos en la tabla 1ro
				reporteMarcoPresupuestarioEstablecimientoVO
						.setRemesasAcumuladas((long) (convenioServicioComponente
								.getMonto() - 3434));

				Cuota cuota = reliquidacionDAO.getCuotaByIdProgramaAnoNroCuota(
						programa.getIdProgramaAno(), (short) 1);
				if (cuota == null) {
					continue;
				}
				Double porcentajeCuotaTransferida = 0.0;
				System.out.println("getMesCurso(true) ---> "
						+ getMesCurso(true)
						+ "  cuota.getIdMes().getIdMes() --> "
						+ cuota.getIdMes().getIdMes());
				if (cuota.getIdMes().getIdMes() < Integer
						.parseInt(getMesCurso(true))) {
					porcentajeCuotaTransferida = (double) cuota.getPorcentaje() / 100;
					reporteMarcoPresupuestarioEstablecimientoVO
							.setPorcentajeCuotaTransferida(porcentajeCuotaTransferida);
				} else {
					reporteMarcoPresupuestarioEstablecimientoVO
							.setPorcentajeCuotaTransferida(0.6);
				}
				reporteMarcoPresupuestarioEstablecimientoVO.setObservacion("");

				resultado.add(reporteMarcoPresupuestarioEstablecimientoVO);

			}

			// //TODO cambiar despues
			// reporteMarcoPresupuestarioVO.setPorcentajeCuotaTransferida(1.0);
			// reporteMarcoPresupuestarioVO.setRemesasAcumuladas(remesasAcumuladasTEMP);
			// reporteMarcoPresupuestarioVO.setObservacion("");

		}

		return resultado;
	}

	public List<ReporteMarcoPresupuestarioEstablecimientoVO> getReporteMarcoPorServicioFiltroEstablecimiento(
			Integer idProgramaAno, Integer idServicio,
			Integer idEstablecimiento, Subtitulo subtitulo) {

		Integer ano = getAnoCurso();
		List<ReporteMarcoPresupuestarioEstablecimientoVO> resultado = new ArrayList<ReporteMarcoPresupuestarioEstablecimientoVO>();
		ProgramaVO programa = programasService.getProgramaAno(idProgramaAno);

		ServicioSalud servicio = servicioSaludDAO
				.getServicioSaludById(idServicio);

		Establecimiento establecimiento = establecimientosDAO
				.getEstablecimientoById(idEstablecimiento);

		List<ComponentesVO> componentes = programasService.getComponenteByProgramaSubtitulos(programa.getId(), subtitulo);
		for (ComponentesVO componenteVO : componentes) {
			System.out.println("programa --> " + programa
					+ "  --  nombre del componente --> "
					+ componenteVO.getNombre());

			ReporteMarcoPresupuestarioEstablecimientoVO reporteMarcoPresupuestarioEstablecimientoVO = new ReporteMarcoPresupuestarioEstablecimientoVO();

			reporteMarcoPresupuestarioEstablecimientoVO.setServicio(servicio
					.getNombre());
			reporteMarcoPresupuestarioEstablecimientoVO.setPrograma(programa
					.getNombre());
			reporteMarcoPresupuestarioEstablecimientoVO
					.setComponente(componenteVO.getNombre());
			reporteMarcoPresupuestarioEstablecimientoVO
					.setEstablecimiento(establecimiento.getNombre());

			// ProgramaServicioCore programaServicioCore =
			// programasDAO.getProgramaServicioCoreByProgramaAnoEstablecimiento(idProgramaAno,
			// establecimiento.getId());
			// if(programaServicioCore == null){
			// continue;
			// }
			// System.out.println("programaServicioCore --> "+programaServicioCore.getEstablecimiento());
			//
			// ProgramaServicioCoreComponente programaServicioCoreComponente
			// =
			// programasDAO.getProgramaServicioCoreComponenteByProgramaAnoEstablecimientoServicioComponenteSubtitulo(idProgramaAno,
			// establecimiento.getId(), idServicio, componenteVO.getId(),
			// subtitulo.getId());
			// if(programaServicioCoreComponente == null){
			// continue;
			// }

			// TODO cambiar esto por la consulta hecha
			reporteMarcoPresupuestarioEstablecimientoVO.setMarco((long) 15000
					* establecimiento.getId());

			ConvenioServicioComponente convenioServicioComponente = conveniosDAO
					.getConvenioServicioComponenteByIdSubtituloIdComponente(
							idProgramaAno, componenteVO.getId(),
							subtitulo.getId(), establecimiento.getId());
			if (convenioServicioComponente == null) {
				System.out.println("convenioServicioComponente es null");
				continue;
			}
			reporteMarcoPresupuestarioEstablecimientoVO
					.setConvenios((long) convenioServicioComponente.getMonto());

			// TODO cambiar esto por la remesa. Cargar datos en la tabla 1ro
			reporteMarcoPresupuestarioEstablecimientoVO
					.setRemesasAcumuladas((long) (convenioServicioComponente
							.getMonto() - 3434));

			Cuota cuota = reliquidacionDAO.getCuotaByIdProgramaAnoNroCuota(
					programa.getIdProgramaAno(), (short) 1);
			if (cuota == null) {
				continue;
			}
			Double porcentajeCuotaTransferida = 0.0;
			System.out.println("getMesCurso(true) ---> " + getMesCurso(true)
					+ "  cuota.getIdMes().getIdMes() --> "
					+ cuota.getIdMes().getIdMes());
			if (cuota.getIdMes().getIdMes() < Integer
					.parseInt(getMesCurso(true))) {
				porcentajeCuotaTransferida = (double) cuota.getPorcentaje() / 100;
				reporteMarcoPresupuestarioEstablecimientoVO
						.setPorcentajeCuotaTransferida(porcentajeCuotaTransferida);
			} else {
				reporteMarcoPresupuestarioEstablecimientoVO
						.setPorcentajeCuotaTransferida(0.6);
			}
			reporteMarcoPresupuestarioEstablecimientoVO.setObservacion("");

			resultado.add(reporteMarcoPresupuestarioEstablecimientoVO);

			// //TODO cambiar despues
			// reporteMarcoPresupuestarioVO.setPorcentajeCuotaTransferida(1.0);
			// reporteMarcoPresupuestarioVO.setRemesasAcumuladas(remesasAcumuladasTEMP);
			// reporteMarcoPresupuestarioVO.setObservacion("");

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

	public List<ReporteGlosaVO> getReporteGlosa(String user) {
		List<ReporteGlosaVO> resultado = new ArrayList<ReporteGlosaVO>();
		List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();

		for (ServicioSalud servicio : servicios) {
			List<Comuna> comunas = servicio.getComunas();
			for (Comuna comuna : comunas) {
				ReporteGlosaVO reporteGlosaVO = new ReporteGlosaVO();
				reporteGlosaVO.setRegion(servicio.getRegion().getNombre());
				reporteGlosaVO.setServicio(servicio.getNombre());
				reporteGlosaVO.setComuna(comuna.getNombre());

				AntecendentesComunaCalculado antecendentesComunaCalculado = antecedentesComunaDAO
						.findByComunaAnoVigencia(comuna.getId(), getAnoCurso());
				if (antecendentesComunaCalculado == null) {
					continue;
				}

				reporteGlosaVO.setArt49perCapita(((antecendentesComunaCalculado.getPercapitaAno() == null) ? 0L : antecendentesComunaCalculado.getPercapitaAno()));

				Long tarifa = 0L;
				Long totalRemesasAcumuladasMesActual = 0L;
				List<ProgramaMunicipalCoreComponente> programaMunicipalCoreComponentes = programasDAO
						.getByIdComuna(comuna.getId());
				for (ProgramaMunicipalCoreComponente programaMunicipalCoreComponente : programaMunicipalCoreComponentes) {
					if (programaMunicipalCoreComponente == null) {
						continue;
					}
					tarifa += programaMunicipalCoreComponente.getTarifa();
				}
				Integer mesActual = Integer.parseInt(getMesCurso(true));
				
				
				for(ProgramaVO programaVO : programasService.getProgramasByUser(user)){
					totalRemesasAcumuladasMesActual+= remesasDAO.getRemesasPagadasComunaPrograma(programaVO.getIdProgramaAno(), comuna.getId(), mesActual);
				}
				reporteGlosaVO.setArt56reforzamientoMunicipal(tarifa);
				reporteGlosaVO.setTotalRemesasEneroMarzo(totalRemesasAcumuladasMesActual);
				resultado.add(reporteGlosaVO);
			}
		}

		return resultado;
	}

	public List<ReporteGlosaVO> getReporteGlosaPorServicio(Integer idServicio, String user) {
		List<ReporteGlosaVO> resultado = new ArrayList<ReporteGlosaVO>();
		ServicioSalud servicio = servicioSaludDAO
				.getServicioSaludById(idServicio);

		List<Comuna> comunas = servicio.getComunas();
		for (Comuna comuna : comunas) {
			ReporteGlosaVO reporteGlosaVO = new ReporteGlosaVO();
			reporteGlosaVO.setRegion(servicio.getRegion().getNombre());
			reporteGlosaVO.setServicio(servicio.getNombre());
			reporteGlosaVO.setComuna(comuna.getNombre());

			AntecendentesComunaCalculado antecendentesComunaCalculado = antecedentesComunaDAO
					.findByComunaAno(comuna.getId(), getAnoCurso());
			if (antecendentesComunaCalculado == null) {
				continue;
			}

			reporteGlosaVO.setArt49perCapita(((antecendentesComunaCalculado.getPercapitaAno() == null) ? 0L : antecendentesComunaCalculado.getPercapitaAno()));

			Long tarifa = 0L;
			Long totalRemesasAcumuladasMesActual = 0L;
			List<ProgramaMunicipalCoreComponente> programaMunicipalCoreComponentes = programasDAO
					.getByIdComuna(comuna.getId());
			for (ProgramaMunicipalCoreComponente programaMunicipalCoreComponente : programaMunicipalCoreComponentes) {
				if (programaMunicipalCoreComponente == null) {
					continue;
				}
				tarifa += programaMunicipalCoreComponente.getTarifa();
			}
			Integer mesActual = Integer.parseInt(getMesCurso(true));
			
			for(ProgramaVO programaVO : programasService.getProgramasByUser(user)){
				totalRemesasAcumuladasMesActual += remesasDAO.getRemesasPagadasComunaPrograma(programaVO.getIdProgramaAno(), comuna.getId(), mesActual);
			}
			
			reporteGlosaVO.setArt56reforzamientoMunicipal(tarifa);
			reporteGlosaVO.setTotalRemesasEneroMarzo(totalRemesasAcumuladasMesActual);
			resultado.add(reporteGlosaVO);
		}

		return resultado;
	}

	public List<ReporteHistoricoPorProgramaComunaVO> getReporteHistoricoComunaAll(
			Subtitulo subtitulo) {

		List<ReporteHistoricoPorProgramaComunaVO> resultado = new ArrayList<ReporteHistoricoPorProgramaComunaVO>();

		List<ProgramaVO> programas = programasService
				.getProgramasBySubtitulo(subtitulo);

		List<ServicioSalud> serviciosSalud = servicioSaludDAO
				.getServiciosOrderId();

		for (ProgramaVO programa : programas) {
			for (ServicioSalud servicio : serviciosSalud) {
				System.out.println("servicioVO.getNombre_servicio() --> "
						+ servicio.getNombre());
				// List<Comuna> comunas = servicio.getComunas();

				for (Comuna comuna : servicio.getComunas()) {
					ReporteHistoricoPorProgramaComunaVO reporteHistoricoPorProgramaComunaVO = new ReporteHistoricoPorProgramaComunaVO();

					reporteHistoricoPorProgramaComunaVO.setRegion(servicio
							.getRegion().getNombre());
					reporteHistoricoPorProgramaComunaVO.setServicio(servicio
							.getNombre());
					reporteHistoricoPorProgramaComunaVO.setComuna(comuna
							.getNombre());
					reporteHistoricoPorProgramaComunaVO.setPrograma(programa
							.getNombre());
					AntecendentesComunaCalculado antecendentesComunaCalculado2006 = null;
					AntecendentesComunaCalculado antecendentesComunaCalculado2007 = null;
					AntecendentesComunaCalculado antecendentesComunaCalculado2008 = null;
					AntecendentesComunaCalculado antecendentesComunaCalculado2009 = null;
					AntecendentesComunaCalculado antecendentesComunaCalculado2010 = null;
					AntecendentesComunaCalculado antecendentesComunaCalculado2011 = null;
					AntecendentesComunaCalculado antecendentesComunaCalculado2012 = null;
					AntecendentesComunaCalculado antecendentesComunaCalculado2013 = null;
					AntecendentesComunaCalculado antecendentesComunaCalculado2014 = null;

					// ********************Año 2006 ****************************

					AntecendentesComuna antecendentesComuna2006 = antecedentesComunaDAO
							.findAntecendentesComunaByComunaServicioAno(
									servicio.getNombre(), comuna.getNombre(),
									2006);
					if (antecendentesComuna2006 == null) {
						reporteHistoricoPorProgramaComunaVO.setMarco2006(0L);
					} else {
						antecendentesComunaCalculado2006 = antecedentesComunaDAO
								.findByIdAntecedentesComuna(antecendentesComuna2006
										.getIdAntecedentesComuna());
						if (antecendentesComunaCalculado2006 == null) {
							reporteHistoricoPorProgramaComunaVO
									.setMarco2006(0L);
						}
					}

					// ***************** Fin año 2006
					// *****************************

					// ********************Año 2007 ****************************

					AntecendentesComuna antecendentesComuna2007 = antecedentesComunaDAO
							.findAntecendentesComunaByComunaServicioAno(
									servicio.getNombre(), comuna.getNombre(),
									2007);
					if (antecendentesComuna2007 == null) {
						reporteHistoricoPorProgramaComunaVO.setMarco2007(0L);
					} else {
						antecendentesComunaCalculado2007 = antecedentesComunaDAO
								.findByIdAntecedentesComuna(antecendentesComuna2007
										.getIdAntecedentesComuna());
						if (antecendentesComunaCalculado2007 == null) {
							reporteHistoricoPorProgramaComunaVO
									.setMarco2007(0L);
						}
					}

					// ***************** Fin año 2007
					// *****************************

					// ********************Año 2008 ****************************

					AntecendentesComuna antecendentesComuna2008 = antecedentesComunaDAO
							.findAntecendentesComunaByComunaServicioAno(
									servicio.getNombre(), comuna.getNombre(),
									2008);
					if (antecendentesComuna2008 == null) {
						reporteHistoricoPorProgramaComunaVO.setMarco2008(0L);
					} else {
						antecendentesComunaCalculado2008 = antecedentesComunaDAO
								.findByIdAntecedentesComuna(antecendentesComuna2008
										.getIdAntecedentesComuna());
						if (antecendentesComunaCalculado2008 == null) {
							reporteHistoricoPorProgramaComunaVO
									.setMarco2008(0L);
						}
					}

					// ***************** Fin año 2008
					// *****************************

					// ********************Año 2009 ****************************

					AntecendentesComuna antecendentesComuna2009 = antecedentesComunaDAO
							.findAntecendentesComunaByComunaServicioAno(
									servicio.getNombre(), comuna.getNombre(),
									2009);
					if (antecendentesComuna2009 == null) {
						reporteHistoricoPorProgramaComunaVO.setMarco2009(0L);
					} else {
						antecendentesComunaCalculado2009 = antecedentesComunaDAO
								.findByIdAntecedentesComuna(antecendentesComuna2009
										.getIdAntecedentesComuna());
						if (antecendentesComunaCalculado2009 == null) {
							reporteHistoricoPorProgramaComunaVO
									.setMarco2009(0L);
						}
					}

					// ***************** Fin año 2009
					// *****************************

					// ********************Año 2010 ****************************

					AntecendentesComuna antecendentesComuna2010 = antecedentesComunaDAO
							.findAntecendentesComunaByComunaServicioAno(
									servicio.getNombre(), comuna.getNombre(),
									2010);
					if (antecendentesComuna2010 == null) {
						reporteHistoricoPorProgramaComunaVO.setMarco2010(0L);
					} else {
						antecendentesComunaCalculado2010 = antecedentesComunaDAO
								.findByIdAntecedentesComuna(antecendentesComuna2010
										.getIdAntecedentesComuna());
						if (antecendentesComunaCalculado2010 == null) {
							reporteHistoricoPorProgramaComunaVO
									.setMarco2010(0L);
						}
					}

					// ***************** Fin año 2010
					// *****************************

					// ********************Año 2011 ****************************

					AntecendentesComuna antecendentesComuna2011 = antecedentesComunaDAO
							.findAntecendentesComunaByComunaServicioAno(
									servicio.getNombre(), comuna.getNombre(),
									2011);
					if (antecendentesComuna2011 == null) {
						reporteHistoricoPorProgramaComunaVO.setMarco2011(0L);
					} else {
						antecendentesComunaCalculado2011 = antecedentesComunaDAO
								.findByIdAntecedentesComuna(antecendentesComuna2011
										.getIdAntecedentesComuna());
						if (antecendentesComunaCalculado2011 == null) {
							reporteHistoricoPorProgramaComunaVO
									.setMarco2011(0L);
						}
					}

					// ***************** Fin año 2011
					// *****************************

					// ********************Año 2012 ****************************

					AntecendentesComuna antecendentesComuna2012 = antecedentesComunaDAO
							.findAntecendentesComunaByComunaServicioAno(
									servicio.getNombre(), comuna.getNombre(),
									2012);
					if (antecendentesComuna2012 == null) {
						reporteHistoricoPorProgramaComunaVO.setMarco2012(0L);
					} else {
						antecendentesComunaCalculado2012 = antecedentesComunaDAO
								.findByIdAntecedentesComuna(antecendentesComuna2012
										.getIdAntecedentesComuna());
						if (antecendentesComunaCalculado2012 == null) {
							reporteHistoricoPorProgramaComunaVO
									.setMarco2012(0L);
						}
					}

					// ***************** Fin año 2012
					// *****************************

					// ********************Año 2013 ****************************

					AntecendentesComuna antecendentesComuna2013 = antecedentesComunaDAO
							.findAntecendentesComunaByComunaServicioAno(
									servicio.getNombre(), comuna.getNombre(),
									2013);
					if (antecendentesComuna2013 == null) {
						reporteHistoricoPorProgramaComunaVO.setMarco2013(0L);
					} else {
						antecendentesComunaCalculado2013 = antecedentesComunaDAO
								.findByIdAntecedentesComuna(antecendentesComuna2013
										.getIdAntecedentesComuna());
						if (antecendentesComunaCalculado2013 == null) {
							reporteHistoricoPorProgramaComunaVO
									.setMarco2013(0L);
						}
					}

					// ***************** Fin año 2013
					// *****************************

					// ********************Año 2014 ****************************

					AntecendentesComuna antecendentesComuna2014 = antecedentesComunaDAO
							.findAntecendentesComunaByComunaServicioAno(
									servicio.getNombre(), comuna.getNombre(),
									2014);
					if (antecendentesComuna2014 == null) {
						reporteHistoricoPorProgramaComunaVO.setMarco2014(0L);
					} else {
						antecendentesComunaCalculado2014 = antecedentesComunaDAO
								.findByIdAntecedentesComuna(antecendentesComuna2014
										.getIdAntecedentesComuna());
						if (antecendentesComunaCalculado2014 == null) {
							reporteHistoricoPorProgramaComunaVO
									.setMarco2014(0L);
						}
					}

					// ***************** Fin año 2014
					// *****************************

					// TODO cambiar el marco
					// if(antecendentesComunaCalculado2006 == null){
					reporteHistoricoPorProgramaComunaVO.setMarco2006(0L);
					// }else{
					// reporteHistoricoPorProgramaVO.setMarco2006(antecendentesComunaCalculado2006.getPercapitaAno()
					// + antecendentesComunaCalculado2006.getPercapitaMes()*4);
					// }

					// TODO cambiar el marco
					// if(antecendentesComunaCalculado2007 == null){
					reporteHistoricoPorProgramaComunaVO.setMarco2007(0L);
					// }else{
					// reporteHistoricoPorProgramaVO.setMarco2007(antecendentesComunaCalculado2007.getPercapitaAno()
					// + antecendentesComunaCalculado2007.getPercapitaMes()*3);
					// }

					// TODO cambiar el marco
					// if(antecendentesComunaCalculado2008 == null){
					reporteHistoricoPorProgramaComunaVO.setMarco2008(0L);
					// }else{
					// reporteHistoricoPorProgramaVO.setMarco2008(antecendentesComunaCalculado2008.getPercapitaAno()
					// + antecendentesComunaCalculado2008.getPercapitaMes()*6);
					// }
					//
					// //TODO cambiar el marco
					// if(antecendentesComunaCalculado2009 == null){
					reporteHistoricoPorProgramaComunaVO.setMarco2009(0L);
					// }else{
					// reporteHistoricoPorProgramaVO.setMarco2009(antecendentesComunaCalculado2009.getPercapitaAno()
					// + antecendentesComunaCalculado2009.getPercapitaMes()*2);
					// }
					//
					// //TODO cambiar el marco
					// if(antecendentesComunaCalculado2010 == null){
					reporteHistoricoPorProgramaComunaVO.setMarco2010(0L);
					// }else{
					// reporteHistoricoPorProgramaVO.setMarco2010(antecendentesComunaCalculado2010.getPercapitaAno()
					// + antecendentesComunaCalculado2010.getPercapitaMes()*5);
					// }
					//
					// //TODO cambiar el marco
					// if(antecendentesComunaCalculado2011 == null){
					reporteHistoricoPorProgramaComunaVO.setMarco2011(0L);
					// }else{
					// reporteHistoricoPorProgramaVO.setMarco2011(antecendentesComunaCalculado2011.getPercapitaAno()
					// + antecendentesComunaCalculado2011.getPercapitaMes()*8);
					// }
					// TODO cambiar el marco
					if (antecendentesComunaCalculado2012 == null
							|| antecendentesComunaCalculado2012
									.getPercapitaMes() == null) {
						reporteHistoricoPorProgramaComunaVO.setMarco2012(0L);
					} else {
						reporteHistoricoPorProgramaComunaVO
								.setMarco2012(antecendentesComunaCalculado2012
										.getPercapitaAno()
										+ antecendentesComunaCalculado2012
												.getPercapitaMes() * 3);
					}

					// TODO cambiar el marco
					if (antecendentesComunaCalculado2013 == null
							|| antecendentesComunaCalculado2013
									.getPercapitaMes() == null) {
						reporteHistoricoPorProgramaComunaVO.setMarco2013(0L);
					} else {
						reporteHistoricoPorProgramaComunaVO
								.setMarco2013(antecendentesComunaCalculado2013
										.getPercapitaAno()
										+ antecendentesComunaCalculado2013
												.getPercapitaMes() * 4);
					}

					// TODO cambiar el marco
					if (antecendentesComunaCalculado2014 == null
							|| antecendentesComunaCalculado2014
									.getPercapitaMes() == null) {
						continue;
					} else {
						System.out
								.println("antecendentesComunaCalculado2014.getPercapitaAno() --> "
										+ antecendentesComunaCalculado2014
												.getPercapitaAno());
						System.out
								.println("antecendentesComunaCalculado2014.getPercapitaMes() --> "
										+ antecendentesComunaCalculado2014
												.getPercapitaMes());

						reporteHistoricoPorProgramaComunaVO
								.setMarco2014(antecendentesComunaCalculado2014
										.getPercapitaAno()
										+ antecendentesComunaCalculado2014
												.getPercapitaMes() * 4);
					}

					resultado.add(reporteHistoricoPorProgramaComunaVO);

				}
			}
		}

		return resultado;

	}

	public List<ReporteHistoricoPorProgramaEstablecimientoVO> getReporteHistoricoEstablecimientoAll(
			Subtitulo subtitulo) {

		List<ReporteHistoricoPorProgramaEstablecimientoVO> resultado = new ArrayList<ReporteHistoricoPorProgramaEstablecimientoVO>();

		List<ProgramaVO> programas = programasService
				.getProgramasBySubtitulo(subtitulo);

		List<ServicioSalud> serviciosSalud = servicioSaludDAO
				.getServiciosOrderId();

		for (ProgramaVO programa : programas) {
			for (ServicioSalud servicio : serviciosSalud) {
				System.out.println("servicioVO.getNombre_servicio() --> "
						+ servicio.getNombre());
				// List<Comuna> comunas = servicio.getComunas();

				for (Establecimiento establecimiento : servicio
						.getEstablecimientos()) {
					ReporteHistoricoPorProgramaEstablecimientoVO reporteHistoricoPorProgramaEstablecimientoVO = new ReporteHistoricoPorProgramaEstablecimientoVO();

					reporteHistoricoPorProgramaEstablecimientoVO
							.setRegion(servicio.getRegion().getNombre());
					reporteHistoricoPorProgramaEstablecimientoVO
							.setServicio(servicio.getNombre());
					reporteHistoricoPorProgramaEstablecimientoVO
							.setEstablecimiento(establecimiento.getNombre());
					reporteHistoricoPorProgramaEstablecimientoVO
							.setPrograma(programa.getNombre());

					// TODO cambiar el marco
					// if(antecendentesComunaCalculado2006 == null){
					reporteHistoricoPorProgramaEstablecimientoVO
							.setMarco2006((long) programa.getIdProgramaAno()
									* servicio.getId()
									* establecimiento.getId() * 3 * 2006);
					// }else{
					// reporteHistoricoPorProgramaVO.setMarco2006(antecendentesComunaCalculado2006.getPercapitaAno()
					// + antecendentesComunaCalculado2006.getPercapitaMes()*4);
					// }

					// TODO cambiar el marco
					// if(antecendentesComunaCalculado2007 == null){
					reporteHistoricoPorProgramaEstablecimientoVO
							.setMarco2007((long) programa.getIdProgramaAno()
									* servicio.getId()
									* establecimiento.getId() * 3 * 2007);
					// }else{
					// reporteHistoricoPorProgramaVO.setMarco2007(antecendentesComunaCalculado2007.getPercapitaAno()
					// + antecendentesComunaCalculado2007.getPercapitaMes()*3);
					// }

					// TODO cambiar el marco
					// if(antecendentesComunaCalculado2008 == null){
					reporteHistoricoPorProgramaEstablecimientoVO
							.setMarco2008((long) programa.getIdProgramaAno()
									* servicio.getId()
									* establecimiento.getId() * 3 * 2009);
					// }else{
					// reporteHistoricoPorProgramaVO.setMarco2008(antecendentesComunaCalculado2008.getPercapitaAno()
					// + antecendentesComunaCalculado2008.getPercapitaMes()*6);
					// }
					//
					// //TODO cambiar el marco
					// if(antecendentesComunaCalculado2009 == null){
					reporteHistoricoPorProgramaEstablecimientoVO
							.setMarco2009((long) programa.getIdProgramaAno()
									* servicio.getId()
									* establecimiento.getId() * 3 * 2009);
					// }else{
					// reporteHistoricoPorProgramaVO.setMarco2009(antecendentesComunaCalculado2009.getPercapitaAno()
					// + antecendentesComunaCalculado2009.getPercapitaMes()*2);
					// }
					//
					// //TODO cambiar el marco
					// if(antecendentesComunaCalculado2010 == null){
					reporteHistoricoPorProgramaEstablecimientoVO
							.setMarco2010((long) programa.getIdProgramaAno()
									* servicio.getId()
									* establecimiento.getId() * 3 * 2010);
					// }else{
					// reporteHistoricoPorProgramaVO.setMarco2010(antecendentesComunaCalculado2010.getPercapitaAno()
					// + antecendentesComunaCalculado2010.getPercapitaMes()*5);
					// }
					//
					// //TODO cambiar el marco
					// if(antecendentesComunaCalculado2011 == null){
					reporteHistoricoPorProgramaEstablecimientoVO
							.setMarco2011((long) programa.getIdProgramaAno()
									* servicio.getId()
									* establecimiento.getId() * 3 * 2011);

					reporteHistoricoPorProgramaEstablecimientoVO
							.setMarco2012((long) programa.getIdProgramaAno()
									* servicio.getId()
									* establecimiento.getId() * 3 * 2012);

					reporteHistoricoPorProgramaEstablecimientoVO
							.setMarco2013((long) programa.getIdProgramaAno()
									* servicio.getId()
									* establecimiento.getId() * 3 * 2013);

					reporteHistoricoPorProgramaEstablecimientoVO
							.setMarco2014((long) programa.getIdProgramaAno()
									* servicio.getId()
									* establecimiento.getId() * 3 * 2014);
					resultado.add(reporteHistoricoPorProgramaEstablecimientoVO);

				}
			}
		}

		return resultado;

	}

	public List<ReporteHistoricoPorProgramaComunaVO> getReporteHistoricoPorProgramaVOAll(
			Integer idProgramaAno, Subtitulo subtitulo) {
		System.out.println("idProgramaAno --> " + idProgramaAno
				+ "  subtitulo --> " + subtitulo.getNombre());

		List<ReporteHistoricoPorProgramaComunaVO> resultado = new ArrayList<ReporteHistoricoPorProgramaComunaVO>();
		ProgramaVO programa = programasService
				.getProgramaAnoPorID(idProgramaAno);
		List<ServicioSalud> serviciosSalud = servicioSaludDAO
				.getServiciosOrderId();
		for (ServicioSalud servicio : serviciosSalud) {
			System.out.println("servicioVO.getNombre_servicio() --> "
					+ servicio.getNombre());
			// List<Comuna> comunas = servicio.getComunas();

			for (Comuna comuna : servicio.getComunas()) {
				ReporteHistoricoPorProgramaComunaVO reporteHistoricoPorProgramaComunaVO = new ReporteHistoricoPorProgramaComunaVO();

				reporteHistoricoPorProgramaComunaVO.setRegion(servicio
						.getRegion().getNombre());
				reporteHistoricoPorProgramaComunaVO.setServicio(servicio
						.getNombre());
				reporteHistoricoPorProgramaComunaVO.setComuna(comuna
						.getNombre());
				reporteHistoricoPorProgramaComunaVO.setPrograma(programa
						.getNombre());
				AntecendentesComunaCalculado antecendentesComunaCalculado2006 = null;
				AntecendentesComunaCalculado antecendentesComunaCalculado2007 = null;
				AntecendentesComunaCalculado antecendentesComunaCalculado2008 = null;
				AntecendentesComunaCalculado antecendentesComunaCalculado2009 = null;
				AntecendentesComunaCalculado antecendentesComunaCalculado2010 = null;
				AntecendentesComunaCalculado antecendentesComunaCalculado2011 = null;
				AntecendentesComunaCalculado antecendentesComunaCalculado2012 = null;
				AntecendentesComunaCalculado antecendentesComunaCalculado2013 = null;
				AntecendentesComunaCalculado antecendentesComunaCalculado2014 = null;

				// ********************Año 2006 ****************************

				AntecendentesComuna antecendentesComuna2006 = antecedentesComunaDAO
						.findAntecendentesComunaByComunaServicioAno(
								servicio.getNombre(), comuna.getNombre(), 2006);
				if (antecendentesComuna2006 == null) {
					reporteHistoricoPorProgramaComunaVO.setMarco2006(0L);
				} else {
					antecendentesComunaCalculado2006 = antecedentesComunaDAO
							.findByIdAntecedentesComuna(antecendentesComuna2006
									.getIdAntecedentesComuna());
					if (antecendentesComunaCalculado2006 == null) {
						reporteHistoricoPorProgramaComunaVO.setMarco2006(0L);
					}
				}

				// ***************** Fin año 2006 *****************************

				// ********************Año 2007 ****************************

				AntecendentesComuna antecendentesComuna2007 = antecedentesComunaDAO
						.findAntecendentesComunaByComunaServicioAno(
								servicio.getNombre(), comuna.getNombre(), 2007);
				if (antecendentesComuna2007 == null) {
					reporteHistoricoPorProgramaComunaVO.setMarco2007(0L);
				} else {
					antecendentesComunaCalculado2007 = antecedentesComunaDAO
							.findByIdAntecedentesComuna(antecendentesComuna2007
									.getIdAntecedentesComuna());
					if (antecendentesComunaCalculado2007 == null) {
						reporteHistoricoPorProgramaComunaVO.setMarco2007(0L);
					}
				}

				// ***************** Fin año 2007 *****************************

				// ********************Año 2008 ****************************

				AntecendentesComuna antecendentesComuna2008 = antecedentesComunaDAO
						.findAntecendentesComunaByComunaServicioAno(
								servicio.getNombre(), comuna.getNombre(), 2008);
				if (antecendentesComuna2008 == null) {
					reporteHistoricoPorProgramaComunaVO.setMarco2008(0L);
				} else {
					antecendentesComunaCalculado2008 = antecedentesComunaDAO
							.findByIdAntecedentesComuna(antecendentesComuna2008
									.getIdAntecedentesComuna());
					if (antecendentesComunaCalculado2008 == null) {
						reporteHistoricoPorProgramaComunaVO.setMarco2008(0L);
					}
				}

				// ***************** Fin año 2008 *****************************

				// ********************Año 2009 ****************************

				AntecendentesComuna antecendentesComuna2009 = antecedentesComunaDAO
						.findAntecendentesComunaByComunaServicioAno(
								servicio.getNombre(), comuna.getNombre(), 2009);
				if (antecendentesComuna2009 == null) {
					reporteHistoricoPorProgramaComunaVO.setMarco2009(0L);
				} else {
					antecendentesComunaCalculado2009 = antecedentesComunaDAO
							.findByIdAntecedentesComuna(antecendentesComuna2009
									.getIdAntecedentesComuna());
					if (antecendentesComunaCalculado2009 == null) {
						reporteHistoricoPorProgramaComunaVO.setMarco2009(0L);
					}
				}

				// ***************** Fin año 2009 *****************************

				// ********************Año 2010 ****************************

				AntecendentesComuna antecendentesComuna2010 = antecedentesComunaDAO
						.findAntecendentesComunaByComunaServicioAno(
								servicio.getNombre(), comuna.getNombre(), 2010);
				if (antecendentesComuna2010 == null) {
					reporteHistoricoPorProgramaComunaVO.setMarco2010(0L);
				} else {
					antecendentesComunaCalculado2010 = antecedentesComunaDAO
							.findByIdAntecedentesComuna(antecendentesComuna2010
									.getIdAntecedentesComuna());
					if (antecendentesComunaCalculado2010 == null) {
						reporteHistoricoPorProgramaComunaVO.setMarco2010(0L);
					}
				}

				// ***************** Fin año 2010 *****************************

				// ********************Año 2011 ****************************

				AntecendentesComuna antecendentesComuna2011 = antecedentesComunaDAO
						.findAntecendentesComunaByComunaServicioAno(
								servicio.getNombre(), comuna.getNombre(), 2011);
				if (antecendentesComuna2011 == null) {
					reporteHistoricoPorProgramaComunaVO.setMarco2011(0L);
				} else {
					antecendentesComunaCalculado2011 = antecedentesComunaDAO
							.findByIdAntecedentesComuna(antecendentesComuna2011
									.getIdAntecedentesComuna());
					if (antecendentesComunaCalculado2011 == null) {
						reporteHistoricoPorProgramaComunaVO.setMarco2011(0L);
					}
				}

				// ***************** Fin año 2011 *****************************

				// ********************Año 2012 ****************************

				AntecendentesComuna antecendentesComuna2012 = antecedentesComunaDAO
						.findAntecendentesComunaByComunaServicioAno(
								servicio.getNombre(), comuna.getNombre(), 2012);
				if (antecendentesComuna2012 == null) {
					reporteHistoricoPorProgramaComunaVO.setMarco2012(0L);
				} else {
					antecendentesComunaCalculado2012 = antecedentesComunaDAO
							.findByIdAntecedentesComuna(antecendentesComuna2012
									.getIdAntecedentesComuna());
					if (antecendentesComunaCalculado2012 == null) {
						reporteHistoricoPorProgramaComunaVO.setMarco2012(0L);
					}
				}

				// ***************** Fin año 2012 *****************************

				// ********************Año 2013 ****************************

				AntecendentesComuna antecendentesComuna2013 = antecedentesComunaDAO
						.findAntecendentesComunaByComunaServicioAno(
								servicio.getNombre(), comuna.getNombre(), 2013);
				if (antecendentesComuna2013 == null) {
					reporteHistoricoPorProgramaComunaVO.setMarco2013(0L);
				} else {
					antecendentesComunaCalculado2013 = antecedentesComunaDAO
							.findByIdAntecedentesComuna(antecendentesComuna2013
									.getIdAntecedentesComuna());
					if (antecendentesComunaCalculado2013 == null) {
						reporteHistoricoPorProgramaComunaVO.setMarco2013(0L);
					}
				}

				// ***************** Fin año 2013 *****************************

				// ********************Año 2014 ****************************

				AntecendentesComuna antecendentesComuna2014 = antecedentesComunaDAO
						.findAntecendentesComunaByComunaServicioAno(
								servicio.getNombre(), comuna.getNombre(), 2014);
				if (antecendentesComuna2014 == null) {
					reporteHistoricoPorProgramaComunaVO.setMarco2014(0L);
				} else {
					antecendentesComunaCalculado2014 = antecedentesComunaDAO
							.findByIdAntecedentesComuna(antecendentesComuna2014
									.getIdAntecedentesComuna());
					if (antecendentesComunaCalculado2014 == null) {
						reporteHistoricoPorProgramaComunaVO.setMarco2014(0L);
					}
				}

				// ***************** Fin año 2014 *****************************

				// TODO cambiar el marco
				// if(antecendentesComunaCalculado2006 == null){
				reporteHistoricoPorProgramaComunaVO.setMarco2006(0L);
				// }else{
				// reporteHistoricoPorProgramaVO.setMarco2006(antecendentesComunaCalculado2006.getPercapitaAno()
				// + antecendentesComunaCalculado2006.getPercapitaMes()*4);
				// }

				// TODO cambiar el marco
				// if(antecendentesComunaCalculado2007 == null){
				reporteHistoricoPorProgramaComunaVO.setMarco2007(0L);
				// }else{
				// reporteHistoricoPorProgramaVO.setMarco2007(antecendentesComunaCalculado2007.getPercapitaAno()
				// + antecendentesComunaCalculado2007.getPercapitaMes()*3);
				// }

				// TODO cambiar el marco
				// if(antecendentesComunaCalculado2008 == null){
				reporteHistoricoPorProgramaComunaVO.setMarco2008(0L);
				// }else{
				// reporteHistoricoPorProgramaVO.setMarco2008(antecendentesComunaCalculado2008.getPercapitaAno()
				// + antecendentesComunaCalculado2008.getPercapitaMes()*6);
				// }
				//
				// //TODO cambiar el marco
				// if(antecendentesComunaCalculado2009 == null){
				reporteHistoricoPorProgramaComunaVO.setMarco2009(0L);
				// }else{
				// reporteHistoricoPorProgramaVO.setMarco2009(antecendentesComunaCalculado2009.getPercapitaAno()
				// + antecendentesComunaCalculado2009.getPercapitaMes()*2);
				// }
				//
				// //TODO cambiar el marco
				// if(antecendentesComunaCalculado2010 == null){
				reporteHistoricoPorProgramaComunaVO.setMarco2010(0L);
				// }else{
				// reporteHistoricoPorProgramaVO.setMarco2010(antecendentesComunaCalculado2010.getPercapitaAno()
				// + antecendentesComunaCalculado2010.getPercapitaMes()*5);
				// }
				//
				// //TODO cambiar el marco
				// if(antecendentesComunaCalculado2011 == null){
				reporteHistoricoPorProgramaComunaVO.setMarco2011(0L);
				// }else{
				// reporteHistoricoPorProgramaVO.setMarco2011(antecendentesComunaCalculado2011.getPercapitaAno()
				// + antecendentesComunaCalculado2011.getPercapitaMes()*8);
				// }
				// TODO cambiar el marco
				if (antecendentesComunaCalculado2012 == null
						|| antecendentesComunaCalculado2012.getPercapitaMes() == null) {
					reporteHistoricoPorProgramaComunaVO.setMarco2012(0L);
				} else {
					reporteHistoricoPorProgramaComunaVO
							.setMarco2012(antecendentesComunaCalculado2012
									.getPercapitaAno()
									+ antecendentesComunaCalculado2012
											.getPercapitaMes() * 3);
				}

				// TODO cambiar el marco
				if (antecendentesComunaCalculado2013 == null
						|| antecendentesComunaCalculado2013.getPercapitaMes() == null) {
					reporteHistoricoPorProgramaComunaVO.setMarco2013(0L);
				} else {
					reporteHistoricoPorProgramaComunaVO
							.setMarco2013(antecendentesComunaCalculado2013
									.getPercapitaAno()
									+ antecendentesComunaCalculado2013
											.getPercapitaMes() * 4);
				}

				// TODO cambiar el marco
				if (antecendentesComunaCalculado2014 == null
						|| antecendentesComunaCalculado2014.getPercapitaMes() == null) {
					continue;
				} else {
					System.out
							.println("antecendentesComunaCalculado2014.getPercapitaAno() --> "
									+ antecendentesComunaCalculado2014
											.getPercapitaAno());
					System.out
							.println("antecendentesComunaCalculado2014.getPercapitaMes() --> "
									+ antecendentesComunaCalculado2014
											.getPercapitaMes());

					reporteHistoricoPorProgramaComunaVO
							.setMarco2014(antecendentesComunaCalculado2014
									.getPercapitaAno()
									+ antecendentesComunaCalculado2014
											.getPercapitaMes() * 4);
				}

				resultado.add(reporteHistoricoPorProgramaComunaVO);

			}
		}

		return resultado;

	}

	public List<ReporteHistoricoPorProgramaEstablecimientoVO> getReporteHistoricoEstablecimientoPorProgramaVOAll(
			Integer idProgramaAno, Subtitulo subtitulo) {
		System.out.println("idProgramaAno --> " + idProgramaAno
				+ "  subtitulo --> " + subtitulo.getNombre());

		List<ReporteHistoricoPorProgramaEstablecimientoVO> resultado = new ArrayList<ReporteHistoricoPorProgramaEstablecimientoVO>();
		ProgramaVO programa = programasService
				.getProgramaAnoPorID(idProgramaAno);
		List<ServicioSalud> serviciosSalud = servicioSaludDAO
				.getServiciosOrderId();
		for (ServicioSalud servicio : serviciosSalud) {
			System.out.println("servicioVO.getNombre_servicio() --> "
					+ servicio.getNombre());
			// List<Comuna> comunas = servicio.getComunas();

			for (Establecimiento establecimiento : servicio
					.getEstablecimientos()) {
				ReporteHistoricoPorProgramaEstablecimientoVO reporteHistoricoPorProgramaEstablecimientoVO = new ReporteHistoricoPorProgramaEstablecimientoVO();

				reporteHistoricoPorProgramaEstablecimientoVO.setRegion(servicio
						.getRegion().getNombre());
				reporteHistoricoPorProgramaEstablecimientoVO
						.setServicio(servicio.getNombre());
				reporteHistoricoPorProgramaEstablecimientoVO
						.setEstablecimiento(establecimiento.getNombre());
				reporteHistoricoPorProgramaEstablecimientoVO
						.setPrograma(programa.getNombre());

				// TODO cambiar el marco
				// if(antecendentesComunaCalculado2006 == null){
				reporteHistoricoPorProgramaEstablecimientoVO
						.setMarco2006((long) programa.getIdProgramaAno()
								* servicio.getId() * establecimiento.getId()
								* 3 * 2006);
				// }else{
				// reporteHistoricoPorProgramaVO.setMarco2006(antecendentesComunaCalculado2006.getPercapitaAno()
				// + antecendentesComunaCalculado2006.getPercapitaMes()*4);
				// }

				// TODO cambiar el marco
				// if(antecendentesComunaCalculado2007 == null){
				reporteHistoricoPorProgramaEstablecimientoVO
						.setMarco2007((long) programa.getIdProgramaAno()
								* servicio.getId() * establecimiento.getId()
								* 3 * 2007);
				// }else{
				// reporteHistoricoPorProgramaVO.setMarco2007(antecendentesComunaCalculado2007.getPercapitaAno()
				// + antecendentesComunaCalculado2007.getPercapitaMes()*3);
				// }

				// TODO cambiar el marco
				// if(antecendentesComunaCalculado2008 == null){
				reporteHistoricoPorProgramaEstablecimientoVO
						.setMarco2008((long) programa.getIdProgramaAno()
								* servicio.getId() * establecimiento.getId()
								* 3 * 2009);
				// }else{
				// reporteHistoricoPorProgramaVO.setMarco2008(antecendentesComunaCalculado2008.getPercapitaAno()
				// + antecendentesComunaCalculado2008.getPercapitaMes()*6);
				// }
				//
				// //TODO cambiar el marco
				// if(antecendentesComunaCalculado2009 == null){
				reporteHistoricoPorProgramaEstablecimientoVO
						.setMarco2009((long) programa.getIdProgramaAno()
								* servicio.getId() * establecimiento.getId()
								* 3 * 2009);
				// }else{
				// reporteHistoricoPorProgramaVO.setMarco2009(antecendentesComunaCalculado2009.getPercapitaAno()
				// + antecendentesComunaCalculado2009.getPercapitaMes()*2);
				// }
				//
				// //TODO cambiar el marco
				// if(antecendentesComunaCalculado2010 == null){
				reporteHistoricoPorProgramaEstablecimientoVO
						.setMarco2010((long) programa.getIdProgramaAno()
								* servicio.getId() * establecimiento.getId()
								* 3 * 2010);
				// }else{
				// reporteHistoricoPorProgramaVO.setMarco2010(antecendentesComunaCalculado2010.getPercapitaAno()
				// + antecendentesComunaCalculado2010.getPercapitaMes()*5);
				// }
				//
				// //TODO cambiar el marco
				// if(antecendentesComunaCalculado2011 == null){
				reporteHistoricoPorProgramaEstablecimientoVO
						.setMarco2011((long) programa.getIdProgramaAno()
								* servicio.getId() * establecimiento.getId()
								* 3 * 2011);

				reporteHistoricoPorProgramaEstablecimientoVO
						.setMarco2012((long) programa.getIdProgramaAno()
								* servicio.getId() * establecimiento.getId()
								* 3 * 2012);

				reporteHistoricoPorProgramaEstablecimientoVO
						.setMarco2013((long) programa.getIdProgramaAno()
								* servicio.getId() * establecimiento.getId()
								* 3 * 2013);

				reporteHistoricoPorProgramaEstablecimientoVO
						.setMarco2014((long) programa.getIdProgramaAno()
								* servicio.getId() * establecimiento.getId()
								* 3 * 2014);
				resultado.add(reporteHistoricoPorProgramaEstablecimientoVO);

			}
		}

		return resultado;

	}

	public List<ReporteHistoricoPorProgramaEstablecimientoVO> getReporteHistoricoEstablecimientoPorProgramaVOFiltroServicio(
			Integer idProgramaAno, Integer idServicio, Subtitulo subtitulo) {
		System.out.println("idProgramaAno --> " + idProgramaAno
				+ "  subtitulo --> " + subtitulo.getNombre());

		List<ReporteHistoricoPorProgramaEstablecimientoVO> resultado = new ArrayList<ReporteHistoricoPorProgramaEstablecimientoVO>();
		ProgramaVO programa = programasService
				.getProgramaAnoPorID(idProgramaAno);
		ServicioSalud servicio = servicioSaludDAO.getById(idServicio);

		System.out.println("servicioVO.getNombre_servicio() --> "
				+ servicio.getNombre());
		// List<Comuna> comunas = servicio.getComunas();

		for (Establecimiento establecimiento : servicio.getEstablecimientos()) {
			ReporteHistoricoPorProgramaEstablecimientoVO reporteHistoricoPorProgramaEstablecimientoVO = new ReporteHistoricoPorProgramaEstablecimientoVO();

			reporteHistoricoPorProgramaEstablecimientoVO.setRegion(servicio
					.getRegion().getNombre());
			reporteHistoricoPorProgramaEstablecimientoVO.setServicio(servicio
					.getNombre());
			reporteHistoricoPorProgramaEstablecimientoVO
					.setEstablecimiento(establecimiento.getNombre());
			reporteHistoricoPorProgramaEstablecimientoVO.setPrograma(programa
					.getNombre());

			// TODO cambiar el marco
			// if(antecendentesComunaCalculado2006 == null){
			reporteHistoricoPorProgramaEstablecimientoVO
					.setMarco2006((long) programa.getIdProgramaAno()
							* servicio.getId() * establecimiento.getId() * 3
							* 2006);
			// }else{
			// reporteHistoricoPorProgramaVO.setMarco2006(antecendentesComunaCalculado2006.getPercapitaAno()
			// + antecendentesComunaCalculado2006.getPercapitaMes()*4);
			// }

			// TODO cambiar el marco
			// if(antecendentesComunaCalculado2007 == null){
			reporteHistoricoPorProgramaEstablecimientoVO
					.setMarco2007((long) programa.getIdProgramaAno()
							* servicio.getId() * establecimiento.getId() * 3
							* 2007);
			// }else{
			// reporteHistoricoPorProgramaVO.setMarco2007(antecendentesComunaCalculado2007.getPercapitaAno()
			// + antecendentesComunaCalculado2007.getPercapitaMes()*3);
			// }

			// TODO cambiar el marco
			// if(antecendentesComunaCalculado2008 == null){
			reporteHistoricoPorProgramaEstablecimientoVO
					.setMarco2008((long) programa.getIdProgramaAno()
							* servicio.getId() * establecimiento.getId() * 3
							* 2009);
			// }else{
			// reporteHistoricoPorProgramaVO.setMarco2008(antecendentesComunaCalculado2008.getPercapitaAno()
			// + antecendentesComunaCalculado2008.getPercapitaMes()*6);
			// }
			//
			// //TODO cambiar el marco
			// if(antecendentesComunaCalculado2009 == null){
			reporteHistoricoPorProgramaEstablecimientoVO
					.setMarco2009((long) programa.getIdProgramaAno()
							* servicio.getId() * establecimiento.getId() * 3
							* 2009);
			// }else{
			// reporteHistoricoPorProgramaVO.setMarco2009(antecendentesComunaCalculado2009.getPercapitaAno()
			// + antecendentesComunaCalculado2009.getPercapitaMes()*2);
			// }
			//
			// //TODO cambiar el marco
			// if(antecendentesComunaCalculado2010 == null){
			reporteHistoricoPorProgramaEstablecimientoVO
					.setMarco2010((long) programa.getIdProgramaAno()
							* servicio.getId() * establecimiento.getId() * 3
							* 2010);
			// }else{
			// reporteHistoricoPorProgramaVO.setMarco2010(antecendentesComunaCalculado2010.getPercapitaAno()
			// + antecendentesComunaCalculado2010.getPercapitaMes()*5);
			// }
			//
			// //TODO cambiar el marco
			// if(antecendentesComunaCalculado2011 == null){
			reporteHistoricoPorProgramaEstablecimientoVO
					.setMarco2011((long) programa.getIdProgramaAno()
							* servicio.getId() * establecimiento.getId() * 3
							* 2011);

			reporteHistoricoPorProgramaEstablecimientoVO
					.setMarco2012((long) programa.getIdProgramaAno()
							* servicio.getId() * establecimiento.getId() * 3
							* 2012);

			reporteHistoricoPorProgramaEstablecimientoVO
					.setMarco2013((long) programa.getIdProgramaAno()
							* servicio.getId() * establecimiento.getId() * 3
							* 2013);

			reporteHistoricoPorProgramaEstablecimientoVO
					.setMarco2014((long) programa.getIdProgramaAno()
							* servicio.getId() * establecimiento.getId() * 3
							* 2014);
			resultado.add(reporteHistoricoPorProgramaEstablecimientoVO);

		}

		return resultado;

	}

	public List<ReporteHistoricoPorProgramaEstablecimientoVO> getReporteHistoricoEstablecimientoPorProgramaVOFiltroServicioEstablecimiento(
			Integer idProgramaAno, Integer idServicio, Integer idEstablecimiento, Subtitulo subtitulo) {
		System.out.println("idProgramaAno --> " + idProgramaAno
				+ "  subtitulo --> " + subtitulo.getNombre());
		
		ProgramaVO programaAnoActual = programasService.getProgramaAno(idProgramaAno); //2014
		
		
		Integer idProgramaAnoActualMenos1 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(), (getAnoCurso() - 1) ); //2013
		Integer idProgramaAnoActualMenos2 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(), (getAnoCurso() - 2) ); //2012
		Integer idProgramaAnoActualMenos3 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(), (getAnoCurso() - 3) ); //2011
		Integer idProgramaAnoActualMenos4 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(), (getAnoCurso() - 4) ); //2010
		Integer idProgramaAnoActualMenos5 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(), (getAnoCurso() - 5) ); //2009
		Integer idProgramaAnoActualMenos6 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(), (getAnoCurso() - 6) ); //2008
		Integer idProgramaAnoActualMenos7 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(), (getAnoCurso() - 7) ); //2007
		Integer idProgramaAnoActualMenos8 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(), (getAnoCurso() - 8) ); //2006
		
		ProgramaVO programaAnoMenos1 = programasService.getProgramaAno(idProgramaAnoActualMenos1); //2013
		ProgramaVO programaAnoMenos2 = programasService.getProgramaAno(idProgramaAnoActualMenos1); //2012
		ProgramaVO programaAnoMenos3 = programasService.getProgramaAno(idProgramaAnoActualMenos1); //2011
		ProgramaVO programaAnoMenos4 = programasService.getProgramaAno(idProgramaAnoActualMenos1); //2010
		ProgramaVO programaAnoMenos5 = programasService.getProgramaAno(idProgramaAnoActualMenos1); //2009
		ProgramaVO programaAnoMenos6 = programasService.getProgramaAno(idProgramaAnoActualMenos1); //2008
		ProgramaVO programaAnoMenos7 = programasService.getProgramaAno(idProgramaAnoActualMenos1); //2007
		ProgramaVO programaAnoMenos8 = programasService.getProgramaAno(idProgramaAnoActualMenos1); //2006
		

		List<ReporteHistoricoPorProgramaEstablecimientoVO> resultado = new ArrayList<ReporteHistoricoPorProgramaEstablecimientoVO>();
		
		
		
		ServicioSalud servicio = servicioSaludDAO.getById(idServicio);

		System.out.println("servicioVO.getNombre_servicio() --> "
				+ servicio.getNombre());
		// List<Comuna> comunas = servicio.getComunas();

		Establecimiento establecimiento = establecimientosDAO
				.getEstablecimientoById(idEstablecimiento);
		List<ComponentesVO> componentes = programasService.getComponenteByProgramaSubtitulos(programaAnoActual.getId(), subtitulo);
		for(ComponentesVO componente : componentes){
			ReporteHistoricoPorProgramaEstablecimientoVO reporteHistoricoPorProgramaEstablecimientoVO = new ReporteHistoricoPorProgramaEstablecimientoVO();

			reporteHistoricoPorProgramaEstablecimientoVO.setRegion(servicio
					.getRegion().getNombre());
			reporteHistoricoPorProgramaEstablecimientoVO.setServicio(servicio
					.getNombre());
			reporteHistoricoPorProgramaEstablecimientoVO
					.setEstablecimiento(establecimiento.getNombre());
			reporteHistoricoPorProgramaEstablecimientoVO.setPrograma(programaAnoActual
					.getNombre());
			
			ProgramaServicioCoreComponente programaServicioCoreComponenteActual = programasDAO.getProgramaServicioCoreComponenteByEstablecimientoProgramaAnoComponenteSubtitulo(servicio.getId(), idProgramaAno, componente.getId(), subtitulo.getId());
			
			
			Long marcoAnoActual = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), programaAnoActual.getIdProgramaAno(), componente.getId(), subtitulo.getId());
			System.out.println("marcoAnoActual --> "+marcoAnoActual);
			
			Long marcoAnoActualMenos1 = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAnoActualMenos1, componente.getId(), subtitulo.getId());
			Long marcoAnoActualMenos2 = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAnoActualMenos2, componente.getId(), subtitulo.getId());
			Long marcoAnoActualMenos3 = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAnoActualMenos3, componente.getId(), subtitulo.getId());
			Long marcoAnoActualMenos4 = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAnoActualMenos4, componente.getId(), subtitulo.getId());
			Long marcoAnoActualMenos5 = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAnoActualMenos5, componente.getId(), subtitulo.getId());
			Long marcoAnoActualMenos6 = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAnoActualMenos6, componente.getId(), subtitulo.getId());
			Long marcoAnoActualMenos7 = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAnoActualMenos7, componente.getId(), subtitulo.getId());
			Long marcoAnoActualMenos8 = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAnoActualMenos8, componente.getId(), subtitulo.getId());
			
			
				System.out.println("programaServicioCoreComponenteActual no es null!!!");
				
				System.out.println("programaServicioCoreComponenteActual.getTarifa() --> "+programaServicioCoreComponenteActual.getTarifa());
				//2014
				reporteHistoricoPorProgramaEstablecimientoVO.setMarco2014(marcoAnoActual);
				
				reporteHistoricoPorProgramaEstablecimientoVO.setMarco2013(marcoAnoActualMenos1);
				
				reporteHistoricoPorProgramaEstablecimientoVO.setMarco2012(marcoAnoActualMenos2);
				
				reporteHistoricoPorProgramaEstablecimientoVO.setMarco2011(marcoAnoActualMenos3);
				
				reporteHistoricoPorProgramaEstablecimientoVO.setMarco2010(marcoAnoActualMenos4);
				
				reporteHistoricoPorProgramaEstablecimientoVO.setMarco2009(marcoAnoActualMenos5);
				
				reporteHistoricoPorProgramaEstablecimientoVO.setMarco2008(marcoAnoActualMenos6);
				
				reporteHistoricoPorProgramaEstablecimientoVO.setMarco2007(marcoAnoActualMenos7);
				
				reporteHistoricoPorProgramaEstablecimientoVO.setMarco2006(marcoAnoActualMenos8);
				
				resultado.add(reporteHistoricoPorProgramaEstablecimientoVO);
				
		
		}

		return resultado;

	}

	public List<ReporteHistoricoPorProgramaComunaVO> getReporteHistoricoPorProgramaVOFiltroServicio(
			Integer idProgramaAno, Integer idServicio, Subtitulo subtitulo) {
		System.out.println("idProgramaAno --> " + idProgramaAno
				+ "  subtitulo --> " + subtitulo.getNombre());

		List<ReporteHistoricoPorProgramaComunaVO> resultado = new ArrayList<ReporteHistoricoPorProgramaComunaVO>();
		ProgramaVO programa = programasService
				.getProgramaAnoPorID(idProgramaAno);
		List<ServicioSalud> serviciosSalud = servicioSaludDAO
				.getServiciosOrderId();
		ServicioSalud servicio = servicioSaludDAO.getById(idServicio);

		for (Comuna comuna : servicio.getComunas()) {
			ReporteHistoricoPorProgramaComunaVO reporteHistoricoPorProgramaComunaVO = new ReporteHistoricoPorProgramaComunaVO();

			reporteHistoricoPorProgramaComunaVO.setRegion(servicio.getRegion()
					.getNombre());
			reporteHistoricoPorProgramaComunaVO.setServicio(servicio
					.getNombre());
			reporteHistoricoPorProgramaComunaVO.setComuna(comuna.getNombre());
			reporteHistoricoPorProgramaComunaVO.setPrograma(programa
					.getNombre());

			AntecendentesComunaCalculado antecendentesComunaCalculado2006 = null;
			AntecendentesComunaCalculado antecendentesComunaCalculado2007 = null;
			AntecendentesComunaCalculado antecendentesComunaCalculado2008 = null;
			AntecendentesComunaCalculado antecendentesComunaCalculado2009 = null;
			AntecendentesComunaCalculado antecendentesComunaCalculado2010 = null;
			AntecendentesComunaCalculado antecendentesComunaCalculado2011 = null;
			AntecendentesComunaCalculado antecendentesComunaCalculado2012 = null;
			AntecendentesComunaCalculado antecendentesComunaCalculado2013 = null;
			AntecendentesComunaCalculado antecendentesComunaCalculado2014 = null;

			// ********************Año 2006 ****************************

			AntecendentesComuna antecendentesComuna2006 = antecedentesComunaDAO
					.findAntecendentesComunaByComunaServicioAno(
							servicio.getNombre(), comuna.getNombre(), 2006);
			if (antecendentesComuna2006 == null) {
				reporteHistoricoPorProgramaComunaVO.setMarco2006(0L);
			} else {
				antecendentesComunaCalculado2006 = antecedentesComunaDAO
						.findByIdAntecedentesComuna(antecendentesComuna2006
								.getIdAntecedentesComuna());
				if (antecendentesComunaCalculado2006 == null) {
					reporteHistoricoPorProgramaComunaVO.setMarco2006(0L);
				}
			}

			// ***************** Fin año 2006 *****************************

			// ********************Año 2007 ****************************

			AntecendentesComuna antecendentesComuna2007 = antecedentesComunaDAO
					.findAntecendentesComunaByComunaServicioAno(
							servicio.getNombre(), comuna.getNombre(), 2007);
			if (antecendentesComuna2007 == null) {
				reporteHistoricoPorProgramaComunaVO.setMarco2007(0L);
			} else {
				antecendentesComunaCalculado2007 = antecedentesComunaDAO
						.findByIdAntecedentesComuna(antecendentesComuna2007
								.getIdAntecedentesComuna());
				if (antecendentesComunaCalculado2007 == null) {
					reporteHistoricoPorProgramaComunaVO.setMarco2007(0L);
				}
			}

			// ***************** Fin año 2007 *****************************

			// ********************Año 2008 ****************************

			AntecendentesComuna antecendentesComuna2008 = antecedentesComunaDAO
					.findAntecendentesComunaByComunaServicioAno(
							servicio.getNombre(), comuna.getNombre(), 2008);
			if (antecendentesComuna2008 == null) {
				reporteHistoricoPorProgramaComunaVO.setMarco2008(0L);
			} else {
				antecendentesComunaCalculado2008 = antecedentesComunaDAO
						.findByIdAntecedentesComuna(antecendentesComuna2008
								.getIdAntecedentesComuna());
				if (antecendentesComunaCalculado2008 == null) {
					reporteHistoricoPorProgramaComunaVO.setMarco2008(0L);
				}
			}

			// ***************** Fin año 2008 *****************************

			// ********************Año 2009 ****************************

			AntecendentesComuna antecendentesComuna2009 = antecedentesComunaDAO
					.findAntecendentesComunaByComunaServicioAno(
							servicio.getNombre(), comuna.getNombre(), 2009);
			if (antecendentesComuna2009 == null) {
				reporteHistoricoPorProgramaComunaVO.setMarco2009(0L);
			} else {
				antecendentesComunaCalculado2009 = antecedentesComunaDAO
						.findByIdAntecedentesComuna(antecendentesComuna2009
								.getIdAntecedentesComuna());
				if (antecendentesComunaCalculado2009 == null) {
					reporteHistoricoPorProgramaComunaVO.setMarco2009(0L);
				}
			}

			// ***************** Fin año 2009 *****************************

			// ********************Año 2010 ****************************

			AntecendentesComuna antecendentesComuna2010 = antecedentesComunaDAO
					.findAntecendentesComunaByComunaServicioAno(
							servicio.getNombre(), comuna.getNombre(), 2010);
			if (antecendentesComuna2010 == null) {
				reporteHistoricoPorProgramaComunaVO.setMarco2010(0L);
			} else {
				antecendentesComunaCalculado2010 = antecedentesComunaDAO
						.findByIdAntecedentesComuna(antecendentesComuna2010
								.getIdAntecedentesComuna());
				if (antecendentesComunaCalculado2010 == null) {
					reporteHistoricoPorProgramaComunaVO.setMarco2010(0L);
				}
			}

			// ***************** Fin año 2010 *****************************

			// ********************Año 2011 ****************************

			AntecendentesComuna antecendentesComuna2011 = antecedentesComunaDAO
					.findAntecendentesComunaByComunaServicioAno(
							servicio.getNombre(), comuna.getNombre(), 2011);
			if (antecendentesComuna2011 == null) {
				reporteHistoricoPorProgramaComunaVO.setMarco2011(0L);
			} else {
				antecendentesComunaCalculado2011 = antecedentesComunaDAO
						.findByIdAntecedentesComuna(antecendentesComuna2011
								.getIdAntecedentesComuna());
				if (antecendentesComunaCalculado2011 == null) {
					reporteHistoricoPorProgramaComunaVO.setMarco2011(0L);
				}
			}

			// ***************** Fin año 2011 *****************************

			// ********************Año 2012 ****************************

			AntecendentesComuna antecendentesComuna2012 = antecedentesComunaDAO
					.findAntecendentesComunaByComunaServicioAno(
							servicio.getNombre(), comuna.getNombre(), 2012);
			if (antecendentesComuna2012 == null) {
				reporteHistoricoPorProgramaComunaVO.setMarco2012(0L);
			} else {
				antecendentesComunaCalculado2012 = antecedentesComunaDAO
						.findByIdAntecedentesComuna(antecendentesComuna2012
								.getIdAntecedentesComuna());
				if (antecendentesComunaCalculado2012 == null) {
					reporteHistoricoPorProgramaComunaVO.setMarco2012(0L);
				}
			}

			// ***************** Fin año 2012 *****************************

			// ********************Año 2013 ****************************

			AntecendentesComuna antecendentesComuna2013 = antecedentesComunaDAO
					.findAntecendentesComunaByComunaServicioAno(
							servicio.getNombre(), comuna.getNombre(), 2013);
			if (antecendentesComuna2013 == null) {
				reporteHistoricoPorProgramaComunaVO.setMarco2013(0L);
			} else {
				antecendentesComunaCalculado2013 = antecedentesComunaDAO
						.findByIdAntecedentesComuna(antecendentesComuna2013
								.getIdAntecedentesComuna());
				if (antecendentesComunaCalculado2013 == null) {
					reporteHistoricoPorProgramaComunaVO.setMarco2013(0L);
				}
			}

			// ***************** Fin año 2013 *****************************

			// ********************Año 2014 ****************************

			AntecendentesComuna antecendentesComuna2014 = antecedentesComunaDAO
					.findAntecendentesComunaByComunaServicioAno(
							servicio.getNombre(), comuna.getNombre(), 2014);
			if (antecendentesComuna2014 == null) {
				reporteHistoricoPorProgramaComunaVO.setMarco2014(0L);
			} else {
				antecendentesComunaCalculado2014 = antecedentesComunaDAO
						.findByIdAntecedentesComuna(antecendentesComuna2014
								.getIdAntecedentesComuna());
				if (antecendentesComunaCalculado2014 == null) {
					reporteHistoricoPorProgramaComunaVO.setMarco2014(0L);
				}
			}

			// ***************** Fin año 2014 *****************************

			// TODO cambiar el marco
			// if(antecendentesComunaCalculado2006 == null){
			reporteHistoricoPorProgramaComunaVO.setMarco2006(0L);
			// }else{
			// reporteHistoricoPorProgramaVO.setMarco2006(antecendentesComunaCalculado2006.getPercapitaAno()
			// + antecendentesComunaCalculado2006.getPercapitaMes()*4);
			// }

			// TODO cambiar el marco
			// if(antecendentesComunaCalculado2007 == null){
			reporteHistoricoPorProgramaComunaVO.setMarco2007(0L);
			// }else{
			// reporteHistoricoPorProgramaVO.setMarco2007(antecendentesComunaCalculado2007.getPercapitaAno()
			// + antecendentesComunaCalculado2007.getPercapitaMes()*3);
			// }

			// TODO cambiar el marco
			// if(antecendentesComunaCalculado2008 == null){
			reporteHistoricoPorProgramaComunaVO.setMarco2008(0L);
			// }else{
			// reporteHistoricoPorProgramaVO.setMarco2008(antecendentesComunaCalculado2008.getPercapitaAno()
			// + antecendentesComunaCalculado2008.getPercapitaMes()*6);
			// }
			//
			// //TODO cambiar el marco
			// if(antecendentesComunaCalculado2009 == null){
			reporteHistoricoPorProgramaComunaVO.setMarco2009(0L);
			// }else{
			// reporteHistoricoPorProgramaVO.setMarco2009(antecendentesComunaCalculado2009.getPercapitaAno()
			// + antecendentesComunaCalculado2009.getPercapitaMes()*2);
			// }
			//
			// //TODO cambiar el marco
			// if(antecendentesComunaCalculado2010 == null){
			reporteHistoricoPorProgramaComunaVO.setMarco2010(0L);
			// }else{
			// reporteHistoricoPorProgramaVO.setMarco2010(antecendentesComunaCalculado2010.getPercapitaAno()
			// + antecendentesComunaCalculado2010.getPercapitaMes()*5);
			// }
			//
			// //TODO cambiar el marco
			// if(antecendentesComunaCalculado2011 == null){
			reporteHistoricoPorProgramaComunaVO.setMarco2011(0L);
			// }else{
			// reporteHistoricoPorProgramaVO.setMarco2011(antecendentesComunaCalculado2011.getPercapitaAno()
			// + antecendentesComunaCalculado2011.getPercapitaMes()*8);
			// }
			// TODO cambiar el marco
			if (antecendentesComunaCalculado2012 == null
					|| antecendentesComunaCalculado2012.getPercapitaMes() == null) {
				reporteHistoricoPorProgramaComunaVO.setMarco2012(0L);
			} else {
				reporteHistoricoPorProgramaComunaVO
						.setMarco2012(antecendentesComunaCalculado2012
								.getPercapitaAno()
								+ antecendentesComunaCalculado2012
										.getPercapitaMes() * 3);
			}

			// TODO cambiar el marco
			if (antecendentesComunaCalculado2013 == null
					|| antecendentesComunaCalculado2013.getPercapitaMes() == null) {
				reporteHistoricoPorProgramaComunaVO.setMarco2013(0L);
			} else {
				reporteHistoricoPorProgramaComunaVO
						.setMarco2013(antecendentesComunaCalculado2013
								.getPercapitaAno()
								+ antecendentesComunaCalculado2013
										.getPercapitaMes() * 4);
			}

			// TODO cambiar el marco
			if (antecendentesComunaCalculado2014 == null
					|| antecendentesComunaCalculado2014.getPercapitaMes() == null) {
				continue;
			} else {
				System.out
						.println("antecendentesComunaCalculado2014.getPercapitaAno() --> "
								+ antecendentesComunaCalculado2014
										.getPercapitaAno());
				System.out
						.println("antecendentesComunaCalculado2014.getPercapitaMes() --> "
								+ antecendentesComunaCalculado2014
										.getPercapitaMes());

				reporteHistoricoPorProgramaComunaVO
						.setMarco2014(antecendentesComunaCalculado2014
								.getPercapitaAno()
								+ antecendentesComunaCalculado2014
										.getPercapitaMes() * 4);
			}

			resultado.add(reporteHistoricoPorProgramaComunaVO);

		}

		return resultado;

	}

	public List<ReporteHistoricoPorProgramaComunaVO> getReporteHistoricoPorProgramaVOFiltroServicioComuna(
			Integer idProgramaAno, Integer idServicio, Integer idComuna,
			Subtitulo subtitulo) {
		System.out.println("idProgramaAno --> " + idProgramaAno
				+ "  subtitulo --> " + subtitulo.getNombre());

		List<ReporteHistoricoPorProgramaComunaVO> resultado = new ArrayList<ReporteHistoricoPorProgramaComunaVO>();
		
		
		ProgramaVO programaAnoActual = programasService.getProgramaAno(idProgramaAno); //2014
		
		
		Integer idProgramaAnoActualMenos1 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(), (getAnoCurso() - 1) ); //2013
		Integer idProgramaAnoActualMenos2 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(), (getAnoCurso() - 2) ); //2012
		Integer idProgramaAnoActualMenos3 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(), (getAnoCurso() - 3) ); //2011
		Integer idProgramaAnoActualMenos4 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(), (getAnoCurso() - 4) ); //2010
		Integer idProgramaAnoActualMenos5 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(), (getAnoCurso() - 5) ); //2009
		Integer idProgramaAnoActualMenos6 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(), (getAnoCurso() - 6) ); //2008
		Integer idProgramaAnoActualMenos7 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(), (getAnoCurso() - 7) ); //2007
		Integer idProgramaAnoActualMenos8 = programasService.getProgramaAnoSiguiente(programaAnoActual.getId(), (getAnoCurso() - 8) ); //2006
		
		ProgramaVO programaAnoMenos1 = programasService.getProgramaAno(idProgramaAnoActualMenos1); //2013
		ProgramaVO programaAnoMenos2 = programasService.getProgramaAno(idProgramaAnoActualMenos1); //2012
		ProgramaVO programaAnoMenos3 = programasService.getProgramaAno(idProgramaAnoActualMenos1); //2011
		ProgramaVO programaAnoMenos4 = programasService.getProgramaAno(idProgramaAnoActualMenos1); //2010
		ProgramaVO programaAnoMenos5 = programasService.getProgramaAno(idProgramaAnoActualMenos1); //2009
		ProgramaVO programaAnoMenos6 = programasService.getProgramaAno(idProgramaAnoActualMenos1); //2008
		ProgramaVO programaAnoMenos7 = programasService.getProgramaAno(idProgramaAnoActualMenos1); //2007
		ProgramaVO programaAnoMenos8 = programasService.getProgramaAno(idProgramaAnoActualMenos1); //2006
		
		
		
		List<ServicioSalud> serviciosSalud = servicioSaludDAO
				.getServiciosOrderId();
		ServicioSalud servicio = servicioSaludDAO.getById(idServicio);
		Comuna comuna = comunaDAO.getComunaById(idComuna);
		List<ComponentesVO> componentes = programasService.getComponenteByProgramaSubtitulos(programaAnoActual.getId(), subtitulo);
		for(ComponentesVO componente : componentes){
			ReporteHistoricoPorProgramaComunaVO reporteHistoricoPorProgramaComunaVO = new ReporteHistoricoPorProgramaComunaVO();

			reporteHistoricoPorProgramaComunaVO.setRegion(servicio.getRegion()
					.getNombre());
			reporteHistoricoPorProgramaComunaVO.setServicio(servicio.getNombre());
			reporteHistoricoPorProgramaComunaVO.setComuna(comuna.getNombre());
			reporteHistoricoPorProgramaComunaVO.setPrograma(programaAnoActual.getNombre());


			
			// ********************Año 2006 ****************************
			Long percapitaMenos8 = 0L;
			Long desempenoDificilMenos8 = 0L;
			Long marcoAnoMenos8 = 0L;
			
			AntecendentesComunaCalculado antecendentesComunaCalculadoMenos8 = antecedentesComunaDAO.findByComunaAnoVigencia(comuna.getId(), (getAnoCurso()-8));
			if(antecendentesComunaCalculadoMenos8 == null){
				reporteHistoricoPorProgramaComunaVO.setMarco2006(0L);
			}
			else{
				percapitaMenos8 = antecendentesComunaCalculadoMenos8.getPercapitaAno();
				desempenoDificilMenos8 = (long)antecendentesComunaCalculadoMenos8.getDesempenoDificil();
			}
			
			marcoAnoMenos8 = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAnoActualMenos8, componente.getId(), subtitulo.getId());
			reporteHistoricoPorProgramaComunaVO.setMarco2006(percapitaMenos8 + desempenoDificilMenos8 + marcoAnoMenos8);
			

			// ***************** Fin año 2006 *****************************
			
			
			// ********************Año 2007 ****************************
			Long percapitaMenos7 = 0L;
			Long desempenoDificilMenos7 = 0L;
			Long marcoAnoMenos7 = 0L;
				
			AntecendentesComunaCalculado antecendentesComunaCalculadoMenos7 = antecedentesComunaDAO.findByComunaAnoVigencia(comuna.getId(), (getAnoCurso()-7));
			if(antecendentesComunaCalculadoMenos7 == null){
				reporteHistoricoPorProgramaComunaVO.setMarco2007(0L);
			}
			else{
				percapitaMenos7 = antecendentesComunaCalculadoMenos7.getPercapitaAno();
				desempenoDificilMenos7 = (long)antecendentesComunaCalculadoMenos7.getDesempenoDificil();
			}
			
			marcoAnoMenos7 = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAnoActualMenos7, componente.getId(), subtitulo.getId());
			reporteHistoricoPorProgramaComunaVO.setMarco2007(percapitaMenos7 + desempenoDificilMenos7 + marcoAnoMenos7);

			// ***************** Fin año 2007 *****************************

			
			// ********************Año 2008 ****************************
			Long percapitaMenos6 = 0L;
			Long desempenoDificilMenos6 = 0L;
			Long marcoAnoMenos6 = 0L;
							
			AntecendentesComunaCalculado antecendentesComunaCalculadoMenos6 = antecedentesComunaDAO.findByComunaAnoVigencia(comuna.getId(), (getAnoCurso()-6));
			if(antecendentesComunaCalculadoMenos6 == null){
				reporteHistoricoPorProgramaComunaVO.setMarco2008(0L);
			}
			else{
				percapitaMenos6 = antecendentesComunaCalculadoMenos6.getPercapitaAno();
				desempenoDificilMenos6 = (long)antecendentesComunaCalculadoMenos6.getDesempenoDificil();
			}
						
			marcoAnoMenos6 = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAnoActualMenos6, componente.getId(), subtitulo.getId());
			reporteHistoricoPorProgramaComunaVO.setMarco2008(percapitaMenos6 + desempenoDificilMenos6 + marcoAnoMenos6);
			

			// ***************** Fin año 2008 *****************************
			
			
			// ********************Año 2009 ****************************
			Long percapitaMenos5 = 0L;
			Long desempenoDificilMenos5 = 0L;
			Long marcoAnoMenos5 = 0L;
										
			AntecendentesComunaCalculado antecendentesComunaCalculadoMenos5 = antecedentesComunaDAO.findByComunaAnoVigencia(comuna.getId(), (getAnoCurso()-5));
			if(antecendentesComunaCalculadoMenos5 == null){
				reporteHistoricoPorProgramaComunaVO.setMarco2009(0L);
			}
			else{
				percapitaMenos5 = antecendentesComunaCalculadoMenos5.getPercapitaAno();
				desempenoDificilMenos5 = (long)antecendentesComunaCalculadoMenos5.getDesempenoDificil();
			}
									
			marcoAnoMenos5 = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAnoActualMenos5, componente.getId(), subtitulo.getId());
			reporteHistoricoPorProgramaComunaVO.setMarco2009(percapitaMenos5 + desempenoDificilMenos5 + marcoAnoMenos5);
						

			// ***************** Fin año 2009 *****************************
			
			
			// ********************Año 2010 ****************************
			Long percapitaMenos4 = 0L;
			Long desempenoDificilMenos4 = 0L;
			Long marcoAnoMenos4 = 0L;
										
			AntecendentesComunaCalculado antecendentesComunaCalculadoMenos4 = antecedentesComunaDAO.findByComunaAnoVigencia(comuna.getId(), (getAnoCurso()-4));
			if(antecendentesComunaCalculadoMenos4 == null){
				reporteHistoricoPorProgramaComunaVO.setMarco2010(0L);
			}
			else{
				percapitaMenos4 = antecendentesComunaCalculadoMenos4.getPercapitaAno();
				desempenoDificilMenos4 = (long)antecendentesComunaCalculadoMenos4.getDesempenoDificil();
			}
									
			marcoAnoMenos4 = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAnoActualMenos4, componente.getId(), subtitulo.getId());
			reporteHistoricoPorProgramaComunaVO.setMarco2010(percapitaMenos4 + desempenoDificilMenos4 + marcoAnoMenos4);
						

			// ***************** Fin año 2010 *****************************
			
			
			// ********************Año 2011 ****************************
			Long percapitaMenos3 = 0L;
			Long desempenoDificilMenos3 = 0L;
			Long marcoAnoMenos3 = 0L;
										
			AntecendentesComunaCalculado antecendentesComunaCalculadoMenos3 = antecedentesComunaDAO.findByComunaAnoVigencia(comuna.getId(), (getAnoCurso()-3));
			if(antecendentesComunaCalculadoMenos3 == null){
				reporteHistoricoPorProgramaComunaVO.setMarco2011(0L);
			}
			else{
				percapitaMenos3 = antecendentesComunaCalculadoMenos3.getPercapitaAno();
				desempenoDificilMenos3 = (long)antecendentesComunaCalculadoMenos3.getDesempenoDificil();
			}
									
			marcoAnoMenos3 = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAnoActualMenos3, componente.getId(), subtitulo.getId());
			reporteHistoricoPorProgramaComunaVO.setMarco2011(percapitaMenos3 + desempenoDificilMenos3 + marcoAnoMenos3);
						

			// ***************** Fin año 2011 *****************************
			
			
			// ********************Año 2012 ****************************
			Long percapitaMenos2 = 0L;
			Long desempenoDificilMenos2 = 0L;
			Long marcoAnoMenos2 = 0L;
										
			AntecendentesComunaCalculado antecendentesComunaCalculadoMenos2 = antecedentesComunaDAO.findByComunaAnoVigencia(comuna.getId(), (getAnoCurso()-2));
			if(antecendentesComunaCalculadoMenos2 == null){
				reporteHistoricoPorProgramaComunaVO.setMarco2012(0L);
			}
			else{
				percapitaMenos2 = antecendentesComunaCalculadoMenos2.getPercapitaAno();
				desempenoDificilMenos2 = (long)antecendentesComunaCalculadoMenos2.getDesempenoDificil();
			}
									
			marcoAnoMenos2 = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAnoActualMenos2, componente.getId(), subtitulo.getId());
			reporteHistoricoPorProgramaComunaVO.setMarco2012(percapitaMenos2 + desempenoDificilMenos2 + marcoAnoMenos2);
						

			// ***************** Fin año 2012 *****************************
			
			
			// ********************Año 2013 ****************************
			Long percapitaMenos1 = 0L;
			Long desempenoDificilMenos1 = 0L;
			Long marcoAnoMenos1 = 0L;
										
			AntecendentesComunaCalculado antecendentesComunaCalculadoMenos1 = antecedentesComunaDAO.findByComunaAnoVigencia(comuna.getId(), (getAnoCurso()-1));
			if(antecendentesComunaCalculadoMenos1 == null){
				reporteHistoricoPorProgramaComunaVO.setMarco2013(0L);
			}
			else{
				percapitaMenos1 = antecendentesComunaCalculadoMenos1.getPercapitaAno();
				desempenoDificilMenos1 = (long)antecendentesComunaCalculadoMenos1.getDesempenoDificil();
			}
									
			marcoAnoMenos1 = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAnoActualMenos1, componente.getId(), subtitulo.getId());
			reporteHistoricoPorProgramaComunaVO.setMarco2013(percapitaMenos1 + desempenoDificilMenos1 + marcoAnoMenos1);
						

			// ***************** Fin año 2013 *****************************
			
			
			// ********************Año 2014 ****************************
			Long percapitaActual = 0L;
			Long desempenoDificilActual = 0L;
			Long marcoAnoActual = 0L;
										
			AntecendentesComunaCalculado antecendentesComunaCalculadoActual = antecedentesComunaDAO.findByComunaAnoVigencia(comuna.getId(), getAnoCurso());
			if(antecendentesComunaCalculadoActual == null){
				reporteHistoricoPorProgramaComunaVO.setMarco2014(0L);
			}
			else{
				percapitaActual = antecendentesComunaCalculadoActual.getPercapitaAno();
				desempenoDificilActual = (long)antecendentesComunaCalculadoActual.getDesempenoDificil();
			}
									
			marcoAnoActual = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAno, componente.getId(), subtitulo.getId());
			reporteHistoricoPorProgramaComunaVO.setMarco2014(percapitaActual + desempenoDificilActual + marcoAnoActual);
						

			// ***************** Fin año 2014 *****************************

			resultado.add(reporteHistoricoPorProgramaComunaVO);
		}

		

		return resultado;

	}

	public Integer generarPlanillaReporteHistoricoComuna() {
		Integer planillaTrabajoId = null;
		List<CellExcelVO> header = new ArrayList<CellExcelVO>();
		List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();

		header.add((new CellExcelVO("REGION", 1, 1)));
		header.add((new CellExcelVO("SERVICIO SALUD", 1, 1)));
		header.add((new CellExcelVO("COMUNA", 1, 1)));
		header.add((new CellExcelVO("PROGRAMA", 1, 1)));
		header.add((new CellExcelVO("MARCO 2006", 1, 1)));
		header.add((new CellExcelVO("MARCO 2007", 1, 1)));
		header.add((new CellExcelVO("MARCO 2008", 1, 1)));
		header.add((new CellExcelVO("MARCO 2009", 1, 1)));
		header.add((new CellExcelVO("MARCO 2010", 1, 1)));
		header.add((new CellExcelVO("MARCO 2011", 1, 1)));
		header.add((new CellExcelVO("MARCO 2012", 1, 1)));
		header.add((new CellExcelVO("MARCO 2013", 1, 1)));
		header.add((new CellExcelVO("MARCO 2014", 1, 1)));

		List<ReporteHistoricoPorProgramaComunaVO> reporteHistoricoPorProgramaComunaVOsub24 = this
				.getReporteHistoricoComunaAll(Subtitulo.SUBTITULO24);

		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator
				+ "Planilla Reporte Historico Programa - Comuna.xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());

		GeneradorExcel generadorExcel = new GeneradorExcel(filename);
		ReporteHistoricoProgramaPorComunaSheetExcel reporteHistoricoProgramaPorComunaSheetExcelSub24 = new ReporteHistoricoProgramaPorComunaSheetExcel(
				header, subHeader, null);
		reporteHistoricoProgramaPorComunaSheetExcelSub24
				.setItems(reporteHistoricoPorProgramaComunaVOsub24);

		generadorExcel.addSheet(
				reporteHistoricoProgramaPorComunaSheetExcelSub24,
				"Subtitulo 24");

		try {
			BodyVO response = alfrescoService.uploadDocument(
					generadorExcel.saveExcel(), contenType,
					folderReportes.replace("{ANO}", getAnoCurso().toString()));
			// System.out.println("response planillaPropuestaEstimacionFlujoCajaConsolidador --->"
			// + response);

			TipoDocumento tipoDocumento = new TipoDocumento(
					TipoDocumentosProcesos.REPORTEHISTORICOPROGRAMACOMUNA
							.getId());
			planillaTrabajoId = documentService.createDocumentReportes(
					tipoDocumento, response.getNodeRef(),
					response.getFileName(), contenType, getAnoCurso(),
					Integer.parseInt(getMesCurso(true)));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return planillaTrabajoId;
	}

	public Integer generarPlanillaReporteHistoricoEstablecimiento() {
		Integer planillaTrabajoId = null;
		List<CellExcelVO> header = new ArrayList<CellExcelVO>();
		List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();

		header.add((new CellExcelVO("REGION", 1, 1)));
		header.add((new CellExcelVO("SERVICIO SALUD", 1, 1)));
		header.add((new CellExcelVO("ESTABLECIMIENTO", 1, 1)));
		header.add((new CellExcelVO("PROGRAMA", 1, 1)));
		header.add((new CellExcelVO("MARCO 2006", 1, 1)));
		header.add((new CellExcelVO("MARCO 2007", 1, 1)));
		header.add((new CellExcelVO("MARCO 2008", 1, 1)));
		header.add((new CellExcelVO("MARCO 2009", 1, 1)));
		header.add((new CellExcelVO("MARCO 2010", 1, 1)));
		header.add((new CellExcelVO("MARCO 2011", 1, 1)));
		header.add((new CellExcelVO("MARCO 2012", 1, 1)));
		header.add((new CellExcelVO("MARCO 2013", 1, 1)));
		header.add((new CellExcelVO("MARCO 2014", 1, 1)));

		List<ReporteHistoricoPorProgramaEstablecimientoVO> reporteHistoricoPorProgramaEstablecimientoVOSub21 = this
				.getReporteHistoricoEstablecimientoAll(Subtitulo.SUBTITULO21);
		List<ReporteHistoricoPorProgramaEstablecimientoVO> reporteHistoricoPorProgramaEstablecimientoVOSub22 = this
				.getReporteHistoricoEstablecimientoAll(Subtitulo.SUBTITULO22);
		List<ReporteHistoricoPorProgramaEstablecimientoVO> reporteHistoricoPorProgramaEstablecimientoVOSub29 = this
				.getReporteHistoricoEstablecimientoAll(Subtitulo.SUBTITULO29);

		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator
				+ "Planilla Reporte Historico Programa - Servicio.xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());

		GeneradorExcel generadorExcel = new GeneradorExcel(filename);

		ReporteHistoricoProgramaPorEstablecimientoSheetExcel reporteHistoricoProgramaPorEstablecimientoSheetExcelSub21 = new ReporteHistoricoProgramaPorEstablecimientoSheetExcel(
				header, subHeader, null);
		ReporteHistoricoProgramaPorEstablecimientoSheetExcel reporteHistoricoProgramaPorEstablecimientoSheetExcelSub22 = new ReporteHistoricoProgramaPorEstablecimientoSheetExcel(
				header, subHeader, null);
		ReporteHistoricoProgramaPorEstablecimientoSheetExcel reporteHistoricoProgramaPorEstablecimientoSheetExcelSub29 = new ReporteHistoricoProgramaPorEstablecimientoSheetExcel(
				header, subHeader, null);

		reporteHistoricoProgramaPorEstablecimientoSheetExcelSub21
				.setItems(reporteHistoricoPorProgramaEstablecimientoVOSub21);
		reporteHistoricoProgramaPorEstablecimientoSheetExcelSub22
				.setItems(reporteHistoricoPorProgramaEstablecimientoVOSub22);
		reporteHistoricoProgramaPorEstablecimientoSheetExcelSub29
				.setItems(reporteHistoricoPorProgramaEstablecimientoVOSub29);

		generadorExcel.addSheet(
				reporteHistoricoProgramaPorEstablecimientoSheetExcelSub21,
				"Subtitulo 21");
		generadorExcel.addSheet(
				reporteHistoricoProgramaPorEstablecimientoSheetExcelSub22,
				"Subtitulo 22");
		generadorExcel.addSheet(
				reporteHistoricoProgramaPorEstablecimientoSheetExcelSub29,
				"Subtitulo 29");

		try {
			BodyVO response = alfrescoService.uploadDocument(
					generadorExcel.saveExcel(), contenType,
					folderReportes.replace("{ANO}", getAnoCurso().toString()));
			// System.out.println("response planillaPropuestaEstimacionFlujoCajaConsolidador --->"
			// + response);

			TipoDocumento tipoDocumento = new TipoDocumento(
					TipoDocumentosProcesos.REPORTEHISTORICOPROGRAMAESTABLECIMIENTO
							.getId());
			planillaTrabajoId = documentService.createDocumentReportes(
					tipoDocumento, response.getNodeRef(),
					response.getFileName(), contenType, getAnoCurso(),
					Integer.parseInt(getMesCurso(true)));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return planillaTrabajoId;
	}

	public Integer getDocumentByTypeAnoActual(
			TipoDocumentosProcesos tipoDocumentoProceso) {
		System.out.println("tipoDocumentoProceso --> "
				+ tipoDocumentoProceso.getName());
		System.out.println("getAnoCurso() --> " + getAnoCurso());
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO = documentService
				.getDocumentByTypeAnoReportes(tipoDocumentoProceso,
						getAnoCurso());
		if (referenciaDocumentoSummaryVO == null) {
			return null;
		} else {
			return referenciaDocumentoSummaryVO.getId();
		}
	}

	public List<ReporteRebajaVO> getReporteRebaja() {
		List<ReporteRebajaVO> resultado = new ArrayList<ReporteRebajaVO>();
		List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
		for (ServicioSalud servicioSalud : servicios) {

			List<Comuna> comunas = servicioSalud.getComunas();
			for (Comuna comuna : comunas) {
				ReporteRebajaVO reporteRebajaVO = new ReporteRebajaVO();
				reporteRebajaVO
						.setRegion(servicioSalud.getRegion().getNombre());
				reporteRebajaVO.setServicio(servicioSalud.getNombre());
				
				reporteRebajaVO.setComuna(comuna.getNombre());

				AntecendentesComunaCalculado antecendentesComunaCalculado = antecedentesComunaDAO
						.findByComunaAnoVigencia(comuna.getId(), getAnoCurso());
				
				if(antecendentesComunaCalculado == null){
					continue;
				}

				Integer porcentajeRebajaCorte1 = 0;
				Integer porcentajeRebajaCorte2 = 0;
				Integer porcentajeRebajaCorte3 = 0;
				Integer porcentajeRebajaCorte4 = 0;
				
				Integer montoRebajaCorte1 = 0;
				Integer montoRebajaCorte2 = 0;
				Integer montoRebajaCorte3 = 0;
				Integer montoRebajaCorte4 = 0;
				
				
				
				
				Set<AntecedentesComunaCalculadoRebaja> setAntecedentesComunaCalculadoRebaja = antecendentesComunaCalculado.getAntecedentesComunaCalculadoRebajas();
				if(setAntecedentesComunaCalculadoRebaja == null){
					continue;
				}
				else{
					List<AntecedentesComunaCalculadoRebaja> antecedentesComunaCalculadoRebajas = new ArrayList<AntecedentesComunaCalculadoRebaja>(setAntecedentesComunaCalculadoRebaja);
					for(AntecedentesComunaCalculadoRebaja antecedentesComunaCalculadoRebaja : antecedentesComunaCalculadoRebajas){
						
						
						
						
						if(antecedentesComunaCalculadoRebaja.getRebaja().getRebajaCorte().getRebajaCorteId() == 1){
							System.out.println("corte1");
							montoRebajaCorte1 = antecedentesComunaCalculadoRebaja.getMontoRebaja();
							
							List<CumplimientoRebajaVO> cumplimientoRebajasVO = rebajaService.getCumplimientoByRebajaComuna(antecedentesComunaCalculadoRebaja.getRebaja().getIdRebaja(), antecedentesComunaCalculadoRebaja.getAntecedentesComunaCalculado().getAntecedentesComuna().getIdComuna().getId());
							if(cumplimientoRebajasVO != null && cumplimientoRebajasVO.size() > 0){
								Integer porcentajeRebajaFinal1 = 0;
								for(CumplimientoRebajaVO cumplimientoRebajaVO : cumplimientoRebajasVO){
									System.out.println("cumplimientoRebajaVO.getRebajaFinal() --> "+cumplimientoRebajaVO.getRebajaFinal());
									porcentajeRebajaFinal1 += (( cumplimientoRebajaVO.getRebajaFinal() == null) ? 0 : cumplimientoRebajaVO.getRebajaFinal().intValue());
								}
								System.out.println("porcentajeRebajaFinal-->"+porcentajeRebajaFinal1);
								porcentajeRebajaCorte1 = porcentajeRebajaFinal1;
							}
							
						}
						else if(antecedentesComunaCalculadoRebaja.getRebaja().getRebajaCorte().getRebajaCorteId() == 2){
							System.out.println("corte2");
							montoRebajaCorte2 = antecedentesComunaCalculadoRebaja.getMontoRebaja();
							
							System.out.println("antecedentesComunaCalculadoRebaja.getRebaja().getIdRebaja() --> "+antecedentesComunaCalculadoRebaja.getRebaja().getIdRebaja());
							System.out.println("antecedentesComunaCalculadoRebaja.getAntecedentesComunaCalculado().getAntecedentesComuna().getIdComuna().getId() --> "+antecedentesComunaCalculadoRebaja.getAntecedentesComunaCalculado().getAntecedentesComuna().getIdComuna().getId());
							
							
							List<CumplimientoRebajaVO> cumplimientoRebajasVO = rebajaService.getCumplimientoByRebajaComuna(antecedentesComunaCalculadoRebaja.getRebaja().getIdRebaja(), antecedentesComunaCalculadoRebaja.getAntecedentesComunaCalculado().getAntecedentesComuna().getIdComuna().getId());
							if(cumplimientoRebajasVO != null && cumplimientoRebajasVO.size() > 0){
								Integer porcentajeRebajaFinal2 = 0;
								for(CumplimientoRebajaVO cumplimientoRebajaVO : cumplimientoRebajasVO){
									System.out.println("cumplimientoRebajaVO.getRebajaFinal() --> "+cumplimientoRebajaVO.getRebajaFinal());
									porcentajeRebajaFinal2 += (( cumplimientoRebajaVO.getRebajaFinal() == null) ? 0 : cumplimientoRebajaVO.getRebajaFinal().intValue());
									System.out.println("CORTE 2 --> porcentajeRebajaFinal-->"+porcentajeRebajaFinal2);
								}
								
								porcentajeRebajaCorte2 = porcentajeRebajaFinal2;
							}
							
						}
						else if(antecedentesComunaCalculadoRebaja.getRebaja().getRebajaCorte().getRebajaCorteId() == 3){
							System.out.println("corte3");
							montoRebajaCorte3 = antecedentesComunaCalculadoRebaja.getMontoRebaja();
							
							List<CumplimientoRebajaVO> cumplimientoRebajasVO = rebajaService.getCumplimientoByRebajaComuna(antecedentesComunaCalculadoRebaja.getRebaja().getIdRebaja(), antecedentesComunaCalculadoRebaja.getAntecedentesComunaCalculado().getAntecedentesComuna().getIdComuna().getId());
							if(cumplimientoRebajasVO != null && cumplimientoRebajasVO.size() > 0){
								Integer porcentajeRebajaFinal3 = 0;
								for(CumplimientoRebajaVO cumplimientoRebajaVO : cumplimientoRebajasVO){
									System.out.println("cumplimientoRebajaVO.getRebajaFinal() --> "+cumplimientoRebajaVO.getRebajaFinal());
									porcentajeRebajaFinal3 += (( cumplimientoRebajaVO.getRebajaFinal() == null) ? 0 : cumplimientoRebajaVO.getRebajaFinal().intValue());
									System.out.println("CORTE 3 --> porcentajeRebajaFinal-->"+porcentajeRebajaFinal3);
								}
								porcentajeRebajaCorte3 = porcentajeRebajaFinal3;
							}
							
						}
						else if(antecedentesComunaCalculadoRebaja.getRebaja().getRebajaCorte().getRebajaCorteId() == 4){
							System.out.println("corte4");
							montoRebajaCorte4 = antecedentesComunaCalculadoRebaja.getMontoRebaja();
							
							List<CumplimientoRebajaVO> cumplimientoRebajasVO = rebajaService.getCumplimientoByRebajaComuna(antecedentesComunaCalculadoRebaja.getRebaja().getIdRebaja(), antecedentesComunaCalculadoRebaja.getAntecedentesComunaCalculado().getAntecedentesComuna().getIdComuna().getId());
							if(cumplimientoRebajasVO != null && cumplimientoRebajasVO.size() > 0){
								Integer porcentajeRebajaFinal4 = 0;
								for(CumplimientoRebajaVO cumplimientoRebajaVO : cumplimientoRebajasVO){
									System.out.println("cumplimientoRebajaVO.getRebajaFinal() --> "+cumplimientoRebajaVO.getRebajaFinal());
									porcentajeRebajaFinal4 += (( cumplimientoRebajaVO.getRebajaFinal() == null) ? 0 : cumplimientoRebajaVO.getRebajaFinal().intValue());
								}
								System.out.println("porcentajeRebajaFinal-->"+porcentajeRebajaFinal4);
								porcentajeRebajaCorte4 = porcentajeRebajaFinal4;
							}
							
						}
					}
					
					
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
		return resultado;
	}

	public Integer generarPlanillaReporteRebaja(String usuario) {
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
		header.add((new CellExcelVO(StringUtil.caracterUnoMayuscula(fechaMesCorte1.getNombre()), 2, 1)));
		header.add((new CellExcelVO("REBAJA APLICADA", 1, 2)));

		subHeader.add((new CellExcelVO("$ MONTO", 1, 1)));
		subHeader.add((new CellExcelVO("%", 1, 1)));
		subHeader.add((new CellExcelVO("$ MONTO", 1, 1)));
		subHeader.add((new CellExcelVO("%", 1, 1)));
		subHeader.add((new CellExcelVO("$ MONTO", 1, 1)));
		subHeader.add((new CellExcelVO("%", 1, 1)));
		subHeader.add((new CellExcelVO("$ MONTO", 1, 1)));
		subHeader.add((new CellExcelVO("%", 1, 1)));

		List<ReporteRebajaVO> reporteRebajaVO = this.getReporteRebaja();

		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator
				+ "Panilla Reporte Rebaja.xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());

		GeneradorExcel generadorExcel = new GeneradorExcel(filename);

		ReporteRebajaSheetExcel reporteRebajaSheetExcel = new ReporteRebajaSheetExcel(
				header, subHeader, null);
		reporteRebajaSheetExcel.setItems(reporteRebajaVO);

		generadorExcel.addSheet(reporteRebajaSheetExcel, "rebaja");

		try {
			BodyVO response = alfrescoService.uploadDocument(
					generadorExcel.saveExcel(), contenType,
					folderReportes.replace("{ANO}", getAnoCurso().toString()));
			// System.out.println("response planillaPropuestaEstimacionFlujoCajaConsolidador --->"
			// + response);

			TipoDocumento tipoDocumento = new TipoDocumento(
					TipoDocumentosProcesos.REPORTEREBAJA.getId());
			planillaTrabajoId = documentService.createDocumentReportes(
					tipoDocumento, response.getNodeRef(),
					response.getFileName(), contenType, getAnoCurso(),
					Integer.parseInt(getMesCurso(true)));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return planillaTrabajoId;
	}

	public Integer generarPlanillaReporteMonitoreoProgramaPorComuna() {
		Integer planillaTrabajoId = null;
		List<CellExcelVO> header = new ArrayList<CellExcelVO>();
		List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
		header.add((new CellExcelVO("SERVICIO", 1, 2)));
		header.add((new CellExcelVO("PROGRAMA", 1, 2)));
		header.add((new CellExcelVO("COMUNA", 1, 2)));
		header.add((new CellExcelVO("MARCO PRESUPUESTARIO", 1, 2)));

		header.add((new CellExcelVO("REMESA ACUMULADA", 1, 1)));
		header.add((new CellExcelVO("CONVENIO", 1, 1)));

		header.add((new CellExcelVO("CONVENIO PENDIENTE", 1, 2)));

		subHeader.add((new CellExcelVO("MONTO", 1, 1)));
		subHeader.add((new CellExcelVO("%", 1, 1)));
		subHeader.add((new CellExcelVO("MONTO", 1, 1)));
		subHeader.add((new CellExcelVO("%", 1, 1)));

		List<ReporteMonitoreoProgramaPorComunaVO> reporteMonitoreoProgramaPorComunaVO = getReporteMonitoreoPorComunaAll(Subtitulo.SUBTITULO24);

		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator
				+ "Panilla Reporte Monitoreo Programa - Comuna.xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());

		GeneradorExcel generadorExcel = new GeneradorExcel(filename);

		ReporteMonitoreoProgramaComunaSheetExcel reporteMonitoreoProgramaComunaSheetExcel = new ReporteMonitoreoProgramaComunaSheetExcel(
				header, subHeader, null);

		reporteMonitoreoProgramaComunaSheetExcel
				.setItems(reporteMonitoreoProgramaPorComunaVO);

		generadorExcel.addSheet(reporteMonitoreoProgramaComunaSheetExcel,
				"Por Comuna");

		try {
			BodyVO response = alfrescoService.uploadDocument(
					generadorExcel.saveExcel(), contenType,
					folderReportes.replace("{ANO}", getAnoCurso().toString()));

			TipoDocumento tipoDocumento = new TipoDocumento(
					TipoDocumentosProcesos.REPORTEMONITOREOPROGRAMACOMUNA
							.getId());
			planillaTrabajoId = documentService.createDocumentReportes(
					tipoDocumento, response.getNodeRef(),
					response.getFileName(), contenType, getAnoCurso(),
					Integer.parseInt(getMesCurso(true)));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return planillaTrabajoId;
	}

	public Integer generarPlanillaReporteMonitoreoProgramaPorServicios() {
		Integer planillaTrabajoId = null;
		List<CellExcelVO> header = new ArrayList<CellExcelVO>();
		List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
		header.add((new CellExcelVO("SERVICIO", 1, 2)));
		header.add((new CellExcelVO("PROGRAMA", 1, 2)));
		header.add((new CellExcelVO("MARCO PRESUPUESTARIO", 1, 2)));

		header.add((new CellExcelVO("REMESA ACUMULADA", 1, 1)));
		header.add((new CellExcelVO("CONVENIO", 1, 1)));

		header.add((new CellExcelVO("CONVENIO PENDIENTE", 1, 2)));

		subHeader.add((new CellExcelVO("MONTO", 1, 1)));
		subHeader.add((new CellExcelVO("%", 1, 1)));
		subHeader.add((new CellExcelVO("MONTO", 1, 1)));
		subHeader.add((new CellExcelVO("%", 1, 1)));

		List<ReporteMonitoreoProgramaPorEstablecimientoVO> reporteMonitoreoProgramaPorEstablecimientoVOSub21 = getReporteMonitoreoPorEstablecimientoAll(Subtitulo.SUBTITULO21);
		List<ReporteMonitoreoProgramaPorEstablecimientoVO> reporteMonitoreoProgramaPorEstablecimientoVOSub22 = getReporteMonitoreoPorEstablecimientoAll(Subtitulo.SUBTITULO22);
		List<ReporteMonitoreoProgramaPorEstablecimientoVO> reporteMonitoreoProgramaPorEstablecimientoVOSub29 = getReporteMonitoreoPorEstablecimientoAll(Subtitulo.SUBTITULO29);

		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator
				+ "Panilla Reporte Monitoreo Programa - Servicios.xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());

		GeneradorExcel generadorExcel = new GeneradorExcel(filename);

		ReporteMonitoreoProgramaEstablecimientoSheetExcel reporteMonitoreoProgramaEstablecimientoSheetExcelSub21 = new ReporteMonitoreoProgramaEstablecimientoSheetExcel(
				header, subHeader, null);

		ReporteMonitoreoProgramaEstablecimientoSheetExcel reporteMonitoreoProgramaEstablecimientoSheetExcelSub22 = new ReporteMonitoreoProgramaEstablecimientoSheetExcel(
				header, subHeader, null);

		ReporteMonitoreoProgramaEstablecimientoSheetExcel reporteMonitoreoProgramaEstablecimientoSheetExcelSub29 = new ReporteMonitoreoProgramaEstablecimientoSheetExcel(
				header, subHeader, null);

		reporteMonitoreoProgramaEstablecimientoSheetExcelSub21
				.setItems(reporteMonitoreoProgramaPorEstablecimientoVOSub21);
		reporteMonitoreoProgramaEstablecimientoSheetExcelSub22
				.setItems(reporteMonitoreoProgramaPorEstablecimientoVOSub22);
		reporteMonitoreoProgramaEstablecimientoSheetExcelSub29
				.setItems(reporteMonitoreoProgramaPorEstablecimientoVOSub29);

		generadorExcel.addSheet(
				reporteMonitoreoProgramaEstablecimientoSheetExcelSub21,
				"Subtitulo 21");
		generadorExcel.addSheet(
				reporteMonitoreoProgramaEstablecimientoSheetExcelSub22,
				"Subtitulo 22");
		generadorExcel.addSheet(
				reporteMonitoreoProgramaEstablecimientoSheetExcelSub29,
				"Subtitulo 29");

		try {
			BodyVO response = alfrescoService.uploadDocument(
					generadorExcel.saveExcel(), contenType,
					folderReportes.replace("{ANO}", getAnoCurso().toString()));

			TipoDocumento tipoDocumento = new TipoDocumento(
					TipoDocumentosProcesos.REPORTEMONITOREOPROGRAMASERVICIO
							.getId());
			planillaTrabajoId = documentService.createDocumentReportes(
					tipoDocumento, response.getNodeRef(),
					response.getFileName(), contenType, getAnoCurso(),
					Integer.parseInt(getMesCurso(true)));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return planillaTrabajoId;
	}

	public Integer generarPlanillaReporteGlosa07(String usuario) {
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
		header.add((new CellExcelVO(
				"ART. 56 REFORZAMIENTO "+mes.getNombre().toUpperCase()+" 2014 APS MUNICIPAL", 2, 1)));
		header.add((new CellExcelVO(
				"TOTAL REMESAS ENERO A "+mes.getNombre().toUpperCase()+" 2014 APS MUNICIPAL", 2, 1)));

		List<ReporteGlosaVO> reporteGlosaVO = this.getReporteGlosa(usuario);

		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator
				+ "Panilla Reporte Glosa07.xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());

		GeneradorExcel generadorExcel = new GeneradorExcel(filename);

		ReporteGlosa07SheetExcel reporteGlosa07SheetExcel = new ReporteGlosa07SheetExcel(
				header, null, null);
		reporteGlosa07SheetExcel.setItems(reporteGlosaVO);

		generadorExcel.addSheet(reporteGlosa07SheetExcel, "glosa07");

		try {
			BodyVO response = alfrescoService.uploadDocument(
					generadorExcel.saveExcel(), contenType,
					folderReportes.replace("{ANO}", getAnoCurso().toString()));
			// System.out.println("response planillaPropuestaEstimacionFlujoCajaConsolidador --->"
			// + response);

			TipoDocumento tipoDocumento = new TipoDocumento(
					TipoDocumentosProcesos.REPORTEGLOSA07.getId());
			planillaTrabajoId = documentService.createDocumentReportes(
					tipoDocumento, response.getNodeRef(),
					response.getFileName(), contenType, getAnoCurso(),
					Integer.parseInt(getMesCurso(true)));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return planillaTrabajoId;
	}

	public Integer generarPlanillaReporteMarcoPresupuestarioComuna() {
		Integer planillaTrabajoId = null;
		List<CellExcelVO> header = new ArrayList<CellExcelVO>();
		List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
		header.add((new CellExcelVO("SERVICIOS", 1, 1)));
		header.add((new CellExcelVO("LINEAS PROGRAMATICAS", 1, 1)));
		header.add((new CellExcelVO("COMPONENTES", 1, 1)));
		header.add((new CellExcelVO("COMUNAS", 1, 1)));
		header.add((new CellExcelVO("MARCO AÑO" + getAnoCurso(), 1, 1)));
		header.add((new CellExcelVO("CONVENIO AÑO" + getAnoCurso(), 1, 1)));
		header.add((new CellExcelVO("REMESAS ACUMULADAS AÑO" + getAnoCurso(),
				1, 1)));
		header.add((new CellExcelVO("% CUOTA TRANSFERIDA A "
				+ getMesCurso(true) + "-" + getAnoCurso(), 1, 1)));
		header.add((new CellExcelVO("OBSERVACIÓN" + getAnoCurso(), 1, 1)));

		List<ReporteMarcoPresupuestarioComunaVO> reporteMarcoPresupuestarioComunaVO = this
				.getReporteMarcoPorComunaAll(Subtitulo.SUBTITULO24);

		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator
				+ "Planilla Marco Presupuestario por Comuna.xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());

		GeneradorExcel generadorExcel = new GeneradorExcel(filename);

		ReporteMarcoPresupuestarioComunaSheetExcel reporteMarcoPresupuestarioComunaSheetExcel = new ReporteMarcoPresupuestarioComunaSheetExcel(
				header, subHeader, null);

		reporteMarcoPresupuestarioComunaSheetExcel
				.setItems(reporteMarcoPresupuestarioComunaVO);

		generadorExcel.addSheet(reporteMarcoPresupuestarioComunaSheetExcel,
				"Subtitulo 24");

		try {
			BodyVO response = alfrescoService.uploadDocument(
					generadorExcel.saveExcel(), contenType,
					folderReportes.replace("{ANO}", getAnoCurso().toString()));
			// System.out.println("response planillaPropuestaEstimacionFlujoCajaConsolidador --->"
			// + response);

			TipoDocumento tipoDocumento = new TipoDocumento(
					TipoDocumentosProcesos.REPORTEMARCOPRESPUESTARIOCOMUNA
							.getId());
			planillaTrabajoId = documentService.createDocumentReportes(
					tipoDocumento, response.getNodeRef(),
					response.getFileName(), contenType, getAnoCurso(),
					Integer.parseInt(getMesCurso(true)));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return planillaTrabajoId;
	}

	public Integer generarPlanillaReporteMarcoPresupuestarioServicios() {
		Integer planillaTrabajoId = null;
		List<CellExcelVO> header = new ArrayList<CellExcelVO>();
		List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
		header.add((new CellExcelVO("SERVICIOS", 1, 1)));
		header.add((new CellExcelVO("LINEAS PROGRAMATICAS", 1, 1)));
		header.add((new CellExcelVO("COMPONENTES", 1, 1)));
		header.add((new CellExcelVO("ESTABLECIMIENTOS", 1, 1)));
		header.add((new CellExcelVO("MARCO AÑO" + getAnoCurso(), 1, 1)));
		header.add((new CellExcelVO("CONVENIO AÑO" + getAnoCurso(), 1, 1)));
		header.add((new CellExcelVO("REMESAS ACUMULADAS AÑO" + getAnoCurso(),
				1, 1)));
		header.add((new CellExcelVO("% CUOTA TRANSFERIDA A "
				+ getMesCurso(true) + "-" + getAnoCurso(), 1, 1)));
		header.add((new CellExcelVO("OBSERVACIÓN" + getAnoCurso(), 1, 1)));

		List<ReporteMarcoPresupuestarioEstablecimientoVO> reporteMarcoPresupuestarioEstablecimientoVO21 = this
				.getReporteMarcoPorEstablecimientoAll(Subtitulo.SUBTITULO21);

		List<ReporteMarcoPresupuestarioEstablecimientoVO> reporteMarcoPresupuestarioEstablecimientoVO22 = this
				.getReporteMarcoPorEstablecimientoAll(Subtitulo.SUBTITULO22);

		List<ReporteMarcoPresupuestarioEstablecimientoVO> reporteMarcoPresupuestarioEstablecimientoVO29 = this
				.getReporteMarcoPorEstablecimientoAll(Subtitulo.SUBTITULO29);

		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator
				+ "Planilla Marco Presupuestario por Servicio.xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());

		GeneradorExcel generadorExcel = new GeneradorExcel(filename);

		ReporteMarcoPresupuestarioEstablecimientoSheetExcel reporteMarcoPresupuestarioEstablecimientoSheetExcel21 = new ReporteMarcoPresupuestarioEstablecimientoSheetExcel(
				header, subHeader, null);

		ReporteMarcoPresupuestarioEstablecimientoSheetExcel reporteMarcoPresupuestarioEstablecimientoSheetExcel22 = new ReporteMarcoPresupuestarioEstablecimientoSheetExcel(
				header, subHeader, null);

		ReporteMarcoPresupuestarioEstablecimientoSheetExcel reporteMarcoPresupuestarioEstablecimientoSheetExcel29 = new ReporteMarcoPresupuestarioEstablecimientoSheetExcel(
				header, subHeader, null);

		reporteMarcoPresupuestarioEstablecimientoSheetExcel21
				.setItems(reporteMarcoPresupuestarioEstablecimientoVO21);
		reporteMarcoPresupuestarioEstablecimientoSheetExcel22
				.setItems(reporteMarcoPresupuestarioEstablecimientoVO22);
		reporteMarcoPresupuestarioEstablecimientoSheetExcel29
				.setItems(reporteMarcoPresupuestarioEstablecimientoVO29);

		generadorExcel.addSheet(
				reporteMarcoPresupuestarioEstablecimientoSheetExcel21,
				"Subtitulo 21");
		generadorExcel.addSheet(
				reporteMarcoPresupuestarioEstablecimientoSheetExcel22,
				"Subtitulo 22");
		generadorExcel.addSheet(
				reporteMarcoPresupuestarioEstablecimientoSheetExcel29,
				"Subtitulo 29");

		try {
			BodyVO response = alfrescoService.uploadDocument(
					generadorExcel.saveExcel(), contenType,
					folderReportes.replace("{ANO}", getAnoCurso().toString()));
			// System.out.println("response planillaPropuestaEstimacionFlujoCajaConsolidador --->"
			// + response);

			TipoDocumento tipoDocumento = new TipoDocumento(
					TipoDocumentosProcesos.REPORTEMARCOPRESPUESTARIOSERVICIO
							.getId());
			planillaTrabajoId = documentService.createDocumentReportes(
					tipoDocumento, response.getNodeRef(),
					response.getFileName(), contenType, getAnoCurso(),
					Integer.parseInt(getMesCurso(true)));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return planillaTrabajoId;
	}

	public List<ReporteMarcoPresupuestarioComunaVO> getReporteMarcoPorComunaAll(
			Subtitulo subtitulo) {
		Integer ano = getAnoCurso();
		List<ReporteMarcoPresupuestarioComunaVO> resultado = new ArrayList<ReporteMarcoPresupuestarioComunaVO>();
		List<ProgramaVO> programasVO = programasService
				.getProgramasBySubtitulo(subtitulo);

		for (ProgramaVO programa : programasVO) {
			List<ServicioSalud> servicios = servicioSaludDAO
					.getServiciosOrderId();
			for (ServicioSalud servicio : servicios) {
				List<Comuna> comunas = servicio.getComunas();
				List<ComponentesVO> componentes = programasService.getComponenteByProgramaSubtitulos(programa.getId(), subtitulo);
				for (ComponentesVO componenteVO : componentes) {

					for (Comuna comuna : comunas) {
						ReporteMarcoPresupuestarioComunaVO reporteMarcoPresupuestarioComunaVO = new ReporteMarcoPresupuestarioComunaVO();
						reporteMarcoPresupuestarioComunaVO.setServicio(servicio
								.getNombre());
						reporteMarcoPresupuestarioComunaVO.setPrograma(programa
								.getNombre());
						reporteMarcoPresupuestarioComunaVO
								.setComponente(componenteVO.getNombre());
						reporteMarcoPresupuestarioComunaVO.setComuna(comuna
								.getNombre());

						// AntecendentesComunaCalculado
						// antecendentesComunaCalculado = antecedentesComunaDAO
						// .findByComunaAno(comuna.getId(), ano);
						// if (antecendentesComunaCalculado == null) {
						// continue;
						// }
						//
						// Long tarifa = 0L;
						// List<ProgramaMunicipalCoreComponente>
						// programaMunicipalCoreComponentes = programasDAO
						// .getByIdComuna(comuna.getId());
						// for (ProgramaMunicipalCoreComponente
						// programaMunicipalCoreComponente :
						// programaMunicipalCoreComponentes) {
						// if (programaMunicipalCoreComponente == null) {
						// continue;
						// }
						// tarifa +=
						// programaMunicipalCoreComponente.getTarifa();
						// }
						//
						// ConvenioComuna convenioComuna = conveniosDAO
						// .getConvenioComunaByIdComunaIdProgramaAno(
						// comuna.getId(), programa.getIdProgramaAno());
						// if (convenioComuna == null) {
						// continue;
						// }
						// ConvenioComunaComponente convenioComunaComponente =
						// conveniosDAO
						// .getByIdConvenioComunaIdSubtituloIdComponente(
						// convenioComuna.getIdConvenioComuna(),
						// subtitulo.getId(), componenteVO.getId());
						//
						// if (convenioComunaComponente == null) {
						// continue;
						// }

						// Long marcoTEMP =
						// antecendentesComunaCalculado.getPercapitaAno()
						// + tarifa;
						reporteMarcoPresupuestarioComunaVO.setMarco((long) 10
								* programa.getIdProgramaAno()
								* servicio.getId() * comuna.getId());

						// Long conveniosTEMP = (long)
						// convenioComunaComponente.getMonto();
						reporteMarcoPresupuestarioComunaVO
								.setConvenios((long) 13
										* programa.getIdProgramaAno()
										* comuna.getId());
						Double porcentajeCuotaTransferida = 0.0;

						// TODO cambiar este valor despues
						// Long remesasAcumuladasTEMP = (long)
						// (convenioComunaComponente.getMonto() + 3);
						if (Integer.parseInt(getMesCurso(true)) > 10) {
							porcentajeCuotaTransferida = 1.0;
						} else {
							Cuota cuota = reliquidacionDAO
									.getCuotaByIdProgramaAnoNroCuota(
											programa.getIdProgramaAno(),
											(short) 1);
							if (cuota.getIdMes().getIdMes() < Integer
									.parseInt(getMesCurso(true))) {
								porcentajeCuotaTransferida = (double) cuota
										.getPorcentaje() / 100;
								reporteMarcoPresupuestarioComunaVO
										.setPorcentajeCuotaTransferida(porcentajeCuotaTransferida);
							} else {
								reporteMarcoPresupuestarioComunaVO
										.setPorcentajeCuotaTransferida(0.6);
							}
						}
						// TODO cambiar despues
						reporteMarcoPresupuestarioComunaVO
								.setPorcentajeCuotaTransferida(1.0);
						reporteMarcoPresupuestarioComunaVO
								.setRemesasAcumuladas((long) servicio.getId()
										* comuna.getId() * 6);
						reporteMarcoPresupuestarioComunaVO.setObservacion("");

						resultado.add(reporteMarcoPresupuestarioComunaVO);

					}

				}

			}

		}

		return resultado;
	}

	public List<ReporteMarcoPresupuestarioEstablecimientoVO> getReporteMarcoPorEstablecimientoAll(
			Subtitulo subtitulo) {
		List<ReporteMarcoPresupuestarioEstablecimientoVO> resultado = new ArrayList<ReporteMarcoPresupuestarioEstablecimientoVO>();
		List<ProgramaVO> programasVO = programasService
				.getProgramasBySubtitulo(subtitulo);

		for (ProgramaVO programa : programasVO) {
			List<ServicioSalud> servicios = servicioSaludDAO
					.getServiciosOrderId();
			for (ServicioSalud servicio : servicios) {
				List<Establecimiento> establecimientos = servicio
						.getEstablecimientos();
				List<ComponentesVO> componentes = programasService.getComponenteByProgramaSubtitulos(programa.getId(), subtitulo);
				for (ComponentesVO componenteVO : componentes) {

					for (Establecimiento establecimiento : establecimientos) {
						ReporteMarcoPresupuestarioEstablecimientoVO reporteMarcoPresupuestarioEstablecimientoVO = new ReporteMarcoPresupuestarioEstablecimientoVO();

						reporteMarcoPresupuestarioEstablecimientoVO
								.setServicio(servicio.getNombre());
						reporteMarcoPresupuestarioEstablecimientoVO
								.setPrograma(programa.getNombre());
						reporteMarcoPresupuestarioEstablecimientoVO
								.setComponente(componenteVO.getNombre());
						reporteMarcoPresupuestarioEstablecimientoVO
								.setEstablecimiento(establecimiento.getNombre());

						// ProgramaServicioCore programaServicioCore =
						// programasDAO.getProgramaServicioCoreByProgramaAnoEstablecimiento(idProgramaAno,
						// establecimiento.getId());
						// if(programaServicioCore == null){
						// continue;
						// }
						// System.out.println("programaServicioCore --> "+programaServicioCore.getEstablecimiento());
						//
						// ProgramaServicioCoreComponente
						// programaServicioCoreComponente
						// =
						// programasDAO.getProgramaServicioCoreComponenteByProgramaAnoEstablecimientoServicioComponenteSubtitulo(idProgramaAno,
						// establecimiento.getId(), idServicio,
						// componenteVO.getId(),
						// subtitulo.getId());
						// if(programaServicioCoreComponente == null){
						// continue;
						// }

						// TODO cambiar esto por la consulta hecha
						reporteMarcoPresupuestarioEstablecimientoVO
								.setMarco((long) 15000
										* establecimiento.getId());

						ConvenioServicioComponente convenioServicioComponente = conveniosDAO
								.getConvenioServicioComponenteByIdSubtituloIdComponente(
										programa.getIdProgramaAno(),
										componenteVO.getId(),
										subtitulo.getId(),
										establecimiento.getId());
						if (convenioServicioComponente == null) {
							System.out
									.println("convenioServicioComponente es null");
							continue;
						}
						reporteMarcoPresupuestarioEstablecimientoVO
								.setConvenios((long) convenioServicioComponente
										.getMonto());

						// TODO cambiar esto por la remesa. Cargar datos en la
						// tabla 1ro
						reporteMarcoPresupuestarioEstablecimientoVO
								.setRemesasAcumuladas((long) (convenioServicioComponente
										.getMonto() - 3434));

						Cuota cuota = reliquidacionDAO
								.getCuotaByIdProgramaAnoNroCuota(
										programa.getIdProgramaAno(), (short) 1);
						if (cuota == null) {
							continue;
						}
						Double porcentajeCuotaTransferida = 0.0;
						System.out.println("getMesCurso(true) ---> "
								+ getMesCurso(true)
								+ "  cuota.getIdMes().getIdMes() --> "
								+ cuota.getIdMes().getIdMes());
						if (cuota.getIdMes().getIdMes() < Integer
								.parseInt(getMesCurso(true))) {
							porcentajeCuotaTransferida = (double) cuota
									.getPorcentaje() / 100;
							reporteMarcoPresupuestarioEstablecimientoVO
									.setPorcentajeCuotaTransferida(porcentajeCuotaTransferida);
						} else {
							reporteMarcoPresupuestarioEstablecimientoVO
									.setPorcentajeCuotaTransferida(0.6);
						}
						reporteMarcoPresupuestarioEstablecimientoVO
								.setObservacion("");

						resultado
								.add(reporteMarcoPresupuestarioEstablecimientoVO);

						// //TODO cambiar despues
						// reporteMarcoPresupuestarioVO.setPorcentajeCuotaTransferida(1.0);
						// reporteMarcoPresupuestarioVO.setRemesasAcumuladas(remesasAcumuladasTEMP);
						// reporteMarcoPresupuestarioVO.setObservacion("");

					}

				}

			}

		}

		return resultado;
	}

	public List<ReporteMonitoreoProgramaPorComunaVO> getReporteMonitoreoPorComunaAll(
			Subtitulo subtitulo) {
		List<ReporteMonitoreoProgramaPorComunaVO> resultado = new ArrayList<ReporteMonitoreoProgramaPorComunaVO>();
		List<ProgramaVO> programas = programasService
				.getProgramasBySubtitulo(subtitulo);
		for (ProgramaVO programa : programas) {
			List<ServicioSalud> servicios = servicioSaludDAO
					.getServiciosOrderId();
			List<ComponentesVO> componentes = programasService.getComponenteByProgramaSubtitulos(programa.getId(), subtitulo);
			for(ComponentesVO componente : componentes){
			
				for (ServicioSalud servicio : servicios) {
					for (Comuna comuna : servicio.getComunas()) {
						ReporteMonitoreoProgramaPorComunaVO reporteMonitoreoProgramaPorComunaVO = new ReporteMonitoreoProgramaPorComunaVO();
						reporteMonitoreoProgramaPorComunaVO.setServicio(servicio
								.getNombre());
						reporteMonitoreoProgramaPorComunaVO.setPrograma(programa
								.getNombre());
						reporteMonitoreoProgramaPorComunaVO.setComponente(componente.getNombre());
						reporteMonitoreoProgramaPorComunaVO.setComuna(comuna
								.getNombre());
						reporteMonitoreoProgramaPorComunaVO
								.setMarco((long) subtitulo.getId()
										* programa.getIdProgramaAno()
										* comuna.getId() * 17);
						reporteMonitoreoProgramaPorComunaVO
								.setRemesa_monto((long) (9081 + subtitulo.getId()
										* comuna.getId() * 1103));
						reporteMonitoreoProgramaPorComunaVO
								.setRemesa_porcentaje(1.0);
						reporteMonitoreoProgramaPorComunaVO
								.setConvenio_monto((long) (15081 + subtitulo
										.getId() * comuna.getId() * 123));
						reporteMonitoreoProgramaPorComunaVO
								.setConvenio_porcentaje(1.0);
						reporteMonitoreoProgramaPorComunaVO
								.setConvenio_pendiente((long) 0);
						resultado.add(reporteMonitoreoProgramaPorComunaVO);
					}

				}
			
			}
			
		}
		return resultado;
	}

	public List<ReporteMonitoreoProgramaPorComunaVO> getReporteMonitoreoPorComunaFiltroPrograma(
			Integer idProgramaAno, Subtitulo subtitulo) {
		List<ReporteMonitoreoProgramaPorComunaVO> resultado = new ArrayList<ReporteMonitoreoProgramaPorComunaVO>();
		ProgramaVO programa = programasService.getProgramaAno(idProgramaAno);
		List<ComponentesVO> componentes = programasService.getComponenteByProgramaSubtitulos(programa.getId(), subtitulo);
		for(ComponentesVO componente : componentes){
			List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
			for (ServicioSalud servicio : servicios) {
				for (Comuna comuna : servicio.getComunas()) {
					ReporteMonitoreoProgramaPorComunaVO reporteMonitoreoProgramaPorComunaVO = new ReporteMonitoreoProgramaPorComunaVO();
					reporteMonitoreoProgramaPorComunaVO.setServicio(servicio
							.getNombre());
					reporteMonitoreoProgramaPorComunaVO.setPrograma(programa
							.getNombre());
					reporteMonitoreoProgramaPorComunaVO.setComuna(comuna
							.getNombre());
					reporteMonitoreoProgramaPorComunaVO.setMarco((long) subtitulo
							.getId()
							* programa.getIdProgramaAno()
							* comuna.getId()
							* 17);
					reporteMonitoreoProgramaPorComunaVO
							.setRemesa_monto((long) (9081 + subtitulo.getId()
									* comuna.getId() * 1103));
					reporteMonitoreoProgramaPorComunaVO.setRemesa_porcentaje(1.0);
					reporteMonitoreoProgramaPorComunaVO
							.setConvenio_monto((long) (15081 + subtitulo.getId()
									* comuna.getId() * 123));
					reporteMonitoreoProgramaPorComunaVO.setConvenio_porcentaje(1.0);
					reporteMonitoreoProgramaPorComunaVO
							.setConvenio_pendiente((long) 0);
					System.out.println("Comuna --> "
							+ reporteMonitoreoProgramaPorComunaVO.getComuna()
							+ "  Marco() ---> "
							+ reporteMonitoreoProgramaPorComunaVO.getMarco());
					resultado.add(reporteMonitoreoProgramaPorComunaVO);
				}

			}
		}

		

		return resultado;

	}

	public List<ReporteMonitoreoProgramaPorComunaVO> getReporteMonitoreoPorComunaFiltroServicioPrograma(
			Integer idProgramaAno, Integer idServicio, Subtitulo subtitulo) {
		System.out.println("entra aqui");
		
		List<ReporteMonitoreoProgramaPorComunaVO> resultado = new ArrayList<ReporteMonitoreoProgramaPorComunaVO>();
		ProgramaVO programa = programasService.getProgramaAno(idProgramaAno);
		List<ComponentesVO> componentes = programasService.getComponenteByProgramaSubtitulos(programa.getId(), subtitulo);
		for(ComponentesVO componente : componentes){
			ServicioSalud servicio = servicioSaludDAO
					.getServicioSaludPorID(idServicio);

			for (Comuna comuna : servicio.getComunas()) {
				ReporteMonitoreoProgramaPorComunaVO reporteMonitoreoProgramaPorComunaVO = new ReporteMonitoreoProgramaPorComunaVO();
				reporteMonitoreoProgramaPorComunaVO.setServicio(servicio
						.getNombre());
				reporteMonitoreoProgramaPorComunaVO.setPrograma(programa
						.getNombre());
				reporteMonitoreoProgramaPorComunaVO.setComponente(componente.getNombre());
				reporteMonitoreoProgramaPorComunaVO.setComuna(comuna.getNombre());
				
				AntecendentesComunaCalculado antecendentesComunaCalculado = antecedentesComunaDAO
						.findByComunaAnoVigencia(comuna.getId(), getAnoCurso());
				
				if(antecendentesComunaCalculado == null){
					continue;
				}
				
				Long percapitaAno = antecendentesComunaCalculado.getPercapitaAno();
				Long desempenoDificil = (long)antecendentesComunaCalculado.getDesempenoDificil();
				Long tarifa = 0L;
				
				List<ProgramaMunicipalCoreComponente>  programaMunicipalCoreComponentes = programasDAO.getByIdComunaIdProgramaAno(comuna.getId(), idProgramaAno);
				if (programaMunicipalCoreComponentes != null) {
					System.out.println("programaMunicipalCoreComponente es null");
					for (ProgramaMunicipalCoreComponente programaMunicipalCoreComponente : programaMunicipalCoreComponentes) {
						
						tarifa += programaMunicipalCoreComponente.getTarifa();
					}
				}
				
				Integer mesActual = Integer.parseInt(getMesCurso(true));
				reporteMonitoreoProgramaPorComunaVO.setMarco(percapitaAno + desempenoDificil + tarifa);
				
				Long totalRemesasAcumuladasMesActual = remesasDAO.getRemesasPagadasComunaProgramaSubtitulo(programa.getIdProgramaAno(), subtitulo.getId(), comuna.getId(), mesActual);
				if(totalRemesasAcumuladasMesActual > 0L){
					System.out.println("Marco --> "+reporteMonitoreoProgramaPorComunaVO.getMarco());
					System.out.println("totalRemesasAcumuladasMesActual --> "+totalRemesasAcumuladasMesActual);
					
					Long marcoMenosRemesasPagadas = reporteMonitoreoProgramaPorComunaVO.getMarco() - totalRemesasAcumuladasMesActual;
					System.out.println("marcoMenosRemesasPagadas ---> "+marcoMenosRemesasPagadas);
					Double porcentajeRemesasPagadas = (totalRemesasAcumuladasMesActual*100.0)/reporteMonitoreoProgramaPorComunaVO.getMarco();
					porcentajeRemesasPagadas = porcentajeRemesasPagadas/100.0;
					System.out.println("porcentajeRemesasPagadas --> "+porcentajeRemesasPagadas);
					
					
					reporteMonitoreoProgramaPorComunaVO.setRemesa_monto(totalRemesasAcumuladasMesActual);
					reporteMonitoreoProgramaPorComunaVO.setRemesa_porcentaje(porcentajeRemesasPagadas);
				}else{
					reporteMonitoreoProgramaPorComunaVO.setRemesa_monto(0L);
					reporteMonitoreoProgramaPorComunaVO.setRemesa_porcentaje(0.0);
				}
				
				
				reporteMonitoreoProgramaPorComunaVO.setConvenio_monto(0L);
				reporteMonitoreoProgramaPorComunaVO.setConvenio_porcentaje(1.0);
				reporteMonitoreoProgramaPorComunaVO.setConvenio_pendiente((long) 0);
				resultado.add(reporteMonitoreoProgramaPorComunaVO);
			}
		}

		

		return resultado;

	}

	public List<ReporteMonitoreoProgramaPorEstablecimientoVO> getReporteMonitoreoPorEstablecimientoAll(
			Subtitulo subtitulo) {
		List<ReporteMonitoreoProgramaPorEstablecimientoVO> resultado = new ArrayList<ReporteMonitoreoProgramaPorEstablecimientoVO>();
		List<ProgramaVO> programas = programasService
				.getProgramasBySubtitulo(subtitulo);
		for (ProgramaVO programa : programas) {
			
			for(ComponentesVO componente : programa.getComponentes()){
				List<ServicioSalud> servicios = servicioSaludDAO
						.getServiciosOrderId();
				for (ServicioSalud servicio : servicios) {
					ReporteMonitoreoProgramaPorEstablecimientoVO reporteMonitoreoProgramaPorEstablecimientoVO = new ReporteMonitoreoProgramaPorEstablecimientoVO();
					reporteMonitoreoProgramaPorEstablecimientoVO
							.setServicio(servicio.getNombre());
					reporteMonitoreoProgramaPorEstablecimientoVO
							.setPrograma(programa.getNombre());
					reporteMonitoreoProgramaPorEstablecimientoVO
							.setMarco((long) subtitulo.getId()
									* programa.getIdProgramaAno()
									* servicio.getId() * 4175 + 950678);
					reporteMonitoreoProgramaPorEstablecimientoVO
							.setRemesa_monto((long) (9081 + subtitulo.getId()
									* servicio.getId() * 103));
					reporteMonitoreoProgramaPorEstablecimientoVO
							.setRemesa_porcentaje(1.0);
					reporteMonitoreoProgramaPorEstablecimientoVO
							.setConvenio_monto((long) (15081 + subtitulo.getId()
									* servicio.getId() * 523));
					reporteMonitoreoProgramaPorEstablecimientoVO
							.setConvenio_porcentaje(1.0);
					reporteMonitoreoProgramaPorEstablecimientoVO
							.setConvenio_pendiente((long) 0);
					resultado.add(reporteMonitoreoProgramaPorEstablecimientoVO);

				}
			}
			
		}

		return resultado;
	}

	public List<ReporteMonitoreoProgramaPorEstablecimientoVO> getReporteMonitoreoPorEstablecimientoFiltroPrograma(
			Integer idProgramaAno, Subtitulo subtitulo) {
		List<ReporteMonitoreoProgramaPorEstablecimientoVO> resultado = new ArrayList<ReporteMonitoreoProgramaPorEstablecimientoVO>();
		ProgramaVO programa = programasService.getProgramaAno(idProgramaAno);
		
		List<ComponentesVO> componentes = programasService.getComponenteByProgramaSubtitulos(programa.getId(), subtitulo);
		
		for(ComponentesVO componente : componentes){
			List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
			for (ServicioSalud servicio : servicios) {
				ReporteMonitoreoProgramaPorEstablecimientoVO reporteMonitoreoProgramaPorEstablecimientoVO = new ReporteMonitoreoProgramaPorEstablecimientoVO();
				reporteMonitoreoProgramaPorEstablecimientoVO.setServicio(servicio
						.getNombre());
				reporteMonitoreoProgramaPorEstablecimientoVO.setPrograma(programa
						.getNombre());
				reporteMonitoreoProgramaPorEstablecimientoVO.setComponente(componente.getNombre());
				
				reporteMonitoreoProgramaPorEstablecimientoVO
						.setMarco((long) subtitulo.getId()
								* programa.getIdProgramaAno() * servicio.getId()
								* 4175 + 950678);
				reporteMonitoreoProgramaPorEstablecimientoVO
						.setRemesa_monto((long) (9081 + subtitulo.getId()
								* servicio.getId() * 103));
				reporteMonitoreoProgramaPorEstablecimientoVO
						.setRemesa_porcentaje(1.0);
				reporteMonitoreoProgramaPorEstablecimientoVO
						.setConvenio_monto((long) (15081 + subtitulo.getId()
								* servicio.getId() * 523));
				reporteMonitoreoProgramaPorEstablecimientoVO
						.setConvenio_porcentaje(1.0);
				reporteMonitoreoProgramaPorEstablecimientoVO
						.setConvenio_pendiente((long) 0);
				resultado.add(reporteMonitoreoProgramaPorEstablecimientoVO);

			}
		}
		
		
		return resultado;
	}

	public List<ReporteMonitoreoProgramaPorEstablecimientoVO> getReporteMonitoreoPorEstablecimientoFiltroServicioPrograma(
			Integer idProgramaAno, Integer idServicio, Subtitulo subtitulo) {
		List<ReporteMonitoreoProgramaPorEstablecimientoVO> resultado = new ArrayList<ReporteMonitoreoProgramaPorEstablecimientoVO>();
		ProgramaVO programa = programasService.getProgramaAno(idProgramaAno);

		ServicioSalud servicio = servicioSaludDAO.getServicioSaludPorID(idServicio);
		List<ComponentesVO> componentes = programasService.getComponenteByProgramaSubtitulos(programa.getId(), subtitulo);
		
		for(ComponentesVO componente : componentes){
			for(Establecimiento establecimiento : servicio.getEstablecimientos()){
				
				
				ReporteMonitoreoProgramaPorEstablecimientoVO reporteMonitoreoProgramaPorEstablecimientoVO = new ReporteMonitoreoProgramaPorEstablecimientoVO();
				reporteMonitoreoProgramaPorEstablecimientoVO.setServicio(servicio
						.getNombre());
				reporteMonitoreoProgramaPorEstablecimientoVO.setPrograma(programa
						.getNombre());
				reporteMonitoreoProgramaPorEstablecimientoVO.setComponente(componente.getNombre());
				
				Long marcoAnoActual = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAno, componente.getId(), subtitulo.getId());

				
				reporteMonitoreoProgramaPorEstablecimientoVO.setMarco(marcoAnoActual);
				
				Integer mesActual = Integer.parseInt(getMesCurso(true));
				Long totalRemesasAcumuladasMesActual = remesasDAO.getRemesasPagadasEstablecimientoProgramaSubtitulo(programa.getIdProgramaAno(), subtitulo.getId(), establecimiento.getId(), mesActual);
				if(totalRemesasAcumuladasMesActual > 0L){
					Double porcentajeRemesasPagadas = (totalRemesasAcumuladasMesActual*100.0)/reporteMonitoreoProgramaPorEstablecimientoVO.getMarco();
					porcentajeRemesasPagadas = porcentajeRemesasPagadas/100.0;
					System.out.println("porcentajeRemesasPagadas --> "+porcentajeRemesasPagadas);
					reporteMonitoreoProgramaPorEstablecimientoVO.setRemesa_monto(totalRemesasAcumuladasMesActual);
					reporteMonitoreoProgramaPorEstablecimientoVO.setRemesa_porcentaje(porcentajeRemesasPagadas);
				}
				else{
					reporteMonitoreoProgramaPorEstablecimientoVO.setRemesa_monto(0L);
					reporteMonitoreoProgramaPorEstablecimientoVO.setRemesa_porcentaje(0.0);
				}
				
				reporteMonitoreoProgramaPorEstablecimientoVO
						.setConvenio_monto(0L);
				reporteMonitoreoProgramaPorEstablecimientoVO
						.setConvenio_porcentaje(0.0);
				reporteMonitoreoProgramaPorEstablecimientoVO.setConvenio_pendiente(0L);
				resultado.add(reporteMonitoreoProgramaPorEstablecimientoVO);
			}
		}

		return resultado;

	}

	public List<ReporteEstadoSituacionByComunaVO> getReporteEstadoSituacionByComunaAll(
			Subtitulo subtitulo) {
		List<ReporteEstadoSituacionByComunaVO> resultado = new ArrayList<ReporteEstadoSituacionByComunaVO>();
		List<ProgramaVO> programas = programasService
				.getProgramasBySubtitulo(subtitulo);
		for (ProgramaVO programa : programas) {
			List<ServicioSalud> servicios = servicioSaludDAO
					.getServiciosOrderId();
			for (ServicioSalud servicio : servicios) {
				for (Comuna comuna : servicio.getComunas()) {
					ReporteEstadoSituacionByComunaVO reporteEstadoSituacionByComunaVO = new ReporteEstadoSituacionByComunaVO();

					reporteEstadoSituacionByComunaVO.setPrograma(programa
							.getNombre());
					reporteEstadoSituacionByComunaVO.setServicio(servicio
							.getNombre());
					reporteEstadoSituacionByComunaVO.setComuna(comuna
							.getNombre());
					reporteEstadoSituacionByComunaVO
							.setMarco_inicial((long) subtitulo.getId()
									* programa.getIdProgramaAno()
									* comuna.getId() * 17);
					reporteEstadoSituacionByComunaVO
							.setMarco_modificado((long) subtitulo.getId()
									* programa.getIdProgramaAno()
									* comuna.getId() * 55);
					reporteEstadoSituacionByComunaVO
							.setConvenioRecibido_monto((long) (15081 + subtitulo
									.getId() * comuna.getId() * 123));
					reporteEstadoSituacionByComunaVO
							.setConvenioRecibido_porcentaje(1.0);
					reporteEstadoSituacionByComunaVO
							.setConvenioPendiente_monto(0L);
					reporteEstadoSituacionByComunaVO
							.setConvenioPendiente_porcentaje(0.0);
					reporteEstadoSituacionByComunaVO
							.setRemesaAcumulada_monto((long) (9081 + subtitulo
									.getId() * comuna.getId() * 1103));
					reporteEstadoSituacionByComunaVO
							.setRemesaAcumulada_porcentaje(1.0);
					reporteEstadoSituacionByComunaVO
							.setRemesaPendiente_monto(0L);
					reporteEstadoSituacionByComunaVO
							.setRemesaPendiente_porcentaje(0.0);
					reporteEstadoSituacionByComunaVO
							.setReliquidacion_monto((long) programa
									.getIdProgramaAno() * comuna.getId() * 7);
					reporteEstadoSituacionByComunaVO
							.setReliquidacion_porcentaje(0.2);
					reporteEstadoSituacionByComunaVO
							.setIncremento(reporteEstadoSituacionByComunaVO
									.getMarco_modificado()
									- reporteEstadoSituacionByComunaVO
											.getMarco_inicial());
					resultado.add(reporteEstadoSituacionByComunaVO);
				}

			}
		}
		return resultado;
	}

	public List<ReporteEstadoSituacionByComunaVO> getReporteEstadoSituacionByComunaFiltroPrograma(
			Integer idProgramaAno, Subtitulo subtitulo) {
		List<ReporteEstadoSituacionByComunaVO> resultado = new ArrayList<ReporteEstadoSituacionByComunaVO>();
		ProgramaVO programa = programasService.getProgramaAno(idProgramaAno);
		List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
		for (ServicioSalud servicio : servicios) {
			for (Comuna comuna : servicio.getComunas()) {
				ReporteEstadoSituacionByComunaVO reporteEstadoSituacionByComunaVO = new ReporteEstadoSituacionByComunaVO();

				reporteEstadoSituacionByComunaVO.setPrograma(programa
						.getNombre());
				reporteEstadoSituacionByComunaVO.setServicio(servicio
						.getNombre());
				reporteEstadoSituacionByComunaVO.setComuna(comuna.getNombre());
				reporteEstadoSituacionByComunaVO
						.setMarco_inicial((long) subtitulo.getId()
								* programa.getIdProgramaAno() * comuna.getId()
								* 17);
				reporteEstadoSituacionByComunaVO
						.setMarco_modificado((long) subtitulo.getId()
								* programa.getIdProgramaAno() * comuna.getId()
								* 55);
				reporteEstadoSituacionByComunaVO
						.setConvenioRecibido_monto((long) (15081 + subtitulo
								.getId() * comuna.getId() * 123));
				reporteEstadoSituacionByComunaVO
						.setConvenioRecibido_porcentaje(1.0);
				reporteEstadoSituacionByComunaVO.setConvenioPendiente_monto(0L);
				reporteEstadoSituacionByComunaVO
						.setConvenioPendiente_porcentaje(0.0);
				reporteEstadoSituacionByComunaVO
						.setRemesaAcumulada_monto((long) (9081 + subtitulo
								.getId() * comuna.getId() * 1103));
				reporteEstadoSituacionByComunaVO
						.setRemesaAcumulada_porcentaje(1.0);
				reporteEstadoSituacionByComunaVO.setRemesaPendiente_monto(0L);
				reporteEstadoSituacionByComunaVO
						.setRemesaPendiente_porcentaje(0.0);
				reporteEstadoSituacionByComunaVO
						.setReliquidacion_monto((long) programa
								.getIdProgramaAno() * comuna.getId() * 7);
				reporteEstadoSituacionByComunaVO
						.setReliquidacion_porcentaje(0.2);
				reporteEstadoSituacionByComunaVO
						.setIncremento(reporteEstadoSituacionByComunaVO
								.getMarco_modificado()
								- reporteEstadoSituacionByComunaVO
										.getMarco_inicial());
				resultado.add(reporteEstadoSituacionByComunaVO);
			}

		}

		return resultado;
	}

	public List<ReporteEstadoSituacionByComunaVO> getReporteEstadoSituacionByComunaFiltroProgramaServicio(
			Integer idProgramaAno, Integer idServicio, Subtitulo subtitulo) {
		System.out.println("entra al metodo");

		List<ReporteEstadoSituacionByComunaVO> resultado = new ArrayList<ReporteEstadoSituacionByComunaVO>();
		ProgramaVO programa = programasService.getProgramaAno(idProgramaAno);
		ServicioSalud servicio = servicioSaludDAO
				.getServicioSaludPorID(idServicio);

		for (Comuna comuna : servicio.getComunas()) {
			ReporteEstadoSituacionByComunaVO reporteEstadoSituacionByComunaVO = new ReporteEstadoSituacionByComunaVO();

			reporteEstadoSituacionByComunaVO.setPrograma(programa.getNombre());
			reporteEstadoSituacionByComunaVO.setServicio(servicio.getNombre());
			reporteEstadoSituacionByComunaVO.setComuna(comuna.getNombre());
			reporteEstadoSituacionByComunaVO.setMarco_inicial((long) subtitulo
					.getId()
					* programa.getIdProgramaAno()
					* comuna.getId()
					* 17);
			reporteEstadoSituacionByComunaVO
					.setMarco_modificado((long) subtitulo.getId()
							* programa.getIdProgramaAno() * comuna.getId() * 55);
			reporteEstadoSituacionByComunaVO
					.setConvenioRecibido_monto((long) (15081 + subtitulo
							.getId() * comuna.getId() * 123));
			reporteEstadoSituacionByComunaVO
					.setConvenioRecibido_porcentaje(1.0);
			reporteEstadoSituacionByComunaVO.setConvenioPendiente_monto(0L);
			reporteEstadoSituacionByComunaVO
					.setConvenioPendiente_porcentaje(0.0);
			reporteEstadoSituacionByComunaVO
					.setRemesaAcumulada_monto((long) (9081 + subtitulo.getId()
							* comuna.getId() * 1103));
			reporteEstadoSituacionByComunaVO.setRemesaAcumulada_porcentaje(1.0);
			reporteEstadoSituacionByComunaVO.setRemesaPendiente_monto(0L);
			reporteEstadoSituacionByComunaVO.setRemesaPendiente_porcentaje(0.0);
			reporteEstadoSituacionByComunaVO
					.setReliquidacion_monto((long) programa.getIdProgramaAno()
							* comuna.getId() * 7);
			reporteEstadoSituacionByComunaVO.setReliquidacion_porcentaje(0.2);
			reporteEstadoSituacionByComunaVO
					.setIncremento(reporteEstadoSituacionByComunaVO
							.getMarco_modificado()
							- reporteEstadoSituacionByComunaVO
									.getMarco_inicial());
			resultado.add(reporteEstadoSituacionByComunaVO);
		}

		return resultado;
	}

	public List<ReporteEstadoSituacionByServiciosVO> getReporteEstadoSituacionByServicioAll(
			Subtitulo subtitulo) {
		List<ReporteEstadoSituacionByServiciosVO> resultado = new ArrayList<ReporteEstadoSituacionByServiciosVO>();
		List<ProgramaVO> programas = programasService
				.getProgramasBySubtitulo(subtitulo);
		for (ProgramaVO programa : programas) {
			List<ServicioSalud> servicios = servicioSaludDAO
					.getServiciosOrderId();
			for (ServicioSalud servicio : servicios) {
				ReporteEstadoSituacionByServiciosVO reporteEstadoSituacionByServiciosVO = new ReporteEstadoSituacionByServiciosVO();

				reporteEstadoSituacionByServiciosVO.setPrograma(programa
						.getNombre());
				reporteEstadoSituacionByServiciosVO.setServicio(servicio
						.getNombre());
				reporteEstadoSituacionByServiciosVO
						.setMarco_inicial((long) subtitulo.getId()
								* programa.getIdProgramaAno()
								* (992 + subtitulo.getId()) * 17);
				reporteEstadoSituacionByServiciosVO
						.setMarco_modificado((long) subtitulo.getId()
								* programa.getIdProgramaAno()
								* (992 + subtitulo.getId()) * 55);
				reporteEstadoSituacionByServiciosVO
						.setConvenioRecibido_monto((long) (15081 + subtitulo
								.getId() * (992 + subtitulo.getId()) * 123));
				reporteEstadoSituacionByServiciosVO
						.setConvenioRecibido_porcentaje(1.0);
				reporteEstadoSituacionByServiciosVO
						.setConvenioPendiente_monto(0L);
				reporteEstadoSituacionByServiciosVO
						.setConvenioPendiente_porcentaje(0.0);
				reporteEstadoSituacionByServiciosVO
						.setRemesaAcumulada_monto((long) (9081 + subtitulo
								.getId() * (992 + subtitulo.getId()) * 1103));
				reporteEstadoSituacionByServiciosVO
						.setRemesaAcumulada_porcentaje(1.0);
				reporteEstadoSituacionByServiciosVO
						.setRemesaPendiente_monto(0L);
				reporteEstadoSituacionByServiciosVO
						.setRemesaPendiente_porcentaje(0.0);
				reporteEstadoSituacionByServiciosVO
						.setReliquidacion_monto((long) programa
								.getIdProgramaAno()
								* (992 + subtitulo.getId())
								* 7);
				reporteEstadoSituacionByServiciosVO
						.setReliquidacion_porcentaje(0.2);
				reporteEstadoSituacionByServiciosVO
						.setIncremento(reporteEstadoSituacionByServiciosVO
								.getMarco_modificado()
								- reporteEstadoSituacionByServiciosVO
										.getMarco_inicial());
				resultado.add(reporteEstadoSituacionByServiciosVO);

			}
		}
		return resultado;
	}

	public List<ReporteEstadoSituacionByServiciosVO> getReporteEstadoSituacionByServicioFiltroProgramaServicio(
			Integer idProgramaAno, Integer idServicio, Subtitulo subtitulo) {
		List<ReporteEstadoSituacionByServiciosVO> resultado = new ArrayList<ReporteEstadoSituacionByServiciosVO>();
		ProgramaVO programa = programasService.getProgramaAno(idProgramaAno);
		ServicioSalud servicio = servicioSaludDAO
				.getServicioSaludPorID(idServicio);

		ReporteEstadoSituacionByServiciosVO reporteEstadoSituacionByServiciosVO = new ReporteEstadoSituacionByServiciosVO();

		reporteEstadoSituacionByServiciosVO.setPrograma(programa.getNombre());
		reporteEstadoSituacionByServiciosVO.setServicio(servicio.getNombre());
		reporteEstadoSituacionByServiciosVO.setMarco_inicial((long) subtitulo
				.getId()
				* programa.getIdProgramaAno()
				* (992 + subtitulo.getId()) * 17);
		reporteEstadoSituacionByServiciosVO
				.setMarco_modificado((long) subtitulo.getId()
						* programa.getIdProgramaAno()
						* (992 + subtitulo.getId()) * 55);
		reporteEstadoSituacionByServiciosVO
				.setConvenioRecibido_monto((long) (15081 + subtitulo.getId()
						* (992 + subtitulo.getId()) * 123));
		reporteEstadoSituacionByServiciosVO.setConvenioRecibido_porcentaje(1.0);
		reporteEstadoSituacionByServiciosVO.setConvenioPendiente_monto(0L);
		reporteEstadoSituacionByServiciosVO
				.setConvenioPendiente_porcentaje(0.0);
		reporteEstadoSituacionByServiciosVO
				.setRemesaAcumulada_monto((long) (9081 + subtitulo.getId()
						* (992 + subtitulo.getId()) * 1103));
		reporteEstadoSituacionByServiciosVO.setRemesaAcumulada_porcentaje(1.0);
		reporteEstadoSituacionByServiciosVO.setRemesaPendiente_monto(0L);
		reporteEstadoSituacionByServiciosVO.setRemesaPendiente_porcentaje(0.0);
		reporteEstadoSituacionByServiciosVO
				.setReliquidacion_monto((long) programa.getIdProgramaAno()
						* (992 + subtitulo.getId()) * 7);
		reporteEstadoSituacionByServiciosVO.setReliquidacion_porcentaje(0.2);
		reporteEstadoSituacionByServiciosVO
				.setIncremento(reporteEstadoSituacionByServiciosVO
						.getMarco_modificado()
						- reporteEstadoSituacionByServiciosVO
								.getMarco_inicial());
		resultado.add(reporteEstadoSituacionByServiciosVO);

		return resultado;
	}

	public Integer generarPlanillaReporteEstadoSituacionPorComuna() {
		Integer planillaTrabajoId = null;
		List<CellExcelVO> header = new ArrayList<CellExcelVO>();
		List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
		header.add((new CellExcelVO("PROGRAMA", 1, 2)));
		header.add((new CellExcelVO("SERVICIO", 1, 2)));
		header.add((new CellExcelVO("COMUNA", 1, 2)));
		header.add((new CellExcelVO("MARCO PRESUPUESTARIO INICIAL", 1, 2)));
		header.add((new CellExcelVO("MARCO PRESUPUESTARIO MODIFICADO", 1, 1)));
		header.add((new CellExcelVO("CONVENIOS RECIBIDOS", 1, 1)));
		header.add((new CellExcelVO("CONVENIOS PENDIENTES", 1, 1)));
		header.add((new CellExcelVO("REMESA ACUMULADA", 1, 1)));
		header.add((new CellExcelVO("RELIQUIDACION", 1, 1)));
		header.add((new CellExcelVO("CONVENIO", 1, 1)));
		header.add((new CellExcelVO("INCREMENTO", 1, 2)));

		subHeader.add((new CellExcelVO("MONTO", 1, 1)));
		subHeader.add((new CellExcelVO("%", 1, 1)));
		subHeader.add((new CellExcelVO("MONTO", 1, 1)));
		subHeader.add((new CellExcelVO("%", 1, 1)));
		subHeader.add((new CellExcelVO("MONTO", 1, 1)));
		subHeader.add((new CellExcelVO("%", 1, 1)));
		subHeader.add((new CellExcelVO("MONTO", 1, 1)));
		subHeader.add((new CellExcelVO("%", 1, 1)));

		List<ReporteEstadoSituacionByComunaVO> reporteEstadoSituacionByComunaVO = getReporteEstadoSituacionByComunaAll(Subtitulo.SUBTITULO24);

		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator
				+ "Panilla Reporte Estado Situacion Programa - Comuna.xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());

		GeneradorExcel generadorExcel = new GeneradorExcel(filename);

		ReporteEstadoSituacionPorComunaSheetExcel reporteEstadoSituacionPorComunaSheetExcel = new ReporteEstadoSituacionPorComunaSheetExcel(
				header, subHeader, null);

		reporteEstadoSituacionPorComunaSheetExcel
				.setItems(reporteEstadoSituacionByComunaVO);

		generadorExcel.addSheet(reporteEstadoSituacionPorComunaSheetExcel,
				"Subtitulo 24");

		try {
			BodyVO response = alfrescoService.uploadDocument(
					generadorExcel.saveExcel(), contenType,
					folderReportes.replace("{ANO}", getAnoCurso().toString()));

			TipoDocumento tipoDocumento = new TipoDocumento(
					TipoDocumentosProcesos.REPORTEESTADOSITUACIONPROGRAMABYCOMUNA
							.getId());
			planillaTrabajoId = documentService.createDocumentReportes(
					tipoDocumento, response.getNodeRef(),
					response.getFileName(), contenType, getAnoCurso(),
					Integer.parseInt(getMesCurso(true)));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return planillaTrabajoId;
	}

}
