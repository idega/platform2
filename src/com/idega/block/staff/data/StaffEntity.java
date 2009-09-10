package com.idega.block.staff.data;


public interface StaffEntity extends com.idega.data.IDOLegacyEntity
{
 public void delete()throws java.sql.SQLException;
 public java.sql.Date getBeganWork();
 public java.lang.String getIDColumnName();
 public int getImageID();
 public void setBeganWork(java.sql.Date p0);
 public void setDefaultValues();
 public void setImageID(int p0);
}
