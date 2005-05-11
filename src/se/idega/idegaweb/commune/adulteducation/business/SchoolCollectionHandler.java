/*
 * $Id: SchoolCollectionHandler.java,v 1.2 2005/05/11 08:42:38 laddi Exp $
 * Created on May 10, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.business;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import se.idega.idegaweb.commune.adulteducation.AdultEducationConstants;
import se.idega.idegaweb.commune.adulteducation.presentation.AdultEducationBlock;
import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.remotescripting.RemoteScriptCollection;
import com.idega.presentation.remotescripting.RemoteScriptHandler;
import com.idega.presentation.remotescripting.RemoteScriptingResults;


/**
 * Last modified: $Date: 2005/05/11 08:42:38 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public class SchoolCollectionHandler implements RemoteScriptCollection {

	/* (non-Javadoc)
	 * @see com.idega.presentation.remotescripting.RemoteScriptCollection#getResults(com.idega.presentation.IWContext)
	 */
	public RemoteScriptingResults getResults(IWContext iwc) {
		String sourceName = iwc.getParameter(RemoteScriptHandler.PARAMETER_SOURCE_PARAMETER_NAME);

		String sourceID = iwc.getParameter(sourceName);

	  return handleSchoolUpdate(iwc, sourceName, sourceID);
	}
	
	private RemoteScriptingResults handleSchoolUpdate(IWContext iwc, String sourceName, String sourceID) {
		AdultEducationBusiness biz = getBusiness(iwc);
		IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(AdultEducationConstants.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);
		
    Collection ids = new ArrayList();
		ids.add("-1");
    Collection names = new ArrayList();
		names.add(iwrb.getLocalizedString("select_school","Select school"));
    
		Object seasonPK = iwc.getParameter(AdultEducationBlock.PARAMETER_SCHOOL_SEASON);
		
		try {
			Collection schools = biz.getAvailableSchools(sourceID, seasonPK);
	    Iterator iter = schools.iterator();
	    while (iter.hasNext()) {
	    		School school = (School) iter.next();
	    		ids.add(school.getPrimaryKey().toString());
	    		names.add(school.getName());
	    }
		}
		catch (RemoteException re) {
			re.printStackTrace();
		}
		
    RemoteScriptingResults rsr = new RemoteScriptingResults(RemoteScriptHandler.getLayerName(sourceName, "id"), ids);
    rsr.addLayer(RemoteScriptHandler.getLayerName(sourceName, "name"), names);

    return rsr;
	}
	
	private AdultEducationBusiness getBusiness(IWApplicationContext iwac) {
		try {
			return (AdultEducationBusiness) IBOLookup.getServiceInstance(iwac, AdultEducationBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
}