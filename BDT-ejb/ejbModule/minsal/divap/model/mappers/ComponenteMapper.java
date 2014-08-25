package minsal.divap.model.mappers;

import minsal.divap.vo.ComponentesVO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import cl.minsal.divap.model.Componente;

public class ComponenteMapper implements Mapper<Componente>{

	@Override
	public Object getSummary(Componente componente) {
		throw new NotImplementedException();
	}

	@Override
	public ComponentesVO getBasic(Componente componente) {
		if (componente == null)
		      return null;
		ComponentesVO componentesVO = new ComponentesVO();
		componentesVO.setComponente_id(componente.getId());
		componentesVO.setNombre(componente.getNombre());
		return componentesVO;
	
	}

	@Override
	public Object getData(Componente componente) {
		throw new NotImplementedException();
	}

}
