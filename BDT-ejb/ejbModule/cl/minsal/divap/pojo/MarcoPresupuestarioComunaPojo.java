package cl.minsal.divap.pojo;

public class MarcoPresupuestarioComunaPojo {
	private String servicio;
	private String comuna;
	private String programa;
	private long marco2014;
	private long convenio2014;
	private float porcentajeConvenio2014;
	private long remesas2014;
	private float porcentajeRemesa2014;
	private String observacion;
	private long convenioPendiente;
	
	
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
	public float getPorcentajeConvenio2014() {
		return porcentajeConvenio2014;
	}
	public void setPorcentajeConvenio2014( float porcentajeConvenio2014 ) {
		this.porcentajeConvenio2014 = porcentajeConvenio2014;
	}
	public long getConvenioPendiente() {
		return convenioPendiente;
	}
	public void setConvenioPendiente( long convenioPendiente ) {
		this.convenioPendiente = convenioPendiente;
	}
	public String getPrograma() {
		return programa;
	}
	public void setPrograma( String programa ) {
		this.programa = programa;
	}
	public long getMarco2014() {
		return marco2014;
	}
	public void setMarco2014( long marco2014 ) {
		this.marco2014 = marco2014;
	}
	public long getConvenio2014() {
		return convenio2014;
	}
	public void setConvenio2014( long convenio2014 ) {
		this.convenio2014 = convenio2014;
	}
	public long getRemesas2014() {
		return remesas2014;
	}
	public void setRemesas2014( long remesas2014 ) {
		this.remesas2014 = remesas2014;
	}
	public float getPorcentajeRemesa2014() {
		return porcentajeRemesa2014;
	}
	public void setPorcentajeRemesa2014( float porcentajeRemesa2014 ) {
		this.porcentajeRemesa2014 = porcentajeRemesa2014;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion( String observacion ) {
		this.observacion = observacion;
	}

	
}
