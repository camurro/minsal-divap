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
@Table(name = "documento_estimacion_flujo_caja_consolidador")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentoEstimacionFlujoCajaConsolidador.findAll", query = "SELECT d FROM DocumentoEstimacionFlujoCajaConsolidador d"),
    @NamedQuery(name = "DocumentoEstimacionFlujoCajaConsolidador.findByIdDocumentoEstimacionFlujoCajaConsolidador", query = "SELECT d FROM DocumentoEstimacionFlujoCajaConsolidador d WHERE d.idDocumentoEstimacionFlujoCajaConsolidador = :idDocumentoEstimacionFlujoCajaConsolidador"),
    @NamedQuery(name = "DocumentoEstimacionFlujoCajaConsolidador.findByFlujoCajaConsolidador", query = "SELECT d FROM DocumentoEstimacionFlujoCajaConsolidador d WHERE d.flujoCajaConsolidador = :flujoCajaConsolidador")})
public class DocumentoEstimacionFlujoCajaConsolidador implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id_documento_estimacion_flujo_caja_consolidador",unique=true, nullable=false)
    @GeneratedValue
    private Integer idDocumentoEstimacionFlujoCajaConsolidador;
    @JoinColumn(name = "tipo_documento", referencedColumnName = "id_tipo_documento")
    @ManyToOne(optional = false)
    private TipoDocumento tipoDocumento;
    @JoinColumn(name = "servicio", referencedColumnName = "id")
    @ManyToOne
    private ServicioSalud servicio;
    @JoinColumn(name = "documento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ReferenciaDocumento documento;
    @JoinColumn(name = "flujo_caja_consolidador", referencedColumnName = "id_flujo_caja_consolidador")
    @ManyToOne(optional = false)
    private FlujoCajaConsolidador flujoCajaConsolidador;
    
    public DocumentoEstimacionFlujoCajaConsolidador() {
    }

    public DocumentoEstimacionFlujoCajaConsolidador(Integer idDocumentoEstimacionFlujoCajaConsolidador) {
        this.idDocumentoEstimacionFlujoCajaConsolidador = idDocumentoEstimacionFlujoCajaConsolidador;
    }

    public Integer getIdDocumentoEstimacionFlujoCajaConsolidador() {
        return idDocumentoEstimacionFlujoCajaConsolidador;
    }

    public void setIdDocumentoEstimacionFlujoCajaConsolidador(Integer idDocumentoEstimacionFlujoCajaConsolidador) {
        this.idDocumentoEstimacionFlujoCajaConsolidador = idDocumentoEstimacionFlujoCajaConsolidador;
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

    public FlujoCajaConsolidador getFlujoCajaConsolidador() {
        return flujoCajaConsolidador;
    }

    public void setFlujoCajaConsolidador(FlujoCajaConsolidador flujoCajaConsolidador) {
        this.flujoCajaConsolidador = flujoCajaConsolidador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDocumentoEstimacionFlujoCajaConsolidador != null ? idDocumentoEstimacionFlujoCajaConsolidador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentoEstimacionFlujoCajaConsolidador)) {
            return false;
        }
        DocumentoEstimacionFlujoCajaConsolidador other = (DocumentoEstimacionFlujoCajaConsolidador) object;
        if ((this.idDocumentoEstimacionFlujoCajaConsolidador == null && other.idDocumentoEstimacionFlujoCajaConsolidador != null) || (this.idDocumentoEstimacionFlujoCajaConsolidador != null && !this.idDocumentoEstimacionFlujoCajaConsolidador.equals(other.idDocumentoEstimacionFlujoCajaConsolidador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.DocumentoEstimacionFlujoCajaConsolidador[ idDocumentoEstimacionFlujoCajaConsolidador=" + idDocumentoEstimacionFlujoCajaConsolidador + " ]";
    }
    
}