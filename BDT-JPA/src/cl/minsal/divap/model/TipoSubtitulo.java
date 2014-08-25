package cl.minsal.divap.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cmurillo
 */
@Entity
@Table(name = "tipo_subtitulo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoSubtitulo.findAll", query = "SELECT t FROM TipoSubtitulo t"),
    @NamedQuery(name = "TipoSubtitulo.findByIdTipoSubtitulo", query = "SELECT t FROM TipoSubtitulo t WHERE t.idTipoSubtitulo = :idTipoSubtitulo"),
    @NamedQuery(name = "TipoSubtitulo.findByNombreSubtitulo", query = "SELECT t FROM TipoSubtitulo t WHERE t.nombreSubtitulo = :nombreSubtitulo")})
public class TipoSubtitulo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tipo_subtitulo")
    private Integer idTipoSubtitulo;
    @Basic(optional = false)
    @Column(name = "nombre_subtitulo")
    private String nombreSubtitulo;

    public TipoSubtitulo() {
    }

    public TipoSubtitulo(Integer idTipoSubtitulo) {
        this.idTipoSubtitulo = idTipoSubtitulo;
    }

    public TipoSubtitulo(Integer idTipoSubtitulo, String nombreSubtitulo) {
        this.idTipoSubtitulo = idTipoSubtitulo;
        this.nombreSubtitulo = nombreSubtitulo;
    }

    public Integer getIdTipoSubtitulo() {
        return idTipoSubtitulo;
    }

    public void setIdTipoSubtitulo(Integer idTipoSubtitulo) {
        this.idTipoSubtitulo = idTipoSubtitulo;
    }

    public String getNombreSubtitulo() {
        return nombreSubtitulo;
    }

    public void setNombreSubtitulo(String nombreSubtitulo) {
        this.nombreSubtitulo = nombreSubtitulo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoSubtitulo != null ? idTipoSubtitulo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TipoSubtitulo)) {
            return false;
        }
        TipoSubtitulo other = (TipoSubtitulo) object;
        if ((this.idTipoSubtitulo == null && other.idTipoSubtitulo != null) || (this.idTipoSubtitulo != null && !this.idTipoSubtitulo.equals(other.idTipoSubtitulo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.TipoSubtitulo[ idTipoSubtitulo=" + idTipoSubtitulo + " ]";
    }
    
}