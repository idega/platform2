/*
 * $Id: SchoolUserChooser.java,v 1.2 2005/03/07 08:35:11 anna Exp $ Created on 24.2.2005
 * 
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
 */
package com.idega.block.school.presentation;

import com.idega.block.school.data.School;
import com.idega.presentation.text.Link;
import com.idega.user.presentation.UserChooser;

/**
 * Last modified: 24.2.2005 15:05:35 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna </a>
 * @version $Revision: 1.2 $
 */
public class SchoolUserChooser extends UserChooser {

	public final String PARAMETER_SCHOOL_ID = "prm_school_id";

	private School school;

	public SchoolUserChooser(String chooserName, School school) {
		super(chooserName);
		this.school = school;
	}

	protected void addParametersToLink(Link link) {
		link.addParameter(PARAMETER_SCHOOL_ID, school.getPrimaryKey().toString());
	}

	public Class getChooserWindowClass() {
		return SchoolUserChooserWindow.class;
	}
}