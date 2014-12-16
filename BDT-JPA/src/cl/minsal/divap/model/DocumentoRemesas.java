package cl.minsal.divap.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "documento_remesas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentoRemesas.findAll", query = "SELECT d FROM DocumentoRemesas d"),
    @NamedQuery(name = "DocumentoRemesas.findByTipoDocumentoAndRemesa", query = "SELECT d FROM DocumentoRemesas d WHERE d.remesa.idRemesa = :idRemesa and d.tipoDocumento.idTipoDocumento = :idTipoDocumento order by d.documento.fechaCreacion desc"),
    @NamedQuery(name = "DocumentoRemesas.findByIdDocumentoRemesas", query = "SELECT d FROM DocumentoRemesas d WHERE d.idDocumentoRemesas = :idDocumentoRemesas")})
public class DocumentoRemesas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_documento_remesas")
    private Integer idDocumentoRemesas;
    @JoinColumn(name = "tipo_documento", referencedColumnName = "id_tipo_documento")
    @ManyToOne(optional = false)
    private TipoDocumento tipoDocumento;
    @JoinColumn(name = "remesa", referencedColumnName = "id_remesa")
    @ManyToOne(optional = false)
    private Remesas remesa;
    @JoinColumn(name = "documento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ReferenciaDocumento documento;

    public DocumentoRemesas() {
    }

    public DocumentoRemesas(Integer idDocumentoRemesas) {
        this.idDocumentoRemesas = idDocumentoRemesas;
    }

    public Integer getIdDocumentoRemesas() {
        return idDocumentoRemesas;
    }

    public void setIdDocumentoRemesas(Integer idDocumentoRemesas) {
        this.idDocumentoRemesas = idDocumentoRemesas;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Remesas getRemesa() {
        return remesa;
    }

    public void setRemesa(Remesas remesa) {
        this.remesa = remesa;
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
        hash += (idDocumentoRemesas != null ? idDocumentoRemesas.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentoRemesas)) {
            return false;
        }
        DocumentoRemesas other = (DocumentoRemesas) object;
        if ((this.idDocumentoRemesas == null && other.idDocumentoRemesas != null) || (this.idDocumentoRemesas != null && !this.idDocumentoRemesas.equals(other.idDocumentoRemesas))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.DocumentoRemesas[ idDocumentoRemesas=" + idDocumentoRemesas + " ]";
    }
    
}