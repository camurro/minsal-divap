package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.List;

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
    @NamedQuery(name = "Caja.findByIdProgramaAnoIdServicio", query = "SELECT c FROM Caja c WHERE c.programa.idProgramaAno = :idProgramaAno and c.servicio.id = :idServicio"),
    @NamedQuery(name = "Caja.findByProgramaAnoServicioSubtitulo", query = "SELECT c FROM Caja c WHERE c.programa.idProgramaAno = :idProgramaAno and c.servicio.id = :idServicio and c.idSubtitulo.idTipoSubtitulo = :idTipoSubtitulo"),
    @NamedQuery(name = "Caja.findBySubtituloAno", query = "SELECT c FROM Caja c WHERE c.idSubtitulo.idTipoSubtitulo = :idSubtitulo and c.programa is not null and c.programa.idProgramaAno = :idProgramaAno "),
    @NamedQuery(name = "Caja.findById", query = "SELECT c FROM Caja c WHERE c.id = :id"),
    @NamedQuery(name = "Caja.findByProgramaAnoSubtitulo", query = "SELECT c FROM Caja c WHERE c.idSubtitulo.idTipoSubtitulo = :idTipoSubtitulo and c.programa.idProgramaAno = :idProgramaAno"),
    @NamedQuery(name = "Caja.findByServicioEstablecimientoProgramaAnoComponenteSubtitulo", query = "SELECT c FROM Caja c WHERE c.servicio.id = :idServicio and c.establecimiento.id = :idEstablecimiento and c.programa.idProgramaAno = :idProgramaAno and c.idComponente.id = :idComponente and c.idSubtitulo.idTipoSubtitulo = :idTipoSubtitulo"),
    @NamedQuery(name = "Caja.findByServicioComunaProgramaAnoComponenteSubtitulo", query = "SELECT c FROM Caja c WHERE c.servicio.id = :idServicio and c.comuna.id = :idComuna and c.programa.idProgramaAno = :idProgramaAno and c.idComponente.id = :idComponente and c.idSubtitulo.idTipoSubtitulo = :idTipoSubtitulo"),
    @NamedQuery(name = "Caja.deleteUsingIdProgramaAno", query = "DELETE FROM Caja c WHERE c.programa.idProgramaAno = :idProgramaAno"),
    @NamedQuery(name = "Caja.countByIdProgramaAno", query = "SELECT COUNT(c) FROM Caja c WHERE c.programa.idProgramaAno = :idProgramaAno"),
    @NamedQuery(name = "Caja.findByIdProgramaAno", query = "SELECT c FROM Caja c WHERE c.programa.idProgramaAno = :idProgramaAno"),
    @NamedQuery(name = "Caja.deleteUsingIds", query = "DELETE FROM Caja c WHERE c.id in (:idCajas)"),
    @NamedQuery(name = "Caja.findByProgramaAnoComponentesSubtitulo", query = "SELECT c FROM Caja c WHERE c.programa.idProgramaAno = :idProgramaAno and c.idComponente.id IN (:idComponentes) and c.idSubtitulo.idTipoSubtitulo = :idSubtitulo")})


public class Caja implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id",unique=true, nullable=false)
    @GeneratedValue
    private Integer id;
    @Basic(optional = false)
    @Column(name = "caja_inicial")
    private boolean cajaInicial;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "caja")
    @OrderBy("mes.idMes ASC")
    private List<CajaMonto> cajaMontos;
    @JoinColumn(name = "id_subtitulo", referencedColumnName = "id_tipo_subtitulo")
    @ManyToOne
    private TipoSubtitulo idSubtitulo;
    @JoinColumn(name = "servicio", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ServicioSalud servicio;
    @JoinColumn(name = "programa", referencedColumnName = "id_programa_ano")
    @ManyToOne(optional = false)
    private ProgramaAno programa;
    @JoinColumn(name = "establecimiento", referencedColumnName = "id")
    @ManyToOne
    private Establecimiento establecimiento;
    @JoinColumn(name = "comuna", referencedColumnName = "id")
    @ManyToOne
    private Comuna comuna;
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
    
    @XmlTransient
    public List<CajaMonto> getCajaMontos() {
		return cajaMontos;
	}

	public void setCajaMontos(List<CajaMonto> cajaMontos) {
		this.cajaMontos = cajaMontos;
	}

	public TipoSubtitulo getIdSubtitulo() {
        return idSubtitulo;
    }

    public void setIdSubtitulo(TipoSubtitulo idSubtitulo) {
        this.idSubtitulo = idSubtitulo;
    }

    public boolean isCajaInicial() {
		return cajaInicial;
	}

	public void setCajaInicial(boolean cajaInicial) {
		this.cajaInicial = cajaInicial;
	}

	public ServicioSalud getServicio() {
		return servicio;
	}

	public void setServicio(ServicioSalud servicio) {
		this.servicio = servicio;
	}

	public ProgramaAno getPrograma() {
		return programa;
	}

	public void setPrograma(ProgramaAno programa) {
		this.programa = programa;
	}

	public Componente getIdComponente() {
        return idComponente;
    }

    public void setIdComponente(Componente idComponente) {
        this.idComponente = idComponente;
    }
    
    public Establecimiento getEstablecimiento() {
		return establecimiento;
	}

	public void setEstablecimiento(Establecimiento establecimiento) {
		this.establecimiento = establecimiento;
	}

	public Comuna getComuna() {
		return comuna;
	}

	public void setComuna(Comuna comuna) {
		this.comuna = comuna;
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
