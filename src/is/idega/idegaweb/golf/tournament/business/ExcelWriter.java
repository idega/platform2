/*
 * Created on 9.6.2004
 *
 * Copyright (C) 2004 Idega hf. All Rights Reserved.
 *
 *  This software is the proprietary information of Idega hf.
 *  Use is subject to license terms.
 */
package is.idega.idegaweb.golf.tournament.business;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.io.MediaWritable;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.presentation.IWContext;

/**
 * @author aron
 *
 * ExcelWriter TODO Describe this type
 */
public abstract class ExcelWriter implements MediaWritable {
	
	protected String tournamentRoundID = null;
	protected MemoryFileBuffer buffer = null;
	protected IWResourceBundle iwrb = null;

	/* (non-Javadoc)
	 * @see com.idega.io.MediaWritable#getMimeType()
	 */
	/* (non-Javadoc)
	 * @see com.idega.io.MediaWritable#getMimeType()
	 */
	public String getMimeType() {
		return "application/x-msexcel";
	}
	
	protected String localize(String key,String defaultString){
		return iwrb.getLocalizedString(key,defaultString);
	}

	/* (non-Javadoc)
	 * @see com.idega.io.MediaWritable#writeTo(java.io.OutputStream)
	 */
	public void writeTo(OutputStream out) throws IOException {
		if (buffer != null) {
			MemoryInputStream mis = new MemoryInputStream(buffer);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// Read the entire contents of the file.
			while (mis.available() > 0) {
				baos.write(mis.read());
			}
			baos.writeTo(out);
		}
		else
			System.err.println("buffer is null");

	}
	
	/* (non-Javadoc)
	 * @see com.idega.io.MediaWritable#init(javax.servlet.http.HttpServletRequest, com.idega.presentation.IWContext)
	 */
	public void init(HttpServletRequest req, IWContext iwc) {
		iwrb = iwc.getIWMainApplication().getBundle("is.idega.idegaweb.golf").getResourceBundle(iwc);
		buffer = new MemoryFileBuffer();
		MemoryOutputStream ous = new MemoryOutputStream(buffer);
		OutputStreamWriter out = new OutputStreamWriter(ous);
		writeFileContent(iwc,out);
	}
	
	public abstract void writeFileContent(IWContext iwc,Writer out);

}
