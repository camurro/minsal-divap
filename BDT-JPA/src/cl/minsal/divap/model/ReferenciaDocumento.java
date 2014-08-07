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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
    @NamedQuery(name = "ReferenciaDocumento.findByAllId", query = "SELECT r FROM ReferenciaDocumento r WHERE r.id IN :listaIdReferencia"),
    @NamedQuery(name = "ReferenciaDocumento.findByContentType", query = "SELECT r FROM ReferenciaDocumento r WHERE r.contentType = :contentType"),
    @NamedQuery(name = "ReferenciaDocumento.findByPath", query = "SELECT r FROM ReferenciaDocumento r WHERE r.path = :path"),
    @NamedQuery(name = "ReferenciaDocumento.findByDocumentoFinal", query = "SELECT r FROM ReferenciaDocumento r WHERE r.documentoFinal = :documentoFinal"),
    @NamedQuery(name = "ReferenciaDocumento.findByNodeRef", query = "SELECT r FROM ReferenciaDocumento r WHERE r.nodeRef = :nodeRef"),
    @NamedQuery(name = "ReferenciaDocumento.findByFechaCreacion", query = "SELECT r FROM ReferenciaDocumento r WHERE r.fechaCreacion = :fechaCreacion")})
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
    @Column(name = "documento_final")
    private Boolean documentoFinal;
    @Column(name = "node_ref")
    private String nodeRef;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @JoinTable(name = "documento_rebaja", joinColumns = {
        @JoinColumn(name = "documento", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "rebaja", referencedColumnName = "id_rebaja")})
    @ManyToMany
    private Collection<Rebaja> rebajaCollection;
    @ManyToMany(mappedBy = "referenciaDocumentoCollection")
    private Collection<DistribucionInicialPercapita> distribucionInicialPercapitaCollection;
    @JoinTable(name = "seguimiento_referencia_documento", joinColumns = {
        @JoinColumn(name = "id_referencia_documento", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "id_seguimiento", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Seguimiento> seguimientoCollection;
    @OneToMany(mappedBy = "documento")
    private Collection<Plantilla> plantillaCollection;

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

    public String getNodeRef() {
        return nodeRef;
    }

    public void setNodeRef(String nodeRef) {
        this.nodeRef = nodeRef;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @XmlTransient
    public Collection<Rebaja> getRebajaCollection() {
        return rebajaCollection;
    }

    public void setRebajaCollection(Collection<Rebaja> rebajaCollection) {
        this.rebajaCollection = rebajaCollection;
    }

    @XmlTransient
    public Collection<DistribucionInicialPercapita> getDistribucionInicialPercapitaCollection() {
        return distribucionInicialPercapitaCollection;
    }

    public void setDistribucionInicialPercapitaCollection(Collection<DistribucionInicialPercapita> distribucionInicialPercapitaCollection) {
        this.distribucionInicialPercapitaCollection = distribucionInicialPercapitaCollection;
    }

    @XmlTransient
    public Collection<Seguimiento> getSeguimientoCollection() {
        return seguimientoCollection;
    }

    public void setSeguimientoCollection(Collection<Seguimiento> seguimientoCollection) {
        this.seguimientoCollection = seguimientoCollection;
    }

    @XmlTransient
    public Collection<Plantilla> getPlantillaCollection() {
        return plantillaCollection;
    }

    public void setPlantillaCollection(Collection<Plantilla> plantillaCollection) {
        this.plantillaCollection = plantillaCollection;
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