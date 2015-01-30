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
@Table(name = "estimacion_flujo_caja_consolidador_seguimiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstimacionFlujoCajaConsolidadorSeguimiento.findAll", query = "SELECT e FROM EstimacionFlujoCajaConsolidadorSeguimiento e"),
    @NamedQuery(name = "EstimacionFlujoCajaConsolidadorSeguimiento.findById", query = "SELECT e FROM EstimacionFlujoCajaConsolidadorSeguimiento e WHERE e.id = :id")})
public class EstimacionFlujoCajaConsolidadorSeguimiento implements Serializable {
    private static final long serialVersionUID = 1L; 
    @Id
    @Column(name = "id",unique=true, nullable=false)
    @GeneratedValue
    private Integer id;
    @JoinColumn(name = "seguimiento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Seguimiento seguimiento;
    @JoinColumn(name = "flujo_caja_consolidador", referencedColumnName = "id_flujo_caja_consolidador")
    @ManyToOne(optional = false)
    private FlujoCajaConsolidador flujoCajaConsolidador;

    public EstimacionFlujoCajaConsolidadorSeguimiento() {
    }

    public EstimacionFlujoCajaConsolidadorSeguimiento(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Seguimiento getSeguimiento() {
        return seguimiento;
    }

    public void setSeguimiento(Seguimiento seguimiento) {
        this.seguimiento = seguimiento;
    }

    public FlujoCajaConsolidador getFlujoCajaConsolidador() {
        return flujoCajaConsolidador;
    }

    public void setFlujoCajaConsolidador(FlujoCajaConsolidador flujoCajaConsolidador) {
        this.flujoCajaConsolidador = flujoCajaConsolidador;
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
        if (!(object instanceof EstimacionFlujoCajaConsolidadorSeguimiento)) {
            return false;
        }
        EstimacionFlujoCajaConsolidadorSeguimiento other = (EstimacionFlujoCajaConsolidadorSeguimiento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.EstimacionFlujoCajaConsolidadorSeguimiento[ id=" + id + " ]";
    }
    
}
