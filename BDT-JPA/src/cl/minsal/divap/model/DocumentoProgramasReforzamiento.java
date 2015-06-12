package cl.minsal.divap.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
@Table(name = "documento_programas_reforzamiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentoProgramasReforzamiento.findAll", query = "SELECT d FROM DocumentoProgramasReforzamiento d"),
    @NamedQuery(name = "DocumentoProgramasReforzamiento.findById", query = "SELECT d FROM DocumentoProgramasReforzamiento d WHERE d.id = :id"),
    @NamedQuery(name = "DocumentoProgramasReforzamiento.deleteById", query = "DELETE FROM DocumentoProgramasReforzamiento d WHERE d.idDocumento.id = :id"),
    @NamedQuery(name = "DocumentoProgramasReforzamiento.findByProgramaAnoTipoDocumento", query = "SELECT d FROM DocumentoProgramasReforzamiento d WHERE d.idTipoDocumento.idTipoDocumento = :idTipoDocumento and d.idProgramaAno.idProgramaAno = :idProgramaAno order by d.id desc"),
    @NamedQuery(name = "DocumentoProgramasReforzamiento.findByProgramaAnoDistribucionRecursosTipoDocumento", query = "SELECT d FROM DocumentoProgramasReforzamiento d WHERE d.idTipoDocumento.idTipoDocumento = :idTipoDocumento and d.idProgramaAno.idProgramaAno = :idProgramaAno and d.distribucionRecursos.idProgramasReforzamiento = :idProceso order by d.id desc"),
    @NamedQuery(name = "DocumentoProgramasReforzamiento.findByProcesoProgramaAnoTipoDocumentoIdServicio", query = "SELECT d FROM DocumentoProgramasReforzamiento d WHERE d.distribucionRecursos.idProgramasReforzamiento = :idProceso and d.idTipoDocumento.idTipoDocumento = :idTipoDocumento and d.idProgramaAno.idProgramaAno = :idProgramaAno and d.idServicio.id = :idServicio order by d.id asc"),
    @NamedQuery(name = "DocumentoProgramasReforzamiento.findVersionFinalByIdRecursosFinancierosTipoDocumento", query = "SELECT d.idDocumento FROM DocumentoProgramasReforzamiento d WHERE d.distribucionRecursos.idProgramasReforzamiento = :idProceso and d.idTipoDocumento.idTipoDocumento = :idTipoDocumento and d.idProgramaAno.idProgramaAno = :idProgramaAno and d.idDocumento.documentoFinal = true order by d.idDocumento.fechaCreacion desc"),
    @NamedQuery(name = "DocumentoProgramasReforzamiento.findByIdProcesoIdProgramaAnoTiposNotFinal", query = "SELECT d.idDocumento FROM DocumentoProgramasReforzamiento d WHERE d.distribucionRecursos.idProgramasReforzamiento = :idProceso and d.idTipoDocumento.idTipoDocumento IN (:idTiposDocumento) and d.idDocumento.documentoFinal = false and d.idProgramaAno.idProgramaAno = :idProgramaAno"),
    @NamedQuery(name = "DocumentoProgramasReforzamiento.deleteUsingIds", query = "DELETE FROM DocumentoProgramasReforzamiento d WHERE d.id IN (:idDocumentosDistribucionRecursos)")})
public class DocumentoProgramasReforzamiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="id", unique=true, nullable=false)
    @GeneratedValue
    private Integer id;
    @JoinColumn(name = "id_tipo_documento", referencedColumnName = "id_tipo_documento")
    @ManyToOne
    private TipoDocumento idTipoDocumento;
    @JoinColumn(name = "id_documento", referencedColumnName = "id")
    @ManyToOne
    private ReferenciaDocumento idDocumento;
    @JoinColumn(name = "distribucion_recursos", referencedColumnName = "id_programas_reforzamiento")
    @ManyToOne
    private ProgramasReforzamiento distribucionRecursos;
    @JoinColumn(name = "id_programa_ano", referencedColumnName = "id_programa_ano")
    @ManyToOne
    private ProgramaAno idProgramaAno;
    @JoinColumn(name = "id_servicio", referencedColumnName = "id")
    @ManyToOne
    private ServicioSalud idServicio;

    public DocumentoProgramasReforzamiento() {
    }

    public DocumentoProgramasReforzamiento(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TipoDocumento getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(TipoDocumento idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public ReferenciaDocumento getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(ReferenciaDocumento idDocumento) {
        this.idDocumento = idDocumento;
    }

    public ProgramaAno getIdProgramaAno() {
        return idProgramaAno;
    }

    public void setIdProgramaAno(ProgramaAno idProgramaAno) {
        this.idProgramaAno = idProgramaAno;
    }
    public ServicioSalud getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(ServicioSalud idServicio) {
        this.idServicio = idServicio;
    }

    public ProgramasReforzamiento getDistribucionRecursos() {
		return distribucionRecursos;
	}

	public void setDistribucionRecursos(ProgramasReforzamiento distribucionRecursos) {
		this.distribucionRecursos = distribucionRecursos;
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
        if (!(object instanceof DocumentoProgramasReforzamiento)) {
            return false;
        }
        DocumentoProgramasReforzamiento other = (DocumentoProgramasReforzamiento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.DocumentoProgramasReforzamiento[ id=" + id + " ]";
    }
    
}