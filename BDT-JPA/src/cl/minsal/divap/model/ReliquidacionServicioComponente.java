/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
@Table(name = "reliquidacion_servicio_componente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReliquidacionServicioComponente.findAll", query = "SELECT r FROM ReliquidacionServicioComponente r"),
    @NamedQuery(name = "ReliquidacionServicioComponente.findByIdReliquidacionServicioComponente", query = "SELECT r FROM ReliquidacionServicioComponente r WHERE r.idReliquidacionServicioComponente = :idReliquidacionServicioComponente"),
    @NamedQuery(name = "ReliquidacionServicioComponente.findByMontoRebaja", query = "SELECT r FROM ReliquidacionServicioComponente r WHERE r.montoRebaja = :montoRebaja"),
    @NamedQuery(name = "ReliquidacionServicioComponente.findByPorcentajeCumplimiento", query = "SELECT r FROM ReliquidacionServicioComponente r WHERE r.porcentajeCumplimiento = :porcentajeCumplimiento"),
    @NamedQuery(name = "ReliquidacionServicioComponente.deleteByIdProgramaAno", query = "DELETE FROM ReliquidacionServicioComponente r WHERE r.reliquidacionServicio.programa.idProgramaAno = :idProgramaAno"),
    @NamedQuery(name = "ReliquidacionServicioComponente.deleteByIdReliquidacionServicio", query = "DELETE FROM ReliquidacionServicioComponente r WHERE r.reliquidacionServicio.reliquidacionServicioId = :idReliquidacionServicio"),
    @NamedQuery(name = "ReliquidacionServicioComponente.findByIdProgramaAnoIdEstablecimientoIdComponenteIdSubtituloIdReliquidacion", query = "SELECT r FROM ReliquidacionServicioComponente r WHERE r.reliquidacionServicio.programa.idProgramaAno = :idProgramaAno and r.reliquidacionServicio.establecimiento.id = :idEstablecimiento and r.componente.id = :idComponente and r.subtitulo.idTipoSubtitulo = :idSubtitulo and r.reliquidacionServicio.reliquidacion.idReliquidacion = :idReliquidacion")})
public class ReliquidacionServicioComponente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
	@Column(name="id_reliquidacion_servicio_componente", unique=true, nullable=false)
	@GeneratedValue
    private Integer idReliquidacionServicioComponente;
    @Column(name = "monto_rebaja")
    private Integer montoRebaja;
    @Basic(optional = false)
    @Column(name = "porcentaje_cumplimiento")
    private Double porcentajeCumplimiento;
    @JoinColumn(name = "reliquidacion_servicio", referencedColumnName = "reliquidacion_servicio_id")
    @ManyToOne(optional = false)
    private ReliquidacionServicio reliquidacionServicio;
    @JoinColumn(name = "subtitulo", referencedColumnName = "id_tipo_subtitulo")
    @ManyToOne(optional = false)
    private TipoSubtitulo subtitulo;
    @JoinColumn(name = "cumplimiento", referencedColumnName = "id_cumplimiento_programa")
    @ManyToOne(optional = false)
    private CumplimientoPrograma cumplimiento;
    @JoinColumn(name = "componente", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Componente componente;

    public ReliquidacionServicioComponente() {
    }

    public ReliquidacionServicioComponente(Integer idReliquidacionServicioComponente) {
        this.idReliquidacionServicioComponente = idReliquidacionServicioComponente;
    }

    public ReliquidacionServicioComponente(Integer idReliquidacionServicioComponente, Double porcentajeCumplimiento) {
        this.idReliquidacionServicioComponente = idReliquidacionServicioComponente;
        this.porcentajeCumplimiento = porcentajeCumplimiento;
    }

    public Integer getIdReliquidacionServicioComponente() {
        return idReliquidacionServicioComponente;
    }

    public void setIdReliquidacionServicioComponente(Integer idReliquidacionServicioComponente) {
        this.idReliquidacionServicioComponente = idReliquidacionServicioComponente;
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

	public ReliquidacionServicio getReliquidacionServicio() {
        return reliquidacionServicio;
    }

    public void setReliquidacionServicio(ReliquidacionServicio reliquidacionServicio) {
        this.reliquidacionServicio = reliquidacionServicio;
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
        hash += (idReliquidacionServicioComponente != null ? idReliquidacionServicioComponente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ReliquidacionServicioComponente)) {
            return false;
        }
        ReliquidacionServicioComponente other = (ReliquidacionServicioComponente) object;
        if ((this.idReliquidacionServicioComponente == null && other.idReliquidacionServicioComponente != null) || (this.idReliquidacionServicioComponente != null && !this.idReliquidacionServicioComponente.equals(other.idReliquidacionServicioComponente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ReliquidacionServicioComponente[ idReliquidacionServicioComponente=" + idReliquidacionServicioComponente + " ]";
    }
    
}
