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
 public java.util.Collection findBySubjectAndStatus(java.lang.Integer p0,java.lang.String p1,java.lang.String p2)throws javax.ejb.FinderException;
 public java.util.Collection findBySubjectAndStatus(java.lang.Integer p0,java.lang.String p1,java.lang.String p2,int p3,int p4)throws javax.ejb.FinderException;
 public int getCountBySubjectAndStatus(java.lang.Integer p0,java.lang.String p1)throws com.idega.data.IDORelationshipException,com.idega.data.IDOException;
 public Collection findByApartmentTypeAndComplex(Integer typeId,	Integer complexID) throws FinderException;
}