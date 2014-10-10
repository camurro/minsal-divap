/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Felipe
 */
@Entity
@Table(name = "convenio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Convenio.findAll", query = "SELECT c FROM Convenio c"),
    @NamedQuery(name = "Convenio.findByIDSubComponenteComunaProgramaAnoAprobacion", query = "SELECT c FROM Convenio c WHERE c.idTipoSubtitulo.idTipoSubtitulo=:sub and c.componente.id=:idComponente and c.idComuna.id=:idComuna and c.idPrograma.programa.id=:idPrograma and c.idPrograma.ano.ano=:ano and c.idEstablecimiento.id=:idEstablecimiento and c.aprobacion=:apro ORDER BY  c.monto DESC"),
    @NamedQuery(name = "Convenio.findByIdConvenio", query = "SELECT c FROM Convenio c WHERE c.idConvenio = :idConvenio"),
    @NamedQuery(name = "Convenio.findByMonto", query = "SELECT c FROM Convenio c WHERE c.monto = :monto"),
    @NamedQuery(name = "Convenio.findByFecha", query = "SELECT c FROM Convenio c WHERE c.fecha = :fecha"),
    @NamedQuery(name = "Convenio.findByIDSubComponenteComunaProgramaAno", query = "SELECT c FROM Convenio c WHERE c.idTipoSubtitulo.idTipoSubtitulo=:sub and c.componente.id=:idComponente and c.idComuna.id=:idComuna and c.idPrograma.programa.id=:idPrograma and c.idPrograma.ano.ano=:ano and c.idEstablecimiento.id=:idEstablecimiento ORDER BY  c.monto DESC"),
    @NamedQuery(name = "Convenio.findByIdProgramaAnoIdServicioIdComponenteIdSubtitulo", query = "SELECT c FROM Convenio c WHERE c.idPrograma.idProgramaAno = :idProgramaAno and c.idComuna.servicioSalud.id =:idServicio and c.componente.id = :idComponente and c.idTipoSubtitulo.idTipoSubtitulo = :idTipoSubtitulo"),
    @NamedQuery(name = "Convenio.findByConveniosById", query = "SELECT c FROM Convenio c WHERE c.idConvenio = :id"),
    @NamedQuery(name = "Convenio.findByComunaEstablecimientoProgramaComponenteMuniORDERMontoDesc", query = "SELECT  c FROM  Convenio c  INNER JOIN c.idPrograma.programa.componentes c2   WHERE    c.idComuna.id = :idComuna AND  c.idEstablecimiento.id = :idEstablecimiento  AND  c.idPrograma.programa.id = :idPrograma   AND c2.id = :idComponente and (c.idTipoSubtitulo.dependencia.idDependenciaPrograma=:muni1 or c.idTipoSubtitulo.dependencia.idDependenciaPrograma=:muni3) ORDER BY  c.monto DESC"),
    @NamedQuery(name = "Convenio.findByComunaEstablecimientoProgramaComponenteMuni", query = "SELECT  c FROM  Convenio c  INNER JOIN c.idPrograma.programa.componentes c2   WHERE   c.monto = (SELECT MAX( c.monto )  FROM Convenio c where  c.idComuna.id = :idComuna AND  c.idEstablecimiento.id = :idEstablecimiento  AND  c.idPrograma.programa.id = :idPrograma   AND c2.id = :idComponente  and (c.idTipoSubtitulo.dependencia.idDependenciaPrograma=:muni1 or c.idTipoSubtitulo.dependencia.idDependenciaPrograma=:muni3))"),
  	@NamedQuery(name = "Convenio.findByNumeroResolucion", query = "SELECT c FROM Convenio c WHERE c.numeroResolucion = :numeroResolucion")})
public class Convenio implements Serializable {
    private static final long serialVersionUID = 1L;

   
    @Id
	@Column(name="id_convenio", unique=true, nullable=false)
	@GeneratedValue
	private Integer idConvenio;
    @Basic(optional = false)
    @Column(name = "monto")
    private Integer monto;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "numero_resolucion")
    private Integer numeroResolucion;
    @Column(name = "aprobacion")
    private Boolean aprobacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "convenio")
    private Set<DocumentoConvenio> documentoConvenios;
    @JoinColumn(name = "id_tipo_subtitulo", referencedColumnName = "id_tipo_subtitulo")
    @ManyToOne
    private TipoSubtitulo idTipoSubtitulo;
    @JoinColumn(name = "id_programa", referencedColumnName = "id_programa_ano")
    @ManyToOne
    private ProgramaAno idPrograma;
    @JoinColumn(name = "mes", referencedColumnName = "id_mes")
    @ManyToOne(optional = false)
    private Mes mes;
    @JoinColumn(name = "id_establecimiento", referencedColumnName = "id")
    @ManyToOne
    private Establecimiento idEstablecimiento;
    @JoinColumn(name = "id_comuna", referencedColumnName = "id")
    @ManyToOne
    private Comuna idComuna;
    @JoinColumn(name = "componente", referencedColumnName = "id")
    @ManyToOne
    private Componente componente;
        
	public Convenio() {
    }

    public Convenio(Integer idConvenio) {
        this.idConvenio = idConvenio;
    }

    public Convenio(Integer idConvenio, Integer monto) {
        this.idConvenio = idConvenio;
        this.monto = monto;
    }

    public Integer getIdConvenio() {
        return idConvenio;
    }

    public void setIdConvenio(Integer idConvenio) {
        this.idConvenio = idConvenio;
    }

    public Integer getMonto() {
        return monto;
    }

    public void setMonto(Integer monto) {
        this.monto = monto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getNumeroResolucion() {
        return numeroResolucion;
    }

    public void setNumeroResolucion(Integer numeroResolucion) {
        this.numeroResolucion = numeroResolucion;
    }

    public TipoSubtitulo getIdTipoSubtitulo() {
        return idTipoSubtitulo;
    }

    public void setIdTipoSubtitulo(TipoSubtitulo idTipoSubtitulo) {
        this.idTipoSubtitulo = idTipoSubtitulo;
    }

    public ProgramaAno getIdPrograma() {
        return idPrograma;
    }

    public void setIdPrograma(ProgramaAno idPrograma) {
        this.idPrograma = idPrograma;
    }

    public Establecimiento getIdEstablecimiento() {
        return idEstablecimiento;
    }

    public void setIdEstablecimiento(Establecimiento idEstablecimiento) {
        this.idEstablecimiento = idEstablecimiento;
    }

    public Comuna getIdComuna() {
        return idComuna;
    }

    public void setIdComuna(Comuna idComuna) {
        this.idComuna = idComuna;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idConvenio != null ? idConvenio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Convenio)) {
            return false;
        }
        Convenio other = (Convenio) object;
        if ((this.idConvenio == null && other.idConvenio != null) || (this.idConvenio != null && !this.idConvenio.equals(other.idConvenio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.Convenio[ idConvenio=" + idConvenio + " ]";
    }
    
    @XmlTransient
	public Set<DocumentoConvenio> getDocumentoConvenios() {
		return documentoConvenios;
	}

	public void setDocumentoConvenios(Set<DocumentoConvenio> documentoConvenios) {
		this.documentoConvenios = documentoConvenios;
	}
    
    public Componente getComponente() {
        return componente;
    }

    public void setComponente(Componente componente) {
        this.componente = componente;
    }

    public Boolean getAprobacion() {
        return aprobacion;
    }

    public void setAprobacion(Boolean aprobacion) {
        this.aprobacion = aprobacion;
    }
}
