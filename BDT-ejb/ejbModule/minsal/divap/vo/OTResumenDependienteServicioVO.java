package minsal.divap.vo;

import java.io.Serializable;
import java.util.List;


public class OTResumenDependienteServicioVO implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8513591987351166534L;
	
	private EstablecimientoVO establecimiento;
	private Long marcoPresupuestario;
	
	private Long transferenciaAcumulada;
	private Long porcentajeTransferencia;
	
	private Long conveniosRecibidos;
	private Long porcentajeConveniosRecibidos;
	
	private List<RemesasProgramaVO> remesas;
	
	

	public EstablecimientoVO getEstablecimiento() {
		return establecimiento;
	}

	public void setEstablecimiento(EstablecimientoVO establecimiento) {
		this.establecimiento = establecimiento;
	}

	public Long getMarcoPresupuestario() {
		return marcoPresupuestario;
	}

	public void setMarcoPresupuestario(Long marcoPresupuestario) {
		this.marcoPresupuestario = marcoPresupuestario;
	}

	public Long getTransferenciaAcumulada() {
		return transferenciaAcumulada;
	}

	public void setTransferenciaAcumulada(Long transferenciaAcumulada) {
		this.transferenciaAcumulada = transferenciaAcumulada;
	}

	public Long getPorcentajeTransferencia() {
		if(marcoPresupuestario!=null && transferenciaAcumulada!=null){
			porcentajeTransferencia = (transferenciaAcumulada*100)/marcoPresupuestario;
		}
		return porcentajeTransferencia;
	}

	public void setPorcentajeTransferencia(Long porcentajeTransferencia) {
		this.porcentajeTransferencia = porcentajeTransferencia;
	}

	public Long getConveniosRecibidos() {
		return conveniosRecibidos;
	}

	public void setConveniosRecibidos(Long conveniosRecibidos) {
		this.conveniosRecibidos = conveniosRecibidos;
	}

	public Long getPorcentajeConveniosRecibidos() {
		if(marcoPresupuestario!=null && conveniosRecibidos!=null){
			porcentajeConveniosRecibidos = (conveniosRecibidos*100)/marcoPresupuestario;
		}
		return porcentajeConveniosRecibidos;
	}

	public void setPorcentajeConveniosRecibidos(Long porcentajeConveniosRecibidos) {
		this.porcentajeConveniosRecibidos = porcentajeConveniosRecibidos;
	}

	public List<RemesasProgramaVO> getRemesas() {
		return remesas;
	}

	public void setRemesas(List<RemesasProgramaVO> remesas) {
		this.remesas = remesas;
	}
	
	
	
	
}