
package minsal.divap.enums;

public enum TipoDocumentosProcesos{
	PLANTILLAASIGNACIONDESEMPENODIFICIL(1, "Plantilla Asignación de Desempeño Difícil"), PLANTILLAPOBLACIONINSCRITA(2,"Plantilla Población Inscrita Validada"),
	PLANILLARESULTADOSCALCULADOS(3,"Planilla de resultados calculados"), BORRADORAPORTEESTATAL(4,"Borrador decreto aporte estatal"),
	PLANTILLABASECUMPLIMIENTO(5, "Plantilla Base Cumplimiento"), REBAJACALCULADA(6,"Plantilla Rebaja Calculada"),
	PLANTILLAOFICIOCONSULTA(7,"Plantilla Oficio Consulta"), OFICIOCONSULTA(8, "Oficio Consulta"),
	PLANTILLARESOLUCIONAPORTEESTATALUR(9,"Plantilla Resoluciones Comunales de Aporte Estatal UR"), PLANTILLARESOLUCIONAPORTEESTATALCF(10,"Plantilla Resoluciones Comunales de Aporte Estatal CF"),
	RESOLUCIONAPORTEESTATALUR(11,"Resoluciones Comunales de Aporte Estatal UR"), RESOLUCIONAPORTEESTATALCF(12,"Resoluciones Comunales de Aporte Estatal CF"),
	PLANTILLABORRADORAPORTEESTATAL(13,"Plantilla Borrador decreto aporte estatal"), ASIGNACIONDESEMPENODIFICIL(14, "Asignación de Desempeño Difícil"),
	PLANTILLABASECUMPLIMIENTOMUNICIPAL(57,"Plantilla Base Cumplimiento Municipal"), PLANTILLABASECUMPLIMIENTOSERVICIO(58,"Plantilla Base Cumplimiento Servicio"),
	
	//Plantilla PARA Distribución de Recursos Financieros para Programas de Reforzamiento de APS
	PLANTILLAPROGRAMAAPSMUNICIPALES(40, "Plantilla Programa APS Municipales"),
	PLANTILLAPROGRAMAAPSMUNICIPALEXCEPCION(41, "Plantilla Programa APS Municipal – Excepción"), 
	PLANTILLAPROGRAMAAPSSERVICIO(42, "Plantilla Programa APS Servicios"),
	PROGRAMAAPSMUNICIPAL(60, "Programa APS Municipales"),
	PROGRAMAAPSMUNICIPALMIXTO(61, "Programa APS Municipal – MIXTO"), 
	PROGRAMAAPSSERVICIO(62, "Programa APS Servicios"),
	POBLACIONINSCRITA(15, "Población Inscrita Validada"),
	PLANTILLAPROPUESTA(16, "Planilla de Estimación de Flujos de Caja"),
	PLANTILLAPROGRAMACION(17, "Planilla Programación"),
	PLANTILLABORRADORORDINARIOPROGRAMACIONCAJA(50,"Plantilla Ordinario Seguimiento"),
	RESOLUCIONPROGRAMASAPS(57,"Resolución Recursos Programa"),
	ORDINARIOPROGRAMASAPS(58,"Ordinario Recursos Programa"),
	PLANTILLARESOLUCIONPROGRAMASAPS(59,"Plantilla Resolución Recursos Programa"),
	PLANTILLALEYAPS(63,"Plantilla Ley"),
	PLANTILLAMUNICIPALAPSRESUMENSERVICIO(64,"Plantilla APS Municipal Resumen Servicio"),
	PLANTILLASERVICIOAPSRESUMENSERVICIO(65,"Plantilla APS Servicio Resumen Servicio"),
	PLANTILLAMUNICIPALAPSRESUMENHISTORICO(66,"Plantilla APS Municipal Resumen Historico"),
	PLANTILLASERVICIOAPSRESUMENHISTORICO(67,"Plantilla APS Servicio Resumen Historico"),
	PLANTILLAAPSDETALLEMUNICIPAL(68,"Plantilla APS Detalle Municipal"),
	PLANTILLAAPSDETALLESERVICIO(69,"Plantilla APS Detalle Servicio"),
	PLANTILLAAPSDETALLEMUNICIPALHISTORICO(70,"Plantilla APS Detalle Municipal Historico"),
	PLANTILLAAPSDETALLESERVICIOHISTORICO(71,"Plantilla APS Detalle Servicio Historico"),
	ADJUNTOCORREO(72,"Adjunto correo"),
	PLANTILLARESOLUCIONCORREO(73, "Plantilla Resolución Correo"),
	PLANTILLAORDINARIOCORREO(74,"Plantilla Ordinario Correo"),

	//Modificacion Reforzamiento APS
	MODIFICACIONRESOLUCIONPROGRAMASAPS(80,"Resolución Modificación Recursos Programa"),
	MODIFICACIONORDINARIOPROGRAMASAPS(81,"Ordinario Modificación Recursos Programa"),
	
	//Plantilla PARA OT
	 PLANTILLAORDINARIOOREDENTRANSFERENCIA(43,"Plantilla Ordinario Orden Transferencia"), 
	 RESUMENCONSOLIDADOFONASA(44,"Resumen Consolidado Fonasa"),
	 PLANTILLAOTCORREO(79,"Plantilla Correo OT"),
		
	BASECUMPLIMIENTO(50, "Base Cumplimiento"), PLANTILLARESOLUCIONREBAJA(51,"Plantilla Resolución Rebaja Aporte Estatal"), RESOLUCIONREBAJA(52,"Resolución Rebaja Aporte Estatal"),
	PLANTILLACORREORESOLUCIONSERVICIOSALUDREBAJA(53,"Plantilla Correo Resoluciones Servicios de Salud - Rebaja"), PLANTILLACORREOCONSULTAREGIONALPERCAPITA(54,"Plantilla Correo Consulta Regional - percapita"),
	PLANTILLACORREORESOLUCIONESPERCAPITA(55,"Plantilla Correo Resoluciones - percapita"), PLANTILLAOFICIOPROGRAMACIONCAJA(56,"Plantilla Oficio Programación Caja"),
	
	CONVENIOS(50,"Convenios Documentos"), PLANILLABASECUMPLIMIENTOMUNICIPAL(59,"Planilla Base Cumplimiento Municipal"), PLANILLABASECUMPLIMIENTOSERVICIO(60,"Planilla Base Cumplimiento Servicio"),
	
	PLANTILLACORREORESOLUCIONRETIRO(65,"Plantilla Correo Resolucion Retiro"),
	ADJUNTOSEGUIMIENTORESOLUCIONRETIRO(150, "Documento Adjunto Seguimiento Resolución Retiro"), RESOLUCIONRETIRO(151, "Resolución Retiro"), PLANTILLARESOLUCIONRETIRO(152, "Plantilla Resolución Retiro"),
	REPORTEPOBLACIONPERCAPITA(100,"Reporte Poblacion Per Capita"), REPORTEREBAJA(101,"Reporte Rebaja"), REPORTEGLOSA07(102,"Reporte Glosa 07"),
	REPORTEMARCOPRESPUESTARIOCOMUNA(103,"Reporte Marco Presupuestario Comuna"), REPORTEMARCOPRESPUESTARIOSERVICIO(104,"Reporte Marco Presupuestario Servicio"), 
	REPORTEHISTORICOPROGRAMACOMUNA(105,"Reporte Historico Por Programa - Comuna"), REPORTEHISTORICOPROGRAMAESTABLECIMIENTO(106,"Reporte Historico Por Programa - Establecimiento"),
	REPORTEMONITOREOPROGRAMACOMUNA(107,"Reporte Monitoreo Programa - Comuna"), REPORTEMONITOREOPROGRAMASERVICIO(108,"Reporte Monitoreo Programa - Servicios"),
	REPORTEESTADOSITUACIONPROGRAMABYCOMUNA(109,"Reporte Estado Situacion Programa - Comuna"), REPORTEESTADOSITUACIONPROGRAMABYSERVICIO(110,"Reporte Estado Situacion Programa - Servicio"),
	CONVENIOSERVICIO(153, "Convenio Servicio"), CONVENIOCOMUNA(154, "Convenio Comuna"), PLANILLACONVENIOMUNICIPAL(155, "Planilla Convenio Municipal"), PLANILLACONVENIOSERVICIO(156, "Planilla Convenio Servicio"),
	PLANTILLAORDINARIOSOLICITUDANTECEDENTES(159, "Ordinario Solicitud Antecedentes"), ORDINARIOSOLICITUDANTECEDENTES(160, "Ordinario Solicitud Antecedentes"),
	PLANTILLACORREOORDINARIOSOLICITUDANTECEDENTES(161, "Plantilla Correo Ordinario Solicitud Antecedentes"), PLANTILLAMODIFICACIONRESOLUCIONAPORTEESTATAL(170, "Plantilla ordinario modificación aporte estatal"),
	ORDINARIOMODIFICACIONRESOLUCIONAPORTEESTATAL(171, "Ordinario modificación aporte estatal"), PLANTILLAMODIFICACIONDECRETOAPORTEESTATAL(172, "Plantilla modificación decreto aporte estatal");

	
	private Integer id;
	private String name;

	private TipoDocumentosProcesos(Integer id, String name) {
		this.id = id; 
		this.name = name;
	}

	public Integer getId()
	{
		return this.id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static TipoDocumentosProcesos getById(int id){
		for (TipoDocumentosProcesos tipoDocumentosProcesos : TipoDocumentosProcesos.values()) {
			if (tipoDocumentosProcesos.id == id)
				return tipoDocumentosProcesos;
		}
		throw new IllegalArgumentException("id de tipo documento proceso no válido");
	}
}
