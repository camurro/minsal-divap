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
@Table(name = "adjuntos_seguimiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AdjuntosSeguimiento.findAll", query = "SELECT a FROM AdjuntosSeguimiento a"),
    @NamedQuery(name = "AdjuntosSeguimiento.findByIdAdjuntosSeguimiento", query = "SELECT a FROM AdjuntosSeguimiento a WHERE a.idAdjuntosSeguimiento = :idAdjuntosSeguimiento")})
public class AdjuntosSeguimiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
  	@Column(name="id_adjuntos_seguimiento", unique=true, nullable=false)
  	@GeneratedValue
    private Integer idAdjuntosSeguimiento;
    @JoinColumn(name = "seguimiento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Seguimiento seguimiento;
    @JoinColumn(name = "documento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ReferenciaDocumento documento;

    public AdjuntosSeguimiento() {
    }

    public AdjuntosSeguimiento(Integer idAdjuntosSeguimiento) {
        this.idAdjuntosSeguimiento = idAdjuntosSeguimiento;
    }

    public Integer getIdAdjuntosSeguimiento() {
        return idAdjuntosSeguimiento;
    }

    public void setIdAdjuntosSeguimiento(Integer idAdjuntosSeguimiento) {
        this.idAdjuntosSeguimiento = idAdjuntosSeguimiento;
    }

    public Seguimiento getSeguimiento() {
        return seguimiento;
    }

    public void setSeguimiento(Seguimiento seguimiento) {
        this.seguimiento = seguimiento;
    }

    public ReferenciaDocumento getDocumento() {
        return documento;
    }

    public void setDocumento(ReferenciaDocumento documento) {
        this.documento = documento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAdjuntosSeguimiento != null ? idAdjuntosSeguimiento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AdjuntosSeguimiento)) {
            return false;
        }
        AdjuntosSeguimiento other = (AdjuntosSeguimiento) object;
        if ((this.idAdjuntosSeguimiento == null && other.idAdjuntosSeguimiento != null) || (this.idAdjuntosSeguimiento != null && !this.idAdjuntosSeguimiento.equals(other.idAdjuntosSeguimiento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.AdjuntosSeguimiento[ idAdjuntosSeguimiento=" + idAdjuntosSeguimiento + " ]";
    }
    
}
