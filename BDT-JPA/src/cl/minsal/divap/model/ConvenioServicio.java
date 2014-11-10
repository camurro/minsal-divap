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
 * @author cmurillo
 */
@Entity
@Table(name = "convenio_servicio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConvenioServicio.findAll", query = "SELECT c FROM ConvenioServicio c"),
    @NamedQuery(name = "ConvenioServicio.findByIdConvenioServicio", query = "SELECT c FROM ConvenioServicio c WHERE c.idConvenioServicio = :idConvenioServicio"),
    @NamedQuery(name = "ConvenioServicio.findByFecha", query = "SELECT c FROM ConvenioServicio c WHERE c.fecha = :fecha"),
    @NamedQuery(name = "ConvenioServicio.findByNumeroResolucion", query = "SELECT c FROM ConvenioServicio c WHERE c.numeroResolucion = :numeroResolucion"),
    @NamedQuery(name = "ConvenioServicio.findByAprobacion", query = "SELECT c FROM ConvenioServicio c WHERE c.aprobacion = :aprobacion"),
    @NamedQuery(name = "ConvenioServicio.findByIdProgramaAnoIdComponentesIdEstablecimientos", query = "SELECT c FROM ConvenioServicio c JOIN c.convenioServicioComponentes cc WHERE c.idPrograma.idProgramaAno = :idProgramaAno and cc.componente.id IN (:idComponentes) and c.idEstablecimiento.id IN (:idEstablecimientos)"),
    @NamedQuery(name = "ConvenioServicio.findByIdProgramaAnoIdComponenteIdSubtituloIdEstablecimiento", query = "SELECT c FROM ConvenioServicio c JOIN c.convenioServicioComponentes cc WHERE c.idPrograma.idProgramaAno = :idProgramaAno and cc.componente.id = :idComponente  and cc.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and c.idEstablecimiento.id = :idEstablecimiento"),
    @NamedQuery(name = "ConvenioServicio.findByIdProgramaAnoIdEstablecimientos", query = "SELECT c FROM ConvenioServicio c WHERE c.idPrograma.idProgramaAno = :idProgramaAno and c.idEstablecimiento.id IN (:idEstablecimientos)"),
    @NamedQuery(name = "ConvenioServicio.findByIdProgramaAnoIdComponentes", query = "SELECT c FROM ConvenioServicio c JOIN c.convenioServicioComponentes cc WHERE c.idPrograma.idProgramaAno = :idProgramaAno and cc.componente.id IN (:idComponentes)"),
    @NamedQuery(name = "ConvenioServicio.findByIdProgramaAnoIdServicio", query = "SELECT c FROM ConvenioServicio c WHERE c.idPrograma.idProgramaAno = :idProgramaAno and c.idEstablecimiento.servicioSalud.id = :idServicio"),
    @NamedQuery(name = "ConvenioServicio.findByIdProgramaAnoIdServicioIdComponentes", query = "SELECT c FROM ConvenioServicio c JOIN c.convenioServicioComponentes cc WHERE c.idPrograma.idProgramaAno = :idProgramaAno and c.idEstablecimiento.servicioSalud.id = :idServicio and cc.componente.id IN (:idComponentes)")})
public class ConvenioServicio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
	@Column(name="id_convenio_servicio", unique=true, nullable=false)
	@GeneratedValue
    private Integer idConvenioServicio;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "numero_resolucion")
    private Integer numeroResolucion;
    @Column(name = "aprobacion")
    private Boolean aprobacion;
    @JoinColumn(name = "id_programa", referencedColumnName = "id_programa_ano")
    @ManyToOne(optional = false)
    private ProgramaAno idPrograma;
    @JoinColumn(name = "mes", referencedColumnName = "id_mes")
    @ManyToOne
    private Mes mes;
    @JoinColumn(name = "id_establecimiento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Establecimiento idEstablecimiento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "convenioServicio")
    private Set<ConvenioServicioComponente> convenioServicioComponentes;
    public ConvenioServicio() {
    }

    public ConvenioServicio(Integer idConvenioServicio) {
        this.idConvenioServicio = idConvenioServicio;
    }

    public Integer getIdConvenioServicio() {
        return idConvenioServicio;
    }

    public void setIdConvenioServicio(Integer idConvenioServicio) {
        this.idConvenioServicio = idConvenioServicio;
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

    public Boolean getAprobacion() {
        return aprobacion;
    }

    public void setAprobacion(Boolean aprobacion) {
        this.aprobacion = aprobacion;
    }

    public ProgramaAno getIdPrograma() {
        return idPrograma;
    }

    public void setIdPrograma(ProgramaAno idPrograma) {
        this.idPrograma = idPrograma;
    }

    public Mes getMes() {
        return mes;
    }

    public void setMes(Mes mes) {
        this.mes = mes;
    }

    public Establecimiento getIdEstablecimiento() {
        return idEstablecimiento;
    }

    public void setIdEstablecimiento(Establecimiento idEstablecimiento) {
        this.idEstablecimiento = idEstablecimiento;
    }
    
    @XmlTransient
    public Set<ConvenioServicioComponente> getConvenioServicioComponentes() {
		return convenioServicioComponentes;
	}

	public void setConvenioServicioComponentes(
			Set<ConvenioServicioComponente> convenioServicioComponentes) {
		this.convenioServicioComponentes = convenioServicioComponentes;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idConvenioServicio != null ? idConvenioServicio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ConvenioServicio)) {
            return false;
        }
        ConvenioServicio other = (ConvenioServicio) object;
        if ((this.idConvenioServicio == null && other.idConvenioServicio != null) || (this.idConvenioServicio != null && !this.idConvenioServicio.equals(other.idConvenioServicio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ConvenioServicio[ idConvenioServicio=" + idConvenioServicio + " ]";
    }
    
}
