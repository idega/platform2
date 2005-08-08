/*
 * $Id: PackageCollectionHandler.java,v 1.1 2005/08/08 22:21:37 laddi Exp $
 * Created on Jul 31, 2005
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
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoice;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourse;
import se.idega.idegaweb.commune.adulteducation.data.CoursePackage;
import se.idega.idegaweb.commune.adulteducation.data.SchoolCoursePackage;
import se.idega.idegaweb.commune.adulteducation.presentation.AdultEducationBlock;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolSeason;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.remotescripting.RemoteScriptCollection;
import com.idega.presentation.remotescripting.RemoteScriptHandler;
import com.idega.presentation.remotescripting.RemoteScriptingResults;


/**
 * Last modified: $Date: 2005/08/08 22:21:37 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class PackageCollectionHandler implements RemoteScriptCollection {

	/* (non-Javadoc)
	 * @see com.idega.presentation.remotescripting.RemoteScriptCollection#getResults(com.idega.presentation.IWContext)
	 */
	public RemoteScriptingResults getResults(IWContext iwc) {
		try {
			String sourceName = iwc.getParameter(RemoteScriptHandler.PARAMETER_SOURCE_PARAMETER_NAME);
			String sourceID = iwc.getParameter(sourceName);

		  return handlePackageUpdate(iwc, sourceName, sourceID);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	private RemoteScriptingResults handlePackageUpdate(IWContext iwc, String sourceName, String sourceID) throws RemoteException {
		AdultEducationBusiness biz = getBusiness(iwc);
		IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(AdultEducationConstants.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);
		
    Collection ids = new ArrayList();
		ids.add("-1");
    Collection names = new ArrayList();
		names.add(iwrb.getLocalizedString("select_package","Select package"));
    
		Object seasonPK = iwc.getParameter(AdultEducationBlock.PARAMETER_SCHOOL_SEASON);
		Object packagePK = iwc.getParameter(AdultEducationBlock.PARAMETER_COURSE_PACKAGE + "_1");
		CoursePackage firstPackage = null;
		if (packagePK != null) {
			try {
				SchoolCoursePackage schoolPackage = getBusiness(iwc).getSchoolCoursePackage(packagePK);
				firstPackage = schoolPackage.getPackage();
			}
			catch (FinderException fe) {
				fe.printStackTrace();
			}
		}
		SchoolSeason season = getBusiness(iwc).getSchoolBusiness().getSchoolSeason(seasonPK);
		School school = getBusiness(iwc).getSchoolBusiness().getSchool(sourceID);
		Collection choices = biz.getChoices(iwc.getCurrentUser(), season);
		
		try {
			Collection coursePackages = biz.getCoursePackages(school, season);
	    Iterator iter = coursePackages.iterator();
	    while (iter.hasNext()) {
	    		SchoolCoursePackage element = (SchoolCoursePackage) iter.next();
	    		Collection courses = null;
	    		try {
	    			courses = element.getCourses();
	    		}
	    		catch (IDORelationshipException ire) {
	    			ire.printStackTrace();
	    			courses = new ArrayList();
	    		}
	    		CoursePackage coursePackage = element.getPackage();
	    		boolean add = element.isActive();
	    		
	    		if (firstPackage != null) {
	    			add = firstPackage.getPrimaryKey().equals(coursePackage.getPrimaryKey());
	    		}
	    		
	    		if (!choices.isEmpty()) {
	    			Iterator iterator = choices.iterator();
						while (iterator.hasNext()) {
							AdultEducationChoice choice = (AdultEducationChoice) iterator.next();
							AdultEducationCourse course = choice.getCourse();
							if (courses.contains(course)) {
								add = false;
								break;
							}
						}
	    		}
	    		
				if (add) {
		    		ids.add(coursePackage.getPrimaryKey().toString());
		    		names.add(coursePackage.getName() + element.getFreeText() != null ? " - " + element.getFreeText() : "");
				}
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