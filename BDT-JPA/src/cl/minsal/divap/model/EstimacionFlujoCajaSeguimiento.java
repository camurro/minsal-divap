package cl.minsal.divap.model;


import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Aldo
 */
@Entity
@Table(name = "estimacion_flujo_caja_seguimiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstimacionFlujoCajaSeguimiento.findAll", query = "SELECT e FROM EstimacionFlujoCajaSeguimiento e"),
    @NamedQuery(name = "EstimacionFlujoCajaSeguimiento.findById", query = "SELECT e FROM EstimacionFlujoCajaSeguimiento e WHERE e.id = :id")})
public class EstimacionFlujoCajaSeguimiento implements Serializable {
    @JoinColumn(name = "seguimiento", referencedColumnName = "id")
    @ManyToOne
    private Seguimiento seguimiento;
    @JoinColumn(name = "id_programa_ano", referencedColumnName = "id_programa_ano")
    @ManyToOne
    private ProgramaAno programaAno;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    public EstimacionFlujoCajaSeguimiento() {
    }

    public EstimacionFlujoCajaSeguimiento(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        if (!(object instanceof EstimacionFlujoCajaSeguimiento)) {
            return false;
        }
        EstimacionFlujoCajaSeguimiento other = (EstimacionFlujoCajaSeguimiento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication2.EstimacionFlujoCajaSeguimiento[ id=" + id + " ]";
    }

    public Seguimiento getSeguimiento() {
        return seguimiento;
    }

    public void setSeguimiento(Seguimiento seguimiento) {
        this.seguimiento = seguimiento;
    }

    public ProgramaAno getProgramaAno() {
        return programaAno;
    }

    public void setProgramaAno(ProgramaAno idProgramaAno) {
        this.programaAno = idProgramaAno;
    }
    
}
