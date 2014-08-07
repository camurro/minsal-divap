package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
 * @author cmurillo
 */
@Entity
@Table(name = "referencia_documento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReferenciaDocumento.findAll", query = "SELECT r FROM ReferenciaDocumento r"),
    @NamedQuery(name = "ReferenciaDocumento.findById", query = "SELECT r FROM ReferenciaDocumento r WHERE r.id = :id"),
    @NamedQuery(name = "ReferenciaDocumento.findByIds", query = "SELECT r FROM ReferenciaDocumento r WHERE r.id IN (:ids)"),
    @NamedQuery(name = "ReferenciaDocumento.findByContentType", query = "SELECT r FROM ReferenciaDocumento r WHERE r.contentType = :contentType"),
    @NamedQuery(name = "ReferenciaDocumento.findByPath", query = "SELECT r FROM ReferenciaDocumento r WHERE r.path = :path"),
    @NamedQuery(name = "ReferenciaDocumento.findByDocumentoFinal", query = "SELECT r FROM ReferenciaDocumento r WHERE r.documentoFinal = :documentoFinal")})
public class ReferenciaDocumento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
	@Column(name="id", unique=true, nullable=false)
	@GeneratedValue
    private Integer id;
    @Column(name = "content_type")
    private String contentType;
    @Column(name = "path")
    private String path;
    @Column(name = "node_ref")
    private String nodeRef;
    @Column(name = "fecha_creacion")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaCreacion;
    @Column(name = "documento_final")
    private Boolean documentoFinal;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idDocumento")
    private Set<DocumentoDistribucionInicialPercapita> documentoDistribucionInicialPercapitaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idReferenciaDocumento")
    private Collection<SeguimientoReferenciaDocumento> seguimientoReferenciaDocumentoCollection;
    @OneToMany(mappedBy = "documento")
    private Set<Plantilla> plantillaCollection;

    public ReferenciaDocumento() {
    }

    public ReferenciaDocumento(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getDocumentoFinal() {
        return documentoFinal;
    }

    public void setDocumentoFinal(Boolean documentoFinal) {
        this.documentoFinal = documentoFinal;
    }

    public Set<DocumentoDistribucionInicialPercapita> getDocumentoDistribucionInicialPercapitaCollection() {
		return documentoDistribucionInicialPercapitaCollection;
	}

	public void setDocumentoDistribucionInicialPercapitaCollection(
			Set<DocumentoDistribucionInicialPercapita> documentoDistribucionInicialPercapitaCollection) {
		this.documentoDistribucionInicialPercapitaCollection = documentoDistribucionInicialPercapitaCollection;
	}

	@XmlTransient
    public Collection<SeguimientoReferenciaDocumento> getSeguimientoReferenciaDocumentoCollection() {
        return seguimientoReferenciaDocumentoCollection;
    }

    public void setSeguimientoReferenciaDocumentoCollection(Collection<SeguimientoReferenciaDocumento> seguimientoReferenciaDocumentoCollection) {
        this.seguimientoReferenciaDocumentoCollection = seguimientoReferenciaDocumentoCollection;
    }

    public String getNodeRef() {
		return nodeRef;
	}

	public void setNodeRef(String nodeRef) {
		this.nodeRef = nodeRef;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ReferenciaDocumento)) {
            return false;
        }
        ReferenciaDocumento other = (ReferenciaDocumento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Set<Plantilla> getPlantillaCollection() {
		return plantillaCollection;
	}

	public void setPlantillaCollection(Set<Plantilla> plantillaCollection) {
		this.plantillaCollection = plantillaCollection;
	}

	@Override
    public String toString() {
        return "cl.minsal.divap.model.ReferenciaDocumento[ id=" + id + " ]";
    }
    
}