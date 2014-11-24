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
@Table(name = "documento_convenio_servicio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentoConvenioServicio.findAll", query = "SELECT d FROM DocumentoConvenioServicio d"),
    @NamedQuery(name = "DocumentoConvenioServicio.findByIdDocumentoConvenioServicio", query = "SELECT d FROM DocumentoConvenioServicio d WHERE d.idDocumentoConvenioServicio = :idDocumentoConvenioServicio"),
    @NamedQuery(name = "DocumentoConvenioServicio.findByIdConvenioServicio", query = "SELECT d FROM DocumentoConvenioServicio d WHERE d.convenio.idConvenioServicio = :idConvenioServicio")})
public class DocumentoConvenioServicio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
   	@Column(name="id_documento_convenio_servicio", unique=true, nullable=false)
   	@GeneratedValue
    private Integer idDocumentoConvenioServicio;
    @JoinColumn(name = "tipo_documento", referencedColumnName = "id_tipo_documento")
    @ManyToOne(optional = false)
    private TipoDocumento tipoDocumento;
    @JoinColumn(name = "documento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ReferenciaDocumento documento;
    @JoinColumn(name = "convenio", referencedColumnName = "id_convenio_servicio")
    @ManyToOne(optional = false)
    private ConvenioServicio convenio;

    public DocumentoConvenioServicio() {
    }

    public DocumentoConvenioServicio(Integer idDocumentoConvenioServicio) {
        this.idDocumentoConvenioServicio = idDocumentoConvenioServicio;
    }

    public Integer getIdDocumentoConvenioServicio() {
        return idDocumentoConvenioServicio;
    }

    public void setIdDocumentoConvenioServicio(Integer idDocumentoConvenioServicio) {
        this.idDocumentoConvenioServicio = idDocumentoConvenioServicio;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public ReferenciaDocumento getDocumento() {
        return documento;
    }

    public void setDocumento(ReferenciaDocumento documento) {
        this.documento = documento;
    }

    public ConvenioServicio getConvenio() {
        return convenio;
    }

    public void setConvenio(ConvenioServicio convenio) {
        this.convenio = convenio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDocumentoConvenioServicio != null ? idDocumentoConvenioServicio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DocumentoConvenioServicio)) {
            return false;
        }
        DocumentoConvenioServicio other = (DocumentoConvenioServicio) object;
        if ((this.idDocumentoConvenioServicio == null && other.idDocumentoConvenioServicio != null) || (this.idDocumentoConvenioServicio != null && !this.idDocumentoConvenioServicio.equals(other.idDocumentoConvenioServicio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.DocumentoConvenioServicio[ idDocumentoConvenioServicio=" + idDocumentoConvenioServicio + " ]";
    }
    
}
