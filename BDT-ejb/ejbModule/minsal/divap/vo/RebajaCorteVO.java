package minsal.divap.vo;

import java.io.Serializable;

public class RebajaCorteVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -931637413116645731L;

	private Integer id;
	private MesVO mesInicio;
	private MesVO mesRebaja;
	private MesVO mesHasta;
	
	public RebajaCorteVO(){

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public MesVO getMesInicio() {
		return mesInicio;
	}

	public void setMesInicio(MesVO mesInicio) {
		this.mesInicio = mesInicio;
	}

	public MesVO getMesRebaja() {
		return mesRebaja;
	}

	public void setMesRebaja(MesVO mesRebaja) {
		this.mesRebaja = mesRebaja;
	}

	public MesVO getMesHasta() {
		return mesHasta;
	}

	public void setMesHasta(MesVO mesHasta) {
		this.mesHasta = mesHasta;
	}

}
