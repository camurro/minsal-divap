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
@Table(name = "antecedentes_comuna_calculado_rebaja")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AntecedentesComunaCalculadoRebaja.findAll", query = "SELECT a FROM AntecedentesComunaCalculadoRebaja a"),
    @NamedQuery(name = "AntecedentesComunaCalculadoRebaja.findByIdAntecedentesComunaCalculadoRebaja", query = "SELECT a FROM AntecedentesComunaCalculadoRebaja a WHERE a.idAntecedentesComunaCalculadoRebaja = :idAntecedentesComunaCalculadoRebaja"),
    @NamedQuery(name = "AntecedentesComunaCalculadoRebaja.findByMontoRebaja", query = "SELECT a FROM AntecedentesComunaCalculadoRebaja a WHERE a.montoRebaja = :montoRebaja")})
public class AntecedentesComunaCalculadoRebaja implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
  	@Column(name="id_antecedentes_comuna_calculado_rebaja", unique=true, nullable=false)
  	@GeneratedValue
    private Integer idAntecedentesComunaCalculadoRebaja;
    @Basic(optional = false)
    @Column(name = "monto_rebaja")
    private int montoRebaja;
    @JoinColumn(name = "rebaja", referencedColumnName = "id_rebaja")
    @ManyToOne(optional = false)
    private Rebaja rebaja;
    @JoinColumn(name = "antecedentes_comuna_calculado", referencedColumnName = "id_antecendentes_comuna_calculado")
    @ManyToOne(optional = false)
    private AntecendentesComunaCalculado antecedentesComunaCalculado;

    public AntecedentesComunaCalculadoRebaja() {
    }

    public AntecedentesComunaCalculadoRebaja(Integer idAntecedentesComunaCalculadoRebaja) {
        this.idAntecedentesComunaCalculadoRebaja = idAntecedentesComunaCalculadoRebaja;
    }

    public AntecedentesComunaCalculadoRebaja(Integer idAntecedentesComunaCalculadoRebaja, int montoRebaja) {
        this.idAntecedentesComunaCalculadoRebaja = idAntecedentesComunaCalculadoRebaja;
        this.montoRebaja = montoRebaja;
    }

    public Integer getIdAntecedentesComunaCalculadoRebaja() {
        return idAntecedentesComunaCalculadoRebaja;
    }

    public void setIdAntecedentesComunaCalculadoRebaja(Integer idAntecedentesComunaCalculadoRebaja) {
        this.idAntecedentesComunaCalculadoRebaja = idAntecedentesComunaCalculadoRebaja;
    }

    public int getMontoRebaja() {
        return montoRebaja;
    }

    public void setMontoRebaja(int montoRebaja) {
        this.montoRebaja = montoRebaja;
    }

    public Rebaja getRebaja() {
        return rebaja;
    }

    public void setRebaja(Rebaja rebaja) {
        this.rebaja = rebaja;
    }

    public AntecendentesComunaCalculado getAntecedentesComunaCalculado() {
        return antecedentesComunaCalculado;
    }

    public void setAntecedentesComunaCalculado(AntecendentesComunaCalculado antecedentesComunaCalculado) {
        this.antecedentesComunaCalculado = antecedentesComunaCalculado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAntecedentesComunaCalculadoRebaja != null ? idAntecedentesComunaCalculadoRebaja.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AntecedentesComunaCalculadoRebaja)) {
            return false;
        }
        AntecedentesComunaCalculadoRebaja other = (AntecedentesComunaCalculadoRebaja) object;
        if ((this.idAntecedentesComunaCalculadoRebaja == null && other.idAntecedentesComunaCalculadoRebaja != null) || (this.idAntecedentesComunaCalculadoRebaja != null && !this.idAntecedentesComunaCalculadoRebaja.equals(other.idAntecedentesComunaCalculadoRebaja))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.AntecedentesComunaCalculadoRebaja[ idAntecedentesComunaCalculadoRebaja=" + idAntecedentesComunaCalculadoRebaja + " ]";
    }
    
}
