package com.idega.block.school.data;


public class SchoolClassHomeImpl extends com.idega.data.IDOFactory implements SchoolClassHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolClass.class;
 }


 public SchoolClass create() throws javax.ejb.CreateException{
  return (SchoolClass) super.createIDO();
 }
 
public SchoolClass findBySchoolClassNameSchoolSchoolYearSchoolSeason(java.lang.String p0,com.idega.block.school.data.School p1,com.idega.block.school.data.SchoolYear p2,com.idega.block.school.data.SchoolSeason p3)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolClassBMPBean)entity).ejbFindBySchoolClassNameSchoolSchoolYearSchoolSeason(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public SchoolClass findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolClass) super.findByPrimaryKeyIDO(pk);
 }



}