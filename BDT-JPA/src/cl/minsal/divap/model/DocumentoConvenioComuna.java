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
@Table(name = "documento_convenio_comuna")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentoConvenioComuna.findAll", query = "SELECT d FROM DocumentoConvenioComuna d"),
    @NamedQuery(name = "DocumentoConvenioComuna.findByIdDocumentoConvenioComuna", query = "SELECT d FROM DocumentoConvenioComuna d WHERE d.idDocumentoConvenioComuna = :idDocumentoConvenioComuna"),
    @NamedQuery(name = "DocumentoConvenioComuna.findByIdConvenioComuna", query = "SELECT d FROM DocumentoConvenioComuna d WHERE d.convenio.idConvenioComuna = :idConvenioComuna") })
public class DocumentoConvenioComuna implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
   	@Column(name="id_documento_convenio_comuna", unique=true, nullable=false)
   	@GeneratedValue
    private Integer idDocumentoConvenioComuna;
    @JoinColumn(name = "tipo_documento", referencedColumnName = "id_tipo_documento")
    @ManyToOne(optional = false)
    private TipoDocumento tipoDocumento;
    @JoinColumn(name = "documento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ReferenciaDocumento documento;
    @JoinColumn(name = "convenio", referencedColumnName = "id_convenio_comuna")
    @ManyToOne(optional = false)
    private ConvenioComuna convenio;

    public DocumentoConvenioComuna() {
    }

    public DocumentoConvenioComuna(Integer idDocumentoConvenioComuna) {
        this.idDocumentoConvenioComuna = idDocumentoConvenioComuna;
    }

    public Integer getIdDocumentoConvenioComuna() {
        return idDocumentoConvenioComuna;
    }

    public void setIdDocumentoConvenioComuna(Integer idDocumentoConvenioComuna) {
        this.idDocumentoConvenioComuna = idDocumentoConvenioComuna;
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

    public ConvenioComuna getConvenio() {
        return convenio;
    }

    public void setConvenio(ConvenioComuna convenio) {
        this.convenio = convenio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDocumentoConvenioComuna != null ? idDocumentoConvenioComuna.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DocumentoConvenioComuna)) {
            return false;
        }
        DocumentoConvenioComuna other = (DocumentoConvenioComuna) object;
        if ((this.idDocumentoConvenioComuna == null && other.idDocumentoConvenioComuna != null) || (this.idDocumentoConvenioComuna != null && !this.idDocumentoConvenioComuna.equals(other.idDocumentoConvenioComuna))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.DocumentoConvenioComuna[ idDocumentoConvenioComuna=" + idDocumentoConvenioComuna + " ]";
    }
    
}
