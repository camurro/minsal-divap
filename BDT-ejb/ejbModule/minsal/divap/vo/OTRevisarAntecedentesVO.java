package minsal.divap.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cl.minsal.divap.model.Programa;
import cl.minsal.divap.model.ProgramaMunicipalCore;
import cl.minsal.divap.model.ProgramaServicioCore;
import cl.minsal.divap.model.Remesa;
import cl.minsal.divap.model.ServicioSalud;
import cl.minsal.divap.pojo.ComponentePojo;

public class OTRevisarAntecedentesVO
{
	Random rnd = new Random();
	private long id;
	private Integer idRemesa;
	private Integer idEstablecimiento;
	private Integer idServicio;
	private Integer idComuna;
	
	private String establecimiento;
	private String servicio;
	private String comuna;
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
	
	public String getComuna() {
		return comuna;
	}
	
	public void setComuna( String comuna ) {
		this.comuna = comuna;
	}
	
	public String getServicio() {
		return servicio;
	}
	
	public void setServicio( String servicio ) {
		this.servicio = servicio;
	}
	
	
	public Integer getIdRemesa() {
		return idRemesa;
	}

	public void setIdRemesa(Integer idRemesa) {
		this.idRemesa = idRemesa;
	}

	public List<OTRevisarAntecedentesVO> crearListaServicioPorPrograma(Programa programa)
	{
		List<OTRevisarAntecedentesVO> lista = new ArrayList<OTRevisarAntecedentesVO>();
		
		if(programa.getIdTipoPrograma().getId() == 1 )//SERVICIO
		{
//			for (ProgramaServicioCore servicioCore : programa.getProgramaServicioCores()) {
//				
//				OTRevisarAntecedentesVO otRevisarAntecedentesVO = new OTRevisarAntecedentesVO();
//				//SERVICIO SALUD
////				if(servicioCore.getServicioSalud() != null)
////				{
////					otRevisarAntecedentesVO.setServicio(servicioCore.getServicioSalud().getNombre());
////					otRevisarAntecedentesVO.setIdServicio(servicioCore.getServicioSalud().getId());
////				}
////				
//				//COMUNA
//				/*
//				if(servicioCore.getComuna() != null)
//				{
//					otRevisarAntecedentesVO.setEstablecimiento(servicioCore.getComuna().getNombre());
//					otRevisarAntecedentesVO.setIdEstablecimiento(servicioCore.getComuna().getId());
//				}
//				*/
//				otRevisarAntecedentesVO.setComuna("COMUNA PRUEBA SERVICIO");
//				otRevisarAntecedentesVO.setIdComuna(1);
//				
//				//ESTABLECIMIENTO
//				/*
//				if(servicioCore.getEstablecimiento() != null)
//				{
//					otRevisarAntecedentesVO.setEstablecimiento(servicioCore.getEstablecimiento().getNombre());
//					otRevisarAntecedentesVO.setIdEstablecimiento(servicioCore.getEstablecimiento().getId());
//				}
//				*/
//				otRevisarAntecedentesVO.setEstablecimiento("ESTABLECIMIENTO PRUEBA SERVICIO");
//				otRevisarAntecedentesVO.setIdEstablecimiento(1);
//				
//				lista.add(otRevisarAntecedentesVO);
//				
//			}			
		}
		else if(programa.getIdTipoPrograma().getId() == 1 )//MUNICIPAL
		{
//			for (ProgramaMunicipalCore municipalCore : programa.getProgramaMunicipalCores()) {
//				
//				OTRevisarAntecedentesVO otRevisarAntecedentesVO = new OTRevisarAntecedentesVO();
//				//SERVICIO SALUD
////				if(municipalCore.getServicioSalud() != null)
////				{
////					otRevisarAntecedentesVO.setEstablecimiento(municipalCore.getServicioSalud().getNombre());
////					otRevisarAntecedentesVO.setIdEstablecimiento(municipalCore.getServicioSalud().getId());
////				}
//				otRevisarAntecedentesVO.setServicio("SERVICIO PRUEBA MUNICIPAL");
//				otRevisarAntecedentesVO.setIdServicio(1);
//				
//				//COMUNA
//				/*
//				if(servicioCore.getComuna() != null)
//				{
//					otRevisarAntecedentesVO.setEstablecimiento(servicioCore.getComuna().getNombre());
//					otRevisarAntecedentesVO.setIdEstablecimiento(servicioCore.getComuna().getId());
//				}
//				*/
//				otRevisarAntecedentesVO.setComuna("COMUNA PRUEBA MUNICIPAL");
//				otRevisarAntecedentesVO.setIdComuna(1);
//				
//				//ESTABLECIMIENTO
//				/*
//				if(servicioCore.getEstablecimiento() != null)
//				{
//					otRevisarAntecedentesVO.setEstablecimiento(servicioCore.getEstablecimiento().getNombre());
//					otRevisarAntecedentesVO.setIdEstablecimiento(servicioCore.getEstablecimiento().getId());
//				}
//				*/
//				otRevisarAntecedentesVO.setEstablecimiento("ESTABLECIMIENTO PRUEBA MUNICIPAL");
//				otRevisarAntecedentesVO.setIdEstablecimiento(1);
//				
//				lista.add(otRevisarAntecedentesVO);
//				
//			}			
		}
		
		return lista;
		
		
	}
	
	public List<OTRevisarAntecedentesVO> obtenerListaSubtitulo21VOPorPrograma(Programa programa)
	{
		List<OTRevisarAntecedentesVO> listaVO = crearListaServicioPorPrograma(programa);
		
		List<Remesa>listaRemesa =  new ArrayList<Remesa>();
		if(programa.getRemesas()!=null)
			listaRemesa = programa.getRemesas();


		for (OTRevisarAntecedentesVO otRevisarAntecedentesVO : listaVO) {

			Integer idServicioSalud = otRevisarAntecedentesVO.getIdServicio();
			Integer idEstablecimiento =0;
			if(otRevisarAntecedentesVO.getIdEstablecimiento()!=null)
				idEstablecimiento= otRevisarAntecedentesVO.getIdEstablecimiento();
			
			Integer idComuna=0;
			if(otRevisarAntecedentesVO.getIdComuna()!=null)
				idComuna= otRevisarAntecedentesVO.getIdComuna();
			
			Integer anio = 2014;// otRevisarAntecedentesVO.getAnio();

			for (Remesa remesa : listaRemesa) {
				
					if(programa.getIdTipoPrograma().getId() == 1)//TIPO SERVICIO
					{
						
						
						cargarVOPorRemesa(otRevisarAntecedentesVO,remesa);
						/*
						if(remesa.getServicioSalud()!=null)
						{
							if(idServicioSalud == remesa.getServicioSalud().getId())
							{
								if(remesa.getEstablecimiento()!=null)
								{
									if(idEstablecimiento== remesa.getEstablecimiento().getId())
									{
										if(anio== remesa.getAnio())
										{
											cargarVOPorRemesa(otRevisarAntecedentesVO,remesa);
										}
									}
								}
							}
						}
						*/
					}
					else if(programa.getIdTipoPrograma().getId() == 2)//TIPO MUNCIPAL
					{
						cargarVOPorRemesa(otRevisarAntecedentesVO,remesa);
						/*
						if(remesa.getServicioSalud()!=null)
						{
							if(idServicioSalud == remesa.getServicioSalud().getId())
							{
								if(remesa.getComuna()!=null)
								{
									if(idComuna== remesa.getComuna().getId())
									{
										if(anio== remesa.getAnio())
										{
											cargarVOPorRemesa(otRevisarAntecedentesVO,remesa);
										}
									}
								}
							}
						}
						*/
					}
			}
		}
		
		return listaVO;
		
	}
	
	
	private void cargarVOPorRemesa(OTRevisarAntecedentesVO otRevisarAntecedentesVO, Remesa remesa)
	{
		
		Long valorDia09 =  1L;//remesa.getValorDia09();
		Long valorDia24 =  1L;//remesa.getValorDia09();
		Long valorDia28 =  1L;//remesa.getValorDia09();
		/*	
		//ENERO
		if(remesa.getMes().getIdMes() == 1)
		{
			otRevisarAntecedentesVO.setEneroRemesa09(valorDia09);
			otRevisarAntecedentesVO.setEneroRemesa24(valorDia24);
			otRevisarAntecedentesVO.setEneroRemesa28(valorDia28);
		}
		
		//FEBRERO
		if(remesa.getMes().getIdMes() == 2)
		{
			otRevisarAntecedentesVO.setFebreroRemesa09(valorDia09);
			otRevisarAntecedentesVO.setFebreroRemesa24(valorDia24);
			otRevisarAntecedentesVO.setFebreroRemesa28(valorDia28);
		}
		//MARZO
		if(remesa.getMes().getIdMes() == 3)
		{
			otRevisarAntecedentesVO.setMarzoRemesa09(valorDia09);
			otRevisarAntecedentesVO.setMarzoRemesa24(valorDia24);
			otRevisarAntecedentesVO.setMarzoRemesa28(valorDia28);
		}
		
		
		//ABRIL
		if(remesa.getMes().getIdMes() == 4)
		{
			otRevisarAntecedentesVO.setAbrilRemesa09(valorDia09);
			otRevisarAntecedentesVO.setAbrilRemesa24(valorDia24);
			otRevisarAntecedentesVO.setAbrilRemesa28(valorDia28);
		}
		
		//MAYO
		if(remesa.getMes().getIdMes() == 5)
		{
			otRevisarAntecedentesVO.setMayoRemesa09(valorDia09);
			otRevisarAntecedentesVO.setMayoRemesa24(valorDia24);
			otRevisarAntecedentesVO.setMayoRemesa28(valorDia28);
		}
		
		//JUNIO
		if(remesa.getMes().getIdMes() == 6)
		{
			otRevisarAntecedentesVO.setJunioRemesa09(valorDia09);
			otRevisarAntecedentesVO.setJunioRemesa24(valorDia24);
			otRevisarAntecedentesVO.setJunioRemesa28(valorDia28);
		}
		
		//JULIO
		if(remesa.getMes().getIdMes() == 7)
		{
			otRevisarAntecedentesVO.setJulioRemesa09(valorDia09);
			otRevisarAntecedentesVO.setJulioRemesa24(valorDia24);
			otRevisarAntecedentesVO.setJulioRemesa28(valorDia28);
		}
		
		//AGOSTO
		if(remesa.getMes().getIdMes() == 8)
		{
			otRevisarAntecedentesVO.setAgostoRemesa09(valorDia09);
			otRevisarAntecedentesVO.setAgostoRemesa24(valorDia24);
			otRevisarAntecedentesVO.setAgostoRemesa28(valorDia28);
		}
		
		//SEPTIEMBRE
		if(remesa.getMes().getIdMes() == 9)
		{
			otRevisarAntecedentesVO.setSeptiembreRemesa09(valorDia09);
			otRevisarAntecedentesVO.setSeptiembreRemesa24(valorDia24);
			otRevisarAntecedentesVO.setSeptiembreRemesa28(valorDia28);
		}
		
		//OCTUBRE
		if(remesa.getMes().getIdMes() == 10)
		{
			otRevisarAntecedentesVO.setOctubreRemesa09(valorDia09);
			otRevisarAntecedentesVO.setOctubreRemesa24(valorDia24);
			otRevisarAntecedentesVO.setOctubreRemesa28(valorDia28);
		}

		//NOVIEMBRE
		if(remesa.getMes().getIdMes() == 11)
		{
			otRevisarAntecedentesVO.setNoviembreRemesa09(valorDia09);
			otRevisarAntecedentesVO.setNoviembreRemesa24(valorDia24);
			otRevisarAntecedentesVO.setNoviembreRemesa28(valorDia28);
		}
		
		//DICIEMBRE
		if(remesa.getMes().getIdMes() == 12)
		{
			otRevisarAntecedentesVO.setDiciembreRemesa09(valorDia09);
			otRevisarAntecedentesVO.setDiciembreRemesa24(valorDia24);
			otRevisarAntecedentesVO.setDiciembreRemesa28(valorDia28);
		}
		*/
	}

	private long eneroRemesa09 = rnd.nextInt(99999999);
	private long eneroRemesa24 = rnd.nextInt(99999999);
	private long eneroRemesa28 = rnd.nextInt(99999999);

	private long febreroRemesa09 = rnd.nextInt(99999999);
	private long febreroRemesa24 = rnd.nextInt(99999999);
	private long febreroRemesa28 = rnd.nextInt(99999999);
	
	private long marzoRemesa09 = rnd.nextInt(99999999);
	private long marzoRemesa24 = rnd.nextInt(99999999);
	private long marzoRemesa28 = rnd.nextInt(99999999);
	
	private long abrilRemesa09 = rnd.nextInt(99999999);
	private long abrilRemesa24 = rnd.nextInt(99999999);
	private long abrilRemesa28 = rnd.nextInt(99999999);
	
	private long mayoRemesa09 = rnd.nextInt(99999999);
	private long mayoRemesa24 = rnd.nextInt(99999999);
	private long mayoRemesa28 = rnd.nextInt(99999999);
	
	private long junioRemesa09 = rnd.nextInt(99999999);
	private long junioRemesa24 = rnd.nextInt(99999999);
	private long junioRemesa28 = rnd.nextInt(99999999);

	private long julioRemesa09 = rnd.nextInt(4);
	private long julioRemesa24 = rnd.nextInt(4);
	private long julioRemesa28 = rnd.nextInt(4);

	private long agostoRemesa09 = rnd.nextInt(1);
	private long agostoRemesa24 = rnd.nextInt(1);
	private long agostoRemesa28 = rnd.nextInt(1);
	
	private long septiembreRemesa09 = rnd.nextInt(99999999);
	private long septiembreRemesa24 = rnd.nextInt(99999999);
	private long septiembreRemesa28 = rnd.nextInt(99999999);
	
	private long octubreRemesa09 = rnd.nextInt(99999999);
	private long octubreRemesa24 = rnd.nextInt(99999999);
	private long octubreRemesa28 = rnd.nextInt(99999999);
	
	private long noviembreRemesa09 = rnd.nextInt(99999999);
	private long noviembreRemesa24 = rnd.nextInt(99999999);
	private long noviembreRemesa28 = rnd.nextInt(99999999);
	
	private long diciembreRemesa09 = rnd.nextInt(99999999);
	private long diciembreRemesa24 = rnd.nextInt(99999999);
	private long diciembreRemesa28 = rnd.nextInt(99999999);
	
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
	private long total = convenio + rnd.nextInt(2);
	private String color = "#FFB5B5";
	
	private long marcoMonto = s24Total;
	// private long marcoPor;
	private float remesaPor = rnd.nextFloat();
	private Double remesaMonto = Double.valueOf(marcoMonto * remesaPor);
	
	private float convenioPor = rnd.nextFloat();
	private long convenioMonto = (long) (marcoMonto * convenioPor);
	
	
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
	
	public Random getRnd() {
		return rnd;
	}
	
	public void setRnd( Random rnd ) {
		this.rnd = rnd;
	}
	
	public String getEstablecimiento() {
		return establecimiento;
	}
	
	public void setEstablecimiento( String establecimiento ) {
		this.establecimiento = establecimiento;
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

	public Integer getIdServicio() {
		return idServicio;
	}

	public void setIdServicio(Integer idServicio) {
		this.idServicio = idServicio;
	}

	public Integer getIdComuna() {
		return idComuna;
	}

	public void setIdComuna(Integer idComuna) {
		this.idComuna = idComuna;
	}
	
	
}