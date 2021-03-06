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
	@NamedQuery(name = "DocumentoDistribucionInicialPercapita.findByTypesServicioIdDistribucionInicialPercapita", query = "SELECT d FROM DocumentoDistribucionInicialPercapita d WHERE d.idDistribucionInicialPercapita.idDistribucionInicialPercapita = :idDistribucionInicialPercapita and d.comuna is not null and d.comuna.servicioSalud.id = :idServicio and d.tipoDocumento.idTipoDocumento IN (:idTiposDocumento)"),
	@NamedQuery(name = "DocumentoDistribucionInicialPercapita.findByTypesIdDistribucionInicialPercapita", query = "SELECT d FROM DocumentoDistribucionInicialPercapita d WHERE d.idDistribucionInicialPercapita.idDistribucionInicialPercapita = :idDistribucionInicialPercapita and d.tipoDocumento.idTipoDocumento IN (:idTiposDocumento)"),
	@NamedQuery(name = "DocumentoDistribucionInicialPercapita.findLastByTypeIdDistribucionInicialPercapita", query = "SELECT d FROM DocumentoDistribucionInicialPercapita d WHERE d.idDistribucionInicialPercapita.idDistribucionInicialPercapita = :idDistribucionInicialPercapita and d.tipoDocumento.idTipoDocumento = :idTipoDocumento ORDER BY d.idDocumento.fechaCreacion DESC"),
	@NamedQuery(name = "DocumentoDistribucionInicialPercapita.findByIdDocumentoDistribucionInicialPercapita", query = "SELECT d FROM DocumentoDistribucionInicialPercapita d WHERE d.idDocumentoDistribucionInicialPercapita = :idDocumentoDistribucionInicialPercapita"),
	@NamedQuery(name = "DocumentoDistribucionInicialPercapita.findLastByTypesServicioIdDistribucionInicialPercapita", query = "SELECT d FROM DocumentoDistribucionInicialPercapita d WHERE d.idDistribucionInicialPercapita.idDistribucionInicialPercapita = :idDistribucionInicialPercapita and d.servicio.id = :idServicio and d.tipoDocumento.idTipoDocumento IN (:idTiposDocumento) order by d.idDocumento.fechaCreacion desc"),
	@NamedQuery(name = "DocumentoDistribucionInicialPercapita.findByIdDistribucionInicialPercapitaTiposNotFinal", query = "SELECT d FROM DocumentoDistribucionInicialPercapita d WHERE d.idDistribucionInicialPercapita.idDistribucionInicialPercapita = :idDistribucionInicialPercapita and d.tipoDocumento.idTipoDocumento IN (:idTiposDocumento) and d.idDocumento.documentoFinal = false"),
	@NamedQuery(name = "DocumentoDistribucionInicialPercapita.deleteUsingIds", query = "DELETE FROM DocumentoDistribucionInicialPercapita d WHERE d.idDocumentoDistribucionInicialPercapita IN (:idDocumentosDistribucionInicialPercapita)"),
	@NamedQuery(name = "DocumentoDistribucionInicialPercapita.findByServicioDistribucionInicialPercapitaTypes", query = "SELECT d.idDocumento FROM DocumentoDistribucionInicialPercapita d WHERE d.idDistribucionInicialPercapita.idDistribucionInicialPercapita = :idDistribucionInicialPercapita and d.servicio.id = :idServicio and d.tipoDocumento.idTipoDocumento IN (:idTiposDocumento) and d.idDocumento.documentoFinal = true order by d.idDocumento.fechaCreacion desc"),
	@NamedQuery(name = "DocumentoDistribucionInicialPercapita.findVersionFinalByIdDistribucionInicialPercapitaTipoDocumento", query = "SELECT d.idDocumento FROM DocumentoDistribucionInicialPercapita d WHERE d.idDistribucionInicialPercapita.idDistribucionInicialPercapita = :idDistribucionInicialPercapita and d.tipoDocumento.idTipoDocumento = :idTipoDocumento and d.idDocumento.documentoFinal = true order by d.idDocumento.fechaCreacion desc"),
	@NamedQuery(name = "DocumentoDistribucionInicialPercapita.findVersionFinalByIdDistribucionInicialPercapitaIdServicioTipoDocumento", query = "SELECT d.idDocumento FROM DocumentoDistribucionInicialPercapita d WHERE d.idDistribucionInicialPercapita.idDistribucionInicialPercapita = :idDistribucionInicialPercapita and d.servicio.id = :idServicio and d.tipoDocumento.idTipoDocumento = :idTipoDocumento and d.idDocumento.documentoFinal = true order by d.idDocumento.fechaCreacion desc")})
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
	@JoinColumn(name = "servicio", referencedColumnName = "id")
	@ManyToOne
	private ServicioSalud servicio;

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

	public ServicioSalud getServicio() {
		return servicio;
	}

	public void setServicio(ServicioSalud servicio) {
		this.servicio = servicio;
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