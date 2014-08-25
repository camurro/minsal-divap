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
public class ProgramaMunicipalCoreComponentePK implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3332371119515232340L;
	@Basic(optional = false)
    @Column(name = "programa_municipal_core")
    private int programaMunicipalCore;
    @Basic(optional = false)
    @Column(name = "componente")
    private int componente;

    public ProgramaMunicipalCoreComponentePK() {
    }

    public ProgramaMunicipalCoreComponentePK(int programaMunicipalCore, int componente) {
        this.programaMunicipalCore = programaMunicipalCore;
        this.componente = componente;
    }

    public int getProgramaMunicipalCore() {
        return programaMunicipalCore;
    }

    public void setProgramaMunicipalCore(int programaMunicipalCore) {
        this.programaMunicipalCore = programaMunicipalCore;
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
        hash += (int) programaMunicipalCore;
        hash += (int) componente;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ProgramaMunicipalCoreComponentePK)) {
            return false;
        }
        ProgramaMunicipalCoreComponentePK other = (ProgramaMunicipalCoreComponentePK) object;
        if (this.programaMunicipalCore != other.programaMunicipalCore) {
            return false;
        }
        if (this.componente != other.componente) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ProgramaMunicipalCoreComponentePK[ programaMunicipalCore=" + programaMunicipalCore + ", componente=" + componente + " ]";
    }
    
}