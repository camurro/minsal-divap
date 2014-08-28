package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
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
@Table(name = "dependencia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DependenciaPrograma.findAll", query = "SELECT d FROM Dependencia d"),
    @NamedQuery(name = "DependenciaPrograma.findByIdDependenciaPrograma", query = "SELECT d FROM Dependencia d WHERE d.idDependenciaPrograma = :idDependenciaPrograma"),
    @NamedQuery(name = "DependenciaPrograma.findByNombre", query = "SELECT d FROM Dependencia d WHERE d.nombre = :nombre")})
public class Dependencia implements Serializable {
    private static final long serialVersionUID = 1L;
	@Id
	@Column(name="id_dependencia_programa", unique=true, nullable=false)
	@GeneratedValue
    private Integer idDependenciaPrograma;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dependencia")
    private Collection<TipoSubtitulo> tipoSubtitulos;

    public Dependencia() {
    }

    public Dependencia(Integer idDependenciaPrograma) {
        this.idDependenciaPrograma = idDependenciaPrograma;
    }

    public Dependencia(Integer idDependenciaPrograma, String nombre) {
        this.idDependenciaPrograma = idDependenciaPrograma;
        this.nombre = nombre;
    }

    public Integer getIdDependenciaPrograma() {
        return idDependenciaPrograma;
    }

    public void setIdDependenciaPrograma(Integer idDependenciaPrograma) {
        this.idDependenciaPrograma = idDependenciaPrograma;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDependenciaPrograma != null ? idDependenciaPrograma.hashCode() : 0);
        return hash;
    }
    
    @XmlTransient
    public Collection<TipoSubtitulo> getTipoSubtitulos() {
		return tipoSubtitulos;
	}

	public void setTipoSubtitulos(Collection<TipoSubtitulo> tipoSubtitulos) {
		this.tipoSubtitulos = tipoSubtitulos;
	}

	@Override
    public boolean equals(Object object) {
        if (!(object instanceof Dependencia)) {
            return false;
        }
        Dependencia other = (Dependencia) object;
        if ((this.idDependenciaPrograma == null && other.idDependenciaPrograma != null) || (this.idDependenciaPrograma != null && !this.idDependenciaPrograma.equals(other.idDependenciaPrograma))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.DependenciaPrograma[ idDependenciaPrograma=" + idDependenciaPrograma + " ]";
    }
    
}