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
	    @Basic(optional = false)
	    @Column(name = "subtitulo")
	    private int subtitulo;

	    public ProgramaServicioCoreComponentePK() {
	    }

	    public ProgramaServicioCoreComponentePK(int programaServicioCore, int componente, int subtitulo) {
	        this.programaServicioCore = programaServicioCore;
	        this.componente = componente;
	        this.subtitulo = subtitulo;
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

	    public int getSubtitulo() {
	        return subtitulo;
	    }

	    public void setSubtitulo(int subtitulo) {
	        this.subtitulo = subtitulo;
	    }

	    @Override
	    public int hashCode() {
	        int hash = 0;
	        hash += (int) programaServicioCore;
	        hash += (int) componente;
	        hash += (int) subtitulo;
	        return hash;
	    }

	    @Override
	    public boolean equals(Object object) {
	        // TODO: Warning - this method won't work in the case the id fields are not set
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
	        if (this.subtitulo != other.subtitulo) {
	            return false;
	        }
	        return true;
	    }

	    @Override
	    public String toString() {
	        return "cl.minsal.divap.model.ProgramaServicioCoreComponentePK[ programaServicioCore=" + programaServicioCore + ", componente=" + componente + ", subtitulo=" + subtitulo + " ]";
	    }
    
}