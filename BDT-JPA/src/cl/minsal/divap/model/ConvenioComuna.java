/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Felipe
 */
@Entity
@Table(name = "convenio_comuna")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "ConvenioComuna.findAll", query = "SELECT c FROM ConvenioComuna c"),
	@NamedQuery(name = "ConvenioComuna.findByIdConvenio", query = "SELECT c FROM ConvenioComuna c WHERE c.idConvenioComuna = :idConvenio"),
	@NamedQuery(name = "ConvenioComuna.findByFecha", query = "SELECT c FROM ConvenioComuna c WHERE c.fecha = :fecha"),
	@NamedQuery(name = "ConvenioComuna.findByIdProgramaAnoIdServicioIdComponenteIdSubtitulo", query = "SELECT c FROM ConvenioComuna c JOIN c.convenioComunaComponentes cc WHERE c.idPrograma.idProgramaAno = :idProgramaAno and c.idComuna.servicioSalud.id =:idServicio and cc.componente.id IN (:idComponentes) and cc.subtitulo.idTipoSubtitulo = :idTipoSubtitulo"),
	@NamedQuery(name = "ConvenioComuna.findByConveniosById", query = "SELECT c FROM ConvenioComuna c WHERE c.idConvenioComuna = :id"),
	@NamedQuery(name = "ConvenioComuna.findByNumeroResolucion", query = "SELECT c FROM ConvenioComuna c WHERE c.numeroResolucion = :numeroResolucion"),
	@NamedQuery(name = "ConvenioComuna.findByIdProgramaAnoIdComponentesIdComunas", query = "SELECT c FROM ConvenioComuna c JOIN c.convenioComunaComponentes cc WHERE c.idPrograma.idProgramaAno = :idProgramaAno and cc.componente.id IN (:idComponentes) and c.idComuna.id IN (:idComunas)"),
	@NamedQuery(name = "ConvenioComuna.findByIdProgramaAnoIdComponenteIdSubtituloIdComuna", query = "SELECT c FROM ConvenioComuna c JOIN c.convenioComunaComponentes cc WHERE c.idPrograma.idProgramaAno = :idProgramaAno and cc.componente.id = :idComponente  and cc.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and c.idComuna.id = :idComuna"),
	@NamedQuery(name = "ConvenioComuna.findByIdProgramaAnoIdComunas", query = "SELECT c FROM ConvenioComuna c WHERE c.idPrograma.idProgramaAno = :idProgramaAno and c.idComuna.id IN (:idComunas)"),
	@NamedQuery(name = "ConvenioComuna.findByIdProgramaAnoIdComponentes", query = "SELECT c FROM ConvenioComuna c JOIN c.convenioComunaComponentes cc WHERE c.idPrograma.idProgramaAno = :idProgramaAno and cc.componente.id IN (:idComponentes)"),
	@NamedQuery(name = "ConvenioComuna.findByIdProgramaAnoIdComponenteIdComuna", query = "SELECT c FROM ConvenioComuna c JOIN c.convenioComunaComponentes cc WHERE c.idPrograma.idProgramaAno = :idProgramaAno and cc.componente.id = :idComponente and c.idComuna.id = :idComuna"),
	@NamedQuery(name = "ConvenioComuna.findByIdProgramaAnoIdServicio", query = "SELECT c FROM ConvenioComuna c WHERE c.idPrograma.idProgramaAno = :idProgramaAno and c.idComuna.servicioSalud.id = :idServicio"),
	@NamedQuery(name = "ConvenioComuna.findByIdProgramaAnoIdServicioIdComponentes", query = "SELECT c FROM ConvenioComuna c JOIN c.convenioComunaComponentes cc WHERE c.idPrograma.idProgramaAno = :idProgramaAno and c.idComuna.servicioSalud.id = :idServicio and cc.componente.id IN (:idComponentes)"),
	@NamedQuery(name = "ConvenioComuna.findByIdProgramaAnoIdComunaIdMes", query = "SELECT c FROM ConvenioComuna c WHERE c.idPrograma.idProgramaAno = :idProgramaAno and c.idComuna.id = :idComuna and c.mes.idMes = :idMes"),
	@NamedQuery(name = "ConvenioComuna.findByIdProgramaAnoIdServicioAprobacion", query = "SELECT c FROM ConvenioComuna c WHERE c.idPrograma.idProgramaAno = :idProgramaAno and c.idComuna.servicioSalud.id = :idServicio and c.aprobacion = :aprobacion")})

public class ConvenioComuna implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="id_convenio_comuna", unique=true, nullable=false)
	@GeneratedValue
	private Integer idConvenioComuna;
	@Column(name = "fecha")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha;
	@Column(name = "numero_resolucion")
	private Integer numeroResolucion;
	@Column(name = "aprobacion")
	private Boolean aprobacion;
	@JoinColumn(name = "id_programa", referencedColumnName = "id_programa_ano")
	@ManyToOne
	private ProgramaAno idPrograma;
	@JoinColumn(name = "mes", referencedColumnName = "id_mes")
	@ManyToOne
	private Mes mes;
	@JoinColumn(name = "id_comuna", referencedColumnName = "id")
	@ManyToOne
	private Comuna idComuna;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "convenio")
	private Set<DocumentoConvenio> documentosConvenio;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "convenioComuna")
	private Set<ConvenioComunaComponente> convenioComunaComponentes;

	public ConvenioComuna() {
	}

	public ConvenioComuna(Integer idConvenioComuna) {
		this.idConvenioComuna = idConvenioComuna;
	}

	public Integer getIdConvenioComuna() {
		return idConvenioComuna;
	}

	public void setIdConvenioComuna(Integer idConvenioComuna) {
		this.idConvenioComuna = idConvenioComuna;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Integer getNumeroResolucion() {
		return numeroResolucion;
	}

	public void setNumeroResolucion(Integer numeroResolucion) {
		this.numeroResolucion = numeroResolucion;
	}

	public ProgramaAno getIdPrograma() {
		return idPrograma;
	}

	public void setIdPrograma(ProgramaAno idPrograma) {
		this.idPrograma = idPrograma;
	}

	public Comuna getIdComuna() {
		return idComuna;
	}

	public void setIdComuna(Comuna idComuna) {
		this.idComuna = idComuna;
	}

	@XmlTransient
	public Set<ConvenioComunaComponente> getConvenioComunaComponentes() {
		return convenioComunaComponentes;
	}

	public void setConvenioComunaComponentes(
			Set<ConvenioComunaComponente> convenioComunaComponentes) {
		this.convenioComunaComponentes = convenioComunaComponentes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((idConvenioComuna == null) ? 0 : idConvenioComuna.hashCode());
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
		ConvenioComuna other = (ConvenioComuna) obj;
		if (idConvenioComuna == null) {
			if (other.idConvenioComuna != null)
				return false;
		} else if (!idConvenioComuna.equals(other.idConvenioComuna))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ConvenioComuna [idConvenioComuna=" + idConvenioComuna
				+ ", fecha=" + fecha + ", numeroResolucion=" + numeroResolucion
				+ ", aprobacion=" + aprobacion + ", idPrograma=" + idPrograma
				+ ", mes=" + mes + ", idComuna=" + idComuna
				+ ", documentosConvenio=" + documentosConvenio + "]";
	}

	@XmlTransient
	public Set<DocumentoConvenio> getDocumentosConvenio() {
		return documentosConvenio;
	}

	public void setDocumentoConvenios(Set<DocumentoConvenio> documentosConvenio) {
		this.documentosConvenio = documentosConvenio;
	}

	public Boolean getAprobacion() {
		return aprobacion;
	}

	public void setAprobacion(Boolean aprobacion) {
		this.aprobacion = aprobacion;
	}
}