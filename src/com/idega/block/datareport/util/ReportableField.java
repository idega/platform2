/*
 * Created on 7.8.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.datareport.util;

import com.idega.data.IDOReportableField;

import dori.jasper.engine.JRField;

/**
 * Title:		ReportableField
 * Description:
 * Copyright:	Copyright (c) 2003
 * Company:		idega Software
 * @author		2003 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class ReportableField implements IDOReportableField, JRField {
	
	JRField _field = null;
	
	/**
	 * @param field
	 */
	public ReportableField(JRField field) {
		_field=field;
	}


	/* (non-Javadoc)
	 * @see com.idega.data.IDOReportableField#getName()
	 */
	public String getName() {
		return _field.getName();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOReportableField#getDescription()
	 */
	public String getDescription() {
		return _field.getDescription();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOReportableField#setDescription(java.lang.String)
	 */
	public void setDescription(String description) {
		_field.setDescription(description);

	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOReportableField#getValueClass()
	 */
	public Class getValueClass() {
		return _field.getValueClass();
	}

}
