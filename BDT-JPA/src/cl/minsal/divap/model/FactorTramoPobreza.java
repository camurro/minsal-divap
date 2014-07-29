package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Set;

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

/**
 *
 * @author cmurillo
 */
@Entity
@Table(name = "factor_tramo_pobreza")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FactorTramoPobreza.findAll", query = "SELECT f FROM FactorTramoPobreza f"),
    @NamedQuery(name = "FactorTramoPobreza.findByIdFactorTramoPobreza", query = "SELECT f FROM FactorTramoPobreza f WHERE f.idFactorTramoPobreza = :idFactorTramoPobreza"),
    @NamedQuery(name = "FactorTramoPobreza.findByValor", query = "SELECT f FROM FactorTramoPobreza f WHERE f.valor = :valor")})
public class FactorTramoPobreza implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_factor_tramo_pobreza")
    private Integer idFactorTramoPobreza;
    @Basic(optional = false)
    @Column(name = "valor")
    private Double valor;
    @OneToMany(mappedBy = "tramoPobreza")
    private Set<AntecendentesComuna> antecendentesComunaCollection;

    public FactorTramoPobreza() {
    }

    public FactorTramoPobreza(Integer idFactorTramoPobreza) {
        this.idFactorTramoPobreza = idFactorTramoPobreza;
    }

    public FactorTramoPobreza(Integer idFactorTramoPobreza, Double valor) {
        this.idFactorTramoPobreza = idFactorTramoPobreza;
        this.valor = valor;
    }

    public Integer getIdFactorTramoPobreza() {
        return idFactorTramoPobreza;
    }

    public void setIdFactorTramoPobreza(Integer idFactorTramoPobreza) {
        this.idFactorTramoPobreza = idFactorTramoPobreza;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
    
    public Set<AntecendentesComuna> getAntecendentesComunaCollection() {
		return antecendentesComunaCollection;
	}

	public void setAntecendentesComunaCollection(
			Set<AntecendentesComuna> antecendentesComunaCollection) {
		this.antecendentesComunaCollection = antecendentesComunaCollection;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idFactorTramoPobreza != null ? idFactorTramoPobreza.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof FactorTramoPobreza)) {
            return false;
        }
        FactorTramoPobreza other = (FactorTramoPobreza) object;
        if ((this.idFactorTramoPobreza == null && other.idFactorTramoPobreza != null) || (this.idFactorTramoPobreza != null && !this.idFactorTramoPobreza.equals(other.idFactorTramoPobreza))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.FactorTramoPobreza[ idFactorTramoPobreza=" + idFactorTramoPobreza + " ]";
    }
    
}

