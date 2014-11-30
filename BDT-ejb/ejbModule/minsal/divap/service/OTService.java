package minsal.divap.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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
import minsal.divap.dao.RecursosFinancierosProgramasReforzamientoDAO;
import minsal.divap.dao.ReliquidacionDAO;
import minsal.divap.dao.RemesasDAO;
import minsal.divap.dao.ServicioSaludDAO;
import minsal.divap.dao.TipoSubtituloDAO;
import minsal.divap.dao.UtilitariosDAO;
import minsal.divap.enums.EstadosConvenios;
import minsal.divap.enums.EstadosProgramas;
import minsal.divap.enums.Subtitulo;
import minsal.divap.model.mappers.ProgramaMapper;
import minsal.divap.vo.ComunaVO;
import minsal.divap.vo.DiaVO;
import minsal.divap.vo.EstablecimientoVO;
import minsal.divap.vo.OTPerCapitaVO;
import minsal.divap.vo.OTResumenDependienteServicioVO;
import minsal.divap.vo.OTResumenMunicipalVO;
import minsal.divap.vo.PagaRemesaVO;
import minsal.divap.vo.ProgramaFonasaVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.RemesasProgramaVO;
import minsal.divap.vo.ResumenFONASAMunicipalVO;
import minsal.divap.vo.ResumenFONASAServicioVO;
import minsal.divap.vo.ResumenProgramaMixtoVO;
import cl.minsal.divap.model.AntecendentesComunaCalculado;
import cl.minsal.divap.model.ConvenioComuna;
import cl.minsal.divap.model.ConvenioComunaComponente;
import cl.minsal.divap.model.ConvenioServicio;
import cl.minsal.divap.model.ConvenioServicioComponente;
import cl.minsal.divap.model.Cuota;
import cl.minsal.divap.model.DetalleRemesas;
import cl.minsal.divap.model.EstadoConvenio;
import cl.minsal.divap.model.EstadoPrograma;
import cl.minsal.divap.model.Festivos;
import cl.minsal.divap.model.Mes;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.ProgramaFechaRemesa;
import cl.minsal.divap.model.ServicioSalud;

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
	private ReportesServices reporteService;
	@EJB
	private ProgramasService programasService;
	
	static long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000;
	static Integer REMESA_REGULAR=24;
	static Integer CONVENIO_EN_TRAMITE=5;
	 
	@Resource(name="tmpDir")
	private String tmpDir;
	@Resource(name="tmpDirDoc")
	private String tmpDirDoc;
	@Resource(name="folderTemplateOT")
	private String folderTemplateTo;
	@Resource(name="folderOT")
	private String folderOT;

	
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
		String mesCurso="10";
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
	
	
	
}