package minsal.divap.vo;

import java.util.List;

import cl.minsal.divap.pojo.EstimacionFlujoMonitoreoGlobalPojo;


public class CajaGlobalVO implements Cloneable {

	 @Override 
	 public CajaGlobalVO clone() {
	        try {
	            final CajaGlobalVO result = (CajaGlobalVO) super.clone();
	            // copy fields that need to be copied here!
	            return result;
	        } catch (final CloneNotSupportedException ex) {
	            throw new AssertionError();
	        }
	 }
	private long totalMarcoMonto;
	/*Se obtienes desde otros procesos*/
	private long totalTransferenciaMonto;
	private long totalRemesaMonto;
	private long totalConvenioMonto;

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
	
	private List<CajaVO> caja;

	public long getTotalMarcoMonto() {
		totalMarcoMonto = 0;
		for (CajaVO e : caja) {
			totalMarcoMonto += e.getMarcoMonto();
		}
		
		return totalMarcoMonto;
	}

	public void setTotalMarcoMonto(long totalMarcoMonto) {
		this.totalMarcoMonto = totalMarcoMonto;
	}

	public long getTotalTransferenciaMonto() {
		
		totalTransferenciaMonto =0;
		for (CajaVO e : caja) {
			total += e.getTransferenciaMonto();
		}
		
		return totalTransferenciaMonto;
	}

	public void setTotalTransferenciaMonto(long totalTransferenciaMonto) {
		this.totalTransferenciaMonto = totalTransferenciaMonto;
	}

	public long getTotalRemesaMonto() {
		totalRemesaMonto = 0;
		for (CajaVO e : caja) {
			totalRemesaMonto += e.getRemesaMonto();
		}
		return totalRemesaMonto;
	}

	public void setTotalRemesaMonto(long totalRemesaMonto) {
		this.totalRemesaMonto = totalRemesaMonto;
	}

	public long getTotalConvenioMonto() {
		totalConvenioMonto = 0;
		for (CajaVO e : caja) {
			totalConvenioMonto += e.getConvenioMonto();
		}
		
		return totalConvenioMonto;
	}

	public void setTotalConvenioMonto(long totalConvenioMonto) {
		this.totalConvenioMonto = totalConvenioMonto;
	}

	public long getEnero() {
		enero = 0;
		for (CajaVO e : caja) {
			enero += e.getEnero();
		}
		
		return enero;
	}

	public void setEnero(long enero) {
		this.enero = enero;
	}

	public long getFebrero() {
		febrero = 0;
		for (CajaVO e : caja) {
			febrero += e.getFebrero();
		}
		return febrero;
	}

	public void setFebrero(long febrero) {
		this.febrero = febrero;
	}

	public long getMarzo() {
		marzo = 0;
		for (CajaVO e : caja) {
			marzo += e.getMarzo();
		}
		return marzo;
	}

	public void setMarzo(long marzo) {
		this.marzo = marzo;
	}

	public long getAbril() {
		abril = 0;
		for (CajaVO e : caja) {
			abril += e.getAbril();
		}
		return abril;
	}

	public void setAbril(long abril) {
		this.abril = abril;
	}

	public long getMayo() {
		mayo = 0;
		for (CajaVO e : caja) {
			mayo += e.getMayo();
		}
		return mayo;
	}

	public void setMayo(long mayo) {
		this.mayo = mayo;
	}

	public long getJunio() {
		junio = 0;
		for (CajaVO e : caja) {
			junio += e.getJunio();
		}
		return junio;
	}

	public void setJunio(long junio) {
		this.junio = junio;
	}

	public long getJulio() {
		julio = 0;
		for (CajaVO e : caja) {
			julio += e.getJulio();
		}
		return julio;
	}

	public void setJulio(long julio) {
		this.julio = julio;
	}

	public long getAgosto() {
		
		agosto = 0;
		for (CajaVO e : caja) {
			agosto += e.getAgosto();
		}
		return agosto;
	}

	public void setAgosto(long agosto) {
		this.agosto = agosto;
	}

	public long getSeptiembre() {
		septiembre = 0;
		for (CajaVO e : caja) {
			septiembre += e.getSeptiembre();
		}
		return septiembre;
	}

	public void setSeptiembre(long septiembre) {
		this.septiembre = septiembre;
	}

	public long getOctubre() {
		octubre = 0;
		for (CajaVO e : caja) {
			octubre += e.getOctubre();
		}
		return octubre;
	}

	public void setOctubre(long octubre) {
		this.octubre = octubre;
	}

	public long getNoviembre() {
		noviembre = 0;
		for (CajaVO e : caja) {
			noviembre += e.getNoviembre();
		}
		return noviembre;
	}

	public void setNoviembre(long noviembre) {
		this.noviembre = noviembre;
	}

	public long getDiciembre() {
		diciembre = 0;
		for (CajaVO e : caja) {
			diciembre += e.getDiciembre();
		}
		return diciembre;
	}

	public void setDiciembre(long diciembre) {
		this.diciembre = diciembre;
	}

	public long getTotal() {
		total = 0;
		for (CajaVO e : caja) {
			total += e.getTotal();
		}
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<CajaVO> getCaja() {
		return caja;
	}

	public void setCaja(
			List<CajaVO> caja) {
		this.caja = caja;
	}
}
