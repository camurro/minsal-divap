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
@Table(name = "cumplimiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cumplimiento.findAll", query = "SELECT c FROM Cumplimiento c"),
    @NamedQuery(name = "Cumplimiento.findByIdCumplimiento", query = "SELECT c FROM Cumplimiento c WHERE c.idCumplimiento = :idCumplimiento"),
    @NamedQuery(name = "Cumplimiento.findByIdTramo", query = "SELECT c FROM Cumplimiento c WHERE c.tramo.idTramo = :idTramo"),
    @NamedQuery(name = "Cumplimiento.findByIdTipoCumplimiento", query = "SELECT c FROM Cumplimiento c WHERE c.tipoCumplimiento.idTipoCumplimiento = :idTipoCumplimiento"),
    @NamedQuery(name = "Cumplimiento.findByRebaja", query = "SELECT c FROM Cumplimiento c WHERE c.rebaja = :rebaja"),
    @NamedQuery(name = "Cumplimiento.findByPorcentajeDesde", query = "SELECT c FROM Cumplimiento c WHERE c.porcentajeDesde = :porcentajeDesde"),
    @NamedQuery(name = "Cumplimiento.findByPorcentajeHasta", query = "SELECT c FROM Cumplimiento c WHERE c.porcentajeHasta = :porcentajeHasta")})
public class Cumplimiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
	@Column(name="id_cumplimiento", unique=true, nullable=false)
	@GeneratedValue
    private Integer idCumplimiento;
    @Basic(optional = false)
    @Column(name = "rebaja")
    private Double rebaja;
    @Basic(optional = false)
    @Column(name = "porcentaje_desde")
    private Double porcentajeDesde;
    @Column(name = "porcentaje_hasta")
    private Double porcentajeHasta;
    @JoinColumn(name = "tramo", referencedColumnName = "id_tramo")
    @ManyToOne(optional = false)
    private Tramo tramo;
    @JoinColumn(name = "tipo_cumplimiento", referencedColumnName = "id_tipo_cumplimiento")
    @ManyToOne(optional = false)
    private TipoCumplimiento tipoCumplimiento;

    public Cumplimiento() {
    }

    public Cumplimiento(Integer idCumplimiento) {
        this.idCumplimiento = idCumplimiento;
    }

    public Cumplimiento(Integer idCumplimiento, Double rebaja, Double porcentajeDesde) {
        this.idCumplimiento = idCumplimiento;
        this.rebaja = rebaja;
        this.porcentajeDesde = porcentajeDesde;
    }

    public Integer getIdCumplimiento() {
        return idCumplimiento;
    }

    public void setIdCumplimiento(Integer idCumplimiento) {
        this.idCumplimiento = idCumplimiento;
    }

    public Double getRebaja() {
        return rebaja;
    }

    public void setRebaja(Double rebaja) {
        this.rebaja = rebaja;
    }

    public Double getPorcentajeDesde() {
        return porcentajeDesde;
    }

    public void setPorcentajeDesde(Double porcentajeDesde) {
        this.porcentajeDesde = porcentajeDesde;
    }

    public Double getPorcentajeHasta() {
        return porcentajeHasta;
    }

    public void setPorcentajeHasta(Double porcentajeHasta) {
        this.porcentajeHasta = porcentajeHasta;
    }

    public Tramo getTramo() {
        return tramo;
    }

    public void setTramo(Tramo tramo) {
        this.tramo = tramo;
    }

    public TipoCumplimiento getTipoCumplimiento() {
        return tipoCumplimiento;
    }

    public void setTipoCumplimiento(TipoCumplimiento tipoCumplimiento) {
        this.tipoCumplimiento = tipoCumplimiento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCumplimiento != null ? idCumplimiento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Cumplimiento)) {
            return false;
        }
        Cumplimiento other = (Cumplimiento) object;
        if ((this.idCumplimiento == null && other.idCumplimiento != null) || (this.idCumplimiento != null && !this.idCumplimiento.equals(other.idCumplimiento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.Cumplimiento[ idCumplimiento=" + idCumplimiento + " ]";
    }
    
}
