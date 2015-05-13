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
@Table(name = "reliquidacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reliquidacion.findAll", query = "SELECT r FROM Reliquidacion r"),
    @NamedQuery(name = "Reliquidacion.findByIdReliquidacion", query = "SELECT r FROM Reliquidacion r WHERE r.idReliquidacion = :idReliquidacion"),
    @NamedQuery(name = "Reliquidacion.findByIdProgramaAno", query = "SELECT r FROM Reliquidacion r WHERE r.idProgramaAno.idProgramaAno = :idProgramaAno order by r.fechaCreacion desc"),
    @NamedQuery(name = "Reliquidacion.findByUsuario", query = "SELECT r FROM Reliquidacion r WHERE r.usuario = :usuario"),
    @NamedQuery(name = "Reliquidacion.findByFechaCreacion", query = "SELECT r FROM Reliquidacion r WHERE r.fechaCreacion = :fechaCreacion")})
public class Reliquidacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
   	@Column(name="id_reliquidacion", unique=true, nullable=false)
   	@GeneratedValue
    private Integer idReliquidacion;
    @JoinColumn(name = "usuario", referencedColumnName = "username")
    @ManyToOne
    private Usuario usuario;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIME)
    private Date fechaCreacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reliquidacion")
    private Set<ReliquidacionServicio> reliquidacionServicios;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reliquidacion")
    private Set<ReliquidacionComuna> reliquidacionComunas;
    @JoinColumn(name = "id_programa_ano", referencedColumnName = "id_programa_ano")
    @ManyToOne
    private ProgramaAno idProgramaAno;
    @JoinColumn(name = "mes", referencedColumnName = "id_mes")
    @ManyToOne(optional = false)
    private Mes mes;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idReliquidacion")
    private Set<DocumentoReliquidacion> documentosReliquidacion;

    public Reliquidacion() {
    }

    public Reliquidacion(Integer idReliquidacion) {
        this.idReliquidacion = idReliquidacion;
    }

    public Integer getIdReliquidacion() {
        return idReliquidacion;
    }

    public void setIdReliquidacion(Integer idReliquidacion) {
        this.idReliquidacion = idReliquidacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
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
    public Set<ReliquidacionServicio> getReliquidacionServicios() {
        return reliquidacionServicios;
    }

    public void setReliquidacionServicios(
            Set<ReliquidacionServicio> reliquidacionServicios) {
        this.reliquidacionServicios = reliquidacionServicios;
    }
   
    @XmlTransient
    public Set<ReliquidacionComuna> getReliquidacionComunas() {
        return reliquidacionComunas;
    }

    public void setReliquidacionComunas(
            Set<ReliquidacionComuna> reliquidacionComunas) {
        this.reliquidacionComunas = reliquidacionComunas;
    }
   
    @XmlTransient
    public Set<DocumentoReliquidacion> getDocumentosReliquidacion() {
        return documentosReliquidacion;
    }

    public void setDocumentosReliquidacion(
            Set<DocumentoReliquidacion> documentosReliquidacion) {
        this.documentosReliquidacion = documentosReliquidacion;
    }

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idReliquidacion != null ? idReliquidacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Reliquidacion)) {
            return false;
        }
        Reliquidacion other = (Reliquidacion) object;
        if ((this.idReliquidacion == null && other.idReliquidacion != null) || (this.idReliquidacion != null && !this.idReliquidacion.equals(other.idReliquidacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.Reliquidacion[ idReliquidacion=" + idReliquidacion + " ]";
    }
   
}
