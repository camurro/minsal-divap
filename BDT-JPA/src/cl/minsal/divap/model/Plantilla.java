package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cmurillo
 */
@Entity
@Table(name = "plantilla")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Plantilla.findAll", query = "SELECT p FROM Plantilla p"),
    @NamedQuery(name = "Plantilla.findByIdPlantilla", query = "SELECT p FROM Plantilla p WHERE p.idPlantilla = :idPlantilla"),
    @NamedQuery(name = "Plantilla.findByFechaCreacion", query = "SELECT p FROM Plantilla p WHERE p.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "Plantilla.findByFechaVigencia", query = "SELECT p FROM Plantilla p WHERE p.fechaVigencia = :fechaVigencia")})
public class Plantilla implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
      @Column(name="id_plantilla", unique=true, nullable=false)
      @GeneratedValue
    private Integer idPlantilla;
    @Basic(optional = false)
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "fecha_vigencia")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVigencia;
    @JoinColumn(name = "tipo_plantilla", referencedColumnName = "id_tipo_documento")
    @ManyToOne(optional = false)
    private TipoDocumento tipoPlantilla;
    @JoinColumn(name = "documento", referencedColumnName = "id")
    @ManyToOne
    private ReferenciaDocumento documento;
    @JoinColumn(name = "id_programa", referencedColumnName = "id")
    @ManyToOne
    private Programa idPrograma;

    public Plantilla() {
    }

    public Plantilla(Integer idPlantilla) {
        this.idPlantilla = idPlantilla;
    }

    public Plantilla(Integer idPlantilla, Date fechaCreacion) {
        this.idPlantilla = idPlantilla;
        this.fechaCreacion = fechaCreacion;
    }

    public Integer getIdPlantilla() {
        return idPlantilla;
    }

    public void setIdPlantilla(Integer idPlantilla) {
        this.idPlantilla = idPlantilla;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaVigencia() {
        return fechaVigencia;
    }

    public void setFechaVigencia(Date fechaVigencia) {
        this.fechaVigencia = fechaVigencia;
    }

    public TipoDocumento getTipoPlantilla() {
        return tipoPlantilla;
    }

    public void setTipoPlantilla(TipoDocumento tipoPlantilla) {
        this.tipoPlantilla = tipoPlantilla;
    }

    public ReferenciaDocumento getDocumento() {
        return documento;
    }

    public void setDocumento(ReferenciaDocumento documento) {
        this.documento = documento;
    }

    public Programa getIdPrograma() {
        return idPrograma;
    }

    public void setIdPrograma(Programa idPrograma) {
        this.idPrograma = idPrograma;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPlantilla != null ? idPlantilla.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Plantilla)) {
            return false;
        }
        Plantilla other = (Plantilla) object;
        if ((this.idPlantilla == null && other.idPlantilla != null) || (this.idPlantilla != null && !this.idPlantilla.equals(other.idPlantilla))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.Plantilla[ idPlantilla=" + idPlantilla + " ]";
    }
    
}