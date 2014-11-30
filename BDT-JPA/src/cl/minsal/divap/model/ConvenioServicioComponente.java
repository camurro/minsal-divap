/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.minsal.divap.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cmurillo
 */
@Entity
@Table(name = "convenio_servicio_componente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConvenioServicioComponente.findAll", query = "SELECT c FROM ConvenioServicioComponente c"),
    @NamedQuery(name = "ConvenioServicioComponente.findByIdConvenioServicioComponente", query = "SELECT c FROM ConvenioServicioComponente c WHERE c.idConvenioServicioComponente = :idConvenioServicioComponente"),
    @NamedQuery(name = "ConvenioServicioComponente.findByMonto", query = "SELECT c FROM ConvenioServicioComponente c WHERE c.monto = :monto"),
    @NamedQuery(name = "ConvenioServicioComponente.findByIdProgramaAnoServicioComponenteSubtitulo", query = "SELECT c FROM ConvenioServicioComponente c WHERE c.convenioServicio.idPrograma.idProgramaAno = :idProgramaAno and c.subtitulo.idTipoSubtitulo = :idTipoSubtitulo"),
    @NamedQuery(name = "ConvenioServicioComponente.findByIdProgramaAnoIdComponenteIdSubtituloIdServicioIdConvenioIdEstadoConvenio", query = "SELECT c FROM ConvenioServicioComponente c WHERE c.convenioServicio.idPrograma.idProgramaAno = :idProgramaAno and c.componente.id = :idComponente and c.subtitulo.idTipoSubtitulo = :idSubtitulo and c.convenioServicio.idEstablecimiento.servicioSalud.id = :idServicio and c.convenioServicio.convenio.idConvenio = :idConvenio and c.convenioServicio.estadoConvenio.idEstadoConvenio = :idEstadoConvenio"),
    @NamedQuery(name = "ConvenioServicioComponente.findByIdProgramaAnoIdComponenteIdSubtituloIdServicioIdEstadoConvenio", query = "SELECT c FROM ConvenioServicioComponente c WHERE c.convenioServicio.idPrograma.idProgramaAno = :idProgramaAno and c.componente.id = :idComponente and c.subtitulo.idTipoSubtitulo = :idSubtitulo and c.convenioServicio.idEstablecimiento.servicioSalud.id = :idServicio and c.convenioServicio.estadoConvenio.idEstadoConvenio = :idEstadoConvenio order by c.convenioServicio.idEstablecimiento.id asc"),
    @NamedQuery(name = "ConvenioServicioComponente.findByIdProgramaAnoIdComponenteIdSubtituloIdEstablecimientoIdConvenioIdEstadoConvenio", query = "SELECT c FROM ConvenioServicioComponente c WHERE c.convenioServicio.idPrograma.idProgramaAno = :idProgramaAno and c.componente.id = :idComponente and c.subtitulo.idTipoSubtitulo = :idSubtitulo and c.convenioServicio.idEstablecimiento.id = :idEstablecimiento and c.convenioServicio.convenio.idConvenio = :idConvenio and c.convenioServicio.estadoConvenio.idEstadoConvenio = :idEstadoConvenio"),
    @NamedQuery(name = "ConvenioServicioComponente.getConveniosPagadosByProgramaAnoComponenteSubtituloEstablecimientoEstadoConvenio", query = "SELECT c FROM ConvenioServicioComponente c WHERE c.convenioServicio.idPrograma.idProgramaAno = :idProgramaAno and c.componente.id = :idComponente and c.subtitulo.idTipoSubtitulo = :idSubtitulo and c.convenioServicio.idEstablecimiento.id = :idEstablecimiento and c.convenioServicio.estadoConvenio.idEstadoConvenio = :idEstadoConvenio")})
												   
public class ConvenioServicioComponente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
   	@Column(name="id_convenio_servicio_componente", unique=true, nullable=false)
   	@GeneratedValue
    private Integer idConvenioServicioComponente;
    @Basic(optional = false)
    @Column(name = "monto")
    private int monto;
    @Basic(optional = false)
    @Column(name = "aprobado")
    private boolean aprobado;
    @JoinColumn(name = "subtitulo", referencedColumnName = "id_tipo_subtitulo")
    @ManyToOne(optional = false)
    private TipoSubtitulo subtitulo;
    @JoinColumn(name = "convenio_servicio", referencedColumnName = "id_convenio_servicio")
    @ManyToOne(optional = false)
    private ConvenioServicio convenioServicio;
    @JoinColumn(name = "componente", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Componente componente;

    public ConvenioServicioComponente() {
    }

    public ConvenioServicioComponente(Integer idConvenioServicioComponente) {
        this.idConvenioServicioComponente = idConvenioServicioComponente;
    }

    public ConvenioServicioComponente(Integer idConvenioServicioComponente, int monto) {
        this.idConvenioServicioComponente = idConvenioServicioComponente;
        this.monto = monto;
    }

    public Integer getIdConvenioServicioComponente() {
        return idConvenioServicioComponente;
    }

    public void setIdConvenioServicioComponente(Integer idConvenioServicioComponente) {
        this.idConvenioServicioComponente = idConvenioServicioComponente;
    }

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

    public TipoSubtitulo getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(TipoSubtitulo subtitulo) {
        this.subtitulo = subtitulo;
    }

    public ConvenioServicio getConvenioServicio() {
        return convenioServicio;
    }

    public void setConvenioServicio(ConvenioServicio convenioServicio) {
        this.convenioServicio = convenioServicio;
    }

    public Componente getComponente() {
        return componente;
    }

    public void setComponente(Componente componente) {
        this.componente = componente;
    }

	public boolean isAprobado() {
		return aprobado;
	}

	public void setAprobado(boolean aprobado) {
		this.aprobado = aprobado;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idConvenioServicioComponente != null ? idConvenioServicioComponente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ConvenioServicioComponente)) {
            return false;
        }
        ConvenioServicioComponente other = (ConvenioServicioComponente) object;
        if ((this.idConvenioServicioComponente == null && other.idConvenioServicioComponente != null) || (this.idConvenioServicioComponente != null && !this.idConvenioServicioComponente.equals(other.idConvenioServicioComponente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ConvenioServicioComponente[ idConvenioServicioComponente=" + idConvenioServicioComponente + " ]";
    }
    
}
