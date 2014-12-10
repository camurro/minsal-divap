package cl.minsal.divap.mantenedores.converter;

import cl.minsal.divap.model.ProgramaMunicipalCoreComponente;
import cl.minsal.divap.mantenedores.facade.ProgramaMunicipalCoreComponenteFacade;
import cl.minsal.divap.mantenedores.bean.util.JsfUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

@Named
public class ProgramaMunicipalCoreComponenteConverter implements Converter {

    @EJB
    private ProgramaMunicipalCoreComponenteFacade ejbFacade;

    private static final String SEPARATOR = "#";
    private static final String SEPARATOR_ESCAPED = "\\#";

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0 || JsfUtil.isDummySelectItem(component, value)) {
            return null;
        }
        return this.ejbFacade.find(getKey(value));
    }

    cl.minsal.divap.model.ProgramaMunicipalCoreComponentePK getKey(String value) {
        cl.minsal.divap.model.ProgramaMunicipalCoreComponentePK key;
        String values[] = value.split(SEPARATOR_ESCAPED);
        key = new cl.minsal.divap.model.ProgramaMunicipalCoreComponentePK();
        key.setProgramaMunicipalCore(Integer.parseInt(values[0]));
        key.setComponente(Integer.parseInt(values[1]));
        return key;
    }

    String getStringKey(cl.minsal.divap.model.ProgramaMunicipalCoreComponentePK value) {
        StringBuffer sb = new StringBuffer();
        sb.append(value.getProgramaMunicipalCore());
        sb.append(SEPARATOR);
        sb.append(value.getComponente());
        return sb.toString();
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null
                || (object instanceof String && ((String) object).length() == 0)) {
            return null;
        }
        if (object instanceof ProgramaMunicipalCoreComponente) {
            ProgramaMunicipalCoreComponente o = (ProgramaMunicipalCoreComponente) object;
            return getStringKey(o.getProgramaMunicipalCoreComponentePK());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), ProgramaMunicipalCoreComponente.class.getName()});
            return null;
        }
    }

}
