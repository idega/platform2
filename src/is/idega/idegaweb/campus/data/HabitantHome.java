package is.idega.idegaweb.campus.data;

import java.util.Collection;

import javax.ejb.FinderException;


public interface HabitantHome extends com.idega.data.IDOHome
{
 public Habitant create() throws javax.ejb.CreateException;
 public Habitant createLegacy();
 public Habitant findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Habitant findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Habitant findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;
 public Collection findByComplex(Integer complexID)throws FinderException;

}