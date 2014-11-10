package minsal.divap.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import minsal.divap.dao.ComunaDAO;
import minsal.divap.vo.ComunaVO;
import minsal.divap.vo.ProgramaAPSVO;
import cl.minsal.divap.model.Comuna;

@Stateless
@LocalBean
public class ComunaService {

	@EJB
	private ComunaDAO comunaDAO;

	public List<ComunaVO> getComunas() {
		List<ComunaVO> ComunasVO = new ArrayList<ComunaVO>();
		List<Comuna> comunas = this.comunaDAO.getComuna();
		if (comunas != null && comunas.size() > 0) {
			for (Comuna comuna : comunas) {
				ComunaVO comunaVO = new ComunaVO();

				comunaVO.setIdComuna(comuna.getId());
				comunaVO.setDescComuna(comuna.getNombre());
				ComunasVO.add(comunaVO);
			}
		}
		return ComunasVO;
	}
	
	public List<ProgramaAPSVO> getServiciosComunas(){
		List<ProgramaAPSVO> servicioComunaVOList = new ArrayList<ProgramaAPSVO>();
		List<Comuna> comunas = this.comunaDAO.getComuna();
		if(comunas != null && comunas.size() > 0){
			for(Comuna comuna : comunas){
				ProgramaAPSVO servicioComunaVO = new ProgramaAPSVO();
				servicioComunaVO.setIdComuna(comuna.getId());
				servicioComunaVO.setComuna(comuna.getNombre());
				servicioComunaVO.setIdServicioSalud(comuna.getServicioSalud().getId());
				servicioComunaVO.setServicioSalud(comuna.getServicioSalud().getNombre());
				servicioComunaVOList.add(servicioComunaVO);
			}
		}
		return servicioComunaVOList;
	}
		
	public Comuna getComunaById(int id){
		Comuna comuna = this.comunaDAO.getComunaById(id);		
		return comuna;	
	}
	
	public List<ComunaVO> getComunasByServicio(Integer idServicio) {
		List<ComunaVO> comunasVO = new ArrayList<ComunaVO>();
		List<Comuna> comunas = this.comunaDAO.getComunasByServicio(idServicio);
		if(comunas != null && comunas.size() > 0){
			for(Comuna comuna : comunas){
				ComunaVO comunaVO = new ComunaVO();
				comunaVO.setIdComuna(comuna.getId());
				comunaVO.setDescComuna(comuna.getNombre());
				comunasVO.add(comunaVO);
			}
		}
		return comunasVO;
	}
	
}
