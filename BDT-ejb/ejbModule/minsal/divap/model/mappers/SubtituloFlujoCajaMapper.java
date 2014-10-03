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

public class SubtituloFlujoCajaMapper implements Mapper<Caja>{


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
		System.out.println("kskskskskksksksksk="+caja.getMonto());
		subtituloFlujoCajaVO.setMarcoPresupuestario(caja.getMonto());
		if(caja.getMarcoPresupuestario() != null){
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
		List<CajaMontoSummaryVO> cajasMontosSummaryVO = new ArrayList<CajaMontoSummaryVO>();
		if(caja.getCajaMontos() != null && caja.getCajaMontos().size() > 0){
			for(CajaMonto cajaMonto : caja.getCajaMontos()){
				if(cajaMonto.getMes().getIdMes() < mes){
					transferenciaAcumulada.setMonto(transferenciaAcumulada.getMonto() + cajaMonto.getMonto().getMonto());
					transferenciaAcumulada.setPorcentaje(transferenciaAcumulada.getPorcentaje() + cajaMonto.getMonto().getPorcentajeMonto());
				}
				CajaMontoSummaryVO cajaMontoSummaryVO = new CajaMontoSummaryVO();
				cajaMontoSummaryVO.setIdCajaMonto(cajaMonto.getCaja().getId());
				cajaMontoSummaryVO.setIdMes(cajaMonto.getMes().getIdMes());
				cajaMontoSummaryVO.setNombreMes(cajaMonto.getMes().getNombre());
				cajaMontoSummaryVO.setMontoMes(cajaMonto.getMonto().getMonto());
				cajasMontosSummaryVO.add(cajaMontoSummaryVO);
			}
		}
		subtituloFlujoCajaVO.setCajaMontos(cajasMontosSummaryVO);
		subtituloFlujoCajaVO.setTransferenciaAcumulada(transferenciaAcumulada);
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
