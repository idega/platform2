package se.idega.idegaweb.commune.childcare.check.data;


public interface GrantedCheckHome extends com.idega.data.IDOHome
{
 public GrantedCheck create() throws javax.ejb.CreateException;
 public GrantedCheck findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findChecks()throws javax.ejb.FinderException;
 public GrantedCheck findChecksByUser(com.idega.user.data.User p0)throws javax.ejb.FinderException;
 public GrantedCheck findChecksByUser(int p0)throws javax.ejb.FinderException;

}