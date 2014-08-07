package cl.minsal.divap.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cmurillo
 */
@Entity
@Table(name = "distribucion_inicial_percapita_seguimiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DistribucionInicialPercapitaSeguimiento.findAll", query = "SELECT d FROM DistribucionInicialPercapitaSeguimiento d"),
    @NamedQuery(name = "DistribucionInicialPercapitaSeguimiento.findByIdDistribucionInicialPercapitaSeguimiento", query = "SELECT d FROM DistribucionInicialPercapitaSeguimiento d WHERE d.idDistribucionInicialPercapitaSeguimiento = :idDistribucionInicialPercapitaSeguimiento")})
public class DistribucionInicialPercapitaSeguimiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
  	@Column(name="id_distribucion_inicial_percapita_seguimiento", unique=true, nullable=false)
  	@GeneratedValue
    private Integer idDistribucionInicialPercapitaSeguimiento;
    @JoinColumn(name = "seguimiento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Seguimiento seguimiento;
    @JoinColumn(name = "distribucion_inicial_percapita", referencedColumnName = "id_distribucion_inicial_percapita")
    @ManyToOne(optional = false)
    private DistribucionInicialPercapita distribucionInicialPercapita;

    public DistribucionInicialPercapitaSeguimiento() {
    }

    public DistribucionInicialPercapitaSeguimiento(Integer idDistribucionInicialPercapitaSeguimiento) {
        this.idDistribucionInicialPercapitaSeguimiento = idDistribucionInicialPercapitaSeguimiento;
    }

    public Integer getIdDistribucionInicialPercapitaSeguimiento() {
        return idDistribucionInicialPercapitaSeguimiento;
    }

    public void setIdDistribucionInicialPercapitaSeguimiento(Integer idDistribucionInicialPercapitaSeguimiento) {
        this.idDistribucionInicialPercapitaSeguimiento = idDistribucionInicialPercapitaSeguimiento;
    }

    public Seguimiento getSeguimiento() {
        return seguimiento;
    }

    public void setSeguimiento(Seguimiento seguimiento) {
        this.seguimiento = seguimiento;
    }

    public DistribucionInicialPercapita getDistribucionInicialPercapita() {
        return distribucionInicialPercapita;
    }

    public void setDistribucionInicialPercapita(DistribucionInicialPercapita distribucionInicialPercapita) {
        this.distribucionInicialPercapita = distribucionInicialPercapita;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDistribucionInicialPercapitaSeguimiento != null ? idDistribucionInicialPercapitaSeguimiento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DistribucionInicialPercapitaSeguimiento)) {
            return false;
        }
        DistribucionInicialPercapitaSeguimiento other = (DistribucionInicialPercapitaSeguimiento) object;
        if ((this.idDistribucionInicialPercapitaSeguimiento == null && other.idDistribucionInicialPercapitaSeguimiento != null) || (this.idDistribucionInicialPercapitaSeguimiento != null && !this.idDistribucionInicialPercapitaSeguimiento.equals(other.idDistribucionInicialPercapitaSeguimiento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.DistribucionInicialPercapitaSeguimiento[ idDistribucionInicialPercapitaSeguimiento=" + idDistribucionInicialPercapitaSeguimiento + " ]";
    }
    
}
