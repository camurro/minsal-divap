package minsal.divap.model.mappers;

import minsal.divap.vo.CajaMontoSummaryVO;
import minsal.divap.vo.ConveniosSummaryVO;
import minsal.divap.vo.SubtituloFlujoCajaVO;
import minsal.divap.vo.TransferenciaSummaryVO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import cl.minsal.divap.model.Caja;
import cl.minsal.divap.model.CajaMonto;

public class SubtituloFlujoCajaMapper implements Mapper<Caja>{


	@Override
	public Object getSummary(Caja caja) {
		throw new NotImplementedException();
	}

	@Override
	public SubtituloFlujoCajaVO getBasic(Caja caja) {
		if (caja == null){
			return null;
		}
		SubtituloFlujoCajaVO subtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
		if(caja.getServicio() != null){
			subtituloFlujoCajaVO.setIdMarcoPresupuestario(caja.getId());
			if(caja.getServicio() != null){
				subtituloFlujoCajaVO.setIdServicio(caja.getServicio().getId());
				subtituloFlujoCajaVO.setServicio(caja.getServicio().getNombre());
			}
		}
		if(caja.getIdSubtitulo() != null){
			subtituloFlujoCajaVO.setIdSubtitulo(caja.getIdSubtitulo().getIdTipoSubtitulo());
			subtituloFlujoCajaVO.setSubtitulo(caja.getIdSubtitulo().getNombreSubtitulo());
		}
		TransferenciaSummaryVO transferenciaAcumulada = new TransferenciaSummaryVO(0.0, 0L);
		subtituloFlujoCajaVO.setTransferenciaAcumulada(transferenciaAcumulada);
		ConveniosSummaryVO convenioRecibido = new ConveniosSummaryVO();
		convenioRecibido.setMonto(0);
		convenioRecibido.setPorcentaje(0.0);
		subtituloFlujoCajaVO.setConvenioRecibido(convenioRecibido);
		Long marcoPresupuestario = 0L;
		if(caja.getCajaMontos() != null && caja.getCajaMontos().size() > 0){
			for(CajaMonto cajaMonto : caja.getCajaMontos()){
				CajaMontoSummaryVO cajaMontoSummaryVO = new CajaMontoSummaryVO();
				cajaMontoSummaryVO.setIdMes(cajaMonto.getMes().getIdMes());
				cajaMontoSummaryVO.setNombreMes(cajaMonto.getMes().getNombre());
				cajaMontoSummaryVO.setMontoMes(new Long(cajaMonto.getMonto()));
				marcoPresupuestario += cajaMonto.getMonto();
				switch (cajaMonto.getMes().getIdMes()) {
				case 1:
					subtituloFlujoCajaVO.setCajaMontoEnero(cajaMontoSummaryVO);
					break;
				case 2:
					subtituloFlujoCajaVO.setCajaMontoFebrero(cajaMontoSummaryVO);
					break;
				case 3:
					subtituloFlujoCajaVO.setCajaMontoMarzo(cajaMontoSummaryVO);
					break;
				case 4:
					subtituloFlujoCajaVO.setCajaMontoAbril(cajaMontoSummaryVO);
					break;
				case 5:
					subtituloFlujoCajaVO.setCajaMontoMayo(cajaMontoSummaryVO);
					break;
				case 6:
					subtituloFlujoCajaVO.setCajaMontoJunio(cajaMontoSummaryVO);
					break;
				case 7:
					subtituloFlujoCajaVO.setCajaMontoJulio(cajaMontoSummaryVO);
					break;
				case 8:
					subtituloFlujoCajaVO.setCajaMontoAgosto(cajaMontoSummaryVO);
					break;
				case 9:
					subtituloFlujoCajaVO.setCajaMontoSeptiembre(cajaMontoSummaryVO);
					break;
				case 10:
					subtituloFlujoCajaVO.setCajaMontoOctubre(cajaMontoSummaryVO);
					break;
				case 11:
					subtituloFlujoCajaVO.setCajaMontoNoviembre(cajaMontoSummaryVO);
					break;
				case 12:
					subtituloFlujoCajaVO.setCajaMontoDiciembre(cajaMontoSummaryVO);
					break;
				default:
					break;
				}

			}
		}
		subtituloFlujoCajaVO.setMarcoPresupuestario(marcoPresupuestario);
		return subtituloFlujoCajaVO;
	}

	@Override
	public Object getData(Caja paramT) {
		throw new NotImplementedException();
	}

}
