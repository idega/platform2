package com.idega.block.application.data;


public interface ApplicationSubject extends com.idega.data.IDOEntity
{
 public java.sql.Date getCreated();
 public java.lang.String getCreatedColumnName();
 public java.lang.String getDescription();
 public java.lang.String getDescriptionColumnName();
 public java.sql.Date getExpires();
 public java.lang.String getExpiresColumnName();
 public java.lang.String getExtraAttribute();
 public java.lang.String getName();
 public java.lang.String getStatus();
 public java.lang.String getStatusColumnName();
 public void setCreated(java.sql.Date p0);
 public void setDescription(java.lang.String p0);
 public void setExpires(java.sql.Date p0);
 public void setExtraAttribute(java.lang.String p0);
 public void setStatus(java.lang.String p0);
}
