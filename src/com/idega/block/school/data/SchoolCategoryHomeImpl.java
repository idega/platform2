package com.idega.block.school.data;


public class SchoolCategoryHomeImpl extends com.idega.data.IDOFactory implements SchoolCategoryHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolCategory.class;
 }


 public SchoolCategory create() throws javax.ejb.CreateException{
  return (SchoolCategory) super.createIDO();
 }


public java.util.Collection findAllCategories()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolCategoryBMPBean)entity).ejbFindAllCategories();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public SchoolCategory findByLocalizedKey(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolCategoryBMPBean)entity).ejbFindByLocalizedKey(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public SchoolCategory findChildcareCategory()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolCategoryBMPBean)entity).ejbFindChildcareCategory();
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public SchoolCategory findCollegeCategory()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolCategoryBMPBean)entity).ejbFindCollegeCategory();
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public SchoolCategory findElementarySchoolCategory()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolCategoryBMPBean)entity).ejbFindElementarySchoolCategory();
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public SchoolCategory findHighSchoolCategory()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolCategoryBMPBean)entity).ejbFindHighSchoolCategory();
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public SchoolCategory findMusicSchoolCategory()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolCategoryBMPBean)entity).ejbFindMusicSchoolCategory();
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public SchoolCategory findUniversityCategory()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolCategoryBMPBean)entity).ejbFindUniversityCategory();
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public SchoolCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolCategory) super.findByPrimaryKeyIDO(pk);
 }



}