package cl.minsal.divap.pojo;

import java.util.Random;

public class MonitoreoPojo {
	Random rnd = new Random();
	private String establecimiento;
	private String servicio;
	private String comuna;
	private ComponentePojo componente;
	
	public ComponentePojo getComponente() {
		return componente;
	}
	
	public void setComponente( ComponentePojo componente ) {
		this.componente = componente;
	}
	
	public String getComuna() {
		return comuna;
	}
	
	public void setComuna( String comuna ) {
		this.comuna = comuna;
	}
	
	public String getServicio() {
		return servicio;
	}
	
	public void setServicio( String servicio ) {
		this.servicio = servicio;
	}
	
	private long s24Enero = rnd.nextInt(99999999);
	private long s24Febrero = rnd.nextInt(99999999);
	private long s24Marzo = rnd.nextInt(99999999);
	private long s24Abril = rnd.nextInt(99999999);
	private long s24Mayo = rnd.nextInt(99999999);
	private long s24Junio = rnd.nextInt(99999999);
	private long s24Julio = rnd.nextInt(99999999);
	private long s24Agosto = rnd.nextInt(99999999);
	private long s24Septiembre = rnd.nextInt(99999999);
	private long s24Octubre = rnd.nextInt(99999999);
	private long s24Noviembre = rnd.nextInt(99999999);
	private long s24Diciembre = rnd.nextInt(99999999);
	private long s24Total = s24Enero + s24Febrero + s24Marzo + s24Abril + s24Mayo + s24Junio + s24Julio + s24Agosto + s24Septiembre
					+ s24Octubre + s24Noviembre + s24Diciembre;
	
	
	
	public long getS24Total() {
		return s24Total;
	}

	public void setS24Total( long s24Total ) {
		this.s24Total = s24Total;
	}

	private long s21Enero = rnd.nextInt(99999999);
	private long s21Febrero = rnd.nextInt(99999999);
	private long s21Marzo = rnd.nextInt(99999999);
	private long s21Abril = rnd.nextInt(99999999);
	private long s21Mayo = rnd.nextInt(99999999);
	private long s21Junio = rnd.nextInt(99999999);
	private long s21Julio = rnd.nextInt(99999999);
	private long s21Agosto = rnd.nextInt(99999999);
	private long s21Septiembre = rnd.nextInt(99999999);
	private long s21Octubre = rnd.nextInt(99999999);
	private long s21Noviembre = rnd.nextInt(99999999);
	private long s21Diciembre = rnd.nextInt(99999999);
	
	private long s22Enero = rnd.nextInt(99999999);
	private long s22Febrero = rnd.nextInt(99999999);
	private long s22Marzo = rnd.nextInt(99999999);
	private long s22Abril = rnd.nextInt(99999999);
	private long s22Mayo = rnd.nextInt(99999999);
	private long s22Junio = rnd.nextInt(99999999);
	private long s22Julio = rnd.nextInt(99999999);
	private long s22Agosto = rnd.nextInt(99999999);
	private long s22Septiembre = rnd.nextInt(99999999);
	private long s22Octubre = rnd.nextInt(99999999);
	private long s22Noviembre = rnd.nextInt(99999999);
	private long s22Diciembre = rnd.nextInt(99999999);
	
	private long s29Enero = rnd.nextInt(99999999);
	private long s29ebrero = rnd.nextInt(99999999);
	private long s29Marzo = rnd.nextInt(99999999);
	private long s29Abril = rnd.nextInt(99999999);
	private long s29Mayo = rnd.nextInt(99999999);
	private long s29Junio = rnd.nextInt(99999999);
	private long s29Julio = rnd.nextInt(99999999);
	private long s29Agosto = rnd.nextInt(99999999);
	private long s29Septiembre = rnd.nextInt(99999999);
	private long s29Octubre = rnd.nextInt(99999999);
	private long s29Noviembre = rnd.nextInt(99999999);
	private long s29Diciembre = rnd.nextInt(99999999);
	
	private long convenio = (long) (s24Julio * 12 * 0.7);
	private long total = convenio + rnd.nextInt(2);
	private String color = "#FFB5B5";
	
	private long marcoMonto = s24Total;
	// private long marcoPor;
	private float remesaPor = rnd.nextFloat();
	private Double remesaMonto = Double.valueOf(marcoMonto * remesaPor);
	
	private float convenioPor = rnd.nextFloat();
	private long convenioMonto = (long) (marcoMonto * convenioPor);
	
	
	public long getConvenioMonto() {
		return convenioMonto;
	}
	
	public void setConvenioMonto( long convenioMonto ) {
		this.convenioMonto = convenioMonto;
	}
	
	public float getConvenioPor() {
		return convenioPor;
	}
	
	public void setConvenioPor( float convenioPor ) {
		this.convenioPor = convenioPor;
	}
	
	public float getRemesaPor() {
		return remesaPor;
	}
	
	public long getMarcoMonto() {
		return marcoMonto;
	}
	
	public void setMarcoMonto( long marcoMonto ) {
		this.marcoMonto = marcoMonto;
	}
	
	// public long getMarcoPor() {
	// return marcoPor;
	// }
	// public void setMarcoPor( long marcoPor ) {
	// this.marcoPor = marcoPor;
	// }
	// public float getRemesaPor() {
	// return remesaPor;
	// }
	public void setRemesaPor( float remesaPor ) {
		this.remesaPor = remesaPor;
	}
	
	public Double getRemesaMonto() {
		return remesaMonto;
	}
	
	public void setRemesaMonto( Double remesaMonto ) {
		this.remesaMonto = remesaMonto;
	}
	
	public long getConvenio() {
		return convenio;
	}
	
	public void setConvenio( long convenio ) {
		this.convenio = convenio;
	}
	
	public long getTotal() {
		return total;
	}
	
	public void setTotal( long total ) {
		this.total = total;
	}
	
	public String getColor() {
		return color;
	}
	
	public void setColor( String color ) {
		this.color = color;
	}
	
	public MonitoreoPojo () {
		if (convenio == total) {
			this.color = "#DFFFCA";
		}
	}
	
	public Random getRnd() {
		return rnd;
	}
	
	public void setRnd( Random rnd ) {
		this.rnd = rnd;
	}
	
	public String getEstablecimiento() {
		return establecimiento;
	}
	
	public void setEstablecimiento( String establecimiento ) {
		this.establecimiento = establecimiento;
	}
	
	public long getS24Enero() {
		return s24Enero;
	}
	
	public void setS24Enero( long s24Enero ) {
		this.s24Enero = s24Enero;
	}
	
	public long getS24Febrero() {
		return s24Febrero;
	}
	
	public void setS24Febrero( long s24Febrero ) {
		this.s24Febrero = s24Febrero;
	}
	
	public long getS24Marzo() {
		return s24Marzo;
	}
	
	public void setS24Marzo( long s24Marzo ) {
		this.s24Marzo = s24Marzo;
	}
	
	public long getS24Abril() {
		return s24Abril;
	}
	
	public void setS24Abril( long s24Abril ) {
		this.s24Abril = s24Abril;
	}
	
	public long getS24Mayo() {
		return s24Mayo;
	}
	
	public void setS24Mayo( long s24Mayo ) {
		this.s24Mayo = s24Mayo;
	}
	
	public long getS24Junio() {
		return s24Junio;
	}
	
	public void setS24Junio( long s24Junio ) {
		this.s24Junio = s24Junio;
	}
	
	public long getS24Julio() {
		return s24Julio;
	}
	
	public void setS24Julio( long s24Julio ) {
		this.s24Julio = s24Julio;
	}
	
	public long getS24Agosto() {
		return s24Agosto;
	}
	
	public void setS24Agosto( long s24Agosto ) {
		this.s24Agosto = s24Agosto;
	}
	
	public long getS24Septiembre() {
		return s24Septiembre;
	}
	
	public void setS24Septiembre( long s24Septiembre ) {
		this.s24Septiembre = s24Septiembre;
	}
	
	public long getS24Octubre() {
		return s24Octubre;
	}
	
	public void setS24Octubre( long s24Octubre ) {
		this.s24Octubre = s24Octubre;
	}
	
	public long getS24Noviembre() {
		return s24Noviembre;
	}
	
	public void setS24Noviembre( long s24Noviembre ) {
		this.s24Noviembre = s24Noviembre;
	}
	
	public long getS24Diciembre() {
		return s24Diciembre;
	}
	
	public void setS24Diciembre( long s24Diciembre ) {
		this.s24Diciembre = s24Diciembre;
	}
	
	public long getS21Enero() {
		return s21Enero;
	}
	
	public void setS21Enero( long s21Enero ) {
		this.s21Enero = s21Enero;
	}
	
	public long getS21Febrero() {
		return s21Febrero;
	}
	
	public void setS21Febrero( long s21Febrero ) {
		this.s21Febrero = s21Febrero;
	}
	
	public long getS21Marzo() {
		return s21Marzo;
	}
	
	public void setS21Marzo( long s21Marzo ) {
		this.s21Marzo = s21Marzo;
	}
	
	public long getS21Abril() {
		return s21Abril;
	}
	
	public void setS21Abril( long s21Abril ) {
		this.s21Abril = s21Abril;
	}
	
	public long getS21Mayo() {
		return s21Mayo;
	}
	
	public void setS21Mayo( long s21Mayo ) {
		this.s21Mayo = s21Mayo;
	}
	
	public long getS21Junio() {
		return s21Junio;
	}
	
	public void setS21Junio( long s21Junio ) {
		this.s21Junio = s21Junio;
	}
	
	public long getS21Julio() {
		return s21Julio;
	}
	
	public void setS21Julio( long s21Julio ) {
		this.s21Julio = s21Julio;
	}
	
	public long getS21Agosto() {
		return s21Agosto;
	}
	
	public void setS21Agosto( long s21Agosto ) {
		this.s21Agosto = s21Agosto;
	}
	
	public long getS21Septiembre() {
		return s21Septiembre;
	}
	
	public void setS21Septiembre( long s21Septiembre ) {
		this.s21Septiembre = s21Septiembre;
	}
	
	public long getS21Octubre() {
		return s21Octubre;
	}
	
	public void setS21Octubre( long s21Octubre ) {
		this.s21Octubre = s21Octubre;
	}
	
	public long getS21Noviembre() {
		return s21Noviembre;
	}
	
	public void setS21Noviembre( long s21Noviembre ) {
		this.s21Noviembre = s21Noviembre;
	}
	
	public long getS21Diciembre() {
		return s21Diciembre;
	}
	
	public void setS21Diciembre( long s21Diciembre ) {
		this.s21Diciembre = s21Diciembre;
	}
	
	public long getS22Enero() {
		return s22Enero;
	}
	
	public void setS22Enero( long s22Enero ) {
		this.s22Enero = s22Enero;
	}
	
	public long getS22Febrero() {
		return s22Febrero;
	}
	
	public void setS22Febrero( long s22Febrero ) {
		this.s22Febrero = s22Febrero;
	}
	
	public long getS22Marzo() {
		return s22Marzo;
	}
	
	public void setS22Marzo( long s22Marzo ) {
		this.s22Marzo = s22Marzo;
	}
	
	public long getS22Abril() {
		return s22Abril;
	}
	
	public void setS22Abril( long s22Abril ) {
		this.s22Abril = s22Abril;
	}
	
	public long getS22Mayo() {
		return s22Mayo;
	}
	
	public void setS22Mayo( long s22Mayo ) {
		this.s22Mayo = s22Mayo;
	}
	
	public long getS22Junio() {
		return s22Junio;
	}
	
	public void setS22Junio( long s22Junio ) {
		this.s22Junio = s22Junio;
	}
	
	public long getS22Julio() {
		return s22Julio;
	}
	
	public void setS22Julio( long s22Julio ) {
		this.s22Julio = s22Julio;
	}
	
	public long getS22Agosto() {
		return s22Agosto;
	}
	
	public void setS22Agosto( long s22Agosto ) {
		this.s22Agosto = s22Agosto;
	}
	
	public long getS22Septiembre() {
		return s22Septiembre;
	}
	
	public void setS22Septiembre( long s22Septiembre ) {
		this.s22Septiembre = s22Septiembre;
	}
	
	public long getS22Octubre() {
		return s22Octubre;
	}
	
	public void setS22Octubre( long s22Octubre ) {
		this.s22Octubre = s22Octubre;
	}
	
	public long getS22Noviembre() {
		return s22Noviembre;
	}
	
	public void setS22Noviembre( long s22Noviembre ) {
		this.s22Noviembre = s22Noviembre;
	}
	
	public long getS22Diciembre() {
		return s22Diciembre;
	}
	
	public void setS22Diciembre( long s22Diciembre ) {
		this.s22Diciembre = s22Diciembre;
	}
	
	public long getS29Enero() {
		return s29Enero;
	}
	
	public void setS29Enero( long s29Enero ) {
		this.s29Enero = s29Enero;
	}
	
	public long getS29ebrero() {
		return s29ebrero;
	}
	
	public void setS29ebrero( long s29ebrero ) {
		this.s29ebrero = s29ebrero;
	}
	
	public long getS29Marzo() {
		return s29Marzo;
	}
	
	public void setS29Marzo( long s29Marzo ) {
		this.s29Marzo = s29Marzo;
	}
	
	public long getS29Abril() {
		return s29Abril;
	}
	
	public void setS29Abril( long s29Abril ) {
		this.s29Abril = s29Abril;
	}
	
	public long getS29Mayo() {
		return s29Mayo;
	}
	
	public void setS29Mayo( long s29Mayo ) {
		this.s29Mayo = s29Mayo;
	}
	
	public long getS29Junio() {
		return s29Junio;
	}
	
	public void setS29Junio( long s29Junio ) {
		this.s29Junio = s29Junio;
	}
	
	public long getS29Julio() {
		return s29Julio;
	}
	
	public void setS29Julio( long s29Julio ) {
		this.s29Julio = s29Julio;
	}
	
	public long getS29Agosto() {
		return s29Agosto;
	}
	
	public void setS29Agosto( long s29Agosto ) {
		this.s29Agosto = s29Agosto;
	}
	
	public long getS29Septiembre() {
		return s29Septiembre;
	}
	
	public void setS29Septiembre( long s29Septiembre ) {
		this.s29Septiembre = s29Septiembre;
	}
	
	public long getS29Octubre() {
		return s29Octubre;
	}
	
	public void setS29Octubre( long s29Octubre ) {
		this.s29Octubre = s29Octubre;
	}
	
	public long getS29Noviembre() {
		return s29Noviembre;
	}
	
	public void setS29Noviembre( long s29Noviembre ) {
		this.s29Noviembre = s29Noviembre;
	}
	
	public long getS29Diciembre() {
		return s29Diciembre;
	}
	
	public void setS29Diciembre( long s29Diciembre ) {
		this.s29Diciembre = s29Diciembre;
	}
	
}
