/*
 * Created on May 31, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package is.idega.experimental.pdftest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.idega.io.FileSaver;
import com.idega.block.pdf.ITextXMLHandler;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.util.FileUtil;
import com.lowagie.text.ElementTags;
import com.lowagie.text.xml.XmlPeer;
/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author aron 
 * @version 1.0
 */
public class PDFHandlerTest {
	public static void main(String[] args) {
		try {
			PDFHandlerTest test = new PDFHandlerTest();
			if (args!=null && args.length >0) {
				if(args[0].equalsIgnoreCase("xml"))
					System.out.println(test.getXMLTemplate());
				else
					test.doTheShit(new FileInputStream(args[0]));
			}
			else {
				test.doTheShit(new StringBufferInputStream(test.getXMLTemplate()));
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void doTheShit(InputStream xmlURI) throws IOException {
		ITextXMLHandler handler = new ITextXMLHandler(ITextXMLHandler.PDF + ITextXMLHandler.HTML + ITextXMLHandler.TXT);
		HashMap tagmap = new HashMap();
		XmlPeer peer = new XmlPeer(ElementTags.ITEXT, "template");
		tagmap.put(peer.getAlias(), peer);
		peer = new XmlPeer(ElementTags.CHUNK, "name");
		peer.setContent("<dummy /> ");
		tagmap.put(peer.getAlias(), peer);
		peer = new XmlPeer(ElementTags.CHUNK, "name1");
		peer.setContent("Beavis ");
		tagmap.put(peer.getAlias(), peer);
		peer = new XmlPeer(ElementTags.CHUNK, "name2");
		peer.setContent("Butthead ");
		tagmap.put(peer.getAlias(), peer);
		Collection buffers = handler.writeToBuffers(tagmap, xmlURI);
		if (buffers != null && !buffers.isEmpty()) {
			System.out.println("Buffer count : " + buffers.size());
			Iterator iter = buffers.iterator();
			if (iter.hasNext())
				writeToFile((MemoryFileBuffer) iter.next(), "temp.pdf");
			if (iter.hasNext())
				writeToFile((MemoryFileBuffer) iter.next(), "temp.txt");
			if (iter.hasNext())
				writeToFile((MemoryFileBuffer) iter.next(), "temp.html");
		}
	}
	public void writeToFile(MemoryFileBuffer buffer, String fileName) throws IOException {
		System.out.println("Buffer " + fileName + " length: " + buffer.length());
		MemoryInputStream in = new MemoryInputStream(buffer);
		//FileUtil.createFile("/home/aron/javatest/"+fileName);
		FileOutputStream out = new FileOutputStream(new File("/home/aron/javatest/" + fileName));
		int c;
		while ((c = in.read()) != -1)
			out.write(c);
		in.close();
		out.close();
	}
	public String getXMLTemplate() {
		StringBuffer xml = new StringBuffer();
		String newline = "\n";
		xml.append("<template> ").append(newline);
		xml.append("<name/> <newline/>").append(newline);;
		xml.append("<name1/>: How do you do my friend <newline />").append(newline);;
		xml.append("<name2/>: Im fine, and you ? <newline />").append(newline);;
		xml.append("<name1/>: Yeah I'm hanging inthere ! <newline />").append(newline);;
		xml.append("<paragraph leading=\"18.0\" font=\"unknown\" align=\"Default\">").append(newline);;
		xml.append("<name2/>: This is funky shit funking down town").append(newline);;
		xml.append("</paragraph>").append(newline);;
		xml.append("<chunk font=\"Helvetica\" size=\"18.0\" fontstyle=\"normal\" red=\"0\" green=\"64\" blue=\"64\">").append(newline);;
		xml.append("<name1/>: Heavy chunk of funk").append(newline);;
		xml.append("</chunk>").append(newline);;
		xml.append("</template>").append(newline);;
		return xml.toString();
	}
}
