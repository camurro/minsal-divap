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
@Table(name = "documento_distribucion_inicial_percapita")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "DocumentoDistribucionInicialPercapita.findAll", query = "SELECT d FROM DocumentoDistribucionInicialPercapita d"),
	@NamedQuery(name = "DocumentoDistribucionInicialPercapita.findByTypeIdDistribucionInicialPercapita", query = "SELECT d FROM DocumentoDistribucionInicialPercapita d WHERE d.idDistribucionInicialPercapita.idDistribucionInicialPercapita = :idDistribucionInicialPercapita and d.tipoDocumento.idTipoDocumento = :idTipoDocumento"),
	@NamedQuery(name = "DocumentoDistribucionInicialPercapita.findLastByTypeIdDistribucionInicialPercapita", query = "SELECT d FROM DocumentoDistribucionInicialPercapita d WHERE d.idDistribucionInicialPercapita.idDistribucionInicialPercapita = :idDistribucionInicialPercapita and d.tipoDocumento.idTipoDocumento = :idTipoDocumento ORDER BY d.idDocumento.fechaCreacion DESC"),
	@NamedQuery(name = "DocumentoDistribucionInicialPercapita.findByIdDocumentoDistribucionInicialPercapita", query = "SELECT d FROM DocumentoDistribucionInicialPercapita d WHERE d.idDocumentoDistribucionInicialPercapita = :idDocumentoDistribucionInicialPercapita")})
public class DocumentoDistribucionInicialPercapita implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="id_documento_distribucion_inicial_percapita", unique=true, nullable=false)
	@GeneratedValue
	private Integer idDocumentoDistribucionInicialPercapita;
	@JoinColumn(name = "tipo_documento", referencedColumnName = "id_tipo_documento")
	@ManyToOne(optional = false)
	private TipoDocumento tipoDocumento;
	@JoinColumn(name = "id_documento", referencedColumnName = "id")
	@ManyToOne(optional = false)
	private ReferenciaDocumento idDocumento;
	@JoinColumn(name = "id_distribucion_inicial_percapita", referencedColumnName = "id_distribucion_inicial_percapita")
	@ManyToOne(optional = false)
	private DistribucionInicialPercapita idDistribucionInicialPercapita;
	@JoinColumn(name = "comuna", referencedColumnName = "id")
	@ManyToOne
	private Comuna comuna;

	public DocumentoDistribucionInicialPercapita() {
	}

	public DocumentoDistribucionInicialPercapita(Integer idDocumentoDistribucionInicialPercapita) {
		this.idDocumentoDistribucionInicialPercapita = idDocumentoDistribucionInicialPercapita;
	}

	public Integer getIdDocumentoDistribucionInicialPercapita() {
		return idDocumentoDistribucionInicialPercapita;
	}

	public void setIdDocumentoDistribucionInicialPercapita(Integer idDocumentoDistribucionInicialPercapita) {
		this.idDocumentoDistribucionInicialPercapita = idDocumentoDistribucionInicialPercapita;
	}

	public ReferenciaDocumento getIdDocumento() {
		return idDocumento;
	}

	public void setIdDocumento(ReferenciaDocumento idDocumento) {
		this.idDocumento = idDocumento;
	}

	public DistribucionInicialPercapita getIdDistribucionInicialPercapita() {
		return idDistribucionInicialPercapita;
	}

	public void setIdDistribucionInicialPercapita(DistribucionInicialPercapita idDistribucionInicialPercapita) {
		this.idDistribucionInicialPercapita = idDistribucionInicialPercapita;
	}

	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
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
		hash += (idDocumentoDistribucionInicialPercapita != null ? idDocumentoDistribucionInicialPercapita.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof DocumentoDistribucionInicialPercapita)) {
			return false;
		}
		DocumentoDistribucionInicialPercapita other = (DocumentoDistribucionInicialPercapita) object;
		if ((this.idDocumentoDistribucionInicialPercapita == null && other.idDocumentoDistribucionInicialPercapita != null) || (this.idDocumentoDistribucionInicialPercapita != null && !this.idDocumentoDistribucionInicialPercapita.equals(other.idDocumentoDistribucionInicialPercapita))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "cl.minsal.divap.model.DocumentoDistribucionInicialPercapita[ idDocumentoDistribucionInicialPercapita=" + idDocumentoDistribucionInicialPercapita + " ]";
	}

}