/*
 * $Id: QueryLogBMPBean.java,v 1.1 2004/09/09 16:53:04 thomas Exp $
 * Created on Sep 9, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.dataquery.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;


/**
 * 
 *  Last modified: $Date: 2004/09/09 16:53:04 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.1 $
 */
public class QueryLogBMPBean extends GenericEntity implements QueryLog {
	
	private static final String ENTITY_NAME = "QUERY_LOG";
	private static final String COLUMN_NAME_STATEMENT = "STATEMENT";
	private static final String COLUMN_NAME_TRANSACTION_ID = "TRANSACTION_ID";

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME_STATEMENT, "Statement", true, true, String.class);
		addAttribute(COLUMN_NAME_TRANSACTION_ID, "Transcation ID", true, true, String.class);
	}
	
	public void setStatement(String statement) {
		setColumn(COLUMN_NAME_STATEMENT, statement);
	}
	
	public String getStatement() {
		return getStringColumnValue(COLUMN_NAME_STATEMENT);
	}
	
	public void setTransactionID(String transactionID) {
		setColumn(COLUMN_NAME_TRANSACTION_ID, transactionID);
	}
	
	public String getTransactionID() {
		return getStringColumnValue(COLUMN_NAME_TRANSACTION_ID);
	}
	
	public Collection ejbFindAll() throws FinderException {
		return this.idoFindAllIDsBySQL();
	}
	
	public Collection ejbFindByTransactionID(String transactionID) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getEntityName());
		sql.appendWhereEquals(COLUMN_NAME_TRANSACTION_ID, transactionID);
		return this.idoFindPKsByQuery(sql);
	}

}
