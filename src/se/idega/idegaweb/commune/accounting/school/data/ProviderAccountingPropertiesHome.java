package se.idega.idegaweb.commune.accounting.school.data;


public interface ProviderAccountingPropertiesHome extends com.idega.data.IDOHome
{
 public ProviderAccountingProperties create() throws javax.ejb.CreateException;
 public ProviderAccountingProperties findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}