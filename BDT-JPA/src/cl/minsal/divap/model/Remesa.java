/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.minsal.divap.model;

import java.io.Serializable;
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
 * @author Aldo
 */
@Entity
@Table(name = "remesa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Remesa.findAll", query = "SELECT r FROM Remesa r"),
    @NamedQuery(name = "Remesa.findByIdRemesa", query = "SELECT r FROM Remesa r WHERE r.idremesa = :idRemesa"),
    @NamedQuery(name = "Remesa.findByAnio", query = "SELECT r FROM Remesa r WHERE r.idprograma.ano.ano = :anio"),
    @NamedQuery(name = "Remesa.findByValorDia09", query = "SELECT r FROM Remesa r WHERE r.valordia09 = :valorDia09"),
    @NamedQuery(name = "Remesa.findByValorDia24", query = "SELECT r FROM Remesa r WHERE r.valordia24 = :valorDia24"),
    @NamedQuery(name = "Remesa.findByValorDia28", query = "SELECT r FROM Remesa r WHERE r.valordia28 = :valorDia28"),
    @NamedQuery(name = "Remesa.findByIdProgramaAnoIdServicioIdSubtitulo", query = "SELECT r FROM Remesa r WHERE r.idprograma.idProgramaAno = :idProgramaAno and r.idserviciosalud.id = :idServicioSalud  and r.tipoSubtitulo.idTipoSubtitulo = :idTipoSubtitulo ORDER BY r.idmes.idMes ASC")})
public class Remesa implements Serializable  {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "idremesa",unique=true, nullable=false)
	@GeneratedValue
    private Integer idremesa;
    @Column(name = "valordia09")
    private Integer valordia09;
    @Column(name = "valordia24")
    private Integer valordia24;
    @Column(name = "valordia28")
    private Integer valordia28;
    @JoinColumn(name = "tipo_subtitulo", referencedColumnName = "id_tipo_subtitulo")
    @ManyToOne
    private TipoSubtitulo tipoSubtitulo;
    @JoinColumn(name = "idserviciosalud", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ServicioSalud idserviciosalud;
    @JoinColumn(name = "idprograma", referencedColumnName = "id_programa_ano")
    @ManyToOne
    private ProgramaAno idprograma;
    @JoinColumn(name = "idmes", referencedColumnName = "id_mes")
    @ManyToOne
    private Mes idmes;
    @JoinColumn(name = "idestablecimiento", referencedColumnName = "id")
    @ManyToOne
    private Establecimiento idestablecimiento;
    @JoinColumn(name = "idcomuna", referencedColumnName = "id")
    @ManyToOne
    private Comuna idcomuna;

    public Remesa() {
    }

    public Remesa(Integer idremesa) {
        this.idremesa = idremesa;
    }

    public Integer getIdremesa() {
        return idremesa;
    }

    public void setIdremesa(Integer idremesa) {
        this.idremesa = idremesa;
    }

    public Integer getValordia09() {
        return valordia09;
    }

    public void setValordia09(Integer valordia09) {
        this.valordia09 = valordia09;
    }

    public Integer getValordia24() {
        return valordia24;
    }

    public void setValordia24(Integer valordia24) {
        this.valordia24 = valordia24;
    }

    public Integer getValordia28() {
        return valordia28;
    }

    public void setValordia28(Integer valordia28) {
        this.valordia28 = valordia28;
    }

    public TipoSubtitulo getTipoSubtitulo() {
        return tipoSubtitulo;
    }

    public void setTipoSubtitulo(TipoSubtitulo tipoSubtitulo) {
        this.tipoSubtitulo = tipoSubtitulo;
    }

    public ServicioSalud getIdserviciosalud() {
        return idserviciosalud;
    }

    public void setIdserviciosalud(ServicioSalud idserviciosalud) {
        this.idserviciosalud = idserviciosalud;
    }

    public ProgramaAno getIdprograma() {
        return idprograma;
    }

    public void setIdprograma(ProgramaAno idprograma) {
        this.idprograma = idprograma;
    }

    public Mes getIdmes() {
        return idmes;
    }

    public void setIdmes(Mes idmes) {
        this.idmes = idmes;
    }

    public Establecimiento getIdestablecimiento() {
        return idestablecimiento;
    }

    public void setIdestablecimiento(Establecimiento idestablecimiento) {
        this.idestablecimiento = idestablecimiento;
    }

    public Comuna getIdcomuna() {
        return idcomuna;
    }

    public void setIdcomuna(Comuna idcomuna) {
        this.idcomuna = idcomuna;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idremesa != null ? idremesa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Remesa)) {
            return false;
        }
        Remesa other = (Remesa) object;
        if ((this.idremesa == null && other.idremesa != null) || (this.idremesa != null && !this.idremesa.equals(other.idremesa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.Remesa[ idremesa=" + idremesa + " ]";
    }
    
}
