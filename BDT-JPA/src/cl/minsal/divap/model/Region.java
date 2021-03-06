package cl.minsal.divap.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the region database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Region.findAll", query="SELECT r FROM Region r order by r.id asc"),
	@NamedQuery(name="Region.findById", query="SELECT r FROM Region r WHERE r.id = :idRegion")})
public class Region implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id", unique=true, nullable=false)
	@GeneratedValue
	private Integer id;
	private String nombre;

	@JoinColumn(name = "secretario_regional", referencedColumnName = "id_persona")
	@ManyToOne(optional = false)
	private Persona secretarioRegional;

	//bi-directional many-to-one association to ServicioSalud
	@OneToMany(mappedBy="region")
	private List<ServicioSalud> servicioSaluds;

	public Region() {
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

	public Persona getSecretarioRegional() {
		return secretarioRegional;
	}

	public void setSecretarioRegional(Persona secretarioRegional) {
		this.secretarioRegional = secretarioRegional;
	}

	public List<ServicioSalud> getServicioSaluds() {
		return this.servicioSaluds;
	}

	public void setServicioSaluds(List<ServicioSalud> servicioSaluds) {
		this.servicioSaluds = servicioSaluds;
	}

	public ServicioSalud addServicioSalud(ServicioSalud servicioSalud) {
		getServicioSaluds().add(servicioSalud);
		servicioSalud.setRegion(this);

		return servicioSalud;
	}

	public ServicioSalud removeServicioSalud(ServicioSalud servicioSalud) {
		getServicioSaluds().remove(servicioSalud);
		servicioSalud.setRegion(null);

		return servicioSalud;
	}

}