package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

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
@Table(name = "mes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Mes.findAll", query = "SELECT m FROM Mes m order by m.idMes ASC"),
    @NamedQuery(name = "Mes.findByIdMes", query = "SELECT m FROM Mes m WHERE m.idMes = :idMes"),
    @NamedQuery(name = "Mes.findByNombre", query = "SELECT m FROM Mes m WHERE m.nombre = :nombre")})
public class Mes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_mes")
    private Integer idMes;
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(mappedBy = "idMes")
    private Collection<ComunaCumplimiento> comunaCumplimientoCollection;
    @OneToMany(mappedBy = "mes")
    private Set<DetalleRemesas> detalleRemesasSet;
    
    public Mes() {
    }

    public Mes(Integer idMes) {
        this.idMes = idMes;
    }

    public Integer getIdMes() {
        return idMes;
    }

    public void setIdMes(Integer idMes) {
        this.idMes = idMes;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idmes")
    private Collection<Remesa> remesaCollection;


    @XmlTransient
       public Collection<Remesa> getRemesaCollection() {
           return remesaCollection;
       }

       public void setRemesaCollection(Collection<Remesa> remesaCollection) {
           this.remesaCollection = remesaCollection;
       }


    @XmlTransient
    public Collection<ComunaCumplimiento> getComunaCumplimientoCollection() {
        return comunaCumplimientoCollection;
    }

    public void setComunaCumplimientoCollection(Collection<ComunaCumplimiento> comunaCumplimientoCollection) {
        this.comunaCumplimientoCollection = comunaCumplimientoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMes != null ? idMes.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Mes)) {
            return false;
        }
        Mes other = (Mes) object;
        if ((this.idMes == null && other.idMes != null) || (this.idMes != null && !this.idMes.equals(other.idMes))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.Mes[ idMes=" + idMes + " ]";
    }
    @XmlTransient
    public Set<DetalleRemesas> getDetalleRemesasSet() {
        return detalleRemesasSet;
    }

    public void setDetalleRemesasSet(Set<DetalleRemesas> detalleRemesasSet) {
        this.detalleRemesasSet = detalleRemesasSet;
    }
}

