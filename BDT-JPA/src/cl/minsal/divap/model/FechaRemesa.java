package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Set;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author cmurillo
 */
@Entity
@Table(name = "fecha_remesa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FechaRemesa.findAll", query = "SELECT f FROM FechaRemesa f"),
    @NamedQuery(name = "FechaRemesa.findAllOrderByDia", query = "SELECT f FROM FechaRemesa f order by f.dia.dia asc"),
    @NamedQuery(name = "FechaRemesa.findByProgramaOrderByDia", query = "SELECT f FROM FechaRemesa f JOIN f.programaFechaRemesaSet p WHERE p.programa.id = :idPrograma order by f.dia.dia asc"),
    @NamedQuery(name = "FechaRemesa.findById", query = "SELECT f FROM FechaRemesa f WHERE f.id = :id"),
    @NamedQuery(name = "FechaRemesa.findByDia", query = "SELECT f FROM FechaRemesa f WHERE f.dia.dia = :dia")})
public class FechaRemesa implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "dia", referencedColumnName = "id")
    @ManyToOne
    private Dia dia;
    @OneToMany(mappedBy = "fechaRemesa")
    private Set<ProgramaFechaRemesa> programaFechaRemesaSet;

    public FechaRemesa() {
    }

    public FechaRemesa(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Dia getDia() {
        return dia;
    }

    public void setDia(Dia dia) {
        this.dia = dia;
    }

    @XmlTransient
    public Set<ProgramaFechaRemesa> getProgramaFechaRemesaSet() {
        return programaFechaRemesaSet;
    }

    public void setProgramaFechaRemesaSet(Set<ProgramaFechaRemesa> programaFechaRemesaSet) {
        this.programaFechaRemesaSet = programaFechaRemesaSet;
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
        if (!(object instanceof FechaRemesa)) {
            return false;
        }
        FechaRemesa other = (FechaRemesa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.FechaRemesa[ id=" + id + " ]";
    }
   
}