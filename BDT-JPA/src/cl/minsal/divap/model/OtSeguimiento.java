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
 * @author LeandroSuarez
 */
@Entity
@Table(name = "ot_seguimiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OtSeguimiento.findAll", query = "SELECT o FROM OtSeguimiento o"),
    @NamedQuery(name = "OtSeguimiento.findByIdOtSeguimiento", query = "SELECT o FROM OtSeguimiento o WHERE o.idOtSeguimiento = :idOtSeguimiento")})
public class OtSeguimiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_ot_seguimiento")
    private Integer idOtSeguimiento;
    @JoinColumn(name = "seguimiento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Seguimiento seguimiento;
    @JoinColumn(name = "orden_transferencia", referencedColumnName = "id_orden_transferencia")
    @ManyToOne(optional = false)
    private OrdenTransferencia ordenTransferencia;

    public OtSeguimiento() {
    }

    public OtSeguimiento(Integer idOtSeguimiento) {
        this.idOtSeguimiento = idOtSeguimiento;
    }

    public Integer getIdOtSeguimiento() {
        return idOtSeguimiento;
    }

    public void setIdOtSeguimiento(Integer idOtSeguimiento) {
        this.idOtSeguimiento = idOtSeguimiento;
    }

    public Seguimiento getSeguimiento() {
        return seguimiento;
    }

    public void setSeguimiento(Seguimiento seguimiento) {
        this.seguimiento = seguimiento;
    }

    public OrdenTransferencia getOrdenTransferencia() {
        return ordenTransferencia;
    }

    public void setOrdenTransferencia(OrdenTransferencia ordenTransferencia) {
        this.ordenTransferencia = ordenTransferencia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idOtSeguimiento != null ? idOtSeguimiento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtSeguimiento)) {
            return false;
        }
        OtSeguimiento other = (OtSeguimiento) object;
        if ((this.idOtSeguimiento == null && other.idOtSeguimiento != null) || (this.idOtSeguimiento != null && !this.idOtSeguimiento.equals(other.idOtSeguimiento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "examples.OtSeguimiento[ idOtSeguimiento=" + idOtSeguimiento + " ]";
    }
    
}
