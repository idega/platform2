package se.idega.idegaweb.commune.accounting.regulations.data;


public interface ActivityTypeHome extends com.idega.data.IDOHome
{
 public ActivityType create() throws javax.ejb.CreateException;
 public ActivityType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ActivityType findActivityType(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllActivityTypes()throws javax.ejb.FinderException;

}