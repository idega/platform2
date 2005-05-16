/*
 * $Id: PDFOverviewCreator.java,v 1.1 2005/05/16 08:57:06 laddi Exp $
 * Created on May 15, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.business;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import javax.servlet.http.HttpServletRequest;
import se.idega.idegaweb.commune.adulteducation.presentation.AdultEducationBlock;
import com.idega.block.pdf.business.PrintingContext;
import com.idega.block.pdf.business.PrintingService;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolSeason;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.io.MediaWritable;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.presentation.IWContext;


/**
 * Last modified: $Date: 2005/05/16 08:57:06 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class PDFOverviewCreator implements MediaWritable {

	private MemoryFileBuffer buffer = null;

	/* (non-Javadoc)
	 * @see com.idega.io.MediaWritable#getMimeType()
	 */
	public String getMimeType() {
		return "application/pdf";
	}

	/* (non-Javadoc)
	 * @see com.idega.io.MediaWritable#init(javax.servlet.http.HttpServletRequest, com.idega.presentation.IWContext)
	 */
	public void init(HttpServletRequest req, IWContext iwc) {
		try {
			SchoolSeason season = getSchoolBusiness(iwc).getSchoolSeason(iwc.getParameter(AdultEducationBlock.PARAMETER_SCHOOL_SEASON));
			buffer = new MemoryFileBuffer();
		  OutputStream mos = new MemoryOutputStream(buffer);
		 
		  PrintingContext pcx = new ChoiceOverviewContext(iwc, season, iwc.getCurrentUser(), iwc.getCurrentLocale());
		  pcx.setDocumentStream(mos);
		  getPrintingService(iwc).printDocument(pcx);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	/* (non-Javadoc)
	 * @see com.idega.io.MediaWritable#writeTo(java.io.OutputStream)
	 */
	public void writeTo(OutputStream out) throws IOException {
		if (buffer != null) {
			MemoryInputStream mis = new MemoryInputStream(buffer);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while (mis.available() > 0) {
				baos.write(mis.read());
			}
			baos.writeTo(out);
		}
		else {
			System.err.println("buffer is null");
		}
	}

  private PrintingService getPrintingService(IWApplicationContext iwac) {
    try {
    		return (PrintingService) IBOLookup.getServiceInstance(iwac, PrintingService.class);
    }
    catch (IBOLookupException ile) {
    		throw new IBORuntimeException(ile);
    }
  }

  private SchoolBusiness getSchoolBusiness(IWApplicationContext iwac) {
    try {
    		return (SchoolBusiness) IBOLookup.getServiceInstance(iwac, SchoolBusiness.class);
    }
    catch (IBOLookupException ile) {
    		throw new IBORuntimeException(ile);
    }
  }
}