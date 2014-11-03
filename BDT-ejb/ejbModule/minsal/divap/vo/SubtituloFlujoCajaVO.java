package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SubtituloFlujoCajaVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4939157865175247881L;

	private Integer idServicio;
	private String servicio;
	private Integer idSubtitulo;
	private String subtitulo;
	private Integer idMarcoPresupuestario;
	private Long marcoPresupuestario;
	private TransferenciaSummaryVO transferenciaAcumulada;
	private ConveniosSummaryVO convenioRecibido;
	private List<CajaMontoSummaryVO> cajaMontos;
	private Long totalMontos;
	private String color = "#FFB5B5";
	private boolean ignoreColor;

	public SubtituloFlujoCajaVO() {
		super();
		this.cajaMontos = new ArrayList<CajaMontoSummaryVO>(Arrays.asList(new CajaMontoSummaryVO(), new CajaMontoSummaryVO(), new CajaMontoSummaryVO(), new CajaMontoSummaryVO(), 
				 new CajaMontoSummaryVO(), new CajaMontoSummaryVO(), new CajaMontoSummaryVO(), new CajaMontoSummaryVO(), new CajaMontoSummaryVO(), new CajaMontoSummaryVO(), 
				 new CajaMontoSummaryVO(), new CajaMontoSummaryVO()));
		this.ignoreColor = true;
		this.color = "#3ADF00";
	}

	
	public SubtituloFlujoCajaVO(Integer idServicio, String servicio,
			Integer idSubtitulo, String subtitulo,
			Integer idMarcoPresupuestario, Long marcoPresupuestario,
			TransferenciaSummaryVO transferenciaAcumulada,
			ConveniosSummaryVO convenioRecibido,
			List<CajaMontoSummaryVO> cajaMontos, Long totalMontos,
			String color) {
		super();
		this.idServicio = idServicio;
		this.servicio = servicio;
		this.idSubtitulo = idSubtitulo;
		this.subtitulo = subtitulo;
		this.idMarcoPresupuestario = idMarcoPresupuestario;
		this.marcoPresupuestario = marcoPresupuestario;
		this.transferenciaAcumulada = transferenciaAcumulada;
		this.convenioRecibido = convenioRecibido;
		this.cajaMontos = cajaMontos;
		this.totalMontos = totalMontos;
		this.color = color;
		this.ignoreColor = true;
		this.color = "#3ADF00";
	}


	public Long getMarcoPresupuestario() {
		return marcoPresupuestario;
	}

	public void setMarcoPresupuestario(Long marcoPresupuestario) {
		this.marcoPresupuestario = marcoPresupuestario;
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public Integer getIdSubtitulo() {
		return idSubtitulo;
	}

	public void setIdSubtitulo(Integer idSubtitulo) {
		this.idSubtitulo = idSubtitulo;
	}

	public String getSubtitulo() {
		return subtitulo;
	}

	public void setSubtitulo(String subtitulo) {
		this.subtitulo = subtitulo;
	}

	public TransferenciaSummaryVO getTransferenciaAcumulada() {
		return transferenciaAcumulada;
	}

	public void setTransferenciaAcumulada(
			TransferenciaSummaryVO transferenciaAcumulada) {
		this.transferenciaAcumulada = transferenciaAcumulada;
	}

	public ConveniosSummaryVO getConvenioRecibido() {
		return convenioRecibido;
	}

	public void setConvenioRecibido(ConveniosSummaryVO convenioRecibido) {
		this.convenioRecibido = convenioRecibido;
	}

	public List<CajaMontoSummaryVO> getCajaMontos() {
		return cajaMontos;
	}

	public void setCajaMontos(List<CajaMontoSummaryVO> cajaMontos) {
		this.cajaMontos = cajaMontos;
	}

	public Long getTotalMontos() {
		totalMontos = 0L;
		if(cajaMontos != null && cajaMontos.size() > 0){
			for(CajaMontoSummaryVO monto : cajaMontos){
				totalMontos += monto.getMontoMes();
			}
		}
		return totalMontos;
	}

	public void setTotalMontos(Long totalMontos) {
		this.totalMontos = totalMontos;
	}
	
	public boolean marcoCuadrado(){
		if(!this.marcoPresupuestario.equals(getTotalMontos())){
			return false;
		}
		return true;
	}
	
	public String getColor() {
		System.out.println("getColor-- marcoPresupuestario->"+marcoPresupuestario+" getTotalMontos()="+getTotalMontos());
		if(!isIgnoreColor()){
			if (!marcoCuadrado()){
				System.out.println("no son iguales");
				this.color = "#FF0000";
			}else{
				System.out.println("son iguales");
				this.color = "#3ADF00";
			}
		}
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Integer getIdServicio() {
		return idServicio;
	}


	public void setIdServicio(Integer idServicio) {
		this.idServicio = idServicio;
	}


	public Integer getIdMarcoPresupuestario() {
		return idMarcoPresupuestario;
	}


	public void setIdMarcoPresupuestario(Integer idMarcoPresupuestario) {
		this.idMarcoPresupuestario = idMarcoPresupuestario;
	}

	public boolean isIgnoreColor() {
		return ignoreColor;
	}


	public void setIgnoreColor(boolean ignoreColor) {
		this.ignoreColor = ignoreColor;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((idMarcoPresupuestario == null) ? 0 : idMarcoPresupuestario
						.hashCode());
		result = prime * result
				+ ((idServicio == null) ? 0 : idServicio.hashCode());
		result = prime * result
				+ ((idSubtitulo == null) ? 0 : idSubtitulo.hashCode());
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
		SubtituloFlujoCajaVO other = (SubtituloFlujoCajaVO) obj;
		if (idMarcoPresupuestario == null) {
			if (other.idMarcoPresupuestario != null)
				return false;
		} else if (!idMarcoPresupuestario.equals(other.idMarcoPresupuestario))
			return false;
		if (idServicio == null) {
			if (other.idServicio != null)
				return false;
		} else if (!idServicio.equals(other.idServicio))
			return false;
		if (idSubtitulo == null) {
			if (other.idSubtitulo != null)
				return false;
		} else if (!idSubtitulo.equals(other.idSubtitulo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SubtituloFlujoCajaVO [idServicio=" + idServicio + ", servicio="
				+ servicio + ", idSubtitulo=" + idSubtitulo + ", subtitulo="
				+ subtitulo + ", idMarcoPresupuestario="
				+ idMarcoPresupuestario + ", marcoPresupuestario="
				+ marcoPresupuestario + ", transferenciaAcumulada="
				+ transferenciaAcumulada + ", convenioRecibido="
				+ convenioRecibido + ", cajaMontos=" + cajaMontos
				+ ", totalMontos=" + totalMontos + ", color=" + color + "]";
	}


	public List<Object> getRow() {
		List<Object> row = new ArrayList<Object>();
		if(getServicio() != null){
			row.add(getServicio());			
		}
		for(CajaMontoSummaryVO cajaMontoSummaryVO : getCajaMontos()){
			row.add(cajaMontoSummaryVO.getMontoMes());	
		}
		row.add(getTotalMontos());	
		return row;
	}

}

