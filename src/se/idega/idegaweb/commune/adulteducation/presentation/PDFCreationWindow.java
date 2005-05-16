/*
 * $Id: PDFCreationWindow.java,v 1.1 2005/05/16 16:05:29 laddi Exp $
 * Created on May 16, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.presentation;

import se.idega.idegaweb.commune.adulteducation.AdultEducationConstants;
import se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusiness;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolSeason;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Window;
import com.idega.util.FileUtil;


/**
 * Last modified: $Date: 2005/05/16 16:05:29 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class PDFCreationWindow extends Window {

	public PDFCreationWindow() {
		this.setWidth(400);
		this.setHeight(350);
		this.setScrollbar(true);
		this.setResizable(true);	
		this.setAllMargins(0);
	}

	/**
	 * @see com.idega.presentation.PresentationObject#main(IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		SchoolSeason season = getSchoolBusiness(iwc).getSchoolSeason(iwc.getParameter(AdultEducationBlock.PARAMETER_SCHOOL_SEASON));
		String path = this.getBundle(iwc).getResourcesRealPath() + FileUtil.getFileSeparator() + "pdf";
		String fileName = "overview_" + iwc.getCurrentUser().getPrimaryKey().toString() + ".pdf";
		
		getBusiness(iwc).createOverviewPDF(iwc.getCurrentUser(), season, path, fileName, iwc.getCurrentLocale());
		iwc.forwardToURL(this, getBundle(iwc).getResourcesVirtualPath() + "/pdf/" + fileName);
	}

	public String getBundleIdentifier() {
		return AdultEducationConstants.IW_BUNDLE_IDENTIFIER;
	}
	
	private AdultEducationBusiness getBusiness(IWApplicationContext iwac) {
		try {
			return (AdultEducationBusiness) IBOLookup.getServiceInstance(iwac, AdultEducationBusiness.class);
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