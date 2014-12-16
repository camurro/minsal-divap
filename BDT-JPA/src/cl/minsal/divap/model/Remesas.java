package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "remesas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Remesas.findAll", query = "SELECT r FROM Remesas r"),
    @NamedQuery(name = "Remesas.findByIdRemesa", query = "SELECT r FROM Remesas r WHERE r.idRemesa = :idRemesa"),
    @NamedQuery(name = "Remesas.findByFechaCreacion", query = "SELECT r FROM Remesas r WHERE r.fechaCreacion = :fechaCreacion")})
public class Remesas implements Serializable {
    private static final long serialVersionUID = 1L;
   
   @Id
@Column(name="id_remesa", unique=true, nullable=false)
@GeneratedValue
    private Integer idRemesa;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIME)
    private Date fechaCreacion;
    @JoinColumn(name = "usuario", referencedColumnName = "username")
    @ManyToOne
    private Usuario usuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "remesa")
    private Set<DocumentoRemesas> documentoRemesasSet;
    @JoinColumn(name = "mes", referencedColumnName = "id_mes")
    @ManyToOne(optional = false)
    private Mes mes;



    
    public Remesas() {
    }

    public Remesas(Integer idRemesa) {
        this.idRemesa = idRemesa;
    }

    public Integer getIdRemesa() {
        return idRemesa;
    }

    public void setIdRemesa(Integer idRemesa) {
        this.idRemesa = idRemesa;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRemesa != null ? idRemesa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Remesas)) {
            return false;
        }
        Remesas other = (Remesas) object;
        if ((this.idRemesa == null && other.idRemesa != null) || (this.idRemesa != null && !this.idRemesa.equals(other.idRemesa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.Remesas[ idRemesa=" + idRemesa + " ]";
    }
    @XmlTransient
    public Set<DocumentoRemesas> getDocumentoRemesasSet() {
        return documentoRemesasSet;
    }

    public void setDocumentoRemesasSet(Set<DocumentoRemesas> documentoRemesasSet) {
        this.documentoRemesasSet = documentoRemesasSet;
    }
  
}