package is.idega.idegaweb.campus.block.phone.data;

import java.util.Collection;

import javax.ejb.FinderException;


public interface CampusPhoneHome extends com.idega.data.IDOHome
{
 public CampusPhone create() throws javax.ejb.CreateException;
 public CampusPhone findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public CampusPhone findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Collection findAll()throws FinderException;
 public Collection findByPhoneNumber(String number)throws FinderException;
}