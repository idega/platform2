package com.idega.block.dataquery.data.sql;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Oct 13, 2003
 */
public class SQLStatementDynamic {
	
	private String statementWithDynamicFields = null;
	
	// id (String) description (String)
	private Map dynamicFieldDescription = new HashMap();
	
	// id (String) value (Object) 
	private Map dynamicFieldValue = new HashMap();
	
	// id (String) value (className)
	private Map dynamicFieldType = new HashMap();
	
	
}
