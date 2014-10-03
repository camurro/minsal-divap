package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FlujoCajaVO implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8523129972295183486L;
	private String servicio;
	private Integer montoSubtitulo;
	private List<CajaMesVO> subtitulo;
	
	
	public String getServicio() {
		return servicio;
	}
	public void setServicio(String servicio) {
		this.servicio = servicio;
	}
	public Integer getMontoSubtitulo() {
		return montoSubtitulo;
	}
	public void setMontoSubtitulo(Integer montoSubtitulo) {
		this.montoSubtitulo = montoSubtitulo;
	}
	public List<CajaMesVO> getSubtitulo() {
		return subtitulo;
	}
	public void setSubtitulo(List<CajaMesVO> subtitulo) {
		this.subtitulo = subtitulo;
	}
	@Override
	public String toString() {
		return "FlujoCajaVO [servicio=" + servicio + ", montoSubtitulo="
				+ montoSubtitulo + ", subtitulo=" + subtitulo + "]";
	}
	public List<Object> getRow() {
		List<Object> row = new ArrayList<Object>();
		if(getServicio() != null){
			row.add(getServicio());			
		}
		if(!getSubtitulo().isEmpty()){
			for(int i=0; i<getSubtitulo().size(); i++){
				row.add(getSubtitulo().get(i).getMonto());
			}
		}
		return row;
	}
	
	
}
