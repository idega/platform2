package se.idega.idegaweb.commune.accounting.regulations.data;


public interface SpecialCalculationTypeHome extends com.idega.data.IDOHome
{
 public SpecialCalculationType create() throws javax.ejb.CreateException;
 public SpecialCalculationType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllConditionTypes()throws javax.ejb.FinderException;
 public SpecialCalculationType findConditionType(int p0)throws javax.ejb.FinderException;

}