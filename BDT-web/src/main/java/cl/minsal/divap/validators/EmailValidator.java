package cl.minsal.divap.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import java.util.regex.Pattern;


@FacesValidator("minsal.divap.emailValidator")
public class EmailValidator implements Validator{
	
	private Pattern pattern;
	  
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    
    public EmailValidator(){
    	pattern = Pattern.compile(EMAIL_PATTERN);
    }
	

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		if(value == null || value.toString().isEmpty()) {
            return;
        }
        
        if(value.toString().startsWith(" ") || value.toString().contains(" ")){
        	throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email: Error de Validación", 
                    value + " el email posee espacios;"));
        }
        String valor = value.toString().replace(" ", "");
        valor = valor.replace(";", ",");
        valor = valor.replace("  ", "");
        
        
        	if(!pattern.matcher(valor).matches()) {
        		UIInput uica = (UIInput) component;
        		uica.setValid(false);
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email: Error de Validación", 
                            value + " no es un email valido;"));
                
            }
		
	}

}
