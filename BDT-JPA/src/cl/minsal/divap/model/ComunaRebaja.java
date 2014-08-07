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
import javax.persistence.GenerationType;
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
@Table(name = "comuna_rebaja")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ComunaRebaja.findAll", query = "SELECT c FROM ComunaRebaja c"),
    @NamedQuery(name = "ComunaRebaja.findByIdComunaRebaja", query = "SELECT c FROM ComunaRebaja c WHERE c.comunaCumplimiento.idComuna.id = :idComunaRebaja order by c.comunaCumplimiento.idComuna.id asc"),
    @NamedQuery(name = "ComunaRebaja.findByIdCumplimiento", query = "SELECT c FROM ComunaRebaja c WHERE c.comunaCumplimiento.idComunaCumplimiento = :idComunaCumplimiento"),
    @NamedQuery(name = "ComunaRebaja.findByRebaja", query = "SELECT c FROM ComunaRebaja c WHERE c.rebajaCalculada = :rebajaCalculada"),
    @NamedQuery(name = "ComunaRebaja.deleteUsingIdCumplimiento", query = "DELETE FROM ComunaRebaja c WHERE c.comunaCumplimiento.idComunaCumplimiento IN (:listaIdCumplimientos)")})
public class ComunaRebaja implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_comuna_rebaja")
    private Integer idComunaRebaja;
    // @Mavalue=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    private Double rebajaCalculada;
    @Column(name = "rebaja_final")
    private Double rebajaFinal;
    @JoinColumn(name = "comuna_cumplimiento", referencedColumnName = "id_comuna_cumplimiento")
    @ManyToOne
    private ComunaCumplimiento comunaCumplimiento;

    public ComunaRebaja() {
    }

    public ComunaRebaja(Integer idComunaRebaja) {
        this.idComunaRebaja = idComunaRebaja;
    }

    public Integer getIdComunaRebaja() {
        return idComunaRebaja;
    }

    public void setIdComunaRebaja(Integer idComunaRebaja) {
        this.idComunaRebaja = idComunaRebaja;
    }

    public Double getRebajaCalculada() {
        return rebajaCalculada;
    }

    public void setRebajaCalculada(Double rebajaCalculada) {
        this.rebajaCalculada = rebajaCalculada;
    }
    
    public Double getRebajaFinal() {
		return rebajaFinal;
	}

	public void setRebajaFinal(Double rebajaFinal) {
		this.rebajaFinal = rebajaFinal;
	}

	public ComunaCumplimiento getComunaCumplimiento() {
        return comunaCumplimiento;
    }

    public void setComunaCumplimiento(ComunaCumplimiento comunaCumplimiento) {
        this.comunaCumplimiento = comunaCumplimiento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idComunaRebaja != null ? idComunaRebaja.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComunaRebaja)) {
            return false;
        }
        ComunaRebaja other = (ComunaRebaja) object;
        if ((this.idComunaRebaja == null && other.idComunaRebaja != null) || (this.idComunaRebaja != null && !this.idComunaRebaja.equals(other.idComunaRebaja))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ComunaRebaja[ idComunaRebaja=" + idComunaRebaja + " ]";
    }
    
}