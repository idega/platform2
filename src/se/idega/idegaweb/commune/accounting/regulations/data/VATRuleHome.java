package se.idega.idegaweb.commune.accounting.regulations.data;


public interface VATRuleHome extends com.idega.data.IDOHome
{
 public VATRule create() throws javax.ejb.CreateException;
 public VATRule findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllVATRules()throws javax.ejb.FinderException;
 public VATRule findVATRule(int p0)throws javax.ejb.FinderException;

}