package minsal.divap.service;


import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import minsal.divap.dao.EstablecimientosDAO;
import minsal.divap.vo.EstablecimientoVO;
import cl.minsal.divap.model.Establecimiento;

@Stateless
@LocalBean
public class EstablecimientosService {


	@EJB
	private EstablecimientosDAO establecimientosDAO;


	public List<EstablecimientoVO> getEstablecimientosByComuna(Integer idComuna) {
		List<Establecimiento> establecimientos = this.establecimientosDAO.getEstablecimientosByComuna(idComuna);
		List<EstablecimientoVO> result = new ArrayList<EstablecimientoVO>();
		if(establecimientos != null && establecimientos.size() > 0){
			for (Establecimiento establecimiento : establecimientos){
				EstablecimientoVO establecimientoVO = new EstablecimientoVO();
				establecimientoVO.setId(establecimiento.getId());
				establecimientoVO.setNombre(establecimiento.getNombre());
				result.add(establecimientoVO);
			}
		}
		return result;
	}

	public List<EstablecimientoVO> getEstablecimientosByServicio(Integer idServicio) {
		List<Establecimiento> establecimientos = this.establecimientosDAO.getEstablecimientosByServicio(idServicio);
		List<EstablecimientoVO> result = new ArrayList<EstablecimientoVO>();
		if(establecimientos != null && establecimientos.size() > 0){
			for (Establecimiento establecimiento : establecimientos){
				EstablecimientoVO establecimientoVO = new EstablecimientoVO();
				establecimientoVO.setId(establecimiento.getId());
				establecimientoVO.setNombre(establecimiento.getNombre());
				result.add(establecimientoVO);
			}
		}
		return result;
	}
}
