package com.idega.block.dataquery.data;

import com.idega.core.file.data.ICFile;
import com.idega.util.xml.XMLFile;


public interface Query extends XMLFile,ICFile
{
 public int getXMLSchemaId();
 public void setXMLSchemaId(int p0);
}
