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
@Table(name = "modificacion_distribucion_inicial_percapita_seguimiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ModificacionDistribucionInicialPercapitaSeguimiento.findAll", query = "SELECT m FROM ModificacionDistribucionInicialPercapitaSeguimiento m"),
    @NamedQuery(name = "ModificacionDistribucionInicialPercapitaSeguimiento.findByIdModificacionDistribucionInicialPercapitaSeguimiento", query = "SELECT m FROM ModificacionDistribucionInicialPercapitaSeguimiento m WHERE m.idModificacionDistribucionInicialPercapitaSeguimiento = :idModificacionDistribucionInicialPercapitaSeguimiento")})
public class ModificacionDistribucionInicialPercapitaSeguimiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
  	@Column(name="id_modificacion_distribucion_inicial_percapita_seguimiento", unique=true, nullable=false)
  	@GeneratedValue
    private Integer idModificacionDistribucionInicialPercapitaSeguimiento;
    @JoinColumn(name = "seguimiento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Seguimiento seguimiento;
    @JoinColumn(name = "modificacion_distribucion_inicial_percapita", referencedColumnName = "id_modificacion_distribucion_inicial_percapita")
    @ManyToOne(optional = false)
    private ModificacionDistribucionInicialPercapita modificacionDistribucionInicialPercapita;

    public ModificacionDistribucionInicialPercapitaSeguimiento() {
    }

    public ModificacionDistribucionInicialPercapitaSeguimiento(Integer idModificacionDistribucionInicialPercapitaSeguimiento) {
        this.idModificacionDistribucionInicialPercapitaSeguimiento = idModificacionDistribucionInicialPercapitaSeguimiento;
    }

    public Integer getIdModificacionDistribucionInicialPercapitaSeguimiento() {
        return idModificacionDistribucionInicialPercapitaSeguimiento;
    }

    public void setIdModificacionDistribucionInicialPercapitaSeguimiento(Integer idModificacionDistribucionInicialPercapitaSeguimiento) {
        this.idModificacionDistribucionInicialPercapitaSeguimiento = idModificacionDistribucionInicialPercapitaSeguimiento;
    }

    public Seguimiento getSeguimiento() {
        return seguimiento;
    }

    public void setSeguimiento(Seguimiento seguimiento) {
        this.seguimiento = seguimiento;
    }

    public ModificacionDistribucionInicialPercapita getModificacionDistribucionInicialPercapita() {
        return modificacionDistribucionInicialPercapita;
    }

    public void setModificacionDistribucionInicialPercapita(ModificacionDistribucionInicialPercapita modificacionDistribucionInicialPercapita) {
        this.modificacionDistribucionInicialPercapita = modificacionDistribucionInicialPercapita;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idModificacionDistribucionInicialPercapitaSeguimiento != null ? idModificacionDistribucionInicialPercapitaSeguimiento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ModificacionDistribucionInicialPercapitaSeguimiento)) {
            return false;
        }
        ModificacionDistribucionInicialPercapitaSeguimiento other = (ModificacionDistribucionInicialPercapitaSeguimiento) object;
        if ((this.idModificacionDistribucionInicialPercapitaSeguimiento == null && other.idModificacionDistribucionInicialPercapitaSeguimiento != null) || (this.idModificacionDistribucionInicialPercapitaSeguimiento != null && !this.idModificacionDistribucionInicialPercapitaSeguimiento.equals(other.idModificacionDistribucionInicialPercapitaSeguimiento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ModificacionDistribucionInicialPercapitaSeguimiento[ idModificacionDistribucionInicialPercapitaSeguimiento=" + idModificacionDistribucionInicialPercapitaSeguimiento + " ]";
    }
    
}
