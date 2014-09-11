package minsal.divap.model.mappers;

import java.util.ArrayList;
import java.util.List;

import minsal.divap.vo.ComunaSummaryVO;
import minsal.divap.vo.ServiciosVO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.ServicioSalud;

public class ServicioMapper implements Mapper<ServicioSalud>{

	@Override
	public Object getSummary(ServicioSalud servicioSalud) {
		throw new NotImplementedException();
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
		return serviciosVO;
	}

	@Override
	public Object getData(ServicioSalud servicioSalud) {
		throw new NotImplementedException();
	}

}
