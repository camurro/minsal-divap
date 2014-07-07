package cl.minsal.divap.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the programa_municipal_core database table.
 * 
 */
@Embeddable
public class ProgramaMunicipalCorePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="id_programa", insertable=false, updatable=false)
	private Integer idPrograma;

	@Column(name="id_comuna", insertable=false, updatable=false)
	private Integer idComuna;

	public ProgramaMunicipalCorePK() {
	}
	public Integer getIdPrograma() {
		return this.idPrograma;
	}
	public void setIdPrograma(Integer idPrograma) {
		this.idPrograma = idPrograma;
	}
	public Integer getIdComuna() {
		return this.idComuna;
	}
	public void setIdComuna(Integer idComuna) {
		this.idComuna = idComuna;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ProgramaMunicipalCorePK)) {
			return false;
		}
		ProgramaMunicipalCorePK castOther = (ProgramaMunicipalCorePK)other;
		return 
			this.idPrograma.equals(castOther.idPrograma)
			&& this.idComuna.equals(castOther.idComuna);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idPrograma.hashCode();
		hash = hash * prime + this.idComuna.hashCode();
		
		return hash;
	}
}