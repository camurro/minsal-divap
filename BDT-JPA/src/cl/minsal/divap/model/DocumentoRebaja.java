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
@Table(name = "documento_rebaja")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentoRebaja.findAll", query = "SELECT d FROM DocumentoRebaja d"),
    @NamedQuery(name = "DocumentoRebaja.findByIdRebajaTipo", query = "SELECT d FROM DocumentoRebaja d WHERE d.rebaja.idRebaja = :idRebaja and d.tipoDocumento.idTipoDocumento = :idTipoDocumento"),
    @NamedQuery(name = "DocumentoRebaja.findByIdDocumentoRebaja", query = "SELECT d FROM DocumentoRebaja d WHERE d.idDocumentoRebaja = :idDocumentoRebaja")})
public class DocumentoRebaja implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
   	@Column(name="id_documento_rebaja", unique=true, nullable=false)
   	@GeneratedValue
    private Integer idDocumentoRebaja;
    @JoinColumn(name = "tipo_documento", referencedColumnName = "id_tipo_documento")
    @ManyToOne(optional = false)
    private TipoDocumento tipoDocumento;
    @JoinColumn(name = "documento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ReferenciaDocumento documento;
    @JoinColumn(name = "rebaja", referencedColumnName = "id_rebaja")
    @ManyToOne(optional = false)
    private Rebaja rebaja;
    @JoinColumn(name = "comuna", referencedColumnName = "id")
    @ManyToOne
    private Comuna comuna;

    public DocumentoRebaja() {
    }

    public DocumentoRebaja(Integer idDocumentoRebaja) {
        this.idDocumentoRebaja = idDocumentoRebaja;
    }

    public Integer getIdDocumentoRebaja() {
        return idDocumentoRebaja;
    }

    public void setIdDocumentoRebaja(Integer idDocumentoRebaja) {
        this.idDocumentoRebaja = idDocumentoRebaja;
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

    public Rebaja getRebaja() {
        return rebaja;
    }

    public void setRebaja(Rebaja rebaja) {
        this.rebaja = rebaja;
    }

    public Comuna getComuna() {
        return comuna;
    }

    public void setComuna(Comuna comuna) {
        this.comuna = comuna;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDocumentoRebaja != null ? idDocumentoRebaja.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DocumentoRebaja)) {
            return false;
        }
        DocumentoRebaja other = (DocumentoRebaja) object;
        if ((this.idDocumentoRebaja == null && other.idDocumentoRebaja != null) || (this.idDocumentoRebaja != null && !this.idDocumentoRebaja.equals(other.idDocumentoRebaja))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.DocumentoRebaja[ idDocumentoRebaja=" + idDocumentoRebaja + " ]";
    }
    
}