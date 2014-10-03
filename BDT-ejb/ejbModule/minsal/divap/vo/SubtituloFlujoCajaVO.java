package minsal.divap.vo;

import java.io.Serializable;
import java.util.List;


public class SubtituloFlujoCajaVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4939157865175247881L;

	private String servicio;
	private Integer idComponente;
	private String componente;
	private Integer marcoPresupuestario;
	private TransferenciaSummaryVO transferenciaAcumulada;
	private ConvenioSummaryVO convenioRecibido;
	private List<CajaMontoSummaryVO> cajaMontos;
	private Integer totalMontos;
	private String color = "#FFB5B5";

	public SubtituloFlujoCajaVO() {
		super();
	}

	public SubtituloFlujoCajaVO(String servicio, Integer idComponente,
			String componente, Integer marcoPresupuestario,
			TransferenciaSummaryVO transferenciaAcumulada,
			ConvenioSummaryVO convenioRecibido,
			List<CajaMontoSummaryVO> cajaMontos) {
		super();
		this.servicio = servicio;
		this.idComponente = idComponente;
		this.componente = componente;
		this.marcoPresupuestario = marcoPresupuestario;
		this.transferenciaAcumulada = transferenciaAcumulada;
		this.convenioRecibido = convenioRecibido;
		this.cajaMontos = cajaMontos;
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public Integer getIdComponente() {
		return idComponente;
	}

	public void setIdComponente(Integer idComponente) {
		this.idComponente = idComponente;
	}

	public String getComponente() {
		return componente;
	}

	public void setComponente(String componente) {
		this.componente = componente;
	}

	public Integer getMarcoPresupuestario() {
		return marcoPresupuestario;
	}

	public void setMarcoPresupuestario(Integer marcoPresupuestario) {
		this.marcoPresupuestario = marcoPresupuestario;
	}

	public TransferenciaSummaryVO getTransferenciaAcumulada() {
		return transferenciaAcumulada;
	}

	public void setTransferenciaAcumulada(
			TransferenciaSummaryVO transferenciaAcumulada) {
		this.transferenciaAcumulada = transferenciaAcumulada;
	}

	public ConvenioSummaryVO getConvenioRecibido() {
		return convenioRecibido;
	}

	public void setConvenioRecibido(ConvenioSummaryVO convenioRecibido) {
		this.convenioRecibido = convenioRecibido;
	}

	public List<CajaMontoSummaryVO> getCajaMontos() {
		return cajaMontos;
	}

	public void setCajaMontos(List<CajaMontoSummaryVO> cajaMontos) {
		this.cajaMontos = cajaMontos;
	}

	public Integer getTotalMontos() {
		totalMontos = 0;
		if(cajaMontos != null && cajaMontos.size() > 0){
			for(CajaMontoSummaryVO monto : cajaMontos){
				totalMontos += monto.getMontoMes();
			}
		}
		return totalMontos;
	}

	public void setTotalMontos(Integer totalMontos) {
		this.totalMontos = totalMontos;
	}

	public String getColor() {
		System.out.println("getColor-- marcoPresupuestario->"+marcoPresupuestario+" getTotalMontos()="+getTotalMontos());
		if (!this.marcoPresupuestario.equals(getTotalMontos())){
			System.out.println("no son iguales");
			this.color = "#FF0000";
		}else{
			System.out.println("son iguales");
			this.color = "#3ADF00";
		}
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return "SubtituloFlujoCajaVO [servicio=" + servicio + ", idComponente="
				+ idComponente + ", componente=" + componente
				+ ", marcoPresupuestario=" + marcoPresupuestario
				+ ", transferenciaAcumulada=" + transferenciaAcumulada
				+ ", convenioRecibido=" + convenioRecibido + ", cajaMontos="
				+ cajaMontos + "]";
	}

}

