package cl.minsal.divap.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the rol database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Rol.findAll", query="SELECT r FROM Rol r"),
	@NamedQuery(name="Rol.findByNombre", query="SELECT r FROM Rol r WHERE r.nombre = :nombre")})
public class Rol implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@Column(name = "nombre")
	private String nombre;

	//bi-directional many-to-many association to Usuario
	@ManyToMany(mappedBy="rols")
	private List<Usuario> usuarios;

	public Rol() {
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Usuario> getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

}

