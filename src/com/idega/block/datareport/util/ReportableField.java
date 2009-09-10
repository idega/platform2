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

import net.sf.jasperreports.engine.JRField;

import com.idega.data.EntityAttribute;
import com.idega.data.IDOEntityField;
import com.idega.data.IDOReportableField;

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
	
	private int _fieldsMaxNumberOfCharacters = 15;
	
	/**
	 * @param field
	 */
	public ReportableField(JRField field) {
		this._jrField=field;
		this._typeOfContainedField = CONTAINED_FIELD_TYPE_JRFIELD;
	}
	

	public ReportableField(String name, Class valueClass) {
		this._customMadeFiledName = name;
		this._customMadeValueClass = valueClass;
		this._typeOfContainedField = CONTAINED_FIELD_TYPE_NONE;
	}
	
	public ReportableField(IDOEntityField field){
		this._idoField = field;
		this._typeOfContainedField=CONTAINED_FIELD_TYPE_IDOFIELD;
		this._fieldsMaxNumberOfCharacters=this._idoField.getMaxLength();
		if(this._idoField instanceof EntityAttribute){
			this._localizedNames = ((EntityAttribute)this._idoField).getMapOfLocalizedNames();
		}
	}
	
	public ReportableField(String name, IDOEntityField field){
		this(field);
		this._customMadeFiledName = name;
	}
	
	
	public void setCustomMadeFieldName(String name){
		this._customMadeFiledName = name;
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOReportableField#getName()
	 */
	public String getName() {
		if(this._customMadeFiledName != null){
			return this._customMadeFiledName;
		} else {
			switch (this._typeOfContainedField) {
				case CONTAINED_FIELD_TYPE_JRFIELD :
					return this._jrField.getName();
				default :
					return this._idoField.getSQLFieldName();
			}			
		}

	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOReportableField#getDescription()
	 */
	public String getDescription() {
		switch (this._typeOfContainedField) {
			case CONTAINED_FIELD_TYPE_JRFIELD :
				return this._jrField.getDescription();
			default :
				return "No description";
		}
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOReportableField#setDescription(java.lang.String)
	 */
	public void setDescription(String description) {
		switch (this._typeOfContainedField) {
			case CONTAINED_FIELD_TYPE_JRFIELD :
				this._jrField.setDescription(description);
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
		if(this._customMadeValueClass != null){
			return this._customMadeValueClass;
		} else {
			switch (this._typeOfContainedField) {
				case CONTAINED_FIELD_TYPE_JRFIELD :
					return this._jrField.getValueClass();
				default :
					return this._idoField.getDataTypeClass();
			}
		}
		
	}
	
	public void setValueClass(Class valClass){
		this._customMadeValueClass = valClass;
	}

	public String getValueClassName() {
		return getValueClass().getName();
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOReportableField#getLocalizedName(java.util.Locale)
	 */
	public String getLocalizedName(Locale locale) {
		String s = (String)this._localizedNames.get(locale);
		if(s == null){
			s = getName();
		}
		return s;
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOReportableField#setLocalizedName(java.lang.String, java.util.Locale)
	 */
	public void setLocalizedName(String name, Locale locale) {
		this._localizedNames.put(locale, name);
	}
	
	public int getMaxNumberOfCharacters(){
		return this._fieldsMaxNumberOfCharacters;
	}
	
	public void setMaxNumberOfCharacters(int number){
		this._fieldsMaxNumberOfCharacters = number;
	}

}
