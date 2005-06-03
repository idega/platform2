/*
 * $Id: GroupCollectionHandler.java,v 1.1 2005/06/03 06:51:18 laddi Exp $
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
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.adulteducation.AdultEducationConstants;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourse;
import com.idega.block.school.data.SchoolClass;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.remotescripting.RemoteScriptCollection;
import com.idega.presentation.remotescripting.RemoteScriptHandler;
import com.idega.presentation.remotescripting.RemoteScriptingResults;


/**
 * Last modified: $Date: 2005/06/03 06:51:18 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class GroupCollectionHandler implements RemoteScriptCollection {

	/* (non-Javadoc)
	 * @see com.idega.presentation.remotescripting.RemoteScriptCollection#getResults(com.idega.presentation.IWContext)
	 */
	public RemoteScriptingResults getResults(IWContext iwc) {
		String sourceName = iwc.getParameter(RemoteScriptHandler.PARAMETER_SOURCE_PARAMETER_NAME);

		String sourceID = iwc.getParameter(sourceName);

	  return handleCourseUpdate(iwc, sourceName, sourceID);
	}
	
	private RemoteScriptingResults handleCourseUpdate(IWContext iwc, String sourceName, String sourceID) {
		AdultEducationBusiness biz = getBusiness(iwc);
		IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(AdultEducationConstants.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);
		
    Collection ids = new ArrayList();
		ids.add("");
    Collection names = new ArrayList();
		names.add(iwrb.getLocalizedString("select_group","Select group"));
    
		try {
			AdultEducationCourse course = biz.getCourse(sourceID);
			Collection availableGroups = biz.getGroups(getSession(iwc).getSchool(), getSession(iwc).getSchoolSeason(), course.getCode());
			if (availableGroups.isEmpty()) {
				SchoolClass group = biz.createDefaultGroup(getSession(iwc).getSchoolSeason(), course);
				if (group != null) {
					availableGroups.add(group);
				}
			}
			Iterator iter = availableGroups.iterator();
			while (iter.hasNext()) {
				SchoolClass group = (SchoolClass) iter.next();
				ids.add(group.getPrimaryKey().toString());
				names.add(group.getName());
			}
		}
		catch (RemoteException re) {
			re.printStackTrace();
		}
		catch (FinderException fe) {
			fe.printStackTrace();
		}
		
    RemoteScriptingResults rsr = new RemoteScriptingResults(RemoteScriptHandler.getLayerName(sourceName, "id"), ids);
    rsr.addLayer(RemoteScriptHandler.getLayerName(sourceName, "name"), names);

    return rsr;
	}
	
	private AdultEducationSession getSession(IWUserContext iwuc) {
		try {
			return (AdultEducationSession) IBOLookup.getSessionInstance(iwuc, AdultEducationSession.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
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