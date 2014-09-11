
package minsal.divap.enums;

public enum TipoDocumentosProcesos{
	PLANTILLAASIGNACIONDESEMPENODIFICIL(1, "Plantilla Asignación de Desempeño Difícil"), PLANTILLAPOBLACIONINSCRITA(2,"Plantilla Población Inscrita Validada"),
	PLANILLARESULTADOSCALCULADOS(3,"Planilla de resultados calculados"), BORRADORAPORTEESTATAL(4,"Borrador decreto aporte estatal"),
	PLANTILLABASECUMPLIMIENTO(5, "Plantilla Base Cumplimiento"), REBAJACALCULADA(6,"Plantilla Rebaja Calculada"),
	PLANTILLAOFICIOCONSULTA(7,"Plantilla Oficio Consulta"), OFICIOCONSULTA(8, "Oficio Consulta"),
	PLANTILLARESOLUCIONAPORTEESTATALUR(9,"Plantilla Resoluciones Comunales de Aporte Estatal UR"), PLANTILLARESOLUCIONAPORTEESTATALCF(10,"Plantilla Resoluciones Comunales de Aporte Estatal CF"),
	RESOLUCIONAPORTEESTATALUR(11,"Resoluciones Comunales de Aporte Estatal UR"), RESOLUCIONAPORTEESTATALCF(12,"Resoluciones Comunales de Aporte Estatal CF"),
	PLANTILLABORRADORAPORTEESTATAL(13,"Plantilla Borrador decreto aporte estatal"), ASIGNACIONDESEMPENODIFICIL(14, "Asignación de Desempeño Difícil"),
	
	//Plantilla PARA Distribución de Recursos Financieros para Programas de Reforzamiento de APS
	PLANTILLAPROGRAMAAPSMUNICIPALES(40, "Plantilla Programa APS Municipales"), PLANTILLAPROGRAMAAPSMUNICIPALEXCEPCION(41, "Plantilla Programa APS Municipal – Excepción"), PLANTILLAPROGRAMAAPSSERVICIO(42, "Plantilla Programa APS Servicios"),
	PROGRAMAAPSMUNICIPAL(43, "Programa APS Municipales"), PROGRAMAAPSMUNICIPALMIXTO(44, "Programa APS Municipal – MIXTO"), PROGRAMAAPSSERVICIO(45, "Programa APS Servicios"),
	POBLACIONINSCRITA(15, "Población Inscrita Validada"),PLANTILLAPROPUESTA(16, "Planilla Propuesta"),PLANTILLAPROGRAMACION(17, "Planilla Programación"), PLANTILLABORRADORORDINARIOPROGRAMACIONCAJA(50,"Plantilla Ordinario Seguimiento"),
	
	//Plantilla PARA OT
	 PLANTILLAORDINARIOOREDENTRANSFERENCIA(43,"Plantilla Ordinario Orden Transferencia"), RESUMENCONSOLIDADOFONASA(44,"Resumen Consolidado Fonasa"),
    
	BASECUMPLIMIENTO(50, "Base Cumplimiento"), PLANTILLARESOLUCIONREBAJA(51,"Plantilla Resolución Rebaja Aporte Estatal"), RESOLUCIONREBAJA(52,"Resolución Rebaja Aporte Estatal"),
	PLANTILLACORREORESOLUCIONSERVICIOSALUDREBAJA(53,"Plantilla Correo Resoluciones Servicios de Salud - Rebaja"), PLANTILLACORREOCONSULTAREGIONALPERCAPITA(54,"Plantilla Correo Consulta Regional - percapita"),
	PLANTILLACORREORESOLUCIONESPERCAPITA(55,"Plantilla Correo Resoluciones - percapita");

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
