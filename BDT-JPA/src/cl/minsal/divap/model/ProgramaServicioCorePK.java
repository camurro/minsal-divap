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
public class ProgramaServicioCorePK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6150372486753871920L;
	@Basic(optional = false)
    @Column(name = "programa_ano")
    private int programaAno;
    @Basic(optional = false)
    @Column(name = "comuna")
    private int comuna;

    public ProgramaServicioCorePK() {
    }

    public ProgramaServicioCorePK(int programaAno, int comuna) {
        this.programaAno = programaAno;
        this.comuna = comuna;
    }

    public int getProgramaAno() {
        return programaAno;
    }

    public void setProgramaAno(int programaAno) {
        this.programaAno = programaAno;
    }

    public int getComuna() {
        return comuna;
    }

    public void setComuna(int comuna) {
        this.comuna = comuna;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) programaAno;
        hash += (int) comuna;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ProgramaServicioCorePK)) {
            return false;
        }
        ProgramaServicioCorePK other = (ProgramaServicioCorePK) object;
        if (this.programaAno != other.programaAno) {
            return false;
        }
        if (this.comuna != other.comuna) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ProgramaServicioCorePK[ programaAno=" + programaAno + ", comuna=" + comuna + " ]";
    }
    
}
