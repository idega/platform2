/*
 * Created on 7.8.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.datareport.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.idega.data.EntityAttribute;
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
	private static final int CONTAINED_FIELD_TYPE_NONE = 2;
	private Map _localizedNames = new HashMap();
	
	private JRField _jrField = null;
	private IDOEntityField _idoField = null;
	private String _customMadeFiledName = null;
	private Class _customMadeValueClass = null;
	
	/**
	 * @param field
	 */
	public ReportableField(JRField field) {
		_jrField=field;
		_typeOfContainedField = CONTAINED_FIELD_TYPE_JRFIELD;
	}
	

	public ReportableField(String name, Class valueClass) {
		_customMadeFiledName = name;
		_customMadeValueClass = valueClass;
		_typeOfContainedField = CONTAINED_FIELD_TYPE_NONE;
	}
	
	public ReportableField(IDOEntityField field){
		_idoField = field;
		_typeOfContainedField=CONTAINED_FIELD_TYPE_IDOFIELD;
		if(_idoField instanceof EntityAttribute){
			_localizedNames = ((EntityAttribute)_idoField).getMapOfLocalizedNames();
		}
	}
	
	
	public void setCustomMadeFieldName(String name){
		_customMadeFiledName = name;
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOReportableField#getName()
	 */
	public String getName() {
		if(_customMadeFiledName != null){
			return _customMadeFiledName;
		} else {
			switch (_typeOfContainedField) {
				case CONTAINED_FIELD_TYPE_JRFIELD :
					return _jrField.getName();
				default :
					return _idoField.getSQLFieldName();
			}			
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
		if(_customMadeValueClass != null){
			return _customMadeValueClass;
		} else {
			switch (_typeOfContainedField) {
				case CONTAINED_FIELD_TYPE_JRFIELD :
					return _jrField.getValueClass();
				default :
					return _idoField.getDataTypeClass();
			}
		}
		
	}
	
	public void setValueClass(Class valClass){
		_customMadeValueClass = valClass;
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOReportableField#getLocalizedName(java.util.Locale)
	 */
	public String getLocalizedName(Locale locale) {
		return (String)_localizedNames.get(locale);
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOReportableField#setLocalizedName(java.lang.String, java.util.Locale)
	 */
	public void setLocalizedName(String name, Locale locale) {
		_localizedNames.put(locale, name);
	}

}
