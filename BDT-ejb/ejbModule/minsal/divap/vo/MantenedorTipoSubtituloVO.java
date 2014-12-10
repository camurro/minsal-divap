package minsal.divap.vo;

import java.io.Serializable;

public class MantenedorTipoSubtituloVO implements Serializable{

	private static final long serialVersionUID = -7975890038326594921L;
	private Integer idSubtitulo;
	private String nombreSubtitulo;
	private Integer idDependencia;
	private String nombreDependencia;
	private Double inflactor;
	
	public MantenedorTipoSubtituloVO(){
		
	}
	
	public MantenedorTipoSubtituloVO(Integer idSubtitulo, String nombreSubtitulo, Integer idDependencia, String nombreDependencia, Double inflactor){
		super();
		this. idSubtitulo = idSubtitulo;
		this. nombreSubtitulo = nombreSubtitulo;
		this. idDependencia = idDependencia;
		this. nombreDependencia = nombreDependencia;
		this. inflactor = inflactor;
	}

	public Integer getIdSubtitulo() {
		return idSubtitulo;
	}

	public void setIdSubtitulo(Integer idSubtitulo) {
		this.idSubtitulo = idSubtitulo;
	}

	public String getNombreSubtitulo() {
		return nombreSubtitulo;
	}

	public void setNombreSubtitulo(String nombreSubtitulo) {
		this.nombreSubtitulo = nombreSubtitulo;
	}

	public Integer getIdDependencia() {
		return idDependencia;
	}

	public void setIdDependencia(Integer idDependencia) {
		this.idDependencia = idDependencia;
	}

	public String getNombreDependencia() {
		return nombreDependencia;
	}

	public void setNombreDependencia(String nombreDependencia) {
		this.nombreDependencia = nombreDependencia;
	}

	public Double getInflactor() {
		return inflactor;
	}

	public void setInflactor(Double inflactor) {
		this.inflactor = inflactor;
	}

	@Override
	public String toString() {
		return "MantenedorTipoSubtitulo [idSubtitulo=" + idSubtitulo
				+ ", nombreSubtitulo=" + nombreSubtitulo + ", idDependencia="
				+ idDependencia + ", nombreDependencia=" + nombreDependencia
				+ ", inflactor=" + inflactor + "]";
	}

	
}
