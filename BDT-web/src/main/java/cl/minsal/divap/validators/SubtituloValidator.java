package cl.minsal.divap.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.primefaces.model.DualListModel;

import minsal.divap.vo.SubtituloVO;


@FacesValidator("minsal.divap.subtituloValidator")
public class SubtituloValidator implements Validator{

	@Override
	public void validate(FacesContext context, UIComponent component, Object value)
			throws ValidatorException {

		if(value == null || value.toString().isEmpty()) {
            return;
        }
		DualListModel<SubtituloVO> subtitulos = (DualListModel<SubtituloVO>) value;
		if(subtitulos.getTarget().size() == 0){
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de Validación, debe seleccionar un subtítulo", 
                    value + "No se ha seleccionado ningún componente"));
		}
		
	}

}
