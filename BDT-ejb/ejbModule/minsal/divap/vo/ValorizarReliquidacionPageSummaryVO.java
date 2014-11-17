package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ValorizarReliquidacionPageSummaryVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 171040417440221727L;

	private Integer idServicio;
	private String servicio;
	private Integer idComuna;
	private String comuna;
	private ComponenteReliquidacionPageVO componenteReliquidacionPageVO;
	private Long marcoFinal;

	public ValorizarReliquidacionPageSummaryVO() {
		super();

	}

	public ValorizarReliquidacionPageSummaryVO(Integer idServicio, String servicio,
			Integer idComuna, String comuna, ComponenteReliquidacionPageVO componenteReliquidacionPageVO,
			Long marcoFinal) {
		super();
		this.idServicio = idServicio;
		this.servicio = servicio;
		this.idComuna = idComuna;
		this.comuna = comuna;
		this.componenteReliquidacionPageVO = componenteReliquidacionPageVO;
		this.marcoFinal = marcoFinal;
	}

	public ComponenteReliquidacionPageVO getComponenteReliquidacionPageVO() {
		return componenteReliquidacionPageVO;
	}

	public void setComponenteReliquidacionPageVO(
			ComponenteReliquidacionPageVO componenteReliquidacionPageVO) {
		this.componenteReliquidacionPageVO = componenteReliquidacionPageVO;
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

		if(getComponenteReliquidacionPageVO() != null){
			if(getComponenteReliquidacionPageVO().getMarcoInicial() != null){
				row.add(getComponenteReliquidacionPageVO().getMarcoInicial());
			}
			if(getComponenteReliquidacionPageVO().getCuotasUno() != null){
				row.add(getComponenteReliquidacionPageVO().getCuotasUno().getPorcentaje());
				row.add(getComponenteReliquidacionPageVO().getCuotasUno().getMonto());
			}
			if(getComponenteReliquidacionPageVO().getCuotasDos() != null){
				row.add(getComponenteReliquidacionPageVO().getCuotasDos().getPorcentaje());
				row.add(getComponenteReliquidacionPageVO().getCuotasDos().getMonto());
			}
			if(getComponenteReliquidacionPageVO().getCuotasTres() != null){
				row.add(getComponenteReliquidacionPageVO().getCuotasTres().getPorcentaje());
				row.add(getComponenteReliquidacionPageVO().getCuotasTres().getMonto());
			}
			if(getComponenteReliquidacionPageVO().getCuotasCuatro() != null){
				row.add(getComponenteReliquidacionPageVO().getCuotasCuatro().getPorcentaje());
				row.add(getComponenteReliquidacionPageVO().getCuotasCuatro().getMonto());
			}
			if(getComponenteReliquidacionPageVO().getCuotasCinco() != null){
				row.add(getComponenteReliquidacionPageVO().getCuotasCinco().getPorcentaje());
				row.add(getComponenteReliquidacionPageVO().getCuotasCinco().getMonto());
			}
			if(getComponenteReliquidacionPageVO().getCuotasSeis() != null){
				row.add(getComponenteReliquidacionPageVO().getCuotasSeis().getPorcentaje());
				row.add(getComponenteReliquidacionPageVO().getCuotasSeis().getMonto());
			}
			if(getComponenteReliquidacionPageVO().getCuotasSiete() != null){
				row.add(getComponenteReliquidacionPageVO().getCuotasSiete().getPorcentaje());
				row.add(getComponenteReliquidacionPageVO().getCuotasSiete().getMonto());
			}
			if(getComponenteReliquidacionPageVO().getCuotasOcho()!= null){
				row.add(getComponenteReliquidacionPageVO().getCuotasOcho().getPorcentaje());
				row.add(getComponenteReliquidacionPageVO().getCuotasOcho().getMonto());
			}
			if(getComponenteReliquidacionPageVO().getCuotasNueve() != null){
				row.add(getComponenteReliquidacionPageVO().getCuotasNueve().getPorcentaje());
				row.add(getComponenteReliquidacionPageVO().getCuotasNueve().getMonto());
			}
			if(getComponenteReliquidacionPageVO().getCuotasDiez() != null){
				row.add(getComponenteReliquidacionPageVO().getCuotasDiez().getPorcentaje());
				row.add(getComponenteReliquidacionPageVO().getCuotasDiez().getMonto());
			}
			if(getComponenteReliquidacionPageVO().getCuotasOnce() != null){
				row.add(getComponenteReliquidacionPageVO().getCuotasOnce().getPorcentaje());
				row.add(getComponenteReliquidacionPageVO().getCuotasOnce().getMonto());
			}
			if(getComponenteReliquidacionPageVO().getCuotasDoce() != null){
				row.add(getComponenteReliquidacionPageVO().getCuotasDoce().getPorcentaje());
				row.add(getComponenteReliquidacionPageVO().getCuotasDoce().getMonto());
			}
			if(getComponenteReliquidacionPageVO().getPorcentajeCumplimiento() != null){
				row.add(getComponenteReliquidacionPageVO().getPorcentajeCumplimiento());
			}

			if(getComponenteReliquidacionPageVO().getPorcentajeReliquidacion() != null){
				row.add(getComponenteReliquidacionPageVO().getPorcentajeReliquidacion());
			}

			if(getComponenteReliquidacionPageVO().getRebajaUltimaCuota() != null){
				row.add(getComponenteReliquidacionPageVO().getRebajaUltimaCuota());
			}

			if(getComponenteReliquidacionPageVO().getMontoUltimaCuota()!= null){
				row.add(getComponenteReliquidacionPageVO().getMontoUltimaCuota());
			}
		}
		if(getMarcoFinal()!= null){
			row.add(getMarcoFinal());
		}
		return row;
	}

	@Override
	public String toString() {
		return "ValorizarReliquidacionSummaryVO [idServicio=" + idServicio
				+ ", servicio=" + servicio + ", idComuna=" + idComuna
				+ ", comuna=" + comuna + ", componenteReliquidacionPageVO="
				+ componenteReliquidacionPageVO + ", marcoFinal=" + marcoFinal + "]";
	}
}
