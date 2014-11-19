package cl.minsal.divap.model;

import java.io.Serializable;
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
@Table(name = "dia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dia.findAll", query = "SELECT d FROM Dia d"),
    @NamedQuery(name = "Dia.findById", query = "SELECT d FROM Dia d WHERE d.id = :id"),
    @NamedQuery(name = "Dia.findByDia", query = "SELECT d FROM Dia d WHERE d.dia = :dia")})
public class Dia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "dia")
    private Integer dia;
    @OneToMany(mappedBy = "dia")
    private Set<FechaRemesa> fechaRemesaSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dia")
    private Set<Festivos> festivosSet;

    public Dia() {
    }

    public Dia(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDia() {
        return dia;
    }

    public void setDia(Integer dia) {
        this.dia = dia;
    }

    @XmlTransient
    public Set<FechaRemesa> getFechaRemesaSet() {
        return fechaRemesaSet;
    }

    public void setFechaRemesaSet(Set<FechaRemesa> fechaRemesaSet) {
        this.fechaRemesaSet = fechaRemesaSet;
    }

    @XmlTransient
    public Set<Festivos> getFestivosSet() {
        return festivosSet;
    }

    public void setFestivosSet(Set<Festivos> festivosSet) {
        this.festivosSet = festivosSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dia)) {
            return false;
        }
        Dia other = (Dia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.Dia[ id=" + id + " ]";
    }
   
}