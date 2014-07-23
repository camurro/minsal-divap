package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author cmurillo
 */
@Entity
@Table(name = "tipo_plantilla")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoPlantilla.findAll", query = "SELECT t FROM TipoPlantilla t"),
    @NamedQuery(name = "TipoPlantilla.findByIdTipoPlantilla", query = "SELECT t FROM TipoPlantilla t WHERE t.idTipoPlantilla = :idTipoPlantilla"),
    @NamedQuery(name = "TipoPlantilla.findByDescripcion", query = "SELECT t FROM TipoPlantilla t WHERE t.descripcion = :descripcion")})
public class TipoPlantilla implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
  	@Column(name="id_tipo_plantilla", unique=true, nullable=false)
  	@GeneratedValue
    private Integer idTipoPlantilla;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoPlantilla")
    private Collection<Plantilla> plantillaCollection;

    public TipoPlantilla() {
    }

    public TipoPlantilla(Integer idTipoPlantilla) {
        this.idTipoPlantilla = idTipoPlantilla;
    }

    public TipoPlantilla(Integer idTipoPlantilla, String descripcion) {
        this.idTipoPlantilla = idTipoPlantilla;
        this.descripcion = descripcion;
    }

    public Integer getIdTipoPlantilla() {
        return idTipoPlantilla;
    }

    public void setIdTipoPlantilla(Integer idTipoPlantilla) {
        this.idTipoPlantilla = idTipoPlantilla;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public Collection<Plantilla> getPlantillaCollection() {
        return plantillaCollection;
    }

    public void setPlantillaCollection(Collection<Plantilla> plantillaCollection) {
        this.plantillaCollection = plantillaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoPlantilla != null ? idTipoPlantilla.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TipoPlantilla)) {
            return false;
        }
        TipoPlantilla other = (TipoPlantilla) object;
        if ((this.idTipoPlantilla == null && other.idTipoPlantilla != null) || (this.idTipoPlantilla != null && !this.idTipoPlantilla.equals(other.idTipoPlantilla))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.TipoPlantilla[ idTipoPlantilla=" + idTipoPlantilla + " ]";
    }
    
}
