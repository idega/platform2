/*
 * Created on May 21, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package com.idega.block.dataquery.data;

import com.idega.core.data.ICFileBMPBean;

/**
 * @author aron
 */
public class QueryBMPBean extends ICFileBMPBean implements Query{
	
	public int getXMLSchemaId() {
	   return Integer.parseInt((String) getMetaData("xml_schema_id"));
	 }
  
	 public void setXMLSchemaId(int xmlSchemaId) {
	   setMetaData("xml_schema_id", Integer.toString(xmlSchemaId));
	 }
	  
}
