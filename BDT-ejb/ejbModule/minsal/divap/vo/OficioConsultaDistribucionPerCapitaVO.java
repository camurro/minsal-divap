package minsal.divap.vo;

import java.util.ArrayList;
import java.util.List;

public class OficioConsultaDistribucionPerCapitaVO extends BaseVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1837081064094132212L;
	
	private String nombreRegion;
	private Double variacion; 

	public OficioConsultaDistribucionPerCapitaVO(Integer region, String nombreRegion,
			String nombreServicio, String nombreComuna, Double variacion) {
		super();
		setRegion(region);
		setNombreRegion(nombreRegion);
		setServicio(nombreServicio);
		setComuna(nombreComuna);
		setVariacion(variacion);
	}

	public String getNombreRegion() {
		return nombreRegion;
	}

	public void setNombreRegion(String nombreRegion) {
		this.nombreRegion = nombreRegion;
	}

	public Double getVariacion() {
		return variacion;
	}

	public void setVariacion(Double variacion) {
		this.variacion = variacion;
	}

	@Override
	public List<Object> getRow() {
		List<Object> row = new ArrayList<Object>();
		if(getRegion() != null){
			row.add(getRegion());
		}else{
			row.add("");
		}
		if(getNombreRegion() != null){
			row.add(getNombreRegion());
		}else{
			row.add("");
		}
		if(getServicio() != null){
			row.add(getServicio());
		}else{
			row.add("");
		}
		if(getComuna() != null){
			row.add(getComuna());
		}else{
			row.add("");
		}
		if(getVariacion() != null){
			row.add(getVariacion());
		}else{
			row.add("");
		}
		return row;
	}

}
