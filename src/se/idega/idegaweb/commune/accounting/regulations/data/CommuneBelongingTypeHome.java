package se.idega.idegaweb.commune.accounting.regulations.data;


public interface CommuneBelongingTypeHome extends com.idega.data.IDOHome
{
 public CommuneBelongingType create() throws javax.ejb.CreateException;
 public CommuneBelongingType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllCommuneBelongingTypes()throws javax.ejb.FinderException;
 public CommuneBelongingType findCommuneBelongingType(int p0)throws javax.ejb.FinderException;

}