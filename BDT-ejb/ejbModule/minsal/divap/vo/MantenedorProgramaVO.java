package minsal.divap.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class MantenedorProgramaVO implements Serializable {

	private static final long serialVersionUID = 7346986726867582210L;
	
	private Integer idPrograma; 
	private Integer idProgramaAno; 
	private String nombrePrograma; 
	private String nombreUsuario; 
	private Integer cuotas; 
	private List<MantenedorCuotasVO> listaCuotas;
	private List<MantenedorCuotasVO> listaCuotasActuales;
	private String descripcion; 
	private Boolean reliquidacion; 
	private Boolean fonasa; 
	private Integer ano; 
	private List<String> componentes; 
	private List<String> componentesFaltantes; 
	private List<String> componentesActuales; 
	private String dependencia; 
	private Integer idEstadoPrograma; 
	private String estadoPrograma; 
	private Integer idEstadoFlujoCaja; 
	private String estadoFlujoCaja; 
	private Integer idEstadoreliquidacion; 
	private String estadoreliquidacion; 
	private Integer idEstadoConvenio; 
	private String estadoConvenio; 
	private Integer idEstadoOT; 
	private String estadoOT; 
	private Integer idEstadoModificacionAPS; 
	private String estadoModificacionAPS; 
	private List<String> diaPagoRemesas; 
	private List<String> diaPagoRemesasActuales; 
	private List<String> diaPagoRemesasFaltantes; 
	private Integer idTipoPrograma;
	private String tipoPrograma;
	private Boolean puedeEditarseComponentes;
	HashMap<String, Boolean> reiniciarProcesos;
	
	
	
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

	public List<String> getComponentes() {
		return componentes;
	}

	public void setComponentes(List<String> componentes) {
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

	public List<String> getDiaPagoRemesas() {
		return diaPagoRemesas;
	}

	public void setDiaPagoRemesas(List<String> diaPagoRemesas) {
		this.diaPagoRemesas = diaPagoRemesas;
	}

	public List<String> getDiaPagoRemesasFaltantes() {
		return diaPagoRemesasFaltantes;
	}

	public void setDiaPagoRemesasFaltantes(List<String> diaPagoRemesasFaltantes) {
		this.diaPagoRemesasFaltantes = diaPagoRemesasFaltantes;
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

	public List<String> getComponentesFaltantes() {
		return componentesFaltantes;
	}

	public void setComponentesFaltantes(List<String> componentesFaltantes) {
		this.componentesFaltantes = componentesFaltantes;
	}

	public Boolean getPuedeEditarseComponentes() {
		return puedeEditarseComponentes;
	}

	public void setPuedeEditarseComponentes(Boolean puedeEditarseComponentes) {
		this.puedeEditarseComponentes = puedeEditarseComponentes;
	}

	public HashMap<String, Boolean> getReiniciarProcesos() {
		return reiniciarProcesos;
	}

	public void setReiniciarProcesos(HashMap<String, Boolean> reiniciarProcesos) {
		this.reiniciarProcesos = reiniciarProcesos;
	}

	public List<String> getComponentesActuales() {
		return componentesActuales;
	}

	public void setComponentesActuales(List<String> componentesActuales) {
		this.componentesActuales = componentesActuales;
	}

	public List<MantenedorCuotasVO> getListaCuotasActuales() {
		return listaCuotasActuales;
	}

	public void setListaCuotasActuales(List<MantenedorCuotasVO> listaCuotasActuales) {
		this.listaCuotasActuales = listaCuotasActuales;
	}

	public List<String> getDiaPagoRemesasActuales() {
		return diaPagoRemesasActuales;
	}

	public void setDiaPagoRemesasActuales(List<String> diaPagoRemesasActuales) {
		this.diaPagoRemesasActuales = diaPagoRemesasActuales;
	}

	@Override
	public String toString() {
		return "MantenedorProgramaVO [idPrograma=" + idPrograma
				+ ", idProgramaAno=" + idProgramaAno + ", nombrePrograma="
				+ nombrePrograma + ", nombreUsuario=" + nombreUsuario
				+ ", cuotas=" + cuotas + ", listaCuotas=" + listaCuotas
				+ ", listaCuotasActuales=" + listaCuotasActuales
				+ ", descripcion=" + descripcion + ", reliquidacion="
				+ reliquidacion + ", fonasa=" + fonasa + ", ano=" + ano
				+ ", componentes=" + componentes + ", componentesFaltantes="
				+ componentesFaltantes + ", componentesActuales="
				+ componentesActuales + ", dependencia=" + dependencia
				+ ", idEstadoPrograma=" + idEstadoPrograma
				+ ", estadoPrograma=" + estadoPrograma + ", idEstadoFlujoCaja="
				+ idEstadoFlujoCaja + ", estadoFlujoCaja=" + estadoFlujoCaja
				+ ", idEstadoreliquidacion=" + idEstadoreliquidacion
				+ ", estadoreliquidacion=" + estadoreliquidacion
				+ ", idEstadoConvenio=" + idEstadoConvenio
				+ ", estadoConvenio=" + estadoConvenio + ", idEstadoOT="
				+ idEstadoOT + ", estadoOT=" + estadoOT
				+ ", idEstadoModificacionAPS=" + idEstadoModificacionAPS
				+ ", estadoModificacionAPS=" + estadoModificacionAPS
				+ ", diaPagoRemesas=" + diaPagoRemesas
				+ ", diaPagoRemesasActuales=" + diaPagoRemesasActuales
				+ ", diaPagoRemesasFaltantes=" + diaPagoRemesasFaltantes
				+ ", idTipoPrograma=" + idTipoPrograma + ", tipoPrograma="
				+ tipoPrograma + ", puedeEditarseComponentes="
				+ puedeEditarseComponentes + ", reiniciarProcesos="
				+ reiniciarProcesos + "]";
	}

}

