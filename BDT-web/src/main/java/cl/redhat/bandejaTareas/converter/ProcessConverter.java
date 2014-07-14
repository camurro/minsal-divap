package cl.redhat.bandejaTareas.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;


@FacesConverter("processConverter")
public class ProcessConverter implements Converter{

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		return null;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		if(value == null){
			return "";
		}
		String arg = (String)value;
		int position = arg.lastIndexOf(".");
		if(position != -1){
			if(position < arg.length()){
				arg = arg.substring(position);
			}
		}
		return arg;
	}

}
