
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
@Table(name = "comuna_cumplimiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ComunaCumplimiento.findAll", query = "SELECT c FROM ComunaCumplimiento c"),
    @NamedQuery(name = "ComunaCumplimiento.findByIdComunaCumplimiento", query = "SELECT c FROM ComunaCumplimiento c WHERE c.idComunaCumplimiento = :idComunaCumplimiento"),
    @NamedQuery(name = "ComunaCumplimiento.findByIdMes", query = "SELECT c FROM ComunaCumplimiento c WHERE c.idMes.idMes= :idMes"),
    @NamedQuery(name = "ComunaCumplimiento.findByValor", query = "SELECT c FROM ComunaCumplimiento c WHERE c.valor = :valor"),
    @NamedQuery(name = "ComunaCumplimiento.findByComuna", query = "SELECT c FROM ComunaCumplimiento c WHERE c.idComuna.id = :idComuna order by c.idComunaCumplimiento asc"),
    @NamedQuery(name = "ComunaCumplimiento.findByServicio", query = "SELECT DISTINCT(c.idComuna) FROM ComunaCumplimiento c WHERE c.idComuna is not null and c.idComuna.servicioSalud is not null and c.idComuna.servicioSalud.id = :idServicio"),
    @NamedQuery(name = "ComunaCumplimiento.findByRebaja", query = "SELECT c FROM ComunaCumplimiento c WHERE c.rebaja.idRebaja = :idRebaja"),
    @NamedQuery(name = "ComunaCumplimiento.deleteUsingIdCumplimiento", query = "DELETE FROM ComunaCumplimiento c WHERE c.idComunaCumplimiento IN (:listaIdCumplimientos)"),
    @NamedQuery(name = "ComunaCumplimiento.findByRebajaComunas", query = "SELECT c FROM ComunaCumplimiento c WHERE c.idComuna.id IN (:listaId) and c.rebaja.idRebaja = :idRebaja"),
    @NamedQuery(name = "ComunaCumplimiento.findByRebajaComuna", query = "SELECT c FROM ComunaCumplimiento c WHERE c.idComuna.id = :idComuna and c.rebaja.idRebaja = :idRebaja"),
    @NamedQuery(name = "ComunaCumplimiento.deleteByRebaja", query="DELETE FROM ComunaCumplimiento c WHERE c.rebaja.idRebaja = :rebaja")})
public class ComunaCumplimiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
	@Column(name="id_comuna_cumplimiento", unique=true, nullable=false)
	@GeneratedValue
    private Integer idComunaCumplimiento;
    @Column(name = "valor")
    private Double valor;
    @JoinColumn(name = "id_tipo_cumplimiento", referencedColumnName = "id_tipo_cumplimiento")
    @ManyToOne
    private TipoCumplimiento idTipoCumplimiento;
    @JoinColumn(name = "rebaja", referencedColumnName = "id_rebaja")
    @ManyToOne(optional = false)
    private Rebaja rebaja;
    @JoinColumn(name = "id_mes", referencedColumnName = "id_mes")
    @ManyToOne
    private Mes idMes;
    @JoinColumn(name = "rebaja_final", referencedColumnName = "comuna_cumplimiento_rebaja_id")
    @ManyToOne
    private ComunaCumplimientoRebaja rebajaFinal;
    @JoinColumn(name = "rebaja_calculada", referencedColumnName = "comuna_cumplimiento_rebaja_id")
    @ManyToOne
    private ComunaCumplimientoRebaja rebajaCalculada;
    @JoinColumn(name = "id_comuna", referencedColumnName = "id")
    @ManyToOne
    private Comuna idComuna;

    public ComunaCumplimiento() {
    }

    public ComunaCumplimiento(Integer idComunaCumplimiento) {
        this.idComunaCumplimiento = idComunaCumplimiento;
    }

    public Integer getIdComunaCumplimiento() {
        return idComunaCumplimiento;
    }

    public void setIdComunaCumplimiento(Integer idComunaCumplimiento) {
        this.idComunaCumplimiento = idComunaCumplimiento;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

     

    public TipoCumplimiento getIdTipoCumplimiento() {
        return idTipoCumplimiento;
    }

    public void setIdTipoCumplimiento(TipoCumplimiento idTipoCumplimiento) {
        this.idTipoCumplimiento = idTipoCumplimiento;
    }

    public Mes getIdMes() {
        return idMes;
    }

    public void setIdMes(Mes idMes) {
        this.idMes = idMes;
    }

    public Rebaja getRebaja() {
		return rebaja;
	}

	public void setRebaja(Rebaja rebaja) {
		this.rebaja = rebaja;
	}

	public ComunaCumplimientoRebaja getRebajaFinal() {
		return rebajaFinal;
	}

	public void setRebajaFinal(ComunaCumplimientoRebaja rebajaFinal) {
		this.rebajaFinal = rebajaFinal;
	}

	public ComunaCumplimientoRebaja getRebajaCalculada() {
		return rebajaCalculada;
	}

	public void setRebajaCalculada(ComunaCumplimientoRebaja rebajaCalculada) {
		this.rebajaCalculada = rebajaCalculada;
	}

	public Comuna getIdComuna() {
        return idComuna;
    }

    public void setIdComuna(Comuna idComuna) {
        this.idComuna = idComuna;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idComunaCumplimiento != null ? idComunaCumplimiento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComunaCumplimiento)) {
            return false;
        }
        ComunaCumplimiento other = (ComunaCumplimiento) object;
        if ((this.idComunaCumplimiento == null && other.idComunaCumplimiento != null) || (this.idComunaCumplimiento != null && !this.idComunaCumplimiento.equals(other.idComunaCumplimiento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ComunaCumplimiento[ idComunaCumplimiento=" + idComunaCumplimiento + " ]";
    }
    
}