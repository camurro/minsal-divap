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
@Table(name = "documento_reliquidacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentoReliquidacion.findAll", query = "SELECT d FROM DocumentoReliquidacion d"),
    @NamedQuery(name = "DocumentoReliquidacion.findByIdDocumentoReliquidacion", query = "SELECT d FROM DocumentoReliquidacion d WHERE d.idDocumentoReliquidacion = :idDocumentoReliquidacion")})
public class DocumentoReliquidacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
   	@Column(name="id_documento_reliquidacion", unique=true, nullable=false)
   	@GeneratedValue
    private Integer idDocumentoReliquidacion;
    @JoinColumn(name = "id_tipo_documento", referencedColumnName = "id_tipo_documento")
    @ManyToOne(optional = false)
    private TipoDocumento idTipoDocumento;
    @JoinColumn(name = "servicio", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ServicioSalud servicio;
    @JoinColumn(name = "id_reliquidacion", referencedColumnName = "id_reliquidacion")
    @ManyToOne(optional = false)
    private Reliquidacion idReliquidacion;
    @JoinColumn(name = "id_documento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ReferenciaDocumento idDocumento;
    @JoinColumn(name = "establecimiento", referencedColumnName = "id")
    @ManyToOne
    private Establecimiento establecimiento;
    @JoinColumn(name = "comuna", referencedColumnName = "id")
    @ManyToOne
    private Comuna comuna;

    public DocumentoReliquidacion() {
    }

    public DocumentoReliquidacion(Integer idDocumentoReliquidacion) {
        this.idDocumentoReliquidacion = idDocumentoReliquidacion;
    }

    public Integer getIdDocumentoReliquidacion() {
        return idDocumentoReliquidacion;
    }

    public void setIdDocumentoReliquidacion(Integer idDocumentoReliquidacion) {
        this.idDocumentoReliquidacion = idDocumentoReliquidacion;
    }

    public TipoDocumento getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(TipoDocumento idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public ServicioSalud getServicio() {
        return servicio;
    }

    public void setServicio(ServicioSalud servicio) {
        this.servicio = servicio;
    }

    public Reliquidacion getIdReliquidacion() {
        return idReliquidacion;
    }

    public void setIdReliquidacion(Reliquidacion idReliquidacion) {
        this.idReliquidacion = idReliquidacion;
    }

    public ReferenciaDocumento getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(ReferenciaDocumento idDocumento) {
        this.idDocumento = idDocumento;
    }

    public Establecimiento getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(Establecimiento establecimiento) {
        this.establecimiento = establecimiento;
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
        hash += (idDocumentoReliquidacion != null ? idDocumentoReliquidacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DocumentoReliquidacion)) {
            return false;
        }
        DocumentoReliquidacion other = (DocumentoReliquidacion) object;
        if ((this.idDocumentoReliquidacion == null && other.idDocumentoReliquidacion != null) || (this.idDocumentoReliquidacion != null && !this.idDocumentoReliquidacion.equals(other.idDocumentoReliquidacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.DocumentoReliquidacion[ idDocumentoReliquidacion=" + idDocumentoReliquidacion + " ]";
    }
   
}
