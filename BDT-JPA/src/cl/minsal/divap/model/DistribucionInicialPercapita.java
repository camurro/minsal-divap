package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cmurillo
 */
@Entity
@Table(name = "distribucion_inicial_percapita")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "DistribucionInicialPercapita.findAll", query = "SELECT d FROM DistribucionInicialPercapita d"),
	@NamedQuery(name = "DistribucionInicialPercapita.findLast", query = "SELECT d FROM DistribucionInicialPercapita d where d.ano.ano = :idAno and d.fechaCreacion is not null order by d.fechaCreacion DESC"),
	@NamedQuery(name = "DistribucionInicialPercapita.findByIdDistribucionInicialPercapita", query = "SELECT d FROM DistribucionInicialPercapita d WHERE d.idDistribucionInicialPercapita = :idDistribucionInicialPercapita"),
	@NamedQuery(name = "DistribucionInicialPercapita.findByFechaCreacion", query = "SELECT d FROM DistribucionInicialPercapita d WHERE d.fechaCreacion = :fechaCreacion")})
public class DistribucionInicialPercapita implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="id_distribucion_inicial_percapita", unique=true, nullable=false)
	@GeneratedValue
	private Integer idDistribucionInicialPercapita;
	@Column(name = "fecha_creacion")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaCreacion;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idDistribucionInicialPercapita")
    private Set<DocumentoDistribucionInicialPercapita> documentoDistribucionInicialPercapitaCollection;
	@JoinColumn(name = "usuario", referencedColumnName = "username")
	@ManyToOne
	private Usuario usuario;
	@JoinColumn(name = "ano", referencedColumnName = "ano")
    @ManyToOne(optional = false)
    private AnoEnCurso ano;
	@OneToMany(mappedBy = "distribucionInicialPercapita")
    private Set<AntecendentesComunaCalculado> antecendentesComunaCalculadoCollection;

	public DistribucionInicialPercapita() {
	}

	public DistribucionInicialPercapita(Integer idDistribucionInicialPercapita) {
		this.idDistribucionInicialPercapita = idDistribucionInicialPercapita;
	}

	public Integer getIdDistribucionInicialPercapita() {
		return idDistribucionInicialPercapita;
	}

	public void setIdDistribucionInicialPercapita(Integer idDistribucionInicialPercapita) {
		this.idDistribucionInicialPercapita = idDistribucionInicialPercapita;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public Set<DocumentoDistribucionInicialPercapita> getDocumentoDistribucionInicialPercapitaCollection() {
		return documentoDistribucionInicialPercapitaCollection;
	}

	public void setDocumentoDistribucionInicialPercapitaCollection(
			Set<DocumentoDistribucionInicialPercapita> documentoDistribucionInicialPercapitaCollection) {
		this.documentoDistribucionInicialPercapitaCollection = documentoDistribucionInicialPercapitaCollection;
	}

	public Set<AntecendentesComunaCalculado> getAntecendentesComunaCalculadoCollection() {
		return antecendentesComunaCalculadoCollection;
	}

	public void setAntecendentesComunaCalculadoCollection(
			Set<AntecendentesComunaCalculado> antecendentesComunaCalculadoCollection) {
		this.antecendentesComunaCalculadoCollection = antecendentesComunaCalculadoCollection;
	}

	public AnoEnCurso getAno() {
		return ano;
	}

	public void setAno(AnoEnCurso ano) {
		this.ano = ano;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (idDistribucionInicialPercapita != null ? idDistribucionInicialPercapita.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof DistribucionInicialPercapita)) {
			return false;
		}
		DistribucionInicialPercapita other = (DistribucionInicialPercapita) object;
		if ((this.idDistribucionInicialPercapita == null && other.idDistribucionInicialPercapita != null) || (this.idDistribucionInicialPercapita != null && !this.idDistribucionInicialPercapita.equals(other.idDistribucionInicialPercapita))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "cl.minsal.divap.model.DistribucionInicialPercapita[ idDistribucionInicialPercapita=" + idDistribucionInicialPercapita + " ]";
	}

}