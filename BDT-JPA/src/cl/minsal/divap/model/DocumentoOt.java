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
 * @author LeandroSuarez
 */
@Entity
@Table(name = "documento_ot")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentoOt.findAll", query = "SELECT d FROM DocumentoOt d"),
    @NamedQuery(name = "DocumentoOt.findByIddocumentoOt", query = "SELECT d FROM DocumentoOt d WHERE d.iddocumentoOt = :iddocumentoOt"),
	@NamedQuery(name = "DocumentoOt.findLastByidOrdenTransferencia", query = "SELECT d FROM DocumentoOt d WHERE d.idOrdenTransferencia.idOrdenTransferencia = :idOrdenTransferencia and d.idTipoDocumento.idTipoDocumento = :idTipoDocumento ORDER BY d.idDocumento.fechaCreacion DESC"),})

public class DocumentoOt implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "iddocumento_ot")
    private Integer iddocumentoOt;
    @JoinColumn(name = "id_tipo_documento", referencedColumnName = "id_tipo_documento")
    @ManyToOne
    private TipoDocumento idTipoDocumento;
    @JoinColumn(name = "id_documento", referencedColumnName = "id")
    @ManyToOne
    private ReferenciaDocumento idDocumento;
    @JoinColumn(name = "id_orden_transferencia", referencedColumnName = "id_orden_transferencia")
    @ManyToOne
    private OrdenTransferencia idOrdenTransferencia;

    public DocumentoOt() {
    }

    public DocumentoOt(Integer iddocumentoOt) {
        this.iddocumentoOt = iddocumentoOt;
    }

    public Integer getIddocumentoOt() {
        return iddocumentoOt;
    }

    public void setIddocumentoOt(Integer iddocumentoOt) {
        this.iddocumentoOt = iddocumentoOt;
    }

    public TipoDocumento getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(TipoDocumento idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public ReferenciaDocumento getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(ReferenciaDocumento idDocumento) {
        this.idDocumento = idDocumento;
    }

    public OrdenTransferencia getIdOrdenTransferencia() {
        return idOrdenTransferencia;
    }

    public void setIdOrdenTransferencia(OrdenTransferencia idOrdenTransferencia) {
        this.idOrdenTransferencia = idOrdenTransferencia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iddocumentoOt != null ? iddocumentoOt.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentoOt)) {
            return false;
        }
        DocumentoOt other = (DocumentoOt) object;
        if ((this.iddocumentoOt == null && other.iddocumentoOt != null) || (this.iddocumentoOt != null && !this.iddocumentoOt.equals(other.iddocumentoOt))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "examples.DocumentoOt[ iddocumentoOt=" + iddocumentoOt + " ]";
    }
    
}
