package cl.minsal.divap.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
    @NamedQuery(name = "DetalleRemesas.findByIdDetalleRemesa", query = "SELECT d FROM DetalleRemesas d WHERE d.idDetalleRemesa = :idDetalleRemesa"),
    @NamedQuery(name = "DetalleRemesas.findByMontoRemesa", query = "SELECT d FROM DetalleRemesas d WHERE d.montoRemesa = :montoRemesa"),
    @NamedQuery(name = "DetalleRemesas.findByRemesaPagada", query = "SELECT d FROM DetalleRemesas d WHERE d.remesaPagada = :remesaPagada"),
    @NamedQuery(name = "DetalleRemesas.getRemesasPagadasComunaLaFecha", query = "SELECT d FROM DetalleRemesas d WHERE d.programaAno.idProgramaAno = :idProgramaAno and d.comuna.id = :idComuna and d.mes.idMes < :idMes and d.subtitulo.idTipoSubtitulo = :idTipoSubtitulo and d.remesaPagada = :remesaPagada")})
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