/*
<<<<<<< CurrentResidencyBMPBean.java
 * $Id: CurrentResidencyBMPBean.java,v 1.3 2004/06/05 07:42:01 aron Exp $
=======

 * $Id: CurrentResidencyBMPBean.java,v 1.3 2004/06/05 07:42:01 aron Exp $

>>>>>>> 1.2
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */

package is.idega.idegaweb.campus.block.application.data;


import java.sql.SQLException;

import javax.ejb.FinderException;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */

public class CurrentResidencyBMPBean
	extends com.idega.data.GenericEntity
	implements is.idega.idegaweb.campus.block.application.data.CurrentResidency {

	private static final String name_ = "cam_curr_res";

	private static final String description_ = "description";

	private static final String requiresExtraInfo_ = "requires_extra_info";

	public CurrentResidencyBMPBean() {
		super();
	}

	public CurrentResidencyBMPBean(int id) throws SQLException {
		super(id);
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(description_, "L?sing", true, true, "java.lang.String");
		addAttribute(requiresExtraInfo_, "?arfnast vi?b?taruppl?singa", true, true, "java.lang.String");
		setMaxLength(description_, 255);
		setMaxLength(requiresExtraInfo_, 1);
	}

	public String getEntityName() {
		return (name_);
	}

	public String getDescriptionColumnName() {
		return (description_);
	}

	public String getRequiresExtranInfoColumnName() {

		return (requiresExtraInfo_);
	}
	public String getName() {
		return (getDescription());
	}

	public String getDescription() {
		return ((String) getColumnValue(description_));
	}
	public void setDescription(String description) {
		setColumn(description_, description);
	}

	public boolean getRequiresExtraInfo() {
		String tmp = (String) getColumnValue(requiresExtraInfo_);
		if (tmp.equalsIgnoreCase("y"))
			return (true);
		else
			return (false);
	}

	public void setRequiresExtranInfo(boolean extraInfo) {
		if (extraInfo)
			setColumn(requiresExtraInfo_, "Y");
		else
			setColumn(requiresExtraInfo_, "N");
	}

	public java.util.Collection ejbFindAll() throws FinderException{
		return super.idoFindAllIDsBySQL();
	}
}
