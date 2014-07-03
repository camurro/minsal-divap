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
public class ProgramaMunicipalCorePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "id_programa")
    private int idPrograma;
    @Basic(optional = false)
    @Column(name = "id_comuna")
    private int idComuna;

    public ProgramaMunicipalCorePK() {
    }

    public ProgramaMunicipalCorePK(int idPrograma, int idComuna) {
        this.idPrograma = idPrograma;
        this.idComuna = idComuna;
    }

    public int getIdPrograma() {
        return idPrograma;
    }

    public void setIdPrograma(int idPrograma) {
        this.idPrograma = idPrograma;
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
        hash += (int) idPrograma;
        hash += (int) idComuna;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProgramaMunicipalCorePK)) {
            return false;
        }
        ProgramaMunicipalCorePK other = (ProgramaMunicipalCorePK) object;
        if (this.idPrograma != other.idPrograma) {
            return false;
        }
        if (this.idComuna != other.idComuna) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ProgramaMunicipalCorePK[ idPrograma=" + idPrograma + ", idComuna=" + idComuna + " ]";
    }
    
}
