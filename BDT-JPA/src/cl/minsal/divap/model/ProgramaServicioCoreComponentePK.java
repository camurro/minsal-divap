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
public class ProgramaServicioCoreComponentePK implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4173003610347035200L;
	@Basic(optional = false)
    @Column(name = "programa_servicio_core")
    private int programaServicioCore;
    @Basic(optional = false)
    @Column(name = "componente")
    private int componente;

    public ProgramaServicioCoreComponentePK() {
    }

    public ProgramaServicioCoreComponentePK(int programaServicioCore, int componente) {
        this.programaServicioCore = programaServicioCore;
        this.componente = componente;
    }

    public int getProgramaServicioCore() {
        return programaServicioCore;
    }

    public void setProgramaServicioCore(int programaServicioCore) {
        this.programaServicioCore = programaServicioCore;
    }

    public int getComponente() {
        return componente;
    }

    public void setComponente(int componente) {
        this.componente = componente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += programaServicioCore;
        hash += componente;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ProgramaServicioCoreComponentePK)) {
            return false;
        }
        ProgramaServicioCoreComponentePK other = (ProgramaServicioCoreComponentePK) object;
        if (this.programaServicioCore != other.programaServicioCore) {
            return false;
        }
        if (this.componente != other.componente) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ProgramaServicioCoreComponentePK[ programaServicioCore=" + programaServicioCore + ", componente=" + componente + " ]";
    }
    
}