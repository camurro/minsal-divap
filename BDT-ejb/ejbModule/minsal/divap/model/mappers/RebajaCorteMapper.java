package minsal.divap.model.mappers;

import minsal.divap.vo.MesVO;
import minsal.divap.vo.RebajaCorteVO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import cl.minsal.divap.model.RebajaCorte;

public class RebajaCorteMapper implements Mapper<RebajaCorte>{

	@Override
	public Object getSummary(RebajaCorte rebajaCorte) {
		throw new NotImplementedException();
	}

	@Override
	public RebajaCorteVO getBasic(RebajaCorte rebajaCorte) {
		if (rebajaCorte == null)
			return null;
		RebajaCorteVO rebajaCorteVO = new RebajaCorteVO();
		rebajaCorteVO.setId(rebajaCorte.getRebajaCorteId());
		if(rebajaCorte.getMesInicio() != null){
			MesVO mesVO = new MesVO();
			mesVO.setId(rebajaCorte.getMesInicio().getIdMes());
			mesVO.setNombre(rebajaCorte.getMesInicio().getNombre());
			rebajaCorteVO.setMesInicio(mesVO);
		}
		if(rebajaCorte.getMesHasta() != null){
			MesVO mesVO = new MesVO();
			mesVO.setId(rebajaCorte.getMesInicio().getIdMes());
			mesVO.setNombre(rebajaCorte.getMesInicio().getNombre());
			rebajaCorteVO.setMesHasta(mesVO);
		}
		if(rebajaCorte.getMesRebaja() != null){
			MesVO mesVO = new MesVO();
			mesVO.setId(rebajaCorte.getMesRebaja().getIdMes());
			mesVO.setNombre(rebajaCorte.getMesRebaja().getNombre());
			rebajaCorteVO.setMesRebaja(mesVO);
		}
		return rebajaCorteVO;
	}

	@Override
	public Object getData(RebajaCorte rebajaCorte) {
		throw new NotImplementedException();
	}

}
