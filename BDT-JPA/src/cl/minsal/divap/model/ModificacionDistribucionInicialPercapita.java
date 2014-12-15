package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "modificacion_distribucion_inicial_percapita")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ModificacionDistribucionInicialPercapita.findAll", query = "SELECT m FROM ModificacionDistribucionInicialPercapita m"),
    @NamedQuery(name = "ModificacionDistribucionInicialPercapita.findByIdModificacionDistribucionInicialPercapita", query = "SELECT m FROM ModificacionDistribucionInicialPercapita m WHERE m.idModificacionDistribucionInicialPercapita = :idModificacionDistribucionInicialPercapita"),
    @NamedQuery(name = "ModificacionDistribucionInicialPercapita.findByFechaCreacion", query = "SELECT m FROM ModificacionDistribucionInicialPercapita m WHERE m.fechaCreacion = :fechaCreacion")})
public class ModificacionDistribucionInicialPercapita implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
   	@Column(name="id_modificacion_distribucion_inicial_percapita", unique=true, nullable=false)
   	@GeneratedValue
    private Integer idModificacionDistribucionInicialPercapita;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @JoinColumn(name = "usuario", referencedColumnName = "username")
    @ManyToOne
    private Usuario usuario;
    @JoinColumn(name = "ano", referencedColumnName = "ano")
    @ManyToOne(optional = false)
    private AnoEnCurso ano;
    @OneToMany(mappedBy = "modificacionPercapita")
    private Set<AntecendentesComunaCalculado> antecendentesComunasCalculado;

    public ModificacionDistribucionInicialPercapita() {
    }

    public ModificacionDistribucionInicialPercapita(Integer idModificacionDistribucionInicialPercapita) {
        this.idModificacionDistribucionInicialPercapita = idModificacionDistribucionInicialPercapita;
    }

    public Integer getIdModificacionDistribucionInicialPercapita() {
        return idModificacionDistribucionInicialPercapita;
    }

    public void setIdModificacionDistribucionInicialPercapita(Integer idModificacionDistribucionInicialPercapita) {
        this.idModificacionDistribucionInicialPercapita = idModificacionDistribucionInicialPercapita;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public AnoEnCurso getAno() {
        return ano;
    }

    public void setAno(AnoEnCurso ano) {
        this.ano = ano;
    }
    
    @XmlTransient
    public Set<AntecendentesComunaCalculado> getAntecendentesComunasCalculado() {
		return antecendentesComunasCalculado;
	}

	public void setAntecendentesComunasCalculado(
			Set<AntecendentesComunaCalculado> antecendentesComunasCalculado) {
		this.antecendentesComunasCalculado = antecendentesComunasCalculado;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idModificacionDistribucionInicialPercapita != null ? idModificacionDistribucionInicialPercapita.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ModificacionDistribucionInicialPercapita)) {
            return false;
        }
        ModificacionDistribucionInicialPercapita other = (ModificacionDistribucionInicialPercapita) object;
        if ((this.idModificacionDistribucionInicialPercapita == null && other.idModificacionDistribucionInicialPercapita != null) || (this.idModificacionDistribucionInicialPercapita != null && !this.idModificacionDistribucionInicialPercapita.equals(other.idModificacionDistribucionInicialPercapita))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ModificacionDistribucionInicialPercapita[ idModificacionDistribucionInicialPercapita=" + idModificacionDistribucionInicialPercapita + " ]";
    }
    
}
