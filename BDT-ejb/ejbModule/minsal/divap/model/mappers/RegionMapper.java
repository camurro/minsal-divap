package minsal.divap.model.mappers;

import java.util.ArrayList;
import java.util.List;

import minsal.divap.vo.RegionVO;
import minsal.divap.vo.ServiciosVO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import cl.minsal.divap.model.Region;
import cl.minsal.divap.model.ServicioSalud;

public class RegionMapper implements Mapper<Region>{

	@Override
	public Object getSummary(Region region) {
		throw new NotImplementedException();
	}

	@Override
	public RegionVO getBasic(Region region) {
		if (region == null)
		      return null;
		RegionVO regionVO = new RegionVO(region.getId(), region.getNombre(), new PersonaMapper().getBasic(region.getSecretarioRegional()));
		if(region.getServicioSaluds() != null && region.getServicioSaluds().size() > 0){
			List<ServiciosVO> servicios = new ArrayList<ServiciosVO>();
			for(ServicioSalud servicioSalud : region.getServicioSaluds()){
				servicios.add(new ServicioMapper().getBasic(servicioSalud));
			}
			regionVO.setServicios(servicios);
		}
		return regionVO;
	}

	@Override
	public Object getData(Region region) {
		throw new NotImplementedException();
	}

}
