/*
 * Created on Jan 8, 2004
 *
 */
package se.idega.idegaweb.commune.message.business;

import se.idega.idegaweb.commune.printing.business.ContentCreationException;
import se.idega.idegaweb.commune.printing.business.DocumentPrintContext;

/**
 * PrintMessageHandler
 * @author aron 
 * @version 1.0
 */
public interface MessagePdfHandler {
	
	public void createMessageContent(DocumentPrintContext dpc) throws ContentCreationException;
}
