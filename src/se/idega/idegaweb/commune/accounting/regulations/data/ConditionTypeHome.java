package se.idega.idegaweb.commune.accounting.regulations.data;


public interface ConditionTypeHome extends com.idega.data.IDOHome
{
 public ConditionType create() throws javax.ejb.CreateException;
 public ConditionType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllConditionTypes()throws javax.ejb.FinderException;
 public ConditionType findConditionType(int p0)throws javax.ejb.FinderException;

}