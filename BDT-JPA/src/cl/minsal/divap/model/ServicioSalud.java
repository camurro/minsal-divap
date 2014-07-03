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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "servicio_salud")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ServicioSalud.findAll", query = "SELECT s FROM ServicioSalud s"),
    @NamedQuery(name = "ServicioSalud.findById", query = "SELECT s FROM ServicioSalud s WHERE s.id = :id"),
    @NamedQuery(name = "ServicioSalud.findByNombre", query = "SELECT s FROM ServicioSalud s WHERE s.nombre = :nombre")})
public class ServicioSalud implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nombre")
    private String nombre;
    @JoinColumn(name = "id_region", referencedColumnName = "id")
    @ManyToOne
    private Region idRegion;
    @OneToMany(mappedBy = "idServicioSalud")
    private Collection<MarcoPresupuestario> marcoPresupuestarioCollection;
    @OneToMany(mappedBy = "idServicioSalud")
    private Collection<ProgramaServicioCore> programaServicioCoreCollection;
    @OneToMany(mappedBy = "idServicioSalud")
    private Collection<Establecimiento> establecimientoCollection;
    @OneToMany(mappedBy = "idServicioSalud")
    private Collection<Comuna> comunaCollection;

    public ServicioSalud() {
    }

    public ServicioSalud(Integer id) {
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

    public Region getIdRegion() {
        return idRegion;
    }

    public void setIdRegion(Region idRegion) {
        this.idRegion = idRegion;
    }

    @XmlTransient
    public Collection<MarcoPresupuestario> getMarcoPresupuestarioCollection() {
        return marcoPresupuestarioCollection;
    }

    public void setMarcoPresupuestarioCollection(Collection<MarcoPresupuestario> marcoPresupuestarioCollection) {
        this.marcoPresupuestarioCollection = marcoPresupuestarioCollection;
    }

    @XmlTransient
    public Collection<ProgramaServicioCore> getProgramaServicioCoreCollection() {
        return programaServicioCoreCollection;
    }

    public void setProgramaServicioCoreCollection(Collection<ProgramaServicioCore> programaServicioCoreCollection) {
        this.programaServicioCoreCollection = programaServicioCoreCollection;
    }

    @XmlTransient
    public Collection<Establecimiento> getEstablecimientoCollection() {
        return establecimientoCollection;
    }

    public void setEstablecimientoCollection(Collection<Establecimiento> establecimientoCollection) {
        this.establecimientoCollection = establecimientoCollection;
    }

    @XmlTransient
    public Collection<Comuna> getComunaCollection() {
        return comunaCollection;
    }

    public void setComunaCollection(Collection<Comuna> comunaCollection) {
        this.comunaCollection = comunaCollection;
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
        if (!(object instanceof ServicioSalud)) {
            return false;
        }
        ServicioSalud other = (ServicioSalud) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.ServicioSalud[ id=" + id + " ]";
    }
    
}
