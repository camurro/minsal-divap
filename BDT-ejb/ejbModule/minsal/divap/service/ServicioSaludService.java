package minsal.divap.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import minsal.divap.dao.ServicioSaludDAO;
import minsal.divap.vo.BaseVO;
import minsal.divap.vo.RebajaVO;
import minsal.divap.vo.ServiciosVO;
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
	
	
	public List<ServiciosVO> getAllServiciosVO() {
		List<ServicioSalud> serviciosSalud = this.servicioSaludDAO.getServicios();
		List<ServiciosVO> result = new ArrayList<ServiciosVO>();
		if(serviciosSalud != null){
			for (ServicioSalud servicioSalud : serviciosSalud){
					ServiciosVO servicioVO = new ServiciosVO();
					servicioVO.setId_servicio(servicioSalud.getId());
					servicioVO.setNombre_servicio(servicioSalud.getNombre());
					
					result.add(servicioVO);
			}
		}
		return result;
	}

}
