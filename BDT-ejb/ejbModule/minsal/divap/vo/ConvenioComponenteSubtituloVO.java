package minsal.divap.vo;

import java.io.Serializable;

public class ConvenioComponenteSubtituloVO implements Serializable {
 
    private static final long serialVersionUID = 1L;
    private Integer id;
    private ComponentesVO componente;
    private SubtituloVO subtitulo;
    private Integer monto;
    
    public ConvenioComponenteSubtituloVO() {
    	componente = new ComponentesVO();
        this.subtitulo = new SubtituloVO();
        this.monto = 0;
    }  
 
    public ConvenioComponenteSubtituloVO(ComponentesVO componente, SubtituloVO subtitulo, Integer monto) {
        this.componente = componente;
        this.subtitulo = subtitulo;
        this.monto = monto;
    }

	public ComponentesVO getComponente() {
		return componente;
	}

	public void setComponente(ComponentesVO componente) {
		this.componente = componente;
	}

	public SubtituloVO getSubtitulo() {
		return subtitulo;
	}

	public void setSubtitulo(SubtituloVO subtitulo) {
		this.subtitulo = subtitulo;
	}

	public Integer getMonto() {
		return monto;
	}

	public void setMonto(Integer monto) {
		this.monto = monto;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((componente == null) ? 0 : componente.hashCode());
		result = prime * result
				+ ((subtitulo == null) ? 0 : subtitulo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConvenioComponenteSubtituloVO other = (ConvenioComponenteSubtituloVO) obj;
		if (componente == null) {
			if (other.componente != null)
				return false;
		} else if (!componente.equals(other.componente))
			return false;
		if (subtitulo == null) {
			if (other.subtitulo != null)
				return false;
		} else if (!subtitulo.equals(other.subtitulo))
			return false;
		return true;
	}
 
      
}
