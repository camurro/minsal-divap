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
@Table(name = "marco_presupuestario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MarcoPresupuestario.findAll", query = "SELECT m FROM MarcoPresupuestario m"),
    @NamedQuery(name = "MarcoPresupuestario.findByMarcoInicial", query = "SELECT m FROM MarcoPresupuestario m WHERE m.marcoInicial = :marcoInicial"),
    @NamedQuery(name = "MarcoPresupuestario.findByMarcoModificado", query = "SELECT m FROM MarcoPresupuestario m WHERE m.marcoModificado = :marcoModificado"),
    @NamedQuery(name = "MarcoPresupuestario.findByIdMarcoPresupuestario", query = "SELECT m FROM MarcoPresupuestario m WHERE m.idMarcoPresupuestario = :idMarcoPresupuestario")})
public class MarcoPresupuestario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "marco_inicial")
    private short marcoInicial;
    @Basic(optional = false)
    @Column(name = "marco_modificado")
    private short marcoModificado;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_marco_presupuestario")
    private Integer idMarcoPresupuestario;
    @JoinColumn(name = "id_servicio_salud", referencedColumnName = "id")
    @ManyToOne
    private ServicioSalud idServicioSalud;
    @JoinColumn(name = "id_programa", referencedColumnName = "id")
    @ManyToOne
    private Programa idPrograma;
    @JoinColumn(name = "ano_ano_en_curso", referencedColumnName = "ano")
    @ManyToOne
    private AnoEnCurso anoAnoEnCurso;

    public MarcoPresupuestario() {
    }

    public MarcoPresupuestario(Integer idMarcoPresupuestario) {
        this.idMarcoPresupuestario = idMarcoPresupuestario;
    }

    public MarcoPresupuestario(Integer idMarcoPresupuestario, short marcoInicial, short marcoModificado) {
        this.idMarcoPresupuestario = idMarcoPresupuestario;
        this.marcoInicial = marcoInicial;
        this.marcoModificado = marcoModificado;
    }

    public short getMarcoInicial() {
        return marcoInicial;
    }

    public void setMarcoInicial(short marcoInicial) {
        this.marcoInicial = marcoInicial;
    }

    public short getMarcoModificado() {
        return marcoModificado;
    }

    public void setMarcoModificado(short marcoModificado) {
        this.marcoModificado = marcoModificado;
    }

    public Integer getIdMarcoPresupuestario() {
        return idMarcoPresupuestario;
    }

    public void setIdMarcoPresupuestario(Integer idMarcoPresupuestario) {
        this.idMarcoPresupuestario = idMarcoPresupuestario;
    }

    public ServicioSalud getIdServicioSalud() {
        return idServicioSalud;
    }

    public void setIdServicioSalud(ServicioSalud idServicioSalud) {
        this.idServicioSalud = idServicioSalud;
    }

    public Programa getIdPrograma() {
        return idPrograma;
    }

    public void setIdPrograma(Programa idPrograma) {
        this.idPrograma = idPrograma;
    }

    public AnoEnCurso getAnoAnoEnCurso() {
        return anoAnoEnCurso;
    }

    public void setAnoAnoEnCurso(AnoEnCurso anoAnoEnCurso) {
        this.anoAnoEnCurso = anoAnoEnCurso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMarcoPresupuestario != null ? idMarcoPresupuestario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
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
