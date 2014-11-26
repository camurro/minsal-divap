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
@Table(name = "convenio_seguimiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConvenioSeguimiento.findAll", query = "SELECT c FROM ConvenioSeguimiento c"),
    @NamedQuery(name = "ConvenioSeguimiento.findByIdConvenioSeguimiento", query = "SELECT c FROM ConvenioSeguimiento c WHERE c.idConvenioSeguimiento = :idConvenioSeguimiento")})
public class ConvenioSeguimiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
  	@Column(name="id_convenio_seguimiento", unique=true, nullable=false)
  	@GeneratedValue
    private Integer idConvenioSeguimiento;
    @JoinColumn(name = "seguimiento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Seguimiento seguimiento;
    @JoinColumn(name = "convenio", referencedColumnName = "id_convenio")
    @ManyToOne(optional = false)
    private Convenio convenio;

    public ConvenioSeguimiento() {
    }

    public ConvenioSeguimiento(Integer idConvenioSeguimiento) {
        this.idConvenioSeguimiento = idConvenioSeguimiento;
    }

    public Integer getIdConvenioSeguimiento() {
        return idConvenioSeguimiento;
    }

    public void setIdConvenioSeguimiento(Integer idConvenioSeguimiento) {
        this.idConvenioSeguimiento = idConvenioSeguimiento;
    }

    public Seguimiento getSeguimiento() {
        return seguimiento;
    }

    public void setSeguimiento(Seguimiento seguimiento) {
        this.seguimiento = seguimiento;
    }

    public Convenio getConvenio() {
        return convenio;
    }

    public void setConvenio(Convenio convenio) {
        this.convenio = convenio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idConvenioSeguimiento != null ? idConvenioSeguimiento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ConvenioSeguimiento)) {
            return false;
        }
        ConvenioSeguimiento other = (ConvenioSeguimiento) object;
        if ((this.idConvenioSeguimiento == null && other.idConvenioSeguimiento != null) || (this.idConvenioSeguimiento != null && !this.idConvenioSeguimiento.equals(other.idConvenioSeguimiento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ConvenioSeguimiento[ idConvenioSeguimiento=" + idConvenioSeguimiento + " ]";
    }
    
}
