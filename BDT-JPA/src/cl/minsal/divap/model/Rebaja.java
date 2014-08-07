package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@Entity
@Table(name = "rebaja")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rebaja.findAll", query = "SELECT r FROM Rebaja r"),
    @NamedQuery(name = "Rebaja.findByIdRebaja", query = "SELECT r FROM Rebaja r WHERE r.idRebaja = :idRebaja"),
    @NamedQuery(name = "Rebaja.findByUsuario", query = "SELECT r FROM Rebaja r WHERE r.usuario = :usuario"),
    @NamedQuery(name = "Rebaja.findByFechaCreacion", query = "SELECT r FROM Rebaja r WHERE r.fechaCreacion = :fechaCreacion")})
public class Rebaja implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_rebaja")
    private Integer idRebaja;
    @JoinColumn(name = "usuario", referencedColumnName = "username")
	@ManyToOne
	private Usuario usuario;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @ManyToMany(mappedBy = "rebajaCollection")
    private Collection<ReferenciaDocumento> referenciaDocumentoCollection;

    public Rebaja() {
    }

    public Rebaja(Integer idRebaja) {
        this.idRebaja = idRebaja;
    }

    public Integer getIdRebaja() {
        return idRebaja;
    }

    public void setIdRebaja(Integer idRebaja) {
        this.idRebaja = idRebaja;
    }

    public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @XmlTransient
    public Collection<ReferenciaDocumento> getReferenciaDocumentoCollection() {
        return referenciaDocumentoCollection;
    }

    public void setReferenciaDocumentoCollection(Collection<ReferenciaDocumento> referenciaDocumentoCollection) {
        this.referenciaDocumentoCollection = referenciaDocumentoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRebaja != null ? idRebaja.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rebaja)) {
            return false;
        }
        Rebaja other = (Rebaja) object;
        if ((this.idRebaja == null && other.idRebaja != null) || (this.idRebaja != null && !this.idRebaja.equals(other.idRebaja))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.Rebaja[ idRebaja=" + idRebaja + " ]";
    }
    
}