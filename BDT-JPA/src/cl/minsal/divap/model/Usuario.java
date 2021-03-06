package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlTransient;


/**
 * The persistent class for the usuario database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u"),
	@NamedQuery(name="Usuario.findNoEliminados", query="SELECT u FROM Usuario u WHERE u.estado.idEstadoUsuario <> :idEstadoUsuario"),
	@NamedQuery(name="Usuario.findRols", query="SELECT r FROM Usuario u JOIN u.rols r WHERE u.username = :username")})
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@Column(name = "username")
	private String username;

	private String apellido;

	private String nombre;

	private String password;

	@JoinColumn(name = "email", referencedColumnName = "id_email")
	@ManyToOne
	private Email email;

	@OneToMany(mappedBy = "usuario")
	private Set<DistribucionInicialPercapita> distribucionInicialPercapitaCollection;
	@JoinColumn(name = "servicio", referencedColumnName = "id")
	@ManyToOne
	private ServicioSalud servicio;
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
	@JoinColumn(name = "estado", referencedColumnName = "id_estado_usuario")
    @ManyToOne(optional = false)
    private EstadoUsuario estado;

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

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
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

	public ServicioSalud getServicio() {
		return servicio;
	}

	public void setServicio(ServicioSalud servicio) {
		this.servicio = servicio;
	}

	@XmlTransient
	public Set<DistribucionInicialPercapita> getDistribucionInicialPercapitaCollection() {
		return distribucionInicialPercapitaCollection;
	}

	public void setDistribucionInicialPercapitaCollection(
			Set<DistribucionInicialPercapita> distribucionInicialPercapitaCollection) {
		this.distribucionInicialPercapitaCollection = distribucionInicialPercapitaCollection;
	}

	public EstadoUsuario getEstado() {
		return estado;
	}

	public void setEstado(EstadoUsuario estado) {
		this.estado = estado;
	}

	@Override
	public String toString() {
		return "Usuario [username=" + username + ", apellido=" + apellido
				+ ", nombre=" + nombre + ", password=" + password + ", email="
				+ email + ", distribucionInicialPercapitaCollection="
				+ distribucionInicialPercapitaCollection + ", servicio="
				+ servicio + ", programas=" + programas + ", rols=" + rols
				+ "]";
	}

}