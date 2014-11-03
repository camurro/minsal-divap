package minsal.divap.vo;

public class ComponenteCumplimientoVO {
	private String componente;
	private Double porcentajeCumplimiento;
	
	
	public String getComponente() {
		return componente;
	}
	public void setComponente(String componente) {
		this.componente = componente;
	}
	public Double getPorcentajeCumplimiento() {
		return porcentajeCumplimiento;
	}
	public void setPorcentajeCumplimiento(Double porcentajeCumplimiento) {
		this.porcentajeCumplimiento = porcentajeCumplimiento;
	}
	@Override
	public String toString() {
		return "ComponenteCumplimientoVO [componente=" + componente
				+ ", porcentajeCumplimiento=" + porcentajeCumplimiento + "]";
	}
}
