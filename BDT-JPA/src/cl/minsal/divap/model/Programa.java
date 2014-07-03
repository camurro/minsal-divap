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
@Table(name = "programa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Programa.findAll", query = "SELECT p FROM Programa p"),
    @NamedQuery(name = "Programa.findById", query = "SELECT p FROM Programa p WHERE p.id = :id"),
    @NamedQuery(name = "Programa.findByNombre", query = "SELECT p FROM Programa p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Programa.findByCantidadCuotas", query = "SELECT p FROM Programa p WHERE p.cantidadCuotas = :cantidadCuotas")})
public class Programa implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "cantidad_cuotas")
    private short cantidadCuotas;
    @OneToMany(mappedBy = "idPrograma")
    private Collection<MarcoPresupuestario> marcoPresupuestarioCollection;
    @OneToMany(mappedBy = "idPrograma")
    private Collection<ProgramaServicioCore> programaServicioCoreCollection;
    @OneToMany(mappedBy = "idPrograma")
    private Collection<Componente> componenteCollection;
    @OneToMany(mappedBy = "idPrograma")
    private Collection<Cuota> cuotaCollection;
    @OneToMany(mappedBy = "idPrograma")
    private Collection<MetadataCore> metadataCoreCollection;
    @JoinColumn(name = "username_usuario", referencedColumnName = "username")
    @ManyToOne
    private Usuario usernameUsuario;
    @JoinColumn(name = "id_tipo_programa", referencedColumnName = "id")
    @ManyToOne
    private TipoPrograma idTipoPrograma;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "programa")
    private Collection<ProgramaMunicipalCore> programaMunicipalCoreCollection;

    public Programa() {
    }

    public Programa(Integer id) {
        this.id = id;
    }

    public Programa(Integer id, short cantidadCuotas) {
        this.id = id;
        this.cantidadCuotas = cantidadCuotas;
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

    public short getCantidadCuotas() {
        return cantidadCuotas;
    }

    public void setCantidadCuotas(short cantidadCuotas) {
        this.cantidadCuotas = cantidadCuotas;
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
    public Collection<Componente> getComponenteCollection() {
        return componenteCollection;
    }

    public void setComponenteCollection(Collection<Componente> componenteCollection) {
        this.componenteCollection = componenteCollection;
    }

    @XmlTransient
    public Collection<Cuota> getCuotaCollection() {
        return cuotaCollection;
    }

    public void setCuotaCollection(Collection<Cuota> cuotaCollection) {
        this.cuotaCollection = cuotaCollection;
    }

    @XmlTransient
    public Collection<MetadataCore> getMetadataCoreCollection() {
        return metadataCoreCollection;
    }

    public void setMetadataCoreCollection(Collection<MetadataCore> metadataCoreCollection) {
        this.metadataCoreCollection = metadataCoreCollection;
    }

    public Usuario getUsernameUsuario() {
        return usernameUsuario;
    }

    public void setUsernameUsuario(Usuario usernameUsuario) {
        this.usernameUsuario = usernameUsuario;
    }

    public TipoPrograma getIdTipoPrograma() {
        return idTipoPrograma;
    }

    public void setIdTipoPrograma(TipoPrograma idTipoPrograma) {
        this.idTipoPrograma = idTipoPrograma;
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
        if (!(object instanceof Programa)) {
            return false;
        }
        Programa other = (Programa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.Programa[ id=" + id + " ]";
    }
    
}
