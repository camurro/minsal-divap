package minsal.divap.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ResumenProgramaMixtoVO implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 8342581230266634287L;
	private Integer idServicio;
	private String nombreServicio;
	private Long totalS21;
	private Long totalS22;
	private Long totalS24;
	private Long totalS29;
	private Long totalServicio;
	
	
	public Integer getIdServicio() {
		return idServicio;
	}
	public void setIdServicio(Integer idServicio) {
		this.idServicio = idServicio;
	}
	public String getNombreServicio() {
		return nombreServicio;
	}
	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}
	public Long getTotalS21() {
		return totalS21;
	}
	public void setTotalS21(Long totalS21) {
		this.totalS21 = totalS21;
	}
	public Long getTotalS22() {
		return totalS22;
	}
	public void setTotalS22(Long totalS22) {
		this.totalS22 = totalS22;
	}
	public Long getTotalS29() {
		return totalS29;
	}
	public void setTotalS29(Long totalS29) {
		this.totalS29 = totalS29;
	}
	
	public Long getTotalServicio() {
		totalServicio=0L;
		if(totalS21!=null){
			totalServicio += totalS21;
		}
		if(totalS22!=null){
			totalServicio += totalS22;
		}
		if(totalS24!=null){
			totalServicio += totalS24;
		}
		if(totalS29!=null){
			totalServicio += totalS29;
		}
		return totalServicio;
	}
	public void setTotalServicio(Long totalServicio) {
		this.totalServicio = totalServicio;
	}
	
	
	public Long getTotalS24() {
		return totalS24;
	}
	public void setTotalS24(Long totalS24) {
		this.totalS24 = totalS24;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResumenProgramaMixtoVO other = (ResumenProgramaMixtoVO) obj;
		if (idServicio == null) {
			if (other.idServicio != null)
				return false;
		} else if (!idServicio.equals(other.idServicio))
			return false;
		return true;
	}
	
	public List<Object> getRow() {
		List<Object> row = new ArrayList<Object>();
		
		if(getIdServicio() != null){
			row.add(getIdServicio()) ;
		}
		if(getNombreServicio() != null){
			row.add(getNombreServicio());
		}
		if(getTotalS21() != null && getTotalS21()> 0){
			row.add(getTotalS21());
		}
		if(getTotalS22() != null && getTotalS22()> 0){
			row.add(getTotalS22());
		}
		if(getTotalS24() != null && getTotalS24()> 0){
			row.add(getTotalS24());
		}
		if(getTotalS29() != null && getTotalS29()> 0){
			row.add(getTotalS29());
		}
		if(getTotalServicio()!=null && getTotalServicio()>0){
			row.add(getTotalServicio());
		}
		return row;
	}
	
	
	
	
}
