package cl.minsal.divap.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author cmurillo
 */
@Entity
@Table(name = "detalle_remesas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetalleRemesas.findAll", query = "SELECT d FROM DetalleRemesas d"),
    @NamedQuery(name = "DetalleRemesas.findDetalleRemesaByComuna", query = "SELECT d FROM DetalleRemesas d WHERE d.comuna.id = :idComuna and d.programaAno.idProgramaAno = :idProgramaAno and d.mes.idMes = :idMes and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo"),
    @NamedQuery(name = "DetalleRemesas.findByIdDetalleRemesa", query = "SELECT d FROM DetalleRemesas d WHERE d.idDetalleRemesa = :idDetalleRemesa"),
    @NamedQuery(name = "DetalleRemesas.findByMontoRemesa", query = "SELECT d FROM DetalleRemesas d WHERE d.montoRemesa = :montoRemesa"),
    @NamedQuery(name = "DetalleRemesas.findByRemesaPagada", query = "SELECT d FROM DetalleRemesas d WHERE d.remesaPagada = :remesaPagada"),
    @NamedQuery(name = "DetalleRemesas.getRemesasPagadasComunaLaFecha", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.comuna.id = :idComuna and d.mes.idMes < :idMes and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.remesaPagada = :remesaPagada"),
    @NamedQuery(name = "DetalleRemesas.getRemesasByProgramaAnoComponenteEstablecimientoSubtituloPagada", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.componente.id = :idComponente and d.establecimiento.id = :idEstablecimiento and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.remesaPagada = :remesaPagada order by d.fecha asc"),
    @NamedQuery(name = "DetalleRemesas.getRemesasByProgramaAnoComponenteComunaSubtituloPagada", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.componente.id = :idComponente and d.comuna.id = :idComuna and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.remesaPagada = :remesaPagada"),
    @NamedQuery(name = "DetalleRemesas.getRemesasByProgramaAnoComponenteComunaSubtituloDiaMesPagada", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.componente.id = :idComponente and d.comuna.id = :idComuna and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.dia.dia <= :idDia and d.mes.idMes <= :idMes and d.remesaPagada = :remesaPagada"),
    @NamedQuery(name = "DetalleRemesas.getRemesasPagadasByProgramaAnoComponenteComunaSubtitulo", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.componente.id = :idComponente and d.comuna.id = :idComuna and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.remesaPagada = :remesaPagada"),
    @NamedQuery(name = "DetalleRemesas.getRemesasPagadasComuna", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.comuna.id = :idComuna and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.remesaPagada = :remesaPagada order by d.cuota.numeroCuota asc"),
    @NamedQuery(name = "DetalleRemesas.getDetalleRemesasByProgramaAnoEstablecimientoSubtitulo", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.establecimiento.id = :idEstablecimiento and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo"),
    @NamedQuery(name = "DetalleRemesas.getRemesasMesActualByMesProgramaAnoServicioSubtitulo", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.establecimiento.servicioSalud.id = :idServicio and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.mes.idMes BETWEEN :idMesDesde and :idMesHasta"),
    @NamedQuery(name = "DetalleRemesas.getRemesasMesActualByMesProgramaAnoServicioSubtituloConsolidador", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.establecimiento.servicioSalud.id = :idServicio and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.revisarConsolidador = :revisarConsolidador and d.mes.idMes BETWEEN :idMesDesde and :idMesHasta"),
    @NamedQuery(name = "DetalleRemesas.getRemesasMesActualByMesProgramaAnoServicioSubtitulo1", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.establecimiento.servicioSalud.id = :idServicio and d.mes.idMes = :idMes and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.remesaPagada = :remesaPagada"),
    @NamedQuery(name = "DetalleRemesas.getRemesasMesActualByDiaMesProgramaAnoServicioSubtitulo1", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.establecimiento.servicioSalud.id = :idServicio and d.dia.dia <= :idDia and d.mes.idMes = :idMes and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.remesaPagada = :remesaPagada"),
    @NamedQuery(name = "DetalleRemesas.getRemesasMesActualByDiaMesProgramaAnoServicioSubtitulo2", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.comuna.servicioSalud.id = :idServicio and d.dia.dia <= :idDia and d.mes.idMes = :idMes and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.remesaPagada = :remesaPagada"),
    @NamedQuery(name = "DetalleRemesas.getRemesasServicioAprobadasConsolidadorByDiaMesProgramaAnoServicio", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.establecimiento.servicioSalud.id = :idServicio and d.dia.dia <= :idDia and d.mes.idMes = :idMes and d.revisarConsolidador = true"),
    @NamedQuery(name = "DetalleRemesas.getRemesasComunaAprobadosConsolidadorDiaMesProgramaAnoServicio", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.comuna.servicioSalud.id = :idServicio and d.dia.dia <= :idDia and d.mes.idMes = :idMes and d.revisarConsolidador = true"),
    @NamedQuery(name = "DetalleRemesas.getRemesasMesActualByMesProgramaAnoServicioSubtituloMunicipal", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.comuna.servicioSalud.id = :idServicio and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.mes.idMes BETWEEN :idMesDesde and :idMesHasta"),
    @NamedQuery(name = "DetalleRemesas.getRemesasMesActualByMesProgramaAnoServicioSubtituloMunicipalConsolidador", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.comuna.servicioSalud.id = :idServicio and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.revisarConsolidador = :revisarConsolidador and d.mes.idMes BETWEEN :idMesDesde and :idMesHasta"),
    @NamedQuery(name = "DetalleRemesas.getRemesasByProgramaAnoMesEstablecimientoSubtitulo", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.establecimiento.codigo = :codEstablecimiento and d.mes.idMes = :idMes and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo"),
    @NamedQuery(name = "DetalleRemesas.getRemesasPagadasComunaProgramaMesActual", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.comuna.id = :idComuna and d.remesaPagada = :remesaPagada and d.mes.idMes = :idMes"),
    @NamedQuery(name = "DetalleRemesas.getRemesasComunaLaFecha", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.comuna.id = :idComuna and d.mes.idMes <= :idMes and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo"),
    @NamedQuery(name = "DetalleRemesas.getDetalleRemesaEstimadaByProgramaAnoEstablecimientoSubtitulo", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.establecimiento.id = :idEstablecimiento and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.estimada = true order by d.cuota.numeroCuota asc"),
    @NamedQuery(name = "DetalleRemesas.getDetalleRemesaEstimadaByProgramaAnoComunaSubtitulo", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.comuna.id = :idComuna and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.estimada = true order by d.cuota.numeroCuota asc"),
    @NamedQuery(name = "DetalleRemesas.groupMontoRemesaProgramaComuna", query = "SELECT d.comuna.id, SUM(d.montoRemesa) FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.comuna.id = :idComuna and d.mes.idMes <= :mes and d.remesaPagada = true GROUP BY d.comuna.id"),
    @NamedQuery(name = "DetalleRemesas.groupMontoRemesaProgramaSubtituloComuna", query = "SELECT d.comuna.id, SUM(d.montoRemesa) FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.subtitulo.idTipoSubtitulo = :idSubtitulo and d.comuna.id = :idComuna and d.mes.idMes <= :mes and d.remesaPagada = true GROUP BY d.comuna.id"),
    @NamedQuery(name = "DetalleRemesas.groupMontoRemesaProgramaComponenteSubtituloComuna", query = "SELECT d.comuna.id, SUM(d.montoRemesa) FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.componente.id = :idComponente and d.subtitulo.idTipoSubtitulo = :idSubtitulo and d.comuna.id = :idComuna and d.mes.idMes <= :mes and d.remesaPagada = true GROUP BY d.comuna.id"),
    @NamedQuery(name = "DetalleRemesas.groupMontoRemesaProgramaSubtituloEstablecimiento", query = "SELECT d.establecimiento.id, SUM(d.montoRemesa) FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.subtitulo.idTipoSubtitulo = :idSubtitulo and d.establecimiento.id = :idEstablecimiento and d.mes.idMes <= :mes and d.remesaPagada = true GROUP BY d.establecimiento.id"),
    @NamedQuery(name = "DetalleRemesas.groupMontoRemesaProgramaComponenteSubtituloEstablecimientoDiaMes", query = "SELECT d.establecimiento.id, SUM(d.montoRemesa) FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.componente.id = :idComponente and d.subtitulo.idTipoSubtitulo = :idSubtitulo and d.establecimiento.id = :idEstablecimiento and d.mes.idMes <= :mes and d.dia.dia <= :dia and d.remesaPagada = true GROUP BY d.establecimiento.id"),
    @NamedQuery(name = "DetalleRemesas.groupMontoRemesaProgramaComponenteSubtituloComunaDiaMes", query = "SELECT d.comuna.id, SUM(d.montoRemesa) FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.componente.id = :idComponente and d.subtitulo.idTipoSubtitulo = :idSubtitulo and d.comuna.id = :idComuna and d.mes.idMes <= :mes and d.dia.dia <= :dia and d.remesaPagada = true GROUP BY d.comuna.id"),
    @NamedQuery(name = "DetalleRemesas.groupMontoRemesaProgramaComponenteSubtituloEstablecimiento", query = "SELECT d.establecimiento.id, SUM(d.montoRemesa) FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.componente.id = :idComponente and d.subtitulo.idTipoSubtitulo = :idSubtitulo and d.establecimiento.id = :idEstablecimiento and d.mes.idMes <= :mes and d.remesaPagada = true GROUP BY d.establecimiento.id"),
    @NamedQuery(name = "DetalleRemesas.groupMontoRemesaProgramaEstablecimiento", query = "SELECT d.establecimiento.id, SUM(d.montoRemesa) FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.establecimiento.id = :idEstablecimiento and d.mes.idMes <= :mes and d.remesaPagada = true GROUP BY d.establecimiento.id"),
    @NamedQuery(name = "DetalleRemesas.groupMontoRemesaNoPagadasProgramaSubtituloEstablecimiento", query = "SELECT d.establecimiento.id, SUM(d.montoRemesa) FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.subtitulo.idTipoSubtitulo = :idSubtitulo and d.establecimiento.id = :idEstablecimiento and d.mes.idMes <= :mes and d.remesaPagada = false GROUP BY d.establecimiento.id"),
    @NamedQuery(name = "DetalleRemesas.groupMontoRemesaNoPagadasProgramaSubtituloComunaMesHasta", query = "SELECT d.comuna.id, SUM(d.montoRemesa) FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.subtitulo.idTipoSubtitulo = :idSubtitulo and d.comuna.id = :idComuna and d.mes.idMes <= :mes and d.remesaPagada = false GROUP BY d.comuna.id"),
    @NamedQuery(name = "DetalleRemesas.getRemesasPagadasComunasProgramaAnoServicioSubtituloMesHasta", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.comuna.servicioSalud.id = :idServicio and d.mes.idMes <= :idMes and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.remesaPagada = :remesaPagada"),
    @NamedQuery(name = "DetalleRemesas.getRemesasPagadasComunasProgramaAnoServicioComponenteSubtituloMesHasta", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.comuna.servicioSalud.id = :idServicio and d.mes.idMes <= :idMes and d.componente.id = :idComponente and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.remesaPagada = :remesaPagada"),
    @NamedQuery(name = "DetalleRemesas.getRemesasPagadasEstablecimientosProgramaAnoServicioSubtituloMesHasta", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.establecimiento.servicioSalud.id = :idServicio and d.mes.idMes <= :idMes and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.remesaPagada = :remesaPagada"),
    @NamedQuery(name = "DetalleRemesas.getRemesasPagadasEstablecimientosProgramaAnoServicioComponenteSubtituloMesHasta", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.establecimiento.servicioSalud.id = :idServicio and d.mes.idMes <= :idMes and d.componente.id = :idComponente and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.remesaPagada = :remesaPagada"),
    @NamedQuery(name = "DetalleRemesas.getRemesasPagadasEstablecimientosProgramaAnoEstablecimientoComponenteSubtituloMesHasta", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.establecimiento.id = :idEstablecimiento and d.mes.idMes <= :idMes and d.componente.id = :idComponente and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.remesaPagada = :remesaPagada"),
    @NamedQuery(name = "DetalleRemesas.getRemesasPagadasComunasProgramaAnoServicioSubtituloMesActual", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.comuna.servicioSalud.id = :idServicio and d.mes.idMes = :idMes and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.remesaPagada = :remesaPagada"),
    @NamedQuery(name = "DetalleRemesas.getRemesasPagadasEstablecimientosProgramaAnoServicioSubtituloMesActual", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.establecimiento.servicioSalud.id = :idServicio and d.mes.idMes = :idMes and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.remesaPagada = :remesaPagada"),
    @NamedQuery(name = "DetalleRemesas.getRemesasComunaSubtituloCuota", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.comuna.id = :idComuna and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.cuota.numeroCuota = :numeroCuota"),
    @NamedQuery(name = "DetalleRemesas.getRemesasNoPagadasComunaSubtituloCuota", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.remesaPagada = false and d.comuna.id = :idComuna and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.cuota.numeroCuota = :numeroCuota"),
    @NamedQuery(name = "DetalleRemesas.getRemesasNoPagadasEstablecimientoSubtituloCuota", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.remesaPagada = false and d.establecimiento.id = :idEstablecimiento and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.cuota.numeroCuota = :numeroCuota"),
    @NamedQuery(name = "DetalleRemesas.getRemesasComunaSubtitulo", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.comuna.id = :idComuna and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo"),
    @NamedQuery(name = "DetalleRemesas.getRemesasEstablecimientoSubtitulo", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.establecimiento.id = :idEstablecimiento and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo"),
    @NamedQuery(name = "DetalleRemesas.getRemesasPorPagarDiaMesActual", query = "SELECT d FROM DetalleRemesas d WHERE d.remesaPagada = :remesaPagada and d.dia.dia <= :idDia and d.mes.idMes = :idMes and d.revisarConsolidador = true"),
    @NamedQuery(name = "DetalleRemesas.getRemesasPagadasComunasProcesoServicioProgramaSubtituloDiaMes", query = "SELECT d FROM DetalleRemesas d WHERE d.remesa.idRemesa = :idProceso and d.comuna.servicioSalud.id = :idServicio and d.programaAno.idProgramaAno = :idProgramaAno and d.subtitulo.idTipoSubtitulo = :idSubtitulo and d.remesaPagada = true and d.dia.dia <= :idDia and d.mes.idMes = :idMes and d.revisarConsolidador = true"),
    @NamedQuery(name = "DetalleRemesas.getRemesasPagadasComunasProcesoComunaProgramaSubtituloDiaMes", query = "SELECT d FROM DetalleRemesas d WHERE d.remesa.idRemesa = :idProceso and d.comuna.id = :idComuna and d.programaAno.idProgramaAno = :idProgramaAno and d.subtitulo.idTipoSubtitulo = :idSubtitulo and d.remesaPagada = true and d.dia.dia <= :idDia and d.mes.idMes = :idMes and d.revisarConsolidador = true"),
    @NamedQuery(name = "DetalleRemesas.getRemesasPagadasEstablecimientosProcesoServicioProgramaSubtituloDiaMes", query = "SELECT d FROM DetalleRemesas d WHERE d.remesa.idRemesa = :idProceso and d.establecimiento.servicioSalud.id = :idServicio and d.programaAno.idProgramaAno = :idProgramaAno and d.subtitulo.idTipoSubtitulo = :idSubtitulo and d.remesaPagada = true and d.dia.dia <= :idDia and d.mes.idMes = :idMes and d.revisarConsolidador = true"),
    @NamedQuery(name = "DetalleRemesas.getRemesasPagadasEstablecimientosProcesoEstablecimientoProgramaSubtituloDiaMes", query = "SELECT d FROM DetalleRemesas d WHERE d.remesa.idRemesa = :idProceso and d.establecimiento.id = :idEstablecimiento and d.programaAno.idProgramaAno = :idProgramaAno and d.subtitulo.idTipoSubtitulo = :idSubtitulo and d.remesaPagada = true and d.dia.dia <= :idDia and d.mes.idMes = :idMes and d.revisarConsolidador = true"),
    @NamedQuery(name = "DetalleRemesas.getRemesasRechazadasByIdProceso", query = "SELECT d FROM DetalleRemesas d WHERE d.remesa.idRemesa = :idProceso"),
    @NamedQuery(name = "DetalleRemesas.getProgramaAnoByRemesaPagada", query = "SELECT DISTINCT d.programaAno FROM DetalleRemesas d WHERE d.remesa.idRemesa = :idProceso and d.revisarConsolidador = true and d.remesaPagada = true"),
    @NamedQuery(name = "DetalleRemesas.getRemesasProgramaAnoComponenteSubtituloEstablecimiento", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.componente.id = :idComponente and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.establecimiento.id = :idEstablecimiento"),
    @NamedQuery(name = "DetalleRemesas.getRemesasPendientesConsolidadorProgramaAnoComponenteSubtituloEstablecimiento", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.componente.id = :idComponente and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.establecimiento.id = :idEstablecimiento and d.remesaPagada = false and d.revisarConsolidador = false order by d.fecha asc"),
    @NamedQuery(name = "DetalleRemesas.getRemesasPendientesConsolidadorProgramaAnoComponenteSubtituloComuna", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.componente.id = :idComponente and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.comuna.id = :idComuna and d.remesaPagada = false and d.revisarConsolidador = false order by d.fecha asc"),
    @NamedQuery(name = "DetalleRemesas.findByIdCuota", query = "SELECT d FROM DetalleRemesas d WHERE d.cuota.id= :idCuota"),})
public class DetalleRemesas implements Serializable {
    private static final long serialVersionUID = 1L;
   @Id
   @Column(name="id_detalle_remesa", unique=true, nullable=false)
   @GeneratedValue
    private Integer idDetalleRemesa;
    @Column(name = "monto_remesa")
    private Long montoRemesa;
    @Basic(optional = false)
    @Column(name = "remesa_pagada")
    private boolean remesaPagada;
    @Basic(optional = false)
    @Column(name = "estimada")
    private boolean estimada;
    @Basic(optional = false)
    @Column(name = "revisar_consolidador")
    private boolean revisarConsolidador;
    @JoinColumn(name = "subtitulo", referencedColumnName = "id_tipo_subtitulo")
    @ManyToOne
    private TipoSubtitulo subtitulo;
    @Basic(optional = false)
    @Column(name = "bloqueado")
    private boolean bloqueado;
    @JoinColumn(name = "programa_ano", referencedColumnName = "id_programa_ano")
    @ManyToOne
    private ProgramaAno programaAno;
    @JoinColumn(name = "mes", referencedColumnName = "id_mes")
    @ManyToOne
    private Mes mes;
    @JoinColumn(name = "establecimiento", referencedColumnName = "id")
    @ManyToOne
    private Establecimiento establecimiento;
    @JoinColumn(name = "dia", referencedColumnName = "id")
    @ManyToOne
    private Dia dia;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @JoinColumn(name = "comuna", referencedColumnName = "id")
    @ManyToOne
    private Comuna comuna;
    @JoinColumn(name = "cuota", referencedColumnName = "id")
    @ManyToOne
    private Cuota cuota;
    @JoinColumn(name = "componente", referencedColumnName = "id")
    @ManyToOne
    private Componente componente;
    @JoinColumn(name = "remesa", referencedColumnName = "id_remesa")
    @ManyToOne
    private Remesas remesa;
    @JoinColumn(name = "remesa_profesional", referencedColumnName = "id_remesa")
    @ManyToOne
    private Remesas remesaProfesional;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "remesa")
    private Set<RemesaConvenios> remesaConvenios;
    
    public DetalleRemesas() {
    }

    public DetalleRemesas(Integer idDetalleRemesa) {
        this.idDetalleRemesa = idDetalleRemesa;
    }

    public DetalleRemesas(Integer idDetalleRemesa, boolean remesaPagada) {
        this.idDetalleRemesa = idDetalleRemesa;
        this.remesaPagada = remesaPagada;
    }

    public Integer getIdDetalleRemesa() {
        return idDetalleRemesa;
    }

    public void setIdDetalleRemesa(Integer idDetalleRemesa) {
        this.idDetalleRemesa = idDetalleRemesa;
    }

    public Long getMontoRemesa() {
        return montoRemesa;
    }

    public void setMontoRemesa(Long montoRemesa) {
        this.montoRemesa = montoRemesa;
    }

    public boolean getRemesaPagada() {
        return remesaPagada;
    }

    public void setRemesaPagada(boolean remesaPagada) {
        this.remesaPagada = remesaPagada;
    }

    public TipoSubtitulo getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(TipoSubtitulo subtitulo) {
        this.subtitulo = subtitulo;
    }

    public ProgramaAno getProgramaAno() {
        return programaAno;
    }

    public void setProgramaAno(ProgramaAno programaAno) {
        this.programaAno = programaAno;
    }

    public Mes getMes() {
        return mes;
    }

    public void setMes(Mes mes) {
        this.mes = mes;
    }

    public Establecimiento getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(Establecimiento establecimiento) {
        this.establecimiento = establecimiento;
    }

    public Dia getDia() {
        return dia;
    }

    public void setDia(Dia dia) {
        this.dia = dia;
    }

    public Comuna getComuna() {
        return comuna;
    }

    public void setComuna(Comuna comuna) {
        this.comuna = comuna;
    }
    public Cuota getCuota() {
        return cuota;
    }

    public void setCuota(Cuota cuota) {
        this.cuota = cuota;
    }
    
    public boolean isEstimada() {
		return estimada;
	}

	public void setEstimada(boolean estimada) {
		this.estimada = estimada;
	}

	public boolean isRevisarConsolidador() {
		return revisarConsolidador;
	}

	public void setRevisarConsolidador(boolean revisarConsolidador) {
		this.revisarConsolidador = revisarConsolidador;
	}

	public Componente getComponente() {
		return componente;
	}

	public void setComponente(Componente componente) {
		this.componente = componente;
	}
	
	@XmlTransient
	public Set<RemesaConvenios> getRemesaConvenios() {
		return remesaConvenios;
	}

	public void setRemesaConvenios(Set<RemesaConvenios> remesaConvenios) {
		this.remesaConvenios = remesaConvenios;
	}

	public Date getFecha() {
		return fecha;
	}

	public boolean isBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(boolean bloqueado) {
		this.bloqueado = bloqueado;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public Remesas getRemesa() {
		return remesa;
	}

	public void setRemesa(Remesas remesa) {
		this.remesa = remesa;
	}
	
	public Remesas getRemesaProfesional() {
		return remesaProfesional;
	}

	public void setRemesaProfesional(Remesas remesaProfesional) {
		this.remesaProfesional = remesaProfesional;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idDetalleRemesa != null ? idDetalleRemesa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleRemesas)) {
            return false;
        }
        DetalleRemesas other = (DetalleRemesas) object;
        if ((this.idDetalleRemesa == null && other.idDetalleRemesa != null) || (this.idDetalleRemesa != null && !this.idDetalleRemesa.equals(other.idDetalleRemesa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.DetalleRemesas[ idDetalleRemesa=" + idDetalleRemesa + " ]";
    }
    
}