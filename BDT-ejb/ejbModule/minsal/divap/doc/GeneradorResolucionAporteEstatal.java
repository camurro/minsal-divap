package minsal.divap.doc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class GeneradorResolucionAporteEstatal extends GeneradorWord {

	private String templateFilename;

	public GeneradorResolucionAporteEstatal(String fileName, String templateFilename) {
		super(fileName);
		this.templateFilename = templateFilename;
	}

	public String getTemplateFilename() {
		return templateFilename;
	}

	public void setTemplateFilename(String templateFilename) {
		this.templateFilename = templateFilename;
	}

	public  <T> void replaceValues(Map<String, Object> parameters, Class<T> clazz) throws IOException, InvalidFormatException {
		if("XWPFDocument".equals(clazz.getSimpleName())){
			saveDocument(replaceValuesDocx(parameters));
		}else{
			//saveDocument(replaceValuesDoc(parameters, values));
			saveDocument(replaceValuesDocx(parameters));
		}
	}

	private HWPFDocument replaceValuesDoc(Map<String, Object> parameters) throws FileNotFoundException, IOException {
		File template = new File(this.templateFilename);
		HWPFDocument document = new HWPFDocument(new FileInputStream(template));

		//to replace the content in header and footnote
		/*HeaderStories hs = new HeaderStories(document); 

        Range hsRange = hs.getRange(); 
        hsRange.replaceText("#day#", (String) parameters.get("#day#")); 
        hsRange.replaceText("#mon#", parameters.get("#mon#")); 
        hsRange.replaceText("#yea#", parameters.get("#yea#")); 
        hsRange.replaceText("#n_1#", parameters.get("#n_1#")); 
        hsRange.replaceText("#n_2#", parameters.get("#n_2#")); 
        hsRange.replaceText("#serial#", parameters.get("#serial#"));*/ 

		Range range = document.getRange(); 
		if(parameters != null && !parameters.isEmpty()){
			for (String key : parameters.keySet()) {
				range.replaceText(key, (String)parameters.get(key)); 
			}

			//to replace the content in body
			/*range.replaceText("#n_v_left#", map.get("#n_v_left#")); 
			range.replaceText("#n_v_right#", map.get("#n_v_right#"));*/ 
		}
		TableIterator iterator = new TableIterator(range);
		int contTables = 0;
		while(iterator.hasNext()){
			Table table = iterator.next();
			break;
			/*for(int rowIndex = 0; rowIndex < table.numRows(); rowIndex++){
				if(rowIndex == 0){
					continue;
				}
				TableRow row = table.getRow(rowIndex);
				for(int colIndex = 0; colIndex < row.numCells(); colIndex++){
					TableCell cell = row.getCell(colIndex);
					System.out.println(cell.getParagraph(0).text());
				}
			}*/
		}
		return document;
	}

	private XWPFDocument replaceValuesDocx(Map<String, Object> parameters) throws FileNotFoundException, IOException, InvalidFormatException {
		File template = new File(this.templateFilename);
		XWPFDocument document = new XWPFDocument(new FileInputStream(template));
		String replacementText = null;
		for (XWPFParagraph p : document.getParagraphs()) {
			for (XWPFRun run : p.getRuns()) {
				String text = run.getText(0);
				//System.out.println("text--->"+text);
				if(text != null && parameters != null && !parameters.isEmpty()){
					for (String key : parameters.keySet()) {
						if(text.contains(key)){
							Object parameter = parameters.get(key);
							if(parameter != null){
								replacementText = parameter.toString() ;
								key = key.replace("{", "\\{").replace("}", "\\}");
								System.out.println("key--->"+key);
								text = text.replaceAll(key, replacementText);
								run.setText(text, 0);
							}
						}
					}
				}
			}
		}
		return document;
	}

}
