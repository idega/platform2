package se.idega.idegaweb.commune.care.data;


public interface ProviderTypeHome extends com.idega.data.IDOHome
{
 public ProviderType create() throws javax.ejb.CreateException;
 public ProviderType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;
 public ProviderType findCommuneType()throws javax.ejb.FinderException;
 public ProviderType findPrivateType()throws javax.ejb.FinderException;

}