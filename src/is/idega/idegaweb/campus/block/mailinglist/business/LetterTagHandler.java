/*
 * Created on Jul 28, 2003
 *
 */
package is.idega.idegaweb.campus.block.mailinglist.business;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * LetterTagHandler to parse letters with tags
 * @author aron 
 * @version 1.0
 */
public class LetterTagHandler extends DefaultHandler {
	
	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
	}

}
