package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
@Table(name = "tipo_destinatario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoDestinatario.findAll", query = "SELECT t FROM TipoDestinatario t"),
    @NamedQuery(name = "TipoDestinatario.findByDescripcion", query = "SELECT t FROM TipoDestinatario t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "TipoDestinatario.findByIdTipoDestinatario", query = "SELECT t FROM TipoDestinatario t WHERE t.idTipoDestinatario = :idTipoDestinatario")})
public class TipoDestinatario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "descripcion_corta")
    private String descripcionCorta;
	@Id
   	@Column(name="id_tipo_destinatario", unique=true, nullable=false)
   	@GeneratedValue
    private Integer idTipoDestinatario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoDestinatario")
    private Collection<Destinatarios> destinatariosCollection;

    public TipoDestinatario() {
    }

    public TipoDestinatario(Integer idTipoDestinatario) {
        this.idTipoDestinatario = idTipoDestinatario;
    }

    public TipoDestinatario(Integer idTipoDestinatario, String descripcion) {
        this.idTipoDestinatario = idTipoDestinatario;
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getIdTipoDestinatario() {
        return idTipoDestinatario;
    }

    public void setIdTipoDestinatario(Integer idTipoDestinatario) {
        this.idTipoDestinatario = idTipoDestinatario;
    }

    public String getDescripcionCorta() {
		return descripcionCorta;
	}

	public void setDescripcionCorta(String descripcionCorta) {
		this.descripcionCorta = descripcionCorta;
	}

	@XmlTransient
    public Collection<Destinatarios> getDestinatariosCollection() {
        return destinatariosCollection;
    }

    public void setDestinatariosCollection(Collection<Destinatarios> destinatariosCollection) {
        this.destinatariosCollection = destinatariosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoDestinatario != null ? idTipoDestinatario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TipoDestinatario)) {
            return false;
        }
        TipoDestinatario other = (TipoDestinatario) object;
        if ((this.idTipoDestinatario == null && other.idTipoDestinatario != null) || (this.idTipoDestinatario != null && !this.idTipoDestinatario.equals(other.idTipoDestinatario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.TipoDestinatario[ idTipoDestinatario=" + idTipoDestinatario + " ]";
    }
    
}
