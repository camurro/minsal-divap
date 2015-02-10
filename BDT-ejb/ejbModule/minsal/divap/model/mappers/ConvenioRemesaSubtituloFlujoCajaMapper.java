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

public class ConvenioRemesaSubtituloFlujoCajaMapper implements Mapper<Caja>{


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
		if(caja.getIdSubtitulo() != null){
			subtituloFlujoCajaVO.setIdSubtitulo(caja.getIdSubtitulo().getIdTipoSubtitulo());
			subtituloFlujoCajaVO.setSubtitulo(caja.getIdSubtitulo().getNombreSubtitulo());
		}
		TransferenciaSummaryVO transferenciaAcumulada = new TransferenciaSummaryVO();
		transferenciaAcumulada.setPorcentaje(0.0);
		transferenciaAcumulada.setMonto(0L);
		subtituloFlujoCajaVO.setTransferenciaAcumulada(transferenciaAcumulada);
		ConveniosSummaryVO convenioRecibido = new ConveniosSummaryVO();
		convenioRecibido.setMonto(0);
		convenioRecibido.setPorcentaje(0.0);
		subtituloFlujoCajaVO.setConvenioRecibido(convenioRecibido);
		List<CajaMontoSummaryVO> cajasMontosSummaryVO = new ArrayList<CajaMontoSummaryVO>();
		if(caja.getCajaMontos() != null && caja.getCajaMontos().size() > 0){
			for(CajaMonto cajaMonto : caja.getCajaMontos()){
				CajaMontoSummaryVO cajaMontoSummaryVO = new CajaMontoSummaryVO();
				cajaMontoSummaryVO.setIdMes(cajaMonto.getMes().getIdMes());
				cajaMontoSummaryVO.setNombreMes(cajaMonto.getMes().getNombre());
				cajaMontoSummaryVO.setMontoMes(new Long(cajaMonto.getMonto()));
				cajasMontosSummaryVO.add(cajaMontoSummaryVO);
			}
		}
		return subtituloFlujoCajaVO;
	}

	@Override
	public Object getData(Caja paramT) {
		throw new NotImplementedException();
	}

}
