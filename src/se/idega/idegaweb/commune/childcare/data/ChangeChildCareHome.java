package se.idega.idegaweb.commune.childcare.data;


public interface ChangeChildCareHome extends com.idega.data.IDOHome
{
 public ChangeChildCare create() throws javax.ejb.CreateException;
 public ChangeChildCare findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}