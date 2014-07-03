/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author cmurillo
 */
@Entity
@Table(name = "tipo_programa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoPrograma.findAll", query = "SELECT t FROM TipoPrograma t"),
    @NamedQuery(name = "TipoPrograma.findById", query = "SELECT t FROM TipoPrograma t WHERE t.id = :id"),
    @NamedQuery(name = "TipoPrograma.findByNombre", query = "SELECT t FROM TipoPrograma t WHERE t.nombre = :nombre")})
public class TipoPrograma implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(mappedBy = "idTipoPrograma")
    private Collection<MetadataCore> metadataCoreCollection;
    @OneToMany(mappedBy = "idTipoPrograma")
    private Collection<Programa> programaCollection;

    public TipoPrograma() {
    }

    public TipoPrograma(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public Collection<MetadataCore> getMetadataCoreCollection() {
        return metadataCoreCollection;
    }

    public void setMetadataCoreCollection(Collection<MetadataCore> metadataCoreCollection) {
        this.metadataCoreCollection = metadataCoreCollection;
    }

    @XmlTransient
    public Collection<Programa> getProgramaCollection() {
        return programaCollection;
    }

    public void setProgramaCollection(Collection<Programa> programaCollection) {
        this.programaCollection = programaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoPrograma)) {
            return false;
        }
        TipoPrograma other = (TipoPrograma) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.TipoPrograma[ id=" + id + " ]";
    }
    
}
