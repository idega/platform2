package is.idega.idegaweb.campus.block.application.data;

import java.util.Collection;

import javax.ejb.FinderException;


public interface CampusApplicationHome extends com.idega.data.IDOHome
{
 public CampusApplication create() throws javax.ejb.CreateException;
 public CampusApplication findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;
 public java.util.Collection findAllByApplicationId(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findBySQL(java.lang.String p0)throws javax.ejb.FinderException;
 public Collection findBySubjectAndStatus(Integer subjectID,String status,String order)throws FinderException;
}