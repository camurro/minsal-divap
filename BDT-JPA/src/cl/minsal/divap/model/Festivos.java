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
@Table(name = "festivos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Festivos.findAll", query = "SELECT f FROM Festivos f"),
    @NamedQuery(name = "Festivos.findById", query = "SELECT f FROM Festivos f WHERE f.id = :id"),
    @NamedQuery(name = "Festivos.findByDiaMesAno", query = "SELECT f FROM Festivos f WHERE f.dia.dia = :dia and f.mes.idMes = :mes and f.ano.ano = :ano")})
public class Festivos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "mes", referencedColumnName = "id_mes")
    @ManyToOne(optional = false)
    private Mes mes;
    @JoinColumn(name = "dia", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Dia dia;
    @JoinColumn(name = "ano", referencedColumnName = "ano")
    @ManyToOne(optional = false)
    private AnoEnCurso ano;

    public Festivos() {
    }

    public Festivos(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Mes getMes() {
        return mes;
    }

    public void setMes(Mes mes) {
        this.mes = mes;
    }

    public Dia getDia() {
        return dia;
    }

    public void setDia(Dia dia) {
        this.dia = dia;
    }

    public AnoEnCurso getAno() {
        return ano;
    }

    public void setAno(AnoEnCurso ano) {
        this.ano = ano;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Festivos)) {
            return false;
        }
        Festivos other = (Festivos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.minsal.divap.model.Festivos[ id=" + id + " ]";
    }
   
}