package minsal.divap.model.mappers;

import java.util.ArrayList;
import java.util.List;

import minsal.divap.vo.ComunaSummaryVO;
import minsal.divap.vo.EstablecimientoSummaryVO;
import minsal.divap.vo.ServiciosSummaryVO;
import minsal.divap.vo.ServiciosVO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.Establecimiento;
import cl.minsal.divap.model.ServicioSalud;

public class ServicioMapper implements Mapper<ServicioSalud>{

	@Override
	public ServiciosSummaryVO getSummary(ServicioSalud servicioSalud) {
		if (servicioSalud == null)
			return null;
		ServiciosSummaryVO serviciosSummaryVO = new ServiciosSummaryVO(servicioSalud.getId(), servicioSalud.getNombre());
		return serviciosSummaryVO;
	}

	@Override
	public ServiciosVO getBasic(ServicioSalud servicioSalud) {
		if (servicioSalud == null)
			return null;
		ServiciosVO serviciosVO = new ServiciosVO(servicioSalud.getId(), servicioSalud.getNombre(), new PersonaMapper().getBasic(servicioSalud.getDirector()),
				new PersonaMapper().getBasic(servicioSalud.getEncargadoAps()), new PersonaMapper().getBasic(servicioSalud.getEncargadoFinanzasAps()));
		if(servicioSalud.getComunas() != null && servicioSalud.getComunas().size() > 0){
			List<ComunaSummaryVO> comunas = new ArrayList<ComunaSummaryVO>();
			for(Comuna comuna : servicioSalud.getComunas()){
				comunas.add(new ComunaSummaryVO(comuna.getId(), comuna.getNombre()));
			}
			serviciosVO.setComunas(comunas);
		}
		if(servicioSalud.getEstablecimientos() != null && servicioSalud.getEstablecimientos().size() > 0){
			List<EstablecimientoSummaryVO> establecimientos = new ArrayList<EstablecimientoSummaryVO>();
			for(Establecimiento establecimiento : servicioSalud.getEstablecimientos()){
				establecimientos.add(new EstablecimientoSummaryVO(establecimiento.getId(), establecimiento.getNombre()));
			}
			serviciosVO.setEstableclimientos(establecimientos);
		}
		return serviciosVO;
	}

	@Override
	public Object getData(ServicioSalud servicioSalud) {
		throw new NotImplementedException();
	}

}
