package se.idega.idegaweb.commune.accounting.regulations.data;


public interface MainRuleHome extends com.idega.data.IDOHome
{
 public MainRule create() throws javax.ejb.CreateException;
 public MainRule findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllMainRules()throws javax.ejb.FinderException;
 public MainRule findMainRule(int p0)throws javax.ejb.FinderException;

}