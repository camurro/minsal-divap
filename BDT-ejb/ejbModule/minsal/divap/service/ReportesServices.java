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
import minsal.divap.dao.ProgramasDAO;
import minsal.divap.dao.RebajaDAO;
import minsal.divap.dao.ReliquidacionDAO;
import minsal.divap.dao.ServicioSaludDAO;
import minsal.divap.enums.Subtitulo;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.excel.GeneradorExcel;
import minsal.divap.excel.impl.ReportePoblacionPercapitaSheetExcel;
import minsal.divap.excel.impl.ReporteRebajaSheetExcel;
import minsal.divap.vo.BodyVO;
import minsal.divap.vo.CellExcelVO;
import minsal.divap.vo.ComponentesVO;
import minsal.divap.vo.ComunaSummaryVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.ReporteGlosaVO;
import minsal.divap.vo.ReporteHistoricoPorProgramaVO;
import minsal.divap.vo.ReporteMarcoPresupuestarioVO;
import minsal.divap.vo.ReportePerCapitaVO;
import minsal.divap.vo.ReporteRebajaVO;
import minsal.divap.vo.ServiciosVO;
import cl.minsal.divap.model.AntecendentesComuna;
import cl.minsal.divap.model.AntecendentesComunaCalculado;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.ComunaCumplimiento;
import cl.minsal.divap.model.ConvenioComuna;
import cl.minsal.divap.model.ConvenioComunaComponente;
import cl.minsal.divap.model.Cuota;
import cl.minsal.divap.model.Establecimiento;
import cl.minsal.divap.model.ProgramaMunicipalCore;
import cl.minsal.divap.model.ProgramaMunicipalCoreComponente;
import cl.minsal.divap.model.Rebaja;
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
	
	@Resource(name = "tmpDir")
	private String tmpDir;
	
	@Resource(name = "folderReportes")
	private String folderReportes;
	
	
	
	
	public List<ReportePerCapitaVO> getReportePercapitaAll(Integer ano) {
		List<ReportePerCapitaVO> listaReportePerCapitaVO = new ArrayList<ReportePerCapitaVO>();

		List<ServiciosVO> serviciosVO = servicioSaludService.getServiciosOrderId();
		for(ServiciosVO servicioVO : serviciosVO){
		
			System.out.println("servicioSalud --> " + servicioVO.getNombre_servicio());

			for (ComunaSummaryVO comuna : servicioVO.getComunas()) {
				ReportePerCapitaVO reportePerCapitaVO = new ReportePerCapitaVO();
				reportePerCapitaVO.setRegion(servicioVO.getRegion().getNombre());
				reportePerCapitaVO.setServicio(servicioVO.getNombre_servicio());
				reportePerCapitaVO.setComuna(comuna.getNombre());

				AntecendentesComunaCalculado antecendentesComunaCalculado = antecedentesComunaDAO.findByComunaAno(comuna.getId(), ano);
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

				Long descuentoIncentivoRetiro = (long) 234234;
				// TODO falta este valor
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

			AntecendentesComunaCalculado antecendentesComunaCalculado = antecedentesComunaDAO.findByComunaAno(comuna.getId(), ano);
			if (antecendentesComunaCalculado == null) {
				continue;
			}

			List<ComunaCumplimiento> comunasCumplimientos = rebajaDAO
					.getCumplimientoPorAnoComuna(ano, comuna.getId());

			if (antecendentesComunaCalculado.getAntecedentesComuna().getClasificacion() != null) {
				reportePerCapitaVO.setClasificacion(antecendentesComunaCalculado.getAntecedentesComuna().getClasificacion().
						getIdTipoComuna().toString());
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

			Long descuentoIncentivoRetiro = (long) 234234;
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

		AntecendentesComuna antecendentesComuna = antecedentesComunaDAO
				.findAntecendentesComunaByComunaServicioAno(
						servicioSalud.getNombre(), comuna.getNombre(), ano);

		AntecendentesComunaCalculado antecendentesComunaCalculado = antecedentesComunaDAO
				.findByIdAntecedentesComuna(antecendentesComuna
						.getIdAntecedentesComuna());
		if (antecendentesComunaCalculado == null) {
			antecendentesComunaCalculado = new AntecendentesComunaCalculado();
		}

		List<ComunaCumplimiento> comunasCumplimientos = rebajaDAO
				.getCumplimientoPorAnoComuna(ano, comuna.getId());

		if (antecendentesComuna.getClasificacion() != null) {
			reportePerCapitaVO.setClasificacion(antecendentesComuna
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
			reportePerCapitaVO.setPercapita(antecendentesComunaCalculado
					.getPercapitaAno());
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

		Long totalRebaja = 0L;

		for (ComunaCumplimiento comunaCumplimiento : comunasCumplimientos) {
			Double porcentajeRebaja = comunaCumplimiento.getRebajaFinal()
					.getRebaja();
			totalRebaja = (long) (totalRebaja + (porcentajeRebaja * reportePerCapitaVO
					.getAporteEstatal()));
		}
		reportePerCapitaVO.setRebajaIAAPS(totalRebaja);

		Long descuentoIncentivoRetiro = (long) 234234;
		// TODO falta este valor
		reportePerCapitaVO
				.setDescuentoIncentivoRetiro(descuentoIncentivoRetiro);
		reportePerCapitaVO.setAporteEstatalFinal(reportePerCapitaVO
				.getAporteEstatal()
				- reportePerCapitaVO.getRebajaIAAPS()
				- reportePerCapitaVO.getDescuentoIncentivoRetiro());

		return reportePerCapitaVO;
	}

	public Integer generarPlanillaPoblacionPercapita(String usuario) {
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
		
		List<ReportePerCapitaVO> reportePerCapita2011 = this.getReportePercapitaAll(getAnoCurso()-3);
		List<ReportePerCapitaVO> reportePerCapita2012 = this.getReportePercapitaAll(getAnoCurso()-2);
		List<ReportePerCapitaVO> reportePerCapita2013 = this.getReportePercapitaAll(getAnoCurso()-1);
		List<ReportePerCapitaVO> reportePerCapita2014 = this.getReportePercapitaAll(getAnoCurso());
		
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator + "Panilla Poblacion per Capita.xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());
		
		GeneradorExcel generadorExcel = new GeneradorExcel(filename);
		
		ReportePoblacionPercapitaSheetExcel reportePoblacionPercapitaSheetExcel2011 = new ReportePoblacionPercapitaSheetExcel(header, null);
		reportePoblacionPercapitaSheetExcel2011.setItems(reportePerCapita2011);
		
		ReportePoblacionPercapitaSheetExcel reportePoblacionPercapitaSheetExcel2012 = new ReportePoblacionPercapitaSheetExcel(header, null);
		reportePoblacionPercapitaSheetExcel2012.setItems(reportePerCapita2012);
		
		ReportePoblacionPercapitaSheetExcel reportePoblacionPercapitaSheetExcel2013 = new ReportePoblacionPercapitaSheetExcel(header, null);
		reportePoblacionPercapitaSheetExcel2013.setItems(reportePerCapita2013);
		
		ReportePoblacionPercapitaSheetExcel reportePoblacionPercapitaSheetExcel2014 = new ReportePoblacionPercapitaSheetExcel(header, null);
		reportePoblacionPercapitaSheetExcel2014.setItems(reportePerCapita2014);
		
		generadorExcel.addSheet(reportePoblacionPercapitaSheetExcel2014, getAnoCurso()+"");
		generadorExcel.addSheet(reportePoblacionPercapitaSheetExcel2013, getAnoCurso()-1+"");
		generadorExcel.addSheet(reportePoblacionPercapitaSheetExcel2012, getAnoCurso()-2+"");
		generadorExcel.addSheet(reportePoblacionPercapitaSheetExcel2011, getAnoCurso()-3+"");
		
		System.out.println("folderPercapita --> "+folderReportes);
		
		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderReportes.replace("{ANO}", getAnoCurso().toString()));
//			System.out.println("response planillaPropuestaEstimacionFlujoCajaConsolidador --->" + response);

			TipoDocumento tipoDocumento = new TipoDocumento(
					TipoDocumentosProcesos.REPORTEPOBLACIONPERCAPITA.getId());
			planillaTrabajoId = documentService
					.createDocumentReportePoblacionPercapita(tipoDocumento, response.getNodeRef(),
							response.getFileName(), contenType, getAnoCurso(), Integer.parseInt(getMesCurso(true)));

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		

		return planillaTrabajoId;
	}
	
	private Integer getAnoCurso() {
		DateFormat formatNowYear = new SimpleDateFormat("yyyy");
		Date nowDate = new Date();
		return Integer.valueOf(formatNowYear.format(nowDate));
	}

	public List<ReporteMarcoPresupuestarioVO> getReporteMarcoPorComuna(Integer idServicio, Subtitulo subtitulo, Integer idComuna, Integer ano, String usuario) {

		List<ReporteMarcoPresupuestarioVO> resultado = new ArrayList<ReporteMarcoPresupuestarioVO>();
		List<ProgramaVO> programasVO = programasService
				.getProgramasBySubtitulo(ano, subtitulo);

		for (ProgramaVO programa : programasVO) {
			ServicioSalud servicio = servicioSaludDAO.getServicioSaludById(idServicio);
			Comuna comuna = comunaService.getComunaById(idComuna);
			System.out.println("servicio.getNombre() --> "+servicio.getNombre()+"  comuna --> "+comuna.getNombre());

			List<ComponentesVO> componentes = programa.getComponentes();
			for (ComponentesVO componenteVO : componentes) {
				// System.out.println("programa --> "+programa+"  --  nombre del componente --> "+componenteVO.getNombre());
				ReporteMarcoPresupuestarioVO reporteMarcoPresupuestarioVO = new ReporteMarcoPresupuestarioVO();
				reporteMarcoPresupuestarioVO.setPrograma(programa.getNombre());
				reporteMarcoPresupuestarioVO.setComponente(componenteVO.getNombre());
				
				AntecendentesComuna antecendentesComuna = antecedentesComunaDAO
						.findAntecendentesComunaByComunaServicioAno(servicio.getNombre(), comuna.getNombre(), getAnoCurso());

				if (antecendentesComuna == null) {
					continue;
				}

				AntecendentesComunaCalculado antecendentesComunaCalculado = antecedentesComunaDAO
						.findByIdAntecedentesComuna(antecendentesComuna
								.getIdAntecedentesComuna());
				if (antecendentesComunaCalculado == null) {
					continue;
				}
				
//				ProgramaMunicipalCore programaMunicipalCore = programasDAO.getProgramaMunicipalCoreByComunaProgramaAno(comuna.getId(), programa.getIdProgramaAno());
				Long tarifa = 0L;
//				if(programaMunicipalCore == null){
//					continue;
//				}
				
//				ProgramaMunicipalCoreComponente programaMunicipalCoreComponente = programasDAO.getByIdProgramaMunicipalCore(programaMunicipalCore.getIdProgramaMunicipalCore());
//				if (programaMunicipalCoreComponente == null) {
//					continue;
//				}
				ConvenioComuna convenioComuna = conveniosDAO.getConvenioComunaByIdComunaIdProgramaAno(comuna.getId(), programa.getIdProgramaAno());
				if(convenioComuna == null){
					continue;
				}
				ConvenioComunaComponente convenioComunaComponente = conveniosDAO.getByIdConvenioComunaIdSubtituloIdComponente(convenioComuna.getIdConvenioComuna(), subtitulo.getId(), componenteVO.getId());
				if(convenioComuna == null){
					continue;
				}
				if(convenioComunaComponente == null){
					continue;
				}
				
				//TODO cambiar esto por el programaMuncipalCoreComponente
				tarifa = (long)convenioComuna.getIdConvenioComuna()*3 + convenioComunaComponente.getMonto();
				

				Long marcoTEMP = antecendentesComunaCalculado.getPercapitaAno() + tarifa; 
				reporteMarcoPresupuestarioVO.setMarco(marcoTEMP);
				
				
				
				
				Long conveniosTEMP = (long)convenioComunaComponente.getMonto();
				reporteMarcoPresupuestarioVO.setConvenios(conveniosTEMP);
				Double porcentajeCuotaTransferida = 0.0;
				
				//TODO cambiar este valor despues
				Long remesasAcumuladasTEMP = (long)(convenioComunaComponente.getMonto()+3);
				if(Integer.parseInt(getMesCurso(true)) > 10){
					porcentajeCuotaTransferida = 1.0;
				}
				else{
					Cuota cuota = reliquidacionDAO.getCuotaByIdProgramaAnoNroCuota(programa.getIdProgramaAno(), (short)1);
					if(cuota.getIdMes().getIdMes() < Integer.parseInt(getMesCurso(true))){
						porcentajeCuotaTransferida = (double)cuota.getPorcentaje()/100;
						reporteMarcoPresupuestarioVO.setPorcentajeCuotaTransferida(porcentajeCuotaTransferida);
					}
					else{
						reporteMarcoPresupuestarioVO.setPorcentajeCuotaTransferida(0.6);
					}
				}
				//TODO cambiar despues
				reporteMarcoPresupuestarioVO.setPorcentajeCuotaTransferida(1.0);
				reporteMarcoPresupuestarioVO.setRemesasAcumuladas(remesasAcumuladasTEMP);
				reporteMarcoPresupuestarioVO.setObservacion("");
				resultado.add(reporteMarcoPresupuestarioVO);
				

			}

		}

		return resultado;
	}
	
	
	public List<ReporteMarcoPresupuestarioVO> getReporteMarcoPorServicio(Integer idServicio, Subtitulo subtitulo, Integer ano, String usuario) {
		List<ReporteMarcoPresupuestarioVO> resultado = new ArrayList<ReporteMarcoPresupuestarioVO>();
		List<ProgramaVO> programasVO = programasService.getProgramasBySubtitulo(ano, subtitulo);
		for (ProgramaVO programa : programasVO) {
			ServicioSalud servicio = servicioSaludDAO.getServicioSaludById(idServicio);
			List<Establecimiento> establecimientos = servicio.getEstablecimientos();

			List<ComponentesVO> componentes = programa.getComponentes();
			for (ComponentesVO componenteVO : componentes) {
				// System.out.println("programa --> "+programa+"  --  nombre del componente --> "+componenteVO.getNombre());
				ReporteMarcoPresupuestarioVO reporteMarcoPresupuestarioVO = new ReporteMarcoPresupuestarioVO();
				reporteMarcoPresupuestarioVO.setPrograma(programa.getNombre());
				reporteMarcoPresupuestarioVO.setComponente(componenteVO.getNombre());
				
//				AntecendentesComuna antecendentesComuna = antecedentesComunaDAO
//						.findAntecendentesComunaByComunaServicioAno(servicio.getNombre(), comuna.getNombre(), 2014);
//
//				if (antecendentesComuna == null) {
//					continue;
//				}
//
//				AntecendentesComunaCalculado antecendentesComunaCalculado = antecedentesComunaDAO
//						.findByIdAntecedentesComuna(antecendentesComuna
//								.getIdAntecedentesComuna());
//				if (antecendentesComunaCalculado == null) {
//					continue;
//				}
//				
////				ProgramaMunicipalCore programaMunicipalCore = programasDAO.getProgramaMunicipalCoreByComunaProgramaAno(comuna.getId(), programa.getIdProgramaAno());
//				Long tarifa = 0L;
////				if(programaMunicipalCore == null){
////					continue;
////				}
//				
////				ProgramaMunicipalCoreComponente programaMunicipalCoreComponente = programasDAO.getByIdProgramaMunicipalCore(programaMunicipalCore.getIdProgramaMunicipalCore());
////				if (programaMunicipalCoreComponente == null) {
////					continue;
////				}
//				ConvenioComuna convenioComuna = conveniosDAO.getConvenioComunaByIdComunaIdProgramaAno(comuna.getId(), programa.getIdProgramaAno());
//				if(convenioComuna == null){
//					continue;
//				}
//				ConvenioComunaComponente convenioComunaComponente = conveniosDAO.getByIdConvenioComunaIdSubtituloIdComponente(convenioComuna.getIdConvenioComuna(), subtitulo.getId(), componenteVO.getId());
//				if(convenioComuna == null){
//					continue;
//				}
//				if(convenioComunaComponente == null){
//					continue;
//				}
//				
//				//TODO cambiar esto por el programaMuncipalCoreComponente
//				tarifa = (long)convenioComuna.getIdConvenioComuna()*3 + convenioComunaComponente.getMonto();
//				
//
//				Long marcoTEMP = antecendentesComunaCalculado.getPercapitaAno() + tarifa; 
//				reporteMarcoPresupuestarioVO.setMarco(marcoTEMP);
//				
//				
//				
//				
//				Long conveniosTEMP = (long)convenioComunaComponente.getMonto();
//				reporteMarcoPresupuestarioVO.setConvenios(conveniosTEMP);
//				Double porcentajeCuotaTransferida = 0.0;
//				
//				//TODO cambiar este valor despues
//				Long remesasAcumuladasTEMP = (long)(convenioComunaComponente.getMonto()+3);
//				if(Integer.parseInt(getMesCurso(true)) > 10){
//					porcentajeCuotaTransferida = 1.0;
//				}
//				else{
//					Cuota cuota = reliquidacionDAO.getCuotaByIdProgramaAnoNroCuota(programa.getIdProgramaAno(), (short)1);
//					if(cuota.getIdMes().getIdMes() < Integer.parseInt(getMesCurso(true))){
//						porcentajeCuotaTransferida = (double)cuota.getPorcentaje()/100;
//						reporteMarcoPresupuestarioVO.setPorcentajeCuotaTransferida(porcentajeCuotaTransferida);
//					}
//					else{
//						reporteMarcoPresupuestarioVO.setPorcentajeCuotaTransferida(0.6);
//					}
//				}
//				//TODO cambiar despues
//				reporteMarcoPresupuestarioVO.setPorcentajeCuotaTransferida(1.0);
//				reporteMarcoPresupuestarioVO.setRemesasAcumuladas(remesasAcumuladasTEMP);
//				reporteMarcoPresupuestarioVO.setObservacion("");
				resultado.add(reporteMarcoPresupuestarioVO);
				

			}

		}
		
		
		
		
		
		return resultado;
	}
	
	
	public String getMesCurso(Boolean numero) {
		SimpleDateFormat dateFormat = null;
		String mesCurso = null;
		if(numero){
			dateFormat = new SimpleDateFormat("MM");
			mesCurso = dateFormat.format(new Date());
		}else{
			dateFormat = new SimpleDateFormat("MMMM");
			mesCurso = dateFormat.format(new Date());
		}
		return mesCurso;
	}

	public List<ReporteGlosaVO> getReporteGlosa() {
		List<ReporteGlosaVO> resultado = new ArrayList<ReporteGlosaVO>();
		List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();

		for (ServicioSalud servicio : servicios) {
			List<Comuna> comunas = servicio.getComunas();
			for (Comuna comuna : comunas) {
				ReporteGlosaVO reporteGlosaVO = new ReporteGlosaVO();
				reporteGlosaVO.setRegion(servicio.getRegion().getNombre());
				reporteGlosaVO.setServicio(servicio.getNombre());
				reporteGlosaVO.setComuna(comuna.getNombre());
				AntecendentesComuna antecendentesComuna = antecedentesComunaDAO
						.findAntecendentesComunaByComunaServicioAno(servicio.getNombre(), comuna.getNombre(),
								2014);

				if (antecendentesComuna == null) {
					continue;
				}

				AntecendentesComunaCalculado antecendentesComunaCalculado = antecedentesComunaDAO
						.findByIdAntecedentesComuna(antecendentesComuna
								.getIdAntecedentesComuna());
				if (antecendentesComunaCalculado == null) {
					continue;
				}

				reporteGlosaVO.setArt49perCapita(antecendentesComunaCalculado
						.getPercapitaAno());

				List<ProgramaMunicipalCore> programasMunicipalesCore = programasDAO
						.getProgramasMunicipalCoreByComuna(comuna.getId());
				Long tarifa = 0L;
				for (ProgramaMunicipalCore programaMunicipalCore : programasMunicipalesCore) {
					ProgramaMunicipalCoreComponente programaMunicipalCoreComponente = programasDAO
							.getByIdProgramaMunicipalCore(programaMunicipalCore
									.getIdProgramaMunicipalCore());
					if (programaMunicipalCoreComponente == null) {
						continue;
					}

					tarifa += programaMunicipalCoreComponente.getTarifa();
				}
				reporteGlosaVO.setArt56reforzamientoMunicipal(tarifa);
				reporteGlosaVO.setTotalRemesasEneroMarzo(0L);
				resultado.add(reporteGlosaVO);
			}
		}

		return resultado;
	}

	
	public List<ReporteGlosaVO> getReporteGlosaPorServicio(Integer idServicio) {
		List<ReporteGlosaVO> resultado = new ArrayList<ReporteGlosaVO>();
		ServicioSalud servicio = servicioSaludDAO
				.getServicioSaludById(idServicio);

		List<Comuna> comunas = servicio.getComunas();
		for (Comuna comuna : comunas) {
			ReporteGlosaVO reporteGlosaVO = new ReporteGlosaVO();
			reporteGlosaVO.setRegion(servicio.getRegion().getNombre());
			reporteGlosaVO.setServicio(servicio.getNombre());
			reporteGlosaVO.setComuna(comuna.getNombre());
			AntecendentesComuna antecendentesComuna = antecedentesComunaDAO
					.findAntecendentesComunaByComunaServicioAno(servicio
							.getRegion().getNombre(), comuna.getNombre(), 2014);

			if (antecendentesComuna == null) {
				continue;
			}

			AntecendentesComunaCalculado antecendentesComunaCalculado = antecedentesComunaDAO
					.findByIdAntecedentesComuna(antecendentesComuna
							.getIdAntecedentesComuna());
			if (antecendentesComunaCalculado == null) {
				continue;
			}

			reporteGlosaVO.setArt49perCapita(antecendentesComunaCalculado
					.getPercapitaAno());

			List<ProgramaMunicipalCore> programasMunicipalesCore = programasDAO
					.getProgramasMunicipalCoreByComuna(comuna.getId());
			Long tarifa = 0L;
			for (ProgramaMunicipalCore programaMunicipalCore : programasMunicipalesCore) {
				ProgramaMunicipalCoreComponente programaMunicipalCoreComponente = programasDAO
						.getByIdProgramaMunicipalCore(programaMunicipalCore
								.getIdProgramaMunicipalCore());
				if (programaMunicipalCoreComponente == null) {
					continue;
				}

				tarifa += programaMunicipalCoreComponente.getTarifa();
			}
			reporteGlosaVO.setArt56reforzamientoMunicipal(tarifa);
			reporteGlosaVO.setTotalRemesasEneroMarzo(0L);
			resultado.add(reporteGlosaVO);
		}

		return resultado;
	}
	
	
	
	public List<ReporteHistoricoPorProgramaVO> getReporteHistoricoPorProgramaVOAll(Integer idProgramaAno, Subtitulo subtitulo){
		System.out.println("idProgramaAno --> "+idProgramaAno+"  subtitulo --> "+subtitulo.getNombre());
		
		
		List<ReporteHistoricoPorProgramaVO> resultado = new ArrayList<ReporteHistoricoPorProgramaVO>();
		ProgramaVO programa = programasService.getProgramaAnoPorID(idProgramaAno);
		List<ServicioSalud> serviciosSalud = servicioSaludDAO.getServiciosOrderId();
		for(ServicioSalud servicio : serviciosSalud){
			System.out.println("servicioVO.getNombre_servicio() --> "+servicio.getNombre());
			//List<Comuna> comunas = servicio.getComunas();
			
			for(Comuna comuna : servicio.getComunas()){
				ReporteHistoricoPorProgramaVO reporteHistoricoPorProgramaVO = new ReporteHistoricoPorProgramaVO();
				
				reporteHistoricoPorProgramaVO.setRegion(servicio.getRegion().getNombre());
				reporteHistoricoPorProgramaVO.setServicio(servicio.getNombre());
				reporteHistoricoPorProgramaVO.setComuna(comuna.getNombre());
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
				
				AntecendentesComuna antecendentesComuna2006 = antecedentesComunaDAO.findAntecendentesComunaByComunaServicioAno(servicio.getNombre(), comuna.getNombre(), 2006);
				if (antecendentesComuna2006 == null) {
					reporteHistoricoPorProgramaVO.setMarco2006(0L);
				}else{
					antecendentesComunaCalculado2006 = antecedentesComunaDAO
							.findByIdAntecedentesComuna(antecendentesComuna2006
									.getIdAntecedentesComuna());
					if (antecendentesComunaCalculado2006 == null) {
						reporteHistoricoPorProgramaVO.setMarco2006(0L);
					}
				}
				
				// ***************** Fin año 2006 *****************************
				
				
				// ********************Año 2007 ****************************
				
				AntecendentesComuna antecendentesComuna2007 = antecedentesComunaDAO.findAntecendentesComunaByComunaServicioAno(servicio.getNombre(), comuna.getNombre(), 2007);
				if (antecendentesComuna2007 == null) {
					reporteHistoricoPorProgramaVO.setMarco2007(0L);
				}else{
					antecendentesComunaCalculado2007 = antecedentesComunaDAO
							.findByIdAntecedentesComuna(antecendentesComuna2007
									.getIdAntecedentesComuna());
					if (antecendentesComunaCalculado2007 == null) {
						reporteHistoricoPorProgramaVO.setMarco2007(0L);
					}
				}
				
				// ***************** Fin año 2007 *****************************
				
				
				// ********************Año 2008 ****************************
				
				AntecendentesComuna antecendentesComuna2008 = antecedentesComunaDAO.findAntecendentesComunaByComunaServicioAno(servicio.getNombre(), comuna.getNombre(), 2008);
				if (antecendentesComuna2008 == null) {
					reporteHistoricoPorProgramaVO.setMarco2008(0L);
				}else{
					antecendentesComunaCalculado2008 = antecedentesComunaDAO
							.findByIdAntecedentesComuna(antecendentesComuna2008
									.getIdAntecedentesComuna());
					if (antecendentesComunaCalculado2008 == null) {
						reporteHistoricoPorProgramaVO.setMarco2008(0L);
					}
				}
				
				// ***************** Fin año 2008 *****************************
				
				
				
				// ********************Año 2009 ****************************
				
				AntecendentesComuna antecendentesComuna2009 = antecedentesComunaDAO.findAntecendentesComunaByComunaServicioAno(servicio.getNombre(), comuna.getNombre(), 2009);
				if (antecendentesComuna2009 == null) {
					reporteHistoricoPorProgramaVO.setMarco2009(0L);
				}else{
					antecendentesComunaCalculado2009 = antecedentesComunaDAO
							.findByIdAntecedentesComuna(antecendentesComuna2009
									.getIdAntecedentesComuna());
					if (antecendentesComunaCalculado2009 == null) {
						reporteHistoricoPorProgramaVO.setMarco2009(0L);
					}
				}
				
				// ***************** Fin año 2009 *****************************
				
				
				
				// ********************Año 2010 ****************************
				
				AntecendentesComuna antecendentesComuna2010 = antecedentesComunaDAO.findAntecendentesComunaByComunaServicioAno(servicio.getNombre(), comuna.getNombre(), 2010);
				if (antecendentesComuna2010 == null) {
					reporteHistoricoPorProgramaVO.setMarco2010(0L);
				}else{
					antecendentesComunaCalculado2010 = antecedentesComunaDAO
							.findByIdAntecedentesComuna(antecendentesComuna2010
									.getIdAntecedentesComuna());
					if (antecendentesComunaCalculado2010 == null) {
						reporteHistoricoPorProgramaVO.setMarco2010(0L);
					}
				}
				
				// ***************** Fin año 2010 *****************************
				
				
				
				// ********************Año 2011 ****************************
				
				AntecendentesComuna antecendentesComuna2011 = antecedentesComunaDAO.findAntecendentesComunaByComunaServicioAno(servicio.getNombre(), comuna.getNombre(), 2011);
				if (antecendentesComuna2011 == null) {
					reporteHistoricoPorProgramaVO.setMarco2011(0L);
				}else{
					antecendentesComunaCalculado2011 = antecedentesComunaDAO
							.findByIdAntecedentesComuna(antecendentesComuna2011
									.getIdAntecedentesComuna());
					if (antecendentesComunaCalculado2011 == null) {
						reporteHistoricoPorProgramaVO.setMarco2011(0L);
					}
				}
				
				// ***************** Fin año 2011 *****************************
				
				
				
				// ********************Año 2012 ****************************
				
				AntecendentesComuna antecendentesComuna2012 = antecedentesComunaDAO.findAntecendentesComunaByComunaServicioAno(servicio.getNombre(), comuna.getNombre(), 2012);
				if (antecendentesComuna2012 == null) {
					reporteHistoricoPorProgramaVO.setMarco2012(0L);
				}else{
					antecendentesComunaCalculado2012 = antecedentesComunaDAO
							.findByIdAntecedentesComuna(antecendentesComuna2012
									.getIdAntecedentesComuna());
					if (antecendentesComunaCalculado2012 == null) {
						reporteHistoricoPorProgramaVO.setMarco2012(0L);
					}
				}
				
				// ***************** Fin año 2012 *****************************
				
				
				
				// ********************Año 2013 ****************************
				
				AntecendentesComuna antecendentesComuna2013 = antecedentesComunaDAO.findAntecendentesComunaByComunaServicioAno(servicio.getNombre(), comuna.getNombre(), 2013);
				if (antecendentesComuna2013 == null) {
					reporteHistoricoPorProgramaVO.setMarco2013(0L);
				}else{
					antecendentesComunaCalculado2013 = antecedentesComunaDAO
							.findByIdAntecedentesComuna(antecendentesComuna2013
									.getIdAntecedentesComuna());
					if (antecendentesComunaCalculado2013 == null) {
						reporteHistoricoPorProgramaVO.setMarco2013(0L);
					}
				}
				
				// ***************** Fin año 2013 *****************************
				
				
				
				// ********************Año 2014 ****************************
				
				AntecendentesComuna antecendentesComuna2014 = antecedentesComunaDAO.findAntecendentesComunaByComunaServicioAno(servicio.getNombre(), comuna.getNombre(), 2014);
				if (antecendentesComuna2014 == null) {
					reporteHistoricoPorProgramaVO.setMarco2014(0L);
				}else{
					antecendentesComunaCalculado2014 = antecedentesComunaDAO
							.findByIdAntecedentesComuna(antecendentesComuna2014
									.getIdAntecedentesComuna());
					if (antecendentesComunaCalculado2014 == null) {
						reporteHistoricoPorProgramaVO.setMarco2014(0L);
					}
				}
				
				// ***************** Fin año 2014 *****************************
				
				
				//TODO cambiar el marco
//				if(antecendentesComunaCalculado2006 == null){
					reporteHistoricoPorProgramaVO.setMarco2006(0L);
//				}else{
//					reporteHistoricoPorProgramaVO.setMarco2006(antecendentesComunaCalculado2006.getPercapitaAno() + antecendentesComunaCalculado2006.getPercapitaMes()*4);
//				}
				
				
				//TODO cambiar el marco
//				if(antecendentesComunaCalculado2007 == null){
					reporteHistoricoPorProgramaVO.setMarco2007(0L);
//				}else{
//					reporteHistoricoPorProgramaVO.setMarco2007(antecendentesComunaCalculado2007.getPercapitaAno() + antecendentesComunaCalculado2007.getPercapitaMes()*3);
//				}
				
				//TODO cambiar el marco
//				if(antecendentesComunaCalculado2008 == null){
					reporteHistoricoPorProgramaVO.setMarco2008(0L);
//				}else{
//					reporteHistoricoPorProgramaVO.setMarco2008(antecendentesComunaCalculado2008.getPercapitaAno() + antecendentesComunaCalculado2008.getPercapitaMes()*6);
//				}
//				
//				//TODO cambiar el marco
//				if(antecendentesComunaCalculado2009 == null){
					reporteHistoricoPorProgramaVO.setMarco2009(0L);
//				}else{
//					reporteHistoricoPorProgramaVO.setMarco2009(antecendentesComunaCalculado2009.getPercapitaAno() + antecendentesComunaCalculado2009.getPercapitaMes()*2);
//				}
//				
//				//TODO cambiar el marco
//				if(antecendentesComunaCalculado2010 == null){
					reporteHistoricoPorProgramaVO.setMarco2010(0L);
//				}else{
//					reporteHistoricoPorProgramaVO.setMarco2010(antecendentesComunaCalculado2010.getPercapitaAno() + antecendentesComunaCalculado2010.getPercapitaMes()*5);
//				}
//				
//				//TODO cambiar el marco
//				if(antecendentesComunaCalculado2011 == null){
					reporteHistoricoPorProgramaVO.setMarco2011(0L);
//				}else{
//					reporteHistoricoPorProgramaVO.setMarco2011(antecendentesComunaCalculado2011.getPercapitaAno() + antecendentesComunaCalculado2011.getPercapitaMes()*8);
//				}
				//TODO cambiar el marco
				if(antecendentesComunaCalculado2012 == null || antecendentesComunaCalculado2012.getPercapitaMes() == null){
					reporteHistoricoPorProgramaVO.setMarco2012(0L);
				}else{
					reporteHistoricoPorProgramaVO.setMarco2012(antecendentesComunaCalculado2012.getPercapitaAno() + antecendentesComunaCalculado2012.getPercapitaMes()*3);
				}
				
				//TODO cambiar el marco
				if(antecendentesComunaCalculado2013 == null || antecendentesComunaCalculado2013.getPercapitaMes() == null){
					reporteHistoricoPorProgramaVO.setMarco2013(0L);
				}else{
					reporteHistoricoPorProgramaVO.setMarco2013(antecendentesComunaCalculado2013.getPercapitaAno() + antecendentesComunaCalculado2013.getPercapitaMes()*4);
				}
				
				//TODO cambiar el marco
				if(antecendentesComunaCalculado2014 == null || antecendentesComunaCalculado2014.getPercapitaMes() == null){
					continue;
				}else{
					System.out.println("antecendentesComunaCalculado2014.getPercapitaAno() --> "+antecendentesComunaCalculado2014.getPercapitaAno());
					System.out.println("antecendentesComunaCalculado2014.getPercapitaMes() --> "+antecendentesComunaCalculado2014.getPercapitaMes());
					
					reporteHistoricoPorProgramaVO.setMarco2014(antecendentesComunaCalculado2014.getPercapitaAno() + antecendentesComunaCalculado2014.getPercapitaMes()*4);
				}
				
				resultado.add(reporteHistoricoPorProgramaVO);
				
				
			}
		}
		
		return resultado;
		
	}
	
	
	public List<ReporteHistoricoPorProgramaVO> getReporteHistoricoPorProgramaVOFiltroServicio(Integer idProgramaAno, Integer idServicio, Subtitulo subtitulo){
		System.out.println("idProgramaAno --> "+idProgramaAno+"  subtitulo --> "+subtitulo.getNombre());
		
		
		List<ReporteHistoricoPorProgramaVO> resultado = new ArrayList<ReporteHistoricoPorProgramaVO>();
		ProgramaVO programa = programasService.getProgramaAnoPorID(idProgramaAno);
		List<ServicioSalud> serviciosSalud = servicioSaludDAO.getServiciosOrderId();
		ServicioSalud servicio = servicioSaludDAO.getById(idServicio);
			
			for(Comuna comuna : servicio.getComunas()){
				ReporteHistoricoPorProgramaVO reporteHistoricoPorProgramaVO = new ReporteHistoricoPorProgramaVO();
				
				reporteHistoricoPorProgramaVO.setRegion(servicio.getRegion().getNombre());
				reporteHistoricoPorProgramaVO.setServicio(servicio.getNombre());
				reporteHistoricoPorProgramaVO.setComuna(comuna.getNombre());
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
				
				AntecendentesComuna antecendentesComuna2006 = antecedentesComunaDAO.findAntecendentesComunaByComunaServicioAno(servicio.getNombre(), comuna.getNombre(), 2006);
				if (antecendentesComuna2006 == null) {
					reporteHistoricoPorProgramaVO.setMarco2006(0L);
				}else{
					antecendentesComunaCalculado2006 = antecedentesComunaDAO
							.findByIdAntecedentesComuna(antecendentesComuna2006
									.getIdAntecedentesComuna());
					if (antecendentesComunaCalculado2006 == null) {
						reporteHistoricoPorProgramaVO.setMarco2006(0L);
					}
				}
				
				// ***************** Fin año 2006 *****************************
				
				
				// ********************Año 2007 ****************************
				
				AntecendentesComuna antecendentesComuna2007 = antecedentesComunaDAO.findAntecendentesComunaByComunaServicioAno(servicio.getNombre(), comuna.getNombre(), 2007);
				if (antecendentesComuna2007 == null) {
					reporteHistoricoPorProgramaVO.setMarco2007(0L);
				}else{
					antecendentesComunaCalculado2007 = antecedentesComunaDAO
							.findByIdAntecedentesComuna(antecendentesComuna2007
									.getIdAntecedentesComuna());
					if (antecendentesComunaCalculado2007 == null) {
						reporteHistoricoPorProgramaVO.setMarco2007(0L);
					}
				}
				
				// ***************** Fin año 2007 *****************************
				
				
				// ********************Año 2008 ****************************
				
				AntecendentesComuna antecendentesComuna2008 = antecedentesComunaDAO.findAntecendentesComunaByComunaServicioAno(servicio.getNombre(), comuna.getNombre(), 2008);
				if (antecendentesComuna2008 == null) {
					reporteHistoricoPorProgramaVO.setMarco2008(0L);
				}else{
					antecendentesComunaCalculado2008 = antecedentesComunaDAO
							.findByIdAntecedentesComuna(antecendentesComuna2008
									.getIdAntecedentesComuna());
					if (antecendentesComunaCalculado2008 == null) {
						reporteHistoricoPorProgramaVO.setMarco2008(0L);
					}
				}
				
				// ***************** Fin año 2008 *****************************
				
				
				
				// ********************Año 2009 ****************************
				
				AntecendentesComuna antecendentesComuna2009 = antecedentesComunaDAO.findAntecendentesComunaByComunaServicioAno(servicio.getNombre(), comuna.getNombre(), 2009);
				if (antecendentesComuna2009 == null) {
					reporteHistoricoPorProgramaVO.setMarco2009(0L);
				}else{
					antecendentesComunaCalculado2009 = antecedentesComunaDAO
							.findByIdAntecedentesComuna(antecendentesComuna2009
									.getIdAntecedentesComuna());
					if (antecendentesComunaCalculado2009 == null) {
						reporteHistoricoPorProgramaVO.setMarco2009(0L);
					}
				}
				
				// ***************** Fin año 2009 *****************************
				
				
				
				// ********************Año 2010 ****************************
				
				AntecendentesComuna antecendentesComuna2010 = antecedentesComunaDAO.findAntecendentesComunaByComunaServicioAno(servicio.getNombre(), comuna.getNombre(), 2010);
				if (antecendentesComuna2010 == null) {
					reporteHistoricoPorProgramaVO.setMarco2010(0L);
				}else{
					antecendentesComunaCalculado2010 = antecedentesComunaDAO
							.findByIdAntecedentesComuna(antecendentesComuna2010
									.getIdAntecedentesComuna());
					if (antecendentesComunaCalculado2010 == null) {
						reporteHistoricoPorProgramaVO.setMarco2010(0L);
					}
				}
				
				// ***************** Fin año 2010 *****************************
				
				
				
				// ********************Año 2011 ****************************
				
				AntecendentesComuna antecendentesComuna2011 = antecedentesComunaDAO.findAntecendentesComunaByComunaServicioAno(servicio.getNombre(), comuna.getNombre(), 2011);
				if (antecendentesComuna2011 == null) {
					reporteHistoricoPorProgramaVO.setMarco2011(0L);
				}else{
					antecendentesComunaCalculado2011 = antecedentesComunaDAO
							.findByIdAntecedentesComuna(antecendentesComuna2011
									.getIdAntecedentesComuna());
					if (antecendentesComunaCalculado2011 == null) {
						reporteHistoricoPorProgramaVO.setMarco2011(0L);
					}
				}
				
				// ***************** Fin año 2011 *****************************
				
				
				
				// ********************Año 2012 ****************************
				
				AntecendentesComuna antecendentesComuna2012 = antecedentesComunaDAO.findAntecendentesComunaByComunaServicioAno(servicio.getNombre(), comuna.getNombre(), 2012);
				if (antecendentesComuna2012 == null) {
					reporteHistoricoPorProgramaVO.setMarco2012(0L);
				}else{
					antecendentesComunaCalculado2012 = antecedentesComunaDAO
							.findByIdAntecedentesComuna(antecendentesComuna2012
									.getIdAntecedentesComuna());
					if (antecendentesComunaCalculado2012 == null) {
						reporteHistoricoPorProgramaVO.setMarco2012(0L);
					}
				}
				
				// ***************** Fin año 2012 *****************************
				
				
				
				// ********************Año 2013 ****************************
				
				AntecendentesComuna antecendentesComuna2013 = antecedentesComunaDAO.findAntecendentesComunaByComunaServicioAno(servicio.getNombre(), comuna.getNombre(), 2013);
				if (antecendentesComuna2013 == null) {
					reporteHistoricoPorProgramaVO.setMarco2013(0L);
				}else{
					antecendentesComunaCalculado2013 = antecedentesComunaDAO
							.findByIdAntecedentesComuna(antecendentesComuna2013
									.getIdAntecedentesComuna());
					if (antecendentesComunaCalculado2013 == null) {
						reporteHistoricoPorProgramaVO.setMarco2013(0L);
					}
				}
				
				// ***************** Fin año 2013 *****************************
				
				
				
				// ********************Año 2014 ****************************
				
				AntecendentesComuna antecendentesComuna2014 = antecedentesComunaDAO.findAntecendentesComunaByComunaServicioAno(servicio.getNombre(), comuna.getNombre(), 2014);
				if (antecendentesComuna2014 == null) {
					reporteHistoricoPorProgramaVO.setMarco2014(0L);
				}else{
					antecendentesComunaCalculado2014 = antecedentesComunaDAO
							.findByIdAntecedentesComuna(antecendentesComuna2014
									.getIdAntecedentesComuna());
					if (antecendentesComunaCalculado2014 == null) {
						reporteHistoricoPorProgramaVO.setMarco2014(0L);
					}
				}
				
				// ***************** Fin año 2014 *****************************
				
				
				//TODO cambiar el marco
//				if(antecendentesComunaCalculado2006 == null){
					reporteHistoricoPorProgramaVO.setMarco2006(0L);
//				}else{
//					reporteHistoricoPorProgramaVO.setMarco2006(antecendentesComunaCalculado2006.getPercapitaAno() + antecendentesComunaCalculado2006.getPercapitaMes()*4);
//				}
				
				
				//TODO cambiar el marco
//				if(antecendentesComunaCalculado2007 == null){
					reporteHistoricoPorProgramaVO.setMarco2007(0L);
//				}else{
//					reporteHistoricoPorProgramaVO.setMarco2007(antecendentesComunaCalculado2007.getPercapitaAno() + antecendentesComunaCalculado2007.getPercapitaMes()*3);
//				}
				
				//TODO cambiar el marco
//				if(antecendentesComunaCalculado2008 == null){
					reporteHistoricoPorProgramaVO.setMarco2008(0L);
//				}else{
//					reporteHistoricoPorProgramaVO.setMarco2008(antecendentesComunaCalculado2008.getPercapitaAno() + antecendentesComunaCalculado2008.getPercapitaMes()*6);
//				}
//				
//				//TODO cambiar el marco
//				if(antecendentesComunaCalculado2009 == null){
					reporteHistoricoPorProgramaVO.setMarco2009(0L);
//				}else{
//					reporteHistoricoPorProgramaVO.setMarco2009(antecendentesComunaCalculado2009.getPercapitaAno() + antecendentesComunaCalculado2009.getPercapitaMes()*2);
//				}
//				
//				//TODO cambiar el marco
//				if(antecendentesComunaCalculado2010 == null){
					reporteHistoricoPorProgramaVO.setMarco2010(0L);
//				}else{
//					reporteHistoricoPorProgramaVO.setMarco2010(antecendentesComunaCalculado2010.getPercapitaAno() + antecendentesComunaCalculado2010.getPercapitaMes()*5);
//				}
//				
//				//TODO cambiar el marco
//				if(antecendentesComunaCalculado2011 == null){
					reporteHistoricoPorProgramaVO.setMarco2011(0L);
//				}else{
//					reporteHistoricoPorProgramaVO.setMarco2011(antecendentesComunaCalculado2011.getPercapitaAno() + antecendentesComunaCalculado2011.getPercapitaMes()*8);
//				}
				//TODO cambiar el marco
				if(antecendentesComunaCalculado2012 == null || antecendentesComunaCalculado2012.getPercapitaMes() == null){
					reporteHistoricoPorProgramaVO.setMarco2012(0L);
				}else{
					reporteHistoricoPorProgramaVO.setMarco2012(antecendentesComunaCalculado2012.getPercapitaAno() + antecendentesComunaCalculado2012.getPercapitaMes()*3);
				}
				
				//TODO cambiar el marco
				if(antecendentesComunaCalculado2013 == null || antecendentesComunaCalculado2013.getPercapitaMes() == null){
					reporteHistoricoPorProgramaVO.setMarco2013(0L);
				}else{
					reporteHistoricoPorProgramaVO.setMarco2013(antecendentesComunaCalculado2013.getPercapitaAno() + antecendentesComunaCalculado2013.getPercapitaMes()*4);
				}
				
				//TODO cambiar el marco
				if(antecendentesComunaCalculado2014 == null || antecendentesComunaCalculado2014.getPercapitaMes() == null){
					continue;
				}else{
					System.out.println("antecendentesComunaCalculado2014.getPercapitaAno() --> "+antecendentesComunaCalculado2014.getPercapitaAno());
					System.out.println("antecendentesComunaCalculado2014.getPercapitaMes() --> "+antecendentesComunaCalculado2014.getPercapitaMes());
					
					reporteHistoricoPorProgramaVO.setMarco2014(antecendentesComunaCalculado2014.getPercapitaAno() + antecendentesComunaCalculado2014.getPercapitaMes()*4);
				}
				
				resultado.add(reporteHistoricoPorProgramaVO);
				
				
			}
		
		
		return resultado;
		
	}
	
	public List<ReporteHistoricoPorProgramaVO> getReporteHistoricoPorProgramaVOFiltroServicioComuna(Integer idProgramaAno, Integer idServicio, Integer idComuna, Subtitulo subtitulo){
		System.out.println("idProgramaAno --> "+idProgramaAno+"  subtitulo --> "+subtitulo.getNombre());
		
		
		List<ReporteHistoricoPorProgramaVO> resultado = new ArrayList<ReporteHistoricoPorProgramaVO>();
		ProgramaVO programa = programasService.getProgramaAnoPorID(idProgramaAno);
		List<ServicioSalud> serviciosSalud = servicioSaludDAO.getServiciosOrderId();
		ServicioSalud servicio = servicioSaludDAO.getById(idServicio);
			Comuna comuna = comunaDAO.getComunaById(idComuna);
			
				ReporteHistoricoPorProgramaVO reporteHistoricoPorProgramaVO = new ReporteHistoricoPorProgramaVO();
				
				reporteHistoricoPorProgramaVO.setRegion(servicio.getRegion().getNombre());
				reporteHistoricoPorProgramaVO.setServicio(servicio.getNombre());
				reporteHistoricoPorProgramaVO.setComuna(comuna.getNombre());
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
				
				AntecendentesComuna antecendentesComuna2006 = antecedentesComunaDAO.findAntecendentesComunaByComunaServicioAno(servicio.getNombre(), comuna.getNombre(), 2006);
				if (antecendentesComuna2006 == null) {
					reporteHistoricoPorProgramaVO.setMarco2006(0L);
				}else{
					antecendentesComunaCalculado2006 = antecedentesComunaDAO
							.findByIdAntecedentesComuna(antecendentesComuna2006
									.getIdAntecedentesComuna());
					if (antecendentesComunaCalculado2006 == null) {
						reporteHistoricoPorProgramaVO.setMarco2006(0L);
					}
				}
				
				// ***************** Fin año 2006 *****************************
				
				
				// ********************Año 2007 ****************************
				
				AntecendentesComuna antecendentesComuna2007 = antecedentesComunaDAO.findAntecendentesComunaByComunaServicioAno(servicio.getNombre(), comuna.getNombre(), 2007);
				if (antecendentesComuna2007 == null) {
					reporteHistoricoPorProgramaVO.setMarco2007(0L);
				}else{
					antecendentesComunaCalculado2007 = antecedentesComunaDAO
							.findByIdAntecedentesComuna(antecendentesComuna2007
									.getIdAntecedentesComuna());
					if (antecendentesComunaCalculado2007 == null) {
						reporteHistoricoPorProgramaVO.setMarco2007(0L);
					}
				}
				
				// ***************** Fin año 2007 *****************************
				
				
				// ********************Año 2008 ****************************
				
				AntecendentesComuna antecendentesComuna2008 = antecedentesComunaDAO.findAntecendentesComunaByComunaServicioAno(servicio.getNombre(), comuna.getNombre(), 2008);
				if (antecendentesComuna2008 == null) {
					reporteHistoricoPorProgramaVO.setMarco2008(0L);
				}else{
					antecendentesComunaCalculado2008 = antecedentesComunaDAO
							.findByIdAntecedentesComuna(antecendentesComuna2008
									.getIdAntecedentesComuna());
					if (antecendentesComunaCalculado2008 == null) {
						reporteHistoricoPorProgramaVO.setMarco2008(0L);
					}
				}
				
				// ***************** Fin año 2008 *****************************
				
				
				
				// ********************Año 2009 ****************************
				
				AntecendentesComuna antecendentesComuna2009 = antecedentesComunaDAO.findAntecendentesComunaByComunaServicioAno(servicio.getNombre(), comuna.getNombre(), 2009);
				if (antecendentesComuna2009 == null) {
					reporteHistoricoPorProgramaVO.setMarco2009(0L);
				}else{
					antecendentesComunaCalculado2009 = antecedentesComunaDAO
							.findByIdAntecedentesComuna(antecendentesComuna2009
									.getIdAntecedentesComuna());
					if (antecendentesComunaCalculado2009 == null) {
						reporteHistoricoPorProgramaVO.setMarco2009(0L);
					}
				}
				
				// ***************** Fin año 2009 *****************************
				
				
				
				// ********************Año 2010 ****************************
				
				AntecendentesComuna antecendentesComuna2010 = antecedentesComunaDAO.findAntecendentesComunaByComunaServicioAno(servicio.getNombre(), comuna.getNombre(), 2010);
				if (antecendentesComuna2010 == null) {
					reporteHistoricoPorProgramaVO.setMarco2010(0L);
				}else{
					antecendentesComunaCalculado2010 = antecedentesComunaDAO
							.findByIdAntecedentesComuna(antecendentesComuna2010
									.getIdAntecedentesComuna());
					if (antecendentesComunaCalculado2010 == null) {
						reporteHistoricoPorProgramaVO.setMarco2010(0L);
					}
				}
				
				// ***************** Fin año 2010 *****************************
				
				
				
				// ********************Año 2011 ****************************
				
				AntecendentesComuna antecendentesComuna2011 = antecedentesComunaDAO.findAntecendentesComunaByComunaServicioAno(servicio.getNombre(), comuna.getNombre(), 2011);
				if (antecendentesComuna2011 == null) {
					reporteHistoricoPorProgramaVO.setMarco2011(0L);
				}else{
					antecendentesComunaCalculado2011 = antecedentesComunaDAO
							.findByIdAntecedentesComuna(antecendentesComuna2011
									.getIdAntecedentesComuna());
					if (antecendentesComunaCalculado2011 == null) {
						reporteHistoricoPorProgramaVO.setMarco2011(0L);
					}
				}
				
				// ***************** Fin año 2011 *****************************
				
				
				
				// ********************Año 2012 ****************************
				
				AntecendentesComuna antecendentesComuna2012 = antecedentesComunaDAO.findAntecendentesComunaByComunaServicioAno(servicio.getNombre(), comuna.getNombre(), 2012);
				if (antecendentesComuna2012 == null) {
					reporteHistoricoPorProgramaVO.setMarco2012(0L);
				}else{
					antecendentesComunaCalculado2012 = antecedentesComunaDAO
							.findByIdAntecedentesComuna(antecendentesComuna2012
									.getIdAntecedentesComuna());
					if (antecendentesComunaCalculado2012 == null) {
						reporteHistoricoPorProgramaVO.setMarco2012(0L);
					}
				}
				
				// ***************** Fin año 2012 *****************************
				
				
				
				// ********************Año 2013 ****************************
				
				AntecendentesComuna antecendentesComuna2013 = antecedentesComunaDAO.findAntecendentesComunaByComunaServicioAno(servicio.getNombre(), comuna.getNombre(), 2013);
				if (antecendentesComuna2013 == null) {
					reporteHistoricoPorProgramaVO.setMarco2013(0L);
				}else{
					antecendentesComunaCalculado2013 = antecedentesComunaDAO
							.findByIdAntecedentesComuna(antecendentesComuna2013
									.getIdAntecedentesComuna());
					if (antecendentesComunaCalculado2013 == null) {
						reporteHistoricoPorProgramaVO.setMarco2013(0L);
					}
				}
				
				// ***************** Fin año 2013 *****************************
				
				
				
				// ********************Año 2014 ****************************
				
				AntecendentesComuna antecendentesComuna2014 = antecedentesComunaDAO.findAntecendentesComunaByComunaServicioAno(servicio.getNombre(), comuna.getNombre(), 2014);
				if (antecendentesComuna2014 == null) {
					reporteHistoricoPorProgramaVO.setMarco2014(0L);
				}else{
					antecendentesComunaCalculado2014 = antecedentesComunaDAO
							.findByIdAntecedentesComuna(antecendentesComuna2014
									.getIdAntecedentesComuna());
					if (antecendentesComunaCalculado2014 == null) {
						reporteHistoricoPorProgramaVO.setMarco2014(0L);
					}
				}
				
				// ***************** Fin año 2014 *****************************
				
				
				//TODO cambiar el marco
//				if(antecendentesComunaCalculado2006 == null){
					reporteHistoricoPorProgramaVO.setMarco2006(0L);
//				}else{
//					reporteHistoricoPorProgramaVO.setMarco2006(antecendentesComunaCalculado2006.getPercapitaAno() + antecendentesComunaCalculado2006.getPercapitaMes()*4);
//				}
				
				
				//TODO cambiar el marco
//				if(antecendentesComunaCalculado2007 == null){
					reporteHistoricoPorProgramaVO.setMarco2007(0L);
//				}else{
//					reporteHistoricoPorProgramaVO.setMarco2007(antecendentesComunaCalculado2007.getPercapitaAno() + antecendentesComunaCalculado2007.getPercapitaMes()*3);
//				}
				
				//TODO cambiar el marco
//				if(antecendentesComunaCalculado2008 == null){
					reporteHistoricoPorProgramaVO.setMarco2008(0L);
//				}else{
//					reporteHistoricoPorProgramaVO.setMarco2008(antecendentesComunaCalculado2008.getPercapitaAno() + antecendentesComunaCalculado2008.getPercapitaMes()*6);
//				}
//				
//				//TODO cambiar el marco
//				if(antecendentesComunaCalculado2009 == null){
					reporteHistoricoPorProgramaVO.setMarco2009(0L);
//				}else{
//					reporteHistoricoPorProgramaVO.setMarco2009(antecendentesComunaCalculado2009.getPercapitaAno() + antecendentesComunaCalculado2009.getPercapitaMes()*2);
//				}
//				
//				//TODO cambiar el marco
//				if(antecendentesComunaCalculado2010 == null){
					reporteHistoricoPorProgramaVO.setMarco2010(0L);
//				}else{
//					reporteHistoricoPorProgramaVO.setMarco2010(antecendentesComunaCalculado2010.getPercapitaAno() + antecendentesComunaCalculado2010.getPercapitaMes()*5);
//				}
//				
//				//TODO cambiar el marco
//				if(antecendentesComunaCalculado2011 == null){
					reporteHistoricoPorProgramaVO.setMarco2011(0L);
//				}else{
//					reporteHistoricoPorProgramaVO.setMarco2011(antecendentesComunaCalculado2011.getPercapitaAno() + antecendentesComunaCalculado2011.getPercapitaMes()*8);
//				}
				//TODO cambiar el marco
				if(antecendentesComunaCalculado2012 == null || antecendentesComunaCalculado2012.getPercapitaMes() == null){
					reporteHistoricoPorProgramaVO.setMarco2012(0L);
				}else{
					reporteHistoricoPorProgramaVO.setMarco2012(antecendentesComunaCalculado2012.getPercapitaAno() + antecendentesComunaCalculado2012.getPercapitaMes()*3);
				}
				
				//TODO cambiar el marco
				if(antecendentesComunaCalculado2013 == null || antecendentesComunaCalculado2013.getPercapitaMes() == null){
					reporteHistoricoPorProgramaVO.setMarco2013(0L);
				}else{
					reporteHistoricoPorProgramaVO.setMarco2013(antecendentesComunaCalculado2013.getPercapitaAno() + antecendentesComunaCalculado2013.getPercapitaMes()*4);
				}
				
				//TODO cambiar el marco
				if(antecendentesComunaCalculado2014 == null || antecendentesComunaCalculado2014.getPercapitaMes() == null){
					reporteHistoricoPorProgramaVO.setMarco2014(0L);
				}else{
					System.out.println("antecendentesComunaCalculado2014.getPercapitaAno() --> "+antecendentesComunaCalculado2014.getPercapitaAno());
					System.out.println("antecendentesComunaCalculado2014.getPercapitaMes() --> "+antecendentesComunaCalculado2014.getPercapitaMes());
					
					reporteHistoricoPorProgramaVO.setMarco2014(antecendentesComunaCalculado2014.getPercapitaAno() + antecendentesComunaCalculado2014.getPercapitaMes()*4);
				}
				
				resultado.add(reporteHistoricoPorProgramaVO);
		
		return resultado;
		
	}

	public Integer getDocumentByTypeAnoActual(TipoDocumentosProcesos tipoDocumentoProceso){
		System.out.println("tipoDocumentoProceso --> "+tipoDocumentoProceso.getName());
		System.out.println("getAnoCurso() --> "+getAnoCurso());
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO =  documentService.getDocumentByTypeAnoReportes(tipoDocumentoProceso, getAnoCurso());
		if(referenciaDocumentoSummaryVO == null){
			return null;
		}
		else{
			return referenciaDocumentoSummaryVO.getId();
		}
	}
	
	public List<ReporteRebajaVO> getReporteRebaja(){
		List<ReporteRebajaVO> resultado = new ArrayList<ReporteRebajaVO>();
		List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
		for(ServicioSalud servicioSalud : servicios){
			
			List<Comuna> comunas = servicioSalud.getComunas();
			for(Comuna comuna : comunas){
				ReporteRebajaVO reporteRebajaVO = new ReporteRebajaVO();
				reporteRebajaVO.setRegion(servicioSalud.getRegion().getNombre());
				reporteRebajaVO.setServicio(servicioSalud.getNombre());
				
				Rebaja rebajaCorte1 = rebajaDAO.getRebajaByRebajaCorteAno(1, 2014);
				if(rebajaCorte1 == null){
					rebajaCorte1 = new Rebaja();
				}
				
				Rebaja rebajaCorte2 = rebajaDAO.getRebajaByRebajaCorteAno(2, 2014);
				if(rebajaCorte2 == null){
					rebajaCorte2 = new Rebaja();
				}
				
				Rebaja rebajaCorte3 = rebajaDAO.getRebajaByRebajaCorteAno(3, 2014);
				if(rebajaCorte3 == null){
					rebajaCorte3 = new Rebaja();
				}
				
				Rebaja rebajaCorte4 = rebajaDAO.getRebajaByRebajaCorteAno(4, 2014);
				if(rebajaCorte4 == null){
					rebajaCorte4 = new Rebaja();
				}
				
				
				reporteRebajaVO.setComuna(comuna.getNombre());
				AntecendentesComuna antecendentesComuna = antecedentesComunaDAO
						.findAntecendentesComunaByComunaServicioAno(
								servicioSalud.getNombre(), comuna.getNombre(), 2014);
				
				if(antecendentesComuna == null){
					continue;
				}

				AntecendentesComunaCalculado antecendentesComunaCalculado = antecedentesComunaDAO
						.findByIdAntecedentesComuna(antecendentesComuna
								.getIdAntecedentesComuna());
				if (antecendentesComunaCalculado == null) {
					continue;
				}
				
//				List<ComunaCumplimiento> comunasCumplimientosCorte1 = rebajaDAO.getCumplimientoByRebejaComuna(rebajaCorte1.getIdRebaja(), comuna.getId());
//				List<ComunaCumplimiento> comunasCumplimientosCorte2 = rebajaDAO.getCumplimientoByRebejaComuna(rebajaCorte2.getIdRebaja(), comuna.getId());
//				List<ComunaCumplimiento> comunasCumplimientosCorte3 = rebajaDAO.getCumplimientoByRebejaComuna(rebajaCorte3.getIdRebaja(), comuna.getId());
//				List<ComunaCumplimiento> comunasCumplimientosCorte4 = rebajaDAO.getCumplimientoByRebejaComuna(rebajaCorte4.getIdRebaja(), comuna.getId());
				
				//TODO recorrer lista y obtener datos
				
				
//				for(ComunaCumplimiento comunaCumplimientoCorte1 : comunasCumplimientosCorte1){
//					reporteRebajaVO.setfCorte1Monto(comunaCumplimientoCorte1.ge) 
//				}
				
				if(antecendentesComunaCalculado.getPercapitaAno() == null){
					antecendentesComunaCalculado.setPercapitaAno(0L);
				}
				//TODO estos valores no son
				reporteRebajaVO.setfCorte1Monto(antecendentesComunaCalculado.getPercapitaAno());
				reporteRebajaVO.setfCorte1Porcentaje(antecendentesComunaCalculado.getValorPerCapitaComunalMes());
				reporteRebajaVO.setfCorte2Monto(antecendentesComunaCalculado.getPercapitaAno());
				reporteRebajaVO.setfCorte2Porcentaje(antecendentesComunaCalculado.getValorPerCapitaComunalMes());
				reporteRebajaVO.setfCorte3Monto(antecendentesComunaCalculado.getPercapitaAno());
				reporteRebajaVO.setfCorte3Porcentaje(antecendentesComunaCalculado.getValorPerCapitaComunalMes());
				reporteRebajaVO.setfCorte4Monto(antecendentesComunaCalculado.getPercapitaAno());
				reporteRebajaVO.setfCorte4Porcentaje(antecendentesComunaCalculado.getValorPerCapitaComunalMes());
				reporteRebajaVO.setRebajaTotal((long)99999999);
				
				
				resultado.add(reporteRebajaVO);
			}
		}
		return resultado;
	}
	
	public Integer generarPlanillaReporteRebaja(String usuario) {
		Integer planillaTrabajoId = null;
		List<ServiciosVO> servicios = servicioSaludService.getServiciosOrderId();
		List<CellExcelVO> header = new ArrayList<CellExcelVO>();
		List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
		header.add((new CellExcelVO("REGION", 1, 2)));
		header.add((new CellExcelVO("SERVICIO", 1, 2)));
		header.add((new CellExcelVO("COMUNA", 1, 2)));
		header.add((new CellExcelVO("REBAJA FECHA CORTE 1", 2, 1)));
		header.add((new CellExcelVO("REBAJA FECHA CORTE 2", 2, 1)));
		header.add((new CellExcelVO("REBAJA FECHA CORTE 3", 2, 1)));
		header.add((new CellExcelVO("REBAJA FECHA CORTE 4", 2, 1)));
		header.add((new CellExcelVO("REBAJA APLICADA", 1, 2)));
		
		subHeader.add((new CellExcelVO("MONTO", 1, 1)));
		subHeader.add((new CellExcelVO("%", 1, 1)));
		subHeader.add((new CellExcelVO("MONTO", 1, 1)));
		subHeader.add((new CellExcelVO("%", 1, 1)));
		subHeader.add((new CellExcelVO("MONTO", 1, 1)));
		subHeader.add((new CellExcelVO("%", 1, 1)));
		subHeader.add((new CellExcelVO("MONTO", 1, 1)));
		subHeader.add((new CellExcelVO("%", 1, 1)));
		
		
		List<ReporteRebajaVO> reporteRebajaVO = this.getReporteRebaja();
		
		
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator + "Panilla Reporte Rebaja.xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());
		
		GeneradorExcel generadorExcel = new GeneradorExcel(filename);
		
		ReporteRebajaSheetExcel reporteRebajaSheetExcel = new ReporteRebajaSheetExcel(header, subHeader, null);
		reporteRebajaSheetExcel.setItems(reporteRebajaVO);
		
		generadorExcel.addSheet(reporteRebajaSheetExcel, "rebaja");
		
		
		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderReportes.replace("{ANO}", getAnoCurso().toString()));
//			System.out.println("response planillaPropuestaEstimacionFlujoCajaConsolidador --->" + response);

			TipoDocumento tipoDocumento = new TipoDocumento(
					TipoDocumentosProcesos.REPORTEREBAJA.getId());
			planillaTrabajoId = documentService
					.createDocumentReporteRebaja(tipoDocumento, response.getNodeRef(),
							response.getFileName(), contenType, getAnoCurso(), Integer.parseInt(getMesCurso(true)));

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		

		return planillaTrabajoId;
	}

}
