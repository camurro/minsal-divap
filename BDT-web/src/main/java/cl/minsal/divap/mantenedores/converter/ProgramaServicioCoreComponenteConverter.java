package cl.minsal.divap.mantenedores.converter;

import cl.minsal.divap.model.ProgramaServicioCoreComponente;
import cl.minsal.divap.mantenedores.facade.ProgramaServicioCoreComponenteFacade;
import cl.minsal.divap.mantenedores.bean.util.JsfUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

@Named
public class ProgramaServicioCoreComponenteConverter implements Converter {

    @EJB
    private ProgramaServicioCoreComponenteFacade ejbFacade;

    private static final String SEPARATOR = "#";
    private static final String SEPARATOR_ESCAPED = "\\#";

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0 || JsfUtil.isDummySelectItem(component, value)) {
            return null;
        }
        return this.ejbFacade.find(getKey(value));
    }

    cl.minsal.divap.model.ProgramaServicioCoreComponentePK getKey(String value) {
        cl.minsal.divap.model.ProgramaServicioCoreComponentePK key;
        String values[] = value.split(SEPARATOR_ESCAPED);
        key = new cl.minsal.divap.model.ProgramaServicioCoreComponentePK();
        key.setProgramaServicioCore(Integer.parseInt(values[0]));
        key.setComponente(Integer.parseInt(values[1]));
        key.setSubtitulo(Integer.parseInt(values[2]));
        return key;
    }

    String getStringKey(cl.minsal.divap.model.ProgramaServicioCoreComponentePK value) {
        StringBuffer sb = new StringBuffer();
        sb.append(value.getProgramaServicioCore());
        sb.append(SEPARATOR);
        sb.append(value.getComponente());
        sb.append(SEPARATOR);
        sb.append(value.getSubtitulo());
        return sb.toString();
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null
                || (object instanceof String && ((String) object).length() == 0)) {
            return null;
        }
        if (object instanceof ProgramaServicioCoreComponente) {
            ProgramaServicioCoreComponente o = (ProgramaServicioCoreComponente) object;
            return getStringKey(o.getProgramaServicioCoreComponentePK());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), ProgramaServicioCoreComponente.class.getName()});
            return null;
        }
    }

}
