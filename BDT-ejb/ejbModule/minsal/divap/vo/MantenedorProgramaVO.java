package minsal.divap.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class MantenedorProgramaVO implements Serializable {

	private static final long serialVersionUID = 7346986726867582210L;
	
	private Integer idPrograma;
	private Integer idProgramaAno;
	private String nombrePrograma;
	private String nombreUsuario;
	private Integer cuotas;
	private List<MantenedorCuotasVO> listaCuotas;
	private String descripcion;
	private Boolean reliquidacion;
	private Boolean fonasa;

	
	public MantenedorProgramaVO(){
		
	}
	
	public MantenedorProgramaVO(Integer idPrograma, Integer idProgramaAno, String nombrePrograma, Integer cuotas, List<MantenedorCuotasVO> listaCuotas, String nombreUsuario, String descripcion, Boolean reliquidacion, Boolean fonasa){
		super();
		this.idPrograma = idPrograma;
		this.idProgramaAno = idProgramaAno;
		this.nombrePrograma = nombrePrograma;
		this.nombreUsuario = nombreUsuario;
		this.cuotas = cuotas;
		this.listaCuotas = listaCuotas;
		this.descripcion = descripcion;
		this.reliquidacion = reliquidacion;
		this.fonasa = fonasa;
	}

	public List<MantenedorCuotasVO> getListaCuotas() {
		return listaCuotas;
	}

	public void setListaCuotas(List<MantenedorCuotasVO> listaCuotas) {
		this.listaCuotas = listaCuotas;
	}

	public Integer getCuotas() {
		return cuotas;
	}

	public void setCuotas(Integer cuotas) {
		this.cuotas = cuotas;
	}

	public Integer getIdPrograma() {
		return idPrograma;
	}

	public void setIdPrograma(Integer idPrograma) {
		this.idPrograma = idPrograma;
	}

	public Integer getIdProgramaAno() {
		return idProgramaAno;
	}

	public void setIdProgramaAno(Integer idProgramaAno) {
		this.idProgramaAno = idProgramaAno;
	}

	public String getNombrePrograma() {
		return nombrePrograma;
	}

	public void setNombrePrograma(String nombrePrograma) {
		this.nombrePrograma = nombrePrograma;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Boolean getReliquidacion() {
		return reliquidacion;
	}

	public void setReliquidacion(Boolean reliquidacion) {
		this.reliquidacion = reliquidacion;
	}

	public Boolean getFonasa() {
		return fonasa;
	}

	public void setFonasa(Boolean fonasa) {
		this.fonasa = fonasa;
	}

	@Override
	public String toString() {
		return "MantenedorProgramaVO [idPrograma=" + idPrograma
				+ ", idProgramaAno=" + idProgramaAno + ", nombrePrograma="
				+ nombrePrograma + ", nombreUsuario=" + nombreUsuario
				+ ", cuotas=" + cuotas + ", listaCuotas=" + listaCuotas
				+ ", descripcion=" + descripcion + ", reliquidacion="
				+ reliquidacion + ", fonasa=" + fonasa + "]";
	}

}

