package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author cmurillo
 */
@Entity
@Table(name = "tipo_subtitulo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoSubtitulo.findAll", query = "SELECT t FROM TipoSubtitulo t"),
    @NamedQuery(name = "TipoSubtitulo.findById", query = "SELECT t FROM TipoSubtitulo t WHERE t.idTipoSubtitulo = :idTipoSubtitulo"),
    @NamedQuery(name = "TipoSubtitulo.findByTipoSubtituloByDependencia", query = "SELECT t FROM TipoSubtitulo t where t.dependencia.idDependenciaPrograma=:idDependenciaPrograma"),
    @NamedQuery(name = "TipoSubtitulo.findByIdTipoSubtitulo", query = "SELECT t FROM TipoSubtitulo t WHERE t.idTipoSubtitulo = :idTipoSubtitulo"),
    @NamedQuery(name = "TipoSubtitulo.findByNombreSubtitulo", query = "SELECT t FROM TipoSubtitulo t WHERE LOWER(t.nombreSubtitulo) = :nombreSubtitulo")})
public class TipoSubtitulo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
	@Column(name="id_tipo_subtitulo", unique=true, nullable=false)
	@GeneratedValue
    private Integer idTipoSubtitulo;
    @Basic(optional = false)
    @Column(name = "nombre_subtitulo")
    private String nombreSubtitulo;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subtitulo")
    private Set<ComponenteSubtitulo> componenteSubtitulos;
    @OneToMany(mappedBy = "subtitulo")
    private List<ProgramaServicioCoreComponente> programaServicioCoreComponentes;
    @JoinColumn(name = "dependencia", referencedColumnName = "id_dependencia_programa")
    @ManyToOne(optional = false)
    private Dependencia dependencia;
    @Basic(optional = false)
    @Column(name = "inflactor")
    private Double inflactor;
    @OneToMany(mappedBy = "subtitulo")
    private Set<ProgramaMunicipalCoreComponente> programaMunicipalCoreComponentes;
    
    @OneToMany(mappedBy = "tipoSubtitulo")
    private Collection<Remesa> remesaCollection;
    @OneToMany(mappedBy = "subtitulo")
    private Set<DetalleRemesas> detalleRemesasSet;


    public TipoSubtitulo() {
    }

    public TipoSubtitulo(Integer idTipoSubtitulo) {
        this.idTipoSubtitulo = idTipoSubtitulo;
    }

    public TipoSubtitulo(Integer idTipoSubtitulo, String nombreSubtitulo) {
        this.idTipoSubtitulo = idTipoSubtitulo;
        this.nombreSubtitulo = nombreSubtitulo;
    }
    
    @XmlTransient
    public Collection<Remesa> getRemesaCollection() {
        return remesaCollection;
    }

    public void setRemesaCollection(Collection<Remesa> remesaCollection) {
        this.remesaCollection = remesaCollection;
    }

    public Integer getIdTipoSubtitulo() {
        return idTipoSubtitulo;
    }

    public void setIdTipoSubtitulo(Integer idTipoSubtitulo) {
        this.idTipoSubtitulo = idTipoSubtitulo;
    }

    public String getNombreSubtitulo() {
        return nombreSubtitulo;
    }

    public void setNombreSubtitulo(String nombreSubtitulo) {
        this.nombreSubtitulo = nombreSubtitulo;
    }

    @XmlTransient
    public Set<ComponenteSubtitulo> getComponenteSubtitulos() {
		return componenteSubtitulos;
	}

	public void setComponenteSubtitulos(
			Set<ComponenteSubtitulo> componenteSubtitulos) {
		this.componenteSubtitulos = componenteSubtitulos;
	}
	
	@XmlTransient
	public List<ProgramaServicioCoreComponente> getProgramaServicioCoreComponentes() {
		return programaServicioCoreComponentes;
	}

	public void setProgramaServicioCoreComponentes(
			List<ProgramaServicioCoreComponente> programaServicioCoreComponentes) {
		this.programaServicioCoreComponentes = programaServicioCoreComponentes;
	}

	public Dependencia getDependencia() {
		return dependencia;
	}

	public void setDependencia(Dependencia dependencia) {
		this.dependencia = dependencia;
	}
	
	@XmlTransient
	public Set<ProgramaMunicipalCoreComponente> getProgramaMunicipalCoreComponentes() {
		return programaMunicipalCoreComponentes;
	}

	public void setProgramaMunicipalCoreComponentes(
			Set<ProgramaMunicipalCoreComponente> programaMunicipalCoreComponentes) {
		this.programaMunicipalCoreComponentes = programaMunicipalCoreComponentes;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoSubtitulo != null ? idTipoSubtitulo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TipoSubtitulo)) {
            return false;
        }
        TipoSubtitulo other = (TipoSubtitulo) object;
        if ((this.idTipoSubtitulo == null && other.idTipoSubtitulo != null) || (this.idTipoSubtitulo != null && !this.idTipoSubtitulo.equals(other.idTipoSubtitulo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.TipoSubtitulo[ idTipoSubtitulo=" + idTipoSubtitulo + " ]";
    }

	public Double getInflactor() {
		return inflactor;
	}

	public void setInflactor(Double inflactor) {
		this.inflactor = inflactor;
	}
    
	 @XmlTransient
	    public Set<DetalleRemesas> getDetalleRemesasSet() {
	        return detalleRemesasSet;
	    }

	    public void setDetalleRemesasSet(Set<DetalleRemesas> detalleRemesasSet) {
	        this.detalleRemesasSet = detalleRemesasSet;
	    }
    
}