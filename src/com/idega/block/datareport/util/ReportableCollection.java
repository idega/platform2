/*
 * Created on 13.7.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.datareport.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import com.idega.data.IDOEntityField;
import com.idega.data.IDOReportableEntity;
import com.idega.data.IDOReportableField;
import com.idega.util.datastructures.QueueMap;

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
	
	private IDOReportableEntity _currentJRDataSource = null;
	private Object _defaultFieldValue = null;
	
	private QueueMap _extraHeaderParameters = new QueueMap();
	private ReportDescription _description = null;
	
	private IWDataSource _source = null;
	
	
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
	
	public JRDataSource getJRDataSource(){
		return new IWDataSource(this);
	}


	
	
	public void addField(ReportableField field){
		if(_description == null){
			_description = new ReportDescription();
		}
		_description.addField(field);
	}
	
	public void addField(IDOReportableField field){
		if(field instanceof ReportableField){
			addField((ReportableField)field);
		}else{
			addField(new ReportableField((IDOEntityField)field));
		}
	}
	
	
	//TODO Gummi generate keys!
	public void addExtraHeaderParameter(String labelKey, String LabelValue, String valueKey, String valueValue){
		if(_description == null){
			_description = new ReportDescription();
		}
		_description.addHeaderParameter(labelKey,LabelValue,valueKey,valueValue);
	}
	
	public void addExtraHeaderParameterAtBeginning(String labelKey, String LabelValue, String valueKey, String valueValue){
		if(_description == null){
			_description = new ReportDescription();
		}
		_description.addHeaderParameterAtBeginning(labelKey,LabelValue,valueKey,valueValue);
	}


	/**
	 * @param prm
	 */
	public void addExtraHeaderParameter(Map prm) {
		if(_description == null){
			_description = new ReportDescription();
		}
		_extraHeaderParameters.putAll(prm);
	}

	/**
	 * @return Returns the description.
	 */
	public ReportDescription getReportDescription() {
		return _description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setReportDescription(ReportDescription description) {
		this._description = description;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.jasperreports.engine.JRDataSource#next()
	 */
	public boolean next() throws JRException {
		if(_source == null){
			_source = (IWDataSource)getJRDataSource();
		}
		return _source.next();
	}

	/* (non-Javadoc)
	 * @see net.sf.jasperreports.engine.JRDataSource#getFieldValue(net.sf.jasperreports.engine.JRField)
	 */
	public Object getFieldValue(JRField jrField) throws JRException {
		return getFieldValue(new ReportableField(jrField));
	}
	
	public Object getFieldValue(ReportableField field) throws JRException {
		if(_source == null){
			next();
		}
		return _source.getFieldValue(field);
	}
	
	
	/**
	 * 
	 *  Last modified: $Date: 2004/10/08 10:15:36 $ by $Author: gummi $
	 * 
	 * @author <a href="mailto:gummi@idega.com">Gudmundur Agust Saemundsson</a>
	 * @version $Revision: 1.11 $
	 */
	private class IWDataSource implements JRDataSource {

		private ReportableCollection _coll = null;
		private Iterator _reportIterator = null;
		
		/**
		 * 
		 */
		public IWDataSource(ReportableCollection coll) {
			super();
			_coll = coll;
		}

		/* (non-Javadoc)
		 * @see net.sf.jasperreports.engine.JRDataSource#next()
		 */
		public boolean next() throws JRException {
			if(_reportIterator == null){
				_reportIterator = _coll.iterator();
			}
			
			try {
				if(_reportIterator.hasNext()){
					_coll._currentJRDataSource = (IDOReportableEntity)_reportIterator.next();
					return true;
				} else {
					_coll._currentJRDataSource = null;
					return false;
				} 
			} catch (ClassCastException e) {
				_coll._currentJRDataSource=null;
				e.printStackTrace();
				throw new JRException(e);
			}
		}

		/* (non-Javadoc)
		 * @see net.sf.jasperreports.engine.JRDataSource#getFieldValue(net.sf.jasperreports.engine.JRField)
		 */
		public Object getFieldValue(JRField field) throws JRException {
			return getFieldValue(new ReportableField(field));
		}
		
		public Object getFieldValue(ReportableField field) throws JRException {
			if(_reportIterator == null){
				next();
			}
			Object returner = _coll._currentJRDataSource.getFieldValue(field);
			if(returner == null){
				return _coll._defaultFieldValue;
			} else {
				return returner;
			}
		}
	}
	
}
