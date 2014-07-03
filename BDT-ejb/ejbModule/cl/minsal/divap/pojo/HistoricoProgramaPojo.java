package cl.minsal.divap.pojo;

import java.util.Random;

public class HistoricoProgramaPojo {
	private String region;
	private String servicio;
	private String comuna;
	
	private Random rnd = new Random();
	
	private long v2006 = rnd.nextInt(99999999);
	private long v2007 = v2006+rnd.nextInt(999999);
	private long v2008 = v2007+rnd.nextInt(999999);
	private long v2009 = v2008+rnd.nextInt(999999);
	private long v2010 = v2009+rnd.nextInt(999999);
	private long v2011 = v2010+rnd.nextInt(999999);
	private long v2012 = v2011+rnd.nextInt(999999);
	private long v2013 = v2012+rnd.nextInt(999999);
	private long v2014 = v2013+rnd.nextInt(999999);
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
	public Random getRnd() {
		return rnd;
	}
	public void setRnd( Random rnd ) {
		this.rnd = rnd;
	}
	public long getV2006() {
		return v2006;
	}
	public void setV2006( long v2006 ) {
		this.v2006 = v2006;
	}
	public long getV2007() {
		return v2007;
	}
	public void setV2007( long v2007 ) {
		this.v2007 = v2007;
	}
	public long getV2008() {
		return v2008;
	}
	public void setV2008( long v2008 ) {
		this.v2008 = v2008;
	}
	public long getV2009() {
		return v2009;
	}
	public void setV2009( long v2009 ) {
		this.v2009 = v2009;
	}
	public long getV2010() {
		return v2010;
	}
	public void setV2010( long v2010 ) {
		this.v2010 = v2010;
	}
	public long getV2011() {
		return v2011;
	}
	public void setV2011( long v2011 ) {
		this.v2011 = v2011;
	}
	public long getV2012() {
		return v2012;
	}
	public void setV2012( long v2012 ) {
		this.v2012 = v2012;
	}
	public long getV2013() {
		return v2013;
	}
	public void setV2013( long v2013 ) {
		this.v2013 = v2013;
	}
	public long getV2014() {
		return v2014;
	}
	public void setV2014( long v2014 ) {
		this.v2014 = v2014;
	}
	
	
}
