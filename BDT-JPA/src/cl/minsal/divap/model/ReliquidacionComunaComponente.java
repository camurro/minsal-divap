package cl.minsal.divap.model;

import java.io.Serializable;

import javax.persistence.Basic;
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
@Table(name = "reliquidacion_comuna_componente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReliquidacionComunaComponente.findAll", query = "SELECT r FROM ReliquidacionComunaComponente r"),
    @NamedQuery(name = "ReliquidacionComunaComponente.findByIdReliquidacionComunaComponente", query = "SELECT r FROM ReliquidacionComunaComponente r WHERE r.idReliquidacionComunaComponente = :idReliquidacionComunaComponente"),
    @NamedQuery(name = "ReliquidacionComunaComponente.findByMontoRebaja", query = "SELECT r FROM ReliquidacionComunaComponente r WHERE r.montoRebaja = :montoRebaja"),
    @NamedQuery(name = "ReliquidacionComunaComponente.findByPorcentajeCumplimiento", query = "SELECT r FROM ReliquidacionComunaComponente r WHERE r.porcentajeCumplimiento = :porcentajeCumplimiento"),
    @NamedQuery(name = "ReliquidacionComunaComponente.deleteByIdProgramaAno", query = "DELETE FROM ReliquidacionComunaComponente r WHERE r.reliquidacionComuna.programa.idProgramaAno = :idProgramaAno"),
    @NamedQuery(name = "ReliquidacionComunaComponente.deleteByIdReliquidacionComuna", query = "DELETE FROM ReliquidacionComunaComponente r WHERE r.reliquidacionComuna.reliquidacionComunaId = :idReliquidacionComuna")})
public class ReliquidacionComunaComponente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
	@Column(name="id_reliquidacion_comuna_componente", unique=true, nullable=false)
	@GeneratedValue
    private Integer idReliquidacionComunaComponente;
    @Column(name = "monto_rebaja")
    private Integer montoRebaja;
    @Basic(optional = false)
    @Column(name = "porcentaje_cumplimiento")
    private Double porcentajeCumplimiento;
    @JoinColumn(name = "subtitulo", referencedColumnName = "id_tipo_subtitulo")
    @ManyToOne(optional = false)
    private TipoSubtitulo subtitulo;
    @JoinColumn(name = "reliquidacion_comuna", referencedColumnName = "reliquidacion_comuna_id")
    @ManyToOne(optional = false)
    private ReliquidacionComuna reliquidacionComuna;
    @JoinColumn(name = "cumplimiento", referencedColumnName = "id_cumplimiento_programa")
    @ManyToOne(optional = false)
    private CumplimientoPrograma cumplimiento;
    @JoinColumn(name = "componente", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Componente componente;

    public ReliquidacionComunaComponente() {
    }

    public ReliquidacionComunaComponente(Integer idReliquidacionComunaComponente) {
        this.idReliquidacionComunaComponente = idReliquidacionComunaComponente;
    }

    public ReliquidacionComunaComponente(Integer idReliquidacionComunaComponente, Double porcentajeCumplimiento) {
        this.idReliquidacionComunaComponente = idReliquidacionComunaComponente;
        this.porcentajeCumplimiento = porcentajeCumplimiento;
    }

    public Integer getIdReliquidacionComunaComponente() {
        return idReliquidacionComunaComponente;
    }

    public void setIdReliquidacionComunaComponente(Integer idReliquidacionComunaComponente) {
        this.idReliquidacionComunaComponente = idReliquidacionComunaComponente;
    }

    public Integer getMontoRebaja() {
        return montoRebaja;
    }

    public void setMontoRebaja(Integer montoRebaja) {
        this.montoRebaja = montoRebaja;
    }

    public Double getPorcentajeCumplimiento() {
        return porcentajeCumplimiento;
    }

    public void setPorcentajeCumplimiento(Double porcentajeCumplimiento) {
        this.porcentajeCumplimiento = porcentajeCumplimiento;
    }

    public TipoSubtitulo getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(TipoSubtitulo subtitulo) {
        this.subtitulo = subtitulo;
    }
    
    public ReliquidacionComuna getReliquidacionComuna() {
        return reliquidacionComuna;
    }

    public void setReliquidacionComuna(ReliquidacionComuna reliquidacionComuna) {
        this.reliquidacionComuna = reliquidacionComuna;
    }

    public CumplimientoPrograma getCumplimiento() {
        return cumplimiento;
    }

    public void setCumplimiento(CumplimientoPrograma cumplimiento) {
        this.cumplimiento = cumplimiento;
    }

    public Componente getComponente() {
        return componente;
    }

    public void setComponente(Componente componente) {
        this.componente = componente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idReliquidacionComunaComponente != null ? idReliquidacionComunaComponente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ReliquidacionComunaComponente)) {
            return false;
        }
        ReliquidacionComunaComponente other = (ReliquidacionComunaComponente) object;
        if ((this.idReliquidacionComunaComponente == null && other.idReliquidacionComunaComponente != null) || (this.idReliquidacionComunaComponente != null && !this.idReliquidacionComunaComponente.equals(other.idReliquidacionComunaComponente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ReliquidacionComunaComponente[ idReliquidacionComunaComponente=" + idReliquidacionComunaComponente + " ]";
    }
    
}
