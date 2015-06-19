package cl.minsal.divap.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "programa_componente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProgramaComponente.findAll", query = "SELECT p FROM ProgramaComponente p"),
    @NamedQuery(name = "ProgramaComponente.findByIdProgramaComponente", query = "SELECT p FROM ProgramaComponente p WHERE p.idProgramaComponente = :idProgramaComponente")})
public class ProgramaComponente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_programa_componente")
    private Integer idProgramaComponente;
    @JoinColumn(name = "programa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Programa programa;
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

    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
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
