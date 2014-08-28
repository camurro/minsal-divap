package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author cmurillo
 */
@Entity
@Table(name = "tipo_componente")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "TipoComponente.findAll", query = "SELECT t FROM TipoComponente t"),
	@NamedQuery(name = "TipoComponente.findById", query = "SELECT t FROM TipoComponente t WHERE t.id = :id"),
	@NamedQuery(name = "TipoComponente.findByNombre", query = "SELECT t FROM TipoComponente t WHERE t.nombre = :nombre")})
public class TipoComponente implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="id", unique=true, nullable=false)
	@GeneratedValue
	private Integer id;
	@Column(name = "nombre")
	private String nombre;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoComponente")
	private Set<Componente> componentes;
	@OneToMany(mappedBy = "idTipoPrograma")
	private Set<MetadataCore> metadataCoreCollection;

	public TipoComponente() {
	}

	public TipoComponente(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@XmlTransient
	public Set<Componente> getComponentes() {
		return componentes;
	}

	public void setComponentes(Set<Componente> componentes) {
		this.componentes = componentes;
	}

	@XmlTransient
	public Set<MetadataCore> getMetadataCoreCollection() {
		return metadataCoreCollection;
	}

	public void setMetadataCoreCollection(Set<MetadataCore> metadataCoreCollection) {
		this.metadataCoreCollection = metadataCoreCollection;
	}



	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof TipoComponente)) {
			return false;
		}
		TipoComponente other = (TipoComponente) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "cl.minsal.divap.model.TipoComponente[ id=" + id + " ]";
	}

}
