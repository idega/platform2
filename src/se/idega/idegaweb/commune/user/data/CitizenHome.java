package se.idega.idegaweb.commune.user.data;

import com.idega.block.school.data.SchoolSeason;


public interface CitizenHome extends com.idega.data.IDOHome
{
 public Citizen create() throws javax.ejb.CreateException;
 public Citizen findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllCitizensRegisteredToSchool(java.sql.Date p0,java.sql.Date p1,java.sql.Date p2)throws javax.ejb.FinderException,com.idega.data.IDOLookupException;
 public java.util.Collection findCitizensNotAssignedToAnyClassOnGivenDate(com.idega.user.data.Group p0,SchoolSeason p1, java.sql.Date p2,java.sql.Date p3,java.sql.Date p4)throws com.idega.data.IDOException,com.idega.data.IDOLookupException,javax.ejb.FinderException;

}