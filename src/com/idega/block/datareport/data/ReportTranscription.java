package com.idega.block.datareport.data;


public interface ReportTranscription extends com.idega.data.IDOEntity
{
 public com.idega.core.file.data.ICFile getLayout();
 public com.idega.core.component.data.ICObjectInstance getObjectInstance();
 public com.idega.block.dataquery.data.Query getQuery();
 public void initializeAttributes();
 public void setLayout(com.idega.core.file.data.ICFile p0);
 public void setObjectInstance(com.idega.core.component.data.ICObjectInstance p0);
 public void setQuery(com.idega.block.dataquery.data.Query p0);
}
