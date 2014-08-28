/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.minsal.divap.model;

import java.io.Serializable;
import java.math.BigInteger;

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
 * @author Aldo
 */
@Entity
@Table(name = "remesa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Remesa.findAll", query = "SELECT r FROM Remesa r"),
    @NamedQuery(name = "Remesa.findByIdRemesa", query = "SELECT r FROM Remesa r WHERE r.idRemesa = :idRemesa"),
    @NamedQuery(name = "Remesa.findByAnio", query = "SELECT r FROM Remesa r WHERE r.anio = :anio"),
    @NamedQuery(name = "Remesa.findByValorDia09", query = "SELECT r FROM Remesa r WHERE r.valorDia09 = :valorDia09"),
    @NamedQuery(name = "Remesa.findByValorDia24", query = "SELECT r FROM Remesa r WHERE r.valorDia24 = :valorDia24"),
    @NamedQuery(name = "Remesa.findByValorDia28", query = "SELECT r FROM Remesa r WHERE r.valorDia28 = :valorDia28")})
public class Remesa implements Serializable  {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "idRemesa",unique=true, nullable=false)
	@GeneratedValue
    private Integer idRemesa;
    @Column(name = "anio")
    private Integer anio;
    @Column(name = "valorDia09")
    private long valorDia09;
    @Column(name = "valorDia24")
    private long valorDia24;
    @Column(name = "valorDia28")
    private long valorDia28;
    @JoinColumn(name = "idServicioSalud", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ServicioSalud idServicioSalud;
    @JoinColumn(name = "idPrograma", referencedColumnName = "id")
    @ManyToOne
    private Programa idPrograma;
    @JoinColumn(name = "idMes", referencedColumnName = "id_mes")
    @ManyToOne
    private Mes idMes;
    @JoinColumn(name = "idEstablecimiento", referencedColumnName = "id")
    @ManyToOne
    private Establecimiento idEstablecimiento;
    @JoinColumn(name = "idComuna", referencedColumnName = "id")
    @ManyToOne
    private Comuna idComuna;

    public Remesa() {
    }

    public Remesa(Integer idRemesa) {
        this.idRemesa = idRemesa;
    }

    public Integer getIdRemesa() {
        return idRemesa;
    }

    public void setIdRemesa(Integer idRemesa) {
        this.idRemesa = idRemesa;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public long getValorDia09() {
        return valorDia09;
    }

    public void setValorDia09(long valorDia09) {
        this.valorDia09 = valorDia09;
    }

    public long getValorDia24() {
        return valorDia24;
    }

    public void setValorDia24(long valorDia24) {
        this.valorDia24 = valorDia24;
    }

    public long getValorDia28() {
        return valorDia28;
    }

    public void setValorDia28(long valorDia28) {
        this.valorDia28 = valorDia28;
    }

    public ServicioSalud getIdServicioSalud() {
        return idServicioSalud;
    }

    public void setIdServicioSalud(ServicioSalud idServicioSalud) {
        this.idServicioSalud = idServicioSalud;
    }

    public Programa getIdPrograma() {
        return idPrograma;
    }

    public void setIdPrograma(Programa idPrograma) {
        this.idPrograma = idPrograma;
    }

    public Mes getIdMes() {
        return idMes;
    }

    public void setIdMes(Mes idMes) {
        this.idMes = idMes;
    }

    public Establecimiento getIdEstablecimiento() {
        return idEstablecimiento;
    }

    public void setIdEstablecimiento(Establecimiento idEstablecimiento) {
        this.idEstablecimiento = idEstablecimiento;
    }

    public Comuna getIdComuna() {
        return idComuna;
    }

    public void setIdComuna(Comuna idComuna) {
        this.idComuna = idComuna;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRemesa != null ? idRemesa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Remesa)) {
            return false;
        }
        Remesa other = (Remesa) object;
        if ((this.idRemesa == null && other.idRemesa != null) || (this.idRemesa != null && !this.idRemesa.equals(other.idRemesa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication2.Remesa[ idRemesa=" + idRemesa + " ]";
    }
    
    public Remesa(Integer idRemesa,Programa programa,ServicioSalud servicioSalud ,Integer anio,Mes mes,long valor09,long valor24,long valor28)
    {
    	setIdRemesa(idRemesa);
    	setIdPrograma(programa);
    	setIdServicioSalud(servicioSalud);
    	setAnio(anio);
    	setIdMes(mes);
    	
    	setValorDia09(valor09);
    	setValorDia24(valor24);
    	setValorDia28(valor28);
    	
    }
    
}
