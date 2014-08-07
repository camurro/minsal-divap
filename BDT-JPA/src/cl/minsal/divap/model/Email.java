package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
@Table(name = "email")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Email.findAll", query = "SELECT e FROM Email e"),
    @NamedQuery(name = "Email.findByIdEmail", query = "SELECT e FROM Email e WHERE e.idEmail = :idEmail"),
    @NamedQuery(name = "Email.findByValor", query = "SELECT e FROM Email e WHERE LOWER(e.valor) = :valor")})
public class Email implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
   	@Column(name="id_email", unique=true, nullable=false)
   	@GeneratedValue
    private Integer idEmail;
    @Basic(optional = false)
    @Column(name = "valor")
    private String valor;
    @OneToMany(mappedBy = "email")
    private Collection<Usuario> usuarioCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "destinatario")
    private Collection<Destinatarios> destinatariosCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mailFrom")
    private Collection<Seguimiento> seguimientoCollection;

    public Email() {
    }

    public Email(Integer idEmail) {
        this.idEmail = idEmail;
    }

    public Email(Integer idEmail, String valor) {
        this.idEmail = idEmail;
        this.valor = valor;
    }

    public Integer getIdEmail() {
        return idEmail;
    }

    public void setIdEmail(Integer idEmail) {
        this.idEmail = idEmail;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @XmlTransient
    public Collection<Usuario> getUsuarioCollection() {
        return usuarioCollection;
    }

    public void setUsuarioCollection(Collection<Usuario> usuarioCollection) {
        this.usuarioCollection = usuarioCollection;
    }

    @XmlTransient
    public Collection<Destinatarios> getDestinatariosCollection() {
        return destinatariosCollection;
    }

    public void setDestinatariosCollection(Collection<Destinatarios> destinatariosCollection) {
        this.destinatariosCollection = destinatariosCollection;
    }

    @XmlTransient
    public Collection<Seguimiento> getSeguimientoCollection() {
        return seguimientoCollection;
    }

    public void setSeguimientoCollection(Collection<Seguimiento> seguimientoCollection) {
        this.seguimientoCollection = seguimientoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEmail != null ? idEmail.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Email)) {
            return false;
        }
        Email other = (Email) object;
        if ((this.idEmail == null && other.idEmail != null) || (this.idEmail != null && !this.idEmail.equals(other.idEmail))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.Email[ idEmail=" + idEmail + " ]";
    }
    
}
