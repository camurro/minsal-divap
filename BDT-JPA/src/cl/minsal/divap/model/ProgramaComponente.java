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
 * @author francisco
 */
@Entity
@Table(name = "programa_componente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProgramaComponente.findAll", query = "SELECT p FROM ProgramaComponente p"),
    @NamedQuery(name = "ProgramaComponente.findByIdProgramaComponente", query = "SELECT p FROM ProgramaComponente p WHERE p.idProgramaComponente = :idProgramaComponente"),
    @NamedQuery(name = "ProgramaComponente.findByIdProgramaAno", query = "SELECT p FROM ProgramaComponente p WHERE p.programa.idProgramaAno =:idProgramaAno"),
    @NamedQuery(name = "ProgramaComponente.findComponentesByIdProgramaAno", query = "SELECT DISTINCT(p.componente) FROM ProgramaComponente p WHERE p.programa.idProgramaAno =:idProgramaAno"),
    @NamedQuery(name = "ProgramaComponente.findByIdProgramaAnoIdComponente", query = "SELECT p FROM ProgramaComponente p WHERE p.programa.idProgramaAno =:idProgramaAno and p.componente.id =:idComponente"),
    @NamedQuery(name = "ProgramaComponente.findByIdProgramaAnoNombreComponente", query = "SELECT p FROM ProgramaComponente p WHERE p.programa.idProgramaAno =:idProgramaAno and p.componente.nombre =:nombreComponente")})
public class ProgramaComponente implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name="id_programa_componente", unique=true, nullable=false)
    @GeneratedValue
    private Integer idProgramaComponente;
    @JoinColumn(name = "programa", referencedColumnName = "id_programa_ano")
    @ManyToOne(optional = false)
    private ProgramaAno programa;
    @JoinColumn(name = "componente", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Componente componente;

    public ProgramaComponente() {
    }

    public ProgramaComponente(Integer idProgramaComponente) {
        this.idProgramaComponente = idProgramaComponente;
    }

    public Integer getIdProgramaComponente() {
        return idProgramaComponente;
    }

    public void setIdProgramaComponente(Integer idProgramaComponente) {
        this.idProgramaComponente = idProgramaComponente;
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
        hash += (idProgramaComponente != null ? idProgramaComponente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProgramaComponente)) {
            return false;
        }
        ProgramaComponente other = (ProgramaComponente) object;
        if ((this.idProgramaComponente == null && other.idProgramaComponente != null) || (this.idProgramaComponente != null && !this.idProgramaComponente.equals(other.idProgramaComponente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ProgramaComponente[ idProgramaComponente=" + idProgramaComponente + " ]";
    }
    
}