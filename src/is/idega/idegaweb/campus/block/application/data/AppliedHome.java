package is.idega.idegaweb.campus.block.application.data;


public interface AppliedHome extends com.idega.data.IDOHome
{
 public Applied create() throws javax.ejb.CreateException;
 public Applied findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;
 public java.util.Collection findByApplicationID(java.lang.Integer p0)throws javax.ejb.FinderException;
 public java.util.Collection findBySQL(java.lang.String p0)throws javax.ejb.FinderException;

}