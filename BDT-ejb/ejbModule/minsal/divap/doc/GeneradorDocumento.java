package minsal.divap.doc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class GeneradorDocumento{
	private String fileName;

	public GeneradorDocumento(String fileName){
		this.fileName = fileName;
	}

	public void saveContent(byte [] content) {
		System.out.println("GeneradorDocumento");
		File file = new File(fileName); 
		if(content == null){
			return;
		}
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
		System.out.println("Fin saveDocument XWPFDocument");
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
