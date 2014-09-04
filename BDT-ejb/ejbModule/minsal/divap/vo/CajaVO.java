package minsal.divap.vo;

import java.util.Comparator;

import cl.minsal.divap.pojo.ComponentePojo;
import cl.minsal.divap.pojo.EstimacionFlujoMonitoreoPojo;

public class CajaVO  {

	private float pesoComponente;
	private int id;
	private String servicio;
	private int idServicio;
	
	private String comuna;
	private int idComuna;
	private long marcoMonto;
	private long transferenciaMonto;
	private float transferenciaPorcentaje;
	
	private long remesaMonto;
	private float remesaPorcentaje;
	private long convenioMonto;
	private float convenioPorcentaje;
	
	
	private String establecimiento;
	private int idEstablecimiento;
	
	
	private ComponentesVO componente;
	
	

	private long enero;
	private long febrero;
	private long marzo;
	private long abril; 
	private long mayo; 
	private long junio; 
	private long julio; 
	private long agosto; 
	private long septiembre; 
	private long octubre;
	private long noviembre; 
	private long diciembre; 
	private long total;
	
	
	
	private String color = "#FFB5B5";



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getServicio() {
		return servicio;
	}



	public void setServicio(String servicio) {
		this.servicio = servicio;
	}



	public String getComuna() {
		return comuna;
	}



	public void setComuna(String comuna) {
		this.comuna = comuna;
	}



	public long getMarcoMonto() {
		return marcoMonto + (long)(Float.valueOf(marcoMonto)*pesoComponente);
	}



	public void setMarcoMonto(long marcoMonto) {
		this.marcoMonto = marcoMonto;
	}



	public long getTransferenciaMonto() {
		return transferenciaMonto +  (long)(Float.valueOf(transferenciaMonto)*pesoComponente);
	}



	public void setTransferenciaMonto(long transferenciaMonto) {
		this.transferenciaMonto = transferenciaMonto;
	}



	public float getTransferenciaPorcentaje() {
		return transferenciaPorcentaje;
	}



	public void setTransferenciaPorcentaje(float transferenciaPorcentaje) {
		this.transferenciaPorcentaje = transferenciaPorcentaje;
	}



	public long getRemesaMonto() {
		return remesaMonto +  (long)(Float.valueOf(remesaMonto)*pesoComponente);
	}



	public void setRemesaMonto(long remesaMonto) {
		this.remesaMonto = remesaMonto;
	}



	public float getRemesaPorcentaje() {
		return remesaPorcentaje;
	}



	public void setRemesaPorcentaje(float remesaPorcentaje) {
		this.remesaPorcentaje = remesaPorcentaje;
	}



	public long getConvenioMonto() {
		return convenioMonto +  (long)(Float.valueOf(convenioMonto)*pesoComponente);
	}



	public void setConvenioMonto(long convenioMonto) {
		this.convenioMonto = convenioMonto;
	}



	public float getConvenioPorcentaje() {
		return convenioPorcentaje;
	}



	public void setConvenioPorcentaje(float convenioPorcentaje) {
		this.convenioPorcentaje = convenioPorcentaje;
	}



	public String getEstablecimiento() {
		return establecimiento;
	}



	public void setEstablecimiento(String establecimiento) {
		this.establecimiento = establecimiento;
	}



	public ComponentesVO getComponente() {
		return componente;
	}



	public void setComponente(ComponentesVO componente) {
		this.componente = componente;
	}



	public long getEnero() {
		return enero +  (long)(Float.valueOf(enero)*pesoComponente);
	}



	public void setEnero(long enero) {
		this.enero = enero;
	}



	public long getFebrero() {
		return febrero +  (long)(Float.valueOf(febrero)*pesoComponente);
	}



	public void setFebrero(long febrero) {
		this.febrero = febrero;
	}



	public long getMarzo() {
		return marzo +  (long)(Float.valueOf(marzo)*pesoComponente);
	}



	public void setMarzo(long marzo) {
		this.marzo = marzo;
	}



	public long getAbril() {
		return abril +  (long)(Float.valueOf(abril)*pesoComponente) ;
	}



	public void setAbril(long abril) {
		this.abril = abril;
	}



	public long getMayo() {
		return mayo +  (long)(Float.valueOf(mayo)*pesoComponente) ;
	}



	public void setMayo(long mayo) {
		this.mayo = mayo;
	}



	public long getJunio() {
		return junio +  (long)(Float.valueOf(junio)*pesoComponente);
	}



	public void setJunio(long junio) {
		this.junio = junio;
	}



	public long getJulio() {
		return julio +  (long)(Float.valueOf(julio)*pesoComponente);
	}



	public void setJulio(long julio) {
		this.julio = julio;
	}



	public long getAgosto() {
		return agosto +  (long)(Float.valueOf(agosto)*pesoComponente);
	}



	public void setAgosto(long agosto) {
		this.agosto = agosto;
	}



	public long getSeptiembre() {
		return septiembre +  (long)(Float.valueOf(septiembre)*pesoComponente);
	}



	public void setSeptiembre(long septiembre) {
		this.septiembre = septiembre;
	}



	public long getOctubre() {
		return octubre +  (long)(Float.valueOf(octubre)*pesoComponente);
	}



	public void setOctubre(long octubre) {
		this.octubre = octubre;
	}



	public long getNoviembre() {
		return noviembre +  (long)(Float.valueOf(noviembre)*pesoComponente);
	}



	public void setNoviembre(long noviembre) {
		this.noviembre = noviembre;
	}



	public long getDiciembre() {
		return diciembre +  (long)(Float.valueOf(diciembre)*pesoComponente);
	}



	public void setDiciembre(long diciembre) {
		this.diciembre = diciembre;
	}



	public long getTotal() {
		total = (enero+febrero+marzo+abril+mayo+junio+julio+agosto+septiembre+octubre+noviembre+diciembre);
		total = total +  (long)(Float.valueOf(total)*pesoComponente);
		return total;
	}



	public void setTotal(long total) {
		this.total = total;
	}



	public String getColor() {
		if (marcoMonto != getTotal())
			color = "#FF0000";
		else
			color = "#3ADF00";
		return color;
	}



	public void setColor(String color) {
		this.color = color;
	}
	
//	public static class OrderByAmount implements Comparator<EstimacionFlujoMonitoreoPojo> {
//
//        @Override
//        public int compare(EstimacionFlujoMonitoreoPojo o1, EstimacionFlujoMonitoreoPojo o2) {
//            return o1.comuna.compareTo(o2.comuna);
//        }
//    }
	
//	@Override
//    public int compareTo(EstimacionFlujoMonitoreoPojo o) {
//        return this.getId() > o.getId() ? 1 : (this.id < o.id ? -1 : 0);
//    }
  
    public int getIdServicio() {
		return idServicio;
	}



	public void setIdServicio(int idServicio) {
		this.idServicio = idServicio;
	}



	public int getIdComuna() {
		return idComuna;
	}



	public void setIdComuna(int idComuna) {
		this.idComuna = idComuna;
	}



	public int getIdEstablecimiento() {
		return idEstablecimiento;
	}



	public void setIdEstablecimiento(int idEstablecimiento) {
		this.idEstablecimiento = idEstablecimiento;
	}



	/*
     * implementing toString method to print orderId of Order
     */
    @Override
    public String toString(){
        return String.valueOf(id);
    }



	public float getPesoComponente() {
		return pesoComponente;
	}



	public void setPesoComponente(float pesoComponente) {
		this.pesoComponente = pesoComponente;
	}
	

	
}

