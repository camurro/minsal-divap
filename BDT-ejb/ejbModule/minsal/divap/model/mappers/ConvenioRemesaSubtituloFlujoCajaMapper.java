package minsal.divap.model.mappers;

import java.util.ArrayList;
import java.util.List;

import minsal.divap.vo.CajaMontoSummaryVO;
import minsal.divap.vo.ConvenioSummaryVO;
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
		throw new NotImplementedException();
	}

	public SubtituloFlujoCajaVO getBasic(Caja caja, Integer mes) {
		if (caja == null){
			return null;
		}
		SubtituloFlujoCajaVO subtituloFlujoCajaVO = new SubtituloFlujoCajaVO();
		if(caja.getMarcoPresupuestario() != null){
			if(caja.getMarcoPresupuestario().getMarcoModificado() > 0){
				subtituloFlujoCajaVO.setMarcoPresupuestario(caja.getMarcoPresupuestario().getMarcoModificado());
			}else{
				subtituloFlujoCajaVO.setMarcoPresupuestario(caja.getMarcoPresupuestario().getMarcoInicial());
			}
			if(caja.getMarcoPresupuestario().getServicioSalud() != null){
				subtituloFlujoCajaVO.setServicio(caja.getMarcoPresupuestario().getServicioSalud().getNombre());
			}
		}
		if(caja.getIdComponente() != null){
			subtituloFlujoCajaVO.setIdComponente(caja.getIdComponente().getId());
			subtituloFlujoCajaVO.setComponente(caja.getIdComponente().getNombre());
		}
		TransferenciaSummaryVO transferenciaAcumulada = new TransferenciaSummaryVO();
		transferenciaAcumulada.setPorcentaje(0);
		transferenciaAcumulada.setMonto(0);
		subtituloFlujoCajaVO.setTransferenciaAcumulada(transferenciaAcumulada);
		List<CajaMontoSummaryVO> cajasMontosSummaryVO = new ArrayList<CajaMontoSummaryVO>();
		if(caja.getCajaMontos() != null && caja.getCajaMontos().size() > 0){
			for(CajaMonto cajaMonto : caja.getCajaMontos()){
				CajaMontoSummaryVO cajaMontoSummaryVO = new CajaMontoSummaryVO();
				cajaMontoSummaryVO.setIdCajaMonto(cajaMonto.getCaja().getId());
				cajaMontoSummaryVO.setIdMes(cajaMonto.getMes().getIdMes());
				cajaMontoSummaryVO.setNombreMes(cajaMonto.getMes().getNombre());
				cajaMontoSummaryVO.setMontoMes(cajaMonto.getMonto().getMonto());
				cajasMontosSummaryVO.add(cajaMontoSummaryVO);
			}
		}
		subtituloFlujoCajaVO.setCajaMontos(cajasMontosSummaryVO);
		ConvenioSummaryVO convenioRecibido = new ConvenioSummaryVO();
		convenioRecibido.setMonto(0);
		convenioRecibido.setPorcentaje(0);
		subtituloFlujoCajaVO.setConvenioRecibido(convenioRecibido);
		return subtituloFlujoCajaVO;
	}

	@Override
	public Object getData(Caja paramT) {
		throw new NotImplementedException();
	}

}