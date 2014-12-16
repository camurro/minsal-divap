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
@Table(name = "remesas_seguimiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RemesasSeguimiento.findAll", query = "SELECT r FROM RemesasSeguimiento r"),
    @NamedQuery(name = "RemesasSeguimiento.findById", query = "SELECT r FROM RemesasSeguimiento r WHERE r.id = :id"),
    @NamedQuery(name = "RemesasSeguimiento.findByInstanciaRemesa", query = "SELECT r FROM RemesasSeguimiento r WHERE r.instanciaRemesa = :instanciaRemesa")})
public class RemesasSeguimiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
  @Column(name="id", unique=true, nullable=false)
  @GeneratedValue
    private Integer id;
    @JoinColumn(name = "instancia_remesa", referencedColumnName = "id_remesa")
    @ManyToOne
    private Remesas instanciaRemesa;
    @JoinColumn(name = "seguimiento", referencedColumnName = "id")
    @ManyToOne
    private Seguimiento seguimiento;

    public RemesasSeguimiento() {
    }

    public RemesasSeguimiento(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Remesas getInstanciaRemesa() {
		return instanciaRemesa;
	}

	public void setInstanciaRemesa(Remesas instanciaRemesa) {
		this.instanciaRemesa = instanciaRemesa;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RemesasSeguimiento)) {
            return false;
        }
        RemesasSeguimiento other = (RemesasSeguimiento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.RemesasSeguimiento[ id=" + id + " ]";
    }
    
}