package minsal.divap.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import minsal.divap.dao.AntecedentesComunaDAO;
import minsal.divap.dao.EstablecimientosDAO;
import minsal.divap.dao.MesDAO;
import minsal.divap.dao.ProgramasDAO;
import minsal.divap.dao.RecursosFinancierosProgramasReforzamientoDAO;
import minsal.divap.dao.RemesasDAO;
import minsal.divap.dao.ServicioSaludDAO;
import minsal.divap.dao.UtilitariosDAO;
import minsal.divap.model.mappers.ProgramaMapper;
import minsal.divap.vo.DiaVO;
import minsal.divap.vo.EstablecimientoVO;
import minsal.divap.vo.OTPerCapitaVO;
import minsal.divap.vo.OTResumenDependienteServicioVO;
import minsal.divap.vo.OTResumenMunicipalVO;
import minsal.divap.vo.ProgramaServicioVO;
import minsal.divap.vo.ProgramaVO;
import minsal.divap.vo.RemesasProgramaVO;
import minsal.divap.vo.ReportePerCapitaVO;
import minsal.divap.vo.SubtituloProgramasVO;
import cl.minsal.divap.model.AntecendentesComuna;
import cl.minsal.divap.model.AntecendentesComunaCalculado;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.DetalleRemesas;
import cl.minsal.divap.model.Establecimiento;
import cl.minsal.divap.model.Festivos;
import cl.minsal.divap.model.Mes;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.ProgramaFechaRemesa;
import cl.minsal.divap.model.ProgramaServicioCoreComponente;
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
	private AntecedentesComunaDAO antecedentesComunaDAO;
	@EJB
	private RemesasDAO remesasDAO;
	@EJB
	private ReportesServices reporteService;
	
	static long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000;
	static Integer REMESA_REGULAR=24;
	 
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
		List<ProgramaServicioCoreComponente> result = programasDAO.findByServicioComponenteSubtitulo(componenteSeleccionado,
				servicioSeleccionado, idTipoSubtitulo, idProgramaAno);
		List<OTResumenDependienteServicioVO> listaOTResumenDependienteServicioVO = new ArrayList<OTResumenDependienteServicioVO>();
		
		for (ProgramaServicioCoreComponente programaServicio : result) {
			OTResumenDependienteServicioVO prog = new OTResumenDependienteServicioVO();
			
			EstablecimientoVO establecimiento = new EstablecimientoVO();
			establecimiento.setCodigoEstablecimiento(programaServicio.getProgramaServicioCore1().getEstablecimiento().getCodigo());
			establecimiento.setId(programaServicio.getProgramaServicioCore1().getEstablecimiento().getId());
			establecimiento.setId_servicio_salud(programaServicio.getProgramaServicioCore1().getEstablecimiento().getServicioSalud().getId());
			establecimiento.setNombre(programaServicio.getProgramaServicioCore1().getEstablecimiento().getNombre());
			
			prog.setEstablecimiento(establecimiento);
			
			prog.setMarcoPresupuestario(programaServicio.getTarifa().longValue());
			
			
			prog.setTransferenciaAcumulada(12345323l);
			prog.setConveniosRecibidos(555424l);
			
			
			
			
			listaOTResumenDependienteServicioVO.add(prog);
		}
		return listaOTResumenDependienteServicioVO;

	}

	public List<OTResumenMunicipalVO> getDetalleOTMunicipal(
			Integer servicioSeleccionado, Integer integer) {
		// TODO Auto-generated method stub
		return null;
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
		 Date hoy = new Date();           
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
		ServicioSalud servicioSalud = servicioSaludDAO.getServicioSaludPorID(servicioSeleccionado);

		for (Comuna comuna : servicioSalud.getComunas()) {
			
			AntecendentesComunaCalculado antecedentesComunaCalculado = antecedentesComunaDAO.findByComunaAno(comuna.getId(), anoCurso);
			if(antecedentesComunaCalculado!=null){
				OTPerCapitaVO percapitaVO = new OTPerCapitaVO();
				percapitaVO.setIdComuna(comuna.getId());
				percapitaVO.setComuna(comuna.getNombre());
				percapitaVO.setMarcoPresupuestario(antecedentesComunaCalculado.getPercapitaAno()!=null?antecedentesComunaCalculado.getPercapitaAno():0);
				
				List<DetalleRemesas> listaRemesas = remesasDAO.getRemesasPagadasComunaLaFecha(programaAno, comuna.getId(),
						3, Integer.parseInt(reporteService.getMesCurso(true)));
				Long remesaAcumulada = 0l;
				for(DetalleRemesas remesaPagada : listaRemesas){
					remesaAcumulada += remesaPagada.getMontoRemesa().longValue();
				}
				
				percapitaVO.setTransferenciaAcumulada(remesaAcumulada);
				percapitaVO.setTipoComuna(antecedentesComunaCalculado.getAntecedentesComuna().getClasificacion().getDescripcion());
				
				
				List<RemesasProgramaVO> remesas = new ArrayList<RemesasProgramaVO>();
				try {
					remesas = getRemesasPrograma(programaAno, Integer.parseInt(reporteService.getMesCurso(true)));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
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
		}
		return listaReportePercapita;
	}
	
}