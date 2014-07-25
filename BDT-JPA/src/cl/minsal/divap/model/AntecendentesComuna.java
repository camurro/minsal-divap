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
    @NamedQuery(name = "AntecendentesComuna.findByTramoPobreza", query = "SELECT a FROM AntecendentesComuna a WHERE a.tramoPobreza = :tramoPobreza"),
    @NamedQuery(name = "AntecendentesComuna.findByIdAntecedentesComuna", query = "SELECT a FROM AntecendentesComuna a WHERE a.idAntecedentesComuna = :idAntecedentesComuna")})
public class AntecendentesComuna implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "clasificacion")
    private String clasificacion;
    @Column(name = "asignacion_zona")
    private Short asignacionZona;
    @Column(name = "tramo_pobreza")
    private Short tramoPobreza;
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

    public AntecendentesComuna(Integer idAntecedentesComuna, String clasificacion) {
        this.idAntecedentesComuna = idAntecedentesComuna;
        this.clasificacion = clasificacion;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public Short getAsignacionZona() {
        return asignacionZona;
    }

    public void setAsignacionZona(Short asignacionZona) {
        this.asignacionZona = asignacionZona;
    }

    public Short getTramoPobreza() {
        return tramoPobreza;
    }

    public void setTramoPobreza(Short tramoPobreza) {
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
