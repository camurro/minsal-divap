package cl.minsal.divap.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.primefaces.model.DualListModel;

@FacesValidator("minsal.divap.rolUsuarioValidator")
public class RolUsuarioValidator implements Validator{

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		
		if(value == null || value.toString().isEmpty()) {
            return;
        }
		
		DualListModel<String> roles = (DualListModel<String>) value;
		if(roles.getTarget().size() == 0){
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de Validación", 
                    value + "No se ha seleccionado ningún rol"));
		}
		
		
	}

}
