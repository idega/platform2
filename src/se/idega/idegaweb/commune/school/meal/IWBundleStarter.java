/*
 * $Id$
 * Created on Sep 29, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.meal;

import se.idega.idegaweb.commune.school.meal.util.MealConstants;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.idegaweb.include.GlobalIncludeManager;


public class IWBundleStarter implements IWBundleStartable {

	public void start(IWBundle starterBundle) {
		GlobalIncludeManager includeManager = GlobalIncludeManager.getInstance();
		includeManager.addBundleStyleSheet(MealConstants.IW_BUNDLE_IDENTIFIER, "/style/meals.css");
	}

	public void stop(IWBundle starterBundle) {
	}
}
