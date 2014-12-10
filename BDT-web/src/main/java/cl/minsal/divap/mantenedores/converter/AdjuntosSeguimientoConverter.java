package cl.minsal.divap.mantenedores.converter;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.bean.util.JsfUtil;
import cl.minsal.divap.mantenedores.facade.AdjuntosSeguimientoFacade;
import cl.minsal.divap.model.AdjuntosSeguimiento;

@Named
public class AdjuntosSeguimientoConverter implements Converter {

    @EJB
    private AdjuntosSeguimientoFacade ejbFacade;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0 || JsfUtil.isDummySelectItem(component, value)) {
            return null;
        }
        return this.ejbFacade.find(getKey(value));
    }

    java.lang.Integer getKey(String value) {
        java.lang.Integer key;
        key = Integer.valueOf(value);
        return key;
    }

    String getStringKey(java.lang.Integer value) {
        StringBuffer sb = new StringBuffer();
        sb.append(value);
        return sb.toString();
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null
                || (object instanceof String && ((String) object).length() == 0)) {
            return null;
        }
        if (object instanceof AdjuntosSeguimiento) {
            AdjuntosSeguimiento o = (AdjuntosSeguimiento) object;
            return getStringKey(o.getIdAdjuntosSeguimiento());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), AdjuntosSeguimiento.class.getName()});
            return null;
        }
    }

}
