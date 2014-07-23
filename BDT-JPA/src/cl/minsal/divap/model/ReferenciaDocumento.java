package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
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
    @Column(name = "documento_final")
    private Boolean documentoFinal;
    @ManyToMany(mappedBy = "referenciaDocumentoCollection")
    private Collection<DistribucionInicialPercapita> distribucionInicialPercapitaCollection;
    @JoinTable(name = "seguimiento_referencia_documento", joinColumns = {
        @JoinColumn(name = "id_referencia_documento", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "id_seguimiento", referencedColumnName = "id")})
    @ManyToMany
    private Set<Seguimiento> seguimientoCollection;
   

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

    @XmlTransient
    public Collection<DistribucionInicialPercapita> getDistribucionInicialPercapitaCollection() {
        return distribucionInicialPercapitaCollection;
    }

    public void setDistribucionInicialPercapitaCollection(Collection<DistribucionInicialPercapita> distribucionInicialPercapitaCollection) {
        this.distribucionInicialPercapitaCollection = distribucionInicialPercapitaCollection;
    }

    @XmlTransient
    public Set<Seguimiento> getSeguimientoCollection() {
        return seguimientoCollection;
    }

    public void setSeguimientoCollection(Set<Seguimiento> seguimientoCollection) {
        this.seguimientoCollection = seguimientoCollection;
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

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ReferenciaDocumento[ id=" + id + " ]";
    }
    
}