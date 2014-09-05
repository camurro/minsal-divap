package minsal.divap.vo;

import cl.minsal.divap.model.Comuna;
import cl.minsal.divap.model.Establecimiento;
import cl.minsal.divap.model.ProgramaAno;
import cl.minsal.divap.model.ServicioSalud;
import cl.minsal.divap.pojo.ComponentePojo;

public class OTRevisarAntecedentesVO
{
	private long id;
	private Integer idRemesaMesEnero;
	private Integer idRemesaMesFebrero;
	private Integer idRemesaMesMarzo;
	private Integer idRemesaMesAbril;
	private Integer idRemesaMesMayo;
	private Integer idRemesaMesJunio;
	private Integer idRemesaMesJulio;
	private Integer idRemesaMesAgosto;
	private Integer idRemesaMesSeptiembre;
	private Integer idRemesaMesOctubre;
	private Integer idRemesaMesNoviembre;
	private Integer idRemesaMesDiciembre;
	private Integer idEstablecimiento;
	private Integer idServicioSalud;
	private Integer idComuna;
	
	private ServicioSalud servicioSalud;
	private Comuna comuna;
	private Establecimiento establecimiento ;
	
	private long marcoPresupuestarioMonto;
	private long acumuladoFechaMonto;
	private String acumuladoFechaPorcentaje;
	private long convenioMonto;
	private String convenioPorcentaje;
	
	private long diferenciaMarcoContraTotalMonto;
	private String diferenciaMarcoContraTotalPorcentaje;
	private ProgramaAno programaAno;
	
	public ProgramaAno getProgramaAno() {
		return programaAno;
	}

	public void setProgramaAno(ProgramaAno programaAno) {
		this.programaAno = programaAno;
	}

	public String getConvenioPorcentaje() {
		return convenioPorcentaje;
	}

	public void setConvenioPorcentaje(String convenioPorcentaje) {
		this.convenioPorcentaje = convenioPorcentaje;
	}

	public long getMarcoPresupuestarioMonto() {
		return marcoPresupuestarioMonto;
	}

	public void setMarcoPresupuestarioMonto(long marcoPresupuestarioMonto) {
		this.marcoPresupuestarioMonto = marcoPresupuestarioMonto;
	}

	public long getAcumuladoFechaMonto() {
		return acumuladoFechaMonto;
	}

	public void setAcumuladoFechaMonto(long acumuladoFechaMonto) {
		this.acumuladoFechaMonto = acumuladoFechaMonto;
	}

	public String getAcumuladoFechaPorcentaje() {
		return acumuladoFechaPorcentaje;
	}

	public void setAcumuladoFechaPorcentaje(String acumuladoFechaPorcentaje) {
		this.acumuladoFechaPorcentaje = acumuladoFechaPorcentaje;
	}

	public long getDiferenciaMarcoContraTotalMonto() {
		return diferenciaMarcoContraTotalMonto;
	}

	public void setDiferenciaMarcoContraTotalMonto(
			long diferenciaMarcoContraTotalMonto) {
		this.diferenciaMarcoContraTotalMonto = diferenciaMarcoContraTotalMonto;
	}

	public String getDiferenciaMarcoContraTotalPorcentaje() {
		return diferenciaMarcoContraTotalPorcentaje;
	}

	public void setDiferenciaMarcoContraTotalPorcentaje(
			String diferenciaMarcoContraTotalPorcentaje) {
		this.diferenciaMarcoContraTotalPorcentaje = diferenciaMarcoContraTotalPorcentaje;
	}

	public ServicioSalud getServicioSalud() {
		return servicioSalud;
	}

	public void setServicioSalud(ServicioSalud servicioSalud) {
		this.servicioSalud = servicioSalud;
	}

	public Comuna getComuna() {
		return comuna;
	}

	public void setComuna(Comuna comuna) {
		this.comuna = comuna;
	}

	public Establecimiento getEstablecimiento() {
		return establecimiento;
	}

	public void setEstablecimiento(Establecimiento establecimiento) {
		this.establecimiento = establecimiento;
	}

	private ComponentePojo componente;
	private Integer anio;
	
	
	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public ComponentePojo getComponente() {
		return componente;
	}
	
	public void setComponente( ComponentePojo componente ) {
		this.componente = componente;
	}
	
	public Integer getIdRemesaMesEnero() {
		return idRemesaMesEnero;
	}

	public void setIdRemesaMesEnero(Integer idRemesaMesEnero) {
		this.idRemesaMesEnero = idRemesaMesEnero;
	}

	public Integer getIdRemesaMesFebrero() {
		return idRemesaMesFebrero;
	}

	public void setIdRemesaMesFebrero(Integer idRemesaMesFebrero) {
		this.idRemesaMesFebrero = idRemesaMesFebrero;
	}

	public Integer getIdRemesaMesMarzo() {
		return idRemesaMesMarzo;
	}

	public void setIdRemesaMesMarzo(Integer idRemesaMesMarzo) {
		this.idRemesaMesMarzo = idRemesaMesMarzo;
	}

	public Integer getIdRemesaMesAbril() {
		return idRemesaMesAbril;
	}

	public void setIdRemesaMesAbril(Integer idRemesaMesAbril) {
		this.idRemesaMesAbril = idRemesaMesAbril;

	}

	public Integer getIdRemesaMesMayo() {
		return idRemesaMesMayo;
	}

	public void setIdRemesaMesMayo(Integer idRemesaMesMayo) {
		this.idRemesaMesMayo = idRemesaMesMayo;
	}

	public Integer getIdRemesaMesJunio() {
		return idRemesaMesJunio;
	}

	public void setIdRemesaMesJunio(Integer idRemesaMesJunio) {
		this.idRemesaMesJunio = idRemesaMesJunio;
	}

	public Integer getIdRemesaMesJulio() {
		return idRemesaMesJulio;
	}

	public void setIdRemesaMesJulio(Integer idRemesaMesJulio) {
		this.idRemesaMesJulio = idRemesaMesJulio;
	}

	public Integer getIdRemesaMesAgosto() {
		return idRemesaMesAgosto;
	}

	public void setIdRemesaMesAgosto(Integer idRemesaMesAgosto) {
		this.idRemesaMesAgosto = idRemesaMesAgosto;
	}

	public Integer getIdRemesaMesSeptiembre() {
		return idRemesaMesSeptiembre;
	}

	public void setIdRemesaMesSeptiembre(Integer idRemesaMesSeptiembre) {
		this.idRemesaMesSeptiembre = idRemesaMesSeptiembre;
	}

	public Integer getIdRemesaMesOctubre() {
		return idRemesaMesOctubre;
	}

	public void setIdRemesaMesOctubre(Integer idRemesaMesOctubre) {
		this.idRemesaMesOctubre = idRemesaMesOctubre;
	}

	public Integer getIdRemesaMesNoviembre() {
		return idRemesaMesNoviembre;
	}

	public void setIdRemesaMesNoviembre(Integer idRemesaMesNoviembre) {
		this.idRemesaMesNoviembre = idRemesaMesNoviembre;
	}

	public Integer getIdRemesaMesDiciembre() {
		return idRemesaMesDiciembre;
	}

	public void setIdRemesaMesDiciembre(Integer idRemesaMesDiciembre) {
		this.idRemesaMesDiciembre = idRemesaMesDiciembre;
	}

	private long eneroRemesa09 =0;
	private long eneroRemesa24 =0;
	private long eneroRemesa28 =0;

	private long febreroRemesa09 =0;
	private long febreroRemesa24 =0;
	private long febreroRemesa28 =0;
	
	private long marzoRemesa09 =0;
	private long marzoRemesa24 =0;
	private long marzoRemesa28 =0;
	
	private long abrilRemesa09 =0;
	private long abrilRemesa24 =0;
	private long abrilRemesa28 =0;
	
	private long mayoRemesa09 =0;
	private long mayoRemesa24 =0;
	private long mayoRemesa28 =0;
	
	private long junioRemesa09 =0;
	private long junioRemesa24 =0;
	private long junioRemesa28 =0;

	private long julioRemesa09 =0;
	private long julioRemesa24 = 0;
	private long julioRemesa28 = 0;

	private long agostoRemesa09 = 0;
	private long agostoRemesa24 = 0;
	private long agostoRemesa28 = 0;
	
	private long septiembreRemesa09 =0;
	private long septiembreRemesa24 =0;
	private long septiembreRemesa28 =0;
	
	private long octubreRemesa09 =0;
	private long octubreRemesa24 =0;
	private long octubreRemesa28 =0;
	
	private long noviembreRemesa09 =0;
	private long noviembreRemesa24 =0;
	private long noviembreRemesa28 =0;
	
	private long diciembreRemesa09 =0;
	private long diciembreRemesa24 =0;
	private long diciembreRemesa28 =0;
	
	//check box 
	private boolean valorCheckBox;
	
	public boolean isValorCheckBox() {
		return valorCheckBox;
	}

	public void setValorCheckBox(boolean valorCheckBox) {
		this.valorCheckBox = valorCheckBox;
	}

	public long getEneroRemesa09() {
		return eneroRemesa09;
	}

	public void setEneroRemesa09(long eneroRemesa09) {
		this.eneroRemesa09 = eneroRemesa09;
	}

	public long getEneroRemesa24() {
		return eneroRemesa24;
	}

	public void setEneroRemesa24(long eneroRemesa24) {
		this.eneroRemesa24 = eneroRemesa24;
	}

	public long getEneroRemesa28() {
		return eneroRemesa28;
	}

	public void setEneroRemesa28(long eneroRemesa28) {
		this.eneroRemesa28 = eneroRemesa28;
	}

	public long getFebreroRemesa09() {
		return febreroRemesa09;
	}

	public void setFebreroRemesa09(long febreroRemesa09) {
		this.febreroRemesa09 = febreroRemesa09;
	}

	public long getFebreroRemesa24() {
		return febreroRemesa24;
	}

	public void setFebreroRemesa24(long febreroRemesa24) {
		this.febreroRemesa24 = febreroRemesa24;
	}

	public long getFebreroRemesa28() {
		return febreroRemesa28;
	}

	public void setFebreroRemesa28(long febreroRemesa28) {
		this.febreroRemesa28 = febreroRemesa28;
	}

	public long getMarzoRemesa09() {
		return marzoRemesa09;
	}

	public void setMarzoRemesa09(long marzoRemesa09) {
		this.marzoRemesa09 = marzoRemesa09;
	}

	public long getMarzoRemesa24() {
		return marzoRemesa24;
	}

	public void setMarzoRemesa24(long marzoRemesa24) {
		this.marzoRemesa24 = marzoRemesa24;
	}

	public long getMarzoRemesa28() {
		return marzoRemesa28;
	}

	public void setMarzoRemesa28(long marzoRemesa28) {
		this.marzoRemesa28 = marzoRemesa28;
	}

	public long getAbrilRemesa09() {
		return abrilRemesa09;
	}

	public void setAbrilRemesa09(long abrilRemesa09) {
		this.abrilRemesa09 = abrilRemesa09;
	}

	public long getAbrilRemesa24() {
		return abrilRemesa24;
	}

	public void setAbrilRemesa24(long abrilRemesa24) {
		this.abrilRemesa24 = abrilRemesa24;
	}

	public long getAbrilRemesa28() {
		return abrilRemesa28;
	}

	public void setAbrilRemesa28(long abrilRemesa28) {
		this.abrilRemesa28 = abrilRemesa28;
	}

	public long getMayoRemesa09() {
		return mayoRemesa09;
	}

	public void setMayoRemesa09(long mayoRemesa09) {
		this.mayoRemesa09 = mayoRemesa09;
	}

	public long getMayoRemesa24() {
		return mayoRemesa24;
	}

	public void setMayoRemesa24(long mayoRemesa24) {
		this.mayoRemesa24 = mayoRemesa24;
	}

	public long getMayoRemesa28() {
		return mayoRemesa28;
	}

	public void setMayoRemesa28(long mayoRemesa28) {
		this.mayoRemesa28 = mayoRemesa28;
	}

	public long getJunioRemesa09() {
		return junioRemesa09;
	}

	public void setJunioRemesa09(long junioRemesa09) {
		this.junioRemesa09 = junioRemesa09;
	}

	public long getJunioRemesa24() {
		return junioRemesa24;
	}

	public void setJunioRemesa24(long junioRemesa24) {
		this.junioRemesa24 = junioRemesa24;
	}

	public long getJunioRemesa28() {
		return junioRemesa28;
	}

	public void setJunioRemesa28(long junioRemesa28) {
		this.junioRemesa28 = junioRemesa28;
	}

	public long getJulioRemesa09() {
		return julioRemesa09;
	}

	public void setJulioRemesa09(long julioRemesa09) {
		this.julioRemesa09 = julioRemesa09;
	}

	public long getJulioRemesa24() {
		return julioRemesa24;
	}

	public void setJulioRemesa24(long julioRemesa24) {
		this.julioRemesa24 = julioRemesa24;
	}

	public long getJulioRemesa28() {
		return julioRemesa28;
	}

	public void setJulioRemesa28(long julioRemesa28) {
		this.julioRemesa28 = julioRemesa28;
	}

	public long getAgostoRemesa09() {
		return agostoRemesa09;
	}

	public void setAgostoRemesa09(long agostoRemesa09) {
		this.agostoRemesa09 = agostoRemesa09;
	}

	public long getAgostoRemesa24() {
		return agostoRemesa24;
	}

	public void setAgostoRemesa24(long agostoRemesa24) {
		this.agostoRemesa24 = agostoRemesa24;
	}

	public long getAgostoRemesa28() {
		return agostoRemesa28;
	}

	public void setAgostoRemesa28(long agostoRemesa28) {
		this.agostoRemesa28 = agostoRemesa28;
	}

	public long getSeptiembreRemesa09() {
		return septiembreRemesa09;
	}

	public void setSeptiembreRemesa09(long septiembreRemesa09) {
		this.septiembreRemesa09 = septiembreRemesa09;
	}

	public long getSeptiembreRemesa24() {
		return septiembreRemesa24;
	}

	public void setSeptiembreRemesa24(long septiembreRemesa24) {
		this.septiembreRemesa24 = septiembreRemesa24;
	}

	public long getSeptiembreRemesa28() {
		return septiembreRemesa28;
	}

	public void setSeptiembreRemesa28(long septiembreRemesa28) {
		this.septiembreRemesa28 = septiembreRemesa28;
	}

	public long getOctubreRemesa09() {
		return octubreRemesa09;
	}

	public void setOctubreRemesa09(long octubreRemesa09) {
		this.octubreRemesa09 = octubreRemesa09;
	}

	public long getOctubreRemesa24() {
		return octubreRemesa24;
	}

	public void setOctubreRemesa24(long octubreRemesa24) {
		this.octubreRemesa24 = octubreRemesa24;
	}

	public long getOctubreRemesa28() {
		return octubreRemesa28;
	}

	public void setOctubreRemesa28(long octubreRemesa28) {
		this.octubreRemesa28 = octubreRemesa28;
	}

	public long getNoviembreRemesa09() {
		return noviembreRemesa09;
	}

	public void setNoviembreRemesa09(long noviembreRemesa09) {
		this.noviembreRemesa09 = noviembreRemesa09;
	}

	public long getNoviembreRemesa24() {
		return noviembreRemesa24;
	}

	public void setNoviembreRemesa24(long noviembreRemesa24) {
		this.noviembreRemesa24 = noviembreRemesa24;
	}

	public long getNoviembreRemesa28() {
		return noviembreRemesa28;
	}

	public void setNoviembreRemesa28(long noviembreRemesa28) {
		this.noviembreRemesa28 = noviembreRemesa28;
	}

	public long getDiciembreRemesa09() {
		return diciembreRemesa09;
	}

	public void setDiciembreRemesa09(long diciembreRemesa09) {
		this.diciembreRemesa09 = diciembreRemesa09;
	}

	public long getDiciembreRemesa24() {
		return diciembreRemesa24;
	}

	public void setDiciembreRemesa24(long diciembreRemesa24) {
		this.diciembreRemesa24 = diciembreRemesa24;
	}

	public long getDiciembreRemesa28() {
		return diciembreRemesa28;
	}

	public void setDiciembreRemesa28(long diciembreRemesa28) {
		this.diciembreRemesa28 = diciembreRemesa28;
	}

	private long s24Total = 1000;

	public long getS24Total() {
		return s24Total;
	}

	public void setS24Total( long s24Total ) {
		this.s24Total = s24Total;
	}

	private long convenio = (long) (julioRemesa09 * 12 * 0.7);
	private long total = convenio + 1111;
	private String color = "#FFB5B5";
	
	private long marcoMonto = s24Total;
	// private long marcoPor;
	private float remesaPor =2222;
	private Double remesaMonto = Double.valueOf(marcoMonto * remesaPor);
	
	private float convenioPor =33333;
	public long getConvenioMonto() {
		return convenioMonto;
	}
	
	public void setConvenioMonto( long convenioMonto ) {
		this.convenioMonto = convenioMonto;
	}
	
	public float getConvenioPor() {
		return convenioPor;
	}
	
	public void setConvenioPor( float convenioPor ) {
		this.convenioPor = convenioPor;
	}
	
	public float getRemesaPor() {
		return remesaPor;
	}
	
	public long getMarcoMonto() {
		return marcoMonto;
	}
	
	public void setMarcoMonto( long marcoMonto ) {
		this.marcoMonto = marcoMonto;
	}
	
	public void setRemesaPor( float remesaPor ) {
		this.remesaPor = remesaPor;
	}
	
	public Double getRemesaMonto() {
		return remesaMonto;
	}
	
	public void setRemesaMonto( Double remesaMonto ) {
		this.remesaMonto = remesaMonto;
	}
	
	public long getConvenio() {
		return convenio;
	}
	
	public void setConvenio( long convenio ) {
		this.convenio = convenio;
	}
	
	public long getTotal() {
		return total;
	}
	
	public void setTotal( long total ) {
		this.total = total;
	}
	
	public String getColor() {
		return color;
	}
	
	public void setColor( String color ) {
		this.color = color;
	}
	
	public OTRevisarAntecedentesVO () {
		if (convenio == total) {
			this.color = "#DFFFCA";
		}
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getIdEstablecimiento() {
		return idEstablecimiento;
	}

	public void setIdEstablecimiento(Integer idEstablecimiento) {
		this.idEstablecimiento = idEstablecimiento;
	}

	public Integer getIdServicioSalud() {
		return idServicioSalud;
	}

	public void setIdServicioSalud(Integer idServicioSalud) {
		this.idServicioSalud = idServicioSalud;
	}

	public Integer getIdComuna() {
		return idComuna;
	}

	public void setIdComuna(Integer idComuna) {
		this.idComuna = idComuna;
	}
	
	public void calcularDiferencia(OTRevisarAntecedentesVO otRevisarAntecedentesVO)
	{
		//DIFERENCIA (MARCO -TOTAL(ACUMULADO+CONVENIO))
		long total = otRevisarAntecedentesVO.getEneroRemesa09()+otRevisarAntecedentesVO.getEneroRemesa24()+otRevisarAntecedentesVO.getEneroRemesa28()+
				otRevisarAntecedentesVO.getFebreroRemesa09()+otRevisarAntecedentesVO.getFebreroRemesa24()+otRevisarAntecedentesVO.getFebreroRemesa28()+
				otRevisarAntecedentesVO.getMarzoRemesa09()+otRevisarAntecedentesVO.getMarzoRemesa24()+otRevisarAntecedentesVO.getMarzoRemesa28()+
				otRevisarAntecedentesVO.getAbrilRemesa09()+otRevisarAntecedentesVO.getAbrilRemesa24()+otRevisarAntecedentesVO.getAbrilRemesa28()+
				otRevisarAntecedentesVO.getMayoRemesa09()+otRevisarAntecedentesVO.getMayoRemesa24()+otRevisarAntecedentesVO.getMayoRemesa28()+
				otRevisarAntecedentesVO.getJunioRemesa09()+otRevisarAntecedentesVO.getJunioRemesa24()+otRevisarAntecedentesVO.getJunioRemesa28()+
				otRevisarAntecedentesVO.getJulioRemesa09()+otRevisarAntecedentesVO.getJulioRemesa24()+otRevisarAntecedentesVO.getJulioRemesa28()+
				otRevisarAntecedentesVO.getAgostoRemesa09()+otRevisarAntecedentesVO.getAgostoRemesa24()+otRevisarAntecedentesVO.getAgostoRemesa28()+
				otRevisarAntecedentesVO.getSeptiembreRemesa09()+otRevisarAntecedentesVO.getSeptiembreRemesa24()+otRevisarAntecedentesVO.getSeptiembreRemesa28()+
				otRevisarAntecedentesVO.getOctubreRemesa09()+otRevisarAntecedentesVO.getOctubreRemesa24()+otRevisarAntecedentesVO.getOctubreRemesa28()+
				otRevisarAntecedentesVO.getNoviembreRemesa09()+otRevisarAntecedentesVO.getNoviembreRemesa24()+otRevisarAntecedentesVO.getNoviembreRemesa28()+
				otRevisarAntecedentesVO.getDiciembreRemesa09()+otRevisarAntecedentesVO.getDiciembreRemesa24()+otRevisarAntecedentesVO.getDiciembreRemesa28()+
				otRevisarAntecedentesVO.getConvenioMonto();		
		
		
		otRevisarAntecedentesVO.setDiferenciaMarcoContraTotalMonto(otRevisarAntecedentesVO.getMarcoPresupuestarioMonto() - total);
		total = otRevisarAntecedentesVO.getMarcoPresupuestarioMonto() - total;
		
		float totalPorcentaje  = ((float)total* (float)100)/(float)otRevisarAntecedentesVO.getMarcoPresupuestarioMonto();
		otRevisarAntecedentesVO.setDiferenciaMarcoContraTotalPorcentaje(String.valueOf(totalPorcentaje)+"%");	
		
	}
}