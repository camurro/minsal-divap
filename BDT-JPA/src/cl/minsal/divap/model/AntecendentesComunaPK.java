/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.minsal.divap.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author cmurillo
 */
@Embeddable
public class AntecendentesComunaPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ano_ano_en_curso")
    private short anoAnoEnCurso;
    @Basic(optional = false)
    @Column(name = "id_comuna")
    private int idComuna;

    public AntecendentesComunaPK() {
    }

    public AntecendentesComunaPK(short anoAnoEnCurso, int idComuna) {
        this.anoAnoEnCurso = anoAnoEnCurso;
        this.idComuna = idComuna;
    }

    public short getAnoAnoEnCurso() {
        return anoAnoEnCurso;
    }

    public void setAnoAnoEnCurso(short anoAnoEnCurso) {
        this.anoAnoEnCurso = anoAnoEnCurso;
    }

    public int getIdComuna() {
        return idComuna;
    }

    public void setIdComuna(int idComuna) {
        this.idComuna = idComuna;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) anoAnoEnCurso;
        hash += (int) idComuna;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AntecendentesComunaPK)) {
            return false;
        }
        AntecendentesComunaPK other = (AntecendentesComunaPK) object;
        if (this.anoAnoEnCurso != other.anoAnoEnCurso) {
            return false;
        }
        if (this.idComuna != other.idComuna) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.AntecendentesComunaPK[ anoAnoEnCurso=" + anoAnoEnCurso + ", idComuna=" + idComuna + " ]";
    }
    
}
