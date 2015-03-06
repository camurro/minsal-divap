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
@Table(name = "remesa_convenios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RemesaConvenios.findAll", query = "SELECT r FROM RemesaConvenios r"),
    @NamedQuery(name = "RemesaConvenios.findByIdRemesaConvenio", query = "SELECT r FROM RemesaConvenios r WHERE r.idRemesaConvenio = :idRemesaConvenio")})
public class RemesaConvenios implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
   	@Column(name="id_remesa_convenio", unique=true, nullable=false)
   	@GeneratedValue
    private Integer idRemesaConvenio;
    @JoinColumn(name = "remesa", referencedColumnName = "id_detalle_remesa")
    @ManyToOne(optional = false)
    private DetalleRemesas remesa;
    @JoinColumn(name = "convenio_servicio", referencedColumnName = "id_convenio_servicio")
    @ManyToOne
    private ConvenioServicio convenioServicio;
    @JoinColumn(name = "convenio_comuna", referencedColumnName = "id_convenio_comuna")
    @ManyToOne
    private ConvenioComuna convenioComuna;

    public RemesaConvenios() {
    }

    public RemesaConvenios(Integer idRemesaConvenio) {
        this.idRemesaConvenio = idRemesaConvenio;
    }

    public Integer getIdRemesaConvenio() {
        return idRemesaConvenio;
    }

    public void setIdRemesaConvenio(Integer idRemesaConvenio) {
        this.idRemesaConvenio = idRemesaConvenio;
    }

    public DetalleRemesas getRemesa() {
        return remesa;
    }

    public void setRemesa(DetalleRemesas remesa) {
        this.remesa = remesa;
    }

    public ConvenioServicio getConvenioServicio() {
        return convenioServicio;
    }

    public void setConvenioServicio(ConvenioServicio convenioServicio) {
        this.convenioServicio = convenioServicio;
    }

    public ConvenioComuna getConvenioComuna() {
        return convenioComuna;
    }

    public void setConvenioComuna(ConvenioComuna convenioComuna) {
        this.convenioComuna = convenioComuna;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRemesaConvenio != null ? idRemesaConvenio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RemesaConvenios)) {
            return false;
        }
        RemesaConvenios other = (RemesaConvenios) object;
        if ((this.idRemesaConvenio == null && other.idRemesaConvenio != null) || (this.idRemesaConvenio != null && !this.idRemesaConvenio.equals(other.idRemesaConvenio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.RemesaConvenios[ idRemesaConvenio=" + idRemesaConvenio + " ]";
    }
    
}
