package se.idega.idegaweb.commune.care.data;


public interface ProviderStatisticsTypeHome extends com.idega.data.IDOHome
{
 public ProviderStatisticsType create() throws javax.ejb.CreateException;
 public ProviderStatisticsType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;

}