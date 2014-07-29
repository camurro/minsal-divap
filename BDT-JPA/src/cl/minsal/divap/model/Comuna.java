/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.minsal.divap.model;

import java.io.Serializable;

import java.util.List;
import java.util.Set;
 
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
@NamedQueries({
    @NamedQuery(name = "Comuna.findAll", query = "SELECT c FROM Comuna c"),
    @NamedQuery(name = "Comuna.findById", query = "SELECT c FROM Comuna c WHERE c.id = :id"),
    @NamedQuery(name = "Comuna.findByNombre", query = "SELECT c FROM Comuna c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "Comuna.findByServicio", query = "SELECT c FROM Comuna c WHERE c.idServicioSalud.id = :idServicio order by c.id asc"),
    @NamedQuery(name = "Comuna.findRebajasByComunas", query = "SELECT distinct(c) FROM Comuna c JOIN c.comunaCumplimientoCollection d WHERE c.id IN (:listaId) and d.idMes.idMes= :idMes order by c.id asc"),
    @NamedQuery(name = "Comuna.findByComunaServicioAno", query = "SELECT c FROM Comuna c JOIN c.antecendentesComunas a WHERE c.nombre = :comuna and c.servicioSalud.nombre = :servicio and a.anoAnoEnCurso.ano = :anoCurso")})
public class Comuna implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id", unique=true, nullable=false)
	@GeneratedValue
	private Integer id;

	private String nombre;

	//bi-directional many-to-one association to AntecendentesComuna
	@OneToMany(mappedBy="idComuna")
	private Set<AntecendentesComuna> antecendentesComunas;

	//bi-directional many-to-one association to ServicioSalud
	@ManyToOne
	@JoinColumn(name="id_servicio_salud")
	private ServicioSalud servicioSalud;

	//bi-directional many-to-one association to Establecimiento
	@OneToMany(mappedBy="comuna")
	private List<Establecimiento> establecimientos;

	//bi-directional many-to-one association to ProgramaMunicipalCore
	@OneToMany(mappedBy="comuna")
	private List<ProgramaMunicipalCore> programaMunicipalCores;
	@OneToMany(mappedBy = "idComuna")
   	 private Collection<ComunaCumplimiento> comunaCumplimientoCollection;

	public Comuna() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Set<AntecendentesComuna> getAntecendentesComunas() {
		return this.antecendentesComunas;
	}

	public void setAntecendentesComunas(Set<AntecendentesComuna> antecendentesComunas) {
		this.antecendentesComunas = antecendentesComunas;
	}

	public ServicioSalud getServicioSalud() {
		return this.servicioSalud;
	}

	public void setServicioSalud(ServicioSalud servicioSalud) {
		this.servicioSalud = servicioSalud;
	}

	public List<Establecimiento> getEstablecimientos() {
		return this.establecimientos;
	}

	public void setEstablecimientos(List<Establecimiento> establecimientos) {
		this.establecimientos = establecimientos;
	}

	public Establecimiento addEstablecimiento(Establecimiento establecimiento) {
		getEstablecimientos().add(establecimiento);
		establecimiento.setComuna(this);

		return establecimiento;
	}

	public Establecimiento removeEstablecimiento(Establecimiento establecimiento) {
		getEstablecimientos().remove(establecimiento);
		establecimiento.setComuna(null);

		return establecimiento;
	}

	public List<ProgramaMunicipalCore> getProgramaMunicipalCores() {
		return this.programaMunicipalCores;
	}

	public void setProgramaMunicipalCores(List<ProgramaMunicipalCore> programaMunicipalCores) {
		this.programaMunicipalCores = programaMunicipalCores;
	}

	public ProgramaMunicipalCore addProgramaMunicipalCore(ProgramaMunicipalCore programaMunicipalCore) {
		getProgramaMunicipalCores().add(programaMunicipalCore);
		programaMunicipalCore.setComuna(this);

		return programaMunicipalCore;
	}

	public ProgramaMunicipalCore removeProgramaMunicipalCore(ProgramaMunicipalCore programaMunicipalCore) {
		getProgramaMunicipalCores().remove(programaMunicipalCore);
		programaMunicipalCore.setComuna(null);

		return programaMunicipalCore;
	}
 @XmlTransient
    public Collection<ComunaCumplimiento> getComunaCumplimientoCollection() {
        return comunaCumplimientoCollection;
    }

    public void setComunaCumplimientoCollection(Collection<ComunaCumplimiento> comunaCumplimientoCollection) {
        this.comunaCumplimientoCollection = comunaCumplimientoCollection;
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

