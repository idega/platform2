package is.idega.idegaweb.campus.block.application.data;


public interface PriorityHome extends com.idega.data.IDOHome
{
 public Priority create() throws javax.ejb.CreateException;
 public Priority findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;

}