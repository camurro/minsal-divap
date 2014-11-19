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
@Table(name = "documento_reportes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentoReportes.findAll", query = "SELECT d FROM DocumentoReportes d"),
    @NamedQuery(name = "DocumentoReportes.findByIdDocumentoReporte", query = "SELECT d FROM DocumentoReportes d WHERE d.idDocumentoReporte = :idDocumentoReporte"),
    @NamedQuery(name = "DocumentoReportes.findByTypesAno", query = "SELECT d FROM DocumentoReportes d WHERE d.tipoDocumento.idTipoDocumento = :idTipoDocumento and d.ano.ano = :ano")})
public class DocumentoReportes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="id_documento_reporte", unique=true, nullable=false)
    @GeneratedValue
    private Integer idDocumentoReporte;
    @JoinColumn(name = "tipo_documento", referencedColumnName = "id_tipo_documento")
    @ManyToOne
    private TipoDocumento tipoDocumento;
    @JoinColumn(name = "documento", referencedColumnName = "id")
    @ManyToOne
    private ReferenciaDocumento documento;
    @JoinColumn(name = "ano", referencedColumnName = "ano", nullable=false)
	@ManyToOne(optional = false)
	private AnoEnCurso ano;

    public DocumentoReportes() {
    }

    public DocumentoReportes(Integer idDocumentoReporte) {
        this.idDocumentoReporte = idDocumentoReporte;
    }

    public Integer getIdDocumentoReporte() {
        return idDocumentoReporte;
    }

    public void setIdDocumentoReporte(Integer idDocumentoReporte) {
        this.idDocumentoReporte = idDocumentoReporte;
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

    public AnoEnCurso getAno() {
		return ano;
	}

	public void setAno(AnoEnCurso ano) {
		this.ano = ano;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idDocumentoReporte != null ? idDocumentoReporte.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DocumentoReportes)) {
            return false;
        }
        DocumentoReportes other = (DocumentoReportes) object;
        if ((this.idDocumentoReporte == null && other.idDocumentoReporte != null) || (this.idDocumentoReporte != null && !this.idDocumentoReporte.equals(other.idDocumentoReporte))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.DocumentoReportes[ idDocumentoReporte=" + idDocumentoReporte + " ]";
    }
   
}