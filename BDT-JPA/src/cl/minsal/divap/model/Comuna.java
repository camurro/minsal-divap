/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Collection;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author cmurillo
 */
@Entity
@Table(name = "comuna")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Comuna.findAll", query = "SELECT c FROM Comuna c"),
    @NamedQuery(name = "Comuna.findById", query = "SELECT c FROM Comuna c WHERE c.id = :id"),
    @NamedQuery(name = "Comuna.findByNombre", query = "SELECT c FROM Comuna c WHERE c.nombre = :nombre")})
public class Comuna implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(mappedBy = "idComuna")
    private Collection<Establecimiento> establecimientoCollection;
    @JoinColumn(name = "id_servicio_salud", referencedColumnName = "id")
    @ManyToOne
    private ServicioSalud idServicioSalud;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comuna")
    private Collection<AntecendentesComuna> antecendentesComunaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comuna")
    private Collection<ProgramaMunicipalCore> programaMunicipalCoreCollection;

    public Comuna() {
    }

    public Comuna(Integer id) {
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
    public Collection<Establecimiento> getEstablecimientoCollection() {
        return establecimientoCollection;
    }

    public void setEstablecimientoCollection(Collection<Establecimiento> establecimientoCollection) {
        this.establecimientoCollection = establecimientoCollection;
    }

    public ServicioSalud getIdServicioSalud() {
        return idServicioSalud;
    }

    public void setIdServicioSalud(ServicioSalud idServicioSalud) {
        this.idServicioSalud = idServicioSalud;
    }

    @XmlTransient
    public Collection<AntecendentesComuna> getAntecendentesComunaCollection() {
        return antecendentesComunaCollection;
    }

    public void setAntecendentesComunaCollection(Collection<AntecendentesComuna> antecendentesComunaCollection) {
        this.antecendentesComunaCollection = antecendentesComunaCollection;
    }

    @XmlTransient
    public Collection<ProgramaMunicipalCore> getProgramaMunicipalCoreCollection() {
        return programaMunicipalCoreCollection;
    }

    public void setProgramaMunicipalCoreCollection(Collection<ProgramaMunicipalCore> programaMunicipalCoreCollection) {
        this.programaMunicipalCoreCollection = programaMunicipalCoreCollection;
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
        if (!(object instanceof Comuna)) {
            return false;
        }
        Comuna other = (Comuna) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.Comuna[ id=" + id + " ]";
    }
    
}
