/*
 * $Id: SchoolGroupSelectorCollectionHandler.java,v 1.1 2004/11/26 09:07:12 aron Exp $
 * Created on 25.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.school.presentation;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassHome;
import com.idega.block.school.data.SchoolHome;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.remotescripting.RemoteScriptCollection;
import com.idega.presentation.remotescripting.RemoteScriptHandler;
import com.idega.presentation.remotescripting.RemoteScriptingResults;

/**
 * 
 *  Last modified: $Date: 2004/11/26 09:07:12 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public class SchoolGroupSelectorCollectionHandler implements RemoteScriptCollection {

    /* (non-Javadoc)
     * @see com.idega.presentation.remotescripting.RemoteScriptCollection#getResults(com.idega.presentation.IWContext)
     */
    public RemoteScriptingResults getResults(IWContext iwc) {
        String sourceName = iwc.getParameter(RemoteScriptHandler.PARAMETER_SOURCE_PARAMETER_NAME);
		String action = iwc.getParameter(SchoolGroupSelector.PARAMETER_ACTION);

		String sourceID = iwc.getParameter(sourceName);

		if (SchoolGroupSelector.ACTION_UPDATE_SCHOOLS.equals(action)) {
		  return handleSchoolUpdate(sourceName, sourceID);
		} else if (SchoolGroupSelector.ACTION_UPDATE_GROUPS.equals(action)) {
		  return handleGroupUpdate(iwc, sourceName, sourceID);
		}
		return null;
	}
	
	private RemoteScriptingResults handleSchoolUpdate(String sourceName, String typeID) {
		if (typeID != null) {
		    
			try {
				SchoolHome schoolHome = (SchoolHome) IDOLookup.getHome(School.class);
				Collection schools = schoolHome.findAllBySchoolType(Integer.parseInt(typeID));
		
			    Vector ids = new Vector();
			    Vector names = new Vector();
			    
			    Iterator iter = schools.iterator();
			    if (iter.hasNext()) {
			    	ids.add("-1");
			    	names.add("Select");
			    }
			    while (iter.hasNext()) {
				    	School s = (School) iter.next();
				    	ids.add(s.getPrimaryKey());
				    	names.add(s.getName());
			    }
			    
			    if (schools.isEmpty()) {
			    		ids.add("-1");
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
	
	private RemoteScriptingResults handleGroupUpdate(IWContext iwc, String sourceName, String schoolID) {
		if (schoolID != null) {

			try {
				//String parTypeID = iwc.getParameter(SchoolGroupSelector.PARAMETER_TYPE_ID);
				//String typeID = iwc.getParameter(parTypeID);
				SchoolClassHome groupHome = (SchoolClassHome) IDOLookup.getHome(SchoolClass.class);
				Collection groups = groupHome.findBySchool(Integer.parseInt(schoolID));
		
			    Vector ids = new Vector();
			    Vector names = new Vector();
			    
			    Iterator iter = groups.iterator();
			    if (iter.hasNext()) {
			    	ids.add("-1");
			    	names.add("Select");
			    }
			    while (iter.hasNext()) {
			        SchoolClass c = (SchoolClass) iter.next();
			        ids.add(c.getPrimaryKey().toString());
			    		names.add(c.getName());
			    }
			    
			    if (groups.isEmpty()) {
			    		ids.add("-1");
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
}

