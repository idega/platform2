/*
 * Created on 7.8.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.datareport.util;

import com.idega.data.IDOEntityField;
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
	
	private int _typeOfContainedField;
	private static final int CONTAINED_FIELD_TYPE_IDOFIELD = 0;
	private static final int CONTAINED_FIELD_TYPE_JRFIELD = 1;
	
	
	private JRField _jrField = null;
	private IDOEntityField _idoField = null;
	
	/**
	 * @param field
	 */
	public ReportableField(JRField field) {
		_jrField=field;
		_typeOfContainedField = CONTAINED_FIELD_TYPE_JRFIELD;
	}
	
	public ReportableField(IDOEntityField field){
		_idoField = field;
		_typeOfContainedField=CONTAINED_FIELD_TYPE_IDOFIELD;
	}


	/* (non-Javadoc)
	 * @see com.idega.data.IDOReportableField#getName()
	 */
	public String getName() {
		switch (_typeOfContainedField) {
			case CONTAINED_FIELD_TYPE_JRFIELD :
				return _jrField.getName();
			default :
				return _idoField.getSQLFieldName();
		}
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOReportableField#getDescription()
	 */
	public String getDescription() {
		switch (_typeOfContainedField) {
			case CONTAINED_FIELD_TYPE_JRFIELD :
				return _jrField.getDescription();
			default :
				return "No description";
		}
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOReportableField#setDescription(java.lang.String)
	 */
	public void setDescription(String description) {
		switch (_typeOfContainedField) {
			case CONTAINED_FIELD_TYPE_JRFIELD :
				_jrField.setDescription(description);
				break;
			default :
				System.out.println("["+this.getClass().getName()+"]: Not able to set description for IDOEntityField");
				break;
		}

	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOReportableField#getValueClass()
	 */
	public Class getValueClass() {
		switch (_typeOfContainedField) {
			case CONTAINED_FIELD_TYPE_JRFIELD :
				return _jrField.getValueClass();
			default :
				return _idoField.getDataTypeClass();
		}
	}

}
