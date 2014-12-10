package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
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
@Table(name = "factor_ref_asig_zona")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FactorRefAsigZona.findAll", query = "SELECT f FROM FactorRefAsigZona f"),
    @NamedQuery(name = "FactorRefAsigZona.findByIdFactorRefAsigZona", query = "SELECT f FROM FactorRefAsigZona f WHERE f.idFactorRefAsigZona = :idFactorRefAsigZona"),
    @NamedQuery(name = "FactorRefAsigZona.findByZona", query = "SELECT f FROM FactorRefAsigZona f WHERE f.zona = :zona"),
    @NamedQuery(name = "FactorRefAsigZona.findByValor", query = "SELECT f FROM FactorRefAsigZona f WHERE f.valor = :valor")})
public class FactorRefAsigZona implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
	@Column(name="id_factor_ref_asig_zona", unique=true, nullable=false)
	@GeneratedValue
    private Integer idFactorRefAsigZona;
    @Basic(optional = false)
    @Column(name = "zona")
    private short zona;
    @Basic(optional = false)
    @Column(name = "valor")
    private Double valor;
    @OneToMany(mappedBy = "asignacionZona")
    private Collection<AntecendentesComuna> antecendentesComunaCollection;

    public FactorRefAsigZona() {
    }

    public FactorRefAsigZona(Integer idFactorRefAsigZona) {
        this.idFactorRefAsigZona = idFactorRefAsigZona;
    }

    public FactorRefAsigZona(Integer idFactorRefAsigZona, short zona, Double valor) {
        this.idFactorRefAsigZona = idFactorRefAsigZona;
        this.zona = zona;
        this.valor = valor;
    }

    public Integer getIdFactorRefAsigZona() {
        return idFactorRefAsigZona;
    }

    public void setIdFactorRefAsigZona(Integer idFactorRefAsigZona) {
        this.idFactorRefAsigZona = idFactorRefAsigZona;
    }

    public short getZona() {
        return zona;
    }

    public void setZona(short zona) {
        this.zona = zona;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    @XmlTransient
    public Collection<AntecendentesComuna> getAntecendentesComunaCollection() {
        return antecendentesComunaCollection;
    }

    public void setAntecendentesComunaCollection(Collection<AntecendentesComuna> antecendentesComunaCollection) {
        this.antecendentesComunaCollection = antecendentesComunaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFactorRefAsigZona != null ? idFactorRefAsigZona.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof FactorRefAsigZona)) {
            return false;
        }
        FactorRefAsigZona other = (FactorRefAsigZona) object;
        if ((this.idFactorRefAsigZona == null && other.idFactorRefAsigZona != null) || (this.idFactorRefAsigZona != null && !this.idFactorRefAsigZona.equals(other.idFactorRefAsigZona))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.FactorRefAsigZona[ idFactorRefAsigZona=" + idFactorRefAsigZona + " ]";
    }
    
}

