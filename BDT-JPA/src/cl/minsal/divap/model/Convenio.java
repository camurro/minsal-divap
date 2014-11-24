/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

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
@Table(name = "convenio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Convenio.findAll", query = "SELECT c FROM Convenio c"),
    @NamedQuery(name = "Convenio.findByIdConvenio", query = "SELECT c FROM Convenio c WHERE c.idConvenio = :idConvenio"),
    @NamedQuery(name = "Convenio.findByFechaCreacion", query = "SELECT c FROM Convenio c WHERE c.fechaCreacion = :fechaCreacion")})
public class Convenio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
  	@Column(name="id_convenio", unique=true, nullable=false)
  	@GeneratedValue
    private Integer idConvenio;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIME)
    private Date fechaCreacion;
    @OneToMany(mappedBy = "convenio")
    private Set<ConvenioComuna> conveniosComuna;
    @JoinColumn(name = "usuario", referencedColumnName = "username")
    @ManyToOne
    private Usuario usuario;
    @JoinColumn(name = "id_programa_ano", referencedColumnName = "id_programa_ano")
    @ManyToOne
    private ProgramaAno idProgramaAno;
    @JoinColumn(name = "mes", referencedColumnName = "id_mes")
    @ManyToOne(optional = false)
    private Mes mes;
    @OneToMany(mappedBy = "convenio")
    private Set<ConvenioServicio> conveniosServicio;

    public Convenio() {
    }

    public Convenio(Integer idConvenio) {
        this.idConvenio = idConvenio;
    }

    public Integer getIdConvenio() {
        return idConvenio;
    }

    public void setIdConvenio(Integer idConvenio) {
        this.idConvenio = idConvenio;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @XmlTransient
    public Set<ConvenioComuna> getConveniosComuna() {
		return conveniosComuna;
	}

	public void setConveniosComuna(Set<ConvenioComuna> conveniosComuna) {
		this.conveniosComuna = conveniosComuna;
	}

	public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public ProgramaAno getIdProgramaAno() {
        return idProgramaAno;
    }

    public void setIdProgramaAno(ProgramaAno idProgramaAno) {
        this.idProgramaAno = idProgramaAno;
    }

    public Mes getMes() {
        return mes;
    }

    public void setMes(Mes mes) {
        this.mes = mes;
    }
    
    @XmlTransient
    public Set<ConvenioServicio> getConveniosServicio() {
		return conveniosServicio;
	}

	public void setConveniosServicio(Set<ConvenioServicio> conveniosServicio) {
		this.conveniosServicio = conveniosServicio;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idConvenio != null ? idConvenio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Convenio)) {
            return false;
        }
        Convenio other = (Convenio) object;
        if ((this.idConvenio == null && other.idConvenio != null) || (this.idConvenio != null && !this.idConvenio.equals(other.idConvenio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.Convenio[ idConvenio=" + idConvenio + " ]";
    }
    
}
