package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlTransient;


/**
 * The persistent class for the establecimiento database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name = "Establecimiento.findAll", query = "SELECT e FROM Establecimiento e"),
	@NamedQuery(name = "Establecimiento.findById", query = "SELECT e FROM Establecimiento e WHERE e.id = :idEstablecimiento"),
	@NamedQuery(name = "Establecimiento.findEstablecimientosByComuna", query = "SELECT e FROM Establecimiento e WHERE e.comuna.id = :idComuna"),
	@NamedQuery(name = "Establecimiento.findEstablecimientosByServicio", query = "SELECT e FROM Establecimiento e WHERE e.servicioSalud.id = :idServicio")})
public class Establecimiento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id", unique=true, nullable=false)
	@GeneratedValue
	private Integer id;

	private String nombre;

	@Column(name = "codigo")
	private String codigo;
	
	@Column(name = "tipo")
	private String tipo;

	//bi-directional many-to-one association to Comuna
	@ManyToOne
	@JoinColumn(name="id_comuna")
	private Comuna comuna;

	//bi-directional many-to-one association to ServicioSalud
	@ManyToOne
	@JoinColumn(name="id_servicio_salud")
	private ServicioSalud servicioSalud;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idestablecimiento")
	private Collection<Remesa> remesaCollection;


	@XmlTransient
	public Collection<Remesa> getRemesaCollection() {
		return remesaCollection;
	}

	public void setRemesaCollection(Collection<Remesa> remesaCollection) {
		this.remesaCollection = remesaCollection;
	}

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

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
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