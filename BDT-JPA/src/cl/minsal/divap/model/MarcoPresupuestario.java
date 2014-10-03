package cl.minsal.divap.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the marco_presupuestario database table.
 * 
 */
@Entity
@Table(name = "marco_presupuestario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MarcoPresupuestario.findAll", query = "SELECT m FROM MarcoPresupuestario m"),
    @NamedQuery(name = "MarcoPresupuestario.findByMarcoInicial", query = "SELECT m FROM MarcoPresupuestario m WHERE m.marcoInicial = :marcoInicial"),
    @NamedQuery(name = "MarcoPresupuestario.findByMarcoModificado", query = "SELECT m FROM MarcoPresupuestario m WHERE m.marcoModificado = :marcoModificado"),
    @NamedQuery(name = "MarcoPresupuestario.deleteUsingIdProgramaAno", query = "DELETE FROM MarcoPresupuestario m WHERE m.idProgramaAno.idProgramaAno = :idProgramaAno"),
    @NamedQuery(name = "MarcoPresupuestario.findByProgramaAnoServicio", query = "SELECT m FROM MarcoPresupuestario m WHERE m.idProgramaAno.idProgramaAno = :idProgramaAno and m.servicioSalud.id = :idServicioSalud"),
    @NamedQuery(name = "MarcoPresupuestario.findByIdMarcoPresupuestario", query = "SELECT m FROM MarcoPresupuestario m WHERE m.idMarcoPresupuestario = :idMarcoPresupuestario")})
public class MarcoPresupuestario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "marco_inicial")
    private int marcoInicial;
    @Basic(optional = false)
    @Column(name = "marco_modificado")
    private int marcoModificado;
    @Id
	@Column(name="id_marco_presupuestario", unique=true, nullable=false)
	@GeneratedValue
    private Integer idMarcoPresupuestario;
    @JoinColumn(name = "id_servicio_salud", referencedColumnName = "id")
    @ManyToOne
    private ServicioSalud servicioSalud;
    @JoinColumn(name = "id_programa_ano", referencedColumnName = "id_programa_ano")
    @ManyToOne
    private ProgramaAno idProgramaAno;

    public MarcoPresupuestario() {
    }

    public MarcoPresupuestario(Integer idMarcoPresupuestario) {
        this.idMarcoPresupuestario = idMarcoPresupuestario;
    }

    public MarcoPresupuestario(Integer idMarcoPresupuestario, int marcoInicial, int marcoModificado) {
        this.idMarcoPresupuestario = idMarcoPresupuestario;
        this.marcoInicial = marcoInicial;
        this.marcoModificado = marcoModificado;
    }

    public int getMarcoInicial() {
        return marcoInicial;
    }

    public void setMarcoInicial(int marcoInicial) {
        this.marcoInicial = marcoInicial;
    }

    public int getMarcoModificado() {
        return marcoModificado;
    }

    public void setMarcoModificado(int marcoModificado) {
        this.marcoModificado = marcoModificado;
    }

    public Integer getIdMarcoPresupuestario() {
        return idMarcoPresupuestario;
    }

    public void setIdMarcoPresupuestario(Integer idMarcoPresupuestario) {
        this.idMarcoPresupuestario = idMarcoPresupuestario;
    }

    public ServicioSalud getServicioSalud() {
		return servicioSalud;
	}

	public void setServicioSalud(ServicioSalud servicioSalud) {
		this.servicioSalud = servicioSalud;
	}

	public ProgramaAno getIdProgramaAno() {
        return idProgramaAno;
    }

    public void setIdProgramaAno(ProgramaAno idProgramaAno) {
        this.idProgramaAno = idProgramaAno;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMarcoPresupuestario != null ? idMarcoPresupuestario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MarcoPresupuestario)) {
            return false;
        }
        MarcoPresupuestario other = (MarcoPresupuestario) object;
        if ((this.idMarcoPresupuestario == null && other.idMarcoPresupuestario != null) || (this.idMarcoPresupuestario != null && !this.idMarcoPresupuestario.equals(other.idMarcoPresupuestario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.MarcoPresupuestario[ idMarcoPresupuestario=" + idMarcoPresupuestario + " ]";
    }
    
}