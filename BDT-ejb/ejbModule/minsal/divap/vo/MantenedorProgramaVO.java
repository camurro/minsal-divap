package minsal.divap.vo;

import java.io.Serializable;
import java.util.List;

public class MantenedorProgramaVO implements Serializable {

	private static final long serialVersionUID = 7346986726867582210L;
	
	private Integer idPrograma; //OK
	private Integer idProgramaAno; //OK
	private String nombrePrograma; //OK
	private String nombreUsuario; //OK
	private Integer cuotas; //OK
	private List<MantenedorCuotasVO> listaCuotas;
	private String descripcion; //OK
	private Boolean reliquidacion; //OK
	private Boolean fonasa; //OK
	private Integer ano; //OK
	private List<ComponentesVO> componentes; //OK
	private String dependencia; //OK
	private Integer idEstadoPrograma; //OK
	private String estadoPrograma; //OK
	private Integer idEstadoFlujoCaja; //OK
	private String estadoFlujoCaja; //OK
	private Integer idEstadoreliquidacion; //OK
	private String estadoreliquidacion; //OK
	private Integer idEstadoConvenio; //OK
	private String estadoConvenio; //OK
	private Integer idEstadoOT; //OK
	private String estadoOT; //OK
	private Integer idEstadoModificacionAPS; //OK
	private String estadoModificacionAPS; //OK
	private List<Integer> diaPagoRemesas; //OK
	
	private Integer idTipoPrograma;
	private String tipoPrograma;
	
	
	
	
	public MantenedorProgramaVO(){
		
	}

	public List<MantenedorCuotasVO> getListaCuotas() {
		return listaCuotas;
	}

	public void setListaCuotas(List<MantenedorCuotasVO> listaCuotas) {
		this.listaCuotas = listaCuotas;
	}

	public Integer getCuotas() {
		return cuotas;
	}

	public void setCuotas(Integer cuotas) {
		this.cuotas = cuotas;
	}

	public Integer getIdPrograma() {
		return idPrograma;
	}

	public void setIdPrograma(Integer idPrograma) {
		this.idPrograma = idPrograma;
	}

	public Integer getIdProgramaAno() {
		return idProgramaAno;
	}

	public void setIdProgramaAno(Integer idProgramaAno) {
		this.idProgramaAno = idProgramaAno;
	}

	public String getNombrePrograma() {
		return nombrePrograma;
	}

	public void setNombrePrograma(String nombrePrograma) {
		this.nombrePrograma = nombrePrograma;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Boolean getReliquidacion() {
		return reliquidacion;
	}

	public void setReliquidacion(Boolean reliquidacion) {
		this.reliquidacion = reliquidacion;
	}

	public Boolean getFonasa() {
		return fonasa;
	}

	public void setFonasa(Boolean fonasa) {
		this.fonasa = fonasa;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public List<ComponentesVO> getComponentes() {
		return componentes;
	}

	public void setComponentes(List<ComponentesVO> componentes) {
		this.componentes = componentes;
	}

	public String getDependencia() {
		return dependencia;
	}

	public void setDependencia(String dependencia) {
		this.dependencia = dependencia;
	}

	public Integer getIdEstadoPrograma() {
		return idEstadoPrograma;
	}

	public void setIdEstadoPrograma(Integer idEstadoPrograma) {
		this.idEstadoPrograma = idEstadoPrograma;
	}

	public String getEstadoPrograma() {
		return estadoPrograma;
	}

	public void setEstadoPrograma(String estadoPrograma) {
		this.estadoPrograma = estadoPrograma;
	}

	public Integer getIdEstadoFlujoCaja() {
		return idEstadoFlujoCaja;
	}

	public void setIdEstadoFlujoCaja(Integer idEstadoFlujoCaja) {
		this.idEstadoFlujoCaja = idEstadoFlujoCaja;
	}

	public String getEstadoFlujoCaja() {
		return estadoFlujoCaja;
	}

	public void setEstadoFlujoCaja(String estadoFlujoCaja) {
		this.estadoFlujoCaja = estadoFlujoCaja;
	}

	public Integer getIdEstadoreliquidacion() {
		return idEstadoreliquidacion;
	}

	public void setIdEstadoreliquidacion(Integer idEstadoreliquidacion) {
		this.idEstadoreliquidacion = idEstadoreliquidacion;
	}

	public String getEstadoreliquidacion() {
		return estadoreliquidacion;
	}

	public void setEstadoreliquidacion(String estadoreliquidacion) {
		this.estadoreliquidacion = estadoreliquidacion;
	}

	public Integer getIdEstadoConvenio() {
		return idEstadoConvenio;
	}

	public void setIdEstadoConvenio(Integer idEstadoConvenio) {
		this.idEstadoConvenio = idEstadoConvenio;
	}

	public String getEstadoConvenio() {
		return estadoConvenio;
	}

	public void setEstadoConvenio(String estadoConvenio) {
		this.estadoConvenio = estadoConvenio;
	}

	public Integer getIdEstadoOT() {
		return idEstadoOT;
	}

	public void setIdEstadoOT(Integer idEstadoOT) {
		this.idEstadoOT = idEstadoOT;
	}

	public String getEstadoOT() {
		return estadoOT;
	}

	public void setEstadoOT(String estadoOT) {
		this.estadoOT = estadoOT;
	}

	public Integer getIdEstadoModificacionAPS() {
		return idEstadoModificacionAPS;
	}

	public void setIdEstadoModificacionAPS(Integer idEstadoModificacionAPS) {
		this.idEstadoModificacionAPS = idEstadoModificacionAPS;
	}

	public String getEstadoModificacionAPS() {
		return estadoModificacionAPS;
	}

	public void setEstadoModificacionAPS(String estadoModificacionAPS) {
		this.estadoModificacionAPS = estadoModificacionAPS;
	}

	public List<Integer> getDiaPagoRemesas() {
		return diaPagoRemesas;
	}

	public void setDiaPagoRemesas(List<Integer> diaPagoRemesas) {
		this.diaPagoRemesas = diaPagoRemesas;
	}

	public Integer getIdTipoPrograma() {
		return idTipoPrograma;
	}

	public void setIdTipoPrograma(Integer idTipoPrograma) {
		this.idTipoPrograma = idTipoPrograma;
	}

	public String getTipoPrograma() {
		return tipoPrograma;
	}

	public void setTipoPrograma(String tipoPrograma) {
		this.tipoPrograma = tipoPrograma;
	}

	@Override
	public String toString() {
		return "MantenedorProgramaVO [idPrograma=" + idPrograma
				+ ", idProgramaAno=" + idProgramaAno + ", nombrePrograma="
				+ nombrePrograma + ", nombreUsuario=" + nombreUsuario
				+ ", cuotas=" + cuotas + ", listaCuotas=" + listaCuotas
				+ ", descripcion=" + descripcion + ", reliquidacion="
				+ reliquidacion + ", fonasa=" + fonasa + ", ano=" + ano
				+ ", componentes=" + componentes + ", dependencia="
				+ dependencia + ", idEstadoPrograma=" + idEstadoPrograma
				+ ", estadoPrograma=" + estadoPrograma + ", idEstadoFlujoCaja="
				+ idEstadoFlujoCaja + ", estadoFlujoCaja=" + estadoFlujoCaja
				+ ", idEstadoreliquidacion=" + idEstadoreliquidacion
				+ ", estadoreliquidacion=" + estadoreliquidacion
				+ ", idEstadoConvenio=" + idEstadoConvenio
				+ ", estadoConvenio=" + estadoConvenio + ", idEstadoOT="
				+ idEstadoOT + ", estadoOT=" + estadoOT
				+ ", idEstadoModificacionAPS=" + idEstadoModificacionAPS
				+ ", estadoModificacionAPS=" + estadoModificacionAPS
				+ ", diaPagoRemesas=" + diaPagoRemesas + ", idTipoPrograma="
				+ idTipoPrograma + ", tipoPrograma=" + tipoPrograma + "]";
	}

}

