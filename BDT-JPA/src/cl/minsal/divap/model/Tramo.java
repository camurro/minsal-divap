package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Set;

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
@Table(name = "tramo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tramo.findAll", query = "SELECT t FROM Tramo t"),
    @NamedQuery(name = "Tramo.findByIdTramo", query = "SELECT t FROM Tramo t WHERE t.idTramo = :idTramo"),
    @NamedQuery(name = "Tramo.findByTramo", query = "SELECT t FROM Tramo t WHERE t.tramo = :tramo")})
public class Tramo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
	@Column(name="id_tramo", unique=true, nullable=false)
	@GeneratedValue
    private Integer idTramo;
    @Basic(optional = false)
    @Column(name = "tramo")
    private String tramo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tramo")
    private Set<Cumplimiento> cumplimientoCollection;

    public Tramo() {
    }

    public Tramo(Integer idTramo) {
        this.idTramo = idTramo;
    }

    public Tramo(Integer idTramo, String tramo) {
        this.idTramo = idTramo;
        this.tramo = tramo;
    }

    public Integer getIdTramo() {
        return idTramo;
    }

    public void setIdTramo(Integer idTramo) {
        this.idTramo = idTramo;
    }

    public String getTramo() {
        return tramo;
    }

    public void setTramo(String tramo) {
        this.tramo = tramo;
    }

    @XmlTransient
    public Set<Cumplimiento> getCumplimientoCollection() {
        return cumplimientoCollection;
    }

    public void setCumplimientoCollection(Set<Cumplimiento> cumplimientoCollection) {
        this.cumplimientoCollection = cumplimientoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTramo != null ? idTramo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tramo)) {
            return false;
        }
        Tramo other = (Tramo) object;
        if ((this.idTramo == null && other.idTramo != null) || (this.idTramo != null && !this.idTramo.equals(other.idTramo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.Tramo[ idTramo=" + idTramo + " ]";
    }
    
}
