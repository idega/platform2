package com.idega.block.dataquery.data.sql;

import java.util.Map;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Oct 13, 2003
 */
public interface DynamicExpression extends Expression {
	
	public boolean isDynamic();
	
	public Map getIdentifierValueMap();
	
	public Map getIdentifierInputDescriptionMap();
	
	public void setIdentifierValueMap(Map identifierValueMap);
	

}
