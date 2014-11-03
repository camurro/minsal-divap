/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

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
    @NamedQuery(name = "ConvenioServicio.findByMonto", query = "SELECT c FROM ConvenioServicio c WHERE c.monto = :monto"),
    @NamedQuery(name = "ConvenioServicio.findByFecha", query = "SELECT c FROM ConvenioServicio c WHERE c.fecha = :fecha"),
    @NamedQuery(name = "ConvenioServicio.findByNumeroResolucion", query = "SELECT c FROM ConvenioServicio c WHERE c.numeroResolucion = :numeroResolucion"),
    @NamedQuery(name = "ConvenioServicio.findByAprobacion", query = "SELECT c FROM ConvenioServicio c WHERE c.aprobacion = :aprobacion"),
    @NamedQuery(name = "ConvenioServicio.findByIdProgramaAnoIdComponentesIdEstablecimientos", query = "SELECT c FROM ConvenioServicio c WHERE c.idPrograma.idProgramaAno = :idProgramaAno and c.componente.id IN (:idComponentes) and c.idEstablecimiento.id IN (:idEstablecimientos)"),
    @NamedQuery(name = "ConvenioServicio.findByIdProgramaAnoIdComponenteIdSubtituloIdEstablecimiento", query = "SELECT c FROM ConvenioServicio c WHERE c.idPrograma.idProgramaAno = :idProgramaAno and c.componente.id = :idComponente  and c.idTipoSubtitulo.idTipoSubtitulo = :idTipoSubtitulo and c.idEstablecimiento.id = :idEstablecimiento"),
    @NamedQuery(name = "ConvenioServicio.findByIdProgramaAnoIdEstablecimientos", query = "SELECT c FROM ConvenioServicio c WHERE c.idPrograma.idProgramaAno = :idProgramaAno and c.idEstablecimiento.id IN (:idEstablecimientos)"),
    @NamedQuery(name = "ConvenioServicio.findByIdProgramaAnoIdComponentes", query = "SELECT c FROM ConvenioServicio c WHERE c.idPrograma.idProgramaAno = :idProgramaAno and c.componente.id IN (:idComponentes)")})
public class ConvenioServicio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
	@Column(name="id_convenio_servicio", unique=true, nullable=false)
	@GeneratedValue
    private Integer idConvenioServicio;
    @Column(name = "monto")
    private Integer monto;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "numero_resolucion")
    private Integer numeroResolucion;
    @Column(name = "aprobacion")
    private Boolean aprobacion;
    @JoinColumn(name = "id_tipo_subtitulo", referencedColumnName = "id_tipo_subtitulo")
    @ManyToOne(optional = false)
    private TipoSubtitulo idTipoSubtitulo;
    @JoinColumn(name = "id_programa", referencedColumnName = "id_programa_ano")
    @ManyToOne(optional = false)
    private ProgramaAno idPrograma;
    @JoinColumn(name = "mes", referencedColumnName = "id_mes")
    @ManyToOne
    private Mes mes;
    @JoinColumn(name = "id_establecimiento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Establecimiento idEstablecimiento;
    @JoinColumn(name = "componente", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Componente componente;

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

    public Integer getMonto() {
        return monto;
    }

    public void setMonto(Integer monto) {
        this.monto = monto;
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

    public TipoSubtitulo getIdTipoSubtitulo() {
        return idTipoSubtitulo;
    }

    public void setIdTipoSubtitulo(TipoSubtitulo idTipoSubtitulo) {
        this.idTipoSubtitulo = idTipoSubtitulo;
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

    public Componente getComponente() {
        return componente;
    }

    public void setComponente(Componente componente) {
        this.componente = componente;
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
