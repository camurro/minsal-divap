package cl.minsal.divap.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the tipo_programa database table.
 * 
 */
@Entity
@Table(name="tipo_programa")
@NamedQuery(name="TipoPrograma.findAll", query="SELECT t FROM TipoPrograma t")
public class TipoPrograma implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id", unique=true, nullable=false)
	@GeneratedValue
	private Integer id;

	private String nombre;

	//bi-directional many-to-one association to MetadataCore
	@OneToMany(mappedBy="tipoPrograma")
	private List<MetadataCore> metadataCores;

	//bi-directional many-to-one association to Programa
	@OneToMany(mappedBy="idTipoPrograma")
	private List<Programa> programas;

	public TipoPrograma() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<MetadataCore> getMetadataCores() {
		return this.metadataCores;
	}

	public void setMetadataCores(List<MetadataCore> metadataCores) {
		this.metadataCores = metadataCores;
	}

	public MetadataCore addMetadataCore(MetadataCore metadataCore) {
		getMetadataCores().add(metadataCore);
		metadataCore.setTipoPrograma(this);

		return metadataCore;
	}

	public MetadataCore removeMetadataCore(MetadataCore metadataCore) {
		getMetadataCores().remove(metadataCore);
		metadataCore.setTipoPrograma(null);

		return metadataCore;
	}

	public List<Programa> getProgramas() {
		return this.programas;
	}

	public void setProgramas(List<Programa> programas) {
		this.programas = programas;
	}

	public Programa addPrograma(Programa programa) {
		getProgramas().add(programa);
		programa.setIdTipoPrograma(this);

		return programa;
	}

	public Programa removePrograma(Programa programa) {
		getProgramas().remove(programa);
		programa.setIdTipoPrograma(null);

		return programa;
	}

}