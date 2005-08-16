/*
 * $Id: RunInputCollectionHandler.java,v 1.4 2005/08/16 14:09:36 laddi Exp $
 * Created on Feb 14, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.data.Year;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.remotescripting.RemoteScriptCollection;
import com.idega.presentation.remotescripting.RemoteScriptHandler;
import com.idega.presentation.remotescripting.RemoteScriptingResults;
import com.idega.user.data.Group;
import com.idega.util.IWTimestamp;


/**
 * 
 *  Last modified: $Date: 2005/08/16 14:09:36 $ by $Author: laddi $
 * 
 * @author <a href="mailto:birna@idega.com">birna</a>
 * @version $Revision: 1.4 $
 */
public class RunInputCollectionHandler extends PresentationObject implements RemoteScriptCollection {

	public String getBundleIdentifier() {
		return IWMarathonConstants.IW_BUNDLE_IDENTIFIER;
	}

	public RemoteScriptingResults getResults(IWContext iwc) {
		String sourceName = iwc.getParameter(RemoteScriptHandler.PARAMETER_SOURCE_PARAMETER_NAME);

		String sourceID = iwc.getParameter(sourceName);

	  return handleDistanceUpdate(iwc, sourceName, sourceID);
	}
	
	private RemoteScriptingResults handleDistanceUpdate(IWContext iwc, String sourceName, String runIdString) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		if (runIdString != null) {
			Integer runId = Integer.valueOf(runIdString);
		    RunBusiness runBiz = getRunBiz(iwc);
				IWTimestamp ts = IWTimestamp.RightNow();
		    Integer y = new Integer(ts.getYear());
		    String yearString = y.toString();
		    IWTimestamp stamp = new IWTimestamp();
		    stamp.addYears(1);
		    String nextYearString = String.valueOf(stamp.getYear());
		 
				try {
					Group run = (Group) runBiz.getRunGroupByGroupId(runId);

					String runnerYearString = yearString;
					boolean finished = false;
					Map yearMap = runBiz.getYearsMap(run);
					Year year = (Year) yearMap.get(yearString);
					if (year != null && year.getLastRegistrationDate() != null) {
						IWTimestamp runDate = new IWTimestamp(year.getLastRegistrationDate());
						if (ts.isLaterThanOrEquals(stamp)) {
							finished = true;
						}
					}
					Year nextYear = (Year) yearMap.get(nextYearString);
					if (finished && nextYear != null) {
						runnerYearString = nextYearString;
					}
					Collection distances = runBiz.getDistancesMap(run, runnerYearString);
		
			    Vector ids = new Vector();
			    Vector names = new Vector();
			    
			    if (distances != null) {
				    Iterator disIter = distances.iterator();
				    if (disIter.hasNext()) {
					    	ids.add("");
					    	names.add(iwrb.getLocalizedString("run_year_ddd.select_distance","Select distance..."));
				    }
				    while (disIter.hasNext()) {
				    		Group distance = (Group) disIter.next();
				    		String s = iwrb.getLocalizedString(distance.getName(),distance.getName());
				    		ids.add(distance.getPrimaryKey().toString());
				    		names.add(s);
				    }
				    if (distances.isEmpty()) {
				    		ids.add("");
				    		names.add("Unavailable");
				    } 
			    }
			    else {
			    		ids.add("");
			    		names.add("Unavailable");
			    }
			    
			    RemoteScriptingResults rsr = new RemoteScriptingResults(RemoteScriptHandler.getLayerName(sourceName, "id"), ids);
			    rsr.addLayer(RemoteScriptHandler.getLayerName(sourceName, "name"), names);
		
			    return rsr;
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		  
		  return null;
	}
	private RunBusiness getRunBiz(IWContext iwc) {
		RunBusiness business = null;
		try {
			business = (RunBusiness) IBOLookup.getServiceInstance(iwc, RunBusiness.class);
		}
		catch (IBOLookupException e) {
			business = null;
		}
		return business;
	}
}
