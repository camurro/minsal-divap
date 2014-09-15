package minsal.divap.vo;

import java.io.Serializable;
import java.util.List;

import cl.minsal.divap.model.Componente;


public class ConveniosVO  implements Serializable {
	
	private static final long serialVersionUID = -3220915341874133549L;

	
	
	private	Integer idConverio;
	private	String comunaNombre;
	private	String establecimientoNombre;
	private	String programaNombre;
	private	String tipoSubtituloNombreSubtitulo;
	private	Integer convenioMonto;
	private Integer numeroResolucion;
	private String componente;
	private Integer idDocumentoConvenio;				
	private String nodoAlfresco;
	private String nombreDocu;
	private	Integer idSubtitulo;
	private	String Fecha;
	private Integer marcoPresupuestario;
	private Integer ano;
	private Integer total;
	
	
	
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getAno() {
		return ano;
	}
	public void setAno(Integer ano) {
		this.ano = ano;
	}
	public String getFecha() {
		return Fecha;
	}
	public void setFecha(String fecha) {
		Fecha = fecha;
	}
	public Integer getIdSubtitulo() {
		return idSubtitulo;
	}
	public void setIdSubtitulo(Integer idSubtitulo) {
		this.idSubtitulo = idSubtitulo;
	}
	public Integer getIdDocumentoConvenio() {
		return idDocumentoConvenio;
	}
	public void setIdDocumentoConvenio(Integer idDocumentoConvenio) {
		this.idDocumentoConvenio = idDocumentoConvenio;
	}
	public String getNodoAlfresco() {
		return nodoAlfresco;
	}
	public void setNodoAlfresco(String nodoAlfresco) {
		this.nodoAlfresco = nodoAlfresco;
	}
	public String getNombreDocu() {
		return nombreDocu;
	}
	public void setNombreDocu(String nombreDocu) {
		this.nombreDocu = nombreDocu;
	}
	public String getComponente() {
		return componente;
	}
	public void setComponente(String list) {
		this.componente = list;
	}
	public Integer getNumeroResolucion() {
		return numeroResolucion;
	}
	public void setNumeroResolucion(Integer numeroResolucion) {
		this.numeroResolucion = numeroResolucion;
	}
	public String getComunaNombre() {
		return comunaNombre;
	}
	public void setComunaNombre(String comunaNombre) {
		this.comunaNombre = comunaNombre;
	}
	public String getEstablecimientoNombre() {
		return establecimientoNombre;
	}
	public void setEstablecimientoNombre(String establecimientoNombre) {
		this.establecimientoNombre = establecimientoNombre;
	}
	public String getProgramaNombre() {
		return programaNombre;
	}
	public void setProgramaNombre(String programaNombre) {
		this.programaNombre = programaNombre;
	}
	public String getTipoSubtituloNombreSubtitulo() {
		return tipoSubtituloNombreSubtitulo;
	}
	public void setTipoSubtituloNombreSubtitulo(String tipoSubtituloNombreSubtitulo) {
		this.tipoSubtituloNombreSubtitulo = tipoSubtituloNombreSubtitulo;
	}
	public Integer getConvenioMonto() {
		return convenioMonto;
	}
	public void setConvenioMonto(Integer convenioMonto) {
		this.convenioMonto = convenioMonto;
	}
	public Integer getIdConverio() {
		return idConverio;
	}
	public void setIdConverio(Integer idConverio) {
		this.idConverio = idConverio;
	}
	public Integer getMarcoPresupuestario() {
		return marcoPresupuestario;
	}
	public void setMarcoPresupuestario(Integer marcoPresupuestario) {
		this.marcoPresupuestario = marcoPresupuestario;
	} 
	
	
	
}
