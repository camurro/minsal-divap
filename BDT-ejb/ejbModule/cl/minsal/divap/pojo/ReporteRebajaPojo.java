package cl.minsal.divap.pojo;

import java.util.Random;

public class ReporteRebajaPojo {
	private String region;
	private String servicio;
	private String comuna;
	
	private Random rnd = new Random();
	
	private long montoRebaja1 = rnd.nextInt(9999999);
	private float porcentajeRebaja1 = rnd.nextFloat();
	
	private long montoRebaja2 = rnd.nextInt(9999999);
	private float porcentajeRebaja2 = rnd.nextFloat();
	
	private long montoRebaja3 = rnd.nextInt(9999999);
	private float porcentajeRebaja3 = rnd.nextFloat();
	
	private long montoRebaja4 = rnd.nextInt(9999999);
	private float porcentajeRebaja4 = rnd.nextFloat();
	
	private long rebajaAplicada = montoRebaja1+montoRebaja2+montoRebaja3+montoRebaja4;
	
	
	
	public long getRebajaAplicada() {
		return rebajaAplicada;
	}
	public void setRebajaAplicada( long rebajaAplicada ) {
		this.rebajaAplicada = rebajaAplicada;
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
	public long getMontoRebaja1() {
		return montoRebaja1;
	}
	public void setMontoRebaja1( long montoRebaja1 ) {
		this.montoRebaja1 = montoRebaja1;
	}
	public float getPorcentajeRebaja1() {
		return porcentajeRebaja1;
	}
	public void setPorcentajeRebaja1( float porcentajeRebaja1 ) {
		this.porcentajeRebaja1 = porcentajeRebaja1;
	}
	public long getMontoRebaja2() {
		return montoRebaja2;
	}
	public void setMontoRebaja2( long montoRebaja2 ) {
		this.montoRebaja2 = montoRebaja2;
	}
	public float getPorcentajeRebaja2() {
		return porcentajeRebaja2;
	}
	public void setPorcentajeRebaja2( float porcentajeRebaja2 ) {
		this.porcentajeRebaja2 = porcentajeRebaja2;
	}
	public long getMontoRebaja3() {
		return montoRebaja3;
	}
	public void setMontoRebaja3( long montoRebaja3 ) {
		this.montoRebaja3 = montoRebaja3;
	}
	public float getPorcentajeRebaja3() {
		return porcentajeRebaja3;
	}
	public void setPorcentajeRebaja3( float porcentajeRebaja3 ) {
		this.porcentajeRebaja3 = porcentajeRebaja3;
	}
	public long getMontoRebaja4() {
		return montoRebaja4;
	}
	public void setMontoRebaja4( long montoRebaja4 ) {
		this.montoRebaja4 = montoRebaja4;
	}
	public float getPorcentajeRebaja4() {
		return porcentajeRebaja4;
	}
	public void setPorcentajeRebaja4( float porcentajeRebaja4 ) {
		this.porcentajeRebaja4 = porcentajeRebaja4;
	}
	
	
}
