package minsal.divap.model.mappers;

import java.util.ArrayList;
import java.util.List;

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
		subtituloFlujoCajaVO.setMarcoPresupuestario(new Long(caja.getMonto()));
		if(caja.getMarcoPresupuestario() != null){
			subtituloFlujoCajaVO.setIdMarcoPresupuestario(caja.getMarcoPresupuestario().getIdMarcoPresupuestario());
			if(caja.getMarcoPresupuestario().getServicioSalud() != null){
				subtituloFlujoCajaVO.setIdServicio(caja.getMarcoPresupuestario().getServicioSalud().getId());
				subtituloFlujoCajaVO.setServicio(caja.getMarcoPresupuestario().getServicioSalud().getNombre());
			}
		}
		if(caja.getIdSubtitulo() != null){
			subtituloFlujoCajaVO.setIdSubtitulo(caja.getIdSubtitulo().getIdTipoSubtitulo());
			subtituloFlujoCajaVO.setSubtitulo(caja.getIdSubtitulo().getNombreSubtitulo());
		}
		TransferenciaSummaryVO transferenciaAcumulada = new TransferenciaSummaryVO(0, 0L);
		subtituloFlujoCajaVO.setTransferenciaAcumulada(transferenciaAcumulada);
		ConveniosSummaryVO convenioRecibido = new ConveniosSummaryVO();
		convenioRecibido.setMonto(0);
		convenioRecibido.setPorcentaje(0);
		subtituloFlujoCajaVO.setConvenioRecibido(convenioRecibido);
		List<CajaMontoSummaryVO> cajasMontosSummaryVO = new ArrayList<CajaMontoSummaryVO>();
		if(caja.getCajaMontos() != null && caja.getCajaMontos().size() > 0){
			for(CajaMonto cajaMonto : caja.getCajaMontos()){
				CajaMontoSummaryVO cajaMontoSummaryVO = new CajaMontoSummaryVO();
				cajaMontoSummaryVO.setIdMes(cajaMonto.getMes().getIdMes());
				cajaMontoSummaryVO.setNombreMes(cajaMonto.getMes().getNombre());
				cajaMontoSummaryVO.setMontoMes(new Long(cajaMonto.getMonto().getMonto()));
				cajasMontosSummaryVO.add(cajaMontoSummaryVO);
			}
		}
		subtituloFlujoCajaVO.setCajaMontos(cajasMontosSummaryVO);
		return subtituloFlujoCajaVO;
	}

	@Override
	public Object getData(Caja paramT) {
		throw new NotImplementedException();
	}

}
