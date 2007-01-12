/*
 * Created on May 21, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package com.idega.block.dataquery.data;

import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileBMPBean;

/**
 * @author aron
 */
public class QueryBMPBean extends ICFileBMPBean implements Query,ICFile{
	
	public int getXMLSchemaId() {
	   return Integer.parseInt(getMetaData("xml_schema_id"));
	 }
  
	 public void setXMLSchemaId(int xmlSchemaId) {
	   setMetaData("xml_schema_id", Integer.toString(xmlSchemaId));
	 }
	 
	 public boolean isTemplate(){
	 	return Boolean.getBoolean(getMetaData("templateflag"));
	 }
	 
	 public void setAsTemplate(boolean template){
	 	setMetaData("templateflag",template +"");	 
	 }
	  
}
