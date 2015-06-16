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
		
		ServiciosVO servicioSaludVO = new ServiciosVO();
		
		servicioSaludVO.setIdServicio(servicioSalud.getId());
		servicioSaludVO.setNombreServicio(servicioSalud.getNombre());
		
		return servicioSaludVO;
	}

	@Override
	public Object getData(ServicioSalud paramT) {
		// TODO Auto-generated method stub
		return null;
	}

}
