/*
 * Created on Jun 29, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.idega.block.datareport.business;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.idega.block.dataquery.data.QueryResult;
import com.idega.business.IBOSessionBean;


/**
 * <p>
 * TODO thomas Describe Type QueryResultSessionBean
 * </p>
 *  Last modified: $Date: 2007/01/12 19:31:31 $ by $Author: idegaweb $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.1.2.1 $
 */
public class QueryResultSessionBean extends IBOSessionBean  implements QueryResultSession{
	
	Map identifierValueMap = null;
	
	QueryResult queryResult = null;
	
	public void storeQueryResult(Map identifierValueMap, QueryResult queryResult) {
		this.queryResult = queryResult;
		this.identifierValueMap = identifierValueMap;
	}
	
	public QueryResult getQueryResult(Map identifierValueMap) {
		if (mapsAreEqual(identifierValueMap)) {
			return this.queryResult;
		}
		deleteQueryResult();
		return null;
	}
	
	public void deleteQueryResult() {
		this.identifierValueMap = null;
		this.queryResult = null;
	}
	
	
	public void setValue(String identifier, Object value) {
		if (this.identifierValueMap == null) {
			this.identifierValueMap = new HashMap();
		}
		this.identifierValueMap.put(identifier, value);
	}
	
	public Object getValue(String identifier) {
		if (this.identifierValueMap == null) {
			return null;
		}
		return this.identifierValueMap.get(identifier);
	}
	
	private boolean mapsAreEqual(Map identifierValueMap) {
		if (this.identifierValueMap == null && identifierValueMap == null) {
			return true;
		}
		// this.identifierMap is not null
		if (identifierValueMap == null) {
			return false;
		}
		// both maps are not null
		if (identifierValueMap.size() != this.identifierValueMap.size()) {
			return false;
		}
		// both maps have the same size
		// compare now the values
		Iterator iterator = identifierValueMap.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			Object value = identifierValueMap.get(key);
			Object previousValue = this.identifierValueMap.get(key);
			if (! valuesAreEqual(previousValue, value)) {
				return false;
			}
		}
		return true;
	}
	
	private boolean valuesAreEqual(Object value1, Object value2) {
		if (value1 == null && value2 == null) {
			return true;
		}
		// value1 is not null
		if (value1 instanceof String) {
			return ((String) value1).equals(value2);
		}
		// value1 is a list
		if (value2 instanceof String) {
			return false;
		}
		// both values are lists
		if (((List) value1).size() != ((List) value2).size()) { 
			return false;
		}
		// both list have the same size
		int size = ((List) value1).size();
		for (int i = 0; i < size; i++) {
			String object1 = ((List) value1).get(i).toString();
			String object2 = ((List) value2).get(i).toString();
			if (! object1.equals(object2)) {
				return false;
			}
		}
		return true;
	}
}
