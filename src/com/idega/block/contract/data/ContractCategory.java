package com.idega.block.contract.data;


public interface ContractCategory extends com.idega.data.IDOEntity
{
 public java.sql.Date getCreationDate();
 public java.lang.String getDescription();
 public java.lang.String getName();
 public java.lang.String getNewsCategoryName();
 public java.util.Collection getRelatedObjectInstances()throws javax.ejb.FinderException,com.idega.data.IDORelationshipException;
 public boolean getValid();
 public java.lang.String getXMLTemplate();
 public void initializeAttributes();
 public void setCategoryName(java.lang.String p0);
 public void setCreationDate(java.sql.Date p0);
 public void setDescription(java.lang.String p0);
 public void setName(java.lang.String p0);
 public void setValid(boolean p0);
 public void setXMLTemplate(java.lang.String p0);
}
