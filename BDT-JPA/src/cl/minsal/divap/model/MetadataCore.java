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
import javax.persistence.GenerationType;
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
@Table(name = "metadata_core")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MetadataCore.findAll", query = "SELECT m FROM MetadataCore m"),
    @NamedQuery(name = "MetadataCore.findById", query = "SELECT m FROM MetadataCore m WHERE m.id = :id"),
    @NamedQuery(name = "MetadataCore.findByIndiceCore", query = "SELECT m FROM MetadataCore m WHERE m.indiceCore = :indiceCore"),
    @NamedQuery(name = "MetadataCore.findByDescripcion", query = "SELECT m FROM MetadataCore m WHERE m.descripcion = :descripcion")})
public class MetadataCore implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "indice_core")
    private short indiceCore;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "id_tipo_programa", referencedColumnName = "id")
    @ManyToOne
    private TipoPrograma idTipoPrograma;
    @JoinColumn(name = "id_programa", referencedColumnName = "id")
    @ManyToOne
    private Programa idPrograma;

    public MetadataCore() {
    }

    public MetadataCore(Integer id) {
        this.id = id;
    }

    public MetadataCore(Integer id, short indiceCore, String descripcion) {
        this.id = id;
        this.indiceCore = indiceCore;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public short getIndiceCore() {
        return indiceCore;
    }

    public void setIndiceCore(short indiceCore) {
        this.indiceCore = indiceCore;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoPrograma getIdTipoPrograma() {
        return idTipoPrograma;
    }

    public void setIdTipoPrograma(TipoPrograma idTipoPrograma) {
        this.idTipoPrograma = idTipoPrograma;
    }

    public Programa getIdPrograma() {
        return idPrograma;
    }

    public void setIdPrograma(Programa idPrograma) {
        this.idPrograma = idPrograma;
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
        if (!(object instanceof MetadataCore)) {
            return false;
        }
        MetadataCore other = (MetadataCore) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.MetadataCore[ id=" + id + " ]";
    }
    
}
