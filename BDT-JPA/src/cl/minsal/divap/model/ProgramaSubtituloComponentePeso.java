package cl.minsal.divap.model;

import java.io.Serializable;

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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cmurillo
 */
@Entity
@Table(name = "programa_subtitulo_componente_peso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProgramaSubtituloComponentePeso.findAll", query = "SELECT p FROM ProgramaSubtituloComponentePeso p"),
    @NamedQuery(name = "ProgramaSubtituloComponentePeso.findByIdProgramaSubtituloComponentePeso", query = "SELECT p FROM ProgramaSubtituloComponentePeso p WHERE p.idProgramaSubtituloComponentePeso = :idProgramaSubtituloComponentePeso"),
    @NamedQuery(name = "ProgramaSubtituloComponentePeso.findByPeso", query = "SELECT p FROM ProgramaSubtituloComponentePeso p WHERE p.peso = :peso"),
    @NamedQuery(name = "ProgramaSubtituloComponentePeso.findProgramaSubtituloComponentePesoByProgramaServicioComponenteSubtitulo", query = "SELECT p FROM ProgramaSubtituloComponentePeso p WHERE p.programa.idProgramaAno = :idProgramaAno and p.servicio.id = :idServicio and p.componente.id = :idComponente and p.subtitulo.idTipoSubtitulo = :idSubtitulo")})
public class ProgramaSubtituloComponentePeso implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
  	@Column(name="id_programa_subtitulo_componente_peso", unique=true, nullable=false)
  	@GeneratedValue
    private Integer idProgramaSubtituloComponentePeso;
    @Basic(optional = false)
    @Column(name = "peso")
    private int peso;
    @JoinColumn(name = "subtitulo", referencedColumnName = "id_tipo_subtitulo")
    @ManyToOne(optional = false)
    private TipoSubtitulo subtitulo;
    @JoinColumn(name = "servicio", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ServicioSalud servicio;
    @JoinColumn(name = "programa", referencedColumnName = "id_programa_ano")
    @ManyToOne(optional = false)
    private ProgramaAno programa;
    @JoinColumn(name = "componente", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Componente componente;

    public ProgramaSubtituloComponentePeso() {
    }

    public ProgramaSubtituloComponentePeso(Integer idProgramaSubtituloComponentePeso) {
        this.idProgramaSubtituloComponentePeso = idProgramaSubtituloComponentePeso;
    }

    public ProgramaSubtituloComponentePeso(Integer idProgramaSubtituloComponentePeso, int peso) {
        this.idProgramaSubtituloComponentePeso = idProgramaSubtituloComponentePeso;
        this.peso = peso;
    }

    public Integer getIdProgramaSubtituloComponentePeso() {
        return idProgramaSubtituloComponentePeso;
    }

    public void setIdProgramaSubtituloComponentePeso(Integer idProgramaSubtituloComponentePeso) {
        this.idProgramaSubtituloComponentePeso = idProgramaSubtituloComponentePeso;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public TipoSubtitulo getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(TipoSubtitulo subtitulo) {
        this.subtitulo = subtitulo;
    }
    
    public ServicioSalud getServicio() {
        return servicio;
    }

    public void setServicio(ServicioSalud servicio) {
        this.servicio = servicio;
    }

    public ProgramaAno getPrograma() {
        return programa;
    }

    public void setPrograma(ProgramaAno programa) {
        this.programa = programa;
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
        hash += (idProgramaSubtituloComponentePeso != null ? idProgramaSubtituloComponentePeso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProgramaSubtituloComponentePeso)) {
            return false;
        }
        ProgramaSubtituloComponentePeso other = (ProgramaSubtituloComponentePeso) object;
        if ((this.idProgramaSubtituloComponentePeso == null && other.idProgramaSubtituloComponentePeso != null) || (this.idProgramaSubtituloComponentePeso != null && !this.idProgramaSubtituloComponentePeso.equals(other.idProgramaSubtituloComponentePeso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ProgramaSubtituloComponentePeso[ idProgramaSubtituloComponentePeso=" + idProgramaSubtituloComponentePeso + " ]";
    }
    
}