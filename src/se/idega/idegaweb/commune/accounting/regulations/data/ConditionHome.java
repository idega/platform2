package se.idega.idegaweb.commune.accounting.regulations.data;


public interface ConditionHome extends com.idega.data.IDOHome
{
 public Condition create() throws javax.ejb.CreateException;
 public Condition findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllConditions()throws javax.ejb.FinderException;
 public java.util.Collection findAllConditionsByRegulation(se.idega.idegaweb.commune.accounting.regulations.data.Regulation p0)throws javax.ejb.FinderException;
 public Condition findCondition(int p0)throws javax.ejb.FinderException;

}