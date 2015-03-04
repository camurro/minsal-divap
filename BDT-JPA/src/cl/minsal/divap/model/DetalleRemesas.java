package cl.minsal.divap.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

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
    @NamedQuery(name = "DetalleRemesas.getRemesasPagadasEstablecimiento", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.establecimiento.id = :idEstablecimiento and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.remesaPagada = :remesaPagada"),
    @NamedQuery(name = "DetalleRemesas.getRemesasPagadasComuna", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.comuna.id = :idComuna and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.remesaPagada = :remesaPagada order by d.cuota.numeroCuota asc"),
    @NamedQuery(name = "DetalleRemesas.getDetalleRemesasByProgramaAnoEstablecimientoSubtitulo", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.establecimiento.id = :idEstablecimiento and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo"),
    @NamedQuery(name = "DetalleRemesas.getRemesasMesActualByMesProgramaAnoServicioSubtitulo", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.establecimiento.servicioSalud.id = :idServicio and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.mes.idMes BETWEEN :idMesDesde and :idMesHasta"),
    @NamedQuery(name = "DetalleRemesas.getRemesasMesActualByMesProgramaAnoServicioSubtituloConsolidador", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.establecimiento.servicioSalud.id = :idServicio and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.revisar_consolidador = :revisarConsolidador and d.mes.idMes BETWEEN :idMesDesde and :idMesHasta"),
    @NamedQuery(name = "DetalleRemesas.getRemesasMesActualByMesProgramaAnoServicioSubtitulo1", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.establecimiento.servicioSalud.id = :idServicio and d.mes.idMes = :idMes and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo"),
    @NamedQuery(name = "DetalleRemesas.getRemesasMesActualByMesProgramaAnoServicioSubtitulo2", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.comuna.servicioSalud.id = :idServicio and d.mes.idMes = :idMes and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo"),
    @NamedQuery(name = "DetalleRemesas.getRemesasMesActualByMesProgramaAnoServicio1", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.establecimiento.servicioSalud.id = :idServicio and d.mes.idMes = :idMes"),
    @NamedQuery(name = "DetalleRemesas.getRemesasMesActualByMesProgramaAnoServicio2", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.comuna.servicioSalud.id = :idServicio and d.mes.idMes = :idMes"),
    @NamedQuery(name = "DetalleRemesas.getRemesasMesActualByMesProgramaAnoServicioSubtituloMunicipal", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.comuna.servicioSalud.id = :idServicio and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.mes.idMes BETWEEN :idMesDesde and :idMesHasta"),
    @NamedQuery(name = "DetalleRemesas.getRemesasMesActualByMesProgramaAnoServicioSubtituloMunicipalConsolidador", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.comuna.servicioSalud.id = :idServicio and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.revisar_consolidador = :revisarConsolidador and d.mes.idMes BETWEEN :idMesDesde and :idMesHasta"),
    @NamedQuery(name = "DetalleRemesas.getRemesasByProgramaAnoMesEstablecimientoSubtitulo", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.establecimiento.codigo = :codEstablecimiento and d.mes.idMes = :idMes and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo"),
    @NamedQuery(name = "DetalleRemesas.getRemesasPagadasComunaProgramaMesActual", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.comuna.id = :idComuna and d.remesaPagada = :remesaPagada and d.mes.idMes = :idMes"),
    @NamedQuery(name = "DetalleRemesas.getRemesasComunaLaFecha", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.comuna.id = :idComuna and d.mes.idMes <= :idMes and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo"),
    @NamedQuery(name = "DetalleRemesas.getDetalleRemesaEstimadaByProgramaAnoEstablecimientoSubtitulo", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.establecimiento.id = :idEstablecimiento and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.estimada = true order by d.cuota.numeroCuota asc"),
    @NamedQuery(name = "DetalleRemesas.getDetalleRemesaEstimadaByProgramaAnoComunaSubtitulo", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.comuna.id = :idComuna and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.estimada = true order by d.cuota.numeroCuota asc"),
    @NamedQuery(name = "DetalleRemesas.groupMontoRemesaProgramaComuna", query = "SELECT d.comuna.id, SUM(d.montoRemesa) FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.comuna.id = :idComuna and d.mes.idMes <= :mes and d.remesaPagada = true GROUP BY d.comuna.id"),
    @NamedQuery(name = "DetalleRemesas.groupMontoRemesaProgramaSubtituloComuna", query = "SELECT d.comuna.id, SUM(d.montoRemesa) FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.subtitulo.idTipoSubtitulo = :idSubtitulo and d.comuna.id = :idComuna and d.mes.idMes <= :mes and d.remesaPagada = true GROUP BY d.comuna.id"),
    @NamedQuery(name = "DetalleRemesas.groupMontoRemesaProgramaSubtituloEstablecimiento", query = "SELECT d.establecimiento.id, SUM(d.montoRemesa) FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.subtitulo.idTipoSubtitulo = :idSubtitulo and d.establecimiento.id = :idEstablecimiento and d.mes.idMes <= :mes and d.remesaPagada = true GROUP BY d.establecimiento.id"),
    @NamedQuery(name = "DetalleRemesas.groupMontoRemesaProgramaEstablecimiento", query = "SELECT d.establecimiento.id, SUM(d.montoRemesa) FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.establecimiento.id = :idEstablecimiento and d.mes.idMes <= :mes and d.remesaPagada = true GROUP BY d.establecimiento.id"),
    @NamedQuery(name = "DetalleRemesas.groupMontoRemesaNoPagadasProgramaSubtituloEstablecimiento", query = "SELECT d.establecimiento.id, SUM(d.montoRemesa) FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.subtitulo.idTipoSubtitulo = :idSubtitulo and d.establecimiento.id = :idEstablecimiento and d.mes.idMes <= :mes and d.remesaPagada = false GROUP BY d.establecimiento.id"),
    @NamedQuery(name = "DetalleRemesas.groupMontoRemesaNoPagadasProgramaSubtituloComunaMesHasta", query = "SELECT d.comuna.id, SUM(d.montoRemesa) FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.subtitulo.idTipoSubtitulo = :idSubtitulo and d.comuna.id = :idComuna and d.mes.idMes <= :mes and d.remesaPagada = false GROUP BY d.comuna.id"),
    @NamedQuery(name = "DetalleRemesas.getRemesasPagadasComunasProgramaAnoServicioSubtituloMesHasta", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.comuna.servicioSalud.id = :idServicio and d.mes.idMes <= :idMes and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.remesaPagada = :remesaPagada"),
    @NamedQuery(name = "DetalleRemesas.getRemesasPagadasEstablecimientosProgramaAnoServicioSubtituloMesHasta", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.establecimiento.servicioSalud.id = :idServicio and d.mes.idMes <= :idMes and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.remesaPagada = :remesaPagada"),
    @NamedQuery(name = "DetalleRemesas.getRemesasPagadasComunasProgramaAnoServicioSubtituloMesActual", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.comuna.servicioSalud.id = :idServicio and d.mes.idMes = :idMes and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.remesaPagada = :remesaPagada"),
    @NamedQuery(name = "DetalleRemesas.getRemesasPagadasEstablecimientosProgramaAnoServicioSubtituloMesActual", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.establecimiento.servicioSalud.id = :idServicio and d.mes.idMes = :idMes and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.remesaPagada = :remesaPagada"),
    @NamedQuery(name = "DetalleRemesas.getRemesasComunaSubtituloCuota", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.comuna.id = :idComuna and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.cuota.numeroCuota = :numeroCuota"),
    @NamedQuery(name = "DetalleRemesas.getRemesasNoPagadasComunaSubtituloCuota", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.remesaPagada = false and d.comuna.id = :idComuna and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.cuota.numeroCuota = :numeroCuota")})
public class DetalleRemesas implements Serializable {
    private static final long serialVersionUID = 1L;
   @Id
   @Column(name="id_detalle_remesa", unique=true, nullable=false)
   @GeneratedValue
    private Integer idDetalleRemesa;
    @Column(name = "monto_remesa")
    private Integer montoRemesa;
    @Basic(optional = false)
    @Column(name = "remesa_pagada")
    private boolean remesaPagada;
    @Basic(optional = false)
    @Column(name = "estimada")
    private boolean estimada;
    @Basic(optional = false)
    @Column(name = "revisar_consolidador")
    private boolean revisar_consolidador;
    @JoinColumn(name = "subtitulo", referencedColumnName = "id_tipo_subtitulo")
    @ManyToOne
    private TipoSubtitulo subtitulo;
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
    @JoinColumn(name = "comuna", referencedColumnName = "id")
    @ManyToOne
    private Comuna comuna;
    @JoinColumn(name = "cuota", referencedColumnName = "id")
    @ManyToOne
    private Cuota cuota;

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

    public Integer getMontoRemesa() {
        return montoRemesa;
    }

    public void setMontoRemesa(Integer montoRemesa) {
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

	
	public boolean isRevisar_consolidador() {
		return revisar_consolidador;
	}

	public void setRevisar_consolidador(boolean revisar_consolidador) {
		this.revisar_consolidador = revisar_consolidador;
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