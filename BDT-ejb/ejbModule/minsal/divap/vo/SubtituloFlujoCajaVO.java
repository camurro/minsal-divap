package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class SubtituloFlujoCajaVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4939157865175247881L;

	private Integer idServicio;
	private String servicio;
	private Integer idSubtitulo;
	private String subtitulo;
	private Integer idMarcoPresupuestario;
	private Long marcoPresupuestario;
	private Double pesoComponentes;
	private TransferenciaSummaryVO transferenciaAcumulada;
	private ConveniosSummaryVO convenioRecibido;
	private CajaMontoSummaryVO cajaMontoEnero;
	private CajaMontoSummaryVO cajaMontoFebrero;
	private CajaMontoSummaryVO cajaMontoMarzo;
	private CajaMontoSummaryVO cajaMontoAbril;
	private CajaMontoSummaryVO cajaMontoMayo;
	private CajaMontoSummaryVO cajaMontoJunio;
	private CajaMontoSummaryVO cajaMontoJulio;
	private CajaMontoSummaryVO cajaMontoAgosto;
	private CajaMontoSummaryVO cajaMontoSeptiembre;
	private CajaMontoSummaryVO cajaMontoOctubre;
	private CajaMontoSummaryVO cajaMontoNoviembre;
	private CajaMontoSummaryVO cajaMontoDiciembre;
	private Long totalMontos;
	private String color = "#FFB5B5";
	private boolean ignoreColor;

	public SubtituloFlujoCajaVO() {
		super();
		this.cajaMontoEnero = new CajaMontoSummaryVO();
		this.cajaMontoFebrero = new CajaMontoSummaryVO();
		this.cajaMontoMarzo = new CajaMontoSummaryVO();
		this.cajaMontoAbril = new CajaMontoSummaryVO();
		this.cajaMontoMayo = new CajaMontoSummaryVO();
		this.cajaMontoJunio = new CajaMontoSummaryVO();
		this.cajaMontoJulio = new CajaMontoSummaryVO();
		this.cajaMontoAgosto = new CajaMontoSummaryVO();
		this.cajaMontoSeptiembre = new CajaMontoSummaryVO();
		this.cajaMontoOctubre = new CajaMontoSummaryVO();
		this.cajaMontoNoviembre = new CajaMontoSummaryVO();
		this.cajaMontoDiciembre = new CajaMontoSummaryVO();
		this.marcoPresupuestario = 0L;
		this.transferenciaAcumulada = new TransferenciaSummaryVO(0.0, 0L);
		this.ignoreColor = true;
		this.color = "#3ADF00";
		this.pesoComponentes = 0.0;
	}

	
	public SubtituloFlujoCajaVO(Integer idServicio, String servicio,
			Integer idSubtitulo, String subtitulo,
			Integer idMarcoPresupuestario, Long marcoPresupuestario,
			TransferenciaSummaryVO transferenciaAcumulada,
			ConveniosSummaryVO convenioRecibido, Long totalMontos,
			String color) {
		this();
		this.idServicio = idServicio;
		this.servicio = servicio;
		this.idSubtitulo = idSubtitulo;
		this.subtitulo = subtitulo;
		this.idMarcoPresupuestario = idMarcoPresupuestario;
		this.marcoPresupuestario = marcoPresupuestario;
		this.transferenciaAcumulada = transferenciaAcumulada;
		this.convenioRecibido = convenioRecibido;
		this.totalMontos = totalMontos;
		this.color = color;
		this.ignoreColor = true;
		this.color = "#3ADF00";
		this.pesoComponentes = 0.0;
	}


	public Long getMarcoPresupuestario() {
		return marcoPresupuestario;
	}

	public void setMarcoPresupuestario(Long marcoPresupuestario) {
		this.marcoPresupuestario = marcoPresupuestario;
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public Integer getIdSubtitulo() {
		return idSubtitulo;
	}

	public void setIdSubtitulo(Integer idSubtitulo) {
		this.idSubtitulo = idSubtitulo;
	}

	public String getSubtitulo() {
		return subtitulo;
	}

	public void setSubtitulo(String subtitulo) {
		this.subtitulo = subtitulo;
	}

	public TransferenciaSummaryVO getTransferenciaAcumulada() {
		return transferenciaAcumulada;
	}

	public void setTransferenciaAcumulada(
			TransferenciaSummaryVO transferenciaAcumulada) {
		this.transferenciaAcumulada = transferenciaAcumulada;
	}

	public ConveniosSummaryVO getConvenioRecibido() {
		return convenioRecibido;
	}

	public void setConvenioRecibido(ConveniosSummaryVO convenioRecibido) {
		this.convenioRecibido = convenioRecibido;
	}

	public CajaMontoSummaryVO getCajaMontoEnero() {
		return cajaMontoEnero;
	}


	public void setCajaMontoEnero(CajaMontoSummaryVO cajaMontoEnero) {
		this.cajaMontoEnero = cajaMontoEnero;
	}


	public CajaMontoSummaryVO getCajaMontoFebrero() {
		return cajaMontoFebrero;
	}


	public void setCajaMontoFebrero(CajaMontoSummaryVO cajaMontoFebrero) {
		this.cajaMontoFebrero = cajaMontoFebrero;
	}


	public CajaMontoSummaryVO getCajaMontoMarzo() {
		return cajaMontoMarzo;
	}


	public void setCajaMontoMarzo(CajaMontoSummaryVO cajaMontoMarzo) {
		this.cajaMontoMarzo = cajaMontoMarzo;
	}


	public CajaMontoSummaryVO getCajaMontoAbril() {
		return cajaMontoAbril;
	}


	public void setCajaMontoAbril(CajaMontoSummaryVO cajaMontoAbril) {
		this.cajaMontoAbril = cajaMontoAbril;
	}


	public CajaMontoSummaryVO getCajaMontoMayo() {
		return cajaMontoMayo;
	}


	public void setCajaMontoMayo(CajaMontoSummaryVO cajaMontoMayo) {
		this.cajaMontoMayo = cajaMontoMayo;
	}


	public CajaMontoSummaryVO getCajaMontoJunio() {
		return cajaMontoJunio;
	}


	public void setCajaMontoJunio(CajaMontoSummaryVO cajaMontoJunio) {
		this.cajaMontoJunio = cajaMontoJunio;
	}


	public CajaMontoSummaryVO getCajaMontoJulio() {
		return cajaMontoJulio;
	}


	public void setCajaMontoJulio(CajaMontoSummaryVO cajaMontoJulio) {
		this.cajaMontoJulio = cajaMontoJulio;
	}


	public CajaMontoSummaryVO getCajaMontoAgosto() {
		return cajaMontoAgosto;
	}


	public void setCajaMontoAgosto(CajaMontoSummaryVO cajaMontoAgosto) {
		this.cajaMontoAgosto = cajaMontoAgosto;
	}


	public CajaMontoSummaryVO getCajaMontoSeptiembre() {
		return cajaMontoSeptiembre;
	}


	public void setCajaMontoSeptiembre(CajaMontoSummaryVO cajaMontoSeptiembre) {
		this.cajaMontoSeptiembre = cajaMontoSeptiembre;
	}


	public CajaMontoSummaryVO getCajaMontoOctubre() {
		return cajaMontoOctubre;
	}


	public void setCajaMontoOctubre(CajaMontoSummaryVO cajaMontoOctubre) {
		this.cajaMontoOctubre = cajaMontoOctubre;
	}


	public CajaMontoSummaryVO getCajaMontoNoviembre() {
		return cajaMontoNoviembre;
	}


	public void setCajaMontoNoviembre(CajaMontoSummaryVO cajaMontoNoviembre) {
		this.cajaMontoNoviembre = cajaMontoNoviembre;
	}


	public CajaMontoSummaryVO getCajaMontoDiciembre() {
		return cajaMontoDiciembre;
	}


	public void setCajaMontoDiciembre(CajaMontoSummaryVO cajaMontoDiciembre) {
		this.cajaMontoDiciembre = cajaMontoDiciembre;
	}


	public Long getTotalMontos() {
		totalMontos = 0L;
		if(cajaMontoEnero != null){
			totalMontos += cajaMontoEnero.getMontoMes();
		}
		if(cajaMontoFebrero != null){
			totalMontos += cajaMontoFebrero.getMontoMes();
		}
		if(cajaMontoMarzo != null){
			totalMontos += cajaMontoMarzo.getMontoMes();
		}
		if(cajaMontoAbril != null){
			totalMontos += cajaMontoAbril.getMontoMes();
		}
		if(cajaMontoMayo != null){
			totalMontos += cajaMontoMayo.getMontoMes();
		}
		if(cajaMontoJunio != null){
			totalMontos += cajaMontoJunio.getMontoMes();
		}
		if(cajaMontoJulio != null){
			totalMontos += cajaMontoJulio.getMontoMes();
		}
		if(cajaMontoAgosto != null){
			totalMontos += cajaMontoAgosto.getMontoMes();
		}
		if(cajaMontoSeptiembre != null){
			totalMontos += cajaMontoSeptiembre.getMontoMes();
		}
		if(cajaMontoOctubre != null){
			totalMontos += cajaMontoOctubre.getMontoMes();
		}
		if(cajaMontoNoviembre != null){
			totalMontos += cajaMontoNoviembre.getMontoMes();
		}
		if(cajaMontoDiciembre != null){
			totalMontos += cajaMontoDiciembre.getMontoMes();
		}
		return totalMontos;
	}

	public void setTotalMontos(Long totalMontos) {
		this.totalMontos = totalMontos;
	}
	
	public boolean marcoCuadrado(){
		if(!this.marcoPresupuestario.equals(getTotalMontos())){
			return false;
		}
		return true;
	}
	
	public String getColor() {
		System.out.println("getColor-- marcoPresupuestario->"+marcoPresupuestario+" getTotalMontos()="+getTotalMontos());
		if(!isIgnoreColor()){
			if (!marcoCuadrado()){
				System.out.println("no son iguales");
				this.color = "#FF0000";
			}else{
				System.out.println("son iguales");
				this.color = "#3ADF00";
			}
		}
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Integer getIdServicio() {
		return idServicio;
	}


	public void setIdServicio(Integer idServicio) {
		this.idServicio = idServicio;
	}


	public Integer getIdMarcoPresupuestario() {
		return idMarcoPresupuestario;
	}


	public void setIdMarcoPresupuestario(Integer idMarcoPresupuestario) {
		this.idMarcoPresupuestario = idMarcoPresupuestario;
	}

	public boolean isIgnoreColor() {
		return ignoreColor;
	}


	public void setIgnoreColor(boolean ignoreColor) {
		this.ignoreColor = ignoreColor;
	}

	public Double getPesoComponentes() {
		return pesoComponentes;
	}

	public void setPesoComponentes(Double pesoComponentes) {
		this.pesoComponentes = pesoComponentes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((idMarcoPresupuestario == null) ? 0 : idMarcoPresupuestario
						.hashCode());
		result = prime * result
				+ ((idServicio == null) ? 0 : idServicio.hashCode());
		result = prime * result
				+ ((idSubtitulo == null) ? 0 : idSubtitulo.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SubtituloFlujoCajaVO other = (SubtituloFlujoCajaVO) obj;
		if (idMarcoPresupuestario == null) {
			if (other.idMarcoPresupuestario != null)
				return false;
		} else if (!idMarcoPresupuestario.equals(other.idMarcoPresupuestario))
			return false;
		if (idServicio == null) {
			if (other.idServicio != null)
				return false;
		} else if (!idServicio.equals(other.idServicio))
			return false;
		if (idSubtitulo == null) {
			if (other.idSubtitulo != null)
				return false;
		} else if (!idSubtitulo.equals(other.idSubtitulo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SubtituloFlujoCajaVO [idServicio=" + idServicio + ", servicio="
				+ servicio + ", idSubtitulo=" + idSubtitulo + ", subtitulo="
				+ subtitulo + ", idMarcoPresupuestario="
				+ idMarcoPresupuestario + ", marcoPresupuestario="
				+ marcoPresupuestario + ", transferenciaAcumulada="
				+ transferenciaAcumulada + ", convenioRecibido="
				+ convenioRecibido + ", cajaMontoEnero=" + cajaMontoEnero
				+ ", cajaMontoFebrero=" + cajaMontoFebrero
				+ ", cajaMontoMarzo=" + cajaMontoMarzo + ", cajaMontoAbril="
				+ cajaMontoAbril + ", cajaMontoMayo=" + cajaMontoMayo
				+ ", cajaMontoJunio=" + cajaMontoJunio + ", cajaMontoJulio="
				+ cajaMontoJulio + ", cajaMontoAgosto=" + cajaMontoAgosto
				+ ", cajaMontoSeptiembre=" + cajaMontoSeptiembre
				+ ", cajaMontoOctubre=" + cajaMontoOctubre
				+ ", cajaMontoNoviembre=" + cajaMontoNoviembre
				+ ", cajaMontoDiciembre=" + cajaMontoDiciembre
				+ ", totalMontos=" + totalMontos + ", color=" + color
				+ ", ignoreColor=" + ignoreColor + "]";
	}


	public List<Object> getRow() {
		List<Object> row = new ArrayList<Object>();
		if(getServicio() != null){
			row.add(getServicio());			
		}
		if(cajaMontoEnero != null){
			row.add(cajaMontoEnero.getMontoMes());
		}
		if(cajaMontoFebrero != null){
			row.add(cajaMontoFebrero.getMontoMes());
		}
		if(cajaMontoMarzo != null){
			row.add(cajaMontoMarzo.getMontoMes());
		}
		if(cajaMontoAbril != null){
			row.add(cajaMontoAbril.getMontoMes());
		}
		if(cajaMontoMayo != null){
			row.add(cajaMontoMayo.getMontoMes());
		}
		if(cajaMontoJunio != null){
			row.add(cajaMontoJunio.getMontoMes());
		}
		if(cajaMontoJulio != null){
			row.add(cajaMontoJulio.getMontoMes());
		}
		if(cajaMontoAgosto != null){
			row.add(cajaMontoAgosto.getMontoMes());
		}
		if(cajaMontoSeptiembre != null){
			row.add(cajaMontoSeptiembre.getMontoMes());
		}
		if(cajaMontoOctubre != null){
			row.add(cajaMontoOctubre.getMontoMes());
		}
		if(cajaMontoNoviembre != null){
			row.add(cajaMontoNoviembre.getMontoMes());
		}
		if(cajaMontoDiciembre != null){
			row.add(cajaMontoDiciembre.getMontoMes());
		}
		row.add(getTotalMontos());	
		return row;
	}

}

