package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CalculoReliquidacionBaseVO extends ReliquidacionBaseVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1686888207704025227L;
	
	private List<ComponenteCumplimientoVO> componentesCumplimientoVO;
	
	public CalculoReliquidacionBaseVO(){
		
	}
	
	public CalculoReliquidacionBaseVO(Integer id_servicio, String servicio, Integer id_comuna, String comuna){
		super(id_servicio, servicio, id_comuna, comuna);
		this.componentesCumplimientoVO = new ArrayList<ComponenteCumplimientoVO>();
	}

	public List<ComponenteCumplimientoVO> getComponentesCumplimientoVO() {
		return componentesCumplimientoVO;
	}

	public void setComponentesCumplimientoVO(
			List<ComponenteCumplimientoVO> componentesCumplimientoVO) {
		this.componentesCumplimientoVO = componentesCumplimientoVO;
	}

	@Override
	public String toString() {
		return "CalculoReliquidacionBaseVO [componentesCumplimientoVO="
				+ componentesCumplimientoVO + "]";
	}
	
}
