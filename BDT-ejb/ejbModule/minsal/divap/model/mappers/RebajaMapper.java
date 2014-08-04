package minsal.divap.model.mappers;

import minsal.divap.vo.CumplimientoSummaryVO;
import minsal.divap.vo.CumplimientoVO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import cl.minsal.divap.model.ComunaCumplimiento;

public class RebajaMapper implements Mapper<ComunaCumplimiento>{

	@Override
	public CumplimientoSummaryVO getSummary(ComunaCumplimiento comunaCumplimiento) {
		if (comunaCumplimiento == null)
		      return null;
		CumplimientoSummaryVO cumplimientoSummary = new CumplimientoSummaryVO();
		cumplimientoSummary.setIdCumplimiento(comunaCumplimiento.getIdComunaCumplimiento());
		cumplimientoSummary.setValor(comunaCumplimiento.getValor());
		return cumplimientoSummary;
	}

	@Override
	public CumplimientoVO getBasic(ComunaCumplimiento comunaCumplimiento) {
		if (comunaCumplimiento == null)
		      return null;
		throw new NotImplementedException();
	
	}

	@Override
	public Object getData(ComunaCumplimiento comunaCumplimiento) {
		throw new NotImplementedException();
	}

}
