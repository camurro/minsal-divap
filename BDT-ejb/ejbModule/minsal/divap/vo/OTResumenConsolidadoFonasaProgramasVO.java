package minsal.divap.vo;

import java.io.Serializable;
import java.util.List;


public class OTResumenConsolidadoFonasaProgramasVO implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer idPrograma;
	private String nombre;
	private long monto;
	private ServiciosVO servicioVO;
	private boolean revisaFonasa;
	
	public Integer getIdPrograma() {
		return idPrograma;
	}
	public void setIdPrograma(Integer idPrograma) {
		this.idPrograma = idPrograma;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public ServiciosVO getServicioVO() {
		return servicioVO;
	}
	public void setServicioVO(ServiciosVO servicioVO) {
		this.servicioVO = servicioVO;
	}
	public long getMonto() {
		return monto;
	}
	public void setMonto(long monto) {
		this.monto = monto;
	}
	public boolean isRevisaFonasa() {
		return revisaFonasa;
	}
	public void setRevisaFonasa(boolean revisaFonasa) {
		this.revisaFonasa = revisaFonasa;
	}
	
	
}