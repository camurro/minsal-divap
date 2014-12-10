package minsal.divap.vo;

import java.io.Serializable;

public class MantenedorEstadoProgramaVO implements Serializable{

	private static final long serialVersionUID = 6578114049090007398L;
	private Integer id_estado_programa;
	private String nombre_estado;
	
	public MantenedorEstadoProgramaVO(){
		
	}
	
	public MantenedorEstadoProgramaVO(Integer id_estado_programa, String nombre_estado){
		super();
		this.id_estado_programa = id_estado_programa;
		this.nombre_estado = nombre_estado;
	}

	public Integer getId_estado_programa() {
		return id_estado_programa;
	}

	public void setId_estado_programa(Integer id_estado_programa) {
		this.id_estado_programa = id_estado_programa;
	}

	public String getNombre_estado() {
		return nombre_estado;
	}

	public void setNombre_estado(String nombre_estado) {
		this.nombre_estado = nombre_estado;
	}

	@Override
	public String toString() {
		return "MantenedorEstadoPrograma [id_estado_programa="
				+ id_estado_programa + ", nombre_estado=" + nombre_estado + "]";
	}
	
	

}
