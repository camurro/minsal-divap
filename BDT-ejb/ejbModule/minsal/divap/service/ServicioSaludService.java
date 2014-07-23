package minsal.divap.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import minsal.divap.dao.ServicioSaludDAO;
import minsal.divap.vo.BaseVO;
import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.ServicioSalud;

@Stateless
@LocalBean
public class ServicioSaludService {
	@EJB
	private ServicioSaludDAO servicioSaludDAO;

	public List<BaseVO> getAllServicios() {
		List<ServicioSalud> serviciosSalud = this.servicioSaludDAO.getServicios();
		List<BaseVO> result = new ArrayList<BaseVO>();
		if(serviciosSalud != null){
			for (ServicioSalud servicioSalud : serviciosSalud){
				for (Comuna comuna : servicioSalud.getComunas()){
					BaseVO baseVO = new BaseVO();
					baseVO.setRegion(((servicioSalud.getRegion() != null)?servicioSalud.getRegion().getId():null));
					baseVO.setServicio(((servicioSalud.getNombre() != null)?servicioSalud.getNombre():null));
					baseVO.setComuna(((comuna != null)?comuna.getNombre():null));
					result.add(baseVO);
				}
			}
		}
		return result;
	}

}
