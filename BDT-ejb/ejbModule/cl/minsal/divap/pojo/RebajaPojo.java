package cl.minsal.divap.pojo;

import java.util.Random;

public class RebajaPojo {
	private String comuna;
	private float itemCumplimiento1;
	private float itemCumplimiento2;
	private float itemCumplimiento3;
	private float rebaja1;
	private float rebaja2;
	private float rebaja3;
	private float rebajaExcepcional1;
	private float rebajaExcepcional2;
	private float rebajaExcepcional3;
	
	private float totalRebaja;
	private float totalRebajaExcepcional;
	
	private Random rnd = new Random();
	private long marco = rnd.nextInt(999999999);
	
	public Random getRnd() {
		return rnd;
	}

	public void setRnd( Random rnd ) {
		this.rnd = rnd;
	}

	public long getMarco() {
		return marco;
	}

	public void setMarco( long marco ) {
		this.marco = marco;
	}

	public String getComuna() {
		return comuna;
	}
	
	public void setComuna( String comuna ) {
		this.comuna = comuna;
	}
	
	public float getItemCumplimiento1() {
		return itemCumplimiento1;
	}
	
	public void setItemCumplimiento1( float itemCumplimiento1 ) {
		this.itemCumplimiento1 = itemCumplimiento1;
	}
	
	public float getItemCumplimiento2() {
		return itemCumplimiento2;
	}
	
	public void setItemCumplimiento2( float itemCumplimiento2 ) {
		this.itemCumplimiento2 = itemCumplimiento2;
	}
	
	public float getItemCumplimiento3() {
		return itemCumplimiento3;
	}
	
	public void setItemCumplimiento3( float itemCumplimiento3 ) {
		this.itemCumplimiento3 = itemCumplimiento3;
	}
	
	public float getRebaja1() {
		return rebaja1;
	}
	
	public void setRebaja1( float rebaja1 ) {
		this.rebaja1 = rebaja1;
	}
	
	public float getRebaja2() {
		return rebaja2;
	}
	
	public void setRebaja2( float rebaja2 ) {
		this.rebaja2 = rebaja2;
	}
	
	public float getRebaja3() {
		return rebaja3;
	}
	
	public void setRebaja3( float rebaja3 ) {
		this.rebaja3 = rebaja3;
	}
	
	public float getRebajaExcepcional1() {
		return rebajaExcepcional1;
	}
	
	public void setRebajaExcepcional1( float rebajaExcepcional1 ) {
		this.rebajaExcepcional1 = rebajaExcepcional1;
	}
	
	public float getRebajaExcepcional2() {
		return rebajaExcepcional2;
	}
	
	public void setRebajaExcepcional2( float rebajaExcepcional2 ) {
		this.rebajaExcepcional2 = rebajaExcepcional2;
	}
	
	public float getRebajaExcepcional3() {
		return rebajaExcepcional3;
	}
	
	public void setRebajaExcepcional3( float rebajaExcepcional3 ) {
		this.rebajaExcepcional3 = rebajaExcepcional3;
	}
	
	public float getTotalRebaja() {
		return totalRebaja;
	}
	
	public void setTotalRebaja( float totalRebaja ) {
		this.totalRebaja = totalRebaja;
	}
	
	public float getTotalRebajaExcepcional() {
		return totalRebajaExcepcional;
	}
	
	public void setTotalRebajaExcepcional( float totalRebajaExcepcional ) {
		this.totalRebajaExcepcional = totalRebajaExcepcional;
	}
	
}
