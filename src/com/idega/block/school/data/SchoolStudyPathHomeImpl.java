package com.idega.block.school.data;


public class SchoolStudyPathHomeImpl extends com.idega.data.IDOFactory implements SchoolStudyPathHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolStudyPath.class;
 }


 public SchoolStudyPath create() throws javax.ejb.CreateException{
  return (SchoolStudyPath) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolStudyPathBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public SchoolStudyPath findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolStudyPath) super.findByPrimaryKeyIDO(pk);
 }



}