package cl.redhat.bandejaTareas.util;

import org.codehaus.jackson.map.ObjectMapper;
import java.io.IOException;
import java.io.StringWriter;

public class JSONHelper {
	public static String toJSON(Object o) {
		StringWriter writer = new StringWriter();
		ObjectMapper mapper = new ObjectMapper();
		String result = null;
		try {
			mapper.writeValue(writer, o);
			writer.flush();
			result = writer.getBuffer().toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				writer.close();
			} catch (IOException localIOException1) {
				localIOException1.printStackTrace();
			}
		}
		return result;
	}

	public static <T> T fromJSON(String json, Class<T> clazz) {
		ObjectMapper mapper = new ObjectMapper();
		Object result = null;
		try	{
			result = mapper.readValue(json, clazz);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return (T) result;
	}
}
