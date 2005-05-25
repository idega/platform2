/*
 * $Id: AdultEducationBlock.java,v 1.5 2005/05/25 13:06:38 laddi Exp $
 * Created on 27.4.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.presentation;

import se.idega.idegaweb.commune.adulteducation.AdultEducationConstants;
import se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusiness;
import se.idega.idegaweb.commune.adulteducation.business.AdultEducationSession;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Window;

/**
 * Last modified: $Date: 2005/05/25 13:06:38 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.5 $
 */
public abstract class AdultEducationBlock extends CommuneBlock {

	public static final String PARAMETER_CHOICE = "co_choice";
	public static final String PARAMETER_SCHOOL_SEASON = "ce_school_season";
	public static final String PARAMETER_STUDY_PATH = "ce_study_path";

	private static final String PROPERTY_RED_COLOR = "red_color";
	
	protected static String RED_COLOR = "#FFE0E0";

	private AdultEducationBusiness business;
	private AdultEducationSession session;
	
	public void main(IWContext iwc) throws Exception {
		setBundle(getBundle(iwc));
		setResourceBundle(getResourceBundle(iwc));
		business = getBusiness(iwc);
		session = getSession(iwc);
		
		RED_COLOR = getBundle(iwc).getProperty(PROPERTY_RED_COLOR, "#FFE0E0");
		
		present(iwc);
	}
	
	protected AdultEducationBusiness getBusiness() {
		return business;
	}
	
	private AdultEducationBusiness getBusiness(IWApplicationContext iwac) {
		try {
			return (AdultEducationBusiness) IBOLookup.getServiceInstance(iwac, AdultEducationBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	protected AdultEducationSession getSession() {
		return session;
	}
	
	protected AdultEducationSession getSession(IWUserContext iwuc) {
		try {
			return (AdultEducationSession) IBOLookup.getSessionInstance(iwuc, AdultEducationSession.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	protected Window getFileWindow() {
		Window w = new Window(localize("pdf", "PDF"), getIWApplicationContext().getIWMainApplication().getMediaServletURI());
		w.setResizable(true);
		w.setMenubar(true);
		w.setHeight(400);
		w.setWidth(500);
		return w;
	}
	
	public String getBundleIdentifier() {
		return AdultEducationConstants.IW_BUNDLE_IDENTIFIER;
	}
	
	public abstract void present(IWContext iwc);
}