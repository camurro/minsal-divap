package minsal.divap.vo;

import java.io.Serializable;

public class MantenedorCumplimientoVO implements Serializable{

	private static final long serialVersionUID = -3845188034239295419L;
	
	private Integer idCumplimiento;
	private Integer idTramo;
	private String tramo;
	private Integer idTipoCumplimiento;
	private String tipoCumplimiento;
    private Double rebaja;
    private Double porcentajeDesde;
    private Double porcentajeHasta;
    
    public MantenedorCumplimientoVO(){
    	
    }
    
    public MantenedorCumplimientoVO(Integer idCumplimiento, Integer idTramo, String tramo, Integer idTipoCumplimiento, String tipoCumplimiento, Double rebaja, Double porcentajeDesde, Double porcentajeHasta){
    	super();
    	this.idCumplimiento = idCumplimiento;
    	this.idTramo = idTramo;
    	this.tramo = tramo;
    	this.idTipoCumplimiento = idTipoCumplimiento;
    	this.tipoCumplimiento = tipoCumplimiento;
        this.rebaja = rebaja;
        this.porcentajeDesde = porcentajeDesde;
        this.porcentajeHasta = porcentajeHasta;
    	
    }

	public Integer getIdCumplimiento() {
		return idCumplimiento;
	}

	public void setIdCumplimiento(Integer idCumplimiento) {
		this.idCumplimiento = idCumplimiento;
	}

	public Integer getIdTramo() {
		return idTramo;
	}

	public void setIdTramo(Integer idTramo) {
		this.idTramo = idTramo;
	}

	public String getTramo() {
		return tramo;
	}

	public void setTramo(String tramo) {
		this.tramo = tramo;
	}

	public Integer getIdTipoCumplimiento() {
		return idTipoCumplimiento;
	}

	public void setIdTipoCumplimiento(Integer idTipoCumplimiento) {
		this.idTipoCumplimiento = idTipoCumplimiento;
	}

	public String getTipoCumplimiento() {
		return tipoCumplimiento;
	}

	public void setTipoCumplimiento(String tipoCumplimiento) {
		this.tipoCumplimiento = tipoCumplimiento;
	}

	public Double getRebaja() {
		return rebaja;
	}

	public void setRebaja(Double rebaja) {
		this.rebaja = rebaja;
	}

	public Double getPorcentajeDesde() {
		return porcentajeDesde;
	}

	public void setPorcentajeDesde(Double porcentajeDesde) {
		this.porcentajeDesde = porcentajeDesde;
	}

	public Double getPorcentajeHasta() {
		return porcentajeHasta;
	}

	public void setPorcentajeHasta(Double porcentajeHasta) {
		this.porcentajeHasta = porcentajeHasta;
	}

	@Override
	public String toString() {
		return "MantenedorCumplimientoVO [idCumplimiento=" + idCumplimiento
				+ ", idTramo=" + idTramo + ", tramo=" + tramo
				+ ", idTipoCumplimiento=" + idTipoCumplimiento
				+ ", tipoCumplimiento=" + tipoCumplimiento + ", rebaja="
				+ rebaja + ", porcentajeDesde=" + porcentajeDesde
				+ ", porcentajeHasta=" + porcentajeHasta + "]";
	}

}
