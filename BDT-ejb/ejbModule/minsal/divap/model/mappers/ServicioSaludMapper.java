package minsal.divap.model.mappers;


import minsal.divap.vo.ServiciosVO;
import cl.minsal.divap.model.ServicioSalud;

public class ServicioSaludMapper implements Mapper<ServicioSalud>{

	
	@Override
	public Object getSummary(ServicioSalud paramT) {
		return null;
	}

	@Override
	public ServiciosVO getBasic(ServicioSalud servicioSalud) {
		if (servicioSalud==null){
			return null;
		}
		
		ServiciosVO servicioVO = new ServiciosVO(servicioSalud.getId(), servicioSalud.getNombre());
		servicioVO.setDirector(new PersonaMapper().getBasic(servicioSalud.getDirector()));
		servicioVO.setEncargadoAps(new PersonaMapper().getBasic(servicioSalud.getEncargadoAps()));
		servicioVO.setEncargadoFinanzasAps(new PersonaMapper().getBasic(servicioSalud.getEncargadoFinanzasAps()));
		return servicioVO;
	}

	@Override
	public Object getData(ServicioSalud servicioSalud) {
		return null;
	}

}
