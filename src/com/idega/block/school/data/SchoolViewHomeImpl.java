package com.idega.block.school.data;


public class SchoolViewHomeImpl extends com.idega.data.IDOFactory implements SchoolViewHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolView.class;
 }


 public SchoolView create() throws javax.ejb.CreateException{
  return (SchoolView) super.createIDO();
 }


public java.util.Collection findAllBySchoolCategory(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolViewBMPBean)entity).ejbFindAllBySchoolCategory(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public SchoolView findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolView) super.findByPrimaryKeyIDO(pk);
 }



}