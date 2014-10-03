package minsal.divap.model.mappers;

import java.util.ArrayList;
import java.util.List;

import minsal.divap.vo.CajaMesVO;
import minsal.divap.vo.FlujoCajaVO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import cl.minsal.divap.model.Caja;
import cl.minsal.divap.model.CajaMonto;

public class FlujoCajaMapper implements Mapper<Caja>{

	
	@Override
	public Object getSummary(Caja paramT) {
		throw new NotImplementedException();
	}

	@Override
	public FlujoCajaVO getBasic(Caja caja) {
		if(caja == null){
			return null;
		}
		FlujoCajaVO flujoCajaVO = new FlujoCajaVO();
		if(caja.getMarcoPresupuestario() != null && caja.getMarcoPresupuestario().getServicioSalud() != null){
			flujoCajaVO.setServicio(caja.getMarcoPresupuestario().getServicioSalud().getNombre());
		}
		 
		flujoCajaVO.setMontoSubtitulo(caja.getMonto());
		if(caja.getCajaMontos() != null && caja.getCajaMontos().size() > 0){
			List<CajaMesVO> cajaMeses = new ArrayList<CajaMesVO>();
			for(CajaMonto cajaMonto : caja.getCajaMontos()){
				CajaMesVO cajaMes = new CajaMesVO();
				if(cajaMonto.getMes() != null){
					cajaMes.setMes(cajaMonto.getMes().getNombre());
				}
				if(cajaMonto.getMonto() != null){
					cajaMes.setMonto(new Long(cajaMonto.getMonto().getMonto()));
				}else{
					cajaMes.setMonto(new Long(0));
				}
				cajaMeses.add(cajaMes);
			}
			flujoCajaVO.setSubtitulo(cajaMeses);
		}
		
		return flujoCajaVO;
	}

	@Override
	public Object getData(Caja paramT) {
		throw new NotImplementedException();
	}

}
