package com.idega.block.email.data;


public interface MailGroup extends com.idega.block.category.data.CategoryEntity,com.idega.block.email.business.EmailGroup
{
 public java.sql.Timestamp getCreated();
 public java.lang.String getDescription();
 public java.lang.String getName();
 public void setCreated(java.sql.Timestamp p0);
 public void setDescription(java.lang.String p0);
 public void setName(java.lang.String p0);
}
