package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;


/**
 * The persistent class for the servicio_salud database table.
 * 
 */
@Entity
@Table(name="servicio_salud")
@NamedQueries({
	@NamedQuery(name="ServicioSalud.findAll", query="SELECT s FROM ServicioSalud s"),
	@NamedQuery(name="ServicioSalud.findServiciosByRegion", query="SELECT s FROM ServicioSalud s WHERE s.region.id = :idRegion order by s.id asc"),
	 @NamedQuery(name = "ServicioSalud.findByIdServicioSalud", query = "SELECT s FROM ServicioSalud s WHERE s.id = :idServicioSalud")})
public class ServicioSalud implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id", unique=true, nullable=false)
	@GeneratedValue
	private Integer id;

	private String nombre;

	//bi-directional many-to-one association to Comuna
	@OneToMany(mappedBy="servicioSalud")
	private List<Comuna> comunas;

	//bi-directional many-to-one association to Establecimiento
	@OneToMany(mappedBy="servicioSalud")
	private List<Establecimiento> establecimientos;

	//bi-directional many-to-one association to MarcoPresupuestario
	@OneToMany(mappedBy="servicioSalud")
	private List<MarcoPresupuestario> marcoPresupuestarios;

	//bi-directional many-to-one association to Region
	@ManyToOne
	@JoinColumn(name="id_region")
	private Region region;
	
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idServicioSalud")
	private Collection<Remesa> remesaCollection;

	public ServicioSalud() {
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

	public List<Comuna> getComunas() {
		return this.comunas;
	}

	public void setComunas(List<Comuna> comunas) {
		this.comunas = comunas;
	}

	
	public List<Establecimiento> getEstablecimientos() {
		return this.establecimientos;
	}

	public void setEstablecimientos(List<Establecimiento> establecimientos) {
		this.establecimientos = establecimientos;
	}

	public Establecimiento addEstablecimiento(Establecimiento establecimiento) {
		getEstablecimientos().add(establecimiento);
		establecimiento.setServicioSalud(this);

		return establecimiento;
	}

	public Establecimiento removeEstablecimiento(Establecimiento establecimiento) {
		getEstablecimientos().remove(establecimiento);
		establecimiento.setServicioSalud(null);

		return establecimiento;
	}

	public List<MarcoPresupuestario> getMarcoPresupuestarios() {
		return this.marcoPresupuestarios;
	}

	public void setMarcoPresupuestarios(List<MarcoPresupuestario> marcoPresupuestarios) {
		this.marcoPresupuestarios = marcoPresupuestarios;
	}

	public MarcoPresupuestario addMarcoPresupuestario(MarcoPresupuestario marcoPresupuestario) {
		getMarcoPresupuestarios().add(marcoPresupuestario);
		marcoPresupuestario.setServicioSalud(this);

		return marcoPresupuestario;
	}

	public MarcoPresupuestario removeMarcoPresupuestario(MarcoPresupuestario marcoPresupuestario) {
		getMarcoPresupuestarios().remove(marcoPresupuestario);
		marcoPresupuestario.setServicioSalud(null);

		return marcoPresupuestario;
	}

	public Region getRegion() {
		return this.region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}
	
	@XmlTransient
   public Collection<Remesa> getRemesaCollection() {
       return remesaCollection;
   }

   public void setRemesaCollection(Collection<Remesa> remesaCollection) {
       this.remesaCollection = remesaCollection;
   }

}