package se.idega.idegaweb.commune.accounting.regulations.data;


public interface Condition extends com.idega.data.IDOEntity
{
 public int getConditionID();
 public int getIndex();
 public int getIntervalID();
 public int getRegulationID();
 public void initializeAttributes();
 public void setConditionID(int p0);
 public void setIndex(int p0);
 public void setIntervalID(int p0);
 public void setRegulationID(int p0);
}
