/*
 * Created on 21.5.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.block.pki.presentation;

import java.io.IOException;

import se.nexus.nbs.sdk.NBSMessageHttp;

import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;

/**
 * @author Roar
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class NBSSigningApplet extends PresentationObject {
	NBSMessageHttp _nbsMessageHttp = null;
	
	private final static String BIDT_JAR_PATH_PROPERTY = "bidt_jar_path";	

	//TODO: (Roar) the jar file should be stored in the bundle		
	private final static String DEFAULT_JAR_ARCHIVE =
		"/nacka/archive/cbt_bidt_2_3_11.jar";

	public NBSSigningApplet(NBSMessageHttp nbsMessageHttp) {
		_nbsMessageHttp = nbsMessageHttp;

	}
	public void print(IWContext iwc) throws IOException {
		System.out.println("NBSApplet.print()");

		String html = new String(_nbsMessageHttp.toHttpMessage().getBody());

		//removing <html>, <heading> etc.
		int start = html.indexOf("<APPLET");
		int end = html.indexOf("</script>");
		html = html.substring(start, end) + "</script>";

		//Changing the archive path
		start = html.indexOf("archive=");
		end = html.indexOf(".jar", start);
		html =
			html.substring(0, start)
				+ " archive=\""
				+ getJarFilepath(iwc)
				+ "\" "
				+ html.substring(end + 5);

		//Changing the form action	TODO: (Roar) read correct url from propertyfile(?)			
		start = html.indexOf("ACTION=");
		end = html.indexOf(">", start);
		html = html.substring(0, start) + " ACTION=\"\" " + html.substring(end);

		//Adding idegaweb session informatin to the form
		start = html.indexOf("</FORM>");
		html =
			html.substring(0, start)
				+ " <INPUT  NAME=ib_page VALUE=\""
				+ iwc.getParameter("ib_page")
				+ "\" TYPE=HIDDEN>\n"
				+ " <INPUT  NAME=idega_session_id VALUE=\""
				+ iwc.getParameter("idega_session_id")
				+ "\" TYPE=HIDDEN>\n"
				+ " <INPUT  NAME=iw_language VALUE=\""
				+ iwc.getParameter("iw_language")
				+ "\" TYPE=HIDDEN>\n"
				+ html.substring(start);

		print(html);
	}
	
	private String getJarFilepath(IWContext iwc){
		String path =  getBundle(iwc).getProperty(BIDT_JAR_PATH_PROPERTY);
		System.out.println("Jar file path: " + path);
		return path != null ? path : DEFAULT_JAR_ARCHIVE;		
		
	}
}
