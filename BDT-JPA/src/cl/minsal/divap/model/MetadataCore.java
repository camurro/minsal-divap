package cl.minsal.divap.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the metadata_core database table.
 * 
 */
@Entity
@Table(name="metadata_core")
@NamedQuery(name="MetadataCore.findAll", query="SELECT m FROM MetadataCore m")
public class MetadataCore implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id", unique=true, nullable=false)
	@GeneratedValue
	private Integer id;

	private String descripcion;

	@Column(name="indice_core")
	private Integer indiceCore;

	//bi-directional many-to-one association to Programa
	@ManyToOne
	@JoinColumn(name="id_programa")
	private Programa programa;

	//bi-directional many-to-one association to TipoPrograma
	@ManyToOne
	@JoinColumn(name="id_tipo_programa")
	private TipoPrograma tipoPrograma;

	public MetadataCore() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getIndiceCore() {
		return this.indiceCore;
	}

	public void setIndiceCore(Integer indiceCore) {
		this.indiceCore = indiceCore;
	}

	public Programa getPrograma() {
		return this.programa;
	}

	public void setPrograma(Programa programa) {
		this.programa = programa;
	}

	public TipoPrograma getTipoPrograma() {
		return this.tipoPrograma;
	}

	public void setTipoPrograma(TipoPrograma tipoPrograma) {
		this.tipoPrograma = tipoPrograma;
	}

}