package minsal.divap.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class GeneradorXML{
	private String fileName;

	public GeneradorXML(String fileName){
		this.fileName = fileName;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T createObject(Class<T> genericType, String content) throws JAXBException {  
		JAXBContext context = JAXBContext.newInstance(genericType);  
		StringReader reader = new StringReader(content);  
		Unmarshaller unmarshaller = context.createUnmarshaller();  
		T commandResponse = (T) unmarshaller.unmarshal(reader);  
		return commandResponse;  
	}
	
	@SuppressWarnings("unchecked")
	public <T> T createObject(Class<T> genericType, byte [] content) throws JAXBException {  
		JAXBContext context = JAXBContext.newInstance(genericType);  
		ByteArrayInputStream input = new ByteArrayInputStream (content); 
		Unmarshaller unmarshaller = context.createUnmarshaller();  
		T commandResponse = (T) unmarshaller.unmarshal(input);  
		return commandResponse;  
	}

	public void saveContent(byte [] content){
		System.out.println("Inicio GeneradorXML");
		File file = new File(fileName); 
		FileOutputStream os = null; 
		try { 
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdir();
			}
			os = new FileOutputStream(file);
			os.write(content);
		} catch (IOException e) { 
			e.printStackTrace();
		} catch (Exception e) { 
			e.printStackTrace();
		} finally { 
			try { 
				if (null != os) 
					os.close(); 
			} catch (Exception ex) { 
				ex.printStackTrace();
			} 
		} 
		System.out.println("Fin GeneradorXML");
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
