/*
 * $Id: ReportDescription.java,v 1.1.2.1 2007/01/12 19:31:42 idegaweb Exp $
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
 *  Last modified: $Date: 2007/01/12 19:31:42 $ by $Author: idegaweb $
 * 
 * @author <a href="mailto:gummi@idega.com">Gudmundur Agust Saemundsson</a>
 * @version $Revision: 1.1.2.1 $
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
		this._valueMap = new QueueMap();
		this._labelKeys = new ArrayList();
		this._prmKeys = new ArrayList();
		this._prmTypeMap = new HashMap();
		this._withMap = new HashMap();
	}
	


	public void addHeaderParameter(String labelKey, int labelWidth, String prmKey, Class type, int prmWith) {
		this._labelKeys.add(labelKey);
		this._withMap.put(labelKey,new Integer(labelWidth));
		this._prmKeys.add(prmKey);
		this._prmTypeMap.put(prmKey,type);
		this._withMap.put(prmKey,new Integer(prmWith));
	}
	
	public void addHeaderParameterAtBeginning(String labelKey, int labelWidth, String prmKey, Class type, int prmWith) {
		this._labelKeys.add(0,labelKey);
		this._withMap.put(labelKey,new Integer(labelWidth));
		this._prmKeys.add(0,prmKey);
		this._prmTypeMap.put(prmKey,type);
		this._withMap.put(prmKey,new Integer(prmWith));
	}

	public List getListOfHeaderParameterLabelKeys(){
		return this._labelKeys;
	}
	
	public List getListOfHeaderParameterKeys(){
		return this._prmKeys;
	}
	
	public String getParameterOrLabelName(String key){
		return (String)this._valueMap.get(key);
	}
	
	public int getWithOfParameterOrLabel(String key){
		return ((Integer)this._withMap.get(key)).intValue();
	}
	
	public Class getParameterClassType(String key){
		return (Class)this._prmTypeMap.get(key);
	}
	
	
	/**
	 * @param string
	 * @return
	 */
	public Object get(String key) {
		return this._valueMap.get(key);
	}

	/**
	 * @param headerParameters
	 */
	public void putAll(Map headerParameters) {
		this._valueMap.putAll(headerParameters);
	}

	/**
	 * @param name
	 * @param localizedName
	 */
	public void put(String name, String localizedName) {
		this._valueMap.put(name,localizedName);
	}

	/**
	 * @return
	 */
	public Map getDisplayValueMap() {
		return this._valueMap;
	}

	/**
	 * @param name
	 */
	public void remove(String name) {
		this._valueMap.remove(name);
	}


	/**
	 * @param field
	 * @return
	 */
	public String getColumnName(ReportableField field) {
		return (String)this._valueMap.get(field.getName());
	}
	
	public void setColumnName(ReportableField field, String name){
		String key = field.getName();
		if(this._valueMap.containsKey(key)){
			this._valueMap.remove(key);
		}
		this._valueMap.put(key,name);
	}

	/**
	 * @param valueKey
	 * @param valueValue
	 */
	public void putAtBeginning(String key, String value) {
		this._valueMap.putAtBeginning(key,value);
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
		this._listOfFields.add(field);
		setColumnName(field,field.getLocalizedName(this._locale));
	}
	
	public List getListOfFields(){
		return this._listOfFields;
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
		return this._listOfFields.size();
	}

	public void setLocale(Locale locale){
		if(getNumberOfFields()>0){
			if(this._locale == null || !this._locale.equals(locale) ){
				this._locale = locale;
				for (Iterator iter = this._listOfFields.iterator(); iter.hasNext();) {
					ReportableField field = (ReportableField) iter.next();
					setColumnName(field,field.getLocalizedName(locale));
				}
			}
		} else {
			this._locale = locale;
		}
		
	}



	/**
	 * @param tmpReportDescriptionForCollectingData
	 */
	public void merge(ReportDescription desc) {
		
		if(this._locale != null){
			if(!this._locale.equals(desc._locale)){
				desc.setLocale(this._locale);
			}
		} else if(desc._locale != null){
			setLocale(desc._locale);
		}
		
		for (Iterator iter = desc._listOfFields.iterator(); iter.hasNext();) {
			Object field = iter.next();
			if(!this._listOfFields.contains(field)){
				this._listOfFields.add(field);
			}
		}
		
		for (Iterator iter = desc._labelKeys.iterator(); iter.hasNext();) {
			Object field = iter.next();
			if(!this._labelKeys.contains(field)){
				this._labelKeys.add(field);
			}
		}
		
		for (Iterator iter = desc._prmKeys.iterator(); iter.hasNext();) {
			Object field = iter.next();
			if(!this._prmKeys.contains(field)){
				this._prmKeys.add(field);
			}
		}
		
		this._valueMap.putAll(desc._valueMap);
		this._prmTypeMap.putAll(desc._prmTypeMap);
		this._withMap.putAll(desc._withMap);
		
	}



	/**
	 * @return
	 */
	public boolean doCreateNewLineForEachParameter() {
		return this.doCreateNewLineForEachParameter;
	}
	
	
	public void setToCreateNewLineForEachParameter(boolean value) {
		this.doCreateNewLineForEachParameter = value;
	}
	
	
}
