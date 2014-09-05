package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author LeandroSuarez
 */
@Entity
@Table(name = "orden_transferencia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrdenTransferencia.findAll", query = "SELECT o FROM OrdenTransferencia o"),
    @NamedQuery(name = "OrdenTransferencia.findByIdOrdenTransferencia", query = "SELECT o FROM OrdenTransferencia o WHERE o.idOrdenTransferencia = :idOrdenTransferencia"),
    @NamedQuery(name = "OrdenTransferencia.findByFechaCreacion", query = "SELECT o FROM OrdenTransferencia o WHERE o.fechaCreacion = :fechaCreacion")})
public class OrdenTransferencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_orden_transferencia")
    private Integer idOrdenTransferencia;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @JoinColumn(name = "usuario", referencedColumnName = "username")
    @ManyToOne
    private Usuario usuario;
    @OneToMany(mappedBy = "idOrdenTransferencia")
    private Collection<DocumentoOt> documentoOtCollection;

    public OrdenTransferencia() {
    }

    public OrdenTransferencia(Integer idOrdenTransferencia) {
        this.idOrdenTransferencia = idOrdenTransferencia;
    }

    public Integer getIdOrdenTransferencia() {
        return idOrdenTransferencia;
    }

    public void setIdOrdenTransferencia(Integer idOrdenTransferencia) {
        this.idOrdenTransferencia = idOrdenTransferencia;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @XmlTransient
    public Collection<DocumentoOt> getDocumentoOtCollection() {
        return documentoOtCollection;
    }

    public void setDocumentoOtCollection(Collection<DocumentoOt> documentoOtCollection) {
        this.documentoOtCollection = documentoOtCollection;
    }

    

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idOrdenTransferencia != null ? idOrdenTransferencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrdenTransferencia)) {
            return false;
        }
        OrdenTransferencia other = (OrdenTransferencia) object;
        if ((this.idOrdenTransferencia == null && other.idOrdenTransferencia != null) || (this.idOrdenTransferencia != null && !this.idOrdenTransferencia.equals(other.idOrdenTransferencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "examples.OrdenTransferencia[ idOrdenTransferencia=" + idOrdenTransferencia + " ]";
    }
    
}
