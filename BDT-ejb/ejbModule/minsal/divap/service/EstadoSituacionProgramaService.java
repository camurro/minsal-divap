package minsal.divap.service;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import cl.minsal.divap.model.MarcoPresupuestario;
import cl.minsal.divap.model.ServicioSalud;
import minsal.divap.dao.CajaDAO;
import minsal.divap.dao.ConveniosDAO;
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
	

	private EstadoSituacionProgramaVo getEstadoSituacionPrograma(Integer idServicio, Integer idProgramaAno, Integer idComponente, Integer idSubtitulo ){
		EstadoSituacionProgramaVo resultado = new EstadoSituacionProgramaVo();
		ProgramaVO programaVO = programasService.getProgramaAno(idProgramaAno);
		ServicioSalud servicio = servicioSaludService.getServicioSaludPorID(idServicio);
		resultado.setServicio(servicio.getNombre());
		
		
		MarcoPresupuestario marco = cajaDAO.getMarcoPresupuestarioByProgramaAnoServicio(idProgramaAno, idServicio);
		resultado.setMarcoInicial((long)marco.getMarcoInicial());
		resultado.setMarcoModificado((long)marco.getMarcoModificado());
		
		

		
		return resultado;
	}
	
	
}
