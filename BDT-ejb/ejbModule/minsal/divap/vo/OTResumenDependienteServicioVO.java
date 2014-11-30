package minsal.divap.vo;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

import cl.minsal.divap.model.Cuota;


public class OTResumenDependienteServicioVO implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8513591987351166534L;
	
	private EstablecimientoVO establecimiento;
	private Long marcoPresupuestario;
	
	private Long transferenciaAcumulada;
	private String porcentajeTransferencia;
	
	private Long conveniosRecibidos;
	private String porcentajeConveniosRecibidos;
	
	private Long diferencia;
	private String porcentajeDiferencia;
	private boolean aprobado;
	
	private List<RemesasProgramaVO> remesas;
	private List<Integer> idConvenios;
	
	private List<Integer> idDetalleRemesa;
	
	private Cuota cuota;
	
	

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

	public String getPorcentajeTransferencia() {
		
		DecimalFormat df = new DecimalFormat("#.00"); 
		if(marcoPresupuestario!=null && transferenciaAcumulada!=null && marcoPresupuestario>0){
			porcentajeTransferencia = df.format(((double)transferenciaAcumulada*100)/(double)marcoPresupuestario);
		}else{
			porcentajeTransferencia="";
		}
		
		return porcentajeTransferencia;
	}

	public void setPorcentajeTransferencia(String porcentajeTransferencia) {
		this.porcentajeTransferencia = porcentajeTransferencia;
	}

	public Long getConveniosRecibidos() {
		return conveniosRecibidos;
	}

	public void setConveniosRecibidos(Long conveniosRecibidos) {
		this.conveniosRecibidos = conveniosRecibidos;
	}

	public String getPorcentajeConveniosRecibidos() {
		DecimalFormat df = new DecimalFormat("#.00"); 
		if(marcoPresupuestario!=null && conveniosRecibidos!=null && marcoPresupuestario>0){
			porcentajeConveniosRecibidos = df.format(((double)conveniosRecibidos*100)/(double)marcoPresupuestario);
		}else{
			porcentajeConveniosRecibidos="";
		}
		
		
		return porcentajeConveniosRecibidos;
	}

	public void setPorcentajeConveniosRecibidos(String porcentajeConveniosRecibidos) {
		this.porcentajeConveniosRecibidos = porcentajeConveniosRecibidos;
	}

	public List<RemesasProgramaVO> getRemesas() {
		return remesas;
	}

	public void setRemesas(List<RemesasProgramaVO> remesas) {
		this.remesas = remesas;
	}
	
	

	public Long getDiferencia() {
		diferencia = marcoPresupuestario - transferenciaAcumulada;
		return diferencia;
	}

	public void setDiferencia(Long diferencia) {
		this.diferencia = diferencia;
	}

	public String getPorcentajeDiferencia() {
		DecimalFormat df = new DecimalFormat("#.00"); 
		if(marcoPresupuestario!=null && diferencia!=null && marcoPresupuestario>0){
			porcentajeDiferencia = df.format(((double)diferencia*100)/(double)marcoPresupuestario);
		}else{
			porcentajeDiferencia="";
		}
		return porcentajeDiferencia;
	}

	public void setPorcentajeDiferencia(String porcentajeDiferencia) {
		this.porcentajeDiferencia = porcentajeDiferencia;
	}

	public boolean isAprobado() {
		return aprobado;
	}

	public void setAprobado(boolean aprobado) {
		this.aprobado = aprobado;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((establecimiento == null) ? 0 : establecimiento.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OTResumenDependienteServicioVO other = (OTResumenDependienteServicioVO) obj;
		if (establecimiento == null) {
			if (other.establecimiento != null)
				return false;
		} else if (!establecimiento.equals(other.establecimiento))
			return false;
		return true;
	}



	public List<Integer> getIdConvenios() {
		return idConvenios;
	}

	public void setIdConvenios(List<Integer> idConvenios) {
		this.idConvenios = idConvenios;
	}

	public List<Integer> getIdDetalleRemesa() {
		return idDetalleRemesa;
	}

	public void setIdDetalleRemesa(List<Integer> idDetalleRemesa) {
		this.idDetalleRemesa = idDetalleRemesa;
	}

	public Cuota getCuota() {
		return cuota;
	}

	public void setCuota(Cuota cuota) {
		this.cuota = cuota;
	}

	
	
	
	
	
}