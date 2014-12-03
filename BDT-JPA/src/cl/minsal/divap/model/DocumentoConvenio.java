/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
@Table(name = "documento_convenio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentoConvenio.findAll", query = "SELECT d FROM DocumentoConvenio d"),
    @NamedQuery(name = "DocumentoConvenio.findByIdDocumentoConvenio", query = "SELECT d FROM DocumentoConvenio d WHERE d.idDocumentoConvenio = :idDocumentoConvenio"),
    @NamedQuery(name = "DocumentoConvenio.findByIdConvenioTipo", query = "SELECT d FROM DocumentoConvenio d WHERE d.convenio.idConvenio = :idConvenio and d.tipoDocumento.idTipoDocumento = :idTipoDocumento"),
    @NamedQuery(name = "DocumentoConvenio.deleteUsingIds", query = "DELETE FROM DocumentoConvenio d WHERE d.idDocumentoConvenio IN (:idDocumentosConvenio)"),
    @NamedQuery(name = "DocumentoConvenio.findByIdConvenioTipoNotFinal", query = "SELECT d FROM DocumentoConvenio d WHERE d.convenio.idConvenio = :idConvenio and d.tipoDocumento.idTipoDocumento = :idTipoDocumento and d.documento.documentoFinal = false"),
    @NamedQuery(name = "DocumentoConvenio.findByIdConvenioTipoDocumento", query = "SELECT d FROM DocumentoConvenio d WHERE d.convenio.idConvenio = :idConvenio and d.tipoDocumento.idTipoDocumento = :idTipoDocumento order by d.documento.fechaCreacion asc"),
    @NamedQuery(name = "DocumentoConvenio.findVersionFinalByIdConvenioTipoDocumento", query = "SELECT d.documento FROM DocumentoConvenio d WHERE d.convenio.idConvenio = :idConvenio and d.tipoDocumento.idTipoDocumento = :idTipoDocumento and d.documento.documentoFinal = true order by d.documento.fechaCreacion asc")})

public class DocumentoConvenio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
	@Column(name="id_documento_convenio", unique=true, nullable=false)
	@GeneratedValue
    private Integer idDocumentoConvenio;
    @JoinColumn(name = "tipo_documento", referencedColumnName = "id_tipo_documento")
    @ManyToOne(optional = false)
    private TipoDocumento tipoDocumento;
    @JoinColumn(name = "servicio", referencedColumnName = "id")
    @ManyToOne
    private ServicioSalud servicio;
    @JoinColumn(name = "documento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ReferenciaDocumento documento;
    @JoinColumn(name = "convenio", referencedColumnName = "id_convenio")
    @ManyToOne(optional = false)
    private Convenio convenio;

    public DocumentoConvenio() {
    }

    public DocumentoConvenio(Integer idDocumentoConvenio) {
        this.idDocumentoConvenio = idDocumentoConvenio;
    }

    public Integer getIdDocumentoConvenio() {
        return idDocumentoConvenio;
    }

    public void setIdDocumentoConvenio(Integer idDocumentoConvenio) {
        this.idDocumentoConvenio = idDocumentoConvenio;
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

    public Convenio getConvenio() {
        return convenio;
    }

    public void setConvenio(Convenio convenio) {
        this.convenio = convenio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDocumentoConvenio != null ? idDocumentoConvenio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DocumentoConvenio)) {
            return false;
        }
        DocumentoConvenio other = (DocumentoConvenio) object;
        if ((this.idDocumentoConvenio == null && other.idDocumentoConvenio != null) || (this.idDocumentoConvenio != null && !this.idDocumentoConvenio.equals(other.idDocumentoConvenio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.DocumentoConvenio[ idDocumentoConvenio=" + idDocumentoConvenio + " ]";
    }
    
}
