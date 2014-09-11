package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
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
@Table(name = "tipo_comuna")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoComuna.findAll", query = "SELECT t FROM TipoComuna t"),
    @NamedQuery(name = "TipoComuna.findByIdTipoComuna", query = "SELECT t FROM TipoComuna t WHERE t.idTipoComuna = :idTipoComuna"),
    @NamedQuery(name = "TipoComuna.findByDescripcion", query = "SELECT t FROM TipoComuna t WHERE t.descripcion = :descripcion")})
public class TipoComuna implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
  	@Column(name="id_tipo_comuna", unique=true, nullable=false)
  	@GeneratedValue
    private Integer idTipoComuna;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(mappedBy = "clasificacion")
    private Collection<AntecendentesComuna> antecendentesComunaCollection;

    public TipoComuna() {
    }

    public TipoComuna(Integer idTipoComuna) {
        this.idTipoComuna = idTipoComuna;
    }

    public TipoComuna(Integer idTipoComuna, String descripcion) {
        this.idTipoComuna = idTipoComuna;
        this.descripcion = descripcion;
    }

    public Integer getIdTipoComuna() {
        return idTipoComuna;
    }

    public void setIdTipoComuna(Integer idTipoComuna) {
        this.idTipoComuna = idTipoComuna;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public Collection<AntecendentesComuna> getAntecendentesComunaCollection() {
        return antecendentesComunaCollection;
    }

    public void setAntecendentesComunaCollection(Collection<AntecendentesComuna> antecendentesComunaCollection) {
        this.antecendentesComunaCollection = antecendentesComunaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoComuna != null ? idTipoComuna.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TipoComuna)) {
            return false;
        }
        TipoComuna other = (TipoComuna) object;
        if ((this.idTipoComuna == null && other.idTipoComuna != null) || (this.idTipoComuna != null && !this.idTipoComuna.equals(other.idTipoComuna))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.TipoComuna[ idTipoComuna=" + idTipoComuna + " ]";
    }
    
}