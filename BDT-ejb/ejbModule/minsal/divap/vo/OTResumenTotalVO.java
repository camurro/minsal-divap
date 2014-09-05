package minsal.divap.vo;


public class OTResumenTotalVO 
{
	private ServiciosVO servicioSaludVO;
	private long idProgramaAno;
	private long subtitulo21Total;
	private long subtitulo22Total;
	private long subtitulo29Total;
	private long subtitulo24Total;
	private long total;
	
	
	
	
	public ServiciosVO getServicioSaludVO() {
		return servicioSaludVO;
	}
	public void setServicioSaludVO(ServiciosVO servicioSaludVO) {
		this.servicioSaludVO = servicioSaludVO;
	}

	public long getSubtitulo21Total() {
		return subtitulo21Total;
	}
	public void setSubtitulo21Total(long subtitulo21Total) {
		this.subtitulo21Total = subtitulo21Total;
	}
	public long getSubtitulo22Total() {
		return subtitulo22Total;
	}
	public void setSubtitulo22Total(long subtitulo22Total) {
		this.subtitulo22Total = subtitulo22Total;
	}
	public long getSubtitulo29Total() {
		return subtitulo29Total;
	}
	public void setSubtitulo29Total(long subtitulo29Total) {
		this.subtitulo29Total = subtitulo29Total;
	}
	public long getSubtitulo24Total() {
		return subtitulo24Total;
	}
	public void setSubtitulo24Total(long subtitulo24Total) {
		this.subtitulo24Total = subtitulo24Total;
	}
	public long getIdProgramaAno() {
		return idProgramaAno;
	}
	public void setIdProgramaAno(long idProgramaAno) {
		this.idProgramaAno = idProgramaAno;
	}
	public long getTotal() {
		return subtitulo21Total + subtitulo22Total+subtitulo29Total+subtitulo24Total;
	}
	public void setTotal(long total) {
		this.total = total;
	}

	
	
	
}