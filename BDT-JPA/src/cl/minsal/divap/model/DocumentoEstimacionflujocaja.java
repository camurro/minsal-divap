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
 * @author Aldo
 */
@Entity
@Table(name = "documento_estimacionflujocaja")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentoEstimacionflujocaja.findAll", query = "SELECT d FROM DocumentoEstimacionflujocaja d"),
    @NamedQuery(name = "DocumentoEstimacionflujocaja.findByProgramaAnoTipoDocumento", query = "SELECT d FROM DocumentoEstimacionflujocaja d WHERE d.idProgramaAno.idProgramaAno = :idProgramaAno and d.idTipoDocumento.idTipoDocumento = :idTipoDocumento"),
    @NamedQuery(name = "DocumentoEstimacionflujocaja.findByTipoDocumento", query = "SELECT d FROM DocumentoEstimacionflujocaja d WHERE d.idTipoDocumento.idTipoDocumento = :idTipoDocumento"),
    @NamedQuery(name = "DocumentoEstimacionflujocaja.deleteUsingIds", query = "DELETE FROM DocumentoEstimacionflujocaja d WHERE d.id IN (:idDocumentosEstimacion)"),
    @NamedQuery(name = "DocumentoEstimacionflujocaja.findById", query = "SELECT d FROM DocumentoEstimacionflujocaja d WHERE d.id = :id"),
    @NamedQuery(name = "DocumentoEstimacionflujocaja.findByTypesIdProgramaAno", query = "SELECT d FROM DocumentoEstimacionflujocaja d WHERE d.idProgramaAno.idProgramaAno = :idProgramaAno and d.idTipoDocumento.idTipoDocumento = :idTipoDocumento")})
public class DocumentoEstimacionflujocaja implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id",unique=true, nullable=false)
    @GeneratedValue
    private Integer id;
    @JoinColumn(name = "id_tipo_documento", referencedColumnName = "id_tipo_documento")
    @ManyToOne
    private TipoDocumento idTipoDocumento;
    @JoinColumn(name = "id_documento", referencedColumnName = "id")
    @ManyToOne
    private ReferenciaDocumento idDocumento;
    @JoinColumn(name = "id_programa_ano", referencedColumnName = "id_programa_ano")
    @ManyToOne
    private ProgramaAno idProgramaAno;
    @JoinColumn(name = "id_mes", referencedColumnName = "id_mes")
    @ManyToOne
    private Mes idMes;
    @JoinColumn(name = "ano", referencedColumnName = "ano")
    @ManyToOne
    private AnoEnCurso ano;

    public DocumentoEstimacionflujocaja() {
    }

    public DocumentoEstimacionflujocaja(Integer id) {
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
    
    public Mes getIdMes() {
        return idMes;
    }

    public void setIdMes(Mes idMes) {
        this.idMes = idMes;
    }

    public AnoEnCurso getAno() {
        return ano;
    }

    public void setAno(AnoEnCurso ano) {
        this.ano = ano;
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
        if (!(object instanceof DocumentoEstimacionflujocaja)) {
            return false;
        }
        DocumentoEstimacionflujocaja other = (DocumentoEstimacionflujocaja) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication2.DocumentoEstimacionflujocaja[ id=" + id + " ]";
    }
   
}