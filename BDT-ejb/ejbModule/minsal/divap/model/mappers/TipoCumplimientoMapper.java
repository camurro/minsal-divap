package minsal.divap.model.mappers;

import minsal.divap.vo.TipoCumplimientoVO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import cl.minsal.divap.model.TipoCumplimiento;

public class TipoCumplimientoMapper implements Mapper<TipoCumplimiento>{

	@Override
	public Object getSummary(TipoCumplimiento tipoCumplimiento) {
		throw new NotImplementedException();
	}

	@Override
	public TipoCumplimientoVO getBasic(TipoCumplimiento tipoCumplimiento) {
		if (tipoCumplimiento == null)
		      return null;
		return new TipoCumplimientoVO(tipoCumplimiento.getIdTipoCumplimiento(), tipoCumplimiento.getDescripcion());
	}

	@Override
	public Object getData(TipoCumplimiento tipoCumplimiento) {
		throw new NotImplementedException();
	}

}
