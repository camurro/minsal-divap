package minsal.divap.model.mappers;

import minsal.divap.vo.CajaMesVO;
import minsal.divap.vo.FlujoCajaVO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import cl.minsal.divap.model.Caja;
import cl.minsal.divap.model.CajaMonto;

public class FlujoCajaMapper implements Mapper<Caja>{


	@Override
	public Object getSummary(Caja caja) {
		throw new NotImplementedException();
	}

	@Override
	public FlujoCajaVO getBasic(Caja caja) {
		if(caja == null){
			return null;
		}
		FlujoCajaVO flujoCajaVO = new FlujoCajaVO();
		if(caja.getServicio() != null){
			flujoCajaVO.setServicio(caja.getServicio().getNombre());
		}

		Integer totalSubittulo = 0;
		int mes = 1;
		if(caja.getCajaMontos() != null && caja.getCajaMontos().size() > 0){
			for(CajaMonto cajaMonto : caja.getCajaMontos()){
				CajaMesVO cajaMes = new CajaMesVO();
				if(cajaMonto.getMes() != null){
					cajaMes.setMes(cajaMonto.getMes().getNombre());
				}
				cajaMes.setMonto(new Long(cajaMonto.getMonto()));
				switch (mes) {
				case 1:
					flujoCajaVO.setCajaMesEneroSubtitulo(cajaMes);
					break;
				case 2:
					flujoCajaVO.setCajaMesFebreroSubtitulo(cajaMes);
					break;
				case 3:
					flujoCajaVO.setCajaMesMarzoSubtitulo(cajaMes);
					break;
				case 4:
					flujoCajaVO.setCajaMesAbrilSubtitulo(cajaMes);
					break;
				case 5:
					flujoCajaVO.setCajaMesMayoSubtitulo(cajaMes);
					break;
				case 6:
					flujoCajaVO.setCajaMesJunioSubtitulo(cajaMes);
					break;
				case 7:
					flujoCajaVO.setCajaMesJulioSubtitulo(cajaMes);
					break;
				case 8:
					flujoCajaVO.setCajaMesAgostoSubtitulo(cajaMes);
					break;
				case 9:
					flujoCajaVO.setCajaMesSeptiembreSubtitulo(cajaMes);
					break;
				case 10:
					flujoCajaVO.setCajaMesOctubreSubtitulo(cajaMes);
					break;
				case 11:
					flujoCajaVO.setCajaMesNoviembreSubtitulo(cajaMes);
					break;
				case 12:
					flujoCajaVO.setCajaMesDiciembreSubtitulo(cajaMes);
					break;
				default:
					break;
				}
				mes++;
			}
		}
		flujoCajaVO.setMontoSubtitulo(totalSubittulo);
		return flujoCajaVO;
	}

	@Override
	public Object getData(Caja paramT) {
		throw new NotImplementedException();
	}

}
