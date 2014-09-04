package cl.minsal.divap.model;


import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Aldo
 */
@Entity
@Table(name = "estado_programa_flujocaja")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadoProgramaFlujocaja.findAll", query = "SELECT e FROM EstadoProgramaFlujocaja e"),
    @NamedQuery(name = "EstadoProgramaFlujocaja.findById", query = "SELECT e FROM EstadoProgramaFlujocaja e WHERE e.id = :id"),
    @NamedQuery(name = "EstadoProgramaFlujocaja.findByNombre", query = "SELECT e FROM EstadoProgramaFlujocaja e WHERE e.nombre = :nombre")})
public class EstadoProgramaFlujocaja implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nombre")
    private String nombre;

    public EstadoProgramaFlujocaja() {
    }

    public EstadoProgramaFlujocaja(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
        if (!(object instanceof EstadoProgramaFlujocaja)) {
            return false;
        }
        EstadoProgramaFlujocaja other = (EstadoProgramaFlujocaja) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication2.EstadoProgramaFlujocaja[ id=" + id + " ]";
    }
    
}
