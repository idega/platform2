package is.idega.idegaweb.campus.block.application.data;


public interface CurrentResidencyHome extends com.idega.data.IDOHome
{
 public CurrentResidency create() throws javax.ejb.CreateException;
 public CurrentResidency findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;

}