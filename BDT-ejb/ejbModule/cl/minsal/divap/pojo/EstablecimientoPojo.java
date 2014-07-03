package cl.minsal.divap.pojo;

public class EstablecimientoPojo {
	public String codigoAntiguo;
	public String codigoNuevo;
	public String codigoEstablecimientoMadre;
	public String codigoRegion;
	public String region;
	public String servicioSalud;
	public String nombreOficial;
	public String alias;
	public ComunaPojo comuna;
	public String via;
	public String numero;
	public String direccion;
	public String vigencia;
	public String fechaCierre;
	public String codigoServicioSaludSeremi;
	
	private float cumplimiento;
	private float reliquidacion;
	
	
	
	public float getCumplimiento() {
		return cumplimiento;
	}

	public void setCumplimiento( float cumplimiento ) {
		this.cumplimiento = cumplimiento;
	}

	public float getReliquidacion() {
		return reliquidacion;
	}

	public void setReliquidacion( float reliquidacion ) {
		this.reliquidacion = reliquidacion;
	}

	public String getCodigoAntiguo() {
		return codigoAntiguo;
	}
	
	public void setCodigoAntiguo( String codigoAntiguo ) {
		this.codigoAntiguo = codigoAntiguo;
	}
	
	public String getCodigoNuevo() {
		return codigoNuevo;
	}
	
	public void setCodigoNuevo( String codigoNuevo ) {
		this.codigoNuevo = codigoNuevo;
	}
	
	public String getCodigoEstablecimientoMadre() {
		return codigoEstablecimientoMadre;
	}
	
	public void setCodigoEstablecimientoMadre( String codigoEstablecimientoMadre ) {
		this.codigoEstablecimientoMadre = codigoEstablecimientoMadre;
	}
	
	public String getCodigoRegion() {
		return codigoRegion;
	}
	
	public void setCodigoRegion( String codigoRegion ) {
		this.codigoRegion = codigoRegion;
	}
	
	public String getRegion() {
		return region;
	}
	
	public void setRegion( String region ) {
		this.region = region;
	}
	
	public String getServicioSalud() {
		return servicioSalud;
	}
	
	public void setServicioSalud( String servicioSalud ) {
		this.servicioSalud = servicioSalud;
	}
	
	public String getNombreOficial() {
		return nombreOficial;
	}
	
	public void setNombreOficial( String nombreOficial ) {
		this.nombreOficial = nombreOficial;
	}
	
	public String getAlias() {
		return alias;
	}
	
	public void setAlias( String alias ) {
		this.alias = alias;
	}
	
	public ComunaPojo getComuna() {
		return comuna;
	}
	
	public void setComuna( ComunaPojo comuna ) {
		this.comuna = comuna;
	}
	
	public String getVia() {
		return via;
	}
	
	public void setVia( String via ) {
		this.via = via;
	}
	
	public String getNumero() {
		return numero;
	}
	
	public void setNumero( String numero ) {
		this.numero = numero;
	}
	
	public String getDireccion() {
		return direccion;
	}
	
	public void setDireccion( String direccion ) {
		this.direccion = direccion;
	}
	
	public String getVigencia() {
		return vigencia;
	}
	
	public void setVigencia( String vigencia ) {
		this.vigencia = vigencia;
	}
	
	public String getFechaCierre() {
		return fechaCierre;
	}
	
	public void setFechaCierre( String fechaCierre ) {
		this.fechaCierre = fechaCierre;
	}
	
	public String getCodigoServicioSaludSeremi() {
		return codigoServicioSaludSeremi;
	}
	
	public void setCodigoServicioSaludSeremi( String codigoServicioSaludSeremi ) {
		this.codigoServicioSaludSeremi = codigoServicioSaludSeremi;
	}
	
	// backing
	private Long pS21 = 0L;
	private Long qS21 = 0L;
	private Long tS21 = 0L;
	private Long pS22 = 0L;
	private Long qS22 = 0L;
	private Long tS22 = 0L;
	private Long pS29 = 0L;
	private Long qS29 = 0L;
	private Long tS29 = 0L;
	private Long pS24 = 0L;
	private Long qS24 = 0L;
	private Long tS24 = 0L;
	
	public Long getpS21() {
		return pS21;
	}
	
	public void setpS21( Long pS21 ) {
		this.pS21 = pS21;
		if (!this.pS21.equals(null) && !this.pS21.equals("")) {
			if (!this.qS21.equals(null) && !this.qS21.equals("")) {
				this.tS21 = this.pS21*this.qS21;
			}
		} else {
			this.tS21 = 0L;
		}
	}
	
	public Long getqS21() {
		return qS21;
	}
	
	public void setqS21( Long qS21 ) {
		this.qS21 = qS21;
		if (!this.pS21.equals(null) && !this.pS21.equals("")) {
			if (!this.qS21.equals(null) && !this.qS21.equals("")) {
				this.tS21 = this.pS21*this.qS21;
			}
		} else {
			this.tS21 = 0L;
		}
	}
	
	public Long gettS21() {
		return tS21;
	}
	
	public void settS21( Long tS21 ) {
		this.tS21 = tS21;
	}
	
	public Long getpS22() {
		return pS22;
	}
	
	public void setpS22( Long pS22 ) {
		this.pS22 = pS22;
		if (!this.pS22.equals(null) && !this.pS22.equals("")) {
			if (!this.qS22.equals(null) && !this.qS22.equals("")) {
				this.tS22 = this.pS22*this.qS22;
			}
		} else {
			this.tS22 = 0L;
		}
	}
	
	public Long getqS22() {
		return qS22;
	}
	
	public void setqS22( Long qS22 ) {
		this.qS22 = qS22;
		if (!this.pS22.equals(null) && !this.pS22.equals("")) {
			if (!this.qS22.equals(null) && !this.qS22.equals("")) {
				this.tS22 = this.pS22*this.qS22;
			}
		} else {
			this.tS22 = 0L;
		}
	}
	
	public Long gettS22() {
		return tS22;
	}
	
	public void settS22( Long tS22 ) {
		this.tS22 = tS22;
	}
	
	public Long getpS29() {
		return pS29;
	}
	
	public void setpS29( Long pS29 ) {
		this.pS29 = pS29;
		if (!pS29.equals(null) && !this.pS29.equals("")) {
			if (!this.qS29.equals(null) && !this.qS29.equals("")) {
				this.tS29 = this.pS29*this.qS29;
			}
		} else {
			this.tS29 = 0L;
		}
	}
	
	public Long getqS29() {
		return qS29;
	}
	
	public void setqS29( Long qS29 ) {
		this.qS29 = qS29;
		if (!pS29.equals(null) && !this.pS29.equals("")) {
			if (!this.qS29.equals(null) && !this.qS29.equals("")) {
				this.tS29 = this.pS29*this.qS29;
			}
		} else {
			this.tS29 = 0L;
		}
	}
	
	public Long gettS29() {
		return tS29;
	}
	
	public void settS29( Long tS29 ) {
		this.tS29 = tS29;
	}
	
	public Long getpS24() {
		return pS24;
	}
	
	public void setpS24( Long pS24 ) {
		this.pS24 = pS24;
		if (!pS24.equals(null) && !this.pS24.equals("")) {
			if (!this.qS24.equals(null) && !this.qS24.equals("")) {
				this.tS24 = this.pS24*this.qS24;
			}
		} else {
			this.tS24 = 0L;
		}
	}
	
	public Long getqS24() {
		return qS24;
	}
	
	public void setqS24( Long qS24 ) {
		this.qS24 = qS24;
		if (!pS24.equals(null) && !this.pS24.equals("")) {
			if (!this.qS24.equals(null) && !this.qS24.equals("")) {
				this.tS24 = this.pS24*this.qS24;
			}
		} else {
			this.tS24 = 0L;
		}
	}
	
	public Long gettS24() {
		return tS24;
	}
	
	public void settS24( Long tS24 ) {
		this.tS24 = tS24;
	}
	
}
