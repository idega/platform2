package se.idega.idegaweb.commune.care.check.data;


public interface Check extends com.idega.data.IDOEntity,com.idega.block.process.data.Case
{
 public int getAmount();
 public java.lang.String getCaseCodeDescription();
 public java.lang.String getCaseCodeKey();
 public java.lang.String[] getCaseStatusDescriptions();
 public java.lang.String[] getCaseStatusKeys();
 public int getCheckFee();
 public int getChildCareType();
 public int getChildId();
 public int getManagerId();
 public int getMethod();
 public java.lang.String getMotherToungueFatherChild();
 public java.lang.String getMotherToungueMotherChild();
 public java.lang.String getMotherToungueParents();
 public java.lang.String getNotes();
 public boolean getRule1();
 public boolean getRule2();
 public boolean getRule3();
 public boolean getRule4();
 public boolean getRule5();
 public java.lang.String getUserNotes();
 public int getWorkSituation1();
 public int getWorkSituation2();
 public void initializeAttributes();
 public void setAmount(int p0);
 public void setCheckFee(int p0);
 public void setChildCareType(int p0);
 public void setChildId(int p0);
 public void setManagerId(int p0);
 public void setMethod(int p0);
 public void setMotherTongueFatherChild(java.lang.String p0);
 public void setMotherTongueMotherChild(java.lang.String p0);
 public void setMotherTongueParents(java.lang.String p0);
 public void setNotes(java.lang.String p0);
 public void setRule1(boolean p0);
 public void setRule2(boolean p0);
 public void setRule3(boolean p0);
 public void setRule4(boolean p0);
 public void setRule5(boolean p0);
 public void setUserNotes(java.lang.String p0);
 public void setWorkSituation1(int p0);
 public void setWorkSituation2(int p0);
}
