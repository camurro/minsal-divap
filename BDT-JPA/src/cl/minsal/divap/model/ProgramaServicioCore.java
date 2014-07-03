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
@Table(name = "programa_servicio_core")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProgramaServicioCore.findAll", query = "SELECT p FROM ProgramaServicioCore p"),
    @NamedQuery(name = "ProgramaServicioCore.findByColumna1", query = "SELECT p FROM ProgramaServicioCore p WHERE p.columna1 = :columna1"),
    @NamedQuery(name = "ProgramaServicioCore.findByColumna2", query = "SELECT p FROM ProgramaServicioCore p WHERE p.columna2 = :columna2"),
    @NamedQuery(name = "ProgramaServicioCore.findByColumna3", query = "SELECT p FROM ProgramaServicioCore p WHERE p.columna3 = :columna3"),
    @NamedQuery(name = "ProgramaServicioCore.findByColumna4", query = "SELECT p FROM ProgramaServicioCore p WHERE p.columna4 = :columna4"),
    @NamedQuery(name = "ProgramaServicioCore.findByColumna5", query = "SELECT p FROM ProgramaServicioCore p WHERE p.columna5 = :columna5"),
    @NamedQuery(name = "ProgramaServicioCore.findByColumna6", query = "SELECT p FROM ProgramaServicioCore p WHERE p.columna6 = :columna6"),
    @NamedQuery(name = "ProgramaServicioCore.findByColumna7", query = "SELECT p FROM ProgramaServicioCore p WHERE p.columna7 = :columna7"),
    @NamedQuery(name = "ProgramaServicioCore.findByColumna8", query = "SELECT p FROM ProgramaServicioCore p WHERE p.columna8 = :columna8"),
    @NamedQuery(name = "ProgramaServicioCore.findByColumna9", query = "SELECT p FROM ProgramaServicioCore p WHERE p.columna9 = :columna9"),
    @NamedQuery(name = "ProgramaServicioCore.findByColumna10", query = "SELECT p FROM ProgramaServicioCore p WHERE p.columna10 = :columna10"),
    @NamedQuery(name = "ProgramaServicioCore.findByIdProgramaServicio", query = "SELECT p FROM ProgramaServicioCore p WHERE p.idProgramaServicio = :idProgramaServicio")})
public class ProgramaServicioCore implements Serializable {
    private static final long serialVersionUID = 1L;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_programa_servicio")
    private Integer idProgramaServicio;
    @JoinColumn(name = "id_servicio_salud", referencedColumnName = "id")
    @ManyToOne
    private ServicioSalud idServicioSalud;
    @JoinColumn(name = "id_programa", referencedColumnName = "id")
    @ManyToOne
    private Programa idPrograma;

    public ProgramaServicioCore() {
    }

    public ProgramaServicioCore(Integer idProgramaServicio) {
        this.idProgramaServicio = idProgramaServicio;
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

    public Integer getIdProgramaServicio() {
        return idProgramaServicio;
    }

    public void setIdProgramaServicio(Integer idProgramaServicio) {
        this.idProgramaServicio = idProgramaServicio;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProgramaServicio != null ? idProgramaServicio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProgramaServicioCore)) {
            return false;
        }
        ProgramaServicioCore other = (ProgramaServicioCore) object;
        if ((this.idProgramaServicio == null && other.idProgramaServicio != null) || (this.idProgramaServicio != null && !this.idProgramaServicio.equals(other.idProgramaServicio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ProgramaServicioCore[ idProgramaServicio=" + idProgramaServicio + " ]";
    }
    
}
