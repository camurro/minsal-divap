package minsal.divap.model.mappers;

import minsal.divap.vo.PersonaVO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import cl.minsal.divap.model.Persona;

public class PersonaMapper implements Mapper<Persona>{

	@Override
	public Object getSummary(Persona persona) {
		throw new NotImplementedException();
	}

	@Override
	public PersonaVO getBasic(Persona persona) {
		if (persona == null)
		      return null;
		PersonaVO personaVO = new PersonaVO(persona.getIdPersona(), persona.getNombre(), persona.getApellidoPaterno(), persona.getApellidoMaterno());
		if(persona.getEmail() != null){
			personaVO.setCorreo(persona.getEmail().getValor());
		}
		return personaVO;
	}

	@Override
	public Object getData(Persona persona) {
		throw new NotImplementedException();
	}

}
