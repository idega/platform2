package com.idega.block.datareport.data;


public interface ReportTranscription extends com.idega.data.IDOEntity
{
 public com.idega.core.data.ICFile getLayout();
 public com.idega.core.data.ICObjectInstance getObjectInstance();
 public com.idega.block.dataquery.data.Query getQuery();
 public void initializeAttributes();
 public void setLayout(com.idega.core.data.ICFile p0);
 public void setObjectInstance(com.idega.core.data.ICObjectInstance p0);
 public void setQuery(com.idega.block.dataquery.data.Query p0);
}
