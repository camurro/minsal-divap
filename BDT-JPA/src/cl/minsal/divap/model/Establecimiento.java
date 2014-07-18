package cl.minsal.divap.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the establecimiento database table.
 * 
 */
@Entity
@NamedQuery(name="Establecimiento.findAll", query="SELECT e FROM Establecimiento e")
public class Establecimiento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id", unique=true, nullable=false)
	@GeneratedValue
	private Integer id;

	private String nombre;

	//bi-directional many-to-one association to Comuna
	@ManyToOne
	@JoinColumn(name="id_comuna")
	private Comuna comuna;

	//bi-directional many-to-one association to ServicioSalud
	@ManyToOne
	@JoinColumn(name="id_servicio_salud")
	private ServicioSalud servicioSalud;

	public Establecimiento() {
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

	public Comuna getComuna() {
		return this.comuna;
	}

	public void setComuna(Comuna comuna) {
		this.comuna = comuna;
	}

	public ServicioSalud getServicioSalud() {
		return this.servicioSalud;
	}

	public void setServicioSalud(ServicioSalud servicioSalud) {
		this.servicioSalud = servicioSalud;
	}

}