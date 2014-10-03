package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the cuota database table.
 * 
 */
@Entity
@Table(name = "cuota")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cuota.findAll", query = "SELECT c FROM Cuota c"),
    @NamedQuery(name = "Cuota.findById", query = "SELECT c FROM Cuota c WHERE c.id = :id"),
    @NamedQuery(name = "Cuota.findByNumeroCuota", query = "SELECT c FROM Cuota c WHERE c.numeroCuota = :numeroCuota"),
    @NamedQuery(name = "Cuota.findByMonto", query = "SELECT c FROM Cuota c WHERE c.monto = :monto"),
    @NamedQuery(name = "Cuota.findByFechaPago", query = "SELECT c FROM Cuota c WHERE c.fechaPago = :fechaPago")})
public class Cuota implements Serializable {
    private static final long serialVersionUID = 1L;
	@Id
	@Column(name="id", unique=true, nullable=false)
	@GeneratedValue
    private Integer id;
    @Basic(optional = false)
    @Column(name = "numero_cuota")
    private short numeroCuota;
    @Basic(optional = false)
    @Column(name = "monto")
    private int monto;
    @Column(name = "fecha_pago")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPago;
    @JoinColumn(name = "id_programa", referencedColumnName = "id_programa_ano")
    @ManyToOne
    private ProgramaAno idPrograma;

    public Cuota() {
    }

    public Cuota(Integer id) {
        this.id = id;
    }

    public Cuota(Integer id, short numeroCuota, int monto) {
        this.id = id;
        this.numeroCuota = numeroCuota;
        this.monto = monto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public short getNumeroCuota() {
        return numeroCuota;
    }

    public void setNumeroCuota(short numeroCuota) {
        this.numeroCuota = numeroCuota;
    }

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public ProgramaAno getIdPrograma() {
        return idPrograma;
    }

    public void setIdPrograma(ProgramaAno idPrograma) {
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
        if (!(object instanceof Cuota)) {
            return false;
        }
        Cuota other = (Cuota) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.Cuota[ id=" + id + " ]";
    }
    
}