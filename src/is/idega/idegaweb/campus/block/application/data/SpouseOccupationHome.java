package is.idega.idegaweb.campus.block.application.data;


public interface SpouseOccupationHome extends com.idega.data.IDOHome
{
 public SpouseOccupation create() throws javax.ejb.CreateException;
 public SpouseOccupation findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;

}