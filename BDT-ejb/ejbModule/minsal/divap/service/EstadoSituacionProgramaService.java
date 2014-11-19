package minsal.divap.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import cl.minsal.divap.model.MarcoPresupuestario;
import cl.minsal.divap.model.ReliquidacionServicio;
import cl.minsal.divap.model.ServicioSalud;
import minsal.divap.dao.CajaDAO;
import minsal.divap.dao.ConveniosDAO;
import minsal.divap.dao.ReliquidacionDAO;
import minsal.divap.vo.EstadoSituacionProgramaVo;
import minsal.divap.vo.ProgramaVO;

@Stateless
@LocalBean
public class EstadoSituacionProgramaService {
	@EJB
	private ProgramasService programasService;
	@EJB
	private ServicioSaludService servicioSaludService;
	@EJB
	private CajaDAO cajaDAO;
	@EJB 
	private ConveniosDAO conveniosDAO;
	@EJB
	private ReliquidacionDAO reliquidacionDAO;
	
	
	
	

	private EstadoSituacionProgramaVo getEstadoSituacionPrograma(Integer idServicio, Integer idProgramaAno, Integer idComponente, Integer idSubtitulo ){
		EstadoSituacionProgramaVo resultado = new EstadoSituacionProgramaVo();
		ProgramaVO programaVO = programasService.getProgramaAno(idProgramaAno);
		ServicioSalud servicio = servicioSaludService.getServicioSaludPorID(idServicio);
		resultado.setServicio(servicio.getNombre());
		
		
		MarcoPresupuestario marco = cajaDAO.getMarcoPresupuestarioByProgramaAnoServicio(idProgramaAno, idServicio);
		resultado.setMarcoInicial((long)marco.getMarcoInicial());
		resultado.setMarcoModificado((long)marco.getMarcoModificado());
		
		
		//TODO ver lo de los convenios
		resultado.setConvenioRecibido_monto((long)66666666);
		resultado.setConvenioRecibido_porcentaje(0.4);
		resultado.setConvenioPendiente_monto((long)999999999);
		resultado.setConvenioPendiente_porcentaje(0.3);
		
		//TODO ver remesas
		resultado.setRemesaAcumulada_monto(null);
		resultado.setRemesaAcumulada_porcentaje(null);
		resultado.setRemesaPendiente_monto(null);
		resultado.setRemesaPendiente_porcentaje(null);
		
		List<ReliquidacionServicio> reliquidacionServicios = reliquidacionDAO.getReliquidacionServicioByProgramaAnoServicioComponente(idProgramaAno, idServicio, idComponente);
		
		//resultado.setReliquidacion_monto();
		
		return resultado;
	}
	
	
}
