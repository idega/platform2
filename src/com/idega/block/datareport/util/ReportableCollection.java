/*
 * Created on 13.7.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.datareport.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.idega.data.IDOReportableEntity;
import com.idega.data.IDOReportableField;
import com.idega.util.datastructures.QueueMap;

import dori.jasper.engine.JRDataSource;
import dori.jasper.engine.JRException;
import dori.jasper.engine.JRField;

/**
 * Title:		IDOReportableCollection
 * Description: Use addCollection(Collection) to add result 
 * 				from IDOEntities that implement IDOReportableEntity 
 * 				to be able to use that result in reports
 * Copyright:	Copyright (c) 2003
 * Company:		idega Software
 * @author		2003 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class ReportableCollection extends Vector implements JRDataSource {
	
	private Iterator _reportIterator = null;
	private IDOReportableEntity _currentJRDataSource = null;
	private List _fields = new ArrayList();
	private Object _defaultFieldValue = null;
	
	private Map _extraHeaderParameters = new QueueMap();
	
	
	/**
	 * @param initialCapacity
	 * @param capacityIncrement
	 */
	public ReportableCollection(int initialCapacity, int capacityIncrement) {
		super(initialCapacity, capacityIncrement);
	}

	/**
	 * @param initialCapacity
	 */
	public ReportableCollection(int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * 
	 */
	public ReportableCollection() {
		super();
	}

	/**
	 * @param c
	 */
	public ReportableCollection(Collection c) {
		super(c);
	}

	/* (non-Javadoc)
	 * @see dori.jasper.engine.JRDataSource#next()
	 */
	public boolean next() throws JRException {
		if(_reportIterator == null){
			_reportIterator = this.iterator();
		}
		
		try {
			if(_reportIterator.hasNext()){
				_currentJRDataSource = (IDOReportableEntity)_reportIterator.next();
				return true;
			} else {
				_currentJRDataSource = null;
				return false;
			} 
		} catch (ClassCastException e) {
			_currentJRDataSource=null;
			e.printStackTrace();
			throw new JRException(e);
		}
	}

	/* (non-Javadoc)
	 * @see dori.jasper.engine.JRDataSource#getFieldValue(dori.jasper.engine.JRField)
	 */
	public Object getFieldValue(JRField field) throws JRException {
		if(_reportIterator == null){
			next();
		}
		Object returner = _currentJRDataSource.getFieldValue(new ReportableField(field));
		if(returner == null){
			return _defaultFieldValue;
		} else {
			return returner;
		}
	}
	
	public List getListOfFields(){
		return _fields;
	}
	
	public void addField(IDOReportableField field){
		_fields.add(field);
	}
	
	public Map getExtraHeaderParameters(){
		return _extraHeaderParameters;
	}
	
	public void addExtraHeaderParameter(String labelKey, String LabelValue, String valueKey, String valueValue){
		_extraHeaderParameters.put(labelKey,LabelValue);
		_extraHeaderParameters.put(valueKey,valueValue);
	}

}
