/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.minsal.divap.model;

import java.io.Serializable;
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
@Table(name = "programa_municipal_core")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProgramaMunicipalCore.findAll", query = "SELECT p FROM ProgramaMunicipalCore p"),
    @NamedQuery(name = "ProgramaMunicipalCore.findByColumna1", query = "SELECT p FROM ProgramaMunicipalCore p WHERE p.columna1 = :columna1"),
    @NamedQuery(name = "ProgramaMunicipalCore.findByColumna2", query = "SELECT p FROM ProgramaMunicipalCore p WHERE p.columna2 = :columna2"),
    @NamedQuery(name = "ProgramaMunicipalCore.findByColumna3", query = "SELECT p FROM ProgramaMunicipalCore p WHERE p.columna3 = :columna3"),
    @NamedQuery(name = "ProgramaMunicipalCore.findByColumna4", query = "SELECT p FROM ProgramaMunicipalCore p WHERE p.columna4 = :columna4"),
    @NamedQuery(name = "ProgramaMunicipalCore.findByColumna5", query = "SELECT p FROM ProgramaMunicipalCore p WHERE p.columna5 = :columna5"),
    @NamedQuery(name = "ProgramaMunicipalCore.findByColumna6", query = "SELECT p FROM ProgramaMunicipalCore p WHERE p.columna6 = :columna6"),
    @NamedQuery(name = "ProgramaMunicipalCore.findByColumna7", query = "SELECT p FROM ProgramaMunicipalCore p WHERE p.columna7 = :columna7"),
    @NamedQuery(name = "ProgramaMunicipalCore.findByColumna8", query = "SELECT p FROM ProgramaMunicipalCore p WHERE p.columna8 = :columna8"),
    @NamedQuery(name = "ProgramaMunicipalCore.findByColumna9", query = "SELECT p FROM ProgramaMunicipalCore p WHERE p.columna9 = :columna9"),
    @NamedQuery(name = "ProgramaMunicipalCore.findByColumna10", query = "SELECT p FROM ProgramaMunicipalCore p WHERE p.columna10 = :columna10"),
    @NamedQuery(name = "ProgramaMunicipalCore.findByIdPrograma", query = "SELECT p FROM ProgramaMunicipalCore p WHERE p.programaMunicipalCorePK.idPrograma = :idPrograma"),
    @NamedQuery(name = "ProgramaMunicipalCore.findByIdComuna", query = "SELECT p FROM ProgramaMunicipalCore p WHERE p.programaMunicipalCorePK.idComuna = :idComuna")})
public class ProgramaMunicipalCore implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProgramaMunicipalCorePK programaMunicipalCorePK;
    @Column(name = "columna1")
    private String columna1;
    @Column(name = "columna2")
    private String columna2;
    @Column(name = "columna3")
    private String columna3;
    @Column(name = "columna4")
    private String columna4;
    @Column(name = "columna5")
    private String columna5;
    @Column(name = "columna6")
    private String columna6;
    @Column(name = "columna7")
    private String columna7;
    @Column(name = "columna8")
    private String columna8;
    @Column(name = "columna9")
    private String columna9;
    @Column(name = "columna10")
    private String columna10;
    @JoinColumn(name = "id_programa", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Programa programa;
    @JoinColumn(name = "id_comuna", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Comuna comuna;

    public ProgramaMunicipalCore() {
    }

    public ProgramaMunicipalCore(ProgramaMunicipalCorePK programaMunicipalCorePK) {
        this.programaMunicipalCorePK = programaMunicipalCorePK;
    }

    public ProgramaMunicipalCore(int idPrograma, int idComuna) {
        this.programaMunicipalCorePK = new ProgramaMunicipalCorePK(idPrograma, idComuna);
    }

    public ProgramaMunicipalCorePK getProgramaMunicipalCorePK() {
        return programaMunicipalCorePK;
    }

    public void setProgramaMunicipalCorePK(ProgramaMunicipalCorePK programaMunicipalCorePK) {
        this.programaMunicipalCorePK = programaMunicipalCorePK;
    }

    public String getColumna1() {
        return columna1;
    }

    public void setColumna1(String columna1) {
        this.columna1 = columna1;
    }

    public String getColumna2() {
        return columna2;
    }

    public void setColumna2(String columna2) {
        this.columna2 = columna2;
    }

    public String getColumna3() {
        return columna3;
    }

    public void setColumna3(String columna3) {
        this.columna3 = columna3;
    }

    public String getColumna4() {
        return columna4;
    }

    public void setColumna4(String columna4) {
        this.columna4 = columna4;
    }

    public String getColumna5() {
        return columna5;
    }

    public void setColumna5(String columna5) {
        this.columna5 = columna5;
    }

    public String getColumna6() {
        return columna6;
    }

    public void setColumna6(String columna6) {
        this.columna6 = columna6;
    }

    public String getColumna7() {
        return columna7;
    }

    public void setColumna7(String columna7) {
        this.columna7 = columna7;
    }

    public String getColumna8() {
        return columna8;
    }

    public void setColumna8(String columna8) {
        this.columna8 = columna8;
    }

    public String getColumna9() {
        return columna9;
    }

    public void setColumna9(String columna9) {
        this.columna9 = columna9;
    }

    public String getColumna10() {
        return columna10;
    }

    public void setColumna10(String columna10) {
        this.columna10 = columna10;
    }

    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }

    public Comuna getComuna() {
        return comuna;
    }

    public void setComuna(Comuna comuna) {
        this.comuna = comuna;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (programaMunicipalCorePK != null ? programaMunicipalCorePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProgramaMunicipalCore)) {
            return false;
        }
        ProgramaMunicipalCore other = (ProgramaMunicipalCore) object;
        if ((this.programaMunicipalCorePK == null && other.programaMunicipalCorePK != null) || (this.programaMunicipalCorePK != null && !this.programaMunicipalCorePK.equals(other.programaMunicipalCorePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ProgramaMunicipalCore[ programaMunicipalCorePK=" + programaMunicipalCorePK + " ]";
    }
    
}
