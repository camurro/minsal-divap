package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@Entity
@Table(name = "antecendentes_comuna")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AntecendentesComuna.findAll", query = "SELECT a FROM AntecendentesComuna a"),
    @NamedQuery(name = "AntecendentesComuna.findByClasificacion", query = "SELECT a FROM AntecendentesComuna a WHERE a.clasificacion = :clasificacion"),
    @NamedQuery(name = "AntecendentesComuna.findByAsignacionZona", query = "SELECT a FROM AntecendentesComuna a WHERE a.asignacionZona = :asignacionZona"),
    @NamedQuery(name = "AntecendentesComuna.findByClasificacionAno", query = "SELECT a FROM AntecendentesComuna a WHERE a.anoAnoEnCurso.ano = :ano and a.clasificacion.idTipoComuna IN (:clasificacion) order by a.idComuna.servicioSalud.id asc, a.idComuna.nombre asc"),
    @NamedQuery(name = "AntecendentesComuna.findByTramoPobreza", query = "SELECT a FROM AntecendentesComuna a WHERE a.tramoPobreza = :tramoPobreza"),
    @NamedQuery(name = "AntecendentesComuna.findByAnoEnCurso", query = "SELECT a FROM AntecendentesComuna a WHERE a.anoAnoEnCurso.ano = :anoEnCurso and a.clasificacion is not null"),
    @NamedQuery(name = "AntecendentesComuna.findByDistribucionInicialPercapita", query = "SELECT a FROM AntecendentesComuna a JOIN a.antecendentesComunaCalculadoCollection c WHERE c.distribucionInicialPercapita.idDistribucionInicialPercapita = :idDistribucionInicialPercapita and c.fechaVigencia is null"),
    @NamedQuery(name = "AntecendentesComuna.findByIdAntecedentesComunaByComuna", query = "SELECT a FROM AntecendentesComuna a WHERE a.idComuna.id = :idComuna"),
    @NamedQuery(name = "AntecendentesComuna.findAntecendentesComunaByComunaServicioAno", query = "SELECT a FROM AntecendentesComuna a WHERE LOWER(a.idComuna.nombre) = :nombreComuna and LOWER(a.idComuna.servicioSalud.nombre) = :nombreServicio and a.anoAnoEnCurso.ano  = :anoEnCurso"),
    @NamedQuery(name = "AntecendentesComuna.groupPercapitaServicio", query = "SELECT a.idComuna.servicioSalud.id, SUM(ac.percapitaAno) FROM AntecendentesComuna a JOIN a.antecendentesComunaCalculadoCollection ac where ac.distribucionInicialPercapita.idDistribucionInicialPercapita = :idDistribucionInicialPercapita GROUP BY a.idComuna.servicioSalud.id"),
    @NamedQuery(name = "AntecendentesComuna.groupDesempenoDificilServicio", query = "SELECT a.idComuna.servicioSalud.id, SUM(ac.desempenoDificil) FROM AntecendentesComuna a JOIN a.antecendentesComunaCalculadoCollection ac where ac.distribucionInicialPercapita.idDistribucionInicialPercapita = :idDistribucionInicialPercapita GROUP BY a.idComuna.servicioSalud.id"),
    @NamedQuery(name = "AntecendentesComuna.findByIdAntecedentesComuna", query = "SELECT a FROM AntecendentesComuna a WHERE a.idAntecedentesComuna = :idAntecedentesComuna"),
    @NamedQuery(name = "AntecendentesComuna.findAntecendentesComunaByIdComunaIdAno", query = "SELECT a FROM AntecendentesComuna a WHERE a.idComuna.id = :idComuna and a.anoAnoEnCurso.ano = :ano"),
    @NamedQuery(name = "AntecendentesComuna.findByDistribucionInicialPercapitaModificacionPercapita", query = "SELECT a FROM AntecendentesComuna a JOIN a.antecendentesComunaCalculadoCollection c WHERE c.distribucionInicialPercapita.idDistribucionInicialPercapita = :idDistribucionInicialPercapita and c.modificacionPercapita.idModificacionDistribucionInicialPercapita = :idModificacionPercapita and c.fechaVigencia is null"),
    @NamedQuery(name = "AntecendentesComuna.findByIdComunasAno", query = "SELECT a FROM AntecendentesComuna a WHERE a.anoAnoEnCurso.ano = :ano and a.idComuna.id IN (:idComunas) order by a.idComuna.servicioSalud.id asc, a.idComuna.nombre asc")})
public class AntecendentesComuna implements Serializable {
    private static final long serialVersionUID = 1L;
    @JoinColumn(name = "clasificacion", referencedColumnName = "id_tipo_comuna")
    @ManyToOne
    private TipoComuna clasificacion;
    @JoinColumn(name = "asignacion_zona", referencedColumnName = "id_factor_ref_asig_zona")
    @ManyToOne
    private FactorRefAsigZona asignacionZona;
    @JoinColumn(name = "tramo_pobreza", referencedColumnName = "id_factor_tramo_pobreza")
    @ManyToOne
    private FactorTramoPobreza tramoPobreza;
    @Id
	@Column(name="id_antecedentes_comuna", unique=true, nullable=false)
	@GeneratedValue
    private Integer idAntecedentesComuna;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "antecedentesComuna")
    private Set<AntecendentesComunaCalculado> antecendentesComunaCalculadoCollection;
    @JoinColumn(name = "id_comuna", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Comuna idComuna;
    @JoinColumn(name = "ano_ano_en_curso", referencedColumnName = "ano")
    @ManyToOne(optional = false)
    private AnoEnCurso anoAnoEnCurso;

    public AntecendentesComuna() {
    }

    public AntecendentesComuna(Integer idAntecedentesComuna) {
        this.idAntecedentesComuna = idAntecedentesComuna;
    }

    public AntecendentesComuna(Integer idAntecedentesComuna, TipoComuna clasificacion) {
        this.idAntecedentesComuna = idAntecedentesComuna;
        this.clasificacion = clasificacion;
    }

    public TipoComuna getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(TipoComuna clasificacion) {
        this.clasificacion = clasificacion;
    }

    public FactorRefAsigZona getAsignacionZona() {
		return asignacionZona;
	}

	public void setAsignacionZona(FactorRefAsigZona asignacionZona) {
		this.asignacionZona = asignacionZona;
	}

	public FactorTramoPobreza getTramoPobreza() {
		return tramoPobreza;
	}

	public void setTramoPobreza(FactorTramoPobreza tramoPobreza) {
		this.tramoPobreza = tramoPobreza;
	}

	public Integer getIdAntecedentesComuna() {
        return idAntecedentesComuna;
    }

    public void setIdAntecedentesComuna(Integer idAntecedentesComuna) {
        this.idAntecedentesComuna = idAntecedentesComuna;
    }

    @XmlTransient
    public Set<AntecendentesComunaCalculado> getAntecendentesComunaCalculadoCollection() {
        return antecendentesComunaCalculadoCollection;
    }

    public void setAntecendentesComunaCalculadoCollection(Set<AntecendentesComunaCalculado> antecendentesComunaCalculadoCollection) {
        this.antecendentesComunaCalculadoCollection = antecendentesComunaCalculadoCollection;
    }

    public Comuna getIdComuna() {
        return idComuna;
    }

    public void setIdComuna(Comuna idComuna) {
        this.idComuna = idComuna;
    }

    public AnoEnCurso getAnoAnoEnCurso() {
        return anoAnoEnCurso;
    }

    public void setAnoAnoEnCurso(AnoEnCurso anoAnoEnCurso) {
        this.anoAnoEnCurso = anoAnoEnCurso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAntecedentesComuna != null ? idAntecedentesComuna.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AntecendentesComuna)) {
            return false;
        }
        AntecendentesComuna other = (AntecendentesComuna) object;
        if ((this.idAntecedentesComuna == null && other.idAntecedentesComuna != null) || (this.idAntecedentesComuna != null && !this.idAntecedentesComuna.equals(other.idAntecedentesComuna))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.AntecendentesComuna[ idAntecedentesComuna=" + idAntecedentesComuna + " ]";
    }
    
}
