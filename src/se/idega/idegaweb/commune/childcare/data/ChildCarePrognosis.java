package se.idega.idegaweb.commune.childcare.data;


public interface ChildCarePrognosis extends com.idega.data.IDOEntity
{
 public int getOneYearPriority();
 public int getOneYearPrognosis();
 public int getProviderID();
 public int getThreeMonthsPriority();
 public int getThreeMonthsPrognosis();
 public java.sql.Date getUpdatedDate();
 public void initializeAttributes();
 public void setOneYearPriority(int p0);
 public void setOneYearPrognosis(int p0);
 public void setProviderID(int p0);
 public void setThreeMonthsPriority(int p0);
 public void setThreeMonthsPrognosis(int p0);
 public void setUpdatedDate(java.sql.Date p0);
}
