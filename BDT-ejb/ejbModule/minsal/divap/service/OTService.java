package minsal.divap.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.xml.bind.JAXBException;

import minsal.divap.dao.AntecedentesComunaDAO;
import minsal.divap.dao.ComunaDAO;
import minsal.divap.dao.ConveniosDAO;
import minsal.divap.dao.DistribucionInicialPercapitaDAO;
import minsal.divap.dao.DocumentDAO;
import minsal.divap.dao.EstablecimientosDAO;
import minsal.divap.dao.InstitucionDAO;
import minsal.divap.dao.MesDAO;
import minsal.divap.dao.ProgramasDAO;
import minsal.divap.dao.RecursosFinancierosProgramasReforzamientoDAO;
import minsal.divap.dao.ReliquidacionDAO;
import minsal.divap.dao.RemesasDAO;
import minsal.divap.dao.SeguimientoDAO;
import minsal.divap.dao.ServicioSaludDAO;
import minsal.divap.dao.TipoSubtituloDAO;
import minsal.divap.dao.UsuarioDAO;
import minsal.divap.dao.UtilitariosDAO;
import minsal.divap.doc.GeneradorResolucionAporteEstatal;
import minsal.divap.doc.GeneradorWord;
import minsal.divap.enums.EstadosConvenios;
import minsal.divap.enums.EstadosProgramas;
import minsal.divap.enums.Instituciones;
import minsal.divap.enums.Subtitulo;
import minsal.divap.enums.TareasSeguimiento;
import minsal.divap.enums.TipoDocumentosProcesos;
import minsal.divap.enums.TiposDestinatarios;
import minsal.divap.excel.GeneradorExcel;
import minsal.divap.excel.impl.OrdenesTransferenciaSheetExcel;
import minsal.divap.model.mappers.ProgramaMapper;
import minsal.divap.model.mappers.SeguimientoMapper;
import minsal.divap.vo.AdjuntosVO;
import minsal.divap.vo.BodyVO;
import minsal.divap.vo.CellExcelVO;
import minsal.divap.vo.ComunaVO;
import minsal.divap.vo.DiaVO;
import minsal.divap.vo.DocumentoVO;
import minsal.divap.vo.EstablecimientoVO;
import minsal.divap.vo.OTPerCapitaVO;
import minsal.divap.vo.OTResumenDependienteServicioVO;
import minsal.divap.vo.OTResumenMunicipalVO;
import minsal.divap.vo.PagaRemesaVO;
import minsal.divap.vo.PlanillaResumenFonasaVO;
import minsal.divap.vo.ProgramaFonasaVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.ReferenciaDocumentoSummaryVO;
import minsal.divap.vo.RemesasProgramaVO;
import minsal.divap.vo.ReporteEmailsEnviadosVO;
import minsal.divap.vo.ResumenFONASAMunicipalVO;
import minsal.divap.vo.ResumenFONASAServicioVO;
import minsal.divap.vo.ResumenProgramaMixtoVO;
import minsal.divap.vo.SeguimientoVO;
import minsal.divap.xml.GeneradorXML;
import minsal.divap.xml.email.Email;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.docx4j.openpackaging.exceptions.Docx4JException;

import cl.minsal.divap.model.AntecendentesComunaCalculado;
import cl.minsal.divap.model.ConvenioComuna;
import cl.minsal.divap.model.ConvenioComunaComponente;
import cl.minsal.divap.model.ConvenioServicio;
import cl.minsal.divap.model.ConvenioServicioComponente;
import cl.minsal.divap.model.Cuota;
import cl.minsal.divap.model.Destinatarios;
import cl.minsal.divap.model.DetalleRemesas;
import cl.minsal.divap.model.EstadoConvenio;
import cl.minsal.divap.model.EstadoPrograma;
import cl.minsal.divap.model.Festivos;
import cl.minsal.divap.model.Institucion;
import cl.minsal.divap.model.Mes;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.ProgramaFechaRemesa;
import cl.minsal.divap.model.ReferenciaDocumento;
import cl.minsal.divap.model.ReporteEmailsAdjuntos;
import cl.minsal.divap.model.ReporteEmailsDestinatarios;
import cl.minsal.divap.model.ReporteEmailsEnviados;
import cl.minsal.divap.model.ReporteEmailsRemesas;
import cl.minsal.divap.model.Seguimiento;
import cl.minsal.divap.model.ServicioSalud;
import cl.minsal.divap.model.TipoDestinatario;
import cl.minsal.divap.model.Usuario;

@Stateless
@LocalBean
public class OTService {

	@EJB
	private ProgramasDAO programasDAO;
	@EJB
	private EstablecimientosDAO establecimientosDAO;
	@EJB
	private MesDAO mesDAO;
	@EJB
	private InstitucionDAO institucionDAO;
	@EJB
	private RecursosFinancierosProgramasReforzamientoDAO recursosFinancierosProgramasReforzamientoDAO;
	@EJB
	private UtilitariosDAO utilitariosDAO;
	@EJB
	private ServicioSaludDAO servicioSaludDAO;
	@EJB
	private TipoSubtituloDAO subtituloDAO;
	@EJB
	private AntecedentesComunaDAO antecedentesComunaDAO;
	@EJB
	private RemesasDAO remesasDAO;
	@EJB
	private ComunaDAO comunaDAO;
	@EJB
	private ConveniosDAO conveniosDAO;
	@EJB
	private ReliquidacionDAO reliquidacionDAO;
	@EJB
	private DistribucionInicialPercapitaDAO perCapitaDAO;
	@EJB
	private UsuarioDAO usuarioDAO;
	@EJB
	private SeguimientoDAO seguimientoDAO;
	@EJB
	private DocumentDAO documentoDAO;
	@EJB
	private ReportesServices reporteService;
	@EJB
	private ProgramasService programasService;
	@EJB
	private DocumentService documentService;
	@EJB
	private AlfrescoService alfrescoService;
	@EJB
	private SeguimientoService seguimientoService;
	@EJB
	private EmailService emailService;

	static long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000;
	static Integer REMESA_REGULAR=24;
	static Integer CONVENIO_EN_TRAMITE=5;

	@Resource(name="folderOrdenesTransferencia")
	private String folderOrdenesTransferencia;
	@Resource(name="tmpDir")
	private String tmpDir;
	@Resource(name = "tmpDirDoc")
	private String tmpDirDoc;



	public List<ProgramaVO> getProgramas(String username) {
		List<ProgramaVO> programasVO = new ArrayList<ProgramaVO>();
		List<ProgramaAno> programas = programasDAO.getProgramasByUserAno(username, getAnoCurso());
		if(programas != null && programas.size() > 0){
			for(ProgramaAno programa : programas){
				programasVO.add(new ProgramaMapper().getBasic(programa));
			}
		}
		return programasVO;
	}

	public Integer getAnoCurso() {
		DateFormat formatNowYear = new SimpleDateFormat("yyyy");
		Date nowDate = new Date();
		return Integer.valueOf(formatNowYear.format(nowDate)); 
	}


	public ProgramaVO getProgramaById(Integer programaSeleccionado) {
		ProgramaAno programaAno = recursosFinancierosProgramasReforzamientoDAO.findById(programaSeleccionado);
		return new ProgramaMapper().getBasic(programaAno);
	}

	public List<OTResumenDependienteServicioVO> getDetalleOTServicio(Integer componenteSeleccionado,
			Integer servicioSeleccionado, Integer idTipoSubtitulo, Integer idProgramaAno) {


		List<ConvenioServicioComponente> conveniosAprobados = conveniosDAO.getConveniosServicioComponenteByProgramaAnoComponenteSubtituloServicioEstadoConvenio(
				idProgramaAno, componenteSeleccionado, idTipoSubtitulo, servicioSeleccionado, EstadosConvenios.APROBADO.getId());

		List<OTResumenDependienteServicioVO> listaOTResumenDependienteServicioVO = new ArrayList<OTResumenDependienteServicioVO>();
		List<Cuota> cuotasPrograma = reliquidacionDAO.getCuotasByProgramaAno(idProgramaAno);
		List<Integer> idConvenios = new ArrayList<Integer>();
		boolean registrado = false;
		for (ConvenioServicioComponente convenioServicio : conveniosAprobados) {

			OTResumenDependienteServicioVO prog = new OTResumenDependienteServicioVO();

			EstablecimientoVO establecimiento = new EstablecimientoVO();
			establecimiento.setCodigoEstablecimiento(convenioServicio.getConvenioServicio().getIdEstablecimiento().getCodigo());
			establecimiento.setId(convenioServicio.getConvenioServicio().getIdEstablecimiento().getId());
			establecimiento.setId_servicio_salud(convenioServicio.getConvenioServicio().getIdEstablecimiento().getServicioSalud().getId());
			establecimiento.setNombre(convenioServicio.getConvenioServicio().getIdEstablecimiento().getNombre());

			prog.setEstablecimiento(establecimiento);
			idConvenios.add(convenioServicio.getConvenioServicio().getConvenio().getIdConvenio());
			prog.setIdConvenios(idConvenios);				
			Long mp = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(convenioServicio.getConvenioServicio().getIdEstablecimiento().getId(),
					idProgramaAno,componenteSeleccionado,idTipoSubtitulo);
			prog.setMarcoPresupuestario(mp);


			List<DetalleRemesas> remesasPagadasEstablecimiento = remesasDAO.getRemesasPagadasEstablecimiento(idProgramaAno, convenioServicio.getConvenioServicio().getIdEstablecimiento().getId(), idTipoSubtitulo);

			Long acumulado=0l;
			for(DetalleRemesas pagadas : remesasPagadasEstablecimiento){
				acumulado += pagadas.getMontoRemesa();
			}
			prog.setTransferenciaAcumulada(acumulado);


			List<ConvenioServicioComponente> conveniosAprobadosEstablecimiento = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(
					idProgramaAno, componenteSeleccionado, idTipoSubtitulo, convenioServicio.getConvenioServicio().getIdEstablecimiento().getId(), EstadosConvenios.APROBADO.getId());


			List<RemesasProgramaVO> remesas = new ArrayList<RemesasProgramaVO>();
			try {
				remesas = getRemesasPrograma(idProgramaAno, Integer.parseInt(getMesCurso(true)));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			List<ConvenioServicioComponente> conveniosPagadosEstablecimiento = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(
					idProgramaAno, componenteSeleccionado, idTipoSubtitulo, convenioServicio.getConvenioServicio().getIdEstablecimiento().getId(), EstadosConvenios.PAGADO.getId());


			Cuota primeraCuotaPrograma = reliquidacionDAO.getCuotaByIdProgramaAnoNroCuota(idProgramaAno, (short)1);

			//ES el primero convenio, por tanto se paga sobre el % de la primera cuota
			if(conveniosPagadosEstablecimiento.size()==0){
				Long totalAprobado=0l;
				for(ConvenioServicioComponente aprobadoEstablecimiento : conveniosAprobadosEstablecimiento)	{
					totalAprobado += aprobadoEstablecimiento.getMonto();
				}
				Long remesa = Math.round(totalAprobado*(primeraCuotaPrograma.getPorcentaje()/100.0));
				prog.setCuota(primeraCuotaPrograma);
				boolean asignado=false;
				for(RemesasProgramaVO remesaPrograma : remesas){
					System.out.println(remesaPrograma.getMes());
					for(DiaVO dia : remesaPrograma.getDias()){
						if(!dia.isBloqueado() && !asignado){
							System.out.println(dia.getDia());
							dia.setMonto(remesa);
							asignado=true;
						}else{
							dia.setMonto(0l);
						}

					}
				}

				// Si existen pagos ya realizados para un convenio anterior
			}else{
				Long totalConveniosPagados=0l;
				for(ConvenioServicioComponente pagado: conveniosPagadosEstablecimiento){
					totalConveniosPagados += pagado.getMonto();
				}
				Long totalRemesasPagadas = 0l;
				for(DetalleRemesas pagadasEstablecimiento : remesasPagadasEstablecimiento){
					totalRemesasPagadas += pagadasEstablecimiento.getMontoRemesa();
				}
				Long cuota1= Math.round(mp*(primeraCuotaPrograma.getPorcentaje()/100.0));

				//Si aún no cubro el porcentaje de la primera cuota con las remesas pagadas
				if(!cuota1.equals(totalRemesasPagadas)){
					prog.setCuota(primeraCuotaPrograma);
					for(ConvenioServicioComponente aprobado: conveniosAprobadosEstablecimiento){
						totalConveniosPagados += aprobado.getMonto();
					}
					Long remesa = Math.round(totalConveniosPagados*(primeraCuotaPrograma.getPorcentaje()/100.0));
					Long aPagar = remesa - totalRemesasPagadas;
					boolean asignado=false;
					for(RemesasProgramaVO remesaPrograma : remesas){
						System.out.println(remesaPrograma.getMes());
						for(DiaVO dia : remesaPrograma.getDias()){
							if(!dia.isBloqueado() && !asignado){
								System.out.println(dia.getDia());
								dia.setMonto(aPagar);
								asignado=true;
							}else{
								dia.setMonto(0l);
							}
						}
					}
					// Si ya cubrí el porcentaje de la primera cuota con las remesas pagadas
				}else{

					System.out.println("Cantidad de cuotas: "+ cuotasPrograma.size());

					//Si el programa tiene 2 cuotas, ya sabemos que la cuota 1 se pagó.
					if(cuotasPrograma.size() == 2){
						for(Cuota cuota : cuotasPrograma){
							//Si estoy en el mes de la segunda cuota
							if(cuota.getIdMes() !=null && (cuota.getIdMes().getIdMes() ==  Integer.parseInt(getMesCurso(true)))){

								//Entonces debo buscar la reliquidación del establecimiento en cuestión



								//Si no estoy en el mes de la segunda cuota lleno con 0
							}else{
								for(RemesasProgramaVO remesaPrograma : remesas){
									System.out.println(remesaPrograma.getMes());
									for(DiaVO dia : remesaPrograma.getDias()){
										dia.setMonto(0l);
									}
								}
							}
						}
					}
					//Si el programa tiene más de 2 cuotas
					if(cuotasPrograma.size() > 2){

					}
				}
			}

			prog.setRemesas(remesas);

			Long acumulador=0l;
			if(!registrado){
				for(ConvenioServicioComponente aprobados : conveniosAprobadosEstablecimiento){
					acumulador += aprobados.getMonto();
				}
				for(ConvenioServicioComponente pagados : conveniosPagadosEstablecimiento){
					acumulador += pagados.getMonto();
				}
				registrado=true;
			}


			if(listaOTResumenDependienteServicioVO.indexOf(prog) != -1){
				prog = listaOTResumenDependienteServicioVO.get(listaOTResumenDependienteServicioVO.indexOf(prog));
				prog.setConveniosRecibidos(prog.getConveniosRecibidos()+acumulador);
			}else{
				prog.setMarcoPresupuestario(mp);
				prog.setConveniosRecibidos(acumulador);
				listaOTResumenDependienteServicioVO.add(prog);
			}
		}

		//Preguntamos si existe alguna remesa por pagar para el mes actual y la agregamos al listado
		List<DetalleRemesas> remesasMesActual = remesasDAO.getRemesasMesActualByMesProgramaAnoServicioSubtitulo(Integer.parseInt(getMesCurso(true)),idProgramaAno,servicioSeleccionado, idTipoSubtitulo);
		List<Integer> idDetalleRemesa = new ArrayList<Integer>();

		for(DetalleRemesas remesaMes : remesasMesActual){
			OTResumenDependienteServicioVO prog = new OTResumenDependienteServicioVO();
			EstablecimientoVO establecimiento = new EstablecimientoVO();
			establecimiento.setCodigoEstablecimiento(remesaMes.getEstablecimiento().getCodigo());
			establecimiento.setId(remesaMes.getEstablecimiento().getId());
			establecimiento.setId_servicio_salud(remesaMes.getEstablecimiento().getServicioSalud().getId());
			establecimiento.setNombre(remesaMes.getEstablecimiento().getNombre());

			prog.setEstablecimiento(establecimiento);

			List<RemesasProgramaVO> remesas = new ArrayList<RemesasProgramaVO>();
			try {
				remesas = getRemesasPrograma(idProgramaAno, Integer.parseInt(getMesCurso(true)));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}

			prog.setCuota(remesaMes.getCuota());
			Cuota ultimaCuota = cuotasPrograma.get(cuotasPrograma.size()-1);

			// busco la remesa y la aplico
			if(remesaMes.getCuota().getNumeroCuota() == ultimaCuota.getNumeroCuota()){
				System.out.println("BUSCANDO RELIQUIDACION PARA ESTABLECIMIENTO "+ establecimiento.getNombre());
			}

			for(RemesasProgramaVO remesaPrograma : remesas){
				System.out.println(remesaPrograma.getMes());
				for(DiaVO dia : remesaPrograma.getDias()){
					if(dia.getDia() ==remesaMes.getDia().getId() && remesaPrograma.getIdMes() == remesaMes.getMes().getIdMes()){
						dia.setMonto(remesaMes.getMontoRemesa().longValue());
					}else{
						dia.setMonto(0l);
					}
				}
			}
			prog.setRemesas(remesas);

			Long mp = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(remesaMes.getEstablecimiento().getId(),
					idProgramaAno,componenteSeleccionado,idTipoSubtitulo);
			prog.setMarcoPresupuestario(mp);


			List<DetalleRemesas> remesasPagadasEstablecimiento = remesasDAO.getRemesasPagadasEstablecimiento(idProgramaAno, remesaMes.getEstablecimiento().getId(), idTipoSubtitulo);

			Long acumulado=0l;
			for(DetalleRemesas pagadas : remesasPagadasEstablecimiento){
				acumulado += pagadas.getMontoRemesa();
			}
			prog.setTransferenciaAcumulada(acumulado);



			List<ConvenioServicioComponente> conveniosAprobadosEstablecimiento = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(
					idProgramaAno, componenteSeleccionado, idTipoSubtitulo,remesaMes.getEstablecimiento().getId(), EstadosConvenios.APROBADO.getId());

			List<ConvenioServicioComponente> conveniosPagadosEstablecimiento = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(
					idProgramaAno, componenteSeleccionado, idTipoSubtitulo, remesaMes.getEstablecimiento().getId(), EstadosConvenios.PAGADO.getId());

			List<ConvenioServicioComponente> conveniosTramiteEstablecimiento = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(
					idProgramaAno, componenteSeleccionado, idTipoSubtitulo, remesaMes.getEstablecimiento().getId(), EstadosConvenios.TRAMITE.getId());

			Long acumulador=0l;
			for(ConvenioServicioComponente aprobados : conveniosAprobadosEstablecimiento){
				acumulador += aprobados.getMonto();
			}
			for(ConvenioServicioComponente pagados : conveniosPagadosEstablecimiento){
				acumulador += pagados.getMonto();
			}
			for(ConvenioServicioComponente tramites : conveniosTramiteEstablecimiento){
				acumulador += tramites.getMonto();
			}

			idDetalleRemesa.add(remesaMes.getIdDetalleRemesa());
			prog.setIdDetalleRemesa(idDetalleRemesa);

			if(listaOTResumenDependienteServicioVO.size()>0){
				if(listaOTResumenDependienteServicioVO.indexOf(prog) == -1){
					prog.setConveniosRecibidos(acumulador);
					listaOTResumenDependienteServicioVO.add(prog);
				}else{
					List<RemesasProgramaVO> remesasEstab = listaOTResumenDependienteServicioVO.get(listaOTResumenDependienteServicioVO.indexOf(prog)).getRemesas();
					for(RemesasProgramaVO remesaEst : remesasEstab){
						for(RemesasProgramaVO remesaProg : prog.getRemesas()){
							for(DiaVO diaEstab : remesaEst.getDias()){
								for(DiaVO diaProg : remesaProg.getDias()){
									if(remesaEst.getIdMes() == remesaProg.getIdMes()){
										if(diaEstab.getDia() == diaProg.getDia()){
											diaEstab.setMonto(diaEstab.getMonto()+diaProg.getMonto());
										}
									}
								}
							}
						}
					}


					prog = listaOTResumenDependienteServicioVO.get(listaOTResumenDependienteServicioVO.indexOf(prog));
					prog.setConveniosRecibidos(prog.getConveniosRecibidos()+acumulador);
					prog.setRemesas(remesasEstab);

				}
			}else{
				prog.setConveniosRecibidos(acumulador);
				listaOTResumenDependienteServicioVO.add(prog);
			}



		}

		return listaOTResumenDependienteServicioVO;

	}

	public List<OTResumenDependienteServicioVO> getDetalleOTServicioConsolidador(Integer componenteSeleccionado,
			Integer servicioSeleccionado, Integer idTipoSubtitulo, Integer idProgramaAno) {

		List<OTResumenDependienteServicioVO> listaOTResumenDependienteServicioVO = new ArrayList<OTResumenDependienteServicioVO>();
		List<Cuota> cuotasPrograma = reliquidacionDAO.getCuotasByProgramaAno(idProgramaAno);

		//Preguntamos si existe alguna remesa por pagar para el mes actual
		List<DetalleRemesas> remesasMesActual = remesasDAO.getRemesasMesActualByMesProgramaAnoServicioSubtitulo(Integer.parseInt(getMesCurso(true)),idProgramaAno,servicioSeleccionado, idTipoSubtitulo);
		List<Integer> idDetalleRemesa = new ArrayList<Integer>();

		for(DetalleRemesas remesaMes : remesasMesActual){
			OTResumenDependienteServicioVO prog = new OTResumenDependienteServicioVO();
			EstablecimientoVO establecimiento = new EstablecimientoVO();
			establecimiento.setCodigoEstablecimiento(remesaMes.getEstablecimiento().getCodigo());
			establecimiento.setId(remesaMes.getEstablecimiento().getId());
			establecimiento.setId_servicio_salud(remesaMes.getEstablecimiento().getServicioSalud().getId());
			establecimiento.setNombre(remesaMes.getEstablecimiento().getNombre());

			prog.setEstablecimiento(establecimiento);

			List<RemesasProgramaVO> remesas = new ArrayList<RemesasProgramaVO>();
			try {
				remesas = getRemesasPrograma(idProgramaAno, Integer.parseInt(getMesCurso(true)));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}

			prog.setCuota(remesaMes.getCuota());
			Cuota ultimaCuota = cuotasPrograma.get(cuotasPrograma.size()-1);

			// busco la remesa y la aplico
			if(remesaMes.getCuota().getNumeroCuota() == ultimaCuota.getNumeroCuota()){
				System.out.println("BUSCANDO RELIQUIDACION PARA ESTABLECIMIENTO "+ establecimiento.getNombre());
			}

			for(RemesasProgramaVO remesaPrograma : remesas){
				System.out.println(remesaPrograma.getMes());
				for(DiaVO dia : remesaPrograma.getDias()){
					if(dia.getDia() ==remesaMes.getDia().getId() && remesaPrograma.getIdMes() == remesaMes.getMes().getIdMes()){
						dia.setMonto(remesaMes.getMontoRemesa().longValue());
					}else{
						dia.setMonto(0l);
					}
				}
			}
			prog.setRemesas(remesas);

			Long mp = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(remesaMes.getEstablecimiento().getId(),
					idProgramaAno,componenteSeleccionado,idTipoSubtitulo);
			prog.setMarcoPresupuestario(mp);


			List<DetalleRemesas> remesasPagadasEstablecimiento = remesasDAO.getRemesasPagadasEstablecimiento(idProgramaAno, remesaMes.getEstablecimiento().getId(), idTipoSubtitulo);

			Long acumulado=0l;
			for(DetalleRemesas pagadas : remesasPagadasEstablecimiento){
				acumulado += pagadas.getMontoRemesa();
			}
			prog.setTransferenciaAcumulada(acumulado);

			List<ConvenioServicioComponente> conveniosAprobadosEstablecimiento = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(
					idProgramaAno, componenteSeleccionado, idTipoSubtitulo,remesaMes.getEstablecimiento().getId(), EstadosConvenios.APROBADO.getId());

			List<ConvenioServicioComponente> conveniosPagadosEstablecimiento = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(
					idProgramaAno, componenteSeleccionado, idTipoSubtitulo, remesaMes.getEstablecimiento().getId(), EstadosConvenios.PAGADO.getId());

			List<ConvenioServicioComponente> conveniosTramiteEstablecimiento = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio(
					idProgramaAno, componenteSeleccionado, idTipoSubtitulo, remesaMes.getEstablecimiento().getId(), EstadosConvenios.TRAMITE.getId());

			Long acumulador=0l;
			for(ConvenioServicioComponente aprobados : conveniosAprobadosEstablecimiento){
				acumulador += aprobados.getMonto();
			}
			for(ConvenioServicioComponente pagados : conveniosPagadosEstablecimiento){
				acumulador += pagados.getMonto();
			}
			for(ConvenioServicioComponente tramites : conveniosTramiteEstablecimiento){
				acumulador += tramites.getMonto();
			}

			idDetalleRemesa.add(remesaMes.getIdDetalleRemesa());
			prog.setIdDetalleRemesa(idDetalleRemesa);

			if(listaOTResumenDependienteServicioVO.size()>0){
				if(listaOTResumenDependienteServicioVO.indexOf(prog) == -1){
					prog.setConveniosRecibidos(acumulador);
					listaOTResumenDependienteServicioVO.add(prog);
				}else{
					List<RemesasProgramaVO> remesasEstab = listaOTResumenDependienteServicioVO.get(listaOTResumenDependienteServicioVO.indexOf(prog)).getRemesas();
					for(RemesasProgramaVO remesaEst : remesasEstab){
						for(RemesasProgramaVO remesaProg : prog.getRemesas()){
							for(DiaVO diaEstab : remesaEst.getDias()){
								for(DiaVO diaProg : remesaProg.getDias()){
									if(remesaEst.getIdMes() == remesaProg.getIdMes()){
										if(diaEstab.getDia() == diaProg.getDia()){
											diaEstab.setMonto(diaEstab.getMonto()+diaProg.getMonto());
										}
									}
								}
							}
						}
					}
					prog = listaOTResumenDependienteServicioVO.get(listaOTResumenDependienteServicioVO.indexOf(prog));
					prog.setConveniosRecibidos(prog.getConveniosRecibidos()+acumulador);
					prog.setRemesas(remesasEstab);
				}
			}else{
				prog.setConveniosRecibidos(acumulador);
				listaOTResumenDependienteServicioVO.add(prog);
			}
		}
		return listaOTResumenDependienteServicioVO;

	}

	public List<OTResumenMunicipalVO> getDetalleOTMunicipal(Integer componenteSeleccionado,
			Integer servicioSeleccionado, Integer idProgramaAno) {
		System.out.println("Buscando Detalle de Convenios/Remesas para Subtitulo 24");

		List<OTResumenMunicipalVO> listaOtResumenMunicipalVO = new ArrayList<OTResumenMunicipalVO>();
		List<Cuota> cuotasPrograma = reliquidacionDAO.getCuotasByProgramaAno(idProgramaAno);
		List<ConvenioComunaComponente> conveniosAprobados = conveniosDAO.getConveniosComunaComponenteByProgramaAnoComponenteServicioEstadoConvenio(
				idProgramaAno, componenteSeleccionado, servicioSeleccionado, EstadosConvenios.APROBADO.getId());
		boolean registrado = false;
		List<Integer> idConvenios = new ArrayList<Integer>();

		for(ConvenioComunaComponente aprobados : conveniosAprobados){
			OTResumenMunicipalVO prog = new OTResumenMunicipalVO();

			ComunaVO comuna = new ComunaVO();
			comuna.setIdComuna(aprobados.getConvenioComuna().getIdComuna().getId());
			comuna.setNombre(aprobados.getConvenioComuna().getIdComuna().getNombre());
			prog.setComuna(comuna);

			idConvenios.add(aprobados.getConvenioComuna().getConvenio().getIdConvenio());
			prog.setIdConvenios(idConvenios);		

			Long mp = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(aprobados.getConvenioComuna().getIdComuna().getId(), idProgramaAno,
					componenteSeleccionado, Subtitulo.SUBTITULO24.getId());
			prog.setMarcoPresupuestario(mp);

			List<DetalleRemesas> remesasPagadasComuna = remesasDAO.getRemesasPagadasComuna(idProgramaAno, aprobados.getConvenioComuna().getIdComuna().getId(), Subtitulo.SUBTITULO24.getId());

			Long acumulado=0l;
			for(DetalleRemesas pagadas : remesasPagadasComuna){
				acumulado += pagadas.getMontoRemesa();
			}
			prog.setTransferenciaAcumulada(acumulado);


			List<ConvenioComunaComponente> conveniosAprobadosComuna = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloComunaEstadoConvenio(
					idProgramaAno, componenteSeleccionado, Subtitulo.SUBTITULO24.getId(), aprobados.getConvenioComuna().getIdComuna().getId(), EstadosConvenios.APROBADO.getId());


			List<RemesasProgramaVO> remesas = new ArrayList<RemesasProgramaVO>();
			try {
				remesas = getRemesasPrograma(idProgramaAno, Integer.parseInt(getMesCurso(true)));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}


			List<ConvenioComunaComponente> conveniosPagadosComuna= conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloComunaEstadoConvenio(
					idProgramaAno, componenteSeleccionado, Subtitulo.SUBTITULO24.getId(), aprobados.getConvenioComuna().getIdComuna().getId(), EstadosConvenios.PAGADO.getId());


			Cuota primeraCuotaPrograma = reliquidacionDAO.getCuotaByIdProgramaAnoNroCuota(idProgramaAno, (short)1);

			//ES el primero convenio, por tanto se paga sobre el % de la primera cuota
			if(conveniosPagadosComuna.size()==0){
				Long totalAprobado=0l;
				for(ConvenioComunaComponente aprobadoComuna : conveniosAprobadosComuna)	{
					totalAprobado += aprobadoComuna.getMonto();
				}
				Long remesa = Math.round(totalAprobado*(primeraCuotaPrograma.getPorcentaje()/100.0));
				prog.setCuota(primeraCuotaPrograma);
				boolean asignado=false;
				for(RemesasProgramaVO remesaPrograma : remesas){
					System.out.println(remesaPrograma.getMes());
					for(DiaVO dia : remesaPrograma.getDias()){
						if(!dia.isBloqueado() && !asignado){
							System.out.println(dia.getDia());
							dia.setMonto(remesa);
							asignado=true;
						}else{
							dia.setMonto(0l);
						}

					}
				}

				// Si existen pagos ya realizados para un convenio anterior
			}else{
				Long totalConveniosPagados=0l;
				for(ConvenioComunaComponente pagado: conveniosPagadosComuna){
					totalConveniosPagados += pagado.getMonto();
				}
				Long totalRemesasPagadas = 0l;
				for(DetalleRemesas pagadasEstablecimiento : remesasPagadasComuna){
					totalRemesasPagadas += pagadasEstablecimiento.getMontoRemesa();
				}
				Long cuota1= Math.round(mp*(primeraCuotaPrograma.getPorcentaje()/100.0));

				//Si aún no cubro el porcentaje de la primera cuota con las remesas pagadas
				if(!cuota1.equals(totalRemesasPagadas)){
					prog.setCuota(primeraCuotaPrograma);
					for(ConvenioComunaComponente aprobado: conveniosAprobadosComuna){
						totalConveniosPagados += aprobado.getMonto();
					}
					Long remesa = Math.round(totalConveniosPagados*(primeraCuotaPrograma.getPorcentaje()/100.0));
					Long aPagar = remesa - totalRemesasPagadas;
					boolean asignado=false;
					for(RemesasProgramaVO remesaPrograma : remesas){
						System.out.println(remesaPrograma.getMes());
						for(DiaVO dia : remesaPrograma.getDias()){
							if(!dia.isBloqueado() && !asignado){
								System.out.println(dia.getDia());
								dia.setMonto(aPagar);
								asignado=true;
							}else{
								dia.setMonto(0l);
							}
						}
					}
					// Si ya cubrí el porcentaje de la primera cuota con las remesas pagadas
				}else{

					System.out.println("Cantidad de cuotas: "+ cuotasPrograma.size());

					//Si el programa tiene 2 cuotas, ya sabemos que la cuota 1 se pagó.
					if(cuotasPrograma.size() == 2){
						for(Cuota cuota : cuotasPrograma){
							//Si estoy en el mes de la segunda cuota
							if(cuota.getIdMes() !=null && (cuota.getIdMes().getIdMes() ==  Integer.parseInt(getMesCurso(true)))){

								//Entonces debo buscar la reliquidación del establecimiento en cuestión



								//Si no estoy en el mes de la segunda cuota lleno con 0
							}else{
								for(RemesasProgramaVO remesaPrograma : remesas){
									System.out.println(remesaPrograma.getMes());
									for(DiaVO dia : remesaPrograma.getDias()){
										dia.setMonto(0l);
									}
								}
							}
						}
					}
					//Si el programa tiene más de 2 cuotas
					if(cuotasPrograma.size() > 2){

					}
				}
			}

			prog.setRemesas(remesas);

			Long acumulador=0l;
			if(!registrado){
				for(ConvenioComunaComponente aprobadosComuna : conveniosAprobadosComuna){
					acumulador += aprobadosComuna.getMonto();
				}
				for(ConvenioComunaComponente pagados : conveniosPagadosComuna){
					acumulador += pagados.getMonto();
				}
				registrado=true;
			}


			if(listaOtResumenMunicipalVO.indexOf(prog) != -1){
				prog = listaOtResumenMunicipalVO.get(listaOtResumenMunicipalVO.indexOf(prog));
				prog.setConveniosRecibidos(prog.getConveniosRecibidos()+acumulador);
			}else{
				prog.setMarcoPresupuestario(mp);
				prog.setConveniosRecibidos(acumulador);
				listaOtResumenMunicipalVO.add(prog);
			}
		}

		//Preguntamos si existe alguna remesa por pagar para el mes actual y la agregamos al listado
		List<DetalleRemesas> remesasMesActual = remesasDAO.getRemesasMesActualByMesProgramaAnoServicioSubtituloMunicipal(Integer.parseInt(getMesCurso(true)),idProgramaAno,servicioSeleccionado, Subtitulo.SUBTITULO24.getId());
		List<Integer> idDetalleRemesa = new ArrayList<Integer>();

		for(DetalleRemesas remesaMes : remesasMesActual){
			OTResumenMunicipalVO prog = new OTResumenMunicipalVO();

			ComunaVO comuna = new ComunaVO();
			comuna.setIdComuna(remesaMes.getComuna().getId());
			comuna.setNombre(remesaMes.getComuna().getNombre());
			prog.setComuna(comuna);

			List<RemesasProgramaVO> remesas = new ArrayList<RemesasProgramaVO>();
			try {
				remesas = getRemesasPrograma(idProgramaAno, Integer.parseInt(getMesCurso(true)));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}

			prog.setCuota(remesaMes.getCuota());
			Cuota ultimaCuota = cuotasPrograma.get(cuotasPrograma.size()-1);

			// busco la remesa y la aplico
			if(remesaMes.getCuota().getNumeroCuota() == ultimaCuota.getNumeroCuota()){
				System.out.println("BUSCANDO RELIQUIDACION PARA COMUNA"+ comuna.getNombre());
			}

			for(RemesasProgramaVO remesaPrograma : remesas){
				System.out.println(remesaPrograma.getMes());
				for(DiaVO dia : remesaPrograma.getDias()){
					if(dia.getDia() ==remesaMes.getDia().getId() && remesaPrograma.getIdMes() == remesaMes.getMes().getIdMes()){
						dia.setMonto(remesaMes.getMontoRemesa().longValue());
					}else{
						dia.setMonto(0l);
					}
				}
			}
			prog.setRemesas(remesas);

			Long mp = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(remesaMes.getComuna().getId(), idProgramaAno,
					componenteSeleccionado, Subtitulo.SUBTITULO24.getId());
			prog.setMarcoPresupuestario(mp);


			List<DetalleRemesas> remesasPagadasComuna = remesasDAO.getRemesasPagadasEstablecimiento(idProgramaAno, 
					remesaMes.getComuna().getId(), Subtitulo.SUBTITULO24.getId());

			Long acumulado=0l;
			for(DetalleRemesas pagadas : remesasPagadasComuna){
				acumulado += pagadas.getMontoRemesa();
			}
			prog.setTransferenciaAcumulada(acumulado);



			List<ConvenioComunaComponente> conveniosAprobadosComuna = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloComunaEstadoConvenio(
					idProgramaAno, componenteSeleccionado, Subtitulo.SUBTITULO24.getId(), remesaMes.getComuna().getId(), EstadosConvenios.APROBADO.getId());

			List<ConvenioComunaComponente> conveniosPagadosComuna= conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloComunaEstadoConvenio(
					idProgramaAno, componenteSeleccionado, Subtitulo.SUBTITULO24.getId(), remesaMes.getComuna().getId(), EstadosConvenios.PAGADO.getId());

			List<ConvenioComunaComponente> conveniosEnTramiteComuna= conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloComunaEstadoConvenio(
					idProgramaAno, componenteSeleccionado, Subtitulo.SUBTITULO24.getId(), remesaMes.getComuna().getId(), EstadosConvenios.TRAMITE.getId());

			Long acumulador=0l;
			for(ConvenioComunaComponente aprobados : conveniosAprobadosComuna){
				acumulador += aprobados.getMonto();
			}
			for(ConvenioComunaComponente pagados : conveniosPagadosComuna){
				acumulador += pagados.getMonto();
			}
			for(ConvenioComunaComponente tramite : conveniosEnTramiteComuna){
				acumulador += tramite.getMonto();
			}

			idDetalleRemesa.add(remesaMes.getIdDetalleRemesa());
			prog.setIdDetalleRemesa(idDetalleRemesa);

			if(listaOtResumenMunicipalVO.size()>0){
				if(listaOtResumenMunicipalVO.indexOf(prog) == -1){
					prog.setConveniosRecibidos(acumulador);
					listaOtResumenMunicipalVO.add(prog);
				}else{
					List<RemesasProgramaVO> remesasEstab = listaOtResumenMunicipalVO.get(listaOtResumenMunicipalVO.indexOf(prog)).getRemesas();
					for(RemesasProgramaVO remesaEst : remesasEstab){
						for(RemesasProgramaVO remesaProg : prog.getRemesas()){
							for(DiaVO diaEstab : remesaEst.getDias()){
								for(DiaVO diaProg : remesaProg.getDias()){
									if(remesaEst.getIdMes() == remesaProg.getIdMes()){
										if(diaEstab.getDia() == diaProg.getDia()){
											diaEstab.setMonto(diaEstab.getMonto()+diaProg.getMonto());
										}
									}
								}
							}
						}
					}


					prog = listaOtResumenMunicipalVO.get(listaOtResumenMunicipalVO.indexOf(prog));
					prog.setConveniosRecibidos(prog.getConveniosRecibidos()+acumulador);
					prog.setRemesas(remesasEstab);

				}
			}else{
				prog.setConveniosRecibidos(acumulador);
				listaOtResumenMunicipalVO.add(prog);
			}



		}
		return listaOtResumenMunicipalVO;
	}

	public List<OTResumenMunicipalVO> getDetalleOTMunicipalConsolidador(Integer componenteSeleccionado,
			Integer servicioSeleccionado, Integer idProgramaAno) {
		System.out.println("Buscando Detalle de Convenios/Remesas para Subtitulo 24");

		List<OTResumenMunicipalVO> listaOtResumenMunicipalVO = new ArrayList<OTResumenMunicipalVO>();
		List<Cuota> cuotasPrograma = reliquidacionDAO.getCuotasByProgramaAno(idProgramaAno);

		//Preguntamos si existe alguna remesa por pagar para el mes actual y la agregamos al listado
		List<DetalleRemesas> remesasMesActual = remesasDAO.getRemesasMesActualByMesProgramaAnoServicioSubtituloMunicipal(Integer.parseInt(getMesCurso(true)),idProgramaAno,servicioSeleccionado, Subtitulo.SUBTITULO24.getId());
		List<Integer> idDetalleRemesa = new ArrayList<Integer>();

		for(DetalleRemesas remesaMes : remesasMesActual){
			OTResumenMunicipalVO prog = new OTResumenMunicipalVO();

			ComunaVO comuna = new ComunaVO();
			comuna.setIdComuna(remesaMes.getComuna().getId());
			comuna.setNombre(remesaMes.getComuna().getNombre());
			prog.setComuna(comuna);

			List<RemesasProgramaVO> remesas = new ArrayList<RemesasProgramaVO>();
			try {
				remesas = getRemesasPrograma(idProgramaAno, Integer.parseInt(getMesCurso(true)));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}

			prog.setCuota(remesaMes.getCuota());
			Cuota ultimaCuota = cuotasPrograma.get(cuotasPrograma.size()-1);

			// busco la remesa y la aplico
			if(remesaMes.getCuota().getNumeroCuota() == ultimaCuota.getNumeroCuota()){
				System.out.println("BUSCANDO RELIQUIDACION PARA COMUNA"+ comuna.getNombre());
			}

			for(RemesasProgramaVO remesaPrograma : remesas){
				System.out.println(remesaPrograma.getMes());
				for(DiaVO dia : remesaPrograma.getDias()){
					if(dia.getDia() ==remesaMes.getDia().getId() && remesaPrograma.getIdMes() == remesaMes.getMes().getIdMes()){
						dia.setMonto(remesaMes.getMontoRemesa().longValue());
					}else{
						dia.setMonto(0l);
					}
				}
			}
			prog.setRemesas(remesas);

			Long mp = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(remesaMes.getComuna().getId(), idProgramaAno,
					componenteSeleccionado, Subtitulo.SUBTITULO24.getId());
			prog.setMarcoPresupuestario(mp);

			List<DetalleRemesas> remesasPagadasComuna = remesasDAO.getRemesasPagadasEstablecimiento(idProgramaAno, 
					remesaMes.getComuna().getId(), Subtitulo.SUBTITULO24.getId());

			Long acumulado=0l;
			for(DetalleRemesas pagadas : remesasPagadasComuna){
				acumulado += pagadas.getMontoRemesa();
			}
			prog.setTransferenciaAcumulada(acumulado);

			List<ConvenioComunaComponente> conveniosAprobadosComuna = conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloComunaEstadoConvenio(
					idProgramaAno, componenteSeleccionado, Subtitulo.SUBTITULO24.getId(), remesaMes.getComuna().getId(), EstadosConvenios.APROBADO.getId());

			List<ConvenioComunaComponente> conveniosPagadosComuna= conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloComunaEstadoConvenio(
					idProgramaAno, componenteSeleccionado, Subtitulo.SUBTITULO24.getId(), remesaMes.getComuna().getId(), EstadosConvenios.PAGADO.getId());

			List<ConvenioComunaComponente> conveniosEnTramiteComuna= conveniosDAO.getConveniosPagadosByProgramaAnoComponenteSubtituloComunaEstadoConvenio(
					idProgramaAno, componenteSeleccionado, Subtitulo.SUBTITULO24.getId(), remesaMes.getComuna().getId(), EstadosConvenios.TRAMITE.getId());

			Long acumulador=0l;
			for(ConvenioComunaComponente aprobados : conveniosAprobadosComuna){
				acumulador += aprobados.getMonto();
			}
			for(ConvenioComunaComponente pagados : conveniosPagadosComuna){
				acumulador += pagados.getMonto();
			}
			for(ConvenioComunaComponente tramite : conveniosEnTramiteComuna){
				acumulador += tramite.getMonto();
			}

			idDetalleRemesa.add(remesaMes.getIdDetalleRemesa());
			prog.setIdDetalleRemesa(idDetalleRemesa);

			if(listaOtResumenMunicipalVO.size()>0){
				if(listaOtResumenMunicipalVO.indexOf(prog) == -1){
					prog.setConveniosRecibidos(acumulador);
					listaOtResumenMunicipalVO.add(prog);
				}else{
					List<RemesasProgramaVO> remesasEstab = listaOtResumenMunicipalVO.get(listaOtResumenMunicipalVO.indexOf(prog)).getRemesas();
					for(RemesasProgramaVO remesaEst : remesasEstab){
						for(RemesasProgramaVO remesaProg : prog.getRemesas()){
							for(DiaVO diaEstab : remesaEst.getDias()){
								for(DiaVO diaProg : remesaProg.getDias()){
									if(remesaEst.getIdMes() == remesaProg.getIdMes()){
										if(diaEstab.getDia() == diaProg.getDia()){
											diaEstab.setMonto(diaEstab.getMonto()+diaProg.getMonto());
										}
									}
								}
							}
						}
					}

					prog = listaOtResumenMunicipalVO.get(listaOtResumenMunicipalVO.indexOf(prog));
					prog.setConveniosRecibidos(prog.getConveniosRecibidos()+acumulador);
					prog.setRemesas(remesasEstab);

				}
			}else{
				prog.setConveniosRecibidos(acumulador);
				listaOtResumenMunicipalVO.add(prog);
			}
		}
		return listaOtResumenMunicipalVO;
	}

	public List<RemesasProgramaVO> getRemesasPrograma(Integer idProgramaAno,
			int idMes) throws ParseException {
		List<ProgramaFechaRemesa> fechasRemesas = programasDAO.findRemesasByPrograma(idProgramaAno);
		List<RemesasProgramaVO> remesasVO = new ArrayList<RemesasProgramaVO>();

		int meses;
		if(idMes < 12){
			meses=2;
		}else{
			meses=1;
		}
		for (int i = 0; i < meses; i++) {
			List<DiaVO> diasVO = new ArrayList<DiaVO>();
			RemesasProgramaVO remesa = new RemesasProgramaVO();
			Mes mes = mesDAO.getMesPorID(idMes+i);
			for(ProgramaFechaRemesa fechaRemesa : fechasRemesas){
				int day=fechaRemesa.getFechaRemesa().getDia().getDia();
				while(isWeekend(mes.getIdMes(), day, getAnoCurso()) || isFeriado(mes.getIdMes(), day, getAnoCurso())){
					day-=1;
				}
				DiaVO dia = new DiaVO();
				dia.setDia(day);
				dia.setBloqueado(diabloqueado(day,mes.getIdMes(),getAnoCurso()));
				diasVO.add(dia);
			}
			remesa.setIdMes(mes.getIdMes());
			remesa.setMes(mes.getNombre());
			remesa.setDias(diasVO);
			remesa.setCantDias(diasVO.size());
			remesasVO.add(remesa);
		}

		return remesasVO;
	}

	public List<RemesasProgramaVO> getRemesasPerCapita(Integer idProgramaAno,
			int idMes) throws ParseException {
		List<ProgramaFechaRemesa> fechasRemesas = programasDAO.findRemesasByPrograma(idProgramaAno);
		List<RemesasProgramaVO> remesasVO = new ArrayList<RemesasProgramaVO>();

		for (int i = 0; i < 1; i++) {
			List<DiaVO> diasVO = new ArrayList<DiaVO>();
			RemesasProgramaVO remesa = new RemesasProgramaVO();
			Mes mes = mesDAO.getMesPorID(idMes+i);
			for(ProgramaFechaRemesa fechaRemesa : fechasRemesas){
				int day=fechaRemesa.getFechaRemesa().getDia().getDia();
				while(isWeekend(mes.getIdMes(), day, getAnoCurso()) || isFeriado(mes.getIdMes(), day, getAnoCurso())){
					day-=1;
				}
				DiaVO dia = new DiaVO();
				dia.setDia(day);
				dia.setBloqueado(diabloqueado(day,mes.getIdMes(),getAnoCurso()));
				diasVO.add(dia);
			}
			remesa.setIdMes(mes.getIdMes());
			remesa.setMes(mes.getNombre());
			remesa.setDias(diasVO);
			remesa.setCantDias(diasVO.size());
			remesasVO.add(remesa);
		}

		return remesasVO;
	}


	public boolean isWeekend(Integer idMes, Integer dia, Integer anoCurso) {
		try {
			DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date date = formatter.parse(idMes+"/"+dia+"/"+anoCurso);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY  || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
				return true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;

	}

	public boolean isFeriado(Integer idMes, int dia, Integer anoCurso) {
		Festivos festivo = utilitariosDAO.findFestivoByDiaMesAno(idMes,dia,anoCurso);
		if(festivo != null){
			return true;
		}
		return false;
	}

	public boolean diabloqueado(int dia, int mes, Integer ano){
		System.out.println("Evaluando fecha: "+dia+"/"+mes+"/"+ano);

		Calendar calendarHoy = Calendar.getInstance();
		calendarHoy.add(Calendar.MONTH, -9);
		Date hoy = new Date(calendarHoy.getTimeInMillis()); 

		Calendar calendar = new GregorianCalendar(ano, mes-1, dia); 
		Date fechaRemesa = new Date(calendar.getTimeInMillis());

		long diferencia = ( fechaRemesa.getTime() - hoy.getTime()  )/MILLSECS_PER_DAY; 

		if(diferencia > 0){
			if(diferencia < 3){
				System.out.println("FECHA BLOQUEADA");
				return true;
			}else{
				System.out.println("FECHA OK");
				return false;
			}
		}else{
			System.out.println("FECHA BLOQUEADA");
			return true;
		}
	}

	public List<OTPerCapitaVO> getDetallePerCapita(
			Integer servicioSeleccionado, Integer anoCurso, Integer programaAno) {

		List<OTPerCapitaVO> listaReportePercapita = new ArrayList<OTPerCapitaVO>();

		List<DetalleRemesas> remesasMesActual = remesasDAO.getRemesasMesActualByMesProgramaAnoServicioSubtituloMunicipal(Integer.parseInt(getMesCurso(true)),programaAno,servicioSeleccionado, Subtitulo.SUBTITULO24.getId());

		for(DetalleRemesas remesaMes : remesasMesActual){
			OTPerCapitaVO percapitaVO = new OTPerCapitaVO();
			percapitaVO.setIdComuna(remesaMes.getComuna().getId());
			percapitaVO.setComuna(remesaMes.getComuna().getNombre());

			AntecendentesComunaCalculado antecedentesComunaCalculado = antecedentesComunaDAO.findByComunaAno(remesaMes.getComuna().getId(), anoCurso);

			if(antecedentesComunaCalculado!=null){
				percapitaVO.setMarcoPresupuestario(antecedentesComunaCalculado.getPercapitaAno()!=null?antecedentesComunaCalculado.getPercapitaAno():0);	

				List<DetalleRemesas> listaRemesas = remesasDAO.getRemesasPagadasComunaLaFecha(programaAno, remesaMes.getComuna().getId(),
						3, Integer.parseInt(getMesCurso(true)));
				Long remesaAcumulada = 0l;
				for(DetalleRemesas remesaPagada : listaRemesas){
					remesaAcumulada += remesaPagada.getMontoRemesa().longValue();
				}

				percapitaVO.setTransferenciaAcumulada(remesaAcumulada);
				percapitaVO.setTipoComuna(antecedentesComunaCalculado.getAntecedentesComuna().getClasificacion().getDescripcion());
			}else{
				percapitaVO.setMarcoPresupuestario(0l);
				percapitaVO.setTransferenciaAcumulada(0l);
				percapitaVO.setTipoComuna("");
			}

			List<RemesasProgramaVO> remesas = new ArrayList<RemesasProgramaVO>();
			try {
				remesas = getRemesasPerCapita(programaAno, Integer.parseInt(getMesCurso(true)));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}

			for(RemesasProgramaVO remesa : remesas){
				for(DiaVO dia : remesa.getDias()){
					if(dia.getDia()==REMESA_REGULAR){
						dia.setMonto(antecedentesComunaCalculado.getPercapitaMes());
					}else{
						dia.setMonto(0l);
					}
				}
			}
			percapitaVO.setRemesas(remesas);
			listaReportePercapita.add(percapitaVO);
		}
		return listaReportePercapita;
	}

	public OTPerCapitaVO actualizarComunaPerCapita(Integer idComuna, OTPerCapitaVO registroTabla, Integer idProgramaAno) {

		DetalleRemesas detalleRemesas = new DetalleRemesas();
		detalleRemesas.setComuna(comunaDAO.getComunaById(idComuna));
		detalleRemesas.setProgramaAno(programasDAO.getProgramaAnoByID(idProgramaAno));
		detalleRemesas.setRemesaPagada(false);
		detalleRemesas.setSubtitulo(subtituloDAO.getTipoSubtituloById(3));

		detalleRemesas.setMes(utilitariosDAO.findMesById(Integer.parseInt(getMesCurso(true))));

		for(RemesasProgramaVO remesas : registroTabla.getRemesas()){
			for(DiaVO dia : remesas.getDias()){
				if( Integer.parseInt(getMesCurso(true)) == remesas.getIdMes()){
					if(dia.getMonto()>0){
						detalleRemesas.setMontoRemesa(dia.getMonto().intValue());
						detalleRemesas.setDia(utilitariosDAO.findDiaById(dia.getDia()));
					}

				}
			}
		}
		remesasDAO.save(detalleRemesas);

		AntecendentesComunaCalculado antecedentesComunaCalculado = antecedentesComunaDAO.findByComunaAno(idComuna, getAnoCurso());
		antecedentesComunaCalculado.setAprobado(new Boolean(true));

		return registroTabla;
	}

	public String getMesCurso(Boolean numero) {
		/*SimpleDateFormat dateFormat = null;
		String mesCurso = null;
		if(numero){
			dateFormat = new SimpleDateFormat("MM");
			mesCurso = dateFormat.format(new Date());
		}else{
			dateFormat = new SimpleDateFormat("MMMM");
			mesCurso = dateFormat.format(new Date());
		}*/
		String mesCurso;
		if(numero){
			mesCurso="10";
		}else{
			mesCurso="OCTUBRE";
		}
		return mesCurso;
	}

	public OTResumenDependienteServicioVO actualizarServicio(
			OTResumenDependienteServicioVO registroTabla,
			Integer idProgramaAno, Integer idSubtitulo, Integer componenteSeleccionado, List<Integer> idDetalleRemesas) {

		//Buscamos las cuotas del programa y su MP
		List<Cuota> cuotasPrograma = reliquidacionDAO.getCuotasByProgramaAno(idProgramaAno);
		Long marcoPresupuestario = programasDAO.getMPEstablecimientoProgramaAnoComponenteSubtitulo(registroTabla.getEstablecimiento().getId(),
				idProgramaAno,componenteSeleccionado,idSubtitulo);
		List<DetalleRemesas> remesasPagadasEstablecimiento = remesasDAO.getRemesasPagadasEstablecimiento(idProgramaAno, registroTabla.getEstablecimiento().getId(), idSubtitulo);

		Long acumulador =0l;
		for(DetalleRemesas remesaPagada : remesasPagadasEstablecimiento){
			acumulador += remesaPagada.getMontoRemesa();
		}

		List<PagaRemesaVO> pagaRemesa = new ArrayList<PagaRemesaVO>();
		for(RemesasProgramaVO registro : registroTabla.getRemesas()){
			for(DiaVO dia : registro.getDias()){
				if(!dia.isBloqueado() && dia.getMonto()!=null && dia.getMonto()>0){
					acumulador += dia.getMonto();
					PagaRemesaVO paga =  new PagaRemesaVO();
					paga.setDia(dia.getDia());
					paga.setIdMes(registro.getIdMes());
					paga.setMonto(dia.getMonto());
					paga.setCuota(registroTabla.getCuota());
					pagaRemesa.add(paga);
				}
			}
		}

		for(PagaRemesaVO pagando : pagaRemesa){
			DetalleRemesas detalleRemesas = new DetalleRemesas();
			detalleRemesas.setEstablecimiento(establecimientosDAO.getEstablecimientoByCodigo(registroTabla.getEstablecimiento().getCodigoEstablecimiento()));
			detalleRemesas.setProgramaAno(programasDAO.getProgramaAnoByID(idProgramaAno));
			detalleRemesas.setRemesaPagada(false);
			detalleRemesas.setSubtitulo(subtituloDAO.getTipoSubtituloById(idSubtitulo));
			detalleRemesas.setMes(utilitariosDAO.findMesById(pagando.getIdMes()));
			detalleRemesas.setDia(utilitariosDAO.findDiaById(pagando.getDia()));
			detalleRemesas.setMontoRemesa(pagando.getMonto().intValue());
			detalleRemesas.setCuota(pagando.getCuota());
			remesasDAO.save(detalleRemesas);
		}

		Long cuota1= Math.round(marcoPresupuestario*(cuotasPrograma.get(0).getPorcentaje()/100.0));

		//Si ya cubrí el marco presupuestario con la suma de las remesas pagadas + las ingresadas, entonces agrego la segunda cuota
		if(cuota1.equals(acumulador)){
			for(Cuota cuota: cuotasPrograma){
				DetalleRemesas detalleRemesasCuota = new DetalleRemesas();
				detalleRemesasCuota.setEstablecimiento(establecimientosDAO.getEstablecimientoByCodigo(registroTabla.getEstablecimiento().getCodigoEstablecimiento()));
				detalleRemesasCuota.setProgramaAno(programasDAO.getProgramaAnoByID(idProgramaAno));
				detalleRemesasCuota.setRemesaPagada(false);
				detalleRemesasCuota.setSubtitulo(subtituloDAO.getTipoSubtituloById(idSubtitulo));

				if(cuota.getIdMes()!=null || cuota.getNumeroCuota()!=1){
					Long remesa = Math.round(marcoPresupuestario*(cuota.getPorcentaje()/100.0));
					int day=REMESA_REGULAR;
					while(isWeekend(cuota.getIdMes().getIdMes(), day, getAnoCurso()) || isFeriado(cuota.getIdMes().getIdMes(), day, getAnoCurso())){
						day-=1;
					}
					detalleRemesasCuota.setCuota(cuota);
					detalleRemesasCuota.setMes(cuota.getIdMes());
					detalleRemesasCuota.setDia(utilitariosDAO.findDiaById(day));
					detalleRemesasCuota.setMontoRemesa(remesa.intValue());
					remesasDAO.save(detalleRemesasCuota);
				}
			}
		}
		if(idDetalleRemesas == null){
			System.out.println("Cambiando el estado de los convenios servicio");
			for(Integer idConvenio : registroTabla.getIdConvenios()){
				ConvenioServicio convenioServicio = conveniosDAO.findByIdConvenio(idConvenio);
				convenioServicio.setEstadoConvenio(new EstadoConvenio(CONVENIO_EN_TRAMITE));
			}
		}
		return registroTabla;
	}

	public OTResumenMunicipalVO actualizarMunicipal(
			OTResumenMunicipalVO registroTabla,
			Integer idProgramaAno, Integer idSubtitulo, Integer componenteSeleccionado, List<Integer> idDetalleRemesas) {

		//Buscamos las cuotas del programa y su MP
		List<Cuota> cuotasPrograma = reliquidacionDAO.getCuotasByProgramaAno(idProgramaAno);

		Long marcoPresupuestario = programasDAO.getMPComunaProgramaAnoComponenteSubtitulo(registroTabla.getComuna().getIdComuna(), idProgramaAno,
				componenteSeleccionado, idSubtitulo);

		List<DetalleRemesas> remesasPagadasComuna = remesasDAO.getRemesasPagadasComuna(idProgramaAno,registroTabla.getComuna().getIdComuna(), idSubtitulo);

		Long acumulador =0l;
		for(DetalleRemesas remesaPagada : remesasPagadasComuna){
			acumulador += remesaPagada.getMontoRemesa();
		}

		List<PagaRemesaVO> pagaRemesa = new ArrayList<PagaRemesaVO>();
		for(RemesasProgramaVO registro : registroTabla.getRemesas()){
			for(DiaVO dia : registro.getDias()){
				if(!dia.isBloqueado() && dia.getMonto()!=null && dia.getMonto()>0){
					acumulador += dia.getMonto();
					PagaRemesaVO paga =  new PagaRemesaVO();
					paga.setDia(dia.getDia());
					paga.setIdMes(registro.getIdMes());
					paga.setMonto(dia.getMonto());
					paga.setCuota(registroTabla.getCuota());
					pagaRemesa.add(paga);
				}
			}
		}

		for(PagaRemesaVO pagando : pagaRemesa){
			DetalleRemesas detalleRemesas = new DetalleRemesas();
			detalleRemesas.setComuna(comunaDAO.getComunaById(registroTabla.getComuna().getIdComuna()));
			detalleRemesas.setProgramaAno(programasDAO.getProgramaAnoByID(idProgramaAno));
			detalleRemesas.setRemesaPagada(false);
			detalleRemesas.setSubtitulo(subtituloDAO.getTipoSubtituloById(idSubtitulo));
			detalleRemesas.setMes(utilitariosDAO.findMesById(pagando.getIdMes()));
			detalleRemesas.setDia(utilitariosDAO.findDiaById(pagando.getDia()));
			detalleRemesas.setMontoRemesa(pagando.getMonto().intValue());
			detalleRemesas.setCuota(pagando.getCuota());
			remesasDAO.save(detalleRemesas);
		}

		Long cuota1= Math.round(marcoPresupuestario*(cuotasPrograma.get(0).getPorcentaje()/100.0));

		//Si ya cubrí el marco presupuestario con la suma de las remesas pagadas + las ingresadas, entonces agrego la segunda cuota
		if(cuota1.equals(acumulador)){
			for(Cuota cuota: cuotasPrograma){
				DetalleRemesas detalleRemesasCuota = new DetalleRemesas();
				detalleRemesasCuota.setComuna(comunaDAO.getComunaById(registroTabla.getComuna().getIdComuna()));
				detalleRemesasCuota.setProgramaAno(programasDAO.getProgramaAnoByID(idProgramaAno));
				detalleRemesasCuota.setRemesaPagada(false);
				detalleRemesasCuota.setSubtitulo(subtituloDAO.getTipoSubtituloById(idSubtitulo));

				if(cuota.getIdMes()!=null || cuota.getNumeroCuota()!=1){
					Long remesa = Math.round(marcoPresupuestario*(cuota.getPorcentaje()/100.0));
					int day=REMESA_REGULAR;
					while(isWeekend(cuota.getIdMes().getIdMes(), day, getAnoCurso()) || isFeriado(cuota.getIdMes().getIdMes(), day, getAnoCurso())){
						day-=1;
					}
					detalleRemesasCuota.setCuota(cuota);
					detalleRemesasCuota.setMes(cuota.getIdMes());
					detalleRemesasCuota.setDia(utilitariosDAO.findDiaById(day));
					detalleRemesasCuota.setMontoRemesa(remesa.intValue());
					remesasDAO.save(detalleRemesasCuota);
				}
			}
		}
		if(idDetalleRemesas == null){
			System.out.println("Cambiando el estado de los convenios servicio");
			for(Integer idConvenio : registroTabla.getIdConvenios()){
				ConvenioComuna convenioComuna = conveniosDAO.getConvenioComunaByIdConvenio(idConvenio);
				convenioComuna.setEstadoConvenio(new EstadoConvenio(CONVENIO_EN_TRAMITE));
			}
		}
		return registroTabla;
	}

	public void eliminarDetalleRemesa(List<Integer> idDetalleRemesas) {
		for(Integer idDetalle : idDetalleRemesas){
			DetalleRemesas remesa = remesasDAO.findDetalleRemesaById(idDetalle);
			remesasDAO.remove(remesa);
		}
	}

	public void cambiarEstadoPrograma(Integer idPrograma,
			EstadosProgramas estadoPrograma) {
		System.out.println("idPrograma-->" + idPrograma + " estadoPrograma.getId()-->"+estadoPrograma.getId());
		ProgramaAno programaAno = recursosFinancierosProgramasReforzamientoDAO.findById(idPrograma);

		programaAno.setEstadoOT(new EstadoPrograma(estadoPrograma.getId()));
		System.out.println("Cambia estado ok");

	}

	public List<ResumenProgramaMixtoVO> getResumenPrograma(
			ProgramaVO programaResumen) {

		List<ResumenProgramaMixtoVO>  resumenPrograma = new ArrayList<ResumenProgramaMixtoVO>();
		List<ServicioSalud> listaServicios = utilitariosDAO.getServicios();

		for(ServicioSalud servicio : listaServicios){
			ResumenProgramaMixtoVO resumen = new ResumenProgramaMixtoVO();
			resumen.setIdServicio(servicio.getId());
			resumen.setNombreServicio(servicio.getNombre());

			List<DetalleRemesas> remesasServicio1 = remesasDAO.getRemesasMesActualByMesProgramaAnoServicio1(
					Integer.parseInt(getMesCurso(true)), programaResumen.getIdProgramaAno(), servicio.getId());

			List<DetalleRemesas> remesasServicio2 = remesasDAO.getRemesasMesActualByMesProgramaAnoServicio2(
					Integer.parseInt(getMesCurso(true)), programaResumen.getIdProgramaAno(), servicio.getId());

			Long acumuladorS21 = 0l;
			Long acumuladorS22 = 0l;
			Long acumuladorS29 = 0l;
			Long acumuladorS24 = 0l;
			for(DetalleRemesas remesa : remesasServicio1){
				if(remesa.getSubtitulo().getIdTipoSubtitulo() == Subtitulo.SUBTITULO21.getId()){
					acumuladorS21 += remesa.getMontoRemesa();
				}
				if(remesa.getSubtitulo().getIdTipoSubtitulo() == Subtitulo.SUBTITULO22.getId()){
					acumuladorS22 += remesa.getMontoRemesa();
				}
				if(remesa.getSubtitulo().getIdTipoSubtitulo() == Subtitulo.SUBTITULO29.getId()){
					acumuladorS29 += remesa.getMontoRemesa();
				}
				if(remesa.getSubtitulo().getIdTipoSubtitulo() == Subtitulo.SUBTITULO24.getId()){
					acumuladorS24 += remesa.getMontoRemesa();
				}
			}
			for(DetalleRemesas remesa : remesasServicio2){
				if(remesa.getSubtitulo().getIdTipoSubtitulo() == Subtitulo.SUBTITULO21.getId()){
					acumuladorS21 += remesa.getMontoRemesa();
				}
				if(remesa.getSubtitulo().getIdTipoSubtitulo() == Subtitulo.SUBTITULO22.getId()){
					acumuladorS22 += remesa.getMontoRemesa();
				}
				if(remesa.getSubtitulo().getIdTipoSubtitulo() == Subtitulo.SUBTITULO29.getId()){
					acumuladorS29 += remesa.getMontoRemesa();
				}
				if(remesa.getSubtitulo().getIdTipoSubtitulo() == Subtitulo.SUBTITULO24.getId()){
					acumuladorS24 += remesa.getMontoRemesa();
				}
			}


			resumen.setTotalS21(acumuladorS21);
			resumen.setTotalS22(acumuladorS22);
			resumen.setTotalS29(acumuladorS29);
			resumen.setTotalS24(acumuladorS24);
			resumenPrograma.add(resumen);
		}

		return resumenPrograma;
	}

	public List<ResumenFONASAServicioVO> cargarFonasaServicio(Integer idSubtitulo) {
		List<ResumenFONASAServicioVO> resultado =  new ArrayList<ResumenFONASAServicioVO>();
		List<ServicioSalud> listaServicios = utilitariosDAO.getServicios();



		for(ServicioSalud servicio : listaServicios){
			ResumenFONASAServicioVO resumen = new ResumenFONASAServicioVO();
			resumen.setIdServicio(servicio.getId());
			resumen.setNombreServicio(servicio.getNombre());
			Long totalServicio=0l;
			List<ProgramaFonasaVO> programasFonasa = programasService.getProgramasFonasa(true);
			List<ProgramaFonasaVO> otrosProgramas = programasService.getProgramasFonasa(false);
			for(ProgramaFonasaVO fonasa: programasFonasa){

				Integer progAno = programasDAO.getIdProgramaAnoAnterior(fonasa.getIdPrograma(),getAnoCurso());
				List<DetalleRemesas> remesas = remesasDAO.getRemesasMesActualByMesProgramaAnoServicioSubtitulo1(Integer.parseInt(getMesCurso(true)),
						progAno, servicio.getId(), idSubtitulo);
				Long acumulador=0l;
				for(DetalleRemesas detalle : remesas){
					acumulador += detalle.getMontoRemesa();
				}
				fonasa.setMonto(fonasa.getMonto()+acumulador);
				totalServicio += acumulador;
			}

			Long totalOtros =0l;
			for(ProgramaFonasaVO otros: otrosProgramas){
				Integer progAno = programasDAO.getIdProgramaAnoAnterior(otros.getIdPrograma(),getAnoCurso());
				List<DetalleRemesas> remesas = remesasDAO.getRemesasMesActualByMesProgramaAnoServicioSubtitulo1(Integer.parseInt(getMesCurso(true)),
						progAno, servicio.getId(), idSubtitulo);

				for(DetalleRemesas detalle : remesas){
					totalOtros += detalle.getMontoRemesa();
					totalServicio += detalle.getMontoRemesa();
				}

			}

			resumen.setProgramasFonasa(programasFonasa);
			resumen.setTotalOtrosProgramas(totalOtros);
			resumen.setTotal(totalServicio);


			resultado.add(resumen);
		}
		return resultado;
	}

	public List<ResumenFONASAMunicipalVO> cargarFonasaMunicipal() {
		List<ResumenFONASAMunicipalVO> resultado =  new ArrayList<ResumenFONASAMunicipalVO>();
		List<ServicioSalud> listaServicios = utilitariosDAO.getServicios();



		for(ServicioSalud servicio : listaServicios){
			ResumenFONASAMunicipalVO resumen = new ResumenFONASAMunicipalVO();
			resumen.setIdServicio(servicio.getId());
			resumen.setNombreServicio(servicio.getNombre());
			Long totalServicio=0l;
			List<ProgramaFonasaVO> programasFonasa = programasService.getProgramasFonasa(true);
			List<ProgramaFonasaVO> otrosProgramas = programasService.getProgramasFonasa(false);

			for(ProgramaFonasaVO fonasa: programasFonasa){

				Integer progAno = programasDAO.getIdProgramaAnoAnterior(fonasa.getIdPrograma(),getAnoCurso());
				List<DetalleRemesas> remesas = remesasDAO.getRemesasMesActualByMesProgramaAnoServicioSubtitulo2(Integer.parseInt(getMesCurso(true)),
						progAno, servicio.getId(), Subtitulo.SUBTITULO24.getId());
				Long acumulador=0l;
				for(DetalleRemesas detalle : remesas){
					acumulador += detalle.getMontoRemesa();
				}
				fonasa.setMonto(fonasa.getMonto()+acumulador);
				totalServicio += acumulador;
			}

			Long totalOtros =0l;
			for(ProgramaFonasaVO otros: otrosProgramas){
				//Descartamos percapita id = -1
						if(otros.getIdPrograma()>0){
							Integer progAno = programasDAO.getIdProgramaAnoAnterior(otros.getIdPrograma(),getAnoCurso());
							List<DetalleRemesas> remesas = remesasDAO.getRemesasMesActualByMesProgramaAnoServicioSubtitulo2(Integer.parseInt(getMesCurso(true)),
									progAno, servicio.getId(), Subtitulo.SUBTITULO24.getId());

							for(DetalleRemesas detalle : remesas){
								totalOtros += detalle.getMontoRemesa();
								totalServicio += detalle.getMontoRemesa();
							}
						}


			}

			resumen.setProgramasFonasa(programasFonasa);
			resumen.setTotalOtrosProgramas(totalOtros);


			Object resultPerCapitaBasal = antecedentesComunaDAO.getPerCapitaBasalByIdServicio(servicio.getId());
			Long perCapitaBasal =0l;
			if(resultPerCapitaBasal != null){
				perCapitaBasal = ((Number)(resultPerCapitaBasal)).longValue();
			}
			resumen.setPerCapitaBasal(perCapitaBasal);

			Object resultDesempenoDificil = antecedentesComunaDAO.getDesempenoDificilByIdServicio(servicio.getId());
			Long desempenoDificil =0l;
			if(resultDesempenoDificil != null){
				desempenoDificil = ((Number)(resultDesempenoDificil)).longValue();
			}
			resumen.setDesempenoDificil(desempenoDificil);

			/*Object resultRebaja = antecedentesComunaDAO.getRebajaByIdServicioMesActual();
			Long rebaja =0l;
			if(resultRebaja != null){
				rebaja = ((Number)(resultRebaja)).longValue();
			}*/
			resumen.setRebaja(0l);
			resumen.setDescuentoRetiro(0l);

			Long totalPercapita = resumen.getPerCapitaBasal()+resumen.getDesempenoDificil()-resumen.getRebaja()-resumen.getDescuentoRetiro();
			resumen.setTotalPercapita(totalPercapita);
			resumen.setTotal(totalServicio+totalPercapita);


			resultado.add(resumen);
		}
		return resultado;
	}


	public void administrarVersionesAlfresco() {
		System.out.println("administrar Versiones Alfresco");		
	}

	public Integer createSeguimientoOT(Integer idProcesoOT, TareasSeguimiento tareaSeguimiento, String subject, String body, String username, List<String> para, List<String> conCopia, List<String> conCopiaOculta, List<Integer> documentos) {
		System.out.println("CREAR SEGUIMIENTO OT");
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
				int ano = getAnoCurso()+1;
				BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummaryVO.getPath()), contenType, folderOrdenesTransferencia.replace("{ANO}", ano+""));
				System.out.println("response upload template --->"+response);
				documentService.updateDocumentTemplate(referenciaDocumentoId, response.getNodeRef(), response.getFileName(), contenType);
			}
		}
		Integer idSeguimiento = seguimientoService.createSeguimiento(tareaSeguimiento, subject, body, from, para, conCopia, conCopiaOculta, documentosTmp);
		Seguimiento seguimiento = seguimientoDAO.getSeguimientoById(idSeguimiento);
		return remesasDAO.createSeguimiento(idProcesoOT, seguimiento);
	}

	public Integer crearInstanciaOT(String username) {
		System.out.println("username--> " + username);
		Usuario usuario = this.usuarioDAO.getUserByUsername(username);
		String mesCurso = getMesCurso(true);
		Mes mes = mesDAO.getMesPorID(Integer.parseInt(mesCurso));

		return remesasDAO.crearInstanciaOT(usuario,mes,new Date());

	}

	public List<SeguimientoVO> getBitacora(Integer idProcesoOT,
			TareasSeguimiento tareaSeguimiento) {
		List<SeguimientoVO> bitacora = new ArrayList<SeguimientoVO>();
		List<Seguimiento> bitacoraSeguimiento = seguimientoDAO.getBitacoraOT(idProcesoOT, tareaSeguimiento);
		if(bitacoraSeguimiento != null && bitacoraSeguimiento.size() > 0){
			for(Seguimiento seguimiento : bitacoraSeguimiento){
				bitacora.add(new SeguimientoMapper().getBasic(seguimiento));
			}
		}
		return bitacora;
	}

	public Integer generarOficiosTransferencia(TipoDocumentosProcesos plantillaordinariooredentransferencia, String idProcesoOT, Long totalFinal) {
		System.out.println("GENERAR OFICIOS DE OT");
		Integer plantillaOrdinarioOT = documentService.getPlantillaByType(plantillaordinariooredentransferencia);

		if (plantillaOrdinarioOT == null) {
			throw new RuntimeException(
					"No se puede crear Borrador Decreto Aporte Estatal, la plantilla no esta cargada");
		}

		try {

			ReferenciaDocumentoSummaryVO referenciaDocOT = documentService.getDocumentByPlantillaId(plantillaOrdinarioOT);
			DocumentoVO documentoOT = documentService.getDocument(referenciaDocOT.getId());
			String templateOrdinarioOT = tmpDirDoc+ File.separator+ documentoOT.getName();
			templateOrdinarioOT = templateOrdinarioOT.replace(" ", "");

			String filenameBorradorOrdinarioOT = tmpDirDoc+ File.separator + new Date().getTime() + "_"
					+ "Resolucion_Ordenes_de_Transferencia.docx";
			System.out.println("filenameBorradorOrdinarioOT filename-->"
					+ filenameBorradorOrdinarioOT);
			System.out.println("templateOrdinarioProgramasAPS template-->"
					+ templateOrdinarioOT);
			GeneradorWord generadorResolucionOT = new GeneradorWord(
					templateOrdinarioOT);
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contentType = mimemap
					.getContentType(templateOrdinarioOT
							.toLowerCase());
			System.out.println("contenTypeBorradorAporteEstatal->"
					+ contentType);

			generadorResolucionOT.saveContent(documentoOT
					.getContent(), XWPFDocument.class);

			Map<String, Object> parametersBorradorResolucionOT = new HashMap<String, Object>();
			parametersBorradorResolucionOT.put("{ano}",getAnoCurso());
			parametersBorradorResolucionOT.put("{mes}",getMesCurso(false));
			parametersBorradorResolucionOT.put("{total}",String.format("%, d",totalFinal));

			GeneradorResolucionAporteEstatal generadorWordDecretoBorradorAporteEstatal = new GeneradorResolucionAporteEstatal(
					filenameBorradorOrdinarioOT,
					templateOrdinarioOT);
			generadorWordDecretoBorradorAporteEstatal.replaceValues(parametersBorradorResolucionOT, XWPFDocument.class);

			int ano = getAnoCurso();
			BodyVO response = alfrescoService.uploadDocument(new File(
					filenameBorradorOrdinarioOT), contentType,
					folderOrdenesTransferencia.replace("{ANO}", ano+""));
			System.out.println("response responseBorradorAporteEstatal --->"
					+ response);


			plantillaOrdinarioOT = documentService
					.createDocumentRemesas(plantillaordinariooredentransferencia, response.getNodeRef(),
							response.getFileName(), contentType, idProcesoOT);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException ex){
			ex.printStackTrace();
		} catch (Docx4JException de){
			de.printStackTrace();
		}

		return plantillaOrdinarioOT;


	}

	public Long generarExcelFonasaOT(
			TipoDocumentosProcesos tipoDocumentoProceso, String idProcesoOT) {

		Long totalFinal=0l;
		Integer plantillaId = 0;
		List<ProgramaFonasaVO> encabezadoFonasa = programasService.getProgramasFonasa(true);
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();

			List<CellExcelVO> header = new ArrayList<CellExcelVO>();
			header.add(new CellExcelVO("COD.",1,2));	
			header.add(new CellExcelVO("SERVICIO DE SALUD",1,2));
			header.add(new CellExcelVO("SUBTITULO 24",(encabezadoFonasa.size()+7),1));
			header.add(new CellExcelVO("SUBTITULO 21",(encabezadoFonasa.size()+2),1));
			header.add(new CellExcelVO("SUBTITULO 22",(encabezadoFonasa.size()+2),1));
			header.add(new CellExcelVO("SUBTITULO 29",(encabezadoFonasa.size()+2),1));
			header.add(new CellExcelVO("TOTAL ($)",1,2));

			List<CellExcelVO> subHeader = new ArrayList<CellExcelVO>();
			subHeader.add(new CellExcelVO("Per Capita Basal",1,1));
			subHeader.add(new CellExcelVO("ADDF",1,1));
			subHeader.add(new CellExcelVO("Descto Retiro Leyes 20.157 y 20.589",1,1));
			subHeader.add(new CellExcelVO("Rebaja Incumpl.IAAPS",1,1));
			subHeader.add(new CellExcelVO("Total Per Capita ($)",1,1));
			for(ProgramaFonasaVO encabezado: encabezadoFonasa){
				subHeader.add(new CellExcelVO(encabezado.getNombrePrograma(),1,1));
			}
			subHeader.add(new CellExcelVO("Otros Ref. Municipal ($)",1,1));
			subHeader.add(new CellExcelVO("Total Ref. Municipal ($)",1,1));
			for(ProgramaFonasaVO encabezado: encabezadoFonasa){
				subHeader.add(new CellExcelVO(encabezado.getNombrePrograma(),1,1));
			}
			subHeader.add(new CellExcelVO("Otros Ref. SS 21 ($)",1,1));
			subHeader.add(new CellExcelVO("Total Ref. Servicios Subt. 21 ($)",1,1));
			for(ProgramaFonasaVO encabezado: encabezadoFonasa){
				subHeader.add(new CellExcelVO(encabezado.getNombrePrograma(),1,1));
			}
			subHeader.add(new CellExcelVO("Otros Ref. SS 22 ($)",1,1));
			subHeader.add(new CellExcelVO("Total Ref. Servicios Subt. 22 ($)",1,1));
			for(ProgramaFonasaVO encabezado: encabezadoFonasa){
				subHeader.add(new CellExcelVO(encabezado.getNombrePrograma(),1,1));
			}
			subHeader.add(new CellExcelVO("Otros Ref. SS 29 ($)",1,1));
			subHeader.add(new CellExcelVO("Total Ref. Servicios Subt. 29 ($)",1,1));




			String filename = tmpDir + File.separator;
			filename += "Plantilla Resumen Ordenes de Transferencia formato FONASA .xlsx";
			GeneradorExcel generadorExcel = new GeneradorExcel(filename);
			String contenType = mimemap.getContentType(filename.toLowerCase());

			List<String> cabezas = new ArrayList<String>();
			for(CellExcelVO head : header){
				String h = head.getName();
				cabezas.add(h);
			}

			List<PlanillaResumenFonasaVO> resumenFonasa = new ArrayList<PlanillaResumenFonasaVO>();
			List<ServicioSalud> listaServicios = utilitariosDAO.getServicios();

			for(ServicioSalud servicio : listaServicios){
				PlanillaResumenFonasaVO resumen = new PlanillaResumenFonasaVO();
				resumen.setIdServicio(servicio.getId());
				resumen.setNombreServicio(servicio.getNombre());

				Object resultPerCapitaBasal = antecedentesComunaDAO.getPerCapitaBasalByIdServicio(servicio.getId());
				Long perCapitaBasal =0l;
				if(resultPerCapitaBasal != null){
					perCapitaBasal = ((Number)(resultPerCapitaBasal)).longValue();
				}
				resumen.setPerCapitaBasal(perCapitaBasal);

				Object resultDesempenoDificil = antecedentesComunaDAO.getDesempenoDificilByIdServicio(servicio.getId());
				Long desempenoDificil =0l;
				if(resultDesempenoDificil != null){
					desempenoDificil = ((Number)(resultDesempenoDificil)).longValue();
				}
				resumen.setAddf(desempenoDificil);

				resumen.setRebajaIaaps(0l);
				resumen.setDesctoLeyes(0l);

				Long totalPerCapita = resumen.getPerCapitaBasal()+resumen.getAddf()-resumen.getRebajaIaaps()-resumen.getDesctoLeyes();
				resumen.setTotalPerCapita(totalPerCapita);

				resumen.setFonasaS24(cargarFonasa(servicio.getId(),Subtitulo.SUBTITULO24.getId()));
				resumen.setOtrosS24(cargarOtrosProgramas(servicio.getId(),Subtitulo.SUBTITULO24.getId()));
				resumen.setTotalS24(calculaTotal(totalPerCapita,resumen.getFonasaS24(),resumen.getOtrosS24()));


				resumen.setFonasaS21(cargarFonasa(servicio.getId(),Subtitulo.SUBTITULO21.getId()));
				resumen.setOtrosS21(cargarOtrosProgramas(servicio.getId(),Subtitulo.SUBTITULO21.getId()));
				resumen.setTotalS21(calculaTotal(0l,resumen.getFonasaS21(),resumen.getOtrosS21()));

				resumen.setFonasaS22(cargarFonasa(servicio.getId(),Subtitulo.SUBTITULO22.getId()));
				resumen.setOtrosS22(cargarOtrosProgramas(servicio.getId(),Subtitulo.SUBTITULO22.getId()));
				resumen.setTotalS22(calculaTotal(0l,resumen.getFonasaS22(),resumen.getOtrosS22()));

				resumen.setFonasaS29(cargarFonasa(servicio.getId(),Subtitulo.SUBTITULO29.getId()));
				resumen.setOtrosS29(cargarOtrosProgramas(servicio.getId(),Subtitulo.SUBTITULO29.getId()));
				resumen.setTotalS29(calculaTotal(0l,resumen.getFonasaS29(),resumen.getOtrosS29()));

				
				totalFinal += resumen.getTotalServicio();
				resumenFonasa.add(resumen);

			}

			OrdenesTransferenciaSheetExcel ordenesTransferenciaSheetExcel = new OrdenesTransferenciaSheetExcel(header, subHeader, resumenFonasa);
			generadorExcel.addSheet(ordenesTransferenciaSheetExcel, "Hoja 1");


			try {
				int ano = getAnoCurso();
				BodyVO response = alfrescoService.uploadDocument(generadorExcel.saveExcel(), contenType, folderOrdenesTransferencia.replace("{ANO}", ano+""));
				plantillaId = documentService.createDocumentRemesas(tipoDocumentoProceso, response.getNodeRef(), response.getFileName(), contenType, idProcesoOT);

			} catch (Exception e) {
				e.printStackTrace();
			}
		
		return totalFinal;

	}

	private Long calculaTotal(Long totalPerCapita, List<ProgramaFonasaVO> programasFonasa, Long otrosProgramas) {
		Long acumulador=0l;
		for(ProgramaFonasaVO prog : programasFonasa){
			acumulador += prog.getMonto();
		}
		acumulador += totalPerCapita;
		acumulador += otrosProgramas;

		return acumulador;

	}

	private Long cargarOtrosProgramas(Integer idServicio, Integer idSubtitulo) {
		List<ProgramaFonasaVO> otrosProgramas = programasService.getProgramasFonasa(false);
		Long totalOtros =0l;
		for(ProgramaFonasaVO otros: otrosProgramas){
			if(otros.getIdPrograma()>0){
				Integer progAno = programasDAO.getIdProgramaAnoAnterior(otros.getIdPrograma(),getAnoCurso());
				List<DetalleRemesas> remesas = remesasDAO.getRemesasMesActualByMesProgramaAnoServicioSubtitulo2(Integer.parseInt(getMesCurso(true)),
						progAno, idServicio, idSubtitulo);

				for(DetalleRemesas detalle : remesas){
					totalOtros += detalle.getMontoRemesa();
				}
			}
		}
		return totalOtros;
	}

	private List<ProgramaFonasaVO> cargarFonasa(Integer idServicio, Integer idSubtitulo) {

		List<ProgramaFonasaVO> programasFonasa = programasService.getProgramasFonasa(true);
		for(ProgramaFonasaVO fonasa: programasFonasa){

			Integer progAno = programasDAO.getIdProgramaAnoAnterior(fonasa.getIdPrograma(),getAnoCurso());
			List<DetalleRemesas> remesas = remesasDAO.getRemesasMesActualByMesProgramaAnoServicioSubtitulo1(Integer.parseInt(getMesCurso(true)),
					progAno, idServicio, idSubtitulo);
			Long acumulador=0l;
			for(DetalleRemesas detalle : remesas){
				acumulador += detalle.getMontoRemesa();
			}
			fonasa.setMonto(fonasa.getMonto()+acumulador);
		}
		return programasFonasa;
	}

	public Integer getIdDocumentoRemesa(Integer idProcesoOT,
			TipoDocumentosProcesos plantillaordinariooredentransferencia) {
		return remesasDAO.getIdDocumentoRemesa(idProcesoOT,plantillaordinariooredentransferencia);
	}

	public void moveToAlfresco(Integer idProcesoOT, Integer docNewVersion,
			TipoDocumentosProcesos tipoDocumento,
			boolean versionFinal, boolean versionFinal2) {
		System.out.println("Buscando referenciaDocumentoId="+docNewVersion);
		ReferenciaDocumentoSummaryVO referenciaDocumentoSummary = documentService.getDocumentSummary(docNewVersion);
		System.out.println("Buscando referenciaDocumentoSummary="+referenciaDocumentoSummary);
		if(referenciaDocumentoSummary != null){
			MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap();
			String contenType= mimemap.getContentType(referenciaDocumentoSummary.getPath().toLowerCase());
			int ano = getAnoCurso();
			BodyVO response = alfrescoService.uploadDocument(new File(referenciaDocumentoSummary.getPath()), contenType, folderOrdenesTransferencia.replace("{ANO}", ano+""));
			System.out.println("response upload template --->"+response);
			documentService.updateDocumentTemplate(referenciaDocumentoSummary.getId(), response.getNodeRef(), response.getFileName(), contenType);
			documentService.createDocumentRemesas(tipoDocumento, response.getNodeRef(), response.getFileName(), contenType, idProcesoOT+"");
		}

	}

	public void enviarDocumentosFonasa(String idProcesoOT) {
		try{
			Integer idPlanillaFonasa= getIdDocumentoRemesa(Integer.parseInt(idProcesoOT),TipoDocumentosProcesos.RESUMENCONSOLIDADOFONASA);
			Integer idOrdinarioOT= getIdDocumentoRemesa(Integer.parseInt(idProcesoOT),TipoDocumentosProcesos.PLANTILLAORDINARIOOREDENTRANSFERENCIA);
			Integer idPlantillaCorreo = documentService.getPlantillaByType(TipoDocumentosProcesos.PLANTILLAOTCORREO);
			
			
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
			
			
			ReferenciaDocumento resolucion = documentoDAO.findById(idPlanillaFonasa);
			ReferenciaDocumento planilla =  documentoDAO.findById(idOrdinarioOT);

			List<EmailService.Adjunto> adjuntos = new ArrayList<EmailService.Adjunto>();


			DocumentoVO documentoResolucion = documentService.getDocument(resolucion.getId());
			String fileNameResolucion = tmpDirDoc + File.separator + documentoResolucion.getName();
			
			DocumentoVO documentoPlanilla = documentService.getDocument(planilla.getId());
			String fileNamePlanilla = tmpDirDoc + File.separator + documentoPlanilla.getName();
			
			

			FileOutputStream fos = new FileOutputStream(fileNameResolucion);
			fos.write(documentoResolucion.getContent());
			fos.close();
			
			FileOutputStream fosPlanilla = new FileOutputStream(fileNamePlanilla);
			fosPlanilla.write(documentoPlanilla.getContent());
			fosPlanilla.close();


			EmailService.Adjunto adjunto = new EmailService.Adjunto();
			adjunto.setDescripcion("Resolucion Orden de Transferencia");
			adjunto.setName(documentoResolucion.getName());
			adjunto.setUrl((new File(fileNameResolucion)).toURI().toURL());

			
			adjuntos.add(adjunto);
			
			
			EmailService.Adjunto adjuntoPlanilla = new EmailService.Adjunto();
			adjuntoPlanilla.setDescripcion("Documentos Adjuntos");
			adjuntoPlanilla.setName(documentoPlanilla.getName());
			adjuntoPlanilla.setUrl((new File(fileNamePlanilla)).toURI().toURL());

			adjuntos.add(adjuntoPlanilla);
			
			Institucion institucion = institucionDAO.findById(Instituciones.FONASA.getId());
			List<String> to = new ArrayList<String>();
			to.add(institucion.getDirector().getEmail().getValor());
			
		
			
			
			if(emailPLantilla != null && emailPLantilla.getAsunto() != null && emailPLantilla.getCuerpo() != null){
				emailService.sendMail(to,null,null,emailPLantilla.getAsunto(),emailPLantilla.getCuerpo().replaceAll("(\r\n|\n)", "<br />"), adjuntos);
			}else{
				emailService.sendMail(to,null,null,"Resolución Orden de Transferencia","Se adjunta Resolución de Orden de Transferencia", adjuntos);
			}
			
			ReporteEmailsEnviados reporteEmailsEnviados = new ReporteEmailsEnviados();
			
			ReporteEmailsDestinatarios destinatarioPara = new ReporteEmailsDestinatarios();
			destinatarioPara.setDestinatario(institucion.getDirector());
			reporteEmailsEnviados.setFecha(new Date());
			remesasDAO.save(reporteEmailsEnviados);
			
			destinatarioPara.setReporteEmailsEnviado(reporteEmailsEnviados);
			destinatarioPara.setTipoDestinatario(new TipoDestinatario(TiposDestinatarios.PARA.getId()));
			remesasDAO.save(destinatarioPara);
			
		
			ReporteEmailsAdjuntos reporteEmailsAdjuntos = new ReporteEmailsAdjuntos();
			reporteEmailsAdjuntos.setDocumento(resolucion);
			reporteEmailsAdjuntos.setReporteEmailsEnviado(reporteEmailsEnviados);
			remesasDAO.save(reporteEmailsAdjuntos);
			
			ReporteEmailsAdjuntos reporteEmailsAdjuntos2 = new ReporteEmailsAdjuntos();
			reporteEmailsAdjuntos2.setDocumento(planilla);
			reporteEmailsAdjuntos2.setReporteEmailsEnviado(reporteEmailsEnviados);
			remesasDAO.save(reporteEmailsAdjuntos2);
			
			ReporteEmailsRemesas reporteEmailsRemesas = new ReporteEmailsRemesas();
			reporteEmailsRemesas.setRemesa(remesasDAO.findById(Integer.parseInt(idProcesoOT)));
			reporteEmailsRemesas.setReporteEmailsEnviados(reporteEmailsEnviados);
			remesasDAO.save(reporteEmailsRemesas);
			
			
			//TODO guardar reportes
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	public List<ReporteEmailsEnviadosVO> getReporteCorreosByIdRemesa(
			Integer idProcesoOT) {
		List<ReporteEmailsRemesas> emailsRemesas = remesasDAO.getReporteCorreosByidRemesa(idProcesoOT);
		List<ReporteEmailsEnviadosVO> emailsEnviadosVO = new ArrayList<ReporteEmailsEnviadosVO>();
		
		if(emailsRemesas != null && emailsRemesas.size() > 0){
			for(ReporteEmailsRemesas reporte : emailsRemesas){
				ReporteEmailsEnviadosVO correo = new ReporteEmailsEnviadosVO();
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
				emailsEnviadosVO.add(correo);
			}
		}
		return emailsEnviadosVO;
	}

	public void reestablecerProgramas(int estadoOT) {
		List<ProgramaAno> progAno = programasDAO.findByAno(getAnoCurso());
		for(ProgramaAno prog : progAno){
			prog.setEstadoOT(new EstadoPrograma(estadoOT));
			
		}
		
	}



}