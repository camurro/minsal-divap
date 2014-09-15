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
	
	
	public List<EstablecimientoVO> getEstablecimientosByComuna(Integer IDComuna) {
		
	
		
		List<Establecimiento> establecimiento = this.establecimientosDAO.getEstablecimientosByComuna(IDComuna);
		ArrayList<EstablecimientoVO> result = new ArrayList<EstablecimientoVO>(establecimiento.size());
		for (Establecimiento estab : establecimiento){
			EstablecimientoVO establecimientoVO = new EstablecimientoVO();
			establecimientoVO.setId(estab.getId());
			establecimientoVO.setNombre(estab.getNombre());
			
			
			
			result.add(establecimientoVO);
		}
		return result;
	}
	
	
	
}
