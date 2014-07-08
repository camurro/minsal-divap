package cl.minsal.divap.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the antecendentes_comuna database table.
 * 
 */
@Embeddable
public class AntecendentesComunaPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ano_ano_en_curso", insertable=false, updatable=false)
	private Integer anoAnoEnCurso;

	@Column(name="id_comuna", insertable=false, updatable=false)
	private Integer idComuna;

	public AntecendentesComunaPK() {
	}
	public Integer getAnoAnoEnCurso() {
		return this.anoAnoEnCurso;
	}
	public void setAnoAnoEnCurso(Integer anoAnoEnCurso) {
		this.anoAnoEnCurso = anoAnoEnCurso;
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
		if (!(other instanceof AntecendentesComunaPK)) {
			return false;
		}
		AntecendentesComunaPK castOther = (AntecendentesComunaPK)other;
		return 
			this.anoAnoEnCurso.equals(castOther.anoAnoEnCurso)
			&& this.idComuna.equals(castOther.idComuna);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.anoAnoEnCurso.hashCode();
		hash = hash * prime + this.idComuna.hashCode();
		
		return hash;
	}
}