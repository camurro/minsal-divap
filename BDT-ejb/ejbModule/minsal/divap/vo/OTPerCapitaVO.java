package minsal.divap.vo;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

public class OTPerCapitaVO implements Serializable{


	private static final long serialVersionUID = 1452145946281053402L;
	private Integer idComuna;
	private String comuna;
	private String tipoComuna;
	private Long marcoPresupuestario;
	private Long transferenciaAcumulada;
	private String porcentajeTransferenciaAcumulada;
	private Long diferencia;
	private String porcentajeDiferencia;
	private boolean aprobado;
	private Long conveniosRecibidos;
	private Integer cuotaFinal;
	private List<Integer> idDetalleRemesaEliminar;
	private List<RemesasProgramaVO> remesas;
	private List<Integer> idDetalleRemesaAProbarConsolidador;
	
	
	public Integer getIdComuna() {
		return idComuna;
	}
	
	public void setIdComuna(Integer idComuna) {
		this.idComuna = idComuna;
	}
	
	public String getComuna() {
		return comuna;
	}
	
	public void setComuna(String comuna) {
		this.comuna = comuna;
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
	
	public String getPorcentajeTransferenciaAcumulada() {
		DecimalFormat df = new DecimalFormat("#.00"); 
		if(marcoPresupuestario!=null && marcoPresupuestario>0){
			porcentajeTransferenciaAcumulada = df.format(((double)transferenciaAcumulada*100)/(double)marcoPresupuestario);
		}else{
			porcentajeTransferenciaAcumulada="";
		}
		return porcentajeTransferenciaAcumulada;
	}
	
	public void setPorcentajeTransferenciaAcumulada(String porcentajeTransferenciaAcumulada) {
		this.porcentajeTransferenciaAcumulada = porcentajeTransferenciaAcumulada;
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
	
	public String getTipoComuna() {
		return tipoComuna;
	}
	
	public void setTipoComuna(String tipoComuna) {
		this.tipoComuna = tipoComuna;
	}
	
	public List<RemesasProgramaVO> getRemesas() {
		return remesas;
	}
	
	public void setRemesas(List<RemesasProgramaVO> remesas) {
		this.remesas = remesas;
	}
	
	public boolean isAprobado() {
		return aprobado;
	}
	
	public void setAprobado(boolean aprobado) {
		this.aprobado = aprobado;
	}
	
	public Integer getCuotaFinal() {
		return cuotaFinal;
	}
	
	public void setCuotaFinal(Integer cuotaFinal) {
		this.cuotaFinal = cuotaFinal;
	}

	public List<Integer> getIdDetalleRemesaEliminar() {
		return idDetalleRemesaEliminar;
	}

	public void setIdDetalleRemesaEliminar(List<Integer> idDetalleRemesaEliminar) {
		this.idDetalleRemesaEliminar = idDetalleRemesaEliminar;
	}

	public Long getConveniosRecibidos() {
		return conveniosRecibidos;
	}

	public void setConveniosRecibidos(Long conveniosRecibidos) {
		this.conveniosRecibidos = conveniosRecibidos;
	}

	public List<Integer> getIdDetalleRemesaAProbarConsolidador() {
		return idDetalleRemesaAProbarConsolidador;
	}

	public void setIdDetalleRemesaAProbarConsolidador(
			List<Integer> idDetalleRemesaAProbarConsolidador) {
		this.idDetalleRemesaAProbarConsolidador = idDetalleRemesaAProbarConsolidador;
	}


}
