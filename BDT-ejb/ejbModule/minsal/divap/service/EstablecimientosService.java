package minsal.divap.service;


import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import minsal.divap.dao.EstablecimientosDAO;
import minsal.divap.vo.EstablecimientoVO;
import minsal.divap.vo.ProgramaAPSServicioVO;
import minsal.divap.vo.ProgramaAPSVO;
import cl.minsal.divap.model.Comuna;
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


	public List<ProgramaAPSServicioVO> getServiciosComunasEstablecimientos() {
		List<ProgramaAPSServicioVO> servicioComunaEstablecimientosVOList = new ArrayList<ProgramaAPSServicioVO>();
		List<Establecimiento> establecimientos = this.establecimientosDAO.getEstablecimientos();
		if(establecimientos != null && establecimientos.size() > 0){
			for(Establecimiento establecimiento : establecimientos){
				ProgramaAPSServicioVO servicioComunaVO = new ProgramaAPSServicioVO();
				servicioComunaVO.setIdEstablecimiento(establecimiento.getId());
				servicioComunaVO.setCodigo(establecimiento.getCodigo());
				servicioComunaVO.setEstablecimiento(establecimiento.getNombre());
				servicioComunaVO.setIdServicioSalud(establecimiento.getServicioSalud().getId());
				servicioComunaVO.setServicioSalud(establecimiento.getServicioSalud().getNombre());
				servicioComunaEstablecimientosVOList.add(servicioComunaVO);
			}
		}
		return servicioComunaEstablecimientosVOList;
	}
	
	
	
}
