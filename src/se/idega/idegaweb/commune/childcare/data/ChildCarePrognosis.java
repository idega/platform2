package se.idega.idegaweb.commune.childcare.data;

import javax.ejb.*;

public interface ChildCarePrognosis extends com.idega.data.IDOEntity
{
 public int getOneYearPrognosis();
 public int getProviderID();
 public int getThreeMonthsPrognosis();
 public java.sql.Date getUpdatedDate();
 public void initializeAttributes();
 public void setOneYearPrognosis(int oneYearPrognosis);
 public void setProviderID(int providerID);
 public void setThreeMonthsPrognosis(int threeMonthsPrognosis);
 public void setUpdatedDate(java.sql.Date date);
}
