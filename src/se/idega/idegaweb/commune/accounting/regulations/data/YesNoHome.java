package se.idega.idegaweb.commune.accounting.regulations.data;


public interface YesNoHome extends com.idega.data.IDOHome
{
 public YesNo create() throws javax.ejb.CreateException;
 public YesNo findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllYesNoValues()throws javax.ejb.FinderException;
 public YesNo findYesNoValue(int p0)throws javax.ejb.FinderException;

}