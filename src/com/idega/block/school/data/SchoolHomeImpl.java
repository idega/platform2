package com.idega.block.school.data;

import java.util.Collection;

public class SchoolHomeImpl extends com.idega.data.IDOFactory implements SchoolHome
{
 protected Class getEntityInterfaceClass(){
  return School.class;
 }


 public School create() throws javax.ejb.CreateException{
  return (School) super.createIDO();
 }


public Collection findAllByAreaAndType(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Collection ids = ((SchoolBMPBean)entity).ejbFindAllByAreaAndType(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public Collection findAllByAreaAndTypeAndYear(int p0,int p1,int p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Collection ids = ((SchoolBMPBean)entity).ejbFindAllByAreaAndTypeAndYear(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public Collection findAllByAreaAndTypes(int p0,Collection p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Collection ids = ((SchoolBMPBean)entity).ejbFindAllByAreaAndTypes(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public Collection findAllBySubAreaAndTypes(int p0,Collection p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Collection ids = ((SchoolBMPBean)entity).ejbFindAllBySubAreaAndTypes(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public Collection findAllByAreaTypeCommune(int p0,int p1,int p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Collection ids = ((SchoolBMPBean)entity).ejbFindAllByAreaTypeCommune(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public Collection findAllByAreaTypeManagement(int p0,int p1,java.lang.String p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Collection ids = ((SchoolBMPBean)entity).ejbFindAllByAreaTypeManagement(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public Collection findAllByAreaTypeManagementCommune(int p0,int p1,Collection p2,int p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Collection ids = ((SchoolBMPBean)entity).ejbFindAllByAreaTypeManagementCommune(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public Collection findAllByAreaTypeManagementCommune(int p0,int p1,java.lang.String p2,int p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Collection ids = ((SchoolBMPBean)entity).ejbFindAllByAreaTypeManagementCommune(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public Collection findAllByAreaTypeManagementCommune(int p0,Collection p1,Collection p2,int p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Collection ids = ((SchoolBMPBean)entity).ejbFindAllByAreaTypeManagementCommune(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public Collection findAllByCategory(com.idega.block.school.data.SchoolCategory p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Collection ids = ((SchoolBMPBean)entity).ejbFindAllByCategory(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public Collection findAllBySchoolArea(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Collection ids = ((SchoolBMPBean)entity).ejbFindAllBySchoolArea(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public Collection findAllBySchoolGroup(com.idega.user.data.Group p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Collection ids = ((SchoolBMPBean)entity).ejbFindAllBySchoolGroup(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public Collection findAllBySchoolName(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Collection ids = ((SchoolBMPBean)entity).ejbFindAllBySchoolName(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public Collection findAllBySchoolType(Collection p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Collection ids = ((SchoolBMPBean)entity).ejbFindAllBySchoolType(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public Collection findAllBySchoolType(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Collection ids = ((SchoolBMPBean)entity).ejbFindAllBySchoolType(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public Collection findAllCentralizedAdministrated()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Collection ids = ((SchoolBMPBean)entity).ejbFindAllCentralizedAdministrated();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public Collection findAllCentralizedAdministratedByType(Collection p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Collection ids = ((SchoolBMPBean)entity).ejbFindAllCentralizedAdministratedByType(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public Collection findAllInHomeCommuneByCategory(com.idega.block.school.data.SchoolCategory p0)throws com.idega.data.IDOLookupException,javax.ejb.EJBException,javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Collection ids = ((SchoolBMPBean)entity).ejbFindAllInHomeCommuneByCategory(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public Collection findAllPrivate()throws com.idega.data.IDOLookupException,javax.ejb.EJBException,javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Collection ids = ((SchoolBMPBean)entity).ejbFindAllPrivate();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public Collection findAllSchools()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Collection ids = ((SchoolBMPBean)entity).ejbFindAllSchools();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public Collection findAllSchoolsByCategoryIncludingTerminated(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Collection ids = ((SchoolBMPBean)entity).ejbFindAllSchoolsByCategoryIncludingTerminated(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public Collection findAllSchoolsIncludingTerminated()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Collection ids = ((SchoolBMPBean)entity).ejbFindAllSchoolsIncludingTerminated();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public School findBySchoolName(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolBMPBean)entity).ejbFindBySchoolName(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public School findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (School) super.findByPrimaryKeyIDO(pk);
 }

 public Collection findAllByPrimaryKeys(Collection p0) throws javax.ejb.FinderException{
  return getEntityCollectionForPrimaryKeys(p0);
 }


public int getNumberOfFreetimeTypes(int p0)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((SchoolBMPBean)entity).ejbHomeGetNumberOfFreetimeTypes(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOfRelations(com.idega.block.school.data.School p0,com.idega.block.school.data.SchoolYear p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((SchoolBMPBean)entity).ejbHomeGetNumberOfRelations(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}
