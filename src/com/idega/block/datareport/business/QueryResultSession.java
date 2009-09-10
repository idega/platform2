/*
 * Created on Jun 30, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.idega.block.datareport.business;

import java.util.Map;
import com.idega.block.dataquery.data.QueryResult;
import com.idega.business.IBOSession;


/**
 * <p>
 * TODO thomas Describe Type QueryResultSession
 * </p>
 *  Last modified: $Date: 2005/07/04 14:08:47 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.1 $
 */
public interface QueryResultSession extends IBOSession {

	/**
	 * @see com.idega.block.datareport.business.QueryResultSessionBean#storeQueryResult
	 */
	public void storeQueryResult(Map identifierValueMap, QueryResult queryResult) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.datareport.business.QueryResultSessionBean#getQueryResult
	 */
	public QueryResult getQueryResult(Map identifierValueMap) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.datareport.business.QueryResultSessionBean#deleteQueryResult
	 */
	public void deleteQueryResult() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.datareport.business.QueryResultSessionBean#setValue
	 */
	public void setValue(String identifier, Object value) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.datareport.business.QueryResultSessionBean#getValue
	 */
	public Object getValue(String identifier) throws java.rmi.RemoteException;
}
