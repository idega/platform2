/*
 * $Id: ReportDescription.java,v 1.1 2004/10/07 14:56:21 gummi Exp $
 * Created on 22.9.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.datareport.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import com.idega.util.datastructures.QueueMap;


/**
 * 
 *  Last modified: $Date: 2004/10/07 14:56:21 $ by $Author: gummi $
 * 
 * @author <a href="mailto:gummi@idega.com">Gudmundur Agust Saemundsson</a>
 * @version $Revision: 1.1 $
 */
public class ReportDescription {

	private List _listOfFields = new ArrayList();
	
	private QueueMap _valueMap = null;
	private List _labelKeys = null;
	private List _prmKeys = null;
	private Map _prmTypeMap = null;
	private Map _withMap = null;
	private int _defaultWidth = 20;
	private Locale _locale = null;
	private boolean doCreateNewLineForEachParameter = true;
	
	/**
	 * 
	 */
	public ReportDescription() {
		super();
		_valueMap = new QueueMap();
		_labelKeys = new ArrayList();
		_prmKeys = new ArrayList();
		_prmTypeMap = new HashMap();
		_withMap = new HashMap();
	}
	


	public void addHeaderParameter(String labelKey, int labelWidth, String prmKey, Class type, int prmWith) {
		_labelKeys.add(labelKey);
		_withMap.put(labelKey,new Integer(labelWidth));
		_prmKeys.add(prmKey);
		_prmTypeMap.put(prmKey,type);
		_withMap.put(prmKey,new Integer(prmWith));
	}
	
	public void addHeaderParameterAtBeginning(String labelKey, int labelWidth, String prmKey, Class type, int prmWith) {
		_labelKeys.add(0,labelKey);
		_withMap.put(labelKey,new Integer(labelWidth));
		_prmKeys.add(0,prmKey);
		_prmTypeMap.put(prmKey,type);
		_withMap.put(prmKey,new Integer(prmWith));
	}

	public List getListOfHeaderParameterLabelKeys(){
		return _labelKeys;
	}
	
	public List getListOfHeaderParameterKeys(){
		return _prmKeys;
	}
	
	public String getParameterOrLabelName(String key){
		return (String)_valueMap.get(key);
	}
	
	public int getWithOfParameterOrLabel(String key){
		return ((Integer)_withMap.get(key)).intValue();
	}
	
	public Class getParameterClassType(String key){
		return (Class)_prmTypeMap.get(key);
	}
	
	
	/**
	 * @param string
	 * @return
	 */
	public Object get(String key) {
		return _valueMap.get(key);
	}

	/**
	 * @param headerParameters
	 */
	public void putAll(Map headerParameters) {
		_valueMap.putAll(headerParameters);
	}

	/**
	 * @param name
	 * @param localizedName
	 */
	public void put(String name, String localizedName) {
		_valueMap.put(name,localizedName);
	}

	/**
	 * @return
	 */
	public Map getDisplayValueMap() {
		return _valueMap;
	}

	/**
	 * @param name
	 */
	public void remove(String name) {
		_valueMap.remove(name);
	}


	/**
	 * @param field
	 * @return
	 */
	public String getColumnName(ReportableField field) {
		return (String)_valueMap.get(field.getName());
	}
	
	public void setColumnName(ReportableField field, String name){
		String key = field.getName();
		if(_valueMap.containsKey(key)){
			_valueMap.remove(key);
		}
		_valueMap.put(key,name);
	}

	/**
	 * @param valueKey
	 * @param valueValue
	 */
	public void putAtBeginning(String key, String value) {
		_valueMap.putAtBeginning(key,value);
	}



	/**
	 * @param labelKey
	 * @param labelValue
	 * @param valueKey
	 * @param valueValue
	 */
	public void addHeaderParameter(String labelKey, String labelValue, String valueKey, String valueValue) {
		addHeaderParameter(labelKey, calculateTextFieldWidthForString(labelValue), valueKey, String.class, calculateTextFieldWidthForString(valueValue));
		put(labelKey, labelValue);
		put(valueKey, valueValue);
	}
	
	/**
	 * @param labelKey
	 * @param labelValue
	 * @param valueKey
	 * @param valueValue
	 */
	public void addHeaderParameterAtBeginning(String labelKey, String labelValue, String valueKey, String valueValue) {
		addHeaderParameterAtBeginning(labelKey, calculateTextFieldWidthForString(labelValue), valueKey, String.class, calculateTextFieldWidthForString(valueValue));
		put(labelKey, labelValue);
		put(valueKey, valueValue);
	}
	
	
	private int calculateTextFieldWidthForString(String str) {
		int fontSize = 9;
		return (int) (5 + (str.length() * fontSize * 0.58));
	}



	/**
	 * @param field
	 */
	public void addField(ReportableField field) {
		_listOfFields.add(field);
		setColumnName(field,field.getLocalizedName(_locale));
	}
	
	public List getListOfFields(){
		return _listOfFields;
	}



	/**
	 * @param allFields
	 */
	public void addFields(List fields) {
		if(fields!=null){
			for (Iterator iter = fields.iterator(); iter.hasNext();) {
				ReportableField field = (ReportableField) iter.next();
				addField(field);
			}
		}
		
	}



	/**
	 * @return
	 */
	public int getNumberOfFields() {
		return _listOfFields.size();
	}

	public void setLocale(Locale locale){
		if(getNumberOfFields()>0){
			if(_locale == null || !_locale.equals(locale) ){
				_locale = locale;
				for (Iterator iter = _listOfFields.iterator(); iter.hasNext();) {
					ReportableField field = (ReportableField) iter.next();
					setColumnName(field,field.getLocalizedName(locale));
				}
			}
		} else {
			_locale = locale;
		}
		
	}



	/**
	 * @param tmpReportDescriptionForCollectingData
	 */
	public void merge(ReportDescription desc) {
		
		if(_locale != null){
			if(!_locale.equals(desc._locale)){
				desc.setLocale(_locale);
			}
		} else if(desc._locale != null){
			setLocale(desc._locale);
		}
		
		for (Iterator iter = desc._listOfFields.iterator(); iter.hasNext();) {
			Object field = (Object) iter.next();
			if(!_listOfFields.contains(field)){
				_listOfFields.add(field);
			}
		}
		
		for (Iterator iter = desc._labelKeys.iterator(); iter.hasNext();) {
			Object field = (Object) iter.next();
			if(!_labelKeys.contains(field)){
				_labelKeys.add(field);
			}
		}
		
		for (Iterator iter = desc._prmKeys.iterator(); iter.hasNext();) {
			Object field = (Object) iter.next();
			if(!_prmKeys.contains(field)){
				_prmKeys.add(field);
			}
		}
		
		_valueMap.putAll(desc._valueMap);
		_prmTypeMap.putAll(desc._prmTypeMap);
		_withMap.putAll(desc._withMap);
		
	}



	/**
	 * @return
	 */
	public boolean doCreateNewLineForEachParameter() {
		return doCreateNewLineForEachParameter;
	}
	
	
	public void setToCreateNewLineForEachParameter(boolean value) {
		doCreateNewLineForEachParameter = value;
	}
	
	
}
