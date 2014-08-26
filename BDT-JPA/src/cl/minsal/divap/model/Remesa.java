package cl.minsal.divap.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the Remesa database table.
 * 
 */
@Entity
@Table(name = "remesa")
@NamedQueries({
	@NamedQuery(name="Remesa.findAll", query="SELECT p FROM Remesa p")})
public class Remesa implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="idRemesa", unique=true, nullable=false)
	@GeneratedValue
	private Integer idRemesa;

	//bi-directional many-to-one association to Programa
	@ManyToOne
	@JoinColumn(name="idPrograma")
	private Programa programa;
	
//	//bi-directional many-to-one association to Establecimiento
//	@ManyToOne
//	@JoinColumn(name="idEstablecimiento")
//	private Establecimiento establecimiento;
//	
//	//bi-directional many-to-one association to ServicioSalud
//	@ManyToOne
//	@JoinColumn(name="idServicioSalud")
//	private ServicioSalud servicioSalud;
//	
//	//bi-directional many-to-one association to Comuna
//	@ManyToOne
//	@JoinColumn(name="idComuna")
//	private Comuna comuna;
	
	@Column(name = "anio")
	private Integer anio;
	
	@Column(name = "valorDia09")
	private Double valorDia09;
	
	@Column(name = "valorDia24")
	private Double valorDia24;
	
	@Column(name = "valorDia28")
	private Double valorDia28;

	public Integer getIdRemesa() {
		return idRemesa;
	}

	public void setIdRemesa(Integer idRemesa) {
		this.idRemesa = idRemesa;
	}

	public Programa getPrograma() {
		return programa;
	}

	public void setPrograma(Programa programa) {
		this.programa = programa;
	}

//	public Establecimiento getEstablecimiento() {
//		return establecimiento;
//	}
//
//	public void setEstablecimiento(Establecimiento establecimiento) {
//		this.establecimiento = establecimiento;
//	}
//
//	public ServicioSalud getServicioSalud() {
//		return servicioSalud;
//	}
//
//	public void setServicioSalud(ServicioSalud servicioSalud) {
//		this.servicioSalud = servicioSalud;
//	}
//
//	public Comuna getComuna() {
//		return comuna;
//	}

//	public void setComuna(Comuna comuna) {
//		this.comuna = comuna;
//	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnoEnCurso(Integer anio) {
		this.anio = anio;
	}
//
//	public Mes getMes() {
//		return Mes;
//	}
//
//	public void setMes(Mes mes) {
//		Mes = mes;
//	}
//	

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Double getValorDia09() {
		return valorDia09;
	}

	public void setValorDia09(Double valorDia09) {
		this.valorDia09 = valorDia09;
	}

	public Double getValorDia24() {
		return valorDia24;
	}

	public void setValorDia24(Double valorDia24) {
		this.valorDia24 = valorDia24;
	}

	public Double getValorDia28() {
		return valorDia28;
	}

	public void setValorDia28(Double valorDia28) {
		this.valorDia28 = valorDia28;
	}
	
	

}