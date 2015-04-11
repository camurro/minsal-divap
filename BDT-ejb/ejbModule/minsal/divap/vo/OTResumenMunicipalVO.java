package minsal.divap.vo;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

import cl.minsal.divap.model.Cuota;


public class OTResumenMunicipalVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7265837851085322699L;
	private ComunaVO comuna;
	private Long marcoPresupuestario;
	private boolean creacion;
	
	private Integer idConvenioComuna;
	private Integer idConvenioComunaComponenteSinAprobar;
	
	private Long transferenciaAcumulada;
	private String porcentajeTransferencia;
	
	private Long conveniosRecibidos;
	private String porcentajeConveniosRecibidos;
	
	private Long diferencia;
	private String porcentajeDiferencia;
	private boolean aprobado;
	private List<RemesasProgramaVO> remesas;
	
	private List<Integer> idConveniosAprobados;
	private List<Integer> idConveniosEnTramite;
	
	private Integer cuotaInicial;
	private Integer cuotaFinal;
	
	private List<Integer> IdDetalleRemesaEliminar;
	private List<Integer> idDetalleRemesaAProbarConsolidador;
	
	private Cuota cuota;
	
	public ComunaVO getComuna() {
		return comuna;
	}

	public void setComuna(ComunaVO comuna) {
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

	public String getPorcentajeTransferencia() {
		DecimalFormat df = new DecimalFormat("#.00"); 
		if(marcoPresupuestario != null && transferenciaAcumulada != null && marcoPresupuestario > 0){
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
		if(marcoPresupuestario != null && conveniosRecibidos != null && marcoPresupuestario > 0){
			porcentajeConveniosRecibidos = df.format(((double)conveniosRecibidos*100)/(double)marcoPresupuestario);
		}else{
			porcentajeConveniosRecibidos="";
		}
		return porcentajeConveniosRecibidos;
	}

	public void setPorcentajeConveniosRecibidos(String porcentajeConveniosRecibidos) {
		this.porcentajeConveniosRecibidos = porcentajeConveniosRecibidos;
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

	public List<RemesasProgramaVO> getRemesas() {
		return remesas;
	}

	public void setRemesas(List<RemesasProgramaVO> remesas) {
		this.remesas = remesas;
	}

	public List<Integer> getIdDetalleRemesaEliminar() {
		return IdDetalleRemesaEliminar;
	}

	public void setIdDetalleRemesaEliminar(List<Integer> idDetalleRemesaEliminar) {
		IdDetalleRemesaEliminar = idDetalleRemesaEliminar;
	}

	public Cuota getCuota() {
		return cuota;
	}

	public void setCuota(Cuota cuota) {
		this.cuota = cuota;
	}

	public List<Integer> getIdConveniosAprobados() {
		return idConveniosAprobados;
	}

	public void setIdConveniosAprobados(List<Integer> idConveniosAprobados) {
		this.idConveniosAprobados = idConveniosAprobados;
	}

	public List<Integer> getIdConveniosEnTramite() {
		return idConveniosEnTramite;
	}

	public void setIdConveniosEnTramite(List<Integer> idConveniosEnTramite) {
		this.idConveniosEnTramite = idConveniosEnTramite;
	}
	
	public Integer getCuotaInicial() {
		return cuotaInicial;
	}

	public void setCuotaInicial(Integer cuotaInicial) {
		this.cuotaInicial = cuotaInicial;
	}

	public Integer getCuotaFinal() {
		return cuotaFinal;
	}

	public void setCuotaFinal(Integer cuotaFinal) {
		this.cuotaFinal = cuotaFinal;
	}

	public boolean isCreacion() {
		return creacion;
	}

	public void setCreacion(boolean creacion) {
		this.creacion = creacion;
	}

	public List<Integer> getIdDetalleRemesaAProbarConsolidador() {
		return idDetalleRemesaAProbarConsolidador;
	}

	public void setIdDetalleRemesaAProbarConsolidador(
			List<Integer> idDetalleRemesaAProbarConsolidador) {
		this.idDetalleRemesaAProbarConsolidador = idDetalleRemesaAProbarConsolidador;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comuna == null) ? 0 : comuna.hashCode());
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
		OTResumenMunicipalVO other = (OTResumenMunicipalVO) obj;
		if (comuna == null) {
			if (other.comuna != null)
				return false;
		} else if (!comuna.equals(other.comuna))
			return false;
		return true;
	}

	public Integer getIdConvenioComuna() {
		return idConvenioComuna;
	}

	public void setIdConvenioComuna(Integer idConvenioComuna) {
		this.idConvenioComuna = idConvenioComuna;
	}

	public Integer getIdConvenioComunaComponenteSinAprobar() {
		return idConvenioComunaComponenteSinAprobar;
	}

	public void setIdConvenioComunaComponenteSinAprobar(
			Integer idConvenioComunaComponenteSinAprobar) {
		this.idConvenioComunaComponenteSinAprobar = idConvenioComunaComponenteSinAprobar;
	}

 
	
}