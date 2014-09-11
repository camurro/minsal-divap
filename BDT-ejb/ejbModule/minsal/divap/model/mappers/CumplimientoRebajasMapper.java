package minsal.divap.model.mappers;

import minsal.divap.vo.CumplimientoRebajaVO;
import minsal.divap.vo.MesVO;
import minsal.divap.vo.TipoCumplimientoVO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import cl.minsal.divap.model.ComunaCumplimiento;

public class CumplimientoRebajasMapper implements Mapper<ComunaCumplimiento>{

	@Override
	public Object getSummary(ComunaCumplimiento comunaCumplimiento) {
		throw new NotImplementedException();
	}

	@Override
	public CumplimientoRebajaVO getBasic(ComunaCumplimiento comunaCumplimiento) {
		if (comunaCumplimiento == null)
			return null;
		CumplimientoRebajaVO cumplimientoRebajaVO = new CumplimientoRebajaVO();
		cumplimientoRebajaVO.setIdCumplimiento(comunaCumplimiento.getIdComunaCumplimiento());
		if(comunaCumplimiento.getIdTipoCumplimiento() != null){
			cumplimientoRebajaVO.setTipoCumplimiento(new TipoCumplimientoVO(comunaCumplimiento.getIdTipoCumplimiento().getIdTipoCumplimiento(), comunaCumplimiento.getIdTipoCumplimiento().getDescripcion()));
		}
		if(comunaCumplimiento.getRebajaCalculada() != null){
			cumplimientoRebajaVO.setRebajaCalculada(comunaCumplimiento.getRebajaCalculada().getRebaja());
		}else{
			cumplimientoRebajaVO.setRebajaCalculada(new Double(0));
		}
		if(comunaCumplimiento.getRebajaFinal() != null){
			cumplimientoRebajaVO.setRebajaFinal(comunaCumplimiento.getRebajaFinal().getRebaja());
		}else{
			cumplimientoRebajaVO.setRebajaFinal(new Double(0));
		}
		if(comunaCumplimiento.getValor() != null){
			cumplimientoRebajaVO.setValor(comunaCumplimiento.getValor());
		}else{
			cumplimientoRebajaVO.setValor(new Double(0));
		}
		if(comunaCumplimiento.getIdMes() != null){
			MesVO mes = new MesVO();
			mes.setId(comunaCumplimiento.getIdMes().getIdMes());
			mes.setNombre(comunaCumplimiento.getIdMes().getNombre());
			cumplimientoRebajaVO.setMes(mes);
		} 
		return cumplimientoRebajaVO;
	}

	@Override
	public Object getData(ComunaCumplimiento comunaCumplimiento) {
		throw new NotImplementedException();
	}

}
