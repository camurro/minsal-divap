package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "dependencia_programa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DependenciaPrograma.findAll", query = "SELECT d FROM DependenciaPrograma d"),
    @NamedQuery(name = "DependenciaPrograma.findByIdDependenciaPrograma", query = "SELECT d FROM DependenciaPrograma d WHERE d.idDependenciaPrograma = :idDependenciaPrograma"),
    @NamedQuery(name = "DependenciaPrograma.findByNombre", query = "SELECT d FROM DependenciaPrograma d WHERE d.nombre = :nombre")})
public class DependenciaPrograma implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_dependencia_programa")
    private Integer idDependenciaPrograma;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dependencia")
    private Collection<Programa> programaCollection;

    public DependenciaPrograma() {
    }

    public DependenciaPrograma(Integer idDependenciaPrograma) {
        this.idDependenciaPrograma = idDependenciaPrograma;
    }

    public DependenciaPrograma(Integer idDependenciaPrograma, String nombre) {
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

    @XmlTransient
    public Collection<Programa> getProgramaCollection() {
        return programaCollection;
    }

    public void setProgramaCollection(Collection<Programa> programaCollection) {
        this.programaCollection = programaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDependenciaPrograma != null ? idDependenciaPrograma.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DependenciaPrograma)) {
            return false;
        }
        DependenciaPrograma other = (DependenciaPrograma) object;
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