package is.idega.idegaweb.campus.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;


public interface AccountPhoneHome extends com.idega.data.IDOHome
{
 public AccountPhone create() throws javax.ejb.CreateException;
 public AccountPhone createLegacy();
 public AccountPhone findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public AccountPhone findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public AccountPhone findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;
 public Collection findByPhoneNumber(String number)throws FinderException;
/**
 * @return
 */
public Collection findAll()throws FinderException;
public Collection findValid(Date toDate)throws FinderException;
}