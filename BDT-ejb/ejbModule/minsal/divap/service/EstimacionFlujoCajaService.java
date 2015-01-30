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
import java.util.Set;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import minsal.divap.dao.AnoDAO;
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
import minsal.divap.dao.TipoSubtituloDAO;
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.doc.GeneradorResolucionAporteEstatal;
import minsal.divap.doc.GeneradorWord;
import minsal.divap.enums.EstadosConvenios;
import minsal.divap.enums.EstadosProgramas;
import minsal.divap.enums.Subtitulo;
import minsal.divap.enums.TareasSeguimiento;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.enums.TiposDestinatarios;
import minsal.divap.excel.GeneradorExcel;
import minsal.divap.excel.impl.EstimacionFlujoCajaConsolidadorSheetExcel;
import minsal.divap.excel.impl.EstimacionFlujoCajaSheetExcel;
import minsal.divap.excel.impl.EstimacionFlujoCajaSubtituloSheetExcel;
import minsal.divap.model.mappers.ReferenciaDocumentoMapper;
import minsal.divap.model.mappers.ServicioMapper;
import minsal.divap.util.Util;
import minsal.divap.vo.AdjuntosVO;
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
import minsal.divap.vo.ReporteEmailsEnviadosVO;
import minsal.divap.vo.ResumenConsolidadorVO;
import minsal.divap.vo.SeguimientoVO;
import minsal.divap.vo.ServiciosVO;
import minsal.divap.vo.SubtituloFlujoCajaVO;
import minsal.divap.vo.TransferenciaSummaryVO;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.jboss.logging.Logger;

import cl.minsal.divap.model.AnoEnCurso;
import cl.minsal.divap.model.AntecendentesComunaCalculado;
import cl.minsal.divap.model.Caja;
import cl.minsal.divap.model.CajaMonto;
import cl.minsal.divap.model.CajaMontoPK;
import cl.minsal.divap.model.Componente;
import cl.minsal.divap.model.ComponenteSubtitulo;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.Convenio;
import cl.minsal.divap.model.ConvenioComuna;
import cl.minsal.divap.model.ConvenioComunaComponente;
import cl.minsal.divap.model.ConvenioServicio;
import cl.minsal.divap.model.ConvenioServicioComponente;
import cl.minsal.divap.model.Cuota;
import cl.minsal.divap.model.DetalleRemesas;
import cl.minsal.divap.model.DistribucionInicialPercapita;
import cl.minsal.divap.model.DocumentoEstimacionflujocaja;
import cl.minsal.divap.model.Establecimiento;
import cl.minsal.divap.model.EstadoPrograma;
import cl.minsal.divap.model.FlujoCajaConsolidador;
import cl.minsal.divap.model.Mes;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.ProgramaSubtituloComponentePeso;
import cl.minsal.divap.model.ReferenciaDocumento;
import cl.minsal.divap.model.ReporteEmailsAdjuntos;
import cl.minsal.divap.model.ReporteEmailsConvenio;
import cl.minsal.divap.model.ReporteEmailsDestinatarios;
import cl.minsal.divap.model.ReporteEmailsFlujoCajaConsolidador;
import cl.minsal.divap.model.Seguimiento;
import cl.minsal.divap.model.ServicioSalud;
import cl.minsal.divap.model.TipoDocumento;
import cl.minsal.divap.model.TipoSubtitulo;
import cl.minsal.divap.model.Usuario;

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
	private TipoSubtituloDAO subtituloDAO;
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
	private AnoDAO anoDAO;
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
	private static final Logger LOGGER = Logger.getLogger(EstimacionFlujoCajaService.class);

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
			programaAno.setEstadoFlujoCaja(new EstadoPrograma(EstadosProgramas.CALCULARPROPUESTA.getId()));
			Map<Integer, List<Integer>> componentesPorSubtitulo = new HashMap<Integer, List<Integer>>();
			for(Componente componente : programaAno.getPrograma().getComponentes()){
				for(ComponenteSubtitulo componenteSubtitulo : componente.getComponenteSubtitulos()){
					if(componentesPorSubtitulo.get(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo()) == null){
						List<Integer> componentes = new ArrayList<Integer>();
						componentes.add(componente.getId());
						componentesPorSubtitulo.put(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo(), componentes);
					}else{
						componentesPorSubtitulo.get(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo()).add(componente.getId());
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
			List<Cuota> cuotasPrograma = estimacionFlujoCajaDAO.getCuotasByProgramaAno(idProgramaAno);
			for (Map.Entry<Integer, List<Integer>> entry : componentesPorSubtitulo.entrySet()) { 
				List<Integer> componentes = entry.getValue();
				for(ServicioSalud servicioSalud : servicios){
					int componentesSize = componentes.size();
					Long marcoPresupuestarioTotalAno = null;
					int cantidadIteraciones = 0;
					int porcentajeAcumulado = 0;
					for(Integer componente : componentes){
						Long marcoPresupuestatarioServicio = 0L;
						Long marcoPresupuestatarioServicioAnoActual = 0L;
						System.out.println("Procesando Servicio=" + servicioSalud.getId() + " componente=" + componente + " Subtitulo=" + entry.getKey());
						if(Subtitulo.SUBTITULO24.getId().equals(entry.getKey())){
							if(marcoPresupuestarioTotalAno == null){
								marcoPresupuestarioTotalAno =  programasDAO.getMPComunasServicioProgramaAnoComponenteSubtitulo(servicioSalud.getId(), idProgramaAno, componentes, Subtitulo.SUBTITULO24.getId());
							}
							for(Comuna comuna : servicioSalud.getComunas()){
								Long marcoPresupuestario = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAno, componente, Subtitulo.SUBTITULO24.getId());
								marcoPresupuestatarioServicio += marcoPresupuestario;
								Long marcoPresupuestarioAnoActual = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), programaAnoActual.getIdProgramaAno(), componente, Subtitulo.SUBTITULO24.getId());
								marcoPresupuestatarioServicioAnoActual += marcoPresupuestarioAnoActual;
							}
							ProgramaSubtituloComponentePeso programaSubtituloComponentePeso = programasDAO.getProgramaSubtituloComponentePesoByProgramaServicioComponenteSubtitulo(idProgramaAno, servicioSalud.getId(), componente, Subtitulo.SUBTITULO24.getId());
							if(programaSubtituloComponentePeso == null){
								programaSubtituloComponentePeso = new ProgramaSubtituloComponentePeso();
								Componente componenteDTO = componenteDAO.getComponenteByID(componente);
								TipoSubtitulo tipoSubtitulo = new TipoSubtitulo(Subtitulo.SUBTITULO24.getId());
								programaSubtituloComponentePeso.setComponente(componenteDTO);
								programaSubtituloComponentePeso.setPrograma(programaAno);
								programaSubtituloComponentePeso.setServicio(servicioSalud);
								programaSubtituloComponentePeso.setSubtitulo(tipoSubtitulo);
								programasDAO.save(programaSubtituloComponentePeso);
							}
							if(marcoPresupuestatarioServicio != 0){
								if(cantidadIteraciones != (componentesSize - 1)){
									int peso = (int)((marcoPresupuestatarioServicio * 100.0) / marcoPresupuestarioTotalAno);
									porcentajeAcumulado += peso;
									programaSubtituloComponentePeso.setPeso(peso);
								}else{
									int peso = 100 - porcentajeAcumulado;
									porcentajeAcumulado = 100;
									programaSubtituloComponentePeso.setPeso(peso);
								}
							}
							else{
								programaSubtituloComponentePeso.setPeso(0);
							}
						}else{
							if(marcoPresupuestarioTotalAno == null){
								marcoPresupuestarioTotalAno =  programasDAO.getMPEstablecimientosServicioProgramaAnoComponenteSubtitulo(servicioSalud.getId(), idProgramaAno, componentes, entry.getKey());
							}
							for(Establecimiento establecimiento : servicioSalud.getEstablecimientos()){
								Long marcoPresupuestario = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAno, componente, entry.getKey());
								marcoPresupuestatarioServicio += marcoPresupuestario;
								Long marcoPresupuestarioAnoActual = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), programaAnoActual.getIdProgramaAno(), componente, entry.getKey());
								marcoPresupuestatarioServicioAnoActual += marcoPresupuestarioAnoActual;
							}
							ProgramaSubtituloComponentePeso programaSubtituloComponentePeso = programasDAO.getProgramaSubtituloComponentePesoByProgramaServicioComponenteSubtitulo(idProgramaAno, servicioSalud.getId(), componente, Subtitulo.SUBTITULO24.getId());
							if(programaSubtituloComponentePeso == null){
								programaSubtituloComponentePeso = new ProgramaSubtituloComponentePeso();
								Componente componenteDTO = componenteDAO.getComponenteByID(componente);
								TipoSubtitulo tipoSubtitulo = new TipoSubtitulo(entry.getKey());
								programaSubtituloComponentePeso.setComponente(componenteDTO);
								programaSubtituloComponentePeso.setPrograma(programaAno);
								programaSubtituloComponentePeso.setServicio(servicioSalud);
								programaSubtituloComponentePeso.setSubtitulo(tipoSubtitulo);
								programasDAO.save(programaSubtituloComponentePeso);
							}
							if(marcoPresupuestatarioServicio != 0){
								if(cantidadIteraciones != (componentesSize - 1)){
									int peso = (int)((marcoPresupuestatarioServicio * 100.0) / marcoPresupuestarioTotalAno);
									porcentajeAcumulado += peso;
									programaSubtituloComponentePeso.setPeso(peso);
								}else{
									int peso = 100 - porcentajeAcumulado;
									porcentajeAcumulado = 100;
									programaSubtituloComponentePeso.setPeso(peso);
								}
							}else{
								programaSubtituloComponentePeso.setPeso(0);
							}
						}
						if(marcoPresupuestatarioServicio != 0){
							Caja cajaActual = cajaDAO.getByServicioProgramaAnoComponenteSubtitulo(servicioSalud.getId(), programaAnoActual.getIdProgramaAno(), componente, Subtitulo.getById(entry.getKey()));
							if(cajaActual != null){
								System.out.println("Se distribuye de la misma forma que el año en curso");
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
								System.out.println("no hay referencia de la caja para el año en curso, se distribuye por cuotas");
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
						}else{
							System.out.println("El marco presupuestario es cera para servicioSalud="+servicioSalud.getId()+ " programa=" + idProgramaAno+" componente="+componente+" subtitulo="+entry.getKey());
							Caja caja = new Caja();
							caja.setCajaInicial(true);
							Componente componenteDTO = componenteDAO.getComponenteByID(componente);
							caja.setIdComponente(componenteDTO);
							caja.setIdSubtitulo(new TipoSubtitulo(entry.getKey()));
							caja.setPrograma(programaAno);
							caja.setServicio(servicioSalud);
							cajaDAO.save(caja);
							for(Integer mes = 1; mes <= 12; mes++){
								CajaMonto cajaMonto = new CajaMonto();
								CajaMontoPK cajaMontoPK = new CajaMontoPK(caja.getId(), mes);
								cajaMonto.setCajaMontoPK(cajaMontoPK);
								cajaMonto.setMes(new Mes(mes));
								cajaMonto.setCaja(caja);
								cajaMonto.setMonto(0);
								cajaDAO.save(cajaMonto);
							}
						}
						cantidadIteraciones++;
					}
				}
			}
			System.out.println("Fin subtitulo 21/22/24/29");
			System.out.println("Tardo " + ((System.currentTimeMillis()-millisecons)/1000) + " segundos");
			programaAno.setEstadoFlujoCaja(new EstadoPrograma(EstadosProgramas.CALCULARPROPUESTA.getId()));
		}else{
			//MODIFICACION NO HAY CALCULO DE PROPUESTA
		}
	}

	public Integer getIdPlantillaProgramacion() {
		Integer plantillaId = null;// documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLAPROGRAMACION);
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
			mesCurso = "4";
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
		System.out.println("Inicio generarPlanillaPropuesta idProgramaAno="+idProgramaAno);
		ProgramaAno programaAno = programasDAO.getProgramaAnoByID(idProgramaAno);
		List<CajaMonto> cajaMontosReparos = cajaDAO.getCajaMontoReparosByProgramaAno(idProgramaAno);
		if(cajaMontosReparos != null && cajaMontosReparos.size() > 0){
			for(CajaMonto cajaMonto : cajaMontosReparos){
				System.out.println("Quitando el reparo a cajaMonto");
				cajaMonto.setReparos(false);
			}
		}

		Map<Integer, List<Integer>> componentesPorSubtitulo = new HashMap<Integer, List<Integer>>();
		for(Componente componente : programaAno.getPrograma().getComponentes()){
			for(ComponenteSubtitulo componenteSubtitulo : componente.getComponenteSubtitulos()){
				if(componentesPorSubtitulo.get(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo()) == null){
					List<Integer> componentes = new ArrayList<Integer>();
					componentes.add(componente.getId());
					componentesPorSubtitulo.put(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo(), componentes);
				}else{
					componentesPorSubtitulo.get(componenteSubtitulo.getSubtitulo().getIdTipoSubtitulo()).add(componente.getId());
				}
			}
		}
		List<SubtituloFlujoCajaVO> flujoCajaSub21 = null;
		if(componentesPorSubtitulo.get(Subtitulo.SUBTITULO21.getId()) != null){
			System.out.println("buscando sub21");
			flujoCajaSub21 = getMonitoreoServicioByProgramaAnoComponenteSubtitulo(programaAno.getIdProgramaAno(), componentesPorSubtitulo.get(Subtitulo.SUBTITULO21.getId()), Subtitulo.SUBTITULO21, true);
		}
		List<SubtituloFlujoCajaVO> flujoCajaSub22 = null;
		if(componentesPorSubtitulo.get(Subtitulo.SUBTITULO22.getId()) != null){
			System.out.println("buscando sub22");
			flujoCajaSub22 = getMonitoreoServicioByProgramaAnoComponenteSubtitulo(programaAno.getIdProgramaAno(), componentesPorSubtitulo.get(Subtitulo.SUBTITULO22.getId()), Subtitulo.SUBTITULO22, true);
		}
		List<SubtituloFlujoCajaVO> flujoCajaSub24 = null;
		if(componentesPorSubtitulo.get(Subtitulo.SUBTITULO24.getId()) != null){
			System.out.println("buscando sub24");
			flujoCajaSub24 = getMonitoreoComunaByProgramaAnoComponenteSubtitulo(programaAno.getIdProgramaAno(), componentesPorSubtitulo.get(Subtitulo.SUBTITULO24.getId()), Subtitulo.SUBTITULO24, true);
		}
		List<SubtituloFlujoCajaVO> flujoCajaSub29 = null;
		if(componentesPorSubtitulo.get(Subtitulo.SUBTITULO29.getId()) != null){
			System.out.println("buscando sub29");
			flujoCajaSub29 = getMonitoreoServicioByProgramaAnoComponenteSubtitulo(programaAno.getIdProgramaAno(), componentesPorSubtitulo.get(Subtitulo.SUBTITULO29.getId()), Subtitulo.SUBTITULO29, true);
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
				totalEnero+=subtituloFlujoCajaVO.getCajaMontoEnero().getMontoMes();
				totalFebrero+=subtituloFlujoCajaVO.getCajaMontoFebrero().getMontoMes();
				totalMarzo+=subtituloFlujoCajaVO.getCajaMontoMarzo().getMontoMes();
				totalAbril+=subtituloFlujoCajaVO.getCajaMontoAbril().getMontoMes();
				totalMayo+=subtituloFlujoCajaVO.getCajaMontoMayo().getMontoMes();
				totalJunio+=subtituloFlujoCajaVO.getCajaMontoJunio().getMontoMes();
				totalJulio+=subtituloFlujoCajaVO.getCajaMontoJulio().getMontoMes();
				totalAgosto+=subtituloFlujoCajaVO.getCajaMontoAgosto().getMontoMes();
				totalSeptiembre+=subtituloFlujoCajaVO.getCajaMontoSeptiembre().getMontoMes();
				totalOctubre+=subtituloFlujoCajaVO.getCajaMontoOctubre().getMontoMes();
				totalNoviembre+=subtituloFlujoCajaVO.getCajaMontoNoviembre().getMontoMes();
				totalDiciembre+=subtituloFlujoCajaVO.getCajaMontoDiciembre().getMontoMes();
				totalSubtituloPercapita+=subtituloFlujoCajaVO.getTotalMontos();
			}
			totalSubtituloFlujoCajaVO.getCajaMontoEnero().setMontoMes(totalEnero);
			totalSubtituloFlujoCajaVO.getCajaMontoFebrero().setMontoMes(totalFebrero);
			totalSubtituloFlujoCajaVO.getCajaMontoMarzo().setMontoMes(totalMarzo);
			totalSubtituloFlujoCajaVO.getCajaMontoAbril().setMontoMes(totalAbril);
			totalSubtituloFlujoCajaVO.getCajaMontoMayo().setMontoMes(totalMayo);
			totalSubtituloFlujoCajaVO.getCajaMontoJunio().setMontoMes(totalJunio);
			totalSubtituloFlujoCajaVO.getCajaMontoJulio().setMontoMes(totalJulio);
			totalSubtituloFlujoCajaVO.getCajaMontoAgosto().setMontoMes(totalAgosto);
			totalSubtituloFlujoCajaVO.getCajaMontoSeptiembre().setMontoMes(totalSeptiembre);
			totalSubtituloFlujoCajaVO.getCajaMontoOctubre().setMontoMes(totalOctubre);
			totalSubtituloFlujoCajaVO.getCajaMontoNoviembre().setMontoMes(totalNoviembre);
			totalSubtituloFlujoCajaVO.getCajaMontoDiciembre().setMontoMes(totalDiciembre);
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
					totalEneroDesempeno+=subtituloFlujoCajaVO.getCajaMontoEnero().getMontoMes();
					totalFebreroDesempeno+=subtituloFlujoCajaVO.getCajaMontoFebrero().getMontoMes();
					totalMarzoDesempeno+=subtituloFlujoCajaVO.getCajaMontoMarzo().getMontoMes();
					totalAbrilDesempeno+=subtituloFlujoCajaVO.getCajaMontoAbril().getMontoMes();
					totalMayoDesempeno+=subtituloFlujoCajaVO.getCajaMontoMayo().getMontoMes();
					totalJunioDesempeno+=subtituloFlujoCajaVO.getCajaMontoJunio().getMontoMes();
					totalJulioDesempeno+=subtituloFlujoCajaVO.getCajaMontoJulio().getMontoMes();
					totalAgostoDesempeno+=subtituloFlujoCajaVO.getCajaMontoAgosto().getMontoMes();
					totalSeptiembreDesempeno+=subtituloFlujoCajaVO.getCajaMontoSeptiembre().getMontoMes();
					totalOctubreDesempeno+=subtituloFlujoCajaVO.getCajaMontoOctubre().getMontoMes();
					totalNoviembreDesempeno+=subtituloFlujoCajaVO.getCajaMontoNoviembre().getMontoMes();
					totalDiciembreDesempeno+=subtituloFlujoCajaVO.getCajaMontoDiciembre().getMontoMes();
					totalSubtituloDesempeno+=subtituloFlujoCajaVO.getTotalMontos();
				}
				totalSubtituloDesempenoFlujoCajaVO.getCajaMontoEnero().setMontoMes(totalEneroDesempeno);
				totalSubtituloDesempenoFlujoCajaVO.getCajaMontoFebrero().setMontoMes(totalFebreroDesempeno);
				totalSubtituloDesempenoFlujoCajaVO.getCajaMontoMarzo().setMontoMes(totalMarzoDesempeno);
				totalSubtituloDesempenoFlujoCajaVO.getCajaMontoAbril().setMontoMes(totalAbrilDesempeno);
				totalSubtituloDesempenoFlujoCajaVO.getCajaMontoMayo().setMontoMes(totalMayoDesempeno);
				totalSubtituloDesempenoFlujoCajaVO.getCajaMontoJunio().setMontoMes(totalJunioDesempeno);
				totalSubtituloDesempenoFlujoCajaVO.getCajaMontoJulio().setMontoMes(totalJulioDesempeno);
				totalSubtituloDesempenoFlujoCajaVO.getCajaMontoAgosto().setMontoMes(totalAgostoDesempeno);
				totalSubtituloDesempenoFlujoCajaVO.getCajaMontoSeptiembre().setMontoMes(totalSeptiembreDesempeno);
				totalSubtituloDesempenoFlujoCajaVO.getCajaMontoOctubre().setMontoMes(totalOctubreDesempeno);
				totalSubtituloDesempenoFlujoCajaVO.getCajaMontoNoviembre().setMontoMes(totalNoviembreDesempeno);
				totalSubtituloDesempenoFlujoCajaVO.getCajaMontoDiciembre().setMontoMes(totalDiciembreDesempeno);
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
				totalEnero+=subtituloFlujoCajaVO.getCajaMontoEnero().getMontoMes();
				totalFebrero+=subtituloFlujoCajaVO.getCajaMontoFebrero().getMontoMes();
				totalMarzo+=subtituloFlujoCajaVO.getCajaMontoMarzo().getMontoMes();
				totalAbril+=subtituloFlujoCajaVO.getCajaMontoAbril().getMontoMes();
				totalMayo+=subtituloFlujoCajaVO.getCajaMontoMayo().getMontoMes();
				totalJunio+=subtituloFlujoCajaVO.getCajaMontoJunio().getMontoMes();
				totalJulio+=subtituloFlujoCajaVO.getCajaMontoJulio().getMontoMes();
				totalAgosto+=subtituloFlujoCajaVO.getCajaMontoAgosto().getMontoMes();
				totalSeptiembre+=subtituloFlujoCajaVO.getCajaMontoSeptiembre().getMontoMes();
				totalOctubre+=subtituloFlujoCajaVO.getCajaMontoOctubre().getMontoMes();
				totalNoviembre+=subtituloFlujoCajaVO.getCajaMontoNoviembre().getMontoMes();
				totalDiciembre+=subtituloFlujoCajaVO.getCajaMontoDiciembre().getMontoMes();
				totalSubtitulo24+=subtituloFlujoCajaVO.getTotalMontos();
			}
			totalSubtituloFlujoCajaVO.getCajaMontoEnero().setMontoMes(totalEnero);
			totalSubtituloFlujoCajaVO.getCajaMontoFebrero().setMontoMes(totalFebrero);
			totalSubtituloFlujoCajaVO.getCajaMontoMarzo().setMontoMes(totalMarzo);
			totalSubtituloFlujoCajaVO.getCajaMontoAbril().setMontoMes(totalAbril);
			totalSubtituloFlujoCajaVO.getCajaMontoMayo().setMontoMes(totalMayo);
			totalSubtituloFlujoCajaVO.getCajaMontoJunio().setMontoMes(totalJunio);
			totalSubtituloFlujoCajaVO.getCajaMontoJulio().setMontoMes(totalJulio);
			totalSubtituloFlujoCajaVO.getCajaMontoAgosto().setMontoMes(totalAgosto);
			totalSubtituloFlujoCajaVO.getCajaMontoSeptiembre().setMontoMes(totalSeptiembre);
			totalSubtituloFlujoCajaVO.getCajaMontoOctubre().setMontoMes(totalOctubre);
			totalSubtituloFlujoCajaVO.getCajaMontoNoviembre().setMontoMes(totalNoviembre);
			totalSubtituloFlujoCajaVO.getCajaMontoDiciembre().setMontoMes(totalDiciembre);
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
				totalEnero+=subtituloFlujoCajaVO.getCajaMontoEnero().getMontoMes();
				totalFebrero+=subtituloFlujoCajaVO.getCajaMontoFebrero().getMontoMes();
				totalMarzo+=subtituloFlujoCajaVO.getCajaMontoMarzo().getMontoMes();
				totalAbril+=subtituloFlujoCajaVO.getCajaMontoAbril().getMontoMes();
				totalMayo+=subtituloFlujoCajaVO.getCajaMontoMayo().getMontoMes();
				totalJunio+=subtituloFlujoCajaVO.getCajaMontoJunio().getMontoMes();
				totalJulio+=subtituloFlujoCajaVO.getCajaMontoJulio().getMontoMes();
				totalAgosto+=subtituloFlujoCajaVO.getCajaMontoAgosto().getMontoMes();
				totalSeptiembre+=subtituloFlujoCajaVO.getCajaMontoSeptiembre().getMontoMes();
				totalOctubre+=subtituloFlujoCajaVO.getCajaMontoOctubre().getMontoMes();
				totalNoviembre+=subtituloFlujoCajaVO.getCajaMontoNoviembre().getMontoMes();
				totalDiciembre+=subtituloFlujoCajaVO.getCajaMontoDiciembre().getMontoMes();
				totalSubtitulo21+=subtituloFlujoCajaVO.getTotalMontos();
			}
			totalSubtituloFlujoCajaVO.getCajaMontoEnero().setMontoMes(totalEnero);
			totalSubtituloFlujoCajaVO.getCajaMontoFebrero().setMontoMes(totalFebrero);
			totalSubtituloFlujoCajaVO.getCajaMontoMarzo().setMontoMes(totalMarzo);
			totalSubtituloFlujoCajaVO.getCajaMontoAbril().setMontoMes(totalAbril);
			totalSubtituloFlujoCajaVO.getCajaMontoMayo().setMontoMes(totalMayo);
			totalSubtituloFlujoCajaVO.getCajaMontoJunio().setMontoMes(totalJunio);
			totalSubtituloFlujoCajaVO.getCajaMontoJulio().setMontoMes(totalJulio);
			totalSubtituloFlujoCajaVO.getCajaMontoAgosto().setMontoMes(totalAgosto);
			totalSubtituloFlujoCajaVO.getCajaMontoSeptiembre().setMontoMes(totalSeptiembre);
			totalSubtituloFlujoCajaVO.getCajaMontoOctubre().setMontoMes(totalOctubre);
			totalSubtituloFlujoCajaVO.getCajaMontoNoviembre().setMontoMes(totalNoviembre);
			totalSubtituloFlujoCajaVO.getCajaMontoDiciembre().setMontoMes(totalDiciembre);
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
				totalEnero+=subtituloFlujoCajaVO.getCajaMontoEnero().getMontoMes();
				totalFebrero+=subtituloFlujoCajaVO.getCajaMontoFebrero().getMontoMes();
				totalMarzo+=subtituloFlujoCajaVO.getCajaMontoMarzo().getMontoMes();
				totalAbril+=subtituloFlujoCajaVO.getCajaMontoAbril().getMontoMes();
				totalMayo+=subtituloFlujoCajaVO.getCajaMontoMayo().getMontoMes();
				totalJunio+=subtituloFlujoCajaVO.getCajaMontoJunio().getMontoMes();
				totalJulio+=subtituloFlujoCajaVO.getCajaMontoJulio().getMontoMes();
				totalAgosto+=subtituloFlujoCajaVO.getCajaMontoAgosto().getMontoMes();
				totalSeptiembre+=subtituloFlujoCajaVO.getCajaMontoSeptiembre().getMontoMes();
				totalOctubre+=subtituloFlujoCajaVO.getCajaMontoOctubre().getMontoMes();
				totalNoviembre+=subtituloFlujoCajaVO.getCajaMontoNoviembre().getMontoMes();
				totalDiciembre+=subtituloFlujoCajaVO.getCajaMontoDiciembre().getMontoMes();
				totalSubtitulo22+=subtituloFlujoCajaVO.getTotalMontos();
			}
			totalSubtituloFlujoCajaVO.getCajaMontoEnero().setMontoMes(totalEnero);
			totalSubtituloFlujoCajaVO.getCajaMontoFebrero().setMontoMes(totalFebrero);
			totalSubtituloFlujoCajaVO.getCajaMontoMarzo().setMontoMes(totalMarzo);
			totalSubtituloFlujoCajaVO.getCajaMontoAbril().setMontoMes(totalAbril);
			totalSubtituloFlujoCajaVO.getCajaMontoMayo().setMontoMes(totalMayo);
			totalSubtituloFlujoCajaVO.getCajaMontoJunio().setMontoMes(totalJunio);
			totalSubtituloFlujoCajaVO.getCajaMontoJulio().setMontoMes(totalJulio);
			totalSubtituloFlujoCajaVO.getCajaMontoAgosto().setMontoMes(totalAgosto);
			totalSubtituloFlujoCajaVO.getCajaMontoSeptiembre().setMontoMes(totalSeptiembre);
			totalSubtituloFlujoCajaVO.getCajaMontoOctubre().setMontoMes(totalOctubre);
			totalSubtituloFlujoCajaVO.getCajaMontoNoviembre().setMontoMes(totalNoviembre);
			totalSubtituloFlujoCajaVO.getCajaMontoDiciembre().setMontoMes(totalDiciembre);
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
				totalEnero+=subtituloFlujoCajaVO.getCajaMontoEnero().getMontoMes();
				totalFebrero+=subtituloFlujoCajaVO.getCajaMontoFebrero().getMontoMes();
				totalMarzo+=subtituloFlujoCajaVO.getCajaMontoMarzo().getMontoMes();
				totalAbril+=subtituloFlujoCajaVO.getCajaMontoAbril().getMontoMes();
				totalMayo+=subtituloFlujoCajaVO.getCajaMontoMayo().getMontoMes();
				totalJunio+=subtituloFlujoCajaVO.getCajaMontoJunio().getMontoMes();
				totalJulio+=subtituloFlujoCajaVO.getCajaMontoJulio().getMontoMes();
				totalAgosto+=subtituloFlujoCajaVO.getCajaMontoAgosto().getMontoMes();
				totalSeptiembre+=subtituloFlujoCajaVO.getCajaMontoSeptiembre().getMontoMes();
				totalOctubre+=subtituloFlujoCajaVO.getCajaMontoOctubre().getMontoMes();
				totalNoviembre+=subtituloFlujoCajaVO.getCajaMontoNoviembre().getMontoMes();
				totalDiciembre+=subtituloFlujoCajaVO.getCajaMontoDiciembre().getMontoMes();
				totalSubtitulo29+=subtituloFlujoCajaVO.getTotalMontos();
			}
			totalSubtituloFlujoCajaVO.getCajaMontoEnero().setMontoMes(totalEnero);
			totalSubtituloFlujoCajaVO.getCajaMontoFebrero().setMontoMes(totalFebrero);
			totalSubtituloFlujoCajaVO.getCajaMontoMarzo().setMontoMes(totalMarzo);
			totalSubtituloFlujoCajaVO.getCajaMontoAbril().setMontoMes(totalAbril);
			totalSubtituloFlujoCajaVO.getCajaMontoMayo().setMontoMes(totalMayo);
			totalSubtituloFlujoCajaVO.getCajaMontoJunio().setMontoMes(totalJunio);
			totalSubtituloFlujoCajaVO.getCajaMontoJulio().setMontoMes(totalJulio);
			totalSubtituloFlujoCajaVO.getCajaMontoAgosto().setMontoMes(totalAgosto);
			totalSubtituloFlujoCajaVO.getCajaMontoSeptiembre().setMontoMes(totalSeptiembre);
			totalSubtituloFlujoCajaVO.getCajaMontoOctubre().setMontoMes(totalOctubre);
			totalSubtituloFlujoCajaVO.getCajaMontoNoviembre().setMontoMes(totalNoviembre);
			totalSubtituloFlujoCajaVO.getCajaMontoDiciembre().setMontoMes(totalDiciembre);
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
		for(int mes = 1; mes <= 12; mes++){
			Mes mesDTO = mesDAO.getMesPorID(mes);
			CajaMontoSummaryVO cajaMontoSummaryVO = new CajaMontoSummaryVO();
			cajaMontoSummaryVO.setIdMes(mesDTO.getIdMes());
			cajaMontoSummaryVO.setNombreMes(mesDTO.getNombre());
			cajaMontoSummaryVO.setMontoMes(percapitaMes);
			switch (mes) {
			case 1:
				subtituloFlujoCajaVO.setCajaMontoEnero(cajaMontoSummaryVO);
				break;
			case 2:
				subtituloFlujoCajaVO.setCajaMontoFebrero(cajaMontoSummaryVO);
				break;
			case 3:
				subtituloFlujoCajaVO.setCajaMontoMarzo(cajaMontoSummaryVO);
				break;
			case 4:
				subtituloFlujoCajaVO.setCajaMontoAbril(cajaMontoSummaryVO);
				break;
			case 5:
				subtituloFlujoCajaVO.setCajaMontoMayo(cajaMontoSummaryVO);
				break;
			case 6:
				subtituloFlujoCajaVO.setCajaMontoJunio(cajaMontoSummaryVO);
				break;
			case 7:
				subtituloFlujoCajaVO.setCajaMontoJulio(cajaMontoSummaryVO);
				break;
			case 8:
				subtituloFlujoCajaVO.setCajaMontoAgosto(cajaMontoSummaryVO);
				break;
			case 9:
				subtituloFlujoCajaVO.setCajaMontoSeptiembre(cajaMontoSummaryVO);
				break;
			case 10:
				subtituloFlujoCajaVO.setCajaMontoOctubre(cajaMontoSummaryVO);
				break;
			case 11:
				subtituloFlujoCajaVO.setCajaMontoNoviembre(cajaMontoSummaryVO);
				break;
			case 12:
				subtituloFlujoCajaVO.setCajaMontoDiciembre(cajaMontoSummaryVO);
				break;
			default:
				break;
			}
		}
		return subtituloFlujoCajaVO;
	}

	private SubtituloFlujoCajaVO getDesempenoDificialFlujoCajaVO(String nombreServicio, Integer desempeno) {
		SubtituloFlujoCajaVO subtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
		subtituloFlujoCajaVO.setServicio(nombreServicio);
		for(int mes = 1; mes <= 12; mes++){
			Mes mesDTO = mesDAO.getMesPorID(mes);
			CajaMontoSummaryVO cajaMontoSummaryVO = new CajaMontoSummaryVO();
			cajaMontoSummaryVO.setIdMes(mesDTO.getIdMes());
			cajaMontoSummaryVO.setNombreMes(mesDTO.getNombre());
			if(desempeno != null){
				cajaMontoSummaryVO.setMontoMes(desempeno.longValue());
			}else{
				cajaMontoSummaryVO.setMontoMes(0L);
			}
			switch (mes) {
			case 1:
				subtituloFlujoCajaVO.setCajaMontoEnero(cajaMontoSummaryVO);
				break;
			case 2:
				subtituloFlujoCajaVO.setCajaMontoFebrero(cajaMontoSummaryVO);
				break;
			case 3:
				subtituloFlujoCajaVO.setCajaMontoMarzo(cajaMontoSummaryVO);
				break;
			case 4:
				subtituloFlujoCajaVO.setCajaMontoAbril(cajaMontoSummaryVO);
				break;
			case 5:
				subtituloFlujoCajaVO.setCajaMontoMayo(cajaMontoSummaryVO);
				break;
			case 6:
				subtituloFlujoCajaVO.setCajaMontoJunio(cajaMontoSummaryVO);
				break;
			case 7:
				subtituloFlujoCajaVO.setCajaMontoJulio(cajaMontoSummaryVO);
				break;
			case 8:
				subtituloFlujoCajaVO.setCajaMontoAgosto(cajaMontoSummaryVO);
				break;
			case 9:
				subtituloFlujoCajaVO.setCajaMontoSeptiembre(cajaMontoSummaryVO);
				break;
			case 10:
				subtituloFlujoCajaVO.setCajaMontoOctubre(cajaMontoSummaryVO);
				break;
			case 11:
				subtituloFlujoCajaVO.setCajaMontoNoviembre(cajaMontoSummaryVO);
				break;
			case 12:
				subtituloFlujoCajaVO.setCajaMontoDiciembre(cajaMontoSummaryVO);
				break;
			default:
				break;
			}
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
		programaAno.setEstadoFlujoCaja(new EstadoPrograma(EstadosProgramas.REVISADO.getId()));
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
		LOGGER.info("Inicio getMonitoreoServicioByProgramaAnoComponenteSubtitulo");
		List<SubtituloFlujoCajaVO> subtitulosFlujosCaja = new ArrayList<SubtituloFlujoCajaVO>();
		List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
		for(ServicioSalud servicioSalud : servicios){
			SubtituloFlujoCajaVO subtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
			subtituloFlujoCajaVO.setMarcoPresupuestario(0L);
			subtituloFlujoCajaVO.setIdServicio(servicioSalud.getId());
			subtituloFlujoCajaVO.setServicio(servicioSalud.getNombre());

			for(Integer idComponente : idComponentes){
				ProgramaSubtituloComponentePeso programaSubtituloComponentePeso = programasDAO.getProgramaSubtituloComponentePesoByProgramaServicioComponenteSubtitulo(idProgramaAno, servicioSalud.getId(), idComponente, subtitulo.getId());
				if(programaSubtituloComponentePeso != null){
					subtituloFlujoCajaVO.setPesoComponentes(subtituloFlujoCajaVO.getPesoComponentes() + programaSubtituloComponentePeso.getPeso());
				}
			}

			subtituloFlujoCajaVO.setPesoComponentes(subtituloFlujoCajaVO.getPesoComponentes()/100.0); 
			Long marcoPresupuestario = 0L;
			for(Establecimiento establecimiento : servicioSalud.getEstablecimientos()){
				for(Integer idComponente : idComponentes){
					marcoPresupuestario += programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAno, idComponente, subtitulo.getId());
				}
			}
			subtituloFlujoCajaVO.setMarcoPresupuestario(marcoPresupuestario);

			for(int mes = 1; mes <= 12; mes++){
				Mes mesDTO = mesDAO.getMesPorID(mes);
				CajaMontoSummaryVO cajaMontoMes = new CajaMontoSummaryVO(null, mes, mesDTO.getNombre());
				Long montoComponentes = 0L;
				for(Integer idComponente : idComponentes){
					CajaMonto cajaMonto = cajaDAO.getCajaMontoByServicioProgramaComponenteSubtituloMes(servicioSalud.getId(), idProgramaAno, idComponente, subtitulo.getId(), mes);
					montoComponentes += ((cajaMonto != null) ? cajaMonto.getMonto() : 0L);
				}
				cajaMontoMes.setMontoMes(montoComponentes);
				cajaMontoMes.setMontoMesOriginal(montoComponentes);
				switch (mes) {
				case 1:
					subtituloFlujoCajaVO.setCajaMontoEnero(cajaMontoMes);
					break;
				case 2:
					subtituloFlujoCajaVO.setCajaMontoFebrero(cajaMontoMes);
					break;
				case 3:
					subtituloFlujoCajaVO.setCajaMontoMarzo(cajaMontoMes);
					break;
				case 4:
					subtituloFlujoCajaVO.setCajaMontoAbril(cajaMontoMes);
					break;
				case 5:
					subtituloFlujoCajaVO.setCajaMontoMayo(cajaMontoMes);
					break;
				case 6:
					subtituloFlujoCajaVO.setCajaMontoJunio(cajaMontoMes);
					break;
				case 7:
					subtituloFlujoCajaVO.setCajaMontoJulio(cajaMontoMes);
					break;
				case 8:
					subtituloFlujoCajaVO.setCajaMontoAgosto(cajaMontoMes);
					break;
				case 9:
					subtituloFlujoCajaVO.setCajaMontoSeptiembre(cajaMontoMes);
					break;
				case 10:
					subtituloFlujoCajaVO.setCajaMontoOctubre(cajaMontoMes);
					break;
				case 11:
					subtituloFlujoCajaVO.setCajaMontoNoviembre(cajaMontoMes);
					break;
				case 12:
					subtituloFlujoCajaVO.setCajaMontoDiciembre(cajaMontoMes);
					break;
				default:
					break;
				}
			}
			subtitulosFlujosCaja.add(subtituloFlujoCajaVO);
		}
		if(!iniciarFlujoCaja){
			Integer mesActual = Integer.parseInt(getMesCurso(true));
			for(SubtituloFlujoCajaVO subtituloFlujoCaja : subtitulosFlujosCaja){
				ConveniosSummaryVO convenioRecibido = new ConveniosSummaryVO(0.0, 0);
				TransferenciaSummaryVO transferenciasAcumulada = new TransferenciaSummaryVO(0.0, 0L);
				Long marcoPresupuestario = subtituloFlujoCaja.getMarcoPresupuestario();
				Integer [] estados = {EstadosConvenios.APROBADO.getId(), EstadosConvenios.PAGADO.getId()};
				Long totalConveniosPorServicio = 0L;
				for(Integer idComponente : idComponentes){
					List<ConvenioServicio> conveniosServicio = conveniosDAO.getConveniosServicioByProgramaAnoComponenteSubtituloServicioEstadosConvenio(idProgramaAno, idComponente, subtitulo.getId(), subtituloFlujoCaja.getIdServicio(), estados);
					if(conveniosServicio != null && conveniosServicio.size() > 0){
						for(ConvenioServicio convenioServicio : conveniosServicio){
							if(convenioServicio.getMes() != null && convenioServicio.getMes().getIdMes() <= mesActual){
								if(convenioServicio.getConvenioServicioComponentes() != null && convenioServicio.getConvenioServicioComponentes().size() > 0){
									int size = convenioServicio.getConvenioServicioComponentes().size();
									ConvenioServicioComponente convenioServicioComponente = convenioServicio.getConvenioServicioComponentes().get((size-1));
									totalConveniosPorServicio += convenioServicioComponente.getMontoIngresado();
								}
							}
						}
					}
				}
				convenioRecibido.setMonto(totalConveniosPorServicio.intValue());
				if(marcoPresupuestario != 0){
					Double porcentaje = ((totalConveniosPorServicio * 100.0) / marcoPresupuestario);
					porcentaje = porcentaje / 100.0;
					convenioRecibido.setPorcentaje(porcentaje);
				}
				subtituloFlujoCaja.setConvenioRecibido(convenioRecibido);

				Long totalRemesasPorServicio = 0L;
				System.out.println("marcoPresupuestario-->"+marcoPresupuestario);
				if(marcoPresupuestario != 0){
					System.out.println("buscando las remesas para este servicio-");
					List<DetalleRemesas> detalleRemesas = remesasDAO.getRemesasPagadasEstablecimientosProgramaAnoServicioSubtituloMesHasta(idProgramaAno, subtituloFlujoCaja.getIdServicio(), subtitulo.getId(), mesActual);
					if(detalleRemesas != null && detalleRemesas.size() > 0){
						for(DetalleRemesas detalleRemesa : detalleRemesas){
							totalRemesasPorServicio += detalleRemesa.getMontoRemesa();
						}
					}
				}

				transferenciasAcumulada.setMonto(totalRemesasPorServicio);
				if(marcoPresupuestario != 0){
					Double porcentajeRemesas = ((totalRemesasPorServicio * 100.0) / marcoPresupuestario);
					porcentajeRemesas = porcentajeRemesas / 100.0;
					transferenciasAcumulada.setPorcentaje(porcentajeRemesas);
				}
				subtituloFlujoCaja.setTransferenciaAcumulada(transferenciasAcumulada);
			}
		}
		LOGGER.info("Fin getMonitoreoServicioByProgramaAnoComponenteSubtitulo");
		return subtitulosFlujosCaja;
	}


	public List<SubtituloFlujoCajaVO> getMonitoreoServicioByProgramaAnoComponenteSubtitulo(Integer idProgramaAno, Integer idServicio, List<Integer> idComponentes, Subtitulo subtitulo, Boolean iniciarFlujoCaja) {
		LOGGER.info("Inicio getMonitoreoServicioByProgramaAnoComponenteSubtitulo");
		List<SubtituloFlujoCajaVO> subtitulosFlujosCaja = new ArrayList<SubtituloFlujoCajaVO>();
		ServicioSalud servicioSalud = servicioSaludDAO.getServicioSaludById(idServicio);
		SubtituloFlujoCajaVO subtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
		subtituloFlujoCajaVO.setMarcoPresupuestario(0L);
		subtituloFlujoCajaVO.setIdServicio(servicioSalud.getId());
		subtituloFlujoCajaVO.setServicio(servicioSalud.getNombre());

		for(Integer idComponente : idComponentes){
			ProgramaSubtituloComponentePeso programaSubtituloComponentePeso = programasDAO.getProgramaSubtituloComponentePesoByProgramaServicioComponenteSubtitulo(idProgramaAno, servicioSalud.getId(), idComponente, subtitulo.getId());
			if(programaSubtituloComponentePeso != null){
				subtituloFlujoCajaVO.setPesoComponentes(subtituloFlujoCajaVO.getPesoComponentes() + programaSubtituloComponentePeso.getPeso());
			}
		}

		subtituloFlujoCajaVO.setPesoComponentes(subtituloFlujoCajaVO.getPesoComponentes()/100.0); 
		Long marcoPresupuestarioServicio = 0L;
		for(Establecimiento establecimiento : servicioSalud.getEstablecimientos()){
			for(Integer idComponente : idComponentes){
				marcoPresupuestarioServicio += programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAno, idComponente, subtitulo.getId());
			}
		}
		subtituloFlujoCajaVO.setMarcoPresupuestario(marcoPresupuestarioServicio);

		for(int mes = 1; mes <= 12; mes++){
			Mes mesDTO = mesDAO.getMesPorID(mes);
			CajaMontoSummaryVO cajaMontoMes = new CajaMontoSummaryVO(null, mes, mesDTO.getNombre());
			Long montoComponentes = 0L;
			for(Integer idComponente : idComponentes){
				CajaMonto cajaMonto = cajaDAO.getCajaMontoByServicioProgramaComponenteSubtituloMes(servicioSalud.getId(), idProgramaAno, idComponente, subtitulo.getId(), mes);
				montoComponentes += ((cajaMonto != null) ? cajaMonto.getMonto() : 0L);
			}
			cajaMontoMes.setMontoMes(montoComponentes);
			cajaMontoMes.setMontoMesOriginal(montoComponentes);
			switch (mes) {
			case 1:
				subtituloFlujoCajaVO.setCajaMontoEnero(cajaMontoMes);
				break;
			case 2:
				subtituloFlujoCajaVO.setCajaMontoFebrero(cajaMontoMes);
				break;
			case 3:
				subtituloFlujoCajaVO.setCajaMontoMarzo(cajaMontoMes);
				break;
			case 4:
				subtituloFlujoCajaVO.setCajaMontoAbril(cajaMontoMes);
				break;
			case 5:
				subtituloFlujoCajaVO.setCajaMontoMayo(cajaMontoMes);
				break;
			case 6:
				subtituloFlujoCajaVO.setCajaMontoJunio(cajaMontoMes);
				break;
			case 7:
				subtituloFlujoCajaVO.setCajaMontoJulio(cajaMontoMes);
				break;
			case 8:
				subtituloFlujoCajaVO.setCajaMontoAgosto(cajaMontoMes);
				break;
			case 9:
				subtituloFlujoCajaVO.setCajaMontoSeptiembre(cajaMontoMes);
				break;
			case 10:
				subtituloFlujoCajaVO.setCajaMontoOctubre(cajaMontoMes);
				break;
			case 11:
				subtituloFlujoCajaVO.setCajaMontoNoviembre(cajaMontoMes);
				break;
			case 12:
				subtituloFlujoCajaVO.setCajaMontoDiciembre(cajaMontoMes);
				break;
			default:
				break;
			}
		}
		subtitulosFlujosCaja.add(subtituloFlujoCajaVO);
		if(!iniciarFlujoCaja){
			Integer mesActual = Integer.parseInt(getMesCurso(true));
			for(SubtituloFlujoCajaVO subtituloFlujoCaja : subtitulosFlujosCaja){
				ConveniosSummaryVO convenioRecibido = new ConveniosSummaryVO(0.0, 0);
				TransferenciaSummaryVO transferenciasAcumulada = new TransferenciaSummaryVO(0.0, 0L);
				Long marcoPresupuestario = subtituloFlujoCaja.getMarcoPresupuestario();
				Integer [] estados = {EstadosConvenios.APROBADO.getId(), EstadosConvenios.PAGADO.getId()};
				Long totalConveniosPorServicio = 0L;
				for(Integer idComponente : idComponentes){
					List<ConvenioServicio> conveniosServicio = conveniosDAO.getConveniosServicioByProgramaAnoComponenteSubtituloServicioEstadosConvenio(idProgramaAno, idComponente, subtitulo.getId(), subtituloFlujoCaja.getIdServicio(), estados);
					if(conveniosServicio != null && conveniosServicio.size() > 0){
						for(ConvenioServicio convenioServicio : conveniosServicio){
							if(convenioServicio.getMes() != null && convenioServicio.getMes().getIdMes() <= mesActual){
								if(convenioServicio.getConvenioServicioComponentes() != null && convenioServicio.getConvenioServicioComponentes().size() > 0){
									int size = convenioServicio.getConvenioServicioComponentes().size();
									ConvenioServicioComponente convenioServicioComponente = convenioServicio.getConvenioServicioComponentes().get((size-1));
									totalConveniosPorServicio += convenioServicioComponente.getMontoIngresado();
								}
							}
						}
					}
				}
				convenioRecibido.setMonto(totalConveniosPorServicio.intValue());
				if(marcoPresupuestario != 0){
					Double porcentaje = ((totalConveniosPorServicio * 100.0) / marcoPresupuestario);
					porcentaje = porcentaje / 100.0;
					convenioRecibido.setPorcentaje(porcentaje);
				}
				subtituloFlujoCaja.setConvenioRecibido(convenioRecibido);

				Long totalRemesasPorServicio = 0L;
				System.out.println("marcoPresupuestario-->"+marcoPresupuestario);
				if(marcoPresupuestario != 0){
					System.out.println("buscando las remesas para este servicio-");
					List<DetalleRemesas> detalleRemesas = remesasDAO.getRemesasPagadasEstablecimientosProgramaAnoServicioSubtituloMesHasta(idProgramaAno, subtituloFlujoCaja.getIdServicio(), subtitulo.getId(), mesActual);
					if(detalleRemesas != null && detalleRemesas.size() > 0){
						for(DetalleRemesas detalleRemesa : detalleRemesas){
							totalRemesasPorServicio += detalleRemesa.getMontoRemesa();
						}
					}
				}

				transferenciasAcumulada.setMonto(totalRemesasPorServicio);
				if(marcoPresupuestario != 0){
					Double porcentajeRemesas = ((totalRemesasPorServicio * 100.0) / marcoPresupuestario);
					porcentajeRemesas = porcentajeRemesas / 100.0;
					transferenciasAcumulada.setPorcentaje(porcentajeRemesas);
				}
				subtituloFlujoCaja.setTransferenciaAcumulada(transferenciasAcumulada);
			}
		}
		LOGGER.info("Fin getMonitoreoServicioByProgramaAnoComponenteSubtitulo");
		return subtitulosFlujosCaja;
	}

	public List<SubtituloFlujoCajaVO> getMonitoreoComunaByProgramaAnoComponenteSubtitulo(Integer idProgramaAno, List<Integer> idComponentes, Subtitulo subtitulo, Boolean iniciarFlujoCaja) {
		LOGGER.info("Inicio getMonitoreoComunaByProgramaAnoComponenteSubtitulo");
		List<SubtituloFlujoCajaVO> subtitulosFlujosCaja = new ArrayList<SubtituloFlujoCajaVO>();
		List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
		for(ServicioSalud servicioSalud : servicios){
			SubtituloFlujoCajaVO subtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
			subtituloFlujoCajaVO.setIdServicio(servicioSalud.getId());
			subtituloFlujoCajaVO.setServicio(servicioSalud.getNombre());
			subtituloFlujoCajaVO.setMarcoPresupuestario(0L);

			for(Integer idComponente : idComponentes){
				ProgramaSubtituloComponentePeso programaSubtituloComponentePeso = programasDAO.getProgramaSubtituloComponentePesoByProgramaServicioComponenteSubtitulo(idProgramaAno, servicioSalud.getId(), idComponente, subtitulo.getId());
				if(programaSubtituloComponentePeso != null){
					subtituloFlujoCajaVO.setPesoComponentes(subtituloFlujoCajaVO.getPesoComponentes() + programaSubtituloComponentePeso.getPeso());
				}
			}

			subtituloFlujoCajaVO.setPesoComponentes(subtituloFlujoCajaVO.getPesoComponentes()/100.0);

			for(int mes = 1; mes <= 12; mes++){
				Mes mesDTO = mesDAO.getMesPorID(mes);
				CajaMontoSummaryVO cajaMontoMes = new CajaMontoSummaryVO(null, mes, mesDTO.getNombre());
				Long montoComponentes = 0L;

				for(Integer idComponente : idComponentes){
					CajaMonto cajaMonto = cajaDAO.getCajaMontoByServicioProgramaComponenteSubtituloMes(servicioSalud.getId(), idProgramaAno, idComponente, subtitulo.getId(), mes);
					montoComponentes += ((cajaMonto != null) ? cajaMonto.getMonto() : 0L);
				}
				cajaMontoMes.setMontoMes(montoComponentes);
				cajaMontoMes.setMontoMesOriginal(montoComponentes);
				switch (mes) {
				case 1:
					subtituloFlujoCajaVO.setCajaMontoEnero(cajaMontoMes);
					break;
				case 2:
					subtituloFlujoCajaVO.setCajaMontoFebrero(cajaMontoMes);
					break;
				case 3:
					subtituloFlujoCajaVO.setCajaMontoMarzo(cajaMontoMes);
					break;
				case 4:
					subtituloFlujoCajaVO.setCajaMontoAbril(cajaMontoMes);
					break;
				case 5:
					subtituloFlujoCajaVO.setCajaMontoMayo(cajaMontoMes);
					break;
				case 6:
					subtituloFlujoCajaVO.setCajaMontoJunio(cajaMontoMes);
					break;
				case 7:
					subtituloFlujoCajaVO.setCajaMontoJulio(cajaMontoMes);
					break;
				case 8:
					subtituloFlujoCajaVO.setCajaMontoAgosto(cajaMontoMes);
					break;
				case 9:
					subtituloFlujoCajaVO.setCajaMontoSeptiembre(cajaMontoMes);
					break;
				case 10:
					subtituloFlujoCajaVO.setCajaMontoOctubre(cajaMontoMes);
					break;
				case 11:
					subtituloFlujoCajaVO.setCajaMontoNoviembre(cajaMontoMes);
					break;
				case 12:
					subtituloFlujoCajaVO.setCajaMontoDiciembre(cajaMontoMes);
					break;
				default:
					break;
				}
			}
			Long marcoPresupuestario = 0L;
			for(Comuna comuna : servicioSalud.getComunas()){
				for(Integer idComponente : idComponentes){
					marcoPresupuestario += programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAno, idComponente, subtitulo.getId());
				}
			}
			subtituloFlujoCajaVO.setMarcoPresupuestario(marcoPresupuestario);
			subtitulosFlujosCaja.add(subtituloFlujoCajaVO);
		}
		if(!iniciarFlujoCaja){
			Integer mesActual = Integer.parseInt(getMesCurso(true));
			for(SubtituloFlujoCajaVO subtituloFlujoCaja : subtitulosFlujosCaja){
				ConveniosSummaryVO convenioRecibido = new ConveniosSummaryVO(0.0, 0);
				TransferenciaSummaryVO transferenciasAcumulada = new TransferenciaSummaryVO(0.0, 0L);
				Long marcoPresupuestario = subtituloFlujoCaja.getMarcoPresupuestario();
				Integer [] estados = {EstadosConvenios.APROBADO.getId(), EstadosConvenios.PAGADO.getId()};
				Long totalConveniosPorServicio = 0L;
				for(Integer idComponente : idComponentes){
					List<ConvenioComuna> conveniosComuna = conveniosDAO.getConveniosComunaByProgramaAnoComponenteSubtituloServicioEstadosConvenio(idProgramaAno, idComponente, subtitulo.getId(), subtituloFlujoCaja.getIdServicio(), estados);
					if(conveniosComuna != null && conveniosComuna.size() > 0){
						for(ConvenioComuna convenioComuna : conveniosComuna){
							if(convenioComuna.getMes() != null && convenioComuna.getMes().getIdMes() <= mesActual){
								if(convenioComuna.getConvenioComunaComponentes() != null && convenioComuna.getConvenioComunaComponentes().size() > 0){
									int size = convenioComuna.getConvenioComunaComponentes().size();
									ConvenioComunaComponente convenioComunaComponente = convenioComuna.getConvenioComunaComponentes().get((size-1));
									totalConveniosPorServicio += convenioComunaComponente.getMontoIngresado();
								}
							}
						}
					}
				}
				convenioRecibido.setMonto(totalConveniosPorServicio.intValue());
				if(marcoPresupuestario != 0){
					Double porcentaje = ((totalConveniosPorServicio * 100.0) / marcoPresupuestario);
					porcentaje = porcentaje / 100.0;
					convenioRecibido.setPorcentaje(porcentaje);
				}
				subtituloFlujoCaja.setConvenioRecibido(convenioRecibido);

				Long totalRemesasPorServicio = 0L;
				System.out.println("marcoPresupuestario-->"+marcoPresupuestario);
				if(marcoPresupuestario != 0){
					System.out.println("buscando las remesas para este servicio-");
					List<DetalleRemesas> detalleRemesas = remesasDAO.getRemesasPagadasComunasProgramaAnoServicioSubtituloMesHasta(idProgramaAno, subtituloFlujoCaja.getIdServicio(), subtitulo.getId(), mesActual);
					if(detalleRemesas != null && detalleRemesas.size() > 0){
						for(DetalleRemesas detalleRemesa : detalleRemesas){
							totalRemesasPorServicio += detalleRemesa.getMontoRemesa();
						}
					}
				}
				transferenciasAcumulada.setMonto(totalRemesasPorServicio);
				if(marcoPresupuestario != 0){
					Double porcentajeRemesas = ((totalRemesasPorServicio * 100.0) / marcoPresupuestario);
					porcentajeRemesas = porcentajeRemesas / 100.0;
					transferenciasAcumulada.setPorcentaje(porcentajeRemesas);
				}
				subtituloFlujoCaja.setTransferenciaAcumulada(transferenciasAcumulada);
			}
		}
		System.out.println("getMonitoreoComunaByProgramaAnoComponenteSubtitulo-->subtitulosFlujosCaja.size()=" +subtitulosFlujosCaja.size());
		LOGGER.info("getMonitoreoComunaByProgramaAnoComponenteSubtitulo-->subtitulosFlujosCaja.size()=" +subtitulosFlujosCaja.size());
		LOGGER.info("Fin getMonitoreoComunaByProgramaAnoComponenteSubtitulo");
		return subtitulosFlujosCaja;
	}

	public List<SubtituloFlujoCajaVO> getMonitoreoComunaByProgramaAnoComponenteSubtitulo(Integer idProgramaAno, Integer idServicio, List<Integer> idComponentes, Subtitulo subtitulo, Boolean iniciarFlujoCaja) {
		LOGGER.info("Inicio getMonitoreoComunaByProgramaAnoComponenteSubtitulo");
		List<SubtituloFlujoCajaVO> subtitulosFlujosCaja = new ArrayList<SubtituloFlujoCajaVO>();
		ServicioSalud servicioSalud = servicioSaludDAO.getServicioSaludById(idServicio);
		SubtituloFlujoCajaVO subtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
		subtituloFlujoCajaVO.setIdServicio(servicioSalud.getId());
		subtituloFlujoCajaVO.setServicio(servicioSalud.getNombre());
		subtituloFlujoCajaVO.setMarcoPresupuestario(0L);

		for(Integer idComponente : idComponentes){
			ProgramaSubtituloComponentePeso programaSubtituloComponentePeso = programasDAO.getProgramaSubtituloComponentePesoByProgramaServicioComponenteSubtitulo(idProgramaAno, servicioSalud.getId(), idComponente, subtitulo.getId());
			if(programaSubtituloComponentePeso != null){
				subtituloFlujoCajaVO.setPesoComponentes(subtituloFlujoCajaVO.getPesoComponentes() + programaSubtituloComponentePeso.getPeso());
			}
		}

		subtituloFlujoCajaVO.setPesoComponentes(subtituloFlujoCajaVO.getPesoComponentes()/100.0);

		for(int mes = 1; mes <= 12; mes++){
			Mes mesDTO = mesDAO.getMesPorID(mes);
			CajaMontoSummaryVO cajaMontoMes = new CajaMontoSummaryVO(null, mes, mesDTO.getNombre());
			Long montoComponentes = 0L;

			for(Integer idComponente : idComponentes){
				CajaMonto cajaMonto = cajaDAO.getCajaMontoByServicioProgramaComponenteSubtituloMes(servicioSalud.getId(), idProgramaAno, idComponente, subtitulo.getId(), mes);
				montoComponentes += ((cajaMonto != null) ? cajaMonto.getMonto() : 0L);
			}
			cajaMontoMes.setMontoMes(montoComponentes);
			cajaMontoMes.setMontoMesOriginal(montoComponentes);
			switch (mes) {
			case 1:
				subtituloFlujoCajaVO.setCajaMontoEnero(cajaMontoMes);
				break;
			case 2:
				subtituloFlujoCajaVO.setCajaMontoFebrero(cajaMontoMes);
				break;
			case 3:
				subtituloFlujoCajaVO.setCajaMontoMarzo(cajaMontoMes);
				break;
			case 4:
				subtituloFlujoCajaVO.setCajaMontoAbril(cajaMontoMes);
				break;
			case 5:
				subtituloFlujoCajaVO.setCajaMontoMayo(cajaMontoMes);
				break;
			case 6:
				subtituloFlujoCajaVO.setCajaMontoJunio(cajaMontoMes);
				break;
			case 7:
				subtituloFlujoCajaVO.setCajaMontoJulio(cajaMontoMes);
				break;
			case 8:
				subtituloFlujoCajaVO.setCajaMontoAgosto(cajaMontoMes);
				break;
			case 9:
				subtituloFlujoCajaVO.setCajaMontoSeptiembre(cajaMontoMes);
				break;
			case 10:
				subtituloFlujoCajaVO.setCajaMontoOctubre(cajaMontoMes);
				break;
			case 11:
				subtituloFlujoCajaVO.setCajaMontoNoviembre(cajaMontoMes);
				break;
			case 12:
				subtituloFlujoCajaVO.setCajaMontoDiciembre(cajaMontoMes);
				break;
			default:
				break;
			}
		}
		Long marcoPresupuestarioServicio = 0L;
		for(Comuna comuna : servicioSalud.getComunas()){
			for(Integer idComponente : idComponentes){
				marcoPresupuestarioServicio += programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAno, idComponente, subtitulo.getId());
			}
		}
		subtituloFlujoCajaVO.setMarcoPresupuestario(marcoPresupuestarioServicio);
		subtitulosFlujosCaja.add(subtituloFlujoCajaVO);
		if(!iniciarFlujoCaja){
			Integer mesActual = Integer.parseInt(getMesCurso(true));
			for(SubtituloFlujoCajaVO subtituloFlujoCaja : subtitulosFlujosCaja){
				ConveniosSummaryVO convenioRecibido = new ConveniosSummaryVO(0.0, 0);
				TransferenciaSummaryVO transferenciasAcumulada = new TransferenciaSummaryVO(0.0, 0L);
				Long marcoPresupuestario = subtituloFlujoCaja.getMarcoPresupuestario();
				Integer [] estados = {EstadosConvenios.APROBADO.getId(), EstadosConvenios.PAGADO.getId()};
				Long totalConveniosPorServicio = 0L;
				for(Integer idComponente : idComponentes){
					List<ConvenioComuna> conveniosComuna = conveniosDAO.getConveniosComunaByProgramaAnoComponenteSubtituloServicioEstadosConvenio(idProgramaAno, idComponente, subtitulo.getId(), subtituloFlujoCaja.getIdServicio(), estados);
					if(conveniosComuna != null && conveniosComuna.size() > 0){
						for(ConvenioComuna convenioComuna : conveniosComuna){
							if(convenioComuna.getMes() != null && convenioComuna.getMes().getIdMes() <= mesActual){
								if(convenioComuna.getConvenioComunaComponentes() != null && convenioComuna.getConvenioComunaComponentes().size() > 0){
									int size = convenioComuna.getConvenioComunaComponentes().size();
									ConvenioComunaComponente convenioComunaComponente = convenioComuna.getConvenioComunaComponentes().get((size-1));
									totalConveniosPorServicio += convenioComunaComponente.getMontoIngresado();
								}
							}
						}
					}
				}
				convenioRecibido.setMonto(totalConveniosPorServicio.intValue());
				if(marcoPresupuestario != 0){
					Double porcentaje = ((totalConveniosPorServicio * 100.0) / marcoPresupuestario);
					porcentaje = porcentaje / 100.0;
					convenioRecibido.setPorcentaje(porcentaje);
				}
				subtituloFlujoCaja.setConvenioRecibido(convenioRecibido);

				Long totalRemesasPorServicio = 0L;
				System.out.println("marcoPresupuestario-->"+marcoPresupuestario);
				if(marcoPresupuestario != 0){
					System.out.println("buscando las remesas para este servicio-");
					List<DetalleRemesas> detalleRemesas = remesasDAO.getRemesasPagadasComunasProgramaAnoServicioSubtituloMesHasta(idProgramaAno, subtituloFlujoCaja.getIdServicio(), subtitulo.getId(), mesActual);
					if(detalleRemesas != null && detalleRemesas.size() > 0){
						for(DetalleRemesas detalleRemesa : detalleRemesas){
							totalRemesasPorServicio += detalleRemesa.getMontoRemesa();
						}
					}
				}
				transferenciasAcumulada.setMonto(totalRemesasPorServicio);
				if(marcoPresupuestario != 0){
					Double porcentajeRemesas = ((totalRemesasPorServicio * 100.0) / marcoPresupuestario);
					porcentajeRemesas = porcentajeRemesas / 100.0;
					transferenciasAcumulada.setPorcentaje(porcentajeRemesas);
				}
				subtituloFlujoCaja.setTransferenciaAcumulada(transferenciasAcumulada);
			}
		}
		System.out.println("getMonitoreoComunaByProgramaAnoComponenteSubtitulo-->subtitulosFlujosCaja.size()=" +subtitulosFlujosCaja.size());
		LOGGER.info("getMonitoreoComunaByProgramaAnoComponenteSubtitulo-->subtitulosFlujosCaja.size()=" +subtitulosFlujosCaja.size());
		LOGGER.info("Fin getMonitoreoComunaByProgramaAnoComponenteSubtitulo");
		return subtitulosFlujosCaja;
	}

	public List<SubtituloFlujoCajaVO> getConvenioRemesaByProgramaAnoComponenteSubtitulo(Integer idProgramaAno, List<Integer> idComponentes, Subtitulo subtitulo) {
		List<SubtituloFlujoCajaVO> subtitulosFlujosCaja = new ArrayList<SubtituloFlujoCajaVO>(); 
		List<ServicioSalud> servicios = servicioSaludDAO.getServiciosOrderId();
		Integer mesActual = Integer.parseInt(getMesCurso(true));
		for(ServicioSalud servicioSalud : servicios){
			SubtituloFlujoCajaVO subtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
			subtituloFlujoCajaVO.setMarcoPresupuestario(0L);
			subtituloFlujoCajaVO.setIdServicio(servicioSalud.getId());
			subtituloFlujoCajaVO.setServicio(servicioSalud.getNombre());

			Long marcoPresupuestario = 0L;

			if(Subtitulo.SUBTITULO24.getId().equals(subtitulo.getId())){
				for(Comuna comuna : servicioSalud.getComunas()){
					for(Integer idComponente : idComponentes){
						marcoPresupuestario += programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(comuna.getId(), idProgramaAno, idComponente, subtitulo.getId());
					}
				}
			}else{
				for(Establecimiento establecimiento : servicioSalud.getEstablecimientos()){
					for(Integer idComponente : idComponentes){
						marcoPresupuestario += programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(establecimiento.getId(), idProgramaAno, idComponente, subtitulo.getId());
					}
				}
			}
			subtituloFlujoCajaVO.setMarcoPresupuestario(marcoPresupuestario);
			Long totalRemesasPorServicio = 0L;

			for(int mes = 1; mes <= 12; mes++){
				Mes mesDTO = mesDAO.getMesPorID(mes);
				CajaMontoSummaryVO cajaMontoMes = new CajaMontoSummaryVO(null, mes, mesDTO.getNombre());
				Long montoComponentes = 0L;
				if(mes < mesActual){
					if(Subtitulo.SUBTITULO24.getId().equals(subtitulo.getId())){
						List<DetalleRemesas> detalleRemesas = remesasDAO.getRemesasPagadasComunasProgramaAnoServicioSubtituloMesActual(idProgramaAno, subtituloFlujoCajaVO.getIdServicio(), subtitulo.getId(), mes);
						if(detalleRemesas != null && detalleRemesas.size() > 0){
							for(DetalleRemesas detalleRemesa : detalleRemesas){
								montoComponentes += detalleRemesa.getMontoRemesa();
							}
						}
					}else{
						List<DetalleRemesas> detalleRemesas = remesasDAO.getRemesasPagadasEstablecimientosProgramaAnoServicioSubtituloMesActual(idProgramaAno, subtituloFlujoCajaVO.getIdServicio(), subtitulo.getId(), mes);
						if(detalleRemesas != null && detalleRemesas.size() > 0){
							for(DetalleRemesas detalleRemesa : detalleRemesas){
								montoComponentes += detalleRemesa.getMontoRemesa();
							}
						}
					}
				}
				totalRemesasPorServicio += montoComponentes;
				cajaMontoMes.setMontoMes(montoComponentes);
				cajaMontoMes.setMontoMesOriginal(montoComponentes);
				switch (mes) {
				case 1:
					subtituloFlujoCajaVO.setCajaMontoEnero(cajaMontoMes);
					break;
				case 2:
					subtituloFlujoCajaVO.setCajaMontoFebrero(cajaMontoMes);
					break;
				case 3:
					subtituloFlujoCajaVO.setCajaMontoMarzo(cajaMontoMes);
					break;
				case 4:
					subtituloFlujoCajaVO.setCajaMontoAbril(cajaMontoMes);
					break;
				case 5:
					subtituloFlujoCajaVO.setCajaMontoMayo(cajaMontoMes);
					break;
				case 6:
					subtituloFlujoCajaVO.setCajaMontoJunio(cajaMontoMes);
					break;
				case 7:
					subtituloFlujoCajaVO.setCajaMontoJulio(cajaMontoMes);
					break;
				case 8:
					subtituloFlujoCajaVO.setCajaMontoAgosto(cajaMontoMes);
					break;
				case 9:
					subtituloFlujoCajaVO.setCajaMontoSeptiembre(cajaMontoMes);
					break;
				case 10:
					subtituloFlujoCajaVO.setCajaMontoOctubre(cajaMontoMes);
					break;
				case 11:
					subtituloFlujoCajaVO.setCajaMontoNoviembre(cajaMontoMes);
					break;
				case 12:
					subtituloFlujoCajaVO.setCajaMontoDiciembre(cajaMontoMes);
					break;
				default:
					break;
				}
			}
			TransferenciaSummaryVO transferenciasAcumulada = new TransferenciaSummaryVO(0.0, 0L);
			transferenciasAcumulada.setMonto(totalRemesasPorServicio);
			if(marcoPresupuestario != 0){
				Double porcentajeRemesas = ((totalRemesasPorServicio * 100.0) / marcoPresupuestario);
				porcentajeRemesas = porcentajeRemesas / 100.0;
				transferenciasAcumulada.setPorcentaje(porcentajeRemesas);
			}
			subtituloFlujoCajaVO.setTransferenciaAcumulada(transferenciasAcumulada);
			subtitulosFlujosCaja.add(subtituloFlujoCajaVO);
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

	public void actualizarMonitoreoServicioSubtituloFlujoCaja(Integer idProgramaAno, Integer idServicio, CajaMontoSummaryVO cajaMontoSummary, Subtitulo subtitulo, Boolean iniciarFlujoCaja) {
		List<CajaMonto> cajaMontos = cajaDAO.getByProgramaAnoServicioSubtituloMes(idProgramaAno, idServicio, subtitulo, cajaMontoSummary.getIdMes());
		System.out.println("idProgramaAno ="+idProgramaAno+" IdServicio="+ idServicio +" subtitulo="+subtitulo.getNombre());
		if(cajaMontos != null && cajaMontos.size() > 0){
			int cajaMontoSize = cajaMontos.size();
			int iteracionPorCaja = 1;
			Long montoAcumulado = 0L;
			for(CajaMonto cajaMonto : cajaMontos){
				System.out.println("cajaMonto.getCajaMontoPK().getCaja()=" + cajaMonto.getCajaMontoPK().getCaja() + " cajaMonto.getCaja().getIdComponente().getId()=" + cajaMonto.getCaja().getIdComponente().getId() + " cajaMonto.getCaja().getIdComponente().getNombre()=" + cajaMonto.getCaja().getIdComponente().getNombre());
				if(iteracionPorCaja !=  cajaMontoSize){
					ProgramaSubtituloComponentePeso programaSubtituloComponentePeso = programasDAO.getProgramaSubtituloComponentePesoByProgramaServicioComponenteSubtitulo(idProgramaAno, idServicio, cajaMonto.getCaja().getIdComponente().getId(), subtitulo.getId());
					if(programaSubtituloComponentePeso != null){
						System.out.println("actualizando el caja monto");
						float pesoComponente =  programaSubtituloComponentePeso.getPeso();
						System.out.println("actualizando el caja monto="+pesoComponente);
						if(((int)pesoComponente) == 100){
							cajaMonto.setMonto(cajaMontoSummary.getMontoMes().intValue());
							montoAcumulado += cajaMontoSummary.getMontoMes(); 
							cajaMonto.setReparos(true);
						}else{
							System.out.println("actualizando el caja monto");
							int montoActualizado = (int)((pesoComponente * cajaMontoSummary.getMontoMes())/100.0);
							cajaMonto.setMonto(montoActualizado);
							montoAcumulado += montoActualizado;
							cajaMonto.setReparos(true);
						}
					}else{
						System.out.println("EL PESO DEL COMPONENTE ES NULO");
					}
				}else{
					cajaMonto.setMonto((int)(cajaMontoSummary.getMontoMes() - montoAcumulado));
					cajaMonto.setReparos(true);
				}
				iteracionPorCaja++;
			}
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
			Long montoMensual = valor / 12;
			for(int mes=1; mes<=12 ; mes++){
				CajaMontoSummaryVO cajaMonto = new CajaMontoSummaryVO();
				cajaMonto.setIdMes(mes);
				cajaMonto.setMontoMes(montoMensual);
				switch (mes) {
				case 1:
					subtituloFlujoCajaVO.setCajaMontoEnero(cajaMonto);
					break;
				case 2:
					subtituloFlujoCajaVO.setCajaMontoFebrero(cajaMonto);
					break;
				case 3:
					subtituloFlujoCajaVO.setCajaMontoMarzo(cajaMonto);
					break;
				case 4:
					subtituloFlujoCajaVO.setCajaMontoAbril(cajaMonto);
					break;
				case 5:
					subtituloFlujoCajaVO.setCajaMontoMayo(cajaMonto);
					break;
				case 6:
					subtituloFlujoCajaVO.setCajaMontoJunio(cajaMonto);
					break;
				case 7:
					subtituloFlujoCajaVO.setCajaMontoJulio(cajaMonto);
					break;
				case 8:
					subtituloFlujoCajaVO.setCajaMontoAgosto(cajaMonto);
					break;
				case 9:
					subtituloFlujoCajaVO.setCajaMontoSeptiembre(cajaMonto);
					break;
				case 10:
					subtituloFlujoCajaVO.setCajaMontoOctubre(cajaMonto);
					break;
				case 11:
					subtituloFlujoCajaVO.setCajaMontoNoviembre(cajaMonto);
					break;
				case 12:
					subtituloFlujoCajaVO.setCajaMontoDiciembre(cajaMonto);
					break;
				default:
					break;
				}
			}		

			subtituloFlujoCajaVO.setMarcoPresupuestario(valor);
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

			for(int mes=1; mes<=12 ; mes++){
				CajaMontoSummaryVO cajaMonto = new CajaMontoSummaryVO();
				cajaMonto.setIdMes(mes);
				cajaMonto.setMontoMes(montoMensual);
				switch (mes) {
				case 1:
					subtituloFlujoCajaVO.setCajaMontoEnero(cajaMonto);
					break;
				case 2:
					subtituloFlujoCajaVO.setCajaMontoFebrero(cajaMonto);
					break;
				case 3:
					subtituloFlujoCajaVO.setCajaMontoMarzo(cajaMonto);
					break;
				case 4:
					subtituloFlujoCajaVO.setCajaMontoAbril(cajaMonto);
					break;
				case 5:
					subtituloFlujoCajaVO.setCajaMontoMayo(cajaMonto);
					break;
				case 6:
					subtituloFlujoCajaVO.setCajaMontoJunio(cajaMonto);
					break;
				case 7:
					subtituloFlujoCajaVO.setCajaMontoJulio(cajaMonto);
					break;
				case 8:
					subtituloFlujoCajaVO.setCajaMontoAgosto(cajaMonto);
					break;
				case 9:
					subtituloFlujoCajaVO.setCajaMontoSeptiembre(cajaMonto);
					break;
				case 10:
					subtituloFlujoCajaVO.setCajaMontoOctubre(cajaMonto);
					break;
				case 11:
					subtituloFlujoCajaVO.setCajaMontoNoviembre(cajaMonto);
					break;
				case 12:
					subtituloFlujoCajaVO.setCajaMontoDiciembre(cajaMonto);
					break;
				default:
					break;
				}
			}		
			subtituloFlujoCajaVO.setMarcoPresupuestario(valor);
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

			for(int mes=1; mes<=12 ; mes++){
				CajaMontoSummaryVO cajaMonto = new CajaMontoSummaryVO();
				cajaMonto.setIdMes(mes);
				cajaMonto.setMontoMes(montoMensual);
				switch (mes) {
				case 1:
					subtituloFlujoCajaVO.setCajaMontoEnero(cajaMonto);
					break;
				case 2:
					subtituloFlujoCajaVO.setCajaMontoFebrero(cajaMonto);
					break;
				case 3:
					subtituloFlujoCajaVO.setCajaMontoMarzo(cajaMonto);
					break;
				case 4:
					subtituloFlujoCajaVO.setCajaMontoAbril(cajaMonto);
					break;
				case 5:
					subtituloFlujoCajaVO.setCajaMontoMayo(cajaMonto);
					break;
				case 6:
					subtituloFlujoCajaVO.setCajaMontoJunio(cajaMonto);
					break;
				case 7:
					subtituloFlujoCajaVO.setCajaMontoJulio(cajaMonto);
					break;
				case 8:
					subtituloFlujoCajaVO.setCajaMontoAgosto(cajaMonto);
					break;
				case 9:
					subtituloFlujoCajaVO.setCajaMontoSeptiembre(cajaMonto);
					break;
				case 10:
					subtituloFlujoCajaVO.setCajaMontoOctubre(cajaMonto);
					break;
				case 11:
					subtituloFlujoCajaVO.setCajaMontoNoviembre(cajaMonto);
					break;
				case 12:
					subtituloFlujoCajaVO.setCajaMontoDiciembre(cajaMonto);
					break;
				default:
					break;
				}
			}		
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
			for(int mes=1; mes<=12 ; mes++){
				CajaMontoSummaryVO cajaMonto = new CajaMontoSummaryVO();
				cajaMonto.setIdMes(mes);
				cajaMonto.setMontoMes(montoMensual);
				switch (mes) {
				case 1:
					subtituloFlujoCajaVO.setCajaMontoEnero(cajaMonto);
					break;
				case 2:
					subtituloFlujoCajaVO.setCajaMontoFebrero(cajaMonto);
					break;
				case 3:
					subtituloFlujoCajaVO.setCajaMontoMarzo(cajaMonto);
					break;
				case 4:
					subtituloFlujoCajaVO.setCajaMontoAbril(cajaMonto);
					break;
				case 5:
					subtituloFlujoCajaVO.setCajaMontoMayo(cajaMonto);
					break;
				case 6:
					subtituloFlujoCajaVO.setCajaMontoJunio(cajaMonto);
					break;
				case 7:
					subtituloFlujoCajaVO.setCajaMontoJulio(cajaMonto);
					break;
				case 8:
					subtituloFlujoCajaVO.setCajaMontoAgosto(cajaMonto);
					break;
				case 9:
					subtituloFlujoCajaVO.setCajaMontoSeptiembre(cajaMonto);
					break;
				case 10:
					subtituloFlujoCajaVO.setCajaMontoOctubre(cajaMonto);
					break;
				case 11:
					subtituloFlujoCajaVO.setCajaMontoNoviembre(cajaMonto);
					break;
				case 12:
					subtituloFlujoCajaVO.setCajaMontoDiciembre(cajaMonto);
					break;
				default:
					break;
				}
			}			 
			subtituloFlujoCajaVO.setMarcoPresupuestario(0L);
			resultado.add(subtituloFlujoCajaVO);

		} 
		return resultado;
	}

	public boolean tieneMarcosConReparos(Integer idProgramaAno) {
		List<CajaMonto> cajaMontoReparos = cajaDAO.getCajaMontoReparosByProgramaAno(idProgramaAno);
		boolean tieneMarcosConReparos = false;
		if(cajaMontoReparos != null && cajaMontoReparos.size() > 0){
			tieneMarcosConReparos = true;
		}
		return tieneMarcosConReparos;
	}

	public boolean existeDistribucionRecursos(Integer idProgramaAno) {
		System.out.println("existeDistribucionRecursos Inicio idProgramaAno-->"+idProgramaAno);
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


	public SubtituloFlujoCajaVO getMonitoreoComunaByProgramaAnoServicioSubtituloMes(Integer idProgramaAno, Integer idServicio, Subtitulo subtitulo, Integer idMes, Boolean iniciarFlujoCaja) {
		LOGGER.info("Inicio getMonitoreoComunaByProgramaAnoServicioSubtituloMes");
		SubtituloFlujoCajaVO subtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
		ServicioSalud servicioSalud = servicioSaludDAO.getById(idServicio);
		subtituloFlujoCajaVO.setIdServicio(servicioSalud.getId());
		subtituloFlujoCajaVO.setServicio(servicioSalud.getNombre());
		subtituloFlujoCajaVO.setMarcoPresupuestario(0L);
		List<Integer> idComponentes = new ArrayList<Integer>();
		for(int mes = 1; mes <= 12; mes++){
			Mes mesDTO = mesDAO.getMesPorID(mes);
			CajaMontoSummaryVO cajaMontoMes = new CajaMontoSummaryVO(null, mes, mesDTO.getNombre());
			Long montoComponentes = 0L;
			List<CajaMonto> cajaMontos = cajaDAO.getByProgramaAnoServicioSubtituloMes(idProgramaAno, servicioSalud.getId(), subtitulo, mes);
			if(cajaMontos != null && cajaMontos.size() > 0){
				for(CajaMonto cajaMonto : cajaMontos){
					if(!idComponentes.contains(cajaMonto.getCaja().getIdComponente().getId())){
						idComponentes.add(cajaMonto.getCaja().getIdComponente().getId());
					}
					montoComponentes += ((cajaMonto != null) ? cajaMonto.getMonto() : 0L);
				}
			}
			cajaMontoMes.setMontoMes(montoComponentes);
			cajaMontoMes.setMontoMesOriginal(montoComponentes);
			switch (mes) {
			case 1:
				subtituloFlujoCajaVO.setCajaMontoEnero(cajaMontoMes);
				break;
			case 2:
				subtituloFlujoCajaVO.setCajaMontoFebrero(cajaMontoMes);
				break;
			case 3:
				subtituloFlujoCajaVO.setCajaMontoMarzo(cajaMontoMes);
				break;
			case 4:
				subtituloFlujoCajaVO.setCajaMontoAbril(cajaMontoMes);
				break;
			case 5:
				subtituloFlujoCajaVO.setCajaMontoMayo(cajaMontoMes);
				break;
			case 6:
				subtituloFlujoCajaVO.setCajaMontoJunio(cajaMontoMes);
				break;
			case 7:
				subtituloFlujoCajaVO.setCajaMontoJulio(cajaMontoMes);
				break;
			case 8:
				subtituloFlujoCajaVO.setCajaMontoAgosto(cajaMontoMes);
				break;
			case 9:
				subtituloFlujoCajaVO.setCajaMontoSeptiembre(cajaMontoMes);
				break;
			case 10:
				subtituloFlujoCajaVO.setCajaMontoOctubre(cajaMontoMes);
				break;
			case 11:
				subtituloFlujoCajaVO.setCajaMontoNoviembre(cajaMontoMes);
				break;
			case 12:
				subtituloFlujoCajaVO.setCajaMontoDiciembre(cajaMontoMes);
				break;
			default:
				break;
			}
		}
		Long marcoPresupuestario = programasDAO.getMPComunasServicioProgramaAnoComponenteSubtitulo(servicioSalud.getId(), idProgramaAno, idComponentes, subtitulo.getId());
		subtituloFlujoCajaVO.setMarcoPresupuestario(marcoPresupuestario);

		if(!iniciarFlujoCaja){
			Integer mesActual = Integer.parseInt(getMesCurso(true));
			ConveniosSummaryVO convenioRecibido = new ConveniosSummaryVO(0.0, 0);
			TransferenciaSummaryVO transferenciasAcumulada = new TransferenciaSummaryVO(0.0, 0L);
			Integer [] estados = {EstadosConvenios.APROBADO.getId(), EstadosConvenios.PAGADO.getId()};
			Long totalConveniosPorServicio = 0L;
			for(Integer idComponente : idComponentes){
				List<ConvenioComuna> conveniosComuna = conveniosDAO.getConveniosComunaByProgramaAnoComponenteSubtituloServicioEstadosConvenio(idProgramaAno, idComponente, subtitulo.getId(), subtituloFlujoCajaVO.getIdServicio(), estados);
				if(conveniosComuna != null && conveniosComuna.size() > 0){
					for(ConvenioComuna convenioComuna : conveniosComuna){
						if(convenioComuna.getMes() != null && convenioComuna.getMes().getIdMes() <= mesActual){
							if(convenioComuna.getConvenioComunaComponentes() != null && convenioComuna.getConvenioComunaComponentes().size() > 0){
								int size = convenioComuna.getConvenioComunaComponentes().size();
								ConvenioComunaComponente convenioComunaComponente = convenioComuna.getConvenioComunaComponentes().get((size-1));
								totalConveniosPorServicio += convenioComunaComponente.getMontoIngresado();
							}
						}
					}
				}
			}
			convenioRecibido.setMonto(totalConveniosPorServicio.intValue());
			if(marcoPresupuestario != 0){
				Double porcentaje = ((totalConveniosPorServicio * 100.0) / marcoPresupuestario);
				porcentaje = porcentaje / 100.0;
				convenioRecibido.setPorcentaje(porcentaje);
			}
			subtituloFlujoCajaVO.setConvenioRecibido(convenioRecibido);

			Long totalRemesasPorServicio = 0L;
			List<DetalleRemesas> detalleRemesas = remesasDAO.getRemesasPagadasComunasProgramaAnoServicioSubtituloMesHasta(idProgramaAno, subtituloFlujoCajaVO.getIdServicio(), subtitulo.getId(), mesActual);
			if(detalleRemesas != null && detalleRemesas.size() > 0){
				for(DetalleRemesas detalleRemesa : detalleRemesas){
					totalRemesasPorServicio += detalleRemesa.getMontoRemesa();
				}
			}

			transferenciasAcumulada.setMonto(totalRemesasPorServicio);
			if(marcoPresupuestario != 0){
				Double porcentajeRemesas = ((totalRemesasPorServicio * 100.0) / marcoPresupuestario);
				porcentajeRemesas = porcentajeRemesas / 100.0;
				transferenciasAcumulada.setPorcentaje(porcentajeRemesas);
			}
			subtituloFlujoCajaVO.setTransferenciaAcumulada(transferenciasAcumulada);
		}

		System.out.println("getMonitoreoComunaByProgramaAnoServicioSubtituloMes-->subtituloFlujoCajaVO=" + subtituloFlujoCajaVO);
		LOGGER.info("Fin getMonitoreoComunaByProgramaAnoServicioSubtituloMes");
		return subtituloFlujoCajaVO;
	}


	public SubtituloFlujoCajaVO getMonitoreoServicioByProgramaAnoServicioSubtituloMes(Integer idProgramaAno, Integer idServicio, Subtitulo subtitulo, Integer idMes, Boolean iniciarFlujoCaja) {
		LOGGER.info("Inicio getMonitoreoServicioByProgramaAnoServicioSubtituloMes");
		SubtituloFlujoCajaVO subtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
		ServicioSalud servicioSalud = servicioSaludDAO.getById(idServicio);
		subtituloFlujoCajaVO.setIdServicio(servicioSalud.getId());
		subtituloFlujoCajaVO.setServicio(servicioSalud.getNombre());
		subtituloFlujoCajaVO.setMarcoPresupuestario(0L);
		List<Integer> idComponentes = new ArrayList<Integer>();
		for(int mes = 1; mes <= 12; mes++){
			Mes mesDTO = mesDAO.getMesPorID(mes);
			CajaMontoSummaryVO cajaMontoMes = new CajaMontoSummaryVO(null, mes, mesDTO.getNombre());
			Long montoComponentes = 0L;
			List<CajaMonto> cajaMontos = cajaDAO.getByProgramaAnoServicioSubtituloMes(idProgramaAno, servicioSalud.getId(), subtitulo, mes);
			if(cajaMontos != null && cajaMontos.size() > 0){
				for(CajaMonto cajaMonto : cajaMontos){
					if(!idComponentes.contains(cajaMonto.getCaja().getIdComponente().getId())){
						idComponentes.add(cajaMonto.getCaja().getIdComponente().getId());
					}
					montoComponentes += ((cajaMonto != null) ? cajaMonto.getMonto() : 0L);
				}
			}
			cajaMontoMes.setMontoMes(montoComponentes);
			cajaMontoMes.setMontoMesOriginal(montoComponentes);
			switch (mes) {
			case 1:
				subtituloFlujoCajaVO.setCajaMontoEnero(cajaMontoMes);
				break;
			case 2:
				subtituloFlujoCajaVO.setCajaMontoFebrero(cajaMontoMes);
				break;
			case 3:
				subtituloFlujoCajaVO.setCajaMontoMarzo(cajaMontoMes);
				break;
			case 4:
				subtituloFlujoCajaVO.setCajaMontoAbril(cajaMontoMes);
				break;
			case 5:
				subtituloFlujoCajaVO.setCajaMontoMayo(cajaMontoMes);
				break;
			case 6:
				subtituloFlujoCajaVO.setCajaMontoJunio(cajaMontoMes);
				break;
			case 7:
				subtituloFlujoCajaVO.setCajaMontoJulio(cajaMontoMes);
				break;
			case 8:
				subtituloFlujoCajaVO.setCajaMontoAgosto(cajaMontoMes);
				break;
			case 9:
				subtituloFlujoCajaVO.setCajaMontoSeptiembre(cajaMontoMes);
				break;
			case 10:
				subtituloFlujoCajaVO.setCajaMontoOctubre(cajaMontoMes);
				break;
			case 11:
				subtituloFlujoCajaVO.setCajaMontoNoviembre(cajaMontoMes);
				break;
			case 12:
				subtituloFlujoCajaVO.setCajaMontoDiciembre(cajaMontoMes);
				break;
			default:
				break;
			}
		}
		Long marcoPresupuestario = programasDAO.getMPEstablecimientosServicioProgramaAnoComponenteSubtitulo(servicioSalud.getId(), idProgramaAno, idComponentes, subtitulo.getId());
		subtituloFlujoCajaVO.setMarcoPresupuestario(marcoPresupuestario);

		if(!iniciarFlujoCaja){
			Integer mesActual = Integer.parseInt(getMesCurso(true));
			ConveniosSummaryVO convenioRecibido = new ConveniosSummaryVO(0.0, 0);
			TransferenciaSummaryVO transferenciasAcumulada = new TransferenciaSummaryVO(0.0, 0L);
			Integer [] estados = {EstadosConvenios.APROBADO.getId(), EstadosConvenios.PAGADO.getId()};
			Long totalConveniosPorServicio = 0L;
			for(Integer idComponente : idComponentes){
				List<ConvenioServicio> conveniosServicio = conveniosDAO.getConveniosServicioByProgramaAnoComponenteSubtituloServicioEstadosConvenio(idProgramaAno, idComponente, subtitulo.getId(), subtituloFlujoCajaVO.getIdServicio(), estados);
				if(conveniosServicio != null && conveniosServicio.size() > 0){
					for(ConvenioServicio convenioServicio : conveniosServicio){
						if(convenioServicio.getMes() != null && convenioServicio.getMes().getIdMes() <= mesActual){
							if(convenioServicio.getConvenioServicioComponentes() != null && convenioServicio.getConvenioServicioComponentes().size() > 0){
								int size = convenioServicio.getConvenioServicioComponentes().size();
								ConvenioServicioComponente convenioServicioComponente = convenioServicio.getConvenioServicioComponentes().get((size-1));
								totalConveniosPorServicio += convenioServicioComponente.getMontoIngresado();
							}
						}
					}
				}
			}
			convenioRecibido.setMonto(totalConveniosPorServicio.intValue());
			if(marcoPresupuestario != 0){
				Double porcentaje = ((totalConveniosPorServicio * 100.0) / marcoPresupuestario);
				porcentaje = porcentaje / 100.0;
				convenioRecibido.setPorcentaje(porcentaje);
			}
			subtituloFlujoCajaVO.setConvenioRecibido(convenioRecibido);

			Long totalRemesasPorServicio = 0L;
			List<DetalleRemesas> detalleRemesas = remesasDAO.getRemesasPagadasEstablecimientosProgramaAnoServicioSubtituloMesHasta(idProgramaAno, subtituloFlujoCajaVO.getIdServicio(), subtitulo.getId(), mesActual);
			if(detalleRemesas != null && detalleRemesas.size() > 0){
				for(DetalleRemesas detalleRemesa : detalleRemesas){
					totalRemesasPorServicio += detalleRemesa.getMontoRemesa();
				}
			}
			transferenciasAcumulada.setMonto(totalRemesasPorServicio);
			if(marcoPresupuestario != 0){
				Double porcentajeRemesas = ((totalRemesasPorServicio * 100.0) / marcoPresupuestario);
				porcentajeRemesas = porcentajeRemesas / 100.0;
				transferenciasAcumulada.setPorcentaje(porcentajeRemesas);
			}
			subtituloFlujoCajaVO.setTransferenciaAcumulada(transferenciasAcumulada);
		}
		System.out.println("getMonitoreoServicioByProgramaAnoServicioSubtituloMes-->subtituloFlujoCajaVO=" + subtituloFlujoCajaVO);
		LOGGER.info("Fin getMonitoreoServicioByProgramaAnoServicioSubtituloMes");
		return subtituloFlujoCajaVO;
	}

	public Integer crearIntanciaConsolidador(String username) {
		System.out.println("username-->"+username);
		Usuario usuario = this.usuarioDAO.getUserByUsername(username);
		AnoEnCurso anoEnCurso = anoDAO.getAnoById(getAnoCurso() + 1);
		Mes mes = mesDAO.getMesPorID(Integer.parseInt(getMesCurso(true)));
		return estimacionFlujoCajaDAO.crearIntanciaConsolidador(usuario, mes, anoEnCurso);
	}

	public void elaborarOrdinarioProgramacionPlanilla(Integer idProceso) {

		asdad
	}

	public void administrarVersionesFinalesConsolidador(Integer idProceso) {
		asdad

	}

	public void enviarOrdinarioFonasaConsolidador(Integer idProceso) {

		asdasds
	}

	public List<SeguimientoVO> getBitacora(Integer idConvenio, TareasSeguimiento tareaSeguimiento) {
		return seguimientoService.getBitacoraConvenio(idConvenio, tareaSeguimiento);
	}

	public Integer getPlantillaCorreo(TipoDocumentosProcesos plantilla) {
		Integer plantillaId = documentService.getPlantillaByType(plantilla);
		return documentService.getDocumentoIdByPlantillaId(plantillaId);
	}

	public Integer cargarPlantillaCorreo(TipoDocumentosProcesos plantilla, File file) {
		Integer plantillaId = documentService.getPlantillaByType(plantilla);
		String filename = "plantillaCorreoOrdinarioProgramacionCaja.xml";
		MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
		filename = tmpDir + File.separator + filename;
		String contentType = mimemap.getContentType(filename.toLowerCase());
		if(plantillaId == null){
			try {
				BodyVO response = alfrescoService.uploadDocument(file, contentType, folderTemplateEstimacionFlujoCaja);
				System.out.println("response upload template --->"+response);
				plantillaId = documentService.createTemplate(plantilla, response.getNodeRef(), response.getFileName(), contentType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			plantillaId = documentService.getDocumentoIdByPlantillaId(plantillaId);
			try {
				BodyVO response = alfrescoService.uploadDocument(file, contentType, folderTemplateEstimacionFlujoCaja);
				System.out.println("response upload template --->"+response);
				documentService.updateDocumentTemplate(plantillaId, response.getNodeRef(), response.getFileName(), contentType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return plantillaId;
	}

	public Integer createSeguimientoFlujoCaja(Integer idProceso, TareasSeguimiento tareaSeguimiento, String subject, String body, String username, List<String> para,
			List<String> conCopia, List<String> conCopiaOculta, List<Integer> documentos) {
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
				BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummaryVO.getPath()), contenType, folderEstimacionFlujoCaja.replace("{ANO}", getAnoCurso().toString()));
				System.out.println("response upload template --->"+response);
				documentService.updateDocumentTemplate(referenciaDocumentoId, response.getNodeRef(), response.getFileName(), contenType);
			}
		}
		Integer idSeguimiento = seguimientoService.createSeguimiento(tareaSeguimiento, subject, body, from, para, conCopia, conCopiaOculta, documentosTmp);
		Seguimiento seguimiento = seguimientoDAO.getSeguimientoById(idSeguimiento);
		return estimacionFlujoCajaDAO.createSeguimientoConsolidador(idProceso, seguimiento);
	}

	public void moveToAlfresco(Integer idProceso, Integer referenciaDocumentoId, TipoDocumentosProcesos tipoDocumento, Boolean lastVersion) {
		System.out.println("Buscando referenciaDocumentoId="+referenciaDocumentoId);
		System.out.println("Buscando idProceso="+idProceso);
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummary = documentService.getDocumentSummary(referenciaDocumentoId);
		System.out.println("Buscando referenciaDocumentoSummary="+referenciaDocumentoSummary);
		if(referenciaDocumentoSummary != null){
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contenType = mimemap.getContentType(referenciaDocumentoSummary.getPath().toLowerCase());
			BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummary.getPath()), contenType, folderEstimacionFlujoCaja.replace("{ANO}", getAnoCurso().toString()));
			System.out.println("response upload template --->"+response);
			documentService.updateDocumentTemplate(referenciaDocumentoSummary.getId(), response.getNodeRef(), response.getFileName(), contenType);
			FlujoCajaConsolidador flujoCajaConsolidador = estimacionFlujoCajaDAO.findFlujoCajaConsolidadorById(idProceso);
			documentService.createDocumentFlujoCaja(flujoCajaConsolidador, tipoDocumento, referenciaDocumentoId, lastVersion);
		}
	}

	public List<ReporteEmailsEnviadosVO> getReporteCorreosByConvenio(Integer idProceso) {
		List<ReporteEmailsEnviadosVO> reporteCorreos = new ArrayList<ReporteEmailsEnviadosVO>();
		List<ReporteEmailsFlujoCajaConsolidador> reportes = estimacionFlujoCajaDAO.getReporteCorreosByFlujoCajaConsolidador(idProceso);
		System.out.println("getReporteCorreosByConvenio reportes.size()=" + ((reportes == null) ? 0 : reportes.size()) );
		if(reportes != null && reportes.size() > 0){
			for(ReporteEmailsFlujoCajaConsolidador reporte : reportes){
				ReporteEmailsEnviadosVO correo = new ReporteEmailsEnviadosVO();
				if(reporte.getReporteEmailsEnviados().getIdServicio() != null){
					correo.setIdServicio(reporte.getReporteEmailsEnviados().getIdServicio().getId());
					correo.setNombreServicio(reporte.getReporteEmailsEnviados().getIdServicio().getNombre());
				}	
				correo.setFecha(reporte.getReporteEmailsEnviados().getFecha());
				correo.setFechaFormat(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(reporte.getReporteEmailsEnviados().getFecha()));
				Set<ReporteEmailsAdjuntos> adjuntos = reporte.getReporteEmailsEnviados().getReporteEmailsAdjuntosSet();
				if(adjuntos != null && adjuntos.size() > 0){
					List<AdjuntosVO> adjs = new ArrayList<AdjuntosVO>();
					for(ReporteEmailsAdjuntos adj : adjuntos){
						AdjuntosVO ad = new AdjuntosVO();
						ad.setId(adj.getDocumento().getId());
						ad.setNombre(adj.getDocumento().getPath());
						adjs.add(ad);
					}
					correo.setAdjuntos(adjs);
				}
				Set<ReporteEmailsDestinatarios> destinatarios = reporte.getReporteEmailsEnviados().getReporteEmailsDestinatariosSet();
				if(destinatarios != null && destinatarios.size() > 0){
					List<String> to = new ArrayList<String>();
					List<String> cc = new ArrayList<String>();
					for(ReporteEmailsDestinatarios destina : destinatarios){
						if( TiposDestinatarios.PARA.getId()  == destina.getTipoDestinatario().getIdTipoDestinatario().intValue()){
							String para = destina.getDestinatario().getEmail().getValor();
							to.add(para);
						}else if( TiposDestinatarios.CC.getId()  == destina.getTipoDestinatario().getIdTipoDestinatario().intValue()){
							String copia = destina.getDestinatario().getEmail().getValor();
							cc.add(copia);
						}
					}
					correo.setCc(cc);
					correo.setTo(to);
				}
				reporteCorreos.add(correo);
			}
		}
		return reporteCorreos;
	}

}
