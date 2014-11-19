package minsal.divap.vo;

import java.io.Serializable;


public class OTResumenDependienteServicioVO implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8513591987351166534L;
	
	private EstablecimientoVO establecimiento;

	public EstablecimientoVO getEstablecimiento() {
		return establecimiento;
	}

	public void setEstablecimiento(EstablecimientoVO establecimiento) {
		this.establecimiento = establecimiento;
	}
	
	
	
}