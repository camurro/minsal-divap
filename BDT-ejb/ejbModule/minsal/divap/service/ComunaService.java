package minsal.divap.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import minsal.divap.dao.ComunaDAO;
import minsal.divap.vo.ComunaVO;
import cl.minsal.divap.model.Comuna;

@Stateless
@LocalBean
public class ComunaService {

	@EJB
	private ComunaDAO comunaDAO;
	
	
	
	public List<ComunaVO> getComunas() {
		
	
	List<ComunaVO> ComunasVO = new ArrayList<ComunaVO>();
	List<Comuna> comunas = this.comunaDAO.getComuna();
	if(comunas != null && comunas.size() > 0){
		for(Comuna comuna : comunas){
			ComunaVO comunaVO = new ComunaVO();
	
		
			comunaVO.setIdComuna(comuna.getId());
			comunaVO.setDescComuna(comuna.getNombre());
			ComunasVO.add(comunaVO);
		}
	}
	return ComunasVO;
}
}
