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
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import minsal.divap.dao.AntecedentesComunaDAO;
import minsal.divap.dao.CajaDAO;
import minsal.divap.dao.ComponenteDAO;
import minsal.divap.dao.ConveniosDAO;
import minsal.divap.dao.DistribucionInicialPercapitaDAO;
import minsal.divap.dao.DocumentDAO;
import minsal.divap.dao.EstimacionFlujoCajaDAO;
import minsal.divap.dao.MesDAO;
import minsal.divap.dao.ProgramasDAO;
import minsal.divap.dao.RemesasDAO;
import minsal.divap.dao.SeguimientoDAO;
import minsal.divap.dao.ServicioSaludDAO;
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.doc.GeneradorResolucionAporteEstatal;
import minsal.divap.doc.GeneradorWord;
import minsal.divap.enums.EstadosProgramas;
import minsal.divap.enums.Subtitulo;
import minsal.divap.enums.TareasSeguimiento;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.excel.GeneradorExcel;
import minsal.divap.excel.impl.EstimacionFlujoCajaConsolidadorSheetExcel;
import minsal.divap.excel.impl.EstimacionFlujoCajaSheetExcel;
import minsal.divap.excel.impl.EstimacionFlujoCajaSubtituloSheetExcel;
import minsal.divap.model.mappers.ModificacionPercapitaReferenciaDocumentoMapper;
import minsal.divap.model.mappers.ReferenciaDocumentoMapper;
import minsal.divap.model.mappers.ServicioMapper;
import minsal.divap.model.mappers.SubtituloFlujoCajaMapper;
import minsal.divap.util.Util;
import minsal.divap.vo.AsignacionDistribucionPerCapitaVO;
import minsal.divap.vo.BaseVO;
import minsal.divap.vo.BodyVO;
import minsal.divap.vo.CajaMontoSummaryVO;
import minsal.divap.vo.CellExcelVO;
import minsal.divap.vo.ConveniosSummaryVO;
import minsal.divap.vo.DocumentoVO;
import minsal.divap.vo.EmailVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.ReferenciaDocumentoVO;
import minsal.divap.vo.ResumenConsolidadorVO;
import minsal.divap.vo.SeguimientoVO;
import minsal.divap.vo.ServiciosVO;
import minsal.divap.vo.SubtituloFlujoCajaVO;
import minsal.divap.vo.TransferenciaSummaryVO;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.docx4j.openpackaging.exceptions.Docx4JException;

import cl.minsal.divap.model.AnoEnCurso;
import cl.minsal.divap.model.AntecendentesComunaCalculado;
import cl.minsal.divap.model.Caja;
import cl.minsal.divap.model.CajaMonto;
import cl.minsal.divap.model.CajaMontoPK;
import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.ComponenteSubtitulo;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.ConvenioComuna;
import cl.minsal.divap.model.Cuota;
import cl.minsal.divap.model.DetalleRemesas;
import cl.minsal.divap.model.DistribucionInicialPercapita;
import cl.minsal.divap.model.DocumentoEstimacionflujocaja;
import cl.minsal.divap.model.DocumentoModificacionPercapita;
import cl.minsal.divap.model.Establecimiento;
import cl.minsal.divap.model.EstadoPrograma;
import cl.minsal.divap.model.MarcoPresupuestario;
import cl.minsal.divap.model.Mes;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.ReferenciaDocumento;
import cl.minsal.divap.model.Remesa;
import cl.minsal.divap.model.Seguimiento;
import cl.minsal.divap.model.ServicioSalud;
import cl.minsal.divap.model.TipoDocumento;
import cl.minsal.divap.model.TipoSubtitulo;

@Stateless
@LocalBean
public class EstimacionFlujoCajaService {
	@EJB
	private EstimacionFlujoCajaDAO estimacionFlujoCajaDAO;
	@EJB
	private DocumentDAO fileDAO;
	@EJB
	private ComponenteDAO componenteDAO;
	@EJB
	private AlfrescoService alfrescoService;
	@EJB
	private DocumentService documentService;
	@EJB
	private ServicioSaludService servicioSaludService;
	@EJB
	private ProgramasService programasService;
	@EJB
	private ProcessService processService; 

	@Resource(name = "tmpDir")
	private String tmpDir;

	@Resource(name = "tmpDirDoc")
	private String tmpDirDoc;

	@Resource(name = "folderEstimacionFlujoCaja")
	private String folderEstimacionFlujoCaja;

	@Resource(name = "folderTemplateEstimacionFlujoCaja")
	private String folderTemplateEstimacionFlujoCaja;

	@Resource(name = "folderDocEstimacionFlujoCaja")
	private String folderDocEstimacionFlujoCaja;

	@Resource(name = "folderTemplatePercapita")
	private String folderTemplatePercapita;

	@Resource(name = "folderDocReliquidacion")
	private String folderDocReliquidacion;


	@EJB
	private UsuarioDAO usuarioDAO;
	@EJB
	private SeguimientoService seguimientoService;
	@EJB
	private SeguimientoDAO seguimientoDAO;
	@EJB
	private DocumentDAO documentDAO;
	@EJB
	private AntecedentesComunaDAO antecedentesComunaDAO;
	@EJB
	private DistribucionInicialPercapitaDAO distribucionInicialPercapitaDAO;
	@EJB
	private CajaDAO cajaDAO;
	@EJB
	private MesDAO mesDAO;
	@EJB
	private ProgramasDAO programasDAO;
	@EJB
	private EmailService emailService;
	@EJB
	private ConveniosDAO conveniosDAO;
	@EJB
	private RemesasDAO remesasDAO;
	@EJB
	private ServicioSaludDAO servicioSaludDAO;

	// Generar documento
	public Integer elaborarOrdinarioProgramacionCaja() {



		Integer plantillaBorradorOrdinarioProgramacionCaja = documentService
				.getPlantillaByType(TipoDocumentosProcesos.PLANTILLAOFICIOPROGRAMACIONCAJA);
		if (plantillaBorradorOrdinarioProgramacionCaja == null) {
			throw new RuntimeException(
					"No se puede crear Borrador Decreto Aporte Estatal, la plantilla no esta cargada");
		}
		try {
			ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryBorradorAporteEstatalVO = documentService
					.getDocumentByPlantillaId(plantillaBorradorOrdinarioProgramacionCaja);
			DocumentoVO documentoBorradorOrdinarioProgramacionCajaVO = documentService
					.getDocument(referenciaDocumentoSummaryBorradorAporteEstatalVO
							.getId());
			String templateOrdinarioProgramacionCaja = tmpDirDoc
					+ File.separator
					+ documentoBorradorOrdinarioProgramacionCajaVO.getName();
			templateOrdinarioProgramacionCaja = templateOrdinarioProgramacionCaja
					.replace(" ", "");
			String filenameBorradorOrdinarioProgramacionCaja = tmpDirDoc
					+ File.separator + new Date().getTime() + "_"
					+ "OrdinarioProgramacionCaja.docx";
			filenameBorradorOrdinarioProgramacionCaja = filenameBorradorOrdinarioProgramacionCaja
					.replaceAll(" ", "");
			System.out.println("filenameBorradorAporteEstatal filename-->"
					+ filenameBorradorOrdinarioProgramacionCaja);
			System.out.println("templateBorradorAporteEstatal template-->"
					+ templateOrdinarioProgramacionCaja);
			GeneradorWord generadorWordPlantillaBorradorOrdinarioProgramacionCaja = new GeneradorWord(
					templateOrdinarioProgramacionCaja);
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contentType = mimemap
					.getContentType(templateOrdinarioProgramacionCaja
							.toLowerCase());
			System.out.println("contenTypeBorradorAporteEstatal->"
					+ contentType);

			generadorWordPlantillaBorradorOrdinarioProgramacionCaja.saveContent(documentoBorradorOrdinarioProgramacionCajaVO
					.getContent(), XWPFDocument.class);

			Map<String, Object> parametersBorradorAporteEstatal = new HashMap<String, Object>();
			parametersBorradorAporteEstatal.put("{ano}",Util.obtenerAno(new Date()));
			parametersBorradorAporteEstatal.put("{mes}",Util.obtenerNombreMes(Util.obtenerMes(new Date())));
			GeneradorResolucionAporteEstatal generadorWordDecretoBorradorAporteEstatal = new GeneradorResolucionAporteEstatal(
					filenameBorradorOrdinarioProgramacionCaja,
					templateOrdinarioProgramacionCaja);
			generadorWordDecretoBorradorAporteEstatal.replaceValues(parametersBorradorAporteEstatal, XWPFDocument.class);
			BodyVO response = alfrescoService.uploadDocument(new File(
					filenameBorradorOrdinarioProgramacionCaja), contentType,
					folderDocEstimacionFlujoCaja.replace("{ANO}", String.valueOf(getAnoCurso() + 1)));
			System.out.println("response responseBorradorAporteEstatal --->"
					+ response);


			TipoDocumento tipoDocumento = new TipoDocumento(
					TipoDocumentosProcesos.PLANTILLAOFICIOPROGRAMACIONCAJA
					.getId());
			plantillaBorradorOrdinarioProgramacionCaja = documentService
					.createDocumentOrdinarioProgramacióndeCaja(tipoDocumento, response.getNodeRef(),
							response.getFileName(), contentType);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (Docx4JException e) {
			e.printStackTrace();
		}
		return plantillaBorradorOrdinarioProgramacionCaja;
	}

	public List<AsignacionDistribucionPerCapitaVO> buscarDatosPlanillaPropuestaEstimacion(
			Integer idPrograma) {
		// List<AntecendentesComunaCalculado> antecendentesComunaCalculado =
		// antecedentesComunaDAO.findAntecedentesComunaCalculadosByDistribucionInicialPercapita(idDistribucionInicialPercapita);
		// List<AsignacionDistribucionPerCapitaVO> antecedentesCalculados = new
		// ArrayList<AsignacionDistribucionPerCapitaVO>();
		// if(antecendentesComunaCalculado != null &&
		// antecendentesComunaCalculado.size() > 0){
		// for(AntecendentesComunaCalculado antecendenteComunaCalculado :
		// antecendentesComunaCalculado){
		// AsignacionDistribucionPerCapitaVO asignacionDistribucionPerCapitaVO =
		// new
		// AsignacionDistribucionPercapitaMapper().getBasic(antecendenteComunaCalculado);
		// if(asignacionDistribucionPerCapitaVO != null){
		// antecedentesCalculados.add(asignacionDistribucionPerCapitaVO);
		// }
		// }
		// }
		// return antecedentesCalculados;
		return null;
	}

	// Para hacer el calculo de la propuesta, se debe hacer una copia de los
	// valores del año pasado.
	public void calcularPropuesta(Integer idProgramaAno, Boolean iniciarFlujoCaja) {
		if(iniciarFlujoCaja){
			long millisecons = System.currentTimeMillis();
			ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idProgramaAno);
			programaAno.setEstadoFlujoCaja(new EstadoPrograma(EstadosProgramas.ENREVISION.getId()));
			Map<Integer, List<Integer>> componentesBySubtitulos = new HashMap<Integer, List<Integer>>();
			for(Componente componente : programaAno.getPrograma().getComponentes()){
				for(ComponenteSubtitulo componenteSubtitulo : componente.getComponenteSubtitulos()){
					if(componentesBySubtitulos.get(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo()) == null){
						List<Integer> componentes = new ArrayList<Integer>();
						componentes.add(componente.getId());
						componentesBySubtitulos.put(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo(), componentes);
					}else{
						componentesBySubtitulos.get(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo()).add(componente.getId());
					}
				}
			}
			List<Caja> cajasPogramaAno = cajaDAO.getCajasByProgramaAno(idProgramaAno);
			if(cajasPogramaAno != null && cajasPogramaAno.size() > 0){
				List<Integer> idCajas = new ArrayList<Integer>();
				for(Caja caja : cajasPogramaAno){
					idCajas.add(caja.getId());
					cajaDAO.eliminarCajaMontosById(caja.getId());
				}
				if(idCajas.size() > 0){
					cajaDAO.eliminarCajasById(idCajas);
				}
			}
			ProgramaAno programaAnoActual = programasDAO.getProgramaAnoByIDProgramaAno(programaAno.getPrograma().getId(), getAnoCurso());
			List<ServicioSalud> servicios = servicioSaludDAO.getServicios();
			System.out.println("Comenzando con subtitulo 24");
			List<Cuota> cuotasPrograma = estimacionFlujoCajaDAO.getCuotasByProgramaAno(idProgramaAno);
			if(componentesBySubtitulos.get(Subtitulo.SUBTITULO24.getId()) != null){
				List<Integer> componentes = componentesBySubtitulos.get(Subtitulo.SUBTITULO24.getId());
				for(ServicioSalud servicioSalud : servicios){
					for(Integer componente : componentes){
						Long marcoPresupuestatarioServicio = 0L;
						Long marcoPresupuestatarioServicioAnoActual = 0L;
						for(Comuna comuna : servicioSalud.getComunas()){
							Long marcoPresupuestario = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAno, componente, Subtitulo.SUBTITULO24.getId());
							marcoPresupuestatarioServicio+=marcoPresupuestario;
							Long marcoPresupuestarioAnoActual = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), programaAnoActual.getIdProgramaAno(), componente, Subtitulo.SUBTITULO24.getId());
							marcoPresupuestatarioServicioAnoActual+=marcoPresupuestarioAnoActual;
						}
						Caja cajaActual = cajaDAO.getByServicioProgramaAnoComponenteSubtitulo(servicioSalud.getId(), programaAnoActual.getIdProgramaAno(), componente, Subtitulo.SUBTITULO24);
						if(cajaActual != null){
							Caja caja = new Caja();
							caja.setCajaInicial(true);
							Componente componenteDTO = componenteDAO.getComponenteByID(componente);
							caja.setIdComponente(componenteDTO);
							caja.setIdSubtitulo(new TipoSubtitulo(Subtitulo.SUBTITULO24.getId()));
							caja.setPrograma(programaAno);
							caja.setServicio(servicioSalud);
							cajaDAO.save(caja);
							List<CajaMonto> cajaMontosActual = cajaActual.getCajaMontos();
							int mesCajaMonto = 1;
							int montoCalculado = 0;
							for(CajaMonto cajaMontoActual : cajaMontosActual){
								boolean mesConMonto = false;
								CajaMonto cajaMonto = new CajaMonto();
								CajaMontoPK cajaMontoPK = new CajaMontoPK(caja.getId(), cajaMontoActual.getMes().getIdMes());
								cajaMonto.setCajaMontoPK(cajaMontoPK);
								cajaMonto.setMes(cajaMontoActual.getMes());
								cajaMonto.setCaja(caja);
								if(cajaMontoActual.getMonto() == 0){
									cajaMonto.setMonto(0);
								}else{
									int porcentaje = (int)((cajaMontoActual.getMonto() * 100.0) / marcoPresupuestatarioServicioAnoActual);
									int monto = (int)(marcoPresupuestatarioServicio * (porcentaje/100.0));
									montoCalculado+=monto;
									cajaMonto.setMonto(monto);
									mesConMonto = true;
								}
								cajaDAO.save(cajaMonto);
								if(mesConMonto){
									mesCajaMonto = cajaMonto.getMes().getIdMes();
								}
							}
							if(marcoPresupuestatarioServicio != montoCalculado){
								int diferenciaMonto = (int)(marcoPresupuestatarioServicio - montoCalculado);
								CajaMonto cajaMonto = cajaDAO.getCajaMontoByCajaMes(caja.getId(), mesCajaMonto);
								if(cajaMonto != null){
									cajaMonto.setMonto(cajaMonto.getMonto() + diferenciaMonto);
								}
							}
						}else{
							Caja caja = new Caja();
							caja.setCajaInicial(true);
							Componente componenteDTO = componenteDAO.getComponenteByID(componente);
							caja.setIdComponente(componenteDTO);
							caja.setIdSubtitulo(new TipoSubtitulo(Subtitulo.SUBTITULO24.getId()));
							caja.setPrograma(programaAno);
							caja.setServicio(servicioSalud);
							cajaDAO.save(caja);
							int montoMarco = 0;
							List<Integer> mesesConCuotas = new ArrayList<Integer>();
							for(int contCuotas = 0; contCuotas <  cuotasPrograma.size(); contCuotas++){
								CajaMonto cajaMonto = new CajaMonto();
								if(contCuotas == 0){
									CajaMontoPK cajaMontoPK = new CajaMontoPK(caja.getId(), 3);
									cajaMonto.setCajaMontoPK(cajaMontoPK);
									cajaMonto.setMes(new Mes(3));
									cajaMonto.setCaja(caja);
									int monto = (int)(marcoPresupuestatarioServicio * (cuotasPrograma.get(contCuotas).getPorcentaje()/100.0));
									montoMarco+=monto;
									cajaMonto.setMonto(monto);
									mesesConCuotas.add(3);
								}else if(contCuotas == (cuotasPrograma.size() - 1)){
									mesesConCuotas.add(cuotasPrograma.get(contCuotas).getIdMes().getIdMes());
									CajaMontoPK cajaMontoPK = new CajaMontoPK(caja.getId(), cuotasPrograma.get(contCuotas).getIdMes().getIdMes());
									cajaMonto.setCajaMontoPK(cajaMontoPK);
									cajaMonto.setMes(cuotasPrograma.get(contCuotas).getIdMes());
									cajaMonto.setCaja(caja);
									int monto = (int)(marcoPresupuestatarioServicio - montoMarco);
									cajaMonto.setMonto(monto);
								}else{
									mesesConCuotas.add(cuotasPrograma.get(contCuotas).getIdMes().getIdMes());
									CajaMontoPK cajaMontoPK = new CajaMontoPK(caja.getId(), cuotasPrograma.get(contCuotas).getIdMes().getIdMes());
									cajaMonto.setCajaMontoPK(cajaMontoPK);
									cajaMonto.setMes(cuotasPrograma.get(contCuotas).getIdMes());
									cajaMonto.setCaja(caja);
									int monto = (int)(marcoPresupuestatarioServicio * (cuotasPrograma.get(contCuotas).getPorcentaje()/100.0));
									montoMarco+=monto;
									cajaMonto.setMonto(monto);
								}
								cajaDAO.save(cajaMonto);
							}
							for(Integer mes = 1; mes <= 12; mes++){
								if(mesesConCuotas.contains(mes)){
									continue;
								}
								CajaMonto cajaMonto = new CajaMonto();
								CajaMontoPK cajaMontoPK = new CajaMontoPK(caja.getId(), mes);
								cajaMonto.setCajaMontoPK(cajaMontoPK);
								cajaMonto.setMes(new Mes(mes));
								cajaMonto.setCaja(caja);
								cajaMonto.setMonto(0);
								cajaDAO.save(cajaMonto);
							}

						}
					}
				}
			}
			System.out.println("Fin subtitulo 24");

			if((componentesBySubtitulos.get(Subtitulo.SUBTITULO21.getId()) != null) || (componentesBySubtitulos.get(Subtitulo.SUBTITULO22.getId()) != null) || (componentesBySubtitulos.get(Subtitulo.SUBTITULO29.getId()) != null)){
				System.out.println("Comenzando subtitulo 21/22/29");
				for (Map.Entry<Integer, List<Integer>> entry : componentesBySubtitulos.entrySet()) { 
					if(!Subtitulo.SUBTITULO24.getId().equals(entry.getKey())){
						List<Integer> componentes = entry.getValue();
						for(ServicioSalud servicioSalud : servicios){
							for(Integer componente : componentes){
								Long marcoPresupuestatarioServicio = 0L;
								Long marcoPresupuestatarioServicioAnoActual = 0L;
								for(Establecimiento establecimiento : servicioSalud.getEstablecimientos()){
									Long marcoPresupuestario = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAno, componente, entry.getKey());
									marcoPresupuestatarioServicio+=marcoPresupuestario;
									Long marcoPresupuestarioAnoActual = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), programaAnoActual.getIdProgramaAno(), componente, entry.getKey());
									marcoPresupuestatarioServicioAnoActual+=marcoPresupuestarioAnoActual;
								}
								Caja cajaActual = cajaDAO.getByServicioProgramaAnoComponenteSubtitulo(servicioSalud.getId(), programaAnoActual.getIdProgramaAno(), componente, Subtitulo.getById(entry.getKey()));
								if(cajaActual != null){
									Caja caja = new Caja();
									caja.setCajaInicial(true);
									Componente componenteDTO = componenteDAO.getComponenteByID(componente);
									caja.setIdComponente(componenteDTO);
									caja.setIdSubtitulo(new TipoSubtitulo(entry.getKey()));
									caja.setPrograma(programaAno);
									caja.setServicio(servicioSalud);
									cajaDAO.save(caja);
									List<CajaMonto> cajaMontosActual = cajaActual.getCajaMontos();
									int mesCajaMonto = 1;
									int montoCalculado = 0;
									for(CajaMonto cajaMontoActual : cajaMontosActual){
										boolean mesConMonto = false;
										CajaMonto cajaMonto = new CajaMonto();
										CajaMontoPK cajaMontoPK = new CajaMontoPK(caja.getId(), cajaMontoActual.getMes().getIdMes());
										cajaMonto.setCajaMontoPK(cajaMontoPK);
										cajaMonto.setMes(cajaMontoActual.getMes());
										cajaMonto.setCaja(caja);
										if(cajaMontoActual.getMonto() == 0){
											cajaMonto.setMonto(0);
										}else{
											int porcentaje = (int)((cajaMontoActual.getMonto() * 100.0) / marcoPresupuestatarioServicioAnoActual);
											int monto = (int)(marcoPresupuestatarioServicio * (porcentaje/100.0));
											montoCalculado+=monto;
											cajaMonto.setMonto(monto);
											mesConMonto = true;
										}
										cajaDAO.save(cajaMonto);
										if(mesConMonto){
											mesCajaMonto = cajaMonto.getMes().getIdMes();
										}
									}
									if(marcoPresupuestatarioServicio != montoCalculado){
										int diferenciaMonto = (int)(marcoPresupuestatarioServicio - montoCalculado);
										CajaMonto cajaMonto = cajaDAO.getCajaMontoByCajaMes(caja.getId(), mesCajaMonto);
										if(cajaMonto != null){
											cajaMonto.setMonto(cajaMonto.getMonto() + diferenciaMonto);
										}
									}
								}else{
									Caja caja = new Caja();
									caja.setCajaInicial(true);
									Componente componenteDTO = componenteDAO.getComponenteByID(componente);
									caja.setIdComponente(componenteDTO);
									caja.setIdSubtitulo(new TipoSubtitulo(entry.getKey()));
									caja.setPrograma(programaAno);
									caja.setServicio(servicioSalud);
									cajaDAO.save(caja);
									int montoMarco = 0;
									List<Integer> mesesConCuotas = new ArrayList<Integer>();
									for(int contCuotas = 0; contCuotas <  cuotasPrograma.size(); contCuotas++){
										CajaMonto cajaMonto = new CajaMonto();
										if(contCuotas == 0){
											CajaMontoPK cajaMontoPK = new CajaMontoPK(caja.getId(), 3);
											cajaMonto.setCajaMontoPK(cajaMontoPK);
											cajaMonto.setMes(new Mes(3));
											cajaMonto.setCaja(caja);
											int monto = (int)(marcoPresupuestatarioServicio * (cuotasPrograma.get(contCuotas).getPorcentaje()/100.0));
											montoMarco+=monto;
											cajaMonto.setMonto(monto);
											mesesConCuotas.add(3);
										}else if(contCuotas == (cuotasPrograma.size() - 1)){
											mesesConCuotas.add(cuotasPrograma.get(contCuotas).getIdMes().getIdMes());
											CajaMontoPK cajaMontoPK = new CajaMontoPK(caja.getId(), cuotasPrograma.get(contCuotas).getIdMes().getIdMes());
											cajaMonto.setCajaMontoPK(cajaMontoPK);
											cajaMonto.setMes(cuotasPrograma.get(contCuotas).getIdMes());
											cajaMonto.setCaja(caja);
											int monto = (int)(marcoPresupuestatarioServicio - montoMarco);
											cajaMonto.setMonto(monto);
										}else{
											mesesConCuotas.add(cuotasPrograma.get(contCuotas).getIdMes().getIdMes());
											CajaMontoPK cajaMontoPK = new CajaMontoPK(caja.getId(), cuotasPrograma.get(contCuotas).getIdMes().getIdMes());
											cajaMonto.setCajaMontoPK(cajaMontoPK);
											cajaMonto.setMes(cuotasPrograma.get(contCuotas).getIdMes());
											cajaMonto.setCaja(caja);
											int monto = (int)(marcoPresupuestatarioServicio * (cuotasPrograma.get(contCuotas).getPorcentaje()/100.0));
											montoMarco+=monto;
											cajaMonto.setMonto(monto);
										}
										cajaDAO.save(cajaMonto);
									}
									for(Integer mes = 1; mes <= 12; mes++){
										if(mesesConCuotas.contains(mes)){
											continue;
										}
										CajaMonto cajaMonto = new CajaMonto();
										CajaMontoPK cajaMontoPK = new CajaMontoPK(caja.getId(), mes);
										cajaMonto.setCajaMontoPK(cajaMontoPK);
										cajaMonto.setMes(new Mes(mes));
										cajaMonto.setCaja(caja);
										cajaMonto.setMonto(0);
										cajaDAO.save(cajaMonto);
									}
								}
							}
						}
					}
				}
				System.out.println("Fin subtitulo 21/22/29 tardo " + ((System.currentTimeMillis()-millisecons)/1000) + "segundos");
			}
		}else{
			//Obtenemos los datos del programa ano anterior.
			/*DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(getAnoCurso());
			List<Object[]> percapitaServicios = antecedentesComunaDAO.groupPercapitaServicioByDistribucionInicialPercapita(distribucionInicialPercapita.getIdDistribucionInicialPercapita());
			if(percapitaServicios != null && percapitaServicios.size() > 0){
				for(Object[] percapitaServicio : percapitaServicios){
					Integer idServicio = ((Number)(percapitaServicio[0])).intValue();
					Long percapitaAno = ((Number)(percapitaServicio[1])).longValue();
					MarcoPresupuestario marcoPresupuestario = cajaDAO.getMarcoPresupuestarioByProgramaAnoServicio(idProgramaAno, idServicio);
					if(percapitaAno != null){
						marcoPresupuestario.setMarcoModificado(percapitaAno.intValue());
					}else{
						marcoPresupuestario.setMarcoModificado(0);
					}
					List<Caja> cajas = cajaDAO.getByIdProgramaAnoIdServicio(idProgramaAno, idServicio);
					if(cajas != null && cajas.size() > 0){
						//Obtenemos el programa actual.
						for (Caja cajaActual : cajas) {
							Integer idComponente = cajaActual.getIdComponente().getId();
							Integer idSubtitulo = cajaActual.getIdSubtitulo().getIdTipoSubtitulo();
							Integer totalComponenteSubtitulo = 0;
							List<ProgramaMunicipalCoreComponente> programaMunicipalesCore = programasDAO.getProgramaMunicipales(idProgramaAno, idComponente, idSubtitulo);
							if(programaMunicipalesCore != null && programaMunicipalesCore.size() > 0){
								for(ProgramaMunicipalCoreComponente programaMunicipalCoreComponente : programaMunicipalesCore){
									totalComponenteSubtitulo += programaMunicipalCoreComponente.getMonto();
								}
							}
							List<ProgramaServicioCoreComponente> programaServiciosCore = programasDAO.getProgramaServicios(idProgramaAno, idComponente, idSubtitulo);
							if(programaServiciosCore != null && programaServiciosCore.size() > 0){
								for(ProgramaServicioCoreComponente programaServicioCoreComponente : programaServiciosCore){
									totalComponenteSubtitulo += programaServicioCoreComponente.getMonto();
								}
							}
							marcoPresupuestario.setMarcoModificado(marcoPresupuestario.getMarcoModificado() + totalComponenteSubtitulo);
						}

						for (Caja cajaActual : cajas) {
							if(cajaActual.getCajaMontos() != null && cajaActual.getCajaMontos().size() > 0){
								for(CajaMonto cajaMonto : cajaActual.getCajaMontos()){
									int monto = (int)(cajaActual.getMonto() * ((100.0/12)/100.0));
									cajaMonto.setMonto(monto);
								}
							}
						}
					}
				}
			}*/
		}
	}

	public Integer getIdPlantillaProgramacion() {
		// TODO Auto-generated method stub
		Integer plantillaId = null;// documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLAPROGRAMACION);
		if (plantillaId == null) {
			// TODO: Implementar la creacion del excel.
			List<BaseVO> servicios = servicioSaludService.getAllServicios();

			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String filename = tmpDir + File.separator
					+ "plantillaPercapita.xlsx";
			String contenType = mimemap.getContentType(filename.toLowerCase());
			GeneradorExcel generadorExcel = new GeneradorExcel(filename);
			List<String> headers = new ArrayList<String>();
			headers.add("REGION");
			headers.add("SERVICIO");
			headers.add("COMUNA");
			headers.add("POBLACION");
			headers.add("POBLACION MAYOR DE 65 AÑOS");
			EstimacionFlujoCajaSheetExcel estimacionFlujoCajaSheetExcel = new EstimacionFlujoCajaSheetExcel(
					headers, servicios);
			generadorExcel.addSheet(estimacionFlujoCajaSheetExcel, "Hoja 1");
			try {
				BodyVO response = alfrescoService.uploadDocument(
						generadorExcel.saveExcel(), contenType,
						folderEstimacionFlujoCaja);
				System.out
				.println("response AsignacionRecursosPercapitaSheetExcel --->"
						+ response);
				plantillaId = documentService.createTemplate(
						TipoDocumentosProcesos.PLANTILLAPOBLACIONINSCRITA,
						response.getNodeRef(), response.getFileName(),
						contenType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else { 
			plantillaId = documentService.getDocumentoIdByPlantillaId(plantillaId);
		}
		return plantillaId;
	}

	public Integer getIdPlantillaPropuesta() {
		Integer plantillaId = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLAPROPUESTA);
		if (plantillaId == null) {
			List<BaseVO> servicios = servicioSaludService.getAllServicios();
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String filename = tmpDir + File.separator
					+ "plantillaPercapita.xlsx";
			String contenType = mimemap.getContentType(filename.toLowerCase());
			GeneradorExcel generadorExcel = new GeneradorExcel(filename);
			List<String> headers = new ArrayList<String>();
			headers.add("REGION");
			headers.add("SERVICIO");
			headers.add("COMUNA");
			headers.add("POBLACION");
			headers.add("POBLACION MAYOR DE 65 AÑOS");
			EstimacionFlujoCajaSheetExcel estimacionFlujoCajaSheetExcel = new EstimacionFlujoCajaSheetExcel(headers, servicios);
			generadorExcel.addSheet(estimacionFlujoCajaSheetExcel, "Hoja 1");
			try {
				BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderEstimacionFlujoCaja);
				System.out.println("response AsignacionRecursosPercapitaSheetExcel --->"+ response);
				plantillaId = documentService.createTemplate(TipoDocumentosProcesos.PLANTILLAPROPUESTA, response.getNodeRef(), response.getFileName(), contenType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			plantillaId = documentService.getDocumentoIdByPlantillaId(plantillaId);
		}
		return plantillaId;
	}

	// CORREO
	public Integer getAnoCurso() {
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
		}else{
			dateFormat = new SimpleDateFormat("MMMM");
			mesCurso = dateFormat.format(new Date());
		}
		return mesCurso;
	}

	public Integer createSeguimiento(Integer idLineaProgramatica,
			TareasSeguimiento tareaSeguimiento, String subject, String body,
			String username, List<String> para, List<String> conCopia,
			List<String> conCopiaOculta, List<Integer> documentos) {

		Integer referenciaDocId = 0;
		String from = usuarioDAO.getEmailByUsername(username);
		if (from == null) {
			throw new RuntimeException("Usuario no tiene un email valido");
		}
		List<ReferenciaDocumentoSummaryVO> documentosTmp = new ArrayList<ReferenciaDocumentoSummaryVO>();
		if (documentos != null && documentos.size() > 0) {
			for (Integer referenciaDocumentoId : documentos) {
				MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
				ReferenciaDocumentoSummaryVO referenciaDocumentoSummaryVO = documentService
						.getDocumentSummary(referenciaDocumentoId);
				documentosTmp.add(referenciaDocumentoSummaryVO);
				String contenType = mimemap
						.getContentType(referenciaDocumentoSummaryVO.getPath()
								.toLowerCase());
				BodyVO response = alfrescoService.uploadDocument(new File(
						referenciaDocumentoSummaryVO.getPath()), contenType,
						folderEstimacionFlujoCaja.replace("{ANO}",
								getAnoCurso().toString()));
				System.out.println("response upload template --->" + response);
				documentService.updateDocumentTemplate(referenciaDocumentoId,
						response.getNodeRef(), response.getFileName(),
						contenType);
			}
		}

		Integer idSeguimiento = seguimientoService.createSeguimiento(
				tareaSeguimiento, subject, body, from, para, conCopia,
				conCopiaOculta, documentosTmp);
		Seguimiento seguimiento = seguimientoDAO
				.getSeguimientoById(idSeguimiento);

		estimacionFlujoCajaDAO.createSeguimiento(idLineaProgramatica,seguimiento);
		return 1;
	}

	public List<SeguimientoVO> getBitacora(TareasSeguimiento tareaSeguimiento) {
		return seguimientoService.getBitacoraEstimacionFlujoCaja(tareaSeguimiento);
	}


	public Integer generarPlanillaPropuestaConsolidador(String username) {
		Integer planillaTrabajoId = null;
		//obtengo todos los programas del usuario

		//obtengo lista de servicios
		Integer mes = Integer.parseInt(getMesCurso(true));
		List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();

		List<ResumenConsolidadorVO> resumenConsolidadorSubtitulo21 = new ArrayList<ResumenConsolidadorVO>();
		List<ResumenConsolidadorVO> resumenConsolidadorSubtitulo22 = new ArrayList<ResumenConsolidadorVO>();
		List<ResumenConsolidadorVO> resumenConsolidadorSubtitulo24 = new ArrayList<ResumenConsolidadorVO>();
		List<ResumenConsolidadorVO> resumenConsolidadorSubtitulo29 = new ArrayList<ResumenConsolidadorVO>();

		List<ProgramaAno> programasSubtitulo21 = programasDAO.getProgramasBySubtitulo(getAnoCurso(), Subtitulo.SUBTITULO21);
		List<ProgramaAno> programasSubtitulo22 = programasDAO.getProgramasBySubtitulo(getAnoCurso(), Subtitulo.SUBTITULO22);
		List<ProgramaAno> programasSubtitulo24 = programasDAO.getProgramasBySubtitulo(getAnoCurso(), Subtitulo.SUBTITULO24);
		List<ProgramaAno> programasSubtitulo29 = programasDAO.getProgramasBySubtitulo(getAnoCurso(), Subtitulo.SUBTITULO29);

		List<CellExcelVO> header = new ArrayList<CellExcelVO>();
		List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
		header.add(new CellExcelVO("COD SS", 1, 2));
		header.add(new CellExcelVO("SERVICIOS DE SALUD", 1, 2));

		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator + "planillaPropuestaEstimacionFlujoCajaConsolidador" + getMesCurso(false) + ".xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());

		GeneradorExcel generadorExcel = new GeneradorExcel(filename);

		if(servicios != null && servicios.size() > 0){
			for(ServicioSalud servicioSalud : servicios){
				ResumenConsolidadorVO resumenConsolidadorVO = new ResumenConsolidadorVO();
				resumenConsolidadorVO.setCodigoServicio(servicioSalud.getId().toString());
				resumenConsolidadorVO.setServicio(servicioSalud.getNombre());
				if(programasSubtitulo21 != null && programasSubtitulo21.size() > 0){
					List<Long> montos = new ArrayList<Long>();
					header.add(new CellExcelVO("SUBTÍTULO 21", programasSubtitulo21.size() + 1, 1));			
					for(ProgramaAno programaAno : programasSubtitulo21) {
						subHeader.add(new CellExcelVO(programaAno.getPrograma().getNombre(), 1, 1));
					}
					subHeader.add(new CellExcelVO("TOTAL REF. SERVICIOS SUBT. 21", 1, 1));
					for(ProgramaAno programaAno : programasSubtitulo21) {
						List<Caja> cajas = cajaDAO.getByProgramaAnoServicioSubtitulo(programaAno.getIdProgramaAno(), servicioSalud.getId(), Subtitulo.SUBTITULO21);
						if(cajas != null && cajas.size() > 0){
							for(Caja caja : cajas) {
								if(caja.getCajaMontos() != null && caja.getCajaMontos().size() > 0){
									Long totalMes = 0L;
									for(CajaMonto cajaMonto : caja.getCajaMontos()){
										if(mes.equals(cajaMonto.getMes().getIdMes())){
											totalMes+=cajaMonto.getMonto();
										}
									}
									montos.add(totalMes);
								}
							}
						}else{
							montos.add(0L);
						}
					}
					resumenConsolidadorVO.setMontos(montos);
					resumenConsolidadorSubtitulo21.add(resumenConsolidadorVO);
				}
			}
			EstimacionFlujoCajaConsolidadorSheetExcel estimacionFlujoCajaConsolidadorSheetExcel = new EstimacionFlujoCajaConsolidadorSheetExcel(header, subHeader, null);
			estimacionFlujoCajaConsolidadorSheetExcel.setItems(resumenConsolidadorSubtitulo21);
			generadorExcel.addSheet(estimacionFlujoCajaConsolidadorSheetExcel, getMesCurso(false));
		}





		//estimacionFlujoCajaSubtituloSheetExcel.setItems(flujoCajaSub22);



		//String mes = getMesCurso(false);


		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel
					.saveExcel(), contenType, folderDocEstimacionFlujoCaja
					.replace("{ANO}", getAnoCurso().toString()));
			System.out
			.println("response planillaPropuestaEstimacionFlujoCajaConsolidador --->"
					+ response);


			TipoDocumento tipoDocumento = new TipoDocumento(
					TipoDocumentosProcesos.PLANTILLAPROPUESTA.getId());
			planillaTrabajoId = documentService
					.createDocumentPropuestaConsolidador(tipoDocumento, response.getNodeRef(),
							response.getFileName(), contenType, getAnoCurso(), Integer.parseInt(getMesCurso(true)));

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("planillaTrabajoId --> "+planillaTrabajoId);

		return planillaTrabajoId;

	}

	@Asynchronous
	public void generarPlanillaPropuesta(Integer idProgramaAno) {
		long milliseconds = System.currentTimeMillis();
		System.out.println("Inicio generarPlanillaPropuesta");
		ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idProgramaAno);
		Map<Integer, List<Integer>> componentesBySubtitulos = new HashMap<Integer, List<Integer>>();
		for(Componente componente : programaAno.getPrograma().getComponentes()){
			for(ComponenteSubtitulo componenteSubtitulo : componente.getComponenteSubtitulos()){
				if(componentesBySubtitulos.get(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo()) == null){
					List<Integer> componentes = new ArrayList<Integer>();
					componentes.add(componente.getId());
					componentesBySubtitulos.put(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo(), componentes);
				}else{
					componentesBySubtitulos.get(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo()).add(componente.getId());
				}
			}
		}
		List<SubtituloFlujoCajaVO> flujoCajaSub21 = null;
		if(componentesBySubtitulos.get(Subtitulo.SUBTITULO21.getId()) != null){
			System.out.println("buscando sub21");
			flujoCajaSub21 = getMonitoreoServicioByProgramaAnoComponenteSubtitulo(programaAno.getIdProgramaAno(), componentesBySubtitulos.get(Subtitulo.SUBTITULO21.getId()), Subtitulo.SUBTITULO21, true);
		}
		List<SubtituloFlujoCajaVO> flujoCajaSub22 = null;
		if(componentesBySubtitulos.get(Subtitulo.SUBTITULO22.getId()) != null){
			System.out.println("buscando sub22");
			flujoCajaSub22 = getMonitoreoServicioByProgramaAnoComponenteSubtitulo(programaAno.getIdProgramaAno(), componentesBySubtitulos.get(Subtitulo.SUBTITULO22.getId()), Subtitulo.SUBTITULO22, true);
		}
		List<SubtituloFlujoCajaVO> flujoCajaSub24 = null;
		if(componentesBySubtitulos.get(Subtitulo.SUBTITULO24.getId()) != null){
			System.out.println("buscando sub24");
			flujoCajaSub24 = getMonitoreoComunaByProgramaAnoComponenteSubtitulo(programaAno.getIdProgramaAno(), componentesBySubtitulos.get(Subtitulo.SUBTITULO24.getId()), Subtitulo.SUBTITULO24, true);
		}
		List<SubtituloFlujoCajaVO> flujoCajaSub29 = null;
		if(componentesBySubtitulos.get(Subtitulo.SUBTITULO29.getId()) != null){
			System.out.println("buscando sub29");
			flujoCajaSub29 = getMonitoreoServicioByProgramaAnoComponenteSubtitulo(programaAno.getIdProgramaAno(), componentesBySubtitulos.get(Subtitulo.SUBTITULO29.getId()), Subtitulo.SUBTITULO29, true);
		}

		DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(getAnoCurso() + 1);
		List<AntecendentesComunaCalculado> antecedentesComunasCalculado = antecedentesComunaDAO.findAntecedentesComunaCalculadosByDistribucionInicialPercapitaVigente(distribucionInicialPercapita.getIdDistribucionInicialPercapita());
		System.out.println("Comenzando con percapita");
		List<SubtituloFlujoCajaVO> flujoCajaPercapita = new ArrayList<SubtituloFlujoCajaVO>();
		List<SubtituloFlujoCajaVO> desempenosDificil = new ArrayList<SubtituloFlujoCajaVO>();
		if(antecedentesComunasCalculado != null && antecedentesComunasCalculado.size() > 0){
			for(AntecendentesComunaCalculado antecedentesComunaCalculado : antecedentesComunasCalculado){
				flujoCajaPercapita.add(getPercapitaFlujoCajaVO(antecedentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getNombre(), antecedentesComunaCalculado.getPercapitaMes()));
				desempenosDificil.add(getDesempenoDificialFlujoCajaVO(antecedentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getNombre(), antecedentesComunaCalculado.getDesempenoDificil()));
			}
		}
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		String filename = tmpDir + File.separator + "planillaPropuestaEstimacionFlujoCaja.xlsx";
		String contenType = mimemap.getContentType(filename.toLowerCase());
		GeneradorExcel generadorExcel = new GeneradorExcel(filename);
		List<CellExcelVO> header = new ArrayList<CellExcelVO>();

		String anoCurso =  "AÑO " + programaAno.getAno().getAno();
		header.add(new CellExcelVO("SERVICIOS DE SALUD", 1, 2));
		header.add(new CellExcelVO(programaAno.getPrograma().getNombre() , 12, 1));

		List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
		Integer mesCurso = Integer.parseInt(getMesCurso(true));
		List<Mes> meses = mesDAO.getAll();
		for(Mes mes : meses){
			if(mes.getIdMes() < mesCurso){
				subHeader.add(new CellExcelVO(mes.getNombre() + " REAL", 1, 1));
			}else{
				subHeader.add(new CellExcelVO(mes.getNombre(), 1, 1));
			}
		}
		System.out.println("flujoCajaPercapita--->"+flujoCajaPercapita);
		boolean removeLast = false;
		System.out.println("Inicio pestaña percapita");
		if(flujoCajaPercapita != null && !flujoCajaPercapita.isEmpty()){
			header.set(1, new CellExcelVO("REMESA PER CÁPITA" , 12, 1));
			EstimacionFlujoCajaSubtituloSheetExcel estimacionFlujoCajaSubtituloSheetExcel = new EstimacionFlujoCajaSubtituloSheetExcel(header, subHeader, null);
			removeLast = true;
			header.add(new CellExcelVO("TOTAL PER CÁPITA " + anoCurso, 1, 2));
			SubtituloFlujoCajaVO totalSubtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
			totalSubtituloFlujoCajaVO.setServicio("Total");
			Long totalEnero=0L, totalFebrero=0L, totalMarzo=0L, totalAbril=0L, totalMayo=0L, totalJunio=0L, totalJulio=0L;
			Long totalAgosto=0L, totalSeptiembre=0L, totalOctubre=0L, totalNoviembre=0L, totalDiciembre=0L, totalSubtituloPercapita=0L;
			for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : flujoCajaPercapita){ 
				totalEnero+=subtituloFlujoCajaVO.getCajaMontos().get(0).getMontoMes();
				totalFebrero+=subtituloFlujoCajaVO.getCajaMontos().get(1).getMontoMes();
				totalMarzo+=subtituloFlujoCajaVO.getCajaMontos().get(2).getMontoMes();
				totalAbril+=subtituloFlujoCajaVO.getCajaMontos().get(3).getMontoMes();
				totalMayo+=subtituloFlujoCajaVO.getCajaMontos().get(4).getMontoMes();
				totalJunio+=subtituloFlujoCajaVO.getCajaMontos().get(5).getMontoMes();
				totalJulio+=subtituloFlujoCajaVO.getCajaMontos().get(6).getMontoMes();
				totalAgosto+=subtituloFlujoCajaVO.getCajaMontos().get(7).getMontoMes();
				totalSeptiembre+=subtituloFlujoCajaVO.getCajaMontos().get(8).getMontoMes();
				totalOctubre+=subtituloFlujoCajaVO.getCajaMontos().get(9).getMontoMes();
				totalNoviembre+=subtituloFlujoCajaVO.getCajaMontos().get(10).getMontoMes();
				totalDiciembre+=subtituloFlujoCajaVO.getCajaMontos().get(11).getMontoMes();
				totalSubtituloPercapita+=subtituloFlujoCajaVO.getTotalMontos();
			}
			totalSubtituloFlujoCajaVO.getCajaMontos().get(0).setMontoMes(totalEnero);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(1).setMontoMes(totalFebrero);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(2).setMontoMes(totalMarzo);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(3).setMontoMes(totalAbril);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(4).setMontoMes(totalMayo);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(5).setMontoMes(totalJunio);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(6).setMontoMes(totalJulio);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(7).setMontoMes(totalAgosto);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(8).setMontoMes(totalSeptiembre);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(9).setMontoMes(totalOctubre);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(10).setMontoMes(totalNoviembre);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(11).setMontoMes(totalDiciembre);
			totalSubtituloFlujoCajaVO.setTotalMontos(totalSubtituloPercapita);
			flujoCajaPercapita.add(totalSubtituloFlujoCajaVO);
			estimacionFlujoCajaSubtituloSheetExcel.setItems(flujoCajaPercapita);
			generadorExcel.addSheet(estimacionFlujoCajaSubtituloSheetExcel, "Per Cápita");

			if(desempenosDificil != null && desempenosDificil.size() >0){
				header.set(1, new CellExcelVO("ASIGNACIÓN DE DESEMPEÑO DIFICIL" , 12, 1));
				if(removeLast){
					header.remove(header.size()-1);
					removeLast = false;
				}
				removeLast = true;
				header.add(new CellExcelVO("TOTAL ASIGNACIÓN DE DESEMPEÑO DIFICIL SUBT. 24 " + anoCurso, 1, 2));
				SubtituloFlujoCajaVO totalSubtituloDesempenoFlujoCajaVO = new SubtituloFlujoCajaVO();
				totalSubtituloDesempenoFlujoCajaVO.setServicio("Total");
				Long totalEneroDesempeno=0L, totalFebreroDesempeno=0L, totalMarzoDesempeno=0L, totalAbrilDesempeno=0L, totalMayoDesempeno=0L, totalJunioDesempeno=0L, totalJulioDesempeno=0L;
				Long totalAgostoDesempeno=0L, totalSeptiembreDesempeno=0L, totalOctubreDesempeno=0L, totalNoviembreDesempeno=0L, totalDiciembreDesempeno=0L, totalSubtituloDesempeno=0L;
				for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : desempenosDificil){ 
					totalEneroDesempeno+=subtituloFlujoCajaVO.getCajaMontos().get(0).getMontoMes();
					totalFebreroDesempeno+=subtituloFlujoCajaVO.getCajaMontos().get(1).getMontoMes();
					totalMarzoDesempeno+=subtituloFlujoCajaVO.getCajaMontos().get(2).getMontoMes();
					totalAbrilDesempeno+=subtituloFlujoCajaVO.getCajaMontos().get(3).getMontoMes();
					totalMayoDesempeno+=subtituloFlujoCajaVO.getCajaMontos().get(4).getMontoMes();
					totalJunioDesempeno+=subtituloFlujoCajaVO.getCajaMontos().get(5).getMontoMes();
					totalJulioDesempeno+=subtituloFlujoCajaVO.getCajaMontos().get(6).getMontoMes();
					totalAgostoDesempeno+=subtituloFlujoCajaVO.getCajaMontos().get(7).getMontoMes();
					totalSeptiembreDesempeno+=subtituloFlujoCajaVO.getCajaMontos().get(8).getMontoMes();
					totalOctubreDesempeno+=subtituloFlujoCajaVO.getCajaMontos().get(9).getMontoMes();
					totalNoviembreDesempeno+=subtituloFlujoCajaVO.getCajaMontos().get(10).getMontoMes();
					totalDiciembreDesempeno+=subtituloFlujoCajaVO.getCajaMontos().get(11).getMontoMes();
					totalSubtituloDesempeno+=subtituloFlujoCajaVO.getTotalMontos();
				}
				totalSubtituloDesempenoFlujoCajaVO.getCajaMontos().get(0).setMontoMes(totalEneroDesempeno);
				totalSubtituloDesempenoFlujoCajaVO.getCajaMontos().get(1).setMontoMes(totalFebreroDesempeno);
				totalSubtituloDesempenoFlujoCajaVO.getCajaMontos().get(2).setMontoMes(totalMarzoDesempeno);
				totalSubtituloDesempenoFlujoCajaVO.getCajaMontos().get(3).setMontoMes(totalAbrilDesempeno);
				totalSubtituloDesempenoFlujoCajaVO.getCajaMontos().get(4).setMontoMes(totalMayoDesempeno);
				totalSubtituloDesempenoFlujoCajaVO.getCajaMontos().get(5).setMontoMes(totalJunioDesempeno);
				totalSubtituloDesempenoFlujoCajaVO.getCajaMontos().get(6).setMontoMes(totalJulioDesempeno);
				totalSubtituloDesempenoFlujoCajaVO.getCajaMontos().get(7).setMontoMes(totalAgostoDesempeno);
				totalSubtituloDesempenoFlujoCajaVO.getCajaMontos().get(8).setMontoMes(totalSeptiembreDesempeno);
				totalSubtituloDesempenoFlujoCajaVO.getCajaMontos().get(9).setMontoMes(totalOctubreDesempeno);
				totalSubtituloDesempenoFlujoCajaVO.getCajaMontos().get(10).setMontoMes(totalNoviembreDesempeno);
				totalSubtituloDesempenoFlujoCajaVO.getCajaMontos().get(11).setMontoMes(totalDiciembreDesempeno);
				totalSubtituloDesempenoFlujoCajaVO.setTotalMontos(totalSubtituloDesempeno);
				desempenosDificil.add(totalSubtituloDesempenoFlujoCajaVO);
				estimacionFlujoCajaSubtituloSheetExcel.setItems(desempenosDificil);
				generadorExcel.addSheet(estimacionFlujoCajaSubtituloSheetExcel, "Per Cápita");
			}

		}
		System.out.println("Fin pestaña percapita");
		if(removeLast){
			header.remove(header.size()-1);
			removeLast = false;
		}
		System.out.println("Inicio pestaña sub24");
		if(flujoCajaSub24 != null && !flujoCajaSub24.isEmpty()){
			header.set(1,new CellExcelVO(programaAno.getPrograma().getNombre() , 12, 1));
			header.add(new CellExcelVO("TOTAL " + programaAno.getPrograma().getNombre() + " SUBT. 24 " + anoCurso, 1, 2));
			removeLast = true;
			SubtituloFlujoCajaVO totalSubtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
			totalSubtituloFlujoCajaVO.setServicio("Total");
			Long totalEnero=0L, totalFebrero=0L, totalMarzo=0L, totalAbril=0L, totalMayo=0L, totalJunio=0L, totalJulio=0L;
			Long totalAgosto=0L, totalSeptiembre=0L, totalOctubre=0L, totalNoviembre=0L, totalDiciembre=0L, totalSubtitulo24=0L;
			for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : flujoCajaSub24){ 
				totalEnero+=subtituloFlujoCajaVO.getCajaMontos().get(0).getMontoMes();
				totalFebrero+=subtituloFlujoCajaVO.getCajaMontos().get(1).getMontoMes();
				totalMarzo+=subtituloFlujoCajaVO.getCajaMontos().get(2).getMontoMes();
				totalAbril+=subtituloFlujoCajaVO.getCajaMontos().get(3).getMontoMes();
				totalMayo+=subtituloFlujoCajaVO.getCajaMontos().get(4).getMontoMes();
				totalJunio+=subtituloFlujoCajaVO.getCajaMontos().get(5).getMontoMes();
				totalJulio+=subtituloFlujoCajaVO.getCajaMontos().get(6).getMontoMes();
				totalAgosto+=subtituloFlujoCajaVO.getCajaMontos().get(7).getMontoMes();
				totalSeptiembre+=subtituloFlujoCajaVO.getCajaMontos().get(8).getMontoMes();
				totalOctubre+=subtituloFlujoCajaVO.getCajaMontos().get(9).getMontoMes();
				totalNoviembre+=subtituloFlujoCajaVO.getCajaMontos().get(10).getMontoMes();
				totalDiciembre+=subtituloFlujoCajaVO.getCajaMontos().get(11).getMontoMes();
				totalSubtitulo24+=subtituloFlujoCajaVO.getTotalMontos();
			}
			totalSubtituloFlujoCajaVO.getCajaMontos().get(0).setMontoMes(totalEnero);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(1).setMontoMes(totalFebrero);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(2).setMontoMes(totalMarzo);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(3).setMontoMes(totalAbril);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(4).setMontoMes(totalMayo);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(5).setMontoMes(totalJunio);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(6).setMontoMes(totalJulio);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(7).setMontoMes(totalAgosto);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(8).setMontoMes(totalSeptiembre);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(9).setMontoMes(totalOctubre);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(10).setMontoMes(totalNoviembre);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(11).setMontoMes(totalDiciembre);
			totalSubtituloFlujoCajaVO.setTotalMontos(totalSubtitulo24);
			flujoCajaSub24.add(totalSubtituloFlujoCajaVO);
			EstimacionFlujoCajaSubtituloSheetExcel estimacionFlujoCajaSubtituloSheetExcel = new EstimacionFlujoCajaSubtituloSheetExcel(header, subHeader, null);
			estimacionFlujoCajaSubtituloSheetExcel.setItems(flujoCajaSub24);
			generadorExcel.addSheet(estimacionFlujoCajaSubtituloSheetExcel, "Ref. Mun.");
		}
		System.out.println("Fin pestaña sub24");
		if(removeLast){
			header.remove(header.size()-1);
			removeLast = false;
		}
		System.out.println("Inicio pestaña sub21");
		if(flujoCajaSub21 != null && !flujoCajaSub21.isEmpty()){
			header.set(1,new CellExcelVO(programaAno.getPrograma().getNombre() , 12, 1));
			header.add(new CellExcelVO("TOTAL " + programaAno.getPrograma().getNombre() + " SUBT. 21 " + anoCurso, 1, 2));
			removeLast = true;
			SubtituloFlujoCajaVO totalSubtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
			totalSubtituloFlujoCajaVO.setServicio("Total");
			Long totalEnero=0L, totalFebrero=0L, totalMarzo=0L, totalAbril=0L, totalMayo=0L, totalJunio=0L, totalJulio=0L;
			Long totalAgosto=0L, totalSeptiembre=0L, totalOctubre=0L, totalNoviembre=0L, totalDiciembre=0L, totalSubtitulo21=0L;
			for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : flujoCajaSub21){ 
				totalEnero+=subtituloFlujoCajaVO.getCajaMontos().get(0).getMontoMes();
				totalFebrero+=subtituloFlujoCajaVO.getCajaMontos().get(1).getMontoMes();
				totalMarzo+=subtituloFlujoCajaVO.getCajaMontos().get(2).getMontoMes();
				totalAbril+=subtituloFlujoCajaVO.getCajaMontos().get(3).getMontoMes();
				totalMayo+=subtituloFlujoCajaVO.getCajaMontos().get(4).getMontoMes();
				totalJunio+=subtituloFlujoCajaVO.getCajaMontos().get(5).getMontoMes();
				totalJulio+=subtituloFlujoCajaVO.getCajaMontos().get(6).getMontoMes();
				totalAgosto+=subtituloFlujoCajaVO.getCajaMontos().get(7).getMontoMes();
				totalSeptiembre+=subtituloFlujoCajaVO.getCajaMontos().get(8).getMontoMes();
				totalOctubre+=subtituloFlujoCajaVO.getCajaMontos().get(9).getMontoMes();
				totalNoviembre+=subtituloFlujoCajaVO.getCajaMontos().get(10).getMontoMes();
				totalDiciembre+=subtituloFlujoCajaVO.getCajaMontos().get(11).getMontoMes();
				totalSubtitulo21+=subtituloFlujoCajaVO.getTotalMontos();
			}
			totalSubtituloFlujoCajaVO.getCajaMontos().get(0).setMontoMes(totalEnero);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(1).setMontoMes(totalFebrero);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(2).setMontoMes(totalMarzo);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(3).setMontoMes(totalAbril);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(4).setMontoMes(totalMayo);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(5).setMontoMes(totalJunio);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(6).setMontoMes(totalJulio);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(7).setMontoMes(totalAgosto);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(8).setMontoMes(totalSeptiembre);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(9).setMontoMes(totalOctubre);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(10).setMontoMes(totalNoviembre);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(11).setMontoMes(totalDiciembre);
			totalSubtituloFlujoCajaVO.setTotalMontos(totalSubtitulo21);
			flujoCajaSub21.add(totalSubtituloFlujoCajaVO);
			EstimacionFlujoCajaSubtituloSheetExcel estimacionFlujoCajaSubtituloSheetExcel = new EstimacionFlujoCajaSubtituloSheetExcel(header, subHeader, null);
			estimacionFlujoCajaSubtituloSheetExcel.setItems(flujoCajaSub21);
			generadorExcel.addSheet(estimacionFlujoCajaSubtituloSheetExcel, "21");
		}
		System.out.println("Fin pestaña sub21");
		if(removeLast){
			header.remove(header.size()-1);
			removeLast = false;
		}
		System.out.println("Inicio pestaña sub22");
		if(flujoCajaSub22 != null && !flujoCajaSub22.isEmpty()){
			header.set(1,new CellExcelVO(programaAno.getPrograma().getNombre() , 12, 1));
			header.add(new CellExcelVO("TOTAL " + programaAno.getPrograma().getNombre() + " SUBT. 22 " + anoCurso, 1, 2));
			removeLast = true;
			SubtituloFlujoCajaVO totalSubtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
			totalSubtituloFlujoCajaVO.setServicio("Total");
			Long totalEnero=0L, totalFebrero=0L, totalMarzo=0L, totalAbril=0L, totalMayo=0L, totalJunio=0L, totalJulio=0L;
			Long totalAgosto=0L, totalSeptiembre=0L, totalOctubre=0L, totalNoviembre=0L, totalDiciembre=0L, totalSubtitulo22=0L;
			for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : flujoCajaSub22){ 
				totalEnero+=subtituloFlujoCajaVO.getCajaMontos().get(0).getMontoMes();
				totalFebrero+=subtituloFlujoCajaVO.getCajaMontos().get(1).getMontoMes();
				totalMarzo+=subtituloFlujoCajaVO.getCajaMontos().get(2).getMontoMes();
				totalAbril+=subtituloFlujoCajaVO.getCajaMontos().get(3).getMontoMes();
				totalMayo+=subtituloFlujoCajaVO.getCajaMontos().get(4).getMontoMes();
				totalJunio+=subtituloFlujoCajaVO.getCajaMontos().get(5).getMontoMes();
				totalJulio+=subtituloFlujoCajaVO.getCajaMontos().get(6).getMontoMes();
				totalAgosto+=subtituloFlujoCajaVO.getCajaMontos().get(7).getMontoMes();
				totalSeptiembre+=subtituloFlujoCajaVO.getCajaMontos().get(8).getMontoMes();
				totalOctubre+=subtituloFlujoCajaVO.getCajaMontos().get(9).getMontoMes();
				totalNoviembre+=subtituloFlujoCajaVO.getCajaMontos().get(10).getMontoMes();
				totalDiciembre+=subtituloFlujoCajaVO.getCajaMontos().get(11).getMontoMes();
				totalSubtitulo22+=subtituloFlujoCajaVO.getTotalMontos();
			}
			totalSubtituloFlujoCajaVO.getCajaMontos().get(0).setMontoMes(totalEnero);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(1).setMontoMes(totalFebrero);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(2).setMontoMes(totalMarzo);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(3).setMontoMes(totalAbril);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(4).setMontoMes(totalMayo);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(5).setMontoMes(totalJunio);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(6).setMontoMes(totalJulio);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(7).setMontoMes(totalAgosto);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(8).setMontoMes(totalSeptiembre);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(9).setMontoMes(totalOctubre);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(10).setMontoMes(totalNoviembre);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(11).setMontoMes(totalDiciembre);
			totalSubtituloFlujoCajaVO.setTotalMontos(totalSubtitulo22);
			flujoCajaSub22.add(totalSubtituloFlujoCajaVO);
			EstimacionFlujoCajaSubtituloSheetExcel estimacionFlujoCajaSubtituloSheetExcel = new EstimacionFlujoCajaSubtituloSheetExcel(header, subHeader, null);
			estimacionFlujoCajaSubtituloSheetExcel.setItems(flujoCajaSub22);
			generadorExcel.addSheet(estimacionFlujoCajaSubtituloSheetExcel, "22");
		}
		System.out.println("Fin pestaña sub22");
		if(removeLast){
			header.remove(header.size()-1);
			removeLast = false;
		}
		System.out.println("Inicio pestaña sub29");
		if(flujoCajaSub29 != null && !flujoCajaSub29.isEmpty()){
			header.set(1,new CellExcelVO(programaAno.getPrograma().getNombre() , 12, 1));
			header.add(new CellExcelVO("TOTAL " + programaAno.getPrograma().getNombre() + " SUBT. 29 " + anoCurso, 1, 2));
			removeLast = true;
			SubtituloFlujoCajaVO totalSubtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
			totalSubtituloFlujoCajaVO.setServicio("Total");
			Long totalEnero=0L, totalFebrero=0L, totalMarzo=0L, totalAbril=0L, totalMayo=0L, totalJunio=0L, totalJulio=0L;
			Long totalAgosto=0L, totalSeptiembre=0L, totalOctubre=0L, totalNoviembre=0L, totalDiciembre=0L, totalSubtitulo29=0L;
			for(SubtituloFlujoCajaVO subtituloFlujoCajaVO : flujoCajaSub29){ 
				totalEnero+=subtituloFlujoCajaVO.getCajaMontos().get(0).getMontoMes();
				totalFebrero+=subtituloFlujoCajaVO.getCajaMontos().get(1).getMontoMes();
				totalMarzo+=subtituloFlujoCajaVO.getCajaMontos().get(2).getMontoMes();
				totalAbril+=subtituloFlujoCajaVO.getCajaMontos().get(3).getMontoMes();
				totalMayo+=subtituloFlujoCajaVO.getCajaMontos().get(4).getMontoMes();
				totalJunio+=subtituloFlujoCajaVO.getCajaMontos().get(5).getMontoMes();
				totalJulio+=subtituloFlujoCajaVO.getCajaMontos().get(6).getMontoMes();
				totalAgosto+=subtituloFlujoCajaVO.getCajaMontos().get(7).getMontoMes();
				totalSeptiembre+=subtituloFlujoCajaVO.getCajaMontos().get(8).getMontoMes();
				totalOctubre+=subtituloFlujoCajaVO.getCajaMontos().get(9).getMontoMes();
				totalNoviembre+=subtituloFlujoCajaVO.getCajaMontos().get(10).getMontoMes();
				totalDiciembre+=subtituloFlujoCajaVO.getCajaMontos().get(11).getMontoMes();
				totalSubtitulo29+=subtituloFlujoCajaVO.getTotalMontos();
			}
			totalSubtituloFlujoCajaVO.getCajaMontos().get(0).setMontoMes(totalEnero);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(1).setMontoMes(totalFebrero);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(2).setMontoMes(totalMarzo);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(3).setMontoMes(totalAbril);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(4).setMontoMes(totalMayo);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(5).setMontoMes(totalJunio);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(6).setMontoMes(totalJulio);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(7).setMontoMes(totalAgosto);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(8).setMontoMes(totalSeptiembre);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(9).setMontoMes(totalOctubre);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(10).setMontoMes(totalNoviembre);
			totalSubtituloFlujoCajaVO.getCajaMontos().get(11).setMontoMes(totalDiciembre);
			totalSubtituloFlujoCajaVO.setTotalMontos(totalSubtitulo29);
			flujoCajaSub29.add(totalSubtituloFlujoCajaVO);
			EstimacionFlujoCajaSubtituloSheetExcel estimacionFlujoCajaSubtituloSheetExcel = new EstimacionFlujoCajaSubtituloSheetExcel(header, subHeader, null);
			estimacionFlujoCajaSubtituloSheetExcel.setItems(flujoCajaSub29);
			generadorExcel.addSheet(estimacionFlujoCajaSubtituloSheetExcel, "29");
		}
		System.out.println("fin pestaña sub29");
		try {
			BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderDocEstimacionFlujoCaja.replace("{ANO}", String.valueOf(getAnoCurso()+1)));
			System.out.println("response AsignacionRecursosPercapitaSheetExcel --->" + response);
			TipoDocumento tipoDocumento = new TipoDocumento(TipoDocumentosProcesos.PLANTILLAPROPUESTA.getId());
			documentService.createDocumentPropuestaEstimacionFlujoCaja(programaAno, tipoDocumento, response.getNodeRef(), response.getFileName(), contenType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Fin generarPlanillaPropuesta demoro " + ((System.currentTimeMillis() - milliseconds) / 1000) +" segundos");
	}

	private SubtituloFlujoCajaVO getPercapitaFlujoCajaVO(String nombreServicio, Long percapitaMes) {
		SubtituloFlujoCajaVO subtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
		subtituloFlujoCajaVO.setServicio(nombreServicio);
		int idMes = 1;
		for(CajaMontoSummaryVO cajaMontoSummaryVO : subtituloFlujoCajaVO.getCajaMontos()){
			Mes mes = mesDAO.getMesPorID(idMes++);
			cajaMontoSummaryVO.setIdMes(mes.getIdMes());
			cajaMontoSummaryVO.setNombreMes(mes.getNombre());
			cajaMontoSummaryVO.setMontoMes(percapitaMes);
		}
		return subtituloFlujoCajaVO;
	}

	private SubtituloFlujoCajaVO getDesempenoDificialFlujoCajaVO(String nombreServicio, Integer desempeno) {
		SubtituloFlujoCajaVO subtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
		subtituloFlujoCajaVO.setServicio(nombreServicio);
		int idMes = 1;
		for(CajaMontoSummaryVO cajaMontoSummaryVO : subtituloFlujoCajaVO.getCajaMontos()){
			Mes mes = mesDAO.getMesPorID(idMes++);
			cajaMontoSummaryVO.setIdMes(mes.getIdMes());
			cajaMontoSummaryVO.setNombreMes(mes.getNombre());
			cajaMontoSummaryVO.setMontoMes(desempeno.longValue());
		}
		return subtituloFlujoCajaVO;
	}


	public void eliminarPlanillaPropuesta(Integer idProgramaAno) {
		System.out.println("eliminarPlanillaPropuesta-->"+idProgramaAno);
		List<DocumentoEstimacionflujocaja> documentosEstimacionFlujoCaja = estimacionFlujoCajaDAO.getDocumentByIDProgramaAnoTipoDocumento(idProgramaAno, TipoDocumentosProcesos.PLANTILLAPROPUESTA);
		if(documentosEstimacionFlujoCaja != null && documentosEstimacionFlujoCaja.size() > 0){
			int ultimoDoc = 0;
			for(DocumentoEstimacionflujocaja documentoEstimacionflujocaja : documentosEstimacionFlujoCaja){
				if(ultimoDoc == 0){
					String key = ((documentoEstimacionflujocaja.getIdDocumento().getNodeRef() == null) ? documentoEstimacionflujocaja.getIdDocumento().getPath() : documentoEstimacionflujocaja.getIdDocumento().getNodeRef().replace("workspace://SpacesStore/", ""));
					System.out.println("key->"+key);
					alfrescoService.delete(key);
					ultimoDoc++;
				}
				List<Integer> idDocumentosEstimacion = new ArrayList<Integer>();
				idDocumentosEstimacion.add(documentoEstimacionflujocaja.getId());
				estimacionFlujoCajaDAO.deleteDocumentoEstimacionflujoCaja(idDocumentosEstimacion);
				estimacionFlujoCajaDAO.deleteDocumento(documentoEstimacionflujocaja.getIdDocumento().getId());
			}
		}
	}

	@Asynchronous
	public void notificarUsuarioConsolidador(Integer idProgramaAno, String usuario) {
		// Iniciar el segundo proceso.
		ProgramaAno programaAno = programasDAO
				.getProgramaAnoByID(idProgramaAno);
		programaAno.setEstadoFlujoCaja(new EstadoPrograma(
				EstadosProgramas.REVISADO.getId()));
		String mailTo = usuarioDAO.getEmailByUsername(usuario);
		EmailVO emailVO = new EmailVO();
		emailVO.setContent("Revisar Consolidación de Estimación de Flujos de Caja <p> para el programaAno "
				+ programaAno.getPrograma().getNombre()
				+ " que se encuentra disponible en su Bandeja de Tareas.</p>");
		emailVO.setSubject("Revisar Consolidación de Estimación de Flujos de Caja");
		emailVO.setTo(mailTo);
		emailService.sendMail(emailVO);
		System.out.println("Fin enviar mail consolidador");
		programaAno.setEstado(new EstadoPrograma(EstadosProgramas.FINALIZADO.getId()));	
		System.out.println("Fin notificarUsuarioConsolidador");
	}


	public Integer eliminarOrdinarioFonasa(Integer idLineaProgramatica) {
		//Eliminar todos menos el ultimo.
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummary =  getLastDocumentSummaryEstimacionFlujoCajaType(idLineaProgramatica, TipoDocumentosProcesos.PLANTILLABORRADORORDINARIOPROGRAMACIONCAJA);

		ProgramaAno programaAno = programasDAO
				.getProgramaAnoByID(idLineaProgramatica);

		TipoDocumento tipoDocumento = new TipoDocumento(
				TipoDocumentosProcesos.PLANTILLABORRADORORDINARIOPROGRAMACIONCAJA
				.getId());
		List<DocumentoEstimacionflujocaja> lstDocEstimacionFlujoCaja = documentService
				.getDocumentEstimacionFlujoCajaByIDProgramaAnoTipoDocumento(
						programaAno, tipoDocumento);

		String keyUltimo = ((referenciaDocumentoSummary.getNodeRef() == null) ? referenciaDocumentoSummary.getPath() : referenciaDocumentoSummary.getNodeRef().replace("workspace://SpacesStore/", ""));

		for (DocumentoEstimacionflujocaja documentoEstimacionflujocaja : lstDocEstimacionFlujoCaja) {


			String key = ((documentoEstimacionflujocaja.getIdDocumento().getNodeRef() == null) ? documentoEstimacionflujocaja.getIdDocumento().getPath() : documentoEstimacionflujocaja.getIdDocumento().getNodeRef()
					.replace("workspace://SpacesStore/", ""));
			if (key.equals(keyUltimo)){
				alfrescoService.delete(key);
			}
		}

		return 0;
	}



	public void moveToAlfresco(Integer idLineaProgramatica,
			Integer referenciaDocumentoId, TipoDocumentosProcesos tipoDocumento,
			boolean versionFinal) {
		System.out.println("Buscando referenciaDocumentoId="
				+ referenciaDocumentoId);
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummary = documentService
				.getDocumentSummary(referenciaDocumentoId);

		System.out.println("Buscando referenciaDocumentoSummary="
				+ referenciaDocumentoSummary);
		if (referenciaDocumentoSummary != null) {
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contenType = mimemap
					.getContentType(referenciaDocumentoSummary.getPath()
							.toLowerCase());
			BodyVO response = alfrescoService.uploadDocument(new File(
					referenciaDocumentoSummary.getPath()), contenType,
					folderEstimacionFlujoCaja.replace("{ANO}", getAnoCurso().toString()));
			System.out.println("response upload template --->" + response);
			documentService.updateDocumentTemplate(
					referenciaDocumentoSummary.getId(), response.getNodeRef(),
					response.getFileName(), contenType);
			//			DistribucionInicialPercapita distribucionInicialPercapita = distribucionInicialPercapitaDAO
			//					.findById(idDistribucionInicialPercapita);
			ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idLineaProgramatica);
			documentService.createDocumentOrdinarioProgramacionEstimacionFlujoCaja(programaAno,tipoDocumento,referenciaDocumentoId,versionFinal);
			//documentService.createDocumentPercapita(
			//		distribucionInicialPercapita, tipoDocumento,
			//		referenciaDocumentoId, lastVersion);

		}

	}

	public ReferenciaDocumentoSummaryVO getLastDocumentSummaryEstimacionFlujoCajaType(
			Integer idProgramaAno,
			TipoDocumentosProcesos tipoDocumento) {
		return documentService.getLastDocumentoSummaryByEstimacionFlujoCajaType(idProgramaAno, tipoDocumento);

	}

	public ReferenciaDocumentoSummaryVO getLastDocumentSummaryEstimacionFlujoCajaTipoDocumento(TipoDocumentosProcesos tipoDocumento) {
		return documentService.getLastDocumentoSummaryByEstimacionFlujoCajaTipoDocumento(tipoDocumento);
	}

	//Obtiene el id del programa para el a�o siguiente para hacer la estimacion del flujo de caja.
	public Integer obtenerIdProgramaAno(Integer id) {
		// TODO Auto-generated method stub
		ProgramaAno programaAnoActual = programasDAO.getProgramaAnoByID(id);

		// Obtener el siguiente programa ano.
		ProgramaAno programaAnoSiguiente = programasDAO
				.getProgramaAnoByIDProgramaAno(programaAnoActual.getPrograma()
						.getId(), programaAnoActual.getAno().getAno()+1);

		Integer idProgramaAnoNuevo = 0;
		// CREAMOS EL PROGRAMA PARA EL A�O SIGUIENTE SI NO EXISTE.
		if (programaAnoSiguiente == null) {


			AnoEnCurso anoCurso = programasDAO.getAnoEnCursoById(programaAnoActual.getAno().getAno() + 1);
			if (anoCurso==null){
				AnoEnCurso anoC = new AnoEnCurso();
				anoC.setAno(programaAnoActual.getAno().getAno() + 1);
				anoC.setAsignacionAdultoMayor(0);
				anoC.setInflactor(0.0);
				anoC.setMontoPercapitalBasal(0);

				anoCurso = programasDAO.saveAnoCurso(anoC);
			}

			EstadoPrograma estadoProgramaCurso = new EstadoPrograma(EstadosProgramas.ENCURSO.getId());
			EstadoPrograma estadoProgramaIniciar = new EstadoPrograma(EstadosProgramas.SININICIAR.getId());
			programaAnoActual.setAno(anoCurso);
			programaAnoActual.setEstado(estadoProgramaIniciar);
			programaAnoActual.setEstadoFlujoCaja(estadoProgramaCurso);
			programaAnoActual.setIdProgramaAno(null);

			idProgramaAnoNuevo = programasDAO.saveProgramaAno(programaAnoActual,true);
			// estadoPrograma );
		}
		else
		{
			idProgramaAnoNuevo = programaAnoSiguiente.getIdProgramaAno();
		}

		return idProgramaAnoNuevo;
	}

	public Integer enviarOrdinarioFONASA(Integer idLineaProgramatica) {
		//System.out.println("enviarDecretoAporteEstatal--> "+borradorAporteEstatalId+" username="+username);

		ReferenciaDocumentoSummaryVO referenciaDocumentoSummary = getLastDocumentSummaryEstimacionFlujoCajaType(idLineaProgramatica, TipoDocumentosProcesos.PLANTILLABORRADORORDINARIOPROGRAMACIONCAJA);
		DocumentoVO documentDecretoAporteEstatalVO = documentService.getDocument(referenciaDocumentoSummary.getId());

		String fileNameDecretoAporteEstatal = tmpDirDoc + File.separator + documentDecretoAporteEstatalVO.getName();

		GeneradorWord generadorWordDecretoAporteEstatal = new GeneradorWord(fileNameDecretoAporteEstatal);
		try {
			generadorWordDecretoAporteEstatal.saveContent(documentDecretoAporteEstatalVO.getContent(), XWPFDocument.class);

			List<EmailService.Adjunto> adjuntos = new ArrayList<EmailService.Adjunto>();
			EmailService.Adjunto adjunto = new EmailService.Adjunto();
			adjunto.setDescripcion("Borrador Decreto Aporte Estatal");
			adjunto.setName(documentDecretoAporteEstatalVO.getName());
			adjunto.setUrl((new File(fileNameDecretoAporteEstatal)).toURI().toURL());
			adjuntos.add(adjunto);
			emailService.sendMail("asaavedra@nectia.com", "Borrador Decreto Aporte Estatal", "Estimado " + "usuario prueba" + ": <br /> <p> Se Adjunta Borrador Decreto Aporte Estatal</p>", adjuntos);
			System.out.println("enviarDecretoAporteEstatal se ejecuto correctamente");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 1;
		//adjunto.set

	}

	public List<SubtituloFlujoCajaVO> getMonitoreoServicioByProgramaAnoComponenteSubtitulo(Integer idProgramaAno, List<Integer> idComponentes, Subtitulo subtitulo, Boolean iniciarFlujoCaja) {
		List<SubtituloFlujoCajaVO> subtitulosFlujosCaja = new ArrayList<SubtituloFlujoCajaVO>();
		List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
		for(ServicioSalud servicioSalud : servicios){
			SubtituloFlujoCajaVO subtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
			subtituloFlujoCajaVO.setMarcoPresupuestario(0L);
			subtituloFlujoCajaVO.setIdServicio(servicioSalud.getId());
			subtituloFlujoCajaVO.setServicio(servicioSalud.getNombre());
			List<CajaMontoSummaryVO> cajaMontoMeses = new ArrayList<CajaMontoSummaryVO>();
			for(int mes = 1; mes <=12; mes++){
				Mes mesDTO = mesDAO.getMesPorID(mes);
				CajaMontoSummaryVO cajaMontoMes = new CajaMontoSummaryVO(null, mes, mesDTO.getNombre());
				cajaMontoMes.setMontoMes(0L);
				cajaMontoMeses.add(cajaMontoMes);
			}
			for(Establecimiento establecimiento : servicioSalud.getEstablecimientos()){
				Long marcoPresupuestario = 0L;
				for(Integer idComponente : idComponentes){
					marcoPresupuestario+=programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAno, idComponente, subtitulo.getId());
				}
				subtituloFlujoCajaVO.setMarcoPresupuestario(marcoPresupuestario);
				List<DetalleRemesas> detallesRemesas = remesasDAO.getDetalleRemesaEstimadaByProgramaAnoEstablecimientoSubtitulo(idProgramaAno, establecimiento.getId(), subtitulo.getId());
				if(detallesRemesas != null && detallesRemesas.size() > 0){
					for(DetalleRemesas detalleRemesas : detallesRemesas){
						if(detalleRemesas.getMes() != null){
							cajaMontoMeses.get((detalleRemesas.getMes().getIdMes()-1)).setMontoMes(cajaMontoMeses.get((detalleRemesas.getMes().getIdMes()-1)).getMontoMes() + detalleRemesas.getMontoRemesa().longValue());
						}
					}
				}
			}
			subtituloFlujoCajaVO.setCajaMontos(cajaMontoMeses);
			subtitulosFlujosCaja.add(subtituloFlujoCajaVO);
		}
		if(!iniciarFlujoCaja){
			Integer mesActual = Integer.parseInt(getMesCurso(true));
			for(SubtituloFlujoCajaVO subtituloFlujoCaja : subtitulosFlujosCaja){
				ConveniosSummaryVO convenioRecibido = new ConveniosSummaryVO(0, 0);
				TransferenciaSummaryVO transferenciasAcumulada = new TransferenciaSummaryVO(0, 0L);
				Long marcoPresupuestario = subtituloFlujoCaja.getMarcoPresupuestario();
				List<ConvenioComuna> convenios = conveniosDAO.getConveniosSummaryByProgramaAnoComponenteSubtitulo(idProgramaAno, subtituloFlujoCaja.getIdServicio(), idComponentes, subtitulo);
				//List<Remesa> remesas = remesasDAO.getRemesasSummaryByProgramaAnoComponenteSubtitulo(idProgramaAno, subtituloFlujoCaja.getIdServicio(), null, subtitulo);
				List<Remesa> remesas = new ArrayList<Remesa>();
				if(convenios != null && convenios.size() > 0){
					for(ConvenioComuna convenio : convenios){
						//convenioRecibido.setMonto(convenioRecibido.getMonto() + ((convenio.getMonto() == null)?0:convenio.getMonto()));
					}
				}
				if(!convenioRecibido.getMonto().equals(0)){
					Integer porcentaje = (int)((convenioRecibido.getMonto() * 100.0)/marcoPresupuestario);
					convenioRecibido.setPorcentaje(porcentaje);
				}
				subtituloFlujoCaja.setConvenioRecibido(convenioRecibido);
				Long montoRemesaMesActual = 0L;
				if(remesas != null && remesas.size() > 0){
					for(Remesa remesa : remesas){
						if(remesa.getIdmes().getIdMes() <= mesActual){
							Long montoRemesaMes = ((remesa.getValordia09() == null) ? 0L : remesa.getValordia09()) + ((remesa.getValordia24() == null) ? 0L : remesa.getValordia24()) +
									((remesa.getValordia28() == null) ? 0L : remesa.getValordia28());
							transferenciasAcumulada.setMonto(transferenciasAcumulada.getMonto() + montoRemesaMes);
							if(remesa.getIdmes().getIdMes() == mesActual){
								montoRemesaMesActual = montoRemesaMes;
							}
						}
					}
				}
				if(!transferenciasAcumulada.getMonto().equals(0)){
					Integer porcentaje = (int)((transferenciasAcumulada.getMonto() * 100.0)/marcoPresupuestario);
					transferenciasAcumulada.setPorcentaje(porcentaje);
				}
				subtituloFlujoCaja.setTransferenciaAcumulada(transferenciasAcumulada);
				for(CajaMontoSummaryVO cajaMontoSummaryVO : subtituloFlujoCaja.getCajaMontos()){
					if(mesActual.equals(cajaMontoSummaryVO.getIdMes()) ){
						cajaMontoSummaryVO.setMontoMes(montoRemesaMesActual);
						break;
					}
				}
			}
		}
		return subtitulosFlujosCaja;
	}

	public List<SubtituloFlujoCajaVO> getMonitoreoComunaByProgramaAnoComponenteSubtitulo(Integer idProgramaAno, List<Integer> idComponentes, Subtitulo subtitulo, Boolean iniciarFlujoCaja) {
		List<SubtituloFlujoCajaVO> subtitulosFlujosCaja = new ArrayList<SubtituloFlujoCajaVO>();
		List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
		for(ServicioSalud servicioSalud : servicios){
			SubtituloFlujoCajaVO subtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
			subtituloFlujoCajaVO.setIdServicio(servicioSalud.getId());
			subtituloFlujoCajaVO.setServicio(servicioSalud.getNombre());
			subtituloFlujoCajaVO.setMarcoPresupuestario(0L);
			List<CajaMontoSummaryVO> cajaMontoMeses = new ArrayList<CajaMontoSummaryVO>();
			for(int mes = 1; mes <=12; mes++){
				Mes mesDTO = mesDAO.getMesPorID(mes);
				CajaMontoSummaryVO cajaMontoMes = new CajaMontoSummaryVO(null, mes, mesDTO.getNombre());
				cajaMontoMes.setMontoMes(0L);
				cajaMontoMeses.add(cajaMontoMes);
			}
			for(Comuna comuna : servicioSalud.getComunas()){
				Long marcoPresupuestario = 0L;
				for(Integer idComponente : idComponentes){
					marcoPresupuestario+=programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAno, idComponente, subtitulo.getId());
				}
				subtituloFlujoCajaVO.setMarcoPresupuestario(marcoPresupuestario);
				List<DetalleRemesas> detallesRemesas = remesasDAO.getDetalleRemesaEstimadaByProgramaAnoComunaSubtitulo(idProgramaAno, comuna.getId(), subtitulo.getId());
				if(detallesRemesas != null && detallesRemesas.size() > 0){
					for(DetalleRemesas detalleRemesas : detallesRemesas){
						if(detalleRemesas.getMes() != null){
							cajaMontoMeses.get((detalleRemesas.getMes().getIdMes()-1)).setMontoMes(cajaMontoMeses.get((detalleRemesas.getMes().getIdMes()-1)).getMontoMes() + detalleRemesas.getMontoRemesa().longValue());
						}
					}
				}
			}
			subtituloFlujoCajaVO.setCajaMontos(cajaMontoMeses);
			subtitulosFlujosCaja.add(subtituloFlujoCajaVO);
		}
		if(!iniciarFlujoCaja){
			Integer mesActual = Integer.parseInt(getMesCurso(true));
			for(SubtituloFlujoCajaVO subtituloFlujoCaja : subtitulosFlujosCaja){
				ConveniosSummaryVO convenioRecibido = new ConveniosSummaryVO(0, 0);
				TransferenciaSummaryVO transferenciasAcumulada = new TransferenciaSummaryVO(0, 0L);
				Long marcoPresupuestario = subtituloFlujoCaja.getMarcoPresupuestario();
				List<ConvenioComuna> convenios = conveniosDAO.getConveniosSummaryByProgramaAnoComponenteSubtitulo(idProgramaAno, subtituloFlujoCaja.getIdServicio(), idComponentes, subtitulo);
				//List<Remesa> remesas = remesasDAO.getRemesasSummaryByProgramaAnoComponenteSubtitulo(idProgramaAno, subtituloFlujoCaja.getIdServicio(), null, subtitulo);
				List<Remesa> remesas = new ArrayList<Remesa>();
				if(convenios != null && convenios.size() > 0){
					for(ConvenioComuna convenio : convenios){
						//convenioRecibido.setMonto(convenioRecibido.getMonto() + ((convenio.getMonto() == null)?0:convenio.getMonto()));
					}
				}
				if(!convenioRecibido.getMonto().equals(0)){
					Integer porcentaje = (int)((convenioRecibido.getMonto() * 100.0)/marcoPresupuestario);
					convenioRecibido.setPorcentaje(porcentaje);
				}
				subtituloFlujoCaja.setConvenioRecibido(convenioRecibido);
				Long montoRemesaMesActual = 0L;
				if(remesas != null && remesas.size() > 0){
					for(Remesa remesa : remesas){
						if(remesa.getIdmes().getIdMes() <= mesActual){
							Long montoRemesaMes = ((remesa.getValordia09() == null) ? 0L : remesa.getValordia09()) + ((remesa.getValordia24() == null) ? 0L : remesa.getValordia24()) +
									((remesa.getValordia28() == null) ? 0L : remesa.getValordia28());
							transferenciasAcumulada.setMonto(transferenciasAcumulada.getMonto() + montoRemesaMes);
							if(remesa.getIdmes().getIdMes() == mesActual){
								montoRemesaMesActual = montoRemesaMes;
							}
						}
					}
				}
				if(!transferenciasAcumulada.getMonto().equals(0)){
					Integer porcentaje = (int)((transferenciasAcumulada.getMonto() * 100.0)/marcoPresupuestario);
					transferenciasAcumulada.setPorcentaje(porcentaje);
				}
				subtituloFlujoCaja.setTransferenciaAcumulada(transferenciasAcumulada);
				for(CajaMontoSummaryVO cajaMontoSummaryVO : subtituloFlujoCaja.getCajaMontos()){
					if(mesActual.equals(cajaMontoSummaryVO.getIdMes()) ){
						cajaMontoSummaryVO.setMontoMes(montoRemesaMesActual);
						break;
					}
				}
			}
		}
		return subtitulosFlujosCaja;
	}

	public List<SubtituloFlujoCajaVO> getMonitoreoByProgramaAnoComponentesComunaSubtitulo(Integer idProgramaAno, List<Integer> idComponentes, Integer idComuna, Subtitulo subtitulo, Boolean iniciarFlujoCaja) {
		List<SubtituloFlujoCajaVO> subtitulosFlujosCaja = new ArrayList<SubtituloFlujoCajaVO>(); 
		if(iniciarFlujoCaja){

		}
		if(!iniciarFlujoCaja){
			Integer mesActual = Integer.parseInt(getMesCurso(true));
			for(SubtituloFlujoCajaVO subtituloFlujoCaja : subtitulosFlujosCaja){
				ConveniosSummaryVO convenioRecibido = new ConveniosSummaryVO(0, 0);
				TransferenciaSummaryVO transferenciasAcumulada = new TransferenciaSummaryVO(0, 0L);
				Long marcoPresupuestario = subtituloFlujoCaja.getMarcoPresupuestario();
				List<ConvenioComuna> convenios = conveniosDAO.getConveniosSummaryByProgramaAnoComponenteSubtitulo(idProgramaAno, subtituloFlujoCaja.getIdServicio(), idComponentes, subtitulo);
				//List<Remesa> remesas = remesasDAO.getRemesasSummaryByProgramaAnoComponenteSubtitulo(idProgramaAno, subtituloFlujoCaja.getIdServicio(), null, subtitulo);
				List<Remesa> remesas = new ArrayList<Remesa>();
				if(convenios != null && convenios.size() > 0){
					for(ConvenioComuna convenio : convenios){
						//convenioRecibido.setMonto(convenioRecibido.getMonto() + ((convenio.getMonto() == null)?0:convenio.getMonto()));
					}
				}
				if(!convenioRecibido.getMonto().equals(0)){
					Integer porcentaje = (int)((convenioRecibido.getMonto() * 100.0)/marcoPresupuestario);
					convenioRecibido.setPorcentaje(porcentaje);
				}
				subtituloFlujoCaja.setConvenioRecibido(convenioRecibido);
				Long montoRemesaMesActual = 0L;
				if(remesas != null && remesas.size() > 0){
					for(Remesa remesa : remesas){
						if(remesa.getIdmes().getIdMes() <= mesActual){
							Long montoRemesaMes = new Long(((remesa.getValordia09() == null) ? 0 : remesa.getValordia09()) + ((remesa.getValordia24() == null) ? 0 : remesa.getValordia24()) +
									((remesa.getValordia28() == null) ? 0 : remesa.getValordia28()));
							transferenciasAcumulada.setMonto(transferenciasAcumulada.getMonto() + montoRemesaMes);
							if(remesa.getIdmes().getIdMes() == mesActual){
								montoRemesaMesActual = montoRemesaMes;
							}
						}
					}
				}
				if(!transferenciasAcumulada.getMonto().equals(0)){
					Integer porcentaje = (int)((transferenciasAcumulada.getMonto() * 100.0)/marcoPresupuestario);
					transferenciasAcumulada.setPorcentaje(porcentaje);
				}
				subtituloFlujoCaja.setTransferenciaAcumulada(transferenciasAcumulada);
				for(CajaMontoSummaryVO cajaMontoSummaryVO : subtituloFlujoCaja.getCajaMontos()){
					if(mesActual.equals(cajaMontoSummaryVO.getIdMes()) ){
						cajaMontoSummaryVO.setMontoMes(montoRemesaMesActual);
						break;
					}
				}
			}
		}
		return subtitulosFlujosCaja;
	}

	public List<SubtituloFlujoCajaVO> getConvenioRemesaByProgramaAnoComponenteSubtitulo(Integer idProgramaAno, List<Integer> idComponentes, Subtitulo subtitulo) {
		List<SubtituloFlujoCajaVO> subtitulosFlujosCaja = new ArrayList<SubtituloFlujoCajaVO>(); 
		List<Caja> flujosCaja = cajaDAO.getConvenioRemesaByProgramaAnoComponenteSubtitulo(idProgramaAno, idComponentes, subtitulo);
		if(flujosCaja != null && flujosCaja.size() > 0){
			for(Caja caja : flujosCaja){
				SubtituloFlujoCajaVO subtituloFlujoCajaVO = new SubtituloFlujoCajaMapper().getBasic(caja);
				int indexOf = subtitulosFlujosCaja.indexOf(subtituloFlujoCajaVO);
				if(indexOf == -1){
					subtitulosFlujosCaja.add(subtituloFlujoCajaVO);
				}else{
					for(int elemento = 0; elemento < subtituloFlujoCajaVO.getCajaMontos().size(); elemento++){
						long nuevoMonto = subtitulosFlujosCaja.get(indexOf).getCajaMontos().get(elemento).getMontoMes();
						nuevoMonto += subtituloFlujoCajaVO.getCajaMontos().get(elemento).getMontoMes();
						subtitulosFlujosCaja.get(indexOf).getCajaMontos().get(elemento).setMontoMes(nuevoMonto);
					}
				}
			}
		}
		Integer mesActual = Integer.parseInt(getMesCurso(true));
		for(SubtituloFlujoCajaVO subtituloFlujoCaja : subtitulosFlujosCaja){
			TransferenciaSummaryVO transferenciasAcumulada = new TransferenciaSummaryVO(0, 0L);
			//List<Remesa> remesas = remesasDAO.getRemesasSummaryByProgramaAnoComponenteSubtitulo(idProgramaAno, subtituloFlujoCaja.getIdServicio(), null, subtitulo);
			List<Remesa> remesas = new ArrayList<Remesa>();
			if((remesas != null) && (remesas.size() > 0) && (mesActual > 2)){
				int elementosConRemesa = 0;
				for(int elemento = 0; elemento < remesas.size(); elemento++){
					if(remesas.get(elemento).getIdmes().getIdMes() <= (mesActual - 2)){
						Long montoRemesaMes = new Long(((remesas.get(elemento).getValordia09() == null) ? 0 : remesas.get(elemento).getValordia09()) + ((remesas.get(elemento).getValordia24() == null) ? 0 : remesas.get(elemento).getValordia24()) +
								((remesas.get(elemento).getValordia28() == null) ? 0 : remesas.get(elemento).getValordia28()));
						transferenciasAcumulada.setMonto(transferenciasAcumulada.getMonto() + montoRemesaMes);
						subtituloFlujoCaja.getCajaMontos().get(elemento).setMontoMes(montoRemesaMes);
						elementosConRemesa++;
					}
				}
				for(int elemento = elementosConRemesa; elemento < subtituloFlujoCaja.getCajaMontos().size(); elemento++){
					subtituloFlujoCaja.getCajaMontos().get(elemento).setMontoMes(0L);
				}
			}else{
				for(CajaMontoSummaryVO cajaMontoSummaryVO : subtituloFlujoCaja.getCajaMontos()){
					cajaMontoSummaryVO.setMontoMes(0L);
				}
			}
			subtituloFlujoCaja.setTransferenciaAcumulada(transferenciasAcumulada);
		}
		return subtitulosFlujosCaja;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Integer obtenerIdProgramaAnoSiguiente(Integer idProgramaAno, Integer anoSiguiente) {
		ProgramaAno programaAnoActual = programasDAO.getProgramaAnoByID(idProgramaAno);
		ProgramaAno programaAnoSiguiente = programasDAO.getProgramaAnoSiguiente(programaAnoActual.getPrograma().getId(), anoSiguiente);
		if(programaAnoSiguiente == null){
			//crear 
			programaAnoSiguiente = new ProgramaAno();
			AnoEnCurso anoSiguienteDTO = programasDAO.getAnoEnCursoById(anoSiguiente);
			if(anoSiguienteDTO == null) {
				AnoEnCurso anoActualDTO = programasDAO.getAnoEnCursoById(anoSiguiente-1);
				anoSiguienteDTO = new AnoEnCurso();
				anoSiguienteDTO.setAno(anoSiguiente);
				anoSiguienteDTO.setMontoPercapitalBasal(anoActualDTO.getMontoPercapitalBasal());
				anoSiguienteDTO.setAsignacionAdultoMayor(anoActualDTO.getAsignacionAdultoMayor());
				anoSiguienteDTO.setInflactor(anoActualDTO.getInflactor());
				anoSiguienteDTO.setInflactorMarcoPresupuestario(anoActualDTO.getInflactorMarcoPresupuestario());
				programasDAO.saveAnoCurso(anoSiguienteDTO);
			}
			programaAnoSiguiente.setAno(anoSiguienteDTO);
			programaAnoSiguiente.setEstado(new EstadoPrograma(1));
			programaAnoSiguiente.setEstadoFlujoCaja(new EstadoPrograma(1));
			programaAnoSiguiente.setPrograma(programaAnoActual.getPrograma());
			programasDAO.save(programaAnoSiguiente);
		} 
		return programaAnoSiguiente.getIdProgramaAno();
	}

	public void actualizarMonitoreoServicioSubtituloFlujoCaja(Integer idProgramaAno, Integer IdServicio, CajaMontoSummaryVO cajaMontoSummary, Subtitulo subtitulo) {
		List<Caja> cajas = cajaDAO.getByProgramaAnoServicioSubtitulo(idProgramaAno, IdServicio, subtitulo);
		System.out.println("idProgramaAno ="+idProgramaAno+" IdServicio="+ IdServicio +" subtitulo="+subtitulo.getNombre());
		if(cajas != null && cajas.size() > 0){
			for(Caja caja : cajas){
				System.out.println("caja.getId()=" + caja.getId() + "caja.getIdComponente().getId()=" + caja.getIdComponente().getId() + "caja.getIdComponente().getNombre()=" + caja.getIdComponente().getNombre());
				float pesoComponente = caja.getIdComponente().getPeso();
				for(CajaMonto cajaMonto : caja.getCajaMontos()){
					if(cajaMontoSummary.getIdMes().equals(cajaMonto.getMes().getIdMes())){
						if(((int)pesoComponente) == 100){
							cajaMonto.setMonto(cajaMontoSummary.getMontoMes().intValue());
						}else{
							int montoActualizado = (int)((pesoComponente * cajaMontoSummary.getMontoMes())/100.0);
							cajaMonto.setMonto(montoActualizado);
						}
					}
				}
			}
		}
		MarcoPresupuestario marco = cajaDAO.getMarcoPresupuestarioByProgramaAnoServicio(idProgramaAno, IdServicio);
		if(marco != null){
			marco.setReparosMarcoPresupuestario(true);
		}
	}


	public List<SubtituloFlujoCajaVO> getPercapitaByProgramaAno(Integer idProgramaAno, Integer idServicio) {
		DistribucionInicialPercapita  distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(getAnoCurso());
		List<AntecendentesComunaCalculado> antecendentesComunasCalculado = antecedentesComunaDAO.findAntecedentesComunaCalculadosByDistribucionInicialPercapitaServicio(distribucionInicialPercapita.getIdDistribucionInicialPercapita(), idServicio);
		List<SubtituloFlujoCajaVO> resultado = new ArrayList<SubtituloFlujoCajaVO>();
		Map<Integer, Long> percapitaServicio = new HashMap<Integer, Long>();

		System.out.println("antecendentesComunasCalculado.size() --> "+antecendentesComunasCalculado.size());
		if(antecendentesComunasCalculado != null && antecendentesComunasCalculado.size()>0){
			for(AntecendentesComunaCalculado antecendentesComunaCalculado : antecendentesComunasCalculado){
				if(percapitaServicio.containsKey(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId())){
					Long valor = percapitaServicio.get(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId());
					valor+= antecendentesComunaCalculado.getPercapitaAno();
					percapitaServicio.put(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId(), valor);
				}else{
					percapitaServicio.put(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId(), antecendentesComunaCalculado.getPercapitaAno());
				}
			}
		}


		for (Integer key : percapitaServicio.keySet()) {
			Long valor = percapitaServicio.get(key); //ojo que en key se guarda el 
			SubtituloFlujoCajaVO subtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
			ServicioSalud servicioSalud = servicioSaludService.getServicioSaludPorID(key);
			if(servicioSalud != null) {
				subtituloFlujoCajaVO.setServicio(servicioSalud.getNombre());
				subtituloFlujoCajaVO.setIdServicio(servicioSalud.getId());
			}
			List<CajaMontoSummaryVO> cajaMontos = new ArrayList<CajaMontoSummaryVO>();
			Long montoMensual = valor / 12;
			for(int i=0; i<12; i++){
				CajaMontoSummaryVO caja = new CajaMontoSummaryVO();
				caja.setIdMes(i+1);
				caja.setMontoMes(montoMensual);
				cajaMontos.add(i, caja);
			}			 

			subtituloFlujoCajaVO.setCajaMontos(cajaMontos);
			subtituloFlujoCajaVO.setMarcoPresupuestario(montoMensual*12);
			resultado.add(subtituloFlujoCajaVO);

		} 
		return resultado;

	}
	public List<SubtituloFlujoCajaVO> getPercapitaByProgramaAno(Integer idProgramaAno) {

		DistribucionInicialPercapita  distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(getAnoCurso());
		Integer id_disperc = distribucionInicialPercapita.getIdDistribucionInicialPercapita();
		System.out.println("id_disperc --> "+id_disperc);


		List<AntecendentesComunaCalculado> antecendentesComunasCalculado = antecedentesComunaDAO.findAntecedentesComunaCalculadosByDistribucionInicialPercapitaVigente(distribucionInicialPercapita.getIdDistribucionInicialPercapita());
		List<SubtituloFlujoCajaVO> resultado = new ArrayList<SubtituloFlujoCajaVO>();
		Map<Integer, Long> percapitaServicio = new HashMap<Integer, Long>();

		System.out.println("antecendentesComunasCalculado.size() --> "+antecendentesComunasCalculado.size());
		if(antecendentesComunasCalculado != null && antecendentesComunasCalculado.size()>0){
			for(AntecendentesComunaCalculado antecendentesComunaCalculado : antecendentesComunasCalculado){
				if(percapitaServicio.containsKey(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId())){
					Long valor = ((percapitaServicio.get(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId()) == null) ? 0 : percapitaServicio.get(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId()));
					valor+= ((antecendentesComunaCalculado.getPercapitaAno() == null) ? 0 : antecendentesComunaCalculado.getPercapitaAno());
					percapitaServicio.put(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId(), valor);
				}else{
					percapitaServicio.put(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId(), antecendentesComunaCalculado.getPercapitaAno());
				}
			}
		}


		for (Integer key : percapitaServicio.keySet()) {
			Long valor = percapitaServicio.get(key); //ojo que en key se guarda el 
			SubtituloFlujoCajaVO subtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
			ServicioSalud servicioSalud = servicioSaludService.getServicioSaludPorID(key);
			if(servicioSalud != null) {
				subtituloFlujoCajaVO.setServicio(servicioSalud.getNombre());
				subtituloFlujoCajaVO.setIdServicio(servicioSalud.getId());
			}
			List<CajaMontoSummaryVO> cajaMontos = new ArrayList<CajaMontoSummaryVO>();
			Long montoMensual = valor / 12;
			for(int i=0; i<12; i++){
				CajaMontoSummaryVO caja = new CajaMontoSummaryVO();
				caja.setIdMes(i+1);
				caja.setMontoMes(montoMensual);
				cajaMontos.add(i, caja);
			}			 

			subtituloFlujoCajaVO.setCajaMontos(cajaMontos);
			subtituloFlujoCajaVO.setMarcoPresupuestario(montoMensual*12);
			resultado.add(subtituloFlujoCajaVO);

		} 

		return resultado;
	}

	public DocumentoEstimacionflujocaja getDocumentByTypProgramaAno(Integer idProgramaAno, TipoDocumentosProcesos tipoDocumentoProceso){
		return documentDAO.getDocumentByTypProgramaAno(idProgramaAno, tipoDocumentoProceso);

	}

	public List<ServiciosVO> getServicioByProgramaAnoSubtitulo(Integer valorComboPrograma, Subtitulo subtitulo) {
		List<ServiciosVO> servicios = new ArrayList<ServiciosVO>();
		List<Caja> cajas = cajaDAO.getCajaByProgramaAnoSubtitulo(valorComboPrograma, subtitulo);
		if(cajas != null && cajas.size() > 0){
			for(Caja caja : cajas){
				if(caja.getServicio() != null){
					ServiciosVO serviciosVO = new ServicioMapper().getBasic(caja.getServicio());
					servicios.add(serviciosVO);
				}
			}
		}
		return servicios;
	}

	public List<SubtituloFlujoCajaVO> getPercapitaByAno() {
		DistribucionInicialPercapita  distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(getAnoCurso());
		System.out.println("distribucionInicialPercapita.getIdDistribucionInicialPercapita() --> "+distribucionInicialPercapita.getIdDistribucionInicialPercapita());
		List<AntecendentesComunaCalculado> antecendentesComunasCalculado = antecedentesComunaDAO.findAntecedentesComunaCalculadosByDistribucionInicialPercapitaVigente(distribucionInicialPercapita.getIdDistribucionInicialPercapita());

		List<SubtituloFlujoCajaVO> resultado = new ArrayList<SubtituloFlujoCajaVO>();
		Map<Integer, Long> percapitaServicio = new HashMap<Integer, Long>();
		System.out.println("antecendentesComunasCalculado.size() --> "+antecendentesComunasCalculado.size());
		if(antecendentesComunasCalculado != null && antecendentesComunasCalculado.size()>0){
			for(AntecendentesComunaCalculado antecendentesComunaCalculado : antecendentesComunasCalculado){
				if(percapitaServicio.containsKey(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId())){
					Long valor = ((percapitaServicio.get(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId()) == null) ? 0 : percapitaServicio.get(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId()));
					valor+= ((antecendentesComunaCalculado.getPercapitaMes() == null) ? 0 : antecendentesComunaCalculado.getPercapitaMes());
					percapitaServicio.put(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId(), valor);
				}else{
					percapitaServicio.put(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId(), antecendentesComunaCalculado.getPercapitaMes());
				}
			}
		}
		for (Integer key : percapitaServicio.keySet()) {
			Long montoMensual = percapitaServicio.get(key);
			SubtituloFlujoCajaVO subtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
			ServicioSalud servicioSalud = servicioSaludService.getServicioSaludPorID(key);
			if(servicioSalud != null) {
				subtituloFlujoCajaVO.setServicio(servicioSalud.getNombre());
				subtituloFlujoCajaVO.setIdServicio(servicioSalud.getId());
			}
			List<CajaMontoSummaryVO> cajaMontos = new ArrayList<CajaMontoSummaryVO>();
			for(int i=0; i<12 ; i++){
				CajaMontoSummaryVO caja = new CajaMontoSummaryVO();
				caja.setIdMes(i+1);
				caja.setMontoMes(montoMensual);
				cajaMontos.add(caja);
			}			 

			subtituloFlujoCajaVO.setCajaMontos(cajaMontos);
			subtituloFlujoCajaVO.setMarcoPresupuestario(0L);
			resultado.add(subtituloFlujoCajaVO);

		} 
		return resultado;
	}

	public List<SubtituloFlujoCajaVO> getPercapitaByAnoServicio(Integer idServicio) {
		DistribucionInicialPercapita  distribucionInicialPercapita = distribucionInicialPercapitaDAO.findLast(getAnoCurso());
		System.out.println("distribucionInicialPercapita.getIdDistribucionInicialPercapita() --> "+distribucionInicialPercapita.getIdDistribucionInicialPercapita());
		List<AntecendentesComunaCalculado> antecendentesComunasCalculado = antecedentesComunaDAO.findAntecedentesComunaCalculadosByDistribucionInicialPercapitaServicio(distribucionInicialPercapita.getIdDistribucionInicialPercapita(),  idServicio);

		List<SubtituloFlujoCajaVO> resultado = new ArrayList<SubtituloFlujoCajaVO>();
		Map<Integer, Long> percapitaServicio = new HashMap<Integer, Long>();
		System.out.println("antecendentesComunasCalculado.size() --> "+antecendentesComunasCalculado.size());
		if(antecendentesComunasCalculado != null && antecendentesComunasCalculado.size() > 0){
			for(AntecendentesComunaCalculado antecendentesComunaCalculado : antecendentesComunasCalculado){
				if(percapitaServicio.containsKey(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId())){
					Long valor = ((percapitaServicio.get(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId()) == null) ? 0 : percapitaServicio.get(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId()));
					valor+= ((antecendentesComunaCalculado.getPercapitaMes() == null) ? 0 : antecendentesComunaCalculado.getPercapitaMes());
					percapitaServicio.put(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId(), valor);
				}else{
					percapitaServicio.put(antecendentesComunaCalculado.getAntecedentesComuna().getIdComuna().getServicioSalud().getId(), antecendentesComunaCalculado.getPercapitaMes());
				}
			}
		}
		for (Integer key : percapitaServicio.keySet()) {
			Long montoMensual = percapitaServicio.get(key);
			SubtituloFlujoCajaVO subtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
			ServicioSalud servicioSalud = servicioSaludService.getServicioSaludPorID(key);
			if(servicioSalud != null) {
				subtituloFlujoCajaVO.setServicio(servicioSalud.getNombre());
				subtituloFlujoCajaVO.setIdServicio(servicioSalud.getId());
			}
			List<CajaMontoSummaryVO> cajaMontos = new ArrayList<CajaMontoSummaryVO>();
			for(int i=0; i<12 ; i++){
				CajaMontoSummaryVO caja = new CajaMontoSummaryVO();
				caja.setIdMes(i+1);
				caja.setMontoMes(montoMensual);
				cajaMontos.add(caja);
			}			 

			subtituloFlujoCajaVO.setCajaMontos(cajaMontos);
			subtituloFlujoCajaVO.setMarcoPresupuestario(0L);
			resultado.add(subtituloFlujoCajaVO);

		} 
		return resultado;
	}

	public boolean tieneMarcosConReparos(Integer idProgramaAno) {
		List<MarcoPresupuestario> marcosPresupuestarios = cajaDAO.getMarcoPresupuestarioByProgramaAnoReparos(idProgramaAno, Boolean.TRUE);
		boolean tieneMarcosConReparos = false;
		if(marcosPresupuestarios != null && marcosPresupuestarios.size() > 0){
			tieneMarcosConReparos = true;
		}
		return tieneMarcosConReparos;
	}

	public boolean existeDistribucionRecursos(Integer idProgramaAno) {
		ProgramaVO programa = programasService.getProgramaAno(idProgramaAno);
		if(!programa.getEstado().getId().equals(EstadosProgramas.FINALIZADO.getId())){
			return false;
		}
		return true;
	}

	public int countAntecendentesComunaCalculadoVigente() {
		return antecedentesComunaDAO.countAntecendentesComunaCalculadoVigente(getAnoCurso() + 1);
	}

	public ReferenciaDocumentoSummaryVO getDocumentEstimacionFlujoCajaByType(Integer idProgramaAno, TipoDocumentosProcesos tipoDocumento) {
		System.out.println("idProgramaAno->"+idProgramaAno);
		System.out.println("tipoDocumento->"+tipoDocumento.getId());
		ReferenciaDocumento referencia = fileDAO.getLastDocumentByTypeEstimacionFlujoCaja(idProgramaAno, tipoDocumento);
		return new ReferenciaDocumentoMapper().getSummary(referencia);
	}

}
