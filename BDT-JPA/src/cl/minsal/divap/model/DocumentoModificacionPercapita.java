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
@Table(name = "documento_modificacion_percapita")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentoModificacionPercapita.findAll", query = "SELECT d FROM DocumentoModificacionPercapita d"),
    @NamedQuery(name = "DocumentoModificacionPercapita.findByIdDocumentoModificacionPercapita", query = "SELECT d FROM DocumentoModificacionPercapita d WHERE d.idDocumentoModificacionPercapita = :idDocumentoModificacionPercapita"),
    @NamedQuery(name = "DocumentoModificacionPercapita.findByIdModificacionDistribucionTipoDocumento", query = "SELECT d FROM DocumentoModificacionPercapita d WHERE d.modificacionPercapita.idModificacionDistribucionInicialPercapita = :idModificacionDistribucionInicialPercapita and d.tipoDocumento.idTipoDocumento = :idTipoDocumento order by d.documento.fechaCreacion desc"),
    @NamedQuery(name = "DocumentoModificacionPercapita.findVersionFinalByIdModificacionDistribucionInicialTipoDocumento", query = "SELECT d.documento FROM DocumentoModificacionPercapita d WHERE d.modificacionPercapita.idModificacionDistribucionInicialPercapita = :idModificacionDistribucionInicialPercapita and d.tipoDocumento.idTipoDocumento = :idTipoDocumento and d.documento.documentoFinal = true order by d.documento.fechaCreacion desc"),
    @NamedQuery(name = "DocumentoModificacionPercapita.findVersionFinalByIdModificacionPercapitaTipoDocumento", query = "SELECT d.documento FROM DocumentoModificacionPercapita d WHERE d.modificacionPercapita.idModificacionDistribucionInicialPercapita = :idModificacionDistribucionInicialPercapita and d.tipoDocumento.idTipoDocumento = :idTipoDocumento and d.documento.documentoFinal = true order by d.documento.fechaCreacion desc"),
    @NamedQuery(name = "DocumentoModificacionPercapita.findByTypesIdModificacionPercapita", query = "SELECT d FROM DocumentoModificacionPercapita d WHERE d.modificacionPercapita.idModificacionDistribucionInicialPercapita = :idModificacionDistribucionInicialPercapita and d.tipoDocumento.idTipoDocumento IN (:idTiposDocumento)"),
    @NamedQuery(name = "DocumentoModificacionPercapita.findByTypesServicioIdModificacionPercapita", query = "SELECT d FROM DocumentoModificacionPercapita d WHERE d.modificacionPercapita.idModificacionDistribucionInicialPercapita = :idModificacionDistribucionInicialPercapita and d.comuna is not null and d.comuna.servicioSalud.id = :idServicio and d.tipoDocumento.idTipoDocumento IN (:idTiposDocumento)"),
    @NamedQuery(name = "DocumentoModificacionPercapita.findLastByTypesServicioIdModificacionPercapita", query = "SELECT d FROM DocumentoModificacionPercapita d WHERE d.modificacionPercapita.idModificacionDistribucionInicialPercapita = :idModificacionDistribucionInicialPercapita and d.servicio.id = :idServicio and d.tipoDocumento.idTipoDocumento IN (:idTiposDocumento) order by d.documento.fechaCreacion desc")})
public class DocumentoModificacionPercapita implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
   	@Column(name="id_documento_modificacion_percapita", unique=true, nullable=false)
   	@GeneratedValue
    private Integer idDocumentoModificacionPercapita;
    @JoinColumn(name = "tipo_documento", referencedColumnName = "id_tipo_documento")
    @ManyToOne(optional = false)
    private TipoDocumento tipoDocumento;
    @JoinColumn(name = "servicio", referencedColumnName = "id")
    @ManyToOne
    private ServicioSalud servicio;
    @JoinColumn(name = "documento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ReferenciaDocumento documento;
    @JoinColumn(name = "modificacion_percapita", referencedColumnName = "id_modificacion_distribucion_inicial_percapita")
    @ManyToOne(optional = false)
    private ModificacionDistribucionInicialPercapita modificacionPercapita;
    @JoinColumn(name = "comuna", referencedColumnName = "id")
    @ManyToOne
    private Comuna comuna;


    public DocumentoModificacionPercapita() {
    }

    public DocumentoModificacionPercapita(Integer idDocumentoModificacionPercapita) {
        this.idDocumentoModificacionPercapita = idDocumentoModificacionPercapita;
    }

    public Integer getIdDocumentoModificacionPercapita() {
        return idDocumentoModificacionPercapita;
    }

    public void setIdDocumentoModificacionPercapita(Integer idDocumentoModificacionPercapita) {
        this.idDocumentoModificacionPercapita = idDocumentoModificacionPercapita;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public ServicioSalud getServicio() {
        return servicio;
    }

    public void setServicio(ServicioSalud servicio) {
        this.servicio = servicio;
    }

    public ReferenciaDocumento getDocumento() {
        return documento;
    }

    public void setDocumento(ReferenciaDocumento documento) {
        this.documento = documento;
    }

    public ModificacionDistribucionInicialPercapita getModificacionPercapita() {
        return modificacionPercapita;
    }

    public void setModificacionPercapita(ModificacionDistribucionInicialPercapita modificacionPercapita) {
        this.modificacionPercapita = modificacionPercapita;
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
        hash += (idDocumentoModificacionPercapita != null ? idDocumentoModificacionPercapita.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DocumentoModificacionPercapita)) {
            return false;
        }
        DocumentoModificacionPercapita other = (DocumentoModificacionPercapita) object;
        if ((this.idDocumentoModificacionPercapita == null && other.idDocumentoModificacionPercapita != null) || (this.idDocumentoModificacionPercapita != null && !this.idDocumentoModificacionPercapita.equals(other.idDocumentoModificacionPercapita))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.DocumentoModificacionPercapita[ idDocumentoModificacionPercapita=" + idDocumentoModificacionPercapita + " ]";
    }
    
}
