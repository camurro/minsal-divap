package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cmurillo
 */
@Entity
@Table(name = "flujo_caja_consolidador")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FlujoCajaConsolidador.findAll", query = "SELECT f FROM FlujoCajaConsolidador f"),
    @NamedQuery(name = "FlujoCajaConsolidador.findByIdFlujoCajaConsolidador", query = "SELECT f FROM FlujoCajaConsolidador f WHERE f.idFlujoCajaConsolidador = :idFlujoCajaConsolidador"),
    @NamedQuery(name = "FlujoCajaConsolidador.findByFechaCreacion", query = "SELECT f FROM FlujoCajaConsolidador f WHERE f.fechaCreacion = :fechaCreacion")})
public class FlujoCajaConsolidador implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
	@Column(name="id_flujo_caja_consolidador", unique=true, nullable=false)
	@GeneratedValue
    private Integer idFlujoCajaConsolidador;
    @Basic(optional = false)
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @JoinColumn(name = "usuario", referencedColumnName = "username")
    @ManyToOne(optional = false)
    private Usuario usuario;
    @JoinColumn(name = "mes", referencedColumnName = "id_mes")
    @ManyToOne(optional = false)
    private Mes mes;
    @JoinColumn(name = "ano", referencedColumnName = "ano")
    @ManyToOne(optional = false)
    private AnoEnCurso ano;

    public FlujoCajaConsolidador() {
    }

    public FlujoCajaConsolidador(Integer idFlujoCajaConsolidador) {
        this.idFlujoCajaConsolidador = idFlujoCajaConsolidador;
    }

    public FlujoCajaConsolidador(Integer idFlujoCajaConsolidador, Date fechaCreacion) {
        this.idFlujoCajaConsolidador = idFlujoCajaConsolidador;
        this.fechaCreacion = fechaCreacion;
    }

    public Integer getIdFlujoCajaConsolidador() {
        return idFlujoCajaConsolidador;
    }

    public void setIdFlujoCajaConsolidador(Integer idFlujoCajaConsolidador) {
        this.idFlujoCajaConsolidador = idFlujoCajaConsolidador;
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

    public Mes getMes() {
        return mes;
    }

    public void setMes(Mes mes) {
        this.mes = mes;
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
        hash += (idFlujoCajaConsolidador != null ? idFlujoCajaConsolidador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FlujoCajaConsolidador)) {
            return false;
        }
        FlujoCajaConsolidador other = (FlujoCajaConsolidador) object;
        if ((this.idFlujoCajaConsolidador == null && other.idFlujoCajaConsolidador != null) || (this.idFlujoCajaConsolidador != null && !this.idFlujoCajaConsolidador.equals(other.idFlujoCajaConsolidador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.FlujoCajaConsolidador[ idFlujoCajaConsolidador=" + idFlujoCajaConsolidador + " ]";
    }
    
}