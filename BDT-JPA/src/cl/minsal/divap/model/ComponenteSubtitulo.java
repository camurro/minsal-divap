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
@Table(name = "componente_subtitulo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ComponenteSubtitulo.findAll", query = "SELECT c FROM ComponenteSubtitulo c"),
    @NamedQuery(name = "ComponenteSubtitulo.findByIdComponenteSubtitulo", query = "SELECT c FROM ComponenteSubtitulo c WHERE c.idComponenteSubtitulo = :idComponenteSubtitulo")})
public class ComponenteSubtitulo implements Serializable {
    private static final long serialVersionUID = 1L;
	@Id
	@Column(name="id_componente_subtitulo", unique=true, nullable=false)
	@GeneratedValue
    private Integer idComponenteSubtitulo;
    @JoinColumn(name = "subtitulo", referencedColumnName = "id_tipo_subtitulo")
    @ManyToOne(optional = false)
    private TipoSubtitulo subtitulo;
    @JoinColumn(name = "componente", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Componente componente;

    public ComponenteSubtitulo() {
    }

    public ComponenteSubtitulo(Integer idComponenteSubtitulo) {
        this.idComponenteSubtitulo = idComponenteSubtitulo;
    }

    public Integer getIdComponenteSubtitulo() {
        return idComponenteSubtitulo;
    }

    public void setIdComponenteSubtitulo(Integer idComponenteSubtitulo) {
        this.idComponenteSubtitulo = idComponenteSubtitulo;
    }

    public TipoSubtitulo getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(TipoSubtitulo subtitulo) {
        this.subtitulo = subtitulo;
    }

    public Componente getComponente() {
        return componente;
    }

    public void setComponente(Componente componente) {
        this.componente = componente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idComponenteSubtitulo != null ? idComponenteSubtitulo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ComponenteSubtitulo)) {
            return false;
        }
        ComponenteSubtitulo other = (ComponenteSubtitulo) object;
        if ((this.idComponenteSubtitulo == null && other.idComponenteSubtitulo != null) || (this.idComponenteSubtitulo != null && !this.idComponenteSubtitulo.equals(other.idComponenteSubtitulo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ComponenteSubtitulo[ idComponenteSubtitulo=" + idComponenteSubtitulo + " ]";
    }
    
}