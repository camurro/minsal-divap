package minsal.divap.model.mappers;

public interface Mapper <T>
{
	Object getSummary(T paramT);

	Object getBasic(T paramT);

	Object getData(T paramT);
}
