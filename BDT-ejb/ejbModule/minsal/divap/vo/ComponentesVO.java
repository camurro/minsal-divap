package minsal.divap.vo;

import java.io.Serializable;
import java.util.List;

public class ComponentesVO implements Serializable{


	private static final long serialVersionUID = -1926890921516075410L;

	private Integer componente_id;
	private String nombre;
	private TipoComponenteVO tipoComponente;
	private List<SubtituloVO> subtitulos;
	
	public Integer getComponente_id() {
		return componente_id;
	}

	public void setComponente_id(Integer componente_id) {
		this.componente_id = componente_id;
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
	
}
