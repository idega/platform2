/*
 * $Id: ForumTopicText.java,v 1.1 2004/12/13 11:03:57 laddi Exp $
 * Created on 13.12.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.forum.presentation;

import com.idega.block.category.business.CategoryFinder;
import com.idega.block.category.data.ICCategory;
import com.idega.block.forum.business.ForumBusiness;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;


/**
 * Last modified: $Date: 2004/12/13 11:03:57 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class ForumTopicText extends Text {

	protected final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.forum";

	public void main(IWContext iwc) throws Exception {
		try {
			int topicID = Integer.parseInt(iwc.getParameter(ForumBusiness.PARAMETER_TOPIC_ID));

			ICCategory topic = CategoryFinder.getInstance().getCategory(topicID);
			setText(topic.getName(iwc.getCurrentLocale()));
		}
		catch (NumberFormatException e) {
			IWResourceBundle iwrb = getResourceBundle(iwc);
			setText(iwrb.getLocalizedString("forum", "Forum"));
		}
	}
	
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
}