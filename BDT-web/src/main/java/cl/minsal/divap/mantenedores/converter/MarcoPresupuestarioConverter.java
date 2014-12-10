package cl.minsal.divap.mantenedores.converter;

import cl.minsal.divap.model.MarcoPresupuestario;
import cl.minsal.divap.mantenedores.facade.MarcoPresupuestarioFacade;
import cl.minsal.divap.mantenedores.bean.util.JsfUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

@Named
public class MarcoPresupuestarioConverter implements Converter {

    @EJB
    private MarcoPresupuestarioFacade ejbFacade;

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
        if (object instanceof MarcoPresupuestario) {
            MarcoPresupuestario o = (MarcoPresupuestario) object;
            return getStringKey(o.getIdMarcoPresupuestario());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), MarcoPresupuestario.class.getName()});
            return null;
        }
    }

}
