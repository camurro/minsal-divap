package cl.minsal.divap.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the cuota database table.
 * 
 */
@Entity
@NamedQuery(name="Cuota.findAll", query="SELECT c FROM Cuota c")
public class Cuota implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id", unique=true, nullable=false)
	@GeneratedValue
	private Integer id;

	@Column(name="fecha_pago1")
	private Integer fechaPago1;

	@Column(name="fecha_pago2")
	private Integer fechaPago2;

	@Column(name="fecha_pago3")
	private Integer fechaPago3;

	private Integer monto;

	@Column(name="numero_cuota")
	private Integer numeroCuota;

	//bi-directional many-to-one association to Programa
	@ManyToOne
	@JoinColumn(name="id_programa")
	private Programa programa;

	public Cuota() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFechaPago1() {
		return this.fechaPago1;
	}

	public void setFechaPago1(Integer fechaPago1) {
		this.fechaPago1 = fechaPago1;
	}

	public Integer getFechaPago2() {
		return this.fechaPago2;
	}

	public void setFechaPago2(Integer fechaPago2) {
		this.fechaPago2 = fechaPago2;
	}

	public Integer getFechaPago3() {
		return this.fechaPago3;
	}

	public void setFechaPago3(Integer fechaPago3) {
		this.fechaPago3 = fechaPago3;
	}

	public Integer getMonto() {
		return this.monto;
	}

	public void setMonto(Integer monto) {
		this.monto = monto;
	}

	public Integer getNumeroCuota() {
		return this.numeroCuota;
	}

	public void setNumeroCuota(Integer numeroCuota) {
		this.numeroCuota = numeroCuota;
	}

	public Programa getPrograma() {
		return this.programa;
	}

	public void setPrograma(Programa programa) {
		this.programa = programa;
	}

}