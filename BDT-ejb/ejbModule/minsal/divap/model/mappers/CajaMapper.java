package minsal.divap.model.mappers;

import minsal.divap.vo.CajaVO;
import minsal.divap.vo.ComponentesVO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import cl.minsal.divap.model.Caja;
import cl.minsal.divap.model.Componente;

public class CajaMapper implements Mapper<Caja>{

	
	@Override
	public Object getSummary(Caja paramT) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CajaVO getBasic(Caja caja) {
		// TODO Auto-generated method stub
		ComponenteMapper componenteMapper = new ComponenteMapper();
		if (caja==null)
		return null;
		
		CajaVO cajaVO = new CajaVO();
		
		cajaVO.setId(caja.getId());
		
		if (caja.getIdServicio()!=null){
			cajaVO.setServicio(caja.getIdServicio().getNombre());
			cajaVO.setIdServicio(caja.getIdServicio().getId());
		}
		
		if (caja.getIdComuna()!=null){
			cajaVO.setComuna(caja.getIdComuna().getNombre());
			cajaVO.setIdComuna(caja.getIdComuna().getId());
		}
		
		if (caja.getMarcoPresupuestario()!=null)
			cajaVO.setMarcoMonto(caja.getMarcoPresupuestario());
		
		//TODO: [ASAAVEDRA] Obtener desde los otros procesos.
		cajaVO.setTransferenciaMonto(0);
		cajaVO.setTransferenciaPorcentaje(0);
		cajaVO.setRemesaMonto(0);
		cajaVO.setRemesaPorcentaje(0);
		cajaVO.setConvenioMonto(0);
		cajaVO.setConvenioPorcentaje(0);
		
		if (caja.getIdEstablecimiento()!=null){
			cajaVO.setEstablecimiento(caja.getIdEstablecimiento().getNombre());
			cajaVO.setIdEstablecimiento(caja.getIdEstablecimiento().getId());
		}
		
		if (caja.getIdComponente()!=null){
			cajaVO.setComponente(componenteMapper.getBasic(caja.getIdComponente()));
		}
		
		
		
		if (caja.getEnero()!=null)
		cajaVO.setEnero(caja.getEnero());
		if (caja.getFebrero()!=null)
			cajaVO.setFebrero(caja.getFebrero());
		if (caja.getMarzo()!=null)
			cajaVO.setMarzo(caja.getMarzo());
		if (caja.getAbril()!=null)
			cajaVO.setAbril(caja.getAbril());
		if (caja.getMayo()!=null)
			cajaVO.setMayo(caja.getMayo());
		if (caja.getJunio()!=null)
			cajaVO.setJunio(caja.getJunio());
		if (caja.getJulio()!=null)
			cajaVO.setJulio(caja.getJulio());
		if (caja.getAgosto()!=null)
			cajaVO.setAgosto(caja.getAgosto());
		if (caja.getSeptiembre()!=null)
			cajaVO.setSeptiembre(caja.getSeptiembre());
		if (caja.getOctubre()!=null)
			cajaVO.setOctubre(caja.getOctubre());
		if (caja.getNoviembre()!=null)
			cajaVO.setNoviembre(caja.getNoviembre());
		if (caja.getDiciembre()!=null)
			cajaVO.setDiciembre(caja.getDiciembre());
		
		if (caja.getTotal()!= null)
			cajaVO.setTotal(caja.getTotal());
		
		
//		cajaVO.setTotalConvenioMonto(0);
//		cajaVO.setTotalRemesaMonto(0);
//		cajaVO.setTotalTransferenciaMonto(0);
		
		
		
		return cajaVO;
	}

	@Override
	public Object getData(Caja paramT) {
		// TODO Auto-generated method stub
		return null;
	}

}
