package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author cmurillo
 */
@Entity
@Table(name = "seguimiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Seguimiento.findAll", query = "SELECT s FROM Seguimiento s"),
    @NamedQuery(name = "Seguimiento.findById", query = "SELECT s FROM Seguimiento s WHERE s.id = :id"),
    @NamedQuery(name = "Seguimiento.findByIdInstancia", query = "SELECT s FROM Seguimiento s WHERE s.idInstancia = :idInstancia"),
    @NamedQuery(name = "Seguimiento.findByMailFrom", query = "SELECT s FROM Seguimiento s WHERE s.mailFrom = :mailFrom"),
    @NamedQuery(name = "Seguimiento.findByMailTo", query = "SELECT s FROM Seguimiento s WHERE s.mailTo = :mailTo"),
    @NamedQuery(name = "Seguimiento.findBySubject", query = "SELECT s FROM Seguimiento s WHERE s.subject = :subject"),
    @NamedQuery(name = "Seguimiento.findByCc", query = "SELECT s FROM Seguimiento s WHERE s.cc = :cc"),
    @NamedQuery(name = "Seguimiento.findByCco", query = "SELECT s FROM Seguimiento s WHERE s.cco = :cco"),
    @NamedQuery(name = "Seguimiento.findByBody", query = "SELECT s FROM Seguimiento s WHERE s.body = :body"),
    @NamedQuery(name = "Seguimiento.findByFechaEnvio", query = "SELECT s FROM Seguimiento s WHERE s.fechaEnvio = :fechaEnvio")})
public class Seguimiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "id_instancia")
    private short idInstancia;
    @Basic(optional = false)
    @Column(name = "mail_from")
    private String mailFrom;
    @Basic(optional = false)
    @Column(name = "mail_to")
    private String mailTo;
    @Basic(optional = false)
    @Column(name = "subject")
    private String subject;
    @Column(name = "cc")
    private String cc;
    @Column(name = "cco")
    private String cco;
    @Column(name = "body")
    private String body;
    @Basic(optional = false)
    @Column(name = "fecha_envio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEnvio;
    @ManyToMany(mappedBy = "seguimientoCollection")
    private Collection<ReferenciaDocumento> referenciaDocumentoCollection;
    @JoinColumn(name = "id_programa", referencedColumnName = "id")
    @ManyToOne
    private Programa idPrograma;

    public Seguimiento() {
    }

    public Seguimiento(Integer id) {
        this.id = id;
    }

    public Seguimiento(Integer id, short idInstancia, String mailFrom, String mailTo, String subject, Date fechaEnvio) {
        this.id = id;
        this.idInstancia = idInstancia;
        this.mailFrom = mailFrom;
        this.mailTo = mailTo;
        this.subject = subject;
        this.fechaEnvio = fechaEnvio;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public short getIdInstancia() {
        return idInstancia;
    }

    public void setIdInstancia(short idInstancia) {
        this.idInstancia = idInstancia;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getCco() {
        return cco;
    }

    public void setCco(String cco) {
        this.cco = cco;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    @XmlTransient
    public Collection<ReferenciaDocumento> getReferenciaDocumentoCollection() {
        return referenciaDocumentoCollection;
    }

    public void setReferenciaDocumentoCollection(Collection<ReferenciaDocumento> referenciaDocumentoCollection) {
        this.referenciaDocumentoCollection = referenciaDocumentoCollection;
    }

    public Programa getIdPrograma() {
        return idPrograma;
    }

    public void setIdPrograma(Programa idPrograma) {
        this.idPrograma = idPrograma;
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
        if (!(object instanceof Seguimiento)) {
            return false;
        }
        Seguimiento other = (Seguimiento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.Seguimiento[ id=" + id + " ]";
    }
    
}