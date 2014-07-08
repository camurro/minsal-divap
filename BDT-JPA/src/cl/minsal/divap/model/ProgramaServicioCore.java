package cl.minsal.divap.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the programa_servicio_core database table.
 * 
 */
@Entity
@Table(name="programa_servicio_core")
@NamedQuery(name="ProgramaServicioCore.findAll", query="SELECT p FROM ProgramaServicioCore p")
public class ProgramaServicioCore implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_programa_servicio")
	private Integer idProgramaServicio;

	private String columna1;

	private String columna10;

	private String columna2;

	private String columna3;

	private String columna4;

	private String columna5;

	private String columna6;

	private String columna7;

	private String columna8;

	private String columna9;

	//bi-directional many-to-one association to Programa
	@ManyToOne
	@JoinColumn(name="id_programa")
	private Programa programa;

	//bi-directional many-to-one association to ServicioSalud
	@ManyToOne
	@JoinColumn(name="id_servicio_salud")
	private ServicioSalud servicioSalud;

	public ProgramaServicioCore() {
	}

	public Integer getIdProgramaServicio() {
		return this.idProgramaServicio;
	}

	public void setIdProgramaServicio(Integer idProgramaServicio) {
		this.idProgramaServicio = idProgramaServicio;
	}

	public String getColumna1() {
		return this.columna1;
	}

	public void setColumna1(String columna1) {
		this.columna1 = columna1;
	}

	public String getColumna10() {
		return this.columna10;
	}

	public void setColumna10(String columna10) {
		this.columna10 = columna10;
	}

	public String getColumna2() {
		return this.columna2;
	}

	public void setColumna2(String columna2) {
		this.columna2 = columna2;
	}

	public String getColumna3() {
		return this.columna3;
	}

	public void setColumna3(String columna3) {
		this.columna3 = columna3;
	}

	public String getColumna4() {
		return this.columna4;
	}

	public void setColumna4(String columna4) {
		this.columna4 = columna4;
	}

	public String getColumna5() {
		return this.columna5;
	}

	public void setColumna5(String columna5) {
		this.columna5 = columna5;
	}

	public String getColumna6() {
		return this.columna6;
	}

	public void setColumna6(String columna6) {
		this.columna6 = columna6;
	}

	public String getColumna7() {
		return this.columna7;
	}

	public void setColumna7(String columna7) {
		this.columna7 = columna7;
	}

	public String getColumna8() {
		return this.columna8;
	}

	public void setColumna8(String columna8) {
		this.columna8 = columna8;
	}

	public String getColumna9() {
		return this.columna9;
	}

	public void setColumna9(String columna9) {
		this.columna9 = columna9;
	}

	public Programa getPrograma() {
		return this.programa;
	}

	public void setPrograma(Programa programa) {
		this.programa = programa;
	}

	public ServicioSalud getServicioSalud() {
		return this.servicioSalud;
	}

	public void setServicioSalud(ServicioSalud servicioSalud) {
		this.servicioSalud = servicioSalud;
	}

}