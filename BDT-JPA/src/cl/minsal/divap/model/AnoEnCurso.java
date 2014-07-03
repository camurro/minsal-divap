/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author cmurillo
 */
@Entity
@Table(name = "ano_en_curso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AnoEnCurso.findAll", query = "SELECT a FROM AnoEnCurso a"),
    @NamedQuery(name = "AnoEnCurso.findByAno", query = "SELECT a FROM AnoEnCurso a WHERE a.ano = :ano"),
    @NamedQuery(name = "AnoEnCurso.findByMontoPercapitalBasal", query = "SELECT a FROM AnoEnCurso a WHERE a.montoPercapitalBasal = :montoPercapitalBasal")})
public class AnoEnCurso implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ano")
    private Short ano;
    @Basic(optional = false)
    @Column(name = "monto_percapital_basal")
    private short montoPercapitalBasal;
    @OneToMany(mappedBy = "anoAnoEnCurso")
    private Collection<MarcoPresupuestario> marcoPresupuestarioCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "anoEnCurso")
    private Collection<AntecendentesComuna> antecendentesComunaCollection;

    public AnoEnCurso() {
    }

    public AnoEnCurso(Short ano) {
        this.ano = ano;
    }

    public AnoEnCurso(Short ano, short montoPercapitalBasal) {
        this.ano = ano;
        this.montoPercapitalBasal = montoPercapitalBasal;
    }

    public Short getAno() {
        return ano;
    }

    public void setAno(Short ano) {
        this.ano = ano;
    }

    public short getMontoPercapitalBasal() {
        return montoPercapitalBasal;
    }

    public void setMontoPercapitalBasal(short montoPercapitalBasal) {
        this.montoPercapitalBasal = montoPercapitalBasal;
    }

    @XmlTransient
    public Collection<MarcoPresupuestario> getMarcoPresupuestarioCollection() {
        return marcoPresupuestarioCollection;
    }

    public void setMarcoPresupuestarioCollection(Collection<MarcoPresupuestario> marcoPresupuestarioCollection) {
        this.marcoPresupuestarioCollection = marcoPresupuestarioCollection;
    }

    @XmlTransient
    public Collection<AntecendentesComuna> getAntecendentesComunaCollection() {
        return antecendentesComunaCollection;
    }

    public void setAntecendentesComunaCollection(Collection<AntecendentesComuna> antecendentesComunaCollection) {
        this.antecendentesComunaCollection = antecendentesComunaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ano != null ? ano.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AnoEnCurso)) {
            return false;
        }
        AnoEnCurso other = (AnoEnCurso) object;
        if ((this.ano == null && other.ano != null) || (this.ano != null && !this.ano.equals(other.ano))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.AnoEnCurso[ ano=" + ano + " ]";
    }
    
}
