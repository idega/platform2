package com.idega.block.dataquery.data;


public interface QuerySequence extends com.idega.data.IDOEntity,com.idega.data.TreeableEntity
{
	public java.lang.String getIDColumnName();
 public java.lang.String getName();
 public com.idega.block.dataquery.data.UserQuery getRealQuery();
 public void setName(java.lang.String p0);
 public void setRealQuery(com.idega.block.dataquery.data.UserQuery p0);
}
