package se.idega.idegaweb.commune.user.data;


public interface CitizenHome extends com.idega.data.IDOHome
{
 public Citizen create() throws javax.ejb.CreateException;
 public Citizen findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllCitizensRegisteredToChildCare(java.sql.Date p0,java.sql.Date p1,java.sql.Date p2,java.sql.Date p3)throws javax.ejb.FinderException,com.idega.data.IDOLookupException;
 public java.util.Collection findAllCitizensRegisteredToSchool(com.idega.block.school.data.SchoolSeason p0,java.sql.Date p1,java.sql.Date p2)throws javax.ejb.FinderException,com.idega.data.IDOLookupException;

}