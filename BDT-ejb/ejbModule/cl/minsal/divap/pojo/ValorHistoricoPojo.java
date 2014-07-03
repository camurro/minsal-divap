package cl.minsal.divap.pojo;

public class ValorHistoricoPojo {
	private Long valorHistoricoS21 = 0L;
	private Double inflactorS21 = 0D;
	private Double totalS21 = 0D;
	
	private Long valorHistoricoS22 = 0L;
	private Double inflactorS22 = 0D;
	private Double totalS22 = 0D;
	
	private Long valorHistoricoS24 = 0L;
	private Double inflactorS24 = 0D;
	private Double totalS24 = 0D;
	
	private Long valorHistoricoS29 = 0L;
	private Double inflactorS29 = 0D;
	private Double totalS29 = 0D;
	
	private String servicioSalud;
	private String comuna;
	
	public Long getValorHistoricoS21() {
		return valorHistoricoS21;
	}
	
	public void setValorHistoricoS21( Long valorHistoricoS21 ) {
		this.valorHistoricoS21 = valorHistoricoS21;
		if(!this.valorHistoricoS21.equals(null)&&!this.valorHistoricoS21.equals("")&&
						!this.inflactorS21.equals(null)&&!this.inflactorS21.equals("")){
			this.totalS21 = this.valorHistoricoS21 * this.inflactorS21;
		}else{
			this.totalS21 = 0D;
		}
	}
	
	public Double getInflactorS21() {
		return inflactorS21;
	}
	
	public void setInflactorS21( Double inflactorS21 ) {
		this.inflactorS21 = inflactorS21;
		if(!this.valorHistoricoS21.equals(null)&&!this.valorHistoricoS21.equals("")&&
						!this.inflactorS21.equals(null)&&!this.inflactorS21.equals("")){
			this.totalS21 = this.valorHistoricoS21 * this.inflactorS21;
		}else{
			this.totalS21 = 0D;
		}
	}
	
	public Double getTotalS21() {
		return totalS21;
	}
	
	public void setTotalS21( Double totalS21 ) {
		this.totalS21 = totalS21;
	}
	
	public Long getValorHistoricoS22() {
		return valorHistoricoS22;
	}
	
	public void setValorHistoricoS22( Long valorHistoricoS22 ) {
		this.valorHistoricoS22 = valorHistoricoS22;
		if(!this.valorHistoricoS22.equals(null)&&!this.valorHistoricoS22.equals("")&&
						!this.inflactorS22.equals(null)&&!this.inflactorS22.equals("")){
			this.totalS22 = this.valorHistoricoS22 * this.inflactorS22;
		}else{
			this.totalS22 = 0D;
		}
	}
	
	public Double getInflactorS22() {
		return inflactorS22;
	}
	
	public void setInflactorS22( Double inflactorS22 ) {
		this.inflactorS22 = inflactorS22;
		if(!this.valorHistoricoS22.equals(null)&&!this.valorHistoricoS22.equals("")&&
						!this.inflactorS22.equals(null)&&!this.inflactorS22.equals("")){
			this.totalS22 = this.valorHistoricoS22 * this.inflactorS22;
		}else{
			this.totalS22 = 0D;
		}
	}
	
	public Double getTotalS22() {
		return totalS22;
	}
	
	public void setTotalS22( Double totalS22 ) {
		this.totalS22 = totalS22;
	}
	
	public Long getValorHistoricoS24() {
		return valorHistoricoS24;
	}
	
	public void setValorHistoricoS24( Long valorHistoricoS24 ) {
		this.valorHistoricoS24 = valorHistoricoS24;
		if(!this.valorHistoricoS24.equals(null)&&!this.valorHistoricoS24.equals("")&&
						!this.inflactorS24.equals(null)&&!this.inflactorS24.equals("")){
			this.totalS24 = this.valorHistoricoS24 * this.inflactorS24;
		}else{
			this.totalS24 = 0D;
		}
	}
	
	public Double getInflactorS24() {
		return inflactorS24;
	}
	
	public void setInflactorS24( Double inflactorS24 ) {
		this.inflactorS24 = inflactorS24;
		if(!this.valorHistoricoS24.equals(null)&&!this.valorHistoricoS24.equals("")&&
						!this.inflactorS24.equals(null)&&!this.inflactorS24.equals("")){
			this.totalS24 = this.valorHistoricoS24 * this.inflactorS24;
		}else{
			this.totalS24 = 0D;
		}
	}
	
	public Double getTotalS24() {
		return totalS24;
	}
	
	public void setTotalS24( Double totalS24 ) {
		this.totalS24 = totalS24;
	}
	
	public Long getValorHistoricoS29() {
		return valorHistoricoS29;
	}
	
	public void setValorHistoricoS29( Long valorHistoricoS29 ) {
		this.valorHistoricoS29 = valorHistoricoS29;
		if(!this.valorHistoricoS29.equals(null)&&!this.valorHistoricoS29.equals("")&&
						!this.inflactorS29.equals(null)&&!this.inflactorS29.equals("")){
			this.totalS29 = this.valorHistoricoS29 * this.inflactorS29;
		}else{
			this.totalS29 = 0D;
		}
	}
	
	public Double getInflactorS29() {
		return inflactorS29;
	}
	
	public void setInflactorS29( Double inflactorS29 ) {
		this.inflactorS29 = inflactorS29;
		if(!this.valorHistoricoS29.equals(null)&&!this.valorHistoricoS29.equals("")&&
						!this.inflactorS29.equals(null)&&!this.inflactorS29.equals("")){
			this.totalS29 = this.valorHistoricoS29 * this.inflactorS29;
		}else{
			this.totalS29 = 0D;
		}
	}
	
	public Double getTotalS29() {
		return totalS29;
	}
	
	public void setTotalS29( Double totalS29 ) {
		this.totalS29 = totalS29;
	}
	
	public String getServicioSalud() {
		return servicioSalud;
	}
	
	public void setServicioSalud( String servicioSalud ) {
		this.servicioSalud = servicioSalud;
	}
	
	public String getComuna() {
		return comuna;
	}
	
	public void setComuna( String comuna ) {
		this.comuna = comuna;
	}
	
}
