package com.idega.block.school.data;


public class SchoolClassMemberHomeImpl extends com.idega.data.IDOFactory implements SchoolClassMemberHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolClassMember.class;
 }


 public SchoolClassMember create() throws javax.ejb.CreateException{
  return (SchoolClassMember) super.createIDO();
 }


public SchoolClassMember findByUserAndSchoolClass(com.idega.user.data.User p0,com.idega.block.school.data.SchoolClass p1)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolClassMemberBMPBean)entity).ejbFindByUserAndSchoolClass(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public SchoolClassMember findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolClassMember) super.findByPrimaryKeyIDO(pk);
 }



}