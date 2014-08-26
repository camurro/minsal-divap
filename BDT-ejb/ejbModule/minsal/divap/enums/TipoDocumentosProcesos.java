package minsal.divap.enums;


public enum TipoDocumentosProcesos{
	PLANTILLAASIGNACIONDESEMPENODIFICIL(1, "Plantilla Asignación de Desempeño Difícil"), PLANTILLAPOBLACIONINSCRITA(2,"Plantilla Población Inscrita Validada"),
	PLANILLARESULTADOSCALCULADOS(3,"Planilla de resultados calculados"), BORRADORAPORTEESTATAL(4,"Borrador decreto aporte estatal"),
	BASECUMPLIMIENTO(5, "Plantilla Base Cumplimiento"), REBAJACALCULADA(6,"Plantilla Rebaja Calculada"),
	PLANTILLAOFICIOCONSULTA(7,"Plantilla Oficio Consulta"), OFICIOCONSULTA(8, "Oficio Consulta"),
	PLANTILLARESOLUCIONAPORTEESTATALUR(9,"Plantilla Resoluciones Comunales de Aporte Estatal UR"), PLANTILLARESOLUCIONAPORTEESTATALCF(10,"Plantilla Resoluciones Comunales de Aporte Estatal CF"),
	RESOLUCIONAPORTEESTATALUR(11,"Resoluciones Comunales de Aporte Estatal UR"), RESOLUCIONAPORTEESTATALCF(12,"Resoluciones Comunales de Aporte Estatal CF"),
	PLANTILLABORRADORAPORTEESTATAL(13,"Plantilla Borrador decreto aporte estatal"), ASIGNACIONDESEMPENODIFICIL(14, "Asignación de Desempeño Difícil"),
	PLANTILLAPROGRAMAAPSMUNICIPALES(40, "Plantilla Programa APS Municipales"), PLANTILLAPROGRAMAAPSMUNICIPALEXCEPCION(41, "Plantilla Programa APS Municipal – Excepción"), PLANTILLAPROGRAMAAPSSERVICIO(42, "Plantilla Programa APS Servicios"),
	PROGRAMAAPSMUNICIPAL(43, "Programa APS Municipales"), PROGRAMAAPSMUNICIPALMIXTO(44, "Programa APS Municipal – MIXTO"), PROGRAMAAPSSERVICIO(45, "Programa APS Servicios"),
	POBLACIONINSCRITA(15, "Población Inscrita Validada"), 
	
	PLANTILLAPROGRAMACION(17,"Planilla Programacion de Caja"),PLANTILLAPROPUESTA(16,"Plantilla Propuesta"), PLANTILLABORRADORORDINARIOPROGRAMACIONCAJA(18,"Plantilla Ordinario Programacion Caja");
	

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
