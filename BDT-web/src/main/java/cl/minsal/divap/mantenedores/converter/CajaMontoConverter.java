package cl.minsal.divap.mantenedores.converter;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

import cl.minsal.divap.mantenedores.bean.util.JsfUtil;
import cl.minsal.divap.mantenedores.facade.CajaMontoFacade;
import cl.minsal.divap.model.CajaMonto;

@Named
public class CajaMontoConverter implements Converter {

    @EJB
    private CajaMontoFacade ejbFacade;

    private static final String SEPARATOR = "#";
    private static final String SEPARATOR_ESCAPED = "\\#";

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0 || JsfUtil.isDummySelectItem(component, value)) {
            return null;
        }
        return this.ejbFacade.find(getKey(value));
    }

    cl.minsal.divap.model.CajaMontoPK getKey(String value) {
        cl.minsal.divap.model.CajaMontoPK key;
        String values[] = value.split(SEPARATOR_ESCAPED);
        key = new cl.minsal.divap.model.CajaMontoPK();
        key.setCaja(Integer.parseInt(values[0]));
        key.setMes(Integer.parseInt(values[1]));
        return key;
    }

    String getStringKey(cl.minsal.divap.model.CajaMontoPK value) {
        StringBuffer sb = new StringBuffer();
        sb.append(value.getCaja());
        sb.append(SEPARATOR);
        sb.append(value.getMes());
        return sb.toString();
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null
                || (object instanceof String && ((String) object).length() == 0)) {
            return null;
        }
        if (object instanceof CajaMonto) {
            CajaMonto o = (CajaMonto) object;
            return getStringKey(o.getCajaMontoPK());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), CajaMonto.class.getName()});
            return null;
        }
    }

}
