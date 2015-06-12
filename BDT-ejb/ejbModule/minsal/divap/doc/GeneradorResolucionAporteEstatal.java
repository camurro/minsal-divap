package minsal.divap.doc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.Text;

public class GeneradorResolucionAporteEstatal extends GeneradorWord {

	private String templateFilename;
	private boolean replaceInTable;

	public GeneradorResolucionAporteEstatal(String fileName, String templateFilename) {
		super(fileName);
		this.templateFilename = templateFilename;
		this.replaceInTable = false;
	}

	public String getTemplateFilename() {
		return templateFilename;
	}

	public void setTemplateFilename(String templateFilename) {
		this.templateFilename = templateFilename;
	}

	public  <T> void replaceValues(Map<String, Object> parameters, Class<T> clazz) throws IOException, InvalidFormatException, Docx4JException {
		if("XWPFDocument".equals(clazz.getSimpleName())){
			saveDocument(replaceValuesDocx(parameters));
			//saveDocument(replaceValuesDocxDoc4j(parameters));
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

	private WordprocessingMLPackage replaceValuesDocxDoc4j(Map<String, Object> parameters) throws Docx4JException{
		File template = new File(this.templateFilename);
		WordprocessingMLPackage wpml = WordprocessingMLPackage.load(template);
		MainDocumentPart mdp = wpml.getMainDocumentPart();
		List<Object> content = mdp.getContent();
		for (Object object : content) {
			Text textElement = (Text) object;
			String textToReplace = textElement.getValue();
			for (String key : parameters.keySet()) {
				System.out.println("key->" + key +" text=" + textToReplace); 
				if(textToReplace.contains(key)){
					textElement.setValue(parameters.get(key).toString());
				}
			}
		}
		return wpml;
	}

	private XWPFDocument replaceValuesDocx(Map<String, Object> parameters) throws FileNotFoundException, IOException, InvalidFormatException {
		/*File template = new File(this.templateFilename);
		XWPFDocument document = new XWPFDocument(new FileInputStream(template));
		XWPFWordExtractor extractor = new XWPFWordExtractor(document);
		if(extractor.getText() != null && parameters != null && !parameters.isEmpty()){
			String contenido = extractor.getText();   
			for (String key : parameters.keySet()) {
				if(contenido.contains(key)){
					String replacementText = parameters.get(key).toString();
					System.out.println("key->"+key+" replacementText->"+replacementText);
					contenido = contenido.replaceAll(key, replacementText);
					System.out.println("contenido-->"+contenido);
				}
			}
		}*/


		File template = new File(this.templateFilename);
		XWPFDocument document = new XWPFDocument(new FileInputStream(template));

		for (XWPFParagraph p : document.getParagraphs()){
			List<XWPFRun> runs = p.getRuns();
			int size = ((runs == null)?0:runs.size());
			int iteracion = 0;
			StringBuilder stringBuilder = new StringBuilder();
			for (XWPFRun run : runs){
				if(iteracion == (size-1)){
					stringBuilder.append(run.toString());
					String text = stringBuilder.toString();
					for (Map.Entry<String, Object> entry : parameters.entrySet()) {
					    String key = entry.getKey();
					    Object value = entry.getValue();
					    key = key.replace("{", "\\{").replace("}", "\\}");
					    text = text.replaceAll(key, value.toString());
					}
					run.setText(text, 0);
				}else{
					stringBuilder.append(run.toString());
					run.setText("", 0);
				}
				iteracion++;
			}
		}
		
		if(replaceInTable){
			List<XWPFTable> tables = document.getTables();
			if(tables != null && tables.size() > 0){
				for(int index=0; index < tables.size(); index++){
				   XWPFTable table = document.getTables().get(index);
				   for (XWPFTableRow row : table.getRows()) {
					      for (XWPFTableCell cell : row.getTableCells()) {
					         for (XWPFParagraph p : cell.getParagraphs()) {
					            for (XWPFRun r : p.getRuns()) {
					              String text = r.getText(0);
					              if (text != null) {
						              System.out.println("table text="+text);
						              for (Map.Entry<String, Object> entry : parameters.entrySet()) {
										    String key = entry.getKey();
										    Object value = entry.getValue();
										    key = key.replace("{", "\\{").replace("}", "\\}");
										    text = text.replaceAll(key, value.toString());
						              }
						              r.setText(text, 0);
					              }
					            }
					         }
					      }
					   }
				   
				   
				   
				   /*final XWPFTableCell cell = table.getRow(0).getCell(0);
				   System.out.println("Current cell value: " + cell.getText());
				   table.removeRow(0);
				   final XWPFTableRow row = table.createRow();
				   final XWPFTableCell nuevaCelda = row.createCell();
				   final String[] lineas = newValue.split("\n");
				   for(String s : lineas){
					   final XWPFParagraph parrafo =  nuevaCelda.addParagraph();
					   parrafo.createRun().setText(s);
				   }*/
				
				}
			}
		}


		//String replacementText = null;
		//for (XWPFParagraph p : document.getParagraphs()) {
			/*StringBuilder stringBuilder = new StringBuilder();
			int numRuns = 0;
			boolean marcaAbierta= false;
			for (XWPFRun run : p.getRuns()){
				int pos = run.getTextPosition();
				if(run.getText(pos) != null) {
					stringBuilder.append(run.getText(pos));
				}
				if( stringBuilder.toString().contains("{") || marcaAbierta){
					if(!marcaAbierta){
						numRuns=1;
						marcaAbierta = true;
					}
					if( stringBuilder.toString().contains("}")){
						//reemplazo inmediatamente
						String key = stringBuilder.substring(stringBuilder.indexOf("{") , stringBuilder.indexOf("}")+1);
						System.out.println("key buscada-->"+key);
						replacementText = ((parameters.get(key) != null) ? parameters.get(key).toString() : null);
						if(replacementText != null){
							key = key.replace("{", "\\{").replace("}", "\\}");
							System.out.println("key escapada -->"+key);
							String text = stringBuilder.toString().replaceAll(key, replacementText);
							run.setText(text, 0);
						}
						stringBuilder = new StringBuilder();
						marcaAbierta = false;
						numRuns=0;
					}else{
						if( numRuns == 1){
							String text = stringBuilder.toString().substring(0, stringBuilder.indexOf("{"));
							run.setText(text, 0);
							marcaAbierta = true;
						}else{
							run.setText("", 0);
						}
						numRuns++;
					}
				}else{
					stringBuilder = new StringBuilder();
					numRuns = 0;
					marcaAbierta= false;
				}
			}*/

			/*for (int j = 0; j <  p.getCTP().getRArray().length; j++) {
				CTR run =  p.getCTP().getRArray()[j];

				for (int k = 0; k < run.getTArray().length; k++) {
					CTText text = run.getTArray()[k];
					String modified = text.getStringValue();
					for (String key : parameters.keySet()) {
						System.out.println("key->"+key+" text="+text); 
						if(modified.contains(key)){
							replacementText = parameters.get(key).toString();
							modified = text.getStringValue().replaceAll(key, replacementText);
						}
					}
					// This will output the text contents
					System.out.println("CTText -->" + modified);
					// And this will set its contents
					text.setStringValue(modified);
				}
			}*/

			/*for (XWPFRun run : p.getRuns()) {
				if (run.getText(run.getTextPosition()) == null)
					continue;
				String text = run.getText(run.getTextPosition());
				if(text != null && parameters != null && !parameters.isEmpty()){
					for (String key : parameters.keySet()) {
						System.out.println("key->"+key+" text="+text); 
						if(text.contains(key)){
							replacementText = parameters.get(key).toString();
							text = text.replaceAll(key, replacementText);
							run.setText(text, 0);
						}
					}
				}
			}*/
		//}

		/*for (XWPFParagraph p : document.getParagraphs()) {
			System.out.println("Inicio parrafo");
			int numberOfRuns = p.getRuns().size();
			StringBuilder stringBuilder = new StringBuilder();
			for (XWPFRun r : p.getRuns()){
				int pos = r.getTextPosition();
				if(r.getText(pos) != null) {
					stringBuilder.append(r.getText(pos));
				}
			}

			if(stringBuilder.length() > 0 && parameters != null && !parameters.isEmpty()) {
				// Remove all existing runs
				for(int i = 0; i < numberOfRuns; i++) {
					p.removeRun(i);
				}

				for (String key : parameters.keySet()) {
					System.out.println("key->" + key +" text=" + stringBuilder.toString()); 
					if(stringBuilder.toString().contains(key)){
						String replacementText = parameters.get(key).toString();
						String tmpReplace = stringBuilder.toString().replaceAll(key, replacementText);
						// Add new run with updated text
						stringBuilder = new StringBuilder(tmpReplace);
					}
				}
				XWPFRun run = p.createRun();
				run.setText(stringBuilder.toString());
				p.addRun(run);
			}

			System.out.println("Fin parrafo");
		}*/
		return document;
	}

	public boolean isReplaceInTable() {
		return replaceInTable;
	}

	public void setReplaceInTable(boolean replaceInTable) {
		this.replaceInTable = replaceInTable;
	}

}
