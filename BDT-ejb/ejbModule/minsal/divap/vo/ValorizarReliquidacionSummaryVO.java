package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ValorizarReliquidacionSummaryVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 171040417440221727L;

	private Integer idServicio;
	private String servicio;
	private Integer idComuna;
	private String comuna;
	
	private List<ComponenteReliquidacionVO> componentesReliquidacion;
	private Long marcoFinal;

	public ValorizarReliquidacionSummaryVO() {
		super();

	}

	public ValorizarReliquidacionSummaryVO(Integer idServicio, String servicio,
			Integer idComuna, String comuna,List<ComponenteReliquidacionVO> componentesReliquidacion,
			Long marcoFinal) {
		super();
		this.idServicio = idServicio;
		this.servicio = servicio;
		this.idComuna = idComuna;
		this.comuna = comuna;
		this.componentesReliquidacion = componentesReliquidacion;
		this.marcoFinal = marcoFinal;
	}

	public List<ComponenteReliquidacionVO> getComponentesReliquidacion() {
		return componentesReliquidacion;
	}

	public void setComponentesReliquidacion(
			List<ComponenteReliquidacionVO> componentesReliquidacion) {
		this.componentesReliquidacion = componentesReliquidacion;
	}

	public Integer getIdServicio() {
		return idServicio;
	}

	public void setIdServicio(Integer idServicio) {
		this.idServicio = idServicio;
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public Integer getIdComuna() {
		return idComuna;
	}

	public void setIdComuna(Integer idComuna) {
		this.idComuna = idComuna;
	}

	public String getComuna() {
		return comuna;
	}

	public void setComuna(String comuna) {
		this.comuna = comuna;
	}

	public Long getMarcoFinal() {
		return marcoFinal;
	}

	public void setMarcoFinal(Long marcoFinal) {
		this.marcoFinal = marcoFinal;
	}

	public List<Object> getRow() {
		List<Object> row = new ArrayList<Object>();

		if(getIdServicio()!= null){
			row.add(getIdServicio());
		}
		if(getServicio()!=null){
			row.add(getServicio());
		}
		if(getIdComuna() != null){
			row.add(getIdComuna());
		}
		if(getComuna() != null){
			row.add(getComuna());
		}
		if(getComponentesReliquidacion() != null && getComponentesReliquidacion().size() > 0){
			for(ComponenteReliquidacionVO componenteReliquidacionVO : getComponentesReliquidacion()){
				if(componenteReliquidacionVO.getNumeroResolucion() != null){
					row.add(componenteReliquidacionVO.getNumeroResolucion());
				}
				if(componenteReliquidacionVO.getCuotasSummaryVO() != null && componenteReliquidacionVO.getCuotasSummaryVO().size() > 0){
					for(CuotaSummaryVO cuotaSummaryVO : componenteReliquidacionVO.getCuotasSummaryVO()){
						row.add(cuotaSummaryVO.getPorcentaje());
						row.add(cuotaSummaryVO.getMonto());
					}
				}
				if(componenteReliquidacionVO.getPorcentajeCumplimiento() != null){
					row.add(componenteReliquidacionVO.getPorcentajeCumplimiento());
				}

				if(componenteReliquidacionVO.getPorcentajeReliquidacion() != null){
					row.add(componenteReliquidacionVO.getPorcentajeReliquidacion());
				}

				if(componenteReliquidacionVO.getRebajaUltimaCuota() != null){
					row.add(componenteReliquidacionVO.getRebajaUltimaCuota());
				}

				if(componenteReliquidacionVO.getMontoUltimaCuota()!= null){
					row.add(componenteReliquidacionVO.getMontoUltimaCuota());
				}
			}
		}
		if(getMarcoFinal()!= null){
			row.add(getMarcoFinal());
		}
		return row;
	}
}
