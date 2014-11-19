package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EstadoSituacionProgramaVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -698992610606147835L;
	private String servicio;
	private Long marcoInicial;
	private Long marcoModificado;
	private Long convenioRecibido_monto;
	private Double convenioRecibido_porcentaje;
	private Long convenioPendiente_monto;
	private Double convenioPendiente_porcentaje;
	private Long remesaAcumulada_monto;
	private Double remesaAcumulada_porcentaje;
	private Long remesaPendiente_monto;
	private Double remesaPendiente_porcentaje;
	private Long reliquidacion_monto;
	private Double reliquidacion_porcentaje;
	private Long incremento;
	
	public EstadoSituacionProgramaVo(){
		super();
	}
	
	public EstadoSituacionProgramaVo(String servicio, Long marcoInicial, Long marcoModificado, Long convenioRecibido_monto,
			Double convenioRecibido_porcentaje, Long convenioPendiente_monto, Double convenioPendiente_porcentaje, 
			Long remesaAcumulada_monto, Double remesaAcumulada_porcentaje, Long remesaPendiente_monto, Double remesaPendiente_porcentaje,
			Long reliquidacion_monto, Double reliquidacion_porcentaje, Long incremento){
		
		super();
		this.servicio = servicio;
		this.marcoInicial = marcoInicial;
		this.marcoModificado = marcoModificado;
		this.convenioRecibido_monto = convenioRecibido_monto;
		this.convenioRecibido_porcentaje = convenioRecibido_porcentaje;
		this.convenioPendiente_monto = convenioPendiente_monto;
		this.convenioPendiente_porcentaje = convenioPendiente_porcentaje;
		this.remesaAcumulada_monto = remesaAcumulada_monto;
		this.remesaAcumulada_porcentaje = remesaAcumulada_porcentaje;
		this.remesaPendiente_monto = remesaPendiente_monto;
		this.remesaPendiente_porcentaje = remesaPendiente_porcentaje;
		this.reliquidacion_monto = reliquidacion_monto;
		this.reliquidacion_porcentaje = reliquidacion_porcentaje;
		this.incremento = incremento;
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public Long getMarcoInicial() {
		return marcoInicial;
	}

	public void setMarcoInicial(Long marcoInicial) {
		this.marcoInicial = marcoInicial;
	}

	public Long getMarcoModificado() {
		return marcoModificado;
	}

	public void setMarcoModificado(Long marcoModificado) {
		this.marcoModificado = marcoModificado;
	}

	public Long getConvenioRecibido_monto() {
		return convenioRecibido_monto;
	}

	public void setConvenioRecibido_monto(Long convenioRecibido_monto) {
		this.convenioRecibido_monto = convenioRecibido_monto;
	}

	public Double getConvenioRecibido_porcentaje() {
		return convenioRecibido_porcentaje;
	}

	public void setConvenioRecibido_porcentaje(Double convenioRecibido_porcentaje) {
		this.convenioRecibido_porcentaje = convenioRecibido_porcentaje;
	}

	public Long getConvenioPendiente_monto() {
		return convenioPendiente_monto;
	}

	public void setConvenioPendiente_monto(Long convenioPendiente_monto) {
		this.convenioPendiente_monto = convenioPendiente_monto;
	}

	public Double getConvenioPendiente_porcentaje() {
		return convenioPendiente_porcentaje;
	}

	public void setConvenioPendiente_porcentaje(Double convenioPendiente_porcentaje) {
		this.convenioPendiente_porcentaje = convenioPendiente_porcentaje;
	}

	public Long getRemesaAcumulada_monto() {
		return remesaAcumulada_monto;
	}

	public void setRemesaAcumulada_monto(Long remesaAcumulada_monto) {
		this.remesaAcumulada_monto = remesaAcumulada_monto;
	}

	public Double getRemesaAcumulada_porcentaje() {
		return remesaAcumulada_porcentaje;
	}

	public void setRemesaAcumulada_porcentaje(Double remesaAcumulada_porcentaje) {
		this.remesaAcumulada_porcentaje = remesaAcumulada_porcentaje;
	}

	public Long getRemesaPendiente_monto() {
		return remesaPendiente_monto;
	}

	public void setRemesaPendiente_monto(Long remesaPendiente_monto) {
		this.remesaPendiente_monto = remesaPendiente_monto;
	}

	public Double getRemesaPendiente_porcentaje() {
		return remesaPendiente_porcentaje;
	}

	public void setRemesaPendiente_porcentaje(Double remesaPendiente_porcentaje) {
		this.remesaPendiente_porcentaje = remesaPendiente_porcentaje;
	}

	public Long getReliquidacion_monto() {
		return reliquidacion_monto;
	}

	public void setReliquidacion_monto(Long reliquidacion_monto) {
		this.reliquidacion_monto = reliquidacion_monto;
	}

	public Double getReliquidacion_porcentaje() {
		return reliquidacion_porcentaje;
	}

	public void setReliquidacion_porcentaje(Double reliquidacion_porcentaje) {
		this.reliquidacion_porcentaje = reliquidacion_porcentaje;
	}

	public Long getIncremento() {
		return incremento;
	}

	public void setIncremento(Long incremento) {
		this.incremento = incremento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((convenioPendiente_monto == null) ? 0
						: convenioPendiente_monto.hashCode());
		result = prime
				* result
				+ ((convenioPendiente_porcentaje == null) ? 0
						: convenioPendiente_porcentaje.hashCode());
		result = prime
				* result
				+ ((convenioRecibido_monto == null) ? 0
						: convenioRecibido_monto.hashCode());
		result = prime
				* result
				+ ((convenioRecibido_porcentaje == null) ? 0
						: convenioRecibido_porcentaje.hashCode());
		result = prime * result
				+ ((incremento == null) ? 0 : incremento.hashCode());
		result = prime * result
				+ ((marcoInicial == null) ? 0 : marcoInicial.hashCode());
		result = prime * result
				+ ((marcoModificado == null) ? 0 : marcoModificado.hashCode());
		result = prime
				* result
				+ ((reliquidacion_monto == null) ? 0 : reliquidacion_monto
						.hashCode());
		result = prime
				* result
				+ ((reliquidacion_porcentaje == null) ? 0
						: reliquidacion_porcentaje.hashCode());
		result = prime
				* result
				+ ((remesaAcumulada_monto == null) ? 0 : remesaAcumulada_monto
						.hashCode());
		result = prime
				* result
				+ ((remesaAcumulada_porcentaje == null) ? 0
						: remesaAcumulada_porcentaje.hashCode());
		result = prime
				* result
				+ ((remesaPendiente_monto == null) ? 0 : remesaPendiente_monto
						.hashCode());
		result = prime
				* result
				+ ((remesaPendiente_porcentaje == null) ? 0
						: remesaPendiente_porcentaje.hashCode());
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
		EstadoSituacionProgramaVo other = (EstadoSituacionProgramaVo) obj;
		if (convenioPendiente_monto == null) {
			if (other.convenioPendiente_monto != null)
				return false;
		} else if (!convenioPendiente_monto
				.equals(other.convenioPendiente_monto))
			return false;
		if (convenioPendiente_porcentaje == null) {
			if (other.convenioPendiente_porcentaje != null)
				return false;
		} else if (!convenioPendiente_porcentaje
				.equals(other.convenioPendiente_porcentaje))
			return false;
		if (convenioRecibido_monto == null) {
			if (other.convenioRecibido_monto != null)
				return false;
		} else if (!convenioRecibido_monto.equals(other.convenioRecibido_monto))
			return false;
		if (convenioRecibido_porcentaje == null) {
			if (other.convenioRecibido_porcentaje != null)
				return false;
		} else if (!convenioRecibido_porcentaje
				.equals(other.convenioRecibido_porcentaje))
			return false;
		if (incremento == null) {
			if (other.incremento != null)
				return false;
		} else if (!incremento.equals(other.incremento))
			return false;
		if (marcoInicial == null) {
			if (other.marcoInicial != null)
				return false;
		} else if (!marcoInicial.equals(other.marcoInicial))
			return false;
		if (marcoModificado == null) {
			if (other.marcoModificado != null)
				return false;
		} else if (!marcoModificado.equals(other.marcoModificado))
			return false;
		if (reliquidacion_monto == null) {
			if (other.reliquidacion_monto != null)
				return false;
		} else if (!reliquidacion_monto.equals(other.reliquidacion_monto))
			return false;
		if (reliquidacion_porcentaje == null) {
			if (other.reliquidacion_porcentaje != null)
				return false;
		} else if (!reliquidacion_porcentaje
				.equals(other.reliquidacion_porcentaje))
			return false;
		if (remesaAcumulada_monto == null) {
			if (other.remesaAcumulada_monto != null)
				return false;
		} else if (!remesaAcumulada_monto.equals(other.remesaAcumulada_monto))
			return false;
		if (remesaAcumulada_porcentaje == null) {
			if (other.remesaAcumulada_porcentaje != null)
				return false;
		} else if (!remesaAcumulada_porcentaje
				.equals(other.remesaAcumulada_porcentaje))
			return false;
		if (remesaPendiente_monto == null) {
			if (other.remesaPendiente_monto != null)
				return false;
		} else if (!remesaPendiente_monto.equals(other.remesaPendiente_monto))
			return false;
		if (remesaPendiente_porcentaje == null) {
			if (other.remesaPendiente_porcentaje != null)
				return false;
		} else if (!remesaPendiente_porcentaje
				.equals(other.remesaPendiente_porcentaje))
			return false;
		if (servicio == null) {
			if (other.servicio != null)
				return false;
		} else if (!servicio.equals(other.servicio))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EstadoSituacionProgramaVo [servicio=" + servicio
				+ ", marcoInicial=" + marcoInicial + ", marcoModificado="
				+ marcoModificado + ", convenioRecibido_monto="
				+ convenioRecibido_monto + ", convenioRecibido_porcentaje="
				+ convenioRecibido_porcentaje + ", convenioPendiente_monto="
				+ convenioPendiente_monto + ", convenioPendiente_porcentaje="
				+ convenioPendiente_porcentaje + ", remesaAcumulada_monto="
				+ remesaAcumulada_monto + ", remesaAcumulada_porcentaje="
				+ remesaAcumulada_porcentaje + ", remesaPendiente_monto="
				+ remesaPendiente_monto + ", remesaPendiente_porcentaje="
				+ remesaPendiente_porcentaje + ", reliquidacion_monto="
				+ reliquidacion_monto + ", reliquidacion_porcentaje="
				+ reliquidacion_porcentaje + ", incremento=" + incremento + "]";
	}
	
	public List<Object> getRow() {
		List<Object> row = new ArrayList<Object>();
		
		if(getServicio() != null){
			row.add(getServicio());
		}
		if(getMarcoInicial() != null){
			row.add(getMarcoInicial());
		}
		if(getMarcoModificado() != null){
			row.add(getMarcoModificado());
		}
		if(getConvenioRecibido_monto() != null){
			row.add(getConvenioRecibido_monto());
		}
		if(getConvenioRecibido_porcentaje() != null){
			row.add(getConvenioRecibido_porcentaje());
		}
		if(getConvenioPendiente_monto() != null){
			row.add(getConvenioPendiente_monto());
		}
		if(getConvenioPendiente_porcentaje() != null){
			row.add(getConvenioPendiente_porcentaje());
		}
		if(getRemesaAcumulada_monto() != null){
			row.add(getRemesaAcumulada_monto());
		}
		if(getRemesaAcumulada_porcentaje() != null){
			row.add(getRemesaAcumulada_porcentaje());
		}
		if(getRemesaPendiente_monto() != null){
			row.add(getRemesaPendiente_monto());
		}
		if(getRemesaPendiente_porcentaje() != null){
			row.add(getRemesaPendiente_porcentaje());
		}
		if(getReliquidacion_monto() != null){
			row.add(getReliquidacion_monto());
		}
		if(getReliquidacion_porcentaje() != null){
			row.add(getReliquidacion_porcentaje());
		}
		if(getIncremento() != null){
			row.add(getIncremento());
		}
		
		return row;
	}
	
	
}
