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

/**
 *
 * @author cmurillo
 */
@Entity
@Table(name = "programa_fecha_remesa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProgramaFechaRemesa.findAll", query = "SELECT p FROM ProgramaFechaRemesa p"),
    @NamedQuery(name = "ProgramaFechaRemesa.findByPrograma", query = "SELECT p FROM ProgramaFechaRemesa p WHERE p.programa.id = :idPrograma"),
    @NamedQuery(name = "ProgramaFechaRemesa.findById", query = "SELECT p FROM ProgramaFechaRemesa p WHERE p.id = :id")})
public class ProgramaFechaRemesa implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "programa", referencedColumnName = "id")
    @ManyToOne
    private Programa programa;
    @JoinColumn(name = "fecha_remesa", referencedColumnName = "id")
    @ManyToOne
    private FechaRemesa fechaRemesa;

    public ProgramaFechaRemesa() {
    }

    public ProgramaFechaRemesa(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }

    public FechaRemesa getFechaRemesa() {
        return fechaRemesa;
    }

    public void setFechaRemesa(FechaRemesa fechaRemesa) {
        this.fechaRemesa = fechaRemesa;
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
        if (!(object instanceof ProgramaFechaRemesa)) {
            return false;
        }
        ProgramaFechaRemesa other = (ProgramaFechaRemesa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ProgramaFechaRemesa[ id=" + id + " ]";
    }
   
}