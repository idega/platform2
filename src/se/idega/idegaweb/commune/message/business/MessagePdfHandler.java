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
public interface MessagePdfHandler{
	
	/**
	 * Creates the content of the open document referenced by the context
	 * @param dpc
	 * @throws ContentCreationException
	 */
	public void createMessageContent(DocumentPrintContext dpc) throws ContentCreationException;
	/**
	 * Returns a unique code name identifying this handler
	 * @return
	 */
	public String getHandlerCode();
	
}
