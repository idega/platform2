package com.idega.block.dataquery.data;

import com.idega.core.data.ICFileBMPBean;
import com.idega.util.xml.XMLFile;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on May 21, 2003
 */
public class QueryResultEntityBMPBean extends ICFileBMPBean implements QueryResultEntity, XMLFile {
  
  
  public int getXMLSchemaId() {
    return Integer.parseInt((String) getMetaData("xml_schema_id"));
  }
  
  public void setXMLSchemaId(int xmlSchemaId) {
    setMetaData("xml_schema_id", Integer.toString(xmlSchemaId));
  }
  
  public void setReportQueryId(int reportQueryId) {
    setMetaData("report_query_id", Integer.toString(reportQueryId));
  }
  
  public int  getReportQueryId()  {
    return Integer.parseInt( (String) getMetaData("report_query_id"));
  }
      


}
