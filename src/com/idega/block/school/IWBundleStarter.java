/*
 * $Id: IWBundleStarter.java,v 1.2 2005/09/22 11:42:59 laddi Exp $
 * Created on 28.4.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.school;

import java.util.Collection;
import java.util.Iterator;
import javax.ejb.FinderException;
import com.idega.block.school.data.SchoolCategoryBMPBean;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolSeasonHome;
import com.idega.block.school.presentation.SchoolBlock;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.idegaweb.include.GlobalIncludeManager;


public class IWBundleStarter implements IWBundleStartable {

	public void start(IWBundle starterBundle) {
		updateData();
	}
	
	public void stop(IWBundle starterBundle) {
		// nothing to do
	}
	
	private void updateData() {
		GlobalIncludeManager includeManager = GlobalIncludeManager.getInstance();
		includeManager.addBundleStyleSheet(SchoolBlock.IW_BUNDLE_IDENTIFIER, "/style/school.css");

		try {
			SchoolSeasonHome home = (SchoolSeasonHome) IDOLookup.getHome(SchoolSeason.class);
			Collection seasons = home.findAllSchoolSeasonsWithoutCategory();
			if (!seasons.isEmpty()) {
				Iterator iter = seasons.iterator();
				while (iter.hasNext()) {
					SchoolSeason element = (SchoolSeason) iter.next();
					element.setSchoolCategory(SchoolCategoryBMPBean.CATEGORY_ELEMENTARY_SCHOOL);
					System.out.println("Updated category value for season = " + element.getSchoolSeasonName());
				}
			}
		}
		catch (IDOLookupException ile) {
			ile.printStackTrace();
		}
		catch (FinderException fe) {
			fe.printStackTrace();
		}
	}

}