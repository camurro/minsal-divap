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
@Table(name = "programas_reforzamiento_seguimiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProgramasReforzamientoSeguimiento.findAll", query = "SELECT p FROM ProgramasReforzamientoSeguimiento p"),
    @NamedQuery(name = "ProgramasReforzamientoSeguimiento.findById", query = "SELECT p FROM ProgramasReforzamientoSeguimiento p WHERE p.id = :id")})
public class ProgramasReforzamientoSeguimiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="id", unique=true, nullable=false)
    @GeneratedValue
    private Integer id;
    @JoinColumn(name = "seguimiento", referencedColumnName = "id")
    @ManyToOne
    private Seguimiento seguimiento;
    @JoinColumn(name = "id_programa_ano", referencedColumnName = "id_programa_ano")
    @ManyToOne
    private ProgramaAno idProgramaAno;

    public ProgramasReforzamientoSeguimiento() {
    }

    public ProgramasReforzamientoSeguimiento(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Seguimiento getSeguimiento() {
        return seguimiento;
    }

    public void setSeguimiento(Seguimiento seguimiento) {
        this.seguimiento = seguimiento;
    }

    public ProgramaAno getIdProgramaAno() {
        return idProgramaAno;
    }

    public void setIdProgramaAno(ProgramaAno idProgramaAno) {
        this.idProgramaAno = idProgramaAno;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProgramasReforzamientoSeguimiento)) {
            return false;
        }
        ProgramasReforzamientoSeguimiento other = (ProgramasReforzamientoSeguimiento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ProgramasReforzamientoSeguimiento[ id=" + id + " ]";
    }
    
}
