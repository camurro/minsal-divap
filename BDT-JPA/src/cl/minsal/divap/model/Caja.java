package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Aldo
 */
@Entity
@Table(name = "caja")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Caja.findAll", query = "SELECT c FROM Caja c"),
    @NamedQuery(name = "Caja.findByProgramaAnoComponenteSubtitulo", query = "SELECT c FROM Caja c WHERE c.idSubtitulo.idTipoSubtitulo = :idTipoSubtitulo and c.idComponente.id = :idComponente and c.marcoPresupuestario.idProgramaAno.idProgramaAno = :idProgramaAno"),
    @NamedQuery(name = "Caja.deleteUsingIdProgramaAno", query = "DELETE FROM Caja c WHERE c.marcoPresupuestario.idProgramaAno.idProgramaAno = :idProgramaAno"),
    @NamedQuery(name = "Caja.findByIdProgramaAnoIdServicio", query = "SELECT c FROM Caja c WHERE c.marcoPresupuestario.idProgramaAno.idProgramaAno = :idProgramaAno and c.marcoPresupuestario.servicioSalud.id = :idServicio"),
    @NamedQuery(name = "Caja.findById", query = "SELECT c FROM Caja c WHERE c.id = :id")})
public class Caja implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id",unique=true, nullable=false)
    @GeneratedValue
    private Integer id;
    @Basic(optional = false)
    @Column(name = "monto")
    private int monto;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "caja")
    @OrderBy("mes.idMes ASC")
    private Set<CajaMonto> cajaMontos;
    @JoinColumn(name = "id_subtitulo", referencedColumnName = "id_tipo_subtitulo")
    @ManyToOne
    private TipoSubtitulo idSubtitulo;
    @JoinColumn(name = "marco_presupuestario", referencedColumnName = "id_marco_presupuestario")
    @ManyToOne
    private MarcoPresupuestario marcoPresupuestario;
    @JoinColumn(name = "id_componente", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Componente idComponente;
   
    public Caja() {
    }

    public Caja(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

    @XmlTransient
    public Set<CajaMonto> getCajaMontos() {
		return cajaMontos;
	}

	public void setCajaMontos(Set<CajaMonto> cajaMontos) {
		this.cajaMontos = cajaMontos;
	}

	public TipoSubtitulo getIdSubtitulo() {
        return idSubtitulo;
    }

    public void setIdSubtitulo(TipoSubtitulo idSubtitulo) {
        this.idSubtitulo = idSubtitulo;
    }

    public MarcoPresupuestario getMarcoPresupuestario() {
        return marcoPresupuestario;
    }

    public void setMarcoPresupuestario(MarcoPresupuestario marcoPresupuestario) {
        this.marcoPresupuestario = marcoPresupuestario;
    }

    public Componente getIdComponente() {
        return idComponente;
    }

    public void setIdComponente(Componente idComponente) {
        this.idComponente = idComponente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Caja)) {
            return false;
        }
        Caja other = (Caja) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.Caja[ id=" + id + " ]";
    }
    
}