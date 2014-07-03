/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.minsal.divap.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "antecendentes_comuna")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AntecendentesComuna.findAll", query = "SELECT a FROM AntecendentesComuna a"),
    @NamedQuery(name = "AntecendentesComuna.findByClasificacion", query = "SELECT a FROM AntecendentesComuna a WHERE a.clasificacion = :clasificacion"),
    @NamedQuery(name = "AntecendentesComuna.findByAsignacionZona", query = "SELECT a FROM AntecendentesComuna a WHERE a.asignacionZona = :asignacionZona"),
    @NamedQuery(name = "AntecendentesComuna.findByPoblacion", query = "SELECT a FROM AntecendentesComuna a WHERE a.poblacion = :poblacion"),
    @NamedQuery(name = "AntecendentesComuna.findByPoblacionMayor", query = "SELECT a FROM AntecendentesComuna a WHERE a.poblacionMayor = :poblacionMayor"),
    @NamedQuery(name = "AntecendentesComuna.findByDesempenoDificil", query = "SELECT a FROM AntecendentesComuna a WHERE a.desempenoDificil = :desempenoDificil"),
    @NamedQuery(name = "AntecendentesComuna.findByTramoPobreza", query = "SELECT a FROM AntecendentesComuna a WHERE a.tramoPobreza = :tramoPobreza"),
    @NamedQuery(name = "AntecendentesComuna.findByPobreza", query = "SELECT a FROM AntecendentesComuna a WHERE a.pobreza = :pobreza"),
    @NamedQuery(name = "AntecendentesComuna.findByRuralidad", query = "SELECT a FROM AntecendentesComuna a WHERE a.ruralidad = :ruralidad"),
    @NamedQuery(name = "AntecendentesComuna.findByValorReferencialZona", query = "SELECT a FROM AntecendentesComuna a WHERE a.valorReferencialZona = :valorReferencialZona"),
    @NamedQuery(name = "AntecendentesComuna.findByAnoAnoEnCurso", query = "SELECT a FROM AntecendentesComuna a WHERE a.antecendentesComunaPK.anoAnoEnCurso = :anoAnoEnCurso"),
    @NamedQuery(name = "AntecendentesComuna.findByIdComuna", query = "SELECT a FROM AntecendentesComuna a WHERE a.antecendentesComunaPK.idComuna = :idComuna")})
public class AntecendentesComuna implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AntecendentesComunaPK antecendentesComunaPK;
    @Basic(optional = false)
    @Column(name = "clasificacion")
    private String clasificacion;
    @Basic(optional = false)
    @Column(name = "asignacion_zona")
    private short asignacionZona;
    @Basic(optional = false)
    @Column(name = "poblacion")
    private short poblacion;
    @Basic(optional = false)
    @Column(name = "poblacion_mayor")
    private short poblacionMayor;
    @Basic(optional = false)
    @Column(name = "desempeno_dificil")
    private short desempenoDificil;
    @Basic(optional = false)
    @Column(name = "tramo_pobreza")
    private short tramoPobreza;
    @Basic(optional = false)
    @Column(name = "pobreza")
    private short pobreza;
    @Basic(optional = false)
    @Column(name = "ruralidad")
    private short ruralidad;
    @Basic(optional = false)
    @Column(name = "valor_referencial_zona")
    private short valorReferencialZona;
    @JoinColumn(name = "id_comuna", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Comuna comuna;
    @JoinColumn(name = "ano_ano_en_curso", referencedColumnName = "ano", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private AnoEnCurso anoEnCurso;

    public AntecendentesComuna() {
    }

    public AntecendentesComuna(AntecendentesComunaPK antecendentesComunaPK) {
        this.antecendentesComunaPK = antecendentesComunaPK;
    }

    public AntecendentesComuna(AntecendentesComunaPK antecendentesComunaPK, String clasificacion, short asignacionZona, short poblacion, short poblacionMayor, short desempenoDificil, short tramoPobreza, short pobreza, short ruralidad, short valorReferencialZona) {
        this.antecendentesComunaPK = antecendentesComunaPK;
        this.clasificacion = clasificacion;
        this.asignacionZona = asignacionZona;
        this.poblacion = poblacion;
        this.poblacionMayor = poblacionMayor;
        this.desempenoDificil = desempenoDificil;
        this.tramoPobreza = tramoPobreza;
        this.pobreza = pobreza;
        this.ruralidad = ruralidad;
        this.valorReferencialZona = valorReferencialZona;
    }

    public AntecendentesComuna(short anoAnoEnCurso, int idComuna) {
        this.antecendentesComunaPK = new AntecendentesComunaPK(anoAnoEnCurso, idComuna);
    }

    public AntecendentesComunaPK getAntecendentesComunaPK() {
        return antecendentesComunaPK;
    }

    public void setAntecendentesComunaPK(AntecendentesComunaPK antecendentesComunaPK) {
        this.antecendentesComunaPK = antecendentesComunaPK;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public short getAsignacionZona() {
        return asignacionZona;
    }

    public void setAsignacionZona(short asignacionZona) {
        this.asignacionZona = asignacionZona;
    }

    public short getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(short poblacion) {
        this.poblacion = poblacion;
    }

    public short getPoblacionMayor() {
        return poblacionMayor;
    }

    public void setPoblacionMayor(short poblacionMayor) {
        this.poblacionMayor = poblacionMayor;
    }

    public short getDesempenoDificil() {
        return desempenoDificil;
    }

    public void setDesempenoDificil(short desempenoDificil) {
        this.desempenoDificil = desempenoDificil;
    }

    public short getTramoPobreza() {
        return tramoPobreza;
    }

    public void setTramoPobreza(short tramoPobreza) {
        this.tramoPobreza = tramoPobreza;
    }

    public short getPobreza() {
        return pobreza;
    }

    public void setPobreza(short pobreza) {
        this.pobreza = pobreza;
    }

    public short getRuralidad() {
        return ruralidad;
    }

    public void setRuralidad(short ruralidad) {
        this.ruralidad = ruralidad;
    }

    public short getValorReferencialZona() {
        return valorReferencialZona;
    }

    public void setValorReferencialZona(short valorReferencialZona) {
        this.valorReferencialZona = valorReferencialZona;
    }

    public Comuna getComuna() {
        return comuna;
    }

    public void setComuna(Comuna comuna) {
        this.comuna = comuna;
    }

    public AnoEnCurso getAnoEnCurso() {
        return anoEnCurso;
    }

    public void setAnoEnCurso(AnoEnCurso anoEnCurso) {
        this.anoEnCurso = anoEnCurso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (antecendentesComunaPK != null ? antecendentesComunaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AntecendentesComuna)) {
            return false;
        }
        AntecendentesComuna other = (AntecendentesComuna) object;
        if ((this.antecendentesComunaPK == null && other.antecendentesComunaPK != null) || (this.antecendentesComunaPK != null && !this.antecendentesComunaPK.equals(other.antecendentesComunaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.AntecendentesComuna[ antecendentesComunaPK=" + antecendentesComunaPK + " ]";
    }
    
}
