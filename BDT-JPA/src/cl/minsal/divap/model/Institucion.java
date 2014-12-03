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
@Table(name = "institucion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Institucion.findAll", query = "SELECT i FROM Institucion i"),
    @NamedQuery(name = "Institucion.findByIdInstitucion", query = "SELECT i FROM Institucion i WHERE i.idInstitucion = :idInstitucion"),
    @NamedQuery(name = "Institucion.findByNombre", query = "SELECT i FROM Institucion i WHERE i.nombre = :nombre")})
public class Institucion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
	@Column(name="id_institucion", unique=true, nullable=false)
	@GeneratedValue
    private Integer idInstitucion;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @JoinColumn(name = "director", referencedColumnName = "id_persona")
    @ManyToOne
    private Persona director;
    @JoinColumn(name = "email", referencedColumnName = "id_email")
    @ManyToOne
    private Email email;

    public Institucion() {
    }

    public Institucion(Integer idInstitucion) {
        this.idInstitucion = idInstitucion;
    }

    public Institucion(Integer idInstitucion, String nombre) {
        this.idInstitucion = idInstitucion;
        this.nombre = nombre;
    }

    public Integer getIdInstitucion() {
        return idInstitucion;
    }

    public void setIdInstitucion(Integer idInstitucion) {
        this.idInstitucion = idInstitucion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Persona getDirector() {
        return director;
    }

    public void setDirector(Persona director) {
        this.director = director;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idInstitucion != null ? idInstitucion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Institucion)) {
            return false;
        }
        Institucion other = (Institucion) object;
        if ((this.idInstitucion == null && other.idInstitucion != null) || (this.idInstitucion != null && !this.idInstitucion.equals(other.idInstitucion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.Institucion[ idInstitucion=" + idInstitucion + " ]";
    }
    
}
