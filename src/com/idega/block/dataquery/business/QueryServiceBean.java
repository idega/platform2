/*
 * Created on May 22, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package com.idega.block.dataquery.business;

import com.idega.business.IBOServiceBean;
import com.idega.util.xml.XMLData;
import com.idega.util.xml.XMLFile;

/**
 * @author aron
 */
public class QueryServiceBean extends IBOServiceBean {

	public QueryHelper getQueryHelper(XMLFile xmlFile){
		XMLData data = XMLData.getInstanceForFile(xmlFile);
		return new QueryHelper(data.getDocument());
	}
	
	public QueryHelper getQueryHelper(int xmlFileID){
		XMLData data = XMLData.getInstanceForFile(xmlFileID);
		return new QueryHelper(data.getDocument());
	}
	
	public QueryHelper getQueryHelper(){
		return new QueryHelper();
	}


}

