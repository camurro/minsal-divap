package minsal.divap.vo;

import java.io.Serializable;
import java.util.List;

public class ComponentesVO implements Serializable{


	private static final long serialVersionUID = -1926890921516075410L;

	private Integer id;
	private String nombre;
	private TipoComponenteVO tipoComponente;
	private List<SubtituloVO> subtitulos;
	private Float peso;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<SubtituloVO> getSubtitulos() {
		return subtitulos;
	}

	public void setSubtitulos(List<SubtituloVO> subtitulos) {
		this.subtitulos = subtitulos;
	}

	public TipoComponenteVO getTipoComponente() {
		return tipoComponente;
	}

	public void setTipoComponente(TipoComponenteVO tipoComponente) {
		this.tipoComponente = tipoComponente;
	}

	public Float getPeso() {
		return peso;
	}

	public void setPeso(Float peso) {
		this.peso = peso;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		ComponentesVO other = (ComponentesVO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
