package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
	private List<ComponenteReliquidacionSummaryVO> componenteReliquidacionSummaryVO;
	private long Total;
	
	
	public ValorizarReliquidacionSummaryVO() {
		super();
		
	}
	
	public ValorizarReliquidacionSummaryVO(Integer idServicio, String servicio, Integer idComuna,
			String comuna, List<ComponenteReliquidacionSummaryVO> componenteReliquidacionSummaryVO, long Total) {
		super();
		
		this.idServicio = idServicio;
		this.servicio = servicio;
		this.idComuna = idComuna;
		this.comuna = comuna;
		this.componenteReliquidacionSummaryVO = componenteReliquidacionSummaryVO;
		this.componenteReliquidacionSummaryVO = componenteReliquidacionSummaryVO;
		this.Total = Total;	
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

	public List<ComponenteReliquidacionSummaryVO> getComponenteReliquidacionSummaryVO() {
		return componenteReliquidacionSummaryVO;
	}

	public void setComponenteReliquidacionSummaryVO(
			List<ComponenteReliquidacionSummaryVO> componenteReliquidacionSummaryVO) {
		this.componenteReliquidacionSummaryVO = componenteReliquidacionSummaryVO;
	}

	public long getTotal() {
		return Total;
	}

	public void setTotal(long total) {
		Total = total;
	}

	@Override
	public String toString() {
		return "ValorizarReliquidacionSummaryVO [idServicio=" + idServicio
				+ ", servicio=" + servicio + ", idComuna=" + idComuna
				+ ", comuna=" + comuna + ", componenteReliquidacionSummaryVO="
				+ componenteReliquidacionSummaryVO + ", Total=" + Total + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (Total ^ (Total >>> 32));
		result = prime
				* result
				+ ((componenteReliquidacionSummaryVO == null) ? 0
						: componenteReliquidacionSummaryVO.hashCode());
		result = prime * result + ((comuna == null) ? 0 : comuna.hashCode());
		result = prime * result
				+ ((idComuna == null) ? 0 : idComuna.hashCode());
		result = prime * result
				+ ((idServicio == null) ? 0 : idServicio.hashCode());
		result = prime * result
				+ ((servicio == null) ? 0 : servicio.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ValorizarReliquidacionSummaryVO other = (ValorizarReliquidacionSummaryVO) obj;
		if (Total != other.Total)
			return false;
		if (componenteReliquidacionSummaryVO == null) {
			if (other.componenteReliquidacionSummaryVO != null)
				return false;
		} else if (!componenteReliquidacionSummaryVO
				.equals(other.componenteReliquidacionSummaryVO))
			return false;
		if (comuna == null) {
			if (other.comuna != null)
				return false;
		} else if (!comuna.equals(other.comuna))
			return false;
		if (idComuna == null) {
			if (other.idComuna != null)
				return false;
		} else if (!idComuna.equals(other.idComuna))
			return false;
		if (idServicio == null) {
			if (other.idServicio != null)
				return false;
		} else if (!idServicio.equals(other.idServicio))
			return false;
		if (servicio == null) {
			if (other.servicio != null)
				return false;
		} else if (!servicio.equals(other.servicio))
			return false;
		return true;
	}

	public List<Object> getRow() {
		List<Object> row = new ArrayList<Object>();

//		private Integer idComuna;
//		private String comuna;
//		private List<CumplimientoApsMunicipalProgramaVO> cumplimientoApsMunicipalProgramaVO;
//		private long Total;
		
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
		for(ComponenteReliquidacionSummaryVO componenteReliquidacionSummaryVO : getComponenteReliquidacionSummaryVO()){
			row.add(componenteReliquidacionSummaryVO.getConvenio());
			row.add(componenteReliquidacionSummaryVO.getPorc_1cuota());
			row.add(componenteReliquidacionSummaryVO.getMonto_1cuota());
			row.add(componenteReliquidacionSummaryVO.getPorc_2cuota());
			row.add(componenteReliquidacionSummaryVO.getMonto_2cuota());
			row.add(componenteReliquidacionSummaryVO.getPorc_cumplimiento());
			row.add(componenteReliquidacionSummaryVO.getPorc_reliquidacion());
			row.add(componenteReliquidacionSummaryVO.getDescuento_2cuota());
			row.add(componenteReliquidacionSummaryVO.getMontoFinal_2cuota());
		}		
		row.add(getTotal());
		return row;
	}
}
