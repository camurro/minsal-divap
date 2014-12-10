package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReporteMonitoreoProgramaPorEstablecimientoVO implements Serializable{

	private static final long serialVersionUID = -7792066115952412397L;
	private String servicio;
	private String programa;
	private Long marco;
	private Long remesa_monto;
	private Double remesa_porcentaje;
	private Long convenio_monto;
	private Double convenio_porcentaje;
	private Long convenio_pendiente;
	
	public ReporteMonitoreoProgramaPorEstablecimientoVO(){
		
	}
	public ReporteMonitoreoProgramaPorEstablecimientoVO(String servicio, String programa, Long marco, Long remesa_monto, Double remesa_porcentaje,
			Long convenio_monto, Double convenio_porcentaje, Long convenio_pendiente){
		super();
		this.servicio = servicio;
		this.programa = programa;
		this.marco = marco;
		this.remesa_monto = remesa_monto;
		this.remesa_porcentaje = remesa_porcentaje;
		this.convenio_monto = convenio_monto;
		this.convenio_porcentaje = convenio_porcentaje;
		this.convenio_pendiente = convenio_pendiente;
	}
	
	public String getServicio() {
		return servicio;
	}
	public void setServicio(String servicio) {
		this.servicio = servicio;
	}
	public String getPrograma() {
		return programa;
	}
	public void setPrograma(String programa) {
		this.programa = programa;
	}
	public Long getMarco() {
		return marco;
	}
	public void setMarco(Long marco) {
		this.marco = marco;
	}
	public Long getRemesa_monto() {
		return remesa_monto;
	}
	public void setRemesa_monto(Long remesa_monto) {
		this.remesa_monto = remesa_monto;
	}
	public Double getRemesa_porcentaje() {
		return remesa_porcentaje;
	}
	public void setRemesa_porcentaje(Double remesa_porcentaje) {
		this.remesa_porcentaje = remesa_porcentaje;
	}
	public Long getConvenio_monto() {
		return convenio_monto;
	}
	public void setConvenio_monto(Long convenio_monto) {
		this.convenio_monto = convenio_monto;
	}
	public Double getConvenio_porcentaje() {
		return convenio_porcentaje;
	}
	public void setConvenio_porcentaje(Double convenio_porcentaje) {
		this.convenio_porcentaje = convenio_porcentaje;
	}
	public Long getConvenio_pendiente() {
		return convenio_pendiente;
	}
	public void setConvenio_pendiente(Long convenio_pendiente) {
		this.convenio_pendiente = convenio_pendiente;
	}
	
	@Override
	public String toString() {
		return "ReporteMonitoreoProgramaPorEstablecimientoVO [servicio="
				+ servicio + ", programa=" + programa + ", marco=" + marco
				+ ", remesa_monto=" + remesa_monto + ", remesa_porcentaje="
				+ remesa_porcentaje + ", convenio_monto=" + convenio_monto
				+ ", convenio_porcentaje=" + convenio_porcentaje
				+ ", convenio_pendiente=" + convenio_pendiente + "]";
	}
	
	
	public List<Object> getRow() {
		List<Object> row = new ArrayList<Object>();
		if(getServicio() != null){
			row.add(getServicio());
		}
		if(getPrograma() != null){
			row.add(getPrograma());
		}
		if(getMarco() != null){
			row.add(getMarco());
		}
		if(getRemesa_monto() != null){
			row.add(getRemesa_monto());
		}
		if(getRemesa_porcentaje() != null){
			row.add(getRemesa_porcentaje());
		}
		if(getConvenio_monto() != null){
			row.add(getConvenio_monto());
		}
		if(getConvenio_porcentaje() != null){
			row.add(getConvenio_porcentaje());
		}
		if(getConvenio_pendiente() != null){
			row.add(getConvenio_pendiente());
		}
		
		return row;
	}

}
