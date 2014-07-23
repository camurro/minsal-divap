package cl.minsal.divap.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;

import java.util.List;
import java.util.Set;


/**
 * The persistent class for the usuario database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u"),
	@NamedQuery(name="Usuario.findRols", query="SELECT r FROM Usuario u JOIN u.rols r WHERE u.username = :username")})
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="username", unique=true, nullable=false)
	@GeneratedValue
	private String username;

	private String apellido;

	private String email;

	private String nombre;

	private String password;
	
	@OneToMany(mappedBy = "usuario")
	private Set<DistribucionInicialPercapita> distribucionInicialPercapitaCollection;

	//bi-directional many-to-one association to Programa
	@OneToMany(mappedBy="usuario")
	private List<Programa> programas;

	//bi-directional many-to-many association to Rol
	@ManyToMany
	@JoinTable(
			name="usuario_rol"
			, joinColumns={
					@JoinColumn(name="username_usuario")
			}
			, inverseJoinColumns={
					@JoinColumn(name="nombre_rol")
			}
			)
	private List<Rol> rols;

	public Usuario() {
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getApellido() {
		return this.apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Programa> getProgramas() {
		return this.programas;
	}

	public void setProgramas(List<Programa> programas) {
		this.programas = programas;
	}

	public Programa addPrograma(Programa programa) {
		getProgramas().add(programa);
		programa.setUsuario(this);

		return programa;
	}

	public Programa removePrograma(Programa programa) {
		getProgramas().remove(programa);
		programa.setUsuario(null);

		return programa;
	}

	public List<Rol> getRols() {
		return this.rols;
	}

	public void setRols(List<Rol> rols) {
		this.rols = rols;
	}
	
	@XmlTransient
	public Set<DistribucionInicialPercapita> getDistribucionInicialPercapitaCollection() {
		return distribucionInicialPercapitaCollection;
	}

	public void setDistribucionInicialPercapitaCollection(
			Set<DistribucionInicialPercapita> distribucionInicialPercapitaCollection) {
		this.distribucionInicialPercapitaCollection = distribucionInicialPercapitaCollection;
	}

}