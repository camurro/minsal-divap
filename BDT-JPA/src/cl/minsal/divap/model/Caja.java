package cl.minsal.divap.model;

import java.io.Serializable;

import javax.persistence.Basic;
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
@Table(name = "caja")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Caja.findAll", query = "SELECT c FROM Caja c"),
    @NamedQuery(name = "Caja.findById", query = "SELECT c FROM Caja c WHERE c.id = :id"),
    @NamedQuery(name = "Caja.findByMarcoPresupuestario", query = "SELECT c FROM Caja c WHERE c.marcoPresupuestario = :marcoPresupuestario"),
    @NamedQuery(name = "Caja.findByEnero", query = "SELECT c FROM Caja c WHERE c.enero = :enero"),
    @NamedQuery(name = "Caja.findByFebrero", query = "SELECT c FROM Caja c WHERE c.febrero = :febrero"),
    @NamedQuery(name = "Caja.findByMarzo", query = "SELECT c FROM Caja c WHERE c.marzo = :marzo"),
    @NamedQuery(name = "Caja.findByAbril", query = "SELECT c FROM Caja c WHERE c.abril = :abril"),
    @NamedQuery(name = "Caja.findByMayo", query = "SELECT c FROM Caja c WHERE c.mayo = :mayo"),
    @NamedQuery(name = "Caja.findByJunio", query = "SELECT c FROM Caja c WHERE c.junio = :junio"),
    @NamedQuery(name = "Caja.findByJulio", query = "SELECT c FROM Caja c WHERE c.julio = :julio"),
    @NamedQuery(name = "Caja.findByAgosto", query = "SELECT c FROM Caja c WHERE c.agosto = :agosto"),
    @NamedQuery(name = "Caja.findBySeptiembre", query = "SELECT c FROM Caja c WHERE c.septiembre = :septiembre"),
    @NamedQuery(name = "Caja.findByOctubre", query = "SELECT c FROM Caja c WHERE c.octubre = :octubre"),
    @NamedQuery(name = "Caja.findByNoviembre", query = "SELECT c FROM Caja c WHERE c.noviembre = :noviembre"),
    @NamedQuery(name = "Caja.findByDiciembre", query = "SELECT c FROM Caja c WHERE c.diciembre = :diciembre"),
    @NamedQuery(name = "Caja.findByTotal", query = "SELECT c FROM Caja c WHERE c.total = :total"),
    @NamedQuery(name = "Caja.findByIdSubtitulo", query = "SELECT c FROM Caja c WHERE c.idSubtitulo = :idSubtitulo"),
    @NamedQuery(name = "Caja.findByIdPrograma", query = "SELECT c FROM Caja c WHERE c.idProgramaAno.programa.id = :idPrograma"),
    @NamedQuery(name = "Caja.findByIdProgramaAno", query = "SELECT c FROM Caja c WHERE c.idProgramaAno.programa.id = :idProgramaAno"),
    @NamedQuery(name = "Caja.findByIdProgramaAnoSubtitulo", query = "SELECT c FROM Caja c WHERE c.idProgramaAno.programa.id = :idPrograma and c.idProgramaAno.ano.ano = :ano and c.idSubtitulo = :idSubtitulo"),
    @NamedQuery(name = "Caja.findByAno", query = "SELECT c FROM Caja c WHERE c.ano = :ano")})




public class Caja implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id",unique=true, nullable=false)
    @GeneratedValue
    private Integer id;
    @Column(name = "marco_presupuestario")
    private Long marcoPresupuestario;
    @Column(name = "enero")
    private Long enero;
    @Column(name = "febrero")
    private Long febrero;
    @Column(name = "marzo")
    private Long marzo;
    @Column(name = "abril")
    private Long abril;
    @Column(name = "mayo")
    private Long mayo;
    @Column(name = "junio")
    private Long junio;
    @Column(name = "julio")
    private Long julio;
    @Column(name = "agosto")
    private Long agosto;
    @Column(name = "septiembre")
    private Long septiembre;
    @Column(name = "octubre")
    private Long octubre;
    @Column(name = "noviembre")
    private Long noviembre;
    @Column(name = "diciembre")
    private Long diciembre;
    @Column(name = "total")
    private Long total;
    @Column(name = "id_subtitulo")
    private Integer idSubtitulo;
    @Column(name = "ano")
    private Integer ano;
    @JoinColumn(name = "id_servicio", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ServicioSalud idServicio;
    @JoinColumn(name = "id_programa_ano", referencedColumnName = "id_programa_ano")
    @ManyToOne(optional = false)
    private ProgramaAno idProgramaAno;
    @JoinColumn(name = "id_comuna", referencedColumnName = "id")
    @ManyToOne
    private Comuna idComuna;
    @JoinColumn(name = "id_componente", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Componente idComponente;
    
    @JoinColumn(name = "id_establecimiento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Establecimiento idEstablecimiento;

    public Establecimiento getIdEstablecimiento() {
		return idEstablecimiento;
	}

	public void setIdEstablecimiento(Establecimiento idEstablecimiento) {
		this.idEstablecimiento = idEstablecimiento;
	}

	public Caja() {
    }

    public Caja(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getMarcoPresupuestario() {
        return marcoPresupuestario;
    }

    public void setMarcoPresupuestario(Long marcoPresupuestario) {
        this.marcoPresupuestario = marcoPresupuestario;
    }

    public Long getEnero() {
        return enero;
    }

    public void setEnero(Long enero) {
        this.enero = enero;
    }

    public Long getFebrero() {
        return febrero;
    }

    public void setFebrero(Long febrero) {
        this.febrero = febrero;
    }

    public Long getMarzo() {
        return marzo;
    }

    public void setMarzo(Long marzo) {
        this.marzo = marzo;
    }

    public Long getAbril() {
        return abril;
    }

    public void setAbril(Long abril) {
        this.abril = abril;
    }

    public Long getMayo() {
        return mayo;
    }

    public void setMayo(Long mayo) {
        this.mayo = mayo;
    }

    public Long getJunio() {
        return junio;
    }

    public void setJunio(Long junio) {
        this.junio = junio;
    }

    public Long getJulio() {
        return julio;
    }

    public void setJulio(Long julio) {
        this.julio = julio;
    }

    public Long getAgosto() {
        return agosto;
    }

    public void setAgosto(Long agosto) {
        this.agosto = agosto;
    }

    public Long getSeptiembre() {
        return septiembre;
    }

    public void setSeptiembre(Long septiembre) {
        this.septiembre = septiembre;
    }

    public Long getOctubre() {
        return octubre;
    }

    public void setOctubre(Long octubre) {
        this.octubre = octubre;
    }

    public Long getNoviembre() {
        return noviembre;
    }

    public void setNoviembre(Long noviembre) {
        this.noviembre = noviembre;
    }

    public Long getDiciembre() {
        return diciembre;
    }

    public void setDiciembre(Long diciembre) {
        this.diciembre = diciembre;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getIdSubtitulo() {
        return idSubtitulo;
    }

    public void setIdSubtitulo(Integer idSubtitulo) {
        this.idSubtitulo = idSubtitulo;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public ServicioSalud getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(ServicioSalud idServicio) {
        this.idServicio = idServicio;
    }

    public ProgramaAno getIdPrograma() {
        return idProgramaAno;
    }

    public void setIdPrograma(ProgramaAno idPrograma) {
        this.idProgramaAno = idPrograma;
    }

    public Comuna getIdComuna() {
        return idComuna;
    }

    public void setIdComuna(Comuna idComuna) {
        this.idComuna = idComuna;
    }

    public Componente getIdComponente() {
        return idComponente;
    }

    public void setIdComponente(Componente idComponente) {
        this.idComponente = idComponente;
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
        if (!(object instanceof Caja)) {
            return false;
        }
        Caja other = (Caja) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication2.Caja[ id=" + id + " ]";
    }
    
}
