package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OTRevisarAntecedentesGlobalVO 
{
	
	
	public List<OTRevisarAntecedentesVO> listadoServicios = new  ArrayList<OTRevisarAntecedentesVO>();
	
	//TOTALES
	Random rnd = new Random();
	private long eneroRemesa09;
	private long eneroRemesa24;
	private long eneroRemesa28;

	private long febreroRemesa09;
	private long febreroRemesa24;
	private long febreroRemesa28;
	
	private long marzoRemesa09;
	private long marzoRemesa24;
	private long marzoRemesa28;
	
	private long abrilRemesa09;
	private long abrilRemesa24;
	private long abrilRemesa28;
	
	private long mayoRemesa09;
	private long mayoRemesa24;
	private long mayoRemesa28;
	
	private long junioRemesa09;
	private long junioRemesa24;
	private long junioRemesa28;

	private long julioRemesa09 ;
	private long julioRemesa24;
	private long julioRemesa28 ;

	private long agostoRemesa09;
	private long agostoRemesa24;
	private long agostoRemesa28;
	
	private long septiembreRemesa09;
	private long septiembreRemesa24;
	private long septiembreRemesa28;
	
	private long octubreRemesa09;
	private long octubreRemesa24;
	private long octubreRemesa28;
	
	private long noviembreRemesa09;
	private long noviembreRemesa24;
	private long noviembreRemesa28;
	
	private long diciembreRemesa09;
	private long diciembreRemesa24;
	private long diciembreRemesa28;
	
	
	
	public List<OTRevisarAntecedentesVO> getListadoServicios() {
		return listadoServicios;
	}
	public void setListadoServicios(List<OTRevisarAntecedentesVO> listadoServicios) {
		this.listadoServicios = listadoServicios;
	}
	public Random getRnd() {
		return rnd;
	}
	public void setRnd(Random rnd) {
		this.rnd = rnd;
	}
	public long getEneroRemesa09() {
		
		eneroRemesa09 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			eneroRemesa09 += e.getEneroRemesa09();
		}
		
		return eneroRemesa09;
	}
	public void setEneroRemesa09(long eneroRemesa09) {
		this.eneroRemesa09 = eneroRemesa09;
	}
	public long getEneroRemesa24() {
		
		eneroRemesa24 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			eneroRemesa24 += e.getEneroRemesa24();
		}
		
		return eneroRemesa24;
	}
	public void setEneroRemesa24(long eneroRemesa24) {
		this.eneroRemesa24 = eneroRemesa24;
	}
	public long getEneroRemesa28() {
		
		eneroRemesa28 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			eneroRemesa28 += e.getEneroRemesa28();
		}
		return eneroRemesa28;
	}
	public void setEneroRemesa28(long eneroRemesa28) {
		this.eneroRemesa28 = eneroRemesa28;
	}
	public long getFebreroRemesa09() {
		
		febreroRemesa09 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			febreroRemesa09 += e.getFebreroRemesa09();
		}
		return febreroRemesa09;
	}
	public void setFebreroRemesa09(long febreroRemesa09) {
		this.febreroRemesa09 = febreroRemesa09;
	}
	public long getFebreroRemesa24() {
		febreroRemesa24 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			febreroRemesa24 += e.getFebreroRemesa24();
		}
		return febreroRemesa24;
	}
	
	public void setFebreroRemesa24(long febreroRemesa24) {
		this.febreroRemesa24 = febreroRemesa24;
	}
	
	
	public long getFebreroRemesa28() {
		febreroRemesa28 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			febreroRemesa28 += e.getFebreroRemesa28();
		}
		return febreroRemesa28;
	}
	public void setFebreroRemesa28(long febreroRemesa28) {
		this.febreroRemesa28 = febreroRemesa28;
	}
	public long getMarzoRemesa09() {
		marzoRemesa09 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			marzoRemesa09 += e.getMarzoRemesa09();
		}
		return marzoRemesa09;
	}
	public void setMarzoRemesa09(long marzoRemesa09) {
		this.marzoRemesa09 = marzoRemesa09;
	}
	public long getMarzoRemesa24() {
		marzoRemesa24 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			marzoRemesa24 += e.getMarzoRemesa24();
		}
		return marzoRemesa24;
	}
	public void setMarzoRemesa24(long marzoRemesa24) {
		this.marzoRemesa24 = marzoRemesa24;
	}
	public long getMarzoRemesa28() {
		marzoRemesa28 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			marzoRemesa28 += e.getMarzoRemesa28();
		}
		return marzoRemesa28;
	}
	public void setMarzoRemesa28(long marzoRemesa28) {
		this.marzoRemesa28 = marzoRemesa28;
	}
	public long getAbrilRemesa09() {
		abrilRemesa09 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			abrilRemesa09 += e.getAbrilRemesa09();
		}
		return abrilRemesa09;
	}
	public void setAbrilRemesa09(long abrilRemesa09) {
		this.abrilRemesa09 = abrilRemesa09;
	}
	public long getAbrilRemesa24() {
		abrilRemesa24 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			abrilRemesa24 += e.getAbrilRemesa24();
		}
		return abrilRemesa24;
	}
	public void setAbrilRemesa24(long abrilRemesa24) {
		marzoRemesa28 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			marzoRemesa28 += e.getAbrilRemesa24();
		}
		this.abrilRemesa24 = abrilRemesa24;
	}
	public long getAbrilRemesa28() {
		abrilRemesa28 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			abrilRemesa28 += e.getAbrilRemesa28();
		}
		return abrilRemesa28;
	}
	public void setAbrilRemesa28(long abrilRemesa28) {
		this.abrilRemesa28 = abrilRemesa28;
	}
	public long getMayoRemesa09() {
		mayoRemesa09 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			mayoRemesa09 += e.getMayoRemesa09();
		}
		return mayoRemesa09;
	}
	public void setMayoRemesa09(long mayoRemesa09) {
		this.mayoRemesa09 = mayoRemesa09;
	}
	public long getMayoRemesa24() {
		mayoRemesa24 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			mayoRemesa24 += e.getMayoRemesa24();
		}
		return mayoRemesa24;
	}
	public void setMayoRemesa24(long mayoRemesa24) {
		
		this.mayoRemesa24 = mayoRemesa24;
	}
	public long getMayoRemesa28() {
		mayoRemesa28 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			mayoRemesa28 += e.getMayoRemesa28();
		}
		return mayoRemesa28;
	}
	public void setMayoRemesa28(long mayoRemesa28) {
		this.mayoRemesa28 = mayoRemesa28;
	}
	public long getJunioRemesa09() {
		junioRemesa09 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			junioRemesa09 += e.getJunioRemesa09();
		}
		return junioRemesa09;
	}
	public void setJunioRemesa09(long junioRemesa09) {
		this.junioRemesa09 = junioRemesa09;
	}
	public long getJunioRemesa24() {
		junioRemesa24 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			junioRemesa24 += e.getJunioRemesa24();
		}
		return junioRemesa24;
	}
	public void setJunioRemesa24(long junioRemesa24) {
		this.junioRemesa24 = junioRemesa24;
	}
	public long getJunioRemesa28() {
		junioRemesa28 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			junioRemesa28 += e.getJunioRemesa28();
		}
		return junioRemesa28;
	}
	public void setJunioRemesa28(long junioRemesa28) {
		this.junioRemesa28 = junioRemesa28;
	}
	public long getJulioRemesa09() {
		julioRemesa09 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			julioRemesa09 += e.getJulioRemesa09();
		}
		return julioRemesa09;
	}
	public void setJulioRemesa09(long julioRemesa09) {
		this.julioRemesa09 = julioRemesa09;
	}
	public long getJulioRemesa24() {
		julioRemesa24 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			julioRemesa24 += e.getJulioRemesa24();
		}
		return julioRemesa24;
	}
	public void setJulioRemesa24(long julioRemesa24) {
		this.julioRemesa24 = julioRemesa24;
	}
	public long getJulioRemesa28() {
		julioRemesa28 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			julioRemesa28 += e.getJulioRemesa28();
		}
		return julioRemesa28;
	}
	public void setJulioRemesa28(long julioRemesa28) {
		this.julioRemesa28 = julioRemesa28;
	}
	public long getAgostoRemesa09() {
		agostoRemesa09 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			agostoRemesa09 += e.getAgostoRemesa09();
		}
		return agostoRemesa09;
	}
	public void setAgostoRemesa09(long agostoRemesa09) {
		this.agostoRemesa09 = agostoRemesa09;
	}
	public long getAgostoRemesa24() {
		agostoRemesa24 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			agostoRemesa24 += e.getAgostoRemesa24();
		}
		return agostoRemesa24;
	}
	public void setAgostoRemesa24(long agostoRemesa24) {
		this.agostoRemesa24 = agostoRemesa24;
	}
	public long getAgostoRemesa28() {
		agostoRemesa28 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			agostoRemesa28 += e.getAgostoRemesa28();
		}
		return agostoRemesa28;
	}
	public void setAgostoRemesa28(long agostoRemesa28) {
		
		this.agostoRemesa28 = agostoRemesa28;
	}
	public long getSeptiembreRemesa09() {
		septiembreRemesa09 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			septiembreRemesa09 += e.getSeptiembreRemesa09();
		}
		return septiembreRemesa09;
	}

	public void setSeptiembreRemesa09(long septiembreRemesa09) {
		this.septiembreRemesa09 = septiembreRemesa09;
	}
	public long getSeptiembreRemesa24() {
		septiembreRemesa24 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			septiembreRemesa24 += e.getSeptiembreRemesa24();
		}
		return septiembreRemesa24;
	}
	public void setSeptiembreRemesa24(long septiembreRemesa24) {
		this.septiembreRemesa24 = septiembreRemesa24;
	}
	public long getSeptiembreRemesa28() {
		septiembreRemesa28 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			septiembreRemesa28 += e.getSeptiembreRemesa28();
		}
		return septiembreRemesa28;
	}
	public void setSeptiembreRemesa28(long septiembreRemesa28) {
		this.septiembreRemesa28 = septiembreRemesa28;
	}
	public long getOctubreRemesa09() {
		octubreRemesa09 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			octubreRemesa09 += e.getOctubreRemesa09();
		}
		return octubreRemesa09;
	}
	public void setOctubreRemesa09(long octubreRemesa09) {
		this.octubreRemesa09 = octubreRemesa09;
	}
	public long getOctubreRemesa24() {
		octubreRemesa24 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			octubreRemesa24 += e.getOctubreRemesa24();
		}
		return octubreRemesa24;
	}
	public void setOctubreRemesa24(long octubreRemesa24) {
		this.octubreRemesa24 = octubreRemesa24;
	}
	public long getOctubreRemesa28() {
		octubreRemesa28 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			octubreRemesa28 += e.getOctubreRemesa28();
		}
		return octubreRemesa28;
	}
	public void setOctubreRemesa28(long octubreRemesa28) {
		this.octubreRemesa28 = octubreRemesa28;
	}
	public long getNoviembreRemesa09() {
		noviembreRemesa09 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			noviembreRemesa09 += e.getNoviembreRemesa09();
		}
		return noviembreRemesa09;
	}
	public void setNoviembreRemesa09(long noviembreRemesa09) {
		this.noviembreRemesa09 = noviembreRemesa09;
	}
	public long getNoviembreRemesa24() {
		noviembreRemesa24 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			noviembreRemesa24 += e.getNoviembreRemesa24();
		}
		return noviembreRemesa24;
	}
	public void setNoviembreRemesa24(long noviembreRemesa24) {
		this.noviembreRemesa24 = noviembreRemesa24;
	}
	public long getNoviembreRemesa28() {
		noviembreRemesa28 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			noviembreRemesa28 += e.getNoviembreRemesa28();
		}
		return noviembreRemesa28;
	}
	public void setNoviembreRemesa28(long noviembreRemesa28) {
		this.noviembreRemesa28 = noviembreRemesa28;
	}
	public long getDiciembreRemesa09() {
		diciembreRemesa09 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			diciembreRemesa09 += e.getDiciembreRemesa09();
		}
		return diciembreRemesa09;
	}
	public void setDiciembreRemesa09(long diciembreRemesa09) {
		this.diciembreRemesa09 = diciembreRemesa09;
	}
	public long getDiciembreRemesa24() {
		diciembreRemesa24 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			diciembreRemesa24 += e.getDiciembreRemesa24();
		}
		return diciembreRemesa24;
	}
	public void setDiciembreRemesa24(long diciembreRemesa24) {
		this.diciembreRemesa24 = diciembreRemesa24;
	}
	public long getDiciembreRemesa28() {
		diciembreRemesa28 = 0;
		for (OTRevisarAntecedentesVO e : listadoServicios) {
			diciembreRemesa28 += e.getDiciembreRemesa28();
		}
		return diciembreRemesa28;
	}
	public void setDiciembreRemesa28(long diciembreRemesa28) {
		this.diciembreRemesa28 = diciembreRemesa28;
	}
	
	
	
	
}