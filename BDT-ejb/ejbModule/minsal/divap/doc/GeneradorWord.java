package minsal.divap.doc;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

public class GeneradorWord{
	private String fileName;

	public GeneradorWord(String fileName){
		this.fileName = fileName;
	}

	public <T> void saveContent(byte [] content, Class<T> clazz) throws IOException {
		System.out.println("GeneradorWord fromContent clazz.getName()-->"+clazz.getSimpleName());
		if("XWPFDocument".equals(clazz.getSimpleName())){
			saveDocument(createDocx(content));
		}else{
			saveDocument(createDoc(content));
		}
	}

	private HWPFDocument createDoc(byte [] content) throws IOException{
		InputStream inputStream = new ByteArrayInputStream(content);
		HWPFDocument document = new HWPFDocument (inputStream);
		return document;
	}

	private XWPFDocument createDocx(byte [] content) throws IOException {
		InputStream inputStream = new ByteArrayInputStream(content);
		XWPFDocument document = new XWPFDocument(inputStream);
		return document;
	}

	protected void saveDocument(XWPFDocument document){
		System.out.println("Inicio saveDocument XWPFDocument");
		File file = new File(fileName); 
		FileOutputStream os = null; 
		try { 
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdir();
			}
			os = new FileOutputStream(file);
			document.write(os);
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

	protected void saveDocument(HWPFDocument document){
		System.out.println("Inicio saveDocument HWPFDocument");
		File file = new File(fileName); 
		FileOutputStream os = null; 
		try { 
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdir();
			}
			os = new FileOutputStream(file);
			document.write(os);
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
		System.out.println("Fin saveDocument HWPFDocument");
	}

	protected void saveDocument(WordprocessingMLPackage wpml){
		System.out.println("Inicio saveDocument WordprocessingMLPackage");
		File file = new File(fileName); 
		try {
			wpml.save(file);
			System.out.println("Fin ok saveDocument HWPFDocument");
		} catch (Docx4JException e) {
			System.out.println("Fin Error saveDocument HWPFDocument");
			e.printStackTrace();
		}
		
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
