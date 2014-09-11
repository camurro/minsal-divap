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
@Table(name = "programa_subtitulo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProgramaSubtitulo.findAll", query = "SELECT p FROM ProgramaSubtitulo p"),
    @NamedQuery(name = "ProgramaSubtitulo.findByIdProgramaSubtitulo", query = "SELECT p FROM ProgramaSubtitulo p WHERE p.idProgramaSubtitulo = :idProgramaSubtitulo")})
public class ProgramaSubtitulo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
	@Column(name="id_programa_subtitulo", unique=true, nullable=false)
	@GeneratedValue
    private Integer idProgramaSubtitulo;
    @JoinColumn(name = "subtitulo", referencedColumnName = "id_tipo_subtitulo")
    @ManyToOne(optional = false)
    private TipoSubtitulo subtitulo;
    @JoinColumn(name = "programa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Programa programa;

    public ProgramaSubtitulo() {
    }

    public ProgramaSubtitulo(Integer idProgramaSubtitulo) {
        this.idProgramaSubtitulo = idProgramaSubtitulo;
    }

    public Integer getIdProgramaSubtitulo() {
        return idProgramaSubtitulo;
    }

    public void setIdProgramaSubtitulo(Integer idProgramaSubtitulo) {
        this.idProgramaSubtitulo = idProgramaSubtitulo;
    }

    public TipoSubtitulo getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(TipoSubtitulo subtitulo) {
        this.subtitulo = subtitulo;
    }

    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProgramaSubtitulo != null ? idProgramaSubtitulo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ProgramaSubtitulo)) {
            return false;
        }
        ProgramaSubtitulo other = (ProgramaSubtitulo) object;
        if ((this.idProgramaSubtitulo == null && other.idProgramaSubtitulo != null) || (this.idProgramaSubtitulo != null && !this.idProgramaSubtitulo.equals(other.idProgramaSubtitulo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ProgramaSubtitulo[ idProgramaSubtitulo=" + idProgramaSubtitulo + " ]";
    }
    
}

