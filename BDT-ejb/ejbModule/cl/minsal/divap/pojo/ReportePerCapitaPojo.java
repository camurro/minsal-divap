package cl.minsal.divap.pojo;

import java.util.Random;

public class ReportePerCapitaPojo {
	private Random rnd = new Random();
	private String region;
	private String servicio;
	private String comuna;
	private String clasificacion;
	
	private long valorPerCapita = rnd.nextInt(9999);
	private long poblacion = rnd.nextInt(999999);
	private long poblacionMayor = rnd.nextInt(9999);
	private long valorPoblacionMayor = valorPerCapita + 599;
	
	private long perCapita = valorPerCapita*poblacion+poblacionMayor*valorPoblacionMayor;
	private long desempenioDificil = rnd.nextInt(999999);
	private long aporteEstatal = rnd.nextInt(999999);
	private long rebajaIaaps = rnd.nextInt(999999);
	private long descuentoRetiro = rnd.nextInt(999999);
	
	private long aporteEstatalFinal = desempenioDificil-rebajaIaaps-descuentoRetiro;

	public Random getRnd() {
		return rnd;
	}

	public void setRnd( Random rnd ) {
		this.rnd = rnd;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion( String region ) {
		this.region = region;
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio( String servicio ) {
		this.servicio = servicio;
	}

	public String getComuna() {
		return comuna;
	}

	public void setComuna( String comuna ) {
		this.comuna = comuna;
	}

	public String getClasificacion() {
		return clasificacion;
	}

	public void setClasificacion( String clasificacion ) {
		this.clasificacion = clasificacion;
	}

	public long getValorPerCapita() {
		return valorPerCapita;
	}

	public void setValorPerCapita( long valorPerCapita ) {
		this.valorPerCapita = valorPerCapita;
	}

	public long getPoblacion() {
		return poblacion;
	}

	public void setPoblacion( long poblacion ) {
		this.poblacion = poblacion;
	}

	public long getPoblacionMayor() {
		return poblacionMayor;
	}

	public void setPoblacionMayor( long poblacionMayor ) {
		this.poblacionMayor = poblacionMayor;
	}

	public long getValorPoblacionMayor() {
		return valorPoblacionMayor;
	}

	public void setValorPoblacionMayor( long valorPoblacionMayor ) {
		this.valorPoblacionMayor = valorPoblacionMayor;
	}

	public long getPerCapita() {
		return perCapita;
	}

	public void setPerCapita( long perCapita ) {
		this.perCapita = perCapita;
	}

	public long getDesempenioDificil() {
		return desempenioDificil;
	}

	public void setDesempenioDificil( long desempenioDificil ) {
		this.desempenioDificil = desempenioDificil;
	}

	public long getAporteEstatal() {
		return aporteEstatal;
	}

	public void setAporteEstatal( long aporteEstatal ) {
		this.aporteEstatal = aporteEstatal;
	}

	public long getRebajaIaaps() {
		return rebajaIaaps;
	}

	public void setRebajaIaaps( long rebajaIaaps ) {
		this.rebajaIaaps = rebajaIaaps;
	}

	public long getDescuentoRetiro() {
		return descuentoRetiro;
	}

	public void setDescuentoRetiro( long descuentoRetiro ) {
		this.descuentoRetiro = descuentoRetiro;
	}

	public long getAporteEstatalFinal() {
		return aporteEstatalFinal;
	}

	public void setAporteEstatalFinal( long aporteEstatalFinal ) {
		this.aporteEstatalFinal = aporteEstatalFinal;
	}
	
	
}
