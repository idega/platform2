package com.idega.block.school.data;


public class SchoolStudyPathHomeImpl extends com.idega.data.IDOFactory implements SchoolStudyPathHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolStudyPath.class;
 }


 public SchoolStudyPath create() throws javax.ejb.CreateException{
  return (SchoolStudyPath) super.createIDO();
 }


public java.util.Collection findAllByIDs(java.lang.String[] p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolStudyPathBMPBean)entity).ejbFindAllByIDs(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllStudyPaths()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolStudyPathBMPBean)entity).ejbFindAllStudyPaths();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllStudyPathsByCodeLength(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolStudyPathBMPBean)entity).ejbFindAllStudyPathsByCodeLength(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public SchoolStudyPath findByCode(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolStudyPathBMPBean)entity).ejbFindByCode(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public SchoolStudyPath findByCodeAndSchoolType(java.lang.String p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolStudyPathBMPBean)entity).ejbFindByCodeAndSchoolType(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public java.util.Collection findBySchool(com.idega.block.school.data.School p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolStudyPathBMPBean)entity).ejbFindBySchool(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBySchoolAndSchoolCategory(com.idega.block.school.data.School p0,com.idega.block.school.data.SchoolCategory p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolStudyPathBMPBean)entity).ejbFindBySchoolAndSchoolCategory(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBySchoolCategory(com.idega.block.school.data.SchoolCategory p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolStudyPathBMPBean)entity).ejbFindBySchoolCategory(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBySchoolType(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolStudyPathBMPBean)entity).ejbFindBySchoolType(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBySchoolTypes(java.lang.String[] p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolStudyPathBMPBean)entity).ejbFindBySchoolTypes(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBySchoolTypes(java.util.Collection p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolStudyPathBMPBean)entity).ejbFindBySchoolTypes(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findStudyPaths(com.idega.block.school.data.School p0,java.util.Collection p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolStudyPathBMPBean)entity).ejbFindStudyPaths(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findStudyPaths(com.idega.block.school.data.School p0)throws com.idega.data.IDORelationshipException,javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolStudyPathBMPBean)entity).ejbFindStudyPaths(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findStudyPaths(com.idega.block.school.data.School p0,java.lang.Object p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolStudyPathBMPBean)entity).ejbFindStudyPaths(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public SchoolStudyPath findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolStudyPath) super.findByPrimaryKeyIDO(pk);
 }



}