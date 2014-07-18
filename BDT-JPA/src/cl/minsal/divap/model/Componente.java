package cl.minsal.divap.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the componente database table.
 * 
 */
@Entity
@NamedQuery(name="Componente.findAll", query="SELECT c FROM Componente c")
public class Componente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id", unique=true, nullable=false)
	@GeneratedValue
	private Integer id;

	private String nombre;

	//bi-directional many-to-one association to Programa
	@ManyToOne
	@JoinColumn(name="id_programa")
	private Programa programa;

	public Componente() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Programa getPrograma() {
		return this.programa;
	}

	public void setPrograma(Programa programa) {
		this.programa = programa;
	}

}