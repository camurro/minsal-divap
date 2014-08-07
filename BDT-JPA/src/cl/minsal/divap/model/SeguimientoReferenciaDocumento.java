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
@Table(name = "seguimiento_referencia_documento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SeguimientoReferenciaDocumento.findAll", query = "SELECT s FROM SeguimientoReferenciaDocumento s"),
    @NamedQuery(name = "SeguimientoReferenciaDocumento.findByIdSeguimientoReferenciaDocumento", query = "SELECT s FROM SeguimientoReferenciaDocumento s WHERE s.idSeguimientoReferenciaDocumento = :idSeguimientoReferenciaDocumento")})
public class SeguimientoReferenciaDocumento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
  	@Column(name="id_seguimiento_referencia_documento", unique=true, nullable=false)
  	@GeneratedValue
    private Integer idSeguimientoReferenciaDocumento;
    @JoinColumn(name = "id_seguimiento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Seguimiento idSeguimiento;
    @JoinColumn(name = "id_referencia_documento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ReferenciaDocumento idReferenciaDocumento;

    public SeguimientoReferenciaDocumento() {
    }

    public SeguimientoReferenciaDocumento(Integer idSeguimientoReferenciaDocumento) {
        this.idSeguimientoReferenciaDocumento = idSeguimientoReferenciaDocumento;
    }

    public Integer getIdSeguimientoReferenciaDocumento() {
        return idSeguimientoReferenciaDocumento;
    }

    public void setIdSeguimientoReferenciaDocumento(Integer idSeguimientoReferenciaDocumento) {
        this.idSeguimientoReferenciaDocumento = idSeguimientoReferenciaDocumento;
    }

    public Seguimiento getIdSeguimiento() {
        return idSeguimiento;
    }

    public void setIdSeguimiento(Seguimiento idSeguimiento) {
        this.idSeguimiento = idSeguimiento;
    }

    public ReferenciaDocumento getIdReferenciaDocumento() {
        return idReferenciaDocumento;
    }

    public void setIdReferenciaDocumento(ReferenciaDocumento idReferenciaDocumento) {
        this.idReferenciaDocumento = idReferenciaDocumento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSeguimientoReferenciaDocumento != null ? idSeguimientoReferenciaDocumento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SeguimientoReferenciaDocumento)) {
            return false;
        }
        SeguimientoReferenciaDocumento other = (SeguimientoReferenciaDocumento) object;
        if ((this.idSeguimientoReferenciaDocumento == null && other.idSeguimientoReferenciaDocumento != null) || (this.idSeguimientoReferenciaDocumento != null && !this.idSeguimientoReferenciaDocumento.equals(other.idSeguimientoReferenciaDocumento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.SeguimientoReferenciaDocumento[ idSeguimientoReferenciaDocumento=" + idSeguimientoReferenciaDocumento + " ]";
    }
    
}
