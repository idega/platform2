/*
 * $Id: SchoolGroupSelectorCollectionHandler.java,v 1.1 2004/11/26 09:07:12 aron
 * Exp $ Created on 25.11.2004
 * 
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
 */
package com.idega.block.school.presentation;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import com.idega.block.school.data.SchoolYear;
import com.idega.block.school.data.SchoolYearHome;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.remotescripting.RemoteScriptCollection;
import com.idega.presentation.remotescripting.RemoteScriptHandler;
import com.idega.presentation.remotescripting.RemoteScriptingResults;

/**
 * 
 * Last modified: $Date: 2005/01/07 10:05:30 $ by $Author: laddi $
 * 
 * @author <a href="mailto:aron@idega.com">aron </a>
 * @version $Revision: 1.2 $
 */
public class SchoolYearSelectorCollectionHandler implements RemoteScriptCollection {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.presentation.remotescripting.RemoteScriptCollection#getResults(com.idega.presentation.IWContext)
	 */
	public RemoteScriptingResults getResults(IWContext iwc) {
		IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle("com.idega.block.school").getResourceBundle(iwc.getCurrentLocale());
		String sourceName = iwc.getParameter(RemoteScriptHandler.PARAMETER_SOURCE_PARAMETER_NAME);

		String sourceID = iwc.getParameter(sourceName);

		return handleYearUpdate(sourceName, sourceID, iwrb);
	}

	private RemoteScriptingResults handleYearUpdate(String sourceName, String typeID, IWResourceBundle iwrb) {
		if (typeID != null) {

			try {
				SchoolYearHome schoolYearHome = (SchoolYearHome) IDOLookup.getHome(SchoolYear.class);
				Collection schools = schoolYearHome.findAllSchoolYearBySchoolType(Integer.parseInt(typeID));

				Vector ids = new Vector();
				Vector names = new Vector();

				Iterator iter = schools.iterator();
				if (iter.hasNext()) {
					ids.add("-1");
					names.add(iwrb.getLocalizedString("select_year", "Select year"));
				}
				while (iter.hasNext()) {
					SchoolYear s = (SchoolYear) iter.next();
					ids.add(s.getPrimaryKey());
					names.add(s.getName());
				}

				if (schools.isEmpty()) {
					ids.add("-1");
					names.add(iwrb.getLocalizedString("unavailable", "Unavailable"));
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