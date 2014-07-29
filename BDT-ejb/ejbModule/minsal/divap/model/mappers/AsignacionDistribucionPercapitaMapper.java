package minsal.divap.model.mappers;

import minsal.divap.vo.AsignacionDistribucionPerCapitaVO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import cl.minsal.divap.model.AntecendentesComuna;
import cl.minsal.divap.model.AntecendentesComunaCalculado;

public class AsignacionDistribucionPercapitaMapper implements Mapper<AntecendentesComunaCalculado>{

	@Override
	public Object getSummary(AntecendentesComunaCalculado antecendentesComunaCalculado) {
		throw new NotImplementedException();
	}

	@Override
	public AsignacionDistribucionPerCapitaVO getBasic(AntecendentesComunaCalculado antecendentesComunaCalculado) {
		if (antecendentesComunaCalculado == null)
		      return null;
		AntecendentesComuna antecendentesComuna = antecendentesComunaCalculado.getAntecedentesComuna();
		AsignacionDistribucionPerCapitaVO asignacionDistribucionPerCapitaVO = new AsignacionDistribucionPerCapitaVO(antecendentesComuna.getIdComuna().getServicioSalud().getRegion().getId(), antecendentesComuna.getIdComuna().getServicioSalud().getNombre(),  antecendentesComuna.getIdComuna().getNombre(), antecendentesComunaCalculado.getPoblacion(), antecendentesComunaCalculado.getPoblacionMayor());
		if(antecendentesComuna.getClasificacion() != null){
			asignacionDistribucionPerCapitaVO.setClasificacion(antecendentesComuna.getClasificacion().getDescripcion());
		}else{
			asignacionDistribucionPerCapitaVO.setClasificacion("");
		}
		if(antecendentesComuna.getAsignacionZona() != null){
			asignacionDistribucionPerCapitaVO.setRefAsignacionZona(antecendentesComuna.getAsignacionZona().getValor());
		}else{
			asignacionDistribucionPerCapitaVO.setRefAsignacionZona(0.0);
		}
		if(antecendentesComuna.getTramoPobreza() != null){
			asignacionDistribucionPerCapitaVO.setTramoPobreza(antecendentesComuna.getTramoPobreza().getValor());
		}else{
			asignacionDistribucionPerCapitaVO.setTramoPobreza(0.0);
		}
		if(antecendentesComuna.getAnoAnoEnCurso() != null){
			asignacionDistribucionPerCapitaVO.setPercapitaBasal(antecendentesComuna.getAnoAnoEnCurso().getMontoPercapitalBasal());
		}else{
			asignacionDistribucionPerCapitaVO.setPercapitaBasal(0);
		}
		asignacionDistribucionPerCapitaVO.setPobreza(antecendentesComunaCalculado.getPobreza());
		asignacionDistribucionPerCapitaVO.setRuralidad(antecendentesComunaCalculado.getRuralidad());
		asignacionDistribucionPerCapitaVO.setValorRefAsignacionZona(antecendentesComunaCalculado.getValorReferencialZona());
		asignacionDistribucionPerCapitaVO.setValorPercapitaMes(antecendentesComunaCalculado.getPercapitaMes());
		asignacionDistribucionPerCapitaVO.setValorPercapitaAno(antecendentesComunaCalculado.getPercapitaAno());
		return asignacionDistribucionPerCapitaVO;
	}

	@Override
	public Object getData(AntecendentesComunaCalculado antecendentesComunaCalculado) {
		throw new NotImplementedException();
	}

}
