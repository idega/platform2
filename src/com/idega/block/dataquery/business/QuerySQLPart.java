/*
 * Created on Aug 6, 2003
 *
 */
package com.idega.block.dataquery.business;

import com.idega.xml.XMLElement;

/**
 * QuerySQLPart
 * @author aron 
 * @version 1.0
 */

public class QuerySQLPart implements QueryPart {

	private String sqlquery;
	
	public QuerySQLPart(String sql){
		this.sqlquery = sql;
	}
	
	public QuerySQLPart(XMLElement xml){
		this("");
		if(xml.hasChildren()){
			
		}
	}
	
	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.business.QueryPart#getQueryElement()
	 */
	public XMLElement getQueryElement() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.business.QueryPart#encode()
	 */
	public String encode() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.business.QueryPart#isLocked()
	 */
	public boolean isLocked() {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.business.QueryPart#setLocked(boolean)
	 */
	public void setLocked(boolean locked) {

	}

}
