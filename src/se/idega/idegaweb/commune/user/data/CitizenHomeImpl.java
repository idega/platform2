package se.idega.idegaweb.commune.user.data;

import com.idega.block.school.data.SchoolSeason;


public class CitizenHomeImpl extends com.idega.data.IDOFactory implements CitizenHome
{
 protected Class getEntityInterfaceClass(){
  return Citizen.class;
 }


 public Citizen create() throws javax.ejb.CreateException{
  return (Citizen) super.createIDO();
 }


public java.util.Collection findAllCitizensRegisteredToSchool(java.sql.Date p0,java.sql.Date p1,java.sql.Date p2)throws javax.ejb.FinderException,com.idega.data.IDOLookupException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CitizenBMPBean)entity).ejbFindAllCitizensRegisteredToSchool(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findCitizensNotAssignedToAnyClassOnGivenDate(com.idega.user.data.Group p0,SchoolSeason p1, java.sql.Date p2,java.sql.Date p3,java.sql.Date p4)throws com.idega.data.IDOLookupException,javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((CitizenBMPBean)entity).ejbFindCitizensNotAssignedToAnyClassOnGivenDate(p0,p1,p2,p3,p4);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Citizen findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Citizen) super.findByPrimaryKeyIDO(pk);
 }



}