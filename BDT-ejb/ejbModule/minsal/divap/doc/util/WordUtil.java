package minsal.divap.doc.util;

import java.io.ByteArrayInputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.util.Iterator;  
import java.util.List;  
import java.util.Map;  
import java.util.Map.Entry;  

import minsal.divap.doc.CustomXWPFDocument;

import org.apache.poi.POIXMLDocument;  
import org.apache.poi.openxml4j.opc.OPCPackage;  
import org.apache.poi.xwpf.usermodel.XWPFParagraph;  
import org.apache.poi.xwpf.usermodel.XWPFRun;  
import org.apache.poi.xwpf.usermodel.XWPFTable;  
import org.apache.poi.xwpf.usermodel.XWPFTableCell;  
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

public class WordUtil {
	public static CustomXWPFDocument generateWord(Map<String, Object> param, String template) {  
		CustomXWPFDocument doc = null;  
		try {  
			OPCPackage pack = POIXMLDocument.openPackage(template);  
			doc = new CustomXWPFDocument(pack);  
			if (param != null && param.size() > 0) {  

				//Manejo párrafo  
				List<XWPFParagraph> paragraphList = doc.getParagraphs();  
				processParagraphs(paragraphList, param, doc);  

				//Manejo tabla 
				Iterator<XWPFTable> it = doc.getTablesIterator();  
				while (it.hasNext()) {  
					XWPFTable table = it.next();  
					List<XWPFTableRow> rows = table.getRows();  
					for (XWPFTableRow row : rows) {  
						List<XWPFTableCell> cells = row.getTableCells();  
						for (XWPFTableCell cell : cells) {  
							List<XWPFParagraph> paragraphListTable =  cell.getParagraphs();  
							processParagraphs(paragraphListTable, param, doc);  
						}  
					}  
				}  
			}  
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
		return doc;  
	}  
	/** 
	 * Manejo párrafo
	 * @param paragraphList 
	 */  
	public static void processParagraphs(List<XWPFParagraph> paragraphList,Map<String, Object> param,CustomXWPFDocument doc){  
		if(paragraphList != null && paragraphList.size() > 0){  
			for(XWPFParagraph paragraph:paragraphList){  
				List<XWPFRun> runs = paragraph.getRuns();  
				for (XWPFRun run : runs) {  
					String text = run.getText(0);  
					if(text != null){  
						boolean isSetText = false;  
						for (Entry<String, Object> entry : param.entrySet()) {  
							String key = entry.getKey();  
							if(text.indexOf(key) != -1){  
								isSetText = true;  
								Object value = entry.getValue();  
								if (value instanceof String) {//La sustitución de texto
									text = text.replace(key, value.toString());  
								} else if (value instanceof Map) {//reemplazo de imagen  
									text = text.replace(key, "");  
									Map pic = (Map)value;  
									int width = Integer.parseInt(pic.get("width").toString());  
									int height = Integer.parseInt(pic.get("height").toString());  
									int picType = getPictureType(pic.get("type").toString());  
									byte[] byteArray = (byte[]) pic.get("content");  
									ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteArray);  
									try {  
										//int ind = doc.addPicture(byteInputStream, picType);  
										doc.addPictureData(byteInputStream, picType);
										int ind = doc.getAllPictures().size()-1;
										doc.createPicture(ind, width , height, paragraph);  
									} catch (Exception e) {  
										e.printStackTrace();  
									}  
								}  
							}  
						}  
						if(isSetText){  
							run.setText(text,0);  
						}  
					}  
				}  
			}  
		}  
	}  
	/** 
	 * De acuerdo al tipo de imagen, obtener el correspondiente código 
	 * @param picType 
	 * @return int 
	 */  
	private static int getPictureType(String picType){  
		int res = CustomXWPFDocument.PICTURE_TYPE_PICT;  
		if(picType != null){  
			if(picType.equalsIgnoreCase("png")){  
				res = CustomXWPFDocument.PICTURE_TYPE_PNG;  
			}else if(picType.equalsIgnoreCase("dib")){  
				res = CustomXWPFDocument.PICTURE_TYPE_DIB;  
			}else if(picType.equalsIgnoreCase("emf")){  
				res = CustomXWPFDocument.PICTURE_TYPE_EMF;  
			}else if(picType.equalsIgnoreCase("jpg") || picType.equalsIgnoreCase("jpeg")){  
				res = CustomXWPFDocument.PICTURE_TYPE_JPEG;  
			}else if(picType.equalsIgnoreCase("wmf")){  
				res = CustomXWPFDocument.PICTURE_TYPE_WMF;  
			}  
		}  
		return res;  
	}  
	/** 
	 * El flujo de entrada de datos en una matriz de bytes 
	 * @param in 
	 * @return 
	 */  
	public static byte[] inputStream2ByteArray(InputStream in,boolean isClose){  
		byte[] byteArray = null;  
		try {  
			int total = in.available();  
			byteArray = new byte[total];  
			in.read(byteArray);  
		} catch (IOException e) {  
			e.printStackTrace();  
		}finally{  
			if(isClose){  
				try {  
					in.close();  
				} catch (Exception e2) {  
					System.out.println("Cierre flujo falla");  
				}  
			}  
		}  
		return byteArray;  
	}  
}  