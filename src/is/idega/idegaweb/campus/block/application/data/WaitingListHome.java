package is.idega.idegaweb.campus.block.application.data;


public interface WaitingListHome extends com.idega.data.IDOHome
{
 public WaitingList create() throws javax.ejb.CreateException;
 public WaitingList createLegacy();
 public WaitingList findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public WaitingList findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public WaitingList findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;
 public java.util.Collection findByApartmentTypeAndComplex(int p0,int p1,java.lang.String p2)throws javax.ejb.FinderException;

}