/*
 * Created on Jun 30, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.idega.block.datareport.business;

import com.idega.business.IBOHomeImpl;


/**
 * <p>
 * TODO thomas Describe Type QueryResultSessionHomeImpl
 * </p>
 *  Last modified: $Date: 2005/07/04 14:08:47 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.1 $
 */
public class QueryResultSessionHomeImpl extends IBOHomeImpl implements QueryResultSessionHome {

	protected Class getBeanInterfaceClass() {
		return QueryResultSession.class;
	}

	public QueryResultSession create() throws javax.ejb.CreateException {
		return (QueryResultSession) super.createIBO();
	}
}
