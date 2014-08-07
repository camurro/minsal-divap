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
@Table(name = "destinatarios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Destinatarios.findAll", query = "SELECT d FROM Destinatarios d"),
    @NamedQuery(name = "Destinatarios.findByIdDestinatarios", query = "SELECT d FROM Destinatarios d WHERE d.idDestinatarios = :idDestinatarios")})
public class Destinatarios implements Serializable {
    private static final long serialVersionUID = 1L;
	@Id
   	@Column(name="id_destinatarios", unique=true, nullable=false)
   	@GeneratedValue
    private Integer idDestinatarios;
    @JoinColumn(name = "tipo_destinatario", referencedColumnName = "id_tipo_destinatario")
    @ManyToOne(optional = false)
    private TipoDestinatario tipoDestinatario;
    @JoinColumn(name = "destinatario", referencedColumnName = "id_email")
    @ManyToOne(optional = false)
    private Email destinatario;
    @JoinColumn(name = "seguimiento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Seguimiento seguimiento;

    public Destinatarios() {
    }

    public Destinatarios(Integer idDestinatarios) {
        this.idDestinatarios = idDestinatarios;
    }

    public Integer getIdDestinatarios() {
        return idDestinatarios;
    }

    public void setIdDestinatarios(Integer idDestinatarios) {
        this.idDestinatarios = idDestinatarios;
    }

    public TipoDestinatario getTipoDestinatario() {
        return tipoDestinatario;
    }

    public void setTipoDestinatario(TipoDestinatario tipoDestinatario) {
        this.tipoDestinatario = tipoDestinatario;
    }

    public Email getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Email destinatario) {
        this.destinatario = destinatario;
    }

    public Seguimiento getSeguimiento() {
		return seguimiento;
	}

	public void setSeguimiento(Seguimiento seguimiento) {
		this.seguimiento = seguimiento;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idDestinatarios != null ? idDestinatarios.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Destinatarios)) {
            return false;
        }
        Destinatarios other = (Destinatarios) object;
        if ((this.idDestinatarios == null && other.idDestinatarios != null) || (this.idDestinatarios != null && !this.idDestinatarios.equals(other.idDestinatarios))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.Destinatarios[ idDestinatarios=" + idDestinatarios + " ]";
    }
    
}
